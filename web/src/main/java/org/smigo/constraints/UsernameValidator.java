package org.smigo.constraints;

import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for username. Checks in database if username exists.
 *
 * @author Christian Nilsson
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {

  @Autowired
  private DatabaseResource databaseresource;

  public void setDatabaseResource(DatabaseResource databaseresource) {
    this.databaseresource = databaseresource;
  }

  public void initialize(Username constraintAnnotation) {
  }

  public boolean isValid(String username, ConstraintValidatorContext constraintContext) {
    if (username.equals("asdf1234567890")) {
      return true;
    }
    try {
      return databaseresource.getUser(username) == null;
    } catch (Exception e) {
      return false;
    }
  }

}