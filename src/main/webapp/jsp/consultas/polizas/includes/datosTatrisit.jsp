<%@ include file="/taglibs.jsp"%>
<%@ page language="java" %>
<script>
////// variables //////
var _p17_formulario;
var _p17_urlLoad = '<s:url namespace="/" action="pantallaValositLoad" />';
var _p17_params  = <s:property value='%{convertToJSON("params")}' escapeHtml='false' />;
debug('_p17_params:',_p17_params);
////// variables //////

Ext.onReady(function()
{
	////// modelos ///////
	Ext.define('_p17_modeloTatrisit',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value = "itemMap.fieldsModelo" /> ]
	});
	////// modelos ///////
	
	////// stores //////
	////// stores //////
	
	////// componentes //////
	_p17_formulario = Ext.create('Ext.form.Panel',
	{
		defaults :
		{
			style : 'margin : 5px;'
		}
	    ,layout   :
	    {
	    	type     : 'table'
	    	,columns : 2
	    }
	    ,items    : [ <s:property value="itemMap.itemsFormulario" /> ]
	});
	////// componentes //////
	
	////// contenido //////
	Ext.create('Ext.panel.Panel',
	{
		defaults  :
		{
			style : 'margin:5px;'
		}
	    ,renderTo : '_p17_divpri'
	    ,model    : '_p17_modeloTatrisit'
	    ,border   : 0
	    ,items    :
	    [
	        _p17_formulario
	    ]
	});
	////// contenido //////
	
	////// loader //////
	Ext.define('_p17_loaderForm',
    {
        extend:'_p17_modeloTatrisit',
        proxy:
        {
            extraParams:
            {
            	'smap1.pv_cdunieco_i'  : _p17_params.cdunieco
                ,'smap1.pv_cdramo_i'   : _p17_params.cdramo
                ,'smap1.pv_estado_i'   : _p17_params.estado
                ,'smap1.pv_nmpoliza_i' : _p17_params.nmpoliza
                ,'smap1.pv_nmsituac_i' : _p17_params.nmsituac
            },
            type:'ajax',
            url : _p17_urlLoad,
            reader:
            {
                type:'json'
            }
        }
    });

    var loaderForm=Ext.ModelManager.getModel('_p17_loaderForm');
    loaderForm.load(123, {
        success: function(resp)
        {
            _p17_formulario.loadRecord(resp);
        },
        failure:function()
        {
            mensajeError('Error al obtener los datos del asegurado');
        }
    });
	////// loader //////
});

////// funciones //////
////// funciones //////
</script>
<div id="_p17_divpri" style="height:350px"></div>