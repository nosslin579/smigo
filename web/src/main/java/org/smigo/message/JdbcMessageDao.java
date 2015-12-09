package org.smigo.message;

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
import java.util.Locale;
import java.util.Map;

@Repository
class JdbcMessageDao implements MessageDao {
    private static final String SELECT = "SELECT\n" +
            "messages.id,\n" +
            "messages.text,\n" +
            "users.username,\n" +
            "messages.createdate,\n" +
            "users.locale\n" +
            "FROM messages\n" +
            "LEFT JOIN users ON users.id = messages.submitter_user_id\n" +
            "WHERE locale LIKE ?\n" +
            "ORDER BY createdate DESC\n" +
            "LIMIT ?,?;";
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("messages").usingGeneratedKeyColumns("id").usingColumns("text", "submitter_user_id");
    }

    @Override
    public List<Message> getMessage(Locale locale, int from, int size) {
        final String whereParameter = locale.getLanguage() + "%";
        return jdbcTemplate.query(SELECT, new Object[]{whereParameter, from, size}, new RowMapper<Message>() {
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
