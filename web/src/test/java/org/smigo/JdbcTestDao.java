package org.smigo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;

public class JdbcTestDao implements TestDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void deleteUserConnection(long providerUserId) {
        String deleteRowSql = "DELETE FROM UserConnection WHERE providerUserId = ?";
        jdbcTemplate.update(deleteRowSql, new Object[]{providerUserId}, new int[]{Types.VARCHAR});
    }

    @Override
    public void removeEmail(String email) {
        String removeEmailSql = "UPDATE users SET email = NULL WHERE email = ?";
        jdbcTemplate.update(removeEmailSql, new Object[]{email}, new int[]{Types.VARCHAR});
    }

}
