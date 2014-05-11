package org.smigo.constraints;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for verifying that two fields match. E.g. passwords
 *
 * @author Christian Nilsson
 */
public class MatchesValidator implements ConstraintValidator<Matches, Object> {
  private static final Logger log = LoggerFactory.getLogger(MatchesValidator.class);

  private String field;
  private String verifyField;

  public void initialize(Matches constraintAnnotation) {
    this.field = constraintAnnotation.field();
    this.verifyField = constraintAnnotation.verifyField();
  }

  public boolean isValid(Object value, ConstraintValidatorContext context) {

    try {
      String verifyFieldObj = BeanUtils.getProperty(value, verifyField);
      String fieldObj = BeanUtils.getProperty(value, field);
//      log.debug("isValid " + value + " field:" + fieldObj + " verifyfield:" + verifyFieldObj);
      if (fieldObj != null && verifyFieldObj != null && fieldObj.equals(verifyFieldObj))
        return true;
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("notmatching").addNode(verifyField)
        .addConstraintViolation();
      return false;
    } catch (Exception e) {
      log.error("Validating fieldmatches faild", e);
    }

    return false;
  }
}
