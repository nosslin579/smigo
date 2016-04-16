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
import java.util.*;

@Repository
class JdbcSpeciesDao implements SpeciesDao {

    private static final String SELECT = "SELECT\n" +
            "species.*, families.name AS family_name,\n" +
            "coalesce(coun.vernacular_name, lang.vernacular_name, def.vernacular_name) AS vernacular_name\n" +
            "FROM species\n" +
            "LEFT JOIN families ON species.family_id = families.id\n" +
            "LEFT JOIN species_translation def ON def.species_id = species.id AND def.language = '' AND def.country = ''\n" +
            "LEFT JOIN species_translation lang ON lang.species_id = species.id AND lang.language = ? AND lang.country = ''\n" +
            "LEFT JOIN species_translation coun ON coun.species_id = species.id AND coun.language = ? AND coun.country = ?\n" +
            "WHERE %s\n" +
            "GROUP BY species.id\n" +
            "LIMIT %d;\n";

    private static final String DEFAULTICONNAME = "defaulticon.png";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertSpecies;
    private SimpleJdbcInsert insertSpeciesTranslation;
    private Map<Integer, Family> families;
    private SpeciesViewRowMapper rowMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertSpecies = new SimpleJdbcInsert(dataSource).withTableName("species").usingGeneratedKeyColumns("id").usingColumns("creator");
        this.insertSpeciesTranslation = new SimpleJdbcInsert(dataSource).withTableName("species_translation");
        rowMapper = new SpeciesViewRowMapper();
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
        return querySpeciesForList(whereClause, 50, Locale.ROOT, new Object[]{});
    }

    @Override
    public List<Species> getUserSpecies(int userId) {
        final String whereClause = "SPECIES.ID IN (SELECT SPECIES_ID FROM PLANTS WHERE USER_ID = " + userId + ")";
        return querySpeciesForList(whereClause, Integer.MAX_VALUE, Locale.ENGLISH, new Object[]{});
    }

    @Override
    public List<Species> searchSpecies(String query, Locale locale) {
        final String whereClause = "coalesce(coun.vernacular_name, lang.vernacular_name, def.vernacular_name) IS NOT NULL AND (def.vernacular_name LIKE ? OR lang.vernacular_name LIKE ? OR coun.vernacular_name LIKE ? OR families.name LIKE ? OR scientific_name LIKE ?)";
        return querySpeciesForList(whereClause, 10, locale, new Object[]{query, query, query, query, query});
    }

    @Override
    public Map<String, String> getSpeciesTranslation(Locale locale) {
        List<Object> sqlArgs = new ArrayList<Object>();
        sqlArgs.add(locale.getLanguage());
        sqlArgs.add(locale.getLanguage());
        sqlArgs.add(locale.getCountry());
        int[] types = new int[sqlArgs.size()];
        Arrays.fill(types, Types.VARCHAR);
        final String sql = String.format(SELECT, "TRUE", 1000);
        return jdbcTemplate.query(sql, sqlArgs.toArray(), types, new ResultSetExtractor<Map<String, String>>() {
            @Override
            public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, String> ret = new HashMap<>(rs.getFetchSize());
                while (rs.next()) {
                    ret.put("msg.species" + rs.getInt("id"), rs.getString("vernacular_name"));
                }
                return ret;
            }
        });
    }

    @Override
    public Map<Locale, String> getSpeciesTranslation(int speciesId) {
        final String sql = "SELECT * FROM species_translation WHERE species_id = ?";
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

    private List<Species> querySpeciesForList(String whereClause, int maxResult, Locale locale, Object[] args) {
        List<Object> sqlArgs = new ArrayList<Object>();
        sqlArgs.add(locale.getLanguage());
        sqlArgs.add(locale.getLanguage());
        sqlArgs.add(locale.getCountry());
        sqlArgs.addAll(Arrays.asList(args));
        final String sql = String.format(SELECT, whereClause, maxResult);
        return jdbcTemplate.query(sql, sqlArgs.toArray(), rowMapper);
    }

    @Override
    public Species getSpecies(int id) {
        final Locale locale = Locale.ENGLISH;
        final Object[] args = {locale.getLanguage(), locale.getLanguage(), locale.getCountry(), id};
        final String sql = String.format(SELECT, "species.id = ?", 1);
        return jdbcTemplate.queryForObject(sql, args, rowMapper);
    }

    @Override
    public void insertSpeciesTranslation(int id, String vernacularName, Locale locale) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("species_id", id, Types.INTEGER);
        s.addValue("language", locale.getLanguage(), Types.VARCHAR);
        s.addValue("country", locale.getCountry(), Types.VARCHAR);
        s.addValue("vernacular_name", vernacularName);
        insertSpeciesTranslation.execute(s);
    }

    @Override
    public void setSpeciesTranslation(int id, String vernacularName, Locale locale) {
        String sql = "MERGE INTO SPECIES_TRANSLATION (SPECIES_ID, LANGUAGE, COUNTRY, VERNACULAR_NAME) KEY (SPECIES_ID, LANGUAGE, COUNTRY) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, locale.getLanguage(), locale.getCountry(), vernacularName);
    }

    @Override
    public void updateSpecies(int id, Species species) {
        String sql = "UPDATE SPECIES SET SCIENTIFIC_NAME = ?, ICONFILENAME = ?, FAMILY_ID = ? WHERE ID = ?;";
        jdbcTemplate.update(sql, species.getScientificName(), species.getIconFileName(), species.getFamilyId(), id);
    }

    private static class SpeciesViewRowMapper implements RowMapper<Species> {

        @Override
        public Species mapRow(ResultSet rs, int rowNum) throws SQLException {
            Species ret = new Species(rs.getInt("id"));
            ret.setScientificName(rs.getString("scientific_name"));
            ret.setItem(rs.getBoolean("item"));
            ret.setAnnual(rs.getBoolean("annual"));
            ret.setFamily(Family.create(rs.getInt("family_id"), rs.getString("family_name")));
            String iconfilename = rs.getString("iconfilename");
            ret.setIconFileName(iconfilename == null ? DEFAULTICONNAME : iconfilename);
            ret.setCreator(rs.getInt("creator"));
            return ret;
        }
    }
}
