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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserAdaptiveMessageSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Properties;

@Configuration
@Profile(EnvironmentProfile.PRODUCTION)
@PropertySource({"classpath:prod.properties"})
public class ProductionConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${mailSenderUsername}")
    private String mailSenderUsername;
    @Value("${mailSenderPassword}")
    private String mailSenderPassword;
    @Value("${mailSenderHost}")
    private String mailSenderHost;
    @Value("${mailSenderPort}")
    private Integer mailSenderPort;

    @Bean
    public MessageSource messageSource() {
        log.debug("MessageSource init");
        return new UserAdaptiveMessageSource(-1);
    }

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        log.debug("JavaMailSender init");
        final JavaMailSenderImpl ret = new JavaMailSenderImpl();
        ret.setUsername(mailSenderUsername);
        ret.setPassword(mailSenderPassword);
        ret.setHost(mailSenderHost);
        ret.setPort(mailSenderPort);
        ret.setDefaultEncoding("UTF-8");

        final Properties props = new Properties();
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.ssl.trust", mailSenderHost);

        ret.setJavaMailProperties(props);
        return ret;
    }
}
