package master.naucnacentrala.delegate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import master.naucnacentrala.model.dto.FieldIdValueDTO;
import master.naucnacentrala.service.KorisnikService;

public class RegistracijaDelegate implements JavaDelegate {
	
	
	private RuntimeService runtimeService;
	
	private IdentityService identityService;
	
	@Autowired
	private KorisnikService korisnikService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		identityService = execution.getProcessEngine().getIdentityService();
		runtimeService = execution.getProcessEngine().getRuntimeService();
		
		Map<String, Object> mapa = execution.getVariables();
		User userWithUsername = identityService.createUserQuery().userId(execution.getVariable("username").toString()) .singleResult();
		User userWithEmail = identityService.createUserQuery().userEmail(execution.getVariable("email").toString()).singleResult();
		
		if(userWithUsername==null && userWithEmail==null) {
			
			User newUser = identityService.newUser(execution.getVariable("username").toString());
			newUser.setEmail(execution.getVariable("email").toString());
			newUser.setFirstName(execution.getVariable("ime").toString());
			newUser.setLastName(execution.getVariable("prezime").toString());
			newUser.setPassword(execution.getVariable("password").toString());
			identityService.saveUser(newUser);
			execution.setVariable("valid", true);
			execution.setVariable("poruka", "Uspešna registracija - vaš nalog je spreman!");
					
		}else {
			execution.setVariable("valid", false);
			System.out.println("Postoji nalog");
		}

	}

}
