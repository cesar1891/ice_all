/**
 * Ventana para mostrar la impresión de pólizas
 * COMPONENTE 0 => #C0
 */
Ext.define('VentanaImpresionLote',
{
    extend : 'Ext.window.Window'
    ,title : 'Impresi\u00F3n'
    ,modal : true
    ,items :
    [
        {
            xtype   : 'panel'
            ,layout :
            {
                type     : 'table'
                ,columns : 3
            }
            ,defaults : { style : 'margin:5px;' }
            ,border   : 0
            ,height   : 150
            ,items    :
            [
                {
                    xtype    : 'displayfield'
                    ,value   : 'Papeler\u00EDa completa: incluye car\u00E1tulas, endosos, remesas y recibos'
                    ,hoja    : 'BM'
                    ,tipoimp : 'GI'
                    ,suma    : 2
                    ,sumaHab : 2
                }
                ,{
                    xtype    : 'button'
                    ,text    : 'Imprimir papeler\u00EDa completa'
                    ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'page_lightning.png'
                    ,hoja    : 'BM'
                    ,tipoimp : 'GI'
                    ,suma    : 2
                    ,sumaHab : 2
                    ,peso    : 11
                }
                ,{
                    xtype    : 'image'
                    ,src     : _GLOBAL_DIRECTORIO_ICONOS+'cancel.png'
                    ,hoja    : 'BM'
                    ,tipoimp : 'GI' 
                    ,suma    : 2
                    ,sumaHab : 2
                }
                ,{
                    xtype    : 'displayfield'
                    ,value   : 'Papeler\u00EDa: incluye car\u00E1tulas, endosos y remesas'
                    ,hoja    : 'B'
                    ,tipoimp : 'X'
                    ,suma    : 1
                    ,sumaHab : 1
                    ,hidden  : true
                }
                ,{
                    xtype    : 'button'
                    ,text    : 'Imprimir papeler\u00EDa'
                    ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'page_go.png'
                    ,hoja    : 'B'
                    ,tipoimp : 'X'
                    ,suma    : 1
                    ,sumaHab : 1
                    ,peso    : 1
                    ,hidden  : false
                }
                ,{
                    xtype    : 'image'
                    ,src     : _GLOBAL_DIRECTORIO_ICONOS+'cancel.png'
                    ,hoja    : 'B'
                    ,tipoimp : 'X' 
                    ,suma    : 1
                    ,sumaHab : 1
                    ,hidden  : false
                }
                ,{
                    xtype    : 'displayfield'
                    ,value   : 'Credenciales: impresi\u00F3n en tarjeta especial'
                    ,hoja    : 'C'
                    ,tipoimp : 'GI'
                    ,suma    : 1
                    ,sumaHab : 1
                }
                ,{
                    xtype    : 'button'
                    ,text    : 'Imprimir credenciales'
                    ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'group_key.png'
                    ,hoja    : 'C'
                    ,tipoimp : 'GI'
                    ,suma    : 1
                    ,sumaHab : 1
                    ,peso    : 100
                }
                ,{
                    xtype    : 'image'
                    ,src     : _GLOBAL_DIRECTORIO_ICONOS+'cancel.png'
                    ,hoja    : 'C'
                    ,tipoimp : 'GI'
                    ,suma    : 1
                    ,sumaHab : 1
                }
                ,{
                    xtype    : 'displayfield'
                    ,value   : 'Recibos: impresi\u00F3n de recibos de pago'
                    ,hoja    : 'M'
                    ,tipoimp : 'X'
                    ,suma    : 1
                    ,sumaHab : 1
                    ,hidden  : false
                }
                ,{
                    xtype    : 'button'
                    ,text    : 'Imprimir recibos'
                    ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'coins.png'
                    ,hoja    : 'M'
                    ,tipoimp : 'X'
                    ,suma    : 1
                    ,sumaHab : 1
                    ,peso    : 10
                    ,hidden  : false
                }
                ,{
                    xtype    : 'image'
                    ,src     : _GLOBAL_DIRECTORIO_ICONOS+'cancel.png'
                    ,hoja    : 'M'
                    ,tipoimp : 'X'
                    ,suma    : 1
                    ,sumaHab : 1
                    ,hidden  : false
                }
            ]
        }
    ]
    ,buttonAlign : 'center'
    ,buttons     :
    [
        {
            itemId   : '_c0_botonContinuar'
            ,text    : 'Continuar'
            ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'accept.png'
            ,hidden  : true
            ,handler : function(me)
            {
                var ven = me.up('window');
                if(!Ext.isEmpty(ven.callback))
                {
                    ven.callback();
                }
                ven.destroy();
            }
        }
        ,{
            itemId   : '_c0_botonRegresar'
            ,text    : 'Regresar'
            ,icon    : _GLOBAL_DIRECTORIO_ICONOS+'control_repeat_blue.png'
            ,hidden  : true
            ,handler : function(me)
            {
                var ven = me.up('window');
                if(!Ext.isEmpty(ven.callback))
                {
                    ven.callback();
                }
                ven.destroy();
            }
        }
    ]
    ,constructor : function(config)
    {
        debug('config:',config,',arguments:',arguments);
        if(Ext.isEmpty(config))
        {
            throw '#C0 - No se recibi\u00F3 configuraci\u00F3n';
        }
        if(Ext.isEmpty(config.lote))
        {
            throw '#C0 - No se recibi\u00F3 el lote';
        }
        if(Ext.isEmpty(config.cdtipram))
        {
            throw '#C0 - No se recibi\u00F3 el tipo de ramo';
        }
        if(Ext.isEmpty(config.tipolote))
        {
            throw '#C0 - No se recibi\u00F3 el tipo de lote';
        }
        if(Ext.isEmpty(config.cdtipimp))
        {
            throw '#C0 - No se recibi\u00F3 el tipo de impresi\u00F3n';
        }
        this.callParent(arguments);
    }
    ,listeners :
    {
        afterrender : function(me)
        {
            var ck = 'Construyendo ventana';
            try
            {
                debug('afterrender lote,cdtipram,cdtipimp:',me.lote,me.cdtipram,me.cdtipimp,'.');
                
                me.setTitle('Impresi\u00F3n de lote '+me.lote);
            
                //ocultar comps que no vayan con el tipo de imp
                var cmps = Ext.ComponentQuery.query('[tipoimp]',me);
                debug('cmps:',cmps);
                for(var i in cmps)
                {
                    var cmp = cmps[i];
                    debug('tipoimp:',cmp.tipoimp,'indexof',me.cdtipimp,cmp.tipoimp.indexOf(me.cdtipimp));
                    if(cmp.tipoimp.indexOf(me.cdtipimp)==-1)
                    {
                        cmp.hide();
                    }
                }
                
                ck = 'Recuperando impresiones disponibles';
                _setLoading(true,me);
                Ext.Ajax.request(
                {
                    url     : _GLOBAL_URL_RECUPERACION
                    ,params :
                    {
                        'params.consulta'  : 'RECUPERAR_IMPRESIONES_DISPONIBLES'
                        ,'params.cdtipram' : me.cdtipram
                        ,'params.tipolote' : me.tipolote
                    }
                    ,success : function(response)
                    {
                        _setLoading(false,me);
                        var ck = 'Decodificando respuesta de impresiones disponibles';
                        try
                        {
                            var json = Ext.decode(response.responseText);
                            debug('### impr disp:',json);
                            if(json.success==true)
                            {
                                var perm = json.params.imprdisp;
                                debug('perm:',perm);
                                // 1 1 1 (1 = si, 0 = no)
                                // C M B (C = credencial, M = membrete, B = blanca)
                                if(perm%10==0)
                                {
                                    var blan = Ext.ComponentQuery.query('[hoja*=B]');
                                    debug('quitar blanca:',blan);
                                    for(var i in blan)
                                    {
                                        blan[i].suma = blan[i].suma-1;
                                        debug('suma blan:',blan[i].suma);
                                        if(Number(blan[i].suma)<1)
                                        {
                                            blan[i].hide();
                                        }
                                    }
                                }
                                perm = Math.floor(perm/10);
                                if(perm%10==0)
                                {
                                    var mem = Ext.ComponentQuery.query('[hoja*=M]');
                                    debug('quitar membrete:',mem);
                                    for(var i in mem)
                                    {
                                        mem[i].suma = mem[i].suma-1;
                                        debug('suma mem:',mem[i].suma);
                                        if(Number(mem[i].suma)<1)
                                        {
                                            mem[i].hide();
                                        }
                                    }
                                }
                                perm = Math.floor(perm/10);
                                if(perm%10==0)
                                {
                                    var cred = Ext.ComponentQuery.query('[hoja*=C]');
                                    debug('quitar credencial:',cred);
                                    for(var i in cred)
                                    {
                                        cred[i].suma = cred[i].suma-1;
                                        debug('suma cred:',cred[i].suma);
                                        if(Number(cred[i].suma)<1)
                                        {
                                            cred[i].hide();
                                        }
                                    }
                                }
                                me.doLayout();
                                
                                me.actualizarBotones();
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
                        _setLoading(false,me);
                        errorComunicacion(null,'Error recuperando impresiones disponibles');
                    }
                });
                
                var buttons = Ext.ComponentQuery.query('[xtype=button][peso]',me);
                debug('buttons:',buttons);
                
                for(var i in buttons)
                {
                    buttons[i].handler=me.buscarImpresora;
                }
                
                if(true==me.cancelable)
                {
                    me.down('[xtype=button][text*=Regresar]').show();
                }
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
    }
    ,buscarImpresora : function(bot)
    {
        debug('buscarImpresora bot:',bot);
        
        var me = bot.up('window');
        
        var ck = 'Buscando impresoras';
        try
        {
            _setLoading(true,me);
            Ext.Ajax.request(
            {
                url     : _GLOBAL_URL_RECUPERACION
                ,params :
                {
                    'params.consulta'  : 'RECUPERAR_IMPRESORAS'
                    ,'params.papel'    : bot.hoja
                    ,'params.swactivo' : 'S'
                }
                ,success : function(response)
                {
                    _setLoading(false,me);
                    var ck = 'Decodificando respuesta al recuperar impresoras';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### impresoras:',json);
                        if(json.success==true)
                        {
                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                            {
                                title  : 'IMPRESORAS DISPONIBLES'
                                ,modal : true
                                ,items :
                                [
                                    Ext.create('Ext.grid.Panel',
                                    {
                                        width    : 500
                                        ,itemId  : '_c0_gridImpresoras'
                                        ,height  : 250
                                        ,columns :
                                        [
                                            {
                                                text       : 'USUARIO'
                                                ,width     : 100
                                                ,dataIndex : 'cdusuari'
                                            }
                                            ,{
                                                text       : 'NOMBRE'
                                                ,dataIndex : 'nombre'
                                                ,flex      : 1
                                                ,hidden    : true
                                            }
                                            ,{
                                                text       : 'DESCRIPCI\u00D3N'
                                                ,dataIndex : 'descrip'
                                                ,flex      : 2
                                            }
                                            ,{
                                                text       : 'BANDEJAS'
                                                ,width     : 100
                                                ,dataIndex : 'num_bandejas'
                                            }
                                        ]
                                        ,store : Ext.create('Ext.data.Store',
                                        {
                                            fields :
                                            [
                                                "cdusuari"
                                                ,"ip"
                                                ,"nmimpres"
                                                ,"num_bandejas"
                                                ,"nombre"
                                                ,"descrip"
                                                ,"swactivo"
                                                ,"charola1"
                                                ,"charola2"
                                            ]
                                            ,data  : json.list
                                        })
                                        ,selModel :
                                        {
                                            selType        : 'checkboxmodel'
                                            ,allowDeselect : true
                                            ,mode          : 'SINGLE'
                                            ,listeners     :
                                            {
                                                selectionchange : function(me,selected,eOpts)
                                                {
                                                    if(selected.length==1)
                                                    {
                                                        _fieldById('_c0_botonImprimir').enable();
                                                        _fieldById('_c0_botonImprimir2').enable();
                                                    }
                                                    else
                                                    {
                                                        _fieldById('_c0_botonImprimir').disable();
                                                        _fieldById('_c0_botonImprimir2').disable();
                                                    }
                                                }
                                            }
                                        }
                                        ,buttonAlign : 'center'
                                        ,buttons     :
                                        [
                                            {
                                                text      : 'Imprimir (test 1 hoja)'
                                                ,itemId   : '_c0_botonImprimir2'
                                                ,icon     : _GLOBAL_DIRECTORIO_ICONOS+'printer.png'
                                                ,disabled : true
                                                ,handler  : function(boton)
                                                {
                                                    if(_fieldById('_c0_gridImpresoras').getSelectionModel().getSelection()[0].get('nombre')=='download')
                                                    {
                                                        me.download(
                                                            bot
                                                            ,boton
                                                        );
                                                    }
                                                    else
                                                    {
                                                        me.imprimir(
                                                            bot
                                                            ,boton
                                                            ,_fieldById('_c0_gridImpresoras').getSelectionModel().getSelection()
                                                            ,'S'
                                                        );
                                                    }
                                                }
                                                ,hidden   : true
                                            },
                                            {
                                                text      : 'Imprimir'
                                                ,itemId   : '_c0_botonImprimir'
                                                ,icon     : _GLOBAL_DIRECTORIO_ICONOS+'printer.png'
                                                ,disabled : true
                                                ,handler  : function(boton)
                                                {
                                                    if(_fieldById('_c0_gridImpresoras').getSelectionModel().getSelection()[0].get('nombre')=='download')
                                                    {
                                                        me.download(
                                                            bot
                                                            ,boton
                                                        );
                                                    }
                                                    else
                                                    {
                                                        me.imprimir(
                                                            bot
                                                            ,boton
                                                            ,_fieldById('_c0_gridImpresoras').getSelectionModel().getSelection()
                                                        );
                                                    }
                                                }
                                            }
                                        ]
                                    })
                                ]
                            }).show());
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
                    _setLoading(false,me);
                    errorComunicacion(null,'Error recuperando impresoras');
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck);
        }
    }
    ,imprimir : function(botPap,botImp,printerRecords,test)
    {
        debug('imprimir botPap,botImp,printerRecords:',botPap,botImp,printerRecords,'.');
        debug('imprimir test:',test,'.');
        
        var me  = this;
        var win = botImp.up('window');
        
        var ck = 'Imprimiendo';
        try
        {
            if(Ext.isEmpty(botPap))
            {
                throw 'Se perdi\u00F3 el componente de impresi\u00F3n';
            }
            if(Ext.isEmpty(printerRecords)||Ext.isEmpty(printerRecords[0]))
            {
                throw 'No se recibi\u00F3 la impresora';
            }
            var printer = printerRecords[0];
            
            _setLoading(true,win);
            Ext.Ajax.request(
            {
                url      : _GLOBAL_URL_IMPRIMIR_LOTE
                ,timeout : 1000*60*60*3
                ,params  :
                {
                    'params.lote'      : me.lote
                    ,'params.hoja'     : botPap.hoja
                    ,'params.peso'     : botPap.peso
                    ,'params.cdtipram' : me.cdtipram
                    ,'params.cdtipimp' : me.cdtipimp
                    ,'params.tipolote' : me.tipolote
                    ,'params.dsimpres' : printer.get('nombre')
                    ,'params.charola1' : printer.get('charola1')
                    ,'params.charola2' : printer.get('charola2')
                    ,'params.test'     : test
                    ,'params.swimpdpx' : printer.get('swactivo')
                }
                ,success : function(response)
                {
                    _setLoading(false,win);
                    var ck = 'Decodificando respuesta al imprimir';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        if(json.success==true && json.dwnError!=true)
                        {
                            mensajeCorrecto(
                                'Impresi\u00F3n correcta'
                                ,'Impresi\u00F3n correcta'
                                ,function()
                                {
                                    win.destroy();
                                    me.actualizarBotones();
                                }
                            );
                        }else if(json.dwnError==true){
                        	var me2=me;
                        	centrarVentanaInterna(Ext.Msg.alert("Error","Se imprimi&oacute; pero con errores, se descargar&aacute; la lista de archivos no encontrados",function(){
                         		debug('DescargarListaErrores');
                                var me = win;
                                var ck = 'Descargando';
                                try
                                {
                                    Ext.create('Ext.form.Panel').submit(
                                    {
                                        url             : _GLOBAL_URL_DESCARGAR_LISTA_ERROR_ARCHIVO
                                        ,params         :
                                        {
                                           
                                        }
                                        ,standardSubmit : true
                                        ,target         : '_blank'
                                    });
                                    win.destroy();
                                    me2.actualizarBotones();
                               }
                               catch(e)
                               {
                            	  
                            	   manejaException(e,ck);
                               }
                         	}));
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
                ,failure  : function()
                {
                    _setLoading(false,win);
                    errorComunicacion(null,'Error al imprimir');
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck);
        }
    }
    ,download : function(botPap,botImp)
    {
        debug('download botPap,botImp:',botPap,botImp,'.');
        
        var me  = this;
        var win = botImp.up('window');
        
        var ck = 'Descargando';
        try
        {
            if(Ext.isEmpty(botPap))
            {
                throw 'Se perdi\u00F3 el componente de impresi\u00F3n';
            }
            
            Ext.create('Ext.form.Panel').submit(
            {
                url             : _GLOBAL_URL_DESCARGAR_LOTE
                ,params         :
                {
                    'params.lote'      : me.lote
                    ,'params.hoja'     : botPap.hoja
                    ,'params.peso'     : botPap.peso
                    ,'params.cdtipram' : me.cdtipram
                    ,'params.cdtipimp' : me.cdtipimp
                    ,'params.tipolote' : me.tipolote
                }
                ,standardSubmit : true
                ,target         : '_blank'
            });
            
            _setLoading(true,win);
            Ext.Ajax.request(
            {
                url      : _GLOBAL_URL_ESPERAR_DESCARGA_LOTE
                ,timeout : 1000*60*60*3
                ,success : function(response)
                {
                    _setLoading(false,win);
                    var ck = 'Decodificando respuesta al imprimir';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        if(json.success==true &&  json.dwnError!=true)
                        {
                            mensajeCorrecto(
                                'Descarga correcta'
                                ,'Descarga correcta'
                                ,function()
                                {
                                    win.destroy();
                                    me.actualizarBotones();
                                }
                            );
                        }
                        else if(json.dwnError==true){
                        	
                       	 centrarVentanaInterna(Ext.Msg.alert("Error","Se gener&oacute; el pdf pero con errores, se descargar&aacute; la lista de archivos no encontrados",function(){
                        		debug('DescargarListaErrores');
                               var me = win;
                               var ck = 'Descargando';
                               try
                               {
                                   Ext.create('Ext.form.Panel').submit(
                                   {
                                       url             : _GLOBAL_URL_DESCARGAR_LISTA_ERROR_ARCHIVO
                                       ,params         :
                                       {
                                          
                                       }
                                       ,standardSubmit : true
                                       ,target         : '_blank'
                                   });
                              }
                              catch(e)
                              {
                           	  
                           	   manejaException(e,ck);
                              }
                        	}));
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
                ,failure  : function()
                {
                    _setLoading(false,win);
                    errorComunicacion(null,'Error al descargar');
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck);
        }
    }
    ,actualizarBotones : function()
    {
        var me = this;
        var ck = 'Recuperando estado de impresi\u00F3n';
        try
        {
            _setLoading(true,me);
            Ext.Ajax.request(
            {
                url      : _GLOBAL_URL_RECUPERACION
                ,params  :
                {
                    'params.consulta' : 'RECUPERAR_DETALLE_IMPRESION_LOTE'
                    ,'params.lote'    : me.lote
                    ,'params.tramite' : me.tramite
                }
                ,success : function(response)
                {
                    _setLoading(false,me);
                    var ck = 'Decodificando respuesta de impresiones anteriores';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### impresiones anteriores:',json);
                        if(json.success==true)
                        {
                            var req  = json.params.requeridas
                                ,eje = json.params.ejecutadas;
                            
                            debug('req:',req,',eje:',eje);
                            
                            var modReq = req%10;
                            var modEje = eje%10;
                            
                            if(modReq-modEje==0)//B ejecutada
                            {
                                var comps = Ext.ComponentQuery.query('[hoja*=B]');
                                debug('quitar B:',comps);
                                for(var i in comps)
                                {
                                    comps[i].sumaHab = comps[i].sumaHab-1;
                                    debug('sumaHab B:',comps[i].sumaHab);
                                    if(Number(comps[i].sumaHab)<1)
                                    {
                                        comps[i].disable();
                                        if(comps[i].xtype=='image')
                                        {
                                            comps[i].setSrc(_GLOBAL_DIRECTORIO_ICONOS+'accept.png');
                                        }
                                    }
                                }
                            }
                            
                            req = Math.floor(req/10);
                            eje = Math.floor(eje/10);
                            modReq = req%10;
                            modEje = eje%10;
                            
                            if(modReq-modEje==0)//M ejecutada
                            {
                                var comps = Ext.ComponentQuery.query('[hoja*=M]');
                                debug('quitar M:',comps);
                                for(var i in comps)
                                {
                                    comps[i].sumaHab = comps[i].sumaHab-1;
                                    debug('sumaHab M:',comps[i].sumaHab);
                                    if(Number(comps[i].sumaHab)<1)
                                    {
                                        comps[i].disable();
                                        if(comps[i].xtype=='image')
                                        {
                                            comps[i].setSrc(_GLOBAL_DIRECTORIO_ICONOS+'accept.png');
                                        }
                                    }
                                }
                            }
                            
                            req = Math.floor(req/10);
                            eje = Math.floor(eje/10);
                            modReq = req%10;
                            modEje = eje%10;
                            
                            if(modReq-modEje==0)//C ejecutada
                            {
                                var comps = Ext.ComponentQuery.query('[hoja*=C]');
                                debug('quitar C:',comps);
                                for(var i in comps)
                                {
                                    comps[i].sumaHab = comps[i].sumaHab-1;
                                    debug('sumaHab C:',comps[i].sumaHab);
                                    if(Number(comps[i].sumaHab)<1)
                                    {
                                        comps[i].disable();
                                        if(comps[i].xtype=='image')
                                        {
                                            comps[i].setSrc(_GLOBAL_DIRECTORIO_ICONOS+'accept.png');
                                        }
                                    }
                                }
                            }
                            
                            var images = Ext.ComponentQuery.query('[xtype=image][hidden=false]',me);
                            debug('images:',images);
                            var todos = true;
                            for(var i in images)
                            {
                                var image = images[i];
                                if(image.src.indexOf('accept')==-1)
                                {
                                    todos = false;
                                    break;
                                }
                            }
                            if(todos)
                            {
                                _fieldById('_c0_botonContinuar').show();
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
                    _setLoading(false,me);
                    errorComunicacion(null,'Error al recuperar impresiones anteriores');
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck);
        }
    }
});