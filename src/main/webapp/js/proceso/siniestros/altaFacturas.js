Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
Ext.onReady(function() {
	var valorIndexSeleccionado= null;
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
	
	Ext.selection.CheckboxModel.override( {
	    mode: 'SINGLE',
	    allowDeselect: true
	});
	//Models:
	Ext.define('modelFacturaSiniestro', {
	    extend:'Ext.data.Model',
	    fields:['noFactura','fechaFactura','tipoServicio','tipoServicioName','proveedor','proveedorName','importe','tipoMoneda','tipoMonedaName','tasaCambio','importeFactura']
	});
	
	Ext.define('modelListadoProvMedico',{
	    extend: 'Ext.data.Model',
		    fields: [
					{type:'string', 	name:'cdpresta'},	{type:'string', name:'nombre'},		{type:'string', name:'cdespeci'},	{type:'string',		name:'descesp'}
			]
	});
	
	Ext.define('modelListAsegPagDirecto',{
	    extend: 'Ext.data.Model',
	    fields: [
	                {type:'string',    name:'modUnieco'},		{type:'string',    name:'modEstado'},		    {type:'string',    name:'modRamo'},
	                {type:'string',    name:'modNmsituac'},		{type:'string',    name:'modPolizaAfectada'},	{type:'string',    name:'modCdpersondesc'},
	                {type:'string',    name:'modNmsolici'},		{type:'string',    name:'modNmsuplem'},		    {type:'string',    name:'modCdtipsit'},
	                {type:'string',    name:'modNmautserv'},	{type:'string',    name:'modFechaOcurrencia'},	{type:'string',    name:'modCdperson'},
	                {type:'string',    name:'modnumPoliza'}
	            ]
	});
	
    var storeTipoMoneda = Ext.create('Ext.data.JsonStore', {
		model:'Generic',
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CATALOGO_TipoMoneda},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
    storeTipoMoneda.load();
    
	oficinaEmisora = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url:_URL_CATALOGOS,
            extraParams : {
            	catalogo:_CATALOGO_OFICINA_RECEP,
            	'params.idPadre' : '1000'},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    
    var storeFacturaDirecto =new Ext.data.Store({
	    autoDestroy: true,
	    model: 'modelFacturaSiniestro'
	});
	
	var storeFacturaReembolso =new Ext.data.Store({
	    autoDestroy: true,
	    model: 'modelFacturaSiniestro'
	});
	
	var storePagoIndemnizatorio =new Ext.data.Store({
		autoDestroy: true,
		model: 'modelFacturaSiniestro'
	});

	var storePagoIndemnizatorioRecupera =new Ext.data.Store({
		autoDestroy: true,
		model: 'modelFacturaSiniestro'
	});
	
	var storeListAsegPagDirecto=new Ext.data.Store({
	    autoDestroy: true,						model: 'modelListAsegPagDirecto'
	});
	
	var storeTipoAtencion = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url:_UR_TIPO_ATENCION,
			reader: {
				type: 'json',
				root: 'listaTipoAtencion'
			}
		}
	});

	var storeTipoPago = Ext.create('Ext.data.JsonStore', {
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
	
    storeRamos = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CAT_RAMO_SALUD},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
	storeRamos.load();

    cmbRamos = Ext.create('Ext.form.field.ComboBox',{
		fieldLabel   : 'Producto',				allowBlank     : false,				editable   : false,
		displayField: 'value',					valueField: 'key',					forceSelection : false,				queryMode :'local',
		width: 300,								name:'cmbRamos',					store 		   : storeRamos,		readOnly : true,
		listeners : {
    		'select':function(e){
    			panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(null);
    			storeTipoAtencion.load({
					params:{
						'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue(),
						'params.tipoPago':panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
					}
				});
    		}
    	}
	});
    
	var tipoPago = Ext.create('Ext.form.ComboBox',{
    	name:'cmbTipoPago',						fieldLabel: 'Tipo pago',			allowBlank:false,			editable:false,
    	displayField: 'value',					valueField: 'key',					queryMode:'local',			emptyText:'Seleccione...',
    	width		 : 300,						store: storeTipoPago,				readOnly : true,
    	listeners : {
    		'select':function(e){
    			limpiarRegistrosTipoPago(e.getValue());
    		}
    	}
	});
	
	var comboTipoAte = Ext.create('Ext.form.ComboBox',{
        name:'cmbTipoAtencion',					fieldLabel: 'Tipo atenci&oacute;n',	allowBlank : false,			editable:true,
        displayField: 'value',					emptyText:'Seleccione...',			valueField: 'key',			forceSelection : true,
        width		 : 300,						queryMode      :'local',			store: storeTipoAtencion,	readOnly : true
    });
    
    cmbProveedor = Ext.create('Ext.form.field.ComboBox', {
    	colspan:2,
    	fieldLabel : 'Proveedor',		displayField : 'nombre',		name:'cmbProveedor',		valueField   : 'cdpresta',
    	forceSelection : true,			matchFieldWidth: false,			queryMode :'remote',		queryParam: 'params.cdpresta',
    	minChars	: 2,				store : storeProveedor,			triggerAction: 'all',		hideTrigger:true,	allowBlank:false,
    	width		: 300,				readOnly : true
    }); 
	
	cmbTipoMoneda = Ext.create('Ext.form.ComboBox',{
        id:'cmbTipoMoneda',			store: storeTipoMoneda,		value:'001',		queryMode:'local',
        displayField: 'value',		valueField: 'key',			editable:false,		allowBlank:false
        ,listeners : {
			select:function(e){
				if(e.getValue() =='001'){
					// EL TIPO DE MONEDA ES PESO
					valorIndexSeleccionado.set('tasaCambio','0.00');
					valorIndexSeleccionado.set('importeFactura','0.00');
				}else{
					var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
					var importeFactura = valorIndexSeleccionado.get('importeFactura');
					var importeMxn = +tasaCambio * +importeFactura;
					valorIndexSeleccionado.set('importe',importeMxn);
				}
			}
        }
    });
	
    cmbProveedorReembolso = Ext.create('Ext.form.field.ComboBox', {
    	displayField : 'nombre',		name:'cmbProveedorReembolso',	valueField   : 'cdpresta',
    	forceSelection : true,			matchFieldWidth: false,			queryMode :'remote',		queryParam: 'params.cdpresta',
    	width		 : 300,				minChars  : 2,					store : storeProveedor,		triggerAction: 'all',
    	hideTrigger:true,	allowBlank:false
    });

    /*////////////////////////////////////////////////////////////////
    ////////////////   DECLARACION DE EDITOR DE INCISOS  ////////////
    ///////////////////////////////////////////////////////////////*/

    //1.- GRID´S PARA EL PAGO DIRECTO
    Ext.define('EditorFacturaDirecto', {
 		extend: 'Ext.grid.Panel',
		name:'editorFacturaDirecto',
 		title: 'Alta de facturas Pago Directo',
 		frame: true,
		selType  : 'rowmodel',
	 	initComponent: function(){
	 			Ext.apply(this, {
	 			width: 750,
	 			height: 250
	 			,plugins  : [
					Ext.create('Ext.grid.plugin.CellEditing', {
					clicksToEdit: 1
					,listeners : {
						beforeedit : function() {
							valorIndexSeleccionado = gridFacturaDirecto.getView().getSelectionModel().getSelection()[0];
						}
					}
		            })
		        ],
	 			store: storeFacturaDirecto,
	 			columns: 
	 			[
				 	{	header: 'No. de Factura',			dataIndex: 'noFactura',			flex:2,  allowBlank: false
				 		,editor: {		
				 				xtype: 'textfield',
				                editable : true,
				                allowBlank: false
			            }
				 	},
				 	{
				 		header: 'Fecha de Factura',			dataIndex: 'fechaFactura',		flex:2,			 	renderer: Ext.util.Format.dateRenderer('d/m/Y'),  allowBlank: false
				 		,editor : {
							xtype : 'datefield',
							format : 'd/m/Y',
							editable : true,
							allowBlank: false
						}
				 	},
				 	{
						header: 'Moneda', 				dataIndex: 'tipoMonedaName',	flex:2,  allowBlank: false
						,renderer : function(v) {
						var leyenda = '';
							if (typeof v == 'string') {
								storeTipoMoneda.each(function(rec) {
									if (rec.data.key == v) {
										leyenda = rec.data.value;
									}
								});
							}else{
								if (v.key && v.value){
									leyenda = v.value;
								} else {
									leyenda = v.data.value;
								}
							}
							return leyenda;
						}
					},
				 	{ 	header: 'Tasa cambio', 				dataIndex: 'tasaCambio',		flex:2,				renderer: Ext.util.Format.usMoney,  allowBlank: false	 	},
				 	{ 	header: 'Importe Factura', 			dataIndex: 'importeFactura',	flex:2,				renderer: Ext.util.Format.usMoney,  allowBlank: false		},
				 	{	header: 'Importe MXN', 				dataIndex: 'importe',		 	flex:2,				renderer: Ext.util.Format.usMoney,  allowBlank: false
					 	,editor: {
							xtype: 'textfield',
							allowBlank: false,
							editable : true,
							minValue: 1,
							listeners : {
								change:function(e){
									validarFacturaPagada(panelInicialPral.down('combo[name=cmbProveedor]').getValue(),valorIndexSeleccionado.get('noFactura'), e.getValue());
								}
							}
			            }
				 	},
				 	{	xtype: 'actioncolumn',			width: 30,		sortable: false,		menuDisabled: true,
					 	items: [{
					 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
					 		tooltip: 'Quitar inciso',
					 		scope: this,
					 		handler: this.onRemoveClick
				 		}]
				 	}
		 		],
		 		selModel: {
			 		selType: 'cellmodel'
			 	},
		 		tbar: [{
                    text     : 'Agregar Factura'
                    ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                    ,handler : _p21_agregarGrupoClic
                }]
		 	});
 			this.callParent();
	 	},
	 	onRemoveClick: function(grid, rowIndex){
	 		var record=this.getStore().getAt(rowIndex);
	 		this.getStore().removeAt(rowIndex);
	 	}
 	});
    gridFacturaDirecto=new EditorFacturaDirecto();
    
	Ext.define('EditorFacturaReembolso', {
 		extend: 'Ext.grid.Panel',
		name:'editorFacturaReembolso',
 		title: 'Alta de facturas Pago Reembolso',
 		frame: true,
		selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
	 	initComponent: function(){
	 			Ext.apply(this, {
	 			width: 750,
	 			height: 250
	 			,plugins  :
		        [
		            Ext.create('Ext.grid.plugin.CellEditing', {
		                clicksToEdit: 1
		                ,listeners : {
								beforeedit : function() {
									valorIndexSeleccionado = gridFacturaReembolso.getView().getSelectionModel().getSelection()[0];
									debug('valorIndexSeleccionado:',valorIndexSeleccionado);
								}
							}
		            })
		        ],
	 			store: storeFacturaReembolso,
	 			columns: 
	 			[
				 	{ 	xtype: 'actioncolumn',		 	width: 40,			 sortable: false,		 	menuDisabled: true,
					 	items: [{
					 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
					 		tooltip: 'Quitar inciso',
					 		scope: this,
					 		handler: this.onRemoveClick
				 		}]
				 	},
				 	{	header: 'No. de Factura',			dataIndex: 'noFactura',			flex:2
				 		,editor: {
				                xtype: 'textfield',
				                allowBlank: false
			            }
				 	},
				 	{	header: 'Fecha de Factura',			dataIndex: 'fechaFactura',		flex:2,			 	renderer: Ext.util.Format.dateRenderer('d/m/Y')
				 		,editor : {
							xtype : 'datefield',
							format : 'd/m/Y',
							editable : true
						}
				 	},
				 	{	header: 'Proveedor', 				dataIndex: 'proveedorName',	flex:2
						,editor : cmbProveedorReembolso
						,renderer : function(v) {
						var leyenda = '';
							if (typeof v == 'string'){
								storeProveedor.each(function(rec) {
									if (rec.data.cdpresta == v) {
										leyenda = rec.data.nombre;
									}
								});
							}else{
								if (v.key && v.value) {
									leyenda = v.value;
								} else {
									leyenda = v.data.value;
								}
							}
							return leyenda;
						}
					},
					{	header: 'Moneda', 				dataIndex: 'tipoMonedaName',	flex:2
						,editor : cmbTipoMoneda
						,renderer : function(v) {
							var leyenda = '';
							if (typeof v == 'string'){
								storeTipoMoneda.each(function(rec) {
									if (rec.data.key == v) {
										leyenda = rec.data.value;
									}
								});
							}else{
								if (v.key && v.value){
									leyenda = v.value;
								} else {
									leyenda = v.data.value;
								}
							}
							return leyenda;
						}
					},
				 	{ 	header: 'Tasa cambio', 				dataIndex: 'tasaCambio',	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
				                xtype: 'textfield',
				                allowBlank: false,
				                listeners : {
									change:function(e){
										var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
										if(tipoMoneda =='001'){
											// EL TIPO DE MONEDA ES PESO
											valorIndexSeleccionado.set('tasaCambio','0.00');
											valorIndexSeleccionado.set('importeFactura','0.00');
											//valorIndexSeleccionado.set('PTMTOARA','0');
										}else{
											var tasaCambio = e.getValue();
											var importeFactura = valorIndexSeleccionado.get('importeFactura');
											var importeMxn = +tasaCambio * +importeFactura;
											valorIndexSeleccionado.set('importe',importeMxn);
											validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
										}
									}
						        }
			            }
				 	},
				 	{ 	header: 'Importe Factura', 				dataIndex: 'importeFactura',		 	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
				                xtype: 'textfield',
				                allowBlank: false,
				                listeners : {
									change:function(e){
										var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
										if(tipoMoneda =='001'){
											// EL TIPO DE MONEDA ES PESO
											valorIndexSeleccionado.set('tasaCambio','0.00');
											valorIndexSeleccionado.set('importeFactura','0.00');
										}else{
											var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
											var importeFactura = e.getValue();
											var importeMxn = +tasaCambio * +importeFactura;
											valorIndexSeleccionado.set('importe',importeMxn);
											validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
										}
									}
						        }
			            }
				 	},
				 	{	header: 'Importe MXN', 					dataIndex: 'importe',		 	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
							xtype: 'textfield',
							allowBlank: false,
							listeners : {
								change:function(e){
									validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), e.getValue());
								}
							}
			            }
				 	}
		 		],
		 		tbar: [
			 		{	text     : 'Agregar Factura'
	                    ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
	                    ,handler : _p21_agregarGrupoClic
	                }
		 		]
		 	});
 			this.callParent();
	 	},
	 	onRemoveClick: function(grid, rowIndex){
	 		var record=this.getStore().getAt(rowIndex);
	 		this.getStore().removeAt(rowIndex);
	 	}
 	});
    gridFacturaReembolso =new EditorFacturaReembolso();

	Ext.define('EditorPagoIndemnizatorio', {
 		extend: 'Ext.grid.Panel',
		name:'editorPagoIndemnizatorio',
 		title: 'Alta de Pago Indemnizatorio',
 		frame: true,
		selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
	 	initComponent: function(){
	 			Ext.apply(this, {
	 			width: 750,
	 			height: 250
	 			,plugins  :
		        [
		            Ext.create('Ext.grid.plugin.CellEditing', {
		                clicksToEdit: 1
		                ,listeners : {
								beforeedit : function(){
									valorIndexSeleccionado = gridPagoIndemnizatorio.getView().getSelectionModel().getSelection()[0];
									debug('valorIndexSeleccionado:',valorIndexSeleccionado);
								}
							}
		            })
		        ],
	 			store: storePagoIndemnizatorio,
	 			columns: 
	 			[
				 	{ 	xtype: 'actioncolumn',	 	width: 40,		 	sortable: false,		 	menuDisabled: true,
					 	items: [{
					 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
					 		tooltip: 'Quitar inciso',
					 		scope: this,
					 		handler: this.onRemoveClick
				 		}]
				 	},
				 	{	header: 'No. de Factura',			dataIndex: 'noFactura',			flex:2, 		hidden:true				 	},
				 	{	header: 'Fecha de Ingreso',			dataIndex: 'fechaFactura',		flex:2,			 	renderer: Ext.util.Format.dateRenderer('d/m/Y')
				 		,editor : {
							xtype : 'datefield',
							format : 'd/m/Y',
							editable : true,
							allowBlank: false
						}
				 	},
				 	{	header: 'Proveedor', 				dataIndex: 'proveedorName',	flex:2
						,editor : cmbProveedorReembolso
						,renderer : function(v) {
						var leyenda = '';
							if (typeof v == 'string'){
								storeProveedor.each(function(rec) {
									if (rec.data.cdpresta == v) {
										leyenda = rec.data.nombre;
									}
								});
							}else {
								if (v.key && v.value){
									leyenda = v.value;
								} else {
									leyenda = v.data.value;
								}
							}
							return leyenda;
						}
					},
					{	header: 'Moneda', 				dataIndex: 'tipoMonedaName',	flex:2
						,editor : cmbTipoMoneda
						,renderer : function(v) {
						var leyenda = '';
							if (typeof v == 'string'){
								storeTipoMoneda.each(function(rec) {
									if (rec.data.key == v) {
										leyenda = rec.data.value;
									}
								});
							}else {
								if (v.key && v.value){
									leyenda = v.value;
								} else {
									leyenda = v.data.value;
								}
							}
							return leyenda;
						}
					},
				 	{ 	header: 'Tasa cambio', 				dataIndex: 'tasaCambio',	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
				                xtype: 'textfield',
				                allowBlank: false,
				                listeners : {
									change:function(e){
										var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
										if(tipoMoneda =='001'){
											// EL TIPO DE MONEDA ES PESO
											valorIndexSeleccionado.set('tasaCambio','0.00');
											valorIndexSeleccionado.set('importeFactura','0.00');
											//valorIndexSeleccionado.set('PTMTOARA','0');
										}else{
											var tasaCambio = e.getValue();
											var importeFactura = valorIndexSeleccionado.get('importeFactura');
											var importeMxn = +tasaCambio * +importeFactura;
											valorIndexSeleccionado.set('importe',importeMxn);
											validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
										}
									}
						        }
			            }
				 	},
				 	{ 	header: 'Importe Factura', 				dataIndex: 'importeFactura',		 	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
				                xtype: 'textfield',
				                allowBlank: false,
				                listeners : {
									change:function(e){
										var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
										if(tipoMoneda =='001'){
											// EL TIPO DE MONEDA ES PESO
											valorIndexSeleccionado.set('tasaCambio','0.00');
											valorIndexSeleccionado.set('importeFactura','0.00');
										}else{
											var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
											var importeFactura = e.getValue();
											var importeMxn = +tasaCambio * +importeFactura;
											valorIndexSeleccionado.set('importe',importeMxn);
											validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
										}
									}
						        }
			            }
				 	},
				 	{ 	header: 'Importe MXN', 					dataIndex: 'importe',		 	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
							xtype: 'textfield',
							allowBlank: false,
							listeners : {
								change:function(e){
									validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), e.getValue());
								}
							}
			            }
				 	}
		 		],
		 		tbar: [
			 		{   text     : 'Agregar Documento'
	                    ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
	                    ,handler : _p21_agregarGrupoClic
	                }
		 		]
		 	});
 			this.callParent();
	 	},
	 	onRemoveClick: function(grid, rowIndex){
	 		var record=this.getStore().getAt(rowIndex);
	 		this.getStore().removeAt(rowIndex);
	 	}
 	});
    gridPagoIndemnizatorio =new EditorPagoIndemnizatorio();
    
	Ext.define('EditorPagoIndemnizatorioRecupera', {
 		extend: 'Ext.grid.Panel',
		name:'editorPagoIndemnizatorioRecupera',
 		title: 'Alta de Pago Indemnizatorio',
 		frame: true,
		selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
	 	initComponent: function(){
	 			Ext.apply(this, {
	 			width: 750,
	 			height: 250
	 			,plugins  :
		        [
		            Ext.create('Ext.grid.plugin.CellEditing',
		            {
		                clicksToEdit: 1
		                ,listeners :
							{
								beforeedit : function()
								{
									valorIndexSeleccionado = gridPagoIndemnizatorioRecupera.getView().getSelectionModel().getSelection()[0];
								}
							}
		            })
		        ],
	 			store: storePagoIndemnizatorioRecupera,
	 			columns: 
	 			[
				 	{
					 	xtype: 'actioncolumn',
					 	width: 40,
					 	sortable: false,
					 	menuDisabled: true,
					 	items: [{
					 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
					 		tooltip: 'Quitar inciso',
					 		scope: this,
					 		handler: this.onRemoveClick
				 		}]
				 	},
				 	{	
				 		header: 'No. de Factura',			dataIndex: 'noFactura',			flex:2, 		hidden:true
				 	},
				 	{
				 		header: 'Fecha de Factura',			dataIndex: 'fechaFactura',		flex:2,			 	renderer: Ext.util.Format.dateRenderer('d/m/Y')
				 	},
				 	{	header: 'Proveedor',			dataIndex: 'proveedorName',			flex:2
					 	,editor: {
							xtype: 'textfield',
							allowBlank: false
						}
					},
					{
						header: 'Moneda', 				dataIndex: 'tipoMonedaName',	flex:2
						,editor : cmbTipoMoneda
						,renderer : function(v) {
						var leyenda = '';
							if (typeof v == 'string')// tengo solo el indice
							{
								storeTipoMoneda.each(function(rec) {
									if (rec.data.key == v) {
										leyenda = rec.data.value;
									}
								});
							}else // tengo objeto que puede venir como Generic u otro mas complejo
							{
								if (v.key && v.value)
								{
									leyenda = v.value;
								} else {
									leyenda = v.data.value;
								}
							}
							return leyenda;
						}
					}
				 	,
				 	{
					 	header: 'Tasa cambio', 				dataIndex: 'tasaCambio',	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
				                xtype: 'textfield',
				                allowBlank: false,
				                listeners : {
									change:function(e){
										var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
										if(tipoMoneda =='001'){
											// EL TIPO DE MONEDA ES PESO
											valorIndexSeleccionado.set('tasaCambio','0.00');
											valorIndexSeleccionado.set('importeFactura','0.00');
											
											//valorIndexSeleccionado.set('PTMTOARA','0');
										}else{
											var tasaCambio = e.getValue();
											var importeFactura = valorIndexSeleccionado.get('importeFactura');
											var importeMxn = +tasaCambio * +importeFactura;
											valorIndexSeleccionado.set('importe',importeMxn);
											//validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
										}
									}
						        }
			            }
				 	},
				 	{
					 	header: 'Importe Factura', 				dataIndex: 'importeFactura',		 	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
				                xtype: 'textfield',
				                allowBlank: false,
				                listeners : {
									change:function(e){
										var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
										if(tipoMoneda =='001'){
											// EL TIPO DE MONEDA ES PESO
											valorIndexSeleccionado.set('tasaCambio','0.00');
											valorIndexSeleccionado.set('importeFactura','0.00');
										}else{
											var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
											var importeFactura = e.getValue();
											var importeMxn = +tasaCambio * +importeFactura;
											valorIndexSeleccionado.set('importe',importeMxn);
											//validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
										}
									}
						        }
			            }
				 	},
				 	{
					 	header: 'Importe MXN', 					dataIndex: 'importe',		 	flex:2,				renderer: Ext.util.Format.usMoney
					 	,editor: {
							xtype: 'textfield',
							allowBlank: false,
							listeners : {
								change:function(e){
									//validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), e.getValue());
								}
							}
						}
				 	}
		 		],
		 		tbar: [
			 		{
	                    text     : 'Agregar Documento'
	                    ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
	                    ,handler : _p21_agregarGrupoClic
	                }
		 		]
		 	});
 			this.callParent();
	 	},
	 	onRemoveClick: function(grid, rowIndex){
	 		var record=this.getStore().getAt(rowIndex);
	 		this.getStore().removeAt(rowIndex);
	 	}
 	});
    gridPagoIndemnizatorioRecupera =new EditorPagoIndemnizatorioRecupera();
    
    /* PANEL PARA LA BUSQUEDA DE LA INFORMACIÓN DEL ASEGURADO PARA LA BUSQUEDA DE LAS POLIZAS */
	var panelInicialPral= Ext.create('Ext.form.Panel',
    	    {
    	        border      : 0,
    	        id          : 'panelInicialPral',
    	        renderTo    : 'div_clau21',
    	        bodyPadding : 5,
    	        width       : 758,
    	        layout      : {
					type    : 'table'
					,columns: 2
				},
				defaults 	: {
					style   : 'margin:5px;'
				},
				items       :[
					{	xtype	: 'textfield',		fieldLabel : 'Tr&aacute;mite',			name		: 'nmtramite',			hidden:true, 		value: valorAction.ntramite},
					cmbRamos,
	            	tipoPago,
	            	comboTipoAte,
	        		{	xtype	   : 'datefield',			fieldLabel  : 'Fecha ocurrencia',		name		: 'dtFechaOcurrencia',		maxValue   :  new Date(),
	            		format	   : 'd/m/Y',				editable    : true,						width		: 300,						allowBlank : false,
	            		value 	   : valorAction.feOcurrencia,	hidden:true
                    },
            		cmbProveedor,
					{	colspan:2
				 		,border: false
				 		,items    :
				 			[
				 			 	gridFacturaDirecto 
			 			 	]
				 	},
				 	{
				 		colspan:2
				 		,border: false
				 		,items    :
				 			[
				 			 	gridFacturaReembolso
			 			 	]
				 	},
				 	{
				 		colspan:2
				 		,border: false
				 		,items    :
				 			[
				 			 	gridPagoIndemnizatorio
			 			 	]
				 	},
				 	{
				 		colspan:2
				 		,border: false
				 		,items    :
				 			[
				 			 	gridPagoIndemnizatorioRecupera
			 			 	]
				 	}
				 	
    	        ],
    	        buttonAlign:'center',
    	        buttons: [{
    	            id:'botonCotizar',
    	            icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
    	            text: 'Guardar Facturas',
            		handler: function() {
            			var form = this.up('form').getForm();
            			myMask.show();
            			if (form.isValid()){
    	                	
							if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){ // Pago Directo
        						var obtener = [];
                				storeFacturaDirecto.each(function(record) {
    								obtener.push(record.data);
								});
                				
                				if(obtener.length <= 0){
                					myMask.hide();
                					centrarVentanaInterna(Ext.Msg.show({
										title:'Error',
										msg: 'Se requiere ingresar al menos una factura',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR
									}));
									return false;
								}else{
									//Se tiene que validar el record de las facturas
	                				for(i=0;i < obtener.length;i++){
	                					if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
	                						obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""){
	                						myMask.hide();
	                						centrarVentanaInterna(Ext.Msg.show({
	    						                title:'Facturas',
	    						                msg: 'Favor de introducir los campos requeridos en la factura',
	    						                buttons: Ext.Msg.OK,
	    						                icon: Ext.Msg.WARNING
	    						            }));
	                						return false;
	                					}
									}
	                				
                				}
        					}else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
        						//PAGO POR REEMBOLSO
        						var obtener = [];
                				storeFacturaReembolso.each(function(record) {
    								obtener.push(record.data);
								});
								
								if(obtener.length <= 0){
									myMask.hide();
                					centrarVentanaInterna(Ext.Msg.show({
    						                title:'Error',
    						                msg: 'Se requiere ingresar al menos una factura',
    						                buttons: Ext.Msg.OK,
    						                icon: Ext.Msg.ERROR
    						            }));
    						            return false;
                				}else{
	                				
	                				for(i=0;i < obtener.length;i++){
	                					if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
	                						obtener[i].importeFactura == null ||obtener[i].proveedorName == null ||obtener[i].tasaCambio == null ||
	                						obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""||
	                						obtener[i].importeFactura == "" ||obtener[i].proveedorName == "" ||obtener[i].tasaCambio == ""){
	                						centrarVentanaInterna(Ext.Msg.show({
	    						                title:'Facturas',
	    						                msg: 'Favor de introducir los campos requeridos en la factura',
	    						                buttons: Ext.Msg.OK,
	    						                icon: Ext.Msg.WARNING
	    						            }));
	                						return false;
	                					}
									}
                				}
        					}else{ //PAGO POR INDEMNIZACION
        						var obtener = [];
        						
        						if(valorAction.cdramo == _RECUPERA){
									storePagoIndemnizatorioRecupera.each(function(record) {
	    								obtener.push(record.data);
									});
								}else{
									storePagoIndemnizatorio.each(function(record) {
	    								obtener.push(record.data);
									});
								}
								if(obtener.length <= 0){
									myMask.hide();
                					centrarVentanaInterna(Ext.Msg.show({
    						                title:'Error',
    						                msg: 'Se requiere ingresar al menos una documento',
    						                buttons: Ext.Msg.OK,
    						                icon: Ext.Msg.ERROR
    						            }));
    						            return false;
                				}else{
	                				for(i=0;i < obtener.length;i++){
	                					if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
	                						obtener[i].importeFactura == null ||obtener[i].proveedorName == null ||obtener[i].tasaCambio == null ||
	                						obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""||
	                						obtener[i].importeFactura == "" ||obtener[i].proveedorName == "" ||obtener[i].tasaCambio == ""){
	                						myMask.hide();
	                						centrarVentanaInterna(Ext.Msg.show({
	    						                title:'Facturas',
	    						                msg: 'Favor de introducir los campos requeridos en la factura',
	    						                buttons: Ext.Msg.OK,
	    						                icon: Ext.Msg.WARNING
	    						            }));
	                						return false;
	                					}
									}
                				}
        					}
        					var submitValues={};
            				var formulario=panelInicialPral.form.getValues();
            				submitValues['params']=formulario;
            				
            				var datosTablas = [];
        					
        					if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO) //PAGO DIRECTO
							{
								//Pago directo
								storeFacturaDirecto.each(function(record,index){
									datosTablas.push({
										nfactura:record.get('noFactura'),
										ffactura:record.get('fechaFactura'),
										cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
										cdpresta:panelInicialPral.down('combo[name=cmbProveedor]').getValue(),
										ptimport:record.get('importe'),
										cdmoneda:record.get('tipoMonedaName'),
										tasacamb:record.get('tasaCambio'),
										ptimporta:record.get('importeFactura')
									});
                				});
							}else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
								//Pago por Reembolso
								storeFacturaReembolso.each(function(record,index){
									datosTablas.push({
										nfactura:record.get('noFactura'),
										ffactura:record.get('fechaFactura'),
										cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
										cdpresta:record.get('proveedorName'),
										ptimport:record.get('importe'),
										cdmoneda:record.get('tipoMonedaName'),
										tasacamb:record.get('tasaCambio'),
										ptimporta:record.get('importeFactura')
									});
                				});
							}
							else{
								//Pago Indemnizatorios
								if(valorAction.cdramo == _RECUPERA){
									storePagoIndemnizatorioRecupera.each(function(record,index){
										datosTablas.push({
											nfactura:record.get('noFactura'),
											ffactura:record.get('fechaFactura'),
											cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
											cdpresta:record.get('proveedorName'),
											nombprov:record.get('proveedorName'),
											ptimport:record.get('importe'),
											cdmoneda:record.get('tipoMonedaName'),
											tasacamb:record.get('tasaCambio'),
											ptimporta:record.get('importeFactura')
										});
									})
								}else{
									storePagoIndemnizatorio.each(function(record,index){
										datosTablas.push({
											nfactura:record.get('noFactura'),
											ffactura:record.get('fechaFactura'),
											cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
											cdpresta:record.get('proveedorName'),
											ptimport:record.get('importe'),
											cdmoneda:record.get('tipoMonedaName'),
											tasacamb:record.get('tasaCambio'),
											ptimporta:record.get('importeFactura')
										});
	                				});
								}
							}
            				submitValues['datosTablas']=datosTablas;
            				panelInicialPral.setLoading(true);
            				debug("VALORES A ENVIAR A GUARDAR --->");
            				debug(submitValues);
        					Ext.Ajax.request({
								url: _URL_GUARDA_FACTURAS_TRAMITE,
								jsonData:Ext.encode(submitValues), // convierte a estructura JSON
								success:function(response,opts){
									panelInicialPral.setLoading(false);
									var jsonResp = Ext.decode(response.responseText);
									if(jsonResp.success == true){
										myMask.hide();
										mensajeCorrecto("&Eeacute;xito"," Se han guardado las Facturas",null);
										storeFacturaDirecto.removeAll();
										storeFacturaReembolso.removeAll();
										storePagoIndemnizatorio.removeAll();
										storePagoIndemnizatorioRecupera.removeAll();
									}
									else{
										myMask.hide();
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
									panelInicialPrincipal.setLoading(false);
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
    	        }
    	    ]
    	    });
    
    if(valorAction.ntramite != null){
    	panelInicialPral.down('combo[name=cmbRamos]').setValue(valorAction.cdramo);
    	panelInicialPral.down('combo[name=cmbTipoPago]').setValue(valorAction.cdTipoPago);
    	storeTipoAtencion.load({
			params:{
				'params.cdramo':valorAction.cdramo ,
				'params.cdtipsit':valorAction.cdtipsit,
				'params.tipoPago':valorAction.cdTipoPago
			}
		});
		panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(valorAction.cdTipoAtencion);
		storeProveedor.load();
		panelInicialPral.down('combo[name=cmbProveedor]').setValue(valorAction.cdpresta);
		limpiarRegistrosTipoPago(valorAction.cdTipoPago);
		
	}
    
//////////////////			FUNCIONES 		/////////////////////
	function limpiarRegistrosTipoPago(tipoPago){
		var pagoDirecto = true;
		var pagoReembolso = true;
		if(tipoPago == _TIPO_PAGO_DIRECTO) {// Pago Directo
		    pagoDirecto = false;
		    pagoReembolso = true;
		    panelInicialPral.down('[name=editorFacturaDirecto]').show();
		    panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
		    panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
		    panelInicialPral.down('[name=editorFacturaReembolso]').hide();
		    panelInicialPral.down('[name=dtFechaOcurrencia]').hide();
		    panelInicialPral.down('[name=dtFechaOcurrencia]').setValue('');
		    panelInicialPral.down('combo[name=cmbProveedor]').show();
		}else if(tipoPago == _TIPO_PAGO_REEMBOLSO){
		    pagoDirecto = true;
		    pagoReembolso = true;
		    panelInicialPral.down('[name=editorFacturaDirecto]').hide();
		    panelInicialPral.down('[name=editorFacturaReembolso]').show();
		    panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
		    panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
		    panelInicialPral.down('combo[name=cmbProveedor]').hide();
		    panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
		    panelInicialPral.down('[name=dtFechaOcurrencia]').hide();
		}else{
			pagoDirecto = true;
		    pagoReembolso = false;
		    panelInicialPral.down('[name=editorFacturaDirecto]').hide();
		    panelInicialPral.down('[name=editorFacturaReembolso]').hide();
		    if(valorAction.cdramo == _RECUPERA){
		    	panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
			    panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').show();
		    }else{
		    	panelInicialPral.down('[name=editorPagoIndemnizatorio]').show();
			    panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
		    }
		    panelInicialPral.down('combo[name=cmbProveedor]').hide();
		    panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
		    panelInicialPral.down('[name=dtFechaOcurrencia]').hide();
		}
		panelInicialPral.down('[name=dtFechaOcurrencia]').allowBlank = pagoReembolso;
		panelInicialPral.down('combo[name=cmbProveedor]').allowBlank = pagoDirecto;
    	return true;
	}
	
	function _p21_agregarGrupoClic(){
		if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
			var obtener = [];
			storeFacturaDirecto.each(function(record) {
				obtener.push(record.data);
			});
			if(obtener.length > 0){
				//Realizamos la validacion de la ultima factura que vayan agregando
				storeFacturaDirecto.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
			}else{
				storeFacturaDirecto.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
			}
		}else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
			storeFacturaReembolso.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
		}else{
			//Tenemos que verificar que tenga fecha de ocurrencia seleccionado
			var fechaOcurrencia = panelInicialPral.down('[name=dtFechaOcurrencia]').getValue();
			if(fechaOcurrencia == null){
				centrarVentanaInterna(mensajeError('Para agregar un documento se requiere la fecha de ocurrencia'));
			}else{
				if(valorAction.cdramo == _RECUPERA){
					storePagoIndemnizatorioRecupera.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001', importe:'0.00'}));
				}else{
					storePagoIndemnizatorio.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
				}
			}
		}
	}
	
	function validarFacturaPagada(cdpresta,nfactura, totalImporte){
		Ext.Ajax.request({
			url     : _URL_CONSULTA_FACTURA_PAGADA
			,params:{
				'params.nfactura' : nfactura,
				'params.cdpresta' : cdpresta,
				'params.ptimport' : totalImporte
			}
			,success : function (response) {
		    	if(Ext.decode(response.responseText).factPagada !=null){
		    		centrarVentanaInterna(Ext.Msg.show({
 		               title: 'Aviso',
 		               msg: 'La factura '+ nfactura +' ya se encuentra procesado en el tr&aacute;mite '+Ext.decode(response.responseText).factPagada,
 		               buttons: Ext.Msg.OK,
 		               icon: Ext.Msg.WARNING
 		           }));
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
});