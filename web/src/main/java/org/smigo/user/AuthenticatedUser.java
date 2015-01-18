package org.smigo.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;

import java.util.List;

public class AuthenticatedUser extends SocialUser implements User, SocialUserDetails {
    private final int id;

    public AuthenticatedUser(int id, String username, String password, List<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUserId() {
        return String.valueOf(id);
    }
}
