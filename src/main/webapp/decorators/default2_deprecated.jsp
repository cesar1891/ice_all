<%@ page language="java" %>

<%@ include file="/taglibs_deprecated.jsp"%>
    
<!-- pagina para decorator "default2_deprecated": -->

<!DOCTYPE html>    
<html>
	<head> 
		<script language="javascript">
		
		    var _URL_AYUDA = "";
		    var _NUM_REGISTROS = "";
		    var _ALTO_GRILLA = "";
		
			if (window.location.href.indexOf("login.html") != -1) {
				window.parent.location.href = "http://acw.biosnettcs.com:7778/login/login.html";
			}
			
			function mostrarAyuda () {		
				if (_URL_AYUDA != "") {		
					var width, height;
					width = screen.width / 2;
					height = screen.height / 2;
					window.open("${helpDir}" + _URL_AYUDA, 'helpWindow', config='height=' + height + ', width=' + width + ', toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, directories=no, status=no');
				}
			}
			
			//Si los datos recuperados son vacios, asigno los valores estandares
			_NUMROWS = (eval("${numRows}") != "")?eval("${numRows}"):20;
			_GRIDHEIGHT = (eval("${gridHeight}") != "")?eval("${gridHeight}"):320;
			
			
			function cerrar() {
			}
		</script>    
		    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<link href="${ctx}/resources/css/header_light.css?yyyyMMdd" rel="stylesheet" type="text/css" />
			<link href="${ctx}/resources/css/toolbar_magenta.css" rel="stylesheet" type="text/css" />
			<link href="${ctx}/resources/css/footer_magenta.css" rel="stylesheet" type="text/css" />
			<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/ext-all.css" />
			<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/xtheme-gray.css" />
			<link href="${ctx}/resources/css/template_css.css?yyyyMMdd" rel="stylesheet" type="text/css" />
			<script type="text/javascript" src="${ctx}/resources/jquery/jquery-1.10.2.min.js"></script>
			<script type="text/javascript" src="${ctx}/resources/extjs/adapter/ext/ext-base.js"></script>
			<script type="text/javascript" src="${ctx}/resources/extjs/ext-all.js"></script>
			<script type="text/javascript" src="${ctx}/resources/scripts/util/ext.js"></script>
			<script type="text/javascript" src="${ctx}/resources/extjs/local/ext-lang-es.js"></script>
		    <decorator:head />
	</head>
	<body onunload="cerrar()">
	    <decorator:body />
	</body>
</html>