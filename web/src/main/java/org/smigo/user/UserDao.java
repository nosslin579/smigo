package org.smigo.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import java.util.List;

public interface UserDao {
    int addUser(RegisterFormBean user, String encodedPassword, long decideTime);

    void addOpenId(int userId, String identityUrl);

    List<? extends UserBean> getUsersByUsername(String username);

    UserBean getUserById(int id);

    List<UserDetails> getUserByEmail(String email);

    List<UserDetails> getUserDetails(OpenIDAuthenticationToken token);

    void updateUser(int id, UserBean user);

    List<UserDetails> getUserDetails(String username);

    UserBean getUser(String name);

    void deleteOpenId(String openIdUrl);

    void updatePassword(int userId, String encodedPassword);

    UserDetails getUserDetails(int userId);
}
