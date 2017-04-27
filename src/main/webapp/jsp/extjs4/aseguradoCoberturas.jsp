<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
--%>
<script>
	///////////////////////
	//////variables //////
	/*///////////////////*/
	var storeCoberturasp3;
	var storeCoberturasBorrarp3;
	var panelCoberturasp3;
	<%--
	var urlRegresar = '<s:url namespace="/" action="editarAsegurados" />';
	--%>
	var urlCargarCoberturasp3 = '<s:url namespace="/" action="cargarPantallaCoberturas" />';
	var inputCduniecop3 = '<s:property value="smap1.pv_cdunieco" />';
	var inputCdramop3 = '<s:property value="smap1.pv_cdramo" />';
	var inputEstadop3 = '<s:property value="smap1.pv_estado" />';
	var inputNmpolizap3 = '<s:property value="smap1.pv_nmpoliza" />';
	var inputNmsituacp3 = '<s:property value="smap1.pv_nmsituac" />';
	var inputCdpersonap3 = '<s:property value="smap1.pv_cdperson" />';
	var inputCdtipsitp3 = '<s:property value="smap1.pv_cdtipsit" />';
	var urlGuardarCoberturasp3 = '<s:url namespace="/" action="guardarCoberturasUsuario" />';
	var panelAdicionalesp3;
	var urlTatrip3 = '<s:url namespace="/" action="obtenerCamposTatrigar" />';
	var urlLoadTatrip3 = '<s:url namespace="/" action="obtenerValoresTatrigar" />';
	var urlSaveTatrip3 = '<s:url namespace="/" action="guardarValoresTatrigar" />';
	var contextop3 = '${ctx}';
	/*///////////////////*/
	//////variables //////
	///////////////////////
	Ext.onReady(function() {

				/////////////////////
				////// Modelos //////
				/*/////////////////*/
				Ext.define('Modelo1p3', {
					extend : 'Ext.data.Model',
					fields : [ {
						name : 'GARANTIA'
					}, {
						name : 'NOMBRE_GARANTIA'
					}, {
						name : 'SWOBLIGA'
					}, {
						name : 'SUMA_ASEGURADA'
					}, {
						name : 'CDCAPITA'
					}, {
						name : 'status'
					}, {
						name : 'cdtipbca'
					}, {
						name : 'ptvalbas'
					}, {
						name : 'swmanual'
					}, {
						name : 'swreas'
					}, {
						name : 'cdagrupa'
					}, {
						name : 'ptreduci'
					}, {
						name : 'fereduci',
						type : 'date',
						dateFormat : 'd/m/Y'
					}, {
						name : 'swrevalo'
					} ]
				});

				Ext.define('ModeloAdicionalesp3', {
					extend : 'Ext.data.Model'
				});
				/*/////////////////*/
				////// Modelos //////
				/////////////////////
				////////////////////
				////// Stores //////
				/*////////////////*/
				storeCoberturasp3 = Ext.create('Ext.data.Store', {
					storeId : 'storeCoberturasp3',
					model : 'Modelo1p3',
					proxy : {
						type : 'ajax',
						url : urlCargarCoberturasp3,
						extraParams : {
							'smap1.pv_cdunieco_i' : inputCduniecop3,
							'smap1.pv_cdramo_i' : inputCdramop3,
							'smap1.pv_estado_i' : inputEstadop3,
							'smap1.pv_nmpoliza_i' : inputNmpolizap3,
							'smap1.pv_nmsituac_i' : inputNmsituacp3
						},
						reader : {
							type : 'json',
							root : 'slist1'
						}
					},
					autoLoad : true
				});

				storeCoberturasBorrarp3 = Ext.create('Ext.data.Store', {
					storeId : 'storeCoberturasBorrarp3',
					model : 'Modelo1p3'
				});
				/*////////////////*/
				////// Stores //////
				////////////////////
				/////////////////////////
				////// Componentes //////
				/*/////////////////////*/
				panelAdicionalesp3 = Ext.create('Ext.form.Panel', {
					frame : false,
					title : 'Datos adicionales',
					collapsible : true,
					titleCollapse : true,
					bodyPadding : 5
				});
				/*/////////////////////*/
				////// Componentes //////
				/////////////////////////
				///////////////////////
				////// Contenido //////
				/*///////////////////*/
				panelCoberturasp3 = Ext.create(
								'Ext.grid.Panel',
								{
									title : 'Coberturas',
									collapsible : true,
									titleCollapse : true,
									style : 'margin:5px;',
									store : storeCoberturasp3,
									renderTo : 'pan_usu_cob_divgrid',
									frame : false,
									columns : [
											{
												header : 'Cobertura',
												dataIndex : 'NOMBRE_GARANTIA',
												flex : 2
											},
											{
												header : 'Suma asegurada',
												dataIndex : 'SUMA_ASEGURADA',
												flex : 1,
												editor : {
													xtype : 'textfield',
													allowBlank : true
												}
											},
											{
												menuDisabled : true,
												dataIndex : 'SWOBLIGA',
												width : 30,
												renderer : function(value) {
													var rvalue = '';
													if (value == 'N') {
														rvalue = '<img src="'+contexto+'/resources/fam3icons/icons/eye.png" data-qtip="Ver datos adicionales" style="cursor:pointer;">';
													}
													return rvalue;
												}
											},
											{
												menuDisabled : true,
												dataIndex : 'SWOBLIGA',
												width : 30,
												renderer : function(value) {
													var rvalue = '';
													if (value == 'N') {
														rvalue = '<img src="'+contexto+'/resources/fam3icons/icons/pencil.png" data-qtip="Editar suma asegurada" style="cursor:pointer;">';
													}
													return rvalue;
												}
											},
											{
												menuDisabled : true,
												dataIndex : 'SWOBLIGA',
												width : 30,
												renderer : function(value) {
													var rvalue = '';
													if (value == 'N') {
														rvalue = '<img src="'+contexto+'/resources/fam3icons/icons/delete.png" data-qtip="Quitar cobertura" style="cursor:pointer;">';
													}
													return rvalue;
												}
											} ],
									selType : 'rowmodel',
									plugins : [ Ext.create(
											'Ext.grid.plugin.RowEditing', {
												clicksToEdit : 1
											}) ],
									//width : 500,
									height : 300,
									buttonAlign : 'center',
									listeners : {
										beforeedit : function(grid, e, eOpts) {
											return e.colIdx == 3
													&& e.record.get('SWOBLIGA') == 'N';
										},
										cellclick : function(grid, td,
												cellIndex, record, tr,
												rowIndex, e, eOpts) {
											if (record.get('SWOBLIGA') == 'N') {
												if (cellIndex == 2)//load tatri
												{
													//console.log("click view record "+ rowIndex);
													panelAdicionalesp3.destroy();
													Ext.Ajax.request({
																url : urlTatrip3,
																params : {
																	'smap1.pv_cdramo_i' : inputCdramop3,
																	'smap1.pv_cdgarant_i' : record.get('GARANTIA'),
																	'smap1.pv_cdtipsit_i' : inputCdtipsitp3
																},
																success : function(response,opts) {
																	var json = Ext.decode(response.responseText);
																	//console.log(json);
																	if (json.success == true) {
																		////// crear modelo con campos que vienen de str1 //////
																		Ext.define(
																						'ModeloAdicionalesp3',
																						{
																							extend : 'Ext.data.Model',
																							fields : Ext.decode(json.str1)
																						});
																		////// !crear modelo con campos que vienen de str1 //////

																		////// crear formuario con campos que vienen de str2 //////
																		panelAdicionalesp3 = Ext.create(
																						'Ext.form.Panel',
																						{
																							frame : false,
																							style : 'margin:5px;',
																							model : 'ModeloAdicionalesp3',
																							title : 'Datos adicionales de '+ record.get('NOMBRE_GARANTIA'),
																							collapsible : true,
																							titleCollapse : true,
																							bodyPadding : 5,
																							maxHeight : 300,
																							buttonAlign : 'center',
																							renderTo : 'pan_usu_cob_divadicionales',
																							url : urlSaveTatrip3,
																							layout:{type:'table',columns:2},
																							items : Ext.decode(json.str2),
																							buttons : [
																									{
																										id : 'botonGuardarAdicionalesCoberturap3',
																										text : 'Guardar cambios',
																										icon : contextop3+ '/resources/fam3icons/icons/accept.png',
																										handler : function() {
																											if (panelAdicionalesp3.getForm().isValid()) {
																												panelAdicionalesp3.getForm().submit(
																																{
																																	params : {
																																		'smap1.pv_cdunieco' : inputCduniecop3,
																																		'smap1.pv_cdramo' : inputCdramop3,
																																		'smap1.pv_estado' : inputEstadop3,
																																		'smap1.pv_nmpoliza' : inputNmpolizap3,
																																		'smap1.pv_nmsituac' : inputNmsituacp3,
																																		'smap1.pv_cdgarant' : record.get('GARANTIA')
																																	},
																																	success : function(form,action) {
																																		if (action.result.success == true) {
																																			Ext.Msg.show({
																																						title : 'Datos guardados',
																																						msg : 'Los datos han sido guardados',
																																						buttons : Ext.Msg.OK
																																					});
																																			panelAdicionalesp3.destroy();
																																		} else {
																																			Ext.Msg
																																					.show({
																																						title : 'Error',
																																						msg : 'Error al guardar',
																																						buttons : Ext.Msg.OK,
																																						icon : Ext.Msg.ERROR
																																					});
																																		}
																																	},
																																	failure : function() {
																																		Ext.Msg
																																				.show({
																																					title : 'Error',
																																					msg : 'Error de comunicaci&oacute;n',
																																					buttons : Ext.Msg.OK,
																																					icon : Ext.Msg.ERROR
																																				});
																																	}
																																});
																											} else {
																												Ext.Msg
																														.show({
																															title : 'Datos incompletos',
																															msg : 'Favor de introducir todos los datos requeridos',
																															buttons : Ext.Msg.OK,
																															icon : Ext.Msg.WARNING
																														});
																											}
																										}
																									},
																									{
																										text : 'Cancelar',
																										icon : contextop3+ '/resources/fam3icons/icons/cancel.png',
																										handler : function() {
																											//window.parent.scrollTo(0,0);
																											panelAdicionalesp3.destroy();
																										}
																									} ]
																						});
																		//window.parent.scrollTo(0,600);
																		////// !crear formuario con campos que vienen de str2 //////

																		////// cargar formulario con modelo creado //////
																		Ext
																				.define(
																						'LoaderModeloAdicionalesp3',
																						{
																							extend : 'ModeloAdicionalesp3',
																							proxy : {
																								type : 'ajax',
																								url : urlLoadTatrip3,
																								extraParams : {
																									'smap1.pv_cdunieco_i' : inputCduniecop3,
																									'smap1.pv_cdramo_i' : inputCdramop3,
																									'smap1.pv_estado_i' : inputEstadop3,
																									'smap1.pv_nmpoliza_i' : inputNmpolizap3,
																									'smap1.pv_nmsituac_i' : inputNmsituacp3,
																									'smap1.pv_cdgarant_i' : record.get('GARANTIA')
																								},
																								reader : {
																									type : 'json'
																								}
																							}
																						});
																		var loaderAdicionalesp3 = Ext.ModelManager.getModel('LoaderModeloAdicionalesp3');
																		loaderAdicionalesp3.load(
																						123,
																						{
																							success : function(resp) {
																								panelAdicionalesp3.getForm().loadRecord(resp);
																							},
																							failure : function() {
																								//window.parent.scrollTo(0,100);
																								panelAdicionalesp3.destroy();
																								Ext.Msg
																										.show({
																											title : 'Error',
																											msg : 'Error de comunicaci&oacute;n',
																											buttons : Ext.Msg.OK,
																											icon : Ext.Msg.ERROR
																										});
																							}
																						});
																		////// !cargar formulario con modelo creado //////
																	} else {
																		Ext.Msg
																				.show({
																					title : 'Error',
																					msg : 'Error al obtener los datos adicionales',
																					buttons : Ext.Msg.OK,
																					icon : Ext.Msg.ERROR
																				});
																	}
																},
																failure : function(
																		response,
																		opts) {
																	//panelCoberturas.setLoading(true);
																	Ext.Msg
																			.show({
																				title : 'Error',
																				msg : 'Error de comunicaci&oacute;n',
																				buttons : Ext.Msg.OK,
																				icon : Ext.Msg.ERROR
																			});
																}
															});
												} else if (cellIndex == 4)//delete
												{
													storeCoberturasBorrarp3.add(grid.getStore().getAt(rowIndex));
													grid.getStore().removeAt(rowIndex);
												}
											}
										}
									},
									buttons : [
									        <%--
											{
												text : 'Regresar',
												icon : contextop3+ '/resources/extjs4/resources/my-custom-theme/images/toolbar/scroll-left.png',
												handler : function() {
													Ext
															.create(
																	'Ext.form.Panel')
															.submit(
																	{
																		url : urlRegresar,
																		standardSubmit : true,
																		params : {
																			'map1.cdunieco' : inputCdunieco,
																			'map1.cdramo' : inputCdramo,
																			'map1.estado' : inputEstado,
																			'map1.nmpoliza' : inputNmpoliza
																		}
																	});
												}
											},
											--%>
											{
												text : 'Guardar cambios',
												icon : contextop3+ '/resources/fam3icons/icons/accept.png',
												handler : function() {
													var jsonList = [];
													storeCoberturasp3.each(function(record,index) {
																jsonList.push({
																			GARANTIA : record
																					.get('GARANTIA'),
																			NOMBRE_GARANTIA : record
																					.get('NOMBRE_GARANTIA'),
																			SWOBLIGA : record
																					.get('SWOBLIGA'),
																			SUMA_ASEGURADA : record
																					.get('SUMA_ASEGURADA'),
																			CDCAPITA : record
																					.get('CDCAPITA'),
																			status : record
																					.get('status'),
																			cdtipbca : record
																					.get('cdtipbca'),
																			ptvalbas : record
																					.get('ptvalbas'),
																			swmanual : record
																					.get('swmanual'),
																			swreas : record
																					.get('swreas'),
																			cdagrupa : record
																					.get('cdagrupa'),
																			ptreduci : record
																					.get('ptreduci'),
																			fereduci : typeof record
																					.get('fereduci') == 'string' ? record
																					.get('fereduci')
																					: Ext.Date
																							.format(
																									record
																											.get('fereduci'),
																									'd/m/Y'),
																			swrevalo : record
																					.get('swrevalo')
																		});
															});
													var jsonListBorrar = [];
													storeCoberturasBorrarp3.each(function(record,index) {
																jsonListBorrar.push({
																			GARANTIA : record
																					.get('GARANTIA'),
																			NOMBRE_GARANTIA : record
																					.get('NOMBRE_GARANTIA'),
																			SWOBLIGA : record
																					.get('SWOBLIGA'),
																			SUMA_ASEGURADA : record
																					.get('SUMA_ASEGURADA'),
																			CDCAPITA : record
																					.get('CDCAPITA'),
																			status : record
																					.get('status'),
																			cdtipbca : record
																					.get('cdtipbca'),
																			ptvalbas : record
																					.get('ptvalbas'),
																			swmanual : record
																					.get('swmanual'),
																			swreas : record
																					.get('swreas'),
																			cdagrupa : record
																					.get('cdagrupa'),
																			ptreduci : record
																					.get('ptreduci'),
																			fereduci : typeof record
																					.get('fereduci') == 'string' ? record
																					.get('fereduci')
																					: Ext.Date
																							.format(
																									record
																											.get('fereduci'),
																									'd/m/Y'),
																			swrevalo : record
																					.get('swrevalo')
																		});
															});
													var post = {};
													post['slist1'] = jsonList;
													post['slist2'] = jsonListBorrar;
													var smap1 = {
														pv_cdunieco_i : inputCduniecop3,
														pv_cdramo_i : inputCdramop3,
														pv_estado_i : inputEstadop3,
														pv_nmpoliza_i : inputNmpolizap3,
														pv_nmsituac_i : inputNmsituacp3
													};
													post['smap1'] = smap1;
													panelCoberturasp3.setLoading(true);
													//console.log(Ext.encode(post));
													Ext.Ajax
															.request({
																url : urlGuardarCoberturasp3,
																jsonData : Ext
																		.encode(post),
																success : function(
																		response,
																		opts) {
																	panelCoberturasp3.setLoading(false);
																	var json = Ext
																			.decode(response.responseText);
																	if (json.success == true) {
																		centrarVentanaInterna(Ext.Msg
																				.show({
																					title : 'Datos guardados',
																					msg : 'Se han guardado las coberturas',
																					buttons : Ext.Msg.OK
																				}));
																		expande(2);
																	} else {
																		Ext.Msg
																				.show({
																					title : 'Error',
																					msg : 'Error al guardar las coberturas',
																					buttons : Ext.Msg.OK,
																					icon : Ext.Msg.ERROR
																				});
																	}
																},
																failure : function(
																		response,
																		opts) {
																	panelCoberturasp3.setLoading(true);
																	Ext.Msg
																			.show({
																				title : 'Error',
																				msg : 'Error de comunicaci&oacute;n',
																				buttons : Ext.Msg.OK,
																				icon : Ext.Msg.ERROR
																			});
																}
															});
												}
											}
											,{
												text:'Cancelar',
												icon : contextop3+ '/resources/fam3icons/icons/cancel.png',
												handler:function(){
													expande(2);
												}
											}
											]
								});
				/*///////////////////*/
				////// Contenido //////
				///////////////////////
			});
</script>
<%--
</head>
<body style="margin: 0; padding: 0;">
--%>
<div id="pan_usu_cob_divgrid" style="height:300px;"></div>
<div id="pan_usu_cob_divadicionales" style="height:300px;"></div>
<%--
	<table width="100%" height="600" border="0">
		<tr height="300">
			<td align="center" valign="top">
				<table align="center" style="border: 0px solid blue;">
					<tr>
						<td></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" valign="top">
				<table align="center" style="border: 0px solid yellow;">
					<tr>
						<td></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	--%>
	<%--
</body>
</html>
--%>