<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var panrehuniUrlDetCanc = '<s:url namespace="/cancelacion"    action="obtenerDetalleCancelacion"/>';
var panrehuniUrlRehab   = '<s:url namespace="/rehabilitacion" action="rehabilitacionUnica"/>';

// Obtenemos el contenido en formato JSON de la propiedad solicitada:
var panrehuniInSmap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('panrehuniInSmap1',panrehuniInSmap1);
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/

/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function(){
    
    /////////////////////
    ////// modelos //////
    /*/////////////////*
    Ext.define('PanRehUniDetalleCancelacion',
    {
    	extend  : 'Ext.data.Model'
    	,fields :
    	[
    	]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    
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
    Ext.create('Ext.form.Panel',
    {
    	id        : 'panrehuniFormPri'
    	,renderTo : 'panrehuniDivPri'
    	,url      : panrehuniUrlRehab
    	,border   : 0
    	,layout   :
    	{
    		type     : 'table'
    		,columns : 2
    	}
    	,defaults :
    	{
    		style : 'margin : 5px;'
    	}
    	,items    :
    	[
    	    <s:property value="imap.itemsForm" />
        ]
    	,buttonAlign : 'center'
    	,buttons     :
    	[
    	    {
    	    	text     : 'Rehabilitar'
    	    	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
    	    	,handler : function() 
    	    	{
    	    		var boton=this;
                    var form=this.up().up();
                    if(form.isValid())
                    {
                        form.setLoading(true);
                        form.submit(
                        {
                            success : function(formu,action)
                            {
                                form.setLoading(false);
                                debug(action);
                                var json = Ext.decode(action.response.responseText);
                                debug(json);
                                if(json.exito)
                                {
                                    debug('ok');
                                    Ext.Msg.show({
                                        title    : 'Rehabilitaci&oacute;n exitosa'
                                        ,msg     : 'Se ha rehabilitado la p&oacute;liza'
                                        ,buttons : Ext.Msg.OK
                                    });
                                    boton.hide();
                                }
                                else
                                {
                                    mensajeError(json.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                form.setLoading(false);
                                Ext.Msg.show(
                                {
                                    title   : 'Error',
                                    icon    : Ext.Msg.ERROR,
                                    msg     : 'Error de comunicaci&oacute;n',
                                    buttons : Ext.Msg.OK
                                });
                            }
                        });
                    }
                    else
                    {
                        Ext.Msg.show(
                        {
                            title   : 'Datos incompletos',
                            icon    : Ext.Msg.WARNING,
                            msg     : 'Favor de llenar los campos requeridos',
                            buttons : Ext.Msg.OK
                        });
                    }
    	    	}
    	    }
    	]
    	,listeners :
        {
            afterrender : function(me){heredarPanel(me);}
        }
    });
    debug('fecha:',Ext.ComponentQuery.query('[name="smap1.pv_fereinst_i"]'));
    debug('panrehuniInSmap1.pv_fereinst_i:',panrehuniInSmap1['FECANCEL']);
    debug('new:',Ext.Date.parse(panrehuniInSmap1['FECANCEL'],"d/m/Y"));
    Ext.ComponentQuery.query('[name="smap1.pv_fereinst_i"]')[Ext.ComponentQuery.query('[name="smap1.pv_fereinst_i"]').length-1].minValue=Ext.Date.parse(panrehuniInSmap1['FECANCEL'],"d/m/Y");
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////

    //////////////////////
    ////// cargador //////
    /*//////////////////*
    Ext.define('LoaderDetalleCancelacion',
    {
        extend :'PanRehUniDetalleCancelacion'
        ,proxy :
        {
            extraParams :
            {
            	'smap1.pv_cdunieco_i'  : panRehInput['cdunieco']
                ,'smap1.pv_cdramo_i'   : panRehInput['cdramo']
                ,'smap1.pv_estado_i'   : panRehInput['estado']
                ,'smap1.pv_nmpoliza_i' : panRehInput['nmpoliza']
            }
            ,type   : 'ajax'
            ,url    : panrehuniUrlDetCanc
            ,reader :
            {
            	type : 'json'
            }
        }
    });

    var loaderDetalleCancelacion=Ext.ModelManager.getModel('LoaderDetalleCancelacion');
    loaderDetalleCancelacion.load(123,
    {
        success  : function(resp)
        {
            debug(resp);
            Ext.getCmp('panrehuniFormDetaCanc').loadRecord(resp);
        }
        ,failure : function()
        {
            Ext.Msg.show(
            {
                title    : 'Error'
                ,icon    : Ext.Msg.ERROR
                ,msg     : 'Error al cargar'
                ,buttons : Ext.Msg.OK
            });
        }
    });
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
});
    
</script>
<div id="panrehuniDivPri"></div>