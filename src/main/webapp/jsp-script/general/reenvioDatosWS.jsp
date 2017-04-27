<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reenvio de datos de WS fallidos</title>
<script>

var _CONTEXT = '${ctx}';
extjs_custom_override_mayusculas = false;

var recargaGridPeticiones;

var _UrlPeticionesFallidas = '<s:url namespace="/general"    action="obtienePeticionesWSFallidas" />';
var _UrlPayloadPeticion    = '<s:url namespace="/general"    action="obtienePayloadPeticionWS" />';
var _UrlReenviarPeticion   = '<s:url namespace="/general"    action="reenviaPeticionWS" />';
var _UrlEliminarPeticion   = '<s:url namespace="/general"    action="eliminaPeticionWS" />';

var _MSG_SIN_DATOS          = 'No hay datos';
var _MSG_BUSQUEDA_SIN_DATOS = 'No hay datos para la b\u00FAsqueda actual.';

var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CAT_SUCURSALES  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
var _CAT_PRODUCTOS  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS"/>';
var _CAT_TIPOS_ERROR_WS  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TERRORWS"/>';


Ext.onReady(function()
{
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('gridPeticionesModel',
	{
		extend : 'Ext.data.Model'
		,fields :
		['CDUNIECO','CDRAMO','NMPOLIZA','NMSUPLEM','DESCRIPL',
		 'MENSAJE','USUARIO','FECHAHR','NTRAMITE','CDERRWS','SEQIDWS']
	});
	
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	var peticionesStore = Ext.create('Ext.data.Store',
    {
		pageSize : 30,
        autoLoad : true
        ,model   : 'gridPeticionesModel'
        ,proxy   :
        {
            type         : 'memory'
            ,enablePaging : true
            ,reader      : 'json'
            ,data        : []
        }
    });
	
	/*////////////////*/
	////// stores //////
	////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	var gridPeticiones = Ext.create('Ext.grid.Panel',
	    {
    	title : 'Peticiones de WS Fallidas'
    	,height : 400
    	,selModel: {
	 		selType: 'checkboxmodel',
	 		allowDeselect: true,
	 		mode: 'MULTI'
	 	}
    	,store : peticionesStore
    	,columns :
    	[ { header     : 'CodigoSeq', dataIndex : 'SEQIDWS', hidden: true},
    	  { header     : 'Producto' , dataIndex : 'CDRAMO', flex: 1},
    	  { header     : 'Sucursal' , dataIndex : 'CDUNIECO', flex: 1},
    	  { header     : 'P&oacute;liza' , dataIndex : 'NMPOLIZA', flex: 2},
    	  { header     : 'Tr&aacute;mite', dataIndex : 'NTRAMITE', flex: 2},
    	  { header     : 'Suplemento'    , dataIndex : 'NMSUPLEM', hidden: true},
    	  { header     : 'Tipo Error' , dataIndex : 'DESCRIPL', flex: 3},
    	  { header     : 'Mensaje'  , dataIndex : 'MENSAJE', flex: 6},
    	  { header     : 'C&oacute;digo Error WS' , dataIndex : 'CDERRWS', flex: 1},
    	  { header     : 'Fecha'   , dataIndex : 'FECHAHR', flex: 2},
          { header     : 'Usuario' , dataIndex : 'USUARIO' , flex: 2}
		]
    	,bbar :
        {
            displayInfo : true,
            store       : peticionesStore,
            xtype       : 'pagingtoolbar'
        },
        tbar: [{
            icon    : '${ctx}/resources/fam3icons/icons/arrow_right.png',
            text    : 'Reenviar Petici&oacute;n WS',
            handler : function(btn, e){
            	var model =  gridPeticiones.getSelectionModel();
            	if(model.hasSelection() && model.getSelection().length == 1){
            		Ext.Msg.show({
                        title: 'Aviso',
                        msg: '&iquest;Esta seguro que desea reenviar esta Petici&oacute;n WS?',
                        buttons: Ext.Msg.YESNO,
                        fn: function(buttonId, text, opt) {
                        	if(buttonId == 'yes') {
                        		
                        		var seqIdWS = model.getSelection()[0].get('SEQIDWS');
                        		
                        		var saveList = [];
                        		saveList.push(model.getSelection()[0].data);
                        		gridPeticiones.setLoading(true);
    		            		
                        		Ext.Ajax.request({
            						url: _UrlReenviarPeticion,
            						timeout : 120000,
            						params: {
            					    		'params.pv_seqidws_i' : seqIdWS
            						},
            						success: function(response, opt) {
            							gridPeticiones.setLoading(false);
            							var jsonRes=Ext.decode(response.responseText);
            							
            							if(jsonRes.success == true){
            								//mensajeCorrecto('Aviso','Se ha reenviado correctamente la Petici&oacute;n WS, Respuesta: '+ jsonRes.mensajeRespuesta);
            								var ventanaResWS = Ext.create('Ext.window.Window',{
    											title   : 'Respuesta de la petici&oacute;n WS',
    											modal  : true,
    											layout: {type:'hbox', pack: 'center'},
    											width  : 500,
    											height : 500,
    											items  :
    											[{
    										        xtype     : 'textareafield',
    										        grow      : true,
    										        value     : jsonRes.mensajeRespuesta,
    										        readOnly  : true,
    										        width     : 450,
    										        height    : 400
    										    }],
    										    buttons: [{
    				        	            			text: 'Eliminar esta petici&oacute;n de la Bit&aacute;cora'
    				            	            		,icon:_CONTEXT+'/resources/fam3icons/icons/delete.png'
    				            	            		,handler: function() {
    				            	            			ventanaResWS.setLoading(true);
    				            	            			Ext.Ajax.request({
    				                    						url: _UrlEliminarPeticion,
    				                    						jsonData: {
    				                    							saveList : 	saveList
    				                    						},
    				                    						success: function(response, opt) {
    				                    							ventanaResWS.setLoading(false);
    				                    							var jsonRes=Ext.decode(response.responseText);

    				                    							if(jsonRes.success == true){
    				                    								mensajeCorrecto('Aviso','Se ha eliminado la Petici&oacute;n WS.');
    				                    								recargaGridPeticiones();        							
    				                    								ventanaResWS.close();
    				                           						}else {
    				                           							mensajeError('No se pudo eliminar la Petici&oacute;n WS.');
    				                           						}
    				                    						},
    				                    						failure: function(){
    				                    							ventanaResWS.setLoading(false);
    				                    							mensajeError('No se pudo eliminar la Petici&oacute;n WS.');
    				                    						}
    				                    					});
    				            	            		}
    				            	            	},{
    				            	            	    text: 'Aceptar',
    				            	            	    icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
    				            	            	    handler: function() {
    				            	            	    	ventanaResWS.close();
    				            	            	    }
    				            	            	}]
    										}).show();
//    										centrarVentanaInterna(ventanaResWS);
            								        							
                   						}else {
                   							mensajeError('No se pudo reenviar la Petici&oacute;n WS. ' + jsonRes.mensajeRespuesta);
                   						}
            						},
            						failure: function(){
            							gridPeticiones.setLoading(false);
            							mensajeError('No se pudo reenviar la Petici&oacute;n WS.');
            						}
            					});
                        	}
            			},
                        animateTarget: btn,
                        icon: Ext.Msg.QUESTION
            		});
            	
            	}else{
            		showMessage("Aviso","Debe seleccionar un solo registro para esta operaci&oacute;n", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            	
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
            text    : 'Eliminar Petici&oacute;n WS',
            scope   : this,
            handler : function (btn, e){
            	var model =  gridPeticiones.getSelectionModel();
            	if(model.hasSelection()){
            		
            		Ext.Msg.show({
    		            title: 'Aviso',
    		            msg: '&iquest;Esta seguro que desea eliminar esta(s) Petici&oacute;n(s) WS?',
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		
    		            		var saveList = [];
    		            		model.getSelection().forEach(function(record,index,arr){
    								saveList.push(record.data);
    							});
    							
    		            		Ext.Ajax.request({
            						url: _UrlEliminarPeticion,
            						jsonData: {
            							saveList : 	saveList
            						},
            						success: function(response, opt) {
            							var jsonRes=Ext.decode(response.responseText);

            							if(jsonRes.success == true){
            								mensajeCorrecto('Aviso','Se ha eliminado la(s) Petici&oacute;n(es) WS.');
            								recargaGridPeticiones();        							
                   						}else {
                   							mensajeError('No se pudo eliminar la(s) Petici&oacute;n(es) WS.');
                   						}
            						},
            						failure: function(){
            							mensajeError('No se pudo eliminar la(s) Petici&oacute;n(es) WS.');
            						}
            					});
    		            	}
            			},
    		            animateTarget: btn,
    		            icon: Ext.Msg.QUESTION
        			});
            		
            	}else{
            		showMessage("Aviso","Debe seleccionar almenos un registro", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            }
        },'-',{
            icon    : '${ctx}/resources/fam3icons/icons/zoom.png',
            text    : 'Ver Payload Xml',
            handler : function(btn, e){
            	var model =  gridPeticiones.getSelectionModel();
            	if(model.hasSelection() && model.getSelection().length == 1){
            		
            		var seqIdWS = model.getSelection()[0].get('SEQIDWS');
            		
            		gridPeticiones.setLoading(true);
            		
            		Ext.Ajax.request({
						url: _UrlPayloadPeticion,
						params: {
					    		'params.pv_seqidws_i' : seqIdWS
						},
						success: function(response, opt) {
							gridPeticiones.setLoading(false);
							var jsonRes=Ext.decode(response.responseText);
							
							if(jsonRes.success == true){

								var ventanaPayload = Ext.create('Ext.window.Window',{
									title   : 'Payload Xml de la Petici&oacute;n',
									modal  : true,
									layout: {type:'hbox', pack: 'center'},
									width  : 500,
									height : 500,
									items  :
									[{
								        xtype     : 'textareafield',
								        grow      : true,
								        value     : jsonRes.mensajeRespuesta,
								        readOnly  : true,
								        width     : 450,
								        height    : 400
								    }],
								    buttons: [{
		            	            	    text: 'Aceptar',
		            	            	    icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
		            	            	    handler: function() {
		            	            	    	ventanaPayload.close();
		            	            	    }
		            	            	}]
								}).show();
//								centrarVentanaInterna(ventanaPayload);
								        							
       						}else {
       							mensajeError('No se pudo obtener el Payload de la Petici&oacute;n WS. ' + jsonRes.mensajeRespuesta);
       						}
						},
						failure: function(){
							gridPeticiones.setLoading(false);
							mensajeError('No se pudo obtener el Payload de la Petici&oacute;n WS.');
						}
					});
            	}else{
            		showMessage("Aviso","Debe seleccionar un solo registro para esta operaci&oacute;n", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            	
            }
        }]
    });
	
	var panelPeticiones = Ext.create('Ext.form.Panel',{
		title: 'Administraci&oacute;n de Peticiones WS fallidas',
		defaults : {
			style : 'margin : 5px;'
		},
	    renderTo : 'mainDivPet',
	    buttonAlign: 'center',
	    items :
	    [{
	    	layout: {type:'hbox', pack: 'center'},
	    	border: false,
	    	bodyStyle:'padding:5px;',
	    	defaults: {
	    		style: 'margin: 5px'// para hacer que los componentes se separen 5px
	    	},
	    	buttonAlign: 'center',
	    	items: [{
	            xtype         : 'combobox',
	            labelWidth    : 50,
	            width         : 250,
	            name          : 'params.pv_cdunieco_i',
	            fieldLabel    : 'Sucursal',
	            valueField    : 'key',
	            displayField  : 'value',
	            forceSelection: true,
	            queryMode     : 'local',
	            typeAhead     : true,
	            anyMatch      : true,
	            tpl: Ext.create('Ext.XTemplate',
	                    '<tpl for=".">',
	                        '<div class="x-boundlist-item">{key} - {value}</div>',
	                    '</tpl>'
	            ),
	            listeners: {
	            	  /*change: function (combobox, newvalue) {
	            	            if(newvalue){
	            	            	newvalue = newvalue.toUpperCase();
	            	            	storeCombo = combobox.getStore();
	            	            	storeCombo.clearFilter();
	            	            	
	            	            	storeCombo.filterBy(function (filteredRecord) {
	            	            	
		            	            	var valorEnKey   = filteredRecord.get('key').toUpperCase().lastIndexOf(newvalue);
		            	            	var valorEnValue = filteredRecord.get('value').toUpperCase().lastIndexOf(newvalue);
		            	            		
		            	            	if(valorEnKey > -1 || valorEnValue > -1){
		            	            		return true;
		            	            	}else{
		            	            		return false;
		            	            	}
				            	    });
	            	            }
	            	        },*/
	            	select: function(combo, records){
	            		panelPeticiones.down('[name=params.pv_cdramo_i]').getStore().load({
	            			params: {
	            				'params.idPadre': records[0].get('key')
	            			}
	            		});
	            	}
	            	
	            },
	            store         : Ext.create('Ext.data.Store', {
	                model     : 'Generic',
	                autoLoad  : true,
	                proxy     : {
	                    type        : 'ajax'
	                    ,url        : _URL_CARGA_CATALOGO
	                    ,extraParams: {catalogo:_CAT_SUCURSALES}
	                    ,reader     :
	                    {
	                        type  : 'json'
	                        ,root : 'lista'
	                    }
	                }
	            })
	        },{
	            xtype         : 'combobox',
	            labelWidth    : 50,
	            width         : 200,
	            name          : 'params.pv_cdramo_i',
	            fieldLabel    : 'Producto',
	            valueField    : 'key',
	            displayField  : 'value',
	            forceSelection: true,
	            queryMode     : 'local',
	            typeAhead     : true,
	            anyMatch      : true,
	            tpl: Ext.create('Ext.XTemplate',
	                    '<tpl for=".">',
	                        '<div class="x-boundlist-item">{key} - {value}</div>',
	                    '</tpl>'
	            ),
	            store         : Ext.create('Ext.data.Store', {
	                model     : 'Generic',
	                proxy     : {
	                    type        : 'ajax'
	                    ,url        : _URL_CARGA_CATALOGO
	                    ,extraParams: {catalogo:_CAT_PRODUCTOS}
	                    ,reader     :
	                    {
	                        type  : 'json'
	                        ,root : 'lista'
	                    }
	                }
	            })
	        },{
		    	xtype: 'textfield',
		    	fieldLabel: 'P&oacute;liza',
		    	labelWidth: 30,
		    	name: 'params.pv_nmpoliza_i'
		    },{
	            xtype         : 'combobox',
	            labelWidth    : 60,
	            width         : 250,
	            name          : 'params.pv_cdcodigo_i',
	            fieldLabel    : 'Tipo Error',
	            valueField    : 'key',
	            displayField  : 'value',
	            forceSelection: true,
	            queryMode     : 'local',
	            typeAhead     : true,
	            anyMatch      : true,
	            store         : Ext.create('Ext.data.Store', {
	                model     : 'Generic',
	                autoLoad  : true,
	                proxy     : {
	                    type        : 'ajax'
	                    ,url        : _URL_CARGA_CATALOGO
	                    ,extraParams: {catalogo:_CAT_TIPOS_ERROR_WS}
	                    ,reader     :
	                    {
	                        type  : 'json'
	                        ,root : 'lista'
	                    }
	                }
	            })
	        }],
		    buttons:
		    	[	{
	                    text     : 'Limpiar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
	                    ,handler : function()
	                    {
	                    	panelPeticiones.getForm().reset();
	                    }
	                },
	                {
	                    text     : 'Buscar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
	                    ,handler : function()
	                    {
	                        if(panelPeticiones.isValid())
	                        {
	                        	recargaGridPeticiones();
	                        }
	                        else
	                        {
	                            Ext.Msg.show(
	                            {
	                                title    : 'Datos imcompletos'
	                                ,icon    : Ext.Msg.WARNING
	                                ,msg     : 'Favor de llenar los campos requeridos'
	                                ,buttons : Ext.Msg.OK
	                            });
	                        }
	                    }
	                }
	            ]
	    },gridPeticiones]
	    
	});
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
	recargaGridPeticiones = function(){
		
		cargaStorePaginadoLocal(peticionesStore, _UrlPeticionesFallidas, 'loadList', panelPeticiones.getValues(), function (options, success, response){
    		if(success){
                var jsonResponse = Ext.decode(response.responseText);
                
                if(!jsonResponse.success) {
                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                }
            }else{
                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
            }
    	}, gridPeticiones);
	};
	

});
</script>

</head>
<body>
<div id="mainDivPet" style="height:450px;"></div>
</body>
</html>