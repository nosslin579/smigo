package org.smigo.species.varieties;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
class JdbcVarietyDao implements VarietyDao {
    private static final String SELECT = "SELECT * FROM VARIETIES WHERE USER_ID = ?";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("VARIETIES").usingGeneratedKeyColumns("ID CREATEDATE").usingColumns("NAME", "USER_ID", "SPECIES_ID");
    }

    @Override
    public int addVariety(Variety variety) {
        return insert.executeAndReturnKey(new BeanPropertySqlParameterSource(variety)).intValue();
    }

    @Override
    public Collection<Variety> getVarietiesByUser(Integer userId) {
        return jdbcTemplate.query(SELECT, new BeanPropertyRowMapper<>(Variety.class), userId);
    }
}
