Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
var storeConceptoAutorizados1;
var storeQuirugicoBase1;
var storeQuirugico1;
var extraParams='';
Ext.onReady(function() {

	Ext.selection.CheckboxModel.override( {
		mode: 'SINGLE',
		allowDeselect: true
	});
	
    // Conversión para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
    
    
    
    Ext.define('modelListadoTablas1',{
        extend: 'Ext.data.Model',
        fields: [
                 	{type:'string',    name:'nmautser'},		{type:'string',    name:'cdtipaut'},		{type:'string',    name:'cdmedico'},
                 	{type:'string',    name:'nombreMedico'},	{type:'string',    name:'cdtipmed'},		{type:'string',    name:'cdcpt'},
                 	{type:'string',    name:'desccpt'},			{type:'string',    name:'precio'},			{type:'string',    name:'cantporc'},
                 	{type:'string',    name:'ptimport'},		{type:'string',    name:'descTipMed'}
                 	
				]
    });
    
    Ext.define('modelConsultaFormulario',{
        extend: 'Ext.data.Model',
        fields: [
                 	{type:'string',    name:'cdunieco'},         {type:'string',    name:'cdramo'},
                 	{type:'string',    name:'estado'},         {type:'string',    name:'nmsituac'},
                 	{type:'string',    name:'cdmedico'},
                 	{type:'string',    name:'nmautser'},         {type:'string',    name:'nmautant'},
                 	{type:'string',    name:'cdperson'},         {type:'string',    name:'nombreCliente'},
                    {type:'string',    name:'fesolici'},         {type:'string',    name:'feautori'},
                    {type:'string',    name:'fevencim'},         {type:'string',    name:'feingres'},
                    {type:'string',    name:'nmpoliza'},
                    {type:'string',    name:'cduniecs'},
                    {type:'string',    name:'cdgarant'},         {type:'string',    name:'descGarantia'},       {type:'string',    name:'cdconval'},    {type:'string',    name:'descSubGarantia'},
                    {type:'string',    name:'cdprovee'},         {type:'string',    name:'nombreProveedor'},
                    {type:'string',    name:'cdmedico'},         {type:'string',    name:'nombreMedico'},
                    {type:'string',    name:'porpenal'},//<-- Penalización Por circulo hospitalario
                    {type:'string', name:'copagofi'},	//<-- Penalización Por cambio de zona
                    {type:'string',    name:'cdicd'},         {type:'string',    name:'descICD'},
                    {type:'string',    name:'mtsumadp'},
                    {type:'string',    name:'cdcausa'},         {type:'string',    name:'descCausa'},
                    {type:'string',    name:'dstratam'},         {type:'string',    name:'dsobserv'},       {type:'string',    name:'dsnotas'}
				]
    
    });
    
    var storeConsultaFormulario = Ext.create('Ext.data.Store', {
        model:'modelConsultaFormulario',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_CONSULTA_AUTORIZACION_ESP1,
            reader: {
                type: 'json',
                root: 'datosAutorizacionEsp'
            }
        }
    });
    
    storeRamos2 = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy:
        {
            type: 'ajax',
			url:_UR_LISTA_RAMO_SALUD2,
            reader:
            {
                type: 'json',
				root: 'listadoRamosSalud'
            }
        }
    });
    
    cmbRamos2 = Ext.create('Ext.form.field.ComboBox',
	{
		colspan	   :2,			fieldLabel   : 'Producto',			id        : 'cmbRamos2',		allowBlank     : false,			width:500	
	    ,editable   : false,		displayField : 'value',			valueField: 'key',			forceSelection : true,
	    labelWidth   : 170,				queryMode :'local',			name           :'cmbRamos2', readOnly   : true
	    ,store : storeRamos2
	});
	
    sucursalConsulta= Ext.create('Ext.form.field.ComboBox',
	{
		colspan		:2,					fieldLabel   : 'Plaza',			id: 'sucConsulta',				allowBlank: false,			width:500	
		,editable   : false,			displayField : 'value',				valueField:'key',			    forceSelection : true
		,labelWidth : 170,				queryMode    :'local',				editable:false,					name:'cduniecs', readOnly   : true
		,store : Ext.create('Ext.data.Store', {
			model:'Generic',
			autoLoad:true,
			proxy:
			{
				type: 'ajax',
				url:_UR_LISTA_PLAZAS,
				reader:
				{
					type: 'json',
					root: 'listaPlazas'
				}
			}
		})
	});
    
    
    //     
    
    causaSiniestro2= Ext.create('Ext.form.field.ComboBox',
	{
	    colspan		:2,					fieldLabel   : 'Causa siniestro',			id: 'idCausaSiniestro2',				allowBlank: false,			width:500	
	    ,editable   : false,			displayField : 'value',				valueField:'key',			    forceSelection : true
	    ,labelWidth : 170,				queryMode    :'local',				editable:false,					name:'cdcausa', readOnly   : true
	    ,store : Ext.create('Ext.data.Store', {
	        model:'Generic',
	        autoLoad:true,
	        proxy:
	        {
	            type: 'ajax',
	            url:_URL_CATALOGOS,
	            extraParams : {catalogo:_CAT_CAUSASINIESTRO1},
	            reader:
	            {
	                type: 'json',
	                root: 'lista'
	            }
	        }
	    })
	});
    
    tratamientoInformacion= Ext.create('Ext.form.field.ComboBox',
	{
	    colspan		:2,					fieldLabel   : 'Tr&aacute;tamiento',			id: 'tratamiento2',				allowBlank: false,			width:500	
	    ,editable   : false,			displayField : 'value',				valueField:'key',			    forceSelection : true
	    ,labelWidth : 170,				queryMode    :'local',				name:'dstratam',readOnly   : true
	    ,store : Ext.create('Ext.data.Store', {
	        model:'Generic',
	        autoLoad:true,
	        proxy:
	        {
	            type: 'ajax',
	            url: _URL_CATALOGOS,
	            extraParams : {catalogo:_CAT_TRATAMIENTO1},
	            reader:
	            {
	                type: 'json',
	                root: 'lista'
	            }
	        }
	    })
	});
    
   //DATOS PARA EL PRIMER GRID --> CONCEPTOS AUTORIZADOS
	storeConceptoAutorizados1=new Ext.data.Store(
	{
		autoDestroy: true,
		model: 'modelListadoTablas1'
	});
	
	//DATOS PARA EL SEGUNDO GRID --> EQUIPO QUIRURGICO BASE
	storeQuirugicoBase1=new Ext.data.Store(
	{
		autoDestroy: true,
		model: 'modelListadoTablas1'
	});
	
	//DATOS PARA EL TERCER GRID --> EQUIPO QUIRURGICO
	storeQuirurgico1=new Ext.data.Store(
	{
		autoDestroy: true,
		model: 'modelListadoTablas1'
	});
    
	
	Ext.define('EditorIncisos1_1', {
		extend: 'Ext.grid.Panel',
		collapsible   : true,
		titleCollapse : true,
		requires: [
		           'Ext.selection.CellModel',
		           'Ext.grid.*',
		           'Ext.data.*',
		           'Ext.util.*',
		           'Ext.form.*'
		           ],
       xtype: 'cell-editing',
       title: 'Conceptos Autorizados',
       frame: false,
       initComponent: function(){
    	   this.cellEditing = new Ext.grid.plugin.CellEditing({
    		   clicksToEdit: 1
    	   });
    	   Ext.apply(this, {
    		   height: 200,
    		   plugins: [this.cellEditing],
    		   store: storeConceptoAutorizados1,
    		   columns: 
    			   [
	    			    
	    			    {	
	    			    	header: 'M&eacute;dico',			dataIndex: 'nombreMedico',			width:250
	    			    },
	    			    
	    			    {
	    			    	header: 'CPT',						dataIndex: 'desccpt',				width:250
	    			    },
	    			    {
	    			    	header: 'Precio',					dataIndex: 'precio',			width:150,				renderer: Ext.util.Format.usMoney
	    			    },
	    			    {
	    			    	header: 'Cantidad', 				dataIndex: 'cantporc',		 	width:150
	    			    },
	    			    {
	    			    	header: 'Importe', 					dataIndex: 'ptimport',		 	width:150,				renderer: Ext.util.Format.usMoney
	    			    }
    			    ],
    			    selModel: {
    			    	selType: 'cellmodel'
    			    }
    	   });
    	   this.callParent();
	   }
	});
	
	gridIncisos1_1=new EditorIncisos1_1();
	
	
	//DATOS PARA EL SEGUDO GRID --> EQUIPO QUIRURGICO BASE
	Ext.define('EditorIncisos2_1', {
		extend: 'Ext.grid.Panel',
		collapsible   : true,
		titleCollapse : true,
		requires: [
		           'Ext.selection.CellModel',
		           'Ext.grid.*',
		           'Ext.data.*',
		           'Ext.util.*',
		           'Ext.form.*'
		           ],
       xtype: 'cell-editing',
       title: 'Equipo quir&uacute;rgico base',
       frame: false,
       initComponent: function(){
    	   this.cellEditing = new Ext.grid.plugin.CellEditing({
    		   clicksToEdit: 1
    	   });
	
	   Ext.apply(this, {
		   height: 200,
		   plugins: [this.cellEditing],
		   store: storeQuirugicoBase1,
		   columns: 
			   [	
			    	{
			    		header: 'CPT',						dataIndex: 'desccpt',				width:250				 		
			    	},
			    	{
			    		header: 'Precio',					dataIndex: 'precio',			width:150,				renderer: Ext.util.Format.usMoney
			    	},
			    	{
			    		header: 'Porcentaje', 				dataIndex: 'cantporc',		width:150
			    	},
			    	{
			    		header: 'Importe', 					dataIndex: 'ptimport',		 	width:150,				renderer: Ext.util.Format.usMoney
			    	}
	    	],
	    	selModel: {
	    		selType: 'cellmodel'
	    	}
	   });
	   this.callParent();
       }
});

	
	gridIncisos2_1=new EditorIncisos2_1();
	
	
	//DATOS PARA EL TERCER GRID --> EQUIPO QUIRURGICO
	Ext.define('EditorIncisos3_1', {
		extend: 'Ext.grid.Panel',
		collapsible   : true,
		titleCollapse : true,
		requires: [
		           'Ext.selection.CellModel',
		           'Ext.grid.*',
		           'Ext.data.*',
		           'Ext.util.*',
		           'Ext.form.*'
		           ],
       xtype: 'cell-editing',
       title: 'Equipo quir&uacute;rgico',
       frame: false,
       initComponent: function(){
    	   this.cellEditing = new Ext.grid.plugin.CellEditing({
    		   clicksToEdit: 1
    	   });
	
	   Ext.apply(this, {
		   height: 200,
		   plugins: [this.cellEditing],
		   store: storeQuirurgico1,
		   columns: 
			   [	{
			    		header: 'M&eacute;dico',		dataIndex: 'nombreMedico',	 		width:250	
			    	},
			    	{
			    		header: 'Porcentaje',			dataIndex: 'cantporc',		width:150				 		
			    	},
			    	{
			    		//<<
			    		header: 'Tipo medico',				dataIndex: 'descTipMed',	 		width:250
			    	},
			    	{
			    		header: 'Importe',				dataIndex: 'ptimport',	 		width:250,					renderer: Ext.util.Format.usMoney
			    	}
		    	],
		    	selModel: {
		    		selType: 'cellmodel'
		    	}
	   });
	   this.callParent();
       }
	});

	gridIncisos3_1=new EditorIncisos3_1();
	
    var panelInicialPrincipal1= Ext.create('Ext.form.Panel',
    		{
    			border    : 0
    			//,title: 'Autorizaci&oacuten de Servicios'
    			,renderTo : 'div_clau21'
    			,id : 'panelInicialPrincipal1'
    			,bodyPadding: 5
    			,width: 750
    			 ,model : 'modelConsultaFormulario'
    			//,url: _URL_GUARDA_AUTORIZACION
    			,layout     :
    			{
    				type     : 'table'
    				,columns : 2
    			}
    			,defaults 	:
    			{
    				style : 'margin:5px;'
    			}
    			,
    			items    	:
    				[

						{
						    xtype       : 'textfield',			fieldLabel : 'unieco',				id  : 'unieco1', 		hidden:true,
						    name       : 'cdunieco',			labelWidth	: 170,					readOnly   : true
						},
						{
						    xtype       : 'textfield',			fieldLabel : 'ramo',				id  : 'cdramo1',		hidden:true,
						    name       : 'cdramo',			labelWidth	: 170,						readOnly   : true
						},
						{
						    xtype       : 'textfield',			fieldLabel : 'estado',				id  : 'estado1',	hidden:true,
						    name       : 'estado',			labelWidth	: 170,					readOnly   : true
						},
						{
						    xtype       : 'textfield',			fieldLabel : 'situac',				id  : 'nmsituac1', 		hidden:true,
						    name       : 'nmsituac',			labelWidth	: 170,					readOnly   : true
						},
						{
						    xtype       : 'textfield',			fieldLabel : 'medico',				id  : 'cdmedico1', 	hidden:true,
						    name       : 'cdmedico',			labelWidth	: 170,					readOnly   : true
						},
						
						{
						    xtype       : 'textfield',			fieldLabel : 'cveGarantia',				id  : 'cveGarantia1', 		hidden:true,
						    name       : 'cdgarant',			labelWidth	: 170,					readOnly   : true
						},
						{
						    xtype       : 'textfield',			fieldLabel : 'cveSubgarantia',				id  : 'cveSubgarantia1', 		hidden:true,
						    name       : 'cdconval',			labelWidth	: 170,					readOnly   : true
						},
						{
						    xtype       : 'textfield',			fieldLabel : 'cveSucursal',				id  : 'cveSucursal1', 	hidden:true,
						    name       : 'cduniecs',			labelWidth	: 170,					readOnly   : true
						},
						
						{
    	                    xtype       : 'textfield',			fieldLabel : 'No. de autorizaci&oacute;n',				id  : 'idNoAutorizacion1',
    	                    name       : 'nmautser',			labelWidth	: 170,										readOnly   : true
    	                },
    	                {
    	                    xtype       : 'textfield',			fieldLabel : 'No. de autorizaci&oacute;n anterior',		id	: 'idNumeroAnterior1',	
    	                    name       : 'nmautant',			labelWidth	: 170,										readOnly   : true
    	                }
    				 	,
    				 	cmbRamos2
    				 	,
    				 	{
    				 		xtype       : 'textfield',			colspan:2,				fieldLabel : 'Asegurado',     	readOnly   : true,
    	                    id:'idAsegurado1',					labelWidth: 170,		width:500,						name       : 'nombreCliente'
    	                }
    				 	,
    				 	{
    				 	    id: 'fechaSolic1'		,xtype		: 'datefield'								,fieldLabel	: 'Fecha Solicitud',
    				 	    name:'fesolici'				,labelWidth : 170						,format		: 'd/m/Y',
    				 	    editable: true				,readOnly   : true
    				 	},
    				 	{
    				 	    id: 'fechaAutorizacion1'					,xtype		: 'datefield'								,fieldLabel	: 'Fecha Autorizaci&oacute;n',
    				 	    labelWidth : 170						,format		: 'd/m/Y',
    				 	    readOnly   : true,							name		: 'feautori'
    				 	},
    				 	{
    				 	    id: 'fechaVencimiento1'					,xtype		: 'datefield'								,fieldLabel	: 'Fecha de vencimiento',
    				 	    labelWidth : 170						,format		: 'd/m/Y',									name:'fevencim',
    				 	    readOnly   : true
    				 	},
    				 	{
    				 	    id: 'fechaIngreso1'						,xtype		: 'datefield'								,fieldLabel	: 'Fecha de Ingreso',
    				 	    labelWidth : 170						,format		: 'd/m/Y',									name:'feingres',
    				 	    readOnly   : true
    				 	},
    				 	{
    				 		colspan:2,   xtype       : 'textfield',			fieldLabel : 'P&oacute;liza afectada'				,id       : 'polizaAfectadaCom1'
		 					 ,allowBlank : false,				labelWidth: 170,				name:'nmpolizaCom',	readOnly   : true, Width: 1100
			 			 }
    				 	,
    				 	sucursalConsulta
    				 	,
    				 	{
    		                 colspan:2,   xtype       : 'textfield',			fieldLabel : 'P&oacute;liza afectada'				,id       : 'polizaAfectada1'
    		                 ,allowBlank : false,				labelWidth: 170,				name:'nmpoliza', hidden:true
    		             }
    				 	,
    				 	{
    	                    colspan:2,   xtype       : 'textfield',			fieldLabel : 'Cobertura Afectada',				id  : 'cobertura1',
    	                    name       : 'descGarantia',			labelWidth	: 170,										readOnly   : true
    	                },
    	                {
    	                    colspan:2,   xtype       : 'textfield',			fieldLabel : 'Sucobertura',				id  : 'subcobertura1',
    	                    name       : 'descGarantia',			labelWidth	: 170,										readOnly   : true, width:600
    	                }
    	               ,
    	                {
    	                    xtype       : 'textfield',			colspan:2,					fieldLabel : 'Proveedor',			id  : 'proveedor1',
    	                    name       : 'nombreProveedor',		labelWidth	: 170,			width:500,							readOnly   : true
    	                },
    	                {
    	                	colspan:2,	xtype       : 'textfield',			fieldLabel : 'Exclusion'		,id       : 'idExclusionPenalizacion1',
    						labelWidth: 170,					hidden:true
    		 			}
    	                ,{
    	                    xtype       : 'textfield',			colspan:2,					fieldLabel : 'Proveedor clave',			id  : 'cdproveedor1',
    	                    name       : 'cdprovee',		labelWidth	: 170,			width:500,							readOnly   : true, hidden:true
    	                },
    	                {
    	                	 colspan:2,	xtype       : 'textfield',			fieldLabel : 'zonaHospProv'		,id       : 'idzonaHospProv1',
    						 labelWidth: 170,					hidden:true
    		 			},
    		 			{
    		 				colspan:2,xtype       : 'textfield',			fieldLabel : 'zonaContratadaPoliza'		,id       : 'idZonaContratadaPoliza1',
    						 labelWidth: 170,					hidden:true
    		 			},
    		 			{
    		 				colspan:2,xtype       : 'textfield',			fieldLabel : 'dsplanPoliza'		,id       : 'iddsplanAsegurado1',
    						 labelWidth: 170,					hidden:true
    		 			},
    				 	{
    	                    colspan:2,xtype       : 'textfield',			fieldLabel : 'Medico',				id  : 'medico1',
    	                    name       : 'nombreMedico',			labelWidth	: 170,										readOnly   : true
    	                },
    	                {
    	                    colspan:2,xtype       : 'textfield',			fieldLabel : 'Especialidad',			id  : 'especialidad1',
    	                   labelWidth	: 170,										readOnly   : true
    	                },
    				 	{
    	                	colspan:2,   xtype       : 'textfield',			fieldLabel : 'Deducible',				id  : 'deducible1',
    	                    labelWidth	: 170,										readOnly   : true
    	                },
    	                {
    	                	colspan:2,   xtype       : 'textfield',			fieldLabel : 'Copago original',			id  : 'copago1',
    	                    labelWidth	: 170,										readOnly   : true,		width:500
    	                },
    	                {
    	                	colspan:2,   xtype       : 'textfield',			fieldLabel : 'TipoCopago',			id  : 'tipoCapago1',
    	                    labelWidth	: 170,										readOnly   : true,		width:500,	hidden:true
    	                },
    	                {
    	                	colspan:2,   xtype       : 'textfield',			fieldLabel : 'Copago final',			id  : 'copagofi',  name:'copagofiMS',
    	                    labelWidth	: 170,										readOnly   : true,		width:500
    	                },
    	                {
							colspan:2,   xtype       : 'textfield',			fieldLabel : 'Penalizaci&oacute;n circulo hospitalario',				id  : 'porpenal1',// 		hidden:true,
						    name       : 'porpenal',			labelWidth	: 170,					readOnly   : true
						},
    				 	{
    				 		colspan:2,   xtype       : 'textfield'				,fieldLabel : 'Penalizaci&oacute;n por cambio de zona'						,id       : 'idPenalCambioZona1'
    			 			,labelWidth: 170						,readOnly   : true,			name       : 'idPenalCambioZona1'
    				 	},
    	                {
    	                    xtype       : 'textfield',			colspan:2,					fieldLabel : 'ICD',			id  : 'icd1',
    	                    name       : 'descICD',		labelWidth	: 170,			width:500,							readOnly   : true
    	                },
    	                causaSiniestro2
    	                ,
    	                {
    	                    xtype       : 'textfield',			colspan:2,				fieldLabel : 'Suma disponible proveedor',			id  : 'sumdisponible1',
    	                    name       : 'mtsumadp',			labelWidth	: 170,										readOnly   : true,	renderer: Ext.util.Format.usMoney
    	                },
    	                tratamientoInformacion
    	                ,
    				 	{
    				 		colspan:2								,xtype       : 'textareafield'				,fieldLabel : 'Observaciones'				,id       : 'observaciones1'
    			 			,labelWidth	 : 170						,name:'dsobserv',		readOnly   : true,
    			 			width      : 700						,height		: 70
    				 	},
    				 	{
    				 		colspan:2								,xtype       : 'textareafield'				,fieldLabel : 'Notas internas'				,id       : 'notaInterna1'
    			 			,labelWidth: 170						,name:'dsnotas',		readOnly   : true,
    			 			width      : 700						,height: 70
    				 	},
    				 	// AQUI VAN LOS GRID
    				 	{
    				 	    colspan:2,
    				 	    items    :
    				 	        [
    				 	         gridIncisos1_1
    				 	         ]
    				 	},
    				 	{
    				 	    colspan:2,
    				 	    items    :
    				 	        [
    				 	         gridIncisos2_1
    				 	         ]
    				 	},
    				 	{
    				 	    colspan:2,
    				 	    items    :
    				 	        [
    				 	         gridIncisos3_1
    				 	         ]
    				 	}
    				 	
    			 	]
    		});
    
    
		var params = {
			'params.nmautser'  : valorAction.nmautser
		};
		storeConsultaFormulario.load({
			params: params,
			callback: function(records, operation, success){
				if(success){
					if(records.length <= 0){
						Ext.Msg.show({
							title: 'Aviso',
							msg: 'No se encontraron datos',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						});
					}else{
						panelInicialPrincipal1.getForm().loadRecord(records[0]);
						Ext.getCmp('sucConsulta').setValue(Ext.getCmp('cveSucursal1').getValue());
						
						storeRamos2.load({
			                params:{
			                	'params.idPadre':Ext.getCmp('sucConsulta').getValue()
			                }
						});
						
						Ext.getCmp('cmbRamos2').setValue(Ext.getCmp('cdramo1').getValue());
						Ext.Ajax.request(
						{
							url     : _URL_LISTA_SUBCOBERTURA1
							,params : 
							{
								'params.cdsubcob':Ext.getCmp('cveSubgarantia1').getValue()
							}
							,success : function (response)
							{
								if(Ext.decode(response.responseText).listaSubcobertura != null)
								{
									var json=Ext.decode(response.responseText).listaSubcobertura[0];
									Ext.getCmp('subcobertura1').setValue(json.value);
								}
							},
							failure : function ()
							{
								me.up().up().setLoading(false);
								Ext.Msg.show({
									title:'Error',
									msg: 'Error de comunicaci&oacute;n',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.ERROR
								});
							}
						});
						
						Ext.Ajax.request(
						{
							url     : _URL_LISTADO_CONCEP_EQUIP1
							,params : params
							,success : function (response)
							{
								if(Ext.decode(response.responseText).listaConsultaTablas != null)
								{
									var json=Ext.decode(response.responseText);
									for(var i=0;i<json.listaConsultaTablas.length;i++)
									{
										/*OBTENEMOS LOS VALORES*/
										var porcentajeCantidad="";
										if(json.listaConsultaTablas[i].cdtipaut==1)
										{
											porcentajeCantidad = json.listaConsultaTablas[i].cantporc;
										}else{
											porcentajeCantidad =(json.listaConsultaTablas[i].cantporc) * 100;
										}
										
										
										var rec = new modelListadoTablas1({
											nmautser: json.listaConsultaTablas[i].nmautser,
											cdtipaut:json.listaConsultaTablas[i].cdtipaut,
											cdmedico:json.listaConsultaTablas[i].cdmedico,
											nombreMedico:json.listaConsultaTablas[i].cdmedico+" "+json.listaConsultaTablas[i].nombreMedico,
											desccpt:json.listaConsultaTablas[i].cdcpt+" "+json.listaConsultaTablas[i].desccpt,
											precio:json.listaConsultaTablas[i].precio,
											cdtipmed:json.listaConsultaTablas[i].cdtipmed,
											cdcpt:json.listaConsultaTablas[i].cdcpt,
											cantporc: porcentajeCantidad,
											ptimport:json.listaConsultaTablas[i].ptimport,
											descTipMed:json.listaConsultaTablas[i].descTipMed
										});
										if(json.listaConsultaTablas[i].cdtipaut==1)
										{
											storeConceptoAutorizados1.add(rec);
										}
										if(json.listaConsultaTablas[i].cdtipaut==2)
										{
											storeQuirugicoBase1.add(rec);
										}
										if(json.listaConsultaTablas[i].cdtipaut==3)
										{
											storeQuirurgico1.add(rec);
										}
									}
								}
								
								
								Ext.Ajax.request(
								{
									url     : _URL_CONSULTA_DEDUCIBLE_COPAGO1
									,params : 
									{
										'params.cdunieco':Ext.getCmp('unieco1').getValue(),
										'params.estado':Ext.getCmp('estado1').getValue(),
										'params.cdramo':Ext.getCmp('cdramo1').getValue(),
										'params.nmpoliza':Ext.getCmp('polizaAfectada1').getValue(),
										'params.nmsituac':Ext.getCmp('nmsituac1').getValue(),
										'params.cdgarant':Ext.getCmp('cveGarantia1').getValue(),
										'params.subcober':Ext.getCmp('cveSubgarantia1').getValue()
										
										
									}
									,success : function (response)
									{
										var respuesta= Ext.decode(response.responseText);
										if(respuesta.listaDatosSiniestro != null)
										{
											var json=Ext.decode(response.responseText).listaDatosSiniestro[0];
											Ext.getCmp('deducible1').setValue(json.deducible);
											Ext.getCmp('copago1').setValue(json.copago);
											Ext.getCmp('tipoCapago1').setValue(json.tipoCopago);
											
											Ext.Ajax.request(
											{
												url     : _URL_CATALOGOS
												,params:{
													'params.cdpresta': Ext.getCmp('cdproveedor1').getValue(),
													catalogo         : _CAT_PROVEEDORES,
													catalogoGenerico : true
												}
												,success : function (response)
												{
													if(Ext.decode(response.responseText).listaGenerica != null)
													{
														var json=Ext.decode(response.responseText).listaGenerica[0];
														Ext.getCmp('idzonaHospProv1').setValue(json.zonaHospitalaria);
													}
													
														Ext.Ajax.request(
														{
															url     : _URL_CATALOGOS
															,params:{
																'params.cdpresta' : Ext.getCmp('cdmedico1').getValue(),
																catalogo         : _CAT_MEDICOS,
																catalogoGenerico : true
															}
															,success : function (response)
															{
																if(Ext.decode(response.responseText).listaGenerica != null)
																{
																	var json=Ext.decode(response.responseText).listaGenerica[0];
																	Ext.getCmp('especialidad1').setValue(json.descesp);
																	panelInicialPrincipal1.down('textfield[name=nombreMedico]').setValue(json.nombre);
																}
																
																
																Ext.Ajax.request(
																{
																	url     : _URL_POLIZA_UNICA
																	,params : 
																	{
																		'params.cdunieco':Ext.getCmp('idUnieco').getValue(),
																		'params.cdramo':Ext.getCmp('idcdRamo').getValue(),
																		'params.estado':Ext.getCmp('idEstado').getValue(),
																		'params.nmpoliza':Ext.getCmp('polizaAfectada').getValue(),
																		'params.cdperson':Ext.getCmp('idAsegurado').getValue()
																	}
																	,success : function (response)
																	{
																		if(Ext.decode(response.responseText).polizaUnica != null)
																		{
																			var json=Ext.decode(response.responseText).polizaUnica[0];
																			Ext.getCmp('idZonaContratadaPoliza1').setValue(json.zonaContratada);
																			Ext.getCmp('polizaAfectadaCom1').setValue(json.numPoliza);
																			Ext.getCmp('iddsplanAsegurado1').setValue(json.dsplan);
																			// aqui ya todos tienen sus valores

											    				    		Ext.Ajax.request(
																		    {
																		        url     : _URL_EXCLUSION_PENALIZACION
																		        ,params:{
																		            'params.cdunieco':Ext.getCmp('unieco1').getValue(),
																		            'params.estado':Ext.getCmp('estado1').getValue(),
																		            'params.cdramo':Ext.getCmp('cdramo1').getValue(),
																		            'params.nmpoliza':Ext.getCmp('polizaAfectada1').getValue(),
																		            'params.nmsituac':Ext.getCmp('nmsituac1').getValue()
																		        }
																		        ,success : function (response)
																		        {
																		            //asignamos el valor de la exclusion al campo para su posterior uso
																		            Ext.getCmp('idExclusionPenalizacion1').setValue(Ext.decode(response.responseText).existePenalizacion);
																		            // realizamos la validación del circulo hospitalario
																				    if(Ext.getCmp('idExclusionPenalizacion1').getValue()=="S")
																				    {
																				        Ext.getCmp('idPenalCambioZona1').setValue("0");
																				    }else{
																				        //Mandamos a llamar la información del porcentaje que se tiene para idpenalizacion
																				        if(Ext.getCmp('idCausaSiniestro2').getValue() == _CODIGO_CAUSA_ACCIDENTE1 || Ext.getCmp('idCausaSiniestro2').getValue() == _CODIGO_CAUSA_MATERNIDAD1)
																                        {
																                            Ext.getCmp('idPenalCambioZona1').setValue("0");
																                        }else{
																                            Ext.Ajax.request(
																                            {
																                                url     : _URL_PORCENTAJE_PENALIZACION1
																                                ,params:{
																                                    'params.zonaContratada': Ext.getCmp('idZonaContratadaPoliza1').getValue(),
																                                    'params.zonaAtencion': Ext.getCmp('idzonaHospProv1').getValue(),
																                                    'params.cdRamo': Ext.getCmp('cmbRamos2').getValue()
																                                    
																                                }
																                                ,success : function (response)
																                                {
																                                    
																                                    Ext.getCmp('idPenalCambioZona1').setValue(Ext.decode(response.responseText).porcentajePenalizacion);
																                                },
																                                failure : function ()
																                                {
																                                    me.up().up().setLoading(false);
																                                    centrarVentanaInterna(Ext.Msg.show({
																                                        title:'Error',
																                                        msg: 'Error de comunicaci&oacute;n',
																                                        buttons: Ext.Msg.OK,
																                                        icon: Ext.Msg.ERROR
																                                    }));
																                                }
																                            });
																                        }
																				    }
																		        },
																		        failure : function ()
																		        {
																		            me.up().up().setLoading(false);
																		            centrarVentanaInterna(Ext.Msg.show({
																		                title:'Error',
																		                msg: 'Error de comunicaci&oacute;n',
																		                buttons: Ext.Msg.OK,
																		                icon: Ext.Msg.ERROR
																		            }));
																		        }
																		    });
																		}
																	},
																	failure : function ()
																	{
																		me.up().up().setLoading(false);
																		Ext.Msg.show({
																			title:'Error',
																			msg: 'Error de comunicaci&oacute;n',
																			buttons: Ext.Msg.OK,
																			icon: Ext.Msg.ERROR
																		});
																	}
																});
															},
															failure : function ()
															{
																me.up().up().setLoading(false);
																Ext.Msg.show({
																	title:'Error',
																	msg: 'Error de comunicaci&oacute;n',
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																});
															}
														});
												},
												failure : function ()
												{
													me.up().up().setLoading(false);
													Ext.Msg.show({
														title:'Error',
														msg: 'Error de comunicaci&oacute;n',
														buttons: Ext.Msg.OK,
														icon: Ext.Msg.ERROR
													});
												}
											});
										}
									},
									failure : function ()
									{
										me.up().up().setLoading(false);
										Ext.Msg.show({
											title:'Error',
											msg: 'Error de comunicaci&oacute;n',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR
										});
									}
								});
								
							},
							failure : function ()
							{
								me.up().up().setLoading(false);
								Ext.Msg.show({
									title:'Error',
									msg: 'Error de comunicaci&oacute;n',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.ERROR
								});
							}
						});
				}
			}else{
				Ext.Msg.show({
					title: 'Error',
					msg: 'Error al obtener los datos',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				});
			}
		}
		
		
		
	});
});
