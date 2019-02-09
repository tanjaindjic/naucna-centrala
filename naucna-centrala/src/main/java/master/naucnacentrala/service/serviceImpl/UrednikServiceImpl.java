package master.naucnacentrala.service.serviceImpl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.repository.UrednikRepository;
import master.naucnacentrala.service.UrednikService;

@Service
public class UrednikServiceImpl implements UrednikService {
	
	@Autowired
	private UrednikRepository urednikRepository;

	@Override
	public Urednik addUrednik(Urednik k) {
		return urednikRepository.save(k);
	}

	@Override
	public Urednik getUrednik(Long id) {
		return urednikRepository.findById(id).get();
	}

	@Override
	public Urednik updateUrednik(Urednik k) {
		return urednikRepository.save(k);
	}

	@Override
	public Collection<Urednik> getAll() {
		return urednikRepository.findAll();
	}

	@Override
	public Urednik getUrednikByUsername(String username) {
		return urednikRepository.findByUsernameIgnoreCase(username);
	}

}
