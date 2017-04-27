<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
////// urls //////

////// variables //////
var _p53_flujo = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;
debug('_p53_flujo:',_p53_flujo);
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
        title     : 'DEBUG'
        ,itemId   : '_p53_panelpri'
        ,renderTo : '_p53_divpri'
        ,width    : 600
        ,border   : 1
        ,layout   :
        {
            type     : 'table'
            ,columns : 2
        }
        ,defaults : { style : 'margin:5px;' }
        ,items    : [ <s:property value="items.items" escapeHtml="false" /> ]
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
<div id="_p53_divpri" style="width:700px;height:400px;border:1px solid #CCCCCC;"></div>
</body>
</html>