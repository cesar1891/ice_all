<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _CONTEXT = '${ctx}';

var _cdAgente = '<s:property value="params.cdagente" />';

var _URL_LoadProductosAgente   = '<s:url namespace="/catalogos" action="obtieneProductosAgente" />';
var _URL_GuardaProductosAgente = '<s:url namespace="/catalogos" action="guardaProductosAgente" />';

Ext.onReady(function() {

	Ext.define('modeloProductos',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'CDRAMO'},
                 {type:'string',    name:'DSRAMO'},
                 {type:'boolean',   name:'TIENE_CDRAMO'}
				]
    });
    
    var storeProductos = Ext.create('Ext.data.JsonStore', {
    	model:'modeloProductos',
        proxy: {
            type: 'ajax',
            url: _URL_LoadProductosAgente,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    
    storeProductos.load({
    	params: {
    		'params.PV_CDAGENTE_I' : _cdAgente
    	}
    });
    
    var gridProductos = Ext.create('Ext.grid.Panel',{
		//title: 'Productos del Agente',
		renderTo : 'maindivProductosAgente',
		autoScroll: true,
		store:  storeProductos,
		//collapsible: true,
		titleCollapse: true,
		style: 'margin:0px',
		height: 300,
		columns       :[ { dataIndex  : 'CDRAMO', hidden: true},
		                 { header     : 'Seleccione' ,dataIndex  : 'TIENE_CDRAMO', xtype: 'checkcolumn', menuDisabled : true, listeners: {
		                	 beforecheckchange: function (chkcol, rowIndex, checked, eOpts){
		                		 if(!checked){
		                			 recordChk = storeProductos.getAt(rowIndex);
		                			 if(!recordChk.dirty){
		                				 return false;	 
		                			 }
		                		 }
		                	 }
		                 } },
		                 { header     : 'Productos' ,dataIndex : 'DSRAMO', flex: 1 }
		 			   ],
		buttonAlign: 'center', 
		buttons:[{
				text: 'Actualizar Productos',
				icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
				handler: function() {
						
					var saveList = [];
					storeProductos.getUpdatedRecords().forEach(function(record,index,arr){
						//para convertir boolean a string ya que el server no lo convierte bien
						record.data['TIENE_CDRAMO'] = record.get('TIENE_CDRAMO')==true?'I':'';
						saveList.push(record.data);
					});
					
					if(saveList.length == 0){
						mensajeWarning('No se agregaron Productos.');
						return;
					}
					
					gridProductos.setLoading(true);
					Ext.Ajax.request({
						url: _URL_GuardaProductosAgente,
						jsonData: {
							params: {
								'PV_CDAGENTE_I' : _cdAgente
					    	},
							saveList : 	saveList
						},
						success: function(response) {
							var res = Ext.decode(response.responseText);
							gridProductos.setLoading(false);
							
							if(res.success){
								storeProductos.reload();
								mensajeCorrecto('Aviso','Se ha guardado con exito.');
								windowLoader.close();
							}else{
								mensajeError('No se pudo guardar.');
							}
							
						},
						failure: function(){
							gridProductos.setLoading(false);
							mensajeError('No se pudo guardar.');
						}
					});
				}
		},{
			text: 'Cancelar',
			icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png',
			handler: function() {
				windowLoader.close();
			}
		}]
	});
	
});


</script>

<div id="maindivProductosAgente" style="height:300px;"></div>