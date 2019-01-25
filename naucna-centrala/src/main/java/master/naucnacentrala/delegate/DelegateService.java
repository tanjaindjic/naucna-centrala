package master.naucnacentrala.delegate;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.KorisnikService;

@Service
public class DelegateService {

	
	@Autowired
	private CasopisService casopisService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private TaskService taskService = null;

	public Korisnik getAssignee(DelegateExecution execution) {
		String username = getAssigneeName(execution);
		return korisnikService.getKorisnikByUsername(username);
	}
	
	public String getAssigneeName(DelegateExecution execution) {
		String activityId = execution.getCurrentActivityId();
		Task task = taskService.createTaskQuery().taskId(activityId).singleResult();
		return task.getAssignee();
	}
	
	public Casopis getCasopis(Long casopisId) {
		return casopisService.getCasopis(casopisId);
	}
	

}
