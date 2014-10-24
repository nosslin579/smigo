package org.smigo.user;

import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

enum Language {
    GERMAN(Locale.GERMAN),
    ENGLISH(Locale.ENGLISH),
    FRENCH(Locale.FRENCH),
    SWEDISH(new Locale("sv")),
    ROMANIAN(new Locale("ro")),
    USA(Locale.US),
    UK(Locale.UK),
    SPANISH(new Locale("es")),
    CZECH(new Locale("cs"));
    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public static Map<String, String> getTransalationMap() {
        SortedMap<String, String> ret = new TreeMap<String, String>();
        for (Language t : Language.values()) {
            ret.put(t.locale.toString(), StringUtils.capitalize(t.locale.getDisplayName(t.locale)));
        }
        return ret;
    }

    public String getName() {
        return StringUtils.capitalize(this.locale.getDisplayName(this.locale));
    }

    public Locale getLocale() {
        return this.locale;
    }

    static boolean contains(Locale l) {
        for (Language language : values()) {
            final Locale l1 = language.getLocale();
            if (l1.equals(l) || (l1.getLanguage().equals(l.getLanguage()) && l1.getCountry().isEmpty())) {
                return true;
            }
        }
        return false;
    }
}
