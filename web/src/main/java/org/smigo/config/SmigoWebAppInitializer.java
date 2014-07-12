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

public class SmigoWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        super.beforeSpringSecurityFilterChain(servletContext);
        log.info("Starting servlet context" +
                ", contextName: " + servletContext.getServletContextName() +
                ", contextPath:" + servletContext.getContextPath() +
                ", effectiveMajorVersion:" + servletContext.getEffectiveMajorVersion() +
                ", effectiveMinorVersion:" + servletContext.getEffectiveMinorVersion() +
                ", majorVersion:" + servletContext.getMajorVersion() +
                ", minorVersion:" + servletContext.getMinorVersion() +
                ", serverInfo:" + servletContext.getServerInfo() +
//               ", virtualServerName:" + servletContext.getVirtualServerName() +
                ", toString:" + servletContext.toString());

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