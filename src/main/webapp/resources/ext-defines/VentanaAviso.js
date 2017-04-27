Ext.define('VentanaAviso', {
    extend      : 'Ext.window.Window',
    title       : 'Aviso',
    itemId      : '_c3_instance',
    closeAction : 'destroy',
    modal       : true,
    autoScroll  : true,
    border      : 0,
    mostrar     : function () {
        var me = this;
        centrarVentanaInterna(me.show());
    },
    buttonAlign : 'center',
    buttons     : [
        {
            text     : 'Aceptar',
            icon     : _GLOBAL_DIRECTORIO_ICONOS + 'accept.png',
            disabled : true,
            handler  : function (me) {
                me.up('window').destroy();
            }
        }
    ],
    constructor : function (config) {
        debug('config:', config, '.');
        var me = this;
        me.config = config;
        Ext.apply(me, {
            html : '<p style="margin : 10px;">' + _NVL(config.aux, '(sin contenido)') + '</p>',
            listeners : {
                afterrender : function (me) {
                    debug('_c3_instance afterrender! args:', arguments);
                    var ck = 'Recuperando acciones posteriores';
                    try {
                        _cargarBotonesEntidad(
                            me.config.cdtipflu
                            ,me.config.cdflujomc
                            ,me.config.tipoent
                            ,me.config.claveent
                            ,me.config.webid
                            ,function (botones) {
                                if (botones.length === 0) {
                                    me.down('button[text=Aceptar]').enable();
                                } else {
                                    me.down('button[text=Aceptar]').hide();
                                    me.down('toolbar').add(botones);
                                }
                            }
                            ,me.config.ntramite
                            ,me.config.status
                            ,me.config.cdunieco
                            ,me.config.cdramo
                            ,me.config.estado
                            ,me.config.nmpoliza
                            ,me.config.nmsituac
                            ,me.config.nmsuplem
                            ,null//callbackDespuesProceso
                        );
                    } catch (e) {
                        manejaException(e, ck);
                    }
                }
            }
        });
        this.callParent(arguments);
    }
});