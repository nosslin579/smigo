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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
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
    private SimpleJdbcInsert insertVernacular;
    private Map<Integer, Family> families;
    private SpeciesRowMapper rowMapper = new SpeciesRowMapper();

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertSpecies = new SimpleJdbcInsert(dataSource).withTableName("species").usingGeneratedKeyColumns("id").usingColumns("creator");
        this.insertVernacular = new SimpleJdbcInsert(dataSource).withTableName("species_translation").usingGeneratedKeyColumns("precedence");
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
        final String whereClause = "SPECIES.ID IN (SELECT SPECIES_ID FROM SPECIES_TRANSLATION WHERE VERNACULAR_NAME LIKE ?) OR SCIENTIFIC_NAME LIKE ? OR FAMILIES.NAME LIKE ?";
        final String sql = String.format(SELECT, whereClause, 10);
        return jdbcTemplate.query(sql, new Object[]{query, query, query}, rowMapper);
    }

    @Override
    public Map<String, String> getVernacular(String language, String country) {
        final String sql = "SELECT * FROM SPECIES_TRANSLATION WHERE LANGUAGE = ? AND COUNTRY = ? ORDER BY PRECEDENCE DESC";
        return jdbcTemplate.query(sql, new Object[]{language, country}, new int[]{Types.VARCHAR, Types.VARCHAR}, new ResultSetExtractor<Map<String, String>>() {
            @Override
            public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, String> ret = new HashMap<>(rs.getFetchSize());
                while (rs.next()) {
                    ret.put("msg.species" + rs.getInt("species_id"), rs.getString("vernacular_name"));
                }
                return ret;
            }
        });
    }

    @Override
    public Map<String, String> getSynonyms(String language) {
        final String sql = "SELECT * FROM SPECIES_TRANSLATION WHERE LANGUAGE = ? ORDER BY PRECEDENCE";
        return jdbcTemplate.query(sql, new Object[]{language}, new int[]{Types.VARCHAR}, new ResultSetExtractor<Map<String, String>>() {
            @Override
            public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, String> ret = new HashMap<>(rs.getFetchSize());
                while (rs.next()) {
                    if (rs.getInt("precedence") == 1) {
                        continue;
                    }
                    String speciesMessageKey = "msg.speciesalt" + rs.getInt("species_id");
                    String vernacularName = rs.getString("vernacular_name");
                    String primary = ret.putIfAbsent(speciesMessageKey, vernacularName);
                    if (primary != null) {
                        ret.put(speciesMessageKey, primary + ", " + vernacularName);
                    }
                }
                return ret;
            }
        });
    }

    @Override
    public Map<Locale, String> getVernacular(int speciesId) {
        final String sql = "SELECT * FROM species_translation WHERE species_id = ? ORDER BY PRECEDENCE DESC ";
        return jdbcTemplate.query(sql, new Object[]{speciesId}, new int[]{Types.INTEGER}, new ResultSetExtractor<Map<Locale, String>>() {
            @Override
            public Map<Locale, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Locale, String> ret = new HashMap<>(rs.getFetchSize());
                while (rs.next()) {
                    String language = rs.getString("language");
                    String country = rs.getString("country");
                    String vernacularName = rs.getString("vernacular_name");
                    ret.put(new Locale(language, country), vernacularName);
                }
                return ret;
            }
        });
    }

    @Override
    public Species getSpecies(int id) {
        final String sql = String.format(SELECT, "SPECIES.ID = ?", 1);
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    }

    @Override
    public void insertVernacular(int id, String vernacularName, Locale locale) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("species_id", id, Types.INTEGER);
        s.addValue("language", locale.getLanguage(), Types.VARCHAR);
        s.addValue("country", locale.getCountry(), Types.VARCHAR);
        s.addValue("vernacular_name", vernacularName);
        insertVernacular.execute(s);
    }

    @Override
    public void setVernacular(int id, String vernacularName, Locale locale) {
        String sql = "MERGE INTO SPECIES_TRANSLATION (SPECIES_ID, LANGUAGE, COUNTRY, VERNACULAR_NAME) KEY (SPECIES_ID, LANGUAGE, COUNTRY) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, locale.getLanguage(), locale.getCountry(), vernacularName);
    }

    @Override
    public void updateSpecies(int id, Species species) {
        String sql = "UPDATE SPECIES SET SCIENTIFIC_NAME = ?, ICONFILENAME = ?, FAMILY_ID = ? WHERE ID = ?;";
        jdbcTemplate.update(sql, species.getScientificName(), species.getIconFileName(), species.getFamilyId(), id);
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
