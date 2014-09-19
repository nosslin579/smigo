package org.smigo.species;

import kga.Family;
import org.smigo.SpeciesView;

import java.util.List;
import java.util.Map;

interface SpeciesDao {

    int addSpecies(SpeciesFormBean species, int id);

    List<SpeciesView> getSpecies(Map<Integer, Family> familyMap);

    SpeciesView getSpecies(int id);
}
