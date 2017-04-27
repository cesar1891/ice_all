Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {
	//Se establece un timeout de 2 min.
	Ext.Ajax.timeout = 120000;
	
	Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });
    
    // Conversion para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
    
    /***MODELS***/
   //Definición del Key Value Model
    Ext.define('KeyValueModel', {
        extend:'Ext.data.Model',
        fields:['key','value']
    });
            
   /*
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
    */
    /*
    Ext.define('ReclamoExpressModel', {
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
            {name: 'fefinval', dateFormat: 'd/m/Y'},
            {name: 'nlogisus'},
            {name: 'nsuplogi', type:'int'},
            {name: 'ptpritot', type : 'float'},
            {name: 'estatus'},
            {name: 'origen'}
        ]
    });
    */
    
    Ext.define('ReclamoExpressModel', {
    	extend: 'Ext.data.Model',
    	fields: [
    		{name: 'asegurado'},
    		{name: 'clavePoliza'},
    		{name: 'claveReclamo'},
            {name: 'cliente'},
            {name: 'destino'},
            {name: 'destinoNombre'},
            {name: 'factura'},
            {name: 'fechaAplicacion'},
            {name: 'fechaCaptura'},
            {name: 'fechaFactura'},
            {name: 'fechaPago'},
            {name: 'fechaProcesamiento'},
            {name: 'idAsegurado'},
            {name: 'idCliente'},
            {name: 'idProveedor'},
            {name: 'idSESAS'},
            {name: 'idTipoServicio'},
            {name: 'importe'},
            {name: 'isr'},
            {name: 'iva'},
            {name: 'isr'},
            {name: 'ivaRetenido'},
            {name: 'modo'},
            {name: 'poliza'},
            {name: 'proveedor'},
            {name: 'proveedorRfc'},
            {name: 'ramo'},
            {name: 'reclamo'},
            {name: 'referencia'},
            {name: 'secuencial'},
            {name: 'siniestro'},
            {name: 'siniestroSerie'},
            {name: 'solicitudCxp'},
            {name: 'sucursal'},
            {name: 'sucursalNombre'},
            {name: 'tipoPago'},
            {name: 'tipoReclamo'},
            {name: 'tipoServicio'}            
            
    	]
    });
    
    Ext.define('RegistroDetalleModel', {
        extend:'Ext.data.Model',
        fields:[
            {name: 'tipprc'},
            /*{name: 'cveprc'}, REVISAR SI SE OCUPARA*/
            {name: 'nomprc'},
            {name: 'nomcob'},
            {name: 'nomsub'},
            {name: 'imppag'}            
        ]
    }); 
    
    Ext.define('ConceptosModel',{
    	extend: 'Ext.data.Model',
    	fields: [
            {type:'string', name:'idconcep'},
            {type:'string', name:'cdconcep'},
            {type:'string', name:'dsconcep'},
            {type:'string', name:'cdgarant'},
            {type:'string', name:'dsgarant'},
            {type:'string', name:'cdconval'},
            {type:'string', name:'dsconval'},
            {type:'float', name:'importe'}
        ]
    });
    
    Ext.define('ListadoCoberturaModel',{
    	extend: 'Ext.data.Model',
    	fields: [
    	   {type:'string',    name:'cdgarant'},
    	   {type:'string',    name:'dsgarant'},
    	   {type:'string',    name:'ptcapita'}
    	]
    });
    /***FIN MODELS***/
    
    /***STORES***/
    //Definición del store de Nivel de Reclamo: General o Detalle
    var storeNivelReclamo = Ext.create('Ext.data.JsonStore', {
        model: 'KeyValueModel',
        proxy: {
            type: 'ajax',
            url: _URL_NIVEL_RECLAMO,
            reader: {
                type: 'json',
                root: 'nivelReclamo'
            }
        }
    });
    storeNivelReclamo.load();
    
    //Lista de reclamaciones express 
    var storeReclamos = new Ext.data.Store({
        model      : 'Generic',
        //autoLoad: true,
        proxy     : {
            type        : 'ajax',
            url         : _URL_RECLAMOS,
            reader : {
                type : 'json',
                root : 'datosReclamos'
            }
        }
        
    });
    storeReclamos.load();
    
    //Lista de secuenciales
    
    var storeSecuencias = Ext.create('Ext.data.Store', {
    	model      : 'Generic',
    	autoLoad: false,
    	proxy :{
    		type        : 'ajax',
    		url         : _URL_SECUENCIALES,
    		reader : {
                type : 'json',
                root : 'datosSecuenciales'
            }
    	}
    });
    
    //Datos de la reclamación
    var storeReclamoExpress = new Ext.data.Store({        
        model: 'ReclamoExpressModel',
        autoLoad: false,
        proxy    : {
            type: 'ajax',
            url : _URL_CONSULTA_RECLAMO_EXPRESS,
            reader: {
                type: 'json',
                root: 'reclamoExpress'
            }            
        }
    });
    
    //Conducto
    var storeConducto = Ext.create('Ext.data.Store', {
        model      : 'Generic',
        data : [            
            {"key":"PROGRAMADA*", "value":"PROGRAMADA*"},
            {"key":"REFERENCIA OTRO HOSPITAL", "value":"REFERENCIA OTRO HOSPITAL"},
            {"key":"POR URGENCIA *", "value":"POR URGENCIA *"},
            {"key":"PARA SERVICIOS*", "value":"PARA SERVICIOS*"},
            {"key":"S/R", "value":"S/R"}
        ]
    });
    
    //Causa del reclamo
    var storeCausa = Ext.create('Ext.data.Store', {
        model      : 'Generic',
        data : [            
            {"key":"ACCIDENTE", "value":"ACCIDENTE"},
            {"key":"ENFERMEDAD", "value":"ENFERMEDAD"},
            {"key":"EMBARAZO", "value":"EMBARAZO"},
            {"key":"NO ESPECIFICADO", "value":"NO ESPECIFICADO"}            
        ]
    });
    
    //Atención 
    var storeAtencionHosp = Ext.create('Ext.data.Store', {
        model      : 'Generic',
        data : [            
            {"key":"AMBULATORIA", "value":"AMBULATORIA"},
            {"key":"NO QUIRURGÍCO", "value":"NO QUIRURGÍCO"},
            {"key":"OBTÉTRICA", "value":"OBTÉTRICA"},
            {"key":"QUIRURGÍCO", "value":"QUIRURGÍCO"},
            {"key":"S/R", "value":"S/R"}
        ]
    });
    
    //Motivo Egreso
    var storeMotivoEgreso = Ext.create('Ext.data.Store', {
        model      : 'Generic',
        data : [            
            {"key":"CURACIÓN", "value":"CURACIÓN"},
            {"key":"MEJORÍA", "value":"MEJORÍA"},
            {"key":"VOLUNTARIO", "value":"VOLUNTARIO"},
            {"key":"PASA A OTRO HOSPITAL", "value":"PASA A OTRO HOSPITAL"},
            {"key":"DEFUNCIÓN", "value":"DEFUNCIÓN"},
            {"key":"S/R", "value":"S/R"}            
        ]
    });
    
    //ICD
    var storeTiposICD = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_LISTA_CPTICD,
            extraParams:{
                'params.cdtabla' : '2TABLICD'
            },
            reader: {
                type: 'json',
                root: 'listaCPTICD'
            }
        }
    });
    
    //CPT
    var storeTiposCPT1 = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_LISTA_CPTICD,
            extraParams:{
                'params.cdtabla' : '2TABLCPT'
            },
            reader: {
                type: 'json',
                root: 'listaCPTICD'
            }
        }
    });
    
    var storeTiposCPT2 = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_LISTA_CPTICD,
            extraParams:{
                'params.cdtabla' : '2TABLCPT'
            },
            reader: {
                type: 'json',
                root: 'listaCPTICD'
            }
        }
    });
    
    var storeTiposCPT3 = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_LISTA_CPTICD,
            extraParams:{
                'params.cdtabla' : '2TABLCPT'
            },
            reader: {
                type: 'json',
                root: 'listaCPTICD'
            }
        }
    });
    
    var storeRegistroDetalle = Ext.create('Ext.data.Store', {
        //storeId:'simpsonsStore',
    	autoDestroy: true,
        model: 'RegistroDetalleModel'
    });
    
    //Conceptos del detalle
    storeConceptos=new Ext.data.Store(
        {
        	autoDestroy: true,
        	model: 'ConceptosModel',
        	proxy: {
        		type: 'ajax',
        		url: _URL_LoadConceptos,
        		reader: {
                    type: 'json',
                    root: 'loadList'
                }
        	}
        }
    );
    
    //Tipo de concepto
    //Se deja fijo para evitar ir a la DB.
    var storeTipoConcepto = Ext.create('Ext.data.JsonStore', {
    	model:'Generic',
    	data:[
    	   {"key":"1", "value":"CPT"},
    	   {"key":"2", "value":"HCPC"},
    	   {"key":"3", "value":"UB"}
    	   
    	]    	
    });
   
    //Conceptos Catálogo
    var storeConceptosCatalogo = Ext.create('Ext.data.JsonStore', {
    	model:'Generic',
    	proxy: {
    		type: 'ajax',
    		url: _URL_CATALOGOS,
    		extraParams : {catalogo:_CATALOGO_ConceptosMedicos},
    		reader: {
                type: 'json',
                root: 'lista'
            }
    	}
    });
    
    //Store Cobertura
    var storeCobertura = Ext.create('Ext.data.Store', {
    	model:'ListadoCoberturaModel',
    	autoLoad:false,
    	proxy: {
    		type: 'ajax',
            url : _URL_LISTA_COBERTURA,
            /*Parametros iniciales. Después se actualizan con los de la póliza*/
            extraParams: {
            	'params.cdunieco' : '1013',
                'params.cdramo'   : '2',
                'params.estado'   : 'M',
                'params.nmpoliza' : '6',
                'params.nmsituac' : '1'
            },
            reader: {
                type: 'json',
                root: 'listaCoberturaPoliza'
            }
    	}
    });
   
    
    //Store subcobertura
    var storeSubcobertura= Ext.create('Ext.data.Store', {
    	model:'Generic',
        autoLoad:false,
         proxy: {
            type: 'ajax',
            url : _URL_LISTA_SUBCOBERTURA,
            reader: {
                type: 'json',
                root: 'listaSubcobertura'
            }
        }
    });
    
    
    
    /***Fin STORES***/
    
    
    /***NIVELES DE RECLAMO (OPCIONES)***/
    var listViewNivelReclamo = Ext.create('Ext.grid.Panel', {
    	collapsible:false,
    	collapsed:false,
    	store: storeNivelReclamo,
    	multiSelect: false,
        hideHeaders:true,
        viewConfig: {
            emptyText: 'No hay niveles de reclamo'
        },
        columns: [
        	{
                flex: 1,
                dataIndex: 'value'
            }
        ]
        /*Faltan listeners*/        
    });
    /***FIN NIVELES DE RECLAMO (OPCIONES)***/
    
    /***FORMULARIOS***/
    
    /***FORMULARIO DATOS DE LA RECLAMACIÓN***/
    var panelDatos = Ext.create('Ext.form.Panel',{
        model:'ReclamoExpressModel',        
        /*width:815,*/
        width:950,
        border: false,
        defaults : {
            bodyPadding : 5,
            border : false
        },
        items:[
            {
            	layout:'hbox',
            	items:[
            	   {xtype: 'tbspacer', width:25},
            	   {xtype: 'datefield', name: 'fechaCaptura', fieldLabel: 'Fecha Captura', fieldAlign:'left',   format: 'd/m/Y', readOnly: true, labelWidth: 100, labelAlign:'left', width: 200},            	   
            	   {xtype: 'tbspacer', width:10},
            	   {xtype: 'textfield', name: 'clavePoliza',   fieldLabel: 'Clave Poliza',      readOnly: true, labelWidth: 100,  width: 250, labelAlign: 'right'},
            	   {xtype: 'tbspacer', width:10},
            	   {xtype: 'textfield', name: 'claveReclamo',   fieldLabel: 'Clave Reclamo',      readOnly: true, labelWidth: 100,  width: 250, labelAlign: 'right'},
            	   {xtype: 'tbspacer', width:10},
            	   {xtype: 'textfield', name: 'ramo',   fieldLabel: 'Ramo',      readOnly: true, labelWidth: 60,  width: 150, labelAlign: 'right'}
            	]
            },
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                   {xtype: 'textfield', name: 'sucursal',   fieldLabel: 'Sucursal',      readOnly: true, labelWidth: 60,  width: 120, labelAlign: 'left'},
                   {xtype: 'textfield', name: 'sucursalNombre',   fieldLabel: '',      readOnly: true, width: 500, labelAlign: 'right'},
                   /*{xtype: 'tbspacer', width:100},*/
                   {xtype: 'textfield', name: 'poliza',   fieldLabel: 'Póliza',      readOnly: true, labelWidth: 120,  width: 260, labelAlign: 'right'}                 
                ]
            },
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {xtype: 'textfield', name: 'idCliente',   fieldLabel: 'Cliente',      readOnly: true, labelWidth: 60,  width: 120, labelAlign: 'left'},
                    {xtype: 'textfield', name: 'cliente',   fieldLabel: '',      readOnly: true, width: 500, labelAlign: 'right'},                   
                    /*{xtype: 'tbspacer', width:100},*/
                   	{xtype: 'textfield', name: 'idSESAS',   fieldLabel: 'ID SESAS',      readOnly: true, labelWidth: 60,  width: 260, labelAlign: 'right'}                                    
                ]
            },
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                   {xtype: 'textfield', name: 'idAsegurado',   fieldLabel: 'Asegurado',      readOnly: true, labelWidth: 60,  width: 120, labelAlign: 'left'},                   
                   {xtype: 'textfield', name: 'asegurado',   fieldLabel: '',      readOnly: true, width: 500, labelAlign: 'right'}
                   /*{xtype: 'tbspacer', width:100},*/
                   /*{
                        xtype: 'radiogroup',                                
                        name: 'groupTipAdm',
                        fieldLabel: 'Género',
                        labelAlign: 'right',

                        columns: 2,
                        vertical: false,
                        items:[
                            {boxLabel:'Masculino', name:'', inputValue: 10, width:90},
                            {boxLabel:'Femenino', name:'', inputValue: 9, width:90}
                        ]                        
                        
                        
                    }*/                    
                ]
            },                        
            {
                layout:'hbox',
                items:[                    
                    {xtype: 'tbspacer', width:25},
                   {xtype: 'textfield', name: 'factura',   fieldLabel: 'No. Factura',      readOnly: true, labelWidth: 100,  width: 260, labelAlign: 'left'},
                   {xtype: 'tbspacer', width:50},
                   {xtype: 'datefield', name: 'fechaFactura', fieldLabel: 'Fecha Factura',    format: 'd/m/Y', readOnly: true, labelWidth: 120, labelAlign: 'right', width: 260},
                   {xtype: 'tbspacer', width:50},
                   {xtype: 'textfield', name: 'importe',   fieldLabel: 'Importe',      readOnly: true, labelWidth: 100,  width: 260, labelAlign: 'right'}
                   
                ]
            },
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                   {xtype: 'textfield', name: 'idProveedor',   fieldLabel: 'Proveedor',      readOnly: true, labelWidth: 60,  width: 120, labelAlign: 'left'},
                   {xtype: 'textfield', name: 'proveedor',   fieldLabel: '',      readOnly: true, width: 500, labelAlign: 'right'},
                   {xtype: 'tbspacer', width:30},
                   {xtype: 'textfield', name: 'proveedorRfc',   fieldLabel: 'RFC',      readOnly: true, labelWidth: 60,  width: 230, labelAlign: 'right'}                                    
                ]
            },
           {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {xtype: 'datefield', name: 'fechaProcesamiento', fieldLabel: 'Fecha Procesamiento',    format: 'd/m/Y', readOnly: true, labelWidth: 130, width: 260},
                    {xtype: 'tbspacer', width:30},
                    {
                        xtype: 'radiogroup',                                
                        name: 'groupTipRec',
                        fieldLabel: 'Tipo Reclamo',
                        labelAlign: 'right',
                        width: 230,
                        columns: 2,
                        vertical: false,                        
                        items:[
                            {boxLabel:'Pago Directo', name:'tipoReclamo', inputValue:'PAGO DIRECTO' , readOnly: true, width:100},
                            {boxLabel:'Reembolso', name:'tipoReclamo', inputValue:'REEMBOLSO' , readOnly: true, width:100}
                        ]                        
                        
                    },
                    {xtype: 'tbspacer', width:100},
                    {xtype: 'textfield', name: 'siniestro',   fieldLabel: 'No. Siniestro',      readOnly: true, labelWidth: 120,  width: 260, labelAlign: 'right'}                    
                ]
            }, 
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                   {xtype: 'datefield', name: 'fechaPago', fieldLabel: 'Fecha Pago',    format: 'd/m/Y', readOnly: true, labelWidth: 100, labelAlign: 'left', width: 200},
                   {xtype: 'textfield', name: 'destino',   fieldLabel: 'Destino',      readOnly: true, labelWidth: 60,  width: 120, labelAlign: 'right'},
                   {xtype: 'textfield', name: 'destinoNombre',   fieldLabel: '',      readOnly: true, width: 300, labelAlign: 'right'},
                   {xtype: 'textfield', name: 'solicitudCxp',   fieldLabel: 'No. Solicitud CxP',      readOnly: true, labelWidth: 120,  width: 260, labelAlign: 'right'}
                    
                ]
            },
            {
                layout:'hbox',
                items:[                    
                    {xtype: 'tbspacer', width:25},
                    {
                        xtype: 'radiogroup',                                
                        name: 'groupTipPag',
                        fieldLabel: 'Tipo Pago',
                        labelAlign: 'left',
                        width: 230,
                        columns: 2,
                        vertical: false,
                        items:[
                            {boxLabel:'Cheque', name:'tipoPago', inputValue:'CHEQUE' , readOnly: true, width:90},
                            {boxLabel:'Transferencia', name:'tipoPago', inputValue:'TRANSFERENCIA' , readOnly: true, width:100}                            
                        ]                        
                        
                       
                    },                    
                    {xtype: 'textfield', name: 'referencia',   fieldLabel: 'Referencia',      readOnly: true, labelWidth: 150,  width: 390, labelAlign: 'right'},
                    {xtype: 'tbspacer', width:20},
                    {xtype: 'datefield', name: 'fechaAplicacion', fieldLabel: 'Fecha Aplicación',    format: 'd/m/Y', readOnly: true, labelWidth: 100, labelAlign: 'right', width: 240}
                ]
            },            
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                   {xtype: 'textfield', name: 'idTipoServicio',   fieldLabel: 'Tipo Servicio',      readOnly: true, labelWidth: 120,  width: 220, labelAlign: 'left'},
                   {xtype: 'textfield', name: 'tipoServicio',   fieldLabel: '',      readOnly: true, width: 400, labelAlign: 'right'}
                   
                ]
            },
            {
            	layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {                    
                        xtype:'combobox',
                        name:'conducto',
                        fieldLabel: 'Conducto',
                        labelWidth: 120, 
                        width: 300,
                        labelAlign: 'left',                                                
                        queryMode : 'local',
                        store:storeConducto,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true,
                        allowBlank:false,
                        forceSelection:true                                       
                   },
                   {xtype: 'tbspacer', width:20},
                   {                    
                        xtype:'combobox',
                        name:'causaReclamo',
                        fieldLabel: 'Causa Reclamo',                        
                        labelWidth: 120, 
                        width: 300,                        
                        labelAlign: 'right',
                        queryMode : 'local',
                        store:storeCausa,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true,
                        allowBlank:false,
                        forceSelection:true
                                       
                   }
                                                       
                ]
            },
            {
                layout:'hbox',
                items:[
                   {xtype: 'tbspacer', width:25},
                   {                    
                        xtype:'combobox',
                        name: 'idIcd',
                        id:'idComboICD',
                        fieldLabel: 'Diagnóstico (ICD)',                        
                        labelAlign: 'left',
                        labelWidth: 100, 
                        width: 880,                        
                        queryMode : 'remote',
                        queryParam:'params.otclave',
                        store:storeTiposICD,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true,
                        /*allowBlank:false,
                        forceSelection:true,*/
                        minChars: 2,
                        editable:true,                        
                        triggerAction: 'all',
                        hideTrigger:true                      
                                                     
                   }
                ]
            },
            {
            	layout:'hbox',
            	items: [
            	   {xtype: 'tbspacer', width:25},
            	   {                    
                        xtype:'combobox',
                        name:'atencionHosp',
                        fieldLabel: 'Atención',
                        labelAlign: 'left',
                        labelWidth: 60, 
                        width: 200,
                        disabled:false,
                        queryMode : 'local',
                        store:storeAtencionHosp,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true
                        /*allowBlank:false,
                        forceSelection:true*/
                   },
                   /*{xtype: 'tbspacer', width:25},*/
                   {xtype: 'datefield', name: 'fechaIngreso', fieldLabel: 'Fecha Ingreso',    format: 'd/m/Y', labelWidth: 90, labelAlign:'right', width: 210},
                   /*{xtype: 'tbspacer', width:25},*/
                   {xtype: 'datefield', name: 'fechaAlta', fieldLabel: 'Fecha Alta',    format: 'd/m/Y', labelWidth: 90, labelAlign:'right', width: 210},
                   {                    
                        xtype:'combobox',
                        name: 'motivoEgreso', 
                        fieldLabel: 'Egreso',                        
                        labelWidth: 70, 
                        width: 260,                 
                        labelAlign: 'right',
                        queryMode : 'local',                        
                        store:storeMotivoEgreso, 
                        valueField:'key',
                        displayField:'value',
                        /*typeAhead: true,
                        allowBlank: false,*/
                        forceSelection: true                
                   }
                   
            	]
            },
            {
            	layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {                    
                        xtype:'combobox',
                        name: 'procedimiento1',
                        id:'idcpt1',
                        fieldLabel: 'Procedimiento 1',                        
                        labelAlign: 'left',
                        labelWidth: 100, 
                        width: 880,                        
                        queryMode : 'remote',
                        queryParam:'params.otclave',
                        store:storeTiposCPT1,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true,
                        /*allowBlank:false,
                        forceSelection:true,*/
                        minChars: 2,
                        editable:true,                        
                        triggerAction: 'all',
                        hideTrigger:true                      
                                         
                   }
                ]
            },
            {
            	 layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {                    
                        xtype:'combobox',
                        name: 'procedimiento2',
                        id:'idcpt2',
                        fieldLabel: 'Procedimiento 2',                        
                        labelAlign: 'left',
                        labelWidth: 100, 
                        width: 880,                        
                        queryMode : 'remote',
                        queryParam:'params.otclave',
                        store:storeTiposCPT2,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true,
                        /*allowBlank:false,
                        forceSelection:true,*/
                        minChars: 2,
                        editable:true,                        
                        triggerAction: 'all',
                        hideTrigger:true                      
                                         
                   }
                ]
            },
            {
            	layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {                    
                        xtype:'combobox',
                        name: 'procedimiento3',
                        id:'idcpt3',
                        fieldLabel: 'Procedimiento 3',                        
                        labelAlign: 'left',
                        labelWidth: 100, 
                        width: 880,                        
                        queryMode : 'remote',
                        queryParam:'params.otclave',
                        store:storeTiposCPT3,
                        valueField:'key',
                        displayField:'value',
                        typeAhead:true,
                        /*allowBlank:false,
                        forceSelection:true,*/
                        minChars: 2,
                        editable:true,                        
                        triggerAction: 'all',
                        hideTrigger:true                      
                                         
                   }
                ]
            },
            {
            	layout:'hbox',
            	items:[
                	{xtype: 'tbspacer', width:25},
                	{
                		xtype:'button',
                		text: 'Guardar',
                		handler: function(btn, e) {
                			
                			var formBusqueda = this.up('form').getForm();
                			panelDatos.setLoading(true);
                			
                            panelDatos.setLoading(false);                            
                            formBusqueda.submit(
                                {
                                	clientValidation: true,
                                	url: _URL_GUARDA_RECLAMO_EXPRESS,
                                	params: {                                		
                                		icdNombre: formBusqueda.findField('idIcd').getRawValue()
                                	},
                                 	failure: function(form, action) {
                                        switch (action.failureType) {
                                            case Ext.form.action.Action.CLIENT_INVALID:
                                                showMessage('', _MSG_DATO_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);                                                
                                                break;
                                            case Ext.form.action.Action.CONNECT_FAILURE:                                                
                                                showMessage('', _MSG_ERROR_COMUNICACION, Ext.Msg.OK, Ext.Msg.INFO);                                                
                                                break;
                                            case Ext.form.action.Action.SERVER_INVALID:
                                               showMessage('', _MSG_RECLAMO_REGISTRADO, Ext.Msg.OK, Ext.Msg.INFO);
                                       }
                                    },
                                    success: function(form, action) {
                                       showMessage('', _MSG_RECLAMO_REGISTRADO, Ext.Msg.OK, Ext.Msg.INFO);
                                    }
                                }
                            );
                            
                		}
                	}
            	]
            }
            
            
        ]
    	
    });
    
    /*** FORMULARIO DETALLE***/
    
    /***COMPONENTES PARA GRID***/
    //Combo para tipo de concepto
    var cmbCveTipoConcepto = Ext.create('Ext.form.ComboBox',{
            name:'idconcep',     
            store: storeTipoConcepto,       
            queryMode:'local',
            displayField: 'value',      
            valueField: 'key',              
            editable:false,             
            allowBlank:false,
            listeners:{
                select: function (combo, records, opts){
                    var cdTipo = records[0].get('key');
                    console.log("CLAVE DE TIPO --> "+cdTipo);
                    storeConceptosCatalogo.proxy.extraParams=
                    {
                        'params.idPadre' : cdTipo,
                        catalogo        : _CATALOGO_ConceptosMedicos
                    };
                    //Se agrega removeAll para que limpie el store antes de volverlo a cargar
                    storeConceptosCatalogo.removeAll();
                    storeConceptosCatalogo.load();
                    storeCobertura.proxy.extraParams=
                    {
                    	'params.cdunieco' : panelDatos.getForm().findField('sucursal').getValue(),
                        'params.cdramo'   : '2',
                        'params.estado'   : 'M',
                        'params.nmpoliza' : panelDatos.getForm().findField('poliza').getValue(),
                        'params.nmsituac' : '1'
                    };
                    storeCobertura.load();
            }
        }
        
    });
    
    //Combo para clave de concepto
    cmbCveConcepto = Ext.create('Ext.form.ComboBox', {
    	name:'cdconcep',
    	store: storeConceptosCatalogo,
    	queryMode:'remote',
    	displayField: 'value',
    	valueField: 'value',
    	editable:true,
    	allowBlank:false,
    	forceSelection: true,
    	queryParam: 'params.descripc',
    	hideTrigger: true,
    	minChars: 3
    	
    });
    
    //Combo para cobertura afectada
    var conceptoSeleccionado;
    
    coberturaAfectada = Ext.create('Ext.form.ComboBox',
    {
        /*colspan:2, width:400,*/
        /*fieldLabel :'Cobertura afectada',*/   
        allowBlank: false,          
        displayField : 'dsgarant',      
        id:'idCobAfectada',     
        name:'cdgarant',
        labelWidth: 170,                    
        valueField   : 'cdgarant',  
        forceSelection : true,          
        matchFieldWidth: false,
        queryMode :'remote',                
        store : storeCobertura,     
        triggerAction: 'all',           
        editable:false,
        listeners : {
            'select' : function(combo, record) {
                Ext.getCmp('idSubcobertura').reset();
                Ext.getCmp('idSubcobertura').setValue('');
                
                conceptoSeleccionado.set('dsgarant',this.getRawValue());
                
                storeSubcobertura.removeAll();
                storeSubcobertura.load({
                    params:{
                        'params.cdgarant' :this.getValue()
                        
                    }
                });
                
                
            }
        }
    });
    /*
    coberturaDescripcion = Ext.create('Ext.form.ComboBox',{
        allowBlank: false,          
        displayField : 'dsgarant',
        name:'dsgarant',
        labelWidth: 170,                    
        valueField   : 'dsgarant',  
        forceSelection : true,          
        matchFieldWidth: false,
        queryMode :'remote',                
        store : storeCobertura2,     
        triggerAction: 'all',           
        editable:false        
    });
    */
    
    //Combo subcobertura
    subCobertura = Ext.create('Ext.form.field.ComboBox',
    {
        /*colspan:2,      
        width:550,
        fieldLabel : 'Subcobertura',*/
    	allowBlank: false,              
    	displayField : 'value',         
    	id:'idSubcobertura',        
    	name:'cdconval',
        labelWidth: 170,                
        valueField   : 'key',           
        forceSelection : true,          
        matchFieldWidth: false,
        queryMode :'remote',            
        store : storeSubcobertura,      
        triggerAction: 'all',          
        editable:false,
        listeners : {
            'select':function(e){
            	conceptoSeleccionado.set('dsconval',this.getRawValue());
                
            }
        }
    });
    
    var gridDetalle = Ext.create('Ext.grid.Panel', {
    	name:'editorConceptos',
        title: 'Capture los conceptos para llegar al importe.',
        height: 400,
        /*width: 870,*/
        width: 950,
        autoScroll:true,
        store: storeConceptos,
        features: [{
            ftype: 'summary'
        }],
        columns: [
            {
                xtype: 'actioncolumn',
                width: 30,
                sortable: false,
                menuDisabled: true,
                items: [{
                    icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                    tooltip: 'Eliminar Renglón',
                    scope: this,
                    handler: onRemoveClick
                }]
            },
            {
                header: 'Concepto',  
                dataIndex: 'idconcep', 
                width : 65,
                allowBlank: false,
                editor: cmbCveTipoConcepto,/*,*/
                
                /*Utilizamos el renderer para mostrar el valor y no la llave*/
                renderer : function(v) {
                	var leyenda = '';
                	//if (typeof v == 'string') {// tengo solo el indice
                	   storeTipoConcepto.each(function(rec) {
                            if (rec.data.key == v) {
                                leyenda = rec.data.value;
                            }
                        });
                	
                        return leyenda;
                },
                summaryType:'count',
                summaryRenderer: function(value){
                    
                    return Ext.String.format('Total {0}', value); 
                }
                
            },
            {
                header: 'Código Concepto', 
                dataIndex: 'dsconcep',
                width : 200,
                allowBlank: false,
                
                editor: cmbCveConcepto,
                renderer : function(v) {
                	var leyenda = v;
                	return leyenda;
                }
                
            },            
            {
                header: 'Cobertura', 
                dataIndex: 'cdgarant',
                width:70,
                allowBlank: false,
                editor: coberturaAfectada
                
            },
            {
                header: 'Nombre Cobertura',                
                dataIndex: 'dsgarant',
                width:170,
                allowBlank: false
                /*editor: coberturaDescripcion*/                
            },
            {
                header: 'Subcobertura', 
                dataIndex: 'cdconval', 
                width:100,
                allowBlank: false,
                editor: subCobertura
            },
            {
                header: 'Nombre Subcobertura', 
                dataIndex: 'dsconval', 
                width:170,
                allowBlank: false
                /*editor: subCoberturaDescripcion*/
            },
            {
                header: 'Importe', 
                dataIndex: 'importe',
                width:130,
                renderer: Ext.util.Format.usMoney,
                allowBlank: false,
                editor:{
                	xtype: 'textfield',
                    allowBlank: false,
                    editable : true        
                },
                /*Añadir summarytype*/                
                summaryType:'sum',
                summaryRenderer: function(value){
                	
                	return Ext.String.format('{0}',Ext.util.Format.usMoney( value)); 
            	}
            }/*,
            {
                xtype: 'actioncolumn',
                width: 40,
                sortable: false,
                menuDisabled: true,
                items: [{
                    icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                    tooltip: 'Eliminar Renglón',
                    scope: this,
                    handler: onRemoveClick
                }]
            }*/
        ],
        selType: 'cellmodel',
        plugins: [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 1,
                listeners : {
                    beforeedit : function(){                    	
                    	conceptoSeleccionado = gridDetalle.getView().getSelectionModel().getSelection()[0];
                    	
                    }
                }
            })
        ],
        tbar:[
            { 
                xtype: 'button', 
                text: 'Agregar renglón',
                handler : agregaRegistroDetalle
            }
        ]
        
        
        
    });
    
    
    
    /***FIN DE COMPONENTES PARA GRID***/
     
    
    var panelDetalle = Ext.create('Ext.form.Panel',{
    	model:'DetalleReclamoModel',
    	width:950,
        border: false,
        defaults : {
            bodyPadding : 5,
            border : false,
            autoScroll:true
        },
        items:[
            {
                layout:'hbox',
                items:[
                   {xtype: 'tbspacer', width:25},
                   {xtype: 'datefield', name: 'fechaProcesamiento', fieldLabel: 'Fecha Procesamiento',    format: 'd/m/Y', readOnly: true, labelWidth: 130, width: 260},                 
                   {xtype: 'tbspacer', width:50},
                   {xtype: 'textfield', name: 'clavePoliza',   fieldLabel: 'Clave Poliza',      readOnly: true, labelWidth: 120,  width: 260, labelAlign: 'right'},
                   {xtype: 'tbspacer', width:50},
                   {xtype: 'textfield', name: 'claveReclamo',   fieldLabel: 'Clave Reclamo',      readOnly: true, allowBlank: false, labelWidth: 120,  width: 260, labelAlign: 'right'}                 
                ]
            },
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {xtype: 'textfield', name: 'idAsegurado',   fieldLabel: 'Cliente',      readOnly: true, labelWidth: 60,  width: 120, labelAlign: 'left'},
                    {xtype: 'textfield', name: 'asegurado',   fieldLabel: '',      readOnly: true, width: 500, labelAlign: 'right'},                   
                    /*{xtype: 'tbspacer', width:100},*/
                    {xtype: 'textfield', name: 'idSesas',   fieldLabel: 'ID SESAS',      readOnly: true, labelWidth: 60,  width: 260, labelAlign: 'right'}                                    
                ]
            },
            {
                layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    {xtype: 'textfield', name: 'importe',   fieldLabel: 'Importe',      readOnly: true, labelWidth: 60,  width: 260, labelAlign: 'left'}
                ]
            },
            {
            	layout:'hbox',
                items:[
                    {xtype: 'tbspacer', width:25},
                    gridDetalle                    
                ]
            },
            {
                layout:'hbox',
                items:[                    
                    {xtype: 'tbspacer', width:25},
                    {
                        xtype:'button',
                        text: 'Guardar Detalle',
                        handler : function() {
                            guardarDetalle();
                        }
                    }
                ]
            }
            
        ]
    });
    
    /***FIN DE FORMULARIOS***/
    
    /***PANEL PRINCIPAL***/
    var tabDatosGenerales = Ext.create('Ext.tab.Panel', {
    	/*width:815,*/
    	width:975,
    	/*height:555,*/
    	/*height:585,*/
    	height:615,
    	items:[
    	   {
    	       id:'tabDatos',
    	       title:'DATOS RECLAMACIÓN',
    	       border:false,
    	       items:[
    	           panelDatos
    	       ]
    	   },
    	   {
               id:'tabDetalle',
               title:'DETALLE RECLAMACIÓN',
               border:false,               
               items:[
                   panelDetalle
               ],
                listeners: {
                    
                    activate: function(tab) {
                    	var fechaProcesamiento = panelDatos.getForm().findField('fechaProcesamiento').getValue();
                    	var clavePoliza = panelDatos.getForm().findField('clavePoliza').getValue();
                    	var claveReclamo = panelDatos.getForm().findField('claveReclamo').getValue();
                    	
                    	var idAsegurado = panelDatos.getForm().findField('idAsegurado').getValue();
                        var asegurado = panelDatos.getForm().findField('asegurado').getValue();
                        var importe = panelDatos.getForm().findField('importe').getValue();
                    	//var idSesas = panelDatos.getForm().findField('idSesas').getValue();
                        //console.log(panelDatos.getForm().findField('fechaProcesamiento').getValue());
                        //showMessage('', panelDatos.getForm().getValues(), Ext.Msg.OK, Ext.Msg.INFO);
                    	panelDetalle.getForm().findField('fechaProcesamiento').setValue(fechaProcesamiento);
                    	panelDetalle.getForm().findField('clavePoliza').setValue(clavePoliza);
                    	panelDetalle.getForm().findField('claveReclamo').setValue(claveReclamo);
                    	panelDetalle.getForm().findField('idAsegurado').setValue(idAsegurado);
                        panelDetalle.getForm().findField('asegurado').setValue(asegurado);
                        panelDetalle.getForm().findField('importe').setValue(importe);
                    	
                    }
                	
                }
           }
    	]
    });
    
    /***CUERPO PRINCIPAL***/
    
    
    
    
    var panelBusqueda = Ext.create('Ext.Panel', {
    	id:'main-panel',
    	baseCls:'x-plain',
    	renderTo: 'dvReclamoExpress',
    	autoScroll:true,
    	defaults: {frame:true, width:200, height: 200, margin : '2'},
    	items:[
    		{
    			title:'B&Uacute;SQUEDA DE RECLAMOS',
    			width:990,
                height:100,
                items:[
                	{                
                        xtype: 'form',
                        
                        border: false,
                        layout: {
                            type:'hbox'
                        },
                        margin: '5',
                        items:[
                        	{
                        		xtype: 'combobox',                                
                                name: 'params.reclamo', 
                                fieldLabel: 'No. de Reclamo',      
                                labelWidth: 120, 
                                width: 300, 
                                queryMode : 'local', 
                                queryParam : 'params.reclamo', 
                                store:storeReclamos, 
                                valueField:'key',
                                displayField:'value',
                                anyMatch: true,
                                allowBlank:false,
                                forceSelection:true,
                                listeners:{
                                	'change' : function( thisComponent, newValue, oldValue, eOpts ) {
                                	   //showMessage('',Ext.String.format('Elemento seleccionado: {0}',newValue),Ext.Msg.OK,Ext.Msg.OK);
                                	   storeSecuencias.load({
                                	       params: {
                                	       	   'params.reclamo' :newValue
                                	       }
                                	   });
                                	}
                                }
                        	},
                        	{
                                xtype: 'tbspacer',
                                flex: 2.5
                            },
                            {
                                id: 'subpanelBusquedas',
                                layout : 'vbox',
                                align:'stretch',
                                flex: 70,
                                border: false,                                
                                items: [
                                    {
                                    	id: 'subpanelBusquedaSecuencial',
                                        layout : 'hbox',
                                        align:'stretch',
                                        border: false,                                    
                                        defaults: {
                                            labelAlign: 'right',
                                            enforceMaxLength: true,
                                            msgTarget: 'side'
                                        },
                                        items:[
                                        	{
                                                xtype: 'combobox',                                
                                                name: 'params.secuencial', 
                                                fieldLabel: 'Secuencial',      
                                                labelWidth: 120, 
                                                width: 200, 
                                                queryMode : 'local',                                                 
                                                store:storeSecuencias, 
                                                valueField:'key',
                                                displayField:'value',
                                                allowBlank: false,
                                                forceSelection: true
                                            }
                                        
                                        ]
                                    }
                                ]                            
                            
                            },                        	
                            {
                                xtype:'tbspacer',
                                flex: 2.5
                            },
                            {
                            	xtype : 'button',
                                flex:10,
                                text: 'Buscar',
                                handler: function(btn, e) {
                                	var formBusqueda = this.up('form').getForm();
                                    panelDatos.setLoading(true);
                                    if(!formBusqueda.findField('params.reclamo').isValid()){
                                        showMessage('', _MSG_RECLAMO_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                        panelDatos.setLoading(false);
                                        return;
                                    }
                                    if(!formBusqueda.findField('params.secuencial').isValid()){
                                        showMessage('', _MSG_SECUENCIAL_INVALIDO, Ext.Msg.OK, Ext.Msg.INFO);
                                        panelDatos.setLoading(false);
                                        return;
                                    }
                                    limpiaSeccionInformacionPrincipal();
                                    cargaReclamoExpress(formBusqueda, btn);
                                    panelDatos.setLoading(false);
                                }
                            }                            
                        ]
                	},
                	{
                		layout: 'column',
                        margin: '5',
                        border: false,
                        name: 'mensaje',
                        html:''
                	}
                ]
    		},
    		{
    			layout: 'hbox',
    			frame:false,
    			border: false,
                width:990,
                /*height: 410,*/
                /*height: 700,*/
                height: 700,
                defaults: {
                    autoScroll:true,
                    frame:true,
                    height: 400
                    /*height: 1000*/
                },
                items:[
                    {
                        title:'NIVEL',
                        width: 135,
                        style: 'margin-right:5px',
                        hidden:true,
                        items: [
                            listViewNivelReclamo
                        ]
                    },
                    {
                        /*width:850,*/
                    	width:990,
                    	/*height:570,*/
                    	/*height:600,*/
                    	height:630,
                        items:[
                            tabDatosGenerales
                        ]
                    }
                ]	
            
    		}
	   ]
    	  
    });
    /*** FIN DE CUERPO PRINCIPAL***/
    
    /*** FUNCIONES ***/
    
    
    function cargaReclamoExpress(formBusqueda, btn){
    	//showMessage('', "BUSCAR RECLAMO " + formBusqueda.findField('reclamo').getValue() + ", SECUENCIA: " + formBusqueda.findField('secuencial').getValue(), Ext.Msg.OK, Ext.Msg.INFO);
    	
    	// Consultar Datos Generales del Reclamo
        storeReclamoExpress.load({
            params : formBusqueda.getValues(),
            callback: function(records, operation, success) {
                if (success) {                	
                    if (records.length > 0) {                                              
                        // Se llenan los datos generales del reclamo                    	
                        panelDatos.getForm().loadRecord(records[0]);  
                        
                        //Se envia mensaje del modo en el que se está trabajando.
                        cambiaTextoMensaje(records[0].get('modo'));
                        
                    } else {
                        showMessage('Aviso', 
                            'No existen datos generales del reclamo seleccionado. Verifique su numero y secuencial.', 
                            Ext.Msg.OK, Ext.Msg.ERROR);
                    }
                } else {
                    showMessage('Error', 
                        'Error al obtener los datos del reclamo, intente m\u00E1s tarde',
                        Ext.Msg.OK, Ext.Msg.ERROR);
                }

            }
        });
        //Limpiamos los conceptos
        storeConceptos.removeAll()
    }
    
    //Función para limpiar formulario y stores
    function limpiaSeccionInformacionPrincipal() {
        panelDatos.getForm().reset();
        panelDetalle.getForm().reset();
        //gridDatosEnfermedades.getStore().removeAll();        
    }
    
    //Función para añadir registros en el detalle
    function agregaRegistroDetalle()
    {
        storeConceptos.add(new ConceptosModel());
    	
    }
    
    function onRemoveClick (grid, rowIndex){    	
        var record = storeConceptos.getAt(rowIndex);
        storeConceptos.removeAt(rowIndex);
    }    
    
    function guardarDetalle(){
    	//showMessage('', "GUARDAR DETALLE ", Ext.Msg.OK, Ext.Msg.INFO);
    	var obtener = [];
    	storeConceptos.each(function(record) {
            obtener.push(record.data);
        });
        //showMessage('', "GUARDAR DETALLE: " + obtener.length, Ext.Msg.OK, Ext.Msg.INFO);
        if(obtener.length <= 0){
        	Ext.Msg.show({
                        title:'Error',
                        msg: 'Se requiere al menos un concepto en el Detalle.',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
            storeConceptos.reload();
            return false;                   
        } else {        	
        	//Recuperamos los datos del store
        	/*
        	for(i=0; i<obtener.length; i++){
        		console.log("Este es el concepto:"  + obtener[i].idconcep);        		
        		console.log("Esta es la descripcion concepto:"  + obtener[i].dsconcep);
        		console.log("Este es el codigo cobertura :"  + obtener[i].cdgarant);
        		console.log("Esta es la descripcion cobertura:"  + obtener[i].dsgarant);
        		console.log("Este es el codigo subcobertura :"  + obtener[i].cdconval);
        		console.log("Esta es la descripcion subcobertura:"  + obtener[i].dsconval);
        		console.log("Este es el importe :"  + obtener[i].importe);
        	}*/
        	for(i=0; i<obtener.length; i++){
        		if(
        		  obtener[i].idconcep == null || obtener[i].idconcep == "" ||
        		  obtener[i].dsconcep == null || obtener[i].dsconcep == "" ||
        		  obtener[i].cdgarant == null || obtener[i].cdgarant == "" ||
        		  obtener[i].dsgarant == null || obtener[i].dsgarant == "" ||
        		  obtener[i].cdconval == null || obtener[i].cdconval == "" ||
        		  obtener[i].dsconval == null || obtener[i].dsconval == "" ||
        		  obtener[i].importe == null || obtener[i].importe == "")
    		  	{
    		  		Ext.Msg.show({
                        title:'Error',
                        msg: 'Favor de introducir los campos requeridos en el concepto',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                    return false;
        		}
                           
        	}
        	
        	var submitValues={};
        	//var formulario=panelInicialPral.form.getValues();
        	var formulario = panelDetalle.form.getValues();
        	submitValues['params']=formulario;
        	var datosTablas = [];
        	storeConceptos.each(function(record,index){
        		datosTablas.push({
        		  indice: index,
        		  idconcep: record.get('idconcep'),
        		  dsconcep: record.get('dsconcep'),
        		  cdgarant: record.get('cdgarant'),
        		  dsgarant: record.get('dsgarant'),
        		  cdconval: record.get('cdconval'),
        		  dsconval: record.get('dsconval'),
        		  importe: record.get('importe')
        		});        	
        	});
        	submitValues['datosTablas']=datosTablas;
        	console.log("VALORES A ENVAR --> submitValues");
        	console.log(submitValues);
        	
        	panelDetalle.setLoading(true);
        	Ext.Ajax.request({
                url: _URL_GUARDA_DETALLE_EXPRESS,
                jsonData:Ext.encode(submitValues),
                success:function(response,opts){
                	panelDetalle.setLoading(false);
                	var jsonResp = Ext.decode(response.responseText);
                	console.log("RESPUESTA-->");
                	console.log(jsonResp);
                	if(jsonResp.success==true){
                		panelDetalle.setLoading(false);
                		/*banderaConcepto = "0";*/
                		storeConceptos.reload();
                		/*showMessage('', _MSG_DETALLE_REGISTRADO, Ext.Msg.OK, Ext.Msg.INFO);*/
                		Ext.Msg.show({
                            title:'',
                            msg: _MSG_DETALLE_REGISTRADO,
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.INFO
                        });
                	}
                },
                failure:function(response,opts){
                	panelInicialPrincipal.setLoading(false);
                	Ext.Msg.show({
                        title:'Error',
                        msg: _MSG_ERROR_COMUNICACION,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                }
                
        	});
        }//Fin else
    }//Fin funcion
    
    function cambiaTextoMensaje(texto) {
        if(!Ext.isEmpty(texto)){
            var color="";
            var mensaje="";
            if(texto == "C"){
                color = "#009900";
                mensaje = "MODO CREACIÓN";             
            } else if(texto == "E"){
                color = "#E96707";
                mensaje = "MODO EDICIÓN: EN ESTE MODO DEBERÁ CAPTURAR NUEVAMENTE EL DETALLE DE LA RECLAMACIÓN."           
            } else {
                color = "#FF0000";
                mensaje = "SELECCIONE EL RECLAMO, SECUENCIAL Y PULSE EL BOTÓN BUSCAR.";
            }
            panelBusqueda.down('[name=mensaje]').update('<span style="color:' + color + ';font-size:14px;font-weight:bold;text-decoration:underline;">'+ mensaje +'</span>');
        }else {
            panelBusqueda.down('[name=mensaje]').update('<span></span>');
        }
    }
    
    /*** FIN DE FUNCIONES ***/
    
}); /*Fin de Ext.onReady*/
