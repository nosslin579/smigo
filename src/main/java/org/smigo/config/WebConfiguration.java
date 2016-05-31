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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.smigo"})
@EnableScheduling
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${databaseUser}")
    private String databaseUser;
    @Value("${databasePassword}")
    private String databasePassword;
    @Value("${databaseUrl}")
    private String databaseUrl;
    @Value("${resourceCachePeriod}")
    private Integer resourceCachePeriod;

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        return JdbcConnectionPool.create(databaseUrl, databaseUser, databasePassword);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(mappingJacksonHttpMessageConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/").setCachePeriod(Integer.MAX_VALUE);
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/WEB-INF/other/").setCachePeriod(Integer.MAX_VALUE);
        registry.addResourceHandler("/google5fc09c8757ef21fa.html").addResourceLocations("/WEB-INF/views/").setCachePeriod(resourceCachePeriod);
        registry.addResourceHandler("/views/*.html").addResourceLocations("/WEB-INF/views/").setCachePeriod(resourceCachePeriod);
        registry.addResourceHandler("/css/*.css").addResourceLocations("/WEB-INF/css/").setCachePeriod(resourceCachePeriod);
        registry.addResourceHandler("/angular/**").addResourceLocations("/WEB-INF/angular/").setCachePeriod(resourceCachePeriod);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/garden-planner-comparison").setViewName("garden-planner-comparison.jsp");
        registry.addViewController("/").setViewName("home.jsp");
        registry.addViewController("/help").setViewName("help.jsp");
        registry.addViewController("/robots.txt").setViewName("robots-txt.jsp");
        registry.addViewController("/sitemap.xml").setViewName("sitemap-xml.jsp");
        registry.addRedirectViewController("/garden-planner.html", "/views/garden-planner.html").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/garden/**", "/garden-planner").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/hasta-luego/**", "/welcome-back").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/beta/**", "/").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/accept-termsofservice/**", "/accept-terms-of-service").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/signup/**", "/register").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/_=_", "/garden-planner").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/wall/{username}/**", "/gardener/{username}").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/tos.html", "/static/terms-of-service.html").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addStatusController("/static/terms-of-service.html", HttpStatus.GONE);
        registry.addStatusController("/listspecies", HttpStatus.GONE);
        registry.addStatusController("/update-species", HttpStatus.GONE);
        registry.addStatusController("/rest/species/search", HttpStatus.GONE);
        registry.addStatusController("/addyear/**", HttpStatus.GONE);
        registry.addStatusController("/deletespecies/**", HttpStatus.GONE);
        registry.addStatusController("/species/**", HttpStatus.GONE);
        registry.addStatusController("/rule/**", HttpStatus.GONE);
        registry.addStatusController("**/*.php", HttpStatus.NOT_FOUND);
        registry.addStatusController("cgi-bin/**", HttpStatus.NOT_FOUND);
        registry.addStatusController("**/*.cgi", HttpStatus.NOT_FOUND);
        registry.addStatusController("/wp/", HttpStatus.NOT_FOUND);
        registry.addStatusController("/wordpress/", HttpStatus.NOT_FOUND);
        registry.addStatusController("/HNAP1/", HttpStatus.NOT_FOUND);
        registry.addStatusController("/blog/robots.txt", HttpStatus.NOT_FOUND);
        registry.addStatusController("/apple-touch-icon-precomposed.png", HttpStatus.NOT_FOUND);
        registry.addStatusController("/apple-touch-icon.png", HttpStatus.NOT_FOUND);
    }

    @Bean
    public ViewResolver getViewResolver() {
        log.debug("getViewResolver");
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setExposeContextBeansAsAttributes(true);
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
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static PropertyOverrideConfigurer propertyOverrideConfigurer() {
        return new PropertyOverrideConfigurer();
    }
}
