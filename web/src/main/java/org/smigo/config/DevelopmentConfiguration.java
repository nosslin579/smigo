package org.smigo.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserAdaptiveMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@Profile(EnvironmentProfile.DEVELOPMENT)
public class DevelopmentConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setUser("nosslin2_dev");
        ds.setPassword("MC7TCz8Dp5inukeJ6z");
        ds.setJdbcUrl("jdbc:mysql://198.38.82.101/nosslin2_stage4");
        ds.setIdleConnectionTestPeriod(160);
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
                return "http://localhost:8080/login-reset/";
            }
        };
    }

}
