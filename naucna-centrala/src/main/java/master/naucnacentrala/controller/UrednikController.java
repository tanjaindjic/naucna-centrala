package master.naucnacentrala.controller;

import java.util.Collection;

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

}
