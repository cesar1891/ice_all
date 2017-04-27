Ext.define('VentanaHistorial',
{
    extend       : 'Ext.window.Window'
    ,title       : 'Historial'
    ,itemId      : '_c21_instance'
    ,closeAction : 'destroy'
    ,modal       : true
    ,width       : 900
    ,height      : 500
    ,autoScroll  : true
    ,maximizable : true
    ,defaults    : {
        style: 'margin: 5px;'
    }
    ,mostrar     : function()
    {
        var me = this;
        centrarVentanaInterna(me.show());
    }
    ,constructor : function(config)
    {
        var me = this;
        Ext.apply(me,
        {
            items :
            [
                Ext.create('Ext.grid.Panel',
                {
                    height      : 200
                    ,width      : 850
                    ,autoScroll : true
                    ,store      : new Ext.data.Store(
                    {
                        fields    :
                        [
                            "NTRAMITE"
                            ,"NMORDINA"
                            ,"CDTIPTRA"
                            ,"CDCLAUSU"
                            ,{name:"FECHAINI",type:'date',dateFormat:'d/m/Y H:i'}
                            ,{name:"FECHAFIN",type:'date',dateFormat:'d/m/Y H:i'}
                            ,"COMMENTS"
                            ,"CDUSUARI_INI"
                            ,"CDUSUARI_FIN"
                            ,"usuario_ini"
                            ,"usuario_fin"
                            ,"STATUS"       , "DSSTATUS"
                            ,"CDSISROL_INI" , "DSSISROL_INI"
                            ,"CDSISROL_FIN" , "DSSISROL_FIN"
                            ,"DSUSUARI_INI" , "DSUSUARI_FIN"
                            ,'STATUS'       , 'DSSTATUS'
                        ]
                        ,autoLoad : true
                        ,proxy    :
                        {
                            type         : 'ajax'
                            ,url         : _GLOBAL_COMP_URL_GET_HISTORIAL
                            ,extraParams :
                            {
                                'smap1.pv_ntramite_i' : config.ntramite
                            }
                            ,reader      :
                            {
                                type  : 'json'
                                ,root : 'slist1'
                            }
                        }
                    })
                    ,columns : 
                    [
                        {
                            header     : 'Tr&aacute;mite'
                            ,dataIndex : 'NTRAMITE'
                            ,width     : 60
                            ,hidden    : true
                        }
                        ,{
                            header     : 'No.'
                            ,dataIndex : 'NMORDINA'
                            ,width     : 40
                        }
                        ,{
                            header     : 'Usuario'
                            ,dataIndex : 'usuario_fin'
                            ,width     : 300
                            ,renderer  : function(v,md,rec)
                            {
                                return _NVL(rec.get('DSUSUARI_FIN')) + ' - ' +  _NVL(rec.get('DSSISROL_FIN'));
                            }
                        }
                        ,{
                            header     : 'Usuario'
                            ,dataIndex : 'usuario_ini'
                            ,width     : 300
                            ,renderer  : function(v,md,rec)
                            {
                                return _NVL(rec.get('DSUSUARI_INI')) + ' - ' +  _NVL(rec.get('DSSISROL_INI'));
                            }
                            ,hidden    : true
                        }
                        ,{
                            header     : 'Inicio'
                            ,xtype     : 'datecolumn'
                            ,dataIndex : 'FECHAINI'
                            ,format    : 'd M Y H:i'
                            ,width     : 125
                        }
                        ,{
                            header     : 'Fin'
                            ,xtype     : 'datecolumn'
                            ,dataIndex : 'FECHAFIN'
                            ,format    : 'd M Y H:i'
                            ,width     : 125
                        }
                        ,{
                            header     : 'Estatus'
                            ,dataIndex : 'DSSTATUS'
                            ,width     : 160
                            ,renderer  : function(v)
                            {
                                return _NVL(v);
                            }
                        }
                        ,{
                            dataIndex : 'usuario_ini'
                            ,width    : 30
                            ,renderer : function(val,md,rec)
                            {
                                var r = '';
                                if(config.cdsisrol!='EJECUTIVOCUENTA'||rec.raw.SWAGENTE=='S')
                                {
                                    r = '<img src="'+_GLOBAL_DIRECTORIO_ICONOS+'book_edit.png" style="cursor:pointer;" data-qtip="Modificar detalle" />';
                                }
                                return r;
                            }
                        }
                    ]
                    ,listeners :
                    {
                        cellclick : function(grid, td,
                                cellIndex, record, tr,
                                rowIndex, e, eOpts)
                        {
                            debug(record);
                            if($(td).find('img').length==0)
                            {
                                _fieldById('_c21_inputReadDetalleHtmlVisor').setValue((config.cdsisrol!='EJECUTIVOCUENTA'||record.raw.SWAGENTE=='S')?record.get('COMMENTS'):'');
                            }
                            else if($(td).find('img').length>0)
                            {
                                debug('finalizar');
                                centrarVentanaInterna(Ext.create('Ext.window.Window',
                                {
                                    title        : 'Modificar detalle'
                                    ,width       : 600
                                    ,height      : 400
                                    ,buttonAlign : 'center'
                                    ,modal       : true
                                    ,closable    : false
                                    ,autoScroll  : true
                                    ,items       :
                                    [
                                        {
                                            xtype   : 'textarea'
                                            ,width  : 570
                                            ,height : 300
                                            //,value  : record.get('COMMENTS')
                                        }
                                    ]
                                    ,buttons     :
                                    [
                                        {
                                            text     : 'Guardar'
                                            ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'disk.png'
                                            ,handler : function(me)
                                            {
                                                var mask, ck = 'Modificando detalle';
                                                try
                                                {
                                                    mask = _maskLocal(ck);
                                                    Ext.Ajax.request(
                                                    {
                                                        url      : _GLOBAL_COMP_URL_MODIF_HIST
                                                        ,params  :
                                                        {
                                                            'params.ntramite'  : record.get('NTRAMITE')
                                                            ,'params.nmordina' : record.get('NMORDINA')
                                                            ,'params.comments' : me.up('window').down('textarea').getValue()
                                                        }
                                                        ,success : function (response)
                                                        {
                                                            mask.close();
                                                            var ck = 'Decodificando respuesta al modificar detalle';
                                                            try
                                                            {
                                                                var json=Ext.decode(response.responseText);
                                                                if(json.success === true)
                                                                {
                                                                    mensajeCorrecto(
                                                                        'Detalle actualizado'
                                                                        ,'Detalle actualizado'
                                                                        ,function()
                                                                        {
                                                                            record.set('COMMENTS',json.params.comments);
                                                                            _fieldById('_c21_inputReadDetalleHtmlVisor').setValue(record.get('COMMENTS'));
                                                                            me.up('window').destroy();
                                                                        }
                                                                    );
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
                                                            mask.close();
                                                            errorComunicacion(null,'Error al modificar detalle');
                                                        }
                                                    });
                                                }
                                                catch(e)
                                                {
                                                    manejaException(e,ck,mask);
                                                }
                                            }
                                        }
                                        ,{
                                            text     : 'Cancelar'
                                            ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'cancel.png'
                                            ,handler : function()
                                            {
                                                this.up('window').destroy();
                                            }
                                        }
                                    ]
                                }).show());
                            }
                        }
                    }
                })
                ,{
                    xtype       : 'textarea'
                    ,itemId     : '_c21_inputReadDetalleHtmlVisor'
                    ,width      : 850
                    ,height     : 180
                    ,readOnly   : true
                    ,labelAlign : 'top'
                    ,fieldLabel : 'Se recibe con los siguientes comentarios:'
                }
            ],
            listeners: {
                afterrender: function (me) {
                    me.agregarGraficoHistorial(me);
                }
            }
        });
        this.callParent(arguments);
    },
    agregarGraficoHistorial: function (me) {
        var ck = 'Cargando datos de historial';
        try {
            _request({
                mask: ck,
                url: _GLOBAL_URL_RECUPERACION,
                params: {
                    'params.consulta': 'RECUPERAR_HISTORIAL_TRAMITE',
                    'params.ntramite': me.ntramite
                },
                success: function (json) {
                    var ck = 'Procesando datos de historial';
                    try {
                        if (json.list.length === 0) {
                            me.agregarGraficoEventos(me);
                            return;
                        }
                        var list = json.list,
                            raw = {},
                            fields = [
                                {
                                    name: 'NTRAMITE',
                                    type: 'int'
                                }, {
                                    name: 'HOYTIME',
                                    type: 'int'
                                }
                            ],
                            fieldsNamesAxis = ['HOYTIME'],
                            fieldsNames = [],
                            hoy = new Date(),
                            j = 1,
                            primerFecha = Ext.Date.parse(list[list.length - 1].FEFECHA, 'd/m/Y H:i'),
                            ultimaFecha = Ext.Date.parse(_NVL(list[0].FEFECHA_FIN, Ext.Date.format(hoy, 'd/m/Y H:i')), 'd/m/Y H:i'),
                            title = [],
                            storeHistorial = Ext.create('Ext.data.Store', {
                                fields: [
                                    'CDUSUARI',
                                    {
                                        name: 'FEINI',
                                        type: 'date',
                                        dateFormat: 'd/m/Y H:i'
                                    }, {
                                        name: 'FEFIN',
                                        type: 'date',
                                        dateFormat: 'd/m/Y H:i'
                                    },
                                    'TIEMPO'
                                ]
                            });
                        raw.NTRAMITE = json.params.ntramite;
                        raw.HOYTIME = Ext.Date.getElapsed(primerFecha, hoy);
                        for (var i = list.length - 1; i >= 0; i--) {
                            var el = list[i];
                            title.push(j + ') ' + el.CDUSUARI.toUpperCase() + ' - ' + el.CDSISROL.toUpperCase());
                            fields.push({
                                name: j + '_' + el.CDUSUARI + '_' + el.CDSISROL,
                                type: 'int'
                            });
                            fieldsNames.push(j + '_' + el.CDUSUARI + '_' + el.CDSISROL);
                            fieldsNamesAxis.push(j + '_' + el.CDUSUARI + '_' + el.CDSISROL);
                            raw[j + '_' + el.CDUSUARI + '_' + el.CDSISROL] = Ext.Date.getElapsed(
                                Ext.Date.parse(el.FEFECHA, 'd/m/Y H:i').getTime(),
                                Ext.Date.parse(_NVL(el.FEFECHA_FIN, Ext.Date.format(hoy, 'd/m/Y H:i')), 'd/m/Y H:i').getTime()
                            );
                            var feIniMil = Ext.Date.parse(el.FEFECHA, 'd/m/Y H:i').getTime(),
                                feFinMil = Ext.Date.parse(_NVL(el.FEFECHA_FIN, Ext.Date.format(hoy, 'd/m/Y H:i')), 'd/m/Y H:i').getTime(),
                                tMil = feFinMil - feIniMil,
                                tSeg = Math.floor(tMil / 1000),
                                tMin = Math.floor(tSeg / 60),
                                tHor = Math.floor(tMin / 60),
                                tDia = Math.floor(tHor / 24),
                                cadena = tDia + ' d\u00eda(s), ' + (tHor % (24)) + ' hora(s), ' + (tMin % 60) + ' minuto(s)',
                                cadenaSplit = cadena.split(', '),
                                cadena2 = '';
                                
                            if (cadenaSplit[0].substr(0,1) != 0) {
                                cadena2 = cadena2 + cadenaSplit[0] + ', ';
                            }
                            if (cadenaSplit[1].substr(0,1) != 0) {
                                cadena2 = cadena2 + cadenaSplit[1] + ', ';
                            }
                            cadena2 = cadena2 + cadenaSplit[2];
                            
                            storeHistorial.add({
                                CDUSUARI: el.CDUSUARI.toUpperCase() + ' - ' + el.CDSISROL.toUpperCase(),
                                FEINI: el.FEFECHA,
                                FEFIN: _NVL(el.FEFECHA_FIN, Ext.Date.format(hoy, 'd/m/Y H:i')),
                                TIEMPO: cadena2
                            });
                            j++;
                        }
                        debug('raw:', raw, 'fields:', fields);
                        
                        var series = [
                            {
                                type: 'bar',
                                axis: 'bottom',
                                //gutter: 80,
                                xField: 'NTRAMITE',
                                yField: fieldsNames,
                                title: title,
                                stacked: true,
                                highlight: true,
                                tips: {
                                    trackMouse: true,
                                    width: 200,
                                    renderer: function(storeItem, item) {
                                        debug('historial char series tips renderer args:', arguments);
                                        var tSeg = Math.floor(item.value[1] / 1000),
                                            tMin = Math.floor(tSeg / 60),
                                            tHor = Math.floor(tMin / 60),
                                            tDia = Math.floor(tHor / 24);
                                        debug('tSeg:', tSeg, 'tMin:', tMin, 'tHor:', tHor);
                                        var cadena = tDia + ' d\u00eda(s), ' + (tHor % (24)) + ' hora(s), ' + (tMin % 60) + ' minuto(s)';
                                        debug('cadena:', cadena);
                                        var cadenaSplit = cadena.split(', ');
                                        var cadena2 = '';
                                        if (cadenaSplit[0].substr(0,1) != 0) {
                                            cadena2 = cadena2 + cadenaSplit[0] + ', ';
                                        }
                                        if (cadenaSplit[1].substr(0,1) != 0) {
                                            cadena2 = cadena2 + cadenaSplit[1] + ', ';
                                        }
                                        cadena2 = cadena2 + cadenaSplit[2];
                                        this.setTitle(cadena2);
                                    }
                                }
                            }
                        ];
                        
                        if (Ext.Date.format(ultimaFecha, 'd/m/Y') === Ext.Date.format(hoy, 'd/m/Y')) {
                            series.push({
                                type: 'scatter',
                                axis: 'bottom',
                                xField: 'HOYTIME',
                                yField: 'NTRAMITE',
                                title: 'Hoy ' + Ext.Date.format(hoy, 'd M H:i'),
                                markerConfig: {
                                    type: 'circle',
                                    size: 5
                                }
                            });
                        }
                        
                        ck = 'Creando gr\u00e1fico de historial';
                        me.add(Ext.create('Ext.chart.Chart',{
                            width: 850,
                            height: 250,
                            animate: true,
                            shadow: true,
                            store: Ext.create('Ext.data.Store', {
                                autoLoad: true,
                                fields: fields,
                                data: [raw]
                            }),
                            legend: {
                                position: 'right',
                                renderer: function (v) {
                                    return 'a';
                                }
                            },
                            axes: [
                                {
                                    type: 'Numeric',
                                    position: 'bottom',
                                    fields: fieldsNamesAxis,
                                    title: 'Turnados',
                                    grid: true,
                                    label: {
                                        renderer: function(v) {
                                            debug('historial chart bottomAxis renderer v:', v);
                                            if (v == '0') {
                                                return Ext.Date.format(primerFecha, 'd M H:i');
                                            } else {
                                                return Ext.Date.format(new Date(primerFecha.getTime() + v), 'd M H:i');
                                            }
                                        }
                                    }
                                }, {
                                    type: 'Numeric',
                                    position: 'left',
                                    fields: ['NTRAMITE'],
                                    title: 'Tr\u00e1mite'
                                }
                            ],
                            series: series
                        }),
                        Ext.create('Ext.grid.Panel', {
                            width: 850,
                            height: 140,
                            columns: [
                                {
                                    text: 'USUARIO',
                                    dataIndex: 'CDUSUARI',
                                    flex: 1
                                }, {
                                    xtype: 'datecolumn',
                                    text: 'INICIO',
                                    dataIndex: 'FEINI',
                                    format: 'd M H:i',
                                    flex: 1
                                }, {
                                    xtype: 'datecolumn',
                                    text: 'FIN',
                                    dataIndex: 'FEFIN',
                                    format: 'd M H:i',
                                    flex: 1
                                }, {
                                    text: 'TIEMPO',
                                    dataIndex: 'TIEMPO',
                                    flex: 1
                                }
                            ],
                            store: storeHistorial
                        }));
                        me.agregarGraficoEventos(me);
                    } catch (e) {
                        manejaException(e, ck);
                    }
                }
            });
        } catch (e) {
            manejaException(e, ck);
        }
    },
    agregarGraficoEventos: function (me) {
        debug('agregarGraficoEventos!');
        var ck = 'Cargando datos de detalle';
        try {
            _request({
                mask: ck,
                url: _GLOBAL_URL_RECUPERACION,
                params: {
                    'params.consulta': 'RECUPERAR_DETALLES_TRAMITE',
                    'params.ntramite': me.ntramite
                },
                success: function (json) {
                    var ck = 'Procesando datos de historial';
                    var list = json.list;
                    if (list.length === 0) {
                        return;
                    }
                    var altura = 100,
                        alturas = {},
                        userRoles = {};
                    for (var i = 0; i < list.length; i++) {
                        var el = list[i],
                            userRol = el.CDUSUARI_INI.toUpperCase() + ' - ' + el.CDSISROL_INI.toUpperCase();
                        if (Ext.isEmpty(userRoles[userRol])) {
                            altura = altura - 1;
                            userRoles[userRol] = altura;
                            el.ALTURA = altura;
                            alturas[altura] = userRol;
                        } else {
                            el.ALTURA = userRoles[userRol];
                        }
                    }
                    debug('list despues de altura:', list);
                    me.insert(0, Ext.create('Ext.chart.Chart',{
                        width: 850,
                        height: 250,
                        animate: true,
                        shadow: true,
                        store: Ext.create('Ext.data.Store', {
                            autoLoad: true,
                            fields: [
                                {
                                    name: 'FECHAINI',
                                    type: 'date',
                                    dateFormat: 'd/m/Y H:i'
                                }, {
                                    name: 'ALTURA',
                                    type: 'int'
                                }
                            ],
                            data: list,
                            listeners: {
                                load: function (me, records, success) {
                                    debug('chart detalles store load records:', records);
                                }
                            }
                        }),
                        axes: [
                            {
                                type: 'Numeric',
                                position: 'bottom',
                                fields: 'FECHAINI',
                                title: 'Eventos',
                                grid: true,
                                label: {
                                    renderer: function(v) {
                                        debug('detalles chart bottomAxis renderer v:', v);
                                        return Ext.Date.format(new Date(v), 'd M H:i');
                                    }
                                }
                            }, {
                                type: 'Numeric',
                                position: 'left',
                                fields: 'ALTURA',
                                title: 'Usuario',
                                majorTickSteps: 99 - altura,
                                minorTickSteps: 0,
                                maximum: 99,
                                minimum: altura - 1,
                                grid: true,
                                label: {
                                    renderer: function (v) {
                                        return alturas[v] || '';
                                    }
                                }
                            }
                        ],
                        series: [
                            {
                                type: 'line',
                                highlight: true,
                                axis: 'bottom',
                                xField: 'FECHAINI',
                                yField: 'ALTURA',
                                markerConfig: {
                                    type: 'cross',
                                    size: 4,
                                    radius: 4,
                                    'stroke-width': 0
                                },
                                tips: {
                                    trackMouse: true,
                                    width: 500,
                                    height: 250,
                                    items: [
                                        {
                                            xtype: 'form',
                                            itemId: 'ventanaHistorialChartDetallesTipsForm',
                                            border: 0,
                                            defaults: {
                                                style: 'margin: 5px;'
                                            },
                                            items: [
                                                {
                                                    xtype: 'displayfield',
                                                    fieldLabel: 'Fecha',
                                                    name: 'FECHAINI'
                                                }, {
                                                    xtype: 'displayfield',
                                                    fieldLabel: 'Usuario',
                                                    name: 'DSUSUARI_INI'
                                                }, {
                                                    xtype: 'displayfield',
                                                    fieldLabel: 'Rol',
                                                    name: 'DSSISROL_INI'
                                                }, {
                                                    xtype: 'displayfield',
                                                    fieldLabel: 'Comentarios',
                                                    name: 'COMMENTS'
                                                }
                                            ]
                                        }
                                    ],
                                    renderer: function (klass, item) {
                                        debug('ventanaHistorialChartDetallesTipsForm renderer args:', arguments);
                                        var recordRaw = item.storeItem.raw;
                                        if (me.cdsisrol === 'EJECUTIVOCUENTA' && recordRaw.SWAGENTE !== 'S') {
                                            recordRaw.COMMENTS = '';
                                        }
                                        _fieldById('ventanaHistorialChartDetallesTipsForm').getForm().loadRecord({
                                            getData : function () {
                                                return recordRaw;
                                            }
                                        });
                                        this.setTitle('Tr\u00e1mite ' + me.ntramite + ' movimiento ' + recordRaw.NMORDINA);
                                    }
                                }
                            }
                        ]
                    }));
                }
            });
        } catch (e) {
            manejaException(e, ck);
        }
    }
});