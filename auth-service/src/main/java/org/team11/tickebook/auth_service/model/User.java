package org.team11.tickebook.auth_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
//	User
//	id : UUID
//	email : String
//	password : String
//	tokenVersion : int
//	role : Role
//	firstName : String
//	lastName : String
//	phoneNumber : String
//	emailVerified : boolean
//	accountLocked : boolean
//	failedLoginAttempts : int
//	isActive : boolean
//	isDeleted : boolean
//	lastLoginAt : LocalDateTime
//	passwordChangedAt : LocalDateTime
//	createdAt : LocalDateTime
//	updatedAt : LocalDateTime
	@Id
	@GeneratedValue
	private UUID id;
	private String email;
	private  String password;
	private int tokenVersion;
	@Enumerated(EnumType.STRING)
	private Role role=Role.USER;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private  boolean emailVerified;
	private boolean accountLocked;
	private int  failedLoginAttempts;
	private boolean  isActive;
	private  boolean isDeleted;
	private LocalDateTime  lastLoginAt;
	private LocalDateTime  passwordChangedAt;
	private LocalDateTime createdAt;
	private LocalDateTime  updatedAt;
	public User() {
	}
	public User(UUID id, String email, String password, int tokenVersion, Role role, String firstName, String lastName,
			String phoneNumber, boolean emailVerified, boolean accountLocked, int failedLoginAttempts, boolean isActive,
			boolean isDeleted, LocalDateTime lastLoginAt, LocalDateTime passwordChangedAt, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.tokenVersion = tokenVersion;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailVerified = emailVerified;
		this.accountLocked = accountLocked;
		this.failedLoginAttempts = failedLoginAttempts;
		this.isActive = isActive;
		this.isDeleted = isDeleted;
		this.lastLoginAt = lastLoginAt;
		this.passwordChangedAt = passwordChangedAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getTokenVersion() {
		return tokenVersion;
	}
	public void setTokenVersion(int tokenVersion) {
		this.tokenVersion = tokenVersion;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public boolean isAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}
	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}
	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}
	public LocalDateTime getPasswordChangedAt() {
		return passwordChangedAt;
	}
	public void setPasswordChangedAt(LocalDateTime passwordChangedAt) {
		this.passwordChangedAt = passwordChangedAt;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	


}
