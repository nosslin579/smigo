package org.smigo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptPasswordEncoder implements PasswordEncoder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public BCryptPasswordEncoder() {
        log.debug("Creating BCryptPasswordEncoder");
    }

    @Override
    public String encode(CharSequence rawPass) {
        return BCrypt.hashpw(rawPass.toString(), BCrypt.gensalt());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}