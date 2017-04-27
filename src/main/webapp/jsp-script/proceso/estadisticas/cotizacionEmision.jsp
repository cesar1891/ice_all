<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<script>
////// urls //////
var _p46_urlRecuperarCotizacionesEmisiones = '<s:url namespace="/estadisticas" action="recuperarCotizacionesEmisiones" />';
////// urls //////

////// variables //////
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p46_formItems = [<s:property value="items.itemsFiltro" escapeHtml="false" />];
////// componentes dinamicos //////

Ext.onReady(function()
{   
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p46_divpri'
        ,itemId   : '_p46_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId       : '_p46_form'
                ,title       : 'INDICADORES DE COTIZACI&Oacute;N / EMISI&Oacute;N'
                ,defaults    : { style : 'margin:5px;' }
                ,layout      :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items       : _p46_formItems
                ,minHeight   : 100
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        itemId   : '_p46_botonBuscar'
                        ,text    : 'Buscar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : _p46_buscar
                    }
                    ,{
                        itemId   : '_p46_botonLimpiar'
                        ,text    : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                        ,handler : function(me)
                        {
                            me.up('form').getForm().reset();
                        }
                    }
                    ,{
                        text     : 'Cerrar gr&aacute;ficas'
                        ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                        ,handler : function()
                        {
                            try
                            {
                                var ven = Ext.ComponentQuery.query('window');
                                for(var i in ven)
                                {
                                    ven[i].destroy();
                                }
                            }
                            catch(e)
                            {
                                manejaException(e,'Cerrando ventanas');
                            }
                        }
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _p46_buscar(me)
{
    var ck   = '';
    var form = me.up('form');
    try
    {
        ck = 'Validando datos';
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        ck = 'Construyendo parametros';
        var values = form.getValues();
        var params = {};
        for(var i in values)
        {
            params['params.'+i]=values[i];
        }
        debug('params para buscar:',params);
        
        form.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p46_urlRecuperarCotizacionesEmisiones
            ,params  : params
            ,success : function(response)
            {
                form.setLoading(false);
                var ck = '';
                try
                {
                    ck = 'Decodificando respuesta';
                    var json = Ext.decode(response.responseText);
                    debug('### filtros:',json);
                    if(json.success)
                    {
                    	
                        var storeUnieco = Ext.create('Ext.data.JsonStore', {
                            fields: ['SUCURSAL', 'COTIZACIONES', 'EMISIONES'],
                            data: json.objetos.unieco
                        });
                        
                        var storeRamo = Ext.create('Ext.data.JsonStore', {
                            fields: ['PRODUCTO', 'COTIZACIONES', 'EMISIONES'],
                            data: json.objetos.ramo
                        });
                        
                        var storeUsuario = Ext.create('Ext.data.JsonStore', {
                            fields: ['USUARIO', 'COTIZACIONES', 'EMISIONES'],
                            data: json.objetos.usuario
                        });
                        
                        var storeAgente = Ext.create('Ext.data.JsonStore', {
                            fields: ['AGENTE', 'COTIZACIONES', 'EMISIONES'],
                            data: json.objetos.agente
                        });
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Por Sucursal',
                            titleCollapse: true,
                            collapsible: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                theme: 'Category2',
                                width:  450,
                                height: 400,
                                animate: true,
                                store: storeUnieco,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['COTIZACIONES', 'EMISIONES'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Pólizas emitidas'
                                }, {
                                    type: 'Category',
                                    position: 'left', //position: 'bottom',
                                    fields: ['SUCURSAL'],
                                    title: 'Sucursales'
                                }],
                                series: [{
                                    type: 'bar', //type: 'column',
                                    highlight: true,
                                    axis: 'bottom',
                                    xField: 'SUCURSAL',
                                    yField: ['COTIZACIONES', 'EMISIONES'],
                                    title:['Cotizaciones','Emisiones'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['COTIZACIONES', 'EMISIONES'],
                                        renderer: Ext.util.Format.numberRenderer('0')
                                    }
                                }]
                            },
                            buttons: [{
                                text: 'Exportar',
                                icon: '${ctx}/resources/fam3icons/icons/chart_bar.png',
                                handler: function() {
                                    console.log(this);
                                    this.up('window').down('chart').save({
                                        type: 'image/png'
                                    });
                                }
                            }]
                        }).showAt(20,200);
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Por Producto',
                            titleCollapse: true,
                            collapsible: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                width:  450,
                                height: 400,
                                animate: true,
                                store: storeRamo,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['COTIZACIONES', 'EMISIONES'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Pólizas emitidas'
                                }, {
                                    type: 'Category',
                                    position: 'left', //position: 'bottom',
                                    fields: ['PRODUCTO'],
                                    title: 'Productos'
                                }],
                                series: [{
                                    type: 'bar', //type: 'column',
                                    highlight: true,
                                    axis: 'bottom',
                                    xField: 'PRODUCTO',
                                    yField: ['COTIZACIONES', 'EMISIONES'],
                                    title:['Cotizaciones','Emisiones'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['COTIZACIONES', 'EMISIONES'],
                                        renderer: Ext.util.Format.numberRenderer('0')
                                    }
                                }]
                            },
                            buttons: [{
                                text: 'Exportar',
                                icon: '${ctx}/resources/fam3icons/icons/chart_bar.png',
                                handler: function() {
                                    console.log(this);
                                    this.up('window').down('chart').save({
                                        type: 'image/png'
                                    });
                                }
                            }]
                        }).showAt(500, 200);
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Por Usuario',
                            titleCollapse: true,
                            collapsible: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                width:  450,
                                height: 400,
                                animate: true,
                                store: storeUsuario,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['COTIZACIONES', 'EMISIONES'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Pólizas emitidas'
                                }, {
                                    type: 'Category',
                                    position: 'left', //position: 'bottom',
                                    fields: ['USUARIO'],
                                    title: 'Usuarios'
                                }],
                                series: [{
                                    type: 'bar', //type: 'column',
                                    highlight: true,
                                    axis: 'bottom',
                                    xField: 'USUARIO',
                                    yField: ['COTIZACIONES', 'EMISIONES'],
                                    title:['Cotizaciones','Emisiones'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['COTIZACIONES', 'EMISIONES'],
                                        renderer: Ext.util.Format.numberRenderer('0')
                                    }
                                }]
                            },
                            buttons: [{
                                text: 'Exportar',
                                icon: '${ctx}/resources/fam3icons/icons/chart_bar.png',
                                handler: function() {
                                    console.log(this);
                                    this.up('window').down('chart').save({
                                        type: 'image/png'
                                    });
                                }
                            }]
                        }).showAt(20, 620);
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Por Agente',
                            titleCollapse: true,
                            collapsible: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                theme: 'Category1',
                                width:  450,
                                height: 400,
                                animate: true,
                                store: storeAgente,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['COTIZACIONES', 'EMISIONES'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Pólizas emitidas'
                                }, {
                                    type: 'Category',
                                    position: 'left', //position: 'bottom',
                                    fields: ['AGENTE'],
                                    title: 'Agentes'
                                }],
                                series: [{
                                    type: 'bar', //type: 'column',
                                    highlight: true,
                                    axis: 'bottom',
                                    xField: 'AGENTE',
                                    yField: ['COTIZACIONES', 'EMISIONES'],
                                    title:['Cotizaciones','Emisiones'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['COTIZACIONES', 'EMISIONES'],
                                        renderer: Ext.util.Format.numberRenderer('0')
                                    }
                                }]
                            },
                            buttons: [{
                                text: 'Exportar',
                                icon: '${ctx}/resources/fam3icons/icons/chart_bar.png',
                                handler: function() {
                                    console.log(this);
                                    this.up('window').down('chart').save({
                                        type: 'image/png'
                                    });
                                }
                            }]
                        }).showAt(500,620);
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                catch(e)
                {
                	console.log(e);
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                form.setLoading(false);
                errorComunicacion(null,'Al buscar indicadores');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,form);
    }
}
////// funciones //////
</script>
</head>
<body><div id="_p46_divpri" style="height:2000px; border:1px solid #CCCCCC;"></div></body>
</html>