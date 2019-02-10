package master.naucnacentrala.model.dto;

import java.util.List;

public class RecenzentQueryDTO {
    Long idRada;
    Double udaljenost;
    boolean moreLikeThis;
    List<String> naucneOblasti;


    public RecenzentQueryDTO(Long idRada, Double udaljenost, boolean moreLikeThis, List<String> naucneOblasti) {
        this.idRada = idRada;
        this.udaljenost = udaljenost;
        this.moreLikeThis = moreLikeThis;
        this.naucneOblasti = naucneOblasti;
    }

    public RecenzentQueryDTO() {
    }

    public Long getIdRada() {
        return idRada;
    }

    public void setIdRada(Long idRada) {
        this.idRada = idRada;
    }

    public Double getUdaljenost() {
        return udaljenost;
    }

    public void setUdaljenost(Double udaljenost) {
        this.udaljenost = udaljenost;
    }

    public boolean isMoreLikeThis() {
        return moreLikeThis;
    }

    public void setMoreLikeThis(boolean moreLikeThis) {
        this.moreLikeThis = moreLikeThis;
    }

    public List<String> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<String> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    @Override
    public String toString() {
        return "RecenzentQueryDTO{" +
                "idRada=" + idRada +
                ", udaljenost=" + udaljenost +
                ", moreLikeThis=" + moreLikeThis +
                ", naucneOblasti=" + naucneOblasti +
                '}';
    }
}
