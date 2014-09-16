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

import com.fasterxml.jackson.annotation.JsonIgnore;
import kga.rules.Rule;

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
    private final Map<Integer, Plant> plantsMap = new HashMap<Integer, Plant>();

    public Square(Location location, List<Species> species) {
        this.location = new YearXY(location);
        for (Species s : species)
            addSpecies(s);
    }


    public Square(Location location) {
        this(location, new ArrayList<Species>());
    }

    public void addSpecies(Species s) {
        plantsMap.put(s.getId(), new Plant(s, this));
    }

    public void removeSpecies(Species s) {
        this.plantsMap.remove(s.getId());
    }

    public boolean isYear(int year) {
        return this.location.getYear() == year;
    }

    public boolean containsSpecies(int speciesId) {
        return plantsMap.containsKey(speciesId);
    }

    /**
     * Checks if square contain an item
     *
     * @return true if contains item
     */
    public boolean containsItem() {
        for (Plant p : plantsMap.values())
            if (p.getSpecies().isItem())
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "Square{" +
                "location=" + location +
                '}';
    }

    @JsonIgnore
    public Collection<Rule> getRules() {
        Set<Rule> rules = new HashSet<Rule>();
        for (Plant p : plantsMap.values())
            rules.addAll(p.getSpecies().getRules());
        return rules;
    }

    @JsonIgnore
    public List<Species> getPerennialSpecies() {
        List<Species> perennialSpecies = new ArrayList<Species>();
        for (Plant p : plantsMap.values())
            if (p.getSpecies().isRecurrent())
                perennialSpecies.add(p.getSpecies());
        return perennialSpecies;
    }

    public Map<Integer, Plant> getPlants() {
        return plantsMap;
    }

    public Species getSpecies(Family family) {
        for (Plant p : plantsMap.values())
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