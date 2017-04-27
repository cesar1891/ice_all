<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// variables //////
var _CONTEXT = '${ctx}';

var _URL_RENOVA_SINIESTRALIDAD  =	'<s:url namespace="/siniestros" action="consultaRenovaSiniestros" />';
var _URL_LISTA_DETALLE       	=   '<s:url namespace="/siniestros" action="includes/consultaListaDetalle" />';

var _url_topicd = '<s:url namespace="/siniestros" action="topIcd" />';
var _url_reservas = '<s:url namespace="/siniestros" action="reservas" />';
var _url_reservasTipPag = '<s:url namespace="/siniestros" action="reservasTipPag" />';

var sesionDsrol   						      = _GLOBAL_CDSISROL;

var itemsFormularioBusqueda      = [<s:property value="imap.busquedaItems" 				  escapeHtml="false" />];

var datosTopIcd 		= null;
var datosReservasTippag = null;
var datosReservas 		= null;
////// variables //////

////// componentes dinamicos //////
////// componentes dinamicos //////

Ext.onReady(function()
{
    Ext.Ajax.timeout = 60*60*1000;
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    
    
    ////////////////////////CHARTS///////////////////////////////////
    
    
     ////// modelos //////
    Ext.define('modeloAutEspecial',{
		extend:'Ext.data.Model',
		fields:[
			{type:'string',		name:'CDUNIECO'	        },	
			{type:'string',		name:'CDRAMO'	        },
			{type:'string',		name:'ESTADO'		    },
			{type:'string',		name:'NMPOLIZA'	        },
			{type:'string',		name:'NTRAMITE'		    },
			{type:'string',		name:'TIPO_PAGO'        },
			{type:'string',		name:'POLIZA'           },
			{type:'string',		name:'NMSINIES'   	    },
			{type:'string',		name:'DSUNIECO'         },
			{type:'string',		name:'FECINIVIG'  	    },
			{type:'string',		name:'FECFINVIG'        },
			{type:'string',		name:'AAAPERTU' 	    },
			{type:'string',		name:'NMAUTSER' 	    },
			{type:'string',		name:'FECHA_OCURRENCIA' },
			{type:'string',		name:'CDICD' 	        }, 
			{type:'string',		name:'DESC_ICD' 	    }, 
			{type:'string',		name:'CDPERSON' 		}, 
		    {type:'string',		name:'NOMBRE_ASEGURADO' },
            {type:'string',		name:'EDAD' 		    }, 
            {type:'string',		name:'SEXO' 		    }, 
            {type:'string',		name:'TIPO_PAGO' 		}, 
            {type:'string',		name:'MONTO_RESERVADO'  },
            {type:'string',		name:'MONTO_APROBADO' 	}, 
            {type:'string',		name:'MONTO_PAGADO'     },
            {type:'string',		name:'DSRAMO'           },
            {type:'string',		name:'CDCAUSA'          },
			{type:'string',     name:'FENACIMI'        }
        ]
	});
	
	    Ext.define('modeloAutDetalle',{
		extend:'Ext.data.Model',
		fields:[
			{type:'string',		name:'NTRAMITE'},
			{type:'string',		name:'CDPERSON'},
			{type:'string',		name:'ASEGURADO'},
			{type:'string',		name:'CDPRESTA'},
			{type:'string',		name:'PROVEEDOR'},
			{type:'string',		name:'NFACTURA'},
			{type:'string',		name:'CDGARANT'},
			{type:'string',		name:'DSGARANT'},
			{type:'string',		name:'CDCONVAL'},
			{type:'string',		name:'DSCONVAL '},
			{type:'string',		name:'MONTO_FACTURA'}, 
			{type:'string',		name:'IVA'},
			{type:'string',		name:'IVA_RETENIDO'}, 
			{type:'string',		name:'ISLR'}, 
			{type:'string',		name:'ICED'}
        ]
	});
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
    	var storeGridAutEspecial = new Ext.data.Store({
		pageSize	: 25
		,model		: 'modeloAutEspecial'
		,autoLoad	: false
		,proxy		: {
			enablePaging	:	true,
			reader			:	'json',
			type			:	'memory',
			data			:	[]
		}
	});
	
	var storeGridAutDetalle = Ext.create('Ext.data.Store', {
		model:'modeloAutDetalle',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_LISTA_DETALLE,
			reader: {
				type: 'json',
				root: 'datosValidacion'
			}
		}
	});
	
    var store = Ext.create('Ext.data.Store', {
	    model: 'icdModel',
	    autoLoad:true,
        proxy:
        {
            type: 'ajax',
            url:_url_topicd,
            extraParams : {
            	'params.pv_CdUniEco_i'   : _fieldByName('cdunieco').getValue()
				,'params.pv_CdRamo_i'    : _fieldByName('cdramo').getValue()
				,'params.pv_nmpoliza_i'  : _fieldByName('nmpoliza').getValue()
				,'params.pv_fecdesde'   : _fieldByName('fecini').getValue()
				,'params.pv_fechasta'   : _fieldByName('fecfin').getValue()
				,'params.pv_cdperson'  : _fieldByName('cdperson').getValue()
				,'params.pv_nmsinies' : _fieldByName('siniestro').getValue()
				,'params.pv_ntramite_i'  : _fieldByName('tramite').getValue()
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
            	'params.pv_CdUniEco_i'   : _fieldByName('cdunieco').getValue()
				,'params.pv_CdRamo_i'    : _fieldByName('cdramo').getValue()
				,'params.pv_nmpoliza_i'  : _fieldByName('nmpoliza').getValue()
				,'params.pv_fecdesde'   : _fieldByName('fecini').getValue()
				,'params.pv_fechasta'   : _fieldByName('fecfin').getValue()
				,'params.pv_cdperson'  : _fieldByName('cdperson').getValue()
				,'params.pv_nmsinies' : _fieldByName('siniestro').getValue()
				,'params.pv_ntramite_i'  : _fieldByName('tramite').getValue()
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
            	'params.pv_CdUniEco_i'   : _fieldByName('cdunieco').getValue()
				,'params.pv_CdRamo_i'    : _fieldByName('cdramo').getValue()
				,'params.pv_nmpoliza_i'  : _fieldByName('nmpoliza').getValue()
				,'params.pv_fecdesde'   : _fieldByName('fecini').getValue()
				,'params.pv_fechasta'   : _fieldByName('fecfin').getValue()
				,'params.pv_cdperson'  : _fieldByName('cdperson').getValue()
				,'params.pv_nmsinies' : _fieldByName('siniestro').getValue()
				,'params.pv_ntramite_i'  : _fieldByName('tramite').getValue()
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
	var detalleRenovaSiniestro = Ext.create('Ext.window.Window', {
		title		: 'Detalle Factura'
		,modal	   : true
		,resizable   : true
		//,buttonAlign : 'center'
		,closable	: true
		,closeAction: 'hide'
		,defaults 	:
		{
			style : 'margin:5px;'
		}
		,items	   : 
		[
		Ext.create('Ext.grid.Panel',{
				id             : 'clausulasGridIdDetalle'
				//,title         : 'Detalle Reserva Siniestros'
				,store         :  storeGridAutDetalle
				,titleCollapse : true
				,style         : 'margin:5px'
				,height: 350
                ,width: 750
				,columns       : [
					{   header : 'Tramite',		          dataIndex : 'NTRAMITE' 		,autoSizeColumn: true},
					{   header : 'clave Persona',		  dataIndex : 'CDPERSON' 		,autoSizeColumn: true},
					{   header : 'Asegurado',		      dataIndex : 'ASEGURADO'		,autoSizeColumn: true},
					{   header : 'cdpresta',			  dataIndex : 'CDPRESTA'	    ,autoSizeColumn: true},
					{   header : 'Proveedor',			  dataIndex : 'PROVEEDOR'		,autoSizeColumn: true},
					{   header : 'No. Factura',		      dataIndex : 'NFACTURA'		,autoSizeColumn: true},
					{   header : 'Clave Cobertura',		  dataIndex : 'CDGARANT'		,autoSizeColumn: true},
					{   header : 'Descripcion Cobertura', dataIndex : 'DSGARANT'		,autoSizeColumn: true},
					{   header : 'CDCONVAL',		      dataIndex : 'CDCONVAL'		,autoSizeColumn: true},
					{   header : 'DSCONVAL',		      dataIndex : 'DSCONVAL'		,autoSizeColumn: true},
					{   header : 'MONTO FACTURA',		  dataIndex : 'MONTO_FACTURA'	,autoSizeColumn: true},
					{   header : 'IVA',		              dataIndex : 'IVA'			    ,autoSizeColumn: true},
					{   header : 'IVA RETENIDO',          dataIndex : 'IVA_RETENIDO'	,autoSizeColumn: true},
					{	header : 'ISLR',		          dataIndex : 'ISLR'			,autoSizeColumn: true},
					{	header : 'ICED',		          dataIndex : 'ICED'			,autoSizeColumn: true}
					
				]/*,
				bbar     :{
				displayInfo : true,
					store		: storeGridAutDetalle,
					xtype		: 'pagingtoolbar'
				}*/
			})
		
		]
	});
    var chart=Ext.create('Ext.chart.Chart', {
    	   
    	   width:900,
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
                   width: 400,
                   renderer: function(storeItem, item) {
                       var browser = item.series.title[Ext.Array.indexOf(item.series.yField, item.yField)];
                       Ext.util.Format.currencySign="$";
                       this.setTitle(browser + ' para ' + storeItem.get('DESC_ICD') + ': ' +Ext.util.Format.currency(storeItem.get(item.yField)) );
                   }
               }
           }]
    	});
    
    var chartTipoPago=Ext.create('Ext.chart.Chart', {
    	width:1000,
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
 	   
    	width:1000,
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
    
    ///////////////////////////////////////////////////////////
    ////// modelos //////
    
    ////// modelos //////
    
    ////// stores //////

    ////// stores //////
    
    ////// componentes //////
    
    ////// contenido //////
    
    var panelInicialPral= Ext.create('Ext.panel.Panel',
    {
        renderTo    : '_p25_divpri'
        ,autoScroll : true
        ,itemId     : 'formBusqueda'
        ,defaults   : { style : 'margin : 5px;' }
        ,border     : 0
        ,autoScroll : true
        ,items      : [
        
        	Ext.create('Ext.form.Panel',{
                title     : 'Buscar p&oacute;lizas a renovar'
                ,itemId   : '_p25_busquedaForm'
                ,defaults : { style : 'margin :5px;' }
                ,layout   : {
                    type     : 'table'
                    ,columns : 3
                }
                ,items       : itemsFormularioBusqueda
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Buscar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : function(){
                        	_mask("Cargando...");
                        	
                        	//storeGridAutEspecial.removeAll();
							var params = {
								'params.pv_CdUniEco_i'   : _fieldByName('cdunieco').getValue()
								,'params.pv_CdRamo_i'    : _fieldByName('cdramo').getValue()
								,'params.pv_nmpoliza_i'  : _fieldByName('nmpoliza').getValue()
								,'params.pv_fecdesde'   : _fieldByName('fecini').getValue()
								,'params.pv_fechasta'   : _fieldByName('fecfin').getValue()
								,'params.pv_cdperson'  : _fieldByName('cdperson').getValue()
								,'params.pv_nmsinies' : _fieldByName('siniestro').getValue()
								,'params.pv_ntramite_i'  : _fieldByName('tramite').getValue()
								,'params.pv_start_i'  : 0
								,'params.pv_limit_i'  : 25
							};
							cargaStorePaginadoLocal(storeGridAutEspecial, _URL_RENOVA_SINIESTRALIDAD, 'datosValidacion', params, function(options, success, response){
								_unmask();
								if(success){
									store.extraParams=params
									storeTipoPago.extraParams=params
									storeReservas.extraParams=params
									store.load({
										params:params
									});
									storeTipoPago.load({
										params:params
									});
									storeReservas.load({
										params:params
									});
									var jsonResponse = Ext.decode(response.responseText);
									if(jsonResponse.datosValidacion &&jsonResponse.datosValidacion.length == 0) {
										if(null == null){
											centrarVentanaInterna(showMessage("Aviso", "No existe tr&aacute;mites.", Ext.Msg.OK, Ext.Msg.INFO));
										}
										_fieldById("graficas").hide();
										return;
									}
									_fieldById("graficas").show();
									debug(jsonResponse);
								}else{
									centrarVentanaInterna(showMessage('Error', 'Error al obtener los datos',Ext.Msg.OK, Ext.Msg.ERROR));
								}
							})}
                    
                    },
                    {
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat.png'
                        ,handler : function (){
                        	//Todo Codigo
                        	/*panelInicialPral.down('combo[name=CDUNIECO]').reset();
							panelInicialPral.down('combo[name=CDRAMO]').reset();
							panelInicialPral.down('[name=NMPOLIZA]').setValue('');
							panelInicialPral.down('[name=CDPERSON]').setValue('');
							panelInicialPral.down('[name=NTRAMITE]').setValue('');
							panelInicialPral.down('[name=NMSINIES]').setValue('');*/
                        	_fieldByName('cdunieco').setValue('');
							_fieldByName('cdramo').setValue('');
							_fieldByName('nmpoliza').setValue('');
							_fieldByName('fecini').setValue(new Date());
							_fieldByName('fecfin').setValue(new Date());
							_fieldByName('cdperson').setValue('');
							_fieldByName('siniestro').setValue('');
							_fieldByName('tramite').setValue('');
							storeGridAutEspecial.removeAll();
                        }
                    }
                ]
                ,listeners	:	{
                	click	:	function(options) {
                		debug('click panel');
                		}
                }
            })
            
        ,
        Ext.create('Ext.tab.Panel', {
        	
        	itemId:"graficas",
            
            items: [{
                title: 'RESERVAS',
                items:[
                	chartReservas
                ]
            }, {
                title: 'RESERVAS TIPO PAGO',
                items:[
                	chartTipoPago
                ]
            },
            {
                title: 'TOP ICD',
                items:[
                	chart
                ]
            }]
        })
        ,Ext.create('Ext.grid.Panel',{
				id             : 'clausulasGridId'
				,title         : 'Reserva Siniestros'
				,store         :  storeGridAutEspecial
				,titleCollapse : true
				,style         : 'margin:5px'
				,height        : 400
				,columns       : [
					{   xtype: 'actioncolumn',      width: 40,          sortable: false,            menuDisabled: true,
	                    items: [{
	                        icon: _CONTEXT+'/resources/fam3icons/icons/application_edit.png',
	                        tooltip: 'Detalle Siniestro',	//(EGS)
	                        scope: this,
	                        handler: function(grid,rowindex){
	                        	 
	                        	// Todo COdigo
	                        	_11_recordActivo = grid.getStore().getAt(rowindex);
	                        	//detalleRenovaSiniestro.close();
	                        	
	                        	storeGridAutDetalle.load({
									params:{
										'params.pv_CdUniEco_i': _11_recordActivo.get('CDUNIECO'),
										'params.pv_CdRamo_i'  : _11_recordActivo.get('CDRAMO'),
										'params.pv_nmpoliza_i': _11_recordActivo.get('NMPOLIZA'),
										'params.pv_cdperson'  : _11_recordActivo.get('CDPERSON'),
										'params.pv_ntramite_i': _11_recordActivo.get('NTRAMITE'),
										'params.pv_nmsinies'  : _11_recordActivo.get('NMSINIES'),
										'params.pv_fecdesde'  : _fieldByName('fecini').getValue(),//_11_recordActivo.get('FECINIVIG'),
								        'params.pv_fechasta'  : _fieldByName('fecfin').getValue()//_11_recordActivo.get('FECFINVIG')
									}
								});
	                        	debug('detalleRenovaSiniestro',detalleRenovaSiniestro);
	                        	detalleRenovaSiniestro.show();
								centrarVentanaInterna(detalleRenovaSiniestro);
	                        	
	                        	//fin
	                        	
	                        	
	                        }
	                    }]
	                 },
					{	header     : 'Cdunieco'       ,	dataIndex : 'CDUNIECO',		flex : 1, 	hidden   : true	},
					{	header     : 'Cdramo'         ,	dataIndex : 'CDRAMO',		flex : 1, 	hidden   : true	},
					{	header     : 'Estado'         ,	dataIndex : 'ESTADO',		flex : 1, 	hidden   : true	},
					{	header     : 'Nmpoliza'       ,	dataIndex : 'NMPOLIZA',		flex : 1, 	hidden   : true	},
					{	header     : 'Ntramite'       ,	dataIndex : 'NTRAMITE',		flex : 1, 	hidden   : true	},
					{	header     : 'Tipo Pago'      , dataIndex : 'TIPO_PAGO',    width : 200             },
					{	header     : 'Poliza'         , dataIndex : 'POLIZA'      , width : 150				},
					{	header     : 'Siniestro'      , dataIndex : 'NMSINIES'    ,	width : 130				},
					{	header     : 'Sucursal'       ,	dataIndex : 'DSUNIECO' ,	width : 150				},
					{	header     : 'Fecha Inicio'   ,	dataIndex : 'FECINIVIG'   ,	width : 80				},
					{	header     : 'Fecha Fin'      ,	dataIndex : 'FECFINVIG'   ,	width : 100				},
					{	header     : 'Autorizaci&oacute;n<br/> (Pre-Siniestro)' , dataIndex : 'NMAUTSER',	      width : 200},
					{	header     : 'Fecha <br/>Ocurrencia'                    , dataIndex : 'FECHA_OCURRENCIA', width : 80 },
					{	header     : 'ICD Principal'                            , dataIndex : 'CDICD',	          width : 80 },
					{	header     : 'Des. ICD <br/>Principal'                  , dataIndex : 'DESC_ICD',	      width : 150 },
					{	header     : 'Cdperson'                                 , dataIndex : 'CDPERSON',		  width : 120 },
					{	header     : 'Nom. Asegurado<br/> Afectado'             , dataIndex : 'NOMBRE_ASEGURADO', width : 150},
					{	header     : 'Edad'                                     , dataIndex : 'EDAD',		      width : 100},
					{	header     : 'Sexo'                                     , dataIndex : 'SEXO',		      width : 200},
					{	header     : 'Monto Reservado'                          , dataIndex : 'MONTO_RESERVADO',  width : 200},
					{	header     : 'Monto Aprobado'                           , dataIndex : 'MONTO_APROBADO',	  width : 200},
					{	header     : 'Monto Pagado'                             , dataIndex : 'MONTO_PAGADO',     width : 200},
					{	header     : 'Ramo'                                     , dataIndex : 'DSRAMO',           width : 200},
					{	header     : 'Causa Siniestro'                          , dataIndex : 'CDCAUSA',          width : 200},
					{	header     : 'Fecha Nacimiento'                         , dataIndex : 'FENACIMI',         width : 200}
					
				],bbar     :{
				displayInfo : true,
					store		: storeGridAutEspecial,
					xtype		: 'pagingtoolbar'
				}
			})
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    _fieldById("graficas").hide();
    var form = _fieldById('_p25_busquedaForm');
    
   /* _fieldByName('division',form).getStore().on({
        load : function(me){
  		    var division = _fieldByName('division',form);
  		    division.setValue('COLECTIVO');  		    
  		    if(true){
  		        debug('division',division);
  		        division.store.removeAt(2);
  		    }  		    
  	    }
  	});
  	    
  	_fieldByName('division',form).on({
  	    change: function(me, newValue) {
  	    	debug(me, newValue);
  	    	
  	    	debug('Sucursal',_fieldByName('Sucursal').getValue());
			debug('Ramo'    ,_fieldByName('Ramo').getValue()    );
			debug('Poliza'  ,_fieldByName('Poliza').getValue()  );
			debug('feDesde' ,_fieldByName('feDesde').getValue() );
			debug('feHasta' ,_fieldByName('feHasta').getValue() );
			debug('division',_fieldByName('division').getValue());
			
  		    if(me.getValue() == 'COLECTIVO'){
  			    _fieldByName('feDesde',form).hide();
  				_fieldByName('feHasta',form).hide();
  				
  				_fieldByName('Sucursal',form).show();
  				_fieldByName('Ramo',form).show();
  				_fieldByName('Poliza',form).show();
  				
  				form.doLayout();
  		    }
  		    else if(me.getValue() == 'RENOVAR_X_FECHAS'){
  			    _fieldByName('feDesde',form).show();
  				_fieldByName('feHasta',form).show();
  				
  				_fieldByName('Sucursal',form).hide();
  				_fieldByName('Ramo',form).hide();
  				_fieldByName('Poliza',form).hide();
  				
  				form.doLayout();
  			}
  	    }
  	});
  	*/
  	
    ////// custom //////
    
    ////// loaders //////
  	
    ////// loaders //////
});

////// funciones //////



////// funciones //////
</script>
</head>
<body><div id="_p25_divpri" style="height:1000px;"></div></body>
</html>