package org.smigo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class VisitLogger extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String REQUEST_TIMER = "request-timer";
    public static final String NOTE_ATTRIBUTE = "LogVisitNote";

    @Autowired
    private LogDao logDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(REQUEST_TIMER, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        final long start = (Long) request.getAttribute(REQUEST_TIMER);
        log.info("Request finished in " + (System.nanoTime() - start) + "ns which is " + (System.nanoTime() - start) / 1000000 + "ms");
        String requestedURL = request.getRequestURL().toString();
        if (requestedURL.endsWith(".js") || requestedURL.endsWith(".png") || requestedURL.endsWith(".css") || requestedURL.endsWith(".html")) {
            log.debug("Not logging " + requestedURL);
        } else if (request.getDispatcherType() != DispatcherType.FORWARD) {
            LogBean logBean = LogBean.create(request, response);

            StringBuilder s = new StringBuilder("Logging request>");
            s.append(logBean.toString());
            s.append(" Auth type:");
            s.append(request.getAuthType());
            s.append(" Principal:");
            s.append(request.getUserPrincipal());
            s.append(" Headers:");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                s.append(headerName).append("=").append(request.getHeader(headerName)).append(" - ");
            }

            log.info(s.toString());
            logDao.log(logBean);
        }
    }

}
