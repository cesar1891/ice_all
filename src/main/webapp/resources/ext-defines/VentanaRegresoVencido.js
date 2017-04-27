Ext.define('VentanaRegresoVencido',
{
    extend      : 'Ext.window.Window',
    title       : 'REGRESAR TR\u00c1MITE VENCIDO',
    itemId      : '_c4_instance',
    closeAction : 'destroy',
    modal       : true,
    width       : 600,
    border      : 0,
    mostrar     : function()
    {
        var me = this;
        centrarVentanaInterna(me.show());
    },
    constructor : function(config)
    {
        var me = this;
        Ext.apply(me,
        {
            defaults : { style : 'margin : 5px;' },
            layout : {
                type    : 'table',
                columns : 2
            },
            items :
            [
                {
                    xtype      : 'displayfield',
                    fieldLabel : 'Tr\u00e1mite',
                    value      : config.ntramite
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Estatus vencido',
                    name       : 'status',
                    value      : 'Cargando...'
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Fecha recepci\u00f3n',
                    name       : 'fecstatu',
                    value      : 'Cargando...'
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Fecha aplicaci\u00f3n vencimiento',
                    name       : 'feregistro',
                    value      : 'Cargando...'
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Fecha primera alerta',
                    name       : 'feamarilla',
                    value      : 'Cargando...'
                }, {
                    xtype : 'image',
                    src   : _GLOBAL_DIRECTORIO_ICONOS + 'flag_yellow.png'
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Fecha segunda alerta',
                    name       : 'feroja',
                    value      : 'Cargando...'
                }, {
                    xtype : 'image',
                    src   : _GLOBAL_DIRECTORIO_ICONOS + 'flag_red.png'
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Fecha vencimiento',
                    name       : 'fevencim',
                    value      : 'Cargando...'
                }, {
                    xtype : 'image',
                    src   : _GLOBAL_DIRECTORIO_ICONOS + 'clock_red.png'
                }, {
                    xtype      : 'displayfield',
                    fieldLabel : 'Roles que pueden regresar',
                    name       : 'roles',
                    value      : 'Cargando...',
                    colspan    : 2,
                    width      : 500
                }
            ],
            buttonAlign : 'center',
            buttons     : [
                {
                    text     : 'REGRESAR'
                    ,icon    : _GLOBAL_DIRECTORIO_ICONOS + 'control_repeat_blue.png'
                    ,handler : function (me) {
                        var mask, ck = 'Regresando tr\u00e1mite vencido';
	                    try {
	                        mask = _maskLocal(ck);
	                        Ext.Ajax.request({
	                            url     : _GLOBAL_URL_REGRESAR_TRAM_VENCIDO,
	                            params  : {
	                                'params.ntramite'    : me.up('window').ntramite,
	                                'params.soloRevisar' : 'N'
	                            },
	                            success : function (response) {
	                                mask.close();
	                                var ck = 'Decodificando respuesta al regresar tr\u00e1mite vencido';
	                                try {
	                                    var json = Ext.decode(response.responseText);
	                                    debug('### permisos vencido:', json);
	                                    if (json.success === true) {
	                                        mensajeCorrecto(
	                                            'Tr\u00e1mite regresado',
	                                            'El tr\u00e1mite ' + json.params.ntramite + ' fue regresado al estatus ' + json.params.DSSTATUS,
	                                            function () {
	                                                _mask('Redireccionando');
                                                    Ext.create('Ext.form.Panel').submit({
                                                        url            : _GLOBAL_COMP_URL_MCFLUJO,
                                                        standardSubmit : true
                                                    });
	                                            }
	                                        );
	                                    } else {
	                                        mensajeError(json.message);
	                                    }
	                                } catch(e) {
	                                    manejaException(e, ck);
	                                }
	                            },
	                            failure : function () {
	                                mask.close();
	                                errorComunicacion(null, 'Error al regresar tr\u00e1mite vencido');
	                            }
	                        });
	                    } catch (e) {
	                        manejaException(e, ck, mask);
	                    }
                    }
                }
            ],
            listeners : {
                afterrender : function (me) {
                    debug('VentanaRegresoVencido.afterrender!');
                    var mask, ck = 'Recuperando permisos para regresar tr\u00e1mite vencido';
                    try {
                        mask = _maskLocal(ck);
                        Ext.Ajax.request({
                            url     : _GLOBAL_URL_REGRESAR_TRAM_VENCIDO,
                            params  : {
                                'params.ntramite'    : me.ntramite,
                                'params.soloRevisar' : 'S'
                            },
                            success : function (response) {
                                mask.close();
                                var ck = 'Decodificando respuesta al recuperar permisos';
                                try {
                                    var json = Ext.decode(response.responseText);
                                    debug('### permisos vencido:', json);
                                    if (json.success === true) {
                                        me.down('[name=status]').setValue(json.params.DSSTATUS);
                                        me.down('[name=roles]').setValue(json.params.rolesCadena);
                                        me.down('[name=fecstatu]').setValue(json.params.FECSTATU);
                                        me.down('[name=feamarilla]').setValue(json.params.FEAMARILLA);
                                        me.down('[name=feroja]').setValue(json.params.FEROJA);
                                        me.down('[name=fevencim]').setValue(json.params.FEVENCIM);
                                        me.down('[name=feregistro]').setValue(json.params.FEREGISTRO);
                                        if (json.params.permiso !== 'S') {
                                            me.down('[xtype=button]').disable();
                                        }
                                    } else {
                                        mensajeError(json.message);
                                    }
                                } catch(e) {
                                    manejaException(e, ck);
                                }
                            },
                            failure : function () {
                                mask.close();
                                errorComunicacion(null, 'Error al revisar permisos para regresar tr\u00e1mite vencido');
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