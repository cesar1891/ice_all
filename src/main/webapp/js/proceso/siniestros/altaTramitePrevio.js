Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]); 
Ext.onReady(function() {
    var rolSiniestro;
        // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    var valorIndexSeleccionado= null;
    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true 
    });

    //Models:
    Ext.define('modelFacturaSiniestro', {
        extend:'Ext.data.Model',
        fields:['noFactura','fechaFactura','tipoServicio','tipoServicioName','proveedor','proveedorName','importe','tipoMoneda','tipoMonedaName','tasaCambio','importeFactura']
    });
    
    Ext.define('modelListadoProvMedico',{
        extend: 'Ext.data.Model',
        fields: [
            {type:'string',     name:'cdpresta'},   {type:'string', name:'nombre'},     {type:'string', name:'cdespeci'},   {type:'string',     name:'descesp'}
        ]
    });
    
    Ext.define('modelListadoPoliza',{
        extend: 'Ext.data.Model',
        fields: [   {type:'string',    name:'cdramo'},              {type:'string',    name:'cdunieco'},                {type:'string',    name:'estado'},
            {type:'string',    name:'nmpoliza'},            {type:'string',    name:'nmsituac'},                {type:'string',    name:'mtoBase'},
            {type:'string',    name:'feinicio'},            {type:'string',    name:'fefinal'},                 {type:'string',    name:'dssucursal'},
            {type:'string',    name:'dsramo'},              {type:'string',    name:'estatus'},                 {type:'string',    name:'dsestatus'},
            {type:'string',    name:'nmsolici'},            {type:'string',    name:'nmsuplem'},                {type:'string',    name:'cdtipsit'},
            {type:'string',    name:'dsestatus'},           {type:'string',    name:'vigenciaPoliza'},          {type:'string',    name:'faltaAsegurado'},
            {type:'string',    name:'fcancelacionAfiliado'},{type:'string',    name:'desEstatusCliente'},       {type:'string',    name:'numPoliza'},
            {type:'string',    name:'telefono'},            {type:'string',    name:'email'}]
    });
    
    Ext.define('modelListAsegPagDirecto',{
        extend: 'Ext.data.Model',
        fields: [
            {type:'string',    name:'modUnieco'},       {type:'string',    name:'modEstado'},           {type:'string',    name:'modRamo'},
            {type:'string',    name:'modNmsituac'},     {type:'string',    name:'modPolizaAfectada'},   {type:'string',    name:'modCdpersondesc'},
            {type:'string',    name:'modNmsolici'},     {type:'string',    name:'modNmsuplem'},         {type:'string',    name:'modCdtipsit'},
            {type:'string',    name:'modNmautserv'},    {type:'string',    name:'modFechaOcurrencia'},  {type:'string',    name:'modCdperson'},
            {type:'string',    name:'modnumPoliza'}
        ]
    });
    
    //Stores:
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
                'params.idPadre' : '1000'},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    
    var storeFacturaDirecto =new Ext.data.Store({
        autoDestroy: true,
        model: 'modelFacturaSiniestro'
    });
    
    var storeFacturaReembolso =new Ext.data.Store({
        autoDestroy: true,
        model: 'modelFacturaSiniestro'
    });
    
    var storePagoIndemnizatorio =new Ext.data.Store({
        autoDestroy: true,
        model: 'modelFacturaSiniestro'
    });
    
    var storePagoIndemnizatorioRecupera =new Ext.data.Store({
        autoDestroy: true,
        model: 'modelFacturaSiniestro'
    });

    var storeListAsegPagDirecto=new Ext.data.Store({
        autoDestroy: true,                      model: 'modelListAsegPagDirecto'
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
            extraParams: {
                'params.cdunieco' : null,
                'params.cdramo'   : null,
                'params.estado'   : null,
                'params.nmpoliza' : null,
                'params.cdperson' : null
            },
            reader: {
                type: 'json',
                root: 'listaAsegurado'
            }
        },
        listeners: {
            load: function(st, recs, succ, eOpts) {
                debug('en load', st);
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
        pageSize : 5
        ,model     : 'modelListadoPoliza'
        ,autoLoad  : false
        ,proxy     : {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
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
    //storeModalidad.load();
    
    cmbRamos = Ext.create('Ext.form.field.ComboBox',{
        colspan:2,                              fieldLabel   : 'Producto',          allowBlank     : false,     editable   : false,
        displayField: 'value',                  valueField: 'key',                  forceSelection : false,     queryMode :'local',
        width: 300,                             name:'cmbRamos',                    store          : storeRamos,

        listeners : {
            'select':function(e){
                panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(null);
                panelInicialPral.down('combo[name=cmbTipoPago]').setValue(null);
                panelInicialPral.down('combo[name=pv_cdtipsit_i]').setValue(null);
                storeTipoPago.removeAll();
                storeTipoPago.load({
                    params:{
                        'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue()
                    }
                });
                
                storeModalidad.removeAll();
                storeModalidad.load({
                    params:{
                        'params.idPadre':panelInicialPral.down('combo[name=cmbRamos]').getValue()
                    }
                });
                
                
                storeTipoAtencion.load({
                    params:{
                        'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                        'params.cdtipsit': panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                        'params.tipoPago':panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                    }
                });
                
                if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                    panelInicialPral.down('combo[name=cmbTipoPago]').setValue('3');
                    panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue('9');
                }
            }
        }
    });
    
    cmbModalidad = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel   : 'Modalidad',         allowBlank     : false,     editable   : false,
        displayField: 'value',              valueField: 'key',          forceSelection : false,             queryMode :'local',
        width: 300,                         name:'pv_cdtipsit_i',       store          : storeModalidad,
        listeners : {
            'change':function(e){
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
    });
    
    var tipoPago = Ext.create('Ext.form.ComboBox',{
        name:'cmbTipoPago',                     fieldLabel: 'Tipo pago',            allowBlank:false,           editable:false,
        displayField: 'value',                  valueField: 'key',                  queryMode:'local',          emptyText:'Seleccione...',
        width        : 300,                     store: storeTipoPago,
        listeners : {
            'change':function(e){
                panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue(null);
                storeTipoAtencion.load({
                    params:{
                        'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                        'params.cdtipsit': panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                        'params.tipoPago':panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
                    }
                });
                
                if(e.getValue() == _TIPO_PAGO_DIRECTO){
                    //PAGO DIRECTO
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
                
                
                
                

                
                
                /*if(e.getValue() == _TIPO_PAGO_DIRECTO){
                    //PAGO DIRECTO
                    limpiarRegistrosTipoPago(e.getValue());
                    panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
                }else if(e.getValue() == _TIPO_PAGO_REEMBOLSO){
                    //PAGO POR REEMBOLSO
                    limpiarRegistrosTipoPago(e.getValue());
                    if(panelInicialPral.down('combo[name=cmbOficReceptora]').getValue() == "1104"){
                        panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1104");
                    }else{
                        panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
                    }
                }else{
                    //PAGO POR REEMBOLSO
                    limpiarRegistrosTipoPago(e.getValue());
                    if(panelInicialPral.down('combo[name=cmbOficReceptora]').getValue() == "1104"){
                        panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1104");
                    }else{
                        panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
                    }
                }*/
            }
        }
    });
    
    var comboTipoAte = Ext.create('Ext.form.ComboBox',{
        name:'cmbTipoAtencion',                 fieldLabel: 'Tipo atenci&oacute;n', allowBlank : false,         editable:true,
        displayField: 'value',                  emptyText:'Seleccione...',          valueField: 'key',          forceSelection : true,
        width        : 300,                     queryMode      :'local',            store: storeTipoAtencion,
        listeners : {
            'change':function(e){
                if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                    panelInicialPral.down('combo[name=cmbTipoAtencion]').setValue('9');
                }
            }
        }
    });
    
    var cmbOficinaReceptora = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel      : 'Oficina receptora',  name        : 'cmbOficReceptora',   allowBlank  : false,        editable        : true,
        displayField    : 'value',              emptyText   :'Seleccione...',       valueField  : 'key',        forceSelection  : true,
        width           : 300,                  queryMode   :'local',               store       : oficinaReceptora
    });
    
    var cmbOficinaEmisora = Ext.create('Ext.form.field.ComboBox',{
        fieldLabel : 'Oficina emisora',         name         : 'cmbOficEmisora',    allowBlank : false,         editable     : true,
        displayField : 'value',                 emptyText:'Seleccione...',          valueField   : 'key',       forceSelection : true,
        width        : 300,                     queryMode      :'local',                store        : oficinaEmisora
    });
    
    aseguradoAfectado = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel : 'Asegurado afectado',      displayField : 'value',         name:'cmbAseguradoAfectado',    valueField   : 'key',
        forceSelection : true,                  matchFieldWidth: false,         queryMode :'remote',            queryParam: 'params.cdperson',
        minChars  : 2,                          store : storeAsegurados,        triggerAction: 'all',           hideTrigger:true,   allowBlank:false,
        width        : 300,                     
        listeners : {
            'select' : function(combo, record) {
                    obtieneCDPerson = this.getValue();
                    panelInicialPral.down('[name=idnombreAsegurado]').setValue(aseguradoAfectado.rawValue);
                   
                    var params = {
                            'params.cdperson' : obtieneCDPerson,
                            'params.cdramo' : panelInicialPral.down('combo[name=cmbRamos]').getValue()
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
    
    
    
    var cmbBeneficiario= Ext.create('Ext.form.ComboBox',{
        name            :'cmbBeneficiario',         
        fieldLabel      : 'Beneficiario',           
        queryMode       : 'remote',     
        displayField    : 'value',
        valueField      : 'key',
        store           : storeAsegurados2,
        width           : 300,
        allowBlank      : false,
        queryDelay      : 1000,
        forceSelection  : true,
        queryCaching    : false,
        //hideTrigger       : true,
        autoSelect      : false,
        enableKeyEvents : true,
        /*      
        allQuery        : 'dummyForAllQuery',       
        typeAhead       : false,
        anyMatch        : false,        
        */
        listeners : {
            'change' : function(e) {
                debug('en change', e.getValue());
                //if(e.getValue().length == 5){
                    //storeAsegurados2.removeAll();
                    storeAsegurados2.proxy.extraParams=
                    {
                        'params.cdunieco': panelInicialPral.down('[name="cdunieco"]').getValue(),
                        'params.cdramo': panelInicialPral.down('[name="cdramo"]').getValue(),
                        'params.estado': panelInicialPral.down('[name="estado"]').getValue(),
                        'params.nmpoliza': panelInicialPral.down('[name="polizaAfectada"]').getValue(),
                        'params.cdperson': e.getValue()
                    };
                    //storeAsegurados2.load();
                //}
            },
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
        fieldLabel : 'Proveedor',       displayField : 'nombre',        name:'cmbProveedor',        valueField   : 'cdpresta',
        forceSelection : true,          matchFieldWidth: false,         queryMode :'remote',        queryParam: 'params.cdpresta',
        minChars    : 2,                store : storeProveedor,         triggerAction: 'all',       hideTrigger:true,   allowBlank:false,
        colspan:2,
        width       : 300,
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
    
    cmbTipoMoneda = Ext.create('Ext.form.ComboBox',{
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
    
    cmbProveedorReembolso = Ext.create('Ext.form.field.ComboBox', {
        displayField : 'nombre',        name:'cmbProveedorReembolso',   valueField   : 'cdpresta',
        forceSelection : true,          matchFieldWidth: false,         queryMode :'remote',        queryParam: 'params.cdpresta',
        width        : 300,             minChars  : 2,                  store : storeProveedor,     triggerAction: 'all',
        hideTrigger:true,   allowBlank:false
    });

    /*////////////////////////////////////////////////////////////////
    ////////////////   DECLARACION DE EDITOR DE INCISOS  ////////////
    ///////////////////////////////////////////////////////////////*/

    //1.- GRID�S PARA EL PAGO DIRECTO
    Ext.define('EditorFacturaDirecto', {
        extend: 'Ext.grid.Panel',
        name:'editorFacturaDirecto',
        title: 'Alta de facturas Pago Directo',
        frame: true,
        selType  : 'rowmodel',
        initComponent: function(){
                Ext.apply(this, {
                width: 750,
                height: 250
                ,plugins  : [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1
                        ,listeners : {
                            beforeedit : function() {
                                valorIndexSeleccionado = gridFacturaDirecto.getView().getSelectionModel().getSelection()[0];
                                debug('valorIndexSeleccionado:',valorIndexSeleccionado);
                            }
                        }
                    })
                ],
                store: storeFacturaDirecto,
                columns: 
                [
                    {   header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2,  allowBlank: false
                        ,editor: {      
                                xtype: 'textfield',
                                editable : true,
                                allowBlank: false
                        }
                    },
                    {
                        header: 'Fecha de Factura',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y'),  allowBlank: false
                        ,editor : {
                            xtype : 'datefield',
                            format : 'd/m/Y',
                            editable : true,
                            allowBlank: false
                        }
                    },
                    {
                        header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2,  allowBlank: false
                        ,renderer : function(v) {
                        var leyenda = '';
                            if (typeof v == 'string') {
                                storeTipoMoneda.each(function(rec) {
                                    if (rec.data.key == v) {
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
                    {   header: 'Tasa cambio',              dataIndex: 'tasaCambio',        flex:2,             renderer: Ext.util.Format.usMoney,  allowBlank: false       },
                    {   header: 'Importe Factura',          dataIndex: 'importeFactura',    flex:2,             renderer: Ext.util.Format.usMoney,  allowBlank: false       },
                    {   header: 'Importe MXN',              dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney,  allowBlank: false
                        ,editor: {
                            xtype: 'textfield',             allowBlank: false,              editable : true,    minValue: 1,
                            listeners : {
                                change:function(e){
                                    validarFacturaPagada(panelInicialPral.down('combo[name=cmbProveedor]').getValue() ,valorIndexSeleccionado.get('noFactura'), e.getValue());
                                }
                            }
                        }
                    },
                    {   xtype: 'actioncolumn',          width: 30,      sortable: false,        menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: 'Quitar inciso',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    }
                ],
                selModel: {
                    selType: 'cellmodel'
                },
                tbar: [{
                    text     : 'Agregar Factura'
                    ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                    ,handler : _p21_agregarGrupoClic
                }]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
        }
    });
    gridFacturaDirecto=new EditorFacturaDirecto();
    //2.- GRID�S PARA EL PAGO REEMBOLSO
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
                                debug('valorIndexSeleccionado:',valorIndexSeleccionado);
                            }
                        }
                    })
                ],
                store: storeFacturaReembolso,
                columns: 
                [
                    {   xtype: 'actioncolumn',          width: 40,           sortable: false,           menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: 'Quitar inciso',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    },
                    {   header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2
                        ,editor: {
                                xtype: 'textfield',
                                allowBlank: false
                        }
                    },
                    {   header: 'Fecha de Factura',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y')
                        ,editor : {
                            xtype : 'datefield',
                            format : 'd/m/Y',
                            editable : true
                        }
                    },
                    {   header: 'Proveedor',                dataIndex: 'proveedorName', flex:2
                        ,editor : cmbProveedorReembolso
                        ,renderer : function(v) {
                        var leyenda = '';
                            if (typeof v == 'string'){
                                storeProveedor.each(function(rec) {
                                    if (rec.data.cdpresta == v) {
                                        leyenda = rec.data.nombre;
                                    }
                                });
                            }else{
                                if (v.key && v.value) {
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    },
                    {   header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2
                        ,editor : cmbTipoMoneda
                        ,renderer : function(v) {
                            var leyenda = '';
                            if (typeof v == 'string'){
                                storeTipoMoneda.each(function(rec) {
                                    if (rec.data.key == v) {
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
                    {   header: 'Tasa cambio',              dataIndex: 'tasaCambio',    flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   header: 'Importe Factura',              dataIndex: 'importeFactura',            flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   header: 'Importe MXN',                  dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   text     : 'Agregar Factura'
                        ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                        ,handler : _p21_agregarGrupoClic
                    }
                ]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
        }
    });
    gridFacturaReembolso =new EditorFacturaReembolso();
    //3.- GRID�S PARA EL PAGO INDEMNIZATORIO GMMI
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
                                beforeedit : function(){
                                    valorIndexSeleccionado = gridPagoIndemnizatorio.getView().getSelectionModel().getSelection()[0];
                                    debug('valorIndexSeleccionado:',valorIndexSeleccionado);
                                }
                            }
                    })
                ],
                store: storePagoIndemnizatorio,
                columns: 
                [
                    {   xtype: 'actioncolumn',      width: 40,          sortable: false,            menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: 'Quitar inciso',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    },
                    {   header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2,         hidden:true                 },
                    {   header: 'Fecha de Ingreso',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y')
                        ,editor : {
                            xtype : 'datefield',
                            format : 'd/m/Y',
                            editable : true,
                            allowBlank: false
                        }
                    },
                    {   header: 'Proveedor',                dataIndex: 'proveedorName', flex:2
                        ,editor : cmbProveedorReembolso
                        ,renderer : function(v) {
                        var leyenda = '';
                            if (typeof v == 'string'){
                                storeProveedor.each(function(rec) {
                                    if (rec.data.cdpresta == v) {
                                        leyenda = rec.data.nombre;
                                    }
                                });
                            }else {
                                if (v.key && v.value){
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    },
                    {   header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2
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
                                if (v.key && v.value){
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    },
                    {   header: 'Tasa cambio',              dataIndex: 'tasaCambio',    flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   header: 'Importe Factura',              dataIndex: 'importeFactura',            flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   header: 'Importe MXN',                  dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   text     : 'Agregar Documento'
                        ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                        ,handler : _p21_agregarGrupoClic
                    }
                ]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
        }
    });
    gridPagoIndemnizatorio =new EditorPagoIndemnizatorio();
    //4.- GRID�S PARA EL PAGO INDEMNIZATORIO RECUPERA
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
                                beforeedit : function(){
                                    valorIndexSeleccionado = gridPagoIndemnizatorioRecupera.getView().getSelectionModel().getSelection()[0];
                                    debug('valorIndexSeleccionado:',valorIndexSeleccionado);
                                }
                            }
                    })
                ],
                store: storePagoIndemnizatorioRecupera,
                columns: 
                [
                    {   xtype: 'actioncolumn',      width: 40,          sortable: false,            menuDisabled: true,
                        items: [{
                            icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                            tooltip: 'Quitar inciso',
                            scope: this,
                            handler: this.onRemoveClick
                        }]
                    },
                    {   header: 'No. de Factura',           dataIndex: 'noFactura',         flex:2,         hidden:true                 },
                    {   header: 'Fecha de Ingreso',         dataIndex: 'fechaFactura',      flex:2,             renderer: Ext.util.Format.dateRenderer('d/m/Y')
                        ,editor : {
                            xtype : 'datefield',
                            format : 'd/m/Y',
                            editable : true,
                            allowBlank: false
                        }
                    },
                    {   header: 'Proveedor',            dataIndex: 'proveedorName',         flex:2
                        ,editor: {
                            xtype: 'textfield',
                            allowBlank: false
                        }
                    },
                    {   header: 'Moneda',               dataIndex: 'tipoMonedaName',    flex:2
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
                                if (v.key && v.value){
                                    leyenda = v.value;
                                } else {
                                    leyenda = v.data.value;
                                }
                            }
                            return leyenda;
                        }
                    },
                    {   header: 'Tasa cambio',              dataIndex: 'tasaCambio',    flex:2,             renderer: Ext.util.Format.usMoney
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
                                            //validarFacturaPagada(valorIndexSeleccionado.get('proveedorName') ,valorIndexSeleccionado.get('noFactura'), valorIndexSeleccionado.get('importe'));
                                        }
                                    }
                                }
                        }
                    },
                    {   header: 'Importe Factura',              dataIndex: 'importeFactura',            flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   header: 'Importe MXN',                  dataIndex: 'importe',           flex:2,             renderer: Ext.util.Format.usMoney
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
                    {   text     : 'Agregar Documento'
                        ,icon:_CONTEXT+'/resources/fam3icons/icons/book.png'
                        ,handler : _p21_agregarGrupoClic
                    }
                ]
            });
            this.callParent();
        },
        onRemoveClick: function(grid, rowIndex){
            var record=this.getStore().getAt(rowIndex);
            this.getStore().removeAt(rowIndex);
        }
    });
    gridPagoIndemnizatorioRecupera =new EditorPagoIndemnizatorioRecupera();
    
    /* PANEL PARA LA BUSQUEDA DE LA INFORMACI�N DEL ASEGURADO PARA LA BUSQUEDA DE LAS POLIZAS */
    gridPolizasAltaTramite= Ext.create('Ext.grid.Panel', {
        id            : 'polizaGridAltaTramite',
        store         : storeListadoPoliza,
        selType       : 'checkboxmodel',
        width         : 700,
        height        : 200,
        columns       : [
            {    header     : 'N&uacute;mero de P&oacute;liza',                                     dataIndex : 'numPoliza',            width       : 200       },
            {    header     : 'Estatus p&oacute;liza ',                                             dataIndex : 'dsestatus',            width       : 100       },
            {    header     : 'Vigencia p&oacute;liza <br/> Fecha inicio \t\t  |  \t\t Fecha fin  ',dataIndex : 'vigenciaPoliza',       width       : 200       },
            {    header     : 'Fecha alta <br/> asegurado',                                         dataIndex : 'faltaAsegurado',       width       : 100       },
            {    header     : 'Fecha cancelaci&oacute;n <br/> asegurado',                           dataIndex : 'fcancelacionAfiliado', width       : 150       },
            {    header     : 'Estatus<br/> asegurado',                                             dataIndex : 'desEstatusCliente',    width       : 100       },
            {    header     : 'Producto',                                                           dataIndex : 'dsramo',               width       : 150       },
            {    header     : 'Sucursal',                                                           dataIndex : 'dssucursal',           width       : 150       },
            {    header     : 'Estado',                                                             dataIndex : 'estado',               width       : 100       },
            {    header     : 'N&uacute;mero de Situaci&oacute;n',                                  dataIndex : 'nmsituac',             width       : 150       }
        ],
        bbar : {
            displayInfo : true,
            store       : storeListadoPoliza,
            xtype       : 'pagingtoolbar'
        },
        listeners: {
                itemclick: function(dv, record, item, index, e){
                    //1.- Validamos que el asegurado este vigente
                    if(record.get('desEstatusCliente') == "Vigente")
                    {
                        var valorFechaOcurrencia;
                        if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
                            var valorFechaOcu = panelListadoAsegurado.query('datefield[name=dtfechaOcurrencias]')[0].rawValue;
                            valorFechaOcurrencia = new Date(valorFechaOcu.substring(6,10)+"/"+valorFechaOcu.substring(3,5)+"/"+valorFechaOcu.substring(0,2));
                        }else{
                            valorFechaOcurrencia = panelInicialPral.down('[name=dtFechaOcurrencia]').getValue();
                        }
                        
                        var valorFechaInicial = new Date(record.get('feinicio').substring(6,10)+"/"+record.get('feinicio').substring(3,5)+"/"+record.get('feinicio').substring(0,2));
                        var valorFechaFinal =   new Date(record.get('fefinal').substring(6,10)+"/"+record.get('fefinal').substring(3,5)+"/"+record.get('fefinal').substring(0,2));
                        var valorFechaAltaAsegurado = new Date(record.get('faltaAsegurado').substring(6,10)+"/"+record.get('faltaAsegurado').substring(3,5)+"/"+record.get('faltaAsegurado').substring(0,2));
                        
                        
		                Ext.Ajax.request({
		                    url     : _URL_VALIDA_STATUSASEG
		                    ,params:{
		                        'params.cdperson'  : panelInicialPral.down('combo[name=cmbAseguradoAfectado]').getValue(),
		                        'params.feoocurre' : valorFechaOcurrencia,
		                        'params.nmpoliza'  : record.get('nmpoliza')
		                    }
		                    ,success : function (response) {
		                        json = Ext.decode(response.responseText);
		                         if(Ext.decode(response.responseText).validacionGeneral =="V"){
			                        if( (valorFechaOcurrencia <= valorFechaFinal) && (valorFechaOcurrencia >= valorFechaInicial)){
			                            if( valorFechaOcurrencia >= valorFechaAltaAsegurado ){
			                                    //cumple la condici�n la fecha de ocurrencia es menor igual a la fecha de alta de tramite
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
			                                    panelInicialPral.down('[name="txtAutEspecial"]').setValue("0");
			                                    modPolizasAltaTramite.hide();
			                            }else{
			                                // No se cumple la condici�n la fecha de ocurrencia es mayor a la fecha de alta de tramite
			                                Ext.Msg.show({
			                                    title:'Error',
			                                    msg: 'La fecha de ocurrencia es mayor a la fecha de alta del asegurado',
			                                    buttons: Ext.Msg.OK,
			                                    icon: Ext.Msg.ERROR
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
			                                        panelInicialPral.down('[name="txtAutEspecial"]').setValue("1");
			                                        modPolizasAltaTramite.hide();
			                                    }else{
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
		                                        panelInicialPral.down('[name="txtAutEspecial"]').setValue("1");
		                                        modPolizasAltaTramite.hide();
		                                    }else{
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
                            icon: Ext.Msg.ERROR
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
    
    var modPolizasAltaTramite = Ext.create('Ext.window.Window',
    {
        title        : 'Listado de P&oacute;liza'
        ,modal       : true
        ,resizable   : false
        ,buttonAlign : 'center'
        ,closable    : false
        ,width       : 710
        ,minHeight   : 100 
        ,maxheight   : 400
        ,items       : [
                gridPolizasAltaTramite
            ]
    });
    
    var panelInicialPral= Ext.create('Ext.form.Panel',
            {
                border      : 0,
                id          : 'panelInicialPral',
                renderTo    : 'div_clau',
                bodyPadding : 5,
                width       : 758,
                layout      : {
                    type    : 'table'
                    ,columns: 2
                },
                defaults    : {
                    style   : 'margin:5px;'
                },
                items       :[
                    {    xtype       : 'textfield',         fieldLabel : 'Unieco'               ,name       : 'cdunieco'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'estado'               ,name       : 'estado'          
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'Ramo'                 ,name       : 'cdramo'
                        ,labelWidth: 170,                   hidden:true
                    },              
                    {    xtype       : 'textfield',         fieldLabel : 'nmsituac'             ,name       : 'nmsituac'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {   xtype       : 'textfield',          fieldLabel : 'P&oacute;liza afectada'
                       ,labelWidth: 170,                    name:'polizaAfectada',  readOnly   : true,  hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'nmsolici'             ,name       : 'idNmsolici'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'nmsuplem'             ,name       : 'idNmsuplem'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'cdtipsit'             ,name       : 'idCdtipsit'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'numPolizaInt'         ,name       : 'idNumPolizaInt'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'Asegurado'            ,name       : 'idnombreAsegurado'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'NumTramite'           ,name       : 'idNumTramite'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'ImporteFactura'       ,name       : 'ImporteIndFactura'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {   xtype       : 'datefield',          fieldLabel : 'fechaIndFactura'      ,name       : 'fechaIndFactura'
                        ,format     : 'd/m/Y',              hidden      :true
                    },
                    {    xtype       : 'textfield',         fieldLabel : 'No.Factura'           ,name       : 'numIndFactura'
                        ,labelWidth: 170,                   hidden:true
                    },
                    {   xtype: 'textfield',                 fieldLabel: 'Estado'                ,name       : 'txtEstado'
                        ,readOnly   : true,                 width        : 300,                 value       :'PENDIENTE'
                    },
                    cmbRamos,
                    cmbModalidad,
                    tipoPago,
                    //tipoPago2,
                    comboTipoAte,
                    cmbOficinaReceptora,
                    cmbOficinaEmisora,
                    {   xtype      : 'datefield',           fieldLabel  : 'Fecha recepci&oacute;n', width      : 300,       name       : 'dtFechaRecepcion',
                        value      : new Date(),            format      : 'd/m/Y',                  readOnly   : true,      allowBlank : false
                    },
                    {   xtype      : 'datefield',           fieldLabel  : 'Fecha ocurrencia',       name        : 'dtFechaOcurrencia',      maxValue   :  new Date(),
                        format     : 'd/m/Y',               editable    : true,                     width       : 300,                      allowBlank : false
                    },
                    aseguradoAfectado,
                    cmbBeneficiario,
                    {   xtype: 'numberfield',               fieldLabel: 'Id. Beneficiario'              ,name       : 'idCveBeneficiario' 
                        ,width       : 300,                 allowBlank : false
                    },
                    cmbProveedor,
                    {   colspan :2,     xtype   : 'textfield',  fieldLabel : 'Nombre Proveedor',    name : 'idnombreBeneficiarioProv',      width   : 300,
                        listeners:{
                            afterrender: function(){
                                this.hide();
                            }
                        }
                    },
                    {   xtype: 'numberfield',               fieldLabel: 'Tel&eacute;fono'               ,name       : 'txtTelefono' 
                        ,width       : 300,                 allowBlank : false
                    },
                    {   xtype: 'textfield',                 fieldLabel: 'Correo Electr&oacute;nico' ,   name        : 'txtEmail'
                        ,width       : 300,                 allowBlank : false,                         vtype: 'email'
                    },
                    {   xtype: 'textfield',                 fieldLabel: 'Autorizacion'  ,name       : 'txtAutEspecial'
                        ,width       : 300,                 value: "0",         hidden:true
                    },
                    {   colspan:2
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
                buttons: [{
                    id:'botonCotizar',
                    icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                    text: 'Generar Tr&aacute;mite',
                    handler: function() {
                        
                        var form = this.up('form').getForm();
                        if (form.isValid()){
                            
                            if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){ // Pago Directo
                                panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
                                var obtener = [];
                                storeFacturaDirecto.each(function(record) {
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
                                    if(obtener.length == 1){
                                        panelInicialPral.down('[name=ImporteIndFactura]').setValue(obtener[0].importe);
                                        panelInicialPral.down('[name=fechaIndFactura]').setValue(obtener[0].fechaFactura);
                                        panelInicialPral.down('[name=numIndFactura]').setValue(obtener[0].noFactura);
                                    }else{
                                        panelInicialPral.down('[name=ImporteIndFactura]').setValue('');
                                        panelInicialPral.down('[name=fechaIndFactura]').setValue('');
                                        panelInicialPral.down('[name=numIndFactura]').setValue('');
                                        
                                        var sumaTotal= 0;
                                        for(i=0;i < obtener.length;i++){
                                                sumaTotal =sumaTotal + (+ obtener[i].importe);
                                                panelInicialPral.down('[name=ImporteIndFactura]').setValue(sumaTotal);
                                        }
                                    }
                                    //Se tiene que validar el record de las facturas
                                    for(i=0;i < obtener.length;i++){
                                        if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
                                            obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""){
                                            Ext.Msg.show({
                                                title:'Facturas',
                                                msg: 'Favor de introducir los campos requeridos en la factura',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.WARNING
                                            });
                                            return false;
                                        }
                                    }
                                    
                                }

                            }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
                                //PAGO POR REEMBOLSO
                                //Validamos Beneficiario. (EGS)
                            	if(panelInicialPral.down('combo[name=cmbBeneficiario]').value == null){
                            		Ext.Msg.show({
                            			title:	'Aviso',
                            			msg	 :	'Dato no valido en campo Beneficiario. Seleccione un elemento de la lista desplegable',
                            			buttons:Ext.Msg.OK,
                            			icon:	Ext.Msg.INFO
                            		});
                            		return false;
                            	} // fin (EGS)
                                var obtener = [];
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
                                    if(obtener.length == 1){
                                        panelInicialPral.down('combo[name=cmbProveedor]').setValue(obtener[0].proveedorName);
                                        panelInicialPral.down('[name=fechaIndFactura]').setValue(obtener[0].fechaFactura);
                                        panelInicialPral.down('[name=numIndFactura]').setValue(obtener[0].noFactura);
                                        
                                    }else{
                                        panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                                        panelInicialPral.down('[name=fechaIndFactura]').setValue('');
                                        panelInicialPral.down('[name=numIndFactura]').setValue('');
                                    }
                                    var sumaTotal= 0;
                                    for(i=0;i < obtener.length;i++){
                                            sumaTotal =sumaTotal + (+ obtener[i].importe);
                                            panelInicialPral.down('[name=ImporteIndFactura]').setValue(sumaTotal);
                                    }
                                    
                                    if(panelInicialPral.down('combo[name=cmbOficReceptora]').getValue() == "1104"){
                                        if(panelInicialPral.down('[name=ImporteIndFactura]').getValue() <= "5000"){
                                            panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1104");
                                        }else{
                                            panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
                                        }
                                    }
                                    
                                    for(i=0;i < obtener.length;i++){
                                        if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
                                            obtener[i].importeFactura == null ||obtener[i].proveedorName == null ||obtener[i].tasaCambio == null ||
                                            obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""||
                                            obtener[i].importeFactura == "" ||obtener[i].proveedorName == "" ||obtener[i].tasaCambio == ""){
                                            Ext.Msg.show({
                                                title:'Facturas',
                                                msg: 'Favor de introducir los campos requeridos en la factura',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.WARNING
                                            });
                                            return false;
                                        }
                                    }
                                }
                            }else{ //PAGO POR INDEMNIZACION
                                var obtener = [];
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
                                            msg: 'Se requiere ingresar al menos una documento',
                                            buttons: Ext.Msg.OK,
                                            icon: Ext.Msg.ERROR
                                        });
                                        return false;
                                }else{
                                    if(obtener.length == 1){
                                        panelInicialPral.down('combo[name=cmbProveedor]').setValue(obtener[0].proveedorName);
                                        panelInicialPral.down('[name=fechaIndFactura]').setValue(obtener[0].fechaFactura);
                                        panelInicialPral.down('[name=numIndFactura]').setValue(obtener[0].noFactura);
                                        
                                    }else{
                                        panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                                        panelInicialPral.down('[name=fechaIndFactura]').setValue('');
                                        panelInicialPral.down('[name=numIndFactura]').setValue('');
                                    }
                                    var sumaTotal= 0;
                                    for(i=0;i < obtener.length;i++){
                                            sumaTotal =sumaTotal + (+ obtener[i].importe);
                                            panelInicialPral.down('[name=ImporteIndFactura]').setValue(sumaTotal);
                                    }
                                    
                                    for(i=0;i < obtener.length;i++){
                                        if(obtener[i].noFactura == null ||obtener[i].fechaFactura == null ||obtener[i].importe == null ||
                                            obtener[i].importeFactura == null ||obtener[i].proveedorName == null ||obtener[i].tasaCambio == null ||
                                            obtener[i].noFactura == "" ||obtener[i].fechaFactura == "" ||obtener[i].importe == ""||
                                            obtener[i].importeFactura == "" ||obtener[i].proveedorName == "" ||obtener[i].tasaCambio == ""){
                                            Ext.Msg.show({
                                                title:'Facturas',
                                                msg: 'Favor de introducir los campos requeridos en la factura',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.WARNING
                                            });
                                            return false;
                                        }
                                    }
                                }
                            }
                            var submitValues={};
                            var formulario=panelInicialPral.form.getValues();
                            submitValues['params']=formulario;
                            
                            var datosTablas = [];
                            
                            if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO) //PAGO DIRECTO
                            {
                                //Pago directo
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
                            }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
                                //Pago por Reembolso
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
                            }
                            else{
                                //Pago Indemnizatorios
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
                            }
                            submitValues['datosTablas']=datosTablas;
                            panelInicialPral.setLoading(true);
                            submitValues.params['caseIdRstn'] = _NVL(valorAction.caseIdRstn);
                            debug("VALORES A ENVIAR A GUARDAR --->");
                            debug(submitValues);
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
                                        if(valorAction.ntramite == null) {
                                            etiqueta = "Guardado";
                                            mensaje = "Se gener&oacute; el n&uacute;mero de tr&aacute;mite "+ Ext.decode(response.responseText).msgResult; 
                                        }else{
                                            etiqueta = "Modificaci&oacute;n";
                                            mensaje = "Se modific&oacute; el n&uacute;mero de tr&aacute;mite "+ valorAction.ntramite;
                                        }
                                        var ntramiteEnviarRstn = valorAction.ntramite;
                                        if (Ext.isEmpty(ntramiteEnviarRstn)) {
                                            ntramiteEnviarRstn = Ext.decode(response.responseText).msgResult;
                                        }
                                        mensajeCorrecto(etiqueta,mensaje,function() {
                                            /*
                                            Ext.create('Ext.form.Panel').submit( {
                                                url             : _p12_urlMesaControl
                                                ,standardSubmit : true
                                                ,params         : {
                                                    'smap1.gridTitle'      : 'Siniestros en espera'
                                                    ,'smap2.pv_cdtiptra_i' : 16
                                                }
                                            });
                                            */
                                            _mask();
                                            location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?ntramite=' + ntramiteEnviarRstn;
                                        });
                                        panelInicialPral.getForm().reset();
                                        storeFacturaDirecto.removeAll();
                                        windowLoader.close();
                                    }
                                    else{
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
                        else {
                            Ext.Msg.show({
                                title:'Datos incompletos',
                                msg: 'Favor de introducir todos los campos requeridos',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                        }
                    }
                },
                {
                    text:'Cancelar',
                    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                    handler:function(){
                        windowLoader.close();
                    }
                }
            ]
            });
    
    if(valorAction.ntramite == null)
    {
        rolSiniestro = valorAction.RolSiniestro;
        /*Oficina Receptora*/
        oficinaReceptora.load();
        panelInicialPral.down('combo[name=cmbOficReceptora]').setValue(valorAction.cdunieco);
        panelInicialPral.down('[name=idCveBeneficiario]').hide();
        
        if(rolSiniestro =="INPLANTSSI"){
            /*Seleccionamos el producto Salud vital*/
            panelInicialPral.down('combo[name=cmbRamos]').setValue(_MULTISALUD);
            /*Modalidad*/
            storeModalidad.removeAll();
            storeModalidad.load({
                params:{
                    'params.idPadre':_MULTISALUD
                }
            });
            panelInicialPral.down('combo[name=pv_cdtipsit_i]').setValue('SSI');
        }else{
            /*Seleccionamos el producto Salud vital*/
            panelInicialPral.down('combo[name=cmbRamos]').setValue(_SALUD_VITAL);
            /*Modalidad*/
            storeModalidad.removeAll();
            storeModalidad.load({
                params:{
                    'params.idPadre':_SALUD_VITAL
                }
            });
            panelInicialPral.down('combo[name=pv_cdtipsit_i]').setValue('SL');
        }
        
        /*Tipo de pago*/
        storeTipoPago.removeAll();
        storeTipoPago.load({
            params:{
                'params.cdramo':panelInicialPral.down('combo[name=cmbRamos]').getValue()
            }
        });
        panelInicialPral.down('combo[name=cmbTipoPago]').setValue(_TIPO_PAGO_DIRECTO);
        /*Tipo de atencion*/
        storeTipoAtencion.load({
            params:{
                'params.cdramo'  : panelInicialPral.down('combo[name=cmbRamos]').getValue(),
                'params.cdtipsit': panelInicialPral.down('combo[name=pv_cdtipsit_i]').getValue(),
                'params.tipoPago': panelInicialPral.down('combo[name=cmbTipoPago]').getValue()
            }
        });
        
        
        limpiarRegistrosTipoPago(panelInicialPral.down('combo[name=cmbTipoPago]').getValue());
        /*Oficina Emisora*/
        oficinaEmisora.load();
        if(panelInicialPral.down('combo[name=cmbOficReceptora]').getValue() == "1104"  && panelInicialPral.down('combo[name=cmbTipoPago]').getValue() ==_TIPO_PAGO_DIRECTO){
            panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1104");
        }else{
            panelInicialPral.down('combo[name=cmbOficEmisora]').setValue("1000");
        }
    }
    
//////////////////          FUNCIONES       /////////////////////
    function limpiarRegistros() {
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
        if(tipoPago == _TIPO_PAGO_DIRECTO) {// Pago Directo
            pagoDirecto = false;
            pagoReembolso = true;
            panelInicialPral.down('[name=editorFacturaDirecto]').show();
            panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
            panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
            panelInicialPral.down('[name=editorFacturaReembolso]').hide();
            panelInicialPral.down('combo[name=cmbBeneficiario]').hide();
            //panelInicialPral.down('[name=idCveBeneficiario]').hide();
            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').hide();
            panelInicialPral.down('[name=dtFechaOcurrencia]').hide();
            panelInicialPral.down('combo[name=cmbBeneficiario]').setValue('');
            panelInicialPral.down('[name=idCveBeneficiario]').setValue('');
            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').setValue('');
            panelInicialPral.down('[name=dtFechaOcurrencia]').setValue('');
            panelInicialPral.down('combo[name=cmbProveedor]').show();
            panelInicialPral.down('[name=txtTelefono]').hide();
            panelInicialPral.down('[name=txtEmail]').hide();
        }else if(tipoPago == _TIPO_PAGO_REEMBOLSO){
            pagoDirecto = true;
            pagoReembolso = false;
            panelInicialPral.down('[name=editorFacturaDirecto]').hide();
            panelInicialPral.down('[name=editorFacturaReembolso]').show();
            panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
            panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
            panelInicialPral.down('combo[name=cmbProveedor]').hide();
            panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
            panelInicialPral.down('[name=idnombreBeneficiarioProv]').hide();
            panelInicialPral.down('combo[name=cmbBeneficiario]').show();
            //panelInicialPral.down('[name=idCveBeneficiario]').show();
            panelInicialPral.down('combo[name=cmbAseguradoAfectado]').show();
            panelInicialPral.down('[name=dtFechaOcurrencia]').show();
            panelInicialPral.down('[name=txtTelefono]').show();
            panelInicialPral.down('[name=txtEmail]').show();
        }else{
            pagoDirecto = true;
            pagoReembolso = false;
            if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                panelInicialPral.down('[name=editorFacturaDirecto]').hide();
                panelInicialPral.down('[name=editorFacturaReembolso]').hide();
                panelInicialPral.down('[name=editorPagoIndemnizatorio]').hide();
                panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').show();
                panelInicialPral.down('combo[name=cmbProveedor]').hide();
                panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                panelInicialPral.down('[name=idnombreBeneficiarioProv]').hide();
                panelInicialPral.down('combo[name=cmbBeneficiario]').show();
                //panelInicialPral.down('[name=idCveBeneficiario]').show();
                panelInicialPral.down('combo[name=cmbAseguradoAfectado]').show();
                panelInicialPral.down('[name=dtFechaOcurrencia]').show();
                panelInicialPral.down('[name=txtTelefono]').show();
                panelInicialPral.down('[name=txtEmail]').show();
            }else{
                panelInicialPral.down('[name=editorFacturaDirecto]').hide();
                panelInicialPral.down('[name=editorFacturaReembolso]').hide();
                panelInicialPral.down('[name=editorPagoIndemnizatorioRecupera]').hide();
                panelInicialPral.down('[name=editorPagoIndemnizatorio]').show();
                panelInicialPral.down('combo[name=cmbProveedor]').hide();
                panelInicialPral.down('combo[name=cmbProveedor]').setValue('');
                panelInicialPral.down('[name=idnombreBeneficiarioProv]').hide();
                panelInicialPral.down('combo[name=cmbBeneficiario]').show();
                //panelInicialPral.down('[name=idCveBeneficiario]').show();
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
    
    function _p21_agregarGrupoClic(){
        if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_DIRECTO){
            var obtener = [];
            storeFacturaDirecto.each(function(record) {
                obtener.push(record.data);
            });
            if(obtener.length > 0){
                storeFacturaDirecto.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
            }else{
                storeFacturaDirecto.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
            }
        }else if(panelInicialPral.down('combo[name=cmbTipoPago]').getValue() == _TIPO_PAGO_REEMBOLSO){
            storeFacturaReembolso.add(new modelFacturaSiniestro({tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
        }else{
            //Tenemos que verificar que tenga fecha de ocurrencia seleccionado
            var fechaOcurrencia = panelInicialPral.down('[name=dtFechaOcurrencia]').getValue();
            if(fechaOcurrencia == null){
                mensajeError('Para agregar un documento se requiere la fecha de ocurrencia');
            }else{
                if(panelInicialPral.down('combo[name=cmbRamos]').getValue() == _RECUPERA){
                    storePagoIndemnizatorioRecupera.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001', importe:'0.00'}));
                }else{
                    storePagoIndemnizatorio.add(new modelFacturaSiniestro({noFactura:'0',fechaFactura:fechaOcurrencia,tasaCambio:'0.00',importeFactura:'0.00',tipoMonedaName:'001'}));
                }
                
            }
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
});
