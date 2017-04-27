ExpresionesVentanaVariablesTemporales = function(storeVariables,rec,row) {
var codigoExpresion;

if(row!=null){
	
    var record = storeVariables.getAt(row);
    codigoExpresion= record.get('codigoExpresion');
    
    
	storeVariables.on('load', function(){
                           Ext.getCmp('cabecera-variables-form').getForm().loadRecord(record);
                          
                           });
                           storeVariables.load();
		
		/************************ 
		*carga expresion
		*************************/
		
		//alert("cod. expresion"+codigoExpresion);
		
		url='librerias/CargaExpresionReglaNegocio.action?codigoExpresion='+codigoExpresion; 	
		var storeExpresion = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaExpresionesReglaNegocio',   
            	totalProperty: 'totalCount',
            	id: 'descripcion'
            	         	
	        	},[
	        		{name: 'descripcion',  type: 'string',  mapping:'otExpresion'},
	        		{name: 'switchRecalcular',  type: 'string',  mapping:'switchRecalcular'},
	        		{name: 'codigoExpresion',  type: 'string',  mapping:'codigoExpresion'}
	        		            
				]),
			
			remoteSort: true
    	});
    	storeExpresion.setDefaultSort('descripcion', 'desc');
    	
    	storeExpresion.on('load', function(){
                           Ext.getCmp('nombre-cabecera').setDisabled(true);
                           var rec = storeExpresion.getAt(0);                  
                           var swRecalcular= rec.get('switchRecalcular');
                           Ext.getCmp('top-variables-temporales').getForm().loadRecord(rec);
                           if(swRecalcular == 'S'){
                           		Ext.getCmp('switch-recalcular-expresion').setValue(true);
                           }if(swRecalcular == 'N'){
                           		Ext.getCmp('switch-recalcular-expresion').setValue(false);
                           }
                           dsComboV.load();
                           
                           });
                           storeExpresion.load();
                           
		//*************************************************************************************
}
/****************************************
ventanota
****************************************/
	var record4editing;
	var detailEl;
	var win;
    var dsComboV = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: '<s:url value="/expresiones/ComboVariables.action"/>'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaComboVariables',
            id: 'comboVariables'
	        },[
           {name: 'codigoExpresion', type: 'float',mapping:'codigoExpresion'},
           {name: 'nombre', type: 'string',mapping:'codigoVariable'},
           {name: 'tabla', type: 'string',mapping:'descripcionTabla'},
           {name: 'columna', type: 'string',mapping:'descripcionColumna'}
           
        ]),
		remoteSort: true
    });
    dsComboV.load(); 

 	var afuera;
 	var temporal=-1;
	var comboVariables =new Ext.form.ComboBox({
    	                    tpl: '<tpl for="."><div ext:qtip="{nombre}. {tabla}" class="x-combo-list-item">{nombre}</div></tpl>',
							store: dsComboV,
						    displayField:'nombre',
						    valueField: 'nombre',
					    	typeAhead: true,
					    	labelAlign: 'top',
					    	heigth:'50',
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'<s:text name="productos.valida.expresion.variables"/>',
					    	selectOnFocus:true,
					    	fieldLabel:  '<s:text name="productos.config.expresion.variables"/>',
					    	name:"claveSeleccionada",
				    		selectFirst : function() {
									this.focusAndSelect(this.store.data.first());
							},
					    	focusAndSelect : function(record) {
									var index = typeof record === 'number' ? record : this.store.indexOf(record);
									this.select(index, this.isExpanded());
									this.onSelect(this.store.getAt(record), index, this.isExpanded());
							},
					    	onSelect : function(record, index, skipCollapse){
									if(this.fireEvent('beforeselect', this, record, index) !== false){
											this.setValue(record.data[this.valueField || this.displayField]);
											if( !skipCollapse ) {
												this.collapse();
											}
											this.lastSelectedIndex = index + 1;
											this.fireEvent('select', this, record, index);
									}
						            afuera=index; 
									Ext.getCmp('editar-variable').on('click',function(){
				                        if(afuera!=temporal){
							            temporal=afuera;
					                        if(comboVariables.getValue()!=null && comboVariables.getValue()!="" 
        	       							    		&& comboVariables.getValue()!="undefined"){
				                        		//alert("index="+index);
						                        NuevaVariableExpresiones(dsComboV,index);  
       				            	       	}else{
		    							    	Ext.MessageBox.alert('Error', 'No ha seleccionado una variable');
		    							    }                                                                          
		    							}    
       					         	});	
									temporal=-1;                                                                           
							}
							/*,
					    	beforeselect:function(combo,rec,index){
					    			alert(index);
					    			alert(this.getValue());
               				    	Ext.getCmp('editar-variable').on('click',function(){
				                        window.show();
       			            	       	Ext.getCmp('variable-form-panel').getForm().loadRecord(rec);                                                                            
       					         	});					    	
					    	}*/
			});
    var descripcion= new Ext.form.TextArea({
	                        name: "descripcion",
	                        id:'descripcion-expresion',
	                        fieldLabel: '<s:text name="productos.config.expresion.descripcion"/>',
	                        width: '250',
	                        labelSeparator:'',
	                        //allowBlank: true,
	                        maxLength:'2000'
	        });
	var recalcular = new Ext.form.Checkbox({	
	  		                fieldLabel: 'Last Name',
	  		                id:'switch-recalcular-expresion',
	           		        hideLabel:true,
	                   		boxLabel:'<s:text name="productos.config.expresion.recalcula"/>',
	                  		name:'switchRecalcular'
		    });

	function detailsUpdate(text){			
			if(!detailEl){
    			var bd = Ext.getCmp('details-panel').body;
    			bd.update('').setStyle('background','#fff');
    			detailEl = bd.createChild(); //create default empty div
    		}
    		detailEl.hide().update("Details:"+text).slideIn('l', {stopFx:true,duration:.2});
	}
	/**************************************************************
	Esta es la cabecera que va a ir cambiando dependiendo de la jsp, 
	solo se le van a agregar mas items, tiene dos hidden que son los 
	valores de la expresion,descripcion que espera un string en el
	action llamado descripcionExpresion y  recalcular que espera un String 
	llamado switchRecalcularExpresion, que se llenaran y enviaran al action
	de la url en la cabecera automaticamente por el handler en el
	boton de guardar de la ventana.
	***************************************************************/
	var nombreVariableTemporal = new Ext.form.TextField({
                    fieldLabel: '<s:text name="librerias.cabecera.nombre"/>',
                    id: 'nombre-cabecera',
                    name: 'nombreCabecera',
                    allowBlank:false,
                    blankText : '<s:text name="librerias.cabecera.nombre.req"/>',
                    width:150  
   	});
   
   	var descripcionVariableTemporal= new Ext.form.TextField({
                    fieldLabel: '<s:text name="librerias.cabecera.descripcion"/>',
                    name: 'descripcionCabecera',
                    allowBlank:false,
                    blankText : '<s:text name="librerias.cabecera.descripcion.req"/>',
                    width:150     
   	});
   	
	var cabecera = new Ext.form.FormPanel({
            	url:'librerias/InsertaReglaNegocio.action?numeroGrid=1',
            	id:'cabecera-variables-form',
            	frame:true,
       			bodyStyle:'padding:5px',
        		width:600,
        		height:88,
        		title:'Variables Temporales',
        		region:'north',
                items: [{xtype:'hidden',id:'hidden-descripcion-expresion',name:'descripcionExpresion'},
						{xtype:'hidden',id:'hidden-switch-recalcular-expresion',name:'switchRecalcularExpresion'},
						{
 				    		border: false,
 				    		layout: 'form',
				    		items: [
				            	nombreVariableTemporal,
				            	descripcionVariableTemporal
				           ]	
		        }]
    });

	/***************************************************************
	Termina la cabecera y empieza la parte de expresiones
	****************************************************************/
    var tab2 = new Ext.FormPanel({
					region:'center',
					labelAlign: 'left',
			        frame: true,
			        margins:'0 0 0 5',
			        url:'<s:url value="/expresiones/AgregarExpresion.action"/>',
			        bodyBorder:false,
			        id:'top-variables-temporales',
			        bodyStyle:'padding:0 0 0',
			        height:500,			        
			        width: 500,
			        items:[{xtype:'hidden',id:'hidden-codigo-expresion',name:'codigoExpresion'},{
		   				layout:'form',
		   				margins:'0 5 5 5',
		   				//height:300,
			        	bodyStyle:'padding:5px 5px 0',
		   				border:false,
		   				frame:false,
		   				title: '<s:text name="productos.expresiones.subtitulo.valorDefecto"/>',
		   				items: [{
					            layout:'column',
					            border:false,
					            items:[{
						                columnWidth:.7,
					    	            layout: 'form',
					        	        border:false,
					            	    labelAlign: 'top',
					                	items: [descripcion]
						            },{
						                columnWidth:.3,
						                layout: 'form',
						                border:false,
						                items: [{
							                    layout: 'form',
							             		heigth:20,
						    	         		border:false
						        	        },{
						            	        layout: 'form',
						                		border:false,
						                		items: [recalcular,{
							            		        xtype:'button',
		                							    text:'<s:text name="productos.config.expresion.btn.comprobarSintaxis"/>'
					        			        }]
					            	    }]
					            }]
					        },{
					        	layout:'column',
					            border:false,
					            items:[{
						                columnWidth:.7,
					    	            layout: 'form',
					        	        border:false,					                
						                items: [comboVariables]
						        	}]
					    	},{
					    	    layout:'column',
					        	border:false,
				    	        items:[{
						                columnWidth:.35,
						                layout: 'form',
						                border:false,					                
						                items: [{
						               	        xtype:'button',
            				   				    text:'<s:text name="productos.config.expresion.btn.agregarVar"/>',
               								    handler:function(){
               								    		
                					    				NuevaVariableExpresiones(dsComboV);
				               				    }					        		     
						                }]
						            },{
						                columnWidth:.35,
						                layout: 'form',
						                border:false,
					    	            items: [{
					        	       	        xtype:'button',
		                					    text:'<s:text name="productos.config.expresion.btn.editarVar"/>',
		                					    id:'editar-variable'	        		     
						                }]
					            	},{
						                columnWidth:.3,
						                layout: 'form',
					    	            border:false,
					        	        
					            	    items: [{
					               		        xtype:'button',
		           			   				    text:'<s:text name="productos.config.expresion.btn.eliminarVar"/>',
        	       							    handler:function(){
        	       							    		var valorCombo=comboVariables.getValue();
        	       							    		if(comboVariables.isDirty()){
			    	           							    comboVariables.reset();
        	       							    			
	        	       							    		var url4delete='/expresiones/EliminarVariable.action?claveSeleccionada='+valorCombo;
    	    	       							    		Ext.MessageBox.confirm('Mensaje','Realmente deseas eliminar el elemento?', function(btn) {
																if(btn == 'yes'){   
																	tab2.form.submit({	
																		url: url4delete,
																	    waitMsg:"Procesando...",
											    						failure: function(form, action) {
																	    	Ext.MessageBox.alert('Status', 'Error al eliminar');
																		},
																		success: function(form, action) {
																		    Ext.MessageBox.alert('Status', 'Elemento eliminado');						    
																		    dsComboV.load();						    
																		}
																	});         	 
																}
															});
			    	           							    //DeleteInDataStoreFromAction(tab2,url4delete,dsComboV);
			    	           							    
;
		    	           							    }else{
		    	           							    	Ext.MessageBox.alert('Error', 'No ha seleccionado una variable');
		    	           							    }
			        	       				    }					        		     
					    	            }]
					        	}]
					    
						    }]
			    	},{layout:'form',hegth:'10'}]
				});
				

/************************************
nueva ventana
***********************************/
	
    // define window and show it in desktop
 var wind = new Ext.Window({
            title: '<s:text name="libreria.titulo.individual"/>',
            closable:true,
            buttonAlign:'center',
            width:700,
            height:340,
            //border:false,
            plain:true,
            layout: 'border',
            modal:true,
			items:[{
						region:'west',
						heigth:'500',
						width:'200',
						minSize:'175',
						maxSize:'300',
	        		    collapsible: false,
		                margins:'0 5 0 5',
           			    layout:'accordion',
	                    layoutConfig:{
       				            animate:true
	                    },
       				    items: [{    
						        title:'<s:text name="productos.expresiones.subtitulo.funciones"/>',
						        xtype: 'treepanel',
			    			    width: 200,
						        autoScroll: true,
						        split: true,
			    			    loader: new Ext.tree.TreeLoader({
						               dataUrl:'<s:url value="/expresiones/FuncionesArbol.action"/>'
					            }), 
						        root:  new Ext.tree.AsyncTreeNode({
						                text: 'Ext JS', 
						                draggable:false, // disable root node dragging
						                id:'source'
					            }),
						        rootVisible: false,
						        listeners: {
			    				        click: function(n) {
								            //Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.attributes.text + '"');								               
	 										//detailsUpdate(n.attributes.descripcion);
	 										var valor = descripcion.getValue();
							            	if(descripcion.isDirty()){
							            		valor = valor+" ";
							            	}
	 										descripcion.setValue(valor+n.attributes.funcion);
							            }/*,
							            dblclick: function(n){
							            	var valor = descripcion.getValue();
							            	if(descripcion.isDirty()){
							            		valor = valor+" ";
							            	}
	 										descripcion.setValue(valor+n.attributes.funcion);
							           }*/
						        }
						    },{    
						        title: '<s:text name="productos.expresiones.subtitulo.variableTemporal"/>',
						        xtype: 'treepanel',
						        width: 200,
			    			    autoScroll: true,
						        split: true,
						        loader: new Ext.tree.TreeLoader({
						               dataUrl:'<s:url value="/expresiones/VariablesTemporalesArbol.action"/>'
					            }), 
						        root:  new Ext.tree.AsyncTreeNode({
						                text: 'Ext JS', 
						                draggable:false, // disable root node dragging
						                id:'source'
					            }),
						        rootVisible: false,
						        listeners: {
			    				        click: function(n) {
								                //Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.attributes.text + '"');								                
	 											//detailsUpdate(n.attributes.descripcion);
	 											var valor= descripcion.getValue();
								            	if(descripcion.isDirty()){
									            	valor=valor+" &";
		 										}else{
	 												valor=valor+"&";	 										
	 											}
	 											descripcion.setValue(valor+n.attributes.funcion);
							            }/*,
							            dblclick: function(n){
								            var valor= descripcion.getValue();
							            	if(descripcion.isDirty()){
								            	valor=valor+" &";
	 										}else{
	 											valor=valor+"&";	 										
	 										}
	 										descripcion.setValue(valor+n.attributes.funcion);
							            }*/
						        }
						    }]
		
										
				},tab2,cabecera],
				buttons:[{
  						text:'<s:text name="productos.config.expresion.btn.guardar"/>',
  						handler: function(){
  							if(descripcion.getValue().length>0) {
					 		        var valorDeDescripcionEnLaExprecion=Ext.getCmp('descripcion-expresion').getValue();
					 		        Ext.getCmp('hidden-descripcion-expresion').setValue( valorDeDescripcionEnLaExprecion );
					 		        var valorDeRecalcularEnLaExprecion=Ext.getCmp('switch-recalcular-expresion').getValue();
					 		        Ext.getCmp('hidden-switch-recalcular-expresion').setValue( valorDeRecalcularEnLaExprecion );
					 		        Ext.getCmp('hidden-codigo-expresion').setValue( codigoExpresion );
					 		        
					 		        tab2.form.submit({
					 		        		waitTitle:'<s:text name="ventana.expresiones.proceso.mensaje.titulo"/>',
					            			waitMsg:'<s:text name="ventana.expresiones.proceso.mensaje"/>',
								            failure: function(form, action) {
												    Ext.MessageBox.alert('Error Message', Ext.util.JSON.decode(action.response.responseText).mensajeDelAction);												    
											},
											success: function(form, action) {
													 cabecera.form.submit({	
								 		        		waitTitle:'<s:text name="ventana.expresiones.proceso.mensaje.titulo"/>',
								            			waitMsg:'<s:text name="ventana.expresiones.proceso.mensaje"/>',
											            failure: function(form, action) {
														    Ext.MessageBox.alert('Error', 'Datos no agregados');
														    //wind.close();
														},
														success: function(form, action) {
														    //Ext.MessageBox.alert('Status', 'Elemento agregado');
														    //Ext.MessageBox.alert('Confirm', action.result.info);						   
														    storeVariables.load();												    
														    wind.close();
														}
											        });       
											}
					 		        });
					 		                  
	        		        } else{
	        		        		descripcion.markInvalid('<s:text name="productos.valida.expresion.descripcion"/>');
									Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
							}     
  						}
  					},{
  						text:'<s:text name="productos.config.expresion.btn.cancelar"/>',
  						handler:function(){
  							//storeVariables.load();	  						
  							wind.close();
  						}
  				}]
        });
        wind.show();
};