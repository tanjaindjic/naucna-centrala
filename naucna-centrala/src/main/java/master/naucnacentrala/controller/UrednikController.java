package master.naucnacentrala.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.dto.RadDTO;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private RuntimeService runtimeService;

	@Value("${camunda.prijavaRadaProcessKey}")
	private String prijavaRadaProcessKey;

	@Value("${camunda.objavaRadaProcessKey}")
	private String objavaRadaProcessKey;

	@PostMapping
	public void addUrednik(@RequestBody Urednik k) {
		urednikService.addUrednik(k);
	}

	@GetMapping
	public Collection<Urednik> getAll() {
		return urednikService.getAll();
	}

	@GetMapping("/{id}")
	public Urednik getUrednik(@PathVariable Long id) {
		return urednikService.getUrednik(id);
	}

	@GetMapping("/{username}/noviRadovi")
	public List<Rad> getNoviRadovi(@PathVariable String username) {

		Urednik u =  urednikService.getUrednikByUsername(username);
		return radService.getRadZaUrednika(u.getUredjuje(), StatusRada.NOVO);
	}

	@GetMapping("/{username}/recenzije")
	public List<RadDTO> getMojeRecenzije(@PathVariable String username) {

		Urednik u =  urednikService.getUrednikByUsername(username);
		List<Rad> radovi =  radService.getRadZaUrednika(u.getUredjuje(), StatusRada.RECENZIRANJE);
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


}
