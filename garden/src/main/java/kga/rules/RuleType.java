package kga.rules;

public enum RuleType {
    goodcompanion(RuleCategory.COMPANION_PLANTING),
    fightdisease(RuleCategory.COMPANION_PLANTING),
    repelpest(RuleCategory.COMPANION_PLANTING),
    improvesflavor(RuleCategory.COMPANION_PLANTING),
    badcompanion(RuleCategory.COMPANION_PLANTING),
    goodcroprotation(RuleCategory.CROP_ROTATION),
    badcroprotation(RuleCategory.CROP_ROTATION),
    speciesrepetition(RuleCategory.CROP_ROTATION);

    private RuleCategory category;

    private RuleType(RuleCategory category) {
        this.category = category;
    }

    public RuleCategory getCategory() {
        return category;
    }

    public String getName() {
        return this.name();
    }

    public int getId() {
        return this.ordinal();
    }

    public String getMessageKey() {
        return "rule." + name();
    }


    public static RuleType valueOfId(String id) {
        for (RuleType ruleType : values()) {
            if (String.valueOf(ruleType.getId()).equals(id))
                return ruleType;
        }
        return null;
    }
}
