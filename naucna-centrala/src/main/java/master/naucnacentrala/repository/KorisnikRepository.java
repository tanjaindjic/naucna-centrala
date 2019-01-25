package master.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import master.naucnacentrala.model.korisnici.Korisnik;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

	Korisnik findByUsernameIgnoreCase(String username);
	Korisnik findByEmailIgnoreCase(String email);

}
