package master.naucnacentrala.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdvancedQueryDTO {
    private String operator;
    private String zona;
    private String upit;
    @JsonProperty
    private Boolean isFraza;

    public AdvancedQueryDTO(String operator, String zona, String upit, Boolean isFraza) {
        this.operator = operator;
        this.zona = zona;
        this.upit = upit;
        this.isFraza = isFraza;
    }

    public AdvancedQueryDTO() {
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUpit() {
        return upit;
    }

    public void setUpit(String upit) {
        this.upit = upit;
    }

    public Boolean getFraza() {
        return isFraza;
    }

    public void setFraza(Boolean fraza) {
        isFraza = fraza;
    }

    @Override
    public String toString() {
        return "AdvancedQueryDTO{" +
                "operator='" + operator + '\'' +
                ", zona='" + zona + '\'' +
                ", upit='" + upit + '\'' +
                ", isFraza=" + isFraza +
                '}';
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
