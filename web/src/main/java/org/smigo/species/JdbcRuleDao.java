package org.smigo.species;

import kga.Family;
import kga.IdUtil;
import kga.Species;
import kga.rules.Rule;
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
import java.util.Map;

@Repository
class JdbcRuleDao implements RuleDao {
    private static final String SELECT = "SELECT * FROM rules WHERE causerfamily IS NULL";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    private FamilyDao familyDao;

    @Cacheable(Cache.RULES)
    public List<Rule> getRules() {
        final String sql = String.format(SELECT);
        final Map<Integer, Family> familyMap = IdUtil.convertToMap(familyDao.getFamilies());
        return jdbcTemplate.query(sql, new RowMapper<Rule>() {
            @Override
            public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {
                final int ruleId = rs.getInt("rule_id");
                final int type = rs.getInt("type");
                final int gap = rs.getInt("gap");//todo should return null if not set
                final Species speciesCauser = new Species(rs.getInt("causer"));
                final Species host = new Species(rs.getInt("host"));
                final Family familyCauser = familyMap.get(rs.getInt("causerfamily"));
                return WebRule.create(ruleId, host, type, speciesCauser, familyCauser, gap);
            }
        });
    }
}