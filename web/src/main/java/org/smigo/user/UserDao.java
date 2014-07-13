package org.smigo.user;

import java.util.List;

public interface UserDao {
    int addUser(CurrentUser user, String encodedPassword, long signupTime, long decideTime);

    void addOpenId(int userId, String identityUrl);

    CurrentUser getUserByUsername(String username);

    List<? extends CurrentUser> getUsersByUsername(String username);

    CurrentUser getUserById(int id);

    CurrentUser getUserByEmail(String email);

    CurrentUser getUserByOpenId(String identityUrl);
}
