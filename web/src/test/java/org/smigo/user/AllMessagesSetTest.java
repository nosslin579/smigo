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
import java.util.Locale;
import java.util.Map;

@Test
public class AllMessagesSetTest {

    private static final Logger log = LoggerFactory.getLogger(AllMessagesSetTest.class);

    public static final List<Object> SV_EXCLUDES = Arrays.asList("ok", "msg.front.head1");
    public static final List<Object> DE_EXCLUDES = Arrays.asList("name", "msg.title.species", "msg.anemailhasbeensent", "msg.link", "msg.twittersharetext", "ok", "msg.changepasswordsuccess", "msg.front.head1");

    @Test
    public void testSvMessagesExists() throws Exception {
        boolean ok = true;
        final UserAdaptiveMessageSource messageSource = new UserAdaptiveMessageSource(-1);
        final Map<Object, Object> englishMessages = messageSource.getAllMessages(Locale.ROOT);
        final Map<Object, Object> swedishMessages = messageSource.getAllMessages(Language.SWEDISH.getLocale());
        final Map<Object, Object> germanMessages = messageSource.getAllMessages(Language.GERMAN.getLocale());
        for (Object key : englishMessages.keySet()) {
            String rootTranslation = englishMessages.get(key).toString();
            String svTranslation = swedishMessages.get(key).toString();
            String deTranslation = germanMessages.get(key).toString();
            if (!SV_EXCLUDES.contains(key) && rootTranslation.equals(svTranslation)) {
                System.out.println(key + "=sv_missing" + rootTranslation);
                ok = false;
            }
            if (!DE_EXCLUDES.contains(key) && rootTranslation.equals(deTranslation)) {
                System.out.println(key + "=de_missing" + rootTranslation + svTranslation);
                ok = false;
            }
        }
        Assert.assertTrue(ok);
    }
}