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

import java.util.Locale;

public class LanguageTest {

    @Test
    public void testContains() throws Exception {
        for (Language language : Language.values()) {
            Assert.assertTrue(Language.contains(language.getLocale()));
        }
        Assert.assertTrue(Language.contains(Locale.CANADA));
        Assert.assertTrue(Language.contains(Locale.CANADA_FRENCH));
        Assert.assertFalse(Language.contains(Locale.ITALY));
        Assert.assertFalse(Language.contains(Locale.CHINESE));

    }
}
