package org.smigo.user;

import org.smigo.log.VisitLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

class OpenIdUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    public static final long NOT_SO_RANDOM_POINT_IN_TIME = 1411140042351l;
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        final List<UserDetails> userDetails = userDao.getUserDetails(token);
        if (userDetails.isEmpty()) {
            request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, "createdUserFromOpenid");
            final RegisterFormBean newUser = new RegisterFormBean();
            newUser.setUsername("user" + String.valueOf(System.currentTimeMillis() - NOT_SO_RANDOM_POINT_IN_TIME));
            userHandler.createUser(newUser, token.getIdentityUrl());
            return userDao.getUserDetails(newUser.getUsername()).get(0);
        }
        return userDetails.get(0);
    }
}
