package master.naucnacentrala.model.dto;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.korisnici.Recenzent;

import java.util.List;

public class RevizijaDTO {
    Rad rad;
    List<Recenzija> recenzije;

    public RevizijaDTO(Rad rad, List<Recenzija> recenzije) {
        this.rad = rad;
        this.recenzije = recenzije;
    }

    public RevizijaDTO() {
    }

    public Rad getRad() {
        return rad;
    }

    public void setRad(Rad rad) {
        this.rad = rad;
    }

    public List<Recenzija> getRecenzije() {
        return recenzije;
    }

    public void setRecenzije(List<Recenzija> recenzije) {
        this.recenzije = recenzije;
    }
}
