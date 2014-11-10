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

/**
 * Family is the family of the species in the Linnaean taxonomy.
 *
 * @author Christian Nilsson
 */
public class Family implements Comparable<Family> {

    private final int id;
    /**
     * The scientific name in latin
     */
    private String name;

    /**
     * Creates a new family.
     *
     * @param id the unique identifier
     */
    public Family(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Family && ((Family) obj).getId() == id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getMessageKey() {
        return "family" + id;

    }

    @Override
    public String toString() {
        return "Family {" + id + "," + name + "}";
    }

    @Override
    public int compareTo(Family f) {
        return name.compareTo(f.name);
    }

    public static Family create(int id, String name) {
        return id == 0 ? null : new Family(id, name);
    }
}
