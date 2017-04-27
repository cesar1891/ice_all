
//Mapa usado para contener los tooltips para cada control
var helpMap = new Map();

	//Map para mostrar tooltip en los íconos de ayuda al lado de cada control
	helpMap.put('icon-help-tooltip', {fieldLabel: 'Presione para ver la ayuda'});

	//Map para los elementos del form
	helpMap.put('codigoError', {tooltip: 'Mensaje asociado al cdError', ayuda: 'Este es un ejemplo de ayuda con mas caracteres para el\n campo cdError', fieldLabel: 'Codigo', hasHelp: true});
	helpMap.put('mensajeError', {tooltip: 'Mensaje asociado al mensajeError', ayuda: 'Este es un ejemplo de ayuda con mas caracteres para el\n campo mensajeError', fieldLabel: 'Mensaje'});

	//Maps para las columnas de la grilla
	helpMap.put('cdError', {tooltip: 'Mensaje asociado a la columna cdError', ayuda: 'Este es un ejemplo de ayuda con mas caracteres para el\n campo mensajeError', fieldLabel: 'C&oacute;digo'});
	helpMap.put('dsMensaje', {tooltip: 'Mensaje asociado a la columna dsMensaje', ayuda: 'Este es un ejemplo de ayuda con mas caracteres para el\n campo mensajeError', fieldLabel: 'Mensaje'});
	helpMap.put('cdTipo', {tooltip: 'Mensaje asociado a la columna cdTipo', ayuda: 'Este es un ejemplo de ayuda con mas caracteres para el\n campo mensajeError', fieldLabel: 'Tipo'});

	//Map para los botones del form
	helpMap.put('form.buttons.Buscar', {fieldLabel: 'Buscar', tooltip: 'Buscar...'});
	helpMap.put('form.buttons.Cancelar', {fieldLabel: 'Cancelar', tooltip: 'Cancelar...'});

	//Map para los elementos de la Bbar de la grilla
	helpMap.put('grilla.bbar.displayMsg', {fieldLabel: 'Mostrando registros {0} - {1} of {2}', tooltip: '...'});
	helpMap.put('grilla.bbar.emptyMsg', {fieldLabel: 'Sin datos', tooltip: '...'});
	helpMap.put('grilla.bbar.firstText', {fieldLabel: 'Primero', tooltip: '...'});
	helpMap.put('grilla.bbar.lastText', {fieldLabel: 'Ultimo', tooltip: '...'});
	helpMap.put('grilla.bbar.prevText', {fieldLabel: 'Previo', tooltip: '...'});
	helpMap.put('grilla.bbar.nextText', {fieldLabel: 'Siguiente', tooltip: '...'});
	helpMap.put('grilla.bbar.beforePageText', {fieldLabel: 'Pagina', tooltip: '...'});
	helpMap.put('grilla.bbar.afterPageText', {fieldLabel: 'de {0}', tooltip: '...'});
	helpMap.put('grilla.bbar.refreshText', {fieldLabel: 'Refrescar', tooltip: '...'});

	//Map para los botones de la grilla
	helpMap.put('grilla.buttons.Agregar', {fieldLabel: 'Agregar', tooltip: 'Agregar...'});
	helpMap.put('grilla.buttons.Editar', {fieldLabel: 'Editar', tooltip: 'Editar...'});
	helpMap.put('grilla.buttons.Regresar', {fieldLabel: 'Regresar', tooltip: 'Regresar...'});
	helpMap.put('grilla.buttons.Exportar', {fieldLabel: 'Exportar', tooltip: 'Exportar...'});

	//Map para los tabs
	helpMap.put('tabPanel.tab.1', {fieldLabel: 'Tabcito 1', tooltip: 'tip tab 1...'});
	helpMap.put('tabPanel.tab.2', {fieldLabel: 'Tabcito 2', tooltip: 'tip tab 2...'});


	//Map para form interno al TabPanel.Tab1
	helpMap.put('tabPanel.tab.1.Form.ComboBox', {fieldLabel: 'Funciona?', tooltip: 'Medir funcionalidad...'});
	helpMap.put('tabPanel.tab.1.Form.Button1', {fieldLabel: 'Enviar', tooltip: 'Enviar datos'});

	//Map para form interno al TabPanel.Tab2
	helpMap.put('tabPanel.tab.2.Form.ComboBox', {fieldLabel: 'Funciona?', tooltip: 'Medir funcionalidad...'});
	helpMap.put('tabPanel.tab.2.Form.Button1', {fieldLabel: 'Enviar', tooltip: 'Enviar datos'});

	helpMap.put('ComboBox3', {fieldLabel: 'Otro Combo', tooltip: 'Otro combito'});

	helpMap.put('dsdsdsds', {fieldLabel: 'Otro Txt', tooltip: 'Otro TextField'});

	helpMap.put('form.buttons.Probar', {fieldLabel: 'Otro Btn', tooltip: 'Otro Boton'});

Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.QuickTips.enable();
	Ext.form.Field.prototype.msgTarget = "side";

/********* Comienza la grilla ******************************/
	var cm = new Ext.grid.ColumnModel ([
				{
           			header: '',
					dataIndex: 'cdError',
					tooltip: '',
					width: 80,
           			menuDisabled : true,
					sortable: true
				},
				{
           			header: '',
					dataIndex: 'dsMensaje',
					width: 280,
           			menuDisabled : true,
					sortable: true
				},
				{
           			header: '',
					dataIndex: 'cdTipo',
					width: 60,
					sortable: true
				}
			]);

	var dsGrilla = new Ext.data.Store ({
			proxy: new Ext.data.HttpProxy ({url: ""}),
			reader: new Ext.data.JsonReader({
						root: 'listaMensajes',
						totalProperty: 'totalCount',
						successProperty: 'success'
					},
					[
						{name: 'cdError', type: 'string', mapping: 'msgId'},
						{name: 'dsMensaje', type: 'string', mapping: 'msgText'},
						{name: 'cdTipo', type: 'string', mapping: 'msgTitle'}
					]
					)
		});
	var grilla = new Ext.grid.GridPanel ({
				id: 'grilla',
				el: 'grillaResultados',
				cm: cm,
				store: dsGrilla,
				border: true,
				stripeRows: true,
				collapsible: true,
				frame: true,
				width: 500,
				height: 300,
				sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
				buttons: [
						{tooltip: 'grilla.buttons.Agregar', handler: function(){agregar()}},
						{
							tooltip: 'grilla.buttons.Editar', 
							handler: function(){
										if (getSelectedRecord(grilla) != null) {
											editar(getSelectedKey(grilla, "cdError"));
										}else {
											Ext.Msg.alert('Aviso', 'Debe seleccionar un mensaje para editar');
										}
									}
						},
						{tooltip: 'grilla.buttons.Regresar', handler: function(){}},
						{tooltip: 'grilla.buttons.Exportar', 
							handler: function(){
								}
						}
					],
				bbar: new Ext.PagingToolbar({
						pageSize:itemsPerPage,
						store: dsGrilla,
						displayInfo: true,
		                displayMsg: '',
		                emptyMsg: ""
				    })
		});
/********* Fin grilla **************************************/

/********* Comienzo un tab de prueba ***********************/

	//Datos de prueba para el combo
	var arrayComboData = [
		['S', 'Si'],
		['N', 'No']
	];
	var _comboStore = new Ext.data.SimpleStore({
		fields: [
			{name: 'codigo'},
			{name: 'descripcion'}
		]
	});
	
	//_comboStore.loadData(arrayComboData);

	var el_tab = new Ext.TabPanel({
		id: 'el_tab',
		activeTab: 0,
		deferredRender: false,
		height: 200,
		tooltip: '343',
		items: [
			{
				title: getLabelFromMap('tabPanel.tab.1', helpMap), tooltip: getToolTipFromMap('tabPanel.tab.1', helpMap), name: 'tabPanel.tab.1', id: 'tabPanel.tab.1', 
				items: [
					new Ext.FormPanel({
						title: 'Form dentro de un panel',
						renderTo: 'formInsideTab',
						name: 'tabPanel.tab.1.Form',
						id: 'tabPanel.tab.1.Form',
						//bodyStyle: {position: 'relative'},
						autoHeight: true,
						frame: true,
						items: [
								/*new Ext.form.TextField({
									name: 'dsdsdsds'
								})*/
								new Ext.form.ComboBox ({
										hasHelpIcon: false,
					                    labelWidth: 50,
					                    tpl: '<tpl for="."><div ext:qtip="{codigo}. {descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
					                    store: _comboStore,
					                    displayField:'descripcion',
					                    valueField:'codigo',
					                    hiddenName: 'fgSiNo1',
					                    typeAhead: true,
					                    mode: 'local',
					                    triggerAction: 'all',
					                    fieldLabel: getLabelFromMap('tabPanel.tab.1.Form.ComboBox', helpMap),
					                    forceSelection: true,
					                    width: 60,
					                    emptyText:'Seleccionar ...',
					                    selectOnFocus:true,
					                    labelSeparator:'',
					                    tooltip: getToolTipFromMap('tabPanel.tab.1.Form.ComboBox', helpMap),
					                    id: 'tabPanel.tab.1.Form.ComboBox',
					                    name: 'tabPanel.tab.1.Form.ComboBox',
					                    width: 120,
					                    listeners: {
					                    	focus: function () {
					                    		if (this.store.getCount() <= 0) {
					                    			this.store.loadData(arrayComboData);
				                    				//this.onTriggerClick();
					                    		}
					                    	}
					                    }
								})
						],
						buttons: [
							{text: getLabelFromMap('tabPanel.tab.1.Form.Button1', helpMap), tooltip: getToolTipFromMap('tabPanel.tab.1.Form.Button1', helpMap)}
						]
					})
				]
			},
			{
				title: getLabelFromMap('tabPanel.tab.2', helpMap), tooltip: getToolTipFromMap('tabPanel.tab.2', helpMap), id: 'tabPanel.tab.2', 
				items: [
					new Ext.FormPanel({
						title: 'Form dentro de un panel2',
						renderTo: 'form2InsideTab',
						name: 'tabPanel.tab.2.Form',
						id: 'tabPanel.tab.2.Form',
						//bodyStyle: {position: 'relative'},
						//height: 110,
						autoHeight: true,
						frame: true,
						items: [
								new Ext.form.TextField({
									name: 'dsdsdsds',
									hasHelpIcon: true,
									fieldLabel: getLabelFromMap('dsdsdsds', helpMap),
									tooltip: getToolTipFromMap('dsdsdsds', helpMap)
								}),
								new Ext.form.ComboBox ({
										hasHelpIcon: true,
					                    labelWidth: 50,
					                    tpl: '<tpl for="."><div ext:qtip="{codigo}. {descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
					                    store: _comboStore,
					                    displayField:'descripcion',
					                    valueField:'codigo',
					                    hiddenName: 'fgSiNo2',
					                    typeAhead: true,
					                    mode: 'local',
					                    triggerAction: 'all',
					                    fieldLabel: getLabelFromMap('tabPanel.tab.2.Form.ComboBox', helpMap),
					                    forceSelection: true,
					                    allowBlank: false,
					                    width: 60,
					                    emptyText:'Seleccionar ...',
					                    selectOnFocus:true,
					                    labelSeparator:'',
					                    tooltip: getToolTipFromMap('tabPanel.tab.2.Form.ComboBox', helpMap),
					                    id: 'tabPanel.tab.2.Form.ComboBox',
					                    name: 'tabPanel.tab.2.Form.ComboBox',
					                    width: 120
								})
						],
						buttons: [
							{
								 id: 'tabPanel.tab.2.Form.Button1', 
								 text: getLabelFromMap('tabPanel.tab.2.Form.Button1', helpMap), 
								 tooltip: getToolTipFromMap('tabPanel.tab.2.Form.Button1', helpMap)
							}
						]
					})
				]
			}
		]
	});

/********* Finaliza el tab de prueba ***********************/

/********* Comienza el form ********************************/
	var el_form = new Ext.FormPanel ({
			id: 'el_form',
			name: 'el_form',
			renderTo: 'formulario',
			title: '<span style="color:black;font-size:14px;">Pantalla de Prueba Tooltips</span>',
            labelWidth : 70,
            frame : true,
            bodyStyle : 'padding:5px 5px 0',
            width : 500,
            autoHeight: true,
            waitMsgTarget : true,
            layout: 'form',
            items: [
            		new Ext.form.NumberField({
            			fieldLabel: getLabelFromMap('codigoError', helpMap),
            			labelSeparator: '',
            			anchor: '40%',
            			maxlength: 6,
            			tooltip: getToolTipFromMap('codigoError', helpMap),
            			id: 'codigoError'
            		}),
            		new Ext.form.TextField({
            			fieldLabel: getLabelFromMap('mensajeError', helpMap),
            			anchor: '40%',
            			hasHelpIcon: true,
            			textHelpIcon: 'Presione para ver la ayuda...',
            			clickIconHelp: 'showHelp()',
            			tooltip: getToolTipFromMap('mensajeError', helpMap),
            			id: 'mensajeError',
            			name: 'mensajeError'
            		}),
            		new Ext.form.ComboBox ({
										hasHelpIcon: true,
										clickIconHelp: 'showHelp()',
					                    labelWidth: 50,
					                    tpl: '<tpl for="."><div ext:qtip="{codigo}. {descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
					                    store: _comboStore,
					                    displayField:'descripcion',
					                    valueField:'codigo',
					                    hiddenName: 'fgSiNo2',
					                    typeAhead: true,
					                    mode: 'local',
					                    triggerAction: 'all',
					                    fieldLabel: getLabelFromMap('ComboBox3', helpMap),
					                    forceSelection: true,
					                    allowBlank: true,
					                    emptyText:'Seleccionar ...',
					                    selectOnFocus:true,
					                    labelSeparator:'',
					                    tooltip: getToolTipFromMap('ComboBox3', helpMap),
					                    id: 'ComboBox3',
					                    name: 'ComboBox3',
					                    width: 220
					}),
            		
            		el_tab
            		],
            		buttonAlign: 'center',
            		buttons: [
            					{id: 'form.buttons.Buscar', 
            					tooltip: getToolTipFromMap('form.buttons.Buscar', helpMap), 
            					text: getLabelFromMap('form.buttons.Buscar', helpMap), handler: function () {
            													Ext.Msg.alert('', 'dss');
	            										}
            					},
            					{text: getLabelFromMap('form.buttons.Cancelar', helpMap), 
            					tooltip: getToolTipFromMap('form.buttons.Cancelar', helpMap), handler: function() {
            								
            								el_form.getForm().reset();
            							}
            					},
            					{id: 'form.buttons.Probar',
            					 text: getLabelFromMap('form.buttons.Probar', helpMap),
            					 tooltip: getToolTipFromMap('form.buttons.Probar', helpMap),
            					 handler: function() {
            					}}
            				]

	});

	/********* Fin del form ************************************/
	
	
	
	//////loadToolTips(Ext.getCmp('el_form'), helpMap);
	//////loadTabPanelToolTips(el_tab, helpMap);
	//////Ext.getCmp('form.buttons.Buscar').setToolTip('aeeeeerrrrr');
	//////Ext.getCmp('tabPanel.tab.2.Form.Button1').setToolTip('Botoncito que hace algo');
	//loadIconHelp(Ext.getCmp('tabPanel.tab.1.Form.ComboBox'), helpMap);
	//////el_tab.setActiveTab(0);
	//////loadToolTips(Ext.getCmp('tabPanel.tab.1.Form'), helpMap);
	//el_tab.setActiveTab(1);
	//loadToolTips(Ext.getCmp('tabPanel.tab.2.Form'), helpMap);
	//////el_tab.setActiveTab(0);
	//////loadGridToolTips(grilla);

	//el_form.render();
	loadGridToolTips(grilla);
	grilla.render();
			
	function renderToolTips (value, p, record) {
		p.attr = 'ext:qtip="Ejemplo tooltip"';
		return value;
	}

	function callbackGrabar (_success, _errorMessages) {
		if (_success) {
			Ext.Msg.alert('Aviso', _errorMessages);
		} else {
			Ext.Msg.alert('Error', _errorMessages);
		}
	}
	
});

function reloadGrid(){
	var params = {
					cdError: Ext.getCmp('el_form').form.findField('codigoError').getValue(),
					dsMensaje:  Ext.getCmp('el_form').form.findField('mensajeError').getValue()
				}
	reloadComponentStore(Ext.getCmp('grilla'), params, myCallback);
}
function myCallback(_rec, _opt, _success, _store) {
	if (!_success) {
		Ext.Msg.alert('Error', _store.reader.jsonData.actionErrors[0]);
		_store.removeAll();
	}
}
