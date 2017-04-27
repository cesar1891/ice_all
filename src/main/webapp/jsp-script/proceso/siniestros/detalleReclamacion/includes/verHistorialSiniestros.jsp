<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Historial Reclamaci&oacute;n</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdPerson =  '<s:property value="params.cdperson" />';
            var _cdramo =  '<s:property value="params.cdramo" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _cdunieco =  '<s:property value="params.cdunieco" />';
            var _proceso  =  '<s:property value="params.proceso" />';
            
            var _URL_cargaHistorialSinies = '<s:url namespace="/siniestros" action="cargaHistorialSiniestros" />';
		            
			Ext.onReady(function() {
		
				Ext.define('ModelHistorial', {
				    extend:'Ext.data.Model',
				    fields:[
						'NTRAMITE',			'CONTRARECIBO',		'ASEGSINIESTRO',	'ASEGURADO',	'EDADASEG',
						'ANTIGUEDAD',		'CONTRATANTE',		'TIPOPAGO',			'ESTATUS',		'SUCURSAL',
						'POLIZA',			'FACTURA',			'FECHAFACT',		'PROVEEDOR',	'SINIESTRO',
						'DIAGNOSTICO',		'CAUSASIN',			'FECHAOCURRE',		'SUBTOTAL',		'IVA',
						'IVARETENIDO',		'ISR',				'IMPCEDULAR',		'PAGADO',		'NMPOLIEX'
					]
				});

				var storeHistorial = new Ext.data.Store({
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
				var params = {	'params.pv_cdperson_i' : _cdPerson,
								'params.pv_cdramo_i'   : _cdramo,
								'params.pv_nmpoliza_i' : _nmpoliza,
								'params.pv_cdunieco_i' : _cdunieco
				};
				cargaStorePaginadoLocal(storeHistorial, _URL_cargaHistorialSinies, 'loadList', params, function(options, success, response){
					if(success){
						var jsonResponse = Ext.decode(response.responseText);
						debug("Valor del Response ===> ",jsonResponse);
						if(jsonResponse.loadList == null || jsonResponse.loadList.length == 0) {
							var respuesta ="";
							if(_proceso =='1'){
								centrarVentanaInterna(Ext.Msg.show({
                                    title:'Aviso',
                                    msg: "La p&oacute;liza no tiene siniestralidad.",
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.INFO
                                }));
							}else{
								centrarVentanaInterna(Ext.Msg.show({
                                    title:'Aviso',
                                    msg: "Este asegurado no cuenta con siniestralidad.",
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.INFO
                                }));
							}
							
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
					title      : 'Historial de Reclamaciones',
                	renderTo  : 'maindivHist',
                	height: 400,
                	border: false,
                	defaults  : {
						style : 'margin:5px'
					},
				    store       : storeHistorial,
				    autoScroll: true,
				    //collapsible: true,
				    titleCollapse: true,
				    buttonAlign : 'center',
				    columns     : [
									{	header    : 'P&oacute;liza',		dataIndex : 'NMPOLIEX',		width: 150	},
									{	header    : 'Asegurado',			dataIndex : 'ASEGURADO',	width: 200, hidden : _proceso != '1'	},
									{	header    : 'Tr&aacute;mite',		dataIndex : 'NTRAMITE',		width: 70	},
									{	header    : 'Contra Recibo',		dataIndex : 'CONTRARECIBO',	width: 90	},
									{	header    : 'Siniestro',			dataIndex : 'SINIESTRO',	width: 70	},
									//{	header    : 'Sucursal',				dataIndex : 'SUCURSAL',		width: 70	},
									{	header    : 'Estatus',				dataIndex : 'ESTATUS',		width: 100	},
									{	header    : 'Tipo Pago',			dataIndex : 'TIPOPAGO',		width: 150	},
									{	header    : 'Factura',				dataIndex : 'FACTURA',		width: 70	},
									{	header    : 'Fecha Factura',		dataIndex : 'FECHAFACT',	width: 90	},
									{	header    : 'Causa Siniestro',		dataIndex : 'CAUSASIN',		width: 95	},
									{	header    : 'Fecha Ocurrencia',		dataIndex : 'FECHAOCURRE',	width: 110	},
									{	header    : 'Proveedor',			dataIndex : 'PROVEEDOR',	width: 200	},
									{	header    : 'Diagn&oacute;stico',	dataIndex : 'DIAGNOSTICO',	width: 200	},
									{	header    : 'Subtotal',				dataIndex : 'SUBTOTAL',		width: 90,	renderer: Ext.util.Format.usMoney	},
									{	header    : 'IVA',					dataIndex : 'IVA',			width: 90,	renderer: Ext.util.Format.usMoney	},
									{	header    : 'IVA Retenido',			dataIndex : 'IVARETENIDO',	width: 90,	renderer: Ext.util.Format.usMoney	},
									{	header    : 'ISR',					dataIndex : 'ISR',			width: 90,	renderer: Ext.util.Format.usMoney	},
									{	header    : 'Impuesto Cedular',		dataIndex : 'IMPCEDULAR',	width: 120,	renderer: Ext.util.Format.usMoney	},
									{	header    : 'Total Pagado',			dataIndex : 'PAGADO',		width: 120,	renderer: Ext.util.Format.usMoney	}
				    			],
				    			bbar : {
				    				displayInfo	: true,
				    				store		: storeHistorial,
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