package master.naucnacentrala.model.dto;

import master.naucnacentrala.model.Casopis;

public class CasopisDTO {
    Casopis casopis;
    boolean kupljen;
    boolean uToku;

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public boolean isKupljen() {
        return kupljen;
    }

    public void setKupljen(boolean kupljen) {
        this.kupljen = kupljen;
    }

    public CasopisDTO() {
    }

    public boolean isuToku() {
        return uToku;
    }

    public void setuToku(boolean uToku) {
        this.uToku = uToku;
    }

    public CasopisDTO(Casopis c, boolean kupljen, boolean uToku) {
        this.casopis = c;
        this.kupljen = kupljen;
        this.uToku = uToku;
    }
}
