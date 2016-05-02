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
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestExceptionResolver implements HandlerExceptionResolver, Ordered {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ModelAndView jsonUnknownErrorView;

    @PostConstruct
    public void init() {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        final Object[] modelObject = {new ObjectError("unknown-error", "msg.unknownerror")};
        this.jsonUnknownErrorView = new ModelAndView(view);
        this.jsonUnknownErrorView.addObject(modelObject);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof AccessDeniedException) {
            return getModelAndView(response, HttpStatus.FORBIDDEN);
        }

        final String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (request.getRequestURI().startsWith("/rest/") || uri != null && uri.startsWith("/rest/")) {
            return getModelAndView(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (handler == null) {
            return null;
        }

        try {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final boolean annotatedWithRestController = handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
            final boolean annotatedWithResponseBody = handlerMethod.getMethodAnnotation(ResponseBody.class) != null;
            if (annotatedWithResponseBody || annotatedWithRestController) {
                return getModelAndView(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Failed to return object error. Handler:" + handler, ex);
        }
        return null;
    }

    private ModelAndView getModelAndView(HttpServletResponse response, HttpStatus httpStatus) {
        if (response.getStatus() == 200) {
            response.setStatus(httpStatus.value());
        }
        return jsonUnknownErrorView;
    }

    @Override
    public int getOrder() {
        return -10;//before springs default ExceptionResolvers
    }
}
