Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {
	
	//Se establece un timeout de 2 min.
	//Ext.Ajax.timeout = 60000;
	Ext.Ajax.timeout = 120000;
	
	console.log('_IS_USUARIO_CALL_CENTER:', _IS_USUARIO_CALL_CENTER);

    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });
    
    // Conversion para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
    
    Ext.define('KeyValueModel', {
        extend: 'Ext.data.Model',
        fields : [{
            name : 'key',
            type : 'int'
        }, {
            name : 'value',
            type : 'string'
        }]
    });

    var storeTiposConsulta = Ext.create('Ext.data.JsonStore', {
        model: 'KeyValueModel',
        proxy: {
            type: 'ajax',
            url: _URL_TIPOS_CONSULTA,
            reader: {
                type: 'json',
                root: 'tiposConsulta'
            }
        }
    });
    storeTiposConsulta.load();
    
    var listViewOpcionesConsulta = Ext.create('Ext.grid.Panel', {
        collapsible:true,
        collapsed:true,
        store: storeTiposConsulta,
        multiSelect: false,
        hideHeaders:true,
        viewConfig: {
            emptyText: 'No hay tipos de consulta'
        },
        columns: [{
            flex: 1,
            dataIndex: 'value'
        }],
        listeners : {
            selectionchange : function(view, nodes) {
                tabDatosGeneralesPoliza.hide();
                /*
                tabPanelAgentes.hide();
                */
            
                if (this.getSelectionModel().hasSelection()) {
                    var tipoConsultaSelected = this.getSelectionModel().getSelection()[0];
                    
                    if (gridSuplementos.getSelectionModel().hasSelection()) {
                        var formBusquedaAux = panelBusqueda.down('form').getForm();
                        switch (tipoConsultaSelected.get('key')) {
                            case 1: //Consulta de Datos generales
                                
                                //Mostrar seccion de datos generales:
                                tabDatosGeneralesPoliza.show();
                                tabDatosGeneralesPoliza.child('#tabDatosGenerales').tab.show();
                                tabDatosGeneralesPoliza.setActiveTab('tabDatosGenerales');
                                
                                
                                //Datos de Copagos de poliza
                                storeCopagosPoliza.load({
                                    params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                        if(!success){
                                            showMessage('Error', 'Error al obtener los copagos de la p\u00F3liza', 
                                                Ext.Msg.OK, Ext.Msg.ERROR)
                                        }              
                                    }
                                });
                                
                                //Datos de Coberturas
                                storeCoberturas.load({
                                    params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                        if(!success){
                                            showMessage('Error', 'Error al obtener las coberturas', 
                                                Ext.Msg.OK, Ext.Msg.ERROR)
                                        }
                                    }
                                });
                                
                                //Datos de Coberturas b�sicas
                                storeCoberturasBasicas.load({
                                    params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                    	if(!success){
                                    		showMessage('Error', 'Error al obtener las coberturas b\u00E1sicas', 
                                                Ext.Msg.OK, Ext.Msg.ERROR)
                                    	}
                                    }
                                });
                                                                                              
                                
                                //Datos para asegurados (familia)
                                storeAsegurados.load({
                                    params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                        if(!success){
                                            showMessage('Error', 'Error al obtener los datos de familia', Ext.Msg.OK, Ext.Msg.ERROR);
                                        }
                                    }
                                });
                                
                                //Datos para endosos
                                storeDatosEndosos.load({
                                    params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                        if(!success){
                                            showMessage('Error', 'Error al obtener los endosos del asegurado', Ext.Msg.OK, Ext.Msg.ERROR);
                                        }
                                    }
                                });
                                
                                //Datos para enfermedades cr�nicas
                                storeDatosEnfermedades.load({
                                    params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                    	if(!success){
                                    		showMessage('Error','Error al obtener Enfermedades del asegurado', Ext.Msg.OK, Ext.Msg.ERROR);
                                    	}
                                    }
                                });
                                
                                //Se activan/desactivan tabs:
                                                                                                                    
                                	tabDatosGeneralesPoliza.child('#tabDatosAsegurado').tab.show();
                                	tabDatosGeneralesPoliza.child('#tabDatosContratante').tab.show();
                                	tabDatosGeneralesPoliza.child('#tabDatosTitular').tab.show();
                                	tabDatosGeneralesPoliza.child('#tabDatosAsegurados').tab.show();
                                	tabDatosGeneralesPoliza.child('#tabEndosos').tab.show();
                                	if(gridSuplementos.getSelectionModel().getSelection()[0].get('origen') == 'SISA') {
                                	   tabDatosGeneralesPoliza.child('#tbDocumentos').tab.hide();
                                	   tabDatosGeneralesPoliza.child('#tabEnfermedades').tab.show();
                                	} else {
                                	   tabDatosGeneralesPoliza.child('#tbDocumentos').tab.show();
                                	   tabDatosGeneralesPoliza.child('#tabEnfermedades').tab.hide();
                                	}
                                    tabDatosGeneralesPoliza.child('#tabDatosPlan').tab.hide();
                                    tabDatosGeneralesPoliza.child('#tabDatosCopagosPoliza').tab.hide();
                                    tabDatosGeneralesPoliza.child('#tabDatosCoberturasBasicas').tab.hide();
                                    tabDatosGeneralesPoliza.child('#tbHistorico').tab.hide();
                                    tabDatosGeneralesPoliza.child('#tbHistoricoFarmacia').tab.hide();
                                    tabDatosGeneralesPoliza.child('#tbVigencia').tab.hide();
                                
                                
                                //Se ocultan pesta�as para usuarios de call center
                                tabDatosGeneralesPoliza.child('#tbRecibos').tab.hide();
                                /*
                                if(_IS_USUARIO_CALL_CENTER){
                                	tabDatosGeneralesPoliza.child('#tbRecibos').tab.hide();                                	
                                } else {
                                	tabDatosGeneralesPoliza.child('#tbRecibos').tab.show();
                                }                                
                                */
                            break;
                            
                            case 2:
                                //Mostrar seccion de plan y coberturas:
                                tabDatosGeneralesPoliza.setActiveTab('tabDatosPlan');
                                tabDatosGeneralesPoliza.show();
                                tabDatosGeneralesPoliza.child('#tabDatosGenerales').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbDocumentos').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosPlan').tab.show();
                                tabDatosGeneralesPoliza.child('#tabDatosCopagosPoliza').tab.show();
                                /*if(gridSuplementos.getSelectionModel().getSelection()[0].get('origen') == 'SISA') {                                    
                                    tabDatosGeneralesPoliza.child('#tabDatosCoberturasBasicas').tab.show();
                                    */
                                	tabDatosGeneralesPoliza.child('#tabDatosCoberturasBasicas').tab.hide();
                                /*} else {
                                	tabDatosGeneralesPoliza.child('#tabDatosCoberturasBasicas').tab.hide();
                                }*/
                                tabDatosGeneralesPoliza.child('#tabDatosAsegurado').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosContratante').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosTitular').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbHistorico').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbHistoricoFarmacia').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosAsegurados').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbVigencia').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabEndosos').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabEnfermedades').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbRecibos').tab.hide();
                            break;
                            
                            case 3:
                                //Mostrar seccion de historicos
                                tabDatosGeneralesPoliza.setActiveTab('tbHistorico');
                                tabDatosGeneralesPoliza.show();                         
                                tabDatosGeneralesPoliza.child('#tabDatosGenerales').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbHistorico').tab.show();
                                //El hist�rico de farmacia solo se muestra para SISA
                                if(gridSuplementos.getSelectionModel().getSelection()[0].get('origen') == 'SISA') {
                                    tabDatosGeneralesPoliza.child('#tbHistoricoFarmacia').tab.show();
                                } else {
                                	tabDatosGeneralesPoliza.child('#tbHistoricoFarmacia').tab.hide();
                                }
                                tabDatosGeneralesPoliza.child('#tbDocumentos').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosPlan').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosCopagosPoliza').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosCoberturasBasicas').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosAsegurado').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosContratante').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosTitular').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosAsegurados').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbVigencia').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabEndosos').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabEnfermedades').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbRecibos').tab.hide();
                            break;
                            
                            case 4:
                                //Mostrar secci�n de Vigencia
                                tabDatosGeneralesPoliza.setActiveTab('tbVigencia');
                                tabDatosGeneralesPoliza.show();
                                tabDatosGeneralesPoliza.child('#tabDatosGenerales').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbVigencia').tab.show();
                                tabDatosGeneralesPoliza.child('#tbHistorico').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbHistoricoFarmacia').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbDocumentos').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosPlan').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosCopagosPoliza').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosCoberturasBasicas').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosAsegurado').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosContratante').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosTitular').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabDatosAsegurados').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabEndosos').tab.hide();
                                tabDatosGeneralesPoliza.child('#tabEnfermedades').tab.hide();
                                tabDatosGeneralesPoliza.child('#tbRecibos').tab.hide();
                            break;                                               
                        }//end switch                
                    } else {                        
                    	showMessage('Aviso', 'Debe seleccionar una opci\u00F3n', Ext.Msg.OK, Ext.Msg.WARN)
                    }
                } 
            }
        }
    });
    
    /**INFORMACION DE LA SECCION DE FAMILIA (ASEGURADOS)**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('AseguradosModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdperson'},
            {type:'string', name:'cdrfc'},
            {type:'string', name:'cdrol'},
            {type:'string', name:'dsrol'},
            {type:'date'  , name:'fenacimi', dateFormat: 'd/m/Y'},
            {type:'string', name:'nmsituac'},
            {type:'string', name:'cdtipsit'},
            {type:'string', name:'sexo'},
            {type:'string', name:'nombre'},
            {type:'string', name:'status'},
            {type:'string', name:'parentesco'}
        ]
    });
    
    // Store
    var storeAsegurados = new Ext.data.Store({
     model: 'AseguradosModel',
     proxy:
     {
          type: 'ajax',
          url : _URL_CONSULTA_DATOS_ASEGURADO,          
      reader:
      {
           type: 'json',
           root: 'datosAsegurados'
      }
     }
    });

    var gridDatosAsegurado = Ext.create('Ext.grid.Panel', {
        title   : 'DATOS DE LOS ASEGURADOS',
        store   : storeAsegurados,
        id      : 'gridDatosAsegurado',
        //width   : 830,
        autoScroll:true,
        columns: [
            //{text:'Rol',dataIndex:'dsrol',width:130 , align:'left'},
            {text:'ID',dataIndex:'cdperson',flex:1,align:'left'},
        	{text:'Parentesco',dataIndex:'parentesco',flex:1 , align:'left'},            
            {text:'Nombre',dataIndex:'nombre',flex:2,align:'left'},
            {text:'Estatus',dataIndex:'status',flex:1,align:'left'},
            {text:'RFC',dataIndex:'cdrfc',width:120,align:'left'},
            {text:'Sexo',dataIndex:'sexo',flex:1 , align:'left'},
            {text:'Nacimiento',dataIndex:'fenacimi',flex:1, align:'left',renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            {
            	text         : 'Hospitalizaci&oacute;n',
            	align        : 'center',
                xtype        : 'actioncolumn',
                id           : 'columnAvisoHospitalizacion',
                icon         : _CONTEXT+'/resources/fam3icons/icons/building.png',
                tooltip      : 'Dar Aviso de Hospitalizaci�n',
                flex         : 1,
                //width        : auto,
                hidden       : _IS_USUARIO_CALL_CENTER? false : true,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                	// Se generan y manejan los elementos a mostrar en la ventana de Aviso de Hospitalizacion
                    var record = grid.getStore().getAt(rowIndex);
                    if(record.get("status") == 'VIGENTE'){
                    var ventanaAviso =  Ext.create('Ext.window.Window', {
                        title       : 'Aviso de Hospitalizacion',
                        modal       : true,
                        closeAction: 'destroy',
                        width       : 850,
                        height      : 500,
                        items : [
							{
								xtype: 'form',
								defaults : {
									bodyPadding : 5,
									border : false
								},
								items : [ 
									{
										layout: 'hbox',
										items : [
													{xtype: 'textfield', name : 'params.cdperson', fieldLabel : 'C&oacute;digo', 
													labelWidth: 60, width: 300, readOnly: true, labelAlign: 'left', value: record.get('cdperson')},
													{xtype:'textfield', name:'params.status', fieldLabel : 'Estatus Asegurado',
													labelWidth: 120, width: 450, readOnly: true, labelAlign: 'right', value: record.get('status')}
										]
									},{
										layout: 'hbox',
										items : [
													{xtype: 'textfield', name: 'params.nombre', fieldLabel: 'Nombre', 
													labelWidth: 60, width: 750, labelAlign: 'left', readOnly: true, value: record.get('nombre')}
										]
									},{
										layout: 'hbox',
										items : [
													{xtype: 'datefield', name: 'params.fecha', fieldLabel: 'Fecha', 
													labelWidth: 60, width: 300, labelAlign: 'left', readOnly: true, value: new Date() },
													{xtype: 'datefield', name: 'params.hora', fieldLabel: 'Hora', 
													labelWidth: 120, width: 300, labelAlign: 'right', readOnly: true, value: new Date(), format: 'g:i a'}
										]
									},{ /*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
										/////////////////////////////////////////COMBOBOX PARA SELECCION DE HOSPITAL DE INGRESO/////////////////////////////////////////*/
										layout: 'hbox',
										items : [ 
													{
														xtype: 'combo',
														fieldLabel: 'Hospital',
														name: 'params.hospital',
														store: storeHospitales,
														minChars: 2,
														queryMode : 'remote', 
														forceSelection: true,
														queryParam : 'params.hospital',
														queryCaching: false,
														displayField: 'value',
														valueField: 'key',
														allowBlank: false,
														blankText : 'Debe seleccionar hospital de ingreso.',
														labelAlign: 'left',
														labelWidth: 60,
														width: 750
													}
										]
									},{
										layout: 'hbox',
										items : [
													{xtype: 'datefield', name: 'params.feingreso', fieldLabel: 'Fecha ingreso', allowBlank: false, 
													blankText : 'Debe seleccionar fecha de ingreso.', minValue: Ext.Date.add(new Date(),Ext.Date.DAY,-10), 
													maxValue: Ext.Date.add(new Date(),Ext.Date.DAY, 10), format: 'd/m/Y', submitFormat: 'd/m/Y'},
													{xtype: 'hiddenfield', name: 'params.cdunieco', value: panelBusqueda.down('form').getForm().findField("params.cdunieco").getValue()},
													{xtype: 'hiddenfield', name: 'params.cdramo', value: panelBusqueda.down('form').getForm().findField("params.cdramo").getValue()},
													{xtype: 'hiddenfield', name: 'params.nmpoliza', value: panelBusqueda.down('form').getForm().findField("params.nmpoliza").getValue()},
													{xtype: 'hiddenfield', name: 'params.cdsubram', value: panelBusqueda.down('form').getForm().findField("params.cdsubram").getValue()}
										]
									},{
										layout: 'hbox',
										items : [
													{xtype: 'textarea', name: 'params.comentario',   fieldLabel: 'Comentarios', allowBlank: false, 
													blankText : 'Debe agregar un comentario.', labelWidth: 110, width: 700, labelAlign: 'left',
													listeners: {
											                change: function( fld, newValue, oldValue, opts ) {
											                    fld.setValue( newValue.toUpperCase() );
										               	 }                    
            										}
													}
										]
									},{
										layout: {
												type: 'vbox',
												align: 'center'
										},
										items : [{
												xtype: 'button', 
												name: 'btnSubbmitAvisoHosp', 
												text: 'Continuar...',
												minWidth: 100,
												handler: function(btn, e) {
													var formPanel = this.up().up();
													if (formPanel.form.isValid())
													{
															Ext.Msg.show({
															title:'Enviar Aviso',
															msg: 'El Aviso se enviar&aacute;, �esta de acuerdo?',
															buttons: Ext.Msg.OKCANCEL,
															icon: Ext.Msg.INFO,
															fn: function(buttonId, text, opt) {
																if (buttonId == 'ok'){
																	ventanaAviso.setLoading(true);
																	formPanel.form.submit(
																	{
																		clientValidation: true,
																	    url: _URL_ENVIAR_AVISO_HOSPITALIZACION,
																	    params: {
																	    	'params.icodpoliza': panelBusqueda.down('form').getForm().findField("params.icodpoliza").getValue(),
																	    	'params.cdagente' : panelBusqueda.down('form').getForm().findField("params.cdagente").getValue()
																	    },
																	    success: function(form, action) {
																	    	ventanaAviso.setLoading(false);
																	    	showMessage('&Eacute;xito (Aviso '+gridSuplementos.getSelectionModel().getSelection()[0].get('origen')+': '+action.result.iCodAviso+')', action.result.mensajeRes, Ext.Msg.OK, Ext.Msg.INFO)
																	    	ventanaAviso.hide();
																	    },
																	    failure: function(form, action) {
																	    	ventanaAviso.setLoading(false);
																	    	if(action.result.mensajeRes == '1')
																	    	{
																	    		showMessage('Aviso No Registrado', 'Error al registrar Aviso de Hospitalizaci&oacute;n en el sistema.', Ext.Msg.OK, Ext.Msg.ERROR);
																	    	}else if(action.result.mensajeRes == '2'){
																	    		showMessage('Aviso ('+gridSuplementos.getSelectionModel().getSelection()[0].get('origen')+': '+action.result.iCodAviso+') Registrado', 'Error al obtener telefono de agente.', Ext.Msg.OK, Ext.Msg.ERROR);
																	    	}else if(action.result.mensajeRes == '3'){
																	    		showMessage('Aviso ('+gridSuplementos.getSelectionModel().getSelection()[0].get('origen')+': '+action.result.iCodAviso+') Registrado', 'El agente no tiene tel&eacute;fono registrado en el sistema.', Ext.Msg.OK, Ext.Msg.WARNING);
																	    	}else if(action.result.mensajeRes == '4'){
																	    		showMessage('Aviso ('+gridSuplementos.getSelectionModel().getSelection()[0].get('origen')+': '+action.result.iCodAviso+') Registrado', 'No se pudo enviar Aviso.', Ext.Msg.OK, Ext.Msg.WARNING);
																	    	}else if(action.result.mensajeRes == '5'){
																	    		showMessage('Aviso ('+gridSuplementos.getSelectionModel().getSelection()[0].get('origen')+': '+action.result.iCodAviso+') Registrado', 'Aviso Registrado y Enviado (Estatus Env&iacute;o en BD: 0)', Ext.Msg.OK, Ext.Msg.INFO);
																	    	}
																	    	ventanaAviso.hide();
																	    }
																	});
															    }
															}
														});
													}
													else
													{
														showMessage('Atenci&oacute;n', 'Complete adecuadamente el formulario.', Ext.Msg.OK, Ext.Msg.WARNING)
													}
												}
											}
										]
									}
								]
							},{
								xtype: 'grid',
								store   : storeAvisosAnteriores,
								title: 'Avisos anteriores:',								
								defaults:{sortable:true, align:'right', autoScroll: true},
								columns:[{
									text:'Fecha Registro',
									dataIndex:'feregistro',
									format:'d M Y',
									align:'left',
									flex: 1
								},{
									text:'Hospital',
									dataIndex:'dspresta',
									align:'left',
									flex: 3
								},{
									text:'Fecha Ingreso',
									dataIndex:'feingreso',
									align:'left',
									format:'d M Y',
									flex: 1
								},{
									text:'Comentario',
									dataIndex:'comentario',
									align:'left',
									flex: 3
								}
								]
							}
                        ]
                     });
                     ventanaAviso.down('grid').getStore().removeAll();
                     ventanaAviso.down('combo').getStore().removeAll();
                     ventanaAviso.show();
                     storeAvisosAnteriores.load({
            		  params: {
            		  			'params.cdperson': record.get('cdperson'),
            		  			'params.icodpoliza': panelBusqueda.down('form').getForm().findField("params.icodpoliza").getValue()
            		  },
            		  callback: function(records, operation, success) {
                            if (!success) {
                                showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                                return;
                            }
                            if(records.length == 0){
                                showMessage('No hay avisos', 'Este asegurado no tiene avisos anteriores', Ext.Msg.OK, Ext.Msg.INFO);
                                return;
                            }
                        }
            		});
                  	}else{
                  		showMessage('Servicio Denegado', 'Asegurado NO posee derechos de servicio debido a su &lsquo;status&rsquo;.', Ext.Msg.OK, Ext.Msg.ERROR);
                  	}
                }
            }
        ]
    });
    
    /**INFORMACION DEL P�LIZA ACTUAL**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('SuplementoModel', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'cdramo'},
            {name: 'cdsubram'},
            {name: 'cdunieco'},
            {name: 'estado'},
            {name: 'nmpoliza'},
            {name: 'nmsuplem'},
            {name: 'dstipsup'},
            {name: 'feemisio', dateFormat: 'd/m/Y'},
            {name: 'feinival', dateFormat: 'd/m/Y'},
            {name: 'fefinval', dateFormat: 'd/m/Y'},
            {name: 'nlogisus'},
            {name: 'nsuplogi', type:'int'},
            {name: 'ptpritot', type : 'float'},
            {name: 'estatus'},
            {name: 'origen'}
        ]
    });
    
    
    var storeSuplementos = new Ext.data.Store({
    	pageSize : 20,
        model: 'SuplementoModel',
        proxy: {
            type         : 'memory'
                ,enablePaging : true
                ,reader      : 'json'
                ,data        : []
            }
    });
    
    var gridSuplementos = Ext.create('Ext.grid.Panel', {
        id : 'suplemento-form',
        store : storeSuplementos,
        selType: 'checkboxmodel',
        //autoScroll:true,
        defaults: {sortable : true, width:120, align : 'right'},
        columns : [{
            text : 'P\u00F3liza',
            dataIndex : 'nmpoliza',
            width:150
        }, {
            id : 'dstipsup',
            text : 'Tipo de endoso',
            dataIndex : 'dstipsup',
            width:200
        },{
            id : 'estatus',
            text : 'Estatus',
            dataIndex : 'estado',
            width:100
        },{
            text : 'Fecha de emisi\u00F3n',
            dataIndex : 'feemisio',
            format: 'd M Y',
            width:150
        }, {
            text : 'Fecha inicio vigencia',
            dataIndex : 'feinival',
            format: 'd M Y',
            width:150
        },{
            text : 'Fecha fin vigencia',
            dataIndex : 'fefinval',
            format: 'd M Y',
            width:150
        }, {
            text : 'Prima total',
            dataIndex : 'ptpritot',
            renderer : 'usMoney',
            width:150,
            hidden: true //Ocultamos prima 
        },{
            text : 'Origen',
            dataIndex : 'origen',
            width : 80
        }],

        listeners : {
            selectionchange : function(model, records) {
                
                listViewOpcionesConsulta.up('panel').setTitle('');
                
                //Limpiar seleccion de la lista de opciones de consulta
                limpiaSeleccionTiposConsulta();
                
                //Limpiar la seccion de informacion principal:
                limpiaSeccionInformacionPrincipal();
                
                if(this.getSelectionModel().hasSelection()) {
                    
                    // Mostrar listado de tipos de consulta:
                    listViewOpcionesConsulta.up('panel').setTitle('Elija una consulta:');
                    listViewOpcionesConsulta.expand();
                    
                
                    //Lenar campos de formulario de busqueda:
                    var rowSelected = gridSuplementos.getSelectionModel().getSelection()[0];
                    panelBusqueda.down('form').getForm().findField("params.cdunieco").setValue(rowSelected.get('cdunieco'));
                    panelBusqueda.down('form').getForm().findField("params.cdramo").setValue(rowSelected.get('cdramo'));
                    panelBusqueda.down('form').getForm().findField("params.cdsubram").setValue(rowSelected.get('cdsubram'));
                    panelBusqueda.down('form').getForm().findField("params.estado").setValue(rowSelected.get('estado'));
                    panelBusqueda.down('form').getForm().findField("params.nmpoliza").setValue(rowSelected.get('nmpoliza'));
                    panelBusqueda.down('form').getForm().findField("params.suplemento").setValue(rowSelected.get('nmsuplem'));
                    
                    //console.log('Params busqueda de datos grales poliza=');console.log(panelBusqueda.down('form').getForm().getValues());

                    // Consultar Datos Generales de la Poliza:
                    storeDatosPoliza.load({
                        params : panelBusqueda.down('form').getForm().getValues(),
                        callback : function(records, operation, success) {
                            if (success) {
                                if (records.length > 0) {
                                    // Se asigna valor al parametro de busqueda:
                                    panelBusqueda.down('form').getForm().findField("params.cdagente").setValue(records[0].get('cdagente'));
                                    // Se llenan los datos generales de la poliza elegida
                                    panelDatosPoliza.getForm().loadRecord(records[0]);
                                    
                                    //Mensaje DXN
                                    cambiaTextoMensajeAgente3(records[0].get('dsperpag'));
                                    
                                    //Resaltar en otro color en plan cuando sea Opci�n Hospitalaria
                                    if(records[0].get('dsplan') == 'OPCI�N HOSPITALARIA'){
                                    	panelDatosPoliza.down('[name=dsplan]').setFieldStyle({'color':'#005B9A','font-weight':'bold'});                                    	
                                    } else {
                                    	panelDatosPoliza.down('[name=dsplan]').setFieldStyle({'color':'#00E','font-weight':'bold'});
                                    }
                                    
                                    //Cuando el Agente sea PREVEX que se identifique f�cilmente
                                    //console.log(records[0].get('cdunieco'));
                                    if(records[0].get('cdunieco') == '1403'){
                                        panelDatosPoliza.down('[name=agente]').setFieldStyle({'color':'#0F6280','font-weight':'bold'});
                                        cambiaTextoMensajeAgente4('AVISO: ATENCION EXCLUSIVA EN RED CERRADA HOSPITAL CLINICA NOVA');
                                    } else {
                                        panelDatosPoliza.down('[name=agente]').setFieldStyle({'color':'#000000','font-weight':'normal'});
                                        cambiaTextoMensajeAgente4('');
                                    }
                                } else {
                                    showMessage('Aviso', 
                                        'No existen datos generales de la p\u00F3liza elegida. La P&oacute;iza no existe, verifique sus datos', 
                                        Ext.Msg.OK, Ext.Msg.ERROR);
                                }
                            } else {
                                showMessage('Error', 
                                    'Error al obtener los datos generales de la p\u00F3liza elegida, intente m\u00E1s tarde',
                                    Ext.Msg.OK, Ext.Msg.ERROR);
                            }

                        }
                    });
                   
                    // Consultar Datos Complementarios de la Poliza:
                    storeDatosComplementarios.load({
                        params : panelBusqueda.down('form').getForm().getValues(),
                        callback : function(records, operation, success) {
                            if (success) {
                                if (records.length > 0) {
                                    // Se asigna valor al parametro de busqueda:
                                    //panelBusqueda.down('form').getForm().findField("params.cdagente").setValue(records[0].get('cdagente'));
                                	
                                    // Se llenan los datos generales de la poliza elegida
                                    panelDatosComplementarios.getForm().loadRecord(records[0]);
                                    
                                    panelDatosComplementarios.setVisible(true);
                                    //Mensajes
                                    cambiaTextoMensajeAgente(records[0].get('statusaseg'));
                                    cambiaTextoMensajeAgente2(records[0].get('statusaseg'));
                                } else {                                    
                                	panelDatosComplementarios.setVisible(false);
                                }
                            } else {
                                showMessage('Error', 
                                    'Error al obtener los datos generales de la p\u00F3liza elegida, intente m\u00E1s tarde',
                                    Ext.Msg.OK, Ext.Msg.ERROR);
                            }

                        }
                    });
                    
                    // Consultar Datos del Asegurado
                    storeDatosAsegurado.load({
                    params : panelBusqueda.down('form').getForm().getValues(),
                    callback : function(records, operation, success) {
                        if (success) {
                            if (records.length > 0) {
                                // Se llenan los datos del asegurado
                                panelDatosAsegurado.getForm().loadRecord(records[0]);
                            } 
                        } else {
                        showMessage('Error', 
                            'Error al obtener los datos del detalle del asegurado, intente m\u00E1s tarde',
                            Ext.Msg.OK, Ext.Msg.ERROR);
                        }
                    
                    }
                    });
                    
                    // Consultar Datos del Titular
                    storeDatosTitular.load({
                    params : panelBusqueda.down('form').getForm().getValues(),
                    callback : function(records, operation, success) {
                        if (success) {
                            if (records.length > 0) {
                                // Se llenan los datos del titular
                                panelDatosTitular.getForm().loadRecord(records[0]);
                            } 
                        } else {
                        showMessage('Error', 
                            'Error al obtener los datos del titular, intente m\u00E1s tarde',
                            Ext.Msg.OK, Ext.Msg.ERROR);
                        }
                    
                    }
                    });
                    
                    // Consultar Datos del Contrante
                    storeDatosContratante.load({
                    params : panelBusqueda.down('form').getForm().getValues(),
                    callback : function(records, operation, success) {
                        if (success) {
                            if (records.length > 0) {
                                // Se llenan los datos del contratante
                                panelDatosContratante.getForm().loadRecord(records[0]);
                            } 
                        } else {
                        showMessage('Error', 
                            'Error al obtener los datos del contratante, intente m\u00E1s tarde',
                            Ext.Msg.OK, Ext.Msg.ERROR);
                        }
                    
                    }
                    });
                    
                    // Consultar Datos del Plan                    
                    storeDatosPlan.load({
                    params : panelBusqueda.down('form').getForm().getValues(),
                    callback : function(records, operation, success) {
                        if (success) {
                            if (records.length > 0) {
                                // Se llenan los datos del contratante
                                panelDatosPlan.getForm().loadRecord(records[0]);
                            } 
                        } else {
                        showMessage('Error', 
                            'Error al obtener los datos del plan, intente m\u00E1s tarde',
                            Ext.Msg.OK, Ext.Msg.ERROR);
                        }
                    
                    }
                    });
                }
            }
        }
    });
    
    gridSuplementos.store.sort([
        { 
        	property    : 'nsuplogi',
        	direction   : 'DESC'
        }
    ]);
    
    
    /**DATOS GENERALES**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('DatosPolizaModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'dsunieco'},
            {type:'string', name:'dsramo'},
            {type:'string', name:'nmpoliex'},            
            {type:'date',   name:'feemisio', dateFormat: 'd/m/Y'},
            {type:'date',   name:'feefecto', dateFormat: 'd/m/Y'},
            {type:'date',   name:'feproren', dateFormat: 'd/m/Y'},
            {type:'string', name:'dsplan'},            
            {type:'string', name:'statuspoliza'},
            {type:'string', name:'dsperpag'},
            {type:'string', name:'statuspago'},
            {type:'date',   name:'fepag', dateFormat: 'd/m/Y'},
            {type:'string', name:'contratante'},
            {type:'string', name:'cdrfc'},            
            {type:'string', name:'titular'},
            {type:'string', name:'agente'},
            {type:'string', name:'cdagente'},
            {type:'string', name:'cdunieco'}
                                   
        ]
    });
    
    //Modelo para datos complementarios
    Ext.define('DatosComplementariosModel',{
        extend: 'Ext.data.Model',
        fields:[
            {type:'string', name:'cdperson'},
            {type:'string', name:'statusaseg'},            
            {type:'string', name:'paterno'},
            {type:'string', name:'materno'},
            {type:'string', name:'nombre'},
            {type:'string', name:'sexo'},
            {type:'string', name:'edad'},
            {type:'string', name:'fenacimi'},
            {type:'string', name:'agente'}
            
        ]
    });

    // Store
    var storeDatosPoliza = new Ext.data.Store({
        model: 'DatosPolizaModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosPoliza'
            }
        }
    });
    
    //Store para datos complementarios
    var storeDatosComplementarios = new Ext.data.Store({
        model: 'DatosComplementariosModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_COMPLEMENTARIOS,
            reader : {
            	type: 'json',
            	root: 'datosComplementarios'
            }
        }
        
    });
    
    // FORMULARIO DATOS DE LA POLIZA
    var panelDatosPoliza = Ext.create('Ext.form.Panel', {
        model : 'DatosPolizaModel',
        width : 815 ,
        border : false,
        //height : 280,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'dsunieco', fieldLabel: 'Sucursal',      readOnly: true, labelWidth: 60, width: 300},
                {xtype: 'textfield', name: 'dsramo',   fieldLabel: 'Ramo',      readOnly: true, labelWidth: 60,  width: 200, labelAlign: 'right'},
                {xtype: 'textfield', name: 'nmpoliex', fieldLabel: 'P&oacute;liza', readOnly: true, labelWidth: 60,  width: 300, labelAlign: 'right'}
            ]
        },
        {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'dsplan',   fieldLabel: 'Plan',      readOnly: true, labelWidth: 60, width: 500},
                {xtype: 'textfield', name: 'statuspoliza', fieldLabel: 'Estatus P&oacute;liza', readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        },
    	{
            layout : 'hbox',
            items : [ 
                {xtype: 'datefield', name: 'feemisio', fieldLabel: 'Fecha emisi&oacute;n',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 250}, 
                {xtype: 'datefield', name: 'feefecto', fieldLabel: 'Fecha inicio vigencia',         format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 250, labelAlign: 'right'}, 
                {xtype: 'datefield', name: 'feproren', fieldLabel: 'Fecha fin vigencia', format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 250, labelAlign: 'right'}
            ]
        },    	  
    	{
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'dsperpag', fieldLabel: 'Forma Pago',  readOnly: true, labelWidth: 120, width: 250},
                {xtype: 'textfield', name: 'statuspago', fieldLabel: 'Estatus de pago',  readOnly: true, labelWidth: 120, width: 300,labelAlign: 'right'},
                {xtype: 'datefield', name: 'fepag', fieldLabel: 'Fecha de pago', format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 250, labelAlign: 'right'}
            ]
        },
        {
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'titular', fieldLabel: 'Contratante', readOnly: true, labelWidth: 120, width: 500}, 
                {xtype:'textfield', name:'cdrfc',   fieldLabel: 'RFC',         readOnly: true, labelWidth: 60,  width: 300, labelAlign: 'right'}
            ]
        },
        {
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'agente', fieldLabel: 'Agente',                readOnly: true, labelWidth: 120, width: 700}
            ]
        }
        ]
    });
    
    // FORMULARIO DATOS COMPLEMENTARIOS
    var panelDatosComplementarios = Ext.create('Ext.form.Panel', {
        model : 'DatosComplementariosModel',
        width : 815 ,
        border : false,
        //height : 280,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [            
                {xtype: 'textfield', name: 'cdperson', fieldLabel: 'C&oacute;digo',      readOnly: true, labelWidth: 60, width: 300, labelAlign: 'left'},
                {xtype: 'textfield', name: 'statusaseg',   fieldLabel: 'Estatus Asegurado',      readOnly: true, labelWidth: 120,  width: 450, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'nombre',   fieldLabel: 'Nombre',      readOnly: true, labelWidth: 60,  width: 750, labelAlign: 'left'}                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'sexo',   fieldLabel: 'Sexo',      readOnly: true, labelWidth: 60,  width: 250, labelAlign: 'left'},
                {xtype: 'textfield', name: 'edad',   fieldLabel: 'Edad',      readOnly: true, labelWidth: 60, width: 250, labelAlign: 'right'},
            	{xtype: 'datefield', name: 'fenacimi', fieldLabel: 'Fecha nacimiento',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 250, labelAlign: 'right'}              
            ]
        }
        
        ]
    });
    
    
    /**INFORMACION DEL PANEL DE AVISO DE HOSPITALIZACION**/
    //-------------------------------------------------------------------------------------------------------------    
    //Modelo
     Ext.define('AvisosAnterioresModel',{
        extend: 'Ext.data.Model',
        fields:[
    	   { type:'string', name:'dspresta' },
    	   { type:'string', name:'comentario' },
    	   { name:'feregistro', dateFormat:'d/m/Y' },
    	   { name:'feingreso', dateFormat:'d/m/Y' }
    	   
    	]
    });
    
    var storeAvisosAnteriores = Ext.create('Ext.data.Store', {
    	pageSize:20,
        model:'AvisosAnterioresModel',
        proxy:{
        	type:'ajax',
        	url: _URL_LOADER_AVISOS_ANTERIORES,
        	enablePaging:true,
        	reader:{
        		type:'json',
        		root:'datosAvisosAnteriores'
        	}  	
        }
    });
    
    var storeHospitales = Ext.create('Ext.data.Store', {
    	model: 'Generic',
    	autoLoad: false,
    	proxy: {
	         type: 'ajax',
	         url : _URL_CONSULTA_HOSPITALES,
	         reader: {
		             type: 'json',
		             root: 'datosHospitales'
	         }
     	},
     	listeners: {
        		beforeload: function( store, operation, eOpts) {
        		operation.params['params.icodpoliza'] =  panelBusqueda.down('form').getForm().findField("params.icodpoliza").getValue()
   				 }
		}
    });
    
    var cboHospital = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Hospital',
        name: 'hospital',
        store: storeHospitales,
        floating : true,
        queryMode : 'remote', 
        forceSelection: true,
        queryParam : 'params.hospital',
        displayField: 'value',
        valueField: 'key',
        allowBlank: false,
        blankText : 'Debe seleccionar hospital de ingreso.',
        labelAlign: 'left',
    	labelWidth: 60,
    	width: 750
    });
    
    
    /**INFORMACION DEL GRID DE TARIFICACION**/
    //-------------------------------------------------------------------------------------------------------------    
    //Modelo
    Ext.define('DatosTarificacionModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'string',    name:'dsgarant'      },
              {type:'float',    name:'montoComision' },
              {type:'float',    name:'montoPrima'    },
              {type:'string',    name:'sumaAsegurada' }
        ]
    });
    
    // Store
    var storeDatosTarificacion = new Ext.data.Store({
        model: 'DatosTarificacionModel',
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_DATOS_TARIFA_POLIZA,
            reader: {
                type: 'json',
                root: 'datosTarifa'
            }
        }
    });
    
    /**GRID PARA LOS DATOS DE TARIFICACION **/
    var gridDatosTarificacion = Ext.create('Ext.grid.Panel', {
        width   : 820,
        //title   : 'DATOS TARIFICACI&Oacute;N',
        store   : storeDatosTarificacion,
        autoScroll:true,
        id      : 'gridDatosTarificacion',
        features:[{
                    ftype:'summary'
                }],
        columns: [
            {
                text            :'Garant&iacute;a',
                dataIndex       :'dsgarant',
                width           :250,
                summaryRenderer : function(value){return Ext.String.format('TOTALES');}
            },
            {
                text            :'Suma Asegurada',  
                dataIndex       :'sumaAsegurada',
                width           :170 , 
                align           :'right'  
                
            },
            {
                text            :'Monto de la Prima',
                dataIndex       :'montoPrima',
                width           : 170,
                align           :'right',        
                renderer        :Ext.util.Format.usMoney,
                summaryType     :'sum'
            },
            {
                text            : 'Monto de la Comisi&oacute;n',
                dataIndex       :'montoComision',
                width           : 170,
                renderer        :Ext.util.Format.usMoney,
                align           :'right',        
                summaryType     :'sum'
            }
        ]
    });
    gridDatosTarificacion.store.sort([
        { 
            property    : 'dsgarant',
            direction   : 'ASC'
        }
    ]);
    
    
    /**INFORMACION DEL GRID DE COPAGOS**/
    //-------------------------------------------------------------------------------------------------------------    
    //Modelo
    Ext.define('CopagosPolizaModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'int',    name:'orden'      },
              {type:'string',    name:'descripcion' },
              {type:'string',    name:'valor' },
              {type:'string',    name:'agrupador' , id:'agrupadorId'},
              {type:'string',    name:'ordenAgrupador' }
        ]
    });
    // Store
    var storeCopagosPoliza = new Ext.data.Store({
        model: 'CopagosPolizaModel',
//        groupField : 'ordenAgrupador',
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_COPAGOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosCopagosPoliza'
            }
        }
    });
    
    
    
    // GRID PARA LOS DATOS DE COPAGOS
    var gridCopagosPoliza = Ext.create('Ext.grid.Panel', {
        width   : 500,
        viewConfig: {
            stripeRows: false,
            enableTextSelection: true
        },
        title   : 'Copagos',
        store   : storeCopagosPoliza,
        autoScroll:true,
        id      : 'gridCopagosPoliza',
        columns: [
            //{text:'Orden',            dataIndex:'orden',       width:50, sortable:false, hidden:true},
            {text:'Descripci\u00F3n', dataIndex:'descripcion', width:300, align:'left', sortable:false},
            {text:'Valor',            dataIndex:'valor',       width:200, align:'left', sortable:false}
        ]
//        ,features: [{
//            groupHeaderTpl: '{[values.children[0].get("agrupador")]}',
//            ftype:          'grouping',
//            startCollapsed: false
//        }]
    });
    gridCopagosPoliza.store.sort([
        { 
            property    : 'orden',
            direction   : 'ASC'
        }
    ]);
    
    //////////////////////
    /**INFORMACION DEL GRID DE COBERTURAS**/
    //-------------------------------------------------------------------------------------------------------------  
    
    //Modelo
    Ext.define('CoberturasModel',{
        extend: 'Ext.data.Model',
        fields: [              
              {type:'string',    name:'descripcion' }
        ]
    });
    
    // Store
    var storeCoberturas = new Ext.data.Store({
        model: 'CoberturasModel',        
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_COBERTURAS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosCoberturasPoliza'
            }
        }
    });
    
    // GRID PARA LOS DATOS DE COBERTURAS
    var gridCoberturas = Ext.create('Ext.grid.Panel', {
        width   : 400,
        viewConfig: {
            stripeRows: false,
            enableTextSelection: true
        },
        title   : 'Coberturas',
        store: storeCoberturas,
        autoScroll:true,
        columns: [
            {text:'', dataIndex : 'descripcion', width:300, align:'left', sortable:false}            
        ]
    });
    /////////////////////
    
    
    /**INFORMACION DEL GRID DE COBERTURAS B�SICAS**/
    //-------------------------------------------------------------------------------------------------------------    
    //Modelo
    Ext.define('CoberturasBasicasModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'string',    name:'descripcion'      },
              {type:'string',    name:'copagoporcentaje' },
              {type:'string',    name:'copagomonto' },
              {type:'string',    name:'incluido' },
              {type:'string',    name:'beneficiomaximo' },
              {type:'string',    name:'beneficiomaximovida' }
        ]
    });
    // Store
    var storeCoberturasBasicas = new Ext.data.Store({
        model: 'CoberturasBasicasModel',
        /*groupField : 'agrupador',*/
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_COBERTURAS_BASICAS,
            reader: {
                type: 'json',
                root: 'datosCoberturasBasicas'
            }
        }
    });
    // GRID PARA LOS DATOS DE COBERTURAS BASICAS
    var gridCoberturasBasicas = Ext.create('Ext.grid.Panel', {
        width   : 700,
        viewConfig: {
            stripeRows: false,
            enableTextSelection: true
        },
        //title   : 'DATOS COBERTURAS BASICAS',
        store   : storeCoberturasBasicas,
        autoScroll:true,
        id      : 'gridCoberturasBasicas',
        columns: [
            //{text:'Orden',            dataIndex:'orden',       width:50, sortable:false, hidden:true},
            {text:'Descripci\u00F3n', dataIndex:'descripcion', width:200, align:'left', sortable:false},
            {text:'(%)Copago', dataIndex:'copagoporcentaje', width:100, align:'left', sortable:false},
            {text:'($)Copago', dataIndex:'copagomonto', width:100, align:'left', sortable:false, renderer : 'usMoney'},
            {text:'Incluido',            dataIndex:'incluido',       width:50, align:'left', sortable:false},
            {text:'Beneficio M\u00E1ximo',            dataIndex:'beneficiomaximo',       width:100, align:'left', sortable:false, renderer : 'usMoney'},
            {text:'Beneficio Maximo Vida',            dataIndex:'beneficiomaximovida',       width:100, align:'left', sortable:false, renderer : 'usMoney'}
        ]
        
    });
    
    /**INFORMACION DE LA SECCION DE PLAN**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('DatosPlanModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'plan'},
            {type:'date',   name:'fecha', dateFormat: 'd/m/Y'},
            {type:'string', name:'descripcion'},
            {type:'string', name:'tipoprograma'},
            {type:'string', name:'calculopor'},
            {type:'string', name:'beneficiomaximoanual'},
            {type:'string', name:'beneficiomaximovida'},
            {type:'string', name:'identificadortarifa'},
            {type:'string', name:'zona'}
        ]
    });
    

    // Store
    var storeDatosPlan = new Ext.data.Store({
        model: 'DatosPlanModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_PLAN,
            reader: {
                type: 'json',
                root: 'datosPlan'
            }
        }
    });
    

    
    // FORMULARIO DATOS DE PLAN
    var panelDatosPlan = Ext.create('Ext.form.Panel', {
        model : 'DatosPlanModel',
        width : 850 ,
        border : false,
        //height : 280,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'plan', fieldLabel: 'Plan',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'datefield', name: 'fecha', fieldLabel: 'Fecha',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 300}                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'descripcion',   fieldLabel: 'Descripcion',      readOnly: true, labelWidth: 120, width: 600}                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'tipoprograma',   fieldLabel: 'Tipo de Programa',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'calculopor', fieldLabel: 'Calculo Por', readOnly: true, labelWidth: 120,  width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [ 
                {
                    xtype:'textfield', 
                    name:'beneficiomaximoanual', 
                    fieldLabel: 'Beneficio M&aacute;ximo Anual', 
                    readOnly: true, 
                    labelWidth: 200, 
                    width: 400
                }, 
                {xtype:'textfield', name:'beneficiomaximovida',   fieldLabel: 'Beneficio M&aacute;ximo por Vida',         readOnly: true, labelWidth: 200,  width: 400, labelAlign: 'right', renderer:Ext.util.Format.usMoney}
            ]
        },{
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'identificadortarifa', fieldLabel: 'Identificador de Tarifa', readOnly: true, labelWidth: 200, width: 500}, 
                {xtype:'textfield', name:'zona',   fieldLabel: 'Zona',         readOnly: true, labelWidth: 100,  width: 300, labelAlign: 'right'}
            ]
        }]
    });
    
    /**INFORMACION DE LA SECCION DE ASEGURADO**/
    //-------------------------------------------------------------------------------------------------------------
    // Model
    Ext.define('DatosAseguradoModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdperson'},
            {type:'string', name:'edad'},
            {type:'string', name:'identidad'},            
            {type:'string', name:'nombre'},
            {type:'string', name:'tiposangre'},
            {type:'string', name:'antecedentes'},
            {type:'string', name:'oii'},
            {type:'string', name:'telefono'},
            {type:'string', name:'direccion'},
            {type:'string', name:'correo'},
            {type:'string', name:'mcp'},
            {type:'string', name:'mcpespecialidad'},
            {type:'string', name:'ocp'},
            {type:'string', name:'ocpespecialidad'}
        ]
    });
    
    //Store
    var storeDatosAsegurado = new Ext.data.Store({
        model: 'DatosAseguradoModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_ASEGURADO_DETALLE,
            reader: {
                type: 'json',
                root: 'datosAseguradoDetalle'
            }
        }
    });
    
    //FORMULARIO ASEGURADO
    var panelDatosAsegurado = Ext.create('Ext.form.Panel', {
        model : 'DatosAseguradoModel',
        itemId: 'formDatosAsegurado',
        width : 850 ,
        tbar   : [
        			{
        				 xtype	: 'button'
        			    ,text	: 'E.C.D'
        			    ,itemId : 'btnECD'
        			    ,listeners : {
        			    	beforerender: function(me){
        			    		me.setLoading(true);
        			    		Ext.Ajax.request(
        			    			    {
        			    			        url      : _URL_CONSULTA_PERFIL
        			    			        ,params  :
        			    			        {
        			    			            'params.listaPersonas'  : ''+panelBusqueda.down('form').getForm().findField("params.cdperson").getValue()
        			    			            
        			    			        }
        			    			        ,success : function(response)
        			    			        {
        			    			            me.setLoading(false);
        			    			            var json = Ext.decode(response.responseText);
        			    			            debug('### cargar suma asegurada:',json);
        			    			            if(json.success)
        			    			            {
        			    			            	
        			    			            	var url=_CONTEXT+'/resources/fam3icons/icons/';
        			    			            	var en=parseInt(json.list[0].PERFIL_FINAL+'')
        			    			            	
        			    			            	if(json.list.length>0){
	        			    			                switch(en){
	        			    			                	case 0:
	        			    			                		url+=FlagsECD.PerfilCero;
	        			    			                		break;
	        			    			                	case 1:
	        			    			                		url+=FlagsECD.PerfilUno;
	        			    			                		break;
	        			    			                	case 2: 
	        			    			                		url+=FlagsECD.PerfilDos;
	        			    			                		break;
	        			    			                	case 3:
	        			    			                		url+=FlagsECD.PerfilTres;
	        			    			                		break;
	        			    			                	
	        			    			                }
        			    			            	}
        			    			                me.setIcon(url);
        			    			            }
        			    			            else
        			    			            {
        			    			                mensajeError(json.respuesta);
        			    			            }
        			    			        }
        			    			        ,failure : function()
        			    			        {
        			    			            me.setLoading(false);
        			    			            errorComunicacion();
        			    			        }
        			    			    });
        			    		me.icon= _CONTEXT+''
        			    	}
        			    }
        			    ,handler: function (){
        			    	try{
        			    		
        			    		Ext.create('Ext.window.Window',
                                        {
                                            title        : 'E.C.D.'
                                            //,modal       : true
                                            ,buttonAlign : 'center'
                                            ,width       : 800
                                            ,height      : 300
                                            ,autoScroll  : true
                                            ,loader      :
                                            {
                                                url       : _URL_CONSULTA_ECD
                                                ,scripts  : true
                                                ,autoLoad : true
                                                ,loadMask : true
                                                ,ajaxOptions: {
                                                    method   : 'POST'
                                                },
                                                params: {
                                                    'params.cdperson':panelBusqueda.down('form').getForm().findField("params.cdperson").getValue()
                                                }
                                            }
                                        }).show();
        			    	
        			    	}catch(e){
        			    		console.log(e)
        			    	}
        			    }
        			    
        			}
        		  ],
        border : false,
        //height : 280,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'cdperson', fieldLabel: 'C&oacute;digo',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'edad',   fieldLabel: 'Edad',      readOnly: true, labelWidth: 50,  width: 200, labelAlign: 'right'},
                {xtype: 'textfield', name: 'identidad', fieldLabel: 'Identidad', readOnly: true, labelWidth: 100,  width: 300, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'nombre',   fieldLabel: 'Nombre',      readOnly: true, labelWidth: 120, width: 700}              
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',   name: 'tiposangre', fieldLabel: 'Tipo de sangre',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield',   name: 'antecedentes',     fieldLabel: 'Antecedentes', readOnly: true, labelWidth: 100, width: 400, labelAlign: 'right'}              
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'oii', fieldLabel: 'OII', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype:'textfield', name:'telefono',   fieldLabel: 'Tel&eacute;fono',         readOnly: true, labelWidth: 80,  width: 300, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'direccion', fieldLabel: 'Direcci&oacute;n', readOnly: true, labelWidth: 120, width: 590}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'correo', fieldLabel: 'e-mail', readOnly: true, labelWidth: 120, width: 590}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'mcp', fieldLabel: 'MCP',  readOnly: true, labelWidth: 120, width: 400},
                {xtype: 'textfield', name: 'mcpespecialidad', fieldLabel: 'Especialidad MCP', readOnly: true, labelWidth: 120, width: 400, labelAlign: 'right'}
            ]
        },
        {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'ocp', fieldLabel: 'OCP',  readOnly: true, labelWidth: 120, width: 400},
                {xtype: 'textfield', name: 'ocpespecialidad', fieldLabel: 'Especialidad OCP', readOnly: true, labelWidth: 120, width: 400, labelAlign: 'right'}
            ]
        }
        ]
    });
    
   /**INFORMACION DE LA SECCION DE TITULAR**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('DatosTitularModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'identificacion'},
            {type:'string', name:'nombre'},
            {type:'string', name:'fenacimiento'},
            {type:'string', name:'edad'},
            {type:'string', name:'sexo'},
            {type:'string', name:'edocivil'},
            {type:'string', name:'feingreso'},
            {type:'string', name:'telefono'},
            {type:'string', name:'direccion'},
            {type:'string', name:'colonia'},
            {type:'string', name:'codigopostal'},
            {type:'string', name:'celular'},
            {type:'string', name:'email'}
        ]
    });
    
    // Store
    var storeDatosTitular = new Ext.data.Store({
        model: 'DatosTitularModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_TITULAR,
            reader: {
                type: 'json',
                root: 'datosTitular'
            }
        }
    });
    
    // FORMULARIO DATOS DE TITULAR
    var panelDatosTitular = Ext.create('Ext.form.Panel', {
        model : 'DatosTitularModel',
        width : 850 ,
        border : false,
        //height : 280,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'identificacion', fieldLabel: 'Identificacion',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'nombre',   fieldLabel: 'Nombre',      readOnly: true, labelWidth: 120,  width: 500, labelAlign: 'right'}                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'fenacimiento',   fieldLabel: 'Fecha Nacimiento',      readOnly: true, labelWidth: 120, width: 250},
                {xtype: 'textfield', name: 'edad', fieldLabel: 'Edad', readOnly: true, labelWidth: 120,  width: 250, labelAlign: 'right'},
                {xtype: 'textfield', name: 'sexo', fieldLabel: 'Sexo', readOnly: true, labelWidth: 120,  width: 250, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'edocivil',   fieldLabel: 'Estado Civil',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'feingreso', fieldLabel: 'Fecha de Ingreso', readOnly: true, labelWidth: 120,  width: 250, labelAlign: 'right'},
                {xtype: 'textfield', name: 'telefono', fieldLabel: 'Tel&eacute;fono', readOnly: true, labelWidth: 120,  width: 250, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'direccion',   fieldLabel: 'Direccion',      readOnly: true, labelWidth: 120, width: 800}                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'colonia',   fieldLabel: 'Colonia',      readOnly: true, labelWidth: 120, width: 500},
                {xtype: 'textfield', name: 'codigopostal', fieldLabel: 'Codigo Postal', readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'celular',   fieldLabel: 'Celular',      readOnly: true, labelWidth: 120, width: 350},
                {xtype: 'textfield', name: 'email', fieldLabel: 'e-mail', readOnly: true, labelWidth: 120,  width: 450, labelAlign: 'right'}
            ]
        }/*,       	
		{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'telefono1', fieldLabel: 'Tel&eacute;fono No. 1',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'area1',     fieldLabel: '&Aacute;rea', readOnly: true, labelWidth: 120, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', name: 'puesto',     fieldLabel: 'Puesto',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'telefono2', fieldLabel: 'Tel&eacute;fono No. 2',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'area2',     fieldLabel: '&Aacute;rea', readOnly: true, labelWidth: 100, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', name: 'giro',     fieldLabel: 'Giro de Empresa',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'fax', fieldLabel: 'Fax',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'area',     fieldLabel: '&Aacute;rea', readOnly: true, labelWidth: 100, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', name: 'relacion',     fieldLabel: 'Relaci&oacute;n',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'codigopostal', fieldLabel: 'C&oacute;digo Postal', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype:'textfield', name:'tipocontratante',   fieldLabel: 'Tipo de contratante',         readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'rfc', fieldLabel: 'RFC', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype:'textfield', name:'imss',   fieldLabel: 'Registro del IMSS',         readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        }*/
        ]
    });
    
    
    /**INFORMACION DE LA SECCION DE CONTRATANTE**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('DatosContratanteModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'razonsocial'},
            {type:'string', name:'zonacosto'},
            {type:'string', name:'domicilio'},
            {type:'string', name:'ciudad'},
            {type:'string', name:'estado'},
            {type:'string', name:'representante'},
            {type:'string', name:'telefono1'},
            {type:'string', name:'area1'},
            {type:'string', name:'telefono2'},
            {type:'string', name:'area2'},
            {type:'string', name:'fax'},
            {type:'string', name:'areafax'},
            {type:'string', name:'codigopostal'},
            {type:'string', name:'representante'},
            {type:'string', name:'puesto'},
            {type:'string', name:'giro'},
            {type:'string', name:'relacion'},
            {type:'string', name:'tipocontratante'},
            {type:'string', name:'rfc'},
            {type:'string', name:'imss'}
        ]
    });
    

    // Store
    var storeDatosContratante = new Ext.data.Store({
        model: 'DatosContratanteModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_CONTRATANTE,
            reader: {
                type: 'json',
                root: 'datosContratante'
            }
        }
    });
    
    // FORMULARIO DATOS DE CONTRATANTE
    var panelDatosContratante = Ext.create('Ext.form.Panel', {
        model : 'DatosContratanteModel',
        width : 850 ,
        border : false,
        //height : 280,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'razonsocial', fieldLabel: 'Razon Social',      readOnly: true, labelWidth: 120, width: 500},
                {xtype: 'textfield', name: 'zonacosto',   fieldLabel: 'Zona de Costo',      readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'domicilio',   fieldLabel: 'Domicilio',      readOnly: true, labelWidth: 120, width: 500},
                {xtype: 'textfield', name: 'ciudad', fieldLabel: 'Ciudad', readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'estado',   fieldLabel: 'Estado',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'representante', fieldLabel: 'Representante', readOnly: true, labelWidth: 120,  width: 500, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'telefono1', fieldLabel: 'Tel&eacute;fono No. 1',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'area1',     fieldLabel: '&Aacute;rea', readOnly: true, labelWidth: 120, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', name: 'puesto',     fieldLabel: 'Puesto',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'telefono2', fieldLabel: 'Tel&eacute;fono No. 2',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'area2',     fieldLabel: '&Aacute;rea', readOnly: true, labelWidth: 100, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', name: 'giro',     fieldLabel: 'Giro de Empresa',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'fax', fieldLabel: 'Fax',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'area',     fieldLabel: '&Aacute;rea', readOnly: true, labelWidth: 100, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', name: 'relacion',     fieldLabel: 'Relaci&oacute;n',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'codigopostal', fieldLabel: 'C&oacute;digo Postal', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype:'textfield', name:'tipocontratante',   fieldLabel: 'Tipo de contratante',         readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'rfc', fieldLabel: 'RFC', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype:'textfield', name:'imss',   fieldLabel: 'Registro del IMSS',         readOnly: true, labelWidth: 120,  width: 300, labelAlign: 'right'}
            ]
        }]
    });
    
    /**INFORMACION DE LA SECCION DE ENDOSOS**/
    //-------------------------------------------------------------------------------------------------------------
    Ext.define('EndososModel', {
                    extend:'Ext.data.Model',
                    fields:['tipoendoso','tipoextraprima','descripcion','resumen']
                });
    
    //Store
    var storeDatosEndosos = new Ext.data.Store({
     model: 'EndososModel',
     proxy:
     {
          type: 'ajax',
          url : _URL_CONSULTA_DATOS_ENDOSOS,          
      reader:
      {
           type: 'json',
           root: 'datosEndosos'
      }
     }
    });
    
    //GRID
    var gridDatosEndosos = Ext.create('Ext.grid.Panel', {
        title   : 'DATOS DE LOS ENDOSOS',
        store   : storeDatosEndosos,
        id      : 'gridDatosEndosos',
        width   : 830,
        selType: 'checkboxmodel',
        autoScroll:true,        
        columns: [            
            {text:'Tipo de Endoso',dataIndex:'tipoextraprima',width:200 , align:'left'},
            {text:'Tipo Extraprima',dataIndex:'tipoendoso',width:200,align:'left'},
            {text:'Descripcion',dataIndex:'descripcion',width:200,align:'left'},
            {text:'Resumen',dataIndex:'resumen',width:200,align:'left'}            
        ],
        listeners : {
            selectionchange : function(model, records) {
            	if (this.getSelectionModel().hasSelection()) {
            		//Tomamos la fila seleccionada
            		var rowSelectedEndoso = this.getSelectionModel().getSelection()[0];                                		
            	   showMessage(rowSelectedEndoso.get('tipoendoso'), rowSelectedEndoso.get('resumen'), Ext.Msg.OK, Ext.Msg.INFO);
            	}
            	  
            }
        }
           
    });
    
    /**INFORMACION DE LA SECCION DE E.C.D. (ENFERMEDADES CR�NICAS)**/
    //-------------------------------------------------------------------------------------------------------------
    Ext.define('EnfermedadesModel', {
                    extend:'Ext.data.Model',
                    fields:['icd','enfermedad']
                });
    
    //Store
    var storeDatosEnfermedades = new Ext.data.Store({
     model: 'EnfermedadesModel',
     proxy:
     {
          type: 'ajax',
          url : _URL_CONSULTA_DATOS_ENFERMEDADES,          
      reader:
      {
           type: 'json',
           root: 'datosEnfermedades'
      }
     }
    });
    
    //GRID
    var gridDatosEnfermedades = Ext.create('Ext.grid.Panel', {
        title   : 'ENFERMEDADES CRONICAS',
        store   : storeDatosEnfermedades,
        id      : 'gridDatosEnfermedades',
        width   : 830,
        /*selType: 'checkboxmodel',*/
        autoScroll:true,        
        columns: [            
            {text:'I C D',dataIndex:'icd',width:200 , align:'left'},
            {text:'Enfermedad',dataIndex:'enfermedad',width:630,align:'left'}                        
        ]        
           
    });
    
    
    /**INFORMACION DEL GRID DE LA POLIZA DEL ASEGURADO**/
    //-------------------------------------------------------------------------------------------------------------    
    // Modelo
    Ext.define('PolizaAseguradoModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdramo'},
            {type:'string', name:'cdunieco'},
            {type:'string', name:'dsramo'},
            {type:'string', name:'dsunieco'},
            {type:'string', name:'estado'},
            {type:'string', name:'nmpoliex'},
            {type:'string', name:'nmpoliza'},
            {type:'string', name:'cdperson'},
            {type:'string', name:'nombreAsegurado'},
            {type:'string', name:'identificacion'},
            {type:'string', name:'icodpoliza'},
            {type:'string', name:'origen'},
            {type:'string'  , name:'feinivigencia', dateFormat: 'd/m/Y'},
            {type:'string'  , name:'fefinvigencia', dateFormat: 'd/m/Y'},
            {type:'string', name:'nmsituac'}
            
        ]
    });
    
    var storePolizaAsegurado = Ext.create('Ext.data.Store', {
        pageSize : 10,
        autoLoad : true,
        model: 'PolizaAseguradoModel',
        proxy    : {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
    });
    
    
    // GRID PARA LAS POLIZAS DEL ASEGURADO
    var gridPolizasAsegurado= Ext.create('Ext.grid.Panel', {
        store : storePolizaAsegurado,
        selType: 'checkboxmodel',
        width : 985,        
        bbar     :
        {
            displayInfo : true,
            store       : storePolizaAsegurado,
            xtype       : 'pagingtoolbar'
        },
        features:[{
           ftype:'summary'
        }],
        columns: [
            {text: 'P&oacute;liza', dataIndex: 'nmpoliex', width: 150},
            {text: 'C&oacute;digo', dataIndex: 'cdperson', width: 85},
            {text: 'Nombre', dataIndex: 'nombreAsegurado', width: 240},
            {text: 'Identificaci&oacute;n', dataIndex: 'identificacion', width: 145, hidden:true},
            /*{text: 'Sucursal', dataIndex: 'dsunieco', width: 145},*/
            {text: 'Estatus P&oacute;liza', dataIndex: 'estado', width: 100},
        	{text: 'Producto', dataIndex: 'dsramo', width:145},            
            {text: 'C&oacute;digo de la p&oacuteliza',  dataIndex: 'icodpoliza', hidden:true},
            {text: 'Origen', dataIndex: 'origen', width: 60},
            {text: 'Inicio', dataIndex: 'feinivigencia'/*, format:'d M Y'*/, width: 85},
            {text: 'Fin', dataIndex: 'fefinvigencia'/*, format:'d M Y'*/, width: 100},
            {text: 'Nmsituac',  dataIndex: 'nmsituac', hidden:true}
            //,{text: '# poliza', dataIndex: 'nmpoliza', width: 70}
        ]
        
    });
    
    /*Filtro para status del Grid de Resultados*/   
    
    var storeStatus = Ext.create('Ext.data.Store', {
    fields: ['abbr', 'name'],
        data : [
            {"abbr":"*", "name":"TODOS"},
        	{"abbr":"V", "name":"VIGENTE"}
            
        ]
    });
    
    var cboStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Ver Estatus:',
        store: storeStatus,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'abbr',
        /*renderTo: Ext.getBody(),*/
        listeners:{
             /*scope: yourScope,*/
             'select': function(combo, records, eOpts){
             	var regExp = new RegExp("^" + combo.getRawValue(),"i");
                if(combo.getValue() == '*') {
                    gridPolizasAsegurado.getStore().clearFilter();
                } else {
                    gridPolizasAsegurado.getStore().filter("estado", regExp);  //GRID.getStore().filter("CAMPO", regExp)
                }
                
             	
             }
        }
    });
    
    var windowPolizas= Ext.create('Ext.window.Window', {
        title : 'Elija un asegurado:',    	
        //height: 400,
        modal:true,
        //width : 660,
        closeAction: 'hide',
        items:[cboStatus,gridPolizasAsegurado],        
        buttons:[{
            text: 'Aceptar',
            handler: function(){
                if (gridPolizasAsegurado.getSelectionModel().hasSelection()) {
                	
                    
                    //Asignar valores de la poliza seleccionada al formulario de busqueda
                    var rowPolizaSelected = gridPolizasAsegurado.getSelectionModel().getSelection()[0];
                    var formBusqueda = panelBusqueda.down('form').getForm();
                    formBusqueda.findField("params.nmpoliex").setValue(rowPolizaSelected.get('nmpoliex'));
                    formBusqueda.findField("params.icodpoliza").setValue(rowPolizaSelected.get('icodpoliza'));
                    formBusqueda.findField("params.cdperson").setValue(rowPolizaSelected.get('cdperson'));
                    formBusqueda.findField("params.nmsituac").setValue(rowPolizaSelected.get('nmsituac'));
                    
                    gridPolizasAsegurado.getStore().removeAll();
                                        
                    windowPolizas.close();
                    
                    // Recargar store con busqueda de historicos de la poliza seleccionada
                    cargaStoreSuplementos(formBusqueda.getValues());
                    try{
                		_fieldById('btnECD',null,true).fireEvent('beforerender',_fieldById('btnECD',null,true))
                	}catch(e){
                		debugError(e);
                	}
                    
                }else{
                    showMessage('Aviso', 'Seleccione un registro', Ext.Msg.OK, Ext.Msg.INFO);
                }
            }
        }]
    });
    
	
    /***INFORMACI�N DEL HISTORICO DE POLIZA***/
    //----------------------------------------
    //Modelo
    Ext.define('HistoricoModel',{
    	extend:'Ext.data.Model',
    	fields:[
    	   {name:'cdramo'},
    	   {name:'cdunieco'},
    	   {name:'estado'},
    	   {name:'nmpoliza'},
    	   {name:'nmsuplem'},
    	   {name:'dstipsup'},
    	   {name:'feemisio', dateFormat:'d/m/Y'},
    	   {name:'feinival', dateFormat:'d/m/Y'},
    	   {name:'fefinval', dateFormat:'d/m/Y'},
    	   {name:'nlogisus'},
    	   {name:'nsuplogi', type:'int'},
    	   {name:'ptpritot', type:'float'},
    	   {name: 'observa'},
           {name: 'dsplan'},
           {name: 'origen'}
    	]
    });
    
    var storeHistorico = new Ext.data.Store({
        pageSize:20,
        model:'HistoricoModel',
        proxy:{
        	type:'ajax',
        	url: _URL_LOADER_HISTORICO,
        	enablePaging:true,
        	reader:{
        		type:'json',
        		root:'datosHistorico'
        	}
        	        	
        }
    });
    
    var gridHistorico = Ext.create('Ext.grid.Panel',{
        id:'historico-form',
        height:350,        
        store:storeHistorico,
        selType:'rowmodel',
        defaults:{sortable:true,width:120,align:'right'},
        columns:[{
            text:'Consecutivo',
            dataIndex:'nsuplogi',
            width:80
        },{
            text:'P\u00F3liza',
            dataIndex:'nmpoliza',
            width:150
        },{
        	text:'Fec. Emisi\u00F3n',
        	dataIndex:'feemisio',
        	format:'d M Y',
        	width:150
        },{
        	text:'Fec. Inicio',
        	dataIndex:'feinival',
        	format:'d M Y',
        	width:150
        },{
            text:'Fec. Fin',
            dataIndex:'fefinval',
            format:'d M Y',
            width:150
        },{
        	text:'Estatus',
        	dataIndex:'estado',
        	width:150
        },{
            text:'Observa.',
            dataIndex:'observa',
            width:150
        },{
        	text:'Plan',
        	dataIndex:'dsplan',
        	width:150
        },{
        	text:'Prima total',
        	dataIndex:'ptpritot',
        	renderer:'usMoney',
        	width:150,
        	hidden:true
        }]
    });
    
    gridHistorico.store.sort([
        {
        	property:'nsuplogi',
        	direction:'DESC'
        }    
    ]);
    
    
    /***INFORMACI�N DEL HISTORICO DE FARMACIA***/    
    //----------------------------------------
    //Model
    Ext.define('FarmaciaModel', {
        extend:'Ext.data.Model',
        fields:['iultimo','poliza','tigrupo','maximo','total','pendiente','gastototal','disponible','orden']
    });
    
    //Store
    var storeHistoricoFarmacia = new Ext.data.Store({
        pageSize:20,
        model:'FarmaciaModel',
        proxy:{
            type:'ajax',
            url: _URL_LOADER_HISTORICO_FARMACIA,
            enablePaging:true,
            reader:{
                type:'json',
                root:'historicoFarmacia'
            }
                        
        }
    });
        
    var gridHistoricoFarmacia = Ext.create('Ext.grid.Panel',{
        id:'historicofarmacia-form',
        height:350,        
        store:storeHistoricoFarmacia,
        selType:'rowmodel',
        defaults:{sortable:true,width:120,align:'right'},
        columns     : [{
            header    : 'Ref.',
            dataIndex : 'iultimo',
            flex      : 1
        },{
            header    : 'Poliza',
            dataIndex : 'poliza',
            flex      : 1
        },{
            header    : 'Grupo',
            dataIndex : 'tigrupo',
            flex      : 1
        },{
            header    : 'Beneficio M�ximo',
            dataIndex : 'maximo',
            flex      : 1,
            renderer  : 'usMoney'
        },{
            header    : 'Gasto Aplicado',
            dataIndex : 'total',
            flex      : 1,
            renderer  : 'usMoney'
        },{
            header    : 'Pend. de Aplicar',
            dataIndex : 'pendiente',
            flex      : 1,
            renderer  : 'usMoney'
        },{
            header    : 'Gasto Total',
            dataIndex : 'gastototal',
            flex      : 1,
            renderer  : 'usMoney'
        },{
            header    : 'Disponible',
            dataIndex : 'disponible',
            flex      : 1,            
            renderer : function(value, meta) {
                if(value < 0) {
                	//Si est� excedido, lo marcar� en color rojo.
                    meta.style = "background-color:red;usMoney";                    
                }                
                return Ext.util.Format.usMoney(value);
            }            
        },{
            header    : 'Observaciones',
            dataIndex : 'orden',
            flex      : 1,
            renderer : function(value, meta) {
                if(value == 'ACTUAL') {
                    meta.style = "background-color:yellow;";
                    
                } else {
                    meta.style = "background-color:pink;";
                }
                return value;
            }   
        }
        ]
    });
    
    
    /***INFORMACI�N DE PERIODOS DE VIGENCIA***/    
    //----------------------------------------
    //Model
    Ext.define('VigenciaModel', {
        extend:'Ext.data.Model',
        fields: [
            {type:'string', name:'cdperson'},
            {type:'string', name:'nombre'},
            {type:'string', name:'estatus'},
            {type:'string', name:'dias'},
            {type:'string', name:'anios'},                        
            {type:'string',   name:'feinicial'},
            {type:'string',   name:'fefinal'}
        ]
    });
    
    //Store
    var storeVigencia = new Ext.data.Store({
        pageSize:20,
        model:'VigenciaModel',
        proxy:{
            type:'ajax',
            url: _URL_LOADER_VIGENCIA,
            enablePaging:true,
            reader:{
                type:'json',
                root:'periodosVigencia'
            }
                        
        }
    });
       
    
    //Grid
    var gridVigencia = Ext.create('Ext.grid.Panel',{
        id:'vigencia-form',
        height:350,        
        store:storeVigencia,
        selType:'rowmodel',
        defaults:{sortable:true,width:120,align:'right'},
        columns : [{
                header    : 'Estatus',
                dataIndex : 'estatus',
                flex      : 1
            },{
                header    : 'No. Dias',
                xtype      : 'numbercolumn',
                format      : '0.00',
                dataIndex : 'dias',                            
                flex      : 1
            },{
                header    : 'No. A�os',
                 xtype      : 'numbercolumn',
                format      : '0.00',
                dataIndex   : 'anios',
                flex      : 1
            },{
                header    : 'Fec. Inicial',
                dataIndex : 'feinicial',
                flex      : 1                            
            },{
                header    : 'Fec. Final',
                dataIndex : 'fefinal',                            
                flex      : 1                            
            }
            ]
    });
    
    /***PANEL PRINCIPAL***/    
    //----------------------------------------
    
    var tabDatosGeneralesPoliza = Ext.create('Ext.tab.Panel', {
        width: 830,
        items: [{
            title : 'DATOS GENERALES',
            itemId: 'tabDatosGenerales',
            border:false,
            items:[{
                items: [panelDatosComplementarios, panelDatosPoliza]
                   
            }]
        },
        {            
            title: 'PLAN',            
            itemId: 'tabDatosPlan',
            items:[{                
                items: [panelDatosPlan]
            }]
        },
    	{
            //title: 'DATOS TARIFICACION',
        	title: 'COBERTURAS',
            //itemId: 'tabDatosTarificacion',
        	itemId: 'tabDatosCopagosPoliza',
        	layout : 'hbox',
            items: [
                gridCopagosPoliza,
                gridCoberturas
            ]
        },
        {            
            title: 'COBERTURAS BASICAS',            
            itemId: 'tabDatosCoberturasBasicas',
            items:[{                
                items: [gridCoberturasBasicas]
            }]
        },
        {            
            title: 'ASEGURADO',            
            itemId: 'tabDatosAsegurado',
            items:[{                
                items: [panelDatosAsegurado]
            }]
        },
        {            
            title: 'TITULAR',            
            itemId: 'tabDatosTitular',
            items:[{                
                items: [panelDatosTitular]
            }]
        },
        {
            title: 'FAMILIA',
            itemId: 'tabDatosAsegurados',
            items:[
				gridDatosAsegurado, 
				{
					xtype  : 'panel',
					name   : 'pnlDatosTatrisit',
					autoScroll : true,
				    loader: {
				        //url: _URL_LOADER_VER_TATRISIT,
				        scripts  : true,
				        loadMask : true,
				        autoLoad : false,
				        ajaxOptions: {
				            method: 'POST'
				        }
				    }
				}
            ]
        },        
        {            
            title: 'CONTRATANTE',            
            itemId: 'tabDatosContratante',
            items:[{                
                items: [panelDatosContratante]
            }]
        },
        {
            title: 'ENDOSOS',
            itemId: 'tabEndosos',
            items:[
                gridDatosEndosos, 
                {
                    xtype  : 'panel',
                    name   : 'pnlDatosEndosos',
                    autoScroll : true,
                    loader: {
                        //url: _URL_LOADER_VER_ENDOSOS,
                        scripts  : true,
                        loadMask : true,
                        autoLoad : false,
                        ajaxOptions: {
                            method: 'POST'
                        }
                    }
                }
            ]
        },
        {
            title: 'E.C.D.',
            itemId: 'tabEnfermedades',
            items:[
                gridDatosEnfermedades, 
                {
                    xtype  : 'panel',
                    name   : 'pnlDatosEnfermedades',
                    autoScroll : true,
                    loader: {                        
                        scripts  : true,
                        loadMask : true,
                        autoLoad : false,
                        ajaxOptions: {
                            method: 'POST'
                        }
                    }
                }
            ]
        },
    	{
            itemId: 'tbDocumentos',
            title : 'DOCUMENTACION',
            width: '350',
            loader : {
                url : _URL_CONSULTA_DOCUMENTOS,
                scripts : true,
                autoLoad : false
            },
            listeners : {
                activate : function(tab) {
                    tab.loader.load({
                        params : {
                            'smap1.readOnly' :  true,
                            'smap1.nmpoliza' :  
                            panelBusqueda.down('form').getForm().findField("params.nmpoliza").getValue(),
                            'smap1.cdunieco' :  panelBusqueda.down('form').getForm().findField("params.cdunieco").getValue(),
                            'smap1.cdramo' :  panelBusqueda.down('form').getForm().findField("params.cdramo").getValue(),
                            'smap1.estado' :  panelBusqueda.down('form').getForm().findField("params.estado").getValue(),
                            'smap1.nmsuplem' :  panelBusqueda.down('form').getForm().findField("params.suplemento").getValue(),
                            'smap1.ntramite' : "",
                            'smap1.tipomov'  : '0'
                        }
                    });
                }
            }
        }, {
        	itemId: 'tbRecibos',
        	title: 'RECIBOS',
        	autoScroll: true,
        	loader: {
        		url: _URL_LOADER_RECIBOS,
        		scripts: true,
        		autoLoad: false
        	},
        	listeners: {
        		activate: function(tab) {
        			tab.loader.load({
        				params : {
                            'params.cdunieco': panelBusqueda.down('form').getForm().findField("params.cdunieco").getValue(),
                            'params.cdramo'  : panelBusqueda.down('form').getForm().findField("params.cdramo").getValue(),
                            'params.estado'  : panelBusqueda.down('form').getForm().findField("params.estado").getValue(),
                            'params.nmpoliza': panelBusqueda.down('form').getForm().findField("params.nmpoliza").getValue(),
                            'params.nmsuplem': panelBusqueda.down('form').getForm().findField("params.suplemento").getValue()
                        }
        			});
        		}
        	}
        }, {
            id:'tbHistorico',
            title: 'HISTORICO',
            autoScroll: true,
            items:[
                gridHistorico
            ],            
            listeners: {
            	activate: function(tab) {
            		storeHistorico.load({
            		  params: panelBusqueda.down('form').getForm().getValues()
            		});
            	}
            }
        }, {
            id:'tbHistoricoFarmacia',
            title: 'HISTORICO FARMACIA',
            autoScroll: true,
            items:[
                gridHistoricoFarmacia
            ],            
            listeners: {
                activate: function(tab) {
                    storeHistoricoFarmacia.load({
                      params: panelBusqueda.down('form').getForm().getValues(),
                      callback: function(records, operation, success) {
                            if (!success) {
                                showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                                return;
                            }
                            if(records.length == 0){
                                showMessage('No hay hist�rico de farmacia', 'Este asegurado no tiene hist�rico de farmacia', Ext.Msg.OK, Ext.Msg.INFO);
                                return;
                            }
                        }
                      
                      
                    });
                }
            }
        },{
            id:'tbVigencia',
            title: 'VIGENCIA',
            autoScroll: true,
            items:[
                gridVigencia
            ],            
            listeners: {
                activate: function(tab) {
                    storeVigencia.load({
                      params: panelBusqueda.down('form').getForm().getValues(),
                      callback: function(records, operation, success) {
                            if (!success) {
                                showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                                return;
                            }
                            if(records.length == 0){
                                showMessage('No hay periodos de vigencia', 'Este asegurado no tiene periodos de vigencia', Ext.Msg.OK, Ext.Msg.INFO);
                                return;
                            }
                        }
                    });
                }
            }
        }
        
        
        ]    
    });
    
    
    // SECCION DE INFORMACION (CUERPO PRINCIPAL)
    
    var panelBusqueda = Ext.create('Ext.Panel', {
        id:'main-panel',
        baseCls:'x-plain',
        renderTo: 'dvConsultasAsegurados',
        autoScroll:true,
        defaults: {frame:true, width:200, height: 200, margin : '2'},
        items:[{
            title:'B&Uacute;SQUEDA DE ASEGURADOS',
            id: 'subPanelBusqueda',
            width:990,
            height:190,
            items: [
                {
                    xtype: 'form',
                    url: _URL_CONSULTA_POLIZA_ACTUAL,
                    border: false,
                    layout: {
                        type:'hbox'
                    },
                    margin: '5',
                    defaults: {
                        bubbleEvents: ['change']
                    },
                    items : [
                        {
                            xtype: 'radiogroup',
                            name: 'groupTipoBusqueda',
                            flex: 15,
                            columns: 1,
                            vertical: true,
                            items: [                                
                            	/*{boxLabel: 'Por n\u00FAmero de p\u00F3liza', name: 'tipoBusqueda', inputValue: 1, checked: true, width: 160},*/                                
                                {boxLabel: 'Por clave de asegurado', name: 'tipoBusqueda', inputValue: 3, checked: true, width: 160},
                                {boxLabel: 'Por nombre', name: 'tipoBusqueda', inputValue: 4}
                                /*Se comenta esta b�squeda porque disminuye el performance.
                                 * ,{boxLabel: 'Por RFC del contratante', name: 'tipoBusqueda', inputValue: 2}
                                 * */
                                
                            ],
                            listeners : {
                                change : function(radiogroup, newValue, oldValue, eOpts) {
                                	Ext.getCmp('subpanelBusquedas').query('panel').forEach(function(c){c.hide();});
                                	//Ext.getCmp('subpanelBusquedas').query('textfield').forEach(function(c){c.setValue('');});
                                	//Limpiamos los par�metros
                                	this.up('form').getForm().findField('params.rfc').setValue('');
                                	this.up('form').getForm().findField('params.cdperson').setValue('');
                                	this.up('form').getForm().findField('params.nombre').setValue('');
                                	//Limpiamos los mensajes
                                	//Limpiamos mensajes:
                                    cambiaTextoMensajeAgente("");
                                    cambiaTextoMensajeAgente2("");
                                    cambiaTextoMensajeAgente3("");
                                    cambiaTextoMensajeAgente4("");
                                	
                                    switch (newValue.tipoBusqueda) {
                                        case 1:
                                            Ext.getCmp('subpanelBusquedaGral').show();                                            
                                            break;
                                        case 2:
                                            Ext.getCmp('subpanelBusquedaRFC').show();
                                            break;
                                        case 3:
                                            Ext.getCmp('subpanelBusquedaCodigoPersona').show();
                                            break;
                                        case 4:
                                            Ext.getCmp('subpanelBusquedaNombre').show();
                                            break;
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'tbspacer',
                            flex: 2.5
                        },
                        {
                        	id: 'subpanelBusquedas',	
                            layout : 'vbox',
                            align:'stretch',
                            flex: 70,
                            border: false,
                            items: [
                                {
                                    id: 'subpanelBusquedaGral',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    defaults: {
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [                                        
                                        {                                            
                                            xtype : 'hiddenfield',
                                            name : 'params.nmpoliex'
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.cdunieco'
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.cdramo'
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.estado'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.nmpoliza'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.suplemento'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.cdagente'
                                        },{
                                        	xtype: 'hiddenfield',
                                            name : 'params.icodpoliza'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.nmsituac'
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.cdsubram'
                                        }
                                    ]
                                },                                
                                {
                                    id: 'subpanelBusquedaCodigoPersona',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,                                    
                                    defaults: {
                                        labelAlign: 'right',
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            xtype: 'numberfield',
                                            name : 'params.cdperson',
                                            fieldLabel : 'Clave de asegurado',
                                            maxLength : 9,
                                            allowBlank: false,
                                            minValue: 0
                                        }
                                    ]
                                },
                                {
                                    id: 'subpanelBusquedaNombre',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    hidden: true,
                                    defaults: {
                                        labelAlign: 'right',
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            xtype: 'textfield',
                                            name : 'params.nombre',
                                            fieldLabel : 'Nombre',
                                            maxLength : 255,
                                            allowBlank: false
                                        }
                                    ]
                                },
                                {
                                    id: 'subpanelBusquedaRFC',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    hidden: true,
                                    defaults: {
                                        labelAlign: 'right',
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            xtype: 'textfield',
                                            name : 'params.rfc',
                                            fieldLabel : 'RFC',
                                            maxLength : 13,
                                            allowBlank: false
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype:'tbspacer',
                            flex: 2.5
                        },
                        {
                            xtype : 'button',
                            flex:10,
                            text: 'Buscar',
                            handler: function(btn, e) {
                            	//Limpiamos filtros del grid de resultados
                            	gridPolizasAsegurado.getStore().clearFilter();
                            	
                            	//Limpiamos mensajes:
                            	cambiaTextoMensajeAgente("");
                                cambiaTextoMensajeAgente2("");
                                cambiaTextoMensajeAgente3("");
                                cambiaTextoMensajeAgente4("");
                            	
                                var formBusqueda = this.up('form').getForm();
                                panelBusqueda.setLoading(true);
                                //Obtenemos el valor elegido en 'groupTipoBusqueda' para elegir el tipo de busqueda a realizar.
                                switch (formBusqueda.findField('groupTipoBusqueda').getValue().tipoBusqueda) {
                                    case 1:
                                        // Busqueda por Datos Generales de la poliza:
                                        if(!formBusqueda.findField('params.nmpoliex').isValid()){
                                            showMessage('', _MSG_NMPOLIEX_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        cargaStoreSuplementos(formBusqueda.getValues());
                                    break;
                                        
                                    case 2:
                                        var expAlfanumerica = /^[0-9a-zA-Z]+$/; 
                                        // Busqueda de polizas por RFC:
                                        if(!formBusqueda.findField('params.rfc').isValid()){
                                            showMessage('', _MSG_RFC_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        } else if(!formBusqueda.findField('params.rfc').getValue().match(expAlfanumerica)){
                                        	showMessage('', _MSG_RFC_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        //Se limpian los par�metros que no corresponden con el filtro                                        
                                        this.up('form').getForm().findField('params.cdperson').setValue('');
                                        this.up('form').getForm().findField('params.nombre').setValue('');
                                        cargaPolizasAsegurado(formBusqueda, btn);
                                    break;
                                    
                                    case 3:
                                    	// Busqueda de polizas por CDPERSON:
                                        if(!formBusqueda.findField('params.cdperson').isValid()){
                                            showMessage('', _MSG_CDPERSON_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        //Se limpian los par�metros que no corresponden con el filtro                                        
                                        this.up('form').getForm().findField('params.rfc').setValue('');                                        
                                        this.up('form').getForm().findField('params.nombre').setValue('');
                                        cargaPolizasAsegurado(formBusqueda, btn);
                                        
                                    break;
                                    
                                    case 4:
                                    	// Busqueda de polizas por nombre:
                                        if(!formBusqueda.findField('params.nombre').isValid()){
                                            showMessage('', _MSG_NOMBRE_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        //Se limpian los par�metros que no corresponden con el filtro                                        
                                        this.up('form').getForm().findField('params.rfc').setValue('');
                                        this.up('form').getForm().findField('params.cdperson').setValue('');                                        
                                        cargaPolizasAsegurado(formBusqueda, btn);
                                        
                                    break;
                                }                                
                                
                            }
                        }
                    ]
                },{
        	    	layout: 'column',
        	    	margin: '5',
        	    	border: false,
        	    	name: 'mensajeAgente',
        	    	html:''
        	     },{
                    layout: 'column',
                    margin: '5',
                    border: false,
                    name: 'mensajeAgente2',
                    html:''
                 },{
                    layout: 'column',
                    margin: '5',
                    border: false,
                    name: 'mensajeAgente3',
                    html:''
                 },{
                    layout: 'column',
                    margin: '5',
                    border: false,
                    name: 'mensajeAgente4',
                    html:''
                 }
            ]
        },{
        	layout: 'hbox',
        	frame:false,
        	border: 0,
        	width:990,
        	height: 410,
        	defaults: {
        		autoScroll:true,
        		frame:true,
        		height: 400
        	},
        	items: [{
                //Panel de la p�liza actual. Est� oculto pero de aqu� se toman datos. NO ELIMINAR ESTE COMPONENTE.
                title:'P&Oacute;LIZA',
                hidden:true,
                items : [
                    gridSuplementos
                ]
            },{
                //title:
                width: 135,
                style: 'margin-right:5px',
                items: [
                    listViewOpcionesConsulta
                ]
            },{
                //title:
                width:850,
                items : [
                    tabDatosGeneralesPoliza
                ]
            }]
        }]
    });
    
    //Asignamos los parametros que se pasan desde la url 
    panelBusqueda.down('form').getForm().findField("params.nmpoliex").setValue(_NMPOLIEX)
    panelBusqueda.down('form').getForm().findField("params.icodpoliza").setValue(_ICODPOLIZA)
    panelBusqueda.down('form').getForm().findField("params.cdperson").setValue(_CDPERSON)
    panelBusqueda.down('form').getForm().findField("params.nmsituac").setValue(_NMSITUAC)
    //Lanzar funcion de busqueda si se ha pasado el _CDPERSON (id del afiliado) como parametro desde la url
    if(!(_CDPERSON==='') && !(_CDPERSON===null)){
    	Ext.getCmp('subPanelBusqueda').hide();
    	cargaStoreSuplementos(panelBusqueda.down('form').getForm().getValues());
    }
    ////Hide elements
    if(tabDatosGeneralesPoliza.isVisible()) {
        tabDatosGeneralesPoliza.hide();
    }
    

    /**
    * 
    * @param String/Object
    */
    function cargaStoreSuplementos(params){
        
        debug('Params busqueda de suplemento: ',params);
        
        cargaStorePaginadoLocal(storeSuplementos, _URL_CONSULTA_POLIZA_ACTUAL, 'datosPolizaActual', params, function (options, success, response){
    		if(success){
                var jsonResponse = Ext.decode(response.responseText);
                
                if(!jsonResponse.success) {
                	showMessage(_MSG_ERROR, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.ERROR);
                    return;
                }
                
                gridSuplementos.setLoading(false);
                //gridSuplementos.getView().el.focus();
                
                //Limpiar seleccion de la lista de opciones de consulta
                limpiaSeleccionTiposConsulta();
                                
                //Preselecciona autom�ticamente las opciones.
                gridSuplementos.getSelectionModel().select(0);
                listViewOpcionesConsulta.getSelectionModel().select(0);
            }else{
                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
            }
    	}, gridSuplementos);
        
    }

    // FUNCION PARA OBTENER RECIBOS DEL AGENTE
        
    function limpiaSeccionInformacionPrincipal() {
        panelDatosPoliza.getForm().reset();
        panelDatosAsegurado.getForm().reset();
        panelDatosTitular.getForm().reset();
        panelDatosContratante.getForm().reset();
        panelDatosComplementarios.getForm().reset();
        panelDatosPlan.getForm().reset();        
        gridCopagosPoliza.getStore().removeAll();
        gridCoberturas.getStore().removeAll();
        gridCoberturasBasicas.getStore().removeAll();
        gridDatosAsegurado.getStore().removeAll();
        gridDatosEndosos.getStore().removeAll();
        gridDatosEnfermedades.getStore().removeAll();
        //Limpiamos seccion de Datos de tatrisit:
        tabDatosGeneralesPoliza.down('panel[name=pnlDatosTatrisit]').setTitle('');
        tabDatosGeneralesPoliza.down('panel[name=pnlDatosTatrisit]').update('');
        tabDatosGeneralesPoliza.setActiveTab(0);        
        tabDatosGeneralesPoliza.hide();        
    }
    
    function limpiaSeleccionTiposConsulta() {
        listViewOpcionesConsulta.getSelectionModel().deselectAll();
        listViewOpcionesConsulta.collapse('top', false);
    }

    function cambiaTextoMensajeAgente(texto) {
    	if(!Ext.isEmpty(texto)){
    		var color="";
    		var mensaje="";
    		if(texto == "VIGENTE"){
    			color = "#009900";
    			mensaje = "AUTORIZADO PARA PAGO DIRECTO";    		  
    		} else if(texto == "PENDIENTE"){
    			color = "#E96707";
    			mensaje = "OTORGAR SERVICIO PAGO VIA REEMBOLSO"    		  
    		} else {
    			color = "#FF0000";
    			mensaje = "NEGAR SERVICIO POR NUESTRA PARTE";
    		}
    		panelBusqueda.down('[name=mensajeAgente]').update('<span style="color:' + color + ';font-size:14px;font-weight:bold;text-decoration:underline;">'+ mensaje +'</span>');
    	}else {
    		panelBusqueda.down('[name=mensajeAgente]').update('<span></span>');
    	}
    }
    
    function cambiaTextoMensajeAgente2(texto) {
        if(!Ext.isEmpty(texto)){
            panelBusqueda.down('[name=mensajeAgente2]').update('<span style="color:#E96707;font-size:14px;font-weight:bold;">ESTATUS ASEGURADO: '+texto+'</span>');
        }else {
            panelBusqueda.down('[name=mensajeAgente2]').update('<span></span>');
        }
    }
    
    function cambiaTextoMensajeAgente3(texto) {
        if(!Ext.isEmpty(texto)){        	
        	if(texto.substring(0,3) == "DXN"){
                panelBusqueda.down('[name=mensajeAgente3]').update('<span style="color:#E96707;font-size:14px;font-weight:bold;">DXN</span>');
        	} else {
        		panelBusqueda.down('[name=mensajeAgente3]').update('<span></span>');
        	}
        }else {
            panelBusqueda.down('[name=mensajeAgente3]').update('<span></span>');
        }
    }
    
    function cambiaTextoMensajeAgente4(texto) {
        if(!Ext.isEmpty(texto)){
            panelBusqueda.down('[name=mensajeAgente4]').update('<span style="color:#E96707;font-size:14px;font-weight:bold;">'+texto+'</span>');
        }else {
            panelBusqueda.down('[name=mensajeAgente4]').update('<span></span>');
        }
    }
    
    function cargaPolizasAsegurado(formBusqueda, btn) {
    	gridPolizasAsegurado.down('pagingtoolbar').moveFirst();
    	var callbackGetPolizasAsegurado = function(options, success, response) {
            if(success){
                var jsonResponse = Ext.decode(response.responseText);
                if(jsonResponse.resultadosAsegurado && jsonResponse.resultadosAsegurado.length == 0) {
                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                    panelBusqueda.setLoading(false);
                    return;
                }
                panelBusqueda.setLoading(false);
                windowPolizas.animateTarget= btn;
                windowPolizas.show();
            }else{
                showMessage('Error', 'Error al obtener las p&oacute;lizas, intente m\u00E1s tarde.', 
                    Ext.Msg.OK, Ext.Msg.ERROR);
            }
        }
        
        //console.log('Params busqueda de polizas por RFC=');console.log(formBusqueda.getValues());
        cargaStorePaginadoLocal(storePolizaAsegurado, 
            _URL_CONSULTA_RESULTADOS_ASEGURADO, 
            'resultadosAsegurado', 
            formBusqueda.getValues(), 
            callbackGetPolizasAsegurado);
    }
    
});