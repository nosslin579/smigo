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

import java.util.List;
import java.util.Locale;
import java.util.Map;

interface SpeciesDao {

    int addSpecies(int id);

    List<Species> getDefaultSpecies();

    /**
     * @return a map where key is species id and value is comma separated string of other vernaculars
     */
    Map<Integer, String> getVernacularOther(String language);

    Map<Locale, String> getVernacular(int speciesId);

    Species getSpecies(int id);

    void insertVernacular(int id, String vernacularName, Locale locale, boolean autoGeneratePrecedence);

    List<Species> searchSpecies(String query, Locale locale);

    /**
     * @return a map where key is species message key and value is primary vernacular
     */
    Map<String, String> getVernacular(String language, String country);

    void setVernacular(int id, String vernacularName, Locale locale);

    void updateSpecies(int id, Species species);

    void deleteSpecies(int speciesId);

    void deleteVernacular(int speciesId);
}
