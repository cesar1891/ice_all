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

var _35_urlGuardar     = '<s:url namespace="/endosos" action="guardarEndosoRfcCliente"       />';
var _35_urlLoadContratantes = '<s:url namespace="/endosos" action="cargarContratantesEndosoContratante" />';

debug('_35_smap1:',_35_smap1);

debug('_35_flujo:',_35_flujo);

var nombreInicial;

var _p35_urlRecuperacionSimple = '<s:url namespace="/emision" action="recuperacionSimple" />';
var _cdTipSupCambioRFC = '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@CAMBIO_RFC_CLIENTE.cdTipSup" />';

////// variables //////
///////////////////////

Ext.onReady(function()
{
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
    			title    : 'Contratante Actual'
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
                title       : 'Cambio de RFC del Contratante'
//                ,height     : 80
//                ,autoScroll : true
                ,defaults :
                {
                    style : 'margin : 8px;'
                }
				,items     : [
				{
					xtype: 'textfield',
					name:  'cdrfc',
					width: 550,
					fieldLabel: 'RFC'
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
//            _35_formLectura
            _35_gridContratantes
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
    		
    		nombreInicial = {
    				cdperson: json.slist1[0].CDPERSON,
    				rfc     : json.slist1[0].CDRFC,
    				nombre  : json.slist1[0].DSNOMBRE,
    				snombre : json.slist1[0].DSNOMBRE1,
    				appat   : json.slist1[0].DSAPELLIDO,
    				apmat   : json.slist1[0].DSAPELLIDO1
    			};
    		
    		if(json.success==true)
    		{
    			/*
    			a.cdunieco, a.cdramo, a.estado, a.nmpoliza, a.cdcontratante, a.nmsuplem, a.status, a.cdtipoag, porredau, a.porparti,nombre
    			*/
    			_35_storeContratantes.add(json.slist1);
    			
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
            ,'smap1.cdtipsup'     : _cdTipSupCambioRFC
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _35_fieldFechaEndoso.setMinValue(json.smap1.FECHA_MINIMA);
                _35_fieldFechaEndoso.setMaxValue(json.smap1.FECHA_MAXIMA);
                _35_fieldFechaEndoso.setValue(json.smap1.FECHA_REFERENCIA);
                _35_fieldFechaEndoso.setReadOnly(json.smap1.EDITABLE=='N');
                _35_fieldFechaEndoso.isValid();
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
    ////// loader //////
    ////////////////////
});

///////////////////////
////// funciones //////
function _35_confirmar()
{
    debug('_35_confirmar');
    
    var valido=true;
    
    if(!validarRFC(_35_formContratante.down('[name=cdrfc]').getValue(), _35_storeContratantes.getAt(0).get('OTFISJUR'))){
		mensajeWarning('Favor de introducir un RFC correcto.');
		return;
	}
    
    if(valido)
    {
        valido=_35_panelEndoso.isValid();
        if(!valido)
        {
            datosIncompletos();
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
                fecha_endoso     : Ext.Date.format(_35_fieldFechaEndoso.getValue(),'d/m/Y')
            }
            ,smap3:{
            	'pv_cdperson_i' : _35_storeContratantes.getAt(0).get('CDPERSON'),
            	'pv_cdrfc_i'    : _35_formContratante.down('[name=cdrfc]').getValue(),
            	'cdperson'         : nombreInicial.cdperson,
            	'rfc'              : nombreInicial.rfc,
            	'nombre'           : nombreInicial.nombre,
            	'snombre'          : nombreInicial.snombre,
            	'appat'            : nombreInicial.appat,
            	'apmat'            : nombreInicial.apmat
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