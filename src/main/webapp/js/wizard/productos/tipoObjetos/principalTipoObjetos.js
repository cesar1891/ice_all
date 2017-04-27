var grid2objetos;
var storeVariableObjeto;  
var NUMERO_MAXIMO_ATRIBUTOS_VARIABLES_OBJETO = 50;
Ext.onReady(function() {      
        Ext.form.Field.prototype.msgTarget = "side";

    
     var dsCatalogoObjeto = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url:'tipoObjeto/CatalogoTipoDeObjeto.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoTipoDeObjeto',
            id: 'objetoCombo'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}       
        ]),
		remoteSort: true
    });
    dsCatalogoObjeto.setDefaultSort('comboObjeto', 'desc'); 
  
 
 var comboObjeto = new Ext.form.ComboBox({
 	id:'id-catalogo-tipo-objeto',
    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
    store: dsCatalogoObjeto,
    displayField:'value',
    valueField:'key',
    //maxLength : '30',
    //maxLengthText : 'Treinta caracteres maximo',
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    emptyText:'Seleccione tipo de objeto',
    selectOnFocus:true,
    fieldLabel: 'Tipo de Objeto*',
    listWidth: '210',
    allowBlank:false,
    blankText : 'Tipo de objeto requerido.',
    name:"descripcionObjeto",
    width:130,
    selec : function() {
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
								var mStoreObjetos = grid2objetos.store;
								mStoreObjetos.baseParams = mStoreObjetos.baseParams || {};
								mStoreObjetos.baseParams['codigoObjeto'] = valor;
								mStoreObjetos.reload({
				                	  callback : function(r,options,success) {
				                      		if (!success) {
		                				         Ext.MessageBox.alert('No se encontraron registros');
						                         mStoreObjetos.removeAll();
                							  }
				        		      }

					              });
								Ext.getCmp('hidden-combo-objeto').setValue(valor);
								
							}
    
});            
 
//**************grid******************
	var afuera;
 	var temporal=-1;      
    
    function testObjetos(){        		        				
 			//url='tipoObjeto/CargaListaDatosVariablesObjeto.action' 			 		 			
 			storeVariableObjeto = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: 'tipoObjeto/CargaListaDatosVariablesObjeto.action'
        		}),
        		reader: new Ext.data.JsonReader({
	            	root:'listaDatosVariables',   
    	        	totalProperty: 'totalCount',
        	    	id: 'descripcionAtributoVariable'            	         	
	        	},[/*
	        		{name: 'descripcionAtributoVariable',  type: 'string',  mapping:'descripcionAtributoVariable'},
	        		{name: 'descripcionListaDeValores',  type: 'string',  mapping:'descripcionTabla'},
	        		{name: 'switchObligatorio',  type: 'string',  mapping:'switchObligatorio'},
	        		{name: 'codigoListaDeValores',  type: 'string',  mapping:'codigoTabla'}       		            
				*/
				
	        		//{name: 'codigoObjeto',  type: 'string',  mapping:'codigoObjeto'},
	        		{name: 'claveObjeto',  type: 'string',  mapping:'codigoObjeto'},
					{name: 'descripcion',  		type: 'string',  mapping:'descripcionAtributoVariable'},
	        		{name: 'formato',  			type: 'string',  mapping:'codigoFormato'},	        		
	        		{name: 'minimo',  			type: 'string',  mapping:'minimo'},
	        		{name: 'maximo',  			type: 'string',  mapping:'maximo'},
   	        		{name: 'obligatorio',  		type: 'string',  mapping:'switchObligatorio'},
	        		{name: 'modificaEmision',  	type: 'string',  mapping:'emision'},	        		
	        		{name: 'modificaEndoso',  	type: 'string',  mapping:'endoso'},
   	        		{name: 'retarificacion',  	type: 'string',  mapping:'retarificacion'},
	        		{name: 'codigoTabla',  		type: 'string',  mapping:'codigoTabla'},
   	        		{name: 'descripcionTabla',  type: 'string',  mapping:'descripcionTabla'},
	        		{name: 'codigoExpresion',  	type: 'string',  mapping:'codigoExpresion'},
	        		{name: 'claveCampo', 		type: 'string',	 mapping:'codigoAtributoVariable'},
	        		{name: 'padre', 		 	type: 'string',	 mapping:'dsAtributoPadre'},
	        		{name: 'agrupador', 		type: 'string',	 mapping:'agrupador'},
	        		{name: 'orden', 		 	type: 'string',	 mapping:'orden'},
	        		{name: 'condicion', 		type: 'string',	 mapping:'dsCondicion'},
	        		{name: 'apareceCotizador',  type: 'string',  mapping:'apareceCotizador'},	        		
	        		{name: 'datoComplementario',  type: 'string',  mapping:'datoComplementario'},
	        		{name: 'obligatorioComplementario',  type: 'string',  mapping:'obligatorioComplementario'},
	        		{name: 'modificableComplementario',  type: 'string',  mapping:'modificableComplementario'},
	        		{name: 'apareceEndoso',  type: 'string',  mapping:'apareceEndoso'},
	        		{name: 'obligatorioEndoso',  type: 'string',  mapping:'obligatorioEndoso'}
	        		    
				
				
				
				
				]),
			//autoLoad:true,
			remoteSort: true,
            baseParams: {codigoObjeto:''}
    	});
    	storeVariableObjeto.setDefaultSort('descripcion', 'desc');
    	//store.load();
		return storeVariableObjeto;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Nombre",			dataIndex:'descripcion',		width: 190, sortable:true,id:'descripcion'},
			{header: "Lista de valores",dataIndex:'descripcionTabla',	width: 190, sortable:true},		    
		    {header: "Obligatorio",   	dataIndex:'obligatorio',		width: 190, sortable:true}
		   
		   	                	
        ]);
        //var grid2objetos;
        var selectedId;
 		
 	function createGridobjetos(){
		grid2objetos= new Ext.grid.GridPanel({
			store:testObjetos(),
			id:'grid-lista-atributos-variables-objeto',
			border:true,
			//baseCls:' background:white ',
			cm: cm,
			sm: new Ext.grid.RowSelectionModel({
				singleSelect: true,
				/*listeners: {
					rowselect: function(sm, row, rec) {
						selectedId = storeVariableObjeto.data.items[row].id;
	    	 			Ext.getCmp('eliminar-grid-objeto').on('click',function(){
   							Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           						if(btn == 'yes'){
           							
           							//Validar si el atributo variable tiene hijos asociados, para mostrar advertencia.
                        			objetosForm.form.submit({
            							url:'tipoObjeto/ValidaHijosAtributoVariableObjeto.action',
            							params: {
            								//codigoObjeto: rec.get('claveObjeto'),
											descripcion: selectedId,
											claveCampo: rec.get('claveCampo')
										},
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
       													//Eliminar Atributo Variable de Objeto
       													eliminaAtributoVariableObjeto(storeVariableObjeto,selectedId,objetosForm, rec);
       												}
												});
											}else{
												//Eliminar Atributo Variable de Objeto
												eliminaAtributoVariableObjeto(storeVariableObjeto,selectedId,objetosForm, rec);
											}
										}
		        					});
	    	 						
								}
							});
						});
	                    afuera=row;
	                    Ext.getCmp('editar-grid-objeto').on('click',function(){
							//agregarValoresGridWindow.show(store);
							//Ext.getCmp('formPanel').getForm().loadRecord(rec);
							if(afuera!=temporal){
								temporal=afuera;
								CreaDatosVariablesObjeto(storeVariableObjeto,temporal);
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
	            	//Validacion que limita agregar mas Atributos Variables de los permitidos:
					if(storeVariableObjeto.getTotalCount() < NUMERO_MAXIMO_ATRIBUTOS_VARIABLES_OBJETO){
						CreaDatosVariablesObjeto(storeVariableObjeto);
					}else{
						Ext.MessageBox.alert('Error', 'Solo se permiten agregar ' + NUMERO_MAXIMO_ATRIBUTOS_VARIABLES_OBJETO + ' atributos variables.');
					}
				}
	        },'-',{
	            text:'Eliminar',
	            id:'eliminar-grid-objeto',
	            tooltip:'Elimina dato variable',
	            iconCls:'remove',
	            handler : function()
	            {
	            	var gridTmp  = grid2objetos;
	            	var storeTmp = storeVariableObjeto;
	            	debug(gridTmp.getSelectionModel().getSelected());
	            	if(gridTmp.getSelectionModel().hasSelection())
	            	{
	            		var rec = gridTmp.getSelectionModel().getSelected();
	            		debug('rec:',recordTmp);
	            		var row = storeTmp.indexOf(rec);
	            		debug('row:',indexTmp);
	            		//
	            		selectedId = storeVariableObjeto.data.items[row].id;
	            		Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
       						if(btn == 'yes'){
       							
       							//Validar si el atributo variable tiene hijos asociados, para mostrar advertencia.
                    			objetosForm.form.submit({
        							url:'tipoObjeto/ValidaHijosAtributoVariableObjeto.action',
        							params: {
        								//codigoObjeto: rec.get('claveObjeto'),
										descripcion: selectedId,
										claveCampo: rec.get('claveCampo')
									},
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
   													//Eliminar Atributo Variable de Objeto
   													eliminaAtributoVariableObjeto(storeVariableObjeto,selectedId,objetosForm, rec);
   												}
											});
										}else{
											//Eliminar Atributo Variable de Objeto
											eliminaAtributoVariableObjeto(storeVariableObjeto,selectedId,objetosForm, rec);
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
	            id:"editar-grid-objeto",
	            tooltip:'Edita atributo variable',
	            iconCls:'option',
	            handler : function()
	            {
	            	var gridTmp  = grid2objetos;
	            	var storeTmp = storeVariableObjeto;
	            	debug(gridTmp.getSelectionModel().getSelected());
	            	if(gridTmp.getSelectionModel().hasSelection())
	            	{
	            		var rec = gridTmp.getSelectionModel().getSelected();
	            		debug('rec:',recordTmp);
	            		var row = storeTmp.indexOf(rec);
	            		debug('row:',indexTmp);
	            		//
	            		CreaDatosVariablesObjeto(storeVariableObjeto,indexTmp);
	            		//
	            	}
	            	else
	            	{
	            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
	            	}
	            }
	        }/*,'-',{
	            text:'<s:text name="productos.configObjetos.btn.valDefectoPoliza"/>',
	            tooltip:'Valor por defecto del rol'
	            //iconCls:'option'
	        }*/],
	    	width:620,
	        height:190,
	    	frame:false,
	        title:'Atributos por Tipo de Objeto',
			bodyStyle:'padding:5px',
			//renderTo:document.body,
			viewConfig: {autoFill: true,forceFit:true},
			bbar: new Ext.PagingToolbar({
				pageSize:25,
				store: storeVariableObjeto,
				displayInfo: true,
				displayMsg: 'Displaying rows {0} - {1} of {1}',
				emptyMsg: "No rows to display"
			})
		});
	}
	
	createGridobjetos();
	//store.load();
      
//*************grid*********************      
      var objetosForm = new Ext.form.FormPanel({
            	
            	frame:false,
            	//labelAlign: 'right',
        		title:'Tipo De Objeto',
        		url:'tipoObjeto/AgregaTipoObjetoInciso.action',
        		id: 'objetos-form',
        		width:620,
        		//iconCls:'icon-grid',
        		//buttonAlign: "center",
                items: [{
                	layout:'form',
                	border:false,
                	items:[{
                		layout:'form',
                		border:false
	                	},{
    	    			layout:'column',
    	    			border:false,
            				items:[{
				                columnWidth:.5,
                				layout: 'form',
                				labelAlign: 'left',
                				labelWidth: 100,
                				width:270,
                				border:false,
                				items: [{xtype:'hidden',id:'hidden-combo-objeto',name:'codigoObjeto'},
                						//{xtype:'hidden',id:'hidden-combo-objeto',name:'codigoAtributoVariable'},
				                	comboObjeto
				                ]
        					},{
				                columnWidth:.5,
                				layout: 'form',
                				border:false,
                				//width:165,
                				//labelAlign: 'right',
				                items: [{
				                    xtype:'button',
				                    text: 'Agregar al cat\u00E1logo',
                				    name: 'AgregarAlCatalogo',
				                    buttonAlign: "left",
                				    handler: function() {                    	
				               			creaCatalogoTipoObjeto(dsCatalogoObjeto);//script que tienes afuera
               			   				
         							}	
				                }]
        					}]
				    }]		
        		
		        },grid2objetos],
		         buttons: [{
	            	text: 'Guardar',
	            	handler: function() {
                	// check form value 
                		if (objetosForm.form.isValid()) {
		 		        	objetosForm.form.submit({			      
					            	waitTitle:'Espere',
					            	waitMsg:'Procesando...',
					            	failure: function(form, action) {
								    Ext.MessageBox.alert('Status', 'El tipo de objeto no pudo ser guardado');
									},
									success: function(form, action) {
								    Ext.MessageBox.alert('Status', 'El tipo de objeto fue guardado con exito');		
								    form.reset();  				    		
						    		objetosForm.form.load({url:'tipoObjeto/LimpiarSesionesTipoObjeto.action'});
	            					storeVariableObjeto.load();
						    		Ext.getCmp('arbol-productos').getRootNode().reload();
								}
			        		});                   
                		} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar los campos requeridos.');
						}             
	        		}
    		    },{
	            	text: 'Eliminar',
	            	id: 'btnEliminarObjetosRiesgo',
	            	handler: function(){
	            		// check form value 
                		if (objetosForm.form.isValid()) {
	            			Ext.MessageBox.confirm('Aviso','Esta seguro que desea eliminar este elemento?', function(btn) {
           						if(btn == 'yes'){
	            					objetosForm.form.submit({
		            					url:'tipoObjeto/BorraTipoObjetoInciso.action',
	            						failure: function(form, action) {
			            					Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta );
										},
										success: function(form, action) {
											Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta );
											//Limpiar y Actualizar datos:
											form.reset();
											objetosForm.form.load({url:'tipoObjeto/LimpiarSesionesTipoObjeto.action'});
											storeVariableObjeto.load();
						    				Ext.getCmp('arbol-productos').getRootNode().reload();
										}
	            					});
								}
							});
						} else{
							Ext.MessageBox.alert('Error', 'Favor de llenar los campos requeridos.');
						}  
						
	            	}
    		    },{
	            	text: 'Cancelar',
	            	handler: function(){
	            		objetosForm.form.load({
		            		url:'tipoObjeto/LimpiarSesionesTipoObjeto.action',
		            		success: function(){
		            			storeVariableObjeto.load();
		            		},
		            		failure: function (){
		            			storeVariableObjeto.load();
		            		}
	            		});
	            		
	            		
	            	}
    		    }]      
    });
    
  // Esta linea carga el arbol en el success cuando se agrega un rol ==>Ext.getCmp('arbol-productos').getRootNode().reload();
    objetosForm.render('centerObjetos');
  	//dsCatalogoObjeto.load({params:{start:0, limit:25}}); 
});
