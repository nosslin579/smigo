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

import kga.*;
import kga.errors.RuleException;

/**
 * This rule gives a hint when a species is planted at the same spot within a
 * given period of years. Also supports perennials.
 *
 * @author Christian Nilsson
 */
public class PerennialRepetitionRule extends AbstractRule {
    /**
     * Years between the plant is planted at the same location. Gap is usually 4 years.
     */
    private int gap = 4;

    /**
     * How many years a perennial can be cultivated at the same spot. For annuals this equals 0.
     */
    private int allowedRepetitions = 0;
    private Species host;

    public PerennialRepetitionRule(int id, Species host, String hintMessageKey, int gap, int allowedRepetitions) {
        super(id, host, RuleType.speciesrepetition, hintMessageKey);
        if (gap <= allowedRepetitions)
            throw new RuleException("Gap must be greater than allowed repetitions");
        this.host = host;
        this.gap = gap;
        this.allowedRepetitions = allowedRepetitions;
    }

    public int getGap() {
        return gap;
    }

    public int getAllowedRepetitions() {
        return allowedRepetitions;
    }

    @Override
    public Hint getHint(Plant affected, Garden g) {
        // Getting squares way back
        for (Square s : g.getPreviousSurroundingSquares(affected.getLocation(), gap + allowedRepetitions, Rule.CLOSEST_NEIGHBOURS)) {
            for (Plant causing : s.getPlants().values()) {
                // Checking if squares contain host but not the squares within the allowed repetitions
                int year = s.getLocation().getYear();
                if ((year - allowedRepetitions) > year && affected.getSpecies().getId() == host.getId()) {
                    return Hint.createHint(affected, causing, getHintMessageKey(), null);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " years back: " + gap;
    }

    @Override
    public RuleType getRuleType() {
        throw new RuntimeException("Not implemented yet");
    }

}