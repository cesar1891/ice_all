<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _CONTEXT = '${ctx}';
var _URL_CONSULTA_CABECERAS_CLAVES = '<s:url namespace="/catalogos" action="obtieneClavesTablaApoyo" />';
var _URL_CONSULTA_CABECERAS_ATRIBUTOS = '<s:url namespace="/catalogos" action="obtieneAtributosTablaApoyo" />';

var _URL_CONSULTA_VALORES_TABLA_CINCO_CLAVES = '<s:url namespace="/catalogos" action="obtieneValoresTablaApoyo5claves" />';
var _URL_GuardaValoresTablaApoyo = '<s:url namespace="/catalogos" action="guardaValoresTablaApoyo" />';
var _URL_CONSULTA_VALORES_TABLA_UNA_CLAVE = '<s:url namespace="/catalogos" action="obtieneValoresTablaApoyo1clave" />';

var _URL_Carga_Masiva = '<s:url namespace="/cargamasiva" action="cargaTablaApoyo" />';
var _URL_Exporta_Tabla = '<s:url namespace="/reportes" action="exportaTablaApoyo" />';
var _URL_DESCARGA_DOCUMENTOS = '<s:url namespace ="/documentos" action="descargaDoc"/>';

var _RUTA_DOCUMENTOS_TEMPORAL = '<s:text name="ruta.documentos.temporal" />';

var _NMTABLA = '<s:property value="params.nmtabla" />';
var _CDTABLA = '<s:property value="params.cdtabla" />';
var _DSTABLA = '<s:property value="params.dstabla" />';
var _TIPOTABLA = '<s:property value="params.tipotab" />';
var cdSisRolActivo = '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />';
var cdUserActivo   ='<s:property value="%{#session['USUARIO'].user}" />';
var tipoproceso ;



debug('_NMTABLA:', _NMTABLA);
debug('_TIPOTABLA:', _TIPOTABLA);


var _TIPO_1CLAVE =  _TIPOTABLA == 1;

Ext.onReady(function() {
	
	if(_TIPOTABLA != 1 && _TIPOTABLA != 5){
		mensajeWarning("El tipo de Tabla a editar no est&aacute; considerado");
		return;
	}
	
	var  _NUM_MAX_FILAS = 1000;
	
	var loadMaskTabla = new Ext.LoadMask('divValoresCincoClaves', {msg:"Cargando Tabla..."});
	
	
	//Models:
	
    Ext.define('CabeceraClaveModel', {
        extend: 'Ext.data.Model',
        fields: ['DSCLAVE1','SWFORMA1','DSFORMA','NMLMIN1','NMLMAX1','NUMCLAVE']
    });

    Ext.define('CabeceraAtributoModel', {
        extend: 'Ext.data.Model',
        fields: ['CDATRIBU','DSATRIBU','SWFORMAT','DSFORMAT','NMLMIN','NMLMAX']
    });
    
    Ext.define('CincoClavesModel', {
        extend: 'Ext.data.Model',
        fields: ['NMTABLA','OTCLAVE1','OTCLAVE2','OTCLAVE3','OTCLAVE4','OTCLAVE5',
        		 {name: 'FEDESDE', defaultValue: Ext.Date.format(new Date(), 'd/m/Y')},
        		 {name: 'FEHASTA', defaultValue: Ext.Date.format(new Date(), 'd/m/Y')},
        		 'OTVALOR01','OTVALOR02','OTVALOR03','OTVALOR04','OTVALOR05',
        		 'OTVALOR06','OTVALOR07','OTVALOR08','OTVALOR09','OTVALOR10',
        		 'OTVALOR11','OTVALOR12','OTVALOR13','OTVALOR14','OTVALOR15',
        		 'OTVALOR16','OTVALOR17','OTVALOR18','OTVALOR19','OTVALOR20',
        		 'OTVALOR21','OTVALOR22','OTVALOR23','OTVALOR24','OTVALOR25','OTVALOR26']
    });
    
    Ext.define('UnaClaveModel', {
        extend: 'Ext.data.Model',
        fields: ['NMTABLA','OTCLAVE1','OTVALOR01']
    });
	
    
    //Stores:
    
    var storeCabecerasClaves = new Ext.data.Store({
        model: 'CabeceraClaveModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_CABECERAS_CLAVES,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });

    var storeCabecerasAtributos = new Ext.data.Store({
        model: 'CabeceraAtributoModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_CABECERAS_ATRIBUTOS,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    
    var storeTablaCincoClaves = new Ext.data.Store({
        model: 'CincoClavesModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_VALORES_TABLA_CINCO_CLAVES,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });

    var storeTablaUnaClave = new Ext.data.Store({
        model: 'UnaClaveModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_VALORES_TABLA_UNA_CLAVE,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
   
    
    // Create an instance of the Spread panel
    var panelValoresTabCincoClaves = new Spread.grid.Panel({
        store: _TIPO_1CLAVE ? storeTablaUnaClave : storeTablaCincoClaves,
        height: 445,
        tbar: [{
        	icon: _CONTEXT + '/resources/fam3icons/icons/table_edit.png',
            text: 'Habilitar edici&oacute;n',
            handler: function() {
                panelValoresTabCincoClaves.setEditable(true);
            }
        }, {
        	icon: _CONTEXT + '/resources/fam3icons/icons/lock.png',
            text: 'Deshabilitar edici&oacute;n',
            handler: function() {
                panelValoresTabCincoClaves.setEditable(false);
            }
        },'-',{
            xtype      : 'numberfield',
            name       : 'rowsToCreate',
            minValue   : 0,
            maxValue   : 100,
            value      : 0,
            width      : 55
        },{
        	icon: _CONTEXT + '/resources/fam3icons/icons/table_add.png',
            text: 'Agregar Filas',
            handler: function() {
            	if(!this.previousSibling().isValid()){
                    Ext.Msg.alert('Aviso', 'Ingrese un n&uacute;mero v&aacute;lido');
                    return;
                }
                var numFilas = this.previousSibling().getValue();
                
                var totalFilas = (_TIPO_1CLAVE ? storeTablaUnaClave.getCount() : storeTablaCincoClaves.getCount()) + numFilas;
                if(totalFilas > _NUM_MAX_FILAS){
                	mensajeWarning("El valor m&aacute;ximo de filas a editar es: " + _NUM_MAX_FILAS);
                	return;
                }
                
                for(var count=0 ;count<numFilas; count++){
                	if(_TIPO_1CLAVE){
                		storeTablaUnaClave.insert(storeTablaUnaClave.getCount(),new UnaClaveModel());
                	}else{
                		storeTablaCincoClaves.insert(storeTablaCincoClaves.getCount(),new CincoClavesModel());	
                	}
        			
        		}
                panelValoresTabCincoClaves.setEditable(true);//Comienza a Mode Edicion de la tabla
            }
        },{
        	icon: _CONTEXT + '/resources/fam3icons/icons/table_delete.png',
            text: 'Eliminar Fila/Clave',
            handler: function(btn,e) {
            	Ext.Msg.show({
    		            title: 'Aviso',
    		            msg: '&iquest;Esta seguro que desea eliminar la fila de la celda seleccionada?',
    		            buttons: Ext.Msg.YESNO,
    		            fn: function(buttonId, text, opt) {
    		            	if(buttonId == 'yes') {
    		            		var rowIndex = panelValoresTabCincoClaves.getSelectionModel().getCurrentFocusPosition().row;
                				
                				if(_TIPO_1CLAVE){
			                		storeTablaUnaClave.removeAt(rowIndex);
			                	}else{
			                		storeTablaCincoClaves.removeAt(rowIndex);	
			                	}
    		            	}
            			},
    		            animateTarget: btn,
    		            icon: Ext.Msg.QUESTION
        		});
            }
        }],
        // You can supply your own viewConfig to change the config of Spread.grid.View!
        //viewConfig: {
            //stripeRows: true
        //},
        listeners: {
        	render: function(grid, eOpts) {
        		cambiarEncabezados(grid);
        	}
        },
        editable: false,
        editModeStyling: true,
        enableKeyNav: true,
        columns: [
        	{
        		text: '#', xtype: 'rownumberer', sortable: false, editable: false, resizable: true, width: 40,
        		tdCls: Ext.baseCSSPrefix + 'grid-cell-header ' + Ext.baseCSSPrefix + 'grid-cell-special'
        	},
            {text: 'NMTABLA',     dataIndex: 'NMTABLA', hidden: true, menuDisabled: true, sortable: false, editable: false},
            {dataIndex: 'OTCLAVE1', itemId: 'OTCLAVE1', hidden: true, menuDisabled: true, sortable: false,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('OTCLAVE1');
		    		}
		           return newValue;
		       }
            },
            {dataIndex: 'OTCLAVE2', itemId: 'OTCLAVE2', hidden: true, menuDisabled: true, sortable: false,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('OTCLAVE2');
		    		}
		           return newValue;
		       }
            },
            {dataIndex: 'OTCLAVE3', itemId: 'OTCLAVE3', hidden: true, menuDisabled: true, sortable: false,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('OTCLAVE3');
		    		}
		           return newValue;
		       }
            },
            {dataIndex: 'OTCLAVE4', itemId: 'OTCLAVE4', hidden: true, menuDisabled: true, sortable: false,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('OTCLAVE4');
		    		}
		           return newValue;
		       }
            },
            {dataIndex: 'OTCLAVE5', itemId: 'OTCLAVE5', hidden: true, menuDisabled: true, sortable: false,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('OTCLAVE5');
		    		}
		           return newValue;
		       }
            },
            {text: 'Fecha Desde', dataIndex: 'FEDESDE', menuDisabled: true, sortable: false, hidden: _TIPO_1CLAVE,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('FEDESDE');
		    		}
		    		
		    		var regexFormato = /^(((((0[1-9])|(1\d)|(2[0-9]))\/((0[1-9])|(1[0-2])))|((31\/((0[13578])|(1[02])))|((29|30)\/((0[1,3-9])|(1[0-2])))))\/(([2-3][0-9][0-9][0-9])|(19[0-9][0-9])))|((29\/02\/(19|20)(([02468][048])|([13579][26]))))$/;
		    		if(regexFormato.test(newValue)){
                		return newValue;
                	}else{
                		setTimeout(function(){
                			mensajeWarning("El valor ' "+ newValue +" ' debe ser una fecha v&aacute;lida (Fila " + (position.row+1) + ", Columna Fecha Desde)",
                				function(){
                					position.focus();
                				}
                			);
                		},200);
                		return position.record.get('FEDESDE');
                	}
		       }
            },
            {text: 'Fecha Hasta',    dataIndex: 'FEHASTA', menuDisabled: true, sortable: false, hidden: _TIPO_1CLAVE,
            	cellwriter: function(newValue, position) {
		           /**
		    		 *Para inhabilitar la edicion de rows que ya esten en la Base de Datos 
		    		 */
					if(!position.record.phantom){
		    			return position.record.get('FEHASTA');
		    		}
		    		
		    		var regexFormato = /^(((((0[1-9])|(1\d)|(2[0-9]))\/((0[1-9])|(1[0-2])))|((31\/((0[13578])|(1[02])))|((29|30)\/((0[1,3-9])|(1[0-2])))))\/(([2-3][0-9][0-9][0-9])|(19[0-9][0-9])))|((29\/02\/(19|20)(([02468][048])|([13579][26]))))$/;
		    		if(regexFormato.test(newValue)){
                		return newValue;
                	}else{
                		setTimeout(function(){
                			mensajeWarning("El valor ' "+ newValue +" ' debe ser una fecha v&aacute;lida (Fila " + (position.row+1) + ", Columna Fecha Hasta)",
                				function(){
                					position.focus();
                				}
                			);
                		},200);
                		return position.record.get('FEHASTA');
                	}
		       }
            },
            {text: 'OTVALOR1',  itemId: 'OTVALOR1',  dataIndex: 'OTVALOR01', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR2',  itemId: 'OTVALOR2',  dataIndex: 'OTVALOR02', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR3',  itemId: 'OTVALOR3',  dataIndex: 'OTVALOR03', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR4',  itemId: 'OTVALOR4',  dataIndex: 'OTVALOR04', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR5',  itemId: 'OTVALOR5',  dataIndex: 'OTVALOR05', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR6',  itemId: 'OTVALOR6',  dataIndex: 'OTVALOR06', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR7',  itemId: 'OTVALOR7',  dataIndex: 'OTVALOR07', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR8',  itemId: 'OTVALOR8',  dataIndex: 'OTVALOR08', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR9',  itemId: 'OTVALOR9',  dataIndex: 'OTVALOR09', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR10', itemId: 'OTVALOR10', dataIndex: 'OTVALOR10', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR11', itemId: 'OTVALOR11', dataIndex: 'OTVALOR11', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR12', itemId: 'OTVALOR12', dataIndex: 'OTVALOR12', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR13', itemId: 'OTVALOR13', dataIndex: 'OTVALOR13', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR14', itemId: 'OTVALOR14', dataIndex: 'OTVALOR14', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR15', itemId: 'OTVALOR15', dataIndex: 'OTVALOR15', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR16', itemId: 'OTVALOR16', dataIndex: 'OTVALOR16', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR17', itemId: 'OTVALOR17', dataIndex: 'OTVALOR17', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR18', itemId: 'OTVALOR18', dataIndex: 'OTVALOR18', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR19', itemId: 'OTVALOR19', dataIndex: 'OTVALOR19', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR20', itemId: 'OTVALOR20', dataIndex: 'OTVALOR20', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR21', itemId: 'OTVALOR21', dataIndex: 'OTVALOR21', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR22', itemId: 'OTVALOR22', dataIndex: 'OTVALOR22', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR23', itemId: 'OTVALOR23', dataIndex: 'OTVALOR23', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR24', itemId: 'OTVALOR24', dataIndex: 'OTVALOR24', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR25', itemId: 'OTVALOR25', dataIndex: 'OTVALOR25', hidden: true, menuDisabled: true, sortable: false},
            {text: 'OTVALOR26', itemId: 'OTVALOR26', dataIndex: 'OTVALOR26', hidden: true, menuDisabled: true, sortable: false}/*,
            {
			 	xtype: 'actioncolumn',
			 	width: 30,
			 	sortable: false,
			 	menuDisabled: true,
			 	items: [{
			 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
			 		tooltip: 'Eliminar Clave',
			 		handler: function(grid, rowIndex){
			 					debug('eliminando',rowIndex);
						 		storeTablaCincoClaves.removeAt(rowIndex);
						 	}
		 		}]
			}*/
        ]
    });
    
    
    var panelValoresTablaApoyo = Ext.create('Ext.form.Panel', {
    	renderTo: 'divValoresCincoClaves',
		border: false,
		defaults: {
			style : 'margin:5px;'
		},
	    items    : [{
			            layout: {
			            	type:'table',
			            	columns: 1
			            },
            			border: false,
            			items: [{
            						layout: {
						            	type:'table',
						            	columns: 2
						            },
			            			border: false,
			            			defaults: {
										style : 'margin:3px;'
									},
			            			items: [{
									xtype      : 'hidden',
									name       : 'params.PV_LIMITE_I',
									value      :  _NUM_MAX_FILAS
								},{
									xtype      : 'hidden',
									name       : 'PV_NMTABLA_I',
									value      :  _NMTABLA
								},{
									xtype      : 'hidden',
									name       : 'ES_UNA_CLAVE',
									value      :  _TIPO_1CLAVE? 'S':'N'
								},{
									xtype      : 'textfield',
									name       : 'params.PV_NMTABLA_I',
									fieldLabel : 'N&uacute;mero de Tabla',
									value      : _NMTABLA,
									readOnly      : true
								},{
						        	xtype      : 'textfield',
						    		name       : 'params.pi_cdtabla',
						    		fieldLabel : 'C&oacute;digo de la Tabla',
									maxLength  : 30,
									maxLengthText: 'Longitud m&aacute;xima de 30 caracteres',
						    		value      : _CDTABLA,
						    		readOnly   : true,
						    		allowBlank : false
						        },{
						        	xtype      : 'textfield',
						    		name       : 'params.pi_dstabla',
						    		allowBlank : false,
						    		readOnly   : true,
						    		fieldLabel : 'Descripci&oacute;n de la Tabla',
						    		width      : 545,
						    		labelWidth : 130,
						    		maxLength  : 60,
									maxLengthText: 'Longitud m&aacute;xima de 60 caracteres',
						    		value      : _DSTABLA
						        }]
			            			
            					},{
						    	layout: 'column',
						    	title: 'Filtrar datos de Tabla',
						    	border: false,
						    	defaults: {
						    		style: 'margin: 3px',// para hacer que los componentes se separen 5px
						    		width: 220
						    	},
						    	buttonAlign: 'center',
						    	items: [{
							    	xtype: 'textfield',
							    	fieldLabel: 'Clave 1',
							    	labelWidth: 120,
							    	readOnly: true,
							    	name: 'params.PV_OTCLAVE1_I'
							    },{
							    	xtype: 'textfield',
							    	fieldLabel: 'Clave 2',
							    	labelWidth: 120,
							    	readOnly: true,
							    	hidden: _TIPO_1CLAVE,
							    	name: 'params.PV_OTCLAVE2_I'
							    },{
							    	xtype: 'textfield',
							    	fieldLabel: 'Clave 3',
							    	labelWidth: 120,
							    	readOnly: true,
							    	hidden: _TIPO_1CLAVE,
							    	name: 'params.PV_OTCLAVE3_I'
							    },{
							    	xtype: 'textfield',
							    	fieldLabel: 'Clave 4',
							    	labelWidth: 120,
							    	readOnly: true,
							    	hidden: _TIPO_1CLAVE,
							    	name: 'params.PV_OTCLAVE4_I'
							    },{
							    	xtype: 'textfield',
							    	fieldLabel: 'Clave 5',
							    	labelWidth: 120,
							    	readOnly: true,
							    	hidden: _TIPO_1CLAVE,
							    	name: 'params.PV_OTCLAVE5_I'
							    },{
							    	xtype: 'datefield',
							    	fieldLabel: 'Fecha Desde',
							    	labelWidth: 80,
							    	hidden: _TIPO_1CLAVE,
							    	name: 'params.PV_FEDESDE_I'
							    },{
							    	xtype: 'datefield',
							    	fieldLabel: 'Fecha Hasta',
							    	labelWidth: 80,
							    	hidden: _TIPO_1CLAVE,
							    	name: 'params.PV_FEHASTA_I'
							    }],
							    buttons:
							    	[	{
						                    text     : 'Limpiar'
						                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
						                    ,handler : function()
						                    {
						                    	panelValoresTablaApoyo.getForm().reset();
						                    }
						                },
						                {
						                    text     : 'Buscar'
						                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
						                    ,handler : function()
						                    {
						                        if(panelValoresTablaApoyo.isValid())
						                        {
						                        	recargagridTabla5Claves();
						                        }
						                        else
						                        {
						                            Ext.Msg.show(
						                            {
						                                title    : 'Datos imcompletos'
						                                ,icon    : Ext.Msg.WARNING
						                                ,msg     : 'Favor de llenar los campos requeridos'
						                                ,buttons : Ext.Msg.OK
						                            });
						                        }
						                    }
						                }
						            ]
						    }]
					}, panelValoresTabCincoClaves],
					buttonAlign : 'center',
							    buttons:
							    	[{
							    	text : 'Carga Masiva',
							    	tooltip: 'Carga Masiva de la tabla, agrega sin eliminar los registros existentes.',
							    	icon:_CONTEXT+'/resources/fam3icons/icons/database_lightning.png',
							    	handler: function(btn){
							    		
						    			var _p22_windowAgregarDocu=Ext.create('Ext.window.Window',
							            {
							                title       : 'Subir Archivo de Carga Masiva'
							                ,closable    : false
							                ,modal       : true
							                ,width       : 500
							                //,height   : 700
							                ,bodyPadding : 5
							                ,items       :
							                [
							                    panelSeleccionDocumento= Ext.create('Ext.form.Panel',
							                    {
							                        border       : 0
							                        ,url         : _URL_Carga_Masiva
							                        ,timeout     : 600
							                        ,buttonAlign : 'center'
							                        ,items       :
							                        [
							                            {
							                                xtype       : 'filefield'
							                                ,fieldLabel : 'Documento'
							                                ,buttonText : 'Examinar...'
							                                ,buttonOnly : false
							                                ,width      : 450
							                                ,name       : 'file'
							                                ,cAccept    : ['xls','xlsx']
							                                ,listeners  :
							                                {
							                                    change : function(me)
							                                    {
							                                        var indexofPeriod = me.getValue().lastIndexOf("."),
							                                        uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
							                                        if (!Ext.Array.contains(this.cAccept, uploadedExtension))
							                                        {
							                                            Ext.MessageBox.show(
							                                            {
							                                                title   : 'Error de tipo de archivo',
							                                                msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
							                                                buttons : Ext.Msg.OK,
							                                                icon    : Ext.Msg.WARNING
							                                            });
							                                            me.reset();
							                                            Ext.getCmp('_p22_botGuaDoc').setDisabled(true);
							                                            
							                                        }
							                                        else
							                                        {
							                                            Ext.getCmp('_p22_botGuaDoc').setDisabled(false);
							                                            
							                                        }
							                                    }
							                                }
							                            },///////////////////
							                            {
										                	xtype: 'radiogroup',
			                            					name: 'groupTipoBusqueda',
			                            					flex: 15,
			                            					columns: 1,
			                            					
			                            					vertical: true,
			                            					items: [
			                            						{boxLabel: 'Agregar Datos Nuevos', name: 'tipoBusqueda', inputValue: 1,  width: 200},
			                            						{boxLabel: 'Actualizar Tarifas', name: 'tipoBusqueda', inputValue: 2, checked: true, width: 200},
			                            						{boxLabel: 'Reemplazar Datos', name: 'tipoBusqueda', inputValue: 3,width: 200, hidden: (cdSisRolActivo==RolSistema.SuscriptorAuto) ? true : false,}
									                                ],
			                            					listeners : {
			                            						change : function(radiogroup, newValue, oldValue, eOpts) {
			                            							var valor = this.up('form').getForm().findField('tipoBusqueda').inputValue;
			                            							switch (newValue.tipoBusqueda) {
			                            								case 1:
			                            								tipoproceso = 1;
			                            									this.up('form').getForm().findField("params.feCierre").setVisible(false);
				                            								
			                            								break;
			                            								case 2: 
			                            								tipoproceso = 2;
				                            								this.up('form').getForm().findField("params.feCierre").setVisible(true);
				                            								
			                            								break;
			                            								case 3:
			                            								tipoproceso = 3;
			                            									this.up('form').getForm().findField("params.feCierre").setVisible(false);
				                            								
			                            								break;
			                            								}
			                            						}
			                            						}
			                            				},{
							                                            // Fecha de Cierre de Tarifa
							                                            xtype : 'datefield',
							                                            name : 'params.feCierre',
							                                            itemId:'feCierre',
							                                            bodyStyle:'padding:2px;',
							                                            fieldLabel : 'Fecha Cierre',
							                                            value : new Date(),
							                                            hidden : false,
							                                            allowBlank: false
							                                        }
										                        ]
							                        ,buttons     :
							                        [
							                            {
							                                id        : '_p22_botGuaDoc'
							                                ,text     : 'Agregar'
							                                ,icon     : '${ctx}/resources/fam3icons/icons/disk.png'
							                                ,disabled : true
							                                ,handler  : function (button,e)
							                                {
							                                    button.setDisabled(true);
							                                    Ext.getCmp('_p22_BotCanDoc').setDisabled(true);
							                                    
							                                    /////TODO: setear valores en los campos hiden campoHidden.setValue(_NMTABLA)
							                                    
							                                    button.up('form').getForm().submit(
							                                    {
							                                        //standardSubmit : true
							                                    	
							                                        params        :
							                                        {
							                                            'params.pi_nmtabla': _NMTABLA
							                                            ,'params.tipotabla': _TIPOTABLA
							                                            ,'params.tipoproceso' : tipoproceso!=null?tipoproceso:2
							                                            //,'params.feinicio' : 
							                                        },
							                                        
							                                        waitMsg: 'Ejecutando Carga Masiva...',
												                    success: function(form, action) {
												                    	_p22_windowAgregarDocu.destroy();
												                        mensajeCorrecto('Exito', 'La carga masiva se ha ejecutado correctamente.');
												                        recargagridTabla5Claves();
												                    },
												                    failure: function(form, action) {
												                    	
												                    	manejaErrorSubmit(form, action, function() {
												                    		
												                    		Ext.getCmp('_p22_botGuaDoc').setDisabled(false);
                                                                            Ext.getCmp('_p22_BotCanDoc').setDisabled(false);
                                                                            
                                                                            if(action.result.resultado.key == 1) {
                                                                                // Error en validacion de formato:
                                                                                msgServer = 'Error en la validación ¿Desea descargar el archivo de errores?';
                                                                                Ext.Msg.show({
                                                                                    title: 'Error', 
                                                                                    msg: msgServer, 
                                                                                    buttons: Ext.Msg.YESNO, 
                                                                                    icon: Ext.Msg.ERROR,
                                                                                    fn: function(btn){
                                                                                        if (btn == 'yes'){
                                                                                            Ext.create('Ext.form.Panel').submit({
                                                                                                url            : _URL_DESCARGA_DOCUMENTOS,
                                                                                                standardSubmit : true,
                                                                                                target         : '_blank',
                                                                                                params         : {
                                                                                                    path     : _RUTA_DOCUMENTOS_TEMPORAL,
                                                                                                    filename : action.result.fileFileName 
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                 });
                                                                            } else if (action.result.resultado.key == 2) {
                                                                                // Error en carga masiva:
                                                                                Ext.Msg.show({title: 'Error', msg: action.result.resultado.value, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
                                                                            }
												                    	});
												        			}
							                                    });
							                                    tipoproceso = null;
							                                }
							                            }
							                            ,{
							                                id       : '_p22_BotCanDoc'
							                                ,text    : 'Cancelar'
							                                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
							                                ,handler : function (button,e)
							                                {
							                                    _p22_windowAgregarDocu.destroy();
							                                }
							                            }
							                        ]
							                    })
							                
    								]
							            }).show();
							            centrarVentanaInterna(_p22_windowAgregarDocu);
							    	}
							    },{
							    	text : 'Exportar',
							    	tooltip: 'Exporta la tabla a formato Excel.',
							    	icon:_CONTEXT+'/resources/fam3icons/icons/database_table.png',
							    	 hidden: (cdSisRolActivo==RolSistema.SuscriptorAuto) ? false : true, //(cdSisRolActivo==RolSistema.SuscriptorAuto || cdSisRolActivo==RolSistema.Parametrizador) ? false : true,//se evalua habilitar o desabilitar Boton, dependiendo el rol
							    	handler: function(btn){
							    		var loadMaskExport = new Ext.LoadMask('divValoresCincoClaves', {msg:"Exportando Tabla de Apoyo..."});
										loadMaskExport.show();
							    		setTimeout(function(){
				                			loadMaskExport.hide();
				                		},25000);
							    		
							    		Ext.create('Ext.form.Panel').submit({
											url : _URL_Exporta_Tabla,
											standardSubmit : true,
											params : panelValoresTablaApoyo.getValues(),
	                                        //waitMsg: 'Exportando Tabla de Apoyo...',
						                    failure: function(form, action) {
						                		switch (action.failureType) {
						                            case Ext.form.action.Action.CONNECT_FAILURE:
						                        	    Ext.Msg.show({title: 'Error', msg: 'Error de comunicaci&oacute;n', buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
						                                break;
						                            case Ext.form.action.Action.SERVER_INVALID:
						                            case Ext.form.action.Action.LOAD_FAILURE:
						                            	 var msgServer = Ext.isEmpty(action.result.mensaje) ? 'Error al exportar, consulte a soporte' : action.result.mensaje;
						                                 Ext.Msg.show({title: 'Error', msg: msgServer, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
						                                break;
						                        }
						        			}
										});
										
							    	}
							    },'->',{
						        	text: 'Guardar Valores',
						        	itemId: 'botonGuardarValoresId',
						        	icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
						        	handler: function(btn, e) {

						        		/**
						        		 * Para forzar el fin de edición de la tabla y evitar perder valores: 
						        		 */
						        		var posActual = panelValoresTabCincoClaves.getSelectionModel().getCurrentFocusPosition();
						        		panelValoresTabCincoClaves.getSelectionModel().setCurrentFocusPosition(posActual);
						        		
						        		
						        		if (panelValoresTablaApoyo.isValid()) {
						        			
						        			Ext.Msg.show({
						    		            title: 'Confirmar acci&oacute;n',
						    		            msg: '&iquest;Esta seguro que desea actualizar esta tabla?',
						    		            buttons: Ext.Msg.YESNO,
						    		            fn: function(buttonId, text, opt) {
						    		            	if(buttonId == 'yes') {
						    		            		
						    		            		// PARA AGREGAR NUEVAS FILAS A  GUARDAR
						    		            		var deleteList = [];
						    		            		var saveList = [];
						    		            		var updateList = [];
    													

						    		            		if(_TIPO_1CLAVE){
									                		storeTablaUnaClave.getRemovedRecords().forEach(function(record,index,arr){
													        	deleteList.push(record.data);
													    	});
							    		            		
							    		            		storeTablaUnaClave.getNewRecords().forEach(function(record,index,arr){
													    		if(record.dirty){
													    			saveList.push(record.data);
													    		}
													    	});
													    	
													    	storeTablaUnaClave.getUpdatedRecords().forEach(function(record,index,arr){
													    		updateList.push(record.data);
													    	});
									                	}else{
									                		storeTablaCincoClaves.getRemovedRecords().forEach(function(record,index,arr){
													        	deleteList.push(record.data);
													    	});
							    		            		
							    		            		storeTablaCincoClaves.getNewRecords().forEach(function(record,index,arr){
													    		if(record.dirty){
													    			saveList.push(record.data);
													    		}
													    	});
													    	
													    	storeTablaCincoClaves.getUpdatedRecords().forEach(function(record,index,arr){
													    		updateList.push(record.data);
													    	});	
									                	}
												    	
													    debug('Claves Removed: ' , deleteList);
													    debug('Claves Added: '   , saveList);
													    debug('Claves Updated: ' , updateList);
												    	
												    	
						    		            		panelValoresTablaApoyo.setLoading(true);
						    		            		
						    		            		Ext.Ajax.request({
										    	            url: _URL_GuardaValoresTablaApoyo,
										    	            jsonData : {
										    	            	params: panelValoresTablaApoyo.getValues(),
										    	                'deleteList' : deleteList,
										    	                'saveList'   : saveList,
										    	                'updateList' : updateList
										    	            },
										    	            success  : function(response, options){
										    	                panelValoresTablaApoyo.setLoading(false);
										    	                var json = Ext.decode(response.responseText);
										    	                if(json.success){
										    	                	recargagridTabla5Claves();
										    	                	mensajeCorrecto('Aviso','Se ha guardado correctamente la tabla');
										    	                }else{
										    	                    mensajeError(json.msgRespuesta);
										    	                }
										    	            }
										    	            ,failure  : function(response, options){
										    	                panelValoresTablaApoyo.setLoading(false);
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
						        }
						      ]
		});
    
    /////////////////////////////////////// INIT //////////////////////
    
    // Cargamos los valores de la tabla:
    
		
	recargagridTabla5Claves = function(){
		loadMaskTabla.show();
		
		if(!_TIPO_1CLAVE){
			storeTablaCincoClaves.load({
				params: panelValoresTablaApoyo.getValues(),
				callback: function(records, operation, success){
					loadMaskTabla.hide();
		    		if(success){
		                var jsonResponse = Ext.decode(operation.response.responseText);
		                
		                if(!jsonResponse.success) {
		                    mensajeError(jsonResponse.msgRespuesta);
		                }else{
		                	if(!Ext.isEmpty(jsonResponse.msgRespuesta)) {
		                		mensajeWarning(jsonResponse.msgRespuesta);
		                	}
		                	
		                	//Agregamos 10 rows vacios por defecto:
				        	for(var count=0 ;count<10; count++){
				        		storeTablaCincoClaves.insert(storeTablaCincoClaves.getCount(),new CincoClavesModel());
				        	}
		                }
		            }else{
		                showMessage('Error', 'Error al obtener los datos de las claves.', Ext.Msg.OK, Ext.Msg.ERROR);
		            }
				}
			});
		}else {
			storeTablaUnaClave.load({
				params: panelValoresTablaApoyo.getValues(),
				callback: function(records, operation, success){
					loadMaskTabla.hide();
		    		if(success){
		                var jsonResponse = Ext.decode(operation.response.responseText);
		                
		                if(!jsonResponse.success) {
		                    mensajeError(jsonResponse.msgRespuesta);
		                }else{
		                	if(!Ext.isEmpty(jsonResponse.msgRespuesta)) {
		                		mensajeWarning(jsonResponse.msgRespuesta);
		                	}
		                	
		                	//Agregamos 10 rows vacios por defecto:
				        	for(var count=0 ;count<10; count++){
		                		storeTablaUnaClave.insert(storeTablaUnaClave.getCount(),new UnaClaveModel());
				        	}
		                }
		            }else{
		                showMessage('Error', 'Error al obtener los datos de los atributos.', Ext.Msg.OK, Ext.Msg.ERROR);
		            }
				}
			});
		}
	};
	
    
    /**
     * Se cambian los encabezados de las columnas de clave según el nmtabla
     * @param grid grid al que se le cambiaran los encabezados
     */
    function cambiarEncabezados(grid) {
    	
    	loadMaskTabla.show();
    	
    	storeCabecerasClaves.load({
            params : {
                'params.pi_nmtabla' : _NMTABLA 
            },
            callback: function(records, operation, success) {
                Ext.each(records, function(record, index) {
                	debug('ClaveIndex:', index+1);
                    // Asignamos la descripción de las columnas de forma dinamica:
                    grid.getView().headerCt.child("#OTCLAVE"+(index+1)).setText(record.get('DSCLAVE1'));
                    
                    panelValoresTablaApoyo.down('[name=params.PV_OTCLAVE'+ (index+1) +'_I]').setFieldLabel(record.get('DSCLAVE1'));
                    panelValoresTablaApoyo.down('[name=params.PV_OTCLAVE'+ (index+1) +'_I]').setReadOnly(false);
                    
                    
                    var dsFormato;
                    var regexFormato;
                    var tipoFormato = record.get('SWFORMA1');
                    var valMinimo = record.get('NMLMIN1');
                    var valMaximo = record.get('NMLMAX1');
                     
                    
                    if(tipoFormato == 'A'){
                    	regexFormato = new RegExp("^[a-zA-Z0-9\-]+$");
                    	dsFormato = 'Alfanum&eacute;rico';
                    }else if(tipoFormato == 'N'){
                    	regexFormato = new RegExp("^([0-9])+$");
                    	dsFormato = 'Num&eacute;rico Entero';
                    }else if(tipoFormato == 'P'){
                    	regexFormato = new RegExp("^\\d+(\\.\\d+)?$");
                    	dsFormato = 'Porcentual';
                    }else if(tipoFormato == 'F'){
                    	regexFormato = /^(((((0[1-9])|(1\d)|(2[0-9]))\/((0[1-9])|(1[0-2])))|((31\/((0[13578])|(1[02])))|((29|30)\/((0[1,3-9])|(1[0-2])))))\/(([2-3][0-9][0-9][0-9])|(19[0-9][0-9])))|((29\/02\/(19|20)(([02468][048])|([13579][26]))))$/;
                    	dsFormato = 'Fecha v&aacute;lida';
                    }
                    
                    var mensajeLongitud = "Longitud m&iacute;nima " + valMinimo + ", m&aacute;xima "+valMaximo;
                    if(tipoFormato == 'F') mensajeLongitud  = "";
                    
                    grid.getView().headerCt.child("#OTCLAVE"+(index+1)).cellwriter = function(newValue, position){
                    	
                    	if(!Ext.isEmpty(newValue)){
	                    	if(regexFormato.test(newValue)){
	                    		if(String(newValue).length >= valMinimo && String(newValue).length <= valMaximo){
	                    			return newValue;
	                    		}else{
	                    			if(tipoFormato == 'F') return newValue;
	                    			
		                    		setTimeout(function(){
			                    			mensajeWarning("El valor ' "+ newValue +" ' debe ser de " + mensajeLongitud + " (Fila " + (position.row+1) + ", Columna " + record.get('DSCLAVE1') + ") </br>&nbsp;",
			                    				function(){
			                    					position.focus();
			                    				}
			                    			);
		                    		},200);
		                    		return '';
		                    	}
	                    	}else{
	                    		setTimeout(function(){
	                    			mensajeWarning("El valor ' "+ newValue +" ' debe ser " + dsFormato + " (Fila " + (position.row+1) + ", Columna " + record.get('DSCLAVE1') + ") </br>" + mensajeLongitud + "</br>&nbsp;",
	                    				function(){
	                    					position.focus();
	                    				}
	                    			);
	                    		},200);
	                    		return '';
	                    	}
                    	}else{
                    		return newValue;
                    	}
                    	
                    }
                    
                    grid.getView().headerCt.child("#OTCLAVE"+(index+1)).setVisible(true);
                    
                });
                
                storeCabecerasAtributos.load({
		            params : {
		                'params.pi_nmtabla' : _NMTABLA 
		            },
		            callback: function(records, operation, success) {
		                Ext.each(records, function(record, index) {
		                	debug('AtrIndex:', index+1);
		                    // Asignamos la descripción de las columnas de forma dinamica:
		                    grid.getView().headerCt.child("#OTVALOR"+(index+1)).setText(record.get('DSATRIBU'));
		                    
		                    var dsFormato;
		                    var regexFormato;
		                    var tipoFormato = record.get('SWFORMAT');
		                    var valMinimo = record.get('NMLMIN');
		                    var valMaximo = record.get('NMLMAX');
		                     
		                    
		                    if(tipoFormato == 'A'){
		                    	regexFormato = new RegExp("^.*$");//new RegExp("^[a-zA-Z0-9\\s]+$");
		                    	dsFormato = 'Alfanum&eacute;rico';
		                    }else if(tipoFormato == 'N'){
		                    	regexFormato = new RegExp("^([0-9])+$");
		                    	dsFormato = 'Num&eacute;rico Entero';
		                    }else if(tipoFormato == 'P'){
		                    	regexFormato = new RegExp("^\\d+(\\.\\d+)?$");
		                    	dsFormato = 'Porcentual';
		                    }else if(tipoFormato == 'F'){
		                    	regexFormato = /^(((((0[1-9])|(1\d)|(2[0-9]))\/((0[1-9])|(1[0-2])))|((31\/((0[13578])|(1[02])))|((29|30)\/((0[1,3-9])|(1[0-2])))))\/(([2-3][0-9][0-9][0-9])|(19[0-9][0-9])))|((29\/02\/(19|20)(([02468][048])|([13579][26]))))$/;
		                    	dsFormato = 'Fecha v&aacute;lida';
		                    }
		                    
		                    var mensajeLongitud = "Longitud m&iacute;nima " + valMinimo + ", m&aacute;xima "+valMaximo;
		                    if(tipoFormato == 'F') mensajeLongitud  = "";
		                    
		                    grid.getView().headerCt.child("#OTVALOR"+(index+1)).cellwriter = function(newValue, position){
		                    	
		                    	if(!Ext.isEmpty(newValue)){
			                    	if(regexFormato.test(newValue)){
			                    		if(String(newValue).length >= valMinimo && String(newValue).length <= valMaximo){
			                    			return newValue;
			                    		}else{
			                    			if(tipoFormato == 'F') return newValue;
			                    			
				                    		setTimeout(function(){
					                    			mensajeWarning("El valor ' "+ newValue +" ' debe ser de " + mensajeLongitud + " (Fila " + (position.row+1) + ", Columna " + record.get('DSATRIBU') + ") </br>&nbsp;",
					                    				function(){
					                    					position.focus();
					                    				}
					                    			);
				                    		},200);
				                    		return '';
				                    	}
			                    	}else{
			                    		setTimeout(function(){
			                    			mensajeWarning("El valor ' "+ newValue +" ' debe ser " + dsFormato + " (Fila " + (position.row+1) + ", Columna " + record.get('DSATRIBU') + ") </br>" + mensajeLongitud + "</br>&nbsp;",
			                    				function(){
			                    					position.focus();
			                    				}
			                    			);
			                    		},200);
			                    		return '';
			                    	}
		                    	}else{
		                    		return newValue;
		                    	}
		                    	
		                    }
		                    
		                    
		                    grid.getView().headerCt.child("#OTVALOR"+(index+1)).setVisible(true);
		                });
		                
		                loadMaskTabla.hide();
		            }
		        });
            }
        });
        
    }
    
    recargagridTabla5Claves();
    
});

</script>
        
<div id="divValoresCincoClaves"/>
