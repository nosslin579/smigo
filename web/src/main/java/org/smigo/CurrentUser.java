package org.smigo;

import org.smigo.entities.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CurrentUser {

    public boolean isAuthenticated() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    public Integer getId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public User getUser() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }


    public Locale getLocale() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLocale();
    }
}
