package org.smigo;

public class JspMessageFunctions {

    public static String species(SpeciesView species) {
        return "species" + species.getId();
    }
}
