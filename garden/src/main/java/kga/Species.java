/**
 * Kitchen garden aid is a planning tool for kitchengardeners.
 * Copyright (C) 2010 Christian Nilsson
 *
 * This file is part of Kitchen garden aid.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * Email contact christian1195@gmail.com
 */

package kga;

import kga.errors.RuleException;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This represent a species, not the physical plant.
 *
 * @author Christian Nilsson
 */
public class Species {
    private static final Logger log = LoggerFactory.getLogger(Species.class);

    private int id;

    private Set<Rule> rules = new HashSet<Rule>();

    private Family family = null;

    // If plant not is annual it is perennial
    private boolean annual = true;

    // If true instance is not a species but an object e.g. path, wall or house.
    private boolean item = false;

    public Species() {
    }

    public Species(int id, Set<Rule> rules, Family family, boolean annual, boolean item) {
        this.id = id;
        this.rules = rules;
        this.family = family;
        this.annual = annual;
        this.item = item;
    }

    public final Collection<Rule> getRules(Class<?>... ruleTypes) {
        if (ruleTypes == null || ruleTypes.length == 0)
            return rules;
        Collection<Rule> rulesToSort = new HashSet<Rule>(rules.size());
        for (Rule r : rules)
            for (Class<?> t : ruleTypes)
                if (t.isInstance(r))
                    rulesToSort.add(r);
        List<Rule> ret = new ArrayList<Rule>(rulesToSort);
        Collections.sort(ret);
        return ret;
    }

    public final Collection<Rule> getRules() {
        return rules;
    }

    public Collection<Rule> getCropRotationRules() {
        return getRules(Rule.CROP_ROTATION_RULES);
    }

    public Collection<Rule> getCompanionPlantingRules() {
        return getRules(Rule.COMPANION_PLANTING_RULES);
    }

    public Rule addRule(Rule r) {
        if (r == null) throw new RuleException("Rule may not be null");
        if (r.getHost() != this)
            throw new RuleException("Host must equals this. This:" + id + " Host:" + r.getHost().getId());
        rules.add(r);
        return r;
    }

    public boolean isRecurrent() {
        return !annual || isItem();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public boolean isAnnual() {
        return annual;
    }

    public void setAnnual(boolean annual) {
        this.annual = annual;
    }

    public boolean isItem() {
        return item;
    }

    public void setItem(boolean item) {
        this.item = item;
    }

    public String getTranslation() {
        return "na";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Species)) return false;

        Species species = (Species) o;

        return id == species.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}