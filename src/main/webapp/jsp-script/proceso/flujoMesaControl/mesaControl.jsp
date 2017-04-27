<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript" src="${ctx}/resources/extjs4/plugins/pagingpersistence/pagingselectionpersistence.js?${now}"></script>

<script>
////// urls //////
var _p54_urlCargar                    = '<s:url namespace="/flujomesacontrol" action="recuperarTramites"             />'
    ,_p54_urlRecuperarPoliza          = '<s:url namespace="/flujomesacontrol" action="recuperarPolizaUnica"          />'
    ,_p54_urlRegistrarTramite         = '<s:url namespace="/flujomesacontrol" action="registrarTramite"              />'
    ,_p54_urlCargarCduniecoAgenteAuto = '<s:url namespace="/emision"          action="cargarCduniecoAgenteAuto"      />'
    ,_p54_urlRecuperarPolizaDanios    = '<s:url namespace="/flujomesacontrol" action="recuperarPolizaUnicaDanios"    />'
    ,_p54_urlRecuperarPolizaSIGS      = '<s:url namespace="/emision"          action="cargarPoliza"                  />'
    ,_p54_urlRecuperarChecklist       = '<s:url namespace="/flujomesacontrol" action="recuperarChecklistInicial"     />'
    ,_p54_urlactulizaNumFolioMcSigs   = '<s:url namespace="/consultasPoliza"  action="actualizaEstatusTramiteMCsigs" />'
    ,_p54_urlCargarParametrosCoti     = '<s:url namespace="/emision"          action="obtenerParametrosCotizacion"   />';
////// urls //////

////// variables //////
var _p54_params = <s:property value="%{convertToJSON('params')}"  escapeHtml="false" />;
debug('_p54_params:',_p54_params);

var _p54_grid;
var _p54_store;

var _p54_tamanioPag = 10;

var _p54_windowNuevo;
var _p54_windowFormBase;
var _p54_formularios = [];

var _p54_btnReasigna = _p54_params.BTN_REASIGNAR == 'S'? true:false;

////// variables //////

////// overrides //////

//Se repara que combos con 'forceSelection' dejen pasar cadena mientras se carga su lista'
Ext.define('ComboBox', {
    override: 'Ext.form.ComboBox',
    
    validator: function(val) {

        var me = this;
        var valido= true;
            
        if (me.forceSelection === true && !Ext.isEmpty(val)) {
            valido = (me.findRecord('value',val)!== false);
        }
        debug(me.name+' - '+me.forceSelection+' - '+val+ '-' +valido)
        return valido || 'No se encuentra el registro';
    }
});
////// overrides //////

////// componentes dinamicos //////
var _p54_filtroItemsDin = [ <s:property value="items.filtroItems"    escapeHtml="false" /> ];
var _p54_formBaseItems  = [ <s:property value="items.formBaseItems"  escapeHtml="false" /> ];
var _p54_formFlujoItems = [ <s:property value="items.formFlujoItems" escapeHtml="false" /> ];

_p54_filtroItems =
[
    {
        xtype       : 'textfield'
        ,fieldLabel : '_AGRUPAMC'
        ,name       : 'AGRUPAMC'
        ,value      : _p54_params.AGRUPAMC
        ,style      : 'margin:5px;'
        ,readOnly   : true
        ,hidden     : true
    }
];

for(var i in _p54_filtroItemsDin)
{
    _p54_filtroItems.push(_p54_filtroItemsDin[i]);
}

_p54_gridButtons = [ <s:property value="items.botonesGrid" escapeHtml="false" /> ];
_p54_gridButtons.push('->');
_p54_gridButtons.push(
{
    xtype       : 'textfield'
    ,itemId     : '_p54_filtroCmp'
    ,fieldLabel : '<span style="color:white;">Filtro:</span>'
    ,labelWidth : 60
    ,espera     : ''
    ,listeners  :
    {
        change : function(me,val)
        {
            clearTimeout(me.espera);
            me.espera=setTimeout(function()
            {
                Ext.ComponentQuery.query('[xtype=button][text=Buscar]')[0].handler(Ext.ComponentQuery.query('[xtype=button][text=Buscar]')[0]);
            },1500);
        }
    }
});

/*
/////////////////////////////
//No hay filtrado en paginado
/////////////////////////////

_p54_gridButtons.push('->');
_p54_gridButtons.push(
{
    xtype       : 'textfield'
    ,fieldLabel : '<span style="color:white;">Filtro (pend.):</span>'
    ,labelWidth : 60
});
*/
////// componentes dinamicos //////

Ext.onReady(function()
{
    Ext.Ajax.timeout = 1*60*60*1000; //1 hr
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

    ////// stores //////
    _p54_store = Ext.create('Ext.data.Store',
    {
        fields    : [ <s:property value="items.gridFields" escapeHtml="false" /> ]
        ,autoLoad : false
        ,pageSize : _p54_tamanioPag
        ,proxy    :
        {
            url          : _p54_urlCargar
            ,type        : 'ajax'
            ,extraParams :
            {
                'params.AGRUPAMC'  : _p54_params.AGRUPAMC
                ,'params.STATUS'   : '-1'
                ,'params.CDAGENTE' : _p54_params.CDAGENTE
            }
            ,reader      :
            {
                type           : 'json'
                ,root          : 'list'
                ,totalProperty : 'total'
                ,idProperty    : 'NTRAMITE'
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    _p54_windowNuevo = Ext.create('Ext.window.Window',
    {
        title        : 'Registrar nuevo tr\u00e1mite'
        ,itemId      : '_p54_formNuevoTramite'
        ,icon        : '${icons}add.png'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                layout :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items       : [ <s:property value="items.formItems" escapeHtml="false" /> ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            _p54_registrarTramite(me);
                        }
                    }
                    ,{
                        text     : 'Limpiar'
                        ,icon    : '${icons}control_repeat_blue.png'
                        ,handler : function (me) {
                            //_p54_windowNuevo.showNew();
                            me.up('window').close();
                            _p54_nuevoTramiteClic();
                        } 
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var me = this;
            me.down('form').getForm().reset();
            
            me.triggerHerencia();
            
            centrarVentanaInterna(me.show());
            
            me.onLevelChange(1);
        }
        ,triggerHerencia : function()
        {
            debug('triggerHerencia');
            
            var me = this;
            
            var componentesAnidados = Ext.ComponentQuery.query('[heredar]',me);
            debug('componentesAnidados:',componentesAnidados,'.');
            for(var i=0;i<componentesAnidados.length;i++)
            {
                componentesAnidados[i].heredar(true);
            }
        }
        ,onLevelChange : function(level)
        {
            var me = this;
            debug('onLevelChange level:',level,'.');
            
            var ck = 'Navegando ventana emergente de tr\u00e1mite nuevo';
            try
            {
                if(level === 1) // solo se muestra tipo trámite y proceso
                {
                    var comps = Ext.ComponentQuery.query('[name]',me);
                    debug('comps:',comps,'.');
                
                    for(var i = 0 ; i < comps.length ; i++ )
                    {
                        _hide(comps[i]);
                    }
                
                    _show(me.down('[name=CDTIPFLU]'));
                    _show(me.down('[name=CDFLUJOMC]'));
                    
                    me.down('[name=CDTIPFLU]').setReadOnly(false);
                    me.down('[name=CDFLUJOMC]').setReadOnly(false);
                }
                else if(level === 2) 
                {
                    me.down('[name=CDTIPFLU]').setReadOnly(true);
                    me.down('[name=CDFLUJOMC]').setReadOnly(true);
                    
                    ck = 'Recuperando tipo de tr\u00e1mite y tipo de producto';
                    
                    var cdtiptra = me.down('form').getValues().CDTIPTRA;
                    debug('cdtiptra:',cdtiptra,'.');
                    
                    var cdtipram = me.down('[name=CDFLUJOMC]').findRecordByValue(me.down('[name=CDFLUJOMC]').getValue()).get('aux');
                    debug('cdtipram:',cdtipram,'.');
                    
                    ck = 'Encendiendo atributos por proceso';
                    
                    var comps = Ext.ComponentQuery.query('[name]:not([fieldLabel^=_])',me);
                    
                    for(var i = 0 ; i < comps.length ; i++ )
                    {
                        _show(comps[i]);
                    }
                    
                    if(Number(cdtiptra) === 1) // para emision
                    {
                        // indistinto salud y danios
                        // mostrar
                        
                        me.down('[name=CDRAMO]').allowBlank = false;
                        _show(me.down('[name=CDRAMO]'));
                        
                        me.down('[name=CDTIPSIT]').allowBlank = false;
                        _show(me.down('[name=CDTIPSIT]'));
                        
                        _fieldByName('NOMBRE',me).allowBlank = false;
                        _show(_fieldByName('NOMBRE',me));
                        
                        _fieldByName('COMMENTS',me).allowBlank = false;
                        _show(_fieldByName('COMMENTS',me));
                        
                        _fieldByName('CDAGENTE',me).allowBlank = false;
                        _show(_fieldByName('CDAGENTE',me));
                        
                        // ocultar
                        
                        me.down('[name=CDUNIEXT]').allowBlank = true;
                        _hide(me.down('[name=CDUNIEXT]'));
                        
                        me.down('[name=RAMO]').allowBlank = true;
                        _hide(me.down('[name=RAMO]'));
                        
                        me.down('[name=NMPOLIEX]').allowBlank = true;
                        _hide(me.down('[name=NMPOLIEX]'));
                        
                        me.down('[name=CDTIPSUPEND]').allowBlank = true;
                        _hide(me.down('[name=CDTIPSUPEND]'));
                        
                        me.down('[name=NMPOLIZA]').allowBlank = false;
                        _hide(me.down('[name=NMPOLIZA]'));
                        
                        _fieldByName('REFERENCIA',me).allowBlank = false;
                        _hide(_fieldByName('REFERENCIA',me));
                        
                        _fieldByName('CDAGENTEEND',me).allowBlank = true;
                        _hide(_fieldByName('CDAGENTEEND',me));
                        
                        if (Number(cdtipram) === Number(TipoRamo.Salud)) { // Para salud
                            _fieldByName('CDSUCDOC',me).forceSelection = true;
                            _show(_fieldByName('CDSUCDOC',me));
                        } else { // Para danios
                            _fieldByName('CDSUCDOC',me).forceSelection = false;
                            _hide(_fieldByName('CDSUCDOC',me));
                        }
                    }
                    else if(Number(cdtiptra) === 15 || Number(cdtiptra) === 21) // para endoso o renovacion
                    {
                        // indistinto salud y danios
                        
                        // mostrar
                        
                        _fieldByName('CDUNIEXT',me).allowBlank = false;
                        _show(_fieldByName('CDUNIEXT',me));
                        
                        me.down('[name=RAMO]').allowBlank = false;
                        _show(me.down('[name=RAMO]'));
                        
                        _fieldByName('CDAGENTEEND',me).allowBlank = false;
                        _show(_fieldByName('CDAGENTEEND',me));
                        
                        // ocultar
                        
                        _fieldByName('CDSUCDOC',me).forceSelection = false;
                        _hide(_fieldByName('CDSUCDOC',me));
                        
                        _fieldByName('CDRAMO',me).allowBlank = true;
                        _hide(_fieldByName('CDRAMO',me));
                        
                        _fieldByName('CDTIPSIT',me).allowBlank = true;
                        _hide(_fieldByName('CDTIPSIT',me));
                        
                        _fieldByName('REFERENCIA',me).allowBlank = true;
                        _hide(_fieldByName('REFERENCIA',me));
                        
                        _fieldByName('NOMBRE',me).allowBlank = true;
                        _hide(_fieldByName('NOMBRE',me));
                        
                        _fieldByName('COMMENTS',me).allowBlank = true;
                        _hide(_fieldByName('COMMENTS',me));
                        
                        _fieldByName('CDAGENTE',me).allowBlank = true;
                        _hide(_fieldByName('CDAGENTE',me));
                        
                        if(Number(cdtiptra) === 15)
                        {
                            _fieldByName('CDTIPSUPEND',me).allowBlank = false;
                            _show(_fieldByName('CDTIPSUPEND',me));
                        }
                        else
                        {
                            _fieldByName('CDTIPSUPEND',me).allowBlank = true;
                            _hide(_fieldByName('CDTIPSUPEND',me));
                        }
                        
                        // salud
                        
                        if(Number(cdtipram) === Number(TipoRamo.Salud)) 
                        {
                            // mostrar
                            
                            me.down('[name=NMPOLIZA]').allowBlank = false;
	                        _show(me.down('[name=NMPOLIZA]'));
	                        
	                        // ocultar
	                        
	                        me.down('[name=NMPOLIEX]').allowBlank = true;
	                        _hide(me.down('[name=NMPOLIEX]'));
                            
                        }
                        
                        // autos
                        
                        else if(Number(cdtipram) === Number(TipoRamo.Autos))
                        {
                            // mostrar
                            
                            me.down('[name=NMPOLIEX]').allowBlank = false;
	                        _show(me.down('[name=NMPOLIEX]'));
	                        
	                        // ocultar
	                        
	                        me.down('[name=NMPOLIZA]').allowBlank = true;
	                        _hide(me.down('[name=NMPOLIZA]'));
                        }
                    }
                }
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
    });
    
    _p54_windowFormBase = Ext.create('Ext.window.Window', {
        title       : 'Registrar nuevo tr\u00e1mite',
        closeAction : 'hide',
        modal       : true,
        border      : 0,
        items       : [
            Ext.create('Ext.form.Panel', {
                border : 0,
                items  : _p54_formBaseItems
            })
        ],
        buttonAlign : 'center',
        buttons     : [
            {
                text    : 'Continuar',
                icon    : '${icons}accept.png',
                handler : function (me) {
                    if (me.up('window').down('form').getForm().isValid()) {
                        _p54_mostrarFormulario(me, me.up('window').down('form').getForm().getValues());
                    }
                }
            }, {
                text    : 'Cancelar',
                icon    : '${icons}cancel.png',
                handler : function (me) {
                    me.up('window').close();
                }
            }
        ]
    });
    
    (function () {
        var ck = 'Construyendo formularios';
        try {
            var items = [];
            for (var i = 0; i < _p54_formFlujoItems.length; i++) {
                var item = _p54_formFlujoItems[i];
                if (item.param4 !== 'CDTIPFLU'
                    || item.param5 !== 'CDFLUJOMC'
                    || Ext.isEmpty(item.value4)
                    || Ext.isEmpty(item.value5)
                    || typeof item.value4 !== 'number'
                    || typeof item.value5 !== 'number'
                    ) {
                    debug('No se considera para los items de formularios:', item);
                    continue;
                }
                var key = item.value4 + '_' + item.value5;
                if (Ext.isEmpty(items[key])) {
                    items[key] = [];
                }
                items[key].push(item);
            }
            debug('items:', items);
            for (var key in items) {
                _p54_formularios[key] = Ext.create('Ext.window.Window', {
                    title       : 'Registrar nuevo tr\u00e1mite',
                    modal       : true,
                    border      : 0,
                    closeAction : 'hide',
                    items       : [
                        Ext.create('Ext.form.Panel', {
                            border : 0,
                            layout : {
                                type    : 'table',
                                columns : 2
                            },
                            items : items[key]
                        })
                    ],
                    buttonAlign : 'center',
                    buttons     : [
                        {
                            text    : 'Guardar',
                            icon    : '${icons}disk.png',
                            handler : _p54_registrarTramite
                        }, {
                            text    : 'Cancelar',
                            icon    : '${icons}cancel.png',
                            handler : function (me) {
                                me.up('window').close();
                            }
                        }
                    ]
                });
            }
            debug('_p54_formularios:', _p54_formularios);
        } catch (e) {
            debugError(e, ck);
        }
    })();
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p54_panelpri'
        ,renderTo : '_p54_divpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                title   : 'Filtro'
                ,itemId : '_p54_filtroForm'
                ,icon   : '${icons}zoom.png'
                ,items  : _p54_filtroItems
                ,layout :
                {
                    type     : 'table'
                    ,columns : 3
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text       : 'Buscar'
                        ,icon      : '${icons}zoom.png'
                        ,listeners :
                        {
                            afterrender : function(me)
                            {
                                _setLoading(true,me);
                                setTimeout(
                                    function()
                                    {
                                        _setLoading(false,me);
                                        if(Ext.isEmpty(_p54_params.ntramiteCargar)){
                                        	me.handler(me);
                                        }
                                        else{
                                        	_p54_cargarTramite(_p54_params.ntramiteCargar);
                                        }
                                    }
                                    ,1.5*1000
                                );
                            }
                        }
                        ,handler   : function(me)
                        {
                            var ck = 'Recuperando tr\u00e1mites';
                            try
                            {
                                var form = me.up('form');
                                if(!form.isValid())
                                {
                                    throw 'Favor de revisar los datos';
                                }
                                
                                var values = form.getValues();
                                values.FILTRO = _fieldById('_p54_filtroCmp').getValue();
                                
                                _p54_store.proxy.extraParams = _formValuesToParams(values);
                                _p54_grid.down('pagingtoolbar').moveFirst();
                                
                                var gridTramites = _fieldById('_p54_grid');
                                gridTramites.deslectAllPersistedSelection();
                                
                            }
                            catch(e)
                            {
                                debugError(e);
                                manejaException(e,ck);
                            }
                        }
                    }
                    ,{
                        text     : 'Limpiar'
                        ,icon    : '${icons}control_repeat_blue.png'
                        ,handler : function(me)
                        {
                            me.up('form').getForm().reset();
                        }
                    }
                    ,{
                        text     : 'Reporte (pendiente)'
                        ,icon    : '${icons}printer.png'
                        ,hidden  : true
                        ,handler : function(me)
                        {}
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title        : 'Tareas'
                ,itemId      : '_p54_grid'
                ,height      : 470
                ,tbar        : _p54_gridButtons
                ,columns     : [ <s:property value="items.gridColumns" escapeHtml="false" /> ]
                ,store       : _p54_store
                ,dockedItems :
                [
                    {
                        xtype        : 'pagingtoolbar'
                        ,store       : _p54_store
                        ,dock        : 'bottom'
                        ,displayInfo : true
                    }
                ]
                ,selModel: Ext.create('Ext.selection.CheckboxModel', {mode: 'MULTI', allowDeselect: true, checkOnly: true,
                    listeners: {
                        beforeselect : function(chk, record, index, eOpts){
                            if(Ext.isEmpty(record.get('CDETAPA')) || record.get('CDETAPA') != '2'){
                                showMessage("Reasignar tr\u00e1mite - Aviso","Este tr\u00e1mite no puede seleccionarse puesto que no se encuetra en proceso para poder ser reasignado.", Ext.Msg.OK, Ext.Msg.INFO);                               
                                return false;
                            }
                        }
                    }
                })
                ,plugins: [{ptype : 'pagingselectpersist'}]
                ,listeners :
                {
                 cellclick : function ( cellsel, td, cellIndex, record, tr, rowIndex, e, eOpts )
                    {
                     //Se cambia evento a Cellclick en vez de select y solo se permite seleccionar a la primer fila
                     // se carga ventana de tranite en el evento de cellclick fuera del checkbox
                     if(cellIndex == 0){
                         return true;
                     }
                     
                        var ck = 'Invocando acciones de proceso';
                        try
                        {
                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                            {
                                title        : 'ACCIONES'
                                ,itemId      : '_p54_windowAcciones'
                                ,modal       : true
                                ,width       : 800
                                ,height      : 300
                                ,border      : 0
                                ,closeAction : 'destroy'
                                ,defaults    :
                                {
                                    style  : 'margin:5px;border-bottom:1px solid #CCCCCC;'
                                    ,width : 350
                                }
                                ,layout      :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                }
                                ,items       :
                                [
                                    {
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'NO. TR\u00c1MITE'
                                        ,value      : record.get('NTRAMITE')
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'TR\u00c1MITE'
                                        ,value      : record.get('DSTIPFLU')
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'PROCESO'
                                        ,value      : record.get('DSFLUJOMC')
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'ESTATUS'
                                        ,value      : record.get('DSSTATUS')
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'SUCURSAL'
                                        ,value      : record.get('CDTIPRAM') == 2 && !Ext.isEmpty(record.get('CDUNIEXT'))
                                                          ? record.get('CDUNIEXT')
                                                          : record.get('CDUNIECO')
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'RAMO'
                                        ,value      : _NVL(record.get('RAMO')) + ' - ' + record.get('DSTIPSIT')
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'P\u00d3LIZA ANTERIOR'
                                        ,value      : _NVL(record.get('RENPOLIEX'), 0)
                                        ,hidden     : record.get('CDTIPTRA') != 21 || record.get('CDTIPRAM') != 2 // Oculto si no es renovacion de auto
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'P\u00d3LIZA'
                                        ,value      : _NVL(record.get('NMPOLIZA'), 0)
                                        ,hidden     : record.get('CDTIPRAM') == 2 // Oculto para autos
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'P\u00d3LIZA'
                                        ,value      : _NVL(record.get('NMPOLIEX'), 0)
                                        ,hidden     : record.get('CDTIPRAM') == 10 // Oculto para salud
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'COTIZACI\u00D3N'
                                        ,value      : _NVL(record.get('NMSOLICI'), 0)
                                        ,hidden     : [1, 21, 22].indexOf(Number(record.get('CDTIPTRA'))) === -1
                                    }
                                    ,{
                                        xtype       : 'displayfield'
                                        ,fieldLabel : 'ENDOSO'
                                        ,value      : _NVL(record.get('NRO_ENDOSO'), '-')
                                        ,hidden     : Number(record.get('CDTIPTRA')) !== 15
                                    }, {
                                        xtype      : 'displayfield',
                                        fieldLabel : 'TR\u00c1MITE SUSTITUTO',
                                        value      : _NVL(record.get('NTRASUST')),
                                        hidden     : record.get('CDTIPRAM') != 2
                                    }
                                ]
                                ,buttonAlign : 'center'
                                ,bbar        : [
                                    {
                                        xtype  : 'panel',
                                        itemId : '_p54_windowAccionesButtons',
                                        border : 0,
                                        ui     : 'footer',
                                        layout : {
                                            type    : 'table',
                                            columns : 6
                                        },
                                        items : _cargarBotonesEntidad(
                                            record.get('CDTIPFLU'),
                                            record.get('CDFLUJOMC'),
                                            'E',
                                            record.get('STATUS'),
                                            '',
                                            function (botones) {
                                                _fieldById('_p54_windowAccionesButtons').add(botones);
                                            },
                                            record.get('NTRAMITE'),
                                            record.get('STATUS'),
                                            record.get('CDUNIECO'),
                                            record.get('CDRAMO'),
                                            record.get('ESTADO'),
                                            record.get('NMPOLIZA'),
                                            record.get('NMSITUAC'),
                                            record.get('NMSUPLEM'),
                                            function () {
                                                _p54_store.reload();
                                            }
                                        )
                                    }
                                ]
                            }).show());
                        }
                        catch(e)
                        {
                            manejaException(e,ck);
                        }
                    }
                }
            })
        ]
    });
    
    
    
    ////// contenido //////
    
    ////// custom //////
    if (Ext.isEmpty(_p54_params.CDUNIECO)) {
        mensajeError('No se encuentra sucursal asociada al usuario y no se pueden crear tr\u00e1mites');
    }
    
    var _p54_grid = _fieldById('_p54_grid');
    
    if(!_p54_btnReasigna){
        _p54_grid.headerCt.items.getAt(0).hide();
    }
 
    var cdtipfluCmp    = _fieldByName('CDTIPFLU'  , _p54_windowNuevo),
        cdflujoCmp     = _fieldByName('CDFLUJOMC' , _p54_windowNuevo),
        cdtiptraCmp    = _fieldByName('CDTIPTRA'  , _p54_windowNuevo),
        cdtipsupCmp    = _fieldByName('CDTIPSUP'  , _p54_windowNuevo),
        nmpolizaCmp    = _fieldByName('NMPOLIZA'  , _p54_windowNuevo),
        nmpoliexCmp    = _fieldByName('NMPOLIEX'  , _p54_windowNuevo),
        estadoCmp      = _fieldByName('ESTADO'    , _p54_windowNuevo),
        cduniextCmp    = _fieldByName('CDUNIEXT'  , _p54_windowNuevo),
        estatusCmp     = _fieldByName('STATUS'    , _p54_windowNuevo),
        cdsucdocCmp    = _fieldByName('CDSUCDOC'  , _p54_windowNuevo),
        cdramoCmp      = _fieldByName('CDRAMO'    , _p54_windowNuevo),
        cdagenteCmp    = _fieldByName('CDAGENTE'  , _p54_windowNuevo),
        cdsucdocCmp    = _fieldByName('CDSUCDOC'  , _p54_windowNuevo);
    
    cdsucdocCmp.heredar = function() {
        debug('>cdsucdocCmp.heredar');
        cdsucdocCmp.getStore().load();
    }
    
    cdramoCmp.heredar = function () {
        debug('>cdramoCmp.heredar');
        cdramoCmp.store.load();
    }
    
    cduniextCmp.heredar = function () {
        debug('>cduniextCmp.heredar');
        cduniextCmp.store.load();
    }
    
    cdtipfluCmp.on({
        select : function (me, records) {
            debug('select data:', records[0].data, '.');
            
            cdtiptraCmp.setValue(records[0].get('aux'));
            
            cdtipsupCmp.setValue(records[0].get('aux2'));
            
            if (records[0].get('aux3') === 'S') {
                nmpolizaCmp.allowBlank      = false;
                nmpolizaCmp.verificarPoliza = true;
                
                nmpoliexCmp.allowBlank      = false;
                nmpoliexCmp.verificarPoliza = true;
            } else {
                nmpolizaCmp.allowBlank      = true;
                nmpolizaCmp.verificarPoliza = false;
                
                nmpoliexCmp.allowBlank      = true;
                nmpoliexCmp.verificarPoliza = false;
            }
        }
    });
    
    cdflujoCmp.on({
        select : function (me, records) {
        debug('cdflujoCmp.select! args:',arguments);
            var cdtipram  = records[0].get('aux'),
                cdflujomc = records[0].get('key');
            cdsucdocCmp.store.proxy.extraParams['params.idPadre'] = cdflujomc;
            cdsucdocCmp.heredar();
            _p54_setearSucursalAgente();
            if (Number(cdtiptraCmp.getValue()) !== 1) { // Para endosos y renovacion
                cduniextCmp.store.proxy.extraParams['params.idPadre'] = cdtipram;
                cduniextCmp.heredar();
            } else {
                cduniextCmp.store.proxy.extraParams['params.idPadre'] = '';
                cduniextCmp.store.removeAll();
            }
        }
    });
    
    /*
    cdtiptraCmp.on({
        change : function (me, val) {
            if (_p54_params.CDSISROL === 'SUSCRIPTOR') {
                if (Number(val) === 1) {
                    estatusCmp.setValue('2'); // para SUSCRIPTOR para EMISION el status es PENDIENTE
                } else {
                    estatusCmp.reset();
                }
            }
        }
    });
    */
    
    if (!Ext.isEmpty(cdagenteCmp.store)) {
        cdagenteCmp.on({
            select : function (me, records) {
                _p54_setearSucursalAgente();
            }
        });
    }
    
    cdtipsupCmp.on({
        change : function (me, val) {
            if (!Ext.isEmpty(val) && Number(val) === 1) { // Para emision no se pueden modificar los numeros de poliza 0
                nmpolizaCmp.setReadOnly(true);
                nmpolizaCmp.setValue('0');
                nmpolizaCmp.isValid();
                
                nmpoliexCmp.setReadOnly(true);
                nmpoliexCmp.setValue('0');
                nmpoliexCmp.isValid();
            } else {                                      // Para endoso se pueden modificar los numeros de poliza
                nmpolizaCmp.setReadOnly(false);
                nmpolizaCmp.setValue('');
                nmpolizaCmp.isValid();
                
                nmpoliexCmp.setReadOnly(false);
                nmpoliexCmp.setValue('');
                nmpoliexCmp.isValid();
            }
        }
    });
    
    cdsucdocCmp.on({
        change : function (me, val) {
            debug('cdsucdocCmp.change! args:', arguments);
            
            cdramoCmp.store.proxy.extraParams['params.idPadre'] = val;
            cdramoCmp.heredar();
        }
    });
    
    nmpolizaCmp.on({
        blur : function (me) {
            var val = me.getValue();
            if (!Ext.isEmpty(me) && me.verificarPoliza === true) {
                var mask, ck = 'Recuperando p\u00f3liza';
                try {
                    var cdunieco = _p54_windowNuevo.down('[name=CDUNIEXT]').getValue(),
                        ramo     = _p54_windowNuevo.down('[name=RAMO]').getValue();
                    
                    if (Ext.isEmpty(cdunieco) || Ext.isEmpty(ramo)) {
                        throw 'Seleccione sucursal y ramo antes de confirmar la p\u00f3liza';
                    }
                
                    mask = _maskLocal(ck);
                    Ext.Ajax.request({
                        url    : _p54_urlRecuperarPoliza,
                        params : {
                            'params.CDUNIECO' : cdunieco,
                            'params.RAMO'     : ramo,
                            'params.ESTADO'   : 'M',
                            'params.NMPOLIZA' : val
                        },
                        success : function (response) {
                            mask.close();
                            var ck = 'Decodificando respuesta al recuperar p\u00f3liza';
                            try {
                                var json = Ext.decode(response.responseText);
                                debug('### poliza:', json);
                                if (json.success === true) {
                                    
                                    // Si la recuperada no es del agente de sesion
                                    if (!Ext.isEmpty(_p54_params.CDAGENTE) && Number(_p54_params.CDAGENTE) !== Number(json.params.CDAGENTE)) {
                                        throw 'No tiene permisos para recuperar esta p\u00f3liza';//, pertenece al agente ' + json.params.CDAGENTE;
                                    }
                                    
                                    centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                        title    : 'P\u00f3liza',
                                        modal    : true,
                                        closable : false,
                                        width    : 400,
                                        border   : 0,
                                        defaults : { style : 'margin:5px;' },
                                        items    : [
                                            {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'SUCURSAL'
                                                ,value      : json.params.CDUNIECO
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'RAMO'
                                                ,value      : ramo
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'P\u00d3LIZA'
                                                ,value      : json.params.NMPOLIZA
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'FECHA DE EMISI\u00d3N'
                                                ,value      : json.params.FEEMISIO
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'INICIO DE VIGENCIA'
                                                ,value      : json.params.FEEFECTO
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'ESTATUS'
                                                ,value      : json.params.STATUSPOL
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'CONTRATANTE'
                                                ,value      : json.params.CONTRATANTE
                                            }, {
                                                xtype       : 'displayfield'
                                                ,fieldLabel : 'AGENTE'
                                                ,value      : json.params.AGENTE
                                            }
                                        ],
                                        buttonAlign : 'center',
                                        buttons     : [
                                            {
                                                text    : 'Aceptar',
                                                icon    : '${icons}accept.png',
                                                handler : function (bot) {
                                                    bot.up('window').destroy();
                                                    
                                                    var agenteEndCmp = me.up('window').down('[name=CDAGENTEEND]');
                                                    
                                                    _setValueCampoAgente(agenteEndCmp, json.params.CDAGENTE);
                                                    
                                                    estadoCmp.setValue('M');
                                                    
                                                    _fieldByName('CDTIPSUPEND' , _p54_windowNuevo).getStore().load({
                                                        params : {
                                                            'params.cdramo'   : json.params.CDRAMO,
                                                            'params.cdtipsit' : json.params.CDTIPSIT,
                                                            'params.vigente'  : json.params.STATUSPOL === 'VIGENTE'
                                                                ? 'S'
                                                                : 'N'
                                                        }
                                                    });
                                                    
                                                    _fieldByName('CDSUCDOC'    , _p54_windowNuevo).setValue(json.params.CDUNIECO);
                                                    _fieldByName('CDRAMOEND'   , _p54_windowNuevo).setValue(json.params.CDRAMO);
                                                    _fieldByName('CDTIPSITEND' , _p54_windowNuevo).setValue(json.params.CDTIPSIT);
                                                }
                                            }, {
                                                text    : 'Cancelar',
                                                icon    : '${icons}cancel.png',
                                                handler : function (bot) {
                                                    me.setValue('');
                                                    estadoCmp.reset();
                                                    me.isValid();
                                                    bot.up('window').destroy();
                                                }
                                            }
                                        ]
                                    }).show());
                                } else {
                                    mensajeError(json.message);
                                    me.setValue('');
                                    estadoCmp.reset();
                                    me.isValid();
                                }
                            } catch (e) {
                                me.setValue('');
                                estadoCmp.reset();
                                me.isValid();
                                manejaException(e,ck);
                            }
                        }
                        ,failure : function () {
                            mask.close();
                            me.setValue('');
                            estadoCmp.reset();
                            me.isValid();
                            errorComunicacion(null, 'Error Recuperando p\u00f3liza');
                        }
                    });
                }
                catch(e)
                {
                    me.setValue('');
                    estadoCmp.reset();
                    me.isValid();
                    manejaException(e, ck, mask);
                }
            }
        }
    });
    
    // Hacer editable el combo de status del filtro de busqueda
    try {
        var comboStatus = _fieldByLabel('ESTATUS', _fieldById('_p54_filtroForm'));
        
        debug('comboStatus:', comboStatus);
        
        comboStatus.store.padre = comboStatus;
        comboStatus.store.on({
            load : function (me){
                me.padre.forceSelection = true;
                me.padre.setEditable(true);
            }
        });
        
        if (comboStatus.store.getCount()>0) {
            comboStatus.forceSelection = true;
            comboStatus.setEditable(true);
        }
    } catch(e) {
        manejaException(e);
    }
    
    var ck = 'Agregando comportamiento adicional a componente de nivel de ventana emergente';
    try {
        var cdflujoComp = _p54_windowNuevo.down('[name=CDFLUJOMC]');
        cdflujoComp.on({
            select : function (me,records) {
                _p54_windowNuevo.down('[name=CDRAMO]').store.proxy.extraParams['params.tipogrupo'] = records[0].get('aux2');
                
                _p54_windowNuevo.down('[name=CDTIPSIT]').store.proxy.extraParams['params.tipogrupo'] = records[0].get('aux2');
                
                debug('extraParams cdramo:',_p54_windowNuevo.down('[name=CDRAMO]').store.proxy.extraParams);
                
                debug('extraParams cdtipsit:',_p54_windowNuevo.down('[name=CDTIPSIT]').store.proxy.extraParams);
                            
                _p54_windowNuevo.onLevelChange(2);
            }
        });
    } catch(e) {
        manejaException(e,ck);
    }
    
    nmpoliexCmp.on({
        blur : function (me) {
            var val = me.getValue();
            if (!Ext.isEmpty(val) && me.verificarPoliza === true) {
                var mask, ck = 'Recuperando p\u00f3liza';
                try {
                    var cduniext = _p54_windowNuevo.down('[name=CDUNIEXT]').getValue(),
                        ramo     = _p54_windowNuevo.down('[name=RAMO]').getValue(),
                        cdtiptra = _p54_windowNuevo.down('[name=CDTIPTRA]').getValue();
                    
                    if (Ext.isEmpty(cduniext)) {
                        throw 'Seleccione la sucursal antes de confirmar la p\u00f3liza';
                    }
                    
                    if (Ext.isEmpty(ramo)) {
                        throw 'Seleccione el ramo antes de confirmar la p\u00f3liza';
                    }
                    
                    if (Number(cdtiptra) === 15) { // Endoso
                        ck = 'Recuperando p\u00f3liza SICAPS';
                        
	                    mask = _maskLocal(ck);
	                    Ext.Ajax.request({
	                        url    : _p54_urlRecuperarPolizaDanios,
	                        params : {
	                            'params.CDUNIEXT' : cduniext,
	                            'params.RAMO'     : ramo,
	                            'params.NMPOLIEX' : val
	                        },
	                        success : function (response) {
	                            mask.close();
	                            var ck = 'Decodificando respuesta al recuperar p\u00f3liza';
	                            try {
	                                var json = Ext.decode(response.responseText);
	                                debug('### poliza:',json);
	                                if (json.success == true) {
	                                    
	                                    // Si la recuperada no es del agente de sesion
                                        if (!Ext.isEmpty(_p54_params.CDAGENTE) && Number(_p54_params.CDAGENTE) !== Number(json.params.CDAGENTE)) {
                                            throw 'No tiene permisos para recuperar esta p\u00f3liza';//, pertenece al agente ' + json.params.CDAGENTE;
                                        }
	                                    
	                                    centrarVentanaInterna(Ext.create('Ext.window.Window',
	                                    {
	                                        title     : 'P\u00f3liza'
	                                        ,modal    : true
	                                        ,closable : false
	                                        ,width    : 400
	                                        ,border   : 0
	                                        ,defaults : { style : 'margin:5px;' }
	                                        ,items    :
	                                        [
	                                            {
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'SUCURSAL'
	                                                ,value      : json.params.CDUNIEXT
	                                            }
	                                            ,{
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'RAMO'
	                                                ,value      : json.params.RAMO
	                                            }
	                                            ,{
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'P\u00d3LIZA'
	                                                ,value      : json.params.NMPOLIEX
	                                            }
	                                            ,{
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'FECHA DE EMISI\u00d3N'
	                                                ,value      : json.params.FEEMISIO
	                                            }
	                                            ,{
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'INICIO DE VIGENCIA'
	                                                ,value      : json.params.FEEFECTO
	                                            }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'ESTATUS'
                                                    ,value      : json.params.STATUSPOL
                                                }
	                                            ,{
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'CONTRATANTE'
	                                                ,value      : json.params.CONTRATANTE
	                                            }
	                                            ,{
	                                                xtype       : 'displayfield'
	                                                ,fieldLabel : 'AGENTE'
	                                                ,value      : json.params.AGENTE
	                                            }
	                                        ]
	                                        ,buttonAlign : 'center'
	                                        ,buttons     :
	                                        [
	                                            {
	                                                text     : 'Aceptar'
	                                                ,icon    : '${icons}accept.png'
	                                                ,handler : function(bot)
	                                                {
	                                                    bot.up('window').destroy();
                                                    
                                                        var agenteEndCmp = me.up('window').down('[name=CDAGENTEEND]');
                                                    
                                                        _setValueCampoAgente(agenteEndCmp, json.params.CDAGENTE);
                                                    
                                                        estadoCmp.setValue('M');
	                                                    
	                                                    me.up('window').down('[name=CDSUCDOC]').setValue(json.params.CDUNIECO);
	                                                    me.up('window').down('[name=CDRAMOEND]').setValue(json.params.CDRAMO);
                                                        me.up('window').down('[name=CDTIPSITEND]').setValue(json.params.CDTIPSIT);
                                                        me.up('window').down('[name=NMPOLIZA]').setValue(json.params.NMPOLIZA);
                                                        
                                                        _fieldByName('CDTIPSUPEND' , _p54_windowNuevo).getStore().load({
                                                            params : {
                                                                'params.cdramo'   : json.params.CDRAMO,
                                                                'params.cdtipsit' : json.params.CDTIPSIT,
                                                                'params.vigente'  : '*'
                                                                /* JTEZVA 30 NOV 2016 RAFA PIDE MOSTRAR TODOS
                                                                'params.vigente'  : json.params.STATUSPOL === 'VIGENTE'
                                                                    ? 'S'
                                                                    : 'N'
                                                                */
                                                            }
                                                        });
	                                                }
	                                            }
	                                            ,{
	                                                text     : 'Cancelar'
	                                                ,icon    : '${icons}cancel.png'
	                                                ,handler : function(bot)
	                                                {
	                                                    me.setValue('');
	                                                    estadoCmp.reset();
	                                                    me.isValid();
	                                                    bot.up('window').destroy();
	                                                }
	                                            }
	                                        ]
	                                    }).show());
	                                }
	                                else
	                                {
	                                    mensajeError(json.message);
	                                    me.setValue('');
	                                    estadoCmp.reset();
	                                    me.isValid();
	                                }
	                            }
	                            catch(e)
	                            {
	                                me.setValue('');
	                                estadoCmp.reset();
	                                me.isValid();
	                                manejaException(e,ck);
	                            }
	                        }
	                        ,failure : function()
	                        {
	                            mask.close();
	                            me.setValue('');
	                            estadoCmp.reset();
	                            me.isValid();
	                            errorComunicacion(null,'Error Recuperando p\u00f3liza');
	                        }
	                    });
                    }
                    else if(Number(cdtiptra) === 21)//renovacion
                    {
                        ck = 'Recuperando p\u00f3liza SIGS';
                        //redirije mc
                        var cdflujoCmp = _p54_windowNuevo.down('[name=CDFLUJOMC]');
                        debug('cdflujoCmp:',cdflujoCmp,'.');
                        
                        var cdflujoRec = cdflujoCmp.findRecord('key',cdflujoCmp.getValue());
                        debug('cdflujoRec:',cdflujoRec,'.');
                        
                        var dsflujo = cdflujoRec.get('value');
                        
                        debug('dsflujo:',dsflujo,'.');
                        
                        debug("dsflujo.toUpperCase().indexOf('PYME'):",dsflujo.toUpperCase().indexOf('PYME'),'.');
                        
                        debug("dsflujo.toUpperCase().indexOf('FLOTILLA'):",dsflujo.toUpperCase().indexOf('FLOTILLA'),'.');
                        
                        var tipoflot = 'I';
                        
                        if(dsflujo.toUpperCase().indexOf('PYME')!=-1)
                        {
                            tipoflot = 'P';
                        }
                        else if(dsflujo.toUpperCase().indexOf('FLOTILLA')!=-1)
                        {
                            tipoflot = 'F';
                        }
                        
                        debug('tipoflot:',tipoflot,'.');
                        
                        mask = _maskLocal(ck);
                        Ext.Ajax.request(
                        {
                            url      : _p54_urlRecuperarPolizaSIGS
                            ,params  :
                            {
                                'smap1.cdsucursal' : cduniext
                                ,'smap1.cdramo'    : ramo
                                ,'smap1.cdpoliza'  : val
                                ,'smap1.cdusuari'  : _p54_params.CDUSUARI
                                ,'smap1.tipoflot'  : tipoflot
                            }
                            ,success : function(response)
                            {
                                mask.close();
                                var ck = 'Decodificando respuesta al recuperar p\u00f3liza SIGS';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### poliza SIGS:',json);
                                    if(json.success==true && !Ext.isEmpty(json.smap1.valoresCampos))
                                    {
                                        var jsonSIGS = Ext.decode(json.smap1.valoresCampos);
                                        
                                        debug('jsonSIGS:',jsonSIGS);
                                        // Si la recuperada no es del agente de sesion
                                        if (!Ext.isEmpty(_p54_params.CDAGENTE) && Number(_p54_params.CDAGENTE) !== Number(jsonSIGS.smap1.cdagente)) {
                                            throw 'No tiene permisos para recuperar esta p\u00f3liza';//, pertenece al agente ' + jsonSIGS.smap1.cdagente;
                                        }
                                        //Si la recuperada ya fue registrada previamente o presenta errores en el proceso de registro
                                        if (!Ext.isEmpty(jsonSIGS.smap1.mensajeError)){
                                            throw jsonSIGS.smap1.mensajeError;
                                        }
                                        //Datos a resaltar de la recuperada cuando no corresponde al tipo fronterizo/residente individual/flotilla indicado
//                                         if (!Ext.isEmpty(jsonSIGS.smap1.mensajeAviso)){
//                                         	mensajeCorrecto('Aviso',jsonSIGS.smap1.mensajeAviso);
//                                         }
                                        
                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                                        {
                                            title     : 'P\u00f3liza'
                                            ,modal    : true
                                            ,closable : false
                                            ,width    : 400
                                            ,border   : 0
                                            ,defaults : { style : 'margin:5px;' }
                                            ,items    :
                                            [
                                                {
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'SUCURSAL'
                                                    ,value      : json.smap1.cdsucursal
                                                }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'RAMO'
                                                    ,value      : json.smap1.cdramo
                                                }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'P\u00d3LIZA'
                                                    ,value      : json.smap1.cdpoliza
                                                }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'FECHA DE EMISI\u00d3N'
                                                    ,value      : jsonSIGS.smap1.fesolici
                                                }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'INICIO DE VIGENCIA'
                                                    ,value      : jsonSIGS.smap1.feproren
                                                }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'CONTRATANTE'
                                                    ,value      : jsonSIGS.smap1.nombreContratante
                                                    
                                                }
                                                ,{
                                                    xtype       : 'displayfield'
                                                    ,fieldLabel : 'AGENTE'
                                                    ,value      : jsonSIGS.smap1.cdagente+' - '+jsonSIGS.smap1.nombreAgente
                                                }
                                            ]
                                            ,buttonAlign : 'center'
                                            ,buttons     :
                                            [
                                                {
                                                    text     : 'Aceptar'
                                                    ,icon    : '${icons}accept.png'
                                                    ,handler : function(bot)
                                                    {
                                                        bot.up('window').destroy();
                                                        
                                                        var agenteEndCmp = me.up('window').down('[name=CDAGENTEEND]');
                                                    
                                                        _setValueCampoAgente(agenteEndCmp, jsonSIGS.smap1.cdagente);
                                                        
                                                        me.up('window').down('[name=CDSUCDOC]').setValue(json.smap1.cdsucursal);
                                                        me.up('window').down('[name=CDRAMOEND]').setValue(jsonSIGS.smap1.cdramo);
                                                        me.up('window').down('[name=CDTIPSITEND]').setValue(jsonSIGS.smap1.cdtipsit);
                                                        me.up('window').down('[name=NMPOLIZA]').setValue('0');
                                                        
                                                        if (tipoflot === 'P' || tipoflot === 'F') {
                                                            var mascaraContarCamiones;
                                                            try {
                                                                mascaraContarCamiones = _maskLocal();
                                                                Ext.Ajax.request({
                                                                    url    : _p54_urlCargarParametrosCoti,
                                                                    params : {
                                                                        'smap1.parametro' : 'MAX_CAMIONES_PYME_FLOT',
                                                                        'smap1.cdramo'    : '5',
                                                                        'smap1.cdtipsit'  : 'AR',
                                                                        'smap1.clave4'    : tipoflot
                                                                    },
                                                                    success : function (response) {
                                                                        mascaraContarCamiones.close();
                                                                        try {
                                                                            var json = Ext.decode(response.responseText);
                                                                            debug('AJAX param MAX_CAMIONES_PYME_FLOT:', json);
                                                                            if (json.exito !== true) {
                                                                                throw json.respuesta;
                                                                            }
                                                                            
                                                                            var cdtipsitCamiones = json.smap1.P1VALOR;
                                                                            
                                                                            debug('cdtipsitCamiones:', cdtipsitCamiones);
                                                                            
                                                                            if (Ext.isEmpty(cdtipsitCamiones)) {
                                                                                throw 'No aplica validacion';
                                                                            }
                                                                            
                                                                            var nCamiones = 0;
                                                                            
                                                                            for (var i = 0; i < jsonSIGS.slist1.length; i++) {
                                                                                debug('iterando:', jsonSIGS.slist1[i]);
                                                                                if (cdtipsitCamiones.indexOf('|' + jsonSIGS.slist1[i].CDTIPSIT + '|') != -1) {
                                                                                    nCamiones = nCamiones + 1;
                                                                                }
                                                                                debug('nCamiones al final de la iteracion:', nCamiones);
                                                                            }
                                                                            
                                                                            if (nCamiones > 0) {
                                                                                debug('nCamiones SI HAY!');
                                                                                var form = _p54_windowNuevo.down('form');
                                                                                try {
                                                                                    form.remove(form.down('[name=otvalor08]'));
                                                                                } catch(e) {}
                                                                                form.add({
                                                                                    xtype      : 'numberfield',
                                                                                    name       : 'otvalor08',
                                                                                    fieldLabel : 'numero de camiones',
                                                                                    value      : '',
                                                                                    hidden     : true
                                                                                });
                                                                                form.doLayout();
                                                                                form.down('[name=otvalor08]').setValue(nCamiones);
                                                                            } else {
                                                                                nCamiones('nCamiones NO HAY');
                                                                            }
                                                                            
                                                                        } catch (e) {
                                                                            debugError('error al contar camiones (2):', e);
                                                                        }
                                                                    },
                                                                    failure : function() {
                                                                        mascaraContarCamiones.close();
                                                                        throw 'error de comunicacion al cargar parametro MAX_CAMIONES_PYME_FLOT';
                                                                    }
                                                                });
                                                            } catch (e) {
                                                                try {
                                                                    mascaraContarCamiones.close();
                                                                } catch (e) {}
                                                                debugError('error al contar camiones (1):', e);
                                                            }
                                                            
                                                          //Validacion de nivel de siniestralidad
                                                            var mascaraSiniestralidad;
                                                            try {
                                                                mascaraSiniestralidad = _maskLocal();
                                                                var json2 = Ext.decode(json.smap1.valoresCampos);
                                                                if (!Ext.isEmpty(json2.smap1.porcenSin))
                                                                {
                                                              	  debug('Poliza can alto nivel de siniestralidad!');
                                                                    var form = _p54_windowNuevo.down('form');
                                                                    try {
                                                                        form.remove(form.down('[name=otvalor10]'));
                                                                    } catch(e) {}
                                                                    form.add({
                                                                        xtype      : 'numberfield',
                                                                        name       : 'otvalor10',
                                                                        fieldLabel : 'porcentaje siniestralidad',
                                                                        value      : '',
                                                                        hidden     : true
                                                                    });
                                                                    form.doLayout();
                                                                    form.down('[name=otvalor10]').setValue(json2.smap1.porcenSin);
                                                                }
                                                                mascaraSiniestralidad.close();
                                                            } catch (e) {
                                                                try {
                                                                    mascaraSiniestralidad.close();
                                                                } catch (e) {}
                                                                debugError('error al contar camiones (1):', e);
                                                            }
                                                              
                                                        }
                                                    }
                                                }
                                                ,{
                                                    text     : 'Cancelar'
                                                    ,icon    : '${icons}cancel.png'
                                                    ,handler : function(bot)
                                                    {
                                                        me.setValue('');
                                                        estadoCmp.reset();
                                                        me.isValid();
                                                        bot.up('window').destroy();
                                                    }
                                                }
                                            ]
                                        }).show());
                                    }
                                    else
                                    {
                                        mensajeError(json.message || 'No se encuentra la p\u00f3liza '+cduniext+' - '+ramo+' - '+val);
                                        me.setValue('');
                                        estadoCmp.reset();
                                        me.isValid();
                                    }
                                }
                                catch(e)
                                {
                                    me.setValue('');
                                    estadoCmp.reset();
                                    me.isValid();
                                    manejaException(e,ck);
                                }
                            }
                            ,failure : function()
                            {
                                mask.close();
                                me.setValue('');
                                estadoCmp.reset();
                                me.isValid();
                                errorComunicacion(null,'Error Recuperando p\u00f3liza');
                            }
                        });
                    }
                } catch (e) {
                    me.setValue('');
                    estadoCmp.reset();
                    me.isValid();
                    manejaException(e, ck, mask);
                }
            }
        }
    });
    
    (function () {
        var ck = 'Configurando formulario de nuevo tr\u00e1mite';
        try {
            var cdtiptraCmp  = _p54_windowFormBase.down('[name=CDTIPTRA]'),
                cdtipsupCmp  = _p54_windowFormBase.down('[name=CDTIPSUP]'),
                swreqpolCmp  = _p54_windowFormBase.down('[name=SWREQPOL]'),
                cdtipramCmp  = _p54_windowFormBase.down('[name=CDTIPRAM]'),
                swgrupoCmp   = _p54_windowFormBase.down('[name=SWGRUPO]'),
                cdtipfluCmp  = _p54_windowFormBase.down('[name=CDTIPFLU]'),
                cdflujomcCmp = _p54_windowFormBase.down('[name=CDFLUJOMC]');
            cdtipfluCmp.on({
                select : function (me, records) {
                    try {
                        cdtiptraCmp.setValue(records[0].get('aux'));
                        cdtipsupCmp.setValue(records[0].get('aux2'));
                        swreqpolCmp.setValue(records[0].get('aux3'));
                    } catch (e) {
                        debugError(e);
                    }
                }
            });
            cdflujomcCmp.on({
                select : function (me, records) {
                    try {
                        cdtipramCmp.setValue(records[0].get('aux'));
                        swgrupoCmp.setValue(records[0].get('aux2'));
                    } catch (e) {
                        debugError(e);
                    }
                }
            });
        } catch (e) {
            manejaException(e, ck);
        }
    })();
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _p54_registrarTramite(bot)
{
    debug('_p54_registrarTramite');
    var mask, ck = 'Registrando tr\u00e1mite';
    var form = bot.up('form');
    if (Ext.isEmpty(form)) {
        form = bot.up('window').down('form');
    }
    try
    {
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos marcados en rojo';
        }
        
        var values = form.getValues();
        if(Number(values.CDTIPTRA) === 15 && Number(values.CDTIPSUP) === 0)
        {
            debug('se reemplaza el cdtipsup 0 por ',values.CDTIPSUPEND,'.');
            values.CDTIPSUP = values.CDTIPSUPEND;
        }
        
        // SI EL AGENTE QUE VIENE ES EL DE ENDOSO, ESE SE MANDA
        if (Ext.isEmpty(values.CDAGENTE) && !Ext.isEmpty(values.CDAGENTEEND)) {
            values.CDAGENTE = values.CDAGENTEEND;
        }
        
        if (Ext.isEmpty(values.CDRAMO) && !Ext.isEmpty(values.CDRAMOEND)) {
            values.CDRAMO = values.CDRAMOEND;
        }
        
        if (Ext.isEmpty(values.CDTIPSIT) && !Ext.isEmpty(values.CDTIPSITEND)) {
            values.CDTIPSIT = values.CDTIPSITEND;
        }
        
        debug('values:',values,'.');
        
        mask = _maskLocal();
        Ext.Ajax.request(
        {
            url      : _p54_urlRegistrarTramite
            ,params  : _formValuesToParams(values)
            ,success : function(response)
            {
                mask.close();
                var ck = 'Decodificando respuesta al registrar tr\u00e1mite';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### +tramite:',json);
                    if(json.success == true)
                    {                  	
                    	var callbackRegistar = function(bandera) 
                        {
                    		if (bandera==false)	{
                    			mensajeError('No se pudo grabar numero de tr\u00e1mite en sistema sigs',function(){callbackRegistar(true)});
                    		}else{
                    			
	                            mensajeCorrecto
	                            ('Tr\u00e1mite generado','Se gener\u00f3 el tr\u00e1mite '+json.params.ntramite,function()
	                             {
	                                 bot.up('window').hide();
	                                 var form  = _fieldById('_p54_filtroForm');
	                                 var boton = _fieldById('_p54_filtroForm').down('button[text=Buscar]');
	                                 form.getForm().reset();
	                                 form.down('[name=NTRAMITE]').setValue(json.params.ntramite);
	                                 form.down('[name=STATUS]').setValue('0');
	                                 _fieldById('_p54_filtroCmp').reset();
	                                 
	                                 var callbackCheck = function(store, records, success) {
	                                     store.removeListener('load', callbackCheck);
	                                     _p54_mostrarCheckDocumentosInicial(json.params.ntramite);
	                                 };
	                                 
	                                 _p54_store.on({
	                                     load : callbackCheck  
	                                 });
	                                 boton.handler(boton);
	                             }
	                            );
                            }
                        }
                    	
                    	if(json.params.CDTIPTRA == 21)
                    	{
	                    	actualizarMCSigs (values.CDUNIEXT,values.RAMO,values.NMPOLIEX,json.params.ntramite,callbackRegistar)
	                    	
                       }else{
                    	   callbackRegistar(true);
                       }
                    }
                    else
                    {
                        mensajeError(json.message);
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                mask.close();
                errorComunicacion(null,'Error al registrar tr\u00e1mite');
            }
        });
    }
    catch(e)
    {
        manejaException(e, ck, mask);
    }
}

/*
 * Recupera la sucursal de un agente
 * ejecuta el callback enviandole ese cdunieco
 */
function _p54_recuperarSucursalAgente(cdagente, cdtipram, callback)
{
    debug('>_p54_recuperarSucursalAgente args:', arguments);
    var ck = 'Recuperando sucursal de agente';
    try {
        if (Ext.isEmpty(cdagente) || Ext.isEmpty(cdtipram) || Ext.isEmpty(callback)) {
            throw 'No hay par\u00e1metros completos para recuperar sucursal de agente';
        }
        
        _mask(ck);
        Ext.Ajax.request({
            url    : _p54_urlCargarCduniecoAgenteAuto,
            params : {
                'smap1.cdagente' : cdagente,
                'smap1.cdtipram' : cdtipram
            },
            success : function (response) {
                _unmask();
                var ck = 'Decodificando respuesta al recuperar sucursal de agente';
                try {
                    var json = Ext.decode(response.responseText);
                    debug('### sucursal agente:',json,'.');
                    if (json.exito === true) {
                        callback(json.smap1.cdunieco);
                    } else {
                        mensajeError(json.respuesta);
                    }
                } catch (e) {
                    manejaException(e,ck);
                }
            },
            failure : function(response) {
                _unmask();
                errorComunicacion(null, 'Error al recuperar sucursal de agente');
            }
        });
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p54_setearSucursalAgente () {
    debug('_p54_setearSucursalAgente');
    var ck;
    try {
        ck = 'Recuperando componentes';
        
        var agenteCmp   = _fieldByName('CDAGENTE'  , _p54_windowNuevo),
            cdtiptraCmp = _fieldByName('CDTIPTRA'  , _p54_windowNuevo),
            flujoCmp    = _fieldByName('CDFLUJOMC' , _p54_windowNuevo);
        
        debug('agenteCmp:'   , agenteCmp   , '.');
        debug('cdtiptraCmp:' , cdtiptraCmp , '.');
        debug('flujoCmp:'    , flujoCmp    , '.');
        
        var cdagente = agenteCmp.getValue(),
            cdtiptra = cdtiptraCmp.getValue(),
            cdtipram = flujoCmp.findRecordByValue(flujoCmp.getValue()).get('aux');
        
        debug('cdagente:', cdagente, '.');
        debug('cdtiptra:', cdtiptra, '.');
        debug('cdtipram:', cdtipram, '.');
        
        if (!Ext.isEmpty(cdagente) && !Ext.isEmpty(cdtiptra) && Number(cdtiptra) === 1) { // Emision
            debug('Se recuperara sucursal de agente para emision');
            _p54_recuperarSucursalAgente(
                cdagente,
                cdtipram,
                function (cdunieco) {
                    debug('callback despues de recuperar sucursal de agente para emision, cdunieco:', cdunieco, '.');
                    
                    if(!Ext.isEmpty(cdunieco)) {
                        var ck = 'Seteando sucural de agente';
                        try {
                            var sucuDocuComp = _fieldByName('CDSUCDOC', _p54_windowNuevo, true);
                            debug('sucuDocuComp:',sucuDocuComp,'.');
                            
                            if (!Ext.isEmpty(sucuDocuComp)) {
                                sucuDocuComp.setValue(cdunieco);
                            }
                            
                            var sucuExtComp = _fieldByName('CDUNIEXT', _p54_windowNuevo, true);
                            debug('sucuExtComp:', sucuExtComp, '.');
                            
                            if (!Ext.isEmpty(sucuExtComp)) {
                                sucuExtComp.setValue(cdunieco);
                            }
                        } catch (e) {
                            manejaException(e, ck);
                        }
                    } else {
                        agenteCmp.setValue('');
                        mensajeError('El agente no tiene sucursal asociada');
                    }
                }
            );
        } else {
            debug('no se recupera sucursal de agente');
        }
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p54_mostrarCheckDocumentosInicial (ntramite) {
    debug('_p54_mostrarCheckDocumentosInicial args:', arguments);
    var mask, ck = 'Verificando tr\u00e1mite en grid';
    try {
        var indexRecord = _p54_store.find('NTRAMITE', ntramite);
        debug('indexRecord:', indexRecord, '.');
        if (indexRecord !== -1) {
            var _p54_grid = _fieldById('_p54_grid');
            _p54_grid.getSelectionModel().deselectAll();
            debug('select', indexRecord, '.');
            
            _p54_grid.fireEvent('cellclick',null, null, 1, _p54_store.getAt(indexRecord));
            //_p54_grid.getSelectionModel().select(indexRecord);
            
            ck = 'Recuperando checklist de documentos';
            mask = _maskLocal(ck);
            Ext.Ajax.request({
                url    : _p54_urlRecuperarChecklist,
                params : {
                    'params.ntramite' : ntramite
                },
                success : function (response) {
                    mask.close();
                    var ck = 'Decodificando respuesta al recuperar checklist de documentos';
                    try {
                        var json = Ext.decode(response.responseText);
                        debug('### checklist:', json, '.');
                        if (json.success === true) {
                            if (!Ext.isEmpty(json.params.CLAVEDEST)) {
                                var record = _p54_store.getAt(indexRecord);
                                _procesaAccion(
                                        json.params.CDTIPFLU,
                                        json.params.CDFLUJOMC,
                                        'R',
                                        json.params.CLAVEDEST,
                                        json.params.WEBIDDEST,
                                        'INICIAL',
                                        record.get('NTRAMITE'),
                                        record.get('STATUS'),
                                        record.get('CDUNIECO'),
                                        record.get('CDRAMO'),
                                        record.get('ESTADO'),
                                        record.get('NMPOLIZA'),
                                        record.get('NMSITUAC'),
                                        record.get('NMSUPLEM'),
                                        _p54_params.CDUSUARI,
                                        _p54_params.CDSISROL,
                                        null//callback
                                );
                            }
                        } else {
                            mensajeError(json.message);
                        }
                    } catch (e) {
                        manejaException(e, ck);
                    }
                },
                failure : function () {
                    mask.close();
                    errorComunicacion(null, 'Error al recuperar checklist de documentos');
                }
            });
        }
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _hide(comp)
{
    debug('_hide comp:',comp,'.');
    if(!Ext.isEmpty(comp) && typeof comp === 'object')
    {
        //comp.addCls('red');
        //comp.removeCls('green');
        comp.hide();
    }
}

function _show(comp)
{
    debug('_show comp:',comp,'.');
    if(!Ext.isEmpty(comp) && typeof comp === 'object')
    {
        //comp.addCls('green');
        //comp.removeCls('red');
        comp.show();
    }
}

function _p54_cargarTramite(ntramite){
	debug('>_p54_cargarTramite',ntramite);
	var form  = _fieldById('_p54_filtroForm');
    var boton = _fieldById('_p54_filtroForm').down('button[text=Buscar]');
    form.getForm().reset();
    form.down('[name=NTRAMITE]').setValue(ntramite);
    form.down('[name=STATUS]').setValue('0');
    _fieldById('_p54_filtroCmp').reset();
    
    var callbackCheck = function(store, records, success) {
        store.removeListener('load', callbackCheck);
        if(success && records.length > 0){
        	_p54_mostrarCheckDocumentosInicial(records[0].get('NTRAMITE'));
        }
        
    };
    
    _p54_store.on({
        load : callbackCheck
    });
    boton.handler(boton);
    debug('<_p54_cargarTramite');
}

function _p54_nuevoTramiteClic (me) {
    debug('!_p54_nuevoTramiteClic args:', arguments);
    _p54_windowFormBase.down('form').getForm().reset();
    centrarVentanaInterna(_p54_windowFormBase.show());
    _p54_windowFormBase.down('[name=CDTIPFLU]').focus();
}

function _p54_mostrarFormulario (boton, values) {
    debug('!_p54_mostrarFormulario args:', arguments);
    var ck = 'Mostrando formulario';
    try {
        boton.up('window').close();
        var key = values.CDTIPFLU + '_' + values.CDFLUJOMC;
        var window = _p54_formularios[key];
        if (Ext.isEmpty(window)) {
            if (['1_12'    , '1_141'   , '200_246' , '103_264',
                 '1_120'   , '1_180'   , '1_181'   , '200_202',
                 '103_220' , '103_240' , '103_241'].indexOf(key) === -1) { // Si no es ninguno de los iniciales no hay
                throw 'No se encuentra configurado el formulario [' + key + ']';
            }
            _p54_mostrarFormularioPrimeraVersion(values);
        } else {
            window.down('form').getForm().reset();
            centrarVentanaInterna(window.show());
            for (var att in values) {
                try {
                    window.down('[name=' + att + ']').setValue(values[att]);
                } catch (e) {
                    debugError(e);
                }
            }
            setTimeout(function () {
                try {
                    window.down('[name][hidden=false]').focus();
                } catch (e) {
                    debugError(e);
                }
            }, 500);
        }
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p54_mostrarFormularioPrimeraVersion (values) {
    debug('!_p54_mostrarFormularioPrimeraVersion args:', arguments);
    var mask, ck = 'Inyectando formulario';
    try {
        _p54_windowNuevo.showNew();
        mask = _maskLocal(ck);
        var cdtipfluCmp = _p54_windowNuevo.down('[name=CDTIPFLU]');
        cdtipfluCmp.setValue(values.CDTIPFLU);
        cdtipfluCmp.fireEvent('select', cdtipfluCmp, [cdtipfluCmp.findRecordByValue(values.CDTIPFLU)]);
        cdtipfluCmp.fireEvent('blur', cdtipfluCmp);
        setTimeout(function () {
            var cdflujomcCmp = _p54_windowNuevo.down('[name=CDFLUJOMC]');
            cdflujomcCmp.setValue(values.CDFLUJOMC);
            cdflujomcCmp.fireEvent('select', cdflujomcCmp, [cdflujomcCmp.findRecordByValue(values.CDFLUJOMC)]);
            cdflujomcCmp.fireEvent('blur', cdflujomcCmp);
            mask.close();
            setTimeout(function () {
                try {
                    _p54_windowNuevo.down('[name][hidden=false]').focus();
                } catch (e) {
                    debugError(e);
                }
            }, 500);
        }, 1000);
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _btnReasigna(btn){
    var gridTramites = _fieldById('_p54_grid');
    var tramitesReasignar = gridTramites.getPersistedSelection();
    
    debug('Persistent selection: ' , tramitesReasignar);
    
    if(tramitesReasignar.length <= 0){
        showMessage("Aviso","No hay tr\u00e1mites seleccionados para reasignar.", Ext.Msg.OK, Ext.Msg.INFO);
        return;
    }
    
    _ventanaReasignarTramites(tramitesReasignar);   
}

function _ventanaReasignarTramites(tramitesR){
    
    var _URL_CARGA_CATALOGO   = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
    var _CAT_ZONAS_REASIGNAR  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ZONAS_SUCURSALES"/>';
    var _UrlReasignarTramites = '<s:url namespace="/mesacontrol"    action="reasignarTramitesBloque" />';
    
    
    var zonasReasigStore = Ext.create('Ext.data.Store',
            {
                autoLoad : true
                ,model   : 'Generic'
                ,proxy   :
                {
                    type        : 'ajax'
                        ,url        : _URL_CARGA_CATALOGO
                        ,extraParams: {catalogo:_CAT_ZONAS_REASIGNAR}
                        ,reader     :
                        {
                            type  : 'json'
                            ,root : 'lista'
                        }
                }
            });
    
    var gridZonasReasig = Ext.create('Ext.grid.Panel',
            {
            title : 'Seleccione una zona para reasignar los tr\u00e1mites seleccionados.'
            ,height : 240
            ,store : zonasReasigStore
            ,columns :
            [ { header     : 'C&oacute;digo Zona' , dataIndex : 'key', flex: 1, hidden: true},
              { header     : 'Nombre de la Zona' , dataIndex : 'value', flex: 3}
            ]
        });
    
    
        var windowReasignar = Ext.create('Ext.window.Window', {
              modal:true,
              height : 320,
              width  : 420,
              border: false,
              items: [gridZonasReasig],
              bodyStyle:'padding:5px;',
              buttonAlign : 'center',
              buttons:[
               {
                     text: 'Reasignar tr\u00e1mites',
                     icon:'${icons}user_go.png',
                     handler: function() {
                         
                         var selModel =  gridZonasReasig.getSelectionModel();
                         if(selModel.hasSelection()){
                             var record = selModel.getLastSelected();
                             
                             var tramitesMsg = '';
                             var tramitesReasig = [];
                             
                             Ext.Array.each(tramitesR,function(tramiteR, indexTram){
                                 tramitesReasig.push(tramiteR.data);
                                 if(Ext.isEmpty(tramitesMsg)){
                                     tramitesMsg = tramitesMsg + tramiteR.get('NTRAMITE');
                                 }else{
                                     tramitesMsg = tramitesMsg + ', ' + tramiteR.get('NTRAMITE');                                         
                                 }
                                 
                             });
                             
                             centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Se reasignar\u00e1n los tr\u00e1mites: '+ tramitesMsg+'<br> A la zona: '+record.get('value')+' <br>¿Desea continuar?', function(btn)
                              {
                                  if(btn === 'yes')
                                  {
                                      mask = _maskLocal('Reasignando tr\u00e1mites...');
                                      Ext.Ajax.request({
                                          url    : _UrlReasignarTramites,
                                          jsonData : {
                                              slist1 : tramitesReasig,
                                              smap1  : {
                                                  'ZONA_REASIG': record.get('key')
                                              }
                                          },
                                          success : function (response) {
                                              mask.close();
                                              var ck = 'Decodificando respuesta al reasignar tramites';
                                              try {
                                                  var json = Ext.decode(response.responseText);
                                                  debug('### respuesta reasingacion:', json, '.');
                                                  
                                                  if (json.success === true) {
                                                      
                                                      mensajeCorrecto('Aviso','Se ha reasignado correctamente.'+json.smap1.resultadosReasignacion);
                                                      windowReasignar.close();
                                                      
                                                      var gridTramites = _fieldById('_p54_grid');
                                                      gridTramites.deslectAllPersistedSelection();
                                                      gridTramites.getStore().reload();
                                                      
                                                      
                                                  } else {
                                                      mensajeError(json.mensaje);
                                                  }
                                                  
                                              } catch (e) {
                                                  manejaException(e, ck);
                                              }
                                          },
                                          failure : function () {
                                              mask.close();
                                              errorComunicacion(null, 'Error al reasignar tr\u00e1mites.');
                                          }
                                      });
                                  }
                              }));
                             
                         }else{
                             showMessage("Aviso","Debe seleccionar una zona.", Ext.Msg.OK, Ext.Msg.INFO);
                         }
                         
                     }
               }
              ]
              });
            windowReasignar.show();
}


////// funciones //////
</script>

<script type="text/javascript" src="${ctx}/js/proceso/flujoMesaControl/mesaControlScripts.js?now=${now}"></script>

</head>
<body>
<div id="_p54_divpri" style="height:800px;border:0px solid #CCCCCC;"></div>
</body>
</html>