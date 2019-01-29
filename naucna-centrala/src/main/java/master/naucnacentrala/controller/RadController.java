package master.naucnacentrala.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping("/rad")
public class RadController {

	@Autowired
	private RadService radService;
	
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping(value = "prijavaRada", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void startPrijava(@RequestHeader(value="Authorization") String Authorization) {
		String username = jwtTokenUtil.getUsernameFromToken(Authorization.substring(7));
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
