package org.smigo.species;

import org.smigo.config.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
class JdbcRuleDao implements RuleDao {
    private static final String SELECT = "SELECT * FROM rules";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    private FamilyDao familyDao;

    @Override
    @Cacheable(Cache.RULES)
    public List<RuleBean> getRules() {
        return jdbcTemplate.query(SELECT, new RowMapper<RuleBean>() {
            @Override
            public RuleBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                final int id = rs.getInt("rule_id");
                final int host = rs.getInt("host");
                final int type = rs.getInt("type");
                final int causerSpecies = rs.getInt("causer"); //todo rename to causerspecies
                final int causerFamily = rs.getInt("causerfamily");
                final int gap = rs.getInt("gap"); //todo should return null if not set
                return RuleBean.create(id, host, type, causerSpecies, causerFamily, gap);
            }
        });
    }
}