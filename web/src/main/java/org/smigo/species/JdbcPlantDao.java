package org.smigo.species;

import kga.PlantData;
import org.codehaus.jackson.map.ObjectMapper;
import org.smigo.entities.PlantDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcPlantDao implements PlantDao {
    private static final String SELECT = "SELECT * FROM plants WHERE fkuserid = ?";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PlantData> getPlants(int userId) {
        final String sql = String.format(SELECT);
        return jdbcTemplate.query(sql, new Object[]{userId}, new RowMapper<PlantData>() {
            @Override
            public PlantData mapRow(ResultSet speciesRS, int rowNum) throws SQLException {
                return new PlantDataBean(
                        speciesRS.getInt("species"),
                        speciesRS.getInt("year"),
                        speciesRS.getInt("x"),
                        speciesRS.getInt("y")
                );
            }
        });
    }
}