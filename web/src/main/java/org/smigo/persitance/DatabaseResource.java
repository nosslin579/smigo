package org.smigo.persitance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.config.VisitLogger;
import org.smigo.species.RuleFormModel;
import org.smigo.species.SpeciesFormBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    public int addSpecies(SpeciesFormBean speciesFormBean, int creatorId) {
        log.info("Adding " + speciesFormBean + " for user:" + creatorId);
        Connection con = null;
        PreparedStatement insertSpecies = null;
        ResultSet rs = null;
        int newSpeciesId;
        try {
            con = getDatasource().getConnection();
            insertSpecies = con.prepareStatement("INSERT INTO species(name,item,annual,family,displaybydefault,creator,iconfilename) VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertSpecies.setString(1, speciesFormBean.getScientificName());
            insertSpecies.setBoolean(2, false);
            insertSpecies.setBoolean(3, speciesFormBean.isAnnual());
            insertSpecies.setInt(4, speciesFormBean.getFamily().getId());
            insertSpecies.setBoolean(5, false);
            insertSpecies.setInt(6, creatorId);
            insertSpecies.setString(7, "defaulticon.png");
            insertSpecies.execute();
            rs = insertSpecies.getGeneratedKeys();
            rs.next();
            newSpeciesId = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Could not insert " + speciesFormBean, e);
        } finally {
            close(con, insertSpecies, rs);
        }

        setSpeciesVisibility(newSpeciesId, creatorId, true);
        setTranslation(creatorId, newSpeciesId, speciesFormBean.getVernacularName());
        return newSpeciesId;
    }

    @Deprecated
    public void updateSpeciesIcon(int speciesId, String iconFileName, CommonsMultipartFile icon) {
        Connection con = null;
        PreparedStatement setSpeciesIconFileName = null;
        try {
            con = getDatasource().getConnection();
            log.debug("Updating iconfilename in species");
//            writeImageToFile(hostEnvironmentInfo.getPlantPicDirectory() + iconFileName, icon);
            setSpeciesIconFileName = con.prepareStatement("UPDATE species SET iconfilename = ? WHERE species_id = ?");
            setSpeciesIconFileName.setString(1, iconFileName);
            setSpeciesIconFileName.setInt(2, speciesId);
            setSpeciesIconFileName.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update species icon " + speciesId, e);
        } finally {
            close(con, setSpeciesIconFileName);
        }
    }

    public void deleteSpecies(int userId, int speciesId) {
        Connection con = null;
        PreparedStatement deleteSpecies = null;
        PreparedStatement deleteUserSetting = null;
        PreparedStatement deleteRules = null;

        try {
            con = getDatasource().getConnection();

            deleteSpecies = con.prepareStatement("DELETE FROM species WHERE species_id=? AND creator=? AND creator IS NOT null");
            deleteSpecies.setInt(1, speciesId);
            deleteSpecies.setInt(2, userId);
            deleteSpecies.execute();

            deleteUserSetting = con.prepareStatement("DELETE FROM usersettingforspecies WHERE species=? AND user=?");
            deleteUserSetting.setInt(1, speciesId);
            deleteUserSetting.setInt(2, userId);
            deleteUserSetting.execute();

            deleteRules = con.prepareStatement("DELETE FROM rules WHERE host=? AND creator=?");
            deleteRules.setInt(1, speciesId);
            deleteRules.setInt(2, userId);
            deleteRules.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting species with id:" + speciesId + " and user:" + userId, e);
        } finally {
            close(con, deleteRules);
            close(con, deleteUserSetting);
            close(con, deleteSpecies);
        }
    }


    public void setSpeciesVisibility(int speciesId, int userId, Boolean visible) {
        if (visible == null)
            return;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO usersettingforspecies (species,user,display) VALUES (?,?,?) ON DUPLICATE KEY UPDATE display=?");
            ps.setInt(1, speciesId);
            ps.setInt(2, userId);
            ps.setBoolean(3, visible);
            ps.setBoolean(4, visible);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not insert/update species visibility", e);
        } finally {
            close(con, ps);
        }
    }

    public void setRulesVisibility(Integer ruleId, int userId, boolean visible) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO usersettingforrules (rule,user,display) VALUES (?,?,?) ON DUPLICATE KEY UPDATE display=?");
            ps.setInt(1, ruleId);
            ps.setInt(2, userId);
            ps.setBoolean(3, visible);
            ps.setBoolean(4, visible);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update rule visibility", e);
        } finally {
            close(con, ps);
        }
    }

    public void addRule(RuleFormModel rule, int userId) {
        log.info("Adding " + rule + " for user:" + userId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO rules(type, host, causer, gap, creator, causerfamily) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, rule.getType().getId());
            ps.setInt(2, rule.getHost());
            ps.setInt(3, rule.getCauser());
            ps.setInt(4, rule.getGap());
            ps.setInt(5, userId);
            ps.setInt(6, rule.getCauserfamily() == null ? 0 : rule.getCauserfamily().getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not insert " + rule, e);
        } finally {
            close(con, ps);
        }
    }


    public void logVisit(HttpServletRequest req) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO visitlog (username,requestedurl,locale,locales, localeport,servername,validsessionid,sessionexists,sessionidfromurl,sessionidfromcookie,useragent,sessionid,method,xforwardedfor,note) VALUES (?,?,?,?, ?,?,?,?, ?,?,?,?, ?,?,?)");
            StringBuilder locales = new StringBuilder();
            for (Enumeration<Locale> l = req.getLocales(); l.hasMoreElements(); )
                locales.append(l.nextElement().toString() + " ");
            ps.setString(1, req.getRemoteUser());
            ps.setString(2, req.getRequestURL().toString());
            ps.setString(3, req.getLocale().toString());
            ps.setString(4, locales.toString());
            ps.setInt(5, req.getLocalPort());
            ps.setString(6, req.getServerName());
            ps.setBoolean(7, req.isRequestedSessionIdValid());
            ps.setBoolean(8, req.getSession(false) != null);
            ps.setBoolean(9, req.isRequestedSessionIdFromURL());
            ps.setBoolean(10, req.isRequestedSessionIdFromCookie());
            ps.setString(11, req.getHeader("user-agent"));// os, webreader
            ps.setString(12, req.getRequestedSessionId());
            ps.setString(13, req.getMethod());
            ps.setString(14, req.getHeader("x-forwarded-for"));
            ps.setString(15, (String) req.getAttribute(VisitLogger.NOTE_ATTRIBUTE));
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not insert logvisit into database.", e);
        } finally {
            close(con, ps);
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

    public void deleteRule(Integer ruleId, int userId) {
        Connection con = null;
        PreparedStatement deleteRule = null;
        try {
            con = getDatasource().getConnection();
            deleteRule = con
                    .prepareStatement("DELETE FROM rules WHERE rule_id=? AND creator=? AND creator != 0");
            deleteRule.setInt(1, ruleId);
            deleteRule.setInt(2, userId);
            deleteRule.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting rule with id:" + ruleId + " and user:" + userId, e);
        } finally {
            close(con, deleteRule);
        }
    }

    void setTranslation(int userId, int speciesId, String value) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = getDatasource().getConnection();
            statement = con.prepareStatement("INSERT INTO usersettingforspecies (species, user, translation) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE translation = ?");
            statement.setInt(1, speciesId);
            statement.setInt(2, userId);
            statement.setString(3, value);
            statement.setString(4, value);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Sql error, userid:" + userId + ", speciesId" + speciesId, e);
        } finally {
            close(con, statement);
        }
    }

    public Map<String, String> getTranslation(int userId) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            con = getDatasource().getConnection();
            statement = con.prepareStatement("SELECT species, translation value FROM usersettingforspecies WHERE user = ?");
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            Map<String, String> ret = new HashMap<String, String>();
            while (resultSet.next()) {
                ret.put(resultSet.getString(1), resultSet.getString(2));
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving table of message code. Userid:" + userId, e);
        } finally {
            close(con, statement, resultSet);
        }
    }
}