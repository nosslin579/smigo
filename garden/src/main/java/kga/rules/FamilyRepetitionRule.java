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

public class FamilyRepetitionRule {
    private Family family;
    private int yearsBackInTime;

    public FamilyRepetitionRule(Family family, int yearsBackInTime) {
        this.family = family;
        this.yearsBackInTime = yearsBackInTime;
    }

    public Hint getHint(Square square) {
        for (Square s : square.getPreviousSurroundingSquares(yearsBackInTime, 1))
            if (s.containsFamily(family)) {
                final String[] messageKeyArguments = {String.valueOf(yearsBackInTime), family.getMessageKey()};
                return new Hint(s, square, getMessageKey(), messageKeyArguments, s.getSpecies(family));
            }
        return null;
    }

    public int getId() {
        return 0;
    }

    public int getYearsBackInTime() {
        return yearsBackInTime;
    }

    @Override
    public String toString() {
        return super.toString() + " yearsback: " + yearsBackInTime;
    }

    public RuleType getRuleType() {
        throw new RuntimeException("Not implemented yet");
    }

    public String getMessageKey() {
        return "hint.repetitionfamily";
    }

}