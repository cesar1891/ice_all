var selectedId;
var grid2;
var store;    
var storeVariablesRol;
var afuera;
var temporal=-1;   
var NUMERO_MAXIMO_ATRIBUTOS_VARIABLES_ROL = 50;
Ext.onReady(function() {      
        Ext.form.Field.prototype.msgTarget = "side";

            
    var masDeUnoCheck= new Ext.form.Checkbox({
				            id:'mas-de-uno-check-rol',
            				name:'numeroMaximo',
            				boxLabel: 'Mas de uno?',
				            hideLabel:true,
				            onClick:function(){
								if(this.getValue()){
				    			this.setRawValue("2");				            	
				    		}
		}
            							       
           });
           
     var domicilioCheck= new Ext.form.Checkbox({
     						id:'domicilio-check-rol',
				            name:'domicilio',
            				boxLabel: 'Domicilio?',
				            hideLabel:true,
				            onClick:function(){
								if(this.getValue()){
				    			this.setRawValue("S");				            	
				    		}
		}
            							       
           });      
           
     var dsCatalogoRol = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url:'rol/CatalogoDeRoles.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoRoles',
            id: 'rolCombo'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}       
        ]),
		remoteSort: true
    });
    dsCatalogoRol.setDefaultSort('comboRol', 'desc'); 
  
 
 var codigoRol = new Ext.form.ComboBox({
 	id:'id-catalogo-roles-en-rol',
    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
    store: dsCatalogoRol,
    displayField:'value',
    valueField:'key',
    //maxLength : '30',
    //maxLengthText : 'Treinta caracteres maximo',
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    emptyText:'Seleccione un rol',
    selectOnFocus:true,
    fieldLabel: 'Rol*',
    listWidth: '210',
    allowBlank:false,
    blankText : 'Rol Requerido.',
    //id:'comboRol',
    name:"descripcionRol",
    width:130,
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
								var mStore = grid2.store;
								mStore.baseParams = mStore.baseParams || {};
								mStore.baseParams['codigoRol'] = valor;
								mStore.reload({
				                	  callback : function(r,options,success) {
				                      		if (!success) {
		                				         Ext.MessageBox.alert('No se encontraron registros');
						                         mStore.removeAll();
                							  }
				        		      }

					              });
								Ext.getCmp('hidden-combo-rol').setValue(valor);
								
							}
    
});            
 
    var dsCatalogoObligatorio = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url:'rol/CatalogoDeObligatorio.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoObligatorio',
            id: 'comboObligatorio'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}       
        ]),
		remoteSort: true
    });
    //dsCatalogoObligatorio.load(); 
    
    var obligatoriedadCombo = new Ext.form.ComboBox({
    		id:'id-combo-obligatoriedad-rol',
    		tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
	 		store: dsCatalogoObligatorio,
			displayField:'value',
			allowBlank:false,
    		blankText : 'Obligatoriedad Requerida.',
			listWidth: '130',
			//valueField: 'key',	    				
			typeAhead: true,
	    	mode: 'local',
			triggerAction: 'all',
			emptyText:'Seleccione obligatoriedad',
    		selectOnFocus:true,
   			fieldLabel: 'Obligatoriedad*',
			name:"descripcionComposicion",
    		width:130,
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
								Ext.getCmp('hidden-composicion-rol').setValue(valor);
								
							}				    			
	});             
   
//**************grid******************
	      
    
    function test(){
 			url='rol/CargaAtributosVariablesRol.action' 			 		 			
 			storeVariablesRol = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
	            	root:'listaAtributoVariable',   
    	        	totalProperty: 'totalCount'
        	    	//id: 'descripcionAtributoVariable'            	         	
	        	},[
	        		{name: 'descripcionAtributoVariable',  type: 'string',  mapping:'descripcionAtributoVariable'},
	        		{name: 'descripcionListaDeValores',  type: 'string',  mapping:'descripcionListaDeValores'},
	        		{name: 'switchObligatorio',  type: 'string',  mapping:'switchObligatorio'},
	        		{name: 'codigoAtributoVariable',  type: 'string',  mapping:'codigoAtributoVariable'},
	        		{name: 'codigoListaDeValores',  type: 'string',  mapping:'codigoListaDeValores'},
	        		{name: 'apareceCotizador',  type: 'string',  mapping:'apareceCotizador'},
	        		{name: 'modificaCotizador',  type: 'string',  mapping:'modificaCotizador'},
	        		{name: 'datoComplementario',  type: 'string',  mapping:'datoComplementario'},
	        		{name: 'obligatorioComplementario',  type: 'string',  mapping:'obligatorioComplementario'},
	        		{name: 'modificableComplementario',  type: 'string',  mapping:'modificableComplementario'},
	        		{name: 'apareceEndoso',  type: 'string',  mapping:'apareceEndoso'},
	        		{name: 'obligatorioEndoso',  type: 'string',  mapping:'obligatorioEndoso'},
	        		{name: 'modificaEndoso',  type: 'string',  mapping:'modificaEndoso'},	        			        		
	        		{name: 'dsPadre',  type: 'string',  mapping:'descripcionPadre'},
	        		{name: 'dsCondicion',  type: 'string',  mapping:'descripcionCondicion'},
	        		{name: 'padre',  type: 'string',  mapping:'codigoPadre'},
	        		{name: 'condicion',  type: 'string',  mapping:'codigoCondicion'},
	        		{name: 'orden',  type: 'string',  mapping:'orden'},
	        		{name: 'agrupador',  type: 'string',  mapping:'agrupador'},
	        		{name: 'retarificacion',  type: 'string',  mapping:'retarificacion'}
				]),
			//autoLoad:true,
			remoteSort: true,
            baseParams: {codigoRol:''}
    	});
    	storeVariablesRol.setDefaultSort('descripcionAtributoVariable', 'desc');
    	//store.load();
		return storeVariablesRol;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Atributos Variables",dataIndex:'descripcionAtributoVariable',	width: 190, sortable:true,id:'descripcionAtributoVariable'},
			{header: "Valor Descripci\u00F3n",dataIndex:'descripcionListaDeValores',	width: 190, sortable:true},		    
		    {header: "Obligatorio",   dataIndex:'switchObligatorio',width: 190, sortable:true}
		    /*{header: "Aparece Cotizador",dataIndex:'apareceCotizador',	width: 50, sortable:true},		    
		    {header: "Modifica Cotizador",   dataIndex:'modificaCotizador',width: 50, sortable:true},
		    {header: "Dato Complementario",dataIndex:'datoComplementario',	width: 50, sortable:true},		    
		    {header: "Dato Obligatorio",   dataIndex:'obligatorioComplementario',width: 50, sortable:true},
		    {header: "Dato Modificable",dataIndex:'modificableComplementario',	width: 50, sortable:true},		    
		    {header: "Aparece Endoso",   dataIndex:'apareceEndoso',width: 50, sortable:true},
		    {header: "Endoso Obligatorio",dataIndex:'obligatorioEndoso',	width: 50, sortable:true},		    
		    {header: "Endoso Modificable",   dataIndex:'modificaEndoso',width: 50, sortable:true}
		    */
		   
		   	                	
        ]);
        //var grid2;
        
 		
 	function createGrid(){
 		
		grid2= new Ext.grid.GridPanel({
			store:test(),
			id:'grid-lista-atributos-variable-rol',
			border:true,
			//baseCls:' background:white ',
			cm: cm,
			sm: new Ext.grid.RowSelectionModel({
			singleSelect: true,
			/*listeners: {
        		rowselect: function(sm, row, rec) {
					//selectedId = store.data.items[row].id;
					//var sel = Ext.getCmp('grid-lista').getSelectionModel().getSelected();
					//selIndex = store.indexOf(sel);
					afuera=row;
					Ext.getCmp('eliminar-grid-rol').on('click',function(){
                		if(afuera!=temporal){
                			temporal=afuera;
                			Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
								if(btn == 'yes'){
									
									//Validar si el atributo variable tiene hijos asociados, para mostrar advertencia.
                        			rolesForm.form.submit({
            							url:'rol/ValidaHijosAtributoVariableRol.action',
            							params: {codigoAtributoVariable: grid2.getSelectionModel().getSelected().get('codigoAtributoVariable')},
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
       													//Eliminar Atributo Variable de Rol
       													eliminaAtributoVariableRol(storeVariablesRol,temporal,rolesForm);
       												}
												});
											}else{
												//Eliminar Atributo Variable de Rol
												eliminaAtributoVariableRol(storeVariablesRol,temporal,rolesForm);
											}
										}
		        					});
		        					
		        				}
							});
                        }
					});
					temporal=-1;
					Ext.getCmp('editar-grid-rol').on('click',function(){
						//agregarValoresGridWindow.show(store);
						//Ext.getCmp('formPanel').getForm().loadRecord(rec);
						if(afuera!=temporal){
							temporal=afuera;
							seleccionaAtributosVariables(storeVariablesRol,temporal);
						}
					});
					temporal=-1;   
	            }
			}*/
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agrega atributo variable',
            iconCls:'add',
            handler: function() {
				if (!Ext.isEmpty( Ext.getCmp('hidden-combo-rol') ) && !Ext.isEmpty( Ext.getCmp('hidden-combo-rol').getValue() )) {
					//Validacion que limita agregar mas Atributos Variables de los permitidos:
            		if(storeVariablesRol.getTotalCount() < NUMERO_MAXIMO_ATRIBUTOS_VARIABLES_ROL){
            			seleccionaAtributosVariables(storeVariablesRol);
            		}else{
            			Ext.MessageBox.alert('Error', 'Solo se permiten agregar ' + NUMERO_MAXIMO_ATRIBUTOS_VARIABLES_ROL + ' atributos variables.');
            		}
            	} else {
            		Ext.MessageBox.alert('Mensaje', 'Debe seleccionar un Rol primero para poder asociar sus atributos variables');
            	}
			}
        },'-',{
            text:'Eliminar',
            id:'eliminar-grid-rol',
            tooltip:'Elimina atributo variable',
            iconCls:'remove',
            handler : function()
            {
            	var gridTmp  = grid2;
            	var storeTmp = storeVariablesRol;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
						if(btn == 'yes'){
							
							//Validar si el atributo variable tiene hijos asociados, para mostrar advertencia.
                			rolesForm.form.submit({
    							url:'rol/ValidaHijosAtributoVariableRol.action',
    							params: {codigoAtributoVariable: grid2.getSelectionModel().getSelected().get('codigoAtributoVariable')},
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
													//Eliminar Atributo Variable de Rol
													eliminaAtributoVariableRol(storeVariablesRol,indexTmp,rolesForm);
												}
										});
									}else{
										//Eliminar Atributo Variable de Rol
										eliminaAtributoVariableRol(storeVariablesRol,indexTmp,rolesForm);
									}
								}
        					});
        					
        				}
					});
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        },'-',{
            text:'Editar',
            id:"editar-grid-rol",
            tooltip:'Edita atributo variable',
            iconCls:'option',
            handler : function()
            {
            	var gridTmp  = grid2;
            	var storeTmp = storeVariablesRol;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		seleccionaAtributosVariables(storeVariablesRol,indexTmp);
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        }/*,'-',{
            
            text:'<s:text name="productos.configRoles.btn.valDefectoPoliza"/>',
            tooltip:'Valor por defecto del rol'
            //iconCls:'option'
        }*/],      							        	    	    
    	width:620,
        height:190,
    	frame:false,
        title:'Atributos Variables',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true},                
		bbar: new Ext.PagingToolbar({
			pageSize:25,
			store: storeVariablesRol,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {1}',
			emptyMsg: "No hay resultados que mostrar"      		    	
			})        							        				        							 
		}); 	
	}
	
	createGrid();
	//store.load();
      
//*************grid*********************      
      var rolesForm = new Ext.form.FormPanel({
            	
            	frame:false,
            	//labelAlign: 'right',
        		title:'Asociaci&oacute;n De Roles',
        		url:'rol/InsertaRolInciso.action',
        		id: 'roles-form',
        		width:670,
        		//iconCls:'icon-grid',
        		//buttonAlign: "center",
                items: [{xtype:'hidden',id:'hidden-combo-rol',name:'codigoRol'},
                		{xtype:'hidden',id:'hidden-composicion-rol',name:'codigoComposicion'},
                	{
                	layout:'form',
                	border:false,
                	items:[{
                		layout:'form',
                		border:false
	                	},{
    	    			layout:'column',
    	    			border:false,
            				items:[{
				                columnWidth:.4,
                				layout: 'form',
                				labelAlign: 'right',
                				labelWidth: 25,
                				width:185,
                				border:false,
                				items: [//{xtype:'hidden',id:'hidden-combo-rol',name:'codigoRol'},
				                	codigoRol
				                ]
        					},{
				                columnWidth:.3,
                				layout: 'form',
                				border:false,
                				width:180,
				                items: [{
				                    xtype:'button',
				                    text: 'Agregar al cat\u00E1logo',
                				    name: 'AgregarAlCatalogo',
                				    buttonAlign: 'left',
                				    handler: function() {                    	
				               			creaCatalogoDeRoles(dsCatalogoRol);//script que tienes afuera
               			   				
         							}	
				                }]
        					},{
               					columnWidth:.3,
			                	layout: 'form',
			                	width:100,
			                	border:false,
			                	labelAlign: 'right',
               					items: [{
				                    xtype:'button',
				                    text: 'Eliminar',
                				    name: 'EliminarRol',
                				    tooltip: 'Eliminar rol',
				                    buttonAlign: 'right',
                				    handler: function() {
                				    	//TODO: IF validar que una opci�n est� elegida en el combo y este en el �rbol(si existe, eliminarla)
                				    	if (rolesForm.form.isValid()) {
                				    		Ext.MessageBox.confirm('Mensaje','Esta seguro de querer eliminar este elemento?', function(btn) {
           										if(btn == 'yes'){
		 		        							rolesForm.form.submit({
		 		        								url:'rol/EliminaRol.action',
					            						waitTitle:'Espere',
					            						waitMsg:'Procesando...',
					            						failure: function(form, action) {
					            							var mensajeRespuesta = Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta;
															Ext.MessageBox.alert('Error', 
																Ext.isEmpty(mensajeRespuesta) ? 'El Rol no pudo ser eliminado' : mensajeRespuesta);
														},
														success: function(form, action) {
															var mensajeRespuesta = Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta;
								    						Ext.MessageBox.alert('Status', 
																Ext.isEmpty(mensajeRespuesta) ? 'Rol eliminado' : mensajeRespuesta);
								    						form.reset();
								    						
								    						storeVariablesRol.removeAll();//se limpia grid de Atributos Variables de Rol
						    								Ext.getCmp('arbol-productos').getRootNode().reload();
														}
			        								});
												}
											});
                						} else{
											Ext.MessageBox.alert('Error', 'Seleccione un rol ya existente.');
										}
         							}
               					}]
				    		}]
				    	},{
    	    			layout:'column',
    	    			border:false,
            				items:[{
               					columnWidth:.3,
			                	layout: 'form',
			                	width:260,
			                	border:false,
			                	labelAlign: 'left',
               					items: [//{xtype:'hidden',id:'hidden-composicion-rol',name:'codigoComposicion'},
               						obligatoriedadCombo
               					]
				    		},{
				                columnWidth:.25,
                				layout: 'form',
                				labelAlign: 'right',
                				labelWidth: 20,
                				border:false,
                				items: [
				                	masDeUnoCheck
				                ]
        					},{
				                columnWidth:.2,
                				layout: 'form',
                				border:false,
                				labelAlign: 'right',
				                items: [
				                	domicilioCheck
				                ]
        					}]
				    }]		
        		
		        },grid2],
		         buttons: [{
	            	text: 'Guardar',
	            	handler: function() {
                	// check form value 
                		if (rolesForm.form.isValid()) {
		 		        	rolesForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Procesando...',
					            	failure: function(form, action) {
								    Ext.MessageBox.alert('Status', 'El Rol no pudo ser guardado');
									},
									success: function(form, action) {
								    Ext.MessageBox.alert('Status', 'El Rol fue guardado con exito');		
								    form.reset();  				    		
						    		rolesForm.form.load({url:'rol/LimpiarSesionAtributosVariablesRol.action'});
	            					storeVariablesRol.load();
						    		Ext.getCmp('arbol-productos').getRootNode().reload();
								}
			        		});                   
                		} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar los campos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Cancelar',
	            	handler: function(){
	            		rolesForm.form.load({url:'rol/LimpiarSesionAtributosVariablesRol.action'});
	            		
	            	}
    		    }]      
    });
    
  // Esta linea carga el arbol en el success cuando se agrega un rol ==>Ext.getCmp('arbol-productos').getRootNode().reload();
    rolesForm.render('center4rolls');
  	//grid2.render('center4rolls');
  	//dsCatalogoRol.load({params:{start:0, limit:25}}); 
        });
    