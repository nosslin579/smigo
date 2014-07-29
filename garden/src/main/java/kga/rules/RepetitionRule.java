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
import kga.Species;
import kga.Square;
import kga.errors.RuleException;

/**
 * This rule gives a hint when a species is planted at the same spot within a
 * given period of years. Also supports perennials.
 *
 * @author Christian Nilsson
 */
public class RepetitionRule extends AbstractRule {
    /**
     * Years between the plant is planted at the same location. Gap is usually 4
     * years.
     */
    private int gap = 4;

    public RepetitionRule(int ruleId, Species host, RuleType ruleType, int gap, String hintMessageKey) {
        super(ruleId, host, ruleType, hintMessageKey);
        if (gap <= 0)
            throw new RuleException("Gap must be greater than 0");
        this.gap = gap;
    }

    public int getGap() {
        return gap;
    }

    @Override
    public Hint getHint(Square square) {
        for (Square s : square.getPreviousSurroundingSquares(gap, Rule.CLOSEST_NEIGHBOURS)) {
            if (s.containsSpecies(getHost().getId())) {
                final String[] messageKeyArguments = {String.valueOf(gap)};
                return new Hint(s, square, getHintMessageKey(), messageKeyArguments, getHost());
            }
        }
        return null;
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