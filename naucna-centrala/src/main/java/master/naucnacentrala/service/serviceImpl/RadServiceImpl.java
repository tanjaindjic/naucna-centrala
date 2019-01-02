package master.naucnacentrala.service.serviceImpl;

import java.util.Collection;

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
		return radRepository.getOne(id);
	}

	@Override
	public Rad updateRad(Rad r) {
		return radRepository.save(r);
	}

	@Override
	public Collection<Rad> getAll() {
		return radRepository.findAll();
	}

}
