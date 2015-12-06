<%@page pageEncoding="UTF-8" contentType="text/plain; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
User-agent: *
Allow: /$
Allow: /garden-planner
Allow: /help
Allow: /login
Allow: /register
Allow: /forum
Allow: /sitemap.xml

Allow: /*.css$
Allow: /*.js$
Allow: /*.png$

sitemap: http://${pageContext.request.serverName}/sitemap.xml