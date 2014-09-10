package org.smigo.user;

import org.smigo.config.VisitLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

class OpenIdUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    @Autowired
    private UserHandler userHandler;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        final UserDetails userDetails = userDao.getUserDetails(token);
        if (userDetails == null) {
            request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, "Created user from openid");
            final RegisterFormBean newUser = new RegisterFormBean();
            newUser.setUsername("user" + System.nanoTime());
            userHandler.createUser(newUser, token.getIdentityUrl());
            return userDao.getUserDetails(newUser.getUsername());
        }
        return userDetails;
    }
}
