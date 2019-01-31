package master.naucnacentrala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Koautor;
import master.naucnacentrala.model.korisnici.Korisnik;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Rad {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //dobija se nakon objave
    private String doi;

	@NotNull
    @Column(nullable = false, length=256)
    private String naslov;
	
	@NotNull
    @OneToOne
    private Korisnik autor;

    @Column
    @OneToMany
    private Collection<Koautor> koautori;

    @Column
    private Long cena;

    @Column
    private String urlSlike;

    @NotNull
    @Column(nullable = false)
    private ArrayList<String> kljucniPojmovi;

	@NotNull
    @Column(nullable = false)
    private String apstrakt;

	@NotNull
    @Column(nullable = false)	
	@Enumerated(EnumType.STRING)
    private NaucnaOblast naucnaOblast;

    @Column
    private String adresaNacrta;

    @Column
    private String adresaKonacnogRada;
    
    @ManyToOne
    @JoinColumn(name="casopis_id", nullable=false)
    private Casopis casopis;


	public Rad() {
    }

    public Rad(String doi, @NotNull String naslov, @NotNull Korisnik autor, Collection<Koautor> koautori,
			@NotNull ArrayList<String> kljucniPojmovi, @NotNull String apstrakt, @NotNull NaucnaOblast naucnaOblast,
			String adresaNacrta, String adresaKonacnogRada, Casopis casopis) {
		super();
		this.doi = doi;
		this.naslov = naslov;
		this.autor = autor;
		this.koautori = koautori;
		this.kljucniPojmovi = kljucniPojmovi;
		this.apstrakt = apstrakt;
		this.naucnaOblast = naucnaOblast;
		this.adresaNacrta = adresaNacrta;
		this.adresaKonacnogRada = adresaKonacnogRada;
		this.casopis = casopis;
	}



    public Casopis getCasopis() {
		return casopis;
	}

	public void setCasopis(Casopis casopis) {
		this.casopis = casopis;
	}
	
	public Korisnik getAutor() {
		return autor;
	}

	public void setAutor(Korisnik autor) {
		this.autor = autor;
	}

	public Collection<Koautor> getKoautori() {
		return koautori;
	}

	public void setKoautori(Collection<Koautor> koautori) {
		this.koautori = koautori;
	}


	public Long getId() {
		return id;
	}

	public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public ArrayList<String> getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi( ArrayList<String> kljucniPojmovi) {
        this.kljucniPojmovi = kljucniPojmovi;
    }

    public String getApstrakt() {
        return apstrakt;
    }

    public void setApstrakt(String apstrakt) {
        this.apstrakt = apstrakt;
    }

    public NaucnaOblast getNaucnaOblast() {
        return naucnaOblast;
    }

    public void setNaucnaOblast(NaucnaOblast naucnaOblast) {
        this.naucnaOblast = naucnaOblast;
    }

    public String getAdresaNacrta() {
        return adresaNacrta;
    }

    public void setAdresaNacrta(String adresaNacrta) {
        this.adresaNacrta = adresaNacrta;
    }

    public String getAdresaKonacnogRada() {
        return adresaKonacnogRada;
    }

    public void setAdresaKonacnogRada(String adresaKonacnogRada) {
        this.adresaKonacnogRada = adresaKonacnogRada;
    }

	public ArrayList<String> getListaKoautora() {
		if(koautori == null)
			return new ArrayList<String>();
		ArrayList<String> lista = new ArrayList<>();
		for(Koautor k : koautori)
			lista.add(k.getIme() + " " + k.getPrezime());
		return lista;
	}

    public Long getCena() {
        return cena;
    }

    public void setCena(Long cena) {
        this.cena = cena;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }
}
