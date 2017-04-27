<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	var _CONTEXT = '${ctx}';
	var paramsEntrada          = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
	var guarda_RehabilitacionDespago = '<s:url namespace="/endosos" action="guardarEndosoRehabilitacionDespago"       />';
	
	var _endRehDesp_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
	
	var _URL_CONSULTA_RECIBOS_DESPAGADOS = '<s:url namespace="/endosos" action="obtieneRecibosDespagados" />';
	
	debug('paramsEntrada  -->:',paramsEntrada);
	debug('_endRehDesp_flujo:',_endRehDesp_flujo);
	
	Ext.onReady(function() {
		
		
		Ext.define('gridRecibosModel',{
			extend : 'Ext.data.Model',
			fields : ['NMRECIBO','NMSUPLEM','NMIMPRES','CDDEVCIA','CDGESTOR','PTIMPORT','FEEMISIO','FEINICIO','FEFINAL','FEESTADO']
		});
		
		var recibosStore = Ext.create('Ext.data.Store',{
			pageSize : 20,
	        model   : 'gridRecibosModel',
	        proxy   :
				{
		            type: 'ajax',
		            url : _URL_CONSULTA_RECIBOS_DESPAGADOS,
		            reader: {
		                type: 'json',
		                root: 'slist1'
		            }
	        	}
	    });
	    
	    var gridRecibos = Ext.create('Ext.grid.Panel',{
	    	title : 'Recibos Pagados, Seleccione el Recibo a Despagar.',
	    	height : 250,
	    	selModel: { selType: 'checkboxmodel', mode: 'SINGLE'},
	    	store : recibosStore,
	    	columns :[
	    	  { text: 'NMRECIBO'            , dataIndex : 'NMRECIBO', hidden: true},
	    	  { text: 'Suplemento'          , dataIndex : 'NMSUPLEM', hidden: true},
	    	  { text: 'No. Recibo'          , dataIndex : 'NMIMPRES'},
	    	  { text: 'Tipo Endoso'         , dataIndex : 'CDDEVCIA'},
	    	  { text: 'N&uacute;mero Endoso', dataIndex : 'CDGESTOR'},
	    	  { text: 'Importe'             , dataIndex : 'PTIMPORT'},
	    	  { text: 'Emisi&oacute;n'      , dataIndex : 'FEEMISIO'},
	    	  { text: 'Inicio Vig.'         , dataIndex : 'FEINICIO'},
	    	  { text: 'Fin Vig.'            , dataIndex : 'FEFINAL'},
	    	  { text: 'FEESTADO'            , dataIndex : 'FEESTADO', hidden: true}
			]
    	});
		
		var panelInicialPral = Ext.create('Ext.form.Panel', {
		    renderTo  : 'mainDivDespago',
		    bodyPadding: 5,
		    defaultType: 'textfield',
		    
			defaults 	:
			{
				style : 'margin:5px;'
			}
			,
		    items: [
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Inicio Endoso',	name	: 'feInival',			labelWidth	: 150,
					format		: 'd/m/Y', value: paramsEntrada.FEINIVAL, readOnly: true
				},gridRecibos
	    	]
			,buttonAlign:'center'
			,buttons: [{
				text: 'Confirmar endoso'
				,icon:_CONTEXT+'/resources/fam3icons/icons/key.png'
				,buttonAlign : 'center',
				handler: function(btn, e) {
					
					var model =  gridRecibos.getSelectionModel();
	            	if(model.hasSelection()){
	            		
		            	Ext.Msg.show({
	                        title: 'Confirmar acci&oacute;n',
	                        msg   : '&iquest;Esta seguro que desea rehabilitar este recibo?',
	                        buttons: Ext.Msg.YESNO,
	                        fn: function(buttonId, text, opt) {
	                            if(buttonId == 'yes') {
	                                var record = model.getLastSelected();
				            		var submitValues={};
				        				paramsEntrada.FEINIVAL = Ext.Date.format(panelInicialPral.down('[name="feInival"]').getValue(),'d/m/Y');
				        				paramsEntrada.NMRECIBO = record.get('NMRECIBO');
				        				paramsEntrada.NMIMPRES = record.get('NMIMPRES');
				        				submitValues['smap1']= paramsEntrada;
				        				
				        				if(!Ext.isEmpty(_endRehDesp_flujo))
				        				{
				        				    submitValues['flujo'] = _endRehDesp_flujo;
				        				}
				        				
				        				var panelMask = new Ext.LoadMask('mainDivDespago', {msg:"Confirmando..."});
										panelMask.show();
					
				        				Ext.Ajax.request( {
				   						    url: guarda_RehabilitacionDespago,
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
		                         }
		                        },
	                        animateTarget: btn,
	                        icon: Ext.Msg.QUESTION
	                    });
	            		
	            	}else{
	            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
	            	}
				}
			}]
		});
		
		recibosStore.load({
		            params : {
		                'smap1.CDUNIECO' : paramsEntrada.CDUNIECO,
		                'smap1.CDRAMO' : paramsEntrada.CDRAMO,
		                'smap1.ESTADO' : paramsEntrada.ESTADO,
		                'smap1.NMPOLIZA' : paramsEntrada.NMPOLIZA
		            }
		 });
            

    });
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="mainDivDespago" style="height:1000px;"></div>