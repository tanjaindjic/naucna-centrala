package master.naucnacentrala.service.serviceImpl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.KorisnikRepository;
import master.naucnacentrala.service.KorisnikService;

public class KorisnikServiceImpl implements KorisnikService{
	
	@Autowired
	private KorisnikRepository korisnikRepository;

	@Override
	public Korisnik addKorisnik(Korisnik k) {
		return korisnikRepository.save(k);
	}

	@Override
	public Korisnik getKorisnik(Long id) {
		// TODO Auto-generated method stub
		return korisnikRepository.getOne(id);
	}
	

	@Override
	public Korisnik updateKorisnik(Korisnik k) {
		return korisnikRepository.save(k);
	}

	@Override
	public Collection<Korisnik> getAll() {
		return korisnikRepository.findAll();
	}

}
