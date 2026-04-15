package org.team11.tickebook.auth_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.team11.tickebook.auth_service.kafka.UserCreatedEvent;
import org.team11.tickebook.auth_service.kafka.UserEventProducer;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;
    @Value("${default.admin.email}")
    private String defaultEmail;
    @Value("${default.admin.password}")
    private String defaultPassword;
    @Override
    public void run(String... args) {
        if (!userRepository.existsByRolesContains(Role.ADMIN)) {

            User admin = new User();
            admin.setEmail(defaultEmail);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setRoles(List.of(Role.ADMIN,Role.THEATREOWNER,Role.USER));
            //added just now
            admin.setActive(true);
            userRepository.save(admin);

            log.info("Default Admin Created");

            UserCreatedEvent createdEvent = new UserCreatedEvent(admin.getId(), admin.getEmail());
            userEventProducer.publishUserCreated(createdEvent);
        }
    }
}
