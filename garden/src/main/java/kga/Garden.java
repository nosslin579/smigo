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

import kga.errors.GardenException;

import java.util.*;

public class Garden {

    private final Map<Integer, ? extends Species> species;
    private Map<Location, Square> squares = new HashMap<Location, Square>();

    public Garden(Map<Integer, ? extends Species> species, List<PlantData> plants) {
        this.species = species;
        if (plants != null) {
            for (PlantData p : plants) {
                Species s = species.get(p.getSpeciesId());
                if (s == null) {
                    throw new IllegalArgumentException("Species may not be null. Id:" + p.getSpeciesId());
                }
                this.addOrGetSquare(new YearXY(p)).addSpecies(s);
            }
        }
    }

    public Garden() {
        this(new HashMap<Integer, Species>(), new ArrayList<PlantData>());
    }

    public void addYear(int newYear) {
        SortedSet<Integer> years = new TreeSet<Integer>(getSquares().keySet());
        if (years.contains(newYear))
            throw new GardenException("Cant add year since year " + newYear + " already exists", "AddYear.YearAlreadyExists");
        copyPermanentSquares(years.last(), newYear);
    }

    public void deleteYear(int year) {
        for (Square s : getSquares().get(year))
            squares.remove(s.getLocation());
    }

    public List<PlantData> getPlants(int year) {
        List<PlantData> ret = new ArrayList<PlantData>();
        for (Square s : squares.values())
            if (s.isYear(year)) {
                for (Plant plant : s.getPlants().values()) {
                    ret.add(new PlantBean(plant));
                }
            }
        return ret;
    }

    /**
     * Copies square for one year to another. Copies only perennial and items.
     * Annuals are not copied.
     *
     * @param fromYear existing year that squares are copied from
     * @param toYear   new year to which square are copied to
     */
    private void copyPermanentSquares(int fromYear, int toYear) {
        Collection<Square> copyOfSquares = new ArrayList<Square>(squares.values());
        for (Square s : copyOfSquares)
            if (s.isYear(fromYear)) {
                Square copy = new Square(new YearXY(toYear, s.getLocation().getX(), s.getLocation().getY()), s.getPerennialSpecies());
                if (!copy.getPlants().isEmpty())
                    squares.put(s.getLocation(), copy);
            }
    }

    public String toString() {
        return "Garden with " + squares.size() + " squares";
    }

    /**
     * Adds a square at year and grid. If there already is a square at this
     * location it will only return this square.
     *
     * @return the added or existing square
     */
    public Square addOrGetSquare(Location location) {
        // log.info("Add square at " + grid);
        Square newSquare = getSquare(location);
        // if square already exist return that one
        if (newSquare != null)
            return newSquare;
        newSquare = new Square(location);
        squares.put(newSquare.getLocation(), newSquare);
        return newSquare;
    }

    public Square getSquare(Location location) {
        return squares.get(location);
    }

    public Map<Integer, Collection<Square>> getSquares() {
        if (squares.isEmpty()) {
            addOrGetSquare(new YearXY(Calendar.getInstance().get(Calendar.YEAR), 0, 0));
        }

        Map<Integer, Collection<Square>> ret = new HashMap<Integer, Collection<Square>>();
        for (Square square : squares.values()) {
            final int year = square.getLocation().getYear();
            if (ret.get(year) == null) {
                ret.put(year, new ArrayList());
            }
            ret.get(year).add(square);
        }
        return ret;
    }

    /**
     * Returns the eight nearest neighbors plus itself.
     */
    public Collection<Square> getSurroundingSquares(Location location) {
        return getSurroundingSquares(location, 1);
    }

    /**
     * Returns neighboring squares plus itself. The radius (radius = 1 gives 9 squares, radius = 2 gives 25)
     */
    public Collection<Square> getSurroundingSquares(Location location, int radius) {
        Collection<Square> surrounding = new HashSet<Square>();
        for (Square s : squares.values()) {
            int xDiff = Math.abs(location.getX() - s.getLocation().getX());
            int yDiff = Math.abs(location.getY() - s.getLocation().getY());
            if (s.getLocation().getYear() == location.getYear() && xDiff <= radius && yDiff <= radius)
                surrounding.add(s);
        }
        return surrounding;
    }

    /**
     * Returns previous squares at same location.
     */
    public Collection<Square> getPreviousSquares(Location location, int yearsBack) {
        Collection<Square> previous = new HashSet<Square>();
        int earliestYear = location.getYear() - yearsBack;
        for (Square s : squares.values()) {
            if (s.getLocation().getYear() < location.getYear() && s.getLocation().getYear() >= earliestYear
                    && s.getLocation().getX() == location.getX() && s.getLocation().getY() == location.getY()) {
                previous.add(s);
            }
        }
        return previous;
    }

    /**
     * Returns neighboring squares and theirs previous squares.
     */
    public Collection<Square> getPreviousAndSurroundingSquares(Location location, int yearsBack, int radius) {
        Collection<Square> ret = new HashSet<Square>();
        int earliestYear = location.getYear() - yearsBack;
        for (Square s : squares.values()) {
            int xDiff = Math.abs(location.getX() - s.getLocation().getX());
            int yDiff = Math.abs(location.getY() - s.getLocation().getY());
            if (s.getLocation().getYear() < location.getYear() && s.getLocation().getYear() > earliestYear && xDiff <= radius && yDiff <= radius)
                ret.add(s);
        }
        return ret;
    }

    public List<Hint> getHints() {
        List<Hint> ret = new ArrayList<Hint>();
        for (Square square : squares.values()) {
            for (Plant plant : square.getPlants().values()) {
                ret.addAll(plant.getHints(this));
            }
        }
        return ret;
    }

    public Collection<? extends Species> getSpecies() {
        return species.values();
    }
}