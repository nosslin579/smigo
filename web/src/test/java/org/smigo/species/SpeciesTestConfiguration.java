package org.smigo.species;

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

import org.h2.jdbcx.JdbcConnectionPool;
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
public class SpeciesTestConfiguration {

    @Value("${databaseUser}")
    private String databaseUser;
    @Value("${databasePassword}")
    private String databasePassword;
    @Value("${databaseUrl}")
    private String databaseUrl;

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        return JdbcConnectionPool.create(databaseUrl, databaseUser, databasePassword);
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
    public SpeciesDao speciesDao() {
        return new JdbcSpeciesDao();
    }

    @Bean
    public FamilyDao familyDao() {
        return new JdbcFamilyDao();
    }

}
