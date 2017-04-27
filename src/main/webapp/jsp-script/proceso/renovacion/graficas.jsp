<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _url_topicd = '<s:url namespace="/siniestros" action="topIcd" />';
var _url_reservas = '<s:url namespace="/siniestros" action="reservas" />';
var _url_reservasTipPag = '<s:url namespace="/siniestros" action="reservasTipPag" />';
////// urls //////

////// variables //////
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('icdModel', {
	    extend: 'Ext.data.Model',
	    fields: [
	    	 { name: 'DESC_ICD'			,type: 'string' },
	         { name: 'CDICD'			,type: 'string' },
	         { name: 'MONTO_RESERVADO'	,type: 'int'  	},
	         { name: 'MONTO_APROBADO' 	,type: 'int' 	},
	         { name: 'MONTO_PAGADO'	  	,type: 'int'	}
	    ]
	});
    
    Ext.define('tipoPagoModel', {
	    extend: 'Ext.data.Model',
	    fields: [
	         { name: 'TIPO_PAGO'		,type: 'string' },
	         { name: 'MONTO_RESERVADO'	,type: 'int'  	},
	         { name: 'MONTO_APROBADO' 	,type: 'int' 	},
	         { name: 'MONTO_PAGADO'	  	,type: 'int'	}
	    ]
	});
    
    Ext.define('reservasModel', {
	    extend: 'Ext.data.Model',
	    fields: [
	         { name: 'TITULO'		,type: 'string' },
	         { name: 'DAT1'	,type: 'int'  	}
	    ]
	});
    ////// modelos //////
    
    ////// stores //////
    
    var store = Ext.create('Ext.data.Store', {
	    model: 'icdModel',
	    autoLoad:true,
        proxy:
        {
            type: 'ajax',
            url:_url_topicd,
            extraParams : {
            	'params.pv_CdUniEco_i'   : ""
				,'params.pv_CdRamo_i'    : ""
				,'params.pv_nmpoliza_i'  : null
				,'params.pv_fecdesde'   : new Date("10/1/2016")
				,'params.pv_fechasta'   : new Date()
				,'params.pv_cdperson'  : null
				,'params.pv_nmsinies' : null
				,'params.pv_ntramite_i'  : null
				,'params.pv_start_i'  : 0
				,'params.pv_limit_i'  : 25
            },
            reader:
            {
                type: 'json',
                root: 'slist1'
            }
        }
	});
    
    var storeTipoPago = Ext.create('Ext.data.Store', {
	    model: 'tipoPagoModel',
	    autoLoad:true,
        proxy:
        {
            type: 'ajax',
            url:_url_reservasTipPag,
            extraParams : {
            	'params.pv_CdUniEco_i'   : ""
				,'params.pv_CdRamo_i'    : ""
				,'params.pv_nmpoliza_i'  : null
				,'params.pv_fecdesde'   : new Date("10/1/2016")
				,'params.pv_fechasta'   : new Date()
				,'params.pv_cdperson'  : null
				,'params.pv_nmsinies' : null
				,'params.pv_ntramite_i'  : null
				,'params.pv_start_i'  : 0
				,'params.pv_limit_i'  : 25
            },
            reader:
            {
                type: 'json',
                root: 'slist1'
            }
        }
	});
    
    var storeReservas = Ext.create('Ext.data.Store', {
	    model: 'reservasModel',
	    autoLoad:true,
        proxy:
        {
            type: 'ajax',
            url:_url_reservas,
            extraParams : {
            	'params.pv_CdUniEco_i'   : ""
				,'params.pv_CdRamo_i'    : ""
				,'params.pv_nmpoliza_i'  : null
				,'params.pv_fecdesde'   : new Date("10/1/2016")
				,'params.pv_fechasta'   : new Date()
				,'params.pv_cdperson'  : null
				,'params.pv_nmsinies' : null
				,'params.pv_ntramite_i'  : null
				,'params.pv_start_i'  : 0
				,'params.pv_limit_i'  : 25
            },
            reader:
            {
                type: 'json',
                root: 'slist1'
            }
        }
	});
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    var chart=Ext.create('Ext.chart.Chart', {
    	   
    	   width: 400,
    	   height: 300,
    	   store: store,
    	   axes: [{
               type: 'numeric',
               position: 'left',
               fields: 'MONTO_RESERVADO',
               grid: true,
               minimum: 0,
               label: {
                   renderer: function(v) { return  '$ ' + v; }
               }
           }, {
               type: 'category',
               position: 'bottom',
               fields: 'CDICD',
               grid: true,
               label: {
                   rotate: {
                       degrees: -45
                   }
               }
           }],
           series: [{
               type: 'column',
               axis: 'left',
               title: [ 'Reservado', 'Aprobado', 'Pagado'],
               xField: 'CDICD',
               yField: [ 'MONTO_RESERVADO', 'MONTO_APROBADO', 'MONTO_PAGADO'  ],
               style: {
                   opacity: 0.80
               },
               highlight: {
                   fill: '#000',
                   'stroke-width': 1,
                   stroke: '#000'
               },
               tips: {
                   trackMouse: true,
                   style: 'background: #FFF',
                   height: 20,
                   width: "auto",
                   renderer: function(storeItem, item) {
                       var browser = item.series.title[Ext.Array.indexOf(item.series.yField, item.yField)];
                       Ext.util.Format.currencySign="$";
                       this.setTitle(browser + ' para ' + storeItem.get('DESC_ICD') + ': ' +Ext.util.Format.currency(storeItem.get(item.yField)) );
                   }
               }
           }]
    	});
    
    var chartTipoPago=Ext.create('Ext.chart.Chart', {
    	width:400,
        height: 410,
        padding: '10 0 0 0',
        animate: true,
        shadow: false,
        style: 'background: #fff;',
        legend: {
            position: 'right',
            boxStrokeWidth: 0,
            labelFont: '12px Helvetica'
        },
        store: storeTipoPago,
        insetPadding: 40,
        axes: [{
            type: 'numeric',
            position: 'bottom',
            fields: 'MONTO_RESERVADO',
            grid: true,
            label: {
                renderer: function(v) { 
                	Ext.util.Format.currencySign="$";
                	return Ext.util.Format.currency(v); }
            },
            minimum: 0
        }, {
            type: 'category',
            position: 'left',
            fields: 'TIPO_PAGO',
            grid: true
        }],
        series: [{
            type: 'bar',
            axis: 'bottom',
            title: [ 'Monto Reservado', 'Monto Aprovado', 'Monto Pagado' ],
            xField: 'TIPO_PAGO',
            yField: [ 'MONTO_RESERVADO', 'MONTO_APROBADO', 'MONTO_PAGADO' ],
            stacked: true,
            style: {
                opacity: 0.80
            },
            highlight: {
                fill: '#000',
                'stroke-width': 2,
                stroke: '#fff'
            },
            tips: {
                trackMouse: true,
                style: 'background: #FFF',
                height: 20,
                width: 350,
                renderer: function(storeItem, item) {
                    var browser = item.series.title[Ext.Array.indexOf(item.series.yField, item.yField)];
                    Ext.util.Format.currencySign="$"
                    this.setTitle(browser + ' for ' + storeItem.get('TIPO_PAGO') + ': ' + Ext.util.Format.currency(storeItem.get(item.yField)) );
                }
            }
        }]
 	});
    
    var chartReservas=Ext.create('Ext.chart.Chart', {
 	   
 	   width: 400,
 	   height: 300,
 	   animate: true,
 	  shadow: false,
 	   store: storeReservas,
 	  height: 410,
      padding: '10 0 0 0',
      style: 'background: #fff',
      insetPadding: 40,
      legend: {
          field: 'TITULO',
          position: 'bottom',
          boxStrokeWidth: 0,
          labelFont: '12px Helvetica'
      },
 	 
        series: [{
            type: 'pie',
            angleField: 'DAT1',
            donut: 50,
            label: {
                field: 'TITULO',
                display: 'outside',
                calloutLine: true
            },
            showInLegend: true,
            highlight: {
                fill: '#000',
                'stroke-width': 1,
                stroke: '#ccc'
            },
            tips: {
                trackMouse: true,
                style: 'background: #FFF',
                height: 20,
                width: 250,
                renderer: function(storeItem, item) {
                	Ext.util.Format.currencySign="$"
                    this.setTitle(storeItem.get('TITULO') + ': ' + Ext.util.Format.currency(storeItem.get('DAT1')) );
                }
            }
        }]
 	});
    Ext.create('Ext.panel.Panel',
    {
        defaults  : { style : 'margin:5px;' }
        ,renderTo : 'divpri'
        ,border   : 0
        ,items    :
        [
        	Ext.create('Ext.form.Panel', {
        	    title: 'B&Úacute;SQUEDA',
        	    bodyPadding: 5,
        	   

        	    // The form will submit an AJAX request to this URL when submitted
        	    url: 'save-form.php',

        	    // Fields will be arranged vertically, stretched to full width
        	    layout: 'anchor',
        	    defaults: {
        	        anchor: '100%'
        	    },

        	    // The fields
        	    defaultType: 'textfield',
        	    items: [
        	    	{
                        xtype       : 'combo',
                        name        : 'params.cdunieco',
                        fieldLabel  : 'Sucursal',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'MC_SUCURSALES_DOCUMENTO',
                                    'params.cdusuari'   : 'ICE' 
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    
                                }
                            }
                        })
        	    	},
        	    	{
        	    		fieldLabel		: "CDRAMO",
        	    		name			: "cdramo",
        	    	},
        	    	{
        	    		fieldLabel		: "POLIZA",
        	    		name			: "cdramo",
        	    	}
        	    ],

        	    // Reset and Submit buttons
        	    buttons: [{
        	        text: 'Limpiar',
        	        handler: function() {
        	            this.up('form').getForm().reset();
        	        }
        	    }, {
        	        text: 'Buscar',
        	        formBind: true, //only enabled once the form is valid
        	        disabled: true,
        	        handler: function() {
        	            var form = this.up('form').getForm();
        	            if (form.isValid()) {
        	                form.submit({
        	                    success: function(form, action) {
        	                       Ext.Msg.alert('Success', action.result.msg);
        	                    },
        	                    failure: function(form, action) {
        	                        Ext.Msg.alert('Failed', action.result.msg);
        	                    }
        	                });
        	            }
        	        }
        	    }]
        	}),
        	Ext.create('Ext.panel.Panel', {
        		layout:'hbox',
        		autoScroll:true,
        	    items: [chart,chartTipoPago,chartReservas]
        	})
        ]
    });
    
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
////// funciones //////
</script>
</head>
<body>
<div id="divpri" style="height:400px;border:1px solid #CCCCCC"></div>
</body>
</html>