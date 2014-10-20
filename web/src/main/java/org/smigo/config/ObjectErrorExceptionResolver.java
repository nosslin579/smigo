package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ObjectErrorExceptionResolver implements HandlerExceptionResolver, PriorityOrdered {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            if (handler != null && ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class) != null) {
                response.setStatus(500);
                final Object[] modelObject = {new ObjectError("unknown-error", "msg.unknownerror")};
                return new ModelAndView("object-error.jsp", "errors", modelObject);
            }
        } catch (Exception e) {
            log.error("Failed to return object error. Handler:" + handler, ex);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
