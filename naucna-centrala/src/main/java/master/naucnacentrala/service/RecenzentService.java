package master.naucnacentrala.service;

import master.naucnacentrala.model.korisnici.Recenzent;

public interface RecenzentService {
    public Recenzent save(Recenzent r);

    Recenzent get(Long id);
}
