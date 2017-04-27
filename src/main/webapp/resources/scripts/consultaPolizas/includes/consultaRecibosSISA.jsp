<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.pagado-row .x-grid-cell { 
    background-color: #80FF00; 
    color: #900; 
}
 
.generado-no-row .x-grid-cell { 
    background-color: #81DAF5; 
    color: #900; 
}

.generado-row .x-grid-cell { 
    background-color: #80FF00; 
    color: #900; 
}

.cancelado-row .x-grid-cell { 
    background-color: #FA8258; 
    color: #900; 
}

.blanco-row .x-grid-cell { 
    background-color: #FFFFFF; 
    color: #900; 
}

.rosa-row .x-grid-cell { 
    background-color: #F5A9A9; 
    color: #900; 
}

</style>
<script>
//////urls //////
//////urls //////

//////variables //////
    var _p25_storeRecibos;
    var _URL_CONSULTA_RECIBOS        = '<s:url namespace="/general" action= "obtenerDatosRecibosSISA"      />';
    var _URL_CONSOLIDA_RECIBOS       = '<s:url namespace="/general" action= "consolidarRecibos"            />';
    var _URL_DESCONSOLIDA_RECIBOS    = '<s:url namespace="/general" action= "desconsolidarRecibos"         />';
    var _URL_CONSULTA_DETALLE_RECIBO = '<s:url namespace="/general" action= "obtieneDetalleReciboSISA"     />';
    var _URL_OBTENCION_REPORTE       = '<s:url namespace="/general" action= "procesoObtencionReporte"      />';
    var _URL_REPORTE_RECIBOS         = '<s:url namespace="/general" action= "procesoReporteRecibos"        />';
    var _URL_CONSULTA_BITACONS       = '<s:url namespace="/general" action= "obtenerBitacoraConsolidacion" />';
    var _URL_OBTIENE_LIGA_RECIBO     = '<s:url namespace="/general" action= "obtenerLigaRecibo"            />';
    //var _URL_CONSULTA_DETALLE_RECIBO = '<s:url namespace="/general" action="obtieneDetalleRecibo"     />';
    //var _URL_CONSULTA_DETALLE_RECIBO = '<s:url namespace="/general" action= "obtieneDetalleRecibo"    />';
    var p_cdunieco                   = '<s:property                 value = "params.cdunieco"         />';
    var p_cdramo                     = '<s:property                 value = "params.cdramo"           />';
    var p_estado                     = '<s:property                 value = "params.estado"           />';
    var p_nmpoliza                   = '<s:property                 value = "params.nmpoliza"         />';
    var p_nmsuplem                   = '<s:property                 value = "params.nmsuplem"         />';
    var pRcb_wndDetalleRecibo;
    var winSimbologia;
    var arrRolesConso                = [RolSistema.SuscriptorTecnico, 
                                        RolSistema.SuscriptorSalud,
                                        RolSistema.SupervicionEmisionSalud,
                                        RolSistema.GerenteOperacionesEmision,
                                        RolSistema.SubdirectorSalud];
//////variables //////

//////overrides //////
//////overrides //////

//////componentes dinamicos //////
    var itemsReciboFields        = [<s:property value="imap.itemsReciboFields"    escapeHtml="false" />];
    var itemsReciboColumns       = [<s:property value="imap.itemsReciboColumns"   escapeHtml="false" />];
    var itemsDetalleFields       = [<s:property value="imap.itemsDetalleFields"   escapeHtml="false" />];
    var itemsDetalleColumns      = [<s:property value="imap.itemsDetalleColumns"  escapeHtml="false" />];
    var itemsBitacoraFields      = [<s:property value="imap.itemsBitacoraFields"  escapeHtml="false" />];
    var itemsBitacoraColumns     = [<s:property value="imap.itemsBitacoraColumns" escapeHtml="false" />];

//////componentes dinamicos //////

Ext.onReady(function(){
    ////// modelos //////
    Ext.define('_p25_modeloRecibo',{
        extend     : 'Ext.data.Model',
        fields     : itemsReciboFields,
        idProperty : 'consecutivo'
    });
    
    Ext.define('_p25_modeloDetalleRecibo',{
        extend  : 'Ext.data.Model',
        fields  : itemsDetalleFields
    });
    
    Ext.define('_p25_modeloBitacoraRecibo',{
        extend  : 'Ext.data.Model',
        fields  : itemsBitacoraFields
    });
    ////// modelos //////
    
    ////// stores //////
    _p25_storeRecibos = Ext.create('Ext.data.Store',{
        model    : '_p25_modeloRecibo',
        autoLoad : false,
        proxy    :
        {
            type   : 'ajax',
            url    : _URL_CONSULTA_RECIBOS,
            reader :
            {
                type            : 'json',
                root            : 'loadList',
                messageProperty : 'respuesta',
                successProperty : 'success'
            }
        }
    });
    
    _p25_storeDetalleRecibo = Ext.create('Ext.data.Store',{
        model    : '_p25_modeloDetalleRecibo',
        proxy    : {
            type   : 'ajax',
            url    : _URL_CONSULTA_DETALLE_RECIBO,
            reader: {
                type: 'json',
                root: 'detallesRecibo'
            }
        }
    });
    
    _p25_storeHistCons = Ext.create('Ext.data.Store',{
        model    : '_p25_modeloBitacoraRecibo',
        autoLoad : false,
        proxy    : {
            type   : 'ajax',
            url    : _URL_CONSULTA_BITACONS,
            reader : {
                type            : 'json',
                root            : 'slist1',
                messageProperty : 'respuesta',
                successProperty : 'success'
            }
        }
    });
    
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',{
        //title    : 'Panel principal',
        panelPri : 'S',
        renderTo : 'dvRecibos',
        defaults : {
            style : 'margin:5px;'
        },
        items    : [
            Ext.create('Ext.panel.Panel',{
                title      : 'Resultados',
                items      : [
                    Ext.create('Ext.grid.Panel',{
                        itemId     : 'gridRecibos',
                        height     : 300,
                        width      : 800,
                        store      : _p25_storeRecibos,
                        columns    : itemsReciboColumns,
                        viewConfig : { 
                            stripeRows: false,
                            getRowClass: function(record) {
                                var style;
                                debug('EstadoRecibo.Pendiente',EstadoRecibo.Pendiente);
                                debug('Coloreando ',record.raw);
                                if (EstadoRecibo.Pendiente === record.data['status']) {
                                    var feactual = new Date();                                  
                                    var feinicio = record.data['feinicio'];
                                    if(feactual > feinicio){
                                        style = 'generado-no-row';
                                    }
                                    else{
                                        style = 'blanco-row';                                        
                                    }
                                } 
                                else if (EstadoRecibo.Cancelado === record.data['status']) {
                                    style = 'cancelado-row';
                                }
                                else if (EstadoRecibo.Pagado === record.data['status']) {
                                    if (record.raw['swdespago'] === 'S') {
                                        style = 'rosa-row';    
                                    }
                                    else{
                                        style = 'pagado-row';
                                    }
                                }
                                else if (EstadoRecibo.Devuelto === record.data['status']) {
                                    if (record.raw['swdespago'] === 'S') {
                                        style = 'rosa-row';    
                                    }
                                    else{
                                        style = 'generado-row';
                                    }                                    
                                }
                                return style;
                            }
                        },
                        selModel : Ext.create('Ext.selection.CheckboxModel', {
                            mode                : 'MULTI',
                            showHeaderCheckbox  : true
                        }),
                        tbar    : [
                            {
                                xtype    : 'button',
                                itemId   : 'btnDetalle',
                                text     : 'Ver detalle',
                                disabled : true,
                                handler  : function(me){
                                    debug(me.up('grid').getSelectionModel().getSelection());
                                    var gridRecibos = _fieldById('gridRecibos');
                                    var datos = gridRecibos.getSelectionModel().getSelection()[0].data;
                                    _p25_storeDetalleRecibo.load({
                                        params: {
                                            'params.cdunieco': p_cdunieco,
                                            'params.cdramo'  : p_cdramo,
                                            'params.estado'  : p_estado,
                                            'params.nmpoliza': p_nmpoliza,
                                            'params.nmrecibo': datos['nmrecibo'],
                                            'params.nmfolcon': datos['folio']
                                        },
                                        callback: function(records, operation, success){
                                            if(success){
                                                pRcb_wndDetalleRecibo.show();
                                            }
                                            else{
                                                showMessage('Error', 'Error al obtener los datos, intente m\u00E1s tarde', Ext.Msg.OK, Ext.Msg.ERROR);
                                            }                    
                                        }
                                    });
                                }
                            },
                            {
                                xtype    : 'button', 
                                itemId   : 'btnConsolidar',
                                text     : 'Consolidar',
                                disabled : true,
                                hidden   : arrRolesConso.indexOf(_GLOBAL_CDSISROL) === -1 ? true : false,
                                handler  : function(){
                                    var gridRecibos   = _fieldById('gridRecibos');                                    
                                    consolidarRecibos(obtenerDataSelected(gridRecibos));                                    
                                }
                            },
                            {
                                xtype    : 'button', 
                                itemId   : 'btnDesconsolidar',
                                text     : 'Desconsolidar',
                                disabled : true,
                                hidden   : arrRolesConso.indexOf(_GLOBAL_CDSISROL) === -1 ? true : false,
                                handler  : function(){
                                    var gridRecibos   = _fieldById('gridRecibos');
                                    desconsolidarRecibos(obtenerDataSelected(gridRecibos));
                                }
                            },
                            {
                                xtype    : 'button', 
                                itemId   : 'btnVerRecibo',
                                text     : 'Ver recibo',
                                disabled : true,
                                hidden   : arrRolesConso.indexOf(_GLOBAL_CDSISROL) === -1 ? true : false,
                                handler  : function(){                           
                                    var seleccionados = obtenerDataSelected(_fieldById('gridRecibos'));
                                    var folio = seleccionados[0]['folio'];
                                    Ext.Ajax.request({
                                        url      : _URL_OBTIENE_LIGA_RECIBO,
                                        params   : {
                                                'params.cdunieco' : p_cdunieco,
                                                'params.cdramo'   : p_cdramo,
                                                'params.estado'   : p_estado,
                                                'params.nmpoliza' : p_nmpoliza,
                                                'params.nmfolcon' : folio
                                        },
                                        success  : function(response){
                                            var json = Ext.decode(response.responseText);
                                            debug('respuesta',json);
                                            if(!Ext.isEmpty(json.respuesta)){
                                                openInNewTab(json.respuesta);
                                            }
                                            else{
                                                mensajeError('No se recuper\u00f3 enlace de recibo consolidado');
                                            }
                                        },
                                        failure  : function(){
                                            errorComunicacion();
                                            winMask.close();
                                        }
                                    });
                                }
                            },
                            {
                                xtype    : 'button',
                                itemId   : 'btnReporte',
                                text     : 'Reporte',
                                handler  : function(){
                                    Ext.create('Ext.form.Panel').submit({
                                        url             : _URL_REPORTE_RECIBOS
                                        ,standardSubmit : true
                                        ,target         : '_blank'
                                        ,params         : {
                                            'params.cdunieco' : p_cdunieco,
                                            'params.cdramo'   : p_cdramo,
                                            'params.estado'   : p_estado,
                                            'params.nmpoliza' : p_nmpoliza
                                        }
                                    });
                                }
                            },
                            {
                                xtype    : 'button',
                                itemId   : 'btnDesglose',
                                text     : 'Desglose',
                                disabled : true,
                                handler  : function(){
                                    var lista = obtenerDataSelected(_fieldById('gridRecibos'));
                                    debug('lista',lista);
                                    var arrRec = [];
                                    for(var i = 0; i < lista.length; i++){
                                        arrRec[i] = lista[i]['nmrecibo'];
                                    }
                                    debug('arrRec',arrRec);
                                    Ext.create('Ext.form.Panel').submit({
                                        url             : _URL_OBTENCION_REPORTE
                                        ,standardSubmit : true
                                        ,target         : '_blank'
                                        ,params         : {
                                            'params.cdunieco' : p_cdunieco,
                                            'params.cdramo'   : p_cdramo,
                                            'params.estado'   : p_estado,
                                            'params.nmpoliza' : p_nmpoliza,
                                            'arrRec'          : arrRec
                                        }
                                    });
                                }
                            },
                            {
                                xtype    : 'button', 
                                itemId   : 'btnSimbologia',
                                text     : 'Simbologia',
                                disabled : false,
                                handler  : function(){
                                    winSimbologia.show();
                                }
                            },
                            {
                                xtype    : 'button', 
                                itemId   : 'btnHistCons',
                                text     : 'Historial consolidado',
                                hidden   : true,
                                disabled : false,
                                handler  : function(){
                                    _p25_storeHistCons.reload();
                                    winHistCons.show();
                                }
                            }
                        ]
                    })
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    _p25_storeRecibos.load({
        params: {
            'params.cdunieco' : p_cdunieco,
            'params.cdramo'   : p_cdramo,
            'params.estado'   : p_estado,
            'params.nmpoliza' : p_nmpoliza
        }
    });
    
    _p25_storeHistCons.load({
        params: {
            'params.cdunieco' : p_cdunieco,
            'params.cdramo'   : p_cdramo,
            'params.estado'   : p_estado,
            'params.nmpoliza' : p_nmpoliza
        }
    });
    
    _p25_storeRecibos.on({
        load : function(me){
            if(me.data.length === 0){
                _habilitarBoton('btnReporte'   ,true);
            }
        }
    });
    
    _fieldById('gridRecibos').on({
       selectionchange : function(me, selected){
           debug(selected);
           var conso    = 0;
           var descon   = 0;
           var serie    = 0;
           var folio    = 0;
           var noPend   = 0;
           deshabilitarBotones();
           if(me.selected.length > 0){
               debug('Antes de recorrer seleccionados ',me.selected);
               _habilitarBoton('btnDesglose'     ,true);
	           for(var i = 0; i < me.selected.items.length; i++){
	               if(i == 0){
                       serie = me.selected.items[i].data['codigo_serial'];  
                   }                   
	               if(esConsolidado(me.selected.items[i].data)){
	                   conso++;
	               }
	               else{
	                   debug('No es consolidado ');
	                   debug('x ',me.selected.items[i].data['codigo_serial']);
	                   debug('y ',serie);
	                   debug('z ',me.selected.items[i].data['status']);
	                   if(me.selected.items[i].data['codigo_serial'] === serie && me.selected.items[i].data['status'] === EstadoRecibo.Pendiente){
	                       descon++;
	                   }
	                   else{
	                       descon = 0;
	                       break;
	                   }
	               }
	               folio = me.selected.items[i].data['folio'];
	               serie = me.selected.items[i].data['codigo_serial'];
	           }
	           
	           debug('termina de recorrer seleccionados',folio,serie);
	           debug('Antes de entrar en condiciones',conso, descon);
	           debug('Seleccionados ',me.selected.length);
	           
	           if(conso === 0 && descon > 1){
	               _habilitarBoton('btnConsolidar'   ,true);  //consolidar
	               _habilitarBoton('btnDesglose'     ,true);  //desglose
	           }
	           
	           if(conso === 1 && sonConsolidados()){
	               seleccionarConsolidados(folio);
	               if(contarConsolidadosNoPendientes() < 1){
	                   _habilitarBoton('btnDesconsolidar',true);  //desconsolidar
	               }
	               else{
                       debug('else contarConsolidadosNoPendientes() ',contarConsolidadosNoPendientes());
                   }
	               _habilitarBoton('btnVerRecibo'    ,true);  //ver recibo
	               _habilitarBoton('btnDesglose'     ,true);  //desglose
	               _habilitarBoton('btnDetalle'      ,true);  //detalle
	           }
	           
	           if(conso > 1 && sonConsolidados()){
                   seleccionarConsolidados(folio);
                   if(contarConsolidadosNoPendientes() < 1){
                       debug('entro a if de pendientes');
                       _habilitarBoton('btnDesconsolidar',true);  //desconsolidar
                   }
                   else{
                       debug('else contarConsolidadosNoPendientes() ',contarConsolidadosNoPendientes());
                   }
                   _habilitarBoton('btnVerRecibo'    ,true);  //ver recibo
                   _habilitarBoton('btnDesglose'     ,true);  //desglose
                   _habilitarBoton('btnDetalle'      ,true);  //detalle
               }
	           
	           if(me.selected.length > 1){
	               _habilitarBoton('btnDesglose'     ,true);  //desglose
	           }
	           
	           if(me.selected.length === 1){
                   _habilitarBoton('btnDetalle'      ,true);  //detalle
               }
               
               if(me.selected.length > 1 && sonConsolidados() === true){
                   _habilitarBoton('btnDetalle'      ,true);  //detalle
               }
           }
       } 
    });
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
    ////// funciones //////
    function esConsolidado(data){
        debug('>esConsolidado', data);
        var consolidado = false;
        if(!Ext.isEmpty(data['folio'])){ //&& data['status'] === EstadoRecibo.Pendiente){
            consolidado = true;
        }
        debug('<esConsolidado',consolidado);
        return consolidado;
    }
    
    function obtenerDataSelected(grid){
        debug('>obtenerDataSelected',grid);
        var seleccionados = grid.getSelectionModel().getSelection();
        debug('grid',seleccionados);
        var listaRecibos = [];
        for(var i = 0; i < seleccionados.length; i++){
            var obj = seleccionados[i].data;
            obj['nmsuplem'] = seleccionados[i].raw['nmsuplem'];
            obj['ntramite'] = seleccionados[i].raw['ntramite'];
            obj['nmsolici'] = seleccionados[i].raw['nmsolici'];
            listaRecibos.push(obj);
        }
        debug('listaRecibos',listaRecibos);
        debug('<obtenerDataSelected',listaRecibos);
        return listaRecibos;
    }
    
    function consolidarRecibos(listaRecibos){
        debug('>consolidarRecibos');
        var winMask = _maskLocal('Consolidando recibos');
        Ext.Ajax.request({
            url      : _URL_CONSOLIDA_RECIBOS,
            jsonData : {
                params    : {
                    'cdunieco' : p_cdunieco,
                    'cdramo'   : p_cdramo,
                    'estado'   : p_estado,
                    'nmpoliza' : p_nmpoliza
                },
                loadList  : listaRecibos
            },
            success  : function(response){
                var json = Ext.decode(response.responseText);
                debug('recibos consolidados con numero de folio',json);
                _p25_storeRecibos.reload();
                _fieldById('gridRecibos').getSelectionModel().deselectAll();
                winMask.close();
                mensajeCorrecto('Aviso','Se consolido correctamente con n\u00f3mero de folio '+json.respuesta);
            },
            failure  : function(){
                errorComunicacion();
                winMask.close();
            }
        });
        debug('<consolidarRecibos');
    }
    
    function desconsolidarRecibos(listaRecibos){
        debug('>consolidarRecibos');
        var winMask = _maskLocal('Desconsolidando recibos');        
        Ext.Ajax.request({
            url      : _URL_DESCONSOLIDA_RECIBOS,
            jsonData : {
                params    : {
                    'cdunieco' : p_cdunieco,
                    'cdramo'   : p_cdramo,
                    'estado'   : p_estado,
                    'nmpoliza' : p_nmpoliza
                },
                loadList  : listaRecibos
            },
            success  : function(response){
                debug('recibos desconsolidados');
                _p25_storeRecibos.reload();
                _fieldById('gridRecibos').getSelectionModel().deselectAll();
                winMask.close();
                mensajeCorrecto('Aviso','Se des consolido correctamente');
            },
            failure  : function(){
                errorComunicacion();
                winMask.close();
            }
        });
        debug('<consolidarRecibos');
    }
    
    function seleccionarConsolidados(folio){
        debug('>seleccionarConsolidados',folio);
        gridRecibos = _fieldById('gridRecibos');
        try{
            for(var s in gridRecibos.store.data.items){
                var rec = gridRecibos.store.getAt(s).raw;
                //debug('rec',rec);
                if(folio === rec['folio']){
                    debug('rec',rec);
                    //gridRecibos.getSelectionModel().deselectAll(true);
                    var modelRec = new _p25_modeloRecibo(rec);
                    debug('modelRec',modelRec);
                    gridRecibos.getSelectionModel().select(modelRec, true, true);
                }
            }
        }
        catch(err){
            debug("Error",err.message);
        }
        debug('<seleccionarConsolidados');
    }
    
    function deshabilitarBotones(){
        debug('>deshabilitarBotones');
        _habilitarBoton('btnConsolidar'   ,false);
        _habilitarBoton('btnDesconsolidar',false);
        _habilitarBoton('btnDetalle'      ,false);
        _habilitarBoton('btnDesglose'     ,false);
        _habilitarBoton('btnVerRecibo'    ,false);
        debug('<deshabilitarBotones');
    }
    
    function sonConsolidados(){
        debug('>sonConsolidados');
        gridRecibos = _fieldById('gridRecibos');
        var consolidado    = 0;
        var desconsolidado = 0;
        var folio;
        var result = true;
        try{
            var gridStore = gridRecibos.getSelectionModel().getSelection();
            debug('gridStore ',gridStore); 
            for(var s = 0; s < gridStore.length; s++){
                var rec = gridStore[s].data;
                if(!Ext.isEmpty(rec['folio'])){
                    consolidado++;                    
                }
                else{
                    desconsolidado++;
                }
                if(desconsolidado > 0){
                    result = false;
                    break;
                }
                debug('folio ',folio);
                debug('rec[folio] ',rec['folio']);
                if(!Ext.isEmpty(folio) && rec['folio'] != folio){
                    result = false;
                    break;
                }
                folio = rec['folio'];
            }
        }
        catch(err){
            debug("Error",err.message);
        }
        debug('<sonConsolidados', result);
        return result;
    }
    
    function openInNewTab(url) {
        var win = window.open(url, '_blank');
        win.focus();
    }
    
    function contarConsolidadosNoPendientes(){
        debug('>contarConsolidadosNoPendientes');
        gridRecibos = _fieldById('gridRecibos').getSelectionModel().getSelection();
        var noPendientes = 0;
        try{
            for(var s = 0; s < gridRecibos.length; s++){
                if(gridRecibos[s].data['status'] != EstadoRecibo.Pendiente){
                    noPendientes++;
                }
            }
        }
        catch(err){
            debug("Error",err.message);
        }        
        debug('<contarConsolidadosNoPendientes',noPendientes);
        return noPendientes;
    }
    ////// funciones //////
    
    pRcb_wndDetalleRecibo = Ext.create('Ext.window.Window', {
        title       : 'Detalle de recibo',
        width       : 430,
        modal       : true,
        closeAction : 'hide',
        items       : [{
            xtype       : 'grid',
            store       : _p25_storeDetalleRecibo,
            height      : 285,
            autoScroll  : true,
            columns     : itemsDetalleColumns
        }],
        buttons: [{
            text    : 'Aceptar',
            handler : function(btn) {
                pRcb_wndDetalleRecibo.close();
            }
        }]
    });
    
    winSimbologia= Ext.create('Ext.window.Window',{
        title       : 'Simbologia',
        width       : 400,
        modal       : true,
        closeAction : 'hide',
        items       : [
            Ext.create('Ext.form.Panel', {
                bodyPadding : 15,
                defaults    : {
                    width      : 350,
                    readOnly   : true
                },
                items       : [
                    {
                        xtype       : 'textfield',
                        name        : 'fieldPagadoId',
                        fieldLabel  : 'Pagado',
                        labelWidth  : 160,
                        fieldStyle  : 'background-color: #80FF00; background-image: none; width:150px;'
                    },
                    {
                        xtype       : 'textfield',
                        name        : 'fieldGeneradoNoId',
                        fieldLabel  : 'Generado(No imp)',
                        labelWidth  : 160,
                        fieldStyle  : 'background-color: #81DAF5; background-image: none; width:150px;'
                    },
                    {
                        xtype       : 'textfield',
                        name        : 'fieldGeneradoId',
                        fieldLabel  : 'Devuelto',
                        labelWidth  : 160,
                        fieldStyle  : 'background-color: #80FF00; background-image: none; width:150px;'
                    },
                    {
                        xtype       : 'textfield',
                        name        : 'fieldCanceladoId',
                        fieldLabel  : 'Cancelado',
                        labelWidth  : 160,
                        fieldStyle  : 'background-color: #FA8258; background-image: none; width:150px;',
                    },
                    {
                        xtype       : 'textfield',
                        name        : 'fieldGeneradoNoId',
                        fieldLabel  : 'Generado(Fuera de vigor)',
                        labelWidth  : 160,
                        fieldStyle  : 'background-color: #FFFFFF; background-image: none; width:150px;'
                    },
                    {
                        xtype       : 'textfield',
                        name        : 'fieldDespagoId',
                        fieldLabel  : 'Despago',
                        labelWidth  : 160,
                        fieldStyle  : 'background-color: #F5A9A9; background-image: none; width:150px;'
                    }
                ],
                buttons     : [{
                    text    : 'Aceptar',
                    handler : function(btn){
                        winSimbologia.close();
                    }
                }]
            })
        ]
    });
    
    winHistCons = Ext.create('Ext.window.Window',{
        title       : 'Historial de consolidacion',
        width       : 600,
        modal       : true,
        itemId      : 'winHistCons',
        closeAction : 'hide',
        items       : [
            Ext.create('Ext.form.Panel', {
                bodyPadding : 15,
                defaults    : {
                    width      : 580,
                    readOnly   : true
                },
                items       : [{
                    xtype       : 'grid',
                    itemId      : 'idGridHistCons',
                    store       : _p25_storeHistCons,
                    height      : 285,
                    autoScroll  : true,
                    columns     : itemsBitacoraColumns                    
                }],
                buttons     : [{
                    text    : 'Aceptar',
                    handler : function(btn){
                        _fieldById('winHistCons').close();
                    }
                }]
            })
        ]
    });
});
</script>
</head>
<body>
<div id="dvRecibos" style="height:350px;"></div>
</body>
</html>