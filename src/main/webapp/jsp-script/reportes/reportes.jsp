<%@ include file="/taglibs.jsp"%>
<%@ page language="java" %>
<html>
<head>
    <title>Reportes</title>
    <script>
    
	    // URLS:
	    var _URL_LISTA_REPORTES             = '<s:url namespace="/reportes" action="obtenerListaReportes" />';            
	    var _URL_LOADER_COMPONENTES_REPORTE = '<s:url namespace="/reportes" action="includes/obtenerComponentesReporte" />';
    
        Ext.onReady(function(){
            
        	
        	// Models:
            Ext.define('ReportesModel',{
                extend : 'Ext.data.Model',
                fields : [
                    "cdReporte",
                    "dsReporte",
                    "cdPantalla",
                    "cdSeccion"
                ]
            });
        	
            
        	//Stores:
        	var storeReportes = Ext.create('Ext.data.Store', {
		        model : 'ReportesModel',
		        proxy : {
                   type: 'ajax',
                   url : _URL_LISTA_REPORTES,
                   reader : {
                       type : 'json',
                       root : 'reportes'
                   }
                },
                sorters: [{
                	property: 'dsReporte',
                	direction: 'ASC'
                }],
                autoLoad : true
		    });
        	
        	
            // Main component:
        	Ext.create('Ext.panel.Panel', {
        		renderTo : 'dvPrincipalReportes',
        		title    : 'Reportes',
        		defaults : {
        			style: 'margin:5px'
        		},
        		items : [{
       		    	xtype : 'panel',
       		    	layout: 'border',
       		    	height: 400,
       		    	border: 0,
       		    	items: [{
       		    	    xtype  : 'grid',
       		    	    region : 'west',
       		    	    title  : 'Seleccione un reporte:',
       		    	    hideHeaders: true,
       		    	    collapsible : true,
       		    	    width: 350,
       		    	    margins : '0 5 0 0',
       		    	    store : storeReportes,
		                columns: [{
		                    dataIndex: 'dsReporte',
		                    flex : 1,
		                    renderer : function(value, metadata, record, rowIndex, colIndex, store, view) {
		                    	// Se agrega tooltips a la columna:
							    metadata.tdAttr = 'data-qtip="' + value + '"';
							    return value;
                    		}
		                }],
       		    	    listeners: {
       		    	    	'itemclick' : function(view, record, item, index, e, eOpts) {
       		    	    		
       		    	    		// Cargar componentes del reporte elegido:
       		    	    		this.up('panel').down('[name=pnlComponentesReporte]').getLoader().load({
       		    	    			params: {
       		    	    				cdreporte : record.get('cdReporte'),
       		    	    				cdPantalla: record.get('cdPantalla'),
       		    	    				cdSeccion: record.get('cdSeccion')
       		    	    			}
       		    	    		});
       		    	    	}
       		    	    }
       		    	},{
       		    		xtype  : 'panel',
       		    		name   : 'pnlComponentesReporte',
       		    		layout : 'fit',
                           region : 'center',
                           loader: {
                               url: _URL_LOADER_COMPONENTES_REPORTE,
                               scripts  : true,
                               loadMask : true,
                               autoLoad : false,
                               ajaxOptions: {
                                   method: 'POST'
                               }
                           }
       		    	}]
        		}]
        	});
        });
    </script>
</head>
<body>
    <div id="dvPrincipalReportes" style="height:400px;"></div>
</body>
</html>