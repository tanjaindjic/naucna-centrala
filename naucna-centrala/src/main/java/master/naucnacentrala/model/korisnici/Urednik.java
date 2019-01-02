package master.naucnacentrala.model.korisnici;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.NaucnaOblast;

@Entity
public class Urednik extends Korisnik {


	@NotNull
	@Column
	private String titula;

	@NotNull
	@Column
	@ElementCollection(targetClass = NaucnaOblast.class)
	@JoinTable(name = "naucnaOblast", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
	private Collection<NaucnaOblast> naucneOblasti;

	@OneToOne
	private Casopis uredjuje;

	@Column
	@OneToMany
	private Collection<Rad> recenzira;

	public Urednik() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitula() {
		return titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
	}

	public Collection<NaucnaOblast> getNaucneOblasti() {
		return naucneOblasti;
	}

	public void setNaucneOblasti(Collection<NaucnaOblast> naucneOblasti) {
		this.naucneOblasti = naucneOblasti;
	}

	public Casopis getUredjuje() {
		return uredjuje;
	}

	public void setUredjuje(Casopis uredjuje) {
		this.uredjuje = uredjuje;
	}

	public Collection<Rad> getRecenzira() {
		return recenzira;
	}

	public void setRecenzira(Collection<Rad> recenzira) {
		this.recenzira = recenzira;
	}
	
	
}
