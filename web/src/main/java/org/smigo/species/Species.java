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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.authentication.UserMustBeAuthenticated;

import javax.validation.constraints.Size;

@UserMustBeAuthenticated
public class Species {
    private static final Logger log = LoggerFactory.getLogger(Species.class);

    private int id;

    private Family family = null;

    // If plant not is annual it is perennial
    private boolean annual = true;

    // If true instance is not a species but an object e.g. path, wall or house.
    private boolean item = false;

    @Size(min = 2, max = 100, message = "msg.minandmaxlength")
    private String scientificName;
    private String iconFileName;

    @JsonIgnore
    private int creator;

    public Species() {
    }

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

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
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

    @Override
    public String toString() {
        return "Species{" +
                "id=" + id +
                ", family=" + family +
                ", annual=" + annual +
                ", item=" + item +
                ", scientificName='" + scientificName + '\'' +
                ", iconFileName='" + iconFileName + '\'' +
                ", creator=" + creator +
                '}';
    }
}
