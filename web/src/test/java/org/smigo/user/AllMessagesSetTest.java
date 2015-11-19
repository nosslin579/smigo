package org.smigo.user;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AllMessagesSetTest {

    public static final List<Object> SV_EXCLUDES = Arrays.asList("msg.forum", "ok", "msg.front.head1");

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