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
public class PerennialRepetitionRule extends Rule {
  /**
   * Years between the plant is planted at the same location. Gap is usually 4
   * years.
   */
  private int gap = 4;

  /**
   * How many years a perennial can be cultivated at the same spot. For
   * annuals this equals 0.
   */
  private int allowedRepetitions = 0;

  public PerennialRepetitionRule(Species host, int gap,
                                 int allowedRepetitions) throws RuleException {
    super(host);
    if (gap <= allowedRepetitions)
      throw new RuleException("Gap must be greater than allowed repetitions");
    this.gap = gap;
    this.allowedRepetitions = allowedRepetitions;
    Object[] params = {host.getTranslation(), gap, allowedRepetitions};
    setInfo("god knows what this is");
  }

  public int getGap() {
    return gap;
  }

  public int getAllowedRepetitions() {
    return allowedRepetitions;
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
    for (Square s : square.getPreviousSquares(gap + allowedRepetitions))
      // Checking if squares contain host but not the squares within the
      // allowed repetitions
      if ((square.getYear() - allowedRepetitions) > s.getYear()
            && s.containsSpecies(getHost())) {
        return new Hint(this, s, square, getHintTranslationKey(), getHost());
      }
    for (Square s : square.getPreviousSurroundingSquares(gap + allowedRepetitions,
                                                          Rule.CLOSEST_NEIGHBOURS))
      if ((square.getYear() - allowedRepetitions) > s.getYear()
            && s.containsSpecies(getHost())) {
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
    throw new RuntimeException("Not implemented yet");
  }

}