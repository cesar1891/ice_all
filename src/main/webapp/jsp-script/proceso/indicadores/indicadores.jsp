<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<style type="text/css">
			.ingresados .x-panel-header {
				background-color: #0B0B61;
			}
			
			.ingresados .x-panel-body {
				font: normal 40px courier !important;
			}
			
			.procesados .x-panel-header {
				background-color : #088A08;
			}
			
			.procesados .x-panel-body {
				font: normal 40px courier !important;
			}
			
			.eficacia {
				font-size: 40px;
			}
			
			
			.pendientes .x-panel-header {
				background-color : #DF0101;
			}
			
			.pendientes .x-panel-body {
				font: normal 40px courier !important;
			}
			
			.my-btn .x-btn-inner {
			    color: #000;
			    font-size: 40px;
				background-color : #FFF;
			    //font: normal 40px courier !important;
			}
		</style>
		
		<script>
		
		////// urls //////
		
		var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
		var _CAT_IDCIERRES      = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@IDCIERRES"/>';
		//
		var _CAT_ZONAS_SUCURSALES = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
		//var _CAT_RAMOS = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS"/>';
		var _CAT_RAMOS = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS_X_TIPORAMO"/>';
		var _CAT_SUBRAMOS = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SUBRAMOS"/>';
		var _CAT_AGENTES = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTES"/>';
		var _CAT_TRAMITES = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_TIPOS_TRAMITE"/>';
		
		////// urls //////
		
		////// variables //////
		
		var _itemsPerPage = 20;
		
		////// variables //////
		
		////// overrides //////
		////// overrides //////
		
		////// componentes dinamicos //////
		////// componentes dinamicos //////
		
		Ext.onReady(function() {
			
			
		    ////// modelos //////
		    
		    Ext.define('IdTipoSeguroModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'cdtipram'},
                    {type:'string', name:'dstipram'}
                ]
            });
		    
		    Ext.define('IdcierresModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'idcierre'},
                    {type:'string', name:'fecha_cierre'}
                ]
            });
		    
		    Ext.define('IdRamoPadreModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'key'},
                    {type:'string', name:'value'}
                ]
            });
		    
		    Ext.define('IdRamosModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'key'},
                    {type:'string', name:'value'}
                ]
            });
		    
		    Ext.define('IdAgentesModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'key'},
                    {type:'string', name:'value'}
                ]
            });
		    
		    Ext.define('IdSucursalesModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'key'},
                    {type:'string', name:'value'}
                ]
            });
		    
		    Ext.define('IdTramitesModel', {
                extend: 'Ext.data.Model',
                fields: [
                    {type:'string', name:'key'},
                    {type:'string', name:'value'}
                ]
            });
		    
			Ext.define('AseguradosModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'ingresados'},
		            {type:'string', name:'procesados'},
		            {type:'string', name:'pendientes'},
		            {type:'string', name:'eficacia'}
		        ]
		    });
		    
		    Ext.define('PendientesModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'undia'},
		            {type:'string', name:'dosdias'},
		            {type:'string', name:'tresdias'},
		            {type:'string', name:'cuatrodias'},
		            {type:'string', name:'cincodias'},
		            {type:'string', name:'mascincodias'},
		            {type:'string', name:'total'}
		        ]
		    });
		    
		    Ext.define('TramitesPorLineaNegocioModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'CD_LINEA_NEGOCIO'},
		            {type:'string', name:'DS_LINEA_NEGOCIO'},
		            {type:'string', name:'CANTIDAD'}
		        ]
		    });
		    
		    Ext.define('DetalleLineaNegocioPorRamoModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'CDRAMO'},
		            {type:'string', name:'DSRAMO'},
		            {type:'string', name:'CANTIDAD'}
		        ]
		    });
		    
		    Ext.define('TramitesLineaNegocioPorSucursalModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'SUCURSAL'},
		            {type:'string', name:'DS_SUCURSAL'},
		            {type:'string', name:'SALUD'},
		            {type:'string', name:'AUTOS'}
		        ]
		    });
		    
		    Ext.define('TramitesLineaNegocioPorUsuarioModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'CDUSUARI_ACT'},
		            {type:'string', name:'DSUSUARI_ACT'},
		            {type:'string', name:'SALUD'},
		            {type:'string', name:'AUTOS'}		            
		        ]
		    });
		    
		    Ext.define('TramitesPendientesPorDiasModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'CDETAPA'},
		            {type:'string', name:'ETAPA'},
		            {type:'string', name:'NTRAMITE'},
		            {type:'string', name:'FECHA_RECEP_TRAMITE'},
		            {type:'string', name:'FECHA_DESDE'},
		            {type:'string', name:'SUCURSAL'},
		            {type:'string', name:'DS_SUCURSAL'},
		            {type:'string', name:'CD_TIPO_TRAMITE'},
		            {type:'string', name:'TIPO_TRAMITE'},
		            {type:'string', name:'CD_LINEA_NEGOCIO'},
		            {type:'string', name:'DS_LINEA_NEGOCIO'},
		            {type:'string', name:'STATUS_TRAMITE'},
		            {type:'string', name:'DS_STATUS_TRAMITE'},
		            {type:'string', name:'CDRAMO'},
		            {type:'string', name:'DSRAMO'},
		            {type:'string', name:'CDPERSON'},
		            {type:'string', name:'CLIENTE'},
		            {type:'string', name:'CDAGENTE'},
		            {type:'string', name:'NOMBRE_AGENTE'},
		            {type:'string', name:'NMPOLIZA'},
		            {type:'string', name:'NMSOLICI'},
		            {type:'string', name:'CDUSUARI_CREA'},
		            {type:'string', name:'CDUSUARI_ACT'},
		            {type:'string', name:'DSUSUARI_ACT'}
		        ]
		    });
		    
		    Ext.define('TramitesPorTipoModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		            {type:'string', name:'CD_TIPO_TRAMITE'},
		            {type:'string', name:'TIPO_TRAMITE'},
		            {type:'int', name:'CANTIDAD'}	            
		        ]
		    });
		    
		    Ext.define('DetalleLineaNegocioModel', {
		        extend: 'Ext.data.Model',
		        fields: [
		        	{type:'string', name:'NTRAMITE'},
		            {type:'string', name:'CDAGENTE'},
		            {type:'string', name:'CDETAPA'},
		            {type:'string', name:'CDRAMO'},
		            {type:'string', name:'SUCURSAL'},
		            {type:'string', name:'NMPOLIZA'},
		            {type:'string', name:'NMSOLICI'},
		            {type:'string', name:'CDUSUARI_ACT'},
		            {type:'string', name:'DSUSUARI_ACT'},
		            {type:'string', name:'CDUSUARI_CREA'},
		            {type:'string', name:'DSUSUARI_CREA'},
		            {type:'string', name:'CD_LINEA_NEGOCIO'},
		            {type:'string', name:'DS_LINEA_NEGOCIO'},
		            {type:'string', name:'CD_TIPO_TRAMITE'},
		            {type:'string', name:'CLIENTE'},
		            {type:'string', name:'DSRAMO'},
		            {type:'string', name:'TIPO_TRAMITE'},
		            {type:'string', name:'DSUNIECO'},
		            {type:'string', name:'STATUS_TRAMITE'},
		            {type:'string', name:'DS_STATUS_TRAMITE'},
		            {type:'string', name:'ETAPA'},
		            {type:'string', name:'FECHA_DESDE'},
		            {type:'string', name:'FECHA_RECEP_TRAMITE'},
		            {type:'string', name:'NOMBRE_AGENTE'},
		            {type:'string', name:'OFICINA_USR_ACT'},
		            {type:'string', name:'DS_OFICINA_USR_ACT'},
		            {type:'string', name:'CDUSUARI_ANT'},
		            {type:'string', name:'DSUSUARI_ANT'},
		            {type:'string', name:'OFICINA_USR_ANT'},
		            {type:'string', name:'DS_OFICINA_USR_ANT'},
		            {type:'string', name: 'CDPERSON'},
					{type:'string', name: 'OFICINA_USR_CREA'},
					{type:'string', name: 'DS_OFICINA_USR_CREA'}
		            
		        ]
		    });
		    
		    ////// modelos //////
		    
		    ////// stores //////
		    
		    var storeTipoSeguro = Ext.create('Ext.data.Store', {
			   fields:['cdtipram', 'dstipram']
		       ,data :{ 'items': [
			            {cdtipram: null,   dstipram: 'Seleccione'},
			            {cdtipram: '2',    dstipram: 'AUTO'},
			            {cdtipram: '10',   dstipram: 'SALUD'}
			    ]}
		    	,proxy: {
		        	type: 'memory',
		        	reader: {
		            	type: 'json',
		            	root: 'items'
		        	}
		    	}
		    });
		    
		    var storeCierres = Ext.create('Ext.data.Store', {
		        model     : 'IdcierresModel',
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo:_CAT_IDCIERRES}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		            //,autoLoad : true
		        }
		    });
		    
		    storeCierres.load({
		        callback: function(){
		            idcierres.setValue('0');
		        }
		    });
    
		    var storeRamoPadre = Ext.create('Ext.data.Store', {
		        model     : 'IdRamoPadreModel',
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo:_CAT_RAMOS}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		            ,autoLoad : true
		        }
		    
		    });
		    
		    var storeAgentes = Ext.create('Ext.data.Store', {
		        model     : 'IdAgentesModel',
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo:_CAT_AGENTES}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		            ,autoLoad : true
		        }
		    });

		    var storeSucursales = Ext.create('Ext.data.Store', {
		        model     : 'IdSucursalesModel',
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo:_CAT_ZONAS_SUCURSALES}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		            ,autoLoad : true
		        }
		    });
		    
		    var storeTramites = Ext.create('Ext.data.Store', {
		        model     : 'IdTramitesModel',
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo:_CAT_TRAMITES}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		            ,autoLoad : true
		        }
		    });
		    
		    var storeTramitesPorLineaNegocio = new Ext.data.Store({
				model: 'TramitesPorLineaNegocioModel',
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list'//,
					    //totalProperty: 'totalCount',
					    //simpleSortMode: true
				    }
				}
			});
			
			var storeDetalleLineaNegocioPorRamo = new Ext.data.Store({
				model: 'DetalleLineaNegocioPorRamoModel',
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list'
					    //totalProperty: 'total'
					    //simpleSortMode: true
				    }
				}
			});
			
			
			var storeTramitesLineaNegocioPorSucursal = new Ext.data.Store({
				model: 'TramitesLineaNegocioPorSucursalModel',
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list'//,
					    //totalProperty: 'totalCount',
					    //simpleSortMode: true
				    }
				}
			});
			
			
			var storeTramitesLineaNegocioPorUsuario = new Ext.data.Store({
				model: 'TramitesLineaNegocioPorUsuarioModel',
				pageSize: _itemsPerPage,
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list',
					    totalProperty: 'total'
					    //simpleSortMode: true
				    }
				}
			});
			
			var storeTramitesPendientesPorDias = new Ext.data.Store({
				model: 'TramitesPendientesPorDiasModel',
				pageSize: _itemsPerPage,
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list',
					    totalProperty: 'total',
					    //simpleSortMode: true
				    }
				}
			});
			
			var storeTramitesPorTipo = new Ext.data.Store({
				model: 'TramitesPorTipoModel',
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list'//,
					    //totalProperty: 'totalCount',
					    //simpleSortMode: true
				    }
				}
			});
			
			var storeDetalleLineaNegocio = new Ext.data.Store({
				model: 'DetalleLineaNegocioModel',
				pageSize: _itemsPerPage,
				proxy: {
				    type: 'ajax',
				 	url : _GLOBAL_URL_RECUPERACION,
					reader: {
					    type: 'json',
					    root: 'list',
					    totalProperty: 'total'
					    //simpleSortMode: true
				    }
				}
			});
			
		    ////// stores //////
		    
		    ////// componentes //////
		    
		    var idTipoSeguro = Ext.create('Ext.form.ComboBox', {
		        name:'params.lineanegocio',
		        fieldLabel: 'Tipo de Seguro:',
		        displayField: 'dstipram',
		        valueField: 'cdtipram',
		        allowBlank:true,
		        forceSelection: true,
		        emptyText:'Seleccione...',
		        typeAhead:true,
		        typeAheadDelay:10,
		        anyMatch:true,
		        store: storeTipoSeguro,
		        listeners: {
		    		change: {
		    			fn:function(){
		    				cargarRamos(idTipoSeguro, idRamoPadre);
		    			}
		    		}
		        }
		    }); 
		    
		    
		    var idcierres = Ext.create('Ext.form.ComboBox', {
		        name:'params.idcierre',
		        fieldLabel: 'Fecha de Cierre',
		        displayField: 'fecha_cierre',
		        valueField: 'idcierre',
		        allowBlank:true,
		        forceSelection: true,
		        emptyText:'Seleccione...',
		        store: storeCierres,
		        listeners:{
		        }
		    }); 

		    var idRamoPadre = Ext.create('Ext.form.ComboBox', {
		        name:'params.cdramoPadre',
		        fieldLabel: 'Ramo:',
		        displayField: 'value',
		        valueField: 'key',
		        allowBlank:true,
		        forceSelection: true,
		        typeAhead:true,
		        anyMatch:true,
		        emptyText:'Seleccione...',
		        queryMode: 'local',
		        store: storeRamoPadre,
		        listeners: {
		    		change: {
		    			fn:function(){
		    				cargarSubramos(idRamos, idRamoPadre);
		    			}
		    		}
		        }
		    }); 
		    
           	var storeRamos=Ext.create('Ext.data.Store', {
   		        model     : 'IdRamosModel',
   		        proxy     : {
   		            type        : 'ajax'
   		            ,url        : _URL_CARGA_CATALOGO
   		            ,extraParams: {
   		            	catalogo:_CAT_SUBRAMOS//,
   		            	//'params.idPadre':idRamoPadre.getValue()
   		            }
   		            ,reader     :
   		            {
   		                type  : 'json'
   		                ,root : 'lista'
   		            }
   		        }
   		    });
		    
		    var idRamos = Ext.create('Ext.form.ComboBox', {
		        name:'params.cdramo',
		        itemId:'params.cdramo',
		        fieldLabel: 'SubRamo:',
		        displayField: 'value',
		        valueField: 'key',
		        allowBlank:true,
		        queryMode: 'local',
		        forceSelection: true,
		        //typeAhead:true,
		        anyMatch:false,
		        emptyText:'Seleccione...',
		        store: storeRamos
		    }); 

		    var idAgentes = Ext.create('Ext.form.ComboBox', {
		        name:'params.cdagente',
		        fieldLabel: 'Agente:',
		        displayField: 'value',
		        valueField: 'key',
		        allowBlank:true,
		        emptyText:'Seleccione...',
		        store: storeAgentes
		    }); 
		    
		    var idSucursales = Ext.create('Ext.form.ComboBox', {
		        name:'params.cdunieco',
		        fieldLabel: 'Sucursal Emisora:',
		        displayField: 'value',
		        valueField: 'key',
		        allowBlank:true,
		        forceSelection: true,
		        emptyText:'Seleccione...',
		        store: storeSucursales,
		        listeners:{}
		    }); 
		    
		    var idTramites = Ext.create('Ext.form.ComboBox', {
		        name:'params.tipotramite',
		        fieldLabel: 'Tipo de Tr&aacute;mite:',
		        displayField: 'value',
		        valueField: 'key',
		        allowBlank:true,
		        emptyText:'Seleccione...',
		        store: storeTramites
		    });
		    
		    ////// componentes //////
		    
		    ////// contenido //////
			Ext.create('Ext.panel.Panel', {
				renderTo : 'dvEstadisticas',
				itemId: 'pnlEstadisticas',
				//width: 1000,
    			//height: 800,//TODO: ver si se elimina
				items : [{
					xtype : 'form',
					title : 'INDICADORES',
					collapsible: true,
					layout: {
		                type     : 'table',
		                columns  : 3//4//TODO: regresar valor
		        	},
		        	defaults: {
		        		//labelAlign: 'right',
		        		labelAlign: 'left',
		        		margin: '5px'
		        	},
		        	buttonAlign: 'center',
					items: [
//						{xtype: 'textfield', name: 'year', fieldLabel: 'A&ntilde;o'},
//						{xtype: 'textfield', name: 'zona', fieldLabel: 'Zona'}, 
//						{xtype: 'textfield', name: 'params.cdramo', fieldLabel: 'Ramo'}, 
//						{xtype: 'textfield', name: 'tipoDocumento', fieldLabel: 'Tipo de documento'}, 
//						{xtype: 'textfield', name: 'mes', fieldLabel: 'Mes'}, 
//						{xtype: 'textfield', name: 'params.cdunieco', fieldLabel: 'Sucursal emisora'}, 
//						{xtype: 'textfield', name: 'params.lineanegocio', fieldLabel: 'Tipo Seguro'}, 
//						{xtype: 'textfield', name: 'params.tipotramite', fieldLabel: 'Tipo de tr&aacute;mite'}, 
//						{xtype: 'textfield', name: 'semana', fieldLabel: 'Semana'}, 
//						{xtype: 'textfield', name: 'sucursal', fieldLabel: 'Suc'}, 
//						{xtype: 'textfield', name: 'tipo', fieldLabel: 'Tipo'}, 
//						{xtype: 'textfield', name: 'tipoMedio', fieldLabel: 'Tipo medio'}, 
//						{xtype: 'datefield', name: 'fechaDesde', fieldLabel: 'Periodo de'}, 
//						{xtype: 'datefield', name: 'fechaHasta', fieldLabel: 'Periodo hasta'}, 
//						{xtype: 'textfield', name: 'usuarioAutoriza', fieldLabel: 'UsrAut'}, 
//						{xtype: 'textfield', name: 'ramoEmisor', fieldLabel: 'Ramo Emisor'}, 
//						{xtype: 'textfield', name: 'priemi', fieldLabel: 'Pri Emi'}, 
//						{xtype: 'textfield', name: 'usrcap', fieldLabel: 'UsrCap'}, 
//						{xtype: 'textfield', name: 'cdtipsup', fieldLabel: 'TipEndoso'}, 
//						{xtype: 'textfield', name: 'area', fieldLabel: '&Aacute;rea'}, 
//						{xtype: 'textfield', name: 'promotoria', fieldLabel: 'Promotor&iacute;a'}, 
//						{xtype: 'textfield', name: 'params.cdagente', fieldLabel: 'Agente'}, 
//						{xtype: 'textfield', name: 'zona', fieldLabel: 'Zona'}, 
						//{xtype: 'textfield', name: 'params.cdunieco', fieldLabel: 'Sucursal emisora', value:0}, 
						idTipoSeguro,
						idRamoPadre,
						//{xtype: 'textfield', name: 'params.cdramo', fieldLabel: 'SubRamo', value:711},
						idRamos,
						idSucursales,
						//{xtype: 'textfield', name: 'params.tipotramite', fieldLabel: 'Tipo de tr&aacute;mite'},
						idTramites,
						idAgentes,
						idcierres,
						{xtype: 'hiddenfield',name: 'params.cdetapa'}
						],
						buttons: [{
                            xtype: 'button',
                            text : 'Buscar',
                            handler: function(btn) {
                                mostrarPanelPrincipal(null);
                                //Ext.ComponentQuery.query('#grdDetalleLOBPorRamo')[0].hide();
                                buscar();
                            }
                    }]
				},{
					xtype : 'panel',
					itemId: 'pnlIndicadores',
					height: 100,
					layout: {
                        type : 'hbox',
                        align: 'stretch'
                    },
					items: [{
						xtype : 'panel',
						flex  : 0.3,
						title : 'INGRESADOS',
						cls   : 'ingresados',
						layout: {
	                        type : 'hbox',
	                        align:'stretch'
	                    },
						items : [{
							xtype  : 'button',
							itemId : 'btnIngresados',
							width  : '100%',
							cls    : 'my-btn',
							tooltip: 'Ver detalle',
							handler: function() {
								buscar();
								mostrarPanelPrincipal('tbDetalle');
								cargarDetalleTramites(1);
							}
						}]
					}, {
						xtype : 'panel',
						flex  : 0.3,
						title : 'ATENDIDOS / PROCESADOS',
						cls   : 'procesados',
						layout: {
	                        type : 'hbox',
	                        align:'stretch'
	                    },
						items : [{
							xtype  : 'button',
							itemId : 'btnProcesados',
							width  : '100%',
							cls    : 'my-btn',
							tooltip: 'Ver detalle',
							handler: function() {
								buscar();
								mostrarPanelPrincipal('tbDetalle');
								cargarDetalleTramites(3);
							}
						}]
					}, {
						xtype : 'panel',
						flex  : 0.4,
						title : 'EN PROCESO / PENDIENTES',
						cls   : 'pendientes',
						layout: {
	                        type : 'hbox',
	                        align:'stretch'
	                    },
						items : [{
							xtype  : 'button',
							itemId : 'btnPendientes',
							width  : '100%',
							cls    : 'my-btn',
							tooltip: 'Ver detalle',
							handler: function() {
								buscar();
								mostrarPanelPrincipal('tbDetalle');
								cargarDetalleTramites(2);
							}
						}]
					}]
				},{
					xtype : 'panel',
					itemId: 'pnlIndicadores2',
					height: 100,//130,
					layout: {
                        type : 'hbox',
                        align: 'stretch'
                    },
					items: [{
						xtype : 'panel',
						flex  : 0.3,
						layout: {
	                        type : 'hbox',
	                        align:'stretch'
	                    }
					}, {
						xtype : 'panel',
						itemId: 'pnlEficacia',
						flex  : 0.3,
						title: 'Eficacia',
						cls: 'procesados',
						layout: {
	                        type : 'vbox',
	                        align:'center'
	                    },
						items : [{
							xtype: 'displayfield',
							name:  'dspEficacia',
							fieldCls: 'eficacia'
						}]
					}, {
						xtype: 'grid', // grid is already a panel
						itemId: 'grdEnProceso',
						flex  : 0.4,
						selType: 'cellmodel',
						title: 'En proceso:',
						layout: 'fit',
						store: Ext.create('Ext.data.ArrayStore', {
							fields: [
								{name: 'undia'},
								{name: 'dosdias'},
								{name: 'tresdias'},
								{name: 'cuatrodias'},
								{name: 'cincodias'},
								{name: 'mascincodias'},
								{name: 'total'}
							]
						}),
						columns: [
							{text: '1h',    flex: 1, dataIndex: 'undia'},
							{text: '2h',    flex: 1, dataIndex: 'dosdias'},
							{text: '3h',    flex: 1, dataIndex: 'tresdias'},
							{text: '4h',    flex: 1, dataIndex: 'cuatrodias'},
							{text: '5h',    flex: 1, dataIndex: 'cincodias'},
							{text: '+5h',   flex: 1, dataIndex: 'mascincodias'},
                           	{text: 'Total', flex: 1, dataIndex: 'total'}
						],
						listeners: {
							cellclick: function ( grd, td, cellIndex, record, tr, rowIndex, e, eOpts ) {
								buscar();
								setTituloDetalleProcesoPorDia(cellIndex+1);
								
								mostrarPanelPrincipal('grdDetalleProcesoPorDia');
								
								var par = Ext.ComponentQuery.query('form')[0].getForm().getValues();
								//par['params.consulta'] = 'RECUPERAR_TRAMITES_PENDIENTES_POR_DIAS_LP';
								par['params.consulta'] = 'RECUPERAR_TRAMITES_PENDIENTES_POR_HORAS_LP';
								par['params.numdias'] = cellIndex+1;

								storeTramitesPendientesPorDias.getProxy().extraParams = par;
								
								storeTramitesPendientesPorDias.loadPage(1);
								
							}
						}
					}],
                    dockedItems: [{
                        xtype: 'toolbar',
                        dock: 'bottom',
                        hidden: true,
                        items:['->',{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
                                   handler: function(){
                                       
                                       var form = Ext.ComponentQuery.query('form')[0].getForm();
                                       
                                          var params = {
                                               "params.consulta"     : 'RECUPERAR_TABLERO_INDICADORES',
                                               "params.cdunieco"     : form.findField('params.cdunieco').getValue(),
                                               "params.lineanegocio" : form.findField('params.lineanegocio').getValue(),
                                               "params.cdramo"       : form.findField('params.cdramo').getValue(),
                                               "params.tipotramite"  : form.findField('params.tipotramite').getValue(),
                                               "params.cdagente"     : form.findField('params.cdagente').getValue(),
                                               "params.idcierre"     : form.findField('params.idcierre').getValue(),
                                               "params.exportar"     : 'S'
                                           };
                                       
                                       mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
                                           
                                       Ext.create('Ext.form.Panel').submit({
                                           url       : _GLOBAL_URL_RECUPERACION,
                                           standardSubmit : true,
                                           target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
                                           params   : params
                                       });
                                   }
                              }]
                     }]
				}, {
					xtype : 'panel',
					itemId: 'pnlDetalles',
					//height: 510,
					items: [{
						xtype: 'tabpanel',
						itemId: 'tbDetalle',
						hidden: true,
						margins: '5 5 5 0',
						layout: 'fit',
						listeners:{
						    tabchange: function(){
						        /**
					            * Para ocultar detalle panel secundario
					            */
					            mostrarPanelSubdetalles(null);
						    }
						},
						items: [{
							title : 'Por Linea de Negocio',
							layout: 'hbox',
							items : [{
								xtype: 'grid',
								itemId: 'grdTramitesPorLineaNegocio',
								//title: 'Total de tramites ingresados por LOB',
								title: 'Total de tramites por LN',
								header: {
								    autoEl: {
								        'data-qtip': 'Click en M\u00E1s Detalles -->'
								    }
								},
								flex: 1,
								//layout: 'fit',
								//height: 350,
								store: storeTramitesPorLineaNegocio,
								columns: [
//									{
//                                        text     : 'Periodo',
//                                        flex     : 2,
//                                        dataIndex: 'periodo'
//                                    },
                                  {
                                    text     : 'LOB', 
                                    flex     : 2,
                                    dataIndex: 'CD_LINEA_NEGOCIO'
                                },{
                                    text     : 'L\u00EDnea de negocio', 
                                    flex     : 3,
                                    dataIndex: 'DS_LINEA_NEGOCIO'
                                },{
                                    text     : 'Cantidad', 
                                    flex     : 3,
                                    dataIndex: 'CANTIDAD'
                                },{
						            xtype    :'actioncolumn',
						            text     : 'M\u00E1s Detalles',
						            flex     : 2,
						            items: [{
						                tooltip : 'Ver Detalle por Subramo',
						                icon    : '${ctx}/resources/fam3icons/icons/color_swatch.png',
						                handler : function(grid, rowIndex, colIndex, item, e, record, row) {
						                						                	
						                	mostrarPanelSubdetalles('grdDetalleLOBPorRamo');
						                	
						                	var par = Ext.ComponentQuery.query('form')[0].getForm().getValues();
											par['params.consulta'] = 'RECUPERAR_TRAMITES_LINEANEGOCIO_POR_RAMO';
											par['params.lineanegocio'] = record.get('CD_LINEA_NEGOCIO');
											par['params.cdetapa'] = Ext.ComponentQuery.query('[name=params.cdetapa]')[0].getValue();
											
											storeDetalleLineaNegocioPorRamo.getProxy().extraParams = par;
											storeDetalleLineaNegocioPorRamo.load();
											
						                }
						            },{
						                tooltip : 'Ver Detalle por tipo de tr\u00E1mite',
						                icon    : '${ctx}/resources/fam3icons/icons/report_edit.png',
						                handler: function(grid, rowIndex, colIndex, item, e, record, row) {
						                	
						                	mostrarPanelSubdetalles('grdTramitesPorTipo');
						                	
											var par = Ext.ComponentQuery.query('form')[0].getForm().getValues();
											par['params.consulta']     = 'RECUPERAR_TRAMITES_POR_TIPO';
											par['params.lineanegocio'] = record.get('CD_LINEA_NEGOCIO');
											par['params.cdetapa']      = Ext.ComponentQuery.query('[name=params.cdetapa]')[0].getValue();
											
											storeTramitesPorTipo.getProxy().extraParams = par;
											storeTramitesPorTipo.load();
						                	
						                }
						            },{
						                tooltip : 'Ver Detalle Completo',
						                icon    : '${ctx}/resources/fam3icons/icons/text_columns.png',
						                handler: function(grid, rowIndex, colIndex, item, e, record, row) {
						                	
						                	mostrarPanelSubdetalles('grdDetalleLineaNegocio');
						                	
						                	var par = Ext.ComponentQuery.query('form')[0].getForm().getValues();
											par['params.consulta']     = 'RECUPERAR_DETALLE_LINEA_NEGOCIO_LP';
											par['params.lineanegocio'] = record.get('CD_LINEA_NEGOCIO');
											par['params.cdetapa']      = Ext.ComponentQuery.query('[name=params.cdetapa]')[0].getValue();
											
											storeDetalleLineaNegocio.getProxy().extraParams = par;
											
											storeDetalleLineaNegocio.loadPage(1);
						                }
						            }]
    							}],
    	                        dockedItems: [{
    	                            xtype: 'toolbar',
    	                            dock: 'bottom',
    	                            items:['->',{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
    	                                       handler: function(){
    	                                           
    	                                           if(storeTramitesPorLineaNegocio.isLoading()){
    	                                               mensajeWarning('No se puede exportar mientras esta cargando.');
    	                                               return;
    	                                           }
    	                                           
    	                                           if(storeTramitesPorLineaNegocio.getTotalCount() <= 0){
                                                       mensajeWarning('No hay datos a exportar.');
                                                       return;
                                                   }
    	                                           
    	                                           var params = Ext.clone(storeTramitesPorLineaNegocio.getProxy().extraParams); 
    	                                           params['params.consulta'] = 'RECUPERAR_TRAMITES_POR_LINEA_NEGOCIO';
    	                                           params['params.exportar'] = 'S';
    	                                           
    	                                           mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
    	                                               
    	                                           Ext.create('Ext.form.Panel').submit({
    	                                               url       : _GLOBAL_URL_RECUPERACION,
    	                                               standardSubmit : true,
    	                                               target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
    	                                               params   : params
    	                                           });
    	                                       }
    	                                  }]
    	                         }],
								listeners: {
									cellclick: function ( grd, td, cellIndex, record, tr, rowIndex, e, eOpts ) {
										
										//setTituloDetalleProcesoPorDia(cellIndex+1);
										//mostrarPanelPrincipal('grdDetalleProcesoPorDia');
										/*
										var par = Ext.ComponentQuery.query('form')[0].getForm().getValues();
										par['params.consulta'] = 'RECUPERAR_TRAMITES_LINEANEGOCIO_POR_RAMO';
										par['params.lineanegocio'] = record.get('CD_LINEA_NEGOCIO');
										par['params.cdetapa'] = Ext.ComponentQuery.query('[name=params.cdetapa]')[0].getValue();
										
										storeDetalleLineaNegocioPorRamo.load({
											params : par
										});
										
										Ext.ComponentQuery.query('#grdDetalleLOBPorRamo')[0].show();
										*/
									}
								}
							}/*,{
								xtype: 'grid',
								itemId: 'grdDetalleLOBPorRamo',
								hidden: true,
								flex: 0.5,
								title: '--> Detalle LOB por Ramo',
								//layout: 'fit',
								store: storeDetalleLineaNegocioPorRamo,
								columns: [
									{
                                        text     : 'C\u00F3digo', 
                                        flex     : 2,
                                        dataIndex: 'CDRAMO'
                                    },{
                                        text     : 'Ramo', 
                                        flex     : 5,
                                        dataIndex: 'DSRAMO'
                                    },{
                                        text     : 'Total', 
                                        flex     : 3,
                                        dataIndex: 'CANTIDAD'
                                    }
								]
							}*/]
						},{
							title: 'Por Sucursal',
							layout: 'fit',
							items : [{
								xtype: 'grid',
								itemId: 'grdTramitesPorSucursal',
								//title: 'Total de tramites ingresados por Sucursal',
								title: 'Total de tramites por Sucursal',
								//layout: 'fit',
								height: 350,
								store: storeTramitesLineaNegocioPorSucursal,
								columns: [
									{
                                        text     : 'C\u00F3digo Sucursal',
                                        flex     : 2,
                                        dataIndex: 'SUCURSAL'
                                    },{
                                        text     : 'Sucursal', 
                                        flex     : 5,
                                        dataIndex: 'DS_SUCURSAL'
                                    },{
                                        text     : 'Total Salud', 
                                        flex     : 3,
                                        dataIndex: 'SALUD'
                                    },{
                                        text     : 'Total Autos', 
                                        flex     : 3,
                                        dataIndex: 'AUTOS'
                                    }
								],
                                dockedItems: [{
                                    xtype: 'toolbar',
                                    dock: 'bottom',
                                    items:['->',{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
                                               handler: function(){
                                                   
                                                   if(storeTramitesLineaNegocioPorSucursal.isLoading()){
                                                       mensajeWarning('No se puede exportar mientras esta cargando.');
                                                       return;
                                                   }
                                                   
                                                   if(storeTramitesLineaNegocioPorSucursal.getTotalCount() <= 0){
                                                       mensajeWarning('No hay datos a exportar.');
                                                       return;
                                                   }
                                                   
                                                   var params = Ext.clone(storeTramitesLineaNegocioPorSucursal.getProxy().extraParams); 
                                                   params['params.consulta'] = 'RECUPERAR_LINEANEGOCIO_POR_SUCURSAL';
                                                   params['params.exportar'] = 'S';
                                                   
                                                   mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
                                                       
                                                   Ext.create('Ext.form.Panel').submit({
                                                       url       : _GLOBAL_URL_RECUPERACION,
                                                       standardSubmit : true,
                                                       target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
                                                       params   : params
                                                   });
                                               }
                                          }]
                                 }]
							}]
						},{
							title: 'Por Usuario',
							layout: 'fit',
							items : [{
								xtype: 'grid',
								itemId: 'grdTramitesPorUsuario',
								//title: 'Total de tramites ingresados por Usuario',
								title: 'Total de tramites por Usuario',
								//layout: 'fit',
								height: 350,
								store: storeTramitesLineaNegocioPorUsuario,
								columns: [
									{text: 'Clave',       flex: 2, dataIndex: 'CDUSUARI_ACT'},
                                    {text: 'Nombre',      flex: 5, dataIndex: 'DSUSUARI_ACT'},
                                    {text: 'Total Salud', flex: 3, dataIndex: 'SALUD'},
                                    {text: 'Total Autos', flex: 3, dataIndex: 'AUTOS'}
								]
							}],
	                        dockedItems: [{
	                            xtype: 'toolbar',
	                            dock: 'bottom',
	                            padding: '0 0 0 0',
	                            items:[{
	                                    xtype: 'pagingtoolbar',
	                                    store: storeTramitesLineaNegocioPorUsuario, // same store GridPanel is using
	                                    displayInfo: true,
	                                    flex: 1
	                                   },{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
	                                       handler: function(){
	                                           
	                                           if(storeTramitesLineaNegocioPorUsuario.isLoading()){
	                                               mensajeWarning('No se puede exportar mientras esta cargando.');
	                                               return;
	                                           }
	                                           
	                                           if(storeTramitesLineaNegocioPorUsuario.getTotalCount() <= 0){
                                                   mensajeWarning('No hay datos a exportar.');
                                                   return;
                                               }
	                                           
	                                           var params = Ext.clone(storeTramitesLineaNegocioPorUsuario.getProxy().extraParams); 
	                                           params['params.consulta'] = 'RECUPERAR_LINEANEGOCIO_POR_USUARIO';
	                                           params['params.exportar'] = 'S';
	                                           
	                                           mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
	                                               
	                                           Ext.create('Ext.form.Panel').submit({
	                                               url       : _GLOBAL_URL_RECUPERACION,
	                                               standardSubmit : true,
	                                               target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
	                                               params   : params
	                                           });
	                                       }
	                                  }]
	                         }]
						}]
					},{
						xtype: 'grid',
						itemId: 'grdDetalleProcesoPorDia',
						hidden: true,
						//layout:'fit',
						height: 380,
						store: storeTramitesPendientesPorDias,
						columns: [
							{text: 'Tramite',         dataIndex: 'NTRAMITE'},
							{text: 'Sucursal',        dataIndex: 'SUCURSAL'},
							{text: 'Ramo',            dataIndex: 'DSRAMO'},
							{text: 'Usuario inicial', dataIndex: 'CDUSUARI_CREA'},
							{text: 'Usuario actual',  dataIndex: 'CDUSUARI_ACT'},
							{text: 'Prospecto',       dataIndex: 'CLIENTE'},
							{text: 'Estatus tramite', dataIndex: 'DS_STATUS_TRAMITE'},
							{text: 'Etapa',           dataIndex: 'ETAPA'},
							{text: 'Fecha Inicio',    dataIndex: 'FECHA_DESDE'},
							{text: 'Fecha Recepcion', dataIndex: 'FECHA_RECEP_TRAMITE'},
							{text: 'Agente',          dataIndex: 'NOMBRE_AGENTE'},
							{text: 'Tipo Tramite',    dataIndex: 'TIPO_TRAMITE'}
						],
						dockedItems: [{
                            xtype: 'toolbar',
                            dock: 'bottom',
                            padding: '0 0 0 0',
                            items:[{
	                                xtype: 'pagingtoolbar',
	                                store: storeTramitesPendientesPorDias, // same store GridPanel is using
	                                displayInfo: true,
	                                flex: 1
	                               },{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
	                                   handler: function(){
	                                       
	                                       if(storeTramitesPendientesPorDias.isLoading()){
	                                           mensajeWarning('No se puede exportar mientras esta cargando.');
	                                           return;
	                                       }

	                                       if(storeTramitesPendientesPorDias.getTotalCount() <= 0){
                                               mensajeWarning('No hay datos a exportar.');
	                                           return;
	                                       }
	                                       
	                                       var params = Ext.clone(storeTramitesPendientesPorDias.getProxy().extraParams); 
	                                       //params['params.consulta'] = 'RECUPERAR_TRAMITES_PENDIENTES_POR_DIAS';
	                                       params['params.consulta'] = 'RECUPERAR_TRAMITES_PENDIENTES_POR_HORAS';
	                                       params['params.exportar'] = 'S';
	                                       
	                                       mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
	                                           
	                                       Ext.create('Ext.form.Panel').submit({
	                                           url       : _GLOBAL_URL_RECUPERACION,
	                                           standardSubmit : true,
                                               target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
	                                           params   : params
	                                       });
	                                   }
	                              }]
                         }]
					}]
				}, {
					xtype : 'panel',
					itemId: 'pnlSubDetalles',
					layout: {
                        type : 'hbox',
                        align: 'stretch'
                    },
					//height: 510,
					items: [{
						xtype: 'grid',
						itemId: 'grdDetalleLOBPorRamo',
						hidden: true,
						flex: 1,
						title: '--> Detalle LOB por Ramo',
						//layout: 'fit',
						store: storeDetalleLineaNegocioPorRamo,
						columns: [
							{text: 'C\u00F3digo', flex: 2, dataIndex: 'CDRAMO'},
							{text: 'Ramo',        flex: 5, dataIndex: 'DSRAMO'},
							{text: 'Total',       flex: 3, dataIndex: 'CANTIDAD'}
						],
                        dockedItems: [{
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items:['->',{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
                                       handler: function(){
                                           
                                           if(storeDetalleLineaNegocioPorRamo.isLoading()){
                                               mensajeWarning('No se puede exportar mientras esta cargando.');
                                               return;
                                           }

                                           if(storeDetalleLineaNegocioPorRamo.getTotalCount() <= 0){
                                               mensajeWarning('No hay datos a exportar.');
                                               return;
                                           }
                                           
                                           var params = Ext.clone(storeDetalleLineaNegocioPorRamo.getProxy().extraParams); 
                                           params['params.consulta'] = 'RECUPERAR_TRAMITES_LINEANEGOCIO_POR_RAMO';
                                           params['params.exportar'] = 'S';
                                           
                                           mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
                                               
                                           Ext.create('Ext.form.Panel').submit({
                                               url       : _GLOBAL_URL_RECUPERACION,
                                               standardSubmit : true,
                                               target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
                                               params   : params
                                           });
                                       }
                                  }]
                         }]
					},{
						xtype: 'grid',
						itemId: 'grdTramitesPorTipo',
						hidden: true,
						flex: 1,
						title: '--> Detalle por tipo de Tr\u00E1mite',
						//layout: 'fit',
						store: storeTramitesPorTipo,
						columns: [
							{text: 'C\u00F3digo', flex: 2, dataIndex: 'CD_TIPO_TRAMITE'},
							{text: 'Tipo',        flex: 5, dataIndex: 'TIPO_TRAMITE'},
                            {text: 'Total',       flex: 3, dataIndex: 'CANTIDAD'}
						],
                        dockedItems: [{
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items:['->',{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
                                       handler: function(){
                                           
                                           if(storeTramitesPorTipo.isLoading()){
                                               mensajeWarning('No se puede exportar mientras esta cargando.');
                                               return;
                                           }

                                           if(storeTramitesPorTipo.getTotalCount() <= 0){
                                               mensajeWarning('No hay datos a exportar.');
                                               return;
                                           }
                                           
                                           var params = Ext.clone(storeTramitesPorTipo.getProxy().extraParams); 
                                           params['params.consulta'] = 'RECUPERAR_TRAMITES_POR_TIPO';
                                           params['params.exportar'] = 'S';
                                           
                                           mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
                                               
                                           Ext.create('Ext.form.Panel').submit({
                                               url       : _GLOBAL_URL_RECUPERACION,
                                               standardSubmit : true,
                                               target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
                                               params   : params
                                           });
                                       }
                                  }]
                         }]
					},{
						xtype: 'grid',
						itemId: 'grdDetalleLineaNegocio',
						hidden: true,
						flex: 1,
						title: '--> Detalle del tr\u00E1mite',
						//autoScroll: true,
						height: 300,
						//layout: 'fit',
						store: storeDetalleLineaNegocio,
						columns: [
							{text: 'Tr\u00E1mite',      dataIndex: 'NTRAMITE'},
							{text: 'CD Agente',         dataIndex: 'CDAGENTE'},
							{text: 'Agente',            dataIndex: 'NOMBRE_AGENTE'},
							//{text: 'C\u00F3digo', dataIndex: 'CDETAPA'},
							//{text: 'C\u00F3digo', dataIndex: 'CDRAMO'},
							{text: 'Ramo',              dataIndex: 'DSRAMO'},
							{text: 'CD Sucursal',       dataIndex: 'SUCURSAL'},
							{text: 'Sucursal',          dataIndex: 'DS_SUCURSAL'},
							{text: 'Poliza',            dataIndex: 'NMPOLIZA'},
							{text: 'No. Solicitud',     dataIndex: 'NMSOLICI'},
							{text: 'CD Usuario inicia', dataIndex: 'CDUSUARI_ACT'},
							{text: 'Usuario inicia',    dataIndex: 'DSUSUARI_ACT'},
							{text: 'CD Usuario crea',   dataIndex: 'CDUSUARI_CREA'},
							{text: 'Usuario inicia',    dataIndex: 'DSUSUARI_CREA'},
							{text: 'CD Sucursal Usr. crea', dataIndex: 'OFICINA_USR_CREA'},
							{text: 'Sucursal Usr. crea', dataIndex: 'DS_OFICINA_USR_CREA'},
							//{text: 'Cd ', dataIndex: 'CD_LINEA_NEGOCIO'},
							{text: 'L\u00EDnea de negocio',   dataIndex: 'DS_LINEA_NEGOCIO'},
							//{text: 'C\u00F3digo', dataIndex: 'CD_TIPO_TRAMITE'},
							{text: 'Tipo de tr\u00E1mite',    dataIndex: 'TIPO_TRAMITE'},
							//{text: 'Es', dataIndex: 'STATUS_TRAMITE'},
							{text: 'Estatus tr\u00E1mite',    dataIndex: 'DS_STATUS_TRAMITE'},
							{text: 'Etapa',             dataIndex: 'ETAPA'},
							{text: 'Fecha inicio',      dataIndex: 'FECHA_DESDE'},
							{text: 'Fecha de recepci\u00F3n', dataIndex: 'FECHA_RECEP_TRAMITE'},
							{text: 'CD Sucursal Usuario', dataIndex: 'OFICINA_USR_ACT'},
							{text: 'Sucursal Usuario', dataIndex: 'DS_OFICINA_USR_ACT'},
							{text: 'CD Usuariol Ant.', dataIndex: 'CDUSUARI_ANT'},
							{text: 'Usuario Ant.', dataIndex: 'DSUSUARI_ANT'},
							{text: 'CD Sucursal Usr. Ant.', dataIndex: 'OFICINA_USR_ANT'},
							{text: 'Sucursal Usr. Ant.', dataIndex: 'DS_OFICINA_USR_ANT'},
							{text: 'CDPERSON', dataIndex: 'CDPERSON'},
							
						],
                        dockedItems: [{
                            xtype: 'toolbar',
                            dock: 'bottom',
                            padding: '0 0 0 0',
                            items:[{
                                    xtype: 'pagingtoolbar',
                                    store: storeDetalleLineaNegocio, // same store GridPanel is using
                                    displayInfo: true,
                                    flex: 1
                                   },{ xtype: 'button', text: 'Exportar', icon: '${ctx}/resources/fam3icons/icons/page_excel.png',
                                       handler: function(){
                                           
                                           if(storeDetalleLineaNegocio.isLoading()){
                                               mensajeWarning('No se puede exportar mientras esta cargando.');
                                               return;
                                           }

                                           if(storeDetalleLineaNegocio.getTotalCount() <= 0){
                                               mensajeWarning('No hay datos a exportar.');
                                               return;
                                           }
                                           
                                           var params = Ext.clone(storeDetalleLineaNegocio.getProxy().extraParams); 
                                           params['params.consulta'] = 'RECUPERAR_DETALLE_LINEA_NEGOCIO';
                                           params['params.exportar'] = 'S';
                                           
                                           mensajeCorrecto('Exportando...' ,'Se descargar&aacute un archivo en su navegador.');
                                               
                                           Ext.create('Ext.form.Panel').submit({
                                               url       : _GLOBAL_URL_RECUPERACION,
                                               standardSubmit : true,
                                               target         : '_blank', // para abrir una nueva pestaña en caso de que se recarge la pagina
                                               params   : params
                                           });
                                       }
                                  }]
                         }]
					}]
				}]
			});
			
		    ////// contenido //////
		    
		    ////// custom //////
		    ////// custom //////
		    
		    ////// loaders //////
		    ////// loaders //////
			
			
			var task = Ext.TaskManager.start({
			    run: buscar,
			    //interval: 60000 //tiempo en milisegundos 
			    interval: 600000 //10 minutos
			});
			
			
		});
		
		////// funciones //////
		
		function cargarSubramos(idRamos, idRamoPadre){
			debug('cargarSubramos', idRamos, idRamoPadre);
           	//idRamos.clearValue();
           	//idRamos.store = storeRamos;
           	debug('idRamoPadre=', idRamoPadre);
           	debug('idRamoPadre.getValue()=', idRamoPadre.getValue());
           	idRamos.getStore().load({
		            params: {
        				'params.idPadre':idRamoPadre.getValue()
		            }
           	});
		}
		
		function cargarRamos(idTipoSeguro, idRamoPadre){
           	debug('idTipoSeguro=', idTipoSeguro);
           	debug('idTipoSeguro.getValue()=', idTipoSeguro.getValue());
           	idRamoPadre.getStore().load({
		            params: {
        				'params.idPadre':idTipoSeguro.getValue()
		            }
           	});
		}
		
		function setTituloDetalleProcesoPorDia(dia) {
			
			var titulo = 'Total en proceso ';
			
			switch(dia) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					//titulo += dia + ' d\u00EDa(s)';
					titulo += dia + ' h';
					break;
				case 6:
					//titulo += 'm\u00E1s de 5 d\u00EDas';
					titulo += ' +5h';
					break;
				default:
					break;
			}
			Ext.ComponentQuery.query('#grdDetalleProcesoPorDia')[0].setTitle(titulo);
		}
		
		
		/**
		 * Buqueda de datos de indicadores
		 */
		function buscar() {

			Ext.ComponentQuery.query('#pnlEstadisticas')[0].setLoading(true);
			
			var form = Ext.ComponentQuery.query('form')[0].getForm();
        	debug("cdunieco ", form.findField('params.cdunieco').getValue());
        	debug("cdramo ", form.findField('params.cdramo').getValue());
        	
			Ext.Ajax.request({
	        	url       : _GLOBAL_URL_RECUPERACION,
	            params    : {
	            	"params.consulta"     : 'RECUPERAR_TABLERO_INDICADORES',
	            	"params.cdunieco"     : form.findField('params.cdunieco').getValue(),
	            	"params.lineanegocio" : form.findField('params.lineanegocio').getValue(),
	            	"params.cdramo"       : form.findField('params.cdramo').getValue(),
	            	"params.tipotramite"  : form.findField('params.tipotramite').getValue(),
	            	"params.cdagente"     : form.findField('params.cdagente').getValue(),
	            	"params.idcierre"     : form.findField('params.idcierre').getValue()
	            },
	            callback  : function (options, success, response){
	            	
	            	Ext.ComponentQuery.query('#pnlEstadisticas')[0].setLoading(false);
	            	
	                if(success){
	                	
	                    var jsonResp = Ext.decode(response.responseText);
	                    debug("jsonResp", jsonResp);
	                    Ext.ComponentQuery.query('#btnIngresados')[0].setText(jsonResp.params.ingresados);
	                    Ext.ComponentQuery.query('#btnProcesados')[0].setText(jsonResp.params.procesados);
	                    Ext.ComponentQuery.query('#btnPendientes')[0].setText(jsonResp.params.pendientes);
	                    // Se crea record:
	                    var pend = Ext.create('PendientesModel', {
						    undia   :    jsonResp.params.undia,
						    dosdias :    jsonResp.params.dosdias,
						    tresdias:    jsonResp.params.tresdias,
						    cuatrodias:  jsonResp.params.cuatrodias,
						    cincodias:   jsonResp.params.cincodias,
						    mascincodias:jsonResp.params.mascincodias,
						    total:       jsonResp.params.total
						});
						
						Ext.ComponentQuery.query('#grdEnProceso')[0].getStore().removeAll();
						Ext.ComponentQuery.query('#grdEnProceso')[0].getStore().loadRecords([pend]);
						Ext.ComponentQuery.query('[name=dspEficacia]')[0].setValue(jsonResp.params.eficacia + ' %');
	                    
	                }else{
	                    showMessage('Error', 'Error al obtener los avisos.', Ext.Msg.OK, Ext.Msg.ERROR);
	                }
	            }
	        });
		}
		
		
		/**
		 * Oculta todos los elementos del panel de detalles y muestra el elemento cuyo itemId se envia
		 * @param {String} itemId
		 */
		function mostrarPanelPrincipal(itemId) {			
			var elements = Ext.ComponentQuery.query('#pnlDetalles > panel');
			
			Ext.Array.each(elements, function(elem, index, elements) {
				elem.hide();
			});
			
			/**
			* Para ocultar detalle panel secundario
			*/
			mostrarPanelSubdetalles(null);
			
			if(!Ext.isEmpty(itemId)) {
				Ext.ComponentQuery.query('#'+itemId)[0].show();
			}
			
		}
		
		
		/**
		 * Oculta todos los elementos del panel de subdetalles y muestra el elemento cuyo itemId se envia
		 * @param {String} itemId
		 */
		function mostrarPanelSubdetalles(itemId) {			
			var elements = Ext.ComponentQuery.query('#pnlSubDetalles > panel');
			
			Ext.Array.each(elements, function(elem, index, elements) {
				elem.hide();
			});
			
			if(!Ext.isEmpty(itemId)) {
				Ext.ComponentQuery.query('#'+itemId)[0].show();
			}
			
		}
		
		
		/**
		 * Carga el detalle de los tramites
		 * @param {int} cdetapa 1 : Ingresados, 2 : En Proceso o Pendientes, 3: Atendidos o Procesados
		 */
		function cargarDetalleTramites(cdetapa) {
			
			// Cambiar estilos:
			var elements = Ext.ComponentQuery.query('#pnlDetalles panel');
			var estilo;
			switch(cdetapa) {
				case 1:
					estilo = 'ingresados';
					break;
				case 2:
					estilo = 'pendientes';
					break;
				case 3:
					estilo = 'procesados';
					break;
			}
			Ext.Array.each(elements, function(elem, index, elements) {
				debug('asignando estilo ' + estilo, elem);
				
				//TODO: mejorar el cambio de estilo:
				elem.removeCls('ingresados');
				elem.removeCls('pendientes');
				elem.removeCls('procesados');
				
				elem.addCls(estilo);
			});
			
			
			mostrarPanelPrincipal('tbDetalle');
								
			// Se setea al valor de cdetapa en parametros y en el campo hidden:
			var par = Ext.ComponentQuery.query('form')[0].getForm().getValues();
			par['params.cdetapa'] = cdetapa;
            Ext.ComponentQuery.query('[name=params.cdetapa]')[0].setValue(cdetapa);
			
			var parNegocio  = Ext.clone(par);
			var parSucursal = Ext.clone(par);
			var parUsuario  = Ext.clone(par);
			
			// Se obtiene la consulta de tramites por linea de negocio:
			parNegocio['params.consulta'] = 'RECUPERAR_TRAMITES_POR_LINEA_NEGOCIO';
			
			Ext.ComponentQuery.query('#grdTramitesPorLineaNegocio')[0].getStore().getProxy().extraParams = parNegocio;
			Ext.ComponentQuery.query('#grdTramitesPorLineaNegocio')[0].getStore().load();
			
			// Se obtiene la consulta de tramites por sucursal:
			parSucursal['params.consulta'] = 'RECUPERAR_LINEANEGOCIO_POR_SUCURSAL';
			
			Ext.ComponentQuery.query('#grdTramitesPorSucursal')[0].getStore().getProxy().extraParams = parSucursal;
			Ext.ComponentQuery.query('#grdTramitesPorSucursal')[0].getStore().load();
			
			// Se obtiene la consulta de tramites por usuario:
			parUsuario['params.consulta'] = 'RECUPERAR_LINEANEGOCIO_POR_USUARIO_LP';
			
			Ext.ComponentQuery.query('#grdTramitesPorUsuario')[0].getStore().getProxy().extraParams = parUsuario;
			Ext.ComponentQuery.query('#grdTramitesPorUsuario')[0].getStore().loadPage(1);
			
		}
		
		////// funciones //////
		</script>
	</head>
	<body>
		<div id="dvEstadisticas" style="height:800px"></div>
	</body>
</html>
