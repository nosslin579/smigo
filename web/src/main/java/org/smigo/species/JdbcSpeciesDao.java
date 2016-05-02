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

import org.smigo.species.vernacular.Vernacular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository
class JdbcSpeciesDao implements SpeciesDao {

    private static final String SELECT = "SELECT * FROM species\n" +
            "LEFT JOIN families ON species.family_id = families.id\n" +
            "WHERE %s\n" +
            "GROUP BY species.id LIMIT %d;";

    private static final String DEFAULTICONNAME = "defaulticon.png";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertSpecies;
    private Map<Integer, Family> families;
    private final RowMapper<Species> rowMapper = new SpeciesRowMapper();
    private final RowMapper<Vernacular> vernacularRowMapper = new BeanPropertyRowMapper<>(Vernacular.class);

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertSpecies = new SimpleJdbcInsert(dataSource).withTableName("species").usingGeneratedKeyColumns("id").usingColumns("creator");
    }

    @Override
    public int addSpecies(int userId) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("creator", userId, Types.INTEGER);
        return insertSpecies.executeAndReturnKey(s).intValue();
    }

    @Override
    public List<Species> getDefaultSpecies() {
        //Unknown, Hemp, Concrete and Sand is never display by default
        String whereClause = "SPECIES.ID IN (SELECT SPECIES_ID FROM PLANTS WHERE SPECIES_ID NOT IN (99,87,102,115) GROUP BY SPECIES_ID ORDER BY count(SPECIES_ID) DESC LIMIT 50)";
        final String sql = String.format(SELECT, whereClause, 50);
        return jdbcTemplate.query(sql, new Object[]{}, rowMapper);
    }

    @Override
    public List<Species> searchSpecies(String search, Locale locale) {
        String query = (search.length() >= 5 ? "%" : "") + search + (search.length() >= 3 ? "%" : "");
        final String whereClause = "SPECIES.ID IN (SELECT SPECIES_ID FROM SPECIES_TRANSLATION WHERE VERNACULAR_NAME LIKE ?) OR SCIENTIFIC_NAME LIKE ? OR FAMILIES.NAME LIKE ? OR CAST(SPECIES.ID AS TEXT) LIKE ?";
        final String sql = String.format(SELECT, whereClause, 10);
        return jdbcTemplate.query(sql, new Object[]{query, query, query, search}, rowMapper);
    }

    @Override
    public Species getSpecies(int id) {
        final String sql = String.format(SELECT, "SPECIES.ID = ?", 1);
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    }

    @Override
    public void updateSpecies(int id, Species species) {
        String sql = "UPDATE SPECIES SET SCIENTIFIC_NAME = ?, ICONFILENAME = ?, FAMILY_ID = ? WHERE ID = ?;";
        jdbcTemplate.update(sql, species.getScientificName(), species.getIconFileName(), species.getFamilyId(), id);
    }

    @Override
    public void deleteSpecies(int speciesId) {
        String sql = "DELETE FROM SPECIES WHERE ID = ?";
        jdbcTemplate.update(sql, speciesId);
    }

    private static class SpeciesRowMapper implements RowMapper<Species> {

        @Override
        public Species mapRow(ResultSet rs, int rowNum) throws SQLException {
            Species ret = new Species(rs.getInt("species.id"));
            ret.setScientificName(rs.getString("scientific_name"));
            ret.setItem(rs.getBoolean("item"));
            ret.setAnnual(rs.getBoolean("annual"));
            ret.setFamily(Family.create(rs.getInt("families.id"), rs.getString("families.name")));
            String iconfilename = rs.getString("iconfilename");
            ret.setIconFileName(iconfilename == null ? DEFAULTICONNAME : iconfilename);
            ret.setCreator(rs.getInt("creator"));
            return ret;
        }
    }
}
