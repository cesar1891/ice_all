Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
var datosgrid;
var storeIncisos;
Ext.define('modelClau', {
	extend:'Ext.data.Model',
	fields:['noFactura','fechaFactura','tipoServicio','proveedor','importe']
});

storeIncisos=new Ext.data.Store( {
	autoDestroy: true,
	model: 'modelClau'
});

Ext.onReady(function() {
	Ext.selection.CheckboxModel.override( {
		mode: 'SINGLE', 
		allowDeselect: true
	});
	///// NUEVOS
	Ext.define('DatosFacturaxTramite',{
		extend: 'Ext.data.Model',
			fields: [ 
				{type:'string',    name:'reclamacion'		},				{type:'string',    name:'ntramite'			},
				{type:'string',    name:'factura'			},				{type:'date',      name:'fechaFactura',	dateFormat : 'd/m/Y'},
				{type:'string',    name:'cdtipser'			},				{type:'string',    name:'descServicio'		},
				{type:'string',    name:'cdpresta'			},				{type:'string',    name:'nomProveedor'		},
				{type:'string',    name:'ptimport'			},				{type:'string',    name:'cdgarant'			},
				{type:'string',    name:'descCdgarant'		},				{type:'string',    name:'desctoPorc'		},
				{type:'string',    name:'desctoNum'			},				{type:'string',    name:'cdconval'			},
				{type:'string',    name:'dssubgar'			},				{type:'string',    name:'cdmoneda'			},
				{type:'string',    name:'descMoneda'		},				{type:'string',    name:'tasaCambio'		},
				{type:'string',    name:'ptimporta'			},				{type:'string',    name:'dctoNuex'			},
				{type:'string',    name:'feegreso'          },				{type:'string',    name:'diasdedu'			},
				{type:'string',    name:'contraRecibo'      },				{type:'string',    name:'totalpagar'		},
				{type:'string',    name:'swisr'      		},				{type:'string',    name:'swice'				}
			]
	});

	var storeDatosFacturaxTramite = Ext.create('Ext.data.Store', {
		storeId: 'storeDatosFacturaxTramite',
		model: 'DatosFacturaxTramite',
		data : recordsStoreFactura
	});

	var gridDatosFacturaxTramite = Ext.create('Ext.grid.Panel', {
		width   : 980,
		height	: 300,
		title   : 'Facturas',
		store   : storeDatosFacturaxTramite,
		autoScroll:true,
		id      : 'gridDatosFacturaxTramite',
		features:[{
			ftype:'summary'
		}],
		columns: _11_columnas_Factura
		,tbar: [{
			text     : 'Agregar Factura'
			,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
			, hidden : (_11_params.CDTIPTRA == _TIPO_PAGO_AUTOMATICO)
			,handler : _p11_agregarFacturas
		}]
	});

	gridDatosFacturaxTramite.store.sort([{ 
		property    : 'factura',
		direction   : 'ASC'
	}]);

	_11_itemsForm.push({
		colspan:2,
		border    : 0,
		items     :	[
			gridDatosFacturaxTramite
		]
	});

	_11_form=Ext.create('Ext.form.Panel',{
		border    	: 0
		,title	  	: 'Afiliados Afectados'
		,renderTo 	: 'div_clau'
		,bodyPadding: 5
		,width		: 1000
		,layout     : {
			type     : 'table' 
			,columns : 2
		}
		,defaults   : {
			style   : 'margin:5px;'
		}
		,listeners 	: {
			afterrender : function(form){
				heredarPanel(form);
			}
		}
		,items    	: _11_itemsForm
		,buttonAlign:'center'
		,buttons	:[
			{
				text     : 'Regresar'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/book_previous.png'
				,handler : _11_regresarMC
			},
			{
				text     : 'Revisi&oacute;n de documentos'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/folder_table.png'
				,handler : _11_revDocumentosWindow
				,hidden  : _11_params.CDTIPTRA == _TIPO_PAGO_AUTOMATICO
				,hidden  : true
			},
			{
				text     : 'Rechazar Tr&aacute;mite'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png'
				,handler : _11_rechazarTramiteSiniestro
			},
			{
				text     : 'Turnar &Aacute;rea M&eacute;dica'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/user_go.png'
				,handler : _11_turnarAreaMedica
				,hidden  : _CDROL == _ROL_MEDICO || _CDROL == _ROL_COORD_MEDICO || _CDROL == _ROL_MESASINIESTRO || _11_params.CDTIPTRA == _TIPO_PAGO_AUTOMATICO
			},
			{
				text     : 'Solicitar Pago'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/money_dollar.png'
				,handler : _11_solicitarPago
				,hidden:   _CDROL == _ROL_MEDICO || _CDROL == _ROL_COORD_MEDICO  || _CDROL == _ROL_MESASINIESTRO
			},
			{
				text     : 'Turnar Operador Reclamaci&oacute;n'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/user_go.png'
				,handler : _11_retornarMedAjustadorAOperador
				,hidden  : _CDROL ==  _OPERADOR_REC ||  _CDROL == _ROL_MESASINIESTRO
			},
			{
				text     : 'Historial'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/clock.png'
				,handler : _11_historialTramite
			},
			{
				text     : 'Devolver'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/note_go.png'
				,handler : _11_turnarDevolucionTramite
				,hidden  : _CDROL == _ROL_MEDICO || _CDROL == _ROL_COORD_MEDICO || _11_params.CDTIPTRA == _TIPO_PAGO_AUTOMATICO
			},
			{
				text     : 'Turnar Operador Reclamaci&oacute;n'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/user_go.png'
				,handler : _11_turnarAreclamaciones
				,hidden  : _CDROL != _ROL_MESASINIESTRO
			},
			{
				text     : 'Generar Contrarecibo'
				,icon    : _CONTEXT+'/resources/fam3icons/icons/accept.png'
				,handler : _11_GenerarContrarecibo
				,hidden  : _CDROL != _ROL_MESASINIESTRO
			}
		]
	});
});