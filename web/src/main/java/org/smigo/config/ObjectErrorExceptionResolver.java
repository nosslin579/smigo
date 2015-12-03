package org.smigo.config;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ObjectErrorExceptionResolver implements HandlerExceptionResolver, PriorityOrdered {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        final String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (uri != null && uri.startsWith("/rest/")) {
            return getModelAndView(response);
        }

        if (handler == null) {
            return null;
        }

        try {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final boolean annotatedWithRestController = handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
            final boolean annotatedWithResponseBody = handlerMethod.getMethodAnnotation(ResponseBody.class) != null;
            if (annotatedWithResponseBody || annotatedWithRestController) {
                return getModelAndView(response);
            }
        } catch (Exception e) {
            log.error("Failed to return object error. Handler:" + handler, ex);
        }
        return null;
    }

    private ModelAndView getModelAndView(HttpServletResponse response) {
        response.setStatus(500);
        final Object[] modelObject = {new ObjectError("unknown-error", "msg.unknownerror")};
        return new ModelAndView("object-error.jsp", "errors", modelObject);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
