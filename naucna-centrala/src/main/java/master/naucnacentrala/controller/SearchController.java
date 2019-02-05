package master.naucnacentrala.controller;

import com.google.gson.Gson;
import master.naucnacentrala.model.dto.BasicQueryResponseDTO;
import master.naucnacentrala.model.dto.HighlightDTO;
import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.repository.RadIndexingUnitRepository;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
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

    @PostMapping(value="/basicQuery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity basicQuery(@RequestBody String query) throws ParseException {

        System.out.println("Query: " + query);
        ArrayList<BasicQueryResponseDTO> retVal = new ArrayList<>();
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("sadrzaj")
                .highlightQuery(QueryBuilders.queryStringQuery( query));


        SearchRequestBuilder request = nodeClient.prepareSearch("naucnirad")
                .setTypes("pdf")
                .setQuery(QueryBuilders.queryStringQuery( query))
                .setSearchType(SearchType.DEFAULT)
                .highlighter(highlightBuilder);
        SearchResponse response = request.get();
        System.out.println(response.toString());
        for(SearchHit hit : response.getHits().getHits()) {
            Gson gson = new Gson();
            BasicQueryResponseDTO basicQueryResponseDTO = new BasicQueryResponseDTO();

            RadIndexUnit object = gson.fromJson(hit.getSourceAsString(), RadIndexUnit.class);
            basicQueryResponseDTO.setRadIndexUnit(object);

            List<HighlightDTO> fields = new ArrayList<>();

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()){
                //ne moze da se smesti direktno HighlightField jer je vrednost tipa Text[] i pukne mi serializer
                String value = Arrays.toString(entry.getValue().fragments());
                //moram substring jer vraca uglaste zagrade fragmenata na pocetku i kraju
                HighlightDTO highlightField = new HighlightDTO(entry.getKey(), value.substring(1, value.length()-1));
                fields.add(highlightField);
            }


            basicQueryResponseDTO.setHighlight(fields);
            retVal.add(basicQueryResponseDTO);
        }

        return new ResponseEntity(retVal, HttpStatus.OK);

    }
}
