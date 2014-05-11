package org.smigo.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.entities.User;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Locale resolver that gets locale from user setting
 *
 * @author Christian Nilsson
 */

@Component("localeResolver")
public class UserSetLocaleResolver extends AbstractLocaleResolver {

  private static final Logger log = LoggerFactory.getLogger(UserSetLocaleResolver.class);

  @Autowired
  private UserSession userSession;

  @Override
  public Locale resolveLocale(HttpServletRequest req) {
    try {
      User u = userSession.getUser();
      return u.isAnonymous() ? req.getLocale() : u.getLocale();
    } catch (Exception e) {
      log.error("Resolve locale failed", e);
    }
    return req.getLocale();
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    log.debug("setLocale if this show up investigate");
  }

}
