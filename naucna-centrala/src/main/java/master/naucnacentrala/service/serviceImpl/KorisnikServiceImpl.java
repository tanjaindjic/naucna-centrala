package master.naucnacentrala.service.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.util.json.JSONException;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.KorisnikRepository;
import master.naucnacentrala.security.JwtAuthenticationRequest;
import master.naucnacentrala.service.KorisnikService;

@Service
public class KorisnikServiceImpl implements KorisnikService{
	
	private Algorithm algorithm = Algorithm.HMAC256("secret");

	@Value("${camunda.url}")
	private String camundaUrl;
	
	@Autowired
	private KorisnikRepository korisnikRepository;

	@Autowired
	private IdentityService identityService;

	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	@Override
	public Korisnik addKorisnik(Korisnik k) {
		return korisnikRepository.save(k);
	}

	@Override
	public Korisnik getKorisnik(Long id) {
		// TODO Auto-generated method stub
		return korisnikRepository.getOne(id);
	}
	

	@Override
	public Korisnik updateKorisnik(Korisnik k) {
		return korisnikRepository.save(k);
	}

	@Override
	public Collection<Korisnik> getAll() {
		return korisnikRepository.findAll();
	}


	@Override
	public Korisnik getKorisnikByUsername(String username) {
		// TODO Auto-generated method stub
		return korisnikRepository.findByUsernameIgnoreCase(username);
	}

	@Override
	public Boolean verifyOnCamunda(JwtAuthenticationRequest authenticationRequest) throws ClientProtocolException, IOException, JSONException {

		User u = identityService.createUserQuery().userId(authenticationRequest.getUsername()).singleResult();
		System.out.println("is user null? " + u);
		HttpEntity entity = new StringEntity(authenticationRequest.toJson());
		HttpPost post = new HttpPost(camundaUrl + "identity/verify");
		post.setEntity(entity);
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "text/plain, application/json");
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		HttpClient client = clientBuilder.build();
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();

		String responseBody = EntityUtils.toString(response.getEntity());
		JSONObject jsonObj = new JSONObject(responseBody);
		if(jsonObj==null)
			return false;
		System.out.println("Autenthicated: " + jsonObj.getString("authenticated"));
		return jsonObj.getBoolean("authenticated");
	}


	@Override
	public void createUser(String username, String pass, String email, String ime, String prezime, String drzava, String grad) {
		korisnikRepository.save(new Korisnik(username, bcrypt.encode(pass), ime, prezime, grad, drzava, email, new ArrayList<>(), new ArrayList<>(), new ArrayList<>() ));
	}

}
