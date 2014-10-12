package org.smigo.species;

import kga.Family;
import kga.IdUtil;
import kga.PlantData;
import org.smigo.SpeciesView;
import org.smigo.config.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
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
            "species.*,\n" +
            "coalesce(coun.vernacular_name, lang.vernacular_name, def.vernacular_name) AS vernacular_name\n" +
            "FROM species\n" +
            "LEFT JOIN plants ON species.id = plants.species\n" +
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

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertSpecies = new SimpleJdbcInsert(dataSource).withTableName("species").usingGeneratedKeyColumns("id");
        this.insertSpeciesTranslation = new SimpleJdbcInsert(dataSource).withTableName("species_translation");
    }

    @Override
    public int addSpecies(SpeciesFormBean species, int userId) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("name", species.getScientificName(), Types.VARCHAR);
        s.addValue("item", species.isItem(), Types.BOOLEAN);
        s.addValue("annual", species.isAnnual(), Types.BOOLEAN);
        s.addValue("family", species.getFamily(), Types.INTEGER);
        s.addValue("creator", userId, Types.INTEGER);
        return insertSpecies.executeAndReturnKey(s).intValue();
    }

    @Override
    @Cacheable(value = Cache.SPECIES, key = "#locale")
    public List<SpeciesView> getDefaultSpecies(Locale locale) {
        //Unknown and Hemp is never display by default
        return querySpeciesForList("species.id NOT IN (99,87)", 58, locale, new Object[]{});
    }

    @Override
    public List<SpeciesView> getUserSpecies(int userId, Locale locale) {
        return querySpeciesForList("plants.fkuserid = ?", Integer.MAX_VALUE, locale, new Object[]{userId});
    }

    @Override
    public List<SpeciesView> searchSpecies(String query, Locale locale) {
        final String whereClause = "def.vernacular_name LIKE ? OR lang.vernacular_name LIKE ? OR coun.vernacular_name LIKE ? OR name LIKE ?";
        return querySpeciesForList(whereClause, 10, locale, new Object[]{query, query, query, query});
    }

    @Override
    public List<SpeciesView> getSpeciesFromList(List<PlantData> plants, Locale locale) {
        final StringBuilder whereClause = new StringBuilder("species.id IN (");
        for (Iterator<PlantData> i = plants.iterator(); i.hasNext(); ) {
            whereClause.append(i.next().getSpeciesId() + (i.hasNext() ? "," : (")")));
        }
        return querySpeciesForList(whereClause.toString(), Integer.MAX_VALUE, locale, new Object[]{});
    }

    private List<SpeciesView> querySpeciesForList(String whereClause, int maxResult, Locale locale, Object[] args) {
        List<Object> sqlArgs = new ArrayList<Object>();
        sqlArgs.add(locale.getLanguage());
        sqlArgs.add(locale.getLanguage());
        sqlArgs.add(locale.getCountry());
        sqlArgs.addAll(Arrays.asList(args));
        final String sql = String.format(SELECT, whereClause, maxResult);
        final SpeciesViewRowMapper rowMapper = new SpeciesViewRowMapper(IdUtil.convertToMap(familyDao.getFamilies()));
        return jdbcTemplate.query(sql, sqlArgs.toArray(), rowMapper);
    }

    @Override
    public SpeciesView getSpecies(int id, Locale locale) {
        final Object[] args = {locale.getLanguage(), locale.getLanguage(), locale.getCountry(), id};
        final String sql = String.format(SELECT, "species.id = ?", 1);
        return jdbcTemplate.queryForObject(sql, args, new SpeciesViewRowMapper(IdUtil.convertToMap(familyDao.getFamilies())));
    }

    @Override
    public void setSpeciesTranslation(int id, String vernacularName, String language, String country) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("species_id", id, Types.INTEGER);
        s.addValue("language", language, Types.VARCHAR);
        s.addValue("country", country, Types.VARCHAR);
        s.addValue("vernacular_name", vernacularName);
        insertSpeciesTranslation.execute(s);
    }

    private static class SpeciesViewRowMapper implements RowMapper<SpeciesView> {
        private final Map<Integer, Family> familyMap;

        public SpeciesViewRowMapper(Map<Integer, Family> familyMap) {
            this.familyMap = familyMap;
        }

        @Override
        public SpeciesView mapRow(ResultSet rs, int rowNum) throws SQLException {
            SpeciesView ret = new SpeciesView();
            ret.setId(rs.getInt("id"));
            ret.setScientificName(rs.getString("name"));
            ret.setItem(rs.getBoolean("item"));
            ret.setAnnual(rs.getBoolean("annual"));
            ret.setFamily(familyMap.get(rs.getInt("family")));
            String iconfilename = rs.getString("iconfilename");
            ret.setIconFileName(iconfilename == null ? DEFAULTICONNAME : iconfilename);
            ret.setVernacularName(rs.getString("vernacular_name"));
            return ret;
        }
    }
}