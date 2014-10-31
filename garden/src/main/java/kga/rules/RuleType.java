package kga.rules;

public enum RuleType {
    GOODCOMPANION(RuleCategory.COMPANION_PLANTING, "rule.goodcompanion", "hint.goodcompanion", 0, 0),
    FIGHTDISEASE(RuleCategory.COMPANION_PLANTING, "rule.fightdisease", "hint.fightdisease", 0, 0),
    REPELPEST(RuleCategory.COMPANION_PLANTING, "rule.repelpest", "hint.repelpest", 0, 0),
    IMPROVESFLAVOR(RuleCategory.COMPANION_PLANTING, "rule.improvesflavor", "hint.improvesflavor", 0, 0),
    BADCOMPANION(RuleCategory.COMPANION_PLANTING, "rule.badcompanion", "hint.badcompanion", 0, 0),
    GOODCROPROTATION(RuleCategory.CROP_ROTATION, "rule.goodcroprotation", "hint.goodcroprotation", 1, 1),
    BADCROPROTATION(RuleCategory.CROP_ROTATION, "rule.badcroprotation", "hint.badcroprotation", 1, 1),
    SPECIESREPETITION(RuleCategory.SPECIES_REPETITION, "rule.speciesrepetition", "hint.speciesrepetition", 1, 4);

    private final RuleCategory category;
    private final String messageKey;
    private final String hintMessageKey;
    private final int yearsBackFrom;
    private final int yearsBackTo;

    RuleType(RuleCategory companionPlanting, String messageKey, String hintMessageKey, int yearsBackFrom, int yearsBackTo) {
        category = companionPlanting;
        this.messageKey = messageKey;
        this.hintMessageKey = hintMessageKey;
        this.yearsBackFrom = yearsBackFrom;
        this.yearsBackTo = yearsBackTo;
    }

    public RuleCategory getCategory() {
        return category;
    }

    public int getId() {
        return this.ordinal();
    }

    public String getMessageKey() {
        return messageKey;
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

    public static RuleType valueOfId(int id) {
        for (RuleType ruleType : values()) {
            if (ruleType.getId() == id)
                return ruleType;
        }
        throw new IllegalArgumentException("No such ruletype. Id:" + id);
    }
}