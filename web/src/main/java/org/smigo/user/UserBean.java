package org.smigo.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.AssertTrue;
import java.util.Locale;

public class UserBean {

    private static final Logger log = LoggerFactory.getLogger(UserBean.class);

    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String displayname = null;

    @Email
    private String email = null;

    @Username
    private String username = null;

    @SafeHtml(whitelistType = WhiteListType.BASIC_WITH_IMAGES)
    private String about = null;

    private Locale locale = null;

    @AssertTrue
    private boolean termsofservice = true;

    public UserBean() {
    }


    public UserBean(String username, String displayname, String email, String about, Locale locale) {
        this.displayname = displayname;
        this.username = username;
        this.email = email;
        this.about = about;
        this.locale = locale;
        log.debug("Create user instance " + this.toString());
    }

    @Override
    public String toString() {
        return "User{" +
                ", displayname='" + displayname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
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

    public boolean isTermsofservice() {
        return termsofservice;
    }

    public void setTermsofservice(boolean termsofservice) {
        this.termsofservice = termsofservice;
    }
}
