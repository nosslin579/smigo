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

/**
 * The core class. Garden is responsible for adding/removing squares. This class
 * represent the garden were all squares are.
 *
 * @author Christian Nilsson
 */
public class Garden implements Iterable<SquareIterator> {
    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(Garden.class
            .getName());

    /**
     * The squares in the garden.
     */
    private Map<YearXY, Square> squares;
    private List<GardenObserver> observers = new ArrayList<GardenObserver>();
    private int id;

    public Garden(Map<Integer, ? extends Species> species, List<PlantData> plants) {
        squares = new HashMap<YearXY, Square>();
        if (plants != null) {
            for (PlantData p : plants) {
                Species s = species.get(p.getSpeciesId());
                this.addOrGetSquare(p.getYear(), p.getX(), p.getY()).addSpecies(s);
            }
        }
    }

    /**
     * Creating a new empty garden
     */
    public Garden() {
        this(null, null);
        log.info("Creating new garden");
    }

    public List<GardenObserver> getObservers() {
        return observers;
    }

    public void addObserver(GardenObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GardenObserver observer) {
        observers.add(observer);
    }

    /**
     * Add new year to garden. Copies items and perennials from latest year to
     * the new year.
     *
     * @param newYear the year to be added
     * @throws GardenException
     */
    public void addYear(int newYear) {
        SortedSet<Integer> years = getYears();
        if (years.contains(newYear))
            throw new GardenException("Cant add year since year " + newYear + " already exists", "AddYear.YearAlreadyExists");
        copyPermanentSquares(years.last(), newYear);
        if (getPlants(newYear).isEmpty())
            throw new GardenException("No plants added ", "AddYear.NoItemsOrPerennials");
    }

    public List<Plant> getPlants(int year) {
        List<Plant> ret = new ArrayList<Plant>();
        for (Square s : squares.values())
            if (s.getYear() == year)
                ret.addAll(s.getPlants());
        return ret;
    }

    public List<Plant> getPlants() {
        List<Plant> ret = new ArrayList<Plant>();
        for (Square s : squares.values())
            ret.addAll(s.getPlants());
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
                Square copy = new Square(toYear, s.getX(), s.getY(), s.getPerennialSpecies(), this);
                if (!copy.getPlants().isEmpty())
                    squares.put(s.getYearXY(), copy);
            }
    }

    /**
     * Returns a sorted set of all the year available in this garden.
     *
     * @return a sorted set of all the years
     */
    public SortedSet<Integer> getYears() {
        SortedSet<Integer> years = new TreeSet<Integer>();
        if (squares != null && squares.size() != 0)
            for (Square s : squares.values())
                years.add(s.getYear());
        return years;
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
    public Square addOrGetSquare(int year, int x, int y) {
        // log.info("Add square at " + grid);
        Square newSquare = getSquare(year, x, y);
        // if square already exist return that one
        if (newSquare != null)
            return newSquare;
        newSquare = new Square(year, x, y, this);
        squares.put(newSquare.getYearXY(), newSquare);
        return newSquare;
    }

    /**
     * Returns square at year and location or null if not present.
     */
    public Square getSquare(int year, int x, int y) {
        return squares.get(new YearXY(year, x, y));
    }

    public Collection<Square> getAllSquares() {
        return squares.values();
    }

    /**
     * Returns all squares for specified year.
     *
     * @param year the year
     * @return squares for specified year
     */
    public List<Square> getSquaresFor(Integer year) {
        List<Square> squaresForYear = new ArrayList<Square>();
        for (Square s : squares.values())
            if (s.isYear(year))
                squaresForYear.add(s);
        return squaresForYear;
    }

    /**
     * Deletes whole year from garden.
     *
     * @param year the year of which squares will be removed
     */
    public void deleteYear(int year) {
        for (Square s : getSquaresFor(year))
            squares.remove(s.getYearXY());
    }

    public void deleteYears(Set<Integer> years) {
        for (Integer y : years)
            deleteYear(y);
    }

    /**
     * Returns the bounds
     *
     * @param year
     * @return the bounds
     */
    public Bounds getBoundsFor(int year) {
        Collection<Square> squaresAtYear = getSquaresFor(year);
        squaresAtYear.add(addOrGetSquare(year, 0, 0));
        if (getPlants(year).isEmpty())
            return new Bounds(-6, -3, 6, 3);
        Bounds ret = new Bounds(-2, -2, 2, 2);
        for (Square s : squaresAtYear) {
            if (!s.getPlants().isEmpty()) {
                ret.setMiny(Math.min(s.getY(), ret.getMiny()));
                ret.setMinx(Math.min(s.getX(), ret.getMinx()));
                ret.setMaxx(Math.max(s.getX(), ret.getMaxx()));
                ret.setMaxy(Math.max(s.getY(), ret.getMaxy()));
            }
        }
        ret.increseBounds();
        return ret;
    }

    public Map<YearXY, Square> getSquares() {
        return squares;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Iterator<SquareIterator> iterator() {
        return new YearIterator(this);
    }

    public Iterator<SquareIterator> getIterator() {
        return iterator();
    }

    public boolean hasSpecies(int id) {
        for (Square s : getAllSquares()) {
            for (Plant p : s.getPlants()) {
                if (p.getSpecies().getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

}