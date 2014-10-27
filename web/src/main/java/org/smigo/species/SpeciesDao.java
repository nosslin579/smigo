package org.smigo.species;

import kga.PlantData;
import kga.Species;

import java.util.List;
import java.util.Locale;
import java.util.Map;

interface SpeciesDao {

    int addSpecies(SpeciesFormBean species, int id);

    List<Species> getDefaultSpecies(Locale locale);

    List<Species> getUserSpecies(int userId, Locale locale);

    Species getSpecies(int id, Locale english);

    void setSpeciesTranslation(int id, String vernacularName, String language, String country);

    List<Species> searchSpecies(String query, Locale locale);

    List<Species> getSpeciesFromList(List<PlantData> plants, Locale locale);

    Map<String, String> getSpeciesTranslation(Locale locale);
}
