package org.smigo.controllers.mav;

import org.smigo.formbean.RuleFormModel;
import org.sourceforge.kga.rules.RuleCategory;
import org.springframework.web.servlet.ModelAndView;

public class AddRuleMAV extends ModelAndView {

  public AddRuleMAV(Integer id) {
    ModelAndView ret = new ModelAndView("ruleform", "rule", new RuleFormModel());
    ret.addObject("companionRuleTypes", RuleCategory.COMPANION_PLANTING.getRuleTypes());
    ret.addObject("cropRotationRuleTypes", RuleCategory.CROP_ROTATION.getRuleTypes());
    ret.addObject("speciesid", id);
  }

}
