<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Hist&oacute;rico de farmacia</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdUnieco =  '<s:property value="params.cdunieco" />';
            var _cdRamo   =  '<s:property value="params.cdramo" />';
            var _estado   =  '<s:property value="params.estado" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _nmsituac =  '<s:property value="params.nmsituac" />';
            var _icodpoliza =  '<s:property value="params.icodpoliza" />';
            var _cdperson =  '<s:property value="params.cdperson" />';
            
            var _URL_CONSULTA_HISTORICO_FARMACIA = '<s:url namespace="/consultasPoliza" action="consultaHistoricoFarmacia" />';
		            
			Ext.onReady(function() {
		
				Ext.define('FarmaciaModel', {
				    extend:'Ext.data.Model',
				    fields:['iultimo','poliza','tigrupo','maximo','total','pendiente','gastototal','disponible']
				});
				
				var storeHistoricoFarmacia = new Ext.data.Store({
			        model      : 'FarmaciaModel',
			        //autoLoad: true,
			        proxy     : {
			        	type        : 'ajax',
			            url         : _URL_CONSULTA_HISTORICO_FARMACIA,
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
			                root : 'historicoFarmacia'
			            }
			        }
			    });
				
				// Cargar store
				storeHistoricoFarmacia.load({
                    callback: function(records, operation, success) {
                    	if (!success) {
                            showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                            return;
                        }
                        if(records.length == 0){
                            showMessage('No hay histórico de farmacia', 'Este asegurado no tiene histórico de farmacia', Ext.Msg.OK, Ext.Msg.INFO);
                            return;
                        }
                    }
                });
				
				Ext.create('Ext.panel.Panel', {
					name      : 'pnlHistoricoFarmacia',
					renderTo  : 'dvHistoricoFarmacia',
					//layout:'fit',
					defaults  : {
						style : 'margin:5px'
					},
	                items: [{
	                	xtype       : 'grid',
					    store       : storeHistoricoFarmacia,
					    buttonAlign : 'center',
					    columns     : [{
					        header    : 'Ref.',
					        dataIndex : 'iultimo',
					        flex      : 1
					    },{
                            header    : 'Poliza',
                            dataIndex : 'poliza',
                            flex      : 1
                        },{
                            header    : 'Grupo',
                            dataIndex : 'tigrupo',
                            flex      : 1
                        },{
                            header    : 'Beneficio Máximo',
                            dataIndex : 'maximo',
                            flex      : 1,
                            renderer  : 'usMoney'
                        },{
                            header    : 'Gasto Aplicado',
                            dataIndex : 'total',
                            flex      : 1,
                            renderer  : 'usMoney'
                        },{
                            header    : 'Pend. de Aplicar',
                            dataIndex : 'pendiente',
                            flex      : 1,
                            renderer  : 'usMoney'
                        },{
                            header    : 'Gasto Total',
                            dataIndex : 'gastototal',
                            flex      : 1,
                            renderer  : 'usMoney'
                        },{
                            header    : 'Disponible',
                            dataIndex : 'disponible',
                            flex      : 1,
                            renderer  : 'usMoney'
                        }
                        ]
					}]
			    });
		            	
		    });
		</script>

    </head>
    <body>
        <div id="dvHistoricoFarmacia"></div>
    </body>
</html>