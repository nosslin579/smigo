package org.smigo.plants;

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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
class JdbcPlantDao implements PlantDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private RowMapper<Plant> rowMapper = new BeanPropertyRowMapper<>(Plant.class);

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Plant> getPlants(int userId) {
        final String sql = "SELECT * FROM PLANTS WHERE USER_ID=?";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    @Override
    public List<Plant> getPlantsBySpecies(int speciesId) {
        final String sql = "SELECT * FROM PLANTS WHERE SPECIES_ID=?";
        return jdbcTemplate.query(sql, rowMapper, speciesId);
    }


    @Override
    public List<Plant> getPlants(String username) {
        final String sql = "SELECT * FROM PLANTS WHERE USER_ID = (SELECT ID FROM USERS WHERE USERNAME=?)";
        return jdbcTemplate.query(sql, rowMapper, username);
    }

    @Override
    public void addPlants(List<Plant> plants, int userId) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(plants.toArray());
        namedParameterJdbcTemplate.batchUpdate(
                "INSERT INTO plants(user_id, species_id, year, x, y, variety_id) VALUES (" + userId + ", :speciesId, :year, :x, :y, :varietyId)",
                batch);
    }

    @Override
    public void deletePlants(List<Plant> plants, int userId) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(plants.toArray());
        namedParameterJdbcTemplate.batchUpdate(
                "DELETE FROM plants WHERE (user_id = " + userId + " AND species_id = :speciesId AND year = :year AND x = :x AND y = :y)",
                batch);
    }

    @Override
    public void deletePlant(int userId, Plant plant) {
        String sql = "DELETE FROM plants WHERE (user_id = ? AND species_id = ? AND year = ? AND x = ? AND y = ?)";
        jdbcTemplate.update(sql, userId, plant.getSpeciesId(), plant.getYear(), plant.getX(), plant.getY());
    }

    @Override
    public void addPlant(int userId, Plant plant) {
        String sql = "INSERT INTO plants(user_id, species_id, year, x, y, variety_id) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, userId, plant.getSpeciesId(), plant.getYear(), plant.getX(), plant.getY(), plant.getVarietyId());
    }

    @Override
    public void replaceSpecies(int oldSpeciesId, int newSpeciesId) {
        String sql = "UPDATE PLANTS SET SPECIES_ID=? WHERE SPECIES_ID = ?;";
        jdbcTemplate.update(sql, newSpeciesId, oldSpeciesId);
    }

}
