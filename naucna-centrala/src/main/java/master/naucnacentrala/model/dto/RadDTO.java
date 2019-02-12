package master.naucnacentrala.model.dto;

public class RadDTO {
    Long id;
    String naslov;
    String komentari;

    public RadDTO(Long id, String naslov, String komentari) {
        this.id = id;
        this.naslov = naslov;
        this.komentari = komentari;
    }

    public RadDTO() {
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

    public String getKomentari() {
        return komentari;
    }

    public void setKomentari(String komentari) {
        this.komentari = komentari;
    }
}
