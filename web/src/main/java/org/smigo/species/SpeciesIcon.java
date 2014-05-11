package org.smigo.species;

import org.smigo.constraints.CommonsMultipartFileImage;
import org.smigo.constraints.CommonsMultipartFileMimeType;
import org.smigo.constraints.CommonsMultipartFileSize;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class SpeciesIcon {
  @CommonsMultipartFileImage(height = 48, width = 48)
  @CommonsMultipartFileMimeType(mimeTypes = {"image/png"})
  @CommonsMultipartFileSize(maxSixe = 10)
  private CommonsMultipartFile uploadedIcon;


}
