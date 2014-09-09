package org.smigo.user;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    public int addUser(RegisterFormBean user, String encodedPassword, long signupTime, long decideTime) {
        final Map map = objectMapper.convertValue(user, Map.class);
        map.put("regtime", signupTime);
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
    public User getUserByUsername(String username) {
        final String sql = String.format(SELECT, "username");
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, mapper);
    }

    @Override
    public List<? extends User> getUsersByUsername(String username) {
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
        final String sql = "SELECT users.* FROM users JOIN openid ON openid.user_id = users.id WHERE openid.identity_url = ?";
        final List<UserBean> query = jdbcTemplate.query(sql, new Object[]{identityUrl}, mapper);
        return query.isEmpty() ? null : query.get(0);
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET email = ?, termsofservice = ? WHERE id = ?";
        Object[] args = {user.getEmail(), user.isTermsofservice(), user.getId()};
        int[] types = {Types.VARCHAR, Types.BOOLEAN, Types.INTEGER};
        jdbcTemplate.update(sql, args, types);
    }

}