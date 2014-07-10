package org.smigo.config;

import org.smigo.entities.User;
import org.smigo.handler.UserHandler;
import org.smigo.listener.VisitLogger;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    @Autowired
    private UserHandler userHandler;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        final String identityUrl = token.getIdentityUrl();
        final User user = userDao.getUserByOpenId(identityUrl);
        if (user == null) {
            request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, "Created user from openid");
            final User newUser = new User();
            newUser.setUsername("user" + System.nanoTime());
            newUser.setLocale(request.getLocale());
            userHandler.createUser(newUser, identityUrl);
            return userDao.getUserByUsername(newUser.getUsername());
        }
        return user;
    }
}
