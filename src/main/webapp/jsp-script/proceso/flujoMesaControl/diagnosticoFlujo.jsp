<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
////// urls //////

////// variables //////
var _p56_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p56_flujo:',_p56_flujo);
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
        renderTo     : '_p56_divpri'
        ,itemId      : '_p56_panelpri'
        ,border      : 0
        ,title       : 'PANTALLA DE DIAGN\u00c1STICO DE FLUJO'
        ,layout      :
        {
            type     : 'table'
            ,columns : 2
        }
        ,defaults    :
        {
            style  : 'margin:5px;border-bottom:1px solid #CCCCCC'
            ,xtype : 'displayfield'
            ,width : 250
        }
        ,items       :
        [
            {
                fieldLabel : 'TR\u00c1MITE'
                ,value     : _p56_flujo.ntramite
            }
            ,{
                fieldLabel : 'STATUS'
                ,value     : _p56_flujo.status
            }
            ,{
                fieldLabel : 'CLAVE DE TR\u00c1MITE'
                ,value     : _p56_flujo.cdtipflu
            }
            ,{
                fieldLabel : 'CLAVE DE PROCESO'
                ,value     : _p56_flujo.cdflujomc
            }
            ,{
                fieldLabel : 'TIPO DE ENTIDAD'
                ,value     : _p56_flujo.tipoent
            }
            ,{
                fieldLabel : 'CLAVE DE ENTIDAD'
                ,value     : _p56_flujo.claveent
            }
            ,{
                fieldLabel : 'ID DE ENTIDAD'
                ,value     : _p56_flujo.webid
            }
            ,{
                fieldLabel : 'CDUNIECO'
                ,value     : _p56_flujo.cdunieco
            }
            ,{
                fieldLabel : 'CDRAMO'
                ,value     : _p56_flujo.cdramo
            }
            ,{
                fieldLabel : 'ESTADO'
                ,value     : _p56_flujo.estado
            }
            ,{
                fieldLabel : 'NMPOLIZA'
                ,value     : _p56_flujo.nmpoliza
            }
            ,{
                fieldLabel : 'NMSITUAC'
                ,value     : _p56_flujo.nmsituac
            }
            ,{
                fieldLabel : 'NMSUPLEM'
                ,value     : _p56_flujo.nmsuplem
            }
        ]
        ,buttonAlign : 'center'
        ,buttons     : _cargarBotonesEntidad
        (
            _p56_flujo.cdtipflu
            ,_p56_flujo.cdflujomc
            ,_p56_flujo.tipoent
            ,_p56_flujo.claveent
            ,_p56_flujo.webid
            ,'_p56_panelpri'
            ,_p56_flujo.ntramite
            ,_p56_flujo.status
            ,_p56_flujo.cdunieco
            ,_p56_flujo.cdramo
            ,_p56_flujo.estado
            ,_p56_flujo.nmpoliza
            ,_p56_flujo.nmsituac
            ,_p56_flujo.nmsuplem
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
<div id="_p56_divpri" style="height:1000px;border:1px solid #CCCCCC;"></div>
</body>
</html>