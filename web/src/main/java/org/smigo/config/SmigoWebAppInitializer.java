package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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
        final String profile;
        try {
            InitialContext initialContext = new InitialContext();
            profile = (String) initialContext.lookup("java:comp/env/profile");
        } catch (NamingException e) {
            throw new RuntimeException("Could not find profile", e);
        }

        WebApplicationContext context = new AnnotationConfigWebApplicationContext() {{
            register(SmigoWebMvcConfiguration.class);
            setDisplayName("Nissepisse");
            getEnvironment().setActiveProfiles(profile);
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