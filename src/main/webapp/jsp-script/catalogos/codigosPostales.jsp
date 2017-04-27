<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Codigos Postales</title>
<script>

///////////////////////
////// variables //////
/*///////////////////*/

var _UrlAgregaCP =        '<s:url namespace="/catalogos"    action="agregaCodigoPostal" />';
var _UrlAsociaZonaCP =    '<s:url namespace="/catalogos"    action="asociaZonaCodigoPostal" />';
var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CAT_ESTADOS  =       '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TESTADOS"/>';
var _CAT_MUNICIPIOS =     '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MUNICIPIOS"/>';
var _CAT_MODALIDADES =    '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPSIT"/>';
var _CAT_ZONAS =          '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TZONAS"/>';
var _CAT_ZONAS_POR_PRODUCTO = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ZONAS_POR_PRODUCTO"/>';

var _MSG_SIN_DATOS = 'No hay datos';

/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function(){
	
	
	var codigoPostal = new Ext.form.TextField({
    	fieldLabel: 'C&oacute;digo Postal',
    	name: 		'params.pv_cdpostal_i',
    	allowBlank: false,
        blankText:'Campo requerido',
        minLength: 5,
        maskRe   : /^[0-9]+$/,
		minLengthText: 'El C&oacute;digo Postal debe tener almenos 5 caracteres.'
	});
	
	var storeMunici = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        //autoLoad  : true,
        proxy     : {
            type        : 'ajax'
            ,url        : _URL_CARGA_CATALOGO
            ,extraParams: {catalogo:_CAT_MUNICIPIOS}
            ,reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
    });
	
	var estado = Ext.create('Ext.form.ComboBox', {
    	name:'params.pv_cdedo_i',
    	fieldLabel: 'Estado',
    	queryMode:'local',
    	displayField: 'value',
    	valueField: 'key',
    	allowBlank:false,
    	forceSelection: true,
    	emptyText:'Seleccione...',
    	store: Ext.create('Ext.data.Store', {
            model     : 'Generic',
            autoLoad  : true,
            proxy     : {
                type        : 'ajax'
                ,url        : _URL_CARGA_CATALOGO
                ,extraParams: {catalogo:_CAT_ESTADOS}
                ,reader     :
                {
                    type  : 'json'
                    ,root : 'lista'
                }
            }
        }),
        listeners: {
        	select: function(combo, records){
        		var selRecord = records[0];
        		storeMunici.load({
        			params: {
        				'params.cdestado': selRecord.get('key')
        			}
        		});
        	}
        }
   	});   

	
	var municipio = Ext.create('Ext.form.ComboBox', {
    	name:'params.pv_cdmunici_i',
    	fieldLabel: 'Municipio',
    	queryMode:'local',
    	displayField: 'value',
    	valueField: 'key',
    	allowBlank:false,
    	forceSelection: true,
    	emptyText:'Seleccione...',
    	store: storeMunici
   	});
	
	
	var panelCP = Ext.create('Ext.form.Panel', {
		title: 'Agregar un nuevo C&oacute;digo Postal',
		url: _UrlAgregaCP,
		border: false,
	    bodyStyle:'padding:5px 0px 0px 40px;',
	    items    : [codigoPostal,estado, municipio],
        buttonAlign: 'center',
	    buttons: [{
        	text: 'Guardar',
        	icon    : '${ctx}/resources/fam3icons/icons/disk.png',
        	handler: function(btn, e) {
        		var form = this.up('form').getForm();
        		if (form.isValid()) {
        			
        			Ext.Msg.show({
    		            title: 'Confirmar acci&oacute;n',
    		            msg: '&iquest;Esta seguro que desea crear este nuevo C&oacute;digo Postal?',
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		form.submit({
			    		        	waitMsg:'Procesando...',			        	
			    		        	failure: function(form, action) {
			    		        		showMessage('Error', action.result.msgRespuesta , Ext.Msg.OK, Ext.Msg.ERROR);
			    					},
			    					success: function(form, action) {
			    						form.reset();
			    						mensajeCorrecto('\u00C9xito', 'El C&oacute;digo Postal se guard&oacute; correctamente.');
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
            	text: 'Agregar Otro / Limpiar',
            	icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
            	handler: function(btn, e) {
            		panelCP.getForm().reset();
            	}
        }]
    });
	
	
	
	var codigoPostalAsc = new Ext.form.TextField({
    	fieldLabel: 'C&oacute;digo Postal',
    	name: 		'params.pv_cdpostal_i',
    	allowBlank: false,
        blankText:'Campo requerido',
        minLength: 5,
        maskRe   : /^[0-9]+$/,
		minLengthText: 'El C&oacute;digo Postal debe tener almenos 5 caracteres.'
	});
	
	var storeZonas = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        autoLoad  : true,
        proxy     : {
            type        : 'ajax'
            ,url        : _URL_CARGA_CATALOGO
            ,extraParams: {catalogo:_CAT_ZONAS}
            ,reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
    });
	
	var modalidad = Ext.create('Ext.form.ComboBox', {
    	name:'params.pv_cdtipsit_i',
    	fieldLabel: 'Modalidad',
    	queryMode:'local',
    	displayField: 'value',
    	valueField: 'key',
    	allowBlank:false,
    	forceSelection: true,
    	emptyText:'Seleccione...',
    	store: Ext.create('Ext.data.Store', {
            model     : 'Generic',
            autoLoad  : true,
            proxy     : {
                type        : 'ajax'
                ,url        : _URL_CARGA_CATALOGO
                ,extraParams: {catalogo:_CAT_MODALIDADES}
                ,reader     :
                {
                    type  : 'json'
                    ,root : 'lista'
                }
            }
        }),
        listeners: {
        	select: function(combo, records){
        		var selRecord = records[0];
        		/*storeZonas.load({
        			params: {
        				'params.pv_cdtipsit_i': selRecord.get('key')
        			}
        		});*/
        	}
        }
   	});

	
	var zona = Ext.create('Ext.form.ComboBox', {
    	name:'params.pv_cvezona_i',
    	fieldLabel: 'Zona',
    	queryMode:'local',
    	displayField: 'value',
    	valueField: 'key',
    	allowBlank:false,
    	forceSelection: true,
    	emptyText:'Seleccione...',
    	store: storeZonas
   	});
	
	
	var panelZonas = Ext.create('Ext.form.Panel', {
		title: 'Asociar Zona a C&oacute;digo Postal',
		url: _UrlAsociaZonaCP,
		border: false,
	    bodyStyle:'padding:5px 0px 0px 40px;',
	    items    : [codigoPostalAsc,modalidad, zona],
        buttonAlign: 'center',
	    buttons: [{
        	text: 'Guardar',
        	icon    : '${ctx}/resources/fam3icons/icons/disk.png',
        	handler: function(btn, e) {
        		var form = this.up('form').getForm();
        		if (form.isValid()) {
        			
        			Ext.Msg.show({
    		            title: 'Confirmar acci&oacute;n',
    		            msg: '&iquest;Esta seguro que desea Asociar la Zona a este C&oacute;digo Postal?',
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		form.submit({
			    		        	waitMsg:'Procesando...',			        	
			    		        	failure: function(form, action) {
			    		        		showMessage('Error', action.result.msgRespuesta , Ext.Msg.OK, Ext.Msg.ERROR);
			    					},
			    					success: function(form, action) {
			    						form.reset();
			    						mensajeCorrecto('\u00C9xito', 'La zona se asoci&oacute; correctamente.');
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
            	text: 'Nueva Asociaci&oacute;n / Limpiar',
            	icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
            	handler: function(btn, e) {
            		panelZonas.getForm().reset();
            	}
        }]
    });
	
	var tabs = Ext.create('Ext.tab.Panel', {
	    width: 400,
	    height: 250,
	    defaults : {
			style : 'margin : 20px;'
		},
	    renderTo: 'mainDivCP',
	    items: [panelCP, panelZonas]
	});
	
		
});
</script>

</head>
<body>
<div id="mainDivCP" style="height:433px;"></div>
</body>
</html>