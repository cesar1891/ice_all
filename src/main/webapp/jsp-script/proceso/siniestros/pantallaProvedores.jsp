<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CONTEXT = '${ctx}';
var _url_lista_provedores  =	'<s:url namespace="/siniestros" action="listaProveedores" />';
var _p31_urlPantallaCliente                = '<s:url namespace="/catalogos"  action="includes/personasLoader"            />';

////// urls //////

////// variables //////
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('modeloProveedores',{
		extend:'Ext.data.Model',
		fields:[
			{type:'string',		name:'CDPRESTA'	        },	
			{type:'string',		name:'CDPERSON'	        },
			{type:'string',		name:'PROVEEDOR'		    },
			{type:'string',		name:'ESPECIALIDAD'	        },
			{type:'string',		name:'TIPO'		    },
			{type:'string',		name:'ZONA'        }
        ]
	});
    ////// modelos //////
    
    ////// stores //////
    var storeProveedores= new Ext.data.Store({
		pageSize	: 25
		,model		: 'modeloProveedores'
		,autoLoad	: false
		,proxy		: {
			enablePaging	:	true,
			reader			:	'json',
			type			:	'memory',
			data			:	[]
		}
	});
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        defaults  : { style : 'margin:5px;' }
        ,renderTo : 'divpri'
        ,border   : 0
        ,items    :
        [
        	Ext.create('Ext.form.Panel', {
        	    title: 'B\u00fasqueda',
        	    bodyPadding: 5,
        	   

        	    // The form will submit an AJAX request to this URL when submitted
        	   // url: 'save-form.php',

        	    // Fields will be arranged vertically, stretched to full width
        	    layout: 'column',
        	    defaults: {
        	       // anchor: '100%',
        	        columnWidth: 0.5,
        	        padding : '10 0 0 20'
        	    },

        	    // The fields
        	    defaultType: 'textfield',
        	    items: [
        	    	{
                        xtype       : 'combo',
                        name        : 'params.cdunieco',
                        fieldLabel  : 'PROVEEDOR',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'PROVEEDORES'
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    
                                }
                            }
                        })
        	    	},
        	    	{
                        xtype       : 'combo',
                        name        : 'params.cdunieco',
                        fieldLabel  : 'ESPECIALIDAD',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'TESPECIALIDADES'
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    
                                }
                            }
                        })
        	    	},
        	    	{
                        xtype       : 'combo',
                        name        : 'params.cdunieco',
                        fieldLabel  : 'TIPO PROVEEDOR',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'TIPOPROVEEDOR'
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    
                                }
                            }
                        })
        	    	},
        	    	{
                        xtype       : 'combo',
                        name        : 'params.cdunieco',
                        fieldLabel  : 'ZONA HOSPITALARIA',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'ZONASHOSPITALARIA'
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    
                                }
                            }
                        })
        	    	}
        	    ],

        	    // Reset and Submit buttons
        	    buttons: [{
        	        text: 'Limpiar',
        	        handler: function() {
        	            this.up('form').getForm().reset();
        	        }
        	    }, {
        	        text: 'Buscar',
        	        //formBind: true, //only enabled once the form is valid
        	        //disabled: true,
        	        handler: function() {
        	        	_mask("Cargando...");
                    	
        	        	storeProveedores.removeAll();
						var params = {
							'params.pv_cdpresta_i'   : _fieldByLabel('PROVEEDOR').getValue()
							,'params.pv_idespecialidad_i'    : _fieldByLabel('ESPECIALIDAD').getValue()
							,'params.pv_tipoProveedor_i'  : _fieldByLabel('TIPO PROVEEDOR').getValue()
							,'params.pv_idZonaHosp_i'   : _fieldByLabel('ZONA HOSPITALARIA').getValue()
							
						}; 
						cargaStorePaginadoLocal(storeProveedores, _url_lista_provedores, 'slist1', params, function(options, success, response){
							_unmask();
							if(success){
								
								var jsonResponse = Ext.decode(response.responseText);
								if(jsonResponse.slist1 &&jsonResponse.slist1.length == 0) {
									
									centrarVentanaInterna(showMessage("Aviso", "No existe tr&aacute;mites.", Ext.Msg.OK, Ext.Msg.INFO));
									
									
									return;
								}
								
								debug(jsonResponse);
							}else{
								centrarVentanaInterna(showMessage('Error', 'Error al obtener los datos',Ext.Msg.OK, Ext.Msg.ERROR));
							}
						})
        	        }
        	    }]
        	}),
        	
        	Ext.create('Ext.grid.Panel',{
				id             : 'clausulasGridId'
				,title         : 'Provedores'
				,autoScroll	   : true
				,store         :  storeProveedores
				,titleCollapse : true
				,style         : 'margin:5px'
				,height        : 400
				,columns       : [
					{   xtype: 'actioncolumn',      width: 40,          sortable: false,            menuDisabled: true,
	                    items: [{
	                        icon: _CONTEXT+'/resources/fam3icons/icons/application_edit.png',
	                        tooltip: 'Generar Autorizaci\u00F3n Especial',	//(EGS)
	                        scope: this,
	                        handler: function(view,rI,cI,item,e,record){
	                        	debug(record);
	                        	windowLoader = Ext.create('Ext.window.Window',
	                                    {
	                                        title        : 'CLIENTE'
	                                        ,modal       : true
	                                        ,buttonAlign : 'center'
	                                        ,width       : 900
	                                        ,height      : 480
	                                        ,autoScroll  : true
	                                        ,loader      :
	                                        {
	                                            url       : _p31_urlPantallaCliente
	                                            ,scripts  : true
	                                            ,autoLoad : true
	                                            ,loadMask : true
	                                            ,ajaxOptions: {
	                                                method   : 'POST'
	                                            },
	                                            params: {
	                                            	'smap1.cdperson' :record.get("CDPERSON")
	                                            }
	                                        }
	                                    }).show();
	                        }
	                    }]
	                 },
					{	header     : 'ID PROVEDOR'       ,	dataIndex : 'CDPRESTA',		 width : 200  	},
					{	header     : 'CDPERSON'         ,	dataIndex : 'CDPERSON',		flex : 1, 	hidden   : true	},
					{	header     : 'NOMBRE PROVEEDOR'      , dataIndex : 'PROVEEDOR',    width : 200             },
					{	header     : 'ESPECIALIDAD'         , dataIndex : 'ESPECIALIDAD'      , width : 200				},
					{	header     : 'TIPO PROVEEDOR'      , dataIndex : 'TIPO'    ,	width : 200				},
					{	header     : 'ZONA HOSPITALARIA'       ,	dataIndex : 'ZONA' ,	width : 200				}
					
				],
				bbar     :{
					displayInfo : true,
					store		: storeProveedores,
					xtype		: 'pagingtoolbar'
				}
			})
        	
        ]
    });

    ////// contenido //////
    
    ////// custom //////
    _mask("Cargando...");
                    	
   	storeProveedores.removeAll();
	var params = {
		'params.pv_cdpresta_i'   : _fieldByLabel('PROVEEDOR').getValue()
		,'params.pv_idespecialidad_i'    : _fieldByLabel('ESPECIALIDAD').getValue()
		,'params.pv_tipoProveedor_i'  : _fieldByLabel('TIPO PROVEEDOR').getValue()
		,'params.pv_idZonaHosp_i'   : _fieldByLabel('ZONA HOSPITALARIA').getValue()
		
	}; 
	cargaStorePaginadoLocal(storeProveedores, _url_lista_provedores, 'slist1', params, function(options, success, response){
		_unmask();
		if(success){
			
			var jsonResponse = Ext.decode(response.responseText);
			if(jsonResponse.slist1 &&jsonResponse.slist1.length == 0) {
				
				centrarVentanaInterna(showMessage("Aviso", "No existe tr&aacute;mites.", Ext.Msg.OK, Ext.Msg.INFO));
				
				
				return;
			}
			
			debug(jsonResponse);
		}else{
			centrarVentanaInterna(showMessage('Error', 'Error al obtener los datos',Ext.Msg.OK, Ext.Msg.ERROR));
		}
	})
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
////// funciones //////
</script>
</head>
<body>
<div id="divpri" style="height:800px;border:1px solid #CCCCCC"></div>
</body>
</html>