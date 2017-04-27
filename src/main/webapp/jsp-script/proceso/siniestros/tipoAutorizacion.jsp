<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Historial de Reclamaciones</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';            
            //var _URL_CARGA_CLAVES_CLAU =    '<s:url namespace="/catalogos" action="cargaClausulas" />';
            var _URL_TIPO_AUTORIZACION= _CONTEXT + '/js/proceso/siniestros/tiposAutorizacion.json';
            var _URL_CONSULTA_CLAUSU =      '<s:url namespace="/catalogos" action="consultaClausulas" />';
        </script>
        <script type="text/javascript" src="${ctx}/js/proceso/siniestros/tipoAutorizacion.js"></script>
        
    </head>
    <body>
    <div style="height:500px;">
            <div id="div_clau"></div>
   </div>
    </body>
</html>