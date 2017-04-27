<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
<script>
Ext.onReady(function()
{
	/*
	ERROR:
	<s:property value="respuestaOculta" />
	<s:property value="message" />
	*/
	mensajeError('<s:property value="respuesta" /><s:property value="message" />');
});
</script>
</head>
<body></body>
</html>