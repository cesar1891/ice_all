configuraConceptoCobertura= function(storeConceptosCobertura,codigoConceptoCobertura, row) {
var conceptosCoberturaForm;



/*if(row!=null){
	                      
    var rec = storeConceptosCobertura.getAt(row);
 
    alert("PERIODO " + rec.get('codigoPeriodo'));
    alert("CONCEPTO " + rec.get('codigoConcepto'));
    alert("ORDEN " + rec.get('orden'));
    
    storeConceptosCobertura.on('load', function(){ 
   						                
   							/*alert("PERIODO 2 " + rec.get('codigoPeriodo'));
    alert("CONCEPTO 2 " + rec.get('codigoConcepto'));
    alert("ORDEN 2 " + rec.get('orden'));						
   	*
    						 if(storeConceptosCobertura.getTotalCount()==null || storeConceptosCobertura.getTotalCount()==""){
                                }else{
    
    
                           Ext.getCmp('conceptos-cobertura-form').getForm().loadRecord(rec);
                           //Ext.getCmp('combo-periodo-conceptos-cobertura').disable();                                                     
                           //Ext.getCmp('combo-concepto-conceptos-cobertura').disable();
                           //Ext.getCmp('id-orden-conceptos-cobertura').disable();
                           Ext.getCmp('hidden-codigo-combo-periodo').setValue(rec.get('codigoPeriodo'));
                           Ext.getCmp('hidden-codigo-combo-concepto').setValue(rec.get('codigoConcepto'));                           
                           Ext.getCmp('hidden-orden').setValue(rec.get('orden'));
                           Ext.getCmp('hidden-edita').setValue(edita);                                                     
                           dsComportamiento.load();                          
					       dsCondicion.load();
                                }
                           });
	storeConceptosCobertura.load();
}*/


//******* nivel de inciso***************

//**************************************************************	
var dsPeriodo = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'conceptosCobertura/CargaListasConceptosPorCobertura.action?lista=periodos'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaPeriodos',
            id: 'comboPeriodo'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsPeriodo.setDefaultSort('comboPeriodo', 'desc'); 
  	dsPeriodo.load();
  	
var comboPeriodo =new Ext.form.ComboBox({
    	                    id:'combo-periodo-conceptos-cobertura',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsPeriodo,
							typeAhead: true,
							width:330,
							listWidth: '330',
							labelAlign: 'left',	
						    displayField:'value',
						    allowBlank:false,
						    blankText : 'Periodo requerido',
						    valueField: 'key',
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'Seleccione periodo',
					    	selectOnFocus:true,
						    fieldLabel: 'Periodo*',
					    	name:"descripcionPeriodo",
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
								Ext.getCmp('hidden-codigo-combo-periodo').setValue(valor);
							}			    	
			});
			                                                              
var dsConcepto = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'conceptosCobertura/CargaListasConceptosPorCobertura.action?lista=conceptos'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaConceptos',
            id: 'comboConcepto'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsConcepto.setDefaultSort('comboConcepto', 'desc'); 
  	dsConcepto.load();
  	
var comboConcepto =new Ext.form.ComboBox({
    	                    id:'combo-concepto-conceptos-cobertura',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsConcepto,
							typeAhead: true,
							width:330,
							listWidth: '350',
							labelAlign: 'left',	
						    displayField:'value',
						    allowBlank:false,
						    blankText : 'Concepto requerido',
						    valueField: 'key',					    	
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'Seleccione concepto',
					    	selectOnFocus:true,
						    fieldLabel: 'Concepto*',
					    	name:"descripcionConcepto",
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
								Ext.getCmp('hidden-codigo-combo-concepto').setValue(valor);
							}			    	
			});
			

var dsComportamiento = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'conceptosCobertura/CargaListasConceptosPorCobertura.action?lista=comportamiento'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaComportamientos',
            id: 'comboComportamiento'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsComportamiento.setDefaultSort('comboComportamiento', 'desc'); 
  	dsComportamiento.load();
  	
var comboComportamiento =new Ext.form.ComboBox({
    	                    id:'combo-comportamiento-conceptos-cobertura',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsComportamiento,
							//disabled:true,
							typeAhead: true,
							width:330,
							listWidth: '330',
							labelAlign: 'left',	
							//anchor: '90%', 					
						    displayField:'value',
						    allowBlank:false,
						    blankText : 'Comportamiento requerido',
						    valueField: 'key',					    	
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'Seleccione Comportamiento',
					    	selectOnFocus:true,
						    fieldLabel: 'Comportamiento*',
						    //labelSeparator:'',
					    	name:"descripcionComportamiento",
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
								Ext.getCmp('hidden-codigo-combo-comportamiento').setValue(valor);
							}			    	
			});	
	comboComportamiento.on('select', function(){
		if(this.getValue() == 'CONDICIONAL' || this.getValue() =='C'){
			Ext.getCmp('id-configurar-concepto-cobertura-validacion-combo-comportamiento').show();
			Ext.getCmp('combo-condicion-conceptos-cobertura').allowBlank = false;
		}else{
			Ext.getCmp('id-configurar-concepto-cobertura-validacion-combo-comportamiento').hide();
			Ext.getCmp('combo-condicion-conceptos-cobertura').allowBlank = true;
		}
	});
	
var dsCondicion = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'conceptosCobertura/CargaListasConceptosPorCobertura.action?lista=condiciones'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaCondicion',
            id: 'comboCondicion'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsCondicion.setDefaultSort('comboCondicion', 'desc'); 
  	dsCondicion.load();
  	
var comboCondicion =new Ext.form.ComboBox({
    	                    id:'combo-condicion-conceptos-cobertura',
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsCondicion,
							typeAhead: true,
							width:330,
							listWidth: '350',
							labelAlign: 'left',	
						    displayField:'value',
						    blankText : 'Condici\u00F3n requerida',
						    valueField: 'key',					    	
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'Seleccione condici\u00F3n',
					    	selectOnFocus:true,
						    fieldLabel: 'Condici\u00F3n*',
					    	name:"descripcionCondicion",
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
								Ext.getCmp('hidden-codigo-combo-condicion-concepto').setValue(valor);
							}			    	
			});
	


	var ordenText= new Ext.form.NumberField({
				            id:'id-orden-conceptos-cobertura',
            				name:'orden',
            				fieldLabel: 'Orden*',
            				allowDecimals : false,
				            allowNegative : false,            				
        				    allowBlank: false,
        				    blankText : 'Orden requerida',            				            				
            				width:330//165	
           });





//************FORMA****************
	 conceptosCoberturaForm = new Ext.form.FormPanel({
            	id:'conceptos-cobertura-form',
            	baseCls: 'x-plain',
            	frame:true,
            	labelAlign: 'left',
            	url:'conceptosCobertura/AgregarConceptosCobertura.action?codigoCobertura='+codigoConceptoCobertura,
        		width:650, 
        		autoHeight:true,
        		buttonAlign: "center",
                items: [{xtype:'hidden',id:'hidden-codigo-combo-periodo',name:'codigoPeriodo'},
                		{xtype:'hidden',id:'hidden-codigo-combo-concepto',name:'codigoConcepto'},
                		{xtype:'hidden',id:'hidden-codigo-combo-comportamiento',name:'codigoComportamiento'},
                		{xtype:'hidden',id:'hidden-codigo-combo-condicion-concepto',name:'codigoCondicion'},
                		{xtype:'hidden',id:'hidden-orden',name:'ordenHidden'},
                		{xtype:'hidden',id:'hidden-codigo-expresion-concepto-cobertura',name: "codigoExpresion"},
                		{
                		layout:'form',
                		baseCls: 'x-plain',                		                		
                		border: false,
                		width:600,
                		items:[
                			comboPeriodo,
				    	 	ordenText,
				    	 	{				    	 		
				    	 		layout:'column',				    	 		
                    			border:false,
                    			baseCls: 'x-plain',
                    			width: '630',
                    			items: [{
                            			columnWidth:.75,
                            			labelAlign: 'rigth',
                            			layout:'form',
                            			baseCls: 'x-plain',
                            			border:false,
                            			items:[comboConcepto]
                        				},{
                            			columnWidth:.25,
                            			labelAlign: 'rigth',
                            			layout:'form',
                            			baseCls: 'x-plain',
                            			border:false,
                            			items:[
                            				{xtype:'button', 
                            				text: 'Agregar Concepto&nbsp;',                        				 
                       						buttonAlign: "right", 
                       						handler: function() {                    	
				     							var connect = new Ext.data.Connection();
						    					connect.request ({
												url:'atributosVariables/ObtenerCodigoExpresion.action',
												callback: function (options, success, response) {				   
													var codigoDeExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
													ExpresionesVentana2(codigoDeExpresion, "EXPRESION_CONCEPTO_TARIFICACION", dsConcepto, '2');
													}
					   							});
				     }
                       						}
                                		]
                        		}]
                			},
                			{				    	 		
				    	 		layout:'column',				    	 		
                    			border:false,
                    			baseCls: 'x-plain',
                    			width: '630',
                    			items: [{
                            			columnWidth:.75,
                            			labelAlign: 'rigth',
                            			layout:'form',
                            			baseCls: 'x-plain',
                            			border:false,
                            			items:[comboComportamiento]
                        				},{
                            			columnWidth:.25,
                            			labelAlign: 'rigth',
                            			layout:'form',
                            			baseCls: 'x-plain',
                            			border:false,
                            			items:[
                            				{xtype:'button', 
                            				text: 'Agregar Variable T.',                        				 
                       						buttonAlign: "right", 
                       						handler: function() {                    	
				     							var connect = new Ext.data.Connection();
						    					connect.request ({
												url:'atributosVariables/ObtenerCodigoExpresion.action',
												callback: function (options, success, response) {				   
													var codigoDeExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
													ExpresionesVentana2(codigoDeExpresion, "EXPRESION_VARIABLES_TEMPORALES", null, '5');
													}
					   							});
				     }
                       						}
                                		]
                        		}]
                			},
                			{                				
                    			layout:'column',
                    			id:'id-configurar-concepto-cobertura-validacion-combo-comportamiento',
                    			border:false,
                    			baseCls: 'x-plain',
                    			width: '630',
                    			items: [{
                            			columnWidth:.75,
                            			labelAlign: 'rigth',
                            			layout:'form',
                            			baseCls: 'x-plain',
                            			border:false,
                            			items:[comboCondicion]
                        				},{
                            			columnWidth:.25,
                            			labelAlign: 'rigth',
                            			layout:'form',
                            			baseCls: 'x-plain',
                            			border:false,
                            			items:[
                                			{xtype:'button', 
                       						text: 'Agregar Condici&oacute;n&nbsp;',
                       						buttonAlign: "right", 
                       						handler: function() {
                									var connect = new Ext.data.Connection();
					    							connect.request ({
														url:'atributosVariables/ObtenerCodigoExpresion.action',
														callback: function (options, success, response) {				   
															codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
															ExpresionesVentana2(codigoExpresion, "EXPRESION_CONDICIONES", dsCondicion, '3');
														}
											   		});
                								}
                       						}
                       					]
                        		}]
                		}]                			
				}]
		});
		    
		    // define window and show it in desktop
    var conceptosCoberturaWindow = new Ext.Window({
        title: 'Configurar Concepto',
        width: 640,
        //height:220,
        autoHeight:true,
        layout: 'fit',
        plain:true,
        buttonAlign:'center',
        modal : true,
  
        items: conceptosCoberturaForm,
		        
		buttons: [{
	            	text: 'Guardar',
	            	handler: function() {                	
                			 
                		if (conceptosCoberturaForm.form.isValid()) {
                			
		 		        	conceptosCoberturaForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Procesando...',
					            	failure: function(form, action) {
						   				Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).msgRespuesta);
									},
									success: function(form, action) {
						    			Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).msgRespuesta);
						    			conceptosCoberturaWindow.close();
						    			storeConceptosCobertura.load();
						    				
						    		
						}
			        		});                   
                		} else{
					Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos');
						}             
	        		}
    		    },{
	            	text: 'Cancelar',
	            	handler: function(){
	            		conceptosCoberturaWindow.close();
	            	}
    		    }]
    	});    	    	
	conceptosCoberturaWindow.show();
	
	if(row!=null){
		var rec = storeConceptosCobertura.getAt(row);
		//storeConceptosCobertura.on('load', function(){ 
    	//if(storeConceptosCobertura.getTotalCount()==null || storeConceptosCobertura.getTotalCount()==""){
        //	}else{    
		Ext.getCmp('conceptos-cobertura-form').getForm().loadRecord(rec);
        //Ext.getCmp('combo-periodo-conceptos-cobertura').disable();                                                     
        //Ext.getCmp('combo-concepto-conceptos-cobertura').disable();
        //Ext.getCmp('id-orden-conceptos-cobertura').disable();
        Ext.getCmp('hidden-codigo-combo-periodo').setValue(rec.get('codigoPeriodo'));
        Ext.getCmp('hidden-codigo-combo-concepto').setValue(rec.get('codigoConcepto'));                           
        Ext.getCmp('hidden-orden').setValue(rec.get('orden'));
        //Ext.getCmp('hidden-edita').setValue(edita);                                                     
        //	}
        //});
        dsComportamiento.load();                          
		dsCondicion.load();
		storeConceptosCobertura.load();
	}
	
	//Si vamos a Agregar(si el row esta vacio) � el combo no tiene el valor 'CONDICIONAL', escondemos dicho combo:
	if( Ext.isEmpty(row) || Ext.getCmp('hidden-codigo-combo-comportamiento').getValue() != 'C' ){
		Ext.getCmp('id-configurar-concepto-cobertura-validacion-combo-comportamiento').hide();
	}
};