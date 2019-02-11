package master.naucnacentrala.service;

import java.util.Collection;
import java.util.List;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.StatusRada;

public interface RadService {
	
	public Rad addRad(Rad r);
	public Rad getRad(Long id);
	public Rad updateRad(Rad r);
	public Collection<Rad> getAll();
    List<Rad> getRadZaUrednika(Casopis uredjuje, StatusRada novo);
    void deleteRad(Long id);
}
