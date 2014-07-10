package org.smigo.constraints;

import org.smigo.CurrentUser;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CurrentPassword.CurrentPasswordValidator.class)
@Documented
public @interface CurrentPassword {

    String message() default "invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {

        @Autowired
        private UserDao userDao;
        @Autowired
        private CurrentUser currentUser;
        @Autowired
        private PasswordEncoder passwordEncoder;


        public void initialize(CurrentPassword constraintAnnotation) {
        }

        public boolean isValid(String rawPassword, ConstraintValidatorContext constraintContext) {
            final String password = userDao.getUserById(currentUser.getId()).getPassword();
            //user who signed up via openid has empty string as password
            if (password.isEmpty()) {
                return rawPassword.isEmpty();
            }
            return passwordEncoder.matches(rawPassword, password);
        }

    }

}