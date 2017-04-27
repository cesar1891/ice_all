<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _urlGenerarProducto = '<s:url namespace="/definicion"    action="GenerarProducto2" />';
////// urls //////

////// variables //////
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
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
     Ext.create('Ext.panel.Panel',{
        defaults : {
            style : 'margin : 5px;'
        }
        ,renderTo 	: 'mainDiv'
        ,items 		: Ext.create('Ext.form.Panel', {
        	
			        	defaults : {
			                style : 'margin : 10px;'
			            }
        				,title		: 'Ejecutar Generar Producto'
        				,itemId		: 'form'
        				,items		:[
        					{
        						 xtype			:	'textfield'
        						,fieldLabel		:	'CDRAMO'
        						,itemId			:   'cdramo'
        					},
        					{
        						 xtype			:	'button'
        						,text			:	'Generar Producto'
        						,handler		: 	function(){
        							
        							_fieldById('form').setLoading(true);
        							Ext.Ajax.request(
        								    {
        								        url      : _urlGenerarProducto
        								        ,params  :
        								        {
        								            'codigoRamo'  : _fieldById('cdramo').getValue()
        								            
        								        }
        								        ,success:function(response)
        								        {
        								            _fieldById('form').setLoading(false);
        								            var json=Ext.decode(response.responseText);
        								            
        								            if(json.success==true)
        								            {
        								            	mensajeCorrecto("Correcto","Producto Generado");
        								            }else{
        								            	mensajeError(json.mensaje);
        								            }
        								        }
        								        ,failure:function()
        								        {
        								        	_fieldById('form').setLoading(false);
        								            errorComunicacion();
        								        }
        								    });
        						}
        					}
        				]
        			})
        
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
<div id="mainDiv" style="height:400px;border:1px solid #CCCCCC"></div>
</body>
</html>