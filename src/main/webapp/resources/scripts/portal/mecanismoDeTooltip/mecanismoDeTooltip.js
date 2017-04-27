
Ext.onReady(function(){  
Ext.QuickTips.init();
Ext.QuickTips.enable();
Ext.form.Field.prototype.msgTarget = "side";


    /********* Comienza el form ********************************/
    var el_form = new Ext.FormPanel ({
    		id: 'el_form',
            renderTo: 'formBusqueda',
            title: '<span style="color:black;font-size:12px;">'+getLabelFromMap('el_form', helpMap,'Tooltips')+'</span>',            
            url : _ACTION_BUSCAR_MECANISMO_DE_TOOLTIP,
            frame : true,
            bodyStyle : 'padding:5px 5px 0',
   	        bodyStyle:'background: white',
	        buttonAlign: 'center',
            labelAlign: 'right',
            waitMsgTarget : true,
			width: 500,
			layout:'form',
			items:[{
				layout:'form',
				border:false,
				items:[{
			        labelWidth: 100,
			        layout: 'form',
					//frame:true,
					bodyStyle:'background: white',
					items:[{
			   		    layout:'column',
					    border:false,
					    html:'<span class="x-form-item" style="color:#98012e;font-size:14px;font:Arial,Helvetica, Sans-serif;">B&uacute;squeda</span><br>',
					    columnWidth: 1,
					
						items:[{
				    		columnWidth:.9,
			           		layout: 'form',
			             	border: false,
			
			            items: [
			                        {xtype: 'textfield', 
			                        id: 'nom_Objeto', 
			                        name: 'nom_Objeto', 
    	                            fieldLabel: getLabelFromMap('nom_Objeto',helpMap,'Objeto'),
    	                            tooltip: getToolTipFromMap('nom_Objeto',helpMap,'Objeto definici&oacute;n de Paquetes'),
                                    hasHelpIcon:getHelpIconFromMap('nom_Objeto',helpMap),								 
                                    Ayuda: getHelpTextFromMap('nom_Objeto',helpMap)
			                        
			                        },
			                        {xtype: 'textfield', 
			                        id: 'nom_Pantalla', 
			                        name: 'nom_Pantalla', 
    	                            fieldLabel: getLabelFromMap('nom_Pantalla',helpMap,'Pantalla'),
    	                            tooltip: getToolTipFromMap('nom_Pantalla',helpMap,'Pantalla definici&oacute;n de Paquetes'),
                                    hasHelpIcon:getHelpIconFromMap('nom_Pantalla',helpMap),								 
                                    Ayuda: getHelpTextFromMap('nom_Pantalla',helpMap)
			                        
			                        },
			                        
			                        {xtype: 'hidden', id: 'idTitulo', name: 'idTitulo'},
			                        {xtype: 'hidden', id: 'Etiqueta', name: 'Etiqueta'},
			                        {xtype: 'hidden', id: 'dsTooltip', name: 'dsTooltip'},
			                        {xtype: 'hidden', id: 'langCode', value:IDIOMA_USR}
			                    ]
			                }]
			            }],
			            buttonAlign: 'center',
			            buttons: [
			                        {/*text: 'Buscar', */
			                            text:getLabelFromMap('consConfRenBtnAdd',helpMap,'Buscar'),
					                    tooltip: getToolTipFromMap('consConfRenBtnAdd',helpMap,'Buscar definici&oacute;n de paquetes'),
			                        
			                             handler: function ()
			                       
			                        									 {
			                                                                if (el_form.form.isValid()) {
			                                                                    if (grilla != null) {
			                                                                        reloadGrid();
			                                                                    }else {
			                                                                        createGrid();
			                                                                    }
			                                                                }
			                                                }
			                        },
			                        {/*text: 'Cancelar', */
			                         text:getLabelFromMap('consConfRenBtnCanc',helpMap,'Cancelar'),
					                  tooltip: getToolTipFromMap('consConfRenBtnCanc',helpMap,'Cancelar la b&uacute;squeda de definici&oacute;n de paquetes'),
			                        
			                        handler: function() {el_form.getForm().reset();}}
			                    ]
			          }]
			                    
			  }]
	                    
	    });
    /********* Fin del form ************************************/


	/********* Comienzo del grid *****************************/
		//Definición Column Model
	    var cm = new Ext.grid.ColumnModel([
	    		{
	    			dataIndex: 'langCode',
	    			hidden: true
	    		}, {
		           	header: getLabelFromMap('consConfRenCmEtiq',helpMap,'Etiqueta'),
                    tooltip: getToolTipFromMap('consConfRenCmEtiq',helpMap,'Etiqueta del Objeto de definici&oacute;n de paquetes'),
                    hasHelpIcon:getHelpIconFromMap('consConfRenCmEtiq',helpMap),								 
                    Ayuda: getHelpTextFromMap('consConfRenCmEtiq',helpMap),

		           	dataIndex: 'nbEtiqueta',
		           	sortable: true,
		           	width: 120
				},{
		           	header: getLabelFromMap('consConfRenCmObjeto',helpMap,'Objeto'),
                    tooltip: getToolTipFromMap('consConfRenCmObjeto',helpMap,'Objeto de definici&oacute;n de paquetes'),
                    hasHelpIcon:getHelpIconFromMap('consConfRenCmObjeto',helpMap),								 
                    Ayuda: getHelpTextFromMap('consConfRenCmObjeto',helpMap),
                     
		           	dataIndex: 'nbObjeto',
		           	sortable: true,
		           	width: 160
	    		}, {
		           	header: getLabelFromMap('consConfRenCmPant',helpMap,'Pantalla'),
                    tooltip: getToolTipFromMap('consConfRenCmPant',helpMap,'Pantalla de definici&oacute;n de paquetes'),
                    hasHelpIcon:getHelpIconFromMap('consConfRenCmPant',helpMap),								 
                    Ayuda: getHelpTextFromMap('consConfRenCmPant',helpMap),
                    
		           	dataIndex: 'dsTitulo',
		           	sortable: true,
		           	width: 220
	        	},{
	    			dataIndex: 'idTitulo',
	    			hidden: true
	        	}
				]);
	    
	    
		//Crea el Store
		function crearGridStore(){
		 			store = new Ext.data.Store({
		    			proxy: new Ext.data.HttpProxy({
						url: _ACTION_BUSCAR_MECANISMO_DE_TOOLTIP,
						waitMsg: getLabelFromMap('400017',helpMap,'Espere ...')
		                }),
		                reader: new Ext.data.JsonReader({
		            	root:'mecanismoTooltipList',
		            	totalProperty: 'totalCount',
			            successProperty : '@success'
			        },[
			        {name: 'langCode', type: 'string', mapping: 'lang_Code'},
			        {name: 'nbObjeto',  type: 'string',  mapping:'nbObjeto'},
			        {name: 'dsTitulo',  type: 'string',  mapping:'dsTitulo'},
			        {name: 'idTitulo',  type: 'string',  mapping:'idTitulo'},
			        {name: 'nbEtiqueta',  type: 'string',  mapping:'nbEtiqueta'},
			        {name: 'idObjeto',  type: 'string',  mapping:'idObjeto'}
					])
		        });
				return store;
		 	}
		//Fin Crea el Store


	var grilla;

	function createGrid(){
		grilla= new Ext.grid.GridPanel({
			id: 'grilla',
	        el:'gridMecanismoDeTooltip',
	        store:crearGridStore(),
	        title:'<span class="x-form-item" style="color:#98012e;font-size:14px;font:Arial,Helvetica, Sans-serif;">Listado</span>',
	        buttonAlign:'Center',
			border:true,
			loadMask: {msg: getLabelFromMap('400070',helpMap,'Cargando datos ...'), disabled: false},
	        cm: cm,
			buttons:[
		        		{
					     text:getLabelFromMap('MeDeTooltip',helpMap,'Agregar'),
					     tooltip: getToolTipFromMap('MeDeTooltip',helpMap,'Agrega un Mecanismo de Tooltip'),
		            		
		            		handler:function(){
			            			if(getSelectedRecord(grilla)!=null){
			                			agregar(getSelectedRecord(grilla));
			                		}
                 			       else{Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400011', helpMap,'Debe seleccionar un registro para realizar esta operaci&oacute;n'));
			                		   }
		                	}
		            	},
		            	{
					        text:getLabelFromMap('MeDeTooltipCan',helpMap,'Editar'),
					        tooltip: getToolTipFromMap('MeDeTooltipCan',helpMap,'Edita un Mecanismo de Tooltip'),
		            		handler:function(){
			            			if(getSelectedRecord(grilla)!=null){
			                			editar(getSelectedRecord(grilla));
			                		}
			                		else{
			                			Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400011', helpMap,'Debe seleccionar un registro para realizar esta operaci&oacute;n'));
			                		}
		                	}
		            	},
		            	{
					        text:getLabelFromMap('MeDeTooltipBorr',helpMap,'Eliminar'),
					        tooltip: getToolTipFromMap('MeDeTooltipBorr',helpMap,'Eliminar un Mecanismo de Tooltip'),
		            		
		                	handler:function(){
		                			if(getSelectedRecord(grilla) != null){
		                					borrar(getSelectedRecord(grilla));
		                			}
		                			else{ Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400067', helpMap,'Debe seleccionar un Mecanismo de Tooltip para eliminar'));		                			
		                			    }
		                		}
		            	},
		            	{
					        text:getLabelFromMap('MeDeTooltipCop',helpMap,'Copiar'),
					        tooltip: getToolTipFromMap('MeDeTooltipCop',helpMap,'Copiar Mecanismo de Tooltip'),
		                	handler:function(){
		                			if(getSelectedRecord(grilla)!=null){
		                					copiar(getSelectedRecord(grilla));
		                			}
		                			else{
			                			Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400011', helpMap,'Debe seleccionar un registro para realizar esta operaci&oacute;n'));
		                	            }
		                		}
		            	},
		            	{
					        text:getLabelFromMap('MeDeTooltipExp',helpMap,'Exportar'),
					        tooltip: getToolTipFromMap('MeDeTooltipExp',helpMap,'Exporta a otros formatos (PDF,DOC...)'),
		                	handler:function(){
                                  var url = _ACTION_EXPORTAR_MECANISMO_DE_TOOLTIP + '?nbObjeto=' + el_form.findById("nom_Objeto").getValue()+ '&dsTitulo=' + Ext.getCmp('el_form').form.findField('nom_Pantalla').getValue()+ '&langCode=' + el_form.findById("langCode").getValue();
                	 	          showExportDialog( url )
                               }
		            	}/*,
		            	{
		            		text:'Regresar',
		            		tooltip:'Regresa a la pantalla anterior'
		            	}*/
	            	],
	    	width:500,
	    	frame:true,
	        buttonAlign: 'center',
			height:590,
			sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
			stripeRows: true,
			collapsible: true,
			bbar: new Ext.PagingToolbar({
					pageSize:itemsPerPage,
					store: store,
					displayInfo: true,
	                displayMsg: 'Mostrando registros {0} - {1}  de {2}',
	                emptyMsg: "No hay registros para visualizar"
			    })
			});

	    grilla.render()
	}

	/********* Fin del grid **********************************/



	//Borra el objeto seleccionada
	function borrar(record){
        Ext.MessageBox.confirm(getLabelFromMap('400000', helpMap,'Aviso'), getLabelFromMap('400031', helpMap,'Se eliminar&aacute; el registro seleccionado'),function(btn){			
			
         		if (btn == "yes"){
         			var _params = {
         							idObjeto: record.get("idObjeto"), lang_Code: record.get('langCode')
         			};
         			execConnection(_ACTION_BORRAR_MECANISMO_DE_TOOLTIP,_params, cbkBorrar)
				}
		})

  	};

	function copiar(record){
        Ext.MessageBox.confirm(getLabelFromMap('400000', helpMap,'Aviso'), getLabelFromMap('400032', helpMap,'Se copiar&aacute; el registro seleccionado'),function(btn){			
			
			if (btn=="yes"){
				var _params={
					idObjeto: record.get("idObjeto"), lang_Code: record.get('langCode')
				};
			execConnection(_ACTION_COPIAR_MECANISMO_DE_TOOLTIP, _params, cbkCopiar)
			}
		})
	};  	
  	
  	
	function cbkBorrar (_success, _message) {
		if (!_success) {
			Ext.Msg.alert('Error', _message);
		}else {
			Ext.Msg.alert('Aviso', _message, function() {reloadGrid();})
		}
	}
	
	function cbkCopiar (_success, _message) {
		if (!_success) {
			Ext.Msg.alert('Error', _message);
		}else {
			Ext.Msg.alert('Aviso', _message, function() {reloadGrid();})
		}
	}
	
    //Muestra los componentes en pantalla
	el_form.render();
	createGrid()
	//Fin Muestra los componentes en pantalla    

});

function reloadGrid(){
	var _params = {
			nbObjeto: Ext.getCmp('el_form').form.findField('nom_Objeto').getValue(),
			langCode: Ext.getCmp('el_form').form.findField('langCode').getValue(),
			dsTitulo: Ext.getCmp('el_form').form.findField('nom_Pantalla').getValue()
			
	};
    reloadComponentStore(Ext.getCmp('grilla'), _params, cbkReload)
}
function cbkReload (_r, _o, _success, _store) {
	if (!_success) {
		//Ext.Msg.alert('Error', _store.reader.jsonData.actionErrors[0]);
		Ext.Msg.alert(getLabelFromMap('400000', helpMap,'Aviso'), _store.reader.jsonData.actionErrors[0]);
		_store.removeAll()
	}

}