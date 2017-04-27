<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
{
"success":"<c:out value="${nombrepanel}" escapeXml="false" />"
<c:if test="${not empty listPanelesView}">
	,"listPanelesView":<c:out value="${listPanelesView}" escapeXml="false" />
</c:if>
}