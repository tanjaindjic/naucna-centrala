package master.naucnacentrala.controller;

import java.io.IOException;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Kupovina;
import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.dto.*;
import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.repository.KupovinaRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.HelpService;
import master.naucnacentrala.service.RadService;
import org.apache.http.ParseException;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.impl.util.json.JSONException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import master.naucnacentrala.exception.AuthenticationException;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.security.JwtAuthenticationRequest;
import master.naucnacentrala.security.JwtAuthenticationResponse;
import master.naucnacentrala.security.JwtTokenUtil;
import master.naucnacentrala.security.JwtUser;
import master.naucnacentrala.service.KorisnikService;
import org.springframework.web.client.RestTemplate;

import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;

	@Autowired
	private HistoryService historyService;

	@Autowired
    private AuthorizationService authorizationService;

	@Autowired
	private CasopisService casopisService;

	@Autowired
	private KupovinaRepository kupovinaRepository;

	@Autowired
	private RadService radService;

	@Autowired
	private HelpService helpService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${camunda.registrationProcessKey}")
	private String registrationProcessKey;

	@Value("${camunda.loginProcessKey}")
	private String loginProcessKey;

	private String tokenHeader;

	@PostMapping
	public void addKorisnik(@RequestBody Korisnik k) {
		korisnikService.addKorisnik(k);
	}

	@GetMapping
	public Collection<Korisnik> getAll() {
		return korisnikService.getAll();
	}

	@GetMapping(value = "/{username}")
	public Korisnik getKorisnik(@PathVariable String username) {
	    System.out.println("usao u get korisnika: " + username);
	    System.out.println("Vracam: " + korisnikService.getKorisnikByUsername(username));
		return korisnikService.getKorisnikByUsername(username);
	}
	
	@GetMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRegistrationFOrm() throws JSONException, UnsupportedOperationException, IOException, org.apache.tomcat.util.json.ParseException {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(registrationProcessKey);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
		return new ResponseEntity<>(new FormFieldsDTO(task.getId(), pi.getId(), properties),HttpStatus.OK);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity finishRegistration(@RequestBody RegisterDTO registerDTO) {

		Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		for(FieldIdValueDTO pair : registerDTO.getFormFields())
			mapa.put(pair.getFieldId(), pair.getFieldValue());
		
		formService.submitTaskForm(registerDTO.getTaskId(), mapa);
		Boolean valid = (Boolean) historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).variableName("valid").singleResult().getValue();
		if(valid) {
		    korisnikService.createUser(mapa.get("username").toString(), mapa.get("password").toString(), mapa.get("email").toString(), mapa.get("ime").toString(), mapa.get("prezime").toString(), mapa.get("drzava").toString(), mapa.get("grad").toString());
            return new ResponseEntity(HttpStatus.OK);
        }
		else return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/login")
	public ResponseEntity<?> getLoginPage(){
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(loginProcessKey);
		System.out.println("Zapocet proces " +loginProcessKey + ", id: " + pi.getId());
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        System.out.println("Zapocet task " +task.getName() + ", id: " + task.getId());
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();

		return new ResponseEntity<>(new FormFieldsDTO(task.getId(), pi.getId(), properties),HttpStatus.OK);
	}

	@RequestMapping(value = "/noAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void initRegistration(@RequestBody RegisterDTO registerDTO) {

		Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		for(FieldIdValueDTO pair : registerDTO.getFormFields())
			mapa.put(pair.getFieldId(), pair.getFieldValue());

		formService.submitTaskForm(registerDTO.getTaskId(), mapa);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitLoginData(@RequestBody RegisterDTO registerDTO) throws AuthenticationException, ParseException, IOException, JSONException {

	    System.out.println("primljen " + registerDTO.toString());
        HashMap<String, Object> mapa = new HashMap<String, Object>();
        for(FieldIdValueDTO pair : registerDTO.getFormFields())
            mapa.put(pair.getFieldId(), pair.getFieldValue());

        formService.submitTaskForm(registerDTO.getTaskId(), mapa);
        String token = (String) historyService.createHistoricVariableInstanceQuery().processInstanceId(registerDTO.getProcessInstanceId()).variableName("token").singleResult().getValue();
        if(token.equals(""))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else return ResponseEntity.ok(new JwtAuthenticationResponse(token));

    }

    @RequestMapping(value = "/finishLogin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException, ParseException, IOException, JSONException {

        Boolean camundaUserExists = korisnikService.verifyOnCamunda(authenticationRequest);

        if (camundaUserExists){
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            // Reload password post-security so we can generate the token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            Authentication auth = new Authentication(authenticationRequest.getUsername(), null);
            identityService.setAuthentication(auth);
            System.out.println("Auth pre: " + identityService.getCurrentAuthentication().getUserId());
            return ResponseEntity.ok(token);

        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

	@GetMapping(value = "logout")
    public void logout(){
	    //TODO ne radi authentification, vezano samo za jedan thread, ne znam kako da pamtim ulogovanog korisnika na kamundi
        //System.out.println("Auth pre: " + identityService.getCurrentAuthentication().getUserId());
	    identityService.clearAuthentication();
	    //System.out.println("Auth posle: " + identityService.getCurrentAuthentication());
    }

	@RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	/**
	 * Authenticates the user. If something is wrong, an
	 * {@link AuthenticationException} will be thrown
	 */
	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("User is disabled!", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Bad credentials!", e);
		}
	}

}
