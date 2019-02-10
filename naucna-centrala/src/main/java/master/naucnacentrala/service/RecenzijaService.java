package master.naucnacentrala.service;

import master.naucnacentrala.model.Recenzija;

import java.util.List;

public interface RecenzijaService {
    public Recenzija save(Recenzija r);

    List<Recenzija> getAll();
}
