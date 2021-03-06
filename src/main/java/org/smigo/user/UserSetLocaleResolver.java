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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Locale;

/**
 * Locale resolver that gets locale from user setting
 *
 * @author Christian Nilsson
 */

@Component("localeResolver")
public class UserSetLocaleResolver implements LocaleResolver {
    private static final Logger log = LoggerFactory.getLogger(UserSetLocaleResolver.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Locale resolveLocale(HttpServletRequest req) {
        final Principal userPrincipal = req.getUserPrincipal();
        if (userPrincipal != null) {
            User user = userDao.getUsersByUsername(userPrincipal.getName()).get(0);
            if (user.getLocale() != null) {
                return user.getLocale();
            }
        }

        final String subDomain = req.getServerName().split("\\.")[0];
        for (Language language : Language.values()) {
            if (language.getLocale().getLanguage().equals(subDomain)) {
                return new Locale(subDomain);
            }
        }

        return req.getLocale() == null ? Locale.ENGLISH : req.getLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException();
    }
}
