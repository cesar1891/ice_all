<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Consulta Clausulas</title>
		<script type="text/javascript">
			var _CONTEXT = '${ctx}';
			var _URL_CATALOGOS 				=	'<s:url namespace="/catalogos"  action="obtieneCatalogo" />';
			var _SINO						=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SINO" />';
			var _SECUENCIA					=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SECUENCIA_IVA" />';
			var _CATALOGO_PROVEEDORES		=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
			var _CATALOGO_TipoMoneda		=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_MONEDA"/>';
			var _CATALOGO_TFORMATOS			=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TFORMATOS"/>';
			var _CATALOGO_CVECOLUMNA		=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CVECOLUMNA"/>';
			var _CATALOGO_FORMATOFECHA		=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@FORMATOFECHA"/>';
			var _ATRIBUTO_LAYOUT			=	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ATRIBUTOLAYOUT"/>';
			var _CATALOGO_ConfLayout		= 	'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CONFLAYOUT"/>';
			
			
			var _URL_GUARDA_CONFIGURACION	=	'<s:url namespace="/siniestros" action="guardaConfiguracionLayout" />';
			var _URL_CONSULTA_CONF_LAYOUT	=	'<s:url namespace="/siniestros" action="consultaConfiguracionLayout" />';
					
			
			var valorAction = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
			debug('Valor de valorAction : ',valorAction);
		</script>
		<!-- <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js"></script>-->
		<script type="text/javascript" src="${ctx}/js/proceso/siniestros/configuracionLayout.js?${now}"></script>
	</head>
	<body>
	<div style="height:100px;">
		<div id="div_clau21"></div>
	</div>>
   </body>
</html>