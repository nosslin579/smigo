package org.smigo.factories;

import kga.Family;
import kga.Species;
import kga.errors.RuleException;
import kga.rules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleFactory {
    private static final Logger log = LoggerFactory.getLogger(RuleFactory.class);

    public Rule createRule(int ruleId, int type, Species host, Species causer, int gap,
                           boolean display, int creatorId, Family family) {
        Rule ret = null;
        try {
            if (RuleType.goodcompanion.getId() == type)
                ret = new BeneficialRule(causer);
            else if (RuleType.badcompanion.getId() == type)
                ret = new DisadvantageousRule(causer);
            else if (RuleType.fightdisease.getId() == type)
                ret = new FightDiseaseRule(causer);
            else if (RuleType.repelpest.getId() == type)
                ret = new PestRepellentRule(causer);
            else if (RuleType.improvesflavor.getId() == type)
                ret = new ImproveFlavorRule(causer);
            else if (RuleType.goodcroprotation.getId() == type)
                ret = new GoodCropRotationRule(family);
            else if (RuleType.badcroprotation.getId() == type)
                ret = new BadCropRotationRule(family);
            else if (RuleType.speciesrepetition.getId() == type)
                ret = new RepetitionRule(host, gap);
            else
                throw new RuntimeException("No such type of rule:" + type);
            ret.setId(ruleId);
            ret.setCreatorId(creatorId);
        } catch (RuleException e) {
            // log.error("Couldnt create rule " + ruleId, e);
        }
        return ret;
    }
}
