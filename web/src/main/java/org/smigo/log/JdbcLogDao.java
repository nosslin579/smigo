package org.smigo.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    public void log(LogBean req) {
        String sql = "INSERT INTO visitlog (httpstatus, username,requestedurl,locales,useragent,referer,sessionid,method,xforwardedfor,note,origin) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {
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
                req.getOrigin()};
        int[] types = new int[args.length];
        Arrays.fill(types, Types.VARCHAR);
        types[0] = Types.INTEGER;
        jdbcTemplate.update(sql, args, types);
    }
}
