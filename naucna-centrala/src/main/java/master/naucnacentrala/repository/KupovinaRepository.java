package master.naucnacentrala.repository;

import master.naucnacentrala.model.Kupovina;
import org.springframework.data.jpa.repository.JpaRepository;
import master.naucnacentrala.model.korisnici.Korisnik;

import java.util.List;

public interface KupovinaRepository extends JpaRepository<Kupovina, Long> {
    List<Kupovina> findByKorisnik(Korisnik k);
}
