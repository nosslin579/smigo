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
@Profile(EnvironmentProfile.PRODUCTION)
public class ProductionConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://198.38.82.101/nosslin2_db");
        ds.setUser("nosslin2_dbuser");
        ds.setPassword("N9WM[ONGP5yv");
        return ds;
    }


    @Bean
    public HostEnvironmentInfo hostEnvironmentInfo() {
        return new HostEnvironmentInfo(EnvironmentProfile.PRODUCTION, false, "/home/nosslin2/public_html/pic/");
    }

    @Bean
    public MessageSource messageSource() {
        log.debug("getMessageSource");
        return new UserAdaptiveMessageSource(-1);
    }

    @Bean
    public Props props() {
        return new Props() {
            @Override
            public String getResetUrl() {
                return "http://smigo.org/login-reset/";
            }
        };
    }


}
