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

import org.sourceforge.kga.Hint;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.Square;
import org.sourceforge.kga.errors.RuleException;

public class FamilyRepetitionRule extends Rule {
  private int yearsBackInTime;

  public FamilyRepetitionRule(Species host, int yearsBackInTime) throws RuleException {
    super(host);
    this.yearsBackInTime = yearsBackInTime;
  }

  @Override
  public Hint getHint(Square square) {
    if (!isDisplay())
      return null;
    for (Square s : square.getPreviousSquares(yearsBackInTime))
      if (s.containsFamily(getHost().getFamily())) {
        return new Hint(this, s, square, getHintTranslationKey(), s.getSpecies(getHost().getFamily()));
      }
    for (Square s : square.getPreviousSurroundingSquares(yearsBackInTime, 1))
      if (s.containsFamily(getHost().getFamily())) {
        return new Hint(this, s, square, "repetitionfamilynearby", s.getSpecies(getHost()
                                                                                  .getFamily()));
      }
    return null;
  }

  public int getYearsBackInTime() {
    return yearsBackInTime;
  }

  @Override
  public String toString() {
    return super.toString() + " yearsback: " + yearsBackInTime;
  }

  public String getHintTranslationKey() {
    return "hint.repetitionfamily";
  }

  @Override
  public RuleType getRuleType() {
    throw new RuntimeException("Not implemented yet");
  }

}