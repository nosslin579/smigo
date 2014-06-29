package org.smigo.i18n;

import com.google.common.collect.Table;
import org.slf4j.LoggerFactory;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public class UserAdaptiveMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserSession userSession;

    public UserAdaptiveMessageSource(int cacheSeconds) {
        super();
        log.debug("Creating " + this.getClass().getSimpleName());
        setCacheSeconds(cacheSeconds);
        setBasenames("messages", "classpath:messages");
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
    }

    @Override
    protected String getMessageInternal(String code, Object[] args, Locale locale) {
        final String message = userSession.getTranslation().get(code);
        if (message != null) {
            return message;
        }
        return super.getMessageInternal(code, args, locale);
    }
}
