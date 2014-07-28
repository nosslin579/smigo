package kga.rules;

import kga.errors.RuleException;

public abstract class AbstractRule implements Rule {

    private int id;
    private RuleType ruleType;
    private String hintMessageKey;

    protected AbstractRule(int id, RuleType ruleType, String hintMessageKey) {
        if (id == 0 || ruleType == null || hintMessageKey == null) {
            throw new RuleException("Illegal values on rule, id:" + id);
        }
        this.id = id;
        this.ruleType = ruleType;
        this.hintMessageKey = hintMessageKey;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public RuleType getRuleType() {
        return ruleType;
    }


    @Override
    public String getMessageKey() {
        return ruleType.getMessageKey();
    }

    @Override
    public String toString() {
        return "AbstractRule{" +
                "id=" + id +
                ", ruleType=" + ruleType +
                ", messageKey=" + hintMessageKey +
                '}';
    }

    public String getHintMessageKey() {
        return hintMessageKey;
    }
}
