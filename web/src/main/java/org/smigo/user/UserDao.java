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

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import java.util.List;

public interface UserDao {
    int addUser(RegisterFormBean user, String encodedPassword, long decideTime);

    void addOpenId(int userId, String identityUrl);

    List<User> getUsersByUsername(String username);

    User getUserById(int id);

    List<User> getUsersByEmail(String email);

    List<User> getUsersByOpenIDAuthenticationToken(OpenIDAuthenticationToken token);

    void updateUser(int id, UserBean user);

    UserBean getUser(String name);

    void deleteOpenId(String openIdUrl);

    void updatePassword(int userId, String encodedPassword);

    UserDetails getUserDetails(int userId);
}
