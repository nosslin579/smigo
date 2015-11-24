package org.smigo.user;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcUserDao implements UserDao {
    private static final String SELECT = "SELECT * FROM users WHERE %s=?";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;
    private SimpleJdbcInsert insertOpenId;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("id").usingColumns("username", "locale", "termsofservice", "email", "displayname", "decidetime", "about", "password");
        this.insertOpenId = new SimpleJdbcInsert(dataSource).withTableName("openid").usingGeneratedKeyColumns("id");
    }

    @Override
    public int addUser(User user) {
        return insert.executeAndReturnKey(new BeanPropertySqlParameterSource(user)).intValue();
    }

    @Override
    public void addOpenId(int userId, String identityUrl) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", userId);
        map.put("identity_url", identityUrl);
        insertOpenId.execute(map);
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
    }

    @Override
    public List<User> getUsersByEmail(String email) {
        final String sql = String.format(SELECT, "email");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
    }

    @Override
    public List<User> getUsersByOpenIDAuthenticationToken(OpenIDAuthenticationToken token) {
        final String sql = "SELECT users.id, username FROM users JOIN openid ON openid.user_id = users.id WHERE openid.identity_url = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), token.getIdentityUrl());
    }

    @Override
    public void deleteOpenId(String openIdUrl) {
        String sql = "DELETE FROM openid WHERE identity_url = ?";
        jdbcTemplate.update(sql, new Object[]{openIdUrl}, new int[]{Types.VARCHAR});
    }

    @Override
    public void updateUser(User u) {
        String sql = "UPDATE users SET email = ?, termsofservice = ?, about = ?, locale = ? , displayname = ?, password = ? WHERE id = ?";
        Object[] args = {u.getEmail(), u.isTermsOfService(), u.getAbout(), u.getLocale(), u.getDisplayName(), u.getPassword(), u.getId()};
        int[] types = {Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
        jdbcTemplate.update(sql, args, types);
    }

    @Override
    public User getUserById(int id) {
        final String sql = String.format(SELECT, "id");
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }
}
