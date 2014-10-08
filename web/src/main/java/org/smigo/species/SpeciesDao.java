package org.smigo.species;

import kga.Family;
import org.smigo.SpeciesView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

interface SpeciesDao {

    int addSpecies(SpeciesFormBean species, int id);

    List<SpeciesView> getSpecies(Map<Integer, Family> familyMap, Locale locale);

    SpeciesView getSpecies(int id, Locale english);
}
