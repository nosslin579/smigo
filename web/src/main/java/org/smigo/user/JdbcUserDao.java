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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final RowMapper<UserBean> mapper = new UserBeanRowMapper();

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("id").usingColumns("username", "locale", "termsofservice", "email", "displayname", "decidetime", "about", "password");
        this.insertOpenId = new SimpleJdbcInsert(dataSource).withTableName("openid").usingGeneratedKeyColumns("id");
    }

    @Override
    public int addUser(RegisterFormBean user, String encodedPassword, long decideTime) {
        final Map map = objectMapper.convertValue(user, Map.class);
        map.put("decidetime", decideTime);
        map.put("password", encodedPassword);
        Number newId = insert.executeAndReturnKey(map);
        return newId.intValue();
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
    public void updateUser(int id, UserBean user) {
        String sql = "UPDATE users SET email = ?, termsofservice = ?, about = ?, locale = ? , displayname = ? WHERE id = ?";
        Object[] args = {user.getEmail(), user.isTermsOfService(), user.getAbout(), user.getLocale(), user.getDisplayName(), id};
        int[] types = {Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
        jdbcTemplate.update(sql, args, types);
    }

    @Override
    public List<UserDetails> getUserDetails(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.query(sql, new UserDetailsRowMapper(), username);
    }

    @Override
    public UserBean getUser(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, mapper);
    }

    @Override
    public void deleteOpenId(String openIdUrl) {
        String sql = "DELETE FROM openid WHERE identity_url = ?";
        jdbcTemplate.update(sql, new Object[]{openIdUrl}, new int[]{Types.VARCHAR});
    }

    @Override
    public void updatePassword(int userId, String encodedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        Object[] args = {encodedPassword, userId};
        int[] types = {Types.VARCHAR, Types.INTEGER};
        jdbcTemplate.update(sql, args, types);
    }

    @Override
    public UserDetails getUserDetails(int userId) {
        final String sql = String.format(SELECT, "id");
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserDetailsRowMapper());
    }

    @Override
    public User getUserById(int id) {
        final String sql = String.format(SELECT, "id");
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(), id);
    }

    private static class UserDetailsRowMapper implements RowMapper<UserDetails> {
        @Override
        public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            String username = rs.getString("username");
            String password = rs.getString("password");
            int id = rs.getInt("id");
            return new AuthenticatedUser(id, username, password);
        }
    }


    private static class UserBeanRowMapper implements RowMapper<UserBean> {
        @Override
        public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserBean ret = new UserBean();
            ret.setTermsOfService(rs.getBoolean("termsofservice"));
            ret.setAbout(rs.getString("about"));
            ret.setUsername(rs.getString("username"));
            ret.setDisplayName(rs.getString("displayname"));
            ret.setEmail(rs.getString("email"));
            ret.setLocale(StringUtils.parseLocaleString(rs.getString("locale")));
            return ret;
        }
    }
}
