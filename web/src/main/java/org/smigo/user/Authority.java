package org.smigo.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Authority {

    public static final GrantedAuthority HUMAN = new SimpleGrantedAuthority("human");
    public static final GrantedAuthority USER = new SimpleGrantedAuthority("user");
}
