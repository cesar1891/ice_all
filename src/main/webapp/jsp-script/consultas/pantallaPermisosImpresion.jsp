<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p51_urlRecuperacion = '<s:url namespace="/recuperacion" action="recuperar"           />';
var _p51_urlMov          = '<s:url namespace="/consultas"    action="movPermisoImpresion" />';
////// urls //////

////// variables //////
var _p51_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
debug('_p51_params:',_p51_params);

var _p51_storeIncSuc
    ,_p51_storeIncAge
    ,_p51_storeIncUsu
    ,_p51_storeExcSuc
    ,_p51_storeExcAge
    ,_p51_storeExcUsu;

var _p51_venAgregarPermiso;

////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos /////
var _p51_formBusqItems          = [<s:property value="items.itemsFormBusq"    escapeHtml="false" />];
var _p51_venAgregarPermisoItems = [<s:property value="items.itemsAgregarPerm" escapeHtml="false" />];

var _p51_venAgregarPermisoItemsCustom =
[
    Ext.create('Ext.form.field.Display',
    {
        fieldLabel : 'TIPO'
        ,value     : 'error'
        ,name      : 'dstipo'
    })
];
for(var i in _p51_venAgregarPermisoItems)
{
    _p51_venAgregarPermisoItemsCustom.push(_p51_venAgregarPermisoItems[i]);
}
////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// requires //////
    ////// requires //////

    ////// modelos //////
    Ext.define('_p51_modelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'tipo' //[S (sucursal) / A (agente) / U (usuario)]
            ,'cdusuari'
            ,'cdunieco'
            ,'cdtipram'
            ,'cduniecoPer'
            ,'cdagentePer'
            ,'cdusuariPer'
            ,'funcion' //[S/N]
            ,'descrip'
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p51_storeIncSuc = Ext.create('Ext.data.Store',
    {
        model : '_p51_modelo'
        ,data : []
    });
    _p51_storeIncAge = Ext.create('Ext.data.Store',
    {
        model : '_p51_modelo'
        ,data : []
    });
    _p51_storeIncUsu = Ext.create('Ext.data.Store',
    {
        model : '_p51_modelo'
        ,data : []
    });
    _p51_storeExcSuc = Ext.create('Ext.data.Store',
    {
        model : '_p51_modelo'
        ,data : []
    });
    _p51_storeExcAge = Ext.create('Ext.data.Store',
    {
        model : '_p51_modelo'
        ,data : []
    });
    _p51_storeExcUsu = Ext.create('Ext.data.Store',
    {
        model : '_p51_modelo'
        ,data : []
    });
    ////// stores //////
    
    ////// componentes //////
    _p51_venAgregarPermiso = Ext.create('Ext.window.Window',
    {
        title        : 'AGREGAR/QUITAR PERMISO'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                border       : 0
                ,defaults    : { style : 'margin:5px;' }
                ,items       : _p51_venAgregarPermisoItemsCustom
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Agregar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            var form = me.up('form');
                            
                            var ck = 'Validando datos';
                            try
                            {
                                if(!form.isValid())
                                {
                                    return datosIncompletos();
                                }
                                
                                var values = form.getValues();
                                debug('values:',values);
                                
                                _p51_mov(
                                    form
                                    ,values.tipo
                                    ,values.cdusuari
                                    ,values.cdunieco
                                    ,values.cdtipram
                                    ,values.clave
                                    ,values.funcion
                                    ,'I'
                                    ,function(){ _p51_venAgregarPermiso.close(); }
                                    );
                            }
                            catch(e)
                            {
                                manejaException(e,ck);
                            }
                        }
                    }
                ]
            })
        ]
    });
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p51_divpri'
        ,itemId   : '_p51_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,layout   :
        {
            type     : 'table'
            ,columns : 3
        }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId       : '_p51_formBusq'
                ,title       : 'B\u00DASQUEDA '
                ,defaults    : { style : 'margin:5px;' }
                ,items       : _p51_formBusqItems
                ,colspan     : 3
                ,width       : 800
                ,layout      :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Buscar'
                        ,name    : 'botonbuscar'
                        ,icon    : '${icons}zoom.png'
                        ,handler : function(me)
                        {
                            var ck = 'Buscando permisos';
                            try
                            {
                                var form = me.up('form');
                                
                                if(!form.isValid())
                                {
                                    throw 'Favor de capturar todos los campos requeridos';
                                }
                                
                                _setLoading(true,form);
                                Ext.Ajax.request(
                                {
                                    url     : _p51_urlRecuperacion
                                    ,params :
                                    {
                                        'params.consulta'  : 'RECUPERAR_PERMISOS_IMPRESION'
                                        ,'params.cdusuari' : form.down('[name=cdusuari]').getValue()
                                        ,'params.cdunieco' : form.down('[name=cdunieco]').getValue()
                                        ,'params.cdtipram' : form.down('[name=cdtipram]').getValue()
                                    }
                                    ,success : function(response)
                                    {
                                        _setLoading(false,form);
                                        var ck = 'Decodificando permisos';
                                        try
                                        {
                                            var json = Ext.decode(response.responseText);
                                            debug('### permisos:',json);
                                            if(json.success==true)
                                            {
                                                if(json.list.length==0)
                                                {
                                                    mensajeWarning('Sin resultados');
                                                }
                                                
                                                var bot = Ext.ComponentQuery.query('[xtype=button][text=Agregar]',_fieldById('_p51_panelpri'));
                                                debug('bot:',bot);
                                                for(var i in bot)
                                                {
                                                    bot[i].enable();
                                                    bot[i].cdusuari = json.params.cdusuari;
                                                    bot[i].cdunieco = json.params.cdunieco;
                                                    bot[i].cdtipram = json.params.cdtipram;
                                                }
                                                debug('bot:',bot);
                                                
                                                _p51_storeIncSuc.removeAll();
                                                _p51_storeIncAge.removeAll();
                                                _p51_storeIncUsu.removeAll();
                                                _p51_storeExcSuc.removeAll();
                                                _p51_storeExcAge.removeAll();
                                                _p51_storeExcUsu.removeAll();
                                                for(var i in json.list)
                                                {
                                                    var raw = json.list[i];
                                                    if(raw.tipo=='S')
                                                    {
                                                        if(raw.funcion=='S')
                                                        {
                                                            _p51_storeIncSuc.add(raw);
                                                        }
                                                        else
                                                        {
                                                            _p51_storeExcSuc.add(raw);
                                                        }
                                                    }
                                                    else if(raw.tipo=='A')
                                                    {
                                                        if(raw.funcion=='S')
                                                        {
                                                            _p51_storeIncAge.add(raw);
                                                        }
                                                        else
                                                        {
                                                            _p51_storeExcAge.add(raw);
                                                        }
                                                    }
                                                    else if(raw.tipo=='U')
                                                    {
                                                        if(raw.funcion=='S')
                                                        {
                                                            _p51_storeIncUsu.add(raw);
                                                        }
                                                        else
                                                        {
                                                            _p51_storeExcUsu.add(raw);
                                                        }
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                mensajeError(json.message);
                                            }
                                        }
                                        catch(e)
                                        {
                                            manejaException(e,ck);
                                        }
                                    }
                                    ,failure : function()
                                    {
                                        _setLoading(false,form);
                                        errorComunicacion(null,'Error al recuperar permisos');
                                    }
                                });
                            }
                            catch(e)
                            {
                                manejaException(e,ck);
                            }
                        }
                    }
                    ,{
                        text     : 'Limpiar'
                        ,name    : 'botonlimpiar'
                        ,icon    : '${icons}arrow_refresh.png'
                        ,handler : function(me)
                        {
                            me.up('form').getForm().reset();
                            _p51_storeIncSuc.removeAll();
                            _p51_storeIncAge.removeAll();
                            _p51_storeIncUsu.removeAll();
                            _p51_storeExcSuc.removeAll();
                            _p51_storeExcAge.removeAll();
                            _p51_storeExcUsu.removeAll();
                        }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p51_gridIncSuc'
                ,title   : 'LIMITAR POR SUCURSALES:'
                ,store   : _p51_storeIncSuc
                ,width   : 300
                ,height  : 230
                ,columns :
                [
                    {
                        text       : 'SUCURSAL'
                        ,dataIndex : 'descrip'
                        ,flex      : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,icon    : '${icons}delete.png'
                        ,width   : 30
                        ,handler : function(grid,row,col,item,e,record)
                        {
                            var store = grid.getStore();
                            debug('store:',store,'record:',record);
                            _p51_mov(
                                grid
                                ,record.get('tipo')
                                ,record.get('cdusuari')
                                ,record.get('cdunieco')
                                ,record.get('cdtipram')
                                ,record.get('tipo')=='S' ? record.get('cduniecoPer') : ( record.get('tipo')=='A' ? record.get('cdagentePer') : record.get('cdusuariPer'))
                                ,record.get('funcion')
                                ,'D'
                                );
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Agregar'
                        ,icon     : '${icons}add.png'
                        ,disabled : true
                        ,tipo     : 'S'
                        ,funcion  : 'S'
                        ,handler  : _p51_agregarClic
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p51_gridIncAge'
                ,title   : 'LIMITAR POR AGENTES:'
                ,store   : _p51_storeIncAge
                ,width   : 300
                ,height  : 230
                ,columns :
                [
                    {
                        text       : 'AGENTE'
                        ,dataIndex : 'descrip'
                        ,flex      : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,icon    : '${icons}delete.png'
                        ,width   : 30
                        ,handler : function(grid,row,col,item,e,record)
                        {
                            var store = grid.getStore();
                            debug('store:',store,'record:',record);
                            _p51_mov(
                                grid
                                ,record.get('tipo')
                                ,record.get('cdusuari')
                                ,record.get('cdunieco')
                                ,record.get('cdtipram')
                                ,record.get('tipo')=='S' ? record.get('cduniecoPer') : ( record.get('tipo')=='A' ? record.get('cdagentePer') : record.get('cdusuariPer'))
                                ,record.get('funcion')
                                ,'D'
                                );
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Agregar'
                        ,icon     : '${icons}add.png'
                        ,disabled : true
                        ,tipo     : 'A'
                        ,funcion  : 'S'
                        ,handler  : _p51_agregarClic
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p51_gridIncUsu'
                ,title   : 'LIMITAR POR USUARIOS:'
                ,store   : _p51_storeIncUsu
                ,width   : 300
                ,height  : 230
                ,columns :
                [
                    {
                        text       : 'USUARIO'
                        ,dataIndex : 'descrip'
                        ,flex      : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,icon    : '${icons}delete.png'
                        ,width   : 30
                        ,handler : function(grid,row,col,item,e,record)
                        {
                            var store = grid.getStore();
                            debug('store:',store,'record:',record);
                            _p51_mov(
                                grid
                                ,record.get('tipo')
                                ,record.get('cdusuari')
                                ,record.get('cdunieco')
                                ,record.get('cdtipram')
                                ,record.get('tipo')=='S' ? record.get('cduniecoPer') : ( record.get('tipo')=='A' ? record.get('cdagentePer') : record.get('cdusuariPer'))
                                ,record.get('funcion')
                                ,'D'
                                );
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Agregar'
                        ,icon     : '${icons}add.png'
                        ,disabled : true
                        ,tipo     : 'U'
                        ,funcion  : 'S'
                        ,handler  : _p51_agregarClic
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p51_gridExtSuc'
                ,title   : 'DESCARTAR SUCURSALES:'
                ,store   : _p51_storeExcSuc
                ,width   : 300
                ,height  : 230
                ,columns :
                [
                    {
                        text       : 'SUCURSAL'
                        ,dataIndex : 'descrip'
                        ,flex      : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,icon    : '${icons}delete.png'
                        ,width   : 30
                        ,handler : function(grid,row,col,item,e,record)
                        {
                            var store = grid.getStore();
                            debug('store:',store,'record:',record);
                            _p51_mov(
                                grid
                                ,record.get('tipo')
                                ,record.get('cdusuari')
                                ,record.get('cdunieco')
                                ,record.get('cdtipram')
                                ,record.get('tipo')=='S' ? record.get('cduniecoPer') : ( record.get('tipo')=='A' ? record.get('cdagentePer') : record.get('cdusuariPer'))
                                ,record.get('funcion')
                                ,'D'
                                );
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Agregar'
                        ,icon     : '${icons}add.png'
                        ,disabled : true
                        ,tipo     : 'S'
                        ,funcion  : 'N'
                        ,handler  : _p51_agregarClic
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p51_gridExtAge'
                ,title   : 'DESCARTAR AGENTES:'
                ,store   : _p51_storeExcAge
                ,width   : 300
                ,height  : 230
                ,columns :
                [
                    {
                        text       : 'AGENTE'
                        ,dataIndex : 'descrip'
                        ,flex      : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,icon    : '${icons}delete.png'
                        ,width   : 30
                        ,handler : function(grid,row,col,item,e,record)
                        {
                            var store = grid.getStore();
                            debug('store:',store,'record:',record);
                            _p51_mov(
                                grid
                                ,record.get('tipo')
                                ,record.get('cdusuari')
                                ,record.get('cdunieco')
                                ,record.get('cdtipram')
                                ,record.get('tipo')=='S' ? record.get('cduniecoPer') : ( record.get('tipo')=='A' ? record.get('cdagentePer') : record.get('cdusuariPer'))
                                ,record.get('funcion')
                                ,'D'
                                );
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Agregar'
                        ,icon     : '${icons}add.png'
                        ,disabled : true
                        ,tipo     : 'A'
                        ,funcion  : 'N'
                        ,handler  : _p51_agregarClic
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p51_gridExtUsu'
                ,title   : 'DESCARTAR USUARIOS:'
                ,store   : _p51_storeExcUsu
                ,width   : 300
                ,height  : 230
                ,columns :
                [
                    {
                        text       : 'USUARIO'
                        ,dataIndex : 'descrip'
                        ,flex      : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,icon    : '${icons}delete.png'
                        ,width   : 30
                        ,handler : function(grid,row,col,item,e,record)
                        {
                            var store = grid.getStore();
                            debug('store:',store,'record:',record);
                            _p51_mov(
                                grid
                                ,record.get('tipo')
                                ,record.get('cdusuari')
                                ,record.get('cdunieco')
                                ,record.get('cdtipram')
                                ,record.get('tipo')=='S' ? record.get('cduniecoPer') : ( record.get('tipo')=='A' ? record.get('cdagentePer') : record.get('cdusuariPer'))
                                ,record.get('funcion')
                                ,'D'
                                );
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Agregar'
                        ,icon     : '${icons}add.png'
                        ,disabled : true
                        ,tipo     : 'U'
                        ,funcion  : 'N'
                        ,handler  : _p51_agregarClic
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    _fieldByName('cdusuari',_fieldById('_p51_formBusq')).on(
    {
        select : function(me,selected)
        {
            var record = selected[0];
            debug('record:',record);
            _fieldByName('cdunieco',_fieldById('_p51_formBusq')).setValue(record.get('aux'));
        }
    });
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _p51_mov(cmp,tipo,cdusuari,cdunieco,cdtipram,clave,funcion,accion,callback)
{
    _setLoading(true,cmp);
    Ext.Ajax.request(
    {
        url     : _p51_urlMov
        ,params :
        {
            'params.tipo'      : tipo
            ,'params.cdusuari' : cdusuari
            ,'params.cdunieco' : cdunieco
            ,'params.cdtipram' : cdtipram
            ,'params.clave'    : clave
            ,'params.funcion'  : funcion
            ,'params.accion'   : accion
        }
        ,success : function(response)
        {
            _setLoading(false,cmp);
            var ck = 'Decodificando respuesta al movimiento de permiso';
            try
            {
                var json = Ext.decode(response.responseText);
                debug('### mov:',json);
                if(json.success==true)
                {
                    var bot = Ext.ComponentQuery.query('[xtype=button][text=Buscar]',_fieldById('_p51_panelpri'))[0];
                    bot.handler(bot);
                    
                    if(!Ext.isEmpty(callback))
                    {
                        callback();
                    }
                }
                else
                {
                    mensajeError(json.message);
                }
            }
            catch(e)
            {
                manejaException(e);
            }
        }
        ,failure : function()
        {
            _setLoading(false,cmp);
            errorComunicacion(null,'Error realizando movimiento de permiso');
        }
    });
}

function _p51_agregarClic(bot)
{
    debug('_p51_agregarClic bot:',bot,'.');
    var ck = 'Revisando datos antes de agregar permiso';
    try
    {
        if(Ext.isEmpty(bot.cdusuari)
            ||Ext.isEmpty(bot.cdunieco)
            ||Ext.isEmpty(bot.cdtipram)
        )
        {
            throw 'Favor de buscar primero el usuario y ramo';
        }
        
        var tipo = 'error';
        if(bot.tipo=='S')
        {
            if(bot.funcion=='S')
            {
                tipo = 'LIMITAR POR SUCURSAL';
            }
            else
            {
                tipo = 'DESCARTAR SUCURSAL';
            }
        }
        else if(bot.tipo=='A')
        {
            if(bot.funcion=='S')
            {
                tipo = 'LIMITAR POR AGENTE';
            }
            else
            {
                tipo = 'DESCARTAR AGENTE';
            }
        }
        else if(bot.tipo=='U')
        {
            if(bot.funcion=='S')
            {
                tipo = 'LIMITAR POR USUARIO';
            }
            else
            {
                tipo = 'DESCARTAR USUARIO';
            }
        }

        _p51_venAgregarPermiso.down('form').getForm().reset();

        _p51_venAgregarPermiso.down('[name=dstipo]').setValue(tipo);
        
        _p51_venAgregarPermiso.down('[name=tipo]').setValue(bot.tipo);
            
        _p51_venAgregarPermiso.down('[name=cdusuari]').setValue(bot.cdusuari);
        
        _p51_venAgregarPermiso.down('[name=cdunieco]').setValue(bot.cdunieco);
        
        _p51_venAgregarPermiso.down('[name=cdtipram]').setValue(bot.cdtipram);
        
        _p51_venAgregarPermiso.down('[name=funcion]').setValue(bot.funcion);
        
        if(bot.tipo=='S')
        {
            var cdunieco = _p51_venAgregarPermiso.down('[fieldLabel=SUCURSAL PERMISO]');
            cdunieco.heredar(true);
            cdunieco.enable();
            cdunieco.show();
            
            var cdagente = _p51_venAgregarPermiso.down('[fieldLabel=AGENTE PERMISO]');
            cdagente.disable();
            cdagente.hide();
            
            var cdusuari = _p51_venAgregarPermiso.down('[fieldLabel=USUARIO PERMISO]');
            cdusuari.disable();
            cdusuari.hide();
        }
        else if(bot.tipo=='A')
        {
            var cdunieco = _p51_venAgregarPermiso.down('[fieldLabel=SUCURSAL PERMISO]');
            cdunieco.disable();
            cdunieco.hide();
            
            var cdagente = _p51_venAgregarPermiso.down('[fieldLabel=AGENTE PERMISO]');
            cdagente.enable();
            cdagente.show();
            
            var cdusuari = _p51_venAgregarPermiso.down('[fieldLabel=USUARIO PERMISO]');
            cdusuari.disable();
            cdusuari.hide();
        }
        else if(bot.tipo=='U')
        {
            var cdunieco = _p51_venAgregarPermiso.down('[fieldLabel=SUCURSAL PERMISO]');
            cdunieco.disable();
            cdunieco.hide();
            
            var cdagente = _p51_venAgregarPermiso.down('[fieldLabel=AGENTE PERMISO]');
            cdagente.disable();
            cdagente.hide();
            
            var cdusuari = _p51_venAgregarPermiso.down('[fieldLabel=USUARIO PERMISO]');
            cdusuari.enable();
            cdusuari.show();
        }
        
        centrarVentanaInterna(_p51_venAgregarPermiso.show());
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}
////// funciones //////
</script>
</head>
<body>
<div id="_p51_divpri" style="height:600px;border:1px solid #CCCCCC;"></div>
</body>
</html>