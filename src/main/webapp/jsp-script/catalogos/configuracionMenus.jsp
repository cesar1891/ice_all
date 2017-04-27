<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configuraci&oacute;n de Men&uacute;s por Rol</title>
<script>

///////////////////////
////// Overrides //////
/*///////////////////*/
// Sobreescribimos CheckboxModel con Selección Simple:
Ext.selection.CheckboxModel.override({
    mode: 'SINGLE',
    allowDeselect: true
});

///////////////////////
////// variables //////
/*///////////////////*/
var _CONTEXT = '${ctx}';
extjs_custom_override_mayusculas = false;

var _CDELEMENTO_DEFAULT_GSEGUROS = '6442'; 
var _ESTADO_MENU_ACTIVO = '1'; 
var _TIPO_MENU_SESION = '2'; 

var _CDNIVEL_PADRE_RAIZ = '0';
var _CDTITULO_PADRE_RAIZ = '221';

var recargaGridMenus;

var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CAT_ROLES_SISTEMA  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ROLES_SISTEMA"/>';
var _CAT_TIPOS_MENU     = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_MENU"/>';

var _UrlOpcionesLiga      = '<s:url namespace="/catalogos"    action="obtieneOpcionesLiga" />';

var _UrlMenusPorRol = '<s:url namespace="/catalogos"    action="obtieneMenusPorRol" />';
var _UrlGuardaMenu  = '<s:url namespace="/catalogos"    action="guardaMenu" />';
var _UrlEliminaMenu = '<s:url namespace="/catalogos"    action="eliminaMenu" />';

var _UrlOpMenu        = '<s:url namespace="/catalogos"    action="obtieneOpMenu" />';
var _UrlGuardaOpMenu  = '<s:url namespace="/catalogos"    action="guardaOpMenu" />';
var _UrlEliminaOpMenu = '<s:url namespace="/catalogos"    action="eliminaOpMenu" />';

var _UrlOpSubMenu        = '<s:url namespace="/catalogos"    action="obtieneOpSubMenu" />';
var _UrlGuardaOpSubMenu  = '<s:url namespace="/catalogos"    action="guardaOpMenu" />'; // este action se reusa en opciones menu y opciones submenu
var _UrlEliminaOpSubMenu = '<s:url namespace="/catalogos"    action="eliminaOpMenu" />';// este action se reusa en opciones menu y opciones submenu

var _MSG_SIN_DATOS          = 'No hay datos';
var _MSG_BUSQUEDA_SIN_DATOS = 'No hay datos para la b\u00FAsqueda actual.';

/*///////////////////*/
////// variables //////
///////////////////////

Ext.onReady(function()
{
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	
	Ext.define('gridOpLigaModel',
			{
				extend : 'Ext.data.Model'
				,fields :
				['CDTITULO','DSTITULO','DSURL','SWTIPDES','DSTIPDES']
	});
	
	Ext.define('gridMenusModel',
	{
		extend : 'Ext.data.Model'
		,fields :
		['CDMENU','DSMENU','CDELEMENTO','CDPERSON','CDROL', 'CDUSUARIO', 'CDESTADO', 'CDTIPOMENU']
	});
	
	Ext.define('gridOpMenuModel',
			{
				extend : 'Ext.data.Model'
				,fields :
				['CDNIVEL','CDNIVEL_PADRE','DSMENU_EST','CDTITULO','DSTITULO']
	});
	
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	
	var menusStore = Ext.create('Ext.data.Store',
    {
		pageSize : 20,
        autoLoad : true
        ,model   : 'gridMenusModel'
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
	
	
	var gridMenus = Ext.create('Ext.grid.Panel',
	    {
    	title : 'Menus del rol seleccionado'
    	,height : 250
    	,selType: 'checkboxmodel'
    	,store : menusStore
    	,columns :
    	[ { header     : 'CDMENU' , dataIndex : 'CDMENU', hidden: true},
    	  { header     : 'CDELEMENTO' , dataIndex : 'CDELEMENTO', hidden: true},
          { header     : 'Nombre Men&uacute;', dataIndex : 'DSMENU', flex: 1},
          { header     : 'CDPERSON' , dataIndex : 'CDPERSON', hidden: true},
          { header     : 'CDROL' , dataIndex : 'CDROL', hidden: true},
          { header     : 'CDUSUARIO' , dataIndex : 'CDUSUARIO', hidden: true},
          { header     : 'CDESTADO' ,dataIndex : 'CDESTADO', hidden: true},
          { header     : 'Tipo Men&uacute;' ,dataIndex : 'CDTIPOMENU', flex: 1, renderer : function(value){
        	  if("1"== value) return "MENU GENERAL";
        	  	else if("2"== value) return "MENU SESION";
        	  		else return res;
          }}
		]
    	,bbar     :
        {
            displayInfo : true,
            store       : menusStore,
            xtype       : 'pagingtoolbar'
            
        },
        tbar: [{
            icon    : '${ctx}/resources/fam3icons/icons/add.png',
            text    : 'Agregar',
            handler : function()
            {
            	if(Ext.isEmpty(panelMenus.down('[name=cdsisrol]').getValue())){
            		showMessage("Aviso","Debe seleccionar un rol.", Ext.Msg.OK, Ext.Msg.INFO);
            		return;
            	}
            	agregarEditarMenu();
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
            text    : 'Editar',
            handler : function()
            {
            	var model =  gridMenus.getSelectionModel();
            	if(model.hasSelection()){
            		var record = model.getLastSelected();
            		agregarEditarMenu(record);
            		
            	}else{
            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
            text    : 'Eliminar',
            scope   : this,
            handler : function (btn, e){
            	var model =  gridMenus.getSelectionModel();
            	if(model.hasSelection()){
            		
            		
            		Ext.Msg.show({
    		            title: 'Aviso',
    		            msg: '&iquest;Esta seguro que desea eliminar este menu?',
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		Ext.Msg.show({
    		    		            title: 'Aviso',
    		    		            msg: 'Se eliminar&aacute;n todos los Sub Men&uacute;s asociados a este Men&uacute;',
    		    		            buttons: Ext.Msg.YESNO,
    		    		            fn: function(buttonId, text, opt) {
    		    		            	if(buttonId == 'yes') {
    		    		            		Ext.Ajax.request({
    		            						url: _UrlEliminaMenu,
    		            						params: {
    		            					    		'params.pv_cdmenu_i' : model.getLastSelected().get('CDMENU')
    		            						},
    		            						success: function(response, opt) {
    		            							var jsonRes=Ext.decode(response.responseText);

    		            							if(jsonRes.success == true){
    		            								mensajeCorrecto('Aviso','Se ha eliminado el menu correctamente.');
    		    										recargaGridMenus();        							
    		                   						}else {
    		                   							mensajeError('No se pudo eliminar el menu.');
    		                   						}
    		            						},
    		            						failure: function(){
    		            							mensajeError('No se pudo eliminar el menu.');
    		            						}
    		            					});
    		    		            	}
    		            			},
    		    		            animateTarget: btn,
    		    		            icon: Ext.Msg.WARNING
    		        			});
    		            	}
            			},
    		            animateTarget: btn,
    		            icon: Ext.Msg.QUESTION
        			});
            		
            	}else{
            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            }
        },'-',{
            icon    : '${ctx}/resources/fam3icons/icons/user_edit.png',
            text    : 'Editar Opciones Men&uacute;',
            handler : function()
            {
            	var model =  gridMenus.getSelectionModel();
            	if(model.hasSelection()){
            		var record = model.getLastSelected();
					windowOpcionesMenu(record.get('CDMENU'), record.get('CDTIPOMENU'));            		
            	}else{
            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            }
        }]
    });
		
	var panelMenus = Ext.create('Ext.panel.Panel',{
		title: 'Administraci&oacute;n de Men&uacute;s',
		defaults : {
			style : 'margin : 5px;'
		}
	    ,renderTo : 'mainDivMenus'
	    ,items :
	    [{
	    	layout: 'column',
	    	border: false,
	    	html:'<br/><strong>Agregar o editar Cat&aacute;logo de URLs:</strong><br/>'
	     },{
	    	xtype: 'button',
	    	text : 'Agregar/Editar Cat&aacute;logo de URLs',
	    	tooltip: 'Agrega o edita las URLs para los Men&uacute;s',
	    	handler: verEditarOpcionesURL
	    },{
	    	layout: 'column',
	    	border: false,
	    	html:'<br/><strong>Agregar o editar Roles:</strong><br/>'
	     },{
	    	xtype: 'button',
	    	text : 'Agregar Rol',
	    	tooltip: 'Agrega o edita los roles en la aplicaci&oacute;n',
	    	icon    : '${ctx}/resources/fam3icons/icons/award_star_silver_2.png',
	    	handler: crearEditarRoles
	    },
	     {
	    	layout: 'column',
	    	border: false,
	    	html:'<br/><strong>Para configurar los Men&uacute;s, primero seleccione un rol:</strong><br/>'
	     },
	     {
            xtype         : 'combobox',
            labelWidth    : 100,
            width         : 400,
            name          : 'cdsisrol',
            fieldLabel    : 'Rol del Sistema',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            queryMode   : 'remote',
            queryParam  : 'params.dsRol',
            minChars    : 3,
            forceSelection: true,
            typeAhead     : true,
            anyMatch      : true,
            store         : Ext.create('Ext.data.Store', {
                model     : 'Generic',
                autoLoad  : true,
                proxy     : {
                    type        : 'ajax'
                    ,url        : _URL_CARGA_CATALOGO
                    ,extraParams: {catalogo:_CAT_ROLES_SISTEMA}
                    ,reader     :
                    {
                        type  : 'json'
                        ,root : 'lista'
                    }
                }
            }),
           listeners: {
        	   select: function ( combo, records, eOpts ){
        		   //var cdrol = records[0].get('key');
        		   recargaGridMenus();
        		   
        	   }
           }
        },{
	    	layout: 'column',
	    	border: false,
	    	html:'<br/>'
	     },
        gridMenus]
	});
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
	recargaGridMenus = function(){
    	
		var params = {
			'params.pv_cdmenu_i': '',
			'params.pv_dsmenu_i': '',
			'params.pv_cdrol_i'	: panelMenus.down('[name=cdsisrol]').getValue()
		};
		cargaStorePaginadoLocal(menusStore, _UrlMenusPorRol, 'loadList', params, function (options, success, response){
    		if(success){
                var jsonResponse = Ext.decode(response.responseText);
                
                if(!jsonResponse.success) {
                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                }
            }else{
                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
            }
    	}, gridMenus);
	};
	
	
	function agregarEditarMenu(record){

		var panelEdicionMenu = Ext.create('Ext.form.Panel',{
	        border  : false
	        ,bodyStyle:'padding:5px;'
	        ,url: _UrlGuardaMenu
	        ,items :
	        [   {
			        xtype      : 'textfield'
			    	,fieldLabel : 'Nombre del Men&uacute;'
		    		,labelWidth: 120
		    		,width: 350
		    		,allowBlank:false
			    	,name       : 'params.pv_dsmenu_i'
			    	,value: record? record.get('DSMENU') : ''
	    		},{
                    xtype         : 'combobox',
                    labelWidth    : 120,
                    width         : 350,
                    name          : 'params.pv_cdtipomenu_i',
                    fieldLabel    : 'Tipo de Men&uacute;',
		    		allowBlank    : false,
		    		readOnly      : record?true:false,
                    valueField    : 'key',
                    displayField  : 'value',
                    forceSelection: true,
                    queryMode     :'local',
                    store         : Ext.create('Ext.data.Store', {
                        model     : 'Generic',
                        autoLoad  : true,
                        proxy     : {
                            type        : 'ajax'
                            ,url        : _URL_CARGA_CATALOGO
                            ,extraParams: {catalogo:_CAT_TIPOS_MENU}
                            ,reader     :
                            {
                                type  : 'json'
                                ,root : 'lista'
                            }
                        },
                        listeners:{
                        	load: function(){
                        		panelEdicionMenu.getForm().findField('params.pv_cdtipomenu_i').setValue(record? record.get('CDTIPOMENU') : '');
                        	}
                        }
                    })
                }
	        ]
	    });
		 var windowMenu = Ext.create('Ext.window.Window', {
	          title: record?'Editar Men&uacute;':'Agregar Men&uacute;',
	          closeAction: 'hide',
	          modal:true,
	          items:[panelEdicionMenu],
	          bodyStyle:'padding:15px;',
	          buttons:[{
	                 text: 'Guardar',
	                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
	                 handler: function() {
	                       if (panelEdicionMenu.form.isValid()) {
	                    	   
	                    	   panelEdicionMenu.form.submit({
	           		        	waitMsg:'Procesando...',			
	           		        	params: {
	           		        		'params.pv_cdmenu_i'    : record? record.get('CDMENU') : '',
	           		        		'params.pv_cdelemento_i': _CDELEMENTO_DEFAULT_GSEGUROS,
	           		        		'params.pv_cdperson_i'  : '',
	           		        		'params.pv_cdrol_i'     : record? record.get('CDROL') : panelMenus.down('[name=cdsisrol]').getValue(),
	           		        		'params.pv_cdusuario_i' : '',
	           		        		'params.pv_cdestado_i'  : _ESTADO_MENU_ACTIVO
	           		        	},
	           		        	failure: function(form, action) {
	           		        		mensajeError("Error al guardar el Men&uacute;. " + action.result.errorMessage);
	           					},
	           					success: function(form, action) {
	           						recargaGridMenus();
	           						panelEdicionMenu.getForm().reset();
	           						windowMenu.close();
	                               	mensajeCorrecto("Aviso","Se ha guardado el Men&uacute;");
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
	             },
	           {
	                 text: 'Cancelar',
	                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
	                 handler: function() {
	                	 panelEdicionMenu.getForm().reset();
	                	 windowMenu.close();
	                 }
	           }
	          ]
	          });
		 windowMenu.show();
	}
	
	
	function windowOpcionesMenu(_cdMenu, _cdTipoMenu){
		debug('CdMenu: ',_cdMenu);

		/**
		 * If, para que un tipo menu sesion no tenga submenus
		 */
		if(_TIPO_MENU_SESION == _cdTipoMenu){
			windowOpcionesSubMenu(_CDNIVEL_PADRE_RAIZ,'Menu Sesion');
			return;
		}
		
		var recargaGridOpMenu;
		
		var opcionesMenuStore = Ext.create('Ext.data.Store',
			    {
					pageSize : 20,
			        autoLoad : true
			        ,model   : 'gridOpMenuModel'
			        ,proxy   :
			        {
			            type         : 'memory'
			            ,enablePaging : true
			            ,reader      : 'json'
			            ,data        : []
			        }
		});
		
		var gridOpMenu = Ext.create('Ext.grid.Panel',
			    {
		    	height : 250
		    	,selType: 'checkboxmodel'
		    	,store : opcionesMenuStore
		    	,columns :
		    	[ { header     : 'CDNIVEL' , dataIndex : 'CDNIVEL', hidden: true},
		          { header     : 'Nombre Opci&oacute;n Men&uacute;', dataIndex : 'DSMENU_EST', flex: 1}
				]
		    	,bbar     :
		        {
		            displayInfo : true,
		            store       : opcionesMenuStore,
		            xtype       : 'pagingtoolbar'
		            
		        },
		        tbar: [{
		            icon    : '${ctx}/resources/fam3icons/icons/add.png',
		            text    : 'Agregar',
		            handler : function()
		            {
		            	agregarEditarOpMenu();
		            }
		        },{
		            icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
		            text    : 'Editar',
		            handler : function()
		            {
		            	var model =  gridOpMenu.getSelectionModel();
		            	if(model.hasSelection()){
		            		var record = model.getLastSelected();
		            		agregarEditarOpMenu(record);
		            		
		            	}else{
		            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		            	}
		            }
		        },{
		            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
		            text    : 'Eliminar',
		            scope   : this,
		            handler : function (btn, e){
		            	var model =  gridOpMenu.getSelectionModel();
		            	if(model.hasSelection()){
		            		
		            		Ext.Msg.show({
		    		            title: 'Aviso',
		    		            msg: '&iquest;Esta seguro que desea eliminar esta opci&oacute;n de menu?',
		    		            buttons: Ext.Msg.YESNO,
		    		            fn: function(buttonId, text, opt) {
		    		            	if(buttonId == 'yes') {
		    		            		Ext.Ajax.request({
		            						url: _UrlEliminaOpMenu,
		            						params: {
		            					    		'params.pv_cdmenu_i'  : _cdMenu,
		            					    		'params.pv_cdnivel_i' : model.getLastSelected().get('CDNIVEL')
		            						},
		            						success: function(response, opt) {
		            							var jsonRes=Ext.decode(response.responseText);

		            							if(jsonRes.success == true){
		            								mensajeCorrecto('Aviso','Se ha eliminado la opci&oacute;n de menu correctamente.');
		            								recargaGridOpMenu();      							
		                   						}else {
		                   							mensajeError('No se pudo eliminar la opci&oacute;n de menu.');
		                   						}
		            						},
		            						failure: function(){
		            							mensajeError('No se pudo eliminar la opci&oacute;n de menu.');
		            						}
		            					});
		    		            	}
		            			},
		    		            animateTarget: btn,
		    		            icon: Ext.Msg.QUESTION
		        			});
		            		
		            	}else{
		            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		            	}
		            }
		        },'-',{
		            icon    : '${ctx}/resources/fam3icons/icons/user_edit.png',
		            text    : 'Editar Opciones Sub Men&uacute;',
		            handler : function()
		            {
		            	var model =  gridOpMenu.getSelectionModel();
		            	if(model.hasSelection()){
		            		var record = model.getLastSelected();
		            		windowOpcionesSubMenu(record.get('CDNIVEL'), record.get('DSMENU_EST'));
		            	}else{
		            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		            	}
		            }
		        }]
		    });
		var windowOpMenu = Ext.create('Ext.window.Window', {
			  title : 'Opciones de Men&uacute;',
	          modal:true,
	          width : 700,
	          height : 350,
	          items:[gridOpMenu],
	          //bodyStyle:'padding:5px;',
	          buttons:[{
		                 text: 'Regresar / Salir',
		                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
		                 handler: function() {
		                	 windowOpMenu.close();
		                 }
	           		} ]
	          });
		 	windowOpMenu.show();
		 	
		 	recargaGridOpMenu = function(){
		    	
				var params = {
					'params.PV_CDMENU_I'	: _cdMenu
				};
				cargaStorePaginadoLocal(opcionesMenuStore, _UrlOpMenu, 'loadList', params, function (options, success, response){
		    		if(success){
		                var jsonResponse = Ext.decode(response.responseText);
		                
		                if(!jsonResponse.success) {
		                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
		                }
		            }else{
		                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
		            }
		    	}, gridOpMenu);
			};
			
			recargaGridOpMenu();
		
		 	function agregarEditarOpMenu(record){

				var panelEdicionOpMenu = Ext.create('Ext.form.Panel',{
			        border  : false
			        ,bodyStyle:'padding:5px;'
			        ,url: _UrlGuardaOpMenu
			        ,items :
			        [   {
					        xtype      : 'textfield'
					    	,fieldLabel : 'Nombre de la Opci&oacute;n de Men&uacute;'
				    		,labelWidth: 120
				    		,width: 350
				    		,allowBlank:false
					    	,name       : 'params.pv_dsmenu_est_i'
					    	,value: record? record.get('DSMENU_EST') : ''
			    		}
			        ]
			    });
				 var windowEdicionOpMenu = Ext.create('Ext.window.Window', {
			          title: record?'Editar Opci&oacute;n Men&uacute;':'Agregar Opci&oacute;n Men&uacute;',
			          closeAction: 'hide',
			          modal:true,
			          items:[panelEdicionOpMenu],
			          bodyStyle:'padding:15px;',
			          buttons:[{
			                 text: 'Guardar',
			                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
			                 handler: function() {
			                       if (panelEdicionOpMenu.form.isValid()) {
			                    	   
			                    	   panelEdicionOpMenu.form.submit({
			           		        	waitMsg:'Procesando...',			
			           		        	params: {
			           		        		'params.pv_cdmenu_i'       : _cdMenu,
			           		        		'params.pv_cdnivel_i'      : record? record.get('CDNIVEL') : '',
			           		        		'params.pv_cdramo_i'       : '',
			           		        		'params.pv_cdnivel_padre_i': _CDNIVEL_PADRE_RAIZ,
			           		        		'params.pv_cdtitulo_i'     : _CDTITULO_PADRE_RAIZ,
			           		        		'params.pv_cdestado_i'     : _ESTADO_MENU_ACTIVO,
			           		        		'params.pv_cdtipsit_i'     : ''
			           		        	},
			           		        	failure: function(form, action) {
			           		        		mensajeError("Error al guardar la Opci&oacute;n de Men&uacute;. " + action.result.errorMessage);
			           					},
			           					success: function(form, action) {
			           						recargaGridOpMenu();
			           						panelEdicionOpMenu.getForm().reset();
			           						windowEdicionOpMenu.close();
			                               	mensajeCorrecto("Aviso","Se ha guardado la Opci&oacute;n de Men&uacute;");
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
			             },
			           {
			                 text: 'Cancelar',
			                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
			                 handler: function() {
			                	 panelEdicionOpMenu.getForm().reset();
			                	 windowEdicionOpMenu.close();
			                 }
			           }
			          ]
			          });
				 windowEdicionOpMenu.show();
			}
		 	
		 	
		 	function windowOpcionesSubMenu(_cdNivelPadre, _NombreOpcionPadre){
				debug('CdNivelPadre: ',_cdNivelPadre);
				
				var recargaGridOpSubMenu;
				
				var opcionesSubMenuStore = Ext.create('Ext.data.Store',
					    {
							pageSize : 20,
					        autoLoad : true
					        ,model   : 'gridOpMenuModel'
					        ,proxy   :
					        {
					            type         : 'memory'
					            ,enablePaging : true
					            ,reader      : 'json'
					            ,data        : []
					        }
				});
				
				var gridOpSubMenu = Ext.create('Ext.grid.Panel',
					    {
				    	height : 300
				    	,selType: 'checkboxmodel'
				    	,store : opcionesSubMenuStore
				    	,columns :
				    	[ { header     : 'CDNIVEL' , dataIndex : 'CDNIVEL', hidden: true},
				    	  { header     : 'CDTITULO' , dataIndex : 'CDTITULO', hidden: true},
				          { header     : 'Nombre Opci&oacute;n Men&uacute;', dataIndex : 'DSMENU_EST', flex: 1},
				          { header     : 'Nombre de la URL', dataIndex : 'DSTITULO', flex: 1}
						]
				    	,bbar     :
				        {
				            displayInfo : true,
				            store       : opcionesSubMenuStore,
				            xtype       : 'pagingtoolbar'
				            
				        },
				        tbar: [{
				            icon    : '${ctx}/resources/fam3icons/icons/add.png',
				            text    : 'Agregar',
				            handler : function()
				            {
				            	agregarEditarOpSubMenu();
				            }
				        },{
				            icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
				            text    : 'Editar',
				            handler : function()
				            {
				            	var model =  gridOpSubMenu.getSelectionModel();
				            	if(model.hasSelection()){
				            		var record = model.getLastSelected();
				            		agregarEditarOpSubMenu(record);
				            		
				            	}else{
				            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
				            	}
				            }
				        },{
				            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
				            text    : 'Eliminar',
				            scope   : this,
				            handler : function (btn, e){
				            	var model =  gridOpSubMenu.getSelectionModel();
				            	if(model.hasSelection()){
				            		
				            		Ext.Msg.show({
				    		            title: 'Aviso',
				    		            msg: '&iquest;Esta seguro que desea eliminar esta opci&oacute;n de Sub Men&uacute;?',
				    		            buttons: Ext.Msg.YESNO,
				    		            fn: function(buttonId, text, opt) {
				    		            	if(buttonId == 'yes') {
				    		            		Ext.Ajax.request({
				            						url: _UrlEliminaOpSubMenu,
				            						params: {
				            					    		'params.pv_cdmenu_i'  : _cdMenu,
				            					    		'params.pv_cdnivel_i' : model.getLastSelected().get('CDNIVEL')
				            						},
				            						success: function(response, opt) {
				            							var jsonRes=Ext.decode(response.responseText);

				            							if(jsonRes.success == true){
				            								mensajeCorrecto('Aviso','Se ha eliminado la opci&oacute;n de Sub Men&uacute; correctamente.');
				            								recargaGridOpSubMenu();      							
				                   						}else {
				                   							mensajeError('No se pudo eliminar la opci&oacute;n de Sub Men&uacute;.');
				                   						}
				            						},
				            						failure: function(){
				            							mensajeError('No se pudo eliminar la opci&oacute;n de Sub Men&uacute;.');
				            						}
				            					});
				    		            	}
				            			},
				    		            animateTarget: btn,
				    		            icon: Ext.Msg.QUESTION
				        			});
				            		
				            	}else{
				            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
				            	}
				            }
				        },'-',{
				            icon    : '${ctx}/resources/fam3icons/icons/user_edit.png',
				            text    : 'Editar Opciones Sub Men&uacute;',
				            hidden  : _TIPO_MENU_SESION == _cdTipoMenu?true:false,
				            handler : function()
				            {
				            	var model =  gridOpSubMenu.getSelectionModel();
				            	if(model.hasSelection()){
				            		var record = model.getLastSelected();
				            		windowOpcionesSubMenu(record.get('CDNIVEL'), record.get('DSMENU_EST'));
				            	}else{
				            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
				            	}
				            }
				        }]
				    });
				
				var windowOpSubMenu = Ext.create('Ext.window.Window', {
					  title : _TIPO_MENU_SESION == _cdTipoMenu?'Opciones de Sub Men&uacute;' : 'Opciones de Sub Men&uacute; para: '+ _NombreOpcionPadre,
			          modal:true,
			          width : 800,
			          height : 400,
			          items:[gridOpSubMenu],
			          //bodyStyle:'padding:5px;',
			          buttons:[{
			        	  text: 'Regresar / Salir',
			                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
				                 handler: function() {
				                	 windowOpSubMenu.close();
				                 }
			           		} ]
			          });
				 	windowOpSubMenu.show();
				 	
				 	recargaGridOpSubMenu = function(){
				    	
						var params = {
							'params.PV_CDMENU_I'	: _cdMenu,
							'params.PV_CDNIVEL_PADRE_I'	: _cdNivelPadre
						};
						cargaStorePaginadoLocal(opcionesSubMenuStore, _UrlOpSubMenu, 'loadList', params, function (options, success, response){
				    		if(success){
				                var jsonResponse = Ext.decode(response.responseText);
				                
				                if(!jsonResponse.success) {
				                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
				                }
				            }else{
				                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
				            }
				    	}, gridOpSubMenu);
					};
					
					recargaGridOpSubMenu();
				
				 	function agregarEditarOpSubMenu(record){

						var panelEdicionOpSubMenu = Ext.create('Ext.form.Panel',{
					        border  : false
					        ,bodyStyle:'padding:5px;'
					        ,url: _UrlGuardaOpSubMenu
					        ,items :
					        [   {
							        xtype      : 'textfield'
							    	,fieldLabel : 'Nombre de la Opci&oacute;n de Sub Men&uacute;'
						    		,labelWidth: 120
						    		,width: 400
						    		,allowBlank:false
							    	,name       : 'params.pv_dsmenu_est_i'
							    	,value: record? record.get('DSMENU_EST') : ''
					    		},{
			                        xtype         : 'combobox',
			                        labelWidth    : 120,
			                        width         : 400,
			                        name          : 'params.pv_cdtitulo_i',
			                        fieldLabel    : 'Nombre de la URL',
						    		allowBlank    : false,
						    		queryMode   : 'remote',
						            queryParam  : 'params.pv_dstitulo_i',
						            minChars    : 3,
			                        valueField    : 'CDTITULO',
			                        displayField  : 'DSTITULO',
			                        tpl: Ext.create('Ext.XTemplate',
			                                '<tpl for=".">',
			                                    '<div class="x-boundlist-item">{CDTITULO} - {DSTITULO}</div>',
			                                '</tpl>'
			                        ),
			                        displayTpl: Ext.create('Ext.XTemplate',
			                                '<tpl for=".">',
			                                    '{CDTITULO} - {DSTITULO}',
			                                '</tpl>'
			                        ),
			                        forceSelection: true,
			                        store         : Ext.create('Ext.data.Store', {
				                        model     : 'gridOpLigaModel',
				                        autoLoad  : true,
				                        proxy     : {
				                            type         : 'ajax'
				                            ,url         : _UrlOpcionesLiga
				                            ,extraParams : {'params.pv_dstitulo_i' : ''}
				                            ,reader      : { type  : 'json' ,root : 'loadList' }
				                        },
				                        listeners: {
				                        	load: function(){
				                        		panelEdicionOpSubMenu.getForm().findField('params.pv_cdtitulo_i').setValue(record? record.get('CDTITULO') : '0');
				                        	}
				                        }
			                        })
			                    },{
			            	    	xtype: 'button',
			            	    	text : 'Agregar/Editar Cat&aacute;logo de URLs',
			            	    	tooltip: 'Agrega o edita las URLs para los Men&uacute;s',
			            	    	handler: verEditarOpcionesURL
			            	    }
					        ]
					    });
						 var windowEdicionOpSubMenu = Ext.create('Ext.window.Window', {
					          title: record?'Editar Opci&oacute;n Sub Men&uacute;':'Agregar Opci&oacute;n Sub Men&uacute;',
					          closeAction: 'hide',
					          modal:true,
					          items:[panelEdicionOpSubMenu],
					          bodyStyle:'padding:15px;',
					          buttons:[{
					                 text: 'Guardar',
					                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
					                 handler: function() {
					                       if (panelEdicionOpSubMenu.form.isValid()) {
					                    	   
					                    	   panelEdicionOpSubMenu.form.submit({
					           		        	waitMsg:'Procesando...',			
					           		        	params: {
					           		        		'params.pv_cdmenu_i'       : _cdMenu,
					           		        		'params.pv_cdnivel_i'      : record? record.get('CDNIVEL') : '',
					           		        		'params.pv_cdramo_i'       : '',
					           		        		'params.pv_cdnivel_padre_i': _cdNivelPadre,
					           		        		'params.pv_cdestado_i'     : _ESTADO_MENU_ACTIVO,
					           		        		'params.pv_cdtipsit_i'     : ''
					           		        	},
					           		        	failure: function(form, action) {
					           		        		mensajeError("Error al guardar la Opci&oacute;n de Sub Men&uacute;. " + action.result.errorMessage);
					           					},
					           					success: function(form, action) {
					           						recargaGridOpSubMenu();
					           						panelEdicionOpSubMenu.getForm().reset();
					           						windowEdicionOpSubMenu.close();
					                               	mensajeCorrecto("Aviso","Se ha guardado la Opci&oacute;n de Sub Men&uacute;");
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
					             },
					           {
					                 text: 'Cancelar',
					                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
					                 handler: function() {
					                	 panelEdicionOpSubMenu.getForm().reset();
					                	 windowEdicionOpSubMenu.close();
					                 }
					           }
					          ]
					          });
						 windowEdicionOpSubMenu.show();
					}
			}
	}
	
	function verEditarOpcionesURL(){
		
		var _UrlGuardaOpcionLiga  = '<s:url namespace="/catalogos"    action="guardaOpcionLiga" />';
		var _UrlEliminaOpcionLiga = '<s:url namespace="/catalogos"    action="eliminaOpcionLiga" />';
		
		var recargaGridOpcionesLiga;
		
		var opcionesLigaStore = Ext.create('Ext.data.Store',
			    {
					pageSize : 20,
			        autoLoad : true
			        ,model   : 'gridOpLigaModel'
			        ,proxy   :
			        {
			            type         : 'memory'
			            ,enablePaging : true
			            ,reader      : 'json'
			            ,data        : []
			        }
			    });
		
		var gridOpcionesLiga = Ext.create('Ext.grid.Panel',
			    {
		    	title : 'URLs existentes para Men&uacute;s'
		    	,height : 300
		    	,selType: 'checkboxmodel'
		    	,store : opcionesLigaStore
		    	,columns :
		    	[ { header     : 'C&oacute;digo' , dataIndex : 'CDTITULO', flex: 1},
		    	  { header     : 'Nombre de URL' , dataIndex : 'DSTITULO', flex: 3},
		          { header     : 'URL' , dataIndex : 'DSURL' , flex: 5}
				]
		    	,bbar     :
		        {
		            displayInfo : true,
		            store       : opcionesLigaStore,
		            xtype       : 'pagingtoolbar'
		        },
		        tbar: [{
		            icon    : '${ctx}/resources/fam3icons/icons/add.png',
		            text    : 'Agregar',
		            handler : function(){agregarEditarOpcionLiga();}
		        },{
		            icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
		            text    : 'Editar',
		            handler : function()
		            {
		            	var model =  gridOpcionesLiga.getSelectionModel();
		            	if(model.hasSelection()){
		            		var record = model.getLastSelected();
		            		agregarEditarOpcionLiga(record);
		            		
		            	}else{
		            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		            	}
		            }
		        },{
		            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
		            text    : 'Eliminar',
		            scope   : this,
		            handler : function (btn, e){
		            	var model =  gridOpcionesLiga.getSelectionModel();
		            	if(model.hasSelection()){
		            		
		            		Ext.Msg.show({
		    		            title: 'Aviso',
		    		            msg: '&iquest;Esta seguro que desea eliminar esta URL?',
		    		            buttons: Ext.Msg.YESNO,
		    		            fn: function(buttonId, text, opt) {
		    		            	if(buttonId == 'yes') {
		    		            		Ext.Ajax.request({
		            						url: _UrlEliminaOpcionLiga,
		            						params: {
		            					    		'params.pv_cdtitulo_i' : model.getLastSelected().get('CDTITULO')
		            						},
		            						success: function(response, opt) {
		            							var jsonRes=Ext.decode(response.responseText);

		            							if(jsonRes.success == true){
		            								mensajeCorrecto('Aviso','Se ha eliminado la URL correctamente.');
		            								recargaGridOpcionesLiga();        							
		                   						}else {
		                   							mensajeError(jsonRes.errorMessage);
		                   						}
		            						},
		            						failure: function(){
		            							mensajeError('No se pudo eliminar la URL.');
		            						}
		            					});
		    		            	}
		            			},
		    		            animateTarget: btn,
		    		            icon: Ext.Msg.QUESTION
		        			});
		            		
		            	}else{
		            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		            	}
		            }
		        }]
		    });
		
		
			var windowOpcionesLiga = Ext.create('Ext.window.Window', {
		          title: 'Cat&aacute;logo de URLs',
		          modal:true,
		          height : 450,
		          width  : 800,
		          items:[{
		        	layout: {type:'hbox', pack: 'center'},
		        	defaults: {
			    		style: 'margin: 10px'// para hacer que los componentes se separen 5px
			    	},
			    	border: false,
			    	items: [{
				    	xtype: 'textfield',
				    	fieldLabel: 'Nombre de URL:',
				    	name: 'dstitle',
				    	labelWidth: 120
				    },{
				    	xtype: 'button',
				    	text: 'Buscar URL',
				    	handler: function(){
				    		recargaGridOpcionesLiga();
				    	}
				    }]
		          	},gridOpcionesLiga],
		          bodyStyle:'padding:15px;',
		          buttons:[
		           {
		                 text: 'Aceptar',
		                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
		                 handler: function() {
		                	 windowOpcionesLiga.close();
		                 }
		           }
		          ]
		          });
				windowOpcionesLiga.show();
				
				recargaGridOpcionesLiga = function(){
					
					var params = {
						'params.pv_dstitulo_i'	: windowOpcionesLiga.down('[name=dstitle]').getValue()
					};
					cargaStorePaginadoLocal(opcionesLigaStore, _UrlOpcionesLiga, 'loadList', params, function (options, success, response){
			    		if(success){
			                var jsonResponse = Ext.decode(response.responseText);
			                
			                if(!jsonResponse.success) {
			                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
			                }
			            }else{
			                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
			            }
			    	}, gridOpcionesLiga);
				};
				
				function agregarEditarOpcionLiga(record){

					var panelEdicionOpcionLiga = Ext.create('Ext.form.Panel',{
				        border  : false
				        ,bodyStyle:'padding:5px;'
				        ,url: _UrlGuardaOpcionLiga
				        ,items :
				        [   {
						        xtype      : 'textfield'
						    	,fieldLabel : 'Nombre de la URL'
					    		,labelWidth: 120
					    		,width: 350
					    		,allowBlank:false
						    	,name       : 'params.pv_dstitulo_i'
						    	,value: record? record.get('DSTITULO') : ''
				    		},{
						        xtype      : 'textfield'
							   	,fieldLabel : 'URL'
						    	,labelWidth: 120
						    	,width: 600
						    	,allowBlank:false
							   	,name       : 'params.pv_dsurl_i'
							   	,value: record? record.get('DSURL') : ''
							
					    	}
				        ]
				    });
					
					 var windowEdicionOpcionLiga = Ext.create('Ext.window.Window', {
				          title: record?'Editar URL':'Agregar URL',
				          closeAction: 'hide',
				          modal:true,
				          items:[panelEdicionOpcionLiga],
				          bodyStyle:'padding:15px;',
				          buttons:[{
				                 text: 'Guardar',
				                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
				                 handler: function() {
				                       if (panelEdicionOpcionLiga.form.isValid()) {
				                    	   
				                    	   panelEdicionOpcionLiga.form.submit({
				           		        	waitMsg:'Procesando...',			
				           		        	params: {
				           		        		'params.pv_cdtitulo_i'   : record? record.get('CDTITULO') : ''
				           		        	},
				           		        	failure: function(form, action) {
				           		        		mensajeError("Error al guardar la URL");
				           					},
				           					success: function(form, action) {
				           						
				           						recargaGridOpcionesLiga();
				           						panelEdicionOpcionLiga.getForm().reset();
				           						windowEdicionOpcionLiga.close();
				                               	mensajeCorrecto("Aviso","Se ha guardado la URL.");
				           						
				           						
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
				             },
				           {
				                 text: 'Cancelar',
				                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
				                 handler: function() {
				                	 panelEdicionOpcionLiga.getForm().reset();
				                	 windowEdicionOpcionLiga.close();
				                 }
				           }
				          ]
				          });
					 windowEdicionOpcionLiga.show();
				}
		
	}
	
function crearEditarRoles(){
		
		var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
		var _CAT_ROLES_SISTEMA  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ROLES_SISTEMA"/>';
		var _UrlCrearEditarRol  = '<s:url namespace="/catalogos"    action="creaEditaRolSistema" />';
		
		var recargaGridCreaRoles;
		
		var creaRolesStore = Ext.create('Ext.data.Store',
			    {
					pageSize : 20,
			        autoLoad : true
			        ,model   : 'Generic'
			        ,proxy   :
			        {
			            type         : 'memory'
			            ,enablePaging : true
			            ,reader      : 'json'
			            ,data        : []
			        }
			    });
		
		var gridCreaRoles = Ext.create('Ext.grid.Panel',
			    {
		    	title : 'Roles existentes en la aplicaci&oacute;n'
		    	,height : 300
		    	,selType: 'checkboxmodel'
		    	,store : creaRolesStore
		    	,columns :
		    	[ { header     : 'C&oacute;digo del Rol' , dataIndex : 'key', flex: 2},
		    	  { header     : 'Nombre del Rol' , dataIndex : 'value', flex: 5}
				]
		    	,bbar     :
		        {
		            displayInfo : true,
		            store       : creaRolesStore,
		            xtype       : 'pagingtoolbar'
		        },
		        tbar: [{
		            icon    : '${ctx}/resources/fam3icons/icons/add.png',
		            text    : 'Crear Rol',
		            handler : function(){crearEditarRol();}
		        },{
		            icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
		            text    : 'Editar Rol',
		            handler : function()
		            {
		            	var model =  gridCreaRoles.getSelectionModel();
		            	if(model.hasSelection()){
		            		var record = model.getLastSelected();
		            		crearEditarRol(record);
		            		
		            	}else{
		            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		            	}
		            }
		        }]
		    });
		
		
			var windowCrearRoles = Ext.create('Ext.window.Window', {
		          title: 'Creci&oacute;n de Roles',
		          modal:true,
		          height : 450,
		          width  : 800,
		          items:[{
		        	layout: {type:'hbox', pack: 'center'},
		        	defaults: {
			    		style: 'margin: 10px'// para hacer que los componentes se separen 5px
			    	},
			    	border: false,
			    	items: [{
				    	xtype: 'textfield',
				    	fieldLabel: 'Nombre del Rol:',
				    	name: 'dsCreaEditaRol',
				    	labelWidth: 120
				    },{
				    	xtype: 'button',
				    	text: 'Buscar Rol',
				    	handler: function(){
				    		recargaGridCreaRoles();
				    	}
				    }]
		          	},gridCreaRoles],
		          bodyStyle:'padding:15px;',
		          buttons:[
		           {
		                 text: 'Aceptar',
		                 icon:'${ctx}/resources/fam3icons/icons/accept.png',
		                 handler: function() {
		                	 windowCrearRoles.close();
		                 }
		           }
		          ]
		          });
				windowCrearRoles.show();
				
				recargaGridCreaRoles = function(){
					
					var params = {
						'params.dsRol' : windowCrearRoles.down('[name=dsCreaEditaRol]').getValue(),
						'catalogo'     : _CAT_ROLES_SISTEMA
					};
					cargaStorePaginadoLocal(creaRolesStore, _URL_CARGA_CATALOGO, 'lista', params, function (options, success, response){
			    		if(success){
			                var jsonResponse = Ext.decode(response.responseText);
			                
			                if(!jsonResponse.success) {
			                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
			                }
			            }else{
			                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
			            }
			    	}, gridCreaRoles);
				};
				
				function crearEditarRol(record){

					var panelCrearEditarRol = Ext.create('Ext.form.Panel',{
				        border  : false
				        ,bodyStyle:'padding:5px;'
				        ,url: _UrlCrearEditarRol
				        ,items :
				        [   {
								xtype      : 'hidden',
								name       : 'params.pv_accion_i',
								value      : record? 'U' : 'I'
							},{
						        xtype      : 'textfield'
						    	,fieldLabel : 'C&oacute;digo del Rol'
					    		,labelWidth: 120
					    		,width: 350
					    		,allowBlank:false
					    		,readOnly: record?true:false
						    	,name       : 'params.pv_cdsisrol_i'
						    	,value: record? record.get('key') : ''
				    		},{
						        xtype      : 'textfield'
							   	,fieldLabel : 'Nombre del Rol'
						    	,labelWidth: 120
						    	,width: 350
						    	,allowBlank:false
							   	,name       : 'params.pv_dssisrol_i'
							   	,value: record? record.get('value') : ''
							
					    	}
				        ]
				    });
					
					 var windowCrearEditarRol = Ext.create('Ext.window.Window', {
				          title: record?'Editar Rol':'Crear Rol',
				          closeAction: 'hide',
				          modal:true,
				          items:[panelCrearEditarRol],
				          bodyStyle:'padding:15px;',
				          buttons:[{
				                 text: 'Guardar',
				                 icon: '${ctx}/resources/fam3icons/icons/accept.png',
				                 handler: function() {
				                       if (panelCrearEditarRol.form.isValid()) {
				                    	   
				                    	   panelCrearEditarRol.form.submit({
				           		        	waitMsg:'Procesando...',			
				           		        	failure: function(form, action) {
				           		        		mensajeError("Error al guardar el Rol");
				           					},
				           					success: function(form, action) {
				           						
				           						recargaGridCreaRoles();
				           						panelMenus.down('[name=cdsisrol]').getStore().load();
				           						panelCrearEditarRol.getForm().reset();
				           						windowCrearEditarRol.close();
				                               	mensajeCorrecto("Aviso","Se ha guardado el Rol.");
				           						
				           						
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
				             },
				           {
				                 text: 'Cancelar',
				                 icon: '${ctx}/resources/fam3icons/icons/cancel.png',
				                 handler: function() {
				                	 panelCrearEditarRol.getForm().reset();
				                	 windowCrearEditarRol.close();
				                 }
				           }
				          ]
				          });
					 windowCrearEditarRol.show();
				}
		
	}

});
</script>

</head>
<body>
<div id="mainDivMenus" style="height:450px;"></div>
</body>
</html>