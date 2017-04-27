<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	var _CONTEXT = '${ctx}';
	
	var clienteSeleccionado  = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
	var _endCli_flujo        = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
	
	var _URL_OBTIENE_CLIENTE = '<s:url namespace="/persona"   action="obtieneInformacionCliente"   />';
	var _URL_CATALOGOS 		 = '<s:url namespace="/catalogos" action="obtieneCatalogo"             />';
	var _35_urlGuardar     	 = '<s:url namespace="/endosos"   action="guardarEndosoNombreRFCFecha" />';
	var _UrlImportaPersonaWS = '<s:url namespace="/catalogos" action="importaPersonaExtWSNoSicaps" />';
	
	var _CATALOGO_SUCURSALES = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
	
	var _35_panelPri;
	var cdpersonNuevo;
	var codigoCliExterno;
	var pantallaPrincipal = clienteSeleccionado.tipoPantalla;
	
	var intNombre1  = 0;
	var intNombre2  = 0;
	var intnomCompl = 0;
	var intappM     = 0;
	var intappP     = 0;
	var intfecha    = 0;
	var intRazonS   = 0;
	var tipoPerGral = null;
	
	debug('clienteSeleccionado:',clienteSeleccionado);
	
	debug('_endCli_flujo:',_endCli_flujo);
	
	debug("pantallaPrincipal ===>", pantallaPrincipal);
	Ext.onReady(function() {
		
		Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
		
		Ext.define('modelListadoAsegurado',{
			extend: 'Ext.data.Model',
			fields: [
				{type:'string',    name:'CVECLIENSIGS'		},			{type:'string',    name:'SUCEMISORA'		},			{type:'string',    name:'TIPERSONA'			},
				{type:'string',    name:'NOMCLIENTE'		},			{type:'string',    name:'NOMCLIENTE1'		},			{type:'string',    name:'NOMCLIENTE2'		},
				{type:'string',    name:'APPATERNO'			},			{type:'string',    name:'APMATERNO'			},			{type:'string',    name:'RAZONSOCIAL'		},
				{type:'string',    name:'ANECLI'			},			{type:'string',    name:'RFCCLIENTE'		},			{type:'string',    name:'CVEIFE'			},
				{type:'string',    name:'CURP'				},			{type:'string',    name:'SEXO'				},			{type:'string',    name:'CVEEDOCIVIL'		},
				{type:'string',    name:'CALLECLIENTE'		},			{type:'string',    name:'NUMCLIENTE'		},			{type:'string',    name:'CPCLIENTE'			},
				{type:'string',    name:'COLCLIENTE'		},			{type:'string',    name:'CVEMUNSIGS'		},			{type:'string',    name:'POBLACION'			},
				{type:'string',    name:'CVEEDOSIGS'		},			{type:'string',    name:'FECNACIMIENTO'		},			{type:'string',    name:'NACEXT'			},
				{type:'string',    name:'CVEOCUPACION'		},			{type:'string',    name:'CVEGIRO'			},			{type:'string',    name:'TELEFONO1'			},
				{type:'string',    name:'TELEFONO2'			},			{type:'string',    name:'TELEFONO3'			},			{type:'string',    name:'EMAIL'				},
				{type:'string',    name:'PAGINAWEB'			},			{type:'string',    name:'CANCON'			},			{type:'string',    name:'FUENTEINGRESO'		},
				{type:'string',    name:'TIPADMINISTRACION'	},			{type:'string',    name:'CARGOPUBL'			},			{type:'string',    name:'NOMCAR'			},
				{type:'string',    name:'PERCAR'			},			{type:'string',    name:'APOCLI'			},			{type:'string',    name:'DOMORI'			},
				{type:'string',    name:'NUMPAS'			},			{type:'string',    name:'CVEUSERCAPTURA'	},			{type:'string',    name:'CVEUSERAUTORIZA'	},
				{type:'string',    name:'FECALTA'			},			{type:'string',    name:'FECHACTUALIZACION'	},			{type:'string',    name:'ESTATUCLIENTE'		},
				{type:'string',    name:'DESTIPERSONA'		},			{type:'string',    name:'CVEEXTERNA'		}
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
		
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		
		var cmbSucursal = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel	: 'Sucursal',			name			:'cmbSucursal',				allowBlank		: false,
			editable	: true,					displayField	:'value',
			emptyText	:'Seleccione...',		valueField		:'key',						forceSelection	: true,
			queryMode	:'local',				store 			:sucursalCliente,			readOnly   : (pantallaPrincipal =="0")
		});
		
		function validaCamposRequerido(tipoPersona){
			if(tipoPersona =="F"){
				datosContratante.down('[name=dsapellido]').allowBlank =false;
				datosContratante.down('[name=dsapellido1]').allowBlank =false;
				datosContratante.down('[name=dsnomCompleto]').allowBlank =false;
				datosContratante.down('[name=dsRazonSocial]').allowBlank =true;
				datosContratante.down('[name=dsapellido]').show();
				datosContratante.down('[name=dsapellido1]').show();
				
				if(pantallaPrincipal =="1"){
					datosContratante.down('[name=dsnomCompleto]').show();
				}else{
					datosContratante.down('[name=dsnomCompleto]').hide();
				}
				datosContratante.down('[name=dsRazonSocial]').hide();
			}else{
				datosContratante.down('[name=dsapellido]').allowBlank =true;
				datosContratante.down('[name=dsapellido1]').allowBlank =true;
				datosContratante.down('[name=dsnomCompleto]').allowBlank =true;
				datosContratante.down('[name=dsRazonSocial]').allowBlank =false;
				datosContratante.down('[name=dsapellido]').hide();
				datosContratante.down('[name=dsapellido1]').hide();
				datosContratante.down('[name=dsnomCompleto]').hide();
				datosContratante.down('[name=dsRazonSocial]').show();
				
				if(pantallaPrincipal =="0"){
					datosContratante.down('[name=dsnombre]').hide();
					datosContratante.down('[name=dsnombre1]').hide();
					datosContratante.down('[name=dsnombre]').allowBlank =true;
					datosContratante.down('[name=dsnombre1]').allowBlank =true;
				}
			}
		}
		
		function validaRFCDatosRequeridos(tipoPersona,nombre1,nombre2,nombreCompleto,razonSocial,appPaterno,appMaterno, fechaNacimiento){
			if(tipoPersona == "F"){
				datosContratante.down('[name=rfcSugerido]').setText('RFC Sugerido : '+generaRFC(tipoPersona, nombreCompleto, null, appPaterno,appMaterno, fechaNacimiento));
			}else{
				datosContratante.down('[name=rfcSugerido]').setText('RFC Sugerido : '+generaRFC(tipoPersona, razonSocial, null, null,null, fechaNacimiento));
			}
		}
		
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
			//El boton unicante aparecera para polizas NO SICAPS
			,buttons: [{
				text: 'Buscar Contratante'
				,icon:'${ctx}/resources/fam3icons/icons/accept.png'
				,buttonAlign : 'center'
				, hidden : (pantallaPrincipal =="0")
				,handler: function() {
					storeListadoAsegurado.removeAll();
					intNombre1  = 0;
					intNombre2  = 0;
					intnomCompl = 0;
					intappM     = 0;
					intappP     = 0;
					intfecha    = 0;
					intRazonS   = 0;
					datosContratante.down('[name=dsnombre]').setValue('');
					datosContratante.down('[name=dsnombre1]').setValue('');
					datosContratante.down('[name=dsapellido]').setValue('');
					datosContratante.down('[name=dsapellido1]').setValue('');
					datosContratante.down('[name=fenacini]').setValue('');
					datosContratante.down('[name=rfcContratante]').setValue('');
					datosContratante.down('[name=dsnomCompleto]').setValue('');
					datosContratante.down('[name=dsRazonSocial]').setValue('');
					datosContratante.down('[name=rfcSugerido]').setText('');
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
								var json=Ext.decode(response.responseText);
								if(json.success!=true){
									centrarVentanaInterna(mensajeError(json.message));
								}else{
									var desTipoPersona = null;
									if(json.list[0].TIPERSONA =="1"){
										desTipoPersona = "FISICA";
										tipoPerGral = "F";
										validaCamposRequerido("F");
									}else if(json.list[0].TIPERSONA =="2"){
										desTipoPersona = "MORAL";
										validaCamposRequerido("M");
										tipoPerGral ="M";
									}else{
										desTipoPersona = "SIMPLIFICADO";
										validaCamposRequerido("S");
										tipoPerGral = "S";
									}
									storeListadoAsegurado.removeAll();
									var rec = new modelListadoAsegurado({
										CVECLIENSIGS    : json.list[0].CVECLIENSIGS,
										SUCEMISORA    	: json.list[0].SUCEMISORA,
										TIPERSONA    	: json.list[0].TIPERSONA,
										DESTIPERSONA    : desTipoPersona,
										NOMCLIENTE    	: json.list[0].NOMCLIENTE,
										APPATERNO    	: json.list[0].APPATERNO,
										APMATERNO    	: json.list[0].APMATERNO,
										RAZONSOCIAL    	: json.list[0].RAZONSOCIAL,
										RFCCLIENTE    	: json.list[0].RFCCLIENTE,
										CVEIFE    		: json.list[0].CVEIFE,
										CURP    		: json.list[0].CURP,
										CALLECLIENTE    : json.list[0].CALLECLIENTE,
										NUMCLIENTE    	: json.list[0].NUMCLIENTE,
										CPCLIENTE    	: json.list[0].CPCLIENTE,
										FECNACIMIENTO   : json.list[0].FECNACIMIENTO
									});
									storeListadoAsegurado.add(rec);
									importaPersonaWS("D", json.list[0].CVEEXTERNA);
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
				{	header	: 'Cliente<br/>SIGS',		dataIndex : 'CVECLIENSIGS',		flex:2 },
				{	header  : 'Suc. Emisora',			dataIndex : 'SUCEMISORA',		flex:2 },
				{	header  : 'Tipo Persona',			dataIndex : 'TIPERSONA',		flex:2,		hidden: true	 },
				{	header  : 'Tipo Persona',			dataIndex : 'DESTIPERSONA',		flex:2 },
				{	header  : 'Nombre Completo',		dataIndex : 'NOMCLIENTE',		flex:2 },
				{	header  : 'Nombre1',				dataIndex : 'NOMCLIENTE1',		flex:2,		hidden: true	 },
				{	header  : 'Nombre2',				dataIndex : 'NOMCLIENTE2',		flex:2,		hidden: true	 },
				{	header  : 'Apellido<br/>paterno',	dataIndex : 'APPATERNO' ,		flex:2 },
				{	header  : 'Apellido<br/>materno' ,	dataIndex : 'APMATERNO'	 ,		flex:2 },
				{	header  : 'Raz&oacute;n social',	dataIndex : 'RAZONSOCIAL',		flex:2 },
				{	header  : 'RFC',					dataIndex : 'RFCCLIENTE',		flex:2 },
				{	header  : 'CURP',					dataIndex : 'CURP',				flex:2 },
				{	header  : 'Fecha nacimiento',		dataIndex : 'FECNACIMIENTO',	flex:2 }
			],
			listeners: {
				itemclick: function(dv, record, item, index, e) {
					datosContratante.down('[name=dsnombre]').setValue(record.get('NOMCLIENTE1'));
					datosContratante.down('[name=dsnombre1]').setValue(record.get('NOMCLIENTE2'));
					datosContratante.down('[name=dsnomCompleto]').setValue(record.get('NOMCLIENTE'));
					datosContratante.down('[name=dsRazonSocial]').setValue(record.get('RAZONSOCIAL'));
					datosContratante.down('[name=dsapellido]').setValue(record.get('APPATERNO'));
					datosContratante.down('[name=dsapellido1]').setValue(record.get('APMATERNO'));
					datosContratante.down('[name=fenacini]').setValue(record.get('FECNACIMIENTO'));
					datosContratante.down('[name=rfcContratante]').setValue(record.get('RFCCLIENTE'));
					
					if(record.get('TIPERSONA') =="1" || record.get('TIPERSONA') =="F"){
						datosContratante.down('[name=rfcSugerido]').setText('RFC Sugerido : '+generaRFC("F", record.get('NOMCLIENTE'), null, record.get('APPATERNO'),
								record.get('APMATERNO'), record.get('FECNACIMIENTO'))); 
								
					}else{
						datosContratante.down('[name=rfcSugerido]').setText('RFC Sugerido : '+generaRFC("M", record.get('RAZONSOCIAL'), null,null,
    							null, record.get('FECNACIMIENTO'))); 
					}
				}
			}
		});
		
		var datosContratante = Ext.create('Ext.form.Panel',{
			id          : 'datosContratante',
			bodyPadding : 5,
			title: 'Cambio del contratante',
			layout      : {
				type     : 'table'
				,columns : 2
			},
			defaults 	: {
				style   : 'margin:5px;'
			},
			items       :[
				{    xtype       : 'textfield',			colspan     :2,			labelWidth: 150,		fieldLabel : 'Primer nombre',				name       : 'dsnombre'
					,width		 : 400,					allowBlank	: (pantallaPrincipal !="0"),    	hidden     : (pantallaPrincipal !="0"),
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intNombre1 > 0){
								validaRFCDatosRequeridos(
										tipoPerGral, 
										newVal,
										datosContratante.down('[name=dsnombre1]').getValue(), 
										datosContratante.down('[name=dsnomCompleto]').getValue(),
										datosContratante.down('[name=dsRazonSocial]').getValue(), 
										datosContratante.down('[name=dsapellido]').getValue(),
										datosContratante.down('[name=dsapellido1]').getValue(),
										Ext.Date.format(datosContratante.down('[name=fenacini]').getValue(),'d/m/Y'));
	                 		}else{
	                 			intNombre1++;
	                 		}
	                 	}
	                }
				},
				{    xtype       : 'textfield',			colspan     :2,			labelWidth: 150,		fieldLabel : 'Segundo nombre',				name       : 'dsnombre1'
					,width		 : 400,					hidden      : (pantallaPrincipal !="0"),
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intNombre2 > 0){
								validaRFCDatosRequeridos(
										tipoPerGral,
										datosContratante.down('[name=dsnombre]').getValue(),
										newVal,
										datosContratante.down('[name=dsnomCompleto]').getValue(),
										datosContratante.down('[name=dsRazonSocial]').getValue(), 
										datosContratante.down('[name=dsapellido]').getValue(),
										datosContratante.down('[name=dsapellido1]').getValue(),
										Ext.Date.format(datosContratante.down('[name=fenacini]').getValue(),'d/m/Y'));
	                 		}else{
	                 			intNombre2++;
	                 		}
	                 	}
	                }
				},
				{    xtype       : 'textfield',			colspan     :2,			labelWidth: 150,		fieldLabel : 'Nombre completo',				name       : 'dsnomCompleto'
					,width		 : 400,					allowBlank	: (pantallaPrincipal =="0"),		hidden     : (pantallaPrincipal =="0"),
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intnomCompl > 0){
								validaRFCDatosRequeridos(
										tipoPerGral,
										datosContratante.down('[name=dsnombre]').getValue(),
										datosContratante.down('[name=dsnombre1]').getValue(),
										newVal,
										datosContratante.down('[name=dsRazonSocial]').getValue(), 
										datosContratante.down('[name=dsapellido]').getValue(),
										datosContratante.down('[name=dsapellido1]').getValue(),
										Ext.Date.format(datosContratante.down('[name=fenacini]').getValue(),'d/m/Y'));
	                 		}else{
	                 			intnomCompl++;
	                 		}
	                 	}
	                }
				},
				{    xtype       : 'textfield',			colspan     :2,			labelWidth: 150,		fieldLabel : 'Raz&oacute;n social',			name       : 'dsRazonSocial'
					,width		 : 400,					allowBlank	: (pantallaPrincipal =="0"),		hidden     : (pantallaPrincipal =="0"),
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intRazonS > 0){
								validaRFCDatosRequeridos(
										tipoPerGral,
										datosContratante.down('[name=dsnombre]').getValue(),
										datosContratante.down('[name=dsnombre1]').getValue(),
										datosContratante.down('[name=dsnomCompleto]').getValue(),
										newVal,
										datosContratante.down('[name=dsapellido]').getValue(),
										datosContratante.down('[name=dsapellido1]').getValue(),
										Ext.Date.format(datosContratante.down('[name=fenacini]').getValue(),'d/m/Y'));
	                 		}else{
	                 			intRazonS++;
	                 		}
	                 	}
	                }
				},
				{    xtype       : 'textfield',			colspan     :2,			labelWidth: 150,		fieldLabel : 'Apellido Paterno',			name       : 'dsapellido'
					,width		 : 400,					allowBlank	: true,
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intappP > 0){
								validaRFCDatosRequeridos(
										tipoPerGral,
										datosContratante.down('[name=dsnombre]').getValue(),
										datosContratante.down('[name=dsnombre1]').getValue(),
										datosContratante.down('[name=dsnomCompleto]').getValue(),
										datosContratante.down('[name=dsRazonSocial]').getValue(),
										newVal,
										datosContratante.down('[name=dsapellido1]').getValue(),
										Ext.Date.format(datosContratante.down('[name=fenacini]').getValue(),'d/m/Y'));
	                 		}else{
	                 			intappP++;
	                 		}
	                 	}
	                }
				},
				{    xtype       : 'textfield',			colspan     :2,			labelWidth: 150,		fieldLabel : 'Apellido Materno',			name       : 'dsapellido1'
					,width		 : 400,					allowBlank	: true,
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intappM > 0){
								validaRFCDatosRequeridos(
										tipoPerGral,
										datosContratante.down('[name=dsnombre]').getValue(),
										datosContratante.down('[name=dsnombre1]').getValue(),
										datosContratante.down('[name=dsnomCompleto]').getValue(),
										datosContratante.down('[name=dsRazonSocial]').getValue(),
										datosContratante.down('[name=dsapellido]').getValue(),
										newVal,
										Ext.Date.format(datosContratante.down('[name=fenacini]').getValue(),'d/m/Y'));
	                 		}else{
	                 			intappM++;
	                 		}
	                 	}
	                }
				},
				{	xtype		: 'datefield',			colspan     :2,			labelWidth: 150,		fieldLabel	: 'Fecha nacimiento',			name	   : 'fenacini'
					,format		: 'd/m/Y',				allowBlank	: false,
					listeners: {
	                    change: function(cmp,newVal){
	                 		if(!Ext.isEmpty(newVal) && intfecha > 0){
								validaRFCDatosRequeridos(
										tipoPerGral,
										datosContratante.down('[name=dsnombre]').getValue(),
										datosContratante.down('[name=dsnombre1]').getValue(),
										datosContratante.down('[name=dsnomCompleto]').getValue(),
										datosContratante.down('[name=dsRazonSocial]').getValue(),
										datosContratante.down('[name=dsapellido]').getValue(),
										datosContratante.down('[name=dsapellido1]').getValue(),
										Ext.Date.format(newVal,'d/m/Y'));
	                 		}else{
	                 			intfecha++;
	                 		}
	                 	}
	                }
				},
				{    xtype      : 'textfield',			labelWidth: 150,		fieldLabel : 'RFC',		name       : 'rfcContratante'
					,width		: 400,					allowBlank	: false
				}
				,
				{	xtype		: 'label',				name: 'rfcSugerido',		text: ''
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
							params = {
								'cdperson'    : clienteSeleccionado.pv_cdperson,
								'cdtipide'    : clienteSeleccionado.CDTIPIDE,
								'cdideper'    : clienteSeleccionado.CDIDEPER,
								'dsnombre'    : datosContratante.down('[name=dsnombre]').getValue(),
								'cdtipper'    : clienteSeleccionado.CDTIPPER,
								'otfisjur'    : clienteSeleccionado.OTFISJUR,
								'otsexo'      : clienteSeleccionado.OTSEXO,
								'fenacimi'    : datosContratante.down('[name=fenacini]').getValue(),
								'cdrfc'       : datosContratante.down('[name=rfcContratante]').getValue(),
								'dsemail'     : clienteSeleccionado.DSEMAIL,
								'dsnombre1'   : datosContratante.down('[name=dsnombre1]').getValue(),
								'dsnombreComp': datosContratante.down('[name=dsnomCompleto]').getValue(),
								'dsapellido'  : datosContratante.down('[name=dsapellido]').getValue(),
								'dsapellido1' : datosContratante.down('[name=dsapellido1]').getValue(),
								'feingreso'   : clienteSeleccionado.FEINGRESO,
								'cdnacion'    : clienteSeleccionado.CDNACION,
								'canaling'    : clienteSeleccionado.CANALING,
								'conducto'    : clienteSeleccionado.CONDUCTO,
								'ptcumupr'    : clienteSeleccionado.PTCUMUPR,
								'residencia'  : clienteSeleccionado.RESIDENCIA,
								'nongrata'    : clienteSeleccionado.NONGRATA,
								'cdideext'    : clienteSeleccionado.CDIDEEXT,
								'cdestciv'    : clienteSeleccionado.CDESTCIV,
								'cdsucemi'    : clienteSeleccionado.CDSUCEMI,
								'cdunieco'    : clienteSeleccionado.pv_cdunieco,
								'cdramo'      : clienteSeleccionado.pv_cdramo,
								'estado'      : clienteSeleccionado.pv_estado,
								'nmpoliza'    : clienteSeleccionado.pv_nmpoliza,
								'nmpoliex'    : clienteSeleccionado.NMPOLIEX,
								'sucursalEntrada': panelInicialPral.down('combo[name=cmbSucursal]').getValue(),
								'ramoEntrada'   : panelInicialPral.down('[name=cveRamo]').getValue(),
								'polizaEntrada' : panelInicialPral.down('[name=nmPoliza]').getValue(),
								'tipoPantalla'  : clienteSeleccionado.tipoPantalla,
								'cdpersonNuevo' : cdpersonNuevo,
								'codigoCliExterno'  : codigoCliExterno,
								'ntramite'  : clienteSeleccionado.NTRAMITE,
								'nmsuplem'  : clienteSeleccionado.NMSUPLEM
							}
							submitValues['smap1']= params;
							
							if(!Ext.isEmpty(_endCli_flujo))
							{
							    submitValues['flujo'] = _endCli_flujo;
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
																	url		: _CONTEXT+'/seguridad/accesoDirecto.action?codigoCliente=6442&codigoRol=EJECUTIVOCUENTA&params.acceso=endosoPolizasNoSICAPS&user=biosnet1'
																	,standardSubmit : true
																});
															}
														}
													}
												}).show();
												windowVerDocu.center();
											}else{
												debug("Entra a la opcion 2");
												_generarRemesaClic(
													true
													 ,clienteSeleccionado.pv_cdunieco
													 ,clienteSeleccionado.pv_cdramo
													 ,clienteSeleccionado.pv_estado
													 ,clienteSeleccionado.pv_nmpoliza
													 ,callbackRemesa
												);
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

		////// loaders //////
		//VALORES DE ENTRADA PARA POLIZAS SICAPS
		if(clienteSeleccionado.tipoPantalla == "0"){
			debug("Entra al if");
			debug("VALOR DE ENTRADA ==> ",clienteSeleccionado.pv_cdunieco, clienteSeleccionado.RAMO, clienteSeleccionado.NMPOLIEX);
			panelInicialPral.down('combo[name=cmbSucursal]').setValue(clienteSeleccionado.pv_cdunieco);
			panelInicialPral.down('[name=cveRamo]').setValue(clienteSeleccionado.RAMO);
			panelInicialPral.down('[name=nmPoliza]').setValue(clienteSeleccionado.NMPOLIEX);
			var desTipoPersona = null;
			var nombreCompl ="";
			var razonSocialC = "";
			debug("Valor de clienteSeleccionado.DSNOMBRE1 ===="+clienteSeleccionado.DSNOMBRE1);
			debug(clienteSeleccionado.DSNOMBRE1);
			if(clienteSeleccionado.DSNOMBRE1 == null ||clienteSeleccionado.DSNOMBRE1 == 'null' || clienteSeleccionado.DSNOMBRE1 == '' ){
				nombreCompl = clienteSeleccionado.DSNOMBRE;
			}else{
				nombreCompl = clienteSeleccionado.DSNOMBRE+" "+clienteSeleccionado.DSNOMBRE1;
			}
			if(clienteSeleccionado.OTFISJUR =="F"){
				desTipoPersona = "FISICA";
				tipoPerGral = "F";
				validaCamposRequerido(clienteSeleccionado.OTFISJUR);
			}else if(clienteSeleccionado.OTFISJUR =="M"){
				desTipoPersona = "MORAL";
				tipoPerGral = "M";
				validaCamposRequerido(clienteSeleccionado.OTFISJUR);
				if(clienteSeleccionado.DSRAZSOC != null ||clienteSeleccionado.DSRAZSOC != 'null'){
					razonSocialC = nombreCompl;
				}else{
					razonSocialC = clienteSeleccionado.DSRAZSOC;
				}
			}else{
				desTipoPersona = "SIMPLIFICADO"
				tipoPerGral = "S";
				validaCamposRequerido("S");
				
				if(clienteSeleccionado.DSRAZSOC != null ||clienteSeleccionado.DSRAZSOC != 'null'){
					razonSocialC = nombreCompl;
				}else{
					razonSocialC = clienteSeleccionado.DSRAZSOC;
				}
			}
			var rec = new modelListadoAsegurado({
				CVECLIENSIGS    : clienteSeleccionado.CDIDEPER,
				SUCEMISORA    	: clienteSeleccionado.pv_cdunieco,
				TIPERSONA    	: clienteSeleccionado.OTFISJUR,
				DESTIPERSONA    : desTipoPersona,
				NOMCLIENTE    	: nombreCompl,
				NOMCLIENTE1    	: clienteSeleccionado.DSNOMBRE,
				NOMCLIENTE2    	: clienteSeleccionado.DSNOMBRE1,
				APPATERNO    	: clienteSeleccionado.DSAPELLIDO,
				APMATERNO    	: clienteSeleccionado.DSAPELLIDO1,
				RAZONSOCIAL    	: razonSocialC,
				RFCCLIENTE    	: clienteSeleccionado.CDRFC,
				CVEIFE    		: null,
				CURP    		: null,
				CALLECLIENTE    : null,//clienteSeleccionado.OTFISJUR,
				NUMCLIENTE    	: null,//clienteSeleccionado.OTFISJUR,
				CPCLIENTE    	: null,//clienteSeleccionado.OTFISJUR,
				FECNACIMIENTO   : clienteSeleccionado.FENACIMI
			});
			storeListadoAsegurado.add(rec);
		}
	});

	function importaPersonaWS(esSaludD, codigoCliExt){
		Ext.Ajax.request({
			url       : _UrlImportaPersonaWS
			,params: {
			'params.esSalud':  esSaludD,
			'params.codigoCliExt':  codigoCliExt
			}
			,success  : function(response){
				var json = Ext.decode(response.responseText);
				debug('response text:',json);
				if(json.exito){
					debug("Valores de recuperacion ==> ",json.params.cdpersonNuevo, json.params.codigoCliExt);
					cdpersonNuevo = json.params.cdpersonNuevo;
					codigoCliExterno  = json.params.codigoCliExt;
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
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindivHist" style="height:500px;"></div>