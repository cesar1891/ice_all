<%--
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
--%>
<%@ include file="/taglibs.jsp"%>
<%--
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Agentes</title>
        --%>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
            var _URL_VALIDA_DATOS_SITUACION =    '<s:url namespace="/flujocotizacion" 	action="consultaDatosPolizaAgente" />'; 
            var _URL_OBTIENE_DATOS_AGENTE_X_PUNTOS =    '<s:url namespace="/flujocotizacion" 	action="obtenerAgentePolizaPorPuntos" />'; 
            var _URL_GENERAL_AGENTES        =    '<s:url namespace="/flujocotizacion"   action="consultaGeneralAgente" />';
            var _URL_CATALOGOS          	=	 '<s:url namespace="/catalogos"         action="obtieneCatalogo" />';
            var _URL_GUARDA_PORCENTAJE      =    '<s:url namespace="/flujocotizacion" 	action="guardaPorcentajeAgentes" />';
            var _URL_GUARDA_PORCENTAJE_X_PUNTOS  =    '<s:url namespace="/flujocotizacion" 	action="guardaPorcentajeAgentesPorPuntos" />';
            var _URL_GUARDA_ELIMINA_PORC    =    '<s:url namespace="/flujocotizacion" 	action="guardaEliminaPorcentajeAgentes" />';
            var _URL_GUARDA_ELIMINA_PORC_X_PUNTOS    =    '<s:url namespace="/flujocotizacion" 	action="guardaEliminaPorcentajeAgentesPorPuntos" />';
            var _CATALOGO_AGENTES			= 	 '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTES"/>';
            var _CATALOGO_AGENTES_UNICO     = 	 '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTE_ESPECIFICO"/>';
            
            <%--
            Estas variables se toman del padre, complementarios.jsp
            var inputCdunieco='<s:property value="cdunieco" />';
            var inputCdramo='<s:property value="cdramo" />';
            var inputEstado='<s:property value="estado" />';
            var inputNmpoliza='<s:property value="nmpoliza" />';
            --%>
        </script>
        <script type="text/javascript" src="${ctx}/js/consultas/agentes/consultaAgentes.js?${now}"></script>
        <%--
    </head>
    <body>
    --%>
    <div style="height:700px;">
            <div id="div_clau"></div>
   </div>
   <%--
    </body>
</html>
--%>