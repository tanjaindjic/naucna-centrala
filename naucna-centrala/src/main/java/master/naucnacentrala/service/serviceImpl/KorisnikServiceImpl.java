package master.naucnacentrala.service.serviceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.KorisnikRepository;
import master.naucnacentrala.security.JwtAuthenticationRequest;
import master.naucnacentrala.service.KorisnikService;

@Service
public class KorisnikServiceImpl implements KorisnikService{
	
	private Algorithm algorithm = Algorithm.HMAC256("secret");
	
	@Autowired
	private KorisnikRepository korisnikRepository;

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

		HttpEntity entity = new StringEntity(authenticationRequest.toJson());
		HttpPost post = new HttpPost("http://localhost:8080/engine-rest/identity/verify");
		post.setEntity(entity);
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "text/plain, application/json");
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		HttpClient client = clientBuilder.build();
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();

		String responseBody = EntityUtils.toString(response.getEntity());
		JSONObject jsonObj = new JSONObject(responseBody);
		System.out.println("Autenthicated: " + jsonObj.getString("authenticated"));
		return jsonObj.getBoolean("authenticated");
	}


	@Override
	public void createUser(String username, String pass, String email, String ime, String prezime, String drzava, String grad) {
		korisnikRepository.save(new Korisnik(username, pass, ime, prezime, grad, drzava, email, new ArrayList<>(), new ArrayList<>() ));
	}

}
