Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
Ext.onReady(function() {
	var panelProveedor;
	Ext.selection.CheckboxModel.override( {
		mode: 'SINGLE',
		allowDeselect: true
	});
	//Declaracion de los modelos 
	Ext.define('modeloAutEspecial',{
		extend:'Ext.data.Model',
		fields:[
			{type:'string',		name:'CDUNIECO'},			{type:'string',		name:'DESCDUNIECO'},
			{type:'string',		name:'CDRAMO'},				{type:'string',		name:'DSRAMO'},
			{type:'string',		name:'DSTATUS'},			{type:'string',		name:'ESTADO'},
			{type:'string',		name:'NMPOLIZA'},			{type:'string',		name:'NMSUPLEM'},
			{type:'string',		name:'NMSITUAC'},			{type:'string',		name:'NMSINIES'},
			{type:'string',		name:'CDPERSON'},			{type:'string',		name:'DESCDPERSON'},
			{type:'string',		name:'NTRAMITE'},			{type:'string',		name:'TIPOPAGO'},
			{type:'string',		name:'DESTIPOPAGO'},		{type:'string',		name:'CDTIPSIT'},
			{type:'string',		name:'NUMPOLIZA'},			{type:'string',		name:'NFACTURA'},
			{type:'string',		name:'VIGENCIA'}, 			{type:'string',		name:'NMSINIES'},
			{type:'string',		name:'NMAUTESP'}, 			{type:'string',		name:'VALRANGO'},
			{type:'string',		name:'VALCOBER'}, 			{type:'string',		name:'CDGARANT'},
			{type:'string',		name:'COMMENTS'}
		   ,{type:'string',		name:'VALEXCEDE'}	// (EGS)
        ]
	});
	
	Ext.define('modelListadoCobertura',{
		extend: 'Ext.data.Model',
		fields: [	{type:'string',    name:'cdgarant'},			{type:'string',    name:'dsgarant'},              	{type:'string',    name:'ptcapita'}		]
	});
	
	//Declaracion de los stores
	var storeGridAutEspecial = new Ext.data.Store({
		pageSize	: 10
		,model		: 'modeloAutEspecial'
		,autoLoad	: false
		,proxy		: {
			enablePaging	:	true,
			reader			:	'json',
			type			:	'memory',
			data			:	[]
		}
	});
	
	storeRamos = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url:_UR_LISTA_RAMO_SALUD,
			reader:	{
				type: 'json',
				root: 'listadoRamosSalud'
			}
		}
	});
	storeRamos.load();
	
	storeTipoPago = Ext.create('Ext.data.JsonStore', {
        model:'Generic',
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            extraParams : {catalogo:_CATALOGO_TipoPago},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    storeTipoPago.load();
    
	storeAsegurados = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_LISTADO_ASEGURADO,
			reader: {
				type: 'json',
				root: 'listaAsegurado'
			}
		}
	});
	
	var storeCobertura = Ext.create('Ext.data.Store', {
		model:'modelListadoCobertura',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_LISTA_COBERTURA,
			reader: {
				type: 'json',
				root: 'listaCoberturaPoliza'
			}
		}
	});
	
	
    contraRecibo = new Ext.form.NumberField({
		fieldLabel	: 'ContraRecibo',		name : 'txtContrarecibo'
	});
	
    factura = new Ext.form.TextField({
		fieldLabel	: 'Factura',			name : 'txtFactura'
	});
	
	cmbRamos = Ext.create('Ext.form.field.ComboBox',{
		fieldLabel : 'Producto',	id             : 'cmbRamos',		editable : false,		displayField : 'value',
		valueField : 'key',			forceSelection : false,      		queryMode:'local',		name         :'cmbRamos',
		store : storeRamos,
        listeners : {
            'select':function(e){
                storeTipoPago.load({
                    params:{
                        'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue()
                    }
                });
            }
        }
	});
	
    tipoPago = Ext.create('Ext.form.ComboBox',{
        name:'cmbTipoPago',                     fieldLabel: 'Tipo pago',            allowBlank:false,           editable:false,
        displayField: 'value',                  valueField: 'key',                  queryMode:'local',          emptyText:'Seleccione...',
        store: storeTipoPago
    });
	
    asegurado = Ext.create('Ext.form.field.ComboBox',{
				fieldLabel 	: 'Asegurado',		allowBlank		: false,		displayField 	: 'value'
				,name			: 'idCodigoAsegurado'
				,valueField   	: 'key',							forceSelection 	: true,			matchFieldWidth	: false
				,minChars  	: 2,									queryMode 		:'remote',		queryParam		: 'params.cdperson'
				,store 		: storeAsegurados,						hideTrigger		:true,			triggerAction	: 'all'
	});
	
	coberturaAfectada = Ext.create('Ext.form.field.ComboBox',{
		colspan:2,
		displayField : 'dsgarant',		id             :'idCobAfectada',			name:'cdgarant',
		valueField   : 'cdgarant',		forceSelection : true,						matchFieldWidth: false,
		queryMode :'local',				store          : storeCobertura,			triggerAction: 'all',			editable:false
	});
	
	comentarios = Ext.create('Ext.form.field.TextArea', {
		colspan:2,fieldLabel: 'Comentarios',	width: 400,
		name:'comments',						height: 70,				allowBlank: false
	});
	
	var panelInicialPral= Ext.create('Ext.panel.Panel',{
		border    : 0
		,renderTo : 'div_clau'
		,items    : [
			Ext.create('Ext.panel.Panel',{
				border  : 0
				,title: 'Filtro de b&uacute;squeda'
				,style         : 'margin:5px'
				,layout : {
					type     : 'table'
					,columns : 3
				}
				,defaults : {
					style : 'margin:5px;'
				}
				,items : [
					cmbRamos
					,tipoPago
					,contraRecibo
					,factura
					,asegurado
				]
				,buttonAlign: 'center'
				,buttons : [{
					text: "Buscar"
					,icon:_CONTEXT+'/resources/fam3icons/icons/magnifier.png'
					,handler: function(){
						cargarPaginacion();
					}	
				},{
					text: "Limpiar"
					,icon:_CONTEXT+'/resources/fam3icons/icons/arrow_refresh.png'
					,handler: function(){
						panelInicialPral.down('combo[name=cmbRamos]').reset();
						panelInicialPral.down('combo[name=cmbTipoPago]').reset();
						panelInicialPral.down('[name=txtContrarecibo]').setValue('');
						panelInicialPral.down('[name=txtFactura]').setValue('');
						panelInicialPral.down('combo[name=idCodigoAsegurado]').reset();
						storeGridAutEspecial.removeAll();
					}	
				}] 
			})
			,Ext.create('Ext.grid.Panel',{
				id             : 'clausulasGridId'
				,title         : 'Autorizaciones Especiales'
				,store         :  storeGridAutEspecial
				,titleCollapse : true
				,style         : 'margin:5px'
				,height        : 400
				,columns       : [
					{   xtype: 'actioncolumn',      width: 40,          sortable: false,            menuDisabled: true,
	                    items: [{
	                        icon: _CONTEXT+'/resources/fam3icons/icons/application_edit.png',
	                        tooltip: 'Generar Autorizaci\u00F3n Especial',	//(EGS)
	                        scope: this,
	                        handler: _11_editar
	                    }]
	                 },
					{	header     : 'Cdunieco',			dataIndex : 'CDUNIECO',		flex : 1, 	hidden   : true	},
					{	header     : 'Cdramo',				dataIndex : 'CDRAMO',		flex : 1, 	hidden   : true	},
					{	header     : 'Estado',				dataIndex : 'ESTADO',		flex : 1, 	hidden   : true	},
					{	header     : 'Nmpoliza',			dataIndex : 'NMPOLIZA',		flex : 1, 	hidden   : true	},
					{	header     : 'Nmsuplem',			dataIndex : 'NMSUPLEM',		flex : 1, 	hidden   : true	},
					{	header     : 'Nmsituac',			dataIndex : 'NMSITUAC',		flex : 1, 	hidden   : true	},
					{	header     : 'Cdperson',			dataIndex : 'CDPERSON',		flex : 1, 	hidden   : true	},
					{	header     : 'TipoPago',			dataIndex : 'TIPOPAGO',		flex : 1, 	hidden   : true	},
					{	header     : 'Cdtipsit',			dataIndex : 'CDTIPSIT',		flex : 1, 	hidden   : true	},
					{	header     : 'Val Rango',			dataIndex : 'VALRANGO',		flex : 1, 	hidden   : true	},
					{	header     : 'Val Cobertura',		dataIndex : 'VALCOBER',		flex : 1, 	hidden   : true	},
					{	header     : 'Cobertura',			dataIndex : 'CDGARANT',		flex : 1, 	hidden   : true	},
					{	header     : 'Comentarios',			dataIndex : 'COMMENTS',		flex : 1, 	hidden   : true	},
					{	header     : 'Tr&aacute;mite',		dataIndex : 'NTRAMITE',		width : 80				},
					{	header     : 'Factura',				dataIndex : 'NFACTURA',		width : 100				},
					{	header     : 'Tipo Pago',			dataIndex : 'DESTIPOPAGO',	width : 130				},
					{	header     : 'Asegurado',			dataIndex : 'DESCDPERSON',	width : 200 			},
					{	header     : 'Siniestro',			dataIndex : 'NMSINIES',		width : 80				},
					{	header     : 'Autorizaci&oacute;n<br/>Especial',	dataIndex : 'NMAUTESP',	width : 80	},
					{	header     : 'Sucursal',			dataIndex : 'DESCDUNIECO',	width : 150				},
					{	header     : 'Producto',			dataIndex : 'DSRAMO',		width : 120				},
					{	header     : 'Poliza',				dataIndex : 'NUMPOLIZA',	width : 150				},
					{	header     : 'Estatus <br/>P&oacute;liza',	dataIndex : 'DSTATUS',		width : 100		},
					{	header     : 'Vigencia P&oacute;liza',		dataIndex : 'VIGENCIA',		width : 200		}
					
				],
				bbar     :{
				displayInfo : true,
					store		: storeGridAutEspecial,
					xtype		: 'pagingtoolbar'
				}
			})
		]
	});
	cargarPaginacion();
	
	function cargarPaginacion(){
		storeGridAutEspecial.removeAll();
		var params = {
			'params.cdramo'   :panelInicialPral.down('combo[name=cmbRamos]').getValue()
			,'params.tipoPago' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
			,'params.ntramite' : panelInicialPral.down('[name=txtContrarecibo]').getValue()
			,'params.nfactura' : panelInicialPral.down('[name=txtFactura]').getValue()
			,'params.cdperson' : panelInicialPral.down('combo[name=idCodigoAsegurado]').getValue()
		};
		cargaStorePaginadoLocal(storeGridAutEspecial, _URL_CONSULTA_AUTESPECIAL, 'datosValidacion', params, function(options, success, response){
			if(success){
				var jsonResponse = Ext.decode(response.responseText);
				if(jsonResponse.datosValidacion &&jsonResponse.datosValidacion.length == 0) {
					if(null == null){
						centrarVentanaInterna(showMessage("Aviso", "No existe tr&aacute;mites.", Ext.Msg.OK, Ext.Msg.INFO));
					}
					return;
				}
			}else{
				centrarVentanaInterna(showMessage('Error', 'Error al obtener los datos',Ext.Msg.OK, Ext.Msg.ERROR));
			}
		});
	}
	
	
	panelConfiguracion= Ext.create('Ext.form.Panel', {
        border      : 0,
        id          : 'panelConfiguracion',
        //renderTo    : 'div_clau',
        bodyPadding : 5,
        width       : 500,
        layout      : {
			type    : 'table'
			,columns: 2
		},
		defaults 	: {
			style   : 'margin:5px;'
		},
		items       :[
			{    xtype : 'textfield',		fieldLabel : 'ntramite',		name : 'txtntramite' ,		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'nfactura',		name : 'txtnfactura' ,		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'cdunieco',		name : 'txtcdunieco',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'cdramo',			name : 'txtcdramo',			hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'estado',			name : 'txtestado',			hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'nmpoliza',		name : 'txtnmpoliza',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'nmsuplem',		name : 'txtnmsuplem',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'nmsituac',		name : 'txtnmsituac',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'nmsinies',		name : 'txtnmsinies',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'tipoPago',		name : 'txttipoPago',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'valRango',		name : 'valRango',				value:'0',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'valGarant',		name : 'valGarant',				value:'0',		hidden:true		},
			{    xtype : 'textfield',		fieldLabel : 'No. autorizacion',name : 'nmautespecial',			value:'0',		hidden:true		},
			{	 xtype : 'textfield',		fieldLabel : 'valLimite',		name : 'valLimite',				value: 0,		hidden:true		}, // (EGS)
			{
				xtype: 'fieldcontainer',
				colspan:2,
				fieldLabel: 'Motivos',
				defaultType: 'checkboxfield',
				layout : {
					type     : 'table'
					,columns : 2
				},
				defaults : {
					style : 'margin:5px;'
				},
				items: [
				        {xtype: 'checkbox', id: 'genCheckboxA', name: 'genCheckboxA', boxLabel: 'Fuera de vigencia', hideLabel: true, colspan:2,
				        	listeners: {
				        	    change: function() {
				        	    	if (Ext.getCmp('genCheckboxA').getValue() == true) {
				        	    		panelConfiguracion.down('[name="valRango"]').setValue("1");   
			                        }else{
			                        	panelConfiguracion.down('[name="valRango"]').setValue("0");
			                        }
				        	    }
				        	}
				        },
				        {xtype: 'checkbox', id: 'genCheckboxB', name: 'genCheckboxB', boxLabel: 'Cobertura no amparada', hideLabel: true,
				        	listeners: {
				        	    change: function() {
				        	    	 if (Ext.getCmp('genCheckboxB').getValue() == true) {
				        	    		 panelConfiguracion.down('combo[name=cdgarant]').allowBlank = false;
				        	    		 panelConfiguracion.down('[name="valGarant"]').setValue("1");
				        	    		 panelConfiguracion.down('combo[name=cdgarant]').setDisabled(false);
				                        }else{
				                        	panelConfiguracion.down('combo[name=cdgarant]').allowBlank = true;
				                        	panelConfiguracion.down('[name="valGarant"]').setValue("0");
				                        	panelConfiguracion.down('combo[name=cdgarant]').setDisabled(true);
				                        }
				        	    }
				        	}
				        },
				        coberturaAfectada
				        ,{xtype: 'checkbox', id: 'genCheckboxC', name: 'genCheckboxC', boxLabel: 'Excedente en L&iacute;mite', hideLabel: true, //(EGS)
				        	listeners: {
				        		change: function(){
				        			debug("Change Excedente medicamentos");
				        			if(Ext.getCmp('genCheckboxC').getValue() == true){
				        				panelConfiguracion.down('[name="valLimite"]').setValue("1");
				        			}else{
				        				panelConfiguracion.down('[name="valLimite"]').setValue("0");
				        			}
				        		}
				        	}
				        } // fin (EGS)
			        ]
			},
			comentarios
        ],
        buttonAlign:'center',
        buttons: [{
            id:'botonCotizar',
            icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
            text: 'Confirmar autorizaci&oacute;n',
    		handler: function() {
				var form = this.up('form').getForm();
    			if (form.isValid()){
    				if(Ext.getCmp('genCheckboxA').getValue() == false && Ext.getCmp('genCheckboxB').getValue() == false && Ext.getCmp('genCheckboxC').getValue() == false){	//(EGS) agregamos tercer condición
        				Ext.Msg.show({
	                    	title:'Datos incompletos',
	                    	msg: 'Seleccione al menos un motivo para la confirmaci&oacute;n de autorizaci&oacute;n especial.',
	                    	buttons: Ext.Msg.OK,
	                    	icon: Ext.Msg.WARNING
	                	})
        			}else{
        				var submitValues={};
        				var formulario=panelConfiguracion.form.getValues();
        				submitValues['params']=formulario;
        				debug(submitValues);
        				Ext.Ajax.request( {
							url: _URL_GUARDA_AUT_ESPECIAL,
							jsonData:Ext.encode(submitValues),
							success:function(response,opts){
								panelConfiguracion.setLoading(false);
								var jsonResp = Ext.decode(response.responseText);
								debug("Valor de respuesta ",jsonResp);
								if(jsonResp.success==true){
									if(panelConfiguracion.down('[name="nmautespecial"]').getValue() > 0){
										mensajeCorrecto('Aviso',"Se actualizo la autorizaci&oacute;n especial con el n&uacute;mero : "+Ext.decode(response.responseText).msgResult);
										
									}else{
										panelConfiguracion.down('[name="nmautespecial"]').setValue(Ext.decode(response.responseText).msgResult);
    									mensajeCorrecto('Aviso',"Se registro la autorizaci&oacute;n especial con el n&uacute;mero : "+Ext.decode(response.responseText).msgResult);
									}
									modPolizasAltaTramite.close();
									cargarPaginacion();
								}
								else{
									Ext.Msg.show({
										title:'Error',
										msg: 'Error en el guardado del alta de tr&aacute;mite',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR
									});
									respuesta= false;
								}
							},
							failure:function(response,opts){
								panelConfiguracion.setLoading(false);
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
    			else {
    				Ext.Msg.show({
                    	title:'Datos incompletos',
                    	msg: 'Favor de introducir todos los campos requeridos',
                    	buttons: Ext.Msg.OK,
                    	icon: Ext.Msg.WARNING
                	});
				}
			}            
        }       
    ]
    });
    	    
    	    
	var modPolizasAltaTramite = Ext.create('Ext.window.Window', {
		title		: 'Detalle Factura'
		,modal	   : true
		,resizable   : false
		,buttonAlign : 'center'
		,closable	: true
		,closeAction: 'hide'
		,defaults 	:
		{
			style : 'margin:5px;'
		}
		,items	   : 
		[
			panelConfiguracion
		]
	});

	function _11_editar(grid,rowindex){
		_11_recordActivo = grid.getStore().getAt(rowindex);
		
		debug("_11_recordActivo ==>",_11_recordActivo);
		panelConfiguracion.getForm().reset();
		panelConfiguracion.down('[name=txtntramite]').setValue(_11_recordActivo.get('NTRAMITE'));
		panelConfiguracion.down('[name=txtnfactura]').setValue(_11_recordActivo.get('NFACTURA'));
		panelConfiguracion.down('[name=txtcdunieco]').setValue(_11_recordActivo.get('CDUNIECO'));
		panelConfiguracion.down('[name=txtcdramo]').setValue(_11_recordActivo.get('CDRAMO'));
		panelConfiguracion.down('[name=txtestado]').setValue(_11_recordActivo.get('ESTADO'));
		panelConfiguracion.down('[name=txtnmpoliza]').setValue(_11_recordActivo.get('NMPOLIZA'));
		panelConfiguracion.down('[name=txtnmsuplem]').setValue(_11_recordActivo.get('NMSUPLEM'));
		panelConfiguracion.down('[name=txtnmsituac]').setValue(_11_recordActivo.get('NMSITUAC'));
		panelConfiguracion.down('[name=txtnmsinies]').setValue(_11_recordActivo.get('NMSINIES'));
		panelConfiguracion.down('[name=txttipoPago]').setValue(_11_recordActivo.get('TIPOPAGO'));
		storeCobertura.load({
			params:{
				'params.cdunieco': _11_recordActivo.get('CDUNIECO'),
				'params.cdramo'  : _11_recordActivo.get('CDRAMO'),
				'params.estado'  : _11_recordActivo.get('ESTADO'),
				'params.nmpoliza': _11_recordActivo.get('NMPOLIZA'),
				'params.nmsituac': _11_recordActivo.get('NMSITUAC'),
				'params.nmsuplem': _11_recordActivo.get('NMSUPLEM'),
				'params.cdtipsit': _11_recordActivo.get('CDTIPSIT')
			}
		});
		
		panelConfiguracion.down('[name=comments]').setValue(_11_recordActivo.get('COMMENTS'));
		panelConfiguracion.down('[name=nmautespecial]').setValue(_11_recordActivo.get('NMAUTESP'));
		
		
	  	if(_11_recordActivo.get('VALRANGO') > 0){
	        panelConfiguracion.down('[name="valRango"]').setValue("1");
	        Ext.getCmp('genCheckboxA').setValue(true);
	    }else{
	        panelConfiguracion.down('[name="valRango"]').setValue("0");
	        Ext.getCmp('genCheckboxA').setValue(false);
	    }
	    
	    if(_11_recordActivo.get('VALCOBER') > 0){
	        Ext.getCmp('genCheckboxB').setValue(true);
	        panelConfiguracion.down('[name="valGarant"]').setValue("1");
	        panelConfiguracion.down('combo[name=cdgarant]').setValue(_11_recordActivo.get('CDGARANT'));
	    }else{
	        panelConfiguracion.down('[name="valGarant"]').setValue("0");
	        Ext.getCmp('genCheckboxB').setValue(false);
	    }
	    // (EGS)
	    if(_11_recordActivo.get('VALEXCEDE') > 0){
	    	Ext.getCmp('genCheckboxC').setValue(true);
	    	panelConfiguracion.down('[name="valLimite"]').setValue("1");
	    }else{
	    	Ext.getCmp('genCheckboxC').setValue(false);
	    	panelConfiguracion.down('[name="valLimite"]').setValue("0");
	    }
	    // fin (EGS)
		modPolizasAltaTramite.show();
		centrarVentanaInterna(modPolizasAltaTramite);
	}
});