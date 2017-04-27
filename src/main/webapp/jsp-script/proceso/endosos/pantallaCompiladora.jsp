<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// overrides //////
extjs_custom_override_mayusculas = false;
////// overrides //////

////// variables //////
var _p19_urlCompilar = '<s:url namespace="/endosos" action="compilarProceso" />';
var _p19_form;
////// variables //////

Ext.onReady(function()
{
	////// modelos //////
	////// modelos //////
	
	////// stores //////
	////// stores //////
	
	////// componentes //////
	////// componentes //////
	
	////// contenido //////
	_p19_form = Ext.create('Ext.form.Panel',
	{
		title     : 'Flujo de proceso'
		,url      : _p19_urlCompilar
		,defaults :
		{
			style : 'margin : 5px;'
		}
	    ,items    :
	    [
	        {
	        	xtype       : 'textfield'
	        	,fieldLabel : 'Archivo'
	        	,name       : 'map1.archivo'
	        	,width      : 800
	        	,value      : '/opt/ice/gseguros/Probando.java'
	        }
	        ,{
	        	xtype          : 'textarea'
	        	,fieldLabel    : 'C&oacute;digo'
	        	,labelPosition : 'top'
	        	,height        : 400
	        	,width         : 800
	        	,name          : 'map1.codigo'
	        	,fieldStyle    :
	        	{
	        		'fontFamily' : 'courier new'
	        		//,'fontSize'  : '8px'
	        	}
	        }
	    ]
	    ,buttonAlign : 'center'
	    ,buttons     :
	    [
	        {
	        	text     : 'Compilar'
	        	,handler : function()
	        	{
	        		var form=this.up().up();
	        		form.setLoading(true);
	        		form.submit(
	        		{
	        			success  : function(action,response)
	        			{
	        				form.setLoading(false);
	        				var json=Ext.decode(response.response.responseText);
	        				if(json.exito)
	        				{
	        					mensajeCorrecto('Compilado','Compilado');
	        				}
	        				else
	        				{
	        					mensajeError(json.mensajeRespuesta);
	        				}
	        			}
	        		    ,failure : function()
	        		    {
	        		    	form.setLoading(false);
	        		    	errorComunicacion();
	        		    }
	        		});
	        	}
	        }
	    ]
	});
	
	Ext.create('Ext.panel.Panel',
	{
		border    : 0
		,defaults :
		{
			style : 'margin : 5px;'
		}
	    ,renderTo : '_p19_divpri'
	    ,items    : _p19_form
	});
	////// contenido //////
});
</script>
</head>
<body>
<div id="_p19_divpri" style="height:1000px;"></div>
</body>
</html>