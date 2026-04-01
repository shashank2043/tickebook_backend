package org.team11.tickebook.mail_server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.team11.tickebook.mail_server.dto.Mail;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailService mailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    // ================= SEND MAIL =================

    @Test
    void sendMail_shouldSendEmail_withCorrectDetails() {

        Mail mail = new Mail();
        mail.setTomail("test@mail.com");
        mail.setSub("Test Subject");
        mail.setContent("Test Content");

        mailService.sendMail(mail);

        // Capture email sent
        verify(javaMailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("shashankpulluri1@gmail.com", sentMessage.getFrom());
        assertEquals("test@mail.com", sentMessage.getTo()[0]);
        assertEquals("Test Subject", sentMessage.getSubject());
        assertEquals("Test Content", sentMessage.getText());
    }

    @Test
    void sendMail_shouldPropagateException_whenMailSenderFails() {

        Mail mail = new Mail();
        mail.setTomail("test@mail.com");

        doThrow(new RuntimeException("Mail failed"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class,
                () -> mailService.sendMail(mail));

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}