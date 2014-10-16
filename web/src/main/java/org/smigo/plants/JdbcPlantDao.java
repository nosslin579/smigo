package org.smigo.plants;

import com.fasterxml.jackson.databind.ObjectMapper;
import kga.PlantData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
class JdbcPlantDao implements PlantDao {
    private static final String SELECT = "SELECT fkuserid,year,x,y,species FROM plants JOIN users ON users.id = plants.fkuserid WHERE %s = ?";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ObjectMapper objectMapper;

    private RowMapper<PlantData> plantDataRowMapper = new RowMapper<PlantData>() {
        @Override
        public PlantData mapRow(ResultSet speciesRS, int rowNum) throws SQLException {
            return new PlantDataBean(
                    speciesRS.getInt("species"),
                    speciesRS.getInt("year"),
                    speciesRS.getInt("x"),
                    speciesRS.getInt("y")
            );
        }
    };


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PlantData> getPlants(int userId) {
        final String sql = String.format(SELECT, "fkuserid");
        return jdbcTemplate.query(sql, new Object[]{userId}, plantDataRowMapper);
    }

    @Override
    public List<PlantData> getPlants(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.query(sql, new Object[]{username}, plantDataRowMapper);
    }

    @Override
    public void addPlants(List<? extends PlantData> plants, int userId) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(plants.toArray());
        namedParameterJdbcTemplate.batchUpdate(
                "INSERT INTO plants(fkuserid, species, year, x, y) VALUES (" + userId + ", :speciesId, :year, :x, :y)",
                batch);
    }

    @Override
    public void deletePlants(List<? extends PlantData> plants, int userId) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(plants.toArray());
        namedParameterJdbcTemplate.batchUpdate(
                "DELETE FROM plants WHERE (fkuserid = " + userId + " AND species = :speciesId AND year = :year AND x = :x AND y = :y)",
                batch);
    }
}