package master.naucnacentrala.service.serviceImpl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.repository.CasopisRepository;
import master.naucnacentrala.service.CasopisService;

@Service
public class CasopisServiceImpl implements CasopisService {

	@Autowired
	private CasopisRepository casopisRepository;
	
	@Override
	public Casopis addCasopis(Casopis k) {
		return casopisRepository.save(k);
	}

	@Override
	public Casopis getCasopis(Long id) {
		return casopisRepository.findById(id).get();
	}

	@Override
	public Casopis updateCasopis(Casopis k) {
		return casopisRepository.save(k);
	}

	@Override
	public Collection<Casopis> getAll() {
		return casopisRepository.findAll();
	}

}
