package master.naucnacentrala;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.RadService;
import master.naucnacentrala.service.UrednikService;

@Component
public class StartData {
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private CasopisService casopisService;
	
	@Autowired
	private UrednikService urednikService;
	
	@Autowired
	private RadService radService;
	
	@PostConstruct
	private void init() {
		
		Korisnik autor = korisnikService.addKorisnik(new Korisnik("autor", "autor", "Autor", "Autor", "Beograd", "Srbija", "autor@gmail.com", new ArrayList<>(), new ArrayList<>())); 
		Urednik urednik = urednikService.addUrednik(new Urednik("urednik", "urednik", "Urednik", "Urednik", "Beograd", "Srbija", "urednik@gmail.com", new ArrayList<>(), new ArrayList<>(), "dr", new ArrayList(), null, new ArrayList()));
		urednik.getNaucneOblasti().add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		urednikService.updateUrednik(urednik);
		Casopis casopis = casopisService.addCasopis(new Casopis("Casopis1", "issn1", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik, new ArrayList<>(), new ArrayList<>()));
		
	}

}
