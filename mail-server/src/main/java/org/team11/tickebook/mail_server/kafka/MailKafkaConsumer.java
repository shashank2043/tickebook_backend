package org.team11.tickebook.mail_server.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.team11.tickebook.mail_server.dto.Mail;
import org.team11.tickebook.mail_server.service.MailService;

@Component
public class MailKafkaConsumer {

    public MailKafkaConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    private final MailService mailService;

    @KafkaListener(
            topics = "mail-notification-topic",
            groupId = "mail-group"
    )
    public void consumeNotificationMail(Mail mail) {
        mailService.sendMail(mail);
        System.out.println("Notification mail sent: " + mail.getTomail());
    }
}
