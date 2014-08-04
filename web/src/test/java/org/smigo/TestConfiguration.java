package org.smigo;

import com.zaxxer.hikari.HikariDataSource;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.JdbcUserDao;
import org.smigo.user.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        log.debug("getDataSource from testconfig");
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(15);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://smigo.org/nosslin2_dbtest");
        ds.setUsername("nosslin2_dbusert");
        ds.setPassword("To4[n=GGkp2l");
        return ds;
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
