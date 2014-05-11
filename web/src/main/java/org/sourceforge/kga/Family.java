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

/**
 * Family is the family of the species in the Linnaean taxonomy.
 *
 * @author Christian Nilsson
 */
public class Family implements Comparable<Family> {
  // private static java.util.logging.Logger log =
  // java.util.logging.Logger.getLogger(Garden.class
  // .getName());
  /**
   * The scientific name in latin
   */
  private String name;
  /**
   * The id is the unique identifier, a number between 7200 and 7299.
   */
  private final int id;

  /**
   * Creates a new family.
   *
   * @param id the unique identifier
   */
  public Family(String name, int id) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    if (name == null)
      return "null";
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Family)
      return ((Family) obj).getId() == id;
    return false;
  }

  @Override
  public int hashCode() {
    return id;
  }

  public String getTranslationKey() {
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
}
