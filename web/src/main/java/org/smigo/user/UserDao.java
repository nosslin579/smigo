package org.smigo.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import java.util.List;

public interface UserDao {
    int addUser(RegisterFormBean user, String encodedPassword, long signupTime, long decideTime);

    void addOpenId(int userId, String identityUrl);

    User getUserByUsername(String username);

    List<? extends User> getUsersByUsername(String username);

    User getUserById(int id);

    User getUserByEmail(String email);

    UserDetails getUserDetails(OpenIDAuthenticationToken openIDAuthenticationToken);

    void updateUser(User user);

    UserDetails getUserDetails(String username);
}
