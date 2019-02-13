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

    private String komentar;

    @Column
    @Enumerated(EnumType.STRING)
    private Rezultat rezultat;

    public Recenzija() {
    }

    public Recenzija(@NotNull Casopis casopis, @NotNull Rad rad, @NotNull Korisnik recenzent,String komentar, Rezultat rezultat) {
        this.casopis = casopis;
        this.rad = rad;
        this.recenzent = recenzent;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
