package org.smigo.user;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Size(min = 6)
@Constraint(validatedBy = NewPassword.NullValidator.class)
public @interface NewPassword {

    String message() default "invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NullValidator implements ConstraintValidator<NewPassword, Object> {

        public void initialize(NewPassword constraintAnnotation) {
        }

        public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
            return object != null;
        }

    }

}