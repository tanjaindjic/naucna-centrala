package master.naucnacentrala;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import master.naucnacentrala.model.Recenzija;/*
import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.model.elastic.RecenzentIndexUnit;
import master.naucnacentrala.model.elastic.RecenzijaIndexUnit;*/
import master.naucnacentrala.model.enums.Rezultat;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Recenzent;/*
import master.naucnacentrala.repository.RecenzentIndexUnitRepository;
import master.naucnacentrala.repository.RecenzijaIndexUnitRepository;*/
import master.naucnacentrala.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;/*
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;/*
import master.naucnacentrala.repository.RadIndexingUnitRepository;*/

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
/*
	@Autowired
	private RadIndexingUnitRepository riuRepository;

	@Autowired
	private Client nodeClient;*/

	@Autowired
	private RecenzentService recenzentService;

	@Autowired
	private RecenzijaService recenzijaService;
/*
	@Autowired
	private RecenzijaIndexUnitRepository recenzijaIndexUnitRepository;

	@Autowired
	private RecenzentIndexUnitRepository recenzentIndexUnitRepository;*/

	@Value("${elasticsearch.baseUrl}")
	private String elasticsearchUrl;


	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	
	@PostConstruct
	private void init() throws Exception {

		Korisnik autor = korisnikService.addKorisnik(new Korisnik("autor1", bcrypt.encode("autor1"), "Autor1", "Autor1", "Beograd", "Srbija", "mali.patuljko@gmail.com", 44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
		Korisnik autor2 = korisnikService.addKorisnik(new Korisnik("autor2", bcrypt.encode("autor2"), "Autor2", "Autor2", "Novi Sad", "Srbija", "mali.patuljko@gmail.com",45.2671, 19.8335, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
		Korisnik demo = korisnikService.addKorisnik(new Korisnik("demo", bcrypt.encode("demo"), "Mika", "Mikic", "Beograd", "Srbija", "mali.patuljko@gmail.com", 44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
		
		Urednik urednik = urednikService.addUrednik(new Urednik("urednik", bcrypt.encode("urednik"), "Pera", "Peric", "Beograd", "Srbija", "mali.patuljko@gmail.com", 44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr", new ArrayList(), null, new ArrayList()));
		Urednik urednik2 = urednikService.addUrednik(new Urednik("urednik2", bcrypt.encode("urednik2"), "Urednik2", "Urednik2", "Beograd", "Srbija", "mali.patuljko@gmail.com", 44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednik3 = urednikService.addUrednik(new Urednik("urednik3", bcrypt.encode("urednik3"), "Urednik3", "Urednik3", "Beograd", "Srbija", "mali.patuljko@gmail.com",  44.7866, 20.4489,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednik4 = urednikService.addUrednik(new Urednik("urednik4", bcrypt.encode("urednik4"), "Urednik4", "Urednik4", "Beograd", "Srbija", "mali.patuljko@gmail.com",44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednikNO = urednikService.addUrednik(new Urednik("urednikno", bcrypt.encode("urednikno"), "Urednikno", "Urednikno", "Beograd", "Srbija", "mali.patuljko@gmail.com",44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Casopis casopis = casopisService.addCasopis(new Casopis("Naučni časopis", "ISSN 231-561X", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik, new HashMap<>(), new ArrayList<>(), "/assets/images/casopis1.jpg", 50F, "casopis001"));
		Casopis casopis2 = casopisService.addCasopis(new Casopis("Hemijska industrija", "ISSN 234-501X", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik2, new HashMap<>(), new ArrayList<>(), "/assets/images/casopis2.jpg", 40F, "casopis002"));
		Casopis casopis3 = casopisService.addCasopis(new Casopis("Socioeconomica", "ISSN 204-561X", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik3, new HashMap<>(), new ArrayList<>(), "/assets/images/casopis3.jpg", 50F, "casopis003"));
		Casopis casopis4 = casopisService.addCasopis(new Casopis("PONS - medicinski časopis", "ISSN 244-561X", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik4, new HashMap<>(), new ArrayList<>(), "/assets/images/casopis4.jpg", 30F, "casopis004"));


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

		casopis.getUredniciNaucnihOblasti().put(NaucnaOblast.PRIRODNO_MATEMATICKE_NAUKE.name(), urednikNO.getUsername());
		urednikNO.setUredjuje(casopis);
		urednikService.addUrednik(urednikNO);
		casopisService.updateCasopis(casopis);

		demo.getPlaceniCasopisi().add(casopis4);
		korisnikService.addKorisnik(demo);
		
		Rad rad = radService.addRad(new Rad("10.1038/nphys1170 ", "Како смо изашли из воде", autor, "",10F, "/assets/images/Article-Icon.png", "evolucija, nauka", "Прелазак живих бића из воде на копно био jе кључан моменат у еволуциjи", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "voda.pdf", "C:\\Users\\hrcak\\Desktop\\NC_uploads\\voda.pdf", casopis, "radradrad1", StatusRada.PRIHVACEN));
		casopis.getRadovi().add(rad);
		Rad rad2 = radService.addRad(new Rad("10.1002/0470841559.ch1", "Пројекат кодирања", autor2, "Mika Peric", 10F, "/assets/images/Article-Icon.png", "gen, kodiranje", "The Earth BioGenome Project планира да секвенцира 1,5 милиона генома, што ће вероватно коштати 4,7 милијарди долара", NaucnaOblast.MEDICINA, "kodiranje.pdf", "C:\\Users\\hrcak\\Desktop\\NC_uploads\\kodiranje.pdf", casopis, "radradrad2", StatusRada.PRIHVACEN));
		casopis.getRadovi().add(rad2);
		Rad rad3 = radService.addRad(new Rad("10.1594/PANGAEA.726855", "Најстарије америчко копље", autor, "",10F, "/assets/images/Article-Icon.png", "koplje, amerika, nauka", "Древно оружје који су археолози недавно пронашли у Тексасу, старо негде између 13.500 и 15.500 година, можда је припадало првим људима који су населили Америку", NaucnaOblast.TEHNICKO_TEHNOLOSKE_NAUKE, "koplje.pdf", "C:\\Users\\hrcak\\Desktop\\NC_uploads\\koplje.pdf", casopis3, "radradrad3", StatusRada.PRIHVACEN));
		casopis3.getRadovi().add(rad3);
		Rad rad4 = radService.addRad(new Rad("10.1594/GFZ.GEOFON.gfz2009kciu", "Како почистити свемирски отпад", autor2, "Dusko Ilic, Ana Milosavljevic", 10F, "/assets/images/Article-Icon.png", "Zemlja, otpad", "Свакога дана америчка војска изда преко 20 упозорења о потенцијалним сударима у свемиру", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "otpad.pdf", "C:\\Users\\hrcak\\Desktop\\NC_uploads\\otpad.pdf", casopis, "radradrad4", StatusRada.PRIHVACEN));
		casopis.getRadovi().add(rad4);
		Rad rad5 = radService.addRad(new Rad("10.3207/2959859860", "Pomračenje Meseca", autor2, "",10F, "/assets/images/Article-Icon.png", "mesec, zemlja", "U noći između 27. i 28. jula stanovnici većeg dela Zemlje moći će da posmatraju pomračenje Meseca, a Mars će biti najbliži Zemlji u poslednjih 15 godina", NaucnaOblast.PRIRODNO_MATEMATICKE_NAUKE,"mesec.pdf", "C:\\Users\\hrcak\\Desktop\\NC_uploads\\mesec.pdf", casopis, "radradrad5", StatusRada.PRIHVACEN));
		casopis.getRadovi().add(rad5);
		casopisService.updateCasopis(casopis);
		casopisService.updateCasopis(casopis3);

		autor.getPlaceniRadovi().add(rad2);
		korisnikService.updateKorisnik(autor);

		List<Casopis> pripada = new ArrayList();
		pripada.add(casopis);
		Recenzent r = new Recenzent("rec1", bcrypt.encode("rec1"), "Mika", "Mikic", "Beograd", "Srbija", "mali.patuljko@gmail.com",
				44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr",casopis.getNaucneOblasti(), pripada , new ArrayList() );
		recenzentService.save(r);
		System.out.println(r.toString());

		List<NaucnaOblast> no = new ArrayList<>();
		no.add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		Recenzent r2 = new Recenzent("rec2", bcrypt.encode("rec2"), "Ivan", "Peric", "Niš", "Srbija", "mali.patuljko@gmail.com",
				43.3209, 21.8958, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr",no, pripada , new ArrayList() );
		recenzentService.save(r2);

		no = new ArrayList<>();
		no.add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		no.add(NaucnaOblast.MEDICINA);
		Recenzent r3 = new Recenzent("rec3", bcrypt.encode("rec3"), "Ana", "Jokic", "Vršac", "Srbija", "mali.patuljko@gmail.com",
				45.1182, 21.2945, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr",no, pripada , new ArrayList() );
		recenzentService.save(r3);

		no = new ArrayList<>();
		no.add(NaucnaOblast.PRIRODNO_MATEMATICKE_NAUKE);
		Recenzent r4 = new Recenzent("rec4", bcrypt.encode("rec4"), "Milica", "Ivkovic", "Beograd", "Srbija", "mali.patuljko@gmail.com",
				44.7866, 20.4489, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr", no, pripada , new ArrayList() );
		recenzentService.save(r4);

		no.add(NaucnaOblast.UMETNOST);
		no.add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		Recenzent r5 = new Recenzent("rec5", bcrypt.encode("rec5"), "Jovan", "Vidic", "Kragujevac", "Srbija", "mali.patuljko@gmail.com",
				44.0128, 20.9114, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr",no, pripada , new ArrayList() );
		recenzentService.save(r5);

		no = new ArrayList<>();
		no.add(NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE);
		no.add(NaucnaOblast.PRIRODNO_MATEMATICKE_NAUKE);
		Recenzent r6 = new Recenzent("rec6", bcrypt.encode("rec6"), "Pavle", "Pavlovic", "Užice", "Srbija", "mali.patuljko@gmail.com",
				43.8556, 19.8425, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr",no, pripada , new ArrayList() );
		recenzentService.save(r6);

		casopis.getRecenzenti().addAll(Arrays.asList(r, r2, r3, r4, r5, r6));
		casopisService.updateCasopis(casopis);
/*
		Recenzija recenzija = recenzijaService.save(new Recenzija(casopis, rad, r, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija2 = recenzijaService.save(new Recenzija(casopis, rad, r2, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija3 = recenzijaService.save(new Recenzija(casopis, rad, r5, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija4 = recenzijaService.save(new Recenzija(casopis, rad2, r6, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija5 = recenzijaService.save(new Recenzija(casopis, rad2, r, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija6 = recenzijaService.save(new Recenzija(casopis, rad2, r3, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija7 = recenzijaService.save(new Recenzija(casopis, rad3, r, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija8 = recenzijaService.save(new Recenzija(casopis, rad3, r5, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija9 = recenzijaService.save(new Recenzija(casopis, rad3, r6, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija10 = recenzijaService.save(new Recenzija(casopis, rad4, r6, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija11 = recenzijaService.save(new Recenzija(casopis, rad4, r, "Sve super.", Rezultat.PRIHVATITI));
		Recenzija recenzija12 = recenzijaService.save(new Recenzija(casopis, rad4, r3, "Sve super.", Rezultat.PRIHVATITI));*/

		/*r.getRecenzira().addAll(Arrays.asList(rad, rad2, rad3, rad4));
		r2.getRecenzira().addAll(Arrays.asList(rad));
		r3.getRecenzira().addAll(Arrays.asList(rad2, rad4));
		r5.getRecenzira().addAll(Arrays.asList(rad, rad3));
		r6.getRecenzira().addAll(Arrays.asList(rad2, rad3, rad4));*/
	/*	r.getRecenzira().add(rad);
		r.getRecenzira().add(rad);
		r.getRecenzira().add(rad);
		r.getRecenzira().add(rad);
		recenzentService.save(r);*/
	/*	recenzentService.save(r2);
		recenzentService.save(r3);
		recenzentService.save(r5);
		recenzentService.save(r6);
		*/
		saveCamundaUser(autor);
		saveCamundaUser(autor2);
		saveCamundaUser(urednik);
		saveCamundaUser(urednik2);
		saveCamundaUser(urednik3);
		saveCamundaUser(urednik4);
		saveCamundaUser(urednikNO);
		saveCamundaUser(demo);
		saveCamundaUser(r);
		saveCamundaUser(r2);
		saveCamundaUser(r3);
		saveCamundaUser(r4);
		saveCamundaUser(r5);
		saveCamundaUser(r6);

	/*	Rad novi1 = radService.addRad(new Rad("", "Скривени екосистем Земље", autor, "Ivan Markovic, Jasna Jelic",70F, "/assets/images/Article-Icon.png", "ekosistem, Zemlja, nauka", "Научници су открили огроман подземни екосистем са милијардама микроорганизама, двоструко већи од светских океанa", NaucnaOblast.PRIRODNO_MATEMATICKE_NAUKE, "ekosistem.pdf", null, casopis, "novirad001", StatusRada.NOVO));
		Rad novi2 = radService.addRad(new Rad("", "Одакле долазе амерички староседеоци", autor, "",50F, "/assets/images/Article-Icon.png", "amerika, Zemlja, nauka", "Амерички староседеоци заиста су пореклом из Америке, као генетички културно препознатљива група", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "starosedeoci.pdf", null, casopis, "novirad002", StatusRada.NOVO));
		radService.addRad(novi1);
		radService.addRad(novi2);
*/


		/*setupTestData(rad3);
		setupTestData(rad2);
		setupTestData(rad);
		setupTestData(rad3);
		setupTestData(rad4);
		setupTestData(rad5);

		setupRecenzije(recenzija);
	    setupRecenzije(recenzija2);
		setupRecenzije(recenzija3);
		setupRecenzije(recenzija4);
		setupRecenzije(recenzija5);
		setupRecenzije(recenzija6);
		setupRecenzije(recenzija7);
		setupRecenzije(recenzija8);
		setupRecenzije(recenzija9);
		setupRecenzije(recenzija10);
		setupRecenzije(recenzija11);
		setupRecenzije(recenzija12);

 		saveRecenzentIdx(r);
		saveRecenzentIdx(r2);
		saveRecenzentIdx(r3);
		saveRecenzentIdx(r4);
		saveRecenzentIdx(r5);
		saveRecenzentIdx(r6);
*/


		/*setupTestData(novi1);
		setupTestData(novi2);*/
    }
/*
	private void setupTestData(Rad rad) throws ExecutionException, InterruptedException {

		ClassLoader classLoader = getClass().getClassLoader();
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		System.out.println(rad.getAdresaNacrta());
		File file = new File(classLoader.getResource(rad.getAdresaNacrta()).getFile());
		String parsedText = "";
		try {
			// PDFBox 2.0.8 require org.apache.pdfbox.io.RandomAccessRead
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			PDFParser parser = new PDFParser(randomAccessFile);
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(5);
			parsedText = pdfStripper.getText(pdDoc);
			pdDoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RadIndexUnit riu = new RadIndexUnit(rad.getId(), rad.getNaslov(), parsedText, rad.getAutor().getIme() + " " + rad.getAutor().getPrezime(), rad.getListaKoautora(), rad.getKljucniPojmovi(), rad.getApstrakt(), NaucnaOblast.normalized(rad.getNaucnaOblast()), rad.getCasopis().isOpenAccess(), rad.getCasopis().getNaziv(), rad.getCasopis().getId(), new GeoPoint(rad.getAutor().getLat(), rad.getAutor().getLon()));
		riu = riuRepository.save(riu);

	}
	private void setupRecenzije(Recenzija r){

		ClassLoader classLoader = getClass().getClassLoader();
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(classLoader.getResource(r.getRad().getAdresaNacrta()).getFile());
		String parsedText = "";
		try {
			// PDFBox 2.0.8 require org.apache.pdfbox.io.RandomAccessRead
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			PDFParser parser = new PDFParser(randomAccessFile);
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			parsedText = pdfStripper.getText(pdDoc);
			pdDoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<NaucnaOblast> oblasti = recenzentService.get(r.getRecenzent().getId()).getNaucneOblasti();
		List<String> stringOblasti = new ArrayList<>();
		for(NaucnaOblast o : oblasti)
			stringOblasti.add(o.name());
		RecenzijaIndexUnit riu = new RecenzijaIndexUnit(r.getId(), r.getRecenzent().getId(), r.getRecenzent().getIme(), r.getRecenzent().getPrezime(),
				r.getRecenzent().getGrad(), r.getRecenzent().getDrzava(), new GeoPoint(r.getRecenzent().getLat(), r.getRecenzent().getLon()), parsedText, String.join(", ", stringOblasti).toLowerCase().replace('_', ' '));
		recenzijaIndexUnitRepository.save(riu);
	}
	private void saveRecenzentIdx(Recenzent r){
		Collection<NaucnaOblast> oblasti = r.getNaucneOblasti();
		List<String> stringOblasti = new ArrayList<>();
		for(NaucnaOblast o : oblasti)
			stringOblasti.add(o.name());
		List<String> radovi = new ArrayList<>();
		for(Recenzija rec : recenzijaService.getAll())
			if(rec.getRecenzent().getId()==r.getId())
				radovi.add( "\"" + rec.getRad().getNaslov() +"\"");
		String recenzije = StringUtils.join(radovi, ", ");
		System.out.println(recenzije);
		RecenzentIndexUnit riu = new RecenzentIndexUnit(r.getId(), r.getIme(), r.getPrezime(), new GeoPoint(r.getLat(), r.getLon()), r.getGrad(), r.getDrzava(), recenzije, String.join(", ", stringOblasti).toLowerCase().replace('_', ' '));
		recenzentIndexUnitRepository.save(riu);
	}*/

	public void saveCamundaUser (Korisnik autor){
		User newUser = identityService.newUser(autor.getUsername());
		newUser.setEmail(autor.getEmail());
		newUser.setFirstName(autor.getIme());
		newUser.setLastName(autor.getPrezime());
		newUser.setPassword(autor.getUsername());//zbog bcrypta sve se cuva tako
		identityService.saveUser(newUser);
		System.out.println(newUser.getId());

	}

}
