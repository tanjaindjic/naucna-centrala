package master.naucnacentrala;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import master.naucnacentrala.model.elastic.RadIndexUnit;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.repository.RadIndexingUnitRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.RadService;
import master.naucnacentrala.service.UrednikService;
import org.springframework.web.client.RestTemplate;

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
	private IdentityService identityService;

	@Autowired
	private RadIndexingUnitRepository riuRepository;

	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	
	@PostConstruct
	private void init() throws Exception {

		Korisnik autor = korisnikService.addKorisnik(new Korisnik("autor1", bcrypt.encode("autor1"), "Autor1", "Autor1", "Beograd", "Srbija", "autor1@gmail.com", new ArrayList<>(), new ArrayList<>())); 
		Korisnik autor2 = korisnikService.addKorisnik(new Korisnik("autor2", bcrypt.encode("autor2"), "Autor2", "Autor2", "Beograd", "Srbija", "autor2@gmail.com", new ArrayList<>(), new ArrayList<>()));
		Korisnik demo = korisnikService.addKorisnik(new Korisnik("demo", bcrypt.encode("demo"), "Demo", "Demo", "Beograd", "Srbija", "demo@gmail.com", new ArrayList<>(), new ArrayList<>())); 
		
		Urednik urednik = urednikService.addUrednik(new Urednik("urednik", bcrypt.encode("urednik1"), "Urednik", "Urednik", "Beograd", "Srbija", "urednik@gmail.com", new ArrayList<>(), new ArrayList<>(), "dr", new ArrayList(), null, new ArrayList()));
		Urednik urednik2 = urednikService.addUrednik(new Urednik("urednik2", bcrypt.encode("urednik2"), "Urednik2", "Urednik2", "Beograd", "Srbija", "urednik2@gmail.com", new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednik3 = urednikService.addUrednik(new Urednik("urednik3", bcrypt.encode("urednik3"), "Urednik3", "Urednik3", "Beograd", "Srbija", "urednik3@gmail.com", new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednik4 = urednikService.addUrednik(new Urednik("urednik4", bcrypt.encode("urednik4"), "Urednik4", "Urednik4", "Beograd", "Srbija", "urednik2@gmail.com", new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Casopis casopis = casopisService.addCasopis(new Casopis("Casopis1", "issn1", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis1.jpg", 100F));
		Casopis casopis2 = casopisService.addCasopis(new Casopis("Casopis2", "issn2", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik2, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis2.jpg", 250F));
		Casopis casopis3 = casopisService.addCasopis(new Casopis("Casopis3", "issn3", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik3, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis3.jpg", 50F));
		Casopis casopis4 = casopisService.addCasopis(new Casopis("Casopis4", "issn4", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik4, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis4.jpg", 350F));

		casopis.setGlavniUrednik(urednik);
		casopisService.updateCasopis(casopis);
		casopis2.setGlavniUrednik(urednik2);
		casopisService.updateCasopis(casopis2);
		casopis3.setGlavniUrednik(urednik3);
		casopisService.updateCasopis(casopis3);
		casopis4.setGlavniUrednik(urednik4);
		casopisService.updateCasopis(casopis4);
		
		urednik.setUredjuje(casopis);
		urednik.getNaucneOblasti().add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		urednikService.updateUrednik(urednik);
		urednik2.setUredjuje(casopis2);
		urednik2.getNaucneOblasti().add(NaucnaOblast.MEDICINA);
		urednikService.updateUrednik(urednik2);
		urednik3.setUredjuje(casopis3);
		urednik3.getNaucneOblasti().add(NaucnaOblast.PRIRODNO_MATEMATICKE_NAUKE);
		urednikService.updateUrednik(urednik3);
		urednik4.setUredjuje(casopis4);
		urednik4.getNaucneOblasti().add(NaucnaOblast.UMETNOST);
		urednikService.updateUrednik(urednik4);
		
		Rad rad = radService.addRad(new Rad("", "Rad1", autor, new ArrayList<>(), new ArrayList(), "apstrakt", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis));
		casopis.getRadovi().add(rad);
		casopisService.updateCasopis(casopis);
		Rad rad2 = radService.addRad(new Rad("", "Rad2", autor2, new ArrayList<>(), new ArrayList(), "apstrakt2", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis));
		casopis.getRadovi().add(rad2);
		casopisService.updateCasopis(casopis);
		
		autor.getPlaceniRadovi().add(rad2);
		korisnikService.updateKorisnik(autor);

		saveCamundaUser(autor);
		saveCamundaUser(autor2);
		saveCamundaUser(urednik);
		saveCamundaUser(urednik2);
		saveCamundaUser(urednik3);
		saveCamundaUser(urednik4);
		saveCamundaUser(demo);
		
		/*RadIndexUnit riu = new RadIndexUnit(rad.getNaslov(), "sadrzaj ovde", rad.getAutor().getIme() + " " + rad.getAutor().getPrezime(), rad.getListaKoautora(), rad.getKljucniPojmovi(), rad.getApstrakt(), rad.getNaucnaOblast(), rad.getCasopis().isOpenAccess(), rad.getCasopis().getNaziv());
		riuRepository.save(riu);*/
	}

	public void saveCamundaUser (Korisnik autor){
		/*String payload = "{\"profile\": \n" +
				"  {\"id\": " + autor.getUsername() + ",\n" +
				"  \"firstName\": " + autor.getIme() + ",\n" +
				"  \"lastName\": " + autor.getPrezime() + ",\n" +
				"  \"email\": " + autor.getEmail() + "},\n" +
				"\"credentials\": \n" +
				"  {\"password\": " + autor.getPass() + "}\n" +
				"}";
		RestTemplate rt = new RestTemplate();
		rt.postForEntity("http://localhost:8096/rest/user/create", payload, String.class);*/
		User newUser = identityService.newUser(autor.getUsername());
		newUser.setEmail(autor.getEmail());
		newUser.setFirstName(autor.getIme());
		newUser.setLastName(autor.getPrezime());
		newUser.setPassword(autor.getUsername());//zbog bcrypta sve se cuva tako
		identityService.saveUser(newUser);
		System.out.println(newUser.getId());

	}

}
