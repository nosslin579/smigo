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
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
public class SpeciesHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private FamilyDao familyDao;

    public int addSpecies(Species vernacularName, AuthenticatedUser user, Locale locale) {
        return speciesDao.addSpecies(user.getId());
    }

    public void deleteSpecies(int speciesId) {
        speciesDao.deleteSpecies(speciesId);
    }

    public String createIconFileName(int userId, int speciesId, CommonsMultipartFile uploadedIcon) {
        if (uploadedIcon == null || uploadedIcon.isEmpty()) {
            return null;
        } else {
            return "u" + userId + "s" + speciesId + "." + uploadedIcon.getContentType().replace("image/", "");
        }
    }

    public List<Species> getDefaultSpecies(Locale locale) {
        return speciesDao.getDefaultSpecies();
    }

    public Species getSpecies(int id, Locale locale) {
        return speciesDao.getSpecies(id);
    }

    public List<Species> searchSpecies(String query, Locale locale) {
        //todo add search on translated family
        return speciesDao.searchSpecies(query, locale);
    }

    public List<Rule> getRules() {
        return ruleDao.getRules();
    }

    public Review updateSpecies(int speciesId, Species updatedSpecies, AuthenticatedUser user) {
        Species originalSpecies = getSpecies(speciesId, Locale.ENGLISH);
        updatedSpecies.setId(originalSpecies.getId());
        updatedSpecies.setAnnual(originalSpecies.isAnnual());
        updatedSpecies.setCreator(originalSpecies.getCreator());

        log.info("Updating species " + speciesId + originalSpecies + updatedSpecies, user);

        boolean isMod = user.isModerator();
        boolean isCreator = originalSpecies.getCreator() == user.getId();
        boolean isNewFamily = Objects.equals(Family.NEW_FAMILY, updatedSpecies.getFamilyId());
        if ((isCreator || isMod) && !isNewFamily) {
            speciesDao.updateSpecies(speciesId, updatedSpecies);
            return Review.NONE;
        }

        mailHandler.sendReviewRequest("Update species", Arrays.asList(originalSpecies), updatedSpecies, user);
        return Review.MODERATOR;
    }


}