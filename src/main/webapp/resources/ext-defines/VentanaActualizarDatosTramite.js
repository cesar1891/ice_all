Ext.define('VentanaActualizarDatosTramite',
{
    extend      : 'Ext.window.Window',
    title       : 'ACTUALIZAR INFORMACI\u00d3N DEL TR\u00c1MITE',
    itemId      : '_c8_instance',
    closeAction : 'destroy',
    modal       : true,
    border      : 0,
    constructor : function (config) {
        debug('_c8_instance constructor args:', arguments);
        var me = this;
        if (Ext.isEmpty(config)) {
            throw 'No se recibieron par\u00e1metros para la ventana de datos de tr\u00e1mite';
        }
        if (Ext.isEmpty(config.aux)) {
            throw 'Falta definir el auxiliar para la ventana de datos de tr\u00e1mite';
        }
        var auxJson = 'error';
        try {
            auxJson = Ext.decode(config.aux);
            debug('auxJson:', auxJson, 'typeof:', typeof auxJson);
            if (typeof auxJson !== 'object') {
                auxJson = 'error';
            }
        } catch (e) {
            debugError('Error al decodificar el auxiliar de ventana de datos de tr\u00e1mite', e);
        }
        if (auxJson === 'error') {
            throw 'El auxiliar definido para la ventana de datos de tr\u00e1mite debe ser JSON';
        }
        if (Ext.isEmpty(auxJson.SECCION)) {
            throw 'Falta definir la secci\u00f3n en el auxiliar para la ventana de datos de tr\u00e1mite';
        }
        me.auxJson = auxJson;
        debug('me:', this);
        var callback = function (comps) {
            debug('callback args:', arguments);
            var ck;
            try {
                me.down('form').add(comps.items);
                comps.items[0].focus();
                ck = 'Recuperando datos de tr\u00e1mite';
                _request({
                    mask   : ck,
                    url    : _GLOBAL_URL_VALIDACION_MONTAR_DATOS,
                    params : {
                        'flujo.cdtipflu'  : me.cdtipflu,
                        'flujo.cdflujomc' : me.cdflujomc,
                        'flujo.tipoent'   : me.tipoent,
                        'flujo.claveent'  : me.claveent,
                        'flujo.webid'     : me.webid,
                        'flujo.ntramite'  : me.ntramite,
                        'flujo.status'    : me.status,
                        'flujo.cdunieco'  : me.cdunieco,
                        'flujo.cdramo'    : me.cdramo,
                        'flujo.estado'    : me.estado,
                        'flujo.nmpoliza'  : me.nmpoliza,
                        'flujo.nmsituac'  : me.nmsituac,
                        'flujo.nmsuplem'  : me.nmsuplem
                    },
                    success : function (json) {
                        var ck = 'Cargando valores del tr\u00e1mite';
                        try {
                            for (var att in json.datosTramite.TRAMITE) {
                                try {
                                    if (!Ext.isEmpty(json.datosTramite.TRAMITE[att])) {
                                        me.down('[name*=' + (att.toLowerCase()) + ']').setValue(json.datosTramite.TRAMITE[att]);
                                    }
                                } catch (e) {}
                            }
                        } catch (e) {
                            manejaException(e, ck);
                        }
                    }
                });
            } catch (e) {
                manejaException(e, ck);
            }
        };
        var callbackError = function () {
            me.close();
        };
        Ext.apply(me, {
            items : [
                {
                    xtype       : 'form',
                    border      : 0,
                    layout      : {
                        type    : 'table',
                        columns : 2
                    },
                    items : [
                        {
                            xtype      : 'numberfield',
                            value      : config.ntramite,
                            fieldLabel : 'TR\u00c1MITE',
                            allowBlank : false,
                            readOnly   : true,
                            style      : 'margin : 5px;',
                            name       : 'ntramite'
                        }, {
                            xtype      : 'datefield',
                            value      : new Date(),
                            fieldLabel : 'FECHA',
                            format     : 'd/m/Y',
                            readOnly   : true,
                            style      : 'margin : 5px;'
                        }
                    ]
                }
            ],
            buttonAlign : 'center',
            buttons     : [
                {
                    text    : 'Guardar y continuar',
                    icon    : _GLOBAL_DIRECTORIO_ICONOS + 'disk.png',
                    handler : me.guardarClic
                }
            ]
        });
        _obtenerComponentes(
            {
                pantalla : 'PANTALLA_DATOS_TRAMITE',
                seccion  : auxJson.SECCION,
                items    : true
            },
            callback,
            callbackError
        );
        this.callParent(arguments);
    },
    mostrar : function () {
        var me = this;
        centrarVentanaInterna(me.show());
    },
    guardarClic : function (bot) {
        debug('guardarClic args:', arguments);
        var ck;
        try {
            ck = 'Validando datos';
            var win  = bot.up('window'),
                form = win.down('form');
            if (!form.isValid()) {
                return datosIncompletos();
            }
            var values = form.getValues();
            debug('values:', values);
            
            var cbAcciones = function (lista) {
                if (lista.length === 0) {
                    win.close();
                    try {
                        var wins = Ext.ComponentQuery.query('[itemId=_p54_windowAcciones]');
                        for (var i = 0; i < wins.length; i++) {
                            wins[i].close();
                        }
                    } catch (e) {}
                    try {
                        _p54_cargarTramite(values.ntramite);
                    } catch (e) {}
                } else if (lista.length === 1) {
                    _procesaAccion(
                        win.cdtipflu,
                        win.cdflujomc,
                        lista[0].TIPODEST,
                        lista[0].CLAVEDEST,
                        lista[0].WEBIDDEST,
                        lista[0].AUX || win.aux,
                        win.ntramite,
                        win.status,
                        win.cdunieco,
                        win.cdramo,
                        win.estado,
                        win.nmpoliza,
                        win.nmsituac,
                        win.nmsuplem,
                        win.cdusuari,
                        win.cdsisrol,
                        win.callback
                    );
                    win.close();
                } else {
                    centrarVentanaInterna(Ext.create('Ext.window.Window', {
                        title       : 'ACCIONES',
                        itemId      : '_c8_windowAcciones',
                        width       : 500,
                        height      : 150,
                        buttonAlign : 'center',
                        modal       : true,
                        buttons     : [],
                        border      : 0,
                        items       : [
                            {
                                xtype  : 'displayfield',
                                value  : 'Puede seleccionar una operaci\u00f3n del men\u00fa inferior',
                                margin : 5
                            }
                        ]
                    }).show());
                    _cargarBotonesEntidad(
                        win.cdtipflu,
                        win.cdflujomc,
                        win.tipoent,
                        win.claveent,
                        win.webid,
                        '_c8_windowAcciones',
                        win.ntramite,
                        win.status,
                        win.cdunieco,
                        win.cdramo,
                        win.estado,
                        win.nmpoliza,
                        win.nmsituac,
                        win.nmsuplem,
                        win.callback
                    );
                    win.close();
                }
            };
            
            var cbGuardar = function (json) {
                _cargarAccionesEntidad(win.cdtipflu, win.cdflujomc, win.tipoent, win.claveent, win.webid, cbAcciones);
            };
            
            ck = 'Guardando datos';
            _request({
                mask    : ck,
                url     : _GLOBAL_COMP_URL_GUARDAR_DATOS_TRAMITE,
                params  : _formValuesToParams(values),
                success : cbGuardar
            });            
        } catch (e) {
            manejaException(e, ck);
        }
    }
});