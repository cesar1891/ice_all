<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Exclusi&oacute;n ICD</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdunieco =  '<s:property value="params.cdunieco" />';
            var _cdramo   =  '<s:property value="params.cdramo" />';
            var _estado   =  '<s:property value="params.estado" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _nmsuplem =  '<s:property value="params.nmsuplem" />';
            var _nmsituac =  '<s:property value="params.nmsituac" />';
            
            var _URL_cargaHistorialSinies = '<s:url namespace="/siniestros" action="cargaICDExcluidosAsegurado" />';
		            
			Ext.onReady(function() {
		
				Ext.define('ModelHistorial', {
				    extend:'Ext.data.Model',
				    fields:[
						'CLAUSULA',			'DESCLAUSULA',		'ICD',	     'DESCICD'
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
				var params = {	
					'params.pv_cdunieco_i' : _cdunieco,
					'params.pv_cdramo_i'   : _cdramo,
					'params.pv_estado_i'   : _estado,
					'params.pv_nmpoliza_i' : _nmpoliza,
					'params.pv_nmsuplem_i' : _nmsuplem,
					'params.pv_nmsituac_i' : _nmsituac
				};
				cargaStorePaginadoLocal(storeHistorial, _URL_cargaHistorialSinies, 'loadList', params, function(options, success, response){
					if(success){
						var jsonResponse = Ext.decode(response.responseText);
						debug("Valor del Response ===> ",jsonResponse);
						if(jsonResponse.loadList == null || jsonResponse.loadList.length == 0) {
							showMessage("Aviso", "Este asegurado no cuenta con exclusión de ICD.", Ext.Msg.OK, Ext.Msg.INFO);
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
                	height: 300,
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
									{	header    : 'Clausula',			   dataIndex : 'CLAUSULA',		width: 100	},
									{	header    : 'Desc. Clausula',	   dataIndex : 'DESCLAUSULA',	width: 250	},
									{	header    : 'ICD',				   dataIndex : 'ICD',		    width: 100	},
									{	header    : 'Desc. ICD',		   dataIndex : 'DESCICD',	    width: 350	}
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