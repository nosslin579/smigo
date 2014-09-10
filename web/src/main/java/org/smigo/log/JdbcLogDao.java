package org.smigo.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
class JdbcLogDao implements LogDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void log(LogBean req) {
        String sql = "INSERT INTO visitlog (username,requestedurl,locales,useragent,referer,sessionid,method,xforwardedfor,note) VALUES (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                req.getRemoteUser(),
                req.getUrl(),
                req.getLocales(),
                req.getUseragent(),
                req.getReferer(),
                req.getSessionid(),
                req.getMethod(),
                req.getIp(),
                req.getNote());
    }
}
