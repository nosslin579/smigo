package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class CrawlFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        HttpServletRequest request = (HttpServletRequest) req;
        String queryString = request.getQueryString();
        if ((queryString != null) && (queryString.contains("_escaped_fragment_"))) {
            log.info("_escaped_fragment_ in url detected" + queryString);
            // use the headless browser to obtain an HTML snapshot

/*
            WebDriver d = new FirefoxDriver();
            d.get(request.getRequestURL().toString());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(d.getPageSource());
            out.close();
            d.quit();
*/
        } else {
            filterChain.doFilter(request, response);
        }
    }

}