<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Coberturas</title>
        <script type="text/javascript">
            ////// variables //////
            // Obtenemos el contenido en formato JSON de la propiedad solicitada:
            var _selCobParams = <s:property value="%{convertToJSON('params')}" escapeHtml="false"/>;
            var _CONTEXT = '${ctx}';
            var _selCobForm;
            var gridFacturasTramite;
            var ventanaDetalleCobertura;
            var _selCobUrlSave                = '<s:url namespace="/siniestros" 	action="guardarSeleccionCobertura"/>';
            var _selCobUrlSavexTramite        = '<s:url namespace="/siniestros" 	action="guardarSeleccionCoberturaxTramite"/>';
            var _selCobUrlAvanza              = '<s:url namespace="/siniestros" 	action="afiliadosAfectados"/>';
            var _selCobUrlAvanzaReembolso     = '<s:url namespace="/siniestros" 	action="detalleSiniestro"/>';
            //var _selDetalleSiniestoDatos    = '<s:url namespace="/siniestros" 	action="detalleSiniestroDatos"/>';
            var _URL_LoadFacturasxTramite     = '<s:url namespace="/siniestros" 	action="obtenerFacturasTramite"/>';
            var _URL_guardarCoberturaxFactura = '<s:url namespace="/siniestros" 	action="guardarCoberturaxFactura"/>';
            var _PeriodoEspera                = '<s:url namespace="/siniestros" 	action="obtenerPeriodoEspera"/>';
            var _URL_DATOS_INFONAVIT          = '<s:url namespace="/siniestros" 	action="obtenerInfCoberturaInfonavit"/>';
            var _UrlRechazarTramiteWindwow    = '<s:url namespace="/siniestros" 	action="includes/rechazoReclamaciones" />';
            var _URL_MESACONTROL			  = '<s:url namespace="/mesacontrol" 	action="mcdinamica" />';
            var _URL_VAL_CONDICIONGRAL		  = '<s:url namespace="/siniestros" 	action="consultaInfCausaSiniestroProducto" />';
            var _TIPO_PAGO_REEMBOLSO		  = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo"/>';
            var _SALUD_VITAL				  = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@SALUD_VITAL.cdramo" />';
            var _MULTISALUD					  = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@MULTISALUD.cdramo" />';
            var _GMMI						  = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES.cdramo" />';
            var _GMMPRUEBA                    = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES_PRUEBA.cdramo" />';
            var _RECUPERA					  = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@RECUPERA.cdramo" />';
            var _SINIESTRO					  = '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@SINIESTRO.cdtiptra"/>';
            var ntramite= null;
            var nfactura = null;
            var ffactura = null;
            var cdtipser = null;
            var cdpresta = null;
            var ptimport = null;
            var descporc = null;
            var descnume = null;
            var tasacamb = null;
            var ptimporta = null;
            var dctonuex = null;
            var cdmoneda = null;
            var tipoAccion = null;
            var nombProv = null;

            debug('_selCobParams:',_selCobParams);
            ////// variables //////
            Ext.onReady(function()
            {
                ////// modelos //////
                Ext.define('modeloFactura',
                {
                    extend  : 'Ext.data.Model'
                    ,fields : [ <s:property value="imap.modelFacturas" /> ]
                });
                ////// stores //////
                storeFacturasTramite = new Ext.data.Store(
                {
                    autoDestroy: true,
                    model: 'modeloFactura',
                    proxy: {
                        type: 'ajax',
                        url: _URL_LoadFacturasxTramite,
                        reader: {
                            type: 'json',
                            root: 'slist1'
                        }
                    }
                });
                ////// contenido //////
                _selCobForm = Ext.create('Ext.form.Panel',
                {
                    //title        : 'DETALLE DE COBERTURA'
                    //,
                    buttonAlign : 'center'
                    ,items       : [ <s:property value="imap.item" /> ]
                    ,listeners   : { afterrender : function(me){heredarPanel(me);} }
                    ,buttons     :
                        [
                            {
                                icon     : '${ctx}/resources/fam3icons/icons/disk.png'
                                ,text    : 'Guardar'
                                ,handler : _selCobGuardar
                                ,hidden	 : true
                            }
                        ]
                });

                _selCobForm.items.items[2].on('blur',function()
                {
                    var comboCoberturas = _selCobForm.items.items[3];
                    comboCoberturas.getStore().load(
                    {
                        params :
                        {
                            'params.cdramo'  : _selCobForm.items.items[1].getValue()
                            ,'params.cdtipsit' : _selCobForm.items.items[2].getValue()
                            ,'params.ntramite'  : _selCobParams.ntramite
                            ,'params.tipopago'  :_selCobParams.tipopago
                            ,'params.estado'  : _selCobParams.estado
                            ,'params.cdunieco'  : _selCobParams.cdunieco
                            ,'params.nmpoliza'  : _selCobParams.nmpoliza
                            ,'params.nmsituac'  : _selCobParams.nmsituac
                        }
                    });
                });

                _selCobForm.items.items[3].on('focus',function()
                {
                    var comboCoberturas = _selCobForm.items.items[3];
                    comboCoberturas.getStore().load(
                    {
                        params :
                        {
                            'params.cdramo'  : _selCobForm.items.items[1].getValue()
                            ,'params.cdtipsit' : _selCobForm.items.items[2].getValue()
                            ,'params.ntramite'  : _selCobParams.ntramite
                            ,'params.tipopago'  :_selCobParams.tipopago
                            ,'params.estado'    : _selCobParams.estado
                            ,'params.cdunieco'  : _selCobParams.cdunieco
                            ,'params.nmpoliza'  : _selCobParams.nmpoliza
                            ,'params.nmsituac'  : _selCobParams.nmsituac
                        }
                    });
                });
                
                _selCobForm.items.items[3].on('blur',function()
                        {
                            var comboSubcobertura = _selCobForm.items.items[4];
                            comboSubcobertura.getStore().load(
                            {
                                params :
                                {
                                    'params.cdramo'  : _selCobForm.items.items[1].getValue()
                                    ,'params.cdgarant'  : _selCobForm.items.items[3].getValue()
                                    ,'params.cdtipsit' : _selCobForm.items.items[2].getValue()
                                    ,'params.ntramite'  : _selCobParams.ntramite
                                    ,'params.tipopago'  :_selCobParams.tipopago
                                    ,'params.estado'  : _selCobParams.estado
                                    ,'params.cdunieco'  : _selCobParams.cdunieco
                                    ,'params.nmpoliza'  : _selCobParams.nmpoliza
                                    ,'params.nmsituac'  : _selCobParams.nmsituac
                                }
                            });
                        });

                        _selCobForm.items.items[4].on('focus',function()
                        {
                            var comboSubcobertura = _selCobForm.items.items[4];
                            comboSubcobertura.getStore().load(
                            {
                                params :
                                {
                                    'params.cdramo'  : _selCobForm.items.items[1].getValue()
                                    ,'params.cdgarant'  : _selCobForm.items.items[3].getValue()
                                    ,'params.cdtipsit' : _selCobForm.items.items[2].getValue()
                                    ,'params.ntramite'  : _selCobParams.ntramite
                                    ,'params.tipopago'  :_selCobParams.tipopago
                                    ,'params.estado'    : _selCobParams.estado
                                    ,'params.cdunieco'  : _selCobParams.cdunieco
                                    ,'params.nmpoliza'  : _selCobParams.nmpoliza
                                    ,'params.nmsituac'  : _selCobParams.nmsituac
                                }
                            });
                        });

                /*/////////////////DECLARACION DE GRID FACTURAS ///////////////*/
                Ext.define('EditorFacturasTramite', {
                    extend: 'Ext.grid.Panel',
                    requires: [
                        'Ext.selection.CellModel',
                        'Ext.grid.*',
                        'Ext.data.*',
                        'Ext.util.*',
                        'Ext.form.*'
                    ],
                    //selType: 'checkboxmodel',
                    title: 'Facturas en Tr&aacute;mite  Contra-Recibo: '+_selCobParams.ntramite,
                    frame: false,
                    initComponent: function(){
                        this.cellEditing = new Ext.grid.plugin.CellEditing({
                            clicksToEdit: 1
                        });
                        Ext.apply(this, {
                            height: 450,
                            plugins: [this.cellEditing],
                            store: storeFacturasTramite,
                            columns:[
                                        {
                                            xtype : 'actioncolumn',
                                            width : 50,
                                            sortable : false,
                                            menuDisabled : true,
                                            items : [{
                                                        icon : '${ctx}/resources/fam3icons/icons/pencil.png',
                                                        tooltip : 'Editar Factura',
                                                        scope : this,
                                                        handler : this.onEditClick
                                                    }]
                                        },
                                        <s:property value="imap.columnas" />
                                    ],
                            viewConfig :
                            {
                                listeners :
                                {
                                    refresh : function(dataview)
                                    {
                                        Ext.each(dataview.panel.columns, function(column)
                                        {
                                            column.autoSize();
                                        });
                                    }
                                }
                            }
                        });
                        this.callParent();
                    },
                    getColumnIndexes: function (){
                        var me, columnIndexes;
                        me = this;
                        columnIndexes = [];
                        Ext.Array.each(me.columns, function (column)
                        {
                            if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
                                columnIndexes.push(column.dataIndex);
                            } else {
                                columnIndexes.push(undefined);
                            }
                        });
                        return columnIndexes;
                    },
                    validateRow: function (columnIndexes,record, y)
                        //hace que una celda de columna con allowblank=false tenga el estilo rojito
                    {
                        var view = this.getView();
                        Ext.each(columnIndexes, function (columnIndex, x)
                        {
                            if(columnIndex)
                            {
                                var cell=view.getCellByPosition({row: y, column: x});
                                cellValue=record.get(columnIndex);
                                if(cell.addCls&&((!cellValue)||(cellValue.lenght==0))){
                                    cell.addCls("custom-x-form-invalid-field");
                                }
                            }
                        });
                        return false;
                    },
                    onEditClick: function(grid, rowIndex){
                        var record=grid.getStore().getAt(rowIndex);
                       debug("VALOR DEL RECORD --->",record);
                        if(record.data.CDGARANT == null ||record.data.CDGARANT == ""){
                            tipoAccion = "I";
                        }else{
                            tipoAccion = "U";
                        }
                        ntramite = record.data.NTRAMITE;
                        nfactura = record.data.NFACTURA;
                        ffactura = record.data.FFACTURA;
                        cdtipser = record.data.CDTIPSER;
                        cdpresta = record.data.CDPRESTA;
                        nombProv = record.data.NOMBREPROVEEDOR;
                        ptimport = record.data.PTIMPORT;
                        descporc = record.data.DESCPORC;
                        descnume = record.data.DESCNUME;
                        tasacamb = record.data.TASACAMB;
                        ptimporta = record.data.PTIMPORTA;
                        dctonuex = record.data.DCTONUEX;
                        cdmoneda = record.data.CDMONEDA;
                        ventanaDetalleCobertura.show();
                    },
                    buttonAlign : 'center',
                    buttons:[
                    {
                        text: 'Continuar',
                        icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                        handler: function() {
							//SE REALIZA LA VALIDACION PARA RECUPERA
							debug("Valores de entrada validacion -->",_selCobParams);
							
							var obtener = [];
							storeFacturasTramite.each(function(record) {
								obtener.push(record.data);
							});
							var  bande=true;
							var coberturaInt = null;
							var subcoberInt = null;
							for(var i=0;i < obtener.length;i++){
								if(obtener[i].CDGARANT == null ||obtener[i].CDGARANT.length <= 0  ){
									bande=false;
									break;
								}else{
									coberturaInt= obtener[i].CDGARANT;
									subcoberInt = obtener[i].CDCONVAL;
								}
							}
							if(bande){
								_selCobForm.setLoading(true);
								var json = _selCobForm.getValues();
								json['ntramite'] = _selCobParams.ntramite;
								json['cdgarant'] = coberturaInt;
								json['cdconval'] = subcoberInt;
								
								//Validamos la informacion general  por producto 1.- Periodo Espera x producto 2.- Maximo de Consultas
								var periodoEspera = true;
								var maxconsultas  = true;
								
								Ext.Ajax.request({
									url     : _URL_VAL_CONDICIONGRAL
									,params : {
										'params.cdramo'   : _selCobParams.cdramo,
										'params.cdtipsit' : _selCobForm.items.items[2].getValue(),
										'params.causaSini': 'VALGRAL',
										'params.cveCausa' : '1'
									}
									,success : function (response){
										var datosExtras = Ext.decode(response.responseText);
										if(Ext.decode(response.responseText).datosInformacionAdicional != null){
											var cveCauSini=Ext.decode(response.responseText).datosInformacionAdicional[0];
											debug("cveCauSini.REQVALIDACION ==>",cveCauSini.REQVALIDACION,"cveCauSini.REQCONSULTAS ===>",cveCauSini.REQCONSULTAS);
											if(cveCauSini.REQVALIDACION =="S" || cveCauSini.REQCONSULTAS =="S" ){
												if(cveCauSini.REQVALIDACION =="S"){//1.- Validacion del periodo de espera
													Ext.Ajax.request( {
														url		:	_PeriodoEspera
														,params	:	{
															'params.cdunieco'  : _selCobParams.cdunieco,
															'params.cdramo'    : _selCobParams.cdramo,
															'params.estado'    : _selCobParams.estado,
															'params.nmpoliza'  : _selCobParams.nmpoliza,
															'params.nmsituac'  : _selCobParams.nmsituac,
															'params.feocurre'  : _selCobParams.feocurre,
															'params.cdgarant'  : coberturaInt,
															'params.cdconval'  : subcoberInt
														}
														,success : function (response){
															var jsonRes = Ext.decode(response.responseText);
															debug("Valor de Respuesta ===>",jsonRes.success);
															if(jsonRes.success == false){
																periodoEspera = jsonRes.success;
																centrarVentanaInterna(Ext.Msg.show({
																	title:'Error',
																	msg: jsonRes.mensaje,
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																}));
															}
														},
														failure : function (){
															me.up().up().setLoading(false);
															centrarVentanaInterna(Ext.Msg.show({
																title:'Error',
																msg: 'Error de comunicaci&oacute;n',
																buttons: Ext.Msg.OK,
																icon: Ext.Msg.ERROR
															}));
														}
													});
												}
												if(cveCauSini.REQCONSULTAS =="S"){//2.- No. de Consultas
													Ext.Ajax.request({
														url		:	_URL_DATOS_INFONAVIT
														,params	:	{
															'params.cdunieco'  : _selCobParams.cdunieco,
															'params.cdramo'    : _selCobParams.cdramo,
															'params.estado'    : _selCobParams.estado,
															'params.nmpoliza'  : _selCobParams.nmpoliza,
															'params.nmsuplem'  :  _selCobParams.nmsuplem,
															'params.nmsituac'  : _selCobParams.nmsituac,
															'params.cdgarant'  : coberturaInt,
															'params.cdconval'  : subcoberInt,
															'params.nmsinies'  : null
														}
														,success : function (response){
															var jsonRes = Ext.decode(response.responseText);
															debug("Valor de Respuesta ===>",jsonRes);
															if(jsonRes.success == true){
																var infonavit = Ext.decode(response.responseText).datosInformacionAdicional[0];
																debug("infonavit ===>",infonavit);
																var consultasTotales = infonavit.NO_CONSULTAS;
																var maxConsulta      = infonavit.OTVALOR07;
																var diferenciador    = infonavit.OTVALOR15;
																debug("consultasTotales =>",consultasTotales,"maxConsulta =>",maxConsulta);
																debug("diferenciador =>",diferenciador);
																
																if(diferenciador == "MEI"){
																	if(+consultasTotales >= +maxConsulta){
																		maxconsultas = false;
							        				    				centrarVentanaInterna(Ext.Msg.show({
							        				    	                   title: 'Aviso',
							        				    	                   msg: 'Se sobrepas&oacute; el n&uacute;mero m&aacute;ximo de servicios para este Asegurado.',
							        				    	                   buttons: Ext.Msg.OK,
							        				    	                   icon: Ext.Msg.WARNING
																		}));
																	}
																}else{
																	if(+consultasTotales <= +maxConsulta){
																		maxconsultas = false;
																		centrarVentanaInterna(Ext.Msg.show({
							        				    	                   title: 'Aviso',
							        				    	                   msg: 'Se sobrepas&oacute; el n&uacote;mero m&aacute;ximo de servicios para este Asegurado.',
							        				    	                   buttons: Ext.Msg.OK,
							        				    	                   icon: Ext.Msg.WARNING
																		}));
																	}
																}
																//alert("Valor de respuesta ==> "+infonavit.MONTO);
															}else{
																maxconsultas = jsonRes.success;
																centrarVentanaInterna(Ext.Msg.show({
																	title:'Error',
																	msg: jsonRes.mensaje,
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																}));
															}
														},
														failure : function (){
															me.up().up().setLoading(false);
															centrarVentanaInterna(Ext.Msg.show({
																title:'Error',
																msg: 'Error de comunicaci&oacute;n',
																buttons: Ext.Msg.OK,
																icon: Ext.Msg.ERROR
															}));
														}
													});
												}
												if(periodoEspera == true && maxconsultas  == true){
													Ext.Ajax.request({
														url       : _selCobUrlSavexTramite
														,jsonData : {
															params : json
														}
														,params	:	{
															'params.cdramo'    : _selCobParams.cdramo,
															'params.cdgarant'  : _selCobParams.estado,
															'params.nmpoliza'  : _selCobParams.nmpoliza,
															'params.nmsituac'  : _selCobParams.nmsituac,
															'params.feocurre'  : _selCobParams.feocurre,
															'params.cdgarant'  : coberturaInt,
															'params.cdconval'  : subcoberInt
														}
														,success  : function(response){
															_selCobForm.setLoading(false);
															json = Ext.decode(response.responseText);
															debug('respuesta:',json);
															_selCobAvanza();
														}
														,failure  : function(response){
															_selCobForm.setLoading(false);
															json = Ext.decode(response.responseText);
															debug('respuesta:',json);
															centrarVentanaInterna(mensajeError(json.mensaje));
														}
													});
												}
											}else{
												Ext.Ajax.request({
													url       : _selCobUrlSavexTramite
													,jsonData : {
														params : json
													}
													,success  : function(response){
														_selCobForm.setLoading(false);
														json = Ext.decode(response.responseText);
														debug('respuesta:',json);
														_selCobAvanza();
													}
													,failure  : function(response){
														_selCobForm.setLoading(false);
														json = Ext.decode(response.responseText);
														debug('respuesta:',json);
														centrarVentanaInterna(mensajeError(json.mensaje));
													}
												});
											}
										}
									},failure : function (){
										centrarVentanaInterna(Ext.Msg.show({
											title:'Error',
											msg: 'Error de comunicaci&oacute;n',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR
										}));
									}
								});
							}else{
								centrarVentanaInterna(mensajeError("Verificar la Cobertura - Subcobertura de las factura"));
							}
						}
					},
					{
						text     : 'Regresar'
							,icon    : _CONTEXT+'/resources/fam3icons/icons/book_previous.png'
							,handler : _11_regresarMC
					}]
				});
                gridFacturasTramite = new EditorFacturasTramite();
                
                ventanaDetalleCobertura = Ext.create('Ext.window.Window', {
                    closeAction: 'hide',
                    title        : 'DETALLE DE COBERTURA',
                    modal: true, 
                    resizable: false,
                    items:[_selCobForm],
                    buttonAlign : 'center',
                    buttons:[
                        {   
                            text: 'Aceptar',
                            icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                            handler: function() {
                                //var recordFactura = gridFacturasTramite.getSelectionModel().getSelection()[0];
                                if (_selCobForm.form.isValid()) {
                                    _selCobForm.form.submit({
                                        waitMsg:'Procesando...',	
                                        url: _URL_guardarCoberturaxFactura,
                                        params: {
                                            'params.ntramite'    : ntramite,
                                            'params.nfactura'    : nfactura,
                                            'params.ffactura'    : ffactura,
                                            'params.cdtipser'    : cdtipser,
                                            'params.cdpresta'    : cdpresta,
                                            'params.ptimport'    : ptimport,
                                            'params.descporc'    : descporc,
                                            'params.descnume'    : descnume,
                                            'params.tasacamb'    : tasacamb,
                                            'params.ptimporta'   : ptimporta,
                                            'params.dctonuex'    : dctonuex,
                                            'params.cdmoneda'    : cdmoneda,
                                            'params.cdgarant'    : _selCobForm.items.items[3].getValue(),
                                            'params.cdconval'    : _selCobForm.items.items[4].getValue(),
                                            'params.tipoAccion'  : tipoAccion,
                                            'params.cdramo'      : _selCobParams.cdramo,
                                            'params.nombProv'    : nombProv
                                        },
                                        failure: function(form, action) {
                                            centrarVentanaInterna(mensajeError("Error al guardar la cobertura y subcobertura "));
                                        },
                                        success: function(form, action) {
                                            storeFacturasTramite.reload();
                                            _selCobForm.getForm().reset();
                                            ventanaDetalleCobertura.close();
                                            //centrarVentanaInterna(mensajeCorrecto("Aviso","Se ha guardado la cobertura y subcobertura"));	
                                        }
                                    });

                                } else {
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title: 'Aviso',
                                        msg: 'Complete la informaci&oacute;n requerida',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.WARNING
                                    }));
                                }
                            }
                        },
                        {
                            text: 'Cancelar',
                            icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                            handler: function() {
                                _selCobForm.getForm().reset();
                                ventanaDetalleCobertura.close();
                            }
                        }
                    ]
                });
                
                /*Cargamos la información de las facturas*/
                if(true){
                    var params = {
                        'smap.ntramite' : _selCobParams.ntramite
                    };
                    storeFacturasTramite.load({
                        params: params,
                        callback: function(records, operation, success){
                            if(success){
                                if(records.length <= 0){
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title: 'Aviso',
                                        msg: 'No se encontraron datos',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    }));
                                }
                            }else{
                                centrarVentanaInterna(Ext.Msg.show({
                                    title: 'Error',
                                    msg: 'Error al obtener los datos',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                }));
                            }
                        }
                    });

                    panelPrincipal = Ext.create('Ext.form.Panel',{
                        renderTo: 'selcobdivpri',
                        border     : false
                        ,bodyStyle:'padding:5px;'
                        ,items      : [
                            gridFacturasTramite
                        ]
                    });
                }
            });
            ////// funciones //////
            	function _11_regresarMC()
				{
					debug('_11_regresarMC');
					Ext.create('Ext.form.Panel').submit({
						url				: _URL_MESACONTROL
						,standardSubmit	:true
						,params			:
						{
							'smap1.gridTitle'		: 'Siniestros'
							,'smap2.pv_cdtiptra_i'	: _SINIESTRO
						}
					});
				}
            
            function _selCobGuardar()
            {
                var valido = true;
                if(valido)
                {
                    valido = _selCobForm.isValid();
                    if(!valido)
                    {
                        datosIncompletos();
                    }
                }
                if(valido)
                {
                    _selCobForm.setLoading(true);
                    var json = _selCobForm.getValues();
                    json['ntramite'] = _selCobParams.ntramite;
                    Ext.Ajax.request(
                    {
                        url       : _selCobUrlSave
                        ,jsonData :
                        {
                            params : json
                        }
                        ,success  : function(response)
                        {
                            _selCobForm.setLoading(false);
                            json = Ext.decode(response.responseText);
                            debug('respuesta:',json);
                            centrarVentanaInterna(mensajeCorrecto(json.mensaje,json.mensaje,_selCobAvanza));
                        }
                        ,failure  : function(response)
                        {
                            _selCobForm.setLoading(false);
                            json = Ext.decode(response.responseText);
                            debug('respuesta:',json);
                            centrarVentanaInterna(mensajeError(json.mensaje));
                        }
                    });
                }
            }
            function _selCobAvanza()
            {
            	console.log("_selCobParams");
            	console.log(_selCobParams);
                var params =
                {
                    'params.ntramite' : _selCobParams.ntramite,
                    'params.tipopago' : _selCobParams.tipopago,
                    'params.cdrol' : _selCobParams.cdrol
                }
                
                Ext.create('Ext.form.Panel').submit(
                {
                    url             : _selCobUrlAvanza//_selCobParams.otvalor02==TipoPago.Directo ? _selCobUrlAvanza : _selCobUrlAvanzaReembolso
                    ,standardSubmit : true
                    ,params         : params
                });
            }
        </script>
    </head>
    <body>
        <div id="selcobdivpri" style="height:600px;"></div>
    </body>
</html>