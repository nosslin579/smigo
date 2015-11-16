package org.smigo.user.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@NotExpired
@Pristine
@Constraint(validatedBy = ValidResetKeyItem.Validator.class)
public @interface ValidResetKeyItem {

    String message() default "Invalid key";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidResetKeyItem, String> {
        private final Logger log = LoggerFactory.getLogger(getClass());

        @Autowired
        private PasswordHandler passwordHandler;
        @Autowired
        private UserDao userDao;

        public void initialize(ValidResetKeyItem constraintAnnotation) {
        }

        public boolean isValid(String resetKey, ConstraintValidatorContext constraintValidatorContext) {
            ResetKeyItem resetKeyItem = passwordHandler.getResetKeyMap().get(resetKey);

            if (resetKeyItem == null) {
                log.warn("No valid resetPasswordKey found. Reset password not possible. ");
                return false;
            }

            String email = resetKeyItem.getEmail();
            List<UserDetails> users = userDao.getUserByEmail(email);
            if (users.isEmpty()) {
                //Should be never persist resetKeyItem if no user found
                log.error("This can not happened. User with this email not found. Reset password not possible. " + resetKeyItem);
                return false;
            }
            return true;
        }

    }

}
