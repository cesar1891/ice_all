<%@ include file="/taglibs.jsp"%>
<%@ page language="java" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>

////// variables //////
var _p14_urlConsultarFacturas = '<s:url namespace="/consultas" action="consultarFacturas" />';

var _p14_formFiltro;
var _p14_storeFacturas;
var _p14_gridFacturas;
////// variables //////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('_p14_Factura',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="mapaItem.fieldsModelo" /> ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p14_storeFacturas = Ext.create('Ext.data.Store',
    {
        pageSize : 10,
        autoLoad : true,
        model    : '_p14_Factura',
        proxy    :
        {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    _p14_formFiltro = Ext.create('Ext.form.Panel',
    {
        title        : 'Buscar facturas'
        ,defaults    :
        {
            style : 'margin : 5px;'
        }
        ,items       : [ <s:property value="mapaItem.itemsFormulario" /> ]
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Limpiar'
                ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                ,handler : function()
                {
                    this.up().up().getForm().reset();
                }
            }
            ,{
                text     : 'Buscar'
                ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                ,handler : _p14_filtrarFacturas
            }
        ]
    });
    _p14_gridFacturas = Ext.create('Ext.grid.Panel',
    {
        title       : 'Facturas'
        ,store      : _p14_storeFacturas
        ,columns    : [ <s:property value="mapaItem.columnasGrid" /> ]
        ,minHeight  : 200
        ,bbar       :
        {
            displayInfo : true
            ,store      : _p14_storeFacturas
            ,xtype      : 'pagingtoolbar'
        }
        ,viewConfig :
        {
            listeners :
            {
                refresh : function(dataview)
                {
                    Ext.each(dataview.panel.columns, function(column)
                    {
                        column.autoSize();
                    });
                }
            }
        }
    });
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p14_divpri'
        ,defaults :
        {
            style : 'margin : 5px;'
        }
        ,items    :
        [
            _p14_formFiltro
            ,_p14_gridFacturas
        ]
    });
    ////// contenido //////
    
});

////// funciones //////
function _p14_filtrarFacturas()
{
    debug('_p14_filtrarFacturas');
    var form = _p14_formFiltro;
    var valido = form.isValid();
    if(valido)
    {
        form.setLoading(true);
        var params = form.getValues();
        debug('datos a enviar:',params);
        cargaStorePaginadoLocal(_p14_storeFacturas, _p14_urlConsultarFacturas, 'listaMapasStringSalida', params, function()
        {
            form.setLoading(false);
        });
    }
    else
    {
        datosIncompletos();
    }
}
////// funciones //////
</script>
</head>
<body>
<div id="_p14_divpri" style="height : 600px;"></div>
</body>
</html>