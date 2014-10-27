package kga.rules;

import kga.Garden;
import kga.Hint;
import kga.Plant;
import kga.Species;
import kga.errors.RuleException;

public abstract class AbstractRule implements Rule {

    private final int id;
    private final RuleType ruleType;
    private final String hintMessageKey;
    private final Species host;
    private final Object hintParameter;

    protected AbstractRule(int id, Species host, RuleType ruleType, String hintMessageKey, Object hintParameter) {
        if (id == 0 || host == null || ruleType == null || hintMessageKey == null) {
            throw new RuleException("Illegal values on rule, id:" + id);
        }
        this.id = id;
        this.host = host;
        this.ruleType = ruleType;
        this.hintMessageKey = hintMessageKey;
        this.hintParameter = hintParameter;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getRuleType() {
        return ruleType.getId();
    }


    @Override
    public String getMessageKey() {
        return ruleType.getMessageKey();
    }

    @Override
    public Species getHost() {
        return host;
    }

    @Override
    public Hint getHint(Plant plant, Garden garden) {
        return null;
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

    public Object getHintParameter() {
        return hintParameter;
    }
}
