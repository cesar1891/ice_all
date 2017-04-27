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

var _35_formLectura;
var _35_formContratante;
var _35_panelPri;
var _35_panelEndoso;
var _35_fieldFechaEndoso;
var _35_storeContratantes;
var _35_gridContratantes;

var _35_urlGuardar     = '<s:url namespace="/endosos" action="guardarEndosoContratante"       />';
var _35_urlLoadContratantes = '<s:url namespace="/endosos" action="cargarContratantesEndosoContratante" />';

var _p35_urlPantallaCliente = '<s:url namespace="/catalogos"  action="includes/personasLoader" />';
//para funcion de pantalla de personas
var obtieneDatosClienteContratante;
//var destruirContLoaderPersona;
var _p22_parentCallback = false;
var _contratanteSaved = false;

debug('_35_smap1:',_35_smap1);

var _p35_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p35_flujo:',_p35_flujo);
////// variables //////
///////////////////////

Ext.onReady(function()
{
	
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
	
	function contratanteGuardado (){
		_contratanteSaved = true;
	}
	
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
    			title    : 'Contratante'
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
                title       : 'Nuevo Contratante'
                ,height     : 400
                ,autoScroll : true
                ,buttonAlign : 'left'
				,loader     :
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
					                'smap1.esSaludDanios' : _35_smap1.CDRAMO != '1'?'S':'D',//Modo danios para Recupera
					                'smap1.esCargaClienteNvo' : 'N' ,
					                'smap1.ocultaBusqueda' : 'S' ,
					                'smap1.cargaCP' : '',
					                'smap1.cargaTipoPersona' : '',
					                'smap1.cargaSucursalEmi' : _35_smap1.CDUNIECO,
					                'smap1.activaCveFamiliar': _35_smap1.CDUNIECO == '1403'?'S':'N'
					            }
                }, 
                buttons:[{
			            icon    : '${ctx}/resources/fam3icons/icons/user_delete.png',
			            text    : 'Quitar/Cambiar Contratante',
			            handler : function(){
							destruirLoaderContratante();	 
							
                            _35_formContratante.getLoader().destroy();
                            
                            _35_formContratante.loader = new Ext.ComponentLoader({
			                    url       : _p35_urlPantallaCliente
			                    ,scripts  : true
			                    ,autoLoad : false
			                    ,ajaxOptions: {
			                            method: 'POST'
			                     }
			                });
			                
                            _35_formContratante.getLoader().load({
						            params: {
						                'smap1.cdperson' : '',
						                'smap1.cdideper' : '',
						                'smap1.cdideext' : '',
						                'smap1.esSaludDanios' : _35_smap1.CDRAMO != '1'?'S':'D',//Modo danios para Recupera
						                'smap1.esCargaClienteNvo' : 'N' ,
						                'smap1.ocultaBusqueda' : 'S' ,
						                'smap1.cargaCP' : '',
						                'smap1.cargaTipoPersona' : '',
						                'smap1.cargaSucursalEmi' : _35_smap1.CDUNIECO,
						                'smap1.activaCveFamiliar': _35_smap1.CDUNIECO == '1403'?'S':'N'
						            }
						     });
						     _p22_parentCallback = contratanteGuardado;
						     _contratanteSaved = false;
						     
			            }
			        }]
            });
            _p22_parentCallback = contratanteGuardado;
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
        ,readOnly   : true
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
    _setLoading(true,_35_panelPri);
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
    		_setLoading(false,_35_panelPri);
    		var json=Ext.decode(response.responseText);
    		debug('contratantes cargados:',json);
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
        	_setLoading(false,_35_panelPri);
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
    
    if(valido)
    {
    	 var datosContr = obtieneDatosClienteContratante();
		if(Ext.isEmpty(datosContr.nombre)){
			mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
			return false;
		}else if(!Ext.isEmpty(datosContr.cdperson) && !_contratanteSaved){
			mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
			return false;
		}
    	
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
                fecha_endoso : Ext.Date.format(_35_fieldFechaEndoso.getValue(),'d/m/Y'),
                cdpersonNvoContr : datosContr.cdperson
            }
            ,slist1 : slist1
        };
        
        if(!Ext.isEmpty(_p35_flujo))
        {
            json.flujo = _p35_flujo;
        }
        
        debug('datos que se enviaran:',json);
        _setLoading(true,_35_panelPri);
        Ext.Ajax.request(
        {
            url       : _35_urlGuardar
            ,timeout  : 240000
            ,jsonData : json
            ,success  : function(response)
            {
                _setLoading(false,_35_panelPri);
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
                    
//                      if(!Ext.isEmpty(destruirContLoaderPersona)){
//							destruirContLoaderPersona();	                                
//	                    }
                        /*//////////////////////////////*/
                        ////// usa codigo del padre //////
                        //////////////////////////////////
                    };
                    
                    mensajeCorrecto('Endoso generado',json.mensaje,function()
                    {
                    	if(json.endosoConfirmado){
                        	_generarRemesaClic(
	                            true
	                            ,_35_smap1.CDUNIECO
	                            ,_35_smap1.CDRAMO
	                            ,_35_smap1.ESTADO
	                            ,_35_smap1.NMPOLIZA
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
                _setLoading(false,_35_panelPri);
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