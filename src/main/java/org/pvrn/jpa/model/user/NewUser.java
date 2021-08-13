package org.pvrn.jpa.model.user;

public class NewUser {
	private String username;
	private String password;
	
	public NewUser() {}
	
	public NewUser(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
