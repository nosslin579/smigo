package org.smigo.user;

import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.species.SpeciesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

public class UserAdaptiveMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpeciesHandler speciesHandler;

    public UserAdaptiveMessageSource(int cacheSeconds) {
        super();
        log.debug("Creating " + this.getClass().getSimpleName());
        setCacheSeconds(cacheSeconds);
        setBasenames("messages", "classpath:messages");
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
    }

    public Properties getAllMessages(Locale locale) {
        clearCacheIncludingAncestors();
        PropertiesHolder propertiesHolder = getMergedProperties(locale);
        Properties properties = propertiesHolder.getProperties();
        Collection<SpeciesView> species = speciesHandler.getSpeciesMap().values();
        for (SpeciesView s : species) {
            if (s.getVernacularName() != null) {
                properties.setProperty(s.getMessageKey(), s.getVernacularName());
            }
        }
        return properties;
    }
}
