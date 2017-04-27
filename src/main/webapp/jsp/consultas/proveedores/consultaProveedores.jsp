<%@ include file="/taglibs.jsp"%>
<%@ page language="java" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>

////// variables //////
var _p13_urlConsultarProveedores = '<s:url namespace="/consultas" action="consultarProveedores" />';

var _p13_formFiltro;
var _p13_storeProveedores;
var _p13_gridProveedores;
////// variables //////

Ext.onReady(function()
{
	////// modelos //////
	Ext.define('_p13_Proveedor',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="mapaItem.fieldsModelo" /> ]
	});
	////// modelos //////
	
	////// stores //////
	_p13_storeProveedores = Ext.create('Ext.data.Store',
    {
        pageSize : 10,
        autoLoad : true,
        model    : '_p13_Proveedor',
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
	_p13_formFiltro = Ext.create('Ext.form.Panel',
	{
		title        : 'Buscar proveedores'
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
		    	,handler : _p13_filtrarProveedores
		    }
		]
	});
	_p13_gridProveedores = Ext.create('Ext.grid.Panel',
	{
		title       : 'Proveedores'
		,store      : _p13_storeProveedores
		,columns    : [ <s:property value="mapaItem.columnasGrid" /> ]
	    ,minHeight  : 200
		,bbar       :
	    {
	        displayInfo : true
	        ,store      : _p13_storeProveedores
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
		renderTo  : '_p13_divpri'
		,defaults :
		{
			style : 'margin : 5px;'
		}
		,items    :
		[
		    _p13_formFiltro
		    ,_p13_gridProveedores
		]
	});
	////// contenido //////
	
});

////// funciones //////
function _p13_filtrarProveedores()
{
	debug('_p13_filtrarProveedores');
	var form = _p13_formFiltro;
	var valido = form.isValid();
	if(valido)
	{
		form.setLoading(true);
		var params = form.getValues();
		debug('datos a enviar:',params);
		cargaStorePaginadoLocal(_p13_storeProveedores, _p13_urlConsultarProveedores, 'listaMapasStringSalida', params, function()
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
<div id="_p13_divpri" style="height : 600px;"></div>
</body>
</html>