Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	
var bd = Ext.getBody();
var detailEl;
var detailElCobertura;
var contentPanel = new Ext.Panel({
                			region:'center',
		                    layout:'card', 
		                    id:'panel-card-add-product',
		                    renderHidden : true,
        		            //deferredRender :false,
                		    activeItem: 0,
		                    items:[
        		            {
                		        contentEl:'center',
                        		id: 'card-Producto',
		                        autoScroll:true

        		            },{
                		        contentEl:'center4rolls',
                        		id: 'card-Rol',
		                        autoScroll:true
        		            },{
                		        contentEl:'centerIncisos',
                        		id: 'card-Inciso',
		                        autoScroll:true
        		            },{
                		        contentEl:'centerAtributos',
                        		id: 'card-AtributosVariables',
		                        autoScroll:true
        		            },{
                		        contentEl:'centerDatosFijos',
                        		id: 'card-DatosFijos',
			                    autoScroll:true
            	    	    },{
                		        contentEl:'centerConceptosGlobales',
                        		id: 'card-ConceptoGlobal',
			                    autoScroll:true
            	    	    },{
                		        contentEl:'centerCoberturas',
                        		id: 'card-Cobertura',
			                    autoScroll:true
            	    	    },{
                		        contentEl:'centerObjetos',
                        		id: 'card-Objeto',
			                    autoScroll:true
            	    	    },{
                		        contentEl:'centerSumaAsegurada',
                        		id: 'card-SumaAsegurada',
		                        autoScroll:true
        		            },{
		                    	contentEl:'centerReglasValidacion',
                        		id: 'card-ReglaValidacion',
		                        autoScroll:true
		                    },{
		                    	contentEl:'centerConceptosCobertura',
                        		id: 'card-ConceptoCobertura',
		                        autoScroll:true
		                    },{
                		        contentEl:'centerVariablesTemporalesProducto',
                        		id: 'card-VariableTemporalProducto',
		                        autoScroll:true
        		            
        		            },{
                		        contentEl:'centerPlanes',
                        		id: 'card-Planes',
		                        autoScroll:true

        		            }]
                    
                });
    var contentPanelCatalogos = new Ext.Panel({
                			region:'center',
		                    layout:'card', 
		                    id:'panel-card-catalogos',
		                    renderHidden : true,
        		            //deferredRender :false,
                		    activeItem: 0,
		                    items:[{
		                    	contentEl:'empty2',
                        		id: 'card-folder',
		                        autoScroll:true
		                    },{
		                    	contentEl:'tablaApoyoListaDeValores',
                        		id: 'card-ListaValores',
		                        autoScroll:true
		                    },{
		                    	contentEl:'tablaApoyoTabla5Claves',
                        		id: 'card-Tabla5Claves',
		                        autoScroll:true
		                    },{
		                    	contentEl:'libreriasAutorizaciones',
                        		id: 'card-Autorizaciones',
		                        autoScroll:true
		                    },{
		                    	contentEl:'libreriasCondiciones',
                        		id: 'card-Condiciones',
		                        autoScroll:true
		                    },{
                		        contentEl:'libreriasTarificacion',
                        		id: 'card-Tarificacion',
		                        autoScroll:true

        		            },{
		                    	contentEl:'libreriasValidaciones',
                        		id: 'card-Validaciones',
		                        autoScroll:true
		                    },{
		                    	contentEl:'libreriasVariables',
                        		id: 'card-Variables',
		                        autoScroll:true
		                    }]                    
                });            
    var treeLoaderYui=new Ext.tree.TreeLoader({dataUrl:'wizard/ArbolListaProductos.action',
    				timeout: 90000,
                    baseParams: {lib:'yui'} // custom http params
                    //root: 'nodo'
                })
//    var treeLoader= new Ext.tree.TreeLoader({
//    									clearOnLoad:true,
//    									preloadChildren:true,
//						               dataUrl:'wizard/ArbolListaProductos.action'
//					            });    
	var recargarArbol=-1;
	var idNodo=-1;
	var arregloDeProductos = new Array(0);
	var arregloDeIdentificadoresDeProductos = new Array(0);
	var arbolProductos= new  Ext.tree.TreePanel({
								id:'arbol-productos',
						        title: 'Productos',
						        xtype: 'treepanel',
						        region:'west',	
						        collapsible:true,
	       	                    minSize: 200,
    		   			        //maxSize: 200,
						        margins: '0 0 0 0',
			    			    width: 200,
						        autoScroll: true,
						        split: true,
						        rootVisible: false,
						        //lines: true,
						        //useArrows: true,
			    			    loader: treeLoaderYui, 
						        root:  new Ext.tree.AsyncTreeNode({
						                text: 'Ext JS', 
						                draggable:false, // disable root node dragging
						                id:'lista-prodcutos'
					            }),
						        rootVisible: false,
						        listeners: {
			    				        click: function(n) {
								            			    				        	
								            idNodo = n.attributes.codigoObjeto;
							            								            		
							            		 		var maximoCodigo= n.attributes.nivel;
								            			var params = "nivel="+n.attributes.nivel;
								            			var path= n.getPath("codigoObjeto");
								            			var codigosPath=new Array();
								            			codigosPath= path.split("/");
								            			var codeIndex=0;
								            			
								            			for(var i=0;i<codigosPath.length;i++){
								            				if(i>=2){
								            					params+="&codigos["+codeIndex+"].key="+codigosPath[i];
								            					codeIndex++;
								            				}
								            			}
								            			params+="&tipoObjeto="+n.attributes.tipoObjeto;
								            			
 								            			//console.log(params2);	
 								            			
														var conn = new Ext.data.Connection();
											            conn.request ({
																url: 'wizard/SubirCodigosASession.action',
																method: 'POST',
																successProperty : '@success',
																params : params,
											    	       		callback: function (options, success, response) {
							                       						if (Ext.util.JSON.decode(response.responseText).success == false) {
								                       							Ext.Msg.alert('Error', 'No se pudieron guardar los cambios');
								                       							//n.attributes.cls="file";
								                       							//n.attributes.leaf=true;
								                       							//n.attributes.children=null;
							                        							//return false;
							                       						} else {
							                       								//Ext.Msg.alert('Confirmaci�n', 'Se han guardado con �xito los cambios');
							                       								//alert("n.attributes.tipoObjeto=" + n.attributes.tipoObjeto + "\n n.attributes.nivel="+ n.attributes.nivel);
							                       								if('Producto'==n.attributes.tipoObjeto){
							                       									//Ext.getCmp('id-combo-clausulas-definicion').store.load();
							                       									//Ext.getCmp('id-combo-tipo-parametro-definicion').store.load();
																					//Ext.getCmp('id-combo-tipo-ramo-definicion').store.load();
																					//Ext.getCmp('id-combo-tipo-poliza-definicion').store.load();
																					//Ext.getCmp('id-combo-tipo-seguro-definicion').store.load();
									                          						recargaProductos();
									                          						
							                       								}
									                          					if('Inciso'==n.attributes.tipoObjeto){
									                          						Incisos();
									                          						Ext.getCmp('combo-codigo-inciso-del-producto').store.load();
								                          							
								                          							if(n.attributes.nivel=="2"){
									                          							Ext.getCmp('edita-descripcion-inciso').setDisabled(true);
									                          							Ext.getCmp('edita-forma-inciso').hide();									                          							
								                          								Ext.getCmp('forma-inciso').getForm().reset();
									                          							Ext.getCmp('combo-codigo-inciso-del-producto').setDisabled(false);
									                          							Ext.getCmp('hidden-form-tipo-inciso').show();
									                          							Ext.getCmp('btn-elimina-inciso').setVisible(false);
																	                	//Ext.getCmp('details-panel-forma-inciso').hide();
									                          							//Ext.getCmp('obligatorio-check').setValue(false);
																	                	//Ext.getCmp('obligatorio-check').setRawValue("N");
																	                	//Ext.getCmp('inserta-check').setValue(false);
																	                	//Ext.getCmp('inserta-check').setRawValue("N");
									                          						}else{
									                          							recargaInciso();									                          						
									                          						}
									                          						
									                          					}
									                          					/*if('Cobertura'==n.attributes.tipoObjeto){
									                          						//alert(n.attributes.nivel);
									                          						Ext.getCmp('combo-suma-asegurada-allowBlank').store.load();
									                          						Ext.getCmp('combo-condicion-allowBlank').store.load();
									                          						Ext.getCmp('combo-cobertura-valor').store.load();
									                          						if(n.attributes.nivel=="4"){
										                          						Ext.getCmp('cobertura-form').getForm().reset();
										                          						Ext.getCmp('id-hidden-form-coberturas').show();
															                           	Ext.getCmp('details-panel-forma-coberturas').hide();
															                           	Ext.getCmp('suma-asegurada-check').setValue(false);
															                           	Ext.getCmp('suma-asegurada-check').setRawValue("N");															                           	
																		    			Ext.getCmp('inserta-check-cobertura').setValue(false);
																		    			Ext.getCmp('inserta-check-cobertura').setRawValue("N");
																		    			Ext.getCmp('hide-form-condicion').hide();	   
																						Ext.getCmp('hide-form-suma').hide();				    			

									                          						}else{
									                          							recargaCobertura();									                          						
									                          						}
							                       								}*/
							                       								if('DatosFijos'==n.attributes.tipoObjeto){
									                          						Ext.getCmp('grid-DatosFijos').getStore().load();
							                       								}
							                       								if('AtributosVariables'==n.attributes.tipoObjeto){
							                       									Ext.getCmp('combo-condicion-atributos-variables-arbol').store.load();
							                       									Ext.getCmp('combo-lista-de-valores-atributos').store.load();
							                       									Ext.getCmp('combo-padre-atributos-variables-arbol').store.load();
							                       									Ext.getCmp('id-atributos-variables-form').getForm().reset();
							                       									
							                       									if(n.attributes.nivel=="8" || n.attributes.nivel=="9" ){							                       																		                       									
								                       									Ext.getCmp('id-atributos-variables-form').setTitle('Datos Variables Para Cobertura');							                       																                       									
							                       										//Ext.getCmp('hidden-form-attributos-coberturas').hide();
							                       										Ext.getCmp('id-atributos-variables-form').getForm().findField('hidden-codigo-expresion-session').
							                       											setValue('EXPRESION_ATRIBUTO_VARIABLE_RIESGO_COBERTURA');
							                       									}else{							                       																	                       										
							                       										Ext.getCmp('hidden-form-attributos-coberturas').show();							                       										
							                       									}
							                       									
							                       									if(n.attributes.nivel=="2" || n.attributes.nivel=="3" ){
							                       										Ext.getCmp('id-atributos-variables-form').setTitle('Datos Variables Para Productos ');   
							                       										Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador').hide();
							                       										Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton').hide();
							                       										Ext.getCmp('id-atributos-variables-form').getForm().findField('hidden-codigo-expresion-session').
							                       											setValue('EXPRESION_ATRIBUTO_VARIABLE_POLIZA');
							                       									}else{							                       										
							                       										Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador').show();
							                       										Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton').show();
							                       									}
							                       									
							                       									if(n.attributes.nivel=="4" || n.attributes.nivel=="5" ){
							                       										Ext.getCmp('id-atributos-variables-form').setTitle('Datos Variables Para Riesgo');
							                       										Ext.getCmp('id-atributos-variables-form').getForm().findField('hidden-codigo-expresion-session').
							                       											setValue('EXPRESION_ATRIBUTO_VARIABLE_RIESGO_COBERTURA');
							                       									}	
							                       									//alert('nivel 1: ' + n.attributes.nivel);
							                       									switch(n.attributes.nivel){
							                       																                       											
							                       										case 3:							                       											
							                       										case 5:
							                       										case 9:
								                       										recargaAtributosVariables();
							                       											//alert("3");
							                       											break;
							                       										case 2:							                       											
							                       										case 4:							                       											
							                       										case 6:	
							                       										case 8:
								                       										atributosvariableNuevo();
							                       											//alert("7");
							                       											break;							                       											
							                       									}							                       									
							                       								}
							                       								if('Rol'==n.attributes.tipoObjeto){
							                       									Ext.getCmp('id-catalogo-roles-en-rol').store.load();
							                       									Ext.getCmp('id-combo-obligatoriedad-rol').store.load();
							                       									Ext.getCmp('grid-lista-atributos-variable-rol').getStore().load();
							                       									//alert("nivel "+n.attributes.nivel);
							                       									//alert("codigoRol"+n.attributes.codigoObjeto);
							                       									Ext.getCmp('roles-form').getForm().reset();	
							                       									//Ext.getCmp('roles-form').getForm().load({url:'rol/LimpiarSesionAtributosVariablesRol.action',failure:function(f,a){
																							if(n.attributes.codigoObjeto!=null && n.attributes.codigoObjeto!=''){
																							var mStore = Ext.getCmp('grid-lista-atributos-variable-rol').getStore();
																							mStore.baseParams = mStore.baseParams || {};								
																							mStore.baseParams['codigoRol'] = n.attributes.codigoObjeto;	
																							mStore.reload({
																			                	  callback : function(r,options,success) {
																		                	  		//alert("callback");
																		                      		if (!success) {
																                				         //Ext.MessageBox.alert('No se encontraron registros');
																				                         mStore.removeAll();
														                							  }
																			        		      }
																				              });
																				              }
							                       									switch(n.attributes.nivel){							                       																                       											
							                       										case 3:
							                       										case 5:
							                       											recargaRol();
							                       											break;
							                       										case 2:							                       											
							                       										case 4:							                       											
							                       											rolNuevo();
							                       											break;							                       											
							                       									}
																					//}});						                       									
							                       								}
							                       								if('SumaAsegurada'==n.attributes.tipoObjeto){
							                       									//Ext.getCmp('grid-SumaAsegurada-producto').getStore().load();
							                       									Ext.getCmp('grid-SumaAsegurada-inciso').getStore().load();
							                       									Ext.getCmp('suma-asegurada-form').getForm().reset();
							                       									var codigoCoberturaSumaAsegurada=codigosPath[8];							                       										
							                       										//alert(codigoCoberturaSumaAsegurada);
							                       										Ext.getCmp('hidden-clave-cobertura-suma-asegurada').setValue(codigoCoberturaSumaAsegurada);
							                       										
							                       										var path3= n.getPath();								            											
								            											var codigosPath3=new Array();
																            			codigosPath3= path3.split("/");		
							                       									var descripcionCoberturaSumaAsegurada = Ext.getCmp('arbol-productos').getNodeById(codigosPath3[8]).attributes.text;
							                       									//alert(descripcionCoberturaSumaAsegurada);
							                       									Ext.getCmp('hidden-descripcion-cobertura-suma-asegurada').setValue(descripcionCoberturaSumaAsegurada);							                       									
							                       									//alert("nivel "+ n.attributes.nivel )
							                       									if(n.attributes.nivel=="6" || n.attributes.nivel=="7" ){
							                       										//Ext.getCmp('id-hidden-form-suma-asegurada-producto').hide();
							                       										Ext.getCmp('id-hidden-form-suma-asegurada-inciso').show();
							                       										//Ext.getCmp('id-boton-valor-defecto-suma-asegurada').show();
							                       									}/*else if(n.attributes.nivel=="2" || n.attributes.nivel=="3" ){
							                       										Ext.getCmp('id-hidden-form-suma-asegurada-producto').show();
							                       										Ext.getCmp('id-hidden-form-suma-asegurada-inciso').hide();
							                       										//Ext.getCmp('id-boton-valor-defecto-suma-asegurada').hide();							                       										
							                       									}*/	
							                       									//alert('nivel 2: ' + n.attributes.nivel);
							                       									switch(n.attributes.nivel){
							                       																                       											
							                       										case 3:
							                       										case 5:
								                       										//recargaAtributosVariables();
							                       											break;
							                       										case 2:							                       											
							                       										case 4:	
								                       										//atributosvariableNuevo();
							                       											break;							                       											
							                       									}							                       									
							                       								}
							                       								if('Objeto'==n.attributes.tipoObjeto){
							                       									Ext.getCmp('id-catalogo-tipo-objeto').store.load();
							                       									Ext.getCmp('grid-lista-atributos-variables-objeto').getStore().load();
							                       									//alert(n.attributes.nivel);
							                       									if (n.attributes.nivel==4) {
							                       										Ext.getCmp('btnEliminarObjetosRiesgo').setDisabled(true);
							                       									}
							                       									
							                       									if (n.attributes.nivel==5) {
								                       									Ext.getCmp('id-catalogo-tipo-objeto').store.load();							                       									
								                       									var path5= n.getPath();								            											
									            										var codigosPath5=new Array();
																	            		codigosPath5= path5.split("/");																            										                       									
																	            		var descripcionObjeto = Ext.getCmp('arbol-productos').getNodeById(codigosPath5[6]).attributes.text;							                       										
								                       									Ext.getCmp('id-catalogo-tipo-objeto').setValue(descripcionObjeto);
								                       									
								                       									var variable5=codigosPath[6];
								                       									Ext.getCmp('hidden-combo-objeto').setValue(variable5);
								                       									var params;
								                       									params = "codigoObjeto="+variable5;							                
								                       									var mStore = Ext.getCmp('grid-lista-atributos-variables-objeto').getStore();
																						mStore.baseParams = mStore.baseParams || {};
																						mStore.baseParams['codigoObjeto'] = variable5;
																						mStore.reload({
																			            	callback : function(r,options,success) {
																		                    	if (!success) {
																				                	mStore.removeAll();
														                						}
																			        		}
																				        });
																				        
																						Ext.getCmp('btnEliminarObjetosRiesgo').setDisabled(false);
							                       									}	                       									
							                       								}
							                       								if('VariableTemporalProducto'==n.attributes.tipoObjeto){
							                       									var conn2 = new Ext.data.Connection();
																					conn2.request ({
																				    		url:'wizard/RemueveCosasDeSessionEnLibrerias.action',
																							method: 'POST',
																							callback: function (options, success, response) {
										                       									Ext.getCmp('id-catalogo-variables-temporales-producto').store.load();										                       									
										                       									Ext.getCmp('grid-variables-temporales-producto').getStore().load();
																							}
																					});    
							                       								}
							                       								if('ReglaValidacion'==n.attributes.tipoObjeto){
							                       									Ext.getCmp('grid-ReglasValidacion').getStore().load();
							                       								}							                       								
							                       								if('ConceptoCobertura'==n.attributes.tipoObjeto){							                       	
							                       									if(n.attributes.nivel==8){
							                       										var variable=codigosPath[8];							                       										
							                       										Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').setValue(variable);
							                       										
							                       										var path2= n.getPath();								            											
								            											var codigosPath2=new Array();
																            			codigosPath2= path2.split("/");																            			
							                       									
																            			var descripcionCobertura2 = Ext.getCmp('arbol-productos').getNodeById(codigosPath2[8]).attributes.text;							                       										
							                       										Ext.getCmp('grid-conceptoCobertura').setTitle('Conceptos Por Cobertura - '+descripcionCobertura2);
							                       										
							                       									}else{
							                       										Ext.getCmp('grid-conceptoCobertura').setTitle('Conceptos Globales');
							                       										var variable='';							                       										
							                       										Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').setValue(variable);
							                       										
							                       									}
							                       									
							                       									Ext.getCmp('grid-conceptoCobertura').getStore().load();
							                       								}
							                       								if('Planes'==n.attributes.tipoObjeto){
							                       									//alert(n.attributes.nivel);
							                       									if (n.attributes.nivel==5) {							                       									
								                       									Ext.getCmp('codigo-form-plan-recarga-store-arbol').store.load();							                       									
								                       									var path4= n.getPath();								            											
									            										var codigosPath4=new Array();
																	            		codigosPath4= path4.split("/");																            										                       									
																	            		var descripcionPlan = Ext.getCmp('arbol-productos').getNodeById(codigosPath4[6]).attributes.text;							                       										
								                       									Ext.getCmp('codigo-form-plan-recarga-store-arbol').setValue(descripcionPlan);
								                       									//Asignar el valor de la clave elegida en el plan
								                       									Ext.getCmp('hidden-combo-plan').setValue(codigosPath[6]);
								                       									//alert(descripcionPlan);
								                       									var variable4=codigosPath[6];
								                       									//alert(variable4);
								                       									var params;
								                       									params = "codigoPlan="+variable4;							                
								                       									var mStore = Ext.getCmp('grid-planes-configuracion-coberturas').getStore();
																							mStore.baseParams = mStore.baseParams || {};								
																							mStore.baseParams['codigoPlan'] = variable4;	
																							mStore.reload({
																			                	  callback : function(r,options,success) {
																		                      		if (!success) {
																				                         mStore.removeAll();
														                							  }
																			        		      }
																				              });
							                       									}
							                       									switch(n.attributes.nivel){							                       														                       										
							                       										case 4:
							                       										planNuevo();
							                       										break;	
																					}
							                       									
							                       									
							                       								}
							                       								if('Cobertura'!=n.attributes.tipoObjeto){
                                       												Ext.getCmp('panel-card-add-product').layout.setActiveItem('card-'+n.attributes.tipoObjeto );
                                      											}
							                       								//Ext.getCmp('panel-card-add-product').layout.setActiveItem('card-'+n.attributes.tipoObjeto );																			
							                       								
							                       								//return true;
							    	                      				}
							       		               			},
												               	waitMsg: 'Espere por favor....'
											    		       	});	 
											
							            },
							            expandnode :function(n){
							            	var bandera=true;
											if(n!=null){
												idNodo = n.attributes.codigoObjeto;
												for (x in arregloDeProductos){
													//alert(x);
													if(idNodo==x){
															bandera=false;
													}
												}
												for(var i=0;i<arregloDeProductos.length;i++){
														//alert(arregloDeProductos[i]);
								            				if(idNodo==arregloDeProductos[i]){
																bandera=false;
															}
						            			}
											}else{
												bandera=false;
											}
											
							            	if(recargarArbol!=idNodo && n!=null && n.attributes.nivel=="1" && !n.isLeaf() && bandera){
										            	var maximoCodigo= n.attributes.nivel;
								            			var params = "nivel="+n.attributes.nivel;
								            			params += "&posicionProducto="+n.attributes.posicion;
								            			
								            			//var codigos= n.attributes.codigo;								            			
								            			var path= n.getPath("codigoObjeto");
								            			var codigosPath=new Array();
								            			codigosPath= path.split("/");
								            			var pathIds= n.getPath("id");
								            			var codigosPathIds=new Array();
								            			codigosPathIds= pathIds.split("/");
								            			//alert(codigosPathIds);
								            			
								            			var codeIndex=0;
								            			
								            			for(var i=0;i<codigosPath.length;i++){
								            				if(i>=2){
								            					params+="&codigos["+codeIndex+"].key="+codigosPath[i];
								            					codeIndex++;
								            				}
								            			}
								            			/*
								            			if(maximoCodigo==(codigosPath.length-2)){
									            			codigosPath.reverse();
									            			for(var i=0;i<(codigosPath.length-2);i++){
									            				var nodoPosicion = arbolProductos.getNodeById(codigosPathIds[i]); 
									            				params += "&&posicion["+i+"]="+nodoPosicion.attributes.posicion;

									            			}
								            			}*/
								            			
														var conn = new Ext.data.Connection();
											            conn.request ({
																url: 'wizard/LlenandoProductos.action',
																timeout: 90000,
																method: 'POST',
																successProperty : '@success',
																params : params,
											    	       		callback: function (options, success, response) {
									                   					arbolProductos.root.reload({timeout: 90000});									                          			
							       		               			},
												               	waitMsg: 'Espere por favor....'
											    		       	});
											    		       	
											    		       	recargarArbol=idNodo;
											    		       	//alert(n.attributes.id);
											    		       	//alert(recargarArbol);
	   		       												var longitud=arregloDeProductos.push(n.attributes.codigoObjeto);
	   		       												arregloDeIdentificadoresDeProductos.push(n.attributes.id);

	   		       												
	   		       												//alert(longitud);
	   		       												//alert(arregloDeProductos);	   		       												
	   		       												//alert(arregloDeProductos[0]);
							            	}   	      	
							            	
							            }
						        },
						        tbar:[{
 								           text:'Agregar Producto',
    	    							   tooltip:'Definir un nuevo Producto',					    			       
								           handler: agregaProducto = function (){	
								    		       	Ext.getCmp('reload-products').getForm().load({url:'definicion/LimpiarSessionClausulasProducto.action',failure:function(f,a){
									    		       	Ext.getCmp('grid-clausulas').getStore().load();
	                            						Ext.getCmp('grid-periodos').getStore().load();								    		       		
								    		       	}});
	            									
								  					Ext.getCmp('reload-products').getForm().reset();
								  					
                            						Ext.getCmp('hidden-codigo-switch-tipo').setValue("1");
				            
                            						Ext.getCmp('id-generar-producto-button').setDisabled(true);
								  					Ext.getCmp('codigo-ramo-producto').enable(); 
                            						Ext.getCmp('codigo-ramo-producto').setValue('');
                            						Ext.getCmp('codigo-ramo-producto').disable(true);
								  					//Ext.getCmp('hidden-bandera-editar').setValue("0");
								  					Ext.getCmp('switch-permiso-pagos-otras-monedas').setValue(false);
					                      			//Ext.getCmp('switch-permiso-pagos-otras-monedas').setRawValue("N");
					                      			Ext.getCmp('switch-reinstalacion-automatica').setValue(false);
				                          			//Ext.getCmp('switch-reinstalacion-automatica').setRawValue("N");
       			                           			Ext.getCmp('switch-distintas-polizas-por-asegurado').setValue(false);
				                           			//Ext.getCmp('switch-distintas-polizas-por-asegurado').setRawValue("N");
													Ext.getCmp('switch-rehabilitacion').setValue(false);
				                           			//Ext.getCmp('switch-rehabilitacion').setRawValue("N");
				                           			//Ext.getCmp('id4test-hidding').hide();    	                       		
       			                           			Ext.getCmp('switch-subincisos').setValue(false);       			                           			
				                           			//Ext.getCmp('switch-subincisos').setRawValue("N");    	        
				                           			Ext.getCmp('switch-renovable').setValue(false);
				                           			//Ext.getCmp('switch-renovable').setRawValue("N"); 
                				           			Ext.getCmp('switch-temporal').setValue(false);
                           							//Ext.getCmp('switch-temporal').setRawValue("N");
													Ext.getCmp('switch-vidaEntera').setValue(false);
				                           			//Ext.getCmp('switch-vidaEntera').setRawValue("N");
				                           			
		                           					Ext.getCmp('panel-card-add-product').layout.setActiveItem('card-Producto');
								  			
										   }
					        			}]
					    });
					   

			/*		    
			var recargar = function(arbolProductos){
                reload : function(callback){
                    this.collapse(false, false);
                    while(this.firstChild){
                        this.removeChild(this.firstChild);
                    }
                    this.childrenRendered = false;
                    this.loaded = false;
                    if(this.isHiddenRoot()){
                        this.expanded = false;
                    }
                    this.expand(false, false, callback);
                };
            };*/
var contadorActivate = true;					    
new Ext.Viewport({
		renderTo: document.body,
		layout: 'border',
		items: [/*{
			region: 'north',
			height: 115,
			maxSize: 150,
			margins: '0 0 0 0',
			bodyStyle: 'padding:5px;',
			split: true,
			frame:true,
			contentEl: 'north'
		},{
			region: 'south',
			height: 40,
			maxSize: 80,
			margins: '0 0 0 0',
			bodyStyle: 'padding:5px;',
			split: true,
			frame:true,
			contentEl: 'south'
		},*/{
			xtype: 'tabpanel',
			id:'application-layout-tabpanel',
			plain: true,
			height: 200,
			deferredRender:false,
			region: 'center',
			margins: '0 5 5 5',
			activeTab: 0,
			items: [{
				title: 'Productos',
				cls: 'inner-tab-custom', // custom styles in layout-browser.css
				layout: 'border',
				items: [
					arbolProductos,
                	contentPanel
                	]
			},{
				title: 'Cat\u00E1logos',
				layout: 'border',
				width: 200,
				minSize: 200,
				maxSize: 200,
       		    collapsible: true,
                margins:'0 5 0 5',
				items: [{   
       				    	region:'west',
					        title: 'Cat\u00E1logos',
					        xtype: 'treepanel',
					        width: 200,
					        minSize: 200,
							maxSize: 200,
		        		    collapsible: true,
			                margins:'0 5 0 5',
					        autoScroll: true,
					        split: true,
					        loader: new Ext.tree.TreeLoader(),
					        root: new Ext.tree.AsyncTreeNode({
						            expanded: true,
    				        children: [{
					                text: 'Librer\u00EDas',
					                tipoObjeto:'Librerias',
					                children:[{
					                		text: 'Variables Temporales',
						                	leaf: true,
						                	tipoObjeto:'Variables'
						            	},{
								            text: 'Validaciones',
							                leaf: true,
							                tipoObjeto:'Validaciones'					                
					                	},{
					                		text: 'Condiciones',
						                	leaf: true,
						                	tipoObjeto:'Condiciones'
						            	}/*,{
					                		text: 'Autorizaciones',
						                	leaf: true,
						                	tipoObjeto:'Autorizaciones'
						            	}*/,{
					                		text: 'Conceptos de Tarificaci\u00F3n',
						                	leaf: true,
						                	tipoObjeto:'Tarificacion'
						            }]
					            
					            },{
					                text: 'Tablas de Apoyo',
					                tipoObjeto:'folder',
					                children:[{
					                		text: 'Lista de Valores',
						                	leaf: true,
						                	tipoObjeto:'ListaValores'
						            	},{
								            text: 'Tabla Cinco Claves',
							                leaf: true,
							                tipoObjeto:'Tabla5Claves'					                
					                }]
    					        }]
					        }),
					        rootVisible: false,
					        listeners: {
					            click: function(n) {
					            		
						                if(n.attributes.tipoObjeto=='ListaValores'){
							            	Ext.getCmp('grid-ListaDeValores').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }
						                if(n.attributes.tipoObjeto=='Tabla5Claves'){
							            	Ext.getCmp('grid-Tabla5Claves').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }
						                
						                if(n.attributes.tipoObjeto=='Variables'){
							            	Ext.getCmp('grid-variables-temporales').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }
						                if(n.attributes.tipoObjeto=='Validaciones'){
							            	Ext.getCmp('grid-validaciones').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }
					            		if(n.attributes.tipoObjeto=='Condiciones'){
							            	Ext.getCmp('grid-condiciones').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }
						                if(n.attributes.tipoObjeto=='Autorizaciones'){
							            	Ext.getCmp('grid-autorizaciones').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }						                
						                if(n.attributes.tipoObjeto=='Tarificacion'){
							            	Ext.getCmp('grid-tarificacion').getStore().load({
                								callback : function(r,options,success) {
					                                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );                    					            
					                        	}
					              			});
						                }
						               
						                
										if(n.attributes.tipoObjeto=='Librerias'){
							               	Ext.getCmp('grid-lista1').getStore().load();
							            	Ext.getCmp('grid-lista2').getStore().load();
							            	Ext.getCmp('grid-lista3').getStore().load();
					    		        	Ext.getCmp('grid-lista4').getStore().load();
					            			Ext.getCmp('grid-lista5').getStore().load();
						                }
						                Ext.getCmp('panel-card-catalogos').layout.setActiveItem('card-'+n.attributes.tipoObjeto );
					            	
					            }
					        }
					    
					},contentPanelCatalogos]
			}]
			
		}]
	});
	
	function recargaProductos(){
		url4Product='definicion/EditarProducto.action'; 	
		var storeProducto = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url4Product
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'editarProducto',   
            	totalProperty: 'totalCount',
            	id: 'descripcion'
            	         	
	        	},[
	        		{name: 'codigoRamo',  type: 'string',  mapping:'codigoRamo'},
	        		{name: 'descripcion',  type: 'string',  mapping:'descripcion'},
	        		{name: 'descripcionRamo',  type: 'string',  mapping:'descripcionRamo'},
	        		{name: 'valorTipoProducto',  type: 'string',  mapping:'codigoTipoParametro'},
	        		{name: 'valorTipoRamo',  type: 'string',  mapping:'codigoTipoRamo'},
   	        		{name: 'valorTipoPoliza',  type: 'string',  mapping:'codigoTipoPoliza'},
	        		{name: 'valorTipoSeguro',  type: 'string',  mapping:'codigoTipoSeguro'},	        			        		
	        		{name: 'switchTipo',  type: 'string',  mapping:'switchTipo'},
	        		{name: 'switchPermisoPagosOtrasMonedas',  type: 'string',  mapping:'switchPermisoPagosOtrasMonedas'},
	        		{name: 'switchReinstalacionAutomatica',  type: 'string',  mapping:'switchReinstalacionAutomatica'},
   	        		{name: 'switchDistintasPolizasPorAsegurado',  type: 'string',  mapping:'switchDistintasPolizasPorAsegurado'},
	        		{name: 'switchSubincisos',  type: 'string',  mapping:'switchSubincisos'},	        		
	        		{name: 'mesesBeneficios',  type: 'string',  mapping:'mesesBeneficios'},
	        		{name: 'tipoCalculoPolizasTemporales',  type: 'string',  mapping:'tipoCalculoPolizasTemporales'},
	        		{name: 'switchRehabilitacion',  type: 'string',  mapping:'switchRehabilitacion'},
	        		{name: 'temporal',  type: 'string',  mapping:'temporal'},
	        		{name: 'renovable',  type: 'string',  mapping:'renovable'},
	        		{name: 'vidaEntera',  type: 'string',  mapping:'vidaEntera'},
	        		{name: 'switchCancelacion',  type: 'string',  mapping:'switchCancelacion'},
	        		{name: 'switchEndosos',  type: 'string',  mapping:'switchEndosos'}
	        		  
				]), 
			
			remoteSort: true
    	});
    	storeProducto.setDefaultSort('descripcion', 'desc');    	
    	storeProducto.on('load', function(){
    		
                            var rec = storeProducto.getAt(0); 
                            Ext.getCmp('reload-products').getForm().loadRecord(rec);
                            Ext.getCmp('grid-clausulas').getStore().load();
                            Ext.getCmp('grid-periodos').getStore().load();                           
                            Ext.getCmp('hidden-bandera-editar').setValue("1");
                            Ext.getCmp('codigo-ramo-producto').enable(); 
                            Ext.getCmp('id-generar-producto-button').setDisabled(false);
                            Ext.getCmp('hidden-codigo-ramo-editar').setValue(rec.get('codigoRamo'));
                           
                            if(rec.get('switchTipo') == '1'){                            	
    	                       		Ext.getCmp('tipo-dagnos').setValue(true);
   	                       			Ext.getCmp('hidden-codigo-switch-tipo').setValue("1");				            
	                        }else if(rec.get('switchTipo') == '2'){
                           			Ext.getCmp('tipo-gastos-medicos').setValue(true);
                       				Ext.getCmp('hidden-codigo-switch-tipo').setValue("2");				            
	                        }else if(rec.get('switchTipo') == '3'){
                           			Ext.getCmp('tipo-vida').setValue(true);
                           			Ext.getCmp('hidden-codigo-switch-tipo').setValue("3");				            
	                        }else{
	    	                    	Ext.getCmp('tipo-otros').setValue(true);
    	                   			Ext.getCmp('hidden-codigo-switch-tipo').setValue("4");				            
	                        }	                       
	                        if(rec.get('switchPermisoPagosOtrasMonedas') == 'S'){    	                       		
    	                       		Ext.getCmp('switch-permiso-pagos-otras-monedas').setValue(true);
    	                       		Ext.getCmp('switch-permiso-pagos-otras-monedas').setRawValue("S");
	                        }else{	                        	
                           			Ext.getCmp('switch-permiso-pagos-otras-monedas').setValue(false);                           			
	                      			//Ext.getCmp('switch-permiso-pagos-otras-monedas').setRawValue("N");	                      			
	                        }	                      
	                        if(rec.get('switchReinstalacionAutomatica') == 'S'){
    	                       		Ext.getCmp('switch-reinstalacion-automatica').setValue(true);
    	                       		Ext.getCmp('switch-reinstalacion-automatica').setRawValue("S");
    	                    }else{
                           			Ext.getCmp('switch-reinstalacion-automatica').setValue(false);
                          			//Ext.getCmp('switch-reinstalacion-automatica').setRawValue("N");
	                        }
	                        if(rec.get('switchEndosos') == 'S'){
    	                       		Ext.getCmp('switch-endosos').setValue(true);
    	                       		Ext.getCmp('switch-endosos').setRawValue("S");
    	                    }else{
                           			Ext.getCmp('switch-endosos').setValue(false);
	                        }
	                        if(rec.get('switchDistintasPolizasPorAsegurado') == 'S'){
    	                       		Ext.getCmp('switch-distintas-polizas-por-asegurado').setValue(true);
    	                       		Ext.getCmp('switch-distintas-polizas-por-asegurado').setRawValue("S");
	                        }else{
                           			Ext.getCmp('switch-distintas-polizas-por-asegurado').setValue(false);
                           			//Ext.getCmp('switch-distintas-polizas-por-asegurado').setRawValue("N");
	                        }	                        
	                       if(rec.get('switchRehabilitacion') == 'S'){
    	                       		Ext.getCmp('switch-rehabilitacion').setValue(true);    	                       		
    	                       		Ext.getCmp('switch-rehabilitacion').setRawValue("S");
    	                       		
    	                       		//Ext.getCmp('id4test-hidding').show();
    	                       		var recMesesBeneficios =rec.get('mesesBeneficios');
    	                       		Ext.getCmp('meses-beneficios').setValue(recMesesBeneficios);
    	                       		
    	                    		
	                        }else{
                           			Ext.getCmp('switch-rehabilitacion').setValue(false);
                           			//Ext.getCmp('switch-rehabilitacion').setRawValue("N");
                           			
                           			//Ext.getCmp('id4test-hidding').hide();
    	                       		Ext.getCmp('meses-beneficios').reset();    	                       		    	                       		
	                        }	                       
	                        if(rec.get('switchSubincisos') == 'S'){
    	                       		Ext.getCmp('switch-subincisos').setValue(true);
    	                       		Ext.getCmp('switch-subincisos').setRawValue("S");    	                       		
	                        }else{
                           			Ext.getCmp('switch-subincisos').setValue(false);
                           			//Ext.getCmp('switch-subincisos').setRawValue("N");    	                       		
	                        } 
	                        
	                        if(rec.get('temporal') == 'S'){
    	                       		Ext.getCmp('switch-temporal').setValue(true);
    	                       		Ext.getCmp('switch-temporal').setRawValue("S");    	                       		
	                        }else{
                           			Ext.getCmp('switch-temporal').setValue(false);
                           			//Ext.getCmp('switch-temporal').setRawValue("N");    	                       		
	                        }
	                       
	                        if(rec.get('renovable') == 'S'){
    	                       		Ext.getCmp('switch-renovable').setValue(true);
    	                       		Ext.getCmp('switch-renovable').setRawValue("S");    	                       		
	                        }else{
                           			Ext.getCmp('switch-renovable').setValue(false);
                           			//Ext.getCmp('switch-renovable').setRawValue("N");    	                       		
	                        }
	                        
	                        if(rec.get('vidaEntera') == 'S'){
    	                       		Ext.getCmp('switch-vidaEntera').setValue(true);
    	                       		Ext.getCmp('switch-vidaEntera').setRawValue("S");    	                       		
	                        }else{
                           			Ext.getCmp('switch-vidaEntera').setValue(false);
                           			//Ext.getCmp('switch-vidaEntera').setRawValue("N");    	                       		
	                        }	                        
	                        if(rec.get('switchCancelacion') == 'S'){
    	                       		Ext.getCmp('switch-cancelacion').setValue(true);    	                       		
    	                       		Ext.getCmp('switch-cancelacion').setRawValue("S");
    	                       		Ext.getCmp('id-forma-rehabilitacion').show();
    	                       		
    	                    		
	                        }else{
                           			Ext.getCmp('switch-cancelacion').setValue(false);
                           			//Ext.getCmp('switch-cancelacion').setRawValue("N");
                           			Ext.getCmp('id-forma-rehabilitacion').hide();    	                       		
	                        }
	                        Ext.getCmp('codigo-ramo-producto').disable();
                           });
                           storeProducto.load();
	}
	function recargaInciso(){
	
		urlEditarInciso='incisos/EditarInciso.action'; 	
		var storeInciso = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
					url: urlEditarInciso
        		}),
        		reader: new Ext.data.JsonReader({
	            	root:'incisos',   
    	        	totalProperty: 'totalCount',
        	    	id: 'editarInciso'            	         	
	        	},[
	        		{name: 'codigoRamo',  type: 'string',  mapping:'cdramo'},
	        		{name: 'descripcion',  type: 'string',  mapping:'dstipsit'},
	        		{name: 'clave',  type: 'string',  mapping:'cdtipsit'},
	        		{name: 'swsitdec',  type: 'string',  mapping:'swsitdec'},
	        		{name: 'inserta',  type: 'string',  mapping:'swinsert'},
   	        		{name: 'obligatorio',  type: 'string',  mapping:'swobliga'},
	        		{name: 'NumeroInciso',  type: 'string',  mapping:'nmsituac'},	        		
	        		{name: 'agrupador',  type: 'string',  mapping:'cdagrupa'},
   	        		{name: 'mode',  type: 'string',  mapping:'mode'},
	        		{name: 'ttiposit',  type: 'string',  mapping:'ttiposit'}        		            
				]),			
			remoteSort: true
    	});
    	storeInciso.setDefaultSort('cdRamo', 'desc');
    	
    	storeInciso.on('load', function(){
				var recInciso = storeInciso.getAt(0); 
                //Ext.getCmp('forma-inciso').getForm().loadRecord(recInciso); 
                /*if(recInciso.get('obligatorio')=="S"){
                	Ext.getCmp('obligatorio-check').setValue(true);
                	Ext.getCmp('obligatorio-check').setRawValue("S");
                }else{
                	Ext.getCmp('obligatorio-check').setValue(false);
                	Ext.getCmp('obligatorio-check').setRawValue("N");
                }
                if(recInciso.get('inserta')=="S"){
                	Ext.getCmp('inserta-check').setValue(true);
                	Ext.getCmp('inserta-check').setRawValue("S");
                }else{
                	Ext.getCmp('inserta-check').setValue(false);
                	Ext.getCmp('inserta-check').setRawValue("N");
                }*/
				Ext.getCmp('hidden-form-tipo-inciso').hide();
				Ext.getCmp('combo-codigo-inciso-del-producto').setDisabled(true);
                Ext.getCmp('combo-codigo-inciso-del-producto').hide();
                Ext.getCmp('agrega-riesgo-catalogo').hide();
                
                
                Ext.getCmp('btn-elimina-inciso').setVisible(true);
                
                //Ext.getCmp('edita-descripcion-inciso').setDisabled(false);
                
                Ext.getCmp('edita-descripcion-inciso').setVisible(true);
                Ext.getCmp('edita-descripcion-inciso').setWidth(300);
                Ext.getCmp('edita-forma-inciso').show();
                //Ext.getCmp('details-panel-forma-inciso').show();
                //var codigoInciso= recInciso.get("descripcion");
                Ext.getCmp('edita-descripcion-inciso').setValue(recInciso.get("descripcion"));
                Ext.getCmp('hidden-clave-combo-inciso').setValue(recInciso.get("clave"));
                //incisoDetailsUpdate(codigoInciso);
                //Ext.getCmp('combo-codigo-inciso-del-producto').setDisabled(true);                
        });
        storeInciso.load();
	}
	function recargaCobertura(){
		url='coberturas/CargaCoberturaAsociada.action' 	
		var storeCobertura = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaCoberturaAsociada',   
            	totalProperty: 'totalCount',
            	id: 'descripcionCobertura'
            	         	
	        	},[	       		
	        		{name: 'claveCobertura',  type: 'string',  mapping:'claveCobertura'},
	        		{name: 'descripcionCobertura',  type: 'string',  mapping:'descripcionCobertura'},
	        		{name: 'sumaAsegurada',  type: 'string',  mapping:'sumaAsegurada'},
	        		{name: 'descripcionCapital',  type: 'string',  mapping:'descripcionCapital'},
	        		{name: 'obligatorio',  type: 'string',  mapping:'obligatorio'},
	        		{name: 'inserta',  type: 'string',  mapping:'inserta'},
	        		{name: 'descripcionCondicion',  type: 'string',  mapping:'descripcionCondicion'}
	        		            
				]),
			
			remoteSort: true
    	});
    	storeCobertura.setDefaultSort('descripcionCobertura', 'desc');
    	
    	storeCobertura.on('load', function(){
    					   
                           var recCobertura = storeCobertura.getAt(0);                  
                           var swSumaAsegurada= recCobertura.get('sumaAsegurada');  
                           var swObligatorio = recCobertura.get('obligatorio');
                           var swInserta = recCobertura.get('inserta');
                           
                           Ext.getCmp('cobertura-form').getForm().loadRecord(recCobertura);
                           if(swSumaAsegurada == 'S'){
                           		Ext.getCmp('hide-form-suma').show();
                           		Ext.getCmp('suma-asegurada-check').setValue(true);
                           		Ext.getCmp('combo-suma-asegurada-allowBlank').setValue(recCobertura.get('descripcionCapital'));
                           }if(swSumaAsegurada == 'N'){
                           		Ext.getCmp('suma-asegurada-check').setValue(false);
                           		Ext.getCmp('hide-form-suma').hide();
                           }
                           if(swObligatorio == 'S'){
                           		Ext.getCmp('obligatorio-check-cobertura').setValue(true);
                           }if(swObligatorio == 'N'){
                           		Ext.getCmp('obligatorio-check-cobertura').setValue(false);
                           }
                           if(swInserta == 'S'){
                           		Ext.getCmp('hide-form-condicion').show();
                           		Ext.getCmp('inserta-check-cobertura').setValue(true);
                           		Ext.getCmp('combo-condicion-allowBlank').setValue(recCobertura.get('descripcionCondicion'));
                           }if(swInserta == 'N'){
	                           	Ext.getCmp('hide-form-condicion').show();
                           		Ext.getCmp('inserta-check-cobertura').setValue(false);
                           }                           
                           Ext.getCmp('id-hidden-form-coberturas').hide();
                           Ext.getCmp('details-panel-forma-coberturas').show();
                           var codigoCobertura= recCobertura.get("descripcionCobertura");
			               coberturaDetailsUpdate(codigoCobertura);
			               //alert(codigoCobertura);
                           });
                           storeCobertura.load();
		}
	 	function atributosvariableNuevo(){
	 		
	 		var connAV = new Ext.data.Connection();
            connAV.request ({url: 'atributosVariables/LimpiarSesionExpresion.action'});
           
            Ext.getCmp('hidden-valor-defecto-atributos-variables').setValue("");
            Ext.getCmp('hidden-radio-atributos-variables').setValue('A');
                	
	 		Ext.getCmp('obligatorio-check-atributos-variables').setValue(false);
			Ext.getCmp('obligatorio-check-atributos-variables').setRawValue("N");
			
			Ext.getCmp('emision-check-atributos-variables').setValue(false);
			Ext.getCmp('emision-check-atributos-variables').setRawValue("N");
			
			Ext.getCmp('endoso-check-atributos-variables').setValue(false);
			Ext.getCmp('endoso-check-atributos-variables').setRawValue("N");
			
			Ext.getCmp('retarificacion-check-atributos-variables').setValue(false);
			Ext.getCmp('retarificacion-check-atributos-variables').setRawValue("N");
		
			Ext.getCmp('cotizador-check-atributos-variables').setValue(false);
			Ext.getCmp('cotizador-check-atributos-variables').setRawValue("N");
			
			//****************
			Ext.getCmp('complementario-check-atributos-variables').setValue(false);
			Ext.getCmp('complementario-check-atributos-variables').setRawValue("N");
			Ext.getCmp('hidden-form-check-complementario').show();//mostrar layout
			
			Ext.getCmp('complementario-obligatorio-check-atributos-variables').setValue(false);
			Ext.getCmp('complementario-obligatorio-check-atributos-variables').setRawValue("N");
			
			Ext.getCmp('complementario-modificable-check-atributos-variables').setValue(false);
			Ext.getCmp('complementario-modificable-check-atributos-variables').setRawValue("N");
			
			Ext.getCmp('aparece-endoso-check-atributos-variables').setValue(false);
			Ext.getCmp('aparece-endoso-check-atributos-variables').setRawValue("N");
			Ext.getCmp('hidden-form-check-endoso-obligatorio').hide();
			Ext.getCmp('obligatorio-endoso-check-atributos-variables').reset();
			Ext.getCmp('endoso-check-atributos-variables').reset();
		
			Ext.getCmp('obligatorio-endoso-check-atributos-variables').setValue(false);
			Ext.getCmp('obligatorio-endoso-check-atributos-variables').setRawValue("N");
			
			Ext.getCmp('atributo-para-todos-check-atributos-variables').setValue(false);
			Ext.getCmp('atributo-para-todos-check-atributos-variables').setRawValue("N");
			//*****************
			//alert('disable 1 : ' + Ext.getCmp('id-descripcion-atributos-variables').disabled);
			Ext.getCmp('id-descripcion-atributos-variables').enable();
			//alert('disable 2 : ' + Ext.getCmp('id-descripcion-atributos-variables').disabled);
			Ext.getCmp('id-descripcion-atributos-variables').setDisabled(false);
			//alert('disable 3 : ' + Ext.getCmp('id-descripcion-atributos-variables').disabled);
			Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador').hide();
           	Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton').hide();  
			
        }
		function recargaAtributosVariables(){
		urlEditarAtributosVariables='atributosVariables/EditarAtributoVariable.action'; 	
		var storeAtributosVariables = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
					url: urlEditarAtributosVariables
        		}),
        		reader: new Ext.data.JsonReader({
	            	root:'listaAtributosVariables',   
    	        	totalProperty: 'totalCount',
        	    	id: 'editarAtributosVariables'            	         	
	        	},[
	        		{name: 'descripcion',  		 		type: 'string',  	mapping:'descripcion'},
	        		{name: 'formato',  			 		type: 'string',  	mapping:'cdFormato'},	        		
	        		{name: 'minimo',  			 		type: 'string',  	mapping:'minimo'},
	        		{name: 'maximo',  			 		type: 'string',  	mapping:'maximo'},
   	        		{name: 'obligatorio',  		 		type: 'string',  	mapping:'obligatorio'},
	        		{name: 'modificaEmision',  	 		type: 'string',  	mapping:'emision'},	        		
	        		{name: 'modificaEndoso',  	 		type: 'string',  	mapping:'endosos'},
   	        		{name: 'retarificacion',  	 		type: 'string',  	mapping:'retarificacion'},
	        		{name: 'despliegaCotizador', 		type: 'string',  	mapping:'cotizador'},	        		
	        		{name: 'codigoTabla',  		 		type: 'string',  	mapping:'cdTabla'},
   	        		{name: 'descripcionTabla',   		type: 'string',  	mapping:'dsTabla'},
	        		{name: 'codigoExpresion',  	 		type: 'string',  	mapping:'codigoExpresion'},
	        		{name: 'claveCampo', 				type: 'string',		mapping:'codigoAtributo'},
	        		//{name: 'padre', 		 	 		type: 'string',		mapping:'dsAtributoPadre'},
	        		{name: 'padre', 		 	 		type: 'string',		mapping:'codigoPadre'}, 
	        		{name: 'agrupador', 		 		type: 'string',		mapping:'agrupador'},
	        		{name: 'orden', 		 	 		type: 'string',		mapping:'orden'},
	        		{name: 'condicion', 		 		type: 'string',		mapping:'dsCondicion'},
	        		{name: 'datoComplementario', 		type: 'string',		mapping:'datoComplementario'},
	        		{name: 'obligatorioComplementario',	type: 'string',		mapping:'obligatorioComplementario'},
	        		{name: 'modificableComplementario',	type: 'string',		mapping:'modificableComplementario'},
	        		{name: 'apareceEndoso', 		 	type: 'string',		mapping:'apareceEndoso'},
	        		{name: 'obligatorioEndoso', 		type: 'string',		mapping:'obligatorioEndoso'},
	        		{name: 'atributoParaTodos',         type: 'string',     mapping:'atributoParaTodos'}
				]),			
			remoteSort: true
    	});
    	storeAtributosVariables.setDefaultSort('descripcion', 'desc');
    	
    	storeAtributosVariables.on('load', function(){    
				var recAtributosVariables = storeAtributosVariables.getAt(0); 
                Ext.getCmp('id-atributos-variables-form').getForm().loadRecord(recAtributosVariables);                 
                if(recAtributosVariables.get('formato') == 'A'){                           		
                	Ext.getCmp('function-radio-alfa-atributos-variables').setValue(true);
                	Ext.getCmp('hidden-radio-atributos-variables').setValue('A');
                	Ext.getCmp('id-hidden-text-maximo').enable();
           			Ext.getCmp('id-hidden-text-minimo').enable();

                }
                if(recAtributosVariables.get('formato') == 'N'){                           		
                	Ext.getCmp('function-radio-numeric-atributos-variables').setValue(true);
                	Ext.getCmp('hidden-radio-atributos-variables').setValue('N');
                	Ext.getCmp('id-hidden-text-maximo').enable();
           			Ext.getCmp('id-hidden-text-minimo').enable();
                }
                if(recAtributosVariables.get('formato') == 'P'){                           		
                	Ext.getCmp('function-radio-percent-atributos-variables').setValue(true);
                	Ext.getCmp('hidden-radio-atributos-variables').setValue('P');
                	Ext.getCmp('id-hidden-text-maximo').reset();
       				Ext.getCmp('id-hidden-text-minimo').reset();					            	
            		Ext.getCmp('id-hidden-text-maximo').disable();            						
	        		Ext.getCmp('id-hidden-text-minimo').disable();
                }
                if(recAtributosVariables.get('formato') == 'F'){                           		
                	Ext.getCmp('function-radio-date-atributos-variables').setValue(true);
                	Ext.getCmp('hidden-radio-atributos-variables').setValue('F');
                	Ext.getCmp('id-hidden-text-maximo').reset();
       				Ext.getCmp('id-hidden-text-minimo').reset();					            	
            		Ext.getCmp('id-hidden-text-maximo').disable();            						
	        		Ext.getCmp('id-hidden-text-minimo').disable();
				            		
                }
                if(recAtributosVariables.get('obligatorio')=="S"){
                	Ext.getCmp('obligatorio-check-atributos-variables').setValue(true);
                	Ext.getCmp('obligatorio-check-atributos-variables').setRawValue("S");
                	
                	Ext.getCmp('hidden-form-check-complementario').hide();
				    Ext.getCmp('hidden-form-check-complementario-obligatorio').hide();				            		
				    Ext.getCmp('complementario-check-atributos-variables').reset();
				    Ext.getCmp('complementario-obligatorio-check-atributos-variables').reset();
				    Ext.getCmp('complementario-modificable-check-atributos-variables').reset();
                }else{
                	Ext.getCmp('obligatorio-check-atributos-variables').setValue(false);
                	Ext.getCmp('obligatorio-check-atributos-variables').setRawValue("N");
                	Ext.getCmp('hidden-form-check-complementario').show();
                }
                if(recAtributosVariables.get('modificaEndoso')=="S"){
                	Ext.getCmp('endoso-check-atributos-variables').setValue(true);
                	Ext.getCmp('endoso-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('endoso-check-atributos-variables').setValue(false);
                	Ext.getCmp('endoso-check-atributos-variables').setRawValue("N");
                }               
                if(recAtributosVariables.get('modificaEmision')=="S"){
                	Ext.getCmp('emision-check-atributos-variables').setValue(true);
                	Ext.getCmp('emision-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('emision-check-atributos-variables').setValue(false);
                	Ext.getCmp('emision-check-atributos-variables').setRawValue("N");
                }
                if(recAtributosVariables.get('retarificacion')=="S"){
                	Ext.getCmp('retarificacion-check-atributos-variables').setValue(true);
                	Ext.getCmp('retarificacion-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('retarificacion-check-atributos-variables').setValue(false);
                	Ext.getCmp('retarificacion-check-atributos-variables').setRawValue("N");
                }
                if(recAtributosVariables.get('despliegaCotizador')=="S"){
                	Ext.getCmp('cotizador-check-atributos-variables').setValue(true);
                	Ext.getCmp('cotizador-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('cotizador-check-atributos-variables').setValue(false);
                	Ext.getCmp('cotizador-check-atributos-variables').setRawValue("N");
                }
                //*******************************
                
                if(recAtributosVariables.get('datoComplementario')=="S"){
                	Ext.getCmp('complementario-check-atributos-variables').setValue(true);
                	Ext.getCmp('complementario-check-atributos-variables').setRawValue("S");
                	Ext.getCmp('hidden-form-check-obligatorio').hide();
				    Ext.getCmp('obligatorio-check-atributos-variables').reset();
				    Ext.getCmp('hidden-form-check-complementario').show();
				    Ext.getCmp('hidden-form-check-complementario-obligatorio').show();
                }else{
                	Ext.getCmp('complementario-check-atributos-variables').setValue(false);
                	Ext.getCmp('complementario-check-atributos-variables').setRawValue("N");
                	Ext.getCmp('hidden-form-check-obligatorio').show();
				    Ext.getCmp('hidden-form-check-complementario-obligatorio').hide();
				    Ext.getCmp('complementario-obligatorio-check-atributos-variables').reset();
				    Ext.getCmp('complementario-modificable-check-atributos-variables').reset();
                }
                if(recAtributosVariables.get('obligatorioComplementario')=="S"){
                	Ext.getCmp('complementario-obligatorio-check-atributos-variables').setValue(true);
                	Ext.getCmp('complementario-obligatorio-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('complementario-obligatorio-check-atributos-variables').setValue(false);
                	Ext.getCmp('complementario-obligatorio-check-atributos-variables').setRawValue("N");
                }               
                if(recAtributosVariables.get('modificableComplementario')=="S"){
                	Ext.getCmp('complementario-modificable-check-atributos-variables').setValue(true);
                	Ext.getCmp('complementario-modificable-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('complementario-modificable-check-atributos-variables').setValue(false);
                	Ext.getCmp('complementario-modificable-check-atributos-variables').setRawValue("N");
                }
                if(recAtributosVariables.get('apareceEndoso')=="S"){
                	Ext.getCmp('aparece-endoso-check-atributos-variables').setValue(true);
                	Ext.getCmp('aparece-endoso-check-atributos-variables').setRawValue("S");
                	Ext.getCmp('hidden-form-check-endoso-obligatorio').show();
                }else{
                	Ext.getCmp('aparece-endoso-check-atributos-variables').setValue(false);
                	Ext.getCmp('aparece-endoso-check-atributos-variables').setRawValue("N");
                	Ext.getCmp('hidden-form-check-endoso-obligatorio').hide();
                	Ext.getCmp('obligatorio-endoso-check-atributos-variables').reset();
                	Ext.getCmp('endoso-check-atributos-variables').reset();
                }
                if(recAtributosVariables.get('obligatorioEndoso')=="S"){
                	Ext.getCmp('obligatorio-endoso-check-atributos-variables').setValue(true);
                	Ext.getCmp('obligatorio-endoso-check-atributos-variables').setRawValue("S");
                }else{
                	Ext.getCmp('obligatorio-endoso-check-atributos-variables').setValue(false);
                	Ext.getCmp('obligatorio-endoso-check-atributos-variables').setRawValue("N");
                }
                
                //console.log('recAtributosVariables:',recAtributosVariables);
                if(recAtributosVariables.get('atributoParaTodos')=="S"){
                    Ext.getCmp('atributo-para-todos-check-atributos-variables').setValue(true);
        			Ext.getCmp('atributo-para-todos-check-atributos-variables').setRawValue("S");
        		}else{
        		    Ext.getCmp('atributo-para-todos-check-atributos-variables').setValue(false);
        		    Ext.getCmp('atributo-para-todos-check-atributos-variables').setRawValue("N");
        		}
                
                //*******************************
                Ext.getCmp('hidden-clave-campo-atributos-variables').setValue(recAtributosVariables.get('claveCampo'));
                Ext.getCmp('hidden-descripcion-atributos-variables').setValue(recAtributosVariables.get('descripcion'));
                Ext.getCmp('hidden-formato-atributos-variables').setValue(recAtributosVariables.get('formato'));
                Ext.getCmp('hidden-lista-valores-atributo').setValue(recAtributosVariables.get('codigoTabla'));
                Ext.getCmp('combo-lista-de-valores-atributos').setValue(recAtributosVariables.get('descripcionTabla'));
                Ext.getCmp('hidden-codigo-expresion-atributos-variables').setValue(recAtributosVariables.get('codigoExpresion'));
                //alert(Ext.getCmp('id-descripcion-atributos-variables').getValue());
				//Ext.getCmp('id-descripcion-atributos-variables').disable();
                                
                //Ext.getCmp('hidden-form-tipo-inciso').hide();
                //Ext.getCmp('details-panel-forma-inciso').show();
                //var codigoAtributosVariables= recAtributosVariables.get("descripcion");
                //atributoVariableDetailsUpdate(codigoAtributosVariables);
                //Ext.getCmp('combo-codigo-inciso-del-producto').setDisabled(true);
				
				Ext.getCmp('combo-condicion-atributos-variables-arbol').setValue(recAtributosVariables.get('condicion'));
				Ext.getCmp('combo-padre-atributos-variables-arbol').setValue(recAtributosVariables.get('padre'));
				Ext.getCmp('id-hidden-text-orden').setValue(recAtributosVariables.get('orden'));
				Ext.getCmp('id-hidden-text-agrupador').setValue(recAtributosVariables.get('agrupador'));
				if(recAtributosVariables.get('padre') == ""){
					Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador').hide();
    		        Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton').hide(); 
    		        Ext.getCmp('forma-boton-vacia').hide();
    		        Ext.getCmp('forma-boton-catalogo-vacia').show();
    		        Ext.getCmp('combo-condicion-atributos-variables-arbol').reset();
    		        Ext.getCmp('id-hidden-text-orden').reset();
    		        Ext.getCmp('id-hidden-text-agrupador').reset();
				}else{ 
					Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador').show();
    		        Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton').show();
    		        Ext.getCmp('forma-boton-vacia').show();
    		        Ext.getCmp('forma-boton-catalogo-vacia').hide();
				}
				
        });
        storeAtributosVariables.load();
		}
		
		function rolNuevo(){
			
			Ext.getCmp('roles-form').getForm().load({url:'rol/LimpiarSesionAtributosVariablesRol.action',failure:function(f,a){
			              
			var mStore = Ext.getCmp('grid-lista-atributos-variable-rol').getStore();
								mStore.baseParams = mStore.baseParams || {};								
								mStore.baseParams['codigoRol'] = null;
								mStore.reload({
				                	  callback : function(r,options,success) {
				                	  		//alert("callback");
				                      		if (!success) {
		                				         //Ext.MessageBox.alert('No se encontraron registros');
						                         mStore.removeAll();
                							  }
				        		      }

					              });
				
			}});
	          					
			Ext.getCmp('mas-de-uno-check-rol').setValue(false);
			Ext.getCmp('domicilio-check-rol').setValue(false);
			
			Ext.getCmp('id-catalogo-roles-en-rol').store.load();
            Ext.getCmp('id-combo-obligatoriedad-rol').store.load(); 
		}
		function recargaRol(){
			
		urlEditarRol='rol/CargaRolAsociado.action'; 	
		var storeRol = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: urlEditarRol
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaRolAsociado',   
            	totalProperty: 'totalCount',
            	id: 'descripcionRol'
            	         	
	        	},[	{name: 'codigoRol',  type: 'string',  mapping:'codigoRol'},        		
	        		{name: 'descripcionRol',  type: 'string',  mapping:'descripcionRol'},
	        		{name: 'numeroMaximo',  type: 'string',  mapping:'numeroMaximo'},
	        		{name: 'codigoComposicion',  type: 'string',  mapping:'codigoComposicion'},
	        		{name: 'descripcionComposicion',  type: 'string',  mapping:'descripcionComposicion'},
	        		{name: 'domicilio',  type: 'string',  mapping:'switchDomicilio'}
	        		
	        		            
				]),
			
			remoteSort: true
    	});
    	storeRol.setDefaultSort('descripcionRol', 'desc');
    	
    	storeRol.on('load', function(){
    					   
                           var rec = storeRol.getAt(0);                  
                           var swMasDeUno= rec.get('numeroMaximo');  
                           var swDomicilio = rec.get('domicilio');
                           var valor = rec.get('codigoRol');
                          
                          //alert("codigoComposicion"+rec.get('codigoComposicion'));
                          //alert("domicilio "+swDomicilio);
                           Ext.getCmp('roles-form').getForm().loadRecord(rec);
                           if(swMasDeUno == '2'){                           		
                           		Ext.getCmp('mas-de-uno-check-rol').setValue(true);
                           		Ext.getCmp('mas-de-uno-check-rol').setRawValue("S");
                           }else{
                           		Ext.getCmp('mas-de-uno-check-rol').setValue(false);
                           		Ext.getCmp('mas-de-uno-check-rol').setRawValue("N");
                           }
                           if(swDomicilio == 'S'){
                           		Ext.getCmp('domicilio-check-rol').setValue(true);
                           		Ext.getCmp('domicilio-check-rol').setRawValue("S");
                           }else{
                           		Ext.getCmp('domicilio-check-rol').setValue(false);
                           		Ext.getCmp('domicilio-check-rol').setRawValue("N");
                           }                           
                           
                           //alert(valor);
                           var mStore = Ext.getCmp('grid-lista-atributos-variable-rol').getStore();
								mStore.baseParams = mStore.baseParams || {};								
								mStore.baseParams['codigoRol'] = valor;
								//mStore.baseParams['edita'] = '1';
								mStore.reload({									  
				                	  callback : function(r,options,success) {
				                	  		//alert("callback");
				                	  		mStore.baseParams['codigoRol'] = valor;
				                      		if (!success) {
		                				         Ext.MessageBox.alert('No se encontraron registros');
						                         mStore.removeAll();
                							  }
				        		      }

					              });
					              
					              //Ext.getCmp('hidden-combo-rol').setValue(valor);   
					        Ext.getCmp('hidden-composicion-rol').setValue(rec.get('codigoComposicion'));                              
                           	Ext.getCmp('id-catalogo-roles-en-rol').store.load();
        				    Ext.getCmp('id-combo-obligatoriedad-rol').store.load(); 
		
                           //Ext.getCmp('grid-lista').getStore().load();
                           });
                           storeRol.load();
		}
	    function incisoDetailsUpdate(text){			
			if(!detailEl){
    			var bdDetail = Ext.getCmp('details-panel-forma-inciso').body;
    			bdDetail.update('').setStyle('background','#fff');
    			detailEl = bdDetail.createChild(); //create default empty div
    		}
    		detailEl.hide().update("Descripcion :        "+text).slideIn('0.l', {stopFx:true,duration:.2});
	    }
	    
	    function coberturaDetailsUpdate(textCobertura){			
			if(!detailElCobertura){
    			var bdDetailCobertura = Ext.getCmp('details-panel-forma-coberturas').body;
    			bdDetailCobertura.update('').setStyle('background','#fff');
    			detailElCobertura = bdDetailCobertura.createChild(); //create default empty div
    		}
    		detailElCobertura.hide().update("Descripcion :        "+textCobertura).slideIn('0.l', {stopFx:true,duration:.2});
	    }
	    
	    function planNuevo(){
	    	Ext.getCmp('codigo-form-plan-recarga-store-arbol').setValue("");
	    	Ext.getCmp('codigo-form-plan-recarga-store-arbol').store.removeAll(); 
	    	Ext.getCmp('grid-planes-configuracion-coberturas').getStore().removeAll();
	    }
	    //Evitar que se reduzca el tama�o de la ventana cuando entremos al wizard:
	    window.parent.parent.setAbsoluteSizeHeight(550);
});

