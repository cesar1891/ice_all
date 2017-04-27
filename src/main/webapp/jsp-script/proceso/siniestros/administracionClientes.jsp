<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Autorizaci&oacute;n Especial</title>
			<script type="text/javascript">
				var	_CONTEXT = '${ctx}';
				var _CAT_AGENTE	 					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTES"/>';
				var _URL_CATALOGOS					= '<s:url namespace="/catalogos" 		action="obtieneCatalogo" />';
				var _P22_URLOBTENERPERSONAS     	= '<s:url namespace="/catalogos"  		action="obtenerPersonasPorRFC"              />';
				var _URL_GUARDA_CLIENTE      		= '<s:url namespace="/persona" 			action="guardarConfiguracionClientes" />';
				var _URL_LISTA_CLIENTE_GRID    		= '<s:url namespace="/persona" 			action="obtieneListaClientesxTipo" />';
				var _URL_LISTA_CLIENTES            = '<s:url namespace="/persona"  		action="consultaClientes" />';
				var proceso = <s:property value='paramsJson' escapeHtml='false'/>;
			</script>
			<script type="text/javascript" src="${ctx}/js/proceso/siniestros/administracionClientes.js"></script>
	</head>
	<body>
		<div style="height:500px;">
			<div id="div_clau"></div>
		</div>
 	</body>
</html>