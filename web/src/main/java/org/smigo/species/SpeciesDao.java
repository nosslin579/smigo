package org.smigo.species;

import kga.PlantData;
import kga.Species;

import java.util.List;
import java.util.Locale;
import java.util.Map;

interface SpeciesDao {

    int addSpecies(SpeciesFormBean species, int id);

    List<Species> getDefaultSpecies();

    List<Species> getUserSpecies(int userId);

    Species getSpecies(int id);

    void setSpeciesTranslation(int id, String vernacularName, String language, String country);

    List<Species> searchSpecies(String query, Locale locale);

    List<Species> getSpeciesFromList(List<PlantData> plants);

    Map<String, String> getSpeciesTranslation(Locale locale);
}
