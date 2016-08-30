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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
class JdbcLogDao implements LogDao {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    @Value("${sqlDirectory}")
    private String sqlDirectory;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void log(Log req) {
        String sql = "INSERT INTO visitlog (sessionage,httpstatus,username,requestedurl,locales,useragent,referer,sessionid,method,xforwardedfor,host,querystring) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
                "SELECT users.username,users.locale,id,decidetime,users.createdate,p.plants AS plants,p.years,speciescreated,firstactive,lastactive\n" +
                "FROM (SELECT username,MAX(CREATEDATE) AS lastactive ,MIN(CREATEDATE) AS firstactive FROM visitlog WHERE USERNAME != '' AND current_timestamp() < dateadd('DAY',8,createdate) GROUP BY username) AS activeusers\n" +
                "  LEFT JOIN USERS ON activeusers.username = users.username\n" +
                "  LEFT JOIN (SELECT user_id,count(*) AS plants,group_concat(DISTINCT year) AS years FROM plants GROUP BY user_id) AS p ON p.user_id = users.id\n" +
                "  LEFT JOIN (SELECT creator AS speciescreator,count(creator) AS speciescreated FROM species GROUP BY creator) AS sc ON sc.speciescreator = users.id\n" +
                "ORDER BY createdate DESC\n" +
                "LIMIT 50;";

        final List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, result);
    }

    @Override
    public QueryReport getReferrerReport() {
        String sql = "SELECT REGEXP_REPLACE(REFERER,'(?<=.{100}).+','...'),COUNT(REFERER),GROUP_CONCAT(DISTINCT HOST) FROM VISITLOG \n" +
                "WHERE REFERER != '' AND REFERER NOT REGEXP '^http:.{2,5}smigo.org' AND current_timestamp() < dateadd('DAY',8,createdate) AND XFORWARDEDFOR IN (SELECT XFORWARDEDFOR FROM VISITLOG WHERE REQUESTEDURL = '/rest/plant' AND XFORWARDEDFOR != '' AND METHOD = 'POST' GROUP BY XFORWARDEDFOR)\n" +
                "GROUP BY REFERER ORDER BY COUNT(REFERER) DESC;";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getSpeciesReport() {
        String sql = "SELECT " +
                "  species.id, " +
                "  u.username AS creator, " +
                "  group_concat(DISTINCT def.vernacular_name SEPARATOR ' ') AS names, " +
                "  group_concat(DISTINCT def.language SEPARATOR ' ') AS language,  " +
                "  group_concat(DISTINCT def.country SEPARATOR ' ') AS country, " +
                "  u.LOCALE, " +
                "  species.createdate " +
                "FROM species " +
                "  LEFT JOIN VERNACULARS def ON def.species_id = species.id " +
                "  LEFT JOIN users u ON u.id = species.creator " +
                "GROUP BY species.id " +
                "ORDER BY id DESC " +
                "LIMIT 40; ";

        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public void backup() {
        final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String backupFile = sqlDirectory + "/dbbackup/file" + date + ".zip";
        String sqlFile = "BACKUP TO '" + backupFile + "';";
        log.info("Backup to file:" + backupFile);
        jdbcTemplate.execute(sqlFile);

        final String backupScript = sqlDirectory + "/dbbackup/script" + date + ".zip";
        String sqlScript = "SCRIPT TO '" + backupScript + "' COMPRESSION ZIP;";
        log.info("Backup to file:" + backupScript);
        jdbcTemplate.execute(sqlScript);
    }

    @PostConstruct
    public void upgrade() throws IOException {
        final String upgradeFile = sqlDirectory + "/upgrade.sql";
        final File file = new File(upgradeFile);
        if (file.exists()) {
            String sql = "RUNSCRIPT FROM '" + upgradeFile + "';";
            log.info("Upgrade from:" + upgradeFile);
            jdbcTemplate.execute(sql);
            FileUtils.writeStringToFile(file, "", false);
        }
    }

    @Override
    public QueryReport getVarietiesReport() {
        String sql = "SELECT * FROM VARIETIES ORDER BY ID DESC LIMIT 30;";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getUserAgentReport() {
        String sql = "SELECT\n" +
                "  GROUP_CONCAT(DISTINCT USERNAME) AS USER,\n" +
                "  REPLACE(VISITLOG.USERAGENT, 'Mozilla/5.0 '),\n" +
                "  COUNT(VISITLOG.USERAGENT) AS REQUESTS,\n" +
                "  COUNT(DISTINCT VISITLOG.SESSIONID) AS SESSIONS,\n" +
                "  MIN(CREATEDATE)\n" +
                "FROM VISITLOG\n" +
                "WHERE current_timestamp() < dateadd('DAY', 8, CREATEDATE) AND USERNAME IS NOT ''\n" +
                "GROUP BY USERAGENT, USERNAME\n" +
                "ORDER BY max(CREATEDATE) DESC\n" +
                "LIMIT 20;\n";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getUrlReport() {
        String sql = "SELECT METHOD,REQUESTEDURL,COUNT(REQUESTEDURL),group_concat(DISTINCT HTTPSTATUS) FROM VISITLOG\n" +
                "WHERE current_timestamp() < dateadd('DAY', 8, CREATEDATE) AND XFORWARDEDFOR IN (SELECT XFORWARDEDFOR FROM VISITLOG WHERE REQUESTEDURL = '/rest/plant' AND XFORWARDEDFOR != '' AND METHOD = 'POST' GROUP BY XFORWARDEDFOR)\n" +
                "GROUP BY REQUESTEDURL,METHOD ORDER BY COUNT(REQUESTEDURL) DESC LIMIT 40;";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getPopularNewVernaculars() {
        String sql = "SELECT\n" +
                "  PLANTS.SPECIES_ID,\n" +
                "  count(PLANTS.SPECIES_ID),\n" +
                "  group_concat(DISTINCT VERNACULAR_NAME) as name,\n" +
                "  group_concat(DISTINCT USER_ID) as users,\n" +
                "  concat(group_concat(DISTINCT LANGUAGE), '-', group_concat(DISTINCT COUNTRY)) as locale\n" +
                "FROM PLANTS\n" +
                "  LEFT JOIN VERNACULARS ON VERNACULARS.SPECIES_ID = PLANTS.SPECIES_ID\n" +
                "WHERE PLANTS.SPECIES_ID > 10000\n" +
                "GROUP BY PLANTS.SPECIES_ID\n" +
                "HAVING COUNT(DISTINCT VERNACULAR_NAME) <= 1\n" +
                "ORDER BY COUNT(DISTINCT USER_ID) DESC\n" +
                "LIMIT 10;";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }

    @Override
    public QueryReport getActivityReport() {
        String sql = "SELECT\n" +
                "  group_concat(DISTINCT USERNAME SEPARATOR ' ')                        AS User,\n" +
                "  bool_or(METHOD = 'POST' AND REQUESTEDURL = '/rest/user')             AS Reg,\n" +
                "  max(SESSIONAGE)                                                      AS MaxAge,\n" +
                "  datediff('MINUTE', min(CREATEDATE), MAX(CREATEDATE))                 AS DiffMin,\n" +
                "  count(XFORWARDEDFOR)                                                 AS Req,\n" +
                "  count(CASE REQUESTEDURL\n" +
                "        WHEN '/rest/plant' THEN 1 ELSE NULL END)                       AS Add,\n" +
                "  count(CASE METHOD\n" +
                "        WHEN 'DELETE' THEN 1 ELSE NULL END)                            AS Del,\n" +
                "  count(DISTINCT SESSIONID)                                            AS Ses,\n" +
                "  substring(group_concat(DISTINCT REFERER ORDER BY CREATEDATE), 0, 50) AS Referer,\n" +
                "  group_concat(DISTINCT LOCALES)                                       AS Locale,\n" +
                "  replace(group_concat(DISTINCT USERAGENT), 'Mozilla/5.0 ')            AS UserAgent\n" +
                "FROM VISITLOG\n" +
                "WHERE current_timestamp() < dateadd('DAY', 8, createdate) and USERAGENT not like '%compatible%'\n" +
                "GROUP BY XFORWARDEDFOR\n" +
                "HAVING count(DISTINCT REFERER) > 1\n" +
                "ORDER BY group_concat(DISTINCT USERNAME SEPARATOR ' ') DESC ,count(XFORWARDEDFOR) DESC\n" +
                "LIMIT 40";
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return new QueryReport(sql, maps);
    }
}