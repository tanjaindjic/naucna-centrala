package master.naucnacentrala.model.dto;

public class HighlightDTO {
    String fieldName;
    String highlight;

    public HighlightDTO(String fieldName, String highlight) {
        this.fieldName = fieldName;
        this.highlight = highlight;
    }

    public HighlightDTO() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }
}
