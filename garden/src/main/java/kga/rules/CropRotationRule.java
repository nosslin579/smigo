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

import kga.Family;
import kga.Hint;
import kga.Square;
import kga.errors.RuleException;

/**
 * This rule gives a hint when rotating crops correct. For instance cabbage
 * after legumes.
 *
 * @author Christian Nilsson
 */

public class CropRotationRule extends AbstractRule {

    private Family family;

    public CropRotationRule(int ruleId, RuleType ruleType, Family family, String hintMessageKey) {
        super(ruleId, ruleType, hintMessageKey);
        if (family == null)
            throw new RuleException("Family may not be null");
        this.family = family;

    }

    @Override
    public Hint getHint(Square square) {
        for (Square s : square.getPreviousSquares(Rule.ONE_YEAR_BACK))
            if (s.containsFamily(family))
                return new Hint(s, square, getMessageKey(), s.getSpecies(family));
        return null;
    }

    public Family getFamily() {
        return family;
    }

}
