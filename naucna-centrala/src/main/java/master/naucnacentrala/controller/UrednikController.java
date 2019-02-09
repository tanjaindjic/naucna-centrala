package master.naucnacentrala.controller;

import java.util.Collection;
import java.util.List;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.service.RadService;
import org.springframework.beans.factory.annotation.Autowired;
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


}
