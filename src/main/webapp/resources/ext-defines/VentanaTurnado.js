Ext.define('VentanaTurnado',
{
    extend       : 'Ext.window.Window'
    ,title       : 'Turnado'
    ,itemId      : '_c22_instance'
    ,closeAction : 'destroy'
    ,border      : 0
    ,modal       : true
    ,width       : 500
    ,height      : 350
    ,mostrar     : function()
    {
        var me = this;
        centrarVentanaInterna(me.show());
    }
    ,constructor : function(config)
    {
        debug('#C22 config:',config,'.');
        var me = this;
        if(Ext.isEmpty(config))
        {
            throw '#C22 - No se recibieron datos';
        }
        if(Ext.isEmpty(config.aux))
        {
            throw '#C22 - No se recibi\u00f3 el par\u00e1metro';
        }
        Ext.apply(me,
        {
            config : config
            ,items :
            [
                Ext.create('Ext.form.Panel',
                {
                    border    : 0
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
                            ,value      : config.aux
                            ,allowBlank : false
                            ,readOnly   : true
                            ,hidden     : true
                        }
                        ,{
                            xtype       : 'textarea'
                            ,fieldLabel : 'Comentarios'
                            ,labelAlign : 'top'
                            ,name       : 'COMMENTS'
                            ,width      : 470
                            ,height     : 220
                        }
                    ]
                })
                ,{
                    xtype       : 'radiogroup'
                    ,fieldLabel : 'Mostrar al agente'
                    ,columns    : 2
                    ,width      : 250
                    ,style      : 'margin:5px;'
                    ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                    ,items      :
                    [
                        {
                            boxLabel    : 'Si'
                            ,itemId     : 'SWAGENTE'
                            ,name       : 'SWAGENTE'
                            ,inputValue : 'S'
                            ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                        }
                        ,{
                            boxLabel    : 'No'
                            ,name       : 'SWAGENTE'
                            ,inputValue : 'N'
                            ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                        }
                    ]
                }
            ]
            ,buttonAlign : 'center'
            ,buttons     :
            [{
                text     : 'Turnar'
                ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'user_go.png'
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
                        debug('values:',values);
                        
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
                                        mensajeCorrecto
                                        (
                                            'Tr\u00e1mite turnado'
                                            ,json.message
                                            ,function()
                                            {
                                                var mask, ck = 'Recuperando acciones posteriores al turnado';
                                                try
                                                {
                                                    _cargarAccionesEntidad(
                                                        win.config.cdtipflu
                                                        ,win.config.cdflujomc
                                                        ,win.config.tipoent
                                                        ,win.config.claveent
                                                        ,win.config.webid
                                                        ,function(acciones)
                                                        {
                                                            debug('callback despues de recuperar acciones posteriores a turnado args:',arguments,'.');
                                                            if(acciones.length === 0)
                                                            {
                                                                _mask('Redireccionando');
                                                                Ext.create('Ext.form.Panel').submit(
                                                                {
                                                                    url             : _GLOBAL_COMP_URL_MCFLUJO
                                                                    ,standardSubmit : true
                                                                });
                                                            }
                                                            else if(acciones.length === 1)
                                                            {
                                                                _procesaAccion(
                                                                    acciones[0].CDTIPFLU
                                                                    ,acciones[0].CDFLUJOMC
                                                                    ,acciones[0].TIPODEST
                                                                    ,acciones[0].CLAVEDEST
                                                                    ,acciones[0].WEBIDDEST
                                                                    ,acciones[0].AUX
                                                                    ,win.config.ntramite
                                                                    ,String(win.config.aux).split('|')[0] //por si viene: 13|MATKAA|SUSCRIAUTO
                                                                    ,win.config.cdunieco
                                                                    ,win.config.cdramo
                                                                    ,win.config.estado
                                                                    ,win.config.nmpoliza
                                                                    ,win.config.nmsituac
                                                                    ,win.config.nmsuplem
                                                                    ,win.config.cdusuari
                                                                    ,win.config.cdsisrol
                                                                    ,null
                                                                );
                                                            }
                                                            else
                                                            {
                                                                mensajeWarning(
                                                                    'Hay m\u00e1s de una acci\u00f3n posterior al turnado, revisar el flujo'
                                                                    ,function()
                                                                    {
                                                                        _mask('Redireccionando');
                                                                        Ext.create('Ext.form.Panel').submit(
                                                                        {
                                                                            url             : _GLOBAL_COMP_URL_MCFLUJO
                                                                            ,standardSubmit : true
                                                                        });
                                                                    }
                                                                );
                                                            }
                                                        }
                                                    );
                                                }
                                                catch(e)
                                                {
                                                    manejaException(e,ck,mask);
                                                }
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