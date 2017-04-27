<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
/**
 * pRcb_ : Pantalla de Consulta de recibos y notas de crédito
 */

Ext.onReady(function(){
	
	// Sobreescribimos el selection model para CheckboxModel
	Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });
    
    // Conversión para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
	
	var _URL_CONSULTA_RECIBOS        = '<s:url namespace="/general" action="includes/obtieneRecibos" />';
    var _URL_CONSULTA_DETALLE_RECIBO = '<s:url namespace="/general" action="obtieneDetalleRecibo" />';
	var pRcb_cdunieco                = '<s:property value="params.cdunieco" />';
	var pRcb_cdramo                  = '<s:property value="params.cdramo" />';
	var pRcb_estado                  = '<s:property value="params.estado" />';
	var pRcb_nmpoliza                = '<s:property value="params.nmpoliza" />';
	var pRcb_nmsuplem                = '<s:property value="params.nmsuplem" />';
	
    
    // Model
    Ext.define('DetalleRecibo',{
        extend: 'Ext.data.Model',
        fields: [
            'cdtipcon',
            'dstipcon',
            'ptimport'
        ]
    });
    
    // Store
    var pRcb_storeDetalleRecibo = Ext.create('Ext.data.Store', {
        model: 'DetalleRecibo',
        proxy: {
            type: 'ajax',
            url:  _URL_CONSULTA_DETALLE_RECIBO,
            reader: {
                type: 'json',
                root: 'detallesRecibo'
            }
        }
    });
	
	
	// Model
	Ext.define('Recibo',{
		extend: 'Ext.data.Model',
		fields: [
			'cdestado',
			'dsestado',
			'dstipore',
			'feemisio',
			'fefinal',
			'feinicio',
			'nmrecibo',
			'ptimport',
			'tiporeci'
		]
	});
	
	// Store
	var pRcb_storeRecibos = Ext.create('Ext.data.Store', {
		model: 'Recibo',
		proxy: {
			type: 'ajax',
			url:  _URL_CONSULTA_RECIBOS,
			reader: {
				type: 'json',
				root: 'recibos'
			}
		}
	});
	
	pRcb_storeRecibos.load({
		params: {
			'params.cdunieco' : pRcb_cdunieco,
			'params.cdramo'   : pRcb_cdramo,
			'params.estado'   : pRcb_estado,
			'params.nmpoliza' : pRcb_nmpoliza,
			'params.nmsuplem' : pRcb_nmsuplem
		}
	});
	
	
	//Grid
	var pRcb_grdRecibos = Ext.create('Ext.grid.Panel', {
		store:   pRcb_storeRecibos,
		selType: 'checkboxmodel',
		tbar: [{
			xtype   : 'button',
			text    : 'Ver detalle',
			disabled: true,
			handler:  function(btn, e) {
				var reciboSelected = this.up('grid').getSelectionModel().getSelection()[0];
				pRcb_storeDetalleRecibo.load({
			        params: {
                        'params.cdunieco': pRcb_cdunieco,
                        'params.cdramo'  : pRcb_cdramo,
                        'params.estado'  : pRcb_estado,
                        'params.nmpoliza': pRcb_nmpoliza,
                        'params.nmrecibo': reciboSelected.get('nmrecibo')
                    },
                    callback: function(records, operation, success){
                        if(success){
                        	pRcb_wndDetalleRecibo.show();
                        }else{
                        	showMessage('Error', 'Error al obtener los datos, intente m\u00E1s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                        }                    
                    }
			    });
			}
		}],
		columns: [
			{text: '#', dataIndex:'nmrecibo', width:100},
			//{text: 'Estado', dataIndex:'cdestado', width:80},
			{text: 'Estado', dataIndex:'dsestado', width:120},
			//{text: 'Tipo recibo', dataIndex:'tiporeci', width:80},
			{text: 'Tipo de recibo', dataIndex:'dstipore', width:120},
			{text: 'Fecha emisi\u00F3n', dataIndex:'feemisio', width:100},
			{text: 'Fecha inicio', dataIndex:'feinicio', width:100},
			{text: 'Fecha final', dataIndex:'fefinal', width:100},
			{text: 'Importe total', dataIndex:'ptimport', width:100, renderer : 'usMoney', align:'right'}
		],
		listeners: {
			selectionchange: function(view, nodes) {
				//Deshabilitar/habilitar botones(acciones):
				var isDisabled = !this.getSelectionModel().hasSelection();
				Ext.each(this.getDockedItems('toolbar[dock="top"]>button'), function(item, index, allItems) {
					item.setDisabled(isDisabled);
				});
			}
		},
		renderTo: 'dvRecibos'
	});
	
	
	//Ventana de detalle del recibo:
	var pRcb_wndDetalleRecibo = Ext.create('Ext.window.Window', {
		title: 'Detalle de recibo',
		width: 430,
		//height: 200,
		modal: true,
		closeAction: 'hide',
		items: [{
			  xtype: 'grid',
			  store: pRcb_storeDetalleRecibo,
			  height:285,
			  autoScroll:true,
		      columns: [
	             {text: 'Tipo de concepto', dataIndex:'dstipcon', width:300},
	             {text: 'Importe', dataIndex:'ptimport', width:100, align:'right'}
	         ]
		}],
		buttons: [{
			text: 'Aceptar',
			handler: function(btn) {
				pRcb_wndDetalleRecibo.close();
			}
		}]
	});
	
});
</script>
<div id="dvRecibos" style="height:350px;"></div>