<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:if test="false">
<script>
</s:if>
debug('#################################################');
debug('###### scriptMesaAutorizacionServicios.jsp ######');
debug('#################################################');

/* ******************** CATALOGOS ******************** */

// Catalogo Estatus de tramite a utilizar:
var _STATUS_PENDIENTE                 = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@PENDIENTE.codigo" />';
var _STATUS_EN_CAPTURA                = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA.codigo" />';
var _STATUS_EN_CAPTURA_CMM			  = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA_CMM.codigo" />';
var _STATUS_EN_ESPERA_DE_AUTORIZACION = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_AUTORIZACION.codigo" />';
var _STATUS_TRAMITE_RECHAZADO         = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';
var _STATUS_TRAMITE_CONFIRMADO        = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@CONFIRMADO.codigo" />'; 
//Catalogo Tipos de tramite a utilizar:
var _TIPO_TRAMITE_AUTORIZACION_SERVICIOS = '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@AUTORIZACION_SERVICIOS.cdtiptra" />';
/* *************************************************** */
///////////////////////
////// variables //////
var _4_urlAutorizarEndoso 			= '<s:url namespace="/endosos" 		action="autorizarEndoso" />';
var _4_authEndUrlDoc      			= '<s:url namespace="/documentos" 	action="ventanaDocumentosPolizaClon" />';

var _4_urlPantallaAutServ 			= '<s:url namespace="/siniestros" 	action="autorizacionServicios" />';

var _UrlAutorizacionServicio    	= '<s:url namespace="/siniestros" 	action="autorizacionServicios" />';
var _UrlRechazarTramiteWindwow  	= '<s:url namespace="/siniestros" 	action="includes/rechazoReclamaciones" />';

var _UrlValidaAutoProceso    		= '<s:url namespace="/siniestros" 	action="validaAutorizacionProceso" />';
var panDocUrlViewDoc     			= '<s:url namespace ="/documentos" 	action="descargaDocInline" />';
var _URL_ActualizaStatusTramite 	= '<s:url namespace="/mesacontrol" 	action="actualizarStatusTramite" />';
var _URL_TURNAR_AUTSERVICIO      	= '<s:url namespace="/mesacontrol" 	action="turnarAutorizacionServicio" />';
var _URL_ActualizaStatusMAUTSERV 	= '<s:url namespace="/siniestros" 	action="cambiarEstatusMAUTSERV" />';
var _URL_Existe_Documentos 			= '<s:url namespace="/siniestros" 	action="validaDocumentosAutoServ" />';

var mesConUrlDetMC        			= '<s:url namespace="/mesacontrol" 	action="obtenerDetallesTramite"    />';
var mesConUrlFinDetalleMC 			= '<s:url namespace="/mesacontrol" 	action="finalizarDetalleTramiteMC" />';
var compleUrlGuardarCartoRechazo 	= '<s:url namespace="/siniestros" 	action="guardarCartaRechazoAutServ" />';
var _URL_CONSULTA_CLAUSU_DETALLE 	= '<s:url namespace="/catalogos" 	action="consultaClausulaDetalle" />';
var _URL_CONSULTA_CLAUSU 			= '<s:url namespace="/catalogos" 	action="consultaClausulas" />';
var _URL_NOMBRE_TURNADO   			= '<s:url namespace="/siniestros" 	action="obtieneUsuarioTurnado" />';

//UserVO usuario  = (UserVO)session.get("USUARIO");
//String cdrol    = usuario.getRolActivo().getObjeto().getValue();

var _4_selectedRecordEndoso;
//var _4_windowAutorizarEndoso;
//var _4_fieldComentAuthEndoso;

_4_botonesGrid.push({
	text     : 'Alta de tr&aacute;mite'
	,icon    : '${ctx}/resources/fam3icons/icons/add.png'
	,handler : function altaTramiteWindow(){
		Ext.create("Ext.form.Panel").submit({url     : _4_urlPantallaAutServ,standardSubmit:true});
	}
});
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
function _4_authEndosoDocumentos(grid,rowIndex,colIndex){
	var store=grid.getStore();
	var record=store.getAt(rowIndex);
	debug('record seleccionado',record);
	
	Ext.create('Ext.window.Window',{
		title        : 'Documentos del tr&aacute;mite '+record.get('ntramite')
		,modal       : true
		,buttonAlign : 'center'
		,width       : 600
		,height      : 400
		,autoScroll  : true
		,cls         : 'VENTANA_DOCUMENTOS_CLASS'
		,loader      :{
			url       : _4_authEndUrlDoc
			,params   :{
				'smap1.nmpoliza'  : record.get('nmpoliza')
				,'smap1.cdunieco' : record.get('cdunieco')
				,'smap1.cdramo'   : record.get('cdramo')
				,'smap1.estado'   : record.get('estado')
				,'smap1.nmsuplem' : record.raw.nmsuplem
				,'smap1.ntramite' : record.get('ntramite')
				,'smap1.nmsolici' : record.raw.nmsolici
				,'smap1.tipomov'  : _TIPO_TRAMITE_AUTORIZACION_SERVICIOS
				,'smap1.cdtiptra' : _TIPO_TRAMITE_AUTORIZACION_SERVICIOS
				//,'smap1.readOnly' : 'si'
			}
			,scripts  : true
			,autoLoad : true
		}
	}).show();
}


function _4_onClockClick(grid,rowIndex){
	var record=grid.getStore().getAt(rowIndex);
	debug(record);
	var window=Ext.create('Ext.window.Window',{
		title        : 'Detalles del tr&aacute;mite '+record.get('ntramite')
		,modal       : true
		,buttonAlign : 'center'
		,width       : 700
		,height      : 400
		,items       :[
			Ext.create('Ext.grid.Panel',{
				height      : 190
				,autoScroll : true
				,store      : new Ext.data.Store({
					model     : 'DetalleMC'
					,autoLoad : true
					,proxy    :{
						type         : 'ajax'
						,url         : mesConUrlDetMC
						,extraParams :{
							'smap1.pv_ntramite_i' : record.get('ntramite')
						}
						,reader      :{
							type  : 'json'
							,root : 'slist1'
						}
					}
				})
				,columns : [
					{	header     : 'Tr&aacute;mite',		dataIndex : 'NTRAMITE',				width     : 60			},
					{	header     : 'No.',		            dataIndex : 'NMORDINA'	,			width     : 40			},
					{	header     : 'Fecha de inicio',		dataIndex : 'FECHAINI',				width     : 130,			xtype     : 'datecolumn',		format    : 'd M Y H:i'		},
					{	header     : 'Usuario inicio',		dataIndex : 'usuario_ini',			width     : 150			},
					{	header     : 'Fecha de fin',		dataIndex : 'FECHAFIN',				width     : 90,			xtype     : 'datecolumn',		format    : 'd M Y H:i'		},
					{	header     : 'Usuario fin',			dataIndex : 'usuario_fin',			width     : 150			},
					{	width      : 30	,					dataIndex    : 'FECHAFIN',			menuDisabled : true,
						renderer     : function(value){
							debug(value);
							if(value&&value!=null){
								value='';
							}else{
								value='<img src="${ctx}/resources/fam3icons/icons/accept.png" style="cursor:pointer;" data-qtip="Finalizar" />';
							}
							return value;
						}
					}
				]
				,listeners :{
					cellclick : function(grid, td,
					cellIndex, record, tr,
					rowIndex, e, eOpts){
						debug(record);
						if(cellIndex<6){
							Ext.getCmp('inputReadDetalleHtmlVisor').setValue((_4_smap1.cdsisrol!='EJECUTIVOCUENTA'||record.raw.SWAGENTE=='S')?record.get('COMMENTS'):'');
						}
						else if(cellIndex==6&&$(td).find('img').length>0){
							debug('finalizar');
							Ext.create('Ext.window.Window',{
								title        : 'Finalizar detalle'
								,width       : 600
								,height      : 400
								,buttonAlign : 'center'
								,modal       : true
								,closable    : false
								,autoScroll  : true
								,items       :[
									Ext.create('Ext.form.HtmlEditor', {
										id      : 'inputHtmlEditorFinalizarDetalleMesCon'
										,width  : 570
										,height : 300
										,value  : record.get('COMMENTS')
									})
								]
								,buttons     :[{
									text     : 'Guardar'
									,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
									,handler : function(){
										var win=this.up().up();
										win.setLoading(true);
										Ext.Ajax.request({
											url      : mesConUrlFinDetalleMC
											,params  :{
												'smap1.pv_ntramite_i'  : record.get('NTRAMITE')
												,'smap1.pv_nmordina_i' : record.get('NMORDINA')
												,'smap1.pv_comments_i' : Ext.getCmp('inputHtmlEditorFinalizarDetalleMesCon').getValue()
											}
											,success : function (response){
												var json=Ext.decode(response.responseText);
												if(json.success==true){
													win.destroy();
													window.destroy();
													Ext.Msg.show({
														title:'Detalle actualizado',
														msg: 'Se finaliz&oacute; el detalle',
														buttons: Ext.Msg.OK
													});
												}
												else{
													win.setLoading(false);
													Ext.Msg.show({
														title:'Error',
														msg: 'Error al finalizar detalle',
														buttons: Ext.Msg.OK,
														icon: Ext.Msg.ERROR
													});
												}
											}
											,failure : function(){
												win.setLoading(false);
												Ext.Msg.show({
													title:'Error',
													msg: 'Error de comunicaci&oacute;n',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.ERROR
												});
											}
										});
									}
								},{
									text     : 'Cancelar'
									,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
									,handler : function(){
										this.up().up().destroy();
									}
								}]
							}).show();
						}
					}
				}
			})
			,Ext.create('Ext.form.HtmlEditor',{
				id        : 'inputReadDetalleHtmlVisor'
				,width    : 690
				,height   : 200
				,readOnly : true
			})
		]
	}).show();
	window.center();
	Ext.getCmp('inputReadDetalleHtmlVisor').getToolbar().hide();
}

//function rechazoAutorizacionServicio(grid,rowIndex,colIndex){
function rechazoAutorizacionServicio(grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
		mensajeWarning('Este tr&aacute;mite ya se encuentra rechazado!');
		return;
	}
	if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
		mensajeWarning('Este tr&aacute;mite ya se encuentra confirmado!');
		return;
	}
	else{
		//Obtengo el valor del ID para obtener el valor de la descripcion
		Ext.Ajax.request({
			url     : _URL_CONSULTA_CLAUSU
			,params : {
				'params.cdclausu' : null,
				'params.dsclausu' : 'CARTA RECHAZO AUT SERV'
			}
			,success : function (response){
				var json=Ext.decode(response.responseText);
				var claveClausula = json.listaGenerica[0].key;
				Ext.Ajax.request({
					url     : _URL_CONSULTA_CLAUSU_DETALLE
					,params : {
						'params.cdclausu'  : claveClausula
					}
					,success : function (response){
						var json=Ext.decode(response.responseText);
						txtContenido =json.msgResult;
						windowLoader = Ext.create('Ext.window.Window',{
							modal       : true,
							buttonAlign : 'center',
							width       : 663,
							height      : 430,
							autoScroll  : true,
							items       : [
								Ext.create('Ext.form.Panel', {
									title: 'Rechazar autorizaci&oacute;n de servicio',
									width: 650,
									url: _URL_ActualizaStatusTramite,
									bodyPadding: 5,
									items: [
										Ext.create('Ext.form.field.TextArea', {
											id        : 'inputTextareaCommentsToRechazo'
											,width  : 570
											,height : 200
											,value  : txtContenido
										}),
										Ext.create('Ext.form.field.TextArea', {
											id        : 'inputTextareaComments'
											,width  : 570
											,height : 100
										})
										,{
							                xtype       : 'radiogroup'
						                    ,fieldLabel : 'Mostrar al agente'
						                    ,columns    : 2
						                    ,width      : 250
						                    ,style      : 'margin:5px;'
						                    ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
						                    ,items      :
						                    [
						                        {
						                            boxLabel    : 'Si'
						                            ,itemId     : 'SWAGENTE2'
						                            ,name       : 'SWAGENTE2'
						                            ,inputValue : 'S'
						                            ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
						                        }
						                        ,{
						                            boxLabel    : 'No'
						                            ,name       : 'SWAGENTE2'
						                            ,inputValue : 'N'
	                                                ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
						                        }
						                    ]
						                }
										],
									buttonAlign:'center',
									buttons: [{
										text: 'Rechazar',
										icon    : '${ctx}/resources/fam3icons/icons/accept.png',
										buttonAlign : 'center',
										handler: function() {
											if (this.up().up().form.isValid()) {
												this.up().up().form.submit({
													waitMsg:'Procesando...',
													params: {
														'smap1.ntramite' : record.get('ntramite'), 
														'smap1.status'   : _STATUS_TRAMITE_RECHAZADO,
														'smap1.comments' : Ext.getCmp('inputTextareaComments').getValue(),
														'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
													},
													failure: function(form, action) {
														mensajeError('No se pudo rechazar.');
													},
													success: function(form, action) {
														Ext.Ajax.request({
															url     : compleUrlGuardarCartoRechazo
															,method:'GET'
															,params :{
																'map1.ntramite'  : record.raw.ntramite
																,'map1.comments' : Ext.getCmp('inputTextareaCommentsToRechazo').getValue()
																,'map1.cdsisrol' : 'MEDICO'
																,'map1.cdunieco' : record.raw.cdunieco
																,'map1.cdramo'   : record.raw.cdramo
																,'map1.estado'   : record.raw.estado
																,'map1.nmpoliza' : record.raw.nmpoliza
																,'map1.nmsuplem' : record.raw.nmsuplem
															}
															,success : function(response){
																// YA NO REALIZA NADA PORQUE YA SE GENERO EL ARCHIVO
															}
															,failure : function(){
																Ext.Msg.show({
																	title:'Error',
																	msg: 'Error de comunicaci&oacute;n',
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																});
															}
														});
														Ext.Ajax.request({
															url     : _URL_ActualizaStatusMAUTSERV
															,params:{
																'params.nmautser' :  record.get('parametros.pv_otvalor01'),
																'params.status'   : _STATUS_TRAMITE_RECHAZADO
															}
															,success : function (response){
																mensajeCorrecto('Aviso','Se ha rechazado con &eacute;xito.');
																loadMcdinStore();
																windowLoader.close();
															},
															failure : function (){
																me.up().up().setLoading(false);
																Ext.Msg.show({
																	title:'Error',
																	msg: 'Error de comunicaci&oacute;n',
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																});
															}
														});
													}
												});
											} else {
												Ext.Msg.show({
													title: 'Aviso',
													msg: 'Complete la informaci&oacute;n requerida',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.WARNING
												});
											}
										}
									},{
										text: 'Cancelar',
										icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
										buttonAlign : 'center',
										handler: function() {
											windowLoader.close();
										}
									},
									{
										text: 'Vista Previa',
										icon    : '${ctx}/resources/fam3icons/icons/application_form_magnify.png',
										buttonAlign : 'center',
										handler: function() {
											Ext.Ajax.request({
												url     : compleUrlGuardarCartoRechazo
												,method:'GET'
												,params :{
													'map1.ntramite'  : record.raw.ntramite
													,'map1.comments' : Ext.getCmp('inputTextareaCommentsToRechazo').getValue()
													,'map1.cdsisrol' : 'MEDICO'
													,'map1.cdunieco' : record.raw.cdunieco
													,'map1.cdramo'   : record.raw.cdramo
													,'map1.estado'   : record.raw.estado
													,'map1.nmpoliza' : record.raw.nmpoliza
													,'map1.nmsuplem' : record.raw.nmsuplem
												}
												,success : function(response){
													// YA NO REALIZA NADA PORQUE YA SE GENERO EL ARCHIVO
													debug("Valor de la respuesta",response);
													var numRand=Math.floor((Math.random()*100000)+1);
										        	debug('numRand a: ',numRand);
										        	var windowVerDocu=Ext.create('Ext.window.Window',
										        	{
										        		title          : 'Carta de Rechazo'
										        		,width         : 700
										        		,height        : 500
										        		,collapsible   : true
										        		,titleCollapse : true
										        		,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
										        		                 +'src="'+panDocUrlViewDoc+'?subfolder=' + record.get('ntramite') + '&filename=' + '<s:text name="pdf.emision.rechazo.nombre"/>' +'">'
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
												,failure : function(){
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
								]
							})  
						]
						}).show();
						centrarVentana(windowLoader);
					},
					failure : function (){
						Ext.Msg.show({
							title:'Error',
							msg: 'Error de comunicaci&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						});
					}
				});
			},
			failure : function (){
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

function _4_onComplementariosClick(grid,rowIndex){
	var record = grid.getStore().getAt(rowIndex);
	if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
		mensajeWarning('No se puede complementar el tr&aacute;mite ya se encuentra rechazado');
		return;
	}
	if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
		mensajeWarning('No se puede complementar el tr&aacute;mite ya se encuentra confirmado');
		return;
	}
	else{
		Ext.create('Ext.form.Panel').submit({
			url             : _UrlAutorizacionServicio
			,params   :{
				'params.nmAutSer': record.get('parametros.pv_otvalor01'),
				'params.ntramite' : record.get('ntramite'),
				'params.cdrol':null
			}
			,standardSubmit : true
		});
	}
}

function _4_generarAutorizacion(grid,rowIndex){
	var record = grid.getStore().getAt(rowIndex);
	debug('_4_generarAutorizacion:',record.raw);
}

// TURNAR AL COORDINADOR MULTIREGIONAL
function turnarCoordinaMedMultiregional(grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	// VERIFICAMOS QUE NO SE ENCUENTRA CONFIRMADA Y CANCELADA 
	if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
		mensajeWarning('Este tr&aacute;mite ya se encuentra rechazado!');
		return;
	}
	if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
		mensajeWarning('Este tr&aacute;mite ya se encuentra confirmado!');
		return;
	}else{
		Ext.Ajax.request({
			url     : _UrlValidaAutoProceso
			,params:{
				'params.nmAutSer': record.get('parametros.pv_otvalor01')
			}
			,success : function (response){
				if(Ext.decode(response.responseText).autorizarProceso =="N"){
					Ext.Msg.show({
						title:'Error',
						msg: 'No se puede turnar debido a que no esta completo la autorizaci&oacute;n',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					});
				}else{
					Ext.Ajax.request({
						url     : _URL_Existe_Documentos
						,params:{
							'params.ntramite': record.get('ntramite')
						}
						,success : function (response){
							if(Ext.decode(response.responseText).existeDocAutServicio =="N"){
								Ext.Msg.show({
									title:'Error',
									msg: 'No se puede turnar debes de registrar al menos un documento',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.ERROR
								});
							}else{
								// se coloca la validacion de que existe archivos
								var comentariosText = Ext.create('Ext.form.field.TextArea', {
									fieldLabel: 'Observaciones'
									,labelWidth: 150
									,width: 600
									,name:'smap1.comments'
									,height: 250
									,allowBlank : false
								});
								windowLoader = Ext.create('Ext.window.Window',{
									modal       : true,
									buttonAlign : 'center',
									width       : 663,
									height      : 400,
									autoScroll  : true,
									items       : [
										Ext.create('Ext.form.Panel', {
											title: 'Turnar a Coordinador M&eacute;dico Multiregional',
											width: 650,
											url: _URL_TURNAR_AUTSERVICIO,
											bodyPadding: 5,
											items: [comentariosText],
											buttonAlign:'center',
											buttons: [{
												text: 'Turnar',
												icon    : '${ctx}/resources/fam3icons/icons/accept.png',
												buttonAlign : 'center',
												handler: function() {
													if (this.up().up().form.isValid()) {
														this.up().up().form.submit({
														waitMsg:'Procesando...',
														params: {
															'smap1.ntramite' : record.get('ntramite'), 
															'smap1.status'   : _STATUS_PENDIENTE,
															'smap1.rol_destino'     : 'COORDMEDMULTI',
															'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
														},
														failure: function(form, action) {
															mensajeError('No se pudo turnar.');
														},
														success: function(form, action) {
															Ext.Ajax.request({
																url      : _URL_NOMBRE_TURNADO
																,params  :{
																	'params.ntramite': record.get('ntramite'),
																	'params.rolDestino': 'COORDMEDMULTI'
																}
																,success : function (response) {
																	var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																	mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
																	loadMcdinStore();
																	windowLoader.close();
																}
																,failure : function(){
																	win.setLoading(false);
																	Ext.Msg.show({
																		title:'Error',
																		msg: 'Error de comunicaci&oacute;n',
																		buttons: Ext.Msg.OK,
																		icon: Ext.Msg.ERROR
																	});
																}
															});
														}
													});
													} else {
														Ext.Msg.show({
															title: 'Aviso',
															msg: 'Complete la informaci&oacute;n requerida',
															buttons: Ext.Msg.OK,
															icon: Ext.Msg.WARNING
													   });
													}
												}
											},{
												text: 'Cancelar',
												icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
												buttonAlign : 'center',
												handler: function() {
													windowLoader.close();
												}
											}
										]
										})  
									]
								}).show();
								centrarVentana(windowLoader);
							}
						},
						failure : function (){
							me.up().up().setLoading(false);
							Ext.Msg.show({
								title:'Error',
								msg: 'Error de comunicaci&oacute;n',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR
							});
						}
					});
				}
			},
			failure : function (){
				me.up().up().setLoading(false);
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

//TURNAR AL GERENTE MULTIREGIONAL
function activarTramiteAutorizacionServ (grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	debug("VALOR DEL RECORD -->>>",record);
	debug("VALOR DEL record.get('status')");
	debug(record.get('status'));
	if(record.get('status') == _STATUS_EN_CAPTURA || record.get('status') == _STATUS_EN_CAPTURA_CMM || record.get('status') ==_STATUS_PENDIENTE){
		mensajeWarning('El tr&aacute;mite se encuentra activo');
		return;
	}else{
		msgWindow = Ext.Msg.show({
		title: 'Aviso',
		msg: '&iquest; Deseas reactivar el tr&aacute;mite '+record.get('ntramite')+'?',
		buttons: Ext.Msg.YESNO,
		icon: Ext.Msg.QUESTION,
		fn: function(buttonId, text, opt){
			if(buttonId == 'yes'){
				var comentarioReactivacion = Ext.create('Ext.form.field.TextArea', {
					fieldLabel: 'Observaciones de reactivaci&oacute;n'
					,labelWidth: 150
					,width: 600
					,name:'smap1.comments'
					,height: 250
					,allowBlank : false
				});
				windowLoader = Ext.create('Ext.window.Window',{
					modal       : true,
					buttonAlign : 'center',
					width       : 663,
					height      : 430,
					autoScroll  : true,
					items       : [
						Ext.create('Ext.form.Panel', {
							title: 'Reactivar Tr&aacute;mite',
							width: 650,
							url: _URL_ActualizaStatusTramite,
							bodyPadding: 5,
							items: [comentarioReactivacion,
							        {
						                xtype       : 'radiogroup'
						                ,fieldLabel : 'Mostrar al agente'
						                ,columns    : 2
						                ,width      : 250
						                ,style      : 'margin:5px;'
						                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
						                ,items      :
						                [
						                    {
						                        boxLabel    : 'Si'
						                        ,itemId     : 'SWAGENTE2'
						                        ,name       : 'SWAGENTE2'
						                        ,inputValue : 'S'
						                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
						                    }
						                    ,{
						                        boxLabel    : 'No'
						                        ,name       : 'SWAGENTE2'
						                        ,inputValue : 'N'
                                                ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
						                    }
						                ]
						            }],
							buttonAlign:'center',
							buttons: [{
								text: 'Reactivar',
								icon    : '${ctx}/resources/fam3icons/icons/accept.png',
								buttonAlign : 'center',
								handler: function() {
									if (this.up().up().form.isValid()) {
										this.up().up().form.submit({
											waitMsg:'Procesando...',
											params: {
												'smap1.ntramite' : record.get('ntramite'), 
												'smap1.status'   : _STATUS_EN_CAPTURA_CMM,
												'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
											},
											failure: function(form, action) {
												mensajeError('No se pudo reactivar dicho tr&aacute;mite.');
											},
											success: function(form, action) {
												//mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito.');
												loadMcdinStore();
												windowLoader.close();
												
											}
										});
									} else {
										Ext.Msg.show({
											title: 'Aviso',
											msg: 'Complete la informaci&oacute;n requerida',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING
										});
									}
								}
							},{
								text: 'Cancelar',
								icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
								buttonAlign : 'center',
								handler: function() {
									windowLoader.close();
								}
							}
							]
						})
					]
				}).show();
				centrarVentana(windowLoader);
			}
		}
		});
		centrarVentana(msgWindow);
	}
}


function turnarGerenteMedMultiregional(grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
		mensajeWarning('No se puede turnar el tr&aacute;mite ya se encuentra rechazado');
		return;
	}
	if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
		mensajeWarning('No se puede turnar el tr&aacute;mite ya se encuentra confirmado');
		return;
	}else{
		Ext.Ajax.request({
			url     : _URL_Existe_Documentos
			,params:{
				'params.ntramite': record.get('ntramite')
			}
			,success : function (response){
				if(Ext.decode(response.responseText).existeDocAutServicio =="N"){
					Ext.Msg.show({
						title:'Error',
						msg: 'No se puede turnar debes de registrar al menos un documento',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					});
				}else{
					msgWindow = Ext.Msg.show({
					title: 'Aviso',
					msg: 'El tr&aacute;mite ser&aacute; turnado al Gerente M&eacute;dico para su Vo.Bo. &iquest; esta seguro ?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(buttonId, text, opt){
						if(buttonId == 'yes'){
								Ext.Ajax.request({
									url     : _UrlValidaAutoProceso
									,params:{
										'params.nmAutSer': record.get('parametros.pv_otvalor01')
									}
									,success : function (response){
										var comentariosText = Ext.create('Ext.form.field.TextArea', {
											fieldLabel: 'Observaciones'
											,labelWidth: 150
											,width: 600
											,name:'smap1.comments'
											,height: 250
											,allowBlank : false
										});
										windowLoader = Ext.create('Ext.window.Window',{
											modal       : true,
											buttonAlign : 'center',
											width       : 663,
											height      : 400,
											autoScroll  : true,
											items       : [
												Ext.create('Ext.form.Panel', {
													title: 'Turnar al gerente m&eacute;dico multiregional',
													width: 650,
													url: _URL_TURNAR_AUTSERVICIO,
													bodyPadding: 5,
													items: [comentariosText],
													buttonAlign:'center',
													buttons: [{
														text: 'Turnar',
														icon    : '${ctx}/resources/fam3icons/icons/accept.png',
														buttonAlign : 'center',
														handler: function() {
															if (this.up().up().form.isValid()) {
																	this.up().up().form.submit({
																		waitMsg:'Procesando...',
																		params: {
																			'smap1.ntramite' : record.get('ntramite'), 
																			'smap1.status'   : _STATUS_EN_ESPERA_DE_AUTORIZACION,
																			'smap1.rol_destino'     : 'GERMEDMULTI',
																			'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
																		},
																		failure: function(form, action) {
																			mensajeError('No se pudo turnar.');
																		},
																		success: function(form, action) {
																			Ext.Ajax.request({
																				url      : _URL_NOMBRE_TURNADO
																				,params  :{
																					'params.ntramite': record.get('ntramite'),
																					'params.rolDestino': 'GERMEDMULTI'
																				}
																				,success : function (response) {
																					var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																					mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
																					loadMcdinStore();
																					windowLoader.close();
																				}
																				,failure : function(){
																					win.setLoading(false);
																					Ext.Msg.show({
																						title:'Error',
																						msg: 'Error de comunicaci&oacute;n',
																						buttons: Ext.Msg.OK,
																						icon: Ext.Msg.ERROR
																					});
																				}
																			});
																		}
																	});
																} else {
																	Ext.Msg.show({
																		title: 'Aviso',
																		msg: 'Complete la informaci&oacute;n requerida',
																		buttons: Ext.Msg.OK,
																		icon: Ext.Msg.WARNING
																	});
																}
															}
														},{
															text: 'Cancelar',
															icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
															buttonAlign : 'center',
															handler: function() {
																windowLoader.close();
															}
														}
													]
												})
											]
										}).show();
										centrarVentana(windowLoader);
									},
									failure : function (){
										me.up().up().setLoading(false);
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
					});
					centrarVentana(msgWindow);
				}
			},
			failure : function (){
				me.up().up().setLoading(false);
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

Ext.onReady(function(){
	Ext.define('DetalleMC',{
		extend:'Ext.data.Model',
		fields:[
			"NTRAMITE"
			,"NMORDINA"
			,"CDTIPTRA"
			,"CDCLAUSU"
			,{name:"FECHAINI",type:'date',dateFormat:'d/m/Y H:i'}
			,{name:"FECHAFIN",type:'date',dateFormat:'d/m/Y H:i'}
			,"COMMENTS"
			,"CDUSUARI_INI"
			,"CDUSUARI_FIN"
			,"usuario_ini"
			,"usuario_fin"
		]
	});
});

<s:if test="false">
</script>
</s:if>