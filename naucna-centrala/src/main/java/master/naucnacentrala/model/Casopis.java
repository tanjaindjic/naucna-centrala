package master.naucnacentrala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import org.apache.ibatis.annotations.Many;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.HashMap;

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
	@ElementCollection(targetClass = NaucnaOblast.class)
	@CollectionTable(name = "casopis_naucnaOblast", joinColumns = @JoinColumn(name = "casopis_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "naucnaOblast_id")
	private Collection<NaucnaOblast> naucneOblasti;

    @Column
    @OneToMany(cascade=CascadeType.ALL, mappedBy="casopis")
    @JsonBackReference
    private Collection<Rad> radovi;

	@NotNull
    @Column(nullable = false)
    private boolean isOpenAccess;

    @OneToOne
    private Urednik glavniUrednik;

    @Column
    @JsonBackReference
    private HashMap<String, Korisnik> uredniciNaucnihOblasti;

    @Column
    @ManyToMany
    @JsonBackReference
    private Collection<Korisnik> recenzenti;

    @Column
    private String urlSlike;

    @Column
    private Float cena;

    @Column
    private String identifikacioniKod;



    public Casopis() {
    }
    
    

    public Casopis(@NotNull String naziv, @NotNull String issn, @NotNull Collection<NaucnaOblast> naucneOblasti,
                   Collection<Rad> radovi, @NotNull boolean isOpenAccess, Urednik glavniUrednik,
                   HashMap<String, Korisnik> uredniciNaucnihOblasti, Collection<Korisnik> recenzenti, String urlSlike, Float cena, String identifikacioniKod) {
		super();
		this.naziv = naziv;
		this.issn = issn;
		this.naucneOblasti = naucneOblasti;
		this.radovi = radovi;
		this.isOpenAccess = isOpenAccess;
		this.glavniUrednik = glavniUrednik;
		this.uredniciNaucnihOblasti = uredniciNaucnihOblasti;
		this.recenzenti = recenzenti;
		this.urlSlike = urlSlike;
		this.cena = cena;
        this.identifikacioniKod = identifikacioniKod;
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

    public Urednik getGlavniUrednik() {
        return glavniUrednik;
    }

    public void setGlavniUrednik(Urednik glavniUrednik) {
        this.glavniUrednik = glavniUrednik;
    }

    public HashMap<String, Korisnik> getUredniciNaucnihOblasti() {
        return uredniciNaucnihOblasti;
    }

    public void setUredniciNaucnihOblasti(HashMap<String, Korisnik> uredniciNaucnihOblasti) {
        this.uredniciNaucnihOblasti = uredniciNaucnihOblasti;
    }

    public Collection<Korisnik> getRecenzenti() {
        return recenzenti;
    }

    public void setRecenzenti(Collection<Korisnik> recenzenti) {
        this.recenzenti = recenzenti;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }

    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public String getIdentifikacioniKod() {
        return identifikacioniKod;
    }

    public void setIdentifikacioniKod(String identifikacioniKod) {
        this.identifikacioniKod = identifikacioniKod;
    }
}
