package master.naucnacentrala.service.serviceImpl;

import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.repository.RecenzijaRepository;
import master.naucnacentrala.service.RecenzijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecenzijaServiceImpl implements RecenzijaService {
    @Autowired
    private RecenzijaRepository recenzijaRepository;

    @Override
    public Recenzija save(Recenzija r) {
        return recenzijaRepository.save(r);
    }
}
