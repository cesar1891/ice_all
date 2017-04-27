<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>

Ext.onReady(function(){
    
    // Conversión para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
	
    var _URL_CONSULTA_DATOS_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaDatosPoliza" />';
	var _cdUnieco                  = '<s:property value="params.cdunieco" />';
	var _cdRamo                    = '<s:property value="params.cdramo" />';
	var _estado                    = '<s:property value="params.estado" />';
	var _nmpoliza                  = '<s:property value="params.nmpoliza" />';
	
	 // Modelo
    Ext.define('DatosPolizaModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'nmsolici'},
            {type:'string', name:'titular'},
            {type:'string', name:'cdrfc'},
            {type:'date',   name:'feemisio', dateFormat: 'd/m/Y'},
            {type:'date',   name:'feefecto', dateFormat: 'd/m/Y'},
            {type:'date',   name:'feproren', dateFormat: 'd/m/Y'},
            {type:'string', name:'dstarifi'},
            {type:'string', name:'dsmoneda'},
            {type:'string', name:'dscuadro'},
            {type:'string', name:'dsperpag'},
            {type:'string', name:'dstempot'},
            {type:'string', name:'nmpoliex'},
            {type:'string', name:'cdagente'},
            {type:'string', name:'statuspoliza'},
            {type:'string', name:'nmpolant'},
            {type:'string', name:'dsunieco'},
            {type:'string', name:'dsramo'},
            {type:'string', name:'dsplan'},
            {type:'string', name:'dstipsit'}
        ]
    });

    // Store
    var storeDatosPoliza = new Ext.data.Store({
        model: 'DatosPolizaModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosPoliza'
            }
        }
    });
    
	
	// FORMULARIO DATOS DE LA POLIZA
    var panelDatosPoliza = Ext.create('Ext.form.Panel', {
        model : 'DatosPolizaModel',
        width : 820,
        border : false,
        //height : 280,
        renderTo: 'divDatGen',
        defaults : {
            bodyPadding : 10,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'dsunieco', fieldLabel: 'Sucursal',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dsramo',   fieldLabel: 'Ramo',      readOnly: true, labelWidth: 65,  width: 290, labelAlign: 'right'},
                {xtype: 'textfield', name: 'nmpoliex', fieldLabel: 'P&oacute;liza', readOnly: true, labelWidth: 50,  width: 210, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'dsplan',   fieldLabel: 'Plan',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dstipsit', fieldLabel: 'Subramo', readOnly: true, labelWidth: 65,  width: 290, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'statuspoliza', fieldLabel: 'Estatus',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', id: 'nmpolant',  name: 'nmpolant',     fieldLabel: 'P&oacute;liza anterior', readOnly: true, labelWidth: 100, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', id: 'nmsolici',  name: 'nmsolici',     fieldLabel: 'No. de solicitud',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        },
        {
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'titular', fieldLabel: 'Contratante', readOnly: true, labelWidth: 120, width: 590}, 
                {xtype:'textfield', name:'cdrfc',   fieldLabel: 'RFC',         readOnly: true, labelWidth: 80,  width: 210, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'datefield', name: 'feemisio', fieldLabel: 'Fecha emisi&oacute;n',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype: 'datefield', name: 'feefecto', fieldLabel: 'Fecha inicio vigencia',         format: 'd/m/Y', readOnly: true, labelWidth: 200, width: 290, labelAlign: 'right'}, 
                {xtype: 'datefield', name: 'feproren', fieldLabel: 'Fecha fin vigencia', format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 210, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'dstarifi', fieldLabel: 'Tipo de tarificaci&oacute;n', readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dscuadro', fieldLabel: 'Cuadro de comisiones',        readOnly: true, labelWidth: 150, width: 290, labelAlign: 'right'},
                {xtype: 'textfield', name: 'dsmoneda', fieldLabel: 'Moneda',                      readOnly: true, labelWidth: 120, width: 210, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'dsperpag', fieldLabel: 'Forma de Pago',  readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dstempot', fieldLabel: 'Tipo de P&oacute;liza', readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        }
        
        
        /*{
        	layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'nmpoliex', fieldLabel: 'N&uacute;mero de P&oacute;liza', readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', id: 'nmsolici',  name: 'nmsolici', fieldLabel: 'N&uacute;mero de solicitud',     readOnly: true, labelWidth: 120, width: 220, labelAlign: 'right'},
                {xtype: 'textfield', id: 'nmpolant',  name: 'nmpolant', fieldLabel: 'P&oacute;liza anterior',         readOnly: true, labelWidth: 120, width: 220, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'titular', fieldLabel: 'Nombre del contratante', readOnly: true, labelWidth: 120, width: 520}, 
                {xtype:'textfield', name:'cdrfc',   fieldLabel: 'RFC',                    readOnly: true, labelWidth: 80,  width: 220, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'datefield', name: 'feemisio', fieldLabel: 'Fecha emisi&oacute;n',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype: 'datefield', name: 'feefecto', fieldLabel: 'Fecha de efecto',         format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 220, labelAlign: 'right'}, 
                {xtype: 'datefield', name: 'feproren', fieldLabel: 'Fecha renovaci&oacute;n', format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 220, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : {xtype: 'textfield', name: 'dstarifi', fieldLabel: 'Tipo de tarificaci&oacute;n', readOnly: true, labelWidth: 120}
        }, {
            layout : 'hbox',
            items : {xtype: 'textfield', name: 'dsmoneda', fieldLabel: 'C&oacute;digo de moneda', readOnly: true, labelWidth: 120}
        }, {
            layout : 'hbox',
            items : {xtype: 'textfield', name: 'nmcuadro', fieldLabel: 'Cuadro de comisiones', readOnly: true, labelWidth: 120}
        }, {
            layout : 'hbox',
            items : {xtype: 'textfield', name: 'dsperpag', fieldLabel: 'Periodicidad de pago', readOnly: true, labelWidth: 120}
        }, {
            layout : 'hbox',
            items : {xtype: 'textfield', name: 'dstempot', fieldLabel: 'Tipo de P&oacute;liza', readOnly: true, labelWidth: 120}
        }, {
            layout : 'hbox',
            items : {xtype: 'textfield', name: 'statuspoliza', fieldLabel: 'ESTATUS', readOnly: true, labelWidth: 120}
        }*/  
        ]
    });
	
	
 	// Consultar Datos Generales de la Poliza:
    storeDatosPoliza.load({
        params : {
        	'params.cdunieco': _cdUnieco,
        	'params.cdramo': _cdRamo,
        	'params.estado': _estado,
        	'params.nmpoliza': _nmpoliza
        },
        callback : function(records, operation, success) {
            if (success) {
                if (records.length > 0) {
                    // Se llenan los datos generales de la poliza elegida
                    panelDatosPoliza.getForm().loadRecord(records[0]);
                } else {
                    showMessage('Aviso', 
                        'No existen datos generales de la p\u00F3liza elegidaLa P&oacute;iza no existe, verifique sus datos', 
                        Ext.Msg.OK, Ext.Msg.ERROR);
                }
            } else {
                showMessage('Error', 
                    'Error al obtener los datos generales de la p\u00F3liza elegida, intente m\u00E1s tarde',
                    Ext.Msg.OK, Ext.Msg.ERROR);
            }

        }
    });
	
});
</script>
<div id="divDatGen" style="height:500px;"></div>