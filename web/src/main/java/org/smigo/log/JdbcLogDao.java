package org.smigo.log;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Arrays;

@Repository
class JdbcLogDao implements LogDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Async
    public void log(LogBean req) {
        String sql = "INSERT INTO visitlog (sessionage,httpstatus,username,requestedurl,locales,useragent,referer,sessionid,method,xforwardedfor,note,origin,host,querystring) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {
                req.getSessionAge(),
                req.getHttpStatus(),
                req.getRemoteUser(),
                req.getUrl(),
                req.getLocales(),
                req.getUseragent(),
                req.getReferer(),
                req.getSessionid(),
                req.getMethod(),
                req.getIp(),
                req.getNote(),
                req.getOrigin(),
                req.getHost(),
                req.getQueryString()};
        int[] types = new int[args.length];
        Arrays.fill(types, Types.VARCHAR);
        types[0] = Types.INTEGER;
        types[1] = Types.INTEGER;
        jdbcTemplate.update(sql, args, types);
    }
}
