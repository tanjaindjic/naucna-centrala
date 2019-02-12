package master.naucnacentrala.repository;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.enums.StatusRada;
import org.springframework.data.jpa.repository.JpaRepository;

import master.naucnacentrala.model.Rad;

import java.util.List;

public interface RadRepository extends JpaRepository<Rad, Long> {

    List<Rad> findByCasopisAndStatusRada(Casopis uredjuje, StatusRada novo);

    List<Rad> findByAutorIdAndStatusRada(Long id, StatusRada korekcija);
}
