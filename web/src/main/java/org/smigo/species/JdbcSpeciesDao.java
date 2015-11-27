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

import org.smigo.config.Cache;
import org.smigo.plants.PlantData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
            "LEFT JOIN plants ON species.id = plants.species_id\n" +
            "LEFT JOIN families ON species.family_id = families.id\n" +
            "LEFT JOIN species_translation def ON def.species_id = species.id AND def.language = '' AND def.country = ''\n" +
            "LEFT JOIN species_translation lang ON lang.species_id = species.id AND lang.language = ? AND lang.country = ''\n" +
            "LEFT JOIN species_translation coun ON coun.species_id = species.id AND coun.language = ? AND coun.country = ?\n" +
            "WHERE %s\n" +
            "GROUP BY species.id\n" +
            "ORDER BY COUNT(species.id) DESC\n" +
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
        this.insertSpecies = new SimpleJdbcInsert(dataSource).withTableName("species").usingGeneratedKeyColumns("id");
        this.insertSpeciesTranslation = new SimpleJdbcInsert(dataSource).withTableName("species_translation");
        rowMapper = new SpeciesViewRowMapper();
    }

    @Override
    public int addSpecies(SpeciesFormBean species, int userId) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("name", species.getScientificName(), Types.VARCHAR);
        s.addValue("item", species.isItem(), Types.BOOLEAN);
        s.addValue("annual", species.isAnnual(), Types.BOOLEAN);
        s.addValue("family_id", species.getFamily(), Types.INTEGER);
        s.addValue("creator", userId, Types.INTEGER);
        //todo add createdate
        return insertSpecies.executeAndReturnKey(s).intValue();
    }

    @Override
    @Cacheable(value = Cache.SPECIES)
    public List<Species> getDefaultSpecies() {
        //Unknown, Hemp, Concrete and Sand is never display by default
        return querySpeciesForList("species.id NOT IN (99,87,102,115)", 50, Locale.ROOT, new Object[]{});
    }

    @Override
    public List<Species> getUserSpecies(int userId) {
        return querySpeciesForList("plants.user_id = ?", Integer.MAX_VALUE, Locale.ENGLISH, new Object[]{userId});
    }

    @Override
    public List<Species> searchSpecies(String query, Locale locale) {
        final String whereClause = "def.vernacular_name LIKE ? OR lang.vernacular_name LIKE ? OR coun.vernacular_name LIKE ? OR families.name LIKE ? OR scientific_name LIKE ?";
        return querySpeciesForList(whereClause, 10, locale, new Object[]{query, query, query, query, query});
    }

    @Override
    public List<Species> getSpeciesFromList(List<PlantData> plants) {
        final StringBuilder whereClause = new StringBuilder("species.id IN (");
        for (Iterator<PlantData> i = plants.iterator(); i.hasNext(); ) {
            whereClause.append(i.next().getSpeciesId() + (i.hasNext() ? "," : (")")));
        }
        return querySpeciesForList(whereClause.toString(), Integer.MAX_VALUE, Locale.ENGLISH, new Object[]{});
    }

    @Override
    @Cacheable(value = Cache.SPECIES_TRANSLATION, key = "#locale")
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
    @Cacheable(Cache.SPECIES_ID)
    public Species getSpecies(int id) {
        final Locale locale = Locale.ENGLISH;
        final Object[] args = {locale.getLanguage(), locale.getLanguage(), locale.getCountry(), id};
        final String sql = String.format(SELECT, "species.id = ?", 1);
        return jdbcTemplate.queryForObject(sql, args, rowMapper);
    }

    @Override
    @CacheEvict(value = Cache.SPECIES_TRANSLATION, allEntries = true)
    public void setSpeciesTranslation(int id, String vernacularName, Locale locale) {
        String language = locale == null ? "" : locale.getLanguage();
        String country = locale == null ? "" : locale.getCountry();
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("species_id", id, Types.INTEGER);
        s.addValue("language", language, Types.VARCHAR);
        s.addValue("country", country, Types.VARCHAR);
        s.addValue("vernacular_name", vernacularName);
        insertSpeciesTranslation.execute(s);
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
            return ret;
        }
    }
}
