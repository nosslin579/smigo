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

    List<Species> getUserSpecies(int userId);

    Map<Locale, String> getSpeciesTranslation(int speciesId);

    Species getSpecies(int id);

    void insertSpeciesTranslation(int id, String vernacularName, Locale locale);

    List<Species> searchSpecies(String query, Locale locale);

    Map<String, String> getSpeciesTranslation(Locale locale);

    void setSpeciesTranslation(int id, String vernacularName, Locale locale);

    void updateSpecies(int id, Species species);
}
