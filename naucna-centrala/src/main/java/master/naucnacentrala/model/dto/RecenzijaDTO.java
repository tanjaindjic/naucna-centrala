package master.naucnacentrala.model.dto;

public class RecenzijaDTO {
    Long radId;
    String komentar;
    String rezultat;

    public RecenzijaDTO(Long radId, String komentar, String rezultat) {
        this.radId = radId;
        this.komentar = komentar;
        this.rezultat = rezultat;
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

    @Override
    public String toString() {
        return "RecenzijaDTO{" +
                "radId=" + radId +
                ", komentar='" + komentar + '\'' +
                ", rezultat='" + rezultat + '\'' +
                '}';
    }
}
