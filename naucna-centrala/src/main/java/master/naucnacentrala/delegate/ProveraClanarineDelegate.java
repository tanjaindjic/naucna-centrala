package master.naucnacentrala.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class ProveraClanarineDelegate implements JavaDelegate {

	@Autowired
	private DelegateService delegateService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		Long casopisId = (Long) execution.getVariable("casopisId");
		String username = delegateService.getAssigneeName(execution);
		//proveriti da li korisnik ima placenu clanarinu
		System.out.println("Id: " + casopisId + ", username: " + username);
		execution.setVariable("clanarinaPlacena", true);

	}

}
