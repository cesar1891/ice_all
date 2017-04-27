// Variable objeto ventana a ser generada y mostrada para la exportacion
var windowExport;

/*function exportButton(action){
	return function(btn, pressed){
        if(!windowExport){
            windowExport = new Ext.Window({title: getLabelFromMap('',helpMap,'Exportar información'),width:330,height:100,resizable: false,closeAction:'hide',plain: true,
            items: fmExport = new Ext.FormPanel({labelWidth: 120,onSubmit: Ext.emptyFn,submit: function() {
            // en la funcion de submit de ExtJS se sobreescribe para hacer un submit sin AJAX 
			this.getEl().dom.setAttribute("action",action);
			this.getEl().dom.submit();},
			defaultType: 'textfield',baseCls: null,bodyStyle:'padding: 5px 10px 0',items: [ 
			cbxFormat = new Ext.form.ComboBox({id:'formato',fieldLabel: getLabelFromMap('',helpMap,'Formato a exportar'),tooltip:getToolTipFromMap('',helpMap,'Formato con el que se exportara la información'),
			store: new Ext.data.SimpleStore({fields: ['cveFormat', 'format'],data : [
			// Elementos del combobox con los formatos a ser exportados
			['pdf', 'PDF'],['csv', 'CSV'],['xml', 'XML'],['txt', 'TXT'],['xls', 'XLS'],['rtf', 'RTF'],['jpg', 'JPG']]}),
			displayField:'format',typeAhead: true,mode: 'local',forceSelection: true,triggerAction: 'all',emptyText:getLabelFromMap('',helpMap,'Seleccione un Formato...'),selectOnFocus:true})],
			// botons con las acciones correspondientes a exportar, genera el submit y cancelar, en cualquiera de los dos casos se esconde la ventana
	        buttons: [btnExp = {text:getLabelFromMap('',helpMap,'Exportar'),handler: function(){windowExport.hide();if( cbxFormat.value ){fmExport.getForm().submit({waitMsg:getLabelFromMap('',helpMap,'Exportando...')});}}},{
	        text: getLabelFromMap('shwExpBtnCanc',helpMap,'Cancelar','Cancela la exportación'),handler: function(){windowExport.hide();}}]})})
        }
        windowExport.show(btn);
    }
}*/

/**********************mexico**************************/

function showExportDialog(action){
	if (Ext.isIE){
	//return function(btn, pressed){
        if(!windowExport){
            	windowExport = new Ext.Window({
            	title: 'Exportar informaci&oacute;n',
            	bodyStyle: {background: 'white'},
            	width:330,
            	height:100,
            	resizable: false,
            	closeAction:'hide',
            	plain: true,
            	modal: true, 
            	items: 
            	fmExport = new Ext.FormPanel({
            		labelWidth: 120,
            		onSubmit: Ext.emptyFn,
            		submit: function() {
            			// en la funcion de submit de ExtJS se sobreescribe para hacer un submit sin AJAX 
						this.getEl().dom.setAttribute("action",action);
						this.getEl().dom.target = '_windowExport2';
						this.getEl().dom.submit();
						},
					defaultType: 'textfield',
					baseCls: null,
					bodyStyle: {background: 'white', padding: '5px 10px 0'},
					items: [ 
						cbxFormat = new Ext.form.ComboBox({
							id:'formato',
							fieldLabel: 'Formato a exportar',
							store: new Ext.data.SimpleStore({
							fields: ['cveFormat', 'format'],
							data : [
							// Elementos del combobox con los formatos a ser exportados
							['pdf', 'PDF'],
							['csv', 'CSV'],
							['xml', 'XML'],
							//['dom', 'DOM'],
							['txt', 'TXT'],
							['xls', 'XLS'],
							['rtf', 'RTF'],
							['jpg', 'JPG']]
							}),
							displayField:'format',
							typeAhead: true,
							mode: 'local',
							forceSelection: true,
							triggerAction: 'all',
							emptyText:'Seleccione un Formato...',
							selectOnFocus:true})],
							// botons con las acciones correspondientes a exportar, genera el submit y cancelar, en cualquiera de los dos casos se esconde la ventana
	        				buttons: [btnExp = {
	        									text:'Exportar',
	        									handler: function(){
													
	        	
										        	var context = action.split('/')[1];
										        	var checkUrl = '/' + context + '/principal/checkSessionExport.action';
										        	execConnection(checkUrl,'', function (success, errorMessages){ 
										        	
										        		if(Ext.getCmp('formato').getValue()!=""){	        		
					        		                        if( cbxFormat.value ){
					        			                        windowExport.hide();
				                                                var opciones="toolbar=no,location=no, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=yes, width=700, height=500, top=100, left=100";
				                                                var paramOp = "";
				                                                if(action.indexOf("?")>=0) paramOp = "&&";
				                                                	else paramOp = "?";
				                                                
								       							window.open(action + paramOp + "formato=" + fmExport.form.findField('formato').getValue(), "_windowExport2", opciones);
																fmExport.getForm().submit({waitMsg:getLabelFromMap('400056',helpMap,'Exportando...')});
				        			                        }
			        		                            }
			        	                                else {
			        	                                 Ext.MessageBox.alert(getLabelFromMap('400000',helpMap,'Aviso'),getLabelFromMap('300008',helpMap,'Debe seleccionar un formato para la exportación'));
			        	                               }	        									
			        									
										        	});
	        	
	        									}
	        								},{
	        						text: 'Cancelar',handler: function(){
	        							windowExport.hide();
	        						}
	        					}]
	        			})
	        		});
        }
        windowExport.show();
    //}
	}else{showExportDialogFireFox(action);}
}



 OpcionesVentana = function(storeCondiciones, rec, row) {
    var cdMenu;

    if(row!=null){
    
        var record = storeCondiciones.getAt(row);
        cdMenu= record.get('cdMenu');
    
        storeCondiciones.on('load', function(){
                           //var record = storeCondiciones.getAt(row);
                           //Ext.getCmp('menuUsuarioForm').getForm().loadRecord(record);
                           
                           //Ext.getCmp('top').getForm().loadRecord(record);
        });
                               // storeCondiciones.load();

        /************************ 
        *carga expresion
        *************************/
        
            document.forms[0].action = '../opcmenuusuario/opcMenuUsuario.action?cdMenu=' + cdMenu;
            document.forms[0].submit(); 
            
    }
            
};

/*****************mexico***********************/







function showExportDialogFireFox(action){
        if(!windowExport){
            windowExport = new Ext.Window({title: getLabelFromMap('138',helpMap,'Exportar información'),bodyStyle:'background: white', width:330,height:100,modal:true,resizable: false,closeAction:'hide',plain: true,
            items: fmExport = new Ext.FormPanel({id:action,labelWidth: 120,onSubmit: Ext.emptyFn,submit: function() {
            // en la funcion de submit de ExtJS se sobreescribe para hacer un submit sin AJAX 
            this.getEl().dom.removeAttribute("action");
            this.getEl().dom.setAttribute("action",action);
            this.getEl().dom.submit();},
			defaultType: 'textfield',baseCls: null,bodyStyle:'padding: 5px 10px 0',items: [ 
			cbxFormat = new Ext.form.ComboBox({id:'formato',fieldLabel: getLabelFromMap('shwExpCmbFormato',helpMap,'Formato a exportar'),tooltip:getToolTipFromMap('shwExpCmbFormato',helpMap,'Formato con el que se realizará la información'),
			store: new Ext.data.SimpleStore({fields: ['cveFormat', 'format'],data : [
			// Elementos del combobox con los formatos a ser exportados
			['pdf', 'PDF'],['csv', 'CSV'],['xml', 'XML'],
			//['dom', 'DOM'],
			['txt', 'TXT'],['xls', 'XLS'],['rtf', 'RTF'],['jpg', 'JPG']]}),
			displayField:'format',typeAhead: true,mode: 'local',forceSelection: true,triggerAction: 'all',emptyText:getLabelFromMap('400055',helpMap,'Seleccione un Formato...'),selectOnFocus:true})],
			// botons con las acciones correspondientes a exportar, genera el submit y cancelar, en cualquiera de los dos casos se esconde la ventana
	        
	        buttons: [btnExp = {text:getLabelFromMap('shwExpTxtExp',helpMap,'Exportar'),
	        tooltip:getToolTipFromMap('shwExpTxtExp',helpMap,'Exporta la información al formato seleccionado'),
	        handler: function(){
	        	
	        	var context = action.split('/')[1];
	        	var checkUrl = '/' + context + '/principal/checkSessionExport.action';
	        	execConnection(checkUrl,'', function (success, errorMessages){ 
	        	
	        		if(Ext.getCmp('formato').getValue()!=""){	        		
		        		if( cbxFormat.value ){
		        			fmExport.getForm().submit({waitMsg:getLabelFromMap('400056',helpMap,'Exportando...')});
	        			}
                    	windowExport.close();
                    	windowExport = null;
		        	}
		        	else {
		        		Ext.MessageBox.alert(getLabelFromMap('400000',helpMap,'Aviso'),getLabelFromMap('300008',helpMap,'Debe seleccionar un formato para la exportación'));
		        	}
		        	
	        	});
	        	
	        	
	        	}},{
	        text: getLabelFromMap('shwExpBtnCanc',helpMap,'Cancelar'),tooltip:getToolTipFromMap('shwExpBtnCanc',helpMap,'Cancela la exportación'),handler: function(){windowExport.hide();}}]})});
        }
	      windowExport.show();
}

/**
*  Ejecuta un request asincrónico, usando el método POST.
*	Cuando se devuelve el control al callback, se envían el valor
*	de success y errorMessages o actionMessages dependiendo del 
*	valor de success.
*
*	@params
*			url: la url del action a ejecutar
*			params: parámetros enviados al action. Puede ser en formato de lista (notación JSON) o una
*					cadena como en la barra de direcciones del browser.
*			callback: función ejecutada cuando el request termina.
*
*	@return
*
*/
function execConnection(_url, _params, _callback, _timeout) {
	if (_url == undefined || _url == "" || _url == null) return false;
	var _method = "POST";

	var conn = new Ext.data.Connection ();
	if (_timeout == null || _timeout == undefined) _timeout = 90000;
	conn.request ({
			url: _url,
			method: _method,
			timeout: _timeout,
			params: _params,
			callback: function (options, success, response) {
							try {
								var errorMessages = Ext.util.JSON.decode(response.responseText).errorMessages[0];
								var success = Ext.util.JSON.decode(response.responseText).success;
								var actionMessages = Ext.util.JSON.decode(response.responseText).actionMessages[0];
								if (success) {
									eval(_callback(success, actionMessages, response.responseText));
								}else {
									eval(_callback(success, errorMessages));
								}
							} catch (e) {
								//Sólo necesario en el caso de errores al establecer el action,
								//ó cuando se vence el timeout de la conexión y desde el action
								//no se obtuvo respuesta alguna
								eval(_callback(false, "Connection Problem!"));
							}
					}
	});
}

/**
*  Función para realizar la recarga del store de un componente (Grilla, form, etc).
*
*  @params
*		comp: componente que se va recargar.
*		params: parámetros que serán enviados al action.
*		callback: Opcional. Función llamada cuando termina la recarga. Si no se
*					especifica, se limpia el store asociado al componente. Si 
*					se especifica el callback, se le envían como entre otros
*					parámetros el success y el store.
*					
*
*  @return
*
*/
function reloadComponentStore (_comp, _params, _callback) {
	var _store;

	if (_comp == null || _comp == undefined) return;

	_store = _comp.store;	
	if (_store == null || _store == undefined) return;

	_store.baseParams = _store.baseParams || {};
	_store.baseParams = _params;
	_store.params = _store.params || {};
	_store.params = {start: 0, limit: itemsPerPage};
	_store.load ({
			callback: (_callback != undefined)?function(r, options, success) {eval(_callback(r, options, success, _store))}:_store.removeAll()
	});
}

/**
*	Función usada para mostrar la descripción de un combo en vez del código
*	en una grilla de tipo EditorGridPanel
*
*
*/
function renderComboEditor (combo) {
	return function (value) {
		var idx = combo.store.find(combo.valueField, value);
		var rec = combo.store.getAt(idx);
		return (rec == null)?value:rec.get(combo.displayField);
	};
}

/**
*	Función usada para devolver el valor de una celda de una grilla.
*	El valor se toma de la fila seleccionada.
*
*	@return 
*		El valor asociado a la celda, o un sring vacío.
*/
function getSelectedKey(_grid, _key){
	if (_grid == null || _grid == undefined) return "";
	if (_key == null || _key == undefined) return "";

     var m = _grid.getSelections();
     var jsonData = "";
     for (var i = 0, len = m.length;i < len; i++) {
       var ss = m[i].get(_key);
       if (i == 0) {
       jsonData = jsonData + ss;
       } else {
         jsonData = jsonData + "," + ss;
      }
     }
     return jsonData;
}

/**
*	Función usada para devolver la fila seleccionada de una grilla*
*
*	@return
*		La fila seleccionada o null si no hay fila seleccionada 
*/
function getSelectedRecord(_grid){
	if (_grid == null || _grid == undefined) return null;
     var m = _grid.getSelections();
     if (m.length == 1 ) {
        return m[0];
     }
}

/**
*	Asigna tooltips a los campos del form que recibe por parámetro. Los textos de los tooltips
*	son obtenidos a través del map.
*
*	@return
*
*/
function loadToolTips(_el_form, el_Map) {
	Ext.each(_el_form.form.items.items, function(campito) {
				if (campito.ownerCt && campito.ownerCt.name == _el_form.name) {
					if (campito.tooltip) {
						if (el_Map.get(campito.tooltip)) {
							var _label = Ext.DomQuery.select(String.format('label[for="{0}"]', campito.tooltip));
							if (_label) {
								_label[0].childNodes[0].nodeValue = el_Map.get(campito.tooltip).fieldLabel + ": ";
							}
							Ext.QuickTips.register({
								target: campito.getEl(),
								text: el_Map.get(campito.tooltip).tooltip
							});
							loadIconHelp(campito, el_Map);
						}
					}
				}
	});
	//return;
	if (_el_form.buttons) {
		Ext.each(_el_form.buttons, function(botoncito){
			if (botoncito.tooltip) {
				botoncito.setText(((el_Map.get(botoncito.tooltip))?el_Map.get(botoncito.tooltip).fieldLabel:botoncito.getText()));
				var ttip = ((el_Map.get(botoncito.tooltip))?el_Map.get(botoncito.tooltip).tooltip:botoncito.tooltip);
				//ttip = Ext.DomQuery.select(String.format(''));
				//botoncito.getEl().dom[botoncito.tooltipType] = ttip;
				//alert(botoncito.el.dom[botoncito.tooltipType]);
				//Ext.QuickTips.register(Ext.apply({target: botoncito.tooltip}, ttip));
				botoncito.getEl().dom[botoncito.qtip] = '<span ext:qtip="Puta Madre">Boton de Mierda</span>'
				/*Ext.QuickTips.register({
					target: botoncito,
					text: 'la puta madre!'
				});*/
			}
		});
	}	
}

/**
*	Asigna tooltips a cada Tab dentro de un tabPanel
*
*
*	@return
*/
function loadTabPanelToolTips (el_tabPanel, el_Map) {
	var i=0;
	Ext.each(el_tabPanel.items.items, function(_tab) {
		if (_tab.tooltip) {
			if (el_Map.get(_tab.tooltip)) {
				_tab.setTitle(el_Map.get(_tab.tooltip).fieldLabel);
				
				/*	
					Código extraído de la API de ExtJS.
					Ver el contructor Ext.TabPanel para mas detalles 
				*/
				var E = el_tabPanel.strip.dom.childNodes[i];
				Ext.fly(E).child("span.x-tab-strip-text", true).qtip = el_Map.get(_tab.tooltip).tooltip;
				i++;
			}
		}
	});
}
/**
*	Asigna tooltips y labels para todos los controles internos a una grilla
*
*
*	@return 
*/
function loadGridToolTips (grilla) {
	var _columnModel = grilla.getColumnModel()
	for (var i=0; i<_columnModel.getColumnCount(); i++) {
		var _dataIndex = _columnModel.getDataIndex(i);
		var tooltip = ((helpMap.get(_dataIndex))?helpMap.get(_dataIndex).tooltip:'');
		var fieldLabel = ((helpMap.get(_dataIndex))?helpMap.get(_dataIndex).fieldLabel:'');
		_columnModel.setColumnHeader(i, '<span ext:qtip="' + tooltip + '">' + fieldLabel + '</span>');
	}
	grilla.getBottomToolbar().displayMsg = ((helpMap.get(grilla.id + '.bbar.displayMsg'))?helpMap.get(grilla.id + '.bbar.displayMsg').fieldLabel:'');
	grilla.getBottomToolbar().emptyMsg = ((helpMap.get(grilla.id + '.bbar.emptyMsg'))?helpMap.get(grilla.id + '.bbar.emptyMsg').fieldLabel:'');
	grilla.getBottomToolbar().firstText = ((helpMap.get(grilla.id + '.bbar.firstText'))?helpMap.get(grilla.id + '.bbar.firstText').fieldLabel:'');
	grilla.getBottomToolbar().lastText = ((helpMap.get(grilla.id + '.bbar.lastText'))?helpMap.get(grilla.id + '.bbar.lastText').fieldLabel:'');
	grilla.getBottomToolbar().prevText = ((helpMap.get(grilla.id + '.bbar.prevText'))?helpMap.get(grilla.id + '.bbar.prevText').fieldLabel:'');
	grilla.getBottomToolbar().nextText = ((helpMap.get(grilla.id + '.bbar.nextText'))?helpMap.get(grilla.id + '.bbar.nextText').fieldLabel:'');
	grilla.getBottomToolbar().beforePageText = ((helpMap.get(grilla.id + '.bbar.beforePageText'))?helpMap.get(grilla.id + '.bbar.beforePageText').fieldLabel:'');
	grilla.getBottomToolbar().afterPageText = ((helpMap.get(grilla.id + '.bbar.afterPageText'))?helpMap.get(grilla.id + '.bbar.afterPageText').fieldLabel:'');
	grilla.getBottomToolbar().refreshText = ((helpMap.get(grilla.id + '.bbar.refreshText'))?helpMap.get(grilla.id + '.bbar.refreshText').fieldLabel:'');

	Ext.each(grilla.buttons, function(campito){
			if (campito.tooltip) {
				campito.text = ((helpMap.get(campito.tooltip))?helpMap.get(campito.tooltip).fieldLabel:campito.text),
				campito.tooltip = ((helpMap.get(campito.tooltip))?helpMap.get(campito.tooltip).tooltip:campito.tooltip);
			}
	});
}
/**
*	Asigna un ícono de ayuda a la derecha del campo del formulario.
*
*	@return
*
*/
function loadIconHelp(_campito, el_Map) {
	var _parent = _campito.el.findParent('.x-form-element', 5, true);
	
	_campito.el.addClass(_campito.el.invalidClass);
	if (!_campito.helpIcon) {
			_campito.helpIcon = _parent.createChild({
					cls: "x-help-field",
					onclick: 'showHelp()'
			});
	}
	var offsetImg = 2;
	if (_campito.getXType() == "combo" || _campito.getXType() == "datefield" || _campito.getXType() == "timefield") {
		offsetImg = 18
	}
	_campito.alignHelpIcon = function () {
		_campito.helpIcon.alignTo(_campito.el, "tl-tr", [offsetImg, 0]);
	};
	_campito.helpIcon.dom.qtip = ((el_Map.get('icon-help-tooltip'))?el_Map.get('icon-help-tooltip').fieldLabel:'');
	_campito.helpIcon.show();
	_campito.on('resize', _campito.alignHelpIcon, _campito);
	_campito.fireEvent('resize', _campito);
}

/**
*	Realiza la apertura de una pantalla con un texto de ayuda asociado a un
*	control de un formulario
*
*	@return 
*
*/
var wndHelp;
function showHelp () {
	if (wndHelp != null) return;
	wndHelp = new Ext.Window ({
		title: 'Ayuda en línea',
		modal: true,
		width: 200,
		height: 200,
		items: [
			new Ext.FormPanel({
				frame: true,
				height: 200,
				border: true,
				defaults: {labelWidth: 0},
				items: [
					new Ext.form.Hidden({
						value: ''
					})
				]
			})
		],
		buttons: [
			{text: 'Cerrar', handler: function(){wndHelp.close(); wndHelp = null;}}
		]
	});
	wndHelp.show();
}


/**
*	Muestra una pantalla de espera sobre algun contenedor específico.
*	Util cuando se quiere bloquear tabs que contienen a un form que
*	se debe cargar
*
*
*	@return
*/
var the_Mask = null;
function startMask(ctrlId, text) {
	if (the_Mask != null) {
		endMask();
	}
	the_Mask = new Ext.LoadMask(ctrlId, {msg: text, disabled: false});
	the_Mask.show();
}

/**
*	Oculta la pantalla de espera
*
*
*	@return 
*/
function endMask() {
	the_Mask.hide();
	the_Mask = null;
}

function getLabelFromMap(_ctrlId, _Map, _defaultLabel) {
	if (_Map == null || _Map == undefined) return _defaultLabel; 
	if (_Map.get(_ctrlId) && _Map.get(_ctrlId).fieldLabel && _Map.get(_ctrlId).fieldLabel != "") {
		return _Map.get(_ctrlId).fieldLabel
	}
	return _defaultLabel;	
}

function getToolTipFromMap(_ctrlId, _Map, _defaultTooltip) {
	if (_Map == null || _Map == undefined) return _defaultTooltip; 
	if (_Map.get(_ctrlId) && _Map.get(_ctrlId).tooltip && _Map.get(_ctrlId).tooltip != "") {
		return _Map.get(_ctrlId).tooltip;
	}
	return 	_defaultTooltip;
}

function getMsgBlankTextFromMap(_ctrlId, _Map, _defaultBlankText) {
	if (_Map == null || _Map == undefined) return _defaultBlankText; 
	if (_Map.get(_ctrlId) && _Map.get(_ctrlId).blankText && _Map.get(_ctrlId).blankText != "") {
		return _Map.get(_ctrlId).blankText;
	}
	return 	_defaultBlankText;
}

function getHelpTextFromMap(_ctrlId, _Map, _defaultText) {
	if (_Map == null || _Map == undefined) return _defaultText; 
	if (_Map.get(_ctrlId) && _Map.get(_ctrlId).ayuda && _Map.get(_ctrlId).ayuda != "") {
		return _Map.get(_ctrlId).ayuda;
	}
	return 	_defaultText;
}

function getHelpIconFromMap(_ctrlId, _Map) {
	var _return = false;
	if (_Map == null || _Map == undefined) return _return; 
	
		if (_Map.get(_ctrlId)) {
			_return = _Map.get(_ctrlId).hasHelp;
		}
	return _return;
}

function exportButton(action){
	return function(btn, pressed){
        if(!windowExport){
            	windowExport = new Ext.Window({
            	title: 'Exportar información',
            	width:330,
            	height:100,
            	resizable: false,
            	closeAction:'hide',
            	plain: true,
            	items: 
            	fmExport = new Ext.FormPanel({
            		labelWidth: 120,
            		onSubmit: Ext.emptyFn,
            		submit: function() {
            			// en la funcion de submit de ExtJS se sobreescribe para hacer un submit sin AJAX 
						this.getEl().dom.setAttribute("action",action);
						if(Ext.isIE){
							this.getEl().dom.target = '_windowExport';
						}
						this.getEl().dom.submit();
						},
					defaultType: 'textfield',
					baseCls: null,
					bodyStyle:'padding: 5px 10px 0',
					items: [ 
						cbxFormat = new Ext.form.ComboBox({
							id:'formato',
							fieldLabel: 'Formato a exportar',
							store: new Ext.data.SimpleStore({
							fields: ['cveFormat', 'format'],
							data : [
							// Elementos del combobox con los formatos a ser exportados
							['pdf', 'PDF'],
							['csv', 'CSV'],
							['xml', 'XML'],
							//['dom', 'DOM'],
							['txt', 'TXT'],
							['xls', 'XLS'],
							['rtf', 'RTF'],
							['jpg', 'JPG']]
							}),
							displayField:'format',
							typeAhead: true,
							mode: 'local',
							forceSelection: true,
							triggerAction: 'all',
							emptyText:'Seleccione un Formato...',
							selectOnFocus:true})],
							// botons con las acciones correspondientes a exportar, genera el submit y cancelar, en cualquiera de los dos casos se esconde la ventana
	        				buttons: [btnExp = {
	        									text:'Exportar',
	        									handler: function(){
	        										
	        										var context = action.split('/')[1];
										        	var checkUrl = '/' + context + '/principal/checkSessionExport.action';
										        	execConnection(checkUrl,'', function (success, errorMessages){ 
										        	
										        		if(cbxFormat.getValue()!=''){
		        										windowExport.hide();
		        										if(Ext.isIE){
		        											var opciones="toolbar=no,location=no, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=yes, width=700, height=500, top=100, left=100";
		        											var paramOp = "";
				                                            if(action.indexOf("?")>=0) paramOp = "&&";
				                                                else paramOp = "?";
				                                                
	        												window.open(action + paramOp + "formato=" + fmExport.form.findField('formato').getValue(), "_windowExport", opciones);
		        										}
		        										if( cbxFormat.value ){
		        											fmExport.getForm().submit(
		        											{waitMsg:'Exportando...'});
		        										}
														}else{
		        											Ext.MessageBox.alert('Aviso', 'Debe seleccionar un formato para la exportación');	        										
		        										}
											        	
										        	});
										        	
	        										
	        									}
	        								},{
	        						text: 'Cancel',handler: function(){
	        							windowExport.hide();
	        						}
	        					}]
	        			})
	        		});
        }windowExport.show(btn);
    };
}

OpcionesVentana = function(storeCondiciones, rec, row) {
    var cdMenu;

    if(row!=null){
    
        var record = storeCondiciones.getAt(row);
        cdMenu= record.get('cdMenu');
    
        storeCondiciones.on('load', function(){
                           //var record = storeCondiciones.getAt(row);
                           //Ext.getCmp('menuUsuarioForm').getForm().loadRecord(record);
                           
                           //Ext.getCmp('top').getForm().loadRecord(record);
        });
                               // storeCondiciones.load();

        /************************ 
        *carga expresion
        *************************/
        
            document.forms[0].action = '../opcmenuusuario/opcMenuUsuario.action?cdMenu=' + cdMenu;
            document.forms[0].submit(); 
            
    }
            
};

/**
*    function trim();
*
*	Función usada para devolver el valor de un String cuando solo está lleno de espacios en blancos.
*	El valor se toma de la variable la cual se quiere verificar.
*
*	@return
*		La cadena con longitud '0' si la misma era solo blancos,
*
*       de lo contrario retorna la misma cadena.
*/

function trim(cadena)
{
    var cadenaAux = cadena;
    var i;
    var iniCad = 1;
    var finCad = cadenaAux.length;

    for (i=1; i <= cadenaAux.length; i++)
    {
        if (cadenaAux.charAt(i) != ' ')
        {
            iniCad = i;
            break;
        }
    }

    for (i = cadenaAux.length; i >= 1; i--)
    {
        if (cadenaAux.charAt(i) != ' ')
        {
            finCad = i;
            break;
        }
    }

    cadenaAux = cadenaAux.substring(iniCad, finCad);

    return cadenaAux;
}
