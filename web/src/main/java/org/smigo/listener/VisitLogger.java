package org.smigo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor that logs non static requests
 *
 * @author Christian Nilsson
 */
public class VisitLogger extends HandlerInterceptorAdapter {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private DatabaseResource dbr;

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) throws Exception {
    try {
      String requestedURL = request.getRequestURL().toString();
      if (requestedURL.endsWith(".js") || requestedURL.endsWith(".png")
            || requestedURL.endsWith(".css"))
        log.debug("Not logging " + requestedURL);
      else {
        dbr.logVisit(request, "");
      }
    } catch (Exception e) {
      log.error("Could not log visit " + request.getRequestURI(), e);
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response,
                         Object handler, ModelAndView modelAndView) throws Exception {
    super.postHandle(request, response, handler, modelAndView);
    // log.debug("Sending response " + response);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
    // log.info("Recieving request " + request + ", url " +
    // request.getRequestURI());
    return super.preHandle(request, response, handler);
  }

}
