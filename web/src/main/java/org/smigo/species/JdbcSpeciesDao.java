package org.smigo.species;

import kga.Family;
import org.codehaus.jackson.map.ObjectMapper;
import org.smigo.SpeciesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcSpeciesDao implements SpeciesDao {
    private static final String SELECT = "SELECT * FROM species";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<SpeciesView> getSpecies() {
        final String sql = String.format(SELECT);
        return jdbcTemplate.query(sql, new Object[]{}, new RowMapper<SpeciesView>() {
            @Override
            public SpeciesView mapRow(ResultSet rs, int rowNum) throws SQLException {
                SpeciesView speciesView = new SpeciesView(
                        rs.getInt("species_id"),
                        rs.getString("scientificname"),
                        rs.getBoolean("item"),
                        rs.getBoolean("annual"),
                        new Family("someFamily", rs.getInt("familyid")));

                speciesView.setIconFileName(rs.getString("iconname"));

                return speciesView;
            }
        });
    }
}