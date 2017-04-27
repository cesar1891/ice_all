<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _CONTEXT = '${ctx}';

//Catalogo Tipos de pago a utilizar:
var _PAGO_DIRECTO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
var _REEMBOLSO    = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';
var _ROL_ACTIVO   = '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@MEDICO_AJUSTADOR.cdsisrol" />';
var _URL_LoadFacturas =  '<s:url namespace="/siniestros" action="loadListaFacturasTramite" />';
var _URL_EliminaFactura =  '<s:url namespace="/siniestros" action="eliminaFacturaTramite" />';

var _URL_LoadConceptos =  '<s:url namespace="/siniestros" action="obtenerMsinival" />';
var _URL_GuardaConcepto =  '<s:url namespace="/siniestros" action="guardarMsinival" />';

var _UrlAjustesMedicos =  '<s:url namespace="/siniestros" action="includes/ajustesMedicos" />';

var _URL_CATALOGOS = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CATALOGO_TipoAtencion = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_ATENCION_SINIESTROS"/>';
var _CATALOGO_PROVEEDORES  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
var _CATALOGO_COBERTURAS  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@COBERTURAS"/>';
var _CATALOGO_SUBCOBERTURAS  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SUBCOBERTURAS"/>';

var _CATALOGO_TipoConcepto  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_CONCEPTO_SINIESTROS"/>';
var _CATALOGO_ConceptosMedicos  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CODIGOS_MEDICOS"/>';
var _CATALOGO_TipoMoneda   = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_MONEDA"/>';

var _URL_MONTO_ARANCEL		= '<s:url namespace="/siniestros"  action="obtieneMontoArancel"/>';
var _URL_LISTA_COBERTURA 	= '<s:url namespace="/siniestros"  action="consultaListaCoberturaPoliza" />';

var _Operacion;
var _Nmordina;

//jtezva
var _revAdm_urlMesaControl = '<s:url namespace="/mesacontrol" action="mcdinamica" />';
var _revAdm_itemsRechazo   = [ <s:property value="imap.itemsCancelar" /> ];
    _revAdm_itemsRechazo[2]['width']  = 500;
    _revAdm_itemsRechazo[2]['height'] = 150;
var _revAdm_formRechazo;
var _revAdm_windowRechazo;
var panelEdicionFacturas;

Ext.onReady(function() {
	
	var storeFacturas;
	var storeConceptos;
	
	var gridFacturas;
	var gridConceptos;
	
	var panelPrincipal;
	
	Ext.define('modelFacturas',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'NFACTURA'},
                 {type:'string',    name:'FFACTURA'},
                 {type:'string',    name:'CDTIPSER'},
                 {type:'string',    name:'DESCRIPC'},
                 {type:'string',    name:'CDPRESTA'},
                 {type:'string',    name:'PTIMPORT'},
                 {type:'string',    name:'CDGARANT'},
                 {type:'string',    name:'DSGARANT'},
                 {type:'string',    name:'DESCPORC'},
                 {type:'string',    name:'DESCNUME'},
                 {type:'string',    name:'CDCONVAL'},
                 {type:'string',    name:'COPAGO'},
                 {type:'string',    name:'NOM_PRESTA'},
                 {type:'string',    name:'DSSUBGAR'},
                 {type:'string',    name:'AUTRECLA'},
                 {type:'string',    name:'AUTMEDIC'},
                 {type:'string',    name:'COMMENME'},
                 {type:'string',    name:'DEDUCIBLE'},
                 {type:'string',    name:'COMMENAR'},
                 {type:'string',    name:'CDMONEDA'},
                 {type:'string',    name:'DESTIPOMONEDA'},
                 {type:'string',    name:'TASACAMB'},
                 {type:'string',    name:'PTIMPORTA'},
                 {type:'string',    name:'DCTONUEX'},
                 {type:'string',    name:'APLICA_IVA'},
                 {type:'string',    name:'ANTES_DESPUES'},
                 {type:'string',    name:'CDPRESTA'},
                 {type:'string',    name:'CODRECLAM'},
                 {type:'string',    name:'IVARETENIDO'}
				]
    });
	
	Ext.define('modelConceptos',{
        extend: 'Ext.data.Model',
        fields: ["CDUNIECO","CDRAMO","ESTADO","NMPOLIZA","NMSUPLEM",
         		"NMSITUAC","AAAPERTU","STATUS","NMSINIES","NFACTURA",
        		"CDGARANT","CDCONVAL","CDCONCEP","IDCONCEP","CDCAPITA",
        		"DSGARANT","DSSUBGAR","DESIDCONCEP","DESCONCEP","TOTAJUSMED","SUBTAJUSTADO","PTMTOARA",
        		"NMORDINA",{name:"FEMOVIMI", type: "date", dateFormat: "d/m/Y"},"CDMONEDA","PTPRECIO","CANTIDAD",
        		"DESTOPOR","DESTOIMP","PTIMPORT","PTRECOBR","NMANNO",
        		"NMAPUNTE","USERREGI",{name:"FEREGIST", type: "date", dateFormat: "d/m/Y"},"PTPCIOEX","DCTOIMEX","PTIMPOEX"]
    });

	Ext.define('modelListadoProvMedico',{
	    extend: 'Ext.data.Model',
	    fields: [
            {type:'string', name:'cdpresta'},
            {type:'string', name:'nombre'},
            {type:'string', name:'cdespeci'},
            {type:'string',name:'descesp'}
	    ]
	});
	
	//jtezva
	Ext.define('_revAdm_FormRechazo',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_revAdm_FormRechazo initComponent');
            Ext.apply(this,
            {
                border  : 0
                ,items  : _revAdm_itemsRechazo
            });
            this.callParent();
        }
    });
	
	//jtezva
	Ext.define('_revAdm_WindowRechazo',
    {
        extend         : 'Ext.window.Window'
        ,initComponent : function()
        {
            debug('_revAdm_WindowRechazo initComponent');
            Ext.apply(this,
            {
                title        : 'Rechazo de tr&aacute;mite'
                ,width       : 600
                ,height      : 350
                ,autoScroll  : true
                ,closeAction : 'hide'
                ,modal       : true
                ,defaults    : { style : 'margin : 5px; ' }
                ,items       : _revAdm_formRechazo
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Rechazar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,handler : _revAdm_rechazoSiniestro
                    }
                ]
            });
            this.callParent();
        }
    });
	
	storeFacturas=new Ext.data.Store(
	{
	    autoDestroy: true,
	    model: 'modelFacturas',
	    proxy: {
            type: 'ajax',
            url: _URL_LoadFacturas,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
	});
	storeFacturas.load({
    	params: {
    		'params.ntramite' : _NTRAMITE,
    		'params.cdunieco'  : _CDUNIECO,
    		'params.cdramo'    : _CDRAMO,
    		'params.estado'    : _ESTADO,
    		'params.nmpoliza'  : _NMPOLIZA,
    		'params.nmsituac'  : _NMSITUAC,
    		'params.nmsuplem'  : _NMSUPLEM,
    		'params.status'    : _STATUS,
    		'params.aaapertu'  : _AAAPERTU,
    		'params.nmsinies'  : _NMSINIES,
    		'params.cdtipsit'  : _CDTIPSIT
    	}
    });

	storeConceptos=new Ext.data.Store(
			{
			    autoDestroy: true,
			    model: 'modelConceptos',
			    proxy: {
		            type: 'ajax',
		            url: _URL_LoadConceptos,
		            reader: {
		                type: 'json',
		                root: 'loadList'
		            }
		        }
			});
	
	var storeTipoAtencion = Ext.create('Ext.data.JsonStore', {
		model:'Generic',
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CATALOGO_TipoAtencion},//Cat.TipoAtencionSiniestros
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
	storeTipoAtencion.load();
	
	var storeProveedor = Ext.create('Ext.data.Store', {
        model:'modelListadoProvMedico',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_CATALOGOS,
            extraParams:{
                catalogo         : _CATALOGO_PROVEEDORES,
                catalogoGenerico : true
            },
            reader: {
                type: 'json',
                root: 'listaGenerica'
            }
        }
    });
	storeProveedor.load();

 
 	Ext.define('modelListadoCobertura',{
        extend: 'Ext.data.Model',
        fields: [	{type:'string',    name:'cdgarant'},			{type:'string',    name:'dsgarant'},              	{type:'string',    name:'ptcapita'}		]
    });
    
	var storeCoberturas = Ext.create('Ext.data.Store',{
        model: 'modelListadoCobertura',
        autoLoad: true,
        proxy: {
            type: 'ajax',
            url: _URL_LISTA_COBERTURA,
            reader: {
                type: 'json',
                root: 'listaCoberturaPoliza'
            },
            extraParams: {
                'params.cdunieco'  : _CDUNIECO,
	    		'params.estado'    : _ESTADO,
	    		'params.cdramo'    : _CDRAMO,
	    		'params.nmpoliza'  : _NMPOLIZA,
	    		'params.nmsituac'  : _NMSITUAC,
	            'params.cdgarant'  : null
            }
        }
	});
	storeCoberturas.load();
	
	var storeTipoMoneda = Ext.create('Ext.data.JsonStore', {
		model:'Generic',
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CATALOGO_TipoMoneda},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
    storeTipoMoneda.load();
    
	var storeSubcoberturas = Ext.create('Ext.data.Store',{
        model: 'Generic',
        autoLoad: false,
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            reader: {
                type: 'json',
                root: 'lista'
            },
            extraParams: {
                catalogo: _CATALOGO_SUBCOBERTURAS
            }
        }
	});

	var storeSubcoberturasC = Ext.create('Ext.data.Store',{
        model: 'Generic',
        autoLoad: false,
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            reader: {
                type: 'json',
                root: 'lista'
            },
            extraParams: {
                catalogo: _CATALOGO_SUBCOBERTURAS
            }
        }
	});
	
	var storeTipoConcepto = Ext.create('Ext.data.JsonStore', {
		model:'Generic',
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CATALOGO_TipoConcepto},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
	storeTipoConcepto.load();
	
	var storeConceptosCatalogo = Ext.create('Ext.data.JsonStore', {
		model:'Generic',
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CATALOGO_ConceptosMedicos},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
	
	
	panelEdicionFacturas= Ext.create('Ext.form.Panel',{
        border  : 0,
        width: 400,
        url: null
        ,bodyStyle:'padding:5px;'
       	,listeners : { 
	    		afterrender : function(){
	    			var aplicaIva= panelEdicionFacturas.items.items[13];
	    			var Antes_despues= panelEdicionFacturas.items.items[14];
	    			var iva_aplicado = panelEdicionFacturas.items.items[15];
	    			
	    			aplicaIva.on('change',function(){
                    	if(this.value == "S")
                   		{
                   			Antes_despues.show();
                    		iva_aplicado.show();
                   		}else{
                   			Antes_despues.hide();
                   			iva_aplicado.hide();
                   		}
                    })
	       	}
       	}
        ,items :
        [   
             {
		        xtype      : 'textfield'
		    	,fieldLabel : 'No. Factura'
	    		,allowBlank:false
		    	,name       : 'params.nfactura'
		    	,labelWidth: 170
    		},            
    		{
    	        name: 'params.fefactura',
    	        fieldLabel: 'Fecha Factura',
    	        xtype: 'datefield',
    	        format: 'd/m/Y',
    	        editable: true,
    	        allowBlank:false,
    	        value:new Date(),
    	        labelWidth: 170
    	    },{
            	xtype: 'combo',
                name:'params.cdtipser',
                valueField: 'key',
                displayField: 'value',
                fieldLabel: 'Tipo de servicio',
                store: storeTipoAtencion,
                queryMode:'local',
                allowBlank:false,
                labelWidth: 170,
                editable:false
            },{
            	xtype       : 'combo',
            	name        : 'params.cdpresta',
            	fieldLabel  : 'Proveedor',
            	displayField: 'nombre',
            	valueField  : 'cdpresta',
            	allowBlank  : false,
                forceSelection : true,
                matchFieldWidth: false,
                queryMode   :'remote',
                queryParam  : 'params.cdpresta',
                store       : storeProveedor,
                triggerAction  : 'all',
                //editable    : false,
                minChars  : 2,
                hideTrigger:true,
                triggerAction: 'all',
                labelWidth: 170
            },{
            	xtype       : 'combo',
            	name        : 'params.cdgarant',
            	fieldLabel  : 'Cobertura',
            	displayField: 'dsgarant',
            	valueField  : 'cdgarant',
            	allowBlank  : true,
                forceSelection : true,
                matchFieldWidth: false,
                queryMode   :'remote',
                labelWidth: 170,
                store       : storeCoberturas,
                triggerAction  : 'all',
                listeners: {
                	select: function (combo, records, opts){
                		var cdGarant =  records[0].get('cdgarant');
                		storeSubcoberturas.load({
                			params: {
                				'params.cdgarant' : cdGarant
                			}
                		});
                	}
                },
                editable    : false
            },{
            	xtype       : 'combo',
            	name        : 'params.cdconval',
            	fieldLabel  : 'Sub Cobertura',
            	displayField: 'value',
            	valueField  : 'key',
            	allowBlank  : true,
                forceSelection : true,
                matchFieldWidth: false,
                queryMode   :'local',
                store       : storeSubcoberturas,
                triggerAction  : 'all',
                labelWidth: 170,
                editable    : false
            },{
            	xtype       : 'combo',
            	name        : 'params.tipoMoneda',
            	fieldLabel  : 'Tipo moneda',
            	displayField: 'value',
            	valueField  : 'key',
            	allowBlank  : true,
                forceSelection : true,
                matchFieldWidth: false,
                queryMode   :'local',
                labelWidth: 170,
                store       : storeTipoMoneda,
                triggerAction  : 'all',
                editable    : false,
                listeners : {
    				change:function(e){
    					if(e.getValue()!='001')
						{
    						panelEdicionFacturas.query('numberfield[name=params.ptimport]')[0].show();
    						panelEdicionFacturas.query('numberfield[name=params.ptimporta]')[0].show();
    						panelEdicionFacturas.query('numberfield[name=params.tasacamb]')[0].show();
    						panelEdicionFacturas.query('numberfield[name=params.dctonuex]')[0].show();
						
						}else{
							panelEdicionFacturas.query('numberfield[name=params.ptimport]')[0].show();
							panelEdicionFacturas.query('numberfield[name=params.ptimporta]')[0].hide();
    						panelEdicionFacturas.query('numberfield[name=params.tasacamb]')[0].hide();
    						panelEdicionFacturas.query('numberfield[name=params.dctonuex]')[0].hide();
						}
    	    		}
    	        }
                
            },{
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Importe'
                ,allowBlank:false
                ,allowDecimals: true
                ,decimalSeparator: '.'
                ,minValue: 0
                ,labelWidth: 170
		    	,name       : 'params.ptimport'
    		},{
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Importe moneda extranjera'
	    		,labelWidth: 170
                ,allowBlank:false
                ,allowDecimals: true
                ,decimalSeparator: '.'
                ,minValue: 0
		    	,name       : 'params.ptimporta'
		    	,listeners : {
    				change:function(e){
    					panelEdicionFacturas.down('[name="params.ptimport"]').setValue(+panelEdicionFacturas.down('[name="params.tasacamb"]').getValue() * +panelEdicionFacturas.down('[name="params.ptimporta"]').getValue() );
    	    		}
    	        }
    		},{
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Tasa Cambio'
                ,allowBlank:false
                ,allowDecimals: true
                ,decimalSeparator: '.'
               	,labelWidth: 170
                ,minValue: 0
		    	,name       : 'params.tasacamb'
		    	,listeners : {
    				change:function(e){
    					var cantidad = e.getValue();
    					if(+ cantidad > 0)
   						{
    						panelEdicionFacturas.down('[name="params.ptimport"]').setValue(+panelEdicionFacturas.down('[name="params.tasacamb"]').getValue() * +panelEdicionFacturas.down('[name="params.ptimporta"]').getValue() );
    				 		panelEdicionFacturas.down('[name="params.descnume"]').setValue(+panelEdicionFacturas.down('[name="params.tasacamb"]').getValue() * +panelEdicionFacturas.down('[name="params.dctonuex"]').getValue() );
   						}else{
   							panelEdicionFacturas.down('[name="params.ptimport"]').setValue('0');
   					 		panelEdicionFacturas.down('[name="params.descnume"]').setValue('0');
   						}
    	    		}
    	        }
    		},{
		        	xtype      : 'numberfield'
			    	,fieldLabel : 'Descuento %'
	                ,allowBlank:false
	                ,allowDecimals: true
	                ,decimalSeparator: '.'
	                ,minValue: 0
	                ,labelWidth: 170
			    	,name       : 'params.descporc'
	    	},{
		        xtype      : 'numberfield'
			    	,fieldLabel : 'Descuento Importe'
	                ,allowBlank:false
	                ,allowDecimals: true
	                ,decimalSeparator: '.'
	                ,minValue: 0
	                ,labelWidth: 170
			    	,name       : 'params.descnume'
	    	},{
		        xtype      : 'numberfield'
			    	,fieldLabel : 'Descuento Importe Ext'
	                ,allowBlank:false
	                ,allowDecimals: true
	                ,decimalSeparator: '.'
	                ,minValue: 0
	                ,labelWidth: 170
			    	,name       : 'params.dctonuex'
		    		,listeners : {
	    				change:function(e){
	    					panelEdicionFacturas.down('[name="params.descnume"]').setValue(+panelEdicionFacturas.down('[name="params.tasacamb"]').getValue() * +panelEdicionFacturas.down('[name="params.dctonuex"]').getValue() );
	    	    		}
	    	        }
	    	}
	    	
	    	<s:property value='%{"," + imap.tatrisinItems}' />
	    	<s:property value='%{"," + imap.itemsEdicion}' />
        ]
    });
	for(var i=13;i<panelEdicionFacturas.items.items.length;i++)
	{
		panelEdicionFacturas.items.items[i].labelWidth=166;
	}
	
    var panelEdicionConceptos = Ext.create('Ext.form.Panel',{
        border  : 0
        ,bodyStyle:'padding:5px;'
        ,url: _URL_GuardaConcepto
        ,items :
        [   
             {
		        xtype      : 'textfield'
		    	,fieldLabel : 'Proveedor'
	    		,allowBlank:false
		    	,name       : 'params.idProvFactura'
		    	,labelWidth: 150
		    	,hidden: true
    		},{
        	xtype       : 'combo',
        	name        : 'params.cdconval',
        	fieldLabel  : 'Sub Cobertura',
        	labelWidth: 150,
        	displayField: 'value',
        	valueField  : 'key',
        	allowBlank  : false,
            forceSelection : true,
            matchFieldWidth: false,
            queryMode   :'local',
            store       : storeSubcoberturasC,
            triggerAction  : 'all',
            editable    : false
        },{
        	xtype: 'combo',
            name:'params.idconcep',
            labelWidth: 150,
            valueField: 'key',
            displayField: 'value',
            fieldLabel: 'Tipo de Concepto',
            store: storeTipoConcepto,
            queryMode:'local',
            allowBlank:false,
            editable:false,
            listeners:{
            	select: function (combo, records, opts){
            		var cdTipo =  records[0].get('key');
            		storeConceptosCatalogo.proxy.extraParams=
            		{
            			'params.idPadre' : cdTipo
            			,catalogo        : _CATALOGO_ConceptosMedicos
            		};
            	}
            }
        },{
        	xtype: 'combo',
            name:'params.cdconcep',
            labelWidth: 150,
            valueField: 'key',
            displayField: 'value',
            fieldLabel: 'Concepto',
            store: storeConceptosCatalogo,
            queryMode:'remote',
            allowBlank:false,
            editable:true,
            forceSelection: true
            ,queryParam  : 'params.descripc'
            ,hideTrigger : true
            ,minChars    : 3
            ,listeners : {
   				select:function(e){
   					Ext.Ajax.request(
					{
					    url     : _URL_MONTO_ARANCEL
					    ,params:{
							'params.tipoConcepto'   : panelEdicionConceptos.down('combo[name=params.idconcep]').getValue(),
							'params.idProveedor'    : panelEdicionConceptos.down('[name="params.idProvFactura"]').getValue(),
                            'params.idConceptoTipo' : e.getValue()
		                }
					    ,success : function (response)
					    {
					    	if(Ext.decode(response.responseText).montoArancel == null){
					    		panelEdicionConceptos.down('[name="params.ptprecioArancel"]').setValue("0");
					    	}else{
					    		panelEdicionConceptos.down('[name="params.ptprecioArancel"]').setValue(Ext.decode(response.responseText).montoArancel);
					    	}
					    },
					    failure : function ()
					    {
					        me.up().up().setLoading(false);
					        Ext.Msg.show({
					            title:'Error',
					            msg: 'Error de comunicaci&oacute;n',
					            buttons: Ext.Msg.OK,
					            icon: Ext.Msg.ERROR
					        });
					    }
					});
   	    		}
   	        }
        
        },
        {
       		xtype      : 'numberfield'
   			,fieldLabel : 'Precio Arancel'
  			,labelWidth: 150
  			,allowBlank:false
  			,allowDecimals: true
			,decimalSeparator: '.'
			,minValue: 0
			,name       : 'params.ptprecioArancel'
			//,readOnly: (Ext.isEmpty(_CDROL) || _CDROL != _ROL_ACTIVO)
   		},
   		{
	        xtype      : 'numberfield'
	    	,fieldLabel : 'Tasa Cambio'
            ,allowDecimals: true
            ,decimalSeparator: '.'
            ,minValue: 0
            ,labelWidth: 150
	    	,name       : 'params.tasacamb1'
    		,listeners : {
   				change:function(e){
   					var cantidad = e.getValue();
   					if(+ cantidad > 0)
  						{
   						panelEdicionConceptos.down('[name="params.ptprecio"]').setValue(+panelEdicionConceptos.down('[name="params.tasacamb1"]').getValue() * +panelEdicionConceptos.down('[name="params.ptpcioex"]').getValue() );
   						panelEdicionConceptos.down('[name="params.destoimp"]').setValue(+panelEdicionConceptos.down('[name="params.tasacamb1"]').getValue() * +panelEdicionConceptos.down('[name="params.dctoimex"]').getValue() );
   						panelEdicionConceptos.down('[name="params.ptimport"]').setValue(+panelEdicionConceptos.down('[name="params.tasacamb1"]').getValue() * +panelEdicionConceptos.down('[name="params.ptimpoex"]').getValue() );
  						}else{
  							panelEdicionConceptos.down('[name="params.ptprecio"]').setValue('0');
  							panelEdicionConceptos.down('[name="params.destoimp"]').setValue('0');
  							panelEdicionConceptos.down('[name="params.ptimport"]').setValue('0');
  						}
   	    		}
   	        }
   		},
        {
       		xtype      : 'numberfield'
   			,fieldLabel : 'Precio Factura'
  			,labelWidth: 150
  			,allowBlank:false
  			,allowDecimals: true
			,decimalSeparator: '.'
			,minValue: 0
			,name       : 'params.ptprecio'
			,listeners: {
					change: calculaImporteConcepto
			}
   		},
   		{
	        xtype      : 'numberfield'
	    	,fieldLabel : 'Precio Ext'
    		,labelWidth: 150
    		,allowBlank:false
    		,allowDecimals: true
               ,decimalSeparator: '.'
               ,minValue: 0
	    	,name       : 'params.ptpcioex'
	    	/*,listeners: {
	    		change: calculaImporteConcepto
	    	}*/
    		,listeners : {
  				change:function(e){
  						panelEdicionConceptos.down('[name="params.ptprecio"]').setValue(+panelEdicionConceptos.down('[name="params.tasacamb1"]').getValue() * +panelEdicionConceptos.down('[name="params.ptpcioex"]').getValue() );
  						calculaImporteConcepto();
  	    		}
   	        }
		}
            ,{
		       		 xtype      : 'numberfield'
			    	,fieldLabel : 'Cantidad'
		    		,labelWidth: 150
		    		,allowBlank:false
	                ,minValue: 0
			    	,name       : 'params.cantidad'
			    	,listeners: {
				    		change: calculaImporteConcepto
				    }
	    	}
	         ,
            {
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Descuento (%)'
	    		,labelWidth: 150
                ,minValue: 0
	    		,allowBlank:false
	    		,allowDecimals: true
                ,decimalSeparator: '.'
		    	,name       : 'params.destopor'
		    	,listeners: {
			    		change: calculaImporteConcepto
			    }
    		},
            {
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Descuento Importe'
	    		,labelWidth: 150
	    		,allowBlank:false
	    		,allowDecimals: true
                ,decimalSeparator: '.'
                ,minValue: 0
		    	,name       : 'params.destoimp'
		    	,listeners: {
			    		change: calculaImporteConcepto
			    }
    		},
    		{
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Descuento Importe Ext'
	    		,labelWidth: 150
	    		,allowBlank:false
	    		,allowDecimals: true
                ,decimalSeparator: '.'
                ,minValue: 0
		    	,name       : 'params.dctoimex'
	    		,listeners : {
	  				change:function(e){
	  						panelEdicionConceptos.down('[name="params.destoimp"]').setValue(+panelEdicionConceptos.down('[name="params.tasacamb1"]').getValue() * +panelEdicionConceptos.down('[name="params.dctoimex"]').getValue() );
	  						calculaImporteConcepto();
	  	    		}
	   	        }
    			/*,listeners: {
			    		change: calculaImporteConcepto
			    }*/
    		}
            ,
            {
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Subtotal factura Ext'
	    		,labelWidth: 150
	    		,allowBlank:false
	    		,allowDecimals: true
                ,decimalSeparator: '.'
                ,readOnly: true
                ,minValue: 0
		    	,name       : 'params.ptimpoex'
    		},
            {
		        xtype      : 'numberfield'
		    	,fieldLabel : 'Subtotal factura'
	    		,labelWidth: 150
	    		,allowBlank:false
	    		,allowDecimals: true
                ,decimalSeparator: '.'
                ,readOnly: true
                ,minValue: 0
		    	,name       : 'params.ptimport'
    		}
        ]
    });
    
    function calculaImporteConcepto(){
    	
    	var cantidad = panelEdicionConceptos.down('[name="params.cantidad"]').getValue()*1;
    	
    	var importe = panelEdicionConceptos.down('[name="params.ptprecio"]').getValue()*1;
    	var destopor = panelEdicionConceptos.down('[name="params.destopor"]').getValue()*1;
    	var destoimp = panelEdicionConceptos.down('[name="params.destoimp"]').getValue()*1;
    	panelEdicionConceptos.down('[name="params.ptimport"]').setValue(((cantidad*importe) *(1-(destopor/100)))-destoimp);
    	
    	var importeEx = panelEdicionConceptos.down('[name="params.ptpcioex"]').getValue()*1;
    	var destoporEx = panelEdicionConceptos.down('[name="params.destopor"]').getValue()*1;
    	var destoimpEx = panelEdicionConceptos.down('[name="params.dctoimex"]').getValue()*1;
    	
    	panelEdicionConceptos.down('[name="params.ptimpoex"]').setValue(((cantidad*importeEx) *(1-(destoporEx/100)))-destoimpEx);
    }

    
    //jtezva
    _revAdm_formRechazo   = new _revAdm_FormRechazo();
    _revAdm_windowRechazo = new _revAdm_WindowRechazo();

	/*PANTALLA EMERGENTE PARA LA INSERCION Y MODIFICACION DE LOS DATOS DEL GRID*/
    var windowConceptos= Ext.create('Ext.window.Window', {
          title: 'Agregar Concepto',
          closeAction: 'hide',
          modal:true,
          items:[panelEdicionConceptos],
          bodyStyle:'padding:15px;',
          buttons:[{
                 text: 'Guardar',
                 icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                 handler: function() {
                       if (panelEdicionConceptos.form.isValid()) {
                    	   var record = gridFacturas.getSelectionModel().getSelection()[0];
                    	   
                    	   panelEdicionConceptos.form.submit({
           		        	waitMsg:'Procesando...',			
           		        	params: {
           		        		'params.cdunieco'   : _CDUNIECO,
           		        		'params.cdramo'     : _CDRAMO,
           		        		'params.estado'     : _ESTADO,
           		        		'params.nmpoliza'   : _NMPOLIZA,
           		        		'params.nmsituac'   : _NMSITUAC,
           		        		'params.nmsuplem'   : _NMSUPLEM,
           		        		'params.status'     : _STATUS,
           		        		'params.aaapertu'   : _AAAPERTU,
           		        		'params.nmsinies'   : _NMSINIES,
           		        		'params.nfactura'   : record.get('NFACTURA'),
           		        		'params.cdgarant'   : record.get('CDGARANT'),
           		        		'params.nmordina'   : _Operacion == 'U'? _Nmordina:'',
           		        		'params.operacion'  : _Operacion,
           		        		'params.ptimporta'  :record.get('PTIMPORTA')
           		        	},
           		        	failure: function(form, action) {
           		        		centrarVentanaInterna(mensajeError("Error al guardar el concepto"));
           					},
           					success: function(form, action) {
           						
           						storeConceptos.reload();
           						panelEdicionConceptos.getForm().reset();
                               	windowConceptos.close();
                               	centrarVentanaInterna(mensajeCorrecto("Aviso","Se ha guardado el concepto."));
           						
           						
           					}
           				});
                       	
                       } else {
                    	   centrarVentanaInterna(Ext.Msg.show({
                                  title: 'Aviso',
                                  msg: 'Complete la informaci&oacute;n requerida',
                                  buttons: Ext.Msg.OK,
                                  icon: Ext.Msg.WARNING
                              }));
                       }
                   }
             },
           {
                 text: 'Cancelar',
                 icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                 handler: function() {
                	 panelEdicionConceptos.getForm().reset();
                     windowConceptos.close();
                 }
           }
          ]
          });
    
    var windowFacturas= Ext.create('Ext.window.Window', {
           title: 'Agregar Factura',
           bodyStyle:'padding:5px;',
           closeAction: 'hide',
           modal: true,
           items:[panelEdicionFacturas],
           
           buttons:[{
                  text: 'Guardar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                  handler: function() {
                        if (panelEdicionFacturas.form.isValid()) {
                        	
                        	var UrlFact;
                        	if(_Operacion == 'I'){
                        		UrlFact = null;
                        	}
                        	//Obtenemos los valores
                        	var tipoMoneda = panelEdicionFacturas.down('[name="params.tipoMoneda"]').getValue();
                        	var importe    = panelEdicionFacturas.down('[name="params.ptimport"]').getValue();
                        	var descuento  = panelEdicionFacturas.down('[name="params.descnume"]').getValue();
                        	
                        	if(tipoMoneda == '001'){
                        		panelEdicionFacturas.down('[name="params.ptimporta"]').setValue('0');
                        		panelEdicionFacturas.down('[name="params.dctonuex"]').setValue('0');
                        		panelEdicionFacturas.down('[name="params.ptimport"]').setValue(importe);
                        		panelEdicionFacturas.down('[name="params.descnume"]').setValue(descuento);
                        		
                        	}
                        	
                        	var autoMed = panelEdicionFacturas.down('[name="params.autrecla"]');
                        	if(autoMed)
                        	{
                        		autoMed = autoMed.getValue();
                        	}
                        	var autoRec = panelEdicionFacturas.down('[name="params.autmedic"]');
                        	if(autoRec)
                        	{
                        		autoRec = autoRec.getValue();
                        	}
                        	debug('autoMed',autoMed,'autoRec',autoRec);
                        	var cancelar = (autoMed && autoMed =='N') || (autoRec && autoRec == 'N');
                        	debug('cancelar sin tipopago',cancelar ? 'si' : 'no');
                        	cancelar = cancelar &&(Ext.isEmpty(_TIPOPAGO) || _TIPOPAGO == _PAGO_DIRECTO);
                        	debug('cancelar con tipopago',cancelar ? 'si' : 'no');
                        	if(cancelar)
                        	{
                        		//jtezva
                        		centrarVentanaInterna(mensajeWarning(
						                'El tr&aacute;mite de pago directo ser&aacute; cancelado debido a que no ha sido autorizada alguna de las facturas'
						                ,function(){_revAdm_windowRechazo.show();centrarVentanaInterna(_revAdm_windowRechazo);}
						        ));
                        	}
                        	else
                        	{
	                        	panelEdicionFacturas.form.submit({
	            		        	waitMsg:'Procesando...',	
	            		        	url: UrlFact,
	            		        	params: {
	            		        		'params.ntramite'   : _NTRAMITE,
	            		        		'params.cdunieco'  : _CDUNIECO,
	            		        		'params.cdramo'    : _CDRAMO,
	            		        		'params.estado'    : _ESTADO,
	            		        		'params.nmpoliza'  : _NMPOLIZA,
	            		        		'params.nmsituac'  : _NMSITUAC,
	            		        		'params.nmsuplem'  : _NMSUPLEM,
	            		        		'params.status'    : _STATUS,
	            		        		'params.aaapertu'  : _AAAPERTU,
	            		        		'params.nmsinies'  : _NMSINIES,
	            		        		'params.cdtipsit'  : _CDTIPSIT,
	            		        		'params.operacion'  : _Operacion
	            		        	},
	            		        	failure: function(form, action) {
	            		        		centrarVentanaInterna(mensajeError("Error al guardar la Factura"));
	            					},
	            					success: function(form, action) {
	            						
	            						storeFacturas.reload();
	            						panelEdicionFacturas.getForm().reset();
	            						storeConceptos.removeAll();
	                                	windowFacturas.close();
	                                	centrarVentanaInterna(mensajeCorrecto("Aviso","Se ha guardado la Factura"));	
	            					}
	            				});	
                        	}
                        } else {
                        	centrarVentanaInterna(Ext.Msg.show({
                                   title: 'Aviso',
                                   msg: 'Complete la informaci&oacute;n requerida',
                                   buttons: Ext.Msg.OK,
                                   icon: Ext.Msg.WARNING
                               }));
                        }
                    }
              },
            {
                  text: 'Cancelar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                  handler: function() {
                      panelEdicionFacturas.getForm().reset();
                      windowFacturas.close();
                  }
            }
           ]
           });    

	
/*////////////////////////////////////////////////////////////////
////////////////   DECLARACION DE GRID FACTURAS ////////////
///////////////////////////////////////////////////////////////*/
Ext.define('EditorFacturas', {
		extend: 'Ext.grid.Panel',
 		requires: [
	 	'Ext.selection.CellModel',
	 	'Ext.grid.*',
	 	'Ext.data.*',
	 	'Ext.util.*',
	 	'Ext.form.*'
 	],
		selType: 'checkboxmodel',
		title: 'Facturas en Tr&aacute;mite',
		frame: false,
 		initComponent: function(){
 		this.cellEditing = new Ext.grid.plugin.CellEditing({
 		clicksToEdit: 1
 		});

 			Ext.apply(this, {
 			height: 250,
 			plugins: [this.cellEditing],
 			store: storeFacturas,
 			columns: 
 				[{
 					xtype : 'actioncolumn',
 					width : 50,
 					sortable : false,
 					menuDisabled : true,
 					items : [{
 						icon : _CONTEXT+'/resources/fam3icons/icons/pencil.png',
 						tooltip : 'Editar Factura',
 						scope : this,
 						handler : this.onEditClick
 					},{
 						icon : _CONTEXT+'/resources/fam3icons/icons/delete.png',
 						tooltip : 'Eliminar Factura',
 						scope : this,
 						handler : this.onRemoveClick
 					}]
 				},{
 					header : 'C&oacute;digo de Reclamaci&oacute;n',
 					dataIndex : 'CODRECLAM',
 					width : 150
 				},{
 					header : 'No. de Factura',
 					dataIndex : 'NFACTURA',
 					width : 150
 				},{
 					header : 'Fecha de Factura',
 					dataIndex : 'FFACTURA',
 					width : 150
 				// , renderer: Ext.util.Format.dateRenderer('d M Y')

 				},{
 					header : 'Cobertura',
 					dataIndex : 'DSGARANT',
 					width : 150
 				},{
 					header : 'Importe',
 					dataIndex : 'PTIMPORT',
 					width : 150,
 					renderer : Ext.util.Format.usMoney
 				},{
 					header : 'Moneda',
 					dataIndex : 'DESTIPOMONEDA',
 					width : 150,
 					hidden: false
 				},{
 					header : 'Tasa cambio',
 					dataIndex : 'TASACAMB',
 					width : 150,
 					hidden: false,
 					renderer : Ext.util.Format.usMoney
 				},{
 					header : 'Deducible',
 					dataIndex : 'DEDUCIBLE',
 					width : 120
 				},{
 					header : 'Copago',
 					dataIndex : 'COPAGO',
 					width : 120
 				} <s:property value='%{"," + imap.gridColumns}' />
 				,{
 					header : 'Moneda',
 					dataIndex : 'CDMONEDA',
 					width : 150,
 					hidden: true
 				},{
 					header : 'Importe Ext',
 					dataIndex : 'PTIMPORTA',
 					width : 150,
 					hidden: true
 				},{
 					header : 'DESC EXT',
 					dataIndex : 'DCTONUEX',
 					width : 150,
 					hidden: true
 				},{
 					header : 'Aplica IVa',
 					dataIndex : 'APLICA_IVA',
 					width : 150
 					,hidden: true
 				},{
 					header : 'Antes_DEspues',
 					dataIndex : 'ANTES_DESPUES',
 					width : 150
 					,hidden: true
 				},
 				{
 					header : 'IVA Retenido',
 					dataIndex : 'IVARETENIDO',
 					width : 150
 					,hidden: true
 				},{
 					header : 'CDPRESTA',
 					dataIndex : 'CDPRESTA',
 					width : 150,
 					hidden: true
 				} ],
	 		tbar: [{
			 	icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
			 	text: 'Agregar Factura',
			 	scope: this,
			 	hidden: (Ext.isEmpty(_TIPOPAGO) || _TIPOPAGO == _PAGO_DIRECTO),
			 	handler: this.onAddClick
	 		}]
	 	});
			this.callParent();
 	},
 	getColumnIndexes: function () {
	 	var me, columnIndexes;
	 	me = this;
	 	columnIndexes = [];
	 	Ext.Array.each(me.columns, function (column)
	 	{
	 		if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
	 			columnIndexes.push(column.dataIndex);
		 	} else {
		 		columnIndexes.push(undefined);
		 	}
	 	});
	 	return columnIndexes;
 	},
 	validateRow: function (columnIndexes,record, y)
 	//hace que una celda de columna con allowblank=false tenga el estilo rojito
 	{
	 	var view = this.getView();
	 	Ext.each(columnIndexes, function (columnIndex, x)
	 	{
	 		if(columnIndex)
		 	{
		 		var cell=view.getCellByPosition({row: y, column: x});
		 		cellValue=record.get(columnIndex);
			 	if(cell.addCls&&((!cellValue)||(cellValue.lenght==0))){
			 		cell.addCls("custom-x-form-invalid-field");
			 	}
		 	}
	 	});
	 	return false;
 	},
 	onAddClick: function(){	 
 		panelEdicionFacturas.getForm().reset();
 		panelEdicionFacturas.down('[name="params.nfactura"]').setReadOnly(false);
 		panelEdicionFacturas.down('[name="params.cdgarant"]').setReadOnly(false);
 		panelEdicionFacturas.down('[name="params.tipoMoneda"]').setValue('001');
 		panelEdicionFacturas.down('[name="parametros.pv_otvalor01"]').setValue("S");
		panelEdicionFacturas.down('[name="parametros.pv_otvalor02"]').setValue("D");
 		panelEdicionFacturas.down('[name="parametros.pv_otvalor03"]').setValue("N");
 		//xca<<<<
 		//panelEdicionFacturas.down('[name="params.tipoMoneda"]').setValue('001');
 		panelEdicionFacturas.down('[name="params.tasacamb"]').setValue('0');
 		panelEdicionFacturas.down('[name="params.ptimporta"]').setValue('0');
 		panelEdicionFacturas.down('[name="params.ptimport"]').setValue('0');
 		panelEdicionFacturas.down('[name="params.descnume"]').setValue('0');
 		panelEdicionFacturas.down('[name="params.dctonuex"]').setValue('0');
 		panelEdicionFacturas.down('[name="params.descporc"]').setValue('0');
 		
 		
 		_Operacion = 'I';
 		
 		windowFacturas.setTitle('Agregar Factura');
 		windowFacturas.show();
 		centrarVentanaInterna(windowFacturas);
 		
 	},
 	onRemoveClick: function(grid, rowIndex){
 		if(Ext.isEmpty(_TIPOPAGO) || _TIPOPAGO == _PAGO_DIRECTO){
 			centrarVentanaInterna(mensajeWarning('No se pueden eliminar Facturas en Pago Directo.'));
 			return;
 		}
 		
 		var record=grid.getStore().getAt(rowIndex);
 		
 		panelEdicionFacturas.getForm().reset();
 		
 		centrarVentanaInterna(Ext.Msg.show({
	        title: 'Aviso',
	        msg: '&iquest;Esta seguro que desea eliminar esta factura?',
	        buttons: Ext.Msg.YESNO,
	        icon: Ext.Msg.QUESTION,
	        fn: function(buttonId, text, opt){
	        	if(buttonId == 'yes'){
	        		
	        		gridFacturas.setLoading(true);
	         		
	         		Ext.Ajax.request({
	        			url: _URL_EliminaFactura,
	        			params: {
	        		    		'params.ntramite' : _NTRAMITE,
	        		    		'params.nfactura' : record.get('NFACTURA')
	        			},
	        			success: function(response) {
	        				var res = Ext.decode(response.responseText);
	        				gridFacturas.setLoading(false);
	        				storeFacturas.reload();
	        				if(res.success){
	        					centrarVentanaInterna(mensajeCorrecto('Aviso','Se ha eliminado con &eacute;xito.'));
	    	    				storeFacturas.reload();
	        				}else {
	        					centrarVentanaInterna(mensajeError('No se pudo eliminar.'));	
	        				}
	        			},
	        			failure: function(){
	        				gridFacturas.setLoading(false);
	        				centrarVentanaInterna(mensajeError('No se pudo eliminar.'));
	        			}
	        		});
	        	}
	        	
	        }
	    }));
 		
 		
 	},
 	onEditClick: function(grid, rowIndex){
 		var record=grid.getStore().getAt(rowIndex);
 		
 		if(Ext.isEmpty(_TIPOPAGO) || _TIPOPAGO == _PAGO_DIRECTO){
 			panelEdicionFacturas.query('numberfield[name=params.ptimporta]')[0].hide();
			panelEdicionFacturas.query('numberfield[name=params.tasacamb]')[0].hide();
			panelEdicionFacturas.query('combo[name=params.tipoMoneda]')[0].hide();
			panelEdicionFacturas.query('numberfield[name=params.dctonuex]')[0].hide();
 		}
 		_Operacion = 'U';
 		debug("Editando...");
 		
 		panelEdicionFacturas.getForm().reset();
 		
 		panelEdicionFacturas.down('[name="params.nfactura"]').setReadOnly(true);
 		panelEdicionFacturas.down('[name="params.cdgarant"]').setReadOnly(false); // true
 		panelEdicionFacturas.down('[name="params.nfactura"]').setValue(record.get('NFACTURA'));
 		panelEdicionFacturas.down('[name="params.fefactura"]').setValue(record.get('FFACTURA'));
 		panelEdicionFacturas.down('[name="params.cdtipser"]').setValue(record.get('CDTIPSER'));
 		panelEdicionFacturas.down('[name="params.cdpresta"]').setValue(record.get('CDPRESTA'));
 		panelEdicionFacturas.down('[name="params.cdgarant"]').setValue(record.get('CDGARANT'));
 		panelEdicionFacturas.down('[name="params.tipoMoneda"]').setValue(record.get('CDMONEDA'));
 		panelEdicionFacturas.down('[name="params.tasacamb"]').setValue(record.get('TASACAMB') == null || record.get('TASACAMB') == ''? "0":record.get('TASACAMB'));
 		panelEdicionFacturas.down('[name="params.ptimporta"]').setValue(record.get('PTIMPORTA') == null || record.get('PTIMPORTA') == ''? "0":record.get('PTIMPORTA'));
 		panelEdicionFacturas.down('[name="params.dctonuex"]').setValue(record.get('DCTONUEX') == null || record.get('DCTONUEX') == ''? "0":record.get('DCTONUEX'));
 		
 		if(record.get('APLICA_IVA') == null || record.get('APLICA_IVA') ==""){
 			panelEdicionFacturas.down('[name="parametros.pv_otvalor01"]').setValue("S");
 			panelEdicionFacturas.down('[name="parametros.pv_otvalor02"]').setValue("D");
 			panelEdicionFacturas.down('[name="parametros.pv_otvalor03"]').setValue("N");
 		}else{
 			panelEdicionFacturas.down('[name="parametros.pv_otvalor01"]').setValue(record.get('APLICA_IVA'));
 	 		panelEdicionFacturas.down('[name="parametros.pv_otvalor02"]').setValue(record.get('ANTES_DESPUES'));
 	 		//panelEdicionFacturas.down('[name="parametros.pv_otvalor03"]').setValue(record.get('IVARETENIDO'));
 	 		if(record.get('IVARETENIDO')== null || record.get('IVARETENIDO') ==''){
	 			panelEdicionFacturas.down('[name="parametros.pv_otvalor03"]').setValue("N");
 			}else{
 				panelEdicionFacturas.down('[name="parametros.pv_otvalor03"]').setValue(record.get('IVARETENIDO'));
 			}
 			
 	 		panelEdicionFacturas.query('combo[name=parametros.pv_otvalor02]')[0].show();
 	 		panelEdicionFacturas.query('combo[name=parametros.pv_otvalor03]')[0].show();
 	 		if(record.get('APLICA_IVA') =="S"){
 	 			panelEdicionFacturas.query('combo[name=parametros.pv_otvalor02]')[0].show();
 	 			panelEdicionFacturas.query('combo[name=parametros.pv_otvalor03]')[0].show();
 	 		}else{
 	 			panelEdicionFacturas.down('[name="parametros.pv_otvalor03"]').setValue("N");
 	 			panelEdicionFacturas.query('combo[name=parametros.pv_otvalor02]')[0].hide();
 	 			panelEdicionFacturas.query('combo[name=parametros.pv_otvalor03]')[0].hide();
 	 		}
 		}
 		
 		panelPrincipal.setLoading(true);
 		storeSubcoberturas.load({
			params: {
				'params.cdgarant' : record.get('CDGARANT')
			},
			callback: function (){ 
				panelEdicionFacturas.down('[name="params.cdconval"]').setValue(record.get('CDCONVAL'));
				panelPrincipal.setLoading(false);
				
				windowFacturas.setTitle('Editar Factura');
				windowFacturas.show();
				centrarVentanaInterna(windowFacturas);
			}
		});
 		panelEdicionFacturas.down('[name="params.ptimport"]').setValue(record.get('PTIMPORT') == null || record.get('PTIMPORT') == ''? "0":record.get('PTIMPORT'));
 		panelEdicionFacturas.down('[name="params.descporc"]').setValue(record.get('DESCPORC') == null || record.get('DESCPORC') == ''? "0":record.get('DESCPORC'));
 		panelEdicionFacturas.down('[name="params.descnume"]').setValue(record.get('DESCNUME') == null || record.get('DESCNUME') == ''? "0":record.get('DESCNUME'));

 		if(!Ext.isEmpty(panelEdicionFacturas.down('[name="params.autrecla"]'))){
 			panelEdicionFacturas.down('[name="params.autrecla"]').setValue(record.get('AUTRECLA'));
 		}
 		if(!Ext.isEmpty(panelEdicionFacturas.down('[name="params.commenar"]'))){
 			panelEdicionFacturas.down('[name="params.commenar"]').setValue(record.get('COMMENAR'));
 		}
 		if(!Ext.isEmpty(panelEdicionFacturas.down('[name="params.autmedic"]'))){
 			panelEdicionFacturas.down('[name="params.autmedic"]').setValue(record.get('AUTMEDIC'));
 		}
 		if(!Ext.isEmpty(panelEdicionFacturas.down('[name="params.commenme"]'))){
 			panelEdicionFacturas.down('[name="params.commenme"]').setValue(record.get('COMMENME'));
 		}
 		
 	},
 	listeners: {
 		select: function (grid, record, index, opts){
 			debug(record.get('NFACTURA'));
 			storeConceptos.load({
 				params: {
 		    		'params.nfactura'  : record.get('NFACTURA'),
 		    		'params.cdunieco'  : _CDUNIECO,
 		    		'params.cdramo'    : _CDRAMO,
 		    		'params.estado'    : _ESTADO,
 		    		'params.nmpoliza'  : _NMPOLIZA,
 		    		'params.nmsituac'  : _NMSITUAC,
 		    		'params.nmsuplem'  : _NMSUPLEM,
 		    		'params.status'    : _STATUS,
 		    		'params.aaapertu'  : _AAAPERTU,
 		    		'params.nmsinies'  : _NMSINIES
 		    	}
 			});
 		}
 	}
	});

gridFacturas=new EditorFacturas();


/*////////////////////////////////////////////////////////////////
////////////////   DECLARACION DE GRID CONCEPTOS  ////////////
///////////////////////////////////////////////////////////////*/
Ext.define('EditorConceptos', {
		extend: 'Ext.grid.Panel',
 	requires: [
	 	'Ext.selection.CellModel',
	 	'Ext.grid.*',
	 	'Ext.data.*',
	 	'Ext.util.*',
	 	'Ext.form.*'
 	],
		title: 'Conceptos en Factura',
		frame: false,

 	initComponent: function(){
 		this.cellEditing = new Ext.grid.plugin.CellEditing({
 		clicksToEdit: 1
 		});

 			Ext.apply(this, {
 			height: 250,
 			plugins: [this.cellEditing],
 			store: storeConceptos,
 			columns: 
 				[{
 					xtype : 'actioncolumn',
 					width : 80,
 					sortable : false,
 					menuDisabled : true,
 					items : [<s:property value="imap.conceptosButton" />]
 				},{
 					dataIndex : 'NMORDINA',
 					width : 20,
 					hidden: true
 				},{
 					header : 'Factura',
 					dataIndex:  'NFACTURA',
 					hidden: true
 				},{
 					header : 'Tipo Concepto',
 					dataIndex : 'DESIDCONCEP',
 					width : 150
 				},{
 					header : 'Codigo Concepto',
 					dataIndex : 'DESCONCEP',
 					width : 150
 				},{
 					header : 'Subcobertura',
 					dataIndex : 'DSSUBGAR',
 					width : 150
 				},{
 					header : 'Precio',
 					dataIndex : 'PTPRECIO',
 					width : 150,
 					renderer : Ext.util.Format.usMoney
 				},{
 					header : 'Cantidad',
 					dataIndex : 'CANTIDAD',
 					width : 150
 				},{
 					header : 'Descuento (%)',
 					dataIndex : 'DESTOPOR',
 					width : 150
 				},{
 					header : 'Descuento ($)',
 					dataIndex : 'DESTOIMP',
 					width : 150
 				},{
 					header : 'Ajuste M&eacute;dico',
 					dataIndex : 'TOTAJUSMED',
 					width : 150,
 					renderer : Ext.util.Format.usMoney
 				},{
 					header : 'Subtotal Factura',
 					dataIndex : 'PTIMPORT',
 					width : 150,
 					renderer : Ext.util.Format.usMoney
 				},{
 					header : 'Subtotal Ajustado',
 					dataIndex : 'SUBTAJUSTADO',
 					width : 150,
 					renderer : Ext.util.Format.usMoney
 				},{
 					header : 'Valor Arancel',
 					dataIndex : 'PTMTOARA',
 					width : 150,
 					renderer : Ext.util.Format.usMoney
 				} ],
	 		tbar: [{
			 	icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
			 	text: 'Agregar concepto',
			 	scope: this,
			 	handler: this.onAddClick
	 		}]
	 	});
			this.callParent();
 	},
 	getColumnIndexes: function () {
	 	var me, columnIndexes;
	 	me = this;
	 	columnIndexes = [];
	 	Ext.Array.each(me.columns, function (column)
	 	{
	 		if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
	 			columnIndexes.push(column.dataIndex);
		 	} else {
		 		columnIndexes.push(undefined);
		 	}
	 	});
	 	return columnIndexes;
 	},
 	validateRow: function (columnIndexes,record, y)
 	//hace que una celda de columna con allowblank=false tenga el estilo rojito
 	{
	 	var view = this.getView();
	 	Ext.each(columnIndexes, function (columnIndex, x)
	 	{
	 		if(columnIndex)
		 	{
		 		var cell=view.getCellByPosition({row: y, column: x});
		 		cellValue=record.get(columnIndex);
			 	if(cell.addCls&&((!cellValue)||(cellValue.lenght==0))){
			 		cell.addCls("custom-x-form-invalid-field");
			 	}
		 	}
	 	});
	 	return false;
 	},
 	onAddClick: function(){
 		if(gridFacturas.getSelectionModel().hasSelection()){
 			
 			panelEdicionConceptos.getForm().reset();
 			panelEdicionConceptos.down('[name="params.cdconval"]').setReadOnly(false);
 			panelEdicionConceptos.down('[name="params.idconcep"]').setReadOnly(false);
 			panelEdicionConceptos.down('[name="params.cdconcep"]').setReadOnly(false);
 			_Operacion = 'I';
 			
 			var record = gridFacturas.getSelectionModel().getSelection()[0];
 			
 			if(record.get('CDPRESTA') =="0"){
 				centrarVentanaInterna(Ext.Msg.show({
					title: 'Error',
					msg: "Favor de verificar el proveedor",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
	           	}));
	           	
	           	return false;
 			}
 			
 			
 			//Validamos el tipo de moneda  Pesos 	 --> No aparecer el campo de tipo cambio 
 			//							   Diferente --> Que aparezca el tipo de cambio
 			panelEdicionConceptos.down('[name="params.tasacamb1"]').setReadOnly(false);
 			//Campo para el proveedor de la factura al momento de guardarlo
 			panelEdicionConceptos.down('[name="params.idProvFactura"]').setReadOnly(false);
 			
 			panelEdicionConceptos.down('[name="params.ptprecio"]').setReadOnly(false);
 			panelEdicionConceptos.down('[name="params.destoimp"]').setReadOnly(false);
 			
 			panelEdicionConceptos.query('numberfield[name=params.ptpcioex]')[0].show();
			panelEdicionConceptos.query('numberfield[name=params.dctoimex]')[0].show();
			panelEdicionConceptos.query('numberfield[name=params.ptimpoex]')[0].show();
 			
 			panelEdicionConceptos.down('[name="params.ptpcioex"]').setValue("0");
 			panelEdicionConceptos.down('[name="params.dctoimex"]').setValue("0");
 			panelEdicionConceptos.down('[name="params.ptimpoex"]').setValue("0");
 			
 			
 			panelEdicionConceptos.down('[name="params.idProvFactura"]').setValue(record.get('CDPRESTA'));
 			
 			//panelEdicionConceptos.down('[name="params.cdmonedaM"]').setValue(record.get('CDMONEDA'));
 			if(record.get('CDMONEDA') == '001')
			{
 				panelEdicionConceptos.query('numberfield[name=params.tasacamb1]')[0].hide();
 				panelEdicionConceptos.query('numberfield[name=params.ptpcioex]')[0].hide();
 				panelEdicionConceptos.query('numberfield[name=params.dctoimex]')[0].hide();
 				panelEdicionConceptos.query('numberfield[name=params.ptimpoex]')[0].hide();
			}else{
				panelEdicionConceptos.query('numberfield[name=params.tasacamb1]')[0].show();
				panelEdicionConceptos.down('[name="params.tasacamb1"]').setValue(record.get('TASACAMB'));
				panelEdicionConceptos.down('[name="params.tasacamb1"]').setReadOnly(true);
				if(record.get('TASACAMB') =="0")
				{
					//PRECIO
					panelEdicionConceptos.down('[name="params.ptprecio"]').setValue("0");
					panelEdicionConceptos.down('[name="params.ptprecio"]').setReadOnly(true);
					//DESCUENTO IMPORTE
					panelEdicionConceptos.down('[name="params.destoimp"]').setValue("0");
					panelEdicionConceptos.down('[name="params.destoimp"]').setReadOnly(true);
				}
			}
 			
 			
 			storeSubcoberturasC.load({
    			params: {
    				'params.cdgarant' : record.get('CDGARANT')
    			}
    		});
 			
 			windowConceptos.setTitle('Agregar Concepto');
 			windowConceptos.show();
 			centrarVentanaInterna(windowConceptos);
 		}else {
 			centrarVentanaInterna(mensajeWarning("Debe seleccionar una factura para poder agregar un concepto a la misma."));
 		} 
 	},
 	onEditClick: function(grid, rowIndex){
 		var seleccionFactura = gridFacturas.getSelectionModel().getSelection()[0];
		var record=grid.getStore().getAt(rowIndex);
 		
 		_Operacion = 'U';
 		_Nmordina = record.get('NMORDINA');
 		debug("Editando...");
 		
 		panelEdicionConceptos.getForm().reset();
 		panelPrincipal.setLoading(true);
 		
 		storeSubcoberturasC.load({
			params: {
				'params.cdgarant' : record.get('CDGARANT')
			},
			callback: function (){ 
				panelEdicionConceptos.down('[name="params.cdconval"]').setValue(record.get('CDCONVAL'));
			}
		});
 		
 		panelEdicionConceptos.down('[name="params.idconcep"]').setValue(record.get('IDCONCEP'));
 		
 		storeConceptosCatalogo.load({
			params: {
				'params.idPadre' : record.get('IDCONCEP')
			},
			callback: function (){ 
				panelEdicionConceptos.down('[name="params.cdconcep"]').setValue(record.get('CDCONCEP'));
				panelPrincipal.setLoading(false);
				
				windowConceptos.setTitle('Editar Concepto');
				windowConceptos.show();
				centrarVentanaInterna(windowConceptos);
			}
		});
 		
 		
 		panelEdicionConceptos.down('[name="params.cantidad"]').setValue(record.get('CANTIDAD'));
 		
 		panelEdicionConceptos.down('[name="params.tasacamb1"]').setValue(seleccionFactura.get('TASACAMB'));
 		panelEdicionConceptos.down('[name="params.ptpcioex"]').setValue(record.get('PTPCIOEX'));
 		panelEdicionConceptos.down('[name="params.dctoimex"]').setValue(record.get('DCTOIMEX'));
 		panelEdicionConceptos.down('[name="params.ptimpoex"]').setValue(record.get('PTIMPOEX'));
 		
 		
 		
 		panelEdicionConceptos.down('[name="params.ptprecio"]').setValue(record.get('PTPRECIO'));
 		panelEdicionConceptos.down('[name="params.destopor"]').setValue(record.get('DESTOPOR'));
 		panelEdicionConceptos.down('[name="params.destoimp"]').setValue(record.get('DESTOIMP'));
 		panelEdicionConceptos.down('[name="params.ptimport"]').setValue(record.get('PTIMPORT'));
 		
 		
 		
 		panelEdicionConceptos.down('[name="params.cdconval"]').setReadOnly(true);
 		panelEdicionConceptos.down('[name="params.idconcep"]').setReadOnly(true);
 		panelEdicionConceptos.down('[name="params.cdconcep"]').setReadOnly(true);
 		
 		//PRECIO
		panelEdicionConceptos.down('[name="params.ptprecio"]').setReadOnly(false);
		//DESCUENTO IMPORTE
		panelEdicionConceptos.down('[name="params.destoimp"]').setReadOnly(false);
		
		panelEdicionConceptos.query('numberfield[name=params.ptpcioex]')[0].show();
		panelEdicionConceptos.query('numberfield[name=params.dctoimex]')[0].show();
		panelEdicionConceptos.query('numberfield[name=params.ptimpoex]')[0].show();
		panelEdicionConceptos.down('[name="params.idProvFactura"]').setValue(seleccionFactura.get('CDPRESTA'));
		
 		if(seleccionFactura.get('CDMONEDA')== '001')
		{
 			panelEdicionConceptos.query('numberfield[name=params.tasacamb1]')[0].hide();
 			panelEdicionConceptos.query('numberfield[name=params.ptpcioex]')[0].hide();
 			panelEdicionConceptos.query('numberfield[name=params.dctoimex]')[0].hide();
 			panelEdicionConceptos.query('numberfield[name=params.ptimpoex]')[0].hide();
		}else{
			panelEdicionConceptos.query('numberfield[name=params.tasacamb1]')[0].show();
			panelEdicionConceptos.down('[name="params.tasacamb1"]').setValue(seleccionFactura.get('TASACAMB'));
			panelEdicionConceptos.down('[name="params.tasacamb1"]').setReadOnly(true);
			if(seleccionFactura.get('TASACAMB') =="0")
			{
				//PRECIO
				panelEdicionConceptos.down('[name="params.ptprecio"]').setReadOnly(true);
				//DESCUENTO IMPORTE
				panelEdicionConceptos.down('[name="params.destoimp"]').setReadOnly(true);
			}
		}
 		
 		//console.log(record.get('MONTOARANCEL'));
 		panelEdicionConceptos.down('[name="params.ptprecioArancel"]').setValue(record.get('PTMTOARA'));
 		debug('monto arancel record',record.get('PTMTOARA'))
 		
 	},
 	onRemoveClick: function(grid, rowIndex){
 		var record=grid.getStore().getAt(rowIndex);
 		_Operacion = 'D';
 		
 		centrarVentanaInterna(Ext.Msg.show({
	        title: 'Aviso',
	        msg: '&iquest;Esta seguro que desea eliminar este concepto?',
	        buttons: Ext.Msg.YESNO,
	        icon: Ext.Msg.QUESTION,
	        fn: function(buttonId, text, opt){
	        	if(buttonId == 'yes'){
	        		
	        		gridConceptos.setLoading(true);
	         		
	         		Ext.Ajax.request({
	        			url: _URL_GuardaConcepto,
	        			params         :
	                    {
	                        'params.ntramite'  : _NTRAMITE
	                        ,'params.cdunieco' : _CDUNIECO
	                        ,'params.cdramo'   : _CDRAMO
	                        ,'params.estado'   : _ESTADO
	                        ,'params.nmpoliza' : _NMPOLIZA
	                        ,'params.nmsuplem' : _NMSUPLEM
	                        ,'params.nmsituac' : _NMSITUAC
	                        ,'params.aaapertu' : _AAAPERTU
	                        ,'params.status'   : _STATUS
	                        ,'params.nmsinies' : _NMSINIES
	                        ,'params.nfactura' : record.get('NFACTURA')
	                        ,'params.cdgarant' : record.get('CDGARANT')
	                        ,'params.cdconval' : record.get('CDCONVAL')
	                        ,'params.cdconcep' : record.get('CDCONCEP')
	                        ,'params.idconcep' : record.get('IDCONCEP')
	                        ,'params.nmordina' : record.get('NMORDINA')
	                        ,'params.operacion': _Operacion
	                    },
	        			success: function(response) {
	        				var res = Ext.decode(response.responseText);
	        				gridConceptos.setLoading(false);
	        				storeConceptos.reload();
	        				if(res.success){
	        					centrarVentanaInterna(mensajeCorrecto('Aviso','Se ha eliminado con &eacute;xito.'));
		        				storeConceptos.reload();
	        				}else {
	        					centrarVentanaInterna(mensajeError('No se pudo eliminar.'));
	        				}
	        			},
	        			failure: function(){
	        				gridConceptos.setLoading(false);
	        				centrarVentanaInterna(mensajeError('No se pudo eliminar.'));
	        			}
	        		});
	        	}
	        	
	        }
	    }));
 		
 		
 	}
	});

gridConceptos=new EditorConceptos();


panelPrincipal = Ext.create('Ext.form.Panel',{
	renderTo: 'maindivAdminData',
	border     : false
	,bodyStyle:'padding:5px;'
	,items      : [
        		gridFacturas,
				gridConceptos
	]
	});
	
function _mostrarVentanaAjustes(grid,rowIndex,colIndex){
    var record = grid.getStore().getAt(rowIndex);
    var recordFactura = gridFacturas.getSelectionModel().getSelection()[0];
    debug("Codigo Garantia: "+recordFactura.get('NFACTURA'));
    
    windowLoader = Ext.create('Ext.window.Window',{
        modal       : true,
        buttonAlign : 'center',
        width       : 650,
        height      : 450,
        autoScroll  : true,
        loader      : {
            url     : _UrlAjustesMedicos,
            params         :
            {
                'params.ntramite'  		: _NTRAMITE
                ,'params.cdunieco' 		: _CDUNIECO
                ,'params.cdramo'   		: _CDRAMO
                ,'params.estado'   		: _ESTADO
                ,'params.nmpoliza' 		: _NMPOLIZA
                ,'params.nmsuplem' 		: _NMSUPLEM
                ,'params.nmsituac' 		: _NMSITUAC
                ,'params.aaapertu' 		: _AAAPERTU
                ,'params.status'   		: _STATUS
                ,'params.nmsinies' 		: _NMSINIES
                ,'params.nfactura'		: recordFactura.get('NFACTURA')
                ,'params.cdgarant' 		: record.get('CDGARANT')
                ,'params.cdconval' 		: record.get('CDCONVAL')
                ,'params.cdconcep' 		: record.get('CDCONCEP')
                ,'params.idconcep' 		: record.get('IDCONCEP')
                ,'params.nmordina' 		: record.get('NMORDINA')
                ,'params.precio'   		: record.get('PTPRECIO')
                ,'params.cantidad'      : record.get('CANTIDAD')
                ,'params.descuentoporc' : record.get('DESTOPOR')
                ,'params.descuentonum'  : record.get('DESTOIMP')
                ,'params.importe'       : record.get('PTIMPORT')
                ,'params.ajusteMedi'    : record.get('TOTAJUSMED')
            },
            scripts  : true,
            loadMask : true,
            autoLoad : true,
            ajaxOptions: {
            	method: 'POST'
            }
        }
	    ,
	    listeners:{
	         close:function(){
	             if(true){
	                 //Actualizamos la información de la consulta del grid inferior
	            	 storeConceptos.reload();
	             }
	         }
	    }
    }).show();
    centrarVentanaInterna(windowLoader);
}
        //jtezva
        _revAdm_formRechazo.items.items[1].on('select',function(combo, records, eOpts)
	    {    
        	_revAdm_formRechazo.items.items[2].setValue(records[0].get('value'));
	    });
});

//jtezva
////// funciones //////
function _revAdm_rechazoSiniestro()
{
    debug('_revAdm_rechazoSiniestro');
    
    var valido = _revAdm_formRechazo.isValid();
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
    	var UrlFact;
        if(_Operacion == 'I'){
            UrlFact = null;
        }
        _revAdm_windowRechazo.setLoading(true);
        panelEdicionFacturas.form.submit({    
            url: UrlFact,
            params: {
                'params.ntramite'       : _NTRAMITE,
                'params.cdunieco'       : _CDUNIECO,
                'params.cdramo'         : _CDRAMO,
                'params.estado'         : _ESTADO,
                'params.nmpoliza'       : _NMPOLIZA,
                'params.nmsituac'       : _NMSITUAC,
                'params.nmsuplem'       : _NMSUPLEM,
                'params.status'         : _STATUS,
                'params.aaapertu'       : _AAAPERTU,
                'params.nmsinies'       : _NMSINIES,
                'params.cancelar'       : 'si',
                'params.cdmotivo'       : _revAdm_itemsRechazo[0].getValue(),
                'params.cdsubmotivo'    : _revAdm_itemsRechazo[1].getValue(),
                'params.rechazocomment' : _revAdm_itemsRechazo[2].getValue()
            },
            failure: function(form, action) {
            	_revAdm_windowRechazo.setLoading(false);
            	centrarVentanaInterna(mensajeError("Error al guardar la Factura"));
            },
            success: function(form, action)
            {
            	_revAdm_windowRechazo.setLoading(false);
            	Ext.create('Ext.form.Panel').submit(
                {
                    url             : _revAdm_urlMesaControl
                    ,standardSubmit :true
                    ,params         :
                    {
                        'smap1.gridTitle'      : 'Siniestros'
                        ,'smap2.pv_cdtiptra_i' : 16
                    }
                });
            }
        });
    }
}
////// funciones //////
</script>

<div id="maindivAdminData" style="height:600px;"></div>