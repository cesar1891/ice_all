<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
    ////// variables //////
    //var panCanUrlCat        = '<s:url namespace="/flujocotizacion"  action="cargarCatalogos" />';
    var panCanUrlCat        = '<s:url namespace="/catalogos"        action="obtieneCatalogo" />';
    var panCanUrlCancelar   = '<s:url namespace="/cancelacion"      action="cancelacionUnica" />';
    var _pCan_urlValidaCanc = '<s:url namespace="/cancelacion" action="validaRazonCancelacion"        />';
    
    var panCanInputFecha;
    var panCanForm;
    
    // Obtenemos el contenido en formato JSON de la propiedad solicitada:
    var pancanInSmap1=<s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
    
    debug('pancanInSmap1',pancanInSmap1);
    ////// variables //////
    
    ////// funciones //////
    
    function comboMotivocambio(combo,nue,ant){ 
    	
    	debug('>comboMotivocambio:',combo,ant,nue);
    	
    	if(nue=='22'){
            debug(22);
            combo.setValue('22');
            panCanInputFecha.setValue(new Date());
            panCanInputFecha.setReadOnly(true);
    	}
    	else if(nue=='24'){
    		debug(24);
    		combo.setValue('24');
    		panCanInputFecha.setValue(pancanInSmap1.FEEFECTO);
            panCanInputFecha.setReadOnly(true);
    	}
    	else if(nue=='25'){
            debug(25);
            combo.setValue('25');
            panCanInputFecha.setValue(pancanInSmap1.FEEFECTO);
            panCanInputFecha.setReadOnly(false);
        }
        
        debug('<comboMotivocambio');
	}
	
    ////// funciones //////
Ext.onReady(function(){
    
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
        ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    panCanForm = Ext.create('Ext.form.Panel',{
        renderTo     : 'panCanDivPri'
    	,id          : 'panCanFormPri'
    	,url         : panCanUrlCancelar
    	,layout      :{
    	   type      : 'table'
    	   ,columns  : 2
    	}
    	,defaults    :{
    	   style : 'margin : 5px;'
    	}
    	,items       :[
    	    <s:property value="imap.itemsMarcocancelacionModelocandidata" />
    	]
    	,buttonAlign : 'center'
    	,buttons     :[
	       {
        	text     : 'Confirmar cancelaci&oacute;n'
        	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
        	,handler : 
        	    function(){
                    panCanForm.setLoading(true);
                    var boton=this;
                    var form=this.up().up();
                    if(form.isValid()){
                        debug(panCanForm.items.items[6].value);
                        Ext.Ajax.request({
                            url             : _pCan_urlValidaCanc
                            ,params       :{
                                     'smap1.cdunieco' : pancanInSmap1.CDUNIAGE
                                    ,'smap1.cdramo'   : pancanInSmap1.CDRAMO
                                    ,'smap1.estado'   : pancanInSmap1.ESTADO
                                    ,'smap1.nmpoliza' : pancanInSmap1.NMPOLIZA
                                    ,'smap1.cdrazon'  : panCanForm.items.items[6].value
                                }
                            ,success  : 
                                function(response){
                                    
                                    jsonData = Ext.decode(response.responseText);
                                    if(jsonData.success==true){
                                        form.submit({
                                            success :
                                                function(formu,action){
                                                    debug(action);
                                                    var json = Ext.decode(action.response.responseText);
                                                    debug(json);
                                                    if(json.success==true){
                                                        panCanForm.setLoading(false);
                                                        debug('ok');
                                                        Ext.Msg.show({
                                                            title    : 'Cancelaci&oacute;n exitosa'
                                                            ,msg     : 'Se ha cancelado la p&oacute;liza'
                                                            ,buttons : Ext.Msg.OK
                                                        });
                                                        boton.hide();
                                                    }
                                                    else{
                                                        panCanForm.setLoading(false);
                                                        Ext.Msg.show({
                                                            title    : 'Error'
                                                            ,msg     : 'Error al cancelar la p&oacute;liza'
                                                            ,icon    : Ext.Msg.ERROR
                                                            ,buttons : Ext.Msg.OK
                                                        });
                                                    }
                                                }
                                            ,failure :
                                                function(formu,action){
                                                	var json = Ext.decode(action.response.responseText);
                                                	
                                                    panCanForm.setLoading(false);
                                                    Ext.Msg.show({
                                                        title   : 'Error',
                                                        icon    : Ext.Msg.ERROR,
                                                        msg     : json.respuesta,
                                                        buttons : Ext.Msg.OK
                                                    });
                                                }
                                        });
                                    }
                                    else{
                                        mensajeError(jsonData.respuesta);
                                        panCanForm.setLoading(false);
                                    }
                                }
                            ,failure  :
                                function(){
                                    panCanForm.setLoading(false);
                                    errorComunicacion();
                                }
                        }); 
                    }
                    else{
                        Ext.Msg.show({
                            title   : 'Datos incompletos',
                            icon    : Ext.Msg.WARNING,
                            msg     : 'Favor de llenar los campos requeridos',
                            buttons : Ext.Msg.OK
                        });
                    }
               }
    	    }
    	]
    });
    var comboMotivoCanc = _fieldByName('smap1.pv_cdrazon_i',panCanForm);
//    comboMotivoCanc.width=400;
    debug('comboMotivoCanc:',comboMotivoCanc);
    panCanInputFecha = _fieldByName('smap1.pv_fecancel_i',panCanForm);;
    debug('panCanInputFecha:',panCanInputFecha);
    
    comboMotivoCanc.addListener('change',comboMotivocambio);
    comboMotivocambio(comboMotivoCanc,'22');
    
    ////// contenido //////
    
    ////// cargador //////
    ////// cargador //////
    
});
</script>
<div id="panCanDivPri"></div>