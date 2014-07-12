package org.smigo.user;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcUserDao implements UserDao {
    private static final String SELECT = "SELECT * FROM users WHERE %s=?";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;
    private SimpleJdbcInsert insertOpenId;
    private final BeanPropertyRowMapper<User> mapper = BeanPropertyRowMapper.newInstance(User.class);

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("id");
        this.insertOpenId = new SimpleJdbcInsert(dataSource).withTableName("openid").usingGeneratedKeyColumns("id");
    }

    @Override
    public void addUser(User user, long signupTime, long decideTime) {
        final Map map = objectMapper.convertValue(user, Map.class);
        map.put("regtime", signupTime);
        map.put("decidetime", decideTime);
        Number newId = insert.executeAndReturnKey(map);
        user.setId(newId.intValue());
    }

    @Override
    public void addOpenId(int userId, String identityUrl) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", userId);
        map.put("identity_url", identityUrl);
        insertOpenId.execute(map);
    }

    @Override
    public User getUserByUsername(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, mapper);
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.query(sql, new Object[]{username}, mapper);
    }

    @Override
    public User getUserById(int id) {
        final String sql = String.format(SELECT, "id");
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
    }

    @Override
    public User getUserByEmail(String email) {
        final String sql = String.format(SELECT, "email");
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, mapper);
    }

    @Override
    public User getUserByOpenId(String identityUrl) {
        final String sql = "SELECT * FROM users JOIN openid ON openid.user_id = users.user_id WHERE openid.identity_url = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{identityUrl}, mapper);
    }

}