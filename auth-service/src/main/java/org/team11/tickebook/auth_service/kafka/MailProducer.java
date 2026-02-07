package org.team11.tickebook.auth_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.team11.tickebook.auth_service.client.Mail;

@Component
public class MailProducer {

    private final KafkaTemplate<String, Mail> kafkaTemplate;

    public MailProducer(KafkaTemplate<String, Mail> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRoleUpdateMail(Mail mail) {
        kafkaTemplate.send("mail-notification-topic", mail);
    }
}
