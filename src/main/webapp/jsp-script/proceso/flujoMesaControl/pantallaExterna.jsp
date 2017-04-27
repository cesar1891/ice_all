<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<frameset rows="100,*">
    <frame src="<s:url namespace="/flujomesacontrol" action="controladorExterno" /><s:property value="params.urlInterna" escapeHtml="false" />" frameborder="0">
    <frame src="<s:property value="params.urlExterna" escapeHtml="false" />">
</frameset>
</html>