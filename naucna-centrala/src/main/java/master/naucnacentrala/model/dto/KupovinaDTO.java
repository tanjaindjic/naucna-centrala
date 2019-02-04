package master.naucnacentrala.model.dto;

public class KupovinaDTO {
    private Long casopisId;
    private Long radId;
    private String username;
    private Boolean pretplata;
    private Float cena;

    public KupovinaDTO(Long casopisId, Long radId, String username, Boolean pretplata, Float cena) {
        this.casopisId = casopisId;
        this.radId = radId;
        this.username = username;
        this.pretplata = pretplata;
        this.cena = cena;
    }

    public KupovinaDTO() {
    }

    @Override
    public String toString() {
        return "KupovinaDTO{" +
                "casopisId=" + casopisId +
                ", radId=" + radId +
                ", username='" + username + '\'' +
                ", pretplata=" + pretplata +
                ", cena=" + cena +
                '}';
    }

    public Long getCasopisId() {
        return casopisId;
    }

    public void setCasopisId(Long casopisId) {
        this.casopisId = casopisId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getPretplata() {
        return pretplata;
    }

    public void setPretplata(Boolean pretplata) {
        this.pretplata = pretplata;
    }

    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public Long getRadId() {
        return radId;
    }

    public void setRadId(Long radId) {
        this.radId = radId;
    }
}
