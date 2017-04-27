Incisos = function(dataStore){
//Ext.QuickTips.init();
//Ext.form.Field.prototype.msgTarget = "side";
    var banderaSelectComboIncisos = false;          
   	var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'incisos/IncisosLista.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'incisos',
            id: 'incisos'
	        },[
           {name: 'clave', type: 'string',mapping:'cdtipsit'},
           {name: 'descripcion', type: 'string',mapping:'dstipsit'}    
        ]),
		remoteSort: true
    });
       ds.setDefaultSort('incisos', 'desc'); 
    
    var obligatorioCheck= new Ext.form.Checkbox({
    			id:'obligatorio-check',
                name:'obligatorio',
                fieldLabel: 'Obligatorio',
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	insertaCheck.setValue(true);
				            	insertaCheck.setRawValue("S");				            	
				            	}
				            }
                });
                
       //obligatorioCheck.setRawValue("N");
	var insertaCheck= new Ext.form.Checkbox({
				id:'inserta-check',
                name:'inserta',
                fieldLabel: 'Por Defecto',
                checked:false,                
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
                });
               // insertaCheck.setRawValue("N");
    // simple array store
//var store = new Ext.data.SimpleStore({
  //  fields: ['clave', 'descripcion', 'nick']
    
//});            
                
    var comboWithTooltip = new Ext.form.ComboBox({
    tpl: '<tpl for="."><div ext:qtip="{clave}" class="x-combo-list-item">{descripcion}</div></tpl>',
    store: ds,
    id:'combo-codigo-inciso-del-producto',
    displayField:'descripcion',
    valueField: 'clave',
    allowBlank:false,
    blankText : 'Descripci\u00F3n Requerida',
    //maxLength : '2',
    //maxLengthText : 'Dos caracteres maximo',
    //typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    emptyText:'Seleccione un Riesgo',
    selectOnFocus:true,
    fieldLabel: 'Descripci\u00F3n*',
    listWidth:250,
    maxHeight:246,
    name:"descripcion",
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
	    	banderaSelectComboIncisos = true;
	    	Ext.getCmp('hidden-clave-combo-inciso').setValue(record.get('clave'));                      	
                   			
    		//alert(banderaSelectCombo);
    }
});   

var descripcionInciso = new Ext.form.TextField({
        fieldLabel: 'Descripci\u00F3n*',
        id:'edita-descripcion-inciso',
        allowBlank: false,
        //maxLength : '30',
   		//maxLengthText : 'Treinta Caracteres M\u00E1ximo',
        blankText : 'Descripci\u00F3n Requerida',
        name: 'descripcion',
        anchor: '90%'  
    });  
       
	var incisosForm = new Ext.form.FormPanel({
            	id:'forma-inciso',
            	baseCls: 'x-plain',
            	boder:false,
            	labelAlign: 'left',
            	url:'incisos/asociarInciso.action',
            	labelWidth: 90,
        		//title: 'Configuraci\u00F3n de Riesgo del Producto',
       			bodyStyle:'padding:5px',
        		width: 570,
        		buttonAlign: "center",
                items: [/*new Ext.Panel({
										id: 'details-panel-forma-inciso',
										baseCls: 'x-plain',
								        border:false	
								        //heigth:'50'
					    	})*/{
					    		layout:'form',
						    	baseCls: 'x-plain',
						    	border:false,
						    	id:'edita-forma-inciso',
						    	items:[
					    			descripcionInciso
					    		]					    	
					    	},{
					    	layout:'form',
					    	baseCls: 'x-plain',
					    	border:false,
					    	id:'hidden-form-tipo-inciso',
					    	items:[{
		 				            layout:'column',
		 				            baseCls: 'x-plain',
 						            border:false,
            						items:[{
							                columnWidth:.65,
            			    				layout: 'form',
            			    				baseCls: 'x-plain',
                							border:false,
			                				//width:200,
							                items: [
				        			        	comboWithTooltip
							                ]
						            	},{
                							columnWidth:.35,
							                layout: 'form',
							                width:165,
							                baseCls: 'x-plain',
							                border:false,
                							items: [{
								                    xtype:'button',
								                    text: 'Agregar al Cat\u00E1logo',
                								    name: 'AgregarAlCatalogo',
								                    buttonAlign: "left",
								                    id:"agrega-riesgo-catalogo",
                								    handler: function() {                   				    	             	
					               							creaAltaCatalogoIncisos(ds);//script que tienes afuera	
    			     								}	
						                	}]
				        		   	}]
				        	}]	   	
		        		}/*,obligatorioCheck,
		        		  insertaCheck,
		        
		        		{
    		    		    xtype:'numberfield',
				            name: "agrupador",
				            allowDecimals : false,
				            allowNegative : false,
				            maxLength : '2',
				            maxLengthText : 'Dos D\u00EDgitos M\u00E1ximo',
        				    allowBlank: false,
        				    blankText : 'Agrupador Num\u00E9rico Requerido',
        				    disableKeyFilter : true,
        				    fieldLabel: 'Agrupador*',
				            width: 150
    	    			}*//*,{
		        		    xtype:'numberfield',
    	    			    name: "NumeroInciso",
    	    			    allowDecimals : false,
				            allowNegative : false,
				            maxLength : '2',
				            maxLengthText : '<s:text name="productos.configIncisos.LongitudMax"/>,
    	    			    allowBlank: false,
    	    			    blankText : '<s:text name="productos.configIncisos.NoInciso.req"/>',
				            fieldLabel: '<s:text name="productos.configIncisos.NoInciso"/>',
        				    width: 150
		        	},{layout:'form',contentEl:'texto-obligatorios',border:false}*/
		        	,{xtype:'hidden',id:'hidden-clave-combo-inciso',name:'clave'}]
		        	

        		/*buttons: [{
	            	text: 'Asociar/Actualizar',
	            	handler: function() {
                	// check form value 
                		if (incisosForm.form.isValid()) {
	                		//alert(Ext.getCmp('hidden-clave-combo-inciso').getValue());
							
		 		        	incisosForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Asociando y actualizando...',
					            	failure: function(form, action) {
								    Ext.MessageBox.alert('Error', 'Error al asociar inciso');
									},
									success: function(form, action) {
								    //Ext.MessageBox.alert('Inciso asociado', action.result.info);
								    if( Ext.getCmp('hidden-form-tipo-inciso').isVisible()){
								    	incisosForm.form.reset();
								    }
								    Ext.getCmp('arbol-productos').getRootNode().reload();						    		
						    		//action.form.reset();    		
								}
			        		});                   
                		} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Eliminar'
    		    },{
	            	text: 'Cancelar',
	            	handler: function() {
	            		window.close();
	            	}
    		    }]  */      
    	});

//ds.load({params:{start:0, limit:25}});    
//Ext.getCmp('details-panel-forma-inciso').hide();
Ext.getCmp('edita-forma-inciso').hide();
//incisosForm.render('centerIncisos');
//bd.createChild({tag: 'h2', html: ' * Datos requeridos'});


var window = new Ext.Window({
        title: 'Configuraci\u00F3n de Riesgo del Producto',
        width: 450,
        height:100,
        //minWidth: 300,
        //minHeight: 150,
        layout: 'fit',
        plain:true,
        //bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
  
        items: incisosForm,
        

buttons: [{
	            	text: 'Asociar/Actualizar',
	            	handler: function() {
                	// check form value 
                		if (incisosForm.form.isValid()) {
	                		//alert(Ext.getCmp('hidden-clave-combo-inciso').getValue());
							
		 		        	incisosForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Asociando y actualizando...',
					            	failure: function(form, action) {
								    Ext.MessageBox.alert('Error', 'Error al asociar riesgo');
									},
									success: function(form, action) {
									//alert(action.result.info);
								    Ext.MessageBox.alert('Aviso', 'Riesgo asociado');										
								    if( Ext.getCmp('hidden-form-tipo-inciso').isVisible()){
								    	incisosForm.form.reset();
								    }
								    Ext.getCmp('arbol-productos').getRootNode().reload();						    		
						    		//action.form.reset();    	
								    window.close();
								    agregaProducto();
								}
			        		});                   
                		} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Eliminar',
	            	id: 'btn-elimina-inciso',
	            	handler: function() {
	            		Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           					if(btn == 'yes'){ 
	            				if (incisosForm.form.isValid()) {
	                				//alert(Ext.getCmp('hidden-clave-combo-inciso').getValue());
									
		 		        			incisosForm.form.submit({
			 		        			url: 'incisos/eliminarInciso.action',
					            		waitTitle:'Espere',
					            		waitMsg:'Eliminando...',
					            		failure: function(form, action) {
										   	Ext.MessageBox.alert('Error', 'Error al eliminar inciso');
										},
										success: function(form, action) {
								   			//	Ext.MessageBox.alert('Inciso asociado', action.result.info);
											Ext.MessageBox.alert('Aviso', 'Riesgo eliminado');
								   			if( Ext.getCmp('hidden-form-tipo-inciso').isVisible()){
									   			incisosForm.form.reset();
								   			}
								   			Ext.getCmp('arbol-productos').getRootNode().reload();						    		
						    				//	action.form.reset();    	
								   			window.close();
								   			agregaProducto();
										}
			        				});
                				} else{
									Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
								}
							}
       					});
	            		//alert('Eliminar');
	            	}
    		    },{
	            	text: 'Cancelar',
	            	handler: function() {
	            		window.close();
	            		agregaProducto();
	            	}
    		    }]

    });

	window.show();


}; 