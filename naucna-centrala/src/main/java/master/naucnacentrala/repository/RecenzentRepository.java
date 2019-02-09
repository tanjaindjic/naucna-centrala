package master.naucnacentrala.repository;

import master.naucnacentrala.model.korisnici.Recenzent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecenzentRepository extends JpaRepository<Recenzent, Long> {
}
