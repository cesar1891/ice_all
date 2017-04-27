<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>

var _CONTEXT = '${ctx}';
	var paramsEntrada          = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
	
	paramsEntrada.FEPROREN_ORIG = paramsEntrada.FEPROREN; 
		
	var guarda_Vigencia_Poliza   = '<s:url namespace="/endosos"      action="guardarEndosoAmpliacionVigencia"       />';
	var url_PantallaPreview      = '<s:url namespace="/endosos"      action="includes/previewEndosos"                />';
	var _p30_urlViewDoc          = '<s:url namespace="/documentos"      action="descargaDocInline"      />';
    var _RUTA_DOCUMENTOS_TEMPORAL = '<s:text name="ruta.documentos.temporal" />';
	
	var endAmpVigFlujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
	
	debug('paramsEntrada  -->:',paramsEntrada);
	
	debug('endAmpVigFlujo:',endAmpVigFlujo);
	
	Ext.onReady(function() {
		
		Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
		
		var myMask = new Ext.LoadMask('maindivAmpVig', {msg:"Cargando..."});
		
		var panelInicialPral = Ext.create('Ext.form.Panel', {
		    title: 'Apliaci&oacute;n Vigencia',
		    renderTo  : 'maindivAmpVig',
		    bodyPadding: 5,
		    defaultType: 'textfield',
		    layout     :
			{
				type     : 'table'
				,columns : 2
			}
			,defaults 	:
			{
				style : 'margin:5px;'
			}
			,
		    items: [
		    	{	xtype		: 'datefield', 	fieldLabel	: 'Fecha Original',			name	: 'feEfecto',			hidden		: true,
					format		:  'm/d/Y', 	editable	: true,						value	: paramsEntrada.FEEFECTO
				},
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Minimo',			name	: 'feMin',				hidden		: true,
					format		: 'd/m/Y',		editable	: true,						value	: paramsEntrada.FEEFECTO
				},
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Maximo',			name	: 'feMax',				hidden		: true,
					format		: 'd/m/Y',		editable	: true,						value	: paramsEntrada.FEEFECTO
				},
				{
					xtype		: 'datefield',	fieldLabel	: 'Fecha Efecto',			name	: 'feIngreso',			labelWidth	: 150,
					format		: 'd/m/Y',		editable	: true,						value	: paramsEntrada.FEEFECTO,	allowBlank	: false
					,readOnly  	: true
					
				},
		    	{	xtype		: 'datefield',	fieldLabel	: 'Fecha Fin',				name	: 'feFin',				labelWidth	: 150,
					format		: 'd/m/Y',		editable	: true,						value	: paramsEntrada.FEPROREN, readOnly  	: true
				},
				{	xtype		: 'datefield',	fieldLabel	: 'Fecha Ampliaci&oacute;n Vigencia', name	: 'feAmpliacion', labelWidth	: 150,
					format		: 'd/m/Y',		editable	: true, 					value   : paramsEntrada.FEPROREN,			allowBlank	: false,
					colspan		:2, minValue : new Date(),
					minText:'La fecha de Ampliaci&oacute;n Vigencia debe ser mayor a Fecha Fin'
				}
	    	]
			,buttonAlign:'center'
			,buttons: [{
				text: 'Emitir'
				,icon:_CONTEXT+'/resources/fam3icons/icons/key.png'
				,buttonAlign : 'center',
				handler: function() {
					var myMask1 = _maskLocal();
					var formPanel = this.up().up();
					myMask.show();
					
					if (formPanel.form.isValid()) {
                        //1.- Verificamos la información de las fechas
						
						var feAmpli  = new Date(panelInicialPral.down('[name="feAmpliacion"]').getValue());
						var feProren = new Date(panelInicialPral.down('[name="feFin"]').getValue());
						paramsEntrada.FEINIVAL = Ext.Date.format(panelInicialPral.down('[name="feIngreso"]').getValue(),'d/m/Y');
						paramsEntrada.FEPROREN = Ext.Date.format(panelInicialPral.down('[name="feAmpliacion"]').getValue(),'d/m/Y');
						
						if(feAmpli > feProren){
						    //Exito
							var submitValues={};
							paramsEntrada.confirmar = 'no';
	        				submitValues['smap1']= paramsEntrada;
	        				
	        				if(!Ext.isEmpty(endAmpVigFlujo))
	        				{
	        				    submitValues['flujo'] = endAmpVigFlujo;
	        				}
	        				
	        				Ext.Ajax.request( {
	   						    url: guarda_Vigencia_Poliza,
	   						    jsonData:  Ext.encode(submitValues)
	   						    ,success:function(response,opts){
	   						    	 myMask.hide();
	   						    	 myMask1.close();
					                 var jsonResp1 = Ext.decode(response.responseText);
					                 Ext.create('Ext.window.Window',
											{
												title        : 'Tarifa final'
												,id          : 'tarifa'
												,autoScroll  : true
												,modal       : true
												,buttonAlign : 'center'
												,width       : 600
												,height      : 550
												,defaults    : { width: 650 }
												,closable    : false
												,autoScroll  : true
												,loader      :
													{
														url       : url_PantallaPreview
														,params   :
															{
												   				'smap4.nmpoliza'  : paramsEntrada.NMPOLIZA
					                                            ,'smap4.cdunieco' : paramsEntrada.CDUNIECO
					                                            ,'smap4.cdramo'   : paramsEntrada.CDRAMO
					                                            ,'smap4.estado'   : paramsEntrada.ESTADO
					                                            ,'smap4.nmsuplem' : jsonResp1.smap2.pv_nmsuplem_o
					                                            ,'smap4.nsuplogi' : jsonResp1.smap2.pv_nsuplogi_o
					                                        }
														,scripts  : true
														,autoLoad : true
												     }
												,buttons:[{
															text    : 'Confirmar endoso'
															,name    : 'endosoButton'
															,icon    : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
															,handler : 
																function (me){
																	var myMask1 = _maskLocal();
																	me.up('window').destroy();
																	myMask.show();
																	//submitValues['smap1.confirmar']='si' ;
																	paramsEntrada.confirmar = 'si';
                                                                    submitValues['smap1']= paramsEntrada;
																	
																	if(!Ext.isEmpty(endAmpVigFlujo))
                                                                    {
                                                                        submitValues['flujo'] = endAmpVigFlujo;
                                                                    }
                                                                    
																	Ext.Ajax.request(
																		{
												   						    url: guarda_Vigencia_Poliza,
												   						    jsonData: Ext.encode(submitValues)
												   						    ,success:function(response,opts){
												   						    	 //myMask.hide();
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
												   						        myMask.hide();
												   						        Ext.Msg.show({
												   						            title:'Error',
												   						            msg: 'Error de comunicaci&oacute;n',
												   						            buttons: Ext.Msg.OK,
												   						            icon: Ext.Msg.ERROR
												   						        });
												   						    }
												   						});
												   						myMask1.close();
												   				}
													           
														   },
														   {
															text    : 'Cancelar'
															,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
															,handler : function (me){
																			me.up('window').destroy();
																			marendNavegacion(2);
																			}
														 	},{
															text    : 'Documentos'
															,name    : 'documentoButton'
															,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
															,handler  :function(){
																 var numRand=Math.floor((Math.random()*100000)+1);
						                                         debug(numRand);
																 centrarVentanaInterna(Ext.create('Ext.window.Window', {
																 	title          : 'Vista previa'
															        ,width         : 700
															        ,height        : 500
															        ,collapsible   : true
															        ,titleCollapse : true
															        ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
															                         +'src="'+_p30_urlViewDoc+"?&path="+_RUTA_DOCUMENTOS_TEMPORAL+"&filename="+jsonResp1.smap2.pdfEndosoNom_o+"\">"
															                         +'</iframe>'
															        ,listeners     : {
															        	resize : function(win,width,height,opt){
															                debug(width,height);
															                $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
															        }
															      }}).show());
															}
															,hidden   : paramsEntrada.TIPOFLOT!= TipoFlotilla.Flotilla? false :true
					                                        } ]
										     }).show();
										     
	   						    },
	   						    failure:function(response,opts){
	   						        myMask.hide();
	   						        Ext.Msg.show({
	   						            title:'Error',
	   						            msg: 'Error de comunicaci&oacute;n',
	   						            buttons: Ext.Msg.OK,
	   						            icon: Ext.Msg.ERROR
	   						        });
	   						    }
	   						});
						    
						    
						}else{
							
							myMask.hide();
						    //Error
							centrarVentanaInterna(Ext.Msg.show({
		                        title:'Error',
		                        msg: 'La fecha de Ampliaci&oacute;n de Vigencia debe ser mayor a la Fecha Fin',
		                        buttons: Ext.Msg.OK,
		                        icon: Ext.Msg.WARNING
		                    }));
						}
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

		
    });
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindivAmpVig" style="height:1000px;"></div>