package org.smigo.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.constraints.Matches;
import org.smigo.constraints.Username;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Locale;

/**
 * Jpa entity and validator for user
 *
 * @author Christian Nilsson
 */
@Matches(field = "password", verifyField = "passwordagain")
public class User {

  private static final Logger log = LoggerFactory.getLogger(User.class);

  private int id;

  @SafeHtml(whitelistType = WhiteListType.NONE)
  private String displayname = null;

  @Email
  private String email = null;

  @Username
  @Size(min = 5, max = 40)
  @Pattern(regexp = "\\w*")
  private String username = null;

  @NotNull
  @Size(min = 6, max = 56)
  private String password = null;

  private String passwordagain = null;

  private boolean publicGarden = true;

  @SafeHtml(whitelistType = WhiteListType.BASIC_WITH_IMAGES)
  private String about;

  private Locale locale;

  private Date registrationDate;

  // system fields
  private String authority;

  @AssertTrue
  private boolean termsofservice;

  public User() {
  }

  /**
   * Default constructor
   */
  public User(int id, String username, String displayname, String email, boolean publicGarden,
              String about, Locale locale, Date registrationDate, String password) {
    this.id = id;
    this.displayname = displayname;
    this.password = password;
    this.username = username;
    this.email = email;
    this.publicGarden = publicGarden;
    this.about = about;
    this.locale = locale;
    this.registrationDate = registrationDate;
    log.debug("Create user instance " + this.toString());
  }

  /**
   * Constructor for getting user
   */
  public User(int id, String username, String displayname, String email, boolean publicGarden,
              String about, Locale locale, Date registrationDate) {
    this(id, username, displayname, email, publicGarden, about, locale, registrationDate, "");
  }

  /**
   * Constructor for adding user
   */
  public User(String username, String displayname, String email, boolean publicGarden,
              String about, Locale locale) {
    this(0, username, displayname, email, publicGarden, about, locale, null);
  }

  /**
   * Constructor for updating public User(String displayname, String email,
   * boolean publicGarden, String location, String about, String locale) {
   * this(null, displayname, email, publicGarden, location, about, locale, 0);
   * }
   */

  public String getHashedPassword() {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public String toString() {
    return "User {" + id + "," + username + "," + email + "," + displayname + ","  + ", " + publicGarden + ", " + ", " + about + ", "
             + authority + ", " + registrationDate + ", " + locale + "}";
  }

  public String getDisplayname() {
    return displayname;
  }

  public void setDisplayname(String firstname) {
    this.displayname = firstname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isPublicGarden() {
    return publicGarden;
  }

  public void setPublicGarden(boolean publicGarden) {
    this.publicGarden = publicGarden;
  }

  public String getPasswordagain() {
    return passwordagain;
  }

  public void setPasswordagain(String passwordagain) {
    this.passwordagain = passwordagain;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public GrantedAuthority[] getAuthorities() {
    GrantedAuthority[] ret = {new GrantedAuthorityImpl(authority)};
    return ret;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registerDate) {
    this.registrationDate = registerDate;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  public boolean isTermsofservice() {
    return termsofservice;
  }

  public void setTermsofservice(boolean termsofservice) {
    this.termsofservice = termsofservice;
  }

  public boolean isAnonymous() {
    return id == 0 || "".equals(username);
  }
}
