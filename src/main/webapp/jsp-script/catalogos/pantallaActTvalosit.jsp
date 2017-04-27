<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p82_urlCargar   = '<s:url namespace="/tvalosit" action="cargarPantallaActTvalosit"  />'
    ,_p82_urlGuardar = '<s:url namespace="/tvalosit" action="guardarPantallaActTvalosit" />';
////// urls //////

////// variables //////
var _p82_params = <s:property value='%{convertToJSON("params")}' escapeHtml="false" />;
debug('_p82_params:',_p82_params);

var _p82_form;
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p82_items = [ <s:property value="items.items" escapeHtml="false" /> ];
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
    _p82_form = Ext.create('Ext.form.Panel',
    {
        renderTo  : '_p82_divpri'
        ,itemId   : '_p82_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,layout   :
        {
            type     : 'table'
            ,columns : 2
        }
        ,items       : _p82_items
        ,buttonAlign : 'center'
        ,buttons     :
        [{
            text     : 'Guardar'
            ,icon    : '${icons}disk.png'
            ,handler : function(me)
            {
                var ck = 'Verificando datos';
                try
                {
                    if(!_p82_form.isValid())
                    {
                        throw 'Favor de revisar los datos';
                    }
                    
                    var datos = _p82_form.getValues();
                    debug('datos:',datos);
                    
                    datos.cdunieco = _p82_params.cdunieco;
                    datos.cdramo   = _p82_params.cdramo;
                    datos.estado   = _p82_params.estado;
                    datos.nmpoliza = _p82_params.nmpoliza;
                    datos.nmsituac = _p82_params.nmsituac;
                    
                    datos = _formValuesToParams(datos);
                    debug('datos:',datos);
                    
                    ck = 'Guardando datos de inciso';
                    _mask(ck);
                    Ext.Ajax.request(
                    {
                        url      : _p82_urlGuardar
                        ,params  : datos
                        ,success : function(response)
                        {
                            _unmask();
                            var ck = 'Decodificando respuesta al guardar datos de situaci\u00f3n';
                            try
                            {
                                var json = Ext.decode(response.responseText);
                                debug('### act tvalosit:',json);
                                if(json.success==true)
                                {
                                    mensajeCorrecto(
                                        'Datos guardados'
                                        ,'Se guardaron los datos de situaci\u00f3n'
                                        ,function()
                                        {
                                            try
                                            {
                                                _fieldById('_p82_contenedor').close();
                                            }
                                            catch(e)
                                            {
                                                debugError('Error manejado al tratar de destruir ventana de act tvalosit',e);
                                            }
                                            
                                            try
                                            {
                                                _p82_callback();
                                            }
                                            catch(e)
                                            {
                                                debugError('Error manejado al intentar llamar callback',e);
                                            }
                                        }
                                    );
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
                            errorComunicacion(null,'Error al guardar datos de inciso');
                        }
                    });
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
        }]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    var ck = 'Cargando datos de inciso';
    _mask(ck);
    try
    {
        Ext.Ajax.request(
        {
            url      : _p82_urlCargar
            ,params  : _formValuesToParams(_p82_params)
            ,success : function(response)
            {
                _unmask();
                var ck = 'Decodificando respuesta al cargar datos de inciso';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### cargar datos de situacion:',json);
                    if(json.success==true)
                    {
                        _cargarForm(_p82_form,json.params);
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
                errorComunicacion(null,'Error al cargar datos de inciso');
            }
        });
    }
    catch(e)
    {
        _unmask();
        manejaException(e,ck);
    }
    ////// loaders //////
});

////// funciones //////
////// funciones //////
</script>
</head>
<body>
<div id="_p82_divpri" style="height:400px;border:1px solid #CCCCCC"></div>
</body>
</html>