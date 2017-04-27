Ext.define('VentanaCorreoEmision', {
    extend        : 'Ext.window.Window',
    itemId        : '_c6_instance',
    closeAction   : 'destroy',
    modal         : true,
    width         : 350,
    height        : 150,
    cls           : 'VENTANA_DOCUMENTOS_CLASS',
    mostrar       : function () {
        var me = this;
        return centrarVentanaInterna(me.show());
    },
    constructor : function (config) {
        debug('config:', config);
        var me = this;
        
        Ext.apply(me, {
            title : 'Enviar correo de emisi\u00f3n del tr\u00e1mite' + (Ext.isEmpty(config.ntramite)
                        ? ''
                        : ' '+config.ntramite),
            items : [
                {
                    xtype      : 'textfield',
                    fieldLabel : 'Correo',
                    name       : 'correo',
                    allowBlank : false,
                    vtype      : 'email',
                    style      : 'margin : 5px;'
                }
            ],
            buttonAlign : 'center',
            buttons     :
            [
                {
                    text    : 'Enviar',
                    icon    : _GLOBAL_DIRECTORIO_ICONOS + 'email.png',
                    handler : function (me) {
                        var mask, ck = 'Enviando correo de emisi\u00f3n';
                        try {
                            var win = me.up('window');
                            if (!win.down('[name=correo]').isValid()) {
                                throw 'Favor de introducir un correo v\u00e1lido';
                            }
                            if (Ext.isEmpty(win.correo)) {
                                throw 'El correo recuperado no es v\u00e1lido';
                            }
                            mask = _maskLocal(ck);
                            Ext.Ajax.request({
                                url     : _GLOBAL_URL_ENVIAR_CORREO,
                                params  : {
                                    to      : win.down('[name=correo]').getValue(),
                                    asunto  : 'Documentaci\u00f3n de emisi\u00f3n del tr\u00e1mite ' + win.ntramite,
                                    mensaje : win.correo,
                                    html    : true
                                },
                                success : function (response) {
                                    mask.close();
                                    var ck = 'Decodificando respuesta al enviar correo';
                                    try {
                                        var json = Ext.decode(response.responseText);
                                        debug('### enviar correo:', json);
                                        if (json.success === true) {
                                            mensajeCorrecto(
                                                'Correo enviado',
                                                'Correo enviado',
                                                function () {
                                                    var win = _fieldById('_c6_instance');
                                                    debug('_c6_instance:', win, '.');
                                                    var callbackRemesa = function () {
                                                        _fieldById('_c6_instance').destroy();
                                                        _mask('Redireccionando...');
                                                        Ext.create('Ext.form.Panel').submit(
                                                        {
                                                            url             : _GLOBAL_COMP_URL_MCFLUJO
                                                            ,standardSubmit : true
                                                        });
                                                    };
                                                    if (Number(win.status) === 3) {
                                                        _generarRemesaClic(
                                                            false,
                                                            win.cdunieco,
                                                            win.cdramo,
                                                            win.estado,
                                                            win.nmpoliza,
                                                            callbackRemesa,
                                                            null,
                                                            win.ntramite
                                                        );
                                                    } else {
                                                        win.destroy();
                                                    }
                                                }
                                            );
                                        } else {
                                            mensajeError('Error en el servidor al enviar el correo');
                                        }
                                    } catch (e) {
                                        manejaException(e, ck);
                                    }
                                },
                                failure : function () {
                                    mask.close();
                                    errorComunicacion(null, 'Error al enviar el correo');
                                }
                            });
                        } catch (e) {
                            manejaException(e, ck);
                        }
                    }
                }
            ],
            listeners : {
                afterrender : function (me) {
                    var mask, ck = 'Verificando existencia de correo';
                    try {
                        mask = _maskLocal(ck);
                        Ext.Ajax.request({
                            url     : _GLOBAL_URL_RECUPERACION,
                            params  : {
                                'params.consulta' : 'RECUPERAR_CORREO_EMISION_TRAMITE',
                                'params.ntramite' : me.ntramite
                            },
                            success : function (response) {
                                mask.close();
                                var ck = 'Decodificando respuesta al recuperar correo de emisi\u00f3n';
                                try {
                                    var json = Ext.decode(response.responseText);
                                    debug('### correo emision tramite:', json);
                                    if (json.success === true) {
                                        if (Ext.isEmpty(json.params.correo)) {
                                            mensajeWarning(
                                                'No se registr\u00f3 correo en la emisi\u00f3n',
                                                function () {
                                                    _fieldById('_c6_instance').destroy();
                                                }
                                            );
                                        } else {
                                            _fieldById('_c6_instance').correo = json.params.correo;
                                            
                                        }
                                    } else {
                                        mensajeError(json.message);
                                    }
                                } catch (e) {
                                    manejaException(e, ck);
                                }
                            },
                            failure : function () {
                                mask.close();
                                errorComunicacion(null, 'Error al recuperar correo de emisi\u00f3ne');
                            }
                        });
                    } catch (e) {
                        manejaException(e, ck, mask);
                    }
                }
            }
        });
        this.callParent(arguments);
    }
});