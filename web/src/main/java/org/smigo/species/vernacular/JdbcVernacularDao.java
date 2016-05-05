package org.smigo.species.vernacular;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@Repository
class JdbcVernacularDao implements VernacularDao {


    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertVernacular;
    private final RowMapper<Vernacular> vernacularRowMapper = new BeanPropertyRowMapper<>(Vernacular.class);

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertVernacular = new SimpleJdbcInsert(dataSource).withTableName("VERNACULARS").
                usingGeneratedKeyColumns("id").usingColumns("species_id", "vernacular_name", "language", "country");
    }


    @Override
    public void deleteVernacular(int vernacularId) {
        String sql = "DELETE FROM VERNACULARS WHERE ID = ?";
        jdbcTemplate.update(sql, vernacularId);
    }

    @Override
    public List<Vernacular> getVernacular(Locale locale) {
        final String sql = "SELECT * FROM VERNACULARS WHERE LANGUAGE=? AND (COUNTRY=? OR COUNTRY='');";
        return jdbcTemplate.query(sql, vernacularRowMapper, locale.getLanguage(), locale.getCountry());
    }

    @Override
    public int insertVernacular(Vernacular vernacular) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(vernacular);
        //http://stackoverflow.com/questions/5483139/springs-simplejdbcinsert-doesnt-produce-auto-generated-keys-as-expected
        return insertVernacular.executeAndReturnKey(parameterSource).intValue();
    }

    @Override
    public Vernacular getVernacularById(int vernacularId) {
        final String sql = "SELECT * FROM VERNACULARS WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{vernacularId}, vernacularRowMapper);

    }

    @Override
    public List<Vernacular> getVernacularBySpecies(int speciesId) {
        final String sql = "SELECT * FROM VERNACULARS WHERE SPECIES_ID=?";
        return jdbcTemplate.query(sql, vernacularRowMapper, speciesId);
    }

    @Override
    public void updateVernacular(Vernacular vernacular) {
        String sql = "UPDATE VERNACULARS SET VERNACULAR_NAME= ? WHERE ID = ?;";
        jdbcTemplate.update(sql, vernacular.getVernacularName(), vernacular.getId());
    }
}
