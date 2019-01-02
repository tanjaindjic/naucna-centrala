package master.naucnacentrala.model.korisnici;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.NaucnaOblast;

@Entity
public class Recenzent extends Korisnik {


	@NotNull
	@Column
	private String titula;


	@NotNull
	@Column
	@ElementCollection(targetClass = NaucnaOblast.class)
	@JoinTable(name = "naucnaOblast", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
	private Collection<NaucnaOblast> naucneOblasti;

	@Column
	@ManyToMany
	// recenzenti mogu u vise casopisa
	private Collection<Casopis> pripada;

	@Column
	@OneToMany
	private Collection<Rad> recenzira;
	

	public Recenzent() {
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

	public Collection<Casopis> getPripada() {
		return pripada;
	}

	public void setPripada(Collection<Casopis> pripada) {
		this.pripada = pripada;
	}

	public Collection<Rad> getRecenzira() {
		return recenzira;
	}

	public void setRecenzira(Collection<Rad> recenzira) {
		this.recenzira = recenzira;
	}
	
	
	
	
	

}