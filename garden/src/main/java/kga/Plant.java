package kga;

import kga.rules.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Plant represent the physical plant i a garden.
 *
 * @author Christian Nilsson
 */
public class Plant {
    private final Square square;
    private final Species species;

    public Plant(Species species, Square square) {
        this.species = species;
        this.square = square;
    }

    /**
     * Return the species this plant based on
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Returns the hints for this plant. The collection does not contain any
     * null elements.
     *
     * @return the hints for this square
     */
    public List<Hint> getHints(Garden garden) {
        List<Hint> hints = new ArrayList<Hint>();
        for (Rule r : species.getRules())
            hints.add(r.getHint(this, garden));
        // removing all null elements
        hints.removeAll(Collections.singleton(null));
        return hints;

    }

    public Location getLocation() {
        return square.getLocation();
    }
}
