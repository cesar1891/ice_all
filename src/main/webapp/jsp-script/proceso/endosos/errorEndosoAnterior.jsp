<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<s:if test="%{respuesta!=null}">
	<s:set name="message"  value="%{respuesta}"/>
</s:if>
<s:else>
    <s:set name="message"  value="%{error}"/>
</s:else>

<script>
	Ext.onReady(function() {
		mensajeError('<s:property value="message" />');
	});
</script>