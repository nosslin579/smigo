package org.smigo.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Locale;

public interface User extends UserDetails {
    int getId();

    String getUsername();

    String getEmail();

    String getPassword();

    Locale getLocale();

    boolean isAuthenticated();

    String getDisplayname();

    String getAbout();
}
