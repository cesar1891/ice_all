NuevaVariableExpresiones= function(variableDataStore, row) {
	var dsComboTabla
	var dsComboColumna;
	var dsComboClave;

	
	dsComboTabla = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: '<s:url namespace="expresiones" action="ComboTabla" includeParams="none"/>'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaTablaJson',
            id: 'comboAbuelo'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsComboTabla.load();
  	
  	dsComboColumna = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: '<s:url namespace="expresiones" action="ComboColumna" includeParams="none"/>'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaColumnaJson',
            id: 'dsComboColumna'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'},
           {name: 'nick', type: 'string',mapping:'nick'}      
        ]),
		remoteSort: true,		
        baseParams: {tabla:'undefined'}
    });
    dsComboColumna.load();



    dsComboClave = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: '<s:url namespace="expresiones" action="ComboClave" includeParams="none"/>'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaClave',
            id: 'comboHijo'
	        },[
           {name: 'clave', type: 'string',mapping:'clave'},
           {name: 'expresion', type: 'string',mapping:'expresion'},             
           {name: 'switchRecalcular', type: 'boolean',mapping:'switchRecalcualar'},
           {name: 'recalcular', type: 'string',mapping:'recalcular'},
           {name: 'idBase', type: 'string',mapping:'idBase'},
           {name: 'codigoSecuencia', type: 'string',mapping:'codigoSecuencia'}          
        ]),
		remoteSort: true,		
        baseParams: {tabla:'undefined',
         			 columna:'undefined',
         			 nombreVariableLocal:'undefined'}
    });
  	dsComboClave.load();
  	var columnModel = new Ext.grid.ColumnModel([
        {id:'clave',header: "Clave", dataIndex: 'clave'},
        {header: "Expresion", dataIndex: 'expresion'},
        {header: "Recalcular", dataIndex: 'switchRecalcular'}
    ]);

    var recalcular= new Ext.form.Checkbox({
   			id:'recalcular-check-variables-expresion',
            name:'recalcular',
            boxLabel: '<s:text name="productos.variableLoc.recalcular"/>',
          	hideLabel:true
    });
   
   	var nombre= new Ext.form.TextField({   					
                    fieldLabel: '<s:text name="productos.variableLoc.nombre"/>',
                    name: 'nombre',
                    width:'150',
		          	allowBlank:false,
		          	blankText : '<s:text name="productos.valida.variableLoc.nombre.req"/>' 
   	});
   	
   	var expresion= new Ext.form.TextField({fieldLabel:'Expresion',disabled:true,name:'expresion', maxLength :'240'});
	var comboTabla =new Ext.form.ComboBox({
							id:'id-combo-tabla-variable-expresion',
    	                    tpl: '<tpl for="."><div ext:qtip="{value}. {nick}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsComboTabla,
						    displayField:'value',
						    valueField: 'key',
					    	typeAhead: true,
					    	mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'<s:text name="productos.valida.variableLoc.tabla"/>',
					    	selectOnFocus:true,
				          	allowBlank:false,
				          	blankText : '<s:text name="productos.valida.variableLoc.tabla.req"/>',
						    fieldLabel: '<s:text name="productos.variableLoc.tabla"/>',
						    listWidth:250,
					    	name:"descripcionTabla",
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
									comboColumna.reset();
									//comboClave.reset();
									dsComboClave.removeAll();									
									dsComboColumna.baseParams['tabla'] = this.getValue();
					                dsComboColumna.load({
                							callback : function(r,options,success) {
							                        if (!success) {
        		    					                    //  Ext.MessageBox.alert('Aviso','No se encontraron registros');  
					                                       	dsComboColumna.removeAll();
                    					            }
					                        }
					                });
					               
							}
			});
	
	var comboColumna =new Ext.form.ComboBox({
							id:'id-combo-columna-variable-expresion',
    	                    tpl: '<tpl for="."><div ext:qtip="{value}. {nick}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsComboColumna,
						    displayField:'value',
						    valueField: 'key',
					    	typeAhead: true,
				          	allowBlank:false,
				          	blankText : '<s:text name="productos.valida.variableLoc.columna.req"/>',
					    	mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'<s:text name="productos.valida.variableLoc.columna"/>',
					    	selectOnFocus:true,
						    fieldLabel: '<s:text name="productos.variableLoc.columna"/>',
					    	name:"descripcionColumna",
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
									//comboClave.reset();
									Ext.getCmp('hidden-switch-formato-variable-expresion').setValue(record.get('nick'));			            													
									dsComboClave.baseParams['tabla'] = Ext.getCmp('id-combo-tabla-variable-expresion').getValue();                
						            dsComboClave.baseParams['columna'] = this.getValue();
						            dsComboClave.baseParams['nombreVariableLocal'] = 'undefined';
						            dsComboClave.load({
					                		callback : function(r,options,success) {
		            					            if (!success) {
						       		                        //  Ext.MessageBox.alert('Aviso','No se encontraron registros');  
                        					               	dsComboClave.removeAll();
						                            }
					                        }
					                });
					                
							}
					    	
			});

    var checkColumn = new Ext.grid.CheckColumn({
       header: "Recalcular",
       dataIndex: 'switchRecalcular'
       //,width: 55,
       //align:'center'
    });
    
	cmClaves = new Ext.grid.ColumnModel([{
           id:'clave',
           header: "Clave",
           dataIndex: 'clave',
           width: 100,
           editor: new Ext.form.TextField({
               allowBlank: false
           })
        },{
           header: "Expresión",
           dataIndex: 'expresion',
           width: 50,
           editor: new Ext.form.TextField({
               allowBlank: false
           })
        },checkColumn
    ]);
 cmClaves.setEditable(0,false);
 gridEditableClaves = new Ext.grid.EditorGridPanel({
    	//id:'grid-editable-claves',
        store: dsComboClave,
        cm: cmClaves,
        autoScroll:true,
        width:400,
        height:175,
        autoExpandColumn:'clave',
        title:'<s:text name="productos.variableLoc.subtitulo.claves"/>',
        collapsible:true,
        frame:true,
        plugins:checkColumn,
        clicksToEdit:1,
		viewConfig: {
	        forceFit: true
	    }
    });

    var formPanel = new Ext.FormPanel({
    	id:'id-forma-variables-expresion',
        labelAlign: 'left',
        frame:true,
        region:'center',
        //layout:'column',        
        bodyStyle:'padding:5px 5px 0',
        width: 530,
        //url:'expresiones/AgregarVariable.action',
        items: [{xtype:'hidden', id:'hidden-codigo-tabla-variable-expresion', name:'tabla'},
		        {xtype:'hidden', id:'hidden-codigo-columna-variable-expresion', name:'columna'},
		        {xtype:'hidden', id:'hidden-switch-formato-variable-expresion', name:'switchFormato'},
		        {xtype:'hidden', id:'hidden-codigo-nombre-variable-local', name:'codigoNombreVariableLocal'},
		        {xtype:'hidden', id:'hidden-codigo-edita-variable-expresion', name:'editaVariableLocal'},
		        {
		        	layout:'column',
		        	border:false,
		        	items:[{
			        		columnWith:.5,
    			    		layout:'form',
        					labelAlign: 'left',
        					border:false,
        					width:280,
        					items:[
	        					nombre,
	   		    				comboTabla,
	   		    	        	comboColumna
				    		    ]       
			    	    },{
    	    				columnWith:.5,
	        				layout:'form',
    	    				labelAlign: 'left',
        					border:false,
		        			items:[{
		        				layout:'form',
		        				border:false,
		        				height:'50'
		        			},{
		        				layout:'form',
		        				border:false,
		        				height:'50'
		        			},{
		        				xtype:'button',
		        				text:'<s:text name="productos.config.variableLoc.btn.5claves"/>',
		        				handler:function(){
		        					TablasDeApoyo(dsComboTabla);
		        					}
		        			}]       
			    	}]
			    },gridEditableClaves
  		]
    });
 	
    // define window and show it in desktop
 var wind = new Ext.Window({
            title: '<s:text name="productos.variableLoc.titulo.principal"/>',
            closable:true,
            buttonAlign:'center',
            width:430,
            height:360,
            //border:false,
            plain:true,
            layout: 'border',
            modal:true,
			items:[formPanel],
  			buttons:[{
  					text:'<s:text name="productos.config.expresion.btn.guardar"/>',
  					handler: function(){
		  					
		  					//var recs = dsComboClave.getModifiedRecords();  						
  							//alert("recs"+recs.length);
							
  							//var boolean1=validarClavesObligatoriasExpresion();
  							//var boolean2=validarClavesObligatoriasModificadasExpresion(recs);
							
  							if(formPanel.form.isValid() ) {
  								if(validarClavesObligatoriasExpresion()){
  									if(row!=null){}else{
						 		        Ext.getCmp('hidden-codigo-tabla-variable-expresion').setValue(Ext.getCmp('id-combo-tabla-variable-expresion').getValue());
								        Ext.getCmp('hidden-codigo-columna-variable-expresion').setValue(Ext.getCmp('id-combo-columna-variable-expresion').getValue());
							        }
					 				Ext.getCmp('hidden-codigo-nombre-variable-local').setValue(nombre.getValue());
					 				
					 				
					 				//var params ="listaClave[0].clave='Hallo' && listaClave[0].recalcular='N' && listaClave[0].expresion='halo'";
					 				var params="";
					 				var maxlengthRecords=dsComboClave.getTotalCount();
					 				for (var i=0; i<maxlengthRecords; i++) {									
									params +=  "listaClave["+i+"].codigoSecuencia="+dsComboClave.getAt(i).get('codigoSecuencia')+
											"&&listaClave["+i+"].clave="+dsComboClave.getAt(i).get('clave')+ 
											"&& listaClave["+i+"].expresion="+dsComboClave.getAt(i).get('expresion')+
											"&& listaClave["+i+"].recalcular="+((dsComboClave.getAt(i).get('switchRecalcular')== true)?'S':'N')+"&&"; 											
									}	      
									//alert("params="+params);
					 		        formPanel.form.submit({	
							 		        url:'expresiones/AgregarVariable.action',		      
								            waitTitle:'<s:text name="ventana.expresiones.proceso.mensaje.titulo"/>',
					            			waitMsg:'<s:text name="ventana.expresiones.proceso.mensaje"/>',								            params:params,
								            failure: function(form, action) {
												    Ext.MessageBox.alert('Error', Ext.util.JSON.decode(action.response.responseText).mensajeDelAction);
											},
											success: function(form, action) {
												    //Ext.MessageBox.alert('Confirm', action.result.info);						   
												    variableDataStore.load({params:{start:0, limit:10}});
												    wind.close();
											}
							        });   
							    }               
	        		        } else{
									Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
							}     
  						}
  					},{
  						text:'<s:text name="productos.config.expresion.btn.cancelar"/>',
  						handler:function(){  						
  							wind.close();
  						}
  				}]
        });


        wind.show();

		function validarClavesObligatoriasExpresion(){
	    	var boleanoGE=false;
	    	//alert("validarClavesObligatoriasExpresion + dsComboClave.getTotalCount()="+dsComboClave.getTotalCount());
	    	if(dsComboClave.getTotalCount() != 0){
	    	
		    	var cuztomizedMessage='Favor de llenar las expresiones de';
		    	var maxlengthRecords=dsComboClave.getTotalCount();	    								
				for(var irec=0;irec<maxlengthRecords;irec++){				
						if(dsComboClave.getAt(irec).get('expresion').length==0 ){
							//alert(dsComboClave.getAt(irec).get('expresion'));
							//alert(dsComboClave.getAt(irec).get('expresion').length);
							
							boleanoGE=true;
							cuztomizedMessage+=', '+dsComboClave.getAt(irec).get("clave") ;
						}
						boleanoG=false;								
				}
				if(boleanoGE){
					Ext.MessageBox.alert('Errors', cuztomizedMessage);
				}
			}
			//alert(boleanoGE);
			return !boleanoGE;				
		}
if(row!=null){
	//alert("dentro del if row");
		var recVariableExpresion = variableDataStore.getAt(row);
        Ext.getCmp('hidden-codigo-edita-variable-expresion').setValue("1");                   
	    variableDataStore.on('load', function(){ 
	    		var claveVariableLocal = recVariableExpresion.get('nombre');
	    		//alert(claveVariableLocal);
	    		Ext.getCmp('hidden-codigo-nombre-variable-local').setValue(claveVariableLocal);  
	    		var claveTabla = recVariableExpresion.get('tabla');
	    		var claveColumna = recVariableExpresion.get('columna');
	    		dsComboTabla.load();
	    		dsComboColumna.baseParams['tabla'] = claveTabla;
                dsComboColumna.load({
                		callback : function(r,options,success) {
		                        if (!success) {
        		                        //  Ext.MessageBox.alert('Aviso','No se encontraron registros');  
                                       	dsComboColumna.removeAll();
                                }
                        }
                });
                dsComboClave.baseParams['tabla'] = claveTabla;                
                dsComboClave.baseParams['columna'] = claveColumna;
                dsComboClave.baseParams['nombreVariableLocal'] = claveVariableLocal;
				dsComboClave.load({
                		callback : function(r,options,success) {
		                        if (!success) {
        		                        Ext.MessageBox.alert('Aviso','No se encontraron registros');  
                                       	dsComboClave.removeAll();
                                }
                        }
                });
                nombre.disable();
			    Ext.getCmp('id-forma-variables-expresion').getForm().loadRecord(recVariableExpresion);                                                               
    	});
        variableDataStore.load();      
	}   
};