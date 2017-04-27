    
	var selIndex;    
	var store;
    
    
    var otClave= new Ext.form.TextField({
                name:'otClave',
                fieldLabel: 'Valor Clave*',
                allowBlank: false,
                readOnly:  true,
                blankText : 'Valor Clave requerido',
                width: 80
            	});
            
	var otValor = new Ext.form.TextField({
        		name: 'otValor',
        		fieldLabel: 'Valor Descripci\u00F3n*',
        		allowBlank: false,
        		blankText : 'Valor Descripci\u00F3n requerido',
        		width: 160  
    			});   
	
    
        
    var formPanel = new Ext.form.FormPanel({
   		id:"formPanel",
        baseCls: 'x-plain',
        labelWidth: 115,
        url:'atributosVariables/CargaManual.action',
        defaultType: 'textfield',
        //collapsed : false,

        items: [
            otClave,
            otValor
    	]
    });

    // define window and show it in desktop
    var agregarValoresGridWindow = new Ext.Window({
        title: 'Carga Manual',
       
        width: 350,
        height:170,
        minWidth: 350,
        minHeight: 170,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
  
        items: formPanel,
        

		buttons: [{
            text: 'Guardar', 
            handler: function() {   
                // check form value 
                if (formPanel.form.isValid()) {
	 		        formPanel.form.submit({	
	 		        	url:'atributosVariables/CargaManual.action?selectedId='+selIndex,		      
			            waitTitle:'Espere',
					    waitMsg:'Procesando...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Error al editar elemento');
						},
						success: function(form, action) {
						    //Ext.MessageBox.alert('Status', 'Elemento editado');
						    agregarValoresGridWindow.hide();
						    
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos.');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){
            	agregarValoresGridWindow.hide();
            }
        }]
    });
	//**************************************************
	//***********LISTA DE VALORES***********************
   	
   	var nombre= new Ext.form.TextField({
                name:'nombre',
                fieldLabel: 'Nombre*',
                allowBlank: false,
                blankText : 'El Nombre es requerido',
                width: 80
            });
            
	var descripcion = new Ext.form.TextField({
        name: 'descripcion',
        fieldLabel: 'Descripci\u00F3n*',
        allowBlank: false,
        blankText : 'La Descripci\u00F3n es requerida',
        width: 160  
    });   
    
    var numero = new Ext.form.NumberField({
        name: 'numero',
        fieldLabel: 'N\u00FAmero',
        allowDecimals : false,
		allowNegative : false,
		disabled:true,
        //allowBlank: false,
        //blankText : 'Dato numerico requerido',
		width: 30  
    });   
   
	
	var tablaDeErroresCheck= new Ext.form.Checkbox({
                name:'enviaTablaError',
                boxLabel: 'Enviar a Tabla de Errores',
            	hideLabel:true,	
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });
            
	var modificarValoresCheck= new Ext.form.Checkbox({
                name:'modificaValor',
                boxLabel: 'Modificar Valores',
            	hideLabel:true,			
                checked:false,
                onClick:function(){
				            	if(this.getValue()){
				            	this.setRawValue("S");				            	
				            	}
				            }
                 
            });     
            
     var clave = new Ext.form.TextField({
        fieldLabel: 'Clave*',
        allowBlank: false,
        blankText : 'La clave es requerida',
        name: 'clave',
        width: 160   
    });    
    
     var functionRadioAlfa= new Ext.form.Radio({
            				//allowBlank: false,
            				name:'formatoClave',
            				checked:true,
            				fieldLabel: 'Formato*',            				
            				boxLabel: 'Alfanum\u00E9rico',            				
  	  	       				onClick: function(){			            		
            					if(this.getValue()){					            	            						
            						this.setRawValue("A");
				            	}
			            	}
            });   
            
    var functionRadioNumeric= new Ext.form.Radio({
    						//allowBlank: false,
            				name:'formatoClave',
            				labelSeparator : "",
            				boxLabel: 'Num\u00E9rico',
            				hideLabel:true,        
  	  	       				onClick: function(){			            		
            					if(this.getValue()){					            	            						
            						this.setRawValue("N");
				            	}
			            	}
            });
         
     var hideTextMaximo= new Ext.form.NumberField({
				            
            				name:'maximoClave',
            				fieldLabel: 'M\u00E1ximo*',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato num\u00E9rico requerido',            				
            				hideParent:true,
            				width:30	
            							       
           });
           
     var hideTextMinimo= new Ext.form.NumberField({
				            
            				name:'minimoClave',
            				fieldLabel: 'M\u00EDnimo*',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato num\u00E9rico requerido',            				
            				hideParent:true,            				
            				width:30	
            							       
           });    
         
      var descripcion2 = new Ext.form.TextField({
        name: 'descripcionFormato',
        fieldLabel: 'Descripci\u00F3n*',
        allowBlank: false,
        blankText : 'La Descripci\u00F3n es requerida',
        width: 160  
    	   });
    	   
	  var functionRadioAlfa2= new Ext.form.Radio({
	  						//allowBlank: false,
            				name:'formatoDescripcion',
            				fieldLabel: 'Formato*',            				
            				boxLabel: 'Alfanum\u00E9rico',  
            				checked:true,          				
  	  	       				onClick: function(){			            		
            					if(this.getValue()){					            	            						
            						this.setRawValue("A");
				            	}
			            	}
            });   
            
      var functionRadioNumeric2= new Ext.form.Radio({
            				//allowBlank: false,
            				name:'formatoDescripcion',
            				labelSeparator : "",
            				boxLabel: 'Num\u00E9rico',
            				hideLabel:true,            				
  	  	       				onClick: function(){			            		
            					if(this.getValue()){					            	            						
            						this.setRawValue("N");
				            	}
			            	}
            });
            
       var hideTextMaximo2= new Ext.form.NumberField({
				            
            				name:'maximoDescripcion',
            				fieldLabel: 'M\u00E1ximo*',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato num\u00E9rico requerido',            				
            				hideParent:true,            				
            				width:30	
            							       
           });
           
     var hideTextMinimo2= new Ext.form.NumberField({
				            
            				name:'minimoDescripcion',
            				fieldLabel: 'M\u00EDnimo*',
            				allowDecimals : false,
				            allowNegative : false,
        				    allowBlank: false,
        				    blankText : 'Dato num\u00E9rico requerido',            				
            				hideParent:true,            				
            				width:30	
            							       
           });     
                	         
         
creaListasDeValores = function(dstore,edita,row) {   
 //***********************************************************
 
//Variables para la paginacion del grid
var startParam = 0;
var limitParam = 20; // Se ha fijado a 20 rows por pagina del grid
var Plant;

//Variable para poder cargar por primer vez los stores de los grids (cada vez que se abre la ventana o se carga una nueva pagina)
var firstLoad = true;

var _paramsEliminar = new Array();

//Variables para Identificar cuando se esta cambiando de row cuando se esta en Modo de Edicion 
var isSelectingWhileEditing = false;
var isBlurToEditCell = false;

//Variables para identificar el row actualmente editandose
var editingColumn = 0;
var editingRow = 0;

//Auxiliar en la paginacion, para obtener el parametro start una vez que cualquier evento de cambio de pagina sea disparado
Ext.override(Ext.PagingToolbar, {
	doLoad : function(C) {
		startParam = C;
		var B = {}, A = this.paramNames;
		B[A.start] = C;
		B[A.limit] = this.pageSize;
		this.store.load({
			params : B
		})
	}
});


  function test(){        		        				
  	
		  	   Plant = Ext.data.Record.create([
		  	   		{name: 'id',  type: 'string',  mapping:'id'},
	        		{name: 'otClave',  type: 'string',  mapping:'key'},
	        		{name: 'otValor',  type: 'string',  mapping:'value'},
	        		{name: 'nick',  type: 'string',  mapping:'nick'}
	        		            
				]);
  	
 			url='atributosVariables/ListaCargaManual.action' 			 		 			
 			store = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url,
				timeout: 60000
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaCargaManual',   
            	totalProperty: 'totalCount',
            	id: 'key'
	        	},Plant),
			remoteSort: true,
			baseParams:{tabla:''},
			listeners: {
	        beforeload: function (str, options){
	        				if(!firstLoad){
	        					if(store.getModifiedRecords().length <= 0)return true;
	        					Ext.Msg.show({
									 title:'Advertencia'
									,msg:'Si cambia de p&aacute;gina se perder&aacute;n los cambios realizados, </br> ¿Desea Continuar?'
									,icon:Ext.Msg.QUESTION
									,buttons:Ext.Msg.YESNO
									,fn: function(btn, txt){
											if(btn == 'yes'){
												firstLoad = true;
												store.rejectChanges();
												store.load({
													params: {
														start: startParam, 
														limit: limitParam
													}
											    });
											}
										}
								});
	        				
	        				return false;
	        				}
	        				return true;
	        			},
	        load: function (){
					firstLoad = false;
	        	}
	        }
    	});
    	store.setDefaultSort('otValor', 'desc');
		return store;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cm = new Ext.grid.ColumnModel([
		    //new Ext.grid.RowNumberer(),
			{
				header: "Valor Clave",
				dataIndex:'otClave',	
				width: 120, 
				id:'otClave',
				editor: new Ext.form.TextField({
	                allowBlank: false,
	                listeners: {
			        render: function(c) {
			          c.getEl().on({
			            'keydown' : {
			            			fn: function(evt, t, o){eventoNavegacionGridCargaManual(evt, t, o)},
			            			scope: c
			            			},
			            'focus' : {
			            
			            	fn: function(evt, t, o){/*isEditing = true;*/ isSelectingWhileEditing = false; isBlurToEditCell = false;},
			            	scope: c
			            }
			          });
			        }
			       }
           		})
			},		    
		    {
			    header: "Valor Descripci\u00F3n",
			    dataIndex:'otValor',
			    width: 120,
			    editor: new Ext.form.TextField({
	               allowBlank: false,
	               listeners: {
			        render: function(c) {
			          c.getEl().on({
			            'keydown' : {
			            			fn: function(evt, t, o){eventoNavegacionGridCargaManual(evt, t, o)},
			            			scope: c
			            			},
			            'focus' : {
			            
			            	fn: function(evt, t, o){/*isEditing = true;*/ isSelectingWhileEditing = false; isBlurToEditCell = false;},
			            	scope: c
			            }
			          });
			        }
			       }
	            })
		    }
		   
		   	                	
        ]);
        var grid2;
        var selectedId;     
	    
	    
	    grid2= new Ext.grid.EditorGridPanel({
		store:test(),
		id:'grid-lista',
		border:true,
		loadMask: {msg: 'Cargando datos ...'},
		cm: cm,
		clicksToEdit: 2,
		listeners : {
			beforeedit: function(e){
	    		editingColumn = e.column;
	    		editingRow = e.row;
	    	},
			dblclick: function(e){
	    		if(store.getCount() <= 0){
	    			agregarFilaGridCargaManual();
	    		}
	    	},
	    	validateedit: function(e){
	    		if(Ext.isEmpty(e.value.trim()))return false;
	    		else return true;
	    	},
	    	cellclick:  function(){
	    		isBlurToEditCell = false;//isEditing = false;    	
	    	}
		},
		sm: new Ext.grid.CellSelectionModel({
		singleSelect: true,
		listeners: {
			beforecellselect: function(){
						if(isBlurToEditCell){
							if(isSelectingWhileEditing)return false;
								else isSelectingWhileEditing = true;
						}
			}
	    }
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar valor',
            iconCls:'add',
            handler: function() {   
			    			agregarFilaGridCargaManual();
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-grid',
            tooltip:'Eliminar valor',
            iconCls:'remove',
            handler: function(){
   						var registroEliminar = grid2.getSelectionModel().getSelectedCell();
   						if(registroEliminar){
   							var recEliminar = store.getAt(registroEliminar[0]);
   							
	        	 					Ext.MessageBox.confirm("Mensaje","Esta seguro que desea eliminar este elemento?",function(btn){									   			
	        	 					 		if(btn == 'yes'){ 
									   			recEliminar.reject();
									   			
									   			if(recEliminar.get("nick") != "I"){
									   				
									   				_paramsEliminar.push(recEliminar.get("otClave").trim());
									   				
									   			}
									   			store.remove(recEliminar);	
									   			
									   		}
									 });
									 
   						}else Ext.Msg.alert('Aviso','Debe seleccionar un registro para realizar esta operaci&oacute;n');
   					
   					}
            
        },'-',{
            
            text:'Carga Autom\u00E1tica',
            tooltip:'Carga automatica de valores'
            //iconCls:'option'
        }],      							        	    	    
    	width:600,
        height:170,
    	frame:true,     
		title:'Carga Manual',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true},                
		bbar: new Ext.PagingToolbar({
			store: store,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} of {1}',
			emptyMsg: "No hay registros que mostrar"      		    	
			})        							        				        							 
		});   
		
	function agregarFilaGridCargaManual(){
    	var p = new Plant({
	        	        otClave: '',
	        	        otValor: '',
	            	    nick: 'I'
            	   	});      
            	   	
		grid2.stopEditing();            		    		
		store.add(p);
		grid2.startEditing(store.getCount()-1, 0);
    }
    
    function eventoNavegacionGridCargaManual(e, t, o){
			
    	   if(e.getKey() == e.ENTER || e.getKey() == e.ESC){
 		   	e.stopEvent();
 		   	isBlurToEditCell = false;
 		   }
 		   if(e.getKey() == e.DOWN){
 		   	e.stopEvent();
 		   	isBlurToEditCell = true;
 		   			if(grid2.getStore().getCount()-1 == editingRow){
 		   				agregarFilaGridCargaManual();
 		   			}
		   			else {
 		   				grid2.startEditing(editingRow + 1, editingColumn);
 		   			}
 		   }
 		   else if(e.getKey() == e.UP){
 		   	e.stopEvent();
 		   			if(editingRow > 0) {
 		   				isBlurToEditCell = true;
 		   				grid2.startEditing(editingRow - 1, editingColumn);
 		   			}
 		   }
 		   if(e.getKey() == e.TAB){
 		   	e.stopEvent();
 		   	isBlurToEditCell = true;
 		   }			
    }
    
if(edita){
		
		
		var rec = dstore.getAt(row);
		var numeroTabla= rec.get('numeroTabla');
		var tabla = rec.get('codigoTabla');    
		//alert("NUMEROTABLA"+numeroTabla);
					
		url='atributosVariables/EditarListaDeValoresCabecera.action?nmTabla='+numeroTabla;
		var storeListaValores = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'editaListaDeValores'
	        	},[
	        		//{name: 'ottipoac',  type: 'string',  mapping:'ottipoac'},
	        		//{name: 'ottipotb',  type: 'string',  mapping:'ottipotb'},	        		
	        		//{name: 'clnatura',  type: 'string',  mapping:'clnatura'},
	        		{name: 'nombre',  type: 'string',  mapping:'nombre'},
	        		{name: 'descripcion',  type: 'string',  mapping:'descripcion'},
	        		{name: 'numero',  type: 'string',  mapping:'numeroTabla'},
	        		{name: 'enviarTablaErrores',  type: 'string',  mapping:'enviarTablaErrores'},
	        		{name: 'modificaValores',  type: 'string',  mapping:'modificaValores'},
	        		{name: 'clave',  type: 'string',  mapping:'clave'},
	        		{name: 'formatoClave',  type: 'string',  mapping:'formatoClave'},
	        		{name: 'minimoClave',  type: 'string',  mapping:'minimoClave'},
	        		{name: 'maximoClave',  type: 'string',  mapping:'maximoClave'},	        		
	        		{name: 'cdAtribu',  type: 'string',  mapping:'cdAtribu'},
	        		{name: 'descripcionFormato',  type: 'string',  mapping:'descripcionFormato'},
	        		{name: 'formatoDescripcion',  type: 'string',  mapping:'formatoDescripcion'},
	        		{name: 'minimoDescripcion',  type: 'string',  mapping:'minimoDescripcion'},
	        		{name: 'maximoDescripcion',  type: 'string',  mapping:'maximoDescripcion'},	        		
	        		{name: 'cdCatalogo1',  type: 'string',  mapping:'cdCatalogo1'},
	        		{name: 'catalogo1',  type: 'string',  mapping:'dsCatalogo1'},
	        		{name: 'cdCatalogo2',  type: 'string',  mapping:'cdCatalogo2'},
	        		{name: 'catalogo2',  type: 'string',  mapping:'dsCatalogo2'},
	        		{name: 'claveDependencia',  type: 'string',  mapping:'claveDependencia'}
	        		            
				]),
			
			remoteSort: true
    	});
    	
    	storeListaValores.on('load', function(){
    				//alert('load');
					if(storeListaValores.getTotalCount()>0){
    						var recLV= storeListaValores.getAt(0);
    						var recLVETE = recLV.get('enviarTablaErrores');
    						var recLVMV = recLV.get('modificaValores');
    						
                           	Ext.getCmp('listas-form').getForm().loadRecord(recLV);                           
                           	if(recLVETE=="S"){
                           		Ext.getCmp('id-envia-tabla-error-lista-valores').setValue(true);
                          		Ext.getCmp('id-envia-tabla-error-lista-valores').setRawValue("S");
                           	}else{
	                           	Ext.getCmp('id-envia-tabla-error-lista-valores').setValue(false);
                          		Ext.getCmp('id-envia-tabla-error-lista-valores').setRawValue("N");
                           	}
                           	if(recLVMV=="S"){
                           		Ext.getCmp('id-modifica-valor-lista-valores').setValue(true);
                          		Ext.getCmp('id-modifica-valor-lista-valores').setRawValue("S");
                           	}else{
	                           	Ext.getCmp('id-modifica-valor-lista-valores').setValue(false);
                          		Ext.getCmp('id-modifica-valor-lista-valores').setRawValue("N");
                           	}                                                    	
                           	//alert("formatoClave"+recLV.get('formatoClave'));
                           	//alert("formatoDescripcion"+recLV.get('formatoDescripcion'));
                           	if(recLV.get('formatoClave') == 'A'){                           		
                				Ext.getCmp('id-formato-alfanumerico-clave-lista-valores').setValue(true);
                				Ext.getCmp('hidden-radio-clave-lista-valor').setValue('A');                	
                			}
                			if(recLV.get('formatoClave') == 'N'){                           		
                				Ext.getCmp('id-formato-numerico-clave-lista-valores').setValue(true);
                				Ext.getCmp('hidden-radio-clave-lista-valor').setValue('N');                	
                			}else{
                				Ext.getCmp('id-formato-alfanumerico-clave-lista-valores').setValue(true);
                				Ext.getCmp('hidden-radio-clave-lista-valor').setValue('A');
                			}
                			if(recLV.get('formatoDescripcion') == 'A'){                           		
                				Ext.getCmp('id-formato-alfanumerico-descripcion-lista-valores').setValue(true);
                				Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('A');                	
                			}
                			if(recLV.get('formatoDescripcion') == 'N'){                           		
                				Ext.getCmp('id-formato-numerico-descripcion-lista-valores').setValue(true);
                				Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('N');                	
                			}else{
                				Ext.getCmp('id-formato-alfanumerico-descripcion-lista-valores').setValue(true);
                				Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('A');       
                			}
                			
                           	//alert("numeroTabla"+recLV.get('numero'));							
                           	Ext.getCmp('hidden-numero-tabla-lista-valor').setValue(recLV.get('numero'));
                           	Ext.getCmp('hidden-nombre-tabla-lista-valor').setValue(recLV.get('nombre'));
                           	Ext.getCmp('hidden-tipo-transaccion-lista-valor').setValue('2');
                           	//Ext.getCmp('hidden-catalogo2-lista-valor').setValue(recLV.get('cdCatalogo2'));
                           	//Ext.getCmp('hidden-codigo-atributo-lista-valor').setValue(recLV.get('cdAtribu'));
                           	   
                           	store.baseParams = store.baseParams || {};								
							store.baseParams['tabla'] = tabla;	
							store.load({
								params: {
									start: startParam, 
									limit: limitParam
									}
								});
							
					}
        });

}
     var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'atributosVariables/CargaCatalogos.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaDeValores',
            id: 'listaDeValores'
	        },[
           {name: 'codigoTabla', type: 'string',mapping:'cdCatalogo1'},
           {name: 'descripcionTabla', type: 'string',mapping:'dsCatalogo1'}    
        ]),
		remoteSort: true
    });
       ds.setDefaultSort('listaDeValores', 'desc'); 
       
     var comboCatalogo1 = new Ext.form.ComboBox({
    	tpl: '<tpl for="."><div ext:qtip="{codigoTabla}" class="x-combo-list-item">{descripcionTabla}</div></tpl>',
    	store: ds,
    	displayField:'descripcionTabla',
    	//valueField: 'codigoTabla',
    	//maxLength : '30',
    	//maxLengthText : 'Treinta caracteres maximo',
	    typeAhead: true,
    	mode: 'local',
    	triggerAction: 'all',
    	emptyText:'Seleccione una dependencia',
    	selectOnFocus:true,
    	fieldLabel: 'Cat\u00E1logo 1',
    	listWidth:'250',
    	id:'catalogo1',
    	name:"catalogo1",
    	vtypeText:'Cat\u00E1logo uno igual al cat\u00E1logo dos',
        vtype: 'validaCatalogo',
        initialPassField: 'catalogo2'
    
	  });      
 
 	var ds2 = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'atributosVariables/CargaCatalogos2.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaDeValores2',
            id: 'listaDeValores2'
	        },[
           {name: 'codigoTabla', type: 'string',mapping:'cdCatalogo1'},
           {name: 'descripcionTabla', type: 'string',mapping:'dsCatalogo1'}    
        ]),
		remoteSort: true
    });
       ds2.setDefaultSort('listaDeValores2', 'desc'); 
       
    var dsClaveDependencia = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: 'atributosVariables/CargaClaveDependcia.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaClavesDependencia',
            id: 'key'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}    
        ]),
		remoteSort: true,
		baseParams: {codigoTabla:''}
		
    });
       ds.setDefaultSort('listaDependencia', 'desc');    
       
    
	var comboCatalogo2 = new Ext.form.ComboBox({
    	tpl: '<tpl for="."><div ext:qtip="{codigoTabla}" class="x-combo-list-item">{descripcionTabla}</div></tpl>',
    	store: ds2,
    	displayField:'descripcionTabla',
    	//maxLength : '30',
    	//maxLengthText : 'Treinta caracteres maximo',
    	typeAhead: true,
    	mode: 'local',
    	triggerAction: 'all',
    	emptyText:'Seleccione una dependencia',
    	selectOnFocus:true,
    	fieldLabel: 'Cat\u00E1logo 2',
    	listWidth:'250',
    	id: 'catalogo2',
    	name:"catalogo2",
    	vtypeText:'Cat\u00E1logo dos igual al cat\u00E1logo uno',
        vtype: 'validaCatalogo',
        initialPassField: 'catalogo1',
        
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
								var valor=record.get('codigoTabla');
								
								//dsClaveDependencia.reset();
								dsClaveDependencia.baseParams['codigoTabla'] = valor;
								dsClaveDependencia.reload({
				                	  callback : function(r,options,success) {
				                      		if (!success) {
		                				         Ext.MessageBox.alert('No se encontraron registros');
						                         //mStore.removeAll();
                							  }
				        		      }

					              });
								Ext.getCmp('combo-clave-dependencia').setValue('');
								
							}
    
	}); 
	
	
	
	var claveDependencia = new Ext.form.ComboBox({
    	tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
    	store: dsClaveDependencia,
    	id:'combo-clave-dependencia',
    	displayField:'value',    	
    	typeAhead: true,
    	mode: 'local',
    	triggerAction: 'all',
    	emptyText:'Seleccione una clave',
    	selectOnFocus:true,
    	fieldLabel: 'Clave',
    	listWidth:'250',    	
    	name: 'descripcionDependencia',
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
								Ext.getCmp('hidden-clave-dependencia').setValue(valor);
								
							}
    	
    
	}); 
	
	
 //***********************************************************      
        


      var listasDeValoresForm = new Ext.form.FormPanel({
            	
            	frame:true,
            	baseCls: 'x-plain',
            	id:'listas-form',
            	labelAlign: 'left',
            	url:'atributosVariables/GuardarListaDeValores.action',
            	labelWidth: 80,
        		//title: 'Creacion De Listas De Valores',
       			bodyStyle:'padding:5px',
        		width:600,
        		buttonAlign: "center",
                
                items: [{xtype:'hidden',id:'hidden-nombre-tabla-lista-valor',name:'tabla'},
                		{xtype:'hidden',id:'hidden-numero-tabla-lista-valor',name:'nmTabla'},
                		{xtype:'hidden',id:'hidden-tipo-transaccion-lista-valor',name:'tipoTransaccion'},
                		{xtype:'hidden',id:'hidden-radio-clave-lista-valor',name:'hiddenRadioClave'},
                		{xtype:'hidden',id:'hidden-radio-descripcion-lista-valor',name:'hiddenRadioDescripcion'},
                		{xtype:'hidden',id:'radio-clave-lista-valor',name:'radioClave'},
                		{xtype:'hidden',id:'radio-descripcion-lista-valor',name:'radioDescripcion'},
                		{xtype:'hidden',id:'hidden-clave-dependencia',name:'claveDependencia'},
                	
                	{
        			layout:'column',
        			baseCls: 'x-plain',
        			border: false,
            				items:[{
				                columnWidth:.30,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
				                items: [
				                	new Ext.form.TextField({
                						name:'nombre',
               							fieldLabel: 'Nombre*',
                						allowBlank: false,
                						blankText : 'El Nombre es requerido ',
                						readOnly: edita? true : false,
                						width: 80
                						})
				                ]
        					},{
               					columnWidth:.42,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.TextField({
        								name: 'descripcion',
        								fieldLabel: 'Descripci\u00F3n*',
        								allowBlank: false,
        								blankText : 'La Descripci\u00F3n es requerida',
        								width: 160  
    								})   
               					]
				    		},{
               					columnWidth:.28,
			                	layout: 'form',
			                	baseCls: 'x-plain',			                	
			                	border: false,
               					items: [
               						new Ext.form.TextField({
            							name: 'numero',
            							fieldLabel: 'N\u00FAmero',       		 						
    									disabled:true,
            							width: 80
    								})
               					]
				            }]
        		
		        },{
        			layout:'column',
        			border: false,
        			baseCls: 'x-plain',
            				items:[{
				                columnWidth:.32,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
				                items: [
				                	new Ext.form.Checkbox({
				                	id:'id-envia-tabla-error-lista-valores',
                					name:'enviaTablaError',
                					fieldLabel: 'Enviar a Tabla de Errores',
            						hideLabel:true,	
                					checked:true,
                					hidden:true,
                					inputValue: "S",
                					onClick:function(){ 
				            			if(this.getValue()){
				            				this.setRawValue("S");				            	
				            			}
				            		}                 
            						})
				                ]
        					},{
               					columnWidth:.45,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
				                	new Ext.form.Checkbox({
				                	id:'id-modifica-valor-lista-valores',
                					name:'modificaValor',
                					fieldLabel: 'Modificar Valores',
            						hideLabel:true,			
                					checked:true,
                					hidden:true,
                					inputValue: "S",
                					onClick:function(){ 
				            			if(this.getValue()){
				            				this.setRawValue("S");				            	
				            			}
				            		}                 
            						})               						
               					]
				    		},{
               					columnWidth:.23,
			                	layout: 'form',
			                	baseCls: 'x-plain',			                	
			                	border: false
               					
				            }]
        		
		        },{
            xtype:'fieldset',
            title: 'Clave',
            collapsible: true,
            autoHeight:true,     
            frame:true,
            items :[{
                    layout:'form',
                    baseCls: 'x-plain',
		        	border: false,
		        	items:[{
				   		
				   		border: false,				   		
				        layout:'form',
				        baseCls: 'x-plain',
				        border: false,
				        items:[
				           		new Ext.form.TextField({
				           		id:'id-descripcion-clave-lista-valores',
        						fieldLabel: 'Clave*',
        						allowBlank: false,
        						blankText : 'La clave es requerida',
        						name: 'clave',
        						width: 160   
    							})
				      	]
				                	
				    }]     		
                },{
        			layout:'column',
        			border: false,
        			baseCls: 'x-plain',
            				items:[{
				                columnWidth:.32,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
				                items: [				                	
				                	new Ext.form.Radio({
		            				//allowBlank: false,
            						id:'id-formato-alfanumerico-clave-lista-valores',
        							name:'formatoClave',
            						fieldLabel: 'Formato*',            				
            						boxLabel: 'Alfanum\u00E9rico',
            						checked:true,            				
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						            								
            								Ext.getCmp('radio-clave-lista-valor').setValue('A');          
            								Ext.getCmp('hidden-radio-clave-lista-valor').setValue('A');
            								mostrarLimitesClave(true);
				            			}
			            			}
            						})
				                ]
        					},{
               					columnWidth:.22,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.Radio({
    								//allowBlank: false,
            						id:'id-formato-numerico-clave-lista-valores',
        							name:'formatoClave',
            						labelSeparator : "",
            						boxLabel: 'Num\u00E9rico',
            						hideLabel:true,        
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						
            								Ext.getCmp('radio-clave-lista-valor').setValue('N');  
            								Ext.getCmp('hidden-radio-clave-lista-valor').setValue('N');
            								mostrarLimitesClave(true);
				            			}
			            			}
            						})
               					]
				    		},{
               					columnWidth:.22,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.Radio({
    								//allowBlank: false,
            						id:'id-formato-porcentaje-clave-lista-valores',
        							name:'formatoClave',
            						labelSeparator : "",
            						boxLabel: 'Porcentaje',
            						hideLabel:true,        
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						
            								Ext.getCmp('radio-clave-lista-valor').setValue('P');  
            								Ext.getCmp('hidden-radio-clave-lista-valor').setValue('P');
            								mostrarLimitesClave(false);
				            			}
			            			}
            						})
               					]
				    		},{
               					columnWidth:.22,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.Radio({
    								//allowBlank: false,
            						id:'id-formato-fecha-clave-lista-valores',
        							name:'formatoClave',
            						labelSeparator : "",
            						boxLabel: 'Fecha',
            						hideLabel:true,        
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						
            								Ext.getCmp('radio-clave-lista-valor').setValue('F');  
            								Ext.getCmp('hidden-radio-clave-lista-valor').setValue('F');
            								mostrarLimitesClave(false);
				            			}
			            			}
            						})
               					]
				    		}]
        		
		        },{
        			layout:'column',
        			border: false,
        			baseCls: 'x-plain',
        			id: 'layoutColumnClaveMinMax',
					items:[
						{
				       		columnWidth:.3,             					
			               	layout: 'form',
			               	baseCls: 'x-plain',
			               	border: false,
               				items: [
	               				new Ext.form.NumberField({				            
									id:'id-minimo-clave-lista-valores',        						
            						name:'minimoClave',
            						fieldLabel: 'M\u00EDnimo*',
	            					allowDecimals : false,
				           			allowNegative : false,
        			    			allowBlank: false,
        			    			blankText : 'Dato num\u00E9rico requerido',            				
            						hideParent:true,            				
            						width:30	            							       
           						})
							]
				    	},{
               				columnWidth:.3,
			               	layout: 'form',
			               	baseCls: 'x-plain',			                	
			               	border: false,
               				items: [
               					new Ext.form.NumberField({				            
            					id:'id-maximo-clave-lista-valores',
        						name:'maximoClave',
            					fieldLabel: 'M\u00E1ximo*',
            					allowDecimals : false,
				           		allowNegative : false,
        			    		allowBlank: false,
        			    		blankText : 'Dato num\u00E9rico requerido',            				
            					hideParent:true,
            					width:30	            							       
           						})
               				]
						}
					]
				}
			]
        	},{
            xtype:'fieldset',
            title: 'Descripcion',
            collapsible: true,
            autoHeight:true,
            frame:true,
            items :[{
                    layout:'form',
                    baseCls: 'x-plain',
		        	border: false,
		        	items:[{
				   		
				   		border: false,				   	
				        layout:'form',
				        baseCls: 'x-plain',
				        border: false,
				        items:[
				           		new Ext.form.TextField({
        						name: 'descripcionFormato',
        						fieldLabel: 'Descripci\u00F3n*',
        						allowBlank: false,
        						width: 160  
    	   						})
				        ]
				                	
				    }]     		
                },{
        			layout:'column',
        			border: false,
        			baseCls: 'x-plain',
            				items:[{
				                columnWidth:.32,
                				layout: 'form',
                				baseCls: 'x-plain',
                				border: false,
				                items: [				                	
				                	new Ext.form.Radio({
	  								allowBlank: false,
		            				name:'formatoDescripcion',
		            				id:'id-formato-alfanumerico-descripcion-lista-valores',
            						fieldLabel: 'Formato*',            				
            						boxLabel: 'Alfanum\u00E9rico',
            						checked:true,             				
  	  	       						onClick: function(){			            		
		            					if(this.getValue()){					            	            						
            								Ext.getCmp('radio-descripcion-lista-valor').setValue('A');  
            								Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('A');
            								mostrarLimitesDescripcion(true);
				            			}
			            			}
            						})
				                ]
        					},{
               					columnWidth:.22,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.Radio({
            						allowBlank: false,
            						name:'formatoDescripcion',
            						id:'id-formato-numerico-descripcion-lista-valores',
            						labelSeparator : "",
            						boxLabel: 'Num\u00E9rico',
            						hideLabel:true,            				
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						
            								Ext.getCmp('radio-descripcion-lista-valor').setValue('N');
            								Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('N');
            								mostrarLimitesDescripcion(true);
				            			}
			            			}
            						})
               					]
				    		},{
               					columnWidth:.22,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.Radio({
            						allowBlank: false,
            						name:'formatoDescripcion',
            						id:'id-formato-porcentaje-descripcion-lista-valores',
            						labelSeparator : "",
            						boxLabel: 'Porcentaje',
            						hideLabel:true,            				
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						
            								Ext.getCmp('radio-descripcion-lista-valor').setValue('P');
            								Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('P');
            								mostrarLimitesDescripcion(false);
				            			}
			            			}
            						})
               					]
				    		},{
               					columnWidth:.22,
			                	layout: 'form',
			                	baseCls: 'x-plain',
			                	border: false,
               					items: [
               						new Ext.form.Radio({
            						allowBlank: false,
            						name:'formatoDescripcion',
            						id:'id-formato-fecha-descripcion-lista-valores',
            						labelSeparator : "",
            						boxLabel: 'Fecha',
            						hideLabel:true,            				
  	  	       						onClick: function(){			            		
            							if(this.getValue()){					            	            						
            								Ext.getCmp('radio-descripcion-lista-valor').setValue('F');
            								Ext.getCmp('hidden-radio-descripcion-lista-valor').setValue('F');
            								mostrarLimitesDescripcion(false);
				            			}
			            			}
            						})
               					]
				    		}]
        		
		        },{
		        	layout:'column',
        			border: false,
        			baseCls: 'x-plain',
        			id: 'layoutColumnDescMinMax',
					items:[
						{
				       		columnWidth:.3,             					
			               	layout: 'form',
			               	baseCls: 'x-plain',
			               	border: false,
               				items: [
								new Ext.form.NumberField({				            
            						name:'minimoDescripcion',
            						id:'id-minimo-desc-lista-valores',   
            						fieldLabel: 'M\u00EDnimo*',
            						allowDecimals : false,
				            		allowNegative : false,
        				    		allowBlank: false,
        				    		blankText : 'Dato num\u00E9rico requerido',               				
            						hideParent:true,            				
            						width:30	 							       
								})
							]
				    	},{
               				columnWidth:.3,
			               	layout: 'form',
			               	baseCls: 'x-plain',			                	
			               	border: false,
               				items: [
               					new Ext.form.NumberField({				            
            						name:'maximoDescripcion',
            					    id:'id-maximo-desc-lista-valores',
            						fieldLabel: 'M\u00E1ximo*',
            						allowDecimals : false,
				            		allowNegative : false,
        				    		allowBlank: false,
        				    		blankText : 'Dato num\u00E9rico requerido',            				
            						hideParent:true,            				
            						width:30
           						})
               				]
						}
					]
				}
            ]
        },{
            xtype:'fieldset',
            title: 'Dependencias',
            collapsible: true,
            autoHeight:true,
            frame:true,
            items :[{
                    layout:'form',
                    baseCls: 'x-plain',
		        	border: false,
		        	items:[{
				   		
				   		border: false,				   		                			
				        layout:'form',
				        baseCls: 'x-plain',
				        border: false,
				        items:[{
				        		layout:'column',
        						border: false,
			        			baseCls: 'x-plain',
            					items:[{
					                columnWidth:.5,
                					layout: 'form',
                					baseCls: 'x-plain',
                					border: false,
				                	items: [		
						           		comboCatalogo1
						           		]
            					}/*,{
					                columnWidth:.5,
                					layout: 'form',
                					baseCls: 'x-plain',
                					border: false,
				                	items: [{
				                    xtype:'button',
				                    text: 'Agregar al catalogo de 1 clave ',                				    
				                    //buttonAlign: "left",
                				    handler: function() {                    	
				               			creaListasDeValores(ds);               			   				
         							}	
				                }]
            					}*/]
				        },{
				        		layout:'column',
        						border: false,
			        			baseCls: 'x-plain',
            					items:[{
					                columnWidth:.5,
                					layout: 'form',
                					baseCls: 'x-plain',
                					border: false,
				                	items: [		
						           		comboCatalogo2
						           		]
            					},{
					                columnWidth:.5,
                					layout: 'form',
                					baseCls: 'x-plain',
                					border: false,
				                	items: [{
				                    xtype:'button',
				                    text: 'Agregar al catalogo de 5 claves',                				    
				                    //buttonAlign: "left",
                				    handler:function(){
		        					TablasDeApoyo(ds2);
		        					}
				                }]
            					}]
				        },					           		
				           		claveDependencia
				        ]
				                	
					}]     		
            }]
        },grid2]

        
    });
    
    var listaValoresForm = new Ext.form.FormPanel({
            	
            	frame:true,
            	url:'atributosVariables/EliminaCargaManual.action',
       			bodyStyle:'padding:5px',
        		width:600,
        		baseCls: 'x-plain',
                items: [{
 				    border: false	
		        }]
    });
    
    
    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Creaci\u00F3n de Listas de Valores',
        width: 650,
        height:650,
        //minWidth: 650,
        //minHeight: 500,
        //autoHeight : true,
        //autoScroll : true, 
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
        listeners: {
            	close: function (){firstLoad = true;}
        },
        items: [{
        		layout:'form',
        		//autoScroll:true,
        		items:[
        				listasDeValoresForm,
 		       			//grid2,
        				listaValoresForm]
        		}],
		listeners: {
        	'beforeshow':	function(cmb){
        		if (storeListaValores != null && storeListaValores != "undefined") {
        		storeListaValores.load(
        		{
        		callback : function (r, options, success) {
        			dsClaveDependencia.baseParams['codigoTabla'] = storeListaValores.getAt(0).get('cdCatalogo2');
					dsClaveDependencia.load();
				Ext.getCmp('combo-clave-dependencia').setValue(storeListaValores.getAt(0).get('claveDependencia'));
        			
        		}
        		});
        		
				}
				//dsClaveDependencia.reset();
				
        	}
        },
        buttons: [{
	            	text: 'Guardar',
	            	handler: function() {
                	// check form value   
	            		    
                		if (listasDeValoresForm.form.isValid()) {
                	       
	                			var validaMinMaxVal  = 1;
	                			var validaMinMaxDesc = 1;
	                			
	                	       if(  Ext.getCmp('layoutColumnClaveMinMax').isVisible()
		                         ){
	                                  var valorMinVal = Ext.getCmp('id-minimo-clave-lista-valores').getValue();
		            	              var valorMaxVal = Ext.getCmp('id-maximo-clave-lista-valores').getValue();
		            	               
		            		          if ( validaValorMinMax ( valorMinVal , valorMaxVal  )  ){
		            		 	         Ext.MessageBox.alert('Error','Clave. El valor M\u00EDnimo debe de ser menor al valor M\u00E1ximo');	
		            		             validaMinMaxVal  = 0;
		            		          }
		                           } 
		            	  
		                       if(  Ext.getCmp('id-minimo-desc-lista-valores').isVisible() &&
		                          Ext.getCmp('id-maximo-desc-lista-valores').isVisible()
		                       ){   
	                               var valorMinDesc =  Ext.getCmp('id-minimo-desc-lista-valores').getValue();   
		            		       var valorMaxDesc =  Ext.getCmp('id-maximo-desc-lista-valores').getValue();
		            	           
		            		       if( validaValorMinMax ( valorMinDesc , valorMaxDesc  )  ){
		            		          Ext.MessageBox.alert('Error','Descripci\u00F3n. el valor M\u00EDnimo debe de ser menor al valor M\u00E1ximo');	
		            		          validaMinMaxDesc = 0;
		            		       }
		                       } 
		            	    
		            		   if(validaMinMaxVal &&  validaMinMaxDesc ){
		            		   	
		            		   	var recordsModifs = store.getModifiedRecords();
		            		   	var cargaManualOk = true;
		            		   	if(recordsModifs.length > 0) cargaManualOk = validarCargaManual(recordsModifs);
		            		   	
		            		   	if(cargaManualOk){
		            		   		
		            		   		var _params = "";
		            		   		
		            		   		//Parametros a enviar para agregar o hacer Update
		            		   		for(var i = 0; i < recordsModifs.length; i++){
		            		   			var idOriginal = "";
		            		   			var recordParam =  recordsModifs[i];
		            		   			var tipoOp = recordParam.get("nick"); // variable auxiliar, ya que si se modifica el mismo record genera error (ya que se alteran los modifieds recors y crea incongruencias  
		            		   			 //para los elementos que se les hace Update de Clave y Valor O unicamente su Valor
		            		   			if( tipoOp != "I"){
		            		   				//if para en caso de que solo se haya modificado el otValue, y no el otClave, hace un update y no hay necesidad de borrar el regsistro original
		            		   				//ya que en el algunos casos cuando no se ha modificado el otClave el lo regresa como undefined
		            		   				if(recordParam.modified.otClave){
		            		   					tipoOp = "I";
		            		   					idOriginal = recordParam.modified.otClave;
		            		   					_paramsEliminar.push(recordParam.modified.otClave);
		            		   				}else {
		            		   					tipoOp = "U";
		            		   				}
		            		   			}
		            		   			
		            		   			_params +=  "listaCargaManual[" + i + "].id=" + idOriginal
		            		   					+ "&&listaCargaManual[" + i + "].key=" + recordParam.get("otClave")
		            		   					+ "&&listaCargaManual[" + i + "].value=" + recordParam.get("otValor")
		            		   					+ "&&listaCargaManual[" + i + "].nick=" + tipoOp + "&&";
		            		   					
		            		   			
		            		   		}
		            		   		
		            		   		//Parametros para aquellos elementos a eliminar.
		            		   		for(var i = 0; i < _paramsEliminar.length; i++){
		            		   			var recordParam =  _paramsEliminar[i];
		            		   			_params +=  "listaClavesDependencia[" + i + "].key=" + recordParam + "&&";  
		            		   		}
		            		   		
			 		        	 	listasDeValoresForm.form.submit({			      
						            	waitTitle:'Espere',
						            	waitMsg:'Procesando...',
						            	method: 'POST',
						            	params: _params,
						            	failure: function(form, action) {
									    	//Ext.MessageBox.alert('Error', action.result.actionErrors[0]);
									    	//Ext.MessageBox.alert('Error Borrando Menu', action.result.errorInfo);
		                                	var mensajeRes = Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta;
								    		Ext.MessageBox.alert('Aviso', mensajeRes);
										},
										success: function(form, action) {
										    Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta, function(){
										    		store.commitChanges();
										    		store.load({
														params: {
																start: startParam, 
																limit: limitParam
															}
													});
													
													if(!edita)window.close();
													dstore.load();
										    	});						    		
								    		
								    		//action.form.reset();    		
										}
				        		 });
		            		    }//Fin de la validacion del grid de carga manual
		            		   }//closed val max min	
				        		
	                			
                		} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Cancelar',
	            	handler: function(){
	            		window.close();
	            	}
    		    },
    		    {
	            	text: 'Cerrar',
	            	handler: function(){
	            		window.close();
	            	}
    		    }]    


    });
    
    function validarCargaManual(recordsModifs){
    	
    	for(var i = 0; i < recordsModifs.length; i++){
    		var recordVal = recordsModifs[i];
    		
    		if(Ext.isEmpty(recordVal.get("otValor"))){
    			Ext.MessageBox.alert('Error', 'Favor de llenar los datos requeridos en la carga Manual.', function(){
    				grid2.startEditing( store.indexOf(recordVal) , 1);
    			});
    			return false;
    		}
    		
    		if(Ext.isEmpty(recordVal.get("otClave"))){
    			Ext.MessageBox.alert('Error', 'Favor de llenar los datos requeridos en la carga Manual.', function(){
    				grid2.startEditing( store.indexOf(recordVal) , 0);
    			});
    			return false;
    		}
    		
    	}
    	
    	return true;
    }
    
	window.show();
    ds.load({params:{start:0, limit:25}});    
    //ds2.load({params:{start:0, limit:25}});
    ds2.load({params:{start:0, limit:25}});  

};




borradoListaValores = function(dataStoreTabla , row , tablaform ) {
	                  
	            		Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           					if(btn == 'yes'){
           						var rec = dataStoreTabla.getAt( row );
                                var numeroTabla= rec.get('numeroTabla');
		                        var codigoTabla = rec.get('codigoTabla');   
                                
           					     //alert("numeroTabla="+  numeroTabla );
           					     //alert("codigoTabla=" + codigoTabla );
                                
	            				tablaform.form.submit({       
					            	url:'atributosVariables/EliminarTablaClave.action' +'?nmTabla=' + numeroTabla,
					            	waitTitle:'Espere',
					            	waitMsg:'Eliminando tabla...',
					            	failure: function(form, action) {
								    	Ext.MessageBox.alert('Error ', 'Error al eliminar Tabla');
									},
									success: function(form, action) {
								    	Ext.MessageBox.alert('Status', 'La tabla fue eliminada con &eacute;xito');						    		
						    			dataStoreTabla.reload();  		
									}
			        			});  
			        		}
						});	
	
};  

function mostrarLimitesClave(mostrar){
	//Esconder panel contenedor de campos min y max de Clave
	if(mostrar){
		Ext.getCmp('layoutColumnClaveMinMax').show();
		Ext.getCmp('id-minimo-clave-lista-valores').allowBlank = false;
		Ext.getCmp('id-maximo-clave-lista-valores').allowBlank = false;
	}else{
		Ext.getCmp('layoutColumnClaveMinMax').hide();
		Ext.getCmp('id-minimo-clave-lista-valores').reset();
		Ext.getCmp('id-minimo-clave-lista-valores').allowBlank = true;
		Ext.getCmp('id-maximo-clave-lista-valores').reset();
		Ext.getCmp('id-maximo-clave-lista-valores').allowBlank = true;
	}
}

function mostrarLimitesDescripcion(mostrar){
	//Esconder panel contenedor de campos min y max de Descripcion
	if(mostrar){
		Ext.getCmp('layoutColumnDescMinMax').show();
		Ext.getCmp('id-minimo-desc-lista-valores').allowBlank = false;
		Ext.getCmp('id-maximo-desc-lista-valores').allowBlank = false;
	}else{
		Ext.getCmp('layoutColumnDescMinMax').hide();
		Ext.getCmp('id-minimo-desc-lista-valores').reset();
		Ext.getCmp('id-minimo-desc-lista-valores').allowBlank = true;
		Ext.getCmp('id-maximo-desc-lista-valores').reset();
		Ext.getCmp('id-maximo-desc-lista-valores').allowBlank = true;
	}
}

function validaValorMinMax( valorMin , valorMax ){
  		if( valorMin > valorMax ){
            return 1;
        }else{
        	return 0;
        }	
    }		
    
