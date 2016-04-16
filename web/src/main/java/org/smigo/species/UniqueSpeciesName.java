package org.smigo.species;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 - 2016 Christian Nilsson
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

import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Locale;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueSpeciesName.UniqueSpeciesNameValidator.class)
@Documented
public @interface UniqueSpeciesName {

    String message() default "msg.speciesalreadyexists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UniqueSpeciesNameValidator implements ConstraintValidator<UniqueSpeciesName, String> {

        @Autowired
        private SpeciesHandler speciesHandler;
        @Autowired
        private UserHandler userHandler;

        public void initialize(UniqueSpeciesName constraintAnnotation) {
        }

        public boolean isValid(String vernacularName, ConstraintValidatorContext constraintContext) {
            AuthenticatedUser user = userHandler.getCurrentUser();
            Locale locale = userHandler.getUser(user).getLocale();
            Collection<String> speciesVernacularNameList = speciesHandler.getSpeciesTranslation(locale).values();
            return !speciesVernacularNameList.contains(vernacularName);
        }

    }
}