seleccionaAtributosVariables = function(store,row) {
	var edita=0;
	var formPanel;
	//alert("row"+row);
if(row!=null){
	edita=1;
    var rec = store.getAt(row);
 
   store.on('load', function(){ 
   						                        
                           
                           Ext.getCmp('atributo-rol-form').getForm().loadRecord(rec);
                           //alert("desc"+ rec.get('descripcionAtributoVariable'));
                           
                           //alert(Ext.getCmp('hidden-atributo-variable-rol').getValue());                                  
                           
                           if(rec.get('switchObligatorio')=="S"){
                				Ext.getCmp('switch-obligatorio-rol').setValue(true);
                				Ext.getCmp('switch-obligatorio-rol').setRawValue("S");
			                }else{
            			    	Ext.getCmp('switch-obligatorio-rol').setValue(false);
                				Ext.getCmp('switch-obligatorio-rol').setRawValue("N");
                			}
                           //**********
                            if(rec.get('apareceCotizador')=="S"){
                				Ext.getCmp('aparece-cotizador-check-atributos-rol').setValue(true);
                				Ext.getCmp('aparece-cotizador-check-atributos-rol').setRawValue("S");
			                }else{
            			    	Ext.getCmp('aparece-cotizador-check-atributos-rol').setValue(false);
                				Ext.getCmp('aparece-cotizador-check-atributos-rol').setRawValue("N");
                			}
                			if(rec.get('retarificacion')=="S"){
                				Ext.getCmp('retarificacion-check-atributos-rol').setValue(true);
                				Ext.getCmp('retarificacion-check-atributos-rol').setRawValue("S");
                			}else{
				            	Ext.getCmp('retarificacion-check-atributos-rol').setValue(false);
				            	Ext.getCmp('retarificacion-check-atributos-rol').setRawValue("N");
                			}
                			if(rec.get('modificaCotizador')=="S"){
                				Ext.getCmp('modificable-cotizador-check-atributos-rol').setValue(true);
                				Ext.getCmp('modificable-cotizador-check-atributos-rol').setRawValue("S");
			                }else{
            			    	Ext.getCmp('modificable-cotizador-check-atributos-rol').setValue(false);
                				Ext.getCmp('modificable-cotizador-check-atributos-rol').setRawValue("N");
                			}
                			if(rec.get('datoComplementario')=="S"){
                				Ext.getCmp('complementario-check-atributos-rol').setValue(true);
                				Ext.getCmp('complementario-check-atributos-rol').setRawValue("S");
                			}else{
                				Ext.getCmp('complementario-check-atributos-rol').setValue(false);
                				Ext.getCmp('complementario-check-atributos-rol').setRawValue("N");
                			}               
                			if(rec.get('obligatorioComplementario')=="S"){
                				Ext.getCmp('complementario-obligatorio-check-atributos-rol').setValue(true);
                				Ext.getCmp('complementario-obligatorio-check-atributos-rol').setRawValue("S");
                			}else{
                				Ext.getCmp('complementario-obligatorio-check-atributos-rol').setValue(false);
                				Ext.getCmp('complementario-obligatorio-check-atributos-rol').setRawValue("N");
                			}
                			if(rec.get('modificableComplementario')=="S"){
                				Ext.getCmp('complementario-modificable-check-atributos-rol').setValue(true);
                				Ext.getCmp('complementario-modificable-check-atributos-rol').setRawValue("S");
                			}else{
                				Ext.getCmp('complementario-modificable-check-atributos-rol').setValue(false);
                				Ext.getCmp('complementario-modificable-check-atributos-rol').setRawValue("N");
                			}
                			if(rec.get('apareceEndoso')=="S"){
                				Ext.getCmp('aparece-endoso-check-atributos-rol').setValue(true);
                				Ext.getCmp('aparece-endoso-check-atributos-rol').setRawValue("S");
                			}else{
                				Ext.getCmp('aparece-endoso-check-atributos-rol').setValue(false);
                				Ext.getCmp('aparece-endoso-check-atributos-rol').setRawValue("N");
                			}
                			if(rec.get('obligatorioEndoso')=="S"){
                				Ext.getCmp('obligatorio-endoso-check-atributos-rol').setValue(true);
                				Ext.getCmp('obligatorio-endoso-check-atributos-rol').setRawValue("S");
                			}else{
                				Ext.getCmp('obligatorio-endoso-check-atributos-rol').setValue(false);
                				Ext.getCmp('obligatorio-endoso-check-atributos-rol').setRawValue("N");
                			}
                            if(rec.get('modificaEndoso')=="S"){
                				Ext.getCmp('modificable-endoso-check-atributos-rol').setValue(true);
                				Ext.getCmp('modificable-endoso-check-atributos-rol').setRawValue("S");
                			}else{
                				Ext.getCmp('modificable-endoso-check-atributos-rol').setValue(false);
                				Ext.getCmp('modificable-endoso-check-atributos-rol').setRawValue("N");
                			}                			      			                			
                			if(rec.get('dsPadre')==""){
                				Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-rol').hide();
    		            		Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton-rol').hide(); 
    		            		Ext.getCmp('forma-boton-vacia-rol').hide(); 
    		            	
    		            		Ext.getCmp('combo-condicion-atributos-variables-rol').reset();
    		            		Ext.getCmp('id-hidden-text-orden-rol').reset();
    		            		Ext.getCmp('id-hidden-text-agrupador-rol').reset();
                			}else{
                				Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-rol').show();
    		            		Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton-rol').show();
    		            		Ext.getCmp('forma-boton-vacia-rol').show();
    		            		Ext.getCmp('combo-padre-atributos-variables-rol').setValue(rec.get('dsPadre'));
    		            		Ext.getCmp('combo-condicion-atributos-variables-rol').setValue(rec.get('dsCondicion'));
                			}
                           //**********
                           Ext.getCmp('hidden-atributo-variable-rol').setValue(rec.get('codigoAtributoVariable'));
                           Ext.getCmp('combo-atributo-variable-rol').disable();
                           Ext.getCmp('hide-button').hide();
                           Ext.getCmp('hidden-edita-rol').setValue(edita);
                           dsCatalogoAtributos.load();
                           ds.load();
     					   dataStoreCondiciones.load();
     					   dataStorePadre.load();
                           });

                                                 
}
    // pre-define fields in the form
	var dsCatalogoAtributos = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url:'rol/CatalogoDeAtributosVariables.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoAtributosVariables',
            id: 'comboAtributosVariables'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}       
        ]),
		remoteSort: true
    });
    dsCatalogoAtributos.load(); 
	
	 var atributosVariablesCombo = new Ext.form.ComboBox({
    		tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
	 		store: dsCatalogoAtributos,
			displayField:'value',
			id:'combo-atributo-variable-rol',
			listWidth: '230',
			valueField: 'key',	    				
			typeAhead: true,
	    	mode: 'local',
			triggerAction: 'all',
			emptyText:'Seleccione un atributo variable',
    		selectOnFocus:true,
   			fieldLabel: 'Atributos Variables',
   			allowBlank:false,
   			blankText : 'Atributo variable requerido.',
			name:"descripcionAtributoVariable",
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
				var valor=record.get('key');
				Ext.getCmp('hidden-atributo-variable-rol').setValue(valor);
			}
    			    			
	}); 
	
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
		remoteSort: true
    });
       ds.setDefaultSort('valoresDeAtributos', 'desc'); 
       ds.load();
       
	var comboWithTooltip = new Ext.form.ComboBox({
		id:'id-descripcion-tabla-combo',
    	tpl: '<tpl for="."><div ext:qtip="{codigoTabla}" class="x-combo-list-item">{descripcionTabla}</div></tpl>',
    	store: ds,
    	displayField:'descripcionTabla',
    	valueField:'codigoTabla',
    	//allowBlank:false,
    	//blankText : '<s:text name="productos.configRoles.listaValor.req"/>',
    	//maxLength : '30',
    	//maxLengthText : 'Treinta caracteres maximo',
    	typeAhead: true,
    	mode: 'local',
    	triggerAction: 'all',
    	emptyText:'Seleccione un valor',
    	selectOnFocus:true,
    	fieldLabel: 'Lista de Valores',
    	listWidth: 200,
    	name:"descripcionListaDeValores",
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
			var valor=record.get('codigoTabla');
			Ext.getCmp('hidden-lista-valores-rol').setValue(valor);
		}
	});                
	
	
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
       
    dataStoreCondiciones.load();
	var comboCondicion = new Ext.form.ComboBox({
				id:'combo-condicion-atributos-variables-rol',
				tpl: '<tpl for="."><div ext:qtip="{nombreCabecera}" class="x-combo-list-item">{descripcionCabecera}</div></tpl>',
    			store : dataStoreCondiciones, 
				listWidth: '250',
    			mode: 'local',
				name: 'dsCondicion',
				hiddenName:'condicion',
    			typeAhead: true,
				//labelSeparator:'',
				//allowBlank:false,
    			triggerAction: 'all',    		
    			displayField:'descripcionCabecera',
    			valueField:'nombreCabecera',
				//forceSelection: true,
				fieldLabel: 'Condici\u00F3n',
				emptyText:'Seleccione una Condici\u00F3n',
				selectOnFocus:true
				
		});     
	
	var	dataStorePadre = new Ext.data.Store({
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
       	dataStorePadre.load(); 
 		
	var comboPadre = new Ext.form.ComboBox({
				id:'combo-padre-atributos-variables-rol',
				tpl: '<tpl for="."><div ext:qtip="{clavePadre}" class="x-combo-list-item">{descripcionPadre}</div></tpl>',
    			store: dataStorePadre, 
				listWidth: '250',
    			mode: 'local',
				name: 'dsPadre',
				hiddenName: 'padre',
    			typeAhead: true,
				//labelSeparator:'',
				//allowBlank:false,
    			triggerAction: 'all',    		
    			displayField:'descripcionPadre',
    			valueField:'clavePadre',
				//forceSelection: true,
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
				var seccion = Ext.getCmp('combo-padre-atributos-variables-rol').getValue();
	    		
	              		if(seccion.length==0 || seccion == 'Seleccione un Padre'){
    		            	Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-rol').hide();
    		            	Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton-rol').hide(); 
    		            	Ext.getCmp('forma-boton-vacia-rol').hide(); 
    		            	
    		            	Ext.getCmp('combo-condicion-atributos-variables-rol').reset();
    		            	Ext.getCmp('id-hidden-text-orden-rol').reset();
    		            	Ext.getCmp('id-hidden-text-agrupador-rol').reset();
	               		}else{
	               			Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-rol').show();
    		            	Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton-rol').show();
    		            	Ext.getCmp('forma-boton-vacia-rol').show();
    		            	
    		            	
	               		}
		}
	
		
	var hideOrden = new Ext.form.NumberField({
      						id:'id-hidden-text-orden-rol',
      						name:'orden',
      						fieldLabel:'Orden',
      						allowDecimals : false,
				            allowNegative : false,
        				    //allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',				
            				width:160
    });
      	   
    var hideAgrupador = new Ext.form.NumberField({
      						id:'id-hidden-text-agrupador-rol',
      						name:'agrupador',
      						fieldLabel:'Agrupador',
      						allowDecimals : false,
				            allowNegative : false,
        				    //allowBlank: false,
        				    blankText : 'Dato Num\u00E9rico Requerido',            				
            				width:160
    });
	
	
	var obligatorioCheck = new Ext.form.Checkbox({
        //fieldLabel: '<s:text name="productos.configRoles.oblig"/>',
        boxLabel: 'Obligatorio',
        hideLabel:true,	 
        checked:false,       
        id:'switch-obligatorio-rol',
        name: 'obligatorio',
        onClick:function(){
					if(this.getValue()){
				            this.setRawValue("S");
				            Ext.getCmp('hidden-form-check-complementario-roles').hide();
				            Ext.getCmp('hidden-form-check-complementario-obligatorio-roles').hide();				            		
				            Ext.getCmp('complementario-check-atributos-rol').reset();
				            Ext.getCmp('complementario-obligatorio-check-atributos-rol').reset();
				            Ext.getCmp('complementario-modificable-check-atributos-rol').reset();
				            		
				    }else{
				       Ext.getCmp('hidden-form-check-complementario-roles').show();
				    }
		}
    });  
    
    //**************
    var apareceCotizadorCheck= new Ext.form.Checkbox({
    			id:'aparece-cotizador-check-atributos-rol',
                name:'apareceCotizador',
                boxLabel: 'Aparece en cotizador',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });
            
     var retarifacionCheck= new Ext.form.Checkbox({
	    			id:'retarificacion-check-atributos-rol',
	                name:'retarificacion',
	                boxLabel:'Interviene en la Tarificaci\u00F3n /<br/> &nbsp;&nbsp;&nbsp;&nbsp; Retarificaci\u00F3n',
	            	hideLabel:true,	
	                checked:false,
	                onClick:function(){
					            	if(this.getValue()){
					            	this.setRawValue("S");				            	
					            	}
					            }
	                 
      });      
            
    var modificableCotizadorCheck= new Ext.form.Checkbox({
    			id:'modificable-cotizador-check-atributos-rol',
                name:'modificaCotizador',
                boxLabel: 'Cotizador modificable',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });        
            
    var complementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-check-atributos-rol',
                name:'datoComplementario',
                boxLabel: 'Dato complementario',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");
				            	Ext.getCmp('hidden-form-check-obligatorio-roles').hide();
				          		Ext.getCmp('switch-obligatorio-rol').reset();
				          		Ext.getCmp('hidden-form-check-complementario-obligatorio-roles').show();
				            	}else{
				            		Ext.getCmp('hidden-form-check-obligatorio-roles').show();
				            		Ext.getCmp('hidden-form-check-complementario-obligatorio-roles').hide();
				            		Ext.getCmp('complementario-obligatorio-check-atributos-rol').reset();
				            		Ext.getCmp('complementario-modificable-check-atributos-rol').reset();
				            	}
				}
                 
            });                 

	var obligaComplementarioCheck= new Ext.form.Checkbox({
    			id:'complementario-obligatorio-check-atributos-rol',
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
    			id:'complementario-modificable-check-atributos-rol',
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
    			id:'aparece-endoso-check-atributos-rol',
                name:'apareceEndoso',
                boxLabel: 'Aparece en endoso',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            		this.setRawValue("S");
				            		Ext.getCmp('hidden-form-check-obligatorio-en-endoso-roles').show();	
				            		Ext.getCmp('obligatorio-endoso-check-atributos-rol').reset();
				            		
				            	}else{
				            		Ext.getCmp('hidden-form-check-obligatorio-en-endoso-roles').hide();
				            	}
				            }
                 
            });     
            
       var obligatorioEndosoCheck= new Ext.form.Checkbox({
    			id:'obligatorio-endoso-check-atributos-rol',
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

		var modificableEndosoCheck= new Ext.form.Checkbox({
    			id:'modificable-endoso-check-atributos-rol',
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
            
    //**************    
    	formPanel = new Ext.form.FormPanel({
  		
  		frame:true,
        baseCls: 'x-plain',
        labelWidth: 115,
        id: 'atributo-rol-form',
        //bodyStyle:'padding:5px',
        url:'rol/AgregaAtributoVariableRol.action',
        //defaultType: 'textfield',
        //collapsed : false,
		items: [{xtype:'hidden',id:'hidden-atributo-variable-rol',name:'codigoAtributoVariable'},
				{xtype:'hidden',id:'hidden-lista-valores-rol',name:'codigoListaDeValores'},
				{xtype:'hidden',id:'hidden-codigo-expresion-rol-ventana',name:'codigoExpresion'},
				{xtype:'hidden',id:'hidden-codigo-expresion-session-rol-ventana',name:'codigoExpresionSession', value:'EXPRESION_ATRIBUTO_VARIABLE_ROL'},
				{xtype:'hidden',id:'hidden-edita-rol',name:'edita'},
				{
        			layout:'column',
        			border: false,
			        baseCls: 'x-plain',
        			
            				items:[{
				                columnWidth:.7,
                				layout: 'form',
                				border: false,
                				baseCls: 'x-plain',
        
				                items: [
				                	atributosVariablesCombo
				                ]
        					},{
                				columnWidth:.3,
				                layout: 'form',
				                border: false,
				                baseCls: 'x-plain',
        
                				items: [{
				                    xtype:'button',
				                    text: 'Agregar al cat\u00E1logo',
                				    //name: 'AgregarAlCatalogo',
				                    buttonAlign: "right",
                				    id:'hide-button',
            						handler: function(){
            							creaAtributosVariables(dsCatalogoAtributos);
            						}	
				                }]
				            }]
        		
		        },
					{
        			layout:'column',
        			border: false,
			        baseCls: 'x-plain',
        			
            				items:[{
				                columnWidth:.7,
                				layout: 'form',
                				border: false,
                				baseCls: 'x-plain',
        
				                items: [
				                	comboWithTooltip
				                ]
        					},{
                				columnWidth:.3,
				                layout: 'form',
				                border: false,
				                baseCls: 'x-plain',
        
                				items: [{
				                    xtype:'button',
				                    text: 'Agregar al cat\u00E1logo',
				                    width:30,
				                    id:'hide-button-lista',
                				    //name: 'AgregarAlCatalogo',
				                    buttonAlign: "right",
                				    handler: function() {                    	
				             			creaListasDeValores(ds);//window listasDeValores
               			   				
         							}		
				                }]
				            }]
        		
		        },{
 				   layout:'column',
 				   border: false,
 				   baseCls: 'x-plain',
            				items:[{
				                	columnWidth:.7,
                					layout: 'form',
                					baseCls: 'x-plain',
                					border: false,
                					//width:300,
				                	items: [{
				                			layout: 'form',
				                			baseCls: 'x-plain',
				                			id:'hidden-forma-padre-rol',
                							border: false,
				                				items:[
				                					comboPadre				                					
				                				]
				                			},{
				                			layout: 'form',
				                			baseCls: 'x-plain',
				                			id:'hidden-forma-padre-condicion-orden-agrupador-rol',
                							border: false,
				                				items:[
				                					//comboPadre,
				                					hideAgrupador,
				                					hideOrden,
				                					comboCondicion
				                				]
				                			}
				                	]
				            },{
                				columnWidth:.3,
                				baseCls: 'x-plain',
				                layout: 'form',
				                //heigth:200,
				                border: false,
				                items:[{
				                		layout: 'form',
				                		baseCls: 'x-plain',
				                		id:'hidden-forma-padre-condicion-orden-agrupador-boton-rol',
                						border: false,				            
                						items: [
                								{layout: 'form', height:80, border: false,id:'forma-boton-vacia-rol',baseCls: 'x-plain'},		
                								{xtype:'button', 
                								text: 'Agregar Condici\u00F3n ', 
                								name: 'AgregarCondicion', 
                								buttonAlign: "right", 
                								handler: function() {
                									var connect = new Ext.data.Connection();
					    							connect.request ({
														url:'atributosVariables/ObtenerCodigoExpresion.action',
														callback: function (options, success, response) {				   
															codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
															ExpresionesVentana2(codigoExpresion, "EXPRESION", dataStoreCondiciones, '3');
														}
											   		});
                								}
                						}]
                				}]		
				                
				            }]
		        },{
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
						        	id:'hidden-form-check-obligatorio-roles',
						        	baseCls: 'x-plain',
						        	border: false,
						        	items:[		        	
				        					obligatorioCheck
						        	]     	
									},
									{
									layout:'form',
						        	id:'form-check-emision-endoso-cotizador-roles',
						        	baseCls: 'x-plain',
						        	border: false,
						        	items:[		        	
				    						modificableEndosoCheck
						        	]     	
									},
            						apareceCotizadorCheck,
            						retarifacionCheck,
            						modificableCotizadorCheck,
            						{
											layout:'form',
								        	id:'hidden-form-check-complementario-roles',
								        	baseCls: 'x-plain',
								        	border: false,
								        	items:[		        	
						        					complementarioCheck
								        	]     	
									}
									
				                ]
        					},{
                				columnWidth:.5,
				                layout: 'form',
				                border: false,
				                baseCls: 'x-plain',
        
                				items: [
                					{
											layout:'form',
								        	id:'hidden-form-check-complementario-obligatorio-roles',
								        	baseCls: 'x-plain',
								        	border: false,
								        	items:[		        	        					
						        					obligaComplementarioCheck,
													modificableComplementarioCheck
								        	]     	
									},
									apareceEndosoCheck,
									{
										layout:'form',
									    id:'hidden-form-check-obligatorio-en-endoso-roles',
									    baseCls: 'x-plain',
									    border: false,
									    items:[		        	
							        			obligatorioEndosoCheck
									          ]     	
									}
									
									
                				]
				            }]
        		
		        }
		        /*
            obligatorioCheck,
            apareceCotizadorCheck,
            modificableCotizadorCheck,
            complementarioCheck,
			obligaComplementarioCheck,
			modificableComplementarioCheck,
			apareceEndosoCheck,
			obligatorioEndosoCheck,
			modificableEndosoCheck*/
    	]
    });

    // define window and show it in desktop
    var atributosWindow = new Ext.Window({
        title: 'Atributos Variables',
        width: 465,
        height:330,
        //minWidth: 300,
        //minHeight: 150,
        layout: 'fit',
        plain:true,
        //bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
  
        items: formPanel,
        

		buttons: [{
            text: 'Guardar', 
            handler: function() {
                // check form value 
                if (formPanel.form.isValid()) {
                //Ext.getCmp('hidden-lista-valores-rol').setValue(Ext.getCmp('id-descripcion-tabla-combo').getValue());
                //Ext.getCmp('hidden-atributo-variable-rol').setValue(Ext.getCmp('combo-atributo-variable').getValue());
                //alert('rol/AgregaAtributoVariableRol.action?edita='+edita);
                	Ext.getCmp('hidden-edita-rol').setValue(edita);
	 		        formPanel.form.submit({		
	 		        	url:'rol/AgregaAtributoVariableRol.action',
			            waitTitle:'Espere',
					    waitMsg:'Procesando...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Atributo variable no agregado');
						},
						success: function(form, action) {
						    //Ext.MessageBox.alert('Status', 'Atributo Variable agregado');
						    store.load();
						    atributosWindow.close();
						    
						    
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar los campos requeridos.');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){atributosWindow.close();}
        },{
            text:'Valores por Defecto Rol',
            tooltip:'Valor por defecto del rol',
            handler:function(){
            	if(Ext.getCmp('hidden-codigo-expresion-rol-ventana').getValue()!=null 
	            		&& Ext.getCmp('hidden-codigo-expresion-rol-ventana').getValue()!=""
	            		&& Ext.getCmp('hidden-codigo-expresion-rol-ventana').getValue()!="0"
	            		&& Ext.getCmp('hidden-codigo-expresion-rol-ventana').getValue()!="undefined"){
	            			ExpresionesVentana2(Ext.getCmp('hidden-codigo-expresion-rol-ventana').getValue(), Ext.getCmp('hidden-codigo-expresion-session-rol-ventana').getValue());
	            		}else{
	            			var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									//alert(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									Ext.getCmp('hidden-codigo-expresion-rol-ventana').setValue(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									ExpresionesVentana2(Ext.getCmp('hidden-codigo-expresion-rol-ventana').getValue(),Ext.getCmp('hidden-codigo-expresion-session-rol-ventana').getValue());
								}
					   		});
	            			
	            				            		
	            		}
            }
        }]
    });
    
    
    if(row!=null){
	    store.load({
			callback:function(){
				if(!Ext.getCmp('complementario-check-atributos-rol').getValue()) Ext.getCmp('hidden-form-check-complementario-obligatorio-roles').hide();
	  			else Ext.getCmp('hidden-form-check-obligatorio-roles').hide();
	    		if(!Ext.getCmp('aparece-endoso-check-atributos-rol').getValue()) Ext.getCmp('hidden-form-check-obligatorio-en-endoso-roles').hide();
			}
		});
    }else {
				if(!Ext.getCmp('complementario-check-atributos-rol').getValue()) Ext.getCmp('hidden-form-check-complementario-obligatorio-roles').hide();
					else Ext.getCmp('hidden-form-check-obligatorio-roles').hide();
				if(!Ext.getCmp('aparece-endoso-check-atributos-rol').getValue()) Ext.getCmp('hidden-form-check-obligatorio-en-endoso-roles').hide();
    }
    
	Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-rol').hide();
	Ext.getCmp('hidden-forma-padre-condicion-orden-agrupador-boton-rol').hide();
	atributosWindow.show();
};
