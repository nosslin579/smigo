package org.smigo.species;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

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
