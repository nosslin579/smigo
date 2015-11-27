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
import java.util.List;
import java.util.Map;

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

    @Override
    public QueryReport getUserReport() {
        String sql = "" +
                "SELECT" +
                "  users.username," +
                "  users.locale," +
                "  id," +
                "  decidetime," +
                "  users.createdate," +
                "  requests," +
                "  sessions," +
                "  p.plants AS plants," +
                "  p.yearfrom  AS fromyear," +
                "  p.yearto  AS toyear," +
                "  speciescreated " +
                "FROM users" +
                "  LEFT JOIN (SELECT" +
                "               user_id," +
                "               count(*)  AS plants," +
                "               min(year) AS yearfrom," +
                "               max(year) AS yearto" +
                "             FROM plants " +
                "             GROUP BY user_id) AS p ON p.user_id = users.id" +
                "  LEFT JOIN (SELECT" +
                "               username," +
                "               count(*)                  AS requests," +
                "               count(DISTINCT sessionid) AS sessions" +
                "             FROM visitlog" +
                "             GROUP BY username) AS r ON r.username = users.username" +
                "  LEFT JOIN (SELECT" +
                "               creator AS speciescreator," +
                "               count(creator) AS speciescreated" +
                "             FROM species" +
                "             GROUP BY creator) AS sc ON sc.speciescreator = users.id " +
                "WHERE current_timestamp() < dateadd('MONTH',1,createdate) " +
                "ORDER BY id DESC " +
                "LIMIT 200;";

        final List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, result);
    }

    @Override
    public QueryReport getReferrerReport() {
        String sql = "" +
                "SELECT" +
                "  referer,count(referer) " +
                "FROM visitlog " +
                "WHERE current_timestamp() < dateadd('MONTH',1,createdate)  AND REFERER != '' AND REFERER NOT LIKE '%smigo.%' AND REFERER NOT LIKE '%.ru/'" +
                "GROUP BY referer " +
                "ORDER BY count(referer) DESC " +
                "LIMIT 200;";

        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getSpeciesReport() {
        String sql = "SELECT " +
                "  species.id, " +
                "  u.username AS creator, " +
                "  group_concat(DISTINCT def.vernacular_name SEPARATOR ' ') AS name, " +
                "  group_concat(DISTINCT def.language SEPARATOR ' ') AS language,  " +
                "  group_concat(DISTINCT def.country SEPARATOR ' ') AS country, " +
                "  u.LOCALE " +
                "FROM species " +
                "  LEFT JOIN species_translation def ON def.species_id = species.id " +
                "  LEFT JOIN users u ON u.id = species.creator " +
                "GROUP BY species.id " +
                "ORDER BY id DESC " +
                "LIMIT 20; ";

        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getSpeciesTranslationReport() {
        String sql = "SELECT " +
                "  vernacular_name, " +
                "  count(DISTINCT species_id) AS repetition, " +
                "  group_concat(language)     AS language, " +
                "  group_concat(country)      AS country, " +
                "  group_concat(species_id)   AS species " +
                "FROM species_translation " +
                "GROUP BY vernacular_name " +
                "ORDER BY repetition DESC " +
                "LIMIT 20; ";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getActivityReport() {
        String sql = "SELECT * " +
                "FROM visitlog " +
                "WHERE current_timestamp() < dateadd('DAY', 8, CREATEDATE) AND httpstatus != 404 AND (sessionid != '' OR note != '') " +
                "ORDER BY createdate DESC " +
                "LIMIT 800;";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getLastActivity() {
        String sql = "SELECT username,requestedurl,locales,useragent,sessionage,createdate " +
                "FROM visitlog " +
                "WHERE current_timestamp() < dateadd('HOUR', 1, CREATEDATE) AND httpstatus != 404 " +
                "ORDER BY createdate DESC " +
                "LIMIT 200;";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);

    }
}
