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
    copago : 10000
    mascopago : si
*/
//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _7_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _7_formLectura;
var _7_formCopago;
var _7_panelPri;
var _7_panelEndoso;
var _7_fieldFechaEndoso;

var _7_urlGuardar = '<s:url namespace="/endosos" action="guardarEndosoCopago" />';

debug('_7_smap1:',_7_smap1);

var _p7_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p7_flujo:',_p7_flujo);
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
    Ext.define('_7_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_7_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [
                    _7_fieldFechaEndoso
                ]
            });
            this.callParent();
        }
    });
    
    Ext.define('_7_FormCopago',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_7_FormCopago initComponent');
            Ext.apply(this,
            {
                title      : 'Copago'
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
                    <s:property value="imap1.itemCopagoLectura" />
                    ,<s:property value="imap1.itemCopago" />
                ]
                ,listeners :
                {
                    afterrender : function(me)
                    {
                        var comboOriginal = me.items.items[0];
                        var comboNuevo    = me.items.items[1];
                        var copagOrigin   = _7_smap1.copago;
                        
                        comboOriginal.setValue(copagOrigin);
                        comboNuevo   .setValue(copagOrigin);
                        
                        comboNuevo.on('change',function(combo,newVal,oldVal)
                        {
                            var copagOrigin = _7_smap1.copago;
                            var incremento  = _7_smap1.mascopago=='si';
                            
                            debug('comparando',copagOrigin,newVal,'incremento '+(incremento?'si':'no'));
                            
                            if(incremento==true)
                            {
                                if(newVal*1<copagOrigin*1)
                                {
                                    combo.setValue(oldVal);
                                    mensajeError('No se puede decrementar el copago');
                                }
                            }
                            else
                            {
                                if(newVal*1>copagOrigin*1)
                                {
                                    combo.setValue(oldVal);
                                    mensajeError('No se puede incrementar el copago');
                                }
                            }
                        });
                    }
                }
            });
            this.callParent();
        }
    });
    
    Ext.define('_7_FormLectura',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_7_FormLectura initComponent');
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
    _7_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
    {
        format      : 'd/m/Y'
        ,fieldLabel : 'Fecha de efecto'
        ,allowBlank : false
        ,value      : new Date()
        ,name       : 'fecha_endoso'
    });
    _7_panelEndoso   = new _7_PanelEndoso();
    _7_formCopago = new _7_FormCopago();
    _7_formLectura   = new _7_FormLectura();
    
    _7_panelPri=Ext.create('Ext.panel.Panel',
    {
        renderTo     : '_7_divPri'
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
                ,handler : _7_confirmar
            }
        ]
        ,items       :
        [
            _7_formLectura
            ,_7_formCopago
            ,_7_panelEndoso
        ]
    });
    ////// contenido //////
    ///////////////////////
});

///////////////////////
////// funciones //////
function _7_confirmar()
{
    debug('_7_confirmar');
    
    var valido=true;
    
    if(valido)
    {
        valido=_7_formCopago.isValid()&&_7_panelEndoso.isValid();
        if(!valido)
        {
            datosIncompletos();
        }
    }
    
    if(valido)
    {
        var json=
        {
            smap1  : _7_smap1
            ,smap2 :
            {
                fecha_endoso : Ext.Date.format(_7_fieldFechaEndoso.getValue(),'d/m/Y')
                ,copago      : _7_formCopago.items.items[1].getValue()
            }
        }
        
        if(!Ext.isEmpty(_p7_flujo))
        {
            json.flujo = _p7_flujo;
        }
        
        debug('datos que se enviaran:',json);
        _setLoading(true,_7_panelPri);
        Ext.Ajax.request(
        {
            url       : _7_urlGuardar
            ,jsonData : json
            ,success  : function(response)
            {
                _setLoading(false,_7_panelPri);
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
                            ,_7_smap1.CDUNIECO
                            ,_7_smap1.CDRAMO
                            ,_7_smap1.ESTADO
                            ,_7_smap1.NMPOLIZA
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
                _setLoading(false,_7_panelPri);
                errorComunicacion();
            }
        });
    }
};
////// funciones //////
///////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_7_divPri" style="height:1000px;"></div>