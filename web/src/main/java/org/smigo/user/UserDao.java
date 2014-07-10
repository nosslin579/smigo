package org.smigo.user;

import org.smigo.entities.User;

import java.util.List;

public interface UserDao {
    void addUser(User user, long signupTime, long decideTime);

    void addOpenId(int userId, String identityUrl);

    User getUserByUsername(String username);

    List<User> getUsersByUsername(String username);

    User getUserById(int id);

    User getUserByEmail(String email);

    User getUserByOpenId(String identityUrl);
}
