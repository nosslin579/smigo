package org.smigo.species;

import kga.Species;
import kga.rules.Rule;
import kga.rules.RuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.factories.RuleFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for verifying that two fields match. E.g. passwords
 *
 * @author Christian Nilsson
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidRule.RuleValidator.class)
@Documented
public @interface ValidRule {

    String message() default "Rule not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class RuleValidator implements ConstraintValidator<ValidRule, RuleFormModel> {
        private static final Logger log = LoggerFactory.getLogger(RuleValidator.class);
        final RuleFactory ruleFactory = new RuleFactory();

        @Autowired
        private SpeciesHandler speciesHandler;


        @Override
        public void initialize(ValidRule validRule) {
        }

        public boolean isValid(RuleFormModel rule, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();
            try {
                if (rule.getHost() == -1 || rule.getType() == null) {
                    context.buildConstraintViolationWithTemplate("No ruletype selected").addConstraintViolation();
                    return false;
                }
                Species host = speciesHandler.getSpecies(rule.getHost());
                Species causer = speciesHandler.getSpecies(rule.getCauser());
                Rule newRule = ruleFactory.createRule(-1, rule.getType().getId(), host, causer, rule.getGap(), true, 0, rule.getCauserfamily());
                if (newRule == null) {
                    context.buildConstraintViolationWithTemplate("No species/family/gap for rule").addConstraintViolation();
                    return false;
                }
                if (host.getRules().contains(newRule)) {
                    context.buildConstraintViolationWithTemplate("Rule alread exists").addConstraintViolation();
                    return false;
                }

                if (!host.isAnnual() && rule.getType() == RuleType.speciesrepetition) {
                    context.buildConstraintViolationWithTemplate("Cant add repetition limit to perennial").addConstraintViolation();
                    return false;
                }
                if (host.isItem() || (causer != null && causer.isItem())) {
                    context.buildConstraintViolationWithTemplate("Cant apply rules to items").addConstraintViolation();
                    return false;
                }
            } catch (Exception e) {
                log.error("Validating rule failed", e);
                return false;
            }
            return true;
        }

    }

}