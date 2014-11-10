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

package org.smigo.species;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Species {
    private static final Logger log = LoggerFactory.getLogger(Species.class);

    private final int id;

    private Family family = null;

    // If plant not is annual it is perennial
    private boolean annual = true;

    // If true instance is not a species but an object e.g. path, wall or house.
    private boolean item = false;

    private String scientificName;
    private String iconFileName;


    public Species(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public String getIconFileName() {
        return iconFileName;
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

    public static Species create(int id, Family family, boolean annual, boolean item) {
        final Species ret = new Species(id);
        ret.setFamily(family);
        ret.setAnnual(annual);
        ret.setItem(item);
        return ret;
    }

    public String getMessageKey() {
        return "msg.species" + id;
    }
}