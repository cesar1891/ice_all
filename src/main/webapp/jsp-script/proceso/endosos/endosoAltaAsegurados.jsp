<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p59_urlRecuperarDatosEndoso  = '<s:url namespace = "/endosos"   action = "recuperarDatosEndosoPendiente"          />',
    _p59_urlGuardarFechaEfecto    = '<s:url namespace = "/endosos"   action = "guardarFechaEfectoEndosoPendiente"      />',
    _p59_urlGuardarAsegurado      = '<s:url namespace = "/endosos"   action = "guardarAseguradoParaEndosoAlta"         />',
    _p59_urlSacaendoso            = '<s:url namespace = "/endosos"   action = "sacaendoso"                             />',
    _p59_urlPantallaDomicilios    = '<s:url namespace = "/catalogos" action = "includes/editarDomicilioAsegurado"      />',
    _p59_urlPantallaExclusiones   = '<s:url namespace = "/"          action = "pantallaExclusion"                      />',
    _p59_urlTarificar             = '<s:url namespace = "/endosos"   action = "tarificarEndosoAltaAsegurados"          />',
    _p59_urlPantallaBeneficiarios = '<s:url namespace = "/catalogos" action = "includes/pantallaBeneficiarios"         />',
    _p59_urlConfirmarEndosoFlujo  = '<s:url namespace = "/endosos"   action = "confirmarEndosoSaludFlujo"              />';
////// urls //////

////// variables //////
var _p59_storeAseguradosNuevos;

var _p59_flujo  = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />,
    _p59_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;

debug('_p59_flujo:', _p59_flujo);
debug('_p59_params:', _p59_params);

var _callbackDomicilioAseg;        // Variable para el callback de la ventana de domicilios
var _callbackAseguradoExclusiones; // Variable para el callback de la ventana de exclusiones
////// variables //////

////// overrides //////
////// overrides //////

Ext.onReady(function()
{
    ////// componentes dinamicos //////
    var _p59_mpersonaItems = [ <s:property value = "items.mpersonaItems" escapeHtml = "false" /> ];
    var _p59_mpoliperItems = [ <s:property value = "items.mpoliperItems" escapeHtml = "false" /> ];
    var _p59_tatrisitItems = [ <s:property value = "items.tatrisitItems" escapeHtml = "false" /> ];
    //var _p59_tatrirolItems = [ <%-- s:property value = "items.tatrirolItems" escapeHtml = "false" / --%> ];
    
    debug('inicio de mpersona items');
    for (var i = 0; i < _p59_mpersonaItems.length; i++) {
        debug(_p59_mpersonaItems[i].name);
    }
    debug('fin de mpersona items');

    var _p59_datosPersonaItems = [];
    for (var i = 0; i < _p59_mpersonaItems.length ; i++) {
        _p59_datosPersonaItems.push(_p59_mpersonaItems[i]);
    }
    for (var i = 0; i < _p59_mpoliperItems.length ; i++) {
        _p59_datosPersonaItems.push(_p59_mpoliperItems[i]);
    }
    for (var i = 0; i < _p59_tatrisitItems.length ; i++) {
        _p59_datosPersonaItems.push(_p59_tatrisitItems[i]);
     }
    ////// componentes dinamicos //////
    
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    _p59_storeAseguradosNuevos = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'CDPERSON', 'NMSITUAC', 'DSPARENT', 'DSNOMBRE', 'DSSEXO', { type : 'date', name : 'FENACIMI', dateFormat : 'd/m/Y' }, 'NMORDDOM',
            'CDRFC'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_ASEGURADOS_ENDOSO_ALTA',
                'params.cdunieco' : _p59_flujo.cdunieco,
                'params.cdramo'   : _p59_flujo.cdramo,
                'params.estado'   : _p59_flujo.estado,
                'params.nmpoliza' : _p59_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function () {
            debug('_p59_storeAseguradosNuevos.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.nmsuplem' : _p59_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p59_storeAseguradosNuevos.load().callback() args:', arguments);
                    if (!success) {
                        mensajeError(op.error);
                    }
                }
            });
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p59_panelpri'
        ,renderTo : '_p59_divpri'
        ,title    : 'ENDOSO DE ALTA DE ASEGURADOS'
        ,border   : 0
        ,defaults : { style : 'margin : 5px;' }
        ,items    :
        [
            Ext.create('Ext.panel.Panel', {
                itemId      : '_p59_panelFlujo',
                title       : 'ACCIONES',
                hidden      : Ext.isEmpty(_p59_flujo),
                buttonAlign : 'left',
                buttons     : [],
                listeners   : {
                    afterrender : function (me) {
                        if (!Ext.isEmpty(_p59_flujo)) {
                            _cargarBotonesEntidad(
                                _p59_flujo.cdtipflu,
                                _p59_flujo.cdflujomc,
                                _p59_flujo.tipoent,
                                _p59_flujo.claveent,
                                _p59_flujo.webid,
                                me.itemId, // callback
                                _p59_flujo.ntramite,
                                _p59_flujo.status,
                                _p59_flujo.cdunieco,
                                _p59_flujo.cdramo,
                                _p59_flujo.estado,
                                _p59_flujo.nmpoliza,
                                _p59_flujo.nmsituac,
                                _p59_flujo.nmsuplem,
                                null // callbackDespuesProceso
                            );
                        }
                    }
                }
            }), {
                xtype     : 'panel'
                ,layout   : 'hbox'
                ,border   : 0
                ,defaults : { style : 'margin : 5px;' }
                ,items    :
                [
                    {
                        xtype       : 'datefield'
                        ,fieldLabel : 'Fecha de efecto'
                        ,format     : 'd/m/Y'
                        ,name       : 'FEEFECTO'
                        ,allowBlank : false
                        ,minValue   : _p59_params.FEEFECTO
                        ,maxValue   : _p59_params.FEPROREN
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Confirmar fecha'
                        ,itemId  : '_p59_botonConfirmarFecha'
                        ,icon    : '${icons}accept.png'
                        ,handler : _p59_confirmarFechaClic
                    }
                ]
            }
            ,{
                xtype       : 'grid'
                ,title      : 'ASEGURADOS NUEVOS'
                ,itemId     : '_p59_gridAseguradosNuevos'
                ,width      : 800
                ,height     : 300
                ,autoScroll : true
                ,hidden     : true
                ,tbar       :
                [
                    {
                        text     : 'Agregar'
                        ,icon    : '${icons}add.png'
                        ,handler : _p59_agregarClic
                    }
                ]
                ,store      : _p59_storeAseguradosNuevos
                ,columns    :
                [
                    {
                        text          : 'NO.'
                        ,dataIndex    : 'NMSITUAC'
                        ,width        : 50
                        ,menuDisabled : true
                        ,sortable     : false
                    }
                    ,{
                        text       : 'PARENTESCO'
                        ,dataIndex : 'DSPARENT'
                        ,width     : 100
                    }
                    ,{
                        text       : 'NOMBRE'
                        ,dataIndex : 'DSNOMBRE'
                        ,width     : 300
                    }
                    ,{
                        text       : 'SEXO'
                        ,dataIndex : 'DSSEXO'
                        ,width     : 100
                    }
                    ,{
                        xtype       : 'datecolumn'
                        ,text       : 'FECHA DE<br/>NACIMIENTO'
                        ,dataIndex  : 'FENACIMI'
                        ,width      : 100
                        ,format     : 'd/m/Y'
                    }
                    ,{
                        xtype         : 'actioncolumn'
                        ,menuDisabled : true
                        ,sortable     : false
                        ,width        : 100
                        ,items        :
                        [
                            {
                                tooltip  : 'Editar'
                                ,icon    : '${icons}pencil.png'
                                ,handler : function(me,row,col,item,e,record)
                                {
                                    _fieldById('_p59_formAsegurado').mostrarParaEditar(record);
                                }
                            }, {
                                tooltip : 'Domicilio',
                                icon    : '${icons}report_key.png',
                                handler : function (me, row, col, item, e, record) {
                                    debug('ASEGURADOS NUEVOS Domicilio handler() args:', arguments);
                                    var ven = centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                        title  : 'EDITAR DOMICILIO',
                                        width  : 500,
                                        height : 350,
                                        modal  : true,
                                        loader : {
                                            url      : _p59_urlPantallaDomicilios,
                                            autoLoad : true,
                                            scripts  : true,
                                            params   : {
                                                'params.cdperson' : record.get('CDPERSON'),
                                                'params.nmorddom' : record.get('NMORDDOM')
                                            }
                                        }
                                    }).show()); 
                                    _callbackDomicilioAseg = function () {
                                        ven.close();
                                    };
                                }
                            }, {
                                tooltip : 'Exclusiones',
                                icon    : '${icons}lock.png',
                                handler : function (me, row, col, item, e, record) {
                                    debug('ASEGURADOS NUEVOS Exclusiones handler() args:', arguments);
                                    var ven = centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                        title  : 'EDITAR EXCLUSIONES',
                                        width  : 700,
                                        height : 500,
                                        modal  : true,
                                        loader : {
                                            url      : _p59_urlPantallaExclusiones,
                                            autoLoad : true,
                                            scripts  : true,
                                            params   : {
                                                'smap1.pv_cdunieco'     : _p59_flujo.cdunieco,
                                                'smap1.pv_cdramo'       : _p59_flujo.cdramo,
                                                'smap1.pv_estado'       : _p59_flujo.estado,
                                                'smap1.pv_nmpoliza'     : _p59_flujo.nmpoliza,
                                                'smap1.pv_nmsituac'     : record.get('NMSITUAC'),
                                                'smap1.pv_nmsuplem'     : _p59_params.nmsuplem,
                                                'smap1.pv_cdperson'     : record.get('CDPERSON'),
                                                'smap1.pv_cdrol'        : '2',
                                                'smap1.nombreAsegurado' : record.get('DSNOMBRE'),
                                                'smap1.cdrfc'           : record.get('CDRFC'),
                                                'smap1.botonCopiar'     : '0'
                                            }
                                        }
                                    }).show()); 
                                    _callbackAseguradoExclusiones = function () {
                                        ven.close();
                                    };
                                }
                            }, {
                                tooltip : 'Beneficiarios',
                                icon    : '${icons}money.png',
                                handler : function (me, row, col, item, e, record) {
                                    debug('ASEGURADOS NUEVOS Beneficiarios handler() args:', arguments);
                                    var ven = centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                        title  : 'EDITAR BENEFICIARIOS',
                                        width  : 800,
                                        height : 400,
                                        modal  : true,
                                        loader : {
                                            url      : _p59_urlPantallaBeneficiarios,
                                            autoLoad : true,
                                            scripts  : true,
                                            params   : {
                                                'smap1.ntramite'     : _p59_flujo.ntramite,
                                                'smap1.cdunieco'     : _p59_flujo.cdunieco,
                                                'smap1.cdramo'       : _p59_flujo.cdramo,
                                                'smap1.estado'       : _p59_flujo.estado,
                                                'smap1.nmpoliza'     : _p59_flujo.nmpoliza,
                                                'smap1.nmsuplem'     : _p59_params.nmsuplem,
                                                'smap1.nmsituac'     : record.get('NMSITUAC'),
                                                'smap1.cdrolPipes'   : '3',
                                                'smap1.cdtipsup'     : '27',
                                                'smap1.ultimaImagen' : 'N',
                                                'smap1.sinConfirmar' : 'S'
                                            }
                                        }
                                    }).show());
                                }
                            }/*, {
                                tooltip  : 'Quitar'
                                ,icon    : '${icons}delete.png'
                                ,handler : function(me,row,col,item,e,record)
                                {
                                    _p59_storeAseguradosNuevos.remove(record);
                                }
                            }*/
                        ]
                    }
                ]
            }
            ,{
                xtype     : 'form'
                ,title    : 'DATOS DE ASEGURADO'
                ,itemId   : '_p59_formAsegurado'
                ,border   : 0
                ,hidden   : true
                ,defaults : { style : 'margin : 5px;' }
                ,layout   : {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    : _p59_datosPersonaItems
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar y continuar'
                        ,icon    : '${icons}disk.png'
                        ,handler : _p59_formAseguradosGuardarClic
                    }
                    ,{
                        text     : 'Cancelar'
                        ,icon    : '${icons}cancel.png'
                        ,handler : function(me)
                        {
                            me.up('form').cerrar();
                        }
                    }
                ]
                ,mostrarParaNuevo : function()
                {
                    debug('_p59_formAsegurado.mostrarParaNuevo args:',arguments);
                    var me = this;
                    me.getForm().reset();
                    me.down('[name=ACCION]').setValue('I');
                    _fieldById('_p59_gridAseguradosNuevos').hide();
                    me.show();
                }
                ,mostrarParaEditar : function(record)
                {
                    debug('_p59_formAsegurado.mostrarParaEditar args:',arguments);
                    var me = this;
                    var mask, ck = 'Recuperando datos de asegurado';
                    try {
                        mask = _maskLocal(ck);
                        Ext.Ajax.request({
                            url     : _GLOBAL_URL_RECUPERACION,
                            params  : {
                                'params.consulta' : 'RECUPERAR_ASEGURADO_COMPLETO_ENDOSO_ALTA',
                                'params.cdunieco' : _p59_flujo.cdunieco,
                                'params.cdramo'   : _p59_flujo.cdramo,
                                'params.estado'   : _p59_flujo.estado,
                                'params.nmpoliza' : _p59_flujo.nmpoliza,
                                'params.nmsuplem' : _p59_params.nmsuplem,
                                'params.nmsituac' : record.get('NMSITUAC')
                            },
                            success : function (response) {
                                mask.close();
                                var ck = 'Decodificando respuesta al recuperar datos de asegurado';
                                try {
                                    var jsonAsegurado = Ext.decode(response.responseText);
                                    debug('AJAX jsonAsegurado:', jsonAsegurado);
                                    if (jsonAsegurado.success !== true) {
                                        throw jsonAsegurado.message;
                                    }
                                    me.getForm().reset();
                                    _fieldById('_p59_gridAseguradosNuevos').hide();
                                    me.getForm().loadRecord({
                                        getData : function () {
                                            return jsonAsegurado.params;
                                        }
                                    });
                                    me.down('[name=ACCION]').setValue('U');
                                    me.show();
                                } catch (e) {
                                    manejaException(e, ck);
                                }
                            },
                            failure : function () {
                                mask.close();
                                errorComunicacion(null, 'Error al recuperar datos de asegurado');
                            }
                        });
                    } catch (e) {
                        manejaException(e, ck, mask);
                    }
                }
                ,cerrar : function()
                {
                    debug('_p59_formAsegurado.cerrar');
                    var me = this;
                    me.hide();
                    _fieldById('_p59_gridAseguradosNuevos').show();
                }
            }
        ]
        ,buttonAlign : 'center'
        ,buttons     : [
            {
                text    : 'Validar datos y tarificar',
                icon    : '${icons}key.png',
                handler : _p59_tarificarClic
            }, {
                text    : 'Borrar cambios',
                icon    : '${icons}delete.png',
                handler : _p59_borrarClic
            }
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    _p59_ocultarRepetidos();
    
    _p59_agregarFuncionamientoRFC();
    ////// custom //////
    
    ////// loaders //////
    _p59_recuperarDatosEndoso();
    ////// loaders //////
});

////// funciones //////
function _p59_confirmarFechaClic(me)
{
    debug('_p59_confirmarFechaClic() args:',arguments);
    
    if(!_fieldByName('FEEFECTO').isValid())
    {
        return datosIncompletos();
    }
    
    var mask, ck = 'Guardando fecha de efecto';
    try {
        var params = {
            'params.fecha'    : _fieldByName('FEEFECTO').getSubmitValue(),
            'params.cdtipsup' : '9'
        };
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p59_urlGuardarFechaEfecto,
            params  : _flujoToParams(_p59_flujo, params),
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al guardar fecha de efecto';
                try {
                    var datosEndoso = Ext.decode(response.responseText);
                    debug('AJAX datosEndoso:', datosEndoso);
                    if (datosEndoso.success === true) {
                        _p59_params.fesolici = datosEndoso.params.pv_fesolici_o;
                        _p59_params.nsuplogi = datosEndoso.params.pv_nsuplogi_o;
                        _p59_params.nmsuplem = datosEndoso.params.pv_nmsuplem_o;
                        debug('_p59_params:', _p59_params);
                        _p59_bloquearFecha(true);
                    } else {
                        mensajeError(datosEndoso.message);
                        _fieldByName('FEEFECTO').setValue('');
                    }
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al guardar fecha de efecto');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _p59_bloquearFecha(bloqueo)
{
    debug('_p59_bloquearFecha() args:', arguments);
    if (bloqueo === true) {
        _fieldByName('FEEFECTO').setReadOnly(true);
        _fieldById('_p59_botonConfirmarFecha').hide();
        _fieldById('_p59_gridAseguradosNuevos').show();
        _fieldById('_p59_formAsegurado').hide();
        _p59_storeAseguradosNuevos.cargar();
    } else {
        _fieldByName('FEEFECTO').setReadOnly(false);
        _fieldById('_p59_botonConfirmarFecha').show();
        _fieldById('_p59_gridAseguradosNuevos').hide();
        _fieldById('_p59_formAsegurado').hide();
    }
}

function _p59_agregarClic(me)
{
    debug('>_p59_agregarClic args:',arguments);
    _fieldById('_p59_formAsegurado').mostrarParaNuevo();
}

function _p59_formAseguradosGuardarClic(me)
{
    debug('_p59_formAseguradosGuardarClic args:',arguments);
    
    var mask, ck = 'Guardando datos';
    try
    {
        var form = me.up('form');
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        var values = form.getValues();
        for (var att in _p59_params) {
            values[att] = _p59_params[att];
        }
        
        values.feendoso = _fieldByName('FEEFECTO').getSubmitValue();
        values.cdunieco = _p59_flujo.cdunieco;
        values.cdramo   = _p59_flujo.cdramo;
        values.estado   = _p59_flujo.estado;
        values.nmpoliza = _p59_flujo.nmpoliza;
        
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p59_urlGuardarAsegurado,
            params  : _formValuesToParams(values),
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al guardar asegurado';
                try {
                    var jsonAsegurado = Ext.decode(response.responseText);
                    debug('AJAX jsonAsegurado:', jsonAsegurado);
                    if (jsonAsegurado.success === true) {
                        form.cerrar();
                        _p59_storeAseguradosNuevos.cargar();
                    } else {
                        mensajeError(jsonAsegurado.message);
                    }
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al guardar asegurado');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,mask);
    }
}

function _p59_borrarClic(me)
{
    debug('_p59_borrarClic() args:',arguments);
    Ext.MessageBox.confirm(
        'Confirmar',
        '¿Est&aacute; seguro que desea borrar todos los cambios?',
        function (btn) {
            if (btn === 'yes') {
                _p59_sacaendoso();
            }
        }
    );
}

function _p59_sacaendoso (callback) {
    debug('_p59_sacaendoso()');
    var mask, ck = 'Borrando cambios';
    try {
        if (Ext.isEmpty(_p59_params.nmsuplem)) {
            throw 'No hay cambios';
        }
        mask = _maskLocal('Borrando cambios');
        Ext.Ajax.request({
            url     : _p59_urlSacaendoso,
            params  : {
                'params.cdunieco' : _p59_flujo.cdunieco,
                'params.cdramo'   : _p59_flujo.cdramo,
                'params.estado'   : _p59_flujo.estado,
                'params.nmpoliza' : _p59_flujo.nmpoliza,
                'params.nmsuplem' : _p59_params.nmsuplem,
                'params.nsuplogi' : _p59_params.nsuplogi
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al borrar cambios';
                try {
                    var jsonSacaendoso = Ext.decode(response.responseText);
                    debug('AJAX jsonSacaendoso:', jsonSacaendoso);
                    if (jsonSacaendoso.success === true) {
                        _p59_params.nsuplogi = '';
                        _p59_params.nmsuplem = '';
                        _p59_params.fesolici = '';
                        _fieldByName('FEEFECTO').setValue('');
                        _p59_bloquearFecha(false);
                        if (!Ext.isEmpty(callback) && typeof callback === 'function') {
                            callback();
                        } else {
                            mensajeCorrecto('Datos borrados', 'Se han borrado todos los cambios');
                        }
                    } else {
                        mensajeError(jsonSacaendoso.message);
                    }
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al borrar cambios');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _p59_tarificarClic(me)
{
    debug('_p59_tarificarClic() args:',arguments);
    var mask, ck;
    try {
        if (_p59_storeAseguradosNuevos.getCount() === 0) {
            throw 'No hay nuevos asegurados';
        }
        ck = 'Tarificando nuevos asegurados';
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p59_urlTarificar,
            params  : {
                'params.cdunieco' : _p59_flujo.cdunieco,
                'params.cdramo'   : _p59_flujo.cdramo,
                'params.estado'   : _p59_flujo.estado,
                'params.nmpoliza' : _p59_flujo.nmpoliza,
                'params.nmsuplem' : _p59_params.nmsuplem,
                'params.feinival' : _fieldByName('FEEFECTO').getSubmitValue()
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al tarificar asegurados nuevos';
                try {
                    var jsonTarifa = Ext.decode(response.responseText);
                    debug('AJAX jsonTarifa:', jsonTarifa);
                    if (jsonTarifa.success !== true) {
                        throw jsonTarifa.message;
                    }
                    for (var i = 0; i < jsonTarifa.list.length; i++) {
                        var e = jsonTarifa.list[i];
                        if (e.NMSITUAC == 999999) {
                            e.AGRUPADOR = 'CONCEPTOS GLOBALES';
                        } else {
                            e.AGRUPADOR = e.NMSITUAC + '. ' + e.ASEGURADO;
                        }
                        e.PRIMA = e.IMPORTE;
                    }
                    Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES + 'VentanaTarifa');
                    new VentanaTarifa({
                        title   : 'TARIFA DEL ENDOSO',
                        datos   : jsonTarifa.list,
                        buttons : [
                            {
                                text    : 'Confirmar',
                                icon    : '${icons}key.png',
                                hidden  : _p59_params.permisoEmitir !== 'S',
                                handler : _p59_confirmarClic
                            }, {
                                text    : 'Autorizar',
                                icon    : '${icons}key.png',
                                hidden  : _p59_params.permisoAutorizar !== 'S',
                                handler : _p59_autorizarClic
                            }
                        ]
                    }).mostrar();
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al tarificar asegurados nuevos');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _p59_mesacontrol()
{
    debug('_p59_mesacontrol');
    _mask('Redireccionando...');
    Ext.create('Ext.form.Panel').submit(
    {
        standardSubmit : true
        ,url           : _GLOBAL_COMP_URL_MCFLUJO
    });
}

function _p59_confirmarEndosoClic(me)
{
    debug('_p59_confirmarEndosoClic args:',arguments);
    mensajeCorrecto(
        'Endoso confirmado'
        ,'Se ha confirmado el endoso 1'
        ,function()
        {
            me.up('window').closable = false;
            me.up('window').down('[text=Confirmar]').hide();
            me.up('window').down('[text=Documentos]').show();
        }
    );
}

function _p59_recuperarDatosEndoso () {
    debug('_p59_recuperarDatosEndoso()');
    var mask, ck = 'Recuperando datos de endoso';
    try {
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p59_urlRecuperarDatosEndoso,
            params  : _flujoToParams(_p59_flujo, { 'params.cdtipsup' : 9 }),
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al recuperar datos de endoso';
                try {
                    var datosEndoso = Ext.decode(response.responseText);
                    debug('AJAX datosEndoso:', datosEndoso);
                    if (datosEndoso.success === true) {
                        if (!Ext.isEmpty(datosEndoso.params.FEINIVAL)) {
                            _p59_params.nmsuplem = datosEndoso.params.NMSUPLEM;
                            _p59_params.nsuplogi = datosEndoso.params.NSUPLOGI;
                            _p59_params.fesolici = datosEndoso.params.FESOLICI;
                            debug('_p59_params:', _p59_params);
                            _fieldByName('FEEFECTO').setValue(datosEndoso.params.FEINIVAL);
                            _p59_bloquearFecha(true);
                        }
                    } else {
                        mensajeError(datosEndoso.message);
                    }
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al recuperar datos de endoso');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _p59_agregarListenerImitadores (cmp, imitadores) {
    debug('_p59_agregarListenerImitadores() args[]:', arguments);
    if (imitadores.length > 0) {
        cmp.imitadores = imitadores;
        for (var i = 0; i < imitadores.length; i++) {
            _hide(imitadores[i]);
        }
        cmp.on({
            change : function (me, val) {
                for (var i = 0; i < me.imitadores.length ; i++) {
                    me.imitadores[i].setValue(val);
                }
            }
        });
    }
}

function _p59_ocultarRepetidos () {
    debug('_p59_ocultarRepetidos()');
    var sexoCmp = _fieldByName('OTSEXO');
    var otrosSexo = Ext.ComponentQuery.query('[name*=otvalor][fieldLabel*=SEXO]');
    debug('otrosSexo:', otrosSexo);
    _p59_agregarListenerImitadores(sexoCmp, otrosSexo);
    var fenacimiCmp = _fieldByName('FENACIMI');
    var otrosFenacimi = Ext.ComponentQuery.query('[name*=otvalor][fieldLabel*=NACIMI]');
    debug('otrosFenacimi:', otrosFenacimi);
    _p59_agregarListenerImitadores(fenacimiCmp, otrosFenacimi);
}

function _hide (comp) {
    debug('_hide comp:',comp,'.');
    if (!Ext.isEmpty(comp) && typeof comp === 'object') {
        //comp.addCls('red');
        //comp.removeCls('green');
        comp.hide();
    }
}

function _show (comp) {
    debug('_show comp:',comp,'.');
    if (!Ext.isEmpty(comp) && typeof comp === 'object') {
        //comp.addCls('green');
        //comp.removeCls('red');
        comp.show();
    }
}

function _p59_agregarFuncionamientoRFC () {
    debug('_p59_agregarFuncionamientoRFC()');
    var ck = 'Agregando funcionamiento de recuperaci\u00f3n de asegurados';
    try {
        var rfcCmp = _fieldByName('CDRFC');
        rfcCmp.on({
            change : function (me) {
                _fieldByName('CDPERSON').setValue('');
                _fieldByName('SWEXIPER').setValue('N');
            },
            blur : function (me) {
                debug('CDRFC.blur!');
                var mask, ck = 'Verificando coincidencias de RFC';
                try {
                    if (Ext.isEmpty(me.getValue()) || me.getValue().length < 6) {
                        return;
                    }
                    mask = _maskLocal(ck);
                    Ext.Ajax.request({
                        url     : _GLOBAL_URL_RECUPERACION,
                        params  : {
                            'params.consulta' : 'RECUPERAR_PERSONAS_FISICAS_POR_RFC_MULTIPLE_DOMICILIO',
                            'params.rfc'      : me.getValue()
                        },
                        success : function (response) {
                            mask.close();
                            var ck = 'Decodificando respuesta al buscar coincidencias de RFC';
                            try {
                                var jsonRFC = Ext.decode(response.responseText);
                                debug('AJAX jsonRFC:', jsonRFC);
                                if (jsonRFC.success !== true) {
                                    throw jsonRFC.message;
                                }
                                if (jsonRFC.list.length === 0) {
                                    return;
                                }
                                var rfcRepetido = false;
                                for (var i = 0; i < jsonRFC.list.length; i++) {
                                    if (me.getValue() === jsonRFC.list[i].RFC) {
                                        rfcRepetido = true;
                                        break;
                                    }
                                }
                                centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                    title       : 'PERSONAS EXISTENTES',
                                    modal       : true,
                                    closable    : !rfcRepetido,
                                    buttonAlign : 'center',
                                    buttons     : [
                                        {
                                            text    : 'CANCELAR',
                                            icon    : '${icons}cancel.png',
                                            hidden  : !rfcRepetido,
                                            handler : function (me) {
                                                debug('PERSONAS EXISTENTES.CANCELAR.handler()');
                                                me.up('window').close();
                                                _fieldByName('CDRFC').setValue('');
                                                _fieldByName('CDRFC').isValid();
                                            }
                                        }
                                    ],
                                    items       : [
                                        Ext.create('Ext.grid.Panel', {
                                            width   : 700,
                                            height  : 300,
                                            border  : 0,
                                            columns : [
                                                {
                                                    xtype   : 'actioncolumn',
                                                    icon    : '${icons}accept.png',
                                                    width   : 30,
                                                    handler : function (v, row, col, item, e, record) {
                                                        debug('PERSONAS EXISTENTES actioncolumn handler() args:', arguments);
                                                        _p59_recuperarPersona(record.get('CDPERSON'), record.get('NMORDDOM'), v.up('window'));
                                                    }
                                                },{
                                                   header     : 'ID. Asegurado',
                                                   dataIndex  : 'CDPERSON',
                                                   width      : 120
                                                }, {
                                                    text      : 'RFC',
                                                    dataIndex : 'RFC',
                                                    width     : 120
                                                }, {
                                                    text      : 'NOMBRE',
                                                    dataIndex : 'NOMBRE',
                                                    width     : 150
                                                }, {
                                                    text      : 'DIRECCI\u00d3N',
                                                    dataIndex : 'DIRECCION',
                                                    flex      : 1
                                                }
                                            ],
                                            store : Ext.create('Ext.data.Store', {
                                                fields : [ "CDPERSON", "NMORDDOM", "RFC", "NOMBRE", "DIRECCION" ],
                                                data : jsonRFC.list
                                            })
                                        })
                                    ]
                                }).show());
                            } catch (e) {
                                manejaException(e, ck);
                            }
                        },
                        failure : function () {
                            mask.close();
                            errorComunicacion(null, 'Error al buscar coincidencias de RFC');
                        }
                    });
                } catch (e) {
                    manejaException(e, ck, mask);
                }
            }
        });
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p59_recuperarPersona (cdperson, nmorddom, window) {
    debug('_p59_recuperarPersona() args:', arguments);
    var mask, ck = 'Recuperando datos de persona';
    try {
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _GLOBAL_URL_RECUPERACION,
            params  : {
                'params.consulta' : 'RECUPERAR_PERSONA_ENDOSO_ALTA',
                'params.cdperson' : cdperson
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al recuperar datos de persona';
                try {
                    var jsonPersona = Ext.decode(response.responseText);
                    debug('AJAX jsonPersona:', jsonPersona);
                    if (jsonPersona.success !== true) {
                        throw jsonPersona.message;
                    }
                    for (var att in jsonPersona.params) {
                        try {
                            _fieldByName(att, _fieldById('_p59_formAsegurado'), true).setValue(jsonPersona.params[att]);
                        } catch (e) {}
                    }
                    _fieldByName('CDPERSON').setValue(cdperson);
                    _fieldByName('NMORDDOM').setValue(nmorddom);
                    _fieldByName('SWEXIPER').setValue('S');
                    window.close();
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al recuperar datos de persona');
            }
        });
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p59_confirmarClic (me) {
    debug('_p59_confirmarClic() args:', arguments);
    _p59_confirmar(me, false);
}

function _p59_autorizarClic (me) {
    debug('_p59_confirmarClic() args:', arguments);
    _p59_confirmar(me, true);
}

function _p59_confirmar (button, autorizar) {
    debug('_p59_confirmar() args:', arguments);
    var mask, ck;
    try {
        ck = 'Recopilando par\u00e1metros';
        var params = _flujoToParams(_p59_flujo);
        params['params.nmsuplem'] = _p59_params.nmsuplem;
        params['params.nsuplogi'] = _p59_params.nsuplogi;
        params['params.fesolici'] = _p59_params.fesolici;
        params['params.feinival'] = _fieldByName('FEEFECTO').getSubmitValue();
        params['params.autoriza'] = autorizar === true ? 'S' : 'N';
        params['params.cdtipsup'] = '9';
        debug('params:', params);
        ck = 'Confirmando endoso';
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p59_urlConfirmarEndosoFlujo,
            params  : params,
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al confirmar endoso';
                try {
                    var jsonConfirmar = Ext.decode(response.responseText);
                    debug('AJAX jsonConfirmar:', jsonConfirmar);
                    if (jsonConfirmar.success !== true) {
                        throw jsonConfirmar.message;
                    }
                    mensajeCorrecto(
                        'Endoso confirmado',
                        jsonConfirmar.params.message,
                        function () {
                            if (jsonConfirmar.params.confirmado === 'S') {
                                var callbackRemesa = function () {
                                    try {
                                        //////////////////////////////////
                                        ////// usa codigo del padre //////
                                        /*// ////////////////////////////*/
                                        marendNavegacion(2);
                                        /*//////////////////////////////*/
                                        ////// usa codigo del padre //////
                                        //////////////////////////////////
                                    } catch (e) {}
                                };
                                _generarRemesaClic(
                                    true,
                                    _p59_flujo.cdunieco,
                                    _p59_flujo.cdramo,
                                    _p59_flujo.estado,
                                    _p59_flujo.nmpoliza,
                                    callbackRemesa
                                );
                            } else {
                                _iceMesaControl();
                            }
                        }
                    );
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al confirmar endoso');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}
////// funciones //////
</script>
</head>
<body>
<div id="_p59_divpri" style="height:800px;border:1px solid #CCCCCC"></div>
</body>
</html>