package org.smigo.user.humanevidence;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
        private HumanEvidenceHandler humanEvidenceHandler;
        @Autowired
        private HttpServletRequest request;
        @Value("${devEnvironment}")
        private boolean devEnvironment;

        @Override
        public void initialize(ReCaptcha reCaptcha) {

        }

        @Override
        public boolean isValid(String reCaptcha, ConstraintValidatorContext constraintValidatorContext) {
            //Dev may bypass captcha by leaving it empty
            if (devEnvironment && reCaptcha != null && reCaptcha.isEmpty()) {
                return true;
            }
            return humanEvidenceHandler.isVerifiedHuman(request) || reCaptcha != null && !reCaptcha.isEmpty() && humanEvidenceHandler.verifyCaptchaChallenge(reCaptcha);
        }
    }
}
