package org.smigo.user;

import org.smigo.config.VisitLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

class CustomAuthenticationUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

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
            final UserBean newUser = new UserBean();
            newUser.setUsername("user" + System.nanoTime());
            newUser.setLocale(request.getLocale());
            userHandler.createUser(newUser, identityUrl);
            return userDao.getUserByUsername(newUser.getUsername());
        }
        return user;
    }
}
