<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Autorizaci&oacute;n Especial</title>
			<script type="text/javascript">
				var	_CONTEXT = '${ctx}';
				var _URL_SOLICITUD_CXP	    		= '<s:url namespace="/siniestros"       action="cargaSolicitudesCxp" />';
			</script>
			<script type="text/javascript" src="${ctx}/js/proceso/siniestros/solicitudesCxp.js"></script>
	</head>
	<body>
		<div style="height:500px;">
			<div id="div_clau"></div>
		</div>
 	</body>
</html>