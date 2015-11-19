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

import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserAdaptiveMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

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
        log.debug("Get all messages took " + (System.currentTimeMillis() - start) + "ms");
        return ret;
    }
}
