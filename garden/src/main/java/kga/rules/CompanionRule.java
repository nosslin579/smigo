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
 * This rule gives a hint when planting a two plant next to each other. Good or bad.
 *
 * @author Christian Nilsson
 */

public class CompanionRule extends AbstractRule implements Rule {
    private Species companion;

    public CompanionRule(int id, Species host, RuleType ruleType, Species companion, String hintMessageKey) {
        super(id, host, ruleType, hintMessageKey, companion);
        if (companion == null) {
            throw new RuleException("Friend can not be null, id:" + id);
        }
        this.companion = companion;
    }

    @Override
    public Hint getHint(Plant affected, Garden garden) {
        for (Square s : garden.getSurroundingSquares(affected.getLocation())) {
            for (Plant causing : s.getPlants().values()) {
                if (causing.getSpecies().getId() == companion.getId()) {
                    final String[] messageKeyArguments = {String.valueOf(getHost().getId()), String.valueOf(companion.getId())};
                    return Hint.createHint(affected, causing, getHintMessageKey(), messageKeyArguments);
                }
            }
        }
        return null;
    }

}
