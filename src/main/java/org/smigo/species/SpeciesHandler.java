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
import org.smigo.plants.Plant;
import org.smigo.plants.PlantHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Autowired
    private PlantHandler plantHandler;

    public int addSpecies(Species vernacularName, AuthenticatedUser user, Locale locale) {
        return speciesDao.addSpecies(user.getId());
    }

    public void deleteSpecies(int deleteId, int replaceId) {
        if (replaceId != 0) {
            List<Plant> restore = plantHandler.getPlants(deleteId);
            mailHandler.sendAdminNotification("plants replaced", "Old:" + deleteId + " New:" + replaceId + System.lineSeparator() + restore.toString());
            plantHandler.replaceSpecies(deleteId, replaceId);
        }
        speciesDao.deleteSpecies(deleteId);
    }


    public List<Species> getDefaultSpecies() {
        return speciesDao.getDefaultSpecies();
    }

    public Species getSpecies(int id) {
        return speciesDao.getSpecies(id);
    }

    public List<Species> searchSpecies(String query, Locale locale) {
        //todo add search on translated family
        List<Species> species = speciesDao.searchSpecies(query, locale);
        if (species.isEmpty()) {
            return speciesDao.searchSpecies(query, Locale.ENGLISH);
        }
        return species;
    }

    public List<Rule> getRules() {
        return ruleDao.getRules();
    }

    public Review updateSpecies(int speciesId, Species updatedSpecies, AuthenticatedUser user) {
        Species originalSpecies = getSpecies(speciesId);
        //these values are not editable and also this will make the review request less confusing
        updatedSpecies.setId(originalSpecies.getId());
        updatedSpecies.setAnnual(originalSpecies.isAnnual());
        updatedSpecies.setCreator(originalSpecies.getCreator());

        log.info("Updating species " + speciesId + originalSpecies + updatedSpecies, user);

        boolean isMod = user.isModerator();
        boolean isCreator = originalSpecies.getCreator() == user.getId();
        boolean isNewFamily = Objects.equals(Family.NEW_FAMILY, updatedSpecies.getFamilyId());
        if ((isCreator || isMod) && !isNewFamily) {
            speciesDao.updateSpecies(updatedSpecies);
            return Review.NONE;
        }

        mailHandler.sendReviewRequest("Update species", Arrays.asList(originalSpecies), updatedSpecies, user);
        return Review.MODERATOR;
    }


}