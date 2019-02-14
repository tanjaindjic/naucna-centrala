package master.naucnacentrala.delegate;

import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.service.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class ProveraClanarineDelegate implements JavaDelegate {

	@Autowired
	private DelegateService delegateService;

	@Autowired
	private KorisnikService korisnikService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("USAO U PROVERA CLANARINE DELEGATE");
		Long casopisId = Long.parseLong(execution.getVariable("casopisId").toString());
		String username =execution.getVariable("username").toString();
		//proveriti da li korisnik ima placenu clanarinu
		System.out.println("Id: " + casopisId + ", username: " + username);
		Collection<Pretplata> clanarine= korisnikService.getKorisnikByUsername(username).getPretplaceniCasopisi();
		Boolean placeno = false;
		for(Pretplata p : clanarine)
			if(p.getCasopis().getId()==casopisId)
				if(p.getVaziDo().after(new Date()))
					placeno = true;
		System.out.println("CLANARINA PLACENA: " + placeno);
		execution.setVariable("clanarinaPlacena", placeno);

	}

}
