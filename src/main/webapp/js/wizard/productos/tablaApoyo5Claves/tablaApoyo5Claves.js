TablasDeApoyo= function(dataStoreTabla5Claves, edita) {

var cmClaves;
var storeClaves;
var gridEditableClaves;
var grid2;


var cmAtributos;
var storeAtributos;
var gridEditableAtributos;

var formPanelEditable;
var nombreEditable;
var descripcionEditable;
var numEditable;
       
var windowConsulta;
 // shorthand alias
var fm = Ext.form;
var PlantGrid1;
var colm;
var grid;
var dataStore;
var store;

var contadorDatoClave=0;




function creaVentanaDeConsulta(numEditaTabla){
/*********************************************************        
Ventana de consulta y asociacion de valores
*********************************************************/
	
//Variables para la paginacion del grid
var startParam = 0;
var limitParam = 10; // Se ha fijado a 10 por pagina, por optimizacion, ya este grid carga demasiada informacion.

//Variables para Identificar cuando se esta cambiando de row cuando se esta en Modo de Edicion 
var isSelectingWhileEditing = false;
var isBlurToEditCell = false;

//Variables para identificar el row actualmente editandose
var editingColumn = 0;
var editingRow = 0;

//Variable para poder cargar por primer vez los stores de los grids (cada vez que se abre la ventana o se carga una nueva pagina)
var firstLoad = true;

var Plant;
//Variable que contiene todas las descripciones de los atributos para una tabla
var desAtributos;
var PlantAtributos;


// Para identificar si solo se han modificado las claves, solo los atributos, las claves y los atributos para un registro o es un nuevo registro

var _soloClavesModificados = 0;
var _soloAtributosModificados = 1;
var _clavesYatributosModificados = 2;
var _nuevoRegistro = 3;

var _actualizarRegistro = "2";
var _insertarRegistro = "1";


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

/******************************************
Grid editable de claves   
*******************************************/

    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store (created below)
    
    function formatDate(value){
    	if(value){
    		try{
    			return value.format('d/m/Y');
    		}catch(e){
    			return value;
    		}
    	}else{
    		return '';
    	}
    };
    
    cmClaves = new Ext.grid.ColumnModel([{
           id:'descripcionClave1',
           header: "Clave1 deshabilitada",
           dataIndex: 'descripcionClave1',
           width: 100,
           editor: new fm.TextField({
               	allowBlank: false,
                maxLength: '10',
                maxLengthText: 'La clave debe ser de maximo 10 caracteres',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
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
        },{
           header: "Clave2 deshabilitada",
           dataIndex: 'descripcionClave2',
           width: 100,
           editor: new fm.TextField({
               	allowBlank: false,
                maxLength: '10',
                maxLengthText: 'La clave debe ser de maximo 10 caracteres',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
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
        },{
           header: "Clave3 deshabilitada",
           dataIndex: 'descripcionClave3',
           width: 100,
           editor: new fm.TextField({
               allowBlank: false,
                maxLength: '10',
                maxLengthText: 'La clave debe ser de maximo 10 caracteres',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
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
        },{
           header: "Clave4 deshabilitada",
           dataIndex: 'descripcionClave4',
           width: 100,
           editor: new fm.TextField({
               allowBlank: false,
                maxLength: '10',
                maxLengthText: 'La clave debe ser de maximo 10 caracteres',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
		            			scope: c
		            			},
		            'focus' : {
		            
		            	fn: function(evt, t, o){/*isEditing = true; */isSelectingWhileEditing = false; isBlurToEditCell = false;},
		            	scope: c
		            }
		          });
		        }
		       }
           })
        },{
           header: "Clave5 deshabilitada",
           dataIndex: 'descripcionClave5',
           width: 100,
           editor: new fm.TextField({
               allowBlank: false,
                maxLength: '10',
                maxLengthText: 'La clave debe ser de maximo 10 caracteres',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
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
        },{
           header: "Fecha Desde",
           dataIndex: 'fechaDesde',
           width: 100,
           renderer: formatDate,
           editor: new fm.DateField({
               allowBlank: false,
               id: 'startdt',
		       vtype: 'daterange',
			   endDateField: 'enddt',
    		   format:'d/m/Y',
                maxLength: '10',
                maxLengthText: 'La fecha debe tener formato dd/MM/yyyy',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
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
        },{
           header: "Fecha Hasta",
           dataIndex: 'fechaHasta',
           width: 100,
           renderer: formatDate,
           editor: new fm.DateField({
               allowBlank: false,               
			   id: 'enddt',
    	       vtype: 'daterange',
			   startDateField: 'startdt',
			   format:'d/m/Y',
                maxLength: '10',
                maxLengthText: 'La fecha debe tener formato dd/MM/yyyy',
                listeners: {
		        render: function(c) {
		          c.getEl().on({
		            'keydown' : {
		            			fn: function(evt, t, o){eventoNavegacionGrid(evt, t, o)},
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
        },{
           header: "Identificador",
           dataIndex: 'identificador',
           width: 100,
           hidden:true
        },{
           header: "Identificador Temporal",
           dataIndex: 'identificadorTemporal',
           width: 100,
           hidden:true
        }
    ]);
	
    function eventoNavegacionGrid(e, t, o){
			
    	   if(e.getKey() == e.ENTER || e.getKey() == e.ESC){
 		   	e.stopEvent();
 		   	isBlurToEditCell = false;
 		   }
 		   if(e.getKey() == e.DOWN){
 		   	e.stopEvent();
 		   	isBlurToEditCell = true;
 		   			if(gridEditableClaves.getStore().getCount()-1 == editingRow){
 		   				agregarFilaGridClaves();
 		   			}
		   			else {
 		   				gridEditableClaves.startEditing(editingRow + 1, editingColumn);
 		   			}
 		   }
 		   else if(e.getKey() == e.UP){
 		   	e.stopEvent();
 		   			if(editingRow > 0) {
 		   				isBlurToEditCell = true;
 		   				gridEditableClaves.startEditing(editingRow - 1, editingColumn);
 		   			}
 		   }
 		   if(e.getKey() == e.TAB){
 		   	e.stopEvent();
 		   	isBlurToEditCell = true;
 		   }			
    }
    
    
    function agregarFilaGridClaves(){
    	
    	var newId = parseInt(storeClaves.getAt(storeClaves.getCount()-1).get('identificador')) + 1;
    	var p = new Plant({
        	        identificador: newId,
        	        identificadorTemporal: _nuevoRegistro,
            	    descripcionClave1: '',
                	descripcionClave2: '',
	                descripcionClave3: '',
    	            descripcionClave4: '',
        	        descripcionClave5: '',
        	        fechaDesde: (new Date()).format('d/m/Y'),
        	        fechaHasta: (new Date()).format('d/m/Y')
            	   	});   
            	   	
		gridEditableClaves.stopEditing();            		    		
		storeClaves.add(p);
		
		if(desAtributos){
			
			for(var i = 0 ; i < desAtributos.length ; i++){
				var atributo =  new PlantAtributos({
				id: newId,
				key: i+1,
				value: desAtributos[i],
				nick: ''
				}); 

				storeAtributos.add(atributo);
			}
			
			storeAtributos.filter('id', new RegExp('^' + newId + '$'));
			
		}else Ext.Msg.alert('Error', 'Existió un problema al obtener los atributos, consulte a su soporte.' );
		
		gridEditableClaves.startEditing(storeClaves.getCount()-1,editingColumn);
    }
    
    function agregarPrimerFilaGridClaves(){
    	
    	var newId = 0;
    	var p = new Plant({
        	        identificador: newId,
        	        identificadorTemporal: _nuevoRegistro,
            	    descripcionClave1: '',
                	descripcionClave2: '',
	                descripcionClave3: '',
    	            descripcionClave4: '',
        	        descripcionClave5: '',
        	        fechaDesde: (new Date()).format('d/m/Y'),
        	        fechaHasta: (new Date()).format('d/m/Y')
            	   	});      
            	   	
		gridEditableClaves.stopEditing();            		    		
		storeClaves.add(p);
		
		if(desAtributos){
			
			for(var i = 0 ; i < desAtributos.length ; i++){
				var atributo =  new PlantAtributos({
				id: newId,
				key: i+1,
				value: desAtributos[i],
				nick: ''
				}); 

				storeAtributos.add(atributo);
			}
			
			storeAtributos.filter('id', new RegExp('^' + newId + '$'));
			
		}else Ext.Msg.alert('Error', 'Existió un problema al obtener los atributos, consulte a su soporte.' );
		
		gridEditableClaves.startEditing(0,0);
    }
    
    
    // by default columns are sortable
    cmClaves.defaultSortable = false;

    // this could be inline, but we want to define the Plant record
    // type so we can add records dynamically
    Plant = Ext.data.Record.create([

           {name: 'identificador', type: 'string'}, //para hacer el match con los records de los Atributos
           {name: 'identificadorTemporal', type: 'string'},// para saber que tipo de modificacion se ha realizado y usarla al guardar (ej. solo atributos, ids y atributos, nuevo registro..)
           {name: 'descripcionClave1', type: 'string'},
           {name: 'descripcionClave2', type: 'string'},          
           {name: 'descripcionClave3', type: 'string'},   
           {name: 'descripcionClave4', type: 'string'},
           {name: 'descripcionClave5', type: 'string'},
           {name: 'fechaDesde', type:'date', dateFormat: 'd/m/Y'},
           {name: 'fechaHasta', type:'date', dateFormat: 'd/m/Y'}         
      ]);

    // create the Data Store
    //var numeroEditable= Ext.getCmp('num-editable').getValue();
    storeClaves = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy( new Ext.data.Connection({
	        	   			url:'tablaCincoClaves/ListaClavesEditable.action',
	        	   			timeout: 60000
	        	   })),
        	reader: new Ext.data.JsonReader({
            root: 'listaClavesEditable',
            id: 'comboPadre',
            totalProperty: 'totalCount'
	        }, Plant),
	        baseParams:{numEditable: numEditaTabla},
	        listeners: {
	        beforeload: function (str, options){
	        				if(!firstLoad){
	        					if(storeClaves.getModifiedRecords().length <= 0 && storeAtributos.getModifiedRecords().length <= 0)return true;
	        					Ext.Msg.show({
									 title:'Advertencia'
									,msg:'Si cambia de p&aacute;gina se perder&aacute;n los cambios realizados, </br> ¿Desea Continuar?'
									,icon:Ext.Msg.QUESTION
									,buttons:Ext.Msg.YESNO
									,fn: function(btn, txt){
											if(btn == 'yes'){
												firstLoad = true;
												storeAtributos.clearFilter();
												
												storeClaves.rejectChanges();
												storeAtributos.rejectChanges();
												
												storeClaves.load({
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
					var myMaskAtrs = new Ext.LoadMask(gridEditableClaves.getEl(),{msg: 'Espere...'});
					myMaskAtrs.show();
					storeAtributos.load({
						callback: function(){
							if(!desAtributos){
								storeAtributos.filter('id',new RegExp('^dummy$')); //para obtener unicamente una lista de Atributos del row dummy creado en java
								obtenerDescAtributos(storeAtributos.getCount());
							}else {
								storeAtributos.filter('id',-1);
							}
							myMaskAtrs.hide();
						}
					});
	        	}
	        }
    });

    // create the editor grid
    gridEditableClaves = new Ext.grid.EditorGridPanel({
    	id:'grid-editable-claves',
        store: storeClaves,
        cm: cmClaves,
        loadMask: {msg: 'Cargando datos ...'},
        width:500,
        height:250,
        autoExpandColumn:'descripcionClave1',
        title:'Claves',
        collapsible:true,
        frame:true,
        clicksToEdit: 2,
		viewConfig: {
	        forceFit: true
	    },
	    listeners : {
	    	
	    	beforeedit: function(e){
	    		editingColumn = e.column;
	    		editingRow = e.row;
	    	},
	    	dblclick: function(e){
	    		if(storeClaves.getCount() <= 0){
	    			agregarPrimerFilaGridClaves();
	    		}
	    	},
	    	cellclick:  function(){
	    		isBlurToEditCell = false;//isEditing = false;    	
	    	}
	    
	    },
	    sm: new Ext.grid.CellSelectionModel({
			listeners: {
					beforecellselect: function(){
						if(isBlurToEditCell){
							if(isSelectingWhileEditing)return false;
								else isSelectingWhileEditing = true;
						}
					},
					
		        	cellselect: function(sm, row, col) {
		        			var rec = storeClaves.getAt(row);
		        			var nuevoFiltro = rec.get('identificador');

		        			//Para optimizar el codigo y no hacer el filtro cada que cabia de celda en la misma fila
		        			if(storeAtributos.getCount() > 0 ){
		        				if(storeAtributos.getAt(0).get('id') != nuevoFiltro ) storeAtributos.filter('id',new RegExp('^' + nuevoFiltro + '$'));
		        			}else storeAtributos.filter('id',new RegExp('^' + nuevoFiltro + '$'));
				   	 }
   		   	}
		}),
        tbar: [{
	            text: 'Agregar Clave',
	            tooltip:'Agregar una fila de claves',
	            iconCls:'add',
    	        handler : function(){
    	        	if(storeClaves.getCount() <= 0){
	    				agregarPrimerFilaGridClaves();
		    		}else {
		    			agregarFilaGridClaves();
		    		}
    	        }
        	},'-',{
   			    	id:'eliminar-claves',
	    	        text:'Eliminar',
			    	tooltip:'Eliminar la fila seleccionada',
   					iconCls:'remove',
   					handler: function (){
   						var registroEliminar = gridEditableClaves.getSelectionModel().getSelectedCell();
   						if(registroEliminar){
   							var recEliminar = storeClaves.getAt(registroEliminar[0]);
   							
	        	 					Ext.MessageBox.confirm("Mensaje","Esta seguro que desea eliminar este elemento?",function(btn){									   			
	        	 					 		if(btn == 'yes'){ 
	        	 					 			var indexAtrEliminar = storeAtributos.find('id', new RegExp('^' + recEliminar.get('identificador') + '$'));
	        	 					 			while(indexAtrEliminar >= 0 ){
	        	 					 				var atrEliminar = storeAtributos.getAt(indexAtrEliminar);
	        	 					 				atrEliminar.reject();
	        	 					 				storeAtributos.remove(atrEliminar);
	        	 					 				indexAtrEliminar = storeAtributos.find('id', new RegExp('^' + recEliminar.get('identificador') + '$'));
									   			}
									   			
									   			recEliminar.reject();
									   			
									   			if(recEliminar.get("identificadorTemporal") != _nuevoRegistro){
									   				
									   				var params="numeroTabla="+Ext.getCmp('nombre-editable').getValue()+"&&descripcionClave1="+recEliminar.get('descripcionClave1')+"&&descripcionClave2="+recEliminar.get('descripcionClave2')+
									   							"&&descripcionClave3="+recEliminar.get('descripcionClave3')+"&&descripcionClave4="+recEliminar.get('descripcionClave4')+"&&descripcionClave5="+recEliminar.get('descripcionClave5');
									   							
													var conn = new Ext.data.Connection();
													var myMaskDelete = new Ext.LoadMask(Ext.getBody(),{msg: 'Borrando Registro...'});
													myMaskDelete.show();
									            	conn.request ({
									            		url:'tablaCincoClaves/BorrarValoresTablaCincoClaves.action',
														method: 'POST',
														successProperty : '@success',
														params : params,
									    	       		callback: function (options, success, response) {
									    	       				myMaskDelete.hide();
					                       						if (Ext.util.JSON.decode(response.responseText).success) {
						                       						storeClaves.remove(recEliminar);
						                       						Ext.Msg.alert('Aviso', Ext.util.JSON.decode(response.responseText).mensajeResultado);
					                       						} else {
							                          					Ext.Msg.alert('Error', Ext.util.JSON.decode(response.responseText).mensajeResultado);
					    	                      				}
					    	                      				
					       		               			}
							    		       		});
									   			}else{
									   				storeClaves.remove(recEliminar);
									   			}
									   			
									   			
									   		}
									 });
									 
   						}else Ext.Msg.alert('Aviso','Debe seleccionar un registro para realizar esta operaci&oacute;n');
   					
   					}
   					
        }],
		    bbar: new Ext.PagingToolbar({
		        pageSize: limitParam,
		        store: storeClaves,                  
		        displayInfo: true,
				displayMsg: 'Mostrando registros {0} - {1} de {2}',
				emptyMsg: 'No hay registros para visualizar',
				beforePageText: 'P&aacute;gina',
				afterPageText: 'de {0}'
		    })
    });				
			
	
    // trigger the data store load
    //storeClaves.load();
/*********************************************
Grid Editable de Atributos
*********************************************/    

    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store (created below)
    cmAtributos = new Ext.grid.ColumnModel([{
           id:'value',
           header: "Clave de Atributo",
           dataIndex: 'value',
           width: 100          
        },{
           header: "Valor de Atributo",
           dataIndex: 'nick',
           width: 100,
           editor: new fm.TextField({
               allowBlank: false
           })
        }
    ]);
	
    // by default columns are sortable
    cmAtributos.defaultSortable = false;
    
    // create the Data Store
    
    PlantAtributos = Ext.data.Record.create([
    				   {name: 'id', type: 'string',mapping:'id'},
			           {name: 'key', type: 'string',mapping:'key'},
			           {name: 'value', type: 'string',mapping:'value'},
			           {name: 'nick', type: 'string',mapping:'nick'}        
			        ]);
       
	storeAtributos = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({        	
        	url:'tablaCincoClaves/ListaAtributosEditable.action'
//			url: '/tablaCincoClaves/ListaAtributosEditable.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaAtributosEditable',
            id: 'comboPadreAtrs'
	        },PlantAtributos),
        baseParams:{numEditable: numEditaTabla},
		remoteSort: false
    });
    
    //funcion para obtener las descripciones de los atributos para cualquiera de los rows.
    function obtenerDescAtributos(storeAtributosSize){
    	
    	if( storeAtributosSize <= 0)return;
    	
    	desAtributos = new Array(storeAtributosSize);

    	for(var i = 0 ; i < storeAtributos.getCount(); i++){
    		desAtributos[i] = storeAtributos.getAt(i).get('value');
    	}
    	
    	storeAtributos.filter('id',-1);
    }
//	storeAtributos.load(); 
//storeClaves.load();
    // create the editor grid
    gridEditableAtributos = new Ext.grid.EditorGridPanel({
    	id:'grid-editable-atributos',
        store: storeAtributos,
        cm: cmAtributos,
        loadMask: {msg: 'Cargando datos ...'},
        //autoScroll:true,
        width:500,
        height:170,
        autoExpandColumn:'value',
        title:'Atributos',
        collapsible:true,
        frame:true,
        //plugins:checkColumn,
        clicksToEdit:1,
		viewConfig: {
	        forceFit: true
	    }
	    //,sm: new Ext.grid.RowSelectionModel({
		//	singleSelect: true			
		//})
    });

    
/**********************************************
Forma del panel Editable
***********************************************/
      nombreEditable= new Ext.form.TextField({
      				id:'nombre-editable',
                    fieldLabel: 'Nombre',
                    labelSeparator:'',
                    width:'85'  ,
                    disabled:true,
                    maxLength: '30',
                    maxLengthText: 'El texto debe ser de 30 caracteres',
                    allowBlank:false,
                    blankText : 'Nombre requerido.',
                    name:'nombreEditable' 
   		});		

      descripcionEditable= new Ext.form.TextField({
      				id:'descripcion-editable',	
                    fieldLabel: 'Descripci\u00F3n',
                    labelSeparator:'',                    
                    width:'195'  ,
                    maxLength: '60',
                    maxLengthText: 'El texto debe ser de 60 caracteres', 
                    disabled:true,
                    allowBlank:false,
                    blankText : 'Descripcion requerida.',
                    name:'descripcionEditable' 
   		});		

      numEditable= new Ext.form.NumberField({
      				id:'num-editable',
                    fieldLabel: 'N\u00FAm.',
                    labelSeparator:'',                    
                    width:'50' ,
                    disabled:true  ,
                    name:'numEditable'
   		});		

    formPanelEditable = new Ext.FormPanel({
		//autoScroll:true,
		id:'panel-editable-grids',
        frame:true,
        region:'center',
        bodyStyle:'padding:5px 5px 0',
        url:'tablaCincoClaves/InsertarValoresTablaEditable.action',
//        url:'/tablaCincoClaves/InsertarValoresTablaEditable.action',
        width:500,
        border:false,
        items:[{
	        	layout:'form',
    	    	border:false,
        		title:'Encabezado',
        		collapsible:true,
	        	width: '500',
    	    	items:[{
			        layout:'column',
		    	    border:true,
	    	    	width: '430',
	    	    	items: [{
			        		columnWidth:.25,
    		  		        labelAlign: 'top',
    			    		layout:'form',
        					border:false,
        					items:[nombreEditable]
			        	},{
    			    		columnWidth:.50,
    				        labelAlign: 'top',
        					layout:'form',
        					border:false,
        					items:[descripcionEditable]
		   		    	},{
	    		    		columnWidth:.2,
    				        labelAlign: 'top',
        					layout:'form',
        					border:false,
        					items:[numEditable]
		        		}]
			    }]		    
		    },
        	gridEditableClaves,gridEditableAtributos]     
    });
    
 windowConsulta = new Ext.Window({
            title: 'Consulta Y Asociaci\u00F3n De Valores',
            closable:true,
            buttonAlign:'center',
            width:560,
            height:600,
            plain:true,
            layout: 'border',
            modal:true,
            listeners: {
            	show: function(){storeClaves.load({
										params: {
											start: startParam, 
											limit: limitParam
										}
								});
								},
            	close: function (){firstLoad = true;}
            },
			items:[formPanelEditable],
  			buttons:[
  				{
  					text:'Guardar',
  					handler:function(){
  						//saveModifidedRecords(true);
  						guardarYFinalizar();
  					}  					
				},{
					text:'Cancelar',
					handler:function(){  
							windowConsulta.close();
							//storeClaves.rejectChanges();		
					}
  				}]
        });
        function loadRecordForm(recordClavesForm){
        	formPanelEditable.form.loadRecord(recordClavesForm);
        }
        
        
	function guardarYFinalizar(){
		var valida;
        var recs = storeClaves.getModifiedRecords();
        var recsAtributos = storeAtributos.getModifiedRecords();
  		
		if(recs.length==0 && recsAtributos.length==0){
			Ext.Msg.alert('Aviso', 'No existen cambios para guardar');
		}else{
        	var storeAtrModif = new Ext.data.Store({
	        	reader: new Ext.data.JsonReader({
	            root: 'listaAtributosEditable'
		        }, PlantAtributos)
		    });
		    storeAtrModif.add(recsAtributos);
  			var idsAtrsModif = storeAtrModif.collect('id');
  			for(var i = 0 ; i < idsAtrsModif.length ; i++ ){
  				var id = idsAtrsModif[i];
  				var indexRecClave = storeClaves.find('identificador', new RegExp('^' + id + '$'));
  				
  				if(indexRecClave >= 0){
	  				var recordClave = storeClaves.getAt(indexRecClave);
	  				
	  				if(Ext.isEmpty(recordClave.get('identificadorTemporal'))){
	  					if(recordClave.dirty) recordClave.set('identificadorTemporal', _clavesYatributosModificados);
	  						else recordClave.set('identificadorTemporal', _soloAtributosModificados);
	  				
	  				recordClave = storeClaves.getAt(indexRecClave);
	  				}
  				}	
  			}
  			
			recs = storeClaves.getModifiedRecords();
			if(recs.length==0){
				Ext.Msg.alert('Aviso', 'No existen cambios para guardar');
				return;
			}
			
			if(validarClavesObligatorias(recs)){
			var params="numeroTabla="+numEditaTabla+"&&";  //numero de la tabla a modificar
			var identificadorClaves; //para saber que fue lo que se modifico de cada registro
			var tipoTransito; //Para indicar si se debe de realizar un update o un insert
		
				
				var myMask = new Ext.LoadMask(Ext.getBody(),{msg: 'Guardando datos...'});
				myMask.show();
				for (var i=0; i<recs.length; i++) {
					
					tipoTransito = _actualizarRegistro;
					
					identificadorClaves = recs[i].get('identificadorTemporal');
					if(Ext.isEmpty(identificadorClaves))identificadorClaves =  _soloClavesModificados;
					
					params  += 	  "listaValores5ClavesParams[" + i + "].descripcionCincoClaves.descripcionClave1=" + recs[i].get('descripcionClave1')+ 
								"&&listaValores5ClavesParams[" + i + "].descripcionCincoClaves.descripcionClave2=" + recs[i].get('descripcionClave2')+
								"&&listaValores5ClavesParams[" + i + "].descripcionCincoClaves.descripcionClave3=" + recs[i].get('descripcionClave3')+ 
								"&&listaValores5ClavesParams[" + i + "].descripcionCincoClaves.descripcionClave4=" + recs[i].get('descripcionClave4')+
								"&&listaValores5ClavesParams[" + i + "].descripcionCincoClaves.descripcionClave5=" + recs[i].get('descripcionClave5')+ 
								"&&listaValores5ClavesParams[" + i + "].fechaDesde=" + formatDate(recs[i].get('fechaDesde'))+											 
								"&&listaValores5ClavesParams[" + i + "].fechaHasta=" + formatDate(recs[i].get('fechaHasta'))+"&&";
							
					if( identificadorClaves == _nuevoRegistro || identificadorClaves == _soloAtributosModificados ){
						if(identificadorClaves == _nuevoRegistro) tipoTransito = _insertarRegistro; ;
							params  += 	  "listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave1=" + recs[i].get('descripcionClave1')+ 
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave2=" + recs[i].get('descripcionClave2')+
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave3=" + recs[i].get('descripcionClave3')+ 
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave4=" + recs[i].get('descripcionClave4')+
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave5=" + recs[i].get('descripcionClave5')+ 
										"&&listaValores5ClavesParams[" + i + "].fechaDesdeAnterior=" + formatDate(recs[i].get('fechaDesde'))+											 
										"&&listaValores5ClavesParams[" + i + "].fechaHastaAnterior=" + formatDate(recs[i].get('fechaHasta'))+"&&";
					}else {

						recs[i].reject(true);
						
							params  += 	  "listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave1=" + recs[i].get('descripcionClave1')+ 
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave2=" + recs[i].get('descripcionClave2')+
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave3=" + recs[i].get('descripcionClave3')+ 
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave4=" + recs[i].get('descripcionClave4')+
										"&&listaValores5ClavesParams[" + i + "].descripcionCincoClavesAnterior.descripcionClave5=" + recs[i].get('descripcionClave5')+ 
										"&&listaValores5ClavesParams[" + i + "].fechaDesdeAnterior=" + formatDate(recs[i].get('fechaDesde'))+											 
										"&&listaValores5ClavesParams[" + i + "].fechaHastaAnterior=" + formatDate(recs[i].get('fechaHasta'))+"&&";
					}
								
								
					storeAtributos.filter('id', new RegExp('^' + recs[i].get('identificador') + '$'));
					//Verificar si el tamaño del store corresponde al tamaño de los atributos
					for(var x = 0 ; x < storeAtributos.getCount(); x++ ){
						params  += 	  "listaValores5ClavesParams[" + i + "].descripcionVeinticincoAtributos.descripcion" + storeAtributos.getAt(x).get('key') + "=" + storeAtributos.getAt(x).get('nick')+ "&&";
					}
					
					params += "listaValores5ClavesParams[" + i + "].tipoTransito=" + tipoTransito + "&&";
				
				}

					var connValores = new Ext.data.Connection();
									connValores.request ({
										url:'tablaCincoClaves/InsertarValoresTablaCincoClaves.action',
										method: 'POST',
										successProperty : '@success',
										params : params,
										callback: function (options, success, response) {
												firstLoad = true;
												storeClaves.commitChanges();
  												storeAtributos.commitChanges();
  												myMask.hide();
  												
												storeClaves.load({
													params: {
														start: startParam, 
														limit: limitParam
													}
											    });

		                       				if (success) {
	                       						Ext.MessageBox.alert('Aviso', Ext.util.JSON.decode(response.responseText).mensajeResultado);		            	              					
											} else {
			                       				Ext.MessageBox.alert('Error', Ext.util.JSON.decode(response.responseText).mensajeResultado);        	                							
											}
										},
										waitTitle:'Espere',
										waitMsg:'Procesando...'
									});//end guardar en la base
				
			}//validacion de campos requeridos
		}//end else
	}

        
  encabezados();      
  windowConsulta.show();      
}  
/***************************************************************************
Ventana de apoyo de 5 claves
****************************************************************************/
 var afuera;
 var temporal=-1;
 var contador=0;
 var selIndexGridclave;
/*************************************
Grid 1
**************************************/
//var fm = Ext.form;

var myData = [
        ["N","Numerico"],
        ["A","Alfanumerico"],
        ["P","Porcentaje"],
        ["F","Fecha"]
        ];
var comboStore = new Ext.data.SimpleStore({
        fields: [
           {name: 'key'},
           {name: 'value'}           
        ]
    });   	
    comboStore.loadData(myData);
     colm = new Ext.grid.ColumnModel([{
           id:'descripcionClave',
           header: "Claves",
           dataIndex: 'descripcionClave',
           width: 100,           
           editor: new Ext.form.TextField({
           		allowBlank: false,
           		blankText : 'Descripcion requerida',
           		maxLength: '30',
                    maxLengthText: 'El texto debe ser de 30 caracteres'
           })
        },{
           header: "Formato",
           dataIndex: 'descripcionFormatoClave',
           width: 100,
           editor: new Ext.form.ComboBox({          										
				typeAhead: true,
				triggerAction: 'all',
				tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
				displayField:'value',
				//valueField: 'key',
				typeAhead: true,
				editable: false, 
				mode: 'local',					    	
				store: comboStore,
				forceSelection:true,
				emptyText:'Seleccione un formato',
				selectOnFocus:true,
				allowBlank:false,
				blankText : 'Formato requerido',    	       
				hideLabel:true,
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
								if(this.getValue()== 'Porcentaje' || this.getValue()== 'Fecha'){								
									validaformatoClave();
									Ext.getCmp('minimo-clave-tabla-5-claves').setDisabled(true);
									Ext.getCmp('maximo-clave-tabla-5-claves').setDisabled(true);
								}else{
									Ext.getCmp('minimo-clave-tabla-5-claves').setDisabled(false);
									Ext.getCmp('maximo-clave-tabla-5-claves').setDisabled(false);
								}
         						//var valor=record.get('key');
								//Ext.getCmp('hidden-combo-suma-asegurada').setValue(valor);
							}			    	
            })		    
        },{
           header: "M\u00EDnimo",
           dataIndex: 'minimoClave',
           width: 90,
           editor: new Ext.form.NumberField({   
           		allowBlank: false,
        		blankText : 'N\u00FAmero m\u00EDnimo requerido',
        		id:'minimo-clave-tabla-5-claves'
            })
        },{
           header: "M\u00E1ximo",
           dataIndex: 'maximoClave',
           width: 90,           
           editor: new Ext.form.NumberField({   
           		allowBlank: false,
        		blankText : 'N\u00FAmero m\u00E1ximo requerido',
        		id:'maximo-clave-tabla-5-claves'
            })
           
        }        
    ]);
    
    
    colm.defaultSortable = false;
    
    PlantGrid1 = Ext.data.Record.create([

           		//{name: 'numeroTabla',  		type: 'string',  mapping:'numeroTabla'},
	        	//{name: 'numeroClave',  		type: 'string',  mapping:'numeroClave'},
        		{name: 'descripcionClave',  type: 'string',  mapping:'descripcion'},
        		{name: 'descripcionFormatoClave',  	type: 'string',  mapping:'descripcionFormato'},        		
        		{name: 'maximoClave',  		type: 'string',  mapping:'maximo'},
        		{name: 'minimoClave',  		type: 'string',  mapping:'minimo'}                      
      ]);

    
    dataStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
            url:'tablaCincoClaves/ListaCincoClaves.action'   
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaClaves',
            id: 'descripcion'
            }, PlantGrid1)
     
    });
    grid = new Ext.grid.EditorGridPanel({
			id:'grid-clave',
        	store: dataStore,
        	cm: colm,
        	//autoScroll:true,
        	width:450,
        	height:190,
        	autoExpandColumn:'descripcionClave',
        	collapsible:true,
        	frame:true,
        	title:'Claves',
        	clicksToEdit:1,
        	viewConfig: {
				forceFit: true
        	},
        	sm: new Ext.grid.RowSelectionModel({
			singleSelect: true,
			listeners: {							
        		rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			//selectedId = dataStore.data.items[row].id;
	        	 			var selGrid = Ext.getCmp('grid-clave').getSelectionModel().getSelected();
	        	 			var selIndexGrid = dataStore.indexOf(selGrid);
							//afuera=row;
							
	        	 			Ext.getCmp('eliminar-clave').on('click',function(){ 
	        	 					 var numData=dataStore.getTotalCount();	
	        	 					 var numelimina = numData + 1;        	 					 			        	 					 		
	        	 					 if(selIndexGrid >= numData){
	        	 					 	Ext.MessageBox.confirm("Mensaje","Esta seguro que desea eliminar este elemento?",function(btn){
	        	 					 		if(btn == 'yes'){ 
									   			dataStore.remove(rec);
									   			contador--;
									   		}
									 	});
	        	 					 }else{
	        	 					 	Ext.MessageBox.alert('Error', 'No se pueden eliminar las claves asociadas');
	        	 					 }                                      
                                 });                                                           	        	 					                                                                                  
                 }
     	     }                                                         							
		}),  
		tbar:[{
            text:'Agregar',
            tooltip:'Agrega una clave',            
            iconCls:'add',            
            handler:function(){         	            		            	
            			var numFila= Ext.getCmp('grid-clave').getStore().getTotalCount();            			
            		    var num=numFila+contador;            		    
            		    contador++;	        	 		
            		   
            		    if(contador == 1){
            		    	selIndexGridclave=Ext.util.JSON.encode(numFila);
            		    	var p = new PlantGrid1({
            	        		descripcionClave: '',
                	    		descripcionFormatoClave: '',
	                    		maximoClave: '',
    	                		minimoClave: ''
            	    		});            	        	
                				grid.stopEditing();
	                			dataStore.insert(numFila, p);
    	            			grid.startEditing(numFila,0);
            		    }else if(num<5){
            		    		selIndexGridclave=Ext.util.JSON.encode(num);
            		    		var p = new PlantGrid1({
            	        		descripcionClave: '',
                	    		descripcionFormatoClave: '',
	                    		maximoClave: '',
    	                		minimoClave: ''
            	    		});            	        	
                				grid.stopEditing();
	                			dataStore.insert(num, p);
    	            			grid.startEditing(num,0);
            		    }else {
            		    	Ext.MessageBox.alert('Error', 'Esta tabla solo puede crecer hasta 5 campos');
            		    }							            			           			
			}
            
        },'-',{
            text:'Eliminar',
            id:'eliminar-clave',
            tooltip:'Elimina la clave seleccionada',
            iconCls:'remove'
            
        }/*,'-',{
            text:'<s:text name="productos.tabla5claves.btn.editar"/>',
            id:"editar-clave",
            tooltip:'Edita la clave seleccionada',
            iconCls:'option'
        }*/] 
    	});

		grid.addListener({
			
			'beforeedit':{
				fn: function(event){
					 var selGridclaveBefore = Ext.getCmp('grid-clave').getSelectionModel().getSelected();
					 var selIndexGridclaveBefore = dataStore.indexOf(selGridclaveBefore);
					 var numeroDeDatos= Ext.getCmp('grid-clave').getStore().getTotalCount();   
					 if(selIndexGridclaveBefore == -1){
					 	selIndexGridclave=selIndexGridclave;
					 }else{					 
					 	if(selIndexGridclaveBefore < numeroDeDatos){					 	 					 	 
					 	 
					 		 selIndexGridclave=Ext.util.JSON.encode(selIndexGridclaveBefore);
					 	}else{
					 	 	selIndexGridclave=Ext.util.JSON.encode(numeroDeDatos);				 	
					 	}
					 }	
				}
				,scope:this
			},						
			'validateedit':{
				fn: function(event){					
					//var selGridclave = Ext.getCmp('grid-clave').getSelectionModel().getSelected();	       			       			
	       			//var selIndexGridclave = dataStore.indexOf(selGridclave);
    				
    				var numeroDeDatos= Ext.getCmp('grid-clave').getStore().getTotalCount();    		    		
    				//alert("numeroDeDatosVal"+numeroDeDatos);
    				//alert("selIndexGridclaveVal"+selIndexGridclave);
    				if(selIndexGridclave < numeroDeDatos){
		        		//Ext.MessageBox.alert('Error', 'No se pueden editar las claves asociadas');		        			        		
		        		//Ext.getCmp('grid-clave').stopEditing();
		        		return false		        				        	
	    			}else{
	    				if(validaMaximoYMinimoClave()){
		        			return true	    				
	    				}else{
							return false							
	    				}
	    			}	    			
				}
				,scope:this
			}		
		});//end grid.addListener				
 
    dataStore.load();

    function validaMaximoYMinimoClave(){
    	//alert('min'+Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
       	//alert('max'+Ext.getCmp('maximo-clave-tabla-5-claves').getValue());
    	if(Ext.getCmp('minimo-clave-tabla-5-claves').getValue()!= '' && Ext.getCmp('maximo-clave-tabla-5-claves').getValue()!= ''){
	    	//alert('entro');
    		if(Ext.getCmp('minimo-clave-tabla-5-claves').getValue()<= Ext.getCmp('maximo-clave-tabla-5-claves').getValue()){
    			//Ext.getCmp('minimo-clave-tabla-5-claves').setValue(Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
    			//Ext.getCmp('maximo-clave-tabla-5-claves').setValue(Ext.getCmp('maximo-clave-tabla-5-claves').getValue());
    			return true
    		}
    		if(Ext.getCmp('minimo-clave-tabla-5-claves').getValue()> Ext.getCmp('maximo-clave-tabla-5-claves').getValue()){
    			Ext.MessageBox.alert('Error', 'Valor maximo es menor que minimo');
    			 var fil= Ext.getCmp('grid-clave').getStore().getTotalCount();
							 var recordio = dataStore.getAt(fil);
							 recordio.set('maximoClave','');
							 recordio.set('minimoClave',Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
	    					 //alert(rec.get('maximoClave'));
    			return false
    		}    		
    	}
    	return true
    }
    
    function validaformatoClave(){
    	//alert('min'+Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
       	//alert('max'+Ext.getCmp('maximo-clave-tabla-5-claves').getValue());
    			 var filClave= Ext.getCmp('grid-clave').getStore().getTotalCount();
							 var recordio = dataStore.getAt(filClave);
							 recordio.set('maximoClave','');
							 recordio.set('minimoClave','');    			     	
    }
/*************************************
Grid 2
**************************************/
var contadorAtributo=0;
var selIndexGridatributo;
var myData2 = [
        ["N","Numerico"],
        ["A","Alfanumerico"],
        ["P","Porcentaje"],
        ["F","Fecha"]
        ];
var comboStore2 = new Ext.data.SimpleStore({
        fields: [
           {name: 'key'},
           {name: 'value'}           
        ]
    });   	
    comboStore2.loadData(myData2);
    
     var cm = new Ext.grid.ColumnModel([{
           id:'descripcionAtributo',
           header: "Atributos",
           dataIndex: 'descripcionAtributo',
           width: 100,
           editor: new Ext.form.TextField({
           	maxLength: '30',
           	maxLengthText: 'El texto debe ser de 60 caracteres'
           })
        },{
           header: "Formato",
           dataIndex: 'descripcionFormatoAtributo',
           width: 100,
           editor: new Ext.form.ComboBox({          										
				typeAhead: true,
				triggerAction: 'all',
				tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
				displayField:'value',
				//valueField: 'key',
				typeAhead: true,
				mode: 'local',		
				editable: false,
				store: comboStore,
				forceSelection:true,
				emptyText:'Seleccione un formato',
				selectOnFocus:true,
				allowBlank:false,
				blankText : 'Formato requerido',    	       
				hideLabel:true,
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
								if(this.getValue()== 'Porcentaje' || this.getValue()== 'Fecha'){									
									
									validaformatoAtributo();
									Ext.getCmp('minimo-atributo-tabla-5-claves').setDisabled(true);
									Ext.getCmp('maximo-atributo-tabla-5-claves').setDisabled(true);
								}else{
									Ext.getCmp('minimo-atributo-tabla-5-claves').setDisabled(false);
									Ext.getCmp('maximo-atributo-tabla-5-claves').setDisabled(false);
								}
         						//var valor=record.get('key');
								//Ext.getCmp('hidden-combo-suma-asegurada').setValue(valor);
							}		
            })
        },{
           header: "M\u00EDnimo",
           dataIndex: 'minimoAtributo',
           width: 80,
           editor: new Ext.form.TextField({
           		allowBlank: false,
        		blankText : 'N\u00FAmero m\u00EDnimo requerido',
        		id:'minimo-atributo-tabla-5-claves'
            })
        },{
           header: "M\u00E1ximo",
           dataIndex: 'maximoAtributo',
           width: 80,           
           editor: new Ext.form.TextField({
           		allowBlank: false,
        		blankText : 'N\u00FAmero m\u00E1ximo requerido',
        		id:'maximo-atributo-tabla-5-claves'
            })
           
        }
    ]);
    
    
    cm.defaultSortable = false;
   
    PlantGrid2 = Ext.data.Record.create([
           		
	        	{name: 'numeroClave',  		type: 'string',  mapping:'numeroClave'},
        		{name: 'descripcionAtributo',  type: 'string',  mapping:'descripcion'},
        		{name: 'descripcionFormatoAtributo',  	type: 'string',  mapping:'descripcionFormato'},        		
        		{name: 'maximoAtributo',  		type: 'string',  mapping:'maximo'},
        		{name: 'minimoAtributo',  		type: 'string',  mapping:'minimo'}                      
      ]);

    
    store = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
            url:'tablaCincoClaves/ListaAtributos.action'   
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaAtributos',
            id: 'attributos'
            }, PlantGrid2)
     
    });
    grid2 = new Ext.grid.EditorGridPanel({
			id:'grid-lista-atrib',
        	store: store,
        	cm: cm,
        	//autoScroll:true,
        	width:450,
        	height:190,
        	autoExpandColumn:'descripcionAtributo',
        	collapsible:true,
        	frame:true,
        	title:'Atributos',
        	clicksToEdit:1,
        	viewConfig: {
				forceFit: true
        	},
        	sm: new Ext.grid.RowSelectionModel({
			singleSelect: true,
			listeners: {							
        		rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			//selectedId = dataStore.data.items[row].id;
	        	 			var selGrid2 = Ext.getCmp('grid-lista-atrib').getSelectionModel().getSelected();
	        	 			var selIndexGrid2 = store.indexOf(selGrid2);
							//afuera=row;
							
	        	 			Ext.getCmp('eliminar-grid-lista-atributos').on('click',function(){ 
	        	 					 var numDataAtributo=store.getTotalCount();	
	        	 					 var numeliminaAtributo = numDataAtributo + 1;        	 					 			        	 					 		
	        	 					 if(selIndexGrid2 >= numDataAtributo){
	        	 					 	Ext.MessageBox.confirm("Mensaje","Esta seguro que desea eliminar este elemento?",function(btn){
									   			//alert("numeliminaAtributo"+numeliminaAtributo);
	        	 					 		if(btn == 'yes'){ 
									   			store.remove(rec);
									   			contadorAtributo--;
									   		}
									 	});
	        	 					 }else{
	        	 					 	Ext.MessageBox.alert('Error', 'No se pueden eliminar los atributos asociados');
	        	 					 }                         
	        	 					                                            
                                 }); 
                  }
            }                                                            							
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agrega un atributo',
            iconCls:'add',                        			
			handler:function(){         	            		            	
	        	 		//alert("contadorAtributo"+contadorAtributo);
            			var numFilaAtributo= Ext.getCmp('grid-lista-atrib').getStore().getTotalCount();            			
            		    var numAtributo=numFilaAtributo+contadorAtributo;            		    
            		    contadorAtributo++;	        	 		
	        	 		//alert("numAtributo"+numAtributo);
            		   
            		    if(contadorAtributo == 1){
            		    	selIndexGridatributo=Ext.util.JSON.encode(numFilaAtributo);
            		    	var p2 = new PlantGrid2({
            	        		descripcionAtributo: '',
                	    		descripcionFormatoAtributo: '',
	                    		maximoAtributo: '',
    	                		minimoAtributo: ''
            	    		});            	        	
                				grid2.stopEditing();
	                			store.insert(numFilaAtributo, p2);
    	            			grid2.startEditing(numFilaAtributo,0);
            		    }else if(numAtributo<25){
            		    		selIndexGridatributo=Ext.util.JSON.encode(numAtributo);
            		    		var p2 = new PlantGrid2({
            	        		descripcionAtributo: '',
                	    		descripcionFormatoAtributo: '',
	                    		maximoAtributo: '',
    	                		minimoAtributo: ''
            	    		});            	        	
                				grid2.stopEditing();
	                			store.insert(numAtributo, p2);
    	            			grid2.startEditing(numAtributo,0);
            		    }else{
            		    	Ext.MessageBox.alert('Error', 'Esta tabla solo puede crecer hasta 25 campos');
            		    }							            			           			
			}
			
            
        },'-',{
            text:'Eliminar',
            id:'eliminar-grid-lista-atributos',
            tooltip:'Elimina el atributo seleccionado',
            iconCls:'remove'
            
        }/*,'-',{
            text:'<s:text name="productos.tabla5claves.btn.editar"/>',
            id:"editar-clave",
            tooltip:'Edita la clave seleccionada',
            iconCls:'option'
        }*/] 
    	});

 	grid2.addListener({
 	
 			'beforeedit':{
				fn: function(event){
					 var selGridatributoBefore = Ext.getCmp('grid-lista-atrib').getSelectionModel().getSelected();
					 var selIndexGridatributoBefore = store.indexOf(selGridatributoBefore);
					 var numeroDeDatosAtributos= Ext.getCmp('grid-lista-atrib').getStore().getTotalCount();   
					 //alert("numeroDeDatosAtributos"+numeroDeDatosAtributos);
					 //alert("selIndexGridatributoBefore"+selIndexGridatributoBefore);
					 if(selIndexGridatributoBefore == -1){
					 	selIndexGridatributo=selIndexGridatributo;
					 }else{					 
					 	if(selIndexGridatributoBefore < numeroDeDatosAtributos){					 	 					 	 
					 	 
					 		 selIndexGridatributo=Ext.util.JSON.encode(selIndexGridatributoBefore);
					 	}else{
					 	 	selIndexGridatributo=Ext.util.JSON.encode(numeroDeDatosAtributos);				 	
					 	}
					 }					 					
				}
				,scope:this
			},			
			'validateedit':{
				fn: function(event){					
    				
    				var numeroDeDatosAtributos= Ext.getCmp('grid-lista-atrib').getStore().getTotalCount();    		    		    				
    				if(selIndexGridatributo < numeroDeDatosAtributos){
		        		//Ext.MessageBox.alert('Error', 'No se pueden editar los atributos asociados');		        			        		        			        	
		        		return false; 
	    			}else{	    				
	    				if(validaMaximoYMinimoAtributo()){
		        			return true	    				
	    				}else{
							return false
	    				}
	    			}		        				 	
				}
				,scope:this
			}		
		});//end grid.addListener
		
    store.load();	

    function validaMaximoYMinimoAtributo(){
    	//alert('min'+Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
       	//alert('max'+Ext.getCmp('maximo-clave-tabla-5-claves').getValue());
    	if(Ext.getCmp('minimo-atributo-tabla-5-claves').getValue()!= '' && Ext.getCmp('maximo-atributo-tabla-5-claves').getValue()!= ''){
	    	//alert('entro');
    		if(Ext.getCmp('minimo-atributo-tabla-5-claves').getValue()<= Ext.getCmp('maximo-atributo-tabla-5-claves').getValue()){
    			//Ext.getCmp('minimo-clave-tabla-5-claves').setValue(Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
    			//Ext.getCmp('maximo-clave-tabla-5-claves').setValue(Ext.getCmp('maximo-clave-tabla-5-claves').getValue());
    			return true
    		}
    		if(Ext.getCmp('minimo-atributo-tabla-5-claves').getValue()> Ext.getCmp('maximo-atributo-tabla-5-claves').getValue()){
    			Ext.MessageBox.alert('Error', 'Valor maximo es menor que minimo');
    			 var filaAtrib= Ext.getCmp('grid-lista-atrib').getStore().getTotalCount();
							 var recordAtributo = store.getAt(filaAtrib);
							 recordAtributo.set('maximoAtributo','');
							 recordAtributo.set('minimoAtributo',Ext.getCmp('minimo-atributo-tabla-5-claves').getValue());
	    					 //alert(rec.get('maximoClave'));
    			return false
    		}    		
    	}
    	return true
    }
    
    function validaformatoAtributo(){
    	//alert('min'+Ext.getCmp('minimo-clave-tabla-5-claves').getValue());
       	//alert('max'+Ext.getCmp('maximo-clave-tabla-5-claves').getValue());
    	
    			 var filaAtribu= Ext.getCmp('grid-lista-atrib').getStore().getTotalCount();
							 var recordAtributo = store.getAt(filaAtribu);
							 recordAtributo.set('maximoAtributo','');
							 recordAtributo.set('minimoAtributo','');
	    					 //alert(rec.get('maximoClave'));
    	
    }
/********************************************
forma
*******************************************/	
	var url='tablaCincoClaves/CatalogoClnatura.action';
	//var url='/tablaCincoClaves/CatalogoClnatura.action'; 			 		 			
	var storeClnatura = new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: url
       		}),
	       		reader: new Ext.data.JsonReader({
    	       	root:'listaClnatura',   
        	   	totalProperty: 'totalCount',
           		id: 'catalogo'           	         	
	        },[
        		{name: 'key',  type: 'string',  mapping:'key'},
        		{name: 'value',  type: 'string',  mapping:'value'}        		            	        		            
			]),
			//autoLoad:true,
			remoteSort: true
   	});
   	storeClnatura.setDefaultSort('catalogo', 'desc');
   	storeClnatura.load();
       var clnatura =new Ext.form.ComboBox({
						    tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
							store: storeClnatura,
						    displayField:'value',
						    valueField: 'value',
					    	typeAhead: true,
						    mode: 'local',
					    	triggerAction: 'all',
						    emptyText:'Seleccione Naturaleza',
					    	selectOnFocus:true,
					    	fieldLabel: 'Naturaleza de la tabla',
					    	allowBlank:false,
    						blankText : 'Naturaleza de la tabla es requerida.',
					    	name:"clnatura"					    	
			});
      var nombre= new Ext.form.TextField({
                    fieldLabel: 'Nombre',
                    labelSeparator:'',
                    width:'80'  ,
                    allowBlank:false,
                    maxLength: '30',
                    maxLengthText: 'El texto debe ser de 30 caracteres',
    				blankText : 'Nombre requerido.',
                    name:'nombre1' 
   		});		

      var descripcion= new Ext.form.TextField({
      				id:'descripcion-test',
                    fieldLabel: 'Descripci\u00F3n',
                    labelSeparator:'',                    
                    width:'196'  ,
                    allowBlank:false,
                    maxLength: '60',
                    maxLengthText: 'El texto debe ser de 60 caracteres',
    				blankText : 'Descripcion requerida.',
                    name:'descripcion1' 
   		});		

      var num= new Ext.form.NumberField({
                    id:'id-num1-tabla-5-claves',
                    fieldLabel: 'N\u00FAm.',
                    labelSeparator:'',                    
                    width:'50',
                    disabled:true,
                    name:'num1'
   		});		
   	 var tipoUnico= new Ext.form.Radio({	
   	 						id:'radio-tipo-unico-tabla5Claves',	 				
    		 				boxLabel:'\u00DAnico',
    		 				labelSeparator:'',                    
    		 				fieldLabel:'Tipo de acceso',
    		 				checked:true,
    		 				name:'tipoDeAcceso',
    		 				onClick:function(){
				            	if(this.getValue()){				            					            		
				            		Ext.getCmp('id-radio-tipo-de-acceso').setValue("U");				            	
				            	}
				            }
    		 });
	 var tipoPorTramos= new Ext.form.Radio({		 				
	 						id:'radio-tipo-porTamos-tabla5Claves',
    		 				boxLabel:'Por Tramos',
    		 				hideLabel:true,
    		 				hideParent:true,
    		 				name:'tipoDeAcceso',
    		 				onClick:function(){
				            	if(this.getValue()){				            	
				            		Ext.getCmp('id-radio-tipo-de-acceso').setValue("T");				            	
				            	}
				            }
	 });	 	 	
	 
	 var switchModificar= new Ext.form.Checkbox({
	 						id:'id-modifica-valor-tabla5claves',
							hideLabel:true,
							checked : true,
							hidden:true,
							inputValue: "S",
            				onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}else{      
				            		this.setRawValue("N");      						
				            	}
				            },
				            name:'switchModificacion'
     });	
	 var switchModificarDummy= new Ext.form.Checkbox({
							width:'350',
							hidden:true,
							labelSeparator:'',                    
            				fieldLabel:'Modificaci\u00F3n de valores a cualquier fecha',
            				hideLabel:true
            				
     });
     var switchEnviar= new Ext.form.Checkbox({
     						id:'id-envia-tabla-error-tabla5claves',
       						hideLabel:true,
            				onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}else{      
				            		this.setRawValue("N");      						
				            	}
				            },
				            name:'switchEnviar'
    });
     var switchEnviarDummy= new Ext.form.Checkbox({
     						width:'350',
     						hidden:true,
     						labelSeparator:'',                   
            				fieldLabel:'Enviar a tabla de error si no existe valor'
            				
    });

    var formPanel = new Ext.FormPanel({
    	id:'id-form-panel-tabla5claves',
		//autoScroll:true,
        frame:true,
        region:'center',
        bodyStyle:'padding:5px 5px 0',
        //url:'<s:url namespace="tablaCincoClaves" action="InsertarTabla" includeParams="none"/>',
        url:'/tablaCincoClaves/InsertarTabla.action',
        width:500,
        border:false,
        items:[{
        	layout:'form',
        	border:false,
        	title:'Encabezado',
        	collapsible:true,
        	width: '450',
        	items:[	{xtype:'hidden',id:'hidden-numero-tabla-cinco-claves',name:'num1'},
        			{xtype:'hidden',id:'id-radio-tipo-de-acceso',name:'radioTipoAcceso'},
        			{xtype:'hidden',id:'hidden-radio-tipo-de-acceso',name:'hiddenRadioTipoAcceso'},
        			{xtype:'hidden',id:'hidden-tipo-transaccion-tabla-5-claves',name:'tipoTransaccion'},             			     		
        		{
		        layout:'column',
		        border:false,
	    	    width: '430',
    	    	items: [{
		        		columnWidth:.23,
    	  		        labelAlign: 'top',
    		    		layout:'form',
        				border:false,
        				items:[nombre]
		        	},{
    		    		columnWidth:.5,
    			        labelAlign: 'top',
        				layout:'form',
        				border:false,
        				items:[descripcion]
		   		    },{
    		    		columnWidth:.18,
    			        labelAlign: 'top',
        				layout:'form',
        				border:false,
        				items:[num]
		        	}]
		    },{
			    layout:'form',							                
		        border:false,
		        bodyBorder:true, 
	    	    labelAlign:'left',   	    
        		width: 400,
		        items: [{
			            layout:'column',
		    	        border:false,
		        	    labelAlign:'left',
			            items:[{
				                columnWidth:.45,
				                layout: 'form',
				                labelAlign:"left",
			    	            border:false,
			        	        items: [tipoUnico]
				            },{
			               		columnWidth:.55,
				                layout: 'form',
				                border:false,
			    	            items: [tipoPorTramos]
		            	}]
		        }]
		    },{
			    layout:'form',							                
		        border:false,
	    	    bodyBorder:true, 	        	    
        		width: '400',
		        items: [{
			            layout:'column',
			            border:false,
		    	        items:[{
			    	            columnWidth:.6,
			        	        layout: 'form',
			            	    labelAlign:"top",
			                	border:false,
				                items: [switchModificarDummy]
				            },{
			               		columnWidth:.3,
				                layout: 'form',
			    	            border:false,
			        	        items: [switchModificar]
			            }]	
		        }]
	    	},/*{
			    layout:'form',							                
		        border:false,
	    	    bodyBorder:true, 	        	    
        		width: '400',
		        items: [{
			            layout:'column',
			            border:false,
		    	        items:[{
			    	            columnWidth:.6,
			        	        layout: 'form',
			            	    labelAlign:"top",
			                	border:false,
				                items: [switchEnviarDummy]
				            },{
			               		columnWidth:.3,
				                layout: 'form',
			    	            border:false,
			        	        items: [switchEnviar]
			            }]
		        }]
		    },*/clnatura]
	    },
	    	grid,grid2]     
    });
 	
    // define window and show it in desktop
 var wind = new Ext.Window({
            title: 'Tablas De Apoyo De Cinco Claves',
            closable:true,
            buttonAlign:'center',
            width:490,
            height:645,
            //autoScroll:true,
            plain:true,
            layout: 'border',
            modal:true,
			items:[formPanel],
  			buttons:[{
  					text:'Grabar',
  					handler: function(){
	  						var boolean1=true;
  							var boolean2=true;
  							var boolean3=true;
  							var boolean4=true;
  							var boolean5=true;
		  					if(Ext.isEmpty(nombre.getValue())) {
		  						nombre.markInvalid("Este dato es requerido");
		  						boolean1=false;
		  					}
		  					if(Ext.isEmpty(descripcion.getValue())) {
		  						descripcion.markInvalid("Este dato es requerido");
		  						boolean2=false;
		  					}
		  					if(Ext.isEmpty(clnatura.getValue())) {
		  						clnatura.markInvalid("Este dato es requerido");
		  						boolean3=false;
		  					}
		  					//alert("store"+store.getTotalCount());
		  					//alert("store"+store.getCount());
		  					if(dataStore.getTotalCount() == 0 && dataStore.getCount()==0) {
		  						Ext.MessageBox.alert('Error', 'almenos debe existir una clave');
		  						boolean4=false;
		  					}
		  						//alert("dataStore"+dataStore.getTotalCount());
		  						//alert("dataStore"+dataStore.getCount());
		  					if(store.getTotalCount() == 0 && store.getCount()==0) {
		  						Ext.MessageBox.alert('Error', 'almenos debe existir un atributo');
		  						boolean5=false;
		  					}
  							if(boolean1 && boolean2 && boolean3 && boolean4 && boolean5) {
  								
  								var total = dataStore.getCount();
  								
  								//para quitar las descripciones que esten vacias, donde tambien se les hace un trim
  								//para que la funcion collect funcione adecuadamente.
  								
  								for(var j=0; j < dataStore.getCount(); j++){
  									var recDes = dataStore.getAt(j).get('descripcionClave');
  									if(Ext.isEmpty(recDes))total--;
  									dataStore.getAt(j).set('descripcionClave' , recDes.trim() );
  								}
  								
  								var ids = dataStore.collect('descripcionClave');
  								
  								if(total != ids.length){
  									Ext.MessageBox.alert('Error', 'No pueden existir claves duplicadas');
  									return;
  								}
  								
															
								var tipoAcceso='';	        
								var modifica='';
								var envia='';
								if(tipoUnico.getValue()){
									tipoAcceso='U';
								}else{
									tipoAcceso='T';
								}
								if(switchModificar.getValue()){
									modifica='S';
								}else{
									modifica='N';
								}
								if(switchEnviar.getValue()){
									envia='S';
								}else{
									envia='N';
								}
								var params=	"clnatura="+clnatura.getValue()+
  											"&&nombre1="+nombre.getValue()+
  											"&&descripcion1="+descripcion.getValue()+
  											"&&num1="+num.getValue()+
  											"&&tipoDeAcceso="+tipoAcceso+  									
  											"&&switchModificacion="+modifica+  									
  											"&&switchEnviar="+envia+"&&";
  								
  								var numeroDeClavesDeLaBase = dataStore.getTotalCount();
  								for(var i = 0 ; i < numeroDeClavesDeLaBase ; i++){
  									//var numclave= i++;
  									params =params +"listaClaves[" + i + "].numeroClave=" + i + 
  													"&&listaClaves[" + i + "].descripcion=" + dataStore.getAt(i).get('descripcionClave')+
  													"&&listaClaves[" + i + "].descripcionFormato=" + dataStore.getAt(i).get('descripcionFormatoClave')+
  													"&&listaClaves[" + i + "].maximo=" + dataStore.getAt(i).get('maximoClave')+
  													"&&listaClaves[" + i + "].minimo=" + dataStore.getAt(i).get('minimoClave')+"&&";
  								}
  								var	recClavesModificadas = Ext.getCmp('grid-clave').getStore().getModifiedRecords();  						
  								var numeroClavesModificadas = recClavesModificadas.length;						  												
								//alert('numeroDeClavesDeLaBase'+numeroDeClavesDeLaBase);
								//alert('numeroClavesModificadas'+numeroClavesModificadas);
								for (var j=0; j<numeroClavesModificadas; j++) {
									var m = j+numeroDeClavesDeLaBase;
									//alert(m);
									//alert('recModificadoDescricpcion'+recClavesModificadas[j].get('descripcionClave'));
									params =params +"listaClaves[" + m + "].numeroClave=" + m + 
													"&&listaClaves[" + m + "].descripcion=" + recClavesModificadas[j].get('descripcionClave')+ 
													"&&listaClaves[" + m + "].descripcionFormato=" + recClavesModificadas[j].get('descripcionFormatoClave')+
													"&&listaClaves[" + m + "].maximo=" + recClavesModificadas[j].get('maximoClave')+ 
													"&&listaClaves[" + m + "].minimo=" + recClavesModificadas[j].get('minimoClave')+"&&";
								}
								var numeroDeAtributosDeLaBase = store.getTotalCount();
  								//se comenta por que no es necesario enviar todos los atributos solo los agregados ya que no se editan los de la base
  								for(var i = 0 ; i < numeroDeAtributosDeLaBase ; i++){
  									//var numclave= i++;
  									params =params +"listaAtributos[" + i + "].numeroClave=" + store.getAt(i).get('numeroClave') + 
  													"&&listaAtributos[" + i + "].descripcion=" + store.getAt(i).get('descripcionAtributo')+
  													"&&listaAtributos[" + i + "].descripcionFormato=" + store.getAt(i).get('descripcionFormatoAtributo')+
  													"&&listaAtributos[" + i + "].maximo=" + store.getAt(i).get('maximoAtributo')+
  													"&&listaAtributos[" + i + "].minimo=" + store.getAt(i).get('minimoAtributo')+"&&";
  								}
								//var recAtributosModificados = store.getModifiedRecords();
  								var recAtributosModificados = Ext.getCmp('grid-lista-atrib').getStore().getModifiedRecords();
  								var numeroAtributosModificados = recAtributosModificados.length;						  												
								for (var j=0; j<numeroAtributosModificados; j++) {
									var m = j+numeroDeAtributosDeLaBase;
									params =params +//"listaAtributos[" + m + "].numeroClave=" + m + 
													"&&listaAtributos[" + m + "].descripcion=" + recAtributosModificados[j].get('descripcionAtributo')+ 
													"&&listaAtributos[" + m + "].descripcionFormato=" + recAtributosModificados[j].get('descripcionFormatoAtributo')+
													"&&listaAtributos[" + m + "].maximo=" + recAtributosModificados[j].get('maximoAtributo')+ 
													"&&listaAtributos[" + m + "].minimo=" + recAtributosModificados[j].get('minimoAtributo')+"&&";
								}
								//alert("PARAMS"+params);	
								
									var conn2 = new Ext.data.Connection();
				            		conn2.request ({
				            				//url:'<s:url namespace="tablaCincoClaves" action="InsertarTabla" includeParams="none"/>',
											url: 'tablaCincoClaves/InsertarTabla.action',
											method: 'POST',
											successProperty : '@success',
											params : params,
				    	       				callback: function (options, success, response) {
	                       						//alert(success);
	                       						//alert("*"+Ext.util.JSON.decode(response.responseText).success);
	                       						if (success) {
		        	                  					Ext.MessageBox.alert('Status', 'Tabla agregada');
		            	              					dataStoreTabla5Claves.load();
						  								store.commitChanges();
  														dataStore.commitChanges();
		            	              					wind.close();
            	           						} else {
		                       							Ext.MessageBox.alert('Error', 'Tabla no agregada');        	                							
    	                	      				}
       		               					},
					            	   		waitTitle:'Espere',
					    					waitMsg:'Procesando...'
		    		       			});
		    	       			
  							
		  																  						                   	        		 
							}else{
									Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
							}						   
  						}
  					},{
  						text:'Consulta/Actualizaci\u00F3n de valores',
  						id:'id-boton-consulta',
  						//disabled :true,
  						handler: function(){
  							if(dataStore.getTotalCount()!=0){
  								//alert("numEditable"+ Ext.getCmp('id-num1-tabla-5-claves').getValue());
  								creaVentanaDeConsulta(Ext.getCmp('id-num1-tabla-5-claves').getValue());  		
  								var nombreTemporal =nombre.getValue();
  								Ext.getCmp('nombre-editable').setValue(nombreTemporal);
  								var descripcionTemporal = Ext.getCmp('descripcion-test');
  								Ext.getCmp('descripcion-editable').setValue(descripcion.getValue());
  								Ext.getCmp('num-editable').setValue(num.getValue());
  							}else{
	  							Ext.MessageBox.alert('Error', 'Debe existir almenos una clave asociada');
  								
  							}				
  					}
  						
	  			},{
  						text:'Carga Autom\u00E1tica',
  						handler:function(){  						
  							//windowCargaAutomatica.show();
  						}
  					},{
  						text:'Cancelar',
  						handler:function(){  						
  							wind.close();
  						}
  						
  					}]
        });
	wind.show();
	function encabezados(){    
    	var maxlength=dataStore.getTotalCount();
		for(ind=0;ind<5;ind++){
			if(ind<maxlength){
				var recordVariableColumns=dataStore.getAt(ind);
				if(!recordVariableColumns){
					Ext.MessageBox.alert('Error', 'Existen Claves duplicadas no visibles. Consulte a su soporte.', function(){
						wind.close();
					});
				}
				//alert(recordVariableColumns);
				var recordDescripcion=recordVariableColumns.get("descripcionClave");
				//alert(recordDescripcion);
				cmClaves.setColumnHeader(ind,recordDescripcion);
			}else{
				cmClaves.setHidden(ind,true);
			}
		}
	}
	
	function validarClavesObligatorias(recordsClavesObligatorias){
    	if(recordsClavesObligatorias.length == 0){
    		boleanoG=false;
    	}else{
	    	var maxlengthGuarda=dataStore.getTotalCount();
	    	var maxlengthRecords=recordsClavesObligatorias.length;
	    	
	    	var indexRowInvalido;
	    	var indexColumnaInvalida;
	    	
			for(var irec=0;irec<maxlengthRecords;irec++){
				for(var ind=0;ind<maxlengthGuarda;ind++){
						boleanoG=true;
						banderaGCO=false;
						switch(ind){
							case 0:
								if(recordsClavesObligatorias[irec].get('descripcionClave1').length==0){
									banderaGCO=true;
									indexColumnaInvalida = 0;
									indexRowInvalido = storeClaves.indexOf(recordsClavesObligatorias[irec]);
								}
								break;
							case 1:
								if(recordsClavesObligatorias[irec].get('descripcionClave2').length==0){
									banderaGCO=true;
									indexColumnaInvalida = 1;
									indexRowInvalido = storeClaves.indexOf(recordsClavesObligatorias[irec]);
								}
								break;
							case 2:
								if(recordsClavesObligatorias[irec].get('descripcionClave3').length==0){
									banderaGCO=true;
									indexColumnaInvalida = 2;
									indexRowInvalido = storeClaves.indexOf(recordsClavesObligatorias[irec]);
								}
								break;
							case 3:
								if(recordsClavesObligatorias[irec].get('descripcionClave4').length==0){
									banderaGCO=true;
									indexColumnaInvalida = 3;
									indexRowInvalido = storeClaves.indexOf(recordsClavesObligatorias[irec]);
								}
								break;
							case 4:
								if(recordsClavesObligatorias[irec].get('descripcionClave5').length==0){
									banderaGCO=true;
									indexColumnaInvalida = 4;
									indexRowInvalido = storeClaves.indexOf(recordsClavesObligatorias[irec]);
								}
								break;
								
						}
						if(banderaGCO){
							ind=maxlengthGuarda;
							irec=maxlengthRecords;
							boleanoG=false;
						}
						
						/*
						var idClaveObligatoria='descripcionClave'+ind+'';
						alert(idClaveObligatoria);
						var lengthCO=recordsClavesObligatorias[ind].get('"'+idClaveObligatoria+'"').length;
						alert(lengthCO);
						if(recordsClavesObligatorias[ind].get('"'+idClaveObligatoria+'"').length==0){
							ind=maxlengthGuarda;
							irec=maxlengthRecords;
							boleanoG=false;
						}*/
								
				}
			}
			if(!boleanoG){
				Ext.MessageBox.alert('Error', 'Por favor llene todas las columnas requeridas', 
					function(){
						
						//alert("indexRowInvalido: "+indexRowInvalido);
						//alert("indexColumnaInvalida: "+indexColumnaInvalida);
						//Posicionar en la celda invalida, para que el usuario sepa qué editar
						gridEditableClaves.startEditing(indexRowInvalido, indexColumnaInvalida);
					}
				);
			}
		}
		return boleanoG;
				
	}
	
  	if(!Ext.isEmpty(edita) && edita >= 0 ){
		var rec = dataStoreTabla5Claves.getAt(edita);
		var nmTabla= rec.get('nick');
		//var camaraAction ='EditaTabla5ClavesCabecera.action?num1='+nmTabla;
		//url4Edit5KeysTabHeader='<s:url namespace="/tablaCincoClaves" action="'+camaraAction+'"/>';
		//alert(url4Edit5KeysTabHeader);	
		var storeListaValores = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: 'tablaCincoClaves/EditaTabla5ClavesCabecera.action?num1='+nmTabla
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'editaCabeceraTabla5claves'
	        	},[
	        		{name: 'nombre1',  type: 'string',  mapping:'nombre'},
	        		{name: 'descripcion1',  type: 'string',  mapping:'descripcion'},
	        		{name: 'num1',  type: 'string',  mapping:'numeroTabla'},
	        		{name: 'enviarTablaErrores',  type: 'string',  mapping:'enviarTablaErrores'},
	        		{name: 'modificaValores',  type: 'string',  mapping:'modificaValores'},	        		
	        		{name: 'ottipoac',  type: 'string',  mapping:'ottipoac'},
	        		{name: 'ottipotb',  type: 'string',  mapping:'ottipotb'},	        		
	        		{name: 'clnatura',  type: 'string',  mapping:'dsNatura'}
	        		//{name: 'claveDependencia',  type: 'string',  mapping:'claveDependencia'},
	        		//{name: 'dsNatura',  type: 'string',  mapping:'dsNatura'},
	        		//{name: 'numero',  type: 'string',  mapping:'cdAtribu'}
	        		            
				]),
			
			remoteSort: true
    	});
    	//alert('despues de declarar el store');
    	storeListaValores.on('load', function(){
    				//alert('load');
					if(storeListaValores.getTotalCount()>0){
    						var recLV= storeListaValores.getAt(0);
    						var recLVETE = recLV.get('enviarTablaErrores');
    						var recLVMV = recLV.get('modificaValores');
    						var recT5CT = recLV.get('ottipoac');
    						//alert("tipoAcceso"+recLV.get('ottipoac'));
                           	Ext.getCmp('id-form-panel-tabla5claves').getForm().loadRecord(recLV);  
                           	Ext.getCmp('id-num1-tabla-5-claves').setValue(recLV.get('num1'));                            
                           	
                           	/*if(recLVETE == "S"){
                           		Ext.getCmp('id-envia-tabla-error-tabla5claves').setValue(true);
                          		Ext.getCmp('id-envia-tabla-error-tabla5claves').setRawValue("S");
                           	}else{
	                           	Ext.getCmp('id-envia-tabla-error-tabla5claves').setValue(false);
                          		Ext.getCmp('id-envia-tabla-error-tabla5claves').setRawValue("N");
                           	}*/
                           	if(recLVMV == "S"){
                           		Ext.getCmp('id-modifica-valor-tabla5claves').setValue(true);
                          		Ext.getCmp('id-modifica-valor-tabla5claves').setRawValue("S");
                           	}else{
	                           	Ext.getCmp('id-modifica-valor-tabla5claves').setValue(false);
                          		Ext.getCmp('id-modifica-valor-tabla5claves').setRawValue("N");
                           	}
                           	if(recT5CT == "T"){
                           		Ext.getCmp('radio-tipo-porTamos-tabla5Claves').setValue(true);
                           		Ext.getCmp('radio-tipo-unico-tabla5Claves').setValue(false);
                          		Ext.getCmp('hidden-radio-tipo-de-acceso').setValue("T");
                           	}if(recT5CT == "U"){
                           		Ext.getCmp('radio-tipo-unico-tabla5Claves').setValue(true);
                           		Ext.getCmp('radio-tipo-porTamos-tabla5Claves').setValue(false);
                          		Ext.getCmp('hidden-radio-tipo-de-acceso').setValue("U");
                           	}else{
	                           	//Ext.getCmp('radio-tipo-unico-tabla5Claves').setValue(true);
                          		Ext.getCmp('hidden-radio-tipo-de-acceso').setValue("U");
                           	}
                           	//alert('cargando store');
                           	
							mStoreA = Ext.getCmp('grid-lista-atrib').getStore();
							mStoreA.baseParams = mStoreA.baseParams || {};
                       		mStoreA.baseParams['num1'] = nmTabla;
                       		//alert('cargando store');
					        mStoreA.reload({					        	
								callback : function(r,options,success) {
									//alert('callback');
									if (!success) {
        		    					//  Ext.MessageBox.alert('Aviso','No se encontraron registros');  
							            mStoreA.removeAll();
						            }
				                }
							});                           
							mStoreC = Ext.getCmp('grid-clave').getStore();
							mStoreC.baseParams = mStoreC.baseParams || {};
                       		mStoreC.baseParams['num1'] = nmTabla;
                       		//alert('cargando store');
					        mStoreC.reload({					        	
								callback : function(r,options,success) {
									//alert('callback');
									if (!success) {
        		    					//  Ext.MessageBox.alert('Aviso','No se encontraron registros');  
							            mStoreC.removeAll();
						            }
				                }
							});
							
							Ext.getCmp('hidden-numero-tabla-cinco-claves').setValue(recLV.get('num1'));
							Ext.getCmp('hidden-tipo-transaccion-tabla-5-claves').setValue('2');
					//Ext.getCmp('grid-clave').getStore().load();
					//Ext.getCmp('grid-lista-atrib').getStore().load();
					}
        });
        storeListaValores.load();
		//alert('jamas entra al load');
}

};


TablasDeApoyoBorrado = function(dataStoreTabla5Claves, row , tablaform) {
	
	            		Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           					if(btn == 'yes'){
           						var rec = dataStoreTabla5Claves.getAt( row );
                                var nmTabla= rec.get('nick');
           					     //alert("rec="+ rec );
           					     //alert("nmTabla=" + nmTabla );
                                
	            				tablaform.form.submit({       
					            	url:'atributosVariables/EliminarTablaClave.action' +'?nmTabla=' + nmTabla,
					            	waitTitle:'Espere',
					            	waitMsg:'Eliminando tabla...',
					            	failure: function(form, action) {
								    	Ext.MessageBox.alert('Error ', 'Error al eliminar Tabla');
									},
									success: function(form, action) {
								    	Ext.MessageBox.alert('Status', 'La tabla fue eliminada con &eacute;xito');						    		
						    			dataStoreTabla5Claves.reload();  		
									}
			        			});  
			        		}
						});	
	
};
