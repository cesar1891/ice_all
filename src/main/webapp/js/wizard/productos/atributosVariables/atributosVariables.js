Ext.onReady(function(){
Ext.QuickTips.init();
var dataStorePadre;
Ext.form.Field.prototype.msgTarget = "side";

	
		    		       	
    var banderaSelectCombo = false;    
    var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'atributosVariables/ListaValoresAtributos.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'valoresDeAtributos',
            id: 'valoresDeAtributos'
	        },[
           {name: 'codigoTabla', type: 'string',mapping:'cdTabla'},
           {name: 'descripcionTabla', type: 'string',mapping:'dsTabla'}
        ]),
        baseParams:{limit:'-1'},
		remoteSort: true
    });
       ds.setDefaultSort('valoresDeAtributos', 'desc'); 
       
    var dataStoreCondiciones = new Ext.data.Store({
    	proxy: new Ext.data.HttpProxy({
				url:'librerias/CargaReglaNegocio.action'+'?numeroGrid='+'3'	
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaReglaNegocioCondiciones',   
            	id: 'cond'
	        	},[
	        		{name: 'nombreCabecera',  		type: 'string',  	mapping:'nombre'},
	        		{name: 'descripcionCabecera',  	type: 'string',  	mapping:'descripcion'}
			]),
			remoteSort: true
    });  
 		dataStoreCondiciones.setDefaultSort('cond', 'desc');
       
    	dataStorePadre = new Ext.data.Store({
    	proxy: new Ext.data.HttpProxy({
				url:'atributosVariables/CargaComboPadre.action'
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaPadre',   
            	id: 'pat'
	        	},[
	        		{name: 'clavePadre',  			type: 'string',  	mapping:'key'},
	        		{name: 'descripcionPadre',  	type: 'string',  	mapping:'value'}
			]),
			baseParams:{codigoAtributoP:'',cdTipSituacion:''},
			remoteSort: true
    });  
 		dataStorePadre.setDefaultSort('pat', 'desc');     
       
       
    var comboWithTooltip = new Ext.form.ComboBox({    		
    		tpl: '<tpl for="."><div ext:qtip="{codigoTabla}" class="x-combo-list-item">{descripcionTabla}</div></tpl>',
    		store: ds,
    		displayField:'descripcionTabla',
    		valueField :'codigoTabla',
	    	id: 'combo-lista-de-valores-atributos',
    		typeAhead: true,
    		mode: 'local',
    		triggerAction: 'all',
    		emptyText:'Seleccione un valor',
    		selectOnFocus:true,
    		fieldLabel: 'Lista de Valores',
    		listWidth: '250',
    		name:"descripcionTabla",
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
	    		banderaSelectCombo = true;
	    		Ext.getCmp('hidden-lista-valores-atributo').setValue(Ext.getCmp('combo-lista-de-valores-atributos').getValue());                      	
    	}
	});
	
	var comboCondicion = new Ext.form.ComboBox({
				id:'combo-condicion-atributos-variables-arbol',
				tpl: '<tpl for="."><div ext:qtip="{nombreCabecera}" class="x-combo-list-item">{descripcionCabecera}</div></tpl>',
    			store : dataStoreCondiciones, 
				listWidth: '250',
    			mode: 'local',
				name: 'condicion',
    			typeAhead: true,
    			triggerAction: 'all',    		
    			displayField:'descripcionCabecera',
				fieldLabel: 'Condici\u00F3n',
				emptyText:'Seleccione una Condici\u00F3n',
				selectOnFocus:true
				
		});

	var comboPadre = new Ext.form.ComboBox({  
				id:'combo-padre-atributos-variables-arbol',
				tpl: '<tpl for="."><div ext:qtip="{clavePadre}" class="x-combo-list-item">{descripcionPadre}</div></tpl>',
    			store: dataStorePadre, 
				listWidth: '250',
    			mode: 'local',
				name: 'padre',
    			typeAhead: true,
    			triggerAction: 'all',
    			displayField:'descripcionPadre',
    			valueField :'clavePadre',
				fieldLabel: 'Padre',
				emptyText:'Seleccione un Padre',
				selectOnFocus:true,
				listeners:{
					collapse: function(){
								validaComboPadre();						
					}
				}
		});     
  
		function validaComboPadre(){
				var seccion = Ext.getCmp('combo-padre-atributos-variables-arbol').getValue();
	    		
	              		if(seccion.length==0 || seccion == 'Seleccione un Padre'){
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
		}
            
		
	function validaValorMinMax( valorMin , valorMax ){
  		if( valorMin > valorMax ){
            Ext.MessageBox.alert('Error','el valor M\u00EDnimo debe de ser menor al valor M\u00E1ximo');
            return 0;
        }else{
        	return 1;
        }	
    }		
			
    var hideTextMaximo= new Ext.form.NumberField({
				            id:'id-hidden-text-maximo',
            				name:'maximo',
            				fieldLabel: 'M\u00E1ximo',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				hideParent:true,
            				width:25	
            							       
           });
           
     var hideTextMinimo= new Ext.form.NumberField({
				            id:'id-hidden-text-minimo',
            				name:'minimo',
            				fieldLabel: 'M\u00EDnimo',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				hideParent:true,
            				width:25	
            							       
           });
           
      var hideOrden = new Ext.form.NumberField({
      						id:'id-hidden-text-orden',
      						name:'orden',
      						fieldLabel:'Orden',
      						allowDecimals : false,
				            allowNegative : false,
        				    blankText : 'Dato Num\u00E9rico Requerido',				
            				width:160
      	   });
      	   
      var hideAgrupador = new Ext.form.NumberField({
      						id:'id-hidden-text-agrupador',
      						name:'agrupador',
      						fieldLabel:'Agrupador',
      						allowDecimals : false,
				            allowNegative : false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				width:160
      	   });
           
     var functionRadioAlfa= new Ext.form.Radio({
     						id:'function-radio-alfa-atributos-variables',
            				name:'formato',
            				fieldLabel:'Formato',
            				checked: true,
            				boxLabel: 'Alfanum\u00E9rico',
  	  	       				onClick: function(){			            		
            						hideTextMaximo.enable();
            						hideTextMinimo.enable();
            						Ext.getCmp('id-hidden-text-maximo').setValue('');
            						Ext.getCmp('id-hidden-text-minimo').setValue('');
            						Ext.getCmp('hidden-radio-atributos-variables').setValue("A");
				            	
			            	}
            });   
            
    var functionRadioNumeric= new Ext.form.Radio({
    						id:'function-radio-numeric-atributos-variables',
            				name:'formato',
            				labelSeparator : "",
            				boxLabel: 'Num\u00E9rico',
            				hideLabel:true,
  	  	       				onClick: function(){			            		
            						hideTextMaximo.enable();
            						hideTextMinimo.enable();
            						Ext.getCmp('id-hidden-text-maximo').setValue('');
            						Ext.getCmp('id-hidden-text-minimo').setValue('');
            						Ext.getCmp('hidden-radio-atributos-variables').setValue("N");
				            	
			            	}
            });  
            
    var functionRadioPercent= new Ext.form.Radio({
            				id:'function-radio-percent-atributos-variables',
            				name:'formato',
            				labelSeparator : "",
            				boxLabel: 'Porcentaje',
  	  	       				onClick: function(){			            		
            						hideTextMaximo.disable();            						
				            		hideTextMinimo.disable();
  	  	       						Ext.getCmp('id-hidden-text-maximo').setValue('');
            						Ext.getCmp('id-hidden-text-minimo').setValue('');
				            		Ext.getCmp('hidden-radio-atributos-variables').setValue("P");
				            
			            	}
            }); 
            
    var functionRadioDate= new Ext.form.Radio({
            				id:'function-radio-date-atributos-variables',
            				name: 'formato',
				           	boxLabel: 'Fecha',
				           	labelSeparator : "",
				            hideLabel:true,
  	  	       				onClick: function(){			            		
            						hideTextMaximo.disable();            						
				            		hideTextMinimo.disable();
  	  	       						Ext.getCmp('id-hidden-text-maximo').setValue('');
            						Ext.getCmp('id-hidden-text-minimo').setValue('');
				            		Ext.getCmp('hidden-radio-atributos-variables').setValue("F");
				            	
			            	}
            });                                
      
    var obligatorioCheck= new Ext.form.Checkbox({
    			id:'obligatorio-check-atributos-variables',
                name:'obligatorio',
                boxLabel: 'Obligatorio',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            		this.setRawValue("S");
				            		Ext.getCmp('hidden-form-check-complementario').hide();
				            		Ext.getCmp('hidden-form-check-complementario-obligatorio').hide();				            		
				            		Ext.getCmp('complementario-check-atributos-variables').reset();
				            		Ext.getCmp('complementario-obligatorio-check-atributos-variables').reset();
				            		Ext.getCmp('complementario-modificable-check-atributos-variables').reset();
				            		
				            	}else{
				            		Ext.getCmp('hidden-form-check-complementario').show();
				            	}
				            }
                 
            });
            
    var emisionCheck= new Ext.form.Checkbox({
    			id:'emision-check-atributos-variables',
                name:'modificaEmision',
                boxLabel: 'Modificable en emision',
            	hideLabel:true,			
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });        
            
    var endosoCheck= new Ext.form.Checkbox({
    			id:'endoso-check-atributos-variables',
                name:'modificaEndoso',
                boxLabel: 'Modificable en endosos',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });
            
    var retarifacionCheck= new Ext.form.Checkbox({
    			id:'retarificacion-check-atributos-variables',
                name:'retarificacion',
                boxLabel:'Interviene en la Tarificaci\u00F3n / Retarificaci\u00F3n',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });      
            
    var cotizadorCheck= new Ext.form.Checkbox({
    			id:'cotizador-check-atributos-variables',
                name:'despliegaCotizador',
                boxLabel: 'Se despliega en el cotizador',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });
            
    var complementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-check-atributos-variables',
                name:'datoComplementario',
                boxLabel: 'Dato complementario',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");
				            	Ext.getCmp('hidden-form-check-obligatorio').hide();
				          		Ext.getCmp('obligatorio-check-atributos-variables').reset();
				          		Ext.getCmp('hidden-form-check-complementario-obligatorio').show();
				            	}else{
				            		Ext.getCmp('hidden-form-check-obligatorio').show();
				            		Ext.getCmp('hidden-form-check-complementario-obligatorio').hide();
				            		Ext.getCmp('complementario-obligatorio-check-atributos-variables').reset();
				            		Ext.getCmp('complementario-modificable-check-atributos-variables').reset();
				            	}
				            }
                 
            });                 

	var obligaComplementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-obligatorio-check-atributos-variables',
                name:'obligatorioComplementario',
                boxLabel: 'Dato complementario obligatorio',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });    
            
     var modificableComplementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-modificable-check-atributos-variables',
                name:'modificableComplementario',
                boxLabel: 'Dato complementario modificable',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });  
            
      var apareceEndosoCheck= new Ext.form.Checkbox({
    			id:'aparece-endoso-check-atributos-variables',
                name:'apareceEndoso',
                boxLabel: 'Aparece en endoso',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
                	if(this.getValue()){
				    	this.setRawValue("S");
				    	Ext.getCmp('hidden-form-check-endoso-obligatorio').show();
					}else{
						Ext.getCmp('hidden-form-check-endoso-obligatorio').hide();
						Ext.getCmp('obligatorio-endoso-check-atributos-variables').reset();
						Ext.getCmp('endoso-check-atributos-variables').reset();
					}
				}
                 
            });     
            
       var obligatorioEndosoCheck= new Ext.form.Checkbox({
    			id:'obligatorio-endoso-check-atributos-variables',
                name:'obligatorioEndoso',
                boxLabel: 'Obligatorio en Endoso',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });    
       
       var esAtributoParaTodosCheck= new Ext.form.Checkbox({
			id:'atributo-para-todos-check-atributos-variables',
           name:'atributoParaTodos',
           boxLabel: 'Repetido en situaciones',
       	hideLabel:true,	
           checked:false,
           onClick:function(){
			            	if(this.getValue()){
			            	this.setRawValue("S");				            	
			            	}
			            }
            
       });  
			
            var datosVariablesForm = new Ext.form.FormPanel({
            	id:'id-atributos-variables-form',
            	frame:false,
            	url:'atributosVariables/GuardarAtributosVariables.action',
        		header:true,
       			bodyStyle:'padding:5px',
        		width:615,
        		buttonAlign: "center",
                items: [{xtype:'hidden',id:'hidden-codigo-expresion-session',name:'codigoExpresionSession'},
                		{xtype:'hidden',id:'hidden-lista-valores-atributo',name:'codigoTabla'},
                {
                	xtype:'hidden',
		            name: "claveCampo",
        		    id: "hidden-clave-campo-atributos-variables"
                },{
                	xtype:'hidden',
		            name: "descripcionHidden",
        		    id: "hidden-descripcion-atributos-variables"
                },{
                	xtype:'hidden',
		            name: "codigoRadioAtributosVariables",
        		    id: "hidden-radio-atributos-variables"
                },{
                	xtype:'hidden',
		            name: "formatoHidden",
        		    id: "hidden-formato-atributos-variables"
                },{
                	xtype:'hidden',                	
                	id:'hidden-valor-defecto-atributos-variables',
		            name: "valorDefecto"      		    
        		 
                },{
                	xtype:'hidden',                	
                	id:'hidden-codigo-expresion-atributos-variables',
		            name: "codigoExpresion"      		    
        		 
                },{
        		    xtype:'textfield',
		            name: "descripcion",
		            allowBlank: false,
		            blankText : 'Descripci\u00F3n Requerida',
		            id:'id-descripcion-atributos-variables',
		            maxLength: 200,
		            maxLengthText: 'M&aacute;ximo 200 caracteres',
        		    fieldLabel: 'Descripci\u00F3n*',
		            width: 150
        		},{
        			layout:'column',
        			border: false,        			
            				items:[{
				                columnWidth:.4,
                				layout: 'form',
                				border: false,                				
				                items: [
				                	functionRadioAlfa
				                ]
        					},{
               					columnWidth:.12,
			                	layout: 'form',
			                	border: false,			                	
               					items: [
               						functionRadioNumeric
               					]
				    		},{
               					columnWidth:.24,
			                	layout: 'form',
			                	labelAlign:"right",
			                	border: false,			                	
               					items: [
               						hideTextMinimo
               					]
				            },{
               					columnWidth:.24,
               					labelAlign:"right",
			                	layout: 'form',
			                	border: false,			                	
               					items: [               						
               						hideTextMaximo
               					]	
				    		}]
        		
		        },{
		        	layout: 'column',
				   	border: false,
				   	items: [{
                				columnWidth:.35,
				                layout:'form',
				                border: false,
				                width:197,				                    	
				                items:[
				                    	functionRadioPercent
				                ]
				             },{
                				columnWidth:.65,
                				layout:'form',
                				border: false,           						
                				items:[
                						functionRadioDate
                				]
				             }
				     ]				           		
				},{
					layout:'form',
		        	id:'hidden-form-check-obligatorio',
		        	border: false,
		        	items:[		        	
        					obligatorioCheck
		        	]     	
				},{
					layout: 'form',
					id: 'form-check-emision-endoso-cotizador',
					border: false,
					items: [
						emisionCheck,
						cotizadorCheck
					]
				},{
		        	layout:'form',
		        	id:'hidden-form-attributos-coberturas',
		        	border: false,
		        	items:[		        	
        					retarifacionCheck
		        	]     		
				},{
					layout:'form',
		        	id:'hidden-form-check-complementario',
		        	border: false,
		        	items:[		        	
        					complementarioCheck
		        	]     	
				},{
					layout:'form',
		        	id:'hidden-form-check-complementario-obligatorio',
		        	border: false,
		        	items:[		        	        					
        					obligaComplementarioCheck,
							modificableComplementarioCheck
		        	]     	
				},{
					layout: 'form',
					id: 'form-check-aparece-endoso',
					border: false,
					items: [
						apareceEndosoCheck
					]
				},{
					layout:'form',
		        	id:'hidden-form-check-endoso-obligatorio',
		        	border: false,
		        	items:[		        	        					
        				endosoCheck,
						obligatorioEndosoCheck
		        	]
				},{
					layout:'form',
		        	//id:'hidden-form-check-obligatorio',
		        	border: false,
		        	items:[
		        	       esAtributoParaTodosCheck
		        	]     	
				},{
 				   layout:'column',
 				   border: false,
            				items:[{
				                	columnWidth:.58,
                					layout: 'form',
                					border: false,
                					width:300,
				                	items: [{
				                			layout: 'form',
				                			id:'hidden-forma-padre',
                							border: false,
				                				items:[
				                					comboPadre				                					
				                				]
				                			},{
				                			layout: 'form',
				                			id:'hidden-forma-padre-condicion-orden-agrupador',
                							border: false,
				                				items:[
				                					hideAgrupador,
				                					hideOrden,
				                					comboCondicion
				                				]
				                			},
				                			{layout: 'form', border: false},
				                			comboWithTooltip
				                	
				                ]
				            },{
                				columnWidth:.42,
				                layout: 'form',
				                heigth:200,
				                border: false,
				                items:[{
				                		layout: 'form',
				                		id:'hidden-forma-padre-condicion-orden-agrupador-boton',
                						border: false,				            
                						items: [
                								{layout: 'form', height:80, border: false,id:'forma-boton-vacia'},		
                								{xtype:'button', 
                								text: '\u00A0Agregar Condici\u00F3n\u00A0\u00A0', 
                								name: 'AgregarCondicion', 
                								buttonAlign: "right", 
                								handler: function() {
                									var connect = new Ext.data.Connection();
					    							connect.request ({
														url:'atributosVariables/ObtenerCodigoExpresion.action',
														callback: function (options, success, response) {				   
															codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
															ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONDICIONES', dataStoreCondiciones, '3');
														}
											   		});
                								}
                								}]
                								}
                								,{
                								layout: 'form', height:20, border: false},
                								{layout: 'form', height:25, border: false, id:'forma-boton-catalogo-vacia'},	
                								{xtype:'button', 
                								text: 'Agregar al Cat\u00E1logo', 
                								name: 'AgregarAlCatalogo', 
                								buttonAlign: "right", 
                								handler: function() {creaListasDeValores(ds);}}
                									
                						]		
				                
				            }]
		        }],

        buttons: [{
	            	text: 'Valor por Defecto',
	            	handler:function(){
	            		if(Ext.getCmp('hidden-codigo-expresion-atributos-variables').getValue()!=null 
	            		&& Ext.getCmp('hidden-codigo-expresion-atributos-variables').getValue()!=""
	            		&& Ext.getCmp('hidden-codigo-expresion-atributos-variables').getValue()!="0"
	            		&& Ext.getCmp('hidden-codigo-expresion-atributos-variables').getValue()!="undefined"){
	            			ExpresionesVentana2(Ext.getCmp('hidden-codigo-expresion-atributos-variables').getValue(),Ext.getCmp('hidden-codigo-expresion-session').getValue());
	            		}else{
	            			var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									Ext.getCmp('hidden-codigo-expresion-atributos-variables').setValue(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									ExpresionesVentana2(Ext.getCmp('hidden-codigo-expresion-atributos-variables').getValue(),Ext.getCmp('hidden-codigo-expresion-session').getValue());
								}
					   		});
	            		}
	            	}	            	
    		    },{
	            	text: 'Guardar',
	            	handler: function() {        	
	            		
                	if (datosVariablesForm.form.isValid()) {
                       
                	   var validaMaxMin = 1; 	
                	  if(  Ext.getCmp('id-hidden-text-maximo').isVisible() &&
	                       Ext.getCmp('id-hidden-text-minimo').isVisible()
	                    ){
                	        var valorMax =  Ext.getCmp('id-hidden-text-maximo').getValue();
            			    var valorMin =  Ext.getCmp('id-hidden-text-minimo').getValue();
	                        validaMaxMin =   validaValorMinMax(valorMin, valorMax);
	                    }
	                    
                	    if( validaMaxMin ){ 
                	
                   		  if((Ext.getCmp('hidden-clave-campo-atributos-variables').getValue()!="undefined" 
                 			&& Ext.getCmp('hidden-clave-campo-atributos-variables').getValue()!=""
                 			&& Ext.getCmp('hidden-clave-campo-atributos-variables').getValue()!=null) 
                 			&& !banderaSelectCombo){
                   		  }else{
                   			Ext.getCmp('hidden-lista-valores-atributo').setValue(Ext.getCmp('combo-lista-de-valores-atributos').getValue());                      	
                   		  }
                   		
                   		 
                   		  banderaSelectCombo=false;          	
                   		  //console.log(datosVariablesForm);
                   		  //console.log(datosVariablesForm.form.getValues());
		 		        	datosVariablesForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Guardando atributo variable...',
					            	failure: function(form, action) {
					            		var mensajeRespuesta = Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta;
					            		Ext.MessageBox.alert('Error', Ext.isEmpty(mensajeRespuesta) ? 'Error al agregar atributos variables' : mensajeRespuesta);
									},
									success: function(form, action) {
								    	var mensajeRespuesta = Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta;
										Ext.MessageBox.alert('Status', Ext.isEmpty(mensajeRespuesta) ? 'El atributo variable se ha guardado con &eacute;xito' : mensajeRespuesta);
						    			Ext.getCmp('arbol-productos').getRootNode().reload();  		
								}
			        		}); 
			        		
			        		
                	    }// cierra valida Max Min 	   
			        		
                		} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Eliminar',
	            	handler: function(){
	            		Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           					if(btn == 'yes'){
           						
           						//Validar si el atributo variable tiene hijos asociados, para mostrar advertencia.
								datosVariablesForm.form.submit({
	            					url:'atributosVariables/ValidaHijosAtributoVariable.action',
					            	waitTitle: 'Espere',
					            	waitMsg: 'Validando...',
					            	failure: function(form, action) {
								    	Ext.MessageBox.alert('Error ', 'Error al verificar asociaciones de atributo variable');
									},
									success: function(form, action) {
										var mensajeRespuesta = Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta;
										//Si mensajeRespuesta no esta vacio, s� hay hijos, entonces preguntamos:
										if(!Ext.isEmpty(mensajeRespuesta)){
											Ext.MessageBox.confirm('Mensaje', mensajeRespuesta, function(btn) {
           										if(btn == 'yes'){
           											eliminarAtributoVariable(datosVariablesForm);
           										}
											});
										}else{
											eliminarAtributoVariable(datosVariablesForm);
										}
									}
			        			});
			        			
			        		}
						});
	            	}
    		    },{
	            	text: 'Cancelar'
    		    }]        
    });
    
    // Funcion eliminaAtributoVariable
    function eliminarAtributoVariable(datosVariablesForm){
    	datosVariablesForm.form.submit({
			url:'atributosVariables/EliminarAtributoVariable.action',
			waitTitle:'Espere',
			waitMsg:'Eliminando atributo variable...',
			failure: function(form, action) {
				Ext.MessageBox.alert('Error ', Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta);
			},
			success: function(form, action) {
				Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta);
				action.form.reset();  
				Ext.getCmp('arbol-productos').getRootNode().reload();  		
			}
		});
    }
    
    var params2 = 'codigoExpresionSession='+Ext.getCmp('hidden-codigo-expresion-session').getValue(); 
    Ext.getCmp('hidden-form-check-complementario-obligatorio').hide();
    Ext.getCmp('hidden-form-check-endoso-obligatorio').hide();
    datosVariablesForm.render('centerAtributos');
  	
});