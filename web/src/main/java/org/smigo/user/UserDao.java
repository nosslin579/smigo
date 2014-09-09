package org.smigo.user;

import java.util.List;

public interface UserDao {
    int addUser(RegisterFormBean user, String encodedPassword, long signupTime, long decideTime);

    void addOpenId(int userId, String identityUrl);

    User getUserByUsername(String username);

    List<? extends User> getUsersByUsername(String username);

    User getUserById(int id);

    User getUserByEmail(String email);

    User getUserByOpenId(String identityUrl);

    void updateUser(User user);
}
