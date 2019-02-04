package master.naucnacentrala.repository;

import master.naucnacentrala.model.Kupovina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KupovinaRepository extends JpaRepository<Kupovina, Long> {
}
