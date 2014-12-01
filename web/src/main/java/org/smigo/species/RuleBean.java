package org.smigo.species;

import java.util.Collections;
import java.util.List;

public class RuleBean {

    private final int id;
    private final int host;
    private final int type;
    private final int param;
    private final List<Integer> impacts;

    public RuleBean(int id, int host, int type, int param, List<Integer> impacts) {
        this.id = id;
        this.host = host;
        this.type = type;
        this.param = param;
        this.impacts = impacts;
    }

    public int getId() {
        return id;
    }

    public int getHost() {
        return host;
    }

    public int getType() {
        return type;
    }

    public int getParam() {
        return param;
    }

    public List<Integer> getImpacts() {
        return impacts;
    }

    public static RuleBean create(int id, int host, int type, int causerSpecies, int causerFamily, int gap, List<Integer> impacts) {
        int param = 0;
        if (type == 0 || type == 4) {
            param = causerSpecies;
        } else if (type == 5 || type == 6) {
            param = causerFamily;
        } else if (type == 7) {
            param = gap;
        } else {
            throw new IllegalArgumentException("No rule with type" + type);
        }

        final List<Integer> impactsNotNull = impacts == null ? Collections.emptyList() : Collections.unmodifiableList(impacts);

        return new RuleBean(id, host, type, param, impactsNotNull);
    }
}
