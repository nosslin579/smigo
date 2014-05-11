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
@Constraint(validatedBy = CommonsMultipartFileMimeTypeValidator.class)
@Documented
public @interface CommonsMultipartFileMimeType {

  String message() default "Invalid mime type";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] mimeTypes();

}