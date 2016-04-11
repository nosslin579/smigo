package org.smigo.species;

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