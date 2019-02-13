package master.naucnacentrala.model.dto;

import java.util.List;

public class RadDTO {
    Long id;
    String naslov;
    List<String> komentari;
    String odgovor;
    String autor;
    String naucnaOblast;

    public RadDTO(Long id, String naslov, List<String> komentari, String autor, String naucnaOblast, String odgovor) {
        this.id = id;
        this.naslov = naslov;
        this.komentari = komentari;
        this.autor = autor;
        this.naucnaOblast = naucnaOblast;
        this.odgovor = odgovor;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNaucnaOblast() {
        return naucnaOblast;
    }

    public void setNaucnaOblast(String naucnaOblast) {
        this.naucnaOblast = naucnaOblast;
    }

    public RadDTO() {
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public List<String> getKomentari() {
        return komentari;
    }

    public void setKomentari(List<String> komentari) {
        this.komentari = komentari;
    }
}
