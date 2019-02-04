package master.naucnacentrala.delegate;

import master.naucnacentrala.security.JwtAuthenticationRequest;
import master.naucnacentrala.security.JwtAuthenticationResponse;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PrijavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Zapocet task: " + execution.getCurrentActivityName());
        JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest();
        jwtAuthenticationRequest.setUsername(execution.getVariable("username").toString());
        jwtAuthenticationRequest.setPassword(execution.getVariable("password").toString());
        RestTemplate rt = new RestTemplate();
        ResponseEntity response = rt.postForEntity("https://localhost:8096/korisnik/finishLogin", jwtAuthenticationRequest, String.class);

        if(response.getStatusCode()== HttpStatus.OK)
            execution.setVariable("token", response.getBody());
        else execution.setVariable("token", "");
    }
}
