package org.smigo.factories;

import kga.Family;
import kga.Species;
import kga.rules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleFactory {
    private static final Logger log = LoggerFactory.getLogger(RuleFactory.class);

    public Rule createRule(int ruleId, int type, Species host, Species causer, int gap, Family family) {
        if (RuleType.goodcompanion.getId() == type)
            return new CompanionRule(ruleId, RuleType.goodcompanion, causer, "hint.goodcompanion");
        else if (RuleType.badcompanion.getId() == type)
            return new CompanionRule(ruleId, RuleType.badcompanion, causer, "hint.badcompanion");
        else if (RuleType.fightdisease.getId() == type)
            return new CompanionRule(ruleId, RuleType.fightdisease, causer, "hint.fightdisease");
        else if (RuleType.repelpest.getId() == type)
            return new CompanionRule(ruleId, RuleType.repelpest, causer, "hint.repelpest");
        else if (RuleType.improvesflavor.getId() == type)
            return new CompanionRule(ruleId, RuleType.improvesflavor, causer, "hint.improvesflavor");
        else if (RuleType.goodcroprotation.getId() == type)
            return new CropRotationRule(ruleId, RuleType.goodcroprotation, family, "hint.goodcroprotation");
        else if (RuleType.badcroprotation.getId() == type)
            return new CropRotationRule(ruleId, RuleType.badcroprotation, family, "hint.badcroprotation");
        else if (RuleType.speciesrepetition.getId() == type)
            return new RepetitionRule(ruleId, RuleType.speciesrepetition, gap, host, "hint.speciesrepetition");

        throw new RuntimeException("No such type of rule:" + type);
    }
}
