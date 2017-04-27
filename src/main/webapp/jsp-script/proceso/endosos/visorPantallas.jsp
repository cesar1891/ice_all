<%@ include file="/taglibs.jsp"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var vispanloaStoreInterno;
/*///////////////////*/
////// variables //////
///////////////////////

Ext.onReady(function()
{
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('vispanloaModeloInterno',
	{
		extend  : 'Ext.data.Model'
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
	vispanloaStoreInterno=Ext.create('Ext.data.Store',
	{
	    pageSize  : 10
	    ,autoLoad : true
	    ,model    : 'vispanloaModeloInterno'
	    ,proxy    :
	    {
	        enablePaging  : true
	        ,reader       : 'json'
	        ,type         : 'memory'
	        ,data         :
	        [
	            {
	            	test:'test'
	            }
	            ,{
                    test:'test'
                }
	            ,{
                    test:'test'
                }
	        ]
	    }
	});
    /*////////////////*/
    ////// stores //////
	////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	Ext.create('Ext.panel.Panel',
	{
		renderTo  : 'visPanDivPri'
		,defaults :
		{
			style : 'margin : 5px;'
		}
		,items    :
		[
            Ext.create('Ext.form.Panel',
            {
            	title     : 'Formulario'
            	,defaults :
            	{
            		style : 'margin : 5px;'
            	}
            	,layout   :
            	{
            		type     : 'table'
            		,columns : 3
            	}
            	,items    :
            	[
            	    <s:property value="item2" />
            	]
            })
            ,Ext.create('Ext.grid.Panel',
            {
            	title    : 'Grid'
            	,store   : vispanloaStoreInterno
            	,height  : 200
            	,columns :
            	[
            	    {
            	    	xtype      : 'checkcolumn'
            	    	,dataIndex : 'activo'
            	    	,width     : 30
            	    }
            	    ,<s:property value="item3" />
            	    ,{
            	    	xtype      : 'actioncolumn'
            	    	,icon      : '${ctx}/resources/fam3icons/icons/clock.png'
            	    	,width     : 30
            	    }
            	]
            })
		]
	});
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
	
});
</script>
<div id="visPanDivPri"></div>