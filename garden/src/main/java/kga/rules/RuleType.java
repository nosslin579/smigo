package kga.rules;

public enum RuleType {
  goodcompanion(BeneficialRule.class, RuleCategory.COMPANION_PLANTING),
  fightdisease(BeneficialRule.class, RuleCategory.COMPANION_PLANTING),
  repelpest(BeneficialRule.class, RuleCategory.COMPANION_PLANTING),
  improvesflavor(BeneficialRule.class, RuleCategory.COMPANION_PLANTING),
  badcompanion(DisadvantageousRule.class, RuleCategory.COMPANION_PLANTING),
  goodcroprotation(GoodCropRotationRule.class, RuleCategory.CROP_ROTATION),
  badcroprotation(BadCropRotationRule.class, RuleCategory.CROP_ROTATION),
  speciesrepetition(RepetitionRule.class, RuleCategory.CROP_ROTATION);
  private Class<? extends Rule> ruleClass;
  private RuleCategory category;

  private RuleType(Class<? extends Rule> ruleClass) {
    this.ruleClass = ruleClass;
  }

  private RuleType(Class<? extends Rule> ruleClass, RuleCategory category) {
    this.ruleClass = ruleClass;
    this.category = category;
  }

  public Class<? extends Rule> getRuleClass() {
    return ruleClass;
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

  public String getTranslationKey() {
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
