package org.smigo;
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

import kga.Family;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.User;

import java.util.HashSet;


/**
 * This represent a species, not the physical plant.
 *
 * @author Christian Nilsson
 */
public class SpeciesView extends kga.Species {
    private static final Logger log = LoggerFactory.getLogger(SpeciesView.class);

    private String scientificName;
    private boolean display;
    private User creator;
    private String iconFileName;

    public SpeciesView() {
    }


    public SpeciesView(int id, String scientificName, boolean item, boolean annual, Family family) {
        super(id, new HashSet<Rule>(), family, annual, item);
        this.scientificName = scientificName.trim();
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getTranslation() {
        throw new UnsupportedOperationException("Remove when done");
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getTranslationKey() {
        return "species" + getId();
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public String getIconFileName() {
        return iconFileName;
    }

}