package master.naucnacentrala;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import master.naucnacentrala.model.elastic.RadIndexUnit;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tomcat.util.json.JSONParser;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private Client nodeClient;

	@Value("${elasticsearch.baseUrl}")
	private String elasticsearchUrl;


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
		
		Rad rad = radService.addRad(new Rad("", "Rad1", autor, new ArrayList<>(), "", "apstrakt", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "/assets/images/Article-Icon.png"));
		casopis.getRadovi().add(rad);
		Rad rad2 = radService.addRad(new Rad("", "Rad2", autor2, new ArrayList<>(), "", "apstrakt2", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "/assets/images/Article-Icon.png"));
		casopis.getRadovi().add(rad2);
		Rad rad3 = radService.addRad(new Rad("", "Rad3", autor, new ArrayList<>(), "", "apstrakt3", NaucnaOblast.DRUSTVENO_HUMANISTICKE_NAUKE, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "/assets/images/Article-Icon.png"));
		casopis.getRadovi().add(rad3);
		Rad rad4 = radService.addRad(new Rad("", "Rad4", autor2, new ArrayList<>(), "", "apstrakt4", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "/assets/images/Article-Icon.png"));
		casopis.getRadovi().add(rad4);
		Rad rad5 = radService.addRad(new Rad("", "Rad5", autor2, new ArrayList<>(), "", "apstrakt5", NaucnaOblast.MEDICINA, "\"C:\\Users\\hrcak\\Desktop\\ES\\test1.pdf", "", casopis, "/assets/images/Article-Icon.png"));
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


        /*CreateIndexRequest request = new CreateIndexRequest("naucnirad");
        //request.source(template, XContentType.JSON);
            IndexResponse response = nodeClient.prepareIndex("naucnirad", "pdf")
                //.setSource(template, XContentType.JSON)
                .get();
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File("C:/my.pdf");
        try {
            // PDFBox 2.0.8 require org.apache.pdfbox.io.RandomAccessRead
            // RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            // PDFParser parser = new PDFParser(randomAccessFile);

            PDFParser parser = new PDFParser(new FileInputStream(file));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);
            String parsedText = pdfStripper.getText(pdDoc);
            System.out.println(parsedText);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RadIndexUnit riu = new RadIndexUnit(rad.getNaslov(), template, rad.getAutor().getIme() + " " + rad.getAutor().getPrezime(), rad.getListaKoautora(), rad.getKljucniPojmovi(), rad.getApstrakt(), rad.getNaucnaOblast(), rad.getCasopis().isOpenAccess(), rad.getCasopis().getNaziv());
		riuRepository.save(riu);

        SearchResponse res = nodeClient.prepareSearch("naucnirad")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("sadrzaj", "заједно"))
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
