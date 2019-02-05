package master.naucnacentrala.controller;

import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.repository.RadIndexingUnitRepository;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private Client nodeClient;

    @Autowired
    private RadIndexingUnitRepository riuRepository;

    @PostMapping(value="/basicQuery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity basicQuery(@RequestBody String query){

        System.out.println("Query: " + query);
        Iterable<RadIndexUnit> res = riuRepository.search(QueryBuilders.boolQuery()
                .must(queryStringQuery(query)));
        ArrayList<RadIndexUnit> retVal = new ArrayList<>();
        if(res!=null){
            for(RadIndexUnit r0 : res) {
                retVal.add(r0);
                System.out.println(r0.toString());
            }
        }

        return new ResponseEntity(retVal, HttpStatus.OK);

    }
}
