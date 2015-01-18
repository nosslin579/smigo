package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserSession userSession;
    @Autowired
    private UserDao userDao;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        log.info("Login successful, event:" + event + ", token:" + event.getSource().getClass().getSimpleName());

        final Authentication authentication = (Authentication) event.getSource();
        UserBean user = userDao.getUser(authentication.getName());
        userSession.setUser(user);
    }
}