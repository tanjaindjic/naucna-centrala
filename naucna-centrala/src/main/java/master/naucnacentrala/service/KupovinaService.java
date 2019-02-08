package master.naucnacentrala.service;

import master.naucnacentrala.model.Kupovina;
import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.model.enums.SyncStatus;
import master.naucnacentrala.model.korisnici.Korisnik;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface KupovinaService {

    public Kupovina getKupovina(Long id);
    public List<Kupovina> getKupovine();
    public List<Kupovina> getKupovineKorisnika(Korisnik k);
    public void saveKupovina(Kupovina k);
    public ResponseEntity<SyncStatus> update(HashMap<Long, Status> transakcije);
}
