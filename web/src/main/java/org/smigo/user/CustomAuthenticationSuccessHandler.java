package org.smigo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Login successful, user: " + authentication.getName());
        final AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        UserBean user = userDao.getUser(principal.getUsername());
        userSession.setUser(user);

        if (authentication instanceof OpenIDAuthenticationToken || authentication instanceof RememberMeAuthenticationToken) {
            response.sendRedirect("");
        } else {
            String userAsJson = objectMapper.writeValueAsString(user);
            response.getWriter().append(userAsJson);
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
        }
    }
}