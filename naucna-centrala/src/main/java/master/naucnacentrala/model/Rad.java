package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private String koautori;

    @Column
    private Float cena;

    @Column
    private String urlSlike;

    @NotNull
    @Column(nullable = false)
    private String kljucniPojmovi;

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
    @JoinColumn(name="casopis_id")
    private Casopis casopis;

    @Column
    private String identifikacioniKod;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusRada statusRada;


	public Rad() {
    }

    public Rad(String doi, @NotNull String naslov, @NotNull Korisnik autor, String koautori, Float cena, String urlSlike, @NotNull String kljucniPojmovi, @NotNull String apstrakt, @NotNull NaucnaOblast naucnaOblast, String adresaNacrta, String adresaKonacnogRada, Casopis casopis, String identifikacioniKod, StatusRada statusRada) {
        this.doi = doi;
        this.naslov = naslov;
        this.autor = autor;
        this.koautori = koautori;
        this.cena = cena;
        this.urlSlike = urlSlike;
        this.kljucniPojmovi = kljucniPojmovi;
        this.apstrakt = apstrakt;
        this.naucnaOblast = naucnaOblast;
        this.adresaNacrta = adresaNacrta;
        this.adresaKonacnogRada = adresaKonacnogRada;
        this.casopis = casopis;
        this.identifikacioniKod = identifikacioniKod;
        this.statusRada = statusRada;
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

	public String getKoautori() {
		return koautori;
	}

	public void setKoautori(String koautori) {
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

    public String getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi( String kljucniPojmovi) {
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

	public String getListaKoautora() {
		return koautori;
	}


    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }

    public String getIdentifikacioniKod() {
        return identifikacioniKod;
    }

    public void setIdentifikacioniKod(String identifikacioniKod) {
        this.identifikacioniKod = identifikacioniKod;
    }

    public StatusRada getStatusRada() {
        return statusRada;
    }

    public void setStatusRada(StatusRada statusRada) {
        this.statusRada = statusRada;
    }
}
