package master.naucnacentrala.service;

import java.util.Collection;

import master.naucnacentrala.model.Rad;

public interface RadService {
	
	public Rad addRad(Rad r);
	public Rad getRad(Long id);
	public Rad updateRad(Rad r);
	public Collection<Rad> getAll();

}
