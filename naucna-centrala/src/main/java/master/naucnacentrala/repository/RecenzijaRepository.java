package master.naucnacentrala.repository;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.enums.Rezultat;
import master.naucnacentrala.model.korisnici.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecenzijaRepository extends JpaRepository<Recenzija, Long>{
    List<Recenzija> findByCasopis(Casopis c);

    List<Recenzija> findByRecenzent(Korisnik rec);

    List<Recenzija> findByRecenzentAndRezultat(Korisnik k, Rezultat r);

    List<Recenzija> findByRadId(long radId);
}
