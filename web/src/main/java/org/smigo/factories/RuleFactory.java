package org.smigo.factories;

import kga.Family;
import kga.Species;
import kga.errors.RuleException;
import kga.rules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.User;

public class RuleFactory {
    private static final Logger log = LoggerFactory.getLogger(RuleFactory.class);

    public Rule createRule(int ruleId, int type, Species host, Species causer, int gap,
                           boolean display, int creatorId, Family family, User user) {
        Rule ret = null;
        try {
            if (RuleType.goodcompanion.getId() == type)
                ret = new BeneficialRule(host, causer);
            else if (RuleType.badcompanion.getId() == type)
                ret = new DisadvantageousRule(host, causer);
            else if (RuleType.fightdisease.getId() == type)
                ret = new FightDiseaseRule(host, causer);
            else if (RuleType.repelpest.getId() == type)
                ret = new PestRepellentRule(host, causer);
            else if (RuleType.improvesflavor.getId() == type)
                ret = new ImproveFlavorRule(host, causer);
            else if (RuleType.goodcroprotation.getId() == type)
                ret = new GoodCropRotationRule(host, family);
            else if (RuleType.badcroprotation.getId() == type)
                ret = new BadCropRotationRule(host, family);
            else if (RuleType.speciesrepetition.getId() == type)
                ret = new RepetitionRule(host, gap);
            else
                throw new RuntimeException("No such type of rule:" + type);
            ret.setInfo("messageSource.getMessage(ret.getHintTranslationKey(), ret.getHintTranslationParams(), \"-\", user.getLocale())");
            ret.setDisplay(display);
            ret.setId(ruleId);
            ret.setCreatorId(creatorId);
        } catch (RuleException e) {
            // log.error("Couldnt create rule " + ruleId, e);
        }
        return ret;
    }
}
