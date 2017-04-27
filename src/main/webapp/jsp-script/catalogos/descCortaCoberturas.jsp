<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Descripcion Corta Coberturas</title>
<script>

///////////////////////
///////  URLS ////////
/*///////////////////*/
var _UrlObtieneCoberturas  = '<s:url namespace="/emision" action="obtieneNombresCoberturasPlan" />';
var _UrlActualizaDescCoberturas  = '<s:url namespace="/catalogos" action="actualizaDescCoberturas" />';

var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';

var _CAT_PRODUCTO   = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS"/>';
var _CAT_MODALIDAD  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPSIT"/>';
var _CAT_PLANES     = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PLANES_X_PRODUCTO"/>';


///////////////////////
///////  URLS ////////
/*///////////////////*/

///////////////////////
//////variables //////
/*///////////////////*/

var _MSG_SIN_DATOS = 'No hay datos';

/*///////////////////*/
////// variables //////
///////////////////////


Ext.onReady(function(){
    
    var storePlanes = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type        : 'ajax'
            ,url        : _URL_CARGA_CATALOGO
            ,extraParams: {catalogo:_CAT_PLANES}
            ,reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
    });
    
    var planes = Ext.create('Ext.form.ComboBox', {
        name:'params.cdtipsit',
        fieldLabel: 'Plan',
        queryMode:'local',
        displayField: 'value',
        valueField: 'key',
        //allowBlank:false,
        forceSelection: true,
        emptyText:'Seleccione...',
        store: storePlanes,
        hidden: true //Temporalmente solo MSC, existen algunos planes que no tienen las coberutas obligatorias
                     //y no salen todas por lo que se opta por ocultar este campo para que salgan todas las del subramo 
    }); 
	
    var storeModalidades = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type        : 'ajax'
            ,url        : _URL_CARGA_CATALOGO
            ,extraParams: {catalogo:_CAT_MODALIDAD}
            ,reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
    });
    
    var modalidades = Ext.create('Ext.form.ComboBox', {
        name:'params.cdtipsit',
        fieldLabel: 'Subramo',
        queryMode:'local',
        displayField: 'value',
        valueField: 'key',
        allowBlank:false,
        forceSelection: true,
        emptyText:'Seleccione...',
        store: storeModalidades,
        listeners: {
            select: function(combo, records){
                var selRecord = records[0];
                storePlanes.load({
                    params: {
                        'params.cdramo': productos.getValue(),
                        'params.cdtipsit': selRecord.get('key')
                    }
                });
            }
        }
    }); 
    
	var storeProducto = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type        : 'ajax'
            ,url        : _URL_CARGA_CATALOGO
            ,extraParams: {catalogo:_CAT_PRODUCTO}
            ,reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
    });
	
	var productos = Ext.create('Ext.form.ComboBox', {
    	name:'params.cdramo',
    	fieldLabel: 'Producto',
    	queryMode:'local',
    	displayField: 'value',
    	valueField: 'key',
    	allowBlank:false,
    	forceSelection: true,
    	emptyText:'Seleccione...',
    	store: storeProducto,
        listeners: {
        	select: function(combo, records){
        		var selRecord = records[0];
        		storeModalidades.load({
        			params: {
        				'params.idPadre': selRecord.get('key')
        			}
        		});
        	}
        }
   	});   

	/**
	* Para cargar solo el producto de Multisalud colectivo 
	*/
	storeProducto.load({
	    callback:  function(records, operation, success){
	        if(success){
	            productos.setValue(Ramo.Multisalud);
	            productos.setReadOnly(true);
	            storeModalidades.load({
                    params: {
                        'params.idPadre': Ramo.Multisalud
                    },
                    callback : function(){
                        modalidades.setValue(TipoSituacion.MultisaludColectivo);
                        modalidades.setReadOnly(true);
                        
                        coberturasStore.load({
                            params:{
                                'smap1.cdramo'   : productos.getValue(),
                                'smap1.cdtipsit' : modalidades.getValue(),
                                'smap1.cdplan'   : planes.getValue()
                            }
                        });
                    }
                });
	        }
	    }
	});
	
	var panelBusqueda = Ext.create('Ext.form.Panel', {
        title: 'Edici&oacute;n de nombres cortos de coberturas.',
        border: false,
        bodyStyle:'padding:2px 0px 0px 5px;',
        items    : [productos,modalidades,planes],
        buttonAlign: 'center',
        buttons: [{
            text: 'Buscar',
            icon    : '${ctx}/resources/fam3icons/icons/zoom.png',
            hidden: true,//Para cargar inmediatamente MSC
            handler: function(btn, e) {
                var form = this.up('form').getForm();
                if (form.isValid()) {
                    coberturasStore.load({
                        params:{
                            'smap1.cdramo'   : productos.getValue(),
                            'smap1.cdtipsit' : modalidades.getValue(),
                            'smap1.cdplan'   : planes.getValue()
                        }
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
                text: 'Limpiar',
                icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png',
                hidden: true,//Para cargar inmediatamente MSC
                handler: function(btn, e) {
                    panelBusqueda.getForm().reset();
                }
        }]
    });
	
	Ext.define('coberturasModel',{
         extend  : 'Ext.data.Model'
         ,fields : ['CDGARANT','DSGARANT','DSGARANT_CORTA']
     });
	
	var coberturasStore = Ext.create('Ext.data.Store',
	        {
	            model   : 'coberturasModel',
                proxy     : {
                    type        : 'ajax'
                    ,url        : _UrlObtieneCoberturas
                    ,reader     :
                    {
                        type  : 'json'
                        ,root : 'slist1'
                    }
                }
	        });
	
    var gridCobeturas = Ext.create('Ext.grid.Panel',{
		    title : 'Coberturas del plan. De clic en el nombre corto de la cobertura que desee editar.',
	        bodyStyle:'padding:2px 0px 0px 2px;',
		    height : 400,
		    store : coberturasStore,
		    plugins : [
		               Ext.create('Ext.grid.plugin.RowEditing', {
		                   clicksToEdit: 1,
		                   listeners :{
	                             /*validateedit: function(editor, context,eopt){
	                                 
	                                 alert('orig:' + context.originalValues['DSGARANT_CORTA']);
	                                 if(context.field == 'DSGARANT_CORTA' && Ext.isEmpty(context.originalValues['DSGARANT_CORTA'])){
	                                     
	                                 }
	                                 alert('nuevo:'context.newValues['DSGARANT_CORTA']);
	                                 
	                                 debug('editor',editor);
	                                 debug('context',context);
	                                 debug('eopt',eopt);
	                             },*/
	                             beforeedit: function(editor, context){
                                     if(context.field != 'DSGARANT_CORTA') return false;
                                 }
	                         }
		               })
		           ],
            border: false,
            buttonAlign: 'center',
		    columns :
	           [ { header: 'C&oacute;digo',        dataIndex : 'CDGARANT',       flex: 1},
	             { header: 'Nombre de la Cobertura' , dataIndex : 'DSGARANT',       flex: 2},
	             { header: 'Nombre Corto (editable)' ,        dataIndex : 'DSGARANT_CORTA', flex: 1,
	                 editor:{
	                     xtype: 'textfield',
	                     regex: /^[a-zA-Z0-9]+$/,
	                     regexText: 'Solo letras y numeros',
                         maxLength  : 6,
                         maxLengthText: 'Longitud m&aacute;xima de 6 caracteres',
                         allowBlank : true
                         
	                 }
	             },
	             
	           ],
	          buttons: [{
	               icon    : '${ctx}/resources/fam3icons/icons/disk.png',
	               text    : 'Guardar',
	               handler : function(btn, e){
	                   Ext.Msg.show({
	                        title: 'Aviso',
	                        msg: '&iquest;Esta seguro que desea guardar estos cambios?',
	                        buttons: Ext.Msg.YESNO,
	                        fn: function(buttonId, text, opt) {
	                            if(buttonId == 'yes') {
	                                
	                                var updateList = [];
	                                coberturasStore.getUpdatedRecords().forEach(function(record,index,arr){
	                                    updateList.push(record.data);
	                                });
	                                
	                                debug('Lista de Coberturas a guardar: ',updateList);
	                                
	                                panelPrincipal.setLoading(true);
	                                
	                                Ext.Ajax.request({
                                        url: _UrlActualizaDescCoberturas,
                                        jsonData : {
                                            'saveList' : updateList
                                        },
                                        success  : function(response, options){
                                            panelPrincipal.setLoading(false);
                                            var json = Ext.decode(response.responseText);
                                            if(json.success){
                                                coberturasStore.reload();
                                                mensajeCorrecto('Aviso','Se ha guardado correctamente.');
                                            }else{
                                                mensajeError(json.msgRespuesta);
                                            }
                                        }
                                        ,failure  : function(response, options){
                                            panelPrincipal.setLoading(false);
                                            panelPrincipal.setLoading(false);
                                            var json = Ext.decode(response.responseText);
                                            mensajeError(json.msgRespuesta);
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
	
    var panelPrincipal = Ext.create('Ext.form.Panel', {
        renderTo: 'mainDivDescCob',
        bodyStyle:'padding:2px 0px 0px 2px;',
        items    : [panelBusqueda,gridCobeturas]
    });
});


///////////////////////
//////funciones //////
/*///////////////////*/
/*///////////////////*/
//////funciones //////
///////////////////////


</script>

</head>
<body>
<div id="mainDivDescCob" style="height:433px;"></div>
</body>
</html>