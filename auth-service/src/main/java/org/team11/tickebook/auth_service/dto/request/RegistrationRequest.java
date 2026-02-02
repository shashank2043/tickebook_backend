package org.team11.tickebook.auth_service.dto.request;

public class RegistrationRequest {
	private String email;
	private  String password;
	private String firstName;
	private String lastName;
	public RegistrationRequest(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	
}
