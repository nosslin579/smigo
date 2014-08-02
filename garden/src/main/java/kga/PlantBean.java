package kga;

public class PlantBean implements PlantData {

    private final Plant plant;

    public PlantBean(Plant plant) {
        this.plant = plant;
    }

    public int getSpeciesId() {
        return plant.getSpecies().getId();
    }

    public int getX() {
        return plant.getLocation().getX();
    }

    public int getY() {
        return plant.getLocation().getY();
    }

    public int getYear() {
        return plant.getLocation().getYear();
    }

}
