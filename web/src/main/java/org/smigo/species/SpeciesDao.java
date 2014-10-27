package org.smigo.species;

import kga.PlantData;
import org.smigo.SpeciesView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

interface SpeciesDao {

    int addSpecies(SpeciesFormBean species, int id);

    List<SpeciesView> getDefaultSpecies(Locale locale);

    List<SpeciesView> getUserSpecies(int userId, Locale locale);

    SpeciesView getSpecies(int id, Locale english);

    void setSpeciesTranslation(int id, String vernacularName, String language, String country);

    List<SpeciesView> searchSpecies(String query, Locale locale);

    List<SpeciesView> getSpeciesFromList(List<PlantData> plants, Locale locale);

    Map<String, String> getSpeciesTranslation(Locale locale);
}
