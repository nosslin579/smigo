package org.smigo.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommonsMultipartFileSizeValidator implements
  ConstraintValidator<CommonsMultipartFileSize, CommonsMultipartFile> {
  private static final Logger log = LoggerFactory.getLogger(CommonsMultipartFileSizeValidator.class);

  private long maxSize;

  public void initialize(CommonsMultipartFileSize constraintAnnotation) {
    maxSize = constraintAnnotation.maxSixe();
  }

  public boolean isValid(CommonsMultipartFile file, ConstraintValidatorContext constraintContext) {
    log.debug("Validating " + file);
    if (file.getSize() < (maxSize * 1024))
      return true;
    return false;
  }

}