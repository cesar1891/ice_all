Ext.define('VentanaRechazo',
{
    extend       : 'Ext.window.Window'
    ,title       : 'Rechazo'
    ,itemId      : '_c22_instance'
    ,closeAction : 'destroy'
    ,border      : 0
    //,modal       : true
    //,width       : 600
    //,height      : 460
    ,mostrar     : function()
    {
        var me = this;
        centrarVentanaInterna(me.show());
    }
    ,constructor : function(config)
    {
        var me = this;
        debug('config:',config);
        if(Ext.isEmpty(config))
        {
            throw '#C22 - No se recibieron datos';
        }
        /*if(Ext.isEmpty(config.aux))
        {
            throw '#C22 - No se recibi\u00f3 el par\u00e1metro';
        }*/
        Ext.apply(me,
        {
            items :
            [
                {
                    xtype     : 'form'
                    ,border   : 0
                    ,defaults : { style : 'margin:5px;' }
                    ,items    :
                    [
                        {
                            xtype       : 'textfield'
                            ,name       : 'CDTIPFLU'
                            ,value      : config.cdtipflu
                            ,allowBlank : false
                            ,readOnly   : true
                            ,hidden     : true
                        }
                        ,{
                            xtype       : 'textfield'
                            ,name       : 'CDFLUJOMC'
                            ,value      : config.cdflujomc
                            ,allowBlank : false
                            ,readOnly   : true
                            ,hidden     : true
                        }
                        ,{
                            xtype       : 'textfield'
                            ,name       : 'NTRAMITE'
                            ,value      : config.ntramite
                            ,allowBlank : false
                            ,readOnly   : true
                            ,hidden     : true
                        }
                        ,{
                            xtype       : 'textfield'
                            ,name       : 'STATUSOLD'
                            ,value      : config.status
                            ,allowBlank : false
                            ,readOnly   : true
                            ,hidden     : true
                        }
                        ,{
                            xtype       : 'textfield'
                            ,name       : 'STATUSNEW'
                            ,value      : '4'//config.aux
                            ,allowBlank : false
                            ,readOnly   : true
                            ,hidden     : true
                        }
                        ,{
                            xtype       : 'textfield'
                            ,name       : 'CDCLAUSU'
                            ,allowBlank : true
                            ,readOnly   : true
                            ,hidden     : true
                        }, {
                            xtype           : 'combobox',
                            fieldLabel      : 'Motivo de rechazo',
                            valueField      : 'key',
                            displayField    : 'value',
                            editable        : true,
                            forceSelection  : config.cdsisrol.indexOf('MED') == -1, // Es force cuando no es medico
                            typeAhead       : true,
                            anyMatch        : true,
                            matchFieldWidth : false,
                            name            : 'CDRAZRECHA',
                            allowBlank      : false,
                            queryMode       : 'local',
                            readOnly        : config.cdsisrol.indexOf('MED') != -1, // Es solo lectura solo para medicos
                            listConfig      : {
                                maxHeight:150,
                                minWidth:120
                            },
                            store           : Ext.create('Ext.data.Store', {
                                model    : 'Generic',
                                autoLoad : true,
                                cdsisrol : config.cdsisrol,
                                proxy    : {
                                    type        : 'ajax',
                                    url         : _GLOBAL_CONTEXTO + '/catalogos/obtieneCatalogo.action',
                                    extraParams : {
                                        catalogo          : 'MOTIVOS_RECHAZO_TRAMITE',
                                        'params.ntramite' : config.ntramite
                                    },
                                    reader : {
                                        type         : 'json',
                                        root         : 'lista',
                                        rootProperty : 'lista'
                                    }
                                },
                                listeners : {
                                    load : function (me, records) {
                                        if (me.cdsisrol.indexOf('MED') == -1) { // Cuando no es medico quitamos rechazo medico
                                            var recordRechazoMedico;
                                            for (var i = 0; i < records.length; i++) {
                                                if (records[i].get('key') == 21) {
                                                    recordRechazoMedico = records[i];
                                                }
                                            }
                                            if (!Ext.isEmpty(recordRechazoMedico)) {
                                                me.remove(recordRechazoMedico);
                                            }
                                        } else { // Cuando es medico
                                            _fieldById('_c22_instance').down('[name=CDRAZRECHA]').seleccionaMedico();
                                        }
                                    }
                                }
                            }),
                            seleccionaMedico : function () { // Selecciona rechazo medico y dispara select
                                debug('>CDRAZRECHA.seleccionaMedico args:', arguments);
                                var combo  = this;
                                var store  = combo.getStore();
                                var recMed = store.getAt(store.find('key','21'));
                                combo.setValue(recMed);
                                combo.fireEvent('select', combo, [recMed]);
                            },
                            /*onSelect : function (me, records) { // Hereda texto al textfield
                                debug('>CDRAZRECHA.onSelect args:', arguments);
                                me.up('form').down('[name=COMMENTSEXT]').setValue(records[0].get('aux'));
                            },*/
                            listeners : {
                                select : function (me, records) {
                                    debug('>CDRAZRECHA.select!');
                                    me.up('form').down('[name=COMMENTSEXT]').setValue(records[0].get('aux'));
                                    try {
                                        me.up('window').down('[name=NTRASUST]').mostrar(records[0].get('value'));
                                    } catch (e) {
                                        debugError('error al invocar funcionamiento de ntrasust', e);
                                    }
                                }
                            }
                        }, {
                            xtype      : 'numberfield',
                            fieldLabel : 'Tr\u00e1mite nuevo',
                            name       : 'NTRASUST',
                            hidden     : true,
                            mostrar    : function (dsmotivo) {
                                debug('NTRASUST.mostrar args:', arguments);
                                var me = this;
                                try {
                                    if (Ext.isEmpty(dsmotivo)
                                        || dsmotivo.toUpperCase().indexOf('POR') == -1
                                        || dsmotivo.toUpperCase().indexOf('SUSTITUCI') == -1) {
                                        debug('NTRASUST hide');
                                        me.allowBlank = true;
                                        me.setValue('');
                                        me.hide();
                                        try {
                                            var correosCmp = me.up('window').down('[name=CORREOS]');
                                            correosCmp.setValue(correosCmp.correoAgente || '');
                                        } catch (e) {}
                                    } else {
                                        debug('NTRASUST show');
                                        me.allowBlank = false;
                                        me.setValue('');
                                        me.show();
                                        try {
                                            me.up('window').down('[name=CORREOS]').setValue('');
                                        } catch (e) {}
                                    }
                                } catch (e) {
                                    debugError('error en el funcionamiento de NTRASUST', e);
                                }
                            }
                        }, {
                            xtype       : 'textarea'
                            ,fieldLabel : 'Comentarios de rechazo'
                            ,labelAlign : 'top'
                            ,name       : 'COMMENTSEXT'
                            ,width      : 570
                            ,height     : 200
                            /*,listeners  :
                            {
                                afterrender : function(me)
                                {
                                    _setLoading(true,me);
                                    var ck = 'Recuperando texto rechazo';
                                    try
                                    {
                                        Ext.Ajax.request(
                                        {
                                            url      : _GLOBAL_COMP_URL_CONS_CLAU
                                            ,params  :
                                            {
                                                'params.cdclausu'     : ''
                                                ,'params.dsclausu'    : config.cdsisrol.indexOf('MED') != -1
                                                    ? 'CARTA RECHAZO MEDICO'
                                                    : 'CARTA RECHAZO ADMINISTRATIVA'
                                            }
                                            ,success : function(response)
                                            {
                                                _setLoading(false,me);
                                                var ck = 'Decodificando respuesta al recuperar detalle de cl\u00e1usula';
                                                try
                                                {
                                                    var json = Ext.decode(response.responseText);
                                                    debug('### clausula:',json);
                                                    var key = json.listaGenerica[0].key;
                                                    
                                                    me.up('window').down('[name=CDCLAUSU]').setValue(key);
                                                    
                                                    ck = 'Recuperando texto rechazo a detalle';
                                                    _setLoading(true,me);
                                                    Ext.Ajax.request(
                                                    {
                                                        url      : _GLOBAL_COMP_URL_CONS_CLAU_DET
                                                        ,params  :
                                                        {
                                                            'params.cdclausu' : key
                                                        }
                                                        ,success : function(response)
                                                        {
                                                            _setLoading(false,me);
                                                            var ck = 'Decodificando respuesta al recuperar texto a detalle';
                                                            try
                                                            {
                                                                var json2 = Ext.decode(response.responseText);
                                                                debug('### detalle:',json2);
                                                                
                                                                ck = 'Recuperando tipo de ramo';
                                                                var mask = _maskLocal(ck);
                                                                Ext.Ajax.request({
                                                                    url    : _GLOBAL_URL_RECUPERACION,
                                                                    params : {
                                                                        'params.consulta' : 'RECUPERAR_SI_ES_CDRAMO_DE_SALUD',
                                                                        'params.cdramo'   : me.up('window').cdramo
                                                                    },
                                                                    success : function (response) {
                                                                        mask.close();
                                                                        var ck = 'Decodificando respuesta al recuperar si es de salud';
                                                                        try {
                                                                            var json3 = Ext.decode(response.responseText);
                                                                            debug('### es salud:', json3);
                                                                            if (json3.success === true) {
                                                                                var combo = me.up('form').down('[name=CDRAZRECHA]');
                                                                                if (json3.params.salud === 'S') { // Salud
                                                                                    // Para salud si es medico aplica carta medica, si es
                                                                                    // otro aplica carta administrativa, que ya se recupero
                                                                                    me.setValue(json2.msgResult);
                                                                                } else { // Danios
                                                                                    // Para auto se toma el texto del combo
                                                                                    combo.on({
                                                                                        select : combo.onSelect
                                                                                    });
                                                                                }
                                                                                if (config.cdsisrol.indexOf('MED') != -1) { // Si es medico
                                                                                    if (combo.getStore().getCount() > 0) {
                                                                                        combo.seleccionaMedico();
                                                                                    } else {
                                                                                        combo.getStore().padre = combo;
                                                                                        combo.getStore().on({
                                                                                            load : function (me) {
                                                                                                me.padre.seleccionaMedico();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                mensajeError(json3.message);
                                                                            }
                                                                        } catch (e) {
                                                                            manejaException(e, ck);
                                                                        }
                                                                    },
                                                                    failure : function () {
                                                                        mask.close();
                                                                        errorComunicacion(null, 'Error al recuperar si es de salud');
                                                                    }
                                                                });
                                                            }
                                                            catch(e)
                                                            {
                                                                manejaException(e,ck);
                                                            }
                                                        }
                                                        ,failure : function()
                                                        {
                                                            _setLoading(false,me);
                                                            errorComunicacion(null,'Error al recuperar detalle de cl\u00e1usula');
                                                        }
                                                    });
                                                }
                                                catch(e)
                                                {
                                                    manejaException(e,ck);
                                                }
                                            }
                                            ,failure : function()
                                            {
                                                _setLoading(false,me);
                                                errorComunicacion(null,'Error al recuperar cl\u00e1usula');
                                            }
                                        });
                                    }
                                    catch(e)
                                    {
                                        manejaException(e,ck);
                                    }
                                }
                            }*/
                        }
                        ,{
                            xtype       : 'textarea'
                            ,fieldLabel : 'Comentarios internos'
                            ,labelAlign : 'top'
                            ,name       : 'COMMENTSINT'
                            ,width      : 570
                            ,height     : 100
                        }, {
                            xtype       : 'radiogroup',
                            fieldLabel : 'Mostrar al agente',
                            columns    : 2,
                            width      : 250,
                            style      : 'margin:5px;',
                            hidden     : _GLOBAL_CDSISROL===RolSistema.Agente,
                            items      : [
                                {
                                    boxLabel    : 'Si',
                                    itemId     : 'SWAGENTE',
                                    name       : 'SWAGENTE',
                                    inputValue : 'S',
                                    checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                                }, {
                                    boxLabel    : 'No',
                                    name       : 'SWAGENTE',
                                    inputValue : 'N',
                                    checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                                }
                            ]
                        }, {
                            xtype      : 'textfield',
                            fieldLabel : 'Correo del agente',
                            readOnly   : true,
                            name       : 'CORREOS',
                            width      : 550,
                            labelWidth : 120,
                            listeners  : {
                                afterrender : function (me) {
                                    _request({
                                        mask   : 'Recuperando correo del agente',
                                        url    : _GLOBAL_URL_RECUPERACION,
                                        params : {
                                            'params.consulta' : 'RECUPERAR_CORREO_AGENTE_TRAMITE',
                                            'params.ntramite' : config.ntramite
                                        },
                                        success : function(json) {
                                            if (!Ext.isEmpty(json.params.correoAgente)) {
                                                me.correoAgente = json.params.correoAgente;
                                                me.setValue(json.params.correoAgente);
                                            }
                                        }
                                    });
                                }
                            }
                        }, {
                            xtype      : 'textfield',
                            fieldLabel : 'Cc (separados por ;)',
                            name       : 'CORREOSCC',
                            width      : 550,
                            emptyText  : 'Correos separados por ;',
                            labelWidth : 120,
                            validator  : function (val) {
                                var r = 'Error';
                                try {
                                    if (Ext.isEmpty(val)) {
                                        return true;
                                    }
                                    var emails = [val];
                                    if (val.indexOf(';') !== -1) { // tiene mas de un correo
                                        emails = val.split(';');
                                    }
                                    var ereg = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
                                    for (var i = 0; i < emails.length; i++) {
                                        if (ereg.test(emails[i]) !== true) {
                                            return 'El correo ' + emails[i] + ' no es v\u00e1lido';
                                        }
                                    }
                                    return true;
                                } catch (e) {
                                    debugError('error al validar emails cc:', e);
                                }
                                return r;
                            }
                        }, {
                            xtype  : 'textfield',
                            name   : 'SOLO_CORREOS_RECIBIDOS',
                            value  : 'S',
                            hidden : true
                        }
                    ]
                }
            ]
            ,buttonAlign : 'center'
            ,buttons     :
            [{
                text     : 'Rechazar'
                ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'cancel.png'
                ,handler : function(me)
                {
                    var win = me.up('window');
                    var ck = 'Turnando tr\u00e1mite';
                    try
                    {
                        var form = win.down('form');
                        if(!form.isValid())
                        {
                            throw 'Favor de revisar los datos';
                        }
                        var values = form.getValues();
                        values.SWAGENTE = _fieldById('SWAGENTE',win).getGroupValue();
                        values.COMMENTS = values.COMMENTSINT;
                        values.cerrado  = 'S';
                        debug('values:',values);
                        
                        var correos = values.CORREOS;
                        if (Ext.isEmpty(correos)) {
                            if (!Ext.isEmpty(values.CORREOSCC)) {
                                correos = values.CORREOSCC;
                            }
                        } else {
                            if (!Ext.isEmpty(values.CORREOSCC)) {
                                correos = correos + ';' + values.CORREOSCC;
                            }
                        }
                        values.CORREOS = correos;
                        
                        debug('values (despues de correos):',values);
                        
                        _mask(ck);
                        Ext.Ajax.request(
                        {
                            url     : _GLOBAL_COMP_URL_TURNAR
                            ,params : _formValuesToParams(values)
                            ,success : function(response)
                            {
                                _unmask();
                                var ck = '';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### turnar:',json);
                                    if(json.success)
                                    {
                                        var ck = 'Guardando carta rechazo';
                                        try
                                        {
                                            _mask(ck);
                                            Ext.Ajax.request(
                                            {
                                                url      : _GLOBAL_COMP_URL_GUARDA_CARTA_RECHAZO
                                                ,params  :
                                                {
                                                    'map1.ntramite'    : config.ntramite
                                                    ,'map1.comments'   : me.up('window').down('[name=COMMENTSEXT]').getValue()
                                                    ,'map1.cdsisrol'   : config.cdsisrol
                                                    ,'map1.cdunieco'   : config.cdunieco
                                                    ,'map1.cdramo'     : config.cdramo
                                                    ,'map1.estado'     : config.estado
                                                    ,'map1.nmpoliza'   : 'R'
                                                    ,'map1.cdrazrecha' : json.params.CDRAZRECHA
                                                }
                                                ,success : function()
                                                {
                                                    _unmask();
                                                    _mask('Redireccionando...');
                                                    Ext.create('Ext.form.Panel').submit(
                                                    {
                                                        url             : _GLOBAL_COMP_URL_MCFLUJO
                                                        ,standardSubmit : true
                                                    });
                                                }
                                                ,failure : function()
                                                {
                                                    _unmask();
                                                    errorComunicacion(null,'Error al guardar carta rechazo');
                                                }
                                            });
                                        }
                                        catch(e)
                                        {
                                            manejaException(e,ck);
                                        }
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
                                _unmask();
                                errorComunicacion(null,'Error al turnar tr\u00e1mite');
                            }
                        });
                    }
                    catch(e)
                    {
                        _unmask();
                        manejaException(e,ck);
                    }
                }
            }]
        });
        this.callParent(arguments);
    }
});