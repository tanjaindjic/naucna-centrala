package master.naucnacentrala.model.dto;

import master.naucnacentrala.model.elastic.RadIndexUnit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.List;

public class BasicQueryResponseDTO {

    RadIndexUnit radIndexUnit;
    String highlights;
    public BasicQueryResponseDTO(RadIndexUnit radIndexUnit, String highlights) {
        this.radIndexUnit = radIndexUnit;
        this.highlights = highlights;
    }

    @Override
    public String toString() {
        return "BasicQueryResponseDTO{" +
                "radIndexUnit=" + radIndexUnit +
                ", highlights=" + highlights +
                '}';
    }

    public BasicQueryResponseDTO() {
    }

    public RadIndexUnit getRadIndexUnit() {
        return radIndexUnit;
    }

    public void setRadIndexUnit(RadIndexUnit radIndexUnit) {
        this.radIndexUnit = radIndexUnit;
    }

    public String getHighlight() {
        return highlights;
    }

    public void setHighlight(String highlight) {
        this.highlights = highlight;
    }
}
