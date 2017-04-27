CreaDatosVariablesObjeto=function(store,row){
edita=0;
if(row!=null){      
	edita=1;    
    var rec = store.getAt(row);
 
   store.on('load', function(){ 
   						   	//Ext.getCmp('id-descripcion-atributos-variables-objeto').setDisabled(true);
   						   	Ext.getCmp('id-datos-variables-objetos-form').getForm().loadRecord(rec);                      
                           	if(rec.get('formato') == 'A'){                           		
                				Ext.getCmp('function-radio-alfa-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue('A');
                				Ext.getCmp('id-hidden-text-maximo-objeto').enable();
           						Ext.getCmp('id-hidden-text-minimo-objeto').enable();
                			}
                			if(rec.get('formato') == 'N'){                           		
                				Ext.getCmp('function-radio-numeric-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue('N');
                				Ext.getCmp('id-hidden-text-maximo-objeto').enable();
           						Ext.getCmp('id-hidden-text-minimo-objeto').enable();
                			}
                			if(rec.get('formato') == 'P'){                           		
                				Ext.getCmp('function-radio-percent-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue('P');
                				Ext.getCmp('id-hidden-text-maximo-objeto').reset();
       							Ext.getCmp('id-hidden-text-minimo-objeto').reset();					            	
            					Ext.getCmp('id-hidden-text-maximo-objeto').disable();            						
	        					Ext.getCmp('id-hidden-text-minimo-objeto').disable();
                			}
                			if(rec.get('formato') == 'F'){                           		
                				Ext.getCmp('function-radio-date-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue('F');
                				Ext.getCmp('id-hidden-text-maximo-objeto').reset();
       							Ext.getCmp('id-hidden-text-minimo-objeto').reset();					            	
            					Ext.getCmp('id-hidden-text-maximo-objeto').disable();            						
	        					Ext.getCmp('id-hidden-text-minimo-objeto').disable();
                			}
                			if(rec.get('obligatorio')=="S"){
                				Ext.getCmp('obligatorio-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('obligatorio-check-atributos-variables-objeto').setRawValue("S");
			                }else{
            			    	Ext.getCmp('obligatorio-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('obligatorio-check-atributos-variables-objeto').setRawValue("N");
                			}
                			if(rec.get('modificaEndoso')=="S"){
                				Ext.getCmp('endoso-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('endoso-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('endoso-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('endoso-check-atributos-variables-objeto').setRawValue("N");
                			}               
                			if(rec.get('modificaEmision')=="S"){
                				Ext.getCmp('emision-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('emision-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('emision-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('emision-check-atributos-variables-objeto').setRawValue("N");
                			}
                			if(rec.get('retarificacion')=="S"){
                				Ext.getCmp('retarificacion-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('retarificacion-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('retarificacion-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('retarificacion-check-atributos-variables-objeto').setRawValue("N");
                			}
                			
                			//***********
                			if(rec.get('apareceCotizador')=="S"){
                				Ext.getCmp('aparece-cotizador-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('aparece-cotizador-check-atributos-variables-objeto').setRawValue("S");
			                }else{
            			    	Ext.getCmp('aparece-cotizador-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('aparece-cotizador-check-atributos-variables-objeto').setRawValue("N");
                			}
                			if(rec.get('datoComplementario')=="S"){
                				Ext.getCmp('complementario-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('complementario-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('complementario-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('complementario-check-atributos-variables-objeto').setRawValue("N");
                			}               
                			if(rec.get('obligatorioComplementario')=="S"){
                				Ext.getCmp('complementario-obligatorio-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('complementario-obligatorio-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('complementario-obligatorio-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('complementario-obligatorio-check-atributos-variables-objeto').setRawValue("N");
                			}
                			if(rec.get('modificableComplementario')=="S"){
                				Ext.getCmp('complementario-modificable-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('complementario-modificable-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('complementario-modificable-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('complementario-modificable-check-atributos-variables-objeto').setRawValue("N");
                			}
                			if(rec.get('apareceEndoso')=="S"){
                				Ext.getCmp('aparece-endoso-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('aparece-endoso-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('aparece-endoso-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('aparece-endoso-check-atributos-variables-objeto').setRawValue("N");
                			}
                			if(rec.get('obligatorioEndoso')=="S"){
                				Ext.getCmp('obligatorio-endoso-check-atributos-variables-objeto').setValue(true);
                				Ext.getCmp('obligatorio-endoso-check-atributos-variables-objeto').setRawValue("S");
                			}else{
                				Ext.getCmp('obligatorio-endoso-check-atributos-variables-objeto').setValue(false);
                				Ext.getCmp('obligatorio-endoso-check-atributos-variables-objeto').setRawValue("N");
                			}
                			//*************
                			
                			Ext.getCmp('hidden-descripcion-atributos-variables-objeto').setValue(rec.get('descripcion'));
                			Ext.getCmp('hidden-clave-campo-atributos-variables-objeto').setValue(rec.get('claveCampo'));
                			
                			/*Ext.getCmp('hidden-formato-atributos-variables').setValue(recAtributosVariables.get('formato'));
                			Ext.getCmp('hidden-lista-valores-atributo').setValue(recAtributosVariables.get('codigoTabla'));
                			Ext.getCmp('combo-lista-de-valores-atributos').setValue(recAtributosVariables.get('descripcionTabla'));
                			Ext.getCmp('hidden-codigo-expresion-atributos-variables-obj').setValue(recAtributosVariables.get('codigoExpresion'));
							Ext.getCmp('id-descripcion-atributos-variables').disable();*/
							/*sErGiO*/
							Ext.getCmp('combo-condicion-atributos-variables-arbol-objeto').setValue(rec.get('condicion'));
							Ext.getCmp('combo-padre-atributos-variables-arbol-objeto').setValue(rec.get('padre'));
							Ext.getCmp('id-hidden-text-orden-objeto').setValue(rec.get('orden'));
							Ext.getCmp('id-hidden-text-agrupador-objeto').setValue(rec.get('agrupador'));
							/*sErGiO*/
							
                          	ds.load();
                          	dataStorePadreObjeto.load();
   							dataStoreCondicionesObjeto.load();
   
                           });
	
}

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
           //{name: 'claveCampo', type: 'string',mapping:'codigoAtributo'}    
        ]),
		remoteSort: true
    });
       ds.setDefaultSort('valoresDeAtributos', 'desc'); 
       
       var comboWithTooltip = new Ext.form.ComboBox({
    		tpl: '<tpl for="."><div ext:qtip="{codigoTabla}" class="x-combo-list-item">{descripcionTabla}</div></tpl>',
    		store: ds,
    		displayField:'descripcionTabla',
    		valueField :'codigoTabla',
    		id: 'combo-lista-de-valores-atributos-objetos',
    		//allowBlank:false,
    		//blankText : 'Dato requerido',
		    //maxLength : '30',
    		//maxLengthText : 'Treinta caracteres maximo',
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
	    	Ext.getCmp('hidden-lista-valores-atributo-objeto').setValue(Ext.getCmp('combo-lista-de-valores-atributos-objetos').getValue());                      	
                   			
    		//alert(banderaSelectCombo);
    }
    //applyTo: 'local-descripcion-with-qtip'
});            
 



 function validaMaxMinClaveAtributoObj(){         
    
	 if( (!hideTextMinimo.disabled) &&  (!hideTextMaximo.disabled)  ){
	    //alert('enable') 
    	if( hideTextMinimo.getValue()!= '' && hideTextMaximo.getValue()!= ''){
	
    		if( hideTextMinimo.getValue() <=  hideTextMaximo.getValue()){
    			return true
    		}
    		if( hideTextMinimo.getValue()> hideTextMaximo.getValue()){
    			Ext.MessageBox.alert('Error', 'Valor maximo es menor que minimo');
    			return false
    		}    		
    	}
    	return true;
	 }else{	//alert('disable');
	 	 hideTextMinimo.reset();
	 	 hideTextMaximo.reset();
    	return true
	 }	  	
 }	                 

            
    var hideTextMaximo= new Ext.form.NumberField({
				            id:'id-hidden-text-maximo-objeto',
            				name:'maximo',
            				fieldLabel: 'M\u00E1ximo',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				hideParent:true,
            				//labelSeparator:'',
            				width:25	
            							       
           });
           
     var hideTextMinimo= new Ext.form.NumberField({
				            id:'id-hidden-text-minimo-objeto',
            				name:'minimo',
            				fieldLabel: 'M\u00EDnimo',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				hideParent:true,
            				//labelSeparator:'',
            				width:25	
            							       
           });      
           
     var functionRadioAlfa= new Ext.form.Radio({
     						id:'function-radio-alfa-atributos-variables-objeto',
            				name:'formato',
            				fieldLabel: 'Formato',
            				checked: true,
            				//labelSeparator : "",
            				boxLabel: 'Alfanum\u00E9rico',
            				//width: 10,
  	  	       				onClick: function(){			            		
            										            	
            						hideTextMaximo.enable();
            						hideTextMinimo.enable();
            						Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue("A");
				            	
			            	}
            });   
            
    var functionRadioNumeric= new Ext.form.Radio({
    						id:'function-radio-numeric-atributos-variables-objeto',
            				name:'formato',
            				labelSeparator : "",
            				boxLabel: 'Num\u00E9rico',
            				hideLabel:true,
            				//width: 10,
  	  	       				onClick: function(){			            		
            										            	
            						hideTextMaximo.enable();
            						hideTextMinimo.enable();
            						Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue("N");
				            	
			            	}
            });  
            
    var functionRadioPercent= new Ext.form.Radio({
            				id:'function-radio-percent-atributos-variables-objeto',
            				name:'formato',
            				labelSeparator : "",
            				boxLabel: 'Porcentaje',
            				//hideLabel:true,
  	  	       				onClick: function(){			            		
            					
            						hideTextMaximo.reset();
            						hideTextMinimo.reset();					            	
            						hideTextMaximo.disable();            						
				            		hideTextMinimo.disable();
				            		Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue("P");
				            
			            	}
            }); 
            
    var functionRadioDate= new Ext.form.Radio({
            				id:'function-radio-date-atributos-variables-objeto',
            				name: 'formato',
				           	boxLabel: 'Fecha',
				           	labelSeparator : "",
				            hideLabel:true,
  	  	       				onClick: function(){			            		
            					
            						hideTextMaximo.reset();
            						hideTextMinimo.reset();					            	
            						hideTextMaximo.disable();            						
				            		hideTextMinimo.disable();
				            		Ext.getCmp('hidden-radio-atributos-variables-objeto').setValue("F");
				            	
			            	}
            });                                
      
    var obligatorioCheck= new Ext.form.Checkbox({
    			id:'obligatorio-check-atributos-variables-objeto',
                name:'obligatorio',
                boxLabel: 'Obligatorio',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            		this.setRawValue("S");
				            		Ext.getCmp('hidden-form-check-complementario-objeto').hide();
				            		Ext.getCmp('hidden-form-check-complementario-obligatorio-objeto').hide();				            		
				            		Ext.getCmp('complementario-check-atributos-variables-objeto').reset();
				            		Ext.getCmp('complementario-obligatorio-check-atributos-variables-objeto').reset();
				            		Ext.getCmp('complementario-modificable-check-atributos-variables-objeto').reset();
				            		
				            	}else{
				            		Ext.getCmp('hidden-form-check-complementario-objeto').show();
				            	}
				            }
                 
            });
            
    var emisionCheck= new Ext.form.Checkbox({
    			id:'emision-check-atributos-variables-objeto',
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
    			id:'endoso-check-atributos-variables-objeto',
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
    			id:'retarificacion-check-atributos-variables-objeto',
                name:'retarificacion',
                boxLabel: 'Interviene en la retarificaci\u00F3n',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });
            
	var apareceCotizadorCheck= new Ext.form.Checkbox({
    			id:'aparece-cotizador-check-atributos-variables-objeto',
                name:'apareceCotizador',
                boxLabel: 'Aparece cotizador',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });    
            
    var complementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-check-atributos-variables-objeto',
                name:'datoComplementario',
                boxLabel: 'Dato complementario',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");
				            	Ext.getCmp('hidden-form-check-obligatorio-objeto').hide();
				          		Ext.getCmp('obligatorio-check-atributos-variables-objeto').reset();
				          		Ext.getCmp('hidden-form-check-complementario-obligatorio-objeto').show();
				            	}else{
				            		Ext.getCmp('hidden-form-check-obligatorio-objeto').show();
				            		Ext.getCmp('hidden-form-check-complementario-obligatorio-objeto').hide();
				            		Ext.getCmp('complementario-obligatorio-check-atributos-variables-objeto').reset();
				            		Ext.getCmp('complementario-modificable-check-atributos-variables-objeto').reset();
				            	}
				            }
                 
            });                 

	var obligaComplementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-obligatorio-check-atributos-variables-objeto',
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
    			id:'complementario-modificable-check-atributos-variables-objeto',
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
    			id:'aparece-endoso-check-atributos-variables-objeto',
                name:'apareceEndoso',
                boxLabel: 'Aparece en endoso',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            		this.setRawValue("S");
				            		Ext.getCmp('hidden-form-check-obligatorio-en-endoso-objeto').show();	
				            		Ext.getCmp('obligatorio-endoso-check-atributos-variables-objeto').reset();
				            		
				            	}else{
				            		Ext.getCmp('hidden-form-check-obligatorio-en-endoso-objeto').hide();
				            	}
				            }
                 
            });     
            
       var obligatorioEndosoCheck= new Ext.form.Checkbox({
    			id:'obligatorio-endoso-check-atributos-variables-objeto',
                name:'obligatorioEndoso',
                boxLabel: 'Endoso obligatorio',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });            
            
      /**Agregado sErGiO*/
   var dataStorePadreObjeto;
   var dataStoreCondicionesObjeto;
   
    dataStoreCondicionesObjeto= new Ext.data.Store({
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
 		dataStoreCondicionesObjeto.setDefaultSort('cond', 'desc');
 		dataStoreCondicionesObjeto.load();
       
    	dataStorePadreObjeto = new Ext.data.Store({
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
 		dataStorePadreObjeto.setDefaultSort('pat', 'desc');     
       	dataStorePadreObjeto.load(); 
	
	var comboCondicion = new Ext.form.ComboBox({
				id:'combo-condicion-atributos-variables-arbol-objeto',
				tpl: '<tpl for="."><div ext:qtip="{nombreCabecera}" class="x-combo-list-item">{descripcionCabecera}</div></tpl>',
    			store : dataStoreCondicionesObjeto, 
				listWidth: '250',
    			mode: 'local',
				name: 'condicion',
    			typeAhead: true,
				labelSeparator:'',
				//allowBlank:false,
    			triggerAction: 'all',    		
    			displayField:'descripcionCabecera',
				forceSelection: true,
				fieldLabel: 'Condici\u00F3n',
				emptyText:'Seleccione una Condici\u00F3n',
				selectOnFocus:true
		});     
	var comboPadre = new Ext.form.ComboBox({
				id:'combo-padre-atributos-variables-arbol-objeto',
				tpl: '<tpl for="."><div ext:qtip="{clavePadre}" class="x-combo-list-item">{descripcionPadre}</div></tpl>',
    			store: dataStorePadreObjeto, 
				listWidth: '250',
    			mode: 'local',
				name: 'padre',
    			typeAhead: true,
				labelSeparator:'',
				//allowBlank:false,
    			triggerAction: 'all',    		
    			displayField:'descripcionPadre',
				forceSelection: true,
				fieldLabel: 'Padre',
				emptyText:'Seleccione un Padre',
				selectOnFocus:true
		});
		
	var hideOrden = new Ext.form.NumberField({
      						id:'id-hidden-text-orden-objeto',
      						name:'orden',
      						fieldLabel:'Orden',
      						allowDecimals : false,
				            allowNegative : false,
        				    //allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',				
            				width:160
      	   });
      	   
	var hideAgrupador = new Ext.form.NumberField({
      						id:'id-hidden-text-agrupador-objeto',
      						name:'agrupador',
      						fieldLabel:'Agrupador',
      						allowDecimals : false,
				            allowNegative : false,
        				    //allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				width:160
      	   });     
  
 /*finish*/
       
                  
  /*          
    var cotizadorCheck= new Ext.form.Checkbox({
    			//id:'cotizador-check-atributos-variables',
                name:'despliegaCotizador',
                boxLabel: '<s:text name="productos.configAtributoVariable.cotizador"/>',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });     
    */        
            //hideTextMaximo.disable();               
            //hideTextMinimo.disable();

            var datosVariablesForm = new Ext.form.FormPanel({
            	id:'id-datos-variables-objetos-form',
            	frame:true,
            	baseCls: 'x-plain',
            	//labelAlign: 'left',
            	url:'tipoObjeto/AgregaDatosVariablesObjeto.action?edita='+edita,
            	//labelWidth: 95,
        		//title: '<s:text name="productos.configAtributoVariable.title.principal"/>',
       			//bodyStyle:'padding:5px',
        		width:615,
        		buttonAlign: "center",
                items: [{xtype:'hidden',id:'hidden-lista-valores-atributo-objeto',name:'codigoTabla'},
                		//{xtype:'hidden',id:'hidden-codigo-atributo',name:'codigoAtributo'},
                {
                	xtype:'hidden',
		            name: "claveCampo",
        		    id: "hidden-clave-campo-atributos-variables-objeto"
                },{
                	xtype:'hidden',
		            name: "descripcionHidden",
        		    id: "hidden-descripcion-atributos-variables-objeto"
                },{
                	xtype:'hidden',
		            name: "codigoRadioAtributosVariables",
        		    id: "hidden-radio-atributos-variables-objeto"
                },{
                	xtype:'hidden',
		            name: "formatoHidden",
        		    id: "hidden-formato-atributos-variables-objeto"
                },{
                	xtype:'hidden',                	
                	id:'hidden-valor-defecto-atributos-variables-obj',
		            name: "valorDefecto"      		    
        		 
                },{
                	xtype:'hidden',                	
                	id:'hidden-codigo-expresion-atributos-variables-obj',
		            name: "codigoExpresion"      		    
        		 
                },{
                	xtype:'hidden',
		            name: 'codigoExpresionSession',
        		    id: 'hidden-codigo-expresion-session-atributos-variables-objeto',
        		    value:'EXPRESION_ATRIBUTOS_VARIABLES_OBJETOS'
                },{
        		    xtype:'textfield',
		            name: "descripcion",
		            allowBlank: false,
		            blankText : 'Descripci\u00F3n Requerida',
		            id:'id-descripcion-atributos-variables-objeto',
		            maxLength: 15,
		            maxLengthText: 'M&aacute;ximo 15 caracteres',
        		    fieldLabel: 'Descripci\u00F3n*',
		            width: 150
        		},{
        			layout:'column',
        			border: false,
        			baseCls: 'x-plain',        			
            				items:[{
				                columnWidth:.4,
                				layout: 'form',
                				border: false,
                				baseCls: 'x-plain',                				
				                items: [
				                	functionRadioAlfa
				                ]
        					},{
               					columnWidth:.12,
			                	layout: 'form',
			                	border: false,
			                	baseCls: 'x-plain',			                	
               					items: [
               						functionRadioNumeric
               					]
				    		},{
               					columnWidth:.24,
			                	layout: 'form',
			                	labelAlign:"right",
			                	border: false,
			                	baseCls: 'x-plain',			                	
               					items: [
               						hideTextMinimo
               					]
				            },{
               					columnWidth:.24,
               					labelAlign:"right",
			                	layout: 'form',
			                	border: false,
			                	baseCls: 'x-plain',			                	
               					items: [               						
               						hideTextMaximo
               					]	
				    		}]
        		
		        },{
		        	layout: 'column',
				   	border: false,
				   	baseCls: 'x-plain',
				   	items: [{
                				columnWidth:.4,
				                layout:'form',
				                border: false,
				                baseCls: 'x-plain',
				                width:230,				                    	
				                items:[
				                    	functionRadioPercent
				                ]
				             },{
                				columnWidth:.6,
                				layout:'form',
                				border: false,     
                				baseCls: 'x-plain',      						
                				items:[
                						functionRadioDate
                				]
				             }
				     ]				           		
				},/*
				    obligatorioCheck,
				    emisionCheck,
				    endosoCheck,
				{
		        	layout:'form',
		        	//id:'hidden-form-attributos-coberturas',
		        	border: false,
		        	items:[		        	
        					retarifacionCheck
        					//cotizadorCheck
		        	]     		
				},*/{
        			layout:'column',
        			border: false,
			        baseCls: 'x-plain',
        			
            				items:[{
				                columnWidth:.5,
                				layout: 'form',
                				border: false,
                				baseCls: 'x-plain',
        
				                items: [
				                	{
									layout:'form',
						        	id:'hidden-form-check-obligatorio-objeto',
						        	baseCls: 'x-plain',
						        	border: false,
						        	items:[		        	
				        					obligatorioCheck
						        	]     	
									},
									{
									layout:'form',
						        	id:'form-check-emision-endoso-cotizador-objeto',
						        	baseCls: 'x-plain',
						        	border: false,
						        	items:[		        	
				        					emisionCheck,
				    						endosoCheck
						        	]     	
									},
				    				
				    				retarifacionCheck,
            						apareceCotizadorCheck
            						
            						
				                ]
        					},{
                				columnWidth:.5,
				                layout: 'form',
				                border: false,
				                baseCls: 'x-plain',
        
                				items: [
                						{
											layout:'form',
								        	id:'hidden-form-check-complementario-objeto',
								        	baseCls: 'x-plain',
								        	border: false,
								        	items:[		        	
						        					complementarioCheck
								        	]     	
										},{
											layout:'form',
								        	id:'hidden-form-check-complementario-obligatorio-objeto',
								        	baseCls: 'x-plain',
								        	border: false,
								        	items:[		        	        					
						        					obligaComplementarioCheck,
													modificableComplementarioCheck
								        	]     	
										},{
											layout: 'form',
											id: 'form-check-aparece-obligatorio-objeto',
											baseCls: 'x-plain',
											border: false,
											items: [
												apareceEndosoCheck,
												{
												layout:'form',
									        	id:'hidden-form-check-obligatorio-en-endoso-objeto',
									        	baseCls: 'x-plain',
									        	border: false,
									        	items:[		        	
							        					obligatorioEndosoCheck
									        	]     	
												}
											]
										}								
                				]
				            }]
        		
		        },{
 				   layout:'column',
 				   border: false,
 				   baseCls: 'x-plain',
            				items:[{
				                columnWidth:.58,
                				layout: 'form',
                				border: false,
                				baseCls: 'x-plain',
                				width:300,
				                items: [
				                	comboPadre,
				                	hideAgrupador,
				                	hideOrden,
				                	comboCondicion,
				                	{layout: 'form',baseCls: 'x-plain', border: false},
				                	comboWithTooltip
				                ]
				            },{
                				columnWidth:.42,
				                layout: 'form',
				                border: false,
				                baseCls: 'x-plain',
                				items: [{layout: 'form', height:80,baseCls: 'x-plain', border: false},                					
                						{xtype:'button', 
                						text: 'Condici\u00F3n', 
                						name: 'AgregarCondicion', 
                						buttonAlign: "right", 
                						handler: function() {ExpresionesVentanaCondiciones(dataStoreCondicionesObjeto);}
                						},	
                						{layout: 'form', height:20,baseCls: 'x-plain', border: false},
                						{xtype:'button', 
                						text: 'Agregar al Cat\u00E1logo', 
                						name: 'AgregarAlCatalogo', 
                						buttonAlign: "right", 
                						handler: function() {creaListasDeValores(ds);}
                					}]
				            }]
		        }]
		        });
		        
		        
		        // define window and show it in desktop
    var atributoVariableObjetosWindow = new Ext.Window({
        title: 'Datos Variables De Tipos De Objetos',
        width: 615,
        height:440,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
  
        items: datosVariablesForm,

        buttons: [{
	            	text: 'Valor por Defecto',
	            	handler:function(){
	            		if(Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').getValue()!="undefined" 
	            		&& Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').getValue()!=""
	            		&& Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').getValue()!="0"
	            		&& Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').getValue()!=null ){
							ExpresionesVentana2(Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').getValue(),Ext.getCmp('hidden-codigo-expresion-session-atributos-variables-objeto').getValue());
	            		}else{
	            			var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									//alert(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').setValue(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									ExpresionesVentana2(Ext.getCmp('hidden-valor-defecto-atributos-variables-obj').getValue(),Ext.getCmp('hidden-codigo-expresion-session-atributos-variables-objeto').getValue());
								}
					   		});            		
	            		}
	            	}	            	
    		    },{
	            	text: 'Guardar',
	            	handler: function() {                    	  
                	// check form value
	            		//alert( "validacion=" +  validaMaxMinClaveAtributoObj() );
	            		
                	if (datosVariablesForm.form.isValid()) {
                		 
                		if(  validaMaxMinClaveAtributoObj() ){
		 		        	datosVariablesForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Procesando...',
					            	failure: function(form, action) {
								    Ext.MessageBox.alert('Error ', 'Error al agregar atributos variables');
									},
									success: function(form, action) {
								    Ext.MessageBox.alert('Status', 'El Atributo Variable se ha guardado con éxito');
								    atributoVariableObjetosWindow.close();						    		
								    store.load();
						    		
						    		 //Ext.getCmp('arbol-productos').getRootNode().reload();  		
								}
			        		});
                		}
                		 
                		} else{
					Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Cancelar',
	            	handler:function(){
	            		atributoVariableObjetosWindow.close();
	            	}
    		    }]        
    });
    
    
    if(row!=null){
	    store.load({
			callback:function(){
				if(!Ext.getCmp('complementario-check-atributos-variables-objeto').getValue()) Ext.getCmp('hidden-form-check-complementario-obligatorio-objeto').hide();
	  			else Ext.getCmp('hidden-form-check-obligatorio-objeto').hide();
	    		if(!Ext.getCmp('aparece-endoso-check-atributos-variables-objeto').getValue()) Ext.getCmp('hidden-form-check-obligatorio-en-endoso-objeto').hide();
			}
		});
    }else {
				if(!Ext.getCmp('complementario-check-atributos-variables-objeto').getValue()) Ext.getCmp('hidden-form-check-complementario-obligatorio-objeto').hide();
	  			else Ext.getCmp('hidden-form-check-obligatorio-objeto').hide();
	    		if(!Ext.getCmp('aparece-endoso-check-atributos-variables-objeto').getValue()) Ext.getCmp('hidden-form-check-obligatorio-en-endoso-objeto').hide();
    }
    
    
    
    
    ds.load({params:{start:0, limit:25}});    
    atributoVariableObjetosWindow.show();	
};

