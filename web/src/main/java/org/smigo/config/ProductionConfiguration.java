package org.smigo.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.i18n.UserAdaptiveMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@ProdProfile
public class ProductionConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        log.debug("Get BoneCPDataSource from profile prod");
        BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
        boneCPDataSource.setDriverClass("com.mysql.jdbc.Driver");
        boneCPDataSource.setJdbcUrl("jdbc:mysql://smigo.org/nosslin2_db");
        boneCPDataSource.setUsername("nosslin2_dbuser");
        boneCPDataSource.setPassword("N9WM[ONGP5yv");
        boneCPDataSource.setIdleConnectionTestPeriodInMinutes(30);
        boneCPDataSource.setIdleMaxAgeInMinutes(120);
        boneCPDataSource.setMinConnectionsPerPartition(1);
        boneCPDataSource.setPartitionCount(3);
        boneCPDataSource.setAcquireIncrement(1);
        boneCPDataSource.setStatementsCacheSize(100);
        return boneCPDataSource;
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
