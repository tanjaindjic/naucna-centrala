package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.model.korisnici.Korisnik;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Kupovina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Korisnik korisnik;


    @ManyToOne
    private Casopis casopis;


    @ManyToOne
    private Rad rad;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Boolean pretplata;

    @NotNull
    private Float cena;

    public Kupovina(@NotNull Korisnik k,Casopis casopis, Rad rad, @NotNull Status status, @NotNull Boolean pretplata, @NotNull Float cena) {
        this.korisnik = k;
        this.casopis = casopis;
        this.rad = rad;
        this.status = status;
        this.pretplata = pretplata;
        this.cena = cena;
    }

    public Kupovina() {
    }

    public Long getId() {
        return id;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getPretplata() {
        return pretplata;
    }

    public void setPretplata(Boolean pretplata) {
        this.pretplata = pretplata;
    }

    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public Rad getRad() {
        return rad;
    }

    public void setRad(Rad rad) {
        this.rad = rad;
    }
}
