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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rules are applied to species. Rules is the way to tell the system how a
 * species should or shouldn't be used. If you break a warning type rule or
 * follow a benefit type rule you will get a hint.
 *
 * @author Christian Nilsson
 */

public abstract class Rule implements IRule {
    private static final Logger log = LoggerFactory.getLogger(Rule.class);

    public static final int ONE_YEAR_BACK = 1;
    public static final int CLOSEST_NEIGHBOURS = 1;

    public static final Class<?>[] COMPANION_PLANTING_RULES = {
            BeneficialRule.class,
            DisadvantageousRule.class
    };

    public static final Class<?>[] CROP_ROTATION_RULES = {
            BadCropRotationRule.class,
            FamilyRepetitionRule.class,
            GoodCropRotationRule.class,
            PerennialRepetitionRule.class,
            RepetitionRule.class
    };

    private int id;

    private int creatorId;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public int hashCode() {
        return getRuleType().getId();
    }

}
