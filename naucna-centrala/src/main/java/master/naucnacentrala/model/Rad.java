package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.NaucnaOblast;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Rad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doi;

    @Column(nullable = false, length=256)
    private String naslov;

    //TODO smisliti kako ovo resiti, moze biti i samo string
    @Column(nullable = false)
    @OneToMany
    private Collection<Korisnik> autori;

    @Column(nullable = false)
    private String kljucniPojmovi;

    @Column(nullable = false)
    private String apstrakt;

    @Column(nullable = false)
    private NaucnaOblast naucnaOblast;

    @Column(nullable = false)
    private String adresaNacrta;

    @Column
    private String adresaKonacnogRada;

    public Rad() {
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

    public Collection<Korisnik> getAutori() {
        return autori;
    }

    public void setAutori(Collection<Korisnik> autori) {
        this.autori = autori;
    }

    public String getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi(String kljucniPojmovi) {
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
