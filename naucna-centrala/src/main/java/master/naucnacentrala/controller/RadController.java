package master.naucnacentrala.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.dto.FieldIdValueDTO;
import master.naucnacentrala.model.dto.FormFieldsDTO;
import master.naucnacentrala.model.dto.RegisterDTO;
import master.naucnacentrala.model.dto.UploadFileResponse;/*
import master.naucnacentrala.model.elastic.RadIndexUnit;
import master.naucnacentrala.model.elastic.RecenzentIndexUnit;
import master.naucnacentrala.model.elastic.RecenzijaIndexUnit;*/
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Recenzent;/*
import master.naucnacentrala.repository.RadIndexingUnitRepository;
import master.naucnacentrala.repository.RecenzentIndexUnitRepository;
import master.naucnacentrala.repository.RecenzijaIndexUnitRepository;*/
import master.naucnacentrala.repository.RecenzijaRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.FileStorageService;
import master.naucnacentrala.service.KorisnikService;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.form.FormFieldImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;/*
import org.elasticsearch.common.geo.GeoPoint;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.security.JwtTokenUtil;
import master.naucnacentrala.service.RadService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@RestController
@RequestMapping("/rad")
public class RadController {

	@Autowired
	private RadService radService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormService formService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
    private CasopisService casopisService;

	@Autowired
    private KorisnikService korisniService;

	@Autowired
    private FileStorageService fileStorageService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
    private RecenzijaRepository recenzijaRepository;

/*
    @Autowired
    private RecenzentIndexUnitRepository recenzentIndexUnitRepository;

    @Autowired
    private RadIndexingUnitRepository riuRepository;
*/

	@Value("${camunda.prijavaRadaProcessKey}")
	private String prijavaRadaProcessKey;

    @Value("${camunda.objavaRadaProcessKey}")
    private String objavaRadaProcessKey;

	@GetMapping(value = "prijavaRada")
	public ResponseEntity<?> startPrijava(@RequestHeader(value="Authorization") String Authorization) throws URISyntaxException {
		System.out.println("ZAPOCET PROCES PRIJAVE RADA: " + prijavaRadaProcessKey);
		String username = "";
		if(Authorization.length()>7)
			username = jwtTokenUtil.getUsernameFromToken(Authorization.substring(7));
		System.out.println("KORISNIK: " + username);
		Map mapa = new HashMap();
		if(username.equals(""))
			mapa.put("ulogovan", false);
		else mapa.put("ulogovan", true);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(prijavaRadaProcessKey, UUID.randomUUID().toString(), mapa);

		if(username.equals("")){
		    System.out.println("KORISNIK NIJE ULOGOVAN, PROCES PRIJAVE RADA SE TERMINIRA I PREUSMERAVA NA LOGIN");
		    //FIXME ne znam kako da dodjem do subprocesa i posaljem na login
		   /* ProcessInstance sub = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(pi.getBusinessKey(), "loginProcess").singleResult();
            Task task = taskService.createTaskQuery().processInstanceId(sub.getId()).list().get(0);
            TaskFormData tfd = formService.getTaskFormData(task.getId());
            List<FormField> properties = tfd.getFormFields();
            ResponseEntity re = new ResponseEntity(new FormFieldsDTO(task.getId(), pi.getId(), properties), HttpStatus.OK);
            re.getHeaders().set("Location", "login");
            return re;*/
		    runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "Korisnik nije ulogovan");
			FormFieldsDTO dto = new FormFieldsDTO(null, null, null);
			dto.setLocation("login");
		   return new ResponseEntity<>(dto, HttpStatus.OK);

		}else {
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
			task.setAssignee(username);
			taskService.saveTask(task);
			System.out.println("ZAPOCET TASK: " + task.getName() + ", ASSIGNEE: " + username);
            FormFieldsDTO dto = new FormFieldsDTO(task.getId(), pi.getId(), null);
			dto.setLocation("noviRad");
            ResponseEntity re =  new ResponseEntity(dto, HttpStatus.OK);

            return re;
		}

	}

    @GetMapping(value = "odabirCasopisa/{taskId}")
    public ResponseEntity<?> getCasopisi(@PathVariable String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        System.out.println("USAO U PROCES PRIJAVE RADA, TASK: " + task.getName());
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        FormFieldsDTO dto = new FormFieldsDTO(task.getId(), task.getProcessInstanceId(), properties);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PostMapping(value = "odabirCasopisa", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setCasopis(@RequestBody RegisterDTO registerDTO){
	    System.out.println("USAO U PROCES PRIJAVE RADA, PRIMIO: " + registerDTO.toString());

        Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
        System.out.println("USAO U PROCES PRIJAVE RADA, TASK: " + task.getName());
        String assignee = task.getAssignee();
        HashMap<String, Object> mapa = new HashMap<String, Object>();
        for(FieldIdValueDTO pair : registerDTO.getFormFields())
            mapa.put(pair.getFieldId(), pair.getFieldValue());
        System.out.println(mapa.get("odabraniCasopis").toString());
        Long id = Long.parseLong(mapa.get("odabraniCasopis").toString());
        Boolean isOpenAccess = casopisService.getCasopis(id).isOpenAccess();
        runtimeService.setVariable(registerDTO.getProcessInstanceId(), "isOpenAccess", isOpenAccess);
        formService.submitTaskForm(registerDTO.getTaskId(), mapa);

        if(!isOpenAccess){
            task = taskService.createTaskQuery().processInstanceId(registerDTO.getProcessInstanceId().toString()).list().get(0);
            task.setAssignee(assignee);
            taskService.saveTask(task);
            System.out.println("ZAPOCET TASK: " + task.getName() + ", ASSIGNEE: " + assignee );
            TaskFormData tfd = formService.getTaskFormData(task.getId());
            List<FormField> properties = tfd.getFormFields();
            FormFieldsDTO dto = new FormFieldsDTO(task.getId(), task.getProcessInstanceId(), properties);
            return new ResponseEntity(dto, HttpStatus.OK);
        }//TODO odraditi ako nije open access
        else return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/nacrt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadNacrt(@RequestParam("file") MultipartFile file) {
	    System.out.println("USAO U UPLOAD NACRTA RADA");
        String fileLocation = fileStorageService.storeFile(file, true);
        System.out.println(fileLocation);
        return new ResponseEntity<String>(fileLocation, HttpStatus.OK);
    }

    @PostMapping(value = "/final", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFinal(@RequestParam("file") MultipartFile file) {
        System.out.println("USAO U UPLOAD FINALNE VERZIJE RADA");
        String fileLocation = fileStorageService.storeFile(file, false);

        return new ResponseEntity<String>(fileLocation, HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void noviRad(@RequestBody RegisterDTO registerDTO){
        System.out.println("ZAVRSAVA PROCES PRIJAVE RADA");
        Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
        System.out.println("USAO U TASK: " + task.getName());
        HashMap<String, Object> mapa = new HashMap<String, Object>();
        for(FieldIdValueDTO pair : registerDTO.getFormFields())
            mapa.put(pair.getFieldId(), pair.getFieldValue());

        Rad r = new Rad();
        r.setAdresaNacrta(mapa.get("rad").toString());
        r.setAdresaKonacnogRada("");
        r.setApstrakt(mapa.get("apstrakt").toString());
        r.setAutor(korisniService.getKorisnikByUsername(task.getAssignee()));
        r.setCasopis(casopisService.getCasopis(Long.parseLong(runtimeService.getVariable(task.getExecutionId(), "odabraniCasopis").toString())));
        r.setDoi(UUID.randomUUID().toString());
        r.setKljucniPojmovi("");
        r.setKoautori(null);
        r.setNaslov(mapa.get("naslov").toString());
        r.setNaucnaOblast(NaucnaOblast.valueOf(mapa.get("naucnaOblast").toString()));
        r.setStatusRada(StatusRada.NOVO);
        r = radService.addRad(r);

        runtimeService.setVariable(task.getProcessInstanceId(), "poruka",
                "Rad \""+ mapa.get("naslov").toString()+"\" je uspešno prijavljen na recenziranje.");
        List<String> mejlovi = new ArrayList<>();
        mejlovi.add(r.getCasopis().getGlavniUrednik().getEmail());
        mejlovi.add(r.getAutor().getEmail());

        runtimeService.setVariable(task.getProcessInstanceId(), "radId", String.valueOf(r.getId()));
        runtimeService.setVariable(task.getProcessInstanceId(), "mejlovi", mejlovi);
        runtimeService.setVariable(task.getProcessInstanceId(), "email", r.getAutor().getEmail());
        runtimeService.setVariable(task.getProcessInstanceId(), "autor", r.getAutor().getUsername());
        runtimeService.setVariable(task.getProcessInstanceId(), "urednik", r.getCasopis().getGlavniUrednik().getUsername());
        formService.submitTaskForm(registerDTO.getTaskId(), mapa);
        System.out.println("KREIRAN RAD: " + r.toString());

    }

	@GetMapping
	public Collection<Rad> getAll() {
		return radService.getAll();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Rad getRad(@PathVariable Long id) {
		return radService.getRad(id);
	}

	@GetMapping(value = "/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) throws IOException {
		// Load file as Resource
        Rad r = radService.getRad(id);
        if(r==null)
                return ResponseEntity.badRequest().body(null);
        Resource resource;
        if(r.getAdresaKonacnogRada()!=null)
            resource = fileStorageService.loadFileAsResource(r.getAdresaKonacnogRada());
        else resource = fileStorageService.loadFileAsResource("C:\\Users\\hrcak\\Desktop\\NC_uploads\\"+r.getAdresaNacrta());

        System.out.println(resource.getURI().toString());

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header( HttpHeaders.LOCATION, resource.getURI().toString())
				.body(resource);
	}
/*

    @GetMapping(value = "/{id}/recenzenti", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecenzentIndexUnit> moguciRecenzenti(@PathVariable Long id){
	    Rad rad = radService.getRad(id);
	    Casopis c = rad.getCasopis();

	    List<RecenzentIndexUnit> ret = new ArrayList();
	    for(Korisnik r : c.getRecenzenti()){
	        RecenzentIndexUnit riu = recenzentIndexUnitRepository.findById(r.getId()).get();
	        ret.add(riu);
        }
        return ret;
    }
*/

   @GetMapping(value = "/{id}/index", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> indexRad(@PathVariable Long id){
       System.out.println("ZAVRSAVA PROCES OBJAVE RADA -  RAD PRIHVACEN");
	    //preuzeti procesnu instancu, submitovati odluku urednika, sledi ObavestenjeDelegate za prihvatanje rada
	    Rad rad = radService.getRad(id);
        rad.setAdresaKonacnogRada(rad.getAdresaNacrta());
        rad.setStatusRada(StatusRada.PRIHVACEN);
        rad.setDoi("10.1002/0470841" + String.valueOf((int)(Math.random()*100)) + ".ch1 ");
        radService.addRad(rad);

       ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(prijavaRadaProcessKey)
               .active()
               .variableValueEquals("radId", id)
               .singleResult();
       runtimeService.setVariable(pi.getProcessInstanceId(),"odluka", "objava");
       runtimeService.setVariable(pi.getProcessInstanceId(),"poruka", "Vaš rad \"" + radService.getRad(id).getNaslov() + "\" je objavljen.");
       Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
       System.out.println("ZAVRSAVA TASK: " + task.getName());
       formService.submitTaskForm(task.getId(), null);

       String ret = "Objavljen rad: \"" + rad.getNaslov() + "\"\r\n"+
               "Autor: " + rad.getAutor() + "\r\n" +
               "Apstrakt: \"..." + rad.getApstrakt() + "...\"\r\n" +
               "DOI: " + rad.getDoi();
       return new ResponseEntity(ret, HttpStatus.OK);

	    /*if(rad==null)
	        return new ResponseEntity("Greska u indeksiranju rada.", HttpStatus.BAD_REQUEST);
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
            return  new ResponseEntity("Greska u indeksiranju rada.", HttpStatus.BAD_REQUEST);
        }
        RadIndexUnit riu = new RadIndexUnit(rad.getId(), rad.getNaslov(), parsedText, rad.getAutor().getIme() + " " + rad.getAutor().getPrezime(), rad.getListaKoautora(), rad.getKljucniPojmovi(), rad.getApstrakt(), NaucnaOblast.normalized(rad.getNaucnaOblast()), rad.getCasopis().isOpenAccess(), rad.getCasopis().getNaziv(), rad.getCasopis().getId(), new GeoPoint(rad.getAutor().getLat(), rad.getAutor().getLon()));
        riu = riuRepository.save(riu);
        rad.setAdresaKonacnogRada("C:\\Users\\hrcak\\Desktop\\NC_uploads\\" + rad.getAdresaNacrta());
        rad.setStatusRada(StatusRada.PRIHVACEN);
        rad.setDoi("10.1002/0470841" + String.valueOf((int)(Math.random()*100)) + ".ch1 ");
        radService.addRad(rad);
        String ret = "Indeksiran rad: \"" + riu.getNaslov() + "\"\r\n"+
                    "Autor: " + riu.getAutor() + "\r\n" +
                    "Apstrakt: \"..." + riu.getApstrakt() + "...\"\r\n" +
                    "DOI: " + rad.getDoi();
        return new ResponseEntity(ret, HttpStatus.OK);*/
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity obrisiRad(@PathVariable Long id){
       System.out.println("ZAVRSAVA PROCES OBJAVE RADA -  RAD ODBIJEN");
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
                .variableValueEquals("radId", id)
                .singleResult();
        runtimeService.setVariable(pi.getId(),"odluka", "odbijanje");
        runtimeService.setVariable(pi.getId(),"poruka", "Vaš rad \"" + radService.getRad(id).getNaslov() + "\" je odbijen.");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        System.out.println("ZAVRSAVA TASK: " + task.getName());
        formService.submitTaskForm(task.getId(), null);
        radService.deleteRad(id);
        return new ResponseEntity("Rad je odbijen.", HttpStatus.OK);

    }

    @PostMapping(value = "/{id}/dorada", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> naDoradu(@PathVariable Long id, @RequestBody String komentar){
        System.out.println("USAO U PROCES OBJAVE RADA -  RAD ZA DORADU");
        Rad rad = radService.getRad(id);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
                .variableValueEquals("radId", id)
                .singleResult();
        runtimeService.setVariable(pi.getId(),"odluka", "dorada");
        runtimeService.setVariable(pi.getId(),"komentar", komentar);
        runtimeService.setVariable(pi.getId(),"poruka", "Vaš rad \"" + rad.getNaslov() + "\" je potrebno doraditi.");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        System.out.println("ZAVRSAVA TASK: " + task.getName());
        formService.submitTaskForm(task.getId(), null);
        rad.setStatusRada(StatusRada.KOREKCIJA);
        radService.addRad(rad);
        return new ResponseEntity("Rad je poslat na doradu.", HttpStatus.OK);
    }

    @RequestMapping(value="/{id}/novaVerzija", method=RequestMethod.POST )
    public ResponseEntity<String> singleSave(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam("odgovor") String odgovor ){
        System.out.println("Primio: " + odgovor);
        System.out.println("USAO U UPLOAD NOVE VERZIJE RADA");
        String fileLocation = fileStorageService.storeFile(file, true);
        System.out.println(fileLocation);
        Rad r = radService.getRad(id);
        r.setAdresaNacrta(fileLocation);
        r.setStatusRada(StatusRada.RECENZIRANJE);
        radService.addRad(r);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
                .variableValueEquals("radId", String.valueOf(id))
                .singleResult();
        runtimeService.setVariable(pi.getId(), "odgovor", odgovor);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        System.out.println("ZAVRSAVA TASK: " + task.getName());
        formService.submitTaskForm(task.getId(), null);
        return new ResponseEntity<String>("Odgovor uspešno zabeležen.", HttpStatus.OK);

    }
}
