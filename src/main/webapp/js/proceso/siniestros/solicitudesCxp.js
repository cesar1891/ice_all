Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
Ext.onReady(function() {
	var panelProveedor;
	Ext.selection.CheckboxModel.override( {
		mode: 'SINGLE',
		allowDeselect: true
	});
	
	Ext.Ajax.timeout = 5*60*1000;
	
	//Declaracion de los modelos 
	
	Ext.define('modelListadoCXP',{
		extend: 'Ext.data.Model',
		fields: [	{type:'string',		name:'cvedpto'},			{type:'string',		name:'destinoComp'},	
		         	{type:'string',		name:'producto'},			{type:'string',		name:'numsol'},				{type:'String',		name:'fecha'},
					{type:'string',		name:'tip_pro'},			{type:'string',		name:'id_pro'},				{type:'string',		name:'destino'},
					{type:'string',		name:'nomdes'},				{type:'string',		name:'rfc'},				{type:'string',		name:'factura'},
					{type:'string',		name:'benef'},				{type:'string',		name:'inomovtos'},			{type:'string',		name:'mtotalap'}
		]
	});
	
	var storeListadoCXP = new Ext.data.Store({
		pageSize	: 10
		,model		: 'modelListadoCXP'
		,autoLoad	: false
		,proxy		: {
			enablePaging	: true,
			reader			: 'json',
			type			: 'memory',
			data			: []
		}
	});
	

	gridPolizasAsegurado= Ext.create('Ext.grid.Panel', {
		id			:	'polizaGridAltaTramite',
		store		:	storeListadoCXP,
		//selType		:	'checkboxmodel',
		width		:	900,
		height		:	400,
		columns		:
		[
			{	header		: 'Departamento',						dataIndex : 'cvedpto',			width		: 100		},
			{	header		: 'Producto',							dataIndex : 'producto',			width		: 150		},
			{	header		: 'Destino',							dataIndex : 'destinoComp',		width	    : 150		},
			{	header		: 'N&uacute;mero <br/> de Solicitud',	dataIndex : 'numsol',			width	 	: 90		},
			{	header		: 'Fecha <br/> de Solicitud',			dataIndex : 'fecha',			width	    : 100		},
			{	header		: 'Tipo',								dataIndex : 'tip_pro',			width	    : 100		},
			{	header		: 'Beneficiario',						dataIndex : 'benef',			width	    : 350		},
			{	header		: 'Factura',							dataIndex : 'factura',			width	    : 100		},
			{	header		: 'Importe',							dataIndex : 'mtotalap',			width	    : 100,
				renderer: Ext.util.Format.usMoney
			}
		],
		bbar : {
			displayInfo	: true,
			store		: storeListadoCXP,
			xtype		: 'pagingtoolbar'
		},
		listeners: {
			itemclick: function(dv, record, item, index, e){
				debug("Valor del Record", record);
			}
		}
	});
	/*gridPolizasAsegurado.store.sort([
		{
			property    : 'nmpoliza',			direction   : 'DESC'
		}
	]);*/

	var panelInicialPral= Ext.create('Ext.form.Panel',{
        border      : 0,
        id          : 'panelInicialPral',
        renderTo    : 'div_clau',
        bodyPadding : 5,
        defaults 	: {
			style   : 'margin:5px;'
		},
		items       :[
			
            {	border: false
		 		,items    :
		 			[
		 			 	gridPolizasAsegurado 
	 			 	]
		 	}
        ]
    });
	
	gridPolizasAsegurado.setLoading(true);
	
	var params = {
		'params.cdrfc'	:	null
	};
	
	cargaStorePaginadoLocal(storeListadoCXP, _URL_SOLICITUD_CXP, 'solicitudPago', params, function(options, success, response){
		if(success){
			gridPolizasAsegurado.setLoading(false);
			var jsonResponse = Ext.decode(response.responseText);
			if(jsonResponse.solicitudPago && jsonResponse.solicitudPago.length == 0) {
				centrarVentanaInterna(showMessage("Aviso", "No se encuentran Solicitudes de pagos Pendientes.", Ext.Msg.OK, Ext.Msg.INFO));
				return;
			}
		}else{
			gridPolizasAsegurado.setLoading(false);
			Ext.Msg.show({
				title: 'Aviso',
				msg: 'Error al obtener los datos.',
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR
			});
		}
	});
});