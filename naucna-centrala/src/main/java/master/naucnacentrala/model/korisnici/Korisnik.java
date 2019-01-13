package master.naucnacentrala.model.korisnici;
import master.naucnacentrala.model.Clanarina;
import master.naucnacentrala.model.Rad;
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

	@Column
	@OneToMany
	private Collection<Clanarina> placeniCasopisi;

	@Column
	@OneToMany
	private Collection<Rad> placeniRadovi;

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
			@NotNull String grad, @NotNull String drzava, @NotNull String email, Collection<Clanarina> placeniCasopisi,
			Collection<Rad> placeniRadovi) {
		super();
		this.username = username;
		this.pass = pass;
		this.ime = ime;
		this.prezime = prezime;
		this.grad = grad;
		this.drzava = drzava;
		this.email = email;
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

	public Collection<Clanarina> getPlaceniCasopisi() {
		return placeniCasopisi;
	}

	public void setPlaceniCasopisi(Collection<Clanarina> placeniCasopisi) {
		this.placeniCasopisi = placeniCasopisi;
	}

	public Collection<Rad> getPlaceniRadovi() {
		return placeniRadovi;
	}

	public void setPlaceniRadovi(Collection<Rad> placeniRadovi) {
		this.placeniRadovi = placeniRadovi;
	}

	/*
	 * public Collection<Role> getRoles() { return roles; }
	 * 
	 * public void setRoles(Collection<Role> roles) { this.roles = roles; }
	 */
}
