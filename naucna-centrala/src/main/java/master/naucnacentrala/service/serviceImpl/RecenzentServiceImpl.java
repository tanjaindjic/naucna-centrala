package master.naucnacentrala.service.serviceImpl;

import master.naucnacentrala.model.korisnici.Recenzent;
import master.naucnacentrala.repository.RecenzentRepository;
import master.naucnacentrala.service.RecenzentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecenzentServiceImpl implements RecenzentService {

    @Autowired
    private RecenzentRepository recenzentRepository;

    @Override
    public Recenzent save(Recenzent r) {
        return recenzentRepository.save(r);
    }

    @Override
    public Recenzent get(Long id) {
        return recenzentRepository.findById(id).get();
    }
}
