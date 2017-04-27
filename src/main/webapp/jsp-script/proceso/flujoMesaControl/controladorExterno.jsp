<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
////// urls //////

////// variables //////
var _p55_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p55_flujo:',_p55_flujo);
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// requires //////
    ////// requires //////
    
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo     : '_p55_divpri'
        ,itemId      : '_p55_panelpri'
        ,title       : 'CONTROLADOR DE PROCESOS EXTERNO'
        ,buttonAlign : 'left'
        ,buttons     : _cargarBotonesEntidad
        (
            _p55_flujo.cdtipflu
            ,_p55_flujo.cdflujomc
		    ,_p55_flujo.tipoent
		    ,_p55_flujo.claveent
		    ,_p55_flujo.webid
		    ,'_p55_panelpri'
		    ,_p55_flujo.ntramite
		    ,_p55_flujo.status
		    ,_p55_flujo.cdunieco
		    ,_p55_flujo.cdramo
		    ,_p55_flujo.estado
		    ,_p55_flujo.nmpoliza
		    ,_p55_flujo.nmsituac
		    ,_p55_flujo.nmsuplem
        )
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
////// funciones //////

</script>
</head>
<body>
<div id="_p55_divpri" style="height:200px;border:1px solid #CCCCCC;"></div>
</body>
</html>