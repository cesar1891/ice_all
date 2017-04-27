AgregaSumaAseguradaInciso= function(claveCoberturaSuma,descripcionCoberturaSuma,storeSumaAseguradaInciso,row) {
var sumaAseguradaIncisoForm;
  

   
if(row!=null){
	  
   var rec = storeSumaAseguradaInciso.getAt(row);
   storeSumaAseguradaInciso.on('load', function(){ 
   						   Ext.getCmp('suma-asegurada-inciso-form').getForm().loadRecord(rec);
                                                                                                
                           Ext.getCmp('hidden-combo-lista-valor-suma-asegurada').setValue(rec.get('codigoListaDeValores'));
                           Ext.getCmp('hidden-combo-tipo-suma-asegurada').setValue(rec.get('codigoTipoCapital'));
                           Ext.getCmp('hidden-combo-suma-asegurada-leyenda').setValue(rec.get('codigoLeyenda'));
                           Ext.getCmp('hidden-codigo-capital').setValue(rec.get('codigoCapital'));
                           
                           if(rec.get('switchReinstalacion')=='S'){
                           Ext.getCmp('reinstalacion-check').setValue(true);
                           Ext.getCmp('reinstalacion-check').setRawValue('S');
                           }else{
                           	Ext.getCmp('reinstalacion-check').setValue(false);
                           }
                           
                           dsTipoSumaAsegurada.load();
                           ds.load();
                           dsLeyenda.load();
						
                           });
	storeSumaAseguradaInciso.load();                               
}


//******* nivel de inciso***************

//**************************************************************	
/*var dsSumaAsegurada = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'sumaAsegurada/CatalogoTipoSumaAsegurada.action'+'?catalogo='+'sumaAsegurada'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoSumaAsegurada',
            id: 'comboSumaAsegurada'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsSumaAsegurada.setDefaultSort('comboSumaAsegurada', 'desc'); 
  	dsSumaAsegurada.load();
  	
var comboSumaAsegurada =new Ext.form.ComboBox({
    	                    id:'combo-inciso-suma-asegurada',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsSumaAsegurada,
							//disabled:true,
							typeAhead: true,
							width:180,
							listWidth: '250',
							labelAlign: 'left',	
							//anchor: '90%', 					
						    displayField:'value',
						    allowBlank:false,
						    blankText : '<s:text name="productos.configIncisoSumaAsegurada.sumaAsegurada.req"/>',
						    valueField: 'key',					    	
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'<s:text name="productos.configIncisoSumaAsegurada.sumaAsegurada.sel"/>',
					    	selectOnFocus:true,
						    fieldLabel: '<s:text name="productos.configIncisoSumaAsegurada.sumaAsegurada"/>',
						    labelSeparator:'',
					    	name:"descripcionSumaAsegurada",
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
								var valor=record.get('key');
								Ext.getCmp('hidden-combo-suma-asegurada').setValue(valor);
							}			    	
			});
*/
var dsTipoSumaAsegurada = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'sumaAsegurada/CatalogoTipoSumaAsegurada.action'+'?catalogo='+'tipoSumaAsegurada'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoTipoSumaAsegurada',
            id: 'tipoSumaAsegurada'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsTipoSumaAsegurada.setDefaultSort('tipoSumaAsegurada', 'desc'); 
  	dsTipoSumaAsegurada.load();
  	
var TipoSumaAsegurada =new Ext.form.ComboBox({
							id:'combo-tipo-suma-asegurada',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsTipoSumaAsegurada,						
						    displayField:'value',
						    width:180,
						    listWidth: '250',
						    allowBlank: false,
						    blankText : 'Tipo de suma asegurada requerido.',
						    valueField: 'key',					    	
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText: 'Seleccione suma asegurada.',
					    	selectOnFocus:true,
						    fieldLabel: 'Tipo de Suma Asegurada',
						    labelSeparator:'',
					    	name:"descripcionTipoCapital",
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
								var valor=record.get('key');
								Ext.getCmp('hidden-combo-tipo-suma-asegurada').setValue(valor);
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
           {name: 'cdTabla', type: 'string',mapping:'cdTabla'},
           {name: 'dsTabla', type: 'string',mapping:'dsTabla'}    
        ]),
		remoteSort: true
    });
       ds.setDefaultSort('valoresDeAtributos', 'desc'); 
       ds.load();
      
    var comboWithTooltip = new Ext.form.ComboBox({
		id:'id-descripcion-tabla-combo-sumaAsegurada',
    	tpl: '<tpl for="."><div ext:qtip="{cdTabla}" class="x-combo-list-item">{dsTabla}</div></tpl>',
    	store: ds,
    	displayField:'dsTabla',
    	width:180,
    	valueField:'cdTabla',
    	//allowBlank:false,
    	//blankText : 'Valor requerido.',
    	//maxLength : '30',
    	//maxLengthText : 'Treinta caracteres maximo',
    	typeAhead: true,
    	mode: 'local',
    	triggerAction: 'all',
    	emptyText:'Seleccione un valor',
    	selectOnFocus:true,
    	fieldLabel: 'Lista de Valores',
    	labelSeparator:'',
    	listWidth: 200,
    	name:"descripcionListaValor",
		onSelect : function(record, index, skipCollapse){
       		if(this.fireEvent('beforeselect', this, record, index) !== false){
       			this.setValue(record.data[this.valueField || this.displayField]);
       			if( !skipCollapse ) {
           			this.collapse();
       			}
       			this.lastSelectedIndex = index + 1;
       			this.fireEvent('select', this, record, index);
      		}
			var valorCodLista=record.get('cdTabla');
			Ext.getCmp('hidden-combo-lista-valor-suma-asegurada').setValue(valorCodLista);
		}
	});                


var dsLeyenda = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'sumaAsegurada/CatalogoTipoSumaAsegurada.action'+'?catalogo='+'leyenda'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoLeyenda',
            id: 'comboLeyenda'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsLeyenda.setDefaultSort('comboLeyenda', 'desc'); 
  	dsLeyenda.load();

var comboLeyenda =new Ext.form.ComboBox({
    	                    id:'combo-cobertura-suma-asegurada-leyenda',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsLeyenda,
							//disabled:true,
							typeAhead: true,
							width:180,
							listWidth: '250',
							//labelAlign: 'left',	
							//anchor: '90%', 					
						    displayField:'value',
						    //allowBlank:true,
						    //blankText : 'Leyenda',
						    valueField: 'key',					    	
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'Seleccione leyenda',
					    	selectOnFocus:true,
						    fieldLabel: 'Leyenda',
						    //labelSeparator:'',
					    	name:"descripcionLeyenda",
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
								var valorCodLeyenda=record.get('key');
								Ext.getCmp('hidden-combo-suma-asegurada-leyenda').setValue(valorCodLeyenda);
							}			    	
			});
			

/*var valorDefectoCheck= new Ext.form.Checkbox({
				id:"valor-por-defecto-suma-asegurada",
                name:'valorDefecto',
                boxLabel: '<s:text name="productos.configProductoSumaAsegurada.valorDefecto"/>',
            	hideLabel:true,	
                //checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");
				            	Ext.getCmp('id-descripcion-tabla-combo-sumaAsegurada').reset();
				            	Ext.getCmp('id-descripcion-tabla-combo-sumaAsegurada').disable();
				            	Ext.getCmp('id-boton-valor-defecto-suma-asegurada').enable();
				            	
				            	}if(!this.getValue()){
				            	Ext.getCmp('id-descripcion-tabla-combo-sumaAsegurada').enable();
				            	Ext.getCmp('id-boton-valor-defecto-suma-asegurada').disable();		            	
				            	
				            	}
				            }
                 
            });
           
var obligatorioCheck= new Ext.form.Checkbox({
                id:'obligatorio-suma-asegurada',
                name:'obligatorio',
                boxLabel: '<s:text name="productos.configProductoSumaAsegurada.obligatorio"/>',
            	hideLabel:true,			
                //checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });  
            
var insertaCheck= new Ext.form.Checkbox({
                id: 'inserta-suma-asegurada',
                name:'inserta',
                boxLabel: '<s:text name="productos.configProductoSumaAsegurada.inserta"/>',
            	hideLabel:true,			
                //checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            					            	
				            	}
				            }
                 
            });
*/ 

var leyenda = new Ext.form.Checkbox({
        boxLabel: 'Usa leyenda?',
        hideLabel: true,
        labelSeparator:'',
        id:'leyenda-check',
        name: 'leyenda',            
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");
				            		/*Ext.getCmp('id-texto-leyenda').enable();
									Ext.getCmp('leyenda-cotizacion-check').enable();
									Ext.getCmp('leyenda-caratula-check').enable();
									*/
				            	}/*else{
				            		Ext.getCmp('id-texto-leyenda').reset();
				            		Ext.getCmp('leyenda-cotizacion-check').reset();	
				            		Ext.getCmp('leyenda-caratula-check').reset();		
				            		Ext.getCmp('id-texto-leyenda').disable();
									Ext.getCmp('leyenda-cotizacion-check').disable();
									Ext.getCmp('leyenda-caratula-check').disable();
													            	
				            	}*/
				            }
          
});  
           
var reinstalacion = new Ext.form.Checkbox({
        boxLabel: 'Reinstalaci\u00F3n autom\u00E1tica?',
        hideLabel: true,
        labelSeparator:'',
        id:'reinstalacion-check',
        name: 'switchReinstalacion',            
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
          
});  
   
var revalorizaciones = new Ext.form.Checkbox({
        boxLabel: 'Revalorizaciones?',
        hideLabel: true,
        labelSeparator:'',
        id:'revalorizacion-check',
        name: 'revalorizacion',                
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");	
				            		//Ext.getCmp('hide-button-revalorizacion').enable();
				            	}/*else{
				            		Ext.getCmp('hide-button-revalorizacion').disable();			            					            	
				            	}*/
				            }
          
});  

var claveCobertura= new Ext.form.TextField({
    		 				//hideLabel: true,
    		 				id:'id-clave-cobertura-suma-asegurada',												                    
							fieldLabel: 'Cobertura',
							name: 'claveCobertura',
							disabled: true,
							labelSeparator:'',
							width:65
							//allowBlank:false,
							//blankText : '<s:text name="dsRamo.requerido"/>',
							//maxLength:'30'
    		 });

var descripcionCobertura= new Ext.form.TextField({
							id:'id-descripcion-cobertura-suma-asegurada',
    		 				hideLabel: true,
    		 				disabled: true,												                    							
							name: 'descripcionCobertura',
							labelSeparator:'',
							width:200
							
    		 });
 Ext.getCmp('id-clave-cobertura-suma-asegurada').setValue(claveCoberturaSuma);
 Ext.getCmp('id-descripcion-cobertura-suma-asegurada').setValue(descripcionCoberturaSuma);
    		 
    		 
var leyendaText= new Ext.form.TextField({
    		 				hideLabel: true,
    		 				id:'id-texto-leyenda',												                    							
							name: 'leyendaText',
							labelSeparator:'',
							width:180
							
    		 });    		 

var leyendaCotizacion = new Ext.form.Checkbox({
        boxLabel: 'Leyenda en cotizaci\u00F3n?',
        hideLabel: true,
        labelSeparator:'',
        id:'leyenda-cotizacion-check',
        name: 'leyendaCotizacion',                
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
          
});  

var leyendaCaratula = new Ext.form.Checkbox({
        boxLabel: 'Leyenda en car\u00E1tula?',
        hideLabel: true,
        labelSeparator:'',
        id:'leyenda-caratula-check',
        name: 'leyendaCaratula',                
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
          
});  

//************FORMA****************
	 sumaAseguradaIncisoForm = new Ext.form.FormPanel({
            	id:'suma-asegurada-inciso-form',
            	baseCls: 'x-plain',
            	//boder:false,
            	frame:true,
            	labelAlign: 'left',
            	url:'sumaAsegurada/AgregaSumaAsegurada.action?nivel=inciso',
            	//labelWidth: 100,
        		//title: '<s:text name="productos.configSumaAsegurada.titulo"/>',
       			//bodyStyle:'padding:5px',
        		width:300,
        		//height:250, 
        		buttonAlign: "center",
                items: [{xtype:'hidden',id:'hidden-codigo-capital',name:'codigoCapital'},
                		{xtype:'hidden',id:'hidden-descripcion-capital',name:'descripcionCapital',value:descripcionCoberturaSuma},
                		{xtype:'hidden',id:'hidden-combo-lista-valor-suma-asegurada',name:'codigoListaValor'},
                		{xtype:'hidden',id:'hidden-combo-tipo-suma-asegurada',name:'codigoTipoCapital'},
                		{xtype:'hidden',id:'hidden-combo-suma-asegurada-leyenda',name:'codigoLeyenda'},                		
                		{xtype:'hidden',id:'hidden-valor-defecto-suma-asegurada-cobertura',name:'codigoExpresion'},
                		               		
                		{
                		layout:'form',
                		baseCls: 'x-plain',                		                		
                		border: false,
                		labelAlign:'left',
                		items:[{
            				layout:'column',
            				baseCls: 'x-plain',
            				border:false,
            				//labelAlign:'top',
            				items:[
            				{
            					columnWidth:.45,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
                				//width:195,
				            	items: [
				                	claveCobertura
				                ]
            				},{
					            columnWidth:.55,
    	            			layout: 'form',
    	            			baseCls: 'x-plain',
        	        			border: false,
        	        			//width:195,
					            items: [
					               	descripcionCobertura
				    	        ]
        					}]
        				},
        					TipoSumaAsegurada,
        					////
        					{
		        				layout:'column',
            					baseCls: 'x-plain',
            					border:false,
		        				items:[{
			        				columnWidth:.70,
    			    				layout:'form',
    			    				baseCls: 'x-plain',
        							border:false,
        							width:280,
        							items:[comboWithTooltip]       
			    	    		},{
	    	    					columnWidth:.30,
	        						layout:'form',
	        						baseCls: 'x-plain',
	        						border:false,
		        					items:[
			        					{
		        							xtype:'button', 
                							text: 'Agregar al Cat\u00E1logo', 
                							name: 'AgregarAlCatalogo', 
                							buttonAlign: "right", 
                							handler: function() {
	                							creaListasDeValores(ds);
                							}
                					}]
			    	    		}]
        					},
        					//////comboWithTooltip,
        					
        					comboLeyenda,
/*				    	 	leyenda,
				    	{
            				layout:'column',
            				baseCls: 'x-plain',
            				border:false,
            				//labelAlign:'top',
            				items:[
            				{
            					columnWidth:.5,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
                				width:195,
				            	items: [
				                	leyendaText
				                	
				                ]
            				},{
					            columnWidth:.5,
    	            			layout: 'form',
    	            			baseCls: 'x-plain',
        	        			border: false,
        	        			width:195,
					            items: [	
					            	leyendaCotizacion,				            	
					               	leyendaCaratula
					            ]
        					}]
        				},
*/        				
				    	 	reinstalacion
/*				    	{
            				layout:'column',
            				baseCls: 'x-plain',
            				border:false,
            				//labelAlign:'top',
            				items:[
            				{
            					columnWidth:.5,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
                				width:195,
				            	items: [
				                	revalorizaciones
				                ]
            				},{
					            columnWidth:.5,
    	            			layout: 'form',
    	            			baseCls: 'x-plain',
        	        			border: false,
        	        			width:195,
					            items: [{
				                    xtype:'button',
				                    text: '<s:text name="productos.configSumaAsegurada.btn.revalorizacion"/>',                				    
				                    buttonAlign: "right",
                				    id:'hide-button-revalorizacion'
            						//handler: function(){
            						//	creaAtributosVariables(dsCatalogoAtributos);
            						//}	
				                }]
        					}]
        				}         
*/				    	 	
				    ]      		
			   	}]
			});
		    
		    // define window and show it in desktop
    var sumaAseguradaIncisoWindow = new Ext.Window({
        title: 'Suma Asegurada de la Cobertura',
        width: 430,
        height:215,        
        layout: 'fit',
        plain:true,
        //bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
  
        items: sumaAseguradaIncisoForm,
		        
		buttons: [{
	            	text: 'Guardar',
	            	handler: function() {                	
                		if (sumaAseguradaIncisoForm.form.isValid()) {
                			
		 		        	sumaAseguradaIncisoForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Procesando...',
					            	failure: function(form, action) {
						   				Ext.MessageBox.alert('Status', 'Suma asegurada no agregada');
									},
									success: function(form, action) {
						    			Ext.MessageBox.alert('Status', 'Suma asegurada agregada');
						    			sumaAseguradaIncisoWindow.close();
						    			storeSumaAseguradaInciso.load();						    				
						    		
						}
			        		});                   
                		} else{
					Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos');
						}             
	        		}
    		    },{
	            	text: 'Cancelar',
	            	handler: function(){
	            		sumaAseguradaIncisoWindow.close();
	            	}
    		    },{
	            	text: 'Valores Por Defecto',
	            	id:'id-boton-valor-defecto-suma-asegurada',
	            	handler:function(){
	            		if(Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').getValue()!="undefined" 
	            		&& Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').getValue()!=""
	            		&& Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').getValue()!="0"
	            		&& Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').getValue()!=null ){
							ExpresionesVentana2(Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').getValue(),"EXPRESION_SUMAS_ASEGURADAS");
	            		}else{
	            			var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									//alert(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').setValue(Ext.util.JSON.decode(response.responseText).codigoExpresion);
									ExpresionesVentana2(Ext.getCmp('hidden-valor-defecto-suma-asegurada-cobertura').getValue(),"EXPRESION_SUMAS_ASEGURADAS");
								}
					   		});            		
	            		}
	            	}	     
    		    }]
    	});    	    	
	//Ext.getCmp('id-boton-valor-defecto-suma-asegurada').disable();
	/*Ext.getCmp('hide-button-revalorizacion').disable();
	Ext.getCmp('id-texto-leyenda').disable();
	Ext.getCmp('leyenda-cotizacion-check').disable();
	Ext.getCmp('leyenda-caratula-check').disable();
	*/
	sumaAseguradaIncisoWindow.show();
    };