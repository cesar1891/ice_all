<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Carga y Procesamiento de Archivos de Cobranza</title>
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

var recargaGridCobCargada;
var recargaGridRemesaAplicada;

var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _UrlSubirArchivoCobranza    = '<s:url namespace="/cobranza" action="subirArchivoCobranza" />';

var _UrlCobCargada    =  '<s:url namespace="/cobranza"    action="consultaCobranzaAplicada" />';
var _UrlRemesaAplicada = '<s:url namespace="/cobranza"    action="consultaRemesaAplicada" />';

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
	
	Ext.define('gridCobCargadaModel',
			{
				extend : 'Ext.data.Model'
				,fields :
				['CDUNIECO', 'CDRAMO', 'ESTADO', 'NMPOLIZA', 'PTIMPORT']
	});
	
	Ext.define('gridRemesaAplicadaModel',
	{
		extend : 'Ext.data.Model'
		,fields :
		['NMREMESA', 'FEAPLICA', 'CDUNIECO', 'CDRAMO', 'ESTADO', 'NMPOLIZA', 'NMRECIBO', 'PTIMPORT', 'TIPOPAGO' , 'SALDO']
	});
	
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	
	var cobCargadaStore = Ext.create('Ext.data.Store',
    {
		pageSize : 20,
        autoLoad : true
        ,model   : 'gridCobCargadaModel'
        ,proxy   :
        {
            type         : 'memory'
            ,enablePaging : true
            ,reader      : 'json'
            ,data        : []
        }
    });

	var remesaAplicadaStore = Ext.create('Ext.data.Store',
		{
		pageSize : 20,
		autoLoad : true
		,model   : 'gridRemesaAplicadaModel'
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
	
	var gridCobCargada = Ext.create('Ext.grid.Panel',
		    {
	    	title : 'Cobranza Cargada'
	    	,height : 300
	    	,width :  800
	    	,selType: 'checkboxmodel'
	    	,store : cobCargadaStore
	    	,columns :[
	    		{ header     : 'Sucursal'		 	,dataIndex : 'CDUNIECO'	, flex: 1},
				{ header     : 'Producto' 			,dataIndex : 'CDRAMO'	, flex: 1},
				{ header     : 'No. P&oacute;liza' 	,dataIndex : 'NMPOLIZA'	, flex: 1},
				{ header     : 'Importe por Aplicar',dataIndex : 'PTIMPORT'	, flex: 1}
			]
	    	,bbar     :
	        {
	            displayInfo : true,
	            store       : cobCargadaStore,
	            xtype       : 'pagingtoolbar'
	            
	        }
	    });
	
	var gridRemesaAplicada = Ext.create('Ext.grid.Panel',
		    {
	    	title : 'Remesa Aplicada'
	    	,height : 300
	    	,width :  800
	    	,selType: 'checkboxmodel'
	    	,store : remesaAplicadaStore
	    	,columns :
	    	[
	    	 	{ header     : 'Remesa'		 				,dataIndex : 'NMREMESA'	, flex: 1},
	    	 	{ header     : 'Fecha de Aplicaci&oacute;n'	,dataIndex : 'FEAPLICA'	, flex: 2},
	    		{ header     : 'Sucursal'		 			,dataIndex : 'CDUNIECO'	, flex: 1},
				{ header     : 'Producto' 					,dataIndex : 'CDRAMO'	, flex: 1},
				{ header     : 'P&oacute;liza' 				,dataIndex : 'NMPOLIZA'	, flex: 1},
				{ header     : 'Recibo' 					,dataIndex : 'NMRECIBO'	, flex: 1},
				{ header     : 'Importe Aplicado'			,dataIndex : 'PTIMPORT'	, flex: 2},
				{ header     : 'Tipo de Pago'				,dataIndex : 'TIPOPAGO'	, flex: 2},
				{ header     : 'Saldo'						,dataIndex : 'SALDO'	, flex: 2}
			]
	    	,bbar     :
	        {
	            displayInfo : true,
	            store       : remesaAplicadaStore,
	            xtype       : 'pagingtoolbar'
	            
	        }
	});
	
	var panelCobranza = Ext.create('Ext.form.Panel',{
		title: 'Cobranza Masiva',
		defaults : {
			style : 'margin : 5px;'
		}
	    ,renderTo : 'mainDivCobranza'
	    ,items :
	    [{
	    	layout: 'column',
	    	border: false,
	    	html:'<br/><strong>Oficina General de Seguros</strong><br/><br/>Elija la el Archivo de Cobranza a Cargar:'
	     },{
	        xtype: 'filefield',
	        name: 'file',
	        width: 300,
	        msgTarget: 'side',
	        allowBlank: false,
	        buttonText: 'Examinar...',
	        cAccept  : ['txt','csv'],
	        listeners: {
	        	change: function(me, value, opts){
	        		
	        		var indexofPeriod = value.lastIndexOf(".");
                    var uploadedExtension = value.substr(indexofPeriod + 1, value.length - indexofPeriod).toLowerCase();
                    if (!Ext.Array.contains(this.cAccept, uploadedExtension)){
                        Ext.MessageBox.show({
                            title   : 'Error de tipo de archivo',
                            msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                            buttons : Ext.Msg.OK,
                            icon    : Ext.Msg.WARNING
                        });
                        me.reset();
                    }else{
                    	me.up('panel').getDockedItems('toolbar[dock="bottom"]').forEach(function(element, index, array){
    	        			element.disable();
    	        		});
                    }
	        		
	        	}
	        }
	    },{
	    	xtype: 'button',
	    	text : 'Aplicar Cobranza Masiva',
	    	tooltip: 'Aplica la Cobranza Masiva para el Archivo Seleccionado',
	    	icon:_CONTEXT+'/resources/fam3icons/icons/database_go.png',
	    	handler: function(btn){
	    		var form = panelCobranza.getForm();
	            if(form.isValid()){
	                form.submit({
	                    url: _UrlSubirArchivoCobranza,
	                    waitMsg: 'Subiendo Archivo...',
	                    success: function(fp, o) {
	                        mensajeCorrecto('Exito', 'La cobranza se ha cargado correctamente.');
	                        btn.up('panel').getDockedItems('toolbar[dock="bottom"]').forEach(function(element, index, array){
	                        	element.enable();
	    	        		});
	                    },
	                    failure: function(form, action) {
	                		switch (action.failureType) {
	                            case Ext.form.action.Action.CONNECT_FAILURE:
	                        	    Ext.Msg.show({title: 'Error', msg: 'Error de comunicaci&oacute;n', buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
	                                break;
	                            case Ext.form.action.Action.SERVER_INVALID:
	                            case Ext.form.action.Action.LOAD_FAILURE:
	                            	 var msgServer = Ext.isEmpty(action.result.mensajeRespuesta) ? 'Error interno del servidor, consulte a soporte' : action.result.mensajeRespuesta;
	                                 Ext.Msg.show({title: 'Error', msg: msgServer, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
	                                break;
	                        }
	        			}
	                });
	            }
	    	}
	    }],
	    buttonAlign: 'center',
	    buttons: [{
	        text: 'Ver Cobranza Cargada',
	        icon:_CONTEXT+'/resources/fam3icons/icons/database_table.png',
	        handler: function() {
	        	var cobranzaWindow = Ext.create('Ext.window.Window', {
	  	          title: 'Cobranza Cargada',
	  	          modal:true,
	  	          closeAction: 'hide',
	  	          items:[gridCobCargada],
	  	          bodyStyle:'padding:10px;',
	  	          buttons:[
	  	           {
	  	                 text: 'Cerrar',
	  	                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
	  	                 handler: function() {
	  	                	cobranzaWindow.close();
	  	                 }
	  	           }
	  	          ]
	  	        });
	        	cobranzaWindow.show();
	        	cargaStorePaginadoLocal(cobCargadaStore, _UrlCobCargada, 'slist1', null, function (options, success, response){
	        		if(success){
	                    var jsonResponse = Ext.decode(response.responseText);
	                    
	                    if(!jsonResponse.success) {
	                        showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
	                    }
	                }else{
	                    showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
	                }
	        	}, gridCobCargada);
	        }
	    },{
	        text: 'Ver Remesa Aplicada',
	        icon:_CONTEXT+'/resources/fam3icons/icons/database_table.png',
	        handler: function() {
	        	var cobranzaWindow = Ext.create('Ext.window.Window', {
		  	          title: 'Remesa Aplicada',
		  	          modal:true,
		  	          closeAction: 'hide',
		  	          items:[gridRemesaAplicada],
		  	          bodyStyle:'padding:10px;',
		  	          buttons:[
		  	           {
		  	                 text: 'Cerrar',
		  	                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
		  	                 handler: function() {
		  	                	cobranzaWindow.close();
		  	                 }
		  	           }
		  	          ]
		  	        });
		        	cobranzaWindow.show();
		        	cargaStorePaginadoLocal(remesaAplicadaStore, _UrlRemesaAplicada, 'slist1', null, function (options, success, response){
		        		if(success){
		                    var jsonResponse = Ext.decode(response.responseText);
		                    
		                    if(!jsonResponse.success) {
		                        showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
		                    }
		                }else{
		                    showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
		                }
		        	}, gridRemesaAplicada);
	        }
	    }]
		    
	});
	
	panelCobranza.getDockedItems('toolbar[dock="bottom"]').forEach(function(element, index, array){
		element.disable();
	});
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
});
</script>

</head>
<body>
<div id="mainDivCobranza" style="height:350px;"></div>
</body>
</html>