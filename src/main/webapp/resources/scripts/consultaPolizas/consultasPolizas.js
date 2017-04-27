Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

var gridDatosAsegurado;
var windowCoberturas;

Ext.onReady(function() {
	
    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

    Ext.selection.CheckboxModel.override({
        mode: 'SINGLE',
        allowDeselect: true
    });
    
    // Conversion para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
    
    Ext.define('KeyValueModel', {
        extend: 'Ext.data.Model',
        fields : [{
            name : 'key',
            type : 'int'
        }, {
            name : 'value',
            type : 'string'
        }]
    });
    
    var storeTiposConsulta = Ext.create('Ext.data.JsonStore', {
        model: 'KeyValueModel',
        proxy: {
            type: 'ajax',
            url: _URL_TIPOS_CONSULTA,
            reader: {
                type: 'json',
                root: 'tiposConsulta'
            }
        }
    });
    storeTiposConsulta.load();
    
    var storeTiposBusqueda = Ext.create('Ext.data.Store', {
        fields: ['key', 'value'],
        data : [
            {'key': 5,'value':'Por n\u00FAmero de p\u00F3liza'},
            {'key': 1,'value':'Por p\u00F3liza en car\u00E1tula'},
            {'key': 2,'value':'Por RFC'},
            {'key': 3,'value':'Por clave de asegurado'},
            {'key': 4,'value':'Por nombre'}
        ]
    });

    var listViewOpcionesConsulta = Ext.create('Ext.grid.Panel', {
        collapsible:true,
        collapsed:true,
        store: storeTiposConsulta,
        multiSelect: false,
        hideHeaders:true,
        viewConfig: {
            emptyText: 'No hay tipos de consulta'
        },
        columns: [{
            flex: 1,
            dataIndex: 'value'
        }],
        listeners : {
            selectionchange : function(view, nodes) {
                tabDatosGeneralesPoliza.hide();
                tabPanelAgentes.hide();
            
                if (this.getSelectionModel().hasSelection()) {
                    var tipoConsultaSelected = this.getSelectionModel().getSelection()[0];
                    
                    if (gridSuplementos.getSelectionModel().hasSelection()) {
                    
                        switch (tipoConsultaSelected.get('key')) {
                            case 1: //Consulta de Datos generales
                                
                                //Mostrar seccion de datos generales:
                                tabDatosGeneralesPoliza.show();
                                
                                debug('antes de cargar storeAsegurados=', storeAsegurados);
                                
                                //Datos para asegurados
                                var params = panelBusqueda.down('form').getForm().getValues();
                               
                                //Perfil Medico
                                var listaPersonas = params['params.cdperson'];
                                params['params.listaPersonas'] = listaPersonas;
                                //Perfil Medico
                                
                                params['params.nombre'] = null;
                                storeAsegurados.getProxy().extraParams = params;
//                                storeAsegurados.getProxy().extraParams = panelBusqueda.down('form').getForm().getValues();
                                debug('se asignaron extra params', storeAsegurados.getProxy().extraParams);
                                storeAsegurados.load({
                                    //params: panelBusqueda.down('form').getForm().getValues(),
                                    callback: function(records, operation, success){
                                    	
//                                    	_fieldByName('filtrarFam',gridDatosAsegurado).setValue('');
//                                    	_fieldByName('filtrarAseg',gridDatosAsegurado).setValue('');
//                                    	_fieldByName('filtrarCveAseg',gridDatosAsegurado).setValue('');
//                                    	
//                                    	storeAsegurados.clearFilter();
                                    	
                                        if(!success){
                                            showMessage('Error', 'Error al obtener los datos del asegurado', Ext.Msg.OK, Ext.Msg.ERROR);
                                        }
                                    }
                                });
                                break;
                                
                                case 2: //Consulta de Agentes
                                    
                                    if (gridSuplementos.getSelectionModel().hasSelection()) {
                                        
                                        //Mostrar seccion de agentes:
                                        tabPanelAgentes.show();
                                    
                                        //Agentes de la poliza
                                        storeAgentesPol.load({
                                            params: panelBusqueda.down('form').getForm().getValues(),
                                            callback: function(records, operation, success) {
                                                if(!success){
                                                    showMessage('Error', 'Error al obtener los datos del agente, intente m\u00E1s tarde',
                                                            Ext.Msg.OK, Ext.Msg.ERROR);
                                                }
                                             }
                                        });
                                        
                                        // Obtenemos los recibos de los agentes:
                                        storeRecibosAgente.load({
                                            params: panelBusqueda.down('form').getForm().getValues(),
                                            callback: function(records, operation, success){
                                                if(success){
                                                    if(records.length > 0){
                                                        Ext.getCmp('montoTotalRecibos').setText(obtieneMontosRecibo(records));
                                                    }
                                                }else{
                                                    showMessage('Error', 'Error al obtener los recibos de dicho Agente', Ext.Msg.OK, Ext.Msg.ERROR);
                                                }                    
                                            }
                                        });
                                    }
                                    break;
                                case 3: //Consulta de Recibos
                                break;                    
                        }//end switch                
                    } else {
                        showMessage('Aviso', 'Debe seleccionar primero un hist\u00F3rico', Ext.Msg.OK, Ext.Msg.WARN)
                    }
                }
            }
        }
    });
    
    // Historico de movimientos (suplementos):
    Ext.define('SuplementoModel', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'cdramo'},
            {name: 'cdunieco'},
            {name: 'estado'},
            {name: 'nmpoliza'},
            {name: 'nmsuplem'},
            {name: 'dstipsup'},
            {name: 'feemisio', dateFormat: 'd/m/Y'},
            {name: 'feinival', dateFormat: 'd/m/Y'},            
            {name: 'nlogisus'},
            {name: 'nsuplogi', type:'int'},
            {name: 'ptpritot', type : 'float'},
            {name: 'switchConvenios'}
        ]
    });
    
    
    var storeSuplementos = new Ext.data.Store({
        model: 'SuplementoModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_SUPLEMENTO,
            reader: {
                type: 'json',
                root: 'datosSuplemento'
            }
        }
    });
    
    var gridSuplementos = Ext.create('Ext.grid.Panel', {
        id    : 'suplemento-form',
        store : storeSuplementos,
        selType: 'checkboxmodel',
        //autoScroll:true,
        defaults: {sortable : true, align : 'right'},
        columns : [{
            text : '#',
            dataIndex : 'nsuplogi',
            width:50
        }, {
            id : 'dstipsup',
            text : 'Tipo de endoso',
            dataIndex : 'dstipsup',
            width:420
        }, {
            text : 'Fecha de emisi\u00F3n',
            dataIndex : 'feemisio',
            format: 'd M Y',
            width:150
        }, {
            text : 'Fecha inicio vigencia',
            dataIndex : 'feinival',
            format: 'd M Y',
            width:150
        }, {
            text : 'Prima total',
            dataIndex : 'ptpritot',
            renderer : 'usMoney',
            width:150
        }],

        listeners : {
            selectionchange : function(model, records) {
                
                listViewOpcionesConsulta.up('panel').setTitle('');
                
                //Limpiar seleccion de la lista de opciones de consulta
                limpiaSeleccionTiposConsulta();
                
                //Limpiar la seccion de informacion principal:
                limpiaSeccionInformacionPrincipal();
                
                if(this.getSelectionModel().hasSelection()) {
                    
                    // Mostrar listado de tipos de consulta:
                    listViewOpcionesConsulta.up('panel').setTitle('Elija una consulta:');
                    listViewOpcionesConsulta.expand();
                    
                
                    //Lenar campos de formulario de busqueda:
                    var rowSelected = gridSuplementos.getSelectionModel().getSelection()[0];
                    panelBusqueda.down('form').getForm().findField("params.cdunieco").setValue(rowSelected.get('cdunieco'));
                    panelBusqueda.down('form').getForm().findField("params.cdramo").setValue(rowSelected.get('cdramo'));
                    panelBusqueda.down('form').getForm().findField("params.estado").setValue(rowSelected.get('estado'));
                    panelBusqueda.down('form').getForm().findField("params.nmpoliza").setValue(rowSelected.get('nmpoliza'));
                    panelBusqueda.down('form').getForm().findField("params.suplemento").setValue(rowSelected.get('nmsuplem'));
                    //console.log('Params busqueda de datos grales poliza=');console.log(panelBusqueda.down('form').getForm().getValues());

                    // Consultar Datos Generales de la Poliza:
                    storeDatosPoliza.load({
                        params : panelBusqueda.down('form').getForm().getValues(),
                        callback : function(records, operation, success) {
                            if (success) {
                                if (records.length > 0) {
                                    // Se asigna valor al parametro de busqueda:
                                    panelBusqueda.down('form').getForm().findField("params.cdagente").setValue(records[0].get('cdagente'));
                                    
                                    // Se llenan los datos generales de la poliza elegida
                                    panelDatosPoliza.getForm().loadRecord(records[0]);
                                    
                                    if('COLECTIVA' == records[0].get('tipopol')){
//										gridDatosAsegurado.getView().headerCt.child("#grupo").setVisible(true);
                                    	gridDatosAsegurado.getView().headerCt.child("#familia").setVisible(true);                                    	
                                    }else{
//                                    	gridDatosAsegurado.getView().headerCt.child("#grupo").setVisible(false);
                                    	gridDatosAsegurado.getView().headerCt.child("#familia").setVisible(false);
                                    }
                                    
                                } else {
                                    showMessage('Aviso', 
                                        'No existen datos generales de la p\u00F3liza elegida. La P&oacute;iza no existe, verifique sus datos', 
                                        Ext.Msg.OK, Ext.Msg.ERROR);
                                }
                            } else {
                                showMessage('Error', 
                                    'Error al obtener los datos generales de la p\u00F3liza elegida, intente m\u00E1s tarde',
                                    Ext.Msg.OK, Ext.Msg.ERROR);
                            }

                        }
                    });
                    
                    for(numAtribu = 1 ; numAtribu <= 50 ; numAtribu++){
                    	var campo = panelDatosPoliza.down('[name=otvalor'+numAtribu+']');
						campo.hide();
             	   		campo.setFieldLabel('otvalor'+numAtribu);
             	   		campo.setValue('');
                    }
                    
                    //Carga de datos de Tvalopol 
                    storeDatosPolizaTvalopol.load({
				        params : panelBusqueda.down('form').getForm().getValues(),
				        callback : function(records, operation, success) {
				            if (success) {
				            	debug('*****Records de tvalopol: ' , records);
				                if (records.length > 0) {
				                 	   records.forEach(function (element, index, array){
				                 	   		var campo = panelDatosPoliza.down('[name=otvalor'+element.get('CDATRIBU')+']');
				                 	   		campo.show();
				                 	   		campo.setFieldLabel(element.get('DSATRIBU'));
				                 	   		campo.setValue(element.get('OTVALOR'));
				                 	   });
				                }
				            } else {
				                showMessage('Error', 
				                    'Error al obtener datos de la p\u00F3liza elegida, intente m\u00E1s tarde',
				                    Ext.Msg.OK, Ext.Msg.ERROR);
				            }
				
				        }
    				});
                }
            }
        }
    });
    gridSuplementos.store.sort([
        { 
            property    : 'nsuplogi',
            direction   : 'DESC'
        }
    ]);
    
    // Modelo
    Ext.define('DatosPolizaModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'nmsolici'},
            {type:'string', name:'titular'},
            {type:'string', name:'cdrfc'},
            {type:'date',   name:'feemisio', dateFormat: 'd/m/Y'},
            {type:'date',   name:'feefecto', dateFormat: 'd/m/Y'},
            {type:'date',   name:'feproren', dateFormat: 'd/m/Y'},
            {type:'string', name:'dstarifi'},
            {type:'string', name:'dsmoneda'},
            {type:'string', name:'dscuadro'},
            {type:'string', name:'dsperpag'},
            {type:'string', name:'dstempot'},
            {type:'string', name:'nmpoliex'},
            {type:'string', name:'cdagente'},
            {type:'string', name:'statuspoliza'},
            {type:'string', name:'nmpolant'},
            {type:'string', name:'dsunieco'},
            {type:'string', name:'dsramo'},
            {type:'string', name:'dsplan'},
            {type:'string', name:'tipopol'},
            {type:'string', name:'dstipsit'},
            {type:'string', name:'nmrenova'}
//            {type:'string', name:'switchConvenios'}
        ]
    });

    // Modelo
    Ext.define('DatosPolizaModelTvalopol', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'OTVALOR'},
            {type:'string', name:'CDATRIBU'},
            {type:'string', name:'DSATRIBU'}
        ]
    });

    // Store
    var storeDatosPoliza = new Ext.data.Store({
        model: 'DatosPolizaModel',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosPoliza'
            }
        }
    });

    // Store
    var storeDatosPolizaTvalopol = new Ext.data.Store({
        model: 'DatosPolizaModelTvalopol',
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_DATOS_POLIZA_TVALOPOL,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    
    // FORMULARIO DATOS DE LA POLIZA
    var panelDatosPoliza = Ext.create('Ext.form.Panel', {
        model : 'DatosPolizaModel',
        title  : 'Datos Generales',
        collapsible: true,
        border : false,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items : [ {
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'dsunieco', fieldLabel: 'Sucursal',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dsramo',   fieldLabel: 'Ramo',      readOnly: true, labelWidth: 65,  width: 290, labelAlign: 'right'},
                {xtype: 'textfield', name: 'nmpoliex', fieldLabel: 'P&oacute;liza', readOnly: true, labelWidth: 70,  width: 210, labelAlign: 'right'}
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield', name: 'dsplan',   fieldLabel: 'Plan',      readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dstipsit', fieldLabel: 'Subramo', readOnly: true, labelWidth: 65,  width: 290, labelAlign: 'right'},
                {xtype: 'textfield', name: 'nmrenova', fieldLabel: 'N&uacute;mero Renovaci&oacute;n',  readOnly: true, labelWidth: 70, width: 210,labelAlign: 'right'}
                
            ]
        },{
            layout : 'hbox',
            items : [
                {xtype: 'textfield',                  name: 'statuspoliza', fieldLabel: 'Estatus',                readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', id: 'nmpolant',  name: 'nmpolant',     fieldLabel: 'P&oacute;liza anterior', readOnly: true, labelWidth: 100, width: 210, labelAlign: 'right'},
                {xtype: 'textfield', id: 'nmsolici',  name: 'nmsolici',     fieldLabel: 'No. de solicitud',       readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype:'textfield', name:'titular', fieldLabel: 'Contratante', readOnly: true, labelWidth: 120, width: 590}, 
                {xtype:'textfield', name:'cdrfc',   fieldLabel: 'RFC',         readOnly: true, labelWidth: 80,  width: 210, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'datefield', name: 'feemisio', fieldLabel: 'Fecha emisi&oacute;n',    format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 300}, 
                {xtype: 'datefield', name: 'feefecto', fieldLabel: 'Fecha inicio vigencia',         format: 'd/m/Y', readOnly: true, labelWidth: 200, width: 290, labelAlign: 'right'}, 
                {xtype: 'datefield', name: 'feproren', fieldLabel: 'Fecha fin vigencia', format: 'd/m/Y', readOnly: true, labelWidth: 120, width: 210, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'dstarifi', fieldLabel: 'Tipo de tarificaci&oacute;n', readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dscuadro', fieldLabel: 'Cuadro de comisiones',        readOnly: true, labelWidth: 150, width: 290, labelAlign: 'right'},
                {xtype: 'textfield', name: 'dsmoneda', fieldLabel: 'Moneda',                      readOnly: true, labelWidth: 120, width: 210, labelAlign: 'right'}
            ]
        }, {
            layout : 'hbox',
            items : [ 
                {xtype: 'textfield', name: 'dsperpag', fieldLabel: 'Forma de Pago',  readOnly: true, labelWidth: 120, width: 300},
                {xtype: 'textfield', name: 'dstempot', fieldLabel: 'Tipo de P&oacute;liza', readOnly: true, labelWidth: 120, width: 290, labelAlign: 'right'}
            ]
        }, Ext.create('Ext.form.Panel', {
        	title : 'Datos Adicionales',
        	layout: {
                type     : 'table',
                columns : 2
        	},
            defaults : {
            	style : 'margin:5px',
            	xtype : 'textfield',
            	readOnly: true,
            	labelWidth: 190,
            	width: 390,
            	hidden: true
            },
            items : [
                {name: 'otvalor1',  fieldLabel: 'otvalor1'},
                {name: 'otvalor2',  fieldLabel: 'otvalor2'},
                {name: 'otvalor3',  fieldLabel: 'otvalor3'},
                {name: 'otvalor4',  fieldLabel: 'otvalor4'},
                {name: 'otvalor5',  fieldLabel: 'otvalor5'},
                {name: 'otvalor6',  fieldLabel: 'otvalor6'},
                {name: 'otvalor7',  fieldLabel: 'otvalor7'},
                {name: 'otvalor8',  fieldLabel: 'otvalor8'},
                {name: 'otvalor9',  fieldLabel: 'otvalor9'},
                {name: 'otvalor10', fieldLabel: 'otvalor10'},
                {name: 'otvalor11', fieldLabel: 'otvalor11'},
                {name: 'otvalor12', fieldLabel: 'otvalor12'},
                {name: 'otvalor13', fieldLabel: 'otvalor13'},
                {name: 'otvalor14', fieldLabel: 'otvalor14'},
                {name: 'otvalor15', fieldLabel: 'otvalor15'},
                {name: 'otvalor16', fieldLabel: 'otvalor16'},
                {name: 'otvalor17', fieldLabel: 'otvalor17'},
                {name: 'otvalor18', fieldLabel: 'otvalor18'},
                {name: 'otvalor19', fieldLabel: 'otvalor19'},
                {name: 'otvalor20', fieldLabel: 'otvalor20'},
                {name: 'otvalor21', fieldLabel: 'otvalor21'},
                {name: 'otvalor22', fieldLabel: 'otvalor22'},
                {name: 'otvalor23', fieldLabel: 'otvalor23'},
                {name: 'otvalor24', fieldLabel: 'otvalor24'},
                {name: 'otvalor25', fieldLabel: 'otvalor25'},
                {name: 'otvalor26', fieldLabel: 'otvalor26'},
                {name: 'otvalor27', fieldLabel: 'otvalor27'},
                {name: 'otvalor28', fieldLabel: 'otvalor28'},
                {name: 'otvalor29', fieldLabel: 'otvalor29'},
                {name: 'otvalor30', fieldLabel: 'otvalor30'},
                {name: 'otvalor31', fieldLabel: 'otvalor31'},
                {name: 'otvalor32', fieldLabel: 'otvalor32'},
                {name: 'otvalor33', fieldLabel: 'otvalor33'},
                {name: 'otvalor34', fieldLabel: 'otvalor34'},
                {name: 'otvalor35', fieldLabel: 'otvalor35'},
                {name: 'otvalor36', fieldLabel: 'otvalor36'},
                {name: 'otvalor37', fieldLabel: 'otvalor37'},
                {name: 'otvalor38', fieldLabel: 'otvalor38'},
                {name: 'otvalor39', fieldLabel: 'otvalor39'},
                {name: 'otvalor40', fieldLabel: 'otvalor40'},
                {name: 'otvalor41', fieldLabel: 'otvalor41'},
                {name: 'otvalor42', fieldLabel: 'otvalor42'},
                {name: 'otvalor43', fieldLabel: 'otvalor43'},
                {name: 'otvalor44', fieldLabel: 'otvalor44'},
                {name: 'otvalor45', fieldLabel: 'otvalor45'},
                {name: 'otvalor46', fieldLabel: 'otvalor46'},
                {name: 'otvalor47', fieldLabel: 'otvalor47'},
                {name: 'otvalor48', fieldLabel: 'otvalor48'},
                {name: 'otvalor49', fieldLabel: 'otvalor49'},
                {name: 'otvalor50', fieldLabel: 'otvalor50'}
            ]
        })
        ],
		tbar:[
			{
				text: 'Ver Siniestralidad',
				icon:_CONTEXT+'/resources/fam3icons/icons/application_view_list.png',
				handler: function(){
					siniestralidad(panelBusqueda.down('form').getForm().findField("params.cdunieco").getValue(), panelBusqueda.down('form').getForm().findField("params.cdramo").getValue(),
                    	null,panelBusqueda.down('form').getForm().findField("params.nmpoliza").getValue(),"1");
				}
			}
		]
    });
    
    
    /**INFORMACION DEL GRID DE TARIFICACION**/
    //-------------------------------------------------------------------------------------------------------------    
    //Modelo
    Ext.define('DatosTarificacionModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'string',    name:'dsgarant'      },
              {type:'float',    name:'montoComision' },
              {type:'float',    name:'montoPrima'    },
              {type:'string',    name:'sumaAsegurada' }
        ]
    });
    
    // Store
    var storeDatosTarificacion = new Ext.data.Store({
        model: 'DatosTarificacionModel',
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_DATOS_TARIFA_POLIZA,
            reader: {
                type: 'json',
                root: 'datosTarifa'
            }
        }
    });
    /**GRID PARA LOS DATOS DE TARIFICACION **/
    var gridDatosTarificacion = Ext.create('Ext.grid.Panel', {
        width   : 820,
        //title   : 'DATOS TARIFICACI&Oacute;N',
        store   : storeDatosTarificacion,
        autoScroll:true,
        id      : 'gridDatosTarificacion',
        features:[{
                    ftype:'summary'
                }],
        columns: [
            {
                text            :'Garant&iacute;a',
                dataIndex       :'dsgarant',
                width           :250,
                summaryRenderer : function(value){return Ext.String.format('TOTALES');}
            },
            {
                text            :'Suma Asegurada',  
                dataIndex       :'sumaAsegurada',
                width           :170 , 
                align           :'right'  
                
            },
            {
                text            :'Monto de la Prima',
                dataIndex       :'montoPrima',
                width           : 170,
                align           :'right',        
                renderer        :Ext.util.Format.usMoney,
                summaryType     :'sum'
            },
            {
                text            : 'Monto de la Comisi&oacute;n',
                dataIndex       :'montoComision',
                width           : 170,
                renderer        :Ext.util.Format.usMoney,
                align           :'right',        
                summaryType     :'sum'
            }
        ]
    });
    gridDatosTarificacion.store.sort([
        { 
            property    : 'dsgarant',
            direction   : 'ASC'
        }
    ]);
    
    
    /**INFORMACION DEL GRID DE COPAGOS/COBERTURAS**/
    //-------------------------------------------------------------------------------------------------------------    
    //Modelo
    Ext.define('CopagosPolizaModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'int',    name:'orden'      },
              {type:'string',    name:'descripcion' },
              {type:'string',    name:'valor' },
              {type:'string',    name:'agrupador' , id:'agrupadorId'},
              {type:'string',    name:'ordenAgrupador' }
        ]
    });
    // Store
    var storeCopagosPoliza = new Ext.data.Store({
        model: 'CopagosPolizaModel',
//        groupField : 'ordenAgrupador',
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_COPAGOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosCopagosPoliza'
            }
        }
    });
    
    
    
    // GRID PARA LOS DATOS DE COPAGOS/COBERTURAS
    var gridCopagosPoliza = Ext.create('Ext.grid.Panel', {
        width   : 570,
        viewConfig: {
            stripeRows: false,
            enableTextSelection: true
        },
        //title   : 'DATOS COPAGOS',
        store   : storeCopagosPoliza,
        autoScroll:true,
        id      : 'gridCopagosPoliza',
        columns: [
            //{text:'Orden',            dataIndex:'orden',       width:50, sortable:false, hidden:true},
            {text:'Descripci\u00F3n', dataIndex:'descripcion', width:370, align:'left', sortable:false},
            {text:'Valor',            dataIndex:'valor',       width:200, align:'left', sortable:false}
        ]
//        ,features: [{
//            groupHeaderTpl: '{[values.children[0].get("agrupador")]}',
//            ftype:          'grouping',
//            startCollapsed: false
//        }]
    });
    gridCopagosPoliza.store.sort([
        { 
            property    : 'orden',
            direction   : 'ASC'
        }
    ]);
    
    windowCoberturas = Ext.create('Ext.window.Window', {
                        title       : 'COBERTURAS',
                        buttonAlign : 'center',
                        autoScroll  : true,
                        modal       : true,
                        closeAction : 'hide',
                        width       : 600,
                        height      : 500,
                        items: [gridCopagosPoliza]
                     });
    
    
    /**INFORMACION DE LA SECCION DE ASEGURADOS**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('AseguradosModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdperson'},
            {type:'string', name:'cdrfc'},
            {type:'string', name:'cdrol'},
            {type:'string', name:'dsrol'},
            {type:'date'  , name:'fenacimi', dateFormat: 'd/m/Y'},
            {type:'string', name:'nmsituac'},
            {type:'string', name:'cdtipsit'},
            {type:'string', name:'sexo'},
            {type:'string', name:'nombre'},
            {type:'string', name:'status'},
            {type:'string', name:'grupo'},
            {type:'string', name:'cdgrupo'},
            {type:'string', name:'familia'},
            {type:'string', name:'cdfamilia'},
            {type:'string', name:'dsplan'},
            {type:'string', name:'parentesco'},
            //Perfil Medico
            {type:'string', name:'cantIcd'},
            {type:'string', name:'maxPerfil'},
            {type:'string', name:'numPerfil'},
            {type:'string', name:'perfilFinal'}
            //Perfil Medico
        ],
        idProperty: 'cdperson'
    });
    
    // Store
    var storeAsegurados = new Ext.data.Store({
     model: 'AseguradosModel',
     groupField : 'grupo',
     pageSize: 25,
     proxy:
     {
          type: 'ajax',
          url : _URL_CONSULTA_DATOS_ASEGURADO,
	      reader:
	      {
	           type: 'json',
	           root: 'datosAsegurados',
	           totalProperty: 'totalCount',
	           simpleSortMode: true
	      }
     } /*,
     listeners: {
         //load: function(store, records, successful, eOpts){
     	 beforeload: function(store) {
        	debug("beforeload",store);
        	if( Ext.isEmpty(store.getProxy().extraParams)){
        		debug('extraParams vacio',store.getProxy().extraParams);
        		store.getProxy().extraParams = panelBusqueda.down('form').getForm().getValues();      	
        	}
        	
//        	if(!Ext.isEmpty(panelBusqueda) && store.getProxy().extraParams != null){
//				debug("Asignando Valores",panelBusqueda.down('form').getForm().getValues());
//        		store.getProxy().extraParams = panelBusqueda.down('form').getForm().getValues()
//        	}
        }
     	 
     }
     */
    });
    
    gridDatosAsegurado = Ext.create('Ext.grid.Panel', {
        title   : 'DATOS DE LOS ASEGURADOS',
        store   : storeAsegurados,
        id      : 'gridDatosAsegurado',
        width   : 950,
        autoScroll:true,
        selModel: Ext.create('Ext.selection.CheckboxModel', {mode: 'MULTI'}),
        plugins: [{ptype : 'pagingselectpersist'}],
        items:[{
           xtype:'textfield', name:'cdrfc', fieldLabel: 'RFC', readOnly: true, labelWidth: 120
        }],
        columns: [
            //{text:'Rol',dataIndex:'dsrol',width:130 , align:'left'},
        	{
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page.png',
                tooltip      : 'Ver Coberturas',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    var values = panelBusqueda.down('form').getForm().getValues();
                    
                    debug('record nmsituac:',record.get('nmsituac'));
                    values['params.nmsituac']=record.get('nmsituac');
                    
                    debug('form values:',values);
                    
//                    panelBusqueda.down('form').getForm().getValues()
                    
                    //Datos de Copagos de poliza
                    storeCopagosPoliza.load({
                        params: values,
                        callback: function(records, operation, success){
                            if(!success){
                                showMessage('Error', 'Error al obtener los copagos de la p\u00F3liza', 
                                    Ext.Msg.OK, Ext.Msg.ERROR)
                            }              
                            gridCopagosPoliza.store.sort([
						        { 
						            property    : 'orden',
						            direction   : 'ASC'
						        }
						    ]);
                        }
                    });
                    
                    
                    windowCoberturas.setTitle('COBERTURAS DE ' + record.get('nombre'));
                    windowCoberturas.show();
                }
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/lock.png',
                tooltip      : 'Ver endosos',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    var values = panelBusqueda.down('form').getForm().getValues();
                    debug('record nmsituac:',record.get('nmsituac'));
                    values['params.nmsituac']=record.get('nmsituac');
                    debug('form values:',values);
                    Ext.create('Ext.window.Window', {
                        title       : 'Endosos',
                        modal       : true,
                        buttonAlign : 'center',
                        autoScroll  : true,
                        width       : 450,
                        height      : 455,
                        loader      :
                        {
                            url      : _URL_LOADER_VER_EXCLUSIONES,
                            scripts  : true,
                            autoLoad : true,
                            params   : values
                        }
                     }).show();
                }
            },{
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/information.png',
                tooltip      : 'Ver detalle del asegurado',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(gridView, rowIndex, colIndex, item, e, record, row) {
                    
                    // Se obtiene los parametros a enviar y se complementan:
                    var values = panelBusqueda.down('form').getForm().getValues();
                    values['params.nmsituac']=record.get('nmsituac');
                    values['params.cdtipsit']=record.get('cdtipsit');
                    debug('parametros para obtener los datos de tatrisit:', values);
                	
                	// Se crea ventana para mostrar el detalle del asegurado:
                	Ext.create('Ext.window.Window', {
                		title       : 'Detalle de ' + record.get('nombre') + ':',
                        modal       : true,
                        width       : 600,
                        items: [{
                            xtype  : 'panel',
                            name   : 'pnlDatosTatrisit',
                            autoScroll  : true,
                            loader: {
                                url : _URL_LOADER_VER_TATRISIT,
                                scripts  : true,
                                loadMask : true,
                                autoLoad : true,
                                ajaxOptions : {
                                    method: 'POST'
                                },
                                params: values
                            }
                        }]
                    }).show();
                	
                }
            },{
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/application_view_list.png',
                tooltip      : 'Siniestralidad',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    debug('record cdperson ==> :',record,record.get('cdperson'));
                    var values = panelBusqueda.down('form').getForm().getValues();
                    siniestralidad(null, null,record.get('cdperson'),null,"0");//cdunieco,cdramo, cdperson, nmpoliza
                }
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page_white_acrobat.png',
                tooltip      : 'Estado de cuenta',
                width        : 20,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
//                    debug('record cdperson ==> :',record,record.get('cdperson'));
                    var values = panelBusqueda.down('form').getForm().getValues();
                    
                    values['params.nmsituac']=record.get('nmsituac');
                    values['params.swfonsin']= "N";
                    _reporteEdoCta(values,"Estado de Cuenta");//cdunieco,cdramo, cdperson, nmpoliza
                }
                	
                
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page_white_database.png',
                tooltip      : 'Edo. Cuenta aplicaci&oacute;n Fondo',
                width        : 20,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
//                    debug('record cdperson ==> :',record,record.get('cdperson'));
                    var values = panelBusqueda.down('form').getForm().getValues();
                    
                    values['params.nmsituac']=record.get('nmsituac');
                    values['params.swfonsin']= "S";
                    
                    _reporteEdoCta(values,"Estado de Cuenta aplicaci&oacute;n Fondo");//cdunieco,cdramo, cdperson, nmpoliza
                }
            },
            //Perfil Medico
            /*{
    			xtype: 'actioncolumn',
    			width: 20,
    			autoShow: true,
    			/*
    			items: [{
    				iconCls:'iconYellow',
    				//icon: _CONTEXT+'/resources/fam3icons/icons/'+FlagsECD.PerfilCero,
    				tooltip: 'Perfil ',
    				handler      : function(grid,rowIndex){
	    				var record = grid.getStore().getAt(rowIndex);
	                	debug('record cdperson ==> :',record, 'cdperson: ', record.get('cdperson'));
	                	var values = panelBusqueda.down('form').getForm().getValues();
	                	values['params.nmsituac']=record.get('nmsituac');
	                	values['params.swfonsin']= "S";
	                	values['params.cdperson']= record.get('cdperson');
	                    
	                	_consultaIcds(values,"Consulta de ICDs");
	    			}
    			},
                {*/
    			/*getClass: function (val, meta, rec) {
    				debug('Entre al getClass this ', this, 'perfilFinal ', rec.get('perfilFinal'));
    				var url=_CONTEXT+'/resources/fam3icons/icons/';
    				this.icon = url+FlagsECD.PerfilCero; //inicializando el icono
    				if (rec.get('perfilFinal')=='3') {
			        	debug('Entre al getClass Perfil 3');
			            url+=FlagsECD.PerfilTres;
			            this.tooltip = 'Perfil 3';
			            this.icon = url;
			            //return 'Perfil 3';
			            return 'iconRed';
			        }
			        if (rec.get('perfilFinal')=='2') {
			        	debug('Entre al getClass Perfil 2');
			        	url+=FlagsECD.PerfilDos;
			            this.tooltip = 'Perfil 2';
			            this.icon = url;
			            //return 'Perfil 2';
			            return 'iconYellow';
			        }
			        if (rec.get('perfilFinal')=='1') {
			        	debug('Entre al getClass Perfil 1');
			        	url+=FlagsECD.PerfilUno;
			            this.tooltip = 'Perfil 1';
			            this.icon = url;
			            //return 'Perfil 1';
			            return 'iconGreen';
			        }
			        if ((rec.get('perfilFinal')=='0') || (rec.get('perfilFinal')=='')){
			        	debug('Entre al getClass Perfil 0');
			            url+=FlagsECD.PerfilCero;
			            this.tooltip = 'Perfil 0';
			            this.icon = url;
			            //return 'Perfil 0';
			            return 'iconBlue';
			        }
    			}
    			,handler      : function(grid,rowIndex){
    				var record = grid.getStore().getAt(rowIndex);
                	debug('record cdperson ==> :',record, 'cdperson: ', record.get('cdperson'));
                	var values = panelBusqueda.down('form').getForm().getValues();
                	values['params.nmsituac']=record.get('nmsituac');
                	values['params.swfonsin']= "S";
                	values['params.cdperson']= record.get('cdperson');
                    
                	_consultaIcds(values,"Consulta de ICDs");
    			}
    			//}]
			},*/
            //Perfil Medico
            {text:'Perfil',dataIndex:'perfilFinal', width:90 , align:'left',
            	renderer: function(value, metaData, record, rowIndex , colIndex, store, view) {
            		debug('entre en el renderer value ', value);
            		value2='Perfil '+value;
                    if (value == '3') {
                    	debug('if3 ', value);
                    	metaData.style += 'color:red;font-weight:bold;text-decoration: underline;';
                    } 
                    if (value == '2') {
                    	debug('if2 ', value);
                    	metaData.style += 'color:yellow;font-weight:bold;text-decoration: underline;';
                    } 
                    if (value == '1') {
                    	debug('if1 ', value);
                    	metaData.style += 'color:green;font-weight:bold;text-decoration: underline;';
                    }
                    if ((value == '0') || (value == '') || (value==null)) {
                    	debug('if0 ', value);
                    	metaData.style += 'color:blue;font-weight:bold;text-decoration: underline;';
                    	value2='Perfil 0';
                    } 
            		debug('valor del perfil: ', value2);
                    return value2;
                }
            },
            {text:'Plan',flex: 1,dataIndex:'dsplan',width:90 , align:'left'},
            {text:'Tipo de <br/>asegurado',flex: 1,dataIndex:'parentesco',width:80 , align:'left'},
            {text:'Clave <br/>Asegurado',flex: 1,dataIndex:'cdperson',width:80,align:'left'},
            {text:'Nombre',flex: 1,dataIndex:'nombre',width:180,align:'left'},
            {text:'Estatus',flex: 1,dataIndex:'status',width:80,align:'left'},
            {text:'RFC',flex: 1,dataIndex:'cdrfc',width:100,align:'left'},
            {text:'Sexo',flex: 1,dataIndex:'sexo',width:60 , align:'left'},
            {text:'Grupo',flex: 1,dataIndex:'grupo', itemId: 'grupo',width:100, align:'left', hidden: true},
            {text:'Familia',flex: 1,dataIndex:'familia', itemId: 'familia',width:100, align:'left', hidden: true},
            {text:'Fecha Nac.',flex: 1,dataIndex:'fenacimi',width:90, align:'left',renderer: Ext.util.Format.dateRenderer('d/m/Y')}
        ],
        tbar: [{
                xtype : 'textfield',
                name : 'filtrarAseg',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Asegurado:</span>',
                labelWidth : 100,
                width: 260,
                maxLength : 50
            },'-',{
                xtype : 'textfield',
                name : 'filtrarCveAseg',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Cve. Asegurado:</span>',
                labelWidth : 120,
                width: 220,
                maxLength : 50
            },'-',{
                xtype : 'numberfield',
                name : 'filtrarFam',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Familia:</span>',
                labelWidth : 80,
                width: 240
            },{
//            },'-',{
//                xtype : 'textfield',
//                name : 'filtrarFam',
//                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Familia:</span>',
//                labelWidth : 80,
//                width: 240
//            },{
	            xtype    : 'button',
	            text     : 'Buscar',
	            icon     : _CONTEXT+'/resources/fam3icons/icons/zoom.png',
	            handler : function(btn) {
	            	debug('antes de asignar valores', storeAsegurados.getProxy().extraParams);
		            storeAsegurados.getProxy().extraParams = panelBusqueda.down('form').getForm().getValues();
//		            storeAsegurados.getProxy().setExtraParam('params.familia', btn.up('grid').down('[name=filtrarFam]').getValue());
		            storeAsegurados.getProxy().setExtraParam('params.nmsitaux', btn.up('grid').down('[name=filtrarFam]').getValue());
		            storeAsegurados.getProxy().setExtraParam('params.nombre', btn.up('grid').down('textfield[name=filtrarAseg]').getValue());
		            storeAsegurados.getProxy().setExtraParam('params.cdperson', btn.up('grid').down('textfield[name=filtrarCveAseg]').getValue());
		            debug('cdperson','params.cdperson', btn.up('grid').down('textfield[name=filtrarCveAseg]').getValue());
		            debug('despues de asignar valores', storeAsegurados.getProxy().extraParams);
		            //storeAsegurados.load();
		            storeAsegurados.loadPage(1);
	            }
                                
            }	
        ]
        ,features: [{
            groupHeaderTpl: '{name}',
            ftype:          'grouping',
            startCollapsed: false
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeAsegurados,
            displayInfo: true,
            displayMsg: 'Asegurados {0} - {1} of {2}',
            emptyMsg: "No hay asegurados"
        })
        
        ,listeners: {
			cellclick: function ( grd, td, cellIndex, record, tr, rowIndex, e, eOpts ) {
				//debug('>cellclick ', ' td ', td, ' cellInde ', cellIndex, ' tr ', tr, ' rowIndex ', rowIndex , ' e ', e);
				debug('>cellclick ');
				if (cellIndex==7){//7 es la columna donde se visualiza la informacion del perfil medico	
					
	                var record = grd.getStore().getAt(rowIndex);
	                debug('record cdperson ==> :',record, 'cdperson: ', record.get('cdperson'));
	                var values = panelBusqueda.down('form').getForm().getValues();
	                values['params.nmsituac']=record.get('nmsituac');
	                values['params.swfonsin']= "S";
	                values['params.cdperson']= record.get('cdperson');
	                    
	                _consultaIcds(values,"Consulta de ICDs");
				}
                debug('<cellclick');
			
			}
		}
		
    });
    
    
    
    /**INFORMACION DEL GRID DE LA POLIZA DEL ASEGURADO**/
    //-------------------------------------------------------------------------------------------------------------    
    // Modelo
    Ext.define('PolizaAseguradoModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdramo'},
            {type:'string', name:'cdunieco'},
            {type:'string', name:'dsramo'},
            {type:'string', name:'dsunieco'},
            {type:'string', name:'dstipsit'},
            {type:'string', name:'cdsubram'},
            {type:'string', name:'estado'},
            {type:'string', name:'nmpoliex'},
            {type:'string', name:'nmpoliza'},
            {type:'string', name:'nombreAsegurado'},
            {type:'string', name:'nombreAgente'},
            {name:'feinivigencia', dateFormat: 'd/m/Y'},
            {name:'fefinvigencia', dateFormat: 'd/m/Y'}
        ]
    });
    
    var storePolizaAsegurado = Ext.create('Ext.data.Store', {
        pageSize : 10,
        autoLoad : true,
        model: 'PolizaAseguradoModel',
        proxy    : {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
    });
    
    
    var botonPerfil = Ext.create('Ext.Button', {
        			    text	: 'E.C.D'
        			    //,renderTo:  
        			    ,listeners : {
        			    	beforerender: function(me)
        			    	{
        			    		me.setLoading(true);
        			    		Ext.Ajax.request(
        			    			    {
        			    			        url      : _URL_CONSULTA_PERFIL
        			    			        ,params  :
        			    			        {
        			    			            'params.listaPersonas'  : ''+panelBusqueda.down('form').getForm().findField("params.cdperson").getValue()
        			    			            
        			    			        }
        			    			        ,success : function(response)
        			    			        {
        			    			            me.setLoading(false);
        			    			            var json = Ext.decode(response.responseText);
        			    			            debug('### cargar suma asegurada:',json);
        			    			            if(json.success)
        			    			            {
        			    			            	
        			    			            	var url=_CONTEXT+'/resources/fam3icons/icons/';
        			    			            	var en=parseInt(json.list[0].PERFIL_FINAL+'')
        			    			            	
        			    			            	if(json.list.length>0){
	        			    			                switch(en){
	        			    			                	case 0:
	        			    			                		url+=FlagsECD.PerfilCero;
	        			    			                		break;
	        			    			                	case 1:
	        			    			                		url+=FlagsECD.PerfilUno;
	        			    			                		break;
	        			    			                	case 2: 
	        			    			                		url+=FlagsECD.PerfilDos;
	        			    			                		break;
	        			    			                	case 3:
	        			    			                		url+=FlagsECD.PerfilTres;
	        			    			                		break;
	        			    			                	
	        			    			                }
        			    			            	}
        			    			                me.setIcon(url);
        			    			            }
        			    			            else
        			    			            {
        			    			                mensajeError(json.respuesta);
        			    			            }
        			    			        }
        			    			        ,failure : function()
        			    			        {
        			    			            me.setLoading(false);
        			    			            errorComunicacion();
        			    			        }
        			    			    });
        			    		me.icon= _CONTEXT+''
        			    	}
        			    }
        			    ,handler: function (){
        			    	try{
        			    		
        			    		Ext.create('Ext.window.Window',
                                        {
                                            title        : 'E.C.D.'
                                            //,modal       : true
                                            ,buttonAlign : 'center'
                                            ,width       : 350
                                            ,height      : 300
                                            ,autoScroll  : true
                                            ,loader      :
                                            {
                                                url       : _URL_CONSULTA_ECD
                                                ,scripts  : true
                                                ,autoLoad : true
                                                ,loadMask : true
                                                ,ajaxOptions: {
                                                    method   : 'POST'
                                                },
                                                params: {
                                                    'params.cdperson':panelBusqueda.down('form').getForm().findField("params.cdperson").getValue()
                                                }
                                            }
                                        }).show();
        			    	
        			    	}catch(e){
        			    		console.log(e)
        			    	}
        			    }
        			    
        			})
        			//fin boton
    // GRID PARA LOS DATOS DEL ASEGURADO
    
    var oculto;
    if(cdSisRolActivo=='PROMOTORAUTO') {
    	oculto = false;
    } else {
    	oculto = true;
    }
    
    var gridPolizasAsegurado= Ext.create('Ext.grid.Panel', {
        store    : storePolizaAsegurado,
        selType  : 'checkboxmodel',
        //width  : anchoGridPolizas,
        minHeight: 300, 
        bbar     :
        {
            displayInfo : true,
            store       : storePolizaAsegurado,
            xtype       : 'pagingtoolbar'
        },
        features:[{
           ftype:'summary'
        }],
        columns: [
            {text: 'P&oacute;liza', dataIndex: 'nmpoliex', width: 160},
            {text: 'P&oacute;liza interna', dataIndex: 'nmpoliza', width: 100},
            {
                text: 'Fecha de vigencia', 
                dataIndex: 'feinivigencia', 
                format: 'd M Y', 
                width:160,
                renderer: function(value, metaData, record, rowIndex , colIndex, store, view) {
                    return value + ' - ' + record.get('fefinvigencia');
                }
            },
            {
                text: 'Estatus P&oacute;liza', 
                dataIndex: 'estado', 
                width: 100,
                renderer: function(value, metaData, record, rowIndex , colIndex, store, view) {
                    if (value == 'VIGENTE') {
                    	metaData.style += 'color:green;font-weight:bold;';
                    } else {
                    	metaData.style += 'color:red;font-weight:bold;';
                    }
                    return value;
                }
            },
            {text: 'Nombre del asegurado', dataIndex: 'nombreAsegurado', width: 200},
            {text: 'Sucursal', dataIndex: 'dsunieco', width: 140},
            //{text: 'Producto', dataIndex: 'dsramo', width:180},
            {text: 'Descripci&oacute;n de producto', dataIndex: 'dstipsit', width:180},
            {text: 'Subramo', dataIndex: 'cdsubram', width:80},
            {text: 'Agente', dataIndex: 'nombreAgente', width:80, hidden:oculto}
            //,{text: '# poliza', dataIndex: 'nmpoliza', width: 70}
        ]
    });
    
    
    var windowPolizas= Ext.create('Ext.window.Window', {
        title : 'Elija una p&oacute;liza:',
        modal:true,
        //autoScroll : true,
        width : 950,
        closeAction: 'hide',
        items:[gridPolizasAsegurado],
        buttons:[{
            text: 'Aceptar',
            handler: function(){
                if (gridPolizasAsegurado.getSelectionModel().hasSelection()) {
                    
                    //Asignar valores de la poliza seleccionada al formulario de busqueda
                    var rowPolizaSelected = gridPolizasAsegurado.getSelectionModel().getSelection()[0];
                    var formBusqueda = panelBusqueda.down('form').getForm();
                    formBusqueda.findField("params.nmpoliex").setValue(rowPolizaSelected.get('nmpoliex'));
                    
                    gridPolizasAsegurado.getStore().removeAll();
                    windowPolizas.close();
                    // Recargar store con busqueda de historicos de la poliza seleccionada  
                   // storeAsegurados.getProxy().extraParams = panelBusqueda.down('form').getForm().getValues();
                    var params = formBusqueda.getValues();                    
                    params['params.nombre'] = rowPolizaSelected.data['nombreAsegurado'];                    
                    cargaStoreSuplementos(params);
                }else{
                    showMessage('Aviso', 'Seleccione un registro', Ext.Msg.OK, Ext.Msg.INFO);
                }
            }
        }]
    });
    
   
    var tabDatosGeneralesPoliza = Ext.create('Ext.tab.Panel', {
        //width: 950,
        items: [{
            title : 'DATOS DE LA POLIZA',
            border:false,
            autoScroll: true,
            items:[
                panelDatosPoliza
            ]
        },{
            title: 'ASEGURADOS',
            autoScroll  : true,
            itemId: 'tabDatosAsegurados',
            items:[
                gridDatosAsegurado
            ]
        }, {
            itemId: 'tbDocumentos',
            title : 'DOCUMENTACION',
            //autoScroll  : true,
            //width: '350',
            loader : {
                url : _URL_CONSULTA_DOCUMENTOS,
                scripts : true,
                autoLoad : false
            },
            listeners : {
                activate : function(tab) {
                    tab.loader.load({
                        params : {
                            'smap1.readOnly' :  true,
                            'smap1.nmpoliza' :  
                            panelBusqueda.down('form').getForm().findField("params.nmpoliza").getValue(),
                            'smap1.cdunieco' :  panelBusqueda.down('form').getForm().findField("params.cdunieco").getValue(),
                            'smap1.cdramo' :  panelBusqueda.down('form').getForm().findField("params.cdramo").getValue(),
                            'smap1.estado' :  panelBusqueda.down('form').getForm().findField("params.estado").getValue(),
                            'smap1.nmsuplem' :  panelBusqueda.down('form').getForm().findField("params.suplemento").getValue(),
                            'smap1.ntramite' : "",
                            'smap1.tipomov'  : '0'
                        }
                    });
                }
            }
        }, {
            itemId: 'tbRecibos',
            title: 'RECIBOS',
            autoScroll: true,
            loader: {
                url: _URL_LOADER_RECIBOS,
                scripts: true,
                autoLoad: false
            },
            listeners: {
                activate: function(tab) {
                    tab.loader.load({
                        params : {
                            'params.cdunieco': panelBusqueda.down('form').getForm().findField("params.cdunieco").getValue(),
                            'params.cdramo'  : panelBusqueda.down('form').getForm().findField("params.cdramo").getValue(),
                            'params.estado'  : panelBusqueda.down('form').getForm().findField("params.estado").getValue(),
                            'params.nmpoliza': panelBusqueda.down('form').getForm().findField("params.nmpoliza").getValue()//,
                            //'params.nmsuplem': panelBusqueda.down('form').getForm().findField("params.suplemento").getValue()
                        }
                    });
                }
            }
        }]    
    });
    
    
    
    /**INFORMACION DE AGENTES EN POLIZA**/
    // Modelo
    Ext.define('AgentesPolModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdagente'},
            {type:'string', name:'nombre'},
            {type:'string', name:'cdtipoAg'},
            {type:'string', name:'descripl'},
            {type:'string', name:'porredau'},
            {type:'string', name:'porparti'}
        ]
    });
    
    // Store
    var storeAgentesPol = new Ext.data.Store({
     model: 'AgentesPolModel',
     proxy:
     {
          type: 'ajax',
          url : _URL_CONSULTA_AGENTES_POLIZA,
      reader:
      {
           type: 'json',
           root: 'agentesPoliza'
      }
     }
    });
    
    
    var gridAgentesPoliza = Ext.create('Ext.grid.Panel', {
        title   : 'Agentes en la P&oacute;liza',
        store   : storeAgentesPol,
//        width   : 830,
//        autoScroll:true,
        columns: [
            {text:'Agente',dataIndex:'cdagente', width:80},
            {text:'Nombre',dataIndex:'nombre', width:300},
            {text:'Tipo Agente',dataIndex:'descripl',width:150},
            {text:'Cesi&oacute;n de Comisi&oacute;n',dataIndex:'porredau',width:150},
            {text:'Participaci&oacute;n',dataIndex:'porparti',width:150}
        ]
    });
    
    // INFORMACION DEL GRID DE CONSULTA DE RECIBOS DEL AGENTE:
    //Modelo
    Ext.define('RecibosAgenteModel',{
    extend: 'Ext.data.Model',
    fields: [
        {type:'string',    name:'dsgarant'},
        {type:'date',    name:'fefin',    dateFormat: 'd/m/Y'},
        {type:'date',    name:'feinicio', dateFormat: 'd/m/Y'},
        {type:'string',    name:'nmrecibo'},
        {type:'float',    name:'ptimport'}
    ]
    });

    //Store    
    var storeRecibosAgente = new Ext.data.Store({
        model: 'RecibosAgenteModel',
        groupField: 'dsgarant',
        proxy: {
            type: 'ajax',     
            url : _URL_CONSULTA_RECIBOS_AGENTE,
            reader: {
                type: 'json',
                root: 'recibosAgente'
            }
        }
    });
    
    
    var totalMontoRecibos = Ext.create('Ext.toolbar.Toolbar',{
        buttonAlign:'center',
        width   : 450,
        style:'color:white;font-weight:bold;',
        items:
        [
            {xtype: 'label', text: 'Monto Total '},
            '->',
            {id:'montoTotalRecibos', xtype: 'label' }
        ]
    });
    
        
    var consultaRecibosAgente = Ext.create('Ext.grid.Panel', {
        title   : 'Ver desglose de coberturas:',
        store   : storeRecibosAgente,
        //autoScroll :true,
        //isExpanded : true,
        collapsible: true,
        collapsed:true,
        width   : 450,
        //height: 250,
        columns: [                   
            {header: 'N&uacute;mero de Recibo',dataIndex: 'nmrecibo', width:200},
            {header: 'Importe', dataIndex:'ptimport', width:250 , align:'right', renderer: Ext.util.Format.usMoney, summaryType: 'sum',summaryRenderer: function(value){ return Ext.String.format('Total     {0}',Ext.util.Format.usMoney( value)); }}
        ],
        features: [{ groupHeaderTpl: '{name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})', ftype:'groupingsummary', summaryType: 'sum', startCollapsed :true }]
    });
    
        
    var tabPanelAgentes = Ext.create('Ext.tab.Panel', {
        border : false,
        defaults: {
            border:false
        },
        items: [{
            title : 'DATOS DEL AGENTE',
            items:[gridAgentesPoliza]
        }, {
            title: 'RECIBOS DEL AGENTE',
            autoScroll:true,            
            items:[totalMontoRecibos, consultaRecibosAgente]
            
        }]
    });
    
    
    var elementosTbar  = [
                          {
                       	   xtype      : 'textfield',
                       	   fieldLabel : '<div style="color: white">Nombre</div>',
//                       	   style      : 'margin:5px',
//                       	   labelWidth : 100,
//                              width      : 300,
                              heigth	  : 10,
                              maxLength  : 50,
                              id		  : 'nombreAsegurado'
                          },
                          {
                       	   xtype   : 'button', 
                       	   text    : 'Buscar',
                       	   style   : 'margin:5px',
                       	   icon    : _CONTEXT+'/resources/fam3icons/icons/zoom.png',
                       	   id      : 'buscarAsegurado',
                              handler : function(){
                                  var formBusqueda = panelBusqueda.down('form').getForm();
                                  var params = formBusqueda.getValues();
                                  params['params.nombre'] = Ext.getCmp('nombreAsegurado').getValue();                    
                                  debug('params',params);
                                  cargaStoreSuplementos(params);
                           	 }   
                          }
                         ];
    
    elementosTbar = elementosTbar.concat(_botonesGrid);
    
    // SECCION DE INFORMACION GENERAL:
    
    var panelBusqueda = Ext.create('Ext.Panel', {
        id:'main-panel',
        baseCls:'x-plain',
        renderTo: 'dvConsultasPolizas',
        layout: {
            type: 'column',
            columns: 2
        },
        autoScroll:true,
        defaults: {frame:true, width:200, height: 200, margin : '2'},
        items:[{
            title:'B&Uacute;SQUEDA DE P&Oacute;LIZAS',
            colspan:2,
            collapsible:true,
            width:990,
            height:90,
            items: [
                {
                    xtype: 'form',
                    url: _URL_CONSULTA_DATOS_SUPLEMENTO,
                    border: false,
                    layout: {
                        type:'hbox'
                    },
                    margin: '5',
                    defaults: {
                        bubbleEvents: ['change']
                    },
                    items : [
                        {
                            xtype       : 'combo',
                            name        : 'params.tipoBusqueda',
                            queryMode   : 'local',
                            valueField  : 'key',
                            displayField: 'value',
                            flex : 17,
                            columns: 1,
                            store: storeTiposBusqueda,
                            listeners: {
                            	render : function(combo, eOpts ) {
                            		combo.select(combo.getStore().getAt(0)); // Seleccionar primer elemento
                            	},
                            	change : function(combo, newValue, oldValue, eOpts) {
                            		Ext.getCmp('subpanelBusquedas').query('panel').forEach(function(c){c.hide();});
                                    this.up('form').getForm().findField('params.rfc').setValue('');
                                    this.up('form').getForm().findField('params.cdperson').setValue('');
                                    this.up('form').getForm().findField('params.nombre').setValue('');
                                    var nombreAsegurado = Ext.getCmp('nombreAsegurado');
                                    var buscarAsegurado = Ext.getCmp('buscarAsegurado');
                                    console.log('newValue', newValue);                                    
                                    switch (newValue) {
                                        case 1:
                                            nombreAsegurado.setVisible(true);
                                            buscarAsegurado.setVisible(true);
                                            Ext.getCmp('subpanelBusquedaGral').show();
                                            break;
                                        case 2:
                                        	nombreAsegurado.setVisible(false);
                                            buscarAsegurado.setVisible(false);
                                            Ext.getCmp('subpanelBusquedaRFC').show();
                                            break;
                                        case 3:
                                            nombreAsegurado.setVisible(false);
                                            buscarAsegurado.setVisible(false);
                                            Ext.getCmp('subpanelBusquedaCodigoPersona').show();
                                            break;
                                        case 4:
                                            nombreAsegurado.setVisible(false);
                                            buscarAsegurado.setVisible(false);
                                            Ext.getCmp('subpanelBusquedaNombre').show();
                                            break;
                                        case 5:
                                        	nombreAsegurado.setVisible(true);
                                            buscarAsegurado.setVisible(true);
                                            Ext.getCmp('subpanelBusquedaPolCorto').show();
                                            break;
                                    }
                            	}
                            }
                        },
                        {
                            id    : 'subpanelBusquedas',
                            layout: 'vbox',
                            align : 'stretch',
                            flex  : 76,
                            border: false,
                            items: [
                                {
                                    id: 'subpanelBusquedaGral',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    defaults: {
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            // Numero de poliza externo
                                            xtype : 'textfield',
                                            name : 'params.nmpoliex',
                                            fieldLabel : 'N&uacute;mero de P&oacute;liza',
                                            labelWidth : 120,
                                            width: 300,
                                            maxLength : 30,
                                            allowBlank: false
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.cdunieco'
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.cdramo'
                                        },{
                                            xtype : 'hiddenfield',
                                            name : 'params.estado'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.nmpoliza'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.suplemento'
                                        },{
                                            xtype: 'hiddenfield',
                                            name : 'params.cdagente'
                                        }
                                    ]
                                },
                                {
                                    id: 'subpanelBusquedaRFC',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    hidden: true,
                                    defaults: {
                                        labelAlign: 'right',
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            xtype: 'textfield',
                                            name : 'params.rfc',
                                            fieldLabel : 'RFC',
                                            maxLength : 13,
                                            allowBlank: false
                                        }
                                    ]
                                },
                                {
                                    id: 'subpanelBusquedaCodigoPersona',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    hidden: true,
                                    defaults: {
                                        labelAlign: 'right',
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            xtype: 'numberfield',
                                            name : 'params.cdperson',
                                            fieldLabel : 'Clave de asegurado',
                                            labelWidth: 120,
                                            maxLength : 9,
                                            allowBlank: false,
                                            minValue: 0
                                        }
                                    ]
                                },
                                {
                                    id: 'subpanelBusquedaNombre',
                                    layout : 'hbox',
                                    align:'stretch',
                                    border: false,
                                    hidden: true,
                                    defaults: {
                                        labelAlign: 'right',
                                        enforceMaxLength: true,
                                        msgTarget: 'side'
                                    },
                                    items : [
                                        {
                                            xtype: 'textfield',
                                            name : 'params.nombre',
                                            fieldLabel : 'Nombre',
                                            maxLength : 255,
                                            allowBlank: false
                                        }
                                    ]
                                },
                                {
                                    id: 'subpanelBusquedaPolCorto',
                                    layout : 'hbox',
                                    align:'left',
                                    border: false,
                                    hidden: true,
                                    defaults: {
                                        labelAlign: 'right'
                                    },
                                    items : [
                                        {
                                            xtype: 'combo',
                                            //width: 280,
                                            name : 'params.sucursal',
                                            fieldLabel : 'Sucursal',
                                            allowBlank: false,
                                            typeAhead:true,
											anyMatch:true,
											displayField:'value',
											valueField:'key',
											forceSelection:true,
											editable:true,
											queryMode:'local',
											store:Ext.create('Ext.data.Store', {
    											model:'Generic',
    											autoLoad:true,
    											proxy: {
                                                type:'ajax',
        											url: _URL_CARGA_CATALOGO,
        											reader:{
                                                        type:'json',
                                                        root:'lista',
                                                        rootProperty:'lista'
                                                    },
                                                    extraParams:{
    												    catalogo:'MC_SUCURSALES_SALUD'
                                                    }
                                                }
											})
                                        },{
                                            xtype: 'combo',
                                            //width: 280,
                                            name : 'params.producto',
                                            fieldLabel : 'Producto',
                                            allowBlank: false,
                                            typeAhead:true,
											anyMatch:true,
											displayField:'value',
											valueField:'key',
											forceSelection:true,
											editable:true,
											queryMode:'local',
											store:Ext.create('Ext.data.Store',{
    											model:'Generic',
    											autoLoad:true,
    											proxy: {
    												type:'ajax',
        											url: _URL_CARGA_CATALOGO,
        											reader:{
        												type:'json',
        												root:'lista',
        												rootProperty:'lista'
                                                    },
                                                    extraParams:{
                                                        catalogo:'RAMOS'
                                                    }
    											}
											})
                                        },{
                                            xtype: 'textfield',
                                            name : 'params.numpolizacorto',
                                            width: 200,
                                            fieldLabel : 'P&oacute;liza',
                                            allowBlank: false
                                        },{
                                        	xtype: 'textfield',
                                        	name : 'params.cdsisrol',
                                        	value: cdSisRolActivo,
                                        	hidden: true
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'button',
                            flex : 6,
                            text : 'Buscar',
                            handler: function(btn, e) {
                            	panelBusqueda.setLoading(true);
                                var formBusqueda = this.up('form').getForm();
                                switch (formBusqueda.findField('params.tipoBusqueda').getValue()) {
                                    case 1:
                                        // Busqueda por Datos Generales de la poliza:
                                        if(!formBusqueda.findField('params.nmpoliex').isValid()){
                                            showMessage('', _MSG_NMPOLIEX_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        debug('busqueda2 ',formBusqueda.getValues());
                                        cargaStoreSuplementos(formBusqueda.getValues());
                                    break;
                                        
                                    case 2:
                                        // Busqueda de polizas por RFC:
                                        if(!formBusqueda.findField('params.rfc').isValid()){
                                            showMessage('', _MSG_RFC_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        } 
                                        cargaPolizasAsegurado(formBusqueda, btn);
                                    break;
                                    
                                    case 3:
                                        // Busqueda de polizas por CDPERSON:
                                        if(!formBusqueda.findField('params.cdperson').isValid()){
                                            showMessage('', _MSG_CDPERSON_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        cargaPolizasAsegurado(formBusqueda, btn);
                                        
                                    break;
                                    
                                    case 4:
                                        // Busqueda de polizas por nombre:
                                        if(!formBusqueda.findField('params.nombre').isValid()){
                                            showMessage('', _MSG_NOMBRE_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }
                                        cargaPolizasAsegurado(formBusqueda, btn);
                                        
                                    break;
                                    
                                    case 5:
                                        // Busqueda de polizas por numero corto:
                                   		if(!formBusqueda.findField('params.sucursal').isValid() || !formBusqueda.findField('params.producto').isValid() || !formBusqueda.findField('params.numpolizacorto').isValid()){
                                            mensajeWarning('Llene los datos requeridos.');
                                            panelBusqueda.setLoading(false);
                                            return;
                                        }                                        
                                       // debug('storeSuplementos',storeSuplementos.getAt(0).get('switchConvenios'));
                                        cargaStoreSuplementos(formBusqueda.getValues());
                                        
                                    break;
                                }
                            }
                        }
                    ]
                },{
                    layout: 'column',
                    margin: '5',
                    border: false,
                    name: 'avisos',
                    html:''
                 }
            ]
        },
        {
            title:'HIST\u00D3RICO DE MOVIMIENTOS',
            width:990,
            height:220,
            colspan:2,
            autoScroll : true,
            collapsible: true,
            items : [
                gridSuplementos
            ],
            tbar: elementosTbar
        },
        {
            //title:
            width: 130,
            height: 400,
            items: [
                listViewOpcionesConsulta
            ]
        },
        {
            //title:
            width:850,
            height:400,
            colspan:2,
            autoScroll:true,
            items : [
                tabDatosGeneralesPoliza,
                tabPanelAgentes
            ]
        }]
    });
    
    
    ////Hide elements
    if(tabDatosGeneralesPoliza.isVisible()) {
        tabDatosGeneralesPoliza.hide();
    }
    if(tabPanelAgentes.isVisible()) {
        tabPanelAgentes.hide();
    }
    

    /**
    * 
    * @param String/Object
    */
    function cargaStoreSuplementos(params){
        
        debug('Params busqueda de suplemento: ',params);
        
        storeSuplementos.load({
            params: params,
        	callback: function(records, operation, success) {
        		panelBusqueda.setLoading(false);
                if(success) {                	
                	//debug('storeSuplementos ',storeSuplementos.getAt(0).get('switchConvenios'));
                    gridSuplementos.setLoading(false);
                    
                    //Limpiar seleccion de la lista de opciones de consulta:
                    limpiaSeleccionTiposConsulta();
                    if( !Ext.isEmpty(records) ) {
                    params['params.switchConvenios'] = records[0].get('switchConvenios');
                    //debug("switch convenios",params.switchConvenios);
                    obtieneAvisosPoliza(params);
                    }
                    
                } else {
                    showMessage('Error', 'Error al obtener el hist\u00F3rico de la p\u00F3liza', Ext.Msg.OK, Ext.Msg.ERROR);
                }
            }
        });
    }
    
    
    /**
     * Mostrar avisos de la poliza:
     */
    function obtieneAvisosPoliza(params) {
    	// Avisos:
        Ext.Ajax.request({
        	url       : _URL_CONSULTA_DATOS_MENSAJES,       	
            params    : params,
            callback  : function (options, success, response){
                if(success){
                    var jsonResponse = Ext.decode(response.responseText);
                    //debug("params",params);
                    if(!Ext.isEmpty(jsonResponse.mensajeRes || !Ext.isEmpty(jsonResponse.mensajeConv))){
                    	panelBusqueda.down('[name=avisos]').update('<span style="color:#E96707;font-size:16px;font-weight:bold;">'
                    												+jsonResponse.mensajeRes
                    												+'</br>'
                    												+jsonResponse.mensajeConv
                    												+'</span>');
                        panelBusqueda.down('[name=avisos]').animate({duration:500, to: {opacity: 1}}).animate({to: {opacity: 0}})
                            .animate({duration:500, to: {opacity: 1}}).animate({to: {opacity: 0}})
                            .animate({duration:500, to: {opacity: 1}}).animate({to: {opacity: 0}})
                            .animate({duration:500, to: {opacity: 1}});
                    }else {
                        panelBusqueda.down('[name=avisos]').update('<span></span>');
                    }
                }else{
                    showMessage('Error', 'Error al obtener los avisos.', Ext.Msg.OK, Ext.Msg.ERROR);
                }
            }
        });
    }
	
    // FUNCION PARA OBTENER RECIBOS DEL AGENTE
    function obtieneMontosRecibo(records) {
        var sum=0;
        for(var i=0;i<records.length;i++) {
            sum+=parseFloat(records[i].get("ptimport"));
        }
        return Ext.util.Format.usMoney(sum);
    }
    
    function limpiaSeccionInformacionPrincipal() {
        panelDatosPoliza.getForm().reset();
        //gridDatosTarificacion.getStore().removeAll();
        gridCopagosPoliza.getStore().removeAll();
        gridDatosAsegurado.getStore().removeAll();
        //Limpiamos seccion de Datos de tatrisit:
        tabDatosGeneralesPoliza.setActiveTab(0);
        tabPanelAgentes.setActiveTab(0);
        tabDatosGeneralesPoliza.hide();
        tabPanelAgentes.hide();
    }
    
    function limpiaSeleccionTiposConsulta() {
        listViewOpcionesConsulta.getSelectionModel().deselectAll();
        listViewOpcionesConsulta.collapse('top', false);
    }
    
    function cargaPolizasAsegurado(formBusqueda, btn) {
        gridPolizasAsegurado.down('pagingtoolbar').moveFirst();
        var callbackGetPolizasAsegurado = function(options, success, response) {
        	panelBusqueda.setLoading(false);
            if(success){
                var jsonResponse = Ext.decode(response.responseText);
                if(jsonResponse.polizasAsegurado && jsonResponse.polizasAsegurado.length == 0) {
                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                    return;
                }
                windowPolizas.animateTarget= btn;
                windowPolizas.show();
            }else{
                showMessage('Error', 'Error al obtener las p&oacute;lizas, intente m\u00E1s tarde.', 
                    Ext.Msg.OK, Ext.Msg.ERROR);
            }
        }
        //console.log('Params busqueda de polizas por RFC=');console.log(formBusqueda.getValues());
        cargaStorePaginadoLocal(storePolizaAsegurado, 
            _URL_CONSULTA_POLIZAS_ASEGURADO, 
            'polizasAsegurado', 
            formBusqueda.getValues(), 
            callbackGetPolizasAsegurado);
    }
    
    function siniestralidad(cdunieco,cdramo, cdperson, nmpoliza, proceso){
		var windowHistSinies = Ext.create('Ext.window.Window',{
			modal       : true,
			buttonAlign : 'center',
			width       : 800,
			height      : 500,
			autoScroll  : true,
			loader      : {
				url     : _URL_LOADER_HISTORIAL_RECLAMACIONES,
				params  : {
					'params.cdperson'  : cdperson,
					'params.cdramo'    : cdramo,
					'params.nmpoliza'  : nmpoliza,
					'params.cdunieco'  : cdunieco,
					'params.proceso'  : proceso
					
				},
				scripts  : true,
				loadMask : true,
				autoLoad : true,
				ajaxOptions: {
					method: 'POST'
				}
			},
			buttons: [{
				icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
				text: 'Cerrar',
				handler: function() {
					windowHistSinies.close();
				}
			}]
		}).show();
		centrarVentana(windowHistSinies);
    }
    
//Genera reporte de Siniestros
    function _reporteEdoCta(values, nombre)
    {
    	debug('iniciando reporte...');
    	debug(_reporteEdoCtaSin);
    	debug(values);
        var me = this;
        var urlRequestViewRep = _urlViewReport 
                + '?destype=cache'
                + '&p_unieco='      + values['params.cdunieco']
                + '&p_ramo='        + values['params.cdramo']   
                + '&p_estado='      + values['params.estado']   
                + '&p_poliza='      + values['params.nmpoliza']
                + '&p_situac='      + values['params.nmsituac']
                + '&p_suplem='      + values['params.suplemento']
                + '&p_swfonsin='    + values['params.swfonsin']
        		+ "&desformat=PDF"
        		+ "&userid="        + _reportsServerUser
        		+ "&report="        + _reporteEdoCtaSin
        		+ "&paramform=no";                                             

        debug(urlRequestViewRep);
        var numRand = Math.floor((Math.random() * 100000) + 1);
        debug(numRand);
        var windowVerRep = Ext.create('Ext.window.Window',
        {
            title          : nombre
            ,width         : 800
            ,height        : 550
            ,collapsible   : true
            ,titleCollapse : true
            ,html : '<iframe innerframe="'
                    + numRand
                    + '" frameborder="0" width="100" height="100"'
                    + 'src="'
                    + _urlViewDoc 
                    + "?contentType=application/pdf&url="
                    + encodeURIComponent(urlRequestViewRep)
                    + "\">"
                    + '</iframe>'
            ,listeners :
            {
                resize : function(win,width,height,opt)
                {
                    debug(width,height);
                    $('[innerframe="'+ numRand+ '"]').attr(
                    {
                        'width'   : width - 11
                        ,'height' : height 
                    });
                }
            }
        }).show();
        windowVerRep.center();
    }   
    
    
    /**
     * Regenerar documentos por endoso
     */
    regeneraDocsConsulta = function (button, evt) {
    	
    	debug('<<<<<>>>>> Parametros regenrar documentos Endoso ::: ');
    	
    	var selRecord;
    	if(gridSuplementos.getSelectionModel().hasSelection()){
    		selRecord = gridSuplementos.getSelectionModel().getLastSelected();
    		debug('>>>>>> Record Seleccionado : ',selRecord);
    	}else{
    		mensajeWarning('Debe seleccionar un registro para esta acci&oacute;n');
    		return;
    	}
    	
    	_mask('Regenerando Documentos...');
        Ext.Ajax.request({
        	url       : _URL_REGENERA_DOCUMENTOS_ENDOSO,       	
            params    : {
            	'smap1.cdunieco' : selRecord.get('cdunieco'),
            	'smap1.cdramo' : selRecord.get('cdramo'),
            	'smap1.estado' : selRecord.get('estado'),
            	'smap1.nmpoliza' : selRecord.get('nmpoliza'),
            	'smap1.nmsuplem' : selRecord.get('nmsuplem'),
            	'smap1.nsuplogi' : selRecord.get('nsuplogi')
            },
            callback  : function (options, success, response){
            	_unmask();
                if(success){
                    var jsonResponse = Ext.decode(response.responseText);
                    
                    if(jsonResponse.success){
                    	mensajeCorrecto('Aviso','Ejecuci&oacuten correcta al regenerar documentos.');
                    }else{
                    	showMessage('Error', 'Error al regenerar documentos.', Ext.Msg.OK, Ext.Msg.ERROR);
                    }
                    
                }else{
                    showMessage('Error', 'Error al regenerar documentos.', Ext.Msg.OK, Ext.Msg.ERROR);
                }
            }
        });
    };

    /**
     * Reenviar recibos por suplemento
     */
    reenviaRecibosSuplemento = function (button, evt) {
    	
    	debug('<<<<<>>>>> Parametros reenviar recibos Endoso ::: ');
    	
    	var selRecord;
    	if(gridSuplementos.getSelectionModel().hasSelection()){
    		selRecord = gridSuplementos.getSelectionModel().getLastSelected();
    		debug('>>>>>> Record Seleccionado : ',selRecord);
    	}else{
    		mensajeWarning('Debe seleccionar un registro para esta acci&oacute;n');
    		return;
    	}
    	
    	_mask('Regenerando Recibos...');
    	Ext.Ajax.request({
    		url       : _URL_RECIBOS_ENDOSO,       	
    		params    : {
    			'map1.cdunieco' : selRecord.get('cdunieco'),
    			'map1.cdramo' : selRecord.get('cdramo'),
    			'map1.estado' : selRecord.get('estado'),
    			'map1.nmpoliza' : selRecord.get('nmpoliza'),
    			'map1.nmsuplem' : selRecord.get('nmsuplem'),
    			'map1.nsuplogi' : selRecord.get('nsuplogi')
    		},
    		callback  : function (options, success, response){
    			_unmask();
    			if(success){
    				var jsonResponse = Ext.decode(response.responseText);
    				
    				if(jsonResponse.success){
    					mensajeCorrecto('Aviso','Ejecuci&oacuten correcta al regenerar recibos.');
    				}else{
    					showMessage('Error', 'Error al regenerar recibos. ' + jsonResponse.respuesta, Ext.Msg.OK, Ext.Msg.ERROR);
    				}
    				
    			}else{
    				showMessage('Error', 'Error al regenerar recibos. Error de conexi&oacute;n.', Ext.Msg.OK, Ext.Msg.ERROR);
    			}
    		}
    	});
    };
    
    /**
     * Perfil Medico: Consulta lista de ICDs
     */
    function _consultaIcds(values, nombre)
    {
    	debug(">_consultaIcds");
        
    	var me = this;
        debug("values ", values, "nombre ", nombre, "this ", this);
        var urlRequestViewRep = _URL_CONSULTA_ICDS + '?params.cdperson=' + values['params.cdperson'];
        debug(urlRequestViewRep);

    	// Se crea ventana para mostrar el detalle del asegurado:
    	Ext.create('Ext.window.Window',
                                        {
                                            title        : 'E.C.D.'
                                            //,modal       : true
                                            ,buttonAlign : 'center'
                                            ,width       : 800
                                            ,height      : 300
                                            ,autoScroll  : true
                                            ,loader      :
                                            {
                                                url       : _URL_CONSULTA_ICDS
                                                ,scripts  : true
                                                ,autoLoad : true
                                                ,loadMask : true
                                                ,ajaxOptions: {
                                                    method   : 'POST'
                                                },
                                                params: {
                                                    'params.cdperson':values['params.cdperson']
                                                }
                                            }
                                        }).show();
        
        debug("<_consultaIcds");
    }   

    

    /**
     * Perfil Medico: funcion para cambiar el icono del grid
     */
    
    
});
