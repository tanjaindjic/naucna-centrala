package master.naucnacentrala.service.serviceImpl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.KorisnikRepository;
import master.naucnacentrala.service.KorisnikService;

@Service
public class KorisnikServiceImpl implements KorisnikService{
	
	private Algorithm algorithm = Algorithm.HMAC256("secret");
	
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

	@Override
	public String createToken(String username) {
		// TODO Auto-generated method stub
		return JWT.create()
				.withSubject(username)
				.sign(algorithm);
	}

}
