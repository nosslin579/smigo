package org.sourceforge.kga.rules;

import java.util.ArrayList;
import java.util.List;

public enum RuleCategory {
  COMPANION_PLANTING,
  CROP_ROTATION;

  public List<RuleType> getRuleTypes() {
    List<RuleType> list = new ArrayList<RuleType>();
    for (RuleType ruleType : RuleType.values()) {
      if (ruleType.getCategory().name().equals(name()))
        list.add(ruleType);
    }
    return list;
  }

  public boolean isType(int type) {
    for (RuleType ruleType : getRuleTypes()) {
      if (ruleType.ordinal() == type)
        return true;
    }
    return false;
  }
}
