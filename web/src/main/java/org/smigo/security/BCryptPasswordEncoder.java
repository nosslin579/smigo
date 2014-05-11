package org.smigo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Encoder and validator for spring security using bcrypt.
 *
 * @author Christian Nilsson
 */
public class BCryptPasswordEncoder implements PasswordEncoder {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public BCryptPasswordEncoder() {
    log.debug("Creating BCryptPasswordEncoder");
  }

  public String encodePassword(String rawPass, Object salt) {
    // log.debug("Encoding password " + rawPass + " with salt " + salt);
    return BCrypt.hashpw(rawPass, BCrypt.gensalt());
  }

  public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
    // log.debug("Validating password {" + encPass + "} {" + rawPass +
    // "} with salt:" + salt);
    return BCrypt.checkpw(rawPass, encPass);
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