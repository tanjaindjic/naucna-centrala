package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.Rezultat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Recenzija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Casopis casopis;

    @Column(nullable = false)
    private Rad rad;

    @Column(nullable = false)
    private Korisnik recenzent;

    @Column(nullable = false)
    private Date rok;

    private String komentar;

    private Rezultat rezultat;

    public Recenzija() {
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
