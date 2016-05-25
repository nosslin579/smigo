package org.smigo.user.password;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.User;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

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
            List<User> users = userDao.getUsersByEmail(email);
            if (users.isEmpty()) {
                log.error("This can not happened. User with this email not found. Reset password not possible. " + resetKeyItem);
                return false;
            }
            return true;
        }

    }

}
