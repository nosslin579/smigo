package org.smigo.user;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailHandler {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Value("${mailSenderUsername}")
    private String mailSenderUsername;
    @Value("${notifierEmail}")
    private String notifierEmail;

    @PostConstruct
    public void sendStartupAdminNotification() {
        sendAdminNotification("Server", "Server started");
    }

    @PreDestroy
    public void sendShutdownAdminNotification() {
        sendAdminNotification("Server", "Server shutdown");
    }


    public void sendAdminNotification(String subject, Object text) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notifierEmail);
        simpleMailMessage.setFrom(mailSenderUsername);
        simpleMailMessage.setSubject("[SMIGO] " + subject);
        simpleMailMessage.setText(text.toString());
        mailSender.send(simpleMailMessage);
    }

    public void sendAdminNotificationHtml(String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(text, true);
        helper.setTo(notifierEmail);
        helper.setFrom(mailSenderUsername);
        helper.setSubject("[SMIGO] " + subject);
        mailSender.send(message);
    }

    public void sendClientMessage(String emailAddress, String subject, String text) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailAddress);
        simpleMailMessage.setFrom(mailSenderUsername);
        simpleMailMessage.setSubject("Smigo " + subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }
}