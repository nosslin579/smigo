package org.smigo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserAdaptiveMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile(EnvironmentProfile.DEVELOPMENT)
public class DevelopmentConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("nosslin2_dbusert");
        ds.setPassword("To4[n=GGkp2l");
        ds.setJdbcUrl("jdbc:mysql://smigo.org/nosslin2_dbtest");
        return ds;
    }

    @Bean
    public HostEnvironmentInfo hostEnvironmentInfo() {
        return new HostEnvironmentInfo(EnvironmentProfile.DEVELOPMENT, true, "/home/nosslin2/");
    }

    @Bean
    public MessageSource messageSource() {
        log.debug("getMessageSource");
        return new UserAdaptiveMessageSource(1);
    }


    @Bean
    public Props props() {
        return new Props() {
            @Override
            public String getResetUrl() {
                return "http://localhost:8080/web/login-reset/";
            }
        };
    }

}
