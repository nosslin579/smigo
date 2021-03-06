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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Test
public class AllMessagesSetTest {

    private static final Logger log = LoggerFactory.getLogger(AllMessagesSetTest.class);

    public static final List<Object> ALL_EXCLUDES = Arrays.asList("family7229", "family7230", "msg.concat.metadescription.accept-terms-of-service", "msg.concat.title.accept-terms-of-service", "msg.front.headvideo", "msg.addcomment");
    public static final List<Object> ES_EXCLUDES = Arrays.asList("family7204", "family7219", "family7218", "family7220", "family7227", "family7224", "family7226", "msg.title.species", "visible", "no", "error", "ok");
    public static final List<Object> SV_EXCLUDES = Arrays.asList("ok", "msg.front.head1");
    public static final List<Object> DE_EXCLUDES = Arrays.asList("name", "msg.title.species", "msg.anemailhasbeensent", "msg.link", "msg.twittersharetext", "ok", "msg.changepasswordsuccess", "msg.front.head1", "msg.clicktoselect");

    @Test
    public void testSvMessagesExists() throws Exception {
        StringBuilder ok = new StringBuilder();
        final UserAdaptiveMessageSource messageSource = new UserAdaptiveMessageSource(-1);
        final Map<Object, Object> englishMessages = messageSource.getAllMessages(Language.ENGLISH.getLocale());
        final Map<Object, Object> swedishMessages = messageSource.getAllMessages(Language.SWEDISH.getLocale());
        final Map<Object, Object> germanMessages = messageSource.getAllMessages(Language.GERMAN.getLocale());
        final Map<Object, Object> spanishMessages = messageSource.getAllMessages(Language.SPANISH.getLocale());
        for (Object key : englishMessages.keySet()) {
            if (ALL_EXCLUDES.contains(key)) {
                continue;
            }
            String enTranslation = englishMessages.get(key).toString();
            String svTranslation = swedishMessages.get(key).toString();
            String deTranslation = germanMessages.get(key).toString();
            String esTranslation = spanishMessages.get(key).toString();
            if (!SV_EXCLUDES.contains(key) && enTranslation.equals(svTranslation)) {
                ok.append(key).append("=sv_missing ").append(enTranslation);
            }
            if (!DE_EXCLUDES.contains(key) && enTranslation.equals(deTranslation)) {
                ok.append(key).append("=de_missing ").append(enTranslation);
            }
            if (!ES_EXCLUDES.contains(key) && enTranslation.equals(esTranslation)) {
                ok.append(key).append("=es_missing ").append(enTranslation);
            }
        }
        Assert.assertEquals(ok.toString(), "", "Translation missing");
    }
}