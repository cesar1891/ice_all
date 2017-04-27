Ext.define('FormularioInicioSesionModelo',
{
    extend  : 'Ext.data.Model'
    ,config :
    {
        fields       :
        [
            {name  : 'user'     , type : 'string'}
            ,{name : 'password' , type : 'password'}
        ]
        ,validations :
        [
            {type  : 'presence' , name : 'user'     , message : ''}
            ,{type : 'presence' , name : 'password' , message : ''}
        ]
    }
    ,constructor : function(config)
    {
        debug('>LoginFormModel constructor');
        debug('config:',config);
        this.callParent(arguments);
        var me = this;
        me.validations.items[0].message = config.textos.usuarioValida;
        me.validations.items[1].message = config.textos.passwordValida;
        debug('me.validations:',me.validations);
        debug('<LoginFormModel constructor');
    }
});