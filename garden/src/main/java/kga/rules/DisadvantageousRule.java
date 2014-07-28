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
 * This rule gives a hint when planting a incompatible companions next to each
 * other. For instance dill next to carrot.
 *
 * @author Christian Nilsson
 */

public class DisadvantageousRule extends Rule {
    private Species foe;

    public DisadvantageousRule(Species foe) {
        if (foe == null)
            throw new RuleException("Foe may not be null");
        this.foe = foe;
    }

    @Override
    public Hint getHint(Square square) {
        for (Square s : square.getSurroundingSquares()) {
            if (s.containsSpecies(foe.getId()))
                return new Hint(s, square, getMessageKey(), foe);
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " species: " + foe;
    }

    public String getMessageKey() {
        return "hint.badcompanion";
    }

    @Override
    public RuleType getRuleType() {
        return RuleType.badcompanion;
    }

}
