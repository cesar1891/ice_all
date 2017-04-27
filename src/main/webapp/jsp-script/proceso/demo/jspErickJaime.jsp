<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
////// urls //////

////// variables //////
var store;
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var itemsFormulario = [<s:property value="items.itemsFormulario" escapeHtml="false" />];
////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('modeloJaimeErick',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            {
                name  : 'nombre'
                ,type : 'string'
            }
            ,{
                name  : 'edad'
                ,type : 'int'
            }
            ,{
                name        : 'fenacimi'
                ,type       : 'date'
                ,dateFormat : 'd/m/Y'
            }
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    store = Ext.create('Ext.data.Store',
    {
        model : 'modeloJaimeErick'
        ,data :
        [
            {
                nombre    : 'Erick'
                ,edad     : 25
                ,fenacimi : '01/01/1990'
            }
            ,{
                nombre    : 'Jaime'
                ,edad     : 24
                ,fenacimi : '01/01/1991'
            }
        ]
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        title     : 'Panel principal'
        ,panelPri : 'S'
        ,renderTo : '_p100_divpri'
        ,defaults :
        {
            style : 'margin:5px;'
        }
        ,items    :
        [
		    Ext.create('Ext.form.Panel',
		    {
		        title     : 'ErickJaime'
		        ,layout   :
		        {
		            type     : 'table'
		            ,columns : 2
		        }
		        ,defaults :
		        {
		            style : 'margin:5px;'
		        }
		        ,items : itemsFormulario
		        ,buttonAlign : 'center'
		        ,buttons :
		        [
		            {
                        text     : 'guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            var values = me.up('form').getValues();
                            _mask('Guardando...');
                            Ext.Ajax.request(
                            {
                                url     : '<s:url namespace="/erickjaime" action="guardarEnBase" />'
                                ,params :
                                {
                                    'params.nombre'    : values.nombre
                                    ,'params.edad'     : values.edad
                                    ,'params.fenacimi' : values.fenacimi
                                }
                                ,success : function(response)
                                {
                                    _unmask();
                                    var json = Ext.decode(response.responseText);
                                    if(json.success==true)
                                    {
                                        mensajeCorrecto('datos guardados','se guardo');
                                    }
                                    else
                                    {
                                        mensajeError(json.message);
                                    }
                                }
                                ,failure : function()
                                {
                                    _unmask();
                                    errorComunicacion(null,'error de red al guardar');
                                }
                            });
                        }
                    }
		            ,{
		                text     : 'agregar'
                        ,icon    : '${icons}add.png'
                        ,handler : function(me)
                        {
                            var values = me.up('form').getValues();
                            store.add(new modeloJaimeErick(values));
                        }
		            }
		            ,{
		                text     : 'abrir ventana'
		                ,icon    : '${icons}clock.png'
		                ,handler : function()
		                {
		                    Ext.create('Ext.window.Window',
		                    {
		                        title   : 'ventana1'
		                        ,width  : 300
		                        ,height : 100
		                        ,modal  : true
		                        ,closable : false
		                        ,items  :
		                        [
		                            {
		                                xtype       : 'datefield'
		                                ,fieldLabel : 'fecha'
		                            }
		                        ]
		                        ,buttons :
		                        [
		                            {
		                                text     : 'cerrar'
		                                ,handler : function(me)
		                                {
		                                    me.up('window').close();
		                                }
		                            }
		                        ]
		                    }).show();
		                }
		            }
		        ]
		    })
		    ,Ext.create('Ext.grid.Panel',
		    {
		        title    : 'Tabla'
		        ,width   : 400
		        ,height  : 200
		        ,store   : store
		        ,columns :
		        [
		            {
		                text       : 'nombre'
		                ,width     : 100
		                ,dataIndex : 'nombre'
		            }
		            ,{
                        text       : 'edad'
                        ,width     : 100
                        ,dataIndex : 'edad'
                    }
                    ,{
                        xtype      : 'datecolumn'
                        ,text      : 'fecha naci.'
                        ,width     : 100
                        ,dataIndex : 'fenacimi'
                        ,format    : 'd - m - Y'
                    }
                    ,{
                        xtype : 'actioncolumn'
                        ,icon : '${icons}user.png'
                        ,tooltip : 'cargar'
                        ,handler : function(me,row,col,item,e,record)
                        {
                            me.up('[panelPri=S]').down('form').loadRecord(record);
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
////// funciones //////
</script>
</head>
<body>
<div id="_p100_divpri" style="height:600px;border:1px solid #CCCCCC"></div>
</body>
</html>