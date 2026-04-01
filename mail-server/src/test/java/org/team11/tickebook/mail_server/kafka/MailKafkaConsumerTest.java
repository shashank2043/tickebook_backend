package org.team11.tickebook.mail_server.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.mail_server.dto.Mail;
import org.team11.tickebook.mail_server.service.MailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailKafkaConsumerTest {

    @Mock
    private MailService mailService;

    @InjectMocks
    private MailKafkaConsumer consumer;

    // ================= CONSUME MAIL =================

    @Test
    void consumeNotificationMail_shouldCallMailService_whenMessageReceived() {

        Mail mail = new Mail();
        mail.setTomail("test@mail.com");

        consumer.consumeNotificationMail(mail);

        verify(mailService).sendMail(mail);
    }

    @Test
    void consumeNotificationMail_shouldPropagateException_whenServiceFails() {

        Mail mail = new Mail();

        doThrow(new RuntimeException("Mail failed"))
                .when(mailService).sendMail(mail);

        assertThrows(RuntimeException.class,
                () -> consumer.consumeNotificationMail(mail));

        verify(mailService).sendMail(mail);
    }
}