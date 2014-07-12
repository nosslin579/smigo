package org.smigo.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;

class CommonsMultipartFileImageValidator implements
        ConstraintValidator<CommonsMultipartFileImage, CommonsMultipartFile> {
    private static final Logger log = LoggerFactory.getLogger(CommonsMultipartFileImageValidator.class);
    private long height;
    private long width;

    public void initialize(CommonsMultipartFileImage constraintAnnotation) {
        height = constraintAnnotation.height();
        width = constraintAnnotation.width();
    }

    public boolean isValid(CommonsMultipartFile file, ConstraintValidatorContext constraintContext) {
        log.debug("Validating:" + file.getOriginalFilename() + " contenttype:"
                + file.getContentType() + " storagedescrip:" + file.getStorageDescription());
        if (file.isEmpty())
            return true;
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image.getHeight() == height && image.getWidth() == width)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}