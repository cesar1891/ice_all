<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var testpantallaStore;
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function()
{

	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('testpantallaModelo',
	{
		extend : 'Ext.data.Model'
		,fields :
		[
            <s:property value="item1" />
		]
	});
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	testpantallaStore = Ext.create('Ext.data.Store',
    {
        autoLoad : true
        ,model   : 'testpantallaModelo'
        ,proxy   :
        {
            type         : 'memory'
            ,reader      : 'json'
            ,data        : []
        }
    });
	/*////////////////*/
	////// stores //////
	////////////////////
	
	/////////////////////////
	////// componentes //////
	/*/////////////////////*/
	/*/////////////////////*/
	////// componentes //////
	/////////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	Ext.create('Ext.panel.Panel',
	{
		defaults :
		{
			style : 'margin : 5px;'
		}
	    ,renderTo : 'testpantallaDivPri'
	    ,items :
	    [
	        Ext.create('Ext.form.Panel',
	        {
	        	title : 'Formulario'
	        	,layout :
	        		{
	        		type : 'table'
	        		,columns : 2
	        		}
	        	,items :
	        	[
	        	    <s:property value="item3" />
	        	]
	        })
	        ,Ext.create('Ext.grid.Panel',
	        {
	        	title : 'Grid'
	        	,height : 150
	        	,store : testpantallaStore
	        	,columns :
	        	[
	        	    {
	        	    	xtype : 'checkcolumn'
	        	    	,width : 30
	        	    }
                    ,<s:property value="item2" />
                    ,{
                        xtype : 'checkcolumn'
                        ,width : 30
                    }
	        	]
	        })
	    ]
	});
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
	//////////////////////
	////// cargador //////
	/*//////////////////*/
	/*//////////////////*/
	////// cargador //////
	//////////////////////

});
</script>
<div id="testpantallaDivPri"></div>