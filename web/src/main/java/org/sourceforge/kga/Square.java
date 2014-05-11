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

package org.sourceforge.kga;

import org.sourceforge.kga.rules.Rule;

import java.util.*;

/**
 * Square is responsible for adding/removing species and can supply hints.
 * Square feet gardening is divided into squares. In each square you can plant
 * one or several plants. This class represent one square and each instance
 * represent a physical square.
 *
 * @author Christian Nilsson
 */
public class Square implements Comparable<Square> {
  private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(Garden.class
                                                                                     .getName());
  private int year, x, y;
  private List<Plant> plants;
  private Garden garden;

  /**
   * Constructs a new Square.
   */
  public Square(int year, int x, int y, List<Species> species, Garden garden) {
    this.garden = garden;
    this.year = year;
    this.x = x;
    this.y = y;
    plants = new ArrayList<Plant>();
    for (Species s : species)
      addSpecies(s);
  }

  /**
   * Constructs a new empty Square
   */
  public Square(int year, int x, int y, Garden garden) {
    this(year, x, y, new ArrayList<Species>(4), garden);
  }

  /**
   * Constructs a new Square.
   */
  public Square(int year, int x, int y, Species species, Garden garden) {
    this(year, x, y, garden);
    this.addSpecies(species);
  }

  /**
   * Adds a species if there is no item and specified species is already
   * present. Calls squareChanged().
   *
   * @return true if added
   */
  public boolean addSpecies(Species s) {
    if (containsItem() || containsSpecies(s))
      return false;
    plants.add(new Plant(s, this));
    squareChanged();
    return true;
  }

  /**
   * Adds a species if there is no item and specified species is already
   * present. Calls squareChanged().
   *
   * @param s
   *            the species
   * @return true if added
   */
  /*
   * public void addSpecies(Integer address) {
	 * addSpecies(SpeciesResource.getSpecies(address)); }
	 */

  /**
   * Removes species. Calls squareChanged().
   *
   * @param s the species
   */
  public void removeSpecies(Species s) {
    if (s != null && containsSpecies(s)) {
      this.plants.remove(new Plant(s, this));
      this.plants.remove(null);
      log.fine("Remove species " + s + " at grid " + x + ":" + y);
    } else {
      removeSpecies();
      // log.info("Remove at grid " + grid);
    }

    squareChanged();
  }

  /**
   * This method is called when a species is added or removed. It notifies
   * garden observer.squareChanged that this square has changed and also
   * notifies garden observer.hintschanged to itself and its nearest neighbor.
   */
  public void squareChanged() {
    for (GardenObserver o : garden.getObservers())
      o.squareChanged(this);

    // Updates hints for this year and all years beyond
    for (Square h : garden.getAllSquares()) {
      if (h.getYear() < getYear())
        continue;
      int xDiff = Math.abs(getX() - h.getX());
      int yDiff = Math.abs(getY() - h.getY());
      if (xDiff > 1 || yDiff > 1)
        continue;
      for (GardenObserver o : garden.getObservers())
        o.hintsChanged(h);
    }
  }

  /**
   * Removes all species in this square and calls squareChanged().
   */
  public void removeSpecies() {
    plants = new ArrayList<Plant>();
    squareChanged();
  }

  /**
   * Returns the eight nearest neighbors plus itself.
   *
   * @return the eight nearest neighbors plus itself
   */
  public Collection<Square> getSurroundingSquares() {
    return getSurroundingSquares(1);
  }

  /**
   * Returns neighboring squares plus itself.
   *
   * @param radius the radius (radius = 1 gives 9 squares, radius = 2 gives 25
   *               squares)
   * @return neighboring squares plus itself
   */
  public Collection<Square> getSurroundingSquares(int radius) {
    Collection<Square> surrounding = new HashSet<Square>();
    for (Square s : garden.getAllSquares()) {
      int xDiff = Math.abs(getX() - s.getX());
      int yDiff = Math.abs(getY() - s.getY());
      if (s.getYear() == this.getYear() && xDiff <= radius && yDiff <= radius)
        surrounding.add(s);
    }
    return surrounding;
  }

  /**
   * Returns previous squares at same location.
   *
   * @param yearsBack years back in time.
   * @return previous squares at same location
   */
  public Collection<Square> getPreviousSquares(int yearsBack) {
    Collection<Square> previous = new HashSet<Square>();
    int earliestYear = this.getYear() - yearsBack;
    for (Square s : garden.getAllSquares())
      if (s.getYear() < this.getYear() && s.getYear() >= earliestYear
            && s.getX() == this.getX() && s.getY() == this.getY())
        previous.add(s);
    return previous;
  }

  /**
   * Returns neighboring squares and theirs previous squares.
   *
   * @param yearsBack years back in time
   * @param range
   * @return neighboring squares and theirs previous squares
   */
  public Collection<Square> getPreviousSurroundingSquares(int yearsBack, int radius) {
    Collection<Square> previousSurrounding = new HashSet<Square>();
    int earliestYear = this.getYear() - yearsBack;
    for (Square s : garden.getAllSquares()) {
      int xDiff = Math.abs(getX() - s.getX());
      int yDiff = Math.abs(getY() - s.getY());
      if (s.getYear() < this.getYear() && s.getYear() > earliestYear && xDiff <= radius
            && yDiff <= radius)
        previousSurrounding.add(s);
    }
    return previousSurrounding;
  }

  /**
   * Returns the hints for this square. The collection does not contain any
   * null elements.
   *
   * @return the hints for this square
   */
  public Collection<Hint> getHints() {
    List<Hint> hints = new ArrayList<Hint>();
    for (Rule r : getRules())
      hints.add(r.getHint(this));
    hints.removeAll(Collections.singleton(null));
    return hints;
  }

  @Override
  public int hashCode() {
    return (((year + 37) + 37) * x + 37) * y;
  }

  @Override
  /**
   * Checks if squares is at same year and location. This method is not necessary,
   * instead you can compare instances (square1 == square2).
   */
  public boolean equals(Object obj) {
    if (obj instanceof Square) {
      Square s = (Square) obj;
      return (this.year == s.year && this.x == s.getX() && this.y == s.getY());
    } else {
      return false;
    }
  }

  /**
   * Checks square year.
   *
   * @param year the year
   * @return true if same year
   */
  public boolean isYear(int year) {
    return this.year == year;
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

  /**
   * Check if square contains specified species
   *
   * @param s the species
   * @return true if it contains species
   */
  public boolean containsSpecies(Species s) {
    return plants.contains(new Plant(s, this));
  }

  /**
   * Checks if any species in this square belongs to specified family
   *
   * @param family the family
   * @return
   */
  public boolean containsFamily(Family family) {
    for (Plant p : plants)
      if (p.getSpecies().getFamily().equals(family))
        return true;
    return false;
  }

  public int getSize() {
    return plants.size();
  }

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
    return "Square year:" + year + " x:" + x + " y:" + y + " species:" + getSize();
  }

  /**
   * Returns all rules for all species in this square in one Collection
   *
   * @return all rules for all species in this square in one Collection
   */
  public Collection<Rule> getRules() {
    Set<Rule> rules = new HashSet<Rule>();
    for (Plant p : plants)
      rules.addAll(p.getSpecies().getRules());
    return rules;
  }

  public Collection<Rule> getVisibleRules() {
    Set<Rule> rules = new HashSet<Rule>();
    for (Plant p : plants)
      for (Rule r : p.getSpecies().getRules())
        if (r.isDisplay())
          rules.add(r);
    return rules;
  }

  /**
   * Returns all species that are perennials or items
   *
   * @return all species that are perennials or items
   */
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

  @Override
  public int compareTo(Square o) {
    if (o.year != year)
      return o.year - year;
    if (o.y != y)
      return o.y - y;
    if (o.x != x)
      return o.x - x;
    return 0;
  }

  /**
   * Returns the first occurrence of species that belongs to family
   *
   * @param family the family
   * @return the first occurrence of species that belongs to family
   */
  public Species getSpecies(Family family) {
    for (Plant p : plants)
      if (p.getSpecies().getFamily().equals(family))
        return p.getSpecies();
    return null;
  }

  public boolean isOrigo() {
    return x == 0 && y == 0;
  }

  public YearXY getYearXY() {
    return new YearXY(year, x, y);
  }

}