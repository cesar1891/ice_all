<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<script>
///////////////////////
////// overrides //////
/*///////////////////*/
Ext.override(Ext.form.TextField,
{
    initComponent:function()
    {
        Ext.apply(this,
        {
        	labelWidth : 250
        });
        return this.callParent();
    }
});
/*///////////////////*/
////// overrides //////
///////////////////////

///////////////////////
////// variables //////
/*///////////////////*/

//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _0_smap1      = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _0_flujo      = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_0_flujo:', _0_flujo);

var _0_flujoAux = {};
try {
    if (!Ext.isEmpty(_0_flujo.aux)) {
        _0_flujoAux = Ext.decode(_0_flujo.aux);
    }
} catch (e) {
    debugError('WARNING al decodificar _0_flujo.aux como JSON:', _0_flujo, e);
}
debug('_0_flujoAux:', _0_flujoAux);

var _0_reporteCotizacion = '<s:text name='%{"rdf.cotizacion.nombre."+smap1.cdtipsit.toUpperCase()}' />';
var _0_urlImprimirCotiza = '<s:property value="rutaServidorReports" />';
var _0_reportsServerUser = '<s:property value="passServidorReports" />';

var _0_urlCotizar                  = '<s:url namespace="/emision"         action="cotizar"                        />';
var _0_urlCotizarExterno           = '<s:url namespace="/externo"         action="cotizar"                        />';
var _0_urlDetalleCotizacion        = '<s:url namespace="/"                action="detalleCotizacion"              />';
var _0_urlCoberturas               = '<s:url namespace="/flujocotizacion" action="obtenerCoberturas4"             />';
var _0_urlDetalleCobertura         = '<s:url namespace="/flujocotizacion" action="obtenerAyudaCoberturas4"        />';
var _0_urlEnviarCorreo             = '<s:url namespace="/general"         action="enviaCorreo"                    />';
var _0_urlViewDoc                  = '<s:url namespace ="/documentos"     action="descargaDocInline"              />';
var _0_urlComprar                  = '<s:url namespace="/flujocotizacion" action="comprarCotizacion4"             />';
var _0_urlVentanaDocumentos        = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza"        />';
var _0_urlDatosComplementarios     = '<s:url namespace="/"                action="datosComplementarios"           />';
var _0_urlUpdateStatus             = '<s:url namespace="/mesacontrol"     action="actualizarStatusTramite"        />';
var _0_urlMesaControl              = '<s:url namespace="/mesacontrol"     action="mcdinamica"                     />';
var _0_urlLoad                     = '<s:url namespace="/emision"         action="cargarCotizacion"               />';
var _0_urlNada                     = '<s:url namespace="/emision"         action="webServiceNada"                 />';
var _0_urlCargarCduniecoAgenteAuto = '<s:url namespace="/emision"         action="cargarCduniecoAgenteAuto"       />';
var _0_urlRecuperarCliente         = '<s:url namespace="/"                action="buscarPersonasRepetidas"        />';
var _0_urlCargarAgentePorFolio     = '<s:url namespace="/emision"         action="cargarCdagentePorFolio"         />';
var _0_urlObtenerParametros        = '<s:url namespace="/emision"         action="obtenerParametrosCotizacion"    />';
var _0_urlCargarAutoPorClaveGS     = '<s:url namespace="/emision"         action="cargarAutoPorClaveGS"           />';
var _0_urlCargarSumaAsegurada      = '<s:url namespace="/emision"         action="cargarSumaAseguradaAuto"        />';
var _0_urlObtenerCliente           = '<s:url namespace="/emision"         action="cargarClienteCotizacion"        />';
var _0_urlValidarCambioZonaGMI     = '<s:url namespace="/emision"         action="validarCambioZonaGMI"           />';
var _0_urlValidarEnfermCatasGMI    = '<s:url namespace="/emision"         action="validarEnfermedadCatastGMI"     />';
var _0_urlRecuperacionSimple       = '<s:url namespace="/emision"         action="recuperacionSimple"             />';
var _0_urlRetroactividadDifer      = '<s:url namespace="/emision"         action="cargarRetroactividadSuplemento" />';
var _0_urlObtieneValNumeroSerie    = '<s:url namespace="/emision" 		  action="obtieneValNumeroSerie"          />';
var _p0_urlCargarPoliza           = '<s:url namespace="/emision"         action="cargarPoliza"                    />';
var _0_urlCargarDetalleNegocioRamo5= '<s:url namespace="/emision"         action="cargarDetalleNegocioRamo5"      />';
var url_obtiene_forma_pago					  = '<s:url namespace="/emision"          action="obtieneFormaPago"    />';

var _0_urlDetalleTramite                    = '<s:url namespace="/mesacontrol"      action="movimientoDetalleTramite"                    />';
var _0_urlActualizarOtvalorTramiteXDsatribu = '<s:url namespace="/emision"          action="actualizarOtvalorTramitePorDsatribu"         />';
var _0_urlRecuperarOtvalorTramiteXDsatribu  = '<s:url namespace="/emision"          action="recuperarOtvalorTramitePorDsatribu"          />';
var _0_urlCargarParamerizacionCoberturas    = '<s:url namespace="/emision"          action="cargarParamerizacionConfiguracionCoberturas" />';
var _0_urlRecuperarDatosTramiteValidacion   = '<s:url namespace="/flujomesacontrol" action="recuperarDatosTramiteValidacionCliente"      />';
var _0_urlCargarPoliza                      = '<s:url namespace="/emision"          action="cargarPoliza"                                />';
var _0_urlCargarCatalogo                    = '<s:url namespace="/catalogos"       action="obtieneCatalogo"                     />';
var _0_urlCargaValidacionDescuentoR6        = '<s:url namespace="/emision"         action="obtieneValidacionDescuentoR6"                 />';
var _0_urlAplicaDxn                         = '<s:url namespace="/emision"         action="aplicaDxn"                 />';


var _0_modeloExtraFields = [
<s:if test='%{getImap().get("modeloExtraFields")!=null}'>
    <s:property value="imap.modeloExtraFields" />
</s:if>
];

var _0_necesitoIncisos = true;
<s:if test='%{getImap().get("fieldsIndividuales")==null}'>
    _0_necesitoIncisos = false;
</s:if>
_0_smap1.conincisos=_0_necesitoIncisos?'si':'no';
debug('_0_necesitoIncisos:',_0_necesitoIncisos);

if(!Ext.isEmpty(_0_flujo))
{
    <s:url namespace="/flujomesacontrol" action="mesaControl" var="urlMesaFlujo" includeParams="get">
        <s:param name="params.AGRUPAMC" value="%{'PRINCIPAL'}" />
    </s:url>
    _0_urlMesaControl = '<s:property value="urlMesaFlujo" />';
    debug('_0_urlMesaControl:',_0_urlMesaControl);
}

var _0_panelPri;
var _0_formAgrupados;
var _0_formAvisos;
var _0_gridIncisos;
var _0_botonera;
var _0_storeIncisos;
var _0_gridTarifas;
var _0_botCotizar;
var _0_botCargar;
var _0_botCargarPoliza;
var _0_botLimpiar;
var _0_fieldNtramite;
var _0_fieldNmpoliza;
var _0_selectedCdplan;
var _0_selectedDsplan;
var _0_selectedCdperpag;
var _0_selectedNmsituac;
var _0_gridCoberturas;
var _0_storeCoberturas;
var _0_windowCoberturas;
var _0_botDetalleCobertura;
var _0_windowAyudaCobertura;
var _0_selectedIdcobertura;
var _0_recordClienteRecuperado;
var _0_semaforoAux;
var sinTarificar;
var cdagenteCotiza;
var cduniecocotiza;
var _0_validacion_custom;

var _parentescoTitular = 'T';
// var rolesSuscriptores = '|SUSCRIAUTO|TECNISUSCRI|EMISUSCRI|JEFESUSCRI|GERENSUSCRI|SUBDIRSUSCRI|';
var plazoenanios;

debug('_0_smap1: ',_0_smap1);

var image = Ext.create('Ext.Img', {
	src: '../../images/confpantallas/icon/paneles.png',rowspan: 2
});

var _CONTEXT = "${ctx}";

//parche para RAMO 16 (FRONTERIZOS) con rol distinto de SUSCRIPTOR AUTO, se oculta el botón Detalle:
var ocultarDetalleCotizacion = false; 
if(_0_smap1.cdramo == Ramo.AutosFronterizos && 
   !RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol)
  )
{
    ocultarDetalleCotizacion = true;
}

var _0_rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
	clicksToEdit : 1,
	errorSummary : true,
	listeners: {
		beforeedit: function(){
			_0_botCotizar.disable();
		},
		edit: function(){
			_0_botCotizar.enable();
		},
		canceledit: function(){
			_0_botCotizar.enable();
		}
	}
});

var cargarXpoliza = false;
var cargaCotiza = false;
var _p28_panelDxnItems= [<s:property value="imap.panelDxnItems"  />];

//CARGO TODOS LOS VALORES QUE SUCURSAL, RAMO Y POLIZA GENERAN
var _0_panel7Items =
   [
       {
           layout  :
           {
              type    : 'table'
             ,columns : 1
             ,style   : 'width:10px !important;'
          }
          ,border : 0
          ,items  :
          [
           {
                    xtype       : 'numberfield'
                   ,itemId      : '_0_numsuc'
                   ,fieldLabel  : 'SUCURSAL'
                   ,name        : 'sucursal'               
                   ,sinOverride : true
                   ,labelWidth  : 170
                   ,style       : 'margin:5px;margin-left:15px;'//'margin:5px;margin-left:15px;width:20px !important;'
                   ,value       : !Ext.isEmpty(_0_smap1.RENUNIEXT) ? _0_smap1.RENUNIEXT : ''
                   ,listeners   :
                   {
                       change : _0_nmpolizaChange
                   }
                   ,readOnly    :  true 
               }
              ,{
                       xtype       : 'numberfield'
                      ,itemId      : '_0_numram'
                      ,fieldLabel  : 'RAMO'
                      ,name        : 'ramo'                   
                      ,sinOverride : true                   
                      ,labelWidth  : 170
                      ,style       : 'margin:5px;margin-left:15px;'//'width : 30px !important;'
                      ,value       : !Ext.isEmpty(_0_smap1.RENRAMO) ? _0_smap1.RENRAMO : ''
                      ,listeners   :
                      {
                          change : _0_nmpolizaChange
                      }
                      ,readOnly    :  true 
                 }
                ,{
                     xtype       : 'numberfield'
                    ,itemId      : '_0_numpol'
                    ,fieldLabel  : 'POLIZA'
                    ,name        : 'poliza'
                    ,sinOverride : true                 
                    ,labelWidth  : 170
                    ,style       : 'margin:5px;margin-left:15px;'//'width : 50px !important;'
                    ,value       : !Ext.isEmpty(_0_smap1.RENPOLIEX) ? _0_smap1.RENPOLIEX : ''
                    ,listeners   :
                    {
                        change : _0_nmpolizaChange
                    }
                    ,readOnly    :  true
              }
          ]
       }
    ];
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
function _0_cargarNumPasajerosAuto()
{
    Ext.Ajax.request(
    {
        url      : _0_urlCargarAutoPorClaveGS
        ,params  :
        {
            'smap1.cdramo'    : _0_smap1.cdramo
            ,'smap1.clavegs'  : _fieldByName('parametros.pv_otvalor22').getValue()
            ,'smap1.cdtipsit' : _0_smap1.cdtipsit
            ,'smap1.tipounidad' : _fieldByLabel('TIPO DE UNIDAD').getValue()
        }
        ,success : function(response)
        {
            var ijson=Ext.decode(response.responseText);
            debug('### obtener auto por clave gs:',ijson);
            if(ijson.exito)
            {
                _fieldByName('parametros.pv_otvalor06').setValue(ijson.smap1.NUMPASAJEROS);
                _fieldByName('parametros.pv_otvalor06').setMinValue(ijson.smap1.PASAJMIN);
                _fieldByName('parametros.pv_otvalor06').setMaxValue(ijson.smap1.PASAJMAX);
                _fieldByName('parametros.pv_otvalor06').isValid();
            }
            else
            {
                mensajeWarning(ijson.respuesta);
            }
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
}

function _0_obtenerClaveGSPorAuto()
{
    _fieldByName('parametros.pv_otvalor22').getStore().load(
    {
        params :
        {
            'params.substr' : _fieldByLabel('VERSION').getValue()
        }
        ,callback : function(records)
        {
            debug('callback records:',records);
            var valor=_fieldByLabel('VERSION').getValue()
                +' - '+_fieldByLabel('TIPO DE UNIDAD').findRecord('key',_fieldByLabel('TIPO DE UNIDAD').getValue()).get('value')
                +' - '+_fieldByLabel('MARCA').findRecord('key',_fieldByLabel('MARCA').getValue()).get('value')
                +' - '+_fieldByLabel('SUBMARCA').findRecord('key',_fieldByLabel('SUBMARCA').getValue()).get('value')
                +' - '+_fieldByLabel('MODELO').findRecord('key',_fieldByLabel('MODELO').getValue()).get('value')
                +' - '+_fieldByLabel('VERSION').findRecord('key',_fieldByLabel('VERSION').getValue()).get('value');
            debug('valor para el auto:',valor);
            _fieldByName('parametros.pv_otvalor22').setValue(
                _fieldByName('parametros.pv_otvalor22').findRecord('value',valor)
            );
            _0_cargarNumPasajerosAuto();
        }
    });
}

function _0_obtenerSumaAseguradaRamo6(mostrarError,respetarValue)
{
//     _0_panelPri.setLoading(true);
    var loading_0_obtenerSumaAseguradaRamo6 = _maskLocal();
    Ext.Ajax.request(
    {
        url      : _0_urlCargarSumaAsegurada
        ,params  :
        {
            'smap1.modelo'    : _fieldByName('parametros.pv_otvalor04').getValue()
                                     .substr(_fieldByName('parametros.pv_otvalor04').getValue().length-4,4)
            ,'smap1.version'  : _fieldByName('parametros.pv_otvalor05').getValue()
            ,'smap1.cdsisrol' : _0_smap1.cdsisrol
            ,'smap1.cdramo'   : _0_smap1.cdramo
            ,'smap1.cdtipsit' : _0_smap1.cdtipsit
        }
        ,success : function(response)
        {

//           _0_panelPri.setLoading(false); 
            loading_0_obtenerSumaAseguradaRamo6.close();
            var json=Ext.decode(response.responseText);
            debug('### json response obtener suma asegurada:',json);
            if(json.exito)
            {
                if(Ext.isEmpty(respetarValue)||respetarValue==false)
                {
                    _fieldByName('parametros.pv_otvalor25').setValue(json.smap1.SUMASEG);
                }
                else
                {
                    debug('SE RESPETA VALUE de VALOR COMERCIAL');
                }
                _fieldByName('parametros.pv_otvalor25').setMinValue((json.smap1.SUMASEG-0)*(1-(json.smap1.FACREDUC-0)));
                _fieldByName('parametros.pv_otvalor25').setMaxValue((json.smap1.SUMASEG-0)*(1+(json.smap1.FACINCREM-0)));
                _fieldByName('parametros.pv_otvalor25').isValid();
            }
            else if(mostrarError==true)
            {
                mensajeWarning(json.respuesta);
            }
        }
        ,failure : function()
        {
//             _0_panelPri.setLoading(false);
            loading_0_obtenerSumaAseguradaRamo6.close();
            errorComunicacion();
        }
    });
}

function _0_funcionFechaChange(field,value)
{
    try
    {
        if(_0_smap1.SITUACION=='AUTO' && _0_smap1.cdtipsit!='AT' && _0_smap1.cdtipsit!='MC'){
            
            if(_fieldByName('fefin').readOnly){
                return ;
            }
        }
        Ext.getCmp('fechaFinVigencia').setValue(Ext.Date.add(value,Ext.Date.YEAR,1));
    }
    catch (e) {}
}

function _0_comprar()
{
    debug('comprar');
    _0_panelPri.setLoading(true);
    var nombreTitular = '';
    
    Ext.Ajax.request(
    {
        url      : _0_urlComprar
        ,params  :
        {
            comprarNmpoliza        : _0_fieldNmpoliza.getValue()
            ,comprarCdplan         : _0_selectedCdplan
            ,comprarCdperpag       : _0_selectedCdperpag
            ,comprarCdramo         : _0_smap1.cdramo
            ,comprarCdciaaguradora : '20'
            ,comprarCdunieco       : _0_smap1.cdunieco
            ,cdtipsit              : _0_smap1.cdtipsit
            ,'smap1.fechaInicio'   : Ext.Date.format(Ext.getCmp('fechaInicioVigencia').getValue(),'d/m/Y')
            ,'smap1.fechaFin'      : Ext.Date.format(Ext.getCmp('fechaFinVigencia').getValue(),'d/m/Y')
            ,'smap1.ntramite'      : _0_smap1.ntramite
            ,'smap1.cdpersonCli'   : Ext.isEmpty(_0_recordClienteRecuperado) ? '' : _0_recordClienteRecuperado.raw.CLAVECLI
            ,'smap1.nmorddomCli'   : Ext.isEmpty(_0_recordClienteRecuperado) ? '' : _0_recordClienteRecuperado.raw.NMORDDOM
            ,'smap1.cdideperCli'   : Ext.isEmpty(_0_recordClienteRecuperado) ? '' : _0_recordClienteRecuperado.raw.CDIDEPER
            ,'smap1.cdagenteExt'   : (_0_smap1.cdramo == '6' || _0_smap1.cdramo == '16') ? _fieldByLabel('AGENTE').getValue() : ''
            ,'smap1.caseIdRstn'    : _NVL(_0_smap1.caseIdRstn)
        }
        ,success : function(response,opts)
        {
            _0_panelPri.setLoading(false);
            var json = Ext.decode(response.responseText);
            if (json.exito)
            {
                Ext.getCmp('_0_botComprarId').hide();
                Ext.getCmp('_0_botDetallesId').hide();
                Ext.getCmp('_0_botCoberturasId').hide();
                Ext.getCmp('_0_botEditarId').hide();
                Ext.getCmp('_0_botMailId').hide();
                Ext.getCmp('_0_botImprimirId').hide();
                //window.parent.scrollTo(0, 0);//ELIMINADA
                
                debug("mostrar documentos");
                
                var ntramite = json.comprarNmpoliza;
                debug("ntramite",ntramite);
                if (!(_0_smap1.ntramite&&_0_smap1.ntramite>0))
                {
                    centrarVentanaInterna(
                        Ext.create('Ext.window.Window',
                        {
                            width        : 600
                            ,height      : 400
                            ,title       : 'Subir documentos de tu tr&aacute;mite'
                            ,closable    : false
                            ,modal       : true
                            ,buttonAlign : 'center'
                            ,loadingMask : true
                            ,loader      :
                            {
                                url       : _0_urlVentanaDocumentos
                                ,scripts  : true
                                ,autoLoad : true
                                ,params   :
                                {
                                    'smap1.cdunieco'  : _0_smap1.cdunieco
                                    ,'smap1.cdramo'   : _0_smap1.cdramo
                                    ,'smap1.estado'   : 'W'
                                    ,'smap1.nmpoliza' : ''
                                    ,'smap1.nmsuplem' : '0'
                                    ,'smap1.ntramite' : json.smap1.ntramite
                                    ,'smap1.tipomov'  : '0'
                                    ,'smap1.nmsolici' : _0_fieldNmpoliza.getValue()
                                }
                            }
                            ,buttons     :
                            [
                                {
                                    text     : 'Aceptar'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                    ,handler : function()
                                    {
                                        this.up().up().destroy();
                                    }
                                }
                            ]
                        }).show()
                    );
                    if(_0_smap1.SITUACION=='AUTO')
                    {
                        debug("_0_smap1.SITUACION=='AUTO'");
                        if(Ext.isEmpty(_0_flujo)
                            ||Ext.isEmpty(_0_flujo.aux)
                            ||_0_flujo.aux.indexOf('onComprar')==-1
                        ) //si no hay flujo, o no hay auxiliar en flujo, o el auxiliar no contiene la palabra onComprar
                        {
                            var msg = Ext.Msg.show(
                            {
                                title    : 'Tr&aacute;mite actualizado'
                                ,msg     : 'La cotizaci&oacute;n se guard&oacute; para el tr&aacute;mite '
                                            + json.smap1.ntramite
                                            + '<br/>y no podr&aacute; ser modificada posteriormente'
                                ,buttons : Ext.Msg.OK
                                ,y       : 50
                                ,fn      : function()
                                {
                                    var paramsDatCom =
                                    {
                                        cdunieco         : _0_smap1.cdunieco
                                        ,cdramo          : _0_smap1.cdramo
                                        ,estado          : 'W'
                                        ,nmpoliza        : _0_fieldNmpoliza.getValue()
                                        ,'map1.ntramite' : json.smap1.ntramite
                                        ,cdtipsit        : _0_smap1.cdtipsit
                                    };
                                    
                                    if(!Ext.isEmpty(_0_flujo))
                                    {
                                        paramsDatCom['flujo.cdtipflu']  = _0_flujo.cdtipflu;
                                        paramsDatCom['flujo.cdflujomc'] = _0_flujo.cdflujomc;
                                        paramsDatCom['flujo.tipoent']   = _0_flujo.tipoent;  //ACTUAL QUE SE RECUPERARA
                                        paramsDatCom['flujo.claveent']  = _0_flujo.claveent; //ACTUAL QUE SE RECUPERARA
                                        paramsDatCom['flujo.webid']     = _0_flujo.webid;    //ACTUAL QUE SE RECUPERARA
                                        paramsDatCom['flujo.ntramite']  = _0_flujo.ntramite;
                                        paramsDatCom['flujo.status']    = _0_flujo.status;
                                        paramsDatCom['flujo.cdunieco']  = _0_flujo.cdunieco;
                                        paramsDatCom['flujo.cdramo']    = _0_flujo.cdramo;
                                        paramsDatCom['flujo.estado']    = _0_flujo.estado;
                                        paramsDatCom['flujo.nmpoliza']  = _0_flujo.nmpoliza;
                                        paramsDatCom['flujo.nmsituac']  = _0_flujo.nmsituac;
                                        paramsDatCom['flujo.nmsuplem']  = _0_flujo.nmsuplem;
                                        paramsDatCom['flujo.aux']       = 'RECUPERAR';
                                    }
                                
                                    Ext.create('Ext.form.Panel').submit(
                                    {
                                        url             : _0_urlDatosComplementarios
                                        ,standardSubmit : true
                                        ,params         : paramsDatCom
                                    });
                                }
                            });
                        }
                        else //flujo tiene la palabra onComprar
                        {
                            //si el flujo tiene este comodin ejecutaremos un turnado con el status indicado
                            var ck = 'Turnando tr\u00e1mite';
                            try
                            {
                                var status = _0_flujo.aux.split('_')[1];
                                debug('status para turnar onComprar:',status,'.');
                                
                                _mask(ck);
                                Ext.Ajax.request(
                                {
                                    url      : _GLOBAL_COMP_URL_TURNAR
                                    ,params  :
                                    {
                                        'params.CDTIPFLU'   : _0_flujo.cdtipflu
                                        ,'params.CDFLUJOMC' : _0_flujo.cdflujomc
                                        ,'params.NTRAMITE'  : _0_flujo.ntramite
                                        ,'params.STATUSOLD' : _0_flujo.status
                                        ,'params.STATUSNEW' : status
                                        ,'params.COMMENTS'  : 'Tr\u00e1mite cotizado'
                                        ,'params.SWAGENTE'  : 'S'
                                    }
                                    ,success : function(response)
                                    {
                                        _unmask();
                                        var ck = '';
                                        try
                                        {
                                            var json = Ext.decode(response.responseText);
                                            debug('### turnar:',json);
                                            if(json.success)
                                            {
                                                mensajeCorrecto
                                                (
                                                    'Tr\u00e1mite turnado'
                                                    //,json.message
                                                    ,'El tr\u00e1mite fue turnado para aprobaci\u00f3n del agente/promotor'
                                                    ,function()
                                                    {
                                                        _mask('Redireccionando');
                                                        Ext.create('Ext.form.Panel').submit(
                                                        {
                                                            url             : _GLOBAL_COMP_URL_MCFLUJO
                                                            ,standardSubmit : true
                                                        });
                                                    }
                                                );
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
                                        errorComunicacion(null,'Error al turnar tr\u00e1mite');
                                    }
                                });
                            }
                            catch(e)
                            {
                                manejaException(e,ck);
                            }
                        }
                    }
                    else
                    {
                        if (_0_smap1.rstn === 'S') {
                            mensajeCorrecto(
                                'Tr\u00e1mite generado',
                                'Se ha generado el tr\u00e1mite ' + json.smap1.ntramite,
                                function () {
                                    _mask();
                                    location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?ntramite=' + json.smap1.ntramite;
                                }
                            );
                            return;
                        }
                        var callbackNormal = function (callback) {
                            mensajeCorrecto(
                                'Tr\u00e1mite generado',
                                'Se ha generado el tr\u00e1mite ' + json.smap1.ntramite +
                                    ', favor de revisar los requisitos y subir sus documentos antes de turnar a SUSCRIPCI\u00d3N',
                                callback
                            );
                        };
                        var mask, ck = 'Recuperando lista de requisitos';
                        try {
                            var ntramite = json.smap1.ntramite;
                            ck = 'Recuperando validaci\u00f3n ligada a requisitos';
                            mask = _maskLocal(ck);
                            Ext.Ajax.request({
                                url     : _GLOBAL_URL_RECUPERACION,
                                params  : {
                                    'params.consulta' : 'RECUPERAR_VALIDACION_POR_CDVALIDAFK',
                                    'params.ntramite' : json.smap1.ntramite,
                                    'params.clave'    : '_CONFCOT'
                                },
                                success : function (response) {
                                    mask.close();
                                    var ck = 'Decodificando respuesta al recuperar validaci\u00f3n ligada a requisitos';
                                    try {
                                        var valida = Ext.decode(response.responseText);
                                        debug('### validacion ligada a checklist:', valida);
                                        if (valida.success === true) {
                                            if (valida.list.length > 0) {
                                                _cargarAccionesEntidad(
                                                    valida.list[0].CDTIPFLU,
                                                    valida.list[0].CDFLUJOMC,
                                                    valida.list[0].TIPOENT,
                                                    valida.list[0].CDENTIDAD,
                                                    valida.list[0].WEBID,
                                                    function (acciones) {
                                                        if (acciones.length > 0) {
                                                            debug('acciones:', acciones);
                                                            callbackNormal(function () {
                                                                _procesaAccion(
                                                                    acciones[0].CDTIPFLU,
                                                                    acciones[0].CDFLUJOMC,
                                                                    acciones[0].TIPODEST,
                                                                    acciones[0].CLAVEDEST,
                                                                    acciones[0].WEBIDDEST,
                                                                    acciones[0].AUX,
                                                                    valida.params.ntramite,
                                                                    valida.list[0].STATUS,
                                                                    null, //cdunieco
                                                                    null, //cdramo
                                                                    null, //estado
                                                                    null, //nmpoliza
                                                                    null, //nmsituac
                                                                    null, //nmsuplem
                                                                    valida.list[0].cdusuari,
                                                                    valida.list[0].cdsisrol,
                                                                    null // callback
                                                                );
                                                            });
                                                        } else {
                                                            callbackNormal();
                                                        }
                                                    }
                                                );
                                            } else {
                                                callbackNormal();
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
                                    errorComunicacion(null, 'Error al recuperar validaci\u00f3n ligada a requisitos');
                                }
                            });
                        } catch (e) {
                            manejaException(e, ck, mask);
                            callbackNormal();
                        }
                    }
                }
                else
                {
                    if(Ext.isEmpty(_0_flujo)
                        ||Ext.isEmpty(_0_flujo.aux)
                        ||_0_flujo.aux.indexOf('onComprar')==-1
                    ) //si no hay flujo, o no hay auxiliar en flujo, o el auxiliar no contiene la palabra onComprar
                    {
                        var msg = Ext.Msg.show(
                        {
                            title    : 'Tr&aacute;mite actualizado'
                            ,msg     : 'La cotizaci&oacute;n se guard&oacute; para el tr&aacute;mite '
                                        + _0_smap1.ntramite
                                        + '<br/>y no podr&aacute; ser modificada posteriormente'
                            ,buttons : Ext.Msg.OK
                            ,y       : 50
                            ,fn      : function()
                            {
                                var paramsDatCom =
                                {
                                    cdunieco         : _0_smap1.cdunieco
                                    ,cdramo          : _0_smap1.cdramo
                                    ,estado          : 'W'
                                    ,nmpoliza        : _0_fieldNmpoliza.getValue()
                                    ,'map1.ntramite' : _0_smap1.ntramite
                                    ,cdtipsit        : _0_smap1.cdtipsit
                                };
                                
                                if(!Ext.isEmpty(_0_flujo))
                                {
                                    paramsDatCom['flujo.cdtipflu']  = _0_flujo.cdtipflu;
                                    paramsDatCom['flujo.cdflujomc'] = _0_flujo.cdflujomc;
                                    paramsDatCom['flujo.tipoent']   = _0_flujo.tipoent;  //ACTUAL QUE SE RECUPERARA
                                    paramsDatCom['flujo.claveent']  = _0_flujo.claveent; //ACTUAL QUE SE RECUPERARA
                                    paramsDatCom['flujo.webid']     = _0_flujo.webid;    //ACTUAL QUE SE RECUPERARA
                                    paramsDatCom['flujo.ntramite']  = _0_flujo.ntramite;
                                    paramsDatCom['flujo.status']    = _0_flujo.status;
                                    paramsDatCom['flujo.cdunieco']  = _0_flujo.cdunieco;
                                    paramsDatCom['flujo.cdramo']    = _0_flujo.cdramo;
                                    paramsDatCom['flujo.estado']    = _0_flujo.estado;
                                    paramsDatCom['flujo.nmpoliza']  = _0_flujo.nmpoliza;
                                    paramsDatCom['flujo.nmsituac']  = _0_flujo.nmsituac;
                                    paramsDatCom['flujo.nmsuplem']  = _0_flujo.nmsuplem;
                                    paramsDatCom['flujo.aux']       = 'RECUPERAR';
                                }
                                
                                Ext.create('Ext.form.Panel').submit(
                                {
                                    url             : _0_urlDatosComplementarios
                                    ,standardSubmit : true
                                    ,params         : paramsDatCom
                                });
                            }
                        });
                        msg.setY(50);
                    }
                    else //flujo tiene la palabra onComprar
                    {
                        //si el flujo tiene este comodin ejecutaremos un turnado con el status indicado
                        var ck = 'Turnando tr\u00e1mite';
                        try
                        {
                            var status = _0_flujo.aux.split('_')[1];
                            debug('status para turnar onComprar:',status,'.');
                            
                            _mask(ck);
                            Ext.Ajax.request(
                            {
                                url      : _GLOBAL_COMP_URL_TURNAR
                                ,params  :
                                {
                                    'params.CDTIPFLU'   : _0_flujo.cdtipflu
                                    ,'params.CDFLUJOMC' : _0_flujo.cdflujomc
                                    ,'params.NTRAMITE'  : _0_flujo.ntramite
                                    ,'params.STATUSOLD' : _0_flujo.status
                                    ,'params.STATUSNEW' : status
                                    ,'params.COMMENTS'  : 'Tr\u00e1mite cotizado'
                                    ,'params.SWAGENTE'  : 'S'
                                }
                                ,success : function(response)
                                {
                                    _unmask();
                                    var ck = '';
                                    try
                                    {
                                        var json = Ext.decode(response.responseText);
                                        debug('### turnar:',json);
                                        if(json.success)
                                        {
                                            mensajeCorrecto
                                            (
                                                'Tr\u00e1mite turnado'
                                                ,json.message
                                                ,function()
                                                {
                                                    _mask('Redireccionando');
                                                    Ext.create('Ext.form.Panel').submit(
                                                    {
                                                        url             : _GLOBAL_COMP_URL_MCFLUJO
                                                        ,standardSubmit : true
                                                    });
                                                }
                                            );
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
                                    errorComunicacion(null,'Error al turnar tr\u00e1mite');
                                }
                            });
                        }
                        catch(e)
                        {
                            manejaException(e,ck);
                        }
                    }
                }
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _0_panelPri.setLoading(false);
            errorComunicacion();
        }
    });
}

function _0_imprimir()
{
    var me = this;
    var urlRequestImpCotiza = _0_urlImprimirCotiza
            + '?p_unieco='      + _0_smap1.cdunieco
            + '&p_ramo='        + _0_smap1.cdramo
            + '&p_subramo='     + _0_smap1.cdtipsit
            + '&p_estado=W'
            + '&p_poliza='      + _0_fieldNmpoliza.getValue()
            + '&p_suplem=0'
            + '&p_cdplan='      + _0_selectedCdplan
            + '&p_plan='        + _0_selectedCdplan
            + '&p_perpag='      + _0_selectedCdperpag
            + '&p_ntramite='    + _0_smap1.ntramite
            + '&p_cdusuari='    + _0_smap1.cdusuari
            + '&destype=cache'
            + "&desformat=PDF"
            + "&userid="        + _0_reportsServerUser
            + "&ACCESSIBLE=YES"
            + "&report="        + _0_reporteCotizacion
            + "&paramform=no";
    debug(urlRequestImpCotiza);
    var numRand = Math.floor((Math.random() * 100000) + 1);
    debug(numRand);
    var windowVerDocu = Ext.create('Ext.window.Window',
    {
        title          : 'Cotizaci&oacute;n'
        ,width         : 700
        ,height        : 500
        ,collapsible   : true
        ,titleCollapse : true
        ,html : '<iframe innerframe="'
                + numRand
                + '" frameborder="0" width="100" height="100"'
                + 'src="'
                + _0_urlViewDoc
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
}

function _0_mail()
{
    Ext.create('Ext.window.Window',
    {
        title : 'Enviar cotizaci&oacute;n'
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
                ,icon : '${ctx}/resources/fam3icons/icons/accept.png'
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
                                urlArchivo : _0_urlImprimirCotiza
                                             + '?p_unieco='      + _0_smap1.cdunieco
                                             + '&p_ramo='        + _0_smap1.cdramo
                                             + '&p_subramo='     + _0_smap1.cdtipsit
                                             + '&p_estado=W'
                                             + '&p_poliza='      + _0_fieldNmpoliza.getValue()
                                             + '&p_suplem=0'
                                             + '&p_cdplan='      + _0_selectedCdplan
                                             + '&p_plan='        + _0_selectedCdplan
                                             + '&p_perpag='      + _0_selectedCdperpag
                                             + '&p_ntramite='    + _0_smap1.ntramite
                                             + '&p_cdusuari='    + _0_smap1.cdusuari
                                             + '&destype=cache'
                                             + "&desformat=PDF"
                                             + "&userid="        + _0_reportsServerUser
                                             + "&ACCESSIBLE=YES"
                                             + "&report="        + _0_reporteCotizacion
                                             + "&paramform=no",
                                nombreArchivo : 'cotizacion_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf'
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
                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                ,handler : function()
                {
                    this.up().up().destroy();
                }
            }
        ]
    }).show();
    Ext.getCmp('_0_idInputCorreos').focus();
}

function _0_bloquear(b)
{
    for(var i=2;i<_0_formAgrupados.items.items.length-1;i++)
    {
        _0_formAgrupados.items.items[i].setReadOnly(b);
    }
    _0_gridIncisos.setDisabled(b);
    _0_botonera.setDisabled(b);
    if(b)
    {
        //window.parent.scrollTo(0, _0_formAgrupados.getHeight()+_0_gridIncisos.getHeight());//ELIMINADA
        // Se aplica el focus en algun boton habilitado del grid de tarifas:
        try {
           _0_gridTarifas.down('button[disabled=false]').focus(false, 1000);
        } catch(e) {
            debug(e);
        }
    } else {
        // Se aplica el focus en el numero de poliza:
        try {
            _0_fieldNmpoliza.focus();
        } catch(e) {
            debug(e);
        }
    }
}

function _0_detallesCobertura()
{
    _0_windowCoberturas.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _0_urlDetalleCobertura
        ,params  :
        {
            idCobertura       : _0_selectedIdcobertura
            ,idCiaAseguradora : '20'
            ,idRamo           : _0_smap1.cdramo
        }
        ,success : function(response)
        {
            _0_windowCoberturas.setLoading(false);
            var jsonResp = Ext.decode(response.responseText);
            if (jsonResp.ayudaCobertura
                    && jsonResp.ayudaCobertura.dsGarant
                    && jsonResp.ayudaCobertura.dsGarant.length > 0
                    && jsonResp.ayudaCobertura.dsAyuda
                    && jsonResp.ayudaCobertura.dsAyuda.length > 0) {
                _0_windowAyudaCobertura.html = '<table width=430 ><tr><td align=left bgcolor="#98012e" style="color:white;font-size:11px;"><b>'
                        + jsonResp.ayudaCobertura.dsGarant
                        + '</b></td></tr><tr><td style="font-size:11px; ">'
                        + jsonResp.ayudaCobertura.dsAyuda
                        + '</td></tr></table>';
                _0_windowAyudaCobertura.show();
            }
            else
            {
                mensajeWarning('No hay detalle de cobertura');
            }
        }
        ,failure : function()
        {
            _0_windowCoberturas.setLoading(false);
            mensajeError('Error al obtener detalle');
        }
    });
}
function _0_coberturas()
{
    _0_storeCoberturas.load(
    {
        params :
        {
            jsonCober_unieco   : _0_smap1.cdunieco
            ,jsonCober_estado  : 'W'
            ,jsonCober_nmpoiza : _0_fieldNmpoliza.getValue()
            ,jsonCober_cdplan  : _0_selectedCdplan
            ,jsonCober_cdramo  : _0_smap1.cdramo
            ,jsonCober_cdcia   : '20'
            ,jsonCober_situa   : _0_selectedNmsituac
        }
    });
    _0_gridCoberturas.setTitle('Plan ' + _0_selectedDsplan);
    _0_botDetalleCobertura.setDisabled(true);
    _0_windowCoberturas.show();
}

function _0_detalles()
{
    Ext.Ajax.request(
    {
        url      : _0_urlDetalleCotizacion
        ,params  :
        {
            'panel1.pv_cdunieco_i'  : _0_smap1.cdunieco
            ,'panel1.pv_cdramo_i'   : _0_smap1.cdramo
            ,'panel1.pv_estado_i'   : 'W'
            ,'panel1.pv_nmpoliza_i' : _0_fieldNmpoliza.getValue()
            ,'panel1.pv_cdperpag_i' : _0_selectedCdperpag
            ,'panel1.pv_cdplan_i'   : _0_selectedCdplan
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug(json);
            if (json.success == true)
            {
                var orden = 0;
                var parentescoAnterior = 'werty';
                for ( var i = 0; i < json.slist1.length; i++)
                {
                    if (json.slist1[i].parentesco != parentescoAnterior)
                    {
                        orden++;
                        parentescoAnterior = json.slist1[i].parentesco;
                    }
                    json.slist1[i].orden_parentesco = orden+ '_'+ json.slist1[i].parentesco;
                }
                debug(json);
                var wndDetalleCotizacion = Ext.create('Ext.window.Window',
                {
                    title       : 'Detalles de cotizaci&oacute;n'
                    //,maxHeight  : 500
                    ,width      : 600
                    ,autoScroll : true
                    ,modal      : true
                    ,items      :
                    [
                        Ext.create('Ext.grid.Panel',
                        {
                            store    : Ext.create('Ext.data.Store',
                            {
                                model       : 'ModeloDetalleCotizacion'
                                ,groupField : 'orden_parentesco'
                                ,sorters    :
                                [
                                    {
                                        sorterFn : function(o1,o2)
                                        {
                                            debug('sorting:',o1,o2);
                                            if (o1.get('orden') === o2.get('orden'))
                                            {
                                                return 0;
                                            }
                                            return o1.get('orden')-0 < o2.get('orden')-0 ? -1 : 1;
                                        }
                                    }
                                ]
                                ,proxy      :
                                {
                                    type    : 'memory'
                                    ,reader : 'json'
                                }
                                ,data : json.slist1
                            })
                            ,columns :
                            [
                                {
                                    header           : 'Nombre de la cobertura'
                                    ,dataIndex       : 'Nombre_garantia'
                                    ,flex            : 3
                                    ,summaryType     : 'count'
                                    ,summaryRenderer : function(value)
                                    {
                                        return Ext.String.format('Total de {0} cobertura{1}',value,value !== 1 ? 's': '');
                                    }
                                }
                                ,{
                                    header       : 'Importe por cobertura'
                                    ,dataIndex   : 'Importe'
                                    ,flex        : 1
                                    ,renderer    : Ext.util.Format.usMoney
                                    ,align       : 'right'
                                    ,summaryType : 'sum'
                                } 
                            ]
                            ,features :
                            [
                                {
                                    groupHeaderTpl :
                                    [
                                        '{name:this.formatName}'
                                        ,{
                                            formatName : function(name)
                                            {
                                                return name.split("_")[1];
                                            }
                                        }
                                    ]
                                ,ftype          : 'groupingsummary'
                                ,startCollapsed : true
                                }
                            ]
                        })
                        ,Ext.create('Ext.toolbar.Toolbar',
                        {
                            buttonAlign : 'right'
                            ,items      :
                            [
                                '->'
                                ,Ext.create('Ext.form.Label',
                                {
                                    style          : 'color:white;'
                                    ,initComponent : function()
                                    {
                                        var sum = 0;
                                        for ( var i = 0; i < json.slist1.length; i++)
                                        {
                                            sum += parseFloat(json.slist1[i].Importe);
                                        }
                                        this.setText('Total: '+ Ext.util.Format.usMoney(sum));
                                        this.callParent();
                                    }
                                })
                            ]
                        })
                    ]
                }).show();
                centrarVentanaInterna(wndDetalleCotizacion);
            }
            else
            {
                mensajeError('Error al obtener detalle');
            }
        }
        ,failure : errorComunicacion
    });
}

function _0_nueva()
{
    _0_formAgrupados.getForm().reset();
    _0_fieldNmpoliza.setValue('');
    _0_storeIncisos.removeAll();
    _0_panelPri.remove(_0_gridTarifas);
    _0_panelPri.doLayout();
    _0_bloquear(false);
    if(_0_formAgrupados.items.items[2])
    {
        _0_formAgrupados.items.items[2].focus();
    }
    else if(_0_formAgrupados.items.items[0].items.items[2])
    {
        _0_formAgrupados.items.items[0].items.items[2].focus();
    }
}

function _0_clonar()
{
    _0_panelPri.remove(_0_gridTarifas);
    _0_panelPri.doLayout();
    _0_fieldNmpoliza.setValue('');
    _0_bloquear(false);
    if(_0_formAgrupados.items.items[2])
    {
        _0_formAgrupados.items.items[2].focus();
    }
    else if(_0_formAgrupados.items.items[0].items.items[2])
    {
        _0_formAgrupados.items.items[0].items.items[2].focus();
    }
}

function _0_editar()
{
    _0_panelPri.remove(_0_gridTarifas);
    _0_panelPri.doLayout();
    _0_bloquear(false);
    if(_0_formAgrupados.items.items[2])
    {
        _0_formAgrupados.items.items[2].focus();
    }
    else if(_0_formAgrupados.items.items[0].items.items[2])
    {
        _0_formAgrupados.items.items[0].items.items[2].focus();
    }
}

function _0_limpiar()
{
    _0_formAgrupados.getForm().reset();
    _0_storeIncisos.removeAll();
    if(_0_formAgrupados.items.items[2])
    {
        _0_formAgrupados.items.items[2].focus();
    }
    else if(_0_formAgrupados.items.items[0].items.items[2])
    {
        _0_formAgrupados.items.items[0].items.items[2].focus();
    }
}

function _0_cargar()
{
    debug('>_0_cargar');
    Ext.Msg.prompt(
    'Cargar cotizaci&oacute;n',
    'N&uacute;mero de cotizaci&oacute;n:',
    function (buttonId, value)
    {
        debug('nmpoliza',value);
        var valido=true;
        //boton pulsado y valor capturado
        if(valido)
        {
            valido = buttonId=='ok'&&(value+'').length>0;
        }
        //valor numerico
        if(valido)
        {
            valido = !isNaN(value);
            if(!valido)
            {
                mensajeWarning('Introduce un n&uacute;mero v&aacute;lido');
            }
        }
        //request
        if(valido)
        {
            _0_recuperarCotizacion(value);
        }
    });
    debug('<_mcotiza_load');
    debug('<_0_cargar');
}

// Cargar Poliza y dtos del cliente
function _0_recuperarCotizacion(nmpoliza)
{
    debug('>_0_recuperarCotizacion nmpoliza=',nmpoliza,'.');
    _0_panelPri.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _0_urlLoad
        ,params  :
        {
            'smap1.nmpoliza'    : nmpoliza
            ,'smap1.cdramo'     : _0_smap1.cdramo
            ,'smap1.cdunieco'   : _0_smap1.cdunieco
            ,'smap1.cdtipsit'   : _0_smap1.cdtipsit
            ,'smap1.ntramiteIn' : _NVL(_0_smap1.ntramite)
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            cargarXpoliza = true;
            
            if(_0_smap1.cdramo=='6' || _0_smap1.cdramo=='16')
            {   
                if(!Ext.isEmpty(json.error))
                {
                	mensajeError(json.error); 
                	_0_panelPri.setLoading(false);
                }
                else
                {
                	var primerInciso = new _0_modeloAgrupado(json.slist1[0]);

                	if(!Ext.isEmpty(primerInciso.raw.CLAVECLI))
                	{
                		    _0_recordClienteRecuperado = primerInciso;
                		    debug('_0_recordClienteRecuperado:',_0_recordClienteRecuperado);
                	}
                    llenandoCampos(json);
                }
                
            }

            else
            {
                _0_panelPri.setLoading(false);
                errorComunicacion(null,'Error al validar el ramo, solo fronterizos');
            }
            
        }
        ,failure : function()
        {
            _0_panelPri.setLoading(false);
            errorComunicacion();
        }
    });
}

function _0_cargarPoliza()
{   cargarXpoliza = true;
    Ext.create('Ext.window.Window',{
        title        : 'Cargar por Sucursal, Ramo Y Poliza'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 450
        ,resizable   : false
        ,height      : 160
        ,items       :[
             Ext.create('Ext.form.Panel',
            {
               items    :
               [
                    {
                      xtype      : 'textfield'
                     ,itemId     : 'sucursal'
                     ,fieldLabel : 'Sucural'
                    }
                    ,{
                        xtype      : 'textfield'
                       ,itemId     : 'ramo'
                       ,fieldLabel : 'Ramo'
                    }
                    ,{
                        xtype      : 'textfield'
                       ,itemId     : 'poliza'
                       ,fieldLabel : 'Poliza'
                    }
                ]
            })
        ],
        buttonAlign:'center',
        buttons: 
        [
            {
                text: 'Aceptar',
                icon: '${ctx}/resources/fam3icons/icons/accept.png',
                buttonAlign : 'center',
                handler: function(button) {
                    
                    var ventana = button.up('window');
                    var form = button.up('window').down('form');
                    debug('form.getValue():',form.getValues());
                    var valores= form.getValues();
//                  var valores = Ext.decode(cadena);
                    debug('Valores:',valores);
                    debug('Sucursal: ', valores['textfield-1059-inputEl']);
                    var sucursal= valores['textfield-1059-inputEl'];
                    debug('Ramo: ', valores['textfield-1060-inputEl']);
                    var ramo = valores['textfield-1060-inputEl'];
                    debug('Poliza: ', valores['textfield-1061-inputEl']);
                    var poliza = valores['textfield-1061-inputEl'];
                                        
                    ventana.close();
                    _0_panelPri.setLoading(true);
                    
                    Ext.Ajax.request(
                            {
                                url      : _p0_urlCargarPoliza
                                 ,params  :
                                 {
                                      'smap1.cdsucursal' : sucursal
                                     ,'smap1.cdramo' : ramo
                                     ,'smap1.cdpoliza' : poliza
                                     ,'smap1.cdusuari' : _0_smap1.cdusuari,
                                 }
                                 ,success : function(response)
                                 {
                                  _0_panelPri.setLoading(false);
                                  var json=Ext.decode(response.responseText);
                                  debug("valoresCampos: ",json);
                                  var json2=Ext.decode(json.smap1.valoresCampos);
                                  json2['success']=true;
                                  cdper     = json2.smap1.cdper;   //D00000000111005
                                  cdperson  = json2.smap1.cdperson;//530400
                                  debug("valoresCampos 2: ",json2);
                                  llenandoCampos(json2);
                                 }
                                ,failure : function()
                                {
                                 _0_panelPri.setLoading(false);
                                 errorComunicacion();
                                }
                            });
                }
            }
        ]
    }).show();
}

function llenandoCampos (json)
{
//   var panelpri = _fieldById('_0_panelpri');
     var nmpoliza = _fieldByName('nmpoliza').getValue();
     debug('### cargar cotizacion:',json);
    
     if(json.success)
     {
         var maestra     =  json.slist1[0].ESTADO=='M';
         var fesolici    =  Ext.Date.parse(json.smap1.FESOLICI,'d/m/Y');
         var fechaHoy    =  Ext.Date.clearTime(new Date());
         var fechaLimite =  Ext.Date.add(fechaHoy,Ext.Date.DAY,-1*(json.smap1.diasValidos-0));
         var vencida     =  fesolici<fechaLimite;
         debug('fesolici='    , fesolici);
         debug('fechaHoy='    , fechaHoy);
         debug('fechaLimite=' , fechaLimite);
         debug('vencida='     , vencida, '.');
    
         _0_limpiar();
    
         var iniVig = Ext.Date.parse(json.smap1.FEEFECTO,'d/m/Y').getTime();
         var finVig = Ext.Date.parse(json.smap1.FEPROREN,'d/m/Y').getTime();
         var milDif = finVig-iniVig;
         var diaDif = milDif/(1000*60*60*24);
         debug('diaDif:',diaDif);
       
         if(diaDif<0)
         {
               diaDif = diaDif*-1;
         }
         
         debug('diaDif:',diaDif);
         
         if(!Ext.isEmpty(json.slist1[0]['feini']))
         {
             _fieldByName('feini').setValue(Ext.Date.parse(json.slist1[0]['feini'],'d/m/Y'));
         }
         else
         {
             _fieldByName('feini').setValue(new Date());
         }
         
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
             _fieldByName('nmpoliza').setValue('');
             mensajeWarning('Se va a duplicar la p&oacute;liza emitida '+json.slist1[0].NMPOLIZA);
         }
         else if(vencida)
         {
             _fieldByName('nmpoliza').setValue('');
             mensajeWarning('La cotizaci&oacute;n ha vencido y solo puede duplicarse');
         }
         else
         {
             _fieldByName('nmpoliza').semaforo=true;
             _fieldByName('nmpoliza').setValue(nmpoliza);
             _fieldByName('nmpoliza').semaforo=false;
         }
         
         var primerInciso = new _0_modeloAgrupado(json.slist1[0]);
         
        _0_panelPri.setLoading(false);
        debug('json response:',json);
 //<< ----------------------------------------------------------------------------     
        if(cargarXpoliza || Ext.isEmpty(json.smap1.NTRAMITE))
        {
            for(var i=0;i<json.slist1.length;i++)
            {
                if(_0_smap1.SITUACION=='AUTO')
                {
                    debug("_0_smap1.SITUACION=='AUTO'");
                    if(json.slist1[i].nombre&&json.slist1[i].nombre+'x'!='x')
                    {
                        _0_storeIncisos.add(new _0_modelo(json.slist1[i]));
                    }
                }
                else
                {
                    _0_storeIncisos.add(new _0_modelo(json.slist1[i]));
                }
            }
            debug('store:',_0_storeIncisos);
            var primerInciso = new _0_modeloAgrupado(json.slist1[0]);
            primerInciso.set('FESOLICI',json.smap1.FESOLICI);
            
            var combcl = 'S';
            if(_0_smap1.cdramo=='6' || !Ext.isEmpty(_fieldLikeLabel('CLIENTE NUEVO',null,true)))
            {
                combcl = _fieldLikeLabel('CLIENTE NUEVO');
                if(!Ext.isEmpty(json.slist1[0].OTVALOR24))
                    {
                      if(json.slist1[0].OTVALOR24==='N')
                          {
                              combcl.semaforo = true;
                              combcl.setValue('N');
                              combcl.semaforo = false;
                          }
                      else
                          {
                              combcl.semaforo = true;
                              combcl.setValue('S');
                              combcl.semaforo = false;
                          }
                    }
            debug('primerInciso:',primerInciso);
            }

            //leer elementos anidados
            var form      = _0_formAgrupados;
            var formItems = form.items.items;
            var hayDerechos = false;
            <s:if test='%{getSmap1().get("CDATRIBU_DERECHO")!=null}'>
                hayDerechos = true;
                formItems = form.items.items[0].items.items;
            </s:if>
            debug('hayDerechos:' , hayDerechos);
            debug('formItems:'   , formItems);
            var numBlurs  = 0;
            for(var i=0;i<formItems.length;i++)
            {
                var item=formItems[i];
                if(item.hasListener('blur'))
                {
                    var numBlursSeguidos = 1;
                    debug('contando blur:',item);
                    for(var j=i+1;j<formItems.length;j++)
                    {
                        if(formItems[j].hasListener('blur'))
                        {
                            numBlursSeguidos=numBlursSeguidos+1;
                        }
                    }
                    if(numBlursSeguidos>numBlurs)
                    {
                        numBlurs=numBlursSeguidos;
                    }
                }
            }
            debug('numBlurs:',numBlurs);
            var i=0;
            var renderiza=function()
            {
                debug('renderiza',i);
                form.loadRecord(primerInciso);
                if(i<numBlurs)
                {
                    i=i+1;
                    for(var j=0;j<formItems.length;j++)
                    {
                        var iItem  = formItems[j]; 
                        var iItem2 = formItems[j+1];
                        debug('iItem2:',iItem2,'store:',iItem2?iItem2.store:'iItem2 no');
                        if(iItem.hasListener('blur')&&iItem2&&iItem2.store&&iItem2.heredar)
                        {
                            debug('tiene blur y lo hacemos heredar',formItems[j]);
                            iItem2.heredar(true);
                        }
                    }
                    setTimeout(renderiza,1000);
                }
                else
                {
                    if(!cargarXpoliza)
                    {
	               	    if(maestra)//SE DEJA EN BLANCO CUANDO "M" PARA GENERAR NUEVO NUMERO 
	               		{
	               		    _fieldByName('nmpoliza').setValue('');
	               		    _fieldByName('FESOLICI').setValue(new Date());
	               		}
	               	    else
	               	    {
	               	        _0_fieldNmpoliza.setValue(json.smap1.nmpoliza);
               		    }
                    }
                    
                    _0_panelPri.setLoading(false);
                    
                        if(_0_smap1.cdramo=='6')
                        {
                            if(_0_smap1.cdtipsit=='MC')
                            {
                                 asignarAgente(primerInciso.get('parametros.pv_otvalor17'));    
                            }
                            if(_0_smap1.cdtipsit=='AT')
                            {
                                _0_obtenerClaveGSPorAuto();
                                _0_obtenerSumaAseguradaRamo6(true,true);
                            }
                            if(_fieldByLabel('FOLIO').getValue()==0)
                            {
                                _fieldByLabel('FOLIO').reset();
                                asignarAgente(primerInciso.get('parametros.pv_otvalor17')); 
                            }
                        }
                        
                        if(_0_smap1.cdtipsit == 'AF' || _0_smap1.cdtipsit == 'PU') {
                            
                            
                            if(json.slist1[0].OTVALOR02 == '1')
                            {
                                var me  =_fieldLikeLabel('TIPO VALOR');
                                var record = me.findRecordByValue('1');
                                if(!record)
                                    {
	                                _fieldLikeLabel('TIPO VALOR').store.add({key:'1',value:'Valor Convenido'});
	                                _fieldLikeLabel('TIPO VALOR').setValue('1');
                                    }
                            }
//                         	if(rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')!=-1)
                        	if(RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol))
                            {
                        		cdagenteCotiza = primerInciso.get('parametros.pv_otvalor32');
                        		cduniecocotiza = primerInciso.raw.CDUNIECO;
                        	}
                        	
                            asignarAgente(primerInciso.get('parametros.pv_otvalor32'));
                            _0_recuperarDescuento();
                        }
                        
                        if(_0_smap1.cdtipsit=='GMI')
                        {
                            _0_gmiPostalSelect(1,2,3,true);
                            _0_gmiCirchospSelect(1,2,3,true);
                        }
                        
                        if('|AF|PU|AT|MC|'.lastIndexOf('|'+_0_smap1.cdtipsit+'|')!=-1)
                        {
                            _0_panelPri.setLoading(true);
                            cargaCotiza = true;
                            sinTarificar = !maestra&&!vencida ;
                            
                            if (cargarXpoliza === true) 
                            { // Cuando es una renovacion importada de SIGS
                                sinTarificar = false;
                                
                                if(_0_smap1.cdramo=='6' || _0_smap1.cdramo=='16' || true)
                                {   
                                           if(!Ext.isEmpty(primerInciso.raw.CLAVECLI))
                                        {
                                                _0_recordClienteRecuperado = primerInciso;
                                                debug('_0_recordClienteRecuperado:',_0_recordClienteRecuperado);
                                        }
                                    
                                }
                            
                                var fesoliciCmp = _fieldByName('FESOLICI', null, true);
                                if (!Ext.isEmpty(fesoliciCmp)
                                    && Ext.isEmpty(fesoliciCmp.getSubmitValue())
                                ) {
                                    fesoliciCmp.setValue(new Date());
                                }
                            }
                            
                            _0_cotizar();
                        }
                        
                        try {
                        	
                        	
                           	_fieldByName('aux.otvalor08').setValue(json.smap1['aux.otvalor08']);
                           	_fieldByName('aux.otvalor09').heredar(true, function(){
                     			_fieldByName('aux.otvalor09').setValue(json.smap1['aux.otvalor09']);
                           	});
                        } catch(e) {
                        	debugError(e);
                        }
                }
            };
            _0_panelPri.setLoading(true);
            renderiza();
        }
        else
        {
            var paramsDatCom =
            {
                cdunieco         : json.smap1.CDUNIECO
                ,cdramo          : json.smap1.cdramo
                ,estado          : 'W'
                ,nmpoliza        : json.smap1.nmpoliza
                ,'map1.ntramite' : json.smap1.NTRAMITE
                ,cdtipsit        : json.smap1.cdtipsit
            };
            
            if(!Ext.isEmpty(_0_flujo))
            {
                paramsDatCom['flujo.cdtipflu']  = _0_flujo.cdtipflu;
                paramsDatCom['flujo.cdflujomc'] = _0_flujo.cdflujomc;
                paramsDatCom['flujo.tipoent']   = _0_flujo.tipoent;  //ACTUAL QUE SE RECUPERARA
                paramsDatCom['flujo.claveent']  = _0_flujo.claveent; //ACTUAL QUE SE RECUPERARA
                paramsDatCom['flujo.webid']     = _0_flujo.webid;    //ACTUAL QUE SE RECUPERARA
                paramsDatCom['flujo.ntramite']  = _0_flujo.ntramite;
                paramsDatCom['flujo.status']    = _0_flujo.status;
                paramsDatCom['flujo.cdunieco']  = _0_flujo.cdunieco;
                paramsDatCom['flujo.cdramo']    = _0_flujo.cdramo;
                paramsDatCom['flujo.estado']    = _0_flujo.estado;
                paramsDatCom['flujo.nmpoliza']  = _0_flujo.nmpoliza;
                paramsDatCom['flujo.nmsituac']  = _0_flujo.nmsituac;
                paramsDatCom['flujo.nmsuplem']  = _0_flujo.nmsuplem;
                paramsDatCom['flujo.aux']       = 'RECUPERAR';
            }
            
            Ext.create('Ext.form.Panel').submit(
            {
                url             : _0_urlDatosComplementarios
                ,standardSubmit : true
                ,params         : paramsDatCom
            });
        }
    }
    else
    {
        mensajeError(json.error);
        _0_panelPri.setLoading(false);
    }
}

function _0_agregarAsegu(boton)
{
    var valido=true;
    if(valido)
    {
        if(!_0_necesitoIncisos)
        {
            valido=_0_storeIncisos.getCount()<1;
            if(!valido)
            {
                mensajeWarning('Solo se puede introducir un inciso');
            }
        }
    }
    if(valido)
    {
        var grid=boton.up().up();
        debug('_0_agregarAsegu');
        var arrayEditores = _0_rowEditing.editor.form.monitor.getItems().items; 
        debug('arrayEditores:',arrayEditores);
        var record = new _0_modelo();
        for(var i = 0;i<arrayEditores.length;i++)
        {
            var iEditor = arrayEditores[i];
            if(iEditor.store)
            {
                record.set(iEditor.name,iEditor.store.getAt(0).get('key'));
            }
            else if(iEditor.format)
            {
                record.set(iEditor.name,Ext.Date.format(new Date(),'d/m/Y'));
            }
            else
            {
                var estaEnModeloExtra=false;
                debug('_0_modeloExtraFields.length:',_0_modeloExtraFields.length);
                for(var j=0;j<_0_modeloExtraFields.length;j++)
                {
                    if(iEditor.name==_0_modeloExtraFields[j].name)
                    {
                        estaEnModeloExtra=true;
                    }
                }
                if(!estaEnModeloExtra)
                {
                    record.set(iEditor.name,iEditor.name);
                }
            }
        }
        record.set('contador',_0_storeIncisos.getCount()+1);
        _0_storeIncisos.add(record);
        _0_rowEditing.startEdit(_0_storeIncisos.getCount()-1,1);
        _0_rowEditing.startEdit(_0_storeIncisos.getCount()-1,1);
        // Se aplica el focus en algun boton habilitado del grid de incisos:
        try {
            _0_botonera.down('button[disabled=false]').focus();
        } catch(e) {
            debug(e);
        }
        //window.parent.scrollTo(0, _0_formAgrupados.getHeight());//ELIMINADA
    }
}

function _0_cotizar(boton)
{
    debug('_0_cotizar');
    if(_0_validarBase())//
    {
    	var smap = _0_smap1;
    	
        if(!Ext.isEmpty(_0_recordClienteRecuperado))
        {
            debug('_0_recordClienteRecuperado:',_0_recordClienteRecuperado);
            _0_smap1['cdpersonCli'] = Ext.isEmpty(_0_recordClienteRecuperado) ? '' : _0_recordClienteRecuperado.raw.CLAVECLI;
            _0_smap1['cdideperCli'] = Ext.isEmpty(_0_recordClienteRecuperado) ? '' : _0_recordClienteRecuperado.raw.CDIDEPER;
            _0_smap1['nmorddomCli'] = Ext.isEmpty(_0_recordClienteRecuperado) ? '' : _0_recordClienteRecuperado.raw.NMORDDOM;
            _0_smap1['notarificar'] = !Ext.isEmpty(sinTarificar)&&sinTarificar==true?'si':'no';//Se utiliza para no retarificar   
          
            if(_0_smap1.cdramo=='16')
            {
               var agenteCmp=_fieldByLabel('AGENTE');
               if(Ext.isEmpty(agenteCmp))
               {
                   smap.cdagenteAux='';
               }
               else
               {
                   if(!Ext.isEmpty(agenteCmp.getValue()))
                   {
                   smap.cdagenteAux=agenteCmp.getValue();
                   }
               }
               
               if(!Ext.isEmpty(cdagenteCotiza))
               {
                    _0_smap1['cdagente']    = 'A'+cdagenteCotiza;
                    _fieldByLabel('AGENTE').setValue(cdagenteCotiza);
               }
            }
        }
        
        if(_0_smap1.cdramo=='16')
        {
        	 _0_smap1['notarificar'] = !Ext.isEmpty(sinTarificar)&&sinTarificar==true?'si':'no';//Se utiliza para no retarificar 
//     		 if(rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')==-1)
	         if(!RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol))
       		 {
       			 _0_smap1['cdsisrol'] =RolSistema.SuscriptorAuto;
       		 }
       	  
        	 if(!Ext.isEmpty(cdagenteCotiza))
        	 {
        		  _0_smap1['cdusuari']    = 'A'+cdagenteCotiza;
        	 }
        	 if(!Ext.isEmpty(cduniecocotiza))
             {
                  _0_smap1['cdunieco']    = ''+cduniecocotiza;
             }
        	 else if(!Ext.isEmpty(_fieldByLabel('AGENTE').getValue()))
             {
        		 cdagenteCotiza = _fieldByLabel('AGENTE').getValue();
                  _0_smap1['cdagente']    = 'A'+cdagenteCotiza;
             }
        }
        
        var json=
        {
             slist1 : []
            ,smap1  : smap 
        };
        
        if(_0_necesitoIncisos)
        {
            _0_storeIncisos.each(function(record)
            {
                var inciso=_0_formAgrupados.getValues();
                for(var key in record.data)
                {
                    var value=record.data[key];
                    debug(typeof value,key,value);
                    if((typeof value=='object')&&value&&value.getDate)
                    {
                        var fecha='';
                        fecha+=value.getDate();
                        if((fecha+'x').length==2)//1x 
                        {
                            fecha = ('x'+fecha).replace('x','0');//x1=01 
                        }
                        fecha+='/';
                        fecha+=value.getMonth()+1<10?
                                (('x'+(value.getMonth()+1)).replace('x','0'))
                                :(value.getMonth()+1);
                        fecha+='/';
                        fecha+=value.getFullYear();
                        value=fecha;
                    }
                    inciso[key]=value;
                }
                json['slist1'].push(inciso);
            });
        }
        else
        {
            var inciso=_0_formAgrupados.getValues();
            if(_0_storeIncisos.getCount()==1)
            {
                var record=_0_storeIncisos.getAt(0);
                for(var key in record.data)
                {
                    var value=record.data[key];
                    debug(typeof value,key,value);
                    if((typeof value=='object')&&value&&value.getDate)
                    {
                        var fecha='';
                        fecha+=value.getDate();
                        if((fecha+'x').length==2)//1x 
                        {
                            fecha = ('x'+fecha).replace('x','0');//x1=01 
                        }
                        fecha+='/';
                        fecha+=value.getMonth()+1<10?
                                (('x'+(value.getMonth()+1)).replace('x','0'))
                                :(value.getMonth()+1);
                        fecha+='/';
                        fecha+=value.getFullYear();
                        value=fecha;
                    }
                    inciso[key]=value;
                }
            }
            json['slist1'].push(inciso);
        }
        debug('json para cotizar:',json);
        _0_panelPri.setLoading(true);
        Ext.Ajax.request(
        {
            url       : _0_smap1['externo']=='si'?_0_urlCotizarExterno:_0_urlCotizar
            ,jsonData : json
            ,success  : function(response)
            {
                _0_bloquear(true);
                _0_panelPri.setLoading(false);
                json=Ext.decode(response.responseText);
                if(json.success==true)
                {
//                 	alert('Regresa');
                    debug(Ext.decode(json.smap1.fields));
                    debug(Ext.decode(json.smap1.columnas));
                    
                    _0_fieldNmpoliza.setValue(json.smap1.nmpoliza);
                                    
                    _grabarEvento('COTIZACION'
                                  ,'COTIZA'
                                  ,_0_smap1.ntramite
                                  ,_0_smap1.cdunieco
                                  ,_0_smap1.cdramo
                                  ,'W'
                                  ,json.smap1.nmpoliza
                                  ,json.smap1.nmpoliza
                                  ,'buscar'
                                  );
                    
                    Ext.define('_0_modeloTarifa',
                    {
                        extend  : 'Ext.data.Model'
                        ,fields : Ext.decode(json.smap1.fields)
                    });
                    
                    
                    
                    /////////////// DXN //////////////////////////////
                    var formasPago=json.slist2,soloDXN=[];
                    if(_0_smap1.SITUACION=='AUTO' && _0_smap1.cdtipsit!='AT' && _0_smap1.cdtipsit!='MC'){
                    
	                    var administradora=_fieldByName("aux.otvalor08");
	                    var retenedora=_fieldByName("aux.otvalor09");
	                    
	                    
	                    
	                    
	                    
	                    if(retenedora.getValue()!=null && retenedora.getValue()!="-1" && retenedora.getValue()!=""){
	                        var fpago=getFormaPago(administradora.getValue(),retenedora.getValue());
//	                      console.log(administradora.getValue());
	                        
	                        
	                        var i;
	                        for(i in formasPago){
	                            
	                            if((fpago.fpago+'').trim()==(formasPago[i].CDPERPAG+'').trim()){
	                                soloDXN.push(formasPago[i]);
	                                break;
	                            }
	                            
	                        }
	                        
	                        
	                        formasPago=soloDXN;
	                    }else{
	                        var i;
	                        for(i in formasPago){
	                            
	                            if((!FormaPago.esDxN((formasPago[i].CDPERPAG+'').trim())) && (formasPago[i].DSPERPAG+'').trim().indexOf("DXN")==-1  ){
	                                
	                                soloDXN.push(formasPago[i]);
	                                
	                            }
	                            
	                        }
	                        formasPago=soloDXN;
	                    }
	                    
                    }
                    
         			///////////////// DXN /////////////////////////////
                    
                    
                    
                    _0_gridTarifas=Ext.create('Ext.grid.Panel',
                    {
                        title             : ( Ext.isEmpty(_0_flujo) ? false : (_0_flujo.cdflujomc == 220 && _0_flujo.cdtipflu == 103 && _0_smap1.cdramo == Ramo.AutosFronterizos) 
                                            )? 'Resultados:<br>Plan y forma de pago de p\u00f3liza a renovar: '+json.smap1.fila+'-'+json.smap1.columna 
                                          :'Resultados'
                        ,store            : Ext.create('Ext.data.Store',
                        {
                            model : '_0_modeloTarifa'
                            ,data : formasPago
                        })
                        ,columns          : Ext.decode(json.smap1.columnas)
                        ,selType          : 'cellmodel'
                        ,minHeight        : 100
                        ,enableColumnMove : false
                        ,buttonAlign      : 'center'
                        ,buttons          :
                        [
                            new _0_BotComprar()
                            ,{
                                text    : 'Enviar',
                                icon    : '${icons}user_go.png',
                                hidden  : typeof _0_flujoAux.onCotizar !== 'number',
                                handler : _0_botonOnCotizarClic
                            }
                            ,new _0_BotDetalles()
                            ,new _0_BotCoberturas()
                            ,new _0_BotEditar()
                            ,new _0_BotClonar()
                            ,new _0_BotNueva()
                            ,new _0_BotMail()
                            ,new _0_BotImprimir()
                        ]
                        ,listeners        :
                        {
                            select       : _0_tarifaSelect
                            ,afterrender : function(me)
                            {
                                if(!Ext.isEmpty(json.smap1.columna) && !Ext.isEmpty(json.smap1.fila))
                                {
                                    var sm = _0_gridTarifas.getSelectionModel();
                                    try
                                    {
                                        var columna=0, fila=999; 
                                        for(var IteGriTar=1;IteGriTar<_0_gridTarifas.columns.length;IteGriTar++)
                                        {
                                            if((_0_gridTarifas.columns[IteGriTar].text).toLowerCase() === json.smap1.columna.toLowerCase())
                                            {
                                                 columna = IteGriTar - columna;
                                                 IteGriTar = _0_gridTarifas.columns.length + 1;
                                            }
                                            else if( IteGriTar%2 != 1)
                                            {
                                                columna ++;
                                            }
                                        }
                                        
                                        for(var IteGriTar=0;IteGriTar<17;IteGriTar++)
                                        {
                                            sm.select({row:IteGriTar,column:columna});
                                            var texto = (sm.getSelection({row:IteGriTar,column:columna})[0].data.DSPERPAG).toLowerCase()
                                            if(json.smap1.fila.toLowerCase() === texto)
                                            {
                                                  fila = IteGriTar;
                                                  IteGriTar = 18;
                                            }
                                        }
                                        
                                        sm.select({row:fila,column:columna});
                                     }catch(e) {
                                       debug("Excede rango fuera de la cuadricula de tarifas");
                                     }
                                }
                                
                                if(!Ext.isEmpty(_0_flujo))// && _0_smap1.SITUACION === 'AUTO' ) // && !sinTarificar===true)
                                {
                                    _0_actualizarCotizacionTramite();
                                }
                            }
                        }
                    });
                    
                    if(_0_smap1.cdramo+'x'=='6x')
                    {
                        Ext.getCmp('_0_botDetallesId').setDisabled(true);
                        Ext.getCmp('_0_botCoberturasId').setDisabled(true);
                    }
                    
                    _0_panelPri.add(_0_gridTarifas);
                    _0_panelPri.doLayout();
                    //setTimeout(function(){debug('timeout 1000');window.parent.scrollTo(0, 99999);},1000);//ELIMINADO
                    // Se aplica el focus en algun boton habilitado del grid de tarifas:
                    try {
                       _0_gridTarifas.down('button[disabled=false]').focus(false, 1000);
                    } catch(e) {
                        debug(e);
                    }
                }
                else
                {
                    _0_bloquear(false);
                    mensajeError('Error al cotizar:<br/>'+json.error);
                }
            }
            ,failure  : function(response)
            {
                _0_panelPri.setLoading(false);
                errorComunicacion();
            }
        });
    }
}

function _0_validarBase()
{
    var valido=true;
    debug('>_0_validarBase');
    
    //form validation
    if(valido && !cargaCotiza)
    {
        valido=_0_formAgrupados.isValid();
        if(!valido)
        {
            datosIncompletos();
        }
    }
    
    //algun inciso
    if(valido&&_0_necesitoIncisos)
    {
        valido=_0_storeIncisos.getCount()>0;
        if(!valido)
        {
            mensajeWarning('No hay incisos');
        }
    }
    
    //validar atributos de tatrisit en grid
    if(valido&&_0_necesitoIncisos)
    {
        _0_storeIncisos.each(function(record)
        {
            for(var key in record.data)
            {
                debug('validando:',record.data);
                //debug('modelo extra fields:',_0_modeloExtraFields);
                var estaEnModeloExtra=false;
                //debug('_0_modeloExtraFields.length:',_0_modeloExtraFields.length);
                for(var i=0;i<_0_modeloExtraFields.length;i++)
                {
                    if(key==_0_modeloExtraFields[i].name)
                    {
                        estaEnModeloExtra=true;
                    }
                }
                if(!estaEnModeloExtra)
                {
                    var value=record.data[key];
                    valido=valido&&value;
                    if(!valido)
                    {
                        debug('falta: ',key);
                    }
                }
            }
        });
        if(!valido)
        {
            mensajeWarning('Los datos de los incisos son requeridos');
        }
    }
    
    if(valido && _0_necesitoIncisos && _0_smap1.SITUACION=='PERSONA'){
        
        try{
            var colNameParentesco = _0_gridIncisos.down('[text=PARENTESCO]').dataIndex; 
            var colNameFechaNacimi = _0_gridIncisos.down('[text*=NACIMIENTO]').dataIndex; 
            var fechaHoy = new Date();
            
            _0_storeIncisos.each(function(record){
    
                var parentescoRecord = record.get(colNameParentesco);
                var fechaRecord      = record.get(colNameFechaNacimi);
                
                debug('fechaHoy: ', fechaHoy);
                debug('fechaRecord: ', fechaRecord);
                
                var years = calculaAniosTranscurridos(fechaRecord,fechaHoy);
                
                debug('years: ', years);
                
                if((parentescoRecord == _parentescoTitular) && years != null && (years < 18) ){
                    mensajeWarning('El Titular es Menor de Edad, se requerir&aacute; una autorizaci&oacute;n posterior.');
                }
                
            });
        }catch(e){
            debug('Error en la validacion de Edad del Titular!!!',e);
        }
    }
    
    //custom validation
    if(valido)
    {
        valido=_0_validacion_custom();
    }
    
    debug('<_0_validarBase');
    return valido;
}

function _0_tarifaSelect(selModel, record, row, column, eOpts)
{
    debug('column:',column);
    if(column>0)
    {
        column = (column * 2) -1;
    }
    debug('( column * 2 )-1:',column);
    var columnName=_0_gridTarifas.columns[column].dataIndex;
    debug('record',record);
    debug('columnName',columnName);
    if(columnName=='DSPERPAG')
    {
        debug('DSPERPAG');
        Ext.getCmp('_0_botDetallesId').setDisabled(true);
        Ext.getCmp('_0_botCoberturasId').setDisabled(true);
        Ext.getCmp('_0_botMailId').setDisabled(true);
        Ext.getCmp('_0_botImprimirId').setDisabled(true);
        Ext.getCmp('_0_botComprarId').setDisabled(true);
    }
    else
    {
        // M N P R I M A X
        //0 1 2 3 4 5 6 7
        _0_selectedCdperpag = record.get("CDPERPAG");
        _0_selectedCdplan   = columnName.substr(7);
        _0_selectedDsplan   = record.get("DSPLAN"+_0_selectedCdplan);
        _0_selectedNmsituac = record.get("NMSITUAC");
        debug('_0_selectedCdperpag',_0_selectedCdperpag);
        debug('_0_selectedCdplan',_0_selectedCdplan);
        debug('_0_selectedDsplan',_0_selectedDsplan);
        debug('_0_selectedNmsituac',_0_selectedNmsituac);
        
        Ext.getCmp('_0_botDetallesId').setDisabled(false);
        Ext.getCmp('_0_botCoberturasId').setDisabled(false);
        if(_0_smap1.cdramo+'x'!='6x')
        {
            Ext.getCmp('_0_botMailId').setDisabled(false);
            Ext.getCmp('_0_botImprimirId').setDisabled(false);
        }
        Ext.getCmp('_0_botComprarId').setDisabled(false);
    }
}

function _0_gmiPostalSelect(a,b,c,sinReset)
{
    var postal = _fieldLikeLabel('POSTAL');
    var zona   = _fieldLikeLabel('CAMBIO DE ZONA');
    debug('POSTAL select:',postal.getValue(),sinReset,'<sinReset');
    Ext.Ajax.request(
    {
        url     : _0_urlValidarCambioZonaGMI
        ,params :
        {
            'smap1.cdramo'     : _0_smap1.cdramo
            ,'smap1.cdtipsit'  : _0_smap1.cdtipsit
            ,'smap1.codpostal' : postal.getValue()
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### validar eliminacion cambio zona:',json);
            if(json.exito)
            {
                if(!sinReset)
                {
                    zona.reset();
                }
                zona.show();
            }
            else
            {
                zona.setValue('N');
                zona.hide();
            }
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
}

function _0_gmiCirchospSelect(a,b,c,sinReset)
{
    var circ  = _fieldLikeLabel('CULO HOSPITALARIO');
    var enfer = _fieldLikeLabel('ENFERMEDAD CATAS');
    debug('CIRCULO HOSP select:',circ.getValue(),sinReset,'<sinReset');
    Ext.Ajax.request(
    {
        url     : _0_urlValidarEnfermCatasGMI
        ,params :
        {
            'smap1.cdramo'    : _0_smap1.cdramo
            ,'smap1.circHosp' : circ.getValue()
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### validar enfermedad catastrofica:',json);
            if(json.exito)
            {
                if(!sinReset)
                {
                    enfer.reset();
                }
                enfer.show();
            }
            else
            {
                enfer.setValue('N');
                enfer.hide();
            }
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
}

/**
 * Verifica que el Codigo Postal pertenezca a algun estado
 * @type String
 */
function agregaValidacionCPvsEstado() {
    // Si existen los campos de codigo postal y estado, y este ultimo esta anidado, agregar validacion:
    var tmpPostal = _fieldLikeLabel('POSTAL', null, true);
    var tmpEstado = _fieldLikeLabel('ESTADO', null, true);
    if( tmpPostal && tmpEstado && !Ext.isEmpty(tmpEstado.anidado) && tmpEstado.anidado == true) {
        // Se agrega listener al store de Estado para verificar si tiene elementos:
        debug("***** Se agrega validacion de CP VS Estado *****");
        tmpEstado.getStore().on({
            load : function(store, records, successful, eOpts) {
                if(store.count() == 0) {
                    mensajeWarning('El C&oacute;digo Postal no existe, introduzca uno v&aacute;lido');
                }
            }
        });
    }
}

function _0_recuperarDescuento()
{
    var ck = 'Recuperando descuento';
    try
    {
        Ext.Ajax.request(
        {
            url     : _0_urlRecuperacionSimple
            ,params :
            {
                'smap1.procedimiento' : 'RECUPERAR_DESCUENTO_RECARGO_RAMO_5'
                ,'smap1.cdtipsit'     : _0_smap1.cdtipsit
                ,'smap1.cdagente'     : _fieldByLabel('AGENTE').getValue()
                ,'smap1.negocio'      : RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol) ? '999999' : '0' //_0_smap1.cdsisrol == 'SUSCRIAUTO'
                ,'smap1.tipocot'      : 'I'
                ,'smap1.cdsisrol'     : _0_smap1.cdsisrol
                ,'smap1.cdusuari'     : _0_smap1.cdusuari
            }
            ,success : function(response)
            {
                var ck = 'Decodificando descuento por rol/usuario';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### cargar rango descuento fronterizo:',json);
                    
                    ck           = 'Recuperando componente de descuento';
                    var itemDesc = _fieldLikeLabel('DESCUENTO',null,true);
                    
                    if(json.exito)
                    {
                        itemDesc.minValue=100*Number(json.smap1.min);
                        itemDesc.maxValue=100*Number(json.smap1.max);
                        itemDesc.isValid();
                        debug('min:',itemDesc.minValue);
                        debug('max:',itemDesc.maxValue);
                        itemDesc.setReadOnly(false);
                    }
                    else
                    {
                        itemDesc.minValue=0;
                        itemDesc.maxValue=0;
                        itemDesc.setValue(0);
                        itemDesc.isValid();
                        itemDesc.setReadOnly(true);
                        mensajeError(json.respuesta);
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                var ck = 'Recuperando componente de descuento';
                try
                {
                    var itemDesc = _fieldLikeLabel('DESCUENTO',null,true);
                    itemDesc.minValue=0;
                    itemDesc.maxValue=0;
                    itemDesc.setValue(0);
                    itemDesc.isValid();
                    itemDesc.setReadOnly(true);
                    errorComunicacion();
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function mensajeValidacionNumSerie(titulo,imagenSeccion,txtRespuesta)
{
    var panelImagen = new Ext.Panel({
        defaults    : {
            style   : 'margin:5px;'
        },
        layout: {
            type: 'hbox'
            ,align: 'center'
            ,pack: 'center'
        }
        ,border: false
        ,items:[{               
            xtype   : 'image'
            ,src    : '${ctx}/images/cotizacionautos/menu_endosos.png'
            ,width: 200
            ,height: 100
        }]
    });

    validacionNumSerie = Ext.create('Ext.window.Window',{
        title        : titulo
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 520
        ,icon :imagenSeccion
        ,resizable: false
        ,height      : 250
        ,items       :[
            Ext.create('Ext.form.Panel', {
                id: 'panelClausula'
                ,width       : 500
                ,height      : 150
                ,bodyPadding: 5
                ,renderTo: Ext.getBody()
                ,defaults    : {
                    style : 'margin:5px;'
                }
                ,border: false
                ,items: [
                {
                    xtype  : 'label'
                    ,text  : txtRespuesta
                    ,width: 100
                    ,height      : 100
                    ,style : 'color:red;margin:10px;'
                }
                ,{
                    border: false
                    ,items    :
                        [   panelImagen     ]
                }]
            })
        ],
        buttonAlign:'center',
        buttons: [{
            text: 'Aceptar',
            icon: _CONTEXT+'/resources/fam3icons/icons/accept.png',
            buttonAlign : 'center',
            handler: function() {
                validacionNumSerie.close();
            }
        }]
    });
    centrarVentanaInterna(validacionNumSerie.show());
}

/**
* Recupera los datos del agente asoaciado a una poliza
* @param cdagente
*/
function asignarAgente(agente)
{
//   if(rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')!=-1)
	if(RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol))
     {
         _fieldByLabel('AGENTE').getStore().load(
         {
             params :
             {
                 'params.agente' : agente
             }
             ,callback : function()
             {
                 
                 _fieldByLabel('AGENTE').setValue(
                     _fieldByLabel('AGENTE').findRecord('key',agente)
                 );
             }
         });
     }
     else
     {
//       else
//       {
      _fieldByLabel('AGENTE').setValue(agente);
//       }
        
//              if(!cargarXpoliza){
//              _fieldByLabel('AGENTE').setValue(
//                  _fieldByLabel('AGENTE').findRecord('key',agente)
//                  );
//          }
         /* else{
         _fieldByLabel('FOLIO').reset();
         } */
     }
}

function _0_calculaVigencia(comp,val)
{
//  alert('_0_calculaVigencia');
    debug('>_0_calculaVigencia');
    var feini = _fieldByName('feini');
    var fefin = _fieldByName('fefin');
    
    var itemVigencia=_fieldByLabel('FIN DE VIGENCIA');
    //itemVigencia.hide();
    
    if(feini.isValid()&&fefin.isValid())
    {
        var milisDif = Ext.Date.getElapsed(feini.getValue(),fefin.getValue());
        var diasDif  = (milisDif/1000/60/60/24).toFixed(0);
        debug('milisDif:',milisDif,'diasDif:',diasDif);
        itemVigencia.setValue(diasDif);
    }
    debug('<_0_calculaVigencia');
}

function retroactividadfechaini()
{
//  alert('retroactividadfechaini');
      Ext.Ajax.request(
          {
              url      : _0_urlRetroactividadDifer
              ,params  :
              { 
                  'smap1.cdunieco'  : _0_smap1.cdunieco
                  ,'smap1.cdramo'   : _0_smap1.cdramo
                  ,'smap1.cdtipsup' : '1'
                  ,'smap1.cdusuari' : _0_smap1.cdusuari
                  ,'smap1.cdtipsit' : _0_smap1.cdtipsit
                  ,'smap1.tipocot'  : 'I'
              }
              ,success : function(response)
              {
                  _0_panelPri.setLoading(false);
                  var ck = 'Recuperando retroactividad';
                  try
                  {
                      var json=Ext.decode(response.responseText);
                      debug('### respuesta obtener rango vigencia:',json);
                      if(json.exito)
                      {
                          _fieldByName('feini').setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.retroac*-1));
                          _fieldByName('feini').setMaxValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.diferi));
                          _fieldByName('FESOLICI').setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.retroac*-1));
                      }
                      else
                      {
                          mensajeWarning('Falta definir rango de vigencia para el producto (RANGOVIGENCIA)');
                      }
                  }
                  catch(e)
                  {
                      manejaException(e,ck);
                  }
              }
              ,failure : function()
              {
                  _0_panelPri.setLoading(false);
                  errorComunicacion();
              }
          });
}

function obtienefechafinplazo()
{
//  alert('obtienefechafinplazo');
    Ext.Ajax.request(
        {
            url     : _0_urlCargarDetalleNegocioRamo5
            ,params :
            {
                'smap1.negocio' : RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol) ? '999999' : '0', //_0_smap1.cdsisrol == 'SUSCRIAUTO',
                'smap1.cdramo'  : _0_smap1.cdramo,
                'smap1.cdtipsit': _0_smap1.cdtipsit
            }
            ,success : function(response)
            {
//                 negoCmp.setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('### detalle negocio:',json);
                
                plazoenanios = Number(json.smap1.LIMITE_SUPERIOR);
                _fieldByName('FESOLICI').setMaxValue(Ext.Date.add(new Date(),Ext.Date.YEAR, Number(json.smap1.LIMITE_SUPERIOR)));
                _fieldByName('fefin').setMaxValue(Ext.Date.add(new Date(),Ext.Date.YEAR,plazoenanios));
                   
                if(Number(json.smap1.MULTIANUAL) != 0) {
                    
//                   plazoenanios = Number(json.smap1.MULTIANUAL);
//                     _fieldByName('FESOLICI').setMaxValue(Ext.Date.add(new Date(),Ext.Date.YEAR, Number(json.smap1.MULTIANUAL)));
//                     _fieldByName('fefin').setMaxValue(Ext.Date.add(new Date(),Ext.Date.YEAR,plazoenanios));
                    _fieldByName('fefin').validator=function(val)
                    {
                        var feiniVal = Ext.Date.format(_fieldByName('feini').getValue(),'d/m/Y');
                        debug('feiniVal:',feiniVal);
                        var fefinVal=[];
                        for(var i=1; i <= Number(json.smap1.MULTIANUAL); i++)
                        {
                            debug('mas anios:',i);
                            fefinVal.push(Ext.Date.format(Ext.Date.add(Ext.Date.parse(feiniVal,'d/m/Y'),Ext.Date.YEAR,i),'d/m/Y'));
                        }
                        debug('validar contra:',fefinVal);
                        var valido = true;
                        if(!Ext.Array.contains(fefinVal,val))
                        {
                            valido = 'Solo se permite:';
                            for(var i in fefinVal)
                            {
                                valido = valido + ' ' + fefinVal[i];
                                if(fefinVal.length>1&&i<fefinVal.length-1)
                                {
                                    valido = valido + ',';
                                }
                            }
                        }
                        return valido;
                    }
                }
                _fieldByName('fefin').isValid();
            }
            ,failure : function()
            {
//                 negoCmp.setLoading(false);
                errorComunicacion();
            }
        });
}

function _0_actualizarCotizacionTramite(callback)
{
    var ck = 'Registrando cotizaci\u00f3n de tr\u00e1mite';
    try
    {
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _0_urlActualizarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : _0_flujo.ntramite
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
                                url      : _0_urlDetalleTramite
                                ,params  :
                                {
                                    'smap1.ntramite'  : _0_flujo.ntramite
                                    ,'smap1.status'   : _0_flujo.status
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

function _0_recuperarCotizacionDeTramite()
{
    if(!Ext.isEmpty(_0_flujo))// && _0_smap1.SITUACION === 'AUTO' )
    {
        var ck = 'Recuperando cotizaci\u00f3n de tr\u00e1mite';
        try
        {
            _mask(ck);
            Ext.Ajax.request(
            {
                url      : _0_urlRecuperarOtvalorTramiteXDsatribu
                ,params  :
                {
                    'params.ntramite'  : _0_flujo.ntramite
                    ,'params.dsatribu' : 'COTIZACI%N%TR%MITE'
                }
                ,success : function(response)
                {
                    _unmask();
                    var ck = 'Decodificando respuesta al recuperar cotizaci\u00f3n de tr\u00e1mite';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### cotizacion de tramite:',json);
                        if(json.success===true)
                        {
                            if(!Ext.isEmpty(json.params.otvalor))
                            {
                                _0_recuperarCotizacion(json.params.otvalor);
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
                ,failure : function(response)
                {
                    _unmask();
                    errorComunicacion(null,'Error al recuperar cotizaci\u00f3n de tr\u00e1mite');
                }
            });
        }
        catch(e)
        {
            _unmask();
            manejaException(e,ck);
        }
    }
}

function _0_cargarParametrizacionCoberturas(callback)
{
//  alert(1);
    debug('>_0_cargarParametrizacionCoberturas callback:',!Ext.isEmpty(callback),'DUMMY');
    
    var _f1_tipoServicio;
    var _f1_modelo;
    
    if(!Ext.isEmpty(_fieldLikeLabel('TIPO SERVICIO',null,true)))
    { _f1_tipoServicio = _fieldByLabel('TIPO SERVICIO').getValue();}
   
    if(!Ext.isEmpty(_fieldLikeLabel('MODELO',null,true)))
    { _f1_modelo = _fieldByLabel('MODELO').getValue();}
    
    var valido =   !Ext.isEmpty(_f1_tipoServicio)
                 && !Ext.isEmpty(_f1_modelo);
                 
    if(valido)
    {
//         var _f1_panelpri = _fieldById('_0_panelpri');
        _0_panelPri.setLoading(true);
        var params =
        {
            'smap1.cdtipsit'      : _0_smap1.cdtipsit
            ,'smap1.cdsisrol'     : _0_smap1.cdsisrol
            ,'smap1.negocio'      : RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol) ? '999999' : '0' //_0_smap1.cdsisrol == 'SUSCRIAUTO'
            ,'smap1.tipoServicio' : (_f1_tipoServicio=='P') ? '01' : '03' 
            ,'smap1.modelo'       : _f1_modelo
            ,'smap1.tipoPersona'  : 'F'
            ,'smap1.submarca'     : '00000'
            ,'smap1.clavegs'      : '00000'
        };
        Ext.Ajax.request(
        {
            url      : _0_urlCargarParamerizacionCoberturas
            ,params  : params
            ,success : function(response)
            {
                _0_panelPri.setLoading(false);
                var _f1_json=Ext.decode(response.responseText);
                debug('### parametrizacion:',_f1_json);
                if(_f1_json.exito)
                {
//                  alert('EXITO');
                    for(var i=0;i<_f1_json.slist1.length;i++)
                    {
                        var item = _fieldByName('parametros.pv_otvalor'+(('00'+_f1_json.slist1[i].cdatribu).slice(-2)));
                        if(_f1_json.slist1[i].aplica+'x'=='1x')
                        {
                            if(<s:property value='%{getSmap1().containsKey("debug")}' />)
                            {
                                item.setReadOnly(false);
                                item.addCls('green');
                                item.removeCls('red');
                            }
                            else
                            {
                                item.show();
                            }
                            var minimo = _f1_json.slist1[i].minimo;
                            var maximo = _f1_json.slist1[i].maximo;
                            if(!Ext.isEmpty(minimo)&&!Ext.isEmpty(maximo))
                            {
                                item.minValue = minimo;
                                item.maxValue = maximo;
                                if(item.xtype=='combobox')
                                {
                                    item.validator=function(value)
                                    {
                                        var valido=true;
                                        if(value+'x'!='x')
                                        {
                                            var value=this.getStore().findRecord('value',value).get('key');
                                            if(Number(value)<Number(this.minValue))
                                            {
                                                valido = 'El valor m&iacute;nimo es '+this.minValue;
                                            }
                                            else if(Number(value)>Number(this.maxValue))
                                            {
                                                valido = 'El valor m&aacute;ximo es '+this.maxValue;
                                            }
                                        }
                                        return valido;
                                    }
                                    if(!item.isValid())
                                    {
                                        item.reset();
                                    }
                                    debug('item=',item.fieldLabel);
                                    debug('minimo=',minimo,'maximo=',maximo);
                                    item.store.filterBy(function(record)
                                    {
                                        debug('filtrando record=',record);
                                        var key=record.get('key')-0;
                                        debug('quitando key=',key,key>=minimo&&key<=maximo,'.');
                                        return key>=minimo&&key<=maximo;
                                    });
                                    item.on(
                                    {
                                        expand : function(me)
                                        {
                                            var minimo = me.minValue;
                                            var maximo = me.maxValue;
                                            me.store.filterBy(function(record)
                                            {
                                                debug('filtrando record=',record);
                                                var key=record.get('key')-0;
                                                debug('quitando key=',key,key>=minimo&&key<=maximo,'.');
                                                return key>=minimo&&key<=maximo;
                                            });
                                        }
                                    });
                                }
                            }
                            item.isValid();
                        }
                        else
                        {
                            if(<s:property value='%{getSmap1().containsKey("debug")}' />)
                            {
                                item.setReadOnly(true);
                                item.addCls('red');
                                item.removeCls('green');
                            }
                            else
                            {
                                item.hide();
                            }
                            item.setValue(_f1_json.slist1[i].valor);
                        }
                    }
                    if(<s:property value='%{getSmap1().containsKey("debug")}' />)
                    {
                        var aux1 = '';
                        for(var i in params)
                        {
                            aux1 = aux1+i+':'+params[i]+'\n';
                        }
                        var aux2 = '';
                        for(var i=0;i<_f1_json.slist1.length;i++)
                        {
                            aux2 = aux2
                                   +_f1_json.slist1[i].cdatribu+' - '
                                   +'aplica ('+_f1_json.slist1[i].aplica
                                   +') valor ('+_f1_json.slist1[i].valor
                                   +') minimo ('+_f1_json.slist1[i].minimo
                                   +') maximo ('+_f1_json.slist1[i].maximo
                                   +')\n';
                        }
                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                        {
                            title   : '[DEBUG] PARAMETRIZACION DE COBERTURAS'
                            ,items  :
                            [
                                {
                                    xtype   : 'textarea'
                                    ,width  : 400
                                    ,height : 150
                                    ,value  : aux1
                                }
                                ,{
                                    xtype   : 'textarea'
                                    ,width  : 400
                                    ,height : 450
                                    ,value  : aux2
                                }
                            ]
                        }).show());
                    }
                    if(!Ext.isEmpty(callback))
                    {
                        callback();
                    }
                    
                    if(!Ext.isEmpty(_fieldLikeLabel('CANAD',null,true)))
                    {	
	                    if(_0_smap1.cdtipsit+'x'=='AFx'||_0_smap1.cdtipsit+'x'=='PUx')
	                    {
	                        
	                    	var canadaCmp = _fieldLikeLabel('CANAD');
	                        debug('@CUSTOM canada:',canadaCmp);
	                        canadaCmp.anidado = true;
	                        canadaCmp.heredar = function(remoto,micallback)
	                        {
	                            var me        = _fieldLikeLabel('CANAD');
	                            var postalCmp = _fieldLikeLabel('POSTAL');
	                            var postalVal = postalCmp.getValue();
	                            if((postalVal+'x').length==6)
	                            {
	                                me.setLoading(true);
	                                Ext.Ajax.request(
	                                {
	                                    url     : _0_urlRecuperacionSimple
	                                    ,params :
	                                    {
	                                        'smap1.procedimiento' : 'VERIFICAR_CODIGO_POSTAL_FRONTERIZO'
	                                        ,'smap1.cdpostal'     : postalVal
	                                    }
	                                    ,success : function(response)
	                                    {
	                                        me.setLoading(false);
	                                        var json=Ext.decode(response.responseText);
	                                        debug('### canada:',json);
	                                        if(json.exito)
	                                        {
	                                            if(json.smap1.fronterizo+'x'=='Sx')
	                                            {
	                                                me.setValue('S');
	                                            }
	                                            else
	                                            {
	                                                me.setValue('N');
	                                            }
	                                        }
	                                        else
	                                        {
	                                            mensajeError(json.respuesta);
	                                        }
	                                        if(!Ext.isEmpty(micallback))
	                                        {
	                                            micallback(_fieldLikeLabel('CANAD'));
	                                        }
	                                    }
	                                    ,failure : function()
	                                    {
	                                        me.setLoading(false);
	                                        errorComunicacion();
	                                    }
	                                });
	                            }
	                            else
	                            {
	                                if(!Ext.isEmpty(micallback))
	                                {
	                                    micallback(_fieldLikeLabel('CANAD'));
	                                }
	                            }
	                        }
	                        _fieldLikeLabel('CANAD').heredar();
	                    }
                    }
                }
                else
                {
                    mensajeError(_f1_json.respuesta);
                }
            }
            ,failure : function()
            {
                _0_panelPri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_0_cargarParametrizacionCoberturas');
}

/*
 * Esta funcion se usa para recuperar una poliza de SIGS para renovacion en ICE
 * solo debe llamarse si: es por flujo, y si no hay una cotizacion anterior
 * si se recupera cotizacion por _0_recuperarCotizacionDeTramite entonces
 * esta funcion no debe recuperar nada porque seria doble recuperacion, es decir
 * la condicion que tenga _0_recuperarCotizacionDeTramite debe estar negada aqui
 */
function _0_recuperarPolizaSIGS()
{
    if(!Ext.isEmpty(_0_flujo))
    {
        debug('_0_recuperarPolizaSIGS');
        
        var mask, ck = 'Recuperando cotizaci\u00f3n de tr\u00e1mite para revisar renovaci\u00f3n';
        
        try
        {
            mask = _maskLocal(ck);
            Ext.Ajax.request(
            {
                url      : _0_urlRecuperarOtvalorTramiteXDsatribu
                ,params  :
                {
                    'params.ntramite'  : _0_flujo.ntramite
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
                                    url      : _0_urlRecuperarDatosTramiteValidacion
                                    ,params  : _flujoToParams(_0_flujo)
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
                                                    
                                                    _0_cargarPoliza(
                                                        jsonDatTram.datosTramite.TRAMITE.RENUNIEXT
                                                        ,jsonDatTram.datosTramite.TRAMITE.RENRAMO
                                                        ,jsonDatTram.datosTramite.TRAMITE.RENPOLIEX
                                                        ,_0_smap1.cdusuari
                                                        ,'I'
                                                    );
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

function _0_cargarPoliza(cduniext,ramo,nmpoliex,cdusuari,tipoflot)
{
    debug('_0_cargarPoliza arguments:',arguments,'.');
    
    var mask, ck = 'Recuperando p\u00f3liza para renovaci\u00f3n';
    
    try
    {
        if(Ext.isEmpty(cduniext)
            ||Ext.isEmpty(ramo)
            ||Ext.isEmpty(nmpoliex)
            ||Ext.isEmpty(cdusuari)
            ||Ext.isEmpty(tipoflot)
        )
        {
            throw 'No hay datos suficientes para renovaci\u00f3n';
        }
        
        mask = _maskLocal(ck);
        
        Ext.Ajax.request(
        {
            url       : _0_urlCargarPoliza
            ,params  :
            {
                'smap1.cdsucursal' : cduniext
                ,'smap1.cdramo'     : ramo
                ,'smap1.cdpoliza'   : nmpoliex
                ,'smap1.cdusuari'   : cdusuari
                ,'smap1.tipoflot'   : tipoflot
                ,'smap1.cargaCotiza': 'S'
            }
            ,success : function(response)
            {
                mask.close();
                
                var ck = 'Decodificando respuesta al recuperar p\u00f3liza para renovaci\u00f3n';
                
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug("### poliza renovacion: ",json,'.');
                    
                    if(json.success === true)
                    {
                        var jsonSIGS = Ext.decode(json.smap1.valoresCampos);
                        debug('### datos SIGS:',jsonSIGS,'.');
                        
                        jsonSIGS.success = true;
                        
                        cargarXpoliza = true; // variable Jaime o_O
                        
                        llenandoCampos(jsonSIGS);
                        
                        /*json2['success']=true;
                        cdper     = json2.smap1.cdper;   //D00000000111005
                        cdperson  = json2.smap1.cdperson;//530400
                        debug("valoresCampos 2: ",json2);
                        llenandoCampos(json2);*/
                    }
                    else
                    {
                        mensajeError(json.respuesta);
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
                errorComunicacion(null,'Error al recuperar p\u00f3liza para renovaci\u00f3n');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,mask);
    }
}

function _0_atributoTipoPersona(combo)
{
    
    var val = 'F';
    
    if(combo != 'F')
    {
        val = combo.getValue();
    }
    
    debug('_0_atributoTipoPersona val:',val,'.');
    
    if(val == 'F')
        {
            if(!Ext.isEmpty(_fieldLikeLabel('SEGURO DE VIDA',null,true)))
            {
                _fieldLikeLabel('SEGURO DE VIDA').allowBlank=false;
                _fieldLikeLabel('SEGURO DE VIDA').show();
            }
            
            if(_fieldLikeLabel('SEGURO DE VIDA').getValue() == 'S')
            {
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=false;
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').show();
            }
        }
    else
        {
           if(!Ext.isEmpty(_fieldLikeLabel('SEGURO DE VIDA',null,true)))
           {
                _fieldLikeLabel('SEGURO DE VIDA').allowBlank=true;
                _fieldLikeLabel('SEGURO DE VIDA').hide();
                
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=true;
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').hide();
           }

        }
}

function _0_atributoNacimientoContratante(combo)
{
    var val = 'S';
    
    if(combo != 'S')
    {
        val = combo.getValue();
    }
    
    debug('_0_atributoNacimientoContratante val:',val,'.');
    
    if(val == 'S')
        {
            if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE',null,true)))
            {
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=false;
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').show();
                
                _fieldLikeLabel('EL CONTRATANTE PADECE').allowBlank=false;
                _fieldLikeLabel('EL CONTRATANTE PADECE').show();
            }
        }
    else
        {
           if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE',null,true)))
           {
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=true;
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').hide();
                
                _fieldLikeLabel('EL CONTRATANTE PADECE').allowBlank=true;
                _fieldLikeLabel('EL CONTRATANTE PADECE').hide();
           }

        }
}

function _0_nmpolizaChange(me)
{
    var sem = me.semaforo;
    if(Ext.isEmpty(sem)||sem==false)
    {
        me.sucio = true;
    }
    else
    {
        me.sucio = false;
    }
}

function _0_botonOnCotizarClic (me) {
    debug('_0_botonOnCotizarClic args:', arguments);
    var ck = 'Enviando datos de cotizaci\u00f3n';
    try {
        Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES + 'VentanaTurnado');
        new VentanaTurnado({
            cdtipflu  : _0_flujo.cdtipflu,
            cdflujomc : _0_flujo.cdflujomc,
            tipoent   : _0_flujo.tipoent,
            claveent  : 0,//_0_flujo.claveent,
            webid     : 0,//_0_flujo.webid,
            aux       : _0_flujoAux.onCotizar,
            ntramite  : _0_flujo.ntramite,
            status    : _0_flujo.status,
            cdunieco  : _0_flujo.cdunieco,
            cdramo    : _0_flujo.cdramo,
            estado    : _0_flujo.estado,
            nmpoliza  : _0_flujo.nmpoliza,
            nmsituac  : _0_flujo.nmsituac,
            nmsuplem  : _0_flujo.nmsuplem,
            cdusuari  : _0_smap1.cdusuari,
            cdsisrol  : _GLOBAL_CDSISROL
        }).mostrar();
    } catch (e) {
        manejaException(e, ck);
    }
}
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function()
{
    
    //_grabarEvento('COTIZACION','ACCCOTIZA',_0_smap1.ntramite,_0_smap1.cdunieco,_0_smap1.cdramo);
    
    Ext.Ajax.timeout = 5*60*1000;
    
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
	_0_validacion_custom = function()
    {
		mensajeWarning('Falta definir la validaci&oacute;n para el producto');
        return true;
    };
    <s:if test='%{getImap().get("validacionCustomButton")!=null}'>
	    var botonValidacionCustom = <s:property value="imap.validacionCustomButton" escapeHtml='false' />;
	     _0_validacion_custom=botonValidacionCustom.handler;
    </s:if>
    
    Ext.define('_0_modeloRecuperado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'NOMBRECLI'
            ,'RFCCLI'
            ,'CLAVECLI'
            ,'DIRECCIONCLI'
        ]
    });
    
    Ext.define('ModeloDetalleCotizacion',
    {
    	extend : 'Ext.data.Model'
    	,fields :
    	[
			'Codigo_Garantia'
			,{
				name : 'Importe'
				,type : 'float'
			}
			,'Nombre_garantia'
			,'cdtipcon'
			,'nmsituac'
			,'orden'
			,'parentesco'
			,'orden_parentesco'
        ]
    });
    
    var tmp = [];
    <s:if test='%{getImap().get("fieldsIndividuales")!=null}'>
        tmp.push(<s:property value="imap.fieldsIndividuales" />);
	</s:if>
	<s:if test='%{getImap().get("modeloExtraFields")!=null}'>
	    tmp.push(<s:property value="imap.modeloExtraFields"  />);
	</s:if>
	debug('_0_modelo fields:',tmp);
    Ext.define('_0_modelo',
    {
    	extend  : 'Ext.data.Model'
    	,fields : tmp
    });
    
    Ext.define('_0_modeloAgrupado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            <s:property value="imap.fieldsAgrupados"/>,'FESOLICI'
        ]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    _0_storeCoberturas=Ext.create('Ext.data.Store',
    {
        autoLoad : false
        ,model   : 'RowCobertura'
        ,sorters    :
        [
            {
                sorterFn : function(o1,o2)
                {
                    debug('sorting:',o1,o2);
                    if (o1.get('orden') === o2.get('orden'))
                    {
                        return 0;
                    }
                    return o1.get('orden')-0 < o2.get('orden')-0 ? -1 : 1;
                }
            }
        ]
        ,proxy   :
        {
            type    : 'ajax'
            ,url    : _0_urlCoberturas
            ,reader :
            {
                type  : 'json'
                ,root : 'listaCoberturas'
            }
        }
    });
    
    _0_storeIncisos = Ext.create('Ext.data.Store',
    {
    	model : '_0_modelo'
    });
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    Ext.define('_0_BotComprar',
    {
        extend    : 'Ext.Button'
        ,id       : '_0_botComprarId'
        ,text     : _0_smap1.ntramite?'Complementar tr&aacute;mite':'Generar tr&aacute;mite'
        ,icon     : '${ctx}/resources/fam3icons/icons/book_next.png'
        ,disabled : true
        ,handler  : _0_comprar
        ,hidden   : (_0_smap1.readOnly && _0_smap1.readOnly.length>0)
            || typeof _0_flujoAux.onCotizar === 'number'
    });
    
    Ext.define('_0_BotImprimir',
    {
        extend    : 'Ext.Button'
        ,id       : '_0_botImprimirId'
        ,text     : 'Imprimir'
        ,icon     : '${ctx}/resources/fam3icons/icons/printer.png'
        ,disabled : false
        ,handler  : _0_imprimir
    });
    
    Ext.define('_0_BotMail',
    {
        extend    : 'Ext.Button'
        ,id       : '_0_botMailId'
        ,text     : 'Enviar'
        ,icon     : '${ctx}/resources/fam3icons/icons/email.png'
        ,disabled : true
        ,handler  : _0_mail
    });
    
    Ext.define('_0_BotCoberturas',
    {
        extend    : 'Ext.Button'
        ,id       : '_0_botCoberturasId'
        ,text     : 'Coberturas'
        ,icon     : '${ctx}/resources/fam3icons/icons/table.png'
        ,disabled : true
        ,handler  : _0_coberturas
    });
    
    
    Ext.define('_0_BotDetalles',
    {
        extend    : 'Ext.Button'
        ,id       : '_0_botDetallesId'
        ,text     : 'Detalles'
        ,icon     : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
        ,disabled : true
        ,hidden   : ocultarDetalleCotizacion  
        ,handler  : _0_detalles
    });
    
    Ext.define('_0_BotNueva',
    {
    	extend   : 'Ext.Button'
    	,id      : '_0_botNuevaId'
        ,text    : 'Nueva'
        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
        ,handler : _0_nueva
    });
    
    Ext.define('_0_BotClonar',
    {
    	extend   : 'Ext.Button'
        ,id      : '_0_botClonarId'
        ,text    : 'Clonar'
        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
        ,handler : _0_clonar
    });
    
    Ext.define('_0_BotEditar',
    {
    	extend   : 'Ext.Button'
        ,id      : '_0_botEditarId'
        ,text    : 'Editar'
        ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
        ,handler : _0_editar
    });
    
    Ext.define("_0_FormAgrupados",
    {
    	extend         : 'Ext.form.Panel'
    	,title         : 'Datos generales de '+(_0_smap1.MODALIDAD.toLowerCase())
    	,initComponent : function()
    	{
    		debug('_0_FormAgrupados initComponent');
    		
    		var itemsFormAgrupados=[
    	            		 _0_fieldNtramite
    	    			    ,_0_fieldNmpoliza
    	    			    <s:if test='%{imap.containsKey("tatripol")}'>
    	    			    ,<s:property value="imap.tatripol"/>
    	    			    </s:if>
    	    			    ,<s:property value="imap.camposAgrupados"/>
    	    			    ,{
    	                        name        : 'FESOLICI'
    	                        ,fieldLabel : 'FECHA DE SOLICITUD'
    	                        ,xtype      : 'datefield'
    	                        ,format     : 'd/m/Y'
    	                        ,editable   : true
    	                        ,allowBlank : false
    	                        ,value      : new Date()
    	                    }
    	    			    ,{
    	                        id          : 'fechaInicioVigencia'
    	                        ,name       : 'feini'
    	                        ,fieldLabel : 'INICIO DE VIGENCIA'
    	                        ,xtype      : 'datefield'
    	                        ,format     : 'd/m/Y'
    	                        ,editable   : true
    	                        ,allowBlank : false
    	                        ,value      : new Date()
    	                        ,listeners  :
    	                        {
    	                            change : _0_funcionFechaChange
    	                        }
    	                    }
    	                    ,{
    	                        id          : 'fechaFinVigencia'
    	                        ,name       : 'fefin'
    	                        ,fieldLabel : 'FIN DE VIGENCIA'
    	                        ,xtype      : 'datefield'
    	                        ,format     : 'd/m/Y'
    	                        ,readOnly   : ('|AF|PU|'.lastIndexOf('|'+_0_smap1.cdtipsit+'|')==-1)//FALSE <<< deshabilita solo lectura
    	                        ,allowBlank : false
    	                        ,value      : Ext.Date.add(new Date(),Ext.Date.YEAR,1)
    	                        ,minValue   : Ext.Date.add(new Date(),Ext.Date.DAY,1)
    	                    }
    	            			];
    	
    	    if(_0_smap1.SITUACION=='AUTO' && _0_smap1.cdtipsit!='AT' && _0_smap1.cdtipsit!='MC'){
    	    	itemsFormAgrupados.splice(itemsFormAgrupados.length-2,0,{
    	        	
    	        	xtype		: 'fieldset'
    	        	,itemId     : 'fieldsetDxn'
    	        	,width : 435
    	        	,title		: '<span style="font:bold 14px Calibri;">DXN</span>'
    	        	,items		: _p28_panelDxnItems
    	        });
    	    }
    		Ext.apply(this,
    		{
    			defaults :
    			{
    				style : 'margin : 5px;'
    			}
    			,items   :itemsFormAgrupados
    			
    		});
    		this.callParent();
    	}
    });
    
    var tmp = [
		{
	        dataIndex     : 'contador'
	        ,width        : 30
	        ,menuDisabled : true
	    }
	];
    <s:if test='%{getImap().get("camposIndividuales")!=null}' >
       tmp.push(<s:property value="imap.camposIndividuales"/>);
    </s:if>
    <s:if test='%{getImap().get("modeloExtraColumns")!=null}' >
       tmp.push(<s:property value="imap.modeloExtraColumns" />);
    </s:if>
    tmp.push(
    {
        xtype  : 'actioncolumn'
        ,width : 30
        ,menuDisabled : true
        ,sortable : false
        ,icon : '${ctx}/resources/fam3icons/icons/delete.png'
        ,handler : function(grid,rowIndex,colIndex)
        {
            _0_storeIncisos.removeAt(rowIndex);
            var contador=1;
            _0_storeIncisos.each(function(record)
            {
                record.set('contador',contador);
                contador=contador+1;
            });
        }
    });
    debug('_0_GridIncisos columns:',tmp);
    Ext.define('_0_GridIncisos',
    {
        extend         : 'Ext.grid.Panel'
        ,initComponent : function()
        {
            debug('_0_GridIncisos initComponent');
            Ext.apply(this,
            {
            	title        : 'Datos de incisos'
            	,store       : _0_storeIncisos
            	,minHeight   : 170
            	//,hidden      : !_0_necesitoIncisos
            	,tbar        :
            	[
            	    {
            	    	text     : 'Agregar'
            	    	,icon    : '${ctx}/resources/fam3icons/icons/add.png'
            	    	,handler : _0_agregarAsegu
            	    }
            	]
                ,columns     : tmp
                ,plugins     :
                [
                    _0_rowEditing
                ]
            });
            this.callParent();
        }
    });
    
    Ext.define('_0_Botonera',
    {
    	extend         : 'Ext.panel.Panel'
    	,initComponent : function()
    	{
    		Ext.apply(this,
    		{
                buttonAlign : 'center'
                ,border     : 0
		        ,buttons    :
		        [
		            _0_botCotizar
		            ,_0_botLimpiar
		            ,_0_botCargar
		            ,_0_botCargarPoliza
		            //>agregado para cancelar un tramite
		            ,{
		                text     : 'Rechazar'
		                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
		                ,hidden  : (!_0_smap1.ntramite) || _0_smap1.ntramite.length==0
		                ,handler : function()
		                {
		                    //debug(form.getValues());
		                    Ext.create('Ext.window.Window',
		                    {
		                        title        : 'Guardar detalle'
		                        ,width       : 600
		                        ,height      : 430
		                        ,buttonAlign : 'center'
		                        ,modal       : true
		                        ,closable    : false
		                        ,autoScroll  : true
		                        ,items       :
		                        [
		                            {
		                                id        : 'inputTextareaCommentsToRechazo'
		                                ,width  : 570
		                                ,height : 300
		                                ,xtype  : 'textarea'
		                            }
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
						                        ,itemId     : 'SWAGENTE'
						                        ,name       : 'SWAGENTE'
						                        ,inputValue : 'S'
						                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
						                    }
						                    ,{
						                        boxLabel    : 'No'
						                        ,name       : 'SWAGENTE'
						                        ,inputValue : 'N'
                                                ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
						                    }
						                ]
						            }
		                        ]
		                        ,buttons    :
		                        [
		                            {
		                                text     : 'Rechazar'
		                                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
		                                ,handler : function()
		                                {
		                                    debug('rechazar');
		                                    var window=this.up().up();
		                                    window.setLoading(true);
		                                    Ext.Ajax.request
		                                    ({
		                                        url     : _0_urlUpdateStatus
		                                        ,params : 
		                                        {
		                                            'smap1.ntramite' : _0_smap1.ntramite
		                                            ,'smap1.status'  : '4'//rechazado
		                                            ,'smap1.comments' : Ext.getCmp('inputTextareaCommentsToRechazo').getValue()
		                                            ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
		                                        }
		                                        ,success : function(response)
		                                        {
		                                            window.setLoading(false);
		                                            var json=Ext.decode(response.responseText);
		                                            if(json.success==true)
		                                            {
		                                                Ext.create('Ext.form.Panel').submit
		                                                ({
		                                                    url             : _0_urlMesaControl
		                                                    ,standardSubmit : true
                                                            ,params         :
                                                            {
                                                                'smap1.gridTitle'      : 'Tareas'
                                                                ,'smap2.pv_cdtiptra_i' : 1
                                                                ,'smap1.editable'      : 1
                                                            }
		                                                });
		                                            }
		                                            else
		                                            {
		                                                Ext.Msg.show({
		                                                    title:'Error',
		                                                    msg: 'Error al rechazar',
		                                                    buttons: Ext.Msg.OK,
		                                                    icon: Ext.Msg.ERROR
		                                                });
		                                            }
		                                        }
		                                        ,failure : function()
		                                        {
		                                            window.setLoading(false);
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
		                            ,{
		                                text  : 'Cancelar'
		                                ,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
		                                ,handler : function()
		                                {
		                                    this.up().up().destroy();
		                                }
		                            }
		                        ]
		                    }).show();
		                }
		            }
		            //<agregado para cancelar un tramite
		        ]
    		});
    		this.callParent();
    	}
    });
    
    Ext.define('_0_PanelPri',
    {
    	extend         : 'Ext.panel.Panel'
    	,initComponent : function()
        {
    		debug('_0_panelPri initComponent');
    		Ext.apply(this,
    		{
    			renderTo  : '_0_divPri'
   		        ,defaults :
   		        {
   		            style : 'margin:5px;'
   		        }
   		        ,border   : 0
   		        ,items    :
   		        [
   		            Ext.create('Ext.panel.Panel',
		            {
		                itemId       : '_0_panelFlujo'
		                ,title       : 'ACCIONES'
		                ,hidden      : Ext.isEmpty(_0_flujo) || !_0_smap1.SITUACION === 'AUTO'
		                ,buttonAlign : 'left'
		                ,buttons     : []
		                ,listeners   :
		                {
		                    afterrender : function(me)
		                    {
		                        if(!Ext.isEmpty(_0_flujo))
		                        {
		                            _cargarBotonesEntidad(
		                                _0_flujo.cdtipflu
		                                ,_0_flujo.cdflujomc
		                                ,_0_flujo.tipoent
		                                ,_0_flujo.claveent
		                                ,_0_flujo.webid
		                                ,me.itemId//callback
		                                ,_0_flujo.ntramite
		                                ,_0_flujo.status
		                                ,_0_flujo.cdunieco
		                                ,_0_flujo.cdramo
		                                ,_0_flujo.estado
		                                ,_0_flujo.nmpoliza
		                                ,_0_flujo.nmsituac
		                                ,_0_flujo.nmsuplem
		                                ,null//callbackDespuesProceso
		                            );
		                        }
		                    }
		                }
		            })
   		            ,_0_formAvisos
   		            ,_0_formAgrupados
   		            ,_0_gridIncisos
   		            ,_0_botonera
   		        ]
    		});
    		this.callParent();
        }
    });
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    var auxItemsAvisos = [];
    <s:if test='%{getActionErrors()!=null&&getActionErrors().size()>0}' >
        <s:iterator value="actionErrors" var="iError">
            auxItemsAvisos.push(
            {
                xtype     : 'panel'
                ,layout   : 'hbox'
                ,border   : 0
                ,defaults : { style : 'margin : 5px;' }
                ,items    :
                [
                    {
                        xtype   : 'image'
                        ,src    : '${ctx}/resources/fam3icons/icons/error.png'
                        ,width  : 16
                        ,height : 16
                    }
                    ,{
                        xtype  : 'label'
                        ,text  : '<s:property value="iError" escapeHtml="false" />'
                        ,style : 'color:red;margin:5px;'
                    }
                ]
            });
        </s:iterator>
    </s:if>
    _0_formAvisos=Ext.create('Ext.panel.Panel',
    {
        hidden    : <s:property value='%{getActionErrors()!=null&&getActionErrors().size()>0?false:true}' />
        ,defaults : { style : 'margin : 5px;'}
        ,border   : 0
        ,items    : auxItemsAvisos
    });
    
    _0_windowAyudaCobertura = new Ext.Window(
    {
    	title        : 'Detalle de cobertura'
    	,width       : 450
    	,height      : 350
    	,bodyStyle   : 'background:white'
    	,overflow    : 'auto'
    	,modal       : true
    	,autoScroll  : true
    	,buttonAlign : 'center'
    	,closable    : false
    	,buttons     :
    	[
    	    {
    	    	text     : 'Cerrar'
    	    	,handler : function()
    	    	{
    	    		this.up().up().hide();
    	    	}
    	    }
    	]
    });
    
    _0_botDetalleCobertura=Ext.create('Ext.Button',
    {
        text     : 'Ver detalle'
        ,icon    : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
        ,handler : _0_detallesCobertura
    });
    
    _0_gridCoberturas=Ext.create('Ext.grid.Panel',
    {
        title        : 'Sin plan'
        ,store       : _0_storeCoberturas
        ,height      : 300
        ,selType     : 'cellmodel'
        ,buttonAlign : 'center'
        ,buttons     : [ _0_botDetalleCobertura ]
        ,columns     :
        [
            {
                dataIndex : 'dsGarant'
                ,text : 'Cobertura'
                ,flex : 3
            }
            ,{
                dataIndex : 'sumaAsegurada'
                ,text : 'Suma asegurada'
                ,flex : 1
            }
            ,{
                dataIndex : 'deducible'
                ,text : 'Deducible'
                ,flex : 1
            }
        ]
        ,listeners   :
        {
            itemclick : function(dv, record, item,index, e)
            {
                var y = this.getSelectionModel().getCurrentPosition().row;
                var x = this.getSelectionModel().getCurrentPosition().column;
                if (x == 0)
                {
                	_0_selectedIdcobertura=record.get('cdGarant');
                    _0_botDetalleCobertura.setDisabled(false);
                }
                else
                {
                    _0_botDetalleCobertura.setDisabled(true);
                }
            }
        }
    });
    
    _0_windowCoberturas = new Ext.Window(
    {
        plain        : true
        ,width       : 500
        ,height      : 400
        ,modal       : true
        ,autoScroll  : true
        ,title       : 'Coberturas'
        ,layout      : 'fit'
        ,bodyStyle   : 'padding:5px;'
        ,buttonAlign : 'center'
        ,closeAction : 'hide'
        ,closable    : true
        ,items       : [ _0_gridCoberturas ] 
        ,buttons     :
        [
            {
            	text     : 'Regresar'
            	,icon    : '${ctx}/resources/fam3icons/icons/arrow_left.png'
            	,handler : function()
            	{
                    this.up().up().hide();
                }
            }
        ]
    });
    
    _0_fieldNtramite=Ext.create('Ext.form.field.Number',
	{
	    name        : 'ntramite'
	    ,fieldLabel : 'TR&Aacute;MITE'
	    ,hidden     : !(_0_smap1.ntramite&&_0_smap1.ntramite>0)
	    ,readOnly   : true
	    ,value      : _0_smap1.ntramite
	});
    
    _0_fieldNmpoliza=Ext.create('Ext.form.field.Number',
    {
        name        : 'nmpoliza'
        ,fieldLabel : 'COTIZACI&Oacute;N'
        ,readOnly   : true
    });
    
    _0_botCotizar=Ext.create('Ext.Button',
    {
        text     : _0_smap1.ntramite?'Precaptura':'Cotizar'
        ,icon    : '${ctx}/resources/fam3icons/icons/calculator.png'
        ,handler : function (me) {
            sinTarificar = false;
            _0_cotizar(me);
        }
    });
    
    _0_botCargar=Ext.create('Ext.Button',
    {
        text     : 'Cargar'
        ,icon    : '${ctx}/resources/fam3icons/icons/database_refresh.png'
        ,handler : _0_cargar
    });
    
    _0_botCargarPoliza=Ext.create('Ext.Button',
    	    {
    	        text     : 'Cargar Poliza'
    	        ,icon    : '${ctx}/resources/fam3icons/icons/database_refresh.png'
    	        ,handler : _0_cargarPoliza
    	        ,hidden  : true
    	    });
    
    _0_botLimpiar=Ext.create('Ext.Button',
    {
        text     : 'Limpiar'
        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
        ,handler : _0_limpiar
    });
    
    _0_formAgrupados = new _0_FormAgrupados();
    _0_gridIncisos   = new _0_GridIncisos();
    
    //[parche] para AF y PU
    if(_0_smap1.cdtipsit=='AF' || _0_smap1.cdtipsit=='PU')
    {
        //OCULTAR parametros.pv_otvalor20 CUANDO EL TIPO DE PERSONA SEA DIFERENTE DE FISICA
        try{
            var tipoPersona = _fieldByLabel('TIPO PERSONA',null,true);
            tipoPersona.on({
                'change':function(me){
                    if(me.getValue()!= TipoPersona.Fisica){
                        _fieldByName('parametros.pv_otvalor20').setValue('N');
                        _fieldByName('parametros.pv_otvalor20').hide();
                        _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').setValue(null);
                        _fieldLikeLabel('EL CONTRATANTE PADECE').setValue(null);
                        _fieldLikeLabel('SEGURO DE VIDA').setValue('N');
                        _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').hide();
                        _fieldLikeLabel('EL CONTRATANTE PADECE').hide();
                        _fieldLikeLabel('SEGURO DE VIDA').hide();
                        _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=true;
                        _fieldLikeLabel('EL CONTRATANTE PADECE').allowBlank=true;
                        _fieldLikeLabel('SEGURO DE VIDA').allowBlank=true;
                    }else{
                        _fieldByName('parametros.pv_otvalor20').show();
                    }
                    _0_atributoTipoPersona(me);
                }
            });
        }catch(e){
            
            debugError(e);
        }
        _0_gridIncisos.setTitle('Datos del contratante prospecto');
        _0_formAgrupados.down('[name=parametros.pv_otvalor03]').addListener('change',function()
        {
            debug('cleaning');
            _0_formAgrupados.down('[name=parametros.pv_otvalor04]').setValue('');
            _0_formAgrupados.down('[name=parametros.pv_otvalor05]').setValue('');
            _0_formAgrupados.down('[name=parametros.pv_otvalor06]').setValue('');
            _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setValue('');
            _0_formAgrupados.down('[name=parametros.pv_otvalor26]').setValue('');
        });
        _0_formAgrupados.down('[name=parametros.pv_otvalor31]').addListener('change',function()
        {
            _0_formAgrupados.down('[name=parametros.pv_otvalor03]').setValue('');
        });
        _0_formAgrupados.down('[name=parametros.pv_otvalor08]').addListener('change',function()
        {
        	_0_formAgrupados.down('[name=parametros.pv_otvalor03]').setValue('');
        });
        _0_formAgrupados.down('[name=parametros.pv_otvalor03]').addListener('blur',function()
        {
        	if(Ext.isEmpty(_0_formAgrupados.down('[name=parametros.pv_otvalor08]').getValue()) || !_0_formAgrupados.down('[name=parametros.pv_otvalor08]').isValid()){
        		mensajeWarning('Debe de capturar primero el C&oacute;digo Postal');
                return;
        	}
        	
            var vim = this.value;
            if( (this.minLength > 0 && vim.length < this.minLength) || (vim.length < this.minLength || vim.length > this.maxLength) )
            {
            	if(this.minLength == this.maxLength) {
            		mensajeWarning('La longitud del n&uacute;mero de serie debe ser ' + this.minLength);
            	} else {
            		mensajeWarning('La longitud del n&uacute;mero de serie debe ser entre ' + this.minLength + ' y ' + this.maxLength);
            	}
                return;
            }
            debug('>llamando a nada:',vim);
            _0_formAgrupados.setLoading(true);
            Ext.Ajax.request(
            {
                url     : _0_urlNada
                ,params :
                {
                    'smap1.vim'       : vim
                    ,'smap1.cdramo'   : _0_smap1.cdramo
                    ,'smap1.cdtipsit' : _0_smap1.cdtipsit
                    ,'smap1.tipoveh'  : _0_formAgrupados.down('[name=parametros.pv_otvalor31]').getValue()
                    ,'smap1.codpos'   : _0_formAgrupados.down('[name=parametros.pv_otvalor08]').getValue()
                }
                ,success : function(response)
                {
                    _0_formAgrupados.setLoading(false);
                    var json = Ext.decode(response.responseText);
                    debug('nada response:', json);
                    if(json.success)
                    {
                        var precioDolar = _0_formAgrupados.down('[name=parametros.pv_otvalor24]').getValue()-0;
                        debug('precioDolar:',precioDolar);
                        _0_formAgrupados.down('[name=parametros.pv_otvalor04]').setValue(json.smap1.AUTO_MARCA);
                        _0_formAgrupados.down('[name=parametros.pv_otvalor05]').setValue(json.smap1.AUTO_ANIO);
                        _0_formAgrupados.down('[name=parametros.pv_otvalor06]').setValue(json.smap1.AUTO_DESCRIPCION);
                        _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setMinValue(((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1-(json.smap1.FACTOR_MIN-0)));
                        _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setMaxValue(((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1+(json.smap1.FACTOR_MAX-0)));
                        _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setValue((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2));
                        _0_formAgrupados.down('[name=parametros.pv_otvalor26]').setValue((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2));
                        debug('set min value:',((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1-(json.smap1.FACTOR_MIN-0)));
                        debug('set max value:',((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1+(json.smap1.FACTOR_MAX-0)));
                        
                        Ext.Ajax.request({
            				url     : _0_urlObtieneValNumeroSerie
            				,params :
            				{
            					'smap1.numSerie'  :  _0_formAgrupados.down('[name=parametros.pv_otvalor03]').getValue()
            					,'smap1.feini'   :  Ext.getCmp('fechaInicioVigencia').getValue()
            				}
            				,success : function(response)
            				{
            					var json=Ext.decode(response.responseText);
            					debug(json);
                    	    	if(json.exito!=true)
                    	    	{
                    	    		//if(_0_smap1.cdsisrol!='SUSCRIAUTO')
//                                  if(rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')==-1)        
	                                if(!RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol))
                    	    		{
                    	    			mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", json.respuesta);
                    				}else{
                    					mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", json.respuesta);
                    				}
                    	    	}
            				}
            				,failure : errorComunicacion
            			});
                    }
                    else
                    {
                    	//parche para RAMO 16 (FRONTERIZOS) con rol SUSCRIPTOR AUTO, no se lanza la validación:
                    	if(_0_smap1.cdramo == Ramo.AutosFronterizos && 
                    			//_0_smap1.cdsisrol == 'SUSCRIAUTO'
//                     			rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')!=-1  
                    			RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol)
                    			) {
                    	    // Si no obtuvo datos el servicio "NADA", reseteamos valores:
                    		_0_formAgrupados.down('[name=parametros.pv_otvalor04]').setValue();
                            _0_formAgrupados.down('[name=parametros.pv_otvalor05]').setValue();
                            _0_formAgrupados.down('[name=parametros.pv_otvalor06]').setValue();
                            _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setValue();
                            _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setMinValue();
                            _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setMaxValue();
                            _0_formAgrupados.down('[name=parametros.pv_otvalor26]').setValue();
                    	} else {
                    		mensajeError(json.error);
                    	}
                    }
                }
                ,failure : function()
                {
                    _0_formAgrupados.setLoading(false);
                    debug("Entra a esta parte");
                    errorComunicacion();
                }
            });
            debug('<llamando a nada');            
        });
        var comboTipoValor =_0_formAgrupados.down('[name=parametros.pv_otvalor02]');
        var itemSumaAsegu  =_0_formAgrupados.down('[name=parametros.pv_otvalor07]');
        var changeFunction = function()
        {
            debug('>comboTipoValor change');
            //if(_0_smap1.cdsisrol!='SUSCRIAUTO')
//          if(rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')==-1)
	        if(!RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol))
            {
            	itemSumaAsegu.setValue('');
                itemSumaAsegu.setReadOnly((comboTipoValor.getValue()+'x')=='2x');
            }
            debug('<comboTipoValor change');
        };
        comboTipoValor.addListener('change',changeFunction);
        changeFunction();
        
        Ext.Ajax.request(
        {
            url      : _0_urlObtenerParametros
            ,params  :
            {
                'smap1.parametro' : 'RANGO_ANIO_MODELO'
                ,'smap1.cdramo'   : _0_smap1.cdramo
                ,'smap1.cdtipsit' : _0_smap1.cdtipsit
                ,'smap1.clave4'   : _0_smap1.cdsisrol
            }
            ,success : function(response)
            {
                var json=Ext.decode(response.responseText);
                debug('### obtener rango años response:',json);
                if(json.exito)
                {
                    var limiteInferior = json.smap1.P1VALOR-0;
                    var limiteSuperior = json.smap1.P2VALOR-0;
                    _0_formAgrupados.down('[name=parametros.pv_otvalor05]').validator=function(value)
                    {
                        var r = true;
                        var anioActual = new Date().getFullYear();
                        var max = anioActual+limiteSuperior;
                        var min = anioActual+limiteInferior;
                        debug('limiteInferior:',limiteInferior);
                        debug('limiteSuperior:',limiteSuperior);
                        debug('anioActual:',anioActual);
                        debug('max:',max);
                        debug('min:',min);
                        debug('value:',value);
                        if(value<min||value>max)
                        {
                            r='El modelo debe estar en el rango '+min+'-'+max;
                        }
                        return r;
                    };
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                errorComunicacion();
            }
        });
        
        if(_0_smap1.cdsisrol=='EJECUTIVOCUENTA')
        {
            _0_recuperarDescuento();
        }
        else
        {
            _fieldLikeLabel('AGENTE').on(
            {
                select : function()
                {
                    _0_recuperarDescuento();
                }
            });
        }
        
        var negoCmp = _fieldByLabel('TIPO SERVICIO');
        var negoVal = negoCmp.getValue();
        negoCmp.setLoading(true);
        
     _fieldByName('feini').on
     ({
         change : function ()
         { //fefin.setValue(Ext.Date.add(val,Ext.Date.YEAR,1));
//           alert("Hello2:",plazoenanios);
             if(_0_smap1.SITUACION=='AUTO' && _0_smap1.cdtipsit!='AT' && _0_smap1.cdtipsit!='MC'){
                 
                 if(_fieldByName('fefin').readOnly){
                     
                   
                     return ;
                 }
             }
            if(!Ext.isEmpty(plazoenanios))
            	{
		          debug('plazoanios',plazoenanios);
		          debug('antes de setMaxValue:', _fieldByName('fefin'))
		        	 _fieldByName('fefin').setMaxValue
		        	 (
		        		 Ext.Date.add(_fieldByName('feini').getValue(),
		        				      Ext.Date.YEAR,plazoenanios
		        				     )
		            );
		          _fieldByName('fefin').isValid();
		        	debug('despues de setMaxValue:', _fieldByName('fefin'))
		         }
         }
     });
     
     
     //DXN custom
    
     if(_0_smap1.SITUACION=='AUTO' && _0_smap1.cdtipsit!='AT' && _0_smap1.cdtipsit!='MC'){
         administradoraAgenteDXN();
	     agregarAgenteDXN();
	     
	    
	         _fieldByLabel('AGENTE').on({
	             select:function(combo,row){
	                 
	                 aplicaDxn(row[0].data.key);
	                 agregarAgenteDXN();
	                 administradoraAgenteDXN();
	             }
	         });
	     
	     
	     _fieldByName("aux.otvalor08").getStore().filter([{filterFn: function(item) {
	        
	        return item.get("key") < 1000; }}]);
	     
	     _fieldByName("aux.otvalor09").on({
	     	change:function(t,e,o){
	     		
	     		try{
		     		var admin=_fieldByName("aux.otvalor08").getValue();
		     		var ret=_fieldByName("aux.otvalor09").getValue();
		     		_fieldByName("fefin").setDisabled(false);
		     		if(ret!=null){
		     			formaPago=getFormaPago(admin,ret);
		     			
		     			var fecha_termino=formaPago.fecha_termino;
		                if((fecha_termino+'').trim()!=''){
		                    fecha_termino= Ext.Date.parse(formaPago.fecha_termino, "d/m/Y");
		                    if(fecha_termino>new Date()){
		                        _fieldByName("fefin").setValue(formaPago.fecha_termino);
		                         _fieldByName("fefin").setReadOnly(true);
		                         _fieldByName("fefin").setMaxValue(formaPago.fecha_termino);
		                           _fieldByName("fefin").setMinValue(formaPago.fecha_termino);
		                         _fieldByName("fefin").validator=function(){ return true;}
		                         _fieldByName("fefin").isValid();
		                        
		                    }else{
		                        _fieldByName("fefin").setReadOnly(false);
		                        _fieldByName("feini").fireEvent('change',_fieldByName("feini"),_fieldByName("feini").getValue());
		                        //_fieldByLabel('NEGOCIO').fireEvent('change',_fieldByLabel('NEGOCIO'),_fieldByLabel('NEGOCIO').getValue());
		                        obtienefechafinplazo();
		                    }
		                    
		                }else{
		                    _fieldByName("fefin").setReadOnly(false);
		                    _fieldByName("feini").fireEvent('change',_fieldByName("feini"),_fieldByName("feini").getValue());
		                    //_fieldByLabel('NEGOCIO').fireEvent('change',_fieldByLabel('NEGOCIO'),_fieldByLabel('NEGOCIO').getValue());
		                    obtienefechafinplazo();
		                }
		     			
		     			
	// 	     			 _fieldByName("fefin").setValue(formaPago.fecha_termino);
	// 	     			 _fieldByName("fefin").setDisabled(true);
		     		}
	     		}catch(e){
	     			debugError(e);
	     		}
	     		
	     	}	
	     }
	     );
	     aplicaDxn();
     }
     
     /////

//      _fieldByName('feini').on(
//     	        {
//     	            change : _0_calculaVigencia
//     	        });
     
//        _fieldByName('fefin').on(
//                 {
//                     change : _0_calculaVigencia
//                 });
            
        obtienefechafinplazo();
//        _0_calculaVigencia();
       
       retroactividadfechaini();
        
        var agente = _fieldByName('parametros.pv_otvalor17');
        agente.on(
                {
                    'select' : retroactividadfechaini
                });
        
        var tipoServicio = _fieldLikeLabel('TIPO SERVICIO',null,true);
        tipoServicio.on(
		        		{
		        			change : function(){ _0_cargarParametrizacionCoberturas();}
		        	    }
		        	   );
//         { _f1_tipoServicio = _fieldByLabel('TIPO SERVICIO').getValue();}
        var modelo = _fieldLikeLabel('MODELO',null,true);
        modelo.on(
                        {
                        	'blur' : function(){ _0_cargarParametrizacionCoberturas();}
                        }
                       );
        
        if(!Ext.isEmpty(_fieldLikeLabel('EL CONTRATANTE PADECE',null,true)))
       	{
                    _fieldLikeLabel('EL CONTRATANTE PADECE').hide();
       	}
        
        if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE',null,true)))
        {
                    _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').hide();
        }
        
        if(!Ext.isEmpty(_fieldLikeLabel('SEGURO DE VIDA',null,true)))
        {
                       _fieldLikeLabel('SEGURO DE VIDA').setValue('N');
//                     _fieldLikeLabel('SEGURO DE VIDA').hide();
        }
        
        if(!Ext.isEmpty(_fieldLikeLabel('TIPO PERSONA',null,true)))
        {
            _fieldByLabel('TIPO PERSONA').on(
                            {
                                select : _0_atributoTipoPersona
                            });
        }
        
        if(!Ext.isEmpty(_fieldLikeLabel('SEGURO DE VIDA',null,true)))
        {
        	_fieldLikeLabel('SEGURO DE VIDA').on(
                            {
                                select : _0_atributoNacimientoContratante
                            });
        }
        
        Ext.Ajax.request(
                {
                    url      : _0_urlCargarCatalogo
                    ,params  :
                    {
                         'catalogo'        : 'RAMO_5_TIPOS_VALOR_X_ROL'
                        ,'params.cdtipsit' : _0_smap1.cdtipsit
                    }
                    ,success : function(response)
                    {
                        setTimeout(
                    		function ()
                    		{
//                     			alert('timer');
		                        _fieldByLabel('TIPO VALOR').setLoading(false);
		                        var json=Ext.decode(response.responseText);
		                        debug('respuesta json obtener rango TIPO VALOR:',json);
		                        
	                        	//VILS
	                            var tipoValor =_fieldByLabel('TIPO VALOR');
	//                          _fieldByName('parametros.pv_otvalor01');
	//                         	_0_formAgrupados.down('[name=parametros.pv_otvalor02]');
	                        	
	                        	debug('vilsss:',tipoValor);
	                        	tipoValor.getStore().removeAll();
	                        	for(var i=0 ; i<json.lista.length; i++)
	                       		{
	                        	    tipoValor.getStore().add(json.lista[i]);
	                       		}
                    			
                    		},
                    		1500
                    	);
                    	
                    }
                    ,failure : function()
                    {
                        _fieldByLabel('TIPO VALOR').setLoading(false);
                        errorComunicacion();
                    }
                });
    }
    //fin [parche]
    
    //parche para AUTOS FRONTERIZOS Y PICKUP Fronterizos con rol SUSCRIPTOR AUTO:
    if((_0_smap1.cdtipsit == TipoSituacion.AutosFronterizos || _0_smap1.cdtipsit == TipoSituacion.AutosPickUp) 
        && 
//         (rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')!=-1)
        RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol)
        ) 
    {
        _0_formAgrupados.down('[name=parametros.pv_otvalor04]').setReadOnly(false);
        _0_formAgrupados.down('[name=parametros.pv_otvalor05]').setReadOnly(false);
        _0_formAgrupados.down('[name=parametros.pv_otvalor06]').setReadOnly(false);
        _0_formAgrupados.down('[name=parametros.pv_otvalor07]').setReadOnly(false);
        _0_formAgrupados.down('[name=parametros.pv_otvalor26]').setReadOnly(false);
    }
    
    <s:if test='%{getSmap1().get("CDATRIBU_DERECHO")!=null}'>
        var items=_0_formAgrupados.items.items;
        debug('items a reordenar:',items);
        var cdatribus_derechos=_0_smap1.CDATRIBU_DERECHO.split(',');
        debug('cdatribus_derechos:',cdatribus_derechos);
        var itemsIzq=[];
        var itemsDer=[];
        itemsIzq.push
        ({
           xtype  : 'fieldset'
          ,itemId : '_p28_fieldBusquedaPoliza'
          ,width  : 435
          ,title  : '<span style="font:bold 14px Calibri;">RENOVAR POR POLIZA</span>'
          ,items  : _0_panel7Items
          ,hidden : !Ext.isEmpty(_0_flujo) ? (_0_flujo.cdflujomc != 220 && _0_flujo.cdtipflu != 103 && _0_smap1.cdramo == Ramo.AutosFronterizos) : true 
        });
        for(var i=0;i<items.length;i++)
        {
            var iItem=items[i];
            debug('item revisado:',iItem);
            var indexOfIItem=$.inArray(iItem.cdatribu,cdatribus_derechos);
            debug('indexOfIItem:',indexOfIItem);
            if(indexOfIItem==-1)
            {
                debug('izquierdo');
                itemsIzq.push(iItem);
            }
            else
            {
                debug('derecho');
                itemsDer.push(iItem);
            }
        }
        _0_formAgrupados.removeAll(false);
        _0_formAgrupados.layout='hbox';
        _0_formAgrupados.add(
        [
            {
                xtype  : 'fieldset'
                ,title : '<span style="font:bold 14px Calibri;">DATOS GENERALES</span>'
                ,items : itemsIzq
            }
            ,{
                xtype  : 'fieldset'
                ,title : '<span style="font:bold 14px Calibri;">DATOS DE COBERTURAS</span>'
                ,items : itemsDer
            }
        ]);
    </s:if>
    _0_botonera      = new _0_Botonera();
    _0_panelPri      = new _0_PanelPri();    
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
    //[parche] para ramo 6
    if(_0_smap1.cdramo+'x'=='6x')
    {
        debug('>parche para ramo 6');
        
        //negocio
        _fieldLikeLabel('NEGOCIO').on(
        {
            focus : function()
            {
                var valido = !Ext.isEmpty(_fieldByLabel('TIPO DE UNIDAD').getValue());
                if(!valido)
                {
                    mensajeWarning('Seleccione el tipo de unidad');
                }
                
                if(valido)
                {
                    valido = !Ext.isEmpty(_fieldByLabel('AGENTE').getValue());
                    if(!valido)
                    {
                        mensajeWarning('Seleccione el agente');
                    }
                }
                
                if(valido)
                {
                    _fieldLikeLabel('NEGOCIO').setLoading(true);
                    _fieldLikeLabel('NEGOCIO').getStore().load(
                    {
                        params :
                        {
                            'params.tipoUnidad' : _fieldByLabel('TIPO DE UNIDAD').getValue()
                            ,'params.cdagente'  : _fieldByLabel('AGENTE').getValue()
                        }
                        ,callback : function()
                        {
                            _fieldLikeLabel('NEGOCIO').setLoading(false);
                        }
                    });
                }
            }
        });
        
        //descuento
        _fieldLikeLabel('DESCUENTO').on(
        {
            focus : function()
            {
                var valido = !Ext.isEmpty(_fieldByLabel('TIPO DE UNIDAD').getValue());
                if(!valido)
                {
                    mensajeWarning('Seleccione el tipo de unidad');
                }
                
                if(valido)
                {
                    valido = !Ext.isEmpty(_fieldByLabel('TIPO DE USO').getValue());
                    if(!valido)
                    {
                        mensajeWarning('Seleccione el tipo de uso');
                    }
                }
                
                if(valido)
                {
                    valido = !Ext.isEmpty(_fieldByLabel('AGENTE').getValue());
                    if(!valido)
                    {
                        mensajeWarning('Seleccione el agente');
                    }
                }
                
                if(valido)
                {
                    _fieldLikeLabel('DESCUENTO').setLoading(true);
                    
                    Ext.Ajax.request({
               	        url      : _0_urlCargaValidacionDescuentoR6
               	        ,params :
                        {
                            'smap1.tipoUnidad' : _fieldByLabel('TIPO DE UNIDAD').getValue()
                            ,'smap1.uso'       : _fieldByLabel('TIPO DE USO').getValue()
                            ,'smap1.cdagente'  : _fieldByLabel('AGENTE').getValue()
                            ,'smap1.cdtipsit'  : _0_smap1.cdtipsit
                            ,'smap1.cdatribu'  : '21'
                        }
               	        ,success : function(response)
               	        {
               	            var ijson=Ext.decode(response.responseText);
               	            debug('### obtener auto por clave gs:',ijson);
               	            if(ijson.success)
               	            {
               	            	_fieldLikeLabel('DESCUENTO').setMinValue(ijson.smap1.RANGO_MINIMO);
               	            	_fieldLikeLabel('DESCUENTO').setMaxValue(ijson.smap1.RANGO_MAXIMO);
               	             	_fieldLikeLabel('DESCUENTO').setLoading(false);
               	            }
               	            else
               	            {
               	                mensajeWarning(ijson.respuesta);
               	            }
               	        }
               	        ,failure : function()
               	        {
               	            errorComunicacion();
               	        }
               	    });
                    
                }
            }
        });
        
        //modelo
        if(_0_smap1.cdtipsit+'x'=='MCx')
        {
            _fieldByLabel('MODELO').setLoading(true);
            Ext.Ajax.request(
            {
                url      : _0_urlObtenerParametros
                ,params  :
                {
                    'smap1.parametro' : 'RANGO_ANIO_MODELO'
                    ,'smap1.cdramo'   : _0_smap1.cdramo
                    ,'smap1.cdtipsit' : _0_smap1.cdtipsit
                }
                ,success : function(response)
                {
                    _fieldByLabel('MODELO').setLoading(false);
                    var json=Ext.decode(response.responseText);
                    debug('respuesta json obtener rango:',json);
                    if(json.exito)
                    {
                        _fieldByLabel('MODELO').setValue(json.smap1.P1VALOR);
                        _fieldByLabel('MODELO').setMinValue(json.smap1.P2VALOR);
                        _fieldByLabel('MODELO').setMaxValue(json.smap1.P3VALOR);
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                ,failure : function()
                {
                    _fieldByLabel('MODELO').setLoading(false);
                    errorComunicacion();
                }
            });
        }
        
        //banco -> meses
        _fieldByName('parametros.pv_otvalor18').on(
        {
            'select' : function()
            {
                if(_fieldByName('parametros.pv_otvalor18').getValue()+'x'=='0x')
                {
                    _fieldByName('parametros.pv_otvalor19').allowBlank=true;
                }
                else
                {
                    _fieldByName('parametros.pv_otvalor19').allowBlank=false;
                }
                _fieldByName('parametros.pv_otvalor19').isValid();
            }
        });
        
        _0_gridIncisos.setTitle('Datos del contratante prospecto');
        
        var agente = _fieldByName('parametros.pv_otvalor17');
        var folio  = _fieldByName('parametros.pv_otvalor16');
        
        //agente
        if((_0_smap1.cdsisrol== RolSistema.PromotorAuto
            ||
            //_0_smap1.cdsisrol=='SUSCRIAUTO'
//           (rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')!=-1)
             RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol)
            )
            &&Ext.isEmpty(_0_smap1.ntramite)
            )
        {
            agente.on(
            {
                'select' : function(comp,records)
                {
                    folio.reset();
                    Ext.Ajax.request(
                    {
                        url     : _0_urlCargarCduniecoAgenteAuto
                        ,params :
                        {
                            'smap1.cdagente' : records[0].get('key')
                        }
                        ,success : function(response)
                        {
                            var json=Ext.decode(response.responseText);
                            debug('obtener cdunieco agente response:',json);
                            if(json.exito)
                            {
                                _0_smap1.cdunieco=json.smap1.cdunieco;
                                debug('_0_smap1:',_0_smap1);
                            }
                            else
                            {
                                mensajeError(json.respuesta);
                            }
                        }
                        ,failure : errorComunicacion
                    });
                }
            });
        }
        
        //folio
        debug('folio:',folio);
        folio.on(
        {
            'change' : function(comp,val)
            {
                debug('folio change val:',val,'dummy');
                if(_0_smap1.cdsisrol+'x'=='PROMOTORAUTOx')
                {
                    agente.setReadOnly(!Ext.isEmpty(val));
                    agente.reset();
                }
            }
            ,'blur' : function()
            {
                if(!Ext.isEmpty(folio.getValue())&&folio.getValue()>0)
                {
                    _0_panelPri.setLoading(true);
                    Ext.Ajax.request(
                    {
                        url      : _0_urlCargarAgentePorFolio
                        ,params  :
                        {
                            'smap1.folio'    : folio.getValue(),
                            'smap1.cdunieco' : _0_smap1.cdunieco,
                            'smap1.cdtipsit' : _0_smap1.cdtipsit,
                            'smap1.cdramo'   : _0_smap1.cdramo,
                            'smap1.idusu'   : '<s:property value="%{#session['USUARIO'].claveUsuarioCaptura}"/>'
                        }
                        ,success : function(response)
                        {
                            _0_panelPri.setLoading(false);
                            var json=Ext.decode(response.responseText);
                            debug('json response obtener agente por folio:',json);
                            if(json.exito)
                            {
                                if(_0_smap1.cdsisrol+'x'=='PROMOTORAUTOx')
                                {
                                    var contiene=false;
                                    agente.getStore().each(function(record)
                                    {
                                        debug('buscando agente',json.smap1.cdagente,'en',record.data);
                                        if(record.get('key')==json.smap1.cdagente)
                                        {
                                            contiene=true;
                                        }
                                    });
                                    if(contiene)
                                    {
                                        agente.setValue(json.smap1.cdagente);
                                    }
                                    else
                                    {
                                        mensajeWarning('El agente '+json.smap1.cdagente+' no se encuentra en la lista del promotor/suscriptor');
                                        agente.reset();
                                    }
                                }
                                //para suscriptor y agente
                                else
                                {
                                    //agente
                                    if(_0_smap1.cdsisrol+'x'=='EJECUTIVOCUENTAx')
                                    {
                                        if(json.smap1.cdagente!=agente.getValue())
                                        {
                                            mensajeWarning('El folio pertenece a otro agente');
                                            folio.reset();
                                            folio.focus();
                                        }
                                    }
                                    //suscriptor
                                    else
                                    {
                                        agente.getStore().load(
                                        {
                                            params :
                                            {
                                                'params.agente' : json.smap1.cdagente 
                                            }
                                            ,callback : function(records)
                                            {
                                               debug('callback records:',records,records.length);
                                               if(_fieldByLabel('AGENTE').findRecord('key',json.smap1.cdagente)){
                                                   agente.setValue(json.smap1.cdagente);
                                               }else{
                                                   mensajeWarning('El agente '+json.smap1.cdagente+' no se encuentra en la lista del promotor/suscriptor');
                                                   folio.reset();
                                                   agente.reset();
                                               }
                                            }
                                        });
                                    }
                                }
                            }
                            else
                            {
                                mensajeError(json.respuesta);
                                folio.reset();
                                agente.reset();
                            }
                        }
                        ,failure : function(response)
                        {
                            _0_panelPri.setLoading(false);
                            errorComunicacion();
                        }
                    });
                }
            }
        });
        
        //tipo de unidad
        if(_0_smap1.cdtipsit+'x'=='MCx')
        {
            _fieldByName('parametros.pv_otvalor01').on(
            {
                'select' : function(comp,valArray)
                {
                    debug('tipo unidad select value:',valArray[0].data.key);
                    _0_panelPri.setLoading(true);
                    Ext.Ajax.request(
                    {
                        url      : _0_urlObtenerParametros
                        ,params  :
                        {
                            'smap1.parametro' : 'NUMERO_PASAJEROS_SERV_PUBL'
                            ,'smap1.cdramo'   : _0_smap1.cdramo
                            ,'smap1.cdtipsit' : _0_smap1.cdtipsit
                            ,'smap1.clave4'   : valArray[0].data.key
                            ,'smap1.clave5'   : _0_smap1.cdsisrol
                        }
                        ,success : function(response)
                        {
                            _0_panelPri.setLoading(false);
                            var json=Ext.decode(response.responseText);
                            debug('json response numero pasajeros:',json);
                            if(json.exito)
                            {
                                _fieldByName('parametros.pv_otvalor04').setValue(json.smap1.P1VALOR);
                                 _fieldByName('parametros.pv_otvalor04').setMinValue(json.smap1.P2VALOR);
                                _fieldByName('parametros.pv_otvalor04').setMaxValue(json.smap1.P3VALOR);
                                _fieldByName('parametros.pv_otvalor22').setValue(json.smap1.P4VALOR);
                                _fieldByName('parametros.pv_otvalor04').isValid();
                                _fieldByName('parametros.pv_otvalor22').isValid();
                            }
                            else
                            {
                                mensajeWarning(json.respuesta);
                            }
                        }
                        ,failure : function()
                        {
                            _0_panelPri.setLoading(false);
                            errorComunicacion();
                        }
                    });
                }
            });
        }
        
        _fieldByName('fefin').setValue('');
        _fieldByName('feini').removeListener('change',_0_funcionFechaChange);
        _fieldByName('feini').on(
        {
            change : function()
            {
                if(_0_smap1.SITUACION=='AUTO' && _0_smap1.cdtipsit!='AT' && _0_smap1.cdtipsit!='MC'){
                    
                    if(_fieldByName('fefin').readOnly){
                        
                        return ;
                    }
                }
                if(Ext.isEmpty(_fieldByName('parametros.pv_otvalor20').getValue()))
                {
                    mensajeWarning('Favor de capturar la vigencia');
                }
                else
                {
//                     debug('val:',val);
//                     var fefin = _fieldByName('fefin');
//                     fefin.setValue(Ext.Date.add(val,Ext.Date.YEAR,1));
//                     fefin.setMinValue(Ext.Date.add(val,Ext.Date.DAY,1));
//                     fefin.isValid();
                    _fieldByName('fefin').setValue(
                         Ext.Date.add(
	                         _fieldByName('feini').getValue()
	                         ,Ext.Date.MONTH
	                         ,_fieldByName('parametros.pv_otvalor20').getValue()
                        )
                    );
                }
            }
        });
        
        _fieldByName('parametros.pv_otvalor20').getStore().on(
        {
            load : function()
            {
                _fieldByName('fefin').setValue(
                    Ext.Date.add(
                        _fieldByName('feini').getValue()
                        ,Ext.Date.MONTH
                        ,_fieldByName('parametros.pv_otvalor20').getValue()
                    )
                );
            }
        });
        
        _fieldByName('parametros.pv_otvalor20').addListener('change',function()
        {
            _fieldByName('fefin').setValue(
                Ext.Date.add(_fieldByName('feini').getValue(),Ext.Date.MONTH,_fieldByName('parametros.pv_otvalor20').getValue())
            );
        });
        
        var combcl=_fieldByName('parametros.pv_otvalor24');
        var codpos=_fieldByName('parametros.pv_otvalor23');
        
        combcl.on('change',function()
        {
            debug('Combo cliente nuevo change:',combcl.getValue());
            //cliente nuevo
            if(combcl.getValue()=='S')
            {
                codpos.reset();
                _0_storeIncisos.removeAll();
                _0_gridIncisos.show();
                codpos.setReadOnly(false);
                codpos.setFieldLabel('C&Oacute;DIGO POSTAL CLIENTE');
                _0_recordClienteRecuperado=null;
                _0_smap1['cdideperCli']=null;
                _0_smap1['cdpersonCli']=null;
            }
            //recuperar cliente
            else if(combcl.getValue()=='N' && ( Ext.isEmpty(combcl.semaforo)||combcl.semaforo==false ))
            {
                codpos.reset();
                _0_storeIncisos.removeAll();
                _0_gridIncisos.hide();
                codpos.setReadOnly(true);
                var ventana=Ext.create('Ext.window.Window',
                {
                    title      : 'Recuperar cliente'
                    ,modal     : true
                    ,width     : 600
                    ,height    : 400
                    ,items     :
                    [
                        {
                            layout    : 'hbox'
                            ,defaults : { style : 'margin : 5px;' }
                            ,items    :
                            [
                                {
                                    xtype       : 'textfield'
                                    ,name       : '_0_recuperaRfc'
                                    ,fieldLabel : 'RFC'
                                    ,minLength  : 9
                                    ,maxLength  : 13
                                }
                                ,{
                                    xtype    : 'button'
                                    ,text    : 'Buscar'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                                    ,handler : function(button)
                                    {
                                        debug('recuperar cliente buscar');
                                        var rfc=_fieldByName('_0_recuperaRfc').getValue();
                                        var valido=true;
                                        if(valido)
                                        {
                                            valido = !Ext.isEmpty(rfc)
                                                     &&rfc.length>8
                                                     &&rfc.length<14;
                                            if(!valido)
                                            {
                                                mensajeWarning('Introduza un RFC v&aacute;lido');
                                            }
                                        }
                                        if(valido)
                                        {
                                            button.up('window').down('grid').getStore().load(
                                            {
                                                params :
                                                {
                                                    'map1.pv_rfc_i'     : rfc,
                                                    'map1.cdtipsit'     : _0_smap1.cdtipsit,
                                                    'map1.pv_cdtipsit_i': _0_smap1.cdtipsit,
                                    			    'map1.pv_cdunieco_i': _0_smap1.cdunieco,
                                               		'map1.pv_cdramo_i'  : _0_smap1.cdramo,
                                               		'map1.pv_estado_i'  : 'W',
                                               		'map1.pv_nmpoliza_i': _0_fieldNmpoliza.getValue()
                                                }
                                            });
                                        }
                                    }
                                }
                            ]
                        }
                        ,Ext.create('Ext.grid.Panel',
                        {
                            title    : 'Resultados'
                            ,columns :
                            [
                                {
                                    xtype    : 'actioncolumn'
                                    ,width   : 30
                                    ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                    ,handler : function(view,row,col,item,e,record)
                                    {
                                        debug('recuperar cliente handler record:',record);
                                        _0_recordClienteRecuperado=record;
                                        codpos.setValue(record.raw.CODPOSTAL);
                                        codpos.setFieldLabel('C&Oacute;DIGO POSTAL CLIENTE<br/>('+record.get('NOMBRECLI')+')');
                                        ventana.destroy();
                                    }
                                }
                                ,{
                                    text       : 'Nombre'
                                    ,dataIndex : 'NOMBRECLI'
                                    ,width     : 200
                                }
                                ,{
                                    text       : 'Direcci&oacute;n'
                                    ,dataIndex : 'DIRECCIONCLI'
                                    ,flex      : 1
                                }
                            ]
                            ,store : Ext.create('Ext.data.Store',
                            {
                                model     : '_0_modeloRecuperado'
                                ,autoLoad : false
                                ,proxy    :
                                {
                                    type    : 'ajax'
                                    ,url    : _0_urlRecuperarCliente
                                    ,timeout: 240000
                                    ,reader :
                                    {
                                        type  : 'json'
                                        ,root : 'slist1'
                                    }
                                }
                            })
                        })
                    ]
                    ,listeners :
                    {
                        close : function()
                        {
                            combcl.setValue('S');
                        }
                    }
                }).show();
                centrarVentanaInterna(ventana);
            }
        });
        
        combcl.getStore().on('load',function()
        {
            debug('combo cliente nuevo store load');
            combcl.setValue('S');
        });
        combcl.setValue('S');
        
        //modelo
        if(_0_smap1.cdtipsit+'x'=='ATx')
        {
            _fieldByLabel('MODELO').on(
            {
                select : function()
                {
                    _0_obtenerClaveGSPorAuto();
                    _0_obtenerSumaAseguradaRamo6(false);
                }
            });
        }
        
        //version
        if(_0_smap1.cdtipsit+'x'=='ATx')
        {
            _fieldByLabel('VERSION').on(
            {
                'select' : _0_obtenerClaveGSPorAuto
            });
        }
        
        //auto combo
        if(_0_smap1.cdtipsit+'x'=='ATx')
        {
            _fieldByName('parametros.pv_otvalor22').on(
            {
                'select' : function(comp,arr)
                {
                    debug('auto seleccionado:',arr[0]);
                    var value    = arr[0].get('value');
                    var splt     = value.split(' - ');
                    var tipo     = splt[1];
                    var marca    = splt[2];
                    var submarca = splt[3];
                    var modelo   = splt[4];
                    var version  = splt[5];
                    debug('tipo:',tipo);
                    debug('marca:',marca);
                    debug('submarca:',submarca);
                    debug('modelo:',modelo);
                    debug('version:',version);
                    
                    _fieldByLabel('TIPO DE UNIDAD').setValue(_fieldByLabel('TIPO DE UNIDAD').findRecord('value',tipo));
                    _fieldByLabel('MARCA').heredar(true,function()
                    {
                        _fieldByLabel('MARCA').setValue(_fieldByLabel('MARCA').findRecord('value',marca));
                        _fieldByLabel('SUBMARCA').heredar(true,function()
                        {
                            _fieldByLabel('SUBMARCA').setValue(_fieldByLabel('SUBMARCA').findRecord('value',submarca));
                            _fieldByLabel('MODELO').heredar(true,function()
                            {
                                _fieldByLabel('MODELO').setValue(_fieldByLabel('MODELO').findRecord('value',modelo));
                                _fieldByLabel('VERSION').heredar(true,function()
                                {
                                    _fieldByLabel('VERSION').setValue(_fieldByLabel('VERSION').findRecord('value',version));    
                                    _0_obtenerSumaAseguradaRamo6(true);                        
                                });
                            });
                        });
                    });
                    
                    _0_cargarNumPasajerosAuto();
                }
            });
        }
        
        //version
        if(_0_smap1.cdtipsit+'x'=='ATx')
        {
            _fieldByLabel('VERSION').on(
            {
                'select' : function()
                {
                    _0_obtenerSumaAseguradaRamo6(true);
                }
            });
        }
        
        debug('<parche para ramo 6');
    }
    
    // Se busca la imagen para mostrar en el cotizador segun el producto:
    if (_0_smap1.cdramo == Ramo.GastosMedicosMayoresPrueba) {
        Ext.Ajax.request({
            url    : _0_urlObtenerParametros,
            params :{
                'smap1.parametro' : 'IMAGEN_COTIZACION',
                'smap1.cdramo'   : _0_smap1.cdramo,
                'smap1.cdtipsit' : _0_smap1.cdtipsit
            },
            success : function(response) {
                var json=Ext.decode(response.responseText);
                debug('########## Respuesta:',json);
                if(json.exito) {
                    Ext.create('Ext.window.Window', {
                        focusOnToFront : false, //evitamos que obtenga el focus en automatico
                        closable : false,
                        header: false,
                        border: false,
                        resizable: false,
                        width: Number(json.smap1.P2VALOR),
                        height: Number(json.smap1.P3VALOR),
                        items: [{
                            xtype : 'image',
                            src : '${ctx}/images/proceso/cotizacion/'+json.smap1.P1VALOR
                        }]
                    }).showAt(Number(json.smap1.P4VALOR), Number(json.smap1.P5VALOR));
               }
            }
        });
    }
    
    if(_0_smap1.ntramite&&_0_smap1.ntramite.length>0)
    {
    	Ext.create('Ext.window.Window',
    	{
            title           : 'Documentos del tr&aacute;mite ' + _0_smap1.ntramite
            ,closable       : false
            ,width          : 500
            ,height         : 300
            ,autoScroll     : true
            ,collapsible    : true
            ,titleCollapse  : true
            ,startCollapsed : true
            ,resizable      : false
            ,cls            : 'VENTANA_DOCUMENTOS_CLASS'
            ,loader         :
            {
                scripts   : true
                ,autoLoad : true
                ,url      : _0_urlVentanaDocumentos
                ,params   :
                {
                    'smap1.cdunieco'  : _0_smap1.cdunieco
                    ,'smap1.cdramo'   : _0_smap1.cdramo
                    ,'smap1.estado'   : 'W'
                    ,'smap1.nmpoliza' : '0'
                    ,'smap1.nmsuplem' : '0'
                    ,'smap1.nmsolici' : '0'
                    ,'smap1.ntramite' : _0_smap1.ntramite
                    ,'smap1.tipomov'  : '0'
                }
            }
        }).showAt(450, 50);
    }
    
    if(_0_formAgrupados.items.items[2])
    {
        _0_formAgrupados.items.items[2].focus();
    }
    else if(_0_formAgrupados.items.items[0].items.items[2])
    {
        _0_formAgrupados.items.items[0].items.items[2].focus();
        debug('_0_formAgrupados.items.items[0].items.items[2].focus();',_0_formAgrupados.items.items[0].items.items[2]);
    }
    else
    {
        debug('no focus:',_0_formAgrupados.items.items[0].items.items);
    }
    
    //obtener minimos y maximo
    /*
    _0_panelPri.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _0_urlObtenerParametros
        ,params  :
        {
            'smap1.parametro' : 'MINIMOS_Y_MAXIMOS'
            ,'smap1.cdramo'   : _0_smap1.cdramo
            ,'smap1.cdtipsit' : _0_smap1.cdtipsit
        }
        ,success : function(response)
        {
            _0_panelPri.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('respuesta json obtener minimos y maximos:',json);
            if(json.exito)
            {
                for(var i=1;i<=13;i=i+2)
                {
                    if(json.smap1['P'+i+'CLAVE']=='CDATRIBU')
                    {
                        _fieldByName('parametros.pv_otvalor'+(('00'+json.smap1['P'+i+'VALOR']).slice(-2))).setMinValue(json.smap1['P'+(i+1)+'CLAVE']);
                        _fieldByName('parametros.pv_otvalor'+(('00'+json.smap1['P'+i+'VALOR']).slice(-2))).setMaxValue(json.smap1['P'+(i+1)+'VALOR']);
                    }
                }
            }
            else
            {
                debug('### ERROR:',json.respuesta);
            }
        }
        ,failure : function()
        {
            _0_panelPri.setLoading(false);
            errorComunicacion();
        }
    });
    */
    
    //VIGENCIA
    if(_0_smap1.SITUACION=='PERSONA')
    {
        Ext.Ajax.request(
        {
            url      : _0_urlRetroactividadDifer
            ,params  :
            { 
                'smap1.cdunieco'  : _0_smap1.cdunieco
                ,'smap1.cdramo'   : _0_smap1.cdramo
                ,'smap1.cdtipsup' : '1'
                ,'smap1.cdusuari' : _0_smap1.cdusuari
                ,'smap1.cdtipsit' : _0_smap1.cdtipsit
            }
            ,success : function(response)
            {
                _0_panelPri.setLoading(false);
                var ck = 'Recuperando retroactividad';
                try
                {
                    var json=Ext.decode(response.responseText);
                    debug('### respuesta obtener rango vigencia:',json);
                    if(json.exito)
                    {
                        _fieldByName('feini').setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.retroac*-1));
                        _fieldByName('feini').setMaxValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.diferi));
                    }
                    else
                    {
                        mensajeWarning('Falta definir rango de vigencia para el producto (RANGOVIGENCIA)');
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                _0_panelPri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    
   
    //para gmi
    if(_0_smap1.cdramo=='7'&&_0_smap1.cdtipsit=='GMI')
    {
        _fieldLikeLabel('POSTAL').on(
        {
            select : _0_gmiPostalSelect
        });
        
        _fieldLikeLabel('CULO HOSPITALARIO').on(
        {
            select : _0_gmiCirchospSelect
        });
    }
    try{
	    if(_0_smap1.cdramo==Ramo.ServicioPublico){
	    	Ext.ComponentQuery
	    	.query("[fieldLabel*='(FRONTERIZO)'],[fieldLabel*='MERO DE SERIE'],[fieldLabel*='TIPO DE CAMBIO AL D'],[fieldLabel*='PAQUETE'],[fieldLabel*='NOMBRE CLIENTE'],[fieldLabel*='TIPO PERSONA'],[fieldLabel*='FECHA DE NACIMIENTO DEL CONTRATANTE']")
	    	.forEach(function(it){ 
		    		it.allowBlank=true; 
		    		it.hide();
	    		}
	    	);
	    	
	    	
	    	
	    	Ext.ComponentQuery.query('[title=<span style="font:bold 14px Calibri;">DATOS GENERALES</span>]')[0]
	    	.items
	    	.items
	    	.sort(function(a,b){
	    		if(_0_smap1.cdtipsit==TipoSituacion.ServicioPublicoAuto){
	    			var ordenOriginal=[16,17,22,1,2,3,4,5,6,25,7,18,19,20,24,23,21,31,32];
	    		}else if(_0_smap1.cdtipsit==TipoSituacion.ServicioPublicoMicro){
	    			var ordenOriginal=[16,17,1,22,2,3,4,5,6,18,19,20,24,23,21,30,31];
	    		}
	    		
	    		
	    		var va=ordenOriginal.indexOf(Number(a.cdatribu));
	    		var vb=ordenOriginal.indexOf(Number(b.cdatribu));
	    	    
	    		if(va==-1 ) va=10000000000;
	    		if(vb==-1 ) vb=10000000000;
	    		if(a.fieldLabel=='COTIZACI&Oacute;N') va=-1;
	    		if(b.fieldLabel=='COTIZACI&Oacute;N') vb=-1;
	    		if(va>vb){
	    			return 1;
	    		}else if(va<vb){
	    			return -1;
	    		}
	    		return 0;
	    	});
	    	Ext.ComponentQuery.query('[title=<span style="font:bold 14px Calibri;">DATOS GENERALES</span>]')[0]
	    	.doLayout();
	    }
    }catch(e){
    	debugError(e);
    }
    
    if (_0_smap1.cdramo == Ramo.GastosMedicosMayoresPrueba) {
        if (!Ext.isEmpty(_fieldLikeLabel('DESCUENTO', null, true))) {
            var ck = 'Recuperando rango de descuento/recargo';
            try {
                _request({
                    mask   : ck,
                    url    : _GLOBAL_URL_RECUPERACION,
                    params : {
                        'params.consulta' : 'RECUPERAR_RANGO_DESCUENTO_RECARGO',
                        'params.cdramo'   : _0_smap1.cdramo,
                        'params.cdtipsit' : _0_smap1.cdtipsit
                    },
                    success : function (json) {
                        var comp = _fieldLikeLabel('DESCUENTO');
                        comp.setMinValue(json.params.MINIMO);
                        comp.setMaxValue(json.params.MAXIMO);
                    }
                });
            } catch (e) {
                manejaException(e, ck);
            }
        }
        
        if (!Ext.isEmpty(_fieldByLabel('PLAN', null, true))) {
            var planCmp = _fieldByLabel('PLAN', null, true);
            _0_formAgrupados.remove(planCmp, false);
            _0_formAgrupados.insert(2, planCmp);
        }
        
        if (!Ext.isEmpty(_fieldByLabel('PLAN', null, true)) && !Ext.isEmpty(_fieldByLabel('SUMA ASEGURADA', null, true))) {
            var planCmp = _fieldByLabel('PLAN', null, true),
                sumaCmp = _fieldByLabel('SUMA ASEGURADA', null, true);
            sumaCmp.anidado = true;
            sumaCmp.heredar = function () {
                sumaCmp.getStore().load({
                    params : {
                        'params.idPadre' : planCmp.getValue()
                    }
                });
            };
        }
        
        if (!Ext.isEmpty(_fieldByLabel('PLAN', null, true)) && !Ext.isEmpty(_fieldLikeLabel('ULO M', null, true))) {
            var planCmp = _fieldByLabel('PLAN', null, true),
                circCmp = _fieldLikeLabel('ULO M', null, true);
            circCmp.anidado = true;
            circCmp.heredar = function () {
                circCmp.getStore().load({
                    params : {
                        'params.idPadre' : planCmp.getValue()
                    }
                });
            };
            planCmp.on({
                blur : function () {
                    circCmp.heredar();
                }
            });
        } 
        
        if (!Ext.isEmpty(_fieldByLabel('PLAN', null, true)) && !Ext.isEmpty(_fieldByLabel('DEDUCIBLE', null, true))) {
            var planCmp = _fieldByLabel('PLAN', null, true),
                deduCmp = _fieldByLabel('DEDUCIBLE', null, true);
            deduCmp.anidado = true;
            deduCmp.heredar = function () {
                deduCmp.getStore().load({
                    params : {
                        'params.idPadre' : planCmp.getValue()
                    }
                });
            };
        }
    }
    
    // Para TODOS LOS PRODUCTOS (si aplican), se agrega validacion de Codigo Postal vs Estado:
    agregaValidacionCPvsEstado();
    
    //si hay flujo de autos se recupera cotizacion ultima
    _0_recuperarCotizacionDeTramite();
    
    //recuperar poliza desde sigs para renovacion
    _0_recuperarPolizaSIGS();
    
    //codigo dinamico recuperado de la base de datos
    <s:property value="smap1.customCode" escapeHtml="false" />

});

    function getFormaPago(administradora,retenedora){
    	var pago='nan';
    	var esperar = true;


    	
    	 
    	  $.ajax({
                          async:false, 
                          cache:false,
                          dataType:"json", 
                          type: 'POST',   
                          url: url_obtiene_forma_pago,
                          data: {
                         	 'smap1.administradora':administradora,
                         	 'smap1.retenedora':retenedora
                         	 
                          }, 
                          success:  function(respuesta){
                        	  var ck = 'Decodificando respuesta al recuperar datos para revisar renovaci\u00f3n';
                              
                           try
                           {
                               var jsonDatTram =  respuesta// Ext.decode(response.responseText);
                               debug('### jsonDatTram:',jsonDatTram,'.');
                               
                               if(jsonDatTram.success === true)
                               {
                              	
                                   pago=jsonDatTram.smap1;
                                  
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
                           esperar=false;
                          },
                          beforeSend:function(){},
                          error:function(objXMLHttpRequest){
                        	  esperar=false;
    	                        errorComunicacion(null,'Error al recuperar datos de tr\u00e1mite para revisar renovaci\u00f3n');
                          }
                        });
    	
    	 return pago;
    }
    
    function agregarAgenteDXN(){
        
        try{
            var cdagente=_fieldByLabel('AGENTE').getValue();
            
            _fieldByLabel('RETENEDORA').store.proxy.extraParams['params.cdagente']=cdagente;
            
        }catch(e){
            debugError(e)
        }
        
        
    }
    
    function aplicaDxn(cdagente){
        
        var ck=''
        try{
	        _fieldByLabel("ADMINISTRADORA").clearValue();
	        _fieldByLabel("RETENEDORA").clearValue();
	        _mask("Validando DXN");
	        Ext.Ajax.request(
                {
                    url      : _0_urlAplicaDxn
                    ,params  :
                    { 
                        
                        'smap1.cdtipsit' : _0_smap1.cdtipsit,
                        'smap1.cdagente' : cdagente==undefined?"":cdagente
                    }
                    ,success : function(response)
                    {
                        
                        _unmask();
                        var ck = 'Recuperando aplica dxn';
                        try
                        {
                            var json=Ext.decode(response.responseText);
                            debug('### respuesta obtener aplica dxn:',json);
                            if(json.exito)
                            {
                                if((json.smap1.aplicaDxn+'')=='N'){
                                    _fieldById('fieldsetDxn').hide();
                                }else{
                                    _fieldById('fieldsetDxn').show();
                                }
                                
                            }
                            else
                            {
                                mensajeWarning('Fallo consultando aplica DXN');
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
                        errorComunicacion();
                    }
                });
        }catch(e){
            debugError(e);
        }
    }
    
    function administradoraAgenteDXN(){
        try{
            var cdagente=_fieldByLabel('AGENTE').getValue();
            
            _fieldByLabel('ADMINISTRADORA').store.proxy.extraParams['params.cdagente']=cdagente;
            _fieldByLabel('ADMINISTRADORA').store.load();
        }catch(e){
            debugError(e)
        }
    }
</script>
</head>
<body><div id="_0_divPri" style="height: 1700px;border:1px solid #CCCCCC;"></div></body>
</html>