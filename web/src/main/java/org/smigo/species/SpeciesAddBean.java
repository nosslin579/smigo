package org.smigo.species;

import org.smigo.user.authentication.UserMustBeAuthenticated;

import javax.validation.constraints.Size;


@UserMustBeAuthenticated
public class SpeciesAddBean {
    @Size(min = 2, max = 40, message = "msg.minandmaxlength")
    private String vernacularName;

    public String getVernacularName() {
        return vernacularName;
    }

    public void setVernacularName(String vernacularName) {
        this.vernacularName = vernacularName;
    }
}
