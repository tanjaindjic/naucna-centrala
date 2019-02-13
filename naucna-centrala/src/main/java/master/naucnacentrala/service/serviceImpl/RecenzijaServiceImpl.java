package master.naucnacentrala.service.serviceImpl;

import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.enums.Rezultat;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.RecenzijaRepository;
import master.naucnacentrala.service.RecenzijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecenzijaServiceImpl implements RecenzijaService {
    @Autowired
    private RecenzijaRepository recenzijaRepository;

    @Override
    public Recenzija save(Recenzija r) {
        return recenzijaRepository.save(r);
    }

    @Override
    public List<Recenzija> getAll() {
        return recenzijaRepository.findAll();
    }

    @Override
    public List<Recenzija> findByRecenzentAndRezultat(Korisnik k) {
        return recenzijaRepository.findByRecenzentAndRezultat(k, Rezultat.NOVO);
    }

    @Override
    public Recenzija findById(Long id) {
        return recenzijaRepository.findById(id).get();
    }

    @Override
    public List<Recenzija> findByRadId(long radId) {
        return recenzijaRepository.findByRadId(radId);
    }

    @Override
    public void deleteRecenzijeByRadId(Long id) {
        List<Recenzija> zaBrisanje = recenzijaRepository.findByRadId(id);
        for(Recenzija r : zaBrisanje)
            recenzijaRepository.delete(r);
    }

}
