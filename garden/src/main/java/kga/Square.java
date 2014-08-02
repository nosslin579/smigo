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

import kga.rules.Rule;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;

/**
 * Square is responsible for adding/removing species and can supply hints.
 * Square feet gardening is divided into squares. In each square you can plant
 * one or several plants. This class represent one square and each instance
 * represent a physical square.
 *
 * @author Christian Nilsson
 */
public class Square {
    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(Garden.class.getName());
    private final Location location;
    private List<Plant> plants = new ArrayList<Plant>();

    public Square(Location location, List<Species> species) {
        this.location = new YearXY(location);
        for (Species s : species)
            addSpecies(s);
    }


    public Square(Location location) {
        this(location, new ArrayList<Species>());
    }

    public boolean addSpecies(Species s) {
        if (containsItem() || containsSpecies(s.getId()))
            return false;
        plants.add(new Plant(s, this));
        return true;
    }

    public void removeSpecies(Species s) {
        if (s != null && containsSpecies(s.getId())) {
            this.plants.remove(new Plant(s, this));
            this.plants.remove(null);
            log.fine("Removed species " + s + " at " + location);
        } else {
            removeSpecies();
            log.info("Remove all speces at " + location);
        }
    }

    public void removeSpecies() {
        plants = new ArrayList<Plant>();
    }

    public boolean isYear(int year) {
        return this.location.getYear() == year;
    }

    public boolean containsSpecies(int speciesId) {
        for (Plant plant : plants) {
            if (plant.getSpecies().getId() == speciesId) {
                return true;
            }
        }
        return false;
    }

    public boolean containsFamily(Family family) {
        for (Plant p : plants) {
            if (p.getSpecies().getFamily().equals(family)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public Collection<Species> getSpecies() {
        List<Species> ret = new ArrayList<Species>(plants.size());
        for (Plant p : plants)
            ret.add(p.getSpecies());
        return ret;
    }

    /**
     * Checks if square contain an item
     *
     * @return true if contains item
     */
    public boolean containsItem() {
        for (Plant p : plants)
            if (p.getSpecies().isItem())
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "Square{" +
                "location=" + location +
                ", plants=" + plants +
                '}';
    }

    @JsonIgnore
    public Collection<Rule> getRules() {
        Set<Rule> rules = new HashSet<Rule>();
        for (Plant p : plants)
            rules.addAll(p.getSpecies().getRules());
        return rules;
    }

    @JsonIgnore
    public List<Species> getPerennialSpecies() {
        List<Species> perennialSpecies = new ArrayList<Species>();
        for (Plant p : plants)
            if (p.getSpecies().isRecurrent())
                perennialSpecies.add(p.getSpecies());
        return perennialSpecies;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public Species getSpecies(Family family) {
        for (Plant p : plants)
            if (p.getSpecies().getFamily().equals(family))
                return p.getSpecies();
        return null;
    }

    public boolean isOrigo() {
        return location.getX() == 0 && location.getY() == 0;
    }

    public Location getLocation() {
        return location;
    }
}