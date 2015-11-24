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
    private UserSession userSession;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PlantHandler plantHandler;
    @Autowired
    private UserDao userDao;
    @Value("${baseUrl}")
    private String baseUrl;

    public AuthenticatedUser createUser() {
        for (int tries = 0; tries < 5; tries++) {
            String username = "user" + (int) (Math.random() * 1000000);
            final List<User> users = userDao.getUsersByUsername(username);
            if (users.size() == 0) {
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

    public void acceptTermsOfService(AuthenticatedUser authenticatedUser) {
        User user = userDao.getUserById(authenticatedUser.getId());
        user.setTermsOfService(true);
        userDao.updateUser(user);
    }

    public PublicInfoUserBean getUserPublicInfo(String username) {
        final List<User> user = userDao.getUsersByUsername(username);
        return user.isEmpty() ? null : new PublicInfoUserBean(user.get(0));
    }

    public UserBean getUser(AuthenticatedUser user) {
        if (user == null) {
            return null;
        }
        return UserBean.create(userDao.getUserById(user.getId()));
    }

    public AuthenticatedUser getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        return null;
    }

    public void updateUser(int userId, UserBean userBean) {
        final User user = userDao.getUserById(userId);
        user.setAbout(userBean.getAbout());
        user.setDisplayName(userBean.getDisplayName());
        user.setEmail(userBean.getEmail());
        user.setLocale(userBean.getLocale());
        userDao.updateUser(user);
    }
}
