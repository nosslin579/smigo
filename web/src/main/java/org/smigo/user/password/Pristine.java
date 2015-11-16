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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = Pristine.Validator.class)
public @interface Pristine {
    String message() default "Key already used";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Pristine, String> {
        private final Logger log = LoggerFactory.getLogger(getClass());

        @Autowired
        private PasswordHandler passwordHandler;

        public void initialize(Pristine constraintAnnotation) {
        }

        public boolean isValid(String resetKey, ConstraintValidatorContext constraintValidatorContext) {
            final ResetKeyItem resetKeyItem = passwordHandler.getResetKeyMap().get(resetKey);
            log.info("Validating pristine. " + resetKeyItem);
            return resetKeyItem == null || resetKeyItem.isPristine();
        }

    }

}