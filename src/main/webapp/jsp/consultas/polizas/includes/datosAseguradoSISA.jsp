<%@ include file="/taglibs.jsp"%>
<%@ page language="java" %>
<script>

////// variables //////
//Se recibe el parametro como propiedad.
var cdperson = '<s:property value="params.cdperson" />';
console.log('cdperson',cdperson);
var _URL_CONSULTA_DATOS_ASEGURADO_DETALLE = '<s:url namespace="/consultasPoliza" action="consultaAseguradoDetalle" />';

////// variables //////

Ext.onReady(function()
{
	// Modelo
    Ext.define('DatosAseguradoModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdperson'},
            {type:'string', name:'edad'},
            {type:'string', name:'identidad'},            
            {type:'string', name:'nombre'},
            {type:'string', name:'tiposangre'},
            {type:'string', name:'antecedentes'},
            {type:'string', name:'oii'},
            {type:'string', name:'telefono'},
            {type:'string', name:'direccion'},
            {type:'string', name:'correo'},
            {type:'string', name:'mcp'},
            {type:'string', name:'mcpespecialidad'},
            {type:'string', name:'ocp'},
            {type:'string', name:'ocpespecialidad'}
        ]
    });
	
    // Store
    var storeDatosAsegurado = new Ext.data.Store({
        model: 'DatosAseguradoModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_ASEGURADO_DETALLE,
            reader: {
                type: 'json',
                root: 'datosAseguradoDetalle'
            }
        }
    });
	
	//FORMULARIO DATOS DE LA POLIZA
	var panelDatosPoliza = Ext.create('Ext.form.Panel', {
	    model : 'DatosAseguradoModel',
	    width : 815 ,
	    border : false,
	    //height : 280,
	    defaults : {
	        bodyPadding : 5,
	        border : false
	    },
	    renderTo : 'divDetalleAsegurado',
	    items : [ {
	        layout : 'hbox',
	        items : [
	            {xtype: 'textfield', name: 'cdperson', fieldLabel: 'C&oacute;digo',      readOnly: true, labelWidth: 120, width: 300},
	            {xtype: 'textfield', name: 'edad',   fieldLabel: 'Edad',      readOnly: true, labelWidth: 65,  width: 290, labelAlign: 'right'},
	            {xtype: 'textfield', name: 'identidad', fieldLabel: 'Identidad', readOnly: true, labelWidth: 50,  width: 210, labelAlign: 'right'}
	        ]
	    },{
	        layout : 'hbox',
	        items : [
	            {xtype: 'textfield', name: 'nombre',   fieldLabel: 'Nombre',      readOnly: true, labelWidth: 120, width: 590}	            
	        ]
	    },{
	        layout : 'hbox',
	        items : [
	            {xtype: 'textfield',   name: 'tiposangre', fieldLabel: 'Tipo de sangre',                readOnly: true, labelWidth: 120, width: 300},
	            {xtype: 'textfield',   name: 'antecedentes',     fieldLabel: 'Antecedentes', readOnly: true, labelWidth: 100, width: 400, labelAlign: 'right'}	            
	        ]
	    }, {
	        layout : 'hbox',
	        items : [ 
	            {xtype:'textfield', name:'oii', fieldLabel: 'OII', readOnly: true, labelWidth: 120, width: 300}, 
	            {xtype:'textfield', name:'telefono',   fieldLabel: 'Tel&eacute;fono',         readOnly: true, labelWidth: 80,  width: 300, labelAlign: 'right'}
	        ]
	    }, {
	        layout : 'hbox',
	        items : [ 
	            {xtype: 'datefield', name: 'direccion', fieldLabel: 'Direcci&oacute;n',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 590}
	        ]
	    }, {
	        layout : 'hbox',
	        items : [ 
	            {xtype: 'textfield', name: 'correo', fieldLabel: 'e-mail', readOnly: true, labelWidth: 120, width: 590}
	        ]
	    }, {
	        layout : 'hbox',
	        items : [ 
	            {xtype: 'textfield', name: 'mcp', fieldLabel: 'MCP',  readOnly: true, labelWidth: 120, width: 400},
	            {xtype: 'textfield', name: 'mcpespecialidad', fieldLabel: 'Especialidad MCP', readOnly: true, labelWidth: 120, width: 400, labelAlign: 'right'}
	        ]
	    },
	    {
	        layout : 'hbox',
	        items : [ 
	            {xtype: 'textfield', name: 'ocp', fieldLabel: 'OCP',  readOnly: true, labelWidth: 120, width: 400},
	            {xtype: 'textfield', name: 'ocpespecialidad', fieldLabel: 'Especialidad OCP', readOnly: true, labelWidth: 120, width: 400, labelAlign: 'right'}
	        ]
	    }
	    ]
	});
		
	//Datos para asegurado detalle
    storeDatosAsegurado.load({
        params: {
            'params.cdperson' : cdperson            
        },
        callback: function(records, operation, success){
        	console.log('records=',records);
            if(success){
            	panelDatosPoliza.getForm().loadRecord(records[0]);
            } else {
            	showMessage('Error', 'Error al obtener los datos del asegurado', Ext.Msg.OK, Ext.Msg.ERROR);
            }
        }
    });

}); //Fin de onReady

////// funciones //////
////// funciones //////
</script>
<div id="divDetalleAsegurado" style="height:350px"></div>