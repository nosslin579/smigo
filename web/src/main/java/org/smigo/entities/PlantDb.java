package org.smigo.entities;

import kga.PlantData;

/**
 * Jpa entity for a plant
 *
 * @author Christian Nilsson
 */
public class PlantDb implements PlantData,Comparable<PlantDb> {
  private int x;
  private int y;
  private int year;
  private int speciesId;

  public PlantDb(int speciesId, int year, int x, int y) {
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
  public int compareTo(PlantDb p) {
    if (this.year != p.year)
      return this.year - p.year;
    if (this.y != p.y)
      return this.y - p.y;
    if (this.x != p.x)
      return this.x - p.x;
    return 0;
  }

}
