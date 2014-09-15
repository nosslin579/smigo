package org.smigo.user;

import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.species.SpeciesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class UserAdaptiveMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpeciesDao speciesDao;

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
        List<SpeciesView> species = speciesDao.getSpecies();
        for (SpeciesView s : species) {
            if (s.getVernacularName() != null) {
                properties.setProperty(s.getMessageKey(), s.getVernacularName());
            }
        }
        return properties;
    }
}
