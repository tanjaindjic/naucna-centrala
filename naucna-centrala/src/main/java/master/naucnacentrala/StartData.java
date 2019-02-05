package master.naucnacentrala;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import master.naucnacentrala.model.elastic.RadIndexUnit;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateAction;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Autowired
	private Client nodeClient;

	@Value("${elasticsearch.baseUrl}")
	private String elasticsearchUrl;


	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	
	@PostConstruct
	private void init() throws Exception {

		Korisnik autor = korisnikService.addKorisnik(new Korisnik("autor1", bcrypt.encode("autor1"), "Autor1", "Autor1", "Beograd", "Srbija", "autor1@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
		Korisnik autor2 = korisnikService.addKorisnik(new Korisnik("autor2", bcrypt.encode("autor2"), "Autor2", "Autor2", "Beograd", "Srbija", "autor2@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
		Korisnik demo = korisnikService.addKorisnik(new Korisnik("demo", bcrypt.encode("demo"), "Demo", "Demo", "Beograd", "Srbija", "demo@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
		
		Urednik urednik = urednikService.addUrednik(new Urednik("urednik", bcrypt.encode("urednik1"), "Urednik", "Urednik", "Beograd", "Srbija", "urednik@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "dr", new ArrayList(), null, new ArrayList()));
		Urednik urednik2 = urednikService.addUrednik(new Urednik("urednik2", bcrypt.encode("urednik2"), "Urednik2", "Urednik2", "Beograd", "Srbija", "urednik2@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednik3 = urednikService.addUrednik(new Urednik("urednik3", bcrypt.encode("urednik3"), "Urednik3", "Urednik3", "Beograd", "Srbija", "urednik3@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Urednik urednik4 = urednikService.addUrednik(new Urednik("urednik4", bcrypt.encode("urednik4"), "Urednik4", "Urednik4", "Beograd", "Srbija", "urednik2@gmail.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "mr", new ArrayList(), null, new ArrayList()));
		Casopis casopis = casopisService.addCasopis(new Casopis("Casopis1", "issn1", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis1.jpg", 100F, "casopis001"));
		Casopis casopis2 = casopisService.addCasopis(new Casopis("Casopis2", "issn2", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), false, urednik2, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis2.jpg", 250F, "casopis002"));
		Casopis casopis3 = casopisService.addCasopis(new Casopis("Casopis3", "issn3", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik3, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis3.jpg", 50F, "casopis003"));
		Casopis casopis4 = casopisService.addCasopis(new Casopis("Casopis4", "issn4", new ArrayList(Arrays.asList(NaucnaOblast.values())), new ArrayList(), true, urednik4, new ArrayList<>(), new ArrayList<>(), "/assets/images/casopis4.jpg", 350F, "casopis004"));

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
		
		Rad rad = radService.addRad(new Rad("", "Rad1", autor, new ArrayList<>(),100F, "/assets/images/Article-Icon.png", "", "apstrakt", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "radradrad1"));
		casopis.getRadovi().add(rad);
		Rad rad2 = radService.addRad(new Rad("", "Rad2", autor2, new ArrayList<>(), 50F, "/assets/images/Article-Icon.png", "", "apstrakt2", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "radradrad2"));
		casopis.getRadovi().add(rad2);
		Rad rad3 = radService.addRad(new Rad("", "Rad3", autor, new ArrayList<>(),25F, "/assets/images/Article-Icon.png", "", "apstrakt3", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "radradrad3"));
		casopis.getRadovi().add(rad3);
		Rad rad4 = radService.addRad(new Rad("", "Rad4", autor2, new ArrayList<>(), 700F, "/assets/images/Article-Icon.png", "", "apstrakt4", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "radradrad4"));
		casopis.getRadovi().add(rad4);
		Rad rad5 = radService.addRad(new Rad("", "Rad5", autor2, new ArrayList<>(),200F, "/assets/images/Article-Icon.png", "", "apstrakt5", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "radradrad5"));
		casopis.getRadovi().add(rad5);
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

		setupTestData(rad);

    }

	private void setupTestData(Rad rad) throws ExecutionException, InterruptedException {
		/*String template = "";
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			template = IOUtils.toString(classLoader.getResourceAsStream("naucnirad.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("nc_template");
		request.source("{\n" +
				"\t\"template\": [\"naucni*\"],\n" +
				"\t\"settings\": {\n" +
				"\t\t\"analysis\": {\n" +
				"\t\t\t\"analyzer\": {\n" +
				"\t\t\t\t\"default\": {\n" +
				"\t\t\t\t\t\"type\": \"serbian-analyzer\"\n" +
				"\t\t\t\t}\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t},\n" +
				"\t\"mappings\": {}\n" +
				"}", XContentType.JSON);

		PutIndexTemplateResponse response1 = nodeClient.admin().indices().execute(PutIndexTemplateAction.INSTANCE, request).get();
		System.out.println(response1);

		IndexResponse response = nodeClient.prepareIndex("naucnirad", "pdf")
				.setSource(template, XContentType.JSON)
				.get();
		System.out.println(response.status());

		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File("C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf");
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
			System.out.println(parsedText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
/*		RestTemplate rt = new RestTemplate();
		rt.put("http://localhost:9300/naucnirad", null);*/
		RadIndexUnit riu = new RadIndexUnit(rad.getId(), rad.getNaslov(), "Латиница се проширила из Италије заједно са латинским језиком у области око Средоземног мора ширењем Римског царства", rad.getAutor().getIme() + " " + rad.getAutor().getPrezime(), rad.getListaKoautora(), rad.getKljucniPojmovi(), rad.getApstrakt(), rad.getNaucnaOblast(), rad.getCasopis().isOpenAccess(), rad.getCasopis().getNaziv(), rad.getCasopis().getId());
		riu = riuRepository.save(riu);
		System.out.println(riu.toString());

		/*SearchResponse res = nodeClient.prepareSearch("naucnirad")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("sadrzaj", "oblasti").analyzer("serbian-analyzer"))
				.setExplain(true)
				.get();
		System.out.println(res.toString());*/
	}

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
