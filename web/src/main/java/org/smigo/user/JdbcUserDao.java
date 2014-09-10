package org.smigo.user;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Repository;

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
    private final BeanPropertyRowMapper<UserBean> mapper = BeanPropertyRowMapper.newInstance(UserBean.class);

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("id");
        this.insertOpenId = new SimpleJdbcInsert(dataSource).withTableName("openid").usingGeneratedKeyColumns("id");
    }

    @Override
    public int addUser(RegisterFormBean user, String encodedPassword, long decideTime) {
        final Map map = objectMapper.convertValue(user, Map.class);
        map.put("decidetime", decideTime);
        map.put("password", encodedPassword);
        map.put("enabled", true);
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
    public List<? extends UserBean> getUsersByUsername(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.query(sql, new Object[]{username}, mapper);
    }

    @Override
    public UserBean getUserById(int id) {
        final String sql = String.format(SELECT, "id");
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
    }

    @Override
    public UserBean getUserByEmail(String email) {
        final String sql = String.format(SELECT, "email");
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, mapper);
    }

    @Override
    public UserDetails getUserDetails(OpenIDAuthenticationToken token) {
        final String sql = "SELECT users.id,username,password FROM users JOIN openid ON openid.user_id = users.id WHERE openid.identity_url = ?";
        Object[] identityUrl = {token.getIdentityUrl()};
        return jdbcTemplate.queryForObject(sql, identityUrl, new UserDetailsRowMapper());
    }

    @Override
    public void updateUser(int id, UserBean user) {
        String sql = "UPDATE users SET email = ?, termsofservice = ? WHERE id = ?";
        Object[] args = {user.getEmail(), user.isTermsofservice(), id};
        int[] types = {Types.VARCHAR, Types.BOOLEAN, Types.INTEGER};
        jdbcTemplate.update(sql, args, types);
    }

    @Override
    public UserDetails getUserDetails(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserDetailsRowMapper());
    }

    @Override
    public UserBean getUser(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, mapper);
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
}