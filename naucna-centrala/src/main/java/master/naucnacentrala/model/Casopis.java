package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Casopis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotNull
    @Column(nullable = false, length=256)
    private String naziv;

	@NotNull
    @Column(nullable = false, length=256)
    private String issn;

	@NotNull
    @Column
    @ElementCollection(targetClass = NaucnaOblast.class)
    @JoinTable(name = "naucnaOblast", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Collection<NaucnaOblast> naucneOblasti;

    @Column
    @OneToMany
    private Collection<Rad> radovi;

	@NotNull
    @Column(nullable = false)
    private boolean isOpenAccess;

    @OneToOne
    private Korisnik glavniUrednik;

    @Column
    @OneToMany
    private Collection<Korisnik> uredniciNaucnihOblasti;

    @Column
    @OneToMany
    private Collection<Korisnik> recenzenti;
    

    public Casopis() {
    }
    

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Collection<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(Collection<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public Collection<Rad> getRadovi() {
        return radovi;
    }

    public void setRadovi(Collection<Rad> radovi) {
        this.radovi = radovi;
    }

    public boolean isOpenAccess() {
        return isOpenAccess;
    }

    public void setOpenAccess(boolean openAccess) {
        isOpenAccess = openAccess;
    }

    public Korisnik getGlavniUrednik() {
        return glavniUrednik;
    }

    public void setGlavniUrednik(Korisnik glavniUrednik) {
        this.glavniUrednik = glavniUrednik;
    }

    public Collection<Korisnik> getUredniciNaucnihOblasti() {
        return uredniciNaucnihOblasti;
    }

    public void setUredniciNaucnihOblasti(Collection<Korisnik> uredniciNaucnihOblasti) {
        this.uredniciNaucnihOblasti = uredniciNaucnihOblasti;
    }

    public Collection<Korisnik> getRecenzenti() {
        return recenzenti;
    }

    public void setRecenzenti(Collection<Korisnik> recenzenti) {
        this.recenzenti = recenzenti;
    }
}