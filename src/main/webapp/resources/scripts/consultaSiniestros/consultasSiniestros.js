Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
Ext.onReady(function() {

    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });
    
    // Conversion para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
    
    var storePagoDirecto = Ext.create('Ext.data.JsonStore', {
    	model:'Generic',
        proxy: {
            type: 'ajax',
            url: _URL_BUSQUEDA_DIRECTO,
            reader: {
                type: 'json',
                root: 'pagoDirecto'
            }
        }
    });
    storePagoDirecto.load();
    
    var storePagoReembolso = Ext.create('Ext.data.JsonStore', {
    	model:'Generic',
        proxy: {
            type: 'ajax',
            url: _URL_BUSQUEDA_REEMBOLSO,
            reader: {
                type: 'json',
                root: 'pagoReembolso'
            }
        }
    });
    storePagoReembolso.load();
    
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
    
    Ext.define('modelListaSiniestroTipoPago',{
        extend: 'Ext.data.Model',
        fields: [	{type:'string',    name:'feapertu'},			{type:'string',    name:'feocurre'},              	{type:'string',    name:'nmautser'},
                 	{type:'string',    name:'nmsinies'},			{type:'string',    name:'ntramite'},				{type:'string',    name:'asegurado'},
                 	{type:'string',    name:'proveedor'},			{type:'string',    name:'factura'}]
    });
    

    
    Ext.define('modelListadoProvMedico',{
	    extend: 'Ext.data.Model',
	    fields: [
            {type:'string', name:'cdpresta'},		{type:'string', name:'nombre'},        {type:'string', name:'cdespeci'},     {type:'string',name:'descesp'}
	    ]
	});
    
    storeListadoAseguradoReembolso = new Ext.data.Store(
    {
    	pageSize : 5
        ,model      : 'modelListaSiniestroTipoPago'
        ,autoLoad  : false
        ,proxy     :
        {
            enablePaging : true,            reader       : 'json',            type         : 'memory',            data         : []
        }
    });
    
    storeListadoPagoDirecto = new Ext.data.Store(
    {
    	pageSize : 5
        ,model      : 'modelListaSiniestroTipoPago'
        ,autoLoad  : false
        ,proxy     :
        {
            enablePaging : true,            reader       : 'json',            type         : 'memory',            data         : []
        }
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
    
    Ext.define('modelListAsegAfiliados',{
	    extend: 'Ext.data.Model',
	    fields: [
	                {type:'string',    name:'NTRAMITE'},		{type:'string',    name:'NFACTURA'},
	                {type:'string',    name:'NMSINIES'},        {type:'string',    name:'NMAUTSER'},        {type:'string',    name:'CDPERSON'},        {type:'string',    name:'NOMBRE'},
                    {type:'string',    name:'FEOCURRE'},        {type:'string',    name:'CDUNIECO'},        {type:'string',    name:'DSUNIECO'},        {type:'string',    name:'AAAPERTU'},
                    {type:'string',    name:'ESTADO'},        {type:'string',    name:'NMSITUAC'},        {type:'string',    name:'NMSUPLEM'},        {type:'string',    name:'CDRAMO'},
                    {type:'string',    name:'DSRAMO'},        {type:'string',    name:'CDTIPSIT'},        {type:'string',    name:'DSTIPSIT'},        {type:'string',    name:'STATUS'},
                    {type:'string',    name:'NMPOLIZA'},        {type:'string',    name:'VOBOAUTO'},        {type:'string',    name:'CDICD'},        {type:'string',    name:'DSICD'},
                    {type:'string',    name:'CDICD2'},        {type:'string',    name:'DSICD2'},        {type:'string',    name:'DESCPORC'},        {type:'string',    name:'DESCNUME'},
                    {type:'string',    name:'COPAGO'},        {type:'string',    name:'PTIMPORT'},        {type:'string',    name:'AUTRECLA'},        {type:'string',    name:'NMRECLAMO'},
                    {type:'string',    name:'COMMENAR'},        {type:'string',    name:'COMMENME'},        {type:'string',    name:'AUTMEDIC'},		{type:'string',    name:'NTRAMITE'},
                    {type:'string',    name:'USERASIGNADO'}
	            ]
	});
    
    var storeListAsegPagDirecto = new Ext.data.Store(
	{
	    autoDestroy: true,						model: 'modelListAsegAfiliados'
	});
    
    Ext.define('EditorAsegPagDirecto', {
    	extend: 'Ext.grid.Panel',
        requires: [
            //'Ext.selection.CellModel',
            'Ext.grid.*',
            'Ext.data.*',
            'Ext.util.*',
            'Ext.form.*'
        ],
        id:'EditorAsegPagDirecto',
        frame: false,
        initComponent: function(){
        	Ext.apply(this, {
        		height: 200,
                store: storeListAsegPagDirecto,
                columns: 
                [
                    {	text :'Usuario<br/>asignado',  width : 100,		   	align  :'center',		dataIndex       :'USERASIGNADO'},
                    {	text :'Tr&aacute;mite',        width : 100,			align  :'center', 	 	dataIndex       :'NTRAMITE'	},
                    {	text :'Factura',               width : 100,			align  :'center', 	 	dataIndex       :'NFACTURA'	},
                    {	text :'Id<br/>Sini.',          width : 100,			align  :'center', 	 	dataIndex       :'NMSINIES'	},
                    {   text :'# Auto.',          	   width : 100,         align  :'center',       dataIndex       :'NMAUTSER' },
                    {   text :'Clave<br/>asegu.',      width : 100,         align  :'center',       dataIndex       :'CDPERSON' },
                    {   text :'Nombre<br/>Asegurado',  width : 150,         align  :'center',       dataIndex       :'NOMBRE'   },
                    {	text :'Fecha<br/>Ocurrencia',  width : 100,         align  :'center',       dataIndex       :'FEOCURRE' },
                    {	text :'P&oacute;liza',         width : 100,			align  :'center',		dataIndex       :'NMPOLIZA'	},
                    {	text :'ICD', 				 width : 100,			align  :'center', 		dataIndex       :'CDICD'	},
                    {	text :'ICD2',                width : 100,           align  :'center',       dataIndex       :'CDICD2'	},
                    {	text :'Copago',              width : 100,           align  :'center',		dataIndex       :'COPAGO'	},
                    {	text :'$<br/>Facturado', 	 width : 100,           align  :'center',		dataIndex       :'PTIMPORT',	renderer       :Ext.util.Format.usMoney  },
                    {	text :'#<br/>Reclamo',       width : 150,		   	align  :'center',		dataIndex       :'NMRECLAMO'}
                    
                    
                ]
                ,listeners: {
                	itemclick: function(dv, record, item, index, e) {
                        /*Obtenemos los valores para los tabpaneles*/
                    	if(_TIPOPAGO != TipoPago.Reembolso)
                		{
                    		_CDUNIECO = record.get('CDUNIECO');
    			    		_CDRAMO = record.get('CDRAMO');
    		                _ESTADO = record.get('ESTADO');
    		                _NMPOLIZA = record.get('NMPOLIZA');
    		                _NMSITUAC = record.get('NMSITUAC');
    		                _NMSUPLEM = record.get('NMSUPLEM');
    		                _STATUS = record.get('STATUS');
    		                _AAAPERTU = record.get('AAAPERTU');
    		                _NMSINIES = record.get('NMSINIES');
    		                _TIPOPAGO = TipoPago.Directo;
    		                _NTRAMITE = record.get('NTRAMITE');
    		                _CDTIPSIT = record.get('CDTIPSIT');
    		                cargaInformacionTab();
                		}
                    	
                    }
                }
            });
            this.callParent();
        }
    });

    gridAsegPagDirecto=new EditorAsegPagDirecto();
    
    //Panel en el que se muestra la información, de la busqueda
    gridAseguradoReembolso  = Ext.create('Ext.grid.Panel',
	{
		id             : 'gridAseguradoReembolso'
		,store         :  storeListadoAseguradoReembolso
		,collapsible   : true
		,titleCollapse : true
		,autoScroll:true
		,style         : 'margin:5px'
		,selType: 'checkboxmodel'
		,width   : 600
		,height: 200
		,columns       :
		[
			 {		 header     : 'N&uacute;mero <br/> Tr&aacute;mite'				 ,dataIndex : 'ntramite'				 ,width	 	: 75		 },
			 {		 header     : 'N&uacute;mero <br/> Autorizaci&oacute;n Servicio' ,dataIndex : 'nmautser'				 ,width     : 150		 },
			 {		 header     : 'N&uacute;mero <br/> Siniestro'					 ,dataIndex : 'nmsinies'				 ,width     : 75		 },
			 {		 header     : 'Factura'									 		 ,dataIndex : 'factura'				 	 ,width	    : 75		 },
			 {		 header     : 'Fecha Ocurrencia'								 ,dataIndex : 'feocurre'				 ,width	    : 110		 },
			 {		 header     : 'Fecha Apertura'									 ,dataIndex : 'feapertu'				 ,width	    : 100		 },
			 {		 header     : 'Asegurado'									 	 ,dataIndex : 'asegurado'				 ,width	    : 200		 },
			 {		 header     : 'Proveedor'									 	 ,dataIndex : 'proveedor'				 ,width	    : 200		 }
			 
		],
		 bbar     :
		 {
			 displayInfo : true,
			 store       : storeListadoAseguradoReembolso,
			 xtype       : 'pagingtoolbar'
		 },
		 listeners: {
		        itemclick: function(dv, record, item, index, e) {
		        	limpiarRegistros();
		        	if(Ext.getCmp('gridAseguradoReembolso').getSelectionModel().hasSelection()){
							var rowSelected = Ext.getCmp('gridAseguradoReembolso').getSelectionModel().getSelection()[0];
							
							var ntramite= rowSelected.get('ntramite');
							_TIPOPAGO = TipoPago.Reembolso;
							validaTipoTramite(ntramite,_TIPOPAGO);
							validaTipoPagoReembolso(_TIPOPAGO,ntramite);
						}else {
							Ext.Msg.show({
								title: 'Aviso',
								msg: 'Debe de seleccionar un registro para realizar la edici&oacute;n',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR
							});
							
						}
					busquedaAsegurado.hide();
		        }
		    }
	});
    
    
	panelbusquedas = Ext.create('Ext.panel.Panel',
	{
		border  : 0,		id     : 'panelbusqueda',				width	: 600,		style  : 'margin:5px'
		,items :
			[
			 Ext.create('Ext.form.field.ComboBox',
				 {
					 fieldLabel 	: 'Nombre/C&oacute;digo asegurado',			allowBlank		: false,		displayField 	: 'value'
					 ,id			: 'idCodigoAsegurado',						labelWidth		: 170,			width		 	: 500
					 ,valueField   	: 'key',									forceSelection 	: true,		matchFieldWidth	: false
					 ,minChars  	: 2,										queryMode 		:'remote'		,queryParam: 'params.cdperson'
					 ,store : storeAsegurados,									hideTrigger:true				,triggerAction: 'all'
				 })
			]
		,buttonAlign: 'center'
		,buttons : [{
			text: "Buscar"
			,icon:_CONTEXT+'/resources/fam3icons/icons/magnifier.png'
			,handler: function(){
				if (panelClausula.form.isValid()) {
					storeListadoAseguradoReembolso.removeAll();
					var params = {
							'params.cdperson' : Ext.getCmp('idCodigoAsegurado').getValue()
					};
					
					cargaStorePaginadoLocal(storeListadoAseguradoReembolso, _URL_CONSULTA_LIST_ASEG_REEMBOLSO, 'datosSiniestroAsegurado', params, function(options, success, response){
						if(success){
							var jsonResponse = Ext.decode(response.responseText);
							if(jsonResponse.datosSiniestroAsegurado == null) {
								Ext.Msg.show({
									title: 'Aviso',
									msg: 'No se encontraron datos.',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.WARNING
								});
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
				} else {
					Ext.Msg.show({
						title: 'Aviso',
						msg: 'Complete la informaci&oacute;n requerida',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					});
				}
			}	
		}] 
	});
	
	
	panelClausula= Ext.create('Ext.form.Panel', {
		id: 'panelClausula',
		border:0,
		bodyPadding: 5,
		defaults 	:
		{	
			style : 'margin:5px;'
		}
		,
		items: [
		        	panelbusquedas 	// <-- contiene el formulario para la busqueda de acuerdo al codigo del asegurado
				    ,gridAseguradoReembolso 			// <-- Contiene la información de los asegurados
				]
		});
	
	
	busquedaAsegurado = Ext.create('Ext.window.Window',
	{
	    title        : 'Busqueda por Asegurado'
	    ,modal       : true
	    ,buttonAlign : 'center'
	    ,closable : true
	    ,autoScroll:true
	    ,closeAction: 'hide'
	    ,autoScroll:true
	    ,width		 : 650
	    ,minHeight 	 : 100 
	    ,maxheight      : 400
	    ,items       :
	        [
	            panelClausula
	        ]
	});
    
    gridProveedoresPDirecto  = Ext.create('Ext.grid.Panel',
	{
		id             : 'gridProveedoresPDirecto'
		,store         :  storeListadoPagoDirecto
		,collapsible   : true
		,titleCollapse : true
		,autoScroll:true
		,style         : 'margin:5px'
		,selType: 'checkboxmodel'
		,width   : 600
		,height: 300
		,columns       :
		[
			 {		 header     : 'N&uacute;mero <br/> Tr&aacute;mite'				 ,dataIndex : 'ntramite'				 ,width	 	: 75		 },
			 {		 header     : 'N&uacute;mero <br/> Autorizaci&oacute;n Servicio' ,dataIndex : 'nmautser'				 ,width     : 150		 },
			 {		 header     : 'N&uacute;mero <br/> Siniestro'					 ,dataIndex : 'nmsinies'				 ,width     : 75		 },
			 {		 header     : 'Factura'									 		 ,dataIndex : 'factura'				 	 ,width	    : 75		 },
			 {		 header     : 'Fecha Ocurrencia'								 ,dataIndex : 'feocurre'				 ,width	    : 110		 },
			 {		 header     : 'Fecha Apertura'									 ,dataIndex : 'feapertu'				 ,width	    : 100		 },
			 {		 header     : 'Asegurado'									 	 ,dataIndex : 'asegurado'				 ,width	    : 200		 },
			 {		 header     : 'Proveedor'									 	 ,dataIndex : 'proveedor'				 ,width	    : 200		 }
			 
		],
		 bbar     :
		 {
			 displayInfo : true,
			 store       : storeListadoPagoDirecto,
			 xtype       : 'pagingtoolbar'
		 },
         // Listener aqui va
	    listeners: {
	        itemclick: function(dv, record, item, index, e) {
	        	limpiarRegistros();
	        	if(Ext.getCmp('gridProveedoresPDirecto').getSelectionModel().hasSelection()){
						var rowSelected = Ext.getCmp('gridProveedoresPDirecto').getSelectionModel().getSelection()[0];
						var ntramite= rowSelected.get('ntramite');
						_TIPOPAGO = TipoPago.Directo;
						validaTipoPagoDirecto(ntramite, null,"1");
					}else {
						Ext.Msg.show({
							title: 'Aviso',
							msg: 'Debe de seleccionar un registro para realizar la edici&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						});
						
					}
	        	busquedaProveedorPDirecto.hide();
	        }
	    }
	});
    	    
    	    
    		busquedaProveedorDirecto = Ext.create('Ext.panel.Panel',
    		{
    			border  : 0,		id     : 'panelbusqueda',				width	: 600,		style  : 'margin:5px'
    			,items :
    				[
    	                {
    			            	id			: 'idProveedorInterno',  xtype       : 'combo',            	name        :'proveedorInterno',           	fieldLabel  : 'Proveedor',            	displayField: 'nombre',
    			            	valueField  : 'cdpresta',          	allowBlank  : false,		            	minChars  : 2,			            	width       : 500,
    			                forceSelection : true,              matchFieldWidth: false,		                queryMode   :'remote',	                queryParam  : 'params.cdpresta',
    			                store       : storeProveedor,	    triggerAction  : 'all',		                labelWidth  : 170,		                emptyText   : 'Seleccione...',
    			                editable    : true,                hideTrigger:true
    		            }
    				]
    			,buttonAlign: 'center'
    			,buttons : [{
    				text: "Buscar"
    				,icon:_CONTEXT+'/resources/fam3icons/icons/magnifier.png'
    				,handler: function(){
    					if (panelProveedorPDirecto.form.isValid()) {
    						storeListadoPagoDirecto.removeAll();
    						var params = {
    								'params.cdproveedor' : Ext.getCmp('idProveedorInterno').getValue()
    						};
    						cargaStorePaginadoLocal(storeListadoPagoDirecto, _URL_FACTURA_PDIRECTO, 'datosFacturaPagoDirecto', params, function(options, success, response){
    							if(success){
    								var jsonResponse = Ext.decode(response.responseText);
    								if(jsonResponse.datosFacturaPagoDirecto == null) {
    									Ext.Msg.show({
    										title: 'Aviso',
    										msg: 'No se encontraron datos.',
    										buttons: Ext.Msg.OK,
    										icon: Ext.Msg.WARNING
    									});
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
    					} else {
    						Ext.Msg.show({
    							title: 'Aviso',
    							msg: 'Complete la informaci&oacute;n requerida',
    							buttons: Ext.Msg.OK,
    							icon: Ext.Msg.WARNING
    						});
    					}
    				}	
    			}] 
    		});
    		
    		
    		panelProveedorPDirecto= Ext.create('Ext.form.Panel', {
    			id: 'panelProveedorPDirecto',
    			border:0,
    			bodyPadding: 5,
    			defaults 	:
    			{	
    				style : 'margin:5px;'
    			}
    			,
    			items: [
    			        	busquedaProveedorDirecto 	// <-- contiene el formulario para la busqueda de acuerdo al codigo del asegurado
    					    ,gridProveedoresPDirecto 			// <-- Contiene la información de los asegurados
    					]
    			});
    		
    		
    		busquedaProveedorPDirecto = Ext.create('Ext.window.Window',
    		{
    		    title        : 'Busqueda por Proveedor'
    		    ,modal       : true
    		    ,buttonAlign : 'center'
    		    ,closable : true
    		    ,autoScroll:true
    		    ,closeAction: 'hide'
    		    ,autoScroll:true
    		    ,width		 : 700
    		    ,height      : 450
    		    ,minHeight 	 : 100 
    		    //,maxheight      : 550
    		    ,items       :
    		        [
    		            panelProveedorPDirecto
    		        ]
    		});
    
    var tabDatosGeneralesPoliza =  Ext.create('Ext.tab.Panel', {
	    border : false,
	    items: [{
	            title: 'Informaci&oacute;n General'
    	    }, {
    	        title: 'Revisi&oacute;n Administrativa'
    	    }, {
    	        title: 'C&aacute;lculos'
    	    }/*, {
    	        title: 'Documentaci&oacute;n'
    	    }*/]
	});

    // SECCION DE INFORMACION GENERAL:
    
    var panelBusqueda = Ext.create('Ext.Panel', {
        id:'main-panel',
        baseCls:'x-plain',
        renderTo: 'dvConsultasPolizas',
        layout: {
            type: 'column',
            columns: 2
        },
        defaults: {frame:true, width:200, height: 200, margin : '2'},
        items:[{
            title:'BUSQUEDA DE SINIESTROS',
            colspan:2,
            width:990,
            height:140,
            items: [
                {
                    xtype: 'form',
                    url: _URL_CONSULTA_DATOS_SUPLEMENTO,
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
                                {boxLabel: 'Pago Directo', name: 'tipoBusqueda', inputValue: 1, checked: true, width: 200},
                                {boxLabel: 'Pago Reembolso', name: 'tipoBusqueda', inputValue: 2,width: 200},
                                {boxLabel: 'Pago Indemnizatorio', name: 'tipoBusqueda', inputValue: 3,width: 200}
                                
                            ],
                            listeners : {
                                change : function(radiogroup, newValue, oldValue, eOpts) {
                                	Ext.getCmp('subpanelBusquedas').query('panel').forEach(function(c){c.hide();});
                                	this.up('form').getForm().findField('params.pagoReembolso').setValue('');
                                	this.up('form').getForm().findField('params.pagoDirecto').setValue('');
                                	
                                    switch (newValue.tipoBusqueda) {
                                        case 1: //Pago Directo
                                            Ext.getCmp('subpanelBusquedaGral').show();
                                            break;
                                        case 2: //Pago Reembolso
                                            Ext.getCmp('subpanelBusquedaRFC').show();
                                            break;
                                        case 3: //Pago Indemnizatorio
                                        	Ext.getCmp('subpanelBusquedaRFC').show();
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
                            //border: false,
                            border  : 0,
                            style  : 'margin:5px',
                        	items: [
                                {
                                    id: 'subpanelBusquedaGral',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    defaults: {
                                        enforceMaxLength: true,
                                        msgTarget: 'side',
                                        labelAlign: 'right'
                                        	
                                    },
                                    items : [
										{
											xtype: 'combo',
											id:'tipoPagoDirecto',
											fieldLabel: 'Filtrar',
											store: storePagoDirecto,
											queryMode:'local',
											displayField: 'value',
											valueField: 'key',
											allowBlank:false,
											blankText:'Es un dato requerido',
											emptyText:'Seleccione tipo busqueda ...',
											listeners : {
									        	change:function(field,value){
									        		limpiarRegistros();
									        		debug("this.getValue() -->",this.getValue());
									        		// 1.- Trámite 	2.- Factura		3.- Proveedor
									        	    this.up('form').getForm().findField('params.pagoReembolso').setValue('');
									            	this.up('form').getForm().findField('params.pagoDirecto').setValue('');
									        		
									        		if(this.getValue() =="3")
									        		{
									        			this.up('form').getForm().findField('params.pagoDirecto').hide();
									    			}else{
									    				this.up('form').getForm().findField('params.pagoDirecto').show();
									    			}
										        }
									        }
										},
                                        {
                                            // Numero de poliza externo
                                            xtype : 'textfield',
                                            name : 'params.pagoDirecto',
                                            bodyStyle:'padding:5px;',
                                            fieldLabel : 'Busqueda',
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
									    	xtype: 'combo',	name:'tipoPagoReembolso',			fieldLabel: 'Filtrar',			store: storePagoReembolso,				queryMode:'local',
											displayField: 'value',			valueField: 'key',				allowBlank:false,						blankText:'Es un dato requerido',
											editable:false,					emptyText:'Seleccione tipo busqueda ...',
											listeners : {
									        	change:function(field,value){
									        		limpiarRegistros();
									        	    this.up('form').getForm().findField('params.pagoReembolso').setValue('');
									            	this.up('form').getForm().findField('params.pagoDirecto').setValue('');
									        		//Tipo de busqueda --> Por asegurado (2)
									        		if(this.getValue() =="2")
									        		{
									        			this.up('form').getForm().findField('params.pagoReembolso').hide();
									    			}else{
									    				this.up('form').getForm().findField('params.pagoReembolso').show();
									    			}
										        }
									        }
										},
										{
                                            xtype: 'textfield',
                                            name : 'params.pagoReembolso',
                                            fieldLabel : 'Busqueda',
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
                            id: 'btnBuscar',
                            handler: function(btn, e) {
                                var formBusqueda = this.up('form').getForm();
                                limpiarRegistros();
                                //Obtenemos el valor elegido en 'groupTipoBusqueda' para elegir el tipo de busqueda a realizar.
                                switch (formBusqueda.findField('groupTipoBusqueda').getValue().tipoBusqueda) {
                                    case 1: // --> Pago Directo
                                    	_TIPOPAGO = TipoPago.Directo;
                                    	// Busqueda por Datos Generales de la poliza:
                                    	if(!formBusqueda.findField('tipoPagoDirecto').isValid()){
                                            showMessage('', _MSG_FILTRO_BUSQUEDA, Ext.Msg.OK, Ext.Msg.INFO);
                                            return;
                                        }else{
                                        	// ---> Numero de tramite
                                        	if(formBusqueda.findField('tipoPagoDirecto').getValue() =="1")
                                        	{
	                                        	if(!formBusqueda.findField('params.pagoDirecto').isValid()){
	                                                showMessage('', _MSG_BUSQUEDA, Ext.Msg.OK, Ext.Msg.INFO);
	                                                return;
	                                            }
	                                        	validaTipoTramite(formBusqueda.findField('params.pagoDirecto').rawValue,_TIPOPAGO);
                                        	}
                                        	// --> Factura
                                        	if(formBusqueda.findField('tipoPagoDirecto').getValue() =="2")
                                        	{
	                                        	if(!formBusqueda.findField('params.pagoDirecto').isValid()){
	                                                showMessage('', _MSG_BUSQUEDA_FACTURA, Ext.Msg.OK, Ext.Msg.INFO);
	                                                return;
	                                            }
	                                        	validaTipoPagoDirecto(null,formBusqueda.findField('params.pagoDirecto').rawValue,"2");
                                        	}
                                        	// ---> Proveedor
                                        	if(formBusqueda.findField('tipoPagoDirecto').getValue() =="3")
                                        	{
	                                        	busquedaProveedorPDirecto.show();
                                        	}
                                        }
                                        
                                        
                                    break;
                                        
                                    case 2:  // --> Pago Reembolso
                                    	_TIPOPAGO = TipoPago.Reembolso;
                                    	if(!formBusqueda.findField('tipoPagoReembolso').isValid()){
                                            showMessage('', _MSG_FILTRO_BUSQUEDA, Ext.Msg.OK, Ext.Msg.INFO);
                                            return;
                                        }else{
                                        	//---> Numero de tramite
                                        	if(formBusqueda.findField('tipoPagoReembolso').getValue() =="1")
                                        	{
                                        		if(!formBusqueda.findField('params.pagoReembolso').isValid()){
                                                    showMessage('', _MSG_BUSQUEDA, Ext.Msg.OK, Ext.Msg.INFO);
                                                    return;
                                                }
                                        		validaTipoTramite(formBusqueda.findField('params.pagoReembolso').rawValue,_TIPOPAGO);
                                        	}else{
                                        		//---> Busqueda por asegurado
                                        		busquedaAsegurado.show();
                                        	}
                                    	}
                                    break;
                                }
                            }
                        }
                    ]
                }
            ]
        },
        {
            title:'ASEGURADOS AFECTADOS',
            width:990,
            height:200,
            colspan:2,
            autoScroll:true,
            items : [
                     gridAsegPagDirecto
            ]
        },
        {
            //title:
            width:990,
            height:600,
            colspan:2,
            autoScroll:true,
            items : [
                tabDatosGeneralesPoliza
            ]
        }]
    });
    
    
    function validaTipoTramite(numeroTramite, tipoPago)
    {
		Ext.Ajax.request(
		{
		    url     : _URL_MESA_DE_CONTROL
		    ,params:{
				'params.ntramite': numeroTramite
            }
		    ,success : function (response)
		    {
		    	if(Ext.decode(response.responseText).listaMesaControl != null)
	    		{
		    		var json=Ext.decode(response.responseText).listaMesaControl[0];
		    		if(tipoPago != json.otvalor02mc){
		    			Ext.Msg.show({
				            title:'Error',
				            msg: 'El tipo de pago no corresponde con el n&uacute;mero de tr&aacute;mite',
				            buttons: Ext.Msg.OK,
				            icon: Ext.Msg.ERROR
				        });
		    		}else{
		    			//--> Pago Directo
		    			if(tipoPago =="1"){
                        	validaTipoPagoDirecto(numeroTramite, null, "1");
		    			}else{
		    				// --> Pago por reembolso
		    				// Si es pago por reembolso insertar en la tabla el valor de otvalor05
		    				var rec = new modelListAsegAfiliados({
			    				USERASIGNADO: json.otvalor17mc
		                    });
			    			storeListAsegPagDirecto.add(rec);
		    				validaTipoPagoReembolso(tipoPago,numeroTramite);
		    			}
		    		}
	    		}else{
	    			Ext.Msg.show({
			            title:'Error',
			            msg: 'El n&uacute;mero de tr&aacute;mite no se encuentra registrado',
			            buttons: Ext.Msg.OK,
			            icon: Ext.Msg.ERROR
			        });
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
		return true;
    }
    
    
    function validaTipoPagoDirecto(numeroTramite, numeroFactura, tipoProceso)
    {
    	storeListAsegPagDirecto.removeAll();
        Ext.Ajax.request(
		{
		    url     : _URL_AFILIADOS_AFECTADOS
		    ,params:{
				'params.ntramite': numeroTramite
				,'params.tipoProceso': tipoProceso
				,'params.nfactura': numeroFactura
            }
		    ,success : function (response)
		    {
		    	var json = Ext.decode(response.responseText).slist1;
		    	debug("VALOR DEL JSON DE RESPUESTA DE AFILIADOS ",json);
		    	if(json != null)
	    		{
		    		var dato = Ext.decode(response.responseText).params;
		    		for(var i = 0; i < json.length; i++){
			    		var rec = new modelListAsegAfiliados({
		    				NTRAMITE: 	json[i].NTRAMITE,		NFACTURA: 	json[i].NFACTURA, 		NMSINIES: 	json[i].NMSINIES,
		    				NMAUTSER: 	json[i].NMAUTSER,		CDPERSON: 	json[i].CDPERSON,  		NOMBRE:   	json[i].NOMBRE,
		    				FEOCURRE:   json[i].FEOCURRE,		CDUNIECO:   json[i].CDUNIECO,  		DSUNIECO:   json[i].DSUNIECO,
		    				AAAPERTU:   json[i].AAAPERTU,		ESTADO:   	json[i].ESTADO,    		NMSITUAC:   json[i].NMSITUAC,
		    				NMSUPLEM:   json[i].NMSUPLEM,		CDRAMO:  	json[i].CDRAMO,    		CDTIPSIT:   json[i].CDTIPSIT,
		    				DSTIPSIT:   json[i].DSTIPSIT,		STATUS:   	json[i].STATUS,    		NMPOLIZA:   json[i].NMPOLIZA,
		    				VOBOAUTO:   json[i].VOBOAUTO,		CDICD:   	json[i].CDICD,			DSICD:   	json[i].DSICD,
		    				CDICD2:   	json[i].CDICD2,			DSICD2:   	json[i].DSICD2,			DESCPORC:   json[i].DESCPORC,
		    				DESCNUME:   json[i].DESCNUME,		COPAGO:   	json[i].COPAGO,			PTIMPORT:   json[i].IMPORTETOTALPAGO,
		    				AUTRECLA:   json[i].AUTRECLA,		NMRECLAMO:  json[i].NMRECLAMO,		COMMENAR:   json[i].COMMENAR,
		    				COMMENME:   json[i].COMMENME,		AUTMEDIC:   json[i].AUTMEDIC,		USERASIGNADO: json[i].USUARIO
	                    });
		    			storeListAsegPagDirecto.add(rec);
		    		}
	    		}else{
	    			Ext.Msg.show({
			            title:'Error',
			            msg: 'No existe informaci&oacute;n suficiente para dicho tr&aacute;mite',
			            buttons: Ext.Msg.OK,
			            icon: Ext.Msg.ERROR
			        });
	    		}
		    },
		    failure : function ()
		    {
		        //me.up().up().setLoading(false);
		        Ext.Msg.show({
		            title:'Error',
		            msg: 'Error de comunicaci&oacute;n',
		            buttons: Ext.Msg.OK,
		            icon: Ext.Msg.ERROR
		        });
		    }
		});
    	return true;
    }
    

    function validaTipoPagoReembolso(tipoPago,valorBusqueda){
    		Ext.Ajax.request(
			{
			    url     : _URL_CARGA_INFORMACION
			    ,params:{
					'params.ntramite': valorBusqueda,
					'params.tipoPago': tipoPago
                }
			    ,success : function (response)
			    {
			    	var json = Ext.decode(response.responseText);
		    		_CDUNIECO = json.params.cdunieco;
		    		_CDRAMO = json.params.cdramo;
	                _ESTADO = json.params.estado;
	                _NMPOLIZA = json.params.nmpoliza;
	                _NMSITUAC = json.params.nmsituac;
	                _NMSUPLEM = json.params.nmsuplem;
	                _STATUS = json.params.status;
	                _AAAPERTU = json.params.aaapertu;
	                _NMSINIES = json.params.nmsinies;
	                _NTRAMITE = json.params.ntramite;
	                _CDTIPSIT = json.params.cdtipsit;
	                cargaInformacionTab();

			    },
			    failure : function ()
			    {
			        //me.up().up().setLoading(false);
			        Ext.Msg.show({
			            title:'Error',
			            msg: 'Error de comunicaci&oacute;n',
			            buttons: Ext.Msg.OK,
			            icon: Ext.Msg.ERROR
			        });
			    }
			});
    	//}
    	return true;
    }
    
    
    //FUNCION QUE CARGA LA INFORMACION
    function limpiarRegistros()
    {
    	tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(0));
        tabDatosGeneralesPoliza.insert(0,{
        	title: 'Informaci&oacute;n General'
        });
        
        tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(1));
        tabDatosGeneralesPoliza.insert(1,{
        	title: 'Revisi&oacute;n Administrativa'
		});
        
        tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(2));
	    tabDatosGeneralesPoliza.insert(2,{
        	title: 'C&aacute;lculos'
        });
        
        /*tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(3));
	    tabDatosGeneralesPoliza.insert(3,{
        	title: 'Documentaci&oacute;n'
        });*/
	    
	    storeListAsegPagDirecto.removeAll();
	    return true;
    }
    
    function cargaInformacionTab(){
    	tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(0));
        tabDatosGeneralesPoliza.insert(0,{
        	title: 'Informaci&oacute;n General',
            loader: {
	        	url: _URL_LOADER_INFO_GRAL_RECLAMACION,
	        	scripts: true,
	        	autoLoad: true,
	        	loadMask : true,
	        	ajaxOptions: {
	        		method: 'POST'
	        	}
	        },
            listeners : {
                activate : function(tab) {
                    tab.loader.load();
                }
            }
        });
        
        tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(1));
        tabDatosGeneralesPoliza.insert(1,{
        	title: 'Revisi&oacute;n Administrativa',
        	loader: {
        		url: _URL_LOADER_REV_ADMIN,
        		scripts: true,
        		autoLoad: true,
        		loadMask : true,
        		ajaxOptions: {
        			method: 'POST'
		    	},
	        	params : {
	        		'params.cdunieco' : _CDUNIECO,
	        		'params.cdramo'   : _CDRAMO,
	        		'params.estado'   : _ESTADO,
	        		'params.nmpoliza' : _NMPOLIZA
	        	}
		    },
		    listeners : {
		        activate : function(tab) {
		            tab.loader.load();
		        }
		    }
		});
        
        tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(2));
	    tabDatosGeneralesPoliza.insert(2,{
        	title: 'C&aacute;lculos',
            loader: {
	        	url: _URL_LOADER_CALCULOS,
	        	scripts: true,
	        	autoLoad: true,
	        	loadMask : true,
	        	params:{
			    	'params.ntramite': _NTRAMITE
                },
                ajaxOptions: {
	        		method: 'POST'
	        	}
	        },
            listeners : {
                activate : function(tab) {
                    tab.loader.load();
                }
            }
        });
        
        /*tabDatosGeneralesPoliza.remove(tabDatosGeneralesPoliza.getComponent(3));
	    tabDatosGeneralesPoliza.insert(3,{
        	title: 'Documentaci&oacute;n',
            loader: {
	        	url: _URLDOCUMENTOSPOLIZA,
	        	scripts: true,
	        	autoLoad: true,
	        	loadMask : true,
	        	 params : {
                    'smap1.readOnly' :  true,
	        	 	'smap1.ntramite' :  _NTRAMITE,
                    'smap1.cdtippag' :  _TIPOPAGO,
	                'smap1.cdtipate' :  "",
	        	 	'smap1.cdtiptra' : _TIPO_TRAMITE_SINIESTRO,
	        	 	'smap1.cdunieco' :  _CDUNIECO,
                    'smap1.cdramo'   :  _CDRAMO,
                    'smap1.estado'   :  _ESTADO,
                    'smap1.nmpoliza' :  _NMPOLIZA,
                    'smap1.nmsuplem' :  _NMSUPLEM,
                    'smap1.nmsolici' : '',
                    'smap1.tipomov'  : '0'
                },
                ajaxOptions: {
	        		method: 'POST'
	        	}
	        },
            listeners : {
                activate : function(tab) {
                    tab.loader.load();
                }
            }
        });*/
        
	    tabDatosGeneralesPoliza.setActiveTab(0);
    	return true;
    }
});