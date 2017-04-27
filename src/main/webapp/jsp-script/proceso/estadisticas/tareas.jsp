<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<script>
////// urls //////
var _p47_urlRecuperarTareas = '<s:url namespace="/estadisticas" action="recuperarTareas" />';
var _p47_URL_OBTENER_REPORTE_EXCEL = '<s:url namespace="/reportes" action="procesoObtencionReporte" />';
////// urls //////

////// variables //////
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p47_formItems = [<s:property value="items.itemsFiltro" escapeHtml="false" />];
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
        renderTo  : '_p47_divpri'
        ,itemId   : '_p47_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId       : '_p47_form'
                ,title       : 'INDICADORES DE PROCESO'
                ,defaults    : { style : 'margin:5px;' }
                ,layout      :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items       : _p47_formItems
                ,minHeight   : 100
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        itemId   : '_p47_botonBuscar'
                        ,text    : 'Buscar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : _p47_buscar
                    }
                    ,{
                        itemId   : '_p47_botonLimpiar'
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
                    },
                    {
                        text  : 'Exportar',
                        //icon    : '${ctx}/resources/fam3icons/icons/page_excel.png',
                        icon    : '${ctx}/resources/fam3icons/icons/page_excel.png',
                        handler: function(btn, e) {
                            
                            var formCmpRep = this.up('form').getForm();
                            
                            debug('formulario:', formCmpRep);
                            debug(formCmpRep.getValues());
                            
                            if (formCmpRep.isValid()) {
                                
                                //Iteramos los fields del formulario para buscar los del tipo datefield
                                Ext.Array.each(formCmpRep.getFields().items, function(item){
                                    debug(item);
                                });
                                
                                formCmpRep.submit({
                                	url : _p47_URL_OBTENER_REPORTE_EXCEL,
                                    standardSubmit : true,
                                    params: {
                                        cdreporte : 'REPEST001'
                                    },
                                    success: function(form, action) {
                                        
                                    },
                                    failure: function(form, action) {
                                        switch (action.failureType) {
                                            case Ext.form.action.Action.CONNECT_FAILURE:
                                                Ext.Msg.alert('Error', 'Error de comunicaci&oacute;n');
                                                break;
                                            case Ext.form.action.Action.SERVER_INVALID:
                                            case Ext.form.action.Action.LOAD_FAILURE:
                                                Ext.Msg.alert('Error', 'Error del servidor, consulte a soporte');
                                                break;
                                       }
                                    }
                                });
                            } else {
                                Ext.Msg.show({
                                    title: 'Aviso',
                                    msg: 'Complete la informaci&oacute;n requerida',
                                    buttons: Ext.Msg.OK,
                                    animateTarget: btn,
                                    icon: Ext.Msg.WARNING
                                });
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
function _p47_buscar(me)
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
            params[i]=values[i];
        }
        debug('params para buscar:',params);
        
        form.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p47_urlRecuperarTareas
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
                        //alert('richi');
                    	
                    	var storeRamo = Ext.create('Ext.data.JsonStore', {
                            fields: ['PRODUCTO', 'TODAS', 'TIEMPO', 'ESCALA'],
                            data: json.objetos.ramo
                        });
                        
                        var storeRol = Ext.create('Ext.data.JsonStore', {
                            fields: ['CDSISROL', 'ROL', 'TODAS', 'TIEMPO', 'ESCALA'],
                            data: json.objetos.rol
                        });
                        
                        var storeTarea = Ext.create('Ext.data.JsonStore', {
                            fields: ['CDTAREA', 'TAREA',  'TODAS', 'TIEMPO', 'ESCALA'],
                            data: json.objetos.tarea
                        });
                        
                        var storeSucursal = Ext.create('Ext.data.JsonStore', {
                            fields: ['SUCURSAL', 'TODAS', 'TIEMPO', 'ESCALA'],
                            data: json.objetos.unieco
                        });
                        
                        var storeUsuario = Ext.create('Ext.data.JsonStore', {
                            fields: ['USUARIO', 'TODAS', 'TIEMPO', 'ESCALA'],
                            data: json.objetos.usuario
                        });
                        
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Por nombre de Tareas',
                            titleCollapse: true,
                            collapsible: true,
                            maximizable: true,
                            height: 375,
                            width: 930,
                            layout: 'fit',
                            items: {
                                xtype: 'chart',
                                width:  920,
                                height: 400,
                                animate: true,
                                store: storeTarea,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Tareas'
                                }, {
                                    type: 'Category',
                                    position: 'left', //position: 'bottom',
                                    fields: ['TAREA'],
                                    title: 'Nombre'
                                }],
                                series: [{
                                    type: 'bar', //type: 'column',
                                    highlight: true,
                                    axis: 'bottom',
                                    xField: 'TAREA',
                                    yField: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    title:['Total','En tiempo', 'Escaladas'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['TODAS', 'TIEMPO', 'ESCALA'],
                                        renderer: Ext.util.Format.numberRenderer('0')
                                        //orientation: 'horizontal',
                                        //color: '#333',
                                       //'text-anchor': 'middle'
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
                        }).showAt(20, 230);
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Tareas por Producto',
                            titleCollapse: true,
                            collapsible: true,
                            maximizable: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                theme: 'Yellow',
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
                                    fields: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Tareas'
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
                                    yField: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    title:['Total','En tiempo', 'Escaladas'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['TODAS', 'TIEMPO', 'ESCALA'],
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
                        }).showAt(20,630);
                        
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Tareas por Sucursal',
                            titleCollapse: true,
                            collapsible: true,
                            maximizable: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                width:  450,
                                height: 400,
                                animate: true,
                                store: storeSucursal,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Tareas'
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
                                    yField: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    title:['Total','En tiempo', 'Escaladas'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['TODAS', 'TIEMPO', 'ESCALA'],
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
                        }).showAt(20,1050);
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Tareas por Rol',
                            titleCollapse: true,
                            collapsible: true,
                            maximizable: true,
                            height: 400,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                width:  450,
                                height: 400,
                                animate: true,
                                store: storeRol,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Tareas'
                                }, {
                                    type: 'Category',
                                    position: 'left', //position: 'bottom',
                                    fields: ['ROL'],
                                    title: 'Roles'
                                }],
                                series: [{
                                    type: 'bar', //type: 'column',
                                    highlight: true,
                                    axis: 'bottom',
                                    xField: 'ROL',
                                    yField: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    title:['Total','En tiempo', 'Escaladas'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['TODAS', 'TIEMPO', 'ESCALA'],
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
                        }).showAt(20, 1470);
                        
                        Ext.create('Ext.window.Window', {
                            title: 'Tareas por Usuario',
                            titleCollapse: true,
                            collapsible: true,
                            maximizable: true,
                            height: 1240,
                            width: 450,
                            layout: 'fit',
                            items: {  
                                xtype: 'chart',
                                theme: 'Category1',
                                width:  450,
                                height: 1240,
                                animate: true,
                                store: storeUsuario,
                                legend: {
                                  position: 'right'  
                                },
                                axes: [{
                                    type: 'Numeric',
                                    position: 'bottom', //position: 'left',
                                    fields: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    minimum: 0,
                                    label: {
                                        renderer: Ext.util.Format.numberRenderer('0,0')
                                    },
                                    grid: true,
                                    title: 'Tareas'
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
                                    yField: ['TODAS', 'TIEMPO', 'ESCALA'],
                                    title:['Total','En tiempo', 'Escaladas'],
                                    label: {
                                        display: 'insideEnd',
                                        field: ['TODAS', 'TIEMPO', 'ESCALA'],
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
                        }).showAt(500,630);
                    	
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                catch(e)
                {
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
<body><div id="_p47_divpri" style="height:2000px; border:1px solid #CCCCCC;"></div></body>
</html>