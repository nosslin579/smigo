package kga.rules;

import kga.Garden;
import kga.Hint;
import kga.Plant;
import kga.Species;
import kga.errors.RuleException;

public abstract class AbstractRule implements Rule {

    private int id;
    private RuleType ruleType;
    private String hintMessageKey;
    private Species host;

    protected AbstractRule(int id, Species host, RuleType ruleType, String hintMessageKey) {
        if (id == 0 || host == null || ruleType == null || hintMessageKey == null) {
            throw new RuleException("Illegal values on rule, id:" + id);
        }
        this.id = id;
        this.host = host;
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
}
