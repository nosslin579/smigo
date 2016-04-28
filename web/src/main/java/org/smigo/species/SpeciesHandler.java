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
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SpeciesHandler {
    public static final String REVIEW_REQUEST = "review request";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailHandler mailHandler;
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
        Vernacular vernacular = new Vernacular();
        vernacular.setCountry(locale.getCountry());
        vernacular.setLanguage(locale.getLanguage());
        vernacular.setVernacularName(vernacularName);
        vernacular.setSpeciesId(id);
        speciesDao.insertVernacular(vernacular);
        return id;
    }

    public void deleteSpecies(int speciesId) {
//        speciesDao.deleteVernacular(speciesId);
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
        List<Species> ret = speciesDao.getDefaultSpecies();
        return addVernacular(ret, locale);
    }

    public Species getSpecies(int id, Locale locale) {
        Species species = speciesDao.getSpecies(id);
        return addVernacular(Collections.singletonList(species), locale).get(0);
    }

    public List<Species> searchSpecies(String query, Locale locale) {
        //todo add search on translated family
        List<Species> ret = speciesDao.searchSpecies(query, locale);
        return addVernacular(ret, locale);
    }

    private List<Species> addVernacular(List<Species> speciesList, Locale locale) {
        List<Vernacular> vernaculars = speciesDao.getVernacular(locale);
        for (Species species : speciesList) {
            List<Vernacular> vernacularForSpecies = vernaculars.stream().filter(v -> v.getSpeciesId() == species.getId()).collect(Collectors.toList());
            species.setVernaculars(vernacularForSpecies);
        }
        return speciesList;
    }

    public List<Rule> getRules() {
        return ruleDao.getRules();
    }

    public CrudResult addVernacular(Vernacular vernacular, AuthenticatedUser user, Locale locale) {
        Species species = getSpecies(vernacular.getSpeciesId(), locale);
        boolean isMod = user.isModerator();
        boolean isCreator = species.getCreator() == user.getId();
        if (isCreator || isMod) {
            vernacular.setLanguage(locale.getLanguage());
            vernacular.setCountry(locale.getCountry());
            speciesDao.insertVernacular(vernacular);
            //http://stackoverflow.com/questions/27547519/most-efficient-way-to-get-the-last-element-of-a-stream
            Vernacular added = speciesDao.getVernacular(locale).stream().reduce((a, b) -> b).get();
            return new CrudResult(added.getId(), Review.NONE);
        }
        List<Vernacular> currentVernaculars = speciesDao.getVernacular(locale);
        currentVernaculars.removeIf(v -> v.getSpeciesId() != vernacular.getSpeciesId());
        String vernacularAsString = currentVernaculars.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        mailHandler.sendAdminNotification(REVIEW_REQUEST, "Add vernacular:" + vernacular + " to:" +
                currentVernaculars + " with locale:" + locale + System.lineSeparator() + "Request made by:" + user.toString());
        return new CrudResult(null, Review.MODERATOR);
    }

    public Review updateSpecies(int speciesId, Species updatedSpecies, AuthenticatedUser user) {
        Species originalSpecies = getSpecies(speciesId, Locale.ENGLISH);
        log.info("Updating species " + speciesId + originalSpecies + updatedSpecies, user);
        boolean isMod = user.isModerator();
        boolean isCreator = originalSpecies.getCreator() == user.getId();
        boolean isNewFamily = Objects.equals(Family.NEW_FAMILY, updatedSpecies.getFamilyId());
        if ((isCreator || isMod) && !isNewFamily) {
            speciesDao.updateSpecies(speciesId, updatedSpecies);
            return Review.NONE;
        }
        String text = "Species change." + System.lineSeparator() +
                "From: " + originalSpecies + System.lineSeparator() +
                "To: " + updatedSpecies + " " + System.lineSeparator() +
                "SpeciesId: " + speciesId + System.lineSeparator() +
                "UserId: " + user.getId() + " - " + user.getUsername() + System.lineSeparator() +
                "UPDATE SPECIES SET FAMILY_ID = " + updatedSpecies.getFamilyId() + " WHERE ID=" + speciesId + ";" + System.lineSeparator() +
                "UPDATE SPECIES SET SCIENTIFIC_NAME = '" + updatedSpecies.getScientificName() + "' WHERE ID=" + speciesId + ";" + System.lineSeparator() +
                "UPDATE SPECIES SET ICONFILENAME = '" + updatedSpecies.getIconFileName() + "' WHERE ID=" + speciesId + ";";
        mailHandler.sendAdminNotification(REVIEW_REQUEST, text);
        return Review.MODERATOR;
    }

    public List<Vernacular> getVernacular(Locale locale) {
        return speciesDao.getVernacular(locale);
    }

    public Review deleteVernacular(int vernacularId, AuthenticatedUser user, Locale locale) {
        boolean isMod = user.isModerator();
        if (isMod) {
            speciesDao.deleteVernacular(vernacularId);
            return Review.NONE;
        }
        List<Vernacular> vernacularList = speciesDao.getVernacular(locale);
        String text = "Delete vernacular " + vernacularId + System.lineSeparator() +
                vernacularList.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        mailHandler.sendAdminNotification(REVIEW_REQUEST, text);
        return Review.MODERATOR;
    }
}