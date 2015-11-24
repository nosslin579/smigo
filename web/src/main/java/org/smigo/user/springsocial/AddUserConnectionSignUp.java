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

import org.smigo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class AddUserConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserDao userDao;

    @Override
    public String execute(Connection<?> connection) {
        final String email = connection.fetchUserProfile().getEmail();
        final User user = getUserCreateIfNotFound(email);

        final UserBean userBean = UserBean.create(user);
        userBean.setEmail(email);
        userBean.setDisplayName(connection.fetchUserProfile().getName());
        userHandler.updateUser(user.getId(), userBean);

        return String.valueOf(user.getId());
    }

    private User getUserCreateIfNotFound(String email) {
        final List<User> users = userDao.getUsersByEmail(email);
        if (users.isEmpty()) {
            final AuthenticatedUser user = userHandler.createUser();
            return userDao.getUserById(user.getId());
        }
        return users.iterator().next();
    }
}
