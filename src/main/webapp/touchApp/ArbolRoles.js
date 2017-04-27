Ext.define('ArbolRoles',
{
    extend       : 'Ext.NestedList'
    ,requires    : ['ArbolRolesModelo']
    ,config      :
    {
        backText      : _text_regresar
        ,fullscreen   : true
        ,title        : ''
        ,displayField : 'text'
        ,store        : null
    }
    ,callback    : function(){debug('ArbolRoles empty callback');}
    ,urlCodigo   : ''
    ,textos      : {}
    ,enItemTap   : function(me,list,index,target,n)
    {
        //n es record (node)
        debug('>ArbolRoles enItemTap');
        debug('n(node):',n);
        maskui();
        Ext.Ajax.request(
        {
            url      : me.urlCodigo
            ,params  :
            {
                codigoRol      : n.get('codigoObjeto')
                ,codigoCliente : n.get('cdElemento')
            }
            ,success : function(response)
            {
                unmaskui();
                var jsonResp = Ext.JSON.decode(response.responseText);
                debug('jsonResp: ',jsonResp);
                if(jsonResp.codigoValido==true)
                {
                    me.callback();
                }
                else
                {
                    mensajeError(me.textos.codigoInvalido);
                }
            }
            ,failure : function()
            {
                unmaskui();
                errorComunicacion(function(){});
            }
        });
        debug('<ArbolRoles enItemTap');
    }
    ,constructor : function(config)
    {
        debug('>ArbolRoles constructor');
        debug('config:',config);
        this.callParent(arguments);
        var me       = this;
        me.callback  = config.callback;
        me.textos    = config.textos;
        me.urlCodigo = config.urlCodigo;
        me.addListener('leafitemtap',me.enItemTap);
        
        var store = Ext.create('Ext.data.TreeStore',
        {
            model                : 'ArbolRolesModelo'
            ,autoLoad            : true
            ,defaultRootProperty : 'children'
            ,proxy               :
            {
                type    : 'ajax'
                ,url    : config.urlCargar
                ,reader :
                {
                    type : 'json' 
                }
            }
            ,listeners           :
            {
                load : function (store,records)
                {
                    debug('>store load records',records);
                    debug('records length:',records.length);
                    debug('first child nodes:',records[0].childNodes);
                    if(records.length==1)
                    {
                        debug('solo una empresa');
                        var children = [];
                        for(var i=0;i<records[0].childNodes.length;i++)
                        {
                            children.push(records[0].childNodes[i].raw);
                        }
                        debug('children: ',children);
                        var storeAux = Ext.create('Ext.data.TreeStore',
                        {
                            model : 'ArbolRolesModelo'
                            ,data : children
                        });
                        me.setStore(storeAux);
                        me.setTitle(config.textos.titulo);
                    }
                    else
                    {
                        me.setStore(store);
                        me.setTitle(config.textos.titulo);
                    }
                    debug('<store load store:',store);
                }
            }
        });

        debug('<ArbolRoles constructor');
    }
});