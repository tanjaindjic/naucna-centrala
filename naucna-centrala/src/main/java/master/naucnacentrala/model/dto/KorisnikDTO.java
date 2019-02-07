package master.naucnacentrala.model.dto;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.korisnici.Korisnik;

import java.util.Collection;

public class KorisnikDTO {

    private Long id;

    private String ime;

    private String prezime;

    private String grad;

    private String drzava;

    private String email;

    private Collection<Pretplata> pretplaceniCasopisi;

    private Collection<Rad> placeniRadovi;

    private Collection<Casopis> placeniCasopisi;

    public KorisnikDTO() {
    }

    public KorisnikDTO(Korisnik k) {
        this.id = k.getId();
        this.ime = k.getIme();
        this.prezime = k.getPrezime();
        this.grad = k.getGrad();
        this.drzava = k.getDrzava();
        this.email = k.getEmail();
        this.pretplaceniCasopisi = k.getPretplaceniCasopisi();
        this.placeniRadovi = k.getPlaceniRadovi();
        this.placeniCasopisi = k.getPlaceniCasopisi();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Collection<Pretplata> getPretplaceniCasopisi() {
        return pretplaceniCasopisi;
    }

    public void setPretplaceniCasopisi(Collection<Pretplata> pretplaceniCasopisi) {
        this.pretplaceniCasopisi = pretplaceniCasopisi;
    }

    public Collection<Rad> getPlaceniRadovi() {
        return placeniRadovi;
    }

    public void setPlaceniRadovi(Collection<Rad> placeniRadovi) {
        this.placeniRadovi = placeniRadovi;
    }

    public Collection<Casopis> getPlaceniCasopisi() {
        return placeniCasopisi;
    }

    public void setPlaceniCasopisi(Collection<Casopis> placeniCasopisi) {
        this.placeniCasopisi = placeniCasopisi;
    }
}
