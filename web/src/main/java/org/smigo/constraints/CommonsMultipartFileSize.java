package org.smigo.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CommonsMultipartFileSizeValidator.class)
@Documented
public @interface CommonsMultipartFileSize {

  String message() default "File to big";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  // CaseMode value();
  long maxSixe();

}