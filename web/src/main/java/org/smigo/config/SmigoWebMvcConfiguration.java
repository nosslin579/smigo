package org.smigo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.smigo"})
@EnableCaching
@EnableAsync
public class SmigoWebMvcConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${mailSenderUsername}")
    private String mailSenderUsername;
    @Value("${mailSenderPassword}")
    private String mailSenderPassword;
    @Value("${mailSenderHost}")
    private String mailSenderHost;

    @Value("${databaseUser}")
    private String databaseUser;
    @Value("${databasePassword}")
    private String databasePassword;
    @Value("${databaseUrl}")
    private String databaseUrl;

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setUser(databaseUser);
        ds.setPassword(databasePassword);
        ds.setJdbcUrl(databaseUrl);
        ds.setIdleConnectionTestPeriod(160);
        return ds;
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
        registry.addResourceHandler("/*.html").addResourceLocations("/WEB-INF/views/");
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
    public MailSender javaMailSender() {
        log.debug("javaMailSender");
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(mailSenderUsername);
        javaMailSender.setPassword(mailSenderPassword);
        javaMailSender.setHost(mailSenderHost);
        javaMailSender.setPort(587);
        javaMailSender.setProtocol("smtp");
        javaMailSender.setJavaMailProperties(new Properties() {
            {
                setProperty("mail.smtp.starttls.enable", "true");
            }
        });
        return javaMailSender;
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
