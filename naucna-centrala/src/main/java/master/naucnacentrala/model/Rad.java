package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Koautor;
import master.naucnacentrala.model.korisnici.Korisnik;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

	@NotNull
    @Column(nullable = false)
    private String[] kljucniPojmovi;

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

    public Rad() {
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

    public  String[] getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi( String[] kljucniPojmovi) {
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
}
