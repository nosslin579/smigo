package org.smigo.species;

import org.smigo.config.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class JdbcRuleDao implements RuleDao {
    private static final String SELECT = "SELECT * FROM rules";
    private static final String SELECT_IMPACTS = "SELECT * FROM rules_x_impacts";
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
        final Map<Integer, List<Integer>> impacts = getImpacts();
        return jdbcTemplate.query(SELECT, new RowMapper<RuleBean>() {
            @Override
            public RuleBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                final int id = rs.getInt("rule_id");
                final int host = rs.getInt("host");
                final int type = rs.getInt("type");
                final int causerSpecies = rs.getInt("causer"); //todo rename to causerspecies
                final int causerFamily = rs.getInt("causerfamily");
                final int gap = rs.getInt("gap"); //todo should return null if not set
                return RuleBean.create(id, host, type, causerSpecies, causerFamily, gap, impacts.get(id));
            }
        });
    }

    public Map<Integer, List<Integer>> getImpacts() {
        return jdbcTemplate.query(SELECT_IMPACTS, new ResultSetExtractor<Map<Integer, List<Integer>>>() {
            @Override
            public Map<Integer, List<Integer>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, List<Integer>> ret = new HashMap<Integer, List<Integer>>();
                while (rs.next()) {
                    final int ruleId = rs.getInt("rule_id");
                    final int impactId = rs.getInt("impact_id");
                    if (ret.get(ruleId) == null) {
                        ret.put(ruleId, new ArrayList<>());
                    }
                    ret.get(ruleId).add(impactId);
                }
                return ret;
            }
        });
    }
}