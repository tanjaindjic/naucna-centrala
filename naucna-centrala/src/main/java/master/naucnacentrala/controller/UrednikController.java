package master.naucnacentrala.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.dto.RadDTO;
import master.naucnacentrala.model.dto.RevizijaDTO;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Recenzent;
import master.naucnacentrala.service.RadService;
import master.naucnacentrala.service.RecenzijaService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.service.UrednikService;

@RestController
@RequestMapping("/urednik")
public class UrednikController {
	
	@Autowired
	private UrednikService urednikService;

	@Autowired
	private RadService radService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Value("${camunda.prijavaRadaProcessKey}")
	private String prijavaRadaProcessKey;

	@Value("${camunda.objavaRadaProcessKey}")
	private String objavaRadaProcessKey;

	@Autowired
	private RecenzijaService recenzijaService;

	@PostMapping
	public void addUrednik(@RequestBody Urednik k) {
		urednikService.addUrednik(k);
	}

	@GetMapping
	public Collection<Urednik> getAll() {
		return urednikService.getAll();
	}

	@GetMapping(value = "/{id}")
	public Urednik getUrednik(@PathVariable Long id) {
		return urednikService.getUrednik(id);
	}

	@GetMapping(value = "/{username}/noviRadovi", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Rad> getNoviRadovi(@PathVariable String username) {

		Urednik u =  urednikService.getUrednikByUsername(username);
		return radService.getRadZaUrednika(u.getUredjuje(), StatusRada.NOVO);
	}

	@GetMapping(value = "/{username}/dorade", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RadDTO> dorade(@PathVariable String username) {

		Urednik u =  urednikService.getUrednikByUsername(username);
		List<Rad> radovi =  radService.getRadZaUrednika(u.getUredjuje(), StatusRada.KOREKCIJA_UREDNIK);
		List<RadDTO> retval = new ArrayList();
		for(Rad r : radovi){
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
					.variableValueEquals("radId", String.valueOf(r.getId()))
					.singleResult();

			retval.add(new RadDTO(r.getId(), r.getNaslov(), (List<String>) runtimeService.getVariable(pi.getProcessInstanceId(), "komentari"),
					r.getAutor().getIme() + " " + r.getAutor().getPrezime(), r.getNaucnaOblast().name(),
					runtimeService.getVariable(pi.getId(), "odgovor").toString() ));

		}
		return retval;
	}

	@GetMapping(value = "/{username}/naCekanju", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RadDTO> getNaCekanju(@PathVariable String username) {

		Urednik u =  urednikService.getUrednikByUsername(username);
		List<Rad> radovi =  radService.getRadZaUrednika(u.getUredjuje(), StatusRada.DODELA_RECENZENATA);
		List<RadDTO> retval = new ArrayList();
		for(Rad r : radovi){
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
					.variableValueEquals("radId", String.valueOf(r.getId()))
					.singleResult();
			if(runtimeService.getVariable(pi.getId(), "urednikNaucneOblasti").toString().equals(username))
				retval.add(new RadDTO(r.getId(), r.getNaslov(), null,
					r.getAutor().getIme() + " " + r.getAutor().getPrezime(), r.getNaucnaOblast().name(),null));

		}
		return retval;
	}

	@GetMapping(value = "/{username}/zaReviziju" , produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RevizijaDTO> zaReviziju(@PathVariable String username){

		List<ProcessInstance> pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
				.variableValueEquals("urednikNaucneOblasti", username)
				.list();
		List<RevizijaDTO> revizije = new ArrayList<>();
		for(ProcessInstance p : pi){
			Task task = taskService.createTaskQuery().processInstanceId(p.getId()).list().get(0);
			if(task.getName().equals("revizija")){
				RevizijaDTO revizijaDTO = new RevizijaDTO();
				revizijaDTO.setRad(radService.getRad(Long.parseLong(runtimeService.getVariable(p.getId(), "radId").toString())));
				revizijaDTO.setRecenzije(new ArrayList<>());
				revizijaDTO.getRecenzije().addAll(recenzijaService.findByRadId(Long.parseLong(runtimeService.getVariable(p.getId(), "radId").toString())));
				revizije.add(revizijaDTO);
			}
		}
		return revizije;
	}



}
