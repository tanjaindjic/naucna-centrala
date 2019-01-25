package master.naucnacentrala.model.dto;

public class RegisterDTO {
	
	private String username;
	private String pass;
	private String email;
	public RegisterDTO(String username, String pass, String email) {
		super();
		this.username = username;
		this.pass = pass;
		this.email = email;
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
	
	

}
