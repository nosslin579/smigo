package org.smigo.config;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserAdaptiveMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
@Profile(EnvironmentProfile.DEVELOPMENT)
@PropertySource({"classpath:dev.properties"})
public class DevelopmentConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final File MAIL_FILE = new File("/tmp/mail.html");
    private static final File MAIL_FILE_TXT = new File("/tmp/mail.txt");


    @Bean
    public MessageSource messageSource() {
        log.debug("getMessageSource");
        return new UserAdaptiveMessageSource(1);
    }

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        return new JavaMailSenderImpl() {
            @Override
            public String getDefaultEncoding() {
                return "UTF-8";
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                try {
                    Thread.sleep(2000);
                    String text = simpleMessage.getText();
                    String subject = simpleMessage.getSubject();
                    FileUtils.writeStringToFile(MAIL_FILE, text, Charset.defaultCharset());
                    FileUtils.writeStringToFile(MAIL_FILE_TXT, subject + System.lineSeparator() + text, Charset.defaultCharset());
                } catch (Exception e) {
                    throw new MailSendException("Error sending email to " + simpleMessage.getFrom(), e);
                }
            }

            @Override
            public void send(MimeMessage mimeMessage) throws MailException {
                try {
                    final String s = IOUtils.toString(mimeMessage.getInputStream());
                    FileUtils.writeStringToFile(MAIL_FILE, s, Charset.defaultCharset());
                } catch (IOException | MessagingException e) {
                    throw new MailSendException("Error sending email: " + mimeMessage.toString(), e);
                }
            }

            @Override
            public void send(SimpleMailMessage[] simpleMessages) throws MailException {
                throw new MailPreparationException("Method not supported");
            }
        };
    }
}
