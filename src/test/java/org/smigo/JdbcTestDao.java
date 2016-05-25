package org.smigo;

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
