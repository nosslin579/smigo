package org.smigo.propertyeditors;


import org.smigo.persitance.UserSession;
import org.sourceforge.kga.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class SpeciesPropertyEditor extends PropertyEditorSupport {
  private UserSession userSession;


  @Autowired
  public void setUserSession(UserSession userSession) {
    this.userSession = userSession;
  }

  @Override
  public String getAsText() {
    Object value = getValue();
    if (value instanceof Species) {
      return String.valueOf(((Species) value).getId());
    } else {
      return super.getAsText();
    }
  }

  @Override
  public void setAsText(final String text) throws IllegalArgumentException {
    if (StringUtils.hasText(text)) {
      Integer id = Integer.valueOf(text);
      setValue(id == 0 ? null : userSession.getSpecies(id));
    } else {
      throw new IllegalArgumentException("Cannot convert text '" + text + "', into a species");
    }
  }
}
