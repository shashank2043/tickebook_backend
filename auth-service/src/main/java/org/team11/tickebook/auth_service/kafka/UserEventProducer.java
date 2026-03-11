package org.team11.tickebook.auth_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void publishUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user-created",event.getUserid().toString(), event)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        System.out.println("Failed to send event: " + ex.getMessage());
                    } else {
                        System.out.println("Event sent to topic user-created");
                    }

                });
    }
}