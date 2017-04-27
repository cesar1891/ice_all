<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Periodos de Vigencia</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdUnieco =  '<s:property value="params.cdunieco" />';
            var _cdRamo   =  '<s:property value="params.cdramo" />';
            var _estado   =  '<s:property value="params.estado" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _nmsituac =  '<s:property value="params.nmsituac" />';
            var _icodpoliza =  '<s:property value="params.icodpoliza" />';
            var _cdperson =  '<s:property value="params.cdperson" />';
            var _nombre     = '<s:property value="params.nombre" />';
            
            var _URL_CONSULTA_VIGENCIA = '<s:url namespace="/consultasPoliza" action="consultaPeriodosVigencia" />';
		            
			Ext.onReady(function() {
		
				Ext.define('VigenciaModel', {
				    extend:'Ext.data.Model',
				    fields: [
                        {type:'string', name:'cdperson'},
                        {type:'string', name:'nombre'},
                        {type:'string', name:'estatus'},
                        {type:'string', name:'dias'},
                        {type:'string', name:'anios'},                        
                        {type:'string',   name:'feinicial'},
                        {type:'string',   name:'fefinal'}
                    ]
				});
				
				var storeVigencia = new Ext.data.Store({
			        model      : 'VigenciaModel',
			        //autoLoad: true,
			        proxy     : {
			        	type        : 'ajax',
			            url         : _URL_CONSULTA_VIGENCIA,
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
			                root : 'periodosVigencia'
			            }
			        }
			    });
				
				// Cargar store
				storeVigencia.load({
                    callback: function(records, operation, success) {
                    	if (!success) {
                            showMessage('Error', 'Error en la consulta, intente m&aacute;s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                            return;
                        }
                        if(records.length == 0){
                            showMessage('No hay periodos de vigencia', 'Este asegurado no tiene periodos de vigencia', Ext.Msg.OK, Ext.Msg.INFO);
                            return;
                        }
                    }
                });
				
				Ext.create('Ext.panel.Panel', {
					name      : 'pnlVigencia',
					renderTo  : 'dvVigencia',
					//layout:'fit',
					defaults  : {
						style : 'margin:5px',
						border : false
					},
	                items: [{
                           layout : 'hbox',
                           items :[
                               {xtype: 'textfield', name: 'cdperson', fieldLabel: 'ID Afiliado',      readOnly: true, labelWidth: 120, width: 300, value : _cdperson},
                               {xtype: 'textfield', name: 'nombre', fieldLabel: 'Nombre',      readOnly: true, labelWidth: 120, width: 600, value : _nombre}
                           ]  
	                   },{
	                	xtype       : 'grid',
					    store       : storeVigencia,
					    buttonAlign : 'center',
					    columns     : [{
					        header    : 'Estatus',
					        dataIndex : 'estatus',
					        flex      : 1
					    },{
                            header    : 'No. Dias',
                            xtype      : 'numbercolumn',
                            format      : '0.00',
                            dataIndex : 'dias',                            
                            flex      : 1
                        },{
                            header    : 'No. Años',
                             xtype      : 'numbercolumn',
                            format      : '0.00',
                            dataIndex   : 'anios',
                            flex      : 1
                        },{
                            header    : 'Fec. Inicial',
                            dataIndex : 'feinicial',
                            flex      : 1                            
                        },{
                            header    : 'Fec. Final',
                            dataIndex : 'fefinal',                            
                            flex      : 1                            
                        }
                        ]
					}]
			    });
		            	
		    });
		</script>

    </head>
    <body>
        <div id="dvVigencia"></div>
    </body>
</html>