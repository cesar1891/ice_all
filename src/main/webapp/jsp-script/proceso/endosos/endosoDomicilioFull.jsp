<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*
_5_smap1:
    cdrfc=MAVA900817001,
    cdperson=511965,
    fenacimi=1990-08-17T00:00:00,
    sexo=H,
    Apellido_Materno=MAT,
    nombre=NOMBRE1,
    nombrecompleto=NOMBRE1  PAT MAT,
    nmsituac=1,
    segundo_nombre=null,
    Parentesco=T,
    CDTIPSIT=SL,
    NTRAMITE=615,
    CDUNIECO=1006,
    CDRAMO=2,
    NMSUPLEM=245673812540000005,
    NMPOLIZA=14,
    swexiper=S,
    NMPOLIEX=1006213000014000000,
    nacional=001,
    activo=true,
    NSUPLOGI=8,
    ESTADO=M,
    cdrol=2,
    tpersona=F,
    Apellido_Paterno=PAT
*/
//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _5_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _5_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

var _5_urlLoadMdomicil = '<s:url namespace="/"        action="cargarPantallaDomicilio" />';
var _5_urlGuardar      = '<s:url namespace="/endosos" action="guardarEndosoDomicilioFull" />';

var _5_panelLectura;
var _5_formDomicil;
var _5_panelEndoso;
//var _5_panelTatriper;
var _5_fieldFechaEndoso;
var _5_panelPri;

debug('_5_smap1:',_5_smap1);
debug('_5_flujo:',_5_flujo);
//////variables //////
//////////////////////

Ext.onReady(function()
{
	/////////////////////
	////// modelos //////
	Ext.define('_5_modeloDomicil',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="imap1.fieldsDomicil" /> ]
	});
    ////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
    ////// stores //////
	////////////////////
	
	/////////////////////////
	////// componentes //////
	/*
	Ext.define('_5_PanelTatriper',
	{
		extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_5_PanelTatriper initComponent');
            Ext.apply(this,
            {
                title   : 'Datos adicionales'
                ,layout :
                {
                	type     : 'table'
                	,columns : 2
                }
                ,items  : [ property value="imap1.itemsTatriper" ]
            });
            this.callParent();
        }
	});
    */
	
	Ext.define('_5_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_5_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,defaults :
                {
                	style : 'margin : 5px;'
                }
                ,items    :
                [
                    _5_fieldFechaEndoso
                ]
            });
            this.callParent();
        }
    });
	
	Ext.define('_5_FormDomicil',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_5_FormDomicil initComponent');
            Ext.apply(this,
            {
                title   : 'Domicilio'
                ,model  : '_5_modeloDomicil'
               	,layout :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items  : [ <s:property value="imap1.itemsDomicil" /> ]
            });
            this.callParent();
        }
    });
	
	Ext.define('_5_PanelLectura',
	{
		extend         : 'Ext.panel.Panel'
		,initComponent : function()
		{
			debug('_5_PanelLectura initComponent');
			Ext.apply(this,
			{
				title      : 'Datos de p&oacute;liza'
				,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
				,items     : [ <s:property value="imap1.itemsLectura" /> ]
				,listeners :
				{
					afterrender : function(me){heredarPanel(me);}
				}
			});
			this.callParent();
		}
	});
    ////// componentes //////
	/////////////////////////
	
	///////////////////////
	////// contenido //////
	_5_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
	{
	    format      : 'd/m/Y'
	    ,fieldLabel : 'Fecha de efecto'
	    ,allowBlank : false
	    ,value      : new Date()
	    ,name       : 'fecha_endoso'
	});
	_5_panelLectura  = new _5_PanelLectura();
	_5_formDomicil   = new _5_FormDomicil();
	_5_panelEndoso   = new _5_PanelEndoso();
	//_5_panelTatriper = new _5_PanelTatriper();
	
	_5_panelPri=Ext.create('Ext.panel.Panel',
	{
		renderTo  : '_5_divPri'
		,border   : 0
		,defaults :
		{
			style : 'margin : 5px;'
		}
	    ,items    :
	    [
	        _5_panelLectura
	        ,_5_formDomicil
	        //,_5_panelTatriper
	        ,_5_panelEndoso
	    ]
	    ,buttonAlign : 'center'
	    ,buttons     :
	    [
	        {
	        	text     : 'Confirmar endoso'
	        	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
	        	,handler : _5_confirmar
	        }
	    ]
	});
    ////// contenido //////
	///////////////////////
	
	////////////////////
	////// loader //////
	_setLoading(true,_5_panelPri);
	Ext.Ajax.request(
	{
		url     : _5_urlLoadMdomicil
		,params :
		{
			'smap1.pv_cdunieco_i'    : _5_smap1.CDUNIECO
            ,'smap1.pv_cdramo_i'     : _5_smap1.CDRAMO
            ,'smap1.pv_estado_i'     : _5_smap1.ESTADO
            ,'smap1.pv_nmpoliza_i'   : _5_smap1.NMPOLIZA
            ,'smap1.pv_nmsituac_i'   : _5_smap1.nmsituac
            ,'smap1.pv_cdperson_i'   : _5_smap1.cdperson
            ,'smap1.pv_cdrol_i'      : _5_smap1.cdrol
            ,'smap1.nombreAsegurado' : ''
            ,'smap1.cdrfc'           : _5_smap1.cdrfc
            ,'smap1.pv_cdtipsit_i'   : _5_smap1.CDTIPSIT
            ,'smap1.pv_nmorddom_i'   : ''// obtiene el asignado a la poliza
		}
	    ,success : function(response)
	    {
	    	_setLoading(false,_5_panelPri);
	    	var json=Ext.decode(response.responseText);
	    	debug('respuesta',json);
	    	_5_formDomicil.loadRecord(new _5_modeloDomicil(json.smap1));
	    	
	    	debug('codpost:',_fieldByName('CODPOSTAL',_5_formDomicil));
	    	debug('colonia:',_fieldByName('CDCOLONI',_5_formDomicil));
	    	_fieldByName('CDCOLONI',_5_formDomicil).setEditable(true);
	    	
	    	//cargar colonia
	    	_fieldByName('CDCOLONI',_5_formDomicil).getStore().load(
            {
                params :
                {
                    'params.cp' : _fieldByName('CODPOSTAL',_5_formDomicil).getValue()
                }
            });
	    	//cargar colonia
	    	
	    	//establecer cargar colonia al cambiar cod pos
	    	_fieldByName('CODPOSTAL',_5_formDomicil).on('blur',function()
	    	{
	    		debug('cod pos change');
	    		_fieldByName('CDCOLONI',_5_formDomicil).getStore().load(
	    		{
	    			params :
	    			{
	    				'params.cp' : _fieldByName('CODPOSTAL',_5_formDomicil).getValue()
	    			}
	    		    ,callback : function()
	    		    {
	    		    	var hay=false;
	    		    	_fieldByName('CDCOLONI',_5_formDomicil).getStore().each(function(record)
	    		    	{
	    		    		if(_fieldByName('CDCOLONI',_5_formDomicil).getValue()==record.get('key'))
	    		    		{
	    		    			hay=true;
	    		    		}
	    		    	});
	    		    	if(!hay)
	    		    	{
	    		    		_fieldByName('CDCOLONI',_5_formDomicil).setValue('');
	    		    	}
	    		    }
	    		});
	    	});
	    	//establecer cargar colonia al cambiar cod pos
	    	
	    	/*
	    	LEIDOS DESDE CURSOR:
		    	CDCOLONI: "137617"
		    	CDEDO: "9600030"
		    	CDMUNICI: "9600030003"
		    	CDPERSON: "511999"
		    	CODPOSTAL: "96000"
		    	DSDOMICI: "FLORES"
		    	Municipio: "ACAYUCAN"
		    	NMNUMERO: "ROSAS"
		    	NMNUMINT: "1"
		    	NMORDDOM: "1"
		    	NMTELEFO: "AA"
		    	estado: "VERACRUZ DE IGNACIO DE LA LLAVE"
	    	ENVIADOS AL PL:
		        String nmordom      = null;-
	            String dsdomici     = null;-
	            String nmtelefo     = null;-
	            String cdpostal     = null;-
	            String cdestado     = null;-
	            String cdmunici     = null;-
	            String cdcoloni     = null;-
	            String nmnumext     = null;-
	            String nmnumint     = null;-
	    	*/
	    }
	    ,failure : function()
	    {
	    	_setLoading(false,_5_panelPri);
	    	errorComunicacion();
	    }
	});
	
	_fieldByName('NMNUMERO').regex = /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/;
    _fieldByName('NMNUMERO').regexText = 'Solo d&iacute;gitos, letras, espacios y guiones';
    _fieldByName('NMNUMINT').regex = /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/;
    _fieldByName('NMNUMINT').regexText = 'Solo d&iacute;gitos, letras, espacios y guiones';
    ////// loader //////
    ////////////////////
});

//////////////////////
//////funciones //////
function _5_confirmar(boton)
{
	debug('_5_confirmar');
	
	var valido=true;
	
	if(valido)
	{
		valido=_5_formDomicil.isValid()&&_5_panelEndoso.isValid();//&&_5_panelTatriper.isValid()
		if(!valido)
		{
			datosIncompletos();
		}
	}
	
	if(valido)
	{
		var json=
		{
			smap1       : _5_smap1
			,smap2      : _5_formDomicil.getValues()
			,smap3      :
			{
				fecha_endoso : Ext.Date.format(_5_fieldFechaEndoso.getValue(),'d/m/Y')
			}
			//,parametros : _5_panelTatriper.getValues()
		};
		
		if(!Ext.isEmpty(_5_flujo))
		{
		    json.flujo = _5_flujo;
		}
		
		debug('datos a enviar:',json);
		_setLoading(true,_5_panelPri);
		Ext.Ajax.request(
		{
			url       : _5_urlGuardar
			,jsonData : json
			,success  : function(response)
			{
				_setLoading(false,_5_panelPri);
				json=Ext.decode(response.responseText);
				if(json.success==true)
				{
					var callbackRemesa = function()
					{
					    //////////////////////////////////
                        ////// usa codigo del padre //////
                        /*//////////////////////////////*/
                        marendNavegacion(2);
                        /*//////////////////////////////*/
                        ////// usa codigo del padre //////
                        //////////////////////////////////
                    };
                    
                    mensajeCorrecto('Endoso generado',json.mensaje,function()
                    {
                    	if(json.endosoConfirmado){
                    		_generarRemesaClic(
	                            true
	                            ,_5_smap1.CDUNIECO
	                            ,_5_smap1.CDRAMO
	                            ,_5_smap1.ESTADO
	                            ,_5_smap1.NMPOLIZA
	                            ,callbackRemesa
	                        );
                    	}else{
                    		callbackRemesa();
                    	}
                    	
                    });
				}
				else
				{
					mensajeError(json.error);
				}
			}
		    ,failure  : function()
		    {
		    	_setLoading(false,_5_panelPri);
		    	errorComunicacion();
		    }
		});
	}
}
//////funciones //////
//////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_5_divPri" style="height:1000px;"></div>