<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tablas de Apoyo</title>

<!-- UX libraries -->
<script type="text/javascript" src="${ctx}/resources/extjs4/plugins/spreadsheet/ux-all-debug.js"></script>
<!-- Spread library -->
<script type="text/javascript" src="${ctx}/resources/extjs4/plugins/spreadsheet/spread-all-debug.js"></script>


<script>

///////////////////////
////// variables //////
/*///////////////////*/

var _UrlObtieneTablas       = '<s:url namespace="/catalogos"    action="obtieneTablasApoyo" />';
var _UrlEliminarTabla       = '<s:url namespace="/catalogos"    action="eliminaTablaApoyo" />';
var _URL_LOADER_NUEVA_TABLA = '<s:url namespace="/catalogos"    action="includes/agregaTablaApoyo" />';
var _URL_LOADER_DATOS_TABLA = '<s:url namespace="/catalogos"    action="includes/tablaApoyo5claves" />';

var _MSG_SIN_DATOS          = 'No hay datos';
var _MSG_BUSQUEDA_SIN_DATOS = 'No hay datos para la b\u00FAsqueda actual.';

var cdSisRolActivo = '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />';
var cdUserActivo   ='<s:property value="%{#session['USUARIO'].user}" />';




/*///////////////////*/
////// variables //////
///////////////////////

Ext.onReady(function(){
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('gridTablasModel',
	{
		extend : 'Ext.data.Model'
		,fields :
		['NMTABLA','CDTABLA','DSTABLA','OTTIPOTB','OTTIPOTB_DESC','OTTIPOAC','CDTABLJ1','CDTABLJ2','CDTABLJ3','CLNATURA']
	});
	
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	var tablasStore = Ext.create('Ext.data.Store',
    {
		pageSize : 30,
        autoLoad : true
        ,model   : 'gridTablasModel'
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
	var gridTablas = Ext.create('Ext.grid.Panel',
	    {
    	title : 'Tablas de Apoyo'
    	,height : 400
    	,selModel: {
	 		selType: 'checkboxmodel',
	 		allowDeselect: true,
	 		mode: 'SINGLE'
	 	}
    	,store : tablasStore
    	,columns :
    	[ 
    	  { header     : 'OTTIPOTB', dataIndex : 'OTTIPOTB', hidden: true},
    	  { header     : 'N&uacute;mero de Tabla' , dataIndex : 'NMTABLA', flex: 1},
    	  { header     : 'C&oacute;digo de Tabla' , dataIndex : 'CDTABLA', flex: 1},
    	  { header     : 'Descripci&oacute;n de Tabla' , dataIndex : 'DSTABLA', flex: 2},
          { header     : 'Tipo de Tabla' , dataIndex : 'OTTIPOTB_DESC' , flex: 1}
		]
    	,bbar :
        {
            displayInfo : true,
            store       : tablasStore,
            xtype       : 'pagingtoolbar'
        },
        tbar: [{
            icon    : '${ctx}/resources/fam3icons/icons/add.png',
            text    : 'Agregar Tabla',
            disabled: (cdSisRolActivo==RolSistema.ParametrizadorSistemas ||  cdSisRolActivo==RolSistema.Parametrizador) ? false : true,//se evalua habilitar o desabilitar Boton, dependiendo el rol
            handler : function()
            {
            	windowLoader = Ext.create('Ext.window.Window',
                {
                    title        : 'Agregar Tabla de Apoyo'
                    ,modal       : true
                    ,buttonAlign : 'center'
                    ,width       : 900
                    ,height      : 750
                    ,autoScroll  : true
                    ,loader      :
                    {
                        url       : _URL_LOADER_NUEVA_TABLA
                        ,scripts  : true
                        ,autoLoad : true
                        ,loadMask : true
                        ,ajaxOptions: {
                            method   : 'POST'
                        },
                        params: {
                        	'params.edit' : 'N'
                        }
                    }
                }).show();
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
            text    : 'Editar Tabla',
            disabled: (cdSisRolActivo==RolSistema.ParametrizadorSistemas ||  cdSisRolActivo==RolSistema.Parametrizador) ? false : true,//se evalua habilitar o desabilitar Boton, dependiendo el rol
            handler : function()
            {
            	var model =  gridTablas.getSelectionModel();
            	if(model.hasSelection()){
            		var record = model.getLastSelected();
            		
            		var dsusuario = record.get('dsUsuario');
            		var nombre = record.get('dsNombre');
            		var snombre = record.get('dsNombre1');
            		var appat = record.get('dsApellido');
            		var apmat = record.get('dsApellido1');
            		
            		windowLoader = Ext.create('Ext.window.Window',
                            {
                                title        : 'Editar Tabla de Apoyo'
                                ,modal       : true
                                ,buttonAlign : 'center'
                                ,width       : 900
                                ,height      : 750
                                ,autoScroll  : true
                                ,loader      :
                                {
                                    url       : _URL_LOADER_NUEVA_TABLA
                                    ,scripts  : true
                                    ,autoLoad : true
                                    ,loadMask : true
                                    ,ajaxOptions: {
                                        method   : 'POST'
                                    },
                                    params: {
                                    	'params.edit'    : 'S',
                                    	'params.cdtabla' : record.get('CDTABLA'),
                                    	'params.dstabla' : record.get('DSTABLA'),
                                    	'params.nmtabla' : record.get('NMTABLA'),
                                    	'params.tipotab' : record.get('OTTIPOTB'),
                                    	'params.tipoacc' : record.get('OTTIPOAC'),
                                    	'params.catuno'  : record.get('CDTABLJ1'),
                                    	'params.catdos'  : record.get('CDTABLJ2'),
                                    	'params.clavecat': record.get('CDTABLJ3'),
                                    	'params.cdnatura': record.get('CLNATURA')
                                    }
                                }
                            }).show();
            		
            	}else{
            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            }
        },{
            icon    : '${ctx}/resources/fam3icons/icons/delete.png',
            text    : 'Eliminar Tabla',
            disabled: (cdSisRolActivo==RolSistema.ParametrizadorSistemas ||  cdSisRolActivo==RolSistema.Parametrizador) ? false : true,//se evalua habilitar o desabilitar Boton, dependiendo el rol
            scope   : this,
            handler : function (btn, e){
            	var model =  gridTablas.getSelectionModel();
            	if(model.hasSelection()){
            		Ext.Msg.show({
    		            title: 'Aviso',
    		            msg: '&iquest;Esta seguro que desea eliminar esta tabla?',
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		
    		            		Ext.Ajax.request({
            						url: _UrlEliminarTabla,
            						params: {
            					    		'params.PV_CDUSUARI_I'  : model.getLastSelected().get('cdUsuario')
            						},
            						success: function(response, opt) {
            							var jsonRes=Ext.decode(response.responseText);

            							if(jsonRes.success == true){
            								mensajeCorrecto('Aviso','Se ha eliminado la tabla correctamente.');
    										recargagridTablas();        							
                   						}else {
                   							mensajeError('No se pudo eliminar la tabla.');
                   						}
            						},
            						failure: function(){
            							mensajeError('No se pudo eliminar la tabla.');
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
            icon    : '${ctx}/resources/fam3icons/icons/database_add.png',
            text    : 'Consulta y Actualizaci&oacute;n de Valores',
            handler : function()
            {
            	var model =  gridTablas.getSelectionModel();
            	if(model.hasSelection()){
            		var record = model.getLastSelected();
            		
            		windowLoader = Ext.create('Ext.window.Window',
                            {
                                title        : 'Consulta y Actualizaci&oacute;n de Valores'
                                ,modal       : true
                                ,buttonAlign : 'center'
                                ,width       : 950
                                ,height      : 760
                                //,layout: 'fit'
                                //,maximized: true
                                //,autoScroll  : true
                                ,loader      :
                                {
                                    url       : _URL_LOADER_DATOS_TABLA
                                    ,scripts  : true
                                    ,autoLoad : true
                                    ,loadMask : true
                                    ,ajaxOptions: {
                                        method   : 'POST'
                                    },
                                    params: {
                                    	'params.cdtabla' : record.get('CDTABLA'),
                                    	'params.dstabla' : record.get('DSTABLA'),
                                    	'params.nmtabla' : record.get('NMTABLA'),
                                    	'params.tipotab' : record.get('OTTIPOTB')
                                    }
                                }
                            }).show();
            		
            	}else{
            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
            	}
            }
        }]
    });
	
	var panelTablas = Ext.create('Ext.form.Panel',{
		title: 'B&uacute;squeda de Tablas de Apoyo',
		defaults : {
			style : 'margin : 5px;'
		},
	    renderTo : 'mainDivTabs',
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
		    	xtype: 'textfield',
		    	fieldLabel: 'N&uacute;mero Tabla',
		    	labelWidth: 60,
		    	name: 'params.PV_NMTABLA_I'
		    },{
		    	xtype: 'textfield',
		    	fieldLabel: 'C&oacute;digo Tabla',
		    	labelWidth: 60,
		    	name: 'params.PV_CDTABLA_I'
		    },{
		    	xtype: 'textfield',
		    	fieldLabel: 'Descripci&oacute;n Tabla',
		    	labelWidth: 70,
		    	name: 'params.PV_DSTABLA_I'
		    },{
	            xtype         : 'combobox',
	            labelWidth    : 40,
	            //width         : 200,
	            name          : 'params.PV_OTTIPOTB_I',
	            fieldLabel    : 'Tipo Tabla',
	            valueField    : 'key',
	            displayField  : 'value',
	            forceSelection: true,
	            queryMode     : 'local',
	            typeAhead     : true,
	            anyMatch      : true,
	            store         :  Ext.create('Ext.data.Store', {
							     model: 'Generic',
							     data : [
							         {key: '1', value: 'Tabla de 1 Clave'},
							         {key: '5', value: 'Tabla de 5 Claves'}
							     ]
							 })
	        }],
		    buttons:
		    	[	{
	                    text     : 'Limpiar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
	                    ,handler : function()
	                    {
	                    	panelTablas.getForm().reset();
	                    }
	                },
	                {
	                    text     : 'Buscar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
	                    ,handler : function()
	                    {
	                        if(panelTablas.isValid())
	                        {
	                        	recargagridTablas();
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
	    },gridTablas]
	    
	});
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
	recargagridTablas = function(){
		
		cargaStorePaginadoLocal(tablasStore, _UrlObtieneTablas, 'loadList', panelTablas.getValues(), function (options, success, response){
    		if(success){
                var jsonResponse = Ext.decode(response.responseText);
                
                if(!jsonResponse.success) {
                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                }
            }else{
                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
            }
    	}, gridTablas);
	};
	
});
</script>
<!-- Spread library -->
<link type="text/css" rel="stylesheet" href="${ctx}/resources/extjs4/plugins/spreadsheet/css/spread.css" />

</head>
<body>
<div id="mainDivTabs" style="height:650px;"></div>
</body>
</html>