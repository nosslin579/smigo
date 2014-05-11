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

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.entities.User;
import org.sourceforge.kga.errors.RuleException;
import org.sourceforge.kga.rules.Rule;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * This represent a species, not the physical plant.
 *
 * @author Christian Nilsson
 */
public class Species implements Comparable<Species> {
  private static final Logger log = LoggerFactory.getLogger(Species.class);

  public static final int UNKNOWN = 0;// "Unknown";


  /**
   * The scientific name of the species. Aka the latin name.
   */
  @Length(min = 5, max = 255)
  private String scientificName;

  /**
   * The translated name of the species. Aka the name used when talking
   * normal.
   */
  @Length(min = 2, max = 100)
  private String translation;

  /**
   * Rules that apply to this species.
   */
  private Set<Rule> rules = new HashSet<Rule>();

  /**
   * Unique integer that is the id to this species
   */
  private int id;

  /**
   * Family is the is the species family in the biological classification.
   */
  @NotNull
  private Family family = null;

  // If plant not is annual it is perennial
  private boolean annual = true;

  // If true instance is not a species but an object e.g. path, wall or house.
  private boolean item = false;

  private String ph;

  private int plantsPerSquare;

  // In millimeter
  private int seedDepth;

  // In millimeter
  private int spacing;

  private boolean precultivate;

  private boolean display;
  private User creator;

  private String iconFileName;

  public Species(int id, String scientificName, String translation, boolean item,
                 boolean annual, Family family) {
    if (family == null)
      log.error("Family not specified for " + scientificName);
    this.id = id;
    this.scientificName = scientificName.trim();
    this.family = family;
    this.item = item;
    this.annual = annual;
    this.translation = translation;
  }

  public Species() {
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Species)
      return ((Species) obj).getId() == this.getId();
    else
      return false;
  }

  public String getScientificName() {
    return scientificName;
  }

  public void setScientificName(String scientificName) {
    this.scientificName = scientificName;
  }

  public String getTranslation() {
    return translation;
  }

  @Override
  public String toString() {
    return "Species {" + translation + "," + getScientificName() + "," + getId() + ","
             + rules.size() + "," + family + "," + iconFileName + "}";
  }

  /**
   * Return the rules for this species. To get all rules call method with
   * subclass as null. To only get Beneficial rules call method with
   * BeneficialRule.class.
   *
   * @return A set of rules
   */
  public Collection<Rule> getRules(Class<?>... ruleTypes) {
    if (ruleTypes == null || ruleTypes.length == 0)
      return rules;
    Collection<Rule> rulesToSort = new HashSet<Rule>(rules.size());
    for (Rule r : rules)
      for (Class<?> t : ruleTypes)
        if (t.isInstance(r))
          rulesToSort.add(r);
    List<Rule> ret = new ArrayList<Rule>(rulesToSort);
    Collections.sort(ret);
    return ret;
  }

  public Collection<Rule> getRules() {
    return rules;
  }

  public Collection<Rule> getCropRotationRules() {
    return getRules(Rule.CROP_ROTATION_RULES);
  }

  public Collection<Rule> getCompanionPlantingRules() {
    return getRules(Rule.COMPANION_PLANTING_RULES);
  }

  /**
   * Adds a rule to this species. All rules must have a host. I.e. the species
   * that the rule applies upon.
   *
   * @param r the rule to add to this species
   * @return the rule just added
   * @throws RuleException
   */
  public Rule addRule(Rule r) {
    if (r == null)
      throw new RuleException("May not add a null rule");
    if (r.getHost() != this)
      throw new RuleException("Adding rule to species " + id + " with another host "
                                + r.getHost().getId());
//    log.debug("Adding " + r + " to " + getScientificName());
    rules.add(r);
    return r;
  }

  public void removeRule(Rule rule) {
    rules.remove(rule);
  }

  public boolean isRecurrent() {
    return !annual || isItem();
  }

  public int getId() {
    return id;
  }

  public Family getFamily() {
    return family;
  }

  public boolean isAnnual() {
    return annual;
  }

  public void setAnnual(boolean annual) {
    this.annual = annual;
  }

  public boolean isItem() {
    return item;
  }

  public String getPh() {
    return ph;
  }

  public int getPlantsPerSquare() {
    return plantsPerSquare;
  }

  /**
   * @return Seed depth in mm
   */
  public int getSeedDepth() {
    return seedDepth;
  }

  /**
   * @return Spacing in mm
   */
  public int getSpacing() {
    return spacing;
  }

  public boolean isPrecultivate() {
    return precultivate;
  }

  @Override
  public int compareTo(Species other) {
    if (other.isItem() != item)
      return item ? 1 : -1;
    return this.translation.compareTo(other.translation);
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public void setFamily(Family family) {
    this.family = family;
  }

  public void setTranslation(String translation) {
    this.translation = translation;
  }

  public String getTranslationKey() {
    return "species" + id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setIconFileName(String iconFileName) {
    this.iconFileName = iconFileName;
  }

  public String getIconFileName() {
    return iconFileName;
  }
}