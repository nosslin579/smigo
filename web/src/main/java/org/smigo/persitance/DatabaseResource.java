package org.smigo.persitance;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import kga.Family;
import kga.PlantData;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.JspMessageFunctions;
import org.smigo.SpeciesView;
import org.smigo.config.HostEnvironmentInfo;
import org.smigo.entities.Message;
import org.smigo.entities.PlantDb;
import org.smigo.entities.User;
import org.smigo.factories.RuleFactory;
import org.smigo.formbean.RuleFormModel;
import org.smigo.formbean.SpeciesFormBean;
import org.smigo.security.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Principal;
import java.sql.*;
import java.util.*;

/**
 * Core database superclass that handles the most general database communication
 *
 * @author Christian Nilsson
 */
@Component("databaseResource")
public class DatabaseResource implements Serializable {
    private final Logger log = LoggerFactory.getLogger(DatabaseResource.class);
    private Map<Integer, Family> familiesCache = null;
    // private List<Locale> local1es = new ArrayList<Locale>(160);
    private RuleFactory ruleFactory = new RuleFactory();

    @Autowired
    private DataSource datasource;
    @Autowired
    private HostEnvironmentInfo hostEnvironmentInfo;

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
            } catch (IOException e) {
            }
        }
    }

    public DataSource getDatasource() {
        return datasource;
    }

    protected void close(Connection connection, Statement stmt) {
        close(connection, stmt, null);
    }

    protected void close(Connection connection, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception ex) {
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    public int addSpecies(SpeciesFormBean speciesView, int creatorId) {
        log.info("Adding " + speciesView + " for user:" + creatorId);
        Connection con = null;
        PreparedStatement insertSpecies = null;
        ResultSet rs = null;
        int newSpeciesId;
        try {
            con = getDatasource().getConnection();
            insertSpecies = con.prepareStatement("INSERT INTO species(name,item,annual,family,displaybydefault,creator,iconfilename) VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertSpecies.setString(1, speciesView.getScientificName());
            insertSpecies.setBoolean(2, false);
            insertSpecies.setBoolean(3, speciesView.isAnnual());
            insertSpecies.setInt(4, speciesView.getFamily().getId());
            insertSpecies.setBoolean(5, false);
            insertSpecies.setInt(6, creatorId);
            insertSpecies.setString(7, "defaulticon.png");
            insertSpecies.execute();
            rs = insertSpecies.getGeneratedKeys();
            rs.next();
            newSpeciesId = rs.getInt(1);
        } catch (Exception e) {
            log.error("Could not insert " + speciesView, e);
            throw new RuntimeException(e);
        } finally {
            close(con, insertSpecies, rs);
        }

        setSpeciesVisibility(newSpeciesId, creatorId, true);
        setTranslation(creatorId, JspMessageFunctions.species(newSpeciesId), speciesView.getVernacularName());
        return newSpeciesId;
    }

    public void updateSpeciesIcon(int speciesId, String iconFileName, CommonsMultipartFile icon) {
        Connection con = null;
        PreparedStatement setSpeciesIconFileName = null;
        try {
            con = getDatasource().getConnection();
            log.debug("Updating iconfilename in species");
            writeImageToFile(hostEnvironmentInfo.getPlantPicDirectory() + iconFileName, icon);
            setSpeciesIconFileName = con.prepareStatement("UPDATE species SET iconfilename = ? WHERE species_id = ?");
            setSpeciesIconFileName.setString(1, iconFileName);
            setSpeciesIconFileName.setInt(2, speciesId);
            setSpeciesIconFileName.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not update species icon " + speciesId, e);
        } finally {
            close(con, setSpeciesIconFileName);
        }
    }

    public Map<Integer, Family> getFamilies() {
        if (familiesCache != null)
            return familiesCache;
        familiesCache = new HashMap<Integer, Family>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("SELECT id, name FROM families");
            rs = ps.executeQuery();
            while (rs.next())
                familiesCache.put(rs.getInt(1), new Family(rs.getString(2), rs.getInt(1)));
            // } catch (Exception e) {
            // log.error("Couldnt get families from database", e);
        } catch (SQLException e) {
            log.error("Error getting families ", e);
        } finally {
            close(con, ps, rs);
        }
        return familiesCache;
    }

    public void deleteSpecies(User user, int speciesId) {
        Connection con = null;
        PreparedStatement deleteSpecies = null;
        PreparedStatement deleteUserSetting = null;
        PreparedStatement deleteRules = null;

        try {
            con = getDatasource().getConnection();

            deleteSpecies = con.prepareStatement("DELETE FROM species WHERE species_id=? AND creator=? AND creator IS NOT null");
            deleteSpecies.setInt(1, speciesId);
            deleteSpecies.setInt(2, user.getId());
            deleteSpecies.execute();

            deleteUserSetting = con.prepareStatement("DELETE FROM usersettingforspecies WHERE species=? AND user=?");
            deleteUserSetting.setInt(1, speciesId);
            deleteUserSetting.setInt(2, user.getId());
            deleteUserSetting.execute();

            deleteRules = con.prepareStatement("DELETE FROM rules WHERE host=? AND creator=?");
            deleteRules.setInt(1, speciesId);
            deleteRules.setInt(2, user.getId());
            deleteRules.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting species with id:" + speciesId + " and user:" + user, e);
        } finally {
            close(con, deleteRules);
            close(con, deleteUserSetting);
            close(con, deleteSpecies);
        }
    }

    public Map<Integer, SpeciesView> getSpecies(User user) {
        // Getting species
        HashMap<Integer, SpeciesView> ret = new HashMap<Integer, SpeciesView>(200);
        Connection con = null;
        PreparedStatement speciesPS = null;
        ResultSet speciesRS = null;
        PreparedStatement rulePS = null;
        ResultSet ruleRS = null;
        try {
            con = getDatasource().getConnection();
            speciesPS = con
                    .prepareStatement("SELECT species_id,item,annual,creator,species.name, translation,coalesce(usersettingforspecies.name,species.name) AS scientificname,coalesce(usersettingforspecies.display,species.displaybydefault) AS display,families.name AS familyname,families.id AS familyid, coalesce(usersettingforspecies.iconfilename,species.iconfilename) AS iconname FROM species LEFT JOIN usersettingforspecies ON species.species_id=usersettingforspecies.species AND usersettingforspecies.user=? LEFT JOIN families ON species.family=families.id WHERE (creator IS NULL OR creator = ?)");
            speciesPS.setInt(1, user.getId());
            speciesPS.setInt(2, user.getId());
            speciesRS = speciesPS.executeQuery();
            while (speciesRS.next()) {
                SpeciesView speciesView = new SpeciesView(speciesRS.getInt("species_id"),
                        speciesRS.getString("scientificname"),
                        speciesRS.getBoolean("item"), speciesRS.getBoolean("annual"),
                        new Family(speciesRS.getString("familyname"), speciesRS.getInt("familyid")));
                speciesView.setDisplay(speciesRS.getBoolean("display"));
                speciesView.setIconFileName(speciesRS.getString("iconname"));
                if (speciesRS.getInt("creator") == user.getId())
                    speciesView.setCreator(user);
                ret.put(speciesView.getId(), speciesView);
            }

            // Adding rules
            rulePS = con
                    .prepareStatement("SELECT rule_id, type, host, causer,gap,creator,causerfamily,coalesce(display,displaybydefault) AS display FROM rules LEFT JOIN usersettingforrules ON rules.rule_id = usersettingforrules.rule AND usersettingforrules.user = ? WHERE (creator IS NULL OR creator = ?)");
            rulePS.setInt(1, user.getId());
            rulePS.setInt(2, user.getId());
            ruleRS = rulePS.executeQuery();
            while (ruleRS.next()) {
                SpeciesView host = ret.get(ruleRS.getInt("host"));
                SpeciesView causer = ret.get(ruleRS.getInt("causer"));
                Family family = getFamilies().get(ruleRS.getInt("causerfamily"));

                Rule rule = ruleFactory.createRule(ruleRS.getInt("rule_id"),
                        ruleRS.getInt("type"), host, causer, ruleRS.getInt("gap"),
                        ruleRS.getBoolean("display"), ruleRS.getInt("creator"), family, user);
                if (rule != null && rule.isDisplay())
                    host.addRule(rule);
            }
        } catch (SQLException e) {
            log.error("Could not get all species from database.", e);
            throw new RuntimeException("Error getting species. " + e.getMessage(), e);
        } finally {
            close(con, speciesPS, speciesRS);
            close(con, rulePS, ruleRS);
        }

        int i = 0;
        for (SpeciesView s : ret.values())
            i += s.getRules().size();
        log.debug("Returning " + ret.values().size() + " species with total " + i + " rules");
        return ret;
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
            log.error("Could not insert/update species visibility", e);
            throw new RuntimeException("Could not insert/update species visibility", e);
        } finally {
            close(con, ps);
        }
    }

    public void setRulesVisibility(Integer ruleId, User user, boolean visible) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO usersettingforrules (rule,user,display) VALUES (?,?,?) ON DUPLICATE KEY UPDATE display=?");
            ps.setInt(1, ruleId);
            ps.setInt(2, user.getId());
            ps.setBoolean(3, visible);
            ps.setBoolean(4, visible);
            ps.execute();
        } catch (SQLException e) {
            log.error("Could not update rule visibility", e);
            throw new RuntimeException("Could not update rule visibility", e);
        } finally {
            close(con, ps);
        }
    }

    public void addRule(RuleFormModel rule, User user) {
        log.info("Adding " + rule + " for user:" + user);
        if (user.isAnonymous())
            throw new RuntimeException("Can not add rule when user is anonymous");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO rules(type, host, causer, gap, creator, causerfamily) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, rule.getType().getId());
            ps.setInt(2, rule.getHost());
            ps.setInt(3, rule.getCauser());
            ps.setInt(4, rule.getGap());
            ps.setInt(5, user.getId());
            ps.setInt(6, rule.getCauserfamily() == null ? 0 : rule.getCauserfamily().getId());
            ps.execute();
        } catch (Exception e) {
            log.error("Could not insert " + rule, e);
            throw new RuntimeException(e);
        } finally {
            close(con, ps, rs);
        }
    }

    public List<PlantData> getPlants(User user) {
        List<PlantData> ret = new ArrayList<PlantData>(1000);
        if (user.getId() == 0)
            return ret;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("SELECT * FROM plants WHERE fkuserid=?");
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            while (rs.next())
                ret.add(new PlantDb(rs.getInt("species"), rs.getInt("year"), rs.getInt("x"), rs
                        .getInt("y")));
            log.debug("Returning " + ret.size() + " plants for user " + user.getId());
        } catch (Exception e) {
            log.error("Could not get garden from database, " + user, e);
            throw new RuntimeException("Could not get plants " + user, e);
        } finally {
            close(con, ps, rs);
        }
        return ret;
    }

    /**
     * Updates plants to database. First deletes users plants from database and
     * then adds plants.
     *
     * @param plants
     * @throws Exception
     */
    public void updateGarden(User user, List<PlantDb> plants) {
        if (plants == null || plants.isEmpty())
            throw new RuntimeException("No plants to update");

        int userId = user.getId();

        Set<Integer> yearsToUpdate = new HashSet<Integer>();
        for (PlantDb p : plants)
            yearsToUpdate.add(p.getYear());

        log.debug("Updating plants for user: " + user + " plants.size() " + plants.size());

        StringBuffer deleteSqlString = new StringBuffer("DELETE FROM plants WHERE fkuserid='"
                + userId + "'");
        for (Integer y : yearsToUpdate)
            deleteSqlString.append(" AND year=" + y);
        deleteSqlString.append(';');

        StringBuffer insertSqlString = new StringBuffer(
                "INSERT INTO plants(fkuserid, species, year, x, y) VALUES ");
        for (PlantDb pl : plants)
            insertSqlString.append("('" + userId + "'," + pl.getSpeciesId() + ","
                    + pl.getYear() + "," + pl.getX() + "," + pl.getY() + "),");
        insertSqlString.setCharAt(insertSqlString.length() - 1, ';');

        Connection con = null;
        PreparedStatement delete = null;
        PreparedStatement insert = null;
        try {
            con = getDatasource().getConnection();
            delete = con.prepareStatement(deleteSqlString.toString());
            insert = con.prepareStatement(insertSqlString.toString());
            delete.execute();
            insert.execute();
        } catch (Exception e) {
            log.error("Could not update plants database.", e);
            throw new RuntimeException(e);
        } finally {
            close(con, delete);
            close(con, insert);
        }
    }

    public void deleteYear(User user, Integer deleteyear) {
        log.debug("Deleting year " + deleteyear + " for user: " + user);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("DELETE FROM plants WHERE fkuserid=? AND year=?");
            ps.setInt(1, user.getId());
            ps.setInt(2, deleteyear);
            ps.execute();
        } catch (SQLException e) {
            log.error("Cant delete year:" + deleteyear + " from user:" + user, e);
            throw new RuntimeException("Cant delete year:" + deleteyear + " from user:" + user, e);
        } finally {
            close(con, ps);
        }
    }

    public void addUser(User user, long signupTime, long decideTime) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO "
                    + "users(username,password,enabled,authority,email,displayname,regtime,about,locale,decidetime) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)");
            if (getUser(user.getUsername(), null) == null) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getHashedPassword());
                ps.setBoolean(3, true);
                ps.setString(4, "user");
                ps.setString(5, user.getEmail());
                ps.setString(6, user.getDisplayname());
                ps.setLong(7, signupTime);
                ps.setString(8, user.getAbout());
                ps.setString(9, user.getLocale().toString());
                ps.setLong(10, decideTime);
                ps.execute();
            }
        } catch (Exception e) {
            log.error("Could not add user to database.", e);
            throw e;
        } finally {
            close(con, ps);
        }
    }

    public void updateUserDetails(User user) {
        log.debug("Update user " + user);
        Connection con = null;
        PreparedStatement updateUser = null;
        try {
            con = getDatasource().getConnection();
            updateUser = con.prepareStatement("UPDATE users SET displayname=?, email=?, about=?, locale=? WHERE user_id=?");
            updateUser.setString(1, user.getDisplayname());
            updateUser.setString(2, user.getEmail());
            updateUser.setString(3, user.getAbout());
            updateUser.setString(4, user.getLocale().toString());
            updateUser.setInt(5, user.getId());
            updateUser.execute();
        } catch (Exception e) {
            log.error("Could not update user: " + user, e);
            throw new RuntimeException("Could not update userdetails", e);
        } finally {
            close(con, updateUser);
        }
    }

    public User getUser(String userName, Locale locale) {
        if (userName == null || userName.isEmpty())
            return new User("", "", "", false, "", locale);
        User ret = getUser(userName);
        return ret;
    }

    public User getUser(int userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String username = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("SELECT username FROM users WHERE user_id=?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next())
                username = rs.getString("username");
        } catch (Exception e) {
            log.error("Could not get user from database. Username: " + username, e);
            throw new RuntimeException(e);
        } finally {
            close(con, ps, rs);
        }
        return getUser(username);
    }

    public User getUser(String username) {
        log.debug("Getting user from database " + username);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDatasource().getConnection();
            if (username != null) {
                ps = con.prepareStatement("SELECT * FROM users WHERE username=?");
                ps.setString(1, username);
            }
            rs = ps.executeQuery();
            User ret = null;
            if (rs.next())
                ret = new User(rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("displayname"), rs.getString("email"),
                        rs.getBoolean("publicgarden"), rs.getString("about"),
                        StringUtils.parseLocaleString(rs.getString("locale")),
                        rs.getTimestamp("createdate"));
            return ret;
        } catch (Exception e) {
            log.error("Could not get user from database. Username: " + username, e);
            throw new RuntimeException(e);
        } finally {
            close(con, ps, rs);
        }
    }

    public void deleteUser(String username) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("DELETE FROM users WHERE username=?");
            ps.setString(1, username);
            ps.execute();
        } finally {
            close(con, ps);
        }
    }

    public void logVisit(HttpServletRequest req, String note) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO visitlog (username,requestedurl,locale,locales, localeport,servername,validsessionid,sessionexists,sessionidfromurl,sessionidfromcookie,useragent,sessionid,method,xforwardedfor,note) VALUES (?,?,?,?, ?,?,?,?, ?,?,?,?, ?,?,?)");
            StringBuffer locales = new StringBuffer();
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
            ps.setString(15, note);
            ps.execute();
        } catch (Exception e) {
            log.error("Could not insert logvisit into database.", e);
        } finally {
            close(con, ps);
        }
    }

    public void addMessage(String message, String column, int location, Principal principal)
            throws SQLException, IllegalAccessException {
        if (principal == null)
            throw new IllegalAccessException("Principal cannont be null");
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("INSERT INTO messages (message," + column
                    + ",postdate2,poster) " + "SELECT ? as message, ? as " + column
                    + ", ? as postdate2, user_id as poster FROM users " + "WHERE username = ?");
            ps.setString(1, message);
            ps.setInt(2, location);
            ps.setLong(3, System.currentTimeMillis());
            ps.setString(4, principal.getName());
            ps.executeUpdate();
        } finally {
            close(con, ps);
        }
    }

    /**
     * Returns messages.
     *
     * @param column   which column e.g. smigowall,species
     * @param location id of location e.g. species address or rule id
     * @return
     * @throws SQLException
     */
    public List<Message> getMessages(String column, int location) throws SQLException {
        List<Message> ret = new ArrayList<Message>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("SELECT message,poster,postdate,postdate2 FROM messages WHERE "
                    + column + " = ?");
            ps.setInt(1, location);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next())
                    ret.add(new Message(rs.getString("message"), column, location, rs
                            .getInt("poster"), rs.getTimestamp("postdate")));
                return ret;
            }
        } finally {
            close(con, ps);
        }
        throw new IllegalStateException("No ResultSet object returned");
    }

    /**
     * Admin use only
     */
    @Deprecated
    public void updateIconFileName() {
        Connection con = null;
        PreparedStatement ps = null;
        for (int i = 1; i < 160; i++) {
            try {
                con = getDatasource().getConnection();
                ps = con.prepareStatement("UPDATE species SET iconfilename=? WHERE species_id=?");
                ps.setString(1, i + ".png");
                ps.setInt(2, i);
                ps.execute();
            } catch (Exception e) {
                log.debug("Not updating " + i + " reason " + e.getMessage());
            } finally {
                close(con, ps);
            }
        }

    }

    public void updatePassword(User user) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("UPDATE users SET password=? WHERE user_id=?");
            ps.setString(1, user.getHashedPassword());
            ps.setInt(2, user.getId());
            ps.execute();
        } catch (Exception e) {
            log.error("Could not update password for user " + user, e);
            throw new RuntimeException("Password not updated", e);
        } finally {
            close(con, ps);
        }

    }

    public boolean validatePassword(User user, String plaintextPassword) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDatasource().getConnection();
            ps = con.prepareStatement("SELECT password FROM users WHERE user_id=?");
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            String encodedPassword = null;
            if (rs.next())
                encodedPassword = rs.getString("password");
            return new BCryptPasswordEncoder().matches(plaintextPassword, encodedPassword);
        } catch (Exception e) {
            log.error("Could not validate password for user " + user, e);
            throw new RuntimeException("Could not validate password", e);
        } finally {
            close(con, ps, rs);
        }
    }

    public void deleteRule(Integer ruleId, User user) {
        Connection con = null;
        PreparedStatement deleteRule = null;
        try {
            con = getDatasource().getConnection();
            deleteRule = con
                    .prepareStatement("DELETE FROM rules WHERE rule_id=? AND creator=? AND creator != 0");
            deleteRule.setInt(1, ruleId);
            deleteRule.setInt(2, user.getId());
            deleteRule.execute();
        } catch (SQLException e) {
            log.error("Error deleting rule with id:" + ruleId + " and user:" + user, e);
        } finally {
            close(con, deleteRule);
        }
    }

    public String getTranslation(int userId, String messageCode) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            con = getDatasource().getConnection();
            statement = con.prepareStatement("SELECT value FROM translation WHERE user=? AND code=?");
            statement.setInt(1, userId);
            statement.setString(2, messageCode);
            resultSet = statement.executeQuery();
            return resultSet.first() ? resultSet.getString("value") : null;
        } catch (SQLException e) {
            throw new IllegalStateException("Error retrieving message code. userid:" + userId + " code:" + messageCode, e);
        } finally {
            close(con, statement, resultSet);
        }
    }

    public Table<Integer, String, String> getTranslation() {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            con = getDatasource().getConnection();
            statement = con.prepareStatement("SELECT user,code,value FROM translation");
            resultSet = statement.executeQuery();
            Table<Integer, String, String> ret = HashBasedTable.create();
            while (resultSet.next()) {
                ret.put(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
            }
            return ret;
        } catch (SQLException e) {
            throw new IllegalStateException("Error retrieving table of message code.", e);
        } finally {
            close(con, statement, resultSet);
        }
    }

    public void setTranslation(int userId, String messageCode, String value) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = getDatasource().getConnection();
            statement = con.prepareStatement("INSERT INTO translation (code, user, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = ?;");
            statement.setString(1, messageCode);
            statement.setInt(2, userId);
            statement.setString(3, value);
            statement.setString(4, value);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error retrieving message code. userid:" + userId + " code:" + messageCode, e);
        } finally {
            close(con, statement);
        }
    }
}