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
@Constraint(validatedBy = CommonsMultipartFileImageValidator.class)
@Documented
public @interface CommonsMultipartFileImage {

  String message() default "Image has wrong width/height. Allowed {width}/{height}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  long height();

  long width();

}