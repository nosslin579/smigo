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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsernameUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = getUser(username);
        //Because security this will never grant more than user authority
        return new AuthenticatedUser(user.getId(), user.getUsername(), user.getPassword(), AuthenticatedUser.USER_AUTHORITY);
    }

    public User getUser(String username) {
        final List<User> byUsername = userDao.getUsersByUsername(username);
        if (!byUsername.isEmpty()) {
            return byUsername.get(0);
        }
        final List<User> byEmail = userDao.getUsersByEmail(username);
        if (!byEmail.isEmpty()) {
            return byEmail.get(0);
        }
        throw new UsernameNotFoundException("User not found:" + username);
    }
}