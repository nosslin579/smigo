package org.smigo.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for verifying that two fields match. E.g. passwords
 *
 * @author Christian Nilsson
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RuleValidator.class)
@Documented
public @interface ValidRule {

  String message() default "Rule not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}