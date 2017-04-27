<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p50_urlRecuperacion  = '<s:url namespace="/recuperacion" action="recuperar"                />';
var _p50_urlGenerarLote   = '<s:url namespace="/consultas"    action="generarLote"              />';
var _p50_urlRecuperarCols = '<s:url namespace="/consultas"    action="recuperarColumnasGridPol" />';
////// urls //////

////// variables //////
var _p50_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
debug('_p50_params:',_p50_params);

var _p50_storeRecibos;

var _p50_dirIconos = '${icons}';
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos /////
var _p50_formBusqItems = [<s:property value="items.itemsFormBusq"     escapeHtml="false" />];
var _p50_gridRecFields = [<s:property value="items.gridRecibosFields" escapeHtml="false" />];
var _p50_gridRecCols   = [];

debug('_p50_formBusqItems', _p50_formBusqItems);

 	_fieldByName('tiporeciboimp').on('change', function(cmb,  newValue, oldValue, eOpts) {
	    debug('Seleccion de combo.');
	    debug('newValue',newValue);
	    if(newValue=='S'){
	    	this,_fieldByName('mes').setValue(('00'+(new Date().getMonth()+2)).slice(-2));
	    }else{
	    	this,_fieldByName('mes').setValue(('00'+(new Date().getMonth()+1)).slice(-2));
	    }
	    	
	});   

    _fieldByName('tiporeciboimp').getStore().on(
    {
        load : function(me)
        {
        	_fieldByName('tiporeciboimp').setValue('S');
            _fieldByName('tiporeciboimp').fireEvent('change',_fieldByName('tiporeciboimp'),'S');
        }
    });
    
    
    
	

//	_fieldByName('mes').getStore().on(
//    {
//        load : function(me)
//        {
//            _fieldByName('mes').setValue(('00'+(new Date().getMonth()+2)).slice(-2));
//        }
//    });

var _p50_formBusqItemsCustom = [];
for(var i in _p50_formBusqItems)
{
	
    if(i==4)
    {
        _p50_formBusqItemsCustom.push(Ext.create('Ext.grid.Panel',
        {
            itemId       : '_p50_gridSucursales'
            ,width       : 255
            ,height      : 80
            ,hideHeaders : true
            //,rowspan     : 2
            ,store       : Ext.create('Ext.data.Store',
            {
                model : 'Generic'
                ,data : []
            })
            ,columns :
            [
                {
                    dataIndex : 'value'
                    ,flex     : 1
                }
                ,{
                    xtype    : 'actioncolumn'
                    ,width   : 30
                    ,icon    : '${icons}delete.png'
                    ,handler : function(view,row,col,item,e,record){ _fieldById('_p50_gridSucursales').getStore().remove(record).rowspan=2; }
                }
            ]
        }));
    }
    _p50_formBusqItemsCustom.push(_p50_formBusqItems[i]);
}

////// componentes dinamicos //////

Ext.onReady(function()
{
	// Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
	
    ////// requires //////
    Ext.require('${defines}VentanaImpresionLote');
    Ext.require('${defines}VentanaDescargaLote');
    ////// requires //////

    ////// modelos //////
    Ext.define('_p50_modeloRecibo',
    {
        extend  : 'Ext.data.Model'
        ,fields : _p50_gridRecFields
    });
    ////// modelos //////
    
    ////// stores //////
    _p50_storeRecibos = Ext.create('Ext.data.Store',
    {
        model     : '_p50_modeloRecibo'
        ,autoLoad : false
        ,proxy    :
        {
            url          : _p50_urlRecuperacion
            ,type        : 'ajax'
            ,extraParams :
            {
                'params.consulta' : 'RECUPERAR_RECIBOS_PARA_EXPLOTAR_DOCS'
            }
            ,reader :
            {
               type             : 'json'
               ,root            : 'list'
               ,successProperty : 'success'
               ,messageProperty : 'message'
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p50_divpri'
        ,itemId   : '_p50_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId       : '_p50_formBusq'
                ,title       : 'B\u00DASQUEDA DE RECIBOS'
                ,defaults    : { style : 'margin:8px;' }
                ,items       : _p50_formBusqItemsCustom
                ,layout      : 
                {
                    type     : 'table'
                    ,columns : 3
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Buscar'
                        ,name    : 'botonbuscar'
                        ,icon    : '${icons}zoom.png'
                        ,handler : function(me)
                        {
                            var ck = 'Buscando recibos';
                            try
                            {
                                var form = me.up('form');
                                
                                if(!form.isValid())
                                {
                                    throw 'Favor de capturar todos los campos requeridos';
                                }
                                
                                if(_fieldById('_p50_gridSucursales').getStore().getCount()==0)
                                {
                                    throw 'Seleccione al menos una sucursal';
                                }
                                
                                _fieldById('_p50_botonImprimir1').disable();
                                _fieldById('_p50_botonImprimir2').disable();
                                _fieldById('botonDescargar').disable();
                                
                                var cduniecos = '|';
                                _fieldById('_p50_gridSucursales').getStore().each(function(record)
                                {
                                    cduniecos = cduniecos + record.get('key') + '|';
                                });
                                
                                _fieldByName('cdtipram').enable();
                                
                                _setLoading(true,form);
                                _p50_loadRecibos(
                                    {
                                        'params.cdtipram'   	: form.getValues()['cdtipram']
                                        ,'params.cduniecos' 	: cduniecos
                                        ,'params.feproces'  	: Ext.Date.format(new Date(), 'd/m/Y')
                                        ,'params.feimpres'  	: Ext.Date.format(new Date((form.getValues()['anio']),form.getValues()['mes'], 0), 'd/m/Y')
                                        ,'params.tiporeciboimp' : form.getValues()['tiporeciboimp'] 
                                    }
                                    ,function()
                                    {
                                        _fieldByName('cdtipram').disable();
                                        _setLoading(false,form);
                                    }
                                );
                            }
                            catch(e)
                            {
                                manejaException(e,ck);
                            }
                        }
                    }
                    ,{
                        text     : 'Limpiar'
                        ,name    : 'botonlimpiar'
                        ,icon    : '${icons}arrow_refresh.png'
                        ,handler : function(me)
                        {
                            me.up('form').getForm().reset();
                            _fieldById('_p50_gridSucursales').getStore().removeAll();
                            _p50_navega(1);
                        }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId    : '_p50_gridRecibos'
                ,title    : 'RESULTADOS'
                ,columns  : _p50_gridRecCols
                ,height   : 350
                ,store    : _p50_storeRecibos
                ,selModel :
                {
                    selType    : 'checkboxmodel'
                    ,mode      : 'SIMPLE'
                    ,listeners :
                    {
                        selectionchange : function(me,selected,eOpts)
                        {
                            _fieldById('_p50_botonImprimir1').setDisabled(selected.length==0);
                            _fieldById('_p50_botonImprimir2').setDisabled(selected.length==0);
                            _fieldById('botonDescargar').setDisabled(selected.length==0);
                        }
                    }
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Impresi\u00F3n gen\u00E9rica'
                        ,icon     : '${icons}printer.png'
                        ,itemId   : '_p50_botonImprimir1'
                        ,disabled : true
                        ,handler  : function(me)
                        {
                            _p50_impresionClic('G');
                        }
                    }
                    ,{
                        text      : 'Impresi\u00F3n intercalada'
                        ,icon     : '${icons}printer.png'
                        ,itemId   : '_p50_botonImprimir2'
                        ,disabled : true
                        ,handler  : function(me)
                        {
                            _p50_impresionClic('I');
                        }
                        ,hidden   : true
                    }
                    ,{
                        text      : 'Descarga PDF'
                            ,icon     : '${icons}printer.png'
                            ,itemId   : 'botonDescargar'
                            ,disabled : true
                            ,handler  : function(me)
                            {
                                _p50_impresionClic('G');
                                
                                descargaPdf();
                            }
                        }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    _fieldByName('cdtipram').on(
    {
        select : function(me,records)
        {
            var ck = 'Heredando producto';
            try
            {
                var cdtipram = records[0].get('key');
                debug('cdtipram:',cdtipram);
                _p50_navega(2);
            }
            catch(e)
            {
                manejaExeption(e,ck);
            }
        }
    });
    
    _fieldByName('cdunieco').on(
    {
        select : function(me,records)
        {
            var ck = 'Agregando sucursal';
            try
            {
                if(_fieldById('_p50_gridSucursales').getStore().indexOf(records[0])==-1)
                {
                    _fieldById('_p50_gridSucursales').getStore().insert(0,records[0]);
                }
                me.reset();
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
    });
    
    
    ////// custom //////
    
    ////// loaders //////
    _p50_navega(1);
    ////// loaders //////
});

////// funciones //////
function _p50_loadRecibos(params,callback)
{
    debug('_p50_loadRecibos params:',params,'callback?:',!Ext.isEmpty(callback));
    _p50_storeRecibos.load(
    {
        params    : params
        ,callback : function(records,op,success)
        {
            callback();
            if(success)
            {
                if(records.length==0)
                {
                    mensajeWarning('No hay resultados');
                }
            }
            else
            {
                mensajeError(op.getError());
            }
        }
    });
}

function _p50_navega(nivel)
{
    if(nivel==1)
    {
        var cmps = Ext.ComponentQuery.query('[name][name!=cdtipram]',_fieldById('_p50_formBusq'));
        debug('cmps:',cmps);
        for(var i in cmps)
        {
            cmps[i].hide();
        }
        _fieldById('_p50_gridSucursales').hide();
        _fieldByName('cdtipram').enable();
        _fieldById('_p50_gridRecibos').hide();
        _fieldById('_p50_gridRecibos').getStore().removeAll();
    }
    else if(nivel==2)
    {
        var cmps = Ext.ComponentQuery.query('[name][fieldLabel!=TIPO DE RAMO]',_fieldById('_p50_formBusq'));
        debug('cmps:',cmps);
        for(var i in cmps)
        {
            cmps[i].show();
        }
        _fieldById('_p50_gridSucursales').show();
        
        var cdtipram = _fieldByName('cdtipram').getValue();
        
        _fieldByName('cdtipram').disable();
        _fieldById('_p50_gridRecibos').show();
        
        var grid = _fieldById('_p50_gridRecibos');
        
        _setLoading(true,grid);
        Ext.Ajax.request(
        {
            url      : _p50_urlRecuperarCols
            ,params  :
            {
                'params.cdtipram'  : cdtipram
                ,'params.pantalla' : 'EXPLOTACION_RECIBOS'
            }
            ,success : function(response)
            {
                _setLoading(false,grid);
                var ck = 'Decodificando respuesta al recuperar columnas';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### cols:',json);
                    if(json.success==true)
                    {
                        ck       = 'Transformando columnas';
                        var cols = Ext.decode('['+json.params.columns+']');
                        debug('cols:',cols);
                        
                        ck = 'Mostrando columnas';
                        grid.reconfigure(_p50_storeRecibos,cols);
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
                _setLoading(false,grid);
                errorComunicacion(null,'Error recuperando columnas');
            }
        });
    }
}

function _p50_impresionClic(tipoimp)
{
    var ck = 'Generando tr\u00E1mite';
    try
    {
        debug('_p50_impresionClic timoimp:',tipoimp);
        centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Se generar\u00E1 el lote y los tr\u00E1mites de impresi\u00F3n ¿Desea continuar?', function(btn)
        {
            if(btn === 'yes')
            {
                var ck = 'Construyendo petici\u00F3n';
                try
                {
                    var jsonData =
                    {
                        params :
                        {
                            cdtipimp  : tipoimp
                            ,tipolote : 'R'
                        }
                        ,list  : []
                    };
                
                    var grid    = _fieldById('_p50_gridRecibos');
                    var records = grid.getSelectionModel().getSelection();
                    debug('records:',records);
                    
                    jsonData.params.cdtipram = records[0].get('cdtipram');
                    
                    for(var i in records)
                    {
                        var rec  = records[i];
                        jsonData.list.push(
                        {
                            cdunieco  : rec.get('cdunieco')
                            ,cdramo   : rec.get('cdramo')
                            ,estado   : rec.get('estado')
                            ,nmpoliza : rec.get('nmpoliza')
                            ,nmsuplem : rec.get('nmsuplem')
                            ,cdagente : rec.get('cdagente')
                            ,nmrecibo : rec.get('nmrecibo')
                        });
                    }
                    debug('jsonData:',jsonData);
                    
                    _setLoading(true,grid);
                    Ext.Ajax.request(
                    {
                        url       : _p50_urlGenerarLote
                        ,jsonData : jsonData
                        ,success  : function(response)
                        {
                            _setLoading(false,grid);
                            var ck = 'Decodificando respuesta al generar lote';
                            try
                            {
                                var json = Ext.decode(response.responseText);
                                debug('### generarLote:',json);
                                if(json.success==true)
                                {
                                    mensajeCorrecto(
                                        'Lote generado'
                                        ,'Se gener\u00F3 el lote '+json.params.lote
                                        ,function()
                                        {
                                            var ck = 'Invocando ventana de impresi\u00F3n';
                                            try
                                            {
                                                var venImp = Ext.create('VentanaImpresionLote',
                                                {
                                                    lote      : json.params.lote
                                                    ,cdtipram : json.params.cdtipram
                                                    ,cdtipimp : json.params.cdtipimp
                                                    ,tipolote : 'R'
                                                    ,callback : function(){ _fieldById('_p50_gridRecibos').getStore().removeAll(); }
                                                });
                                                venImp.on(
                                                {
                                                    close : function()
                                                    {
                                                        _fieldById('_p50_gridRecibos').getStore().removeAll();
                                                    }
                                                });
                                                centrarVentanaInterna(venImp.show());
                                            }
                                            catch(e)
                                            {
                                                manejaException(e,ck);
                                            }
                                        });
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
                            _setLoading(false,grid);
                            errorComunicacion(null,'Error al generar lote');
                        }
                    });
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
        }));
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function descargaPdf(){
	var tipoimp='G';
	 var ck = 'Generando tr\u00E1mite';
	    try
	    {
	        debug('_p50_impresionClic timoimp:',tipoimp);
	        centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Se generar\u00E1 el lote y los tr\u00E1mites de impresi\u00F3n ¿Desea continuar?', function(btn)
	        {
	            if(btn === 'yes')
	            {
	                var ck = 'Construyendo petici\u00F3n';
	                try
	                {
	                    var jsonData =
	                    {
	                        params :
	                        {
	                            cdtipimp  : tipoimp
	                            ,tipolote : 'R'
	                        }
	                        ,list  : []
	                    };
	                
	                    var grid    = _fieldById('_p50_gridRecibos');
	                    var records = grid.getSelectionModel().getSelection();
	                    debug('records:',records);
	                    
	                    jsonData.params.cdtipram = records[0].get('cdtipram');
	                    
	                    for(var i in records)
	                    {
	                        var rec  = records[i];
	                        jsonData.list.push(
	                        {
	                            cdunieco  : rec.get('cdunieco')
	                            ,cdramo   : rec.get('cdramo')
	                            ,estado   : rec.get('estado')
	                            ,nmpoliza : rec.get('nmpoliza')
	                            ,nmsuplem : rec.get('nmsuplem')
	                            ,cdagente : rec.get('cdagente')
	                            ,nmrecibo : rec.get('nmrecibo')
	                        });
	                    }
	                    debug('jsonData:',jsonData);
	                    
	                    _setLoading(true,grid);
	                    Ext.Ajax.request(
	                    {
	                        url       : _p50_urlGenerarLote
	                        ,jsonData : jsonData
	                        ,success  : function(response)
	                        {
	                            _setLoading(false,grid);
	                            var ck = 'Decodificando respuesta al generar lote';
	                            try
	                            {
	                                var json = Ext.decode(response.responseText);
	                                debug('### generarLote:',json);
	                                if(json.success==true)
	                                {
	                                    mensajeCorrecto(
	                                        'Lote generado'
	                                        ,'Se gener\u00F3 el lote '+json.params.lote
	                                        ,function()
	                                        {
	                                            var ck = 'Invocando ventana de impresi\u00F3n';
	                                            try
	                                            {
	                                                var venImp = Ext.create('VentanaDescargaLote',
	                                                {
	                                                    lote      : json.params.lote
	                                                    ,cdtipram : json.params.cdtipram
	                                                    ,cdtipimp : json.params.cdtipimp
	                                                    ,tipolote : 'R'
	                                                    ,callback : function(){ _fieldById('_p50_gridRecibos').getStore().removeAll(); }
	                                                });
	                                                venImp.on(
	                                                {
	                                                    close : function()
	                                                    {
	                                                        _fieldById('_p50_gridRecibos').getStore().removeAll();
	                                                    }
	                                                });
	                                                centrarVentanaInterna(venImp.show());
	                                            }
	                                            catch(e)
	                                            {
	                                                manejaException(e,ck);
	                                            }
	                                        });
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
	                            _setLoading(false,grid);
	                            errorComunicacion(null,'Error al generar lote');
	                        }
	                    });
	                }
	                catch(e)
	                {
	                    manejaException(e,ck);
	                }
	            }
	        }));
	    }
	    catch(e)
	    {
	        manejaException(e,ck);
	    }
}
////// funciones //////
</script>
</head>
<body>
<div id="_p50_divpri" style="height:600px;border:1px solid #CCCCCC;"></div>
</body>
</html>