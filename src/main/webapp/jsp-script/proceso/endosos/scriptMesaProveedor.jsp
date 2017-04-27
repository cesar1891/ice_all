<%@ include file="/taglibs.jsp"%>
<s:if test="false">
<script>
</s:if>
var _CONTEXT = '${ctx}';


/* ******************** CATALOGOS ******************** */

// Catalogo Tipos de pago a utilizar:
var _PAGO_DIRECTO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
var _REEMBOLSO    = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';
var _INDEMNIZACION= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@INDEMNIZACION.codigo" />';
var _URL_CATALOGOS                      = '<s:url namespace="/catalogos"   action="obtieneCatalogo" />';
// Catalogo Estatus de tramite a utilizar:
var _STATUS_TRAMITE_EN_REVISION_MEDICA      = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_REVISION_MEDICA.codigo" />';
var _STATUS_TRAMITE_RECHAZADO               = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';
var _STATUS_TRAMITE_EN_CAPTURA              = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA.codigo" />';
var _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_ASIGNACION.codigo" />';
var _STATUS_TRAMITE_CONFIRMADO              = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@CONFIRMADO.codigo" />';
var _CAT_DESTINOPAGO                        = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@DESTINOPAGO"/>';
var _CAT_CONCEPTO                           = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CATCONCEPTO"/>';
var _STATUS_DEVOLVER_TRAMITE				= '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@TRAMITE_EN_DEVOLUCION.codigo" />';


// Catalogo Tipos de tramite a utilizar:
var _TIPO_TRAMITE_SINIESTRO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@SINIESTRO.cdtiptra" />';
/* *************************************************** */
var _Url_AltaTramite_Previo      		= '<s:url namespace="/siniestros" 	action="includes/altaCitasMedica"      />';

var _UrlAutorizacionServicio    	= '<s:url namespace="/siniestros" 	action="autorizacionServicios" />';




// URLs:
var _UrlDocumentosPoliza        		= '<s:url namespace="/documentos" 	action="ventanaDocumentosPoliza"   />';
var _URL_CATALOGOS              		= '<s:url namespace="/catalogos"   	action="obtieneCatalogo" />';
var _URL_ActualizaStatusTramite 		= '<s:url namespace="/mesacontrol" 	action="actualizarStatusTramite" />';
var _URL_TurnarAOperadorReclamacion 	= '<s:url namespace="/mesacontrol" 	action="turnarAOperadorReclamacion" />';
var panDocUrlViewDoc     				= '<s:url namespace ="/documentos" 	action="descargaDocInline" />';
var mesConUrlDetMC        				= '<s:url namespace="/mesacontrol" 	action="obtenerDetallesTramite"    />';
var mesConUrlFinDetalleMC 				= '<s:url namespace="/mesacontrol" 	action="finalizarDetalleTramiteMC" />';
//AUTORIZACION DE SERVICIO
var _Url_ComplementoCitasMedicas  		= '<s:url namespace="/siniestros" 	action="complementoCitasMedicas"      />';
var _4_urlPantallaAutServ 				= '<s:url namespace="/siniestros" 	action="autorizacionServicios" />';
var _UrlRevisionDocsSiniestro   		= '<s:url namespace="/siniestros" 	action="includes/revisionDocumentos"        />';
var _UrlValidaDocumentosCargados		= '<s:url namespace="/siniestros" 	action="validaDocumentosCargados"        />';
var _UrlGenerarContrarecibo     		= '<s:url namespace="/siniestros" 	action="generarContrarecibo"       />';
var _URL_CONSULTA_TRAMITE       		= '<s:url namespace="/siniestros"   action="consultaListadoMesaControl" />';
var _URL_CONSULTA_GRID_ALTA_TRAMITE 	= '<s:url namespace="/siniestros"   action="consultaListadoAltaTramite" />';
var _UrlGeneraSiniestroTramite 			= '<s:url namespace="/siniestros" 	action="generaSiniestroTramite" />';
var _URL_VALIDA_FACTURAASEGURADO  		= '<s:url namespace="/siniestros"	action="validarFacturaAsegurado" />';
var _URL_NOMBRE_TURNADO   				= '<s:url namespace="/siniestros" 	action="obtieneUsuarioTurnado" />';

//SINIESTROS
var _UrlRechazarTramiteWindwow  		= '<s:url namespace="/siniestros" 	action="includes/rechazoReclamaciones" />';
var _UrlDetalleSiniestro        		= '<s:url namespace="/siniestros" 	action="detalleSiniestro" />';
var _UrlDetalleSiniestroDirecto 		= '<s:url namespace="/siniestros" 	action="afiliadosAfectados"        />';
var _UrlSolicitarPago           		= '<s:url namespace="/siniestros" 	action="solicitarPago"             />';
var _URL_CONCEPTODESTINO        		= '<s:url namespace="/siniestros"   action="guardarConceptoDestino" />';
var _mesasin_url_lista_reasignacion 	= '<s:url namespace="/siniestros" 	action="obtenerUsuariosPorRol" />';
var _selCobUrlAvanza              		= '<s:url namespace="/siniestros" 	action="afiliadosAfectados"/>';
var _urlSeleccionCobertura      		= '<s:url namespace="/siniestros" 	action="seleccionCobertura"        />';
var _URL_VAL_AJUSTADOR_MEDICO			= '<s:url namespace="/siniestros" 	action="consultaDatosValidacionAjustadorMed"/>';
var _URL_INF_ASEGURADO					= '<s:url namespace="/siniestros" 	action="consultaDatosAseguradoSiniestro"/>';
var _URL_POLIZA_UNICA					= '<s:url namespace="/siniestros"  action="consultaPolizaUnica"/>';

var windowLoader;
var msgWindow;

	_4_botonesGrid =
	[
        <s:property value="imap1.gridbuttons" />
	];
	
	function altaTramiteWindow(){
	    windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	       	width       : 500,
	        height      : 320,
	        title: 'Alta de Citas',
	        autoScroll  : true,
	        loader      : {
	            url     : _Url_AltaTramite_Previo,
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
	    centrarVentana(windowLoader);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	function complementarAltaWindow(grid,rowIndex){
		
		var record = grid.getStore().getAt(rowIndex);
		if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
			mensajeWarning('No se puede complementar el tr&aacute;mite ya se encuentra rechazado');
			return;
		}
		else{
			var record = grid.getStore().getAt(rowIndex);
			Ext.create("Ext.form.Panel").submit(
					{
						url     : _Url_ComplementoAltaTramite,
						params :{ 'params.ntramite' : record.get('ntramite')},
						standardSubmit:true
					});
		}
	}
	
	
	function revDocumentosWindow(grid,rowIndex,colIndex){
	    var record = grid.getStore().getAt(rowIndex);
	    
	    windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 600,
	        height      : 400,
	        autoScroll  : true,
	        loader      : {
	            url     : _UrlRevisionDocsSiniestro,
	            params  : {
	                'params.nmTramite'  : record.get('ntramite'),
	                'params.cdTipoPago' : record.get('parametros.pv_otvalor02'),
	                'params.cdTipoAtencion'  : record.get('parametros.pv_otvalor07'),
	                'params.tieneCR'  : !Ext.isEmpty(record.get('parametros.pv_otvalor01'))
	            },
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
	    centrarVentana(windowLoader);
	}
	
	function rechazarTramiteWindow(grid,rowIndex,colIndex){
		
		var record = grid.getStore().getAt(rowIndex);
		if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
			mensajeWarning('Este tr&aacute;mite ya se encuentra rechazado!');
			return;
		}
		
		windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 700,
	        height      : 500,
	        autoScroll  : true,
	        loader      : {
	            url     : _UrlRechazarTramiteWindwow,
	            params  : {
	            	'params.cdunieco' : record.get('cdunieco'),
		    		'params.cdramo'   : record.get('cdramo'),
		    		'params.estado'   : record.get('estado'),
		    		'params.nmpoliza' : record.get('nmpoliza'),
		    		'params.nmsuplem' : record.get('nmsuplem'),
		    		'params.nmsolici' : record.get('nmsolici'),
	                'params.nmTramite'  : record.get('ntramite'),
	                'params.tipopago'   : record.get('parametros.pv_otvalor02')
	            },
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
		
		centrarVentana(windowLoader);
	}
	
	function documentosWindow(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
	    debug('record',record);
	    
	    windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        title       : 'Documentos del siniestro',
	        width       : 600,
	        height      : 400,
	        autoScroll  : true,
	        cls         : 'VENTANA_DOCUMENTOS_CLASS',
	        loader      : {
	            url     : _UrlDocumentosPoliza,
	            params  : {
	                'smap1.ntramite'  : record.get('ntramite')
	                ,'smap1.cdtippag' : record.get('parametros.pv_otvalor02')
	                ,'smap1.cdtipate' : record.get('parametros.pv_otvalor07')
	                ,'smap1.cdtiptra' : _TIPO_TRAMITE_SINIESTRO
                    ,'smap1.cdunieco' : record.get('cdsucdoc')
                    ,'smap1.cdramo'   : record.get('cdramo')
                    ,'smap1.estado'   : record.get('estado')
	                ,'smap1.nmpoliza' : record.get('nmpoliza')
                    ,'smap1.nmsuplem' : '0'
                    ,'smap1.nmsolici' : ''
                    ,'smap1.tipomov'  : record.get('parametros.pv_otvalor02')
	            },
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
	    centrarVentana(windowLoader);
	}
	
	function generaContrareciboWindow(grid,rowIndex,colIndex){
		
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url: _UrlGenerarContrarecibo,
			params: {
		    		'paramsO.pv_cdunieco_i' : record.get('cdunieco'),
		    		'paramsO.pv_cdramo_i'   : record.get('cdramo'),
		    		'paramsO.pv_estado_i'   : record.get('estado'),
		    		'paramsO.pv_nmpoliza_i' : record.get('nmpoliza'),
		    		'paramsO.pv_nmsuplem_i' : record.get('nmsuplem'),
		    		'paramsO.pv_ntramite_i' : record.get('ntramite'),
		    		'paramsO.pv_nmsolici_i' : record.get('nmsolici'),
		    		'paramsO.pv_cdtippag_i' : record.get('parametros.pv_otvalor02'),
		    		'paramsO.pv_cdtipate_i' : record.get('parametros.pv_otvalor07'),
		    		'paramsO.pv_tipmov_i'   : record.get('parametros.pv_otvalor02')
		    	
			},
			success: function(response, opt) {
				var jsonRes=Ext.decode(response.responseText);

				if(jsonRes.success == true){
					loadMcdinStore();
					
					var numRand=Math.floor((Math.random()*100000)+1);
		        	debug('numRand a: ',numRand);
		        	var windowVerDocu=Ext.create('Ext.window.Window',
		        	{
		        		title          : 'Contrarecibo de Documentos del Siniestro'
		        		,width         : 700
		        		,height        : 500
		        		,collapsible   : true
		        		,titleCollapse : true
		        		,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
		        		                 +'src="'+panDocUrlViewDoc+'?subfolder=' + record.get('ntramite') + '&filename=' + '<s:text name="siniestro.contrarecibo.nombre"/>' +'">'
		        		                 +'</iframe>'
		        		,listeners     :
		        		{
		        			resize : function(win,width,height,opt){
		                        debug(width,height);
		                        $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
		                    }
		        		}
		        	}).show();
		        	windowVerDocu.center();
					}else {
						mensajeError(jsonRes.msgResult);
					}
			},
			failure: function(){
				mensajeError('No se pudo generar contrarecibo.');
			}
		});
	}
	
	function turnarAreclamaciones(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		
		Ext.Ajax.request({
			url: _UrlValidaDocumentosCargados,
			params: {
				'params.PV_NTRAMITE_I' : record.get('ntramite'),
				'params.PV_CDRAMO_I'   : record.get('cdramo'),
				'params.PV_cdtippag_I' : record.get('parametros.pv_otvalor02'),
				'params.PV_CDTIPATE_I' : record.get('parametros.pv_otvalor07')
			},
			success: function(response, opt) {
				var jsonRes=Ext.decode(response.responseText);

				if(jsonRes.success == true){
					Ext.Ajax.request({
						url: _URL_VALIDA_FACTURAASEGURADO
						,params	:	{
							'smap.ntramite' : record.get('ntramite'),
							'smap.tipoPago' : record.get('parametros.pv_otvalor02')
						}
						,success : function (response){
							var jsonRes=Ext.decode(response.responseText);
							if(jsonRes.loadList[0].faltaAsegurados =="0"){
								var comentariosText = Ext.create('Ext.form.field.TextArea', {
									fieldLabel: 'Observaciones'
									,labelWidth: 150
									,width: 600
									,name:'smap1.comments'
									,height: 250
								});
								
								windowLoader = Ext.create('Ext.window.Window',{
									modal       : true,
									buttonAlign : 'center',
									width       : 663,
									height      : 430,
									autoScroll  : true,
									items       : [
													Ext.create('Ext.form.Panel', {
													title: 'Turnar a Coordinador de Reclamaciones',
													width: 650,
													url: _URL_ActualizaStatusTramite,
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
														text: 'Turnar'
														,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
														,buttonAlign : 'center',
														handler: function() {
															var formPanel = this.up().up();
															if (formPanel.form.isValid()) {
																
																if(record.get('parametros.pv_otvalor02') == _REEMBOLSO || record.get('parametros.pv_otvalor02') == _INDEMNIZACION){
																	Ext.Ajax.request({
																		url: _UrlGeneraSiniestroTramite,
																		params: {
																			'params.pv_ntramite_i' : record.get('ntramite')
																		},
																		success: function(response, opt) {
																			
																			var jsonRes=Ext.decode(response.responseText);

																			if(jsonRes.success == true){
																				formPanel.form.submit({
																					waitMsg:'Procesando...',
																					params: {
																						'smap1.ntramite' : record.get('ntramite'), 
																						'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
																						,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
																					},
																					failure: function(form, action)
																					{
																						debug(action);
																						switch (action.failureType)
																						{
																							case Ext.form.action.Action.CONNECT_FAILURE:
																								errorComunicacion();
																								break;
																							case Ext.form.action.Action.SERVER_INVALID:
																								mensajeError(action.result.mensaje);
																								break;
																						}
																					},
																					success: function(form, action) {
																						Ext.Ajax.request(
																						{
																							url: _URL_TurnarAOperadorReclamacion,
																							params: {
																									'smap1.ntramite' : record.get('ntramite'), 
																									'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
																									,'smap1.rol_destino'     : 'operadorsini'
																									,'smap1.usuario_destino' : ''
																							},
																							success:function(response,opts){
																								Ext.Ajax.request(
																				    	        {
																				    	            url     : _URL_NOMBRE_TURNADO
																				    	            ,params : 
																				    	            {           
																				    	                'params.ntramite': record.get('ntramite'),
																				    	                'params.rolDestino': 'operadorsini'
																				    	            }
																				    	            ,success : function (response)
																				    	            {
																				    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																				    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
														        	            						loadMcdinStore();
														        	            						windowLoader.close();
																				    	            },
																				    	            failure : function ()
																				    	            {
																				    	                me.up().up().setLoading(false);
																				    	                centrarVentanaInterna(Ext.Msg.show({
																				    	                    title:'Error',
																				    	                    msg: 'Error de comunicaci&oacute;n',
																				    	                    buttons: Ext.Msg.OK,
																				    	                    icon: Ext.Msg.ERROR
																				    	                }));
																				    	            }
																				    	        });
																							},
																							failure:function(response,opts)
																							{
																								Ext.Msg.show({
																									title:'Error',
																									msg: 'Error de comunicaci&oacute;n',
																									buttons: Ext.Msg.OK,
																									icon: Ext.Msg.ERROR
																								});
																							}
																						});
																					}
																				});
																			}else{
																				mensajeError('Error al generar Siniestro para Area de Reclamaciones');
																			}
																		},
																		failure: function(){
																			mensajeError('Error al generar Siniestro para Area de Reclamaciones');
																		}
																	});
																	
																}else{
																	formPanel.form.submit({
																		waitMsg:'Procesando...',
																		params: {
																			'smap1.ntramite' : record.get('ntramite'), 
																			'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
																		},
																		failure: function(form, action) {
																			debug(action);
																			switch (action.failureType)
																			{
																				case Ext.form.action.Action.CONNECT_FAILURE:
																					errorComunicacion();
																					break;
																				case Ext.form.action.Action.SERVER_INVALID:
																					mensajeError(action.result.mensaje);
																					break;
																			}
																		},
																		success: function(form, action) {
																			Ext.Ajax.request(
																			{
																				url: _URL_TurnarAOperadorReclamacion,
																				params: {
																						'smap1.ntramite' : record.get('ntramite'), 
																						'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
																						,'smap1.rol_destino'     : 'operadorsini'
																						,'smap1.usuario_destino' : ''
																				},
																				success:function(response,opts){
																					Ext.Ajax.request(
																	    	        {
																	    	            url     : _URL_NOMBRE_TURNADO
																	    	            ,params : 
																	    	            {           
																	    	                'params.ntramite': record.get('ntramite'),
																	    	                'params.rolDestino': 'operadorsini'
																	    	            }
																	    	            ,success : function (response)
																	    	            {
																	    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																	    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
											        	            						loadMcdinStore();
											        	            						windowLoader.close();
																	    	            },
																	    	            failure : function ()
																	    	            {
																	    	                me.up().up().setLoading(false);
																	    	                centrarVentanaInterna(Ext.Msg.show({
																	    	                    title:'Error',
																	    	                    msg: 'Error de comunicaci&oacute;n',
																	    	                    buttons: Ext.Msg.OK,
																	    	                    icon: Ext.Msg.ERROR
																	    	                }));
																	    	            }
																	    	        });
																				},
																				failure:function(response,opts)
																				{
																					Ext.Msg.show({
																						title:'Error',
																						msg: 'Error de comunicaci&oacute;n',
																						buttons: Ext.Msg.OK,
																						icon: Ext.Msg.ERROR
																					});
																				}
																			});
																		}
																	});
																}
																
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
														icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
														buttonAlign : 'center',
														handler: function() {
															windowLoader.close();
														}
													}
													]
												})  
											]
									}).show();
								
									centrarVentana(windowLoader);
							
							}else{
								mensajeError("Falta asegurados para la factura:"+jsonRes.loadList[0].facturasFaltantes);
							}
						},
						failure: function(){
							mensajeError('Error al turnar.');
						}
					});
					}else {
						mensajeError(jsonRes.msgResult);
					}
			},
			failure: function(){
				mensajeError('Error al turnar.');
			}
		});
	        	
	}
	
	function detalleReclamacionWindow(grid,rowIndex,colIndex)
	{
		var record = grid.getStore().getAt(rowIndex);
		debug('record Valor de respuesta :',record.data);
		
		Ext.create('Ext.form.Panel').submit({
			url             : _Url_ComplementoCitasMedicas
			,params   :{
				'params.cdperson': record.get('parametros.pv_otvalor03')
			}
			,standardSubmit : true
		});

	}
	
	function turnarAareaMedica(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		 var comentariosText = Ext.create('Ext.form.field.TextArea', {
        	fieldLabel: 'Observaciones'
    		,labelWidth: 150
    		,width: 600
    		,name:'smap1.comments'
			,height: 250
        });
		
		windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 663,
	        height      : 430,
	        autoScroll  : true,
	        items       : [
        	        		Ext.create('Ext.form.Panel', {
        	                title: 'Turnar al Area M&eacute;dica',
        	                width: 650,
        	                url: _URL_ActualizaStatusTramite,
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
       	                                ,itemId     : 'SWAGENTE3'
       	                                ,name       : 'SWAGENTE3'
       	                                ,inputValue : 'S'
       	                                ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
       	                            }
       	                            ,{
       	                                boxLabel    : 'No'
       	                                ,name       : 'SWAGENTE3'
       	                                ,inputValue : 'N'
                                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
       	                            }
       	                        ]
       	                    }],
        	        	    buttonAlign:'center',
        	        	    buttons: [{
        	            		text: 'Turnar'
        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
        	            		,buttonAlign : 'center',
        	            		handler: function() {
        	            	    	if (this.up().up().form.isValid()) {
        	            	    		this.up().up().form.submit({
        	            		        	waitMsg:'Procesando...',
        	            		        	params: {
        	            		        		'smap1.ntramite' : record.get('ntramite'), 
        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_REVISION_MEDICA
        	            		        		,'smap1.rol_destino'     : 'medajustador'
        	            		        		,'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
        	            		        		,'smap1.swagente' : _fieldById('SWAGENTE3').getGroupValue()
        	            		        	},
        	            		        	failure: function(form, action) {
        	            		        		debug(action);
        	            		        		switch (action.failureType)
                                                {
                                                    case Ext.form.action.Action.CONNECT_FAILURE:
                                                        errorComunicacion();
                                                        break;
                                                    case Ext.form.action.Action.SERVER_INVALID:
                                                    	mensajeError(action.result.mensaje);
                                                    	break;
                                                }
        	            		        		//mensajeError('No se pudo turnar.');
        	            					},
        	            					success: function(form, action) {
        	            						Ext.Ajax.request(
								    	        {
								    	            url     : _URL_NOMBRE_TURNADO
								    	            ,params : 
								    	            {           
								    	                'params.ntramite': record.get('ntramite'),
								    	                'params.rolDestino': 'medajustador'
								    	            }
								    	            ,success : function (response)
								    	            {
								    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
								    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
		        	            						loadMcdinStore();
		        	            						windowLoader.close();
								    	            },
								    	            failure : function ()
								    	            {
								    	                me.up().up().setLoading(false);
								    	                centrarVentanaInterna(Ext.Msg.show({
								    	                    title:'Error',
								    	                    msg: 'Error de comunicaci&oacute;n',
								    	                    buttons: Ext.Msg.OK,
								    	                    icon: Ext.Msg.ERROR
								    	                }));
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
        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
        	            	    buttonAlign : 'center',
        	            	    handler: function() {
        	            	        windowLoader.close();
        	            	    }
        	            	}
        	            	]
        	            })  
	            	]
	    }).show();
		
		centrarVentana(windowLoader);
		
	}
	
	function turnarAreclamacionesMedAjustador(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		
		Ext.Ajax.request({
			url: _UrlValidaDocumentosCargados,
			params: {
				'params.PV_NTRAMITE_I' : record.get('ntramite'),
				'params.PV_CDRAMO_I'   : record.get('cdramo'),
				'params.PV_cdtippag_I' : record.get('parametros.pv_otvalor02'),
				'params.PV_CDTIPATE_I' : record.get('parametros.pv_otvalor07')
			},
			success: function(response, opt) {
				var jsonRes=Ext.decode(response.responseText);

				if(jsonRes.success == true){
					var comentariosText = Ext.create('Ext.form.field.TextArea', {
	                	fieldLabel: 'Observaciones'
	            		,labelWidth: 150
	            		,width: 600
	            		,name:'smap1.comments'
	        			,height: 250
	                });
	        		
	        		windowLoader = Ext.create('Ext.window.Window',{
	        	        modal       : true,
	        	        buttonAlign : 'center',
	        	        width       : 663,
	        	        height      : 430,
	        	        autoScroll  : true,
	        	        items       : [
			        	        		Ext.create('Ext.form.Panel', {
			        	                title: 'Turnar a Coordinador de Reclamaciones',
			        	                width: 650,
			        	                url: _URL_ActualizaStatusTramite,
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
		        	                                ,itemId     : 'SWAGENTE4'
		        	                                ,name       : 'SWAGENTE4'
		        	                                ,inputValue : 'S'
		        	                                ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
		        	                            }
		        	                            ,{
		        	                                boxLabel    : 'No'
		        	                                ,name       : 'SWAGENTE4'
		        	                                ,inputValue : 'N'
                                                    ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
		        	                            }
		        	                        ]
		        	                    }],
			        	        	    buttonAlign:'center',
			        	        	    buttons: [{
			        	            		text: 'Turnar'
			        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
			        	            		,buttonAlign : 'center',
			        	            		handler: function() {
			        	            			var formPanel = this.up().up();
			        	            	    	if (formPanel.form.isValid()) {
			        	            	    		formPanel.form.submit({
							        	            		        	waitMsg:'Procesando...',
							        	            		        	params: {
							        	            		        		'smap1.ntramite' : record.get('ntramite'), 
							        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
							        	            		        		,'smap1.swagente' : _fieldById('SWAGENTE4').getGroupValue()
							        	            		        	},
							        	            		        	failure: function(form, action)
							        	            		        	{
							        	            		        		debug(action);
							        	            		        		switch (action.failureType)
							        	            		        		{
							        	            		        		    case Ext.form.action.Action.CONNECT_FAILURE:
							        	            		        		    	errorComunicacion();
								        	            		                    break;
								        	            		                case Ext.form.action.Action.SERVER_INVALID:
								        	            		                	mensajeError(action.result.mensaje);
								        	            		                	break;
								        	            		            }
							        	            		        		//mensajeError('No se pudo turnar.');
							        	            					},
							        	            					success: function(form, action) {
							        	            						mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito.');
							        	            						loadMcdinStore();
							        	            						windowLoader.close();
							        	            						
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
			        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
			        	            	    buttonAlign : 'center',
			        	            	    handler: function() {
			        	            	        windowLoader.close();
			        	            	    }
			        	            	}
			        	            	]
			        	            })  
	        	            	]
	        	    	}).show();
	        		
	        			centrarVentana(windowLoader);
					}else {
						mensajeError(jsonRes.msgResult);
					}
			},
			failure: function(){
				mensajeError('Error al turnar.');
			}
		});
	}
	
	
	
	function turnarAoperadorReclamaciones(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		var comentariosText = Ext.create('Ext.form.field.TextArea', {
        	fieldLabel: 'Observaciones'
    		,labelWidth: 150
    		,width: 600
    		,name:'smap1.comments'
			,height: 250
        });
		
		windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 663,
	        height      : 430,
	        autoScroll  : true,
	        items       : [
        	        		Ext.create('Ext.form.Panel', {
        	                title: 'Turnar a Operador de Reclamaciones',
        	                width: 650,
        	                url: _URL_ActualizaStatusTramite,
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
       	                                ,itemId     : 'SWAGENTE5'
       	                                ,name       : 'SWAGENTE5'
       	                                ,inputValue : 'S'
       	                                ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
       	                            }
       	                            ,{
       	                                boxLabel    : 'No'
       	                                ,name       : 'SWAGENTE5'
       	                                ,inputValue : 'N'
                                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
       	                            }
       	                        ]
       	                    }],
        	        	    buttonAlign:'center',
        	        	    buttons: [{
        	            		text: 'Turnar'
        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
        	            		,buttonAlign : 'center',
        	            		handler: function() {
        	            	    	if (this.up().up().form.isValid()) {
        	            	    		this.up().up().form.submit({
        	            		        	waitMsg:'Procesando...',
        	            		        	params: {
        	            		        		'smap1.ntramite' : record.get('ntramite'), 
        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
        	            		        		,'smap1.rol_destino'     : 'operadorsini'
                                                ,'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
                                                ,'smap1.swagente' : _fieldById('SWAGENTE5').getGroupValue()
        	            		        	},
        	            		        	failure: function(form, action) {
        	            		        		debug(action);
        	            		        		switch (action.failureType)
                                                {
                                                    case Ext.form.action.Action.CONNECT_FAILURE:
                                                        errorComunicacion();
                                                        break;
                                                    case Ext.form.action.Action.SERVER_INVALID:
                                                    	mensajeError(action.result.mensaje);
                                                    	break;
                                                }
        	            		        		//mensajeError('No se pudo turnar.');
        	            					},
        	            					success: function(form, action) {
        	            						Ext.Ajax.request(
   								    	        {
   								    	            url     : _URL_NOMBRE_TURNADO
   								    	            ,params : 
   								    	            {           
   								    	                'params.ntramite': record.get('ntramite'),
   								    	                'params.rolDestino': 'operadorsini'
   								    	            }
   								    	            ,success : function (response)
   								    	            {
   								    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
   								    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
   		        	            						loadMcdinStore();
   		        	            						windowLoader.close();
   								    	            },
   								    	            failure : function ()
   								    	            {
   								    	                me.up().up().setLoading(false);
   								    	                centrarVentanaInterna(Ext.Msg.show({
   								    	                    title:'Error',
   								    	                    msg: 'Error de comunicaci&oacute;n',
   								    	                    buttons: Ext.Msg.OK,
   								    	                    icon: Ext.Msg.ERROR
   								    	                }));
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
        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
        	            	    buttonAlign : 'center',
        	            	    handler: function() {
        	            	        windowLoader.close();
        	            	    }
        	            	}
        	            	]
        	            })  
	            	]
	    }).show();
		
		centrarVentana(windowLoader);
	}
	
	function solicitarPago(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		
		if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
			mensajeWarning('Ya se ha solicitado el pago para este tr&aacute;mite.');	
			return;
		}else{
			if( record.get('parametros.pv_otvalor02') ==_PAGO_DIRECTO){
				mostrarSolicitudPago(grid,rowIndex,colIndex);
			}else{
				Ext.Ajax.request({
					url	 : _URL_VAL_AJUSTADOR_MEDICO
					,params:{
						'params.ntramite': record.get('ntramite')
					}
					,success : function (response)
					{
						if(Ext.decode(response.responseText).datosValidacion != null){
							var autAM = null;
							var result ="";
							banderaValidacion = "0";
							var json = Ext.decode(response.responseText).datosValidacion;
							if(json.length > 0){
								for(var i = 0; i < json.length; i++){
									if(json[i].AREAAUTO =="ME"){
										var valorValidacion = json[i].SWAUTORI+"";
										if(valorValidacion == null || valorValidacion == ''|| valorValidacion == 'null'){
											banderaValidacion = "1";
											result = result + 'El m&eacute;dico no autoriza la factura ' + json[i].NFACTURA + '<br/>';
										}
									}
								}
								if(banderaValidacion == "1"){
									centrarVentanaInterna(mensajeWarning(result));
								}else{
									mostrarSolicitudPago(grid,rowIndex,colIndex);
								}
							}else{
								centrarVentanaInterna(mensajeWarning('El m&eacute;dico no ha autizado la factura'));
							}
						}
					},
					failure : function (){
						me.up().up().setLoading(false);
						Ext.Msg.show({
							title:'Error',
							msg: 'Error de comunicaci&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						});
					}
				});
				
			}
		}
	}
	
	function mostrarSolicitudPago(grid,rowIndex,colIndex){
		storeDestinoPago = Ext.create('Ext.data.JsonStore', {
		    model:'Generic',
	        //autoLoad:true,
	        proxy:
	        {
	            type: 'ajax',
	            url: _URL_CATALOGOS,
	            extraParams : {catalogo:_CAT_DESTINOPAGO},
	            reader:
	            {
	                type: 'json',
	                root: 'lista'
	            }
	        }
	    });
		storeDestinoPago.load();
		storeCatConcepto = Ext.create('Ext.data.JsonStore', {
	        model:'Generic',
	        proxy:
	        {
	            type: 'ajax',
	            url: _URL_CATALOGOS,
	            extraParams : {catalogo:_CAT_CONCEPTO},
	            reader:
	            {
	                type: 'json',
	                root: 'lista'
	            }
	        }
	    });
		storeCatConcepto.load();
		msgWindow = Ext.Msg.show({
	        title: 'Aviso',
	        msg: '&iquest;Esta seguro que desea solicitar el pago?',
	        buttons: Ext.Msg.YESNO,
	        icon: Ext.Msg.QUESTION,
	        fn: function(buttonId, text, opt){
	        	if(buttonId == 'yes'){
	        		var record = grid.getStore().getAt(rowIndex);
	        		
	        		var pagocheque = Ext.create('Ext.form.field.ComboBox',
   		    	    {
   		    	        colspan	   :2,				fieldLabel   	: 'Destino Pago', 	name			:'destinoPago',
   		    	        allowBlank : false,			editable     	: true,			displayField    : 'value',
   		    	        valueField:'key',			forceSelection  : true,			width			:350,
   		    	        queryMode    :'local',		store 			: storeDestinoPago
   		    	    });
   		    		
   		    		var concepPago = Ext.create('Ext.form.field.ComboBox',
   		    	    {
   		    	        colspan	   :2,				fieldLabel   	: 'Concepto Pago', 	name			:'concepPago',
   		    	        allowBlank : false,			editable     	: true,			displayField    : 'value',
   		    	        valueField:'key',			forceSelection  : true,			width			:350,
   		    	        queryMode    :'local',		store 			: storeCatConcepto
   		    	    });
	        		
   		    		var cdramoTramite="";
   		    		var cdtipsitTramite ="";
	        		//LLAMADA A LA MESA DE CONTROL PARA VERIFICAR LOS CAMPOS OTVALOR18 Y OTVALOR19
	        		Ext.Ajax.request({
	        			url     : _URL_CONSULTA_TRAMITE
	        			,params:{
	        				'params.ntramite': record.get('ntramite')
	                    }
	        			,success : function (response)
	        			{
		        			 	if(Ext.decode(response.responseText).listaMesaControl != null)
		        		    	{
		        			    	var json=Ext.decode(response.responseText).listaMesaControl[0];
		        			    	cdramoTramite = json.cdramomc;
		           		    		cdtipsitTramite = json.cdtipsitmc;
		        			    	
		        			    	if(json.otvalor18mc !=null)
	        			    		{
		        			    		panelModificacion.query('combo[name=destinoPago]')[0].setValue(json.otvalor18mc);
	        			    		}
		        			    	
		        			    	if(json.otvalor19mc !=null)
	        			    		{
		        			    		panelModificacion.query('combo[name=concepPago]')[0].setValue(json.otvalor19mc);
	        			    		}
	        		    		}
	        			    },
	        			    failure : function ()
	        			    {
	        			        me.up().up().setLoading(false);
	        			        Ext.Msg.show({
	        			            title:'Error',
	        			            msg: 'Error de comunicaci&oacute;n',
	        			            buttons: Ext.Msg.OK,
	        			            icon: Ext.Msg.ERROR
	        			        });
	        			    }
	        		});
	        		
		    		
			        windowCvePago = Ext.create('Ext.window.Window',{
				        modal       : true,
				        buttonAlign : 'center',
				        width       : 550,
				        autoScroll  : true,
				        items       : [
			        	        		panelModificacion = Ext.create('Ext.form.Panel', {
			        	                title: 'Destino de Pago',
			        	                bodyPadding: 5,
			        	                items: [pagocheque,
			        	                        concepPago],
			        	        	    buttonAlign:'center',
			        	        	    buttons: [{
			        	            		text: 'Solicitar'
			        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
			        	            		,buttonAlign : 'center',
			        	            		handler: function() { 
			        	            			if (panelModificacion.form.isValid()) {
			        	            				var datos=panelModificacion.form.getValues();
			        	            				
			        	            				Ext.Ajax.request({
			        	        	        			url     : _URL_CONCEPTODESTINO
			        	        	        			,jsonData: {
			        	        	        				params:{ntramite:record.get('ntramite'),cdtipsit:cdtipsitTramite,destinoPago:datos.destinoPago,concepPago:datos.concepPago}
	        	                                        }
			        	        	        			,success : function (response)
			        	        	        			{
			        	        		        			 	
			        	        	        				windowCvePago.close();
			        	        	        				mcdinGrid.setLoading(true);
			        	        	     	        		Ext.Ajax.request({
			        	        	     						url: _UrlSolicitarPago,
			        	        	     						params: {
			        	        	     				    		'params.pv_ntramite_i' : record.get('ntramite'),
			        	        	     				    		'params.pv_tipmov_i'   : record.get('parametros.pv_otvalor02')
			        	        	     				    	},
			        	        	     						success: function(response, opts) {
			        	        	     							mcdinGrid.setLoading(false);
			        	        	     							var respuesta = Ext.decode(response.responseText);
			        	        	     							if(respuesta.success){
			        	        	     								mensajeCorrecto('Aviso','El pago se ha solicitado con &eacute;xito.');	
			        	        	     							}else {
			        	        	     								mensajeError(respuesta.mensaje);
			        	        	     							}
			        	        	     							
			        	        	     						},
			        	        	     						failure: function(){
			        	        	     							mcdinGrid.setLoading(false);
			        	        	     							mensajeError('No se pudo solicitar el pago.');
			        	        	     						}
			        	        	     					});
			        	        	     	        		
			        	        	        			    },
			        	        	        			    failure : function ()
			        	        	        			    {
			        	        	        			        me.up().up().setLoading(false);
			        	        	        			        Ext.Msg.show({
			        	        	        			            title:'Error',
			        	        	        			            msg: 'Error de comunicaci&oacute;n',
			        	        	        			            buttons: Ext.Msg.OK,
			        	        	        			            icon: Ext.Msg.ERROR
			        	        	        			        });
			        	        	        			    }
			        	        	        		});
			        	            			}else {
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
			        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
			        	            	    buttonAlign : 'center',
			        	            	    handler: function() {
			        	            	        windowCvePago.close();
			        	            	    }
			        	            	}
			        	            	]
			        	            })  
				            	]
				    }).show();
					centrarVentana(windowCvePago);

	        	}
	        	
	        }
	    });
		centrarVentana(msgWindow);
	}

	
	function _4_onClockClick(grid,rowIndex)
	{
	    var record=grid.getStore().getAt(rowIndex);
	    debug(record);
	    var window=Ext.create('Ext.window.Window',
	    {
	        title        : 'Detalles del tr&aacute;mite '+record.get('ntramite')
	        ,modal       : true
	        ,buttonAlign : 'center'
	        ,width       : 700
	        ,height      : 400
	        
	        ,items       :
	        [
	            Ext.create('Ext.grid.Panel',
	            {
	                height      : 190
	                ,autoScroll : true
	                ,store      : new Ext.data.Store(
	                {
	                    model     : 'DetalleMC'
	                    ,autoLoad : true
	                    ,proxy    :
	                    {
	                        type         : 'ajax'
	                        ,url         : mesConUrlDetMC
	                        ,extraParams :
	                        {
	                            'smap1.pv_ntramite_i' : record.get('ntramite')
	                        }
	                        ,reader      :
	                        {
	                            type  : 'json'
	                            ,root : 'slist1'
	                        }
	                    }
	                })
	                ,columns : 
	                [
	                    {
	                        header     : 'Tr&aacute;mite'
	                        ,dataIndex : 'NTRAMITE'
	                        ,width     : 60
	                    }
	                    ,{
	                        header     : 'Consecutivo'
	                        ,dataIndex : 'NMORDINA'
	                        ,width     : 80
	                    }
	                    ,{
	                        header     : 'Fecha de inicio'
	                        ,xtype     : 'datecolumn'
	                        ,dataIndex : 'FECHAINI'
	                        ,format    : 'd M Y'
	                        ,width     : 90
	                    }
	                    ,{
	                        header     : 'Usuario inicio'
	                        ,dataIndex : 'usuario_ini'
	                        ,width     : 150
	                    }
	                    ,{
	                        header     : 'Fecha de fin'
	                        ,xtype     : 'datecolumn'
	                        ,dataIndex : 'FECHAFIN'
	                        ,format    : 'd M Y'
	                        ,width     : 90
	                    }
	                    ,{
	                        header     : 'Usuario fin'
	                        ,dataIndex : 'usuario_fin'
	                        ,width     : 150
	                    }
	                    ,{
	                        width         : 30
	                        ,menuDisabled : true
	                        ,dataIndex    : 'FECHAFIN'
	                        ,renderer     : function(value)
	                        {
	                            debug(value);
	                            if(value&&value!=null)
	                            {
	                                value='';
	                            }
	                            else
	                            {
	                                value='<img src="${ctx}/resources/fam3icons/icons/accept.png" style="cursor:pointer;" data-qtip="Finalizar" />';
	                            }
	                            return value;
	                        }
	                    }
	                    /*,{
	                        width         : 30
	                        ,menuDisabled : true
	                        ,dataIndex    : 'CDCLAUSU'
	                        ,renderer     : function(value)
	                        {
	                            debug(value);
	                            if(value&&value!=null&&value.length>0)
	                            {
	                                value='<img src="${ctx}/resources/fam3icons/icons/printer.png" style="cursor:pointer;" data-qtip="Imprimir" />';
	                            }
	                            else
	                            {
	                                value='';
	                            }
	                            return value;
	                        }
	                    }*/
	                ]
	                ,listeners :
	                {
	                    cellclick : function(grid, td,
	                            cellIndex, record, tr,
	                            rowIndex, e, eOpts)
	                    {
	                        debug(record);
	                        if(cellIndex<6)
	                        {
	                            Ext.getCmp('inputReadDetalleHtmlVisor').setValue((_4_smap1.cdsisrol!='EJECUTIVOCUENTA'||record.raw.SWAGENTE=='S')?record.get('COMMENTS'):'');
	                        }
	                        else if(cellIndex==6&&$(td).find('img').length>0)
	                        {
	                            debug('finalizar');
	                            Ext.create('Ext.window.Window',
	                            {
	                                title        : 'Finalizar detalle'
	                                ,width       : 600
	                                ,height      : 400
	                                ,buttonAlign : 'center'
	                                ,modal       : true
	                                ,closable    : false
	                                ,autoScroll  : true
	                                ,items       :
	                                [
	                                    Ext.create('Ext.form.HtmlEditor', {
	                                        id      : 'inputHtmlEditorFinalizarDetalleMesCon'
	                                        ,width  : 570
	                                        ,height : 300
	                                        ,value  : record.get('COMMENTS')
	                                    })
	                                ]
	                                ,buttons     :
	                                [
	                                    {
	                                        text     : 'Guardar'
	                                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
	                                        ,handler : function()
	                                        {
	                                            var win=this.up().up();
	                                            win.setLoading(true);
	                                            Ext.Ajax.request
	                                            ({
	                                                url      : mesConUrlFinDetalleMC
	                                                ,params  :
	                                                {
	                                                    'smap1.pv_ntramite_i'  : record.get('NTRAMITE')
	                                                    ,'smap1.pv_nmordina_i' : record.get('NMORDINA')
	                                                    ,'smap1.pv_comments_i' : Ext.getCmp('inputHtmlEditorFinalizarDetalleMesCon').getValue()
	                                                }
	                                                ,success : function (response)
	                                                {
	                                                    var json=Ext.decode(response.responseText);
	                                                    if(json.success==true)
	                                                    {
	                                                        win.destroy();
	                                                        window.destroy();
	                                                        Ext.Msg.show({
	                                                            title:'Detalle actualizado',
	                                                            msg: 'Se finaliz&oacute; el detalle',
	                                                            buttons: Ext.Msg.OK
	                                                        });
	                                                    }
	                                                    else
	                                                    {
	                                                        win.setLoading(false);
	                                                        Ext.Msg.show({
	                                                            title:'Error',
	                                                            msg: 'Error al finalizar detalle',
	                                                            buttons: Ext.Msg.OK,
	                                                            icon: Ext.Msg.ERROR
	                                                        });
	                                                    }
	                                                }
	                                                ,failure : function()
	                                                {
	                                                    win.setLoading(false);
	                                                    Ext.Msg.show({
	                                                        title:'Error',
	                                                        msg: 'Error de comunicaci&oacute;n',
	                                                        buttons: Ext.Msg.OK,
	                                                        icon: Ext.Msg.ERROR
	                                                    });
	                                                }
	                                            });
	                                        }
	                                    }
	                                    ,{
	                                        text     : 'Cancelar'
	                                        ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
	                                        ,handler : function()
	                                        {
	                                            this.up().up().destroy();
	                                        }
	                                    }
	                                ]
	                            }).show();
	                        }
	                        /*else if(cellIndex==5&&$(td).find('img').length>0)
	                        {
	                            debug("APRETASTE EL BOTON IMPRIMIR PARA EL RECORD:",record);
	                        }*/
	                    }
	                }
	            })
	            ,Ext.create('Ext.form.HtmlEditor',
	            {
	                id        : 'inputReadDetalleHtmlVisor'
	                ,width    : 690
	                ,height   : 200
	                ,readOnly : true
	            })
	        ]
	    }).show();
	    window.center();
	    Ext.getCmp('inputReadDetalleHtmlVisor').getToolbar().hide();
	}
	
function reasignarClick(grid,dataIndex)
{
	var record=grid.getStore().getAt(dataIndex);
	debug('reasignarClick record:',record);
	var rol='';
	if(record.get('status')=='1')
	{
		rol='medajustador';
	}
	else if(record.get('status')=='7')
	{
		rol='operadorsini';
	}
	debug('rol:',rol);
	
	var usuario = record.get('parametros.pv_otvalor16');
	debug('usuario encargado:',usuario);
	
	var valido = rol && usuario;
	if(!valido)
	{
		mensajeWarning('El tr&aacute;mite solo se puede reasignar si est&aacute; asignado a alg&uacute;n usuario'
		+' y en status "En captura" o "En revisi&oacute;n m&eacute;dica"');
	}
	
	if(valido)
	{
		Ext.Ajax.request(
		{
			url      : _mesasin_url_lista_reasignacion
			,params  :
			{
				'smap.cdsisrol' : rol
			}
			,success : function(response)
			{
				var json = Ext.decode(response.responseText);
				debug('respuesta:',json);
				var ventana = Ext.create('Ext.window.Window',
				{
					title   : 'Seleccionar usuario'
					,modal  : true
					,width  : 400
					,height : 300
					,items  :
					[
					    Ext.create('Ext.grid.Panel',
					    {
					    	columns :
					    	[
					    	    {
					    	    	header     : 'Nombre'
					    	    	,dataIndex : 'DSUSUARIO'
					    	    	,flex      : 1
					    	    }
					    	    ,{
					    	    	xtype         : 'actioncolumn'
					    	    	,width        : 20
					    	    	,menuDisabled : true
					    	    	,sortable     : false
					    	    	,icon         : '${ctx}/resources/fam3icons/icons/accept.png'
					    	    	,handler      : function(gridVen,rowIndexVen)
					    	    	{
					    	    		var record  = gridVen.getStore().getAt(rowIndexVen);
					    	    		var usuarioNuevo = record.get('CDUSUARIO');
					    	    		debug('usuarioNuevo:',usuarioNuevo);
					    	    		if(rol=='medajustador')
					    	    		{
					    	    			turnarAareaMedica(grid,dataIndex,usuarioNuevo);
					    	    			ventana.destroy();
					    	    		}
					    	    		else if(rol=='operadorsini')
					    	    		{
					    	    			turnarAoperadorReclamaciones(grid,dataIndex,usuarioNuevo);
					    	    			ventana.destroy();
					    	    		}
					    	    	}
					    	    }
					    	]
					        ,store  : Ext.create('Ext.data.Store',
					        {
					        	fields    : ['CDUSUARIO','DSUSUARIO']
					        	,autoLoad : true
					        	,proxy    :
					        	{
					        		type    : 'memory'
					        		,reader : 'json'
					        	    ,data   : json.slist1					        		
					        	}
					        })
					    })
					]
				});
				ventana.show();
				centrarVentanaInterna(ventana);
			}
		    ,failure : errorComunicacion
		});
	}
}

function turnarDevolucionTramite(grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	var comentariosText = Ext.create('Ext.form.field.TextArea', {
		fieldLabel: 'Observaciones'
		,labelWidth: 150
		,width: 600
		,name:'smap1.comments'
		,height: 250
	});
	
	windowLoader = Ext.create('Ext.window.Window',{
		modal       : true,
		buttonAlign : 'center',
		width       : 663,
		height      : 430,
		autoScroll  : true,
		items       : [
			Ext.create('Ext.form.Panel', {
				title: 'Devolver Tr&aacute;mite',
				width: 650,
				url: _URL_ActualizaStatusTramite,
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
                            ,itemId     : 'SWAGENTE6'
                            ,name       : 'SWAGENTE6'
                            ,inputValue : 'S'
                            ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                        }
                        ,{
                            boxLabel    : 'No'
                            ,name       : 'SWAGENTE6'
                            ,inputValue : 'N'
                            ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                        }
                    ]
                }],
				buttonAlign:'center',
				buttons: [{
					text: 'Devolver'
					,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
					,buttonAlign : 'center',
					handler: function() {
						var formPanel = this.up().up();
						if (formPanel.form.isValid()) {
							formPanel.form.submit({
								waitMsg:'Procesando...',
								params: {
									'smap1.ntramite' : record.get('ntramite'), 
									'smap1.status'   : _STATUS_DEVOLVER_TRAMITE
									,'smap1.swagente' : _fieldById('SWAGENTE6').getGroupValue()
								},
								failure: function(form, action)
								{
									debug(action);
									switch (action.failureType)
									{
										case Ext.form.action.Action.CONNECT_FAILURE:
											errorComunicacion();
										break;
										case Ext.form.action.Action.SERVER_INVALID:
											mensajeError(action.result.mensaje);
										break;
									}
								},
								success: function(form, action) {
									mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito.');
									loadMcdinStore();
									windowLoader.close();

								}
							});
						}else {
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
					icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
					buttonAlign : 'center',
					handler: function() {
						windowLoader.close();
					}
				}
				]
			})
		]
	}).show();
	centrarVentana(windowLoader);
}

Ext.onReady(function()
		{
			
			/////////////////////
			////// modelos //////
			Ext.define('DetalleMC',{
		        extend:'Ext.data.Model',
		        fields:
		        [
		            "NTRAMITE"
		            ,"NMORDINA"
		            ,"CDTIPTRA"
		            ,"CDCLAUSU"
		            ,{name:"FECHAINI",type:'date',dateFormat:'d/m/Y'}
		            ,{name:"FECHAFIN",type:'date',dateFormat:'d/m/Y'}
		            ,"COMMENTS"
		            ,"CDUSUARI_INI"
		            ,"CDUSUARI_FIN"
		            ,"usuario_ini"
		            ,"usuario_fin"
		        ]
		    });
		    ////// modelos //////
			/////////////////////
		});
<s:if test="false">
</script>
</s:if>