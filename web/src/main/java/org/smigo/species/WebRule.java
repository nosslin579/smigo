package org.smigo.species;

import kga.*;
import kga.rules.Rule;
import kga.rules.RuleCategory;
import kga.rules.RuleType;

public class WebRule implements Rule {


    private final int id;
    private final Species host;
    private final Object causer;
    private final String messageKey;
    private final String hintMessageKey;
    private final int yearsBackFrom;
    private final int yearsBackTo;
    private final Object parameterMessageObject;
    private final String causerType;
    private final int ruleCategory;

    public WebRule(int id, Species host, Object causer, String messageKey, String hintMessageKey, int yearsBackFrom, int yearsBackTo, Object parameterMessageObject, String causerType, int ruleCategory) {
        this.id = id;
        this.host = host;
        this.causer = causer;
        this.messageKey = messageKey;
        this.hintMessageKey = hintMessageKey;
        this.yearsBackFrom = yearsBackFrom;
        this.yearsBackTo = yearsBackTo;
        this.parameterMessageObject = parameterMessageObject;
        this.causerType = causerType;
        this.ruleCategory = ruleCategory;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public Species getHost() {
        return host;
    }

    @Override
    public Hint getHint(Plant plant, Garden garden) {
        return null;
    }

    public Object getCauser() {
        return causer;
    }

    public String getHintMessageKey() {
        return hintMessageKey;
    }

    public int getYearsBackFrom() {
        return yearsBackFrom;
    }

    public int getYearsBackTo() {
        return yearsBackTo;
    }

    public Object getParameterMessageObject() {
        return parameterMessageObject;
    }

    public String getCauserType() {
        return causerType;
    }

    public int getRuleCategory() {
        return ruleCategory;
    }

    public static Rule create(int ruleId, Species host, int ruletype, Species speciesRuleParam, Family familyRuleParam, int gap) {
        final RuleType rt = RuleType.valueOfId(ruletype);
        int yearsBackTo = gap == 0 ? rt.getYearsBackTo() : gap;
        Object causer = null;
        Object parameterMessageObject = null;
        String causerType = "";

        //todo this is a bit ugly
        if (speciesRuleParam != null && rt.getCategory() == RuleCategory.COMPANION_PLANTING) {
            causer = speciesRuleParam;
            parameterMessageObject = speciesRuleParam;
            causerType = "Species";
        } else if (familyRuleParam != null && rt.getCategory() == RuleCategory.CROP_ROTATION) {
            causer = familyRuleParam;
            parameterMessageObject = familyRuleParam;
            causerType = "Family";
        } else if (gap != 0 && rt.getCategory() == RuleCategory.SPECIES_REPETITION) {
            causer = host;
            parameterMessageObject = gap;
            causerType = "Species";
        } else {
            throw new IllegalArgumentException("Could not create rule. Rule id:" + ruleId);
        }

        return new WebRule(ruleId, host, causer, rt.getMessageKey(), rt.getHintMessageKey(), rt.getYearsBackFrom(), yearsBackTo, parameterMessageObject, causerType, rt.getCategory().getId());
    }
}
