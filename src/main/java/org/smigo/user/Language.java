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

import org.springframework.util.StringUtils;

import java.util.*;

enum Language {
    ENGLISH(Locale.ENGLISH),
    SPANISH(new Locale("es")),
    GERMAN(Locale.GERMAN),
    FRENCH(Locale.FRENCH),
    SWEDISH(new Locale("sv")),
    ROMANIAN(new Locale("ro")),
    USA(Locale.US),
    UK(Locale.UK),
    PORTUGUESE(new Locale("pt")),
    DUTCH(new Locale("nl")),
    TURKISH(new Locale("tr")),
    GALICIAN(new Locale("gl")),
    ITALIAN(Locale.ITALIAN),
    CZECH(new Locale("cs")),
    CATALAN(new Locale("ca"));

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public static Map<String, String> getLanguagesForDisplay(Enumeration<Locale> locales, Locale additionalLocale) {
        SortedMap<String, String> ret = new TreeMap<>();
        for (Language t : Language.values()) {
            ret.put(t.locale.toString(), StringUtils.capitalize(t.locale.getDisplayName(t.locale)));
        }
        while (locales.hasMoreElements()) {
            Locale locale = locales.nextElement();
            ret.put(locale.toString(), StringUtils.capitalize(locale.getDisplayName(locale)));
        }
        ret.put(additionalLocale.toString(), StringUtils.capitalize(additionalLocale.getDisplayName(additionalLocale)));
        return ret;
    }

    public String getName() {
        return StringUtils.capitalize(this.locale.getDisplayName(this.locale));
    }

    public Locale getLocale() {
        return this.locale;
    }

}
