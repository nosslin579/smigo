package org.smigo.log;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Locale;

class LogBean {

    private final String remoteUser;
    private final String url;
    private final String locales;
    private final String useragent;
    private final String referer;
    private final String sessionid;
    private final String method;
    private final String ip;
    private final String note;
    private final String origin;

    public LogBean(String remoteUser, String url, String locales, String useragent,
                   String referer, String sessionid, String method, String ip, String note, String origin) {
        this.remoteUser = remoteUser;
        this.url = url;
        this.locales = locales;
        this.useragent = useragent;
        this.referer = referer;
        this.sessionid = sessionid;
        this.method = method;
        this.ip = ip;
        this.note = note;
        this.origin = origin;
    }

    public static LogBean create(HttpServletRequest req) {
        StringBuilder locales = new StringBuilder();
        for (Enumeration<Locale> l = req.getLocales(); l.hasMoreElements(); ) {
            locales.append(l.nextElement().toString()).append(" ");
        }

        return new LogBean(truncate(req.getRemoteUser()),
                truncate(req.getRequestURL().toString()),
                truncate(locales.toString()),
                truncate(req.getHeader("user-agent")),
                truncate(req.getHeader("referer")),
                truncate(req.getRequestedSessionId()),
                req.getMethod(),
                truncate(req.getHeader("x-forwarded-for")),
                truncate((String) req.getAttribute(VisitLogger.NOTE_ATTRIBUTE)),
                truncate(req.getHeader("origin")));

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

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "LogBean{" +
                "remoteUser='" + remoteUser + '\'' +
                ", url='" + url + '\'' +
                ", locales='" + locales + '\'' +
                ", useragent='" + useragent + '\'' +
                ", referer='" + referer + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", method='" + method + '\'' +
                ", ip='" + ip + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public String getOrigin() {
        return origin;
    }
}
