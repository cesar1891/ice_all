Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
	Ext.onReady(function() {
		var panelProveedor;
		var complemento;
		Ext.selection.CheckboxModel.override( {
			mode: 'SINGLE',
			allowDeselect: true
		});
		//Declaracion de los modelos 
		Ext.define('_p22_modeloGrid',{
			extend  : 'Ext.data.Model',
			fields : [ 
			            {type:'string', 	name:'STATUS'	},		{type:'string', 	name:'CDRFC'	},			{type:'string', 	name:'OTFISJUR'	},
			            {type:'string', 	name:'DSL_CDIDEPER'},	{type:'string', 	name:'DSL_CDIDEEXT'},		{type:'string', 	name:'CDSUCEMI'},
			            {type:'string', 	name:'DSNOMBRE'},		{type:'string', 	name:'DSNOMBRE1'},			{type:'string', 	name:'DSAPELLIDO'},	
			            {type:'string', 	name:'DSAPELLIDO1'},	{type:'string', 	name:'OTSEXO'},				{type:'string', 	name:'CDNACION'},
			            {type:'string', 	name:'RESIDENTE'},		{type:'string', 	name:'PTCUMUPR'},			{type:'string', 	name:'NONGRATA'},
			            {type:'string', 	name:'CANALING'},		{type:'string', 	name:'CDESTCIV'},			{type:'string', 	name:'TELEFONO'},
			            {type:'string', 	name:'EMAIL'},			{type:'string', 	name:'CDIDEEXT'},			{type:'string', 	name:'CONDUCTO'},
			            {type:'string', 	name:'NOMBRE_COMPLETO'},{type:'string', 	name:'CDPERSON'},			{type:'string', 	name:'DIRECCIONCLI'},
			            {type:'string', 	name:'CDIDEPER'},		
			            {type:'date',		dateFormat:'d/m/Y',		name:'FENACIMI'	},			
			            {type:'date',		dateFormat:'d/m/Y',		name:'FEINGRESO'}
		            ]
		});

		Ext.define('modelListadoCliente',{
			extend: 'Ext.data.Model',
			fields: [	{type:'string',		name:'CDRFC'},			{type:'string',		name:'STATUS'},				{type:'string',		name:'CDTIPPER'},
						{type:'string',		name:'CDAGENTE'},		{type:'string',		name:'DSNOMBRE'},			{type:'string',		name:'DSDOMICIL'},
						{type:'string',		name:'OBSERMOT'},		{type:'string',		name:'CDUSER'},				{type:'string',		name:'FEFECHA'},
						{type:'string',		name:'DESCAGENTE'},		{type:'string',		name:'DESTIPOPER'}
			]
		});
		
		storeObtenerPersona = Ext.create('Ext.data.Store', {
            model     : '_p22_modeloGrid', 
            proxy     : {
                    type        : 'ajax'
                    ,url        : _P22_URLOBTENERPERSONAS
                    ,reader     :
                    {
                        type  : 'json'
                        ,root : 'slist1'
                    }
                }
        });

		storeAgentes = Ext.create('Ext.data.Store', {
			model:'Generic',
			autoLoad:true,
			proxy: {
				type: 'ajax',
				url : _URL_CATALOGOS,
				extraParams : {catalogo:_CAT_AGENTE},
				reader: {
					type: 'json',
					root: 'lista'
				}
			}
		});
		
		var storeListadoCliente = new Ext.data.Store({
			pageSize	: 5
			,model		: 'modelListadoCliente'
			,autoLoad	: false
			,proxy		: {
				enablePaging	: true,
				reader			: 'json',
				type			: 'memory',
				data			: []
			}
		});
		
		
		storeAsegurados = Ext.create('Ext.data.Store', {
			model:'Generic',
			autoLoad:false,
			proxy: {
				type: 'ajax',
				url : _URL_LISTA_CLIENTES,
				reader: {
					type: 'json',
					root: 'genericVO'
				}
			}
		});
		
		asegurado = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel: 'Asegurado',		allowBlank     : false,				displayField   : 'value',
			id     :'idAsegurado',			labelWidth 	   : 170,				valueField     : 'key',			queryParam   : 'params.cdperson',
			width  :500,					forceSelection : true,				matchFieldWidth: false,			queryMode    :'remote',
			minChars  : 2,					store 		   : storeAsegurados,	triggerAction  : 'all',			name:'cdperson',
			hideTrigger:true,
			listeners : {
				'select' : function(combo, record) {
					CargaListaCliente(this.getValue());
				}
			}
		});
		
	
		
		agente = Ext.create('Ext.form.field.ComboBox',{
			ieldLabel 	  : 'Agente', 			name 	  :'smap1.agente',			allowBlank 	   : false,			
			displayField  : 'value',			valueField:'key',					forceSelection : true,			
			width		  :350,					queryMode :'remote',				store 		   : storeAgentes,
			queryParam    : 'params.agente',	matchFieldWidth: false,				triggerAction  : 'all',			
			hideTrigger	  :true,				minChars  : 2,						fieldLabel	   : 'Agente'
		});
		
		comentariosText = Ext.create('Ext.form.field.TextArea', {
			fieldLabel: 'Observaciones'
			,width: 700
			,name:'smap1.comments'
			,height: 150
			,allowBlank : false
		});

		
		gridListadoCliente = Ext.create('Ext.grid.Panel', {
			id			:	'gridListadoCliente',
			store		:	storeListadoCliente,
			selType		:	'checkboxmodel',
			width		:	800,
			height		:	300,
			columns		:
			[
				{	header		: 'RFC',						dataIndex : 'CDRFC',		width		: 150		},
				{	header		: 'Nombre',						dataIndex : 'DSNOMBRE',		width	 	: 200		},
				{	header		: 'Domicilio',					dataIndex : 'DSDOMICIL',	width	    : 200		},
				{	header		: 'Tipo Persona',				dataIndex : 'DESTIPOPER',	width	    : 100		},
				{	header		: 'Agente',						dataIndex : 'DESCAGENTE',	width		: 200		},
				{	header		: 'Observaciones<br/>Causa',	dataIndex : 'OBSERMOT',		width	    : 500		},
			],
			bbar : {
				displayInfo	: true,
				store		: storeListadoCliente,
				xtype		: 'pagingtoolbar'
			},
			listeners: {
				itemclick: function(dv, record, item, index, e){
					//1.- Validamos que el asegurado este vigente
					debug("Valor del Record", record);
					
			 		centrarVentanaInterna(Ext.Msg.show({
				        title: 'Aviso',
				        msg: '&iquest;Desea deshabilitar el Cliente ?',
				        buttons: Ext.Msg.YESNO,
				        icon: Ext.Msg.QUESTION,
				        fn: function(buttonId, text, opt){
				        	if(buttonId == 'yes'){
				        		Ext.Ajax.request({
									url	 : _URL_GUARDA_CLIENTE
									,params:{
										'params.cdrfc'  	: record.get('CDRFC'),
										'params.status'   	: "0",
										'params.cdtipper'   : record.get('CDTIPPER'),
										'params.agente'     : record.get('CDAGENTE'),
										'params.dsnombre'   : record.get('DSNOMBRE'),
										'params.dsdomicil'  : record.get('DSDOMICIL'),
										'params.obsermot'   : record.get('OBSERMOT'),
										'params.proceso'    : proceso.procesoCliente,
										'params.accion'     : "U"
									}
									,success : function (response){
										mensajeCorrecto('&Eacute;XITO','El cliente se ha deshabilitado Correctamente.',function(){
											CargaListaCliente(null);
										});
										
									},
									failure : function () {
										Ext.Msg.show({
											title:'Error',
											msg: 'Error de comunicaci&oacute;n',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR
										});
									}
								});
				        	}else{
				        		CargaListaCliente(null);
				        	}
				        	
				        }
				    }));
			 		
				}
			}
		});
		_PanelPrincipalPersonas = Ext.create('Ext.panel.Panel',{
			//renderTo  : Ext.getBody()
			defaults : { style : 'margin:5px;' }
			,border   : 0
			,itemId   : '_p22_PanelPrincipal'
			,items    :
				[
				 	Ext.create('Ext.form.Panel',{
			 			title        : "Escriba el RFC/Nombre de la Persona a buscar",
		 				itemId      : '_p22_formBusqueda',
		 				defaults    : { style : 'margin:5px;' },
		 				items       : 
	 					[
			               {	xtype      : 'fieldcontainer',		fieldLabel : 'Tipo de Compa&ntilde;ia',
			            	   defaultType: 'radiofield',          	id         : 'companiaGroupId',
			            	   border: true,						defaults : { style : 'margin:5px;' },
			            	   layout: 'hbox',
			            	   items: 
		            		   [
		            	           {
		            	        	   boxLabel  : 'General de Seguros',       	   name      : 'smap1.esSalud',
		            	        	   inputValue: 'D',            	        	   checked   : true,
		            	        	   id        : 'companiaId',
		            	        	   listeners: {
	            	        		   		change: function(){
	            	        		   			var form=_p22_formBusqueda();
	            	        		   			form.down('[name=smap1.rfc]').reset();
	            	        		   			form.down('[name=smap1.nombre]').reset();
	            	        		   			form.down('[name=smap1.rfc]').getStore().removeAll();
	            	        		   			form.down('[name=smap1.nombre]').getStore().removeAll();
	            	        		   		}
		            	        	   }
		            	           },
		            	           {
		            	        	   boxLabel  : 'General de Salud',			name      : 'smap1.esSalud',
		            	        	   inputValue: 'S',							checked   : false
		            	           }
	            	           ]
		            	   },
		            	   {
		            		   xtype		  : 'combobox',			fieldLabel	 :'B&uacute;squeda por RFC',	displayField 	: 'NOMBRE_COMPLETO',  
		            		   labelWidth	  : 100,	    		width		 :    600,						queryParam  	: 'smap1.rfc',
		            		   queryMode   	  : 'remote',  			queryCaching : false,						allQuery    	: 'dummyForAllQuery',
		            		   minChars    	  : 9,					minLength    : 2,							name        	: 'smap1.rfc',
		            		   valueField     : 'CDRFC',			autoSelect   : false,		            	hideTrigger   	: true,
		            		   enableKeyEvents: true,				store        : storeObtenerPersona,
		            		   listeners: {
		            			   select: function(comb, records){
		            				    var form=_p22_formBusqueda();
	            				   		form.down('[name=smap1.nombre]').reset();
	            				   		form.down('[name=smap1.rfcCliente]').setValue(records[0].get('CDRFC'));
		            				    form.down('[name=smap1.tipoPersona]').setValue(records[0].get('OTFISJUR'));
		            				    form.down('[name=smap1.nombreCliente]').setValue(records[0].get('DSNOMBRE')+" "+records[0].get('DSNOMBRE1')+" "+records[0].get('DSAPELLIDO')+" "+records[0].get('DSAPELLIDO1'));
		            				    form.down('[name=smap1.domicilio]').setValue(records[0].get('DIRECCIONCLI'));
		            			   }, 
		            			   keydown: function( com, e, eOpts ){
		            				   var form=_p22_formBusqueda();
		            				   if(e.isSpecialKey() || e.isNavKeyPress() || e.getKey() == e.CONTEXT_MENU || e.getKey() == e.HOME || e.getKey() == e.NUM_CENTER 
		            						   || e.getKey() == e.F1 || e.getKey() == e.F2 || e.getKey() == e.F3 || e.getKey() == e.F4 || e.getKey() == e.F5 || e.getKey() == e.F6 
		            						   || e.getKey() == e.F7 || e.getKey() == e.F8 || e.getKey() == e.F9 || e.getKey() == e.F10 || e.getKey() == e.F11 || e.getKey() == e.F12){
		            					   if(e.getKey() == e.BACKSPACE){
		            						   form.down('[name=smap1.rfc]').getStore().removeAll();
		            					   }
		            				   }else{
		            					   //_RFCsel = '';
		            					   form.down('[name=smap1.nombre]').reset();
		            					   form.down('[name=smap1.rfc]').getStore().removeAll();
		            				   }
		            			   },
		            			   change: function(me, val, oldVal, eopts){
		            				   try{
		            					   if('string' == typeof val){
		            						   if(String(val.toUpperCase()).indexOf(" -") != -1){
		            							   me.setValue(ultimoValorQueryRFC);
		            						   }else {
		            							   ultimoValorQueryRFC = val.toUpperCase();
		            							   me.setValue(ultimoValorQueryRFC);
		            						   }
		            					   }
		            				   }
		            				   catch(e){
		            					   debug(e);
		            				   }
		            			   },
		            			   beforequery: function( queryPlan, eOpts ){
		            				   queryPlan.query = Ext.String.trim(queryPlan.query);
		            				   if(String(queryPlan.query).indexOf(" -") != -1){
		            					   queryPlan.query = ultimoValorQueryRFC;
		            				   }
		            			   }
		            		   }
		            	   },
		            	   {
		            		   xtype		  : 'combobox',      	fieldLabel	 :'B&uacute;squeda por Nombre',	displayField  	: 'NOMBRE_COMPLETO',
		            		   labelWidth	  : 100,				width		 :    600,						queryParam  	: 'smap1.nombre',
		            		   queryMode   	  : 'remote',			queryCaching : false,						allQuery    	: 'dummyForAllQuery',
		            		   minChars    	  : 2,					minLength    : 2,							name          	: 'smap1.nombre',
		            		   valueField     : 'CDRFC',			autoSelect   : false,						forceSelection	: false,
		            		   typeAhead      : false,				anyMatch     : false,						hideTrigger   	: true,
		            		   enableKeyEvents: true,				store        : storeObtenerPersona,
		            		   listeners: {
		            			   select: function(comb, records){
		            				   var form=_p22_formBusqueda();
		            				   form.down('[name=smap1.rfc]').reset();
		            				   form.down('[name=smap1.rfcCliente]').setValue(records[0].get('CDRFC'));
		            				   form.down('[name=smap1.tipoPersona]').setValue(records[0].get('OTFISJUR'));
		            				   form.down('[name=smap1.nombreCliente]').setValue(records[0].get('DSNOMBRE')+" "+records[0].get('DSNOMBRE1')+" "+records[0].get('DSAPELLIDO')+" "+records[0].get('DSAPELLIDO1'));
		            				   form.down('[name=smap1.domicilio]').setValue(records[0].get('DIRECCIONCLI'));
		            			   }, 
		            			   keydown: function( com, e, eOpts ){
		            				   var form=_p22_formBusqueda();
		            				   if(e.isSpecialKey() || e.isNavKeyPress() || e.getKey() == e.CONTEXT_MENU || e.getKey() == e.HOME || e.getKey() == e.NUM_CENTER 
		            						   || e.getKey() == e.F1 || e.getKey() == e.F2 || e.getKey() == e.F3 || e.getKey() == e.F4 || e.getKey() == e.F5 || e.getKey() == e.F6 
		            						   || e.getKey() == e.F7 || e.getKey() == e.F8 || e.getKey() == e.F9 || e.getKey() == e.F10 || e.getKey() == e.F11 || e.getKey() == e.F12){
		            					   if(e.getKey() == e.BACKSPACE){
		            						   form.down('[name=smap1.nombre]').getStore().removeAll();
		            					   }
		            				   }else{
		            					   form.down('[name=smap1.rfc]').reset();
		            					   form.down('[name=smap1.nombre]').getStore().removeAll();
		            				   }
		            			   },
		            			   change: function(me, val){
		            				   try{
		            					   if('string' == typeof val){
		            						   me.setValue(val.toUpperCase());
		            					   }
		            				   }
		            				   catch(e){
		            					   debug(e);
		            				   }
		            			   }
		            		   }
		            	   },
		            	   {	xtype	: 'textfield',		fieldLabel : 'RFC',					name		: 'smap1.rfcCliente',		hidden:true		},
		            	   {	xtype	: 'textfield',		fieldLabel : 'Status',				name		: 'smap1.status',			value :'1',	hidden:true		},
		            	   {	xtype	: 'textfield',		fieldLabel : 'tipoPersona',			name		: 'smap1.tipoPersona',		hidden:true		},
		            	   agente,
		            	   {	xtype	: 'textfield',		fieldLabel : 'NombreCompleto',		name		: 'smap1.nombreCliente',	hidden:true		},
		            	   {	xtype	: 'textfield',		fieldLabel : 'Domicilio',			name		: 'smap1.domicilio',		hidden:true		},
		            	   comentariosText
	            	   ],
	            	   buttonAlign:'center',
	            	   buttons: [
        	             {
	            		   id:'botonAltaCliente',
	            		   icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
	            		   text: 'Alta Cliente',
	            		   handler: function() {
	            			   var formulario = this.up('form').getForm();
	            			   if (formulario.isValid()){
	            				   Ext.Ajax.request({
										url	 : _URL_GUARDA_CLIENTE
										,params:{
											'params.cdrfc'  	: _PanelPrincipalPersonas.down('[name=smap1.rfcCliente]').getValue(),
											'params.status'   	: _PanelPrincipalPersonas.down('[name=smap1.status]').getValue(),
											'params.cdtipper'   : _PanelPrincipalPersonas.down('[name=smap1.tipoPersona]').getValue(),
											'params.agente'     : _PanelPrincipalPersonas.down('combo[name=smap1.agente]').getValue(),
											'params.dsnombre'   : _PanelPrincipalPersonas.down('[name=smap1.nombreCliente]').getValue(),
											'params.dsdomicil'  : _PanelPrincipalPersonas.down('[name=smap1.domicilio]').getValue(),
											'params.obsermot'   : _PanelPrincipalPersonas.down('[name=smap1.comments]').getValue(),
											'params.proceso'    : proceso.procesoCliente,
											'params.accion'     : "I"												
										}
										,success : function (response){
											json = Ext.decode(response.responseText);
											if(json.success==false){
												Ext.Msg.show({
													title:'Error',
													msg: json.message,
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.WARNING
												});
											}else{
												mensajeCorrecto('&Eacute;XITO','El cliente se ha agregado Correctamente.');
												formulario.reset();
											}
											
										},
										failure : function () {
											Ext.Msg.show({
												title:'Error',
												msg: 'Error de comunicaci&oacute;n',
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.ERROR
											});
										}
									});

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
        	             },
        	             {
        	            	 text:'Cancelar',
        	            	 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
        	            	 handler:function(){
        	            		 var formulario = this.up('form').getForm();
        	            		 formulario.reset();
        	            		 windowCvePago.close();
        	            	 }
        	             }
    	             ]
			 	})   
			]
			});

		
		
		
		var panelInicialPral= Ext.create('Ext.form.Panel',{
			border      : 0,
			id          : 'panelInicialPral',
			renderTo    : 'div_clau',
			bodyPadding : 5,
			layout      : {
				type    : 'table'
					,columns: 3
			},
			defaults 	: {
				style   : 'margin:5px;'
			},
			items       :[
			              	asegurado,
			              	{	xtype   : 'button',
			            	  	colspan :2,
			            	  	id      : 'btnAltaCliente',
		            	  		text    : ' ',
		            	  		icon    : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
		            	  		handler : function() {
									windowCvePago = Ext.create('Ext.window.Window',{
										modal       : true,
										title		: 'Alta'+ complemento,
										buttonAlign : 'center',
										width       : 870,
										height		: 430,										
										autoScroll  : true,
										closeAction: 'hide',
										items       : [
							               		_PanelPrincipalPersonas
										],listeners:{
											close:function(){
												if(true){
													CargaListaCliente(null);
												}
											}
										}}).show();
										centrarVentana(windowCvePago);
		            	  		}
			              	}
			              ,{	colspan:3
			            	  ,border: false
			            	  ,items    : [
			            		   	gridListadoCliente 
		            		   	]
			              }
              ]
			});
		
		

		CargaListaCliente(null);
		
		function CargaListaCliente(rfc){
			
			if(proceso.procesoCliente =="1"){
				Ext.getCmp('btnAltaCliente').setText('Alta Clientes Non Gratos');
				complemento = " Clientes Non Gratos";
			}else if(proceso.procesoCliente =="2"){
				Ext.getCmp('btnAltaCliente').setText('Alta Clientes Políticamente Expuestos');
				complemento = " Clientes Políticamente Expuestos";
			}else{
				Ext.getCmp('btnAltaCliente').setText('Alta Clientes VIP');
				complemento = " Clientes VIP";
			}
			
			
			var params = {
					'params.cdrfc'				:	rfc
					,'params.proceso'	:	proceso.procesoCliente
				};
				
				cargaStorePaginadoLocal(storeListadoCliente, _URL_LISTA_CLIENTE_GRID, 'list', params, function(options, success, response){
					if(success){
						var jsonResponse = Ext.decode(response.responseText);
						if(jsonResponse.list && jsonResponse.list.length == 0) {
							centrarVentanaInterna(showMessage("Aviso", "No se encontraron "+complemento +".", Ext.Msg.OK, Ext.Msg.INFO));
							return;
						}
						
					}else{
						Ext.Msg.show({
							title: 'Aviso',
							msg: 'Error al obtener los datos.',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						});
					}
				});
		}
		
		
		/*Cargamos la información de los clientes non gratos*/

		
		function _p22_formBusqueda()
		{
		    debug('>_p22_formBusqueda<');
		    debug(Ext.ComponentQuery.query('#_p22_formBusqueda'));
			return Ext.ComponentQuery.query('#_p22_formBusqueda')[Ext.ComponentQuery.query('#_p22_formBusqueda').length-1];
		}
	});