package org.smigo.comment;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
class JdbcCommentDao implements CommentDao {
    private static final String SELECT = "SELECT\n" +
            "COMMENTS.ID,\n" +
            "COMMENTS.TEXT,\n" +
            "SU.USERNAME AS SUBMITTER,\n" +
            "COMMENTS.CREATEDATE,\n" +
            "COMMENTS.YEAR,\n" +
            "COMMENTS.UNREAD\n" +
            "FROM COMMENTS\n" +
            "JOIN USERS SU ON SU.ID = COMMENTS.SUBMITTER_USER_ID\n" +
            "JOIN USERS RU ON RU.ID = COMMENTS.RECEIVER_USER_ID\n" +
            "WHERE RU.USERNAME = ?\n" +
            "ORDER BY CREATEDATE ASC;";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("COMMENTS").usingGeneratedKeyColumns("ID").usingColumns("TEXT", "SUBMITTER_USER_ID", "RECEIVER_USER_ID", "YEAR");
    }

    @Override
    public List<Comment> getComments(String receiver) {
        return jdbcTemplate.query(SELECT, new Object[]{receiver}, new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Comment(rs.getInt("ID"), rs.getString("TEXT"), rs.getString("SUBMITTER"), rs.getInt("YEAR"), rs.getDate("CREATEDATE"), rs.getBoolean("UNREAD"));
            }
        });
    }

    @Override
    public int addComment(Comment message, int submitter, int receiver) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("TEXT", message.getText());
        parameterSource.addValue("SUBMITTER_USER_ID", submitter);
        parameterSource.addValue("RECEIVER_USER_ID", receiver);
        parameterSource.addValue("YEAR", message.getYear());
        return insert.executeAndReturnKey(parameterSource).intValue();
    }

    @Override
    public void deleteComment(int id) {
        String sql = "DELETE FROM COMMENTS WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(Comment comment) {
        String sql = "UPDATE COMMENTS SET UNREAD = ? WHERE ID = ?;";
        jdbcTemplate.update(sql, comment.isUnread(), comment.getId());

    }
}
