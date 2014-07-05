package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.listener.VisitLogger;
import org.smigo.propertyeditors.CustomPropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.smigo"})
public class SmigoWebMvcConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/beta").setViewName("forward:/garden");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/messages/**").addResourceLocations("/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitLogger());
    }


    @Bean
    public VisitLogger visitLogger() {
        return new VisitLogger();
    }

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        final SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        simpleMappingExceptionResolver.setDefaultErrorView("error.jsp");
        simpleMappingExceptionResolver.setDefaultStatusCode(500);
        return simpleMappingExceptionResolver;
    }

    @Bean
    public ViewResolver getViewResolver() {
        log.debug("getViewResolver");
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setExposeContextBeansAsAttributes(true);
        resolver.setExposedContextBeanNames(new String[]{"hostEnvironmentInfo", "viewBean"});
        return resolver;
    }

    @Bean
    public MultipartResolver getMultipartResolver() {
        log.debug("getMultipartResolver");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }

    @Bean
    public CustomEditorConfigurer getCustomEditorConfigurer() {
        log.debug("getCustomEditorConfigurer");
        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
        customEditorConfigurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{new CustomPropertyEditorRegistrar()});
        return customEditorConfigurer;
    }

}
