package org.smigo.propertyeditors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sourceforge.kga.Family;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

//todo inline this method
public final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {
  private static final Logger log = LoggerFactory.getLogger(CustomPropertyEditorRegistrar.class);

  @Override
  public void registerCustomEditors(PropertyEditorRegistry registry) {
    // log.debug("Registering custom property editors");
    // it is expected that new PropertyEditor instances are created
    registry.registerCustomEditor(Family.class, new FamilyPropertyEditor());

    // you could register as many custom property editors as are required
    // here...
  }
}