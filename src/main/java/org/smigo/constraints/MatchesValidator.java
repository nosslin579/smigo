package org.smigo.constraints;

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


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class MatchesValidator implements ConstraintValidator<Matches, Object> {
    private static final Logger log = LoggerFactory.getLogger(MatchesValidator.class);

    private String field;
    private String verifyField;

    public void initialize(Matches constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.verifyField = constraintAnnotation.verifyField();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        try {
            String verifyFieldObj = BeanUtils.getProperty(value, verifyField);
            String fieldObj = BeanUtils.getProperty(value, field);
//      log.debug("isValid " + value + " field:" + fieldObj + " verifyfield:" + verifyFieldObj);
            if (fieldObj != null && verifyFieldObj != null && fieldObj.equals(verifyFieldObj))
                return true;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("notmatching").addNode(verifyField)
                    .addConstraintViolation();
            return false;
        } catch (Exception e) {
            log.error("Validating fieldmatches faild", e);
        }

        return false;
    }
}
