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

package org.sourceforge.kga.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sourceforge.kga.Hint;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.Square;
import org.sourceforge.kga.errors.RuleException;

/**
 * Rules are applied to species. Rules is the way to tell the system how a
 * species should or shouldn't be used. If you break a warning type rule or
 * follow a benefit type rule you will get a hint.
 *
 * @author Christian Nilsson
 */

public abstract class Rule implements Comparable<Rule> {
  private static final Logger log = LoggerFactory.getLogger(Rule.class);

  public static final int ONE_YEAR_BACK = 1;
  public static final int CLOSEST_NEIGHBOURS = 1;

  public static final Class<?>[] COMPANION_PLANTING_RULES = {BeneficialRule.class,
                                                              DisadvantageousRule.class};

  public static final Class<?>[] CROP_ROTATION_RULES = {BadCropRotationRule.class,
                                                         FamilyRepetitionRule.class, GoodCropRotationRule.class, PerennialRepetitionRule.class,
                                                         RepetitionRule.class};

  /**
   * The species that this rule applies to.
   */
  private Species host;

  private boolean display = true;

  private int id;

  private String info;

  private Object[] textParams;

  private int creatorId;

  public Rule(Species host) throws RuleException {
    if (host == null)
      throw new RuleException("Host may not be null");
    this.host = host;
    textParams = new Object[0];
  }

  /**
   * Return the hint for this rule if the warning rule type is violated or the
   * advice rule type is followed. Else it will return null
   *
   * @param s the square that the species is located at
   * @return a hint or null
   */
  public abstract Hint getHint(Square s);

  public String toString() {
    return getClass().getSimpleName() + " host: " + host;
  }

  /**
   * Returns the species that this rule applies to.
   *
   * @return a species
   */
  public Species getHost() {
    return host;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public int getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(int creatorId) {
    this.creatorId = creatorId;
  }

  public abstract String getHintTranslationKey();

  public Object[] getHintTranslationParams() {
    return textParams;
  }

  public void setTextParams(Object[] textParams) {
    this.textParams = textParams;
  }

  public abstract RuleType getRuleType();

  @Override
  public int compareTo(Rule o) {
    return getInfo().compareTo(o.getInfo());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Rule) {
      Rule r = (Rule) obj;
      return r.getRuleType().ordinal() == this.getRuleType().ordinal() && r.getHost().equals(this.getHost());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getRuleType().getId();
  }


}
