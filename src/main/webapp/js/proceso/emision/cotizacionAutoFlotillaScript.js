function transformaSigsEnIce (sigs) {
    debug('transformaSigsEnIce() args:', arguments);
    var ice = {
        smap1: {
            CDIDEPER: sigs.smap1.cdper,
            CDPERSON: sigs.smap1.cdperson,
            CDUNIECO: sigs.smap1.CDUNIECO,
            ESTADO: 'W',
            FEINI: !Ext.isEmpty(sigs.slist1[0].feini) ? sigs.slist1[0].feini : sigs.smap1.feini,
            FEFIN: !Ext.isEmpty(sigs.slist1[0].fefin) ? sigs.slist1[0].fefin : sigs.smap1.fefin,
            FESOLICI: sigs.smap1.fesolici,
            NMPOLIZA: '',
            TRAMITE: _p30_flujo.ntramite,
            TIPOFLOT: _p30_smap1.tipoflot,
            //aux...
            cdramo: sigs.smap1.cdramo,
            diasValidos: '20',
            nmpoliza: '',
            ntramiteIn: _p30_flujo.ntramite,
            'parametros.pv_cdagrupa': null,
            'parametros.pv_cdasegur': null,
            'parametros.pv_cdestado': null,
            'parametros.pv_cdgrupo': null,
            'parametros.pv_cdplan': null,
            'parametros.pv_cdramo': sigs.smap1.cdramo,
            'parametros.pv_cdtipsit': 'XPOLX',
            'parametros.pv_cdunieco': sigs.smap1.CDUNIECO,
            'parametros.pv_dsgrupo': null,
            'parametros.pv_estado': 'W',
            'parametros.pv_fecharef': 'null',
            'parametros.pv_fefecsit': 'null',
            'parametros.pv_nmpoliza': '',
            'parametros.pv_nmsbsitext': null,
            'parametros.pv_nmsitaux': null,
            'parametros.pv_nmsituac': '-1',
            'parametros.pv_nmsituaext': null,
            'parametros.pv_nmsuplem': '0',
            //parametros.pv_otvalorXY
            'parametros.pv_status': 'V',
            'parametros.pv_swreduci': null,
            'cambio': sigs.smap1.tvalopol_4,
            'moneda': sigs.smap1.tvalopol_5
        },
        slist2: [],
        success: true,
        exito: true
    };
    
    if(sigs.slist1[0].CDRAMO!=null && sigs.slist1[0].CDRAMO == Ramo.AutosFronterizos)
	{
    	 for (var att in sigs.smap1) {
    	    	// a u x . ...
    	    	//0 1 2 3 4
    	    	if (att.slice(0, 11) === 'aux.otvalor') {
    	    		ice.smap1['parametros.pv_otvalor'+att.slice(att.length-2,att.length)] = sigs.smap1[att];
    	    		delete sigs.smap1[att];
    	    	}
    	    }
	}
    else
    {
        //ice.smap1.parametros.pv_otvalor...
        for (var att in sigs.slist1[0]) {
            // p a r a m e t r o s . p v _ o t v a l o r ...
            //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
            if (att.slice(0, 21) === 'parametros.pv_otvalor') {
                ice.smap1[att] = sigs.slist1[0][att];
            }
        }
    }
    
    //ice.smap1.aux...
    for (var att in sigs.smap1) {
    	// a u x . ...
    	//0 1 2 3 4
    	if (att.slice(0, 4) === 'aux.') {
    		ice.smap1[att] = sigs.smap1[att];
    	}
    }
    
    for (var i = 0; i < sigs.slist1.length; i++) {
        var autoSigs = sigs.slist1[i];
        var autoIce  = {
            CDAGRUPA: '1',
            CDASEGUR: '30',
            CDESTADO: '0',
            CDGRUPO: '',
            CDPLAN: autoSigs.cdplan,
            CDRAMO: autoSigs.cdramo,
            CDTIPSIT: autoSigs.cdtipsit,
            CDUNIECO: autoSigs.cdunieco,
            DSGRUPO: '',
            ESTADO: 'W',
            FECHAREF: autoSigs.feini,
            FEFECSIT: autoSigs.feini,
            NMPOLIZA: '',
            NMSBSITEXT: '',
            NMSITAUX: '1',
            NMSITUAC: autoSigs.nmsituac,
            NMSITUAEXT: '',
            NMSUPLEM: '0',
            STATUS: 'V',
            SWREDUCI: '',
            cdplan: autoSigs.cdplan,
            cdtipsit: autoSigs.cdtipsit,
            nmsituac: autoSigs.nmsituac
            //parametros.pv_otvalor...
        };
        for (var att in autoSigs) {
            // p a r a m e t r o s . p v _ o t v a l o r ...
            //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
            if (att.slice(0, 21) === 'parametros.pv_otvalor') {
                autoIce[att] = autoSigs[att];
            }
        }
        ice.slist2.push(autoIce);
    }
    
    debug('transformaSigsEnIce ice:', ice);
    return ice;
}

/*
 * Esta funcion se usa para recuperar una poliza de SIGS para renovacion en ICE
 * solo debe llamarse si: es por flujo, y si no hay una cotizacion anterior
 * si se recupera cotizacion por _p30_recuperarCotizacionDeTramite entonces
 * esta funcion no debe recuperar nada porque seria doble recuperacion, es decir
 * la condicion que tenga _p30_recuperarCotizacionDeTramite debe estar negada aqui
 */
function _p30_recuperarPolizaSIGS()
{
    if(!Ext.isEmpty(_p30_flujo))
    {
        debug('_p30_recuperarPolizaSIGS');
        
        var mask, ck = 'Recuperando cotizaci\u00f3n de tr\u00e1mite para revisar renovaci\u00f3n';
        
        try
        {
            mask = _maskLocal(ck);
            Ext.Ajax.request(
            {
                url      : _p30_urlRecuperarOtvalorTramiteXDsatribu
                ,params  :
                {
                    'params.ntramite'  : _p30_flujo.ntramite
                    ,'params.dsatribu' : 'COTIZACI%N%TR%MITE'
                }
                ,success : function(response)
                {
                    mask.close();
                    var ck = 'Decodificando respuesta al recuperar cotizaci\u00f3n de tr\u00e1mite';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### cotizacion de tramite:',json);
                        if(json.success===true)
                        {
                            if(Ext.isEmpty(json.params.otvalor)) // si no se ha cotizado antes, verificamos si hay renovacion
                            {
                                ck = 'Revisando p\u00f3liza de renovaci\u00f3n';
                                
                                mask = _maskLocal(ck);
                                
                                Ext.Ajax.request(
                                {
                                    url      : _p30_urlRecuperarDatosTramiteValidacion
                                    ,params  : _flujoToParams(_p30_flujo)
                                    ,success : function(response)
                                    {
                                        mask.close();
                                        
                                        var ck = 'Decodificando respuesta al recuperar datos para revisar renovaci\u00f3n';
                                        
                                        try
                                        {
                                            var jsonDatTram = Ext.decode(response.responseText);
                                            debug('### jsonDatTram:',jsonDatTram,'.');
                                            
                                            if(jsonDatTram.success === true)
                                            {
                                                if(!Ext.isEmpty(jsonDatTram.datosTramite.TRAMITE.RENPOLIEX))
                                                {
                                                    var renuniext  = jsonDatTram.datosTramite.TRAMITE.RENUNIEXT
                                                        ,renramo   = jsonDatTram.datosTramite.TRAMITE.RENRAMO
                                                        ,renpoliex = jsonDatTram.datosTramite.TRAMITE.RENPOLIEX;
                                                    debug('se encontraron datos para renovar:',renuniext,renramo,renpoliex,'.');
                                                    _p30_cargarPolizaSIGS(renuniext, renramo, renpoliex);
                                                }
                                            }
                                            else
                                            {
                                                mensajeError(jsonDatTram.message);
                                            }
                                        }
                                        catch(e)
                                        {
                                            manejaException(e,ck,mask);
                                        }
                                        
                                    }
                                    ,failure : function()
                                    {
                                        mask.close();
                                        errorComunicacion(null,'Error al recuperar datos de tr\u00e1mite para revisar renovaci\u00f3n');
                                    }
                                });
                            }
                        }
                    }
                    catch(e)
                    {
                        manejaException(e,ck,mask);
                    }
                }
                ,failure : function()
                {
                    mask.close();
                    errorComunicacion(null,'Error al recuperar cotizaci\u00f3n de tr\u00e1mite para revisar renovaci\u00f3n');
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck,mask);
        }
    }
}

function _p30_cargarPolizaSIGS(sucursal, ramo, poliza)
{
    debug('>_p30_cargarPolizaSIGS args:', arguments);
    var mask, ck = 'Recuperando datos de cotizacion desde SIGS';
    try {
        if (Ext.isEmpty(sucursal) || Ext.isEmpty(ramo) || Ext.isEmpty(poliza)) {
            throw 'Faltan datos para recuperar renovaci\u00f3n';
        }
        ck = 'Recuperando p\u00f3liza para renovar';
        mask = _maskLocal(ck);
        Ext.Ajax.request({
            url: _p30_urlCargarPolizaSIGS,
            params: {
                'smap1.cdsucursal'  : sucursal
                ,'smap1.cdramo'     : ramo
                ,'smap1.cdpoliza'   : poliza
                ,'smap1.tipoflot'   : _p30_smap1.tipoflot
                ,'smap1.cdtipsit'   : 'AR'
                ,'smap1.cargaCotiza': 'S'
            },
            success: function (response) {
                mask.close();
                var ck = 'Decodificando respuesta al recuperar p\u00f3liza para renovar';
                try {
                    var json = Ext.decode(response.responseText);
                    debug("### peticion poliza SIGS: ", json);
                    if (Ext.isEmpty(json.smap1.valoresCampos)) {
                        throw 'No se encontraron datos para renovar';
                    }
                    var json2 = Ext.decode(json.smap1.valoresCampos);
                    debug("### datos poliza SIGS: ", json2);
                    json2['success'] = true;
                    // TODO cdper = json2.smap1.cdper;       //D00000000111005
                    // TODO cdperson = json2.smap1.cdperson; //530400
                    if (!Ext.isEmpty(json2.smap1.mensajeError))
                    {
                        mensajeError(json2.smap1.mensajeError);
                    }
                    else
                    {
                        llenandoCampos(transformaSigsEnIce(json2), '', true);
                    }
                } catch (e) {
                    manejaException(e,ck);
                }
            },
            failure: function () {
                mask.close();
                errorComunicacion(null, 'Error al recuperar p\u00f3liza de SIGS');
            }
        });
    } catch (e) {
        manejaException(e, ck, mask);
    }
}

function llenandoCampos (json, nmpoliza, renovacionSIGS) {
    debug('>llenandoCampos args:', arguments);
    var ck = 'Seteando valores recuperados';
    try {
        _p30_smap1.cdunieco=json.smap1.CDUNIECO;
        
        var maestra=json.smap1.ESTADO=='M';

        _p30_limpiar();

        var fesolici    = Ext.Date.parse(json.smap1.FESOLICI,'d/m/Y');
        var fechaHoy    = Ext.Date.clearTime(new Date());
        var fechaLimite = Ext.Date.add(fechaHoy,Ext.Date.DAY,-1*(json.smap1.diasValidos-0));
        var vencida     = fesolici<fechaLimite;
        debug('fesolici='    , fesolici);
        debug('fechaHoy='    , fechaHoy);
        debug('fechaLimite=' , fechaLimite);
        debug('vencida='     , vencida , '.');
        
        if(!Ext.isEmpty(json.smap1.FEINI) && !Ext.isEmpty(json.smap1.FEFIN))
        {
            var iniVig = Ext.Date.parse(json.smap1.FEINI,'d/m/Y');
            var finVig = Ext.Date.parse(json.smap1.FEFIN,'d/m/Y');
            _fieldByName('feini').setValue(iniVig);
            _fieldByName('fefin').setValue(finVig);
        }
        else
        {
            _fieldByName('feini').setValue(new Date());
        }

        var milDif = finVig-iniVig;
        var diaDif = milDif/(1000*60*60*24);
        debug('diaDif:',diaDif);
        _fieldByName('fefin').setValue
        (
            Ext.Date.add
            (
                _fieldByName('feini').getValue()
                ,Ext.Date.DAY
                ,diaDif
            )
        );
                
        if(maestra)
        {
            _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue('');
            mensajeWarning('Se va a duplicar la p&oacute;liza emitida '+json.smap1.NMPOLIZA);
        }
        else if(vencida)
        {
            _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue('');
            mensajeWarning('La cotizaci&oacute;n ha vencido y solo puede duplicarse');
        }
        else
        {
            _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo=true;
            _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue(nmpoliza);
            _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo=false;
        }
        
        _p30_cargarIncisoXpolxTvalopolTconvalsit(json, true === renovacionSIGS);
        
        ck='Recuperando incisos base';
        var recordsAux = [];
    	if(_p30_smap1.cdramo=='5')
        {
           var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
           try{
   	        if(_p30_smap1.turistas=='S'){
   	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
   	    	}
           }catch(e){
           	debugError(e);
           }
           if(itemsTatripol[1].fieldLabel == "MONEDA")
           {
        	  
        	  
        	   if(_p30_smap1.turistas=='S'){
        		   json.smap1.moneda=json.smap1['aux.otvalor05']
        	   }
           	  if(json.smap1.moneda == '2')
           	  {
              itemsTatripol[1].setValue('DOLARES');
              itemsTatripol[0].setValue(json.smap1.cambio)
           	  }
              else
              {
              itemsTatripol[1].setValue('PESOS');
              itemsTatripol[0].setValue(json.smap1.cambio)
              }
           }
           
           for(var i in json.slist2)
           {
               if('|AF|PU|'.lastIndexOf('|'+json.slist2[i].CDTIPSIT+'|')!=-1)
                {
                    if(json.slist2[i]['parametros.pv_otvalor02'] == '1')
                    {
                          var me  =_fieldById('_p30_tatrisitParcialForm'+json.slist2[i].CDTIPSIT).down('[fieldLabel=TIPO VALOR]')
                          var record = me.findRecordByValue('1');
                          if(!record)
                          {
                                _fieldById('_p30_tatrisitParcialForm'+json.slist2[i].CDTIPSIT).down('[fieldLabel=TIPO VALOR]').store.add({key:'1',value:'Valor Convenido'});
                                _fieldById('_p30_tatrisitParcialForm'+json.slist2[i].CDTIPSIT).down('[fieldLabel=TIPO VALOR]').setValue('1');
                          }
                    }
                }
                else
                {
                    if(json.slist2[i]['parametros.pv_otvalor12'] == '1')
                    {
                          var me  =_fieldById('_p30_tatrisitParcialForm'+json.slist2[i].CDTIPSIT).down('[fieldLabel=TIPO VALOR]')
                          var record = me.findRecordByValue('1');
                          if(!record)
                          {
                                _fieldById('_p30_tatrisitParcialForm'+json.slist2[i].CDTIPSIT).down('[fieldLabel=TIPO VALOR]').store.add({key:'1',value:'Valor Convenido'});
                                _fieldById('_p30_tatrisitParcialForm'+json.slist2[i].CDTIPSIT).down('[fieldLabel=TIPO VALOR]').setValue('1');
                          }
                    }
                }
            recordsAux.push(new _p30_modelo(json.slist2[i]));
            }
        }
        _p30_store.add(recordsAux);
        
        if(maestra||vencida||(renovacionSIGS === true))
        {
            _fieldById('_p30_form').formOculto.getForm().reset();
        }

        if (renovacionSIGS === true) {
            _p30_cotizar(false);
        } else if(Ext.isEmpty(json.smap1.NTRAMITE)||vencida||maestra) {
            _p30_cotizar(!maestra&&!vencida);
        }
        else
        {
            centrarVentanaInterna(Ext.create('Ext.window.Window',
            {
                title      : 'P&oacute;liza en emisi&oacute;n'
                ,modal     : true
                ,bodyStyle : 'padding:5px;'
                ,closable  : false
                ,html      : 'La cotizaci&oacute;n se encuentra en proceso de emisi&oacute;n'
                ,buttonAlign : 'center'
                ,buttons   :
                [
                    {
                        text     : 'Complementar'
                        ,handler : function()
                        {
                            var swExiper = (Ext.isEmpty(json.smap1.CDPERSON) && !Ext.isEmpty(json.smap1.CDIDEPER))? 'N' : 'S' ;
                            Ext.create('Ext.form.Panel').submit(
                            {
                                url             : _p30_urlDatosComplementarios
                                ,standardSubmit : true
                                ,params         :
                                {
                                    'smap1.cdunieco'  : json.smap1.CDUNIECO
                                    ,'smap1.cdramo'   : json.smap1.cdramo
                                    ,'smap1.cdtipsit' : _p30_smap1.cdtipsit
                                    ,'smap1.estado'   : 'W'
                                    ,'smap1.nmpoliza' : json.smap1.nmpoliza
                                    ,'smap1.ntramite' : json.smap1.NTRAMITE
                                    ,'smap1.swexiper' : swExiper
                                    ,'smap1.tipoflot' : json.smap1.TIPOFLOT
                                }
                            });
                        }
                    }
                    ,{
                        text     : 'Duplicar'
                        ,handler : function(bot)
                        {
                            bot.up('window').destroy();
                            _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue('');
                            _fieldById('_p30_form').formOculto.getForm().reset();
                        }
                    }
                ]
            }).show());
        }
    } catch (e) {
        manejaException(e, ck);
    }
}

function _p30_actualizarCotizacionTramite(callback)
{
    var ck = 'Registrando cotizaci\u00f3n de tr\u00e1mite';
    try
    {
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p30_urlActualizarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : _p30_flujo.ntramite
                ,'params.dsatribu' : 'COTIZACI%N%TR%MITE%'
                ,'params.otvalor'  : _fieldByName('nmpoliza').getValue()
                ,'params.accion'   : 'U'
            }
            ,success : function(response)
            {
                _unmask();
                var ck = 'Decodificando respuesta al guardar estatus de tr\u00e1mite';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### guardar estatus de tramite:',json);
                    if(json.success===true)
                    {
                        var ck = 'Guardando detalle de cotiazci\u00f3n de tr\u00e1mite';
                        try
                        {
                            _mask(ck);
                            Ext.Ajax.request(
                            {
                                url      : _p30_urlDetalleTramite
                                ,params  :
                                {
                                    'smap1.ntramite'  : _p30_flujo.ntramite
                                    ,'smap1.status'   : _p30_flujo.status
                                    ,'smap1.dscoment' : 'Se guard\u00f3 la cotizaci\u00f3n '+_fieldByName('nmpoliza').getValue()
                                    ,'smap1.swagente' : 'S'
                                }
                                ,success : function(response)
                                {
                                    _unmask();
                                    var ck = 'Decodificando respuesta al guardar detalle de cotizaci\u00f3n de tr\u00e1mite';
                                    try
                                    {
                                        var jsonDetalle = Ext.decode(response.responseText);
                                        debug('### guardar detalle cotizacion tramite:',jsonDetalle);
                                        if(!Ext.isEmpty(callback))
                                        {
                                            callback();
                                        }
                                    }
                                    catch(e)
                                    {
                                        manejaException(e,ck);
                                    }
                                }
                                ,failure : function()
                                {
                                    _unmask();
                                    errorComunicacion(null,'Error al guardar detalle de cotizaci\u00f3n de tr\u00e1mite');
                                }
                            });
                        }
                        catch(e)
                        {
                            _unmask();
                            manejaException(e,ck);
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
                _unmask();
                errorComunicacion(null,'Error al guardar estatus de tr\u00e1mite');
            }
        });
    }
    catch(e)
    {
        _unmask();
        manejaException(e,ck);
    }
}

function _p30_actualizarSwexiperTramite(callback)
{
    var ck = 'Registrando estado de cliente de tr\u00e1mite';
    try
    {
        var swExiper =
        (
            !Ext.isEmpty(_p30_recordClienteRecuperado)
            && Ext.isEmpty(_p30_recordClienteRecuperado.raw.CLAVECLI)
            && !Ext.isEmpty(_p30_recordClienteRecuperado.raw.CDIDEPER)
        ) ? 'N' : 'S' ;
    
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p30_urlActualizarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : _p30_flujo.ntramite
                ,'params.dsatribu' : 'SWEXIPER'
                ,'params.otvalor'  : swExiper
                ,'params.accion'   : 'U'
            }
            ,success : function(response)
            {
                _unmask();
                var ck = 'Decodificando respuesta al guardar estado de cliente de tr\u00e1mite';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### guardar estatus de cliente de tramite:',json);
                    if(json.success===true)
                    {
                        if(!Ext.isEmpty(callback))
                        {
                            callback();
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
                _unmask();
                errorComunicacion(null,'Error al guardar estado de cliente de tr\u00e1mite');
            }
        });
    }
    catch(e)
    {
        _unmask();
        manejaException(e,ck);
    }
}

function _p30_botonOnCotizarClic (me) {
    debug('_p30_botonOnCotizarClic args:', arguments);
    var ck = 'Enviando datos de cotizaci\u00f3n';
    try {
        Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES + 'VentanaTurnado');
        new VentanaTurnado({
            cdtipflu  : _p30_flujo.cdtipflu,
            cdflujomc : _p30_flujo.cdflujomc,
            tipoent   : _p30_flujo.tipoent,
            claveent  : 0,//_p28_flujo.claveent,
            webid     : 0,//_p28_flujo.webid,
            aux       : _p30_flujoAux.onCotizar,
            ntramite  : _p30_flujo.ntramite,
            status    : _p30_flujo.status,
            cdunieco  : _p30_flujo.cdunieco,
            cdramo    : _p30_flujo.cdramo,
            estado    : _p30_flujo.estado,
            nmpoliza  : _p30_flujo.nmpoliza,
            nmsituac  : _p30_flujo.nmsituac,
            nmsuplem  : _p30_flujo.nmsuplem,
            cdusuari  : _p30_smap1.cdusuari,
            cdsisrol  : _GLOBAL_CDSISROL
        }).mostrar();
    } catch (e) {
        manejaException(e, ck);
    }
}

function turistasFormaPago(feini,fefin,listFP){
	try{
		var date1 = feini;
		var date2 = fefin;
		var timeDiff = Math.abs(date2.getTime() - date1.getTime());
		var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
		var fp=[];
		if(diffDays<365){
			listFP.forEach(function(it){
				if(it.CDPERPAG==FormaPago.ANUAL || it.CDPERPAG==FormaPago.CONTADO){
					fp.push(it)
					return;
				}
			})
			return fp;
		}
	}catch(e){
		debugError(e);
	}
	return listFP;
}
