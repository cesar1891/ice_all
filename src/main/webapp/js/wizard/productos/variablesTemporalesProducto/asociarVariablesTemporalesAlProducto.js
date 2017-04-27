Ext.onReady(function() {
			Ext.form.Field.prototype.msgTarget = "side";
			var storeComboVariables;
			var grid2;
			url4vp = "librerias/ListaCatalogoVariablesProducto.action";
			storeComboVariables = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : url4vp
				}),
				reader : new Ext.data.JsonReader({
					root : 'listaReglaNegocioVariables',
					totalProperty : 'totalCount',
					id : 'n'
				}, [ {
					name : 'nombreCabecera',
					type : 'string',
					mapping : 'nombre'
				}, {
					name : 'descripcionCabecera',
					type : 'string',
					mapping : 'descripcion'
				}, {
					name : 'codigoExpresion',
					type : 'string',
					mapping : 'codigoExpresion'
				} ]),
				remoteSort : true
			});
			storeComboVariables.setDefaultSort('descripcionCabecera', 'desc');
			var codigoVariableTemporalProducto = new Ext.form.ComboBox(
					{
						id : 'id-catalogo-variables-temporales-producto',
						tpl : '<tpl for="."><div ext:qtip="{nombreCabecera}" class="x-combo-list-item">{descripcionCabecera}</div></tpl>',
						store : storeComboVariables,
						displayField : 'descripcionCabecera',
						valueField : 'nombreCabecera',
						typeAhead : true,
						mode : 'local',
						triggerAction : 'all',
						emptyText : 'Seleccione variable temporal',
						selectOnFocus : true,
						fieldLabel : 'Variable Temporal',
						listWidth : '210',
						blankText : 'Variable temporal requerida',
						name : "descripcionVariableProducto",
						width : 160,
						selectFirst : function() {
							this.focusAndSelect(this.store.data.first());
						},
						focusAndSelect : function(record) {
							var index = typeof record === 'number' ? record
									: this.store.indexOf(record);
							this.select(index, this.isExpanded());
							this.onSelect(this.store.getAt(record), index, this
									.isExpanded());
						},
						onSelect : function(record, index, skipCollapse) {
							if (this.fireEvent('beforeselect', this, record,
									index) !== false) {
								this.setValue(record.data[this.valueField
										|| this.displayField]);
								if (!skipCollapse) {
									this.collapse();
								}
								this.lastSelectedIndex = index + 1;
								this.fireEvent('select', this, record, index);
							}
							var valor = record.get('nombreCabecera');
							Ext.getCmp(
									'hidden-combo-variable-temporal-producto')
									.setValue(valor);
						}
					});
			var afuera;
			var temporal = -1;
			var storeVariablesProducto;
			url4vap = "librerias/ListaVariablesAsociadasAlProducto.action";
			storeVariablesProducto = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : url4vap
				}),
				reader : new Ext.data.JsonReader({
					root : 'listaReglaNegocioVariablesProducto',
					totalProperty : 'totalCount',
					id : 'a'
				}, [ {
					name : 'nombreCabecera',
					type : 'string',
					mapping : 'nombre'
				}, {
					name : 'descripcionCabecera',
					type : 'string',
					mapping : 'descripcion'
				}, {
					name : 'codigoExpresion',
					type : 'string',
					mapping : 'codigoExpresion'
				} ]),
				baseParams:{limit:'-1'},
				remoteSort : true
			});
			storeVariablesProducto
					.setDefaultSort('descripcionCabecera', 'desc');
			function toggleDetails(btn, pressed) {
				var view = grid.getView();
				view.showPreview = pressed;
				view.refresh();
			}
			var cmVariables = new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(), {
						header : "Nombre",
						dataIndex : 'nombreCabecera',
						width : 250,
						sortable : true,
						id : 'nombreCabecera'
					}, {
						header : "Descripci�n",
						dataIndex : 'descripcionCabecera',
						width : 250,
						sortable : true
					},{
						header: 'codigoExpresion',
						dataIndex : 'codigoExpresion',
						hidden: true
					} ]);
			var grid2 = new Ext.grid.GridPanel(
					{
						store : storeVariablesProducto,
						id : 'grid-variables-temporales-producto',
						border : true,
						cm : cmVariables,
						sm : new Ext.grid.RowSelectionModel(
								{
									singleSelect : true
								}),
						tbar : [
								{
									text : 'Agregar',
									tooltip : 'Agregar variable temporal',
									iconCls : 'add',
									handler : function() {
										if (Ext
												.getCmp(
														'id-catalogo-variables-temporales-producto')
												.isDirty()) {
											var valor = Ext
													.getCmp(
															'hidden-combo-variable-temporal-producto')
													.getValue();
											Ext
													.getCmp(
															'variables-producto-form')
													.getForm()
													.submit(
															{
																url : 'librerias/AsociarVariableAlProducto.action',
																success : function(
																		a, t) {
																	storeVariablesProducto
																			.load();
																	storeComboVariables
																			.load();
																			Ext.MessageBox.alert('Aviso', 'Variable asociada exitosamente.');
																}
															});
										} else {
											Ext.MessageBox.alert('Status',
													'seleccione una variable');
										}
									}
								}, '-', {
									text : 'Editar',
									tooltip : 'Editar variable temporal',
									iconCls : 'option',
									handler : function() {
										if (Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().hasSelection()) {
											var codigoExpresion = Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().getSelected().get('codigoExpresion');
											var nombreExp = Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().getSelected().get('nombreCabecera');
											var descExp = Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().getSelected().get('descripcionCabecera');
											
											var storeAux = new Ext.data.SimpleStore({
											    fields: [{name: 'nombreCabecera'},{name: 'descripcionCabecera'}],
											    data: [[nombreExp, descExp]]
											});
											
											ExpresionesVentana2(codigoExpresion, 'EXPRESION_VARIABLES_TEMPORALES', storeAux, '5', 0);
										} else {
											Ext.MessageBox.alert('Status','Seleccione un registro.');
										}
									}
								}, '-', {
									text : 'Eliminar',
									id : 'eliminar-variables-producto',
									tooltip : 'Eliminar variable temporal',
									iconCls : 'remove',
									handler : function() {
										if (Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().hasSelection()) {
											var codigoExpresion = Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().getSelected().get('codigoExpresion');
											var nombreExp = Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().getSelected().get('nombreCabecera');
											var descExp = Ext.getCmp('grid-variables-temporales-producto').getSelectionModel().getSelected().get('descripcionCabecera');
											
											//console.log("codigoExpresion:", codigoExpresion, "nombreExp",nombreExp,"descExp",descExp);
											
											var conn2 = new Ext.data.Connection();
											var params2 = {
													codigoVariableProducto: nombreExp
											};
						            		conn2.request ({
													url: 'librerias/DesasociarVariableDelProducto.action',
													method: 'POST',
													successProperty : '@success',
													params : params2,
						    	       				callback: function (options, success, response) {
			                       						if (success) {
				        	                  				Ext.MessageBox.alert('Aviso', 'Variable borrada exitosamente.');
				        	                  				storeVariablesProducto.load();
				        	                  				storeComboVariables.load();
		            	           						} else {
				                       							Ext.MessageBox.alert('Error', 'Variable no borrada.');        	                							
		    	                	      				}
		       		               					},
							            	   		waitTitle:'Espere',
							    					waitMsg:'Procesando...'
				    		       			});
											
										} else {
											Ext.MessageBox.alert('Status','Seleccione un registro.');
										}
									}
								} ],
						width : 600,
						height : 290,
						frame : true,
						bodyStyle : 'padding:5px',
						viewConfig : {
							autoFill : true,
							forceFit : true
						}/*,
						bbar : new Ext.PagingToolbar({
							pageSize : 25,
							store : storeVariablesProducto,
							displayInfo : true,
							displayMsg : 'Displaying rows {0} - {1} of {1}',
							emptyMsg : "No rows to display"
						})*/
					});
			
			var FiltroLista = new Ext.form.TextField({
				fieldLabel: 'Filtrar',
				id: 'filtroListaVarTempProdId',
				width:150
			});
			
			var variablesProductoForm = new Ext.form.FormPanel(
					{
						frame : false,
						title : 'Variables Temporales por Producto',
						url : 'librerias/GuardarVariablesTemporalesDelProducto.action',
						id : 'variables-producto-form',
						bodyStyle : 'padding:10px',
						width : 620,
						items : [
								{
									layout : 'form',
									border : false,
									items : [
											{
												layout : 'form',
												border : false
											},
											{
												layout : 'column',
												border : false,
												items : [
														{
															columnWidth : .5,
															layout : 'form',
															labelAlign : 'right',
															labelWidth : 100,
															width : 300,
															border : false,
															items : [
																	{
																		xtype : 'hidden',
																		id : 'hidden-combo-variable-temporal-producto',
																		name : 'codigoVariableProducto'
																	},
																	codigoVariableTemporalProducto ]
														},
														{
															columnWidth : .3,
															layout : 'form',
															border : false,
															width : 165,
															labelAlign : 'right',
															items : [ {
																xtype : 'button',
																hidden:true,
																text : 'Agregar al Cat\u00E1logo',
																name : 'AgregarAlCatalogo',
																buttonAlign : "left",
																handler : function() {
																	ExpresionesVentanaVariablesTemporales(storeComboVariables);
																}
															} ]
														} ]
											},FiltroLista, grid2 ]
								}],
						buttons : [
								{
									text : 'Guardar',
									hidden:true,
									handler : function() {
										variablesProductoForm.form
												.submit({
													waitTitle : 'Espere',
													waitMsg : 'Procesando...',
													failure : function(form,
															action) {
														Ext.MessageBox
																.alert(
																		'Status',
																		'no se pudo asociar la variable');
													},
													success : function(form,
															action) {
														Ext.MessageBox
																.alert(
																		'Status',
																		'Las variables se asociaron correctamente');
														form.reset();
														storeVariablesProducto
																.load();
													}
												});
									}
								},
								{
									text : 'Cancelar',
									hidden:true,
									handler : function() {
										variablesProductoForm.form
												.load({
													url : 'RemueveCosasDeSessionEnLibrerias.action',
													failure : function() {
														storeVariablesProducto
																.load();
													}
												});
									}
								} ]
					});
			variablesProductoForm.render('centerVariablesTemporalesProducto');
			
			$('#filtroListaVarTempProdId').on('keyup',function(){
				storeVariablesProducto.filterBy(function(record, id){
					
					var key = record.get('nombreCabecera').toUpperCase().replace(/ /g,'');
					var value = record.get('descripcionCabecera').toUpperCase().replace(/ /g,'');
					
					var filtro = FiltroLista.getValue().toUpperCase().replace(/ /g,'');
		    		var posK = key.lastIndexOf(filtro);
		    		var posV = value.lastIndexOf(filtro);
		    		
		    		if(posK > -1 || posV > -1){
		    			return true;
		    		}else{
		    			return false;
		    		}
				});
			});
		});