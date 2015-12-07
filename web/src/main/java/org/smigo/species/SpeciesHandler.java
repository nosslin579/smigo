package org.smigo.species;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SpeciesHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserSession userSession;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private FamilyDao familyDao;

    public int addSpecies(String vernacularName, AuthenticatedUser user, Locale locale) {
        final int id = speciesDao.addSpecies(user.getId());
        speciesDao.setSpeciesTranslation(id, vernacularName, null);
        speciesDao.setSpeciesTranslation(id, vernacularName, locale);
        return id;
    }


    public String createIconFileName(int userId, int speciesId, CommonsMultipartFile uploadedIcon) {
        if (uploadedIcon == null || uploadedIcon.isEmpty()) {
            return null;
        } else {
            return "u" + userId + "s" + speciesId + "." + uploadedIcon.getContentType().replace("image/", "");
        }
    }

    public List<Species> getSpeciesMap() {
        return speciesDao.getDefaultSpecies();
    }

    public Species getSpecies(int id) {
        return speciesDao.getSpecies(id);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
    }

    public List<Species> searchSpecies(String query, Locale locale) {
        if (query.length() >= 5) {
            return speciesDao.searchSpecies('%' + query + '%', locale);
        } else if (query.length() >= 3) {
            return speciesDao.searchSpecies(query + '%', locale);
        } else {
            return speciesDao.searchSpecies(query, locale);
        }
    }

    public Map<String, String> getSpeciesTranslation(Locale locale) {
        return speciesDao.getSpeciesTranslation(locale);
    }

    public List<RuleBean> getRules() {
        return ruleDao.getRules();
    }
}
