Ext.define('VentanaCambioTipoEndoso', {
    extend      : 'Ext.window.Window',
    title       : 'Cambiar motivo de endoso',
    itemId      : '_c9_instance',
    closeAction : 'destroy',
    modal       : true,
    border      : 0,
    mostrar     : function () {
        var me = this;
        centrarVentanaInterna(me.show());
    },
    constructor : function (config) {
        var me = this;
        debug('config:',config);
        if (Ext.isEmpty(config)) {
            throw '#C9 - No se recibieron datos';
        }
        Ext.apply(me, {
            items : [
                {
                    xtype    : 'form',
                    border   : 0,
                    defaults : { style : 'margin:5px;' },
                    items    : [
                        {
                            xtype           : 'combobox',
                            fieldLabel      : 'Motivo de endoso',
                            valueField      : 'key',
                            displayField    : 'value',
                            editable        : true,
                            forceSelection  : true,
                            typeAhead       : true,
                            anyMatch        : true,
                            matchFieldWidth : false,
                            name            : 'CDTIPSUP',
                            allowBlank      : false,
                            queryMode       : 'local',
                            listConfig      : {
                                maxHeight:150,
                                minWidth:120
                            },
                            store           : Ext.create('Ext.data.Store', {
                                model    : 'Generic',
                                autoLoad : true,
                                proxy    : {
                                    type        : 'ajax',
                                    url         : _GLOBAL_CONTEXTO + '/catalogos/obtieneCatalogo.action',
                                    extraParams : {
                                        catalogo          : 'TIPOS_ENDOSO_X_TRAMITE',
                                        'params.ntramite' : config.ntramite
                                    },
                                    reader : {
                                        type         : 'json',
                                        root         : 'lista',
                                        rootProperty : 'lista'
                                    }
                                }
                            })
                        }, {
                            xtype      : 'textarea',
                            labelAlign : 'top',
                            fieldLabel : 'Comentarios',
                            width      : 400,
                            height     : 200,
                            name       : 'COMMENTS'
                        }, {
                            xtype      : 'radiogroup',
                            fieldLabel : 'Mostrar al agente',
                            columns    : 2,
                            width      : 250,
                            style      : 'margin:5px;',
                            hidden     : _GLOBAL_CDSISROL === RolSistema.Agente,
                            items      : [
                                {
                                    boxLabel   : 'Si',
                                    itemId     : 'SWAGENTE',
                                    name       : 'SWAGENTE',
                                    inputValue : 'S',
                                    checked    : _GLOBAL_CDSISROL === RolSistema.Agente
                                }, {
                                    boxLabel   : 'No',
                                    name       : 'SWAGENTE',
                                    inputValue : 'N',
                                    checked    : _GLOBAL_CDSISROL !== RolSistema.Agente
                                }
                            ]
                        }
                    ]
                }
            ],
            buttonAlign : 'center',
            buttons     : [
                {
                    text    : 'Guardar',
                    icon    : _GLOBAL_DIRECTORIO_ICONOS + 'disk.png',
                    handler : function(me) {
                        var win = me.up('window');
                        var ck = 'Cambiando motivo de endoso';
                        try {
                            var form = win.down('form');
                            if (!form.isValid()) {
                                throw 'Favor de revisar los datos';
                            }
                            var values = form.getValues();
                            values.SWAGENTE = _fieldById('SWAGENTE', win).getGroupValue();
                            values.NTRAMITE = win.ntramite;
                            values.STATUS   = win.status;
                            debug('values:',values);
                            
                            _request({
                                mask    : ck,
                                url     : _GLOBAL_COMP_URL_CAMBIAR_TIPO_ENDOSO,
                                params  : _formValuesToParams(values),
                                success : function(json) {
                                    _mask('Recargando...');
                                    location.reload();
                                }
                            });
                        } catch(e) {
                            manejaException(e, ck);
                        }
                    }
                }
            ]
        });
        this.callParent(arguments);
    }
});