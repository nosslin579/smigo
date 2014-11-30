package org.smigo.species;

import java.util.Collections;
import java.util.List;

public class RuleBean {

    private final int id;
    private final int host;
    private final int type;
    private final int param;
    private final List<Integer> tags;

    public RuleBean(int id, int host, int type, int param, List<Integer> tags) {
        this.id = id;
        this.host = host;
        this.type = type;
        this.param = param;
        this.tags = tags;
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

    public List<Integer> getTags() {
        return tags;
    }

    public static RuleBean create(int id, int host, int type, int causerSpecies, int causerFamily, int gap, List<Integer> tags) {
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

        final List<Integer> tagsNotNull = tags == null ? Collections.emptyList() : Collections.unmodifiableList(tags);

        return new RuleBean(id, host, type, param, tagsNotNull);
    }
}
