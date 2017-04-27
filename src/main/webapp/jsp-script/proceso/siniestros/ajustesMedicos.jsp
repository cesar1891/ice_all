<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Consulta Clausulas</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
            var mesConUrlLoadCatalo    		= '<s:url namespace="/catalogos"  action="obtieneCatalogo"  />';
            var _URL_CATALOGOS 				= '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
            var _amUrlCargar           		= '<s:url namespace="/siniestros" action="obtenerTdsinival" />';
            var _amUrlAgregar         		= '<s:url namespace="/siniestros" action="guardarTdsinival" />';
            var _amUrlEliminar          	= '<s:url namespace="/siniestros" action="eliminarTdsinival" />';
            var _amUrlModificar          	= '<s:url namespace="/siniestros" action="modificarTdsinival" />';
            
            var _CATALOGO_OFICINA_RECEP 	= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
            var _CATALOGO_TipoConcepto  	= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_CONCEPTO_SINIESTROS"/>';
            var _CATALOGO_ConceptosMedicos  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CODIGOS_MEDICOS"/>';
            var _amParams 					= <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
			debug("_amParams: ",_amParams);
            
        </script>
        <!-- <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js"></script>-->
        <script type="text/javascript" src="${ctx}/js/proceso/siniestros/ajustesMedico.js"></script>
        
    </head>
    <body>
    <!-- <div style="height:500px;">
            <div id="div_clau"></div>
   </div>-->
    	<div style="height:10px;">
            <div id="div_clau2"></div>
        </div>
   </body>
</html>