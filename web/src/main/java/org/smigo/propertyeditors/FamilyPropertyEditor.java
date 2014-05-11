package org.smigo.propertyeditors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sourceforge.kga.Family;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class FamilyPropertyEditor extends PropertyEditorSupport {
  private static final Logger log = LoggerFactory.getLogger(FamilyPropertyEditor.class);

  public FamilyPropertyEditor() {
  }

  @Override
  public String getAsText() {
    Object value = getValue();
    if (value instanceof Family) {
      return String.valueOf(((Family) value).getId());
    } else {
      return super.getAsText();
    }
  }

  @Override
  public void setAsText(final String text) throws IllegalArgumentException {
//		log.debug("Converting " + text + " to Family");
    if (StringUtils.hasText(text)) {
      Integer id = Integer.valueOf(text);
      setValue(id == 0 ? null : new Family("", id));
    } else {
      throw new IllegalArgumentException("Cannot convert text '" + text + "', into a family");
    }
  }
}
