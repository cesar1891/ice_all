<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
////// variables //////
var _p27_urlCargarTvalositTitular = '<s:url namespace="/endosos" action="cargarTvalositTitular"                  />';
var _p27_urlConfirmar             = '<s:url namespace="/endosos" action="guardarEndosoAtributosSituacionGeneral" />';

var _p27_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p27_smap1:',_p27_smap1);

var _p27_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p27_flujo:',_p27_flujo);
////// variables //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('_p27_modeloTatrisit',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap1.actualFields" /> ]
    });
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p27_panelpri'
        ,renderTo : '_p27_divpri'
        ,defaults : { style : 'margin : 5px;' }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId    : '_p27_lecturaForm'
                ,title    : 'Datos de la p&oacute;liza'
                ,defaults : { style : 'margin : 5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    :
                [
                    <s:property value="imap1.lecturaItems" />
                ]
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p27_actualForm'
                ,title    : 'Datos actuales'
                ,defaults : { style : 'margin : 5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    :
                [
                    <s:property value="imap1.actualItems" />
                ]
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p27_nuevoForm'
                ,title    : 'Datos modificables'
                ,defaults : { style : 'margin : 5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    :
                [
                    <s:property value="imap1.nuevoItems" />
                ]
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p27_endosoForm'
                ,title    : 'Datos del endoso'
                ,defaults : { style : 'margin : 5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    :
                [
                    <s:property value="imap1.endosoItems" />
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Confirmar endoso'
                        ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                        ,handler : _p27_confirmar
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    _setLoading(true,_fieldById('_p27_panelpri'));
    Ext.Ajax.request(
    {
        url     : _p27_urlCargarTvalositTitular
        ,params :
        {
            'smap1.cdunieco'  : _p27_smap1.CDUNIECO
            ,'smap1.cdramo'   : _p27_smap1.CDRAMO
            ,'smap1.estado'   : _p27_smap1.ESTADO
            ,'smap1.nmpoliza' : _p27_smap1.NMPOLIZA
            ,'smap1.nmsuplem' : _p27_smap1.NMSUPLEM
        }
        ,success : function(response)
        {
            _setLoading(false,_fieldById('_p27_panelpri'));
            var json = Ext.decode(response.responseText);
            debug('### obtener tvalosit titular:',json);
            if(json.exito)
            {
                _fieldById('_p27_actualForm').loadRecord(new _p27_modeloTatrisit(json.smap1));
                _fieldById('_p27_nuevoForm').loadRecord(new _p27_modeloTatrisit(json.smap1));
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _setLoading(false,_fieldById('_p27_panelpri'));
            errorComunicacion();
        }
    });
    ////// loaders //////
});

////// funciones //////
function _p27_confirmar(me)
{
    debug('>_p27_confirmar');
    
    var valido = _fieldById('_p27_nuevoForm').isValid();
    
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
        valido = _fieldById('_p27_endosoForm').isValid();
        if(!valido)
        {
            datosIncompletos();
        }
    }
    
    if(valido)
    {
        var names        = [];
        var validaciones = [];
        var labels       = [];
        
        for(var i=1;i<=16;i++)
        {
            if(!Ext.isEmpty(_p27_smap1['P'+i+'CLAVE']))
            {
                var cdatribu = _p27_smap1['P'+i+'VALOR'];
                var name='parametros.pv_otvalor'+(('00'+cdatribu).slice(-2));
                names.push(name);
                validaciones.push(_p27_smap1['P'+i+'CLAVE']);
                labels.push(_fieldByName(name,_fieldById('_p27_nuevoForm')).getFieldLabel());
            }
        }
        
        debug('names:'        , names);
        debug('validaciones:' , validaciones);
        debug('labels:'       , labels);
        
        var errores = '';
        
        for(var i=0;i<validaciones.length;i++)
        {
            var validacion = validaciones[i];
            var name       = names[i];
            var label      = labels[i];
            
            if(validacion+'x'=='+x'||validacion+'x'=='-x'||validacion+'x'=='=x')
            {
                if(validacion+'x'=='+x')
                {
                    var valorActual = _fieldByName(name,_fieldById('_p27_actualForm')).getValue();
                    var valorNuevo  = _fieldByName(name,_fieldById('_p27_nuevoForm')).getValue();
                    if(Number(valorNuevo) <= Number(valorActual))
                    {
                        valido = false;
                        errores = errores + 'El valor de '+label+' debe ser mayor<br/>';
                    }
                }
                else if(validacion+'x'=='-x')
                {
                    var valorActual = _fieldByName(name,_fieldById('_p27_actualForm')).getValue();
                    var valorNuevo  = _fieldByName(name,_fieldById('_p27_nuevoForm')).getValue();
                    if(Number(valorNuevo) >= Number(valorActual))
                    {
                        valido = false;
                        errores = errores + 'El valor de '+label+' debe ser menor<br/>';
                    }
                }
                else
                {
                    var valorActual = _fieldByName(name,_fieldById('_p27_actualForm')).getValue();
                    var valorNuevo  = _fieldByName(name,_fieldById('_p27_nuevoForm')).getValue();
                    if(Number(valorNuevo) != Number(valorActual))
                    {                    
                        valido = false;
                        errores = errores + 'El valor de '+label+' no puede ser modificado<br/>';
                    }
                }
            }
        }
        
        if(!valido)
        {
            mensajeError(errores);
        }
    }
    
    if(valido)
    {
        _setLoading(true,_fieldById('_p27_panelpri'));
        
        var jsonData =
        {
            smap1  : _p27_smap1
            ,smap2 : _fieldById('_p27_nuevoForm').getValues()
            ,smap3 : _fieldById('_p27_endosoForm').getValues()
        };
        
        if(!Ext.isEmpty(_p27_flujo))
        {
            jsonData.flujo = _p27_flujo;
        }
        
        debug('jsonData:',jsonData,'.');
        
        Ext.Ajax.request(
        {
            url       : _p27_urlConfirmar
            ,jsonData : jsonData
            ,success  : function(response)
            {
                _setLoading(false,_fieldById('_p27_panelpri'));
                var json = Ext.decode(response.responseText);
                debug('### confirmar endoso atributos general:',json);
                if(json.exito)
                {
                    var callbackRemesa = function()
                    {
                        //////////////////////////////////
                        ////// USA CODIGO DEL PADRE //////
                        marendNavegacion(2);
                        ////// USA CODIGO DEL PADRE //////
                        //////////////////////////////////
                    };
                    
                    mensajeCorrecto('Endoso generado',json.respuesta,function()
                    {
                        _generarRemesaClic(
                            true
                            ,_p27_smap1.CDUNIECO
                            ,_p27_smap1.CDRAMO
                            ,_p27_smap1.ESTADO
                            ,_p27_smap1.NMPOLIZA
                            ,callbackRemesa
                        );
                    });
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                _setLoading(false,_fieldById('_p27_panelpri'));
                errorComunicacion();
            }
        });
    }
    debug('<_p27_confirmar');
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_p27_divpri"></div>