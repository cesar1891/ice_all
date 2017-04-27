<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p45_urlConfirmar    = '<s:url namespace="/endosos" action="confirmarEndosoRehabilitacionPolAuto" />';
var _p45_urlMarcarPoliza = '<s:url namespace="/endosos" action="marcarPolizaParaRehabilitar"          />';
////// urls //////

////// variables //////
var _p45_smap1 = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
debug('_p45_smap1:',_p45_smap1);

var _p45_flujo  = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;
debug('_p45_flujo:',_p45_flujo);

////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p45_panelLecturaItems = [ <s:property value="imap.panelLecturaItems" escapeHtml="false" /> ];
var _p45_formEndosoItems   = [ <s:property value="imap.formEndosoItems"   escapeHtml="false" /> ];
////// componentes dinamicos //////

Ext.onReady(function()
{
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p45_divpri'
        ,itemId   : '_p45_panelpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                title     : 'Datos de p&oacute;liza'
                ,defaults : { style : 'margin:5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    : _p45_panelLecturaItems
            })
            ,Ext.create('Ext.form.Panel',
            {
                title     : 'Datos de endoso'
                ,defaults : { style : 'margin:5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items       : _p45_formEndosoItems
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        itemId   : '_p45_botonConfirmar'
                        ,text    : 'Confirmar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                        ,handler : function(me)
                        {
                            if(!me.up('form').getForm().isValid())
                            {
                                datosIncompletos();
                                return;
                            }
                            
                            me.disable();
                            me.setText('Cargando...');
                            
                            var paramsConfirmar =
                            {
                                    'smap1.cdtipsup'  : _p45_smap1.cdtipsup
                                    ,'smap1.cdunieco' : _p45_smap1.CDUNIECO
                                    ,'smap1.cdramo'   : _p45_smap1.CDRAMO
                                    ,'smap1.estado'   : _p45_smap1.ESTADO
                                    ,'smap1.nmpoliza' : _p45_smap1.NMPOLIZA
                                    ,'smap1.feefecto' : _p45_smap1.FEEFECTO
                                    ,'smap1.feproren' : _p45_smap1.FEPROREN
                                    ,'smap1.fecancel' : _p45_smap1.FECANCEL
                                    ,'smap1.feinival' : Ext.Date.format(_fieldByName('feinival').getValue(),'d/m/Y')
                                    ,'smap1.cdrazon'  : _p45_smap1.CDRAZON
                                    ,'smap1.cdperson' : _p45_smap1.CDPERSON
                                    ,'smap1.cdmoneda' : _p45_smap1.CDMONEDA
                                    ,'smap1.nmcancel' : _p45_smap1.NMCANCEL
                                    ,'smap1.comments' : ''
                                    ,'smap1.nmsuplem' : _p45_smap1.NMSUPLEM
                            };
                            
                            if(!Ext.isEmpty(_p45_flujo))
                            {
                                paramsConfirmar['flujo.ntramite']  = _p45_flujo.ntramite;
                                paramsConfirmar['flujo.status']    = _p45_flujo.status;
                                paramsConfirmar['flujo.cdtipflu']  = _p45_flujo.cdtipflu;
                                paramsConfirmar['flujo.cdflujomc'] = _p45_flujo.cdflujomc;
                                paramsConfirmar['flujo.webid']     = _p45_flujo.webid;
                                paramsConfirmar['flujo.tipoent']   = _p45_flujo.tipoent;
                                paramsConfirmar['flujo.claveent']  = _p45_flujo.claveent;
                                paramsConfirmar['flujo.cdunieco']  = _p45_flujo.cdunieco;
                                paramsConfirmar['flujo.cdramo']    = _p45_flujo.cdramo;
                                paramsConfirmar['flujo.estado']    = _p45_flujo.estado;
                                paramsConfirmar['flujo.nmpoliza']  = _p45_flujo.nmpoliza;
                                paramsConfirmar['flujo.nmsituac']  = _p45_flujo.nmsituac;
                                paramsConfirmar['flujo.nmsuplem']  = _p45_flujo.nmsuplem;
                                paramsConfirmar['flujo.aux']       = _p45_flujo.aux;
                            }
                            
                            Ext.Ajax.request(
                            {
                                url      : _p45_urlConfirmar
                                ,params  : paramsConfirmar
                                ,success : function(response)
                                {
                                    me.setText('Confirmar');
                                    me.enable();
                                    var json = Ext.decode(response.responseText);
                                    debug('### confirmar:',json);
                                    if(json.success)
                                    {
                                        mensajeCorrecto('Endoso generado','Endoso generado');
                                        marendNavegacion(2);
                                    }
                                    else
                                    {
                                        mensajeError(json.respuesta);
                                    }
                                }
                                ,failure : function()
                                {
                                    me.setText('Confirmar');
                                    me.enable();
                                }
                            });
                        }
                    }
                ]
            })
        ]
    })
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    Ext.Ajax.request(
    {
        url      : _p45_urlMarcarPoliza
        ,params  :
        {
            'smap1.cdunieco'  : _p45_smap1.CDUNIECO
            ,'smap1.cdramo'   : _p45_smap1.CDRAMO
            ,'smap1.nmpoliza' : _p45_smap1.NMPOLIZA
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### marcar:',json);
            if(json.success)
            {
                _fieldByName('feinival').setValue(json.smap1.FECANCEL);
                _fieldByName('_FEEFECTO').setValue(json.smap1.FEEFECTO);
                _fieldByName('_FEPROREN').setValue(json.smap1.FEVENCIM);
                _fieldByName('_FECANCEL').setValue(json.smap1.FECANCEL);
                
                _p45_smap1['FEEFECTO'] = json.smap1.FEEFECTO;
                _p45_smap1['FEPROREN'] = json.smap1.FEVENCIM;
                _p45_smap1['FECANCEL'] = json.smap1.FECANCEL;
                _p45_smap1['CDRAZON']  = json.smap1.CDRAZON;
                _p45_smap1['CDPERSON'] = json.smap1.CDPERSON;
                _p45_smap1['CDMONEDA'] = json.smap1.CDMONEDA;
                _p45_smap1['NMCANCEL'] = json.smap1.NMCANCEL;
                _p45_smap1['NMSUPLEM'] = json.smap1.NMSUPLEM;
                
                debug('_p45_smap1:',_p45_smap1);
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
    ////// loaders //////
});

////// funciones //////
////// funciones //////
</script>
</head>
<body><div id="_p45_divpri" style="height:250px;border:1px solid #999999;"></div></body>
</html>