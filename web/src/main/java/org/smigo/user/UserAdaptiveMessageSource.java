package org.smigo.user;

import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.species.SpeciesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    public Map<Object, Object> getAllMessages(Locale locale) {
        long start = System.currentTimeMillis();
        clearCacheIncludingAncestors();
        PropertiesHolder propertiesHolder = getMergedProperties(locale);
        Map properties = propertiesHolder.getProperties();
        Map<Object, Object> ret = new HashMap<Object, Object>(properties);

        Collection<SpeciesView> species = speciesHandler.getSpeciesMap().values();
        for (SpeciesView s : species) {
            if (s.getVernacularName() != null) {
                ret.put(s.getMessageKey(), s.getVernacularName());
            }
        }
        log.debug("Get all messages took " + (System.currentTimeMillis() - start) + "ms");
        return ret;
    }
}
