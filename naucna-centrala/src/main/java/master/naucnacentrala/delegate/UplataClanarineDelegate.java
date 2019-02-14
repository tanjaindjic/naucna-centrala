package master.naucnacentrala.delegate;

import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.PretplataRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UplataClanarineDelegate implements JavaDelegate {

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private CasopisService casopisService;

	@Autowired
	private PretplataRepository pretplataRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("USAO U UPLATA CLANARINE DELEGATE");
		if(Math.random() < 0.8){
			Pretplata p = new Pretplata(casopisService.getCasopis(Long.parseLong(execution.getVariable("casopisId").toString())), new Date());
			pretplataRepository.save(p);
			Korisnik k = korisnikService.getKorisnikByUsername(execution.getVariable("username").toString());
			k.getPretplaceniCasopisi().add(p);
			korisnikService.addKorisnik(k);
			execution.setVariable("uspesnoPlacanje", true);

			System.out.println("CLANARINA USPESNO PLACENA");
		}else {
			execution.setVariable("uspesnoPlacanje", false);
			System.out.println("CLANARINA NIJE PLACENA");
		}
	}

}
