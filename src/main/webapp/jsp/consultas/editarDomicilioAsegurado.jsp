<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _CONTEXT = '${ctx}';

var _cdPersonAsegurado = '<s:property value="params.cdperson" />';
var _nmordinaDomiclio = '<s:property value="params.nmorddom" />';

debug('_nmordinaDomiclio:', _nmordinaDomiclio, '.');

var _URL_ObtieneDomicilioAseg   = '<s:url namespace="/catalogos" action="obtenerDomicilioPorCdperson" />';
var _URL_GuardaDomicilioAseg    = '<s:url namespace="/catalogos" action="guardarDomicilioAsegurado" />';

var _URL_OBTEN_CATALOGO_GENERICO=           '<s:url namespace="/catalogos" action="obtieneCatalogo" />';

var panelDomici;
var recordCargaDomi;

var primerCarga = 0;

Ext.onReady(function() {

	Ext.define('modeloDomicil',{
        extend: 'Ext.data.Model',
        fields: [
        		 {type:'string',    name:'smap1.CDPERSON' , mapping:'CDPERSON'},
                 {type:'string',    name:'smap1.NMORDDOM' , mapping:'NMORDDOM'},
                 {type:'string',    name:'smap1.DSDOMICI' , mapping:'DSDOMICI'},
                 {type:'string',    name:'smap1.NMTELEFO' , mapping:'NMTELEFO'},
                 {type:'string',    name:'smap1.CODPOSTAL', mapping:'CODPOSTAL'},
                 {type:'string',    name:'smap1.CDEDO'    , mapping:'CDEDO'},
                 {type:'string',    name:'smap1.ESTADO'   , mapping:'ESTADO'},
                 {type:'string',    name:'smap1.CDMUNICI' , mapping:'CDMUNICI'},
                 {type:'string',    name:'smap1.MUNICIPIO', mapping:'MUNICIPIO'},
                 {type:'string',    name:'smap1.CDCOLONI' , mapping:'CDCOLONI'},
                 {type:'string',    name:'smap1.NMNUMERO' , mapping:'NMNUMERO'},
                 {type:'string',    name:'smap1.NMNUMINT' , mapping:'NMNUMINT'}
				]
    });
    
    var storeDomicilio = Ext.create('Ext.data.JsonStore', {
    	model:'modeloDomicil',
        proxy: {
            type: 'ajax',
            url: _URL_ObtieneDomicilioAseg,
            reader: {
                type: 'json',
                root: 'smap1'
            }
        }
    });
    
    var storeEstados = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type  : 'ajax',
            url   : _URL_OBTEN_CATALOGO_GENERICO,
			extraParams : {
				catalogo : 'TATRISIT',
				'params.cdatribu' : '4',
				'params.cdtipsit' : 'SL'
			},
            reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        },
        listeners: {
            load: function (){}   
        }
    });

	var storeMunici = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type  : 'ajax',
            url   : _URL_OBTEN_CATALOGO_GENERICO,
			extraParams : {
				catalogo : 'TATRISIT',
				'params.cdatribu' : '17',
				'params.cdtipsit' : 'SL'
			},
            reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        },
        listeners: {
            load: function (){}   
        }
    });

    var storeColonia = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type  : 'ajax',
            url   : _URL_OBTEN_CATALOGO_GENERICO,
            extraParams : {
				catalogo : 'COLONIAS'
			},
            reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        },
        listeners: {
            load: function (){}   
        }
    });
    
    
    storeDomicilio.load({
    	params: {
    		'smap1.cdperson' : _cdPersonAsegurado,
    		'smap1.nmorddom' : _nmordinaDomiclio
    	},
    	callback: function(records, op, success){
    		if (success === true && records.length > 0) {
    		    recordCargaDomi = storeDomicilio.getAt(0);
    		    panelDomici.loadRecord(recordCargaDomi);
    		    panelDomici.down('[name=smap1.accion]').setValue('U');
    		    debug('Valores a agregar:',storeDomicilio.getAt(0));
    		} else {
    		    panelDomici.down('[name=smap1.accion]').setValue('I');
    		}
    	}
    });
    
    panelDomici = Ext.create('Ext.form.Panel',{
		title: 'Edite los datos del domicilio',
		renderTo : 'maindivDomicilio',
		autoScroll: true,
		titleCollapse: true,
		defaults : {
            style : 'margin : 5px;'
        },
		url: _URL_GuardaDomicilioAseg,
		height: 300,
		items : [   {
		                xtype      : 'textfield',
		                name       : 'smap1.accion',
		                allowBlank : false,
		                fieldLabel : 'accion',
		                hidden     : true
		            }, {
                        xtype: 'textfield',
                        fieldLabel: 'CDPERSON',
                        name: 'smap1.CDPERSON',
                        hidden: true
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'NMORDDOM',
                        name: 'smap1.NMORDDOM',
                        hidden: true
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'NMTELEFO',
                        name: 'smap1.NMTELEFO',
                        hidden: true
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'MUNICIPIO',
                        name: 'smap1.MUNICIPIO',
                        hidden: true
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'ESTADO',
                        name: 'smap1.ESTADO',
                        hidden: true
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'C&oacute;digo Postal',
                        name: 'smap1.CODPOSTAL',
                        allowBlank: false,
                        listeners : {
                        	change: function(cmp,newVal){
                        		primerCarga ++;
                        		if(!Ext.isEmpty(newVal) && (new String(newVal)).length > 4 ){
                        			storeEstados.load({
										params : {
											'params.idPadre' : newVal
										},
										callback : function(records,operation,success) {}
									});
									
									storeColonia.load({
										params : {
											'params.cp' : newVal
										},
										callback : function(records,operation,success) {
											if(primerCarga == 1 && recordCargaDomi){
												_fieldByName('smap1.CDMUNICI',panelDomici).setValue(recordCargaDomi.get('smap1.CDMUNICI'));
											}
										}
									});
                        		}
                        	}
                        }
                    },{
                        xtype         : 'combobox',
                        name          : 'smap1.CDEDO',
                        fieldLabel    : 'Estado',
                        valueField    : 'key',
                        displayField  : 'value',
                        forceSelection: true,
                        queryMode     :'local',
                        store         : storeEstados,
                        listeners: {
                           select: function ( combo, records, eOpts ){},
                           change: function(combo,newVal){
                        		if(!Ext.isEmpty(newVal)){
                        			storeMunici.load({
									params : {
										'params.idPadre' : newVal
									},
									callback : function(records,operation,success) {
										if(primerCarga == 1 && recordCargaDomi){
											_fieldByName('smap1.CDMUNICI',panelDomici).setValue(recordCargaDomi.get('smap1.CDMUNICI'));
										}
									}
								});
                        		}
                        	}
                       }
                    },{
                        xtype         : 'combobox',
                        name          : 'smap1.CDMUNICI',
                        fieldLabel    : 'Municipio',
                        valueField    : 'key',
                        displayField  : 'value',
                        forceSelection: true,
                        queryMode     :'local',
                        store         : storeMunici,
                       listeners: {
                           select: function ( combo, records, eOpts ){},
                           change: function(cmb,newVal){}
                       }
                    },{
                        xtype         : 'combobox',
                        name          : 'smap1.CDCOLONI',
                        fieldLabel    : 'Colonia',
                        valueField    : 'key',
                        displayField  : 'value',
                        forceSelection: true,
                        queryMode     :'local',
                        store         : storeColonia,
                       listeners: {
                           select: function ( combo, records, eOpts ){},
                           change: function(cmb,newVal){}
                       }
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'Calle',
                        name: 'smap1.DSDOMICI'
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'Exterior',
                        name: 'smap1.NMNUMERO',
                        regex: /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/,
                        regexText: 'Solo d&iacute;gitos, letras, espacios y guiones'
                    },{
                        xtype: 'textfield',
                        fieldLabel: 'Interior',
                        name: 'smap1.NMNUMINT',
                        regex: /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/,
                        regexText: 'Solo d&iacute;gitos, letras, espacios y guiones'
                    }],
		buttonAlign: 'center', 
		buttons:[{
				text: 'Guardar Domicilio',
				icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
				handler: function() {
					
					var form = panelDomici.getForm();
					
					if (form.isValid()) {
					    var mask = _maskLocal('Guardando domicilio');
		                form.submit({
		                    success: function(form, action) {
		                        mask.close();
		                    	if(action.result.exito === true){
		                    		mensajeCorrecto('Aviso', 'Datos guardados.');
		                    		try {
                                        _callbackDomicilioAseg();
                                    } catch (e) {
                                        debugError('No hay funcion callback en domicilio.',e);
                                    }	
		                    	}else{
		                    		 mensajeError(action.result ? action.result.respuesta : 'Error al guardar los datos.');
		                    	}
		                       
		                       
		                    },
		                    failure: function(form, action) {
		                        mask.close();
		                        mensajeError('Error', action.result ? action.result.respuesta : 'Error al guardar los datos.');
		                    }
		                });
		            }
				}
		}]
	});
	
});


</script>

<div id="maindivDomicilio" style="height:300px;"></div>