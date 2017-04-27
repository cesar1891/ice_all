<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Historial Reclamaci&oacute;n</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdnmautser =  '<s:property value="params.nmautser" />';
            
            var _URL_cargaHistorialCPTPagado = '<s:url namespace="/siniestros" action="cargaHistorialCPTPagados" />';
		            
			Ext.onReady(function() {
		
				Ext.define('ModelHistorial', {
				    extend:'Ext.data.Model',
				    fields:[
						'NTRAMITE',			'CONTRARECIBO',			'NFACTURA',			'NMSINIES',
						'DSGARANT',			'DSCONVAL',				'SUBTOTAL',			'IVA',
						'IVARETENIDO',		'ISR',					'IMPCEDULAR',		'PAGADO',
						'DSESTATUS',		'NOMBREASEG',			'POLIZA'
					]
				});

				var storeHistorialCPTPagado = new Ext.data.Store({
					pageSize	: 10
					,model		: 'ModelHistorial'
					,autoLoad	: false
					,proxy		: {
						enablePaging	: true,
						reader			: 'json',
						type			: 'memory',
						data			: []
					}
				});
				
				// Cargar store
				var params = {	'params.pv_nmautser_i' : _cdnmautser
				};
				cargaStorePaginadoLocal(storeHistorialCPTPagado, _URL_cargaHistorialCPTPagado, 'loadList', params, function(options, success, response){
					if(success){
						var jsonResponse = Ext.decode(response.responseText);
						debug("Valor del Response ===> ",jsonResponse);
						if(jsonResponse.loadList == null || jsonResponse.loadList.length == 0) {
							var respuesta ="";
							/*if(_proceso =='1'){
								showMessage("Aviso", "La autorización no ha sido pagado en ningun ContraRecibo..", Ext.Msg.OK, Ext.Msg.INFO);
							}else{
								showMessage("Aviso", "La autorización no ha sido pagado en ningun ContraRecibo.", Ext.Msg.OK, Ext.Msg.INFO);
							}*/
							
						}
					}else{
						Ext.Msg.show({
							title: 'Aviso',
							msg: 'Error al obtener los datos.',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
							});
					}
				});

				Ext.create('Ext.grid.Panel', {
					title      : 'Historial CPT pagados',
                	renderTo  : 'maindivHist',
                	height: 400,
                	border: false,
                	defaults  : {
						style : 'margin:5px'
					},
				    store       : storeHistorialCPTPagado,
				    autoScroll: true,
				    //collapsible: true,
				    titleCollapse: true,
				    buttonAlign : 'center',
				    columns     : [
									{	header    : 'Tr&aacute;mite',		dataIndex : 'NTRAMITE',		width: 100	},
									{	header    : 'Factura',				dataIndex : 'NFACTURA',		width: 100	},
									{	header    : 'Contrarecibo',			dataIndex : 'CONTRARECIBO',	width: 100	},
									{	header    : 'Estatus',				dataIndex : 'DSESTATUS',	width: 100	},
									{	header    : 'Asegurado',			dataIndex : 'NOMBREASEG',	width: 250	},
									{	header    : 'P&oacute;liza',		dataIndex : 'POLIZA',		width: 100	},
									{	header    : 'Siniestro',			dataIndex : 'NMSINIES',		width: 100	},
									{	header    : 'Cobertura',			dataIndex : 'DSGARANT',		width: 150	},
									{	header    : 'Subcobertura',			dataIndex : 'DSCONVAL',		width: 200	},
									{	header    : 'Subtotal',				dataIndex : 'SUBTOTAL',		width: 100,		renderer: Ext.util.Format.usMoney	},
									{	header    : 'IVA',					dataIndex : 'IVA',			width: 100,		renderer: Ext.util.Format.usMoney	},
									{	header    : 'IVA Retenido',			dataIndex : 'IVARETENIDO',	width: 100,		renderer: Ext.util.Format.usMoney	},
									{	header    : 'ISR',					dataIndex : 'ISR',			width: 100,		renderer: Ext.util.Format.usMoney	},
									{	header    : 'Imp. Cedular',			dataIndex : 'IMPCEDULAR',	width: 100,		renderer: Ext.util.Format.usMoney	},
									{	header    : 'Pagado',				dataIndex : 'PAGADO',		width: 100,		renderer: Ext.util.Format.usMoney	}
				    			],
				    			bbar : {
				    				displayInfo	: true,
				    				store		: storeHistorialCPTPagado,
				    				xtype		: 'pagingtoolbar'
				    			}
				});
		            	
		    });
		</script>

    </head>
    <body>
        <div id="maindivHist"></div>
    </body>
</html>