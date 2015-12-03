package org.smigo.user;

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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.Locale;

public class UserBean implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(UserBean.class);

    private int id;
    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String displayName = null;

    @Email
    private String email = null;

    private String username = null;

    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String about = null;

    private Locale locale = null;

    @AssertTrue
    private boolean termsOfService = false;

    public UserBean() {
    }

    public UserBean(int id, String username, String displayName, String email, String about, Locale locale, boolean termsOfService) {
        this.id = id;
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.about = about;
        this.locale = locale;
        this.termsOfService = termsOfService;
        log.debug("Create UserBean instance " + this.toString());
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", about='" + about + '\'' +
                ", locale=" + locale +
                ", termsOfService=" + termsOfService +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String firstname) {
        this.displayName = firstname;
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

    public boolean isTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(boolean termsOfService) {
        this.termsOfService = termsOfService;
    }

    public static UserBean create(User user) {
        return new UserBean(user.getId(), user.getUsername(), user.getDisplayName(), user.getEmail(), user.getAbout(), user.getLocale(), user.isTermsOfService());
    }
}
