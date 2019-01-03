package master.naucnacentrala.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.IdentityService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import master.naucnacentrala.model.dto.LoginDTO;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.service.KorisnikService;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private IdentityService identityService;

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
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void login(@RequestBody LoginDTO dto) throws ClientProtocolException, IOException, JSONException {
		System.out.println("Primio: " + dto.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity entity = new StringEntity(dto.toJson());
		HttpPost post = new HttpPost("http://localhost:8080/engine-rest/identity/verify");
		post.setEntity(entity);
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept","text/plain, application/json");
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		HttpClient client = clientBuilder.build();
		HttpResponse response = client.execute(post);
		// Getting the status code.
		int statusCode = response.getStatusLine().getStatusCode();

		// Getting the response body.
		String responseBody = EntityUtils.toString(response.getEntity());
		JSONObject jsonObj = new JSONObject(responseBody);
		System.out.println("Saljem: " + dto.toJson());
		System.out.println("status: " + statusCode + " , responseBody: " + responseBody);
		System.out.println("Autenthicated: " + jsonObj.getString("authenticated"));//ako je true setovati token ili cookie
	}

}
