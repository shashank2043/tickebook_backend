package org.team11.tickebook.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team11.tickebook.auth_service.dto.request.RegistrationRequest;
import org.team11.tickebook.auth_service.dto.response.RegistrationResult;
import org.team11.tickebook.auth_service.model.Role;
import org.team11.tickebook.auth_service.model.User;
import org.team11.tickebook.auth_service.repository.UserRepository;
import org.team11.tickebook.auth_service.security.CustomUserDetailsService;
import org.team11.tickebook.auth_service.security.JwtUtil;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    public RegistrationResult register(RegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // DEFAULT SYSTEM VALUES
        user.setRole(Role.USER);
        user.setEmailVerified(false);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setTokenVersion(0);
        user.setActive(true);
        user.setDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(saved.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return new RegistrationResult(
                saved,
                token
        );
    }
}
