package org.team11.tickebook.mail_server.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.mail_server.dto.Mail;
import org.team11.tickebook.mail_server.service.MailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailControllerTest {

    @Mock
    private MailService mailService;

    @InjectMocks
    private MailController controller;

    // ================= SEND MAIL =================

    @Test
    void sendMail_shouldCallService_andReturnSuccess() {

        Mail mail = new Mail();
        mail.setTomail("test@mail.com");

        ResponseEntity<?> response = controller.sendMail(mail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sent successfully", response.getBody());

        verify(mailService).sendMail(mail);
    }

    @Test
    void sendMail_shouldPropagateException_whenServiceFails() {

        Mail mail = new Mail();

        doThrow(new RuntimeException("Mail failed"))
                .when(mailService).sendMail(mail);

        assertThrows(RuntimeException.class,
                () -> controller.sendMail(mail));

        verify(mailService).sendMail(mail);
    }
}