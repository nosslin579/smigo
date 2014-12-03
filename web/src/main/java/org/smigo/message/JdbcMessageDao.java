package org.smigo.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class JdbcMessageDao implements MessageDao {
    private static final String SELECT = "SELECT\n" +
            "messages.id,\n" +
            "messages.text,\n" +
            "users.username,\n" +
            "messages.createdate  \n" +
            "FROM messages\n" +
            "LEFT JOIN users ON users.id = messages.submitter_user_id\n" +
            "WHERE location = ?;\n";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ObjectMapper objectMapper;
    private SimpleJdbcInsert insert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("messages").usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Message> getMessage(String location) {
        final String sql = String.format(SELECT);
        return jdbcTemplate.query(sql, new Object[]{location}, new RowMapper<Message>() {
            @Override
            public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Message(rs.getInt("id"), rs.getString("text"), rs.getString("username"), rs.getDate("createdate"));
            }
        });
    }

    @Override
    public int addMessage(Message message, AuthenticatedUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("text", message.getText());
        map.put("submitter_user_id", user.getId());
        map.put("location", "wall");
        return insert.execute(map);
    }
}