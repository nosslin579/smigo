package org.smigo.plants;

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

import java.io.Serializable;

public class PlantDataBean implements PlantData, Comparable<PlantDataBean>, Serializable {
    private int x;
    private int y;
    private int year;
    private int speciesId;

    public PlantDataBean() {
    }

    public PlantDataBean(int speciesId, int year, int x, int y) {
        this.speciesId = speciesId;
        this.year = year;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{Plant:" + year + ", " + y + ", " + x + ", " + speciesId + "}";
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getYear() {
        return year;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    @Override
    public int compareTo(PlantDataBean p) {
        if (this.year != p.year)
            return this.year - p.year;
        if (this.y != p.y)
            return this.y - p.y;
        if (this.x != p.x)
            return this.x - p.x;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlantData)) return false;

        PlantData that = (PlantData) o;

        if (speciesId != that.getSpeciesId()) return false;
        if (x != that.getX()) return false;
        if (y != that.getY()) return false;
        if (year != that.getYear()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + year;
        result = 31 * result + speciesId;
        return result;
    }
}
