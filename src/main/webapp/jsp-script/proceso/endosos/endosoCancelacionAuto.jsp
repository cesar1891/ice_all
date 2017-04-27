<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p41_urlConfirmarEndoso         = '<s:url namespace="/endosos" action="confirmarEndosoCancelacionEndoso" />';
var _p41_urlRecuperacionSimpleLista = '<s:url namespace="/emision" action="recuperacionSimpleLista"        />';
////// urls //////

////// variables //////
var _p41_smap1  = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
debug('_p41_smap1:',_p41_smap1);

var _p41_flujo  = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;
debug('_p41_flujo:',_p41_flujo);
debug('TipoEndoso', TipoEndoso.CancelacionAutomaticaEndodo);

var _p41_storeEndosos = null;
////// variables //////

////// componentes dinamicos //////
var _p41_formLecturaItems   = [ <s:property value="imap.formLecturaItems"   escapeHtml="false" /> ];
var _p41_formEndosoItems    = [ <s:property value="imap.formEndosoItems"    escapeHtml="false" /> ];
var _p41_gridEndososColumns = [ <s:property value="imap.gridEndososColumns" escapeHtml="false" /> ];
var _p41_modeloEndosoFields = [ <s:property value="imap.modeloEndosoFields" escapeHtml="false" /> ];
////// componentes dinamicos //////

Ext.onReady(function()
{
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
    ////// overrides //////
    ////// overrides //////
    
    ////// modelos //////
    Ext.define('_p41_modeloEndoso',
    {
        extend  : 'Ext.data.Model'
        ,fields : _p41_modeloEndosoFields
    });
    ////// modelos //////
    
    ////// stores //////
    _p41_storeEndosos = Ext.create('Ext.data.Store',
    {
        model     : '_p41_modeloEndoso'
        ,autoLoad : false
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p41_urlRecuperacionSimpleLista
            ,extraParams :
            {
                'smap1.procedimiento' : !Ext.isEmpty(_p41_smap1.cdtipsup) && _p41_smap1.cdtipsup==TipoEndoso.CancelacionAutomaticaEndodo?'RECUPERAR_ENDOSOS_CANCELABLES':'RECUPERAR_ENDOSOS_SINIESTRALIDAD'
                ,'smap1.cdunieco'     : _p41_smap1.CDUNIECO
                ,'smap1.cdramo'       : _p41_smap1.CDRAMO
                ,'smap1.estado'       : _p41_smap1.ESTADO
                ,'smap1.nmpoliza'     : _p41_smap1.NMPOLIZA
            }
            ,reader      :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,successProperty : 'exito'
                ,messageProperty : 'respuesta'
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p41_divpri'
        ,itemId   : '_p41_panelpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId  : '_p41_formLectura'
                ,title  : 'Datos de p&oacute;liza'
                ,layout :
                {
                    type     : 'table'
                    ,columns : 3
                }
                ,defaults : { style : 'margin:5px;' }
                ,items  : _p41_formLecturaItems
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId     : '_p41_gridEndosos'
                ,title     : 'Endosos cancelables'
                ,columns   : _p41_gridEndososColumns
                ,store     : _p41_storeEndosos
                ,minHeight : 150
                ,maxHeight : 400
                ,selModel  :
                {
                    selType        : 'checkboxmodel'
                    ,allowDeselect : true
                    ,mode          : 'SINGLE'
                    ,listeners     :
                    {
                        selectionchange : function(me,selected,eOpts)
                        {
                            debug('selected:',selected);
                            if(selected.length==1)
                            {
                                _fieldByName('feefecto').setValue(selected[0].get('FEINICIO'));
                            }
                            else
                            {
                                _fieldByName('feefecto').setValue('');
                            }
                        }
                    }
                }
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId  : '_p41_formEndoso'
                ,title  : 'Datos de endoso'
                ,layout :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,defaults : { style : 'margin:5px;' }
                ,items    : _p41_formEndosoItems
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        itemId   : '_p41_botonConfirmar'
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
                            var record = _fieldById('_p41_gridEndosos').getSelectionModel().getSelection()[0];
                            
                            var paramsConfirmar =
                            {
                                'smap1.cdunieco'  : _p41_smap1.CDUNIECO
                                ,'smap1.cdramo'   : _p41_smap1.CDRAMO
                                ,'smap1.estado'   : _p41_smap1.ESTADO
                                ,'smap1.nmpoliza' : _p41_smap1.NMPOLIZA
                                ,'smap1.cdtipsup' : _p41_smap1.cdtipsup
                                ,'smap1.nsuplogi' : record.get('NSUPLOGI')
                                ,'smap1.cddevcia' : record.get('CDDEVCIA')
                                ,'smap1.cdgestor' : record.get('CDGESTOR')
                                ,'smap1.feemisio' : record.raw['FEEMISIO']
                                ,'smap1.feinival' : record.raw['FEINIVAL']
                                ,'smap1.fefinval' : record.raw['FEFINVAL']
                                ,'smap1.feefecto' : record.raw['FEEFECTO']
                                ,'smap1.feproren' : record.raw['FEPROREN']
                                ,'smap1.cdmoneda' : record.get('CDMONEDA')
                                ,'smap1.nmsuplem' : record.get('NMSUPLEM')
                                ,'smap1.feinicio' : record.raw['FEINICIO']
                            };
                            
                            if(!Ext.isEmpty(_p41_flujo))
                            {
                                paramsConfirmar['flujo.ntramite']  = _p41_flujo.ntramite;
                                paramsConfirmar['flujo.status']    = _p41_flujo.status;
                                paramsConfirmar['flujo.cdtipflu']  = _p41_flujo.cdtipflu;
                                paramsConfirmar['flujo.cdflujomc'] = _p41_flujo.cdflujomc;
                                paramsConfirmar['flujo.webid']     = _p41_flujo.webid;
                                paramsConfirmar['flujo.tipoent']   = _p41_flujo.tipoent;
                                paramsConfirmar['flujo.claveent']  = _p41_flujo.claveent;
                                paramsConfirmar['flujo.cdunieco']  = _p41_flujo.cdunieco;
                                paramsConfirmar['flujo.cdramo']    = _p41_flujo.cdramo;
                                paramsConfirmar['flujo.estado']    = _p41_flujo.estado;
                                paramsConfirmar['flujo.nmpoliza']  = _p41_flujo.nmpoliza;
                                paramsConfirmar['flujo.nmsituac']  = _p41_flujo.nmsituac;
                                paramsConfirmar['flujo.nmsuplem']  = _p41_flujo.nmsuplem;
                                paramsConfirmar['flujo.aux']       = _p41_flujo.aux;
                            }
                            
                            Ext.Ajax.request(
                            {
                                url      : _p41_urlConfirmarEndoso
                                ,params  : paramsConfirmar
                                ,success : function(response)
                                {
                                    me.enable();
                                    me.setText('Confirmar');
                                    var json = Ext.decode(response.responseText);
                                    debug('### confirmar:',json);
                                    if(json.success)
                                    {
                                        var callbackRemesa = function()
                                        {
                                            marendNavegacion(2);
                                        };
                                        mensajeCorrecto('Endoso generado','Endoso generado',function()
                                        {
                                            _generarRemesaClic(
                                                true
                                                ,_p41_smap1.CDUNIECO
                                                ,_p41_smap1.CDRAMO
                                                ,_p41_smap1.ESTADO
                                                ,_p41_smap1.NMPOLIZA
                                                ,callbackRemesa
                                            );
                                        });
                                    }
                                    else
                                    {
                                        mensajeError(json.respuesta);
                                    }
                                }
                                ,failure : function()
                                {
                                    me.enable();
                                    me.setText('Confirmar');
                                    errorComunicacion();
                                }
                            });
                        }
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    _p41_storeEndosos.load(function(records,op,success)
    {
        if(success&&!Ext.isEmpty(records))
        {
            
        }
        else if(success)
        {
            mensajeError('No hay endosos cancelables');
        }
        else
        {
            mensajeError(op.getError());
        }
    });
    ////// loaders //////
});

////// funciones //////
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body><div id="_p41_divpri" style="height:300px;border:1px solid #999999;"></div></body>
</html>