package master.naucnacentrala.service;

import java.util.Collection;

import master.naucnacentrala.model.korisnici.Korisnik;

public interface KorisnikService {

	public Korisnik addKorisnik(Korisnik k);
	public Korisnik getKorisnik(Long id);
	public Korisnik updateKorisnik(Korisnik k);
	public Collection<Korisnik> getAll();
	public String createToken(String string);

}
