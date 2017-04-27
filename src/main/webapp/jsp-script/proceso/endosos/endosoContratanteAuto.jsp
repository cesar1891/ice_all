<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////

/*
smap1:
    CDRAMO: "2"
    CDTIPSIT: "SL"
    CDUNIECO: "1006"
    DSCOMENT: ""
    DSTIPSIT: "SALUD VITAL"
    ESTADO: "M"
    FEEMISIO: "22/01/2014"
    FEINIVAL: "22/01/2014"
    NMPOLIEX: "1006213000024000000"
    NMPOLIZA: "24"
    NMSUPLEM: "245668019180000000"
    NSUPLOGI: "1"
    NTRAMITE: "678"
    PRIMA_TOTAL: "12207.37"
*/
//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _35_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _35_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

var _35_formLectura;
var _35_formContratante;
var _35_panelPri;
var _35_panelEndoso;
var _35_fieldFechaEndoso;
var _35_storeContratantes;
var _35_gridContratantes;

var _35_urlGuardar     = '<s:url namespace="/endosos" action="guardarEndosoContratante"       />';
var _35_urlLoadContratantes = '<s:url namespace="/endosos" action="cargarContratantesEndosoContratante" />';

var _p35_urlRecuperarCliente = '<s:url namespace="/" action="buscarPersonasRepetidas" />';
var _p35_urlPantallaCliente  = '<s:url namespace="/catalogos" action="includes/personasLoader"/>';

var _p35_urlRecuperacionSimple = '<s:url namespace="/emision"         action="recuperacionSimple"     />';

debug('_35_smap1:',_35_smap1);
debug('_35_flujo:',_35_flujo);

var _cdpersonActual;
var _cdpersonNuevo = '';

var _p22_recuperaCallback;
var ventanaContratante;
var obtieneDatosClienteContratante;
////// variables //////
///////////////////////

Ext.onReady(function()
{
	
	// Se aumenta el timeout para todas las peticiones:
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
//	Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
//	Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
//	Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
	
	var query = Ext.ComponentQuery.query('#_p22_PanelPrincipal');
	if(query.length>0){
		
		
		for(var pos = query.length-1 ; pos>=0; pos--){
		
			var pantallaPersonas = query[pos];
			
			pantallaPersonas.query().forEach(function(element){
				try{
					element.destroy();
				}catch(e){
					debug('Error al destruir en Pantalla de Clientes',e);
				}
			});
		
			try{
				pantallaPersonas.destroy();	
			}catch(e){
				debug('Error al destruir Panel Principal en Pantalla de Clientes',e);
			}
		
			try{
				windowAccionistas.destroy();	
			}catch(e){
				debug('Error al destruir Window accionistas en Pantalla de Clientes',e);
			}
		}
	}
	
	
	///////////////////////
	////// overrides //////
	Ext.override(Ext.form.field.ComboBox,
	{
		initComponent : function()
		{
			debug('Ext.form.field.ComboBox initComponent');
			Ext.apply(this,
			{
				forceSelection : false
			});
			return this.callParent();
		}
	});
	////// overrides //////
	///////////////////////
	
    /////////////////////
    ////// modelos //////
    Ext.define('_35_ModeloContratante',
    {
    	extend  : 'Ext.data.Model'
    	,fields : [ <s:property value="imap1.fieldsModelo" /> ]
    });
    
    Ext.define('_p35_modeloRecuperado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'NOMBRECLI'
            ,'DIRECCIONCLI'
        ]
    });
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    _35_storeContratantes=Ext.create('Ext.data.Store',
    {
		autoLoad : false
    	,model   : '_35_ModeloContratante'
        ,proxy   :
        {
            type    : 'ajax'
            ,reader : 'json'
            ,data   : []
        }
    });
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    Ext.define('_35_GridContratantes',
    {
    	extend         : 'Ext.grid.Panel'
    	,initComponent : function()
    	{
    		debug('_35_GridContratantes initComponent');
    		Ext.apply(this,
    		{
    			title    : 'Datos del Contratante Actual'
    			,store   : _35_storeContratantes
    			,columns : [ <s:property value="imap1.columnsGrid" escapeHtml="false" /> ]
    		});
    		this.callParent();
    	}
    });
    
    Ext.define('_35_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_35_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [
                    _35_fieldFechaEndoso
                ]
            });
            this.callParent();
        }
    });
    
    Ext.define('_35_FormContratante',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_35_FormContratante initComponent');
            Ext.apply(this,
            {
                title       : 'Datos del Nuevo Contratante',
                layout: {
			        type: 'hbox',
			        align: 'stretch'
			    }
//                ,height     : 80
//                ,autoScroll : true
                ,defaults :
                {
                    style : 'margin : 8px;'
                }
				,items     : [
				{
					xtype: 'textfield',
					name:  'nuevoContratante',
					width: 550,
					fieldLabel: 'Nombre',
					allowBlank: false,
					readOnly: true
				},
				{
					xtype: 'button',
					text: 'Seleccionar Cliente',
					handler: function(){
					        ventanaContratante = Ext.create('Ext.window.Window',
					        {
					            title      : 'Recuperar cliente'
					            ,modal     : true
					            ,width     : 800
					            ,height    : 250
					            ,loader    : 
					            {
				                    url       : _p35_urlPantallaCliente
				                    ,scripts  : true
				                    ,autoLoad : true
				                    ,ajaxOptions: {
				                            method: 'POST'
				                     },
				                     params: {
									                'smap1.cdperson' : '',
									                'smap1.cdideper' : '',
									                'smap1.cdideext' : '',
									                'smap1.esSaludDanios' : 'D',
									                'smap1.esCargaClienteNvo' : 'N' ,
									                'smap1.ocultaBusqueda' : 'S' ,
									                'smap1.cargaCP' : '',
									                'smap1.cargaTipoPersona' : '',
									                'smap1.activaCveFamiliar': 'N',
									                'smap1.modoRecuperaDanios': 'S',
									                'smap1.modoSoloEdicion': 'S'
									            }
				                }
					        }).show();
					        centrarVentanaInterna(ventanaContratante);
					    
					}
				}
				]
            });
            this.callParent();
        }
    });
    
    Ext.define('_35_FormLectura',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_35_FormLectura initComponent');
            Ext.apply(this,
            {
                title      : 'Datos de la p&oacute;liza'
                ,hidden    : true
                ,defaults  :
                {
                    style : 'margin : 5px;'
                }
                ,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,listeners :
                {
                    afterrender : function(me){heredarPanel(me);}
                }
                ,items     : [ <s:property value="imap1.itemsPanelLectura" /> ]
            });
            this.callParent();
        }
    });
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    _35_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
    {
        format      : 'd/m/Y'
        ,fieldLabel : 'Fecha de efecto'
        ,allowBlank : false
        ,value      : '<s:property value="smap1.fechaInicioEndoso" />'
        ,name       : 'fecha_endoso'
    });
    _35_panelEndoso = new _35_PanelEndoso();
    _35_formContratante  = new _35_FormContratante();
    _35_formLectura = new _35_FormLectura();
    _35_gridContratantes = new _35_GridContratantes();
    
    _35_panelPri=Ext.create('Ext.panel.Panel',
    {
        renderTo     : '_35_divPri'
        ,defaults    :
        {
            style : 'margin : 5px;'
        }
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text      : 'Confirmar endoso'
                ,icon     : '${ctx}/resources/fam3icons/icons/key.png'
                ,handler  : _35_confirmar
            }
        ]
        ,items       :
        [
            _35_formLectura
            ,_35_gridContratantes
            ,_35_formContratante
            ,_35_panelEndoso
        ]
    });
    ////// contenido //////
    ///////////////////////
    
    ////////////////////
    ////// loader //////
    _35_panelPri.setLoading(true);
    Ext.Ajax.request(
    {
    	url      : _35_urlLoadContratantes
    	,params  :
    	{
    		'smap1.cdunieco'  : _35_smap1.CDUNIECO
    		,'smap1.cdramo'   : _35_smap1.CDRAMO
    		,'smap1.estado'   : _35_smap1.ESTADO
    		,'smap1.nmpoliza' : _35_smap1.NMPOLIZA
    		,'smap1.nmsuplem' : _35_smap1.NMSUPLEM
    	}
    	,success : function (response)
    	{
    		_35_panelPri.setLoading(false);
    		var json=Ext.decode(response.responseText);
    		debug('contratantes cargados:',json);
    		if(json.success==true)
    		{
    			/*
    			a.cdunieco, a.cdramo, a.estado, a.nmpoliza, a.cdcontratante, a.nmsuplem, a.status, a.cdtipoag, porredau, a.porparti,nombre
    			*/
    			_35_storeContratantes.add(json.slist1);
    			
    			_cdpersonActual = _35_storeContratantes.getAt(0).get('CDPERSON');
    			
    		}
    		else
    		{
    			mensajeError(json.error);
    			//////////////////////////////////
                ////// usa codigo del padre //////
                /*//////////////////////////////*/
                marendNavegacion(4);
//                if(!Ext.isEmpty(destruirContLoaderPersona)){
//					destruirContLoaderPersona();	                                
//	            }
                /*//////////////////////////////*/
                ////// usa codigo del padre //////
                //////////////////////////////////
    		}
    	}
        ,failure : function()
        {
        	_35_panelPri.setLoading(false);
        	errorComunicacion();
        }
    });
    ////// loader //////
    ////////////////////
});

///////////////////////
////// funciones //////


_p22_recuperaCallback  =  function (){
	var datosPersona = obtieneDatosClienteContratante();
  
    if(_cdpersonActual == datosPersona.cdperson){
    	
    	_cdpersonNuevo = '';
    	_35_formContratante.down('[name=nuevoContratante]').reset();
    	
    	destruirLoaderContratante();
    	ventanaContratante.destroy();
    	
    	setTimeout(function(){
    		mensajeWarning('El cliente seleccionado es el mismo que se encuentra actualmente registrado en la p&oacute;liza. Seleccione uno distinto');
    	},1000);
    	
    	return;
    }
      
    _cdpersonNuevo = datosPersona.cdperson;
    
    _35_formContratante.down('[name=nuevoContratante]').setValue( datosPersona.nomRecupera );
    
    destruirLoaderContratante();
    ventanaContratante.destroy();
    
};


Ext.Ajax.request(
{
    url      : _p35_urlRecuperacionSimple
    ,params  :
    {
        'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
        ,'smap1.cdunieco'     : _35_smap1.CDUNIECO
        ,'smap1.cdramo'       : _35_smap1.CDRAMO
        ,'smap1.estado'       : _35_smap1.ESTADO
        ,'smap1.nmpoliza'     : _35_smap1.NMPOLIZA
        ,'smap1.cdtipsup'     : _35_smap1.cdtipsup
    }
    ,success : function(response)
    {
        var json = Ext.decode(response.responseText);
        debug('### fechas:',json);
        if(json.exito)
        {
            _fieldByName('fecha_endoso').setMinValue(json.smap1.FECHA_MINIMA);
            _fieldByName('fecha_endoso').setMaxValue(json.smap1.FECHA_MAXIMA);
            _fieldByName('fecha_endoso').setReadOnly(json.smap1.EDITABLE=='N');
            _fieldByName('fecha_endoso').isValid();
        }
        else
        {
            mensajeError(json.respuesta);
        }
    }
    ,failure : function()
    {
        errorComunicacion();
    }
});

function _35_confirmar()
{
    debug('_35_confirmar');
    
    var valido=true;
    
    if(valido)
    {
        valido=!Ext.isEmpty(_cdpersonNuevo);
        if(!valido)
        {
            mensajeWarning('Seleccione el nuevo Contratante.');
        }
    }
    
    if(valido)
    {
        /*
        a.cdunieco, a.cdramo, a.estado, a.nmpoliza, a.cdcontratante, a.nmsuplem, a.status, a.cdtipoag, porredau, a.porparti,nombre,cdsucurs,nmcuadro
        */
        var slist1=[];
        _35_storeContratantes.each(function(record)
        {
        	slist1.push(record.raw);
        });
        var json=
        {
            smap1   : _35_smap1
            ,smap2  :
            {
                'fecha_endoso'     : Ext.Date.format(_35_fieldFechaEndoso.getValue(),'d/m/Y'),
                'cdpersonNvoContr' : _cdpersonNuevo
            }
            ,slist1 : slist1
        }
        
        if(!Ext.isEmpty(_35_flujo))
        {
            json.flujo = _35_flujo;
        }
        
        debug('datos que se enviaran:',json);
        var panelMask = new Ext.LoadMask('_35_divPri', {msg:"Confirmando..."});
		panelMask.show();
		
        Ext.Ajax.request(
        {
            url       : _35_urlGuardar
            ,jsonData : json
            ,success  : function(response)
            {
                panelMask.hide();
                json=Ext.decode(response.responseText);
                debug('datos recibidos:',json);
                if(json.success==true)
                {
                    var callbackRemesa = function()
                    {
                        //////////////////////////////////
                        ////// usa codigo del padre //////
                        /*//////////////////////////////*/
                        marendNavegacion(2);
                    
    //                    if(!Ext.isEmpty(destruirContLoaderPersona)){
    //							destruirContLoaderPersona();	                                
    //	                }
                        /*//////////////////////////////*/
                        ////// usa codigo del padre //////
                        //////////////////////////////////
                    };
                    
                    mensajeCorrecto('Endoso generado',json.mensaje,function()
                    {
                        _generarRemesaClic(
                            true
                            ,_35_smap1.CDUNIECO
                            ,_35_smap1.CDRAMO
                            ,_35_smap1.ESTADO
                            ,_35_smap1.NMPOLIZA
                            ,callbackRemesa
                        );
                    });
                    
                }
                else
                {
                    mensajeError(json.error);
                }
            }
            ,failure  : function()
            {
                panelMask.hide();
                errorComunicacion();
            }
        });
    }
};
////// funciones //////
///////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_35_divPri" style="height:1000px;"></div>