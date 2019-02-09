package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.Rezultat;
import master.naucnacentrala.model.korisnici.Korisnik;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Recenzija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotNull
    @OneToOne
    private Casopis casopis;

	@NotNull
    @OneToOne
    private Rad rad;

	@NotNull
    @OneToOne
    //moze biti urednik ili recenzent
    private Korisnik recenzent;

	@NotNull
    @Column(nullable = false)
    private Date rok;

    private String komentar;

    private Rezultat rezultat;

    public Recenzija() {
    }

    public Recenzija(@NotNull Casopis casopis, @NotNull Rad rad, @NotNull Korisnik recenzent, @NotNull Date rok, String komentar, Rezultat rezultat) {
        this.casopis = casopis;
        this.rad = rad;
        this.recenzent = recenzent;
        this.rok = rok;
        this.komentar = komentar;
        this.rezultat = rezultat;
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

    public Korisnik getRecenzent() {
        return recenzent;
    }

    public void setRecenzent(Korisnik recenzent) {
        this.recenzent = recenzent;
    }

    public Date getRok() {
        return rok;
    }

    public void setRok(Date rok) {
        this.rok = rok;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public Rezultat getRezultat() {
        return rezultat;
    }

    public void setRezultat(Rezultat rezultat) {
        this.rezultat = rezultat;
    }
}
