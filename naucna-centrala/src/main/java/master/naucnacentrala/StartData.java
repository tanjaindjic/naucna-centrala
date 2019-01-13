package master.naucnacentrala;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.repository.RadIndexingUnitRepository;
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
	
	@Autowired
	private RadIndexingUnitRepository riuRepository;
	
	@Autowired 
	private IdentityService identityService;
	
	@PostConstruct
	private void init() {
		
		Korisnik autor = korisnikService.addKorisnik(new Korisnik("demo", "demo", "Autor", "Autor", "Beograd", "Srbija", "autor@gmail.com", new ArrayList<>(), new ArrayList<>())); 
		Korisnik autor2 = korisnikService.addKorisnik(new Korisnik("autor2", "autor2", "Autor2", "Autor2", "Beograd", "Srbija", "autor2@gmail.com", new ArrayList<>(), new ArrayList<>()));
		Urednik urednik = urednikService.addUrednik(new Urednik("urednik", "urednik", "Urednik", "Urednik", "Beograd", "Srbija", "urednik@gmail.com", new ArrayList<>(), new ArrayList<>(), "dr", new ArrayList(), null, new ArrayList()));
		Urednik urednik2 = urednikService.addUrednik(new Urednik("urednik2", "urednik2", "Urednik2", "Urednik2", "Beograd", "Srbija", "urednik2@gmail.com", new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Casopis casopis = casopisService.addCasopis(new Casopis("Casopis1", "issn1", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik, new ArrayList<>(), new ArrayList<>()));
		Casopis casopis2 = casopisService.addCasopis(new Casopis("Casopis2", "issn2", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik, new ArrayList<>(), new ArrayList<>()));
		
		casopis.setGlavniUrednik(urednik);
		casopisService.updateCasopis(casopis);
		casopis2.setGlavniUrednik(urednik2);
		casopisService.updateCasopis(casopis2);
		
		urednik.setUredjuje(casopis);
		urednik.getNaucneOblasti().add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		urednikService.updateUrednik(urednik);
		urednik2.setUredjuje(casopis2);
		urednik2.getNaucneOblasti().add(NaucnaOblast.MEDICINA);
		urednikService.updateUrednik(urednik2);
		
		Rad rad = radService.addRad(new Rad("", "Rad1", autor, new ArrayList<>(), new ArrayList(), "apstrakt", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis));
		casopis.getRadovi().add(rad);
		casopisService.updateCasopis(casopis);
		Rad rad2 = radService.addRad(new Rad("", "Rad2", autor2, new ArrayList<>(), new ArrayList(), "apstrakt2", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis));
		casopis.getRadovi().add(rad2);
		casopisService.updateCasopis(casopis);
		
		autor.getPlaceniRadovi().add(rad2);
		korisnikService.updateKorisnik(autor);
		
		
		User camundaUser = identityService.newUser("autor");
		camundaUser.setFirstName(autor.getIme());
		camundaUser.setLastName(autor.getPrezime());
		camundaUser.setEmail(autor.getEmail());
		camundaUser.setPassword(autor.getPass());
		identityService.saveUser(camundaUser);
		
		camundaUser = identityService.newUser("autor2");
		camundaUser.setFirstName(autor2.getIme());
		camundaUser.setLastName(autor2.getPrezime());
		camundaUser.setEmail(autor2.getEmail());
		camundaUser.setPassword(autor2.getPass());
		identityService.saveUser(camundaUser);
		
		//RadIndexUnit riu = new RadIndexUnit(rad.getNaslov(), "sadrzaj ovde", rad.getAutor().getIme() + " " + rad.getAutor().getPrezime(), rad.getListaKoautora(), rad.getKljucniPojmovi(), rad.getApstrakt(), rad.getNaucnaOblast(), rad.getCasopis().isOpenAccess(), rad.getCasopis().getNaziv());
		//riuRepository.save(riu);
	}

}
