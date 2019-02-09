package master.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import master.naucnacentrala.model.korisnici.Urednik;

public interface UrednikRepository extends JpaRepository<Urednik, Long> {

    Urednik findByUsernameIgnoreCase(String username);
}
