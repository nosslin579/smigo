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
 * This rule gives a hint when planting a beneficial plant next to each other.
 * For instance onion next to carrot.
 *
 * @author Christian Nilsson
 */

public abstract class AbstractBeneficialRule extends Rule {
  private Species friend;

  public AbstractBeneficialRule(Species host, Species friend) throws RuleException {
    super(host);
    if (friend == null)
      throw new RuleException("Friend may not be null");
    this.friend = friend;

    Object[] params = {getHost().getTranslation(), friend.getTranslation()};
    setTextParams(params);
  }

  public Hint getHint(Square target) {
    if (!isDisplay())
      return null;
    for (Square s : target.getSurroundingSquares()) {
      if (s.containsSpecies(friend))
        return new Hint(this, s, target, getHintTranslationKey(), friend);
    }
    return null;
  }

  @Override
  public String toString() {
    return super.toString() + " " + friend;
  }

  public Species getFriend() {
    return friend;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AbstractBeneficialRule) {
      AbstractBeneficialRule r = (AbstractBeneficialRule) obj;
      return this.getRuleType().ordinal() == r.getRuleType().ordinal() &&
               this.getFriend().getId() == r.getFriend().getId() &&
               this.getHost().getId() == r.getHost().getId();
    }
    return false;
  }

}
