package org.smigo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class GeneralEventListener implements ApplicationListener<ApplicationEvent>,
                                               ApplicationContextAware {
  private static final Logger log = LoggerFactory.getLogger(GeneralEventListener.class);
  private ApplicationContext applicationContext;

  @Override
  public void onApplicationEvent(ApplicationEvent appEvent) {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    if (appEvent instanceof ServletRequestHandledEvent) {
      ServletRequestHandledEvent event = (ServletRequestHandledEvent) appEvent;
      // log.debug("ServletRequest = " + event.getRequestUrl());
    } else {
      // log.debug("Event occured " +
      // appEvent.getClass().getSimpleName());
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    log.debug("Setting  applicationContext" + applicationContext);
    this.applicationContext = applicationContext;

  }

}