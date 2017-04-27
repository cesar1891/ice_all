<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _CONTEXT = '${ctx}';

var _parametros = <s:property value='%{convertToJSON("params")}' escapeHtml="false" />;
debug('parametrosTApoyo:',_parametros);
var editMode = (_parametros.edit == 'S');

var _NMTABLA =  _parametros.nmtabla;

var _URL_GuardaTablaApoyo = '<s:url namespace="/catalogos" action="guardaTablaApoyo" />';
var _URL_CatalogoClnatura = '<s:url namespace="/tablaCincoClaves" action="CatalogoClnatura" />';
var _UrlCargaClavesTabla  = '<s:url namespace="/catalogos" action="obtieneClavesTablaApoyo" />';
var _UrlCargaAtributosTabla  = '<s:url namespace="/catalogos" action="obtieneAtributosTablaApoyo" />';

var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';

/**
 * Para combos de dependencias para Una Clave: 
 */
var _URL_Catalogo1     = '<s:url namespace="/atributosVariables" action="CargaCatalogos" />';
var _URL_Catalogo2     = '<s:url namespace="/atributosVariables" action="CargaCatalogos2" />';
var _URL_CatalogoClave = '<s:url namespace="/atributosVariables" action="CargaClaveDependcia" />';

var _CAT_FORMATOS  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TFORMATOS"/>';

Ext.onReady(function() {
	
		var formatosStore = Ext.create('Ext.data.Store', {
		                    model     : 'Generic',
		                    proxy     : {
		                        type        : 'ajax'
		                        ,url        : _URL_CARGA_CATALOGO
		                        ,extraParams: {catalogo:_CAT_FORMATOS}
		                        ,reader     :
		                        {
		                            type  : 'json'
		                            ,root : 'lista'
		                        }
		                    }
		                });
		formatosStore.load();
	
		
	/**
	 * GRID DE CLAVES
	 */
		
		Ext.define('modeloClaves',{
	        extend  : 'Ext.data.Model'
	        ,fields :
	        [
	             'NUMCLAVE'
	            ,'DSCLAVE1'
	            ,'SWFORMA1'
	            ,'DSFORMA1'
	            ,'NMLMIN1'
	            ,'NMLMAX1'
	        ]
		});
		
		var clavesStore = Ext.create('Ext.data.Store',
			    {
					pageSize : 100
			        ,model   : 'modeloClaves'
			        ,proxy   :
			        {
			            type         : 'memory'
			            ,enablePaging : true
			            ,reader      : 'json'
			            ,data        : []
			        }
		});
			    
		var gridClaves = Ext.create('Ext.grid.Panel',
		    {
		    title    : 'Claves de la Tabla, Doble Click para editar la fila'
		    ,height  : 200
		    ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
		    {
		    	pluginId: 'clavesRowId',
		        clicksToEdit  : 2,
		        errorSummary : false,
		        listeners: {
		    		beforeedit: function(editor, context){
		    			/**
		    			 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    			 */
		    			if(!context.record.phantom){
		    				return false;
		    			}

		    			panelTablaApoyo.down('#botonGuardarId').disable();
		    		},
		    		edit: function(){
		    			panelTablaApoyo.down('#botonGuardarId').enable();
		    		},
		    		canceledit: function(){
		    			panelTablaApoyo.down('#botonGuardarId').enable();
		    		}
		    	}
		        
		    })
		    ,tbar     :
		        [
		            {
		                text     : 'Agregar'
		                ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
		                ,handler : function(){
		                	var tipoTabSel = panelTablaApoyo.getForm().findField('pi_clnatura').getValue();
		                	var clavesPermitidas = 0;
		                	
		                	if(!Ext.isEmpty(tipoTabSel)){
		                		if(tipoTabSel != 1){
			                		clavesPermitidas = 5;
			                	}else{
			                		clavesPermitidas = 1;
			                	}
		                	}
		                	
		                	
		                	if(clavesStore.getCount() >= clavesPermitidas){
		                		mensajeInfo('No se permiten mas claves para este tipo de tabla. Verifique la Naturaleza de la tabla.');
		                		return false;
		                	}
		                	clavesStore.insert(clavesStore.getCount(),new modeloClaves());
		                	gridClaves.getPlugin('clavesRowId').startEdit(clavesStore.getCount()-1,0);
		                }
		            },{
		                text     : 'Eliminar'
			                ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
			                ,handler : function(){
			                	if(gridClaves.getSelectionModel().hasSelection()){
									var record = gridClaves.getSelectionModel().getLastSelected();
				                	/**
					    			 *Para inhabilitar la eliminacion de rows que ya esten en la Base de Datos 
					    			 */
					    			if(!record.phantom){
					    				mensajeInfo('Esta clave ya no puede ser eliminada.');
					    				return false;
					    			}
			                	clavesStore.remove(record);			                		
			                	}else{
				            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
				            	}
			                }
			            }
		        ]
		    ,columns :
		    [
		    	{
		            header     : 'No. Clave'
		            ,dataIndex : 'NUMCLAVE'
		            ,hidden    : true
		            ,flex      : 1
		            /*,editor    :
		            {
		                xtype             : 'textfield'
		                ,allowBlank       : false
		            }*/
		        },{
		            header     : 'Clave'
		            ,dataIndex : 'DSCLAVE1'
		            ,flex      : 3
		            ,editor    :
		            {
		                xtype             : 'textfield'
		                ,allowBlank       : false
		            }
		        }
		        ,{
		            header     : 'Formato'
		            ,dataIndex : 'SWFORMA1'
		            ,flex      : 4
		            ,renderer  : function(valor){
		            	return rendererColumnasDinamico(valor,'SWFORMA1'); 
		            }
		            ,editor    :
		            {
		                xtype         : 'combobox',
		                allowBlank    : false,
		                name          : 'SWFORMA1',
		                valueField    : 'key',
		                displayField  : 'value',
		                forceSelection: true,
		                typeAhead     : true,
		                anyMatch      : true,
		                store         : formatosStore
		               
		            }
		        }
		        ,{
		            header     : 'Valor M&iacute;nimo'
		            ,dataIndex : 'NMLMIN1'
		            ,flex      : 3
		            ,editor    :
		            {
		                xtype             : 'numberfield'
		                ,allowBlank       : false
		                ,allowDecimals    : false
		                ,minValue: 0
		            }
		        },{
		            header     : 'Valor M&aacute;ximo'
		            ,dataIndex : 'NMLMAX1'
		            ,flex      : 3
		            ,editor    :
		            {
		                xtype             : 'numberfield'
		                ,allowBlank       : false
		                ,allowDecimals    : false
		                ,minValue: 0
		            }
		        }
		    ]
		    ,store : clavesStore
		});
		
		if(editMode){
			var paramsClavesT = {
				'params.pi_nmtabla' : _NMTABLA 
			}; 
			cargaStorePaginadoLocal(clavesStore, _UrlCargaClavesTabla, 'loadList', paramsClavesT, function (options, success, response){
	    		if(success){
	                var jsonResponse = Ext.decode(response.responseText);
	                
	                if(!jsonResponse.success) {
	                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
	                }
	            }else{
	                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
	            }
	    	}, gridClaves);
		}
		
		
	/**
	 * GRID DE ATRIBUTOS
	 */

		Ext.define('modeloAtributos',{
	        extend  : 'Ext.data.Model'
	        ,fields :
	        [
	             'CDATRIBU'
	            ,'DSATRIBU'
	            ,'SWFORMAT'
	            ,'DSFORMAT'
	            ,'NMLMIN'
	            ,'NMLMAX'
	        ]
		});
		
		var atributosStore = Ext.create('Ext.data.Store',
			    {
					pageSize : 100
			        ,model   : 'modeloAtributos'
			        ,proxy   :
			        {
			            type         : 'memory'
			            ,enablePaging : true
			            ,reader      : 'json'
			            ,data        : []
			        }
		});
			    
		var gridAtributos = Ext.create('Ext.grid.Panel',
		    {
		    title    : 'Atributos de la Tabla, Doble Click para editar la fila'
		    ,height  : 300
		    ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
		    {
		    	pluginId: 'atributosRowId',
		        clicksToEdit  : 2,
		        errorSummary : false,
		        listeners: {
		    		beforeedit: function(editor, context){
		    			/**
		    			 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    			 */
		    			if(!context.record.phantom){
		    				return false;
		    			}

		    			panelTablaApoyo.down('#botonGuardarId').disable();
		    		},
		    		edit: function(){
		    			panelTablaApoyo.down('#botonGuardarId').enable();
		    		},
		    		canceledit: function(){
		    			panelTablaApoyo.down('#botonGuardarId').enable();
		    		}
		    	}
		        
		    })
		    ,tbar     :
		        [
		            {
		                text     : 'Agregar'
		                ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
		                ,handler : function(){
		                	var tipoTabSel = panelTablaApoyo.getForm().findField('pi_clnatura').getValue();
		                	var atributosPermitidos = 0;
		                	
		                	if(!Ext.isEmpty(tipoTabSel)){
		                		if(tipoTabSel != 1){
			                		atributosPermitidos = 26;
			                	}else{
			                		atributosPermitidos = 1;
			                	}
		                	}
		                	
		                	
		                	if(atributosStore.getCount() >= atributosPermitidos){
		                		mensajeInfo('No se permiten mas claves para este tipo de tabla. Verifique la Naturaleza de la tabla.');
		                		return false;
		                	}
		                	
		                	atributosStore.insert(atributosStore.getCount(),new modeloAtributos());
		                	gridAtributos.getPlugin('atributosRowId').startEdit(atributosStore.getCount()-1,0);
		                }
		            },{
		                text     : 'Eliminar'
			                ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
			                ,handler : function(){
			                	if(gridAtributos.getSelectionModel().hasSelection()){
			                		var record = gridAtributos.getSelectionModel().getLastSelected();
				                	/**
					    			 *Para inhabilitar la eliminacion de rows que ya esten en la Base de Datos 
					    			 */
					    			if(!record.phantom){
					    				mensajeInfo('Este atributo ya no puede ser eliminado.');
					    				return false;
					    			}
				                	atributosStore.remove(record);
			                	}else{
				            		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
				            	}
			                }
			            }
		        ]
		    ,columns :
		    [
		    	{
		            header     : 'CdAtribu'
		            ,dataIndex : 'CDATRIBU'
		            ,hidden    : true
		            ,flex      : 1
		            /*,editor    :
		            {
		                xtype             : 'textfield'
		                ,allowBlank       : false
		            }*/
		        },{
		            header     : 'Atributo'
		            ,dataIndex : 'DSATRIBU'
		            ,flex      : 3
		            ,editor    :
		            {
		                xtype             : 'textfield'
		                ,allowBlank       : false
		            }
		        }
		        ,{
		            header     : 'Formato'
		            ,dataIndex : 'SWFORMAT'
		            ,flex      : 4
		            ,renderer  : function(valor){
		            	return rendererColumnasDinamico(valor,'SWFORMAT'); 
		            }
		            ,editor    :
		            {
		                xtype         : 'combobox',
		                allowBlank    : false,
		                name          : 'SWFORMAT',
		                valueField    : 'key',
		                displayField  : 'value',
		                forceSelection: true,
		                typeAhead     : true,
		                anyMatch      : true,
		                store         : formatosStore
		               
		            }
		        }
		        ,{
		            header     : 'Valor M&iacute;nimo'
		            ,dataIndex : 'NMLMIN'
		            ,flex      : 3
		            ,editor    :
		            {
		                xtype             : 'numberfield'
		                ,allowBlank       : false
		                ,allowDecimals    : false
		                ,minValue: 0
		            }
		        },{
		            header     : 'Valor M&aacute;ximo'
		            ,dataIndex : 'NMLMAX'
		            ,flex      : 3
		            ,editor    :
		            {
		                xtype             : 'numberfield'
		                ,allowBlank       : false
		                ,allowDecimals    : false
		                ,minValue: 0
		            }
		        }
		    ]
		    ,store : atributosStore
		});
		
		if(editMode){
			var paramsAtributosT = {
				'params.pi_nmtabla' : _NMTABLA 
			}; 
			cargaStorePaginadoLocal(atributosStore, _UrlCargaAtributosTabla, 'loadList', paramsAtributosT, function (options, success, response){
	    		if(success){
	                var jsonResponse = Ext.decode(response.responseText);
	                
	                if(!jsonResponse.success) {
	                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
	                }
	            }else{
	                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
	            }
	    	}, gridAtributos);
		}
		

		
		
	/**
	 * PANEL PRINCIPAL QUE CONTIENE LOS DOS GRIDS DE CLAVES Y DE ATRIBUTOS
	 */

	var panelTablaApoyo = Ext.create('Ext.form.Panel', {
    	url: _URL_GuardaTablaApoyo,
		border: false,
		defaults: {
			style : 'margin:5px;'
		},
	    renderTo : 'maindivAgrTbl',
	    items    : [
					{
						xtype      : 'hidden',
						name       : 'pi_tip_tran',
						value      : editMode? '2' : '1'  // 1 INSERT, 2 UPDATE
					},{
			            layout: 'column',
            			columns: 3,
            			border: false,
            			defaults: {
							style : 'margin:5px;'
						},
            			items: [{
									xtype      : 'textfield',
									name       : 'pi_nmtabla',
									fieldLabel : 'N&uacute;mero de Tabla',
									value      : _parametros.nmtabla,
									readOnly      : true
								},{
						        	xtype      : 'textfield',
						    		name       : 'pi_cdtabla',
						    		fieldLabel : 'C&oacute;digo de la Tabla',
						    		regex      : /^[a-zA-Z0-9]+$/,
									regexText  : 'El nombre solo puede contener letras y n&uacute;meros',
									maxLength  : 30,
									maxLengthText: 'Longitud m&aacute;xima de 30 caracteres',
						    		value      : _parametros.cdtabla,
						    		readOnly      : editMode,
						    		allowBlank    : false
						        },{
			                        xtype         : 'combo',
			                        name          : 'pi_clnatura',
			                        fieldLabel    : 'Naturaleza',
			                        readOnly      : editMode,
						    		allowBlank    : false,
			                        valueField    : 'key',
			                        displayField  : 'value',
			                        forceSelection: true,
			                        queryMode     :'local',
			                        store         : Ext.create('Ext.data.Store', {
				                        model     : 'Generic',
				                        autoLoad  : true,
				                        proxy     : {
				                            type        : 'ajax'
				                            ,url        : _URL_CatalogoClnatura
				                            ,reader     :
				                            {
				                                type  : 'json'
				                                ,root : 'listaClnatura'
				                            }
				                        },
				                        listeners: {
				                        	load: function (){
				                        		if(editMode){
				                        			panelTablaApoyo.getForm().findField('pi_clnatura').setValue(_parametros.cdnatura);
				                        			
				                        		   var fieldUnCla = panelTablaApoyo.down('#fieldUnaClave');
					                    		   
					                    		   if(1 != _parametros.cdnatura){
					                    		   		fieldUnCla.hide();
					                    		   }else {
					                    			   	fieldUnCla.show();
					                    		   }
				                        		}
				                        	}
				                        }
			                        }),
			                       listeners: {
			                    	   select: function ( combo, records, eOpts ){
			                    		   var cdnatura = records[0].get('key');
			                    		   var fieldUnCla = panelTablaApoyo.down('#fieldUnaClave');
			                    		   var combTipAcc = panelTablaApoyo.down('#tipoAccId');
			                    		   
			                    		   combTipAcc.select('U');
			                    		   
			                    		   if(1 != cdnatura){
			                    		   		fieldUnCla.hide();
			                    		   		combTipAcc.setReadOnly(false);
			                    		   }else {
			                    			   	fieldUnCla.show();
			                    			   	combTipAcc.setReadOnly(true);
			                    		   }
			                    	   }
			                       }
						        },{
						        	xtype      : 'textfield',
						    		name       : 'pi_dstabla',
						    		allowBlank : false,
						    		fieldLabel : 'Descripci&oacute;n de la Tabla',
						    		width      : 545,
						    		labelWidth : 130,
						    		maxLength  : 60,
									maxLengthText: 'Longitud m&aacute;xima de 60 caracteres',
						    		value      : _parametros.dstabla
						        },{
						            xtype         : 'combobox',
						            name          : 'pi_ottipoac',
						            fieldLabel    : 'Tipo Acceso',
						            itemId        : 'tipoAccId',
						            valueField    : 'key',
						            displayField  : 'value',
						            forceSelection: true,
						            queryMode     : 'local',
						            typeAhead     : true,
						            anyMatch      : true,
						            readOnly      : editMode,
						    		allowBlank    : false,
						            store         :  Ext.create('Ext.data.Store', {
												     model: 'Generic',
												     data : [
												         {key: 'U', value: 'Único'},
												         {key: 'T', value: 'Por Tramos'}
												     ]
												 }),
									value: _parametros.tipoacc
				        		}]
					},{
						xtype      : 'fieldset',
						itemId     : 'fieldUnaClave',
						title      : 'Dependencias para Lista de Valores',
						collapsible: true,
            			defaults: {
							style : 'margin:5px;'
						},
						items      : [{
									layout: 'column',
			            			columns: 3,
			            			border: false,
			            			defaults: {
										style : 'margin:5px;'
									},
			            			items: [{
								        	xtype       : 'combo',
								        	name        : 'pi_cdtablj1',
								        	fieldLabel  : 'Cat&aacute;logo 1',
								        	width       : 290,
								        	labelWidth  : 55,
								        	valueField  : 'cdCatalogo1',
											displayField: 'dsCatalogo1',
											forceSelection: true,
					                        queryMode   :'local',
					                        typeAhead     : true,
						            		anyMatch      : true,
											store       : Ext.create('Ext.data.Store', {
												model   : Ext.define('Modelo1', {extend: 'Ext.data.Model',fields: [{type: 'string', name: 'cdCatalogo1'},{type: 'string', name: 'dsCatalogo1'}]}),
												autoLoad : true,
												proxy : {
													type : 'ajax',
													url : _URL_Catalogo1,
													reader : {
														type : 'json',
														root : 'listaDeValores'
													}
												},
						                        listeners: {
						                        	load: function (){
						                        		if(editMode){
						                        			panelTablaApoyo.getForm().findField('pi_cdtablj1').setValue(_parametros.catuno);
						                        		}
						                        	}
						                        }
											})
								        },{
								        	xtype       : 'combo',
								        	name        : 'pi_cdtablj2',
								        	fieldLabel  : 'Cat&aacute;logo 2',
								        	width       : 290,
								        	labelWidth  : 55,
								        	valueField  : 'cdCatalogo1',
											displayField: 'dsCatalogo1',
											forceSelection: true,
					                        queryMode   :'local',
					                        typeAhead     : true,
						            		anyMatch      : true,
											store       : Ext.create('Ext.data.Store', {
												model   : Ext.define('Modelo2', {extend: 'Ext.data.Model',fields: [{type: 'string', name: 'cdCatalogo1'},{type: 'string', name: 'dsCatalogo1'}]}),
												autoLoad : true,
												proxy : {
													type : 'ajax',
													url : _URL_Catalogo2,
													reader : {
														type : 'json',
														root : 'listaDeValores2'
													}
												},
						                        listeners: {
						                        	load: function (){
						                        		if(editMode){
						                        			panelTablaApoyo.getForm().findField('pi_cdtablj2').setValue(_parametros.catdos);
						                        			panelTablaApoyo.getForm().findField('pi_cdtablj3').getStore().load({
						                        				params: {
						                        					'codigoTabla': _parametros.catdos
						                        				}
						                        			});
						                        		}
						                        	}
						                        }
											}),
											listeners: {
													select: function(comb, records){
						                        		var selValue = records[0].get("cdCatalogo1");
						                        		panelTablaApoyo.getForm().findField('pi_cdtablj3').getStore().load({
						                        				params: {
						                        					'codigoTabla': selValue
						                        				}
						                        		});
						                        	}
											}
								        },{
								        	xtype       : 'combo',
								        	name        : 'pi_cdtablj3',
								        	fieldLabel  : 'Clave',
								        	labelWidth  : 35,
								        	valueField  : 'key',
											displayField: 'value',
											forceSelection: true,
					                        queryMode   :'local',
					                        typeAhead     : true,
						            		anyMatch      : true,
											store       : Ext.create('Ext.data.Store', {
												model : 'Generic',
												proxy : {
													type : 'ajax',
													url : _URL_CatalogoClave,
													reader : {
														type : 'json',
														root : 'listaClavesDependencia'
													}
												},
						                        listeners: {
						                        	load: function (){
						                        		if(editMode){
						                        			panelTablaApoyo.getForm().findField('pi_cdtablj3').setValue(_parametros.clavecat);
						                        		}
						                        	}
						                        }
											})
								        }]
									}]
					},
		gridClaves,
		gridAtributos
        ],
        buttonAlign: 'center',
	    buttons: [{
        	text: 'Guardar',
        	itemId: 'botonGuardarId',
        	icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
        	handler: function(btn, e) {
        		
        		if (panelTablaApoyo.isValid()) {
        			
        			var msjeConfirmaGuardado;
        			if(editMode){
        				msjeConfirmaGuardado = '&iquest;Esta seguro que desea actualizar esta tabla?';
        			}else{
        				msjeConfirmaGuardado = '&iquest;Esta seguro que desea crear esta tabla?';
        			}
        			
        			Ext.Msg.show({
    		            title: 'Confirmar acci&oacute;n',
    		            msg: msjeConfirmaGuardado,
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		
    		            		var tipoTabSel = panelTablaApoyo.getForm().findField('pi_clnatura').getValue();
			                	var numPermitidas = 0;
			                	
			                	if(!Ext.isEmpty(tipoTabSel)){
			                		if(tipoTabSel == 1) numPermitidas = 1;
			                	}
			                	
    		            		var validacionMinMax = true;
    		            		var contadorClaves = 0;
    		            		var contadorAtributos = 0;
    		            		

    		            		// PARA AGREGAR CLAVES A GUARDAR Y AL MISMO TIEMPO VALIDAR VALORES MINIMOS Y MAXIMOS
    		            		var saveList = [];//para claves
    		            		clavesStore.each(function(record){
						    		if(!Ext.isEmpty(record.get('DSCLAVE1'))){
						    			saveList.push(record.data);
						    			if(record.get("NMLMIN1") > record.get("NMLMAX1")){
						    				validacionMinMax = false;
						    				mensajeWarning("El valor m&iacute;nimo debe ser menor al valor m&aacute;ximo de la clave: " + record.get("DSCLAVE1"));
						    				return false;  //break here
						    			}
						    			contadorClaves++;
						    		}
						    	});
						    	if(!validacionMinMax) return; // saliendo de la funcion principal
						    	if(numPermitidas == 1 && contadorClaves > 1){
						    		mensajeWarning("Para este tipo de Tabla solo se Permite una Clave.");
						    		return;
						    	}
						    	debug('Claves A Grabar: ', saveList);
    		            		
						    	
						    	// PARA AGREGAR ATRIBUTOS A GUARDAR Y AL MISMO TIEMPO VALIDAR VALORES MINIMOS Y MAXIMOS
								var saveList2 = [];//para atributos
    		            		atributosStore.getNewRecords().forEach(function(record,index,arr){
						    		if(record.dirty){
						    			saveList2.push(record.data);
						    			if(record.get("NMLMIN") > record.get("NMLMAX")){
						    				validacionMinMax = false;
						    				mensajeWarning("El valor m&iacute;nimo debe ser menor al valor m&aacute;ximo del atributo: " + record.get("DSATRIBU"));
						    				return false; //break here
						    			}
						    			contadorAtributos++;
						    		}else if(!Ext.isEmpty(record.get('DSATRIBU'))){
						    			contadorAtributos++;
						    		}
						    	});
						    	if(!validacionMinMax) return; // saliendo de la funcion principal
						    	if(numPermitidas == 1 && contadorAtributos > 1){
						    		mensajeWarning("Para este tipo de Tabla solo se Permite un Atributo.");
						    		return;
						    	}
						    	debug('Atributos Added: ', saveList2);
						    	
						    	
    		            		panelTablaApoyo.setLoading(true);
    		            		
    		            		Ext.Ajax.request({
				    	            url: _URL_GuardaTablaApoyo,
				    	            jsonData : {
				    	            	params: panelTablaApoyo.getValues(),
				    	                'saveList'   : saveList,
				    	                'saveList2'   : saveList2
				    	            },
				    	            success  : function(response){
				    	                panelTablaApoyo.setLoading(false);
				    	                var json = Ext.decode(response.responseText);
				    	                if(json.success){
				    	                	recargagridTablas();
				    						windowLoader.close();
				    						mensajeCorrecto('\u00C9xito', 'La tabla se guard\u00F3 correctamente', Ext.Msg.OK, Ext.Msg.INFO);
				    						panelTablaApoyo.getForm().reset();
				    	                }else{
				    	                    mensajeError(json.msgRespuesta);
				    	                }
				    	            }
				    	            ,failure  : function()
				    	            {
				    	                panelTablaApoyo.setLoading(false);
				    	                errorComunicacion();
				    	            }
						    	});
    		            	}
            			},
    		            animateTarget: btn,
    		            icon: Ext.Msg.QUESTION
        			});
    			} else {
    				Ext.Msg.show({
    					title: 'Aviso',
    		            msg: 'Complete la informaci&oacute;n requerida',
    		            buttons: Ext.Msg.OK,
    		            animateTarget: btn,
    		            icon: Ext.Msg.WARNING
    				});
    			}
        	}
        },
        {
            	text: 'Cancelar',
            	icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png',
            	handler: function(btn, e) {
            		windowLoader.close();
            	}
        }]
    });
	
    var tmField = panelTablaApoyo.down('#fieldUnaClave');
    tmField.hide();
    
});


</script>

<div id="maindivAgrTbl" style="height:700px;"></div>