<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p37_urlConfirmar          = '<s:url namespace="/endosos" action="confirmarEndosoBajaIncisos" />';
var _p37_urlRecuperacionSimple = '<s:url namespace="/emision" action="recuperacionSimple"         />';
////// urls //////

////// variables //////
var _p37_smap1  = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
var _p37_slist1 = <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />;
var _p37_flujo  = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;

debug('_p37_smap1:'  , _p37_smap1);
debug('_p37_slist1:' , _p37_slist1);
debug('_p37_flujo:'  , _p37_flujo);

var _p37_store;
////// variables //////

////// overrides //////
var _p37_gridColumns =
[
    <s:property value="imap.gridColumns" escapeHtml="false" />
];
////// overrides //////

////// dinamicos //////
////// dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('_p37_modelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            //MPOLISIT
            "CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
            ,"NMSITUAC"   , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
            ,"SWREDUCI"   , "CDAGRUPA" , "CDESTADO"   , "CDGRUPO"
            ,"NMSITUAEXT" , "NMSITAUX" , "NMSBSITEXT" , "CDPLAN"
            ,"CDASEGUR"   , "DSGRUPO"
            ,{ name : 'FEFECSIT' , type : 'date' , dateFormat : 'd/m/Y' }
            ,{ name : 'FECHAREF' , type : 'date' , dateFormat : 'd/m/Y' }
            //TVALOSIT
            ,'NMSUPLEM_TVAL'
            ,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05" , "OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
            ,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15" , "OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
            ,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25" , "OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
            ,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35" , "OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
            ,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45" , "OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
            ,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55" , "OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
            ,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65" , "OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR70"
            ,"OTVALOR71" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75" , "OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR80"
            ,"OTVALOR81" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85" , "OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR90"
            ,"OTVALOR91" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95" , "OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
            ,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05" , "DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
            ,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15" , "DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
            ,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25" , "DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
            ,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35" , "DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
            ,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45" , "DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
            ,"DSVALOR51" , "DSVALOR52" , "DSVALOR53" , "DSVALOR54" , "DSVALOR55" , "DSVALOR56" , "DSVALOR57" , "DSVALOR58" , "DSVALOR59" , "DSVALOR60"
            ,"DSVALOR61" , "DSVALOR62" , "DSVALOR63" , "DSVALOR64" , "DSVALOR65" , "DSVALOR66" , "DSVALOR67" , "DSVALOR68" , "DSVALOR69" , "DSVALOR70"
            ,"DSVALOR71" , "DSVALOR72" , "DSVALOR73" , "DSVALOR74" , "DSVALOR75" , "DSVALOR76" , "DSVALOR77" , "DSVALOR78" , "DSVALOR79" , "DSVALOR80"
            ,"DSVALOR81" , "DSVALOR82" , "DSVALOR83" , "DSVALOR84" , "DSVALOR85" , "DSVALOR86" , "DSVALOR87" , "DSVALOR88" , "DSVALOR89" , "DSVALOR90"
            ,"DSVALOR91" , "DSVALOR92" , "DSVALOR93" , "DSVALOR94" , "DSVALOR95" , "DSVALOR96" , "DSVALOR97" , "DSVALOR98" , "DSVALOR99"
            //MPERSONA
            ,"CDPERSON"    , "CDTIPIDE"  , "CDIDEPER"   , "DSNOMBRE"
            ,"CDTIPPER"    , "OTFISJUR"  , "OTSEXO"     , "CDRFC"
            ,"FOTO"        , "DSEMAIL"   , "DSNOMBRE1"  , "DSAPELLIDO"
            ,"DSAPELLIDO1" , "CDNACION"  , "DSCOMNOM"   , "DSRAZSOC"
            ,"DSNOMUSU"    , "CDESTCIV"  , "CDGRUECO"   , "CDSTIPPE"
            ,"NMNUMNOM"    , "CURP"      , "CANALING"   , "CONDUCTO"
            ,"PTCUMUPR"    , "STATUSPER" , "RESIDENCIA" , "NONGRATA"
            ,"CDIDEEXT"    , "CDSUCEMI"
            ,{ name : 'FENACIMI'  , type : 'date' , dateFormat : 'd/m/Y' }
            ,{ name : 'FEINGRESO' , type : 'date' , dateFormat : 'd/m/Y' }
            ,{ name : 'FEACTUAL'  , type : 'date' , dateFormat : 'd/m/Y' }
            //MPOLIPER
            ,"CDROL" , "NMORDDOM" , "SWRECLAM" , "SWEXIPER" , "CDPARENT" , "PORBENEF"
            //CUSTOM
            ,'ATRIBUTOS','NOMBRECOMPLETO'
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p37_store = Ext.create('Ext.data.Store',
    {
        model : '_p37_modelo'
    });
    for(var i in _p37_slist1)
    {
        _p37_store.add(new _p37_modelo(_p37_slist1[i]));
    }
    debug('_p37_store.data:',_p37_store.data);
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.grid.Panel',
    {
        itemId     : '_p37_grid'
        ,renderTo  : '_p37_divpri'
        ,title     : 'Incisos'
        ,store     : _p37_store
        ,columns   : _p37_gridColumns
        ,minHeight : 200
        ,maxHeight : 400
        ,bbar      :
        [
            '->'
            ,{
                xtype     : 'form'
                ,itemId   : '_p36_formEndoso'
                ,layout   : 'hbox'
                ,defaults : { style : 'margin:5px;' }
                ,items    :
                [
                    {
                        xtype       : 'datefield'
                        ,itemId     : '_p37_fechaCmp'
                        ,fieldLabel : 'Fecha de efecto'
                        ,value      : new Date()
                        ,allowBlank : false
                        ,name       : 'fechaEndoso'
                    }
                    ,Ext.create('Ext.form.ComboBox',
                    {
                        xtype           : 'combo'
                        ,store          : _g_storeSino
                        ,allowBlank     : false
                        ,forceSelection : true
                        ,valueField     : 'key'
                        ,displayField   : 'value'
                        ,queryMode      : 'local'
                        ,value          : 'N'
                        ,hidden         : true
                        ,name           : 'devoPrim'
                        ,fieldLabel     : 'Devoluci&oacute;n derechos'
                    })
                    ,{
                        xtype    : 'button'
		                ,itemId  : '_p37_botonConfirmar'
		                ,text    : 'Confirmar'
		                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
		                ,handler : function(me){ _p37_confirmar(me); }
		            }
		        ]
		    }
		    ,'->'
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    Ext.Ajax.request(
    {
        url      : _p37_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : _p37_smap1.CDUNIECO
            ,'smap1.cdramo'       : _p37_smap1.CDRAMO
            ,'smap1.estado'       : _p37_smap1.ESTADO
            ,'smap1.nmpoliza'     : _p37_smap1.NMPOLIZA
            ,'smap1.cdtipsup'     : _p37_smap1.cdtipsup
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _fieldByName('fechaEndoso').setMinValue(json.smap1.FECHA_MINIMA);
                _fieldByName('fechaEndoso').setMaxValue(json.smap1.FECHA_MAXIMA);
                _fieldByName('fechaEndoso').setReadOnly(json.smap1.EDITABLE=='N');
                _fieldByName('fechaEndoso').isValid();
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
    
    
    Ext.Ajax.request(
    {
        url      : _p37_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_PERMISO_USUARIO_DEVOLUCION_PRIMAS'
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### permiso:',json);
            if(json.exito)
            {
                if(json.smap1.permiso=='S')
                {
                    _fieldByName('devoPrim').show();
                }
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
    ////// loaders //////
});

////// funciones //////
function _p37_confirmar(boton)
{
    debug('>_p37_confirmar');
    
    if(!boton.up('form').getForm().isValid())
    {
        datosIncompletos();
        return;
    }
    
    _p37_smap1['fechaEndoso'] = Ext.Date.format(_fieldByName('fechaEndoso').getValue(),'d/m/Y');
    _p37_smap1['devoPrim']    = _fieldByName('devoPrim').getValue();
    
    boton.setDisabled(true);
    boton.setText('Cargando...');
    
    var jsonData =
    {
        smap1   : _p37_smap1
        ,slist1 : _p37_slist1
    };
    
    if(!Ext.isEmpty(_p37_flujo))
    {
        jsonData.flujo = _p37_flujo;
    }
    
    Ext.Ajax.request(
    {
        url       : _p37_urlConfirmar
        ,jsonData : jsonData
        ,success  : function(response)
        {
            boton.setText('Confirmar');
            boton.setDisabled(false);
            var json = Ext.decode(response.responseText);
            debug('### confirmar:',json);
            if(json.success)
            {
                var callbackRemesa = function()
                {
                    marendNavegacion(2);
                };
                mensajeCorrecto('Endoso generado',json.respuesta,function()
                {
                    _generarRemesaClic(
                        true
                        ,_p37_smap1.CDUNIECO
                        ,_p37_smap1.CDRAMO
                        ,_p37_smap1.ESTADO
                        ,_p37_smap1.NMPOLIZA
                        ,callbackRemesa
                    );
                });
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure  : function()
        {
            boton.setText('Confirmar');
            boton.setDisabled(false);
            errorComunicacion();
        }
    });
    
    debug('<_p37_confirmar');
}

function _p37_renderer(valor,mapeo,view)
{
    debug('_p37_renderer valor,mapeo,view.id:',valor,mapeo,view.id);
    var mapeos=mapeo.split('@');
    debug('mapeos:',mapeos);
    var valores  = valor.split('|');
    debug('valores:',valores);
    var cdtipsit = valores[0];
    debug('cdtipsit:',cdtipsit);
    var mapeado=false;
    for(var i in mapeos)
    {
        var bloques   = mapeos[i].split('#');
        var cdtipsits = bloques[0];
        var cdatribu  = bloques[1];
        var tipo      = bloques[2];
        if(('|'+cdtipsits+'|').lastIndexOf('|'+cdtipsit+'|')!=-1)
        {
            debug('tipo:'     , tipo);
            debug('cdatribu:' , cdatribu);
            if(tipo+'x'=='VALORx')
            {
                mapeado=true;
                valor=valores[cdatribu].split('~')[0];
                debug('tipo VALOR cdatribu,valor:',cdatribu,valor,'.');
                break;
            }
            else if(tipo+'x'=='DISPLAYx')
            {
                mapeado=true;
                valor=valores[cdatribu].split('~')[1];
                debug('tipo DISPLAY cdatribu,valor:',cdatribu,valor,'.');
                break;
            }
        } 
    }
    if(!mapeado)
    {
        valor='N/A';
    }
    return valor;
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body><div id="_p37_divpri" style="height:500px;border:1px solid #999999;"></div></body>
</html>