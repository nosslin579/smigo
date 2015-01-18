package org.smigo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class LogHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogDao logDao;


    public void log(HttpServletRequest request, HttpServletResponse response) {
        String requestedURL = request.getRequestURL().toString();
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

        final long start = (Long) request.getAttribute(VisitLogger.REQUEST_TIMER);
        s.append(" Request finished in " + (System.nanoTime() - start) + "ns which is " + (System.nanoTime() - start) / 1000000 + "ms");
        log.info(s.toString());
        logDao.log(logBean);

    }
}
