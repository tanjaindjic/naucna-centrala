package master.naucnacentrala.model.dto;

import java.io.Serializable;

public class FieldIdValueDTO implements Serializable{
	String fieldId;
	String fieldValue;
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	public FieldIdValueDTO(){}
	public FieldIdValueDTO(String fieldId, String fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}
	
	

}
