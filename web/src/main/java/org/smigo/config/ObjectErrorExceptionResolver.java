package org.smigo.config;

import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@Component
public class ObjectErrorExceptionResolver implements HandlerExceptionResolver, PriorityOrdered {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        final Annotation[] annotations = ((HandlerMethod) handler).getMethod().getAnnotations();
        if (((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class) != null) {
            final Object[] modelObject = {new ObjectError("unknown-error", "msg.unknownerror")};
            response.setStatus(500);
            return new ModelAndView("object-error.jsp", "errors", modelObject);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
