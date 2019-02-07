package master.naucnacentrala.controller;

import java.util.*;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.NaucnaOblast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.service.CasopisService;

@RestController
@RequestMapping("/casopis")
public class CasopisController {
	
	@Autowired
	private CasopisService casopisService;

	@PostMapping
	public void addCasopis(@RequestBody Casopis r) {
		casopisService.addCasopis(r);
	}

	@GetMapping
	public Collection<Casopis> getAll() {
		return casopisService.getAll();
	}


	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Casopis getCasopis(@PathVariable Long id) {
		return casopisService.getCasopis(id);
	}

    @GetMapping(value = "/{id}/radovi", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Rad> getRadoviCasopisa(@PathVariable Long id) {
        return casopisService.getCasopis(id).getRadovi();
    }

	@GetMapping(value = "/naucneOblasti", produces = MediaType.APPLICATION_JSON_VALUE )
	public ArrayList<NaucnaOblast> getNaucneOblasti(){
		return new ArrayList<NaucnaOblast>(EnumSet.allOf(NaucnaOblast.class));
	}

}
