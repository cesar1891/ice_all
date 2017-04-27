<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<script>
<s:if test='%{getMap1()!=null&&getMap1().get("url")!=null}'>
window.onload=function()
{
	window.location.href='<s:property value="map1.url" escapeHtml="false" />';
}
</s:if>
</script>
</head>
</html>