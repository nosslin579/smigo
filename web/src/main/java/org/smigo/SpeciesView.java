package org.smigo;

import kga.Family;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;


public class SpeciesView extends kga.Species {
    private static final Logger log = LoggerFactory.getLogger(SpeciesView.class);

    private String scientificName;
    private boolean display;
    private String iconFileName;
    private String vernacularName;

    public SpeciesView() {
    }


    public SpeciesView(int id, String scientificName, boolean item, boolean annual, Family family) {
        super(id, new HashSet<Rule>(), family, annual, item);
        this.scientificName = scientificName.trim();
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public String getMessageKey() {
        return JspFunctions.speciesMessageKey(getId());
    }

    public void setVernacularName(String vernacularName) {
        this.vernacularName = vernacularName;
    }

    public String getVernacularName() {
        return vernacularName;
    }
}