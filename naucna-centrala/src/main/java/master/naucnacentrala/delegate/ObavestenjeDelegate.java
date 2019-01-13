package master.naucnacentrala.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.service.CasopisService;

public class ObavestenjeDelegate implements JavaDelegate {
	
	@Autowired
	private DelegateService delegateService;
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		Long casopisId = (Long) execution.getVariable("casopisId");
		Urednik urednik = delegateService.getCasopis(casopisId).getGlavniUrednik();
		Korisnik korisnik = delegateService.getAssignee(execution);
		//pozvati email service
	}

}
