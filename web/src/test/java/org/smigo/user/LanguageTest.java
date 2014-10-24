package org.smigo.user;

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