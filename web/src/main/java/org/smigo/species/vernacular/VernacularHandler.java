package org.smigo.species.vernacular;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 - 2016 Christian Nilsson
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

import org.smigo.species.CrudResult;
import org.smigo.species.Review;
import org.smigo.species.Species;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class VernacularHandler {

    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private VernacularDao vernacularDao;


    public List<Vernacular> getVernacular(Locale locale) {
        return vernacularDao.getVernacular(locale);
    }

    public CrudResult addVernacular(Vernacular vernacular, AuthenticatedUser user, Locale locale) {
        Species species = speciesHandler.getSpecies(vernacular.getSpeciesId(), locale);
        boolean isMod = user.isModerator();
        boolean isCreator = species.getCreator() == user.getId();
        if (isCreator || isMod) {
            vernacular.setLanguage(locale.getLanguage());
            vernacular.setCountry(locale.getCountry());
            vernacularDao.insertVernacular(vernacular);
            //http://stackoverflow.com/questions/27547519/most-efficient-way-to-get-the-last-element-of-a-stream
            Vernacular added = vernacularDao.getVernacular(locale).stream().reduce((a, b) -> b).get();
            return new CrudResult(added.getId(), Review.NONE);
        }
        List<Vernacular> currentVernaculars = vernacularDao.getVernacular(locale);
        currentVernaculars.removeIf(v -> v.getSpeciesId() != vernacular.getSpeciesId());
        String vernacularAsString = currentVernaculars.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        mailHandler.sendAdminNotification(SpeciesHandler.REVIEW_REQUEST, "Add vernacular:" + vernacular + " to:" +
                currentVernaculars + " with locale:" + locale + System.lineSeparator() + "Request made by:" + user.toString());
        return new CrudResult(null, Review.MODERATOR);
    }

    public Review deleteVernacular(int vernacularId, AuthenticatedUser user, Locale locale) {
        Vernacular vernacular = vernacularDao.getVernacularById(vernacularId);
        Species species = speciesHandler.getSpecies(vernacular.getSpeciesId(), locale);

        boolean isMod = user != null && user.isModerator();
        boolean isCreator = user != null && species.getCreator() == user.getId();
        if (isCreator || isMod) {
            vernacularDao.deleteVernacular(vernacularId);
            return Review.NONE;
        }
        List<Vernacular> vernacularList = vernacularDao.getVernacularBySpecies(species.getId());
        String text = "Delete vernacular: " + vernacular + System.lineSeparator() + System.lineSeparator() + "Other vernaculars of that species:" +
                vernacularList.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                species + System.lineSeparator() + "Request made by user:" + user;
        mailHandler.sendAdminNotification(SpeciesHandler.REVIEW_REQUEST, text);
        return Review.MODERATOR;
    }
}
