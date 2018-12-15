package master.naucnacentrala.model;

import master.naucnacentrala.model.enums.NaucnaOblast;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String pass;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(nullable = false)
    private String grad;

    @Column(nullable = false)
    private String drzava;

    @Column(nullable = false)
    private String email;

    @Column
    private String titula;

    @Column
    @Enumerated(EnumType.STRING)
    @OneToMany
    private Collection<NaucnaOblast> naucneOblasti;

    @Column
    @ManyToMany
    private Collection<Casopis> recenzira;

    @Column
    @OneToMany
    private Collection<Clanarina> placeniCasopisi;

    @Column
    @OneToMany
    private Collection<Rad> placeniRadovi;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public Korisnik() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getTitula() {
        return titula;
    }

    public void setTitula(String titula) {
        this.titula = titula;
    }

    public Collection<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(Collection<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public Collection<Casopis> getRecenzira() {
        return recenzira;
    }

    public void setRecenzira(Collection<Casopis> recenzira) {
        this.recenzira = recenzira;
    }

    public Collection<Clanarina> getPlaceniCasopisi() {
        return placeniCasopisi;
    }

    public void setPlaceniCasopisi(Collection<Clanarina> placeniCasopisi) {
        this.placeniCasopisi = placeniCasopisi;
    }

    public Collection<Rad> getPlaceniRadovi() {
        return placeniRadovi;
    }

    public void setPlaceniRadovi(Collection<Rad> placeniRadovi) {
        this.placeniRadovi = placeniRadovi;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
