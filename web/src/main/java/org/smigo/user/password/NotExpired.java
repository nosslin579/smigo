package org.smigo.user.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotExpired.Validator.class)
public @interface NotExpired {

    String message() default "Key expired";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<NotExpired, String> {
        private final Logger log = LoggerFactory.getLogger(getClass());

        @Autowired
        private PasswordHandler passwordHandler;

        public void initialize(NotExpired constraintAnnotation) {
        }

        public boolean isValid(String resetKey, ConstraintValidatorContext constraintValidatorContext) {
            final ResetKeyItem resetKeyItem = passwordHandler.getResetKeyMap().get(resetKey);
            log.info("Validating expire date. " + resetKeyItem);
            if (resetKeyItem == null) {
                return true;
            }
            final long expireDate = resetKeyItem.getCreateDate() + TimeUnit.HOURS.toMillis(24);
            return expireDate > System.currentTimeMillis();
        }
    }
}
