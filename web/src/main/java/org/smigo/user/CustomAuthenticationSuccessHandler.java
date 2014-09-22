package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserSession userSession;
    @Autowired
    private UserDao userDao;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Login successful, user:" + authentication.getName() + ", token:" + authentication.getClass().getSimpleName());
        final AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        UserBean user = userDao.getUser(principal.getUsername());
        userSession.setUser(user);

        if (authentication instanceof OpenIDAuthenticationToken || authentication instanceof RememberMeAuthenticationToken) {
            response.sendRedirect("");
        }
    }
}