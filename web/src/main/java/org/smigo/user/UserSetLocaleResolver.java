package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Locale resolver that gets locale from user setting
 *
 * @author Christian Nilsson
 */

@Component("localeResolver")
public class UserSetLocaleResolver implements LocaleResolver {
    private static final Logger log = LoggerFactory.getLogger(UserSetLocaleResolver.class);

    @Autowired
    private UserSession userSession;

    @Override
    public Locale resolveLocale(HttpServletRequest req) {
        UserBean user = userSession.getUser();
        if (user.getLocale() != null) {
            return user.getLocale();
        }
        final Set<String> availableLocales = Language.getTransalationMap().keySet();
        for (Locale locale : Collections.list(req.getLocales())) {
            if (Language.contains(locale)) {
                return locale;
            }
        }
        return req.getLocale() == null ? Locale.ENGLISH : req.getLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException();
    }
}
