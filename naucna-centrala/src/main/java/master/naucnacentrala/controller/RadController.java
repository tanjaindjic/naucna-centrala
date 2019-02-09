package master.naucnacentrala.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.dto.FieldIdValueDTO;
import master.naucnacentrala.model.dto.FormFieldsDTO;
import master.naucnacentrala.model.dto.RegisterDTO;
import master.naucnacentrala.model.dto.UploadFileResponse;
import master.naucnacentrala.model.elastic.RecenzentIndexUnit;
import master.naucnacentrala.model.elastic.RecenzijaIndexUnit;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Recenzent;
import master.naucnacentrala.repository.RecenzentIndexUnitRepository;
import master.naucnacentrala.repository.RecenzijaIndexUnitRepository;
import master.naucnacentrala.repository.RecenzijaRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.FileStorageService;
import master.naucnacentrala.service.KorisnikService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.form.FormFieldImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
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

    @Autowired
    private RecenzentIndexUnitRepository recenzentIndexUnitRepository;

	@Value("${camunda.prijavaRadaProcessKey}")
	private String prijavaRadaProcessKey;

	@GetMapping(value = "prijavaRada")
	public ResponseEntity<?> startPrijava(@RequestHeader(value="Authorization") String Authorization) throws URISyntaxException {
		System.out.println("Zapocinje proces: prijava rada");
		System.out.println(Authorization);
		String username = "";
		if(Authorization.length()>7)
			username = jwtTokenUtil.getUsernameFromToken(Authorization.substring(7));
		System.out.println(username);
		Map mapa = new HashMap();
		if(username.equals(""))
			mapa.put("ulogovan", false);
		else mapa.put("ulogovan", true);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(prijavaRadaProcessKey, UUID.randomUUID().toString(), mapa);

		if(username.equals("")){
		    //FIXME ne znam kako da dodjem do subprocesa i posaljem na login
		   /* ProcessInstance sub = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(pi.getBusinessKey(), "loginProcess").singleResult();
            Task task = taskService.createTaskQuery().processInstanceId(sub.getId()).list().get(0);
            TaskFormData tfd = formService.getTaskFormData(task.getId());
            List<FormField> properties = tfd.getFormFields();
            ResponseEntity re = new ResponseEntity(new FormFieldsDTO(task.getId(), pi.getId(), properties), HttpStatus.OK);
            re.getHeaders().set("Location", "login");
            return re;*/
			FormFieldsDTO dto = new FormFieldsDTO(null, null, null);
			dto.setLocation("login");
		   return new ResponseEntity<>(dto, HttpStatus.OK);

		}else {
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
			task.setAssignee(username);
			taskService.saveTask(task);
            FormFieldsDTO dto = new FormFieldsDTO(task.getId(), pi.getId(), null);
			dto.setLocation("noviRad");
            ResponseEntity re =  new ResponseEntity(dto, HttpStatus.OK);

            return re;
		}

	}

    @GetMapping(value = "odabirCasopisa/{taskId}")
    public ResponseEntity<?> getCasopisi(@PathVariable String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        FormFieldsDTO dto = new FormFieldsDTO(task.getId(), task.getProcessInstanceId(), properties);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PostMapping(value = "odabirCasopisa", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setCasopis(@RequestBody RegisterDTO registerDTO){
	    System.out.println(registerDTO.toString());
	    System.out.println("Odabrao casopis");

        Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
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
            System.out.println("Zapocet task " + task.getName());
            task.setAssignee(assignee);
            taskService.saveTask(task);
            TaskFormData tfd = formService.getTaskFormData(task.getId());
            List<FormField> properties = tfd.getFormFields();
            FormFieldsDTO dto = new FormFieldsDTO(task.getId(), task.getProcessInstanceId(), properties);
            return new ResponseEntity(dto, HttpStatus.OK);
        }
        else return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/nacrt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadNacrt(@RequestParam("file") MultipartFile file) {
        String fileLocation = fileStorageService.storeFile(file, true);
        System.out.println(fileLocation);
        return new ResponseEntity<String>(fileLocation, HttpStatus.OK);
    }

    @PostMapping(value = "/final", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFinal(@RequestParam("file") MultipartFile file) {
        String fileLocation = fileStorageService.storeFile(file, false);

        return new ResponseEntity<String>(fileLocation, HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void noviRad(@RequestBody RegisterDTO registerDTO){
        Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
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
        radService.addRad(r);

        runtimeService.setVariable(task.getProcessInstanceId(), "poruka",
                "Rad \""+ mapa.get("naslov").toString()+"\" je uspešno prijavljen na recenziranje.");
        List<String> mejlovi = new ArrayList<>();
        mejlovi.add(r.getCasopis().getGlavniUrednik().getEmail());
        mejlovi.add(r.getAutor().getEmail());
        runtimeService.setVariable(task.getProcessInstanceId(), "mejlovi", mejlovi);


        formService.submitTaskForm(registerDTO.getTaskId(), mapa);
        System.out.println("Kreiran rad: " + r.toString());

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
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
		// Load file as Resource
        Rad r = radService.getRad(id);
        if(r==null)
                return ResponseEntity.badRequest().body(null);
		Resource resource = fileStorageService.loadFileAsResource(r.getAdresaKonacnogRada());

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
				.body(resource);
	}

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

}
