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

/**
 * Family is the family of the species in the Linnaean taxonomy.
 *
 * @author Christian Nilsson
 */
public class Family implements Comparable<Family> {

    public static final int NEW_FAMILY = -1;
    private int id;
    /**
     * The scientific name in latin
     */
    private String name;

    /**
     * For json generated instance indicating existing family
     */
    public Family() {
    }

    /**
     * For json generated instance indicating existing family
     *
     * @param id id of existing family
     */
    public Family(int id) {
        this.id = id;
    }

    /**
     * For json generated instance indicating new family by setting id to -1
     *
     * @param name new name of the family
     */
    public Family(String name) {
        this.id = NEW_FAMILY;
        this.name = name;
    }

    public Family(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
