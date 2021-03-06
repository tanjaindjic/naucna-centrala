package master.naucnacentrala.service.serviceImpl;

import master.naucnacentrala.model.Kupovina;
import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.model.enums.SyncStatus;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.KupovinaRepository;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.KupovinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KupovinaServiceImpl implements KupovinaService {

    @Autowired
    private KupovinaRepository kupovinaRepository;

    @Autowired
    private KorisnikService korisnikService;

    @Override
    public Kupovina getKupovina(Long id) {
        return kupovinaRepository.findById(id).get();
    }

    @Override
    public List<Kupovina> getKupovine() {
        return kupovinaRepository.findAll();
    }

    @Override
    public List<Kupovina> getKupovineKorisnika(Korisnik k) {

        return kupovinaRepository.findByKorisnik(k);
    }

    @Override
    public void saveKupovina(Kupovina k) {
        kupovinaRepository.save(k);
        if(k.getStatus().equals(Status.U)){
           Korisnik korisnik = k.getKorisnik();
           if(!korisnik.getPlaceniCasopisi().contains(k.getCasopis())) {
               korisnik.getPlaceniCasopisi().add(k.getCasopis());
               korisnikService.addKorisnik(korisnik);
           }
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<SyncStatus> update(HashMap<Long, Status> transakcije) {
        try {
            for (Map.Entry<Long, Status> entry : transakcije.entrySet()) {
                System.out.println("ID transakcije: " + entry.getKey() + " ,status: " + entry.getValue());
                Kupovina k = getKupovina(entry.getKey());
                k.setStatus(entry.getValue());
                saveKupovina(k);
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(SyncStatus.FAILED, HttpStatus.OK);
        }
        return new ResponseEntity<>(SyncStatus.SUCCESS, HttpStatus.OK);
    }
}
