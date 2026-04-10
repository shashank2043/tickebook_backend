package org.team11.tickebook.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.team11.tickebook.auth_service.kafka.UserCreatedEvent;
import org.team11.tickebook.auth_service.kafka.UserEventProducer;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByRolesContains(Role.ADMIN)) {

            User admin = new User();
            admin.setEmail("admin@tickebook.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(List.of(Role.ADMIN,Role.THEATREOWNER,Role.USER));
            //added just now
            admin.setActive(true);
            userRepository.save(admin);

            System.out.println("Default Admin Created");

            UserCreatedEvent createdEvent = new UserCreatedEvent(admin.getId(), admin.getEmail());
            userEventProducer.publishUserCreated(createdEvent);
        }
    }
}
