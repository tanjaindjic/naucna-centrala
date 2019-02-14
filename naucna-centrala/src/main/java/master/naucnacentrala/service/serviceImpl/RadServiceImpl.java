package master.naucnacentrala.service.serviceImpl;

import java.util.Collection;
import java.util.List;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.repository.RadRepository;
import master.naucnacentrala.service.RadService;

@Service
public class RadServiceImpl implements RadService {
	
	@Autowired
	private RadRepository radRepository;

	@Override
	public Rad addRad(Rad r) {
		return radRepository.save(r);
	}

	@Override
	public Rad getRad(Long id) {
		// TODO Auto-generated method stub
		return radRepository.findById(id).get();
	}

	@Override
	public Rad updateRad(Rad r) {
		return radRepository.save(r);
	}

	@Override
	public Collection<Rad> getAll() {
		return radRepository.findAll();
	}

	@Override
	public List<Rad> getRadZaUrednika(Casopis uredjuje, StatusRada novo) {
		return radRepository.findByCasopisAndStatusRada(uredjuje, novo);
	}

	@Override
	public void deleteRad(Long id) {
		radRepository.deleteById(id);
	}

	@Override
	public List<Rad> getRecenziraniRadovi(Long id) {
		return radRepository.findByAutorIdAndStatusRada(id, StatusRada.KOREKCIJA_AUTOR);
	}

	@Override
	public List<Rad> findObjavljeno(Long id, StatusRada prihvacen) {
		return radRepository.findByAutorIdAndStatusRada(id, prihvacen);
	}

}
