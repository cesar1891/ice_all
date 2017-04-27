<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _ejecutaQuery = '<s:url namespace="/consultasPoliza" action="ejecutaQuery"/>';
////// urls //////

////// variables //////
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
////// componentes dinamicos //////

Ext.onReady(function () {

    Ext.Ajax.timeout = 30*60*1000;
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel', {
        title    : 'Panel principal',
        panelPri : 'S',
        renderTo : '_p100_divpri',
        border   : 0,
        defaults : {
            style : 'margin:5px;'
        },
    	items    : [
    	    Ext.create('Ext.panel.Panel', {
                title     : 'Sentencia',
                bodyStyle : 'padding-bottom: 10px;',
                defaults  : {
                    style : 'margin:5px;'
                },
                items     : [
                    {
                        xtype      : 'textareafield',
                        itemId	   : 'queryVal',
                        name       : 'codigo',
                        grow       : true,
                        fieldLabel : 'Codigo',
                        width      : 900,
                        height 	   : 300
                    },
                    new TextfieldCodificado({
                        fieldLabel : 'Password',
                        name       : 'password',
                        itemId     : 'password'
                    })
                ],
                buttons : [
                    {
                        text        : 'Ejecutar',
                        icon        : '${icons}lightning.png',
                        buttonAlign : 'center',
                        handler     : function (me) {
                            var values = me.up('form');
                            debug('values ',values);
                            _ejecutarQueryPantallaConsultaBD(_fieldById('queryVal').getValue(), _fieldById('password').getValue());
                        }
                    }
                ]
            }),
            Ext.create('Ext.panel.Panel', {
                title  : 'Resultados',
                border : 0,
                items  : [
                    Ext.create('Ext.grid.Panel', {
                        title    : 'Cursor',
                        itemId  : 'griCursor',
                        height  : 400,
                        columns : []
                    })
                ]
            }),
            Ext.create('Ext.form.Panel', {
                title    : 'Extractor',
                defaults : {
                    style : 'margin : 5px;'
                },
                layout : {
                    type    : 'table',
                    columns : 3
                },
                items : [
                    {
                        xtype      : 'textfield',
                        name       : 'OBJETO',
                        fieldLabel : 'OBJETO',
                        allowBlank : false
                    }, {
                        xtype           : 'combo',
                        fieldLabel      : 'TIPO',
                        valueField      : 'key',
                        name            : 'TIPO',
                        displayField    : 'value',
                        matchFieldWidth : false,
                        forceSelection  : true,
                        allowBlank      : false,
                        listConfig      : {
                            maxHeight : 150,
                            minWidth : 120
                        },
                        queryMode       : 'local',
                        store           : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            data  : [
                                {
                                    key   : 'FUNCTION',
                                    value : 'FUNCTION'
                                }, {
                                    key   : 'JAVA SOURCE',
                                    value : 'JAVA SOURCE'
                                }, {
                                    key   : 'LIBRARY',
                                    value : 'LIBRARY'
                                }, {
                                    key   : 'PACKAGE',
                                    value : 'PACKAGE'
                                }, {
                                    key   : 'PACKAGE BODY',
                                    value : 'PACKAGE BODY'
                                }, {
                                    key   : 'PROCEDURE',
                                    value : 'PROCEDURE'
                                }, {
                                    key   : 'TRIGGER',
                                    value : 'TRIGGER'
                                }, {
                                    key   : 'TYPE',
                                    value : 'TYPE'
                                }, {
                                    key   : 'TYPE BODY',
                                    value : 'TYPE BODY'
                                }
                            ]
                        })
                    }, {
                        xtype   : 'button',
                        text    : 'Extraer',
                        handler : function (me) {
                            try {
                                var form = me.up('form');
                                if (!form.isValid()) {
                                    throw datosIncompletos();
                                }
                                
                                if (!_fieldById('password').isValid()) {
                                    _fieldById('password').focus();
                                    throw mensajeWarning('Favor de introducir su clave');
                                }
                                
                                var objeto = _fieldByName('OBJETO').getValue(),
                                    tipo   = _fieldByName('TIPO').getValue(),
                                    query  = "SELECT TEXT FROM ALL_SOURCE WHERE TYPE = '" + tipo + "' AND NAME = '" + objeto + "'"  
                                    pass   = _fieldById('password').getValue();
                                debug('query:', query);
                                _ejecutarQueryPantallaConsultaBD(query, pass, true);
                            } catch (e) {
                                debugError('error al extraer', e);
                            }
                        }
                    }, {
                        xtype   : 'textarea',
                        itemId  : 'extractorTextarea',
                        width   : 900,
                        height  : 500,
                        colspan : 3
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _ejecutarQueryPantallaConsultaBD (query, pass, extractor) {
    debug('_ejecutarQueryPantallaConsultaBD args:', arguments);
    _mask('Ejecutando...');
    Ext.Ajax.request({
        url     : _ejecutaQuery,
        params : {
            'params.query'    : query,
            'params.password' : pass
        },
        success : function (response){
            _unmask();
            var json = Ext.decode(response.responseText);
            debug('json',json);
            
            if (json.success !== true) {
                mensajeError(json.mensajeRes);
                return;
            }
            
            var list = json.loadList;
            if (list.length > 0) {
                if (extractor === true) {
                    var a = '';
                    for (var i = 0; i < list.length; i++) {
                        a = a + list[i].TEXT;
                    }
                    _fieldById('extractorTextarea').setValue(a);
                } else {
                    var keys1 = [];
                    //Se obtienen keys de objetos
                    for (var k in list[0]) {
                        keys1.push(k);
                    }
                    Ext.define('_modeloCursor', {
                        extend : 'Ext.data.Model',
                        fields : keys1
                    });
                    var columns = [];
                    for (var i = 0; i < keys1.length; i++) {
                        columns.push({
                            dataIndex : keys1[i],
                            text      : keys1[i],
                            flex      : 1
                        });
                    }
                    var store = Ext.create('Ext.data.Store', {
                        model : '_modeloCursor',
                        data : list
                    });
                    _fieldById('griCursor').reconfigure(store, columns);
                }
            }
            mensajeCorrecto('query ejecutado con exito','exito');
        },
        failure : function () {
            _unmask();
            errorComunicacion(null,'error de red al ejecutar query');
        }
    });
}
////// funciones //////
</script>
</head>
<body>
<div id="_p100_divpri" style="height:1600px;border:1px solid #CCCCCC"></div>
</body>
</html>