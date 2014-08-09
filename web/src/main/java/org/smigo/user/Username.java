package org.smigo.user;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for UsernameValidator
 *
 * @author Christian Nilsson
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = Username.UsernameValidator.class)
@Documented
@Size(min = 5, max = 40)
@Pattern(regexp = "^[\\w]+$")
public @interface Username {

    String message() default "msg.usernametaken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UsernameValidator implements ConstraintValidator<Username, String> {

        @Autowired
        private UserDao userDao;

        public void initialize(Username constraintAnnotation) {
        }

        public boolean isValid(String username, ConstraintValidatorContext constraintContext) {
            return userDao.getUsersByUsername(username).isEmpty();
        }

    }
}