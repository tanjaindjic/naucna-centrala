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
    private Korisnik k;


    @ManyToOne
    private Casopis casopis;


    @ManyToOne
    private Rad rad;

    private Status status;

    private Boolean pretplata;

    private Float cena;

    public Kupovina(@NotNull Korisnik k,Casopis casopis, Rad rad, Status status, Boolean pretplata, Float cena) {
        this.k = k;
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

    public Korisnik getK() {
        return k;
    }

    public void setK(Korisnik k) {
        this.k = k;
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
