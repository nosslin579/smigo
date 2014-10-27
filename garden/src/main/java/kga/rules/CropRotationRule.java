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
 * This rule gives a hint when rotating crops correct. For instance cabbage
 * after legumes.
 *
 * @author Christian Nilsson
 */

public class CropRotationRule extends AbstractRule {

    private Family family;

    public CropRotationRule(int ruleId, Species host, RuleType ruleType, Family family, String hintMessageKey) {
        super(ruleId, host, ruleType, hintMessageKey, family);
        if (family == null)
            throw new RuleException("Family may not be null");
        this.family = family;

    }

    @Override
    public Hint getHint(Plant affected, Garden garden) {
        for (Square s : garden.getPreviousSquares(affected.getLocation(), Rule.ONE_YEAR_BACK)) {
            for (Plant causing : s.getPlants().values()) {
                if (causing.getSpecies().getFamily().getId() == family.getId()) {
                    return Hint.createHint(affected, causing, getMessageKey(), null);
                }
            }
        }
        return null;

    }

    public Family getFamily() {
        return family;
    }

}
