package org.smigo.persitance;

import org.smigo.SpeciesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
@Scope(value = "prototype")
@Lazy
public class SpeciesComparator implements java.util.Comparator<org.smigo.SpeciesView> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LocaleResolver resolver;
    private Locale locale;

    @PostConstruct
    public void init() {
        locale = resolver.resolveLocale(request);
    }

    @Override
    public int compare(SpeciesView o1, SpeciesView o2) {
        if (o1.isItem() != o2.isItem())
            return o1.isItem() ? 1 : -1;
        String translationKey1 = o1.getTranslationKey();
        String translationKey2 = o2.getTranslationKey();
        String name1 = messageSource.getMessage(translationKey1, null, locale);
        String name2 = messageSource.getMessage(translationKey2, null, locale);
        return name1.compareTo(name2);
    }
}
