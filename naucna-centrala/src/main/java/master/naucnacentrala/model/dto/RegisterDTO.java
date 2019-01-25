package master.naucnacentrala.model.dto;

public class RegisterDTO {
	
	private String username;
	private String pass;
	private String email;
	private String registrationProcessId;
	public RegisterDTO(String username, String pass, String email, String registrationProcessId) {
		super();
		this.username = username;
		this.pass = pass;
		this.email = email;
		this.registrationProcessId = registrationProcessId;
	}
	public RegisterDTO() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegistrationProcessId() {
		return registrationProcessId;
	}
	public void setRegistrationProcessId(String registrationProcessId) {
		this.registrationProcessId = registrationProcessId;
	}
	
	

}
