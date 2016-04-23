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

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;

import java.util.Collections;

public class AuthenticatedUser extends SocialUser implements SocialUserDetails {
    public static final String USER_AUTHORITY = "user";
    public static final String MOD_AUTHORITY = "mod";

    private final int id;

    public AuthenticatedUser(int id, String username, String password, String authority) {
        super(username, password, Collections.singletonList(new SimpleGrantedAuthority(authority)));
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUserId() {
        return String.valueOf(id);
    }

    public boolean isModerator() {
        return getAuthorities().contains(new SimpleGrantedAuthority(MOD_AUTHORITY));
    }
}
