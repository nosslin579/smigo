package org.smigo.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//@Component("messageSource")
public class SmigoMessageSource implements MessageSource {
  private static final Logger log = LoggerFactory.getLogger(SmigoMessageSource.class);
  private final Properties defaultProperties = new Properties();
  private String prefix = "/WEB-INF/messages/messages";
  private String suffix = ".properties";
  private Map<Locale, Properties> propertiesMap = new HashMap<Locale, Properties>();
  private ServletContext servletContext;
  private Locale defaultLocale = Locale.ENGLISH;

  @Autowired
  public SmigoMessageSource(ServletContext servletContext) throws IOException {
    this.servletContext = servletContext;
    defaultProperties.load(getInputStream(null));
    for (Locale locale : getLocalesInOrder()) {
      log.debug("Loading " + locale);
      propertiesMap.put(locale, getLoadedProperties(locale));
    }
  }

  public static List<Locale> getLocalesInOrder() {
    List<Locale> ret = new ArrayList<Locale>();
    ret.add(Locale.ENGLISH);
    ret.add(new Locale("de"));
    ret.add(new Locale("fr"));
    ret.add(new Locale("sv"));
    ret.add(new Locale("ro"));
    ret.add(new Locale("en", "US"));
    ret.add(new Locale("en", "UK"));
    return ret;
  }

  public static Map<String, Locale> getLocales() {
    Map<String, Locale> ret = new HashMap<String, Locale>();
    for (Locale locale : getLocalesInOrder()) {
      ret.put(locale.toString(), locale);
    }
    return ret;
  }

  private Properties getLoadedProperties(Locale locale) {
    try {
      Properties propertiesForLocale = new Properties(getProperties(getParentLocale(locale)));
      propertiesForLocale.load(getInputStream(locale));
      return propertiesForLocale;
    } catch (IOException e) {
      throw new RuntimeException("Error loading locale " + locale, e);
    }
  }

  private Locale getParentLocale(Locale child) {
    log.debug("Getting parent for " + child);
    if (child == null || child.equals(defaultLocale))
      return null;
    if (!child.getVariant().isEmpty())
      return new Locale(child.getLanguage(), child.getCountry());
    if (!child.getCountry().isEmpty())
      return new Locale(child.getLanguage());
    return defaultLocale;
  }

  @Override
  public String getMessage(String key, Object[] objects, String fallback, Locale locale) {
    return getProperties(locale).getProperty(key, fallback);
  }

  private Properties getProperties(Locale locale) {
    log.debug("Getting properties for " + locale);
    if (locale == null)
      return new Properties();
    for (; ; ) {
      if (propertiesMap.containsKey(locale))
        return propertiesMap.get(locale);
      else
        getProperties(getParentLocale(locale));
    }
  }

  @Override
  public String getMessage(String key, Object[] objects, Locale locale) throws NoSuchMessageException {
    return getProperties(locale).getProperty(key, key);
  }

  @Override
  public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
    return "messageSourceResolvable";
  }

  private InputStream getInputStream(Locale locale) throws FileNotFoundException {
    return servletContext.getResourceAsStream(prefix + (locale == null ? "" : "_" + locale) + suffix);
  }

}
