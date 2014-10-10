package org.smigo.species;

import org.smigo.SpeciesView;

import java.util.List;
import java.util.Locale;

interface SpeciesDao {

    int addSpecies(SpeciesFormBean species, int id);

    List<SpeciesView> getDefaultSpecies(Locale locale);

    List<SpeciesView> getUserSpecies(int userId, Locale locale);

    SpeciesView getSpecies(int id, Locale english);

    void setSpeciesTranslation(int id, String vernacularName, String locale);

    List<SpeciesView> searchSpecies(String query, Locale locale);
}
