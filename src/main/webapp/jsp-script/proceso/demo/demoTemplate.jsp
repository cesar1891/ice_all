<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p81_urlGuardar = '<s:url namespace="/demo" action="guardarDatosDemo" />'
    ,_p81_urlCargar = '<s:url namespace="/demo" action="cargarDatosDemo"  />';
////// urls //////

////// variables //////
var _p81_params = <s:property value='%{convertToJSON("params")}' escapeHtml="false" />
    ,_p81_flujo = <s:property value='%{convertToJSON("flujo")}' escapeHtml="false" />;

debug('_p81_params:' , _p81_params , '.');
debug('_p81_flujo:'  , _p81_flujo  , '.');
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p81_unoItems   = [<s:if test='%{!("x"+getParams().get("uno")).equals("x")}'  ><s:property value="items.uno"  escapeHtml="false" /></s:if>]
    ,_p81_dosItems  = [<s:if test='%{!("x"+getParams().get("dos")).equals("x")}'  ><s:property value="items.dos"  escapeHtml="false" /></s:if>]
    ,_p81_tresItems = [<s:if test='%{!("x"+getParams().get("tres")).equals("x")}' ><s:property value="items.tres" escapeHtml="false" /></s:if>];
////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.form.Panel',
    {
        renderTo  : '_p81_divpri'
        ,itemdId  : '_p81_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,items    :
        [
            {
                xtype     : 'fieldset'
                ,title    : '<span style="font:bold 14px Calibri;">DATOS DEL TR\u00c1MITE</span>'
                ,defaults : { style : 'margin:5px;' }
                ,items    :
                [
                    {
                        xtype       : 'numberfield'
                        ,fieldLabel : 'TR\u00c1MITE'
                        ,readOnly   : true
                        ,value      : _p81_flujo.ntramite
                        ,name       : 'ntramite'
                    }
                    ,{
                        xtype       : 'numberfield'
                        ,fieldLabel : 'SUCURSAL'
                        ,readOnly   : true
                        ,value      : _p81_flujo.cdunieco
                        ,name       : 'cdunieco'
                    }
                ]
                ,hidden   : Ext.isEmpty(_p81_params.uno)
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
            }
            ,{
                xtype     : 'fieldset'
                ,title    : '<span style="font:bold 14px Calibri;">'+_p81_params.uno+'</span>'
                ,defaults : { style : 'margin:5px;' }
                ,items    : _p81_unoItems
                ,hidden   : Ext.isEmpty(_p81_params.uno)
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
            }
            ,{
                xtype     : 'fieldset'
                ,title    : '<span style="font:bold 14px Calibri;">'+_p81_params.dos+'</span>'
                ,defaults : { style : 'margin:5px;' }
                ,items    : _p81_dosItems
                ,hidden   : Ext.isEmpty(_p81_params.dos)
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
            }
            ,{
                xtype     : 'fieldset'
                ,title    : '<span style="font:bold 14px Calibri;">'+_p81_params.tres+'</span>'
                ,defaults : { style : 'margin:5px;' }
                ,items    : _p81_tresItems
                ,hidden   : Ext.isEmpty(_p81_params.tres)
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
            }
        ]
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Guardar'
                ,icon    : '${icons}disk.png'
                ,handler : function(me)
                {
                    var ck = 'Verificando valores';
                    try
                    {
                        var form = me.up('form');
                        
                        if(!form.isValid())
                        {
                            return datosIncompletos();
                        }
                        
                        var values = form.getValues();
                        debug('values:',values);
                        
                        ck = 'Guardando datos';
                        _mask(ck);
                        Ext.Ajax.request(
                        {
                            url      : _p81_urlGuardar
                            ,params  : _formValuesToParams(values)
                            ,success : function(response)
                            {
                                _unmask();
                                var ck = 'Decodificando respuesta al cargar datos';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### guardar:',json);
                                    if(json.success==true)
                                    {
                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                                        {
                                            title        : 'Continuar proceso'
                                            ,itemId      : '_p81_windowBotones'
                                            ,closeAction : 'destroy'
                                            ,modal       : true
                                            ,width       : 500
                                            ,height      : 100
                                            ,border      : 0
                                            ,html        : '<div style="padding:5px;">Ahora puede continuar el proceso, seleccione una opci\u00f3n</div>'
                                            ,buttonAlign : 'center'
                                            ,buttons     : []
                                            ,listeners   :
                                            {
                                                afterrender : function()
                                                {
                                                    _cargarBotonesEntidad(
                                                        _p81_flujo.cdtipflu
                                                        ,_p81_flujo.cdflujomc
                                                        ,_p81_flujo.tipoent
                                                        ,_p81_flujo.claveent
                                                        ,_p81_flujo.webid
                                                        ,'_p81_windowBotones'
                                                        ,_p81_flujo.ntramite
                                                        ,_p81_flujo.status
                                                        ,_p81_flujo.cdunieco
                                                        ,_p81_flujo.cdramo
                                                        ,_p81_flujo.estado
                                                        ,_p81_flujo.nmpoliza
                                                        ,_p81_flujo.nmsituac
                                                        ,_p81_flujo.nmsuplem
                                                    );
                                                }
                                            }
                                        }).show());
                                    }
                                    else
                                    {
                                        mensajeError(json.message);
                                    }
                                }
                                catch(e)
                                {
                                    manejaException(e,ck);
                                }
                            }
                            ,failure : function()
                            {
                                _unmask();
                                errorComunicacion(null,'Error guardando datos');
                            }
                        });
                    }
                    catch(e)
                    {
                        manejaException(e,ck);
                    }
                }
            }
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    var ck = 'Cargando datos';
    try
    {
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p81_urlCargar
            ,params  : { 'params.ntramite' : _p81_flujo.ntramite }
            ,success : function(response)
            {
                _unmask();
                var ck = 'Decodificando respuesta al cargar datos';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load:',json);
                    if(json.success==true)
                    {
                        if(!Ext.isEmpty(json.params.ntramite))
                        {
                            for(var name in json.params)
                            {
                                var campo = _fieldByName(name,null,true);
                                if(!Ext.isEmpty(campo))
                                {
                                    campo.setValue(json.params[name]);
                                }
                            }
                        }
                    }
                    else
                    {
                        mensajeError(json.message);
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                _unmask();
                errorComunicacion(null,'Error cargando datos');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
    ////// loaders //////
});

////// funciones //////
////// funciones //////
</script>
</head>
<body>
<div id="_p81_divpri" style="height:600px;border:1px solid #CCCCCC"></div>
</body>
</html>