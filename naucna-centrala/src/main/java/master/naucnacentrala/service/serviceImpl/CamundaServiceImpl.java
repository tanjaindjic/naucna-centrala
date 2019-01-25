package master.naucnacentrala.service.serviceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import master.naucnacentrala.service.CamundaService;

@Service
public class CamundaServiceImpl implements CamundaService {

	@Value("${camunda.url}")
	private String camundaUrl;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Override
	public String startRegistrationProcess() throws JSONException {

		ProcessInstantiationBuilder builder = runtimeService.createProcessInstanceByKey("registration_process");

		ProcessInstance instance = builder.execute();
		Task task = taskService.createTaskQuery().active().processInstanceId(instance.getId()).list().get(0);

		return instance.getProcessInstanceId();

		/*
		 * RestTemplate rt = new RestTemplate(); String val = "{}";
		 * 
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Content-Type",
		 * "application/json");
		 * headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		 * HttpEntity<String> entity = new HttpEntity<>(val, headers);
		 * ResponseEntity<String> response = rt.exchange(camundaUrl +
		 * "process-definition/key/registration_process/start", HttpMethod.POST, entity,
		 * String.class); System.out.println(response.getBody()); JSONObject json = new
		 * JSONObject(response.getBody()); System.out.println(json.getString("id"));
		 * return json.getString("id");
		 */
	}

	@Override
	public void startProcessEngine() throws Exception {
		Runtime.getRuntime().exec("cmd /c start \"\" C:\\Users\\hrcak\\Desktop\\startCamunda.lnk");

	}

}
