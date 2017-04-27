<%@ include file="/taglibs.jsp"%>
<s:if test="false">
<script>
</s:if>
var _CONTEXT = '${ctx}';
///////////////////////////////////////////////////
var _STATUS_TRAMITE_RECHAZADO               = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';
var _PAGO_AUTOMATICO						= '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@PAGO_AUTOMATICO.cdtiptra" />';
var _PAGO_DIRECTO 							= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
var _REEMBOLSO    							= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';
var _INDEMNIZACION							= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@INDEMNIZACION.codigo" />';
var _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_ASIGNACION.codigo" />';
var _STATUS_TRAMITE_EN_CAPTURA              = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA.codigo" />';
var _STATUS_TRAMITE_EN_REVISION_MEDICA      = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_REVISION_MEDICA.codigo" />';
var _STATUS_TRAMITE_CONFIRMADO              = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@CONFIRMADO.codigo" />';
var _CAT_DESTINOPAGO                        = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@DESTINOPAGO"/>';
var _CATALOGO_CONCEPTOPAGO					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CONCEPTOPAGO"/>';
var _CATALOGO_PROVEEDORES  					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
var _CATALOGO_ConfLayout					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CONFLAYOUT"/>';

var _UrlValidaDocumentosCargados			= '<s:url namespace="/siniestros" 	action="validaDocumentosCargados"/>';
var _URL_P_MOV_MAUTSINI						= '<s:url namespace="/siniestros"	action="obtieneMensajeMautSini"/>';
var _UrlRechazarTramiteWindwow  			= '<s:url namespace="/siniestros" 	action="includes/rechazoReclamaciones" />';
var _UrlDocumentosPoliza    				= '<s:url namespace="/documentos" 	action="ventanaDocumentosPoliza"   />';
var _UrlGenerarContrarecibo     			= '<s:url namespace="/siniestros" 	action="generarContrarecibo"       />';
var panDocUrlViewDoc     					= '<s:url namespace="/documentos" 	action="descargaDocInline" />';
var _URL_INF_ASEGURADO						= '<s:url namespace="/siniestros" 	action="consultaDatosAseguradoSiniestro"/>';
var _UrlDetalleSiniestroDirecto 			= '<s:url namespace="/siniestros" 	action="afiliadosAfectados"        />';
var _selCobUrlAvanza              			= '<s:url namespace="/siniestros" 	action="afiliadosAfectados"/>';
var _URL_POLIZA_UNICA						= '<s:url namespace="/siniestros"	action="consultaPolizaUnica"/>';
var _urlSeleccionCobertura      			= '<s:url namespace="/siniestros" 	action="seleccionCobertura"        />';
var _URL_VALIDA_FACTMONTO					= '<s:url namespace="/siniestros" 	action="validaFacturaMontoPagoAutomatico" />';
var _URL_ActualizaStatusTramite 			= '<s:url namespace="/mesacontrol" 	action="actualizarStatusTramite" />';
var _UrlGeneraSiniestroTramite 				= '<s:url namespace="/siniestros" 	action="generaSiniestroTramite" />';
var _URL_TurnarAOperadorReclamacion 		= '<s:url namespace="/mesacontrol" 	action="turnarAOperadorReclamacion" />';
var _URL_NOMBRE_TURNADO   					= '<s:url namespace="/siniestros" 	action="obtieneUsuarioTurnado" />';
var _URL_VALIDA_ARANCELES					= '<s:url namespace="/siniestros" 	action="validaArancelesTramitexProveedor" />';
var _URL_GENERAR_CALCULO					= '<s:url namespace="/siniestros" 	action="generarCalculoSiniestros" />';
var _URL_MESACONTROL						= '<s:url namespace="/mesacontrol" 	action="mcdinamica" />';
var _URL_VALIDA_SOLICITUD_PAGO				= '<s:url namespace="/siniestros" 	action="solicitarPagoAutomatico" />';
var _URL_MONTO_PAGO_SINIESTRO				= '<s:url namespace="/siniestros"	action="obtieneMontoPagoSiniestro"/>';
var _URL_VAL_AJUSTADOR_MEDICO				= '<s:url namespace="/siniestros" 	action="consultaDatosValidacionAjustadorMed"/>';
var _URL_CATALOGOS							= '<s:url namespace="/catalogos"    action="obtieneCatalogo" />';
var _URL_LISTADO_ASEGURADO_POLIZA			= '<s:url namespace="/siniestros"   action="consultaListaAseguradoPoliza" />';
var _URL_CONSULTA_BENEFICIARIO				= '<s:url namespace="/siniestros"	action="consultaDatosBeneficiario" />';
var _URL_CONSULTA_TRAMITE       			= '<s:url namespace="/siniestros"   action="consultaListadoMesaControl" />';
var _URL_CONCEPTODESTINO        			= '<s:url namespace="/siniestros"   action="guardarConceptoDestino" />';
var _UrlSolicitarPago           			= '<s:url namespace="/siniestros" 	action="solicitarPago"             />';
var _mesasin_url_lista_reasignacion 		= '<s:url namespace="/siniestros" 	action="obtenerUsuariosPorRol" />';
var _URL_ACTUALIZA_TURNADOMC				= '<s:url namespace="/siniestros" 	action="actualizaTurnadoMesaControl" />';
var _UrlProcesarTramiteLayout            	= '<s:url namespace="/siniestros" 	action="procesarTramiteLayout"       />';
var _UrlProcesarTramiteSiniestro          	= '<s:url namespace="/siniestros" 	action="procesarTramiteSiniestroSISCO"       />';
var _URL_SubirLayout                    	= '<s:url namespace="/siniestros"   action="subirLayoutGeneral" />';
var _URL_EXISTE_CONF_PROV 					= '<s:url namespace="/siniestros"	action="validaExisteConfiguracionProv" />';
var _URL_ValidaLayoutFormatoExcel       	= '<s:url namespace="/siniestros"   action="validaLayoutFormatoExcel"   />';
var _URL_ValidaLayoutConfigExcel        	= '<s:url namespace="/siniestros"   action="validaLayoutConfiguracionExcel"   />';
var _URL_VALIDA_COBASEGURADOS				= '<s:url namespace="/siniestros" 	action="validaLimiteCoberturaAsegurados"/>';
var _URL_VALIDA_COBASEGURADOSCR				= '<s:url namespace="/siniestros" 	action="validarMultiplesCRSISCO"/>';
var _URL_VALIDA_IMPASEGURADOSINIESTRO		= '<s:url namespace="/siniestros" 	action="validaImporteTramiteAsegurados"/>';


var windowLoader;
var msgWindow;

	_4_botonesGrid =
	[
        <s:property value="imap1.gridbuttons" />
	];
	
	/***** RECHAZAR TRAMITE *****/
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
	            	'params.cdunieco'  : record.get('cdunieco'),
		    		'params.cdramo'    : record.get('cdramo'),
		    		'params.estado'    : record.get('estado'),
		    		'params.nmpoliza'  : record.get('nmpoliza'),
		    		'params.nmsuplem'  : record.get('nmsuplem'),
		    		'params.nmsolici'  : record.get('nmsolici'),
	                'params.nmTramite' : record.get('ntramite'),
	                'params.tipopago'  : record.get('parametros.pv_otvalor02'),
	                'params.cdtipsit'  : record.get('cdtipsit')
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
	
	/***** SUBIR DOCUMENTOS *****/
	function documentosWindow(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
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
	                ,'smap1.cdtiptra' : _PAGO_AUTOMATICO
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
	    centrarVentanaInterna(windowLoader);
	}

	/***** GENERACION DE CONTRARECIBO *****/
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
		    		'paramsO.pv_tipmov_i'   : record.get('parametros.pv_otvalor02'),
		    		'paramsO.pv_pagoAut_i'  : "1"//pago Automatico 
		    	
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
	
	/***** DETALLE DE LA RECLAMACION *****/
	function detalleReclamacionWindow(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url	 : _URL_INF_ASEGURADO
			,params:{
				'params.ntramite': record.get('ntramite')
			}
			,success : function (response){
				if(Ext.decode(response.responseText).datosValidacion != null){
					var json = Ext.decode(response.responseText).datosValidacion[0];
					var formapago = json.OTVALOR02;
					var esPagoDirecto = formapago == _PAGO_DIRECTO;
					var params = {};
					
					params['params.tipopago'] = formapago;
					params['params.ntramite'] = json.NTRAMITE;
					
					var conCoberYSubcober = false;
					
					debug('conCoberYSubcober:',conCoberYSubcober ? 'si' : 'no');
					
					var urlDestino;
					if(esPagoDirecto||true) {
						if(true){
							if(esPagoDirecto){
								urlDestino = _UrlDetalleSiniestroDirecto;
								debug('urlDestino_1 :',urlDestino);
								debug('params_1 :',params);
								Ext.create('Ext.form.Panel').submit({
									url             : urlDestino
									,params         : params
								    ,standardSubmit : true
								});
							}
							else{
								// Pago diferente a Directo
								if(json.OTVALOR12 && json.OTVALOR12.length>0){
									conCoberYSubcober = true;
								}
								
								if(conCoberYSubcober){ // true
									urlDestino = _selCobUrlAvanza;
									debug('urlDestino_2 :',urlDestino);
									debug('params_2:',params);
									Ext.create('Ext.form.Panel').submit({
										url             : urlDestino
										,params         : params
									    ,standardSubmit : true
									});
								}else{
									//PASAMOS LOS VALORES PARA SELECCIONAR LA COBERTURA Y SUBCOBERTURA
									Ext.Ajax.request( {
										url     : _URL_POLIZA_UNICA
										,params : {
											'params.cdunieco': record.get('cdunieco'),
											'params.cdramo'  : json.CDRAMO,
											'params.estado'  : json.ESTADO,
											'params.nmpoliza': json.NMPOLIZA,
											'params.cdperson': json.CDPERSON
										}
										,success : function (response){
											if(Ext.decode(response.responseText).polizaUnica != null) {
												var jsonValorAsegurado = Ext.decode(response.responseText).polizaUnica[0];
												debug("Valor de respuesta ---> : ",jsonValorAsegurado);
												
												urlDestino = _urlSeleccionCobertura;
												params['params.cdunieco']  		= record.get('cdunieco');
												params['params.otvalor02'] 		= json.OTVALOR02;
												params['params.cdramo']    		= json.CDRAMO;
												params['params.cdtipsit']  		= json.CDTIPSIT;
												params['params.nmpoliza']  		= json.NMPOLIZA;
												params['params.nmsituac']  		= json.NMSITUAC;
												params['params.estado']    		= json.ESTADO;
												params['params.periodoEspera']  = jsonValorAsegurado.diasAsegurado;
												params['params.feocurre']    	= json.FEOCURRE;
												
												Ext.create('Ext.form.Panel').submit( {
													url             : urlDestino
													,params         : params
												    ,standardSubmit : true
												});
											}
										},
										failure : function (){
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
							}
						}
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
	
	/***** TURNAR AL OPERADOR DE RECLAMACIONES POR PARTE DE LA MESA DE CONTROL *****/
	function turnarAreclamaciones(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		
		Ext.Ajax.request({
			url	 : _URL_VALIDA_FACTMONTO
			,params:{
				'params.ntramite'  : record.get('ntramite')
			}
			,success : function (response) {
				banderaAranceles ="0";
				var resultAranceles = 'Los siguientes C.R. no se procesaran : <br/>';
				var arancelesTra = Ext.decode(response.responseText).loadList;
				for(i = 0; i < arancelesTra.length; i++){
					banderaAranceles = "1";
					resultAranceles = resultAranceles + '   - El C.R.' + arancelesTra[i].NTRAMITE+ ' el n&uacute;mero de factura es:  ' + arancelesTra[i].NFACTURA + ' el importe de la factura es : '+ arancelesTra[i].PTIMPORT+'<br/>';
				}
				
				if(banderaAranceles == "1"){
					centrarVentanaInterna(Ext.Msg.show({
						title:'Aviso del sistema',
						msg: resultAranceles,
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					}));
				}else{
					//Si es correcto entonces procedemos con el turnado al operador de reclamacioness
					var comentariosText = Ext.create('Ext.form.field.TextArea', {
						fieldLabel: 'Observaciones'
						,labelWidth: 150
						,width: 600
						,name:'smap1.comments'
						,height: 250
						,allowBlank : false
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
																			'smap1.ntramite'  : record.get('ntramite'), 
																			'smap1.status'    : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
																			,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
																		},
																		failure: function(form, action) {
																			debug(action);
																			switch (action.failureType) {
																				case Ext.form.action.Action.CONNECT_FAILURE:
																					errorComunicacion();
																					break;
																				case Ext.form.action.Action.SERVER_INVALID:
																					mensajeError(action.result.mensaje);
																					break;
																			}
																		},
																		success: function(form, action) {
																			Ext.Ajax.request( {
																				url: _URL_TurnarAOperadorReclamacion,
																				params: {
																						'smap1.ntramite' 		 : record.get('ntramite'), 
																						'smap1.status'   		 : _STATUS_TRAMITE_EN_CAPTURA
																						,'smap1.rol_destino'     : 'operadorsini'
																						,'smap1.usuario_destino' : ''
																				},
																				success:function(response,opts){
																					Ext.Ajax.request( {
																						url     : _URL_NOMBRE_TURNADO
																						,params : {
																							'params.ntramite': record.get('ntramite'),
																							'params.rolDestino': 'operadorsini'
																						}
																						,success : function (response) {
																							var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																							Ext.Ajax.request({
																								url     : _URL_ACTUALIZA_TURNADOMC
																								,params : {           
																									'params.ntramite': record.get('ntramite')
																								}
																								,success : function (response){
																									mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
																									loadMcdinStore();
																									windowLoader.close();
																								},
																								failure : function (){
																									centrarVentanaInterna(Ext.Msg.show({
																										title:'Error',
																										msg: 'Error de comunicaci&oacute;n',
																										buttons: Ext.Msg.OK,
																										icon: Ext.Msg.ERROR
																									}));
																								}
																							});
																						},
																						failure : function () {
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
																				failure:function(response,opts) {
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
																switch (action.failureType) {
																	case Ext.form.action.Action.CONNECT_FAILURE:
																		errorComunicacion();
																		break;
																	case Ext.form.action.Action.SERVER_INVALID:
																		mensajeError(action.result.mensaje);
																		break;
																}
															},
															success: function(form, action) {
																Ext.Ajax.request( {
																	url: _URL_TurnarAOperadorReclamacion,
																	params: {
																			'smap1.ntramite' 	 	 : record.get('ntramite'), 
																			'smap1.status'   		 : _STATUS_TRAMITE_EN_CAPTURA
																			,'smap1.rol_destino'     : 'operadorsini'
																			,'smap1.usuario_destino' : ''
																	},
																	success:function(response,opts){
																		Ext.Ajax.request( {
																			url     : _URL_NOMBRE_TURNADO
																			,params : {
																				'params.ntramite'  : record.get('ntramite'),
																				'params.rolDestino': 'operadorsini'
																			}
																			,success : function (response) {
																				var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																				Ext.Ajax.request({
																					url     : _URL_ACTUALIZA_TURNADOMC
																					,params : {           
																						'params.ntramite': record.get('ntramite')
																					}
																					,success : function (response){
																						mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito : '+usuarioTurnadoSiniestro);
																						loadMcdinStore();
																						windowLoader.close();
																					},
																					failure : function (){
																						centrarVentanaInterna(Ext.Msg.show({
																							title:'Error',
																							msg: 'Error de comunicaci&oacute;n',
																							buttons: Ext.Msg.OK,
																							icon: Ext.Msg.ERROR
																						}));
																					}
																				});
																			},
																			failure : function () {
																				centrarVentanaInterna(Ext.Msg.show({
																					title:'Error',
																					msg: 'Error de comunicaci&oacute;n',
																					buttons: Ext.Msg.OK,
																					icon: Ext.Msg.ERROR
																				}));
																			}
																		});
																	},
																	failure:function(response,opts) {
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
				}
				
			},failure : function () {
				form.setLoading(false);
				Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				});
			}
		});
	}
	
	/***TURNAR AL AREA MEDICA POR PARTE DEL OPERADOR DE RECLAMACIONES***/
	function turnarAareaMedica(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		 var comentariosText = Ext.create('Ext.form.field.TextArea', {
        	fieldLabel: 'Observaciones'
    		,labelWidth: 150
    		,width: 600
    		,name:'smap1.comments'
			,height: 250
			,allowBlank : false
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
        	            	    	if (this.up().up().form.isValid()) {
        	            	    		this.up().up().form.submit({
        	            		        	waitMsg:'Procesando...',
        	            		        	params: {
        	            		        		'smap1.ntramite' : record.get('ntramite'), 
        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_REVISION_MEDICA
        	            		        		,'smap1.rol_destino'     : 'medajustador'
        	            		        		,'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
        	            		        		,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
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
								    	            	Ext.Ajax.request({
															url     : _URL_ACTUALIZA_TURNADOMC
															,params : {           
																'params.ntramite': record.get('ntramite')
															}
															,success : function (response){
																mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
				        	            						loadMcdinStore();
				        	            						windowLoader.close();
															},
															failure : function (){
																centrarVentanaInterna(Ext.Msg.show({
																	title:'Error',
																	msg: 'Error de comunicaci&oacute;n',
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																}));
															}
														});
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
	
	/***** SOLICITAR PAGO *****/
	function solicitarPago(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		debug("Valor del record :",record);
		if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
			mensajeWarning('Ya se ha solicitado el pago para este tr&aacute;mite.');	
			return;
		}else{
			//validamos los montos 
			Ext.Ajax.request({
				url	: _URL_MONTO_PAGO_SINIESTRO
				,params:{
					'params.ntramite' : record.get('ntramite'),
					'params.cdramo'   : record.get('cdramo'),
					'params.tipoPago' : record.get('parametros.pv_otvalor02')
				}
				,success : function (response){
					var jsonRespuesta =Ext.decode(response.responseText);//.datosInformacionAdicional[0];
					debug("Valor de Respuesta", jsonRespuesta);
					
					if(jsonRespuesta.success == true){
						if( record.get('parametros.pv_otvalor02') ==_PAGO_DIRECTO){
							_11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex,"0");
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
												_11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex,"0");
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
					}else {
						centrarVentanaInterna(mensajeWarning(jsonRespuesta.mensaje));
					}
				},
				failure : function (){
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
	
	//Validamos si existe las Validaciones 
	function _11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex,procesaPago){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		myMask.show();
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url     : _URL_VALIDA_COBASEGURADOS
			,params:{
				'params.ntramite'  : record.get('ntramite')
			}
			,success : function (response) {
				json = Ext.decode(response.responseText);
				if(json.success==false){
					myMask.hide();
					centrarVentanaInterna(mensajeWarning(json.msgResult));
				}else{
					myMask.hide();
					if(procesaPago =="0"){
						_11_validaImporteAseguradoTramite(grid,rowIndex,colIndex);
					}
				}
			},
			failure : function (){
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
	}
	
	
		//Validamos si existe las Validaciones 
	function _11_validaImporteAseguradoTramite(grid,rowIndex,colIndex){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		myMask.show();
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url     : _URL_VALIDA_IMPASEGURADOSINIESTRO
			,params:{
				'params.ntramite'  : record.get('ntramite'),
				'params.tipopago'  : record.get('parametros.pv_otvalor02')
			}
			,success : function (response) {
				json = Ext.decode(response.responseText);
				if(json.success==false){
					myMask.hide();
					centrarVentanaInterna(mensajeWarning(json.msgResult));
				}else{
					myMask.hide();
					//mostrarSolicitudPago(grid,rowIndex,colIndex);
				}
			},
			failure : function (){
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
	}
	
	function mostrarSolicitudPago(grid,rowIndex,colIndex){
		storeDestinoPago = Ext.create('Ext.data.JsonStore', {
			model:'Generic',
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
		
		storeConceptoPago = Ext.create('Ext.data.JsonStore', {
			model:'Generic',
			autoLoad:true,
			proxy: {
				type: 'ajax',
				url: _URL_CATALOGOS,
				extraParams : {catalogo:_CATALOGO_CONCEPTOPAGO},
				reader: {
					type: 'json',
					root: 'lista'
				}
			}
		});
		
		storeAsegurados2 = Ext.create('Ext.data.Store', {
			model:'Generic',
			autoLoad:false,
			proxy: {
				type: 'ajax',
				url : _URL_LISTADO_ASEGURADO_POLIZA,
				reader: {
					type: 'json',
					root: 'listaAsegurado'
				}
			}
		});
		
		msgWindow = Ext.Msg.show({
	        title: 'Aviso',
	        msg: '&iquest;Esta seguro que desea solicitar el pago?',
	        buttons: Ext.Msg.YESNO,
	        icon: Ext.Msg.QUESTION,
	        fn: function(buttonId, text, opt){
	        	if(buttonId == 'yes'){
	        		var record = grid.getStore().getAt(rowIndex);
	        		var recordAdicional = grid.getStore().getAt(rowIndex);
	        		
	        		storeConceptoPago.load({
						params : {
							'params.cdramo': record.get('cdramo')
						}
					});
	        		
	        		storeAsegurados2.load({
						params:{
							'params.cdunieco': record.raw.cdunieco,
							'params.cdramo': record.raw.cdramo,
							'params.estado': record.raw.estado,
							'params.nmpoliza': record.raw.nmpoliza
						}
					});
	        		
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
   		    	        queryMode    :'local',		store 			: storeConceptoPago
   		    	    });
	        		
   		    		var cmbBeneficiario= Ext.create('Ext.form.ComboBox',{
						name:'cmbBeneficiario',			fieldLabel: 'Beneficiario',			queryMode: 'local'/*'remote'*/,			displayField: 'value',
						valueField: 'key',				editable:true,						forceSelection : true,		matchFieldWidth: false,
						queryParam: 'params.cdperson',	minChars  : 2, 						store : storeAsegurados2,	triggerAction: 'all',
						width		 : 350,
						allowBlank: record.get('parametros.pv_otvalor02') == _PAGO_DIRECTO,
						hidden : record.get('parametros.pv_otvalor02') == _PAGO_DIRECTO,
						listeners : {
							'select' : function(e) {
								Ext.Ajax.request({
									url     : _URL_CONSULTA_BENEFICIARIO
									,params:{
										'params.cdunieco'  : record.raw.cdunieco,
										'params.cdramo'    : record.raw.cdramo,
										'params.estado'    : record.raw.estado,
										'params.nmpoliza'  : record.raw.nmpoliza,
										'params.cdperson'  : e.getValue()
									}
									,success : function (response) {
										json = Ext.decode(response.responseText);
										if(json.success==false){
											Ext.Msg.show({
												title:'Beneficiario',
												msg: json.mensaje,
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.WARNING
											});
											panelModificacion.query('combo[name=cmbBeneficiario]')[0].setValue('')
										}
									},
									failure : function (){
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
						}
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
		           		    		panelModificacion.query('combo[name=cmbBeneficiario]')[0].setValue(json.otvalor04mc);
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
			        	                        concepPago,
												cmbBeneficiario],
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
			        	        	        				params:{
			        	        	        					ntramite:record.get('ntramite'),
			        	        	        					cdtipsit:cdtipsitTramite,
			        	        	        					destinoPago:datos.destinoPago,
			        	        	        					concepPago:datos.concepPago,
			        	        	        					beneficiario : datos.cmbBeneficiario,
																tipoPago : record.get('parametros.pv_otvalor02')
			        	        	        				}
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

	/*** VALIDACION DE LOS BOTONES ***/
	/*Validacion de los aranceles para el pago automatico*/
	function validarArancelesPagoAutomatico(button, grid,rowIndex,colIndex){
		var form=button.up().up();
		
		if(mcdinGrid.getView().getSelectionModel().hasSelection()){
			totalTramites = mcdinGrid.getView().getSelectionModel().getSelection();
			var totalTramite ="";
			for(var i=0;i<totalTramites.length;i++) {
                tramite=totalTramites[i];
                totalTramite = totalTramite+""+tramite.get('ntramite');
                if(i< totalTramites.length -1){
                	totalTramite = totalTramite+"|";
                }
            }
			form.setLoading(true);
			Ext.Ajax.request({
				url	 : _URL_VALIDA_ARANCELES
				,params:{
					'params.ntramite'  : totalTramite
				}
				,success : function (response) {
					form.setLoading(false);
					banderaAranceles ="0";
					var resultAranceles = "";
					var arancelesTra = Ext.decode(response.responseText).loadList;
					for(i = 0; i < arancelesTra.length; i++){
						banderaAranceles = "1";
						resultAranceles = resultAranceles + 'El C.R.' + arancelesTra[i].NTRAMITE+ ' en la Factura ' + arancelesTra[i].NFACTURA + ' del siniestro '+ arancelesTra[i].NMSINIES+' .El concepto '+ arancelesTra[i].CDCONCEP+ ' el importe del arancel es 0. <br/>';
					}
					if(banderaAranceles == "1"){
						centrarVentanaInterna(mensajeWarning(resultAranceles));
					}else{
						centrarVentanaInterna(mensajeCorrecto('\u00C9xito', 'Los montos de los aranceles son mayores a 0.', Ext.Msg.OK, Ext.Msg.INFO));
					}
					
				},failure : function () {
					form.setLoading(false);
					Ext.Msg.show({
						title:'Error',
						msg: 'Error de comunicaci&oacute;n',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					});
				}
			});
			
		}else {
			centrarVentanaInterna(mensajeWarning("Debe seleccionar al menos un Contrarecibo."));
		}
	}
	
	/*Generacion de calculos pagos Automaticos*/
	function generarCalculoPagoAutomatico(button, grid,rowIndex,colIndex){
		var form=button.up().up();
		if(mcdinGrid.getView().getSelectionModel().hasSelection()){
			totalTramites = mcdinGrid.getView().getSelectionModel().getSelection();
			form.setLoading(true);
			for(var i=0;i<totalTramites.length;i++) {
                var tramite=totalTramites[i];
                if(+tramite.get('parametros.pv_otvalor25') != 1){
	                Ext.Ajax.request({
	   					url	 : _URL_GENERAR_CALCULO
	   					,params:{
	   						'params.ntramite'  : tramite.get('ntramite')
	   					}
	   					,success : function (response){
	   						form.setLoading(false);
	   					}
	   				});
                }
            }
			form.setLoading(false);
			Ext.create('Ext.form.Panel').submit({
				url				: _URL_MESACONTROL
				,standardSubmit	:true
				,params			:
				{
					'smap1.gridTitle'		: 'Siniestros'
					,'smap2.pv_cdtiptra_i'	: _PAGO_AUTOMATICO
				}
			});
		}else {
			form.setLoading(false);
			centrarVentanaInterna(mensajeWarning("Debe seleccionar al menos un Contrarecibo."));
		}
	}	

	/*Validacion de los aranceles para el pago automatico*/
	function solicitarPagoAutomatico(button, grid,rowIndex,colIndex){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		myMask.show();
		var form=button.up().up();
		if(mcdinGrid.getView().getSelectionModel().hasSelection()){
			totalTramites = mcdinGrid.getView().getSelectionModel().getSelection();
			var totalTramite ="";
			for(var i=0;i<totalTramites.length;i++) {
                tramite=totalTramites[i];
                totalTramite = totalTramite+""+tramite.get('ntramite');
                if(i< totalTramites.length -1){
                	totalTramite = totalTramite+"|";
                }
            }
			Ext.Ajax.request({
				url	 : _URL_VALIDA_COBASEGURADOSCR
				,params:{
					'params.ntramite'  : totalTramite
				}
				,success : function (response) {
					
					json = Ext.decode(response.responseText);
					if(json.success==false){
						myMask.hide();
						centrarVentanaInterna(mensajeWarning(json.msgResult));
					}else{
						Ext.Ajax.request({
							url	 : _URL_VALIDA_FACTMONTO
							,params:{
								'params.ntramite'  : totalTramite
							}
							,success : function (response) {
								myMask.hide();
								banderaAranceles ="0";
								var resultAranceles = 'Los siguientes C.R. no se procesaran : <br/>';
								var arancelesTra = Ext.decode(response.responseText).loadList;
								for(i = 0; i < arancelesTra.length; i++){
									banderaAranceles = "1";
									resultAranceles = resultAranceles + '   - El C.R.' + arancelesTra[i].NTRAMITE+ ' el n&uacute;mero de factura es:  ' + arancelesTra[i].NFACTURA + ' el importe de la factura es : '+ arancelesTra[i].PTIMPORT+'<br/>';
								}
								
								if(banderaAranceles == "1"){
									centrarVentanaInterna(Ext.Msg.show({
										title:'Aviso del sistema',
										msg: resultAranceles, //'Se requere una autorizaci&oacute;n especial para continuar.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.WARNING,
										fn: function(){
											debug("Mandamos el listado de los tramites");
											validaInformacion(totalTramite);
										}
									}));
								}else{
									myMask.show();
									validaInformacion(totalTramite);
								}
								
							},failure : function () {
								form.setLoading(false);
								Ext.Msg.show({
									title:'Error',
									msg: 'Error de comunicaci&oacute;n',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.ERROR
								});
							}
						});
					}
				},failure : function () {
					form.setLoading(false);
					Ext.Msg.show({
						title:'Error',
						msg: 'Error de comunicaci&oacute;n',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					});
				}
			});
		}else {
			centrarVentanaInterna(mensajeWarning("Debe seleccionar al menos un Contrarecibo."));
		}
	}
	
	function validaInformacion(totalTramite){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		myMask.show();
		Ext.Ajax.request({
			url: _URL_VALIDA_SOLICITUD_PAGO,
			params: {
	    		'params.ntramite' : totalTramite
	    	},
			success: function(response, opts) {
				myMask.hide();
				var respuestaMensaje = Ext.decode(response.responseText).loadList;
				var resultRespuesta  = 'C.R. Procesados : <br/>';
				var banderaRespuesta = 0;
				for(i = 0; i < respuestaMensaje.length; i++){
					banderaRespuesta = "1";
					resultRespuesta = resultRespuesta + respuestaMensaje[i].mensajeRespuesta+'<br/>';
				}
				
				if(banderaRespuesta == "1"){
					myMask.hide();
					centrarVentanaInterna(Ext.Msg.show({
						title:'Respuesta Pagos',
						msg: resultRespuesta,
						buttons: Ext.Msg.OK,
						fn: function(){
							Ext.create('Ext.form.Panel').submit({
								url				: _URL_MESACONTROL
								,standardSubmit	:true
								,params			:
								{
									'smap1.gridTitle'		: 'Siniestros'
									,'smap2.pv_cdtiptra_i'	: _PAGO_AUTOMATICO
								}
							});
						}
					}));
				}
			},
			failure: function(){
				mcdinGrid.setLoading(false);
				mensajeError('No se pudo solicitar el pago.');
			}
		});
	}
	
	function turnarAreclamacionesMedAjustador(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url     : _URL_P_MOV_MAUTSINI
			,params : {
				'params.ntramite': record.get('ntramite')
			}
			,success : function (response) {
				var respuestaMensaje = Ext.decode(response.responseText).mensaje;
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
			            		, value : respuestaMensaje
			        			,height: 250
			        			,allowBlank : false
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
					        	            			var formPanel = this.up().up();
					        	            	    	if (formPanel.form.isValid()) {
					        	            	    		formPanel.form.submit({
									        	            		        	waitMsg:'Procesando...',
									        	            		        	params: {
									        	            		        		'smap1.ntramite' : record.get('ntramite'), 
									        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
									        	            		        		,'smap1.swagente' : _fieldById('SWAGENTE3').getGroupValue()
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
									        	            						Ext.Ajax.request({
																						url     : _URL_ACTUALIZA_TURNADOMC
																						,params : {           
																							'params.ntramite': record.get('ntramite')
																						}
																						,success : function (response){
																							mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito.');
											        	            						loadMcdinStore();
											        	            						windowLoader.close();
																						},
																						failure : function (){
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
							}else {
								mensajeError(jsonRes.msgResult);
							}
					},
					failure: function(){
						mensajeError('Error al turnar.');
					}
				});
			},
			failure : function (){
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
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
			,allowBlank : false
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
	   								    	             Ext.Ajax.request({
																url     : _URL_ACTUALIZA_TURNADOMC
																,params : {           
																	'params.ntramite': record.get('ntramite')
																}
																,success : function (response){
																	mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
																	loadMcdinStore();
																	windowLoader.close();
																},
																failure : function (){
																	centrarVentanaInterna(Ext.Msg.show({
																		title:'Error',
																		msg: 'Error de comunicaci&oacute;n',
																		buttons: Ext.Msg.OK,
																		icon: Ext.Msg.ERROR
																	}));
																}
															});
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
	
	function reasignarClick(grid,dataIndex)
	{
		var record=grid.getStore().getAt(dataIndex);
		debug('reasignarClick record:',record);
		var rol='';
		if(record.get('status') == _STATUS_TRAMITE_EN_REVISION_MEDICA)
		{
			rol='medajustador';
		}
		else if(record.get('status') == _STATUS_TRAMITE_EN_CAPTURA)
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
	
	function subirDocumentoParaWindow(){
        windowLoader = Ext.create('Ext.window.Window', {
            title   : 'Cargar documento Layout'
            ,closeAction : 'hide'
           	,width  : 400
            ,modal  : true
    		,bodyStyle:'padding:5px;'
            ,items  :
            [
                Ext.create('Ext.form.Panel',
                {
                    url          : _URL_SubirLayout
                    ,border 	 : 0
                    ,bodyPadding : 5
                    ,items       :
                    [
                        cmbProveedor,
                        tipoLayout,
                        {
                            xtype       : 'filefield'
                            ,fieldLabel : 'Archivo'
                            ,buttonText : 'Examinar...'
                            ,buttonOnly : false
                            ,name       : 'censo'
                            ,labelWidth : 100
                            ,width      : 330
                            ,allowBlank : false
                            ,msgTarget  : 'side'
                            ,cAccept    : ['xls','xlsx']
                            ,listeners  :
                            {
                                change : function(me)
                                {
                                    var indexofPeriod = me.getValue().lastIndexOf("."),
                                    uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                                    if (!Ext.Array.contains(this.cAccept, uploadedExtension))
                                    {
                                        centrarVentanaInterna(Ext.MessageBox.show(
                                        {
                                            title   : 'Error de tipo de archivo',
                                            msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                                            buttons : Ext.Msg.OK,
                                            icon    : Ext.Msg.WARNING
                                        }));
                                        me.reset();
                                    }
                                }
                            }
                        }
                    ]
                    ,buttonAlign : 'center'
                    ,buttons     :
                    [
                        {
                            text     : 'Cargar archivo'
                            ,icon    : '${ctx}/resources/fam3icons/icons/group_edit.png'
                            ,handler : function(me){ _p21_subirArchivoParametrizadosLayout(me); } //_p21_subirArchivoParametrizadosLayout
                        }
                    ]
                })
            ]
        });
       	centrarVentanaInterna(windowLoader.show());
	}
	
    function _p21_subirArchivoParametrizadosLayout(button,nombreCensoParaConfirmar)
    {
        var form=button.up().up();
        var valido=form.isValid();
        if(!valido){
            datosIncompletos();
        }
        
        if(valido){
        	var cdprestador = form.down('combo[name=cmbProveedor]').getValue();
        	var layoutConf  = form.down('combo[name=tipoLayout]').getValue();
        	form.setLoading(true);
            var timestamp = new Date().getTime();
            form.submit({
                params   : {
                    'smap1.timestamp' : timestamp
                    ,'smap1.ntramite' : ''
                }
                ,success : function() {
                	Ext.Ajax.request({
            			url     : _URL_EXISTE_CONF_PROV
            			,params:{
            				'params.cdpresta'  : cdprestador,
            				'params.tipoLayout': layoutConf
            			}
            			,success : function (response){
            				debug("Respuesta de configuracion ==> ",Ext.decode(response.responseText).validacionGeneral);
            				if( Ext.decode(response.responseText).validacionGeneral =="S"){
        							//Continuamos para la validacion del archivo, para eso tenemos que validar con el excel
				                    var conceptos = {};
				                    conceptos['timestamp']             = timestamp;
				                    conceptos['nombreLayoutConfirmado'] = nombreCensoParaConfirmar;
				                    conceptos['cdpresta']              = cdprestador;
				                    conceptos['tipoLayout']            = layoutConf;
				                    var grupos = [];
				                    
        							Ext.Ajax.request( {
				                        url         : _URL_ValidaLayoutFormatoExcel
				                        ,jsonData   : {
				                            smap1   : conceptos
				                            ,olist1 : grupos
				                        }
				                        ,success  : function(response) {
				                            form.setLoading(true);
				                            var json=Ext.decode(response.responseText);
				                            debug('### subir censo completo response:',json);
				                            
				                            if(json.exito){
				                            	//Realizamos el llamado al procedure de la validacion del
				                            	Ext.Ajax.request( {
													url         : _URL_ValidaLayoutConfigExcel
													,jsonData   : {
														smap1   : conceptos
														,olist1 : grupos
													}
													,success  : function(response) {
														var json=Ext.decode(response.responseText);
														debug('### subir censo completo response:',json);
														if(json.exito){
															//Realizamos el llamado para procesar los registros y nos respondera con los numero de tramites generados
													        Ext.Ajax.request({
																url: _UrlProcesarTramiteLayout,
																params:{
										            				'params.cdpresta'    : cdprestador,
										            				'params.tipoproc'    : layoutConf,
										            				'params.layoutConf'  : timestamp
										            			},
																success: function(response, opts) {
																	var respuesta = Ext.decode(response.responseText);
																	debug("Valor de la respuesta ===> ",respuesta);
																	if(respuesta.success){
																		form.setLoading(false);
																		centrarVentanaInterna(Ext.Msg.show({
																			title:'eeacute;xito',
																			msg: respuesta.mensaje,
																			buttons: Ext.Msg.OK,
																			fn: function(){
																				Ext.create('Ext.form.Panel').submit({
																					url				: _URL_MESACONTROL
																					,standardSubmit	:true
																					,params			:
																					{
																						'smap1.gridTitle'		: 'Siniestros'
																						,'smap2.pv_cdtiptra_i'	: _PAGO_AUTOMATICO
																					}
																				});
																			}
																		}));
																	}else {
																		form.setLoading(false);
																		centrarVentanaInterna(mensajeWarning(respuesta.mensaje));
																	}
																	
																},
																failure: function(){
																	mcdinGrid.setLoading(false);
																	mensajeError('Redireccionar a Mesa de Control.');
																}
															});
														 }
														 else{
															form.setLoading(false);
															centrarVentanaInterna(mensajeWarning('El archivo contiene errores de Datos.<br/>Favor de validarlo.',function(){
																centrarVentanaInterna(Ext.create('Ext.window.Window', {
																	modal  : true
																	,title : 'Error'
																	,items : [
																		{
																			 xtype     : 'textarea'
																			 ,width    : 700
																			 ,height   : 400
																			 ,readOnly : true
																			 ,value    : json.smap1.erroresCenso
																		}
																	]
																}).show());
															}));
														 }
													 }
													 ,failure  : function(){
														 form.setLoading(false);
														 errorComunicacion();
													 }
												 });
				                             }
				                             else{
				                             	form.setLoading(false);
					                            centrarVentanaInterna(mensajeWarning('El archivo contiene errores de Formato.<br/>Favor de validarlo.',function(){
													centrarVentanaInterna(Ext.create('Ext.window.Window', {
				                                     	modal  : true
				                                     	,title : 'Error'
				                                     	,items : [
				                                     		{
					                                             xtype     : 'textarea'
					                                             ,width    : 700
					                                             ,height   : 400
					                                             ,readOnly : true
					                                             ,value    : json.respuesta
				                                         	}
				                                     	]
			                                     	}).show());
												}));
				                             }
				                         }
				                         ,failure  : function(){
				                             form.setLoading(false);
				                             errorComunicacion();
				                         }
				                     });
            				}else{
            					form.setLoading(false);
            					centrarVentanaInterna(mensajeWarning('El provedor seleccionado, no tiene la configuraci&oacute;n para pago.'));
            				}
            			},
            			failure : function (){
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
                 ,failure : function()
                 {
                     if(!Ext.isEmpty(nombreCensoParaConfirmar)){
                         debug('se quita allowblank');
                         form.down('filefield').allowBlank = false;
                     }
                     form.setLoading(false);
                     errorComunicacion(null,'Error complementando datos');
                 }
             });
         }
     }
    
	function procesarTramiteSisco(button, grid,rowIndex,colIndex){
		var form=button.up().up();
		form.setLoading(true);
        Ext.Ajax.request({
			url: _UrlProcesarTramiteSiniestro,
			success: function(response, opts) {
				var respuesta = Ext.decode(response.responseText);
				debug("Valor de la respuesta ===> ",respuesta);
				if(respuesta.success){
					form.setLoading(false);
					centrarVentanaInterna(mensajeCorrecto('Aviso',respuesta.mensaje));	
				}else {
					form.setLoading(false);
					centrarVentanaInterna(mensajeWarning(respuesta.mensaje));
				}
				
			},
			failure: function(){
				mcdinGrid.setLoading(false);
				mensajeError('No se pudo solicitar el pago.');
			}
		});
	}

	Ext.onReady(function(){
		    Ext.Ajax.timeout = 1000*60*15; // 15 minutos
		    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
		    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
		    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
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
		
		Ext.define('modelListadoProvMedico',{
			extend: 'Ext.data.Model',
				fields: [
						{type:'string',		name:'cdpresta'},	{type:'string', name:'nombre'},		{type:'string', name:'cdespeci'},
						{type:'string',		name:'descesp'}
			]
		});
	    var storeProveedor = Ext.create('Ext.data.Store', {
	    	model:'modelListadoProvMedico',
	    	autoLoad:false,
	    	proxy: {
	    		type: 'ajax',
	    		url : _URL_CATALOGOS,
	    		extraParams:{
	    			catalogo         : _CATALOGO_PROVEEDORES,
	    			catalogoGenerico : true
	    		},
	    		reader: {
	    			type: 'json',
	    			root: 'listaGenerica'
	    		}
	    	}
	    });
	    
		var storeTipoLayout = Ext.create('Ext.data.JsonStore', {
			model:'Generic',
			proxy: {
				type: 'ajax',
				url: _URL_CATALOGOS,
				extraParams : {catalogo:_CATALOGO_ConfLayout},
				reader: {
					type: 'json',
					root: 'lista'
				}
			}
		});
		storeTipoLayout.load();
	    
		cmbProveedor = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel : 'Proveedor',			displayField : 'nombre',			name:'cmbProveedor',
			valueField   : 'cdpresta',			forceSelection : true,
			matchFieldWidth: false,				queryMode :'remote',				queryParam: 'params.cdpresta',
			minChars  : 2,						store : storeProveedor,				triggerAction: 'all',
			hideTrigger:true,					allowBlank:false
			,labelWidth : 100
            ,width      : 330
		});
		
		tipoLayout= Ext.create('Ext.form.ComboBox',{
			fieldLabel   : 'Layout',	allowBlank   : false,
			editable   :false,			displayField : 'value',				valueField: 'key',			    		forceSelection  : true,
			labelWidth : 100,			queryMode    :'local',				editable  :false,						name			:'tipoLayout',
			width	   : 330,			store: storeTipoLayout,				emptyText:'Seleccione...'
		});
	});
<s:if test="false">
</script>
</s:if>