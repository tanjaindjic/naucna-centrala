package master.naucnacentrala.service;

import java.io.IOException;

import org.apache.tomcat.util.json.ParseException;
import org.camunda.bpm.engine.ProcessEngine;
import org.json.JSONException;

public interface CamundaService {
	
	public void startProcessEngine() throws Exception;
	public String startRegistrationProcess() throws JSONException, UnsupportedOperationException, IOException, ParseException;


}
