package master.naucnacentrala.service.serviceImpl;

import java.io.IOException;
import java.util.Collections;

import org.camunda.bpm.engine.RuntimeService;
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
   
    
	@Override
	public String startRegistrationProcess() throws JSONException {
		
		
		RestTemplate rt = new RestTemplate();
		String val = "{}";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(val, headers);
		ResponseEntity<String> response = rt.exchange(camundaUrl + "process-definition/key/registration_process/start", HttpMethod.POST, entity, String.class);
		System.out.println(response.getBody());
		JSONObject json = new JSONObject(response.getBody());
		System.out.println(json.getString("id"));
		return json.getString("id");
	}


	@Override
	public void startProcessEngine() throws Exception {
		Runtime.getRuntime().exec("cmd /c start \"\" C:\\Users\\hrcak\\Desktop\\startCamunda.lnk");
		
	}

}
