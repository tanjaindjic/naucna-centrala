package master.naucnacentrala.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import master.naucnacentrala.model.elastic.RecenzentIndexUnit;

public interface RecenzentIndexUnitRepository extends ElasticsearchRepository<RecenzentIndexUnit, Long> {

}
