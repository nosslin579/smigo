package org.smigo.species;

import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.smigo.SpeciesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
public class SpeciesHandler {

  private static final String DEFAULTICONNAME = "defaulticon.png";

  @Autowired
  private DatabaseResource databaseresource;
  @Autowired
  private UserSession userSession;

  public int updateSpecies(SpeciesView speciesView) {
    int id = speciesView.getId();
    User user = userSession.getUser();
    if (id == 0) {
      speciesView.setCreator(user);
      id = databaseresource.addSpecies(speciesView);
    } else {
      databaseresource.updateUserSettingsForSpecies(speciesView, user);
    }
    userSession.reloadSpecies();
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
