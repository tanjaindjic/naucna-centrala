package master.naucnacentrala.service;

import java.util.Collection;

import master.naucnacentrala.model.Casopis;

public interface CasopisService {
	public Casopis addCasopis(Casopis k);
	public Casopis getCasopis(Long id);
	public Casopis updateCasopis(Casopis k);
	public Collection<Casopis> getAll();

}
