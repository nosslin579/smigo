package org.smigo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailHandler {

    @Autowired
    private MailSender mailSender;
    @Value("${notifierEmail}")
    private String notifierEmail;


    public void sendAdminNotification(String subject, String text) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notifierEmail);
        simpleMailMessage.setSubject("[SMIGO]" + subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }

    public void sendClientMessage(String emailAddress, String subject, String text) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailAddress);
        simpleMailMessage.setSubject("Smigo " + subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }
}