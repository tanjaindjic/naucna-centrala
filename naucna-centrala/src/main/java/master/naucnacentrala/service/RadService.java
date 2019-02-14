package master.naucnacentrala.service;

import java.util.Collection;
import java.util.List;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;

public interface RadService {
	
	public Rad addRad(Rad r);
	public Rad getRad(Long id);
	public Rad updateRad(Rad r);
	public Collection<Rad> getAll();
    List<Rad> getRadZaUrednika(Casopis uredjuje, StatusRada novo);
    void deleteRad(Long id);

    List<Rad> getRecenziraniRadovi(Long id);

    List<Rad> findObjavljeno(Long id, StatusRada prihvacen);
}
