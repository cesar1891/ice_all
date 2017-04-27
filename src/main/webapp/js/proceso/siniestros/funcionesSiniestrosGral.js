function cargarPaginacion(ntramite,nfactura){
    debug("######ENTRA#####");
    storeAseguradoFactura.removeAll();
    var params = {
        'smap.ntramite'  : ntramite
        ,'smap.nfactura' : nfactura
    };
    cargaStorePaginadoLocal(storeAseguradoFactura, _URL_OBTENERSINIESTROSTRAMITE, 'slist1', params, function(options, success, response){
        if(success){
            var jsonResponse = Ext.decode(response.responseText);
        }else{
            centrarVentanaInterna(showMessage('Error', 'Error al obtener los datos',Ext.Msg.OK, Ext.Msg.ERROR));
        }
    });
}
//1.- Regresar a Mesa de Control
function _11_regresarMC() {
    debug('_11_regresarMC');
    Ext.create('Ext.form.Panel').submit({
        url             : _URL_MESACONTROL
        ,standardSubmit : true
        ,params         : {
            'smap1.gridTitle'       : 'Siniestros en espera'
            ,'smap2.pv_cdtiptra_i'  : _11_params.CDTIPTRA
        }
    });
}

// (EGS)
function calculaEdad(){
    var fecha = ''; 
    fecha = fecha.concat(_fenacimi.substr(6,4),_fenacimi.substr(2,4),_fenacimi.substr(0,2));
    _edad = calculaAniosTranscurridos(new Date(fecha),new Date());
}