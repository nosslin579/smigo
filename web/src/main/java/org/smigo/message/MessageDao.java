package org.smigo.message;

import org.smigo.user.AuthenticatedUser;

import java.util.List;

public interface MessageDao {
    List<Message> getMessage(String location, int from, int size);

    int addMessage(Message message, AuthenticatedUser user);
}
