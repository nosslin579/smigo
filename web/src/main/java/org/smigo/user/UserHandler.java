package org.smigo.user;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.plants.PlantData;
import org.smigo.plants.PlantHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private UserSession userSession;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender javaMailSender;
    @Autowired
    private PersistentTokenRepository tokenRepository;
    @Autowired
    private PlantHandler plantHandler;
    @Autowired
    private UserDao userDao;
    @Value("${resetPasswordUrl}")
    private String resetPasswordUrl;

    private final Map<String, ResetKeyItem> resetKeyMap = new ConcurrentHashMap<String, ResetKeyItem>();

    public AuthenticatedUser createUser() {
        for (int tries = 0; tries < 5; tries++) {
            String username = "user" + (int) (Math.random() * 1000000);
            final List<UserDetails> userDetails = userDao.getUserDetails(username);
            if (userDetails.size() == 0) {
                final Locale locale = localeResolver.resolveLocale(request);
                final RegisterFormBean newUser = new RegisterFormBean();
                newUser.setUsername(username);
                final int id = createUser(newUser, locale);
                return new AuthenticatedUser(id, username, "");
            }
        }
        throw new IllegalStateException("Tried 5 times and could not find a free username");
    }

    public int createUser(RegisterFormBean user, Locale locale) {
        long decideTime = System.currentTimeMillis() - request.getSession().getCreationTime();
        final String rawPassword = user.getPassword();
        final String encoded = rawPassword.isEmpty() ? "" : passwordEncoder.encode(rawPassword);
        user.setLocale(locale);
        final int userId = userDao.addUser(user, encoded, decideTime);

        //save plants
        List<PlantData> plants = userSession.getPlants();
        plantHandler.addPlants(plants, userId);
        return userId;
    }

    public void updatePassword(AuthenticatedUser username, String newPassword) {
        final String encodedPassword = passwordEncoder.encode(newPassword);
        userDao.updatePassword(username.getId(), encodedPassword);
        tokenRepository.removeUserTokens(username.getUsername());
    }

    public void sendResetPasswordEmail(String email) {
        log.info("Sending reset email to: " + email);
        log.info("Size of resetKeyMap:" + resetKeyMap.size());
        final String id = UUID.randomUUID().toString();

        for (String s : resetKeyMap.keySet()) {
            ResetKeyItem resetKeyItem = resetKeyMap.get(s);
            if (resetKeyItem != null && email.equals(resetKeyItem.getEmail()) && resetKeyItem.isValid()) {
                log.warn("Multiple occurrence of email in resetPasswordMap" + resetKeyItem);
                resetKeyItem.invalidate();
            }
        }

        resetKeyMap.put(id, new ResetKeyItem(id, email));

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Smigo reset password");
        simpleMailMessage.setText("Click link to reset password. " + resetPasswordUrl + id);
        javaMailSender.send(simpleMailMessage);
    }

    public void acceptTermsOfService(AuthenticatedUser principal) {
        UserBean user = userDao.getUser(principal.getUsername());
        user.setTermsOfService(true);
        userDao.updateUser(principal.getId(), user);
    }

    public PublicInfoUserBean getUserPublicInfo(String username) {
        final UserBean user = userDao.getUser(username);
        return new PublicInfoUserBean(user);
    }

    public UserBean getUser(AuthenticatedUser user) {
        if (user == null) {
            return null;
        }
        return userDao.getUser(user.getUsername());
    }

    public AuthenticatedUser getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        return null;
    }

    public void updateUser(UserBean userBean, AuthenticatedUser user) {
        userSession.setUser(userBean);
        userDao.updateUser(user.getId(), userBean);
    }

    public void updateUser(int userId, UserBean userBean) {
        userDao.updateUser(userId, userBean);
    }

    public boolean setPassword(ResetKeyPasswordFormBean resetFormBean) {
        String resetKey = resetFormBean.getResetKey();
        ResetKeyItem resetKeyItem = resetKeyMap.get(resetKey);

        if (resetKeyItem == null) {
            log.warn("No valid resetPasswordKey found" + resetFormBean);
            return false;
        }

        if (!resetKeyItem.isValid()) {
            log.info("Reset key has expired." + resetFormBean);
            return false;
        }

        String email = resetKeyItem.getEmail();
        List<UserDetails> users = userDao.getUserByEmail(email);
        if (users.isEmpty()) {
            log.warn("No such email:" + email);
            return false;
        }

        AuthenticatedUser user = (AuthenticatedUser) users.get(0);
        String password = resetFormBean.getPassword();
        updatePassword(user, password);
        resetKeyItem.invalidate();
        return true;
    }
}
