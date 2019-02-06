package master.naucnacentrala.controller;

import com.google.gson.Gson;
import master.naucnacentrala.model.dto.AdvancedQueryDTO;
import master.naucnacentrala.model.dto.BasicQueryResponseDTO;
import master.naucnacentrala.model.dto.ComplexQueryDTO;
import master.naucnacentrala.model.dto.HighlightDTO;
import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.repository.RadIndexingUnitRepository;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private Client nodeClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
            bqb.must(QueryBuilders.termsQuery("naucnaOblast", queryDTO.getNaucneOblasti()));
        for(AdvancedQueryDTO dto : query){
            if(dto.getOperator().equals("I")) {
                if (dto.getFraza()) {
                    if(dto.getZona().equals("sve"))
                        bqb.must(QueryBuilders.multiMatchQuery(dto.getUpit(), "naslov", "sadrzaj", "autor",
                            "koautori", "apstrakt", "kljucniPojmovi", "casopis", "naucnaOblast").type("phrase"));
                    else bqb.must(QueryBuilders.matchPhraseQuery(dto.getZona().toLowerCase(), dto.getUpit()));
                } else bqb.must(QueryBuilders.queryStringQuery(dto.getUpit()));

            } else if(dto.getOperator().equals("ILI"))   {
                if (dto.getFraza()) {
                    if(dto.getZona().equals("sve"))
                        bqb.should(QueryBuilders.multiMatchQuery(dto.getUpit(), "naslov", "sadrzaj", "autor",
                            "koautori", "apstrakt", "kljucniPojmovi", "casopis", "naucnaOblast").type("phrase"));
                    else bqb.should(QueryBuilders.matchPhraseQuery(dto.getZona().toLowerCase(), dto.getUpit()));
                } else bqb.should(QueryBuilders.queryStringQuery(dto.getUpit()));
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
}
