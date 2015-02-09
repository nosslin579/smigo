package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserAdaptiveMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Profile(EnvironmentProfile.PRODUCTION)
@PropertySource({"classpath:default.properties", "classpath:prod.properties"})
public class ProductionConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public MessageSource messageSource() {
        log.debug("getMessageSource");
        return new UserAdaptiveMessageSource(-1);
    }
}
