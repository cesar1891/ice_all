<%@ page language="java" errorPage="/error.jsp" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%
   String directorioIdioma = "/biosnet/"+((mx.com.aon.portal.model.UserVO)session.getAttribute("USUARIO")).getIdioma().getLabel();
   session.setAttribute("helpDir",directorioIdioma);
%>

<c:set var="datePattern"><fmt:message key="date.format"/></c:set>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="helpDir" value="${sessionScope.helpDir}"/>

<%
	int numRows = ((mx.com.aon.portal.model.UserVO)session.getAttribute("USUARIO")).getTamagnoPaginacionGrid();
	session.setAttribute("numRows",numRows);
	//En el caso de que se implemente el alto del grid en session, sino, borrar:
	//int gridHeight = userVO.getAltoGrid();
	int gridHeight = 320;
	session.setAttribute("gridHeight",gridHeight);
%>
<c:set var="numRows" value="${sessionScope.numRows}"/>
<c:set var="gridHeight" value="${sessionScope.gridHeight}"/>