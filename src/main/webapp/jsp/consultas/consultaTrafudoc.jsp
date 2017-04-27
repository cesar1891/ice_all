<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
////// urls //////

////// variables //////
var _ejecutaQuery  		    = '<s:url namespace="/consultas" action="obtenerCursorTrafudoc"/>';
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var itemsFormularioBusqueda = [<s:property value="imap.busquedaItems" escapeHtml="false" />];
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
        title     : 'Panel principal'
        ,panelPri : 'S'
        ,renderTo : '_p100_divpri'
        ,defaults :
        {
            style : 'margin:5px;'
        }
    	,items    :	[
    	           	 Ext.create('Ext.form.Panel',
    	           			 {	
    	           		 title      : 'Sentencia',
    	           		 itemId     : 'formBusqueda',
    	           		 layout     : {
    	                    type     : 'table',
    	                    columns  : 3
    	                 },
    	           	 	 items      : itemsFormularioBusqueda
    	           	,buttons :
    			        [
    			         {
    			        	 text         : 'Buscar'
   	                         ,icon        : '${icons}zoom.png'
   	                         ,buttonAlign : 'center'
   	                         ,handler     : function(me){
   	                        	 var params = _formValuesToParams(me.up('form').getValues());
   	                        	 params['params.cdfunci'] = _fieldByName('cdfunci', me.up('form')).rawValue;
                                 debug('params ',params);
   	                        	 _mask('Buscando.');
    	                         Ext.Ajax.request(
    	                        		 {
    	                        			 url     : _ejecutaQuery
    	                                	 ,params : params
    	                        			 ,success : function(response){
    	                        				 _unmask();
    	                        				 var json = Ext.decode(response.responseText);
    	                        				 debug('json',json);
    	                        				 if(json.success == true){
    	                        					 debug('json',json);
    	                        					 var list = json.slist1;
    	                        					 if(list.length > 0){
    	                        						var keys1 = [];
    	                        						//Se obtienen keys de objetos
    	                        						for(var k in list[0]){
    	                        							keys1.push(k);
    	                        						}
    	                        						 Ext.define('_modeloCursor',{
    	                        							 extend  : 'Ext.data.Model'
    	                        							 ,fields : keys1
    	                        						 });
    	                        						 var columns = [];
    	                        						 for(var i = 0; i < keys1.length; i++){
    	                        							 columns.push({
    	                        								 dataIndex:keys1[i],
    	                        								 text	  :keys1[i],
    	                        								 flex     : 1
    	                        							 });
    	                        							 }
    	                        						 var store = Ext.create('Ext.data.Store',
    	                        								 {
    	                        							 model : '_modeloCursor'
    	                        							 ,data : list
    	                        						 });
    	                        						 _fieldById('griCursor').reconfigure(store, columns);
    	                        					 }
    	                        					 mensajeCorrecto("","Operacion realizada con exito");
    	                        					 }
    	                        				 else{
    	                        					 mensajeError(json.mensajeRes);
    	                        					 }
    	                        				 }
    	                        			 ,failure : function()
    	                        			 {
    	                        				 _unmask();
    	                        				 errorComunicacion(null,'error de red al ejecutar query');
    	                        				 }
    	                        			 });
    	                         }
   	                         }
    			         ]
    	           	 }),
    	           	 Ext.create('Ext.panel.Panel',
    	           			 {	
    	           		 title     : 'Resultados',
    	           	 	 items    :	[
    	           	 	           	 Ext.create('Ext.grid.Panel',
    	           	 	           			 {
    	           	 	           		 title      : 'Cursor',
    	           	 	           		 itemId     : 'griCursor',
    	           	                     autoscroll : true,
    	           	                     minHeight  : 200,
    	           	                     maxHeight  : 400,
    	           	 	           		 columns : []
    	           	 	           			 })
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