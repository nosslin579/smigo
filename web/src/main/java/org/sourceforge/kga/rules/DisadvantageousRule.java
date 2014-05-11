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

/**
 * This rule gives a hint when planting a incompatible companions next to each
 * other. For instance dill next to carrot.
 *
 * @author Christian Nilsson
 */

public class DisadvantageousRule extends Rule {
  private Species foe;

  public DisadvantageousRule(Species host, Species foe) throws RuleException {
    super(host);
    if (foe == null)
      throw new RuleException("Foe may not be null");
    this.foe = foe;
    Object[] params = {getHost().getTranslation(), foe.getTranslation()};
    setTextParams(params);
  }

  @Override
  public Hint getHint(Square square) {
    if (!isDisplay())
      return null;
    for (Square s : square.getSurroundingSquares()) {
      if (s.containsSpecies(foe))
        return new Hint(this, s, square, getHintTranslationKey(), foe);
    }
    return null;
  }

  @Override
  public String toString() {
    return super.toString() + " species: " + foe;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DisadvantageousRule && super.equals(obj)) {
      DisadvantageousRule r = (DisadvantageousRule) obj;
      return this.getRuleType().ordinal() == r.getRuleType().ordinal() && this.getFoe().getId() == r.getFoe().getId();
    }
    return false;
  }

  public Species getFoe() {
    return foe;
  }

  public String getHintTranslationKey() {
    return "hint.badcompanion";
  }

  @Override
  public RuleType getRuleType() {
    return RuleType.badcompanion;
  }

}
