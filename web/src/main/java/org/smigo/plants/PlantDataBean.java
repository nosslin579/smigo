package org.smigo.plants;

import java.io.Serializable;

class PlantDataBean implements PlantData, Comparable<PlantDataBean>, Serializable {
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
        return "Plant " + year + ", " + y + ", " + x + ", " + speciesId;
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
