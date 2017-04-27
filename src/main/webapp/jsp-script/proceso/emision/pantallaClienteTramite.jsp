<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p61_urlPantallaCliente          = '<s:url namespace="/catalogos" action="includes/personasLoader"  />',
    _p61_urlMovimiento               = '<s:url namespace="/emision"   action="movimientoClienteTramite" />',
    _p61_urlRecuperarNmsoliciTramite = '<s:url namespace="/emision"   action="recuperarNmsoliciTramite" />';
////// urls //////

////// variables //////
var _p61_params = <s:property value="%{convertToJSON('params')}"  escapeHtml="false" />;
debug('_p61_params:',_p61_params);

var _p61_flujo = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;
debug('_p61_flujo:',_p61_flujo);

var _p22_parentCallback;
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
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
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p61_divpri'
        ,itemId   : '_p61_panelpri'
        ,border   : 0
        ,title    : 'Cliente de tr\u00e1mite'
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            {
                xtype        : 'form'
                ,itemId      : '_p61_form'
                ,title       : 'Acciones'
                ,buttonAlign : 'left'
                ,buttons     :
                [
                    {
                        text     : 'CAMBIAR CLIENTE'
                        ,itemId  : '_p61_botonCambiarCliente'
                        ,icon    : '${icons}control_repeat_blue.png'
                        ,handler : function()
                        {
                            _p61_movimiento(
                                _p61_flujo.ntramite
                                ,''
                                ,'D'
                                ,function()
                                {
                                    _p61_params.cdperson = '';
                                    _fieldById('_p61_panelCliente').getLoader().load(
                                    {
                                        params :
                                        {
                                            'smap1.cdperson'         : _p61_params.cdperson
                                            ,'smap1.polizaEnEmision' : 'S'
                                            ,'smap1.esSaludDanios'   : _p61_params.esSaludDanios
                                            //,'smap1.modoSoloEdicion' : 'S'
                                        }
                                    });
                                }
                            );
                        }
                    }
                ]
                ,listeners :
                {
                    afterrender : function(me)
                    {
                        _cargarBotonesEntidad(
                            _p61_flujo.cdtipflu
                            ,_p61_flujo.cdflujomc
                            ,_p61_flujo.tipoent
                            ,_p61_flujo.claveent
                            ,_p61_flujo.webid
                            ,me.itemId//callback
                            ,_p61_flujo.ntramite
                            ,_p61_flujo.status
                            ,_p61_flujo.cdunieco
                            ,_p61_flujo.cdramo
                            ,_p61_flujo.estado
                            ,_p61_flujo.nmpoliza
                            ,_p61_flujo.nmsituac
                            ,_p61_flujo.nmsuplem
                            ,null//callbackDespuesProceso
                        );
                    }
                }
            }
            ,{
                xtype       : 'panel'
                ,itemId     : '_p61_panelCliente'
                ,title      : 'Cliente'
                ,border     : 0
                ,height     : 600
                ,autoScroll : true
                ,loader     :
                {
                    url       : _p61_urlPantallaCliente
                    ,scripts  : true
                    ,autoLoad : true
                    ,border   : 0
                    ,params   :
                    {
                        'smap1.cdperson'         : _p61_params.cdperson
                        ,'smap1.polizaEnEmision' : 'S'
                        ,'smap1.esSaludDanios'   : _p61_params.esSaludDanios
                        //,'smap1.modoSoloEdicion' : 'S'
                    }
                }
            }
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    var mask, ck = 'Verificando que no exista cotizaci\u00f3n';
    try {
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url    : _p61_urlRecuperarNmsoliciTramite,
            params : {
                'params.ntramite' : _p61_flujo.ntramite
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al verificar cotizaci\u00f3n';
                try {
                    var json = Ext.decode(response.responseText);
                    debug('### validar cotizacion:',json,'.');
                    if (json.success === true) {
                        if (!Ext.isEmpty(json.params.nmsolici) && Number(json.params.nmsolici) > 0) {
                            mensajeWarning('Este tr\u00e1mite ya fue cotizado y no permite vincular cliente');
                            _fieldById('_p61_botonCambiarCliente').hide();
                            _fieldById('_p61_panelCliente').hide();
                        }
                    } else {
                        mensajeError(json.message);
                    }
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al verificar existencia de cotizaci\u00f3n');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
    ////// loaders //////
});

////// funciones //////
function _p61_movimiento(ntramite,cdperson,accion,callback)
{
    var ck = 'Ejecutando movimiento cliente tr\u00e1mite';
    try
    {
    
        if(Ext.isEmpty(ntramite)
            ||Ext.isEmpty(accion))
        {
            throw 'Faltan datos para ejecutar movimiento';
        }
    
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p61_urlMovimiento
            ,params  :
            {
                'params.ntramite'  : ntramite
                ,'params.cdperson' : cdperson
                ,'params.accion'   : accion
            }
            ,success : function(response)
            {
                _unmask();
                var ck = 'Decodificando respuesta al ejecutar movimiento cliente tr\u00e1mite';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### guardar:',json);
                    if(json.success===true)
                    {
                        if (accion === 'D') {
                            mensajeCorrecto('Datos guardados','Se ha desvinculado el cliente del tr\u00e1mite, por favor seleccione uno nuevo',callback);
                        } else if (accion === 'I') {
                            mensajeCorrecto('Datos guardados','Se ha vinculado el cliente al tr\u00e1mite',callback);
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
                errorComunicacion(null,'Error al ejecutar movimiento cliente tr\u00e1mite');
            }
        });
    }
    catch(e)
    {
        _unmask();
        manejaException(e,ck);
    }
}

_p22_parentCallback = function(json)
{
    debug('>_p22_parentCallback json:',json);
    _p61_params.cdperson = json.smap1.CDPERSON;
    
    debug('_p61_params:',_p61_params);
    
    _p61_movimiento(
        _p61_flujo.ntramite
        ,json.smap1.CDPERSON
        ,'I'
    );
    debug('<_p22_parentCallback');
}
////// funciones //////
</script>
</head>
<body>
<div id="_p61_divpri" style="height:800px;border:1px solid #CCCCCC"></div>
</body>
</html>