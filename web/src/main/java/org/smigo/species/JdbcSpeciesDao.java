package org.smigo.species;

import kga.Family;
import org.smigo.SpeciesView;
import org.smigo.config.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
import java.util.List;
import java.util.Map;

@Repository
class JdbcSpeciesDao implements SpeciesDao {
    private static final String SELECT = "SELECT * FROM species";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert create;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.create = new SimpleJdbcInsert(dataSource).withTableName("species").usingGeneratedKeyColumns("id");
    }

    @Override
    @CacheEvict(value = Cache.SPECIES, allEntries = true)
    public int addSpecies(SpeciesFormBean species, int userId) {
        MapSqlParameterSource s = new MapSqlParameterSource();
        s.addValue("name", species.getScientificName(), Types.VARCHAR);
        s.addValue("item", species.isItem(), Types.BOOLEAN);
        s.addValue("annual", species.isAnnual(), Types.BOOLEAN);
        s.addValue("family", species.getFamily(), Types.INTEGER);
        s.addValue("creator", userId, Types.INTEGER);
        s.addValue("vernacularname", species.getVernacularName());
        Number update = create.executeAndReturnKey(s);
        return update.intValue();
    }

    @Override
    @Cacheable(Cache.SPECIES)
    public List<SpeciesView> getSpecies(final Map<Integer, Family> familyMap) {
        final String sql = String.format(SELECT);
        return jdbcTemplate.query(sql, new Object[]{}, new RowMapper<SpeciesView>() {
            @Override
            public SpeciesView mapRow(ResultSet rs, int rowNum) throws SQLException {
                SpeciesView ret = new SpeciesView();
                ret.setId(rs.getInt("species_id"));
                ret.setScientificName(rs.getString("name"));
                ret.setItem(rs.getBoolean("item"));
                ret.setAnnual(rs.getBoolean("annual"));
                ret.setFamily(familyMap.get(rs.getInt("family")));
                String iconfilename = rs.getString("iconfilename");
                ret.setIconFileName(iconfilename == null ? "defaulticon.png" : iconfilename);
                ret.setVernacularName(rs.getString("vernacularName"));
                return ret;
            }
        });
    }
}