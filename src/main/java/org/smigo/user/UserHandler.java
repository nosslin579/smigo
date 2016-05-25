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
import org.smigo.message.MessageHandler;
import org.smigo.plants.Plant;
import org.smigo.plants.PlantHandler;
import org.smigo.plants.PlantHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Component
public class UserHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private PlantHolder plantHolder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PlantHandler plantHandler;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private MailHandler mailHandler;

    public User createUser() {
        for (int tries = 0; tries < 5; tries++) {
            String username = "user" + (int) (Math.random() * 1000000);
            final List<User> users = userDao.getUsersByUsername(username);
            if (users.size() == 0) {
                final Locale locale = getLocale();
                return createUser(username, null, locale, false);
            }
        }
        throw new IllegalStateException("Tried 5 times and could not find a free username");
    }

    public User createUser(UserAdd user, Locale locale) {
        final String encoded = passwordEncoder.encode(user.getPassword());
        return createUser(user.getUsername(), encoded, locale, user.isTermsOfService());
    }

    private User createUser(String username, String password, Locale locale, boolean tos) {
        long decideTime = System.currentTimeMillis() - request.getSession().getCreationTime();

        User newUser = new User();
        newUser.setLocale(locale);
        newUser.setTermsOfService(tos);
        newUser.setDecideTime((int) decideTime);
        newUser.setEnabled(true);
        newUser.setPassword(password);
        newUser.setUsername(username);
        final int userId = userDao.addUser(newUser);
        newUser.setId(userId);

        //save plants
        List<Plant> plants = plantHolder.getPlants();
        plantHandler.addPlants(plants, userId);

        messageHandler.addWelcomeNewsMessage(newUser, plants.size());
        return newUser;
    }

    public void acceptTermsOfService(AuthenticatedUser authenticatedUser) {
        User user = userDao.getUserById(authenticatedUser.getId());
        user.setTermsOfService(true);
        userDao.updateUser(user);
    }

    public UserPublic getUserPublicInfo(String username) {
        final List<User> user = userDao.getUsersByUsername(username);
        return user.isEmpty() ? null : new UserPublic(user.get(0));
    }

    public User getUser(AuthenticatedUser user) {
        if (user == null) {
            return null;
        }
        return userDao.getUserById(user.getId());
    }

    public AuthenticatedUser getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        return null;
    }

    public void updateUser(int userId, User update) {
        final User current = userDao.getUserById(userId);
        log.info("User updated account. Current:" + current + " Update:" + update);
        mailHandler.sendAdminNotification("user updated account", "Current:" + current + System.lineSeparator() + "Update:" + update);
        current.setAbout(update.getAbout());
        current.setDisplayName(update.getDisplayName());
        current.setEmail(update.getEmail());
        current.setLocale(update.getLocale());
        current.setUsername(update.getUsername());
        userDao.updateUser(current);
    }

    public Locale getLocale() {
        return localeResolver.resolveLocale(request);
    }
}
