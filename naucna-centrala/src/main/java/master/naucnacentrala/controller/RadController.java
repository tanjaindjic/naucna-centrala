package master.naucnacentrala.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import master.naucnacentrala.model.dto.FormFieldsDTO;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.security.JwtTokenUtil;
import master.naucnacentrala.service.RadService;

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
	private JwtTokenUtil jwtTokenUtil;

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
    public ResponseEntity getCasopisi(@PathVariable String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        FormFieldsDTO dto = new FormFieldsDTO(task.getId(), task.getProcessInstanceId(), properties);
        return new ResponseEntity(dto, HttpStatus.OK);
    }


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addRad(@RequestHeader(value="Authorization") String Authorization) {
		String username = jwtTokenUtil.getUsernameFromToken(Authorization.substring(7));
	}

	@GetMapping
	public Collection<Rad> getAll() {
		return radService.getAll();
	}

	@GetMapping("/id")
	public Rad getRad(@PathVariable Long id) {
		return radService.getRad(id);
	}
	

}
