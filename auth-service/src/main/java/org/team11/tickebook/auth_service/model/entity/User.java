package org.team11.tickebook.auth_service.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.auth_service.model.enums.Role;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
	private String password;
	private int tokenVersion;
	@ElementCollection(targetClass = Role.class)
	@Enumerated(EnumType.STRING)
	private List<Role> roles;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private boolean emailVerified;
	private boolean accountLocked;
	private int failedLoginAttempts;
	private boolean isActive;
	private boolean isDeleted;
	private LocalDateTime lastLoginAt;
	private LocalDateTime passwordChangedAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
