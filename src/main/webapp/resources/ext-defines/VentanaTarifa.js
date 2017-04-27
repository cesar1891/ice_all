Ext.define('VentanaTarifa',
{
    extend  : 'Ext.window.Window',
    mostrar : function () {
        var me = this;
        return centrarVentanaInterna(me.show());
    },
    constructor : function (config) {
        debug('VentanaTarifa constructor args:', arguments);
        var me = this;
        if (Ext.isEmpty(config)) {
            throw 'VentanaTarifa - Falta config';
        }
        if (Ext.isEmpty(config.datos)) {
            throw 'VentanaTarifa - Faltan datos';
        }
        var suma = 0;
        for (var i = 0; i < config.datos.length; i++) {
            suma += parseFloat(config.datos[i].PRIMA);
        }
        Ext.apply(me, {
            title       : 'TARIFA',
            modal       : true,
            closeAction : 'hide',
            border      : 0,
            items       : [
                Ext.create('Ext.grid.Panel', {
                    width   : 600,
                    height  : 400,
                    border  : 0,
                    columns : [
                        {
                            text            : 'COBERTURA',
                            dataIndex       : 'COBERTURA',
                            flex            : 1,
                            summaryType     : 'count',
                            summaryRenderer : function (v) {
                                return 'TOTAL DE ' + v + ' COBERTURA' + ( v > 1 ? 'S' : '');
                            }
                        }, {
                            text        : 'PRIMA',
                            dataIndex   : 'PRIMA',
                            renderer    : Ext.util.Format.usMoney,
                            width       : 150,
                            summaryType : 'sum'
                        }
                    ],
                    store : Ext.create('Ext.data.Store', {
                        fields : [
                            'AGRUPADOR', 'COBERTURA', {
                                name : 'PRIMA',
                                type : 'float'
                            }
                        ],
                        groupField : 'AGRUPADOR',
                        data       : config.datos
                    }),
                    features : [
                        {
                            groupHeaderTpl : '{name}',
                            ftype          : 'groupingsummary',
                            startCollapsed : true
                        }
                    ],
                    bbar : [
                        '->',
                        {
                            xtype : 'label',
                            style : 'color : white;',
                            text  : 'TOTAL : ' + Ext.util.Format.usMoney(suma)
                        }
                    ]
                })
            ],
            buttonAlign : 'center'
        });
        this.callParent(arguments);
    }
});