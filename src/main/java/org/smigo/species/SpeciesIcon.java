package org.smigo.species;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.smigo.constraints.CommonsMultipartFileImage;
import org.smigo.constraints.CommonsMultipartFileMimeType;
import org.smigo.constraints.CommonsMultipartFileSize;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

class SpeciesIcon {
    @CommonsMultipartFileImage(height = 48, width = 48)
    @CommonsMultipartFileMimeType(mimeTypes = {"image/png"})
    @CommonsMultipartFileSize(maxSixe = 10)
    private CommonsMultipartFile uploadedIcon;


}
