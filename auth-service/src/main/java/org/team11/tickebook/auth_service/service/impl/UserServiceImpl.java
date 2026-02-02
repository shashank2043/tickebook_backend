package org.team11.tickebook.auth_service.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team11.tickebook.auth_service.exception.UserNotFoundException;
import org.team11.tickebook.auth_service.model.User;
import org.team11.tickebook.auth_service.repository.UserRepository;
import org.team11.tickebook.auth_service.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(UUID id) {
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.delete(user);
        return true;
    }
//
//	@Override
//	public RegistrationResponse createUser(RegistrationRequest dto) {
//
//		User user = new User();
//		//From dto
//		user.setId(UUID.randomUUID());
//		user.setEmail(dto.getEmail());
//		user.setFirstName(dto.getFirstName());
//		user.setLastName(dto.getLastName());
//		user.setPassword(passwordEncoder.encode(dto.getPassword()));
//		//defaults
//		user.setRole(Role.USER);
//		user.setEmailVerified(false);
//		user.setAccountLocked(false);
//		user.setFailedLoginAttempts(0);
//		user.setTokenVersion(0);
//		user.setActive(true);
//		user.setDeleted(false);
//
//		user.setCreatedAt(LocalDateTime.now());
//		user.setUpdatedAt(LocalDateTime.now());
//
//		User save = userRepository.save(user);
//		return new RegistrationResponse(
//				save.getId(),
//				save.getEmail(),
//				save.getFirstName(),
//				save.getLastName(),
//				save.getRole().name(),
//				save.isEmailVerified(),);
//	}

}
