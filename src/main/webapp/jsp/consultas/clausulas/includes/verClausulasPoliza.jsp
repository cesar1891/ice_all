<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Cl&aacute;usulas de p&oacute;liza</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdUnieco =  '<s:property value="params.cdunieco" />';
            var _cdRamo   =  '<s:property value="params.cdramo" />';
            var _estado   =  '<s:property value="params.estado" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _nmsituac =  '<s:property value="params.nmsituac" />';
            var _icodpoliza =  '<s:property value="params.icodpoliza" />';
            var _cdperson =  '<s:property value="params.cdperson" />';
            
            var _URL_CONSULTA_CLAUSULAS_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaClausulasPoliza" />';
		            
			Ext.onReady(function() {
		
				Ext.define('ClausulaModel', {
				    extend:'Ext.data.Model',
				    fields:['cdclausu','dsclausu','linea_usuario','cdtipcla','linea_general']
				});
				
				var storeClausulasPoliza = new Ext.data.Store({
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
			                'params.nmsituac' : _nmsituac,
			                'params.icodpoliza' : _icodpoliza,
			                'params.cdperson'  : _cdperson
			            },
			            reader : {
			                type : 'json',
			                root : 'clausulasPoliza'
			            }
			        }
			    });
				
				// Cargar store
				storeClausulasPoliza.load({
                    callback: function(records, operation, success) {
                    	if (!success) {
                            //centrarVentanaInterna(showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR));
                    		centrarVentanaInterna(Ext.Msg.show({
                                title: 'Error',
                                msg: 'Error en la consulta, intente m&aacute;s tarde',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            }));
                            return;
                        }
                        if(records.length == 0){
                            //centrarVentanaInterna(showMessage('No hay exclusiones', 'Este asegurado no tiene exclusiones', Ext.Msg.OK, Ext.Msg.INFO));
                            centrarVentanaInterna(Ext.Msg.show({
                                title: 'No hay exclusiones',
                                msg: 'Este asegurado no tiene exclusiones',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.INFO
                            }));
                            return;
                        }
                    }
                });
				
				Ext.create('Ext.panel.Panel', {
					name      : 'pnlClausulas',
					renderTo  : 'dvClausulasPoliza',
					//layout:'fit',
					defaults  : {
						style : 'margin:5px'
					},
	                items: [{
	                	xtype       : 'grid',
					    store       : storeClausulasPoliza,
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
	                                this.up('panel[name=pnlClausulas]').down('[name=detalleClausula]').setValue(rec.get('linea_usuario'));
                                }
	                        }]
					    }]
					},{
                        xtype: 'label',
                        text: 'Detalles:'
                    },{
						xtype : 'textarea',
						name  : 'detalleClausula',
                        height: 300,
                        width : 425
					}]
			    });
		            	
		    });
		</script>

    </head>
    <body>
        <div id="dvClausulasPoliza"></div>
    </body>
</html>