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

import org.smigo.user.*;
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

    String message() default "msg.passwordincorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {

        @Autowired
        private UserDao userDao;
        @Autowired
        private UserHandler userHandler;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private MailHandler mailHandler;


        public void initialize(CurrentPassword constraintAnnotation) {
        }

        public boolean isValid(String rawPassword, ConstraintValidatorContext constraintContext) {
            AuthenticatedUser authenticatedUser = userHandler.getCurrentUser();
            User user = userDao.getUsersByUsername(authenticatedUser.getUsername()).get(0);
            final String password = user.getPassword();
            final boolean matches = passwordEncoder.matches(rawPassword, password);
            if (!matches) {
                mailHandler.sendAdminNotification("Password change failed", "User:" + authenticatedUser.getUsername() + " failed to enter correct current password when changing it.");
            }
            return matches;
        }

    }

}
