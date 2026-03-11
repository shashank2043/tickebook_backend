package org.team11.tickebook.adminservice.kafka;

import lombok.Data;

import java.util.UUID;

@Data
public class UserCreatedEvent {

    private UUID userid;
    private String email;

}