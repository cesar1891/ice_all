<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script>
			////// variables //////
			var _p15_form;
			
			var _p15_urlHabilitarRecibos  = '<s:url namespace="/endosos"                action="habilitarRecibosSubsecuentes" />';
			var _p15_urlReporte           = '<s:url namespace="/reportes"               action="procesoObtencionReporte"      />';
			var _p15_urlObtenerDatosEmail = '<s:url namespace="/reexpediciondocumentos" action="obtenerDatosEmail"            />';
			var _p15_urlEnviarCorreo      = '<s:url namespace="/general"                action="enviaCorreo" />';
			////// variables //////
			
			Ext.onReady(function() {
				
				Ext.Ajax.timeout = 5*60*1000;//se aumenta tiempo de espera de la pantalla a 5 minutos
				
				////// modelos //////
				////// modelos //////
				
				////// stores //////
				////// stores //////
				
				////// componentes //////
				////// componentes //////
				
				////// contenido //////
				_p15_form = Ext.create('Ext.form.Panel', {
			        title  : 'Habilitar recibos subsecuentes',
			        layout : {
			            type     : 'table',
			            columns : 2
			        },
			        defaults : {
			            style : 'margin:5px;'
			        },
			        buttonAlign : 'center',
			        buttons : [
			            {
			            	text     : 'Habilitar recibos',
			            	icon    : '${ctx}/resources/fam3icons/icons/folder_go.png',
			            	handler : _p15_botonHabilitarHandler
			            }
			        ],
			        items : [{
			                xtype       : 'datefield'
			                ,itemId     : '_p15_fechaInicio'
			                ,name       : 'fechaInicio'
			                ,fieldLabel : 'Fecha de proceso'
			                ,value      : new Date()
			                ,allowBlank : false
			            },{
				                xtype       : 'datefield'
				                ,itemId     : '_p15_fechaFin'
				                ,name       : 'fechaFin'
				                ,fieldLabel : 'Fecha de fin'
				                ,readOnly   : true
				        },<s:property value="imap1.itemsDeFormulario" />
			        ]
				});
				
				Ext.create('Ext.panel.Panel', {
					renderTo  : '_p15_divpri',
					defaults : {
			            style : 'margin:5px;'
			        },
				    border   : 0,
					items    : [
					    _p15_form
					]
				});
				////// contenido //////
				
				////// eventos //////
				_p15_getFechaInicio().addListener('change',_p15_fechaInicioChange);
				_p15_fechaInicioChange(_p15_getFechaInicio(),new Date(),null);
				////// eventos //////
			});
			
			////// getters //////
			function _p15_getFechaInicio() {
				return Ext.ComponentQuery.query('#_p15_fechaInicio')[0];
			}
			
			function _p15_getFechaFin() {
			    return Ext.ComponentQuery.query('#_p15_fechaFin')[0];
			}
			
			function _p15_getFormulario() {
				return _p15_form;
			}
			////// getters //////
			
			
			////// funciones //////
			function _p15_botonHabilitarHandler() {
				debug('>_p15_botonHabilitarHandler');
				var form = _p15_getFormulario();
				var valido = form.isValid();
				if(!valido) {
					datosIncompletos();
				}
				if(valido) {
					form.setLoading(true);
					// Se habilitan los recibos subsecuentes y se obtiene una lista de las sucursales:
					Ext.Ajax.request({
						url      : _p15_urlHabilitarRecibos,
						jsonData : {
							smap1 : form.getValues()
						},
					    success  : function(response) {
					    	form.setLoading(false);
					    	var response = Ext.decode(response.responseText);
					    	debug('response:',response);
					    	if(response.success) {
					    		//mensajeCorrecto('Recibos habilitados',response.mensaje);
					    		var listaCdunieco=response.slist1;
								debug('listaCdunieco:',listaCdunieco);
					    		if(listaCdunieco.length==0) {
					    			// Se mandan los bytes del reporte a la UI:
					    			Ext.create('Ext.form.Panel').submit({
			                            standardSubmit : true,
			                            url:_p15_urlReporte,
			                            params: {
			                                cdreporte : 'REPEXC008',
			                                'params.pv_feproces_i' : Ext.Date.format(_p15_getFechaInicio().getValue(),'d/m/Y')
			                            },
			                            success: function(form, action) {
			                                
			                            },
			                            failure: function(form, action) {
			                                switch (action.failureType) {
			                                    case Ext.form.action.Action.CONNECT_FAILURE:
			                                        Ext.Msg.alert('Error', 'Error de comunicaci&oacute;n');
			                                        break;
			                                    case Ext.form.action.Action.SERVER_INVALID:
			                                    case Ext.form.action.Action.LOAD_FAILURE:
			                                        Ext.Msg.alert('Error', 'Error del servidor, consulte a soporte');
			                                        break;
			                               }
			                            }
			                        });
					    		}
					    		var contador=0;
					    		var respuestaEnvioMail='';
					    		for(var i=0;i<listaCdunieco.length;i++) {
						    		form.setLoading(true);
						    		// Se obtienen los datos del email para enviar informacion de la sucursal:
				                    Ext.Ajax.request({
				                        url    : _p15_urlObtenerDatosEmail,
										params : {
											'stringMap.cdunieco':listaCdunieco[i].CDUNIECO
										},
				                        success : function(response) {
				                            form.setLoading(false);
				                            var json=Ext.decode(response.responseText);
				                            debug('email data response:',json);
				                            if(json.success) {
				                            	form.setLoading(true);
				                            	// Se envia el correo con informacion de la sucursal:
				                            	Ext.Ajax.request({
				                                    url    : _p15_urlEnviarCorreo,
				                                    params : {
				                                        to         : json.stringMap.correos,
				                                        asunto     : 'Recibos subsecuentes habilitados',
				                                        mensaje    : 'Se habilitaron recibos subsecuentes a partir de la fecha '
				                                                        + Ext.Date.format(_p15_getFechaInicio().getValue(),'d/m/Y'),
				                                        urlArchivo : json.stringMap.url
							                                            + '?cdreporte=REPEXC008'
							                                            + '&params.pv_feproces_i=' + Ext.Date.format(_p15_getFechaInicio().getValue(),'d/m/Y')
							                                            + '&params.pv_cdunieco_i=' + json.stringMap.cdunieco,
				                                        nombreArchivo : 'recibos_habilitados_(' +json.stringMap.cdunieco +')_'+ Ext.Date.format(new Date(),'Y-m-d_(g_i_s_u)') + '.xls'
				                                    },
				                                    callback : function(options,success,response) {
				                                    	
				                                    	if(success) {
				                                    		respuestaEnvioMail += 'Correo de sucursal ' + json.stringMap.cdunieco + ' enviado<br/>';
				                                    	} else {
				                                    		respuestaEnvioMail += '<font style="color:red;">Error al enviar correo de sucursal <strong>' 
				                                    		                          + json.stringMap.cdunieco + '</strong></font><br/>';
				                                    	}
				                                    	// Si ya se ejecutaron todas las peticiones de envio de correo: 
														if(contador==listaCdunieco.length-1) {
															form.setLoading(false);
															// Se muestra el conjunto de mensajes de respuesta de envio de emails:
															debug(respuestaEnvioMail);
														    mensajeInfo(respuestaEnvioMail);
															
															// Se mandan los bytes del reporte a la UI:
															Ext.create('Ext.form.Panel').submit({
													            standardSubmit : true,
													            url:_p15_urlReporte,
													            params: {
													                cdreporte : 'REPEXC008'
													                ,'params.pv_feproces_i' : Ext.Date.format(_p15_getFechaInicio().getValue(),'d/m/Y')
													                ,'params.pv_cdunieco_i' : _p15_form.getValues().cdunieco
													            },
													            success: function(form, action) {
													                
													            },
													            failure: function(form, action) {
													                switch (action.failureType) {
													                    case Ext.form.action.Action.CONNECT_FAILURE:
													                        Ext.Msg.alert('Error', 'Error de comunicaci&oacute;n');
													                        break;
													                    case Ext.form.action.Action.SERVER_INVALID:
													                    case Ext.form.action.Action.LOAD_FAILURE:
													                        Ext.Msg.alert('Error', 'Error del servidor, consulte a soporte');
													                        break;
													               }
													            }
													        });
														}
														contador = contador + 1;
				                                    }
				                                });
				                            } else {
				                                mensajeError(json.mensaje);
				                            }
				                        },
				                        failure : function() {
				                            form.setLoading(false);
				                            errorComunicacion();
				                        }
				                    });
					    		}
					    	} else {
					    		mensajeError(response.mensaje);
					    	}
					    },
					    failure  : function() {
					    	form.setLoading(false);
					    	errorComunicacion();
					    }
					});
				}
				debug('<_p15_botonHabilitarHandler');
			}
			
			
			function _p15_fechaInicioChange(comp,newVal,oldVal) {
				debug('>_p15_fechaInicioChange',newVal,oldVal);
				var fechaFin = Ext.Date.add(newVal,Ext.Date.MONTH,1);
				debug('mas mes:',fechaFin);
				var ultimoDia = Ext.Date.getDaysInMonth(fechaFin);
				debug('ultimo dia:',ultimoDia);
				debug('dias:',fechaFin.getDate());
				fechaFin=Ext.Date.add(fechaFin,Ext.Date.DAY,ultimoDia-fechaFin.getDate());
				_p15_getFechaFin().setValue(fechaFin);
				debug('<_p15_fechaInicioChange',fechaFin);
			}
			////// funciones //////
		</script>
	</head>
    <body>
	   <div id="_p15_divpri" style="height:600px;"></div>
    </body>
</html>