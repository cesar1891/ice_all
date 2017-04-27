<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:if test="false">
<script>
</s:if>
//alert(1);
//_generarRemesaClic({xtype:'1'},1000,2,'M',282,function(){alert('fin');});

////// variables //////
//var _impLotUrlGenerarRemesa   = '<s:url namespace="/consultas" action="generarRemesaEmisionEndoso" />';
//var _impLotUrlMarcarImpresion = '<s:url namespace="/consultas" action="marcarImpresionOperacion" />';
////// variables //////

////// funciones //////
/*
function _generarRemesaClic(required,cdunieco,cdramo,estado,nmpoliza,callback,marcar,ntramite)
{
    debug('_generarRemesaClic args:', arguments);
    var ck = 'Revisando impresi\u00f3n';
    try
    {
        var ven = centrarVentanaInterna(Ext.create('Ext.window.Window',
        {
            title     : 'Verificando impresi\u00f3n'
            ,html     : '<span style="padding:5px;">Verificando impresi\u00f3n</span>'
            ,width    : 200
            ,height   : 100
            ,modal    : true
            ,closable : false
        }).show());
        
        _setLoading(true,ven);
        
        Ext.Ajax.request(
        {
            url      : _impLotUrlMarcarImpresion
            ,params  :
            {
                'params.cdunieco'    : cdunieco
                ,'params.cdramo'     : cdramo
                ,'params.estado'     : estado
                ,'params.nmpoliza'   : nmpoliza
                ,'params.marcar'     : marcar
                ,'params.ntramiteIn' : _NVL(ntramite)
            }
            ,success : function(response)
            {
                _setLoading(false,ven);
                ven.destroy();
                var ck = 'Decodificando respuesta al marcar impresi\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### marcar impresion:',json);
                    if(json.success==true)
                    {
                        if(json.params.preguntar=='S')
                        {
                            centrarVentanaInterna(Ext.MessageBox.confirm(
                                'Confirmar'
                                ,'¿Desea marcar el tr\u00e1mite como impreso y entregado?'
                                ,function(btn)
                                {
                                    if(btn === 'yes')
                                    {
                                        _generarRemesaClic(required,cdunieco,cdramo,estado,nmpoliza,callback,'S',ntramite);
                                    }
                                    else
                                    {
                                        debug('no quiso marcar');
                                        callback();
                                    }
                                }
                            ));
                        }
                        else if(json.params.marcado=='S')
                        {
                            debug('marcado');
                            mensajeCorrecto('Aviso','La emisi\u00f3n/endoso se marc\u00f3 como impresa',function(){ callback(); });
                        }
                        else
                        {
                            debug('no preguntar');
                            callback();
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
                _setLoading(false,ven);
                ven.destroy();
                errorComunicacion(null,'Error marcando impresi\u00f3n');
            }
        });
        
        *
        var _impLot_impresionClic;
        
        centrarVentanaInterna(Ext.create('Ext.window.Window',
        {
            title        : 'Impresi\u00f3n'
            ,itemId      : '_impLot_ventana'
            ,width       : 600
            ,height      : 200
            ,closeAction : 'destroy'
            ,modal       : true
            ,closable    : !required
            ,items       :
            [
                {
                    xtype  : 'displayfield'
                    ,value : '¿Que tipo de impresi\u00f3n desea?<br/>La impresi\u00f3n gen\u00e9rica es la impresi\u00f3n por separado de cada tipo de papel<br/>La impresi\u00f3n intercalada junta papeler\u00eda y recibos, por bandeja 1 y 2 respectivamente<br/>(Si ya hubo un intento anterior, se respetar\u00e1 el tipo especificado anteriormente)'
                    ,style : 'margin:5px;'
                }
            ]
            ,buttonAlign : 'center'
            ,buttons     :
            [
                {
                    text     : 'Impresi\u00F3n gen\u00E9rica'
                    ,icon    : '${icons}printer.png'
                    ,handler : function(me)
                    {
                        _impLot_impresionClic(me,'G');
                    }
                }
                ,{
                    text     : 'Impresi\u00F3n intercalada'
                    ,icon    : '${icons}printer.png'
                    ,handler : function(me)
                    {
                        _impLot_impresionClic(me,'I');
                    }
                }
            ]
        }).show());
        
        _impLot_impresionClic = function(bot,tipoimp)
        {
            centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', '¿Est\u00e1 seguro de generar el tr\u00e1mite de impresi\u00f3n tipo '
                +(tipoimp=='I'?'Intercalada':'Gen\u00E9rica')
                +'?', function(btn)
            {
                if(btn === 'yes')
                {
                    var loadCmp = bot.up('window');
                    _setLoading(true,loadCmp);
                    Ext.Ajax.request(
                    {
                        url      : _impLotUrlGenerarRemesa
                        ,params  :
                        {
                            'params.cdunieco'  : cdunieco
                            ,'params.cdramo'   : cdramo
                            ,'params.estado'   : estado
                            ,'params.nmpoliza' : nmpoliza
                            ,'params.cdtipimp' : tipoimp
                        }
                        ,success : function(response)
                        {
                            _setLoading(false,loadCmp);
                            var ck = 'Decodificando respuesta al generar remesa';
                            try
                            {
                                var json = Ext.decode(response.responseText);
                                debug('### remesa:',json);
                                if(json.success == true)
                                {
                                    mensajeCorrecto(
                                        'Impresi\u00f3n de remesa'
                                        ,json.params.nueva=='S' ? 'Se gener\u00f3 la remesa '+json.params.remesa : 'Se recuper\u00f3 la remesa '+json.params.remesa
                                        ,function()
                                        {
                                            var ck = 'Invocando ventana de impresi\u00f3n';
                                            try
                                            {
                                                _fieldById('_impLot_ventana').destroy();
                                                var venImp = Ext.create('VentanaImpresionLote',
                                                {
                                                    lote        : json.params.lote
                                                    ,cdtipram   : json.params.cdtipram
                                                    ,cdtipimp   : json.params.cdtipimp
                                                    ,tipolote   : 'P'
                                                    ,callback   : callback
                                                    ,closable   : true
                                                    ,cancelable : true
                                                });
                                                centrarVentanaInterna(venImp.show());
                                            }
                                            catch(e)
                                            {
                                                manejaException(e,ck);
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
                            _setLoading(false,loadCmp);
                            errorComunicacion(null,'Error al generar remesa');
                        }
                    });
                }
            }));
        };
        *
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}
*/
/*
function _generarRemesaClic2(required,cdunieco,cdramo,estado,nmpoliza,callback,marcar,ntramite)
{
	callback();
}
script type="text/javascript" src="${defines}VentanaImpresionLote.js?n=${now}"  /script
*/
////// funciones //////
<s:if test="false">
</script>
</s:if>
</script>
<script>