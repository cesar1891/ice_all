Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]); 
Ext.onReady(function() {
    Ext.Ajax.timeout = 5*60*1000;
    var valorIndexSeleccionado= null;
    var banderaFactura = "0";
    var banderaAsegurado = "0";
    var retornaMC = "0";
    var facturaTemporal =null;
    var _11_aseguradoSeleccionado = null;
    var origProd = null;	// para guardar el producto original, x validaci�n de que no se modifique (EGS)
    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true  
    });

/*******    MODELOS     *******/
    Ext.define('modeloRechazos',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'key'},    {type:'string',    name:'value'}]
    });
    
    Ext.define('modeloIncisosRechazos',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'key'},    {type:'string',    name:'value'}]
    });
    
    Ext.define('modelFacturaSiniestro', {
        extend:'Ext.data.Model',
        fields:[
            {type:'string',     name:'noFactura'},          {type:'date',       name:'fechaFactura'},       {type:'string',     name:'tipoServicio'},
            {type:'string',     name:'tipoServicioName'},   {type:'string',     name:'proveedor'},          {type:'string',     name:'proveedorName'},
            {type:'string',     name:'importe'},            {type:'string',     name:'tipoMoneda'},         {type:'string',     name:'tipoMonedaName'},
            {type:'string',     name:'tasaCambio'},         {type:'string',     name:'importeFactura'},     {type:'string',     name:'telefono'},
            {type:'string',     name:'email'}
        ]
    });
    
    Ext.define('modelListadoProvMedico',{
        extend: 'Ext.data.Model',
            fields: [
                    {type:'string',     name:'cdpresta'},   {type:'string', name:'nombre'},     {type:'string', name:'cdespeci'},
                    {type:'string',     name:'descesp'}
        ]
    });
    
    Ext.define('modelListadoPoliza',{
        extend: 'Ext.data.Model',
        fields: [   {type:'string',     name:'cdramo'},             {type:'string',     name:'cdunieco'},               {type:'string',     name:'estado'},
                    {type:'string',     name:'nmpoliza'},           {type:'string',     name:'nmsituac'},               {type:'string',     name:'mtoBase'},
                    {type:'string',     name:'feinicio'},           {type:'string',     name:'fefinal'},                {type:'string',     name:'dssucursal'},
                    {type:'string',     name:'dsramo'},             {type:'string',     name:'estatus'},                {type:'string',     name:'dsestatus'},
                    {type:'string',     name:'nmsolici'},           {type:'string',     name:'nmsuplem'},               {type:'string',     name:'cdtipsit'},
                    {type:'string',     name:'dsestatus'},          {type:'string',     name:'vigenciaPoliza'},         {type:'string',     name:'faltaAsegurado'},
                    {type:'string',     name:'fcancelacionAfiliado'},{type:'string',    name:'desEstatusCliente'},      {type:'string',     name:'numPoliza'},
                    {type:'string',     name:'nombAsegurado'},      {type:'string',     name:'telefono'},               {type:'string',     name:'email'}
        ]
    });
    
    Ext.define('modelListAsegPagDirecto',{
        extend: 'Ext.data.Model',
        fields: [
                    {type:'string',     name:'modUnieco'},          {type:'string',     name:'modEstado'},              {type:'string',     name:'modRamo'},
                    {type:'string',     name:'modNmsituac'},        {type:'string',     name:'modPolizaAfectada'},      {type:'string',     name:'modCdpersondesc'},
                    {type:'string',     name:'modNmsolici'},        {type:'string',     name:'modNmsuplem'},            {type:'string',     name:'modCdtipsit'},
                    {type:'string',     name:'modNmautserv'},       {type:'Date',       name:'modFechaOcurrencia', dateFormat:'d/m/Y'},     {type:'string',     name:'modCdperson'},
                    {type:'string',     name:'modnumPoliza'},       {type:'string',     name:'modFactura'},             {type:'string',     name:'modTelefono'},
                    {type:'string',     name:'modEmail'},           {type:'string',     name:'modtxtAutEspecial'}
                ]
    });
    
/*******        STORE       *******/
    var storeIncisosRechazos = Ext.create('Ext.data.JsonStore', {
        model:'modeloIncisosRechazos',
        proxy: {
            type: 'ajax',
            url: _URL_ListaIncisosRechazos,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    
    var storeRechazos = Ext.create('Ext.data.JsonStore', {
        model:'modeloRechazos',
        proxy: {
            type: 'ajax',
            url: _URL_ListaRechazos,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    storeRechazos.load();
    
    oficinaReceptora = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url:_URL_CATALOGOS,
            extraParams : {
                catalogo:_CATALOGO_OFICINA_RECEP,
                'params.idPadre' : '1000'
            },
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });

    var storeTipoMoneda = Ext.create('Ext.data.JsonStore', {
        model:'Generic',
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            extraParams : {catalogo:_CATALOGO_TipoMoneda},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    storeTipoMoneda.load();

    oficinaEmisora = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url:_URL_CATALOGOS,
            extraParams : {
                catalogo:_CATALOGO_OFICINA_RECEP,
                'params.idPadre' : '1000'
            },
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });

    var storeFacturaDirecto =new Ext.data.Store({
        autoDestroy: true,          model: 'modelFacturaSiniestro'
    });
    
    var storeFacturaReembolso =new Ext.data.Store({
        autoDestroy: true,          model: 'modelFacturaSiniestro'
    });

    var storePagoIndemnizatorio =new Ext.data.Store({
        autoDestroy: true,
        model: 'modelFacturaSiniestro'
    });

    var storePagoIndemnizatorioRecupera =new Ext.data.Store({
        autoDestroy: true,
        model: 'modelFacturaSiniestro'
    });
    
    var storeListAsegPagDirecto = new Ext.data.Store({
        autoDestroy: true,
        model: 'modelListAsegPagDirecto',
        proxy: {
            type: 'ajax',
            url: _URL_ASEGURADO_FACTURA,
            reader: {
                type: 'json',
                root: 'slist1'
            }
        }
    });
    
    var storeTipoAtencion = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url:_UR_TIPO_ATENCION,
            reader: {
                type: 'json',
                root: 'listaTipoAtencion'
            }
        }
    });

    var storeAsegurados = Ext.create('Ext.data.Store', {
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
    
    var storeAsegurados2 = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_LISTADO_ASEGURADO_POLIZA,
            reader: {
                type: 'json',
                root: 'listaAsegurado'
            }
        }
    });

    var storeTipoPago = Ext.create('Ext.data.JsonStore', {
        model:'Generic',
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            extraParams : {catalogo:_CATALOGO_TipoPago},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    
    var storeProveedorReembolsoRender = Ext.create('Ext.data.Store',{
        model:'modelListadoProvMedico',
        autoLoad:true,
        cargado:false,
        proxy: {
            type: 'ajax',
            url : _URL_CATALOGOS,
            extraParams:{
                catalogo         : _CATALOGO_PROVEEDORES,
                catalogoGenerico : true
            },
            reader: {
                type: 'json',
                root: 'listaGenerica'
            }
        }
        ,listeners: {
            load : function() {
                this.cargado=true;
                if(!Ext.isEmpty(gridFacturaReembolso)) {
                    gridFacturaReembolso.getView().refresh();
                }
            }
        }
    });
    
    var storeProveedorIndemnizatorioRender = Ext.create('Ext.data.Store',{
        model:'modelListadoProvMedico',
        autoLoad:true,
        cargado:false,
        proxy: {
            type: 'ajax',
            url : _URL_CATALOGOS,
            extraParams:{
                catalogo         : _CATALOGO_PROVEEDORES,
                catalogoGenerico : true
            },
            reader: {
                type: 'json',
                root: 'listaGenerica'
            }
        }
        ,listeners: {
            load : function() {
                this.cargado=true;
                if(!Ext.isEmpty(gridPagoIndemnizatorio)) {
                    gridPagoIndemnizatorio.getView().refresh();
                }
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
                catalogo         : _CATALOGO_PROVEEDORES,
                catalogoGenerico : true
            },
            reader: {
                type: 'json',
                root: 'listaGenerica'
            }
        }
    });

    var storeListadoPoliza = new Ext.data.Store({
        pageSize    : 5
        ,model      : 'modelListadoPoliza'
        ,autoLoad   : false
        ,proxy      : {
            enablePaging    : true,
            reader          : 'json',
            type            : 'memory',
            data            : []
        }
    });

    storeRamos = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            extraParams : {catalogo:_CAT_RAMO_SALUD},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    storeRamos.load();
    
    storeModalidad = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url: _URL_CATALOGOS,
            extraParams : {catalogo:_CAT_MODALIDADES},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });

/*******        DECLARACION DE COMBOX Y LABEL       *******/
    cmbRamos = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel   : 'Producto',          allowBlank     : false,             editable: false,
        displayField : 'value',             valueField: 'key',                  forceSelection : false,
        width        : 350,                 queryMode :'local',                 name           :'cmbRamos'
        ,store : storeRamos
        ,listeners: {	// (EGS) agrego listener
        	'select' : function(e){
        		validaProducto();
        	}
        }
    });
    
    cmbModalidad = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel   : 'Modalidad',         allowBlank     : false,     editable   : false,
        displayField: 'value',              valueField: 'key',          forceSelection : false,             queryMode :'local',
        width: 350,                         name:'pv_cdtipsit_i',       store          : storeModalidad,
        listeners : {
            'select':function(e){
                panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(null);
                storeTipoAtencion.load({
                    params:{
                        'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                        'params.cdtipsit': panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                        'params.tipoPago':panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                    }
                });
            },
            'select':function(e){
                panelInicialPral.down('combo[name=cmbTipoPago]').setValue('');
            }
        }
    })
    
    var tipoPago= Ext.create('Ext.form.ComboBox',{
        name:'cmbTipoPago',                 fieldLabel: 'Tipo pago',            queryMode:'local',
        displayField: 'value',              valueField: 'key',                  allowBlank:false,
        editable:false,                     width        : 350,                 emptyText:'Seleccione...',
        store: storeTipoPago,
        listeners : {
            'select':function(e){
                panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(null);
                
                storeTipoAtencion.load({
                    params:{
                        'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                        'params.cdtipsit': panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                        'params.tipoPago':panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                    }
                });
                if(e.getValue() == _TIPO_PAGO_DIRECTO){
                    limpiarRegistrosTipoPago(e.getValue());
                    panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");                   
                }else{
                    Ext.Ajax.request({
                        url     : _URL_VAL_CAUSASINI
                        ,params : {
                            'params.cdramo'   : panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                            'params.cdtipsit' : panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                            'params.causaSini': 'IDBENEFI',
                            'params.cveCausa' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                        }
                        ,success : function (response){
                            var datosExtras = Ext.decode(response.responseText);
                            if(Ext.decode(response.responseText).datosInformacionAdicional != null){
                                var cveCauSini=Ext.decode(response.responseText).datosInformacionAdicional[0];
                                
                                if(cveCauSini.REQVALIDACION =="S"){
                                    //Visualizamos el campo
                                    panelInicialPral.down('[name=idCveBeneficiario]').show();
                                    panelInicialPral.down('[name=idCveBeneficiario]').setValue('');
                                }else{
                                    //ocultamos el campo
                                    panelInicialPral.down('[name=idCveBeneficiario]').setValue('0');
                                    panelInicialPral.down('[name=idCveBeneficiario]').hide();
                                }
                                
                                limpiarRegistrosTipoPago(e.getValue());
                                if(panelInicialPral.down('combo[name=cmbOficReceptora]').getValue() == "1104"){
                                    panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1104");
                                }else{
                                    panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
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

    var comboTipoAte = Ext.create('Ext.form.ComboBox',{
        name:'cmbTipoAtencion',         fieldLabel: 'Tipo atenci&oacute;n',     allowBlank : false,     editable:true,
        displayField: 'value',          emptyText:'Seleccione...',              valueField: 'key',      forceSelection : true,
        width        : 350,             queryMode:'local',              store: storeTipoAtencion
    });
    
    var cmbOficinaReceptora = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel  : 'Oficina receptora',  name            :'cmbOficReceptora',        allowBlank      : false,
        editable    : true,                 displayField    :'value',                   width           : 350,
        emptyText   :'Seleccione...',       valueField      :'key',                     forceSelection  : true,
        queryMode   :'local',               store           :oficinaReceptora
    });
   
    var cmbOficinaEmisora = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel : 'Oficina emisora',     name      : 'cmbOficEmisora',
        allowBlank : false,                 editable   : true,                  displayField : 'value',
        width        : 350,                 emptyText:'Seleccione...',          valueField   : 'key',
        forceSelection : true,              queryMode      :'local',            store : oficinaEmisora
    });
    
    aseguradoAfectado = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel : 'Asegurado afectado',  displayField : 'value',             name:'cmbAseguradoAfectado',
        width        : 350,                 valueField   : 'key',               queryMode :'remote',
        forceSelection : true,              matchFieldWidth: false,             queryParam: 'params.cdperson',
        minChars  : 2,                      store : storeAsegurados,            triggerAction: 'all',
        hideTrigger:true,                   allowBlank:false,
        listeners : {
            'select' : function(combo, record) {
                obtieneCDPerson = this.getValue();
                panelInicialPral.down('[name=idnombreAsegurado]').setValue(aseguradoAfectado.rawValue);
                var params = {
                        'params.cdperson'   :   obtieneCDPerson,
                        'params.cdramo'     :   panelInicialPral.down('combo[name=cmbRamos]').getValue()
                };
                cargaStorePaginadoLocal(storeListadoPoliza, _URL_CONSULTA_LISTADO_POLIZA, 'listaPoliza', params, function(options, success, response){
                    if(success){
                        var jsonResponse = Ext.decode(response.responseText);
                        if(jsonResponse.listaPoliza == null) {
                            Ext.Msg.show({
                                title: 'Aviso',
                                msg: 'No existen p&oacute;lizas para el asegurado elegido.',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
                            modPolizasAltaTramite.hide();
                            return;
                        }
                    }else{
                        Ext.Msg.show({
                            title: 'Aviso',
                            msg: 'Error al obtener los datos.',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                        });
                    }
                });
                modPolizasAltaTramite.show();
            }
        }
    });
    
    cmbAseguradosPagDirecto = Ext.create('Ext.form.field.ComboBox', {
        displayField : 'value',             name:'cmbAseguradoPagoDirecto',     valueField   : 'key',
        forceSelection : true,              matchFieldWidth: false,             queryMode :'remote',
        queryParam: 'params.cdperson',      minChars  : 2,                      store : storeAsegurados,
        triggerAction: 'all',               hideTrigger:true,                   allowBlank:false,
        listeners : {
            'select' : function(combo, record) {
                obtieneCDPerson = this.getValue();
                var params = {
                        'params.cdperson'   :   this.getValue(),
                        'params.cdramo'     :   panelInicialPral.down('combo[name=cmbRamos]').getValue()
                };
                cargaStorePaginadoLocal(storeListadoPoliza, _URL_CONSULTA_LISTADO_POLIZA, 'listaPoliza', params, function(options, success, response){
                    if(success){
                        var jsonResponse = Ext.decode(response.responseText);
                        if(jsonResponse.listaPoliza == null) {
                            Ext.Msg.show({
                                title: 'Aviso',
                                msg: 'No existen p&oacute;lizas para el asegurado elegido.',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING,
                                fn: function() {
                                    valorIndexSeleccionado.set('modCdperson','');
                                    valorIndexSeleccionado.set('modCdpersondesc','');
                                }
                            });
                            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
                            modPolizasAltaTramite.hide();
                            return;
                        }
                    }else{
                        Ext.Msg.show({
                            title: 'Aviso',
                            msg: 'Error al obtener los datos.',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                            });
                    }
                });
                modPolizasAltaTramite.show();
            }
        }
    });
    
    /* PANEL PARA LA BUSQUEDA DE LA INFORMACION DEL ASEGURADO PARA LA BUSQUEDA DE LAS POLIZAS */
    gridPolizasAltaTramite= Ext.create('Ext.grid.Panel', {
        id          :   'polizaGridAltaTramite',
        store       :   storeListadoPoliza,
        selType     :   'checkboxmodel',
        width       :   700,
        height      :   200,
        columns     :
        [
            {   header      : 'N&uacute;mero de P&oacute;liza',                                         dataIndex : 'numPoliza',        width       : 200       },
            {   header      : 'Estatus p&oacute;liza ',                                                 dataIndex : 'dsestatus',        width       : 100       },
            {   header      : 'Vigencia p&oacute;liza <br/> Fecha inicio \t\t  |  \t\t Fecha fin  ',    dataIndex : 'vigenciaPoliza',   width       : 200       },
            {   header      : 'Fecha alta <br/> asegurado',                                             dataIndex : 'faltaAsegurado',   width       : 100       },
            {   header      : 'Fecha cancelaci&oacute;n <br/> asegurado',                               dataIndex : 'fcancelacionAfiliado', width   : 150       },
            {   header      : 'Estatus<br/> asegurado',                                                 dataIndex : 'desEstatusCliente',width       : 100       },
            {   header      : 'Producto',                                                               dataIndex : 'dsramo',           width       : 150       },
            {   header          : 'Sucursal',                                                           dataIndex : 'dssucursal',       width       : 150       },
            {   header      : 'Estado',                                                                 dataIndex : 'estado',           width       : 100       },
            {   header      : 'N&uacute;mero de Situaci&oacute;n',                                      dataIndex : 'nmsituac',         width       : 150       }
        ],
        bbar : {
            displayInfo : true,
            store       : storeListadoPoliza,
            xtype       : 'pagingtoolbar'
        },
        listeners: {
            itemclick: function(dv, record, item, index, e){
                //1.- Validamos que el asegurado este vigente
            	debug("Valor del Record de la poliza ==> ",record);
                if(record.get('desEstatusCliente')=="Vigente"){
                    var valorFechaOcurrencia;
                    if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
                        valorFechaOcurrencia = new Date(valorIndexSeleccionado.get('modFechaOcurrencia'));
                    }else{
                        valorFechaOcurrencia = panelInicialPral.down('[name=dtFechaOcurrencia]').getValue();
                    }
                    var valorFechaInicial = new Date(record.get('feinicio').substring(6,10)+"/"+record.get('feinicio').substring(3,5)+"/"+record.get('feinicio').substring(0,2));
                    var valorFechaFinal =   new Date(record.get('fefinal').substring(6,10)+"/"+record.get('fefinal').substring(3,5)+"/"+record.get('fefinal').substring(0,2));
                    var valorFechaAltaAsegurado = new Date(record.get('faltaAsegurado').substring(6,10)+"/"+record.get('faltaAsegurado').substring(3,5)+"/"+record.get('faltaAsegurado').substring(0,2));
                    var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                    var noFactura= rowSelected.get('noFactura');
                    
                    Ext.Ajax.request({
	                    url     : _URL_VALIDA_STATUSASEG
	                    ,params:{
	                        'params.cdperson'  : valorIndexSeleccionado.get('modCdperson'),
	                        'params.feoocurre' : valorFechaOcurrencia,
	                        'params.nmpoliza'  : record.get('nmpoliza')
	                    }
	                    ,success : function (response) {
	                        json = Ext.decode(response.responseText);
	                        debug("Valor de respuesta  ===> ",json);
	                        
	                        if(Ext.decode(response.responseText).validacionGeneral =="V"){
			                    if( (valorFechaOcurrencia <= valorFechaFinal) && (valorFechaOcurrencia >= valorFechaInicial)){
			                        if( valorFechaOcurrencia >= valorFechaAltaAsegurado ){
			                            //cumple la condicion la fecha de ocurrencia es menor igual a la fecha de alta de tramite
			                            panelInicialPral.down('[name="cdunieco"]').setValue(record.get('cdunieco'));
			                            panelInicialPral.down('[name="estado"]').setValue(record.get('estado'));
			                            panelInicialPral.down('[name="cdramo"]').setValue(record.get('cdramo'));
			                            panelInicialPral.down('[name="nmsituac"]').setValue(record.get('nmsituac'));
			                            panelInicialPral.down('[name="polizaAfectada"]').setValue(record.get('nmpoliza'));
			                            panelInicialPral.down('[name="idNmsolici"]').setValue(record.get('nmsolici'));
			                            panelInicialPral.down('[name="idNmsuplem"]').setValue(record.get('nmsuplem'));
			                            panelInicialPral.down('[name="idCdtipsit"]').setValue(record.get('cdtipsit'));
			                            panelInicialPral.down('[name="idNumPolizaInt"]').setValue(record.get('numPoliza'));
			                            panelInicialPral.down('[name="txtTelefono"]').setValue(record.get('telefono'));
			                            panelInicialPral.down('[name="txtEmail"]').setValue(record.get('email'));
			                            // realizamos la asignacion de las variables JOSE
			                            
			                            valorIndexSeleccionado.set('modUnieco',record.get('cdunieco'));
			                            valorIndexSeleccionado.set('modEstado',record.get('estado'));
			                            valorIndexSeleccionado.set('modRamo',record.get('cdramo'));
			                            valorIndexSeleccionado.set('modNmsituac',record.get('nmsituac'));
			                            valorIndexSeleccionado.set('polizaAfectada',record.get('nmpoliza'));
			                            valorIndexSeleccionado.set('modNmsolici',record.get('nmsolici'));
			                            valorIndexSeleccionado.set('modNmsuplem',record.get('nmsuplem'));
			                            valorIndexSeleccionado.set('modCdtipsit',record.get('cdtipsit'));
			                            valorIndexSeleccionado.set('modNmautserv',null);
			                            valorIndexSeleccionado.set('modCdpersondesc',record.get('nombAsegurado'));
			                            valorIndexSeleccionado.set('modnumPoliza',record.get('numPoliza'));
			                            valorIndexSeleccionado.set('modTelefono',record.get('telefono'));
			                            valorIndexSeleccionado.set('modEmail',record.get('email'));
			                            valorIndexSeleccionado.set('modFactura',noFactura);
			                            valorIndexSeleccionado.set('modPolizaAfectada',record.get('nmpoliza'));
			                            valorIndexSeleccionado.set('modtxtAutEspecial',"0");
			                            banderaAsegurado = "1";
			                            limpiarRegistros();
			                            modPolizasAltaTramite.hide();
			                        }else{
			                            // No se cumple la condicion la fecha de ocurrencia es mayor a la fecha de alta de tramite
			                            Ext.Msg.show({
			                                title:'Error',
			                                msg: 'La fecha de ocurrencia es mayor a la fecha de alta del asegurado',
			                                buttons: Ext.Msg.OK,
			                                icon: Ext.Msg.ERROR,
			                                fn: function() {
			                                    valorIndexSeleccionado.set('modCdperson','');
			                                    valorIndexSeleccionado.set('modCdpersondesc','');
			                                }
			                            });
			                            modPolizasAltaTramite.hide();
			                            limpiarRegistros();
			                            if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
			                                panelListadoAsegurado.query('combo[name=cmbAseguradoAfect]')[0].setValue('');
			                            }else{
			                                panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
			                            }
			                        }
			                    }else{
			                    // La fecha de ocurrencia no se encuentra en el rango de la poliza vigente
			                        centrarVentanaInterna(Ext.Msg.show({
			                            title: 'Aviso',
			                            msg: 'La fecha de ocurrencia se encuentra fuera del rango de la p&oacute;liza vigente.<br/> &iquest;Desea continuar ?',
			                            buttons: Ext.Msg.YESNO,
			                            icon: Ext.Msg.QUESTION,
			                            fn: function(buttonId, text, opt){
			                                if(buttonId == 'yes'){
			                                    panelInicialPral.down('[name="cdunieco"]').setValue(record.get('cdunieco'));
			                                    panelInicialPral.down('[name="estado"]').setValue(record.get('estado'));
			                                    panelInicialPral.down('[name="cdramo"]').setValue(record.get('cdramo'));
			                                    panelInicialPral.down('[name="nmsituac"]').setValue(record.get('nmsituac'));
			                                    panelInicialPral.down('[name="polizaAfectada"]').setValue(record.get('nmpoliza'));
			                                    panelInicialPral.down('[name="idNmsolici"]').setValue(record.get('nmsolici'));
			                                    panelInicialPral.down('[name="idNmsuplem"]').setValue(record.get('nmsuplem'));
			                                    panelInicialPral.down('[name="idCdtipsit"]').setValue(record.get('cdtipsit'));
			                                    panelInicialPral.down('[name="idNumPolizaInt"]').setValue(record.get('numPoliza'));
			                                    panelInicialPral.down('[name="txtTelefono"]').setValue(record.get('telefono'));
			                                    panelInicialPral.down('[name="txtEmail"]').setValue(record.get('email'));
			                                    // realizamos la asignacion de las variables JOSE
			                                    valorIndexSeleccionado.set('modUnieco',record.get('cdunieco'));
			                                    valorIndexSeleccionado.set('modEstado',record.get('estado'));
			                                    valorIndexSeleccionado.set('modRamo',record.get('cdramo'));
			                                    valorIndexSeleccionado.set('modNmsituac',record.get('nmsituac'));
			                                    valorIndexSeleccionado.set('polizaAfectada',record.get('nmpoliza'));
			                                    valorIndexSeleccionado.set('modNmsolici',record.get('nmsolici'));
			                                    valorIndexSeleccionado.set('modNmsuplem',record.get('nmsuplem'));
			                                    valorIndexSeleccionado.set('modCdtipsit',record.get('cdtipsit'));
			                                    valorIndexSeleccionado.set('modNmautserv',null);
			                                    valorIndexSeleccionado.set('modCdpersondesc',record.get('nombAsegurado'));
			                                    valorIndexSeleccionado.set('modnumPoliza',record.get('numPoliza'));
			                                    valorIndexSeleccionado.set('modTelefono',record.get('telefono'));
			                                    valorIndexSeleccionado.set('modEmail',record.get('email'));
			                                    valorIndexSeleccionado.set('modFactura',noFactura);
			                                    valorIndexSeleccionado.set('modPolizaAfectada',record.get('nmpoliza'));
			                                    valorIndexSeleccionado.set('modtxtAutEspecial',"1");
			                                    banderaAsegurado = "1";
			                                    limpiarRegistros();
			                                    modPolizasAltaTramite.hide();
			                                }else{
			                                    valorIndexSeleccionado.set('modCdperson','');
			                                    valorIndexSeleccionado.set('modCdpersondesc','');
			                                    modPolizasAltaTramite.hide();
			                                    limpiarRegistros();
			                                    if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
			                                        panelListadoAsegurado.query('combo[name=cmbAseguradoAfect]')[0].setValue('');
			                                    }else{
			                                        panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
			                                    }
			                                }
			                                
			                            }
			                        }));
		                    	}
	                        }else{
		                        centrarVentanaInterna(Ext.Msg.show({
		                            title: 'Aviso',
		                            msg: 'El asegurado de la p&oacute;liza seleccionado no se encuentra vigente.<br/> &iquest;Desea continuar ?',
		                            buttons: Ext.Msg.YESNO,
		                            icon: Ext.Msg.QUESTION,
		                            fn: function(buttonId, text, opt){
		                                if(buttonId == 'yes'){
		                                    panelInicialPral.down('[name="cdunieco"]').setValue(record.get('cdunieco'));
		                                    panelInicialPral.down('[name="estado"]').setValue(record.get('estado'));
		                                    panelInicialPral.down('[name="cdramo"]').setValue(record.get('cdramo'));
		                                    panelInicialPral.down('[name="nmsituac"]').setValue(record.get('nmsituac'));
		                                    panelInicialPral.down('[name="polizaAfectada"]').setValue(record.get('nmpoliza'));
		                                    panelInicialPral.down('[name="idNmsolici"]').setValue(record.get('nmsolici'));
		                                    panelInicialPral.down('[name="idNmsuplem"]').setValue(record.get('nmsuplem'));
		                                    panelInicialPral.down('[name="idCdtipsit"]').setValue(record.get('cdtipsit'));
		                                    panelInicialPral.down('[name="idNumPolizaInt"]').setValue(record.get('numPoliza'));
		                                    panelInicialPral.down('[name="txtTelefono"]').setValue(record.get('telefono'));
		                                    panelInicialPral.down('[name="txtEmail"]').setValue(record.get('email'));
		                                    // realizamos la asignacion de las variables JOSE
		                                    valorIndexSeleccionado.set('modUnieco',record.get('cdunieco'));
		                                    valorIndexSeleccionado.set('modEstado',record.get('estado'));
		                                    valorIndexSeleccionado.set('modRamo',record.get('cdramo'));
		                                    valorIndexSeleccionado.set('modNmsituac',record.get('nmsituac'));
		                                    valorIndexSeleccionado.set('polizaAfectada',record.get('nmpoliza'));
		                                    valorIndexSeleccionado.set('modNmsolici',record.get('nmsolici'));
		                                    valorIndexSeleccionado.set('modNmsuplem',record.get('nmsuplem'));
		                                    valorIndexSeleccionado.set('modCdtipsit',record.get('cdtipsit'));
		                                    valorIndexSeleccionado.set('modNmautserv',null);
		                                    valorIndexSeleccionado.set('modCdpersondesc',record.get('nombAsegurado'));
		                                    valorIndexSeleccionado.set('modnumPoliza',record.get('numPoliza'));
		                                    valorIndexSeleccionado.set('modTelefono',record.get('telefono'));
		                                    valorIndexSeleccionado.set('modEmail',record.get('email'));
		                                    valorIndexSeleccionado.set('modFactura',noFactura);
		                                    valorIndexSeleccionado.set('modPolizaAfectada',record.get('nmpoliza'));
		                                    valorIndexSeleccionado.set('modtxtAutEspecial',"1");
		                                    banderaAsegurado = "1";
		                                    limpiarRegistros();
		                                    modPolizasAltaTramite.hide();
		                                }else{
		                                    valorIndexSeleccionado.set('modCdperson','');
		                                    valorIndexSeleccionado.set('modCdpersondesc','');
		                                    modPolizasAltaTramite.hide();
		                                    limpiarRegistros();
		                                    if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
		                                        panelListadoAsegurado.query('combo[name=cmbAseguradoAfect]')[0].setValue('');
		                                    }else{
		                                        panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
		                                    }
		                                }
		                                
		                            }
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
                }else{
                    // El asegurado no se encuentra vigente
                    Ext.Msg.show({
                        title:'Error',
                        msg: 'El asegurado de la p&oacute;liza seleccionado no se encuentra vigente',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR,
                        fn: function() {
                            valorIndexSeleccionado.set('modCdperson','');
                            valorIndexSeleccionado.set('modCdpersondesc','');
                        }
                    });
                    modPolizasAltaTramite.hide();
                    limpiarRegistros();
                    if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
                        panelListadoAsegurado.query('combo[name=cmbAseguradoAfect]')[0].setValue('');
                    }else{
                        panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
                    }
                }
            }
        }
    });
    gridPolizasAltaTramite.store.sort([
        {
            property    : 'nmpoliza',           direction   : 'DESC'
        }
    ]);

    var modPolizasAltaTramite = Ext.create('Ext.window.Window', {
        title           : 'Listado de P&oacute;liza'
        ,modal          : true
        ,resizable      : false
        ,buttonAlign    : 'center'
        ,closable       : false
        ,width          : 710
        ,minHeight      : 100 
        ,maxheight      : 400
        ,items          :
        [
            gridPolizasAltaTramite
        ]
    });

    var cmbBeneficiario= Ext.create('Ext.form.ComboBox',{
        name:'cmbBeneficiario',         fieldLabel: 'Beneficiario',         queryMode: 'local'/*'remote'*/,         displayField: 'value',
        valueField: 'key',              editable:true,                      forceSelection : true,      matchFieldWidth: false,
        queryParam: 'params.cdperson',  minChars  : 2,                      store : storeAsegurados2,   triggerAction: 'all',
        width        : 350,             allowBlank:false,
        listeners : {
            'select' : function(e) {
                panelInicialPral.down('[name=idnombreBeneficiarioProv]').setValue(cmbBeneficiario.rawValue);
                // Realizamos la validacion si es menor de edad
                Ext.Ajax.request({
                    url     : _URL_CONSULTA_BENEFICIARIO
                    ,params:{
                        'params.cdunieco'  : panelInicialPral.down('[name="cdunieco"]').getValue(),
                        'params.cdramo'    : panelInicialPral.down('[name="cdramo"]').getValue(),
                        'params.estado'    : panelInicialPral.down('[name="estado"]').getValue(),
                        'params.nmpoliza'  : panelInicialPral.down('[name="polizaAfectada"]').getValue(),
                        'params.nmsuplem'  : panelInicialPral.down('[name="idNmsuplem"]').getValue(),
                        'params.cdperson'  : e.getValue()
                    }
                    ,success : function (response) {
                        json = Ext.decode(response.responseText);
                        if(json.success==false){
                            Ext.Msg.show({
                                title:'Beneficiario',
                                msg: json.mensaje,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                            panelInicialPral.down('combo[name=cmbBeneficiario]').setValue('')
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

    cmbProveedor = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel : 'Proveedor',           displayField : 'nombre',            name:'cmbProveedor',
        width        : 350,                 valueField   : 'cdpresta',          forceSelection : true,
        matchFieldWidth: false,             queryMode :'remote',                queryParam: 'params.cdpresta',
        minChars  : 2,                      store : storeProveedor,             triggerAction: 'all',
        hideTrigger:true,                   allowBlank:false,                   colspan:2,
        listeners : {
            'select' : function(combo, record) {
                if(this.getValue() =='0'){
                    panelInicialPral.down('[name=idnombreBeneficiarioProv]').setValue('');
                    panelInicialPral.down('[name=idnombreBeneficiarioProv]').show();
                }else{
                    panelInicialPral.down('[name=idnombreBeneficiarioProv]').setValue(cmbProveedor.rawValue);
                    panelInicialPral.down('[name=idnombreBeneficiarioProv]').hide();
                }
            }
        }
    }); 


/**     PAGO REEMBOLSO                      **/
    cmbProveedorReembolso = Ext.create('Ext.form.field.ComboBox', {
        displayField : 'nombre',            name:'cmbProveedorReembolso',       valueField   : 'cdpresta',
        forceSelection : true,              matchFieldWidth: false,             queryMode :'remote',
        queryParam: 'params.cdpresta',      minChars  : 2,                      store : storeProveedor,
        triggerAction: 'all',               hideTrigger:true,                   allowBlank:false
    });
    

/**     PAGO INDEMNIZATORIOS                **/
    cmbProveedorIndemnizatorio = Ext.create('Ext.form.field.ComboBox', {
        displayField : 'nombre',            name:'cmbProveedorIndemnizatorio',      valueField   : 'cdpresta',
        forceSelection : true,              matchFieldWidth: false,             queryMode :'remote',
        queryParam: 'params.cdpresta',      minChars  : 2,                      store : storeProveedor,
        triggerAction: 'all',               hideTrigger:true,                   allowBlank:false
    });
    

/**     PAGO REEMBOLSO / INDEMNIZATORIOS    **/
    cmbTipoMoneda = Ext.create('Ext.form.ComboBox', {
        id:'cmbTipoMoneda',         store: storeTipoMoneda,     value:'001',        queryMode:'local',  
        displayField: 'value',      valueField: 'key',          editable:false,     allowBlank:false
        ,listeners : {
            select:function(e){
                if(e.getValue() =='001'){
                    // EL TIPO DE MONEDA ES PESO
                    valorIndexSeleccionado.set('tasaCambio','0.00');
                    valorIndexSeleccionado.set('importeFactura','0.00');
                }else{
                    var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
                    var importeFactura = valorIndexSeleccionado.get('importeFactura');
                    var importeMxn = +tasaCambio * +importeFactura;
                    valorIndexSeleccionado.set('importe',importeMxn);
                }
            }
        }
    });

    /**     CAMPOS PARA RECHAZO         **/
    motivoRechazo= Ext.create('Ext.form.ComboBox',{
        id:'motivoRechazo',         name:'smap1.cdmotivo',      fieldLabel: 'Motivo',       store: storeRechazos,
        queryMode:'local',          displayField: 'value',      valueField: 'key',          allowBlank:false,
        blankText:'El motivo es un dato requerido',             editable:false,             labelWidth : 150,
        width: 600,                 emptyText:'Seleccione ...',
        listeners: {
            select: function(combo, records, eOpts){
                panelRechazarReclamaciones.down('[name=smap1.incisosRechazo]').setValue('');
                panelRechazarReclamaciones.down('[name=smap1.comments]').setValue('');
                storeIncisosRechazos.removeAll();
                storeIncisosRechazos.load({
                     params: {
                        'params.pv_cdmotivo_i' : records[0].get('key'),
                        'params.pv_cdramo_i'   : panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                        'params.pv_cdtipsit_i' : panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue()
                     }
                 });
            }
        }
    });
    
    textoRechazo = Ext.create('Ext.form.field.TextArea', {
        fieldLabel: 'Descripci&oacute;n modificado',            labelWidth: 150,            width: 600
        ,name:'smap1.comments',                                 height: 250,                allowBlank: false
        ,blankText:'La descripci&oacute;n es un dato requerido'
    });

    incisosRechazo= Ext.create('Ext.form.ComboBox',{
        id:'incisosRechazo',                            name:'smap1.incisosRechazo',                        fieldLabel: 'Incisos Rechazo',
        store: storeIncisosRechazos,                    queryMode:'local',                                  displayField: 'value',
        valueField: 'key',                              blankText:'El motivo es un dato requerido',         editable:false,
        labelWidth : 150,                               width: 600,                                         emptyText:'Seleccione ...',
        listeners: {
            select: function(combo, records, eOpts){
                textoRechazo.setValue(records[0].get('value'));
            }
        }
    });

    /*//////////////////////////////////////////////////////////////////
    ////////////////   DECLARACION DE FACTURAS PAGO DIRECTO ////////////
    /////////////////////////////////////////////////////////////////*/
    Ext.define('EditorFacturaDirecto', {
        extend: 'Ext.grid.Panel',
        name:'editorFacturaDirecto',
        title: 'Alta de facturas Pago Directo',
        frame: true,
        selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
        initComponent: function(){
            Ext.apply(this, {
            width: 750,
            height: 250,
            plugins  :
            [
                Ext.create('Ext.grid.plugin.CellEditing',{
                    clicksToEdit: 1
                    ,listeners : {
                        beforeedit : function() {
                            valorIndexSeleccionado = gridFacturaDirecto.getView().getSelectionModel().getSelection()[0];
                        }
                    }
                })
            ],
            store: storeFacturaDirecto,
            columns: 
            [
                {   xtype: 'actioncolumn',      width: 40,          sortable: false,            menuDisabled: true,
                    items: [{
                        icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                        tooltip: '1',
                        scope: this,
                        handler: this.onRemoveClick
                    }]
                 },
                 {  header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2
                    ,editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {   header: 'Fecha de Factura',         dataIndex: 'fechaFactura',      flex:2,     renderer: Ext.util.Format.dateRenderer('d/m/Y')
                    ,editor : {
                        xtype : 'datefield',
                        format : 'd/m/Y',
                        editable : true
                    }
                },
                {   header: 'Moneda',                   dataIndex: 'tipoMonedaName',    flex:2
                    ,renderer : function(v) {
                        var leyenda = '';
                        if (typeof v == 'string'){
                            storeTipoMoneda.each(function(rec){
                                if (rec.data.key == v){
                                    leyenda = rec.data.value;
                                }
                            });
                        }else{
                            if (v.key && v.value){
                                leyenda = v.value;
                            } else {
                                leyenda = v.data.value;
                            }
                        }
                        return leyenda;
                    }
                },
                {   header: 'Tasa cambio',              dataIndex: 'tasaCambio',        flex:2,             renderer: Ext.util.Format.usMoney       },
                {   header: 'Importe Factura',          dataIndex: 'importeFactura',    flex:2,             renderer: Ext.util.Format.usMoney       },
                {   header: 'Importe MXN',              dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
                    ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    validarFacturaPagada(panelInicialPral.down('combo[name=cmbProveedor]').getValue(),valorIndexSeleccionado.get('noFactura'), e.getValue());
                                }
                            }
                        }
                }
            ],
            tbar:[
                {   text     : 'Agregar Factura'
                    ,icon    : _CONTEXT+'/resources/fam3icons/icons/book.png'
                    ,handler : _p21_agregarFactura
                },
                {   /*1.- MANDAMOS A GUARDAR LA INFORMACION DE LAS FACTURAS UNICAMENTE EN EL TRAMITE*/
                    text     : 'Guardar Cambios Factura'
                    ,icon    : _CONTEXT+'/resources/fam3icons/icons/accept.png'
                    ,handler : function() {
                        guardarFacturaSiniestro(); //Guardar Cambios Factura
                    }
                },
                {   /*MOSTRAMOS LA INFORMACION INICIAL DEL STORE DE LAS FACTURAS*/
                    text        :'Restaurar Facturas'
                    ,icon       :_CONTEXT+'/resources/fam3icons/icons/delete.png'
                    ,handler    :function() {
                        Ext.Ajax.request( {
                            url     :   _URL_CONSULTA_FACTURAS
                            ,params :   {
                                    'smap.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue()
                            }
                            ,success : function (response) {
                                storeFacturaDirecto.removeAll();
                                var json = Ext.decode(response.responseText).slist1;
                                for(var i = 0; i < json.length; i++){
                                    var fechaFacturaM = json[i].FFACTURA.match(/\d+/g); 
                                    var dateFec = new Date(fechaFacturaM[2], fechaFacturaM[1]-1,fechaFacturaM[0]);
                                    var rec = new modelFacturaSiniestro({
                                        noFactura: json[i].NFACTURA,
                                        fechaFactura: dateFec,
                                        tipoServicio: json[i].CDTIPSER,
                                        proveedor: json[i].CDPRESTA,
                                        importe: json[i].PTIMPORT,
                                        proveedorName: json[i].CDPRESTA,
                                        tipoServicioName: json[i].CDTIPSER,
                                        tipoMoneda: json[i].CDMONEDA,
                                        tipoMonedaName: json[i].CDMONEDA,
                                        tasaCambio: json[i].TASACAMB,
                                        importeFactura:json[i].PTIMPORTA
                                    });
                                    storeFacturaDirecto.add(rec);
                                }
                            },
                            failure : function () {
                                me.up().up().setLoading(false);
                                centrarVentanaInterna(Ext.Msg.show({
                                    title:'Error',
                                    msg: 'Error de comunicaci&oacute;n',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                }));
                            }
                        });
                        storeListAsegPagDirecto.removeAll();
                    }
            }],
            listeners: {
                itemclick: function(dv, record, item, index, e) {
                    /*OBTENEMOS LA INFORMACiON DE LOS ASEGURADOS*/
                    if(panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().hasSelection()){
                        if(banderaAsegurado =="1"){
                            guardarAseguradosFactura(); //Cambio de Factura
                        }
                            
                        var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                        var noFactura= rowSelected.get('noFactura');
                        storeListAsegPagDirecto.removeAll();
                        //REALIZAMOS EL LLAMADO POR EL NUMERO DE FACTURA Y TRAMITE
                        Ext.Ajax.request( {
                            url     : _URL_ASEGURADO_FACTURA
                            ,params : {
                                'params.nfactura'  : rowSelected.get('noFactura'),
                                'params.ntramite'  : panelInicialPral.down('[name=idNumTramite]').getValue()
                            }
                            ,success : function (response) {
                                var json=Ext.decode(response.responseText).slist1;
                                if(Ext.decode(response.responseText).slist1 != null) {
                                    for(var i = 0; i < json.length; i++){
                                        var fechaFacturaM = json[i].MODFECHAOCURRENCIA.match(/\d+/g); 
                                        var rec = new modelListAsegPagDirecto({///////
                                            modUnieco: json[i].MODUNIECO,
                                            modEstado: json[i].MODESTADO,
                                            modFechaOcurrencia:fechaFacturaM[2]+"/"+fechaFacturaM[1]+"/"+fechaFacturaM[0] ,
                                            modCdtipsit: json[i].MODCDTIPSIT,
                                            modCdperson: json[i].MODCDPERSON,
                                            modCdpersondesc: json[i].MODCDPERSONDESC,
                                            modNmsituac: json[i].MODNMSITUAC,
                                            modNmsolici: json[i].MODNMSOLICI,
                                            modNmsuplem: json[i].MODNMSUPLEM,
                                            modPolizaAfectada: json[i].MODPOLIZAAFECTADA,
                                            modRamo: json[i].MODRAMO,
                                            modFactura: json[i].NOFACTURAINT,
                                            modnumPoliza: json[i].NMPOLIEX,
                                            modNmautserv: json[i].MODNMAUTSERV,
                                            modTelefono: json[i].MODTELEFONO,
                                            modEmail: json[i].MODEMAIL
                                            
                                        });
                                        storeListAsegPagDirecto.add(rec);
                                    }
                                }
                            },
                            failure : function () {
                                me.up().up().setLoading(false);
                                Ext.Msg.show({
                                    title:'Error',
                                    msg: 'Error de comunicaci&oacute;n',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            }
                        });
                    }else{
                        storeListAsegPagDirecto.removeAll();
                    }
                }
            }
         });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            /*Eliminamos el record del store*/
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
            banderaFactura = "1";
            Ext.Ajax.request({
                url     : _URL_ELIMINAR_FACT_ASEG
                ,params:{
                    'params.ntramite'     : panelInicialPral.down('[name=idNumTramite]').getValue(),
                    'params.nfactura'     : record.data.noFactura,
                    'params.tipoPago'     : panelInicialPral.down('combo[name=cmbTipoPago]').getValue(),
                    'params.procedencia'  : 'ALTA_TRAMITE',
                    'params.valorAccion'  : 1
                }
                ,success : function (response){
                    var json = Ext.decode(response.responseText);
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
    gridFacturaDirecto=new EditorFacturaDirecto();

    /*DEFINIMOS LA TABLA DE PAGO REEMBOLSO*/
    Ext.define('EditorFacturaReembolso', {
        extend: 'Ext.grid.Panel',
        name:'editorFacturaReembolso',
        title: 'Alta de facturas Pago Reembolso',
        frame: true,
        selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
        initComponent: function(){
            Ext.apply(this, {
                width: 750,
                height: 250
                ,plugins  :
                [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1
                        ,listeners : {
                            beforeedit : function() {
                                valorIndexSeleccionado = gridFacturaReembolso.getView().getSelectionModel().getSelection()[0];
                            }
                        }
                    })
                ],
                store: storeFacturaReembolso,
                columns: 
                [
                    {
                        xtype: 'actioncolumn',
                        width: 40,
                        sortable: false,
                        menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: '2',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    },
                    {   
                        header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false
                        }
                    },
                    {
                        header: 'Fecha de Factura',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y')
                        ,editor : {
                            xtype : 'datefield',
                            format : 'd/m/Y',
                            editable : true
                        }
                    },
                    {
                        header: 'Proveedor',                dataIndex: 'proveedorName', flex:2
                        ,editor : cmbProveedorReembolso
                        ,renderer : function(v) {
                            var leyenda = '';
                            if (typeof v == 'string'){
                                if(storeProveedorReembolsoRender.cargado) {
                                    storeProveedorReembolsoRender.each(function(rec) {
                                        if (rec.data.cdpresta == v) {
                                            leyenda = rec.data.nombre;
                                        }
                                    });
                                }
                                else {
                                    leyenda='Cargando...';
                                }
                            }else{
                                if (v.key && v.value) {
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                                leyenda='2';
                            }
                            return leyenda;
                        }
                    },
                    {
                        header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2
                        ,editor : cmbTipoMoneda
                        ,renderer : function(v) {
                            var leyenda = '';
                            if (typeof v == 'string'){
                                storeTipoMoneda.each(function(rec) {
                                    if (rec.data.key == v) {
                                        leyenda = rec.data.value;
                                    }
                                });
                            }else {
                                if (v.key && v.value) {
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    },
                    {
                        header: 'Tasa cambio',              dataIndex: 'tasaCambio',    flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
                                    if(tipoMoneda =='001'){
                                        // EL TIPO DE MONEDA ES PESO
                                        valorIndexSeleccionado.set('tasaCambio','0.00');
                                        valorIndexSeleccionado.set('importeFactura','0.00');
                                        
                                        //valorIndexSeleccionado.set('PTMTOARA','0');
                                    }else{
                                        var tasaCambio = e.getValue();
                                        var importeFactura = valorIndexSeleccionado.get('importeFactura');
                                        var importeMxn = +tasaCambio * +importeFactura;
                                        valorIndexSeleccionado.set('importe',importeMxn);
                                        validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                    }
                                }
                            }
                        }
                    },
                    {
                        header: 'Importe Factura',              dataIndex: 'importeFactura',            flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
                                    if(tipoMoneda =='001'){
                                        // EL TIPO DE MONEDA ES PESO
                                        valorIndexSeleccionado.set('tasaCambio','0.00');
                                        valorIndexSeleccionado.set('importeFactura','0.00');
                                    }else{
                                        var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
                                        var importeFactura = e.getValue();
                                        var importeMxn = +tasaCambio * +importeFactura;
                                        valorIndexSeleccionado.set('importe',importeMxn);
                                        validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                    }
                                }
                            }
                        }
                    },
                    {
                        header: 'Importe MXN',                  dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), e.getValue());
                                }
                            }
                        }
                    }
                ],
                tbar: [
                    {
                        text        :   'Agregar Factura'
                        ,icon       : _CONTEXT+'/resources/fam3icons/icons/add.png'
                        ,handler    : _p21_agregarFactura
                    }
                ]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
            banderaFactura = "1";
        }
    });
    gridFacturaReembolso =new EditorFacturaReembolso();

    Ext.define('EditorPagoIndemnizatorio', {
        extend: 'Ext.grid.Panel',
        name:'editorPagoIndemnizatorio',
        title: 'Alta de Pago Indemnizatorio',
        frame: true,
        selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
        initComponent: function(){
                Ext.apply(this, {
                width: 750,
                height: 250
                ,plugins  :
                [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1
                        ,listeners : {
                            beforeedit : function() {
                                valorIndexSeleccionado = gridPagoIndemnizatorio.getView().getSelectionModel().getSelection()[0];
                            }
                        }
                    })
                ],
                store: storePagoIndemnizatorio,
                columns: 
                [
                    {
                        xtype: 'actioncolumn',
                        width: 40,
                        sortable: false,
                        menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: 'Quitar inciso',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    },
                    {   
                        header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2,         hidden:true
                    },
                    {
                        header: 'Fecha de Factura',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y')
                    },
                    {
                        header: 'Proveedor',                dataIndex: 'proveedorName', flex:2
                        ,editor : cmbProveedorIndemnizatorio
                        ,renderer : function(v) {
                            var leyenda = '';
                            if (typeof v == 'string') {
                                if(storeProveedorIndemnizatorioRender.cargado) {
                                    storeProveedorIndemnizatorioRender.each(function(rec) {
                                        if (rec.data.cdpresta == v) {
                                            leyenda = rec.data.nombre;
                                        }
                                    });
                                }
                                else {
                                    leyenda='Cargando...';
                                }
                            }else {
                                if (v.key && v.value) {
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                                leyenda='2';
                            }
                            return leyenda;
                        }
                    },
                    {
                        header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2
                        ,editor : cmbTipoMoneda
                        ,renderer : function(v) {
                        var leyenda = '';
                            if (typeof v == 'string') {
                                storeTipoMoneda.each(function(rec) {
                                    if (rec.data.key == v) {
                                        leyenda = rec.data.value;
                                    }
                                });
                            }else {
                                if (v.key && v.value) {
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    }
                    ,
                    {
                        header: 'Tasa cambio',              dataIndex: 'tasaCambio',    flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
                                    if(tipoMoneda =='001'){
                                        // EL TIPO DE MONEDA ES PESO
                                        valorIndexSeleccionado.set('tasaCambio','0.00');
                                        valorIndexSeleccionado.set('importeFactura','0.00');
                                        
                                        //valorIndexSeleccionado.set('PTMTOARA','0');
                                    }else{
                                        var tasaCambio = e.getValue();
                                        var importeFactura = valorIndexSeleccionado.get('importeFactura');
                                        var importeMxn = +tasaCambio * +importeFactura;
                                        valorIndexSeleccionado.set('importe',importeMxn);
                                        validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                    }
                                }
                            }
                        }
                    },
                    {
                        header: 'Importe Factura',              dataIndex: 'importeFactura',            flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
                                    if(tipoMoneda =='001'){
                                        // EL TIPO DE MONEDA ES PESO
                                        valorIndexSeleccionado.set('tasaCambio','0.00');
                                        valorIndexSeleccionado.set('importeFactura','0.00');
                                    }else{
                                        var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
                                        var importeFactura = e.getValue();
                                        var importeMxn = +tasaCambio * +importeFactura;
                                        valorIndexSeleccionado.set('importe',importeMxn);
                                        validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                    }
                                }
                            }
                        }
                    },
                    {
                        header: 'Importe MXN',                  dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), e.getValue());
                                }
                            }
                        }
                    }
                ],
                tbar: [
                    {
                        text     : 'Agregar Documento'
                        ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                        ,handler : _p21_agregarFactura
                    }
                ]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
            banderaFactura = "1";
        }
    });
    gridPagoIndemnizatorio =new EditorPagoIndemnizatorio();

        Ext.define('EditorPagoIndemnizatorioRecupera', {
        extend: 'Ext.grid.Panel',
        name:'editorPagoIndemnizatorioRecupera',
        title: 'Alta de Pago Indemnizatorio',
        frame: true,
        selModel: { selType: 'checkboxmodel', mode: 'SINGLE', checkOnly: true },
        initComponent: function(){
                Ext.apply(this, {
                width: 750,
                height: 250
                ,plugins  :
                [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1
                        ,listeners : {
                            beforeedit : function()
                            {
                                valorIndexSeleccionado = gridPagoIndemnizatorioRecupera.getView().getSelectionModel().getSelection()[0];
                            }
                        }
                    })
                ],
                store: storePagoIndemnizatorioRecupera,
                columns: 
                [
                    {
                        xtype: 'actioncolumn',
                        width: 40,
                        sortable: false,
                        menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: 'Quitar inciso',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    },
                    {   
                        header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2,         hidden:true
                    },
                    {
                        header: 'Fecha de Factura',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y')
                    },
                    {   header: 'Proveedor',            dataIndex: 'proveedorName',         flex:2
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false
                        }
                    },
                    {
                        header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2
                        ,editor : cmbTipoMoneda
                        ,renderer : function(v) {
                        var leyenda = '';
                            if (typeof v == 'string') {
                                storeTipoMoneda.each(function(rec) {
                                    if (rec.data.key == v) {
                                        leyenda = rec.data.value;
                                    }
                                });
                            }else {
                                if (v.key && v.value) {
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    }
                    ,
                    {
                        header: 'Tasa cambio',              dataIndex: 'tasaCambio',    flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
                                    if(tipoMoneda =='001'){
                                        // EL TIPO DE MONEDA ES PESO
                                        valorIndexSeleccionado.set('tasaCambio','0.00');
                                        valorIndexSeleccionado.set('importeFactura','0.00');
                                    }else{
                                        var tasaCambio = e.getValue();
                                        var importeFactura = valorIndexSeleccionado.get('importeFactura');
                                        var importeMxn = +tasaCambio * +importeFactura;
                                        valorIndexSeleccionado.set('importe',importeMxn);
                                        //validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                    }
                                }
                            }
                        }
                    },
                    {
                        header: 'Importe Factura',              dataIndex: 'importeFactura',            flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    var tipoMoneda = valorIndexSeleccionado.get('tipoMonedaName');
                                    if(tipoMoneda =='001'){
                                        // EL TIPO DE MONEDA ES PESO
                                        valorIndexSeleccionado.set('tasaCambio','0.00');
                                        valorIndexSeleccionado.set('importeFactura','0.00');
                                    }else{
                                        var tasaCambio = valorIndexSeleccionado.get('tasaCambio');
                                        var importeFactura = e.getValue();
                                        var importeMxn = +tasaCambio * +importeFactura;
                                        valorIndexSeleccionado.set('importe',importeMxn);
                                        //validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                    }
                                }
                            }
                        }
                    },
                    {
                        header: 'Importe MXN',                  dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            listeners : {
                                change:function(e){
                                    //validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), e.getValue());
                                }
                            }
                        }
                    }
                ],
                tbar: [
                    {
                        text     : 'Agregar Documento'
                        ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                        ,handler : _p21_agregarFactura
                    }
                ]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
            banderaFactura = "1";
        }
    });
    gridPagoIndemnizatorioRecupera =new EditorPagoIndemnizatorioRecupera();
    
    /*GENERACION DE LA TABLA PARA LOS ASEGURADOS*/
    Ext.define('EditorAsegPagoDirecto', {
        extend: 'Ext.grid.Panel',
        name:'editorAsegPagoDirecto',
        title: 'Asegurados Modificado',
        frame: true,
        //selType  : 'rowmodel',
        initComponent: function(){
                Ext.apply(this, {
                width: 750,
                height: 250,
                plugins  : [
                    Ext.create('Ext.grid.plugin.CellEditing',{
                        clicksToEdit: 1
                        ,listeners : {
                            beforeedit : function() {
                                valorIndexSeleccionado = gridAsegPagoDirecto.getView().getSelectionModel().getSelection()[0];
                                debug("Valor seleccionado gridAsegPagoDirecto --->",valorIndexSeleccionado);
                            }
                        }
                    })
                ],
                store: storeListAsegPagDirecto,
                columns: 
                [
                    {
                        xtype: 'actioncolumn',
                        width: 50,
                        sortable: false,
                        menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: '3',
                            scope: this,
                            handler: this.onRemoveClick
                        },
                        {
                            icon:_CONTEXT+'/resources/fam3icons/icons/application_view_list.png',
                            tooltip: 'Historial Siniestro',
                            scope: this,
                            handler: this.onHistorialClick
                        }]
                    },
                    {   header: 'Fecha <br/> Ocurrencia',       dataIndex: 'modFechaOcurrencia',        width:100,      renderer: Ext.util.Format.dateRenderer('d/m/Y')
                        ,editor : {
                            xtype : 'datefield',
                            format : 'd/m/Y',
                            editable : true
                        }
                    },
                    {
                        header: 'Cve<br/>Asegurado',        dataIndex: 'modCdperson',           width:110,              editor : cmbAseguradosPagDirecto
                    },
                    {
                        header: 'Nombre <br/> Asegurado',       dataIndex: 'modCdpersondesc',           width:250
                    },
                    {
                        header: 'N&uacute;mero <br/>P&oacute;liza', dataIndex: 'modnumPoliza',          width:150//,        hidden:true
                    },
                    {
                        header: 'Tel&eacute;fono',  dataIndex: 'modTelefono',           width:150
                        ,editor: {
                            xtype: 'numberfield',
                            allowBlank: false
                        }
                    },
                    {
                        header: 'Correo electr&oacute;nico',    dataIndex: 'modEmail',          width:150
                        ,editor: {
                            xtype: 'textfield',
                            vtype: 'email',
                            allowBlank: false
                        }
                    }/*,
                    {
                        header: 'Autorizacion', dataIndex: 'modtxtAutEspecial',         width:150
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false
                        }
                    }*/
                ],
                tbar: [
                    {
                        text        :   'Agregar Asegurado'
                        ,icon       :   _CONTEXT+'/resources/fam3icons/icons/user_add.png'
                        ,handler    :   _p21_agregarAseguradoClic
                    },
                    {
                        text     : 'Guardar cambios Asegurado'
                        ,icon    : _CONTEXT+'/resources/fam3icons/icons/accept.png'
                        ,handler : function() {
                            guardarAseguradosFactura();
                        }
                    }
                ]
            });
            this.callParent();
        },
        /*REMOVEMOS LOS ASEGURADOS*/
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
            banderaAsegurado = "1";
        },
        onHistorialClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            //this.getStore().removeAt(rowIndex);
            var windowHistSinies = Ext.create('Ext.window.Window',{
            modal       : true,
            buttonAlign : 'center',
            width       : 800,
            height      : 500,
            autoScroll  : true,
            loader      : {
                url     : _URL_LOADER_HISTORIAL_RECLAMACIONES,
                params  : {
                    'params.cdperson'  : record.get('modCdperson'),
                    'params.cdramo'    : null,
                    'params.nmpoliza'  : null,
                    'params.cdunieco'  : null
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
                handler: function() {windowHistSinies.close();}
            }]
        }).show();
            centrarVentana(windowHistSinies);
        }       
    });
    gridAsegPagoDirecto=new EditorAsegPagoDirecto();
    
    
    /* PANEL PARA EL PAGO DIRECTO */
    var panelListadoAsegurado= Ext.create('Ext.form.Panel',{
        border  : 0
        ,startCollapsed : true
        ,bodyStyle:'padding:5px;'
        ,items :
        [
            {
                xtype      : 'datefield',           name       : 'dtfechaOcurrencias',      fieldLabel : 'Fecha ocurrencia',
                allowBlank: false,                  maxValue   :  new Date(),               format      : 'd/m/Y'
            },{
                xtype: 'combo',                     name:'cmbAseguradoAfect',               fieldLabel : 'Asegurado',
                displayField : 'value',             valueField   : 'key',                   allowBlank: false,
                width:500,                          minChars  : 2,                          forceSelection : true,
                matchFieldWidth: false,             queryMode :'remote',                    queryParam: 'params.cdperson',
                store : storeAsegurados,            triggerAction: 'all',                   hideTrigger:true,
                listeners : {
                    'select' : function(combo, record) {
                        var params = {  'params.cdperson' : this.getValue(),
                                        'params.cdramo' : panelInicialPral.down('combo[name=cmbRamos]').getValue()};
                        cargaStorePaginadoLocal(storeListadoPoliza, _URL_CONSULTA_LISTADO_POLIZA, 'listaPoliza', params, function(options, success, response){
                            if(success){
                                var jsonResponse = Ext.decode(response.responseText);
                                if(jsonResponse.listaPoliza == null) {
                                    Ext.Msg.show({
                                        title: 'Aviso',
                                        msg: 'No existen p&oacute;lizas para el asegurado elegido.',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.WARNING
                                    });
                                    combo.clearValue();
                                    modPolizasAltaTramite.hide();
                                    return;
                                }
                            }else{
                                Ext.Msg.show({
                                    title: 'Aviso',
                                    msg: 'Error al obtener los datos.',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            }
                        });
                        modPolizasAltaTramite.show();
                    }
                }
            }
        ]
    });
    /*PANTALLA EMERGENTE PARA EL PAGO DIRECTO */
    /*var ventanaAgregarAsegurado = Ext.create('Ext.window.Window', {
        title: 'Asegurados',
        closeAction: 'hide',
        modal: true, 
        resizable: false,
        items:[panelListadoAsegurado],
        buttonAlign : 'center',
        buttons:[
            {
                text: 'Aceptar',
                icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                handler: function() {
                    if (panelListadoAsegurado.form.isValid()) {
                        var datos=panelListadoAsegurado.form.getValues();
                        var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                        var noFactura= rowSelected.get('noFactura');
                        var rec = new modelListAsegPagDirecto({.
                            modUnieco: panelInicialPral.down('[name="cdunieco"]').getValue(),
                            modEstado: panelInicialPral.down('[name="estado"]').getValue(),
                            modRamo: panelInicialPral.down('[name="cdramo"]').getValue(),
                            modNmsituac: panelInicialPral.down('[name="nmsituac"]').getValue(),
                            modPolizaAfectada: panelInicialPral.down('[name="polizaAfectada"]').getValue(),
                            modNmsolici: panelInicialPral.down('[name="idNmsolici"]').getValue(),
                            modNmsuplem: panelInicialPral.down('[name="idNmsuplem"]').getValue(),
                            modCdtipsit: panelInicialPral.down('[name="idCdtipsit"]').getValue(),
                            modNmautserv: "",
                            modFechaOcurrencia: datos.dtfechaOcurrencias,
                            modCdperson: datos.cmbAseguradoAfect,
                            modCdpersondesc: panelListadoAsegurado.query('combo[name=cmbAseguradoAfect]')[0].rawValue,
                            modnumPoliza:panelInicialPral.down('[name="idNumPolizaInt"]').getValue(),
                            modFactura:noFactura
                        });
                        storeListAsegPagDirecto.add(rec);
                        banderaAsegurado = "1";
                        limpiarRegistros();
                        panelListadoAsegurado.getForm().reset();
                        ventanaAgregarAsegurado.close();
                    } else {
                        Ext.Msg.show({
                            title: 'Aviso',
                            msg: 'Complete la informaci&oacute;n requerida',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.WARNING
                        });
                    }
                }
            },
            {
                text: 'Cancelar',
                icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                handler: function(){
                    panelListadoAsegurado.getForm().reset();
                    ventanaAgregarAsegurado.close();
                }
            }
        ]
    });*/

    var modPolizasAltaTramite = Ext.create('Ext.window.Window',
    {
        title           : 'Listado de P&oacute;liza'
        ,modal          : true
        ,resizable      : false
        ,buttonAlign    : 'center'
        ,closable       : false
        ,width          : 710
        ,minHeight      : 100 
        ,maxheight      : 400
        ,items  :
        [
            gridPolizasAltaTramite
        ]
    });
    
    //PANEL PRINCIPAL DEL PROCESO DE SINIESTRO
    var panelInicialPral= Ext.create('Ext.form.Panel', {
        border    : 0
        ,id : 'panelInicialPral'
        ,renderTo : 'div_clau'
        ,bodyPadding: 10
        ,width: 800
        ,layout      : {
            type     : 'table'
            ,columns : 2
        }
        ,defaults    : {
            style : 'margin:5px;'
        }
        ,items    :
        [
            {   xtype   : 'textfield',      fieldLabel : 'Unieco',              name        : 'cdunieco',           labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'estado',              name        : 'estado'  ,           labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'Ramo',                name        : 'cdramo'  ,           labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'nmsituac',            name        : 'nmsituac',           labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'P&oacute;liza afectada',name      :'polizaAfectada',      labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'nmsolici',            name        : 'idNmsolici',         labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'nmsuplem',            name        : 'idNmsuplem',         labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'cdtipsit',            name        : 'idCdtipsit',         labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'numPolizaInt',        name        : 'idNumPolizaInt',     labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'Asegurado',           name        : 'idnombreAsegurado',  labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'NumTramite',          name        : 'idNumTramite',       labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'ImporteFactura',      name        : 'ImporteIndFactura',  labelWidth: 170,    hidden:true     },
            {   xtype   : 'datefield'       ,fieldLabel : 'fechaIndFactura',    name        : 'fechaIndFactura',    format: 'd/m/Y',    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel : 'No.Factura',          name        : 'numIndFactura',      labelWidth: 170,    hidden:true     },
            {   xtype   : 'textfield',      fieldLabel: 'Estado',               name        : 'txtEstado',          readOnly   : true,  width   : 350,  value:'PENDIENTE'},
            cmbRamos,
            cmbModalidad,
            tipoPago,
            comboTipoAte,
            cmbOficinaReceptora,
            cmbOficinaEmisora,
            {   xtype   : 'datefield',      width       : 350,          fieldLabel  : 'Fecha recepci&oacute;n', name        : 'dtFechaRecepcion'    ,
                value   : new Date(),       format      : 'd/m/Y',      readOnly    : true,                     allowBlank  : false
            },
            {   xtype   : 'datefield',      fieldLabel  : 'Fecha ocurrencia',       name        : 'dtFechaOcurrencia',          format  : 'd/m/Y',
                editable: true,             width       : 350,                      maxValue    :  new Date(),                  allowBlank : false
            },
            aseguradoAfectado,
            cmbBeneficiario,
            {   xtype: 'numberfield',               fieldLabel: 'Id. Beneficiario', name        : 'idCveBeneficiario' 
                ,width       : 350,                 allowBlank : false
            },
            cmbProveedor,
            {   xtype   : 'textfield',      fieldLabel : 'Nombre Proveedor',    name   : 'idnombreBeneficiarioProv',    colspan:2,   width  : 350,
                listeners:{
                    afterrender: function(){
                        this.hide();
                    }
                }
            },
            {   xtype: 'numberfield',               fieldLabel: 'Tel&eacute;fono'           ,name       : 'txtTelefono' 
                ,width       : 350
            },
            {   xtype: 'textfield',                 fieldLabel: 'Correo Electr&oacute;nico' ,name       : 'txtEmail'
                ,width       : 350,                 vtype: 'email'
            },
            {   xtype: 'textfield',                 fieldLabel: 'Autorizacion'  ,name       : 'txtAutEspecial'
                ,width       : 300,                 value: "0",         hidden:true
            },
            {
                colspan:2
                ,border: false
                ,items    :
                [
                    gridFacturaDirecto 
                ]
            },
            {
                colspan:2
                ,border: false
                ,items    :
                [
                    gridFacturaReembolso
                ]
            },
            /*{
                colspan:2
                ,border: false
                ,items    :
                [
                    gridAsegPagDirecto
                ]
            },*/
            {
                colspan:2
                ,border: false
                ,items    :
                [
                    gridAsegPagoDirecto
                ]
            },
            {
                colspan:2
                ,border: false
                ,items    :
                [
                    gridPagoIndemnizatorio
                ]
            },
            {
                colspan:2
                ,border: false
                ,items    :
                    [
                        gridPagoIndemnizatorioRecupera
                    ]
            }
        ],
        buttonAlign:'center',
        buttons:[{
                xtype :'panel',
                defaults : { style : 'margin:5px' },
                border   : 0,
                width    : 750,
                ui       :'footer',
                items    : [
                    {
                        id:'botonCotizar',
                        icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                        xtype   : 'button',
                        text: 'Guardar cambios',
                        handler: function() {
                            var form = this.up('form').getForm();
                            
                            if (form.isValid()){
                                retornaMC = "1";
                                guardarInformacionAdicional();
                            }
                            else{
                                Ext.Msg.show({
                                    title:'Datos incompletos',
                                    msg: 'Favor de introducir todos los campos requeridos',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.WARNING
                                });
                            }
                        }
                    },{
                        text:'Checklist',
                        icon:_CONTEXT+'/resources/fam3icons/icons/application_view_list.png',
                        xtype   : 'button',
                        handler:function()
                        {
                            verificarFacturaAsegurado(); //Cheklist
                            windowLoader = Ext.create('Ext.window.Window',{
                                modal       : true,
                                buttonAlign : 'center',
                                width       : 600,
                                height      : 400,
                                autoScroll  : true,
                                loader      : {
                                    url     : _UrlRevisionDocsSiniestro,
                                    params  : {
                                        'params.nmTramite'  : panelInicialPral.down('[name=idNumTramite]').getValue(),
                                        'params.cdTipoPago' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue(),
                                        'params.cdTipoAtencion'  : panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
                                        'params.tieneCR'  : !Ext.isEmpty(null)
                                    },
                                    scripts  : true,
                                    loadMask : true,
                                    autoLoad : true,
                                    ajaxOptions: {
                                        method: 'POST'
                                    }
                                }
                            }).show();
                            centrarVentana(windowLoader);
                        }
                    },
                    {
                        text:'Generar Contra-Recibo',
                        xtype   : 'button',
                        icon: _CONTEXT+'/resources/fam3icons/icons/page_white_edit.png',
                        handler:function()
                        {
                            verificarFacturaAsegurado(); //Contra-Recibo
                            Ext.Ajax.request({
                                url: _UrlGenerarContrarecibo,
                                params: {
                                    'paramsO.pv_cdunieco_i' : panelInicialPral.down('combo[name=cmbOficReceptora]').getValue(),
                                    'paramsO.pv_cdramo_i'   : panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                                    'paramsO.pv_estado_i'   : panelInicialPral.down('[name="estado"]').getValue(),
                                    'paramsO.pv_nmpoliza_i' : panelInicialPral.down('[name="polizaAfectada"]').getValue(),
                                    'paramsO.pv_nmsuplem_i' : panelInicialPral.down('[name="idNmsuplem"]').getValue(),
                                    'paramsO.pv_ntramite_i' : panelInicialPral.down('[name="idNumTramite"]').getValue(),
                                    'paramsO.pv_nmsolici_i' : panelInicialPral.down('[name="idNmsolici"]').getValue(),
                                    'paramsO.pv_cdtippag_i' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue(),
                                    'paramsO.pv_cdtipate_i' : panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
                                    'paramsO.pv_tipmov_i'   : panelInicialPral.down('combo[name=cmbTipoPago]').getValue(),
                                    'paramsO.pv_pagoAut_i'  : "0" ,//pago Normal 
                                    'paramsO.caseIdRstn'    : _NVL(valorAction.caseIdRstn) //pago Normal 
                                },
                                success: function(response, opt) {
                                    var jsonRes=Ext.decode(response.responseText);
                                    if(jsonRes.success == true){
                                        //loadMcdinStore();
                                        var numRand=Math.floor((Math.random()*100000)+1);
                                        var windowVerDocu=Ext.create('Ext.window.Window',
                                        {
                                            title          : 'Contrarecibo de Documentos del Siniestro'
                                            ,width         : 700
                                            ,height        : 500
                                            ,collapsible   : true
                                            ,titleCollapse : true
                                            ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
                                                                +'src="'+panDocUrlViewDoc+'?subfolder=' + panelInicialPral.down('[name="idNumTramite"]').getValue() + '&filename=' + 'Contra_Recibo_Siniestro.pdf' +'">'
                                                                +'</iframe>'
                                            ,listeners     :
                                            {
                                                resize : function(win,width,height,opt){
                                                    $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
                                                }
                                            }
                                        }).show();
                                        windowVerDocu.center();
                                    }else {
                                            mensajeError(jsonRes.msgResult);
                                    }
                                },
                                failure: function(){
                                    mensajeError('No se pudo generar contrarecibo.');
                                }
                            });
                        }
                    },
                    {
                        text:'Enviar Contra-Recibo',
                        icon:_CONTEXT+'/resources/fam3icons/icons/user_edit.png',
                        xtype   : 'button',
                        handler:function()
                        {
                            _0_mail();
                        }
                    },
                    {
                        text:'Subir Documentos',
                        icon:_CONTEXT+'/resources/fam3icons/icons/folder_go.png',
                        xtype   : 'button',
                        handler:function()
                        {
                            verificarFacturaAsegurado(); //Subir Documento
                            windowLoader = Ext.create('Ext.window.Window',{
                                modal       : true,
                                buttonAlign : 'center',
                                title       : 'Documentos del siniestro',
                                width       : 600,
                                height      : 400,
                                autoScroll  : true,
                                cls         : 'VENTANA_DOCUMENTOS_CLASS',
                                loader      : {
                                    url     : _UrlDocumentosPoliza,
                                    params  : {
                                        'smap1.ntramite'  : panelInicialPral.down('[name=idNumTramite]').getValue()
                                        ,'smap1.cdtippag' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                                        ,'smap1.cdtipate' : panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue()
                                        ,'smap1.cdtiptra' : _TIPO_TRAMITE_SINIESTRO
                                        ,'smap1.cdunieco' : null
                                        ,'smap1.cdramo'   : panelInicialPral.down('combo[name=cmbRamos]').getValue()
                                        ,'smap1.estado'   : null
                                        ,'smap1.nmpoliza' : null
                                        ,'smap1.nmsuplem' : '0'
                                        ,'smap1.nmsolici' : ''
                                        ,'smap1.tipomov'  : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                                    },
                                    scripts  : true,
                                    loadMask : true,
                                    autoLoad : true,
                                    ajaxOptions: {
                                        method: 'POST'
                                    }
                                }
                            }).show();
                            centrarVentana(windowLoader);
                        }
                    },
                    {
                        text:'Turnar ',
                        icon:_CONTEXT+'/resources/fam3icons/icons/group_go.png',
                        xtype   : 'button',
                        handler:function()
                        {
                            verificarFacturaAsegurado(); //Turnar
                            turnarAreclamaciones();
                        }
                    },
                    {
                        text:'Rechazar Tr&aacute;mite',
                        icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                        xtype   : 'button',
                        handler:function()
                        {
                            verificarFacturaAsegurado(); //Rechazar tramite
                            panelRechazarReclamaciones= Ext.create('Ext.form.Panel', {
                                id: 'panelRechazarReclamaciones',
                                width: 650,
                                url: _URL_ActualizaStatusTramite,
                                bodyPadding: 5,
                                items: [
                                    motivoRechazo,incisosRechazo,textoRechazo,{
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
                                    text: 'Rechazar'
                                    ,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
                                    ,buttonAlign : 'center',
                                    handler: function() {
                                        if (panelRechazarReclamaciones.form.isValid()) {
                                            panelRechazarReclamaciones.form.submit({
                                                waitMsg:'Procesando...',
                                                params: {
                                                    'smap1.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(), 
                                                    'smap1.status'   : _RECHAZADO
                                                    ,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
                                                },
                                                failure: function(form, action) {
                                                    Ext.Msg.show({
                                                        title: 'ERROR',
                                                        msg: 'Error al Rechazar.',
                                                        buttons: Ext.Msg.OK,
                                                        icon: Ext.Msg.ERROR
                                                    });
                                                },
                                                success: function(form, action) {
                                                    var respuesta = Ext.decode(action.response.responseText);
                                                    if(respuesta.success==true){
                                                        windowLoader.close();
                                                        /*Se cierra y se tiene que mandar a la  MC*/
                                                        Ext.Ajax.request({
                                                            url: _UrlGeneraCartaRechazo,
                                                            params: {
                                                                'paramsO.pv_cdunieco_i' : null,
                                                                'paramsO.pv_cdramo_i'   : null,
                                                                'paramsO.pv_estado_i'   : null,
                                                                'paramsO.pv_nmpoliza_i' : null,
                                                                'paramsO.pv_nmsuplem_i' : null,
                                                                'paramsO.pv_nmsolici_i' : null,
                                                                'paramsO.pv_tipmov_i'   : panelInicialPral.down('combo[name=cmbTipoPago]').getValue(),
                                                                'paramsO.pv_ntramite_i' : panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                'paramsO.tipopago' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                                                            },
                                                            success: function(response, opt) {
                                                                var jsonRes=Ext.decode(response.responseText);
                                                                if(jsonRes.success == true){    
                                                                    var numRand=Math.floor((Math.random()*100000)+1);
                                                                    var windowVerDocu=Ext.create('Ext.window.Window',
                                                                    {
                                                                        title          : 'Carta de Rechazo del Siniestro'
                                                                        ,width         : 700
                                                                        ,height        : 500
                                                                        ,collapsible   : true
                                                                        ,titleCollapse : true
                                                                        ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
                                                                                            +'src="'+panDocUrlViewDoc+'?subfolder=' + panelInicialPral.down('[name=idNumTramite]').getValue() + '&filename=' + nombreReporteRechazo +'">'
                                                                                            +'</iframe>'
                                                                        ,listeners     :
                                                                        {
                                                                            resize : function(win,width,height,opt){
                                                                                $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
                                                                            }
                                                                        }
                                                                    });
                                                                }else {
                                                                    mensajeError('Error al generar la carta de rechazo.');
                                                                }
                                                            },
                                                            failure: function(){
                                                                mensajeError('Error al generar la carta de rechazo.');
                                                            }
                                                        });
                                                        mensajeCorrecto('&Eacute;XITO','Se ha rechazado correctamente.',function(){
                                                            windowLoader.close();
                                                            Ext.create('Ext.form.Panel').submit(
                                                            {
                                                                url     : _p12_urlMesaControl
                                                                ,standardSubmit : true
                                                                ,params         :
                                                                {
                                                                    'smap1.gridTitle'      : 'Siniestros en espera'
                                                                    ,'smap2.pv_cdtiptra_i' : _TIPO_TRAMITE_SINIESTRO
                                                                }
                                                            });
                                                        });
                                                        
                                                    }else {
                                                        Ext.Msg.show({
                                                            title: 'ERROR',
                                                            msg: 'Error al Rechazar.',
                                                            buttons: Ext.Msg.OK,
                                                            icon: Ext.Msg.ERROR
                                                        });
                                                    }
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
                                    }
                                }
                                ]
                            });
                            windowLoader = Ext.create('Ext.window.Window',
                            {
                                title           : 'Rechazar Tr&aacute;mite'
                                ,modal          : true
                                ,buttonAlign    : 'center'
                                ,items          :
                                [
                                    panelRechazarReclamaciones
                                ]
                            }).show();
                            centrarVentana(windowLoader);
                        }
                    }
                ]
        }]
    });

    
    
    
    if(valorAction.ntramite == null){
        /*Oficina Receptora*/
        oficinaReceptora.load();
        panelInicialPral.down('combo[name=cmbOficReceptora]').setValue(valorAction.cdunieco);
        /*Tipo de pago*/
        panelInicialPral.down('combo[name=cmbTipoPago]').setValue(_TIPO_PAGO_DIRECTO);
        limpiarRegistrosTipoPago(panelInicialPral.down('combo[name=cmbTipoPago]').getValue());
        /*Oficina Emisora*/
        oficinaEmisora.load();
        panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
    }else{
        storeProveedor.load();
        Ext.Ajax.request(
        {
            url     : _URL_CONSULTA_ALTA_TRAMITE
            ,params:{
                'params.ntramite': valorAction.ntramite
            }
            ,success : function (response)
            {
                if(Ext.decode(response.responseText).listaMesaControl != null){
                    var json=Ext.decode(response.responseText).listaMesaControl[0];
                    debug("VALOR DEL JSON MODIFICADO ====>>>>",json);
                    origProd = json.cdramomc;	// guardamos el producto original, para validar que no se modifique (EGS)
                    //ASIGNACION DE VALORES GENERALES PARA PAGO DIRECTO Y REEMBOLSO
                    panelInicialPral.down('[name=idNumTramite]').setValue(valorAction.ntramite);
                    panelInicialPral.down('[name=txtEstado]').setValue('PENDIENTE');
                    panelInicialPral.down('combo[name=cmbOficReceptora]').setValue(json.cdsucdocmc);
                    oficinaEmisora.load();
                    panelInicialPral.down('combo[name=cmbOficEmisora]').setValue(json.cdsucadmmc);
                    panelInicialPral.down('[name=dtFechaRecepcion]').setValue(json.ferecepcmc);
                    
                    panelInicialPral.down('combo[name=cmbTipoPago]').setValue(json.otvalor02mc);
                    
                    storeRamos.load({
                        params:{
                            'params.idPadre':panelInicialPral.down('combo[name=cmbOficEmisora]').getValue()
                        }
                    });
                    
                    if(json.otvalor20mc == null || json.otvalor20mc==''){
                        panelInicialPral.down('combo[name=cmbRamos]').setValue(_SALUD_VITAL);
                    }else{
                        panelInicialPral.down('combo[name=cmbRamos]').setValue(json.otvalor20mc);
                    }
                    
                    storeModalidad.load({
                        params:{
                            'params.idPadre':panelInicialPral.down('combo[name=cmbRamos]').getValue()
                        }
                    });
                    panelInicialPral.down('combo[name=pv_cdtipsit_i]').setValue(json.otvalor26mc);
                    
                    storeTipoPago.load({
                        params:{
                            'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue()
                        }
                    });
                    
                    limpiarRegistrosTipoPago(panelInicialPral.down('combo[name=cmbTipoPago]').getValue());
                    
                    storeTipoAtencion.load({ 
                        params:{
                            'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                            'params.cdtipsit': panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                            'params.tipoPago':panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                        }
                    });
                    panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(json.otvalor07mc);
                    
                    Ext.Ajax.request({
                        url     : _URL_VAL_CAUSASINI
                        ,params : {
                            'params.cdramo'   : panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                            'params.cdtipsit' : panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                            'params.causaSini': 'IDBENEFI',
                            'params.cveCausa' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                        }
                        ,success : function (response){
                            var datosExtras = Ext.decode(response.responseText);
                            if(Ext.decode(response.responseText).datosInformacionAdicional != null){
                                var cveCauSini=Ext.decode(response.responseText).datosInformacionAdicional[0];
                                if(cveCauSini.REQVALIDACION =="S"){
                                    //Visualizamos el campo
                                    panelInicialPral.down('[name=idCveBeneficiario]').show();
                                    panelInicialPral.down('[name=idCveBeneficiario]').setValue(json.otvalor27mc);
                                }else{
                                    //ocultamos el campo
                                    panelInicialPral.down('[name=idCveBeneficiario]').setValue('0');
                                    panelInicialPral.down('[name=idCveBeneficiario]').hide();
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
                    
                    
                    //VALORES DE PAGO DIRECTO
                    if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
                        panelInicialPral.down('combo[name=cmbProveedor]').setValue(json.otvalor11mc);
                        panelInicialPral.down('[name=ImporteIndFactura]').setValue(json.otvalor03mc);
                        panelInicialPral.down('[name=fechaIndFactura]').setValue(json.otvalor06mc);
                        panelInicialPral.down('[name=numIndFactura]').setValue(json.otvalor08mc);
                        panelInicialPral.down('[name=idnombreBeneficiarioProv]').setValue(json.otvalor15mc);
                        if(json.otvalor11mc =='0'){
                            panelInicialPral.down('[name=idnombreBeneficiarioProv]').show();
                        }else{
                            panelInicialPral.down('[name=idnombreBeneficiarioProv]').hide();
                        }
                    }else{
                        //VALORES DE PAGO POR REEMBOLSO
                        panelInicialPral.down('[name="cdunieco"]').setValue(json.cduniecomc);
                        panelInicialPral.down('[name="estado"]').setValue(json.estadomc);
                        panelInicialPral.down('[name="cdramo"]').setValue(json.cdramomc);
                        panelInicialPral.down('[name="polizaAfectada"]').setValue(json.nmpolizamc);
                        panelInicialPral.down('[name="idNmsolici"]').setValue(json.nmsolicimc);
                        panelInicialPral.down('[name="idNmsuplem"]').setValue(json.nmsuplemmc);
                        panelInicialPral.down('[name="idCdtipsit"]').setValue(json.cdtipsitmc);
                        panelInicialPral.down('[name=dtFechaOcurrencia]').setValue(json.otvalor10mc);
                        panelInicialPral.down('[name=idnombreBeneficiarioProv]').setValue(json.otvalor15mc);
                        panelInicialPral.down('[name=idnombreAsegurado]').setValue(json.nombremc);
                        panelInicialPral.down('[name=txtAutEspecial]').setValue(json.otvalor22mc);
                        storeAsegurados.load({
                            params:{
                                'params.cdperson':json.otvalor09mc
                            }
                        });
                        panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue(json.otvalor09mc);
                        storeAsegurados2.load({
                            params:{
                                'params.cdunieco': json.cduniecomc,
                                'params.cdramo':   json.cdramomc,
                                'params.estado':   json.estadomc,
                                'params.nmpoliza': json.nmpolizamc,
                                'params.cdperson': json.otvalor04mc
                            }
                        });
                        
                        panelInicialPral.down('combo[name=cmbBeneficiario]').setValue(json.otvalor04mc);
                    }
                    
                    /*COMO SEGUNDO PUNTO ES OBTENER LA INFORMACION DE LOS GRIDS*/
                    Ext.Ajax.request({
                        url     : _URL_CONSULTA_GRID_ALTA_TRAMITE
                        ,params:{
                            'params.ntramite': valorAction.ntramite
                        }
                        ,success : function (response)
                        {
                            if(Ext.decode(response.responseText).listaAltaTramite != null)
                            {
                                var json=Ext.decode(response.responseText).listaAltaTramite;
                                debug("VALOR DEL JSON GRID ====> : ", json);
                                if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
                                    //PAGO DIRECTO
                                    for(var i = 0; i < json.length; i++){
                                        var fechaFacturaM = json[i].ffactura.match(/\d+/g); 
                                        var date = new Date(fechaFacturaM[2], fechaFacturaM[1]-1,fechaFacturaM[0]);
                                        var rec = new modelFacturaSiniestro({
                                            noFactura: json[i].nfactura,
                                            fechaFactura: date,
                                            tipoServicio: json[i].cdtipser,
                                            proveedor: json[i].cdpresta,
                                            importe: json[i].ptimport,
                                            proveedorName: json[i].cdpresta,
                                            tipoServicioName: json[i].dstipser,
                                            tipoMoneda: json[i].cdmoneda,
                                            tipoMonedaName: json[i].cdmoneda,
                                            tasaCambio: json[i].tasacamb,
                                            importeFactura:json[i].ptimporta
                                        });
                                        storeFacturaDirecto.add(rec);
                                    }
                                }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
                                    //PAGO POR REEMBOLSO
                                    storeProveedor.load();
                                    panelInicialPral.down('[name="nmsituac"]').setValue(json[0].nmsituac);
                                    panelInicialPral.down('[name="txtTelefono"]').setValue(json[0].telefono);
                                    panelInicialPral.down('[name="txtEmail"]').setValue(json[0].email);
                                    for(var i = 0; i < json.length; i++){
                                        var fechaFacturaM = json[i].ffactura.match(/\d+/g); 
                                        var dateFac = new Date(fechaFacturaM[2], fechaFacturaM[1]-1,fechaFacturaM[0]);
                                        var rec = new modelFacturaSiniestro({
                                            noFactura: json[i].nfactura,
                                            fechaFactura: dateFac,
                                            tipoServicio: json[i].cdtipser,
                                            proveedor: json[i].cdpresta,
                                            importe: json[i].ptimport,
                                            proveedorName: json[i].cdpresta,
                                            tipoServicioName: json[i].dstipser,
                                            tipoMoneda: json[i].cdmoneda,
                                            tipoMonedaName: json[i].cdmoneda,
                                            tasaCambio: json[i].tasacamb,
                                            importeFactura:json[i].ptimporta
                                        });
                                        storeFacturaReembolso.add(rec);
                                    }
                                }else{
                                    //Indemnizacion
                                    storeProveedor.load();
                                    panelInicialPral.down('[name="nmsituac"]').setValue(json[0].nmsituac);
                                    panelInicialPral.down('[name="txtTelefono"]').setValue(json[0].telefono);
                                    panelInicialPral.down('[name="txtEmail"]').setValue(json[0].email);
                                    for(var i = 0; i < json.length; i++){
                                        var fechaFacturaM = json[i].ffactura.match(/\d+/g); 
                                        var dateFac = new Date(fechaFacturaM[2], fechaFacturaM[1]-1,fechaFacturaM[0]);
                                        
                                        if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                                            var rec = new modelFacturaSiniestro({
                                                noFactura: json[i].nfactura,
                                                fechaFactura: dateFac,
                                                tipoServicio: json[i].cdtipser,
                                                proveedor: json[i].cdpresta,
                                                importe: json[i].ptimport,
                                                proveedorName: json[i].nombProv,
                                                tipoServicioName: json[i].dstipser,
                                                tipoMoneda: json[i].cdmoneda,
                                                tipoMonedaName: json[i].cdmoneda,
                                                tasaCambio: json[i].tasacamb,
                                                importeFactura:json[i].ptimporta
                                            });
                                            storePagoIndemnizatorioRecupera.add(rec);
                                        }else{
                                            var rec = new modelFacturaSiniestro({
                                                noFactura: json[i].nfactura,
                                                fechaFactura: dateFac,
                                                tipoServicio: json[i].cdtipser,
                                                proveedor: json[i].cdpresta,
                                                importe: json[i].ptimport,
                                                proveedorName: json[i].cdpresta,
                                                tipoServicioName: json[i].dstipser,
                                                tipoMoneda: json[i].cdmoneda,
                                                tipoMonedaName: json[i].cdmoneda,
                                                tasaCambio: json[i].tasacamb,
                                                importeFactura:json[i].ptimporta
                                            });
                                            storePagoIndemnizatorio.add(rec);
                                        }
                                    }
                                }
                            }
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////         F   U   N   C   I   O   N   E   S   
    
    // (EGS). No permitir que se modifique producto
    function validaProducto(){
    	debug("PRODUCTO ORIGINAL",origProd,panelInicialPral.down('combo[name=cmbRamos]').getValue()); //(EGS)
    	if (origProd != panelInicialPral.down('combo[name=cmbRamos]').getValue()){
    		Ext.Msg.show({
                    title:'Aviso',
                    msg: 'Si necesita modificar Producto. Rechace el tr&aacute;mite y genere uno nuevo',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.WARNING
            });
            panelInicialPral.down('combo[name=cmbRamos]').setValue(origProd);
    	}
    }
    
    function procesaGuardaAltaTramite(submitValues,retornaMC){
        panelInicialPral.setLoading(true);
        Ext.Ajax.request(
        {
            url: _URL_GUARDA_ALTA_TRAMITE,
            jsonData:Ext.encode(submitValues), // convierte a estructura JSON
            success:function(response,opts){
                panelInicialPral.setLoading(false);
                var jsonResp = Ext.decode(response.responseText);
                if(jsonResp.success==true){
                    var etiqueta;
                    var mensaje;
                    if(retornaMC =="1"){
                        if(valorAction.ntramite == null){
                        etiqueta = "Guardado";
                        mensaje = "Se gener&oacute; el n&uacute;mero de tr&aacute;mite "+ Ext.decode(response.responseText).msgResult; 
                        }else{
                            etiqueta = "Modificaci&oacute;n";
                            mensaje = "Se modific&oacute; el n&uacute;mero de tr&aacute;mite "+ valorAction.ntramite;
                        }
                        /*mensajeCorrecto(etiqueta,mensaje,function(){
                            Ext.create('Ext.form.Panel').submit(
                            {
                                url     : _p12_urlMesaControl
                                ,standardSubmit : true
                                ,params         :
                                {
                                    'smap1.gridTitle'      : 'Siniestros en espera'
                                    ,'smap2.pv_cdtiptra_i' : 16
                                }
                            });
                        });
                        panelInicialPral.getForm().reset();
                        storeFacturaDirecto.removeAll();
                        windowLoader.close();*/
                    }else{
                        if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
                            banderaFactura = "0";
                        }else{
                            banderaFactura = "0";
                        }
                    }
                }else{
                    Ext.Msg.show({
                        title:'Error',
                        msg: 'Error en el guardado del alta de tr&aacute;mite',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                    respuesta= false;
                }
            },
            failure:function(response,opts){
                panelInicialPrincipal.setLoading(false);
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        }); 
    }

    function guardarInformacionAdicional(){
        if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){//PARA PAGO DIRECTO
            panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
            var obtener = [];
            storeFacturaDirecto.each(function(record) {
                obtener.push(record.data);
            });
            /*VERIFICAMOS QUE EXISTA AL MENOS UNA FACTURA*/
            if(obtener.length <= 0){
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Se requiere ingresar al menos una factura',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
                return false;
            }else{
                /*VERIFICAMOS SI ES UNA FACTURA Y EL NUMERO DE ASEGURADO*/
                if(obtener.length == 1){
                    panelInicialPral.down('[name=ImporteIndFactura]').setValue(obtener[0].importe);
                    panelInicialPral.down('[name=fechaIndFactura]').setValue(obtener[0].fechaFactura);
                    panelInicialPral.down('[name=numIndFactura]').setValue(obtener[0].noFactura);
                    Ext.Ajax.request(
                    {
                        url     : _URL_ASEGURADO_FACTURA
                        ,params: {
                            'params.nfactura'  : obtener[0].noFactura,
                            'params.ntramite'  : panelInicialPral.down('[name=idNumTramite]').getValue()
                        }
                        ,success : function (response){
                            var listadoAsegurado=Ext.decode(response.responseText).slist1;
                            /*  SI ASEGURADO ES IGUAL A 1   */
                            if(listadoAsegurado.length == "1"){
                                var fecOcurrencia = listadoAsegurado[0].MODFECHAOCURRENCIA.match(/\d+/g);
                                panelInicialPral.down('[name="cdunieco"]').setValue(listadoAsegurado[0].MODUNIECO);
                                panelInicialPral.down('[name="estado"]').setValue(listadoAsegurado[0].MODESTADO);
                                panelInicialPral.down('[name="polizaAfectada"]').setValue(listadoAsegurado[0].MODPOLIZAAFECTADA);
                                panelInicialPral.down('[name="idNmsuplem"]').setValue(listadoAsegurado[0].MODNMSUPLEM);
                                panelInicialPral.down('[name="idNmsolici"]').setValue(listadoAsegurado[0].MODNMSOLICI);
                                panelInicialPral.down('[name="nmsituac"]').setValue(listadoAsegurado[0].MODNMSITUAC);
                                panelInicialPral.down('[name="idCdtipsit"]').setValue(listadoAsegurado[0].MODCDTIPSIT);
                                panelInicialPral.down('[name=dtFechaOcurrencia]').setValue(fecOcurrencia[2]+"/"+fecOcurrencia[1]+"/"+fecOcurrencia[0]);//(listadoAsegurado[0].MODFECHAOCURRENCIA);
                                storeAsegurados.load({
                                    params:{
                                        'params.cdperson':listadoAsegurado[0].MODCDPERSON
                                    }
                                });
                                panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue(listadoAsegurado[0].MODCDPERSON);//listadoAsegurado[0].MODCDPERSON);
                            }
                            if(listadoAsegurado.length > "1"){
                                //else{
                                    /*  SI ASEGURADO ES MAYOR A 1   */
                                    panelInicialPral.down('[name="cdunieco"]').setValue('');
                                    panelInicialPral.down('[name="estado"]').setValue('');
                                    panelInicialPral.down('[name="polizaAfectada"]').setValue('');
                                    panelInicialPral.down('[name="idNmsuplem"]').setValue('');
                                    panelInicialPral.down('[name="idNmsolici"]').setValue('');
                                    panelInicialPral.down('[name="nmsituac"]').setValue('');
                                    panelInicialPral.down('[name="idCdtipsit"]').setValue(listadoAsegurado[0].MODCDTIPSIT);
                                    panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
                                    panelInicialPral.down('[name=dtFechaOcurrencia]').setValue('');
                            }
                            var submitValues={};
                            var formulario=panelInicialPral.form.getValues();
                            submitValues['params']=formulario;
                            
                            procesaGuardaAltaTramite(submitValues,retornaMC);
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
                }else{
                    //obtenemos el valor del cdtipsit y le hacemos la asignacion
                    Ext.Ajax.request(
                    {
                        url     : _URL_VALOR_CDTIPSIT
                        ,params:{
                            'params.ntramite': panelInicialPral.down('[name=idNumTramite]').getValue()
                        }
                        ,success : function (response)
                        {
                            panelInicialPral.down('[name="idCdtipsit"]').setValue(Ext.decode(response.responseText).validaCdTipsitTramite);
                            panelInicialPral.down('[name=fechaIndFactura]').setValue('');
                            panelInicialPral.down('[name=numIndFactura]').setValue('');
                            var sumaTotal= 0;
                            for(i=0;i < obtener.length;i++){
                                sumaTotal =sumaTotal + (+ obtener[i].importe);
                                panelInicialPral.down('[name=ImporteIndFactura]').setValue(sumaTotal);
                            }
                            
                            var submitValues={};
                            var formulario=panelInicialPral.form.getValues();
                            submitValues['params']=formulario;
                            procesaGuardaAltaTramite(submitValues,retornaMC);
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
        }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
            //PAGO POR REEMBOLSO
            var obtener = [];
            /*Verificamos el numero total de facturas*/
            storeFacturaReembolso.each(function(record) {
                obtener.push(record.data);
            });
            if(obtener.length <= 0){
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Se requiere ingresar al menos una factura',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
                return false;
            }else{
                
                for(i=0;i < obtener.length;i++){
                    if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
                        obtener[i].importeFactura == null ||obtener[i].proveedorName == null ||obtener[i].tasaCambio == null ||
                        obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""||
                        obtener[i].importeFactura == "" ||obtener[i].proveedorName == "" ||obtener[i].tasaCambio == ""){
                        centrarVentanaInterna(Ext.Msg.show({
                            title:'Facturas',
                            msg: 'Favor de introducir los campos requeridos en la factura',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.WARNING
                        }));
                        return false;
                    }
                }
                
                if(obtener.length == 1){
                    panelInicialPral.down('combo[name=cmbProveedor]').setValue(obtener[0].proveedorName);
                    panelInicialPral.down('[name=ImporteIndFactura]').setValue(obtener[0].importe);
                    panelInicialPral.down('[name=fechaIndFactura]').setValue(obtener[0].fechaFactura);
                    panelInicialPral.down('[name=numIndFactura]').setValue(obtener[0].noFactura);
                
                }else{
                    panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                    //panelInicialPral.down('[name=ImporteIndFactura]').setValue('');
                    panelInicialPral.down('[name=fechaIndFactura]').setValue('');
                    panelInicialPral.down('[name=numIndFactura]').setValue('');
                    var sumaTotal= 0;
                    for(i=0;i < obtener.length;i++){
                        sumaTotal =sumaTotal + (+ obtener[i].importe);
                        panelInicialPral.down('[name=ImporteIndFactura]').setValue(sumaTotal);
                    }
                }
            }
            var submitValues={};
            var datosTablas = [];
            var formulario=panelInicialPral.form.getValues();
            submitValues['params']=formulario;
            
            storeFacturaReembolso.each(function(record,index){
                datosTablas.push({
                    nfactura:record.get('noFactura'),
                    ffactura:record.get('fechaFactura'),
                    cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
                    cdpresta:record.get('proveedorName'),
                    ptimport:record.get('importe'),
                    cdmoneda:record.get('tipoMonedaName'),
                    tasacamb:record.get('tasaCambio'),
                    ptimporta:record.get('importeFactura')
                });
            });
            
            submitValues['datosTablas']=datosTablas;
            panelInicialPral.setLoading(true);
            submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);
            procesaGuardaAltaTramite(submitValues,retornaMC);
        }
        else{ //PAGO POR INMEDIZACION
            var obtener = [];
            /*Verificamos el numero total de facturas*/
            if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                storePagoIndemnizatorioRecupera.each(function(record) {
                    obtener.push(record.data);
                });
            }else{
                storePagoIndemnizatorio.each(function(record) {
                    obtener.push(record.data);
                });
            }
            
            if(obtener.length <= 0){
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Se requiere ingresar al menos un documento',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
                return false;
            }else{
                for(i=0;i < obtener.length;i++){
                    if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
                        obtener[i].importeFactura == null ||obtener[i].proveedorName == null ||obtener[i].tasaCambio == null ||
                        obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""||
                        obtener[i].importeFactura == "" ||obtener[i].proveedorName == "" ||obtener[i].tasaCambio == ""){
                        centrarVentanaInterna(Ext.Msg.show({
                            title:'Documentos',
                            msg: 'Favor de introducir los campos requeridos en el documento',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.WARNING
                        }));
                        return false;
                    }
                }
                
                if(obtener.length == 1){
                    panelInicialPral.down('combo[name=cmbProveedor]').setValue(obtener[0].proveedorName);
                    panelInicialPral.down('[name=ImporteIndFactura]').setValue(obtener[0].importe);
                    panelInicialPral.down('[name=fechaIndFactura]').setValue(obtener[0].fechaFactura);
                    panelInicialPral.down('[name=numIndFactura]').setValue(obtener[0].noFactura);
                
                }else{
                    panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                    panelInicialPral.down('[name=fechaIndFactura]').setValue('');
                    panelInicialPral.down('[name=numIndFactura]').setValue('');
                    var sumaTotal= 0;
                    for(i=0;i < obtener.length;i++){
                        sumaTotal =sumaTotal + (+ obtener[i].importe);
                        panelInicialPral.down('[name=ImporteIndFactura]').setValue(sumaTotal);
                    }
                }
            }
            var submitValues={};
            var datosTablas = [];
            var formulario=panelInicialPral.form.getValues();
            submitValues['params']=formulario;
            
            if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                storePagoIndemnizatorioRecupera.each(function(record,index){
                    datosTablas.push({
                        nfactura:record.get('noFactura'),
                        ffactura:record.get('fechaFactura'),
                        cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
                        cdpresta:record.get('proveedorName'),
                        nombprov:record.get('proveedorName'),
                        ptimport:record.get('importe'),
                        cdmoneda:record.get('tipoMonedaName'),
                        tasacamb:record.get('tasaCambio'),
                        ptimporta:record.get('importeFactura')
                    });
                });
            }else{
                storePagoIndemnizatorio.each(function(record,index){
                    datosTablas.push({
                        nfactura:record.get('noFactura'),
                        ffactura:record.get('fechaFactura'),
                        cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
                        cdpresta:record.get('proveedorName'),
                        ptimport:record.get('importe'),
                        cdmoneda:record.get('tipoMonedaName'),
                        tasacamb:record.get('tasaCambio'),
                        ptimporta:record.get('importeFactura')
                    });
                });
            }
            
            submitValues['datosTablas']=datosTablas;
            panelInicialPral.setLoading(true);
            submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);
            procesaGuardaAltaTramite(submitValues,retornaMC);
        }
    }
    
    function limpiarRegistros(){
        panelInicialPral.down('[name="cdunieco"]').setValue('');
        panelInicialPral.down('[name="estado"]').setValue('');
        panelInicialPral.down('[name="cdramo"]').setValue('');
        panelInicialPral.down('[name="nmsituac"]').setValue('');
        panelInicialPral.down('[name="polizaAfectada"]').setValue('');
        panelInicialPral.down('[name="idNmsolici"]').setValue('');
        panelInicialPral.down('[name="idNmsuplem"]').setValue('');
        panelInicialPral.down('[name="idCdtipsit"]').setValue('');
        panelInicialPral.down('[name="idNumPolizaInt"]').setValue('');
        return true;
    }
    
    function limpiarRegistrosTipoPago(tipoPago){
        var pagoDirecto = true;
        var pagoReembolso = true;
        if(tipoPago == _TIPO_PAGO_DIRECTO){
            pagoDirecto = false;
            pagoReembolso = true;
            panelInicialPral.down('[name=editorFacturaDirecto]').show();
            panelInicialPral.down('[name=editorAsegPagoDirecto]').show();
            panelInicialPral.down('[name=editorFacturaReembolso]').hide();
            panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
            panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
            panelInicialPral.down('combo[name=cmbBeneficiario]').hide();
            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').hide();
            panelInicialPral.down('[name=dtFechaOcurrencia]').hide();
            panelInicialPral.down('combo[name=cmbBeneficiario]').setValue('');
            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
            panelInicialPral.down('[name=dtFechaOcurrencia]').setValue('');
            panelInicialPral.down('combo[name=cmbProveedor]').show();
            panelInicialPral.down('[name=txtTelefono]').hide();
            panelInicialPral.down('[name=txtEmail]').hide();
        }else if(tipoPago == _TIPO_PAGO_REEMBOLSO){
            pagoDirecto = true;
            pagoReembolso = false;
            panelInicialPral.down('[name=editorFacturaDirecto]').hide();
            panelInicialPral.down('[name=editorAsegPagoDirecto]').hide();
            panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
            panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
            panelInicialPral.down('[name=editorFacturaReembolso]').show();
            panelInicialPral.down('combo[name=cmbProveedor]').hide();
            panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
            panelInicialPral.down('combo[name=cmbBeneficiario]').show();
            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').show();
            panelInicialPral.down('[name=dtFechaOcurrencia]').show();
            panelInicialPral.down('[name=txtTelefono]').show();
            panelInicialPral.down('[name=txtEmail]').show();
        }else{
            pagoDirecto = true;
            pagoReembolso = false;
            if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
                panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').show();
                panelInicialPral.down('[name=editorFacturaDirecto]').hide();
                panelInicialPral.down('[name=editorAsegPagoDirecto]').hide();
                panelInicialPral.down('[name=editorFacturaReembolso]').hide();
                panelInicialPral.down('combo[name=cmbProveedor]').hide();
                panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                panelInicialPral.down('combo[name=cmbBeneficiario]').show();
                panelInicialPral.down('combo[name=cmbAseguradoAfectado]').show();
                panelInicialPral.down('[name=dtFechaOcurrencia]').show();
                panelInicialPral.down('[name=txtTelefono]').show();
                panelInicialPral.down('[name=txtEmail]').show();
            }else{
                panelInicialPral.down('[name=editorPagoIndemnizatorio]').show();
                panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
                panelInicialPral.down('[name=editorFacturaDirecto]').hide();
                panelInicialPral.down('[name=editorAsegPagoDirecto]').hide();
                panelInicialPral.down('[name=editorFacturaReembolso]').hide();
                panelInicialPral.down('combo[name=cmbProveedor]').hide();
                panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                panelInicialPral.down('combo[name=cmbBeneficiario]').show();
                panelInicialPral.down('combo[name=cmbAseguradoAfectado]').show();
                panelInicialPral.down('[name=dtFechaOcurrencia]').show();
                panelInicialPral.down('[name=txtTelefono]').show();
                panelInicialPral.down('[name=txtEmail]').show();
            }
        }
        panelInicialPral.down('combo[name=cmbBeneficiario]').allowBlank = pagoReembolso;
        panelInicialPral.down('[name=idCveBeneficiario]').allowBlank = pagoReembolso;
        panelInicialPral.down('combo[name=cmbAseguradoAfectado]').allowBlank = pagoReembolso;
        panelInicialPral.down('[name=dtFechaOcurrencia]').allowBlank = pagoReembolso;
        panelInicialPral.down('[name=txtTelefono]').allowBlank = pagoReembolso;
        panelInicialPral.down('[name=txtEmail]').allowBlank = pagoReembolso;
        panelInicialPral.down('combo[name=cmbProveedor]').allowBlank = pagoDirecto;
        return true;
    }
    
    function guardarAseguradosFactura(){
        var obtener = [];
        /*OBTENEMOS LA INFORMACION DE LOS ASEGURADOS*/
        storeListAsegPagDirecto.each(function(record) {
            obtener.push(record.data);
        });
        
        if(obtener.length <= 0){
            Ext.Msg.show({
                title:'Error',
                msg: 'Se requiere al menos un asegurado',
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });
            return false;
        }else{
            for(i=0;i < obtener.length;i++){
                if(obtener[i].modTelefono == null ||obtener[i].modEmail == null ||
                    obtener[i].modTelefono == "" ||obtener[i].modEmail == ""){
                    centrarVentanaInterna(Ext.Msg.show({
                        title:'Asegurado',
                        msg: 'Los siguientes campos son requeridos: tel&eacute;fono / Correo electr&oacute;nico.',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    }).defer(100));
                    return false;
                }
            }
            
            var submitValues={};
            var formulario=panelInicialPral.form.getValues();
            submitValues['params']=formulario;
            var datosTablas = [];
            storeListAsegPagDirecto.each(function(record,index){
                debug("VALOR DEL RECORD ===> ",record);
                
                datosTablas.push({
                    modUnieco: record.get('modUnieco'),
                    modEstado: record.get('modEstado'),
                    modFechaOcurrencia: record.get('modFechaOcurrencia'),
                    modCdtipsit: record.get('modCdtipsit'),
                    modCdperson: record.get('modCdperson'),
                    modCdpersondesc: record.get('modCdpersondesc'),
                    modNmsituac: record.get('modNmsituac'),
                    modNmsolici: record.get('modNmsolici'),
                    modNmsuplem: record.get('modNmsuplem'),
                    modPolizaAfectada: record.get('modPolizaAfectada'),
                    modRamo: record.get('modRamo'),
                    modFactura: record.get('modFactura'),
                    modnumPoliza: record.get('modnumPoliza'),
                    modNmautserv: record.get('modNmautserv'),
                    modTelefono: record.get('modTelefono'),
                    modEmail: record.get('modEmail'),
                    modtxtAutEspecial: record.get('modtxtAutEspecial')
                });
            });
            submitValues['datosTablas']=datosTablas;
            submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);
            panelInicialPral.setLoading(true);
            Ext.Ajax.request(
            {
                url: _URL_GUARDA_ASEGURADO,
                jsonData:Ext.encode(submitValues),
                success:function(response,opts){
                    panelInicialPral.setLoading(false);
                    var jsonResp = Ext.decode(response.responseText);
                    if(jsonResp.success==true){
                        panelInicialPral.setLoading(false);
                        banderaAsegurado = "0";
                        retornaMC = 0;
                        guardarInformacionAdicional();
                    }
                    else{
                        Ext.Msg.show({
                            title:'Error',
                            msg: 'Error en el guardado de asegurados',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                        });
                        respuesta= false;
                    }
                },
                failure:function(response,opts)
                {
                    panelInicialPrincipal.setLoading(true);
                    Ext.Msg.show({
                        title:'Error',
                        msg: 'Error de comunicaci&oacute;n',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                }
            });
        }
    }
    
    function guardarFacturaSiniestro() {
        var obtener = [];
        storeFacturaDirecto.each(function(record) {
            obtener.push(record.data);
        });
        if(obtener.length <= 0){
            Ext.Msg.show({
                title:'Error',
                msg: 'Se requiere al menos una factura en el tr&aacute;mite',
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });
            return false;
        }else{
            
            for(i = 0; i < obtener.length; i++){
                if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
                    obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""){
                    centrarVentanaInterna(Ext.Msg.show({
                        title:'Facturas',
                        msg: 'Favor de introducir los campos requeridos en la factura',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    }));
                    return false;
                }
            }
            var submitValues={};
            var formulario=panelInicialPral.form.getValues();
            submitValues['params']=formulario;
            var datosTablas = [];
            storeFacturaDirecto.each(function(record,index){
                datosTablas.push({
                    nfactura:record.get('noFactura'),
                    ffactura:record.get('fechaFactura'),
                    cdtipser:panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue(),
                    cdpresta:panelInicialPral.down('combo[name=cmbProveedor]').getValue(),
                    ptimport:record.get('importe'),
                    cdmoneda:record.get('tipoMonedaName'),
                    tasacamb:record.get('tasaCambio'),
                    ptimporta:record.get('importeFactura')
                });
            });
            submitValues['datosTablas']=datosTablas;
            panelInicialPral.setLoading(true);
            submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);
            Ext.Ajax.request(
            {
                url: _URL_GUARDA_FACTURA_TRAMITE,
                jsonData:Ext.encode(submitValues),
                success:function(response,opts){
                    panelInicialPral.setLoading(false);
                    var jsonResp = Ext.decode(response.responseText);
                    if(jsonResp.success==true){
                        panelInicialPral.setLoading(false);
                        banderaFactura ="0";
                        retornaMC = 0;
                        guardarInformacionAdicional();
                    }else{
                        centrarVentanaInterna(Ext.Msg.show({
                            title:'Error',
                            msg: 'Favor de introducir los campos requeridos en la factura',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                        }));
                        respuesta= false;
                    }
                },
                failure:function(response,opts)
                {
                    panelInicialPrincipal.setLoading(false);
                    Ext.Msg.show({
                        title:'Error',
                        msg: 'Error de comunicaci&oacute;n',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                }
            });
        }
    }
    
    function _p21_agregarFactura()
    {
        if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
            if(banderaFactura =="1"){
                //GUARDAMOS LAS FACTURAS
                guardarFacturaSiniestro(); //Agregar Factura
                //VERIFICAMOS SI TIENE TAMBIEN ASEGURADOS QUE NO SE HAN GUARDADO
                if(banderaAsegurado =="1"){
                    guardarAseguradosFactura(); //Agregar Factura
                }
                banderaFactura = "1";
                storeFacturaDirecto.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
            }else{
                if(banderaAsegurado =="1"){
                    guardarAseguradosFactura(); //Agregar Factura
                }
                banderaFactura = "1";
                storeFacturaDirecto.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
            }
        }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
            debug("VALOR DE BANDERA FACTURA-->",banderaFactura);            
            if(banderaFactura =="1"){
                retornaMC = "0";
                guardarInformacionAdicional();
            }
            banderaFactura = "1";
            storeFacturaReembolso.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
        }else{
            if(banderaFactura =="1"){
                retornaMC = "0";
                guardarInformacionAdicional();
            }
            var fechaOcurrencia = panelInicialPral.down('[name=dtFechaOcurrencia]').getValue();
            if(fechaOcurrencia == null){
                mensajeError('Para agregar un documento se requiere la fecha de ocurrencia');
            }else{
                banderaFactura = "1";
                //storePagoIndemnizatorio.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
                if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                    storePagoIndemnizatorioRecupera.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001', importe:'0.00'}));
                }else{
                    storePagoIndemnizatorio.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
                }
            }
        }
    }
    
    function _p21_agregarAseguradoClic(){
        if(panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().hasSelection()){
            if(banderaFactura =="1"){
                //GUARDAMOS LAS FACTURAS
                guardarFacturaSiniestro();  //Agregar asegurados
                //VERIFICAMOS SI TIENE TAMBIEN ASEGURADOS QUE NO SE HAN GUARDADO
                if(banderaAsegurado =="1"){
                    guardarAseguradosFactura();  //Agregar asegurados
                    limpiarRegistros();
                    var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                    var noFactura= rowSelected.get('noFactura');
                    storeListAsegPagDirecto.add(new modelListAsegPagDirecto({ }));
                }else{
                    limpiarRegistros();
                    var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                    var noFactura= rowSelected.get('noFactura');
                    storeListAsegPagDirecto.add(new modelListAsegPagDirecto({ }));
                }
            }else{
                if(banderaAsegurado =="1"){
                    guardarAseguradosFactura();  //Agregar asegurados
                    limpiarRegistros();
                    var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                    var noFactura= rowSelected.get('noFactura');
                    storeListAsegPagDirecto.add(new modelListAsegPagDirecto({ }));
                }else{
                    limpiarRegistros();
                    var rowSelected = panelInicialPral.down('[name=editorFacturaDirecto]').getSelectionModel().getSelection()[0];
                    var noFactura= rowSelected.get('noFactura');
                    storeListAsegPagDirecto.add(new modelListAsegPagDirecto({ }));
                }
            }
        }else{
            centrarVentanaInterna(mensajeWarning("Debe seleccionar una factura para poder agregar los asegurados."));
        }
    }
    
    
    function validarFacturaPagada(cdpresta,nfactura, totalImporte){
        Ext.Ajax.request({
            url     : _URL_CONSULTA_FACTURA_PAGADA
            ,params:{
                'params.nfactura' : nfactura,
                'params.cdpresta' : cdpresta,
                'params.ptimport' : totalImporte
            }
            ,success : function (response) {
                if(Ext.decode(response.responseText).factPagada !=null){
                    centrarVentanaInterna(Ext.Msg.show({
                        title: 'Aviso',
                        msg: 'La factura '+ nfactura +' ya se encuentra procesado en el tr&aacute;mite '+Ext.decode(response.responseText).factPagada,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
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
    /*function validarFacturaPagada(cdpresta,nfactura){
        Ext.Ajax.request(
        {
            url     : _URL_CONSULTA_FACTURA_PAGADA
            ,params:{
                'params.nfactura' : nfactura,
                'params.cdpresta' : cdpresta
            }
            ,success : function (response)
            {
                if(Ext.decode(response.responseText).factPagada !=null)
                {
                    centrarVentanaInterna(Ext.Msg.show({
                        title: 'Aviso',
                        msg: 'La factura '+ nfactura +' ya se encuentra pagada en el tr&aacute;mite '+Ext.decode(response.responseText).factPagada,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    }));
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
    }*/
    
    function verificarFacturaAsegurado(){
        if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
            //Pago Directo
            if(banderaFactura =="1"){
                //GUARDAMOS LAS FACTURAS
                guardarFacturaSiniestro(); //Verificar Fac PD
                //VERIFICAMOS SI TIENE TAMBIEN ASEGURADOS QUE NO SE HAN GUARDADO
                if(banderaAsegurado =="1"){
                    guardarAseguradosFactura();  //Verificar Factura/asegurados
                }
            }else{
                if(banderaAsegurado =="1"){
                    guardarAseguradosFactura();  //Verificar Factura/asegurados
                }
            }
        }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
            //Pago Reembolso
            debug("VALOR DEL BANDERA FACTURA -->",banderaFactura);
            if(banderaFactura =="1"){
                retornaMC = "0";
                guardarInformacionAdicional();
            }
        }else{
            //Pago Reembolso e indemnizacion
            if(banderaFactura =="1"){
                retornaMC = "0";
                guardarInformacionAdicional();
            }
        }
    }
    
    function _0_mail()
    {
        Ext.create('Ext.window.Window',
        {
            title : 'Enviar Contra-Recibo'
            ,width : 550
            ,modal : true
            ,height : 150
            ,buttonAlign : 'center'
            ,bodyPadding : 5
            ,items :
            [
                {
                    xtype       : 'textfield'
                    ,id         : '_0_idInputCorreos'
                    ,fieldLabel : 'Correo(s)'
                    ,emptyText  : 'Correo(s) separados por ;'
                    ,labelWidth : 100
                    ,allowBlank : false
                    ,blankText  : 'Introducir correo(s) separados por ;'
                    ,width      : 500
                }
            ]
            ,buttons :
            [
                {
                    text : 'Enviar'
                    ,icon : _CONTEXT+'/resources/fam3icons/icons/accept.png'
                    ,handler : function()
                    {
                        var me = this;
                        if (Ext.getCmp('_0_idInputCorreos').getValue().length > 0
                                &&Ext.getCmp('_0_idInputCorreos').getValue() != 'Correo(s) separados por ;')
                        {
                            debug('Se va a enviar cotizacion');
                            me.up().up().setLoading(true);
                            Ext.Ajax.request(
                            {
                                url : _0_urlEnviarCorreo
                                ,params :
                                {
                                    to : Ext.getCmp('_0_idInputCorreos').getValue(),
                                    urlArchivo : _0_urlRutaReporte
                                                 + '?p_usuario='      + "rherdez"
                                                 + "&p_TRAMITE=" + panelInicialPral.down('[name=idNumTramite]').getValue()
                                                 + '&destype=cache'
                                                 + "&desformat=PDF"
                                                 + "&userid="        + _0_reportsServerUser
                                                 + "&ACCESSIBLE=YES"
                                                 + "&report="        + _0_reporteContraRecibo
                                                 + "&paramform=no",
                                    nombreArchivo : 'Contrarecibo_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf',
                                    asunto:'Contra-Recibo',
                                    mensaje :'Estimado(a) cliente,anexamos a este e-mail el contrarecibo de su(s) factura(s)  y nos ponemos a sus apreciables �rdenes.'
                                },
                                callback : function(options,success,response)
                                {
                                    me.up().up().setLoading(false);
                                    if (success)
                                    {
                                        var json = Ext.decode(response.responseText);
                                        if (json.success == true)
                                        {
                                            me.up().up().destroy();
                                            Ext.Msg.show(
                                            {
                                                title : 'Correo enviado'
                                                ,msg : 'El correo ha sido enviado'
                                                ,buttons : Ext.Msg.OK
                                            });
                                        }
                                        else
                                        {
                                            mensajeError('Error al enviar');
                                        }
                                    }
                                    else
                                    {
                                        errorComunicacion();
                                    }
                                }
                            });
                        }
                        else
                        {
                            mensajeWarning('Introduzca al menos un correo');
                        }
                    }
                }
                ,
                {
                    text     : 'Cancelar'
                    ,icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png'
                    ,handler : function()
                    {
                        this.up().up().destroy();
                    }
                }
            ]
        }).show();
        Ext.getCmp('_0_idInputCorreos').focus();
    }
    
    
    function turnarAreclamaciones(){
        Ext.Ajax.request({
        url: _UrlValidaDocumentosCargados,
        params: {
            'params.PV_NTRAMITE_I' : panelInicialPral.down('[name=idNumTramite]').getValue(),
            'params.PV_CDRAMO_I'   : panelInicialPral.down('combo[name=cmbRamos]').getValue(),
            'params.PV_cdtippag_I' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue(),
            'params.PV_CDTIPATE_I' : panelInicialPral.down('combo[name=cmbTipoAtencion]').getValue()
        },
        success: function(response, opt) {
            var jsonRes=Ext.decode(response.responseText);
            if(jsonRes.success == true){
                Ext.Ajax.request({
                    url: _URL_VALIDA_FACTURAASEGURADO
                    ,params :   {
                            'smap.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(),
                            'smap.tipoPago' : panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                    }
                    ,success : function (response){
                        var jsonRes=Ext.decode(response.responseText);
                        if(jsonRes.loadList[0].faltaAsegurados =="0"){
                            var comentariosText = Ext.create('Ext.form.field.TextArea', {
                                fieldLabel: 'Observaciones'
                                ,labelWidth: 150
                                ,width: 600
                                ,name:'smap1.comments'
                                ,height: 250
                                ,allowBlank : false
                            });
                            
                            windowLoader = Ext.create('Ext.window.Window',{
                                modal   : true,
                                buttonAlign : 'center',
                                width       : 663,
                                height      : 430,
                                autoScroll  : true,
                                items       : [
                                    Ext.create('Ext.form.Panel', {
                                        title: 'Turnar a Coordinador de Reclamaciones',
                                        width: 650,
                                        url: _URL_ActualizaStatusTramite,
                                        bodyPadding: 5,
                                        items: [comentariosText,{
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
                                                    ,itemId     : 'SWAGENTE3'
                                                    ,name       : 'SWAGENTE3'
                                                    ,inputValue : 'S'
                                                    ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                                                }
                                                ,{
                                                    boxLabel    : 'No'
                                                    ,name       : 'SWAGENTE3'
                                                    ,inputValue : 'N'
                                                    ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                                                }
                                            ]
                                        }],
                                        buttonAlign:'center',
                                        buttons: [{
                                            text: 'Turnar'
                                            ,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
                                            ,buttonAlign : 'center',
                                            handler: function() {
                                                var formPanel = this.up().up();
                                                if (formPanel.form.isValid()) {
                                                    if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _REEMBOLSO || panelInicialPral.down('combo[name=cmbTipoPago]').getValue() ==_INDEMNIZACION){
                                                        Ext.Ajax.request({
                                                            url: _UrlGeneraSiniestroTramite,
                                                            params: {
                                                                'params.pv_ntramite_i' : panelInicialPral.down('[name=idNumTramite]').getValue()
                                                            },
                                                            success: function(response, opt) {
                                                                var jsonRes=Ext.decode(response.responseText);
                                                                if(jsonRes.success == true){
                                                                    formPanel.form.submit({
                                                                        waitMsg:'Procesando...',
                                                                        params: {
                                                                            'smap1.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                            'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
                                                                            ,'smap1.swagente' : _fieldById('SWAGENTE3').getGroupValue()
                                                                        },
                                                                        failure: function(form, action)
                                                                        {
                                                                            switch (action.failureType){
                                                                                case Ext.form.action.Action.CONNECT_FAILURE:
                                                                                    errorComunicacion();
                                                                                    break;
                                                                                case Ext.form.action.Action.SERVER_INVALID:
                                                                                    mensajeError(action.result.mensaje);
                                                                                    break;
                                                                            }
                                                                        },
                                                                        success: function(form, action) {
                                                                            Ext.Ajax.request(
                                                                            {
                                                                                url: _URL_TurnarAOperadorReclamacion,
                                                                                params: {
                                                                                        'smap1.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                                        'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
                                                                                        ,'smap1.rol_destino'     : 'operadorsini'
                                                                                        ,'smap1.usuario_destino' : ''
                                                                                },
                                                                                success:function(response,opts){
                                                                                    Ext.Ajax.request(
                                                                                    {
                                                                                        url     : _URL_NOMBRE_TURNADO
                                                                                        ,params : 
                                                                                        {           
                                                                                            'params.ntramite': panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                                            'params.rolDestino':'operadorsini'
                                                                                        }
                                                                                        ,success : function (response)
                                                                                        {
                                                                                            if(panelInicialPral.down('combo[name=cmbRamos]').getValue() =="11"){
                                                                                                var usuarioTurnadoSiniestro1 = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
                                                                                                ///mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
                                                                                                debug("Usuario turnado1 ==>"+usuarioTurnadoSiniestro1);
                                                                                                var fields = usuarioTurnadoSiniestro1.split('|');
                                                                                                var Nombre = fields[0];
                                                                                                var usuario = fields[1];
                                                                                                
                                                                                                Ext.Ajax.request(
                                                                                                {
                                                                                                    url: _URL_ActualizaStatusTramite,
                                                                                                    params: {
                                                                                                            'smap1.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(), 
                                                                                                            'smap1.status'   : _STATUS_TRAMITE_EN_REVISION_MEDICA
                                                                                                            ,'smap1.rol_destino'     : 'medajustador'
                                                                                                            ,'smap1.usuario_destino' : ''
                                                                                                            ,'smap1.rol_inicial'     : 'OPERADORSINI'
                                                                                                            ,'smap1.usuario_inicial' :  usuario 
                                                                                                    },
                                                                                                    success:function(response,opts){
                                                                                                        Ext.Ajax.request(
                                                                                                        {
                                                                                                            url     : _URL_NOMBRE_TURNADO
                                                                                                            ,params : 
                                                                                                            {           
                                                                                                                'params.ntramite': panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                                                                'params.rolDestino': 'medajustador'
                                                                                                            }
                                                                                                            ,success : function (response)
                                                                                                            {
                                                                                                                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
                                                                                                                debug("Usuario turnado2 ==>"+usuarioTurnadoSiniestro);
                                                                                                                mensajeCorrecto('&Eacute;XITO','Se ha turnado correctamente a: '+usuarioTurnadoSiniestro,function(){
                                                                                                                    windowLoader.close();
                                                                                                                    Ext.create('Ext.form.Panel').submit(
                                                                                                                    {
                                                                                                                        url     : _p12_urlMesaControl
                                                                                                                        ,standardSubmit : true
                                                                                                                        ,params         :
                                                                                                                        {
                                                                                                                            'smap1.gridTitle'      : 'Siniestros en espera'
                                                                                                                            ,'smap2.pv_cdtiptra_i' : _TIPO_TRAMITE_SINIESTRO
                                                                                                                        }
                                                                                                                    });
                                                                                                                });
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
                                                                                                    },
                                                                                                    failure:function(response,opts)
                                                                                                    {
                                                                                                        Ext.Msg.show({
                                                                                                            title:'Error',
                                                                                                            msg: 'Error de comunicaci&oacute;n',
                                                                                                            buttons: Ext.Msg.OK,
                                                                                                            icon: Ext.Msg.ERROR
                                                                                                        });
                                                                                                    }
                                                                                                });
                                                                                            }else{
                                                                                                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
                                                                                                debug("VALOR DE RESPUESTA -->",usuarioTurnadoSiniestro);
                                                                                                mensajeCorrecto('&Eacute;XITO','Se ha turnado correctamente a: '+usuarioTurnadoSiniestro,function(){
                                                                                                    windowLoader.close();
                                                                                                    Ext.create('Ext.form.Panel').submit(
                                                                                                    {
                                                                                                        url     : _p12_urlMesaControl
                                                                                                        ,standardSubmit : true
                                                                                                        ,params         :
                                                                                                        {
                                                                                                            'smap1.gridTitle'      : 'Siniestros en espera'
                                                                                                            ,'smap2.pv_cdtiptra_i' : _TIPO_TRAMITE_SINIESTRO
                                                                                                        }
                                                                                                    });
                                                                                                });
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
                                                                                },
                                                                                failure:function(response,opts)
                                                                                {
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
                                                                }else{
                                                                    mensajeError('Error al generar Siniestro para Area de Reclamaciones');
                                                                }
                                                            },
                                                            failure: function(){
                                                                mensajeError('Error al generar Siniestro para Area de Reclamaciones');
                                                            }
                                                        });
                                                    }else{ // ---> PAGO DIRECTO
                                                        formPanel.form.submit({
                                                            waitMsg:'Procesando...',
                                                            params: {
                                                                'smap1.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
                                                            },
                                                            failure: function(form, action) {
                                                                switch (action.failureType){
                                                                    case Ext.form.action.Action.CONNECT_FAILURE:
                                                                        errorComunicacion();
                                                                        break;
                                                                    case Ext.form.action.Action.SERVER_INVALID:
                                                                        mensajeError(action.result.mensaje);
                                                                        break;
                                                                }
                                                            },
                                                            success: function(form, action) {
                                                                Ext.Ajax.request(
                                                                {
                                                                    url: _URL_TurnarAOperadorReclamacion,
                                                                    params: {
                                                                            'smap1.ntramite' : panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                            'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
                                                                            ,'smap1.rol_destino'     : 'operadorsini'
                                                                            ,'smap1.usuario_destino' : ''
                                                                    },
                                                                    success:function(response,opts){
                                                                        Ext.Ajax.request(
                                                                        {
                                                                            url     : _URL_NOMBRE_TURNADO
                                                                            ,params : 
                                                                            {           
                                                                                'params.ntramite': panelInicialPral.down('[name=idNumTramite]').getValue(),
                                                                                'params.rolDestino':'operadorsini'
                                                                            }
                                                                            ,success : function (response)
                                                                            {
                                                                                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
                                                                                debug("VALOR DE RESPUESTA -->",usuarioTurnadoSiniestro);
                                                                                mensajeCorrecto('&Eacute;XITO','Se ha turnado correctamente a: '+usuarioTurnadoSiniestro,function(){
                                                                                    windowLoader.close();
                                                                                    Ext.create('Ext.form.Panel').submit(
                                                                                    {
                                                                                        url     : _p12_urlMesaControl
                                                                                        ,standardSubmit : true
                                                                                        ,params         :
                                                                                        {
                                                                                            'smap1.gridTitle'      : 'Siniestros en espera'
                                                                                            ,'smap2.pv_cdtiptra_i' : _TIPO_TRAMITE_SINIESTRO
                                                                                        }
                                                                                    });
                                                                                });
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
                                                                    },
                                                                    failure:function(response,opts)
                                                                    {
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
                                                    }
                                                }else {
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
                                            }
                                        }]
                                    })
                                ]
                            }).show();
                            centrarVentana(windowLoader);
                        }else{
                            mensajeError("Falta asegurados para la factura:"+jsonRes.loadList[0].facturasFaltantes);
                        }
                    },
                    failure: function(){
                        mensajeError('Error al turnar.');
                    }
                });
            }else {
                mensajeError(jsonRes.msgResult);
            }
        },
        failure: function(){
            mensajeError('Error al turnar.');
        }
        });
    }
});
