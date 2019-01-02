package master.naucnacentrala.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import master.naucnacentrala.model.elastic.RadIndexUnit;

public interface RadIndexingUnitRepository extends ElasticsearchRepository<RadIndexUnit, Long> {

}
