package org.smigo.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

class CommonsMultipartFileMimeTypeValidator implements
        ConstraintValidator<CommonsMultipartFileMimeType, CommonsMultipartFile> {
    private static final Logger log = LoggerFactory.getLogger(CommonsMultipartFileMimeTypeValidator.class);
    private List<String> mimeTypes;

    public void initialize(CommonsMultipartFileMimeType constraintAnnotation) {
        mimeTypes = Arrays.asList(constraintAnnotation.mimeTypes());
    }

    public boolean isValid(CommonsMultipartFile file, ConstraintValidatorContext constraintContext) {
        log.debug("Validating:" + file.getOriginalFilename() + " contenttype:"
                + file.getContentType() + " storagedescrip:" + file.getStorageDescription());
        return file.isEmpty() || mimeTypes.contains(file.getContentType());
    }
}