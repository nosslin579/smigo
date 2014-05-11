package org.smigo.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

//@Component("messageSource")
public class SmigoMessageSource2 implements MessageSource {
  private final Properties defaultProperties = new Properties();
  private final String prefix = "/WEB-INF/messages/messages";
  private final String suffix = ".properties";
  private final Map<Locale, Properties> propertiesMap = new HashMap<Locale, Properties>();
  private final ServletContext servletContext;

  @Autowired
  public SmigoMessageSource2(ServletContext servletContext) throws IOException {
    this.servletContext = servletContext;
    defaultProperties.load(servletContext.getResourceAsStream(prefix + suffix));
  }

  private Locale getParentLocale(Locale child) {
    if (!child.getVariant().isEmpty())
      return new Locale(child.getLanguage(), child.getCountry());
    if (!child.getCountry().isEmpty())
      return new Locale(child.getLanguage());
    return null;
  }

  @Override
  public String getMessage(String key, Object[] objects, String fallback, Locale locale) {
    return getProperties(locale).getProperty(key, fallback);
  }

  private Properties getProperties(Locale locale) {
    if (locale == null)
      return defaultProperties;
    Properties ret = propertiesMap.get(locale);
    return ret == null ? createAndCacheProperites(locale) : ret;
  }

  @Override
  public String getMessage(String key, Object[] objects, Locale locale) throws NoSuchMessageException {
    String asf = getProperties(locale).getProperty(key, key);
    return getProperties(locale).getProperty(key, key);
  }

  @Override
  public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
    return "messageSourceResolvable";
  }

  private InputStream getInputStream(Locale locale) throws FileNotFoundException {
    return servletContext.getResourceAsStream(prefix + "_" + locale + suffix);
  }

  private Properties createAndCacheProperites(Locale locale) {
    Properties ret = new Properties(getProperties(getParentLocale(locale)));
    try {
      ret.load(getInputStream(locale));
    } catch (Exception e) {
    }
    propertiesMap.put(locale, ret);
    return ret;
  }
}