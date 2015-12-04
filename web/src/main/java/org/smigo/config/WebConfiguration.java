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
import org.smigo.log.VisitLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.smigo"})
@EnableCaching
@EnableAsync
@EnableScheduling
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${databaseUser}")
    private String databaseUser;
    @Value("${databasePassword}")
    private String databasePassword;
    @Value("${databaseUrl}")
    private String databaseUrl;

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
        registry.setOrder(-1);
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/WEB-INF/other/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/WEB-INF/other/");
        registry.addResourceHandler("/sitemap.xml").addResourceLocations("/WEB-INF/other/");
        registry.addResourceHandler("/*.html").addResourceLocations("/WEB-INF/views/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitLogger());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(-2);
        registry.addViewController("/garden-planner-comparison").setViewName("garden-planner-comparison.jsp");
        registry.addViewController("/").setViewName("home.jsp");
        registry.addViewController("/help").setViewName("help.jsp");
        registry.addViewController("/welcome-back").setViewName("welcome-back.jsp");
        registry.addRedirectViewController("/garden", "/garden-planner").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/hasta-luego", "/welcome-back").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/beta", "/").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/tos.html", "/static/terms-of-service.html").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/accept-termsofservice", "/accept-terms-of-service").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/_=_", "/garden-planner").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addRedirectViewController("/signup", "/register").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        registry.addStatusController("/addyear", HttpStatus.GONE);
        registry.addStatusController("/garden/2014", HttpStatus.GONE);
    }

    @Bean
    public VisitLogger visitLogger() {
        return new VisitLogger();
    }

    @Bean
    public ViewResolver getViewResolver() {
        log.debug("getViewResolver");
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setExposeContextBeansAsAttributes(true);
//        resolver.setExposedContextBeanNames(new String[]{"hostEnvironmentInfo"});
        return resolver;
    }

    @Bean
    public MultipartResolver getMultipartResolver() {
        log.debug("getMultipartResolver");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }

//    @Bean
//    public CustomEditorConfigurer getCustomEditorConfigurer() {
//        log.debug("getCustomEditorConfigurer");
//        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
//        customEditorConfigurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{new CustomPropertyEditorRegistrar()});
//        return customEditorConfigurer;
//    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<ConcurrentMapCache> concurrentMapCacheArrayList = new ArrayList<ConcurrentMapCache>();
        concurrentMapCacheArrayList.add(new ConcurrentMapCache(Cache.RULES));
        concurrentMapCacheArrayList.add(new ConcurrentMapCache(Cache.SPECIES));
        concurrentMapCacheArrayList.add(new ConcurrentMapCache(Cache.FAMILIES));
        concurrentMapCacheArrayList.add(new ConcurrentMapCache(Cache.SPECIES_TRANSLATION));
        concurrentMapCacheArrayList.add(new ConcurrentMapCache(Cache.SPECIES_ID));
        cacheManager.setCaches(concurrentMapCacheArrayList);
        return cacheManager;
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
