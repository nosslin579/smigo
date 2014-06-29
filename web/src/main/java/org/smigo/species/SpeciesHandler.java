package org.smigo.species;

import org.smigo.CurrentUser;
import org.smigo.JspMessageFunctions;
import org.smigo.formbean.SpeciesFormBean;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
public class SpeciesHandler {

    private static final String DEFAULTICONNAME = "defaulticon.png";

    @Autowired
    private DatabaseResource databaseresource;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private UserSession userSession;

    public int addSpecies(SpeciesFormBean speciesFormBean) {
        int id = databaseresource.addSpecies(speciesFormBean, currentUser.getId());
        userSession.reloadSpecies();
        userSession.getTranslation().put(JspMessageFunctions.species(id), speciesFormBean.getVernacularName());
        return id;
    }


    public String getIconFileName(String iconFileName) {
        return iconFileName == null ? DEFAULTICONNAME : iconFileName;
    }

    public String createIconFileName(int userId, int speciesId, CommonsMultipartFile uploadedIcon) {
        if (uploadedIcon == null || uploadedIcon.isEmpty()) {
            return null;
        } else {
            return "u" + userId + "s" + speciesId + "." + uploadedIcon.getContentType().replace("image/", "");
        }
    }
}
