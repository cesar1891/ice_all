Ext.onReady(function(){
Ext.form.Field.prototype.msgTarget = "side";
    var grid2;
    var selectedId;
    var codigoPlan;
    var codigoProducto;
    var descripcionProducto;
    var descripcionPlan;
    var cm;
    var store;
    var ds;
function toggleDetails(btn, pressed){
            var view = grid.getView();
            view.showPreview = pressed;
            view.refresh();
        }
       
        var cm = new Ext.grid.ColumnModel([
            new Ext.grid.RowNumberer(),
            {header: "Clave",                   dataIndex:'key',    width: 100, sortable:true},         
            {header: "Nombre de la cobertura",  dataIndex:'value',  width: 220, sortable:true}
                                    
        ]);
function getSelectedRecord(){
        var m = grid2.getSelections();
            if (m.length == 1 ) {
                return m[0];
             }
    }
function createGrid(){
    grid2= new Ext.grid.GridPanel({
        id:'grid-planes-configuracion-coberturas',
        store:test(),
        border:true,
        cm: cm,                                                     
        width:500,
        frame:true,
        height:320,        
        title:'Coberturas',
        
        
        sm: new Ext.grid.RowSelectionModel({
            singleSelect: true,
            listeners: {                            
                        rowselect: function(sm, row, rec) {                                                                                                                                     
                        }
                }
        
        }),
        tbar:[{
            text:'Agregar',
            tooltip:'Agregar una nueva cobertura',
            handler:function(){
            agregar(ds);}
        },'-',{
            id:'editar',
            text:'Editar',
            tooltip:'Edita cobertura seleccionada',
            handler:function(){
                if (getSelectedRecord() != null) {
                    editarCatalogoPlanes();                                 
                    Ext.getCmp('editFormPlan').getForm().loadRecord(getSelectedRecord());   
                } else {
                Ext.MessageBox.alert('Aviso','Debe seleccionar un registro para realizar esta operacion');
                }
            }
        },'-',{
            id:'eliminar',
            text:'Eliminar',
            tooltip:'Elimina cobertura seleccionada',
            handler:function(){
                if (getSelectedRecord() != null) {
                	Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
						if(btn == 'yes'){
							var recordEliminar = getSelectedRecord();
							//Eliminando cobertura de plan
							var conn = new Ext.data.Connection();
			                conn.request ({
			                	url: 'planes/eliminaCoberturasPlan.action',
			                	method: 'POST',
			                	successProperty : '@success',
			                	params : {'cdGarant': recordEliminar.get('key')},
			                	callback: function (options, success, response) {
			                		Ext.Msg.alert('Estatus', Ext.util.JSON.decode(response.responseText).mensajeRespuesta);
			                		store.load();// se recarga grid de coberturas
			                		Ext.getCmp('arbol-productos').getRootNode().reload();
								}
							});
						}
					});
                } else {
                	Ext.MessageBox.alert('Aviso','Debe seleccionar un registro para realizar esta operacion');
                }
            }
        }],
        viewConfig: {autoFill: true,forceFit:true},                
        bbar: new Ext.PagingToolbar({
            pageSize:25,
            store: store,                                               
            displayInfo: true,
            displayMsg: 'Mostrando registros {0} - {1} de {2}',
            emptyMsg: "No hay resultados que mostrar"                      
            })                                                                                               
        });     
    }
    createGrid();
    
    
    
function test(){                                        
    url='planes/ListaCoberturas.action';                                
    store = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: url
        }),
        reader: new Ext.data.JsonReader({
            root:'coberturas',
            id:'cobertura',   
            totalProperty: 'totalCount'             
            },[
            {name: 'key',   type: 'string',  mapping:'key'},
            {name: 'value', type: 'string',  mapping:'value'}               
        ]),
        remoteSort: true,
        baseParams: {codigoPlan:''}
    });
    store.setDefaultSort('cobertura', 'desc');
    //store.load();
    return store;
        }
    var dsComboCobertura = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url:_ACTION_COBERTURAS+'?combo='+'cobertura'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaCoberturas',
            id: 'comboCobertura'
            },[
                {name: 'key',   type: 'string', mapping:'key'},
                {name: 'value', type: 'string', mapping:'value'}      
            ]),
        remoteSort: true
    });
    
    
    ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'planes/ListaPlanes.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'planes',
            id: 'listas'
            },[
                {name: 'key',   type: 'string',  mapping:'key'},
                {name: 'value', type: 'string',  mapping:'value'}    
            ]),
            remoteSort: true
        });
    
    ds.setDefaultSort('listas', 'desc');

/***************************************
*Se crea la ventana de agregar planes.  
****************************************/  
agregar = function() {
    
    
    var comboCobertura =new Ext.form.ComboBox({
            tpl: '<tpl for="."><div ext:qtip="{value}" class="x-combo-list-item">{value}</div></tpl>',
            store: dsComboCobertura,                        
            displayField:'value',
            listWidth: '250',
            maxHeight : '250',
            allowBlank:false,
            blankText :'Seleccione cobertura',
            valueField: 'key',                          
            mode: 'local',
            triggerAction: 'all',                           
            selectOnFocus:true,
            emptyText:'Seleccione cobertura...',
            fieldLabel:'Cobertura*',                 
            name:"descripcionCobertura",
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
                Ext.getCmp('hidden-combo-cobertura-planes').setValue(valor);
            }                           
        });
    var separador = new Ext.form.TextField({
        fieldLabel: '',
        hiddeParent:true,
        labelSeparator:'',
        hidden:true
        
    });
    
    
    var formPanel = new Ext.form.FormPanel({
        url:'planes/asociaCoberturaPlan.action',
        baseCls: 'x-plain',
        labelWidth: 75,
        width: 480,
        frame:true,
        height:130,
        items:[separador,{
                layout:'column',
                border:false,
                width: '450',
                baseCls: 'x-plain',
                items: [{columnWidth:.65,
                    layout:'form',
                    labelAlign:'left',
                    baseCls: 'x-plain',
                    border:false,
                    items:[comboCobertura,{xtype:'hidden',id:'hidden-combo-cobertura-planes',name:'codigoCondicion'}]
                    },{
                        columnWidth:.35,
                        layout:'form',              
                        border:false,
                        baseCls: 'x-plain',
                            items:[
                                {xtype:'button', 
                                text: 'Agregar al Catalogo', 
                                name: 'AgregarCatalogo',
                                buttonAlign:'left', 
                                handler: function() {
                                    creaAltaCatalogoCoberturas(dsComboCobertura);
                                    
                                }}
                            ]   
                        }]
                }]
        });

        var windowPlan = new Ext.Window({
            title: 'Configuracion de coberturas del plan',
            plain:true,
            modal:true,      
            width: 480,
            height:130,
            minWidth: 300,
            minHeight: 100,
            buttonAlign:'center',
            items: formPanel,
            buttons: [{
                text: 'Guardar', 
                handler: function() {                
                    if (formPanel.form.isValid()) {
                    	//Validacion: Si se eligio un plan antes(si no esta vacio), se permitira guardar la cobertura
                    	if( !Ext.isEmpty(codigoPlan.getValue()) ){
							formPanel.form.submit({
                            	waitTitle:'Espere',
                            	waitMsg:'Procesando...',
                            	failure: function(form, action) {
	                                Ext.MessageBox.alert('Error', Ext.util.JSON.decode(action.response.responseText).mensajeRespuesta);
                                	windowPlan.close();
                            	},
                            	success: function(form, action) {
	                            	Ext.MessageBox.alert('Estado', 'Cobertura agregada');
                                	windowPlan.close();
                                	store.load();
                                	Ext.getCmp('arbol-productos').getRootNode().reload();
                            	}
                        	});
                    	}else{
                    		Ext.MessageBox.alert('Aviso', 'Debe seleccionar un plan antes de agregar la cobertura');
                    	}
                    }else{
                        Ext.MessageBox.alert('Error', 'Inserte Campos Requeridos');
                    }             
                }
            },{
                text: 'Regresar',
                handler: function(){
                    windowPlan.close();
                }
            }]
        });
    windowPlan.show();
};
/*******CREAMOS LA VENTANA DE AGREGAR AL CATALOGO*************/
agregarCatalogoPlanes = function(ds){
    var clavePlan = new Ext.form.TextField({
        fieldLabel: 'Clave*',
        allowBlank: false,
        name:'cdPlan',
        maxLength: '8',
        maxLengthText: 'La Clave del plan debe ser de un M\u00C1ximo de 8 caracateres',
        blankText : 'Campo requerido',
        anchor:'90%'    
    });
    var nombrePlan = new Ext.form.TextField({
        fieldLabel: 'Nombre*',
        allowBlank: false,
        name:'dsPlan',
        maxLength: '100',
        maxLengthText: 'La Clave del plan debe ser de un M\u00C1ximo de 100 caracateres',
        blankText : 'Campo requerido',
        anchor:'90%'    
    });  
    var catalogoForm= new Ext.FormPanel({
        id:'agregar-catalogo-planes',
        labelWidth: 75,
        baseCls: 'x-plain',
        width: 350,
        frame:true,
        height:140,
        url:'planes/GuardaPlan.action',
        items:[clavePlan, nombrePlan]
    });
    var windowPlanAgregar = new Ext.Window({
            title: 'Agregar Nuevo Plan',
            plain:true,
            modal:true,      
            width: 350,
            height:140,
            minWidth: 300,
            minHeight: 100,
            buttonAlign:'center',
            items: catalogoForm,
            buttons: [{
                text: 'Guardar', 
                handler: function() {                
                    if (catalogoForm.form.isValid()) {
                        catalogoForm.form.submit({  
                            waitTitle:'Espere',
                            waitMsg:'Procesando...',
                            failure: function(form, action) {
                                Ext.MessageBox.alert('Error','Error agregando un plan');
                                windowPlanAgregar.close();                              
                            },
                            success: function(form, action) {
                                Ext.MessageBox.alert('Estado','Plan Creado Exitosamente');                                
                                windowPlanAgregar.close();
                                ds.load();
								Ext.getCmp('arbol-productos').getRootNode().reload();
                            }
                        });                   
                    }else{
                        Ext.MessageBox.alert('Error', 'Inserte Campos Requeridos');
                    }             
                }
            },{
                text: 'Cancelar',
                handler: function(){
                    windowPlanAgregar.close();
                }
            }]
        });
    windowPlanAgregar.show();
      
};

/*******TERMINAMOS LA VENTANA DE AGREGAR AL CATALOGO*************/


/*******CREAMOS LA VENTANA DE EDITAR PLAN *************/
editarPlan = function(ds){
    var clavePlan = new Ext.form.TextField({
        fieldLabel: 'Clave',
        allowBlank: false,
        name:'cdPlan',
        maxLength: '8',
        readOnly: true,
        value: hiddenCodigoPlan.getValue(),
        anchor:'90%'    
    });
    var nombrePlan = new Ext.form.TextField({
        fieldLabel: 'Nombre*',
        allowBlank: false,
        name:'dsPlan',
        maxLength: '100',
        value: codigoPlan.getValue(),
        maxLengthText: 'La Clave del plan debe ser de un M\u00C1ximo de 100 caracateres',
        blankText : 'Campo requerido',
        anchor:'90%'    
    });  
    var editaPlanForm= new Ext.FormPanel({
        id:'agregar-catalogo-planes',
        labelWidth: 75,
        baseCls: 'x-plain',
        width: 350,
        frame:true,
        height:140,
        url:'planes/EditaPlan.action',
        items:[clavePlan, nombrePlan]
    });
    var windowEditarPlan = new Ext.Window({
            title: 'Editar Plan',
            plain:true,
            modal:true,      
            width: 350,
            height:140,
            minWidth: 300,
            minHeight: 100,
            buttonAlign:'center',
            items: editaPlanForm,
            buttons: [{
                text: 'Guardar', 
                handler: function() {                
                    if (editaPlanForm.form.isValid()) {
                        editaPlanForm.form.submit({  
                            waitTitle:'Espere',
                            waitMsg:'Procesando...',
                            failure: function(form, action) {
                                Ext.MessageBox.alert('Error','Error editando un plan');
                                windowEditarPlan.close();                              
                            },
                            success: function(form, action) {
                                Ext.MessageBox.alert('Estado','Plan Editado Exitosamente');                                
                                windowEditarPlan.close();
								Ext.getCmp('arbol-productos').getRootNode().reload();
                            }
                        });                   
                    }else{
                        Ext.MessageBox.alert('Error', 'Inserte Campos Requeridos');
                    }             
                }
            },{
                text: 'Cancelar',
                handler: function(){
                    windowEditarPlan.close();
                }
            }]
        });
    windowEditarPlan.show();
      
};


 /*************************************
*Crea la ventana de editar
 **************************************/    
editarCatalogoPlanes = function(){

    var key = new Ext.form.TextField({
        fieldLabel: 'Clave*',
        allowBlank: false,
        name:'key',
        blankText : 'Campo requerido',
        readOnly : true,
        anchor:'90%'    
    });
    var value = new Ext.form.TextField({
        fieldLabel: 'Nombre*',
        allowBlank: false,
        name:'value',
        blankText : 'Campo requerido',
        anchor:'90%'    
    });  
    
    var editForm= new Ext.FormPanel({
        id:'editFormPlan',
        labelWidth: 75,
        baseCls: 'x-plain',
        width: 350,
        frame:true,
        height:140,
        url:'planes/ModificaCoberturaPlan.action',
        items:[key, value]
    });
    
    var windowEditarCatalogoPlan = new Ext.Window({
        title: 'Editar Cobertura',
        plain:true,
        modal:true,      
        width: 350,
        height:140,
        minWidth: 300,
        minHeight: 100,
        buttonAlign:'center',
        items: editForm,
        buttons: [{
                    text: 'Guardar', 
                    handler: function() {
                        if (editForm.form.isValid()) {
                                editForm.form.submit({
                                    waitTitle:'Espere',
                                    waitMsg:'Procesando...',
                                    failure: function(form, action) {
                                        Ext.MessageBox.alert('Estado','Error en la Edicion ');
                                        windowEditarCatalogoPlan.close();
                                    },
                                    success: function(form, action) {
                                        Ext.MessageBox.alert('Estado','Cobertura Editada Exitosamente');
                                        windowEditarCatalogoPlan.close();
                                        store.load();
                                        //TODO: Analizar si es necesario recargar el arbol de producto
                                    }
                                });                   
                        }else{
                            Ext.MessageBox.alert('Error', 'Inserte Campos Requeridos');
                        }             
                    }
                },{
                    text: 'Cancelar',
                    handler: function(){
                        windowEditarCatalogoPlan.close();
                    }
                }]
        });     
    windowEditarCatalogoPlan.show();
};


/******************
*Form para el grid. 
*******************/   
    var complemento = new Ext.form.TextField({
        fieldLabel: '',
        hiddeParent:true,
        labelSeparator:'',
        hidden:true
        
    });
    var separador2 = new Ext.form.TextField({
        fieldLabel: '',
        hiddeParent:true,
        labelSeparator:'',
        hidden:true
        
    });
    var separador3 = new Ext.form.TextField({
        fieldLabel: '',
        hiddeParent:true,
        labelSeparator:'',
        hidden:true
        
    });
    
    var separador4 = new Ext.form.TextField({
        fieldLabel: '',
        hiddeParent:true,
        labelSeparator:'',
        hidden:true
        
    });
    
    var separador5 = new Ext.form.TextField({
        fieldLabel: '',
        hiddeParent:true,
        labelSeparator:'',
        hidden:true
        
    });
    
    var productoLista = new Ext.form.TextField({
        fieldLabel: 'Riesgo',
        name:'cdRamoB',
        width:200
    });
    
    var hiddenCodigoPlan = new Ext.form.Hidden({
        id:'hidden-combo-plan',
        name: 'cdPlan'
    });
     
    var codigoPlan = new Ext.form.ComboBox({
                id:'codigo-form-plan-recarga-store-arbol',
                tpl: '<tpl for="."><div ext:qtip="{key}. {value}" class="x-combo-list-item">{value}</div></tpl>',
                store: ds,
                width:180,
                mode: 'local',
                name: 'codigoPlan',
                valueField: 'key',
                typeAhead: true,
                labelSeparator:'',
                triggerAction: 'all',           
                displayField:'value',
                listWidth: '185',
                forceSelection: true,
                fieldLabel: 'Plan',
                emptyText:'Seleccione un Plan...',
                selectOnFocus:true,
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
                    mStore.baseParams['codigoPlan'] = valor;
                    mStore.reload({
                    	callback : function(r,options,success) {
                        	if (!success) {
                            	Ext.MessageBox.alert('No se encontraron registros');
                                mStore.removeAll();
							}
						}
					});
					var valor=record.get('key');
                	Ext.getCmp('hidden-combo-plan').setValue(valor);
                    //Ext.getCmp('hidden-combo-plan-cobertura').setValue(valor);
				}
        });  
    
    var incisosForm = new Ext.form.FormPanel({
        title: 'Configuracion de planes del producto',
        bodyStyle:'background: white',
        labelAlign: 'left',
        labelWidth:30,  
        frame:true,  
        url:'',                        
        width: 570,
        items:[{
                layout:'form',
                border:true,
                buttonAlign:'left',
                width: '570',
                items: [{
                        layout:'column',
                        border:true,
                        width: '570',
                        baseCls: 'x-plain',
                        items: [{               
                                    columnWidth:.4,
                                    layout:'form',
                                    width:'245',
                                    labelAlign:'left',
                                    baseCls: 'x-plain',
                                    border:false,
                                    items:[codigoPlan,hiddenCodigoPlan]
                                    },{
                                    columnWidth:.2,
                                    layout:'form',             
                                    border:false,
                                    width: '170',
                                    baseCls: 'x-plain',
                                    items:[ 
                                            {xtype:'button',
                                            text: 'Agregar al Catalogo',
                                            name: 'AgregarCatalogo',
                                            buttonAlign:'left',
                                            handler: function() {
                                            agregarCatalogoPlanes(ds);
                                            }
                                            }]  
                                    },{
                                    columnWidth:.2,
                                    layout:'form',             
                                    border:false,
                                    width: '70',
                                    baseCls: 'x-plain',
                                    items:[
                                            {xtype:'button',
                                            text: 'Editar',
                                            name: 'EditarCatalogoPlanes',
                                            buttonAlign:'left',
                                            handler: function() {
                                            	if( !Ext.isEmpty(codigoPlan.getValue()) ){
                                            		editarPlan(ds);//agregarCatalogoPlanes(ds);
                                            	}else{
                                            		Ext.MessageBox.alert('Aviso', 'Seleccione el plan a editar');
                                            	}
                                            }
                                            }]  
                                    },{
                                    	columnWidth:.2,
                                    	layout:'form',             
                                    	border:false,
                                    	width: '85',
                                    	baseCls: 'x-plain',
                                    	items:[{
                                            	xtype:'button',
                                            	text: 'Eliminar',
                                            	tooltip:'Elimina plan seleccionado',
                                            	name: 'EliminarCatalogoPlanes',
                                            	buttonAlign:'left',
                                            	handler: function() {
                                            		if(hiddenCodigoPlan.getValue()!= null && hiddenCodigoPlan.getValue()!=''){
                                            			Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           													if(btn == 'yes'){
                                            					//Eliminacion de planes
                                            					incisosForm.form.submit({
		                                            				url:'planes/EliminaPlan.action',
                                            						waitTitle:'Espere',
                            										waitMsg:'Procesando...',
                            										failure: function(form, action) {
		                                								Ext.MessageBox.alert('Error','Error eliminando el plan');
                            										},
                            										success: function(form, action) {
		                                								Ext.MessageBox.alert('Estado','Plan Eliminado');
                                										ds.load();//carga planes
                                										store.load();//se limpia grid de coberturas
																		Ext.getCmp('arbol-productos').getRootNode().reload();
																		codigoPlan.reset();
																		hiddenCodigoPlan.setValue('');
                            										}
                                            					});                                            				
                                            				}
														});
                                            		}else{
                                            			Ext.MessageBox.alert('Aviso','Debe seleccionar un elemento de la lista para realizar esta operacion');
                                            		}
                                            	}
                                            }]
                                    }]
                            },grid2]
                    }]                      
        });
        incisosForm.render('centerPlanes');
    
    ds.load();
    dsComboCobertura.load();
});