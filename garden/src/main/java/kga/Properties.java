package kga;

import java.util.HashMap;
import java.util.Map;

public class Properties {

  /**
   * Family is the is the species family in the biological classification.
   */
  private static final Integer FAMILY = 8032;
  /**
   * The scientific name of the species.
   */
  private static final Integer SIENTIFIC_NAME = 8031;

  private Map<Integer, Object> properties = new HashMap<Integer, Object>();

  public String getName() {
    return (String) properties.get(SIENTIFIC_NAME);
  }

  public void setName(String name) {
    properties.put(SIENTIFIC_NAME, name);
  }

  public Family getFamily() {
    return (Family) properties.get(FAMILY);
  }

  public void setFamily(Family family) {
    properties.put(FAMILY, family);
  }

}
