package master.naucnacentrala.service;

import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.korisnici.Recenzent;

import java.util.List;

public interface RecenzentService {
    public Recenzent save(Recenzent r);

    Recenzent get(Long id);
}
