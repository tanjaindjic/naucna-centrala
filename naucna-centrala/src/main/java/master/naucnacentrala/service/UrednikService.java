package master.naucnacentrala.service;

import java.util.Collection;

import master.naucnacentrala.model.korisnici.Urednik;

public interface UrednikService {
	
	public Urednik addUrednik(Urednik k);
	public Urednik getUrednik(Long id);
	public Urednik updateUrednik(Urednik k);
	public Collection<Urednik> getAll();
    Urednik getUrednikByUsername(String username);
}
