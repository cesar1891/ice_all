Ext.onReady(function(){
Ext.QuickTips.init();
Ext.QuickTips.enable();
Ext.form.Field.prototype.msgTarget = "side";

 var storeFormatos = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({url: _ACTION_OBTENER_COMBO_FORMATO}),
                reader: new Ext.data.JsonReader({
            		root:'comboFormatosCampo',
            		successProperty : '@success'
	        		},
	        		[
		          		{name : 'codigo', mapping : 'codigo', type : 'string'},
		             	{name : 'descripcion',mapping : 'descripcion',type : 'string'}
             		]
	        	)
    	});
	
	var storeListaValores = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({url: _ACTION_OBTENER_COMBO_LISTA_VALORES}),
                reader: new Ext.data.JsonReader({
            		root:'comboListaValores',
            		successProperty : '@success'
	        		},
	        		[
		          		{name : 'codigo', type : 'string'},
		             	{name : 'descripcion',type : 'string'}
             		]
	        	),
	        	baseParams: {cdTipo: '1'}
    	});
    	
    var storeTipoPersona = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({url: _ACTION_OBTENER_COMBO_TIPO_PERSONA}),
                reader: new Ext.data.JsonReader({
            		root:'personasJ',
            		successProperty : '@success'
	        		},
	        		[
		          		{name : 'codigo', type : 'string'},
		             	{name : 'descripcion',type : 'string'}
             		]
	        	)
    	});
    	
    var storeCategoriaPersona = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({url: _ACTION_OBTENER_COMBO_CATEGORIA_PERSONA}),
                reader: new Ext.data.JsonReader({
            		root:'comboCategorias',
            		successProperty : '@success'
	        		},
	        		[
		          		{name : 'codigo', type : 'string'},
		             	{name : 'descripcion',type : 'string'}
             		]
	        	)
    	});
     
     
     	var recordGrilla = new Ext.data.Record.create([
				{name:'cdAtribu',  type:'string'},
		        {name:'dsAtribu',  type:'string'},
		        {name:'swFormat', type:'string'},
		        {name:'nmlmin',  type:'string'},
		        {name:'nmlmax',  type:'string'},
		        {name:'otTabVal',  type:'string'},
		        {name:'gbSwFormat',  type:'string'},
		        {name:'cdFisJur',  type:'string'},
		        {name:'cdCatego',  type:'string'},
		        {name:'swObliga',  type:'bool'}
			]);	
			
    var storeGrilla = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({url: _ACTION_BUSCAR_ATRIBUTOS_VARIABLES_PERSONA}),
                reader: new Ext.data.JsonReader({
            			root:'MListAtribuVarPersona',
            			totalProperty: 'totalCount',
            			successProperty : '@success'
	        	},
	        	recordGrilla
	        	)
    });


    var comboFormatos = new Ext.form.ComboBox(
    	{
    		tpl: '<tpl for="."><div ext:qtip="{codigo}.{descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
            store: storeFormatos, 
            anchor:'100%', 
            displayField:'descripcion', 
            valueField: 'codigo', 
            hiddenName: 'swFormat',
            typeAhead: true, 
            triggerAction: 'all', 
            lazyRender:   true, 
            emptyText:'Seleccione Formato...', 
            selectOnFocus:true,
            name: 'swFormat',
            id:'comboFormatoId',
            forceSelection: true,
            mode: 'local'
       });

    var comboListaValores = new Ext.form.ComboBox(
    	{
    		tpl: '<tpl for="."><div ext:qtip="{codigo}.{descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
            store: storeListaValores, 
            anchor:'100%', 
            displayField:'descripcion', 
            valueField: 'codigo', 
            hiddenName: 'otTabVal',
            typeAhead: true, 
            triggerAction: 'all', 
            lazyRender:   true, 
            emptyText:'Seleccione Valor...', 
            selectOnFocus:true,
            name: 'otTabVal',
            id:'comboListaValoresId',
            forceSelection: true,
            mode: 'local'
     });
	
	var comboTipoPersona = new Ext.form.ComboBox(
    	{
    		tpl: '<tpl for="."><div ext:qtip="{codigo}.{descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
            store: storeTipoPersona, 
            anchor:'100%', 
            displayField:'descripcion', 
            valueField: 'codigo', 
            hiddenName: 'cdFisJur',
            typeAhead: true, 
            triggerAction: 'all', 
            lazyRender:   true, 
            emptyText:'Seleccione Tipo de Persona...', 
            selectOnFocus:true,
            name: 'cdFisJur',
            id:'comboTipoPersonaId',
            forceSelection: true,
            mode: 'local'
     });
	
	var comboCategoriaPersona = new Ext.form.ComboBox(
    	{
    		tpl: '<tpl for="."><div ext:qtip="{codigo}.{descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
            store: storeCategoriaPersona, 
            anchor:'100%', 
            displayField:'descripcion', 
            valueField: 'codigo', 
            hiddenName: 'cdCatego',
            typeAhead: true, 
            triggerAction: 'all', 
            lazyRender:   true, 
            emptyText:'Seleccione Categoria de la Persona...', 
            selectOnFocus:true,
            name: 'cdCatego',
            id:'comboCategoriaPersonaId',
            forceSelection: true,
            mode: 'local'
     });
     
    var nmObligatorio = new Ext.grid.CheckColumn({
      header:getLabelFromMap('cmAtbVrbPerOblig',helpMap,'Obl.'),
      tooltip:getToolTipFromMap('cmAtbVrbPerOblig',helpMap, 'Tilde si es obligatorio'),
      dataIndex: 'swObliga',
      align: 'center',
      sortable: true,
      width: 40
    });
    
	var cm = new Ext.grid.ColumnModel ([
			{
		        header: getLabelFromMap('cmAtbVrbPerCodigo',helpMap,'C&oacute;digo'),
		        tooltip: getToolTipFromMap('cmAtbVrbPerCodigo',helpMap,'C&oacute;digo'),
		        dataIndex: 'cdAtribu',
		        sortable: true,
		        width:50,
		        editor: new Ext.form.TextField({name: 'cdAtribu',maxLength:4 , readOnly:true })
			},
			{
		        header: getLabelFromMap('cmAtbVrbPerDesc',helpMap,'Descripci&oacute;n'),
		        tooltip: getToolTipFromMap('cmAtbVrbPerDesc',helpMap,'Descripci&oacute;n'),
		        dataIndex: 'dsAtribu',
		        width: 130,	
		        sortable: true,
		        editor: new Ext.form.TextField(
		        	 {name: 'dsAtribu',
		        	  maxLength:120,
		        	  allowBlank: false }),
		        	  
		        //  funcion que permite colocarle a todas las celdas el rojo de requerido y el icono de requerido
                renderer:function extGrid_renderer(val, cell, record, row, col, store) {
						if (record != undefined) {
						// Make sure we have a value
						   if (val == '') {
						   // No value so highlight this cell as in an error state
							  cell.css += 'x-form-invalid';
							  cell.attr = 'ext:qtip="Este campo es requerido"; ext:qclass="x-form-invalid-tip"';
							 //cell.attr = 'ext:qclass="x-form-invalid-tip"';
					        } else {
 						           // Value is valid
						             cell.css = '';
						             cell.attr = 'ext:qtip=""';
					               }
					       }
 	             // Return the value so that it is displayed within the grid
				 return val;
			    }
			},
			{
		      header: getLabelFromMap('cmAtbVrbPerFormat',helpMap,'Formato'),
		      tooltip: getToolTipFromMap('cmAtbVrbPerFormat',helpMap,'Formato'),
		      dataIndex: 'swFormat',
		      sortable: true,
		      width: 100,		        
		      editor: comboFormatos,
		      // renderer: renderComboEditor(comboFormatos)
              renderer: function extGrid_renderer(val, cell, record, row, col, store) {
                    if (record != undefined) {

                         // Make sure we have a value
                          if (val == '') {
                           // No value so highlight this cell as in an error state

                             cell.css += 'x-form-invalid';
                             cell.attr = 'ext:qtip="Este campo es requerido"; ext:qclass="x-form-invalid-tip"';

                             //cell.attr = 'ext:qclass="x-form-invalid-tip"';
                          }else {
                           //Value is valid

                                 cell.css = '';
                                 cell.attr = 'ext:qtip=""';
                                }
                    }

                     var idx;
                     
                     //value = eval(value);
                     for (var i=0; i < comboFormatos.store.data.items.length; i++) {

                     var registro = comboFormatos.store.getAt(i);
                           if (registro) {
                                if (registro.get(comboFormatos.valueField) == val) {
                                          idx = i;
                                  }//else alert("registro: " + registro.get(combo.valueField) + "\nvalue: " + value);
                           } 
                     }
                      //var idx = combo.store.find(combo.valueField, value, 0, true, true);
                      var rec = comboFormatos.store.getAt(idx);
                    //  console.log(comboFormatos.store);
                      return (rec == null)?val:rec.get(comboFormatos.displayField);
                      //return val;
              }
			},
			{
		        header: getLabelFromMap('cmAtbVrbPerMin',helpMap,'M&iacute;n'),
		        tooltip: getToolTipFromMap('cmAtbVrbPerMin',helpMap,'M&iacute;nimo'),
		        dataIndex: 'nmlmin',
		        width: 40,
		        sortable: true,
		     // editor: new Ext.form.NumberField({name: 'nmLMin'})

                editor: new Ext.form.NumberField({  // name: 'nmLMin',	
                 maxLength:2,                	
                 allowBlank: false
                 }),
                   //  funcion que permite colocarle a todas las celdas el rojo de requerido y el icono de requerido
                renderer:function extGrid_renderer(val, cell, record, row, col, store) {
						if (record != undefined) {
						// Make sure we have a value
						   if (val == '') {
						   // No value so highlight this cell as in an error state
							  cell.css += 'x-form-invalid';
							  cell.attr = 'ext:qtip="Este campo es requerido"; ext:qclass="x-form-invalid-tip"';
							 //cell.attr = 'ext:qclass="x-form-invalid-tip"';
					        } else {
 						           // Value is valid
						             cell.css = '';
						             cell.attr = 'ext:qtip=""';
					               }
					       }
 	             // Return the value so that it is displayed within the grid
				 return val;
			    }
			},
			{
		        header: getLabelFromMap('cmAtbVrbPerMax',helpMap,'M&aacute;x'),
		        tooltip: getToolTipFromMap('cmAtbVrbPerMax',helpMap,'M&aacute;ximo'),
		        dataIndex: 'nmlmax',
		        width: 40,
		        sortable: true,
		      //  editor: new Ext.form.NumberField({name: 'nmLMax'})
		        
                editor: new Ext.form.NumberField({ 
                 maxLength:2,                 
                 allowBlank: false
                 }),
                   //  funcion que permite colocarle a todas las celdas el rojo de requerido y el icono de requerido
                renderer:function extGrid_renderer(val, cell, record, row, col, store) {
						if (record != undefined) {
						// Make sure we have a value
						   if (val == '') {
						   // No value so highlight this cell as in an error state
							  cell.css += 'x-form-invalid';
							  cell.attr = 'ext:qtip="Este campo es requerido"; ext:qclass="x-form-invalid-tip"';
							 //cell.attr = 'ext:qclass="x-form-invalid-tip"';
					        } else {
 						           // Value is valid
						             cell.css = '';
						             cell.attr = 'ext:qtip=""';
					               }
					       }
 	             // Return the value so that it is displayed within the grid
				 return val;
			    }
		        
			},
			nmObligatorio,
			{
		        header: getLabelFromMap('cmAtbVrbPerVal',helpMap,'Lista de Valores'),
		        tooltip: getToolTipFromMap('cmAtbVrbPerVal',helpMap,'Lista de Valores'),
		        dataIndex: 'otTabVal',
		        sortable: true,
		        editor: comboListaValores,
		        renderer: renderComboEditor(comboListaValores)
			},
			{
		        header: getLabelFromMap('cmAtbVrbPerTipPer',helpMap,'Tipo de Persona'),
		        tooltip: getToolTipFromMap('cmAtbVrbPerTipPer',helpMap,'Tipo de Persona'),
		        dataIndex: 'cdFisJur',
		        sortable: true,
		        editor: comboTipoPersona,
		    //    renderer: renderComboEditor(comboTipoPersona)
		        
                renderer: function extGrid_renderer(val, cell, record, row, col, store) {
                    if (record != undefined) {

                         // Make sure we have a value
                          if (val == '') {
                           // No value so highlight this cell as in an error state

                             cell.css += 'x-form-invalid';
                             cell.attr = 'ext:qtip="Este campo es requerido"; ext:qclass="x-form-invalid-tip"';

                             //cell.attr = 'ext:qclass="x-form-invalid-tip"';
                          }else {
                           //Value is valid

                                 cell.css = '';
                                 cell.attr = 'ext:qtip=""';
                                }
                    }

                     var idx;
                     //value = eval(value);
                     for (var i=0; i < comboTipoPersona.store.data.items.length; i++) {

                     var registro = comboTipoPersona.store.getAt(i);
                           if (registro) {
                                if (registro.get(comboTipoPersona.valueField) == val) {
                                         // alert("matching: " + i + "\nvalue: " + value);
                                          idx = i;
                                  }//else alert("registro: " + registro.get(combo.valueField) + "\nvalue: " + value);
                           } 
                     }
                      //var idx = combo.store.find(combo.valueField, value, 0, true, true);
                      var rec = comboTipoPersona.store.getAt(idx);
                   //   console.log(comboTipoPersona.store);
                      return (rec == null)?val:rec.get(comboTipoPersona.displayField);
                      //return val;
              }
			},
	        {dataIndex: 'gbSwFormat',hidden :true}
			
	]);
	

	var grid2= new Ext.grid.EditorGridPanel({
        renderTo:'gridAtribuVblesPersona',
        id: 'grid2',
        store: storeGrilla,
        clicksToEdit: 1,
		border:true,
		title: '<span class="x-form-item" style="color:#98012e;font-size:14px;font:Arial,Helvetica, Sans-serif;">Configuraci&oacute;n de datos adicionales de personas</span>',
        cm: cm,
        loadMask: {msg: getLabelFromMap('400058',helpMap,'Cargando datos ...'), disabled: false},            	
    	width:630,
    	frame:true,
		height:590,
		sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
		collapsible: true,
		buttonAlign: 'center',
	   /*
		* plugins para validar la celda en el jsp se debe agregar: <script src="${ctx}/resources/scripts/ValidacionGrilla/Ext.ux.plugins.GridValidator.js" type="text/javascript"></script>
		*/
        plugins: [nmObligatorio,new Ext.ux.plugins.GridValidator()],		
		buttons: [
            		{
            			text: getLabelFromMap('btnAtbVrbPerAgr',helpMap,'Agregar'),
                 		tooltip:getToolTipFromMap('btnAtbVrbPerAgr',helpMap,'Agrega atributos de persona'),
                 		handler: function () {
                 			var _record = new recordGrilla ({
                 				cdAtribu:'',
                 				dsAtribu: '',
                 				swFormat: '',
                 				nmlmin: '',
                 				nmlmax: '',
                 				nmObligatorio: '',
                 				otTabVal: '',
                 				cdFisJur: '',
                 				cdCatego: ''
                 			});
                 				grid2.stopEditing();
								grid2.store.insert(0, _record);
								grid2.startEditing(0, 0);
								grid2.getSelectionModel().selectRow(0);
                 		}
            		},
            		{
            			text: getLabelFromMap('btnAtbVrbPerGuar',helpMap,'Guardar'),
                 		tooltip:getToolTipFromMap('btnAtbVrbPerGuar',helpMap,'Guarda atributos de persona'),
                 		handler: function () {
                 			if (grid2.store.getModifiedRecords()==0)
							{
					  	  		Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400011', helpMap,'Debe ingresar un registro para realizar esta operaci&oacute;n'));
	            	 		}else{
	            	 			verifico=grid2.store.getModifiedRecords();
	            	 			//grid2.stopEditing();
	            	 			bandEx=0;
  								for (var i=0; i<verifico.length; i++) {
  									if ((verifico[i].get('nmlmin'))>(verifico[i].get('nmlmax'))){
  										bandEx++;
  									}
  								}

  								if (bandEx!=0)
					 			{
						  			Ext.Msg.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400039', helpMap,'Existe un o mas valor m&iacute;nimo o m&aacute;ximo nulo'));
					 				bandEx=0;
					 			}else
					 			{
              	                  cols = grid2.colModel.getColumnCount(); // determino cantidad de columnas de la grilla
                                  rows = grid2.store.getCount();		  // determino cantidad de filas de la grilla
              	
              	                  var bandValid = 0; // bandera para saber si ya todas las celdas de mi grilla son validas
              	
                               	//recorro toda mi grilla preguntando si las celdas son validas
              	                  for (var i = 0; i < rows; i++) {
              		                 for (var j = 0; j < cols; j++) {
              		                  if(!grid2.isCellValid(j,i)){
              		   		              bandValid =+ 1;
              		                    }
              		                }
              	                  }
              	                  
              		                
                                   //aseguro de que los combos son validos
              		                
                                     verifico = grid2.store.getModifiedRecords();
                                     
                                     
                                     for (var i=0; i<verifico.length; i++) {
                                        if ((verifico[i].get('swFormat'))==''){
                                       	
                                            bandValid=+1;
                                          }
                                        
                                         if ((verifico[i].get('cdFisJur'))==''){
                                            bandValid=+1;
                                           }
                                        
                                      }   
                                      
                  /*
                   *             Ingresa al If si los combos o las celdas son invalidas 
                   */
              	                  if ((bandValid != 0)) {
              		                  Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400013', helpMap,'Complete la informaci&oacute;n requerida'));
              		                  
              	                   }else{
              		                      guardarAtributoVariablesPersona(); 
              	                        }
					 			}
							} 
						}
					},
            		{
            			text: getLabelFromMap('btnAtbVrbPerBor',helpMap,'Eliminar'),
                 		tooltip:getToolTipFromMap('btnAtbVrbPerBor',helpMap,'Elimina atributos variables de persona'),
                 		handler: function () {
                 				if (!grid2.getSelectionModel().getSelected()) {
										Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400011', helpMap,'Debe seleccionar un registro para realizar esta operaci&oacute;n'));	               
	   							}else{
	   								if (grid2.getSelectionModel().getSelected().get("cdAtribu")==""){
	   									grid2.store.remove(grid2.getSelectionModel().getSelected());
	   								}else{
	   	    		   					borrar(getSelectedRecord(grid2));
	   								}
                 				}
                 	
						}
					}            		
		],
		bbar: new Ext.PagingToolbar({
				pageSize: itemsPerPage,
				store: storeGrilla,
				displayInfo: true/*,
				displayMsg: getLabelFromMap('400009', helpMap,'Mostrando registros {0} - {1} of {2}'),
				emptyMsg: getLabelFromMap('400012', helpMap,'No hay registros para visualizar')*/
		    })
		});

	grid2.render();


//validador de minimos y maximos	
/*grid2.on('afteredit',function(e){
	if (e.field=='nmlmin'){
		if (e.record.get("nmlmax")!=""){
	   		if (e.record.get("nmlmax")<= e.value)
	   		{
	   			Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400039', helpMap,'El valor m&iacute;nimo no puede ser mayor al m&aacute;ximo'));
	   			e.record.set("nmlmin",e.record.get("nmlmax"));
	   		}
	  	}
		return false;
	}
});*/

 grid2.on("afteredit",function(e){
		   	if (e.record.data["nmlmin"]!="" && e.record.data["nmlmax"]!="") {
		   			if (eval(e.record.data["nmlmin"]) > eval(e.record.data["nmlmax"])){
		   			       Ext.Msg.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400039', helpMap,'El valor m&iacute;nimo no puede ser mayor al m&aacute;ximo'));
		   			       //grid2.getSelectionModel().getSelected().set('nmlmax', eval(e.record.data["nmlmin"])+1);
		   			}
		   		}
   });

 storeListaValores.load({
		callback: function () {
				storeFormatos.load({
				callback: function(){
					storeTipoPersona.load({
						callback:function(){
							storeCategoriaPersona.load({
								callback:function(){
									reloadGrid(grid2);
								}
							});
						}
					});				
					}
				});
			}
	});
	
 function guardarAtributoVariablesPersona () {
  var _params = "";
  
  var recs = grid2.store.getModifiedRecords();
  grid2.stopEditing();
  for (var i=0; i<recs.length; i++) {
   _params +=  "grillaListAtributosVblesPersona["+i+"].cdAtribu="+recs[i].get('cdAtribu')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].dsAtribu="+recs[i].get('dsAtribu')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].swFormat="+recs[i].get('swFormat')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].nmlmin="+recs[i].get('nmlmin')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].nmlmax="+recs[i].get('nmlmax')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].swObliga="+ recs[i].get('swObliga')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].otTabVal="+ recs[i].get('otTabVal')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].gbSwFormat="+ recs[i].get('swFormat')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].cdFisJur="+ recs[i].get('cdFisJur')+"&"+
      "&grillaListAtributosVblesPersona["+i+"].cdCatego="+ recs[i].get('cdCatego')+"&";
  }
  if (recs.length > 0) {

   var conn = new Ext.data.Connection ();
   conn.request({
     url: _ACTION_GUARDAR_ATRIBUTOS_VARIABLES_PERSONA,
     params: _params,
     method: 'POST',
     callback: function (options, success, response) {
         if (Ext.util.JSON.decode(response.responseText).success == false) {
          Ext.Msg.alert(getLabelFromMap('400010', helpMap,'Error'), Ext.util.JSON.decode(response.responseText).errorMessages[0]);
         } else {
          Ext.Msg.alert(getLabelFromMap('400000', helpMap,'Aviso'), Ext.util.JSON.decode(response.responseText).actionMessages[0], function(){
	          	grid2.store.commitChanges();
	          	reloadGrid(grid2)
          });
         }
       }
   });
  }
 } 	

function borrar(record) {
		if(record != "")
		{
			Ext.MessageBox.confirm(getLabelFromMap('400000', helpMap,'Aviso'), getLabelFromMap('400031', helpMap,'Se eliminar&aacute; el registro seleccionado'),function(btn)
			{
		        if (btn == "yes")
		        {
         			var _params = {
         						cdAtribu: record.get('cdAtribu')
         			};
         			execConnection(_ACTION_BORRAR_ATRIBUTOS_VARIABLES_PERSONA, _params, cbkConnection);
               }
			})
		}else{
				Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400011', helpMap,'Debe seleccionar un registro para realizar esta operaci&oacute;n'));
		}
};
function cbkConnection (_success, _message) {
	if (!_success) {
		Ext.Msg.alert(getLabelFromMap('400010', helpMap,'Error'), _message);
	}else {
		Ext.Msg.alert(getLabelFromMap('400000', helpMap,'Aviso'), _message, function(){reloadGrid(grid2);});
	}
}      

function reloadGrid(grid2){
	var _params = '';
	reloadComponentStore(grid2, _params, cbkReload);
};
function cbkReload(_r, _options, _success, _store) {
	if (!_success) {
		_store.removeAll();
		//Ext.Msg.alert(getLabelFromMap('400010', helpMap,'Error'), _store.reader.jsonData.actionErrors[0]);
		Ext.Msg.alert(getLabelFromMap('400000', helpMap,'Aviso'), _store.reader.jsonData.actionErrors[0]);
	}
};	

});


/*************  Definición de Plugin para Checkbox en grillas ********************************************/
		Ext.grid.CheckColumn = function(config){
		    Ext.apply(this, config);
		    if(!this.id){
		        this.id = Ext.id();
		    }
		    this.renderer = this.renderer.createDelegate(this);
		};

		Ext.grid.CheckColumn.prototype ={
		    init : function(grid){
		        this.grid = grid;
		        this.grid.on('render', function(){
		            var view = this.grid.getView();
		            view.mainBody.on('mousedown', this.onMouseDown, this);
		        }, this);
		    },
		
		    onMouseDown : function(e, t){
		        if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
		            e.stopEvent();
		            var index = this.grid.getView().findRowIndex(t);
		            var record = this.grid.store.getAt(index);
		            record.set(this.dataIndex, !record.data[this.dataIndex]);
		        }
		    },
			onClick: function (e, t) {
			},
		    renderer : function(v, p, record){
		        p.css += ' x-grid3-check-col-td'; 
		        return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
		    }
		};
/*************  Fin Definición de Plugin para Checkbox en grillas ********************************************/