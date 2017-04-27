<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p60_urlRecuperarDatosEndoso                = '<s:url namespace = "/endosos" action = "recuperarDatosEndosoPendiente"       />',
    _p60_urlGuardarFechaEfecto                  = '<s:url namespace = "/endosos" action = "guardarFechaEfectoEndosoPendiente"   />',
    _p60_urlSacaendoso                          = '<s:url namespace = "/endosos" action = "sacaendoso"                          />',
    _p60_urlPantallaExclusiones                 = '<s:url namespace = "/"        action = "pantallaExclusion"                   />',
    _p60_urlTarificar                           = '<s:url namespace = "/endosos" action = "tarificarEndosoCoberturasFlujo"      />',
    _p60_urlConfirmarEndosoCoberturasFlujo      = '<s:url namespace = "/endosos" action = "confirmarEndosoSaludFlujo"           />',
    _p60_urlObtenerComponenteSituacionCobertura = '<s:url namespace = "/endosos" action = "obtenerComponenteSituacionCobertura" />',
    _p60_urlAgregarCobertura                    = '<s:url namespace = "/endosos" action = "agregarCoberturaEndosoCoberturas"    />',
    _p60_urlEliminarCoberturaAgregada           = '<s:url namespace = "/endosos" action = "quitarCoberturaAgregadaEndCob"       />',
    _p60_urlEliminarCobertura                   = '<s:url namespace = "/endosos" action = "eliminarCoberturaEndosoCoberturas"   />',
    _p60_urlEliminarCoberturaEliminada          = '<s:url namespace = "/endosos" action = "restaurarCoberturaEliminadaEndCob"   />';
////// urls //////

////// variables //////
var _p60_flujo  = <s:property value = "%{convertToJSON('flujo')}"  escapeHtml = "false" />,
    _p60_params = <s:property value = "%{convertToJSON('params')}" escapeHtml = "false" />;

debug('_p60_flujo:', _p60_flujo);
debug('_p60_params:', _p60_params);

var _p60_gridAseguradosAfectados,
    _p60_gridBusquedaAsegurados,
    _p60_storeAseguradosAfectados,
    _p60_storeBusquedaAsegurados,
    _p60_storeCoberturasAmparadas,
    _p60_storeCoberturasDisponibles,
    _p60_storeCoberturasAgregadas,
    _p60_storeCoberturasBorradas,
    _p60_gridCoberturasAmparadas,
    _p60_gridCoberturasDisponibles,
    _p60_gridCoberturasAgregadas,
    _p60_gridCoberturasBorradas;

var _p60_recordAseguradoSeleccionado;

var _callbackAseguradoExclusiones; // Variable para el callback de la ventana de exclusiones
////// variables //////

////// overrides //////
////// overrides //////

Ext.onReady(function()
{
    ////// componentes dinamicos //////
    ////// componentes dinamicos //////
    
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    _p60_storeAseguradosAfectados = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'NMSITUAC', 'DSPARENT', 'DSNOMBRE', 'DSSEXO', { type : 'date', name : 'FENACIMI', dateFormat : 'd/m/Y' },
            'GRUPO', 'TITULAR', 'CDTIPSIT'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_ASEGURADOS_AFECTADOS_END_COBERTURAS',
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function () {
            debug('_p60_storeAseguradosAfectados.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.nmsuplem' : _p60_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p60_storeAseguradosAfectados.load().callback() args:', arguments);
                    if (!success) {
                        manejaException(op.error, 'Error al recuperar asegurados afectados');
                    }
                }
            });
        }
    });
    
    _p60_storeBusquedaAsegurados = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'NMSITUAC', 'DSPARENT', 'DSNOMBRE', 'DSSEXO', { type : 'date', name : 'FENACIMI', dateFormat : 'd/m/Y' },
            'GRUPO', 'TITULAR', 'CDTIPSIT'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_BUSQUEDA_ASEGURADOS_END_COBERTURAS',
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function (cadena) {
            debug('_p60_storeBusquedaAsegurados.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.cadena'   : cadena,
                    'params.nmsuplem' : _p60_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p60_storeBusquedaAsegurados.load().callback() args:', arguments);
                    if (!success) {
                        manejaException(op.error, 'Error al recuperar asegurados');
                    }
                }
            });
        }
    });
    
    _p60_storeCoberturasAmparadas = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'CDGARANT', 'DSGARANT', 'SWOBLIGA'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_COBERTURAS_AMPARADAS_CONFIRMADAS',
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function (nmsituac) {
            debug('_p60_storeCoberturasAmparadas.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.nmsituac' : nmsituac,
                    'params.nmsuplem' : _p60_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p60_storeCoberturasAmparadas.load().callback() args:', arguments);
                    if (!success) {
                        manejaException(op.error, 'Error al recuperar coberturas amparadas');
                    }
                }
            });
        }
    });
    
    _p60_storeCoberturasDisponibles = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'CDGARANT', 'DSGARANT'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_COBERTURAS_DISPONIBLES',
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function (nmsituac) {
            debug('_p60_storeCoberturasDisponibles.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.nmsituac' : nmsituac,
                    'params.nmsuplem' : _p60_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p60_storeCoberturasDisponibles.load().callback() args:', arguments);
                    if (!success) {
                        manejaException(op.error, 'Error al recuperar coberturas disponibles');
                    }
                }
            });
        }
    });
    
    _p60_storeCoberturasAgregadas = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'CDGARANT', 'DSGARANT'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_COBERTURAS_AGREGADAS',
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function (nmsituac) {
            debug('_p60_storeCoberturasAgregadas.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.nmsituac' : nmsituac,
                    'params.nmsuplem' : _p60_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p60_storeCoberturasAgregadas.load().callback() args:', arguments);
                    if (!success) {
                        manejaException(op.error, 'Error al recuperar coberturas agregadas');
                    }
                }
            });
        }
    });
    
    _p60_storeCoberturasBorradas = Ext.create('Ext.data.Store', {
        autoLoad: false,
        fields: [
            'CDGARANT', 'DSGARANT'
        ],
        proxy : {
            type        : 'ajax',
            url         : _GLOBAL_URL_RECUPERACION,
            extraParams : {
                'params.consulta' : 'RECUPERAR_COBERTURAS_BORRADAS',
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza
            },
            reader : {
                type            : 'json',
                root            : 'list',
                successProperty : 'success',
                messageProperty : 'message'
            }
        },
        cargar: function (nmsituac) {
            debug('_p60_storeCoberturasBorradas.cargar()');
            var me = this;
            me.load({
                params : {
                    'params.nmsituac' : nmsituac,
                    'params.nmsuplem' : _p60_params.nmsuplem
                },
                callback : function (records, op, success) {
                    debug('_p60_storeCoberturasBorradas.load().callback() args:', arguments);
                    if (!success) {
                        manejaException(op.error, 'Error al recuperar coberturas eliminadas');
                    }
                }
            });
        }
    });
    ////// stores //////
    
    ////// componentes //////
    _p60_gridAseguradosAfectados = Ext.create('Ext.grid.Panel', {
        title   : 'ASEGURADOS AFECTADOS',
        width   : 800,
        height  : 200,
        hidden  : true,
        store   : _p60_storeAseguradosAfectados,
        columns : [
            {
                text      : 'NO.',
                width     : 50,
                dataIndex : 'NMSITUAC'
            }, {
                text      : 'GRUPO',
                width     : 100,
                dataIndex : 'GRUPO'
            }, {
                text      : 'TITULAR FAMILIA',
                width     : 120,
                dataIndex : 'TITULAR'
            }, {
                text      : 'PARENTESCO',
                width     : 120,
                dataIndex : 'DSPARENT'
            }, {
                text      : 'NOMBRE',
                flex      : 1,
                dataIndex : 'DSNOMBRE'
            }, {
                text      : 'SEXO',
                width     : 80,
                dataIndex : 'DSSEXO'
            },{
                xtype       : 'datecolumn',
                text       : 'F. NACIMIENTO',
                dataIndex  : 'FENACIMI',
                width      : 100,
                format     : 'd/m/Y'
            }
        ],
        listeners : {
            selectionChange : function (me, selected) {
                debug('_p60_gridBusquedaAsegurados.selectionChange() args:', arguments);
                if (selected.length > 0) {
                    _p60_recordAseguradoSeleccionado = selected[0];
                    _p60_cargarStoresCoberturas();
                }
            }
        }
    });
    
    _p60_gridBusquedaAsegurados = Ext.create('Ext.grid.Panel', {
        title   : 'BUSCAR ASEGURADOS',
        width   : 800,
        height  : 200,
        hidden  : true,
        store   : _p60_storeBusquedaAsegurados,
        tbar    : [
            {
                xtype      : 'textfield',
                fieldLabel : '<span style="color:white;">BUSCAR POR NOMBRE:</span>',
                labelWidth : 150,
                allowBlank : false,
                minLength  : 3,
                itemId     : '_p60_buscarField'
            }, {
                xtype : 'button',
                text  : 'BUSCAR',
                icon  : '${icons}zoom.png',
                handler : function (me) {
                    if (!_fieldById('_p60_buscarField').isValid()) {
                        mensajeWarning('Favor de revisar el criterio de b\u00fasqueda');
                    } else {
                        _p60_storeBusquedaAsegurados.cargar(_fieldById('_p60_buscarField').getValue());
                    }
                }
            }, '->', {
                xtype : 'button',
                text  : 'TRAER TODOS',
                icon  : '${icons}group.png',
                handler : function (me) {
                    Ext.MessageBox.confirm(
                        'Confirmar',
                        'Consultar todos los asegurados puede resultar lento en p\u00f3lizas colectivas ¿Desea continuar?',
                        function (btn) {
                            if (btn === 'yes') {
                                _p60_storeBusquedaAsegurados.cargar('');
                            }
                        }
                    );
                }
            }
        ],
        columns : [
            {
                text      : 'NO.',
                width     : 50,
                dataIndex : 'NMSITUAC'
            }, {
                text      : 'GRUPO',
                width     : 100,
                dataIndex : 'GRUPO'
            }, {
                text      : 'TITULAR FAMILIA',
                width     : 120,
                dataIndex : 'TITULAR'
            }, {
                text      : 'PARENTESCO',
                width     : 120,
                dataIndex : 'DSPARENT'
            }, {
                text      : 'NOMBRE',
                flex      : 1,
                dataIndex : 'DSNOMBRE'
            }, {
                text      : 'SEXO',
                width     : 80,
                dataIndex : 'DSSEXO'
            },{
                xtype       : 'datecolumn',
                text       : 'F. NACIMIENTO',
                dataIndex  : 'FENACIMI',
                width      : 100,
                format     : 'd/m/Y'
            }
        ],
        listeners : {
            selectionChange : function (me, selected) {
                debug('_p60_gridBusquedaAsegurados.selectionChange() args:', arguments);
                if (selected.length > 0) {
                    _p60_recordAseguradoSeleccionado = selected[0];
                    _p60_cargarStoresCoberturas();
                }
            }
        }
    });
    
    _p60_gridCoberturasAmparadas = Ext.create('Ext.grid.Panel', {
        title       : 'COBERTURAS AMPARADAS',
        width       : 400,
        height      : 200,
        hideHeaders : true,
        store       : _p60_storeCoberturasAmparadas,
        columns     : [
            {
                xtype : 'rownumberer'
            }, {
                text      : 'DSGARANT',
                dataIndex : 'DSGARANT',
                flex      : 1
            }, {
                dataIndex : 'SWOBLIGA',
                width     : 30,
                hidden    : Number(_p60_params.cdtipsup) !== 7,
                renderer  : function (v, md, record, row) {
                    var r = '';
                    if (record.get('SWOBLIGA') !== 'S') {
                        r = '<a href = "#"><img src = "${icons}delete.png" data-qtip = "Eliminar" onclick = "_p60_eliminarClic('
                            + row + '); return false;"/></a>';
                    }
                    return r;
                }
            }
        ]
    });
    
    _p60_gridCoberturasDisponibles = Ext.create('Ext.grid.Panel', {
        title       : 'COBERTURAS DISPONIBLES',
        width       : 400,
        height      : 200,
        hideHeaders : true,
        store       : _p60_storeCoberturasDisponibles,
        columns     : [
            {
                xtype : 'rownumberer'
            }, {
                text      : 'DSGARANT',
                dataIndex : 'DSGARANT',
                flex      : 1
            }, {
                xtype   : 'actioncolumn',
                icon    : '${icons}add.png',
                tooltip : 'AGREGAR',
                width   : 30,
                hidden  : Number(_p60_params.cdtipsup) !== 6,
                handler : _p60_agregarCoberturaClic
            }
        ]
    });
    
    _p60_gridCoberturasAgregadas = Ext.create('Ext.grid.Panel', {
        title       : 'COBERTURAS AGREGADAS',
        width       : 400,
        height      : 200,
        hideHeaders : true,
        store       : _p60_storeCoberturasAgregadas,
        columns     : [
            {
                xtype : 'rownumberer'
            }, {
                text      : 'DSGARANT',
                dataIndex : 'DSGARANT',
                flex      : 1
            }, {
                xtype   : 'actioncolumn',
                icon    : '${icons}cancel.png',
                tooltip : 'DESHACER',
                hidden  : Number(_p60_params.cdtipsup) !== 6,
                width   : 30,
                handler : _p60_quitarCoberturaAgregadaClic
            }
        ]
    });
    
    _p60_gridCoberturasBorradas = Ext.create('Ext.grid.Panel', {
        title       : 'COBERTURAS ELIMINADAS',
        width       : 400,
        height      : 200,
        hideHeaders : true,
        store       : _p60_storeCoberturasBorradas,
        columns     : [
            {
                xtype : 'rownumberer'
            }, {
                text      : 'DSGARANT',
                dataIndex : 'DSGARANT',
                flex      : 1
            }, {
                xtype   : 'actioncolumn',
                icon    : '${icons}cancel.png',
                tooltip : 'DESHACER',
                width   : 30,
                hidden  : Number(_p60_params.cdtipsup) !== 7,
                handler : _p60_quitarCoberturaEliminadaClic
            }
        ]
    });
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p60_panelpri'
        ,renderTo : '_p60_divpri'
        ,title    : 'ENDOSO DE ALTA/BAJA DE COBERTURAS'
        ,border   : 0
        ,defaults : { style : 'margin : 5px;' }
        ,items    :
        [
            Ext.create('Ext.panel.Panel', {
                itemId      : '_p60_panelFlujo',
                title       : 'ACCIONES',
                hidden      : Ext.isEmpty(_p60_flujo),
                buttonAlign : 'left',
                buttons     : [],
                listeners   : {
                    afterrender : function (me) {
                        if (!Ext.isEmpty(_p60_flujo)) {
                            _cargarBotonesEntidad(
                                _p60_flujo.cdtipflu,
                                _p60_flujo.cdflujomc,
                                _p60_flujo.tipoent,
                                _p60_flujo.claveent,
                                _p60_flujo.webid,
                                me.itemId, // callback
                                _p60_flujo.ntramite,
                                _p60_flujo.status,
                                _p60_flujo.cdunieco,
                                _p60_flujo.cdramo,
                                _p60_flujo.estado,
                                _p60_flujo.nmpoliza,
                                _p60_flujo.nmsituac,
                                _p60_flujo.nmsuplem,
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
                        ,minValue   : _p60_params.FEEFECTO
                        ,maxValue   : _p60_params.FEPROREN
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Confirmar fecha'
                        ,itemId  : '_p60_botonConfirmarFecha'
                        ,icon    : '${icons}accept.png'
                        ,handler : _p60_confirmarFechaClic
                    }
                ]
            },
            _p60_gridAseguradosAfectados,
            _p60_gridBusquedaAsegurados,
            {
                xtype  : 'panel',
                border : 0,
                itemId : '_p60_panelGridsCoberturas',
                hidden : true,
                layout : {
                    type    : 'table',
                    columns : 2
                },
                defaults : { style : 'margin : 5px;' },
                items : [
                    _p60_gridCoberturasAmparadas,
                    _p60_gridCoberturasDisponibles,
                    _p60_gridCoberturasAgregadas,
                    _p60_gridCoberturasBorradas
                ]
            }
        ]
        ,buttonAlign : 'center'
        ,buttons     : [
            {
                text    : 'Validar datos y tarificar',
                icon    : '${icons}key.png',
                handler : _p60_tarificarClic
            }, {
                text    : 'Borrar cambios',
                icon    : '${icons}delete.png',
                handler : _p60_borrarClic
            }
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    _p60_recuperarDatosEndoso();
    ////// loaders //////
});

////// funciones //////
function _p60_confirmarFechaClic(me)
{
    debug('_p60_confirmarFechaClic() args:',arguments);
    
    if(!_fieldByName('FEEFECTO').isValid())
    {
        return datosIncompletos();
    }
    
    var mask, ck = 'Guardando fecha de efecto';
    try {
        var params = {
            'params.fecha'    : _fieldByName('FEEFECTO').getSubmitValue(),
            'params.cdtipsup' : _p60_params.cdtipsup
        };
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p60_urlGuardarFechaEfecto,
            params  : _flujoToParams(_p60_flujo, params),
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al guardar fecha de efecto';
                try {
                    var datosEndoso = Ext.decode(response.responseText);
                    debug('AJAX datosEndoso:', datosEndoso);
                    if (datosEndoso.success === true) {
                        _p60_params.fesolici = datosEndoso.params.pv_fesolici_o;
                        _p60_params.nsuplogi = datosEndoso.params.pv_nsuplogi_o;
                        _p60_params.nmsuplem = datosEndoso.params.pv_nmsuplem_o;
                        debug('_p60_params:', _p60_params);
                        _p60_bloquearFecha(true);
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

function _p60_bloquearFecha(bloqueo)
{
    debug('_p60_bloquearFecha() args:', arguments);
    if (bloqueo === true) {
        _fieldByName('FEEFECTO').setReadOnly(true);
        _fieldById('_p60_botonConfirmarFecha').hide();
        _p60_gridAseguradosAfectados.show();
        _p60_gridBusquedaAsegurados.show();
        _fieldById('_p60_panelGridsCoberturas').show();
        _p60_storeAseguradosAfectados.cargar();
        _p60_storeBusquedaAsegurados.removeAll();
        _p60_storeCoberturasAmparadas.removeAll();
        _p60_storeCoberturasDisponibles.removeAll();
        _p60_storeCoberturasAgregadas.removeAll();
        _p60_storeCoberturasBorradas.removeAll();
    } else {
        _fieldByName('FEEFECTO').setReadOnly(false);
        _fieldById('_p60_botonConfirmarFecha').show();
        _p60_gridAseguradosAfectados.hide();
        _p60_gridBusquedaAsegurados.hide();
        _fieldById('_p60_panelGridsCoberturas').hide();
    }
}

function _p60_borrarClic(me)
{
    debug('_p60_borrarClic() args:',arguments);
    Ext.MessageBox.confirm(
        'Confirmar',
        '¿Est&aacute; seguro que desea borrar todos los cambios?',
        function (btn) {
            if (btn === 'yes') {
                _p60_sacaendoso();
            }
        }
    );
}

function _p60_sacaendoso (callback) {
    debug('_p60_sacaendoso()');
    var mask, ck = 'Borrando cambios';
    try {
        if (Ext.isEmpty(_p60_params.nmsuplem)) {
            throw 'No hay cambios';
        }
        mask = _maskLocal('Borrando cambios');
        Ext.Ajax.request({
            url     : _p60_urlSacaendoso,
            params  : {
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza,
                'params.nmsuplem' : _p60_params.nmsuplem,
                'params.nsuplogi' : _p60_params.nsuplogi
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al borrar cambios';
                try {
                    var jsonSacaendoso = Ext.decode(response.responseText);
                    debug('AJAX jsonSacaendoso:', jsonSacaendoso);
                    if (jsonSacaendoso.success === true) {
                        _p60_params.nsuplogi = '';
                        _p60_params.nmsuplem = '';
                        _p60_params.fesolici = '';
                        _fieldByName('FEEFECTO').setValue('');
                        _p60_bloquearFecha(false);
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

function _p60_tarificarClic(me)
{
    debug('_p60_tarificarClic() args:',arguments);
    var mask, ck;
    try {
        if (Ext.isEmpty(_p60_params.nmsuplem)) {
            return mensajeWarning('Favor de confirmar la fecha de efecto');
        }
        ck = 'Tarificando cambios en las coberturas';
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p60_urlTarificar,
            params  : {
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza,
                'params.nmsuplem' : _p60_params.nmsuplem,
                'params.feinival' : _fieldByName('FEEFECTO').getSubmitValue(),
                'params.cdtipsup' : _p60_params.cdtipsup
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al tarificar cambios en coberturas';
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
                                hidden  : _p60_params.permisoEmitir !== 'S',
                                handler : _p60_confirmarClic
                            }, {
                                text    : 'Autorizar',
                                icon    : '${icons}key.png',
                                hidden  : _p60_params.permisoAutorizar !== 'S',
                                handler : _p60_autorizarClic
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

function _p60_recuperarDatosEndoso () {
    debug('_p60_recuperarDatosEndoso()');
    var mask, ck = 'Recuperando datos de endoso';
    try {
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p60_urlRecuperarDatosEndoso,
            params  : _flujoToParams(_p60_flujo, { 'params.cdtipsup' : _p60_params.cdtipsup }),
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al recuperar datos de endoso';
                try {
                    var datosEndoso = Ext.decode(response.responseText);
                    debug('AJAX datosEndoso:', datosEndoso);
                    if (datosEndoso.success === true) {
                        if (!Ext.isEmpty(datosEndoso.params.FEINIVAL)) {
                            _p60_params.nmsuplem = datosEndoso.params.NMSUPLEM;
                            _p60_params.nsuplogi = datosEndoso.params.NSUPLOGI;
                            _p60_params.fesolici = datosEndoso.params.FESOLICI;
                            debug('_p60_params:', _p60_params);
                            _fieldByName('FEEFECTO').setValue(datosEndoso.params.FEINIVAL);
                            _p60_bloquearFecha(true);
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

function _p60_confirmarClic (me) {
    debug('_p60_confirmarClic() args:', arguments);
    _p60_confirmar(me, false);
}

function _p60_autorizarClic (me) {
    debug('_p60_confirmarClic() args:', arguments);
    _p60_confirmar(me, true);
}

function _p60_confirmar (button, autorizar) {
    debug('_p60_confirmar() args:', arguments);
    var mask, ck;
    try {
        ck = 'Recopilando par\u00e1metros';
        var params = _flujoToParams(_p60_flujo);
        params['params.nmsuplem'] = _p60_params.nmsuplem;
        params['params.nsuplogi'] = _p60_params.nsuplogi;
        params['params.fesolici'] = _p60_params.fesolici;
        params['params.feinival'] = _fieldByName('FEEFECTO').getSubmitValue();
        params['params.autoriza'] = autorizar === true ? 'S' : 'N';
        params['params.cdtipsup'] = _p60_params.cdtipsup;
        debug('params:', params);
        ck = 'Confirmando endoso';
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p60_urlConfirmarEndosoCoberturasFlujo,
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
                                    _p60_flujo.cdunieco,
                                    _p60_flujo.cdramo,
                                    _p60_flujo.estado,
                                    _p60_flujo.nmpoliza,
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

function _p60_cargarStoresCoberturas () {
    debug('_p60_cargarStoresCoberturas()');
    if (Ext.isEmpty(_p60_recordAseguradoSeleccionado)) {
        return mensajeError('Seleccione un asegurado');
    }
    var nmsituac = _p60_recordAseguradoSeleccionado.get('NMSITUAC');
    _p60_storeCoberturasAmparadas.cargar(nmsituac);
    _p60_storeCoberturasDisponibles.cargar(nmsituac);
    _p60_storeCoberturasAgregadas.cargar(nmsituac);
    _p60_storeCoberturasBorradas.cargar(nmsituac);
}

function _p60_recuperarComponentesSituacionCobertura (record, callback) {
    debug('_p60_recuperarComponentesSituacionCobertura() args:', arguments);
    var mask, ck = 'Recuperando componentes de situaci\u00f3n';
    try {
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url    : _p60_urlObtenerComponenteSituacionCobertura,
            params : {
                'smap1.cdramo'   : _p60_flujo.cdramo,
                'smap1.cdtipsit' : _p60_recordAseguradoSeleccionado.get('CDTIPSIT'),
                'smap1.cdgarant' : record.get('CDGARANT'),
                'smap1.cdtipsup' : _p60_params.cdtipsup
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al recuperar componentes de situaci\u00f3n';
                try {
                    var jsonCompsSit = Ext.decode(response.responseText);
                    debug('AJAX jsonCompsSit:', jsonCompsSit);
                    if (jsonCompsSit.exito !== true) {
                        throw jsonCompsSit.respuesta;
                    }
                    ck = 'Enviando componentes de situaci\u00f3n';
                    callback(record, jsonCompsSit);
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al recuperar componentes de situaci\u00f3n');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _p60_agregarCoberturaClic (me, row, col, item, e, record) {
    debug('_p60_agregarCoberturaClic() args:', arguments);
    var ck;
    try {
        ck = 'Declarando receptor de componentes de situaci\u00f3n';
	    var callback = function (record, jsonCompsSit) {
	        debug('_p60_agregarCoberturaClic callback() args:', arguments);
	        var ck;
	        try {
	            ck = 'Declarando petici\u00f3n de inserci\u00f3n de cobertura';
		        var peticionGuardarCobertura = function (cdatribu1, otvalor1, cdatribu2, otvalor2, cdatribu3, otvalor3) {
		            debug('peticionGuardarCobertura() args:', arguments);
		            var mask, ck = 'Agregando cobertura';
		            try {
		                mask = _maskLocal(ck);
		                Ext.Ajax.request({
		                    url     : _p60_urlAgregarCobertura,
		                    params  : {
		                        'params.cdunieco'  : _p60_flujo.cdunieco,
		                        'params.cdramo'    : _p60_flujo.cdramo,
		                        'params.estado'    : _p60_flujo.estado,
		                        'params.nmpoliza'  : _p60_flujo.nmpoliza,
		                        'params.nmsituac'  : _p60_recordAseguradoSeleccionado.get('NMSITUAC'),
		                        'params.nmsuplem'  : _p60_params.nmsuplem,
		                        'params.cdgarant'  : record.get('CDGARANT'),
		                        'params.cdatribu1' : cdatribu1,
		                        'params.otvalor1'  : otvalor1,
		                        'params.cdatribu2' : cdatribu2,
		                        'params.otvalor2'  : otvalor2,
		                        'params.cdatribu3' : cdatribu3,
		                        'params.otvalor3'  : otvalor3,
		                        'params.cdtipsit'  : _p60_recordAseguradoSeleccionado.get('CDTIPSIT')
		                    },
		                    success : function (response) {
		                        mask.close();
		                        var ck = 'Decodificando respuesta al agregar cobertura';
		                        try {
		                            var jsonAgregarCober = Ext.decode(response.responseText);
		                            if (jsonAgregarCober.success !== true) {
		                                throw jsonAgregarCober.message;
		                            }
		                            _p60_cargarStoresCoberturas();
		                            _p60_storeAseguradosAfectados.cargar();
		                        } catch (e) {
		                            manejaException(e, ck);
		                        }
		                    },
		                    failure : function () {
		                        mask.close();
		                        errorComunicacion(null, 'Error al agregar la cobertura');
		                    }
		                });
		            } catch (e) {
		                manejaException(e, ck, mask);
		            }
		        };
		        ck = 'Procesando componentes de situaci\u00f3n';
		        if (jsonCompsSit.smap1.CONITEM === 'true') {
		            ck = 'Construyendo ventana de componentes de situaci\u00f3n';
		            centrarVentanaInterna(Ext.create('Ext.window.Window', {
		                title       : 'VALOR DE COBERTURA',
		                modal       : true,
		                width       : 300,
		                minHeight   : 150,
		                closeAction : 'destroy',
		                border      : 0,
		                items       : Ext.decode('['+jsonCompsSit.smap1.item+']'),
		                buttonAlign : 'center',
		                buttons     : [
		                    {
		                        text    : 'Aceptar',
		                        icon    : '${icons}accept.png',
		                        handler : function (me) {
		                            var item1 = me.up().up().items.items[0];
		                            var item2 = me.up().up().items.items[1];
		                            var item3 = me.up().up().items.items[2];
		                            if (!Ext.isEmpty(item1) && Ext.isEmpty(item1.getValue())) {
		                                throw 'Favor de verificar los datos';
		                            }
		                            if (!Ext.isEmpty(item2) && Ext.isEmpty(item2.getValue())) {
		                                throw 'Favor de verificar los datos';
		                            }
		                            if (!Ext.isEmpty(item2) && Ext.isEmpty(item2.getValue())) {
		                                throw 'Favor de verificar los datos';
		                            }
		                            peticionGuardarCobertura(
		                                Ext.isEmpty(item1) ? null : item1.cdatribu,
		                                Ext.isEmpty(item1) ? null : item1.getValue(),
		                                Ext.isEmpty(item2) ? null : item2.cdatribu,
		                                Ext.isEmpty(item2) ? null : item2.getValue(),
		                                Ext.isEmpty(item3) ? null : item3.cdatribu,
		                                Ext.isEmpty(item3) ? null : item3.getValue()
		                            );
		                            me.up('window').close();
		                        }
		                    }
		                ]
		            }).show());
		        } else {
		            peticionGuardarCobertura();
		        }
		    } catch (e) {
		        manejaException(e, ck);
		    }
	    };
	    ck = 'Invocando recuperaci\u00f3n de componentes de situaci\u00f3n';
	    _p60_recuperarComponentesSituacionCobertura(record, callback);
	} catch (e) {
	    manejaException(e, ck);
	}
}

function _p60_quitarCoberturaAgregadaClic (me, row, col, item, e, record) {
    debug('_p60_quitarCoberturaAgregadaClic() args:', arguments);
    var ck;
    try {
        ck = 'Declarando receptor de componentes de situaci\u00f3n';
        var callback = function (record, jsonCompsSit) {
            debug('_p60_quitarCoberturaAgregadaClic callback() args:', arguments);
            var mask, ck;
            try {
                var cdatribu1, cdatribu2, cdatribu3;
                if (jsonCompsSit.smap1.CONITEM === 'true') {
                    ck = 'Construyendo componentes de situaci\u00f3n';
                    var items = Ext.decode('['+jsonCompsSit.smap1.item+']');
                    try {
                        cdatribu1 = items[0].cdatribu;
                    } catch (e) {}
                    try {
                        cdatribu2 = items[1].cdatribu;
                    } catch (e) {}
                    try {
                        cdatribu3 = items[2].cdatribu;
                    } catch (e) {}
                }
                ck = 'Eliminando cobertura';
		        mask = _maskLocal(ck);
		        Ext.Ajax.request({
		            url     : _p60_urlEliminarCoberturaAgregada,
		            params  : {
		                'params.cdunieco'  : _p60_flujo.cdunieco,
		                'params.cdramo'    : _p60_flujo.cdramo,
		                'params.estado'    : _p60_flujo.estado,
		                'params.nmpoliza'  : _p60_flujo.nmpoliza,
		                'params.nmsituac'  : _p60_recordAseguradoSeleccionado.get('NMSITUAC'),
		                'params.nmsuplem'  : _p60_params.nmsuplem,
		                'params.cdgarant'  : record.get('CDGARANT'),
		                'params.cdtipsit'  : _p60_recordAseguradoSeleccionado.get('CDTIPSIT'),
		                'params.cdatribu1' : cdatribu1,
                        'params.cdatribu2' : cdatribu2,
                        'params.cdatribu3' : cdatribu3
		            },
		            success : function (response) {
		                mask.close();
		                var ck = 'Decodificando respuesta al eliminar cobertura agregada';
		                try {
		                    var jsonQuitarAgregada = Ext.decode(response.responseText);
		                    debug('AJAX jsonQuitarAgregada:', jsonQuitarAgregada);
		                    if (jsonQuitarAgregada.success !== true) {
		                        throw jsonQuitarAgregada.message;
		                    }
		                    _p60_cargarStoresCoberturas();
		                    _p60_storeAseguradosAfectados.cargar();
		                } catch (e) {
		                    manejaException(e, ck);
		                }
		            },
		            failure : function () {
		                mask.close();
		                errorComunicacion(null, 'Error al eliminar cobertura agregada');
		            }
		        });
		    } catch (e) {
		        manejaException(e, ck, mask);
		    }
		};
		ck = 'Invocando recuperaci\u00f3n de componentes de situaci\u00f3n';
		_p60_recuperarComponentesSituacionCobertura(record, callback);
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p60_eliminarClic (row) {
    debug('_p60_eliminarClic() args:', arguments);
    var mask, ck = 'Eliminando cobertura';
    try {
        var record = _p60_storeCoberturasAmparadas.getAt(row);
        debug('record:', record);
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url     : _p60_urlEliminarCobertura,
            params  : {
                'params.cdunieco' : _p60_flujo.cdunieco,
                'params.cdramo'   : _p60_flujo.cdramo,
                'params.estado'   : _p60_flujo.estado,
                'params.nmpoliza' : _p60_flujo.nmpoliza,
                'params.nmsituac' : _p60_recordAseguradoSeleccionado.get('NMSITUAC'),
                'params.cdgarant' : record.get('CDGARANT'),
                'params.nmsuplem' : _p60_params.nmsuplem,
                'params.cdtipsit' : _p60_recordAseguradoSeleccionado.get('CDTIPSIT')
            },
            success : function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al eliminar cobertura';
                try {
                    var jsonEliminar = Ext.decode(response.responseText);
                    debug('AJAX jsonEliminar:', jsonEliminar);
                    if (jsonEliminar.success !== true) {
                        throw jsonEliminar.message;
                    }
                    _p60_cargarStoresCoberturas();
                    _p60_storeAseguradosAfectados.cargar();
                } catch (e) {
                    manejaException(e, ck);
                }
            },
            failure : function () {
                mask.close();
                errorComunicacion(null, 'Error al eliminar cobertura');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function _p60_quitarCoberturaEliminadaClic (me, row, col, item, e, record) {
    debug('_p60_quitarCoberturaEliminadaClic() args:', arguments);
    var ck;
    try {
        ck = 'Declarando receptor de componentes de situaci\u00f3n';
        var callback = function (record, jsonCompsSit) {
            debug('_p60_quitarCoberturaAgregadaClic callback() args:', arguments);
		    var mask, ck;
		    try {
		        var cdatribu1, cdatribu2, cdatribu3;
                if (jsonCompsSit.smap1.CONITEM === 'true') {
                    ck = 'Construyendo componentes de situaci\u00f3n';
                    var items = Ext.decode('['+jsonCompsSit.smap1.item+']');
                    try {
                        cdatribu1 = items[0].cdatribu;
                    } catch (e) {}
                    try {
                        cdatribu2 = items[1].cdatribu;
                    } catch (e) {}
                    try {
                        cdatribu3 = items[2].cdatribu;
                    } catch (e) {}
                }
                ck = 'Restaurando cobertura';
		        mask = _maskLocal(ck);
		        Ext.Ajax.request({
		            url     : _p60_urlEliminarCoberturaEliminada,
		            params  : {
		                'params.cdunieco'  : _p60_flujo.cdunieco,
		                'params.cdramo'    : _p60_flujo.cdramo,
		                'params.estado'    : _p60_flujo.estado,
		                'params.nmpoliza'  : _p60_flujo.nmpoliza,
		                'params.nmsituac'  : _p60_recordAseguradoSeleccionado.get('NMSITUAC'),
		                'params.nmsuplem'  : _p60_params.nmsuplem,
		                'params.cdgarant'  : record.get('CDGARANT'),
		                'params.cdtipsit'  : _p60_recordAseguradoSeleccionado.get('CDTIPSIT'),
		                'params.cdatribu1' : cdatribu1,
                        'params.cdatribu2' : cdatribu2,
                        'params.cdatribu3' : cdatribu3
		            },
		            success : function (response) {
		                mask.close();
		                var ck = 'Decodificando respuesta al restaurar cobertura eliminada';
		                try {
		                    var jsonRestaurarQuitada = Ext.decode(response.responseText);
		                    debug('AJAX jsonRestaurarQuitada:', jsonRestaurarQuitada);
		                    if (jsonRestaurarQuitada.success !== true) {
		                        throw jsonRestaurarQuitada.message;
		                    }
		                    _p60_cargarStoresCoberturas();
		                    _p60_storeAseguradosAfectados.cargar();
		                } catch (e) {
		                    manejaException(e, ck);
		                }
		            },
		            failure : function () {
		                mask.close();
		                errorComunicacion(null, 'Error al restaurar cobertura eliminada');
		            }
		        });
		    } catch (e) {
		        manejaException(e, ck, mask);
		    }
        };
        ck = 'Invocando recuperaci\u00f3n de componentes de situaci\u00f3n';
        _p60_recuperarComponentesSituacionCobertura(record, callback);
    } catch (e) {
        manejaException(e, ck);
    }
}
////// funciones //////
</script>
</head>
<body>
<div id="_p60_divpri" style="height:1200px;border:1px solid #CCCCCC"></div>
</body>
</html>