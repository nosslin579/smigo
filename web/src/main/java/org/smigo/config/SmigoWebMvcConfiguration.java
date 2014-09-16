package org.smigo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.log.VisitLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.smigo"})
public class SmigoWebMvcConfiguration extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(mappingJacksonHttpMessageConverter());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/beta").setViewName("forward:/garden");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
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
        resolver.setExposedContextBeanNames(new String[]{"hostEnvironmentInfo"});
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
        javaMailSender.setUsername("smigo.org@gmail.com");
        javaMailSender.setPassword("lstN09LLrZZx");
        javaMailSender.setHost("smtp.gmail.com");
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
}
