Ext.define('VentanaAutorizacionEndoso',
{
    extend       : 'Ext.window.Window'
    ,title       : 'Autorizaci\u00f3n'
    ,itemId      : '_c81_instance'
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
        var me = this;
        if(Ext.isEmpty(config))
        {
            throw '#C81 - No se recibieron datos';
        }
        
        me.config = config;
        debug('me.config',config,'.');
        
        debug('c81 config:',config,'.');
        
        Ext.apply(me,
        {
            items :
            [
                Ext.create('Ext.form.Panel',
                {
                    border    : 0
                    ,defaults : { style : 'margin:5px;' }
                    ,items    :
                    [
                        {
                            xtype       : 'textarea'
                            ,fieldLabel : 'Comentarios'
                            ,labelAlign : 'top'
                            ,name       : 'COMMENTS'
                            ,width      : 470
                            ,height     : 220
                        }
                    ]
                })
            ]
            ,buttonAlign : 'center'
            ,buttons     :
            [{
                text     : 'Autorizar'
                ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'key.png'
                ,handler : function(me)
                {
                    var win = me.up('window');
                    var mask, ck = 'Autorizando tr\u00e1mite';
                    try
                    {
                        var form = win.down('form');
                        if(!form.isValid())
                        {
                            throw 'Favor de revisar los datos';
                        }
                        
                        var COMMENTS = form.getValues().COMMENTS;
                        
                        debug('COMMENTS:',COMMENTS,'.');
                        
                        var params = _flujoToParams(me.up('window').config);
                        
                        params['flujo.aux'] = COMMENTS;
                        
                        debug('params:',params,'.');
                        
                        mask = _maskLocal(ck);
                        Ext.Ajax.request(
                        {
                            url      : _GLOBAL_COMP_URL_AUTORIZAR_ENDOSO
                            ,params  : params
                            ,success : function(response)
                            {
                                mask.close();
                                var ck = '';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### turnar:',json);
                                    if(json.success === true )
                                    {
                                        mensajeCorrecto
                                        (
                                            'Tr\u00e1mite autorizado'
                                            ,'Tr\u00e1mite autorizado'//json.message
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
                                errorComunicacion(null,'Error al autorizar tr\u00e1mite');
                            }
                        });
                    }
                    catch(e)
                    {
                        manejaException(e,ck,mask);
                    }
                }
            }]
        });
        this.callParent(arguments);
    }
});