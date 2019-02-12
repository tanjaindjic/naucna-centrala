/*
package master.naucnacentrala.controller;

import com.google.gson.Gson;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.dto.*;
import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.model.elastic.RecenzentIndexUnit;
import master.naucnacentrala.model.elastic.RecenzijaIndexUnit;
import master.naucnacentrala.repository.RadIndexingUnitRepository;
import master.naucnacentrala.repository.RecenzentIndexUnitRepository;
import master.naucnacentrala.service.RadService;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private Client nodeClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private RadService radService;

    @Autowired
    private RecenzentIndexUnitRepository recenzentIndexUnitRepository;

    @Autowired
    private RadIndexingUnitRepository riuRepository;

    private HighlightBuilder highlightBuilder = new HighlightBuilder()
            .field("sadrzaj", 50)
            .field("naslov", 50)
            .field("autor", 50)
            .field("koautori", 50)
            .field("apstrakt", 50)
            .field("kljucniPojmovi", 50)
            .field("casopis", 50)
            .field("naucnaOblast", 50);

    @PostMapping(value="/basicQuery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity basicQuery(@RequestBody String query) throws ParseException {

        System.out.println("Query: " + query);

        ArrayList<BasicQueryResponseDTO> retVal = new ArrayList<>();

        highlightBuilder.highlightQuery(QueryBuilders.queryStringQuery( query));

        SearchRequestBuilder request = nodeClient.prepareSearch("naucnirad")
                .setTypes("pdf")
                .setQuery(QueryBuilders.queryStringQuery( query))
                .setSearchType(SearchType.DEFAULT)
                .highlighter(highlightBuilder);
        SearchResponse response = request.get();
        System.out.println(response.toString());
        retVal = getResponse(response);

        return new ResponseEntity(retVal, HttpStatus.OK);

    }

    @PostMapping(value="/advancedQuery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity advancedQuery(@RequestBody ComplexQueryDTO queryDTO){

        System.out.println("primio u advanced search: " + queryDTO.toString());

        List<AdvancedQueryDTO> query = queryDTO.getUpiti();
        ArrayList<BasicQueryResponseDTO> retVal = new ArrayList<>();

        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        if(!queryDTO.getNaucneOblasti().isEmpty())
            for (String obl : queryDTO.getNaucneOblasti()) {
                MatchPhraseQueryBuilder mpqb = new MatchPhraseQueryBuilder("naucnaoblast", obl);
                bqb.must(mpqb);
            }
        for(AdvancedQueryDTO dto : query){
            if(dto.getUpit()!=null && dto.getUpit()!="")
                if(dto.getOperator().equals("I")) {

                    if (dto.getFraza()) {  //ako je fraza
                        if(dto.getZona().equals("sve")) //ako po svim poljima
                            bqb.must(QueryBuilders.multiMatchQuery(dto.getUpit(), "naslov", "sadrzaj", "autor",
                                "koautori", "apstrakt", "kljucnipojmovi", "casopis", "naucnaoblast").type("phrase"));
                        else //ako po odredjenom polju
                            bqb.must(QueryBuilders.matchPhraseQuery(dto.getZona().toLowerCase(), dto.getUpit()));
                    } else { //nije fraza
                        if(dto.getZona().equals("sve")) //ako po svim poljima
                            bqb.must(QueryBuilders.multiMatchQuery(dto.getUpit(), "naslov", "sadrzaj", "autor",
                                    "koautori", "apstrakt", "kljucnipojmovi", "casopis", "naucnaoblast"));
                        else //ako po odredjenom polju
                            bqb.must(QueryBuilders.matchQuery(dto.getZona().toLowerCase(), dto.getUpit()));

                    }

                } else if(dto.getOperator().equals("ILI"))   {
                    if (dto.getFraza()) {
                        if(dto.getZona().equals("sve"))
                            bqb.should(QueryBuilders.multiMatchQuery(dto.getUpit(), "naslov", "sadrzaj", "autor",
                                "koautori", "apstrakt", "kljucnipojmovi", "casopis", "naucnaoblast").type("phrase"));
                        else bqb.should(QueryBuilders.matchPhraseQuery(dto.getZona().toLowerCase(), dto.getUpit()));
                    } else {
                        if(dto.getZona().equals("sve")) //ako po svim poljima
                            bqb.should(QueryBuilders.multiMatchQuery(dto.getUpit(), "naslov", "sadrzaj", "autor",
                                    "koautori", "apstrakt", "kljucnipojmovi", "casopis", "naucnaoblast"));
                        else //ako po odredjenom polju
                            bqb.should(QueryBuilders.matchQuery(dto.getZona().toLowerCase(), dto.getUpit()));

                    }
                }
        }

        highlightBuilder.highlightQuery(bqb);
        SearchRequestBuilder request = nodeClient.prepareSearch("naucnirad")
                .setTypes("pdf")
                .setQuery(bqb)
                .setSearchType(SearchType.DEFAULT)
                .highlighter(highlightBuilder);
        System.out.println(request);
        SearchResponse response = request.get();
        System.out.println(response.toString());
        retVal = getResponse(response);
        System.out.println(retVal.size());

        return new ResponseEntity(retVal, HttpStatus.OK);
    }

    @PostMapping(value="/recenzentQuery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity recenzentQuery(@RequestBody RecenzentQueryDTO recenzentQueryDTO){
        System.out.println("primio " + recenzentQueryDTO.toString());

        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        Rad r=radService.getRad(recenzentQueryDTO.getIdRada());
        List<RecenzentIndexUnit> retval = new ArrayList<>();

        if(!recenzentQueryDTO.getNaucneOblasti().isEmpty()) {

            for (String obl : recenzentQueryDTO.getNaucneOblasti()) {
                MatchPhraseQueryBuilder mpqb = new MatchPhraseQueryBuilder("naucneOblasti", obl);
                bqb.must(mpqb);
            }

        }
        if(recenzentQueryDTO.getUdaljenost()!=null){
            GeoDistanceQueryBuilder gdqb = new GeoDistanceQueryBuilder("lokacija");
            gdqb.distance(recenzentQueryDTO.getUdaljenost(), DistanceUnit.KILOMETERS);
            gdqb.point(r.getAutor().getLat(), r.getAutor().getLon());
            bqb.mustNot(gdqb);
        }
        if(recenzentQueryDTO.isMoreLikeThis()){
            String[] likeThis = new String[1];
            likeThis[0]=getSadrzaj(r);
            System.out.println(likeThis[0]);
            String[] fields = new String[1];
            fields[0] = "sadrzaj";
            MoreLikeThisQueryBuilder mltqb=new MoreLikeThisQueryBuilder(fields, likeThis,null );
            mltqb.maxQueryTerms(10);
            mltqb.minTermFreq(1);
            mltqb.minimumShouldMatch("65%");
            mltqb.minDocFreq(1);
            mltqb.analyzer("serbian-analyzer");
            bqb.must(mltqb);
        }

        SearchRequestBuilder request = nodeClient.prepareSearch("naucnicasopis_recenzija")
                .setTypes("recenzija")
                .setQuery(bqb)
                .setSearchType(SearchType.DEFAULT);
        request.setFetchSource(null, "sadrzaj");
        System.out.println(request);
        SearchResponse response = request.get();
        System.out.println(response.toString());
        for(SearchHit hit : response.getHits().getHits()) {
            Gson gson = new Gson();
            RecenzentIndexUnit rec = recenzentIndexUnitRepository.findById(gson.fromJson(hit.getSourceAsString(), RecenzijaIndexUnit.class).getIdRecenzenta()).get();
            if(!retval.stream().map(RecenzentIndexUnit::getId).filter(rec.getId()::equals).findFirst().isPresent())
                retval.add(rec);
        }
        System.out.println(retval);
        return new ResponseEntity(retval, HttpStatus.OK);
    }

    private ArrayList<BasicQueryResponseDTO> getResponse(SearchResponse response){
        ArrayList<BasicQueryResponseDTO> retVal = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()) {
            Gson gson = new Gson();
            BasicQueryResponseDTO basicQueryResponseDTO = new BasicQueryResponseDTO();

            RadIndexUnit object = gson.fromJson(hit.getSourceAsString(), RadIndexUnit.class);
            basicQueryResponseDTO.setRadIndexUnit(object);

            //sve generisane highlightse vracam kao jedan veliki string da ne opterecujem front budzenjem prikaza
            String allHighlights = "...";

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()){
                //ne moze da se smesti direktno HighlightField jer je vrednost tipa Text[] i pukne mi serializer
                String value = Arrays.toString(entry.getValue().fragments());
                //moram substring jer vraca uglaste zagrade fragmenata na pocetku i kraju
                allHighlights+=value.substring(1, value.length()-1);
                allHighlights+="...";

            }

            allHighlights = allHighlights.replace("<em>", "<b>");
            allHighlights = allHighlights.replace("</em>", "</b>");
            System.out.println(allHighlights);
            basicQueryResponseDTO.setHighlight(allHighlights);
            retVal.add(basicQueryResponseDTO);
        }
        return retVal;
    }
    private String getSadrzaj(Rad rad){
        ClassLoader classLoader = getClass().getClassLoader();
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        System.out.println(rad.getAdresaNacrta());
        File file = new File(classLoader.getResource(rad.getAdresaNacrta()).getFile());
        String parsedText = "";
        try {
            // PDFBox 2.0.8 require org.apache.pdfbox.io.RandomAccessRead
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);
            parsedText = pdfStripper.getText(pdDoc);
            pdDoc.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parsedText;
    }

}
*/
