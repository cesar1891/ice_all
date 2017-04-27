<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	var _CONTEXT = '${ctx}';
	var asegAlterno                      = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
	var smap2Alterno                      = <s:property value="%{convertToJSON('smap2')}" escapeHtml="false" />;
	var guarda_Aseg_alterno              = '<s:url namespace="/endosos" action="guardarEndosoAseguradoAlterno" />';
	var _pAsegAlte_urlRecuperacionSimple = '<s:url namespace="/emision" action="recuperacionSimple"             />';
	
	var endAseAltflujo = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;
	
	debug('asegAlterno :',asegAlterno);
	debug('smap2Alterno :',smap2Alterno);
	debug('endAseAltflujo:',endAseAltflujo);
	
	Ext.onReady(function() {
		
		Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
		
		
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		
		var panelInicialPral = Ext.create('Ext.form.Panel', {
		    title: 'Alta Asegurado',
		    renderTo  : 'maindivHist',
		    bodyPadding: 5,
		    defaultType: 'textfield',
		    items: [
		    	{
					xtype		: 'datefield'
					,fieldLabel	: 'Fecha Inicio Endoso'
					,name		: 'feInival'
					,itemId     : '_pAsegAlte_fechaCmp'
					,labelWidth	: 150
					,format		: 'd/m/Y'
					,editable   : true
					,allowBlank	: false
					, value     : asegAlterno.FEEFECTO
				},
			 	{
			        fieldLabel	: 'Asegurado Alterno',
			        name		: 'asegAlt',
			        width		: 600,
			        labelWidth	: 150,
			        value 		: smap2Alterno.AsegAlterno,
			        allowBlank	: false
		    	}
	    	]
			,buttonAlign:'center'
			,buttons: [{
				text: 'Confirmar endoso'
				,icon:_CONTEXT+'/resources/fam3icons/icons/key.png'
				,buttonAlign : 'center',
				handler: function() {
					var formPanel = this.up().up();
					//panelInicialPral.setLoading(true);
					myMask.show();
					if (formPanel.form.isValid()) {
                        // Realizamos el proceso de guardado
						var submitValues={};
						asegAlterno.ASEG_ALTERNO = panelInicialPral.down('[name="asegAlt"]').getValue();
						asegAlterno.FEINIVAL  = Ext.Date.format(panelInicialPral.down('[name="feInival"]').getValue(),'d/m/Y');
        				submitValues['smap1']= asegAlterno;
        				
        				if(!Ext.isEmpty(endAseAltflujo))
        				{
        				    submitValues['flujo'] = endAseAltflujo;
        				}
        				
        				Ext.Ajax.request(
   						{
   						    url: guarda_Aseg_alterno,
   						    jsonData: Ext.encode(submitValues),
   						    success:function(response,opts){
   						    	myMask.hide();
   						    	panelInicialPral.setLoading(false);
   						         var jsonResp = Ext.decode(response.responseText);
   						         
   						         var callbackRemesa = function()
   						         {
   						             //usa codigo del marcoEndososAuto.jsp
                                     marendNavegacion(2);
   						         };
   						         
   						         mensajeCorrecto("Endoso",jsonResp.respuesta,function()
   						         {
   						             _generarRemesaClic(
   						                 true
   						                 ,asegAlterno.CDUNIECO
   						                 ,asegAlterno.CDRAMO
   						                 ,asegAlterno.ESTADO
   						                 ,asegAlterno.NMPOLIZA
   						                 ,callbackRemesa
   						             );
   						         });
   						    },
   						    failure:function(response,opts)
   						    {
   						    	myMask.hide();
   						        Ext.Msg.show({
   						            title:'Error',
   						            msg: 'Error de comunicaci&oacute;n',
   						            buttons: Ext.Msg.OK,
   						            icon: Ext.Msg.ERROR
   						        });
   						    }
   						});
        				
					}else {
						myMask.hide();
						Ext.Msg.show({
							title: 'Aviso',
							msg: 'Complete la informaci&oacute;n requerida',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.WARNING
						});
					}
				}
			}]
		});
		
		////// loaders //////
		
		Ext.Ajax.request(
	    {
	        url      : _pAsegAlte_urlRecuperacionSimple
	        ,params  :
	        {
	            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
	            ,'smap1.cdunieco'     : asegAlterno.CDUNIECO
	            ,'smap1.cdramo'       : asegAlterno.CDRAMO
	            ,'smap1.estado'       : asegAlterno.ESTADO
	            ,'smap1.nmpoliza'     : asegAlterno.NMPOLIZA
	            ,'smap1.cdtipsup'     : asegAlterno.cdtipsup
	        }
	        ,success : function(response)
	        {
	            var json = Ext.decode(response.responseText);
	            debug('### fechas:',json);
	            if(json.exito)
	            {
	                _fieldById('_pAsegAlte_fechaCmp').setMinValue(json.smap1.FECHA_MINIMA);
	                _fieldById('_pAsegAlte_fechaCmp').setMaxValue(json.smap1.FECHA_MAXIMA);
	                _fieldById('_pAsegAlte_fechaCmp').setValue(json.smap1.FECHA_REFERENCIA);
	                _fieldById('_pAsegAlte_fechaCmp').setReadOnly(json.smap1.EDITABLE=='N');
	                _fieldById('_pAsegAlte_fechaCmp').isValid();
	            }
	            else
	            {
	                mensajeError(json.respuesta);
	            }
	        }
	        ,failure : function()
	        {
	            errorComunicacion();
	        }
	    });
		////// loaders //////
            	
    });
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindivHist" style="height:1000px;"></div>