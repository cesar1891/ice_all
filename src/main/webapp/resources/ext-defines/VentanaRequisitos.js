Ext.define('VentanaRequisitos',
{
    extend         : 'Ext.window.Window'
    ,itemId        : '_c5_instance'
    ,closeAction   : 'destroy'
    //,autoScroll    : true
    ,collapsible   : true
    ,titleCollapse : true
    ,cls           : 'VENTANA_DOCUMENTOS_CLASS'
    ,mostrar       : function()
    {
        var me = this;
        return centrarVentanaInterna(me.show());
    }
    ,constructor : function(config)
    {
        debug('config:',config);
        var me = this;
        
        Ext.apply(me,
        {
            title : 'Requisitos del tr\u00e1mite' + (Ext.isEmpty(config.ntramite)
                                                        ? ''
                                                        : ' '+config.ntramite),
            items : [
                {
                    xtype       : 'grid',
                    width       : 650,
                    height      : 300,
                    autoScroll  : true,
                    columnLines : true,
                    selType    : 'cellmodel',
                    plugins    : [
                        Ext.create('Ext.grid.plugin.CellEditing', {
                            clicksToEdit : 1,
                            listeners    : {
                                validateedit : function () { return false; }
                            }
                        })
                    ],
                    columns     : [
                        {
                            text      : 'Requisito',
                            dataIndex : 'DSREQUISI',
                            flex      : 1
                        }, {
                            text      : 'Valor',
                            dataIndex : 'DSDATO',
                            flex      : 1,
                            editor    : {
                                xtype    : 'textfield',
                                readOnly : true
                            }
                        }
                    ],
                    store : Ext.create('Ext.data.Store', {
                        autoLoad : true,
                        fields   : [
                            'CDREQUISI' , 'DSREQUISI' , 'CDUSUARI' , 'DSUSUARI',
                            'CDSISROL'  , 'DSSISROL'  , 'FEFECHA'  , 'DSDATO', 'SWACTIVO'
                        ],
                        proxy    : {
                            url    : _GLOBAL_URL_RECUPERACION,
                            type   : 'ajax',
                            reader : {
                                type : 'json',
                                root : 'list'
                            },
                            extraParams : {
                                'params.consulta' : 'RECUPERAR_REQUISITOS_DATOS_TRAMITE',
                                'params.ntramite' : config.ntramite
                            }
                        }
                    })
                }
            ]
        });
        this.callParent(arguments);
    }
});