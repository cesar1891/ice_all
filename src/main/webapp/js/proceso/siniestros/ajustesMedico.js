Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
var datosgrid;
var storeIncisos;
var tipoAccion = 0; // Nuevo

Ext.define('modelAjusteMedico',{
    extend: 'Ext.data.Model',
    fields: [
             {type:'string', name:'COMMENTS'},
             {type:'string', name:'PTIMPORT'},
             {type:'string', name:'NMORDMOV'}
    ]
});

Ext.define('modelInforGral', {
    extend:'Ext.data.Model',
    fields:['txtPrecio','txtCantidad','','txtDescPor','txtDescNum','txtAjusteMed','txtSubTotalFac','txtSbubTotalAjuste']
});

Ext.define('modelAjusteMedico',{
    extend: 'Ext.data.Model',
    fields: [
             {type:'string', name:'COMMENTS'},
             {type:'string', name:'PTIMPORT'},
             {type:'string', name:'NMORDMOV'}
    ]
});

var storeModelInforGral =new Ext.data.Store(
{
    autoDestroy: true,
    model: 'modelInforGral'
});

storeIncisos=new Ext.data.Store(
{
    autoDestroy: true,
    model: 'modelAjusteMedico'
});

var storeTipoConcepto = Ext.create('Ext.data.JsonStore', {
	model:'Generic',
	proxy: {
		type: 'ajax',
		url: _URL_CATALOGOS,
		extraParams : {catalogo:_CATALOGO_TipoConcepto},
		reader: {
			type: 'json',
			root: 'lista'
		}
	}
});
storeTipoConcepto.load();

var storeConceptosCatalogo = Ext.create('Ext.data.JsonStore', {
	model:'Generic',
	proxy: {
		type: 'ajax',
		url: _URL_CATALOGOS,
		extraParams : {catalogo:_CATALOGO_ConceptosMedicos},
		reader: {
			type: 'json',
			root: 'lista'
		}
	}
});

function _amRecargar()
{
	var json =
	{
		'params.cdunieco' : _amParams.cdunieco,
		'params.cdramo'   : _amParams.cdramo,
		'params.estado'   : _amParams.estado,
		'params.nmpoliza' : _amParams.nmpoliza,
		'params.nmsuplem' : _amParams.nmsuplem,
		'params.nmsituac' : _amParams.nmsituac,
		'params.aaapertu' : _amParams.aaapertu,
		'params.status'   : _amParams.status,
		'params.nmsinies' : _amParams.nmsinies,
		'params.nfactura' : _amParams.nfactura,
		'params.cdgarant' : _amParams.cdgarant,
		'params.cdconval' : _amParams.cdconval,
		'params.cdconcep' : _amParams.cdconcep,
		'params.idconcep' : _amParams.idconcep,
		'params.nmordina' : _amParams.nmordina,
		'params.precio'   : _amParams.precio,
		'params.cantidad' : _amParams.cantidad,
		'params.descuentonum' : _amParams.descuentonum,
		'params.importe'  : _amParams.importe,
		'params.ajusteMedi':_amParams.ajusteMedi//,
		//importetxtDescNum
	};
	//LLenamos la información de los combos
	storeTipoConcepto.load();
	Ext.getCmp('idConceptos').setValue(_amParams.idconcep);
	storeConceptosCatalogo.load({
		params: {
			'params.idPadre' : _amParams.idconcep
		}
	});
	
	Ext.getCmp('conceptosID').setValue(_amParams.cdconcep);
	Ext.Ajax.request(
	{
		url : _amUrlCargar
		,params : json
		,success : function(response)
		{
			json = Ext.decode(response.responseText);
			if(json.success == true)
			{
				storeIncisos.removeAll();
				storeIncisos.add(json.loadList);
				var arr = [];
	    		storeIncisos.each(function(record) {
		    	    arr.push(record.data);
		    	});
	    		var montoAjustado= 0;
	    		for(var i = 0; i < arr.length; i++)
		    	{
	    			montoAjustado = montoAjustado + (+ arr[i].PTIMPORT);
		    	}
	    		Ext.getCmp('idTotalAjustado').setValue(montoAjustado);
	    		
	    		storeModelInforGral.removeAll();
	    		var rec = new modelInforGral({
	    			txtPrecio:   _amParams.precio,
	    			txtCantidad: _amParams.cantidad,
	    			txtDescPor:  _amParams.descuentoporc,
	    			txtDescNum:  _amParams.descuentonum,
	    			txtSubTotalFac: _amParams.importe,
	    			txtAjusteMed: montoAjustado,
	    			txtSbubTotalAjuste: (+ _amParams.importe - +montoAjustado)
	    			});
	    		
	    		storeModelInforGral.add(rec);
			}
			else
			{
				mensajeError('No se pudieron obtener los ajustes m&eacute;dicos');
			}
		}
	    ,failure : function()
	    {
	    	gridIncisos.setLoading(false);
	    	errorComunicacion();
	    }
	});
}


function _amEliminar(nmordmov,ptimport,comments)
{
	var json =
	{
		'params.cdunieco' : _amParams.cdunieco,
		'params.cdramo'   : _amParams.cdramo,
		'params.estado'   : _amParams.estado,
		'params.nmpoliza' : _amParams.nmpoliza,
		'params.nmsuplem' : _amParams.nmsuplem,
		'params.nmsituac' : _amParams.nmsituac,
		'params.aaapertu' : _amParams.aaapertu,
		'params.status'   : _amParams.status,
		'params.nmsinies' : _amParams.nmsinies,
		'params.nfactura' : _amParams.nfactura,
		'params.cdgarant' : _amParams.cdgarant,
		'params.cdconval' : _amParams.cdconval,
		'params.cdconcep' : _amParams.cdconcep,
		'params.idconcep' : _amParams.idconcep,
		'params.nmordina' : _amParams.nmordina,
		'params.nmordmov' : nmordmov,
		'params.ptimport' : ptimport,
		'params.comments' : comments
	};
	
	Ext.Ajax.request(
	{
		url : _amUrlEliminar
		,params : json
		,success : function(response)
		{
			tipoAccion = 0;
			json = Ext.decode(response.responseText);
	    	if(json.success == true){
	    		_amRecargar();
	    	}
	    	else
	    	{
	    		mensajeError(json.mensaje);
	    	}
		}
	    ,failure : function()
	    {
	    	errorComunicacion();
	    }
	});
}


function _amModificar(consecutivo,ptimport,comments)
{
	var json =
	{
		'params.cdunieco' : _amParams.cdunieco,
		'params.cdramo'   : _amParams.cdramo,
		'params.estado'   : _amParams.estado,
		'params.nmpoliza' : _amParams.nmpoliza,
		'params.nmsuplem' : _amParams.nmsuplem,
		'params.nmsituac' : _amParams.nmsituac,
		'params.aaapertu' : _amParams.aaapertu,
		'params.status'   : _amParams.status,
		'params.nmsinies' : _amParams.nmsinies,
		'params.nfactura' : _amParams.nfactura,
		'params.cdgarant' : _amParams.cdgarant,
		'params.cdconval' : _amParams.cdconval,
		'params.cdconcep' : _amParams.cdconcep,
		'params.idconcep' : _amParams.idconcep,
		'params.nmordina' : _amParams.nmordina,
		'params.nmordmov' : consecutivo,
		'params.ptimport' : ptimport,
		'params.comments' : comments
	};
	
	Ext.Ajax.request(
	{
		url : _amUrlModificar
		,params : json
		,success : function(response)
		{
			json = Ext.decode(response.responseText);
			tipoAccion = 0;
	    	if(json.success == true){
	    		ventanaGridAjusteMedico.close();
	    		_amRecargar();
	    	}
	    	else
	    	{
	    		mensajeError(json.mensaje);
	    	}
		}
	    ,failure : function()
	    {
	    	errorComunicacion();
	    }
	});
}

function _amAgregar(ptimport,comments)
{
	var json =
	{
		'params.cdunieco' : _amParams.cdunieco,
		'params.cdramo'   : _amParams.cdramo,
		'params.estado'   : _amParams.estado,
		'params.nmpoliza' : _amParams.nmpoliza,
		'params.nmsuplem' : _amParams.nmsuplem,
		'params.nmsituac' : _amParams.nmsituac,
		'params.aaapertu' : _amParams.aaapertu,
		'params.status'   : _amParams.status,
		'params.nmsinies' : _amParams.nmsinies,
		'params.nfactura' : _amParams.nfactura,
		'params.cdgarant' : _amParams.cdgarant,
		'params.cdconval' : _amParams.cdconval,
		'params.cdconcep' : _amParams.cdconcep,
		'params.idconcep' : _amParams.idconcep,
		'params.nmordina' : _amParams.nmordina,
		'params.ptimport' : ptimport,
		'params.comments' : comments
	};
	debug('datos que se envian al servidor: ',json);
	ventanaGridAjusteMedico.setLoading(true);
	Ext.Ajax.request(
	{
		url     : _amUrlAgregar
		,params : json
	    ,success : function(response)
	    {
	    	ventanaGridAjusteMedico.setLoading(false);
	    	json = Ext.decode(response.responseText);
	    	tipoAccion = 0;
	    	if(json.success == true)
	    	{
	    		ventanaGridAjusteMedico.close();
	    		_amRecargar();
	    		
	    	}
	    	else
	    	{
	    		mensajeError(json.mensaje);
	    	}
	    }
	    ,failure : function()
	    {
	    	ventanaGridAjusteMedico.setLoading(false);
	    	errorComunicacion();
	    }
	});
}

Ext.onReady(function() {

    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });

    var panelAjusteMedico= Ext.create('Ext.form.Panel',{
        border  : 0
        ,bodyStyle:'padding:5px;'
        ,items :
        [   
			{   
				xtype:'numberfield',
				name:'idConsecutivo',
				fieldLabel : 'Consecutivo',
				minValue: 0,
			    labelWidth: 170,
			    hidden:true
			},
	         {   
	            	xtype:'numberfield',
			        name:'idAjusteImporte',
			        fieldLabel : 'Importe',
			    	allowBlank:false,
			    	allowDecimals :true,
			    	decimalSeparator :'.',
			    	minValue: 0,
	                labelWidth: 170
	    	},
	         {
		    	 xtype      : 'textfield',
		    	 fieldLabel : 'Observaciones',
		    	 labelWidth: 170,
		    	 allowBlank:false,
		    	 width: 500,
		    	 name : 'idObservaciones'
	         }
        ]
    });
    /*PANTALLA EMERGENTE PARA LA INSERCION Y MODIFICACION DE LOS DATOS DEL GRID*/
    ventanaGridAjusteMedico= Ext.create('Ext.window.Window', {
         renderTo: document.body,
           title: 'Ajuste M&eacute;dico',
           height: 150,
           width: 600,
           closeAction: 'hide',
           items:[panelAjusteMedico],
           
           buttons:[{
                  text: 'Aceptar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                  handler: function() {
                        if (panelAjusteMedico.form.isValid()) {
                        	// realizamos el guardado del registro
                        	var datos=panelAjusteMedico.form.getValues();
                        	if(tipoAccion == 0) // insertar
                        	{
                        		_amAgregar(datos.idAjusteImporte,datos.idObservaciones);
                        	}else{
                        		_amModificar(datos.idConsecutivo,datos.idAjusteImporte,datos.idObservaciones);
                        	}
                        	
                        } else {
                            centrarVentanaInterna(Ext.Msg.show({
                                   title: 'Aviso',
                                   msg: 'Complete la informaci&oacute;n requerida',
                                   buttons: Ext.Msg.OK,
                                   icon: Ext.Msg.WARNING
                               }));
                        }
                    }
              },
            {
                  text: 'Cancelar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                  handler: function() {
                       //Ext.getCmp('btnGuardaRegistro').disable();
                      panelAjusteMedico.getForm().reset();
                      ventanaGridAjusteMedico.close();
                  }
            }
           ]
           });
    
    /*////////////////////////////////////////////////////////////////
    ////////////////   DECLARACION DE EDITOR DE INCISOS  ////////////
    ///////////////////////////////////////////////////////////////*/
    Ext.define('EditorIncisos', {
 		extend: 'Ext.grid.Panel',
	 	requires: [
		 	'Ext.selection.CellModel',
		 	'Ext.grid.*',
		 	'Ext.data.*',
		 	'Ext.util.*',
		 	'Ext.form.*'
	 	],
 		xtype: 'cell-editing',
		id:'editorIncisos',
 		//title: 'Facturas en Tr&aacute;mite',
 		frame: false,

	 	initComponent: function(){
	 		this.cellEditing = new Ext.grid.plugin.CellEditing({
	 		clicksToEdit: 1
	 		});

	 			Ext.apply(this, {
	 			width: 750,
	 			height: 200,
	 			plugins: [this.cellEditing],
	 			store: storeIncisos,
	 			columns: 
	 			[

					{
						header: 'Consecutivo',			dataIndex: 'NMORDMOV',			width: 100
					},
					{
				 		header: 'Ajuste de importe',	dataIndex: 'PTIMPORT',			width: 200		,renderer: Ext.util.Format.usMoney
				 	},
				 	{
					 	header: 'Observaciones',		dataIndex: 'COMMENTS',	 	flex:2	
				 	},
				 	{
					 	xtype: 'actioncolumn',
					 	width: 30,
					 	sortable: false,
					 	menuDisabled: true,
					 	items: [{
	 						icon : _CONTEXT+'/resources/fam3icons/icons/pencil.png',
	 						tooltip : 'Editar movimiento',
	 						scope : this,
	 						handler : this.onEditClick
	 					}]
				 	},
				 	{
					 	xtype: 'actioncolumn',
					 	width: 30,
					 	sortable: false,
					 	menuDisabled: true,
					 	items: [{
					 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
					 		tooltip: 'Quitar ajuste m&eacute;dico',
					 		scope: this,
					 		handler: this.onRemoveClick
				 		}]
				 	}
		 		],
		 		selModel: {
			 		selType: 'cellmodel'
			 	},
		 		tbar: [{
				 	icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
				 	text: 'Agregar ajuste',
				 	scope: this,
				 	handler: this.onAddClick
		 		}]
		 	});
 			this.callParent();
	 	},
	 	getColumnIndexes: function () {
		 	var me, columnIndexes;
		 	me = this;
		 	columnIndexes = [];
		 	Ext.Array.each(me.columns, function (column)
		 	{
		 		if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
		 			columnIndexes.push(column.dataIndex);
			 	} else {
			 		columnIndexes.push(undefined);
			 	}
		 	});
		 	return columnIndexes;
	 	},
	 	validateRow: function (columnIndexes,record, y)
	 	//hace que una celda de columna con allowblank=false tenga el estilo rojito
	 	{
		 	var view = this.getView();
		 	Ext.each(columnIndexes, function (columnIndex, x)
		 	{
		 		if(columnIndex)
			 	{
			 		var cell=view.getCellByPosition({row: y, column: x});
			 		cellValue=record.get(columnIndex);
				 	if(cell.addCls&&((!cellValue)||(cellValue.lenght==0))){
				 		cell.addCls("custom-x-form-invalid-field");
				 	}
			 	}
		 	});
		 	return false;
	 	},
	 	onEditClick: function(grid, rowIndex){
	 		panelAjusteMedico.getForm().reset();
	 		var record=this.getStore().getAt(rowIndex);
	 		tipoAccion = 1;
	 		panelAjusteMedico.getForm().reset();
	 		panelAjusteMedico.down('[name="idConsecutivo"]').setValue(record.get('NMORDMOV'));
	 		panelAjusteMedico.down('[name="idAjusteImporte"]').setValue(record.get('PTIMPORT'));
	 		panelAjusteMedico.down('[name="idObservaciones"]').setValue(record.get('COMMENTS'));
	 		//ventanaGridAjusteMedico.showAt(150,600);
	 		centrarVentanaInterna(ventanaGridAjusteMedico.show());
	 	},
	 	onAddClick: function(){
	 		tipoAccion= 0;
	 		panelAjusteMedico.getForm().reset();
	 		//ventanaGridAjusteMedico.showAt(150,600);
	 		centrarVentanaInterna(ventanaGridAjusteMedico.show());
	 	},
	 	onRemoveClick: function(grid, rowIndex){
	 		var record=this.getStore().getAt(rowIndex);
	 		this.getStore().removeAt(rowIndex);
	 		_amEliminar(record.data.NMORDMOV,record.data.PTIMPORT,record.data.COMMENTS);
	 	}
 	});
    gridIncisos=new EditorIncisos();
    mesConStoreUniAdmin=[];
    
    Ext.define('EditorInforGral', {
 		extend: 'Ext.grid.Panel',
	 	requires: [
		 	'Ext.selection.CellModel',
		 	'Ext.grid.*',
		 	'Ext.data.*',
		 	'Ext.util.*',
		 	'Ext.form.*'
	 	],
 		xtype: 'cell-editing',
		id:'editorInforGral',
 		title: 'Informaci&oacute;n General',
 		frame: false,

	 	initComponent: function(){
	 		this.cellEditing = new Ext.grid.plugin.CellEditing({
	 		clicksToEdit: 1
	 		});

	 			Ext.apply(this, {
	 			width: 750,
	 			height: 100,
	 			plugins: [this.cellEditing],
	 			store: storeModelInforGral,
	 			columns: 
	 			[
				 	{   header: 'Precio',			    dataIndex: 'txtPrecio',			    flex:2		 ,renderer: Ext.util.Format.usMoney	},
				 	{   header: 'Cantidad',			    dataIndex: 'txtCantidad',		    flex:2       },
				 	{   header: 'Descuento %',		    dataIndex: 'txtDescPor',	 		flex:2	     },
				 	{ 	header: 'Descuento $',		    dataIndex: 'txtDescNum',			flex:2  	 ,renderer: Ext.util.Format.usMoney	},
				 	{   header: 'Ajuste M&eacute;dico', dataIndex: 'txtAjusteMed',			flex:2       ,renderer: Ext.util.Format.usMoney	},
				 	{ 	header: 'Subtotal Factura', 	dataIndex: 'txtSubTotalFac',		flex:2       ,renderer: Ext.util.Format.usMoney	},
				 	{   header: 'Subtotal Ajustado',	dataIndex: 'txtSbubTotalAjuste',	flex:2   	 ,renderer: Ext.util.Format.usMoney	}
		 		]
		 	});
 			this.callParent();
	 	}
 	});
    gridInforGral = new EditorInforGral();
    
    Ext.create('Ext.form.Panel',
    	    {
    	        border    : 0
    	        ,renderTo : 'div_clau2'
	        	,width: 800
	            ,defaults 	:
	    		{
	    			style : 'margin:5px;'
	    		}
    	        ,items    :
    	        [
            		{
			        	xtype: 'combo',
			            name:'params.idconcep',
			            id: 'idConceptos',
			            labelWidth: 150,
			            valueField: 'key',
			            displayField: 'value',
			            fieldLabel: 'Tipo de Concepto',
			            store: storeTipoConcepto,
			            queryMode:'local',
			            allowBlank:false,
			            editable:false,
			            readOnly   : true
			        },
			        {
			        	xtype: 'combo',
			            name:'params.cdconcep',
			            id:'conceptosID',
			            labelWidth: 150,
			            valueField: 'key',
			            displayField: 'value',
			            fieldLabel: 'Concepto',
			            store: storeConceptosCatalogo,
			            queryMode:'local',
			            allowBlank:false,
			            editable:true,
			            forceSelection: true,
			            readOnly   : true,
			            width:550
			        },
			        gridInforGral,
			        gridIncisos,
                    {
            			id:'idTotalAjustado'
        		        ,xtype      : 'displayfield'
        		    	,fieldLabel : 'Total ajustado'
        	    		,labelWidth: 170
        	    		,name       : 'idTotalAjustado',
        		    	allowDecimals :true,
        		    	decimalSeparator :'.',
        		    	renderer: Ext.util.Format.usMoney//,
        		    	//readOnly   : true
        	        }
    	        ]
    	    });
    _amRecargar();
    
});