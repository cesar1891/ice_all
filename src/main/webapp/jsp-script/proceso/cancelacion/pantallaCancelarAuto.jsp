<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var panCanAutoUrlCancelar = '<s:url namespace="/cancelacion" action="cancelacionAutoManual" />';
var panCanAutoStorePolizas;

//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var slist1JSON = <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />;
debug('slist1JSON='+ slist1JSON);

/*///////////////////*/
////// variables //////
///////////////////////

Ext.onReady(function()
{
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('PanCanAutoPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            <s:property value="imap.fieldsMarcocancelacionModelocandidata" />
        ]
    });
    /*/////////////////*/
    ////// modelos //////
	/////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
	panCanAutoStorePolizas = Ext.create('Ext.data.Store',
    {
        pageSize  : 10
        ,autoLoad : true
        ,model    : 'PanCanAutoPoliza'
        ,proxy    :
        {
            enablePaging  : true
            ,reader       : 'json'
            ,type         : 'memory'
            ,data         : slist1JSON
        }
    });
    /*////////////////*/
    ////// stores //////
	////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	Ext.create('Ext.grid.Panel',
	{
		renderTo     : 'panCanAutoDivPri'
		,id          : 'panCanAutoGridPri'
		,title       : 'Confirmar cancelaciones'
		,store       : panCanAutoStorePolizas
		,buttonAlign : 'center'
		,bbar        :
        {
            displayInfo : true
            ,store      : panCanAutoStorePolizas
            ,xtype      : 'pagingtoolbar'
        }
		,buttons     :
		[
		    {
		    	text     : 'Confirmar'
		    	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
		    	,handler : function()
		    	{
		    		var boton=this;
		    		var grid = this.up().up();
		    		grid.setLoading(true);
		    		Ext.Ajax.request(
		    		{
		    			url       : panCanAutoUrlCancelar
		    			,params: {
		    				'smap1.feproces': _feproces
		    			}
		    			,success  : function(response,opts)
                        {
		    				grid.setLoading(false);
		    				var json=Ext.decode(response.responseText);
		    				if(json.success==true)
		    				{
		    					debug('ok');
		    					Ext.Msg.show({
                                    title    : 'Cancelaci&oacute;n exitosa'
                                    ,msg     : 'Se han cancelado las p&oacute;lizas'
                                    ,buttons : Ext.Msg.OK
                                });
		    					boton.hide();
		    				}
		    				else
		    				{
		    					Ext.Msg.show({
                                    title:'Error',
                                    msg: 'Error al cancelar',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
		    				}
                        }
		    		    ,failure  : function()
		    		    {
		    		    	grid.setLoading(false);
		    		    	Ext.Msg.show({
                                title:'Error',
                                msg: 'Error de comunicaci&oacute;n',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            });
		    		    }
		    		});
		    	}
		    }
		]
		,columns     :
		[
		    <s:property value="imap.columnsMarcocancelacionModelocandidata" />
		]
	});
	
});
</script>
<div id="panCanAutoDivPri">
</div>