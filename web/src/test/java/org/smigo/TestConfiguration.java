package org.smigo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.JdbcUserDao;
import org.smigo.user.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class TestConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setUser("nosslin2_dev");
        ds.setPassword("MC7TCz8Dp5inukeJ6z");
        ds.setJdbcUrl("jdbc:mysql://198.38.82.101/nosslin2_stage4");
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
