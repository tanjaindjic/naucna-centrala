package master.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import master.naucnacentrala.model.korisnici.Koautor;

public interface KoautorRepository extends JpaRepository<Koautor, Long> {

}
