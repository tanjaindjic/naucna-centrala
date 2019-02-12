package master.naucnacentrala.model.korisnici;
import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.model.Rad;/*
import org.elasticsearch.common.geo.GeoPoint;*/

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Korisnik {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String username;

	@NotNull
	@Column(nullable = false)
	private String pass;

	@NotNull
	@Column(nullable = false)
	private String ime;

	@NotNull
	@Column(nullable = false)
	private String prezime;

	@NotNull
	@Column(nullable = false)
	private String grad;

	@NotNull
	@Column(nullable = false)
	private String drzava;

	@NotNull
	@Column(nullable = false)
	private String email;

	@NotNull
	@Column(nullable = false)
	private Double lat;

	@NotNull
	@Column(nullable = false)
	private Double lon;

	@Column
	@ManyToMany
	private Collection<Pretplata> pretplaceniCasopisi;

	@Column
	@ManyToMany
	private Collection<Rad> placeniRadovi;

	@Column
	@ManyToMany
	private Collection<Casopis> placeniCasopisi;

	/*
	 * @ManyToMany
	 * 
	 * @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name =
	 * "user_id", referencedColumnName = "id"), inverseJoinColumns
	 * = @JoinColumn(name = "role_id", referencedColumnName = "id")) private
	 * Collection<Role> roles;
	 */

	public Korisnik() {
	}

	
	
	public Korisnik(@NotNull String username, @NotNull String pass, @NotNull String ime, @NotNull String prezime,
					@NotNull String grad, @NotNull String drzava, @NotNull String email, @NotNull Double lat, Double lon, Collection<Pretplata> pretplaceniCasopisi, Collection<Casopis> placeniCasopisi,
					Collection<Rad> placeniRadovi) {
		super();
		this.username = username;
		this.pass = pass;
		this.ime = ime;
		this.prezime = prezime;
		this.grad = grad;
		this.drzava = drzava;
		this.email = email;
		this.lat = lat;
		this.lon = lon;
		this.pretplaceniCasopisi = pretplaceniCasopisi;
		this.placeniCasopisi = placeniCasopisi;
		this.placeniRadovi = placeniRadovi;
	}



	public Long getId() {
		return id;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Casopis> getPlaceniCasopisi() {
		return placeniCasopisi;
	}

	public void setPlaceniCasopisi(Collection<Casopis> placeniCasopisi) {
		this.placeniCasopisi = placeniCasopisi;
	}

	public Collection<Rad> getPlaceniRadovi() {
		return placeniRadovi;
	}

	public void setPlaceniRadovi(Collection<Rad> placeniRadovi) {
		this.placeniRadovi = placeniRadovi;
	}

	public Collection<Pretplata> getPretplaceniCasopisi() {
		return pretplaceniCasopisi;
	}

	public void setPretplaceniCasopisi(Collection<Pretplata> pretplaceniCasopisi) {
		this.pretplaceniCasopisi = pretplaceniCasopisi;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	/*
	 * public Collection<Role> getRoles() { return roles; }
	 * 
	 * public void setRoles(Collection<Role> roles) { this.roles = roles; }
	 */
}
