package master.naucnacentrala.repository;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Recenzija;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecenzijaRepository extends JpaRepository<Recenzija, Long>{
    List<Recenzija> findByCasopis(Casopis c);
}
