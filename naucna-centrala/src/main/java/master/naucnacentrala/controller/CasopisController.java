package master.naucnacentrala.controller;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import master.naucnacentrala.model.Kupovina;
import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.dto.CasopisDTO;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.KupovinaService;
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

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/casopis")
public class CasopisController {
	
	@Autowired
	private CasopisService casopisService;

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private KupovinaService kupovinaService;

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

	@GetMapping(value = "/{id}/korisnik/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CasopisDTO getCasopisUsername(@PathVariable Long id, @PathVariable String username) {

		Casopis c = casopisService.getCasopis(id);
		if(c==null)
			return null;
		if(username=="all")
			return new CasopisDTO(c, false, false);
		Korisnik k = korisnikService.getKorisnikByUsername(username);
		if(k==null)
			return new CasopisDTO(c, false, false);
		boolean kupljen = false;
		boolean uToku = false;
		for( Kupovina kupovina : kupovinaService.getKupovineKorisnika(k)){
			if((kupovina.getCasopis().getId()==c.getId()) && kupovina.getStatus().equals(Status.C))
				uToku = true;
		}
		if(!uToku){
			if(k.getPlaceniCasopisi().contains(c))
				kupljen = true;
			for(Pretplata p : k.getPretplaceniCasopisi())
				if(p.getCasopis().getId()==c.getId())
					kupljen = true;
		}
		System.out.println("kupljen: " + kupljen + ", u toku: " + uToku);
		return new CasopisDTO(c, kupljen, uToku);
	}

    @GetMapping(value = "/{id}/radovi", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Rad> getRadoviCasopisa(@PathVariable Long id) {
        return casopisService.getCasopis(id).getRadovi();
    }

	@GetMapping(value = "/{id}/objavljeniRadovi", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Rad> getObjRadoviCasopisa(@PathVariable Long id) {
		return casopisService.getCasopis(id).getRadovi().stream().filter(o -> o.getStatusRada().equals(StatusRada.PRIHVACEN)).collect(Collectors.toList());
	}

	@GetMapping(value = "/naucneOblasti", produces = MediaType.APPLICATION_JSON_VALUE )
	public ArrayList<NaucnaOblast> getNaucneOblasti(){
		return new ArrayList<NaucnaOblast>(EnumSet.allOf(NaucnaOblast.class));
	}

}
