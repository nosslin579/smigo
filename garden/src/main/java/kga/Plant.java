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
public class Plant implements PlantData{
  private Species species;
  private Square square;

  public Plant(Species species, Square square) {
    this.square = square;
    this.species = species;
  }

  /**
   * Return the species this plant based on
   */
  public Species getSpecies() {
    return species;
  }

  /**
   * Returns the square this plant resides in
   */
  public Square getSquare() {
    return square;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Plant) {
      Plant comp = (Plant) obj;
      return (comp.getSpecies().equals(species) && comp.getSquare().equals(square));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((37 + species.getId()) + 37) * square.hashCode();
  }

  /**
   * Returns the hints for this plant. The collection does not contain any
   * null elements.
   *
   * @return the hints for this square
   */
  public List<Hint> getHints() {
    List<Hint> hints = new ArrayList<Hint>();
    for (Rule r : species.getRules())
      hints.add(r.getHint(square));
    // removing all null elements
    hints.removeAll(Collections.singleton(null));
    return hints;

  }

  public List<Hint> getCompanionPlantingHints() {
    List<Hint> hints = new ArrayList<Hint>();
    for (Rule r : species.getRules(Rule.COMPANION_PLANTING_RULES))
      hints.add(r.getHint(square));
    // removing all null elements
    hints.removeAll(Collections.singleton(null));
    return hints;
  }

  public List<Hint> getCropRotationHints() {
    List<Hint> hints = new ArrayList<Hint>();
    for (Rule r : species.getRules(Rule.CROP_ROTATION_RULES))
      hints.add(r.getHint(square));
    // removing all null elements
    hints.removeAll(Collections.singleton(null));
    return hints;
  }

    @Override
    public int getSpeciesId() {
        return species.getId();
    }

    @Override
    public int getX() {
        return square.getX();
    }

    @Override
    public int getY() {
        return square.getY();
    }

    @Override
    public int getYear() {
        return square.getYear();
    }
}
