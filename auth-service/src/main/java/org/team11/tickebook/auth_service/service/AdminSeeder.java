package org.team11.tickebook.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.team11.tickebook.auth_service.model.Role;
import org.team11.tickebook.auth_service.model.User;
import org.team11.tickebook.auth_service.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByRolesContains(Role.ADMIN)) {

            User admin = new User();
            admin.setEmail("admin@tickebook.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(List.of(Role.ADMIN));

            userRepository.save(admin);

            System.out.println("Default Admin Created");
        }
    }
}
