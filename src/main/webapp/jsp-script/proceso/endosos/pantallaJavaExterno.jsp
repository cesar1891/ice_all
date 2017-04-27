<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// variables //////
var _p18_urlOperacion = '<s:url namespace="/endosos" action="operacionJavaExterno" />';
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
    Ext.create('Ext.panel.Panel',
    {
        border    : 0
        ,renderTo : '_p18_divpri'
        ,defaults :
        {
        	style : 'margin:5px;'
        }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
            	title     : 'Formulario'
            	,defaults :
            	{
            		style : 'margin:5px;'
            	}
                ,items    :
                [
                	{
                		xtype       : 'textfield'
                		,fieldLabel : 'a'
                		,name       : 'panel1.a'
                	}
                	,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'b'
                        ,name       : 'panel1.b'
                    }
                	,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'c'
                        ,name       : 'panel1.c'
                        ,readOnly   : true
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
	                {
	                	text     : 'Operacion'
	                	,handler : function()
	                	{
	                		var form = this.up().up();
	                		form.setLoading(true);
	                		Ext.Ajax.request(
	                		{
	                			url      : _p18_urlOperacion
	                			,params  : form.getValues()
	                			,success : function(response)
	                			{
	                				form.setLoading(false);
	                				var resp=Ext.decode(response.responseText);
	                				debug(resp);
	                				form.items.items[2].setValue(resp.panel1.c);
	                			}
	                		    ,failure : function()
	                		    {
	                		    	form.setLoading(false);
	                		    }
	                		});
	                	}
	                }
	            ]
            })
        ]
    });
    ////// contenido //////
    
    ////// cargador //////
    ////// cargador //////
});
</script>
</head>
<body>
<div id="_p18_divpri" style="heigth:600px;"></div>
</body>
</html>