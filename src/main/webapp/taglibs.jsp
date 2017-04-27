<%@ page language="java" errorPage="/error.jsp" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core"              prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"               prefix="fmt" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/struts-tags"                                   prefix="s" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:set var="icons" value="${pageContext.request.contextPath}/resources/fam3icons/icons/" />

<c:set var="images" value="${pageContext.request.contextPath}/images/" />

<c:set var="defines" value="${pageContext.request.contextPath}/resources/ext-defines/" />

<c:set var="flujoimg" value="${pageContext.request.contextPath}/resources/images/flujomc/" />

<c:set var="now" value="<%= new java.util.Date() %>" />
<fmt:formatDate pattern="yyyyMMddHHmm" value="${now}" var="now" />