package org.smigo.species;

import kga.Family;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SpeciesFormBean {
    @Length(min = 2, max = 100)
    private String vernacularName;
    @Length(min = 5, max = 255)
    private String scientificName;
    @NotNull
    private Family family;
    private boolean annual = true;

    public String getVernacularName() {
        return vernacularName;
    }

    public void setVernacularName(String vernacularName) {
        this.vernacularName = vernacularName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public boolean isAnnual() {
        return annual;
    }

    public void setAnnual(boolean annual) {
        this.annual = annual;
    }
}
