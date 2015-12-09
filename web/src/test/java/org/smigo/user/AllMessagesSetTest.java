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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AllMessagesSetTest {

    public static final List<Object> SV_EXCLUDES = Arrays.asList("ok", "msg.front.head1");

    @Test
    public void testSvMessagesExists() throws Exception {
        final UserAdaptiveMessageSource messageSource = new UserAdaptiveMessageSource(-1);
        final Map<Object, Object> englishMessages = messageSource.getAllMessages(Locale.ENGLISH);
        final Map<Object, Object> swedishMessages = messageSource.getAllMessages(Language.SWEDISH.getLocale());
        for (Map.Entry svEntry : swedishMessages.entrySet()) {
            if (!SV_EXCLUDES.contains(svEntry.getKey())) {
                final Object enEntry = englishMessages.get(svEntry.getKey());
                Assert.assertNotEquals(svEntry.getValue(), enEntry, "Not translated:" + svEntry);
            }
        }
    }
}