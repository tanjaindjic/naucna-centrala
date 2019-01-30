package master.naucnacentrala.controller;

import java.util.*;

import master.naucnacentrala.model.enums.NaucnaOblast;
import org.springframework.beans.factory.annotation.Autowired;
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
	public void addRad(@RequestBody Casopis r) {
		casopisService.addCasopis(r);
	}

	@GetMapping
	public Collection<Casopis> getAll() {
		return casopisService.getAll();
	}

	@GetMapping("/id")
	public Casopis getRad(@PathVariable Long id) {
		return casopisService.getCasopis(id);
	}

	@GetMapping("/naucneOblasti")
	public ArrayList<NaucnaOblast> getNaucneOblasti(){
		return new ArrayList<NaucnaOblast>(EnumSet.allOf(NaucnaOblast.class));
	}

}
