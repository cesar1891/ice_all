<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p33_urlRecuperacionSimple      = '<s:url namespace="/emision" action="recuperacionSimple"      />';
var _p33_urlRecuperacionSimpleLista = '<s:url namespace="/emision" action="recuperacionSimpleLista" />';
var _p33_urlGuardar                 = '<s:url namespace="/emision" action="guardarClausulasPoliza"  />';
////// urls //////

////// variables //////
var _p33_smap1 = <s:property value='%{convertToJSON("smap1")}' escapeHtml='false' />;
debug('_p33_smap1:',_p33_smap1);

var _p33_store            = null;
var _p33_ventanaClausulas = null;
var _p33_comboClausulas   = null;
////// variables //////

////// dinamicos //////
var _p33_comboClausulas = <s:property value="imap.comboClausulas" />;
////// dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('_p33_modelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'cdunieco'
            ,'cdramo'
            ,'estado'
            ,'nmpoliza'
            ,'nmsuplem'
            ,'status'
            ,'cdclausu'
            ,'dsclausu'
            ,'dslinea'
            ,'accion'
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p33_store=Ext.create('Ext.data.Store',
    {
        model : '_p33_modelo'
    });
    ////// stores //////
    
    ////// componentes //////
    _p33_ventanaClausulas = Ext.create('Ext.window.Window',
    {
        title        : 'CL&Aacute;USULA'
        ,modal       : true
        ,closeAction : 'hide'
        ,defaults    : { style : 'margin:5px;' }
        ,items       :
        [
            _p33_comboClausulas
            ,{
                xtype       : 'textarea'
                ,itemId     : '_p33_textarea'
                ,allowBlank : false
                ,width      : 500
                ,height     : 300
            }
        ]
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Aceptar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function(){ _p33_agregarEditarClausula(); }
            }
        ]
    });
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.grid.Panel',
    {
        itemId      : '_p33_grid'
        ,renderTo   : '_p33_divpri'
        ,title      : 'CL&Aacute;USULAS DE P&Oacute;LIZA'
        ,autoScroll : true
        ,tbar       :
        [
            {
                text     : 'Agregar'
                ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                ,handler : function()
                {
                    _p33_accion='I';
                    _p33_mostrarVentanaClausulas();
                }
            }
        ]
        ,columns    :
        [
            {
                header     : 'CL&Aacute;USULA'
                ,dataIndex : 'dsclausu'
                ,flex      : 1
            }
            ,{
                xtype         : 'actioncolumn'
                ,width        : 50
                ,menuDisabled : true
                ,sortable     : false
                ,items        :
                [
                    {
                        icon     : '${ctx}/resources/fam3icons/icons/pencil.png'
                        ,tooltip : 'Editar'
                        ,handler : function(view,row,col,item,e,record)
                        {
                            _p33_mostrarVentanaClausulas(record);
                        }
                    }
                    ,{
                        icon     : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,tooltip : 'Borrar'
                        ,handler : function(view,row,col,item,e,record)
                        {
                            if(record.get('accion')+'x'=='Ix')
                            {
                                debug('se elimina:',record);
                                _p33_store.remove(record);
                            }
                            else
                            {
                                record.set('accion','B');
                                debug('se puso como B:',record.data);
                            }
                            _p33_store.filterBy(function(record)
                            {
                                return record.get('accion')+'x'!='Bx';
                            });
                        }
                    }
                ]
            }
        ]
        ,store       : _p33_store
        ,height      : 300
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Guardar'
                ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                ,handler : function(){ _p33_guardar(); }
            }
        ]
    });
    ////// contenido //////
    
    ////// loaders //////
    _p33_cargar();
    ////// loaders //////
    
    ////// custom //////
    _p33_comboClausulas.on(
    {
        select : function(me)
        {
            var value=me.getValue();
            _fieldById('_p33_textarea').setLoading(true);
            Ext.Ajax.request(
            {
                url     : _p33_urlRecuperacionSimple
                ,params :
                {
                    'smap1.procedimiento' : 'RECUPERAR_TEXTO_CLAUSULA_POLIZA'
                    ,'smap1.cdclausu'     : value
                }
                ,success : function(response)
                {
                    _fieldById('_p33_textarea').setLoading(false);
                    var json=Ext.decode(response.responseText);
                    debug('### texto clausula:',json);
                    if(json.exito)
                    {
                        _fieldById('_p33_textarea').setValue(json.smap1.dsclausu);
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                ,failure : function()
                {
                    _fieldById('_p33_textarea').setLoading(false);
                    errorComunicacion();
                }
            });
        }
    });
    ////// custom //////
});

////// funciones //////
function _p33_guardar()
{
    debug('>_p33_guardar');
    
    var ck;
    try
    {
        ck='Verificando cambios';
        var lista=[];
        
        _p33_store.clearFilter();
        
        _p33_store.each(function(record)
        {
            if(record.get('accion')+'x'!='x')
            {
                lista.push(record.data);
            }
        });
        
        _p33_store.filterBy(function(record)
        {
            return record.get('accion')+'x'!='Bx';
        });
        
        if(lista.length==0)
        {
            throw 'No hay cambios';
        }
        
        ck='Enviando datos';
        _fieldById('_p33_grid').setLoading(true);
        Ext.Ajax.request(
        {
            url       : _p33_urlGuardar
            ,jsonData :
            {
                slist1 : lista
            }
            ,success : function(response)
            {
                _fieldById('_p33_grid').setLoading(false);
                var json=Ext.decode(response.responseText);
                if(json.exito)
                {
                    _p33_cargar();
                    mensajeCorrecto('Datos guardados','Datos guardados');
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                _fieldById('_p33_grid').setLoading(false);
                errorComunicacion();
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
    
    debug('<_p33_guardar');
}

function _p33_mostrarVentanaClausulas(record)
{
    debug('>_p33_mostrarVentanaClausulas record:',record,'.');
    _p33_ventanaClausulas.record=Ext.isEmpty(record)?'':record;
    _p33_comboClausulas.allowBlank=!Ext.isEmpty(record);
    _p33_comboClausulas.reset();
    if(Ext.isEmpty(record))
    {
        _p33_comboClausulas.reset();
        _fieldById('_p33_textarea').reset();
        _p33_comboClausulas.show();
    }
    else
    {
        _p33_comboClausulas.hide();
        _fieldById('_p33_textarea').setValue(record.get('dslinea'));
    }
    
    centrarVentanaInterna(_p33_ventanaClausulas.show());
    debug('<_p33_mostrarVentanaClausulas');
}

function _p33_agregarEditarClausula()
{
    debug('>_p33_agregarEditarClausula');
    
    var ck='';
    try
    {
        ck='Validando datos';
        if(!_p33_comboClausulas.isValid()||!_fieldById('_p33_textarea').isValid())
        {
            throw 'Favor de verificar los datos';
        }
        
        debug('ventana.record:',_p33_ventanaClausulas.record);
        if(Ext.isEmpty(_p33_ventanaClausulas.record))
        {
            ck='Creando registro';
            var record=new _p33_modelo(
            {
                cdunieco  : _p33_smap1.cdunieco
                ,cdramo   : _p33_smap1.cdramo
                ,estado   : _p33_smap1.estado
                ,nmpoliza : _p33_smap1.nmpoliza
                ,nmsuplem : _p33_smap1.nmsuplem
                ,status   : 'V'
                ,cdclausu : _p33_comboClausulas.getValue()
                ,dsclausu : _p33_comboClausulas.findRecordByValue(_p33_comboClausulas.getValue()).get('value')
                ,dslinea  : _fieldById('_p33_textarea').getValue()
                ,accion   : 'I'
            });
            debug('record nuevo:',record);
            _p33_store.add(record);
        }
        else
        {
            vk='Modificando registro';
            _p33_ventanaClausulas.record.set('dslinea',_fieldById('_p33_textarea').getValue());
            if(_p33_ventanaClausulas.record.get('accion')+'x'!='Ix')
            {
                _p33_ventanaClausulas.record.set('accion','M');
                debug('se puso como M:',_p33_ventanaClausulas.record);
            }
            else
            {
                debug('se deja como I');
            }
        }
        
        _p33_ventanaClausulas.hide();
    }
    catch(e)
    {
        manejaException(e,ck);
    }
    
    debug('<_p33_agregarEditarClausula');
}

function _p33_cargar()
{
    debug('>_p33_cargar');
    
    _p33_store.clearFilter();
    _p33_store.removeAll();
    
    _fieldById('_p33_grid').setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p33_urlRecuperacionSimpleLista
        ,params :
        {
            'smap1.procedimiento' : 'RECUPERAR_CLAUSULAS_POLIZA'
            ,'smap1.cdunieco'     : _p33_smap1.cdunieco
            ,'smap1.cdramo'       : _p33_smap1.cdramo
            ,'smap1.estado'       : _p33_smap1.estado
            ,'smap1.nmpoliza'     : _p33_smap1.nmpoliza
            ,'smap1.nmsuplem'     : _p33_smap1.nmsuplem
        }
        ,success : function(response)
        {
            _fieldById('_p33_grid').setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### cargar:',json);
            if(json.exito)
            {
                for(var i in json.slist1)
                {
                    var record=new _p33_modelo(json.slist1[i]);
                    debug('cargado:',record.data);
                    _p33_store.add(record);
                }
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _fieldById('_p33_grid').setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p33_cargar');
}
////// funciones //////
</script>
</head>
<body>
<div id="_p33_divpri" style="height:310px;border:1px solid #999;"></div>
</body>
</html>