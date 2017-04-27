<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:if test="false">
<script>
</s:if>
////// variables //////
var _4_urlAutorizarEmision = '<s:url namespace="/"            action="autorizaEmision"         />';
var _4_urlVerDocumentos    = '<s:url namespace="/documentos"  action="ventanaDocumentosPoliza" />';
var _4_urlAutorizarEndoso  = '<s:url namespace="/endosos"     action="autorizarEndoso"         />';
var _4_urlUpdateStatus     = '<s:url namespace="/mesacontrol" action="actualizarStatusTramite" />';
var _4_urlUpdateComments   = '<s:url namespace="/mesacontrol" action="actualizaComentariosTramite" />';
var _4_urlRegresarEmision  = '<s:url namespace="/mesacontrol" action="regresarEmisionEnAutori" />';
var urlReintentarWS ='<s:url namespace="/" action="reintentaWSautos" />';

var _STATUS_EN_ESPERA_DE_AUTORIZACION = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_AUTORIZACION.codigo" />';
var _STATUS_TRAMITE_RECHAZADO         = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';

var _4_windowComentario;
var _4_recordAutoSelected;
var _4_windowComentarioOperacion;
var _4_windowComentarioTipo;
////// variables //////

////// funciones //////
function _4_moverTramite()
{
	debug('>_4_moverTramite');
	var ventana   = _4_windowComentario;
	var record    = _4_recordAutoSelected;
	var coment    = _4_windowComentario.items.items[0].getValue();
	var operacion = _4_windowComentarioOperacion;
	var tipo      = _4_windowComentarioTipo;
	debug('record:',record.data);
	debug('comentario:',coment);
	debug('operacion:',operacion);
	debug('tipo:',tipo);
	if(tipo=='EMISION'&&operacion=='regresar')
	{
		debug('>regresar emision');
		ventana.setLoading(true);
		Ext.Ajax.request(
		{
			url       : _4_urlRegresarEmision
			,jsonData :
			{
				smap1 :
				{
					ntramiteAuto : record.get('ntramite')
					,ntramiteEmi : record.get('parametros.pv_otvalor03')
					,comentario  : coment
				}
			}
		    ,success : function(response)
		    {
		    	ventana.setLoading(false);
		    	var json = Ext.decode(response.responseText);
		    	if(json.success)
		    	{
		    		mensajeCorrecto('Aviso',json.mensaje);
		    		ventana.hide();
		    	}
		    	else
		    	{
		    		mensajeError(json.mensaje);
		    	}
		    }
		    ,failure : function()
		    {
		    	ventana.setLoading(false);
		    	errorComunicacion();
		    }
		});
		debug('<regresar emision');
	}
	else if(tipo=='EMISION'&&operacion=='emitir')
	{
		ventana.setLoading(true);
        debug('Autorizar emision');
        Ext.Ajax.request(
        {
            url       : _4_urlAutorizarEmision
            ,jsonData :
            {
                panel1  : record.raw
                ,panel2 :
                {
                    observaciones : coment
                }
            }
            ,success  : function(response)
            {
                ventana.setLoading(false);
                var jsonResponse = Ext.decode(response.responseText);
                debug('jsonResponse:',jsonResponse);
                if(jsonResponse.success)
                {
	                ventana.hide();
                    mensajeCorrecto('Aviso',jsonResponse.mensajeRespuesta, function(){
                    	if(!Ext.isEmpty(jsonResponse.nmpolAlt)){
                    		mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + jsonResponse.nmpolAlt);
                    	}
                    });
                }
                else
                {
                    mensajeError(jsonResponse.mensajeRespuesta, function(){
                    	if(jsonResponse.retryWS){
                    		var paramsWS = {
                                    'panel1.pv_nmpoliza'  : record.get('nmpoliza')
                                    ,'panel1.pv_ntramite' : record.get('otvalor03')
                                    ,'panel2.pv_cdramo'   : record.get('cdramo')
                                    ,'panel2.pv_cdunieco' : record.get('cdunieco')
                                    ,'panel2.pv_estado'   : "M"
                                    ,'panel2.pv_nmpoliza' : record.get('nmpoliza')
                                    ,'panel2.pv_cdtipsit' : record.get('cdtipsit')
                                    ,'nmpoliza'           : jsonResponse.nmpoliza
                                    ,'nmsuplem'           : jsonResponse.nmsuplem
                                    ,'cdIdeper'           : jsonResponse.cdIdeper
                        		};
                    		reintentarWSAuto(ventana, paramsWS);
                    	}
                    });
                }
            }
            ,failure  : function()
            {
                ventana.setLoading(false);
                errorComunicacion();
            }
        });
    }
	else if(tipo!='EMISION'&&operacion=='emitir')
	{
		ventana.setLoading(true);
        Ext.Ajax.request(
        {
            url       : _4_urlAutorizarEndoso
            ,params   :
            {
                'smap1.ntramiteemi'  : record.get('parametros.pv_otvalor08')
                ,'smap1.ntramiteend' : record.get('parametros.pv_otvalor03')
                ,'smap1.cdunieco'    : record.get('cdunieco')
                ,'smap1.cdramo'      : record.get('cdramo')
                ,'smap1.estado'      : record.get('estado')
                ,'smap1.nmpoliza'    : record.get('nmpoliza')
                ,'smap1.nmsuplem'    : record.get('nmsuplem')
                ,'smap1.nsuplogi'    : record.get('parametros.pv_otvalor07')
                ,'smap1.cdtipsup'    : record.get('parametros.pv_otvalor06')
                ,'smap1.status'      : '3'
                ,'smap1.fechaEndoso' : Ext.Date.format(record.get('ferecepc'),'d/m/Y')
                ,'smap1.observacion' : coment
            }
            ,success  : function(response)
            {
                ventana.setLoading(false);
                var json=Ext.decode(response.responseText);
                if(json.success==true)
                {
                	ventana.hide();
                    Ext.Ajax.request
                    ({
                        url     : _4_urlUpdateStatus
                        ,params : 
                        {
                            'smap1.ntramite'  : record.get('ntramite')
                            ,'smap1.status'   : '3'//confirmado 
                            ,'smap1.comments' : ''
                        }
                    });
                    mensajeCorrecto('Aviso','Endoso autorizado');
                }
                else
                {
                    mensajeError(json.error);
                }
            }
            ,failure  : function()
            {
                ventana.setLoading(false);
                errorComunicacion();
            }
        });
    }
	debug('<_4_moverTramite');
}

function _4_onRegresarClick(grid,rowIndex)
{
	debug('>_4_onRegresarClick');
	_4_recordAutoSelected=grid.getStore().getAt(rowIndex);
	debug('_4_recordAutoSelected:',_4_recordAutoSelected);
	var valido = true;
	if(valido)
	{
		valido = _4_recordAutoSelected.get('status')=='11';
		if(!valido)
		{
			mensajeWarning('Este tr&aacute;mite no se puede regresar, verifique el status');
		}
	}
	if(valido)
	{
		valido = _4_recordAutoSelected.get('parametros.pv_otvalor05')=='EMISION';
		if(!valido)
		{
			mensajeWarning('Solo se pueden regresar tr&aacute;mites de emisi&oacute;n');
		}
	}
	if(valido)
	{
		_4_windowComentarioTipo=_4_recordAutoSelected.get('parametros.pv_otvalor05');
		_4_windowComentarioOperacion = 'regresar';
		_4_windowComentario.items.items[0].setValue('');
		_4_windowComentario.setTitle('Regresar tr&aacute;mite');
		_4_windowComentario.show();
		centrarVentanaInterna(_4_windowComentario);
	}
	debug('<_4_onRegresarClick');
}

function _4_onFolderClick(grid,rowIndex)
{
	//return _4_onRegresarClick(grid,rowIndex);
    debug(rowIndex);
    var record=grid.getStore().getAt(rowIndex);
    debug(record);
    Ext.create('Ext.window.Window',
    {
        title        : 'Documentaci&oacute;n'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 600
        ,height      : 400
        ,autoScroll  : true
        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
        ,loader      :
        {
            url       : _4_urlVerDocumentos
            ,params   :
            {
                'smap1.nmpoliza'  : record.get('nmpoliza')&&record.get('nmpoliza').length>0?record.get('nmpoliza'):'0'
                ,'smap1.cdunieco' : record.get('cdunieco')
                ,'smap1.cdramo'   : record.get('cdramo')
                ,'smap1.estado'   : record.get('estado')
                ,'smap1.nmsuplem' : '0'
                ,'smap1.ntramite' : record.get('parametros.pv_otvalor03')
                ,'smap1.nmsolici' : record.get('nmsolici')&&record.get('nmsolici').length>0?record.get('nmsolici'):'0'
                ,'smap1.tipomov'  : '0'
            }
            ,scripts  : true
            ,autoLoad : true
        }
    }).show();
}

function _4_preVerComments(grid,rowIndex)
{
	debug('>_4_preVerComments');
	debug(grid.getStore().getAt(rowIndex));
	var ventana = Ext.create('Ext.window.Window',
	{
		title   : 'Observaciones'
		,modal  : true
		,width  : 600
		,height : 400
		,html   : grid.getStore().getAt(rowIndex).get('comments')
	});
	ventana.show();
	centrarVentanaInterna(ventana);
	debug('<_4_preVerComments');
}

function _4_preAutorizarEmision(grid,rowIndex)
{
	debug('>_4_preAutorizarEmision');
    _4_recordAutoSelected=grid.getStore().getAt(rowIndex);
    debug('_4_recordAutoSelected:',_4_recordAutoSelected);
    var valido = true;
    if(valido)
    {
        valido = _4_recordAutoSelected.get('status')=='11';
        if(!valido)
        {
            mensajeWarning('Este tr&aacute;mite no se puede autorizar, verifique el status');
        }
    }
    if(valido)
    {
        _4_windowComentarioTipo=_4_recordAutoSelected.get('parametros.pv_otvalor05');
        _4_windowComentarioOperacion = 'emitir';
        _4_windowComentario.items.items[0].setValue('');
        _4_windowComentario.setTitle('Autorizar tr&aacute;mite');
        _4_windowComentario.show();
        centrarVentanaInterna(_4_windowComentario);
    }
    debug('<_4_preAutorizarEmision');
}

function rechazarTramiteWindow(grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	var windowRechazo;
	debug("Estatus al rechazar",record.get('status'));
	
	if(record.get('status') != _STATUS_EN_ESPERA_DE_AUTORIZACION){
		mensajeWarning('Este tr&aacute;mite no se puede rechazar');
		return;
	}
	
	var comentariosText = Ext.create('Ext.form.field.TextArea', {
        fieldLabel: 'Observaciones'
        ,labelWidth: 150
        ,width: 600
        ,name:'smap1.comments'
        ,height: 250
    });
    
	
	var panelRechazoEmi = Ext.create('Ext.form.Panel', {
        title: 'Rechazar autorizaci&oacute;n de emisi&oacute;n',
        width: 650,
        url: _4_urlUpdateStatus,
        bodyPadding: 5,
        items: [comentariosText,{
        	xtype       : 'radiogroup'
            ,fieldLabel : 'Mostrar al agente'
            ,columns    : 2
            ,width      : 250
            ,style      : 'margin:5px;'
            ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
            ,items      :
            [
                {
                    boxLabel    : 'Si'
                    ,itemId     : 'SWAGENTE2'
                    ,name       : 'SWAGENTE2'
                    ,inputValue : 'S'
                    ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                }
                ,{
                    boxLabel    : 'No'
                    ,name       : 'SWAGENTE2'
                    ,inputValue : 'N'
                    ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                }
            ]
        }],
        buttonAlign:'center',
        buttons: [{
            text: 'Rechazar',
            icon    : '${ctx}/resources/fam3icons/icons/accept.png',
            buttonAlign : 'center',
            handler: function() {
    	    	if (panelRechazoEmi.form.isValid()) {
    	    		panelRechazoEmi.form.submit({
    		        	waitMsg:'Procesando...',
    		        	params: {
    		        		'smap1.ntramite' : record.get('ntramite')
    		        		,'smap1.status'   : _STATUS_TRAMITE_RECHAZADO
    		        		,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
    		        	},
    		        	failure: function(form, action) {
    		        		mensajeError('No se pudo rechazar.');
    					},
    					success: function(form, action) {
    						panelRechazoEmi.form.submit({
            		        	waitMsg:'Procesando...',
            		        	params: {
            		        		'smap1.ntramite' : record.get('parametros.pv_otvalor03'),
            		        		'smap1.status'   : _STATUS_TRAMITE_RECHAZADO,
            		        	},
            		        	failure: function(form, action) {
            		        		mensajeError('No se pudo rechazar.');
            					},
            					success: function(form, action) {
            						mensajeCorrecto('Aviso','Se ha rechazado correctamente.');
            						loadMcdinStore();
            						windowRechazo.close();
            					}
            				});
    						
    						Ext.Ajax.request({
    							url: _4_urlUpdateComments,
    							params: {
    					    		'tmpNtramite' : record.get('ntramite'),
    					    		'mensaje'     : record.get('comments')+"</br>Observaciones del rechazo de autorizaci&oacute;n:</br>"+panelRechazoEmi.down('[name=smap1.comments]').getValue()
    							}
    						});
    					}
    				});
    			} else {
    				Ext.Msg.show({
    	                   title: 'Aviso',
    	                   msg: 'Complete la informaci&oacute;n requerida',
    	                   buttons: Ext.Msg.OK,
    	                   icon: Ext.Msg.WARNING
    	               });
    			}
    		}
        },{
            text: 'Cancelar',
            icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
            buttonAlign : 'center',
            handler: function() {
            	windowRechazo.close();
            }
        }
        ]
    });
	
    windowRechazo = Ext.create('Ext.window.Window',{
        modal       : true,
        buttonAlign : 'center',
        width       : 663,
        height      : 430,
        autoScroll  : true,
        items       : [panelRechazoEmi]
    }).show();
    centrarVentana(windowRechazo);	
}

//funcion para reintentar WS auto

function reintentarWSAuto(loading, params){

	Ext.Msg.show({
       title    :'Confirmaci&oacute;n'
       ,msg     : '&iquest;Desea Reenviar los Web Services de Autos?'
       ,buttons : Ext.Msg.YESNO
       ,icon    : Ext.Msg.QUESTION
       ,fn      : function(boton, text, opt){
       	if(boton == 'yes'){
       		
       		loading.setLoading(true);
        	
        	Ext.Ajax.request(
                	{
                		url     : urlReintentarWS
                		,timeout: 240000
                		,params :params
                	    ,success:function(response)
                	    {
                	    	loading.setLoading(false);
                	    	var json=Ext.decode(response.responseText);
                	    	debug(json);
                	    	if(json.success==true)
                	    	{
                	    		mensajeCorrecto('Aviso', 'Ejecuci&oacute;n Correcta de Reintento. P&oacute;liza Emitida: ' + json.nmpolAlt);
                	    	}
                	    	else
                	    	{
                	    		Ext.Msg.show({
                                    title    :'Aviso'
                                    ,msg     : json.mensajeRespuesta
                                    ,buttons : Ext.Msg.OK
                                    ,icon    : Ext.Msg.WARNING
                                    ,fn      : function(){
                                    	reintentarWSAuto(loading, params);
                                    }
                                });
                	    	}
                	    }
                	    ,failure:function()
                	    {
                	    	loading.setLoading(false);
                	    	Ext.Msg.show({
                                title:'Error',
                                msg: 'Error de comunicaci&oacute;n',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                                ,fn      : function(){
                                	reintentarWSAuto(loading, params);
                                }
                            });
                	    }
                	});
       	}
       }
	});
	                	
}
////// funciones //////

Ext.onReady(function()
{
	////// modelos //////
	////// modelos //////
	
	////// stores //////
	////// stores //////
	
	////// componentes //////
	////// componentes //////
	
	////// contenido //////
	_4_windowComentario = Ext.create('Ext.window.Window',
	{
		width        : 300
		,height      : 200
		,modal       : true
		,buttonAlign : 'center'
		,items       : Ext.create('Ext.form.field.TextArea',
		{
			width   : 285
			,height : 120
		})
		,buttons     :
		[
		    {
		    	text     : 'Enviar'
		    	,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
		    	,handler : _4_moverTramite
		    }
		    ,{
		    	text     : 'Cancelar'
		    	,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
		    	,handler : function()
		    	{
		    		_4_windowComentario.hide();
		    	}
		    }
		]
	});
	////// contenido //////
});
<s:if test="false">
</script>
</s:if>