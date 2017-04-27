<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	var _CONTEXT = '${ctx}';
	var paramsEntrada          = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
	var situaciones          = <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />;
	var guardaTextoLibre = '<s:url namespace="/endosos" action="guardarEndosoTextoLibre"       />';
	
	var endTexLibFlujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
	
	var _p44_urlRecuperacionSimple = '<s:url namespace="/emision" action="recuperacionSimple" />';
	var _cdTipSupEndLibre = '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@ENDOSO_B_LIBRE.cdTipSup" />';
	
	debug('paramsEntrada  -->:',paramsEntrada);
	debug('endTexLibFlujo:',endTexLibFlujo);
	
	Ext.onReady(function() {
		
		var panelInicialPral = Ext.create('Ext.form.Panel', {
		    title: 'Texto Libre',
		    renderTo  : 'maindivText',
		    bodyPadding: 5,
			defaults 	:
			{
				style : 'margin:5px;'
			}
			,
		    items: [
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Minimo',			name	: 'feMin',				hidden		: true,
					format		: 'd/m/Y',		editable	: true,						value	: paramsEntrada.FEEFECTO
				},
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Maximo',			name	: 'feMax',				hidden		: true,
					format		: 'd/m/Y',		editable	: true,						value	: paramsEntrada.FEEFECTO
				},
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Inicio Endoso',	name	: 'feInival',			labelWidth	: 150,
					format		: 'd/m/Y',		editable	: true, 					value   : new Date(),			allowBlank	: false
				},
				{
					xtype: 'textareafield',
					fieldLabel: 'Texto',
					name:       'textoEndoso',
					width: 600,
					height: 200,
					maxLength: 10000,
					maxLengthText: 'No se permite mas de 10000 caracteres'
				}
	    	]
			,buttonAlign:'center'
			,buttons: [{
				text: 'Confirmar endoso'
				,icon:_CONTEXT+'/resources/fam3icons/icons/key.png'
				,buttonAlign : 'center',
				handler: function() {
					var formPanel = this.up().up();
					if (formPanel.form.isValid()) {
                        //1.- Verificamos la información de las fechas
                        var fechaMinValMod = Ext.Date.format(panelInicialPral.down('[name="feMin"]').getValue(),'d/m/Y');
						var fechaMaxValMod = Ext.Date.format(panelInicialPral.down('[name="feMax"]').getValue(),'d/m/Y');
//						var fechaEfectoMod = Ext.Date.format(panelInicialPral.down('[name="feIngreso"]').getValue(),'d/m/Y');

						var submitValues={};
							//paramsEntrada.FEEFECTO = Ext.Date.format(panelInicialPral.down('[name="feIngreso"]').getValue(),'d/m/Y');
//	        				paramsEntrada.FEPROREN = Ext.Date.format(panelInicialPral.down('[name="feFin"]').getValue(),'d/m/Y');
	        				paramsEntrada.FEINIVAL = Ext.Date.format(panelInicialPral.down('[name="feInival"]').getValue(),'d/m/Y');
	        				paramsEntrada.TEXTOEND = panelInicialPral.down('[name="textoEndoso"]').getValue();
	        				submitValues['smap1']= paramsEntrada;
	        				submitValues['slist1']= situaciones;
	        				
	        				if(!Ext.isEmpty(endTexLibFlujo))
	        				{
	        				    submitValues['flujo'] = endTexLibFlujo;
	        				}
	        				
	        				var panelMask = new Ext.LoadMask('maindivText', {msg:"Confirmando..."});
							panelMask.show();
							
	        				Ext.Ajax.request( {
	   						    url: guardaTextoLibre,
	   						    jsonData: Ext.encode(submitValues),
	   						    success:function(response,opts){
	   						    	 panelMask.hide();
	   						         var jsonResp = Ext.decode(response.responseText);
	   						         
	   						         var callbackRemesa = function()
	   						         {
	   						             marendNavegacion(2);
	   						         };
	   						         
	   						      	 mensajeCorrecto("Endoso",jsonResp.respuesta,function()
	   						      	 {
	   						      	     _generarRemesaClic(
	   						      	         true
	   						      	         ,paramsEntrada.CDUNIECO
	   						      	         ,paramsEntrada.CDRAMO
	   						      	         ,paramsEntrada.ESTADO
	   						      	         ,paramsEntrada.NMPOLIZA
	   						      	         ,callbackRemesa
	   						      	     );
	   						      	 });
	   						      	 
	   						    },
	   						    failure:function(response,opts){
	   						        panelMask.hide();
	   						        Ext.Msg.show({
	   						            title:'Error',
	   						            msg: 'Error de comunicaci&oacute;n',
	   						            buttons: Ext.Msg.OK,
	   						            icon: Ext.Msg.ERROR
	   						        });
	   						    }
	   						});
						
					}else {
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

		
		Ext.Ajax.request(
   		{
        url      : _p44_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : paramsEntrada.CDUNIECO
            ,'smap1.cdramo'       : paramsEntrada.CDRAMO	
            ,'smap1.estado'       : paramsEntrada.ESTADO
            ,'smap1.nmpoliza'     : paramsEntrada.NMPOLIZA
            ,'smap1.cdtipsup'     : _cdTipSupEndLibre
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                panelInicialPral.down('[name="feInival"]').setMinValue(json.smap1.FECHA_MINIMA);
                panelInicialPral.down('[name="feInival"]').setMaxValue(json.smap1.FECHA_MAXIMA);
                panelInicialPral.down('[name="feInival"]').setValue(json.smap1.FECHA_REFERENCIA);
                panelInicialPral.down('[name="feInival"]').setReadOnly(json.smap1.EDITABLE=='N');
                panelInicialPral.down('[name="feInival"]').isValid();
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
		
    });
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindivText" style="height:1000px;"></div>