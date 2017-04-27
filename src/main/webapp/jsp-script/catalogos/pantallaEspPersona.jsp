<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p47_urlGuardarPersona      = '<s:url namespace="/persona" action="guardarPantallaEspPersona" />';
var _p47_urlRecuperarEspPersona = '<s:url namespace="/persona" action="recuperarEspPersona"       />';
////// urls //////

////// variables //////
var _p47_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
debug('_p47_params:',_p47_params);
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p47_items = [<s:property value="items.items" escapeHtml="false" />];
debug('_p47_items:',_p47_items);

var _p47_buttons = [<s:property value="items.buttons" escapeHtml="false" />];
debug('_p47_buttons:',_p47_buttons);
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
        title        : 'DATOS DE PERSONA'
        ,itemId      : '_p47_form'
        ,renderTo    : '_p47_divpri'
        ,defaults    : { style : 'margin:5px;' }
        ,layout      :
        {
            type     : 'table'
            ,columns : 2
        }
        ,items       : _p47_items
        ,buttonAlign : 'center'
        ,border      : 0
        ,buttons     : _p47_buttons
        ,autoScroll  : true
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    _p47_recuperarPersona(_p47_params.cdperson);
    ////// loaders //////
});

////// funciones //////
function _p47_recuperarPersona(cdperson)
{
    debug('_p47_recuperarPersona cdperson:',cdperson);
    var ck = 'Recuperando datos de persona';
    try
    {
        var form = _fieldById('_p47_form');
        _setLoading(true,form);
        Ext.Ajax.request(
        {
            url     : _p47_urlRecuperarEspPersona
            ,params :
            {
                'params.cdperson' : cdperson
            }
            ,success : function(response)
            {
                _setLoading(false,form);
                var ck = 'Decodificando datos de persona';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### recuperar persona:',json);
                    if(json.success)
                    {
                        _cargarForm(form,json.params);
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
            ,failure : function(response)
            {
                _setLoading(false,form);
                errorComunicacion(null,'Error cargando datos de persona');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p47_guardarYCerrarVentana(me)
{
    debug('_p47_guardarYCerrarVentana');
    _p47_guardar(me,function()
    {
        try
        {
            _fieldById('_p47_contenedor').destroy();
        }
        catch(e)
        {
            debugError('Error al intentar cerrar ventana contenedora de p47.',e);
        }
        
        try
        {
            _p47_callback();
        }
        catch(e)
        {
            debugError('Error al intentar ejecutar _p47_callback.',e);
        }
    });
}

function _p47_guardar(me,callback)
{
    debug('_p47_guardar callback?',!Ext.isEmpty(callback));
    
    var ck = 'Validando datos';
    try
    {
        var form = me.up('form');
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        var params = _formValuesToParams(form.getValues());
        params['params.cdunieco'] = _p47_params.cdunieco;
        params['params.cdramo']   = _p47_params.cdramo;
        params['params.estado']   = _p47_params.estado;
        params['params.nmpoliza'] = _p47_params.nmpoliza;
        params['params.nmsituac'] = _p47_params.nmsituac;
        params['params.cdtipsit'] = _p47_params.cdtipsit;
        debug('parametros para guardar espejo persona:',params);
        
        _setLoading(true,form);
        Ext.Ajax.request(
        {
            url      : _p47_urlGuardarPersona
            ,params  : params
            ,success : function(response)
            {
                _setLoading(false,form);
                var ck = 'Decodificando respuesta al guardar';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success)
                    {
                        mensajeCorrecto('Datos guardados',json.message,callback);
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
                _setLoading(false,form);
                errorComunicacion(null,'Error al guardar datos de persona');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}
////// funciones //////
</script>
</head>
<body>
<div id="_p47_divpri" style="height:400px;border:1px solid #CCCCCC;"></div>
</body>
</html>