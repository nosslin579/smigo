package org.smigo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.JdbcUserDao;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@PropertySource({"classpath:local.properties"})
public class TestConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${databaseUser}")
    private String databaseUser;
    @Value("${databasePassword}")
    private String databasePassword;
    @Value("${databaseUrl}")
    private String databaseUrl;

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setUser(databaseUser);
        ds.setPassword(databasePassword);
        ds.setJdbcUrl(databaseUrl);
        ds.setIdleConnectionTestPeriod(160);
        return ds;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static PropertyOverrideConfigurer propertyOverrideConfigurer() {
        return new PropertyOverrideConfigurer();
    }

    @Bean
    public UserDao userDao() {
        return new JdbcUserDao();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
