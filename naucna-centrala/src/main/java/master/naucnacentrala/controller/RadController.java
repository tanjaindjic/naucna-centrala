package master.naucnacentrala.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.service.RadService;

@RestController
@RequestMapping("/rad")
public class RadController {

	@Autowired
	private RadService radService;

	@PostMapping
	public void addRad(@RequestBody Rad r) {
		radService.addRad(r);
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
