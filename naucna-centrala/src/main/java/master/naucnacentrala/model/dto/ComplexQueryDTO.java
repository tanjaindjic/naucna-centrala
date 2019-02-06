package master.naucnacentrala.model.dto;

import java.util.List;

public class ComplexQueryDTO {
    private List<AdvancedQueryDTO> upiti;

    private List<String> naucneOblasti;

    public ComplexQueryDTO(List<AdvancedQueryDTO> upiti, List<String> naucneOblasti) {
        this.upiti = upiti;
        this.naucneOblasti = naucneOblasti;
    }

    public ComplexQueryDTO() {
    }

    @Override
    public String toString() {
        return "ComplexQueryDTO{" +
                "upiti=" + upiti +
                ", naucneOblasti=" + naucneOblasti +
                '}';
    }

    public List<AdvancedQueryDTO> getUpiti() {
        return upiti;
    }

    public void setUpiti(List<AdvancedQueryDTO> upiti) {
        this.upiti = upiti;
    }

    public List<String> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<String> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }
}
