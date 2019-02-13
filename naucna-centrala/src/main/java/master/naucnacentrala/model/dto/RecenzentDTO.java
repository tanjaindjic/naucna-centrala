package master.naucnacentrala.model.dto;

import master.naucnacentrala.model.korisnici.Korisnik;

public class RecenzentDTO {

    Long id;
    String username;
    String ime;
    String prezime;
    String grad;
    String drzava;

    public RecenzentDTO(Korisnik rec) {
        this.id = rec.getId();
        this.username = rec.getUsername();
        this.ime = rec.getIme();
        this.prezime = rec.getPrezime();
        this.grad = rec.getGrad();
        this.drzava = rec.getDrzava();
    }

    public RecenzentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
