<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// variables //////
var contexto							      = '${ctx}';
var urlServidorReports  					  = '<s:property value="rutaServidorReports"         										    />';
var complerepSrvUsr     					  = '<s:property value="passServidorReports"         										    />';
var panDatComMap1 						      = '<s:property value="%{convertToJSON('slist')}" escapeHtml="false" 				        />';
var urlGuardar                   		      = '<s:url namespace="/"            action="guardarDatosComplementarios" 				    />';
var urlCargarCatalogos              	      = '<s:url namespace="/catalogos"   action="obtieneCatalogo"             				    />';
var _p25_urlBuscarPolizas  				      = '<s:url namespace="/renovacion"  action="buscarPolizasIndividualesRenovables" 		    />';
var _p25_urlBuscarPolizasMasivas		      = '<s:url namespace="/renovacion"  action="buscarPolizasIndividualesMasivasRenovables"    />';
var _p25_urlBuscarCondiciones		          = '<s:url namespace="/renovacion"  action="obtenerCondicionesRenovacionIndividual"        />';
var _p25_urlBuscarCalendario		          = '<s:url namespace="/renovacion"  action="obtenerCalendarizacionRenovacionIndividual"    />';
var _p25_urlRenovarPolizaIndividual		      = '<s:url namespace="/renovacion"  action="renovarPolizaIndividual"			   		    />';
var _p25_urlRenovarPolizasMasivasIndividuales = '<s:url namespace="/renovacion"  action="renovarPolizasMasivasIndividuales"             />';
var _p25_urlConfirmarPolizaIndividual	      = '<s:url namespace="/renovacion"  action="confirmarPolizaIndividual"				        />';
var _p25_urlActualizaValoresCotizacion	      = '<s:url namespace="/renovacion"  action="actualizaValoresCotizacion"				    />';
var _p25_urlMovimientoCondiciones	      	  = '<s:url namespace="/renovacion"  action="movimientoCondicionesRenovacionIndividual"	    />';
var _p25_urlMovimientoCalendario	      	  = '<s:url namespace="/renovacion"  action="movimientoCalendarizacionRenovacionIndividual" />';
var _p25_urlValidaExclusionValor              = '<s:url namespace="/renovacion"  action="validaExclusionValor" />';
var _p25_urlBuscarContratantes  		      = '<s:url namespace="/endoso" 	 action="cargarContratantesEndosoContratante" 		    />';
var urlPantallaValosit           		      = '<s:url namespace="/"            action="pantallaValosit"             				    />';
var urlEditarAsegurados 				      = '<s:url namespace="/" 		     action="editarAsegurados"							    />';           
var _p25_urlPantallaCliente        	 	      = '<s:url namespace="/catalogos"   action="includes/personasLoader"            		    />';
var panDatComUrlDoc2                          = '<s:url namespace="/documentos"  action="ventanaDocumentosPolizaClon" 				    />';
var compleUrlViewDoc             			  = '<s:url namespace="/documentos"  action="descargaDocInline"           				    />';
var urlRecotizar                 		      = '<s:url namespace="/"            action="recotizar"                   				    />';
var _p25_urlObtenerItemsTvalopol		      = '<s:url namespace="/renovacion"  action="obtenerItemsTvalopol"						    />';
var _p25_urlValidateValueExclusion            = '<s:url namespace="/renovacion"  action="validateValueExclusion"                        />';
var sesionDsrol   						      = _GLOBAL_CDSISROL;
var _p25_storePolizas;
var _p25_storePolizasMasivas;
var _p25_storeCondiciones;
var _p25_ultimosParams;

var winAutoServicio;
var winServicioAsistido;
var winCambioPago;
var winCambioDomicilio;
var wineditarContratante;
var winMensaje;
var panCondicion;
var panAyuda;
var pantallaValositParche = false;
var _p22_parentCallback;
var _NOMBRE_REPORTE_CARATULA;
/*var panDatComUpdateFerenova = function(field,value)
{
	try
    	{
        	if(!Ext.isEmpty(plazoEnDias) && '|16|5|6|'.lastIndexOf('|'+panDatComMap1.cdramo+'|')!=-1)
            	{
                	Ext.getCmp('fechaRenovacion').setValue(Ext.Date.add(value, Ext.Date.DAY, plazoEnDias));
                }
                else
                {
                	Ext.getCmp('fechaRenovacion').setValue(Ext.Date.add(value, Ext.Date.YEAR, 1));
                }
		}catch(e)
        {
        	debugError(e);
		}
}*/
////// variables //////

////// componentes dinamicos //////
var itemsFormularioBusqueda      = [<s:property value="imap.busquedaItems" 				  escapeHtml="false" />];
var itemsFormularioContratante   = [<s:property value="imap.itemsFormularioContratante"   escapeHtml="false" />];
var itemsFormularioPolizaColumns = [<s:property value="imap.itemsFormularioPolizaColumns" escapeHtml="false" />];
var gridColumns					 = [<s:property value="imap.gridColumns"                  escapeHtml="false" />];
var itemsEditarPago				 = [<s:property value="imap.itemsEditarPago"              escapeHtml="false" />];
var itemsEditarDomicilio	     = [<s:property value="imap.itemsEditarDomicilio"         escapeHtml="false" />];
var itemsTatrisit	     		 = [<s:property value="imap.itemsTatrisit"				  escapeHtml="false" />];
var itemsCondicionesRenovacion   = [<s:property value="imap.itemsCondicionesRenovacion"	  escapeHtml="false" />];
var itemsCondicionesColumns 	 = [<s:property value="imap.itemsCondicionesColumns"      escapeHtml="false" />];
var itemsCalendarioRenovacion    = [<s:property value="imap.itemsCalendarioRenovacion"	  escapeHtml="false" />];
var itemsCalendarioColumns 	 	 = [<s:property value="imap.itemsCalendarioColumns"       escapeHtml="false" />];
itemsEditarPago.splice(1, 0, { border : 0 });
itemsEditarPago.push({ 
						xtype      : 'button',
						itemId     : 'itemEditDomicil',
						icon      : '${ctx}/resources/fam3icons/icons/report_key.png',
						text       : 'Editar domicilio',
						arrowAlign : 'bottom',
						handler    : function(){
							debug('antes de _p25_ventanaCambioDomicilio');
							_p25_ventanaCambioDomicilio();
						} 
					});

/*itemsCondicionesColumns.push({ 
	xtype      : 'actioncolumn',
	itemId     : 'buttonEditarCondicion',
	icon       : '${ctx}/resources/fam3icons/icons/arrow_refresh.png',
	tooltip    : 'Actualizar condicion',
	arrowAlign : 'bottom',
	handler    : function(view,	rowIndex, colIndex, item, e, record){
		debug('actualizar condicion',record.data);
		actualizaCondicion(record);
	}
});*/

itemsCondicionesColumns.push({
	xtype      : 'actioncolumn',
	itemId     : 'buttonBorrarCondicion',
	icon       : '${ctx}/resources/fam3icons/icons/cross.png',
	tooltip    : 'Borrar condicion',
	arrowAlign : 'bottom',
	handler    : function(view,	rowIndex, colIndex, item, e, record){
		debug('borrar condicion');
		borraCondicion(record);
	}
});

itemsCalendarioColumns.push({ 
	xtype      : 'actioncolumn',
	itemId     : 'buttonEditarCalendario',
	icon       : '${ctx}/resources/fam3icons/icons/arrow_refresh.png',
	tooltip    : 'Actualizar',
	arrowAlign : 'bottom',
	handler    : function(view,	rowIndex, colIndex, item, e, record){
		debug('actualizar',record.data);		
		actualizaCalendario(record);
	}
});

itemsCalendarioColumns.push({
	xtype      : 'actioncolumn',
	itemId     : 'buttonBorrarCalendario',
	icon       : '${ctx}/resources/fam3icons/icons/cross.png',
	tooltip    : 'Borrar',
	arrowAlign : 'bottom',
	handler    : function(view,	rowIndex, colIndex, item, e, record){
		debug('borrar');
		borraCalendario(record);
	}
});

//itemsCondicionesRenovacion.splice(3, 0, { border : 0 });

itemsCondicionesRenovacion.splice(3, 0, { border : 0 });

itemsCondicionesRenovacion.splice(7,0, {
    xtype      : 'button',
    itemId     : 'buttonHelpValue',
    icon       : '${ctx}/resources/fam3icons/icons/help.png',
    tooltip    : 'Ayuda',
    arrowAlign : 'bottom',
    handler    : function(view, rowIndex, colIndex, item, e, record){
        debug('ayuda');
        panAyuda.show();
    }
});

itemsCondicionesRenovacion.push({ 
    xtype       : 'displayfield',
    itemId      : 'idValueExclu',
    allowBlank  : true,
    width       : 320 
});

itemsCondicionesRenovacion.push({ 
    xtype       : 'displayfield',
    itemId      : 'idValueExclu2',
    allowBlank  : true,
    width       : 320 
});
////// componentes dinamicos //////

Ext.onReady(function()
{
    Ext.Ajax.timeout = 60*60*1000;
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    ////// modelos //////
    Ext.define('_p25_modeloPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.itemsFormularioPolizaFields" escapeHtml="false" /> ]
    });
    
    Ext.define('_p25_modeloContratante',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.fieldsFormularioContratante" escapeHtml="false" /> ]
    });
    
    Ext.define('_p25_modeloPolizasMasivas',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.gridFields" escapeHtml="false" /> ]
    });
    
    Ext.define('_p25_modeloCondiciones',{
    	extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.itemsCondicionesFields" escapeHtml="false" /> ]
    });
    
    Ext.define('_p25_modeloCalendario',{
    	extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.itemsCalendarioFields" escapeHtml="false" /> ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p25_storePolizas = Ext.create('Ext.data.Store',
    {
        model     : '_p25_modeloPoliza'
        ,autoLoad : false
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p25_urlBuscarPolizas
            ,reader :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,messageProperty : 'respuesta'
                ,successProperty : 'success'
            }
        }
    });
    
    _p25_storePolizasMasivas = Ext.create('Ext.data.Store',
    {
        model     : '_p25_modeloPolizasMasivas'
        ,autoLoad : false
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p25_urlBuscarPolizasMasivas
            ,reader :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,messageProperty : 'respuesta'
            }
        }
    });
    
    _p25_storeContratantes = Ext.create('Ext.data.Store',
    {
        model     : '_p25_modeloContratante'
        ,autoLoad : false
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p25_urlBuscarContratantes
            ,reader :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,messageProperty : 'respuesta'
            }
        }
    });
    
    _p25_storeCondiciones = Ext.create('Ext.data.Store',
    	    {
    	        model     : '_p25_modeloCondiciones'
    	        ,autoLoad : false
    	        ,proxy    :
    	        {
    	            type    : 'ajax'
    	            ,url    : _p25_urlBuscarCondiciones
    	            ,reader :
    	            {
    	                type             : 'json'
    	                ,root            : 'slist1'
    	                ,messageProperty : 'respuesta'
    	            }
    	        }
    	    });
    	    
   	_p25_storeCalendario = Ext.create('Ext.data.Store',
    	    {
    	        model     : '_p25_modeloCalendario'
    	        ,autoLoad : false
    	        ,proxy    :
    	        {
    	            type    : 'ajax'
    	            ,url    : _p25_urlBuscarCalendario
    	            ,reader :
    	            {
    	                type             : 'json'
    	                ,root            : 'slist1'
    	                ,messageProperty : 'respuesta'
    	            }
    	        }
    	    });
    ////// stores //////
    
    ////// componentes //////
    winCambioDomicilio = Ext.create('Ext.window.Window', {
		title       : 'Autoservicio-Editar domicilio',
		itemId      : 'winCambioDomicilio',
    	layout      : 'fit',
    	closeAction : 'hide',
    	items       : [
    		Ext.create('Ext.form.Panel', {
    			bodyPadding : 10,
    			items       : itemsEditarDomicilio,
    			layout :
                {
                    type     : 'table',
                    columns : 2
                },
    			buttons	:	
    				[
    					{ 
    						text : 'Guardar',
    						handler : function(){
    							_fieldById('winCambioDomicilio').close();
    						}
    					},
    					{ 
    						text    : 'Cancelar',
    						handler : function(){
    							_fieldById('winCambioDomicilio').close();
    						}
    					}
    				]
    		})
        ]
    });
    
    winAutoServicio = Ext.create('Ext.window.Window', {
		title       : 'Auto-servicio',
		itemId      : 'winAutoServicio',
    	layout      : 'fit',
    	closeAction : 'hide',
    	items       : [
    		Ext.create('Ext.form.Panel', {
    			bodyPadding : 10,
    			items       : [
    			    {
    			    	xtype		: 'displayfield',
    			    	itemId      : 'dsTramite'
    			    },
    				{
    					xtype       : 'radiogroup',
    					itemId      : 'itemRadio',
    					//fieldLabel  : 'Se creo el tramite '+this.resRenova,
    					columns		: 1,
        				vertical	: false,
            			items       : [
                			{
                                boxLabel   : 'Mismas condiciones de renovacion',
                                name       : 'topping',
                                inputValue : '3',
                                itemId     : 'checkbox3',
                                checked    : true
                            },
                			{
                    			boxLabel   : 'Cambio en forma de pago y/o domicilio del contratante',
                    			name       : 'topping',
                    			inputValue : '1',
                    			itemId     : 'checkbox1'
                			},
                			{
                    			boxLabel   : 'Otros cambios',
                    			name       : 'topping',
                    			inputValue : '2',
                    			itemId     : 'checkbox2'
                			}                			
            			]
    				}	
    			],
    			buttons	:	
    				[
    					{ 
    						text    : 'Aceptar',
    						handler : function(){
    							var ntramite = this.up('window').resRenova['ntramite'];
    							if(_fieldById('itemRadio').getValue()['topping'] == 1){
    								_fieldById('winAutoServicio').close();
    								_p25_ventanaCambioFormaPago(this.up('window').resRenova);
    							}
    							else if(_fieldById('itemRadio').getValue()['topping'] == 2){
    							    turnar(ntramite);    		                        
    							}
    							else if(_fieldById('itemRadio').getValue()['topping'] == 3){
    							     debug('resRenova',this.up('window').resRenova);
    							     tarifaFinalMismasCondiciones(this.up('window').resRenova);
    							     /*var window = this.up('window');
    							     var resRenova = window.resRenova;
    							     _mask('Generando renovaci\u00f3n');
    							     Ext.Ajax.request({
                                         url      : _p25_urlConfirmarPolizaIndividual,
                                         params   : {
                                             'params.cdunieco' : resRenova['cdunieco'],
                                             'params.cdramo'   : resRenova['cdramo'],
                                             'params.estado'   : resRenova['estado'],
                                             'params.nmpoliza' : resRenova['nmpoliza'],
                                             'params.nmsuplem' : resRenova['nmsuplem'],
                                             'params.ntramite' : resRenova['ntramite'],
                                             'params.cdperpag' : resRenova['cdperpag'],
                                             'params.feefecto' : resRenova['feefecto']
                                         },
                                         success  : function(response){
                                             _unmask();
                                             var resp = Ext.decode(response.responseText);
                                             var list = resp.slist1;
                                             if(resp.success==true && list.length > 0){
                                                 window.close();
                                                 mensajeCorrecto('Aviso','Se gener\u00f3 la p\u00f3liza '+list[0]['nmpoliex']);
                                             }
                                             else{
                                                 mensajeError(resp.respuesta);
                                             }
                                         },
                                         failure  : function(){
                                             _unmask();
                                             errorComunicacion();
                                         }
                                     });*/
    						    }
    					    } 
    					},
    					{ 
    						text    : 'Cancelar',
    						handler : function(){
    							_fieldById('winAutoServicio').close();
    						}
    					}
    				]
    		})
        ]
    });
    
    winServicioAsistido = Ext.create('Ext.window.Window', {
		title       : 'Auto-servicio',
		itemId      : 'winServicioAsistido',
    	layout      : 'fit',
    	closeAction : 'hide',
    	items  : [
    		Ext.create('Ext.form.Panel', {
    			bodyPadding : 10,
    			items       : [
    				{
    					xtype       : 'fieldcontainer',
            			defaultType : 'checkboxfield',
            			layout :
            			{
                    		type     : 'table',
                    		columns : 1
                		},
            			items       : [
                			{
                    			boxLabel   : 'Cambio de ',
                    			name       : 'topping',
                    			inputValue : 'S',
                    			itemId     : 'checkbox1'
                			},
                			{
                    			boxLabel   : 'Modificar los datos del tramite',
                    			name       : 'topping',
                    			inputValue : 'M',
                    			itemId     : 'checkbox2'
                			}
            			]
    				}	
    			],
    			buttons	:	
    				[
    					{ 
    						text : 'Aceptar' 
    					},
    					{ 
    						text    : 'Cancelar',
    						handler : function(){
    							_fieldById('winServicioAsistido').close();
    						}
    					}
    				]
    		})
        ]
    });
    
    winCambioPago = Ext.create('Ext.window.Window', {
		title       : 'Autoservicio-Editar',
		itemId      : 'winCambioPago',
    	closeAction : 'hide',
    	layout      : 'fit',
    	items       : [
    		Ext.create('Ext.form.Panel', {
    			bodyPadding : 10,
    			items       : itemsEditarPago,
    			layout :
                {
                    type     : 'table',
                    columns  : 2
                },
    			buttons	:	
    				[
    					{ 
    						text : 'Guardar' 
    					},
    					{ 
    						text    : 'Cancelar',
    						handler : function(){
    							_fieldById('winCambioPago').close();
    						}
    					}
    				]
    		})
        ]
    });
    
    _p25_clientePanel = Ext.create('Ext.panel.Panel',{
		itemId     : '_p25_clientePanel',
		title      : 'Cambio de domicilio del contratante',
		height     : 400,
		autoScroll : true,
		loader     :
				{
    				url       	: _p25_urlPantallaCliente,
    				scripts  	: true,
    				autoLoad 	: false,
    				ajaxOptions	: {
                    	method: 'POST'
                 	},
                 	params:	{
						//'smap1.cdperson' 			: this.resRenova['cdperson'],//_fieldById('winCambioPago').resRenova['cdperson'],	//_fieldByName('cdperson',form).getValue(),
    					'smap1.cdideper' 			: '',	//json.smap1.cdideper,
    					'smap1.cdideext' 			: '',	//json.smap1.cdideext,
    					'smap1.esSaludDanios' 		: 'S',
    					'smap1.esCargaClienteNvo' 	: 'N',	//(Ext.isEmpty(json.smap1.cdperson)? 'S' : 'N' ),
    					'smap1.ocultaBusqueda' 		: 'N',
    					'smap1.cargaCP' 			: '',	//json.smap1.cdpostal,
    					'smap1.cargaTipoPersona' 	: '',	//json.smap1.otfisjur,
    					'smap1.cargaSucursalEmi' 	: '',	//_fieldByName('cdunieco',form).getValue(),
    					'smap1.activaCveFamiliar'	: 'N',
						'smap1.modoRecuperaDanios'	: 'N',
						//'smap1.modoSoloEdicion'		: 'S',
						'smap1.modoEdicionDomicilio': 'S'
					}
				}
	});
    
    wineditarContratante = Ext.create('Ext.window.Window', {
		title       : 'Autoservicio-Editar',
		itemId      : 'winCambioPago',
    	closeAction : 'hide',
    	layout      : 'fit',
    	width		: 880,
    	height      : 550,
    	autoScroll  : 'true',
    	items		: [
			Ext.create('Ext.tab.Panel', {
    			width		: 300,
    			height		: 200,
    			activeTab	: 0,
    			items: [
					Ext.create('Ext.panel.Panel',{
						itemId     : '_p25_clientePanel',
						title      : 'Contratante',
						height     : 400,
						autoScroll : true,
						items	   : [
							/*Ext.create('Ext.Button', {
							    text		: 'Editar contratante',
							    handler		: function() {
							    	_p25_loaderContratante(this.up('window').resRenova['cdpersoncon']);
							    }
							}),
							Ext.create('Ext.Button', {
							    text		: 'Cambiar contratante',
							    handler		: function() {
							    	_p25_loaderContratante('');
							    }
							}),*/
							_p25_clientePanel							
						]
					}),
					Ext.create('Ext.panel.Panel',{
						title 	    : 'Cambio forma de pago / emitir',
				        cls		    : 'claseTitulo',
				        itemId 	    : 'panelDatos',
				        url         : urlGuardar,
				        buttonAlign : 'center',
				        autoScroll  : true,
				        border		: 0,
				        items		: [
				            Ext.create('Ext.panel.Panel',{
				                itemId			: 'panelDatosGenerales',
				                title			: 'Datos generales de la poliza',
				                style			: 'margin:5px',
				                frame			: false,
				                collapsible		: true,
				                titleCollapse	: true,
				                layout			: {
				                    type	: 'table',
				                    columns : 3
				                },
				                items:[
				                    Ext.create('Ext.form.TextField', {
				                        itemId		: 'companiaAseguradora',//id3
				                        name		: 'dsunieco',
				                        fieldLabel	: 'Sucursal',
				                        readOnly	: true,
				                        style		: 'margin:5px;'
				                    }),
				                    Ext.create('Ext.form.TextField', {
				                    	itemId		: 'agenteVentas',
				                        name		: 'nombre',
				                        fieldLabel	: 'Agente',		                            
				                        readOnly	: true,
				                        style		:'margin:5px;'
				                    }),
				                    Ext.create('Ext.form.TextField', {
				                    	itemId		: 'producto',
				                        name		: 'dsramo',
				                        fieldLabel	: 'Producto',		                                 
				                        readOnly	: true,
				                        style		: 'margin:5px;'
				                    })
				                ]
				            }),
				            Ext.create('Ext.panel.Panel',{
				            	itemId			: 'panelDatosPoliza',//id5
			                    title			: 'Datos generales',
			                    style			: 'margin:5px',
			                    frame			:  false,
			                    collapsible		: true,
			                    titleCollapse	: true,
			                    layout			: {
			                        type	: 'table',
			                        columns	: 3
			                    },
			                    items:[
			                        {
			                            xtype		: 'textfield',
			                            itemId		: 'poliza',//id6
			                            name		: 'nmpoliza',
			                            readOnly	: true,
			                            fieldLabel	: 'Poliza',
			                            style		: 'margin:5px;',
			                            hidden		: true
			                        },
			                        {                        
			                        	xtype		: 'textfield',     
	                                    readOnly	: true,       
	                                    fieldLabel	: 'Poliza', 
	                                    value		: '0',           
	                                    style		:' margin:5px;',
                                        hidden      : true 
			                        },                       
			                        {
			                            xtype			: 'combo',
			                            itemId			: 'estadoPoliza',//id8
			                            name			: 'estado',
			                            fieldLabel		: 'Estado',
			                            displayField	: 'value',
			                            valueField		: 'key',
			                            readOnly		: true,
                                        hidden          : true,
			                            store			: Ext.create('Ext.data.Store', {
			                                model		: 'Generic',
			                                autoLoad	: true,
			                                proxy		:
			                                {
			                                    type		: 'ajax',
			                                    url			: urlCargarCatalogos,
			                                    extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@STATUS_POLIZA"/>'},
			                                    reader		:
			                                    {
			                                        type	: 'json',
			                                        root	: 'lista'
			                                    }
			                                }
			                            }),
			                            editable	: false,
			                            queryMode	: 'local',
			                            style		: 'margin:5px;',
			                            allowBlank	: false
			                        },
			                        {
			                            xtype		: 'numberfield',
			                            itemId		: 'solicitud',
			                            name		: 'nmsolici',
			                            fieldLabel	: 'Cotizaci&oacute;n',
			                            style		: 'margin:5px;',
			                            allowBlank	: false,
			                            readOnly	: true,
                                        hidden      : true
			                        },
			                        {
			                            xtype		: 'datefield',
			                            itemId		: 'fechaSolicitud',
			                            name		: 'fesolici',
			                            fieldLabel	: 'Fecha de solicitud',
			                            allowBlank	: false,
			                            style		: 'margin:5px;',
			                            format		: 'd/m/Y',
			                            readOnly	: true,
                                        hidden      : true
			                        },
			                        {
			                            xtype		: 'datefield',
			                            itemId		: 'fechaEfectividad',//id11
			                            name		: 'feefecto',
			                            fieldLabel	: 'Fecha de inicio de vigencia',
			                            allowBlank	: false,
			                            style		: 'margin:5px;',
			                            format		: 'd/m/Y',
			                            /* listeners	: {
			                                change  : panDatComUpdateFerenova
			                            }, */
			                            minValue : panDatComMap1.fechaMinEmi,
	                                    maxValue : panDatComMap1.fechaMaxEmi,
	                                    readOnly : true,
                                        hidden   : true
			                        },
			                        {
			                            xtype		: 'datefield',
			                            itemId		: 'fechaRenovacion',//id14
			                            name		: 'feproren',
			                            fieldLabel	: 'Fecha de t&eacute;rmino de vigencia',
			                            allowBlank	: false,
			                            style		: 'margin:5px;',
			                            format		: 'd/m/Y',
			                            readOnly	: true,
                                        hidden      : true
			                        },
			                        {
			                            xtype			: 'combo',
			                            itemId			: 'tipoPoliza',//id12
			                            name			: 'ottempot',
			                            fieldLabel		: 'Tipo de poliza',
			                            displayField	: 'value',
			                            valueField		: 'key',
			                            readOnly	    : true,
			                            store			: Ext.create('Ext.data.Store', {
			                                model	 : 'Generic',
			                                //autoLoad : true,
			                                proxy	 :
			                                {
			                                    type		: 'ajax',
			                                    url			: urlCargarCatalogos,
			                                    /* <s:if test='%{getMap1().get("SITUACION").equals("AUTO")}'>
			                                    extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_POLIZA_AUTO"/>'},
			                                    </s:if>
			                                    <s:else> */
			                                    extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_POLIZA"/>'},
			                                    /* </s:else> */
			                                    reader:
			                                    {
			                                        type: 'json',
			                                        root: 'lista'
			                                    }
			                                }
			                            }),
			                            editable	: false,
			                            queryMode	: 'local',
			                            style		: 'margin:5px;',
			                            allowBlank	: false,
                                        hidden      : true
			                        },
			                        {
			                            xtype			: 'combo',
			                            itemId			: 'formaPagoPoliza',//id15
			                            name			: 'cdperpag',
			                            fieldLabel		: 'Forma de pago',
			                            displayField	: 'value',
			                            valueField		: 'key',
			                            store			: Ext.create('Ext.data.Store', {
			                                model		: 'Generic',
			                                //autoLoad	: true,
			                                proxy		: {
			                                    type		: 'ajax',
			                                    url			: urlCargarCatalogos,
			                                    /* extraParams : {
			                                        catalogo           : 'FORMAS_PAGO_POLIZA_POR_RAMO_TIPSIT',
			                                        'params.cdramo'    : panDatComMap1.cdramo,
			                                        'params.cdtipsit'  : panDatComMap1.cdtipsit
			                                    }, */
			                                    reader:
			                                    {
			                                        type: 'json',
			                                        root: 'lista'
			                                    }
			                                }
			                            }),
			                            editable	: false,
			                            queryMode	: 'local',
			                            style		: 'margin:5px;',
			                            allowBlank	: false,
			                            readOnly 	: Number(panDatComMap1.cdramo)==16,
                                        hidden      : false
			                        },
			                        {
	                                    xtype	 	: 'textfield',
	                                    name	  	: 'dsplan',
	                                    readOnly  	: true,
	                                    fieldLabel	: 'Plan',
	                                    style		: 'margin:5px;',
                                        hidden      : true
	                                },
			                        {
	                                    xtype		: 'numberfield',
	                                    name		: 'nmrenova',
	                                    allowBlank	: false,
	                                    maxValue	: 99,
	        							minValue	: 0,
	                                    value		: 0,
	                                    fieldLabel	: 'N&uacute;mero Renovaci&oacute;n',
	                                    style		: 'margin:5px;',
	                                    readOnly	: true,
                                        hidden      : true
	                                },
			                        {
	                                    xtype		: 'textfield',
	                                    name		: 'nmpolant',
	                                    fieldLabel	: 'P&oacute;liza Anterior',
	                                    style		: 'margin:5px;',
	                                    readOnly	: true,
                                        hidden      : true
	                                }
	                                ,{
	                                    xtype           : 'combo',
	                                    itemId         : '_panDatCom_nmcuadroCmp',
	                                    fieldLabel     : 'Cuadro de comisiones',
	                                    name           : 'nmcuadro',
	                                    style          : 'margin:5px;',
	                                    forceSelection : true,
	                                    valueField     : 'key',
	                                    displayField   : 'value',       
	                                    editable       : true,
	                                    queryMode      : 'local',
	                                    disabled       : panDatComMap1.cambioCuadro != 'S',
	                                    hidden         : panDatComMap1.cambioCuadro!='S',
	                                    allowBlank     : false,
	                                    readOnly	   : true,
	                                    store          :
	                                    Ext.create('Ext.data.Store',
	                                    {
	                                        model     	: 'Generic',
	                                        autoLoad 	: true,
	                                        proxy    	:
	                                        {
	                                           type         : 'ajax',
	                                           url         	: urlCargarCatalogos,
	                                           extraParams 	:
	                                           {
	                                               catalogo          : 'CUADROS_POR_SITUACION',
	                                               'params.cdtipsit' : panDatComMap1.cdtipsit
	                                           },
	                                           reader      :
	                                           {
	                                               type  : 'json',
	                                               root  : 'lista'
	                                           }
	                                        }
	                                    })
	                                }
			                    ]
			                })/*,
			                Ext.create('Ext.panel.Panel',{
			                	itemId			: 'panelDatosAdicionales',
			                    title			: 'Datos adicionales',
			                    style			: 'margin:5px',
			                    collapsible		: true,
			                    titleCollapse	: true,
			                    autoScroll		: true,
			                    maxHeight		: 300,
			                    layout			: {
			                        type	: 'table',
			                        columns	: 3
			                    },
			                    items 			: itemsTatrisit
			                })*/
				        ],
				        buttons:	[
	                        {
	                            text	: 'Guardar',
	                            itemId  : 'panDatComBotonGuardar',
	                            icon	: contexto+'/resources/fam3icons/icons/accept.png',
	                            handler	: function(){ _p29_actualizarCotizacion(null);}	
	                        },
                            {
	                            text		: 'Editar agentes',
	                            icon		: contexto+'/resources/fam3icons/icons/user_gray.png',
	                            disabled	: true,
	                            hidden		: true
	                        },
	                        {
	                            text		: 'Editar documentos',
	                            icon		: contexto+'/resources/fam3icons/icons/book.png',
	                            disabled	: true,
	                            hidden		: true
	                        },
	                        {
	                            text    : ['COTIZADOR', 'SUPTECSALUD'].indexOf(sesionDsrol) != -1 ? 'Cotizar' : 'Emitir',
                                   itemId  : 'panDatComBotonRetarificar',
                                   icon    : contexto+'/resources/fam3icons/icons/key.png',
                                   hidden  : /*panDatComMap1.SITUACION !== 'AUTO' &&*/ (['SUSCRIPTOR', 'COTIZADOR', 'SUPTECSALUD'].indexOf(sesionDsrol) === -1),
                                   handler	: function(){
                                	   _p29_actualizarCotizacion(null);
                                	   var winCambioPago = _fieldById('winCambioPago');                                	   
                                	   tarifaFinal();
                                	}
                                 },
	                        {
                                   text    : 'Guardar y dar Vo. Bo.',
                                   icon    : '${ctx}/resources/fam3icons/icons/heart_add.png',
                                   hidden  : ((!sesionDsrol)||sesionDsrol!='MEDICO'),//||!Ext.isEmpty(panDatComFlujo),
                                   handler : function()
                                   {
                                   	var form=Ext.getCmp('formPanel');                                         
                                       if(form.isValid()) {           
                                   	
                                    	Ext.create('Ext.window.Window',
                                        {
                                            title       : 'Dictamen para mesa de control',
                                            width       : 600,
                                            height      : 430,
                                            buttonAlign : 'center',
                                            modal       : true,
                                            closable    : false,
                                            autoScroll  : true,
                                            items       :
                                            [
												{
												    itemId : 'inputTextareaCommentsToMCFromMedico',
											    	width  : 570,
                                                    height : 300,
                                                    xtype  : 'textarea',
												},
												{
									                xtype      : 'radiogroup',
									                fieldLabel : 'Mostrar al agente',
									                columns    : 2,
									                width      : 250,
									                style      : 'margin:5px;',
									                hidden     : _GLOBAL_CDSISROL===RolSistema.Agente,
									                items      :
									                [
									                    {
									                        boxLabel   : 'Si',
									                        itemId     : 'SWAGENTE',
									                        name       : 'SWAGENTE',
									                        inputValue : 'S',
									                        checked    : _GLOBAL_CDSISROL===RolSistema.Agente
									                    },
									                    {
									                        boxLabel   : 'No',
									                        name       : 'SWAGENTE',
									                        inputValue : 'N',
                                                               checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
									                    }
									                ]
									            }
                                            ]
                                            ,buttons    :
                                            [
                                                {
                                                    text    : 'Guardar y dar Vo. Bo.',
                                                    icon    : '${ctx}/resources/fam3icons/icons/heart_add.png',
                                                    handler : function()
                                                    {
				                                        var form=Ext.getCmp('formPanel');
				                                        var window=this.up().up();
				                                        	//window.setLoading(true);
				                                            form.submit({
				                                                params:{
				                                                    'map1.pv_cdunieco' :  panDatComMap1.cdunieco,
				                                                    'map1.pv_cdramo'   :  panDatComMap1.cdramo,
				                                                    'map1.pv_estado'   :  panDatComMap1.estado,
				                                                    'map1.pv_nmpoliza' :  panDatComMap1.nmpoliza
				                                                },
				                                                success:function(){
				                                                    Ext.Ajax.request
				                                                    ({
				                                                        url     : datComUrlMCUpdateStatus
				                                                        ,params : 
				                                                        {
				                                                            'smap1.ntramite'  : panDatComMap1.ntramite,
				                                                            'smap1.status'    : '5',//Vo.Bo.Medico
				                                                            'smap1.comments'  : Ext.getCmp('inputTextareaCommentsToMCFromMedico').getValue(),
				                                                            'smap1.swagente'  : _fieldById('SWAGENTE').getGroupValue()
				                                                        }
				                                                        ,success : function(response)
				                                                        {
				                                                            var json=Ext.decode(response.responseText);
				                                                            if(json.success==true)
				                                                            {
				                                                                Ext.create('Ext.form.Panel').submit
				                                                                ({
				                                                                    url             : datComUrlMC
				                                                                    ,standardSubmit : true
				                                                                    ,params         :
                                                                                    {
                                                                                        'smap1.gridTitle'	  : 'Tareas',
                                                                                        'smap2.pv_cdtiptra_i' : 1,
                                                                                        'smap1.editable'	  : 1
                                                                                    }
				                                                                });
				                                                            }
				                                                            else
				                                                            {
				                                                            	//window.setLoading(false);
				                                                                Ext.Msg.show({
				                                                                    title	: 'Error',
				                                                                    msg		: 'Error al guardar Vo. Bo.',
				                                                                    buttons	: Ext.Msg.OK,
				                                                                    icon	: Ext.Msg.ERROR
				                                                                });
				                                                            }
				                                                        }
				                                                        ,failure : function()
				                                                        {
				                                                        	//window.setLoading(false);
				                                                            Ext.Msg.show({
				                                                                title	: 'Error',
				                                                                msg		: 'Error de comunicaci&oacute;n',
				                                                                buttons	: Ext.Msg.OK,
				                                                                icon	: Ext.Msg.ERROR
				                                                            });
				                                                        }
				                                                    });
				                                                },
				                                                failure:function(){
				                                                	//window.setLoading(false);
				                                                    Ext.Msg.show({
				                                                        title	: 'Error',
				                                                        msg		: 'Error de comunicaci&oacute;n',
				                                                        buttons	: Ext.Msg.OK,
				                                                        icon	: Ext.Msg.ERROR
				                                                    });
				                                                }
				                                            });
                                                    }
                                                }
                                                ,
                                                {
                                                	text  	: 'Cancelar',
                                                    icon 	: '${ctx}/resources/fam3icons/icons/cancel.png',
                                                    handler : function()
                                                    {
                                                        this.up().up().destroy();
                                                    }
                                                }
                                            ]
                                        }).show();
                                       } else {
                                           Ext.Msg.show({
                                               title	: 'Datos incompletos',
                                               msg		: 'Favor de introducir todos los campos requeridos',
                                               buttons : Ext.Msg.OK,
                                               icon	: Ext.Msg.WARNING
                                           });
                                       }
                                   }
                               },
	                        {
                                   text    : 'Guardar como pendiente de informaci&oacute;n',
                                   icon    : '${ctx}/resources/fam3icons/icons/clock.png',
                                   hidden  : ((!sesionDsrol)||sesionDsrol!='MEDICO'),//||!Ext.isEmpty(panDatComFlujo),
                                   handler : function()
                                   {
                                   	var form=Ext.getCmp('formPanel');                                         
                                       if(form.isValid()) {                                        	
                                           Ext.create('Ext.window.Window',
                                           {
                                               title        : 'Dictamen para mesa de control'
                                               ,width       : 600
                                               ,height      : 430
                                               ,buttonAlign : 'center'
                                               ,modal       : true
                                               ,closable    : false
                                               ,autoScroll  : true
                                               ,items       :
                                               [
                                                   {
                                                       itemId : 'inputTextareaCommentsToMCFromMedico',
                                                       width  : 570,
                                                       height : 300,
                                                       xtype  : 'textarea'
                                                   }
                                                   ,{
									                xtype      : 'radiogroup',
									                fieldLabel : 'Mostrar al agente',
									                columns    : 2,
									                width      : 250,
									                style      : 'margin:5px;',
									                hidden     : _GLOBAL_CDSISROL===RolSistema.Agente,
									                items      :
									                [
									                    {
									                        boxLabel   : 'Si',
									                        itemId     : 'SWAGENTE',
									                        name       : 'SWAGENTE',
									                        inputValue : 'S',
									                        checked    : _GLOBAL_CDSISROL===RolSistema.Agente
									                    }
									                    ,{
									                        boxLabel   : 'No',
									                        name       : 'SWAGENTE',
									                        inputValue : 'N',
                                                               checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
									                    }
									                ]
									            }
                                               ]
                                               ,buttons    :
                                               [
                                                   {
                                                   	text    : 'Guardar como pendiente de informaci&oacute;n',
                                                       icon    : '${ctx}/resources/fam3icons/icons/clock.png',
                                                       handler : function()
                                                       {
                                                           var form=Ext.getCmp('formPanel');
                                                           var window=this.up().up();
                                                           //console.log(form.getValues());
                                                          
                                                               //window.setLoading(true);
                                                               form.submit({
                                                                   params:{
                                                                       'map1.pv_cdunieco' :  panDatComMap1.cdunieco,
                                                                       'map1.pv_cdramo'   :  panDatComMap1.cdramo,
                                                                       'map1.pv_estado'   :  panDatComMap1.estado,
                                                                       'map1.pv_nmpoliza' :  panDatComMap1.nmpoliza
                                                                   },
                                                                   success:function(){
                                                                       Ext.Ajax.request
                                                                       ({
                                                                           url     : datComUrlMCUpdateStatus
                                                                           ,params : 
                                                                           {
                                                                               'smap1.ntramite'  : panDatComMap1.ntramite,
                                                                               'smap1.status'    : '6',
                                                                               'smap1.comments'  : Ext.getCmp('inputTextareaCommentsToMCFromMedico').getValue(),
                                                                               'smap1.swagente'  : _fieldById('SWAGENTE').getGroupValue() 
                                                                           }
                                                                           ,success : function(response)
                                                                           {
                                                                               var json=Ext.decode(response.responseText);
                                                                               if(json.success==true)
                                                                               {
                                                                                   Ext.create('Ext.form.Panel').submit
                                                                                   ({
                                                                                       url             : datComUrlMC
                                                                                       ,standardSubmit : true
                                                                                       ,params         :
                                                                                       {
                                                                                           'smap1.gridTitle'	  : 'Tareas',
                                                                                           'smap2.pv_cdtiptra_i' : 1,
                                                                                           'smap1.editable'	  : 1
                                                                                       }
                                                                                   });
                                                                               }
                                                                               else
                                                                               {
                                                                                   //window.setLoading(false);
                                                                                   Ext.Msg.show({
                                                                                       title	: 'Error',
                                                                                       msg		: 'Error al guardar Vo. Bo.',
                                                                                       buttons	: Ext.Msg.OK,
                                                                                       icon	: Ext.Msg.ERROR
                                                                                   });
                                                                               }
                                                                           }
                                                                           ,failure : function()
                                                                           {
                                                                               //window.setLoading(false);
                                                                               Ext.Msg.show({
                                                                                   title	: 'Error',
                                                                                   msg		: 'Error de comunicaci&oacute;n',
                                                                                   buttons	: Ext.Msg.OK,
                                                                                   icon	: Ext.Msg.ERROR
                                                                               });
                                                                           }
                                                                       });
                                                                   },
                                                                   failure:function(){
                                                                       //window.setLoading(false);
                                                                       Ext.Msg.show({
                                                                           title	: 'Error',
                                                                           msg		: 'Error de comunicaci&oacute;n',
                                                                           buttons	: Ext.Msg.OK,
                                                                           icon	: Ext.Msg.ERROR
                                                                       });
                                                                   }
                                                               });
                                                           
                                                         
                                                       }
                                                   }
                                                   ,
                                                   {
                                                       text  : 'Cancelar'
                                                       ,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
                                                       ,handler : function()
                                                       {
                                                           this.up().up().destroy();
                                                       }
                                                   }
                                               ]
                                           }).show();
                                       } else {                                        	    
                                               Ext.Msg.show({                                                	
                                                   title	: 'Datos incompletos',
                                                   msg		: 'Favor de introducir todos los campos requeridos',
                                                   buttons	: Ext.Msg.OK,
                                                   icon	: Ext.Msg.WARNING
                                               });
                                         }
                                   }
                               }				                	 
		            		]
					})
    			]
			})
		]
    });    
    
    panDatComVentanaTarifaFinal = Ext.create('Ext.window.Window',{
        title: 'Tarifa final',
    	itemId: 'panDatComVentanaTarifaFinal',
    	autoScroll:true,
    	width: 660,
    	height: 400,
    	defaults: {
    		width: 650
    	},
    	modal:false,
    	closable:false,
    	collapsible:true,
    	titleCollapse:true,
    	items:[
    		Ext.create('Ext.grid.Panel',{
    		width : 600
    			,store:Ext.create('Ext.data.Store',{
    				model:'ModeloDetalleCotizacion',
    				groupField: 'orden_parentesco',
    				sorters: [{
    					sorterFn: function(o1, o2){
    						if (o1.get('orden') === o2.get('orden'))
    						{
    							return 0;
    						}
    						return o1.get('orden') < o2.get('orden') ? -1 : 1;
    					}
    				}],
    				proxy: {
    					type: 'memory',
    					reader: 'json'
    				}//,
    				//data:json.slist1
    			}),
    			columns:
    			[
    				{
    					header    : 'Nombre de la cobertura',
    					dataIndex : 'Nombre_garantia',
    					flex      : 2,
    					summaryType: 'count',
    					summaryRenderer: function(value){
    						return Ext.String.format('Total de {0} cobertura{1}', value, value !== 1 ? 's' : '');
    					}
    				},
    				{
    					header      : 'Importe por cobertura',
    					dataIndex   : 'Importe',
    					flex        : 1,
    					renderer    : Ext.util.Format.usMoney,
    					align       : 'right',
    					summaryType : 'sum'
    				}
    			],
    			features: [{
    				groupHeaderTpl:
    					[
    						'{name:this.formatName}',
    						{
    							formatName:function(name)
    							{
    								return name.split("_")[1];
    							}
    						}
    					],
    				ftype:'groupingsummary',
    				startCollapsed :true
    			}]
    		})
    		,Ext.create('Ext.toolbar.Toolbar',{
    			buttonAlign:'right'
    			,items:
    			[
    				'->'
    				,Ext.create('Ext.form.Label',
    				{
    					style:'color:white;'
    					,initComponent:function()
    					{
    						var sum=0;
    						/* for(var i=0;i<json.slist1.length;i++)
    						{
    							sum+=parseFloat(json.slist1[i].Importe);
    						} */
    						this.setText('Total: '+Ext.util.Format.usMoney(sum));
    						this.callParent();
    					}
    				})
    			]
    		})
    		,Ext.create('Ext.form.Panel',
    		{
    			layout:
    			{
    				type    : 'table',
    				columns : 5
    			}
    			,defaults:
    			{
    				style : 'margin:5px;'
    			}
    			,items:
    			[
    				/*{
    					xtype : 'textfield'
    					,itemId   : 'numerofinalpoliza'
    					,fieldLabel : 'N&uacute;mero de poliza'
    					,readOnly   : true
    				}
    				,*/
    				{
    					id      : 'botonEmitirPolizaFinal'
    					,xtype  : 'button'
    					,text   : 'Emitir'
    					,hidden : panDatComMap1.SITUACION !== 'AUTO' && ('SUSCRIPTOR' !== sesionDsrol)
    					,icon   : contexto+'/resources/fam3icons/icons/award_star_gold_3.png'
    					,handler:function()
    					{
    						var me=this;
    						//me.up().up().setLoading(true);
    						Ext.Ajax.request(
    						{
    							url     : urlEmitir
    							,params :
    							{
    								'panel1.pv_nmpoliza'  : panDatComMap1.nmpoliza
    								,'panel1.pv_ntramite' : panDatComMap1.ntramite
    								,'panel2.pv_cdramo'   : panDatComMap1.cdramo
    								,'panel2.pv_cdunieco' : panDatComMap1.cdunieco
    								,'panel2.pv_estado'   : panDatComMap1.estado
    								,'panel2.pv_nmpoliza' : panDatComMap1.nmpoliza
    								,'panel2.pv_cdtipsit' : panDatComMap1.cdtipsit
    							}
    							,success:function(response)
    							{
    								//me.up().up().setLoading(false);
    								//var json = Ext.decode(response.responseText);
    								//debug(json);
    								//if(json.success==true)
    								//{
    									//datComPolizaMaestra=json.panel2.nmpoliza;
    									//debug("datComPolizaMaestra",datComPolizaMaestra);
    									//_numeroPolizaExt = json.panel2.nmpoliex;
    									/*if(json.retryRec){
    										
    										Ext.getCmp('botonEmitirPolizaFinal').hide();
    										Ext.getCmp('botonEmitirPolizaFinalPreview').hide();
    										Ext.getCmp('botonImprimirPolizaFinal').setDisabled(false);
    										Ext.getCmp('botonPagar').setDisabled(false);
    										
    										Ext.Msg.show({
    											title    :'Aviso'
    											,msg     : 'Error en la emisi&oacute;n. No se pudo emitir la p&oacute;liza.'
    											,buttons : Ext.Msg.OK
    											,icon    : Ext.Msg.WARNING
    											,fn      : function(){
    												if(''+json.panel1.necesitaAutorizacion=='S')
    												{
    													Ext.create('Ext.form.Panel').submit(
    													{
    														url     : datComUrlMC
    														,params :
    														{
    															'smap1.gridTitle'     : 'Tareas',
    															'smap2.pv_cdtiptra_i' : 1,
    															'smap1.editable'      : 1
    														}
    														,standardSubmit : true
    													});
    												}
    												var paramsWS = {
    														'panel1.pv_nmpoliza'  : panDatComMap1.nmpoliza
    														,'panel1.pv_ntramite' : panDatComMap1.ntramite
    														,'panel2.pv_cdramo'   : panDatComMap1.cdramo
    														,'panel2.pv_cdunieco' : panDatComMap1.cdunieco
    														,'panel2.pv_estado'   : panDatComMap1.estado
    														,'panel2.pv_nmpoliza' : panDatComMap1.nmpoliza
    														,'panel2.pv_cdtipsit' : panDatComMap1.cdtipsit
    														,'nmpoliza'           : json.nmpoliza
    														,'nmsuplem'           : json.nmsuplem
    														,'cdIdeper'           : json.cdIdeper
    														,'nmpolAlt'           : json.nmpolAlt
    														,'sucursalGS'         : json.sucursalGS
    														,'cdRamoGS'           : json.cdRamoGS
    														,'retryRec'           : json.retryRec
    													};
    												reintentarWSAuto(me.up().up(), paramsWS);
    											}
    										});
    										return;
    									}*/
    									
    									/*Ext.getCmp('numerofinalpoliza').setValue(json.panel2.nmpoliex);
    									
    									Ext.getCmp('botonEmitirPolizaFinal').hide();
    									Ext.getCmp('botonEmitirPolizaFinalPreview').hide();
    									Ext.getCmp('botonImprimirPolizaFinal').setDisabled(false);
    									Ext.getCmp('botonPagar').setDisabled(false);
    									
    									Ext.getCmp('botonReenvioWS').hide();
    									
    									if(panDatComMap1.SITUACION == 'AUTO'){
    										_mensajeEmail = json.mensajeEmail;
    										//debug("Mensaje Mail: " + _mensajeEmail);
    										Ext.getCmp('botonEnvioEmail').enable();
    									}else {
    										Ext.getCmp('botonEnvioEmail').hide();
    									}
    									
    									if(panDatComMap1.SITUACION=='AUTO' && Ext.isEmpty(panDatComFlujo)){
    										Ext.getCmp('venDocVenEmiBotIrCotiza').show();
    									}
    									Ext.getCmp('venDocVenEmiBotMesa').show();
    									Ext.getCmp('venDocVenEmiBotCancelar').setDisabled(true);*/
    									/*if(json.mensajeRespuesta&&json.mensajeRespuesta.length>0)
    									{
    										var ventanaTmp = Ext.Msg.show({
    											title:'Aviso del sistema',
    											msg: json.mensajeRespuesta,
    											buttons: Ext.Msg.OK,
    											icon: Ext.Msg.WARNING,
    											fn: function(){
    												if(!Ext.isEmpty(json.nmpolAlt)){
    													mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
    												}
    											}
    										});
    										centrarVentanaInterna(ventanaTmp);
    									}else { 
    										if(!Ext.isEmpty(json.nmpolAlt)){
    											mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
    										}
    									}*/
    								//}
    								//else
    								//{
    									/*if(json.retryWS){
    										datComPolizaMaestra=json.panel2.nmpoliza;
    										debug("datComPolizaMaestra",datComPolizaMaestra);
    										
    										Ext.getCmp('botonEmitirPolizaFinal').hide();
    										Ext.getCmp('botonEmitirPolizaFinalPreview').hide();
    										
    										if(panDatComMap1.SITUACION=='AUTO' && Ext.isEmpty(panDatComFlujo))
    										{
    											Ext.getCmp('venDocVenEmiBotIrCotiza').show();
    										}
    										Ext.getCmp('venDocVenEmiBotMesa').show();
    										Ext.getCmp('venDocVenEmiBotCancelar').setDisabled(true);
    									}*/
    									/*Ext.Msg.show({
    										title    :'Aviso'
    										,msg     : json.mensajeRespuesta
    										,buttons : Ext.Msg.OK
    										,icon    : Ext.Msg.WARNING
    										,fn      : function(){
    											if(''+json.panel1.necesitaAutorizacion=='S')
    											{
    												Ext.create('Ext.form.Panel').submit(
    												{
    													url     : datComUrlMC
    													,params :
    													{
    														'smap1.gridTitle'     : 'Tareas',
    														'smap2.pv_cdtiptra_i' : 1,
    														'smap1.editable'      : 1
    													}
    													,standardSubmit : true
    												});
    											}
    											if(json.retryWS){
    												var paramsWS = {
    														'panel1.pv_nmpoliza'  : panDatComMap1.nmpoliza
    														,'panel1.pv_ntramite' : panDatComMap1.ntramite
    														,'panel2.pv_cdramo'   : panDatComMap1.cdramo
    														,'panel2.pv_cdunieco' : panDatComMap1.cdunieco
    														,'panel2.pv_estado'   : panDatComMap1.estado
    														,'panel2.pv_nmpoliza' : panDatComMap1.nmpoliza
    														,'panel2.pv_cdtipsit' : panDatComMap1.cdtipsit
    														,'nmpoliza'           : json.nmpoliza
    														,'nmsuplem'           : json.nmsuplem
    														,'cdIdeper'           : json.cdIdeper
    													};
    												reintentarWSAuto(me.up().up(), paramsWS);
    											}
    										}
    									});*/
    								//}
    							}
    							,failure:function()
    							{
    								//me.up().up().setLoading(false);
    								Ext.Msg.show({
    									title:'Error',
    									msg: 'Error de comunicaci&oacute;n',
    									buttons: Ext.Msg.OK,
    									icon: Ext.Msg.ERROR
    								});
    							}
    						});
    					}
    				}
    				,{
    					id     : 'botonEnvioEmail'
    					,xtype : 'button'
    					,text  : 'Enviar Email'
    					,icon  : contexto+'/resources/fam3icons/icons/email.png'
    					,disabled: true
    					,hidden: (panDatComMap1.SITUACION != 'AUTO') ? true: false
    					/* ,handler:function()
    					{
    						Ext.Msg.prompt('Envio de Email', 'Escriba los correos que recibir&aacute;n la documentaci&oacute;n (separados por ;)', 
    						function(buttonId, text){
    							if(buttonId == "ok" && !Ext.isEmpty(text)){
    								
    								if(Ext.isEmpty(_mensajeEmail)){
    									mensajeError('Mensaje de Email sin contenido. Consulte a Soporte T&eacute;cnico');
    									return;
    								}
    								
    								Ext.Ajax.request(
    										{
    											url : _urlEnviarCorreo,
    											params :
    											{
    												to     : text,
    												asunto : 'Documentación de póliza de Autos',
    												mensaje: _mensajeEmail,
    												html   : true
    											},
    											callback : function(options,success,response)
    											{
    												if (success)
    												{
    													var json = Ext.decode(response.responseText);
    													if (json.success == true)
    													{
    														Ext.Msg.show(
    														{
    															title    : 'Correo enviado'
    															,msg     : 'El correo ha sido enviado'
    															,buttons : Ext.Msg.OK
    															,fn      : function()
    															{
    																_generarRemesaClic(
    																	false
    																	,panDatComMap1.cdunieco
    																	,panDatComMap1.cdramo
    																	,'M'
    																	,datComPolizaMaestra
    																	,function(){}
    																	,'S'
    																	);
    															}
    														});
    													}
    													else
    													{
    														mensajeError('Error al enviar el correo');
    													}
    												}
    												else
    												{
    													errorComunicacion();
    												}
    											}
    										});
    							
    							}else {
    								mensajeWarning('Introduzca al menos una direcci&oacute;n de email');    
    							}
    						})
    					} */
    				}
    				,{
    					id     : 'botonReenvioWS'
    					,xtype : 'button'
    					,text  : 'Reintentar Emisi&oacute;n'
    					,icon  : contexto+'/resources/fam3icons/icons/award_star_gold_3.png'
    					,disabled: true
    					,hidden: (panDatComMap1.SITUACION != 'AUTO') ? true: false
    					,handler:function()
    					{
    						var me=this;
    						reintentarWSAuto(me.up().up(), _paramsRetryWS);
    					}
    				}
    				,{
    					xtype    : 'button'
    					,id      : 'botonEmitirPolizaFinalPreview'
    					,text    : 'Vista previa'
    					,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
    					,hidden  : panDatComMap1.cdramo=='6'
    					,handler : function()
    					{
    						var nombre_caratula = getNombreReporteCaratura(panDatComMap1.cdtipsit);
    						var me=this;
    						var urlRequestImpCotiza=urlServidorReports
    						+'?destype=cache'
    						+"&desformat=PDF"
    						+"&report="+nombre_caratula
    						+"&paramform=no"
    						+"&userid="+complerepSrvUsr
    						+"&ACCESSIBLE=YES" //parametro que habilita salida en PDF
    						+'&p_unieco='+panDatComMap1.cdunieco
    						+'&p_estado=W'
    						+'&p_ramo='+
    						+'&p_poliza='+panDatComMap1.nmpoliza
    						debug(urlRequestImpCotiza);
    						var numRand=Math.floor((Math.random()*100000)+1);
    						debug(numRand);
    						var windowVerDocu=Ext.create('Ext.window.Window',
    						{
    							title          : 'Vista previa'
    							,width         : 700
    							,height        : 500
    							,collapsible   : true
    							,titleCollapse : true
    							,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
    											 +'src="'+compleUrlViewDoc+"?contentType=application/pdf&url="+encodeURIComponent(urlRequestImpCotiza)+"\">"
    											 +'</iframe>'
    							,listeners     :
    							{
    								resize : function(win,width,height,opt){
    									debug(width,height);
    									$('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
    								}
    							}
    						}).show();
    						windowVerDocu.center();
    					}
    				}
    				/*,{
	                   xtype    : 'button'
	                   ,id      : 'botonEmitirPolizaFinalPreview'
	                   ,text    : 'Vista previa'
	                   ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'																	
	                   ,handler : function(){
	                       var me=this;
	                       var urlRequestImpCotiza=urlServidorReports
	                       +'?destype=cache'
	                       +"&desformat=PDF"
	                       +"&report="+_NOMBRE_REPORTE_CARATULA
	                       +"&paramform=no"
	                       +"&userid="+complerepSrvUsr
	                       +"&ACCESSIBLE=YES" //parametro que habilita salida en PDF
	                       +'&p_unieco='+inputCdunieco
	                       +'&p_estado=W'
	                       +'&p_ramo='+inputCdramo
	                       +'&p_poliza='+inputNmpoliza
	                       debug(urlRequestImpCotiza);
	                       var numRand=Math.floor((Math.random()*100000)+1);
	                       debug(numRand);
	                       var windowVerDocu=Ext.create('Ext.window.Window',{
	                           title          : 'Vista previa'
	                           ,width         : 700
	                           ,height        : 500
	                           ,collapsible   : true
	                           ,titleCollapse : true
	                           ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
	                                            +'src="'+compleUrlViewDoc+"?contentType=application/pdf&url="+encodeURIComponent(urlRequestImpCotiza)+"\">"
	                                            +'</iframe>'
	                           ,listeners     : {
	                               resize : function(win,width,height,opt){
	                                   debug(width,height);
	                                   $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
	                               }
	                           }
	                        }).show();
	                        windowVerDocu.center();
	                    }
	                }*/    				
    				,{
    					xtype     : 'button'
    					,id       : 'botonImprimirPolizaFinal'
    					,text     : 'Imprimir'
    					,icon     : contexto+'/resources/fam3icons/icons/printer.png'
    					,disabled : true
    					,handler  : function(me)
    					{
    						var callbackRemesa = function()
    						{
    							try
    							{
    								_fieldById('IdvenDocuTramite', true).destroy();
    							}
    							catch(e){
    								debugError(e, 'venDocuTramite');
    							}
    							
    							Ext.create('Ext.window.Window',
    							{
    								title        : 'Documentos del tr&aacute;mite '+panDatComMap1.ntramite
    								,modal       : true
    								,buttonAlign : 'center'
    								,width       : 600
    								,height      : 400
    								,autoScroll  : true
    								,cls         : 'VENTANA_DOCUMENTOS_CLASS'
    								,loader      :
    								{
    									url       : panDatComUrlDoc2
    									,params   :
    									{
    										'smap1.nmpoliza'  : datComPolizaMaestra
    										,'smap1.cdunieco' : panDatComMap1.cdunieco
    										,'smap1.cdramo'   : panDatComMap1.cdramo
    										,'smap1.estado'   : 'M'
    										,'smap1.nmsuplem' : '0'
    										,'smap1.ntramite' : panDatComMap1.ntramite
    										,'smap1.nmsolici' : panDatComMap1.nmpoliza
    										,'smap1.tipomov'  : '0'
    									}
    									,scripts  : true
    									,autoLoad : true
    								}
    							}).show();
    						};
    						_generarRemesaClic(
    							false
    							,panDatComMap1.cdunieco
    							,panDatComMap1.cdramo
    							,'M'
    							,datComPolizaMaestra
    							,callbackRemesa
    							);
    					}
    				}
    				,{
    					xtype     : 'button'
    					,id       : 'botonPagar'
    					,text     : 'Pagar'
    					,icon     : contexto+'/resources/fam3icons/icons/money.png'
    					,disabled : true
    					,hidden  : true//TODO: Reemplazar obtDatLoaderContratante por obtieneDatosClienteContratante //inputCdramo!='2'
    					,handler  : function()
    					{
    						if(Ext.isEmpty(_numeroPolizaExt)){
    							mensajeWarning('Error al cargar el n&uacute;mero de p&oacute;liza.');
    							return;
    						}
    							
    						var nombreCompCont = '';
    						try{
    							var datosCont = obtDatLoaderContratante();
    							debug(datosCont);
    							nombreCompCont = datosCont.nombre +" "+ datosCont.snombre +" "+ datosCont.appat +" "+ datosCont.apmat;
    						}
    						catch(e){
    							debug('Sin datos de contratante.');
    							nombreCompCont = '';
    						}
    						
    						var IDventana = window.open("http://www.biosnettcs.com/", "IDventana", "width=610,height=725,top=0,left=190");
    							var parametrosPago = {
    									'etiqueta': 'USERADM1',
    									'etiquetaSys': 'lorant',
    									'mensajeExito': '16168',
    									'asegPago' : '906',//qualitas
    									'contratante' : nombreCompCont,//qualitas
    									'polext' : _numeroPolizaExt
    							};
    							
    							creaWindowPay("http://www.ibseguros.com/securepayment/Validate.action", parametrosPago, "IDventana");
    					}
    				}
    				,{
    					xtype    : 'button'
    					,id      : 'venDocVenEmiBotMesa'
    					,text    : 'Regresar a mesa de control'
    					,icon    : '${ctx}/resources/fam3icons/icons/house.png'
    					//,hidden  : panDatComMap1.SITUACION=='AUTO'
    					,handler : function()
    					{
    						var me=this;
    						Ext.create('Ext.form.Panel').submit(
    						{
    							standardSubmit : true
    							,url           : datComUrlMC
    							,params        :
    							{
    								'smap1.gridTitle':'Tareas',
    								'smap2.pv_cdtiptra_i':1,
    								'smap1.editable':1
    							}
    						});
    					}
    				}
    				,{
    					xtype    : 'button'
    					,id      : 'venDocVenEmiBotIrCotiza'
    					,text    : 'Nueva cotizaci&oacute;n'
    					,icon    : '${ctx}/resources/fam3icons/icons/book_open.png'
    					,hidden  : panDatComMap1.SITUACION!='AUTO'
    					,handler : function()
    					{
    						var me=this;
    						Ext.create('Ext.form.Panel').submit(
    						{
    							standardSubmit : true
    							,url           : compleUrlCotizacion
    							,params        :
    							{
    								'smap1.cdramo'    : panDatComMap1.cdramo
    								,'smap1.cdtipsit' : panDatComMap1.cdtipsit
    							}
    						});
    					}
    				}
    				,{
    					xtype    : 'button'
    					,id      : 'venDocVenEmiBotCancelar'
    					,text    : 'Cancelar'
    					,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
    					,handler : function()
    					{
    						var me=this;
    						me.up().up().destroy();
    					}
    				}
    			]
    		})
    	]
    });
	
	debug('itemsCondicionesRenovacion',itemsCondicionesRenovacion);

	for(var con = 0; con < itemsCondicionesRenovacion.length; con++){
	    if(con === 3 || con === 7){
            itemsCondicionesRenovacion[con].labelWidth = 0;
            itemsCondicionesRenovacion[con].width = 25;
	    }
	    else{
	        itemsCondicionesRenovacion[con].labelWidth = 90;
            itemsCondicionesRenovacion[con].width = 250;
	    }	    
	}
	
	panCondicion = Ext.create('Ext.window.Window',{
    	itemId			: 'panCondicion',
    	autoScroll		: true,
    	height			: 250,
    	width           : 830,
    	modal			: false,
    	closable		: true,
    	closeAction     : 'hide',
    	items			: [
    		Ext.create('Ext.form.Panel',{
            	title        : 'Condiciones de renovacion programada'
                ,itemId      : '_p25_condiciones'
                ,defaults    : { 
                	style      : 'margin :2px;'
                }
                ,layout        : {
                    type       : 'table',
                    columns    : 4,
                    align      : 'left'
                }
                ,items       : itemsCondicionesRenovacion
                ,buttonAlign : 'center'
            })
		],
		buttons: [{
  			text	: 'Guardar',
  			handler : function(me){
  				var _p25_condiciones = _fieldById('_p25_condiciones');
  				debug(_p25_condiciones.form.getValues());
  				var datos = _p25_condiciones.form.getValues(); 
  				var params = {};
  				debug('_p25_condiciones.form',_p25_condiciones.form);
  				var i = 0;
  				var valid = true;
  				for(var k in datos){  					
  					if(k === 'criterio'){
  	  					params['params.'+k] = _p25_condiciones.form.getFields().items[i].rawValue;
  					}
  					else{
  						params['params.'+k] = datos[k];
  					}
  					i++;
  				}  				
  				params['params.operacion'] = panCondicion['operacion'];
  				var _p25_gridCondiciones   = _fieldById('_p25_gridCondiciones');
  				params['params.nmperiod']  = _p25_gridCondiciones.nmperiod;
  				params['params.cdunieco']  = _p25_gridCondiciones.cdunieco;
  				params['params.cdramo']    = _p25_gridCondiciones.cdramo;
  				debug('params',params);
  				if(params['params.campo'] === 'FEPROREN'){
  				    debug('es fecha',isValidDate(params['params.valor']));
                    if(!isValidDate(params['params.valor'])){
                        valid = false;
                        mensajeError('El valor de fecha no es valido (DD/MM/YYYY)');
                    }
                    if(!Ext.isEmpty(params['params.valor2'])){
                        if(!isValidDate(params['params.valor2'])){
                            valid = false;
                            mensajeError('El valor 2 de fecha no es valido (DD/MM/YYYY)');
                        }
                    }
    		    }
    		    if (valid === true){
    		        _mask('Guardando cambios');    		    
                    Ext.Ajax.request({
                        url       : _p25_urlMovimientoCondiciones,
                        params    : params,
                        success  : function(response){
                            _unmask();
                            var resp = Ext.decode(response.responseText);
                            debug('resp',resp);                      
                            if(resp.exito == true){
                                mensajeCorrecto('Mensaje','Guardado con exito');
                                var _p25_gridCondiciones = _fieldById('_p25_gridCondiciones');
                                _p25_gridCondiciones.store.reload();
                                me.up('window').close();
                            }
                            else{
                                mensajeError(resp.respuesta);
                            }
                        },
                        failure  : function(){
                            _unmask();
                            errorComunicacion();
                        }
                    });
                }
  		    }
  		},
  		{ 
  			text: 'Cancelar',
  			handler : function(){
  				this.up('window').close();
  			} 
  		}]
	});
       
	panCalendario = Ext.create('Ext.window.Window',{
    	itemId			: 'panCalendario',
    	autoScroll		: true,
    	height			: 250,
    	modal			: false,
    	closable		: true,
    	closeAction     : 'hide',
    	items			: [
    		Ext.create('Ext.form.Panel',{
            	title        : 'Calendario de renovacion programada'
                ,itemId      : '_p25_calendario'
                ,defaults    : { 
                	style : 'margin :5px;' 
                }
                ,layout      : {
                    type     : 'table'
                    ,columns : 3
                }
                ,items       : itemsCalendarioRenovacion
                ,buttonAlign : 'center'
            })
		],
		buttons: [{
  			text	: 'Guardar',
  			handler : function(me){
  			    var formBusqueda = _fieldById('_p25_busquedaForm');
                _fieldByName('anio',formBusqueda).readOnly = false;
                _fieldByName('mes', formBusqueda).readOnly = false;
  				var _p25_calendario = _fieldById('_p25_calendario');
  				debug('_p25_calendario',_p25_calendario.form.getValues());
  				var desde     = _fieldByName('feinicio',_p25_calendario).getValue();
  				var hasta     = _fieldByName('fefinal',_p25_calendario).getValue();
  				var feaplica  = _fieldByName('feaplica',_p25_calendario).getValue();
  				var anio;
  				var mes;
  				if(!Ext.isEmpty(desde)){
  				    anio = desde.getFullYear(); 
  				    mes  = desde.getMonth();  
  				}  				
  				var inicio    = toDate(anio, mes+1, 1);
  				var fin       = Ext.Date.add(inicio, Ext.Date.MONTH, 1);
  				fin           = Ext.Date.add(fin,    Ext.Date.DAY,  -1);
  				debug('inicio',inicio);
  				debug('desde', desde);
  				debug(Ext.Date.isEqual(inicio,desde));
  				debug('fin',   fin);
  				debug('hasta', hasta);
  				debug(Ext.Date.isEqual(fin,hasta));
  				if(Ext.Date.isEqual(inicio,desde) == false || Ext.Date.isEqual(fin,hasta) == false){
  				    centrarVentanaInterna(Ext.MessageBox.confirm('Atencion', 'No esta seleccionando un mes completo<br>¿Desea continuar?', function(btn){
                        if(btn === 'yes'){
                            debug('diferencia', daydiff(desde, feaplica));
                            if((daydiff(desde, feaplica)) > 45 || (daydiff(desde, feaplica)) < -45){
                                centrarVentanaInterna(Ext.MessageBox.confirm('Atencion', 'F. Ejecucion fuera de rango por 45 dias<br>¿Desea continuar?', function(btn){
                                    if(btn === 'yes'){
                                        guardarCalendario();
                                    }
                                }));
                            }
                            else{
                                guardarCalendario();
                            }
                        }                        
                    }));
  				}
  				else{
  				    debug('diferencia', daydiff(desde, feaplica));
                    if((daydiff(desde, feaplica)) > 45 || (daydiff(desde, feaplica)) < -45){
                        centrarVentanaInterna(Ext.MessageBox.confirm('Atencion', 'F. Ejecucion fuera de rango por 45 dias<br>¿Desea continuar?', function(btn){
                            if(btn === 'yes'){
                                guardarCalendario();
                            }
                         }));
                    }
                    else{
                        guardarCalendario();
                    }  
  				}  				
  			 }
  		},
  		{
  		    text    : 'Limpiar',
  		    handler : function(me){
  		        debug('me up',me.up('window').down('form'));
  		        limpiarValoresCalendario(me.up('window').down('form'));
  		    }
  		},
  		{ 
  			text: 'Cancelar',
  			handler : function(){
  			    var formBusqueda = _fieldById('_p25_busquedaForm');
                _fieldByName('anio',formBusqueda).readOnly = false;
                _fieldByName('mes', formBusqueda).readOnly = false;
  				this.up('window').close();
  			} 
  		}]
	});
	
	panAyuda = Ext.create('Ext.window.Window',{
        itemId          : 'panAyuda',
        autoScroll      : true,
        height          : 219,
        width           : 350,
        modal           : false,
        closable        : true,
        closeAction     : 'hide',
        items           : [
            Ext.create('Ext.form.Panel',{
                title        : 'Ayuda en el formato de valores'
                ,itemId      : '_p25_panAyuda'
                ,defaults    : { 
                    style : 'margin :5px;' 
                }
                ,layout      : {
                    type     : 'table'
                    ,columns : 3
                }
                ,items       : [
                    {
                        xtype       : 'displayfield',
                        itemId      : 'idSuggestValue',
                        allowBlank  : true,
                        width       : 320
                    }
                ]
                ,buttonAlign : 'center'
            })
        ],
        buttons: [{
            text    : 'Cerrar',
            handler : function(me){
                this.up('window').close();
            }
        }]
    });
	
    ////// componentes //////
    
    ////// contenido //////
    
    Ext.create('Ext.panel.Panel',
    {
        renderTo    : '_p25_divpri'
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
                        ,handler : _p25_buscarClic
                    },
                    {
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat.png'
                        ,handler : _p25_limpiarFiltros
                    }
                ]
                ,listeners	:	{
                	click	:	function(options) {
                		debug('click panel');
                		}
                }
            })
            ,Ext.create('Ext.form.Panel',{
                title        : 'Contratante'
                ,itemId      : '_p25_contratante'
                ,defaults    : { style : 'margin :5px;' }
                ,autoScroll	 : true
                ,hidden		 : 'true'
                ,layout      : {
                    type     : 'table'
                    ,columns : 3
                }
                ,items    :	itemsFormularioContratante
            })
            ,Ext.create('Ext.grid.Panel',{
                title        : 'Informacion de poliza'
                ,itemId      : '_p25_poliza'
                ,selType     : 'checkboxmodel'
                ,store       : _p25_storePolizas                
                //,minHeight   : 200
                //,maxHeight   : 400
                ,height      : 200
                ,columns     : itemsFormularioPolizaColumns
                ,buttonAlign : 'center'
                ,hidden		 : 'true'
                //,autoScroll	 : true
                ,buttons     :
                [
                    {
                        text      : 'Renovar'
                        ,itemId   : '_p25_polizaBotonRenovar'
                        ,icon     : '${ctx}/resources/fam3icons/icons/date_add.png'
                        ,handler  : _p25_renovarPolizaClic
                        ,disabled : true
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',{
                title        : 'Resultados'
                ,itemId      : '_p25_grid'
                ,selType     : 'checkboxmodel'
                ,store       : _p25_storePolizasMasivas
                //,minHeight   : 200
                //,maxHeight   : 400
                ,height      : 400
                ,columns     : gridColumns
                //,viewConfig  : viewConfigAutoSize
                ,buttonAlign : 'center'
                ,hidden		 : 'true'
                ,autoScroll :  true
                ,buttons     :
                [
                    {
                        text      : 'Renovar'
                        ,itemId   : '_p25_gridBotonRenovar'
                        ,icon     : '${ctx}/resources/fam3icons/icons/date_add.png'
                        ,handler  : _p25_renovarClic
                        ,disabled : true
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',{
                title        : 'Calendarizacion programada'
                ,itemId      : '_p25_gridCalendario'
                ,selType     : 'checkboxmodel'
                ,store       : _p25_storeCalendario
                ,height      : 200
                ,columns     : itemsCalendarioColumns
                ,buttonAlign : 'center'
                ,hidden		 : 'true'
                ,autoScroll	 : true
                ,selModel    : Ext.create('Ext.selection.CheckboxModel',{
                	mode               : 'SINGLE',
                	showHeaderCheckbox : false
                })
                ,bbar			: [
  		    		{ 
  		        		xtype   : 'button', 
  		        		text    : 'Agregar fecha',
  		        		handler : function(){
  		        			agregaCalendario();
  		        		}
  		    		},
  		    		{
  		        		xtype   : 'button', 
  		        		text    : 'Exclusiones',
  		        		//handler : mostrarCondiciones()
  		        		handler : function(){
  		        			var _p25_gridCalendario = _fieldById('_p25_gridCalendario');
  		        			if(_p25_gridCalendario.getSelectionModel().hasSelection()){
	  		        			debug('_p25_gridCalendario',_p25_gridCalendario.getSelectionModel().getSelection()[0].raw);
	  		        			var calen = _p25_gridCalendario.getSelectionModel().getSelection()[0].raw;
	  		        			_p25_storeCondiciones.load({
	    						params    :	{
	    							'smap1.nmperiod' : calen['nmperiod'],
	    							'smap1.cdunieco' : calen['cdunieco'],
	    							'smap1.cdramo'   : calen['cdramo'],
	    							'smap1.anio'     : calen['anio'],
	    							'smap1.mes'      : calen['mes']
	    						},
	    						callback :  function(recordsCon, opCon, successCon){
	            					debug('entro a callback');
	            					if(successCon){
	            						var respCon = Ext.decode(opCon.response.responseText);
	            						debug('respCon',respCon);
	                					if(respCon.exito != true){
	                    					mensajeWarning('No hay resultados');
	                					}
	                					else{
	                    					var _p25_gridCondiciones 	  = _fieldById('_p25_gridCondiciones');
	                    					_p25_gridCondiciones.nmperiod = calen['nmperiod'];
	                    					_p25_gridCondiciones.cdunieco = calen['cdunieco'];
	                    					_p25_gridCondiciones.cdramo   = calen['cdramo'];
	                						_fieldById('_p25_gridCondiciones').show();
	                					}
	            					}
	            					else{
	                					mensajeError(opCon.getError());
	            					}
	        					}
	    						});
  		        			}
  		        			else{
  		        			    mensajeWarning('Debe seleccionar un registro');
  		        			}
  		        		} 
  		    		}  		    		
				]
            })
            ,Ext.create('Ext.grid.Panel',{
                title        : 'Condiciones de renovacion programada'
                ,itemId      : '_p25_gridCondiciones'
                ,selType     : 'checkboxmodel'
                ,store       : _p25_storeCondiciones
                ,height      : 200
                ,columns     : itemsCondicionesColumns
                ,buttonAlign : 'center'
                ,hidden		 : 'true'
                ,autoScroll	 : true
                ,bbar			: [
  		    		{ 
  		        		xtype   : 'button', 
  		        		text    : 'Agregar exclusi\u00f3n',
  		        		handler : function(){
  		        			agregaCondicion();
  		        		}
  		    		}
				]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    var form = _fieldById('_p25_busquedaForm');
    
    _fieldByName('tipo',form).getStore().on({
        load : function(me){
  		    var tipo = _fieldByName('tipo',form);
  		    tipo.setValue('AS');  		    
  		    if(_GLOBAL_CDSISROL != RolSistema.GerenteOperacionesEmision){
  		        debug('tipo',tipo);
  		        tipo.store.removeAt(2);
  		    }  		    
  		    if(_GLOBAL_CDSISROL == RolSistema.Agente || _GLOBAL_CDSISROL == RolSistema.EjecutivoVenta || _GLOBAL_CDSISROL == RolSistema.MesaControl){
  		        tipo.readOnly = true;
  		    }
  	    }
  	});
  	    
  	_fieldByName('tipo',form).on({
  	    change: function(me, newValue) {
  		    if(me.getValue() == 'AS' || me.getValue() == 'SA'){
  			    _fieldByName('fecini',form).hide();
  				_fieldByName('fecfin',form).hide();
  				_fieldByName('status',form).hide();
  				_fieldByName('cdunieco',form).show();
  				_fieldByName('cdramo',form).show();
  				_fieldByName('nmpoliza',form).show();
  				_fieldByName('cdtipsit',form).hide();
  				_fieldByName('administradora',form).hide();
  				_fieldByName('retenedora',form).hide();
  				_fieldByName('cdperson',form).hide();
  				_fieldByName('anio',form).hide();
  				_fieldByName('mes',form).hide();
  				_fieldById('_p25_contratante').hide();
  				_fieldById('_p25_poliza').hide();
  				_fieldById('_p25_grid').hide();
  				_fieldById('_p25_gridCalendario').hide();
  				_fieldById('_p25_gridCondiciones').hide();
  				form.doLayout();
  		    }
  		    else if(me.getValue() == 'P'){
  			    _fieldByName('fecini',form).hide();
  				_fieldByName('fecfin',form).hide();
  				_fieldByName('status',form).hide();
  				_fieldByName('cdunieco',form).hide();
  				_fieldByName('cdramo',form).hide();
  				_fieldByName('nmpoliza',form).hide();
  				_fieldByName('cdtipsit',form).hide();
  				_fieldByName('administradora',form).hide();
  				_fieldByName('retenedora',form).hide();
  				_fieldByName('cdperson',form).hide();
  				_fieldByName('anio',form).show();
  				_fieldByName('mes',form).show();
  				_fieldById('_p25_contratante').hide();
  				_fieldById('_p25_poliza').hide();
  				_fieldById('_p25_grid').hide();
  				_fieldById('_p25_gridCalendario').hide();
  				_fieldById('_p25_gridCondiciones').hide();
  				form.doLayout();
  			}
  			else if(me.getValue() == 'M'){
  			    _fieldByName('fecini',form).show();
  				_fieldByName('fecfin',form).show();
  				_fieldByName('status',form).hide();
  				_fieldByName('cdunieco',form).show();
  				_fieldByName('cdramo',form).show();
  				_fieldByName('nmpoliza',form).hide();
  				_fieldByName('cdtipsit',form).show();
  				_fieldByName('administradora',form).show();
  				_fieldByName('retenedora',form).show();
  				_fieldByName('cdperson',form).show();
  				_fieldByName('anio',form).hide();
  				_fieldByName('mes',form).hide();
  				//_fieldByName('fecini',form).setMinValue(new Date());
  				//_fieldByName('fecfin',form).setMinValue(new Date());
  				_fieldById('_p25_contratante').hide();
  				_fieldById('_p25_poliza').hide();
  				_fieldById('_p25_grid').hide();
  				_fieldById('_p25_gridCalendario').hide();
  				_fieldById('_p25_gridCondiciones').hide();
  				form.doLayout();
  		    }
  	    }
  	});
  	
  	_fieldByName('cdunieco',form).on({
  	    select : function(){
  		    debug('> Cambiando cdperson');
  		    var cdperson = _fieldByName('cdperson',form);
  		    cdperson.store.proxy.extraParams['params.cdunieco'] = _fieldByName('cdunieco',form).getValue();
  		    cdperson.store.proxy.extraParams['params.cdramo']   = _fieldByName('cdramo',form).getValue(); 
  		    debug(cdperson.store.extraParams);
		    debug('< Cambiando cdperson');  															
  	    }
    });
    
    _fieldByName('fecini',form).on({
        blur : function(){
            debug('> Cambiando fecini');
            var fecini = _fieldByName('fecini',form);
            var fecfin = _fieldByName('fecfin',form);
            fecfin.setValue('');
            fecfin.setMinValue(fecini.getValue());
            debug('< Cambiando fecini');                                                              
        }
    });
    
	_fieldByName('cdramo',form).on({
  		select : function(){
  			debug('> Cambiando cdperson');
  			var cdperson = _fieldByName('cdperson',form);
  			cdperson.store.proxy.extraParams['params.cdunieco'] = _fieldByName('cdunieco',form).getValue();
  			cdperson.store.proxy.extraParams['params.cdramo']   = _fieldByName('cdramo',form).getValue(); 
 			debug(cdperson.store.extraParams);
 			debug('< Cambiando cdperson');
 			debug('> Cambiando admistradora');
 			debug(_fieldByName('administradora',form));
 			_fieldByName('administradora',form).store.proxy.extraParams['params.cdramo'] = _fieldByName('cdramo',form).getValue();
 			_fieldByName('administradora',form).store.proxy.extraParams['params.dsatribu'] = 'ADMINISTRADORA';
			_fieldByName('administradora',form).store.load();
			debug('< Cambiando admistradora');
  		}
  	});
  	
  	_fieldByName('administradora',form).on({
  		select : function(){  			
 			debug('> Cambiando retenedora');
 			debug(_fieldByName('retenedora',form));
 			_fieldByName('retenedora',form).store.proxy.extraParams['params.cdramo']   = _fieldByName('cdramo',form).getValue();;
 			_fieldByName('retenedora',form).store.proxy.extraParams['params.dsatribu'] = 'RETENEDORA';
			_fieldByName('retenedora',form).store.load();
			debug('< Cambiando retenedora');
  		}
  	});
  	
  	_fieldByName('mes',form).getStore().on({
        load : function(me){
            debug('cargando meses');
            me.insert(0,new Generic({key:'-1',value:'TODOS'}));
            _fieldByName('mes',form).setValue('-1');
        }
    });
  	
    _fieldById('_p25_grid').getSelectionModel().on({
        selectionChange : _p25_gridSelectionChange
    });
    
    _fieldById('_p25_poliza').getSelectionModel().on({
        selectionChange : _p25_polizaSelectionChange
    });
    
    _fieldById('_p25_gridCalendario').getSelectionModel().on({
        selectionChange : function(){
            mostrarCondiciones();
        }
    });    
     
     var formCondicion = panCondicion.down('form');
     
	 _fieldByName('criterio',formCondicion).on({
  	     select : function(me){
  		     debug('> Cambiando criterio',me.getValue());
  		     if(me.getValue() == 2){
	     	     _fieldByName('valor2',formCondicion).enable();
	         }
	 		 else{
	 		    setTextHelpValue(_fieldByName('campo',formCondicion));
	     	    _fieldByName('valor2',formCondicion).disable();
	 		 }  		
			 debug('< Cambiando criterio');
  	     }
  	 });
	 
	 _fieldByName('campo',formCondicion).on({
         select : function(me){
             setTextHelpValue(_fieldByName('campo', formCondicion).getValue());
         }        
     });
	 
	 _fieldByName('valor',formCondicion).on({         
         blur   : function(me){
            var campo = _fieldByName('campo',formCondicion).getValue();
            debug('campo ',campo);
            debug('me '   ,me.getValue());
            if(!Ext.isEmpty(campo)){
                _mask('Validando...');
                Ext.Ajax.request({
                    url     : _p25_urlValidateValueExclusion
                    ,params : {
                        'params.criterio'  : campo,
                        'params.valor'     : me.getValue()
                    }
                    ,success : function(response){
                        _unmask();
                        var ck = '';
                        var json = Ext.decode(response.responseText);
                        try{                         
                            if(json.exito === true){
                                _fieldById('idValueExclu').setValue(json.respuesta);
                                debug('respuesta',json.respuesta);                                                     
                            }
                            else{
                                mensajeError(json.respuesta);
                            }
                        }
                        catch(e){
                            manejaException(e,ck);
                        }
                    }
                    ,failure : function(){
                        _unmask();
                        errorComunicacion(null,'Error de red');
                    }
                });
            }    
        }         
    });
	 
	 _fieldByName('valor2',formCondicion).on({         
         blur   : function(me){
            var campo = _fieldByName('campo',formCondicion).getValue();
            debug('campo ',campo);
            debug('me '   ,me.getValue());
            if(!Ext.isEmpty(campo)){
                _mask('Validando...');
                Ext.Ajax.request({
                    url     : _p25_urlValidateValueExclusion
                    ,params : {
                        'params.criterio'  : campo,
                        'params.valor'     : me.getValue()
                    }
                    ,success : function(response){
                        _unmask();
                        var ck = '';
                        var json = Ext.decode(response.responseText);
                        try{                         
                            if(json.exito === true){
                                _fieldById('idValueExclu2').setValue(json.respuesta);
                                debug('respuesta',json.respuesta);                                                     
                            }
                            else{
                                mensajeError(json.respuesta);
                            }
                        }
                        catch(e){
                            manejaException(e,ck);
                        }
                    }
                    ,failure : function(){
                        _unmask();
                        errorComunicacion(null,'Error de red');
                    }
                });
            }
        }         
    });
	 
	 var formCalendario = panCalendario.down('form');
	 
	 _fieldByName('mes',formCalendario).on({
         blur : function(me){
             debug('> Cambiando mes',me.getValue());
             var anio     = _fieldByName('anio',formCalendario).getValue();
             var feinicio = toDate(anio, me.getValue()    , 1);
             _fieldByName('feinicio',formCalendario).setValue(feinicio);
             feinicio = Ext.Date.add(feinicio, Ext.Date.DAY, -1);
             _fieldByName('feaplica',formCalendario).setValue(feinicio);             
             feinicio = Ext.Date.add(feinicio, Ext.Date.MONTH, 1);
             _fieldByName('fefinal', formCalendario).setValue(feinicio);
             debug('< Cambiando mes');
         }
     });

	 _fieldByName('feaplica',formCalendario).on({
         blur : function(me){
             debug('> Cambiando feinicio',me.getValue());
             var feaplica = _fieldByName('feaplica',formCalendario).getValue();
             var fecha = toDate(feaplica.getFullYear(), feaplica.getMonth()+1,  1); //new Date(feaplica.getFullYear(), feaplica.getMonth()+1,  1);
             _fieldByName('feinicio',formCalendario).setMinValue(fecha);
             _fieldByName('fefinal', formCalendario).setMinValue(fecha);
             _fieldByName('feinicio',formCalendario).setValue(fecha);
             _fieldByName('fefinal', formCalendario).setValue('');
             debug('< Cambiando feinicio');
         }
     });
     
     _fieldByName('feinicio',formCalendario).on({
         blur : function(me){
             debug('> Cambiando feinicio',me.getValue());
             _fieldByName('fefinal',formCalendario).setMinValue(_fieldByName('feinicio',formCalendario).getValue());
             _fieldByName('fefinal',formCalendario).setValue('');
             debug('< Cambiando feinicio');
         }
     });
     
     _fieldByName('cdunieco',formCalendario).getStore().on({
        load : function(me){
            me.insert(0,new Generic({key:'-1',value:'TODOS'}));
            _fieldByName('cdunieco', formCalendario).setValue('-1');
        }
     });     
     
     _fieldByName('cdramo',formCalendario).getStore().on({
        load : function(me){
            me.insert(0,new Generic({key:'-1',value:'TODOS'}));
            _fieldByName('cdramo', formCalendario).setValue('-1');
        }
     });
     
     var formCondicion = panCondicion.down('form');
     
     _fieldByName('valor',formCondicion).on({
         blur : function(me){
             var criterio = _fieldByName('criterio',formCondicion).rawValue;
             var campo    = _fieldByName('campo',   formCondicion).getValue();             
             debug('criterio',criterio);
             debug('campo',   campo);
             if(campo === 'FEPROREN'){
                 debug('es fecha',isValidDate(me.getValue()));
                 if(!isValidDate(me.getValue())){
                     _fieldByName('valor',   formCondicion).setValue()
                     mensajeError('El valor de fecha no es valido (DD/MM/YYYY)');
                 }
             }
             debug('< Validando valor');
         }
     });
     
     _fieldByName('valor2',formCondicion).on({
         blur : function(me){
             var criterio = _fieldByName('criterio',formCondicion).rawValue;
             var campo    = _fieldByName('campo',   formCondicion).getValue();
             if(campo === 'FEPROREN'){
                 //debug('es fecha',isValidDate(me.getValue()));
                 if(!isValidDate(me.getValue())){
                     _fieldByName('valor2',   formCondicion).setValue()
                     mensajeError('El valor de fecha no es valido (DD/MM/YYYY)');
                 }
                 else{
                     var valor  = _fieldByName('valor',  formCondicion).getValue();
                     var valor2 = _fieldByName('valor2',  formCondicion).getValue();
                     if(!Ext.isEmpty(valor)){
                         var feciniArr = valor.split('/');
                         var fecfinArr = valor2.split('/');
                         var fecini = toDate(feciniArr[2],feciniArr[1],feciniArr[0]);  //new Date(feciniArr[2],feciniArr[1],feciniArr[0]);
                         var fecfin = toDate(fecfinArr[2],fecfinArr[1],fecfinArr[0]);  //new Date(fecfinArr[2],fecfinArr[1],fecfinArr[0]);
                         debug('fecini',fecini);
                         debug('fecfin',fecfin);
                         if(fecini > fecfin){
                             _fieldByName('valor2',  formCondicion).setValue('');
                             mensajeError('La segunda fecha debe ser mayor o igual a la primera');
                         }    
                     }
                 }
             }
             debug('< Validando valor');
         }
     });
     
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _p25_buscarClic(button,e)
{
    debug('>_p25_buscarClic');
    var panDatComBotonRetarificar = _fieldById('panDatComBotonRetarificar');
    var panDatComBotonGuardar     = _fieldById('panDatComBotonGuardar');
    panDatComBotonRetarificar.enable();
    panDatComBotonGuardar.enable();
    _fieldById('_p25_contratante').hide();
  	_fieldById('_p25_poliza').hide();
  	_fieldById('_p25_grid').hide();
    var form = button.up('form');
    var tipo = _fieldByName('tipo'     , form);
    debug('tipo ',tipo);
    tipo.disable();
    if (tipo.getValue() == 'AS' || tipo.getValue() == 'SA'){
    //AS && SA
    	debug('entro por AS & SA');
    	_p25_ultimosParams =
    	{
    		'smap1.cdunieco' 	: _fieldByName('cdunieco' 	, form).getValue()
        	,'smap1.cdramo'   	: _fieldByName('cdramo'   	, form).getValue()
        	,'smap1.estado'   	: 'M'
        	,'smap1.nmpoliza' 	: _fieldByName('nmpoliza' 	, form).getValue()       
    	};
    	_mask('Obteniendo datos de poliza');
    	_p25_storePolizas.load({
    		params    :	_p25_ultimosParams,
        	callback :  function(records, op, success)
        	{
            	debug('entro a callback');
            	debug('op',op);
            	if(success)
            	{
            		_unmask();
            		debug('records',records);
                	if(records.length==0)
                	{
                    	mensajeWarning('Poliza no existe');
                    	
                	}else{
                		_p25_storeContratante(records, op, success);
                		var tipo = _fieldByName('tipo');
                		_p25_selectionCharge(tipo);
                		_unmask();
                	}
            	}
            	else
            	{
            		_unmask();
                	mensajeError(op.getError());
            	}
        	}
    	});
    	debug(_p25_ultimosParams);
    	debug('sale de AS & SA');
    }else if (tipo.getValue() == 'M'){
    //M
    	debug('Entra a M');
    	_mask('Obteniendo polizas candidatas a renovacion');
    	debug('form ',form);
    	_p25_ultimosParams = {
    		'smap1.fecini'		: Ext.Date.format(_fieldByName('fecini'		, form).getValue(), "d/m/Y")
    		,'smap1.fecfin'		: Ext.Date.format(_fieldByName('fecfin'		, form).getValue(), "d/m/Y")
    		,'smap1.cdunieco' 	: _fieldByName('cdunieco' 	, form).getValue()
        	,'smap1.cdramo'   	: _fieldByName('cdramo'   	, form).getValue()
        	,'smap1.estado'   	: 'M'
        	,'smap1.cdtipsit'	: _fieldByName('cdtipsit'	, form).getValue()
        	,'smap1.retenedora'	: _fieldByName('retenedora'	, form).getValue()
        	,'smap1.cdperson'	: _fieldByName('cdperson'	, form).getValue()
    	};
    	_p25_storePolizasMasivas.load({
    		params    :	_p25_ultimosParams,
    		callback :  function(records, op, success){
            	debug('entro a callback');
            	debug('op ',op);
            	debug('records ',records);	
            	if(success){
            		_unmask();
            		var response = Ext.decode(op.response.responseText);
                	debug('response ',response);
            		if(response["exito"]){
	                	if(records.length==0){
	                    	mensajeWarning('No hay resultados');
	                	}
	                	else{
	                		_p25_selectionCharge(tipo);
	                	}
	            	}
	            	else{
	            		mensajeError(response["respuesta"]);
	            	}
            	}
            	else{
            		_unmask();
            		errorComunicacion(null);
            	}
        	}
    	});    	
    	debug('_p25_ultimosParams',_p25_ultimosParams);
    	debug('Sale de M');
    }else if (tipo.getValue() == 'P'){
    //P
    	debug('Entra a P');
    	_fieldByName('anio', form).enable();
    	_fieldByName('mes',  form).enable();
    	_p25_ultimosParams =
    	{
    		'smap1.anio'    : _fieldByName('anio', form).getValue(),
    		'smap1.mes'		: _fieldByName('mes' , form).getValue() == -1 ? null : _fieldByName('mes' , form).getValue()
    		
    	};    	
    	_p25_storeCalendario.load({
    		params    :	_p25_ultimosParams,
    		callback :  function(records, op, success){
            	debug('entro a callback');            	            	
            	if(success){
            		var resp = Ext.decode(op.response.responseText);
            		debug('resp',resp);
                	if(resp.exito!=true){
                    	mensajeWarning(resp.respuesta);
                	}
                	else{
                		//var minDate = Ext.Date.parse("01/"+_fieldByName('mes' , form).getValue()+"/"+_fieldByName('anio', form).getValue(), "d/m/Y");
    					//var maxDate = Ext.Date.getLastDateOfMonth(minDate);
    					//panCalendario.minDate  = minDate;
    					//panCalendario.maxDate  = maxDate;
    					_p25_selectionCharge(tipo);
                	}
            	}
            	else{
                	mensajeError(op.getError());
            	}
        	}
    	});    	
    	debug('_p25_ultimosParams',_p25_ultimosParams);
    	debug('Sale de P');
    }
    debug('<_p25_buscarClic');
}

function _p25_storeContratante(records, op, success){
debug('>_p25_storeContratante');
	if(success && records.length > 0){
		_fieldById('_p25_contratante').loadRecord(new _p25_modeloContratante(records[0].raw));
	}
debug('<_p25_storeContratante');
}

function _p25_renovarPolizaClic(button,e)
{
    debug('>_p25_renovarPolizaClic');
    var pol = _fieldById('_p25_poliza').store.data.items[0].raw;
    debug('pol',pol);
    var sePuedeRenovar = false;
    var mensaje = 'No ha sido posible renovar por las siguientes razones<br/>';
    debug('renovada',pol['renovada']);
    if(pol['renovada'] == 'NO'){
    	sePuedeRenovar = true;
    	debug('pagada',pol['pagada']);
    	if(pol['pagada'] == 'SI'){
    	    sePuedeRenovar = true;
    		debug('aseg_edad_val',pol['aseg_edad_val']);
    		if(Number(pol['aseg_edad_val']) === 0){
    		    debug('igual a 0');
    			sePuedeRenovar = false;
                mensaje = mensaje+'La p\u00F3liza no tiene asegurados con edad valida<br/>';
    		}
    		else{
    		    debug('diferente a 0');
    		    sePuedeRenovar = true;    			
    		}    		
    	}
    	else{
    		sePuedeRenovar = false;
    		mensaje = mensaje+'La p\u00F3liza no est\u00E1 pagada<br/>';
    	}
    }
    else{
    	sePuedeRenovar = false;
    	if(pol['renovada'] == 'SI'){
    	    mensaje = mensaje+'La p\u00F3liza ya est\u00E1 renovada '+pol['nmpoliex']+'<br/>';
    	}
    	else if(pol['renovada'] == 'CANCELADA'){
    	    mensaje = mensaje+'La p\u00F3liza est\u00E1 cancelada '+'<br/>';
    	}
    	else if(pol['renovada'] == 'BLOQUEADA'){
            mensaje = mensaje+'La p\u00F3liza est\u00E1 bloqueada '+'<br/>';
        }
    	else{
    	    mensaje = mensaje+'La p\u00F3liza est\u00E1 en proceso de renovaci\u00F3n'+'<br/>';
    	}
    }        
    debug('sePuedeRenovar',sePuedeRenovar);
    if(sePuedeRenovar == true){
    		_mask('Renovando poliza');
    		Ext.Ajax.request({
        		url       : _p25_urlRenovarPolizaIndividual,
        		params     : 
        		{
        			'params.cdunieco'  : pol['cdunieco'],
        			'params.cdramo'    : pol['cdramo'],
        			'params.estado'    : 'M',
        			'params.nmpoliza'  : pol['nmpoliza'],
        			'params.feefecto'  : pol['feefecto'],
        			'params.feproren'  : pol['feproren'],
        			'params.cdmoneda'  : pol['cdmoneda'],
        			'params.estadoNew' : 'W'
        		},
        		success  : function(response)
        		{
                	_unmask();
            		var resp = Ext.decode(response.responseText);
            		debug('resp',resp);
            		if(resp.exito){
	            		var ntramite = resp.slist1[0]['ntramite'];
	            		debug('resp',ntramite);
	            		debug('pol',pol);
	            		if(!Ext.isEmpty(ntramite)){
	                		var form = _fieldById('_p25_busquedaForm');
	                    	if(_fieldByName('tipo',form).getValue() == 'AS'){
	                    		var resRenova = resp.slist1[0];
	                    		resRenova['feefecto_ant'] = pol['feefecto'];
	                    		resRenova['cdmoneda'] 	  = pol['cdmoneda'];
	                    		_p25_ventanaAutoServicio(resRenova);
	                    	}
	                    	else{
	                    		_p25_ventanaServicioAsistido();
	                    	}
	                		_fieldById('_p25_contratante').hide();
	  						_fieldById('_p25_poliza').hide();
	  						_fieldById('_p25_grid').hide();
	            		}
	            		else
	            		{
	                		mensajeError(resp.respuesta);
	            		}
            		}
            		else{
            			mensajeError(resp.respuesta);
            		}
        		},
        		failure  : function()
        		{
        			_unmask();
            		errorComunicacion();
        		}
    		});
    	//}
    }
    else{
    	mensajeError(mensaje);
    }
    debug('<_p25_renovarPolizaClic');
}

function _p25_renovarClic(button,e)
{
    debug('>_p25_renovarClic');
    var json   		= {};
    var slist1 		= [];
    var noRenova	= [];
    var mensajeErr  = "";
    var mensajecmp  = "";
    debug('_p25_grid',_fieldById('_p25_grid').getSelectionModel());
    var arr    = _fieldById('_p25_grid').getSelectionModel().getSelection();
    debug('arr',arr);
    Ext.Array.each(arr,function(record){
        var val 		= record.raw;
        val['cducreno']	= record.get('cducreno');
        //debug('record',record);
        if(record.data['renovada'] == 'SI' || record.data['renovada'] == 'CANCELADA' || record.data['renovada'] == 'BLOQUEADA' || record.data['pagada'] == 'NO' || record.data['aseg_edad_val'] == 0){
        	noRenova.push(val);
        }
        else{
        	slist1.push(val);
        }
    });
    json['slist1'] = slist1;    
    debug('### renovar json params:',json);
    _mask('Renovando...');
    //_fieldById('_p25_grid').setLoading(true);
    Ext.Ajax.request({
        url       : _p25_urlRenovarPolizasMasivasIndividuales
        ,jsonData : json
        ,success  : function(response){
            //_fieldById('_p25_grid').setLoading(false);
            var resp = Ext.decode(response.responseText);
            debug('### renovar json response:',resp);
            if(resp.exito == true){
                _fieldById('_p25_grid').getStore().removeAll();
                _unmask();
                mensajeCorrecto('Proceso completo','Se renovo con exito');
            }
            else{
                _unmask();
                mensajeError(resp.respuesta);
            }
            if(noRenova.length == 1){
    			mensajeErr = "La poliza(";
    			mensajecmp = ") no es valida para renovar.";
    		}
    		else{
    			mensajeErr = "Las polizas(";
    			mensajecmp = ") no son validas para renovar.";
    		}
    		if(noRenova.length > 0){
    			Ext.Array.each(noRenova,function(record){
        			mensajeErr = mensajeErr + record['cdunieco'] + "-" + record['cdramo'] + "-" + record['nmpoliza'] + ", ";
    			});
    			mensajeErr = mensajeErr.substr(0, mensajeErr.length -2) + mensajecmp;
    			mensajeError(mensajeErr);
    		}
        }
        ,failure  : function(){
            //_fieldById('_p25_grid').setLoading(false);
            _unmask();
            errorComunicacion();
        }
    });
    debug('<_p25_renovarClic');
}

function _p25_gridSelectionChange(selModel,selected,e)
{
    debug('>_p25_gridSelectionChange selected.length:',selected.length);
    _fieldById('_p25_gridBotonRenovar').setDisabled(selected.length==0);
}

function _p25_polizaSelectionChange(selModel,selected,e)
{
    debug('>_p25_polizaSelectionChange selected.length:',selected.length);
    _fieldById('_p25_polizaBotonRenovar').setDisabled(selected.length==0);
}

function _p25_selectionCharge(field){
debug('>_p25_selectionCharge');
	if(field.getValue() == 'AS' || field.getValue() == 'SA'){
  		_fieldById('_p25_contratante').show();
  		_fieldById('_p25_poliza').show();
  		_fieldById('_p25_grid').hide();
  		_fieldById('_p25_gridCalendario').hide();
  		_fieldById('_p25_gridCondiciones').hide();
  	}else if(field.getValue() == 'P'){
  		_fieldById('_p25_contratante').hide();
  		_fieldById('_p25_poliza').hide();
  		_fieldById('_p25_grid').hide();
  		_fieldById('_p25_gridCalendario').show();
  		_fieldById('_p25_gridCondiciones').hide();
  	}else if(field.getValue() == 'M'){
  		_fieldById('_p25_contratante').hide();
  		_fieldById('_p25_poliza').hide();
  		_fieldById('_p25_grid').show();
  		_fieldById('_p25_gridCalendario').hide();
  		_fieldById('_p25_gridCondiciones').hide();
  	}
debug('<_p25_selectionCharge');
}

function _p25_limpiarFiltros(button,e)
{
    debug('>_p25_limpiarFiltros');
    var form = button.up('form');
    _fieldByName('tipo', form).enable();
    _fieldByName('anio', form).enable();
    _fieldByName('mes',  form).enable();
    var cdperson = _fieldByName('cdperson',form);
  	cdperson.store.proxy.extraParams['params.cdunieco'] = '';
  	cdperson.store.proxy.extraParams['params.cdramo']   = ''; 
    _fieldByName('fecini',form).setValue('');
  	_fieldByName('fecfin',form).setValue('');
  	_fieldByName('status',form).setValue('');
  	_fieldByName('cdunieco',form).setValue('');
  	_fieldByName('cdramo',form).setValue('');
  	_fieldByName('nmpoliza',form).setValue('');
  	_fieldByName('cdtipsit',form).setValue('');
  	_fieldByName('administradora',form).setValue('');
  	_fieldByName('retenedora',form).setValue('');
  	_fieldByName('cdperson',form).setValue('');
  	_fieldByName('anio',form).setValue('');
  	//_fieldByName('mes',form).setValue('');
    debug('<_p25_limpiarFiltros');
}

function _p25_ventanaAutoServicio(resRenova){
	debug('>_p25_ventanaAutoServicio');
	debug(resRenova);
	winAutoServicio.resRenova = resRenova;
	var dsTramite = _fieldById('dsTramite');
	dsTramite.setValue('Se cre\u00f3 el tramite '+ resRenova['ntramite']);
	winAutoServicio.show();
	debug('<_p25_ventanaAutoServicio');
}

function _p25_ventanaServicioAsistido(){
	debug('>_p25_ventanaServicioAsistido');
	winServicioAsistido.show();	
	debug('<_p25_ventanaServicioAsistido');
}

function _p25_ventanaCambioFormaPago(resRenova){
	debug('>_p25_ventanaCambioFormaPago');
	Ext.Ajax.request({
		url     : _p25_urlObtenerItemsTvalopol
		,params : 
		{
			'params.cdramo'  : resRenova['cdramo'],
			'params.cdtipsit': resRenova['cdtipsit']
		}
		,success : function(response){
			var resp = Ext.decode(response.responseText);
			debug('resp',resp);
			if(!Ext.isEmpty(resp.params.items)){
				var items = Ext.decode(resp.params.items);
				//var panelDatosAdicionales = _fieldById('panelDatosAdicionales');
				//panelDatosAdicionales.removeAll();
				//panelDatosAdicionales.add(items);
				_fieldById('winCambioPago').resRenova = resRenova;
				wineditarContratante.show();
				_p25_loaderContratante(resRenova['cdpersoncon']);
				_p25_cargarValoresComplementarios(resRenova);
			}
			else{
				mensajeError('No se pudieron obtener los atributos variables de poliza');
			}
		}
		,failure : function(){
				//window.setLoading(false);
				Ext.Msg.show({
					title	: 'Error',
					msg		: 'Error de comunicaci&oacute;n',
					buttons	: Ext.Msg.OK,
					icon	: Ext.Msg.ERROR
				});
		}
	});
	
	debug('<_p25_ventanaCambioFormaPago');
}

function _p25_loaderContratante(cdperson){
	debug('>_p25_loaderContratante ',cdperson);
	//_fieldById('_p25_clientePanel')
	_p25_clientePanel.getLoader().load({params : { 'smap1.cdperson' : cdperson}});
	debug('<_p25_loaderContratante');
}


function _p29_actualizarCotizacion(callback){
	debug('>_p29_actualizarCotizacion');
	var panelDatosPoliza 	  = _fieldById('panelDatosPoliza');
	//var panelDatosAdicionales = _fieldById('panelDatosAdicionales');
	var mapUpdate 	  		  = {};
	
	for(var i = 0; i < panelDatosPoliza.items.items.length; i++){
		var item = panelDatosPoliza.items.items[i];
		mapUpdate['params.'+item.name] = item.value;
	}
	
	/*for(var i = 0; i < panelDatosAdicionales.items.items.length; i++){
		var item = panelDatosAdicionales.items.items[i];
		mapUpdate['params.'+item.name.replace('parametros.','')] = item.value;
	}*/
	
	var panelDatos = _fieldById('panelDatos');
	mapUpdate['params.cdunieco'] 	 = wineditarContratante.resRenova['cdunieco'];
	mapUpdate['params.cdramo']		 = wineditarContratante.resRenova['cdramo'];
	mapUpdate['params.nmsuplem']	 = wineditarContratante.resRenova['nmsuplem'];
	mapUpdate['params.cdtipsit']	 = wineditarContratante.resRenova['cdtipsit'];
	mapUpdate['params.feefecto_ant'] = wineditarContratante.resRenova['feefecto_ant'];
	mapUpdate['params.cdagente'] 	 = wineditarContratante.resRenova['cdagente'];
	mapUpdate['params.cdmoneda'] 	 = wineditarContratante.resRenova['cdmoneda'];
	mapUpdate['params.cdcontra'] 	 = wineditarContratante.resRenova['cdpersoncon'];	
	_mask("Actualizando valores");	
	Ext.Ajax.request(
   		{
       		url      : _p25_urlActualizaValoresCotizacion,
       		params   : mapUpdate,
       		success  : function(response)
       		{
           		var resp = Ext.decode(response.responseText);
           		debug('resp',resp);
           		if(resp.success==true)
                {
           		    mostrarMensajeVentana('Se ha actualizado con exito.');
           			//mensajeCorrecto('Aviso','Se ha actualizado con exito.');
           			_unmask();
                }
           		else{
           		    mostrarMensajeVentana('Error en la validacion de session');
           			//mensajeError('Error en la validacion de session');
           			_unmask();
           		}
       		},
       		failure  : function()
       		{
           		errorComunicacion();
       		}
   		});
    /*var form=Ext.getCmp('formPanel');
    if(form.isValid())
    {
        //form.setLoading(true);
        form.submit({
            params:{
                'map1.pv_cdunieco' :  panDatComMap1.cdunieco,
                'map1.pv_cdramo' :    panDatComMap1.cdramo,
                'map1.pv_estado' :    panDatComMap1.estado,
                'map1.pv_nmpoliza' :  panDatComMap1.nmpoliza
            },
            success:function(){
                //form.setLoading(false);
                centrarVentanaInterna(Ext.Msg.show({
                    title    : 'Cambios guardados'
                    ,msg     : 'Sus cambios han sido guardados'
                    ,buttons : Ext.Msg.OK
                    ,fn      : function()
                    {
                    	 if(!Ext.isEmpty(callback))
                         {
                             callback();
                         }
                    }
                }));
            },
            failure:function(){
                //form.setLoading(false);
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
    }
    else
    {
        Ext.Msg.show({
            title:'Datos incompletos',
            msg: 'Favor de introducir todos los campos requeridos',
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.WARNING
        });
    }*/
    debug('<_p29_actualizarCotizacion');
}

function _p25_cargarValoresComplementarios(resRenova){
	debug('>_p25_cargarValoresComplementarios ',resRenova);
	var panelDatos 		= _fieldById('panelDatos');
	for (var i = 0; i < panelDatos.items.items.length; i++){
		var panel = panelDatos.items.items[i];
		for (var j = 0; j < panel.items.items.length; j++){
			var item = panel.items.items[j];
			var data = resRenova[item.name];
			if(!Ext.isEmpty(data)){
				item.setValue(data);
			}
		}
	}
	var tipoPoliza		= _fieldById('tipoPoliza');
	var formaPagoPoliza = _fieldById('formaPagoPoliza');
	tipoPoliza.getStore().load();
	//formaPagoPoliza.getStore().load({params : { 'catalogo' : 'FORMAS_PAGO_POLIZA_POR_RAMO_TIPSIT', 'params.cdramo' : resRenova['cdramo'], 'params.cdtipsit' : resRenova['cdtipsit']}});	
	formaPagoPoliza.getStore().load({params : { 'catalogo' : 'TIPOS_PAGO_POLIZA_SIN_DXN_MULTIANUAL'}});
	debug('<_p25_cargarValoresComplementarios');
}

_p22_parentCallback = function(json){
	debug('>_p22_parentCallback',json);
	wineditarContratante.resRenova['cdpersoncon'] = json.smap1['CDPERSON'];
	debug('wineditarContratante.resRenova ',wineditarContratante.resRenova);
	debug('<_p22_parentCallback');
}

function tarifaFinal(){
	debug('>tarifaFinal');
	debug('wineditarContratante',wineditarContratante);
	_mask('Preparando para confirmar');
	Ext.Ajax.request({
        url     : urlRecotizar,
        params :{
            'panel1.nmpoliza'   : wineditarContratante.resRenova['nmpoliza'],
            cdunieco            : wineditarContratante.resRenova['cdunieco'],
            cdramo              : wineditarContratante.resRenova['cdramo'],
            cdtipsit            : wineditarContratante.resRenova['cdtipsit'],
            'panel1.notarifica' : ( Number(wineditarContratante.resRenova['cdramo'])==16 || Number(wineditarContratante.resRenova['cdramo'])==6 || Number(wineditarContratante.resRenova['cdramo'])==5 ) ? 'si' : '',
            'panel1.renovacion' : 'S'
        },
        success : function(response){
            _unmask();
            var json=Ext.decode(response.responseText);
            debug('json ',json);
            if(!Ext.isEmpty(json.slist1) && Ext.isEmpty(json.mensajeRespuesta)){
            	if(json.slist1.length > 0){
			            datos = [];
			            for(var i = 0; i < json.slist1.length; i++){
			                datos.push({
			                    AGRUPADOR : json.slist1[i]['parentesco'],
			        		    COBERTURA : json.slist1[i]['Nombre_garantia'],
			        		    PRIMA     : json.slist1[i]['Importe']
			        	    });
			            }
			            debug('datos ',datos);
			            Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES + 'VentanaTarifa');
						new VentanaTarifa({
			    			title   : 'TARIFA DEL ENDOSO',
			    			datos   : datos,
			    			buttons : [
			    			    {
                                    xtype       : 'textfield'
                                    ,itemId     : 'numerofinalpoliza'
                                    ,fieldLabel : 'N&uacute;mero de poliza'
                                    ,readOnly   : true
                                },
			    				{
			    					xtype     : 'button',
			    					itemId    : 'botonEmitirPolizaFinal',
			    					text      : 'Emitir',
			    					icon      : contexto+'/resources/fam3icons/icons/award_star_gold_3.png',
			    					handler   : function(){
			    						_mask('Emitiendo Poliza');
			    						var panDatComBotonRetarificar = _fieldById('panDatComBotonRetarificar');
		            					var panDatComBotonGuardar     = _fieldById('panDatComBotonGuardar');
		            					panDatComBotonRetarificar.disable();
		            					panDatComBotonGuardar.disable();
			    						Ext.Ajax.request({
			                                url      : _p25_urlConfirmarPolizaIndividual,
			                                params   : {
			                                    'params.cdunieco' : wineditarContratante.resRenova['cdunieco'],
			                                	'params.cdramo'   : wineditarContratante.resRenova['cdramo'],
			                                	'params.estado'   : wineditarContratante.resRenova['estado'],
			                                	'params.nmpoliza' : wineditarContratante.resRenova['nmpoliza'],
			                                	'params.nmsuplem' : wineditarContratante.resRenova['nmsuplem'],
			                                	'params.ntramite' : wineditarContratante.resRenova['ntramite'],
			                                    'params.cdperpag' : wineditarContratante.resRenova['cdperpag'],
			                                	'params.feefecto' : wineditarContratante.resRenova['feefecto']
			                                },
			                                success  : function(response){
			                                	_unmask();
			                                    var resp = Ext.decode(response.responseText);
			                                   	var list = resp.slist1;
			                                   	debug('resp',resp);
			                                	if(resp.success==true && list.length > 0){
			                                		wineditarContratante.resRenova['nmpolizaNew'] = list[0]['nmpolizaNew'];
			                                		wineditarContratante.resRenova['nmsuplemNew'] = list[0]['nmsuplemNew'];
			                                		_fieldById('botonEmitirPolizaFinal').disable();
			                                		_fieldById('botonImprimirPolizaFinal').enable();
			                                		_fieldById('botonEmitirPolizaFinalPreview').hide();
			                                		_fieldById('venDocVenEmiBotCancelar').setText('Regresar a Menú Principal');
			                                		_fieldById('numerofinalpoliza').setValue(list[0]['nmpoliex']);
			                                	    mensajeCorrecto('Aviso',resp.respuesta);
			                                	}
			                                	else{
			                                		mensajeError(resp.respuesta);
			                                	}
			                                },
			                                failure  : function(){
			                                	_unmask();
			                                    errorComunicacion();
			                                }
			                            });
			    					}
			    				},
			    				{
									xtype     : 'button'
				                    ,itemId   : 'botonImprimirPolizaFinal'
				                    ,text     : 'Imprimir'
				                    ,icon     : contexto+'/resources/fam3icons/icons/printer.png'
				                    ,disabled : true
				                    ,handler  : function(me){
				                        debug(wineditarContratante.resRenova);
				                        var callbackRemesa = function(){
				                            Ext.create('Ext.window.Window',{
				                                title       : 'Documentos del tr&aacute;mite '+ wineditarContratante.resRenova['ntramite'],
				                                modal       : true,
				                                buttonAlign : 'center',
				                                width       : 600,
				                                height      : 400,
				                                autoScroll  : true,
				                                cls         : 'VENTANA_DOCUMENTOS_CLASS',
				                                loader      : {
				                                    url       : panDatComUrlDoc2
				                                    ,params   : {
				                                        'smap1.nmpoliza' : wineditarContratante.resRenova['nmpolizaNew'],
				                                        'smap1.cdunieco' : wineditarContratante.resRenova['cdunieco'],
				                                        'smap1.cdramo'   : wineditarContratante.resRenova['cdramo'],
				                                        'smap1.estado'   : 'M',
				                                        'smap1.nmsuplem' : '0',
				                                        'smap1.ntramite' : wineditarContratante.resRenova['ntramite'],
				                                        'smap1.nmsolici' : wineditarContratante.resRenova['nmsolici'],
				                                        'smap1.tipomov'  : '0'
				                                     },
				                                     scripts  : true,
				                                     autoLoad : true
				                                 }
				                             }).show();
				                         };
				                         _generarRemesaClic(
				                             false
				                             ,wineditarContratante.resRenova['cdunieco']
				                             ,wineditarContratante.resRenova['cdramo']
				                             ,'M'
				                             ,wineditarContratante.resRenova['nmpolizaNew']
				                             ,callbackRemesa
				                             );
				                     }
				                },
				                {
			    					xtype    : 'button'
			    					,itemId  : 'botonEmitirPolizaFinalPreview'
			    					,text    : 'Vista previa'
			    					,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
			    					,hidden  : panDatComMap1.cdramo=='6'
			    					,handler : function()
			    					{
			    						var nombre_caratula = getNombreReporteCaratura(wineditarContratante.resRenova['cdtipsit']);
			    						var me=this;
			    						var urlRequestImpCotiza=urlServidorReports
			    						+'?destype=cache'
			    						+"&desformat=PDF"
			    						+"&report="+nombre_caratula
			    						+"&paramform=no"
			    						+"&userid="+complerepSrvUsr
			    						+"&ACCESSIBLE=YES" //parametro que habilita salida en PDF
			    						+'&p_unieco='+wineditarContratante.resRenova['cdunieco']
			    						+'&p_estado=W'
			    						+'&p_ramo='+wineditarContratante.resRenova['cdramo']
			    						+'&p_poliza='+wineditarContratante.resRenova['nmpoliza']
			    						debug(urlRequestImpCotiza);
			    						var numRand=Math.floor((Math.random()*100000)+1);
			    						debug(numRand);
			    						var windowVerDocu=Ext.create('Ext.window.Window',
			    						{
			    							title          : 'Vista previa'
			    							,width         : 700
			    							,height        : 500
			    							,collapsible   : true
			    							,titleCollapse : true
			    							,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
			    											 +'src="'+compleUrlViewDoc+"?contentType=application/pdf&url="+encodeURIComponent(urlRequestImpCotiza)+"\">"
			    											 +'</iframe>'
			    							,listeners     :
			    							{
			    								resize : function(win,width,height,opt){
			    									debug(width,height);
			    									$('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
			    								}
			    							}
			    						}).show();
			    						windowVerDocu.center();
			    					}
			    				},
			    				{
				                    xtype   : 'button',
				                    itemId  : 'venDocVenEmiBotCancelar',
				                    text    : 'Cancelar',
				                    icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
				                    handler : function(){
				                        if (Ext.isEmpty(_fieldById('numerofinalpoliza').getValue()) === true){
				                            var me=this;
                                            me.up().up().destroy();
				                        }
				                        else{
				                            location.reload();
				                        }				                        
				                    }
				                }
			    			],
			    			listeners : {
                                afterrender : function(me){
                                    try{
                                        debug('mensaje agente activo',json.panel1['mensajeAgenteActivo']);
                                        if(!Ext.isEmpty(json.panel1['mensajeAgenteActivo'])){
                                            setTimeout(function(){ 
                                                mensajeWarning(json.panel1['mensajeAgenteActivo']); 
                                            }, 3000);
                                        }
                                    }
                                    catch(e){
                                        debugError(e);
                                    }
                                }
                            }
							}).mostrar();
            	}
            }
            else{
            	/* if(json.mensajeRespuesta === 'Favor de verificar los datos adicionales correspondientes al Descuento por Nómina'){ */            	    
            	    mostrarMensajeVentana('El tramite # ' + wineditarContratante.resRenova['ntramite'] + ' se turnara a suscripción para completar información.');
            	    setTimeout(function(){ 
            	        turnar(wineditarContratante.resRenova['ntramite']) 
            	    }, 3000);
            	/* }
            	else{
            	    mensajeError(json.mensajeRespuesta);
            	} */
            }
        }
        ,failure : function()
        {
            _unmask();
            Ext.Msg.show({
                title:'Error',
                msg: 'Error de comunicaci&oacute;n',
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });
        }
    });
    debug('<tarifaFinal');
}

function actualizaCondicion(record){
	debug('>actualizaCondicion',record);	
	var form = panCondicion.down('form');
	_fieldByName('anio',form).readOnly = true;
	_fieldByName('mes',form).readOnly = true;
	_fieldByName('campo',form).readOnly = true;
	var criterio = record.data['criterio'];
	if(criterio == 2){
		_fieldByName('valor2',form).enable();
	}
	else{
		_fieldByName('valor2',form).disable();
	}
	panCondicion['operacion'] = 'A';
	panCondicion.setTitle('Editar condicion');
	panCondicion.show();
	debug(panCondicion.down('form'));
	panCondicion.down('form').loadRecord(record);
	_p25_gridCondiciones.store.reload;
	debug('<actualizaCondicion',record);
}

function borraCondicion(record){
	debug('>borraCondicion',record);	
	var _p25_condiciones = _fieldById('_p25_condiciones');
  	debug(_p25_condiciones.form.getValues());
  	var datos = record.data; 
  	var params = {};
  	for(var k in datos){
  		params['params.'+k] = datos[k]; 
  	}
  	params['params.nmperiod']  = record.data['nmperiod'];
  	params['params.cdunieco']  = record.data['cdunieco'];
  	params['params.cdramo']    = record.data['cdramo'];
  	params['params.operacion'] = 'B';
  	debug('params',params);
  	_mask('Eliminando condicion');
  	Ext.Ajax.request({
        url       : _p25_urlMovimientoCondiciones,
        params    : params,
        success  : function(response){
	        _unmask();
	        var resp = Ext.decode(response.responseText);
	        debug('resp',resp);
	        if(resp.exito == true){	        
	        	mensajeCorrecto('Mensaje','Borrado con exito');
	        	var _p25_gridCondiciones = _fieldById('_p25_gridCondiciones');
	        	_p25_gridCondiciones.store.reload();
	        }
	        else{
	        	mensajeError(resp.respuesta);
	        }
        },
        failure  : function(){
        	_unmask();
            errorComunicacion();
        }
    });
	debug('<borraCondicion',record);
}

function agregaCondicion(){
	debug('>agregaCondicion');
	panCondicion['operacion'] = 'I';
	panCondicion.setTitle('Agregar exclusi\u00f3n');
	var form = panCondicion.down('form');
	debug(_fieldByName('anio',form));
	var data = _fieldById('_p25_gridCalendario').getSelectionModel().getSelection()[0].data;
	debug('data',data);
	_fieldByName('anio',     form).setValue(data['anio']);
	_fieldByName('mes',      form).setValue(data['mes']);
	_fieldByName('nmperiod', form).setValue(data['nmperiod']);
	_fieldByName('anio',     form).readOnly = true;
	_fieldByName('mes',      form).readOnly = true;
	_fieldByName('nmperiod', form).readOnly = true;
	_fieldByName('campo',    form).readOnly = false;	
	_fieldByName('valor2',   form).readOnly = false;
	panCondicion.show();
	debug('<agregaCondicion');
}

function getNombreReporteCaratura(cdtipsit){
	debug('>getNombreReporteCaratura',cdtipsit);
	var _NOMBRE_REPORTE_CARATULA;
	if(cdtipsit == 'SL' || cdtipsit == 'SN'){
        _NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.nombre" />';
    }
    else if(cdtipsit == 'GMI'){
        _NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.gmi.nombre" />';
    }
    else if(cdtipsit == 'MS'){
        _NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.ms.nombre" />';
    }
    else if(cdtipsit == 'RI'){
        _NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.ri.nombre" />';
    }
	debug('<getNombreReporteCaratura',_NOMBRE_REPORTE_CARATULA);
    return _NOMBRE_REPORTE_CARATULA;
}

function actualizaCalendario(record){
	debug('>actualizaCalendario',record);
	panCalendario.nmperiod = record.raw['nmperiod'];
	var form = panCalendario.down('form');
	_fieldByName('anio',form).readOnly = true;
	_fieldByName('mes', form).readOnly = true;	
	panCalendario['operacion'] = 'A';
	panCalendario.setTitle('Editar');
	_fieldByName('feaplica', form).setMinValue(Ext.Date.add(new Date(), Ext.Date.DAY, 1));
	panCalendario.show();
	debug('panCalendario',form);
	form.loadRecord(record);
	var cdunieco = _fieldByName('cdunieco', form);
	if(Ext.isEmpty(cdunieco.getValue())){
	    _fieldByName('cdunieco', form).setValue('-1');
	}
	var cdramo = _fieldByName('cdramo', form);
    if(Ext.isEmpty(cdramo.getValue())){
        _fieldByName('cdramo', form).setValue('-1');
    }
	var _p25_gridCalendario = _fieldById('_p25_gridCalendario');
	_p25_gridCalendario.store.reload;
	debug('<actualizaCalendario',record);
}

function borraCalendario(record){
	debug('>borraCalendario',record);	
	var _p25_calendario = _fieldById('_p25_calendario');
  	debug(_p25_calendario.form.getValues());
  	var datos = record.data; 
  	var params = {};
  	for(var k in datos){
  		params['params.'+k] = datos[k]; 
  	}
  	params['params.nmperiod'] = record.raw['nmperiod'];
  	params['params.operacion'] = 'B';
  	debug('params',params);
  	_mask('Eliminando');
  	Ext.Ajax.request({
        url       : _p25_urlMovimientoCalendario,
        params    : params,
        success  : function(response){
	        _unmask();
	        var resp = Ext.decode(response.responseText);
	        debug('resp',resp);
	        if(resp.exito == true){	        
	        	mensajeCorrecto('Mensaje','Borrado con exito');
	        	var _p25_gridCalendario = _fieldById('_p25_gridCalendario');
	        	_p25_gridCalendario.store.reload();
	        }
	        else{
	        	mensajeError(resp.respuesta);
	        }
        },
        failure  : function(){
        	_unmask();
            errorComunicacion();
        }
    });
	debug('<borraCalendario',record);
}

function agregaCalendario(){
	debug('>agregaCalendario');
	panCalendario['operacion'] = 'I';
	panCalendario.setTitle('Agregar');	
	var form = panCalendario.down('form');
	debug(_fieldByName('anio',form));
	var formBusqueda = _fieldById('_p25_busquedaForm');
	debug('formBusqueda',formBusqueda);
	debug('feinicio',_fieldByName('feinicio', form));
	_fieldByName('anio',form).setValue(_fieldByName('anio', formBusqueda).getValue());
	if(!Ext.isEmpty(_fieldByName('mes', formBusqueda).getValue()) && _fieldByName('mes', formBusqueda).getValue() != -1){
	   _fieldByName('mes',form).setValue(_fieldByName('mes', formBusqueda).getValue());
	   _fieldByName('mes',form).readOnly = true;
	   var anio     = _fieldByName('anio',form).getValue();
       var feinicio = toDate(anio, _fieldByName('mes',form).getValue(), 1);
       _fieldByName('feinicio',form).setValue(feinicio);
       feinicio = Ext.Date.add(feinicio, Ext.Date.DAY, -1);
       _fieldByName('feaplica',form).setValue(feinicio);             
       feinicio = Ext.Date.add(feinicio, Ext.Date.MONTH, 1);
       _fieldByName('fefinal', form).setValue(feinicio);
	}
	else{
	   _fieldByName('mes',form).readOnly = false;
	}
	_fieldByName('feaplica', form).setMinValue(Ext.Date.add(new Date(), Ext.Date.DAY,   1));	
	_fieldByName('anio',formBusqueda).readOnly = true;
	_fieldByName('mes', formBusqueda).readOnly = true;
	panCalendario.show();	
	debug('<agregaCalendario');
}
    
 function limpiarValoresCalendario(form){
    debug('>limpiarValoresCalendario', form);    
    _fieldByName('cdunieco',form).setValue('');
    _fieldByName('cdramo',  form).setValue('');
    _fieldByName('feaplica',form).setValue('');
    _fieldByName('feinicio',form).setValue('');
    _fieldByName('fefinal', form).setValue('');
    debug('<limpiarValoresCalendario');
 }

 function mostrarMensajeVentana(mensaje){
    var winMensaje = Ext.create('Ext.window.Window', {
        title       : 'Mensaje',
        itemId      : 'winMensaje',
        layout      : 'fit',
        closeAction : 'hide',
        items       : [
            Ext.create('Ext.panel.Panel', {
                width       : 200,
                html        : '<p align = "center">'+mensaje+'</p>',
                buttons     :   [
                    { 
                        text    : 'Aceptar',
                        handler : function(){
                            _fieldById('winMensaje').close();
                        }
                    }
                ]
            })
        ]
    }).show();
}

 function mostrarCondiciones(){
    var _p25_gridCalendario = _fieldById('_p25_gridCalendario');
    if(_p25_gridCalendario.getSelectionModel().hasSelection()){
        debug('_p25_gridCalendario',_p25_gridCalendario.getSelectionModel().getSelection()[0].raw);
        var calen = _p25_gridCalendario.getSelectionModel().getSelection()[0].raw;
        _p25_storeCondiciones.load({
        params    : {
            'smap1.nmperiod' : calen['nmperiod'],
            'smap1.cdunieco' : calen['cdunieco'],
            'smap1.cdramo'   : calen['cdramo'],
            'smap1.anio'     : calen['anio'],
            'smap1.mes'      : calen['mes']
        },
        callback :  function(recordsCon, opCon, successCon){
            debug('entro a callback');
            if(successCon){
                var respCon = Ext.decode(opCon.response.responseText);
                debug('respCon',respCon);
                if(respCon.exito != true){
                    mensajeWarning('No hay resultados');
                }
                else{
                    var _p25_gridCondiciones      = _fieldById('_p25_gridCondiciones');
                    _p25_gridCondiciones.nmperiod = calen['nmperiod'];
                    _p25_gridCondiciones.cdunieco = calen['cdunieco'];
                    _p25_gridCondiciones.cdramo   = calen['cdramo'];
                    _fieldById('_p25_gridCondiciones').show();
                }
            }
            else{
                mensajeError(opCon.getError());
            }
        }
        });
    }
    else{
        mensajeWarning('Debe seleccionar un registro');
    }
 }
 
 function daydiff(first, second) {
     return Math.round((second-first)/(1000*60*60*24));
 }
 
 function guardarCalendario(){
     var _p25_calendario = _fieldById('_p25_calendario');
     var datos = _p25_calendario.form.getValues();
     var params = {};
     debug('_p25_calendario.form',_p25_calendario.form);
     var i = 0;
     for(var k in datos){
         debug('datos[k]',datos[k]);
         params['params.'+k] = datos[k];
         i++;
     }
     params['params.operacion'] = panCalendario['operacion'];
     params['params.nmperiod']  = panCalendario.nmperiod;     
     params['params.cdunieco']  = params['params.cdunieco'] == -1 ? null : params['params.cdunieco'];
     params['params.cdramo']    = params['params.cdramo']   == -1 ? null : params['params.cdramo'];
     debug('params',params);
     _mask('Guardando cambios');
     Ext.Ajax.request({
         url       : _p25_urlMovimientoCalendario,
         params    : params,
         success  : function(response){
             _unmask();
             var resp = Ext.decode(response.responseText);
             debug('resp',resp);                     
             if(resp.exito == true){
                 mensajeCorrecto('Mensaje','Guardado con exito');
                 var _p25_gridCalendario = _fieldById('_p25_gridCalendario');
                 _p25_gridCalendario.store.reload();
                 panCalendario.close();
             }
             else{
                 mensajeError(resp.respuesta);
             }
         },
         failure  : function(){
             _unmask();
             errorComunicacion();
         }
     });
 }
 
 function isValidDate(date){
     debug('>isValidDate',date);
     var isDate = false;
     var dateArr = date.split('/');
     if(dateArr.length === 3){
        isDate = Ext.Date.isValid(dateArr[2],dateArr[1],dateArr[0]);
     }
     debug('<isValidDate',isDate);
     return isDate;
 }
 
 function tarifaFinalMismasCondiciones(resRenova){
     debug('>tarifaFinalMismasCondiciones',resRenova);
     _mask('Preparando para confirmar');
     Ext.Ajax.request({
         url     : urlRecotizar,
         params :{
             'panel1.nmpoliza'   : resRenova['nmpoliza'],
             cdunieco            : resRenova['cdunieco'],
             cdramo              : resRenova['cdramo'],
             cdtipsit            : resRenova['cdtipsit'],
             'panel1.notarifica' : ( Number(resRenova['cdramo'])==16 || Number(resRenova['cdramo'])==6 || Number(resRenova['cdramo'])==5 ) ? 'si' : '',
             'panel1.renovacion' : 'S'
         },
         success : function(response){
             _unmask();
             var json=Ext.decode(response.responseText);
             debug('json ',json);
             if(!Ext.isEmpty(json.slist1) && Ext.isEmpty(json.mensajeRespuesta)){
                 if(json.slist1.length > 0){
                     datos = [];
                     for(var i = 0; i < json.slist1.length; i++){
                         datos.push({
                             AGRUPADOR : json.slist1[i]['parentesco'],
                             COBERTURA : json.slist1[i]['Nombre_garantia'],
                             PRIMA     : json.slist1[i]['Importe']
                         });
                     }
                     debug('datos ',datos);
                     Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES + 'VentanaTarifa');
                     new VentanaTarifa({
                         title   : 'TARIFA DEL ENDOSO',
                         datos   : datos,
                         buttons : [
                             {
                                 xtype      : 'textfield',
                                 itemId     : 'numerofinalpoliza',
                                 fieldLabel : 'N&uacute;mero de poliza',
                                 readOnly   : true
                             },
                             {
                                 xtype     : 'button',
                                 itemId    : 'botonEmitirPolizaFinal',
                                 text      : 'Emitir',
                                 icon      : contexto+'/resources/fam3icons/icons/award_star_gold_3.png',
                                 handler   : function(){
                                     _mask('Emitiendo Poliza');
                                     var panDatComBotonRetarificar = _fieldById('panDatComBotonRetarificar');
                                     var panDatComBotonGuardar     = _fieldById('panDatComBotonGuardar');
                                     panDatComBotonRetarificar.disable();
                                     panDatComBotonGuardar.disable();
                                     Ext.Ajax.request({
                                         url      : _p25_urlConfirmarPolizaIndividual,
                                         params   : {
                                             'params.cdunieco' : resRenova['cdunieco'],
                                             'params.cdramo'   : resRenova['cdramo'],
                                             'params.estado'   : resRenova['estado'],
                                             'params.nmpoliza' : resRenova['nmpoliza'],
                                             'params.nmsuplem' : resRenova['nmsuplem'],
                                             'params.ntramite' : resRenova['ntramite'],
                                             'params.cdperpag' : resRenova['cdperpag'],
                                             'params.feefecto' : resRenova['feefecto']
                                         },
                                         success  : function(response){
                                             _unmask();
                                             var resp = Ext.decode(response.responseText);
                                             var list = resp.slist1;
                                             debug('resp',resp);
                                             if(resp.success==true && list != null){
                                                 resRenova['nmpolizaNew'] = list[0]['nmpolizaNew'];
                                                 resRenova['nmsuplemNew'] = list[0]['nmsuplemNew'];
                                                 _fieldById('botonEmitirPolizaFinal').disable();
                                                 _fieldById('botonImprimirPolizaFinal').enable();
                                                 _fieldById('botonEmitirPolizaFinalPreview').hide();
                                                 _fieldById('venDocVenEmiBotCancelar').setText('Regresar a Menú Principal');
                                                 _fieldById('numerofinalpoliza').setValue(list[0]['nmpoliex']);
                                                 mensajeCorrecto('Aviso',resp.respuesta);
                                             }
                                             else{
                                                 mensajeError(resp.respuesta);
                                             }
                                         },
                                         failure  : function(){
                                             _unmask();
                                             errorComunicacion();
                                         }
                                     });
                                 }
                             },
                             {
                                 xtype    : 'button',
                                 itemId   : 'botonImprimirPolizaFinal',
                                 text     : 'Imprimir',
                                 icon     : contexto+'/resources/fam3icons/icons/printer.png',
                                 disabled : true,
                                 handler  : function(me){
                                     debug(wineditarContratante.resRenova);
                                     var callbackRemesa = function(){
                                         Ext.create('Ext.window.Window',{
                                             title       : 'Documentos del tr&aacute;mite '+ resRenova['ntramite'],
                                             modal       : true,
                                             buttonAlign : 'center',
                                             width       : 600,
                                             height      : 400,
                                             autoScroll  : true,
                                             cls         : 'VENTANA_DOCUMENTOS_CLASS',
                                             loader      : {
                                                 url       : panDatComUrlDoc2,
                                                 params   : {
                                                     'smap1.nmpoliza' : resRenova['nmpolizaNew'],
                                                     'smap1.cdunieco' : resRenova['cdunieco'],
                                                     'smap1.cdramo'   : resRenova['cdramo'],
                                                     'smap1.estado'   : 'M',
                                                     'smap1.nmsuplem' : '0',
                                                     'smap1.ntramite' : resRenova['ntramite'],
                                                     'smap1.nmsolici' : resRenova['nmsolici'],
                                                     'smap1.tipomov'  : '0'
                                                 },
                                                 scripts  : true,
                                                 autoLoad : true
                                             }
                                         }).show();
                                     };
                                     _generarRemesaClic(
                                         false,
                                         resRenova['cdunieco'],
                                         resRenova['cdramo'],
                                         'M',
                                         resRenova['nmpolizaNew'],
                                         callbackRemesa);
                                 }
                             },
                             {
                                 xtype   : 'button',
                                 itemId  : 'botonEmitirPolizaFinalPreview',
                                 text    : 'Vista previa',
                                 icon    : '${ctx}/resources/fam3icons/icons/zoom.png',
                                 hidden  : panDatComMap1.cdramo=='6',
                                 handler : function(){
                                     var nombre_caratula = getNombreReporteCaratura(resRenova['cdtipsit']);
                                     var me=this;
                                     var urlRequestImpCotiza=urlServidorReports
                                     +'?destype=cache'
                                     +"&desformat=PDF"
                                     +"&report="+nombre_caratula
                                     +"&paramform=no"
                                     +"&userid="+complerepSrvUsr
                                     +"&ACCESSIBLE=YES" //parametro que habilita salida en PDF
                                     +'&p_unieco='+resRenova['cdunieco']
                                     +'&p_estado=W'
                                     +'&p_ramo='+resRenova['cdramo']
                                     +'&p_poliza='+resRenova['nmpoliza']
                                     debug(urlRequestImpCotiza);
                                     var numRand=Math.floor((Math.random()*100000)+1);
                                     debug(numRand);
                                     var windowVerDocu=Ext.create('Ext.window.Window',{
                                         title          : 'Vista previa'
                                         ,width         : 700
                                         ,height        : 500
                                         ,collapsible   : true
                                         ,titleCollapse : true
                                         ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
                                             +'src="'+compleUrlViewDoc+"?contentType=application/pdf&url="+encodeURIComponent(urlRequestImpCotiza)+"\">"
                                             +'</iframe>'
                                         ,listeners     : {
                                             resize : function(win,width,height,opt){
                                                 debug(width,height);
                                                 $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
                                             }
                                         }
                                     }).show();
                                     windowVerDocu.center();
                                 }
                             },
                             {
                                 xtype   : 'button',
                                 itemId  : 'venDocVenEmiBotCancelar',
                                 text    : 'Cancelar',
                                 icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
                                 handler : function(){
                                     if (Ext.isEmpty(_fieldById('numerofinalpoliza').getValue()) === true){
                                         var me=this;
                                         me.up().up().destroy();
                                     }
                                     else{
                                         location.reload();
                                     }                                       
                                 }
                             }
                         ],
                         listeners : {
                             afterrender : function(me){
                                 try{
                                     debug('mensaje agente activo',json.panel1['mensajeAgenteActivo']);
                                     if(!Ext.isEmpty(json.panel1['mensajeAgenteActivo'])){
                                         setTimeout(function(){ 
                                             mensajeWarning(json.panel1['mensajeAgenteActivo']); 
                                         }, 3000);
                                     }
                                 }
                                 catch(e){
                                     debugError(e);
                                 }
                             }
                         }
                     }).mostrar();
                 }
             }
             else{
                 mostrarMensajeVentana('El tramite # ' + resRenova['ntramite'] + ' se turnara a suscripción para completar información.');
                 setTimeout(function(){ 
                     turnar(resRenova['ntramite']) 
                 }, 3000);
                 /* mensajeError(json.mensajeRespuesta); */
             }
         },
         failure : function(){
             _unmask();
             Ext.Msg.show({
                 title:'Error',
                 msg: 'Error de comunicaci&oacute;n',
                 buttons: Ext.Msg.OK,
                 icon: Ext.Msg.ERROR
             });
         }
     });
     debug('<tarifaFinalMismasCondiciones');
 }
 
 function toDate(year, month, day){
     debug('>toDate');
     var fecha;
     try{
        var mes = Number(month)-1;
        fecha = new Date(year, mes, day);
     }
     catch(err){
        debug('error al convertir fecha',err);
        return null;
     } 
     return fecha;
     debug('<toDate');
 }
 
 function turnar(ntramite){
     if (_GLOBAL_CDSISROL == RolSistema.Agente || _GLOBAL_CDSISROL == RolSistema.EjecutivoVenta || _GLOBAL_CDSISROL == RolSistema.MesaControl){
         var callbackNormal = function (callback) {
             mensajeCorrecto(
                 'Tr\u00e1mite generado',
                 'Se ha generado el tr\u00e1mite ' + ntramite +
                     ', favor de revisar los requisitos y subir sus documentos antes de turnar a SUSCRIPCI\u00d3N',
                     callback
                 );
             };
             var mask, ck = 'Recuperando lista de requisitos';
             try {
                 ck = 'Recuperando validaci\u00f3n ligada a requisitos';
                 mask = _maskLocal(ck);
                 Ext.Ajax.request({
                     url     : _GLOBAL_URL_RECUPERACION,
                         params  : {
                         'params.consulta' : 'RECUPERAR_VALIDACION_POR_CDVALIDAFK',
                         'params.ntramite' : ntramite,
                         'params.clave'    : '_CONFREN'
                     },
                     success : function (response) {
                         mask.close();
                         var ck = 'Decodificando respuesta al recuperar validaci\u00f3n ligada a requisitos';
                         try {
                             var valida = Ext.decode(response.responseText);
                             debug('### validacion ligada a checklist:', valida);
                             if (valida.success === true) {
                                 if (valida.list.length > 0) {
                                     _cargarAccionesEntidad(
                                         valida.list[0].CDTIPFLU,
                                         valida.list[0].CDFLUJOMC,
                                         valida.list[0].TIPOENT,
                                         valida.list[0].CDENTIDAD,
                                         valida.list[0].WEBID,
                                         function (acciones) {
                                             if (acciones.length > 0) {
                                                 debug('acciones:', acciones);
                                                 callbackNormal(function () {
                                                     _procesaAccion(
                                                         acciones[0].CDTIPFLU,
                                                         acciones[0].CDFLUJOMC,
                                                         acciones[0].TIPODEST,
                                                         acciones[0].CLAVEDEST,
                                                         acciones[0].WEBIDDEST,
                                                         acciones[0].AUX,
                                                         valida.params.ntramite,
                                                         valida.list[0].STATUS,
                                                         null, //cdunieco
                                                         null, //cdramo
                                                         null, //estado
                                                         null, //nmpoliza
                                                         null, //nmsituac
                                                         null, //nmsuplem
                                                         valida.list[0].cdusuari,
                                                         valida.list[0].cdsisrol,
                                                         null // callback
                                                     );
                                                 });
                                             } 
                                             else {
                                                 callbackNormal();
                                             }
                                         }
                                     );
                                 } 
                                 else {
                                     callbackNormal();
                                 }
                             } 
                             else {
                                 mensajeError(json.message);
                             }
                         } 
                         catch (e) {
                             manejaException(e, ck);
                         }
                     },
                     failure : function () {
                         mask.close();
                         errorComunicacion(null, 'Error al recuperar validaci\u00f3n ligada a requisitos');
                     }
                 });
             } 
             catch (e) {
                 manejaException(e, ck, mask);
                 callbackNormal();
             }
         }
         else{                               
             Ext.Ajax.request({
                 url     : _GLOBAL_COMP_URL_TURNAR
                 ,params : {
                     'params.NTRAMITE'  : ntramite,
                     'params.CDTIPFLU'  : winAutoServicio.resRenova['cdtipflu'],
                     'params.CDFLUJOMC' : winAutoServicio.resRenova['cdflujomc'],
                     'params.STATUSOLD' : winAutoServicio.resRenova['estadomc'],
                     'params.STATUSNEW' : '43'
                 }
                 ,success : function(response){
                     _unmask();
                     var ck = '';
                     try{
                         var json = Ext.decode(response.responseText);
                         debug('### turnar:',json);
                         if(json.success){
                             _iceMesaControl(ntramite);                                                      
                         }
                         else{
                             mensajeError(json.message);
                         }
                     }
                     catch(e){
                         manejaException(e,ck);
                     }
                 }
                 ,failure : function(){
                       _unmask();
                       errorComunicacion(null,'Error al turnar tr\u00e1mite');
                 }
             });
         }
 }
 
 function setTextHelpValue(campo){
     debug('>setTextHelpValue',campo);
     var ayuda = "";
     if(campo === 'CDSUBRAM'){
         ayuda = "Ramos de salud individual<br/>Formato: numerico 3 digitos<br/>Ejemplo:210";
     }
     else if(campo === 'CDAGENTE'){
         ayuda = "Codigo de agente<br/>Formato: numerico<br/>Ejemplo:0";
     }
     else if(campo === 'FEPROREN'){
         ayuda = "Fecha<br/>Formato: fecha simple DD/MM/YYYY<br/>Ejemplo:01/01/2017";
     }
     else if(campo === 'NMPOLIEX'){
         ayuda = "Poliza<br/>Formato: No. sucursal a 4 digitos(1000)+No. Ramo a 3 digitos(210)+No. Poliza a 6 digistos(001114)+No. renovacion a 6 digitos(000000)<br/>Ejemplo:1000210001114000000";
     }
     else if(campo === 'CDPERSON'){
         ayuda = "Codigo de contratante<br/>Formato: numerico<br/>Ejemplo:524003";
     }
     else if(campo === 'CDRETEN'){
         ayuda = "Codigo de reteneroda<br/>Formato: No. sucursal a 4 digitos(1000)+Codigo retenedora a 3 digitos(004)<br/>Ejemplo:1000004";
     }
     else if(campo === 'CDUNIECO'){
         ayuda = "Codigo de sucursal<br/>Formato: numerico<br/>Ejemplo:1000";
     }
     idSuggestValue = _fieldById("idSuggestValue");
     idSuggestValue.setValue(ayuda);
     debug('<setTextHelpValue',campo);
 }

////// funciones //////
</script>
</head>
<body><div id="_p25_divpri" style="height:600px;"></div></body>
</html>