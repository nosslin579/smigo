package org.smigo.user.authentication;

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


import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UserHeaderAndAuthenticatedUserMatch.UserHeaderAndAuthenticatedUserMatchValidator.class)
@Documented
public @interface UserHeaderAndAuthenticatedUserMatch {
    String message() default "Invalid user";

    int httpStatus() default HttpServletResponse.SC_FORBIDDEN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UserHeaderAndAuthenticatedUserMatchValidator implements ConstraintValidator<UserHeaderAndAuthenticatedUserMatch, Object> {

        @Autowired
        private MailHandler mailHandler;
        @Autowired
        private HttpServletRequest request;

        public void initialize(UserHeaderAndAuthenticatedUserMatch constraintAnnotation) {
        }

        public boolean isValid(Object o, ConstraintValidatorContext constraintContext) {
            final String remoteUser = request.getRemoteUser();
            final String headerUser = request.getHeader("SmigoUser");
            boolean clientServerMatch = Objects.equals(remoteUser, headerUser);
            if (!clientServerMatch) {
                mailHandler.sendAdminNotification("header SmigoUser mismatch", "RemoteUser:" + remoteUser + " Header:" + headerUser);
            }
            return clientServerMatch;
        }

    }
}
