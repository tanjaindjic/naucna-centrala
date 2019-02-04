package master.naucnacentrala.model.dto;

import java.util.List;

public class RegisterDTO {
	
	String taskId;
	String processInstanceId;
	List<FieldIdValueDTO> formFields;
	
	
	public RegisterDTO(String taskId, String processInstanceId, List<FieldIdValueDTO> formFields) {
		super();
		this.taskId = taskId;
		this.processInstanceId = processInstanceId;
		this.formFields = formFields;
	}
	public RegisterDTO() {
		super();
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public List<FieldIdValueDTO> getFormFields() {
		return formFields;
	}
	public void setFormFields(List<FieldIdValueDTO> formFields) {
		this.formFields = formFields;
	}

	@Override
	public String toString() {
		return "RegisterDTO{" +
				"taskId='" + taskId + '\'' +
				", processInstanceId='" + processInstanceId + '\'' +
				", formFields=" + formFields +
				'}';
	}
}
