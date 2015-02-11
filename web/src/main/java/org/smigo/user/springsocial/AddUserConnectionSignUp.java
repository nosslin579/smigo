package org.smigo.user.springsocial;

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

import org.smigo.user.RegisterFormBean;
import org.smigo.user.UserBean;
import org.smigo.user.UserHandler;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.util.Locale;

class AddUserConnectionSignUp implements ConnectionSignUp {

    public static final long NOT_SO_RANDOM_POINT_IN_TIME = 1411140042351l;

    private final UserHandler userHandler;

    public AddUserConnectionSignUp(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public String execute(Connection<?> connection) {
        final RegisterFormBean user = new RegisterFormBean();
        user.setUsername("user" + String.valueOf(System.currentTimeMillis() - NOT_SO_RANDOM_POINT_IN_TIME));
        user.setTermsOfService(false);
        final int id = userHandler.createUser(user, Locale.ENGLISH);

        final UserBean userBean = new UserBean();
        userBean.setEmail(connection.fetchUserProfile().getEmail());
        userBean.setDisplayName(connection.fetchUserProfile().getName());
        userBean.setLocale(Locale.ENGLISH);
        userHandler.updateUser(id, userBean);

        return String.valueOf(id);
    }
}
