package org.smigo.user.password;

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
import org.smigo.user.MailHandler;
import org.smigo.user.User;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
class PasswordHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String MAIL_SUBJECT = "password reset";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersistentTokenRepository tokenRepository;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MailHandler emailHandler;

    private final Map<String, ResetKeyItem> resetKeyMap = new ConcurrentHashMap<>();

    public Map<String, ResetKeyItem> getResetKeyMap() {
        return resetKeyMap;
    }

    void setPassword(ResetKeyPasswordFormBean resetFormBean) {
        String resetKey = resetFormBean.getResetKey();
        ResetKeyItem resetKeyItem = resetKeyMap.get(resetKey);
        String email = resetKeyItem.getEmail();
        List<User> users = userDao.getUsersByEmail(email);

        User user = users.get(0);
        String password = resetFormBean.getPassword();
        updatePassword(user.getId(), password);
        resetKeyItem.setPristine(false);
        log.info("Password successfully reset by: " + user);
        emailHandler.sendAdminNotification(MAIL_SUBJECT, "User successfully reset password. " + user.getUsername());
    }

    public void updatePassword(int userId, String newPassword) {
        final User user = userDao.getUserById(userId);
        final String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userDao.updateUser(user);
        tokenRepository.removeUserTokens(user.getUsername());
    }

    public void sendResetPasswordEmail(String emailAddress, String host) {
        log.info("Sending reset email to: " + emailAddress);
        log.info("Size of resetKeyMap:" + resetKeyMap.size());

        List<User> users = userDao.getUsersByEmail(emailAddress);
        if (users.isEmpty()) {
            final String text = "Can not reset password. No user with email " + emailAddress;
            emailHandler.sendAdminNotification(MAIL_SUBJECT, text);
            emailHandler.sendClientMessage(emailAddress, MAIL_SUBJECT, text);
        } else {
            final String id = UUID.randomUUID().toString().replaceAll("-", "");
            resetKeyMap.put(id, new ResetKeyItem(id, emailAddress));

            final String text = "Your username is " + users.get(0).getUsername() + ". Click link to reset password. http://" + host + "/reset-password/" + id;
            emailHandler.sendClientMessage(emailAddress, MAIL_SUBJECT, text);
        }
    }


}
