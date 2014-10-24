package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.AssertTrue;
import java.util.Locale;

public class RegisterFormBean {

    private static final Logger log = LoggerFactory.getLogger(RegisterFormBean.class);

    @Username
    private String username = "";

    @NewPassword
    private String password = "";

    @AssertTrue
    private boolean termsOfService;

    private Locale locale;

    public RegisterFormBean() {
    }

    @Override
    public String toString() {
        return "RegisterFormBean{" +
                "username='" + username + '\'' +
                ", password='*******'" +
                ", termsOfService=" + termsOfService +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(boolean termsOfService) {
        this.termsOfService = termsOfService;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}