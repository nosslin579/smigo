package org.smigo;

import com.jolbox.bonecp.BoneCPDataSource;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.persitance.DatabaseResource;
import org.smigo.user.JdbcUserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = {DatabaseResource.class, JdbcUserDao.class})
public class TestConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
        boneCPDataSource.setDriverClass("com.mysql.jdbc.Driver");
        boneCPDataSource.setJdbcUrl("jdbc:mysql://smigo.org/nosslin2_dbtest");
        boneCPDataSource.setUsername("nosslin2_dbusert");
        boneCPDataSource.setPassword("To4[n=GGkp2l");
        boneCPDataSource.setIdleConnectionTestPeriodInMinutes(5);
        boneCPDataSource.setIdleMaxAgeInMinutes(5);
        boneCPDataSource.setMinConnectionsPerPartition(1);
        boneCPDataSource.setPartitionCount(1);
        boneCPDataSource.setAcquireIncrement(1);
        boneCPDataSource.setStatementsCacheSize(100);
        boneCPDataSource.setMaxConnectionsPerPartition(2);
        return boneCPDataSource;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
