<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Exclusiones de p&oacute;liza</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdUnieco =  '<s:property value="params.cdunieco" />';
            var _cdRamo   =  '<s:property value="params.cdramo" />';
            var _estado   =  '<s:property value="params.estado" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _nmsituac =  '<s:property value="params.nmsituac" />';
            
            var _URL_CONSULTA_CLAUSULAS_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaClausulasPoliza" />';
		            
			Ext.onReady(function() {
		
				Ext.define('ClausulaModel', {
				    extend:'Ext.data.Model',
				    fields:['cdclausu','dsclausu','linea_usuario','cdtipcla','linea_general']
				});
				
				var storeExclusionesPoliza = new Ext.data.Store({
			        model      : 'ClausulaModel',
			        //autoLoad: true,
			        proxy     : {
			        	type        : 'ajax',
			            url         : _URL_CONSULTA_CLAUSULAS_POLIZA,
			            extraParams : {
			                'params.cdunieco' : _cdUnieco,
			                'params.cdramo'   : _cdRamo,
			                'params.estado'   : _estado,
			                'params.nmpoliza' : _nmpoliza,
			                'params.nmsituac' : _nmsituac
			            },
			            reader : {
			                type : 'json',
			                root : 'clausulasPoliza'
			            }
			        }
			    });
				
				// Cargar store
				storeExclusionesPoliza.load({
                    callback: function(records, operation, success) {
                    	if (!success) {
                            showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                            return;
                        }
                        if(records.length == 0){
                            showMessage('No hay exclusiones', 'Este asegurado no tiene exclusiones', Ext.Msg.OK, Ext.Msg.INFO);
                            return;
                        }
                    }
                });
				
				Ext.create('Ext.panel.Panel', {
					name      : 'pnlExclusiones',
					renderTo  : 'maindivExcs',
					//layout:'fit',
					defaults  : {
						style : 'margin:5px'
					},
	                items: [{
	                	xtype       : 'grid',
					    store       : storeExclusionesPoliza,
					    buttonAlign : 'center',
					    columns     : [{
					        header    : 'Nombre',
					        dataIndex : 'dsclausu',
					        flex      : 1
					    },{
	                        //menuDisabled: true,
	                        xtype : 'actioncolumn',
	                        width : 30,
	                        items : [{
                                icon    : _CONTEXT+'/resources/fam3icons/icons/application_view_detail.png',
                                tooltip : 'Ver detalle',
                                handler : function(gridview, rowIndex, colIndex) {
	                                var rec = gridview.getStore().getAt(rowIndex);
	                                this.up('panel[name=pnlExclusiones]').down('[name=detalleExclusion]').setValue(rec.get('linea_usuario'));
                                }
	                        }]
					    }]
					},{
                        xtype: 'label',
                        text: 'Detalles:'
                    },{
						xtype : 'textarea',
						name  : 'detalleExclusion',
                        height: 300,
                        width : 425
					}]
			    });
		            	
		    });
		</script>

    </head>
    <body>
        <div id="maindivExcs"></div>
    </body>
</html>