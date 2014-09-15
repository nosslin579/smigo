package org.smigo.persitance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.*;

/**
 * Core database superclass that handles the most general database communication
 *
 * @author Christian Nilsson
 */
@Component("databaseResource")
public class DatabaseResource implements Serializable {
    private final Logger log = LoggerFactory.getLogger(DatabaseResource.class);

    @Autowired
    private DataSource datasource;

    private void writeImageToFile(String fileName, MultipartFile source) {
        log.debug("Writing image " + fileName);
        if (fileName == null || source == null || source.isEmpty())
            return;
        OutputStream destination = null;
        try {
            destination = new FileOutputStream(fileName);
            destination.write(source.getBytes());
        } catch (Exception e) {
            log.error("Could not write image " + fileName, e);
        } finally {
            try {
                destination.close();
            } catch (Exception e) {
            }
        }
    }

    DataSource getDatasource() {
        return datasource;
    }

    void close(Connection connection, Statement stmt) {
        close(connection, stmt, null);
    }

    void close(Connection connection, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
            log.warn("Could not close resultset", ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception ex) {
                log.warn("Could not close statment", ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception ex) {
                    log.warn("Could not close connection", ex);
                }
            }
        }
    }


    public void updatePassword(String username, String encodedPassword) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("UPDATE users SET password=? WHERE username=?");
            ps.setString(1, encodedPassword);
            ps.setString(2, username);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Password not updated. Username:" + username, e);
        } finally {
            close(con, ps);
        }

    }

}