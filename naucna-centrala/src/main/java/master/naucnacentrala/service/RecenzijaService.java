package master.naucnacentrala.service;

import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.korisnici.Korisnik;

import java.util.List;

public interface RecenzijaService {
    public Recenzija save(Recenzija r);

    List<Recenzija> getAll();

    List<Recenzija> findByRecenzentAndRezultat(Korisnik k);

    Recenzija findById(Long id);

    List<Recenzija> findByRadId(long radId);

    void deleteRecenzijeByRadId(Long id);
}
