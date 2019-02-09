package master.naucnacentrala.repository;

import master.naucnacentrala.model.elastic.RecenzentIndexUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecenzentIndexUnitRepository extends ElasticsearchRepository<RecenzentIndexUnit, Long> {
}
