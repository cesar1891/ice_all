Ext.define('MenuPrincipal',
{
    extend       : 'Ext.NestedList'
    ,requires    : ['MenuPrincipalModelo']
    ,config      :
    {
        backText      : _text_regresar
        ,fullscreen   : true
        ,title        : ''
        ,displayField : 'text'
        ,store        : null
        ,items        :
        [
            {
                xtype   : 'toolbar'
                ,docked : 'top'
                ,items  :
                [
                    { xtype : 'spacer' }
                    ,{
                        text    : ''
                        ,itemId : 'logoutButton'
                        ,ui     : 'decline'
                    }
                ]
            }
        ]
    }
    ,textos      : {}
    ,enItemTap   : function(me,list,index,target,record)
    {
        debug('>MenuPrincipal enItemTap');
        debug('record.data:',record.data);
        var liga = record.get('href');
        debug('pre length:',("javascript:LoadPage('").length);
        debug('post length:',liga.length-("')").length);
        liga = liga.substring(
                ("javascript:LoadPage('").length,
                liga.length-("')").length
                );
        debug('liga:',liga);
        
        var prefijoLigasCotizacion = "/emision/cotizacion";
        debug('prefijoLigasCotizacion:',prefijoLigasCotizacion);
        var lengthPrefijoLigasCotizacion = prefijoLigasCotizacion.length;
        debug('lengthPrefijoLigasCotizacion:',lengthPrefijoLigasCotizacion);
        
        var valida = true;
        /*
        if(valida)
        {
            valida = liga.length>=lengthPrefijoLigasCotizacion;
            if(!valida)
            {
                debug('liga demasiado corta para ser decotizacion');
                mensajeError(this.textos.ligaNoMovil,function(){});
            }
        }
        if(valida)
        {
            var subliga = liga.substr(0,lengthPrefijoLigasCotizacion);
            debug('subliga:',subliga);
            valida = subliga==prefijoLigasCotizacion;
            if(!valida)
            {
                debug('no coincide el prefijo');
                mensajeError(this.textos.ligaNoMovil,function(){});
            }
        }
        */
        if(valida)
        {
        	maskui();
            window.location.replace(_contexto+liga);
        }
        debug('<MenuPrincipal enItemTap');
    }
    ,constructor : function(config)
    {
        debug('>MenuPrincipal constructor');
        debug('config:',config);
        this.callParent(arguments);
        var me=this;
        
        function ponLeaf(menu)
        {
            debug('recursive leaf search on:',menu.text);
            if(menu.menu==null)
            {
                menu.leaf=true;
                debug('is leaf');
            }
            else
            {
                for(var i=0;i<menu.menu.length;i++)
                {
                    ponLeaf(menu.menu[i]);
                }
            }
        }
        
        maskui();
        Ext.Ajax.request(
        {
            url      : config.urlCargar
            ,success : function(response)
            {
                unmaskui();
                var menu=Ext.JSON.decode(response.responseText);
                var menuTmp=
                {
                    menu : menu
                };
                ponLeaf(menuTmp);
                debug('json response:',menu);
                var store = Ext.create('Ext.data.TreeStore',
                {
                    model                : 'MenuPrincipalModelo'
                    ,defaultRootProperty : 'menu'
                    ,data                : menu
                });
                me.setStore(store);
                me.down('toolbar').setTitle(config.textos.titulo);
                me.down('#logoutButton').setText(config.textos.botonLogout);
                me.down('#logoutButton').setHandler(config.logout);
                me.addListener('leafitemtap',me.enItemTap);
                me.textos = config.textos;
            }
            ,failure : function()
            {
                unmaskui();
                errorComunicacion(function(){});
            }
        });
        debug('<MenuPrincipal constructor');
    }
});