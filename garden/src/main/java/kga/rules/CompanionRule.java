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
 * This rule gives a hint when planting a beneficial plant next to each other.
 * For instance onion next to carrot.
 *
 * @author Christian Nilsson
 */

public class CompanionRule extends AbstractRule implements Rule {
    private Species friend;

    public CompanionRule(int id, Species host, RuleType ruleType, Species friend, String hintMessageKey) {
        super(id, host, ruleType, hintMessageKey);
        if (friend == null) {
            throw new RuleException("Friend can not be null, id:" + id);
        }
        this.friend = friend;
    }

    public Hint getHint(Square target) {
        for (Square s : target.getSurroundingSquares()) {
            if (s.containsSpecies(friend.getId())) {
                final String[] messageKeyArguments = {friend.getMessageKey()};
                return new Hint(s, target, getHintMessageKey(), messageKeyArguments, friend);
            }
        }
        return null;
    }

}
