package master.naucnacentrala.model.dto;

public class RecenzijaDTO {
    Long radId;
    String komentar;
    String rezultat;
    String zaUrednika;

    public RecenzijaDTO(Long radId, String komentar, String rezultat, String zaUrednika) {
        this.radId = radId;
        this.komentar = komentar;
        this.rezultat = rezultat;
        this.zaUrednika = zaUrednika;
    }

    public RecenzijaDTO() {
    }

    public Long getRadId() {
        return radId;
    }

    public void setRadId(Long radId) {
        this.radId = radId;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getRezultat() {
        return rezultat;
    }

    public void setRezultat(String rezultat) {
        this.rezultat = rezultat;
    }

    public String getZaUrednika() {
        return zaUrednika;
    }

    public void setZaUrednika(String zaUrednika) {
        this.zaUrednika = zaUrednika;
    }

    @Override
    public String toString() {
        return "RecenzijaDTO{" +
                "radId=" + radId +
                ", komentar='" + komentar + '\'' +
                ", rezultat='" + rezultat + '\'' +
                ", zaUrednika='" + zaUrednika + '\'' +
                '}';
    }
}
