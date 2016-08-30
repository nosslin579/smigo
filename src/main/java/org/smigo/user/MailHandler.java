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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class MailHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserHandler userHandler;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Value("${mailSenderUsername}")
    private String mailSenderUsername;
    @Value("${notifierEmail}")
    private String notifierEmail;

    private Executor senderExecutor = Executors.newSingleThreadExecutor();

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
        senderExecutor.execute(() -> mailSender.send(simpleMailMessage));
    }

    public void sendAdminNotificationHtml(String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setText(text, true);
            helper.setTo(notifierEmail);
            helper.setFrom(mailSenderUsername);
            helper.setSubject("[SMIGO] " + subject);
            senderExecutor.execute(() -> mailSender.send(message));
        } catch (MessagingException e) {
            log.error("Send message failed:" + text, e);
        }
    }

    public void sendClientMessage(String emailAddress, String subject, String text) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailAddress);
        simpleMailMessage.setFrom(mailSenderUsername);
        simpleMailMessage.setSubject("Smigo " + subject);
        simpleMailMessage.setText(text);
        senderExecutor.execute(() -> mailSender.send(simpleMailMessage));
    }

    public void sendReviewRequest(String title, List<?> current, Object edit, AuthenticatedUser user) {
        User u = userHandler.getUser(user);
        String indentation = "  ";
        StringBuilder sb = new StringBuilder(title);
        sb.append(System.lineSeparator());
        sb.append("----------------------------------------------");
        sb.append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("Current value(s):").append(System.lineSeparator());
        sb.append(indentation).append(current.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator() + indentation)));
        sb.append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("Changing value:").append(System.lineSeparator());
        sb.append(indentation).append(edit);
        sb.append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("Requested by user:").append(System.lineSeparator());
        sb.append(indentation).append(u);
        sendAdminNotification("review request", sb.toString());
    }
}