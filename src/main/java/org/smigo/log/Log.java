package org.smigo.log;

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

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class Log {

    private final String remoteUser;
    private final String url;
    private final String locales;
    private final String useragent;
    private final String referer;
    private final String sessionid;
    private final String method;
    private final String ip;
    private final int httpStatus;
    private final long sessionAge;
    private final String queryString;
    private final String host;

    public Log(String remoteUser, String url, String locales, String useragent, String host,
               String referer, String sessionid, String method, String ip,
               int status, long sessionAge, String queryString) {
        this.remoteUser = remoteUser;
        this.url = url;
        this.locales = locales;
        this.useragent = useragent;
        this.referer = referer;
        this.host = host;
        this.sessionid = sessionid;
        this.method = method;
        this.ip = ip;
        this.httpStatus = status;
        this.sessionAge = sessionAge;
        this.queryString = queryString;
    }

    public static Log create(HttpServletRequest req, HttpServletResponse response) {
        StringBuilder locales = new StringBuilder();
        for (Enumeration<Locale> l = req.getLocales(); l.hasMoreElements(); ) {
            locales.append(l.nextElement().toString()).append(" ");
        }

        final HttpSession session = req.getSession(false);
        final long age = session == null ? 0 : (System.currentTimeMillis() - session.getCreationTime());
        final long ageSeconds = TimeUnit.MILLISECONDS.toSeconds(age);
        String requestedSessionId = StringUtils.isEmpty(req.getRequestedSessionId()) ? null : req.getRequestedSessionId();
        return new Log(truncate(req.getRemoteUser()),
                truncate(getRequestURI(req, response)),
                truncate(locales.toString()),
                truncate(req.getHeader("user-agent")),
                truncate(req.getHeader("host")),
                truncate(req.getHeader("referer")),
                truncate(requestedSessionId),
                req.getMethod(),
                truncate(req.getRemoteAddr()),
                response.getStatus(),
                ageSeconds,
                truncate(truncate(req.getQueryString())));
    }

    private static String getRequestURI(HttpServletRequest req, HttpServletResponse response) {
        String errorUri = (String) req.getAttribute("javax.servlet.error.request_uri");
        if (errorUri != null) {
            return errorUri;
        }
        return req.getRequestURI();

    }

    private static String truncate(String s) {
        if (s == null) {
            return "";
        }
        return s.length() > 255 ? s.substring(0, 255) : s;

    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public String getUrl() {
        return url;
    }

    public String getLocales() {
        return locales;
    }

    public String getUseragent() {
        return useragent;
    }

    public String getReferer() {
        return referer;
    }

    public String getSessionid() {
        return sessionid;
    }

    public String getMethod() {
        return method;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "Log{" +
                "remoteUser='" + remoteUser + '\'' +
                ", url='" + url + '\'' +
                ", locales='" + locales + '\'' +
                ", useragent='" + useragent + '\'' +
                ", referer='" + referer + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", method='" + method + '\'' +
                ", ip='" + ip + '\'' +
                ", httpStatus=" + httpStatus +
                ", sessionAge=" + sessionAge +
                ", queryString='" + queryString + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getHost() {
        return host;
    }

    public long getSessionAge() {
        return sessionAge;
    }

    public String getQueryString() {
        return queryString;
    }
}
