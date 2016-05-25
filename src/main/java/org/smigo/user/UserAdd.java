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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.password.NewPassword;

import javax.validation.constraints.AssertTrue;
import java.util.Locale;

public class UserAdd {

    private static final Logger log = LoggerFactory.getLogger(UserAdd.class);

    @Username
    private String username = "";

    @NewPassword
    private String password = "";

    @AssertTrue
    private boolean termsOfService;

    private Locale locale = Locale.ENGLISH;

    public UserAdd() {
    }

    @Override
    public String toString() {
        return "RegisterFormBean{" +
                "username='" + username + '\'' +
                ", password='*******'" +
                ", termsOfService=" + termsOfService +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(boolean termsOfService) {
        this.termsOfService = termsOfService;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
