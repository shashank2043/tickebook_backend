package org.team11.tickebook.adminservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.team11.tickebook.adminservice.service.AdminSeeder;

@Component
@RequiredArgsConstructor
public class UserCreatedEventConsumer {

    private final AdminSeeder adminSeeder;

    @KafkaListener(topics = "user-created", groupId = "admin-service")
    public void handleUserCreated(UserCreatedEvent event) {

        System.out.println("Received user event: " + event.getEmail());

        adminSeeder.createAdmin(event.getUserid());
    }
}