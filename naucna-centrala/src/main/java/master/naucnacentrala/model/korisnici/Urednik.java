package master.naucnacentrala.model.korisnici;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.enums.NaucnaOblast;/*
import org.elasticsearch.common.geo.GeoPoint;*/

@Entity
public class Urednik extends Korisnik {

	@NotNull
	@Column
	private String titula;

	@NotNull
	@ElementCollection(targetClass = NaucnaOblast.class)
	@CollectionTable(name = "urednik_naucnaOblast", joinColumns = @JoinColumn(name = "urednik_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "naucnaOblast_id")
	private Collection<NaucnaOblast> naucneOblasti;

	@ManyToOne(optional = true)
	@JsonBackReference
	private Casopis uredjuje;

	@Column
	@OneToMany
	private Collection<Rad> recenzira;

	public Urednik() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Urednik(String string, String string2, String string3, String string4, String string5, String string6,
				   String string7, Double lat, Double lon, ArrayList lista, ArrayList arrayList, ArrayList arrayList2, String titula,
				   Collection<NaucnaOblast> naucneOblasti, Casopis uredjuje, Collection<Rad> recenzira) {
		super(string, string2, string3, string4, string5, string6, string7, lat, lon, lista, arrayList, arrayList2);
		this.titula = titula;
		this.naucneOblasti = naucneOblasti;
		this.uredjuje = uredjuje;
		this.recenzira = recenzira;
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
