package org.smigo.species;

import org.smigo.SpeciesView;

import java.util.List;

public interface SpeciesDao {
    List<SpeciesView> getSpecies();

    int addSpecies(SpeciesFormBean species, int id);
}
