package org.team11.tickebook.mail_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.team11.tickebook.mail_server.dto.Mail;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("shashankpulluri1@gmail.com");
        message.setTo(mail.getTomail());
        message.setSubject(mail.getSub());
        message.setText(mail.getContent());
        javaMailSender.send(message);
    }
}