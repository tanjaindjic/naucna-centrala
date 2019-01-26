package master.naucnacentrala.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.security.JwtAuthenticationRequest;

public interface KorisnikService {

	public Korisnik addKorisnik(Korisnik k);
	public Korisnik getKorisnik(Long id);
	public Korisnik getKorisnikByUsername(String username);
	public Korisnik updateKorisnik(Korisnik k);
	public Collection<Korisnik> getAll();
	public Boolean verifyOnCamunda(JwtAuthenticationRequest authenticationRequest) throws UnsupportedEncodingException, ClientProtocolException, IOException, JSONException;
	void createUser(String username, String pass, String email, String ime, String prezime, String drzava, String grad);

}
