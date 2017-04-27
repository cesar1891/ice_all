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
	deducible : 10000
	masdeducible : si
*/
//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _6_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _6_formLectura;
var _6_formDeducible;
var _6_panelPri;
var _6_panelEndoso;
var _6_fieldFechaEndoso;

var _6_urlGuardar = '<s:url namespace="/endosos" action="guardarEndosoDeducible" />';

debug('_6_smap1:',_6_smap1);

var _p6_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p6_flujo:',_p6_flujo);
////// variables //////
///////////////////////

Ext.onReady(function()
{
	/////////////////////
	////// modelos //////
    ////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
    ////// stores //////
	////////////////////
	
	/////////////////////////
	////// componentes //////
	Ext.define('_6_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_6_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [
                    _6_fieldFechaEndoso
                ]
            });
            this.callParent();
        }
    });
	
	Ext.define('_6_FormDeducible',
	{
		extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_6_FormDeducible initComponent');
            Ext.apply(this,
            {
                title      : 'Deducibe'
                ,defaults  :
                {
                    style : 'margin : 5px;'
                }
                ,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items     :
                [
                    <s:property value="imap1.itemDeducibleLectura" />
                    ,<s:property value="imap1.itemDeducible" />
                ]
                ,listeners :
                {
                	afterrender : function(me)
                	{
                		var comboOriginal = me.items.items[0];
                		var comboNuevo    = me.items.items[1];
                		var deducOrigin   = _6_smap1.deducible;
                		
                		comboOriginal.setValue(deducOrigin);
                		comboNuevo   .setValue(deducOrigin);
                		
                		comboNuevo.on('change',function(combo,newVal,oldVal)
                		{
                			var deducOrigin = _6_smap1.deducible;
                			var incremento  = _6_smap1.masdeducible=='si';
                			
                			debug('comparando',deducOrigin,newVal,'incremento '+(incremento?'si':'no'));
                			
                			if(incremento==true)
                			{
                				if(newVal*1<deducOrigin*1)
                				{
                					combo.setValue(oldVal);
                					mensajeError('No se puede decrementar el deducible');
                				}
                			}
                			else
                			{
                				if(newVal*1>deducOrigin*1)
                                {
                                    combo.setValue(oldVal);
                                    mensajeError('No se puede incrementar el deducible');
                                }
                			}
                		});
                	}
                }
            });
            this.callParent();
        }
	});
	
	Ext.define('_6_FormLectura',
	{
		extend         : 'Ext.form.Panel'
		,initComponent : function()
		{
			debug('_6_FormLectura initComponent');
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
				,items     : [ <s:property value="imap1.itemsLectura" /> ]
			});
			this.callParent();
		}
	});
    ////// componentes //////
	/////////////////////////
	
	///////////////////////
	////// contenido //////
	_6_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
    {
        format      : 'd/m/Y'
        ,fieldLabel : 'Fecha de efecto'
        ,allowBlank : false
        ,value      : new Date()
        ,name       : 'fecha_endoso'
    });
	_6_panelEndoso   = new _6_PanelEndoso();
	_6_formDeducible = new _6_FormDeducible();
	_6_formLectura   = new _6_FormLectura();
    
    _6_panelPri=Ext.create('Ext.panel.Panel',
    {
    	renderTo     : '_6_divPri'
    	,defaults    :
    	{
    		style : 'margin : 5px;'
    	}
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
            	text     : 'Confirmar endoso'
            	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
            	,handler : _6_confirmar
            }
        ]
        ,items       :
        [
            _6_formLectura
            ,_6_formDeducible
            ,_6_panelEndoso
        ]
    });
    ////// contenido //////
	///////////////////////
});

///////////////////////
////// funciones //////
function _6_confirmar()
{
	debug('_6_confirmar');
	
	var valido=true;
	
	if(valido)
	{
		valido=_6_formDeducible.isValid()&&_6_panelEndoso.isValid();
		if(!valido)
		{
			datosIncompletos();
		}
	}
	
	if(valido)
	{
		var json=
		{
			smap1  : _6_smap1
			,smap2 :
			{
				fecha_endoso : Ext.Date.format(_6_fieldFechaEndoso.getValue(),'d/m/Y')
				,deducible   : _6_formDeducible.items.items[1].getValue()
			}
		}
		
		if(!Ext.isEmpty(_p6_flujo))
		{
		    json.flujo = _p6_flujo;
		}
		
		debug('datos que se enviaran:',json);
		_setLoading(true,_6_panelPri);
		Ext.Ajax.request(
		{
			url       : _6_urlGuardar
			,jsonData : json
			,success  : function(response)
			{
				_setLoading(false,_6_panelPri);
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
                        /*//////////////////////////////*/
                        ////// usa codigo del padre //////
                        //////////////////////////////////
                    };
                    
                    mensajeCorrecto('Endoso generado',json.mensaje,function()
                    {
                        _generarRemesaClic(
                            true
                            ,_6_smap1.CDUNIECO
                            ,_6_smap1.CDRAMO
                            ,_6_smap1.ESTADO
                            ,_6_smap1.NMPOLIZA
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
		    	_setLoading(false,_6_panelPri);
		    	errorComunicacion();
		    }
		});
	}
};
////// funciones //////
///////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_6_divPri" style="height:1000px;"></div>