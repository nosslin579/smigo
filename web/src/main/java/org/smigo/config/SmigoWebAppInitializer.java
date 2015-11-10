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
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Enumeration;
import java.util.Map;

public class SmigoWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        super.beforeSpringSecurityFilterChain(servletContext);
        log.info("Starting servlet context");
        log.info("contextName: " + servletContext.getServletContextName());
        log.info("contextPath:" + servletContext.getContextPath());
        log.info("effectiveMajorVersion:" + servletContext.getEffectiveMajorVersion());
        log.info("effectiveMinorVersion:" + servletContext.getEffectiveMinorVersion());
        log.info("majorVersion:" + servletContext.getMajorVersion());
        log.info("minorVersion:" + servletContext.getMinorVersion());
        log.info("serverInfo:" + servletContext.getServerInfo());
//               ", virtualServerName:" + servletContext.getVirtualServerName() +
        log.info("toString:" + servletContext.toString());

        for (Enumeration<String> e = servletContext.getAttributeNames(); e.hasMoreElements(); ) {
            log.info("Attribute:" + e.nextElement());
        }
        for (Map.Entry<String, String> env : System.getenv().entrySet()) {
            log.info("System env:" + env.toString());
        }
        for (Map.Entry<Object, Object> prop : System.getProperties().entrySet()) {
            log.info("System prop:" + prop.toString());
        }

        final String profile = System.getProperty("spring.profiles.active");
        if (profile == null) {
            throw new IllegalArgumentException("System property spring.profiles.active not set.");
        }
        log.info("Starting with profile " + profile);

        WebApplicationContext context = new AnnotationConfigWebApplicationContext() {{
            register(SmigoWebMvcConfiguration.class);
            setDisplayName("SomeRandomName");
        }};


        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("CharacterEncodingFilter", new CharacterEncodingFilter());
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");

        servletContext.addListener(new RequestContextListener());
        servletContext.addListener(new ContextLoaderListener(context));


        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.addMapping("/");
    }
}
