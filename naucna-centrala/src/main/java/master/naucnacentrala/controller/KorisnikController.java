package master.naucnacentrala.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.dto.*;
import master.naucnacentrala.repository.KupovinaRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.HelpService;
import master.naucnacentrala.service.RadService;/*
import org.apache.http.ParseException;*/
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.impl.util.json.JSONException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;/*
import org.elasticsearch.common.geo.GeoPoint;*/
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

	@Value("${camunda.objavaRadaProcessKey}")
	private String objavaRadaProcessKey;

	private String tokenHeader;

	@PostMapping
	public void addKorisnik(@RequestBody Korisnik k) {
		korisnikService.addKorisnik(k);
	}

	@GetMapping
	public Collection<Korisnik> getAll() {
		return korisnikService.getAll();
	}

	@GetMapping(value = "/{username}/placeniCasopisi", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Long> getPlaceniCasopisi(@PathVariable String username){
		return korisnikService.getPlaceniCasopisi(username);
	}

	@GetMapping(value = "/{username}/pretplate", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Long> getPretplate(@PathVariable String username){
		return korisnikService.getPretplate(username);
	}
	@GetMapping(value = "/{username}/placeniRadovi", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Long> getPlaceniRadovi(@PathVariable String username){
		return korisnikService.getPlaceniRadovi(username);
	}

	@GetMapping(value = "/{username}/recenziraniRadovi", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RadDTO> getRecenziraniRadovi(@PathVariable String username){
		Korisnik k = korisnikService.getKorisnikByUsername(username);
		List<Rad> radovi = radService.getRecenziraniRadovi(k.getId());
		List<RadDTO> retval = new ArrayList();
		for(Rad r : radovi){
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
					.variableValueEquals("radId", String.valueOf(r.getId()))
					.singleResult();

			retval.add(new RadDTO(r.getId(), r.getNaslov(), (List<String>) runtimeService.getVariable(pi.getProcessInstanceId(), "komentari"), null, null, null));

		}
		return retval;
	}


	@GetMapping(value = "/{username}")
	public KorisnikDTO getKorisnik(@PathVariable String username) {
		return new KorisnikDTO(korisnikService.getKorisnikByUsername(username));
	}
	
	@GetMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRegistrationFOrm() throws JSONException, UnsupportedOperationException, IOException, org.apache.tomcat.util.json.ParseException {
		System.out.println("ZAPOCINJE PROCES REGISTRACIJE");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(registrationProcessKey);
		System.out.println("ZAPOCET PROCES: " + registrationProcessKey);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		System.out.println("ZAPOCET TASK: " + task.getName());
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
		return new ResponseEntity<>(new FormFieldsDTO(task.getId(), pi.getId(), properties),HttpStatus.OK);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity finishRegistration(@RequestBody RegisterDTO registerDTO) {
		System.out.println("ZAVRSAVA PROCES REGISTRACIJE");
		Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
		System.out.println("ZAPOCINJE TASK: " + task.getName());
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		for(FieldIdValueDTO pair : registerDTO.getFormFields())
			mapa.put(pair.getFieldId(), pair.getFieldValue());
		
		formService.submitTaskForm(registerDTO.getTaskId(), mapa);
		Boolean valid = (Boolean) historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).variableName("valid").singleResult().getValue();
		if(valid) {
			System.out.println("USPESNA REGISTRACIJA KORISNIKA");
		    korisnikService.createUser(mapa.get("username").toString(), mapa.get("password").toString(), mapa.get("email").toString(),  44.7866, 20.4489, mapa.get("ime").toString(), mapa.get("prezime").toString(), mapa.get("drzava").toString(), mapa.get("grad").toString());
            return new ResponseEntity(HttpStatus.OK);
        }
		System.out.println("NEUSPESNA REGISTRACIJA KORISNIKA");
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/login")
	public ResponseEntity<?> getLoginPage(){
		System.out.println("ZAPOCET PROCES PRIJAVE: " + loginProcessKey);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(loginProcessKey);
		System.out.println("Zapocet proces " +loginProcessKey + ", id: " + pi.getId());
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		System.out.println("ZAPOCET TASK: " + task.getName());
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();

		return new ResponseEntity<>(new FormFieldsDTO(task.getId(), pi.getId(), properties),HttpStatus.OK);
	}

	@RequestMapping(value = "/noAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void initRegistration(@RequestBody RegisterDTO registerDTO) {
		System.out.println("USAO U PROCES REGISTRACIJE");
		Task task = taskService.createTaskQuery().taskId(registerDTO.getTaskId()).singleResult();
		System.out.println("SALJE PODATKE ZA REGISTRACIJU, TASK: " + task.getName());
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		for(FieldIdValueDTO pair : registerDTO.getFormFields())
			mapa.put(pair.getFieldId(), pair.getFieldValue());

		formService.submitTaskForm(registerDTO.getTaskId(), mapa);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitLoginData(@RequestBody RegisterDTO registerDTO) throws AuthenticationException, ParseException, IOException, JSONException {
		System.out.println("ZAVRSAVA PROCES PRIJAVE");
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

		System.out.println("Auth req: " + authenticationRequest.toString());
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
