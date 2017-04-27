Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
var datosgrid;
var storeConceptoAutorizados;
var storeQuirugicoBase;
var storeQuirugico;
var extraParams='';
var cdrol;
var notasInternas ='';
//var mensajeInicial = ' Movimiento no procede por padecimiento de periodo de espera de ';
var _Existe = "S";
var _NExiste = "N";
Ext.onReady(function() {
	
    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
	Ext.selection.CheckboxModel.override( {
		mode: 'SINGLE',
		allowDeselect: true
	});
	// Conversi�n para el tipo de moneda
	Ext.util.Format.thousandSeparator = ',';
	Ext.util.Format.decimalSeparator = '.';
	////////////////////////////////////////////////
	/////////// 		MODELO				////////
	////////////////////////////////////////////////
	Ext.define('modelListadoCobertura',{
		extend: 'Ext.data.Model',
		fields: [	{type:'string',    name:'cdgarant'},			{type:'string',    name:'dsgarant'},              	{type:'string',    name:'ptcapita'}		]
	});

	Ext.define('modelListadoProvMedico',{
		extend: 'Ext.data.Model',
		fields: [  	{type:'string',    name:'cdpresta'},       		{type:'string',    name:'nombre'},                 	{type:'string',    name:'cdespeci'},
					{type:'string',    name:'descesp'},				{type:'string',    name:'circulo'},					{type:'string',    name:'codpos'},
					{type:'string',    name:'zonaHospitalaria'}]
	});

	Ext.define('modelListadoAsegurado',{
		extend: 'Ext.data.Model',
		fields: [  	{type:'string',    name:'nmautser'},       		{type:'string',    name:'nmautant'},               	{type:'string',    name:'fesolici'},
					{type:'string',    name:'polizaafectada'},		{type:'string',    name:'cdprovee'},				{type:'string',    name:'nombreProveedor'},
					{type:'string',    name:'statusTramite'},		{type:'string',    name:'descICD'},					{type:'string',    name:'cobertura'},
					{type:'string',    name:'subcobertura'}]
	});

	Ext.define('modelListadoPoliza',{
		extend: 'Ext.data.Model',
		fields: [	{type:'string',    name:'cdramo'},				{type:'string',    name:'cdunieco'},				{type:'string',    name:'estado'},
					{type:'string',    name:'nmpoliza'},			{type:'string',    name:'nmsituac'},				{type:'string',    name:'mtoBase'},
					{type:'string',    name:'feinicio'},			{type:'string',    name:'fefinal'},					{type:'string',    name:'dssucursal'},
					{type:'string',    name:'dsramo'},				{type:'string',    name:'estatus'},					{type:'string',    name:'dsestatus'},
					{type:'string',    name:'nmsuplem'},			{type:'string',    name:'cdtipsit'},				{type:'string',    name:'estatusCliente'},
					{type:'string',    name:'faltaAsegurado'},		{type:'date',    name:'fcancelacionAfiliado'},	{type:'string',    name:'mtoBeneficioMax'},
					{type:'string',    name:'zonaContratada'},		{type:'string',    name:'vigenciaPoliza'},			{type:'string',    name:'desEstatusCliente'},
					{type:'string',    name:'numPoliza'},			{type:'string',    name:'dsplan'},					{type:'string',    name:'mesesAsegurado'},
					{type:'string',    name:'dsTipsit'},			{type:'string',	   name:'genero'},					{type:'string',	   name:'fenacimi'},
					{type:'string',    name:'cirHosp'}
					] // (EGS) genero, fenacimi
	});

	Ext.define('modelListadoTmanteni',{
		extend: 'Ext.data.Model',
		fields: [	{type:'string',    name:'cdtabla'},				{type:'string',    name:'codigo'},					{type:'string',    name:'descripc'},
					{type:'string',    name:'descripl'}		]
	});

	Ext.define('modelListadoTabulador',{
		extend: 'Ext.data.Model',
		fields: [	{type:'string',    name:'porcentaje'},			{type:'string',    name:'mtomedico'}	]
	});

	Ext.define('modelListadoTablas',{
		extend: 'Ext.data.Model',
			fields: [	{type:'string',    name:'nmautser'},		{type:'string',    name:'cdtipaut'},				{type:'string',    name:'cdmedico'},
						{type:'string',    name:'nombreMedico'},	{type:'string',    name:'cdtipmed'},				{type:'string',    name:'cdcpt'},
						{type:'string',    name:'desccpt'},			{type:'string',    name:'precio'},					{type:'string',    name:'cantporc'},
						{type:'string',    name:'ptimport'},		{type:'string',    name:'descTipMed'}	]
	});

    Ext.define('ClausulaModelo', {
        extend:'Ext.data.Model',
        fields:['cdclausu','dsclausu','linea_usuario','cdtipcla','linea_general']
    });
    
    Ext.define('ModelHistorialICD', {
        extend:'Ext.data.Model',
        fields:[
            'CLAUSULA',         'DESCLAUSULA',      'ICD',       'DESCICD'
        ]
    });
	//STORE PARA SELECCIONAR EL TIPO DE AUTORIZACION
	storeTipoAutorizacion = Ext.create('Ext.data.JsonStore', {
		model:'Generic',
		proxy: {
			type: 'ajax',
			url: _URL_TIPO_AUTORIZACION,
			reader: {
				type: 'json',
				root: 'tiposAutorizacion'
			}
		}
	});
	storeTipoAutorizacion.load();

	storeAsegurados = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_LISTADO_ASEGURADO,
			reader: {
				type: 'json',
				root: 'listaAsegurado'
			}
		}
	});

	storePlazas = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url:_UR_LISTA_PLAZAS,
			reader: {
				type: 'json',
				root: 'listaPlazas'
			}
		}
	});

	storeTratamiento = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url : _URL_CATALOGOS,
			extraParams : {catalogo:_CAT_TRATAMIENTO},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});

    storeTipEventoGNP = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url : _URL_CATALOGOS,
            extraParams : {catalogo:_CAT_TIPOEVENTOGNP},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
	
	/*Store que contiene la informaci�n general en el grid*/
	storeListadoAsegurado = new Ext.data.Store({
		pageSize : 5
		,model      : 'modelListadoAsegurado'
		,autoLoad  : false
		,proxy     : {
			enablePaging : true,
			reader       : 'json',
			type         : 'memory',
			data         : []
		}
	});

	/*Store que contiene la informaci�n general en el grid para la poliza*/
	storeListadoPoliza = new Ext.data.Store({
		pageSize : 5
		,model      : 'modelListadoPoliza'
		,autoLoad   : false
		,proxy      : {
			enablePaging : true,
			reader       : 'json',
			type         : 'memory',
			data         : []
		}
	});

	var storeMedico = Ext.create('Ext.data.Store', {
		model:'modelListadoProvMedico',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_CATALOGOS,
			extraParams: {
				catalogo         : _CAT_MEDICOS,
				catalogoGenerico : true
			},
			reader: {
				type: 'json',
				root: 'listaGenerica'
			}
		}
	});

	var storeProveedor = Ext.create('Ext.data.Store', {
		model:'modelListadoProvMedico',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_CATALOGOS,
			extraParams:{
				catalogo         : _CAT_PROVEEDORES,
				catalogoGenerico : true
			},
			reader: {
				type: 'json',
				root: 'listaGenerica'
			}
		}
	});

	var storeTiposICD = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url: _URL_LISTA_ICD, // (EGS)
			reader: {
				type: 'json',
				root: 'listaCPTICD'
			}
		},
		listeners: { // se agrega listener (EGS)
			'beforeload' : function(store, operation) {
				store.removeAll();
				store.proxy.extraParams = {
						'params.cdicd' 		: Ext.getCmp('idComboICD').getValue(),
						'params.cdramo' 	: Ext.getCmp('idcdRamo').getValue(),
						'params.cdtipsit' 	: Ext.getCmp('idcdtipsit').getValue(),
						'params.edad' 		: calculaAniosTranscurridos(Ext.getCmp('edad').getValue(),new Date()),
						'params.genero' 	: Ext.getCmp('genero').getValue()
				};
			}
		}
	});
	
    var storeTiposCPTUnico = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url: _URL_LISTA_CDPUNIECO,
            reader: {
                type: 'json',
                root: 'datosValidacionGral'
            }
        },
        listeners: { // se agrega listener (EGS)
            'beforeload' : function(store, operation) {
                store.removeAll();
                store.proxy.extraParams = {
                        'params.cdicd'      : Ext.getCmp('idComboICD').getValue()
                };
            }
        }
    });

    var storeTiposMedicos = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url: _URL_LISTA_TIPOMEDICO,
            reader: {
                type: 'json',
                root: 'datosValidacionGral'
            }
        },
        listeners: { // se agrega listener (EGS)
            'beforeload' : function(store, operation) {
                store.removeAll();
                store.proxy.extraParams = {
                        'params.cdicd'      : Ext.getCmp('idComboICD').getValue(),
                        'params.cdpts'      : Ext.getCmp('idcptGeneral').getValue()
                };
            }
        }
    });
    
	var storeTiposCPT = Ext.create('Ext.data.Store', {
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

	storeTipoMedico = Ext.create('Ext.data.Store', {
		model:'modelListadoTmanteni',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_TIPO_MEDICO,
			reader: {
				type: 'json',
				root: 'listaConsultaManteni'
			}
		}
	});

	storeTabulador = Ext.create('Ext.data.Store', {
		model:'modelListadoTabulador',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_LISTA_TABULADOR,
			reader: {
				type: 'json',
				root: 'listaPorcentaje'
			}
		}
	});

	storeRamos = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url:_UR_LISTA_RAMO_SALUD,
			reader:	{
				type: 'json',
				root: 'listadoRamosSalud'
			}
		}
	});

	//DATOS PARA EL PRIMER GRID --> CONCEPTOS AUTORIZADOS
	storeConceptoAutorizados = new Ext.data.Store({
		autoDestroy: true,						model: 'modelListadoTablas'
	});

	//DATOS PARA EL SEGUNDO GRID --> EQUIPO QUIRURGICO BASE
	storeQuirugicoBase = new Ext.data.Store({
		autoDestroy: true,						model: 'modelListadoTablas'
	});

	//DATOS PARA EL TERCER GRID --> EQUIPO QUIRURGICO
	storeQuirurgico = new Ext.data.Store({
		autoDestroy: true,						model: 'modelListadoTablas'
	});

	var storeCobertura = Ext.create('Ext.data.Store', {
		model:'modelListadoCobertura',
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url : _URL_LISTA_COBERTURA,
			reader: {
				type: 'json',
				root: 'listaCoberturaPoliza'
			}
		}
	});

	var storeSubcobertura = Ext.create('Ext.data.Store', {
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
	
    var storeTipoEvento = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_LISTA_TIPOEVENTO,
            reader: {
                type: 'json',
                root: 'datosValidacionGral'
            }
        }
    });
	

	storeCausaSinestro = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_CAT_CAUSASINIESTRO},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});

	storeAplCirculoHos = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_SINO},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
	storeAplCirculoHos.load();
	
	storeAplZona = Ext.create('Ext.data.Store', {
		model:'Generic',
		autoLoad:true,
		proxy: {
			type: 'ajax',
			url: _URL_CATALOGOS,
			extraParams : {catalogo:_SINO},
			reader: {
				type: 'json',
				root: 'lista'
			}
		}
	});
	storeAplZona.load();
	
    storeAplicaNegoc = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            extraParams : {catalogo:_SINO},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    storeAplicaNegoc.load();
	/////////////////////////////////////////////////////////////////////////////////////////////////
	//1.- INCIO DEL PROCESO DEL MODO DE AUTORIZACION  							/////////////////////
	gridDatos = Ext.create('Ext.grid.Panel',{
		id             : 'clausulasGridId'
		,store         :  storeListadoAsegurado
		,collapsible   : true
		,titleCollapse : true
		,style         : 'margin:5px'
		,selType	   : 'checkboxmodel'
		,width   	   : 600
		,height		   : 280
		,columns       : [
			{	header	: 'N&uacute;mero <br/> Autorizaci&oacute;n',			dataIndex : 'nmautser',			width	   : 100 },
			{	header  : 'N&uacute;mero <br/> Autorizaci&oacute;n Anterior',	dataIndex : 'nmautant',			width      : 200 },
			{	header  : 'Estatus <br/> Tr&aacute;mite',						dataIndex : 'statusTramite',	width      : 200 },
			{	header  : 'Fecha Solicitud',									dataIndex : 'fesolici',			width      : 100 },
			{	header  : 'P&oacute;liza <br/> Afectada',						dataIndex : 'polizaafectada' ,	width	   : 100 },
			{	header  : 'Clave Proveedor' ,									dataIndex : 'cdprovee'	 ,		width	   : 100 },
			{	header  : 'Nombre del Proveedor',								dataIndex : 'nombreProveedor',	width	   : 200 },
			{	header  : 'Internamiento',										dataIndex : 'descICD',			width	   : 200 },
			{	header  : 'Cobertura',											dataIndex : 'cobertura',		width	   : 200 },
			{	header  : 'Subcobertura',										dataIndex : 'subcobertura',		width	   : 200 }
		],
		bbar: {
			displayInfo : true,
			store       : storeListadoAsegurado,
			xtype       : 'pagingtoolbar'
		},
		listeners: {
			itemclick: function(dv, record, item, index, e) {
				idTipoAutorizacion= Ext.getCmp('tipoAutorizacion').getValue();
				if(idTipoAutorizacion !=1){
					if(Ext.getCmp('clausulasGridId').getSelectionModel().hasSelection()){
						var rowSelected = Ext.getCmp('clausulasGridId').getSelectionModel().getSelection()[0];
						debug("Valor Seleccionado : ",rowSelected);
						var nmautser= rowSelected.get('nmautser');
						//debug("VALOR DE valorAction.cdrol -->"+valorAction.cdrol);
						cargarInformacionAutorizacionServicio(nmautser,null,valorAction.cdrol);
					}else {
						centrarVentanaInterna(Ext.Msg.show({
							title: 'Aviso',
							msg: 'Debe de seleccionar un registro para realizar la edici&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						}));
					}
				}
				modificacionClausula.hide();
			}
		}
	});
	
	panelbusquedas = Ext.create('Ext.panel.Panel',{
		border  : 0,		id     : 'panelbusqueda',				width	: 600,		style  : 'margin:5px'
		,items : [
			Ext.create('Ext.form.field.ComboBox',{
				fieldLabel 	: 'Nombre/C&oacute;digo asegurado',		allowBlank		: false,		displayField 	: 'value'
				,id			: 'idCodigoAsegurado',					labelWidth		: 170,			width		 	: 500
				,valueField   	: 'key',							forceSelection 	: true,			matchFieldWidth	: false
				,minChars  	: 2,									queryMode 		:'remote',		queryParam		: 'params.cdperson'
				,store 		: storeAsegurados,						hideTrigger		:true,			triggerAction	: 'all'
			})
		]
		,buttonAlign: 'center'
		,buttons : [{
			text: "Buscar"
			,icon:_CONTEXT+'/resources/fam3icons/icons/magnifier.png'
			,handler: function(){
			if (panelClausula.form.isValid()) {
				storeListadoAsegurado.removeAll();
				var params = {
					'params.cdperson' : Ext.getCmp('idCodigoAsegurado').getValue(),
					'params.tipoAut'  : Ext.getCmp('tipoAutorizacion').getValue()
				};
				
				cargaStorePaginadoLocal(storeListadoAsegurado, _URL_CONSULTA_LISTADO_AUTORIZACION, 'listaAutorizacion', params, function(options, success, response){
					if(success){
						var jsonResponse = Ext.decode(response.responseText);
						if(jsonResponse.listaAutorizacion == null) {
							centrarVentanaInterna(Ext.Msg.show({
								title: 'Aviso',
								msg: 'No se encontraron datos.',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.WARNING
							}));
							return;
						}
					}else{
						centrarVentanaInterna(Ext.Msg.show({
							title: 'Aviso',
							msg: 'Error al obtener los datos.',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						}));
					}
				});
			} else {
				centrarVentanaInterna(Ext.Msg.show({
					title: 'Aviso',
					msg: 'Complete la informaci&oacute;n requerida',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING
				}));
			}
		}
		}] 
	});


	Ext.getCmp('panelbusqueda').hide();
	Ext.getCmp('clausulasGridId').hide();
	panelClausula= Ext.create('Ext.form.Panel', {
		id: 'panelClausula',
		border:0,
		bodyPadding: 5,
		defaults 	: {	
			style : 'margin:5px;'
		},
		items: [
			{	xtype: 'combo',id:'tipoAutorizacion',	fieldLabel: 'Tipo autorizaci&oacute;n',			store: storeTipoAutorizacion,
				queryMode:'local',						displayField: 'value',							valueField: 'key',
				blankText:'Es un dato requerido',		editable:false,									labelWidth : 170,
				width: 400,								emptyText:'Seleccione Autorizaci&oacute;n ...',	allowBlank:false,
				listeners : {
					'select' : function(combo, record) {
						closedStatusSelectedID = this.getValue();
						debug("Valor Seleccionado Inicial -->",closedStatusSelectedID);
						Ext.getCmp('claveTipoAutoriza').setValue(closedStatusSelectedID);
						Ext.getCmp('cveTipoAutorizaG').setValue(closedStatusSelectedID);
						Ext.getCmp('idEstatusTramite').setValue('0');
						Ext.getCmp('idaplicaCirHosp').setValue('S');
						Ext.getCmp('idaplicaZona').setValue('S');
						Ext.getCmp('idaplicaNegociacion').setValue('N');
						Ext.getCmp('idTipoEventoGNP').setValue('CP');
						Ext.getCmp('idCopagoPrevio').hide();
						Ext.getCmp('idTipoEvento').hide();
						Ext.getCmp('idSucursal').setValue('13');
                        Ext.getCmp('idCausaSiniestro').setValue('1');
                        Ext.getCmp('tratamiento').setValue('1');
                        Ext.getCmp('sumDisponible').setValue('0');
                        						
						if(closedStatusSelectedID !=1){
							Ext.getCmp('panelbusqueda').show();
							Ext.getCmp('clausulasGridId').show();
							if(cdrol=="COORDINAMED"){
								Ext.getCmp('Autorizar').hide();
							}else{
								Ext.getCmp('Autorizar').show();
							}
						}else{
							Ext.getCmp('panelbusqueda').hide();
							Ext.getCmp('clausulasGridId').hide();
							Ext.getCmp('idNumeroAnterior').hide();
							Ext.getCmp('btnBuscar').hide();
							if(cdrol=="COORDINAMED"){
								Ext.getCmp('Autorizar').hide();
							}else{
								Ext.getCmp('Autorizar').show();
							}
							modificacionClausula.hide();
						}
					}
				}
			}
			,panelbusquedas 	// <-- contiene el formulario para la busqueda de acuerdo al codigo del asegurado
			,gridDatos 			// <-- Contiene la informaci�n de los asegurados
		]
	});

	modificacionClausula = Ext.create('Ext.window.Window',{
		title        : 'Modo de Autorizaci&oacute;n'
		,modal       : true
		,buttonAlign : 'center'
		,closable    : false
		,width		 : 650
		,minHeight 	 : 100 
		,maxheight   : 400
		,items       : [
			panelClausula
		]
	});

	/****************************************************
	* Validacion inicial para si la autorizacion       *
	* es un tramite nuevo o uno anterior               *
	***************************************************/
	if(valorAction.nmAutSer == null && valorAction.ntramite ==null){
		cdrol = valorAction.cdrol;
		storeTipoMedico.load({
			params:{
				'params.codigo': null
			}
		});
		
		storeMedico.load();
		storeTipEventoGNP.load();
		storeProveedor.load();
		centrarVentanaInterna(modificacionClausula.show()); 
		
	}else{
		storeMedico.load();
		storeTratamiento.load();
		storeTipEventoGNP.load();
		storePlazas.load();
		storeProveedor.load();
		storeTipoMedico.load({
			params:{
				'params.codigo': null
			}
		});
		storeTabulador.load();
		storeTipoAutorizacion.load();
		cdrol = valorAction.cdrol;
		cargarInformacionAutorizacionServicio(valorAction.nmAutSer, valorAction.ntramite,valorAction.cdrol); //-->
	}

	cmbRamos = Ext.create('Ext.form.field.ComboBox',{
		colspan	   :2,			fieldLabel   : 'Producto',		id        : 'cmbRamos',		allowBlank     : false,	
		editable   : false,		displayField : 'value',			valueField: 'key',			forceSelection : false,
		width	   :450,		labelWidth   : 170,				queryMode :'local',			name           :'cmbRamos'
		,store : storeRamos
		,listeners : {
			'select' : function(combo, record) {
				Ext.getCmp('idAsegurado').setValue('');
				panelInicialPrincipal.down('[name="cdperson"]').setReadOnly(false);
				Ext.getCmp('polizaAfectadaCom').setValue('');
			}
		}
	});
	
	tipoEventoGNP = Ext.create('Ext.form.field.ComboBox',{
        colspan    :2,          fieldLabel   : 'Tipo Evento',    id        : 'idTipoEventoGNP',      allowBlank     : false, 
        editable   : false,     displayField : 'value',                 valueField: 'key',              forceSelection : true,
        width      :500,        labelWidth   : 170,                     queryMode : 'local',            name           :'idTipoEventoGNP'
        ,store : storeTipEventoGNP
        ,listeners : {
            'select' : function(combo, record) {
                if(this.getValue()=="RH"){
                    Ext.getCmp('fechaAutorizacion').setValue(new Date());
                    Ext.getCmp('fechaIngreso').setValue(new Date());
                    Ext.getCmp('fechaAutorizacion').setReadOnly(true);
                    Ext.getCmp('fechaIngreso').setReadOnly(true);
                }else{
                	Ext.getCmp('fechaAutorizacion').setValue('');
                    Ext.getCmp('fechaIngreso').setValue('');
                    Ext.getCmp('fechaAutorizacion').setReadOnly(false);
                    Ext.getCmp('fechaIngreso').setReadOnly(false);
                }
            }
        }
    });

	gridDatosPoliza= Ext.create('Ext.grid.Panel',{
		id            : 'polizaGridId',
		store         : storeListadoPoliza,
		selType	      : 'checkboxmodel',
		width         : 700,
		height		  : 200,
		columns       :[
			{	header : 'N&uacute;mero de P&oacute;liza',		dataIndex : 'numPoliza',			width	: 200        },
			{   header : 'Estatus p&oacute;liza ',				dataIndex : 'dsestatus',			width	: 100        },
			{   header : 'Vigencia p&oacute;liza <br/> ' +
			'Fecha inicio \t\t  |  \t\t ' +
			'Fecha fin  ',					                    dataIndex : 'vigenciaPoliza',		width	: 200        },
			{   header : 'Fecha alta <br/> asegurado',			dataIndex : 'faltaAsegurado',		width	: 100        },
			{   header : 'Fecha cancelaci&oacute;n ' +
				'<br/> asegurado',						        dataIndex : 'fcancelacionAfiliado',	width	: 150        },
			{   header : 'Estatus<br/> asegurado',				dataIndex : 'desEstatusCliente',	width	: 100        },
			{   header : 'Producto',							dataIndex : 'dsramo',				width   : 150        },
			{   header : 'Modalidad',							dataIndex : 'dsTipsit',				width   : 150        },
			{   header : 'Sucursal',							dataIndex : 'dssucursal',			width   : 150        },
			{   header : 'Estado',								dataIndex : 'estado',				width	: 100        },
			{   header : 'N&uacute;mero de Situaci&oacute;n',	dataIndex : 'nmsituac',		        width	: 150        }
		],
		bbar : {
			displayInfo : true,
			store       : storeListadoPoliza,
			xtype       : 'pagingtoolbar'
		},
		listeners: {
			itemclick: function(dv, record, item, index, e){
				Ext.getCmp('idUnieco').setValue(record.get('cdunieco'));
				Ext.getCmp('idEstado').setValue(record.get('estado'));
				Ext.getCmp('idcdRamo').setValue(record.get('cdramo'));
				Ext.getCmp('idNmSituac').setValue(record.get('nmsituac'));
				Ext.getCmp('polizaAfectada').setValue(record.get('nmpoliza'));
				Ext.getCmp('polizaAfectadaCom').setValue(record.get('numPoliza'));
				Ext.getCmp('iddsplanAsegurado').setValue(record.get('dsplan'));
				Ext.getCmp('idModalidad').setValue(record.get('dsTipsit'));
				if(record.get('mtoBase') == null || record.get('mtoBase')==''){
					Ext.getCmp('idMontoBase').setValue("21000");
				}else{
					Ext.getCmp('idMontoBase').setValue(record.get('mtoBase'));
				}
				Ext.getCmp('idNmsuplem').setValue(record.get('nmsuplem'));
				Ext.getCmp('idZonaContratadaPoliza').setValue(record.get('zonaContratada'));
				Ext.getCmp('idcdtipsit').setValue(record.get('cdtipsit'));
				Ext.getCmp('idMesesAsegurado').setValue(record.get('mesesAsegurado'));
				Ext.getCmp('edad').setValue(record.get('fenacimi'));
				Ext.getCmp('genero').setValue(record.get('genero'));
				Ext.getCmp('idcirculohosPoliza').setValue(record.get('cirHosp'));//-->
				
				Ext.getCmp('btnExclusionPoliza').setDisabled(false);
				
				Ext.getCmp('btnSiniestralidad').setDisabled(false);
                Ext.getCmp('btnVerCoberturas').setDisabled(false);
                Ext.getCmp('btnDetallePoliza').setDisabled(false);

				var storeClausulasPoliza = new Ext.data.Store({
                    model      : 'ClausulaModelo',
                    //autoLoad: true,
                    proxy     : {
                        type        : 'ajax',
                        url         : _URL_CONSULTA_CLAUSULAS_POLIZA,
                        extraParams : {
                            'params.cdunieco' : Ext.getCmp('idUnieco').getValue(),
                            'params.cdramo'   : Ext.getCmp('idcdRamo').getValue(),
                            'params.estado'   : Ext.getCmp('idEstado').getValue(),
                            'params.nmpoliza' : Ext.getCmp('polizaAfectada').getValue(),
                            'params.nmsituac' : Ext.getCmp('idNmSituac').getValue(),
                            'params.icodpoliza' : null,
                            'params.cdperson'  : null
                        },
                        reader : {
                            type : 'json',
                            root : 'clausulasPoliza'
                        }
                    }
                });
				storeClausulasPoliza.load({
                    callback: function(records, operation, success) {
                    	debug("Valor storeClausulasPoliza de respuesta 1.- ==> ",records);
                        debug("Valor storeClausulasPoliza de respuesta 2.- ==> ",records.length);
                        if (!success) {
                            return;
                        }
                        if(records.length == 0){
                            Ext.getCmp('btnExclusionPoliza').setDisabled(true);
                        }else{
                        	Ext.getCmp('btnExclusionPoliza').setDisabled(false);
                        }
                        return;
                    }
                });
                
                var storeHistorialICD= new Ext.data.Store({
                    model      : 'ModelHistorialICD',
                    proxy     : {
                        type        : 'ajax',
                        url         : _URL_cargaHistorialSinies,
                        extraParams : {
                            'params.pv_cdunieco_i' : Ext.getCmp('idUnieco').getValue(),
                            'params.pv_cdramo_i'   : Ext.getCmp('idcdRamo').getValue(),
                            'params.pv_estado_i'   : Ext.getCmp('idEstado').getValue(),
                            'params.pv_nmpoliza_i' : Ext.getCmp('polizaAfectada').getValue(),
                            'params.pv_nmsuplem_i' : Ext.getCmp('idNmsuplem').getValue(),
                            'params.pv_nmsituac_i' : Ext.getCmp('idNmSituac').getValue()
                        },
                        reader : {
                            type : 'json',
                            root : 'loadList'
                        }
                    }
                });
                storeHistorialICD.load({
                    callback: function(records, operation, success) {
                    	
                    	debug("Valor de respuesta 1.- ==> ",records);
                    	debug("Valor de respuesta 2.- ==> ",records.length);
                    	
                        if (!success) {
                            return;
                        }
                        if(records.length == 0){
                            Ext.getCmp('btnExclusionICD').setDisabled(true);
                        }else{
                            Ext.getCmp('btnExclusionICD').setDisabled(false);
                        }
                        return;
                    }
                });
                
                debug("storeHistorialICD ==>",storeHistorialICD);
                
                
                
                
                
				storeCobertura.load({
					params:{
						'params.cdunieco':Ext.getCmp('idUnieco').getValue(),
						'params.estado':Ext.getCmp('idEstado').getValue(),
						'params.cdramo':Ext.getCmp('idcdRamo').getValue(),
						'params.nmpoliza':Ext.getCmp('polizaAfectada').getValue(),
						'params.nmsituac':Ext.getCmp('idNmSituac').getValue()
					}
				});
				
				storeTiposICD.removeAll();
				storeTiposICD.load({
					params:{
						'params.cdramo' 	: Ext.getCmp('idcdRamo').getValue(),
						'params.cdtipsit' 	: Ext.getCmp('idcdtipsit').getValue(),
						'params.edad' 		: calculaAniosTranscurridos(Ext.getCmp('edad').getValue(),new Date()),
						'params.genero' 	: Ext.getCmp('genero').getValue()
					}
				
				});
				storeListadoAsegurado.removeAll();
				modificacionPolizas.hide();
			}
		}
	});
	gridDatosPoliza.store.sort([{
		property    : 'nmpoliza',			direction   : 'DESC'
	}]);

	//PANTALLA QUE CARGA LOS VALORES DEL PANEL 
	modificacionPolizas = Ext.create('Ext.window.Window',{
		title        : 'Listado de P&oacute;liza'
		,modal       : true
		,buttonAlign : 'center'
		,closable 	 : false
		,width		 : 710
		,minHeight 	 : 100 
		,maxheight      : 400
		,items       :[
			gridDatosPoliza
		]
	});

	asegurado = Ext.create('Ext.form.field.ComboBox',{
		/*colspan:2,*/						fieldLabel     : 'Asegurado',		allowBlank     : false,			displayField : 'value',
		id     :'idAsegurado',			labelWidth 	   : 170,				valueField     : 'key',			queryParam   : 'params.cdperson',
		width  :500,					forceSelection : true,				matchFieldWidth: false,			queryMode    :'remote',
		minChars  : 2,					store 		   : storeAsegurados,	triggerAction  : 'all',			name:'cdperson',
		hideTrigger:true,				readOnly	   : true,
		listeners : {
			'select' : function(combo, record) {
			obtieneCDPerson = this.getValue();
			storeListadoPoliza.removeAll();
			storeSubcobertura.removeAll();
			storeCobertura.removeAll();
			Ext.getCmp('idDeducible').setValue('');
			Ext.getCmp('idTipoCopago').setValue('');
			Ext.getCmp('idCopago').setValue('');
			Ext.getCmp('idCobAfectada').reset();
			Ext.getCmp('idSubcobertura').reset();
			Ext.getCmp('dsNombreAsegurado').setValue(asegurado.rawValue);

			var params = {
				'params.cdperson' : obtieneCDPerson,
				'params.cdramo' : Ext.getCmp('cmbRamos').getValue()
			};

			cargaStorePaginadoLocal(storeListadoPoliza, _URL_CONSULTA_LISTADO_POLIZA, 'listaPoliza', params, function(options, success, response){
				if(success){
					var jsonResponse = Ext.decode(response.responseText);
					if(jsonResponse.listaPoliza == null) {
						centrarVentanaInterna(Ext.Msg.show({
							title: 'Aviso',
							msg: 'No se encontraron P&oacute;liza de dicho asegurado',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.WARNING
						}));
						Ext.getCmp('idAsegurado').setValue('');
						storeListadoAsegurado.removeAll();
						modificacionPolizas.hide();
						return;
					}
				}else{
					centrarVentanaInterna(Ext.Msg.show({
						title: 'Aviso',
						msg: 'Error al obtener los datos.',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					}));
				}
			});
			storeListadoAsegurado.removeAll();
			centrarVentanaInterna(modificacionPolizas.show());
			}
		}
	});	

	sucursal = Ext.create('Ext.form.field.ComboBox',{
		colspan		:2,					fieldLabel   : 'Sucursales',	id: 'idSucursal',				allowBlank: false,			width:350	
		,editable   : false,			displayField : 'value',			valueField:'key',			    forceSelection : true
		,labelWidth : 170,				queryMode    :'local',			editable:false,					name:'cduniecs'
		,store : storePlazas
	});
	
    tipoEvento = Ext.create('Ext.form.field.ComboBox',{
        colspan     :2,                 fieldLabel   : 'Tipo Evento',   id: 'idTipoEvento',             allowBlank: false,          width:500
        ,editable   : false,            displayField : 'value',         valueField:'key',               forceSelection : true
        ,labelWidth : 170,              queryMode    :'local',          editable:false,                 name:'idTipoEvento'
        ,store : storeTipoEvento
    });

	coberturaAfectada = Ext.create('Ext.form.field.ComboBox',{
		colspan:2, width:400,
		fieldLabel :'Cobertura afectada',	allowBlank: false,			displayField : 'dsgarant',		id:'idCobAfectada',		name:'cdgarant',
		labelWidth: 170,					valueField   : 'cdgarant',	forceSelection : true,			matchFieldWidth: false,
		queryMode :'local',					store : storeCobertura,		triggerAction: 'all',			editable:false,
		listeners : {
			'select' : function(combo, record) {
				Ext.getCmp('idSubcobertura').reset();
				Ext.getCmp('idSubcobertura').setValue('');
				Ext.getCmp('idDeducible').setValue('');
				Ext.getCmp('idTipoCopago').setValue('');
				Ext.getCmp('idCopago').setValue('');
				storeSubcobertura.removeAll();
				storeSubcobertura.load({
					params:{
						'params.cdunieco' : Ext.getCmp('idUnieco').getValue(),
						'params.cdramo'   : Ext.getCmp('idcdRamo').getValue(),
						'params.estado'   : Ext.getCmp('idEstado').getValue(),
						'params.nmpoliza' : Ext.getCmp('polizaAfectada').getValue(),
						'params.nmsituac' : Ext.getCmp('idNmSituac').getValue(),
						'params.cdtipsit' : Ext.getCmp('idcdtipsit').getValue(),
						'params.cdgarant' : Ext.getCmp('idCobAfectada').getValue(),
						'params.cdsubcob' : null
					}
				});
				
				storeTipoEvento.removeAll();
                storeTipoEvento.load({
                    params:{
                        'params.cdramo'      : Ext.getCmp('idcdRamo').getValue(),
                        'params.cdtipsit'    : Ext.getCmp('idcdtipsit').getValue(),
                        'params.cdgarant'    : Ext.getCmp('idCobAfectada').getValue(),
                        'params.cveCatalogo' : "1"
                    }
                });
			}
		}
	});

	subCobertura = Ext.create('Ext.form.field.ComboBox',{
		colspan:2, 		width:550,
		fieldLabel : 'Subcobertura',	allowBlank: false,				displayField : 'value',			id:'idSubcobertura',		name:'cdconval',
		labelWidth: 170,				valueField   : 'key',			forceSelection : true,			matchFieldWidth: false,
		queryMode :'local',			store : storeSubcobertura,		triggerAction: 'all',			editable:false,
		listeners : {
			'select':function(e){
				if(Ext.getCmp('cveTipoAutorizaG').getValue()=="3" && (Ext.getCmp('claveTipoAutoriza').getValue()=="2" ||Ext.getCmp('claveTipoAutoriza').getValue()=="3" )){
					Ext.getCmp('cveTipoAutorizaG').setValue("2");
				}
				obtieneDatosCoberturaSubCobertura();
			}
		}
	});

	proveedor = Ext.create('Ext.form.field.ComboBox',{
		colspan:2,						fieldLabel : 'Proveedor',		allowBlank: false,					displayField : 'nombre',		name:'cdprovee',
		id:'idProveedor',				labelWidth: 170,				valueField   : 'cdpresta',			forceSelection : true,	width:500,
		matchFieldWidth: false,			queryMode :'remote',			queryParam: 'params.cdpresta',		store : storeProveedor,
		minChars  : 2,					triggerAction: 'all',			hideTrigger:true,
		listeners : {
			'select':function(field,value){
				obtieneInformacion();
				if(validaRamoTramite (Ext.getCmp('idcdRamo').getValue()) == false){
					obtieneCirHospitalarioMultisalud();
				}
			}
		}
	});

	medico = Ext.create('Ext.form.field.ComboBox',{
		colspan:2,fieldLabel: 'M&eacute;dico',		allowBlank   : false,			displayField : 'nombre',				name		   :'cdmedico',
		id				: 'idMedico',				labelWidth   : 170,			    valueField   : 'cdpresta',				forceSelection : true,
		matchFieldWidth : false,					triggerAction: 'all',			queryParam   : 'params.cdpresta',		store          : storeMedico,
		minChars  		: 2,						queryMode    :'remote',			hideTrigger:true,		width:500,
		listeners : {
			'select':function(field,value){
				if(this.getValue() =='0'){
					Ext.getCmp('medicoPExt').show();
					Ext.getCmp('especialidadPExt').show();
					Ext.getCmp('idEspecialidad').hide();
				}else{
					Ext.getCmp('medicoPExt').hide();
					Ext.getCmp('especialidadPExt').hide();
					Ext.getCmp('idEspecialidad').show();
				}
				
				Ext.getCmp('idEspecialidad').setValue('');
				especialidadMedico(this.getValue());
			}
		}
	});

	comboICD = Ext.create('Ext.form.field.ComboBox', {
		colspan:2,						fieldLabel : 'ICD X',					allowBlank: false,				displayField : 'value',		width:500,	//displayField : 'value',
		id:'idComboICD',				labelWidth: 170,					valueField   : 'key',			forceSelection : true,
		matchFieldWidth: false,			queryMode :'remote',				queryParam: 'params.otclave1',	store : storeTiposICD,	// queryParam: 'params.otclave' (EGS)
		minChars  : 2,					name:'cdicd',	editable:true,		triggerAction: 'all',			hideTrigger:true,
		listeners : {
			'select':function(field,value){
				storeTiposCPTUnico.removeAll();
                 storeTiposCPTUnico.load({
                    params:{
                        'params.cdicd'   : this.getValue()
                    }                    
                 });
				 
				 if(Ext.getCmp('idcdRamo').getValue() == _GMMPRUEBA || Ext.getCmp('idcdRamo').getValue() == _GMMI){
				 	//Validamos Exclusiones
				 	Ext.Ajax.request({
                        url     : _URL_VALIDA_EXCLUSION
                        ,params : {
                            'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
                            'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
                            'params.estado'    : Ext.getCmp('idEstado').getValue(),
                            'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
                            'params.nmsuplem'  : Ext.getCmp('idNmsuplem').getValue(),
                            'params.nmsituac'  : Ext.getCmp('idNmSituac').getValue(),
                            'params.cdicd'     : Ext.getCmp('idComboICD').getValue()
                        }
                        ,success : function (response){
                            var resEspera = Ext.decode(response.responseText).msgResult;
                            debug("Valor de respuesta ==> ",resEspera);
                            if(resEspera =="S"){
                            	mensajeCorrecto('Atenci&oacute;n','El Diagn&oacutestico no procede por exclusi&oacute;n',function(){
                                    guardadoAutorizacionServicio(_Existe,'1');
                                });
                            }else{
                            	Ext.Ajax.request({
                                    url     : _URL_NUM_MESES_TIEMPO_ESPERA
                                    ,params : {
                                        'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
                                        'params.cdtipsit': Ext.getCmp('idcdtipsit').getValue(),
                                        'params.cdicd'   : Ext.getCmp('idComboICD').getValue(),
                                        'params.dsplan'  : Ext.getCmp('iddsplanAsegurado').getValue()
                                    }
                                    ,success : function (response){
                                        var resEspera = Ext.decode(response.responseText);
                                        var tiempoEsperaICD = resEspera.mesesTiempoEspera;
                                        if(!(+Ext.getCmp('idMesesAsegurado').getValue() >= +tiempoEsperaICD)){
                                            if(Ext.getCmp('idCausaSiniestro').getValue()!= _CODIGO_CAUSA_ACCIDENTE){
                                                notasInternas= Ext.getCmp('notaInterna').getValue() +" ICD :" +comboICD.rawValue + resEspera.mensaje;
                                                mensajeCorrecto('Atenci&oacute;n',resEspera.mensaje,function(){
                                                    guardadoAutorizacionServicio(_Existe,'1');
                                                });
                                            }
                                        }else{
                                        	Ext.getCmp('Autorizar').show();
                                            Ext.Ajax.request({
                                                url     : _URL_DATOS_COMPLEMENTO_ICD
                                                ,params : {
                                                    'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
                                                    'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
                                                    'params.estado'    : Ext.getCmp('idEstado').getValue(),
                                                    'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
                                                    'params.cdicd'     : Ext.getCmp('idComboICD').getValue(),
                                                    'params.cdperson'  : Ext.getCmp('idAsegurado').getValue()
                                                }
                                                ,success : function (response){
                                                    var resEspera = Ext.decode(response.responseText).msgResult;
                                                    debug("Valor de respuesta ==> ",resEspera);
                                                    var fields = resEspera.split('|');
                                                    var subsecuente = fields[0];
                                                    var noReclamo   = fields[1];
                                                    
                                                    if(subsecuente =="N"){
                                                         Ext.getCmp('idTipo').setValue("NUEVO");
                                                    }else{
                                                         Ext.getCmp('idTipo').setValue("SUBSECUENTE");
                                                    }
                                                    Ext.getCmp('idNumSubsecuente').setValue(noReclamo);
                                                },
                                                failure : function (){
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
                                    failure : function (){
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
                        failure : function (){
                            me.up().up().setLoading(false);
                            centrarVentanaInterna(Ext.Msg.show({
                                title:'Error',
                                msg: 'Error de comunicaci&oacute;n',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            }));
                        }
                    });
				 	
				 	
				 	
                    /*Ext.Ajax.request({
                        url     : _URL_NUM_MESES_TIEMPO_ESPERA
                        ,params : {
                            'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
                            'params.cdtipsit': Ext.getCmp('idcdtipsit').getValue(),
                            'params.cdicd'   : this.getValue(),
                            'params.dsplan'  : Ext.getCmp('iddsplanAsegurado').getValue()
                        }
                        ,success : function (response){
                            var resEspera = Ext.decode(response.responseText);
                            var tiempoEsperaICD = resEspera.mesesTiempoEspera;
                            if(!(+Ext.getCmp('idMesesAsegurado').getValue() >= +tiempoEsperaICD)){
                                if(Ext.getCmp('idCausaSiniestro').getValue()!= _CODIGO_CAUSA_ACCIDENTE){
                                    Ext.getCmp('idComboICD').setValue('');
                                    notasInternas= Ext.getCmp('notaInterna').getValue() +" ICD :" +comboICD.rawValue + resEspera.mensaje;
                                    //Ext.getCmp('notaInterna').setValue(notasInternas);
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title: 'Error',
                                        msg: resEspera.mensaje,
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.WARNING
                                    }));
                                    
                                }
                            }else{
                                Ext.Ajax.request({
                                    url     : _URL_DATOS_COMPLEMENTO_ICD
                                    ,params : {
                                        'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
                                        'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
                                        'params.estado'    : Ext.getCmp('idEstado').getValue(),
                                        'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
                                        'params.cdicd'     : Ext.getCmp('idComboICD').getValue(),
                                        'params.cdperson'  : Ext.getCmp('idAsegurado').getValue()
                                    }
                                    ,success : function (response){
                                        var resEspera = Ext.decode(response.responseText).msgResult;
                                        debug("Valor de respuesta ==> ",resEspera);
                                        var fields = resEspera.split('|');
                                        var subsecuente = fields[0];
                                        var noReclamo   = fields[1];
                                        
                                        if(subsecuente =="N"){
                                        	 Ext.getCmp('idTipo').setValue("NUEVO");
                                        }else{
                                        	 Ext.getCmp('idTipo').setValue("SUBSECUENTE");
                                        }
                                        Ext.getCmp('idNumSubsecuente').setValue(noReclamo);
                                    },
                                    failure : function (){
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
                        failure : function (){
                            me.up().up().setLoading(false);
                            centrarVentanaInterna(Ext.Msg.show({
                                title:'Error',
                                msg: 'Error de comunicaci&oacute;n',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            }));
                        }
                    });*/
				 }else{
                    Ext.Ajax.request({
                        url     : _URL_NUM_MESES_TIEMPO_ESPERA
                        ,params : {
                            'params.otvalor01': this.getValue(),
                            'params.cdtabla'  : 'TAPERESP',
                            'params.cdramo'   : Ext.getCmp('idcdRamo').getValue()
                        }
                        ,success : function (response){
                            var resEspera = Ext.decode(response.responseText);
                            var tiempoEsperaICD = resEspera.mesesTiempoEspera;
                            if(!(+Ext.getCmp('idMesesAsegurado').getValue() >= +tiempoEsperaICD)){
                                if(Ext.getCmp('idCausaSiniestro').getValue()!= _CODIGO_CAUSA_ACCIDENTE){
                                    Ext.getCmp('idComboICD').setValue('');
                                    notasInternas= Ext.getCmp('notaInterna').getValue() +" ICD :" +comboICD.rawValue + resEspera.mensaje;
                                    //Ext.getCmp('notaInterna').setValue(notasInternas);
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title: 'Error',
                                        msg: resEspera.mensaje,
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.WARNING
                                    }));
                                }
                            }
                        },
                        failure : function (){
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
		}
	});

	causaSiniestro = Ext.create('Ext.form.field.ComboBox',{
		colspan	   :2,				fieldLabel   : 'Causa siniestro',	id		  : 'idCausaSiniestro',			allowBlank		: false,			width:500,
		editable   : false,			displayField : 'value',				valueField:'key',			    		forceSelection  : true,
		labelWidth : 170,			queryMode    :'local',				editable  :false,						name			:'cdcausa',
		store : storeCausaSinestro,
		listeners : {
			'select':function(field,value){
				Ext.Ajax.request({
					url     : _URL_VAL_CAUSASINI
					,params : {
						'params.cdramo'   : Ext.getCmp('idcdRamo').getValue(),
						'params.cdtipsit' : Ext.getCmp('idcdtipsit').getValue(),
						'params.causaSini': 'CAUSA',
						'params.cveCausa' : this.getValue()
					}
					,success : function (response){
						var datosExtras = Ext.decode(response.responseText);
						if(Ext.decode(response.responseText).datosInformacionAdicional != null){
							var cveCauSini=Ext.decode(response.responseText).datosInformacionAdicional[0];
							debug("cveCauSini ===>",cveCauSini);
							Ext.getCmp('idCausaSini').setValue(cveCauSini.REQVALIDACION);
							
							if(cveCauSini.REQVALIDACION =="S"){
								obtieneInformacion();
							}else{
								Ext.getCmp('notaInterna').setValue('');
								Ext.getCmp('idCopagoFin').setValue('0');
								Ext.getCmp('idCopagoPrevio').setValue('0');
								Ext.getCmp('idPenalCircHospitalario').setValue('0');
								Ext.getCmp('idPenalCambioZona').setValue('0');
							}
							if(Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_MATERNIDAD){
								debug("1.- Entra a 1");
								var salarioMin = Ext.getCmp('idSalarioMin').getValue();
								Ext.getCmp('sumDisponible').setValue(salarioMin);
							}
						}
					},
					failure : function (){
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
	});

	aplicaCirHosp = Ext.create('Ext.form.field.ComboBox',{
		colspan	   :2,				fieldLabel   : 'Aplica penalizaci&oacute;n circulo hospitalario',	id		  : 'idaplicaCirHosp',			allowBlank		: false,//			width:350,
		editable   : false,			displayField : 'value',				valueField:'key',			    		forceSelection  : true,
		labelWidth : 170,			queryMode    :'local',				editable  :false,						name			:'idaplicaCirHosp',
		store : storeAplCirculoHos,	readOnly: (Ext.isEmpty(cdrol) || cdrol != "GERMEDMULTI"),
		listeners : {
			'select' : function(combo, record) {
				if(this.getValue() =='N'){
					Ext.getCmp('idPenalCircHospitalario').setValue('0');
					validacionCopagoTotal()
				}else{
					obtieneInformacion();
				}
				
			}
		}
	});

	aplicaZona = Ext.create('Ext.form.field.ComboBox',{
		colspan	   :2,				fieldLabel   : 'Aplica penalizaci&oacute;n cambio de zona',	id		  : 'idaplicaZona',			allowBlank		: false,//			width:350,
		editable   : false,			displayField : 'value',				valueField:'key',			    		forceSelection  : true,
		labelWidth : 170,			queryMode    :'local',				editable  :false,						name			:'idaplicaZona',
		store : storeAplZona,		readOnly: (Ext.isEmpty(cdrol) || cdrol != "GERMEDMULTI"),
		listeners : {
			'select' : function(combo, record) {
				if(this.getValue() =='N'){
					Ext.getCmp('idPenalCambioZona').setValue('0');
					validacionCopagoTotal()
				}else{
					obtieneInformacion();
				}
			}
		}
	});

	tratamiento = Ext.create('Ext.form.field.ComboBox',{
		colspan	   :2,			fieldLabel   : 'Tr&aacute;tamiento',	id        : 'tratamiento',		allowBlank     : false,	
		editable   : false,		displayField : 'value',					valueField: 'key',			    forceSelection : true,
		width	   :500,		labelWidth   : 170,						queryMode : 'local',			name           :'dstratam'
		,store : storeTratamiento
	});
	
    cptConAutorizado = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel  : 'CPT',       allowBlank : false,             displayField    : 'value',               id         : 'cptConAutorizado',
        width       :450,          valueField :'key',              forceSelection  : true,                  editable   : false,
        queryMode   :'local',      store : storeTiposCPTUnico,     name            :'cptConAutorizado',
        listeners : {
            'select' : function(combo, record) {
            	Ext.Ajax.request({
                    url     : _URL_OBTIENE_IMPORTEARANCEL
                    ,params : {
                        'params.cdpresta': Ext.getCmp('idmedicoConAutorizado').getValue(),
                        'params.cpt'     : Ext.getCmp('cptConAutorizado').getValue()
                    }
                    ,success : function (response){
                        var datosExtras = Ext.decode(response.responseText).msgResult;
                        Ext.getCmp('precioConAutorizado').setValue(datosExtras);
                    },
                    failure : function (){
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
        
    });
    
    cptQuirBase    = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel  : 'CPT',       allowBlank : false,             displayField    : 'value',               id         : 'cptQuirBase',
        width       :450,          valueField :'key',              forceSelection  : true,                  editable   : false,
        queryMode   :'local',      store : storeTiposCPTUnico,     name            :'cptQuirBase',
        listeners : {
            'select' : function(combo, record) {
            	Ext.Ajax.request({
                    url     : _URL_OBTIENE_IMPORTEARANCEL
                    ,params : {
                        'params.cdpresta': Ext.getCmp('idProveedor').getValue(),
                        'params.cpt'     : Ext.getCmp('cptQuirBase').getValue()
                    }
                    ,success : function (response){
                        var datosExtras = Ext.decode(response.responseText).msgResult;
                        Ext.getCmp('precioQuirurgico').setValue(datosExtras);
                    },
                    failure : function (){
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
        
    });

	tipoMedico = Ext.create('Ext.form.field.ComboBox',{
		fieldLabel : 'Tipo m&eacute;dico',	allowBlank: false,					displayField : 'value',			id:'idTipoMedico',
		labelWidth: 100,					width:450,							valueField   : 'key',		     forceSelection : true,
		matchFieldWidth: false,				queryMode :'local',				    store : storeTiposMedicos,
		editable:false,						triggerAction: 'all',			     name:'idTipoMedico',
		listeners : {
			'select' : function(combo, record) {
				Ext.getCmp('porcentajeEqQuirurg').setValue('');
				Ext.getCmp('importeEqQuirurg').setValue('');
				Ext.Ajax.request({
					url     : _URL_PORCENTAJE_QUIRURGICO
					,params : {
						'params.tipoMedico': this.getValue(),
						'params.feautori'  : Ext.getCmp('fechaAutorizacion').getValue()
					}
					,success : function (response){
						var datosExtras = Ext.decode(response.responseText).msgResult;
						//debug("VALOR DE DATOS EXTRAS -->",datosExtras);
						Ext.getCmp('porcentajeEqQuirurg').setValue(datosExtras);
					},
					failure : function (){
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
	});
	
    aplicaNegociacion = Ext.create('Ext.form.field.ComboBox',{
        colspan    :2,              fieldLabel   : 'Aplica Negociaci&oacute;n', id        : 'idaplicaNegociacion',         allowBlank      : false,//          width:350,
        editable   : false,         displayField : 'value',             valueField:'key',                       forceSelection  : true,
        labelWidth : 170,           queryMode    :'local',              editable  :false,                       name            :'idaplicaNegociacion',
        store : storeAplicaNegoc,//       readOnly: (Ext.isEmpty(cdrol) || cdrol != "GERMEDMULTI"),
        listeners : {
            'select' : function(combo, record) {
                if(this.getValue() =='S'){
                    Ext.getCmp('sumDisponible').setReadOnly(false);
                }else{
                    Ext.getCmp('sumDisponible').setReadOnly(true);
                }
            }
        }
    });


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////				P	A	N	E	L	E	S		 D	E		 L	O	S			 G	R	I	D	'	S			//////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///// PANEL 1 :- 
	var panelConceptosAutorizados = Ext.create('Ext.form.Panel',{
		border  : 0
		,startCollapsed : true
		,bodyStyle:'padding:5px;'
		,items :
		[
			{	xtype       : 'combo',			fieldLabel : 'M&eacute;dico',	allowBlank: false,				displayField : 'nombre',	id:'idmedicoConAutorizado',
				labelWidth: 100,				width:450,						valueField   : 'cdpresta',		forceSelection : true,		minChars  : 2,
				matchFieldWidth: false,			queryMode :'remote',			queryParam: 'params.cdpresta',	store : storeMedico,		triggerAction: 'all',
				name:'idmedicoConAutorizado',	hideTrigger:true
				,listeners : {
					'select' : function(combo, record) {
						if(this.getValue() =='0'){
							Ext.getCmp('medicoExt').show();
						}else{
							Ext.getCmp('medicoExt').hide();
						}
					}
				}
			},{	
				id		: 'medicoExt',		xtype  : 'textfield',			fieldLabel 	: 'Nombre',					labelWidth: 100,
				width	:450,				name   :'medicoExt', 			hidden: true
			},
			cptConAutorizado,
			{	id		: 'precioConAutorizado',		xtype  : 'textfield',			fieldLabel 	: 'Precio',					labelWidth: 100,
				width	:350,							name   :'precioConAutorizado',  allowBlank	: false,					allowDecimals :true,//-->
				decimalSeparator :'.',                  readOnly   : true
			},
			{	id		: 'cantidadConAutorizado',		xtype      	: 'numberfield',	fieldLabel 	: 'Cantidad',		labelWidth: 100,
				name    : 'cantidadConAutorizado',		allowBlank	: false,			width:350,		
				listeners:{
					change:function(field,value){
						try{
							var importeTotal= Ext.getCmp('precioConAutorizado').getValue() * Ext.getCmp('cantidadConAutorizado').getValue();
							Ext.getCmp('importeConAutorizado').setValue(importeTotal);
						}catch(e){}
					}
				}
			},
			{	id		: 'importeConAutorizado',		xtype      	: 'numberfield',			fieldLabel 	: 'Importe',				labelWidth: 100,			width:350,
				name    : 'importeConAutorizado',		allowBlank	: false,					allowDecimals :true,          			decimalSeparator :'.'
			}
		]
	});

	///// PANEL 2 :- 
	var panelEquipoQuirurgicoBase= Ext.create('Ext.form.Panel',{
		border  : 0
		,bodyStyle:'padding:5px;'
		,items :
		[
			cptQuirBase,
			{	id		: 'precioQuirurgico',				xtype      	: 'textfield',			fieldLabel 	: 'Precio',				labelWidth: 100,
				name    : 'precioQuirurgico',				allowBlank	: false,				allowDecimals :true,          		decimalSeparator :'.'
			},
			{	id		: 'porcentajeQuirurgico',			xtype      	: 'numberfield',		fieldLabel 	: 'Porcentaje',			labelWidth: 100,
				name    : 'porcentajeQuirurgico',			allowBlank	: false,				decimalSeparator :'.',
				listeners:{
					change:function(field,value){
						try {
							var importeTotal= Ext.getCmp('precioQuirurgico').getValue() * (Ext.getCmp('porcentajeQuirurgico').getValue()/100);
							Ext.getCmp('importeQuirurgico').setValue(importeTotal);
						}catch(e){}
					}
				}
			},
			{	id		: 'importeQuirurgico',				xtype      	: 'numberfield',		fieldLabel 	: 'Importe',			labelWidth: 100,
				name    : 'importeQuirurgico',				allowBlank	: false,				allowDecimals :true,          		decimalSeparator :'.',
				editable:false,								readOnly    : true
			}
		]
	});

	///// PANEL 3 :-
	var panelEquipoQuirurgico = Ext.create('Ext.form.Panel',{
		border  : 0
		,bodyStyle:'padding:5px;'
		,items :
		[ 
			{	xtype       : 'combo',
				fieldLabel : 'M&eacute;dico',	allowBlank: false,				displayField : 'nombre',			id:'idmedicoEqQuirurg',
				labelWidth: 100,				width:450,						valueField   : 'cdpresta',			forceSelection : true,
				matchFieldWidth: false,			queryMode :'remote',			queryParam: 'params.cdpresta',		store : storeMedico,
				triggerAction: 'all',			name:'idmedicoEqQuirurg',		minChars  : 2,						hideTrigger:true
				,listeners : {
					'select' : function(combo, record) {
						if(this.getValue() =='0'){
							Ext.getCmp('medicoExtEqQ').setValue('EQUIPO QUIR\u00DARGICO'),
							Ext.getCmp('medicoExtEqQ').show();
						}else{
							Ext.getCmp('medicoExtEqQ').setValue(''),
							Ext.getCmp('medicoExtEqQ').hide();
						}
					}
				}
			},
			{	id		: 'medicoExtEqQ',				xtype  : 'textfield',			fieldLabel 	: 'Nombre',			labelWidth: 100,
				width	:450,							name   :'medicoExtEqQ', 		hidden: true
			},
			tipoMedico,
			{	id		: 'porcentajeEqQuirurg',		xtype      	: 'numberfield',	fieldLabel 	: 'Porcentaje',		labelWidth: 100,
				name    : 'porcentajeEqQuirurg',		allowBlank	: false,			decimalSeparator :'.',
				listeners:{
					change:function(field,value){
						if(Ext.getCmp('idTipoMedico').getValue() > 2){
							if(Ext.getCmp('porcentajeEqQuirurg').getValue()>=100){
								centrarVentanaInterna(Ext.Msg.show({
									title: 'Aviso',
									msg: 'El porcentaje es mayor al del Cirujano',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.WARNING
								}));
								Ext.getCmp('porcentajeEqQuirurg').setValue('');
							}
						}
						try{
							var importeTotal= Ext.getCmp('idValorBase').getValue() * (Ext.getCmp('porcentajeEqQuirurg').getValue()/100);
							Ext.getCmp('importeEqQuirurg').setValue(importeTotal);
						}catch(e){}
					}
				}
			},
			{
				id		: 'importeEqQuirurg',			xtype      	: 'numberfield',		fieldLabel 	: 'Importe',		labelWidth: 100,
				name    : 'importeEqQuirurg',			allowBlank	: false,				allowDecimals :true,     		decimalSeparator :'.',
				readOnly   : true
			}
		]
	});

	ventanaConceptosAutorizado = Ext.create('Ext.window.Window', {
		title: 'tr&aacute;tamiento m&eacute;dico',
		closeAction: 'hide',           
		items:[panelConceptosAutorizados],
		buttonAlign : 'center',
		buttons:[
			{  	text: 'Aceptar',
				icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
				handler: function() {
					if (panelConceptosAutorizados.form.isValid()) {
						var datos = panelConceptosAutorizados.form.getValues();
						var nombreProveedor;
						if(datos.idmedicoConAutorizado =='0'){
							nombreProveedor = datos.medicoExt;
						}else{
							nombreProveedor = Ext.getCmp('idmedicoConAutorizado').getRawValue();
						}
						var rec = new modelListadoTablas({
							cdmedico: datos.idmedicoConAutorizado,
							nombreMedico: nombreProveedor,
							cdcpt: datos.cptConAutorizado,
							desccpt:cptConAutorizado.rawValue,
							precio: datos.precioConAutorizado,
							cantporc: datos.cantidadConAutorizado,
							ptimport: datos.importeConAutorizado,
							cdtipaut:'1'
						});
						debug("Entra a la opcion 2");
						if(Ext.getCmp('idCausaSini').getValue() =="N"){
						//if(Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_ACCIDENTE || Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_MATERNIDAD){//2.-
							Ext.getCmp('idCopagoPrevio').setValue("0");
							Ext.getCmp('idCopagoFin').setValue("0");
						}else{
							if(Ext.getCmp('idValSesiones').getValue() =="1"){
								var importe = 0;
								if(+TotalRegistros(storeConceptoAutorizados) > 0){
									importe = +Ext.getCmp('idCopagoPrevio').getValue();
								}
								var copagoPrevio = +importe + (+Ext.getCmp('idCopago').getValue() * datos.cantidadConAutorizado);
								Ext.getCmp('idCopagoPrevio').setValue(copagoPrevio);
								Ext.getCmp('idCopagoFin').setValue(copagoPrevio);
							}
						}
						// Si es maternidad y Salud Vital
						if(Ext.getCmp('idValMaternidad').getValue() =="1" && Ext.getCmp('idReqValidacionMat').getValue() =="1"){
							var sumaDisponible = Ext.getCmp('sumDisponible').getValue();
							Ext.getCmp('sumDisponible').setValue((+sumaDisponible) - (+datos.importeConAutorizado));
						}
						storeConceptoAutorizados.add(rec);
						panelConceptosAutorizados.getForm().reset();
						ventanaConceptosAutorizado.close();
					} else {
						centrarVentanaInterna(Ext.Msg.show({
							title: 'Aviso',
							msg: 'Complete la informaci&oacute;n requerida',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.WARNING
						}));
					}
				}
			},
			{
				text: 'Cancelar',
				icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
				handler: function(){
					panelConceptosAutorizados.getForm().reset();
					ventanaConceptosAutorizado.close();
				}
			}
		]
	});

	ventanaEqQuirurgicoBase = Ext.create('Ext.window.Window', {
		title: 'Equipo quir&uacute;rgico base',
		closeAction: 'hide',
		items:[panelEquipoQuirurgicoBase],
		buttonAlign : 'center',
		buttons:[{
			text: 'Aceptar',
			icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
			handler: function() {
				if (panelEquipoQuirurgicoBase.form.isValid()){
					var datos=panelEquipoQuirurgicoBase.form.getValues();
					var rec = new modelListadoTablas({
						cdcpt: datos.cptQuirBase,
						desccpt:cptQuirBase.rawValue,
						precio: datos.precioQuirurgico,
						cantporc: datos.porcentajeQuirurgico,
						ptimport: datos.importeQuirurgico,
						cdtipaut:'2'
					});
					storeQuirugicoBase.add(rec);
					panelEquipoQuirurgicoBase.getForm().reset();
					// aqui tenemos que recorrer el vector y obtener el VALOR BASE
					obtenerValorBase(storeQuirugicoBase);
					ModificarEquipoQuirurguico(storeQuirurgico);
					ventanaEqQuirurgicoBase.close();
				} else {
					centrarVentanaInterna(Ext.Msg.show({
						title: 'Aviso',
						msg: 'Complete la informaci&oacute;n requerida',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					}));
				}
			}
		},
		{
			text: 'Cancelar',
			icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
			handler: function() {
				panelEquipoQuirurgicoBase.getForm().reset();
				ventanaEqQuirurgicoBase.close();
			}
		}]
	});

	//DATOS PARA EL TERCER GRID --> EQUIPO QUIRURGICO
	ventanaEqQuirurgico = Ext.create('Ext.window.Window', {
		title: 'Equipo quir&uacute;rgico',
		closeAction: 'hide',
		items:[panelEquipoQuirurgico],
		buttonAlign : 'center',
		buttons:[{
			text: 'Aceptar',
			icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
			handler: function() {
				if (panelEquipoQuirurgico.form.isValid()){
					var datos=panelEquipoQuirurgico.form.getValues();
					var nombreProveedor;
					if(datos.idmedicoEqQuirurg =='0'){
						nombreProveedor = datos.medicoExtEqQ;
					}else{
						nombreProveedor = Ext.getCmp('idmedicoEqQuirurg').getRawValue();
					}
					var rec = new modelListadoTablas({
						cdmedico: datos.idmedicoEqQuirurg,
						nombreMedico:nombreProveedor,
						cantporc: datos.porcentajeEqQuirurg,
						ptimport: datos.importeEqQuirurg,
						cdtipmed:datos.idTipoMedico,
						descTipMed:tipoMedico.rawValue,
						cdtipaut:'3'
					});
					if(Ext.getCmp('idValMaternidad').getValue() =="1" && Ext.getCmp('idReqValidacionMat').getValue() =="1" ){
						var sumaDisponible = Ext.getCmp('sumDisponible').getValue();
						sumaDisponible = Ext.getCmp('sumDisponible').getValue() - datos.importeEqQuirurg;
						Ext.getCmp('sumDisponible').setValue(sumaDisponible);
					}
					
					storeQuirurgico.add(rec);
					panelEquipoQuirurgico.getForm().reset();
					obtenerCoaseguroHonorarios(storeQuirurgico);
					ventanaEqQuirurgico.close();
				} else {
					centrarVentanaInterna(Ext.Msg.show({
						title: 'Aviso',
						msg: 'Complete la informaci&oacute;n requerida',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					}));
				}
			}
		},
		{
			text: 'Cancelar',
			icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
			handler: function() {
				panelEquipoQuirurgico.getForm().reset();
				ventanaEqQuirurgico.close();
			}
		}]
	});

	/***********************************************************************
	*  grid de visualizacion de la informacion de la tabulacion del       *
	*  tratamiento medico							     				   *
	***********************************************************************/
	Ext.define('EditorIncisos', {
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
		title: 'Tabulaci&oacute;n tratamiento m&eacute;dico',
		frame: false,
		initComponent: function(){
			this.cellEditing = new Ext.grid.plugin.CellEditing({
				clicksToEdit: 1
			});
			Ext.apply(this, {
				height: 250,
				plugins: [this.cellEditing],
				store: storeConceptoAutorizados,
				columns: [
					{	header: 'M&eacute;dico',	dataIndex: 'nombreMedico',		width:250    },
					{  	header: 'CPT',				dataIndex: 'desccpt',			width:250    },
					{  	header: 'Precio',			dataIndex: 'precio',			width:100,				renderer: Ext.util.Format.usMoney  },
					{  	header: 'Cantidad', 		dataIndex: 'cantporc',		 	width:100  	 },
					{  	header: 'Importe', 			dataIndex: 'ptimport',		 	width:100,				renderer: Ext.util.Format.usMoney  },
					{  	xtype: 'actioncolumn',		width: 30,					 	sortable: false,	 	menuDisabled: true,
						items: [{
							icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
							tooltip: 'Quitar tr&aacute;tamiento m&eacute;dico',
							scope: this,
							handler: this.onRemoveClick
						}]
					}
				],
				selModel: {
					selType: 'cellmodel'
				},
				tbar: [{
					icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
					text: 'Agregar tratamiento m&eacute;dico',
					scope: this,
					handler: this.onAddClick
				}]
			});
			this.callParent();
		},
		onAddClick: function(btn, e){
			Ext.getCmp('medicoExt').hide();
			ventanaConceptosAutorizado.animateTarget=btn;
			ventanaConceptosAutorizado.showAt(150,1100);
		},
		onRemoveClick: function(grid, rowIndex){
			var record=this.getStore().getAt(rowIndex);
			var cantidad = record.get("cantporc");
			var importeEliminar = record.get("ptimport");
			if(Ext.getCmp('idValMaternidad').getValue() =="1" && Ext.getCmp('idReqValidacionMat').getValue() =="1"){
				var sumaDisponible = Ext.getCmp('sumDisponible').getValue();
				Ext.getCmp('sumDisponible').setValue((+sumaDisponible) + (+importeEliminar));
			}
			debug("Entra a 3");
			if(Ext.getCmp('idCausaSini').getValue() =="N"){
			//if(Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_ACCIDENTE || Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_MATERNIDAD){//3.-
				Ext.getCmp('idCopagoPrevio').setValue("0");
				Ext.getCmp('idCopagoFin').setValue("0");
			}else{
				if(Ext.getCmp('idValSesiones').getValue() =="1"){
					//Se realiza la validacion si ya no hay ninguno entonces se coloca el valor original de la poliza
					if(+TotalRegistros(storeConceptoAutorizados) > 0){
						var copagoPrevio = +Ext.getCmp('idCopagoPrevio').getValue() - (+Ext.getCmp('idCopago').getValue() * cantidad);
						Ext.getCmp('idCopagoPrevio').setValue(copagoPrevio);
						Ext.getCmp('idCopagoFin').setValue(copagoPrevio);
					}else{
						var copagoPrevio = +Ext.getCmp('idCopago').getValue();
						Ext.getCmp('idCopagoPrevio').setValue(copagoPrevio);
						Ext.getCmp('idCopagoFin').setValue(copagoPrevio);
					}
				}
			}
			this.getStore().removeAt(rowIndex);
		}
	});
	gridIncisos=new EditorIncisos();

	/***********************************************************************
	*  grid de visualizacion de la informacion de la tabulacion del       *
	*  base equipo quirurgico  					     				   *
	***********************************************************************/
	Ext.define('EditorIncisos2', {
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
		title: 'Tabulaci&oacute;n Base Equipo quir&uacute;rgico',
		frame: false,
		initComponent: function(){
			this.cellEditing = new Ext.grid.plugin.CellEditing({
				clicksToEdit: 1
			});
			Ext.apply(this, {
				height: 250,
				plugins: [this.cellEditing],
				store: storeQuirugicoBase,
				columns: [
					{	header: 'CPT',			dataIndex: 'desccpt',			width:350   	},
					{	header: 'Precio',		dataIndex: 'precio',			width:100,				renderer: Ext.util.Format.usMoney   	},
					{	header: 'Porcentaje', 	dataIndex: 'cantporc',			width:100    	},
					{	header: 'Importe', 		dataIndex: 'ptimport',		 	width:100,				renderer: Ext.util.Format.usMoney    	},
					{	xtype: 'actioncolumn',	width: 30,						sortable: false,		menuDisabled: true,
						items: [{
							icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
							tooltip: 'Quitar base equipo quir&uacute;gico',
							scope: this,
							handler: this.onRemoveClick
						}]
					}
				],
				selModel: {
					selType: 'cellmodel'
				},
				tbar: [{
					icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
					text: 'Agregar base equipo quir&uacute;gico',
					scope: this,
					handler: this.onAddClick
				}]
			});
			this.callParent();
		},
		onAddClick: function(btn, e){
			ventanaEqQuirurgicoBase.animateTarget=btn;
			ventanaEqQuirurgicoBase.showAt(150,1300);
		},
		onRemoveClick: function(grid, rowIndex){
			var record=this.getStore().getAt(rowIndex);
			this.getStore().removeAt(rowIndex);
			obtenerValorBase(storeQuirugicoBase);
			ModificarEquipoQuirurguico(storeQuirurgico);
		}
	});
	gridIncisos2=new EditorIncisos2();

	/***********************************************************************
	*  grid de visualizacion de la informacion de la tabulacion           *
	*  equipo quirurgico		  					     				   *
	***********************************************************************/
	Ext.define('EditorIncisos3', {
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
		title: 'Tabulaci&oacute;n equipo quir&uacute;rgico',
		frame: false,
		initComponent: function(){
			this.cellEditing = new Ext.grid.plugin.CellEditing({
				clicksToEdit: 1
			});
			Ext.apply(this, {
				height: 250,
				plugins: [this.cellEditing],
				store: storeQuirurgico,
				columns: [
					{	header: 'M&eacute;dico',		dataIndex: 'nombreMedico',	 	width:250   	},
					{	header: 'Porcentaje',			dataIndex: 'cantporc',			width:100	   	},
					{	header: 'Tipo medico',			dataIndex: 'descTipMed',	 	width:150  		},
					{	header: 'Importe',				dataIndex: 'ptimport',	 		width:100,			renderer: Ext.util.Format.usMoney  	},
					{	xtype: 'actioncolumn',				width: 30,						sortable: false,		menuDisabled: true,
						items: [{
							icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
							tooltip: 'Quitar equipo quir&uacute;gico',
							scope: this,
							handler: this.onRemoveClick
						}]
					}
				],
				selModel: {
					selType: 'cellmodel'
				},
				tbar: [{
					icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
					text: 'Agregar equipo quir&uacute;gico',
					scope: this,
					handler: this.onAddClick
				},{
                    icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
                    text: 'Vista Previa Proveedor',
                    scope: this,
                    id:'btnVoBoProveedor',
                    disabled  : true,
                    handler: function() {
                        if (panelInicialPrincipal.form.isValid()) {
                            var urlRequestImpProveedores = _0_urlRutaReporte
                                            + '?p_unieco='      + Ext.getCmp('idUnieco').getValue()
                                            + '&p_ramo='        + Ext.getCmp('idcdRamo').getValue()
                                            + '&p_estado='      + Ext.getCmp('idEstado').getValue()
                                            + '&p_poliza='      + Ext.getCmp('polizaAfectada').getValue()
                                            + '&P_AUTSER='      + Ext.getCmp('idNoAutorizacion').getValue()
                                            + '&P_CDPERSON='    + Ext.getCmp('idAsegurado').getValue()
                                            + '&destype=cache'
                                            + "&desformat=PDF"
                                            + "&userid="        + _0_reportsServerUser
                                            + "&ACCESSIBLE=YES"
                                            + "&report="        + _0_reporteVistaPreviaProv
                                            + "&paramform=no";
                                    debug(urlRequestImpProveedores);
                                    var numRand = Math.floor((Math.random() * 100000) + 1);
                                    debug(numRand);
                                    var windowVerDocu = Ext.create('Ext.window.Window',
                                    {
                                        title          : 'Impresi&oacute;n Proveedores'
                                        ,width         : 700
                                        ,height        : 500
                                        ,collapsible   : true
                                        ,titleCollapse : true
                                        ,html : '<iframe innerframe="'
                                                + numRand
                                                + '" frameborder="0" width="100" height="100"'
                                                + 'src="'
                                                + panDocUrlViewDoc
                                                + "?contentType=application/pdf&url="
                                                + encodeURIComponent(urlRequestImpProveedores)
                                                + "\">"
                                                + '</iframe>'
                                        ,listeners :
                                        {
                                            resize : function(win,width,height,opt)
                                            {
                                                debug(width,height);
                                                $('[innerframe="'+ numRand+ '"]').attr(
                                                {
                                                    'width'   : width - 20
                                                    ,'height' : height - 60
                                                });
                                            }
                                        }
                                    }).show();
                                    windowVerDocu.center();
                        } else {
                            centrarVentanaInterna(Ext.Msg.show({
                                title: 'Aviso',
                                msg: 'Complete la informaci&oacute;n requerida',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            }));
                        }
                    }
                }]
			});
			this.callParent();
		},
		onAddClick: function(btn, e){
			Ext.getCmp('medicoExtEqQ').hide();
			ventanaEqQuirurgico.animateTarget=btn;
			ventanaEqQuirurgico.showAt(150,1550);
		},
		onRemoveClick: function(grid, rowIndex){
			var record=this.getStore().getAt(rowIndex);
			var importeEliminar = record.get("ptimport");
			if(Ext.getCmp('idValMaternidad').getValue() =="1" && Ext.getCmp('idReqValidacionMat').getValue() =="1" ){
				var sumaDisponible = Ext.getCmp('sumDisponible').getValue();
				Ext.getCmp('sumDisponible').setValue((+sumaDisponible) + (+importeEliminar));
			}
			this.getStore().removeAt(rowIndex);
			obtenerCoaseguroHonorarios(storeQuirurgico);
		}
	});
	gridIncisos3=new EditorIncisos3();

	/*################################################################################################################################################
	###################################################### 			 PANTALLA 	PRINCIPAL 		 #####################################################
	##################################################################################################################################################*/
	var panelInicialPrincipal= Ext.create('Ext.form.Panel',{
		border    : 0
		,title: 'Autorizaci&oacuten de Servicios'
		,renderTo : 'div_clau'
		,id : 'panelInicialPrincipal'
		,bodyPadding: 5
		,width: 850
		,url: _URL_GUARDA_AUTORIZACION
		,layout     :{
			type     : 'table'
			,columns : 2
		}
		,defaults 	: {
			style : 'margin:5px;'
		},
		items    	: [
			//se agrupa el No. de autorizaci�n , el No. de autorizaci�n anterior y el bot�n de buscar
			{	 xtype       : 'textfield',			fieldLabel : 'ValSesiones'		,		id       : 'idValSesiones', 	name		:'ValSesiones',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'Hospital Plus',	 		id       : 'idHospitalPlus', 	name		:'idHospitalPlus',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'porcentajeIncremento',	id       : 'idPorcIncremento', 	name		:'idPorcIncremento',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'Multiplo Incremento',		id       : 'idMultiploIncrem', 	name		:'idMultiploIncrem',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'mtoBase'				,	id       : 'idMontoBase',
				labelWidth: 170,					hidden:true
			},
            {    xtype       : 'textfield',         fieldLabel : 'cptGeneral'           ,   id       : 'idcptGeneral',
                labelWidth: 170,                  hidden:true
            },
			{	 xtype       : 'textfield',			fieldLabel : 'claveTipoAutoriza'	,	id       : 'claveTipoAutoriza',	name       : 'claveTipoAutoriza'
				,allowBlank : false,				labelWidth: 170,	value:2,	hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'cveTipoAutorizaG'		,	id       : 'cveTipoAutorizaG',	name       : 'cveTipoAutorizaG'
				,allowBlank : false,				labelWidth: 170,	hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'Unieco'				,	id       : 'idUnieco',			name       : 'cdunieco'
				,allowBlank : false,				labelWidth: 170,	hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'estado'				,	id       : 'idEstado',			name       : 'estado'			
				,allowBlank : false,				labelWidth: 170,	hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'Ramo'					,	id       : 'idcdRamo',			name       : 'cdramo'
				,allowBlank : false,				labelWidth: 170,	hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'nmsituac'				,	id       : 'idNmSituac',		name       : 'nmsituac'
				,allowBlank : false,				labelWidth: 170,	hidden:true
			},
			{	id: 'idAaapertu'					,xtype		: 'datefield'					,fieldLabel	: 'aaapertu',
				labelWidth : 170					,name		: 'aaapertu'					,format		: 'Y',
				editable: true						,value		: new Date(),					hidden:true
			},
			{	xtype       : 'textfield',			fieldLabel : 'status id:(idstatus)'	,id       : 'idstatus',				name       : 'status'
				,allowBlank : false,				labelWidth: 170	,					value:'1',		hidden:true
			},
			{	id: 'idFesistem'					,xtype		: 'datefield'			,fieldLabel	: 'fesistem',
				labelWidth : 170					,name		: 'fesistem'			,format		: 'd/m/Y',
				editable: true						,value		: new Date()			,hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'nmsuplem'				,id       : 'idNmsuplem',			name       : 'nmsuplem',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'cdtipsit'				,id       : 'idcdtipsit',			name       : 'cdtipsit',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'zonaContratadaPoliza'	,id       : 'idZonaContratadaPoliza',name       : 'zonaContratadaPoliza',
				labelWidth: 170,					hidden:true
			},
            {    xtype       : 'textfield',         fieldLabel : 'dsplanPoliza'     ,id       : 'iddsplanAsegurado',
                labelWidth: 170,                  hidden:true
            },
            {     xtype       : 'textfield',         fieldLabel : 'dsCirculohosPoliza'     ,id       : 'idcirculohosPoliza',
                labelWidth: 170,                  hidden:true
            },
			{	 xtype       : 'textfield',			fieldLabel : 'circuloHospProve'		,id       : 'idCirculoHospProv',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'codigoPostalProve'		,id       : 'codPostalProv',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'zonaHospProv'		,id       : 'idzonaHospProv',
				labelWidth: 170,					hidden:true
			},
            {    xtype       : 'textfield',         fieldLabel : 'zonaTafiProv'     ,id       : 'idzonaTafiProv',
                labelWidth: 170,                  hidden:true
            },
			{	 xtype       : 'textfield',			fieldLabel : 'Exclusion'		,id       : 'idExclusionPenalizacion',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'dsNombreAsegurado'		,id       : 'dsNombreAsegurado', 	name:'dsNombreAsegurado',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'NumTramite'		,		id       : 'idNumtramiteInicial', 	name:'idNumtramiteInicial',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'MesesAsegurado'		,	id       : 'idMesesAsegurado', 		name:'idMesesAsegurado',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'TipoCopago'		,		id       : 'idTipoCopago', 			name:'idTipoCopago',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'MesesTmpEsperaICD'		,id       : 'idTiempoEsperaICD', 	name:'idTiempoEsperaICD',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'MesesTmpEsperaCPT'		,id       : 'idTiempoEsperaCPT', 	name:'idTiempoEsperaCPT',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'ReqPenalizacion'		,	id       : 'idReqPenalizacion', 	name:'idReqPenalizacion',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'ValMaternidad'		,	id       : 'idValMaternidad', 		name:'idValMaternidad',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'ReqValidacionMat'		,	id       : 'idReqValidacionMat', 	name:'idReqValidacionMat',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'SalarioMinimo'		,	id       : 'idSalarioMin', 			name:'idSalarioMin',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'idEstatusTramite'		,	id       : 'idEstatusTramite', 		name:'idEstatusTramite',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'idCausaSini'			,	id       : 'idCausaSini', 			name:'idCausaSini',
				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'datefield',			fieldLabel : 'Edad'					,	id       : 'edad', 					name:'edad',
				format		 : 'd/m/Y',				labelWidth: 170,					hidden:true
			},
			{	 xtype       : 'textfield',			fieldLabel : 'Genero'				,	id       : 'genero', 				name:'genero',
				labelWidth: 170,					hidden:true
			},
			{	colspan:2
				,border: false
				,layout      :	{
					type     : 'table'
					,columns : 3
				},
				items    : [
					{	xtype       : 'textfield',			fieldLabel : 'No. de autorizaci&oacute;n',				id  : 'idNoAutorizacion',
						name        : 'nmautser',			labelWidth	: 170,					readOnly   : true
					},
					{	xtype       : 'textfield',			fieldLabel : 'No. de autorizaci&oacute;n anterior',		id	: 'idNumeroAnterior',	
						name       : 'nmautant',			labelWidth	: 170,		readOnly   : true
					},
					Ext.create('Ext.Button', {
						text: 'Buscar',
						id:'btnBuscar',
						icon : _CONTEXT + '/resources/fam3icons/icons/folder.png',
						handler: function() {
							Ext.create('Ext.window.Window',{
								title         : 'Autorizaci&oacute;n de servicio'
								,buttonAlign  : 'center'
								,width        : 750
								,height       : 400
								,autoScroll   : true
								,loader       : {
									url       : _VER_AUTORIZACION_SERVICIO
									,scripts  : true
									,autoLoad : true
									,params   : {
										'params.nmautser':Ext.getCmp('idNumeroAnterior').getValue()
									}
								}
							}).showAt(150,150);
						}
					})
				]
			}
			, cmbRamos		//1.- Producto
			,{	colspan:2								,border: false
				,layout      : {
					type     : 'table'
					,columns : 2
				},
				items    :[
					asegurado,
					{	xtype   : 'button',
						text    : 'Siniestralidad',
						id      : 'btnSiniestralidad',
                        disabled: true,
						icon    : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
						handler : function() {
							var windowHistSinies = Ext.create('Ext.window.Window',{
								modal       : true,
								buttonAlign : 'center',
								width       : 800,
								height      : 500,
								autoScroll  : true,
								loader      : {
									url     : _URL_LOADER_HISTORIAL_RECLAMACIONES,
									params  : {
										'params.cdperson'  : panelInicialPrincipal.down('[name=cdperson]').getValue(),
										'params.cdramo'    : null,
										'params.nmpoliza'  : null,
										'params.cdunieco'  : null,
										'params.proceso'  : '0'
										
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
					}
				]
			}
			,{	xtype       : 'textfield',			fieldLabel : 'Modalidad',			id  : 'idModalidad',	width	: 500,
				name        : 'idModalidad',		labelWidth	: 170,					readOnly   : true,		colspan:2
			},
			tipoEventoGNP
			,//3.- Fecha de Solicitud
			{	id: 'fechaSolicitud'		,xtype		: 'datefield'				,fieldLabel	: 'Fecha Solicitud',
				name:'fesolici'				,labelWidth : 170						,id 		: 'fechaSolicitud',			format		: 'd/m/Y',
				editable: true				,value		: new Date()				,readOnly   : true
			},//4.- Fecha de Autorizacion
			{	id: 'fechaAutorizacion'		,xtype		: 'datefield'				,fieldLabel	: 'Fecha Autorizaci&oacute;n',
				labelWidth : 170			,format		: 'd/m/Y',
				editable: true				,name		: 'feautori',
				listeners:{
					change:function(field,value){
						Ext.Ajax.request({
							url     : _URL_NUMERO_DIAS
							,params:{
								'params.cdramo' : Ext.getCmp('idcdRamo').getValue(),
								'params.cdtipsit' : Ext.getCmp('idcdtipsit').getValue()
							}
							,success : function (response){
								Ext.getCmp('fechaVencimiento').setValue(Ext.Date.add(value, Ext.Date.DAY, Ext.decode(response.responseText).diasMaximos));
							},
							failure : function (){
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
			},//5.- Fecha de Vencimiento
			{	id: 'fechaVencimiento'					,xtype		: 'datefield'								,fieldLabel	: 'Fecha de vencimiento',
				labelWidth : 170						,format		: 'd/m/Y',									name:'fevencim',
				editable: false							,readOnly   : true
			},//6.- Fecha de Ingreso
			{	id: 'fechaIngreso'						,xtype		: 'datefield'								,fieldLabel	: 'Fecha de Ingreso',
				labelWidth : 170						,format		: 'd/m/Y',									name:'feingres',
				editable: true							,allowBlank : false
			},
			{	colspan:2								,border: false
				,layout      : {
					type     : 'table'
					,columns : 6
				},
				items    :[
					//7.- Poliza Afectada NMPOLIEX
					{	 xtype       : 'textfield',			fieldLabel : 'P&oacute;liza afectada'				,id       : 'polizaAfectadaCom'
						,allowBlank : false,				labelWidth: 170,				name:'nmpolizaCom',	readOnly   : true, Width: 1100
					},
					//7.- Poliza Afectada corto
					{	 xtype       : 'textfield',			fieldLabel : 'P&oacute;liza afectada'				,id       : 'polizaAfectada'
						,allowBlank : false,				labelWidth: 170,				name:'nmpoliza',	readOnly   : true,  hidden:true
					},
					Ext.create('Ext.Button', {
						text    : 'Ver coberturas',
						id      : 'btnVerCoberturas',
                        disabled: true,
						icon : _CONTEXT + '/resources/fam3icons/icons/application_view_list.png',
						handler: function() {
							centrarVentanaInterna(Ext.create('Ext.window.Window',{
								title        : 'COBERTURA'
								,buttonAlign : 'center'
								,width       : 620
								,height      : 400
								,autoScroll  : true
								,loader      :	{
									url       : _VER_COBERTURAS
									,scripts  : true
									,autoLoad : true
									,params:{
										'params.cdunieco':Ext.getCmp('idUnieco').getValue(),
										'params.estado':Ext.getCmp('idEstado').getValue(),
										'params.cdramo':Ext.getCmp('idcdRamo').getValue(),
										'params.nmpoliza':Ext.getCmp('polizaAfectada').getValue(),
										'params.suplemento':Ext.getCmp('idNmsuplem').getValue(),
										'params.nmsituac': Ext.getCmp('idNmSituac').getValue()
									}
								}
							}).show());
						}
					}),
					{	xtype  : 'button',
						text   : 'Detalle p&oacute;liza',
						id      : 'btnDetallePoliza',
                        disabled: true,
						icon   : _CONTEXT + '/resources/fam3icons/icons/application_add.png',
						handler: function() {
							var windowDetPol = Ext.create('Ext.window.Window', {
							title       : 'Detalle de la p&oacute;liza',
							modal       : true,
							buttonAlign : 'center',
							width       : 830,
							height      : 500,
							autoScroll  : true,
							items: [
								Ext.create('Ext.tab.Panel',
								{
									width: 830,
									items: [
										{
											title : 'DATOS DE LA POLIZA',
											loader: {
												url: _URL_LOADER_DATOS_POLIZA,
												scripts: true,
												loadMask : true,
												autoLoad: false,
												ajaxOptions: {
													method: 'POST'
												}
											},
											listeners: {
												activate: function(tab) {
													tab.loader.load({
														params : {
															'params.cdunieco': Ext.getCmp('idUnieco').getValue(),
															'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
															'params.estado'  : Ext.getCmp('idEstado').getValue(),
															'params.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
															'params.nmsituac': Ext.getCmp('idNmSituac').getValue()
														}
													});
												}
											}
										},
										{	title: 'ASEGURADOS',
											loader: {
												url: _URL_LOADER_ASEGURADOS_POLIZA,
												scripts: true,
												loadMask : true,
												autoLoad: false,
												ajaxOptions: {
													method: 'POST'
												}
											},
											listeners: {
												activate: function(tab) {
													tab.loader.load({
														params : {
															'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
															'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
															'params.estado'    : Ext.getCmp('idEstado').getValue(),
															'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
															'params.suplemento': Ext.getCmp('idNmsuplem').getValue()
														}
													});
												}
											}
										},
										{
                                           title : 'DOCUMENTACION',
                                           width: '350',
                                           loader : {
                                               url : _URL_CONSULTA_DOCUMENTOS,
                                               scripts : true,
                                               autoLoad : false
                                           },
                                           listeners : {
                                               activate : function(tab) {
                                                   tab.loader.load({
                                                       params : {
                                                           'smap1.readOnly': true,
                                                           'smap1.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
                                                           'smap1.cdunieco': Ext.getCmp('idUnieco').getValue(),
                                                           'smap1.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
                                                           'smap1.estado'  : Ext.getCmp('idEstado').getValue(),
                                                           'smap1.nmsuplem': Ext.getCmp('idNmsuplem').getValue(),
                                                           'smap1.ntramite': null,
                                                           'smap1.tipomov' : '0'
                                                       }
                                                   });
                                               }
                                           }
                                       },
										{	title: 'RECIBOS',
											loader: {
												url: _URL_LOADER_RECIBOS,
												scripts: true,
												loadMask : true,
												autoLoad: false,
												ajaxOptions: {
													method: 'POST'
												}
											},
											listeners: {
												activate: function(tab) {
													tab.loader.load({
														params : {
															'params.cdunieco': Ext.getCmp('idUnieco').getValue(),
															'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
															'params.estado'  : Ext.getCmp('idEstado').getValue(),
															'params.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
															'params.nmsuplem': Ext.getCmp('idNmsuplem').getValue()
														}
													});
												}
											}
										}
									]
								})]
							}).show();
							centrarVentanaInterna(windowDetPol);
						}
					},
					{	xtype   : 'button',
						text    : 'Exclusi&oacute;n p&oacute;liza',
						id      : 'btnExclusionPoliza',
						disabled: true,
						icon    : _CONTEXT + '/resources/fam3icons/icons/application_delete.png',
						handler : function() {
							centrarVentanaInterna(Ext.create('Ext.window.Window', {
								title       : 'Exclusiones de la p&oacute;liza',
								modal       : true,
								buttonAlign : 'center',
								autoScroll  : true,
								width       : 450,
								height      : 455,
								loader      : {
									url     : _URL_LOADER_VER_EXCLUSIONES,
									scripts : true,
									autoLoad: true,
									params  : {
										'params.cdunieco': Ext.getCmp('idUnieco').getValue(),
										'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
										'params.estado'  : Ext.getCmp('idEstado').getValue(),
										'params.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
										'params.nmsituac': Ext.getCmp('idNmSituac').getValue()
									}
								}
							}).show());
						}
					},
                    {   xtype   : 'button',
                        text    : 'Exclusi&oacute;n ICD',
                        id      : 'btnExclusionICD',
                        disabled: true,
                        icon    : _CONTEXT + '/resources/fam3icons/icons/application_form_delete.png',
                        handler : function() {
                            centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                title       : 'Exclusiones ICD',
                                modal       : true,
                                buttonAlign : 'center',
                                autoScroll  : true,
                                width       : 800,
                                height      : 500,
                                loader      : {
                                    url     : _URL_LOADER_EXLUSION_ICD_ASEGURADOS,
                                    scripts : true,
                                    autoLoad: true,
                                    params  : {
                                        'params.cdunieco': Ext.getCmp('idUnieco').getValue(),
                                        'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
                                        'params.estado'  : Ext.getCmp('idEstado').getValue(),
                                        'params.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
                                        'params.nmsuplem': Ext.getCmp('idNmsuplem').getValue(),
                                        'params.nmsituac': Ext.getCmp('idNmSituac').getValue()
                                    }
                                }
                            }).show());
                        }
                    }
				]
			},
			sucursal,				//8.- Plazas
			coberturaAfectada,		//9.- Cobertura Afectada
			subCobertura,			//10.- SubCobertura
			tipoEvento,
			proveedor,				//11.- Proveedor
			medico,					//12.- Medico
			//12.- Medico si no se encuentra en red
			{	colspan:2, 			id    : 'medicoPExt',		xtype  : 'textfield',			fieldLabel 	: 'Nombre M&eacute;dico',
				labelWidth: 170,	width :350,					width  :500,					name   :'medicoPExt', 					hidden: false,
				listeners:{
					afterrender: function(){
						this.hide();
					}
				}
			},
			//13.- Especialidad en red
			{	colspan:2, 			id	: 'especialidadPExt',	xtype  : 'textfield',			fieldLabel 	: 'Especialidad',		labelWidth: 170,
				width	:350,		width:500,					name   :'especialidadPExt', 	hidden: false,
				listeners:{
					afterrender: function(){
						this.hide();
					}
				}
			},
			//13.- Especialidad sin red
			{	colspan:2,xtype   : 'textfield'			,fieldLabel : 'Especialidad'		,id       : 'idEspecialidad',		name:'idEspecialidad'
				,labelWidth: 170						,readOnly   : true
			},
			//14.- Deducible
			{	colspan:2,  xtype : 'textfield'			,fieldLabel : 'Deducible'			,id       : 'idDeducible'
				,labelWidth: 170						,readOnly   : true
			},
			//15.- Copago
			{	colspan:2, xtype  : 'textfield'			,fieldLabel : 'Copago original'		,id       : 'idCopago'
				,labelWidth: 170						,readOnly   : true,  width: 670
			},
			//16.- Copago Final Previo
			{	colspan:2, xtype  : 'textfield'			,fieldLabel : 'Copago final 1'		,id       : 'idCopagoPrevio'
				,labelWidth: 170						,readOnly   : true,					name:'copagoPrevio',  		width: 670
			},
			//16.- Copago Final Total
			{	colspan:2, xtype  : 'textfield'			,fieldLabel : 'Copago final 2'		,id       : 'idCopagoFin'
				,labelWidth: 170						,readOnly   : true,					name:'copagoTotal',  		width: 670
			},
			aplicaCirHosp,
			//17.- P. Circulo Hospitalario
			{	colspan:2, xtype  : 'textfield'			,fieldLabel : 'Penalizaci&oacuten circulo hospitalario'			,id       : 'idPenalCircHospitalario'
				,labelWidth: 170						,readOnly   : true, name: 'idPenalCircHospitalario'
			},
			aplicaZona,
			//18.- P. Cambio de zona
			{	colspan:2, xtype  : 'textfield'			,fieldLabel : 'Penalizaci&oacuten por cambio de zona'			,id       : 'idPenalCambioZona'
				,labelWidth: 170						,readOnly   : true,			name       : 'idPenalCambioZona'
			},
			comboICD,					//19.- ICD
			{    xtype       : 'textfield'               ,fieldLabel : 'Tipo'                      ,id             : 'idTipo'
                ,labelWidth: 170    ,align:'center',    readOnly   : true,                      renderer: Ext.util.Format.usMoney   
            },
            {   xtype       : 'textfield'               ,fieldLabel : 'No. Reclamo'                      ,id       : 'idNumSubsecuente' ,name       : 'idNumSubsecuente'
                ,labelWidth: 170,align:'center',    readOnly   : true,                      renderer: Ext.util.Format.usMoney   
            },
			causaSiniestro,				//20.- Causa Siniestro
			aplicaNegociacion,
			//21.- Suma Disponible
			{	colspan:2,			xtype : 'numberfield',              id:'sumDisponible',           fieldLabel: 'Suma disponible proveedor',    readOnly   : true,
				labelWidth: 170,    allowBlank: false,	                allowDecimals :true,          decimalSeparator :'.',                 allowBlank:false,
				name:'mtsumadp',	minValue: 0
			},
			tratamiento,				//22.- Tratamiento
			//23.- Observaciones
			{	colspan:2					,xtype : 'textareafield'				,fieldLabel : 'Observaciones'		,id       : 'observaciones'
				,labelWidth	 : 170			,name  :'dsobserv'						,width      : 700					,height   : 70
			},
			//24.- Notas Internas
			{	colspan:2					,xtype       : 'textareafield'			,fieldLabel : 'Notas internas'		,id       : 'notaInterna'
				,labelWidth: 170			,name:'dsnotas'							,width      : 700					,height: 100
			},
			{	colspan:2,
				items    : [
					gridIncisos
				]
			},
			{	colspan:2,
				items    : [
					gridIncisos2
				]
			},
			{	colspan:2, 			xtype       : 'textfield'				,fieldLabel : 'Valor base'						,id       : 'idValorBase'
				,align:'center',	readOnly   : true,						renderer: Ext.util.Format.usMoney 	
			},
			{	colspan:2,
				items    : [
					gridIncisos3
				]
			},
            {   xtype       : 'textfield'               ,fieldLabel : 'Coaseguro (Honorarios)'                      ,id       : 'idCoaseguroHon'
                ,align:'center',    readOnly   : true,                      renderer: Ext.util.Format.usMoney   
            },
            {   xtype       : 'textfield'               ,fieldLabel : 'Coaseguro (Gasto Hospitalario)'                      ,id       : 'idCoaseguroHos'
                ,align:'center',    readOnly   : true,                      renderer: Ext.util.Format.usMoney   
            }
		],
		buttonAlign:'center',
		buttons: [{
			text:'Guardar',
			icon:_CONTEXT+'/resources/fam3icons/icons/disk.png',
			id:'botonGuardar',
			handler: function() {
				if (panelInicialPrincipal.form.isValid()) {
					guardadoAutorizacionServicio(_Existe,'1');
				} else {
					centrarVentanaInterna(Ext.Msg.show({
						title: 'Aviso',
						msg: 'Complete la informaci&oacute;n requerida',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					}));
				}
			}
		},
		{	text:'Autorizar',
		    xtype   : 'button',
			icon:_CONTEXT+'/resources/fam3icons/icons/key.png',
			id:'Autorizar',
			disabled: false,
			handler:function() {
				if (panelInicialPrincipal.form.isValid()) {
					//validamos que exista valor del registro
					if(valorAction == null ){
						//Solo se guarda y no realiza la validacion del monto
						guardadoAutorizacionServicio(_NExiste,'0');//---2
						
					}else{
						Ext.Ajax.request( {
							url     : _URL_Existe_Documentos
							,params:{
								'params.ntramite': valorAction.ntramite
							}
							,success : function (response){
								if(Ext.decode(response.responseText).existeDocAutServicio =="N"){
									guardadoAutorizacionServicio(_NExiste,'0');//---3
								}else{
									Ext.Ajax.request( {
										url     : _URL_MONTO_MAXIMO
										,params:{
											'params.cdramo' : Ext.getCmp('idcdRamo').getValue(),
											'params.cdtipsit' : Ext.getCmp('idcdtipsit').getValue()
										}
										,success : function (response){
											//debug("VALORA DE LA SUMA DISPONIBLE -->> ",Ext.getCmp('sumDisponible').getValue(), " Monto Maximo --->> ",Ext.decode(response.responseText).montoMaximo);
											if(cdrol=="COORDMEDMULTI"){
												if(+ (Ext.getCmp('sumDisponible').getValue() <= +(Ext.decode(response.responseText).montoMaximo))){
													
													if(Ext.getCmp('fechaAutorizacion').getValue()!=null && Ext.getCmp('fechaVencimiento').getValue()!=null ){
														Ext.getCmp('idstatus').setValue("2");
														guardadoAutorizacionServicio(_Existe,'0');//---4
													}
													else {
														centrarVentanaInterna(Ext.Msg.show({
															title: 'Error',
															msg: 'La fecha de autorizaci&oacute;n y fecha vencimiento son requeridas',
															buttons: Ext.Msg.OK,
															icon: Ext.Msg.ERROR
														}));
													}
												}else{
													centrarVentanaInterna(Ext.Msg.show({
														title: 'Aviso',
														msg: 'Este tr&aacute;mite debe de ser turnada al gerente  &iquest;Deseas guardar los cambios?',
														buttons: Ext.Msg.YESNO,
														icon: Ext.Msg.QUESTION,
														fn: function(buttonId, text, opt){
															if(buttonId == 'yes'){
																guardadoAutorizacionServicio(_Existe,'0');//---5
															}
														}
													}));
												}
											}else{
												Ext.getCmp('idstatus').setValue("2");
												guardadoAutorizacionServicio(_Existe,'0');//----6
											}
										},
										failure : function (){
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
							failure : function (){
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
				} else {
					centrarVentanaInterna(Ext.Msg.show({
						title: 'Aviso',
						msg: 'Complete la informaci&oacute;n requerida',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					}));
				}
			}
		},
		{
            text:'Vista Previa',
            icon:_CONTEXT+'/resources/fam3icons/icons/disk.png',
            id:'botonVoBo',
            disabled  : true,
            handler: function() {
            	if (panelInicialPrincipal.form.isValid()) {
                    var urlRequestImpCotiza = _0_urlRutaReporte
                                    + '?p_unieco='      + Ext.getCmp('idUnieco').getValue()
                                    + '&p_ramo='        + Ext.getCmp('idcdRamo').getValue()
                                    + '&p_estado='      + Ext.getCmp('idEstado').getValue()
                                    + '&p_poliza='      + Ext.getCmp('polizaAfectada').getValue()
                                    + '&P_AUTSER='      + Ext.getCmp('idNoAutorizacion').getValue()
                                    + '&P_CDPERSON='    + Ext.getCmp('idAsegurado').getValue()
                                    + '&destype=cache'
                                    + "&desformat=PDF"
                                    + "&userid="        + _0_reportsServerUser
                                    + "&ACCESSIBLE=YES"
                                    + "&report="        + _0_reporteVistaPreviaGral
                                    + "&paramform=no";
                            debug(urlRequestImpCotiza);
                            var numRand = Math.floor((Math.random() * 100000) + 1);
                            debug(numRand);
                            var windowVerDocu = Ext.create('Ext.window.Window',
                            {
                                title          : 'Vista Previa'
                                ,width         : 700
                                ,height        : 500
                                ,collapsible   : true
                                ,titleCollapse : true
                                ,html : '<iframe innerframe="'
                                        + numRand
                                        + '" frameborder="0" width="100" height="100"'
                                        + 'src="'
                                        + panDocUrlViewDoc
                                        + "?contentType=application/pdf&url="
                                        + encodeURIComponent(urlRequestImpCotiza)
                                        + "\">"
                                        + '</iframe>'
                                ,listeners :
                                {
                                    resize : function(win,width,height,opt)
                                    {
                                        debug(width,height);
                                        $('[innerframe="'+ numRand+ '"]').attr(
                                        {
                                            'width'   : width - 20
                                            ,'height' : height - 60
                                        });
                                    }
                                }
                            }).show();
                            windowVerDocu.center();
                } else {
                    centrarVentanaInterna(Ext.Msg.show({
                        title: 'Aviso',
                        msg: 'Complete la informaci&oacute;n requerida',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    }));
                }
            }
        }
		]
	});

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////			FUNCIONES A UTILIZAR DENTRO DE LA AUTORIZACION DE SERVICIO			//////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
    function obtieneDatosCoberturaSubCobertura(){
        Ext.Ajax.request({
            url    : _URL_MONTO_DISP_PROVEEDOR
            ,params:{
                'params.cobertura'   : Ext.getCmp('idCobAfectada').getValue(),
                'params.subcobertura': Ext.getCmp('idSubcobertura').getValue(),
                'params.cdramo'      : Ext.getCmp('idcdRamo').getValue(),
                'params.cdtipsit'    : Ext.getCmp('idcdtipsit').getValue()
            }
            ,success : function (response){
                Ext.getCmp('idSalarioMin').setValue('');
                Ext.getCmp('idReqPenalizacion').setValue('');
                Ext.getCmp('idValMaternidad').setValue('');
                Ext.getCmp('idReqValidacionMat').setValue('');
                Ext.getCmp('idValSesiones').setValue('');
                var json = Ext.decode(response.responseText).datosInformacionAdicional[0];
                var montoDisponible = json.SUMADISP;
                Ext.getCmp('idSalarioMin').setValue(montoDisponible);
                Ext.getCmp('idReqPenalizacion').setValue(json.REQPENALIZACION);
                Ext.getCmp('idValMaternidad').setValue(json.VALMATERNIDAD);
                Ext.getCmp('idReqValidacionMat').setValue(json.REQVALSUMASEGURADA);
                Ext.getCmp('idValSesiones').setValue(json.VALSESIONES);
                
                
                if(json.REQTIPOATENCION == "1"){
                    Ext.getCmp('idTipoEvento').show();
                    Ext.getCmp('idTipoEvento').allowBlank = false;
                }else{
                    Ext.getCmp('idTipoEvento').hide();
                    Ext.getCmp('idTipoEvento').allowBlank = true;
                }
                
                if(Ext.getCmp('idValMaternidad').getValue() == "1"){
                    Ext.Ajax.request({
                        url     : _URL_MESES_MAXIMO_MAT
                        ,params:{
                            'params.cdramo'   : Ext.getCmp('idcdRamo').getValue(),
                            'params.cdtipsit' : Ext.getCmp('idcdtipsit').getValue()
                        }
                        ,success : function (response){
                            var mesesMaximo = Ext.decode(response.responseText).mesMaximoMaternidad;
                            if(+ Ext.getCmp('idMesesAsegurado').getValue() < +mesesMaximo ){
                                centrarVentanaInterna(Ext.Msg.show({
                                    title: 'Error',
                                    msg: 'El n&uacute;mero de meses es menor al m&iacute;nimo requerido',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                }));
                            }
                        },
                        failure : function (){
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
                obtieneInformacion();
            },
            failure : function (){
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
        return true;
    }

    function obtieneInformacion() {
        Ext.getCmp('idDeducible').setValue('');
        Ext.getCmp('idCopago').setValue('');
        Ext.getCmp('idCopagoFin').setValue('');
        Ext.getCmp('idTipoCopago').setValue('');
        Ext.getCmp('idPenalCircHospitalario').setValue('');
        Ext.getCmp('idPenalCambioZona').setValue('');
        
        if(+Ext.getCmp('idValSesiones').getValue() > 0){
            Ext.getCmp('idCopagoPrevio').show();
            Ext.getCmp('idCopagoFin').hide();
        }else{
            Ext.getCmp('idCopagoPrevio').hide();
            Ext.getCmp('idCopagoFin').show();
        }
        Ext.Ajax.request({
            url     : _URL_CONSULTA_DEDUCIBLE_COPAGO
            ,params : {
                'params.cdunieco': Ext.getCmp('idUnieco').getValue(),
                'params.estado'  : Ext.getCmp('idEstado').getValue(),
                'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
                'params.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
                'params.nmsituac': Ext.getCmp('idNmSituac').getValue(),
                'params.cdgarant': Ext.getCmp('idCobAfectada').getValue(),
                'params.subcober': Ext.getCmp('idSubcobertura').getValue(),
                'params.cdpresta': Ext.getCmp('idProveedor').getValue(),
                'params.cdtipsit': Ext.getCmp('idcdtipsit').getValue()
            }
            ,success : function (response){
                var respuesta= Ext.decode(response.responseText);
                if(respuesta.listaDatosSiniestro != null){
                    var json=Ext.decode(response.responseText).listaDatosSiniestro[0];
                    Ext.getCmp('idDeducible').setValue(json.deducible);
                    Ext.getCmp('idTipoCopago').setValue(json.tipoCopago);
                    Ext.getCmp('idCopago').setValue(json.copago);
                    //obtenerCoaseguroHonorarios(storeQuirurgico);
                    debug("VALOR idReqPenalizacion :",Ext.getCmp('idReqPenalizacion').getValue(),"cveTipoAutorizaG : ",Ext.getCmp('cveTipoAutorizaG').getValue());
                    if(Ext.getCmp('idReqPenalizacion').getValue() == "1" && Ext.getCmp('cveTipoAutorizaG').getValue() != "3"){
                        var idProv = ""+Ext.getCmp('idProveedor').getValue();
                        if(idProv !="undefined"){
                            Ext.Ajax.request({
                                url     : _URL_CATALOGOS
                                ,params:{
                                    'params.cdpresta': Ext.getCmp('idProveedor').getValue(),
                                    catalogo         : _CAT_MEDICOS_ESPECIFICO,
                                    catalogoGenerico : true
                                }
                                ,success : function (response){
                                    if(Ext.decode(response.responseText).listaGenerica != null){
                                        var json=Ext.decode(response.responseText).listaGenerica[0];
                                        debug("VALOR DE RESPUESTA===========> ", json);
                                        Ext.getCmp('idCirculoHospProv').setValue(json.circulo);
                                        Ext.getCmp('codPostalProv').setValue(json.codpos);
                                        Ext.getCmp('idzonaHospProv').setValue(json.zonaHospitalaria);
                                        validarZonaCirculoHospitalario();
                                    }
                                },
                                failure : function (){
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
                    }else{
                        debug("Entra a 4");
                        if(Ext.getCmp('idCausaSini').getValue() =="N"){
                        //if(Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_ACCIDENTE || Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_MATERNIDAD){//4.-
                            Ext.getCmp('idCopagoFin').setValue('0');
                            Ext.getCmp('idCopagoPrevio').setValue('0');
                            Ext.getCmp('idPenalCircHospitalario').setValue('0');
                            Ext.getCmp('idPenalCambioZona').setValue('0');
                        }else{
                            if(Ext.getCmp('cveTipoAutorizaG').getValue() == "3"){
                                debug("Entra a la validacion if");
                                Ext.getCmp('idCopagoFin').setValue('0');
                                Ext.getCmp('idCopagoPrevio').setValue('0');
                            }else{
                                debug("Entra a la validacion else");
                                if(Ext.getCmp('idCopago').getValue() =="NA"||Ext.getCmp('idCopago').getValue()=="NO"){
                                    Ext.getCmp('idCopagoFin').setValue('0');
                                }else{
                                    Ext.getCmp('idCopagoFin').setValue(Ext.getCmp('idCopago').getValue());
                                }
                                if(Ext.getCmp('idEstatusTramite').getValue() == "2"){
                                    Ext.getCmp('idCopagoFin').setValue('0');
                                    Ext.getCmp('idCopagoPrevio').setValue('0');
                                }
                            }
                            Ext.getCmp('idPenalCircHospitalario').setValue('0');
                            Ext.getCmp('idPenalCambioZona').setValue('0');
                        }
                    }
                    obtenerCoaseguroHonorarios(storeQuirurgico);
                }
            },
            failure : function (){
                me.up().up().setLoading(false);
                centrarVentanaInterna(Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                }));
            }
        });
        return true;
    }
    
    function validarZonaCirculoHospitalario() {
        Ext.getCmp('idExclusionPenalizacion').setValue('');
        Ext.getCmp('idPenalCambioZona').setValue('');
        Ext.getCmp('idCopagoFin').setValue('');
        Ext.getCmp('idPenalCircHospitalario').setValue('');
        Ext.getCmp('idPenalCambioZona').setValue('');
        // === > Validacion de la causa del Siniestro
        debug("Entra a 5");
        if(Ext.getCmp('idCausaSini').getValue() =="N"){
        //if(Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_ACCIDENTE || Ext.getCmp('idCausaSiniestro').getValue() == _CODIGO_CAUSA_MATERNIDAD){//5.-
            Ext.getCmp('idCopagoFin').setValue('0');
            Ext.getCmp('idPenalCircHospitalario').setValue('0');
            Ext.getCmp('idPenalCambioZona').setValue('0');
        }else{
            if(validaRamoTramite (Ext.getCmp('idcdRamo').getValue()) == true){
                // ===> Verificamos que tenga exclusion de penalizacion
                Ext.Ajax.request({
                    url     : _URL_EXCLUSION_PENALIZACION
                    ,params:{
                        'params.cdunieco':Ext.getCmp('idUnieco').getValue(),
                        'params.estado':Ext.getCmp('idEstado').getValue(),
                        'params.cdramo':Ext.getCmp('idcdRamo').getValue(),
                        'params.nmpoliza':Ext.getCmp('polizaAfectada').getValue(),
                        'params.nmsituac':Ext.getCmp('idNmSituac').getValue()
                    }
                    ,success : function (response){
                        Ext.getCmp('idExclusionPenalizacion').setValue(Ext.decode(response.responseText).existePenalizacion);
                        if(Ext.getCmp('idExclusionPenalizacion').getValue()=="S"){
                            Ext.getCmp('idPenalCambioZona').setValue("0");
                            if(Ext.getCmp('idcdRamo').getValue() == _GMMPRUEBA){
                            	validacionCirculoHospitalario(Ext.getCmp('idcirculohosPoliza').getValue(),Ext.getCmp('idCirculoHospProv').getValue()); //-->1
                            }else{
                            	validacionCirculoHospitalario(Ext.getCmp('iddsplanAsegurado').getValue(),Ext.getCmp('idCirculoHospProv').getValue()); //-->1
                            }
                            
                        }else{
                            Ext.Ajax.request({
                                url     : _URL_PORCENTAJE_PENALIZACION
                                ,params:{
                                    'params.zonaContratada': Ext.getCmp('idZonaContratadaPoliza').getValue(),
                                    'params.zonaAtencion': Ext.getCmp('idzonaHospProv').getValue(),
                                    'params.cdRamo': Ext.getCmp('idcdRamo').getValue()
                                }
                                ,success : function (response){
                                    //alert(3);
                                    Ext.getCmp('idPenalCambioZona').setValue(Ext.decode(response.responseText).porcentajePenalizacion);
                                    if(Ext.getCmp('idcdRamo').getValue() == _GMMPRUEBA){
                                        validacionCirculoHospitalario(Ext.getCmp('idcirculohosPoliza').getValue(),Ext.getCmp('idCirculoHospProv').getValue()); //-->2
                                    }else{
                                        validacionCirculoHospitalario(Ext.getCmp('iddsplanAsegurado').getValue(),Ext.getCmp('idCirculoHospProv').getValue()); //-->2
                                    }
                                },
                                failure : function (){
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
                    failure : function (){
                        me.up().up().setLoading(false);
                        centrarVentanaInterna(Ext.Msg.show({
                            title:'Error',
                            msg: 'Error de comunicaci&oacute;n',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                        }));
                    }
                });
            }else{
                //2.- MULTISALUD O DIFERENTE DE MULTISALUD
                Ext.getCmp('idPenalCircHospitalario').setValue('0');
                Ext.getCmp('idPenalCambioZona').setValue("0");
                obtieneCirHospitalarioMultisalud();
            }
        }
        return true;
    }

    function validacionCirculoHospitalario(circuloHosPoliza,circuloHosProv){
        debug("VALOR DE LOS DATOS validacionCirculoHospitalario ==> ",circuloHosPoliza,circuloHosProv);
        Ext.Ajax.request({
            url     : _URL_CIRCULO_HOSPITALARIO
            ,params:{
                'params.circuloHosPoliza': circuloHosPoliza,
                'params.circuloHosProv': circuloHosProv,
                'params.cdramo': Ext.getCmp('idcdRamo').getValue(),
                'params.feautori': Ext.getCmp('fechaAutorizacion').getValue()
            }
            ,success : function (response){
                Ext.getCmp('idPenalCircHospitalario').setValue(Ext.decode(response.responseText).porcentajePenalizacion);
                validacionCopagoTotal();
            },
            failure : function (){
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
    
   function obtieneCirHospitalarioMultisalud(){
        Ext.Ajax.request({
            url     : _URL_CIRCULO_HOSP_MULTISALUD
            ,params : {
                'params.cdpresta': Ext.getCmp('idProveedor').getValue(),
                'params.cdramo': Ext.getCmp('idcdRamo').getValue(),
                'params.feautori': Ext.getCmp('fechaAutorizacion').getValue()
            }
            ,success : function (response){
                var datosExtras = Ext.decode(response.responseText);
                if(Ext.decode(response.responseText).datosInformacionAdicional != null){
                    var copagoOrig = Ext.getCmp('idCopago').getValue() ;
                    var sumatoria = 0;
                    var json=Ext.decode(response.responseText).datosInformacionAdicional[0];
                    if(json == null){
                        Ext.getCmp('idMultiploIncrem').setValue("0");
                        Ext.getCmp('idHospitalPlus').setValue("0");
                        Ext.getCmp('idPorcIncremento').setValue("0");
                    }else{
                        Ext.getCmp('idMultiploIncrem').setValue(json.MULTINCREMENTO);
                        Ext.getCmp('idHospitalPlus').setValue(json.HOSPITALPLUS);
                        Ext.getCmp('idPorcIncremento').setValue(json.PORCINCREMENTO);
                    }
                    validacionCopagoTotal();
                }
            },
            failure : function (){
                me.up().up().setLoading(false);
                centrarVentanaInterna(Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                }));
            }
        });
        return true;
    }

    function validacionCopagoTotal(){
        var copagoPositivo = 0;
        var copagoOrig = Ext.getCmp('idCopago').getValue() ;
        var tipoCopago = Ext.getCmp('idTipoCopago').getValue() ;
        var sumatoria  = 0;
        if(Ext.getCmp('idaplicaCirHosp').getValue() =='N'){
            Ext.getCmp('idPenalCircHospitalario').setValue("0")
        }
        if(Ext.getCmp('idProveedor').getValue() =="512805"){
        	Ext.getCmp('idPenalCircHospitalario').setValue("-10")
        }else{
        	Ext.getCmp('idPenalCircHospitalario').setValue("0")
        }
        
        if(Ext.getCmp('idaplicaZona').getValue() =='N'){
            Ext.getCmp('idPenalCambioZona').setValue("0")
        }
        
        if(validaRamoTramite (Ext.getCmp('idcdRamo').getValue()) == true){
            if( copagoOrig =="NO" || copagoOrig =="NA"){
                sumatoria = + Ext.getCmp('idPenalCircHospitalario').getValue() +  +Ext.getCmp('idPenalCambioZona').getValue();
                //Ext.getCmp('idCopagoFin').setValue(sumatoria);
                if(+sumatoria > 0){
                    Ext.getCmp('idCopagoFin').setValue(sumatoria);
                }else{
                    Ext.getCmp('idCopagoFin').setValue(copagoPositivo);
                }
                
                return true;
            }
            if(tipoCopago =="$"){
                sumatoria = + Ext.getCmp('idPenalCircHospitalario').getValue() + + Ext.getCmp('idPenalCambioZona').getValue();
                if(sumatoria > 0){
                    Ext.getCmp('idCopagoFin').setValue("$"+copagoOrig +" y "+ sumatoria +"%");
                }else{
                    Ext.getCmp('idCopagoFin').setValue(copagoOrig);
                }
                return true;
            }
            if(tipoCopago =="%"){
                sumatoria = + Ext.getCmp('idPenalCircHospitalario').getValue() + +Ext.getCmp('idPenalCambioZona').getValue() +  +copagoOrig.replace("%","");
                //Ext.getCmp('idCopagoFin').setValue(sumatoria);
                if(+sumatoria > 0){
                    Ext.getCmp('idCopagoFin').setValue(sumatoria);
                }else{
                    Ext.getCmp('idCopagoFin').setValue(copagoPositivo);
                }
                return true;
            }
            else{
                sumatoria = + Ext.getCmp('idPenalCircHospitalario').getValue() + +Ext.getCmp('idPenalCambioZona').getValue() +  +copagoOrig;//.replace("%","");
                //Ext.getCmp('idCopagoFin').setValue(sumatoria);
                if(+sumatoria > 0){
                    Ext.getCmp('idCopagoFin').setValue(sumatoria);
                }else{
                    Ext.getCmp('idCopagoFin').setValue(copagoPositivo);
                }
                return true;
            }
        }else{
            if(Ext.getCmp('idHospitalPlus').getValue() =="0"){
                Ext.getCmp('idCopagoFin').setValue(copagoOrig);
                return true;
            }else{
                var valorCopago = 0;
                if( copagoOrig =="NO" || copagoOrig =="NA" || copagoOrig =="null"|| copagoOrig == null){
                    valorCopago = 0;
                }else{
                    valorCopago = copagoOrig;
                }
                if(tipoCopago =="$"){
                    if(+Ext.getCmp('idPorcIncremento').getValue() > 0){
                        Ext.getCmp('idCopagoFin').setValue("$"+valorCopago +" y "+ Ext.getCmp('idPorcIncremento').getValue() +"%");
                    }else{
                        Ext.getCmp('idCopagoFin').setValue(valorCopago);
                    }
                    
                    return true;
                }
                if(tipoCopago =="%"){
                    var sumatoria = + valorCopago + +Ext.getCmp('idPorcIncremento').getValue();
                    //Ext.getCmp('idCopagoFin').setValue(sumatoria);
                    if(+sumatoria > 0){
                        Ext.getCmp('idCopagoFin').setValue(sumatoria);
                    }else{
                        Ext.getCmp('idCopagoFin').setValue(copagoPositivo);
                    }
                    return true;
                }
            }
        }
        return true;
    }
    
    function validaRamoTramite(ramoPoliza){
        if(ramoPoliza == _SALUD_VITAL || ramoPoliza == _GMMI || ramoPoliza == _GMMPRUEBA){
            return true;
        }else{
            return false;
        }
    }
    
    function especialidadMedico(cdprestaMedico){
        Ext.Ajax.request({
            url     : _URL_CATALOGOS
            ,params : {
                'params.cdpresta': cdprestaMedico,
                catalogo         : _CAT_MEDICOS_ESPECIFICO,
                catalogoGenerico : true
            }
            ,success : function (response) {
                if(Ext.decode(response.responseText).listaGenerica != null){
                    var json=Ext.decode(response.responseText).listaGenerica[0];
                    Ext.getCmp('idEspecialidad').setValue(json.descesp);
                }
            },
            failure : function (){
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
    
	function cargarInformacionAutorizacionServicio(nmautser,ntramite,cdrol) {
		/*************************************************************
		*  Obtenemos la informacion de los grid�s para los grids    *
		*************************************************************/
		Ext.Ajax.request( {
			url     : _URL_LISTADO_CONCEP_EQUIP
			,params : {
				'params.nmautser'  : nmautser
			}
			,success : function (response) {
				if(Ext.decode(response.responseText).listaConsultaTablas != null){
					var json = Ext.decode(response.responseText);
					for(var i = 0;i < json.listaConsultaTablas.length;i++){
						if(json.listaConsultaTablas[i].cdmedico == 0){
							var medicoCompleto = json.listaConsultaTablas[i].nombreMedico;
						}else{
							var medicoCompleto = json.listaConsultaTablas[i].cdmedico+" "+json.listaConsultaTablas[i].nombreMedico;
						}
						var cptCompleto = json.listaConsultaTablas[i].cdcpt+" "+json.listaConsultaTablas[i].desccpt;
						if(json.listaConsultaTablas[i].cdtipaut == 1){
							var rec = new modelListadoTablas({
								nmautser    : json.listaConsultaTablas[i].nmautser,
								cdtipaut    : json.listaConsultaTablas[i].cdtipaut,
								cdmedico    : json.listaConsultaTablas[i].cdmedico,
								nombreMedico: medicoCompleto,
								desccpt		: cptCompleto,
								precio		: json.listaConsultaTablas[i].precio,
								cdtipmed	: json.listaConsultaTablas[i].cdtipmed,
								cdcpt		: json.listaConsultaTablas[i].cdcpt,
								cantporc	: json.listaConsultaTablas[i].cantporc,
								ptimport	: json.listaConsultaTablas[i].ptimport,
								descTipMed	: json.listaConsultaTablas[i].descTipMed
							});
							storeConceptoAutorizados.add(rec);
						}
						else{
							var rec = new modelListadoTablas({
								nmautser	: json.listaConsultaTablas[i].nmautser,
								cdtipaut	: json.listaConsultaTablas[i].cdtipaut,
								cdmedico	: json.listaConsultaTablas[i].cdmedico,
								nombreMedico: medicoCompleto,
								desccpt		: cptCompleto,
								precio		: json.listaConsultaTablas[i].precio,
								cdtipmed	: json.listaConsultaTablas[i].cdtipmed,
								cdcpt		: json.listaConsultaTablas[i].cdcpt,
								cantporc	: (json.listaConsultaTablas[i].cantporc) * 100,
								ptimport	: json.listaConsultaTablas[i].ptimport,
								descTipMed	: json.listaConsultaTablas[i].descTipMed
							});

							if(json.listaConsultaTablas[i].cdtipaut == 2){
								storeQuirugicoBase.add(rec);
								obtenerValorBase(storeQuirugicoBase);//-->
							}

							if(json.listaConsultaTablas[i].cdtipaut == 3){
								storeQuirurgico.add(rec);
								Ext.getCmp('btnVoBoProveedor').setDisabled(false);
							}
						}
					}
				}
			},
			failure : function (){
				me.up().up().setLoading(false);
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});

		/**********************************************************************
		*  Obtenemos la autorizacion especifica del numero de autorizacion   *
		**********************************************************************/
		Ext.Ajax.request({
			url     : _URL_CONSULTA_AUTORIZACION_ESP
			,params : {
				'params.nmautser'  : nmautser
			}
			,success : function (response) {
				var json=Ext.decode(response.responseText).datosAutorizacionEsp;
				debug("Valor del json :) ==> ",json);
				var cdtipsitRe= json.cdtipsit;
				Ext.getCmp('botonVoBo').setDisabled(false);
        		Ext.getCmp('idcdtipsit').setValue(json);
				if(json.aplicaCirHos == null ||json.aplicaCirHos ==''){
					Ext.getCmp('idaplicaCirHosp').setValue('S');
				}else{
					Ext.getCmp('idaplicaCirHosp').setValue(json.aplicaCirHos);
				}
				if(json.idaplicaZona == null ||json.idaplicaZona ==''){
					Ext.getCmp('idaplicaZona').setValue('S');
				}else{
					Ext.getCmp('idaplicaZona').setValue(json.aplicaZonaHosp);
				}
				
				
				debug("json.aplicaNeg  ====>>>"+json.aplicaNeg);
				
				if(json.aplicaNeg == null ||json.aplicaNeg ==''){
                    Ext.getCmp('idaplicaNegociacion').setValue('N');
                    Ext.getCmp('sumDisponible').setReadOnly(true);
                }else{
                    Ext.getCmp('idaplicaNegociacion').setValue(json.aplicaNeg);
                    Ext.getCmp('sumDisponible').setReadOnly(false);
                }
                
                if(json.tipoServ == null ||json.tipoServ ==''){
                    Ext.getCmp('idTipoEventoGNP').setValue('CP');
                    Ext.getCmp('fechaAutorizacion').setReadOnly(false);
                    Ext.getCmp('fechaIngreso').setReadOnly(false);
                }else{
                    Ext.getCmp('idTipoEventoGNP').setValue(json.tipoServ);
                    
                    if(Ext.getCmp('idTipoEventoGNP').getValue()=="RH"){
                    	Ext.getCmp('fechaAutorizacion').setReadOnly(true);
                        Ext.getCmp('fechaIngreso').setReadOnly(true);
                    }else{
                    	Ext.getCmp('fechaAutorizacion').setReadOnly(false);
                        Ext.getCmp('fechaIngreso').setReadOnly(false);
                    }
                }
				
				Ext.getCmp('idModalidad').setValue(json.descTipsit);
				Ext.getCmp('idUnieco').setValue(json.cdunieco);								// Valor de Cdunieco
				Ext.getCmp('idEstado').setValue(json.estado);								// Valor de Estado
				Ext.getCmp('idcdRamo').setValue(json.cdramo);								// Valor del Ramo
				Ext.getCmp('idNmSituac').setValue(json.nmsituac);							// Valor de Nmsituac
				Ext.getCmp('idEstatusTramite').setValue(json.status);						// Valor del estatus del tramite
				Ext.getCmp('idAaapertu').setValue(json.aaapertu);							// Valor de anio de apertura
				if(Ext.getCmp('claveTipoAutoriza').getValue() == 3 ){
					Ext.getCmp('idNoAutorizacion').setValue();								// Valor de N�mero de autorizaci�n
					Ext.getCmp('idNumeroAnterior').setValue(json.nmautser);					// Valor de N�mero de autorizacion Anterior
				}else{
					Ext.getCmp('idNoAutorizacion').setValue(json.nmautser);					// Valor de N�mero de autorizaci�n
					Ext.getCmp('idNumeroAnterior').setValue(json.nmautant);					// Valor de N�mero de autorizacion Anterior
					debug("Valor de json.cveTipoAutorizaG -->"+json.cveTipoAutorizaG);
					if(json.cveTipoAutorizaG == "1"||json.cveTipoAutorizaG == "2" ||json.cveTipoAutorizaG == null ||json.cveTipoAutorizaG == ''){
						Ext.getCmp('cveTipoAutorizaG').setValue("2");						// Asignacion de Cve Tipo autorizaG
					}else{
						Ext.getCmp('cveTipoAutorizaG').setValue(json.cveTipoAutorizaG);		// Asignacion de Cve Tipo autorizaG
					}
				}
				Ext.getCmp('idNumtramiteInicial').setValue(ntramite);						// Valor de Numero de tramite Inicial
				storeAsegurados.load({
					params:{
						'params.cdperson':json.cdperson
					}
				});
				panelInicialPrincipal.down('[name=cdperson]').setValue(json.cdperson);		// Valor del asegurado
				var dsnom = json.cdperson+" "+json.nombreCliente;
				Ext.getCmp('dsNombreAsegurado').setValue(dsnom);							// Valor del Nombre del asegurado
				Ext.getCmp('fechaSolicitud').setValue(json.fesolici);						// Valor de Fecha Solicitud
				Ext.getCmp('fechaIngreso').setValue(json.feingres);							// Valor de Fecha de Ingreso
				Ext.getCmp('fechaVencimiento').setValue(json.fevencim);						// Valor de Fecha Afectado
				Ext.getCmp('polizaAfectada').setValue(json.nmpoliza);						// Valor de P�liza Afectada
				Ext.getCmp('idMedico').setValue(json.cdmedico);								// Valor del Id del M�dico
				if(json.cdmedico == '0'){
					Ext.getCmp('medicoPExt').setValue(json.nombreMedico);					// Valor del Medico
					Ext.getCmp('especialidadPExt').setValue(json.especialidadMedico);		// Valor de la especialidad
					Ext.getCmp('medicoPExt').show();
					Ext.getCmp('especialidadPExt').show();
					Ext.getCmp('idEspecialidad').hide();
					
				}else{
					especialidadMedico(json.cdmedico);
					Ext.getCmp('medicoPExt').hide();
					Ext.getCmp('especialidadPExt').hide();
					Ext.getCmp('idEspecialidad').show();
				}
				Ext.getCmp('tratamiento').setValue(json.dstratam);							// Valor del Tratamiento
				Ext.getCmp('observaciones').setValue(json.dsobserv);						// Valor de Observaciones
				Ext.getCmp('notaInterna').setValue(json.dsnotas);							// Valor de Nota Interna
				Ext.getCmp('sumDisponible').setValue(json.mtsumadp);						// Valor de Suma Disponible
				Ext.getCmp('idCopagoPrevio').setValue(json.copagofi);						// Valor de Copago Previo
				debug("Valor del copago recuperado -->",json.copagofi);
				Ext.getCmp('idCopagoFin').setValue(json.copagofi); 							// Valor de Copago Fin
				Ext.getCmp('idPenalCircHospitalario').setValue(json.porpenal);				// Valor de Penal. x Circulo hospitalario
				storePlazas.load();
				Ext.getCmp('idSucursal').setValue(json.cduniecs);							// Valor de la Plazas
				storeRamos.load({
					params:{
						'params.idPadre':Ext.getCmp('idSucursal').getValue()
					}
				});
				Ext.getCmp('cmbRamos').setValue(json.cdramo);								// Valor de Ramos
				panelInicialPrincipal.down('[name="cdperson"]').setReadOnly(false);
				Ext.getCmp('edad').setValue(json.fenacimi); // (EGS)
				Ext.getCmp('genero').setValue(json.genero); // (EGS)
				storeTiposICD.removeAll(); // (EGS)
				storeTiposICD.load({ // consulta de catalogo (EGS)
					params:{
						'params.cdicd' 		:  json.cdicd,
						'params.cdramo' 	: json.cdramo,
						'params.cdtipsit' 	: json.cdtipsit,
						'params.edad' 		: calculaAniosTranscurridos(Ext.getCmp('edad').getValue(),new Date()),
						'params.genero' 	: Ext.getCmp('genero').getValue()
					}
				
				});				
				Ext.getCmp('idComboICD').setValue(json.cdicd);								// Valor de ICD
                storeTiposMedicos.removeAll();
                storeTiposMedicos.load({
                    params:{
                        'params.cdicd'   : Ext.getCmp('idComboICD').getValue(),
                        'params.cdpts'   : Ext.getCmp('idcptGeneral').getValue()
                    }                    
                });
                
                storeTiposCPTUnico.removeAll();
                 storeTiposCPTUnico.load({
                    params:{
                        'params.cdicd'   : Ext.getCmp('idComboICD').getValue()
                    }                    
                 });
				//debug("<--- Valor de cdrol --->",cdrol);
				if(cdrol == "COORDINAMED"){
					Ext.getCmp('Autorizar').hide();
				}else{
					Ext.getCmp('Autorizar').show();
				}
				
				var dateFechaAutorizacion= json.feautori;
				Ext.getCmp('fechaAutorizacion').setValue(dateFechaAutorizacion);
				
				storeTipoEvento.load({
                    params:{
                        'params.cdramo'      : json.cdramo,
                        'params.cdtipsit'    : json.cdtipsit,
                        'params.cdgarant'    : json.cdgarant,
                        'params.cveCatalogo' : "1"
                    }
                });
                
                Ext.getCmp('btnSiniestralidad').setDisabled(false);
                Ext.getCmp('btnVerCoberturas').setDisabled(false);
                Ext.getCmp('btnDetallePoliza').setDisabled(false);
                
                
                

                    
                /*Ext.Ajax.request({
                                    url     : _URL_DATOS_COMPLEMENTO_ICD
                                    ,params : {
                                        'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
                                        'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
                                        'params.estado'    : Ext.getCmp('idEstado').getValue(),
                                        'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
                                        'params.cdicd'     : Ext.getCmp('idComboICD').getValue(),
                                        'params.cdperson'  : Ext.getCmp('idAsegurado').getValue()
                                    }
                                    ,success : function (response){
                                        var resEspera = Ext.decode(response.responseText).msgResult;
                                        debug("Valor de respuesta ==> ",resEspera);
                                        var fields = resEspera.split('|');
                                        var subsecuente = fields[0];
                                        var noReclamo   = fields[1];
                                        
                                        if(subsecuente =="N"){
                                             Ext.getCmp('idTipo').setValue("NUEVO");
                                        }else{
                                             Ext.getCmp('idTipo').setValue("SUBSECUENTE");
                                        }
                                        Ext.getCmp('idNumSubsecuente').setValue(json.numrecla);
                                        
                                    },
                                    failure : function (){
                                        me.up().up().setLoading(false);
                                        centrarVentanaInterna(Ext.Msg.show({
                                            title:'Error',
                                            msg: 'Error de comunicaci&oacute;n',
                                            buttons: Ext.Msg.OK,
                                            icon: Ext.Msg.ERROR
                                        }));
                                    }
                                });*/
                
                
                
                var storeClausulasPoliza = new Ext.data.Store({
                    model      : 'ClausulaModelo',
                    //autoLoad: true,
                    proxy     : {
                        type        : 'ajax',
                        url         : _URL_CONSULTA_CLAUSULAS_POLIZA,
                        extraParams : {
                            'params.cdunieco' : Ext.getCmp('idUnieco').getValue(),
                            'params.cdramo'   : Ext.getCmp('idcdRamo').getValue(),
                            'params.estado'   : Ext.getCmp('idEstado').getValue(),
                            'params.nmpoliza' : Ext.getCmp('polizaAfectada').getValue(),
                            'params.nmsituac' : Ext.getCmp('idNmSituac').getValue(),
                            'params.icodpoliza' : null,
                            'params.cdperson'  : null
                        },
                        reader : {
                            type : 'json',
                            root : 'clausulasPoliza'
                        }
                    }
                });
                storeClausulasPoliza.load({
                    callback: function(records, operation, success) {
                        if (!success) {
                            return;
                        }
                        if(records.length == 0){
                            Ext.getCmp('btnExclusionPoliza').setDisabled(true);
                        }else{
                            Ext.getCmp('btnExclusionPoliza').setDisabled(false);
                        }
                        return;
                    }
                });
                
                var storeHistorialICD= new Ext.data.Store({
                    model      : 'ModelHistorialICD',
                    proxy     : {
                        type        : 'ajax',
                        url         : _URL_cargaHistorialSinies,
                        extraParams : {
                            'params.pv_cdunieco_i' : Ext.getCmp('idUnieco').getValue(),
                            'params.pv_cdramo_i'   : Ext.getCmp('idcdRamo').getValue(),
                            'params.pv_estado_i'   : Ext.getCmp('idEstado').getValue(),
                            'params.pv_nmpoliza_i' : Ext.getCmp('polizaAfectada').getValue(),
                            'params.pv_nmsuplem_i' : Ext.getCmp('idNmsuplem').getValue(),
                            'params.pv_nmsituac_i' : Ext.getCmp('idNmSituac').getValue()
                        },
                        reader : {
                            type : 'json',
                            root : 'loadList'
                        }
                    }
                });
                storeHistorialICD.load({
                    callback: function(records, operation, success) {
                        if (!success) {
                            return;
                        }
                        if(records.length == 0){
                            Ext.getCmp('btnExclusionICD').setDisabled(true);
                        }else{
                            Ext.getCmp('btnExclusionICD').setDisabled(false);
                        }
                        return;
                    }
                });
                //Ext.getCmp('idTipoEvento').setValue(json.idTipoEvento);
				
				//**********************************************************************
				//*  Obtenemos la informacion del proveedor, Asi como la informacion    *
				//*  adicional para las validaciones de Siniestro     				    *
				//**********************************************************************
				Ext.Ajax.request( {
					url    : _URL_MONTO_DISP_PROVEEDOR
					,params:{
						'params.cobertura'   : json.cdgarant,
						'params.subcobertura': json.cdconval,
						'params.cdramo'      : json.cdramo,
						'params.cdtipsit'    : json.cdtipsit
					}
					,success : function (response) {
						var jsonRes = Ext.decode(response.responseText).datosInformacionAdicional[0];
						//debug("VALOR DEL JSONRES -->",jsonRes);
						var montoDisponible = json.SUMADISP;
						Ext.getCmp('idSalarioMin').setValue(montoDisponible);					// Valor del monto disponible
						Ext.getCmp('idReqPenalizacion').setValue(jsonRes.REQPENALIZACION);		// Valor para verificar si requiere penalizacion
						Ext.getCmp('idValMaternidad').setValue(jsonRes.VALMATERNIDAD);			// Valor para verificar la validacion de maternidad
						Ext.getCmp('idReqValidacionMat').setValue(jsonRes.REQVALSUMASEGURADA);	// Valor para verificar la validacion de maternidad
						Ext.getCmp('idValSesiones').setValue(jsonRes.VALSESIONES);				// Valor para Validacion de Sesiones
						if(+Ext.getCmp('idValSesiones').getValue() > 0){
							Ext.getCmp('idCopagoPrevio').show();
							Ext.getCmp('idCopagoFin').hide();
						}else{
							Ext.getCmp('idCopagoPrevio').hide();
							Ext.getCmp('idCopagoFin').show();
						}
						
						if(jsonRes.REQTIPOATENCION == "1"){
                            Ext.getCmp('idTipoEvento').show();
                            Ext.getCmp('idTipoEvento').allowBlank = false;
                        }else{
                            Ext.getCmp('idTipoEvento').hide();
                            Ext.getCmp('idTipoEvento').allowBlank = true;
                        }
					},
					failure : function (){
						Ext.Msg.show({
							title:'Error',
							msg: 'Error de comunicaci&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						});
					}
				});
				
				//**********************************************************************
				//*  Obtenemos la informacion de la poliza en especifico para obtener   *
				//*  los valores que se necesita para la penalizacion				    *
				//**********************************************************************
				Ext.Ajax.request( {
					url     : _URL_POLIZA_UNICA
					,params : {
						'params.cdunieco': Ext.getCmp('idUnieco').getValue(),
						'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
						'params.estado'  : Ext.getCmp('idEstado').getValue(),
						'params.nmpoliza': Ext.getCmp('polizaAfectada').getValue(),
						'params.cdperson': Ext.getCmp('idAsegurado').getValue()
					}
					,success : function (response){
						if(Ext.decode(response.responseText).polizaUnica != null) {
							var json=Ext.decode(response.responseText).polizaUnica[0];
							debug("Valor de Poliza Unica ==>>>>> ",json);
							if(json.mtoBase == null ||json.mtoBase ==''){
								Ext.getCmp('idMontoBase').setValue("21000");						// Valor del Monto Base si viene vacio
							}else{
								Ext.getCmp('idMontoBase').setValue(json.mtoBase);					// Asignacion del Valor monto base
							}
							Ext.getCmp('idNmsuplem').setValue(json.nmsuplem);						// Valor del nmsuplem
							Ext.getCmp('idZonaContratadaPoliza').setValue(json.zonaContratada);		// Valor de la zona contratada de la poliza
							Ext.getCmp('polizaAfectadaCom').setValue(json.numPoliza);				// Valor de nmpoliex
							Ext.getCmp('idcdtipsit').setValue(json.cdtipsit);						// Valor de CDTIPSIT
							Ext.getCmp('iddsplanAsegurado').setValue(json.dsplan);					// Valor del Plan Asegurado
							Ext.getCmp('idMesesAsegurado').setValue(json.mesesAsegurado);			// Valor del No. de Meses
							Ext.getCmp('fechaAutorizacion').setValue(dateFechaAutorizacion);		// Valor de Fecha de autorizacion
							Ext.getCmp('idcirculohosPoliza').setValue(json.cirHosp);
						}
						
		                //-->
                        Ext.Ajax.request({
                            url     : _URL_VALIDA_EXCLUSION
                            ,params : {
                                'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
                                'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
                                'params.estado'    : Ext.getCmp('idEstado').getValue(),
                                'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
                                'params.nmsuplem'  : Ext.getCmp('idNmsuplem').getValue(),
                                'params.nmsituac'  : Ext.getCmp('idNmSituac').getValue(),
                                'params.cdicd'     : Ext.getCmp('idComboICD').getValue()
                            }
                            ,success : function (response){
                                var resEspera = Ext.decode(response.responseText).msgResult;
                                debug("Valor de respuesta ==> ",resEspera);
                                if(resEspera =="S"){
                                    //Ext.getCmp('autorizar').setDisabled(false);
                                    Ext.getCmp('Autorizar').hide();
                                    rechazoAutorizacionServicio();
                                }else{
                                    //Ext.getCmp('Autorizar').show();
                                    //Validamos tiempo de Espera
                                    Ext.Ajax.request({
                                        url     : _URL_NUM_MESES_TIEMPO_ESPERA
                                        ,params : {
                                            'params.cdramo'  : Ext.getCmp('idcdRamo').getValue(),
                                            'params.cdtipsit': Ext.getCmp('idcdtipsit').getValue(),
                                            'params.cdicd'   : Ext.getCmp('idComboICD').getValue(),
                                            'params.dsplan'  : Ext.getCmp('iddsplanAsegurado').getValue()
                                        }
                                        ,success : function (response){
                                            var resEspera = Ext.decode(response.responseText);
                                            var tiempoEsperaICD = resEspera.mesesTiempoEspera;
                                            if(!(+Ext.getCmp('idMesesAsegurado').getValue() >= +tiempoEsperaICD)){
                                                if(Ext.getCmp('idCausaSiniestro').getValue()!= _CODIGO_CAUSA_ACCIDENTE){
                                                    Ext.getCmp('Autorizar').hide();
                                                    rechazoAutorizacionServicio();
                                                }
                                            }else{
                                                Ext.getCmp('Autorizar').show();
                                                Ext.Ajax.request({
                                                    url     : _URL_DATOS_COMPLEMENTO_ICD
                                                    ,params : {
                                                        'params.cdunieco'  : Ext.getCmp('idUnieco').getValue(),
                                                        'params.cdramo'    : Ext.getCmp('idcdRamo').getValue(),
                                                        'params.estado'    : Ext.getCmp('idEstado').getValue(),
                                                        'params.nmpoliza'  : Ext.getCmp('polizaAfectada').getValue(),
                                                        'params.cdicd'     : Ext.getCmp('idComboICD').getValue(),
                                                        'params.cdperson'  : Ext.getCmp('idAsegurado').getValue()
                                                    }
                                                    ,success : function (response){
                                                        var resEspera = Ext.decode(response.responseText).msgResult;
                                                        debug("Valor de respuesta ==> ",resEspera);
                                                        var fields = resEspera.split('|');
                                                        var subsecuente = fields[0];
                                                        var noReclamo   = fields[1];
                                                        
                                                        if(subsecuente =="N"){
                                                             Ext.getCmp('idTipo').setValue("NUEVO");
                                                        }else{
                                                             Ext.getCmp('idTipo').setValue("SUBSECUENTE");
                                                        }
                                                        Ext.getCmp('idNumSubsecuente').setValue(noReclamo);
                                                    },
                                                    failure : function (){
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
                                        failure : function (){
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
                            failure : function (){
                                me.up().up().setLoading(false);
                                centrarVentanaInterna(Ext.Msg.show({
                                    title:'Error',
                                    msg: 'Error de comunicaci&oacute;n',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                }));
                            }
                        });
					},
					failure : function (){
						me.up().up().setLoading(false);
						centrarVentanaInterna(Ext.Msg.show({
							title:'Error',
							msg: 'Error de comunicaci&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						}));
					}
				});

				

                
                
				//Se llena la informacion de la Cobertura y se realiza la asignacion
				storeCobertura.load({
					params:{
						'params.cdunieco':Ext.getCmp('idUnieco').getValue(),
						'params.estado':Ext.getCmp('idEstado').getValue(),
						'params.cdramo':Ext.getCmp('idcdRamo').getValue(),
						'params.nmpoliza':Ext.getCmp('polizaAfectada').getValue(),
						'params.nmsituac':Ext.getCmp('idNmSituac').getValue()
					}
				});
				Ext.getCmp('idCobAfectada').setValue(json.cdgarant);							// Valor de la Cobertura
				//Se llena la informacion de la Sub-Cobertura y se realiza la asignacion
				storeSubcobertura.load({
					params:{
						'params.cdunieco' : Ext.getCmp('idUnieco').getValue(),
						'params.cdramo' : Ext.getCmp('idcdRamo').getValue(),
						'params.estado' : Ext.getCmp('idEstado').getValue(),
						'params.nmpoliza' : Ext.getCmp('polizaAfectada').getValue(),
						'params.nmsituac' : Ext.getCmp('idNmSituac').getValue(),
						'params.cdtipsit' : json.cdtipsit, //Ext.getCmp('idcdtipsit').getValue(),
						'params.cdgarant' : Ext.getCmp('idCobAfectada').getValue(),
						'params.cdsubcob' : null
					}
				});
				Ext.getCmp('idSubcobertura').setValue(json.cdconval);							// Valor de la Sub-Cobertura
				Ext.getCmp('idProveedor').setValue(json.cdprovee);								// Valor del Proveedor
				storeCausaSinestro.load();
				Ext.getCmp('idCausaSiniestro').setValue(json.cdcausa);							// Valor de la Causa del Siniestro
				
				Ext.Ajax.request({
					url     : _URL_VAL_CAUSASINI
					,params : {
						'params.cdramo'   : Ext.getCmp('idcdRamo').getValue(),
						'params.cdtipsit' : json.cdtipsit,
						'params.causaSini': 'CAUSA',
						'params.cveCausa' : json.cdcausa
					}
					,success : function (response){
						var datosExtras = Ext.decode(response.responseText);
						if(Ext.decode(response.responseText).datosInformacionAdicional != null){
							var cveCauSini=Ext.decode(response.responseText).datosInformacionAdicional[0];
							debug("cveCauSini ===>",cveCauSini);
							Ext.getCmp('idCausaSini').setValue(cveCauSini.REQVALIDACION);
							obtieneInformacion();
						}else{
							obtieneInformacion();
						}
					},
					failure : function (){
						me.up().up().setLoading(false);
						centrarVentanaInterna(Ext.Msg.show({
							title:'Error',
							msg: 'Error de comunicaci&oacute;n',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR
						}));
					}
				});
			},
			failure : function (){
				me.up().up().setLoading(false);
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
		
		return true;
	}
	
	function obtenerValorBase(storeQuirugicoBase){
		var arr = [];
		var valorBase=0;
		var cptCompletos ="";
		storeQuirugicoBase.each(function(record) {
			arr.push(record.data);
		});

		debug()
		
		for(var i = 0; i < arr.length; i++){
			debug("Valor de la respuesta ==> ",arr[i]);
			valorBase=parseFloat(valorBase) + parseFloat(arr[i].ptimport);
			cptCompletos = cptCompletos+arr[i].cdcpt+"|";
		}
		
		Ext.getCmp('idValorBase').setValue(valorBase);
		Ext.getCmp('idcptGeneral').setValue(cptCompletos);
		Ext.getCmp('sumDisponible').setValue(valorBase);
		
		debug("Valor de la suma Disponible ==>"+valorBase);
		debug("Valor de la respuesta cptCompletos ==> ",cptCompletos);
		//Realizamos el llamado para la actualizacion de los campos 
        if(Ext.getCmp('idComboICD').getValue() != null){
            storeTiposMedicos.removeAll();
            storeTiposMedicos.load({
                params:{
                    'params.cdicd'   : Ext.getCmp('idComboICD').getValue(),
                    'params.cdpts'   : cptCompletos
                }                    
            });
        }
        return true;
	}
	
    function obtenerCoaseguroHonorarios(storeQuirurgico){
        var arr = [];
        var valorHonararios =0;
        storeQuirurgico.each(function(record) {
            arr.push(record.data);
        });
        
        for(var i = 0; i < arr.length; i++){
            debug("Valor de la respuesta ==> ",arr[i]);
            valorHonararios=parseFloat(valorHonararios) + parseFloat(arr[i].ptimport);
        }
        
        if(Ext.getCmp('idaplicaNegociacion').getValue() =="S"){
        	
        }else{
        	Ext.getCmp('sumDisponible').setValue(+valorHonararios);
        }
        if(Ext.getCmp('idProveedor').getValue() =="512805" || Ext.getCmp('idProveedor').getValue() =="860360"){
        	Ext.getCmp('idCoaseguroHon').setValue(valorHonararios * (+0/100));
            var importe = Ext.getCmp('idDeducible').getValue();
            var importeModificado = importe.replace(",","");
            Ext.getCmp('idCoaseguroHos').setValue((+Ext.getCmp('sumDisponible').getValue() - +importeModificado)* (+0/100));
        }else{
        	Ext.getCmp('idCoaseguroHon').setValue(valorHonararios * (+10/100));
            var importe = Ext.getCmp('idDeducible').getValue();
            var importeModificado = importe.replace(",","");
            Ext.getCmp('idCoaseguroHos').setValue((+Ext.getCmp('sumDisponible').getValue() - +importeModificado)* (+10/100));
        }
        
        
        
        return true;
    }
	
	function TotalRegistros(storeRecibido){
		var arr = [];
		var totalRegistro = 0;
		var valorBase=0;
		storeRecibido.each(function(record) {
			arr.push(record.data);
		});
		for(var i = 0; i < arr.length; i++){
			totalRegistro = i;
		}
		return totalRegistro;
	}

	function ModificarEquipoQuirurguico(storeQuirurgico){
		var arr = [];
		if(storeQuirurgico != undefined){
			storeQuirurgico.each(function(record) {
			arr.push(record.data);
			});
			storeQuirurgico.removeAll();
			for(var i = 0; i < arr.length; i++){
				var rec = new modelListadoTablas({
				cdmedico: arr[i].cdmedico,
				cantporc: arr[i].cantporc,
				cdcpt: arr[i].cdcpt,
				cdmedico: arr[i].cdmedico,
				cdtipaut: arr[i].cdtipaut,
				cdtipmed: arr[i].cdtipmed,
				descTipMed: arr[i].descTipMed,
				desccpt: arr[i].desccpt,
				nmautser: arr[i].nmautser,
				nombreMedico: arr[i].nombreMedico,
				precio: arr[i].precio,
				ptimport: Ext.getCmp('idValorBase').getValue() *(arr[i].cantporc/100)
				});
				storeQuirurgico.add(rec);
			}
			obtenerCoaseguroHonorarios(storeQuirurgico);
		}
		return true;
	}

	function  guardadoAutorizacionServicio(valor,guardado){//1.-
		var respuesta=true;
		var submitValues={};
		
		var formulario=panelInicialPrincipal.form.getValues();
		submitValues['params']=formulario;
		submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);

		var datosTablas = [];
		storeConceptoAutorizados.each(function(record,index){
			datosTablas.push({
				cdtipaut: record.get('cdtipaut'),
				cdmedico: record.get('cdmedico'),
				cdcpt: record.get('cdcpt'),
				precio: record.get('precio'),
				cantporc: record.get('cantporc'),
				ptimport: record.get('ptimport'),
				cdtipmed: record.get('cdtipmed'),
				nombreMedico: record.get('nombreMedico')
			});
		});
		storeQuirugicoBase.each(function(record,index){
			datosTablas.push({
				cdtipaut: record.get('cdtipaut'),
				cdmedico: record.get('cdmedico'),
				cdcpt: record.get('cdcpt'),
				precio: record.get('precio'),
				cantporc: record.get('cantporc')/100,
				ptimport: record.get('ptimport'),
				cdtipmed: record.get('cdtipmed'),
				nombreMedico: record.get('nombreMedico')
			});
		});
		storeQuirurgico.each(function(record,index){
			datosTablas.push({
				cdtipaut: record.get('cdtipaut'),
				cdmedico: record.get('cdmedico'),
				cdcpt: record.get('cdcpt'),
				precio: record.get('precio'),
				cantporc: record.get('cantporc')/100,
				ptimport: record.get('ptimport'),
				cdtipmed: record.get('cdtipmed'),
				nombreMedico: record.get('nombreMedico')
			});
		});

		var valorIdEstatus= Ext.getCmp('idstatus').getValue();
		submitValues['datosTablas']=datosTablas;
		submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);
		panelInicialPrincipal.setLoading(true);
		Ext.Ajax.request( {
			url: _URL_GUARDA_AUTORIZACION,
			jsonData:Ext.encode(submitValues), // convierte a estructura JSON
			success:function(response,opts){
				panelInicialPrincipal.setLoading(false);
				var jsonResp = Ext.decode(response.responseText);
				if(jsonResp.success==true){
					var numeroAutorizacion = Ext.decode(response.responseText).numeroAutorizacion.nmautser;
					var ntramiteEntrada    = Ext.decode(response.responseText).numeroAutorizacion.ntramite;
					Ext.getCmp('idNoAutorizacion').setValue(numeroAutorizacion);
					var mensaje='';
					// si el estatus es igual a 2 se va a autorizar
					if(Ext.getCmp('idstatus').getValue() == "2"){
						mensaje= 'Se gener&oacute; la carta para la autorizaci&oacute;n con el n&uacute;mero ';
					}else{
						if(Ext.getCmp('claveTipoAutoriza').getValue() == "1"){
							mensaje = 'Se guard&oacute; la autorizaci&oacute;n de servicio con el n&uacute;mero ';
						}
						if(Ext.getCmp('claveTipoAutoriza').getValue() == "2") {
							mensaje = 'Se modific&oacute; la autorizaci&oacute;n con el n&uacute;mero ';
						}
						if(Ext.getCmp('claveTipoAutoriza').getValue() == "3") {
							mensaje= 'Se complement&oacute; la autorizaci&oacute;n de servicio con el n&uacute;mero ';
						}
					}
					if(valor=="N") {
						mensaje= mensaje+" : "+numeroAutorizacion +". Para autorizar se requiere al menos subir un documento.";
					}else{
						mensaje= mensaje+" : "+numeroAutorizacion +".";
					}
					
					if(valor=="N"){
						mensajeCorrecto('Datos guardados',mensaje,function(){
                            Ext.create('Ext.form.Panel').submit({
                                url             : _p12_urlMesaControl
                                ,standardSubmit : true
                                ,params         : {
                                    'smap1.gridTitle'      : 'Autorizaci\u00F3n de servicio'
                                    ,'smap2.pv_cdtiptra_i' : _AUTORIZACION_SERVICIO
                                }
                            });
                        });
					}else{
						if(guardado =='1'){
                        	Ext.create('Ext.form.Panel').submit({
                                standardSubmit : true
                                ,params        : {
                                    'params.nmAutSer':numeroAutorizacion
                                    ,'params.ntramite':ntramiteEntrada
                                    ,'params.cdrol':valorAction.cdrol
                                }
                            });
                        }else{
                        	//_mask();
                            //location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?ntramite=' + ntramiteEntrada;
                            mensajeCorrecto('Datos guardados',mensaje,function(){
                                Ext.create('Ext.form.Panel').submit({
                                    url             : _p12_urlMesaControl
                                    ,standardSubmit : true
                                    ,params         : {
                                        'smap1.gridTitle'      : 'Autorizaci\u00F3n de servicio'
                                        ,'smap2.pv_cdtiptra_i' : _AUTORIZACION_SERVICIO
                                    }
                                });
                            });
                        }
					}
				}
				else{
					centrarVentanaInterna(Ext.Msg.show({
						title:'Error',
						msg: 'Error al modificar los registros',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					}));
					respuesta= false;
				}
			},
			failure:function(response,opts){
				panelInicialPrincipal.setLoading(false);
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			respuesta=false;
			}
		});
		return respuesta;
	}
	
    function rechazoAutorizacionServicio(){
    	Ext.Ajax.request({
            url     : _URL_CONSULTA_CLAUSU
            ,params : {
                'params.cdclausu' : null,
                'params.dsclausu' : 'CARTA RECHAZO AUT SERV'
            }
            ,success : function (response){
                var json=Ext.decode(response.responseText);
                var claveClausula = json.listaGenerica[0].key;
                Ext.Ajax.request({
                    url     : _URL_CONSULTA_CLAUSU_DETALLE
                    ,params : {
                        'params.cdclausu'  : claveClausula
                    }
                    ,success : function (response){
                        var json=Ext.decode(response.responseText);
                        txtContenido =json.msgResult;
                        windowLoader = Ext.create('Ext.window.Window',{
                            modal       : true,
                            buttonAlign : 'center',
                            width       : 663,
                            height      : 460,
                            autoScroll  : true,
                            items       : [
                                Ext.create('Ext.form.Panel', {
                                    title: 'Rechazar autorizaci&oacute;n de servicio',
                                    width: 650,
                                    url: _URL_ActualizaStatusTramite,
                                    bodyPadding: 5,
                                    items: [
                                        Ext.create('Ext.form.field.TextArea', {
                                            id        : 'inputTextareaCommentsToRechazo'
                                            ,width  : 570
                                            ,height : 200
                                            ,value  : txtContenido
                                        }),
                                        Ext.create('Ext.form.field.TextArea', {
                                            id        : 'inputTextareaComments'
                                            ,width  : 570
                                            ,height : 100
                                        })
                                        ,{
                                            xtype       : 'radiogroup'
                                            ,fieldLabel : 'Mostrar al agente'
                                            ,columns    : 2
                                            ,width      : 250
                                            ,style      : 'margin:5px;'
                                            ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                                            ,items      :
                                            [
                                                {
                                                    boxLabel    : 'Si'
                                                    ,itemId     : 'SWAGENTE2'
                                                    ,name       : 'SWAGENTE2'
                                                    ,inputValue : 'S'
                                                    ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                                                }
                                                ,{
                                                    boxLabel    : 'No'
                                                    ,name       : 'SWAGENTE2'
                                                    ,inputValue : 'N'
                                                    ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                                                }
                                            ]
                                        }
                                        ],
                                    buttonAlign:'center',
                                    buttons: [{
                                        text: 'Rechazar',
                                        icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                                        buttonAlign : 'center',
                                        handler: function() {
                                            if (this.up().up().form.isValid()) {
                                                this.up().up().form.submit({
                                                    waitMsg:'Procesando...',
                                                    params: {
                                                        'smap1.ntramite' : valorAction.ntramite,
                                                        'smap1.status'   : _STATUS_TRAMITE_RECHAZADO,
                                                        'smap1.comments' : Ext.getCmp('inputTextareaComments').getValue(),
                                                        'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
                                                    },
                                                    failure: function(form, action) {
                                                        mensajeError('No se pudo rechazar.');
                                                    },
                                                    success: function(form, action) {
                                                        Ext.Ajax.request({
                                                            url     : compleUrlGuardarCartoRechazo
                                                            ,method:'GET'
                                                            ,params :{
                                                                'map1.ntramite'  : valorAction.ntramite
                                                                ,'map1.comments' : Ext.getCmp('inputTextareaCommentsToRechazo').getValue()
                                                                ,'map1.cdsisrol' : 'MEDICO'
                                                                ,'map1.cdunieco' : Ext.getCmp('idUnieco').getValue()
                                                                ,'map1.cdramo'   : Ext.getCmp('idcdRamo').getValue()
                                                                ,'map1.estado'   : Ext.getCmp('idEstado').getValue()
                                                                ,'map1.nmpoliza' : Ext.getCmp('polizaAfectada').getValue()
                                                                ,'map1.nmsuplem' : Ext.getCmp('idNmsuplem').getValue()
                                                            }
                                                            ,success : function(response){
                                                                // YA NO REALIZA NADA PORQUE YA SE GENERO EL ARCHIVO
                                                            }
                                                            ,failure : function(){
                                                                Ext.Msg.show({
                                                                    title:'Error',
                                                                    msg: 'Error de comunicaci&oacute;n',
                                                                    buttons: Ext.Msg.OK,
                                                                    icon: Ext.Msg.ERROR
                                                                });
                                                            }
                                                        });
                                                        Ext.Ajax.request({
                                                            url     : _URL_ActualizaStatusMAUTSERV
                                                            ,params:{
                                                                'params.nmautser' :  valorAction.nmAutSer,
                                                                'params.status'   : _STATUS_TRAMITE_RECHAZADO
                                                            }
                                                            ,success : function (response){
                                                                mensajeCorrecto('Aviso','Se ha rechazado con &eacute;xito.',function(){
                                                                Ext.create('Ext.form.Panel').submit({
                                                                    url             : _p12_urlMesaControl
                                                                    ,standardSubmit : true
                                                                    ,params         : {
                                                                        'smap1.gridTitle'      : 'Autorizaci\u00F3n de servicio'
                                                                        ,'smap2.pv_cdtiptra_i' : _AUTORIZACION_SERVICIO
                                                                    }
                                                                });
                                                            });
                                                            },
                                                            failure : function (){
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
                                                });
                                            } else {
                                                Ext.Msg.show({
                                                    title: 'Aviso',
                                                    msg: 'Complete la informaci&oacute;n requerida',
                                                    buttons: Ext.Msg.OK,
                                                    icon: Ext.Msg.WARNING
                                                });
                                            }
                                        }
                                    },{
                                        text: 'Cancelar',
                                        icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                                        buttonAlign : 'center',
                                        handler: function() {
                                            windowLoader.close();
                                            Ext.getCmp('Autorizar').hide();
                                            //Ext.getCmp('autorizar').setDisabled(false);
                                        }
                                    }
                                ]
                            })  
                        ]
                        }).show();
                        centrarVentana(windowLoader);
                    },
                    failure : function (){
                        Ext.Msg.show({
                            title:'Error',
                            msg: 'Error de comunicaci&oacute;n',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                        });
                    }
                });
            },
            failure : function (){
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
    }
});