package org.smigo.user.humanevidence;

import org.smigo.config.Props;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ReCaptcha.CaptchaValidator.class)
@Documented
public @interface ReCaptcha {

    String message() default "msg.provenotrobot";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CaptchaValidator implements ConstraintValidator<ReCaptcha, String> {

        @Autowired
        private Props props;
        @Autowired
        private HumanEvidenceHandler humanEvidenceHandler;
        @Autowired
        private HttpServletRequest request;

        @Override
        public void initialize(ReCaptcha reCaptcha) {

        }

        @Override
        public boolean isValid(String reCaptcha, ConstraintValidatorContext constraintValidatorContext) {
            //Dev may bypass captcha by leaving it empty
            if (props.isDev() && reCaptcha != null && reCaptcha.isEmpty()) {
                return true;
            }
            return humanEvidenceHandler.isVerifiedHuman(request) || reCaptcha != null && !reCaptcha.isEmpty() && humanEvidenceHandler.verifyCaptchaChallenge(reCaptcha);
        }
    }
}