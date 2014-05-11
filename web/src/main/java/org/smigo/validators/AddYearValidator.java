package org.smigo.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AddYearValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Integer.class.equals(clazz);
  }

  @Override
  public void validate(Object arg0, Errors e) {

  }

}
