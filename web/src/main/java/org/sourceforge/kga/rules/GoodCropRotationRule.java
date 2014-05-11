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

import org.sourceforge.kga.Family;
import org.sourceforge.kga.Hint;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.Square;
import org.sourceforge.kga.errors.RuleException;

/**
 * This rule gives a hint when rotating crops correct. For instance cabbage
 * after legumes.
 *
 * @author Christian Nilsson
 */

public class GoodCropRotationRule extends Rule {

  private Family family;

  public GoodCropRotationRule(Species host, Family family) throws RuleException {
    super(host);
    if (family == null)
      throw new RuleException("Family may not be null");
    this.family = family;
    Object[] params = {host.getTranslation(), family.getName()};
    setTextParams(params);
  }

  @Override
  public Hint getHint(Square square) {
    if (!isDisplay())
      return null;

    for (Square s : square.getPreviousSquares(Rule.ONE_YEAR_BACK))
      if (s.containsFamily(family))
        return new Hint(this, s, square, getHintTranslationKey(), s.getSpecies(family));
    return null;
  }

  public Family getFamily() {
    return family;
  }

  @Override
  public String toString() {
    return super.toString() + " family: " + family.getName();
  }

  public String getHintTranslationKey() {
    return "hint.goodcroprotation";
  }

  @Override
  public RuleType getRuleType() {
    return RuleType.goodcroprotation;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof GoodCropRotationRule && super.equals(obj)) {
      GoodCropRotationRule r = (GoodCropRotationRule) obj;
      return this.getRuleType().ordinal() == r.getRuleType().ordinal() && this.getFamily().getId() == r.getFamily().getId();
    }
    return false;
  }
}
