package org.smigo.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public class AuthenticatedUser extends org.springframework.security.core.userdetails.User implements User {
    private final int id;

    public AuthenticatedUser(int id, String username, String password, List<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
