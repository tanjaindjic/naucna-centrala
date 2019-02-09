package master.naucnacentrala.repository;

import master.naucnacentrala.model.elastic.RecenzijaIndexUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface RecenzijaIndexUnitRepository extends ElasticsearchRepository<RecenzijaIndexUnit, Long> {

}
