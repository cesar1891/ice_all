<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

	/*
	var _CDUNIECO = '<s:property value="params.cdunieco" />';
	var _CDRAMO   = '<s:property value="params.cdramo" />';
	var _ESTADO   = '<s:property value="params.estado" />';
	var _NMPOLIZA = '<s:property value="params.nmpoliza" />';
	var _NMSITUAC = '<s:property value="params.nmsituac" />';
	var _NMSUPLEM = '<s:property value="params.nmsuplem" />';
	var _STATUS   = '<s:property value="params.status" />';// status del siniestro
	var _AAAPERTU = '<s:property value="params.aaapertu" />';
	var _NMSINIES = '<s:property value="params.nmsinies" />';
	var _NTRAMITE = '<s:property value="params.ntramite" />';
	*/
	
	//Catalogo Tipos de pago a utilizar:
	var _PAGO_DIRECTO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
	var _REEMBOLSO    = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';

	var _URL_CATALOGOS                      = '<s:url namespace="/catalogos"       action="obtieneCatalogo" />';
	var _URL_INFO_GRAL_SINIESTRO            = '<s:url namespace="/siniestros"      action="obtieneDatosGeneralesSiniestro" />';
	var _URL_ACTUALIZA_INFO_GRAL_SIN        = '<s:url namespace="/siniestros"      action="actualizaDatosGeneralesSiniestro" />';
	var _URL_LOADER_VER_COBERTURAS          = '<s:url namespace="/consultasPoliza" action="includes/verCoberturasPoliza" />';
	var _URL_LOADER_VER_EXCLUSIONES         = '<s:url namespace="/consultasPoliza" action="includes/verClausulas" />';
	var _URL_LOADER_HISTORIAL_RECLAMACIONES = '<s:url namespace="/siniestros"      action="includes/historialReclamaciones" />';
	var _URL_LOADER_DATOS_POLIZA            = '<s:url namespace="/consultasPoliza" action="includes/ventanaDatosPoliza" />';
	var _URL_LOADER_ASEGURADOS_POLIZA       = '<s:url namespace="/consultasPoliza" action="includes/ventanaAseguradosPoliza" />';
	var _URL_LOADER_CONSULTA_DOCUMENTOS     = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
	var _URL_CONSULTA_DOCUMENTOS          = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
    var _URL_LOADER_RECIBOS                 = '<s:url namespace="/general"         action="includes/loadRecibos" />';
    var _UrlDetalleSiniestroDirecto 		= '<s:url namespace="/siniestros" action="afiliadosAfectados"        />';

	Ext.onReady(function() {
		
		Ext.define('CurrencyField', {
		    extend: 'Ext.form.field.Display',
		    alias: ['widget.currencyfield'],
		    
		    valueToRaw: function(value) {
		        return Ext.util.Format.currency(value, '$ ', 2);
		    }
		});
		
		// Models:
		
		Ext.define('ModelInfoGralSiniestro',{
		    extend: 'Ext.data.Model',
		    fields: [
		        {type:'string', name:'CONTREC'},
				{type:'string', name:'DESSTATUS'},
				{type:'string', name:'CDSUCADM'},
				{type:'string', name:'CDSUCDOC'},
				{type:'string', name:'FERECEPC'},
				{type:'string', name:'FEOCURRE'},
				{type:'string', name:'CDCAUSA'},
				{type:'string', name:'PLANTARI'},
				{type:'string', name:'CIRHOSPI'},
				{type:'string', name:'DSZONAT'},
				{type:'string', name:'SUMAASEG'},
				{type:'string', name:'DSTIPPAG'},
				{type:'string', name:'NMPOLIEX'},
				{type:'string', name:'IMPORTE'},
				{type:'string', name:'FEINIVAL'},
				{type:'string', name:'FEFINVAL'},
				{type:'string', name:'STAPOLIZA'},
				{type:'string', name:'FEANTIG'},
				{type:'string', name:'FEALTA'},
				{type:'string', name:'CONTEO'},
				{type:'string', name:'DSPERPAG'},
				{type:'string', name:'ASEGURADO'},
				{type:'string', name:'DSPROVEED'},
				{type:'string', name:'CIRHOPROV'},
				{type:'string', name:'CDICD'},
				{type:'string', name:'CDICD2'},
				{type:'string', name:'NMRECLAMO'},
				{type:'string', name:'CDPERSON'},
				{type:'string', name:'BENEFICIARIO'},
				{type:'string', name:'FERECEP'}
		    ]
		});
	
	
		var storeInfoGralSiniestro=new Ext.data.Store({
	        autoDestroy: true,
	        model: 'ModelInfoGralSiniestro',
	        proxy: {
	            type: 'ajax',
	            url: _URL_INFO_GRAL_SINIESTRO,
	            reader: {
	                type: 'json',
	                root: 'siniestro'
	            }
	        }
	    });
		 
		var pnlInformacionGral= Ext.create('Ext.form.Panel',{
			url       : _URL_ACTUALIZA_INFO_GRAL_SIN,
		    border    : 0,
		    renderTo  : 'dvInformacionGeneral',
		    bodyPadding: 10,
		    width: 900,
		    layout :{
		        type   : 'table',
		        columns: 2
		    },
		    defaults:{
		    	colspan    : 2,
		        style      : 'margin:5px;',
		        labelWidth : 200
		    },
		    items:
		    [
				{
				    xtype      : 'hidden',
				    name       : 'NMRECLAMO'
				},{
				    xtype      : 'hidden',
				    name       : 'CDPERSON'
				},{
		            xtype      : 'displayfield',
		            name       : 'CONTREC',
		            fieldLabel : 'Contra recibo'
		        },{
		        	xtype      : 'displayfield',
		        	name       : 'DESSTATUS',
		            fieldLabel : 'Estado'
		        },{            
		        	xtype: 'combo',
					name:'CDSUCADM',
					fieldLabel: 'Oficina emisora',
					queryMode:'local',
					displayField: 'value',
					valueField: 'key',
					allowBlank:false,
					forceSelection : true,
					emptyText:'Seleccione...',
					store : Ext.create('Ext.data.Store', {
	                    model:'Generic',
	                    autoLoad:true,
	                    proxy: {
	                        type: 'ajax',
	                        url:  _URL_CATALOGOS,
	                        extraParams : {
	                        	catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>'
	                        },
	                        reader: {
	                            type: 'json',
	                            root: 'lista'
	                        }
	                    }
	                })
		        },{
		        	xtype: 'combo',
					name:'CDSUCDOC',
					fieldLabel: 'Oficina documento',
					queryMode:'local',
					displayField: 'value',
					valueField: 'key',
					allowBlank:false,
					forceSelection : true,
					emptyText:'Seleccione...',
					store : Ext.create('Ext.data.Store', {
	                    model:'Generic',
	                    autoLoad:true,
	                    proxy: {
	                        type: 'ajax',
	                        url:  _URL_CATALOGOS,
	                        extraParams : {
	                            catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_DOCUMENTO"/>'
	                        },
	                        reader: {
	                            type: 'json',
	                            root: 'lista'
	                        }
	                    }
	                })
		        },{
		            xtype      : 'displayfield',
		            fieldLabel : 'Fecha recepci&oacute;n',
		            name       : 'FERECEP'
		        },{
		            xtype      : 'displayfield',
		            fieldLabel : 'Fecha ocurrencia',
		            name       : 'FEOCURRE'
		        },{
		        	xtype       : 'combo',
					name        : 'CDCAUSA',
					fieldLabel  : 'Origen siniestro',
					queryMode   : 'local',
					displayField: 'value',
					valueField  : 'key',
					allowBlank  : false,
					forceSelection : true,
					emptyText   :'Seleccione...',
					store : Ext.create('Ext.data.Store', {
	                    model:'Generic',
	                    autoLoad:true,
	                    proxy: {
	                        type: 'ajax',
	                        url:  _URL_CATALOGOS,
	                        extraParams : {
	                            catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CAUSA_SINIESTRO"/>'
	                        },
	                        reader: {
	                            type: 'json',
	                            root: 'lista'
	                        }
	                    }
	                })
		        },{
		            xtype      : 'displayfield',
		            name       : 'PLANTARI',
		            fieldLabel : 'Plan'
		        },{
		            xtype      : 'displayfield',
		            fieldLabel : 'Circulo hospitalario',
		            name       : 'CIRHOSPI'
		        },{
		            xtype      : 'displayfield',
		            name       : 'DSZONAT',
		            fieldLabel : 'Zona tarificaci&oacute;n'
		        },{
		            xtype      : 'currencyfield',
		            name       : 'SUMAASEG',
		            fieldLabel : 'Suma asegurada contratada'
		        },{
                    xtype      : 'displayfield',
                    name       : 'DSTIPPAG',
                    fieldLabel : 'Tipo pago'
                },{
		        	colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'NMPOLIEX',
		            fieldLabel : 'P&oacute;liza'
		        },{
		        	colspan: 1,
		        	xtype  : 'button',
		        	text   : 'Ver detalle p&oacute;liza',
	                width  : 180,
	                icon   : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
	                handler: function() {
	                    var windowDetPol = Ext.create('Ext.window.Window', {
	                       title        : 'Detalle de la p&oacute;liza',
	                       modal       : true,
	                       buttonAlign : 'center',
	                       width       : 830,
	                       height      : 500,
	                       autoScroll  : true,
	                       items: [Ext.create('Ext.tab.Panel', {
		                           width: 830,
		                           items: [{
		                               title : 'DATOS DE LA POLIZA',
		                               loader: {
			                           		url: _URL_LOADER_DATOS_POLIZA,
			                           		scripts: true,
			                           		loadMask : true,
			                           		autoLoad: false,
			                           		ajaxOptions: {
			                	            	method: 'POST'
			                	            }
			                           	},
			                           	listeners: {
			                           		activate: function(tab) {
			                           			tab.loader.load({
			                           				params : {
			                                               'params.cdunieco': _CDUNIECO,
			                                               'params.cdramo'  : _CDRAMO,
			                                               'params.estado'  : _ESTADO,
			                                               'params.nmpoliza': _NMPOLIZA
			                                           }
			                           			});
			                           		}
			                           	}
		                           }, /*{
		                               //title: 'DATOS TARIFICACION',
		                           	title: 'COPAGOS',
		                           	loader: {
		                           		url: _URL_LOADER_VER_COBERTURAS,
		                           		scripts: true,
		                           		loadMask : true,
		                           		autoLoad: false,
		                           		ajaxOptions: {
		                	            	method: 'POST'
		                	            }
		                           	},
		                           	listeners: {
		                           		activate: function(tab) {
		                           			tab.loader.load({
		                           				params : {
		                                               'params.cdunieco'  : _CDUNIECO,
		                                               'params.cdramo'    : _CDRAMO,
		                                               'params.estado'    : _ESTADO,
		                                               'params.nmpoliza'  : _NMPOLIZA,
		                                               'params.suplemento': _NMSUPLEM
		                                           }
		                           			});
		                           		}
		                           	}
		                           },*/ {
		                               title: 'ASEGURADOS',
		                               loader: {
			                           		url: _URL_LOADER_ASEGURADOS_POLIZA,
			                           		scripts: true,
			                           		loadMask : true,
			                           		autoLoad: false,
			                           		ajaxOptions: {
			                	            	method: 'POST'
			                	            }
			                           	},
			                           	listeners: {
			                           		activate: function(tab) {
			                           			tab.loader.load({
			                           				params : {
			                                               'params.cdunieco'  : _CDUNIECO,
			                                               'params.cdramo'    : _CDRAMO,
			                                               'params.estado'    : _ESTADO,
			                                               'params.nmpoliza'  : _NMPOLIZA,
			                                               'params.suplemento': _NMSUPLEM
			                                           }
			                           			});
			                           		}
			                           	}
		                           },
		                           {
		                               //id: 'tbDocumentos',
		                               title : 'DOCUMENTACION',
		                               width: '350',
		                               loader : {
		                                   url : _URL_CONSULTA_DOCUMENTOS,
		                                   scripts : true,
		                                   autoLoad : false
		                               },
		                               listeners : {
		                                   activate : function(tab) {
		                                       tab.loader.load({
		                                           params : {
		                                               'smap1.readOnly': true,
		                                               'smap1.nmpoliza': _NMPOLIZA,
		                                               'smap1.cdunieco': _CDUNIECO,
		                                               'smap1.cdramo'  : _CDRAMO,
		                                               'smap1.estado'  : _ESTADO,
		                                               'smap1.nmsuplem': _NMSUPLEM,
		                                               'smap1.ntramite': _NTRAMITE,
		                                               'smap1.tipomov' : '0'
		                                           }
		                                       });
		                                   }
		                               }
		                           }
		                           
		                           /*{
		                               title : 'DOCUMENTACION',
		                               width: '350',
		                               loader : {
		                                   url : _URL_LOADER_CONSULTA_DOCUMENTOS,
		                                   scripts : true,
		                                   loadMask : true,
		                                   autoLoad : false,
		                                   ajaxOptions: {
			                	            	method: 'POST'
			                	           }
		                               },
		                               listeners : {
		                                   activate : function(tab) {
		                                       tab.loader.load({
		                                           params : {
		                                               'smap1.readOnly': true,
		                                               'smap1.nmpoliza': _NMPOLIZA,
		                                               'smap1.cdunieco': _CDUNIECO,
		                                               'smap1.cdramo'  : _CDRAMO,
		                                               'smap1.estado'  : _ESTADO,
		                                               'smap1.nmsuplem': _NMSUPLEM,
		                                               //'smap1.ntramite': _NTRAMITE,
		                                               'smap1.tipomov' : '0'
		                                           }
		                                       });
		                                   }
		                               }
		                           }*/, {
		                           	title: 'RECIBOS',
		                           	loader: {
		                           		url: _URL_LOADER_RECIBOS,
		                           		scripts: true,
		                           		loadMask : true,
		                           		autoLoad: false,
		                           		ajaxOptions: {
		                	            	method: 'POST'
		                	            }
		                           	},
		                           	listeners: {
		                           		activate: function(tab) {
		                           			tab.loader.load({
		                           				params : {
		                                               'params.cdunieco': _CDUNIECO,
		                                               'params.cdramo'  : _CDRAMO,
		                                               'params.estado'  : _ESTADO,
		                                               'params.nmpoliza': _NMPOLIZA,
		                                               'params.nmsuplem': _NMSUPLEM
		                                           }
		                           			});
		                           		}
		                           	}
		                           }]    
		                       })]
	                    }).show();
	                    centrarVentana(windowDetPol);
	                }
		        },{
		        	colspan    : 1,
		        	xtype      : 'currencyfield',
		            name       : 'IMPORTE',
		            fieldLabel : 'Suma asegurada disponible'
		        },{
		        	colspan    : 1,
		        	xtype: 'button',
		        	text: 'Ver coberturas',
	                width : 180,
	                icon : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
	                handler: function() {
	                	centrarVentanaInterna(Ext.create('Ext.window.Window', {
	                       title        : 'Coberturas de la p&oacute;liza',
	                       //modal       : true,
	                       buttonAlign : 'center',
	                       width       : 620,
	                       height      : 400,
	                       autoScroll  : true,
	                       loader      : {
	                           url      : _URL_LOADER_VER_COBERTURAS,
	                           scripts  : true,
	                           autoLoad : true,
	                           params:{
	                               'params.cdunieco'  : _CDUNIECO,
	                               'params.cdramo'    : _CDRAMO,
	                               'params.estado'    : _ESTADO,
	                               'params.nmpoliza'  : _NMPOLIZA,
	                               'params.suplemento': _NMSUPLEM
	                           }
	                       }
	                    }).showAt());
	                }
		        },{
		        	colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'FEINIVAL',
		            fieldLabel : 'Inicio vigencia'
		        },{
		        	colspan: 1,
		        	xtype  : 'button',
		        	text   : 'Ver historial de reclamaci&oacute;n',
	                width  : 180,
	                icon   : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
	                handler: function(){
	                	
	                	var windowHistSinies = Ext.create('Ext.window.Window',{
	            	        modal       : true,
	            	        buttonAlign : 'center',
	            	        width       : 800,
	            	        height      : 300,
	            	        autoScroll  : true,
	            	        loader      : {
	            	            url     : _URL_LOADER_HISTORIAL_RECLAMACIONES,
	            	            params  : {
	            	                'params.cdperson'  : pnlInformacionGral.down('[name=CDPERSON]').getValue()
	            	            },
	            	            scripts  : true,
	            	            loadMask : true,
	            	            autoLoad : true,
	            	            ajaxOptions: {
	            	            	method: 'POST'
	            	            }
	            	        },
	            	        buttons: [{
	            	        	 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
	            			     text: 'Cerrar',
	            			     handler: function() {windowHistSinies.close();}
	            	        }]
	            	    }).show();
	            	    centrarVentana(windowHistSinies);
	                } 
		        },{
		        	colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'FEFINVAL',
		            fieldLabel : 'Fin vigencia'
		        },{
		        	colspan : 1,
		        	xtype   : 'button',
		        	text    : 'Ver exclusiones p&oacute;liza',
	                width   : 180,
	                icon    : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
	                handler : function() {
	                	centrarVentanaInterna(Ext.create('Ext.window.Window', {
                           title       : 'Exclusiones de la p&oacute;liza',
                           modal       : true,
                           buttonAlign : 'center',
                           autoScroll  : true,
                           width       : 450,
                           height      : 455,
                           loader      : {
                               url     : _URL_LOADER_VER_EXCLUSIONES,
                               scripts : true,
                               autoLoad: true,
                               params:{
                                   'params.cdunieco': _CDUNIECO,
                                   'params.cdramo'  : _CDRAMO,
                                   'params.estado'  : _ESTADO,
                                   'params.nmpoliza': _NMPOLIZA,
                                   'params.nmsituac': _NMSITUAC//,
                                   //'params.nmsuplem': _NMSUPLEM
                               }
                           }
                        }).show());
                    }
		        },{
		        	//colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'STAPOLIZA',
		            fieldLabel : 'Estatus p&oacute;liza'
		        }/*,{
		        	colspan    : 1,
		        	xtype      : 'button',
		        	text       : 'Ver historial rehabilitaciones',
	                width      : 180,
	                icon       : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
	                disabled   : true
		        }*/,{
		        	xtype      : 'displayfield',
		        	name       : 'FEANTIG',
		            fieldLabel : 'Reconocimiento de Antig&uuml;edad'
		        },{
		        	colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'FEALTA',
		            fieldLabel : 'Antig&uuml;edad con General de Salud',
		            readOnly   : true
		        },{
		        	colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'CONTEO'
		            
		        },{
		            xtype      : 'displayfield',
		            name       : 'DSPERPAG',
		            fieldLabel : 'Forma de pago de la p&oacute;liza'
		        },{
					xtype      : 'displayfield',
					name       : 'ASEGURADO',
					fieldLabel : 'Asegurado afectado'
		        },{
                    //colspan    : 1,
                    xtype      : 'displayfield',
                    name       : 'BENEFICIARIO',
                    fieldLabel : 'Beneficiario'
                },{
                    colspan    : 1,
                    xtype      : 'displayfield',
                    name       : 'DSPROVEED',
                    fieldLabel : 'Proveedor',
                    hidden: (_TIPOPAGO == _REEMBOLSO)
                },{
		        	colspan    : 1,
		            xtype      : 'displayfield',
		            name       : 'CIRHOPROV',
		            fieldLabel : 'Circulo hospitalario',
		            labelWidth : 120,
		            hidden: (_TIPOPAGO == _REEMBOLSO)
		        },{
		        	colspan     : 1,
		        	xtype       : 'combo',
		        	name        : 'CDICD_TEMP',
		        	fieldLabel  : 'ICD',
		        	emptyText   : 'Seleccione...',
		        	valueField  : 'key',
		        	displayField: 'value',
		            forceSelection : true,
		            matchFieldWidth: false,
		            hideTrigger : true,
		            triggerAction: 'all',
		            queryMode   : 'remote',
	                queryParam  : 'params.otclave',
	                minChars    : 2,
	                store       : Ext.create('Ext.data.Store', {
	                    model:'Generic',
	                    autoLoad:false,
	                    proxy: {
	                        type: 'ajax',
	                        url : _URL_CATALOGOS,
	                        extraParams:{
	                            catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ICD"/>',
	                            'params.cdtabla' : '2TABLICD'
	                        },
	                        reader: {
	                            type: 'json',
	                            root: 'lista'
	                        }
	                    }
	                }),
	                listeners : {
	                	select : function(combo, records, eOpts ) {
                            pnlInformacionGral.down('[name=CDICD]').setValue(combo.getValue());
                        }
	                }
		        },{
                    colspan    : 1,
                    xtype      : 'displayfield',
                    name       : 'CDICD',
                    labelWidth : 120
                },{
		        	colspan     : 1,
		        	xtype       : 'combo',
		        	name        : 'CDICD2_TEMP',
					fieldLabel  : 'ICD secundario',
					emptyText   : 'Seleccione...',
					hidden: (_TIPOPAGO == _REEMBOLSO),
					valueField  : 'key',
					displayField: 'value',
					forceSelection : true,
					matchFieldWidth: false,
					hideTrigger : true,
					triggerAction: 'all',
					queryMode   : 'remote',
	                queryParam  : 'params.otclave',
	                minChars    : 2,
					store       : Ext.create('Ext.data.Store', {
				        model:'Generic',
				        autoLoad:false,
				        proxy: {
				            type: 'ajax',
				            url : _URL_CATALOGOS,
				            extraParams:{
				            	catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ICD"/>',
				                'params.cdtabla' : '2TABLICD'
				            },
				            reader: {
				                type: 'json',
				                root: 'lista'
				            }
				        }
				    }),
                    listeners : {
                        select : function(combo, records, eOpts ) {
                            pnlInformacionGral.down('[name=CDICD2]').setValue(combo.getValue());
                        }
                    }
		        },{
                    colspan    : 1,
                    xtype      : 'displayfield',
                    name       : 'CDICD2',
                    labelWidth : 120
                }
		    ]/*,
		    buttonAlign:'center',
		    buttons: [{
		        icon:_CONTEXT+'/resources/fam3icons/icons/calculator.png',
		        text: 'Guardar',
		        handler: function() {
		            
		        	var form = this.up('form').getForm();
		            
		            if (form.isValid()){
		            	
		            	form.submit({
		            	    params: {
		            	        'params.cdunieco' : _CDUNIECO,
		            	        'params.cdramo'   : _CDRAMO,
		            	        'params.estado'   : _ESTADO,
		            	        'params.nmpoliza' : _NMPOLIZA,
		            	        'params.nmsuplem' : _NMSUPLEM,
		            	        'params.aaapertu' : _AAAPERTU,
		            	        'params.nmsinies' : _NMSINIES,
		            	        'params.feocurre' : pnlInformacionGral.down('[name=FEOCURRE]').getValue(),
		            	        'params.nmreclamo': pnlInformacionGral.down('[name=NMRECLAMO]').getValue(),
		            	        'params.cdicd'    : pnlInformacionGral.down('[name=CDICD]').getValue(),
		            	        'params.cdicd2'   : pnlInformacionGral.down('[name=CDICD2]').getValue(),
		            	        'params.cdcausa'  : pnlInformacionGral.down('[name=CDCAUSA]').getValue(),
		            	        'params.ntramite' : _NTRAMITE,
		            	        'params.cdsucadm' : pnlInformacionGral.down('[name=CDSUCADM]').getValue(),
		            	        'params.cdsucdoc' : pnlInformacionGral.down('[name=CDSUCDOC]').getValue()
		            	    },
			                success: function(form, action) {
			                	centrarVentanaInterna(Ext.Msg.show({
								      title:'Exito',
								      msg: 'Se actualizaron los datos generales del siniestro',
								      buttons: Ext.Msg.OK,
								      icon: Ext.Msg.WARNING
								}));
			                },
			                failure: function(form, action) {
			                    switch (action.failureType) {
			                        case Ext.form.action.Action.CONNECT_FAILURE:
			                        	centrarVentanaInterna(Ext.Msg.alert('Error', 'Error de comunicaci&oacute;n'));
			                            break;
			                        case Ext.form.action.Action.SERVER_INVALID:
			                        case Ext.form.action.Action.LOAD_FAILURE:
			                        	centrarVentanaInterna(Ext.Msg.alert('Error', 'Error del servidor, consulte a soporte'));
			                            break;
			                   }
			                }
		            	});
		            	
		            } else {
		            	centrarVentanaInterna(Ext.Msg.show({
		                    title:'Datos incompletos',
		                    msg: 'Favor de introducir todos los campos requeridos',
		                    buttons: Ext.Msg.OK,
		                    icon: Ext.Msg.WARNING
		                }));
		            }
		        }
		    },{
		        text:'Restaurar',
		        icon:_CONTEXT+'/resources/fam3icons/icons/arrow_refresh.png',
		        handler:function(){
		        	this.up('form').getForm().reset();
		        }
		    },{
		        text:'Regresar',
		        icon:_CONTEXT+'/resources/fam3icons/icons/application_side_contract.png',
		        hidden: (_TIPOPAGO == _REEMBOLSO),
		        handler:function(){
		        	Ext.create('Ext.form.Panel').submit(
        			{
        				url             : _UrlDetalleSiniestroDirecto
        				,params         : {	'params.tipopago' :_TIPOPAGO,
        									'params.ntramite' : _NTRAMITE}
        			    ,standardSubmit : true
        			});
		        }
		    ]}*/
		});
		
		storeInfoGralSiniestro.load({
            params: {
                'params.cdunieco' : _CDUNIECO,
                'params.cdramo'   : _CDRAMO,
                'params.estado'   : _ESTADO,
                'params.nmpoliza' : _NMPOLIZA,
                'params.nmsituac' : _NMSITUAC,
                'params.nmsuplem' : _NMSUPLEM,
                'params.status'   : _STATUS,
                'params.aaapertu' : _AAAPERTU,
                'params.nmsinies' : _NMSINIES,
                'params.ntramite' : _NTRAMITE
            },
            callback: function(records, operation, success) {
                if(success){
                    if(records.length > 0){
                    	pnlInformacionGral.getForm().loadRecord(records[0]);  
                    }else {
                    	centrarVentanaInterna(showMessage('Error', 'No hay datos de la p&oacute;liza', Ext.Msg.OK, Ext.Msg.ERROR));
                    }
                }else {
                	centrarVentanaInterna(showMessage('Error', 'Error al obtener los datos de la p&oacute;liza, intente m\u00E1s tarde',
                    Ext.Msg.OK, Ext.Msg.ERROR));
                }
            }
        });
		
	
	});
</script>
<div id="dvInformacionGeneral" style="height:1000px;"></div>