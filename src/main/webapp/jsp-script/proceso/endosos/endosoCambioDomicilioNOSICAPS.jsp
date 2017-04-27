<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	var _CONTEXT = '${ctx}';
	var clienteSeleccionado             = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
	var _URL_OBTIENE_CLIENTE      		= '<s:url namespace="/persona" action="obtieneInformacionCliente" />';
	var _URL_OBTEN_CATALOGO_GENERICO	= '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
	var _URL_ObtieneDomicilioAseg   	= '<s:url namespace="/catalogos" action="obtenerDomicilioPorCdperson" />';
	var _UrlImportaPersonaWS 			= '<s:url namespace="/catalogos" action="importaPersonaExtWSNoSicaps" />';
	var _URL_CATALOGOS 					= '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
	var _CATALOGO_SUCURSALES			= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
	
	
	var _35_urlGuardar     				= '<s:url namespace="/endosos" action="guardarEndosoDomicilioNoSICAPS"       />';
	var _35_panelPri;
	var cdpersonNuevo;
	var codigoCliExterno;
	var pantallaPrincipal = clienteSeleccionado.tipoPantalla;
	var primerCarga = 0;
	var recordCargaDomi;
	
	var endDomNoSicapsFlujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
    debug('endDomNoSicapsFlujo:',endDomNoSicapsFlujo);
	
	
	Ext.onReady(function() {
		
		Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
		
		Ext.define('modelListadoAsegurado',{
			extend: 'Ext.data.Model',
			fields: [
				{type:'string',    name:'CPCLIENTE'			},			{type:'string',    name:'CALLECLIENTE'		},			{type:'string',    name:'NUMCLIENTE'		},
				{type:'string',    name:'CVEEDOSIGS'		},			{type:'string',    name:'CVEMUNSIGS'		},			{type:'string',    name:'COLCLIENTE'		},
				{type:'string',    name:'POBLACION'			},			{type:'string',    name:'NUMEXTERIOR'		},			{type:'string',    name:'NUMINTERIOR'		},
				{type:'string',    name:'ESTADOASEG'		},			{type:'string',    name:'CDEDOICE'			},			{type:'string',    name:'CDCOLONICE'		},
				{type:'string',    name:'TELEFONO1'			},			{type:'string',    name:'TELEFONO2'			},			{type:'string',    name:'TELEFONO3'			}
			]
		});

		storeListadoAsegurado = new Ext.data.Store({
			autoDestroy: true,						model: 'modelListadoAsegurado'
		});
		
		sucursalCliente = Ext.create('Ext.data.Store', {
			model:'Generic',
			autoLoad:true,
			proxy: {
				type: 'ajax',
				url:_URL_CATALOGOS,
				extraParams : {
					catalogo:_CATALOGO_SUCURSALES,
					'params.idPadre' : '0'
				},
				reader: {
					type: 'json',
					root: 'lista'
				}
			}
		});
		
		sucursalCliente.load();
		
	    var storeEstados = Ext.create('Ext.data.Store', {
	        model     : 'Generic',
	        proxy     : {
	            type  : 'ajax',
	            url   : _URL_OBTEN_CATALOGO_GENERICO,
				extraParams : {
					catalogo : 'TATRISIT',
					'params.cdatribu' : '4',
					'params.cdtipsit' : 'SL'
				},
	            reader     :
	            {
	                type  : 'json'
	                ,root : 'lista'
	            }
	        },
	        listeners: {
	            load: function (){}   
	        }
	    });

		var storeMunici = Ext.create('Ext.data.Store', {
	        model     : 'Generic',
	        proxy     : {
	            type  : 'ajax',
	            url   : _URL_OBTEN_CATALOGO_GENERICO,
				extraParams : {
					catalogo : 'TATRISIT',
					'params.cdatribu' : '17',
					'params.cdtipsit' : 'SL'
				},
	            reader     : {
	                type  : 'json'
	                ,root : 'lista'
	            }
	        },
	        listeners: {
	            load: function (){}   
	        }
	    });

	    var storeColonia = Ext.create('Ext.data.Store', {
	        model     : 'Generic',
	        proxy     : {
	            type  : 'ajax',
	            url   : _URL_OBTEN_CATALOGO_GENERICO,
	            extraParams : {
					catalogo : 'COLONIAS'
				},
	            reader     : {
	                type  : 'json'
	                ,root : 'lista'
	            }
	        },
	        listeners: {
	            load: function (){}   
	        }
	    });
		
	    storeDomicilio = Ext.create('Ext.data.JsonStore', {
	    	model:'modeloDomicil',
	        proxy: {
	            type: 'ajax',
	            url: _URL_ObtieneDomicilioAseg,
	            reader: {
	                type: 'json',
	                root: 'smap1'
	            }
	        }
	    });
	    
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		
		var cmbSucursal = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel	: 'Sucursal',			name			:'cmbSucursal',				allowBlank		: false,
			editable	: true,					displayField	:'value',
			emptyText	:'Seleccione...',		valueField		:'key',						forceSelection	: true,
			queryMode	:'local',				store 			:sucursalCliente,			readOnly   : (pantallaPrincipal =="0")
		});
		
		var panelInicialPral = Ext.create('Ext.form.Panel', {
			title: 'Datos de la P&oacute;liza',
			id: 'panelInicialPral',
			bodyPadding: 5,
			defaultType: 'textfield',
			layout      : {
				type    : 'table'
				,columns: 3
			},
			defaults 	: {
				style   : 'margin:5px;'
			},
			items: [
				cmbSucursal,
				{       fieldLabel	: 'Ramo',		        name		: 'cveRamo',		        allowBlank	: false, 		type: 'numberfield',	readOnly   : (pantallaPrincipal =="0")	},
				{       fieldLabel	: 'P&oacute;liza',      name		: 'nmPoliza',		        allowBlank	: false,		readOnly   : (pantallaPrincipal =="0")	}
			]
			,buttonAlign:'center'
			,buttons: [{
				text: 'Buscar Contratante'
				,icon:'${ctx}/resources/fam3icons/icons/accept.png'
				,buttonAlign : 'center'
				,handler: function() {
					storeListadoAsegurado.removeAll();
					datosContratante.down('[name=smap1.CODPOSTAL]').setValue('');					
					datosContratante.down('[name=smap1.CDEDO]').setValue('');
					datosContratante.down('[name=smap1.CDMUNICI]').setValue('');
        			datosContratante.down('[name=smap1.CDCOLONI]').setValue('');
        			datosContratante.down('[name=smap1.CALLE]').setValue('');
        			datosContratante.down('[name=smap1.NUMEXT]').setValue('');
        			datosContratante.down('[name=smap1.NUMINT]').setValue('');
        			datosContratante.down('[name=smap1.TELEFONO1]').setValue('');
        			datosContratante.down('[name=smap1.TELEFONO2]').setValue('');
        			datosContratante.down('[name=smap1.TELEFONO3]').setValue('');
        			
					var form = this.up('form').getForm();
					if (form.isValid()){
						Ext.Ajax.request({
							url     : _URL_OBTIENE_CLIENTE
							,params : {
								'params.sucursal'  : panelInicialPral.down('combo[name=cmbSucursal]').getValue()
								,'params.ramo'      : panelInicialPral.down('[name=cveRamo]').getValue()
								,'params.poliza'    : panelInicialPral.down('[name=nmPoliza]').getValue()
							}
							,success : function(response) {
								var jsonCliente =Ext.decode(response.responseText);
								debug ("jsonCliente ===> ",jsonCliente);
								if(jsonCliente.success!=true){
									centrarVentanaInterna(mensajeError(jsonCliente.message));
								}else{
									var desTipoPersona = null;
									Ext.Ajax.request({
										url       : _UrlImportaPersonaWS
										,params: {
										'params.esSalud':  "D",
										'params.codigoCliExt':  jsonCliente.list[0].CVEEXTERNA
										}
										,success  : function(response){
											var json = Ext.decode(response.responseText);
											//debug('response text de la importacion de persona :',json);
											if(json.exito){
												debug("Valores de recuperacion ==> ",json.params.cdpersonNuevo, json.params.codigoCliExt);
												cdpersonNuevo = json.params.cdpersonNuevo;
												codigoCliExterno  = json.params.codigoCliExt;
												
												Ext.Ajax.request({
													url     : _URL_ObtieneDomicilioAseg
													,params : {
														'smap1.cdperson' : cdpersonNuevo
													}
													,success : function(response) {
														var jsonICE = Ext.decode(response.responseText);
														debug("Valor del json de respuesta del asegurado en ICE ==> ",jsonICE);
														if(jsonICE.success!=true){
															centrarVentanaInterna(mensajeError(jsonICE.message));
														}else{
															storeListadoAsegurado.removeAll();
															var rec = new modelListadoAsegurado({
																CPCLIENTE       : jsonCliente.list[0].CPCLIENTE,
																CALLECLIENTE    : jsonCliente.list[0].CALLECLIENTE,
																NUMCLIENTE      : jsonCliente.list[0].NUMCLIENTE,
																CVEEDOSIGS      : jsonCliente.list[0].CVEEDOSIGS,
																CVEMUNSIGS      : jsonCliente.list[0].CVEMUNSIGS,
																COLCLIENTE      : jsonCliente.list[0].COLCLIENTE,
																POBLACION       : jsonCliente.list[0].POBLACION,
																NUMEXTERIOR     : jsonICE.smap1.NMNUMERO,
																NUMINTERIOR     : jsonICE.smap1.NMNUMINT,
																ESTADOASEG      : jsonICE.smap1.ESTADO,
																CDEDOICE        : jsonICE.smap1.CDEDO,
																CDCOLONICE      : jsonICE.smap1.CDCOLONI,
																TELEFONO1       : jsonCliente.list[0].TELEFONO1,
																TELEFONO2      	: jsonCliente.list[0].TELEFONO2,
																TELEFONO3      	: jsonCliente.list[0].TELEFONO3
															});
															storeListadoAsegurado.add(rec);
														}
													}
													,failure : errorComunicacion
												});
											}else{
												mensajeError("Error al Editar Cliente, vuelva a intentarlo.");
												storeListadoAsegurado.removeAll();
											}
										}
										,failure  : function(){
											errorComunicacion(null,'En importar persona. Consulte a soporte.');
											storeListadoAsegurado.removeAll();
										}
									});
								}
							}
							,failure : errorComunicacion
						});
					}else{
						Ext.Msg.show({
							title:'Datos incompletos',
							msg: 'Favor de introducir todos los campos requeridos',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.WARNING
						});
					}
				}
			}]
		});
		
		gridDatos = Ext.create('Ext.grid.Panel',{
			id             : 'clausulasGridId'
			,store         : storeListadoAsegurado
			,title		   : 'Datos del Contratante'
			,style         : 'margin:5px'
			,height		   : 150
			,columns       : [
				{	header  : 'C&oacute;digo Postal',	dataIndex : 'CPCLIENTE',	flex:2 },
				{	header  : 'Domicilio',				dataIndex : 'CALLECLIENTE',	flex:2 },
				{	header  : 'No. Exterior',			dataIndex : 'NUMEXTERIOR',	flex:2 },
				{	header  : 'No. Interior',			dataIndex : 'NUMINTERIOR',	flex:2 },
				{	header  : 'Cve. Edo',				dataIndex : 'CVEEDOSIGS',	flex:2,  hidden: true	},
				{	header  : 'Cve. Mun',				dataIndex : 'CVEMUNSIGS',	flex:2,  hidden: true	},
				{	header  : 'Edo. ICE',				dataIndex : 'CDEDOICE',		flex:2,  hidden: true	},
				{	header  : 'Col. ICE',				dataIndex : 'CDCOLONICE',	flex:2,  hidden: true	}, 
				{	header  : 'Colonia',				dataIndex : 'COLCLIENTE',	flex:2 },
				{	header  : 'Estado',					dataIndex : 'ESTADOASEG',	flex:2 },
				{	header  : 'Municipio',				dataIndex : 'POBLACION',	flex:2 },
				{	header  : 'Tel&eacute;fono 1',		dataIndex : 'TELEFONO1',	flex:2 },
				{	header  : 'Tel&eacute;fono 2',		dataIndex : 'TELEFONO2',	flex:2 },
				{	header  : 'Tel&eacute;fono 3',		dataIndex : 'TELEFONO3',	flex:2 }
			],
			listeners: {
				itemclick: function(dv, record, item, index, e) {
					
					datosContratante.down('[name=smap1.CODPOSTAL]').setValue(record.get('CPCLIENTE'));					
					storeEstados.load({
						params : {
							'params.idPadre' : record.get('CPCLIENTE')
						},
						callback : function(records,operation,success) {}
					});
					
					datosContratante.down('[name=smap1.CDEDO]').setValue(record.get('CDEDOICE'));
					storeMunici.load({
						params : {
							'params.idPadre' : record.get('CDEDOICE')
						}
					});
        			datosContratante.down('[name=smap1.CDMUNICI]').setValue(record.get('CPCLIENTE')+""+record.get('CVEEDOSIGS')+""+record.get('CVEMUNSIGS'));
        			datosContratante.down('[name=smap1.CDCOLONI]').setValue(record.get('CDCOLONICE'));
        			datosContratante.down('[name=smap1.CALLE]').setValue(record.get('CALLECLIENTE'));
        			datosContratante.down('[name=smap1.NUMEXT]').setValue(record.get('NUMEXTERIOR'));
        			datosContratante.down('[name=smap1.NUMINT]').setValue(record.get('NUMINTERIOR'));
        			datosContratante.down('[name=smap1.TELEFONO1]').setValue(record.get('TELEFONO1'));
        			datosContratante.down('[name=smap1.TELEFONO2]').setValue(record.get('TELEFONO2'));
        			datosContratante.down('[name=smap1.TELEFONO3]').setValue(record.get('TELEFONO3'));
				}
			}
		});
		
		var datosContratante = Ext.create('Ext.form.Panel',{
			id          : 'datosContratante',
			bodyPadding : 5,
			title: 'Cambio del contratante',
			defaults 	: {
				style   : 'margin:5px;'
			},
			items       :[
				{
                    xtype     : 'textfield',          fieldLabel : 'C&oacute;digo Postal',               name: 'smap1.CODPOSTAL',
                    allowBlank: false,                width		 : 250,
                    listeners : {
                    	change: function(cmp,newVal){
                    		primerCarga ++;
                    		if(!Ext.isEmpty(newVal) && (new String(newVal)).length > 4 ){
                    			storeEstados.load({
									params : {
										'params.idPadre' : newVal
									},
									callback : function(records,operation,success) {}
								});
								
								storeColonia.load({
									params : {
										'params.cp' : newVal
									},
									callback : function(records,operation,success) {
										if(primerCarga == 1 && recordCargaDomi){
											_fieldByName('smap1.CDMUNICI',panelDomici).setValue(recordCargaDomi.get('smap1.CDMUNICI'));
										}
									}
								});
                    		}
                    	}
                    }
                },{
                    xtype         : 'combobox',        name          : 'smap1.CDEDO',                   fieldLabel    : 'Estado',
                    valueField    : 'key',             displayField  : 'value',		                    allowBlank    : false,
                    width		  : 250,               forceSelection: true,		                    queryMode     :'local',
                    store         : storeEstados,
                    listeners: {
                       select: function ( combo, records, eOpts ){},
                       change: function(combo,newVal){
                    		if(!Ext.isEmpty(newVal)){
                    			storeMunici.load({
									params : {
										'params.idPadre' : newVal
									},
									callback : function(records,operation,success) {
										if(primerCarga == 1 && recordCargaDomi){
											_fieldByName('smap1.CDMUNICI',panelDomici).setValue(recordCargaDomi.get('smap1.CDMUNICI'));
										}
									}
								});
                    		}
                    	}
                   }
                },{
                    xtype         : 'combobox',         name          : 'smap1.CDMUNICI',               fieldLabel    : 'Municipio',
                    valueField    : 'key',              displayField  : 'value',		                forceSelection: true,
                    queryMode     :'local',             allowBlank    : false,		                    width		  : 400,
                    store         : storeMunici,
                   	listeners: {
                       	select: function ( combo, records, eOpts ){},
                       	change: function(cmb,newVal){}
                   	}
               	},{
                    xtype         : 'combobox',         name          : 'smap1.CDCOLONI',                fieldLabel    : 'Colonia',
                    valueField    : 'key',              displayField  : 'value',		                 allowBlank    : false,
                    width		  : 400,                forceSelection: true,		                     queryMode     :'local',
                    store         : storeColonia,
                   listeners: {
                       select: function ( combo, records, eOpts ){},
                       change: function(cmb,newVal){}
                   }
                },{
                	xtype       : 'textfield',			name        : 'smap1.CALLE',					fieldLabel		: 'Calle',
					width		 : 400,					allowBlank      : false
				},{
                	xtype       : 'textfield',			name        : 'smap1.NUMEXT',					fieldLabel		: 'N&uacute;m. Ext.',				
					width		 : 400,					allowBlank      : false
				},{
                	xtype       : 'textfield',			name        : 'smap1.NUMINT',					fieldLabel		: 'N&uacute;m. Int.',				
					width		 : 400
				},{
                	xtype       : 'textfield',			name        : 'smap1.TELEFONO1',				fieldLabel		: 'Tel&eacute;fono 1',				
					width		 : 400
				},{
                	xtype       : 'textfield',			name        : 'smap1.TELEFONO2',				fieldLabel		: 'Tel&eacute;fono 2',				
					width		 : 400
				},{
                	xtype       : 'textfield',			name        : 'smap1.TELEFONO3',				fieldLabel		: 'Tel&eacute;fono 3',				
					width		 : 400
				}
			]
		});

		_35_panelPri=Ext.create('Ext.panel.Panel',{
			renderTo     : 'maindivHist'
			, id : '_35_panelPri'
			,defaults    : {
				style : 'margin : 5px;'
			}
			,items       : [
				panelInicialPral
				,gridDatos
				,datosContratante
			]
			,buttonAlign : 'center'
			,buttons     : [
				{
					text      : 'Confirmar endoso'
					,icon     : '${ctx}/resources/fam3icons/icons/key.png'
					,handler: function() {
						var formPanel = this.up().up();
						debug(datosContratante.isValid());
						if(datosContratante.isValid()){
							var submitValues={};
							
							//datosContratante.down('[name=smap1.CDMUNICI]').getRawValue();
							var submitValues={};
							params = {
								'codPostal'			: datosContratante.down('[name=smap1.CODPOSTAL]').getValue(),
								'cveEdo'			: datosContratante.down('[name=smap1.CDEDO]').getValue(),
								'desEdo'			: datosContratante.down('[name=smap1.CDEDO]').getRawValue(),
								'cveMunicipio'		: datosContratante.down('[name=smap1.CDMUNICI]').getValue(),
								'desMunicipio'		: datosContratante.down('[name=smap1.CDMUNICI]').getRawValue(),
								'cveColonia'		: datosContratante.down('[name=smap1.CDCOLONI]').getValue(),
								'desColonia'		: datosContratante.down('[name=smap1.CDCOLONI]').getRawValue(),
								'desCalle'			: datosContratante.down('[name=smap1.CALLE]').getValue(),
								'numExterior'		: datosContratante.down('[name=smap1.NUMEXT]').getValue(),
								'numInterior'		: datosContratante.down('[name=smap1.NUMINT]').getValue(),
								'sucursalEntrada'   : panelInicialPral.down('combo[name=cmbSucursal]').getValue(),
								'ramoEntrada'       : panelInicialPral.down('[name=cveRamo]').getValue(),
								'polizaEntrada'     : panelInicialPral.down('[name=nmPoliza]').getValue(),
								'tipoPantalla'      : clienteSeleccionado.tipoPantalla,
								'cdpersonNuevo'     : cdpersonNuevo,
								'codigoCliExterno'  : codigoCliExterno,
								'telefono1'		    : datosContratante.down('[name=smap1.TELEFONO1]').getValue(),
								'telefono2'	        : datosContratante.down('[name=smap1.TELEFONO2]').getValue(),
								'telefono3'     	: datosContratante.down('[name=smap1.TELEFONO3]').getValue()
							}
							submitValues['smap1']= params;
							
							
							if(!Ext.isEmpty(endDomNoSicapsFlujo))
                            {
							    submitValues['flujo'] = endDomNoSicapsFlujo;
                            }
							
							debug("Valor a enviar ==>",submitValues);
							Ext.Ajax.request( {
								url: _35_urlGuardar,
								jsonData: Ext.encode(submitValues),
								success:function(response,opts){
									panelInicialPral.setLoading(false);
									var jsonResp = Ext.decode(response.responseText);
									var callbackRemesa = function(){
										marendNavegacion(2);
									};
									if(jsonResp.success==false){
										Ext.Msg.show({
											title:'Endoso',
											msg: jsonResp.respuesta,
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING
										});
									}else{
										mensajeCorrecto("Endoso",jsonResp.respuesta,function(){
											if(pantallaPrincipal =="1"){
												var numRand=Math.floor((Math.random()*100000)+1);
												var windowVerDocu=Ext.create('Ext.window.Window', {
													title          : 'ENDOSOS P&Oacute;LIZA NO SICAPS'
													,width         : 700
													,height        : 500
													,collapsible   : true
													,titleCollapse : true
													,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
																		+'src='+clienteSeleccionado.rutaPDF+'?'+panelInicialPral.down('combo[name=cmbSucursal]').getValue()
																		+','+panelInicialPral.down('[name=cveRamo]').getValue()+','+panelInicialPral.down('[name=nmPoliza]').getValue()
																		+',,0,'+jsonResp.smap2.numEndosoSIGS+',0>'
																	+'</iframe>'
													,listeners     : {
														resize : function(win,width,height,opt){
															$('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
														},
														close:function(){
															if(true){
																cdpersonNuevo = 0;
																codigoCliExterno = 0;
																_generarRemesaClic2(
																	true
																	,panelInicialPral.down('combo[name=cmbSucursal]').getValue()
																	 ,panelInicialPral.down('[name=cveRamo]').getValue()
																	 ,'M'
																	 ,panelInicialPral.down('[name=nmPoliza]').getValue()
																	 ,callbackRemesa
																);
																Ext.create('Ext.form.Panel').submit(
																{
																	url		: _CONTEXT+'/seguridad/accesoDirecto.action?codigoCliente=6442&codigoRol=EJECUTIVOCUENTA&params.acceso=endosoDomicilioNOSICAPS&user=biosnet1'
																	,standardSubmit : true
																});
															}
														}
													}
												}).show();
												windowVerDocu.center();
											}
										});
									}
								},
								failure:function(response,opts){
									panelInicialPral.setLoading(false);
									Ext.Msg.show({
										title:'Error',
										msg: 'Error de comunicaci&oacute;n',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR
									});
								}
							});


						}else {
							myMask.hide();
							Ext.Msg.show({
								title: 'Aviso',
								msg: 'Complete la informaci&oacute;n requerida',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.WARNING
							});
						}
					}
				}
			]
		});
	});
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindivHist" style="height:500px;"></div>