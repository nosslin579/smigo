package org.smigo.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class AuthenticatedUser extends User {
    private final int id;

    public AuthenticatedUser(int id, String username, String password) {
        super(username, password, Collections.singleton(new SimpleGrantedAuthority("user")));
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
