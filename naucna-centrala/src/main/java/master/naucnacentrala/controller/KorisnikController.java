package master.naucnacentrala.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.client.RestTemplate;

import master.naucnacentrala.exception.AuthenticationException;
import master.naucnacentrala.model.dto.LoginDTO;
import master.naucnacentrala.model.dto.RegisterDTO;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.security.JwtAuthenticationRequest;
import master.naucnacentrala.security.JwtAuthenticationResponse;
import master.naucnacentrala.security.JwtTokenUtil;
import master.naucnacentrala.security.JwtUser;
import master.naucnacentrala.service.CamundaService;
import master.naucnacentrala.service.KorisnikService;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CamundaService camundaService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProcessEngineServices processEngineServices;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	private String tokenHeader;

	@PostMapping
	public void addKorisnik(@RequestBody Korisnik k) {
		korisnikService.addKorisnik(k);
	}

	@GetMapping
	public Collection<Korisnik> getAll() {
		return korisnikService.getAll();
	}

	@GetMapping("/{id}")
	public Korisnik getKorisnik(@PathVariable Long id) {
		return korisnikService.getKorisnik(id);
	}
	
	@GetMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRegistrationFOrm() throws JSONException, UnsupportedOperationException, IOException, org.apache.tomcat.util.json.ParseException {
		String processInstanceId = camundaService.startRegistrationProcess();
		Task task = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
		System.out.println("task id: " + task.getId());
		HashMap mapa = new HashMap();
		mapa.put("taskId", task.getId());
		return new ResponseEntity<HashMap>(mapa, HttpStatus.OK);
		
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createAuthenticationToken(@RequestBody RegisterDTO registerDTO) {
		
		List<User> users = identityService.createUserQuery().userId(registerDTO.getUsername()).list();
		List<User> mails = identityService.createUserQuery().userEmail(registerDTO.getEmail()).list();
		
		Task task = taskService.createTaskQuery().active().processInstanceId(registerDTO.getTaskId()).list().get(0);
		HashMap<String, Object> map = new HashMap<String, Object>();
		//for(FieldIdNamePairDto pair:dto)
			//map.put(pair.getFieldId(), pair.getFieldValue());
		
		//runtimeService.setVariable(task.getProcessInstanceId(), "registerData", dto);
		formService.submitTaskForm(registerDTO.getTaskId(), map);
		return new ResponseEntity<>(HttpStatus.OK);
		
		/*
		 * if(users.isEmpty() && mails.isEmpty()) { User user =
		 * identityService.newUser(registerDTO.getUsername());
		 * user.setEmail(registerDTO.getEmail());
		 * user.setPassword(registerDTO.getPass()); identityService.saveUser(user);
		 * 
		 * runtimeService.setVariable(task.getExecutionId(), "email",
		 * registerDTO.getEmail()); runtimeService.setVariable(task.getExecutionId(),
		 * "valid", true);
		 * 
		 * taskService.complete(task.getId()); return new ResponseEntity(
		 * HttpStatus.OK);
		 * 
		 * } runtimeService.setVariable(task.getExecutionId(), "valid", false);
		 * taskService.complete(task.getId()); return new
		 * ResponseEntity(HttpStatus.BAD_REQUEST);
		 */
		
		
		/*
		 * JwtAuthenticationRequest authenticationRequest = new
		 * JwtAuthenticationRequest(registerDTO.getUsername(), registerDTO.getPass());
		 * 
		 * Boolean camundaUserExists =
		 * korisnikService.verifyOnCamunda(authenticationRequest);
		 * 
		 * if (camundaUserExists) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		 * 
		 * System.out.println("HIHI"); System.out.println("Registration process id: " +
		 * registerDTO.getRegistrationProcessId()); return new
		 * ResponseEntity<>(HttpStatus.OK);
		 */
		
		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException, ParseException, IOException, JSONException {
		
		Boolean camundaUserExists = korisnikService.verifyOnCamunda(authenticationRequest);

		if (camundaUserExists){

			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			Korisnik k = korisnikService.getKorisnikByUsername(authenticationRequest.getUsername());

			// Reload password post-security so we can generate the token
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails);

			// Return the token
			return ResponseEntity.ok(new JwtAuthenticationResponse(token));
			
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
