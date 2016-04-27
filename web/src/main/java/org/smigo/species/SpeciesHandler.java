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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
        speciesDao.insertVernacular(id, vernacularName, locale, true);
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
        Map<Integer, String> vernacularOther = speciesDao.getVernacularOther(locale.getLanguage());
        List<Species> ret = speciesDao.getDefaultSpecies();
        ret.forEach(s -> s.setVernacularOther(vernacularOther.get(s.getId())));
        return ret;
    }

    public Species getSpecies(int id, Locale locale) {
        Species ret = speciesDao.getSpecies(id);
        ret.setVernacularOther(speciesDao.getVernacularOther(locale.getLanguage()).get(id));
        return ret;
    }

    public List<Species> searchSpecies(String query, Locale locale) {
        //todo add search on translated family
        Map<Integer, String> vernacularOther = speciesDao.getVernacularOther(locale.getLanguage());
        List<Species> ret = speciesDao.searchSpecies(query, locale);
        ret.forEach(s -> s.setVernacularOther(vernacularOther.get(s.getId())));
        return ret;
    }

    public Map<String, String> getVernacular(Locale locale) {
        final Map<String, String> ret = speciesDao.getVernacular("en", "");
        final Map<String, String> language = speciesDao.getVernacular(locale.getLanguage(), "");
        final Map<String, String> country = speciesDao.getVernacular(locale.getLanguage(), locale.getCountry());
        ret.putAll(language);
        ret.putAll(country);
        return ret;
    }

    public List<Rule> getRules() {
        return ruleDao.getRules();
    }

    public Review addVernacular(Vernacular name, int speciesId, AuthenticatedUser user, Locale locale) {
        Species species = getSpecies(speciesId, locale);
        boolean isMod = user.isModerator();
        boolean isCreator = species.getCreator() == user.getId();
        if (isCreator || isMod) {
            speciesDao.insertVernacular(speciesId, name.getVernacularName(), locale, name.isPrimary());
            return Review.NONE;
        }
        Map<Locale, String> vernacular = speciesDao.getVernacular(speciesId);
        mailHandler.sendAdminNotification(REVIEW_REQUEST, "Add vernacular:" + name + " to:" + vernacular + " with locale:" + locale + System.lineSeparator() + "Request made by:" + user.toString());
        return Review.MODERATOR;
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

    public List<Vernacular> getVernacular2(Locale locale) {
        return speciesDao.getVernacular(locale);
    }

    public Review deleteVernacular(int vernacularId, AuthenticatedUser user) {
//        Species species = getSpecies(speciesId, locale);
        boolean isMod = user.isModerator();
        boolean isCreator = false;//species.getCreator() == user.getId();
        if (isCreator || isMod) {
            speciesDao.deleteVernacular(vernacularId);
            return Review.NONE;
        }
        String text = "Delete vernacular ";
        mailHandler.sendAdminNotification(REVIEW_REQUEST, text);
        return null;
    }
}