package master.naucnacentrala.delegate;

import master.naucnacentrala.security.JwtAuthenticationRequest;
import master.naucnacentrala.security.JwtAuthenticationResponse;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.web.client.RestTemplate;

public class PrijavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest();
        jwtAuthenticationRequest.setUsername(execution.getVariable("username").toString());
        jwtAuthenticationRequest.setPassword(execution.getVariable("password").toString());
        RestTemplate rt = new RestTemplate();
        rt.postForEntity("http://localhost:8096/korisnik/login", jwtAuthenticationRequest, JwtAuthenticationResponse.class);

    }
}
