package org.smigo.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.factories.RuleFactory;
import org.smigo.formbean.RuleFormModel;
import org.smigo.persitance.UserSession;
import kga.Species;
import kga.rules.Rule;
import kga.rules.RuleType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RuleValidator implements ConstraintValidator<ValidRule, RuleFormModel> {
  private static final Logger log = LoggerFactory.getLogger(RuleValidator.class);
  RuleFactory ruleFactory = new RuleFactory();

  private UserSession userSession;

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
      Species host = userSession.getSpecies(rule.getHost());
      Species causer = userSession.getSpecies(rule.getCauser());
      Rule newRule = ruleFactory.createRule(-1, rule.getType().getId(), host, causer, rule.getGap(), true, 0, rule.getCauserfamily(), null);
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

  @Autowired
  public void setUserSession(UserSession userSession) {
    this.userSession = userSession;
  }
}
