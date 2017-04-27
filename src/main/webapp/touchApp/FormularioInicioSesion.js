Ext.define('FormularioInicioSesion',
{
    extend  : 'Ext.form.FormPanel'
    ,config :
    {
        title      : ''
        ,url       : ''
        ,msgError  : ''
        ,items     :
        [
            {
                xtype   : 'toolbar'
                ,docked : 'top'
                ,title  : 'a'
            }
            ,{
                xtype  : 'fieldset'
                ,items :
                [
                    {
                        xtype       : 'textfield'
                        ,name       : 'user'
                        ,allowBlank : false
                        ,labelAlign : 'top'
                    }
                    ,{
                        xtype       : 'passwordfield'
                        ,name       : 'password' 
                        ,allowBlank : false
                        ,labelAlign : 'top'
                    }
                ]
            }
            ,{
                xtype   : 'container'
                ,layout :
                {
                    type   : 'vbox'
                    ,pack  : 'center'
                    ,align : 'center'
                }
                ,items  :
                [
                    {
                        xtype : 'button'
                        ,ui   : 'round'
                    }
                ]
            }
        ]
        ,listeners    :
        {
            painted : function()
            {
                debug('>LoginForm painted');
                var me = this;
                me.down('[name=user]').focus();
                debug('<LoginForm painted');
            }
        }
    }
    ,onButtonTap : function(button,event)
    {
        debug('>LoginForm onButtonTap');
        var form=button.up('formpanel');
        debug("form.get('msgError'):",form.get('msgError'));
        var validatorConfig = form.getValues();
        validatorConfig.textos={
            usuarioValida   : form.textos.usuarioValida
            ,passwordValida : form.textos.passwordValida
        };
        var record = Ext.create('FormularioInicioSesionModelo',validatorConfig);
        var message="";
        debug('record:',record);
        var errors=record.validate();
        if(errors.isValid())
        {
            debug('valid');
            form.submit(
            {
                waitMsg  : _text_cargando
                ,success : function()
                {
                    debug('>success');
                    form.callback();
                    debug('<success');
                }
                ,failure : function(form2,action)
                {
                    debug('>failure');
                    var msgServer = Ext.isEmpty(action.errorMessage) ? form.get('msgError') : action.errorMessage;
                    mensajeError(msgServer, function(){});
                    debug('<failure');
                }
            });
        }
        else
        {
            debug('errors.items:',errors.items);
            Ext.each(errors.items,function(rec,i)
            {
                message += rec.config.message+"<br>";
            });
            datosIncompletos(message, function(){});
        }
        debug('<LoginForm onButtonTap');
    }
    ,callback    : function(){debug('FormularioInicioSesion empty callback');}
    ,textos      : {}
    ,constructor : function(config)
    {
        debug('>LoginForm constructor');
        debug('config:',config);
        this.callParent(arguments);
        var me=this;
        me.setTitle(config.textos.titulo);
        me.setUrl(config.url);
        me.setMsgError(config.textos.msgError);
        me.down('toolbar').setTitle(config.textos.titulo);
        me.down('[name=user]').setLabel(config.textos.usuario);
        me.down('[name=password]').setLabel(config.textos.contrasena);
        me.down('fieldset').setTitle(config.textos.tituloFieldSet);
        me.down('fieldset').setInstructions(config.textos.instrucciones);
        me.down('button').setText(config.textos.boton);
        me.down('button').setHandler(me.onButtonTap);
        me.callback = config.callback;
        me.textos   = config.textos;
        debug('<LoginForm constructor');
    }
});