package org.smigo.i18n;

import com.google.common.collect.Table;
import org.slf4j.LoggerFactory;
import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class UserAdaptiveMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatabaseResource databaseResource;
    @Autowired
    private HttpServletRequest request;

    private Table<Integer, String, String> translationTable;

    public UserAdaptiveMessageSource(int cacheSeconds) {
        super();
        log.debug("Creating " + this.getClass().getSimpleName());
        setCacheSeconds(cacheSeconds);
        setBasenames("messages", "classpath:messages");
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
    }

    @PostConstruct
    public void init() {
        translationTable = databaseResource.getTranslation();
    }

    @Override
    protected String getMessageInternal(String code, Object[] args, Locale locale) {
/*        final User userPrincipal = (User) request.getUserPrincipal();
        if (userPrincipal != null) {
        int userId = userPrincipal.getId();
        final String message = translationTable.get(userId, code);
        if (message != null) {
            return message;
        }
        } */
        return super.getMessageInternal(code, args, locale);
    }
}
