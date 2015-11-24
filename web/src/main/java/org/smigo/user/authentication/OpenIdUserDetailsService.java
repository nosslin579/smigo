package org.smigo.user.authentication;

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

import org.smigo.user.AuthenticatedUser;
import org.smigo.user.User;
import org.smigo.user.UserDao;
import org.smigo.user.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class OpenIdUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        final List<User> users = userDao.getUserDetails(token);
        if (users.isEmpty()) {
            final AuthenticatedUser createdUser = userHandler.createUser();
            final int userId = createdUser.getId();
            userDao.addOpenId(userId, token.getIdentityUrl());
            return userDao.getUserDetails(userId);
        }
        final User user = users.get(0);
        return new AuthenticatedUser(user.getId(), user.getUsername(), "");
    }
}
