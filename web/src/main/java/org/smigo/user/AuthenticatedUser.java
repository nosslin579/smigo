package org.smigo.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class AuthenticatedUser extends org.springframework.security.core.userdetails.User implements User {
    private final int id;

    public AuthenticatedUser(int id, String username, String password) {
        super(username, password, Collections.singleton(new SimpleGrantedAuthority("user")));
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
