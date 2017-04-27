Ext.define('VentanaRecuperacionCotiColec', {
    extend      : 'Ext.window.Window',
    itemId      : '_c7_instance',
    closeAction : 'destroy',
    modal       : true,
    mostrar     : function () {
        var me = this;
        return centrarVentanaInterna(me.show());
    },
    constructor : function (config) {
        var me = this;
        Ext.apply(me, {
            title : 'COTIZACIONES APROBADAS PARA EL TR\u00c1MITE ' + config.ntramite,
            items : [
                Ext.create('Ext.grid.Panel', {
                    width      : 400,
                    height     : 250,
                    autoScroll : true,
                    store      : new Ext.data.Store({
                        fields : [
                            'CDUNIECO',
                            'CDRAMO',
                            'ESTADO',
                            'NMPOLIZA',
                            'PRIMA'
                        ],
                        autoLoad : true,
                        proxy    : {
                            type        : 'ajax',
                            url         : _GLOBAL_URL_RECUPERACION, 
                            extraParams : {
                                'params.consulta' : 'RECUPERAR_COTIZACIONES_COLECTIVAS_APROBADAS',
                                'params.ntramite' : config.ntramite
                            },
                            reader      : {
                                type : 'json',
                                root : 'list'
                            }
                        },
                        listeners : {
                            load : function (me, records) {
                                if (records.length === 0) {
                                    mensajeWarning('No hay cotizaciones aprobadas anteriores');
                                    _fieldById('_c7_instance').close();
                                }
                            }
                        }
                    }),
                    columns : [
                        {
                            text      : 'COTIZACI\u00d3N',
                            dataIndex : 'NMPOLIZA',
                            width     : 100
                        }, {
                            text      : 'PRIMA',
                            dataIndex : 'PRIMA',
                            flex      : 1,
                            renderer  : Ext.util.Format.usMoney
                        }, {
                            xtype   : 'actioncolumn',
                            width   : 50,
                            items   :
                            [
                                {
                                    icon    : _GLOBAL_DIRECTORIO_ICONOS + 'chart_bar_edit.png',
                                    tooltip : 'RECUPERAR',
                                    handler : function (me, row, col, item, e, record) {
                                        var win = me.up('window');
                                        win.recuperar(record.get('NMPOLIZA'), function () {
                                            mensajeCorrecto(
                                                'Cotizaci\u00f3n recuperada',
                                                'Se ha recuperado y ligado la cotizaci\u00f3n ' + record.get('NMPOLIZA')
                                                + ' para el tr\u00e1mite ' + win.ntramite,
                                                function () {
                                                    try {
                                                        var form  = _fieldById('_p54_filtroForm');
                                                        var boton = _fieldById('_p54_filtroForm').down('button[text=Buscar]');
                                                        form.getForm().reset();
                                                        form.down('[name=NTRAMITE]').setValue(win.ntramite);
                                                        form.down('[name=STATUS]').setValue('0');
                                                        _fieldById('_p54_filtroCmp').reset();
                                                        var callbackCheck = function(store, records, success) {
                                                            store.removeListener('load', callbackCheck);
                                                            var indexRecord = _p54_store.find('NTRAMITE', win.ntramite);
                                                            debug('indexRecord:', indexRecord, '.');
                                                            if (indexRecord !== -1) {
                                                                var _p54_grid = _fieldById('_p54_grid');
                                                                _p54_grid.getSelectionModel().deselectAll();
                                                                _p54_grid.getSelectionModel().select(indexRecord);
                                                            }
                                                        };
                                                        _p54_store.on({
                                                            load : callbackCheck
                                                        });
                                                        boton.handler(boton);
                                                        win.close();
                                                        _fieldById('_p54_windowAcciones').close();
                                                    } catch (e) {
                                                        debugError(e);
                                                        _mask('Redireccionando...');
                                                        Ext.create('Ext.form.Panel').submit(
                                                        {
                                                            url             : _GLOBAL_COMP_URL_MCFLUJO
                                                            ,standardSubmit : true
                                                        });
                                                    }
                                                }
                                            );
                                        });
                                    }
                                }, {
                                    icon    : _GLOBAL_DIRECTORIO_ICONOS + 'user_go.png',
                                    tooltip : 'ENVIAR A NUEVA APROBACI\u00D3N',
                                    handler : function (me, row, col, item, e, record) {
                                        debug('ENVIAR A NUEVA APROBACI\u00D3N clic! args', arguments);
                                        var win = me.up('window');
                                        win.recuperar(record.get('NMPOLIZA'), function () {
                                            var mask, ck = 'Turnando tr\u00e1mite';
                                            try {
                                                mask = _maskLocal(ck);
                                                Ext.Ajax.request({
                                                    url     : _GLOBAL_COMP_URL_TURNAR,
                                                    params  : {
                                                        'params.CDTIPFLU'  : win.cdtipflu,
                                                        'params.CDFLUJOMC' : win.cdflujomc,
                                                        'params.NTRAMITE'  : win.ntramite,
                                                        'params.STATUSOLD' : win.status,
                                                        'params.STATUSNEW' : '14',
                                                        'params.SWAGENTE'  : 'S',
                                                        'params.COMMENTS'  : 'Se recupera y env\u00eda a aprobaci\u00f3n la cotizaci\u00f3n '
                                                            + record.get('NMPOLIZA')
                                                    },
                                                    success : function (response) {
                                                        mask.close();
                                                        var ck = 'Decodificando respuesta al turnar tr\u00e1mite';
                                                        try {
                                                            var jsonTurnar = Ext.decode(response.responseText);
                                                            debug('### turnar:', jsonTurnar);
                                                            if (jsonTurnar.success === true) {
                                                                mensajeCorrecto(
                                                                    'Tr\u00e1mite turnado',
                                                                    jsonTurnar.message,
                                                                    function () {
                                                                        _mask('Redireccionando...');
                                                                        Ext.create('Ext.form.Panel').submit(
                                                                        {
                                                                            url             : _GLOBAL_COMP_URL_MCFLUJO
                                                                            ,standardSubmit : true
                                                                        });
                                                                    }
                                                                );
                                                            } else {
                                                                mensajeError(jsonTurnar.message);
                                                            }
                                                        } catch (e) {
                                                            manejaException(e, ck);
                                                        }
                                                    },
                                                    failure : function () {
                                                        mask.close();
                                                        errorComunicacion(null, 'Error al turnar tr\u00e1mite');
                                                    }
                                                });
                                            } catch (e) {
                                                manejaException(e, ck, mask);
                                            }
                                        });
                                    }
                                }
                            ]
                        }
                    ]
                })
            ]
        });
        this.callParent(arguments);
    },
    recuperar : function (nmpoliza, callback) {
        var win = this;
        var mask, ck = 'Recuperando cotizaci\u00f3n anterior';
        try {
            mask = _maskLocal(ck);
            Ext.Ajax.request({
                url     : _GLOBAL_COMP_RECUPERAR_COTI_COLEC,
                params  : {
                    'params.ntramite' : win.ntramite,
                    'params.nmsolici' : nmpoliza,
                    'params.status'   : win.status
                },
                success : function (response) {
                    mask.close();
                    var ck = 'Decodificando respuesta el recuperar cotizaci\u00f3n anterior';
                    try {
                        var json = Ext.decode(response.responseText);
                        debug('### recuperar coti colec:', json);
                        if (json.success === true) {
                            callback();
                        } else {
                            mensajeError(json.message);
                        }
                    } catch (e) {
                        manejaException(e, ck);
                    }
                },
                failure : function () {
                    mask.close();
                    errorComunicacion(null, 'Error al recuperar cotizaci\u00f3n anterior');
                }
            });
        } catch (e) {
            manejaException(e, ck, mask);
        }
    }
});