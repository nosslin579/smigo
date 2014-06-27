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

package kga.rules;

import kga.Hint;
import kga.errors.RuleException;
import kga.Species;
import kga.Square;

/**
 * This rule gives a hint when a species is planted at the same spot within a
 * given period of years. Also supports perennials.
 *
 * @author Christian Nilsson
 */
public class RepetitionRule extends Rule {
  /**
   * Years between the plant is planted at the same location. Gap is usually 4
   * years.
   */
  private int gap = 4;

  public RepetitionRule(Species host, int gap) throws RuleException {
    super(host);
    if (gap <= 0)
      throw new RuleException("Gap must be greater than 0");
    this.gap = gap;
    Object[] params = {host.getTranslation(), gap};
    setTextParams(params);
  }

  public int getGap() {
    return gap;
  }

  // public RepetitionRule(Effect effect) {
  // this(effect, 4);
  // }

  @Override
  public Hint getHint(Square square) {
    if (!isDisplay())
      return null;
    // log.info("Searching for " + getHost()) + " in " + ;
    // Getting squares way back
    for (Square s : square.getPreviousSquares(gap))
      // Checking if squares contain host
      if (s.containsSpecies(getHost())) {
        return new Hint(this, s, square, getHintTranslationKey(), getHost());
      }
    for (Square s : square.getPreviousSurroundingSquares(gap, Rule.CLOSEST_NEIGHBOURS))
      if (s.containsSpecies(getHost())) {
        return new Hint(this, s, square, "speciesrepetitionnearby", getHost());
      }
    return null;
  }

  @Override
  public String toString() {
    return super.toString() + " years back: " + gap;
  }

  public String getHintTranslationKey() {
    return "hint.speciesrepetition";
  }

  @Override
  public RuleType getRuleType() {
    return RuleType.speciesrepetition;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RepetitionRule && super.equals(obj)) {
      RepetitionRule r = (RepetitionRule) obj;
      return this.getGap() == r.getGap();
    }
    return false;

  }
}