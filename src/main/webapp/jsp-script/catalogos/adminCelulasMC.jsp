<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configuraci&oacute;n de Celulas de Trabajo</title>
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

var _URL_CARGA_CATALOGO      = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CAT_ZONAS_SUCURSALES    = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ZONAS_SUCURSALES"/>';
var _CAT_NIVELES_SUCURSALES  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@NIVELES_SUCURSALES"/>';
var _UrlConsultaConfSucursal = '<s:url namespace="/despachador"    action="cargaConfSucursales" />';
var _UrlActualizaConfSucursales = '<s:url namespace="/despachador" action="guardaConfSucursales" />';
var _UrlAgregaConfSucursal = '<s:url namespace="/despachador" action="agregaConfSucursal" />';
var _UrlEliminaConfSucursal = '<s:url namespace="/despachador" action="eliminaConfSucursal" />';


var _CAT_TIPO_FLUJOS         = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TTIPFLUMC"/>';
var _CAT_FLUJOS_MC           = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TFLUJOMC"/>';
var _CAT_RAMOS               = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS"/>';
var _CAT_SUBRAMOS            = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPSIT"/>';
var _UrlConsultaConfPermisos = '<s:url namespace="/despachador"    action="cargaConfPermisos" />';
var _UrlActualizaConfPermisos = '<s:url namespace="/despachador" action="guardaConfPermisos" />';
var _UrlAgregaConfPermiso = '<s:url namespace="/despachador" action="agregaConfPermiso" />';
var _UrlEliminaConfPermiso = '<s:url namespace="/despachador" action="eliminaConfPermiso" />';

/*///////////////////*/
////// variables //////
///////////////////////

Ext.onReady(function()
{
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	//Modelo de configuracion de Sucursal
	
	Ext.define('modelGridSucursales',
			{
				extend : 'Ext.data.Model'
				,fields :
				['CDUNIECO','DSUNIECO','CDUNIZON','DESCRIPC',{name: 'NMCAPACI', type: 'int'},'CDNIVEL',{name: 'SWAPOYO', type: 'boolean'},{name: 'SWACTIVA', type: 'boolean'}]
	});
	
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	
	var editorPluginSucursales = Ext.create('Ext.grid.plugin.RowEditing', {
    	autoCancel : false,
    	dirtyText  : 'Necesita Aceptar o Cancelar sus cambios',
        clicksToEdit: 2
    });
	
	
	Ext.define('sucursalesStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
				model     : 'Generic',
		        autoLoad  : true,
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo: Cat.SucursalesAdminMC}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		        }
		    }, cfg) ]);
		}
	});

	Ext.define('zonasSucStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
		        autoLoad : true
		        ,model   : 'Generic'
		        ,proxy   :
		        {
		            type        : 'ajax'
		                ,url        : _URL_CARGA_CATALOGO
		                ,extraParams: {catalogo:_CAT_ZONAS_SUCURSALES}
		                ,reader     :
		                {
		                    type  : 'json'
		                    ,root : 'lista'
		                }
		        }
		    }, cfg) ]);
		}
	});

	Ext.define('nivelesSucStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
		        autoLoad : true
		        ,model   : 'Generic'
		        ,proxy   :
		        {
		            type        : 'ajax'
		                ,url        : _URL_CARGA_CATALOGO
		                ,extraParams: {catalogo:_CAT_NIVELES_SUCURSALES}
		                ,reader     :
		                {
		                    type  : 'json'
		                    ,root : 'lista'
		                }
		        }
		    }, cfg) ]);
		}
	});

	var confSucursalesStore = Ext.create('Ext.data.Store',
    {
		pageSize : 20
        ,model   : 'modelGridSucursales'
        ,proxy   :
        {
        	type        : 'ajax'
            ,url        : _UrlConsultaConfSucursal
            ,reader     :
            {
                type  : 'json'
                ,root : 'list'
            }
        }
    });
	
	/*////////////////*/
	////// stores //////
	////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	
	
	var sucursalesGrid = Ext.create('Ext.grid.Panel',
	    {
    	title : 'Sucursales Configuradas. -> De Doble Clic para editar'
    	,height : 320
    	,store : confSucursalesStore
    	,columns :
    	[ { header     : 'Sucursal' , dataIndex : 'DSUNIECO', flex: 2},
    	  { header     : 'Num. Sucursal' , dataIndex : 'CDUNIECO', flex: 1},
          { header     : 'Zona', dataIndex : 'CDUNIZON', flex: 1,
    			editor : {
	                xtype         : 'combobox',
	                allowBlank    : false,
	                name          : 'CDUNIZON',
	                valueField    : 'key',
	                displayField  : 'value',
	                forceSelection: true,
	                anyMatch      : true,
                    queryMode     : 'local',
	                store         : new zonasSucStore()
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnUsandoEditor(value,record,colIndex,this); //this -> sucursalesGrid
	            }
          },
          { header     : 'Capacidad' , dataIndex : 'NMCAPACI', flex: 1,
	  			editor : {
	                xtype         : 'numberfield',
	                allowBlank    : false,
	                name          : 'NMCAPACI',
	               	maxValue      : 999 
	            }
	      },
          { header     : 'Nivel' , dataIndex : 'CDNIVEL', flex: 1,
	  			editor : {
	                xtype         : 'combobox',
	                allowBlank    : false,
	                name          : 'CDNIVEL',
	                valueField    : 'key',
	                displayField  : 'value',
	                forceSelection: true,
	                anyMatch      : true,
                    queryMode     : 'local',
	                store         : new nivelesSucStore()
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnUsandoEditor(value,record,colIndex,this); //this -> sucursalesGrid
	            }
	      },
          { header     : 'Es de Apoyo' , dataIndex : 'SWAPOYO', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWAPOYO'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      },
          { header     : 'Activa' ,dataIndex : 'SWACTIVA', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWACTIVA'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      }
		],
        tbar: [{
            icon    : '${ctx}/resources/fam3icons/icons/add.png',
            text    : 'Agregar Sucursal',
            handler : function()
            {
            	
            	var windowAgregarSuc;
                
                var panelAgregarSuc = Ext.create('Ext.form.Panel', {
                    url: _UrlAgregaConfSucursal,
                    border: false,
                    bodyStyle:'padding:15px 0px 10px 70px;',
                    items    : [{
                        xtype         : 'combobox',
                        name          : 'params.CDUNIECO',
                        fieldLabel    : 'Sucursal',
                        valueField    : 'key',
                        displayField  : 'value',
                        forceSelection: true,
                        store         : new sucursalesStore(),
                        allowBlank    : false,
                        anyMatch      : true,
                        queryMode     : 'local'
                    },{
    	                xtype         : 'combobox',
    	                allowBlank    : false,
    	                name          : 'params.CDUNIZON',
    	                fieldLabel    : 'Zona',
    	                valueField    : 'key',
    	                displayField  : 'value',
    	                forceSelection: true,
    	                anyMatch      : true,
                        queryMode     : 'local',
    	                store         : new zonasSucStore(),
    	                allowBlank    : false
    	            },{
    	                xtype         : 'numberfield',
    	                fieldLabel    : 'Capacidad',
    	                allowBlank    : false,
    	                name          : 'params.NMCAPACI',
    	               	maxValue      : 999,
    	               	allowBlank    : false
    	            },{
    	                xtype         : 'combobox',
    	                allowBlank    : false,
    	                name          : 'params.CDNIVEL',
    	                fieldLabel    : 'Nivel',
    	                valueField    : 'key',
    	                displayField  : 'value',
    	                forceSelection: true,
    	                anyMatch      : true,
                        queryMode     : 'local',
    	                store         : new nivelesSucStore(),
    	                allowBlank    : false
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWAPOYO',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsApoyo',
    	                fieldLabel    : '&iquest;Es de Apoyo?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarSuc.down('[name=params.SWAPOYO]').setValue('S');
    	                		}else{
    	                			panelAgregarSuc.down('[name=params.SWAPOYO]').setValue('N');
    	                		}
    	                	}
    	                }
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWACTIVA',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsActiva',
    	                fieldLabel    : '&iquest; Est\u00e1 Activa?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarSuc.down('[name=params.SWACTIVA]').setValue('S');
    	                		}else{
    	                			panelAgregarSuc.down('[name=params.SWACTIVA]').setValue('N');
    	                		}
    	                	}
    	                }
    	            }],
                    buttonAlign: 'center',
                    buttons: [{
                        text: 'Agregar',
                        icon    : '${ctx}/resources/fam3icons/icons/disk.png',
                        handler: function(btn, e) {
                            var form = this.up('form').getForm();
                            if (form.isValid()) {
                                
                                Ext.Msg.show({
                                    title: 'Confirmar acci&oacute;n',
                                    msg: '&iquest;Est\u00e1 seguro que desea agregar esta sucursal para ser considerada en el despachador?',
                                    buttons: Ext.Msg.YESNO,
                                    fn: function(buttonId, text, opt) {
                                        if(buttonId == 'yes') {
                                        	
                                            form.submit({
                                                waitMsg:'Guardando...',                        
                                                failure: function(form, action) {
                                                    showMessage('Error', action.result.message, Ext.Msg.OK, Ext.Msg.ERROR);
                                                },
                                                success: function(form, action) {
                                                    form.reset();
                                                    windowAgregarSuc.close();
                                                    mensajeCorrecto('\u00C9xito', 'La sucursal se agreg\u00f3 correctamente.');
                                                }
                                            });
                                        }
                                    },
                                    animateTarget: btn,
                                    icon: Ext.Msg.QUESTION
                                });
                            } else {
                                Ext.Msg.show({
                                    title: 'Aviso',
                                    msg: 'Complete la informaci&oacute;n requerida',
                                    buttons: Ext.Msg.OK,
                                    animateTarget: btn,
                                    icon: Ext.Msg.WARNING
                                });
                            }
                        }
                    },
                    {
                            text: 'Cancelar',
                            icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
                            handler: function(btn, e) {
                                windowAgregarSuc.close();
                            }
                    }]
                });
                
                windowAgregarSuc = Ext.create('Ext.window.Window',
                        {
                            title        : 'Agregar sucursal a la configuraci\u00f3n'
                            ,modal       : true
                            ,buttonAlign : 'center'
                            ,width       : 450
                            ,height      : 280
                            ,autoScroll  : true
                            ,items: [panelAgregarSuc]
                        }).show();
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
            text    : 'Eliminar Sucursal',
            handler : function(btn)
            {
            	if (sucursalesGrid.getSelectionModel().hasSelection()) {
                    
                    Ext.Msg.show({
                        title: 'Confirmar acci&oacute;n',
                        msg: '&iquest;Est\u00e1 seguro que desea Eliminar esta sucursal para dejar ser considerada en el despachador?',
                        buttons: Ext.Msg.YESNO,
                        fn: function(buttonId, text, opt) {
                            if(buttonId == 'yes') {
                            	
                            	var record = sucursalesGrid.getSelectionModel().getLastSelected();
                            		
                                var datosSucursal = {
                                   	'params.CDUNIECO' : String(record.data.CDUNIECO),
                                   	'params.CDUNIZON' : String(record.data.CDUNIZON),
                                   	'params.NMCAPACI' : String(record.data.NMCAPACI),
                                   	'params.CDNIVEL'  : String(record.data.CDNIVEL),
                                   	'params.SWAPOYO'  : record.data.SWAPOYO == true? 'S':'N',
                                   	'params.SWACTIVA' : record.data.SWACTIVA== true? 'S':'N'
                                 }
                                
                                debug('Sucursal a Eliminar: ',datosSucursal);
                                
                                var maskGuarda = _maskLocal('Eliminando...');
                                
                                Ext.Ajax.request({
                                    url: _UrlEliminaConfSucursal,
                                    params   : datosSucursal,
                                    success  : function(response, options){
                                   	 maskGuarda.close();
                                        var json = Ext.decode(response.responseText);
                                        if(json.success){
                                       	 	confSucursalesStore.reload();
                                       		mensajeCorrecto('\u00C9xito', 'La sucursal se elimin\u00f3 correctamente.');
                                        }else{
                                            mensajeError(json.message);
                                        }
                                    }
                                    ,failure  : function(response, options){
                                   	 maskGuarda.close();
                                        var json = Ext.decode(response.responseText);
                                        mensajeError(json.message);
                                    }
                                });
                                
                            }
                        },
                        animateTarget: btn,
                        icon: Ext.Msg.QUESTION
                    });
                } else {
                    Ext.Msg.show({
                        title: 'Aviso',
                        msg: 'Seleccione un registro a eliminar',
                        buttons: Ext.Msg.OK,
                        animateTarget: btn,
                        icon: Ext.Msg.WARNING
                    });
                }
            	
            }
        }],
        selType: 'rowmodel',
        plugins: [
            editorPluginSucursales
        ],
        buttonAlign: 'center',
        buttons: [{
            icon    : '${ctx}/resources/fam3icons/icons/disk.png',
            text    : 'Guardar Cambios Sucursales',
            handler : function(btn, e){
            	
            	if(editorPluginSucursales.editing){
            		mensajeWarning('Antes de guardar primero finalice la edicion de las sucursales.');
            		return;
            	}
                Ext.Msg.show({
                     title: 'Aviso',
                     msg: '&iquest;Est\u00e1 seguro que desea guardar estos cambios?',
                     buttons: Ext.Msg.YESNO,
                     fn: function(buttonId, text, opt) {
                         if(buttonId == 'yes') {
                             
                             var updateList = [];
                             confSucursalesStore.getUpdatedRecords().forEach(function(record,index,arr){
                                 var datosSucursal = {
                                	CDUNIECO : String(record.data.CDUNIECO),
                                	CDUNIZON : String(record.data.CDUNIZON),
                                	NMCAPACI : String(record.data.NMCAPACI),
                                	CDNIVEL  : String(record.data.CDNIVEL),
                                	SWAPOYO  : record.data.SWAPOYO == true? 'S':'N',
                                	SWACTIVA : record.data.SWACTIVA== true? 'S':'N'
                                 }
                            	 updateList.push(datosSucursal);
                             });
                             
                             if(updateList.length <= 0){
                            	 mensajeWarning('No hay cambios que guardar.');
                            	 return;
                             }
                             
                             debug('Lista de Sucursales a guardar: ',updateList);
                             
                             var maskGuarda = _maskLocal('Guardando...');
                             
                             Ext.Ajax.request({
                                 url: _UrlActualizaConfSucursales,
                                 jsonData : {
                                     'list' : updateList
                                 },
                                 success  : function(response, options){
                                	 maskGuarda.close();
                                     var json = Ext.decode(response.responseText);
                                     if(json.success){
                                    	 confSucursalesStore.reload();
                                         mensajeCorrecto('Aviso','Se ha guardado correctamente.');
                                     }else{
                                         mensajeError(json.message);
                                     }
                                 }
                                 ,failure  : function(response, options){
                                	 maskGuarda.close();
                                     var json = Ext.decode(response.responseText);
                                     mensajeError(json.message);
                                 }
                             });
                         }
                     },
                     animateTarget: btn,
                     icon: Ext.Msg.QUESTION
             });
                
            }
        }]
    });
		
	var panelBusqSuc = Ext.create('Ext.form.Panel',{
		title: 'B\u00fasqueda de Sucursales',
		defaults : {
			style : 'margin : 6px 6px 8px 25px;'
		},
	    items :
	    [{
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdunieco',
            fieldLabel    : 'Sucursal',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new sucursalesStore(),
            listeners : {
            	select: function(){
                	var zona     = panelBusqSuc.down('[name=params.cdunizon]');
                	var nivel    = panelBusqSuc.down('[name=params.cdnivel]');
                	zona.clearValue();
                	nivel.clearValue();
            	}
            }
        },
	    {
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdunizon',
            fieldLabel    : 'Zona',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new zonasSucStore(),
            listeners : {
            	select: function(){
            		var sucursal = panelBusqSuc.down('[name=params.cdunieco]');
            		sucursal.clearValue();
            	}
            }
        },
	    {
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdnivel',
            fieldLabel    : 'Nivel',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new nivelesSucStore(),
            listeners : {
            	select: function(){
            		var sucursal = panelBusqSuc.down('[name=params.cdunieco]');
            		sucursal.clearValue();
            	}
            }
        }
        ]
	    ,buttonAlign: 'center'
        ,buttons       :
            [
                {
                    text     : 'Buscar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                    ,handler : function()
                    {
                    	var sucursal = panelBusqSuc.down('[name=params.cdunieco]');
                    	var zona     = panelBusqSuc.down('[name=params.cdunizon]');
                    	var nivel    = panelBusqSuc.down('[name=params.cdnivel]');
                    	
                    	
                        if(!Ext.isEmpty(sucursal.getValue()) || !Ext.isEmpty(zona.getValue()) || !Ext.isEmpty(nivel.getValue()) )
                        {
                        	sucursalesGrid.setLoading(true);	
                        	confSucursalesStore.load({
                        		params:{
                        			'params.cdunieco': sucursal.getValue(),
                        			'params.cdunizon': zona.getValue(),
                        			'params.cdnivel' : nivel.getValue()
                        		},
                        		callback: function(records, operation, success){
                        			sucursalesGrid.setLoading(false);		
                        			
                        			if(!success){
                        				mensajeError('Error al cargar sucursales.')
                        			}
                        		}
                        	})
                        }
                        else
                        {
                            Ext.Msg.show(
                            {
                                title    : 'Aviso.'
                                ,icon    : Ext.Msg.WARNING
                                ,msg     : 'Seleccione almenos uno de los filtros para buscar.'
                                ,buttons : Ext.Msg.OK
                            });
                        }
                    }
                },
                {
                    text     : 'Limpiar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                    ,handler : function()
                    {
                        this.up().up().getForm().reset();
                    }
                }
            ]
	});
	
///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////    PARA SEGUNDA PARTE DE LA PANTALLA /////////////////////////////////
/////////////////////////
	
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	//Modelo de configuracion de Permisos
	
	Ext.define('modelGridPermisos',
	{
		extend : 'Ext.data.Model'
		,fields :
		['CDTIPFLU','CDFLUJOMC','CDRAMO','CDTIPSIT','DSTIPFLU','DSFLUJOMC','DSRAMO','DSTIPSIT',{name: 'SWMATEMI', type: 'boolean'},{name: 'SWMATSUS', type: 'boolean'},{name: 'SWSUCPRI', type: 'boolean'},{name: 'SWSUCSEC', type: 'boolean'},{name: 'SWSUCOFI', type: 'boolean'},'COMMENTS']
	});
	
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	
	var editorPluginPermisos = Ext.create('Ext.grid.plugin.RowEditing', {
    	autoCancel : false,
    	dirtyText  : 'Necesita Aceptar o Cancelar sus cambios',
        clicksToEdit: 2
    });
	
	Ext.define('tiposFlujoStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
				model     : 'Generic',
		        autoLoad  : true,
		        proxy     : {
		            type        : 'ajax'
		            ,url        : _URL_CARGA_CATALOGO
		            ,extraParams: {catalogo: _CAT_TIPO_FLUJOS, 'params.agrupamc' : 'PRINCIPAL'}
		            ,reader     :
		            {
		                type  : 'json'
		                ,root : 'lista'
		            }
		        }
		    }, cfg) ]);
		}
	});
	
	Ext.define('flujosMCStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
		        autoLoad : true
		        ,model   : 'Generic'
		        ,proxy   :
		        {
		            type        : 'ajax'
		                ,url        : _URL_CARGA_CATALOGO
		                ,extraParams: {catalogo:_CAT_FLUJOS_MC, 'params.swfinal' : 'S'}
		                ,reader     :
		                {
		                    type  : 'json'
		                    ,root : 'lista'
		                }
		        }
		    }, cfg) ]);
		}
	});
	
	Ext.define('ramosStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
		        autoLoad : true
		        ,model   : 'Generic'
		        ,proxy   :
		        {
		            type        : 'ajax'
		                ,url        : _URL_CARGA_CATALOGO
		                ,extraParams: {catalogo:_CAT_RAMOS,'params.aniadeComodinTodos' : 'S'}
		                ,reader     :
		                {
		                    type  : 'json'
		                    ,root : 'lista'
		                }
		        }
		    }, cfg) ]);
		}
	});
	
	Ext.define('subramosStore', {
		extend : 'Ext.data.Store',
		constructor : function(cfg) {
			var me = this;
			cfg = cfg || {};
			me.callParent([ Ext.apply({
		        autoLoad : true
		        ,model   : 'Generic'
		        ,proxy   :
		        {
		            type        : 'ajax'
		                ,url        : _URL_CARGA_CATALOGO
		                ,extraParams: {catalogo:_CAT_SUBRAMOS,'params.aniadeComodinTodos' : 'S'}
		                ,reader     :
		                {
		                    type  : 'json'
		                    ,root : 'lista'
		                }
		        }
		    }, cfg) ]);
		}
	});

	
	var confPermisosStore = Ext.create('Ext.data.Store',
    {
		pageSize : 20
        ,model   : 'modelGridPermisos'
        ,proxy   :
        {
        	type        : 'ajax'
            ,url        : _UrlConsultaConfPermisos
            ,reader     :
            {
                type  : 'json'
                ,root : 'list'
            }
        }
    });
	
	/*////////////////*/
	////// stores //////
	////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	
	
	var permisosGrid = Ext.create('Ext.grid.Panel',
	    {
    	title : 'Permisos Configurados. -> De Doble Clic para editar'
    	,height : 360
    	,store : confPermisosStore
    	,columns :
    	[ { header     : 'Tipo Flujo' , dataIndex : 'DSTIPFLU', flex: 2},
    	  { header     : 'FlujoMC'    , dataIndex : 'DSFLUJOMC', flex: 2},
          { header     : 'Producto'   , dataIndex : 'DSRAMO', flex: 2},
          { header     : 'Subramo'    , dataIndex : 'DSTIPSIT', flex: 2},
          { header     : 'Emisor Matriz' , dataIndex : 'SWMATEMI', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWMATEMI'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      },
          { header     : 'Suscriptor Matriz' ,dataIndex : 'SWMATSUS', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWMATSUS'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      },
          { header     : 'Suscursal Primaria' ,dataIndex : 'SWSUCPRI', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWSUCPRI'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      },
          { header     : 'Suscursal Secundaria' ,dataIndex : 'SWSUCSEC', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWSUCSEC'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      },
          { header     : 'Sucursal Oficina' ,dataIndex : 'SWSUCOFI', flex: 1,
	  			editor : {
	                xtype         : 'checkbox',
	                name          : 'SWSUCOFI'
	            },
	            renderer: function(value,metaData,record,rowIndex,colIndex,store){
	            	return rendererColumnCheck(value);
	            }
	      },
          { header     : 'Comentarios' ,dataIndex : 'COMMENTS', flex: 1,
	  			editor : {
	                xtype         : 'textfield',
	                name          : 'COMMENTS',
	                maxLength     : 250,
	                maxLengthText : 'M\u00e1ximo 250 caracteres'
	            }
	      }
		],
        tbar: [{
            icon    : '${ctx}/resources/fam3icons/icons/add.png',
            text    : 'Agregar Permisos',
            handler : function()
            {
            	var windowAgregarPer;
                
                var panelAgregarPer = Ext.create('Ext.form.Panel', {
                    url: _UrlAgregaConfPermiso,
                    border: false,
                    bodyStyle:'padding:15px 0px 10px 70px;',
                    items    : [{
                        xtype         : 'combobox',
                        name          : 'params.CDTIPFLU',
                        fieldLabel    : 'Tipo Flujo',
                        valueField    : 'key',
                        displayField  : 'value',
                        forceSelection: true,
                        store         : new tiposFlujoStore(),
                        allowBlank    : false,
                        anyMatch      : true,
                        queryMode     : 'local',
                        listeners: {
                        	select: function(combo,records){
                            	var flujoCombo = panelAgregarPer.down('[name=params.CDFLUJOMC]');
                            	flujoCombo.getStore().load({
                            		params:{
                            			'params.idPadre': records[0].get('key')
                            		}
                            	});
                        	}
                        }
                    },{
    	                xtype         : 'combobox',
    	                allowBlank    : false,
    	                name          : 'params.CDFLUJOMC',
    	                fieldLabel    : 'Flujo MC',
    	                valueField    : 'key',
    	                displayField  : 'value',
    	                forceSelection: true,
    	                anyMatch      : true,
                        queryMode     : 'local',
    	                store         : new flujosMCStore(),
    	                allowBlank    : false
    	            },{
    	                xtype         : 'combobox',
    	                allowBlank    : false,
    	                name          : 'params.CDRAMO',
    	                fieldLabel    : 'Ramo',
    	                valueField    : 'key',
    	                displayField  : 'value',
    	                forceSelection: true,
    	                anyMatch      : true,
                        queryMode     : 'local',
    	                store         : new ramosStore(),
    	                allowBlank    : false,
                        listeners: {
                        	select: function(combo,records){
                            	var subramosCombo = panelAgregarPer.down('[name=params.CDTIPSIT]');
                            	subramosCombo.getStore().load({
                            		params:{
                            			'params.idPadre': records[0].get('key')
                            		}
                            	});
                        	}
                        }
    	            },{
    	                xtype         : 'combobox',
    	                allowBlank    : false,
    	                name          : 'params.CDTIPSIT',
    	                fieldLabel    : 'Subramo',
    	                valueField    : 'key',
    	                displayField  : 'value',
    	                forceSelection: true,
    	                anyMatch      : true,
                        queryMode     : 'local',
    	                store         : new subramosStore(),
    	                allowBlank    : false
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWMATEMI',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsSWMATEMI',
    	                labelWidth    : 200,
    	                fieldLabel    : '&iquest;Permiso Emisor Matriz?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarPer.down('[name=params.SWMATEMI]').setValue('S');
    	                		}else{
    	                			panelAgregarPer.down('[name=params.SWMATEMI]').setValue('N');
    	                		}
    	                	}
    	                }
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWMATSUS',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsSWMATSUS',
    	                labelWidth    : 200,
    	                fieldLabel    : '&iquest;Permiso Suscriptor Matriz?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarPer.down('[name=params.SWMATSUS]').setValue('S');
    	                		}else{
    	                			panelAgregarPer.down('[name=params.SWMATSUS]').setValue('N');
    	                		}
    	                	}
    	                }
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWSUCPRI',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsSWSUCPRI',
    	                labelWidth    : 200,
    	                fieldLabel    : '&iquest;Permiso Suscursal Primaria?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarPer.down('[name=params.SWSUCPRI]').setValue('S');
    	                		}else{
    	                			panelAgregarPer.down('[name=params.SWSUCPRI]').setValue('N');
    	                		}
    	                	}
    	                }
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWSUCSEC',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsSWSUCSEC',
    	                labelWidth    : 200,
    	                fieldLabel    : '&iquest;Permiso Suscursal Secundaria?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarPer.down('[name=params.SWSUCSEC]').setValue('S');
    	                		}else{
    	                			panelAgregarPer.down('[name=params.SWSUCSEC]').setValue('N');
    	                		}
    	                	}
    	                }
    	            },{
                        xtype         : 'hidden',
                        name          : 'params.SWSUCOFI',
                        value         : 'N'
                    },{
    	                xtype         : 'checkbox',
    	                name          : 'chEsSWSUCOFI',
    	                labelWidth    : 200,
    	                fieldLabel    : '&iquest;Permiso Sucursal Oficina?',
    	                listeners: {
    	                	change: function(chk,newValue){
    	                		if(newValue ==  true){
    	                			panelAgregarPer.down('[name=params.SWSUCOFI]').setValue('S');
    	                		}else{
    	                			panelAgregarPer.down('[name=params.SWSUCOFI]').setValue('N');
    	                		}
    	                	}
    	                }
    	            },{
    	                xtype         : 'textfield',
    	                name          : 'params.COMMENTS',
    	                width         : 270,
    	                fieldLabel    : 'Comentarios'
    	            }],
                    buttonAlign: 'center',
                    buttons: [{
                        text: 'Agregar',
                        icon    : '${ctx}/resources/fam3icons/icons/disk.png',
                        handler: function(btn, e) {
                            var form = this.up('form').getForm();
                            if (form.isValid()) {
                                
                                Ext.Msg.show({
                                    title: 'Confirmar acci&oacute;n',
                                    msg: '&iquest;Est\u00e1 seguro que desea agregar este Permiso para ser considerado en el despachador?',
                                    buttons: Ext.Msg.YESNO,
                                    fn: function(buttonId, text, opt) {
                                        if(buttonId == 'yes') {
                                        	
                                            form.submit({
                                                waitMsg:'Guardando...',                        
                                                failure: function(form, action) {
                                                    showMessage('Error', action.result.message, Ext.Msg.OK, Ext.Msg.ERROR);
                                                },
                                                success: function(form, action) {
                                                    form.reset();
                                                    windowAgregarPer.close();
                                                    mensajeCorrecto('\u00C9xito', 'Los Permisos se agregaron correctamente.');
                                                }
                                            });
                                        }
                                    },
                                    animateTarget: btn,
                                    icon: Ext.Msg.QUESTION
                                });
                            } else {
                                Ext.Msg.show({
                                    title: 'Aviso',
                                    msg: 'Complete la informaci&oacute;n requerida',
                                    buttons: Ext.Msg.OK,
                                    animateTarget: btn,
                                    icon: Ext.Msg.WARNING
                                });
                            }
                        }
                    },
                    {
                            text: 'Cancelar',
                            icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
                            handler: function(btn, e) {
                                windowAgregarPer.close();
                            }
                    }]
                });
                
                windowAgregarPer = Ext.create('Ext.window.Window',
                        {
                            title        : 'Agregar permisos a la configuraci\u00f3n'
                            ,modal       : true
                            ,buttonAlign : 'center'
                            ,width       : 450
                            ,height      : 400
                            ,autoScroll  : true
                            ,items: [panelAgregarPer]
                        }).show();
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
            text    : 'Eliminar Permisos',
            handler : function(btn)
            {
            	if (permisosGrid.getSelectionModel().hasSelection()) {
            		
            		Ext.Msg.show({
                        title: 'Confirmar acci&oacute;n',
                        msg: '&iquest;Est\u00e1 seguro que desea Eliminar este permiso para dejar ser considerado en el despachador?',
                        buttons: Ext.Msg.YESNO,
                        fn: function(buttonId, text, opt) {
                            if(buttonId == 'yes') {
                            	
            	                var record = permisosGrid.getSelectionModel().getLastSelected();

            	                var datosPermisos = {
            	                	'params.CDTIPFLU'  : String(record.data.CDTIPFLU),
           	                   		'params.CDFLUJOMC' : String(record.data.CDFLUJOMC),
           	                   		'params.CDRAMO'    : String(record.data.CDRAMO),
           	                   		'params.CDTIPSIT'  : String(record.data.CDTIPSIT),
           	                   		'params.SWMATEMI'  : record.data.SWMATEMI == true? 'S':'N',
           	                   		'params.SWMATSUS' : record.data.SWMATSUS== true? 'S':'N',
           	                 		'params.SWSUCPRI' : record.data.SWSUCPRI== true? 'S':'N',
           	             			'params.SWSUCSEC' : record.data.SWSUCSEC== true? 'S':'N',
           	           				'params.SWSUCOFI' : record.data.SWSUCOFI== true? 'S':'N',
           	        				'params.COMMENTS' : String(record.data.COMMENTS)
           	                    }
            	               	
            	                debug('Permiso a Eliminar: ',datosPermisos);
            	                
            	                var maskGuarda = _maskLocal('Eliminando...');
            	                
            	                Ext.Ajax.request({
            	                    url: _UrlEliminaConfPermiso,
            	                    params :  datosPermisos,
            	                    success  : function(response, options){
            	                   	 maskGuarda.close();
            	                        var json = Ext.decode(response.responseText);
            	                        if(json.success){
            	                       		confPermisosStore.reload();
            	                      	 	mensajeCorrecto('\u00C9xito', 'La permisos se eliminaron correctamente.');
            	                        }else{
            	                            mensajeError(json.message);
            	                        }
            	                    }
            	                    ,failure  : function(response, options){
            	                   	 maskGuarda.close();
            	                        var json = Ext.decode(response.responseText);
            	                        mensajeError(json.message);
            	                    }
            	                });
                            }
                        },
                        animateTarget: btn,
                        icon: Ext.Msg.QUESTION
                    });
	            	
            	} else {
                    Ext.Msg.show({
                        title: 'Aviso',
                        msg: 'Seleccione un registro a eliminar',
                        buttons: Ext.Msg.OK,
                        animateTarget: btn,
                        icon: Ext.Msg.WARNING
                    });
                }
        }
        }],
        selType: 'rowmodel',
        plugins: [
            editorPluginPermisos
        ],
        buttonAlign: 'center',
        buttons: [{
            icon    : '${ctx}/resources/fam3icons/icons/disk.png',
            text    : 'Guardar Cambios Permisos',
            handler : function(btn, e){
            	
            	if(editorPluginPermisos.editing){
            		mensajeWarning('Antes de guardar primero finalice la edicion de los permisos.');
            		return;
            	}
                Ext.Msg.show({
                     title: 'Aviso',
                     msg: '&iquest;Est\u00e1 seguro que desea guardar estos cambios?',
                     buttons: Ext.Msg.YESNO,
                     fn: function(buttonId, text, opt) {
                         if(buttonId == 'yes') {
                             
                             var updateList = [];
                             confPermisosStore.getUpdatedRecords().forEach(function(record,index,arr){
                                 var datosPermisos = {
										CDTIPFLU  : String(record.data.CDTIPFLU),
                                		CDFLUJOMC : String(record.data.CDFLUJOMC),
                                		CDRAMO    : String(record.data.CDRAMO),
                                		CDTIPSIT  : String(record.data.CDTIPSIT),
                                		SWMATEMI  : record.data.SWMATEMI == true? 'S':'N',
                                		SWMATSUS : record.data.SWMATSUS== true? 'S':'N',
                                		SWSUCPRI : record.data.SWSUCPRI== true? 'S':'N',
                                		SWSUCSEC : record.data.SWSUCSEC== true? 'S':'N',
                                		SWSUCOFI : record.data.SWSUCOFI== true? 'S':'N',
                                		COMMENTS : String(record.data.COMMENTS)
                                 }
                            	 updateList.push(datosPermisos);
                             });
                             
                             if(updateList.length <= 0){
                            	 mensajeWarning('No hay cambios que guardar.');
                            	 return;
                             }
                             
                             debug('Lista de permisos a guardar: ',updateList);
                             
                             var maskGuarda = _maskLocal('Guardando...');
                             
                             Ext.Ajax.request({
                                 url: _UrlActualizaConfPermisos,
                                 jsonData : {
                                     'list' : updateList
                                 },
                                 success  : function(response, options){
                                	 maskGuarda.close();
                                     var json = Ext.decode(response.responseText);
                                     if(json.success){
                                    	 confPermisosStore.reload();
                                         mensajeCorrecto('Aviso','Se ha guardado correctamente.');
                                     }else{
                                         mensajeError(json.message);
                                     }
                                 }
                                 ,failure  : function(response, options){
                                	 maskGuarda.close();
                                     var json = Ext.decode(response.responseText);
                                     mensajeError(json.message);
                                 }
                             });
                         }
                     },
                     animateTarget: btn,
                     icon: Ext.Msg.QUESTION
             });
                
            }
        }]
    });
		
	var panelBusqPer = Ext.create('Ext.form.Panel',{
		title: 'B\u00fasqueda de Permisos',
		defaults : {
			style : 'margin : 6px 6px 8px 25px;'
		},
	    items :
	    [{
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdtipflu',
            fieldLabel    : 'Tipo Flujo',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new tiposFlujoStore(),
            listeners : {
            	select: function(combo,records){
                	var flujoCombo = panelBusqPer.down('[name=params.cdflujomc]');
                	flujoCombo.getStore().load({
                		params:{
                			'params.idPadre': records[0].get('key')
                		}
                	});
            	}
            }
        },
	    {
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdflujomc',
            fieldLabel    : 'Flujo MC',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new flujosMCStore(),
            listeners : {
            	select: function(){
            		/* var sucursal = panelBusqPer.down('[name=params.cdunieco]');
            		sucursal.reset(); */
            	}
            }
        },
	    {
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdramo',
            fieldLabel    : 'Ramo',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new ramosStore(),
            listeners: {
            	select: function(combo,records){
                	var subramosCombo = panelBusqPer.down('[name=params.cdtipsit]');
                	subramosCombo.getStore().load({
                		params:{
                			'params.idPadre': (records[0].get('key') == '-1')?'' : records[0].get('key')
                		}
                	});
            	}
            }
        },
	    {
            xtype         : 'combobox',
            labelWidth    : 60,
            width         : 300,
            name          : 'params.cdtipsit',
            fieldLabel    : 'Subramo',
            valueField    : 'key',
            displayField  : 'value',
            forceSelection: true,
            anyMatch      : true,
            queryMode     : 'local',
            store         : new subramosStore(),
            listeners : {
            	select: function(){
            		/* var sucursal = panelBusqPer.down('[name=params.cdunieco]');
            		sucursal.reset(); */
            	}
            }
        }
        ]
	    ,buttonAlign: 'center'
        ,buttons       :
            [
                {
                    text     : 'Buscar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                    ,handler : function()
                    {
                    	var tipoFlujo = panelBusqPer.down('[name=params.cdtipflu]');
                    	var flujoMC   = panelBusqPer.down('[name=params.cdflujomc]');
                    	var ramo      = panelBusqPer.down('[name=params.cdramo]');
                    	var subramo   = panelBusqPer.down('[name=params.cdtipsit]');
                    	
                        if(!Ext.isEmpty(tipoFlujo.getValue()) || !Ext.isEmpty(flujoMC.getValue()) || !Ext.isEmpty(ramo.getValue()) || !Ext.isEmpty(subramo.getValue()) )
                        {
                        	permisosGrid.setLoading(true);	
                        	confPermisosStore.load({
                        		params:{
                        			'params.cdtipflu' : tipoFlujo.getValue(),
                        			'params.cdflujomc': flujoMC.getValue(),
                        			'params.cdramo'   : ramo.getValue(),
                        			'params.cdtipsit' : subramo.getValue()
                        		},
                        		callback: function(records, operation, success){
                        			permisosGrid.setLoading(false);		
                        			
                        			if(!success){
                        				mensajeError('Error al cargar permisos.')
                        			}
                        		}
                        	})
                        }
                        else
                        {
                            Ext.Msg.show(
                            {
                                title    : 'Aviso.'
                                ,icon    : Ext.Msg.WARNING
                                ,msg     : 'Seleccione almenos uno de los filtros para buscar.'
                                ,buttons : Ext.Msg.OK
                            });
                        }
                    }
                },
                {
                    text     : 'Limpiar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                    ,handler : function()
                    {
                        this.up().up().getForm().reset();
                    }
                }
            ]
	});
	
	var panelPrincipalSuc = Ext.create('Ext.panel.Panel',{
		title: 'Administraci\u00f3n de sucursales',
		titleAlign: 'center',
		defaults : {
			style : 'margin : 3px;'
		}
	    ,renderTo : 'mainDivConfSucurTrabajo'
	    ,items :
	    [panelBusqSuc,sucursalesGrid]
	});
	
	var panelPrincipalPer = Ext.create('Ext.panel.Panel',{
		title: 'Administraci\u00f3n de permisos MC.',
		titleAlign: 'center',
		defaults : {
			style : 'margin : 3px;'
		}
	    //,renderTo : 'mainDivConfSucurTrabajo'
	    ,items :
	    [panelBusqPer,permisosGrid]
	});
	
	var panelPrincipalPer = Ext.create('Ext.panel.Panel',{
		title: 'Administraci\u00f3n de permisos MC.',
		titleAlign: 'center',
		defaults : {
			style : 'margin : 3px;'
		}
	    //,renderTo : 'mainDivConfSucurTrabajo'
	    ,items :
	    [panelBusqPer,permisosGrid]
	});
	
	Ext.create('Ext.panel.Panel', {
	    defaults: {
	        bodyStyle: 'padding:3px'
	    },
	    layout: {
	        type: 'accordion'
	    },
	    items: [panelPrincipalSuc,panelPrincipalPer],
	    renderTo: 'mainDivConfSucurTrabajo'
	});
	
	
	
	function rendererColumnUsandoEditor(value, record, columnIndex, grid){
		var nuevoValor = value;
		
		var comboColumnaFila = grid.columns[columnIndex].getEditor(record);
		var recordSel = comboColumnaFila.findRecordByValue(value);
       
		if(recordSel){
			nuevoValor=recordSel.get('value');
        }else{
        	
        	nuevoValor = 'Cargando...';
        	
        	var store = comboColumnaFila.getStore();
        	
            if(Ext.isEmpty(store.padreView))
            {
                store.padreView=grid.getView();
                store.on(
                {
                    load : function(me)
                    {
                        me.padreView.refresh();
                    }
                });
            }
        }
		
		
		
		return nuevoValor;
	}

	function rendererColumnCheck(value){
		if(!Ext.isEmpty(value) && value == true ){
			return '<img src="${ctx}/resources/fam3icons/icons/accept.png" data-qtip="Si">';//'Si';
		}else{
			return '<img src="${ctx}/resources/fam3icons/icons/cancel.png" data-qtip="No">';//'No';	
		}	
		
	}
	
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
});
</script>

</head>
<body>
<div id="mainDivConfSucurTrabajo" style="height:600px;"></div>
</body>
</html>