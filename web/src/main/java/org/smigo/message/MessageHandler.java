package org.smigo.message;

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

import org.smigo.user.AuthenticatedUser;
import org.smigo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class MessageHandler {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MessageDao messageDao;

    public List<Message> getMessages(Locale locale, Integer page, Integer size) {
        int from = page * size;
        final List<Message> messages = messageDao.getMessage(locale, from, size);
        messages.stream().forEach(m -> m.setText(getInterpolateMessage(m.getText())));
        return messages;
    }

    public int addMessage(Message message, AuthenticatedUser user) {
        return messageDao.addMessage(message, user);
    }

    @Async
    public void addWelcomeNewsMessage(User user, int plants) {
        if (plants == 0) {
            return;
        }
        final AddMessageBean message = new AddMessageBean();
        final String text = messageSource.getMessage("msg.welcomenewusermessage", new Object[]{user.getUsername()}, user.getLocale());
        message.setText(text);
        message.setSubmitterUserId(1);
        message.setLocale(user.getLocale().toLanguageTag());
        messageDao.addMessage(message);
    }

    private String getInterpolateMessage(String text) {
        StringBuilder ret = new StringBuilder();
        final String[] split = text.split(" ");
        final List<String> stringList = Arrays.asList(split);
        for (String wordInText : stringList) {
            if (wordInText.startsWith("@")) {
                String username = wordInText.replaceAll("@", "");
                ret.append("<a href='/wall/").append(username).append("/'>").append(wordInText).append("</a>");
            } else {
                ret.append(wordInText);
            }
            ret.append(" ");
        }
        return ret.toString().trim();
    }
}
