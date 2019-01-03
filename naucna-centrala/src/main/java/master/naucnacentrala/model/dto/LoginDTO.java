package master.naucnacentrala.model.dto;

public class LoginDTO {
	
	private String username;
	private String pass;
	public LoginDTO(String username, String pass) {
		super();
		this.username = username;
		this.pass = pass;
	}
	public LoginDTO() {
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
	public String toJson() {
		// TODO Auto-generated method stub
		return "{ " + 
				"	\"username\" : \"" + username + "\"," + 
				"	\"password\": \"" + pass + "\"" + 
				"}";
	}
	
	
	
	

}
