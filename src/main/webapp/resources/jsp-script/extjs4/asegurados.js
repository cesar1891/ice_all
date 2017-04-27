///////////////////////////////////////
////// ini catalogos de tatrisit //////
///////////////////////////////////////
var comboGeneros, storeGeneros; // 1
// fecha nacimiento 2
var campoCodigoPostal; // 3
var comboEstados, storeEstados; // 4
// var comboCiudad,storeCiudades; //4 X(
var comboDeducible, storeDeducible; // 5
var comboCopago, storeCopagos; // 6
var comboSumaAsegurada, storeSumasAseguradas; // 7
var comboCirculoHospitalario, storeCirculoHospitalario; // 8
var comboCoberturaVacunas, storeCoberturaVacunas; // 9
var comboCoberturaPrevencionEnfermedadesAdultos, storeCoberturaPrevencionEnfermedadesAdultos; // 10
var comboMaternidad, storeMaternidad; // 11
var comboSumaAseguradaMaternidad, storeSumaAseguradaMaternidad; // 12
var comboBaseTabuladorReembolso, storeBaseTabuladorReembolso; // 13
var comboCostoEmergenciaExtranjero, storeCostoEmergenciaExtranjero; // 14
var comboCobElimPenCambioZona, storeCobElimPenCambioZona; // 15
var comboRoles, storeRoles; // 16
var comboMunicipios, storeMunicipios; // 17 --- va en el 5
// /////////////////////////////////////
// //// fin catalogos de tatrisit //////
// /////////////////////////////////////

var storeIncisos;
var formPanel;
var gridIncisos;
var gridResultados, storeResultados;
var botonVerCoberturas;
var botonComprar;
var botonDetalle;

// window coberturas
var windowCoberturas;
var coberturasFormPanel;
var storeCoberturas;
var gridCoberturas;
var windowAyudaCobertura;
var botonVerDetalleCobertura;

// grid selection
var selected_prima;
var selected_cd_plan;
var selected_ds_plan;
var selected_nm_plan;
var selected_record;
var selected_cobertura;

function detallesCobertura() {
	windowCoberturas.setLoading(true);
	Ext.Ajax
			.request({
				url : _URL_DETALLE_COBERTURA,
				params : {
					idCobertura : selected_cobertura.get('cdGarant'),
					idCiaAseguradora : selected_record.get('cdCiaaseg'),// del
																		// grid
																		// de
																		// resultados
					idRamo : selected_record.get('cdRamo')
				// del grid de resultados
				},
				success : function(response, opts) {
					// Ext.MessageBox.hide();
					windowCoberturas.setLoading(false);
					var jsonResp = Ext.decode(response.responseText);
					if (jsonResp.ayudaCobertura
							&& jsonResp.ayudaCobertura.dsGarant
							&& jsonResp.ayudaCobertura.dsGarant.length > 0
							&& jsonResp.ayudaCobertura.dsAyuda
							&& jsonResp.ayudaCobertura.dsAyuda.length > 0) {
						windowAyudaCobertura.html = '<table width=430 ><tr><td align=left bgcolor="#98012e" style="color:white;font-size:11px;"><b>'
								+ jsonResp.ayudaCobertura.dsGarant
								+ '</b></td></tr><tr><td style="font-size:11px; ">'
								+ jsonResp.ayudaCobertura.dsAyuda
								+ '</td></tr></table>';
						windowAyudaCobertura.show();
					} else {
						Ext.Msg.show({
							title : 'No hay informaci&oacute;n',
							msg : 'No hay informaci&oacute;n de la cobertura',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.ERROR
						});
					}
				},
				failure : function(response, opts) {
					windowCoberturas.setLoading(false);
					Ext.Msg.show({
						title : 'Error',
						msg : 'Error de comunicaci&oacute;n',
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.ERROR
					});
				}
			});
}

function bloquearFormulario(isBloqueado) {
	formPanel.getForm().getFields().each(function(field, index) {
		field.setReadOnly(isBloqueado);
	});
	Ext.getCmp('botonLimpiar').setDisabled(isBloqueado);
	Ext.getCmp('botonCotizar').setDisabled(isBloqueado);
	Ext.getCmp('idCotizacion').setReadOnly(true);
	Ext.getCmp('ntramite').setReadOnly(true);
	Ext.getCmp('fechaFinVigencia').setReadOnly(true);
	gridIncisos.setDisabled(isBloqueado);
}

// /////////////////////////////////////////////////////
// //// funcion que muestra el grid de resultados //////
/* /////////////////////////////////////////////////// */
function mostrarGrid() {
	bloquearFormulario(true);
	gridResultados.show();
	window.parent.scrollTo(0, 800);
	storeResultados.load({
		callback : function() {
			// window.console&&console.log("cotizado");
			gridResultados.getView().refresh();
		}
	});
}
/* /////////////////////////////////////////////////// */
// //// funcion que muestra el grid de resultados //////
// /////////////////////////////////////////////////////
Ext
		.onReady(function() {

			Ext.define('ModeloDetalleCotizacion', {
				extend : 'Ext.data.Model',
				fields : [ {
					name : 'Codigo_Garantia'
				}, {
					name : 'Importe',
					type : 'float'
				}, {
					name : 'Nombre_garantia'
				}, {
					name : 'cdtipcon'
				}, {
					name : 'nmsituac'
				}, {
					name : 'orden'
				}, {
					name : 'parentesco'
				}, {
					name : 'orden_parentesco'
				} ]
			});

			// ////////////////////////////
			// //// Inicio de stores //////
			// ////////////////////////////

			// 1 sexo (GRID)
			storeGeneros = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_SEXO,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 4 estados
			storeEstados = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : false,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_ESTADO,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					/*
					 * , sorters: [ { property : 'value', direction: 'ASC' } ]
					 */
					});

			// 17 municipios
			storeMunicipios = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : false,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_MUNICIPIO,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					/*
					 * , sorters: [ { property : 'value', direction: 'ASC' } ]
					 */
					});

			/*
			 * 4 ciudades X( storeCiudades = new Ext.data.Store({ model:
			 * 'Generic', autoLoad:false, proxy: { type: 'ajax', url :
			 * _URL_OBTEN_CATALOGO_GENERICO, reader: { type: 'json', root:
			 * 'lista' } }, sorters: [ { property : 'value', direction: 'ASC' } ]
			 * });
			 */

			// 5 deducible
			storeDeducible = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_DEDUCIBLE,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 6 copago
			storeCopagos = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_COPAGO,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 7 suma asegurada
			storeSumasAseguradas = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_SUMA_ASEGURADA,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 8 circulo hospitalario
			storeCirculoHospitalario = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_CIRCULO_HOSPITALARIO,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					/*
					 * , sorters: [ { property : 'value', direction: 'ASC' } ]
					 */
					});

			// 9 cobertura vacunas
			storeCoberturaVacunas = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo : constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_COBERTURA_VACUNAS,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 10 enfemedades adultos
			storeCoberturaPrevencionEnfermedadesAdultos = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo:constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_COBERTURA_PREV_ENF_ADULTOS,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 11 maternidad
			storeMaternidad = new Ext.data.Store({
				model : 'Generic',
				autoLoad : true,
				proxy : {
					type : 'ajax',
					url : _URL_OBTEN_CATALOGO_GENERICO,
					extraParams : {
						catalogo:constanteTatrisitPantallaCotiza,
						'params.cdatribu' : CDATRIBU_MATERNIDAD,
						'params.cdtipsit' : inputCdtipsit
					},
					reader : {
						type : 'json',
						root : 'lista'
					}
				}
			});

			// 12 suma maternidad
			storeSumaAseguradaMaternidad = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo:constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_SUMA_ASEGUARADA_MATERNIDAD,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 13 base tabulador reembolso
			storeBaseTabuladorReembolso = new Ext.data.Store({
				model : 'Generic',
				autoLoad : true,
				proxy : {
					type : 'ajax',
					url : _URL_OBTEN_CATALOGO_GENERICO,
					extraParams : {
						catalogo:constanteTatrisitPantallaCotiza,
						'params.cdatribu' : CDATRIBU_BASE_TABULADOR_REEMBOLSO,
						'params.cdtipsit' : inputCdtipsit
					},
					reader : {
						type : 'json',
						root : 'lista'
					}
				}
			});

			// 14 emergencia extranjero
			storeCostoEmergenciaExtranjero = new Ext.data.Store(
					{
						model : 'Generic',
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : _URL_OBTEN_CATALOGO_GENERICO,
							extraParams : {
								catalogo:constanteTatrisitPantallaCotiza,
								'params.cdatribu' : CDATRIBU_COSTO_EMERGENCIA_EXTRANJERO,
								'params.cdtipsit' : inputCdtipsit
							},
							reader : {
								type : 'json',
								root : 'lista'
							}
						}
					});

			// 15 cobertura eliminacion penalizaacion cambio zona
			storeCobElimPenCambioZona = new Ext.data.Store({
				model : 'Generic',
				autoLoad : true,
				proxy : {
					type : 'ajax',
					url : _URL_OBTEN_CATALOGO_GENERICO,
					extraParams : {
						catalogo:constanteTatrisitPantallaCotiza,
						'params.cdatribu' : CDATRIBU_COB_ELIM_PEN_CAMBIO_ZONA,
						'params.cdtipsit' : inputCdtipsit
					},
					reader : {
						type : 'json',
						root : 'lista'
					}
				}
			});

			// 16 roles (GRID)
			storeRoles = new Ext.data.Store({
				model : 'Generic',
				autoLoad : true,
				proxy : {
					type : 'ajax',
					url : _URL_OBTEN_CATALOGO_GENERICO,
					extraParams : {
						catalogo:constanteTatrisitPantallaCotiza,
						'params.cdatribu' : CDATRIBU_ROL,
						'params.cdtipsit' : inputCdtipsit
					},
					reader : {
						type : 'json',
						root : 'lista'
					}
				}
			});

			storeIncisos = new Ext.data.Store({
				// destroy the store if the grid is destroyed
				autoDestroy : true,
				model : 'IncisoSalud'/*
										 * , data: [ { rol:new
										 * Generic({'key':'20','value':'Tomador'}),
										 * fechaNacimiento:new Date(), sexo:new
										 * Generic({'key':'H','value':'Hombre'}),
										 * nombre:'Alvaro',
										 * segundoNombre:'Jair',
										 * apellidoPaterno:'Mart�nez',
										 * apellidoMaterno:'Varela' }, { rol:new
										 * Generic({'key':'20','value':'Tomador'}),
										 * fechaNacimiento:new Date(), sexo:new
										 * Generic({'key':'H','value':'Hombre'}),
										 * nombre:'Ricardo', segundoNombre:'',
										 * apellidoPaterno:'Bautista',
										 * apellidoMaterno:'Silva' } ]
										 */
			});

			// ///////////////////////////
			// //// Store resutados //////
			/* ///////////////////////// */
			Ext.define('StoreResultados', {
				extend : 'Ext.data.Store',
				constructor : function(cfg) {
					var me = this;
					cfg = cfg || {};
					me.callParent([ Ext.apply({
						autoLoad : false,
						model : 'RowCotizacion',
						storeId : 'StoreResultados',
						// groupField: 'dsUnieco',
						proxy : {
							type : 'ajax',
							url : _URL_RESULTADOS,
							reader : {
								type : 'json',
								root : 'dataResult'
							}
						}
					}, cfg) ]);
				}
			});
			storeResultados = new StoreResultados();
			/* ///////////////////////// */
			// //// Store resutados //////
			// ///////////////////////////
			// ///////////////////////////
			// //// storeCoberturas //////
			/* ///////////////////////// */
			Ext.define('StoreCoberturas', {
				extend : 'Ext.data.Store',
				constructor : function(cfg) {
					var me = this;
					cfg = cfg || {};
					me.callParent([ Ext.apply({
						autoLoad : false,
						model : 'RowCobertura',
						storeId : 'StoreCoberturas',
						// groupField: 'dsUnieco',
						proxy : {
							type : 'ajax',
							url : _URL_COBERTURAS,
							reader : {
								type : 'json',
								root : 'listaCoberturas'
							}
						}
					}, cfg) ]);
				}
			});
			storeCoberturas = new StoreCoberturas();
			/* ///////////////////////// */
			// //// storeCoberturas //////
			// ///////////////////////////
			// /////////////////////////
			// //// Fin de stores //////
			// /////////////////////////
			// /////////////////////////////////////////////////////////
			// //// ComboBox que solo manda el key sin ser objeto //////
			// /////////////////////////////////////////////////////////
			Ext.define('Ext.form.ComboBox2', {
				extend : 'Ext.form.ComboBox',
				setValue : function(v) {
					if (v && v.key && v.value) {
						this.setValue(v.key);
					} else {
						this.callOverridden(arguments);
					}
				}
			});
			// ////////////////////////////////////////////////////////////////
			// //// Fin de comboBox que solo manda el key sin ser objeto //////
			// ////////////////////////////////////////////////////////////////

			// ////////////////////////////////////////////////////////////////////
			// //// Inicio de combos de formulario y combos editores de grid
			// //////
			// ////////////////////////////////////////////////////////////////////

			// 1 sexo (GRID)
			comboGeneros = Ext.create('Ext.form.ComboBox', {
				id : 'comboGeneros',
				store : storeGeneros,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false
			});

			// 3 codigo postal
			campoCodigoPostal = Ext
					.create(
							'Ext.form.field.Text',
							{
								fieldLabel : 'C&oacute;digo postal',
								name : 'codigoPostal',
								allowBlank : false,
								maskRe : /[0-9]/,
								regex : /[0-9]/,
								listeners : {
									blur : function(el) {
										// comboEstados.setLoading(true);
										if (inputCdtipsit == 'SN'
												&& (campoCodigoPostal
														.getValue() < 36000 || campoCodigoPostal
														.getValue() > 38998)) {
											Ext.Msg
													.show({
														title : 'Datos inv&aacute;lidos',
														msg : 'C&oacute;digo postal no v&aacute;lido para este producto',
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.WARNING,
														fn : function() {
															campoCodigoPostal
																	.focus();
														}
													});
										} else {
											storeEstados
													.load({
														params : {
															// codigoTabla:'2TMUNI',
															'params.idPadre' : el.value
														},
														callback : function(
																records,
																operation,
																success) {
															var estadoActual = comboEstados
																	.getValue();
															var actualEnStoreEstados = false;
															storeEstados
																	.each(function(
																			record) {
																		if (estadoActual == record
																				.get('key')) {
																			actualEnStoreEstados = true;
																		}
																	});
															if (!actualEnStoreEstados) {
																comboEstados
																		.clearValue();
															}
															// comboEstados.setLoading(false);
														}
													});
										}
									}
								},
								labelWidth : 250
							});

			// 4 estado
			comboEstados = Ext.create('Ext.form.ComboBox2', {
				id : 'comboEstados',
				fieldLabel : 'Estado',
				name : 'estado',
				model : 'GeEstado',
				store : storeEstados,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250,
				listeners : {
					blur : function(el) {
						storeMunicipios.load({
							params : {
								'params.idPadre' : el.value
							},
							callback : function(records, operation, success) {
								var estadoActual = comboMunicipios.getValue();
								var actualEnStoreEstados = false;
								storeMunicipios.each(function(record) {
									if (estadoActual == record.get('key')) {
										actualEnStoreEstados = true;
									}
								});
								if (!actualEnStoreEstados) {
									comboMunicipios.clearValue();
								}
								// comboEstados.setLoading(false);
							}
						});
					}
				}
			});

			comboMunicipios = Ext.create('Ext.form.ComboBox2', {
				id : 'comboMunicipios',
				fieldLabel : 'Municipio',
				name : 'municipio',
				model : 'GeMunicipio',
				store : storeMunicipios,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250
			});

			/*
			 * 4 ciudad X( comboCiudad=Ext.create('Ext.form.ComboBox2', {
			 * id:'comboCiudad', fieldLabel: 'Ciudad', name:'ciudad',
			 * model:'GeCiudad', store: storeCiudades, queryMode:'local',
			 * displayField: 'value', valueField: 'key', allowBlank:false,
			 * editable:false, emptyText:'Seleccione un estado...' });
			 */

			// 5 deducible
			comboDeducible = Ext.create('Ext.form.ComboBox2', {
				id : 'comboDeducible',
				name : 'deducible',
				fieldLabel : 'Deducible',
				store : storeDeducible,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250
			});

			// 6 copago
			comboCopago = Ext.create('Ext.form.ComboBox2', {
				id : 'comboCopago',
				name : 'copago',
				fieldLabel : 'Copago',
				store : storeCopagos,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250
			});

			// 7 suma asegurada
			comboSumaAsegurada = Ext.create('Ext.form.ComboBox2', {
				id : 'comboSumaAsegurada',
				name : 'sumaSegurada',
				fieldLabel : 'Beneficio m&aacute;ximo',
				store : storeSumasAseguradas,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250
			});

			// 8 circulo hospitalario
			comboCirculoHospitalario = Ext.create('Ext.form.ComboBox2', {
				id : 'comboCirculoHospitalario',
				name : 'circuloHospitalario',
				fieldLabel : 'C&iacute;rculo hospitalario',
				store : storeCirculoHospitalario,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...'
			});

			// 9 cobertura vacunas
			comboCoberturaVacunas = Ext.create('Ext.form.ComboBox2', {
				id : 'comboCoberturaVacunas',
				name : 'coberturaVacunas',
				fieldLabel : 'Cobertura de vacunas',
				store : storeCoberturaVacunas,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250,
				value : inputCdtipsit == 'SN' ? 'N' : '',
				hidden : inputCdtipsit == 'SN'
			});

			// 10 enfermedades adultos
			comboCoberturaPrevencionEnfermedadesAdultos = Ext
					.create(
							'Ext.form.ComboBox2',
							{
								id : 'comboCoberturaPrevencionEnfermedadesAdultos',
								name : 'coberturaPrevencionEnfermedadesAdultos',
								fieldLabel : 'Cobertura de prevenci&oacute;n de enfermedades en adultos',
								store : storeCoberturaPrevencionEnfermedadesAdultos,
								queryMode : 'local',
								displayField : 'value',
								valueField : 'key',
								allowBlank : false,
								editable : false,
								emptyText : 'Seleccione...',
								labelWidth : 250,
								value : inputCdtipsit == 'SN' ? 'N' : '',
								hidden : inputCdtipsit == 'SN'
							});

			// 11 maternidad
			comboMaternidad = Ext.create('Ext.form.ComboBox2', {
				id : 'comboMaternidad',
				name : 'maternidad',
				fieldLabel : 'Maternidad',
				store : storeMaternidad,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250,
				value : 'S',
				hidden : true
			});

			// 12 suma maternidad
			comboSumaAseguradaMaternidad = Ext.create('Ext.form.ComboBox2', {
				id : 'comboSumaAseguradaMaternidad',
				name : 'sumaAseguradaMaternidad',
				fieldLabel : 'Suma asegurada maternidad',
				store : storeSumaAseguradaMaternidad,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250,
				value : 0,
				hidden : true
			});

			// 13 tabulador reembolso
			comboBaseTabuladorReembolso = Ext.create('Ext.form.ComboBox2', {
				id : 'comboBaseTabuladorReembolso',
				name : 'baseTabuladorReembolso',
				fieldLabel : 'Base de tabulador de reembolso',
				store : storeBaseTabuladorReembolso,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250,
				hidden : true,
				value : "21000"
			});

			// 14 emergencia extranjero
			comboCostoEmergenciaExtranjero = Ext.create('Ext.form.ComboBox2', {
				id : 'comboCostoEmergenciaExtranjero',
				name : 'costoEmergenciaExtranjero',
				fieldLabel : 'Costo de emergencia en el extranjero',
				store : storeCostoEmergenciaExtranjero,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250
			});

			// 15 combo cobertura eliminacion penalizacion cambio zona
			comboCobElimPenCambioZona = Ext.create('Ext.form.ComboBox2', {
				id : 'comboCobEiPenCamZona',
				name : 'coberturaEliminacionPenalizacionCambioZona',
				fieldLabel : 'Cobertura de elim. de pen. de cambio de zona',
				store : storeCobElimPenCambioZona,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false,
				emptyText : 'Seleccione...',
				labelWidth : 250,
				value : inputCdtipsit == 'SN' ? 'N' : '',
				hidden : inputCdtipsit == 'SN'
			});

			// 16 rol (GRID)
			comboRoles = Ext.create('Ext.form.ComboBox', {
				id : 'comboRoles',
				store : storeRoles,
				queryMode : 'local',
				displayField : 'value',
				valueField : 'key',
				allowBlank : false,
				editable : false
			});
			// /////////////////////////////////////////////////////////////////
			// //// Fin de combos de formulario y combos editores de grid //////
			// /////////////////////////////////////////////////////////////////

			// ///////////////////
			// //// Botones //////
			/* ///////////////// */
			botonVerCoberturas = Ext.create('Ext.Button', {
				text : 'Ver coberturas',
				icon : contexto + '/resources/fam3icons/icons/table.png',
				disabled : true,
				handler : function() {
					storeCoberturas
							.load({
								params : {
									jsonCober_unieco : selected_record
											.get('cdUnieco'),
									jsonCober_estado : selected_record
											.get('estado'),
									jsonCober_nmpoiza : selected_record
											.get('nmPoliza'),
									jsonCober_cdplan : selected_cd_plan,
									jsonCober_cdramo : selected_record
											.get('cdRamo'),
									jsonCober_cdcia : selected_record
											.get('cdCiaaseg'),
									jsonCober_situa : selected_nm_plan
								}
							});
					gridCoberturas.setTitle('Plan ' + selected_ds_plan);
					botonVerDetalleCobertura.setDisabled(true);
					windowCoberturas.show();
				}
			});
			botonVerDetalleCobertura = Ext.create('Ext.Button', {
				text : 'Ver detalle de coberturas',
				disabled : true,
				handler : detallesCobertura
			});
			botonDetalle = Ext
					.create(
							'Ext.Button',
							{
								text : 'Detalles',
								icon : contexto
										+ '/resources/fam3icons/icons/text_list_numbers.png',
								disabled : true,
								handler : function() {
									Ext.Ajax
											.request({
												url : urlDetalleCotizacion,
												params : {
													'panel1.pv_cdunieco_i' : selected_record
															.get('cdUnieco'),
													'panel1.pv_cdramo_i' : selected_record
															.get('cdRamo'),
													'panel1.pv_estado_i' : 'W',
													'panel1.pv_nmpoliza_i' : selected_record
															.get('nmPoliza'),
													'panel1.pv_cdplan_i' : selected_cd_plan,
													'panel1.pv_cdperpag_i' : selected_record
															.get('cdPerpag')
												},
												success : function(response,
														opts) {
													var json = Ext
															.decode(response.responseText);
													if (json.success == true) {
														var orden = 0;
														var parentescoAnterior = 'qwerty';
														for ( var i = 0; i < json.slist1.length; i++) {
															if (json.slist1[i].parentesco != parentescoAnterior) {
																orden++;
																parentescoAnterior = json.slist1[i].parentesco;
															}
															json.slist1[i].orden_parentesco = orden
																	+ '_'
																	+ json.slist1[i].parentesco;
														}
														// console.log(json.slist1);
														Ext
																.create(
																		'Ext.window.Window',
																		{
																			title : 'Detalles de cotizaci&oacute;n',
																			maxHeight : 500,
																			width : 700,
																			autoScroll : true,
																			// layout:
																			// 'fit',
																			modal : true,
																			items : [ // Let's
																						// put
																						// an
																						// empty
																						// grid
																						// in
																						// just
																						// to
																						// illustrate
																						// fit
																						// layout
																					Ext
																							.create(
																									'Ext.grid.Panel',
																									{
																										store : Ext
																												.create(
																														'Ext.data.Store',
																														{
																															model : 'ModeloDetalleCotizacion',
																															groupField : 'orden_parentesco',
																															sorters : [ {
																																sorterFn : function(
																																		o1,
																																		o2) {
																																	if (o1
																																			.get('orden') === o2
																																			.get('orden')) {
																																		return 0;
																																	}
																																	return o1
																																			.get('orden') < o2
																																			.get('orden') ? -1
																																			: 1;
																																}
																															} ],
																															proxy : {
																																type : 'memory',
																																reader : 'json'
																															},
																															data : json.slist1
																														}),
																										columns : [
																												{
																													header : 'Nombre de la cobertura',
																													dataIndex : 'Nombre_garantia',
																													flex : 1,
																													summaryType : 'count',
																													summaryRenderer : function(
																															value) {
																														return Ext.String
																																.format(
																																		'Total de {0} cobertura{1}',
																																		value,
																																		value !== 1 ? 's'
																																				: '');
																													}
																												},
																												{
																													header : 'Importe por cobertura',
																													dataIndex : 'Importe',
																													flex : 1,
																													renderer : Ext.util.Format.usMoney,
																													align : 'right',
																													summaryType : 'sum'
																												} ],
																										features : [ {
																											groupHeaderTpl : [
																													'{name:this.formatName}',
																													{
																														formatName : function(
																																name) {
																															return name
																																	.split("_")[1];
																														}
																													} ],
																											ftype : 'groupingsummary',
																											startCollapsed : true
																										} ]
																									}),
																					Ext
																							.create(
																									'Ext.toolbar.Toolbar',
																									{
																										buttonAlign : 'right',
																										items : [
																												'->',
																												Ext
																														.create(
																																'Ext.form.Label',
																																{
																																	style : 'color:white;',
																																	initComponent : function() {
																																		var sum = 0;
																																		for ( var i = 0; i < json.slist1.length; i++) {
																																			sum += parseFloat(json.slist1[i].Importe);
																																		}
																																		this
																																				.setText('Total: '
																																						+ Ext.util.Format
																																								.usMoney(sum));
																																		this
																																				.callParent();
																																	}
																																}) ]
																									}) ]
																		})
																.show();
													} else {
														Ext.Msg
																.show({
																	title : 'Error',
																	msg : 'Error al obtener los detalles',
																	buttons : Ext.Msg.OK,
																	icon : Ext.Msg.ERROR
																});
													}
												},
												failure : function() {
													Ext.Msg
															.show({
																title : 'Error',
																msg : 'Error de comunicaci&oacute;n',
																buttons : Ext.Msg.OK,
																icon : Ext.Msg.ERROR
															});
												}
											});
								}
							});
			botonComprar = Ext
					.create(
							'Ext.Button',
							{
								text : hayTramiteCargado ? 'Complementar tr&aacute;mite '
										+ ntramiteCargado
										: 'Generar tr&aacute;mite',
								icon : contexto
										+ '/resources/fam3icons/icons/book_next.png',
								disabled : true,
								hidden : cotizacionUserSoloCotiza,
								handler : function() {
									debug("ahora si comprar");
									debug("trigger:");
									debug("nmpoliza", selected_record
											.get('nmPoliza'));
									debug("cdperpag", selected_record
											.get('cdPerpag'));
									debug("cdplan", selected_cd_plan);
									formPanel.setLoading(true);
									// this.up().up().destroy();
									debug(Ext.getCmp('fechaInicioVigencia')
											.getValue());
									debug(Ext.getCmp('fechaFinVigencia')
											.getValue());
									var nombreTitular = '';
									storeIncisos
											.each(function(record) {
												if (record.get('rol') == 'T'
														|| (typeof record
																.get('rol') == 'object' && record
																.get('rol')
																.get('key') == 'T')) {
													if (record.get('nombre')
															&& record
																	.get('nombre').length > 0) {
														nombreTitular = record
																.get('nombre')
																+ ' '
																+ (record
																		.get('segundoNombre')
																		&& record
																				.get('segundoNombre').length > 0 ? (record
																		.get('segundoNombre') + ' ')
																		: '')
																+ record
																		.get('apellidoPaterno')
																+ ' '
																+ record
																		.get('apellidoMaterno');
														debug(nombreTitular);
													}
												}
											});
									Ext.Ajax
											.request({
												url : urlComprarCotizacion,
												params : {
													comprarNmpoliza : selected_record
															.get('nmPoliza'),
													comprarCdplan : selected_cd_plan,
													comprarCdperpag : selected_record
															.get('cdPerpag'),
													comprarCdramo : selected_record
															.get('cdRamo'),
													comprarCdciaaguradora : selected_record
															.get('cdCiaaseg'),
													comprarCdunieco : selected_record
															.get('cdUnieco'),
													cdtipsit : inputCdtipsit,
													'smap1.fechaInicio' : Ext.Date
															.format(
																	Ext
																			.getCmp(
																					'fechaInicioVigencia')
																			.getValue(),
																	'd/m/Y'),
													'smap1.fechaFin' : Ext.Date
															.format(
																	Ext
																			.getCmp(
																					'fechaFinVigencia')
																			.getValue(),
																	'd/m/Y'),
													'smap1.nombreTitular' : nombreTitular,
													'smap1.ntramite' : Ext
															.getCmp('ntramite')
															.getValue()
												},
												success : function(response,
														opts) {
													formPanel.setLoading(false);
													var json = Ext
															.decode(response.responseText);
													if (json.success == true) {
														// Ext.MessageBox.hide();
														// matar botones
														botonDetalle.hide();
														botonComprar.hide();
														Ext
																.getCmp(
																		'botonImprimir')
																.hide();
														Ext
																.getCmp(
																		'botonEmail')
																.hide();
														botonVerCoberturas
																.hide();
														botonVerDetalleCobertura
																.hide();
														Ext.getCmp(
																'botonCotizar')
																.hide();
														Ext.getCmp(
																'botonLimpiar')
																.hide();
														Ext
																.getCmp(
																		'botonEditarCotiza')
																.hide();
														// Ext.getCmp('botonClonarCotiza').hide();
														// Ext.getCmp('botonNuevaCotiza').hide();
														Ext
																.getCmp(
																		'botonImprimir')
																.hide();
														Ext
																.getCmp(
																		'botonEmail')
																.hide();
														// !matar botones
														var jsonResp = Ext
																.decode(response.responseText);
														// window.console&&console.log(jsonResp);
														window.parent.scrollTo(
																0, 0);
														//
														debug("mostrar documentos");
														var ntramite = json.comprarNmpoliza;
														debug("ntramite",
																ntramite);
														if (!hayTramiteCargado) {
															Ext
																	.create(
																			'Ext.window.Window',
																			{
																				width : 600,
																				height : 400,
																				title : 'Subir documentos de tu tr&aacute;mite',
																				closable : false,
																				modal : true,
																				buttonAlign : 'center',
																				loadingMask : true,
																				loader : {
																					url : urlVentanaDocumentos,
																					scripts : true,
																					autoLoad : true,
																					params : {
																						'smap1.cdunieco' : inputCdunieco,
																						'smap1.cdramo' : inputCdramo,
																						'smap1.estado' : 'W',
																						'smap1.nmpoliza' : Ext
																								.getCmp(
																										'idCotizacion')
																								.getValue(),
																						'smap1.nmsuplem' : '0',
																						'smap1.ntramite' : ntramite,
																						'smap1.tipomov' : '0'
																					}
																				},
																				buttons : [ {
																					text : 'Aceptar',
																					icon : contexto
																							+ '/resources/fam3icons/icons/accept.png',
																					handler : function() {
																						this
																								.up()
																								.up()
																								.destroy();
																					}
																				} ]
																			})
																	.show();
															//
															var msg = Ext.Msg
																	.show({
																		title : 'Solicitud enviada',
																		msg : 'Su solicitud ha sido enviada a mesa de control con el n&uacute;mero de tr&aacute;mite '
																				+ json.comprarNmpoliza
																				+ ', ahora puede subir los documentos del trámite',
																		buttons : Ext.Msg.OK
																		// ,x:100
																		,
																		y : 50
																	});
															msg.setY(50);
														} else {
															var msg = Ext.Msg
																	.show({
																		title : 'Tr&aacute;mite actualizado',
																		msg : 'La cotizaci&oacute;n se guard&oacute; para el tr&aacute;mite '
																				+ ntramiteCargado
																				+ '<br/>y no podr&aacute; ser modificada posteriormente',
																		buttons : Ext.Msg.OK
																		// ,x:100
																		,
																		y : 50,
																		fn : function() {
																			Ext
																					.create(
																							'Ext.form.Panel')
																					.submit(
																							{
																								url : urlDatosComplementarios,
																								standardSubmit : true,
																								params : {
																									cdunieco : inputCdunieco,
																									cdramo : inputCdramo,
																									estado : 'W',
																									nmpoliza : Ext
																											.getCmp(
																													'idCotizacion')
																											.getValue(),
																									'map1.ntramite' : ntramiteCargado,
																									cdtipsit : inputCdtipsit
																								}
																							});
																		}
																	});
															msg.setY(50);
														}
													} else {
														Ext.Msg
																.show({
																	title : 'Error',
																	msg : 'Error al comprar la cotizaci&oacute;n '
																			+ opts.params.comprarNmpoliza,
																	buttons : Ext.Msg.OK,
																	icon : Ext.Msg.ERROR
																});
													}
												},
												failure : function() {
													// Ext.MessageBox.hide();
													formPanel.setLoading(false);
													// window.console&&console.log("error");
													Ext.Msg
															.show({
																title : 'Error',
																msg : 'Error de comunicaci&oacute;n',
																buttons : Ext.Msg.OK,
																icon : Ext.Msg.ERROR
															});
												}
											});
								}
							});
			/* ///////////////// */
			// //// Botones //////
			// ///////////////////
			// /////////////////////////////
			// //// window coberturas //////
			/* /////////////////////////// */
			gridCoberturas = Ext
					.create(
							'Ext.grid.Panel',
							{
								title : 'Sin plan',
								store : storeCoberturas,
								height : 300,
								selType : 'cellmodel',
								bbar : new Ext.PagingToolbar(
										{
											pageSize : 15,
											store : storeCoberturas,
											displayInfo : true,
											displayMsg : 'Registros mostrados {0} - {1} de {2}',
											emptyMsg : 'No hay registros para mostrar',
											beforePageText : 'P&aacute;gina',
											afterPageText : 'de {0}'
										}),
								buttons : [ botonVerDetalleCobertura ],
								columns : [ {
									dataIndex : 'dsGarant',
									text : 'Cobertura',
									flex : 1
								}, {
									dataIndex : 'sumaAsegurada',
									text : 'Suma asegurada',
									flex : 1
								}, {
									dataIndex : 'deducible',
									text : 'Deducible',
									flex : 1
								} /*
									 * , { dataIndex:'cdGarant', hidden:true }, {
									 * dataIndex:'cdCiaaseg', hidden:true }, {
									 * dataIndex:'cdRamo', hidden:true }
									 */
								],
								listeners : {
									itemclick : function(dv, record, item,
											index, e) {
										var y = this.getSelectionModel()
												.getCurrentPosition().row;
										var x = this.getSelectionModel()
												.getCurrentPosition().column;
										if (x == 0) {
											selected_cobertura = record;
											botonVerDetalleCobertura
													.setDisabled(false);
										} else {
											botonVerDetalleCobertura
													.setDisabled(true);
										}
									}
								}
							});

			coberturasFormPanel = new Ext.form.FormPanel({
				id : 'coberturasFormPanel',
				url : 'flujocotizacion/obtenerAyudaCobertura.action',
				border : false,
				layout : 'form',
				items : gridCoberturas
			});

			windowCoberturas = new Ext.Window({
				plain : true,
				id : 'windowCoberturas',
				width : 500,
				height : 400,
				modal : true,
				autoScroll : true,
				title : 'Coberturas',
				layout : 'fit',
				bodyStyle : 'padding:5px;',
				buttonAlign : 'center',
				closeAction : 'hide',
				closable : true,
				items : coberturasFormPanel,
				buttons : [ {
					text : 'Regresar',
					handler : function() {
						windowCoberturas.hide();
					}
				} ]
			});
			/* /////////////////////////// */
			// //// window coberturas //////
			// /////////////////////////////
			// ///////////////////////////////////
			// //// window ayuda coberturas //////
			/* ///////////////////////////////// */
			windowAyudaCobertura = new Ext.Window({
				title : 'Ayuda Coberturas',
				width : 450,
				height : 350,
				bodyStyle : 'background:white',
				overflow : 'auto',
				modal : true,
				autoScroll : true,
				buttonAlign : 'center',
				closable : false,
				buttons : [/*
							 * { text: 'Imprimir', handler: function() { // ***
							 * Cambiamos los nombres de los id's para que al
							 * IMPRIMIR se muestre solo lo que queremos if
							 * (!Ext.isEmpty(document.getElementById("impresionAyudaInvisible"))) {
							 * document.getElementById("impresionAyudaInvisible").id =
							 * "impresionAyuda"; } if
							 * (!Ext.isEmpty(document.getElementById("impresionResultadosCotizacion"))) {
							 * document.getElementById("impresionResultadosCotizacion").id =
							 * "impresionResultadosCotizacionInvisible"; }
							 * 
							 * document.getElementById("impresionAyuda").innerHTML = '<table
							 * width=430 ><tr><td align=left bgcolor="#98012e" style="color:white;font-size:12px;"><b>Ayuda
							 * Coberturas</b></td></tr><tr><td style="font-size:12px; ">' +
							 * ayudaVoL + '</td></tr></table>';
							 * 
							 * var spans =
							 * document.getElementById("impresionAyuda").getElementsByTagName("span");
							 * for (var i = 0; i < spans.length; i++) {
							 * spans[i].style.fontSize = "";
							 * spans[i].style.lineHeight = "";
							 * spans[i].style.textAlign = ""; } window.print();
							 * 
							 *  // *** Esconder elemento que ya se imprimi� para
							 * que no se muestre en la pantalla de cotizacion
							 * esconderElemento("impresionAyuda"); } },
							 */{
					text : 'Cerrar',
					handler : function() {
						windowAyudaCobertura.hide();
					}
				} ]
			});
			/* ///////////////////////////////// */
			// //// window ayuda coberturas //////
			// ///////////////////////////////////
			// ///////////////////////////
			// //// grid resultados //////
			/* ///////////////////////// */
			Ext
					.define(
							'GridResultados',
							{
								extend : 'Ext.grid.Panel',
								/*
								 * xtype: 'grouped-grid', requires: [
								 * 'Ext.grid.feature.Grouping' ], collapsible:
								 * true, features: [{ ftype: 'grouping',
								 * groupHeaderTpl: 'Aseguradora: {name}',
								 * hideGroupedHeader: true, startCollapsed:
								 * true, id: 'restaurantGrouping' }],
								 */
								renderTo : 'divResultados',
								hidden : true,
								frame : true,
								store : storeResultados,
								height : 300,
								width : 800,
								title : 'Resultados',
								selType : 'cellmodel',
								bbar : new Ext.PagingToolbar(
										{
											pageSize : 15,
											store : storeResultados,
											displayInfo : true,
											displayMsg : 'Registros mostrados {0} - {1} de {2}',
											emptyMsg : 'No hay registros para mostrar',
											beforePageText : 'P&aacute;gina',
											afterPageText : 'de {0}'
										}),
								buttonAlign : 'center',
								buttons : [
										botonComprar,
										botonDetalle,
										botonVerCoberturas,
										{
											id : 'botonEditarCotiza',
											text : 'Editar',
											icon : contexto
													+ '/resources/fam3icons/icons/pencil.png',
											handler : function() {
												bloquearFormulario(false);
												gridResultados.hide();
												window.parent.scrollTo(0, 0);
												botonVerCoberturas
														.setDisabled(true);
												botonDetalle.setDisabled(true);
												botonComprar.setDisabled(true);
												Ext.getCmp('botonImprimir')
														.setDisabled(true);
												Ext.getCmp('botonEmail')
														.setDisabled(true);
												window.parent.scrollTo(0, 0);
												campoCodigoPostal.focus();
											}
										},
										{
											id : 'botonClonarCotiza',
											text : 'Clonar',
											icon : contexto
													+ '/resources/fam3icons/icons/control_repeat_blue.png',
											handler : function() {
												bloquearFormulario(false);
												gridResultados.hide();
												botonVerCoberturas
														.setDisabled(true);
												botonDetalle.setDisabled(true);
												botonComprar.setDisabled(true);
												Ext.getCmp('botonImprimir')
														.setDisabled(true);
												Ext.getCmp('botonEmail')
														.setDisabled(true);
												window.parent.scrollTo(0, 0);
												Ext.getCmp('idCotizacion')
														.setValue('');
												campoCodigoPostal.focus();
												// desbloquear botones
												if (!cotizacionUserSoloCotiza)
													botonComprar.show();
												Ext.getCmp('botonImprimir')
														.show();
												Ext.getCmp('botonEmail').show();
												botonDetalle.show();
												botonVerCoberturas.show();
												botonVerDetalleCobertura.show();
												Ext.getCmp('botonCotizar')
														.show();
												Ext.getCmp('botonLimpiar')
														.show();
												Ext.getCmp('botonEditarCotiza')
														.show();
												Ext.getCmp('botonClonarCotiza')
														.show();
												Ext.getCmp('botonImprimir')
														.show();
												Ext.getCmp('botonEmail').show();
											}
										},
										{
											id : 'botonNuevaCotiza',
											icon : contexto
													+ '/resources/fam3icons/icons/arrow_refresh.png',
											text : 'Nueva',
											handler : function() {
												gridIncisos.hayTitular = false;
												gridIncisos.hayConyugue = false;
												bloquearFormulario(false);
												formPanel.getForm().reset();
												gridResultados.hide();
												window.parent.scrollTo(0, 0);
												gridIncisos.getSelectionModel()
														.deselectAll();
												storeIncisos
														.remove(storeIncisos
																.getRange());
												debug("gridIncisos.getSelectionModel().deselectAll();storeIncisos.remove(storeIncisos.getRange());");
												// storeIncisos.sync();
												botonVerCoberturas
														.setDisabled(true);
												botonDetalle.setDisabled(true);
												botonComprar.setDisabled(true);
												Ext.getCmp('botonImprimir')
														.setDisabled(true);
												Ext.getCmp('botonEmail')
														.setDisabled(true);
												// desbloquear botones
												if (!cotizacionUserSoloCotiza)
													botonComprar.show();
												Ext.getCmp('botonImprimir')
														.show();
												Ext.getCmp('botonEmail').show();
												botonDetalle.show();
												botonVerCoberturas.show();
												botonVerDetalleCobertura.show();
												Ext.getCmp('botonCotizar')
														.show();
												Ext.getCmp('botonLimpiar')
														.show();
												Ext.getCmp('botonEditarCotiza')
														.show();
												Ext.getCmp('botonClonarCotiza')
														.show();
												// Ext.getCmp('botonNuevaCotiza').show();
												Ext.getCmp('botonImprimir')
														.show();
												Ext.getCmp('botonEmail').show();
												campoCodigoPostal.focus();
											}
										},
										{
											id : 'botonImprimir',
											icon : contexto
													+ '/resources/fam3icons/icons/printer.png',
											text : 'Imprimir',
											disabled : true,
											handler : function() {
												var me = this;
												/*
												 * window.open(
												 * urlImprimirCotiza+'?p_cdplan='+selected_cd_plan
												 * +"&p_estado='W'"
												 * +'&p_poliza='+Ext.getCmp('idCotizacion').getValue()
												 * +'&p_ramo=2' +'&p_unieco=1'
												 * +'&destype=cache'
												 * +"&desformat=PDF"
												 * +"&userid="+repSrvUsr
												 * +"&ACCESSIBLE=YES"
												 * //parametro que habilita
												 * salida en PDF
												 * +"&report="+_NOMBRE_REPORTE_COTIZACION
												 * +"&paramform=no" ,'_blank'
												 * ,'width=800,height=600');
												 */
												var urlRequestImpCotiza = urlImprimirCotiza
														+ '?p_cdplan='
														+ selected_cd_plan
														+ '&p_estado=W'
														+ '&p_poliza='
														+ Ext.getCmp(
																'idCotizacion')
																.getValue()
														+ '&p_unieco='
														+ inputCdunieco
														+ '&p_ramo='
														+ inputCdramo
														+ '&p_cdusuari='
														+ sesionUsuarioUser
														+ '&p_ntramite='
														+ ntramiteCargado
														+ '&destype=cache'
														+ "&desformat=PDF"
														+ "&userid="
														+ repSrvUsr
														+ "&ACCESSIBLE=YES" // parametro
																			// que
																			// habilita
																			// salida
																			// en
																			// PDF
														+ "&report="
														+ _NOMBRE_REPORTE_COTIZACION
														+ "&paramform=no";
												debug(urlRequestImpCotiza);
												var numRand = Math
														.floor((Math.random() * 100000) + 1);
												debug(numRand);
												var windowVerDocu = Ext
														.create(
																'Ext.window.Window',
																{
																	title : 'Cotizaci&oacute;n',
																	width : 700,
																	height : 500,
																	collapsible : true,
																	titleCollapse : true,
																	html : '<iframe innerframe="'
																			+ numRand
																			+ '" frameborder="0" width="100" height="100"'
																			+ 'src="'
																			+ panDocUrlViewDoc
																			+ "?contentType=application/pdf&url="
																			+ encodeURIComponent(urlRequestImpCotiza)
																			+ "\">"
																			+ '</iframe>',
																	listeners : {
																		resize : function(
																				win,
																				width,
																				height,
																				opt) {
																			debug(
																					width,
																					height);
																			$(
																					'[innerframe="'
																							+ numRand
																							+ '"]')
																					.attr(
																							{
																								'width' : width - 20,
																								'height' : height - 60
																							});
																		}
																	}
																}).show();
												windowVerDocu.center();
											}
										},
										{
											id : 'botonEmail',
											disabled : true,
											text : 'Enviar',
											icon : contexto
													+ '/resources/fam3icons/icons/email.png',
											handler : function() {
												Ext
														.create(
																'Ext.window.Window',
																{
																	title : 'Enviar cotizaci&oacute;n',
																	width : 400,
																	modal : true,
																	height : 150,
																	buttonAlign : 'center',
																	bodyPadding : 5,
																	items : [ {
																		xtype : 'textfield',
																		id : 'idInputCorreos',
																		fieldLabel : 'Correo(s)',
																		emptyText : 'Correo(s) separados por ;',
																		allowBlank : false,
																		blankText : 'Introducir correo(s) separados por ;',
																		width : 350
																	} ],
																	buttons : [
																			{
																				text : 'Enviar',
																				icon : contexto
																						+ '/resources/fam3icons/icons/accept.png',
																				handler : function() {
																					var me = this;
																					if (Ext
																							.getCmp(
																									'idInputCorreos')
																							.getValue().length > 0
																							&& Ext
																									.getCmp(
																											'idInputCorreos')
																									.getValue() != 'Correo(s) separados por ;') {
																						debug('Se va a enviar cotizacion');
																						me
																								.up()
																								.up()
																								.setLoading(
																										true);
																						Ext.Ajax
																								.request({
																									url : urlEnviarCorreo,
																									params : {
																										to : Ext.getCmp('idInputCorreos').getValue(),
																										urlArchivo : urlImprimirCotiza
																												+ '?p_cdplan='   + selected_cd_plan
																												+ "&p_estado='W'"
																												+ '&p_poliza='   + Ext.getCmp('idCotizacion').getValue()
																												+ '&p_unieco='   + inputCdunieco
																												+ '&p_ramo='     + inputCdramo
																												+ '&p_cdusuari=' + sesionUsuarioUser
																												+ '&p_ntramite=' + ntramiteCargado
																												+ '&destype=cache'
																												+ "&desformat=PDF"
																												+ "&userid="     + repSrvUsr
																												+ "&ACCESSIBLE=YES" // habilita salida en PDF
																												+ "&report="     + _NOMBRE_REPORTE_COTIZACION
																												+ "&paramform=no",
																										nombreArchivo : 'cotizacion_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf'
																									},
																									callback : function(
																											options,
																											success,
																											response) {
																										me
																												.up()
																												.up()
																												.setLoading(
																														false);
																										if (success) {
																											var json = Ext
																													.decode(response.responseText);
																											if (json.success == true) {
																												me
																														.up()
																														.up()
																														.destroy();
																												Ext.Msg
																														.show({
																															title : 'Correo enviado',
																															msg : 'El correo ha sido enviado',
																															buttons : Ext.Msg.OK
																														});
																											} else {
																												Ext.Msg
																														.show({
																															title : 'Error',
																															msg : 'Error al enviar',
																															buttons : Ext.Msg.OK,
																															icon : Ext.Msg.ERROR
																														});
																											}
																										} else {
																											Ext.Msg
																													.show({
																														title : 'Error',
																														msg : 'Error de comunicaci&oacute;n',
																														buttons : Ext.Msg.OK,
																														icon : Ext.Msg.ERROR
																													});
																										}
																									}
																								});
																					} else {
																						Ext.Msg
																								.show({
																									title : 'Error',
																									msg : 'Introduzca al menos un correo',
																									buttons : Ext.Msg.OK,
																									icon : Ext.Msg.WARNING
																								});
																					}
																				}
																			},
																			{
																				text : 'Cancelar',
																				icon : contexto
																						+ '/resources/fam3icons/icons/cancel.png',
																				handler : function() {
																					this
																							.up()
																							.up()
																							.destroy();
																				}
																			} ]
																}).show();
												Ext.getCmp('idInputCorreos')
														.focus();
											}
										} ],
								listeners : {
									itemclick : function(dv, record, item,
											index, e) {
										var y = this.getSelectionModel()
												.getCurrentPosition().row;
										var x = this.getSelectionModel()
												.getCurrentPosition().column;
										if (x > 0) {
											var pos = '';
											if (x == 1) {
												pos = 'Plus100';
											} else if (x == 2) {
												pos = 'Plus500';
											} else {
												pos = 'Plus1000';
											}

											// //// parche para cuando el
											// producto es SN //////
											if (inputCdtipsit == 'SN') {
												pos = 'Plus1000';
											}
											// ////////////////////////////////////////////////

											selected_prima = record.get(pos);
											selected_cd_plan = record.get('CD'
													+ pos);
											selected_ds_plan = record.get('DS'
													+ pos);
											selected_nm_plan = record.get('NM'
													+ pos);
											selected_record = record;
											debug(
													'debug(selected_prima,selected_cd_plan,selected_ds_plan);',
													selected_prima,
													selected_cd_plan,
													selected_ds_plan);
											botonVerCoberturas
													.setDisabled(false);
											botonComprar.setDisabled(false);
											Ext.getCmp('botonImprimir')
													.setDisabled(false);
											Ext.getCmp('botonEmail')
													.setDisabled(false);
											botonDetalle.setDisabled(false);
										} else {
											botonVerCoberturas
													.setDisabled(true);
											botonComprar.setDisabled(true);
											Ext.getCmp('botonImprimir')
													.setDisabled(true);
											Ext.getCmp('botonEmail')
													.setDisabled(true);
											botonDetalle.setDisabled(true);
										}
										// alert("idplan = " + idplan + "
										// desplan = " + desplan+ " nmplan="+
										// nmplan + " mnPrima=" + mnPrima);

									}
								},
								columns : [ {
									header : "cdIdentifica",
									dataIndex : 'cdIdentifica',
									sortable : true,
									id : 'cdIdentifica',
									hidden : true
								}, {
									header : "cdUnieco",
									dataIndex : 'cdUnieco',
									sortable : true,
									hidden : true
								}, {
									header : "cdCiaaseg",
									dataIndex : 'cdCiaaseg',
									sortable : true,
									hidden : true
								}, {
									header : "dsUnieco",
									dataIndex : 'dsUnieco',
									sortable : true,
									hidden : true
								}, {
									header : "cdPerpag",
									dataIndex : 'cdPerpag',
									sortable : true,
									hidden : true
								}, {
									header : "numeroSituacion",
									dataIndex : 'numeroSituacion',
									sortable : true,
									hidden : true
								}, {
									header : "cdRamo",
									dataIndex : 'cdRamo',
									sortable : true,
									hidden : true
								}, {
									header : "Descripci&oacute;n",
									dataIndex : 'dsPerpag',
									sortable : false,
									menuDisabled : true,
									flex : 1
								}, {
									header : "cdPlan",
									dataIndex : 'cdPlan',
									sortable : true,
									hidden : true
								}, {
									header : "dsPlan",
									dataIndex : 'dsPlan',
									sortable : false,
									hidden : true
								}, {
									header : "feEmisio",
									dataIndex : 'feEmisio',
									sortable : false,
									hidden : true
								}, {
									header : "feVencim",
									dataIndex : 'feVencim',
									sortable : false,
									hidden : true
								},
								/* generadas */
								{
									dataIndex : "Plus100",
									header : "Plus 100",
									hidden : inputCdtipsit == 'SN',
									id : "Plus100",
									sortable : false,
									flex : 1,
									renderer : Ext.util.Format.usMoney
								}, {
									dataIndex : "CDPlus100",
									header : "CDPlus100",
									hidden : true,
									id : "CDPlus100",
									sortable : false,
									width : 100
								}, {
									dataIndex : "DSPlus100",
									header : "DSPlus100",
									hidden : true,
									id : "DSPlus100",
									sortable : false,
									width : 100
								}, {
									dataIndex : "NMPlus100",
									header : "NMPlus100",
									hidden : true,
									id : "NMPlus100",
									sortable : false,
									width : 100
								}, {
									dataIndex : "Plus500",
									header : "Plus 500",
									hidden : inputCdtipsit == 'SN',
									id : "Plus500",
									sortable : false,
									flex : 1,
									renderer : Ext.util.Format.usMoney
								}, {
									dataIndex : "CDPlus500",
									header : "CDPlus500",
									hidden : true,
									id : "CDPlus500",
									sortable : false,
									width : 100
								}, {
									dataIndex : "DSPlus500",
									header : "DSPlus500",
									hidden : true,
									id : "DSPlus500",
									sortable : false,
									width : 100
								}, {
									dataIndex : "NMPlus500",
									header : "NMPlus500",
									hidden : true,
									id : "NMPlus500",
									sortable : false,
									width : 100
								}, {
									dataIndex : "Plus1000",
									header : "Plus 1000",
									hidden : false,
									id : "Plus1000",
									sortable : false,
									flex : 1,
									renderer : Ext.util.Format.usMoney
								}, {
									dataIndex : "CDPlus1000",
									header : "CDPlus1000",
									hidden : true,
									id : "CDPlus1000",
									sortable : false,
									width : 100
								}, {
									dataIndex : "DSPlus1000",
									header : "DSPlus1000",
									hidden : true,
									id : "DSPlus1000",
									sortable : false,
									width : 100
								}, {
									dataIndex : "NMPlus1000",
									header : "NMPlus1000",
									hidden : true,
									id : "NMPlus1000",
									sortable : false,
									width : 100
								} ]
							});
			gridResultados = new GridResultados();
			/* ///////////////////////// */
			// //// grid resultados //////
			// ///////////////////////////
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// //// Inicio de declaracion de grid //////
			// ////
			// http://docs.sencha.com/extjs/4.2.1/extjs-build/examples/build/KitchenSink/ext-theme-neptune/#cell-editing
			// //////
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Ext
					.define(
							'EditorIncisos',
							{
								extend : 'Ext.grid.Panel',

								requires : [ 'Ext.selection.CellModel',
										'Ext.grid.*', 'Ext.data.*',
										'Ext.util.*', 'Ext.form.*' ],
								xtype : 'cell-editing',

								title : 'Asegurados',
								frame : false,

								initComponent : function() {
									this.cellEditing = new Ext.grid.plugin.CellEditing(
											{
												clicksToEdit : 1
											});

									Ext
											.apply(
													this,
													{
														width : 750,
														height : 200,
														plugins : [ this.cellEditing ],
														store : storeIncisos,
														columns : [
																{
																	header : 'Tipo de asegurado',
																	dataIndex : 'rol',
																	flex : 2,
																	editor : comboRoles,
																	renderer : function(
																			v) {
																		var leyenda = '';
																		if (typeof v == 'string')
																		// tengo
																		// solo
																		// el
																		// indice
																		{
																			// window.console&&console.log('string:');
																			storeRoles
																					.each(function(
																							rec) {
																						// window.console&&console.log('iterando...');
																						if (rec.data.key == v) {
																							leyenda = rec.data.value;
																						}
																					});
																			// window.console&&console.log(leyenda);
																		} else
																		// tengo
																		// objeto
																		// que
																		// puede
																		// venir
																		// como
																		// Generic
																		// u
																		// otro
																		// mas
																		// complejo
																		{
																			// window.console&&console.log('object:');
																			if (v.key
																					&& v.value)
																			// objeto
																			// Generic
																			{
																				leyenda = v.value;
																			} else {
																				leyenda = v.data.value;
																			}
																			// window.console&&console.log(leyenda);
																		}
																		return leyenda;
																	}
																},
																{
																	header : 'Fecha de nacimiento',
																	dataIndex : 'fechaNacimiento',
																	flex : 2,
																	renderer : Ext.util.Format
																			.dateRenderer('d M Y'),
																	editor : {
																		xtype : 'datefield',
																		format : 'd/m/Y',
																		editable : true
																	}
																},
																{
																	header : 'Sexo',
																	dataIndex : 'sexo',
																	flex : 1,
																	editor : comboGeneros,
																	renderer : function(
																			v) {
																		var leyenda = '';
																		if (typeof v == 'string')
																		// tengo
																		// solo
																		// el
																		// indice
																		{
																			// window.console&&console.log('string:');
																			storeGeneros
																					.each(function(
																							rec) {
																						// window.console&&console.log('iterando...');
																						if (rec.data.key == v) {
																							leyenda = rec.data.value;
																						}
																					});
																			// window.console&&console.log(leyenda);
																		} else
																		// tengo
																		// objeto
																		// que
																		// puede
																		// venir
																		// como
																		// Generic
																		// u
																		// otro
																		// mas
																		// complejo
																		{
																			// window.console&&console.log('object:');
																			if (v.key
																					&& v.value)
																			// objeto
																			// Generic
																			{
																				leyenda = v.value;
																			} else {
																				leyenda = v.data.value;
																			}
																			// window.console&&console.log(leyenda);
																		}
																		return leyenda;
																	}
																},
																{
																	header : 'Nombre',
																	dataIndex : 'nombre',
																	flex : 1,
																	editor : {
																		// allowBlank:
																		// false
																		maxLength : 90
																	}
																},
																{
																	header : 'Segundo nombre',
																	dataIndex : 'segundoNombre',
																	flex : 2,
																	editor : {
																		// allowBlank:
																		// false
																		maxLength : 30
																	}
																},
																{
																	header : 'Apellido paterno',
																	dataIndex : 'apellidoPaterno',
																	flex : 2,
																	editor : {
																		// allowBlank:
																		// false
																		maxLength : 30
																	}
																},
																{
																	header : 'Apellido materno',
																	dataIndex : 'apellidoMaterno',
																	flex : 2,
																	editor : {
																		// allowBlank:
																		// false
																		maxLength : 30
																	}
																},
																{
																	xtype : 'actioncolumn',
																	width : 30,
																	sortable : false,
																	menuDisabled : true,
																	items : [ {
																		icon : contexto
																				+ '/resources/fam3icons/icons/delete.png',
																		// iconCls:
																		// 'icon-delete',
																		tooltip : 'Quitar inciso',
																		scope : this,
																		handler : this.onRemoveClick
																	} ]
																} ],
														selModel : {
															selType : 'cellmodel'
														},
														tbar : [ {
															icon : contexto
																	+ '/resources/fam3icons/icons/add.png',
															text : 'Agregar inciso',
															scope : this,
															handler : this.onAddClick
														} ],
														/* http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29 */
														// valida las celdas y
														// les pone el estilo
														// rojito
														listeners : {
															// add the
															// validation after
															// render so that
															// validation is not
															// triggered when
															// the record is
															// loaded.
															validateedit : function(
																	editor, e) {
																if (e.colIdx == 0) {
																	var nuevo = e.value;
																	var anterior = e.record.data[e.field];
																	if ((typeof anterior) == "object") {
																		anterior = anterior
																				.get('key');
																	}
																	// console.log(anterior,nuevo);
																	if (anterior != nuevo) {
																		if (nuevo == 'T'
																				|| nuevo == 'C') {
																			// console.log('hay
																			// que
																			// revisar');
																			// console.log(gridIncisos.hayTitular);
																			// console.log(gridIncisos.hayConyugue);
																			if (nuevo == 'T'
																					&& gridIncisos.hayTitular == true)// titular
																														// repetido
																			{
																				e.cancel = true;
																				Ext.Msg
																						.show({
																							title : 'Error',
																							msg : 'Ya existe un titular',
																							buttons : Ext.Msg.OK,
																							icon : Ext.Msg.WARNING
																						});
																			} else if (nuevo == 'C'
																					&& gridIncisos.hayConyugue == true)// conyugue
																														// repetido
																			{
																				e.cancel = true;
																				Ext.Msg
																						.show({
																							title : 'Error',
																							msg : 'Ya existe un c&oacute;nyugue',
																							buttons : Ext.Msg.OK,
																							icon : Ext.Msg.WARNING
																						});
																			} else// se
																					// acepta
																					// el
																					// cambio
																			{
																				// bloquear
																				// titular
																				// o
																				// conyugue
																				if (nuevo == 'T') {
																					// console.log('titular
																					// bloqueado');
																					gridIncisos.hayTitular = true;
																				} else if (nuevo == 'C') {
																					// console.log('conyugue
																					// bloqueado');
																					gridIncisos.hayConyugue = true;
																				}

																				// desbloquear
																				// titular
																				// o
																				// conyugue
																				if (anterior == 'T') {
																					// console.log('titular
																					// desbloqueado');
																					gridIncisos.hayTitular = false;
																				} else if (anterior == 'C') {
																					// console.log('conyugue
																					// desbloqueado');
																					gridIncisos.hayConyugue = false;
																				}
																			}
																		} else {
																			// bloquear
																			// titular
																			// o
																			// conyugue
																			if (nuevo == 'T') {
																				// console.log('titular
																				// bloqueado');
																				gridIncisos.hayTitular = true;
																			} else if (nuevo == 'C') {
																				// console.log('conyugue
																				// bloqueado');
																				gridIncisos.hayConyugue = true;
																			}

																			// desbloquear
																			// titular
																			// o
																			// conyugue
																			if (anterior == 'T') {
																				// console.log('titular
																				// desbloqueado');
																				gridIncisos.hayTitular = false;
																			} else if (anterior == 'C') {
																				// console.log('conyugue
																				// desbloqueado');
																				gridIncisos.hayConyugue = false;
																			}
																		}
																	}
																}
															},
															afterrender : function(
																	grid) {
																var view = grid
																		.getView();
																// validation on
																// record level
																// through
																// "itemupdate"
																// event
																view
																		.on(
																				'itemupdate',
																				function(
																						record,
																						y,
																						node,
																						options) {
																					this
																							.validateRow(
																									this
																											.getColumnIndexes(),
																									record,
																									y,
																									true);
																				},
																				grid);
															}
														}
													/* http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29 */

													});

									this.callParent();

									/*
									 * this.on('afterlayout', this.loadStore,
									 * this, { delay: 1, single: true })
									 */
								},

								/*
								 * loadStore: function() {
								 * this.getStore().load({ // store loading is
								 * asynchronous, use a load listener or callback
								 * to handle results callback: this.onStoreLoad
								 * }); },
								 * 
								 * onStoreLoad: function(){ Ext.Msg.show({
								 * title: 'Store Load Callback', msg: 'store was
								 * loaded, data available for processing', icon:
								 * Ext.Msg.INFO, buttons: Ext.Msg.OK }); },
								 */

								/* http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29 */
								// regresa las columnas con editor que tengan
								// allowBlank=false (requeridas)
								getColumnIndexes : function() {
									var me, columnIndexes;
									me = this;
									columnIndexes = [];
									Ext.Array
											.each(
													me.columns,
													function(column) {
														// only validate column
														// with editor
														if (column.getEditor
																&& Ext
																		.isDefined(column
																				.getEditor())
																&& column
																		.getEditor().allowBlank == false) {
															columnIndexes
																	.push(column.dataIndex);
														} else {
															columnIndexes
																	.push(undefined);
														}
													});
									return columnIndexes;
								},
								validateRow : function(columnIndexes, record, y)
								// hace que una celda de columna con
								// allowblank=false tenga el estilo rojito
								{
									var view = this.getView();
									Ext
											.each(
													columnIndexes,
													function(columnIndex, x) {
														if (columnIndex) {
															var cell = view
																	.getCellByPosition({
																		row : y,
																		column : x
																	});
															cellValue = record
																	.get(columnIndex);
															if (cell.addCls
																	&& ((!cellValue) || (cellValue.lenght == 0))) {
																cell
																		.addCls("custom-x-form-invalid-field");
															}
														}
													});
									return false;
								}/* http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29 */,

								hayTitular : false,
								hayConyugue : false,

								onAddClick : function() {
									window.parent.scrollTo(0, 600);
									var rol;
									var indexToDelete = -1;

									if (gridIncisos.hayTitular == false)// si no
																		// hay
																		// titular,
																		// insertar
																		// titular
									{
										storeRoles
												.each(function(record, index) {
													if (record.get('key') == 'T') {
														rol = new Generic(
																{
																	key : record
																			.get('key'),
																	value : record
																			.get('value')
																});
													}
												});
										gridIncisos.hayTitular = true;
									} else if (gridIncisos.hayConyugue == false)// hay
																				// titular
																				// pero
																				// no
																				// hay
																				// conyugue,
																				// insertar
																				// conyuge
									{
										storeRoles
												.each(function(record, index) {
													if (record.get('key') == 'C') {
														rol = new Generic(
																{
																	key : record
																			.get('key'),
																	value : record
																			.get('value')
																});
													}
												});
										gridIncisos.hayConyugue = true;
									} else// insertar hijos
									{
										storeRoles
												.each(function(record, index) {
													if (record.get('key') == 'H') {
														rol = new Generic(
																{
																	key : record
																			.get('key'),
																	value : record
																			.get('value')
																});
													}
												});
									}

									// Create a model instance
									var rec = new IncisoSalud(
											{
												rol : rol,
												fechaNacimiento : new Date(),
												sexo : new Generic(
														{
															key : storeGeneros
																	.getAt(0).data.key,
															value : storeGeneros
																	.getAt(0).data.value
														}),
												nombre : '',
												segundoNombre : '',
												apellidoPaterno : '',
												apellidoMaterno : ''
											});

									this.getStore().add(rec);

									// para acomodarse en la primer celda para
									// editar
									this.cellEditing
											.startEditByPosition({
												row : storeIncisos.getRange().length - 1,
												column : 1
											});
								},

								onRemoveClick : function(grid, rowIndex) {
									var record = this.getStore()
											.getAt(rowIndex);
									if (record.get('rol') == 'T'
											|| (typeof record.get('rol') == 'object' && record
													.get('rol').get('key') == 'T')) {
										gridIncisos.hayTitular = false;
									} else if (record.get('rol') == 'C'
											|| (typeof record.get('rol') == 'object' && record
													.get('rol').get('key') == 'C')) {
										gridIncisos.hayConyugue = false;
									}
									this.getStore().removeAt(rowIndex);
								}
							});
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// //// Fin de declaracion de grid //////
			// ////
			// http://docs.sencha.com/extjs/4.2.1/extjs-build/examples/build/KitchenSink/ext-theme-neptune/#cell-editing
			// //////
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			gridIncisos = new EditorIncisos();

			// /////////////////////////////
			// //// Inicio formulario //////
			// /////////////////////////////
			formPanel = Ext
					.create(
							'Ext.form.Panel',
							{
								title : '<span style="font-size: 16px;">Cotizaci&oacute;n de Salud Vital</span>',
								overflowY : 'auto',
								overflowX : 'auto',
								bodyPadding : 10,
								frame : true,
								width : 800,
								buttonAlign : 'center',
								renderTo : 'maindiv',
								model : 'CotizacionSalud',
								items : [
										{
											xtype : 'textfield',
											name : 'cdunieco',
											value : inputCdunieco,
											hidden : true
										},
										{
											xtype : 'textfield',
											name : 'cdramo',
											value : inputCdramo,
											hidden : true
										},
										{
											xtype : 'textfield',
											name : 'cdtipsit',
											value : inputCdtipsit,
											hidden : true
										},
										{
											id : 'ntramite',
											xtype : 'textfield',
											fieldLabel : 'N&uacute;mero de tr&aacute;mite',
											readOnly : true,
											labelWidth : 250,
											name : 'ntramite',
											hidden : !hayTramiteCargado,
											value : ntramiteCargado
										},
										{// 0
											id : 'idCotizacion',
											name : 'id',
											xtype : 'numberfield',
											fieldLabel : 'N&uacute;mero de cotizaci&oacute;n',
											readOnly : true,
											labelWidth : 250
										},
										// sexo (inciso) 1
										// fecha nacimiento (inciso) 2
										campoCodigoPostal, // 3
										comboEstados, // 4
										comboMunicipios, // 17
										// comboCiudad, 4 X(
										comboDeducible, // 5
										comboCopago, // 6
										comboSumaAsegurada, // 7
										// comboCirculoHospitalario, //8
										comboCoberturaVacunas, // 9
										comboCoberturaPrevencionEnfermedadesAdultos, // 10
										comboMaternidad, // 11
										comboSumaAseguradaMaternidad, // 12
										comboBaseTabuladorReembolso, // 13
										comboCostoEmergenciaExtranjero, // 14
										comboCobElimPenCambioZona, // 15
										// rol (inciso) 16
										{
											xtype : 'panel',
											layout : 'hbox',
											width : 750,
											bodyPadding : 5,
											style : 'margin-bottom:5px;',
											items : [
													{
														id : 'fechaInicioVigencia',
														name : 'fechaInicioVigencia',
														fieldLabel : 'Fecha de inicio de vigencia',
														xtype : 'datefield',
														format : 'd/m/Y',
														editable : true,
														allowBlank : false,
														labelWidth : 180,
														value : new Date(),
														listeners : {
															change : function(
																	field,
																	value) {
																try {
																	Ext
																			.getCmp(
																					'fechaFinVigencia')
																			.setValue(
																					Ext.Date
																							.add(
																									value,
																									Ext.Date.YEAR,
																									1));
																} catch (e) {
																}
															}
														}
													},
													{
														id : 'fechaFinVigencia',
														name : 'fechaFinVigencia',
														fieldLabel : 'Fecha de fin de vigencia',
														xtype : 'datefield',
														format : 'd/m/Y',
														readOnly : true,
														allowBlank : false,
														labelWidth : 180,
														style : 'margin-left:5px;',
														value : Ext.Date.add(
																new Date(),
																Ext.Date.YEAR,
																1)
													} ]
										}, gridIncisos ],
								buttons : [
										{
											id : 'botonCotizar',
											icon : contexto
													+ '/resources/fam3icons/icons/calculator.png',
											text : hayTramiteCargado ? 'Precaptura'
													: 'Cotizar',
											handler : function() {
												// The getForm() method returns
												// the Ext.form.Basic instance:
												var form = this.up('form')
														.getForm();

												var hayTitular = 0;
												var menorDeEdad = false;
												var isMayorAEdadMaxima = false;
												var fueraDeGuanajuato = inputCdtipsit == 'SN'
														&& (campoCodigoPostal
																.getValue() < 36000 || campoCodigoPostal
																.getValue() > 38998);
												debug(
														'fueraDeGuanajuato',
														fueraDeGuanajuato ? 'si'
																: 'no');
												storeIncisos
														.each(function(record,
																index) {
															var rol = typeof record
																	.get('rol') == 'string' ? record
																	.get('rol')
																	: record
																			.get(
																					'rol')
																			.get(
																					'key');
															if (rol == "T" || true)// se
																					// agrego
																					// true
																					// por
																					// los
																					// isMayorAEdadMaxima
															{
																var fechaNacimiento = new Date(
																		record
																				.get('fechaNacimiento'));
																var hoy = new Date();
																var edad = parseInt((hoy
																		/ 365
																		/ 24
																		/ 60
																		/ 60
																		/ 1000 - fechaNacimiento
																		/ 365
																		/ 24
																		/ 60
																		/ 60
																		/ 1000));
																if (edad > EDAD_MAXIMA_COTIZACION) {
																	isMayorAEdadMaxima = true;
																}
																// console.log("hoy",parseInt(hoy));
																// console.log("fenacimi",parseInt(fechaNacimiento));
																// console.log("edad",edad);
																if (rol == "T") {
																	menorDeEdad = edad < 18;
																	hayTitular++;
																}
															}
														});
												if (form.isValid()) {
													if (hayTitular == 1) {
														if (!menorDeEdad || true) {
															if (!isMayorAEdadMaxima) {
																var incisosRecords = storeIncisos
																		.getRange();
																if (incisosRecords
																		&& incisosRecords.length > 0) {
																	if (!fueraDeGuanajuato) {
																		///////////////////////////////////////////////////////////////////////////
																		////// para los dispositivos moviles se necesita 1 segundo de espera //////
																		////// para que hagan el unfocus de los combos del grid anidado      //////
																		///////////////////////////////////////////////////////////////////////////
																		debug('mobile timeout in');
																		formPanel.setLoading(true);
																		//alert('1');
																		setTimeout(function(){
																		debug('mobile timeout out');
																		formPanel.setLoading(false);
																		//alert('2');
																		var incisosJson = [];
																		var nombres = 0;
																		storeIncisos
																				.each(function(
																						record,
																						index) {
																					if (record
																							.get('nombre')
																							&& record
																									.get('nombre').length > 0) {
																						nombres++;
																					}
																					incisosJson
																							.push({
																								id : record
																										.get('id'),
																								rol : {
																									key : typeof record
																											.get('rol') == 'string' ? record
																											.get('rol')
																											: record
																													.get(
																															'rol')
																													.get(
																															'key'),
																									value : ''
																								},
																								fechaNacimiento : record
																										.get('fechaNacimiento'),
																								sexo : {
																									key : typeof record
																											.get('sexo') == 'string' ? record
																											.get('sexo')
																											: record
																													.get(
																															'sexo')
																													.get(
																															'key'),
																									value : ''
																								},
																								nombre : record
																										.get('nombre'),
																								segundoNombre : record
																										.get('segundoNombre'),
																								apellidoPaterno : record
																										.get('apellidoPaterno'),
																								apellidoMaterno : record
																										.get('apellidoMaterno')
																							});
																				});
																		if (nombres == 0
																				|| nombres == incisosRecords.length) {
																			var submitValues = form
																					.getValues();
																			submitValues['incisos'] = incisosJson;
																			// window.console&&console.log(submitValues);
																			// Submit
																			// the
																			// Ajax
																			// request
																			// and
																			// handle
																			// the
																			// response
																			formPanel
																					.setLoading(true);
																			/*
																			 * Ext.MessageBox.show({
																			 * msg:
																			 * 'Cotizando...',
																			 * width:300,
																			 * wait:true,
																			 * waitConfig:{interval:100}
																			 * });
																			 */
																			Ext.Ajax
																					.request({
																						url : _URL_COTIZAR,
																						timeout : 120000,
																						jsonData : Ext
																								.encode(submitValues),
																						success : function(
																								response,
																								opts) {
																							// Ext.MessageBox.hide();
																							formPanel
																									.setLoading(false);
																							var jsonResp = Ext
																									.decode(response.responseText);
																							// window.console&&console.log(jsonResp);
																							if (jsonResp.success == true) {
																								Ext
																										.getCmp(
																												'idCotizacion')
																										.setValue(
																												jsonResp.id);
																								mostrarGrid();
																								if (hayTramiteCargado) {
																									Ext
																											.getCmp(
																													'botonClonarCotiza')
																											.hide();
																								}
																							} else {
																								Ext.Msg
																										.show({
																											title : 'Error',
																											msg : 'Error al cotizar',
																											buttons : Ext.Msg.OK,
																											icon : Ext.Msg.ERROR
																										});
																							}
																						},
																						failure : function(
																								response,
																								opts) {
																							// Ext.MessageBox.hide();
																							formPanel
																									.setLoading(false);
																							// window.console&&console.log("error");
																							Ext.Msg
																									.show({
																										title : 'Error',
																										msg : 'Error de comunicaci&oacute;n',
																										buttons : Ext.Msg.OK,
																										icon : Ext.Msg.ERROR
																									});
																						}
																					});
																		} else {
																			Ext.Msg
																					.show({
																						title : 'Datos incompletos',
																						msg : 'Si introduce el nombre de alg&uacute;n asegurado, es requerido introducirlo para el resto de asegurados',
																						buttons : Ext.Msg.OK,
																						icon : Ext.Msg.WARNING
																					});
																		}/////////////////////////////////////////////////////////////////////////
																		////// para los dispositivos moviles se necesita 1 segundo de espera //////
																		////// para que hagan el unfocus de los combos del grid anidado      //////
																		///////////////////////////////////////////////////////////////////////////
																		},1500);
																	} else {
																		Ext.Msg
																				.show({
																					title : 'Datos inv&aacute;lidos',
																					msg : 'C&oacute;digo postal no v&aacute;lido para este producto',
																					buttons : Ext.Msg.OK,
																					icon : Ext.Msg.WARNING
																				});
																	}
																} else {
																	Ext.Msg
																			.show({
																				title : 'Datos incompletos',
																				msg : 'Favor de introducir al menos un asegurado',
																				buttons : Ext.Msg.OK,
																				icon : Ext.Msg.WARNING
																			});
																}
															} else {
																Ext.Msg
																		.show({
																			title : 'Datos incompletos',
																			msg : 'La edad del asegurado no debe exceder de '+EDAD_MAXIMA_COTIZACION+' a&ntilde;os',
																			buttons : Ext.Msg.OK,
																			icon : Ext.Msg.WARNING
																		});
															}
														} else {
															Ext.Msg
																	.show({
																		title : 'Datos incompletos',
																		msg : 'El titular no puede ser menor de edad',
																		buttons : Ext.Msg.OK,
																		icon : Ext.Msg.WARNING
																	});
														}
													} else {
														Ext.Msg
																.show({
																	title : 'Datos incompletos',
																	msg : 'Favor de introducir solo un titular',
																	buttons : Ext.Msg.OK,
																	icon : Ext.Msg.WARNING
																});
													}
												} else {
													Ext.Msg
															.show({
																title : 'Datos incompletos',
																msg : 'Favor de introducir todos los campos requeridos',
																buttons : Ext.Msg.OK,
																icon : Ext.Msg.WARNING
															});
												}
											}
										},
										{
											text : 'Limpiar',
											icon : contexto
													+ '/resources/fam3icons/icons/arrow_refresh.png',
											id : 'botonLimpiar',
											handler : function() {
												gridIncisos.hayTitular = false;
												debug("gridIncisos.hayTitular=false;");
												gridIncisos.hayConyugue = false;
												debug("gridIncisos.hayConyugue=false;");
												formPanel.getForm().reset();
												debug("formPanel.getForm().reset();");
												window.parent.scrollTo(0, 0);
												campoCodigoPostal.focus();
												debug("window.parent.scrollTo(0,0);campoCodigoPostal.focus();");
												gridIncisos.getSelectionModel()
														.deselectAll();
												debug("gridIncisos.getSelectionModel().deselectAll();");
												storeIncisos
														.remove(storeIncisos
																.getRange());
												// storeIncisos.sync();
												debug("storeIncisos.remove(storeIncisos.getRange());");
											}
										}
										// agregado para cancelar un tramite {
										,{
		                                    text     : 'Rechazar'
		                                    ,icon    : contexto+'/resources/fam3icons/icons/cancel.png'
		                                    ,hidden  : !hayTramiteCargado
		                                    ,handler : function()
		                                    {
		                                        //console.log(form.getValues());
		                                        Ext.create('Ext.window.Window',
		                                        {
		                                            title        : 'Guardar detalle'
		                                            ,width       : 600
		                                            ,height      : 430
		                                            ,buttonAlign : 'center'
		                                            ,modal       : true
		                                            ,closable    : false
		                                            ,autoScroll  : true
		                                            ,items       :
		                                            [
		                                                {
		                                                    xtype   : 'textarea'
		                                                    ,id     : 'inputTextareaCommentsToRechazo'
		                                                    ,width  : 570
		                                                    ,height : 300
		                                                }
		                                                ,{
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
											                        ,itemId     : 'SWAGENTE'
											                        ,name       : 'SWAGENTE'
											                        ,inputValue : 'S'
											                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
											                    }
											                    ,{
											                        boxLabel    : 'No'
											                        ,name       : 'SWAGENTE'
											                        ,inputValue : 'N'
                                                                    ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
											                    }
											                ]
											            }
		                                            ]
		                                            ,buttons    :
		                                            [
		                                                {
		                                                    text     : 'Rechazar'
		                                                    ,icon    : contexto+'/resources/fam3icons/icons/cancel.png'
		                                                    ,handler : function()
		                                                    {
		                                                    	debug('rechazar');
		                                                        var window=this.up().up();
		                                                        window.setLoading(true);
		                                                        Ext.Ajax.request
                                                                ({
                                                                    url     : datComUrlMCUpdateStatus
                                                                    ,params : 
                                                                    {
                                                                        'smap1.ntramite' : ntramiteCargado
                                                                        ,'smap1.status'  : '4'//rechazado
                                                                        ,'smap1.comments' : Ext.getCmp('inputTextareaCommentsToRechazo').getValue()
                                                                        ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
                                                                    }
                                                                    ,success : function(response)
                                                                    {
                                                                    	window.setLoading(false);
                                                                        var json=Ext.decode(response.responseText);
                                                                        if(json.success==true)
                                                                        {
                                                                            Ext.create('Ext.form.Panel').submit
                                                                            ({
                                                                                url             : datComUrlMC
                                                                                ,standardSubmit : true
                                                                            });
                                                                        }
                                                                        else
                                                                        {
                                                                            Ext.Msg.show({
                                                                                title:'Error',
                                                                                msg: 'Error al rechazar',
                                                                                buttons: Ext.Msg.OK,
                                                                                icon: Ext.Msg.ERROR
                                                                            });
                                                                        }
                                                                    }
                                                                    ,failure : function()
                                                                    {
                                                                    	window.setLoading(false);
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
		                                                    text  : 'Cancelar'
		                                                    ,icon : contexto+'/resources/fam3icons/icons/cancel.png'
		                                                    ,handler : function()
		                                                    {
		                                                        this.up().up().destroy();
		                                                    }
		                                                }
		                                            ]
		                                        }).show();
		                                    }
		                                }
										// agregado para cancelar un tramite }
										]
							});
			// //////////////////////////
			// //// Fin formulario //////
			// //////////////////////////

			// /////////////////////////////////////////////
			// //// Cargador de formulario (sin grid) //////
			// /////////////////////////////////////////////
			/*
			 * Ext.define('LoaderCotizacion', { extend:'CotizacionSalud', proxy: {
			 * type:'ajax', url:_URL_CARGAR_COTIZACION, reader:{ type:'json',
			 * root:'cotizacion' } } });
			 * 
			 * var
			 * loaderCotizacion=Ext.ModelManager.getModel('LoaderCotizacion');
			 * loaderCotizacion.load(123, { success: function(resp) {
			 * formPanel.getForm().loadRecord(resp); bloquearFormulario(true);
			 * bloquearFormulario(false); } });
			 */
			// ////////////////////////////////////////////////////
			// //// Fin de cargador de formulario (sin grid) //////
			// ////////////////////////////////////////////////////
			campoCodigoPostal.focus();
			
			if(inputCdtipsit=='SL')
			{
				Ext.create('Ext.window.Window', {
					closable : false,
					width : 153,
					header : false,
					border : false,
					height : 340,
					resizable : false,
					items :
					[
					    {
					    	xtype : 'image'
					    	,src  : contexto+'/images/proceso/emision/imagencotizador.PNG'
					    }
					]
				}).showAt(610, 45);
			}

			if (hayTramiteCargado) {
				Ext.create('Ext.window.Window', {
					title : 'Documentos del tr&aacute;mite ' + ntramiteCargado,
					closable : false,
					width : 300,
					height : 300,
					autoScroll : true,
					collapsible : true,
					titleCollapse : true,
					startCollapsed : true,
					resizable : false,
					cls         : 'VENTANA_DOCUMENTOS_CLASS',
					loader : {
						scripts : true,
						autoLoad : true,
						url : urlDocumentosTramite,
						params : {
							'smap1.cdunieco' : inputCdunieco,
							'smap1.cdramo' : inputCdramo,
							'smap1.estado' : 'W',
							'smap1.nmpoliza' : '0',
							'smap1.nmsuplem' : '0',
							'smap1.nmsolici' : '0',
							'smap1.ntramite' : ntramiteCargado,
							'smap1.tipomov' : '0'
						}
					}
				}).showAt(450, 45);
			}

		});