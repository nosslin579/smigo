package org.smigo.user.springsocial;

import org.smigo.user.RegisterFormBean;
import org.smigo.user.UserHandler;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.util.Locale;

class AddUserConnectionSignUp implements ConnectionSignUp {

    public static final long NOT_SO_RANDOM_POINT_IN_TIME = 1411140042351l;

    private final UserHandler userHandler;

    public AddUserConnectionSignUp(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public String execute(Connection<?> connection) {
        final RegisterFormBean user = new RegisterFormBean();
        user.setUsername("user" + String.valueOf(System.currentTimeMillis() - NOT_SO_RANDOM_POINT_IN_TIME));
        user.setTermsOfService(false);
        final int id = userHandler.createUser(user, Locale.ENGLISH);
        return String.valueOf(id);
    }
}