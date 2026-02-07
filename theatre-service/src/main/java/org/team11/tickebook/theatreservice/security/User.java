package org.team11.tickebook.theatreservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private UUID id;
	private String email;
	private String password;
	private int tokenVersion;
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
