Ext.define('VentanaDocumentos',
{
    extend       : 'Ext.window.Window'
    ,itemId      : '_c2_instance'
    ,closeAction : 'destroy'
    ,width       : 700
    ,height      : 400
    ,autoScroll  : true
    ,collapsible   : true
    ,titleCollapse : true
    ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
    ,mostrar     : function()
    {
        var me = this;
        return centrarVentanaInterna(me.show());
    }
    ,constructor : function(config)
    {
        debug('config:',config);
        var me = this;
        
        /*
        EL AUXILIAR DEBE SER UN OBJETO JSON
        ACTUALMENTE SE LEEN LAS PROPIEDADES: lista Y aseguradosPrimero
        lista es para el combo de asociación
        usuariosPrimero es para que muestre arriba los docs de usuario
        */
        var auxJson = {};
        if(!Ext.isEmpty(config.aux))
        {
            var ck = 'Decodificando mapa auxiliar de ventana de documentos';
            try
            {
                auxJson = Ext.decode(config.aux);
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        debug('_c2 auxJson:',auxJson,'.');
        
        Ext.apply(me,
        {
            title   : 'Documentos del tr\u00e1mite'+(Ext.isEmpty(config.ntramite) ? '' : ' '+config.ntramite)
            ,loader :
            {
                scripts   : true
                ,autoLoad : true
                ,url      : _GLOBAL_COMP_URL_VENTANA_DOCS
                ,params   :
                {
                    'smap1.cdunieco'           : config.cdunieco
                    ,'smap1.cdramo'            : config.cdramo
                    ,'smap1.estado'            : config.estado
                    ,'smap1.nmpoliza'          : config.nmpoliza
                    ,'smap1.nmsuplem'          : _NVL(config.nmsuplem,0)
                    ,'smap1.nmsolici'          : config.nmpoliza
                    ,'smap1.ntramite'          : config.ntramite
                    ,'smap1.tipomov'           : '0'
                    ,'smap1.lista'             : Ext.isEmpty(auxJson.lista) ? '' : auxJson.lista
                    ,'smap1.usuariosPrimero'   : 'S' === auxJson.usuariosPrimero ? 'S' : 'N'
                    ,'smap1.cddocumeParasubir' : config.cddocumeParasubir
                    ,'smap1.dsdocumeParasubir' : config.dsdocumeParasubir
                }
            }
        });
        this.callParent(arguments);
    }
});