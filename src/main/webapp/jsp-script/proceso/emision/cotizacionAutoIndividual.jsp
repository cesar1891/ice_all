<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.conTimeout>tbody>tr::after {
    content: url('${ctx}/resources/fam3icons/icons/clock.png');
    margin-left: 5px;
}
</style>
<script>
////// overrides //////


Ext.override(Ext.form.TextField,
{
    initComponent:function()
    {
        if(Ext.isEmpty(this.sinOverride)||this.sinOverride==false)
        {
            Ext.apply(this,
            {
                labelWidth : 250
            });
        }
        if(<s:property value='%{getSmap1().containsKey("debug")}' />&&!Ext.isEmpty(this.cdatribu))
        {
            this.on(
            {
                afterrender : function(me)
                {
                    me.el.on(
                    {
                        mouseover : function()
                        {
                            me.ventana=Ext.create('Ext.panel.Panel',
                            {
                                floating : true
                                ,frame   : true
                                ,html    : me.cdatribu+':'+me.getValue()
                            });
                            me.ventana.show();
                            me.ventana.setY(me.getY());
                            me.ventana.setX(me.getX());
                        }
                        ,mouseout : function()
                        {
                            me.ventana.destroy();
                        }
                    });
                }
            });
        }
        return this.callParent();
    }
});

////// urls //////
var _p28_urlCargarCduniecoAgenteAuto          = '<s:url namespace="/emision"          action="cargarCduniecoAgenteAuto"                       />';
var _p28_urlCatalogos                         = '<s:url namespace="/catalogos"        action="obtieneCatalogo"                                />';  
var _p28_urlCotizar                           = '<s:url namespace="/emision"          action="cotizar"                                        />';
var _p28_urlRecuperarCliente                  = '<s:url namespace="/"                 action="buscarPersonasRepetidas"                        />';
var _p28_urlCargarRetroactividadSuplemento    = '<s:url namespace="/emision"          action="cargarRetroactividadSuplemento"                 />';
var _p28_urlCargarSumaAseguradaRamo5          = '<s:url namespace="/emision"          action="cargarSumaAseguradaRamo5"                       />';
var _p28_urlCargar                            = '<s:url namespace="/emision"          action="cargarCotizacion"                               />';
var _p28_urlDatosComplementarios              = '<s:url namespace="/emision"          action="emisionAutoIndividual"                          />';
var _p28_urlCargarParametros                  = '<s:url namespace="/emision"          action="obtenerParametrosCotizacion"                    />';
var _p28_urlCoberturas                        = '<s:url namespace="/flujocotizacion"  action="obtenerCoberturas4"                             />';
var _p28_urlComprar                           = '<s:url namespace="/flujocotizacion"  action="comprarCotizacion4"                             />';
var _p28_urlViewDoc                           = '<s:url namespace ="/documentos"      action="descargaDocInline"                              />';
var _p28_urlEnviarCorreo                      = '<s:url namespace="/general"          action="enviaCorreo"                                    />';
var _p28_urlDetalleCotizacion                 = '<s:url namespace="/"                 action="detalleCotizacion"                              />';
var _p28_urlGuardarConfig                     = '<s:url namespace="/emision"          action="guardarConfigCotizacion"                        />';
var _p28_urlCargarConfig                      = '<s:url namespace="/emision"          action="cargarConfigCotizacion"                         />';
var _p28_urlRecuperacionSimple                = '<s:url namespace="/emision"          action="recuperacionSimple"                             />';
var _p28_urlCargarParamerizacionCoberturas    = '<s:url namespace="/emision"          action="cargarParamerizacionConfiguracionCoberturas"    />';
var _p28_urlValidarTractocamionRamo5          = '<s:url namespace="/emision"          action="cargarValidacionTractocamionRamo5"              />';
var _p28_urlCargarObligatorioCamionRamo5      = '<s:url namespace="/emision"          action="cargarObligatorioTractocamionRamo5"             />';
var _p28_urlCargarDetalleNegocioRamo5         = '<s:url namespace="/emision"          action="cargarDetalleNegocioRamo5"                      />';
var _p28_urlCargarParamerizacionCoberturasRol = '<s:url namespace="/emision"          action="cargarParamerizacionConfiguracionCoberturasRol" />';
var _p28_urlCargarTipoCambioWS                = '<s:url namespace="/emision"          action="cargarTipoCambioWS"                             />';
var _p28_urlRecuperacion                      = '<s:url namespace="/recuperacion"     action="recuperar"                                      />';
var _p28_urlCargarPoliza                      = '<s:url namespace="/emision"          action="cargarPoliza"                                   />';
var _p28_urlDetalleTramite                    = '<s:url namespace="/mesacontrol"      action="movimientoDetalleTramite"                       />';
var _p28_urlActualizarOtvalorTramiteXDsatribu = '<s:url namespace="/emision"          action="actualizarOtvalorTramitePorDsatribu"            />';
var _p28_urlRecuperarOtvalorTramiteXDsatribu  = '<s:url namespace="/emision"          action="recuperarOtvalorTramitePorDsatribu"             />';
var _p28_urlRecuperarDatosTramiteValidacion   = '<s:url namespace="/flujomesacontrol" action="recuperarDatosTramiteValidacionCliente"         />';
var url_obtiene_forma_pago                    = '<s:url namespace="/emision"          action="obtieneFormaPago"                               />';
var _p28_datosFlujo                           = '<s:url namespace="/emision"          action="datosFlujo"                                     />';
var _p28_urlCargarAutoPorClaveGS              = '<s:url namespace="/emision"          action="cargarAutoPorClaveGS"           />';
var _p28_urlCargarSumaAsegurada               = '<s:url namespace="/emision"          action="cargarSumaAseguradaAuto"        />';
var _p28_urlImprimirCotiza                    = '<s:property value="rutaServidorReports" />';
var _p28_reportsServerUser                    = '<s:property value="passServidorReports" />';
var _0_urlCargaValidacionDescuentoR6          = '<s:url namespace="/emision"          action="obtieneValidacionDescuentoR6"                 />';
var _0_urlNada                                = '<s:url namespace="/emision"          action="webServiceNada"                 />';
var _p28_urlImprimirCotiza = '<s:property value="rutaServidorReports" />'; 
var _p28_reportsServerUser = '<s:property value="passServidorReports" />';
var _0_urlObtieneValNumeroSerie    = '<s:url namespace="/emision"         action="obtieneValNumeroSerie"          />';
////// urls //////

////// variables //////
var cargarXpoliza = false;
var _p28_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p28_smap1:',_p28_smap1);
var _0_smap1 =<s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _p28_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p28_flujo:',_p28_flujo);

var _p28_flujoAux = {};
try {
    if (!Ext.isEmpty(_p28_flujo.aux)) {
        _p28_flujoAux = Ext.decode(_p28_flujo.aux);
    }
} catch (e) {
    debugError('WARNING al decodificar _p28_flujo.aux como JSON:', _p28_flujo, e);
}
debug('_p28_flujoAux:', _p28_flujoAux);

var _p28_reporteCotizacion = '<s:text name='%{"rdf.cotizacion.nombre."+smap1.cdtipsit.toUpperCase()}' />';
debug('_p28_reporteCotizacion:',_p28_reporteCotizacion);

var _p28_formFields   = [<s:property value="imap.formFields"   />];
var _p28_panel2Items  = [<s:property value="imap.panel2Items"  />];
var _p28_panel3Items  = [<s:property value="imap.panel3Items"  />];
var _p28_panel4Items  = [<s:property value="imap.panel4Items"  />];
var _p28_panelDxnItems= [<s:property value="imap.panelDxnItems"  />];
    
//panel para la renovacio por poliza
var _p28_panel5Items  = [<s:property value="imap.panel5Items"  />];

var _p28_recordClienteRecuperado = null;
var _p28_storeCoberturas         = null;
var _p28_windowCoberturas        = null;
var _p28_selectedCdperpag        = null;
var _p28_selectedCdplan          = null;
var _p28_selectedDsplan          = null;
var _p28_selectedNmsituac        = null;
var _p28_precioDolarDia          = null;
var valorRecuperadoValorVehiSigs = null;
var cdper                        = null;
var cdperson                     = null;
var formaPago                    = null;
var _p28_negocio                 = null;
var _rangoValorBaseDatos;
// var rolesSuscriptores = '|SUSCRIAUTO|TECNISUSCRI|EMISUSCRI|JEFESUSCRI|GERENSUSCRI|SUBDIRSUSCRI|';
var plazoenanios;
////// variables //////


Ext.onReady(function()
{
    
    Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    //INICIO DE THOUSAND SEPARATOR 
    Ext.util.Format.thousandSeparator=",";
    Ext.define("Ext.override.ThousandSeparatorNumberField", {
        override: "Ext.form.field.Number",
        
        /**
        * @cfg {Boolean} useThousandSeparator
        */
        useThousandSeparator: false,
        
        /**
         * @inheritdoc
         */
        toRawNumber: function (value) {
            
            var cadena = String(value).replace(this.decimalSeparator,'.').replace(new RegExp(Ext.util.Format.thousandSeparator, "g"), '');
            return cadena;
        },
        
        /**
         * @inheritdoc
         */
        getErrors: function (value) {
            if (!this.useThousandSeparator) return this.callParent(arguments);
            
            var me = this,
                errors = Ext.form.field.Text.prototype.getErrors.apply(me, arguments),
                format = Ext.String.format,
                num;

            value = Ext.isDefined(value) ? value : this.processRawValue(this.getRawValue());

            if (value.length < 1) { // if it's blank and textfield didn't flag it then it's valid
                return errors;
            }

            value = me.toRawNumber(value);

            if (isNaN(value.replace(Ext.util.Format.thousandSeparator,''))) {
                errors.push(format(me.nanText, value));
            }

            num = me.parseValue(value);

            if (me.minValue === 0 && num < 0) {
                errors.push(this.negativeText);
            }
            else if (num < me.minValue) {
                errors.push(format(me.minText, me.minValue));
            }

            if (num > me.maxValue) {
                errors.push(format(me.maxText, me.maxValue));
            }

            return errors;
        },
        
        /**
         * @inheritdoc
         */
         valueToRaw: function (value) {
            if (!this.useThousandSeparator) return this.callParent(arguments);
            
            var me = this;
            var format = "000,000";
            
            for (var i = 0; i < me.decimalPrecision; i++) {
                if (i == 0) format += ".";
                format += "0";
            }
            
            value = me.parseValue(Ext.util.Format.number(value, format));
            value = me.fixPrecision(value);
            value = Ext.isNumber(value) ? value : parseFloat(me.toRawNumber(value));
            value = isNaN(value) ? '' : String(Ext.util.Format.number(value, format)).replace('.', me.decimalSeparator);
            return value;
        },
        
        /**
         * @inheritdoc
         */
        getSubmitValue: function () {
            if (!this.useThousandSeparator)
                return this.callParent(arguments);
            var me = this,
                value = me.callParent();

            if (true||!me.submitLocaleSeparator) {
                value = me.toRawNumber(value);
            }
            return value;
        },
        
        /**
         * @inheritdoc
         */
        setMinValue: function (value) {
            if (!this.useThousandSeparator)
                return this.callParent(arguments);
            var me = this,
                allowed;

            me.minValue = Ext.Number.from(value, Number.NEGATIVE_INFINITY);
            me.toggleSpinners();

            // Build regexes for masking and stripping based on the configured options
            if (me.disableKeyFilter !== true) {
                allowed = me.baseChars + '';

                if (me.allowExponential) {
                    allowed += me.decimalSeparator + 'e+-';
                }
                else {
                    allowed += Ext.util.Format.thousandSeparator;
                    if (me.allowDecimals) {
                        allowed += me.decimalSeparator;
                    }
                    if (me.minValue < 0) {
                        allowed += '-';
                    }
                }

                allowed = Ext.String.escapeRegex(allowed);
                me.maskRe = new RegExp('[' + allowed + ']');
                if (me.autoStripChars) {
                    me.stripCharsRe = new RegExp('[^' + allowed + ']', 'gi');
                }
            }
        },
        
        /**
         * @private
         */
        parseValue: function (value) {
            if (!this.useThousandSeparator)
                return this.callParent(arguments);
            value = parseFloat(this.toRawNumber(value));
            return isNaN(value) ? null : value;
        }
    }); //FIN THOUSAND SEPARATOR
    
     
    //_grabarEvento('COTIZACION','ACCCOTIZA',null,null,_p28_smap1.cdramo);

//     Ext.Ajax.timeout = 3*60*1000;
 
    ////// modelos //////
    Ext.define('_p28_modeloRecuperado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
             'NOMBRECLI'
            ,'DIRECCIONCLI'
        ]
    });
    
    Ext.define('_p28_formModel',
    {
        extend  : 'Ext.data.Model'
        ,fields : _p28_formFields
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
    ////// modelos //////
    
    ////// stores //////
    _p28_storeCoberturas=Ext.create('Ext.data.Store',
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
            ,url    : _p28_urlCoberturas
            ,reader :
            {
                type  : 'json'
                ,root : 'listaCoberturas'
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    _p28_windowCoberturas = new Ext.Window(
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
        ,items       :
        [
            Ext.create('Ext.grid.Panel',
            {
                itemId       : '_p28_gridCoberturas'
                ,title       : 'Sin plan'
                ,store       : _p28_storeCoberturas
                ,height      : 300
                ,selType     : 'cellmodel'
                ,buttonAlign : 'center'
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
                    /*,{
                        dataIndex : 'deducible'
                        ,text : 'Deducible'
                        ,flex : 1
                    }*/
                ]
            })
        ] 
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
    
    _p28_panel4Items.push(
    {
        xtype    : 'button'
        ,text    : 'Guardar configuraci&oacute;n'
        ,style   : 'margin:5px;margin-left:265px;'
        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
        ,handler : _p28_guardarConfig
    });
    ////// componentes //////
    
    ////// contenido //////
    var _p28_formOcultoItems = [];
    <s:if test='%{getImap().get("panel5Items")!=null}'>
        var items = [<s:property value="imap.panel5Items" />];
        for(var i=0;i<items.length;i++)
        {
            _p28_formOcultoItems.push(items[i]);
        }
    </s:if>
    <s:if test='%{getImap().get("panel6Items")!=null}'>
        var items = [<s:property value="imap.panel6Items" />];
        for(var i=0;i<items.length;i++)
        {
            _p28_formOcultoItems.push(items[i]);
        }
    </s:if>
   
    var _p28_panel1Items =
    [
        {
            layout  :
            {
                type     : 'table'
                ,columns : 2
            }
            ,border : 0
            ,items  :
            [
                {
                    xtype        : 'numberfield'
                    ,itemId      : '_p28_nmpolizaItem'
                    ,fieldLabel  : 'FOLIO'
                    ,name        : 'nmpoliza'
                    ,style       : 'margin : 15px;'
                    ,sinOverride : true
                    ,labelWidth  : 170
                    ,style       : 'margin:5px;margin-left:15px;'
                    ,listeners   :
                    {
                        change : _p28_nmpolizaChange
                    }
                 }
                ,{
                    xtype    : 'button'
                    ,itemId  : '_p28_botonCargar'
                    ,text    : 'BUSCAR'
                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                    ,handler : _p28_cargar
                 }      
              ]
         }
    ];
    
    
    <s:if test='%{getImap().get("panel1Items")!=null}'>
        var aux = [<s:property value='imap.panel1Items' />];
        for(var i=0;i<aux.length;i++)
        {
            aux[i].style='margin-left:15px;';
            _p28_panel1Items.push(aux[i]);
        }
    </s:if>
    
    
    try{
        _p28_panel2Items.forEach(function(it,idx){
            if(it.fieldLabel=='TIPO DE UNIDAD')
                it.style='margin-left:15px;';
        });
    }catch(e){
        debugError(e);
    }
    
    
   //CARGO TODOS LOS VALORES QUE SUCURSAL, RAMO Y POLIZA GENERAN
     var _p28_panel7Items =
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
                        ,itemId      : '_p28_numsuc'
                        ,fieldLabel  : 'SUCURSAL'
                        ,name        : 'sucursal'               
                        ,sinOverride : true
                        ,labelWidth  : 170
                        ,style       : 'margin:5px;margin-left:15px;'//'margin:5px;margin-left:15px;width:20px !important;'
                        ,value       : !Ext.isEmpty(_p28_smap1.RENUNIEXT) ? _p28_smap1.RENUNIEXT : ''
                        ,listeners   :
                        {
                            change : _p28_nmpolizaChange
                        }
                        ,readOnly    :  true 
                    }
                   ,{
                            xtype       : 'numberfield'
                           ,itemId      : '_p28_numram'
                           ,fieldLabel  : 'RAMO'
                           ,name        : 'ramo'                   
                           ,sinOverride : true                   
                           ,labelWidth  : 170
                           ,style       : 'margin:5px;margin-left:15px;'//'width : 30px !important;'
                           ,value       : !Ext.isEmpty(_p28_smap1.RENRAMO) ? _p28_smap1.RENRAMO : ''
                           ,listeners   :
                           {
                               change : _p28_nmpolizaChange
                           }
                           ,readOnly    :  true 
                      }
                     ,{
                          xtype       : 'numberfield'
                         ,itemId      : '_p28_numpol'
                         ,fieldLabel  : 'POLIZA'
                         ,name        : 'poliza'
                         ,sinOverride : true                 
                         ,labelWidth  : 170
                         ,style       : 'margin:5px;margin-left:15px;'//'width : 50px !important;'
                         ,value       : !Ext.isEmpty(_p28_smap1.RENPOLIEX) ? _p28_smap1.RENPOLIEX : ''
                         ,listeners   :
                         {
                             change : _p28_nmpolizaChange
                         }
                         ,readOnly    :  true
                   }
                  ,{
                         xtype   : 'button'
                        ,itemId  : '_p28_botonCargarPoliza'
                        ,text    : 'BUSCAR'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,style   : 'margin-right'
                        ,handler : _p28_cargarPoliza
                        ,hidden  : true
                  }
                  ,{
                      xtype       : 'textfield'
                     ,itemId      : '_p28_agenteSec'
                     ,fieldLabel  : 'AGENTESEC'
                     ,name        : 'agenteSec'                   
                     ,sinOverride : true                   
                     ,labelWidth  : 170
                     ,style       : 'margin:5px;margin-left:15px;'//'width : 30px !important;'
                     ,value       : ''
                     ,listeners   :
                     {
                         change : _p28_nmpolizaChange
                     }
                     ,readOnly    :  true 
                     ,hidden      :  true
                   }
                  ,{
                      xtype       : 'textfield'
                     ,itemId      : '_p28_porcenSec'
                     ,fieldLabel  : 'PORCENSEC'
                     ,name        : 'porcenSec'                   
                     ,sinOverride : true                   
                     ,labelWidth  : 170
                     ,style       : 'margin:5px;margin-left:15px;'//'width : 30px !important;'
                     ,value       : ''
                     ,listeners   :
                     {
                         change : _p28_nmpolizaChange
                     }
                     ,readOnly    :  true 
                     ,hidden      :  true
                   }
               ]
            }
         ];
    
    var tatripolItems=
    [
        <s:if test='%{!"0".equals(getSmap1().get("tatripolItemsLength"))}' >
            <s:property value="imap.tatripolItems" />
        </s:if>
    ];
    
    _p28_panel1Items.push(
    {
           xtype   : 'fieldset'
          ,itemId : '_p28_fieldBusquedaPoliza'
          ,width  : 435
          ,title  : '<span style="font:bold 14px Calibri;">RENOVAR POR POLIZA</span>'
          ,items  : _p28_panel7Items
          ,hidden : !Ext.isEmpty(_p28_flujo) ? (_p28_flujo.cdflujomc != 220 && _p28_flujo.cdtipflu != 103) : true // || _p28_smap1.renovacion+'x'!='Sx'
    }
   ,{
         xtype   : 'fieldset'
        ,itemId : '_p28_fieldsetVehiculo'
        ,width  : 435
        ,title  :  _p28_smap1.cdtipsit+ 'x' != 'TLx'? '<span style="font:bold 14px Calibri;">VEH&Iacute;CULO</span>' : '<span style="font:bold 14px Calibri;">LICENCIA</span>'
        ,items  : _p28_panel2Items
    }
    ,{
        xtype  : 'fieldset'
        ,itemId: '_p28_fieldsetCliente'
        ,width : 435
        ,title : '<span style="font:bold 14px Calibri;">CLIENTE</span>'
        ,items : _p28_panel3Items
    }
    ,{
        xtype   : 'fieldset'
        ,itemId : '_p28_fieldsetTatripol'
        ,width  : 435
        ,title  : '<span style="font:bold 14px Calibri;">DATOS ADICIONALES DE P&Oacute;LIZA</span>'
         ,hidden : _p28_smap1.cdramo+'x'=='5x'
                  ?(
                        (_p28_smap1.cdtipsit+'x'!='CRx' && _p28_smap1.cdtipsit+'x'!='TVx' && _p28_smap1.cdtipsit+'x'!='TLx' ) || 
                        !RolSistema.puedeSuscribirAutos(_p28_smap1.cdsisrol) 
//                      (rolesSuscriptores.lastIndexOf('|'+_p28_smap1.cdsisrol+'|')==-1)
//                      _p28_smap1.cdtipsit+'x'!='CRx'||_p28_smap1.cdsisrol!='SUSCRIAUTO'
                   )
                  :false 
        ,items  : tatripolItems
    }
,{
        
        xtype       : 'fieldset'
        ,itemId     : 'fieldDXN'
        ,title      : '<span style="font:bold 14px Calibri;">DXN</span>'
        ,width : 435
        ,items      : _p28_panelDxnItems
        ,hidden     : _p28_smap1.cdramo+'x'=='6x'
    }
    ,{
        xtype       : 'datefield'
        ,itemId     : '_p28_feiniItem'
        ,name       : 'feini'
        ,fieldLabel : 'INICIO DE VIGENCIA'
        ,value      : new Date()
        ,style      : 'margin-left:15px;'
        //,readOnly   : _p28_smap1.cdramo+'x'=='5x'&&_p28_smap1.cdsisrol!='SUSCRIAUTO'
    }
    ,{
        xtype       : 'datefield'
        ,itemId     : '_p28_fefinItem'
        ,name       : 'fefin'
        ,fieldLabel : 'FIN DE VIGENCIA'
        ,value      : Ext.Date.add(new Date(),Ext.Date.YEAR,1)
        ,minValue   : Ext.Date.add(new Date(),Ext.Date.DAY,1)
        ,style      : 'margin-left:15px;'
    });
    
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p28_panelpri'
        ,border   : 0
        ,renderTo : '_p28_divpri'
        ,defaults : { style : 'margin : 5px;' }
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                itemId       : '_p28_panelFlujo'
                ,title       : 'ACCIONES'
                ,hidden      : Ext.isEmpty(_p28_flujo)
                ,buttonAlign : 'left'
                ,buttons     : []
                ,listeners   :
                {
                    afterrender : function(me)
                    {
                        if(!Ext.isEmpty(_p28_flujo))
                        {
                            _cargarBotonesEntidad(
                                _p28_flujo.cdtipflu
                                ,_p28_flujo.cdflujomc
                                ,_p28_flujo.tipoent
                                ,_p28_flujo.claveent
                                ,_p28_flujo.webid
                                ,me.itemId//callback
                                ,_p28_flujo.ntramite
                                ,_p28_flujo.status
                                ,_p28_flujo.cdunieco
                                ,_p28_flujo.cdramo
                                ,_p28_flujo.estado
                                ,_p28_flujo.nmpoliza
                                ,_p28_flujo.nmsituac
                                ,_p28_flujo.nmsuplem
                                ,null//callbackDespuesProceso
                            );
                        }
                    }
                }
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId      : '_p28_form'
                ,border     : 0
                ,defaults   : { style : 'margin : 5px;' }
                ,formOculto : Ext.create('Ext.form.Panel',{ items : _p28_formOcultoItems })
                ,title      : _p28_smap1.titulo||'COTIZACI\u00d3N'
                ,layout     :
                {
                    type     : 'table'
                    ,columns : 2
                    ,tdAttrs : { valign : 'top' }
                }
                ,items    :
                [
                    {
                        xtype  : 'fieldset'
                        ,name  : 'datos_generales'
                        ,title : '<span style="font:bold 14px Calibri;">DATOS GENERALES</span>'
                        ,items : _p28_panel1Items
                    }
                    ,{
                        xtype   : 'fieldset'
                        ,itemId : '_p28_panel4Fieldset'
                        ,title  : '<span style="font:bold 14px Calibri;">COBERTURAS</span>'
                        ,items  : _p28_panel4Items
                    }
                ]
            })
            ,Ext.create('Ext.panel.Panel',
            {
                itemId       : '_p28_botonera'
                ,buttonAlign : 'center'
                ,border      : 0
                ,buttons     :
                [
                    {
                        itemId   : '_p28_cotizarButton'
                        ,text    : 'Cotizar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/calculator.png'
                        ,handler : function(){_p28_cotizar();}
                    }
                    ,{
                        itemId   : '_p28_limpiarButton'
                        ,text    : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                        ,handler : _p28_limpiar
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    try{
	    if( _p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo){
	    	_fieldLikeLabel('VALOR VEH').on(
	    			{
	    				change:function(me,n,v){ 
		    					if(me.maximoTotal<n ){ 
		    						me.setValue(v); 
		    						mensajeWarning('Valor m&aacute;ximo '+me.maximoTotal)
		    					}  
	    			
	    					}
	    				}
	    			)
	    }
	    if( _p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo || _p28_smap1.cdtipsit==TipoSituacion.TuristaLicencia ){
		    _fieldByLabel("MONEDA").getStore().filter([{filterFn: function(item) {
	            
	            return item.get("key") == '2'; }}])
	    }
    }catch(e){
    	debugError(e);
    }
    
    //dxn
    
    _fieldByName("aux.otvalor08").getStore().filter([{filterFn: function(item) {
        
        return item.get("key") < 1000; }}]);
    
    _fieldByName("aux.otvalor08").on({
        change:function(){
            _fieldByName("fefin").setReadOnly(false);
            _fieldByName("feini").fireEvent('change',_fieldByName("feini"),_fieldByName("feini").getValue());
        }
    });
    _fieldByName("aux.otvalor09").on({
        change:function(t,e,o){
            try{
            var admin=_fieldByName("aux.otvalor08").getValue();
            var ret=_fieldByName("aux.otvalor09").getValue();
            
            _fieldByName("fefin").setReadOnly(false);
             _fieldByName("feini").fireEvent('change',_fieldByName("feini"),_fieldByName("feini").getValue());
           

            if(ret!=null){
                formaPago=getFormaPago(admin,ret);
                
                var fecha_termino=formaPago.fecha_termino;
                if((fecha_termino+'').trim()!=''){
                    fecha_termino= Ext.Date.parse(formaPago.fecha_termino, "d/m/Y");
                    if(fecha_termino>new Date()){
                         _fieldByName("fefin").setValue(formaPago.fecha_termino);
                         _fieldByName("fefin").setReadOnly(true);
                         _fieldByName("fefin").validator=function(){ return true;}
                         _fieldByName("fefin").isValid();
                    }else{
                        _fieldByName("fefin").setReadOnly(false);
                        _fieldByName("feini").fireEvent('change',_fieldByName("feini"),_fieldByName("feini").getValue());
                        _fieldByLabel('NEGOCIO').fireEvent('change',_fieldByLabel('NEGOCIO'),_fieldByLabel('NEGOCIO').getValue());
                       
                    }
                    
                }else{
                    _fieldByName("fefin").setReadOnly(false);
                    _fieldByName("feini").fireEvent('change',_fieldByName("feini"),_fieldByName("feini").getValue());
                    _fieldByLabel('NEGOCIO').fireEvent('change',_fieldByLabel('NEGOCIO'),_fieldByLabel('NEGOCIO').getValue());
                }
                

            }
            }catch(e){
                debugError(e);
            }
            
        }   
    }
    );
    // ocultando campos que son de emision
    _fieldByName("aux.otvalor10").hide();
    _fieldByName("aux.otvalor11").hide();
    _fieldByName("aux.otvalor12").hide();
    _fieldByName("aux.otvalor13").hide();
    _fieldByName("aux.otvalor14").hide();
    _fieldByName("aux.otvalor15").hide();
    _fieldByName("aux.otvalor16").hide();
    
    
    //fechas
    _fieldByName('feini').on(
    {
        change : function(me,val)
        {
            
                if(_fieldByName('fefin').readOnly){
                   
                    return ;
                }
                
                debug('val:',val);
                var fefin = _fieldByName('fefin');
                fefin.setValue(Ext.Date.add(val,Ext.Date.YEAR,1));
                fefin.setMinValue(Ext.Date.add(val,Ext.Date.DAY,1));
               
                if(!Ext.isEmpty(plazoenanios))
                {
                    fefin.setMaxValue(Ext.Date.add(val,Ext.Date.YEAR,plazoenanios));
                    
                }
                fefin.isValid();
            
        }
    });
    
    //fechas
    
    //Ramo 8
    if(_p28_smap1.cdramo+'x'=='8x')//Hoteles y Restaurantes
    {
         if(_p28_smap1.cdtipsit =='8HO')//Hoteles
             {           
             _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').hide();
             _fieldLikeLabel('ACOMODADORES').hide();
             _fieldLikeLabel('NUMERO DE CAJONES').hide();
             _fieldLikeLabel('SUBLIMITE POR CAJON').hide();
             _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').hide();
             
             _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').allowBlank=true;
             _fieldLikeLabel('ACOMODADORES').allowBlank=true;
             _fieldLikeLabel('NUMERO DE CAJONES').allowBlank=true;
             _fieldLikeLabel('SUBLIMITE POR CAJON').allowBlank=true;
             _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').allowBlank=true;
             
             _fieldByLabel('ESTACIONAMIENTO').on(
                        {
                            select : _p28_camposEstacionamiento
                        });
             
             }
    }
    //ramo 8
    
    //ramo 5
    if(_p28_smap1.cdramo+'x'=='5x')
    {
        //fechas
        _fieldByName('feini').on(
        {
            change : _p28_calculaVigencia
        });
    
        _fieldByName('fefin').on(
        {
            change : _p28_calculaVigencia
        });
    
        _p28_calculaVigencia();
        //fechas
    
        var agente;
        if(!Ext.isEmpty(_fieldByLabel('AGENTE',null,true)))
        {
            agente    = _fieldByLabel('AGENTE');
        }
        var clave;
        if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor06',null,true)))
        {
            clave     = _fieldByName('parametros.pv_otvalor06');
        }
        var marca;
        if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor07',null,true)))
        {
            marca     = _fieldByName('parametros.pv_otvalor07');
        }
        var submarca;
        if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor08',null,true)))
        {
            submarca  = _fieldByName('parametros.pv_otvalor08');
        }
        var modelo;
        if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor09',null,true)))
        {
            modelo    = _fieldByName('parametros.pv_otvalor09');
        }
        var version;
        if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor10',null,true)))
        {
            version   = _fieldByName('parametros.pv_otvalor10');
        }
        var tipoValor;
        if(!Ext.isEmpty(_fieldByLabel('TIPO VALOR',null,true)))
        {
            tipoValor = _fieldByLabel('TIPO VALOR');
        }
        var sumaAsegu;
        if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
        {
            sumaAsegu = _fieldByLabel('VALOR VEHICULO');
        }
        
        var combcl = 'S';
        if(!Ext.isEmpty(_fieldLikeLabel('CLIENTE NUEVO',null,true)))
        {
            combcl    = _fieldLikeLabel('CLIENTE NUEVO');
        }
        
        if(!Ext.isEmpty( _fieldByName('parametros.pv_otvalor13',null,true)))
        {
            Ext.apply(_fieldByName('parametros.pv_otvalor13'),
                    {
                     useThousandSeparator: true,
                    });
        }
        Ext.apply(_fieldByName('parametros.pv_otvalor27'),
                {
                 useThousandSeparator: true,
                });
        Ext.apply(_fieldByName('parametros.pv_otvalor29'),
                {
                 useThousandSeparator: true,
                }); 
        
        //agente
       if(_p28_smap1.cdsisrol=='EJECUTIVOCUENTA')
       {
           agente.setValue(_p28_smap1.cdagente);
           agente.setReadOnly(true);
           _p28_ramo5AgenteSelect(agente,_p28_smap1.cdagente);
       }
       else if(
               RolSistema.PromotorAuto === _p28_smap1.cdsisrol
               ||
               RolSistema.puedeSuscribirAutos(_p28_smap1.cdsisrol) 
              )
       {
           agente.on(
           {
               'select' : _p28_ramo5AgenteSelect
           });
       }
        //agente
        
        //cliente nuevo
        if(!Ext.isEmpty(_fieldLikeLabel('CLIENTE NUEVO',null,true)))
        {
            combcl.on(
            {
                change : _p28_ramo5ClienteChange
            });
            combcl.getStore().on('load',function()
            {
                debug('combo cliente nuevo store load');
                combcl.setValue('S');
            });
            combcl.setValue('S');
         }
        //cliente nuevo
        
        
        if(('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1))
        {   
            //version
            version.anidado = true;
            version.heredar = function()
            {
                version.getStore().load(
                {
                    params :
                    {
                        'params.submarca' : submarca.getValue()
                        ,'params.modelo'  : modelo.getValue()
                    }
                });
            };
            
            submarca.on(
            {
                select : function(){ version.reset(); }
            });
            
            modelo.on(
            {
                select : function()
                {
                    version.getStore().load(
                    {
                        params :
                        {
                            'params.submarca' : submarca.getValue()
                            ,'params.modelo'  : modelo.getValue()
                        }
                    });
                }
                ,change : function()
                {
                    tipoValor.setValue('');
                }
            });
            //version
            
            //clave
            clave.on(
            {
                select : function(combo,records){ _p28_herenciaDescendiente(clave,marca,submarca,modelo,version,records[0]); }
            });
            
            modelo.on(
            {
                select : function() { _p28_herenciaAscendente(clave,marca,submarca,modelo,version); }
            });
            
            version.on(
            {
                select : function() { _p28_herenciaAscendente(clave,marca,submarca,modelo,version); }
            });
            //clave
        }
        //Cambia Atributos campo Fecha de nacimiento dependiendo del seguro de vida
        if(!Ext.isEmpty(_fieldByLabel('SEGURO DE VIDA',null,true)))
        {
            _fieldByLabel('SEGURO DE VIDA').on(
                            {
                                select : _p28_atributoNacimientoContratante
                            });
        }
        //tipovalor
        if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE FACTURA',null,true)))
        {
            _fieldLikeLabel('FECHA DE FACTURA').hide();
        }
    
        if(!Ext.isEmpty(_fieldByLabel('TIPO VALOR',null,true)))
         {
            tipoValor.on(
            {
                select : function()
                {
                    var valor = tipoValor.getValue();
                    if(_p28_smap1.cdramo+'x'=='5x'&&valor-0==3)
                    {
                        var modelo = _fieldByLabel('MODELO').getValue()-0;
                        var anioAc = new Date().getFullYear()-0;
                        if(anioAc-modelo>1)
                        {
                            mensajeWarning('Solo se permite para modelos del a&ntilde;o actual o anterior');
                            tipoValor.setValue('');
                        }
                        _p28_cargarRangoValorRamo5();
                    }
                    else
                    {
                        _p28_cargarRangoValorRamo5();
                    }
     /* ------------------------------- VALOR FACTURA ------------------- */
                 
                   var valor = tipoValor.getValue();
                    if(valor-0==3)/* 3 = VALOR FACTURA*/
                    {
                        var numeroDiasFechaFacturacion;
                        
                         Ext.Ajax.request(
                                 { 
                                     url     : _p28_urlRecuperacion
                                 
                                     ,params :
                                     {
                                         'params.consulta' :'RECUPERAR_DIAS_FECHA_FACTURACION'
                                        ,'params.cdtipsit' : _p28_smap1.cdtipsit
                                         
                                        }
                                        ,success : function(response)
                                        {                                   
                                            try
                                            {
                                                var json = Ext.decode(response.responseText);
                                                debug('### load status:',json);
                                                if(json.success==true)
                                                {
                                                    numeroDiasFechaFacturacion = json.params.dias;
                                                    var hoy = new  Date();
                                                    var limite = Ext.Date.add(hoy, Ext.Date.DAY,-1*(numeroDiasFechaFacturacion));
                                                    
                                                    _fieldLikeLabel('FECHA DE FACTURA').setMinValue(limite);
                                                    
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
                                            errorComunicacion(null,'Error al dias de factura');
                                        }
                                    });
                            
                            
                        var hoy = new  Date();
                        var limite = Ext.Date.add(hoy, Ext.Date.DAY,numeroDiasFechaFacturacion);
                        if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE FACTURA',null,true)))
                        {
                            _fieldLikeLabel('FECHA DE FACTURA').setMinValue(limite);
                            _fieldLikeLabel('FECHA DE FACTURA').setMaxValue(hoy);
                            _fieldLikeLabel('FECHA DE FACTURA').show();
                            _fieldLikeLabel('FECHA DE FACTURA').allowBlank=false;
                        }
                    }
            else
                    {
                        if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE FACTURA',null,true)))
                        {
                            _fieldLikeLabel('FECHA DE FACTURA').hide();
                            _fieldLikeLabel('FECHA DE FACTURA').allowBlank=true;
                        }
                    }
                    
                    }
            });
            //tipovalor
         }
        
        //sumaAsegurada
        if(('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1))
        {
            sumaAsegu.on(
            {
                change : function(){ _p28_limitarCoberturasDependientesSumasegRamo5(); }
            });
        }
        //sumaAsegurada
        
        //parametrizacion coberturas
        if(!Ext.isEmpty(_fieldLikeLabel('NEGOCIO',null,true)))
        _fieldByLabel('NEGOCIO').on(
        {
            //llama filtrando record
            change : function(){ _p28_cargarParametrizacionCoberturas(); }
        });
        if(!Ext.isEmpty(_fieldLikeLabel('TIPO PERSONA',null,true)))
        _fieldByLabel('TIPO PERSONA').on(
        {
            //llama filtrando record
            change : function(){ _p28_cargarParametrizacionCoberturas(); }
        });
        if(!Ext.isEmpty(_fieldLikeLabel('TIPO SERVICIO',null,true)))
        _fieldByLabel('TIPO SERVICIO').on(
        {
            change : function(me,val)
            {
                if(me.findRecord('key',val)!=false)
                {
                    ////llama filtrando record
                    _p28_cargarParametrizacionCoberturas();
                    if(!Ext.isEmpty(_fieldLikeLabel('CLAVE GS',null,true)) && ('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1))
                    {
                        var yy = _fieldLikeLabel('CLAVE GS');
                        _fieldLikeLabel('CLAVE GS').store.proxy.extraParams['params.servicio']=val;
                    }
                }
            }
        });
        //parametrizacion coberturas
        
        //uso
        if('TL'.lastIndexOf(_p28_smap1.cdtipsit)==-1 && !Ext.isEmpty(_fieldByLabel('TIPO USO',null,false)))
        {
            var usoCmp;
            usoCmp = _fieldByLabel('TIPO USO');
            debug('@CUSTOM uso:',usoCmp);
            usoCmp.anidado = true;
            usoCmp.heredar = function(remoto,micallback)
            {
                    debug('usoCmp.heredar');
                var negocioCmp  = _fieldByLabel('NEGOCIO');
                var servicioCmp = _fieldByLabel('TIPO SERVICIO');
                var negocioVal  = negocioCmp.getValue();
                var servicioVal = servicioCmp.getValue();
                debug('negocioVal:'  , negocioVal);
                debug('servicioVal:' , servicioVal);
                if(!Ext.isEmpty(negocioVal)
                    &&!Ext.isEmpty(servicioVal)
                    )
                {
                    _fieldByLabel('TIPO USO').getStore().load(
                    {
                        params :
                        {
                            'params.cdnegocio' : negocioVal
                            ,'params.servicio' : servicioVal
                        }
                        ,callback : function()
                        {
                            var me  = _fieldByLabel('TIPO USO');
                            var val = me.getValue();
                            var den = false;
                            me.getStore().each(function(record)
                            {
                                if(record.get('key')==val)
                                {
                                    den = true;
                                }
                            });
                            if(!den)
                            {
                                me.clearValue();
                            }
                            
                            if(!Ext.isEmpty(_fieldLikeLabel('CLAVE GS',null,true)) && ('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1))
                            {_fieldLikeLabel('CLAVE GS').store.proxy.extraParams['params.uso']=me.getValue();}
                            
                            if(!Ext.isEmpty(micallback))
                            {
                                micallback(_fieldByLabel('TIPO USO'));
                            }
                        }
                    });
                }
                else
                {
                    if(!Ext.isEmpty(micallback))
                    {
                        micallback(_fieldByLabel('TIPO USO'));
                    }
                }
            };

            
        _fieldByLabel('NEGOCIO').on(
        {
            change : function(me,val)
            {
                if(me.findRecord('key',val)!=false)
                {
                    _fieldByLabel('TIPO USO').heredar(true);
                }
            }
        });
            _fieldByLabel('TIPO SERVICIO').on(
                    {
                        change : function(me,val)
                        {
                            if(me.findRecord('key',val)!=false)
                            {
                                _fieldByLabel('TIPO USO').heredar(true);
                            }
                        }
                    });
            
            usoCmp.on(
                    {
                        change : function(me,val)
                        {
                            valido = false;
                            var valido  = !Ext.isEmpty(_fieldLikeLabel('CLAVE GS',null,true))
                                        &&!Ext.isEmpty(_fieldLikeLabel('MODELO',null,true))
                                        && ('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1);
                            if(valido)
                            {
                                var claveCmp = "";
                                if(!Ext.isEmpty(_fieldLikeLabel('CLAVE GS',null,true)))
                                {claveCmp=_fieldLikeLabel('CLAVE GS');}
                                var modelo = "";
                                if(!Ext.isEmpty(_fieldLikeLabel('MODELO',null,true)))
                                {modelo = _fieldByLabel('MODELO').getValue();}
                                claveCmp.store.proxy.extraParams['params.uso']=val;
                                if(!Ext.isEmpty(claveCmp.getValue())&&!Ext.isEmpty(modelo))
                                 {
                                    var fs = _fieldById('_p28_fieldsetVehiculo');
                                    for(var i in fs.items.items)
                                    {
                                        try
                                        {
                                            fs.items.items[i].setValue();
                                            fs.items.items[i].clearValue();
                                        }
                                        catch(e)
                                        {
                                            debugError(e);
                                        }
                                    }
                                }
                            }
                        }
                    });
    }
                    //uso
     
        
        //camion
        if(_p28_smap1.cdtipsit+'x'=='CRx')
        {
            _fieldLikeLabel('CLAVE GS').on(
            {
                select : function(){ _p28_recuperaObligatorioCamionRamo5(); }
            });
            _fieldLikeLabel('VALOR VEH').on(
            {
                change : function(){ _p28_recuperaObligatorioCamionRamo5(); }
            });
            _fieldLikeLabel('LIZA TRACTOCAMI').allowBlank=false;
            _fieldLikeLabel('LIZA TRACTOCAMI').on(
            {
                blur : function(me)
                {
                    var val=me.getValue();
                    if(!Ext.isEmpty(val)&&val+'x'!='x')
                    {
                        me.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url     : _p28_urlValidarTractocamionRamo5
                            ,params :
                            {
                                'smap1.poliza' : val
                                ,'smap1.rfc'   : Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.RFCCLI
                            }
                            ,success : function(response)
                            {
                                me.setLoading(false);
                                var json = Ext.decode(response.responseText);
                                debug('### tractocamion:',json);
                                if(!json.exito)
                                {
                                    mensajeWarning(json.respuesta);
                                    me.setValue('');
                                }
                            }
                            ,failure : function()
                            {
                                me.setLoading(false);
                                me.setValue('');
                                errorComunicacion();
                            }
                        });
                    }
                }
            });
        }
        //camion
        
        //negocio
        try{
        	
        	if(('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')!=-1)){
 					      		
        		_fieldByLabel('NEGOCIO').on({
        			change : function(me,val){
        				
        				if(me.findRecord('key',val)!=false)
                        {
        					var negoCmp = _fieldByLabel('NEGOCIO');
                            var negoVal = negoCmp.getValue();
                            negoCmp.setLoading(true);
                            Ext.Ajax.request(
                            {
                                url     : _p28_urlCargarDetalleNegocioRamo5
                                ,params :
                                {
                                    'smap1.negocio' : negoVal,
                                    'smap1.cdramo'  : _p28_smap1.cdramo,
                                    'smap1.cdtipsit': _p28_smap1.cdtipsit
                                }
                                ,success : function(response)
                                {
                                	 negoCmp.setLoading(false);
                                     var json = Ext.decode(response.responseText);
                                     debug('### detalle negocio:',json);
                                  
                                     _p28_negocio=json.smap1;
                                     
                                     if((_p28_negocio.DXN+'').trim()=='' || (_p28_negocio.DXN+'').trim()==0 || _p28_negocio.DXN==null )
                                     {
                                         _fieldById('fieldDXN').hide();
                                         _fieldByLabel("ADMINISTRADORA").clearValue();
                                         _fieldByLabel("RETENEDORA").clearValue();
                                     }else{
                             			 agregarAgenteDXN();
                                         administradoraAgenteDXN();
                                         _fieldById('fieldDXN').show();
                                     }
                                }
                                ,failure : function()
                                {
                                    negoCmp.setLoading(false);
                                    errorComunicacion();
                                }
                            });
                        }
        			}		
        		});
        	}
        }catch(e){
        	
        	debugError(e);
        }
         if(('|TV|TL|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1))
        {
            _fieldByLabel('NEGOCIO').on(
            {
                change : function(me,val)
                {
                    if(me.findRecord('key',val)!=false)
                    {
                        marca.getStore().load(
                        {
                            params :
                            {
                                'params.cdnegocio' : val
                            }
                        });
                        if(_p28_smap1.cdtipsit+'x'=='CRx'||_p28_smap1.cdtipsit+'x'=='PCx')
                        {
                            _fieldByLabel('CARGA').getStore().load(
                            {
                                params :
                                {
                                    'params.negocio' : _fieldByLabel('NEGOCIO').getValue()
                                }
                            });
                        }
                        
                        var negoCmp = _fieldByLabel('NEGOCIO');
                        var negoVal = negoCmp.getValue();
                        negoCmp.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url     : _p28_urlCargarDetalleNegocioRamo5
                            ,params :
                            {
                                'smap1.negocio' : negoVal,
                                'smap1.cdramo'  : _p28_smap1.cdramo,
                                'smap1.cdtipsit': _p28_smap1.cdtipsit
                            }
                            ,success : function(response)
                            {
                                negoCmp.setLoading(false);
                                var json = Ext.decode(response.responseText);
                                debug('### detalle negocio:',json);
                             
                                _p28_negocio=json.smap1;
                                
                                if((_p28_negocio.DXN+'').trim()=='' || (_p28_negocio.DXN+'').trim()==0 || _p28_negocio.DXN==null )
                                {
                                    _fieldById('fieldDXN').hide();
                                    _fieldByLabel("ADMINISTRADORA").clearValue();
                                    _fieldByLabel("RETENEDORA").clearValue();
                                }else{
                        agregarAgenteDXN();
                                    administradoraAgenteDXN();
                                    _fieldById('fieldDXN').show();
                                }
                                  
                                
                                plazoenanios = Number(json.smap1.LIMITE_SUPERIOR);
//                              _fieldByName('FESOLICI').setMaxValue(Ext.Date.add(new Date(),Ext.Date.YEAR, Number(json.smap1.LIMITE_SUPERIOR)));
                                if(!Ext.isEmpty(_fieldByName('feini').getValue()))
                                {
                                    _fieldByName('fefin').setMaxValue(Ext.Date.add(_fieldByName('feini').getValue(),Ext.Date.YEAR,plazoenanios));
                                }
                                else
                                {
                                    _fieldByName('fefin').setMaxValue(Ext.Date.add(new Date(),Ext.Date.YEAR,plazoenanios));
                                }
                                if(Number(json.smap1.MULTIANUAL) != 0) {
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
                                negoCmp.setLoading(false);
                                errorComunicacion();
                            }
                        });
                    }
                }
            });
        }
        //negocio
        
        //asistencia eua y canada
        if(_p28_smap1.cdtipsit+'x'=='ARx'||_p28_smap1.cdtipsit+'x'=='PPx')
        {
            var canadaCmp = _fieldLikeLabel('CANAD');
            debug('@CUSTOM canada:',canadaCmp);
            canadaCmp.anidado = true;
            canadaCmp.heredar = function(remoto,micallback)
            {
                var me        = _fieldLikeLabel('CANAD');
                var postalCmp = _fieldLikeLabel('CIRCULACI');
                var postalVal = postalCmp.getValue();
                if((postalVal+'x').length==6)
                {
                    me.setLoading(true);
                    Ext.Ajax.request(
                    {
                        url     : _p28_urlRecuperacionSimple
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
        
            var postalCmp = _fieldLikeLabel('CIRCULACI');
            postalCmp.on(
            {
                change : function()
                {
                    _fieldLikeLabel('CANAD').heredar();
                }
            });
        }
        //asistencia eua y canada
    }
    //ramo 5
    
    
    if(_p28_smap1.cdramo==Ramo.ServicioPublico)
    {
        
        debug('>parche para ramo 6');
        
        try{
        	_fieldByLabel('AGENTE',null,true).on({
        		change:function(){
        			_fieldByName('fefin').setValue(
            	            Ext.Date.add(_fieldByName('feini').getValue(),Ext.Date.MONTH,_fieldByName('parametros.pv_otvalor20',null,true).getValue())
            	        );
        		}
        	});
    	        
        	
        }catch(e){
        	debugError(e);
        }
        
        //FRILTRO DE PLANES
        try{
            
	        _fieldByLabel('PLAN').store.on({
	                                    load      : function(store){
	                                                   store.insert(0,{"aux":null,"aux2":null,"aux3":null,"key":null,"value":"(TODOS)"})
	                                                }
	        });
        }catch(e){
            debugError(e)
        }

        //PARCHE PARA TOUPPERCASE EN TEXTFIELDS
        
        Ext.ComponentQuery.query('[xtype=textfield]').forEach(function(item,idx,arr){
            item.on({
                        change:function(){
                            try
                            {
                               if('string' == typeof item.getValue() && true !== item.sinmayus)
                               {
                                                                            //debug('mayus de '+this.getValue());
                                   item.setValue(item.getValue().toUpperCase());
                               }
                            }
                           catch(e){}
                        }
                      })
         });
        
        try{
            //OCULTANDO EL CAMPO FOLIO QUE VIENE DE LA BASE
            Ext.ComponentQuery.query('[cdatribu=16]')[0].hide();
        }catch(e){
            debugError(e);
        }
        //EJECUTANDO TARJETAS DE NEGOCIO
        try{
            _fieldByLabel('TIPO DE INDEMNIZACION').on({
                select:function(){
                    _p28_cargarParametrizacionCoberturas();
                }
            });
            Ext.ComponentQuery.query('[fieldLabel=NEGOCIO],[fieldLabel="TIPO PERSONA"]') //,[fieldLabel=MODELO],[fieldLabel=SUBMARCA],[fieldLabel*="CLAVE GS"]
            .forEach(function(it,idx){
                debug("item: ",it.fieldLabel);
                if(it.xtype=='combobox'){
                    it.on({
                        select:function(){
                            _p28_cargarParametrizacionCoberturas();
                        },
                        change:function(){
                            _p28_cargarParametrizacionCoberturas();
                        }
                    })
                }else{
                    it.on({
                        blur:function(){
                            _p28_cargarParametrizacionCoberturas();
                        }
                    })
                }
             });
            
        }catch(e){
            debugError(e)
        }
        
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
                            	try{
	                                _fieldLikeLabel('DESCUENTO').setMinValue(ijson.smap1.RANGO_MINIMO);
	                                _fieldLikeLabel('DESCUENTO').setMaxValue(ijson.smap1.RANGO_MAXIMO);
                            	}catch(e){
                            		debugError(e)
                            	}
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
                url      : _p28_urlCargarParametros
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
        
        //_0_gridIncisos.setTitle('Datos del contratante prospecto');
        
        var agente = _fieldByName('parametros.pv_otvalor17');
        var folio  = _fieldByName('parametros.pv_otvalor16');
        
        
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
                    _mask();
                    Ext.Ajax.request(
                    {
                        url      : _p28_urlCargarParametros
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
                            _unmask();
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
                            _unmask();
                            errorComunicacion();
                        }
                    });
                }
            });
        }
        
        //_fieldByName('fefin').setValue('');
        //_fieldByName('feini').removeListener('change',_0_funcionFechaChange);
        _fieldByName('feini').on(
        {
            change : function()
            {
                
                if(Ext.isEmpty(_fieldByName('parametros.pv_otvalor20').getValue()))
                {
                    mensajeWarning('Favor de capturar la vigencia');
                }
                else
                {
    //                 debug('val:',val);
    //                 var fefin = _fieldByName('fefin');
    //                 fefin.setValue(Ext.Date.add(val,Ext.Date.YEAR,1));
    //                 fefin.setMinValue(Ext.Date.add(val,Ext.Date.DAY,1));
    //                 fefin.isValid();
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
        
        combcl.on({
            change:_p28_ramo5ClienteChange //usamos la misma funcion que el ramo 5
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

    
    
    //ramo 6
    if(_p28_smap1.cdramo==Ramo.ServicioPublico)
    {
     
       /////SOLO AUTOS SERVICIO PUBLICO///////
       if(_p28_smap1.cdtipsit==TipoSituacion.ServicioPublicoAuto){
           tipoUnidadFronteriza();
           _fieldByName('parametros.pv_otvalor35').allowBlank=true;
           //FILTRO PARA TIPO DE UNIDAD
           try{
        	   Ext.ComponentQuery.query('[name=parametros.pv_otvalor22]')
        	   .forEach(function(it){ 
        		   it.getStore().filter(
        				   [
        					   {
        						   filterFn: function(item) { 
        							   debug('FILTRANDO CLAVE GS POR TIPO DE UNIDAD');
        							   if(!_fieldByLabel('TIPO DE UNIDAD')){
        								   return true;
        							   }
        					   		   return (item.get("value")+"")
        					   		   			.indexOf(
        					   		   					_fieldByLabel('TIPO DE UNIDAD').getRawValue()
        					   		   					) != -1;
			}}])  });
           }catch(e){
        	   debugError(e)
           }
       }
       /////SOLO AUTOS SERVICIO PUBLICO///////
       
          // FECHA FIN DE VIGENCIA SOLO LECTURA
          _fieldByLabel("FIN DE VIGENCIA").setReadOnly(true);
        
      //agente
        var agente;
        if(!Ext.isEmpty(_fieldByLabel('AGENTE',null,true)))
        {
            agente    = _fieldByLabel('AGENTE');
        }
        if(_p28_smap1.cdsisrol=='EJECUTIVOCUENTA')
        {
            
            agente.setValue(_p28_smap1.cdagente);
            agente.setReadOnly(true);
            _p28_ramo5AgenteSelect(agente,_p28_smap1.cdagente);
        }
        else (
                '|PROMOTORAUTO'.lastIndexOf(_p28_smap1.cdsisrol)!=-1
                &&
                RolSistema.puedeSuscribirAutos(_p28_smap1.cdsisrol) 
             )
            /* if(_p28_smap1.cdsisrol=='PROMOTORAUTO'||_p28_smap1.cdsisrol=='SUSCRIAUTO') */
        {
            agente.on(
            {
                'select' : _p28_ramo5AgenteSelect
            });
        }
        //agente
        
        // ORDENANDO items del formulario
        try{
	        var itIzq=_fieldById('_p28_form').items.items[0].items.items;
	        
	        
	        var idxVeh=-1;
	        for(var i=0;i< itIzq.length;i++){
	            if(typeof itIzq[i] == 'object' && itIzq[i].itemId=='_p28_fieldsetVehiculo' ){
	                idxVeh=i;
	                break;
	            }
	        }
	        //lugar dependiendo de cdtipsit
	        var lugar=_p28_smap1.cdtipsit+'x'=='ATx'?5:4;
	        itIzq.splice(lugar, 0, itIzq[idxVeh]);
	        itIzq.splice(idxVeh+1, 1);
	        debug("->>",idxVeh,itIzq);
	        
	        
	        idxVeh=-1
	        for(var i=0;i< itIzq.length;i++){
	            if(typeof itIzq[i] == 'object' && itIzq[i].itemId=='_p28_fieldsetCliente' ){
	                idxVeh=i;
	                break;
	            }
	        }
	        //lugar dependiendo de cdtipsit
            lugar=_p28_smap1.cdtipsit+'x'=='ATx'?6:5;
	        itIzq.splice(lugar, 0, itIzq[idxVeh]);
	        itIzq.splice(idxVeh+1, 1);
	        
	        debug("->>",idxVeh,itIzq);
	        
	        //LUGAR DE TIPO DE UNIDAD
	        try{
	            _fieldByName('datos_generales').insert(3,_fieldByLabel('TIPO DE UNIDAD'));
	        }catch(e){
	            debugError(e);
	        }
        }catch(e){
            debugError(e);
        }
        
        // ORDENANDO
        
        // AGREGANDO FECHA DE SOLICITUD
        
        _fieldById('_p28_form').items.items[0].add({
                name        : 'FESOLICI'
                ,fieldLabel : 'FECHA DE SOLICITUD'
                ,xtype      : 'datefield'
                ,format     : 'd/m/Y'
                ,editable   : true
                ,allowBlank : false
                ,value      : new Date()
                ,style      : 'margin-left:15px;'
            });
        //ordenando fecha de solicitud
        for(var i=0;i< itIzq.length;i++){
            if(typeof itIzq[i] == 'object' && itIzq[i].name=='FESOLICI' ){
                idxVeh=i;
                break;
            }
        }
        itIzq.splice(itIzq.length-3, 0, itIzq[idxVeh]);
        itIzq.splice(idxVeh+1, 1);
        
        _fieldById('_p28_form').doLayout();
        
        // AGREGANDO FECHA DE SOLICITUD
        
        
        // OCULTANDO DATOS ADICIONALES A LA POLIZA
        _fieldById('_p28_fieldsetTatripol').hide();
        
        // CARGANDO DATOS AUTO (CLAVE GS ...
        _fieldByName('parametros.pv_otvalor22').on(
            {
                'select' : function(comp,arr)
                {
                    
                    var tmp=_fieldByName('parametros.pv_otvalor22').getValue()
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
                    
                   
                    _fieldByName('parametros.pv_otvalor22').setValue(tmp)
                    _0_cargarNumPasajerosAuto();
                }
            });
    }    
    
    //codigo dinamico recuperado de la base de datos
    <s:property value="smap1.customCode" escapeHtml="false" />
    
    //obigatorio si hay administradora
    
    agregarAgenteDXN();
    administradoraAgenteDXN();
    ////// custom //////
    
    ////// loaders //////
    _p28_cargarConfig();
    
    if('|AR|CR|PC|PP|TV|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')!=-1)
    {
        debug('@CUSTOM valor max');
        var valorCmp = _fieldLikeLabel('VALOR VEH');
        Ext.Ajax.request(
        {
            url     : _p28_urlRecuperacionSimple
            ,params :
            {
                'smap1.procedimiento' : 'RECUPERAR_VALOR_MAXIMO_SITUACION_POR_ROL'
                ,'smap1.cdtipsit'     : _p28_smap1.cdtipsit
            }
            ,success : function(response)
            {
                var json = Ext.decode(response.responseText);
                debug('### valor maximo por rol:',json);
                if(json.exito)
                {
                    valorCmp.maximoTotal = json.smap1.VALOR;
                    valorCmp.validator = function(val)
                    {
                        var me = _fieldLikeLabel('VALOR VEH');
                        debug('validar contra ',me.maximoTotal);
                        if(Number(val)>Number(me.maximoTotal))
                        {
                            if(_p28_smap1.cdsisrol =='EJECUTIVOCUENTA'){
                                return 'Favor de acudir a Mesa de Control para realizar la cotización';
                            }else{
                                return 'El valor m&aacute;ximo es '+me.maximoTotal;
                            }
                        }
                        return true;
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
    }
    
    if('TV'.lastIndexOf(_p28_smap1.cdtipsit)!=-1)
            {
                Ext.Ajax.request(
                {
                    url : _p28_urlCargarParametros
                    ,params  :
                    {
                         'smap1.parametro' : 'RANGO_ANIO_MODELO'
                        ,'smap1.cdramo'   : _p28_smap1.cdramo
                        ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### obtener rango años response:',json);
                        if(json.exito)
                        {
                            var modeloCmp = _fieldByName('parametros.pv_otvalor09');
                            modeloCmp.limiteInferior = json.smap1.P1VALOR-0;
                            modeloCmp.limiteSuperior = json.smap1.P2VALOR-0;
                            
                            modeloCmp.validator=function(value)
                            {
                                var modeloCmp = _fieldByName('parametros.pv_otvalor09');
                                
                                var r = true;
                                var anioActual = new Date().getFullYear()-0;
                                var max = anioActual+modeloCmp.limiteSuperior;
                                var min = anioActual+modeloCmp.limiteInferior;
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
                
                Ext.Ajax.request(
                {
                    url : _p28_urlCargarParametros
                    ,params  :
                    {
                         'smap1.parametro' : 'RANGO_DERECHO_AGENTE'
                        ,'smap1.cdramo'   : _p28_smap1.cdramo
                        ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### obtener rango años response:',json);
                        if(json.exito)
                        {
                            var derechoAgenteCmp = _fieldByName('parametros.pv_otvalor50', null, true);
                            derechoAgenteCmp.limiteInferior = json.smap1.P1VALOR;
                            derechoAgenteCmp.limiteSuperior = json.smap1.P2VALOR;
                            /* se coloca en comentario a solicitud de Angeles
                            derechoAgenteCmp.validator=function(value)
                            {
                                var derechoAgenteCmp = _fieldByName('parametros.pv_otvalor50', null, true);
                                
                                var r = true;
                                var max = derechoAgenteCmp.limiteSuperior;
                                var min = derechoAgenteCmp.limiteInferior;
                                debug('max:',max);
                                debug('min:',min);
                                debug('value:',value);
                                if(value<min||value>max)
                                {
                                    r='El modelo debe estar en el rango '+min+'-'+max;
                                }
                                return r;
                           };
                           */
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
                
            }
    
    _p28_recuperarClienteTramite();
    
    _p28_recuperarCotizacionDeTramite();
    
    _p28_recuperarPolizaSIGS();
    
    ////// loaders //////
});

////// funciones //////}

function _p28_camposEstacionamiento(combo)
{
    var val = 'N';
    
    if(combo != 'N')
    {
        val = combo.getValue();
    }
    
    debug('_p28_camposEstacionamiento val:',val,'.');
    
    if(val == 'S')
        {
            _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').show();
            _fieldLikeLabel('ACOMODADORES').show();
            _fieldLikeLabel('NUMERO DE CAJONES').show();
            _fieldLikeLabel('SUBLIMITE POR CAJON').show();
            _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').show();
            
            _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').allowBlank=false; 
            _fieldLikeLabel('ACOMODADORES').allowBlank=false;
            _fieldLikeLabel('NUMERO DE CAJONES').allowBlank=false;
            _fieldLikeLabel('SUBLIMITE POR CAJON').allowBlank=false;
            _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').allowBlank=false;
        }
    else
        {
            _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').hide();
            _fieldLikeLabel('ACOMODADORES').hide();
            _fieldLikeLabel('NUMERO DE CAJONES').hide();
            _fieldLikeLabel('SUBLIMITE POR CAJON').hide();
            _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').hide();
            
            _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').reset();
            _fieldLikeLabel('ACOMODADORES').reset();
            _fieldLikeLabel('NUMERO DE CAJONES').reset();
            _fieldLikeLabel('SUBLIMITE POR CAJON').reset();
            _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').reset();
            
            _fieldLikeLabel('TIPO DE ESTACIONAMIENTO').allowBlank=true; 
            _fieldLikeLabel('ACOMODADORES').allowBlank=true;
            _fieldLikeLabel('NUMERO DE CAJONES').allowBlank=true;
            _fieldLikeLabel('SUBLIMITE POR CAJON').allowBlank=true;
            _fieldLikeLabel('L.U.C. ESTACIONAMIENTO').allowBlank=true;
        }
}

function _p28_cotizar(sinTarificar)
{
    debug('>_p28_cotizar sintarifa:',sinTarificar,'DUMMY');
    var administradora=_fieldByName("aux.otvalor08").getValue();
    var retenedora=_fieldByName("aux.otvalor09").getValue();
    
    if(administradora!=null && (administradora+'').trim()!='' && administradora!="-1"){
        if(retenedora==null || (retenedora+'').trim()==''){
            mensajeWarning('Favor de registrar una retenedora');
            
            return ;
        }
    }

    var panelpri = _fieldById('_p28_panelpri');
    var form     = _fieldById('_p28_form');
    var valido   = form.isValid();
    debug('p28_form:',_fieldById('_p28_form'));
    if(!valido)
    {
    	_fieldById('_p28_form').query("field{isValid()==false}").forEach(function(it){
        	debug("### Falta llenar : ",it," - ",it.getValue()," valido ",it.isValid())
        	debug("### msj: ",it.invalidText)
        });
        datosIncompletos();
    }
    
    if(valido)
    {
        valido = _fieldByName('nmpoliza').sucio==false;
        if(!valido)
        {
            _fieldByName('nmpoliza').semaforo = true;
            _fieldByName('nmpoliza').setValue('');
            _fieldByName('nmpoliza').semaforo = false;
            valido = true;
        }
    }
    
    if(valido)
    {
        //copiar paneles a oculto
        var arr = Ext.ComponentQuery.query('#_p28_gridTarifas');
        if(arr.length>0)
        {
            var formDescuentoActual = _fieldById('_p28_formDescuento');
            var formCesion          = _fieldById('_p28_formCesion');
            var recordPaneles       = new _p28_formModel(formDescuentoActual.getValues());
            var itemsCesion         = Ext.ComponentQuery.query('[fieldLabel]',formCesion);
            for(var i=0;i<itemsCesion.length;i++)
            {
                recordPaneles.set(itemsCesion[i].getName(),itemsCesion[i].getValue());
            }
            form.formOculto.loadRecord(recordPaneles);
            debug('form.formOculto.getValues():',form.formOculto.getValues());
        }
        
//      cdideperCli: "D10000001278417"
//      cdpersonCli: "525987"
        
        var smap = _p28_smap1;
        _p28_smap1['cdpersonCli'] = Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.CLAVECLI;
        _p28_smap1['cdideperCli'] = Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.CDIDEPER;
        _p28_smap1['nmorddomCli'] = Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.NMORDDOM;
        
        if(!Ext.isEmpty(sinTarificar)      &&  sinTarificar     == true)
        {
            _p28_smap1['notarificar'] = 'si';//Se utiliza para no retarid
        }
        else if(!Ext.isEmpty(sinTarificar) && sinTarificar != false && sinTarificar != true)
        {
            _p28_smap1['notarificar'] = 'no';//Se utiliza para no retarid
            _p28_smap1['modPrim']     = sinTarificar;
        }
        else
        {
            _p28_smap1['notarificar'] = 'no';//Se utiliza para no retarid
        }
        
        if(cargarXpoliza)
            {
            _p28_smap1['cdpersonCli'] = cdperson;
            _p28_smap1['cdideperCli'] = cdper ;
            }
        
        var agenteCmp=_fieldByLabel('AGENTE',_fieldById('_p28_form'),true);
        if(Ext.isEmpty(agenteCmp))
        {
            smap.cdagenteAux='';
        }
        else
        {
            smap.cdagenteAux=agenteCmp.getValue();
        }
    
        var json=
        {
            slist1 :
            [
                form.getValues()
            ]
            ,smap1 : smap
        };
        
       //json.slist1[0]["parametros.pv_otvalor13"] = json.slist1[0]["parametros.pv_otvalor13"].replace(",","");
       //json.slist1[0]["parametros.pv_otvalor27"] = json.slist1[0]["parametros.pv_otvalor27"].replace(",","");
       //json.slist1[0]["parametros.pv_otvalor29"] = json.slist1[0]["parametros.pv_otvalor29"].replace(",","");
               
        var valuesFormOculto = form.formOculto.getValues();
        for(var att in valuesFormOculto)
        {
            json.slist1[0][att]=valuesFormOculto[att];
            debug('Agregado a cotizacion:',att,':',valuesFormOculto[att]);
        }
        if(cargarXpoliza)
        {
            json.smap1.cargarXpoliza= 'S';
            json.smap1.AGENTESEC= _fieldLikeLabel('AGENTESEC').getValue();
            json.smap1.PORCENSEC= _fieldLikeLabel('PORCENSEC').getValue();
        }
        debug('json a enviar para cotizar:',json);

        panelpri.setLoading(true);
        Ext.Ajax.request(
        {
            url       : _p28_urlCotizar
            ,jsonData : json
            ,success  : function(response)
            {
                _p28_bloquear(true);
                panelpri.setLoading(false);
                json=Ext.decode(response.responseText);
                debug('### cotizar:',json);
                if(json.success==true)
                {
                    if(!Ext.isEmpty(json.smap1.msnPantalla))
                    {
                        mensajeCorrectoTarifas('Aviso',json.smap1.msnPantalla);
                    }
                    debug(Ext.decode(json.smap1.fields));
                    debug(Ext.decode(json.smap1.columnas));
                    debug(json.slist2);
                    
                    _fieldByName('nmpoliza').semaforo=true;
                    _fieldByName('nmpoliza').setValue(json.smap1.nmpoliza);
                    _fieldByName('nmpoliza').semaforo=false;
                    
                    if(Ext.isEmpty(sinTarificar)||false==sinTarificar)
                    {
                        _grabarEvento('COTIZACION'
                                      ,'COTIZA'
                                      ,_p28_smap1.ntramite
                                      ,_p28_smap1.cdunieco
                                      ,_p28_smap1.cdramo
                                      ,'W'
                                      ,json.smap1.nmpoliza
                                      ,json.smap1.nmpoliza
                                      ,'buscar'
                                      );
                    }
                    
                    Ext.define('_p28_modeloTarifa',
                    {
                        extend  : 'Ext.data.Model'
                        ,fields : Ext.decode(json.smap1.fields)
                    });
                    
                    var itemsDescuento =
                    [
                        {
                            xtype  : 'displayfield'
                            ,value : '¿Desea usar su descuento de agente?'
                        }
                        ,{
                            xtype  : 'displayfield'
                            ,value : 'Si desea aplicar un DESCUENTO seleccione el porcentaje mayor a 0%'
                        }
                        ,{
                            xtype  : 'displayfield'
                            ,value : 'Si desea aplicar un RECARGO seleccione el porcentaje menor a 0%'
                        }
                    ];
                    
                    var itemAntRefPol =
                        [
                            {
                                xtype  : 'displayfield'
                                ,value : 'Plan y forma de pago de p\u00f3liza a renovar: '+json.smap1.fila+'-'+json.smap1.columna
                            }
                        ];
                    
                    <s:if test='%{getImap().get("panel5Items")!=null}'>
                        var itemsaux = [<s:property value="imap.panel5Items" />];
                        for(var ii=0;ii<itemsaux.length;ii++)
                        {
                            debug('itemsaux[ii]:',itemsaux[ii]);
                            itemsDescuento.push(itemsaux[ii]);
                        }
                    </s:if>
                    
                    var itemsComision =
                    [
                        {
                            xtype  : 'displayfield'
                            ,value : 'Indique el porcentaje de comisi&oacute;n que desea ceder'
                        }
                    ];
                    
                    <s:if test='%{getImap().get("panel6Items")!=null}'>
                        var itemsaux = [<s:property value="imap.panel6Items" />];
                        for(var ii=0;ii<itemsaux.length;ii++)
                        {
                            itemsaux[ii].minValue=0;
                            itemsaux[ii].maxValue=100;
                            itemsComision.push(itemsaux[ii]);
                        }
                    </s:if>
                    debug('itemsComision:',itemsComision);
                    
                    var arr = Ext.ComponentQuery.query('#_p28_gridTarifas');
                    if(arr.length>0)
                    {
                        _fieldById('_p28_formCesion').destroy();
                        panelpri.remove(arr[arr.length-1],true);
                    }
                    
                    var _p28_formDescuento = Ext.create('Ext.form.Panel',
                    {
                        itemId        : '_p28_formDescuento'
                        ,border       : 0
                        ,defaults     : { style : 'margin:5px;' }
                        ,style        : 'margin-left:535px;'
                        ,width        : 450
                        ,hidden: _p28_smap1.cdramo==Ramo.ServicioPublico
                        ,windowCesion : Ext.create('Ext.window.Window',
                        {
                            title        : 'CESI&Oacute;N DE COMISI&Oacute;N'
                            ,autoScroll  : true
                            ,closeAction : 'hide'
                            ,modal       : true
                            ,items       :
                            [
                                Ext.create('Ext.form.Panel',
                                {
                                    itemId       : '_p28_formCesion'
                                    ,border      : 0
                                    ,defaults    : { style : 'margin:5px;' }
                                    ,items       : itemsComision
                                    ,buttonAlign : 'center'
                                    ,buttons     :
                                    [
                                        {
                                            itemId   : '_p28_botonAplicarCesion'
                                            ,text    : 'Aplicar'
                                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                            ,handler : function(me)
                                            {
                                                if(me.up('form').getForm().isValid())
                                                {
                                                    me.up('window').hide();
                                                    
                                                    _p28_cotizar(true);
                                                }
                                                else
                                                {
                                                    mensajeWarning('Favor de verificar los datos');
                                                }
                                            }
                                        }
                                    ]
                                })
                            ]
                        })
                        ,items       :
                        [
                            {
                                xtype  : 'fieldset'
                                ,title : '<span style="font:bold 14px Calibri;">DESCUENTO DE AGENTE</span>'
                                ,items : itemsDescuento
                            }
                            ,{
                                xtype  : 'fieldset'
                                ,title : '<span style="font:bold 14px Calibri;">Datos de Renovacion</span>'
                                ,items : itemAntRefPol
                                ,hidden: !Ext.isEmpty(_p28_flujo) ? (_p28_flujo.cdflujomc != 220 && _p28_flujo.cdtipflu != 103) : true
                            }
                        ]
                        ,buttonAlign : 'right'
                        ,buttons     :
                        [
                            {
                                itemId   : '_p28_botonAplicarDescuento'
                                ,text    : 'Aplicar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                ,handler : function(me)
                                {
                                    if(me.up('form').getForm().isValid())
                                    {
//                                      var modPrim = Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p28_formDescuento'))[0].lastValue;
                                        _p28_cotizar(false);//_p28_cotizar(modPrim);
                                    }
                                    else
                                    {
                                        mensajeWarning('Favor de verificar los datos');
                                    }
                                }
                            }
                        ]
                    });
                    
                    _p28_formDescuento.loadRecord(new _p28_formModel(form.formOculto.getValues()));
                    _fieldById('_p28_formCesion').loadRecord(new _p28_formModel(form.formOculto.getValues()));
                    
                    //bloquear descuento
                    var arrDesc = Ext.ComponentQuery.query('[fieldLabel]',_p28_formDescuento);
                    var disabledDesc = false;
                    for(var i=0;i<arrDesc.length;i++)
                    {
                        if(arrDesc[i].getValue()-0!=0)
                        {
                            arrDesc[i].setReadOnly(true);
                            disabledDesc = true;
                        }
                    }
                    
                    if (disabledDesc === true && true === _p28_flujoAux.multiDesc) {
                        disabledDesc = false;
                    }
                    
                    _fieldById('_p28_botonAplicarDescuento').setDisabled(disabledDesc);
                    
                    //bloquear comision
                    var arrComi      = Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p28_formCesion'));
                    var disabledComi = false;
                    for(var i=0;i<arrComi.length;i++)
                    {
                        if(arrComi[i].getValue()-0!=0)
                        {
                            arrComi[i].setReadOnly(true);
                            disabledComi = true;
                        }
                    }
                    
                    if (disabledComi === true && true === _p28_flujoAux.multiDesc) {
                        disabledComi = false;
                    }
                    
                    _fieldById('_p28_botonAplicarCesion').setDisabled(disabledComi);
                    
                    /////////////// DXN //////////////////////////////
                    var formasPago=json.slist2,soloDXN=[];
                    var administradora=_fieldByName("aux.otvalor08");
                    var retenedora=_fieldByName("aux.otvalor09");
                    
                    if(retenedora.getValue()!=null && retenedora.getValue()!="-1" && retenedora.getValue()!=""){
                        var fpago=getFormaPago(administradora.getValue(),retenedora.getValue());
                        
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
                            
                            if((!FormaPago.esDxN((formasPago[i].CDPERPAG+'').trim())) && (formasPago[i].DSPERPAG+'').trim().indexOf("DXN")==-1 ){
                                
                                soloDXN.push(formasPago[i]);
                                
                            }
                            debug(formasPago[i]);
                        }
                        
                        formasPago=soloDXN;
                    }
                    ///////////////// DXN /////////////////////////////
                    
                    //// FORMAS DE PAGO PARA TURISTAS INICIO
					try{
						if(TipoSituacion.TuristaLicencia== _p28_smap1.cdtipsit || TipoSituacion.TuristaVehiculo==_p28_smap1.cdtipsit){
							formasPago=turistasFormaPago(_fieldByName('feini').getValue()
    								,_fieldByName('fefin').getValue()
    								,formasPago)
						}
					}catch(e){
						debugError(e)
					}                    	
                    //// FORMAS DE PAGO PARA TURISTAS FIN
                    
                    /////// FILTRO PARA LOS PLANES DE COTIZACION
                    var columnas = Ext.decode(json.smap1.columnas);
                    try{
                        
                        if(_p28_smap1.cdramo==Ramo.ServicioPublico  ){
		                    var columnas = Ext.decode(json.smap1.columnas);
		                    debug('PLANES: ',columnas);
		                    var plan = _fieldByLabel('PLAN').getValue();
		                    if(plan != null && (plan+"").trim() != "")
		                        Ext.Array.each(columnas,function(it,idx){
		                            if(it.dataIndex!="DSPERPAG" && it.dataIndex!='MNPRIMA'+plan ){
		                                it.hidden = true;
		                                debug('Columna oculta: ',it.dataIndex)
		                            }
			                        
			                    });
		                    debug('PLAN: ',plan);
		                    debug('PLANES filtrados: ',columnas);
                        }
                    }catch(e){
                        debugError(e);
                    }
                    
                    /////// FILTRO PARA LOS PLANES DE COTIZACION
                   var gridTarifas=Ext.create('Ext.panel.Panel',
                    {
                        itemId : '_p28_gridTarifas'
                        ,items :
                        [
                            Ext.create('Ext.grid.Panel',
                            {
                                title             : 'Resultados'
                                ,border           : 0
                                ,store            : Ext.create('Ext.data.Store',
                                {
                                    model : '_p28_modeloTarifa'
                                    ,data : formasPago
                                })
                                ,columns          : columnas
                                ,selType          : 'cellmodel'
                                ,minHeight        : 100
                                ,enableColumnMove : false
                                ,listeners        :
                                {
                                    select       : _p28_tarifaSelect
                                    ,afterrender : function(me)
                                    {
                                        if(!Ext.isEmpty(json.smap1.columna) && !Ext.isEmpty(json.smap1.fila))
                                        {
	                                        var gridTarifas = _fieldById('_p28_gridTarifas').down('grid');
	                                        var sm = gridTarifas.getSelectionModel();
	                                        try
	                                        {
	                                            var columna=0, fila=999; 
	                                            for(var IteGriTar=1;IteGriTar<gridTarifas.columns.length;IteGriTar++)
                                                {
	                                                if((gridTarifas.columns[IteGriTar].text).toLowerCase() === json.smap1.columna.toLowerCase())
                                                    {
                                                         columna = IteGriTar - columna;
                                                         IteGriTar = gridTarifas.columns.length + 1;
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
                                        
                                        if(!Ext.isEmpty(_p28_flujo)) // && sinTarificar !== true)
                                        {
                                            _p28_actualizarCotizacionTramite(_p28_actualizarSwexiperTramite);
                                        }
                                    }
                                }
//                                ,selType = cellModel
                            })
                            ,_p28_formDescuento
                            ,Ext.create('Ext.panel.Panel',
                            {
                                defaults : { style : 'margin:5px;' }
                                ,border  : 0
                                ,tbar    :
                                [
                                    '->'
                                    ,{
                                        itemId    : '_p28_botonDetalles'
                                        ,text     : 'Detalles'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
                                        ,disabled : true
                                        ,handler  : _p28_detalles
                                        ,hidden   : !RolSistema.puedeSuscribirAutos(_p28_smap1.cdsisrol) //(rolesSuscriptores.lastIndexOf('|'+_p28_smap1.cdsisrol+'|')==-1)
                                                    //_p28_smap1.cdsisrol!='SUSCRIAUTO'
                                    }
                                    ,{
                                        itemId    : '_p28_botonCoberturas'
                                        ,text     : 'Coberturas'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/table.png'
                                        ,disabled : true
                                        ,handler  : _p28_coberturas
                                    }
                                    ,{
                                        itemId   : '_p28_botonEditar'
                                        ,text    : 'Editar'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
                                        ,handler : _p28_editar
                                    }
                                    ,{
                                        itemId   : '_p28_botonClonar'
                                        ,text    : 'Duplicar'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                                        ,handler : _p28_clonar
                                    }
                                    ,{
                                        itemId   : '_p28_botonNueva'
                                        ,text    : 'Nueva'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                                        ,handler : _p28_nueva
                                    }
                                ]
                                ,bbar    :
                                [
                                    '->'
                                    ,{
                                        itemId    : '_p28_botonEnviar'
                                        ,xtype    : 'button'
                                        ,text     : 'Enviar'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/email.png'
                                        ,disabled : true
                                        ,handler  : _p28_enviar
                                        ,hidden	  : _p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo || _p28_smap1.cdtipsit==TipoSituacion.TuristaLicencia
                                    }
                                    ,{
                                        itemId    : '_p28_botonImprimir'
                                        ,xtype    : 'button'
                                        ,text     : 'Imprimir'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/printer.png'
                                        ,disabled : true
                                        ,handler  : _p28_imprimir
                                        ,hidden	  : _p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo || _p28_smap1.cdtipsit==TipoSituacion.TuristaLicencia
                                    }
                                    ,{
                                        itemId   : '_p28_botonCesion'
                                         ,xtype   : 'button'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/page_white_star.png'
                                        ,text    : 'Cesi&oacute;n de comisi&oacute;n'
                                        ,handler : _p28_cesionClic
                                        ,hidden	  : _p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo || _p28_smap1.cdtipsit==TipoSituacion.TuristaLicencia
                                    }
                                    ,{
                                        itemId    : '_p28_botonComprar'
                                        ,xtype    : 'button'
                                        ,text     : 'Confirmar cotizaci&oacute;n'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/book_next.png'
                                        ,disabled : true
                                        ,handler  : _p28_comprar
                                        ,hidden   : typeof _p28_flujoAux.onCotizar === 'number'
                                    }, {
                                        itemId  : '_p28_botonOnCotizar',
                                        xtype   : 'button',
                                        text    : 'Enviar',
                                        icon    : '${icons}user_go.png',
                                        hidden  : typeof _p28_flujoAux.onCotizar !== 'number',
                                        handler : _p28_botonOnCotizarClic
                                    }
                                ]
                            })
                        ]
                    });
                    
                    var panelPri = _fieldById('_p28_panelpri');
                    
                    panelPri.add(gridTarifas);
                    panelPri.doLayout();
                    
                    if(_p28_smap1.cdramo+'x'=='5x'&&arrDesc.length>0)
                    {
                        _p28_formDescuento.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url     : _p28_urlRecuperacionSimple
                            ,params :
                            {
                                'smap1.procedimiento' : 'RECUPERAR_DESCUENTO_RECARGO_RAMO_5'
                                ,'smap1.cdtipsit'     : _p28_smap1.cdtipsit
                                ,'smap1.cdagente'     : _fieldByLabel('AGENTE').getValue()
                                ,'smap1.negocio'      : _fieldByLabel('NEGOCIO').getValue()
                                ,'smap1.tipocot'      : 'I'
                                ,'smap1.cdsisrol'     : _p28_smap1.cdsisrol
                                ,'smap1.cdusuari'     : _p28_smap1.cdusuari
                            }
                            ,success : function(response)
                            {
                                _p28_formDescuento.setLoading(false);
                                var json = Ext.decode(response.responseText);
                                debug('### cargar rango descuento ramo 5:',json);
                                if(json.exito)
                                {
                                    for(var i=0;i<arrDesc.length;i++)
                                    {
                                        arrDesc[i].minValue=100*(json.smap1.min-0);
                                        arrDesc[i].maxValue=100*(json.smap1.max-0);
                                        arrDesc[i].isValid();
                                        debug('min:',arrDesc[i].minValue);
                                        debug('max:',arrDesc[i].maxValue);
                                        arrDesc[i].setReadOnly(false);
                                    }
                                }
                                else
                                {
                                    for(var i=0;i<arrDesc.length;i++)
                                    {
                                        arrDesc[i].minValue=0;
                                        arrDesc[i].maxValue=0;
                                        arrDesc[i].setValue(0);
                                        arrDesc[i].isValid();
                                        arrDesc[i].setReadOnly(true);
                                    }
                                    mensajeError(json.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                _p28_formDescuento.setLoading(false);
                                for(var i=0;i<arrDesc.length;i++)
                                {
                                    arrDesc[i].minValue=0;
                                    arrDesc[i].maxValue=0;
                                    arrDesc[i].setValue(0);
                                    arrDesc[i].isValid();
                                    arrDesc[i].setReadOnly(true);
                                }
                                errorComunicacion();
                            }
                        });
                    }
                    
                    try {
                       gridTarifas.down('button[disabled=false]').focus(false, 1000);
                    } catch(e) {
                        debug(e);
                    }
                }
                else
                {
                    _p28_bloquear(false);
                    mensajeError('Error al cotizar:<br/>'+json.error);
                }
            }
            ,failure  : function(response)
            {
                panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_p28_cotizar');
}

function _p28_bloquear(b)
{
    debug('>_p28_bloquear:',b);
    
    var comps=Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p28_form'));
    for(var i=0;i<comps.length;i++)
    {
        comps[i].setReadOnly(b);
    }
    
    if(TipoSituacion.ServicioPublicoAuto==_p28_smap1.cdtipsit && b==false){
	    Ext.ComponentQuery
	    .query('[name=parametros.pv_otvalor36],[name=parametros.pv_otvalor37],[name=parametros.pv_otvalor38],[name=parametros.pv_otvalor34]')
	    .forEach(function(it,idx){
	        it.setReadOnly(!RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol));
	    });
    }
    
    _fieldById('_p28_botonera').setDisabled(b);
    _fieldById('_p28_botonCargar').setDisabled(b);
    
    if(b)
    {
    }
    else
    {
        try {
            _fieldByName('nmpoliza').focus();
        } catch(e) {
            debug(e);
        }
    }
    
    if(_p28_smap1.cdramo+'x'=='5x'&&_p28_smap1.cdsisrol=='EJECUTIVOCUENTA')
    {
        var agente = _fieldByLabel('AGENTE');
        agente.setValue(_p28_smap1.cdagente);
        agente.setReadOnly(true);
        _p28_ramo5AgenteSelect(agente,_p28_smap1.cdagente);
    }
    
    _p28_recuperarClienteTramite();
    
    debug('<_p28_bloquear');
}

function _p28_ramo5AgenteSelect(comp,records)
{
    debug('_p28_ramo5AgenteSelect comp,records:',comp,records,'.');
    cdagente = typeof records == 'string' ? records : records[0].get('key');
    debug('>_p28_ramo5AgenteSelect cdagente:',cdagente);
    Ext.Ajax.request(
    {
        url     : _p28_urlCargarCduniecoAgenteAuto
        ,params :
        {
            'smap1.cdagente' : cdagente
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('#### obtener cdunieco agente response:',json);
            if(json.exito)
            {
                if(Ext.isEmpty(_p28_smap1.ntramite))
                {
                    _p28_smap1.cdunieco=json.smap1.cdunieco;
                }
                debug('_p28_smap1:',_p28_smap1);
                Ext.Ajax.request(
                {
                    url     : _p28_urlCargarRetroactividadSuplemento
                    ,params :
                    {
                        'smap1.cdunieco'  : _p28_smap1.cdunieco
                        ,'smap1.cdramo'   : _p28_smap1.cdramo
                        ,'smap1.cdtipsup' : 1
                        ,'smap1.cdusuari' : _p28_smap1.cdusuari
                        ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
                        ,'smap1.tipocot'  : 'I'
                    }
                    ,success : function(response)
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### obtener retroactividad:',json);
                        if(json.exito)
                        {
                            var feini = _fieldByName('feini');
                            var fefin = _fieldByName('fefin');
                            
                            feini.setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,(json.smap1.retroac-0)*-1));
                            feini.setMaxValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.diferi-0));
                            feini.isValid();
                        }
                        else
                        {
                            mensajeError(json.respuesta);
                        }
                    }
                    ,failure : errorComunicacion
                });
            }
            else
            {
                _fieldByLabel('AGENTE').reset();
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _fieldByLabel('AGENTE').reset();
            errorComunicacion();
        }
    });
    
    _fieldByLabel('NEGOCIO').getStore().load(
    {
        params :
        {
            'params.cdagente' : cdagente
        }
    });
    
    ////DXN   
        agregarAgenteDXN();
        administradoraAgenteDXN();
    //////  
    
    debug('<_p28_ramo5AgenteSelect');
}

function _p28_ramo5ClienteChange(combcl)
{
    debug('>_p28_ramo5ClienteChange value:',combcl.getValue());
    
    var nombre  = _fieldLikeLabel('NOMBRE CLIENTE',null,true);
    
    var tipoper;
    if(!Ext.isEmpty(_fieldLikeLabel('TIPO PERSONA',null,true)))
    {tipoper = _fieldByLabel('TIPO PERSONA',null,true);}
    
    var codpos;
    if(!Ext.isEmpty(_fieldLikeLabel('CP CIRCULACI',null,true)))
    {codpos  = _fieldLikeLabel('CP CIRCULACI',null,true);}
  
    
    if( _p28_smap1.cdtipsit+'x' == 'TVx' ){
        if(Ext.isEmpty(nombre)
        )
        {
            return;
        }
        
        //cliente nuevo
        if(combcl.getValue()=='S')
        {
            nombre.reset();
            nombre.setReadOnly(false);
            _p28_recordClienteRecuperado=null;
        }
        //recuperar cliente
        else if(combcl.getValue()=='N' && ( Ext.isEmpty(combcl.semaforo)||combcl.semaforo==false ) )
        {
            nombre.reset();
            nombre.setReadOnly(true);
            
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
                                ,name       : '_p28_recuperaRfc'
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
                                    var rfc=_fieldByName('_p28_recuperaRfc').getValue();
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
                                                'map1.pv_rfc_i'       : rfc
                                                ,'map1.cdtipsit'      : _p28_smap1.cdtipsit
                                                ,'map1.pv_cdtipsit_i' : _p28_smap1.cdtipsit
                                                ,'map1.pv_cdunieco_i' : _p28_smap1.cdunieco
                                                ,'map1.pv_cdramo_i'   : _p28_smap1.cdramo
                                                ,'map1.pv_estado_i'   : 'W'
                                                ,'map1.pv_nmpoliza_i' : _fieldByName('nmpoliza').getValue()
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
                                    _p28_recordClienteRecuperado=record;
                                    nombre.setValue(record.raw.NOMBRECLI);
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
                            model     : '_p28_modeloRecuperado'
                            ,autoLoad : false
                            ,proxy    :
                            {
                                type    : 'ajax'
                                ,url    : _p28_urlRecuperarCliente
                                ,timeout: 2400000
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
    }else {
        
        
        if(Ext.isEmpty(nombre)
            ||Ext.isEmpty(tipoper)
            ||Ext.isEmpty(codpos)
        )
        {
            return;
        }
        
        var fenacim = '';
        if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE',null,true)))
        {
            fenacim = _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE');
        }
        
        
        //cliente nuevo
        if(combcl.getValue()=='S')
        {
            nombre.reset();
            tipoper.reset();
            codpos.reset();
            if(!Ext.isEmpty(fenacim))
            {
                fenacim.reset();
            }
            
            nombre.setReadOnly(false);
            tipoper.setReadOnly(false);
            codpos.setReadOnly(false);
            if(!Ext.isEmpty(fenacim))
            {
                fenacim.setReadOnly(false);
            }
            _p28_recordClienteRecuperado=null;
        }
        //recuperar cliente
        else if(combcl.getValue()=='N' && ( Ext.isEmpty(combcl.semaforo)||combcl.semaforo==false ) )
        {
            nombre.reset();
            tipoper.reset();
            codpos.reset();
            if(!Ext.isEmpty(fenacim))
            {
                fenacim.reset();
            }
            
            nombre.setReadOnly(true);
            tipoper.setReadOnly(true);
            codpos.setReadOnly(true);
            if(!Ext.isEmpty(fenacim))
            {
                fenacim.setReadOnly(true);
            }
            
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
                                ,name       : '_p28_recuperaRfc'
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
                                    var rfc=_fieldByName('_p28_recuperaRfc').getValue();
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
                                                'map1.pv_rfc_i'       : rfc
                                                ,'map1.cdtipsit'      : _p28_smap1.cdtipsit
                                                ,'map1.pv_cdtipsit_i' : _p28_smap1.cdtipsit
                                                ,'map1.pv_cdunieco_i' : _p28_smap1.cdunieco
                                                ,'map1.pv_cdramo_i'   : _p28_smap1.cdramo
                                                ,'map1.pv_estado_i'   : 'W'
                                                ,'map1.pv_nmpoliza_i' : _fieldByName('nmpoliza').getValue()
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
                                    _p28_recordClienteRecuperado=record;
                                    nombre.setValue(record.raw.NOMBRECLI);
                                    tipoper.setValue(record.raw.TIPOPERSONA);
                                    codpos.setValue(record.raw.CODPOSTAL);
                                    if(!Ext.isEmpty(fenacim))
                                    {
                                        fenacim.setValue(record.raw.FENACIMICLI);
                                    }
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
                            model     : '_p28_modeloRecuperado'
                            ,autoLoad : false
                            ,proxy    :
                            {
                                type    : 'ajax'
                                ,url    : _p28_urlRecuperarCliente
                                ,timeout: 2400000
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
    } 
    debug('<_p28_ramo5ClienteChange');
}

function _p28_limpiar()
{
    debug('>_p28_limpiar');
    _fieldByName('nmpoliza').semaforo=true;
    _fieldByName('sucursal').semaforo=true;
    _fieldByName('ramo').semaforo=true;
    _fieldByName('poliza').semaforo=true;
    _fieldById('_p28_form').getForm().reset();
    _fieldById('_p28_form').formOculto.getForm().reset();
    _fieldByName('poliza').semaforo=false;
    _fieldByName('ramo').semaforo=false;
    _fieldByName('sucursal').semaforo=false;
    _fieldByName('nmpoliza').semaforo=false;
    
    _p28_cargarConfig();
    
    if(_p28_smap1.cdramo+'x'=='5x')
    {
        _p28_calculaVigencia();
        
        if(!Ext.isEmpty(_fieldLikeLabel('CLIENTE NUEVO',null,true)))
        {_fieldLikeLabel('CLIENTE NUEVO').setValue('S');}    
        
        if(_p28_smap1.cdsisrol=='EJECUTIVOCUENTA')
        {
            var agente = _fieldByLabel('AGENTE');
            agente.setValue(_p28_smap1.cdagente);
            agente.setReadOnly(true);
            _p28_ramo5AgenteSelect(agente,_p28_smap1.cdagente);
        }
        
        if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
        {
            _fieldLikeLabel('VALOR VEH').minValue=0;
            _fieldLikeLabel('VALOR VEH').maxValue=9999999;
        }
    }
    
    _p28_recuperarClienteTramite();
    
    debug('<_p28_limpiar');
}

function _p28_calculaVigencia(comp,val)
{
    var feini = _fieldByName('feini');
    var fefin = _fieldByName('fefin');
    debug('>_p28_calculaVigencia feini:',feini,'fefin',fefin);
    
    var itemVigencia=_fieldByLabel('VIGENCIA');
    itemVigencia.hide();
    
    if(feini.isValid()&&fefin.isValid())
    {
        var milisDif = Ext.Date.getElapsed(feini.getValue(),fefin.getValue());
        debug('>_p28_calculaVigencia feini:',feini,'fefin',fefin);
        var diasDif  = (milisDif/1000/60/60/24).toFixed(0);
        
        debug('milisDif:',milisDif,'diasDif:',diasDif);
        itemVigencia.setValue(diasDif);
    }
    debug('<_p28_calculaVigencia');
}

function _p28_editar()
{
    debug('>_p28_editar');
    var panelPri    = _fieldById('_p28_panelpri');
    var gridTarifas = _fieldById('_p28_gridTarifas');
    
    _fieldById('_p28_formCesion').destroy();
    panelPri.remove(gridTarifas,true);
    panelPri.doLayout();
    
    _p28_bloquear(false);
    debug('<_p28_editar');
}

function _p28_nueva()
{
    debug('>_p28_nueva');
    _p28_editar();
    _p28_limpiar();
    debug('<_p28_nueva');
}

function _p28_herenciaDescendiente(clave,marca,submarca,modelo,version,record)
{
    debug('>_p28_herenciaDescendiente');
    //var record = clave.findRecord('key',clave.getValue());
    debug('record:',record);
    var splitted=record.get('value').split(' - ');
    debug('splitted:',splitted);
    var clavev    = splitted[0];
    var marcav    = splitted[1];
    var submarcav = splitted[2];
    var modelov   = splitted[3];
    var versionv  = splitted[4];
    
    marca.setValue(marca.findRecord('value',marcav));
    submarca.heredar(true,function()
    {
        submarca.setValue(submarca.findRecord('value',submarcav));
        modelo.heredar(true,function()
        {
            modelo.setValue(modelo.findRecord('value',modelov));
            version.getStore().load(
            {
                params :
                {
                    'params.submarca' : submarca.getValue()
                    ,'params.modelo'  : modelo.getValue()
                }
                ,callback : function()
                {
                    version.setValue(version.findRecord('value',versionv));
                    _p28_cargarSumaAseguradaRamo5(clave,modelo);
                }
            });
        });
    });
    
    debug('<_p28_herenciaDescendiente');
}

function _p28_herenciaAscendente(clave,marca,submarca,modelo,version,callback)
{
    debug('>_p28_herenciaAscendente');
    var versionval = version.getValue();
    
    if(!Ext.isEmpty(versionval))
    {
        var versiondes = version.findRecord('key',versionval).get('value');
        clave.getStore().load(
        {
            params :
            {
                'params.cadena' : versiondes
            }
            ,callback : function()
            {
                
                var valor = versionval
                            +' - '
                            +marca.findRecord('key',marca.getValue()).get('value')
                            +' - '
                            +submarca.findRecord('key',submarca.getValue()).get('value')
                            +' - '
                            +modelo.findRecord('key',modelo.getValue()).get('value')
                            +' - '
                            +version.findRecord('key',versionval).get('value');
                debug('>valor:',valor);
                clave.setValue(clave.findRecord('value',valor));
               
              //ramo 6
              if((_p28_smap1.cdramo+'')!=Ramo.ServicioPublico)
                _p28_cargarSumaAseguradaRamo5(clave,modelo,callback);
              else{
                  callback();
              }
            }
        });
    }
    
    debug('<_p28_herenciaAscendente');
}

function _p28_cargarSumaAseguradaRamo5(clave,modelo,callback)
{
    debug('>_p28_cargarSumaAseguradaRamo5');
    var form=_fieldById('_p28_form');
    form.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _p28_urlCargarSumaAseguradaRamo5
        ,params  :
        {
            'smap1.cdtipsit'  : _p28_smap1.cdtipsit
            ,'smap1.clave'    : clave.getValue()
            ,'smap1.modelo'   : modelo.getValue()
            ,'smap1.cdsisrol' : _p28_smap1.cdsisrol
        }
        ,success : function(response)
        {
            form.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### cargar suma asegurada:',json);
            if(json.exito)
            {
                if(!Ext.isEmpty(json.respuesta))
                {
                 //comentado porque rafa no quiere avisos
                 //mensajeWarning(json.respuesta);
                }
                var sumaseg;
                if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor13',null,true)))
                    {
                        sumaseg= _fieldByName('parametros.pv_otvalor13');
                        sumaseg.setValue(json.smap1.sumaseg);
                        sumaseg.valorCargado=json.smap1.sumaseg;
                    }
                try
                {
                    _fieldByLabel('RESPALDO VALOR').setValue(json.smap1.sumaseg);
                }
                catch(e)
                {
                    debugError('error al guardar respaldo valor',e);
                }
                
                 _p28_cargarRangoValorRamo5(callback);
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            form.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p28_cargarSumaAseguradaRamo5');
}

function _p28_cargar(boton)
{
    var nmpoliza = _fieldByName('nmpoliza').getValue();
    var valido   = !Ext.isEmpty(nmpoliza);
    if(!valido)
    {
        mensajeWarning('Introduce un n&uacute;mero v&aacute;lido');
    }
    
    if(valido)
    {
        var panelpri = _fieldById('_p28_panelpri');
        panelpri.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p28_urlCargar
            ,params  :
            {
                'smap1.nmpoliza'    : nmpoliza
                ,'smap1.cdramo'     : _p28_smap1.cdramo
                ,'smap1.cdunieco'   : _p28_smap1.cdunieco
                ,'smap1.cdtipsit'   : _p28_smap1.cdtipsit
                ,'smap1.ntramiteIn' : _NVL(_p28_smap1.ntramite)
            }
            ,success : function(response)
            {
             panelpri.setLoading(false);
             var json=Ext.decode(response.responseText);
             llenandoCampos(json);
            }
            ,failure : function()
            {
                panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
}

function _p28_cargarPoliza(boton)
{   cargarXpoliza = true;
    var sucursal = _fieldByName('sucursal').getValue();
    var ramo = _fieldByName('ramo').getValue();
    var poliza = _fieldByName('poliza').getValue();
    var valido   = !Ext.isEmpty(poliza);// !Ext.isEmpty(ramo) !Ext.isEmpty(sucursal);
    if(!valido)
    {
        mensajeWarning('Introduce los datos necesarios');
    }
    
    if(valido)
    {
        var mask,ck = 'Recuperando p\u00f3liza para renovar';
        
        try
        {
            mask = _maskLocal(ck);
            var panelpri = _fieldById('_p28_panelpri');
            //panelpri.setLoading(true);
            Ext.Ajax.request(
            {
                url      : _p28_urlCargarPoliza
                 ,params  :
                 {
                      'smap1.cdsucursal' : sucursal
                     ,'smap1.cdramo'     : ramo
                     ,'smap1.cdpoliza'   : poliza
                     ,'smap1.tipoflot'   : 'I'
                     ,'smap1.cdtipsit'   : _p28_smap1.cdtipsit
                     ,'smap1.cargaCotiza': 'S'
                 }
                 ,success : function(response)
                 {
                  //panelpri.setLoading(false);
                      mask.close();
                      var ck = 'Decodificando respuesta al recuperar p\u00f3liza para renovar';
                      try
                      {
                          var json=Ext.decode(response.responseText);
                          debug("valoresCampos: ",json);
                          if(Ext.isEmpty(json.smap1.valoresCampos))
                          {
                              throw 'No se encontraron datos para renovar';
                          }
                          var json2=Ext.decode(json.smap1.valoresCampos);
                          json2['success']=true;
                          cdper     = json2.smap1.cdper;   //D00000000111005
                          cdperson  = json2.smap1.cdperson;//530400
                          debug("valoresCampos 2: ",json2);
                          if(!Ext.isEmpty(json2.smap1.mensajeError))
                          {
                              mensajeError(json2.smap1.mensajeError);
                          }
                          if(!Ext.isEmpty(json2.smap1.mensajeAviso))
                          {
                              mensajeInfo(json2.smap1.mensajeAviso);
                              llenandoCampos(json2);
                          }
                          else
                          {
                               llenandoCampos(json2);
                          }
                      }
                      catch(e)
                      {
                          manejaException(e,ck);
                      }
                 }
                ,failure : function()
                {
                 //panelpri.setLoading(false);
                 mask.close();
                 errorComunicacion();
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck,mask);
        }
    }
}

function llenandoCampos(json)
{   
    var panelpri = _fieldById('_p28_panelpri');
    var nmpoliza = _fieldByName('nmpoliza').getValue();
    
    var sucursal = _fieldByName('sucursal').getValue();
    var ramo = _fieldByName('ramo').getValue();
    var poliza = _fieldByName('poliza').getValue();
    
  	//CARGAMOS EL STORE DEL CAMPO AGENTE PARA QUE AL CARGAR UNA COTIZACION SE PUEDA LLENAR EL CAMPO
    try{
    	if(_p28_smap1.cdramo==Ramo.ServicioPublico)	
      		_fieldByLabel('AGENTE',null,true).store.load();
    }catch(e){
    	debugError(e)
    }
    
    debug('### cargar cotizacion:',json);
//      valorRecuperadoValorVehiSigs = Number(json.slist1[0]["parametros.pv_otvalor13"]);
    if(json.success)
    {

        var maestra  = json.slist1[0].ESTADO=='M';
        var fesolici    = Ext.Date.parse(json.smap1.FESOLICI,'d/m/Y');
        var fechaHoy    = Ext.Date.clearTime(new Date());
        var fechaLimite = Ext.Date.add(fechaHoy,Ext.Date.DAY,-1*(json.smap1.diasValidos-0));
        var vencida     = fesolici<fechaLimite;
        debug('fesolici='    , fesolici);
        debug('fechaHoy='    , fechaHoy);
        debug('fechaLimite=' , fechaLimite);
        debug('vencida='     , vencida,'.');

        _p28_limpiar();
        
        if(!Ext.isEmpty(json.smap1.agenteSec) && !Ext.isEmpty(json.smap1.porcenSec))
                          {
                              _fieldLikeLabel('AGENTESEC').semaforo=true;
                              _fieldLikeLabel('AGENTESEC').setValue(json.smap1.agenteSec);
                              _fieldLikeLabel('AGENTESEC').semaforo=false;
                              _p28_nmpolizaChange(_fieldLikeLabel('AGENTESEC'));
                              
                              _fieldLikeLabel('PORCENSEC').semaforo=true;
                              _fieldLikeLabel('PORCENSEC').setValue(json.smap1.porcenSec);
                              _fieldLikeLabel('PORCENSEC').semaforo=false;
                              _p28_nmpolizaChange(_fieldLikeLabel('PORCENSEC'));
                          }

        var iniVig = Ext.Date.parse(json.smap1.FEEFECTO,'d/m/Y').getTime();
        var finVig = Ext.Date.parse(json.smap1.FEPROREN,'d/m/Y').getTime();
        var milDif = finVig-iniVig;
        var diaDif = milDif/(1000*60*60*24);
        
        if(diaDif<0)
        {
              diaDif = diaDif*-1;
        }
        
        debug('diaDif:',diaDif);
      
        /*if(!maestra&&!vencida)
        {
            _fieldByName('feini').setValue(Ext.Date.parse(json.smap1.FEEFECTO,'d/m/Y'));
        }*/
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
    
        _fieldByName('sucursal').semaforo=true;
        _fieldByName('sucursal').setValue(sucursal);
        _fieldByName('sucursal').semaforo=false;

        _fieldByName('ramo').semaforo=true;
        _fieldByName('ramo').setValue(ramo);
        _fieldByName('ramo').semaforo=false;
        
        _fieldByName('poliza').semaforo=true;
        _fieldByName('poliza').setValue(poliza);
        _fieldByName('poliza').semaforo=false;        
        
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
        var primerInciso = new _p28_formModel(json.slist1[0]);
        if(_p28_smap1.cdramo=='5' && _p28_smap1.cdtipsit!=TipoSituacion.TuristaLicencia  /*&& _p28_smap1.cdtipsit!=TipoSituacion.TuristaVehiculo*/)
        {
             if(json.slist1[0].OTVALOR12 == '1' &&  _fieldLikeLabel('TIPO VALOR').findRecord('1','VALOR CONVENIDO')==false)
            {
                _fieldLikeLabel('TIPO VALOR').store.add({key:'1',value:'VALOR CONVENIDO'});
                _fieldLikeLabel('TIPO VALOR').setValue('1');
            }
            else{
             	_fieldLikeLabel('TIPO VALOR').setValue('2');
                }
            primerInciso.set('parametros.pv_otvalor14','S');
        }
        if(_p28_smap1.cdramo=='6' )
        {
            primerInciso.set('parametros.pv_otvalor24','S');
        }
        debug('primerInciso:',primerInciso);
        //leer elementos anidados
        var form      = _fieldById('_p28_form');
        var formItems = Ext.ComponentQuery.query('[fieldLabel]',form);
        debug('formItems:' , formItems);
        var numBlurs  = 0;
        for(var i=0;i<formItems.length;i++)
        {
            var item=formItems[i];
            if(item.anidado == true)
            {
                var numBlursSeguidos = 1;
                debug('contando blur:',item);
                for(var j=i+1;j<formItems.length;j++)
                {
                    if(formItems[j].anidado == true)
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
            form.formOculto.loadRecord(primerInciso);
            debug('form oculto values:',form.formOculto.getValues());
            if(i<numBlurs)
            {
                i=i+1;
                for(var j=0;j<formItems.length;j++)
                {
                    var iItem  = formItems[j]; 
                    var iItem2 = formItems[j+1];
                    debug('iItem2:',iItem2,'store:',iItem2?iItem2.store:'iItem2 no');
                    if(iItem2&&iItem2.anidado==true)
                    {
                        debug('tiene blur y lo hacemos heredar',formItems[j]);
                        iItem2.heredar(true);
                    }
                }
                setTimeout(renderiza,1000);
            }
            else
            {
                panelpri.setLoading(false);
                
                var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p28_fieldsetTatripol'));
                debug('itemsTatripol:',itemsTatripol);
                for(var j in itemsTatripol)
                {
                    var tatri=itemsTatripol[j];
                    tatri.setValue(json.smap1[tatri.name]);
                }
                
                if(_p28_smap1.cdramo=='5'){
                    try {
                        _fieldByName('aux.otvalor08').setValue(json.smap1['aux.otvalor08']);
                        _fieldByName('aux.otvalor09').heredar(true, function(){
                            _fieldByName('aux.otvalor09').setValue(json.smap1['aux.otvalor09']);
                        });
                    } catch(e) {
                        debugError(e);
                    }
                }
                
                if((_p28_smap1.cdramo=='5' && 'TL'.lastIndexOf(_p28_smap1.cdtipsit)==-1) )
                {
                    var clave    = _fieldByName('parametros.pv_otvalor06');
                    var marca    = _fieldByName('parametros.pv_otvalor07');
                    var submarca = _fieldByName('parametros.pv_otvalor08');
                    var modelo   = _fieldByName('parametros.pv_otvalor09');
                    var version;
                    if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor10',null,true)))
                    {
                        version  = _fieldByName('parametros.pv_otvalor10');
                    }
                    var cotiz=function()
                    {
                        form.loadRecord(primerInciso);
                        if(!Ext.isEmpty(primerInciso.raw.CLAVECLI))
                        {
                            if(maestra&&false)
                            {
                                _fieldLikeLabel('NOMBRE CLIENTE').setValue('');
                            }
                            else
                            {
                                _p28_recordClienteRecuperado = new _p28_modeloRecuperado(primerInciso.raw);
                                debug('_p28_recordClienteRecuperado:',_p28_recordClienteRecuperado);
                            
                                var combcl = 'S';
                                if(!Ext.isEmpty(_fieldLikeLabel('CLIENTE NUEVO',null,true)))
                                {combcl = _fieldLikeLabel('CLIENTE NUEVO');}
                                
                                combcl.semaforo = true;
                                combcl.setValue('N');
                                combcl.semaforo = false;
                            }
                        }
                        
                        if(maestra||vencida)
                        {
                            _fieldById('_p28_form').formOculto.getForm().reset();
                        }
                        
                        if(Ext.isEmpty(json.smap1.NTRAMITE)&&!vencida && !cargarXpoliza)
                        {
                            try
                            {
                            	if(_p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo){
                            		_p28_cotizar(!maestra&&!vencida);
                            	}else{
	                                if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
	                            	{
	                                    _fieldLikeLabel('VALOR VEH').valorCargado=_fieldByLabel('RESPALDO VALOR').getValue();
	                            	}
	                                _p28_cargarRangoValorRamo5(function()
	                                {
	                                    _p28_cotizar(!maestra&&!vencida);
	                                });
                            	}
//                             json.slist1[0]["parametros.pv_otvalor13"]=valorRecuperadoValorVehiSigs+0;
//                             json.slist1[0]["parametros.pv_otvalor62"]=valorRecuperadoValorVehiSigs+0;
//                             json.slist1[0].pv_otvalor13=valorRecuperadoValorVehiSigs+0;
//                             Json.slist1[0].PV_OTVALOR13=valorRecuperadoValorVehiSigs+0;
                            
                            }
                            catch(e)
                            {
                                debugError('error al parchar respaldo valor respaldo',e);
                            }
                        }
                        else if(vencida)
                        {
                            _p28_cotizar(!maestra&&!vencida);
                        }
                        else if(!cargarXpoliza)
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
                                            var swExiper = (!Ext.isEmpty(_p28_recordClienteRecuperado)
                                                && Ext.isEmpty(_p28_recordClienteRecuperado.raw.CLAVECLI)
                                                && !Ext.isEmpty(_p28_recordClienteRecuperado.raw.CDIDEPER))? 'N' : 'S' ;
                                            Ext.create('Ext.form.Panel').submit(
                                            {
                                                url             : _p28_urlDatosComplementarios
                                                ,standardSubmit : true
                                                ,params         :
                                                {
                                                    'smap1.cdunieco'  : json.smap1.CDUNIECO
                                                    ,'smap1.cdramo'   : json.smap1.cdramo
                                                    ,'smap1.cdtipsit' : json.smap1.cdtipsit
                                                    ,'smap1.estado'   : 'W'
                                                    ,'smap1.nmpoliza' : json.smap1.nmpoliza
                                                    ,'smap1.ntramite' : json.smap1.NTRAMITE
                                                    ,'smap1.swexiper' : swExiper 
                                                }
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Duplicar'
                                        ,handler : function(bot)
                                        {
                                            bot.up('window').close();
                                            _fieldByName('nmpoliza').setValue('');
                                            _fieldById('_p28_form').formOculto.getForm().reset();
                                        }
                                    }
                                ]
                            }).show());
                        }
                        else if(!Ext.isEmpty(_p28_flujo))
                        {
                           var tramite = 
                           {
                                 flujo :!Ext.isEmpty(_p28_flujo) ?_p28_flujo :null
                           }
                           Ext.Ajax.request(
                                   {
                                       url       : _p28_datosFlujo
                                       ,jsonData : tramite
                                       ,success : function(response)
                                        {
                                           var json=Ext.decode(response.responseText);
                                           if(json.exito)
                                           {
                                               if(json.smap1.CDTIPTRA+'x' == '21x')
                                               {
                                                   cargarXpoliza= true;
                                                   if(json.smap1.NMPOLIZA+'x' != '0X')
                                                   {
                                                       _fieldByName('nmpoliza').semaforo=true;
                                                       _fieldByName('nmpoliza').setValue(json.smap1.NMPOLIZA);
                                                       _fieldByName('nmpoliza').semaforo=false;
                                                       _p28_cotizar();
                                                   }
                                                   else
                                                   {
                                                       _p28_cotizar();
                                                   }
                                               }
                                           }
                                           else
                                           {
                                               mensajeError('Error al obtener datos de flujo');
                                           }
                                        }
                                       ,failure : function()
                                        {
                                           errorComunicacion(null,'Error al enviar datos para flujo');
                                        }
                                   });
                        }
                    };
                    if(_p28_smap1.cdtipsit==TipoSituacion.TuristaVehiculo){
                    	cotiz();
                    }else{
                    	_p28_herenciaAscendente(clave,marca,submarca,modelo,version,cotiz);
                    }
                }
                // ramo 6
               if((_p28_smap1.cdramo=='6' && ( 'AT'==(_p28_smap1.cdtipsit+'')  || 'MC'==(_p28_smap1.cdtipsit+'')) ) ) 
                {
                    var clave    = _fieldByName('parametros.pv_otvalor22');
                    var marca    = _fieldByName('parametros.pv_otvalor02');
                    var submarca = _fieldByName('parametros.pv_otvalor03');
                    var modelo   = _fieldByName('parametros.pv_otvalor04');
                    var version;
                    if(!Ext.isEmpty(_fieldByName('parametros.pv_otvalor05',null,true)))
                    {
                        version  = _fieldByName('parametros.pv_otvalor05');
                    }
                    
                    var cotizar=function(){
                    	form.loadRecord(primerInciso);
                        if(!Ext.isEmpty(primerInciso.raw.CLAVECLI))
                        {
                            if(maestra&&false)
                            {
                                _fieldLikeLabel('NOMBRE CLIENTE').setValue('');
                            }
                            else
                            {
                                _p28_recordClienteRecuperado = new _p28_modeloRecuperado(primerInciso.raw);
                                debug('_p28_recordClienteRecuperado:',_p28_recordClienteRecuperado);
                            
                                var combcl = 'S';
                                if(!Ext.isEmpty(_fieldLikeLabel('CLIENTE NUEVO',null,true)))
                                {combcl = _fieldLikeLabel('CLIENTE NUEVO');}
                                
                                combcl.semaforo = true;
                                combcl.setValue('N');
                                combcl.semaforo = false;
                            }
                        }
                        
                        if(maestra||vencida)
                        {
                            _fieldById('_p28_form').formOculto.getForm().reset();
                        }
                        
                        if(Ext.isEmpty(json.smap1.NTRAMITE)&&!vencida && !cargarXpoliza)
                        {
                            try
                            {
                                if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
                                {
                                  //  _fieldLikeLabel('VALOR VEH').valorCargado=_fieldByLabel('RESPALDO VALOR').getValue();
                                }
                               /* _p28_cargarRangoValorRamo5(function()
                                {
                                    _p28_cotizar(!maestra&&!vencida);
                                });
                                */
                                _p28_cotizar(!maestra&&!vencida);
                            
                            }
                            catch(e)
                            {
                                debugError('error al parchar respaldo valor respaldo',e);
                            }
                        }
                        else if(vencida)
                        {
                            _p28_cotizar(!maestra&&!vencida);
                        }
                        else if(!cargarXpoliza)
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
                                            var swExiper = (!Ext.isEmpty(_p28_recordClienteRecuperado)
                                                && Ext.isEmpty(_p28_recordClienteRecuperado.raw.CLAVECLI)
                                                && !Ext.isEmpty(_p28_recordClienteRecuperado.raw.CDIDEPER))? 'N' : 'S' ;
                                            Ext.create('Ext.form.Panel').submit(
                                            {
                                                url             : _p28_urlDatosComplementarios
                                                ,standardSubmit : true
                                                ,params         :
                                                {
                                                    'smap1.cdunieco'  : json.smap1.CDUNIECO
                                                    ,'smap1.cdramo'   : json.smap1.cdramo
                                                    ,'smap1.cdtipsit' : json.smap1.cdtipsit
                                                    ,'smap1.estado'   : 'W'
                                                    ,'smap1.nmpoliza' : json.smap1.nmpoliza
                                                    ,'smap1.ntramite' : json.smap1.NTRAMITE
                                                    ,'smap1.swexiper' : swExiper 
                                                }
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Duplicar'
                                        ,handler : function(bot)
                                        {
                                            bot.up('window').close();
                                            _fieldByName('nmpoliza').setValue('');
                                            _fieldById('_p28_form').formOculto.getForm().reset();
                                        }
                                    }
                                ]
                            }).show());
                        }
                        else if(!Ext.isEmpty(_p28_flujo))
                        {
                           var tramite = 
                           {
                                 flujo :!Ext.isEmpty(_p28_flujo) ?_p28_flujo :null
                           }
                           Ext.Ajax.request(
                                   {
                                       url       : _p28_datosFlujo
                                       ,jsonData : tramite
                                       ,success : function(response)
                                        {
                                           var json=Ext.decode(response.responseText);
                                           if(json.exito)
                                           {
                                               if(json.smap1.CDTIPTRA+'x' == '21x')
                                               {
                                                   if(json.smap1.NMPOLIZA+'x' != '0X')
                                                   {
                                                       _fieldByName('nmpoliza').semaforo=true;
                                                       _fieldByName('nmpoliza').setValue(json.smap1.NMPOLIZA);
                                                       _fieldByName('nmpoliza').semaforo=false;
                                                       _p28_cotizar();
                                                   }
                                                   else
                                                   {
                                                       _p28_cotizar();
                                                   }
                                               }
                                           }
                                           else
                                           {
                                               mensajeError('Error al obtener datos de flujo');
                                           }
                                        }
                                       ,failure : function()
                                        {
                                           errorComunicacion(null,'Error al enviar datos para flujo');
                                        }
                                   });
                        }
                    }
                    
                    if('MC'!=(_p28_smap1.cdtipsit+'')){
                        _0_obtenerClaveGSPorAuto(cotizar);
                        
                    }else{
                    	cotizar();
                    }
                    
                    
                }
                // fin ramo 6
                
                try{
                	if(_p28_smap1.cdramo==Ramo.AutosResidentes && _p28_smap1.cdtipsit==TipoSituacion.TuristaLicencia){
                    	
                		if(Ext.isEmpty(json.smap1.NTRAMITE)&&!vencida && !cargarXpoliza)
                        {
                            try
                            {
                                if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
                                {
                                  //  _fieldLikeLabel('VALOR VEH').valorCargado=_fieldByLabel('RESPALDO VALOR').getValue();
                                }
                               /* _p28_cargarRangoValorRamo5(function()
                                {
                                    _p28_cotizar(!maestra&&!vencida);
                                });
                                */
                                _p28_cotizar(!maestra&&!vencida);
                            
                            }
                            catch(e)
                            {
                                debugError('error al parchar respaldo valor respaldo',e);
                            }
                        } else if(vencida)
                        {
                            _p28_cotizar(!maestra&&!vencida);
                        }
                        else if(!cargarXpoliza)
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
                                            var swExiper = (!Ext.isEmpty(_p28_recordClienteRecuperado)
                                                && Ext.isEmpty(_p28_recordClienteRecuperado.raw.CLAVECLI)
                                                && !Ext.isEmpty(_p28_recordClienteRecuperado.raw.CDIDEPER))? 'N' : 'S' ;
                                            Ext.create('Ext.form.Panel').submit(
                                            {
                                                url             : _p28_urlDatosComplementarios
                                                ,standardSubmit : true
                                                ,params         :
                                                {
                                                    'smap1.cdunieco'  : json.smap1.CDUNIECO
                                                    ,'smap1.cdramo'   : json.smap1.cdramo
                                                    ,'smap1.cdtipsit' : json.smap1.cdtipsit
                                                    ,'smap1.estado'   : 'W'
                                                    ,'smap1.nmpoliza' : json.smap1.nmpoliza
                                                    ,'smap1.ntramite' : json.smap1.NTRAMITE
                                                    ,'smap1.swexiper' : swExiper 
                                                }
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Duplicar'
                                        ,handler : function(bot)
                                        {
                                            bot.up('window').close();
                                            _fieldByName('nmpoliza').setValue('');
                                            _fieldById('_p28_form').formOculto.getForm().reset();
                                        }
                                    }
                                ]
                            }).show());
                        }
                        else if(!Ext.isEmpty(_p28_flujo))
                        {
                           var tramite = 
                           {
                                 flujo :!Ext.isEmpty(_p28_flujo) ?_p28_flujo :null
                           }
                           Ext.Ajax.request(
                                   {
                                       url       : _p28_datosFlujo
                                       ,jsonData : tramite
                                       ,success : function(response)
                                        {
                                           var json=Ext.decode(response.responseText);
                                           if(json.exito)
                                           {
                                               if(json.smap1.CDTIPTRA+'x' == '21x')
                                               {
                                                   cargarXpoliza= true;
                                                   if(json.smap1.NMPOLIZA+'x' != '0X')
                                                   {
                                                       _fieldByName('nmpoliza').semaforo=true;
                                                       _fieldByName('nmpoliza').setValue(json.smap1.NMPOLIZA);
                                                       _fieldByName('nmpoliza').semaforo=false;
                                                       _p28_cotizar();
                                                   }
                                                   else
                                                   {
                                                       _p28_cotizar();
                                                   }
                                               }
                                           }
                                           else
                                           {
                                               mensajeError('Error al obtener datos de flujo');
                                           }
                                        }
                                       ,failure : function()
                                        {
                                           errorComunicacion(null,'Error al enviar datos para flujo');
                                        }
                                   });
                        }
                    }
                }catch(e){
                	debugError(e);
                }
            }
        };
        panelpri.setLoading(true);
        if( _p28_smap1.cdramo+'x'=='5x'
            &&
            (RolSistema.puedeSuscribirAutos(_p28_smap1.cdsisrol)
            ||
            RolSistema.PromotorAuto === _p28_smap1.cdsisrol)
          )            
        {
            var agente  = _fieldByName('parametros.pv_otvalor01');
            var negocio = _fieldByLabel('NEGOCIO');
            agente.getStore().load(
            {
                params :
                {
                    'params.agente' : primerInciso.get('parametros.pv_otvalor01')
                }
                ,callback : function()
                {
                    agente.setValue(agente.findRecord('key',primerInciso.get('parametros.pv_otvalor01')));
                    if(!Ext.isEmpty(agente.getValue()))
                    {
                        _p28_ramo5AgenteSelect(agente,agente.getValue());
                    }
                    else
                    {
                        _p28_ramo5AgenteSelect(agente,primerInciso.get('parametros.pv_otvalor01'));
                    }
                    renderiza();
                }
            });
        }
        else
        {
            renderiza();
        }
    }
    else
    {
        mensajeError(json.error);
    }   
}

function _p28_cargarRangoValorRamo5(callback)
{
    debug('>_p28_cargarRangoValorRamo5');
    var tipovalor = _fieldByLabel('TIPO VALOR');
    
    var valor,valorCargado,tipovalorval;
    if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
    {
        valor= _fieldLikeLabel('VALOR VEH');
        valorval     = valor.getValue();
        valorCargado = valor.valorCargado;
        tipovalorval = tipovalor.getValue();
    }
    var valido = !Ext.isEmpty(tipovalorval)&&!Ext.isEmpty(valorval)&&!Ext.isEmpty(valorCargado);
    if(valido)
    {
        var panelpri = _fieldById('_p28_panelpri');
        panelpri.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p28_urlCargarParametros
            ,params  :
            {
                'smap1.parametro' : 'RANGO_VALOR'
                ,'smap1.cdramo'   : _p28_smap1.cdramo
                ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
                ,'smap1.clave4'   : tipovalorval
                ,'smap1.clave5'   : _p28_smap1.cdsisrol
            }
            ,success : function(response)
            {
                panelpri.setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('### obtener rango valor:',json);
                if(json.exito)
                {
                    valor.validator=function(value){
                        var r = true;
                        // http://stackoverflow.com/questions/11832914/round-to-at-most-2-decimal-places-in-javascript
                        valormin = Math.round((valorCargado * (1 + (json.smap1.P1VALOR-0))) * 100) / 100;
                        valormax = Math.round((valorCargado * (1 + (json.smap1.P2VALOR-0))) * 100) / 100;
                        debug('valormin:',valormin);
                        debug('valormax:',valormax);

                        if(valorRecuperadoValorVehiSigs>valormax)
                            {valorRecuperadoValorVehiSigs=valormax;}
                        else(valorRecuperadoValorVehiSigs<valormin)
                            {valorRecuperadoValorVehiSigs=valormin;}
                        
                        if(valor.lastValue>valormax)
                         {
                            _fieldLikeLabel('VALOR VEH').setMaxValue(valormax);
//                          _fieldLikeLabel('VALOR VEH').setValue(valormax);
                         }
                        else(valor.lastValue<valormin)
                         {
                            _fieldLikeLabel('VALOR VEH').setMinValue(valormin);
//                          _fieldLikeLabel('VALOR VEH').setValue(valormin);
                         }
                        
                        if(_p28_smap1.cdsisrol =='EJECUTIVOCUENTA')
                        {
                            var me;
                            if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
                            {
                                me = _fieldLikeLabel('VALOR VEH');
                                if(Number(value)>Number(me.maximoTotal)){
                                    valor.setMinValue(me.maximoTotal);
                                    valor.setMaxValue(me.maximoTotal);
                                    valor.setMinValue(false);
                                    valor.setMaxValue(false);
                                    r='Favor de acudir a Mesa de Control para realizar la cotización.';
                                }else if((Number(valormin)>= Number(me.maximoTotal)) && (Number(valormax)>= Number(me.maximoTotal))){
                                    valor.setMinValue(me.maximoTotal);
                                    valor.setMaxValue(me.maximoTotal);
                                }
                            } 
                            else{
                                valor.setMaxValue(valormax);
                                valor.setMinValue(valormin);
                            }
                        }else{
                            valor.setMinValue(valormin);
                            valor.setMaxValue(valormax);
                        }
                       return r;
                    }

                    try
                    {
                        _rangoValorBaseDatos(valor, json);
                    }
                    catch(e)
                    {
                        debugError(e);
                    }
                    valor.isValid();

                    _p28_cargarParametrizacionCoberturas(callback);
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            } 
            ,failure : function()
            {
                panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    debug('<_p28_cargarRangoValorRamo5');
}

function _p28_limitarCoberturasDependientesSumasegRamo5()
{
    var sumaAsegu,suma;
    if(!Ext.isEmpty(_fieldByLabel('VALOR VEHICULO',null,true)))
        {
           sumaAsegu = _fieldLikeLabel('VALOR VEH');
           suma      = sumaAsegu.getValue();
        }
    
    if(!Ext.isEmpty(suma))
    {
        Ext.Ajax.request(
        {
            url      : _p28_urlCargarParametros
            ,params  :
            {
                'smap1.parametro' : 'RANGO_COBERTURAS_DEPENDIENTES'
                ,'smap1.cdramo'   : _p28_smap1.cdramo
                ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
                ,'smap1.clave4'   : _p28_smap1.cdsisrol
            }
            ,success : function(response)
            {
                var json=Ext.decode(response.responseText);
                debug('### obtener rangos coberturas dependientes:',json);
                if(json.exito)
                {
                    var equipoEspecial = 0;
                    if(!Ext.isEmpty(_fieldLikeLabel('SUMA ASEGURADA EQUIPO ESPECIAL',null,true)))
                    {
                        equipoEspecial = _fieldByLabel('SUMA ASEGURADA EQUIPO ESPECIAL');
                        var min            = suma*(1+(json.smap1.P1VALOR-0));
                        var max            = suma*(1+(json.smap1.P2VALOR-0));
                        debug('min:',min,'max:',max);
                        equipoEspecial.setMinValue(min);
                        equipoEspecial.setMaxValue(max);
                        equipoEspecial.isValid();
                    
                        var adaptaciones = _fieldByLabel('SUMA ASEGURADA ADAPTACIONES Y CONVERSIONES');
                        min              = suma*(1+(json.smap1.P3VALOR-0));
                        max              = suma*(1+(json.smap1.P4VALOR-0));
                        debug('min:',min,'max:',max);
                        adaptaciones.setMinValue(min);
                        adaptaciones.setMaxValue(max);
                        adaptaciones.isValid();
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : errorComunicacion
        });
    }
}

function _p28_nmpolizaChange(me)
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

function _p28_clonar()
{
    debug('>_p28_clonar');
    _fieldById('_p28_form').formOculto.getForm().reset();
    _p28_editar();
    _fieldByName('nmpoliza').setValue('');
    debug('<_p28_clonar');
}

function _p28_coberturas()
{
    _p28_storeCoberturas.load(
    {
        params :
        {
            jsonCober_unieco   : _p28_smap1.cdunieco
            ,jsonCober_cdramo  : _p28_smap1.cdramo
            ,jsonCober_estado  : 'W'
            ,jsonCober_nmpoiza : _fieldByName('nmpoliza').getValue()
            ,jsonCober_cdplan  : _p28_selectedCdplan
            ,jsonCober_cdcia   : '20'
            ,jsonCober_situa   : _p28_selectedNmsituac
        }
    });
    _fieldById('_p28_gridCoberturas').setTitle('Plan ' + _p28_selectedDsplan);
    _p28_windowCoberturas.show();
    centrarVentanaInterna(_p28_windowCoberturas);
}

function _p28_tarifaSelect(selModel, record, row, column, eOpts)
{
    var gridTarifas = _fieldById('_p28_gridTarifas').down('grid');
    if(column>0)
    {
        column = (column * 2) -1;
    }
    debug('( column * 2 )-1:',column);
    var columnName=gridTarifas.columns[column].dataIndex;
    debug('record',record);
    debug('columnName',columnName);
    if(columnName=='DSPERPAG')
    {
        debug('DSPERPAG');
        _fieldById('_p28_botonCoberturas').setDisabled(true);
        _fieldById('_p28_botonComprar').setDisabled(true);
        _fieldById('_p28_botonImprimir').setDisabled(true);
        _fieldById('_p28_botonEnviar').setDisabled(true);
        _fieldById('_p28_botonDetalles').setDisabled(true);
    }
    else
    {
        // M N P R I M A X
        //0 1 2 3 4 5 6 7
        _p28_selectedCdperpag = record.get("CDPERPAG");
        _p28_selectedCdplan   = columnName.substr(7);
        _p28_selectedDsplan   = record.get("DSPLAN"+_p28_selectedCdplan);
        _p28_selectedNmsituac = record.get("NMSITUAC");
        debug('_p28_selectedCdperpag' , _p28_selectedCdperpag);
        debug('_p28_selectedCdplan'   , _p28_selectedCdplan);
        debug('_p28_selectedDsplan'   , _p28_selectedDsplan);
        debug('_p28_selectedNmsituac' , _p28_selectedNmsituac);
        
        _fieldById('_p28_botonCoberturas').setDisabled(false);
        _fieldById('_p28_botonComprar').setDisabled(false);
        _fieldById('_p28_botonImprimir').setDisabled(false);
        _fieldById('_p28_botonEnviar').setDisabled(false);
        _fieldById('_p28_botonDetalles').setDisabled(false);
        /*if( Number(_p28_selectedCdperpag) == 1 && (_p28_selectedCdplan=="3A" || _p28_selectedCdplan =="4L" || _p28_selectedCdplan =="5B"))
        {
            debug(".D.");
            debug('DSPERPAG');  
            _fieldById('_p28_botonCoberturas').setDisabled(true);
            _fieldById('_p28_botonComprar').setDisabled(true);
            _fieldById('_p28_botonImprimir').setDisabled(true);
            _fieldById('_p28_botonEnviar').setDisabled(true);
            _fieldById('_p28_botonDetalles').setDisabled(true);
        }*/
    }
}

function _p28_comprar()
{
    debug('comprar');
    var panelPri = _fieldById('_p28_panelpri');
    panelPri.setLoading(true);
    var nombreTitular = '';
    
    if(!cargarXpoliza)
    {
      cdper      =   Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.CDIDEPER;
      cdperson   =   Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.CLAVECLI;
    }
    
    var paramsComprar =
    {
        comprarNmpoliza        : _fieldByName('nmpoliza').getValue()
        ,comprarCdplan         : _p28_selectedCdplan
        ,comprarCdperpag       : _p28_selectedCdperpag
        ,comprarCdramo         : _p28_smap1.cdramo
        ,comprarCdciaaguradora : '20'
        ,comprarCdunieco       : _p28_smap1.cdunieco
        ,cdtipsit              : _p28_smap1.cdtipsit
        ,'smap1.fechaInicio'   : Ext.Date.format(_fieldByName('feini').getValue(),'d/m/Y')
        ,'smap1.fechaFin'      : Ext.Date.format(_fieldByName('fefin').getValue(),'d/m/Y')
        ,'smap1.ntramite'      : _p28_smap1.ntramite
        ,'smap1.nmorddomCli'   : Ext.isEmpty(_p28_recordClienteRecuperado) ? '' : _p28_recordClienteRecuperado.raw.NMORDDOM
        ,'smap1.cdagenteExt'   : _p28_smap1.cdramo+'x'=='5x' ? _fieldByLabel('AGENTE').getValue() : ''
        ,'smap1.cdpersonCli'   : cdperson
        ,'smap1.cdideperCli'   : cdper
    };
    
    if(!Ext.isEmpty(_p28_flujo))
    {
        paramsComprar['flujo.cdtipflu']  = _p28_flujo.cdtipflu;
        paramsComprar['flujo.cdflujomc'] = _p28_flujo.cdflujomc;
        paramsComprar['flujo.tipoent']   = _p28_flujo.tipoent;
        paramsComprar['flujo.claveent']  = _p28_flujo.claveent;
        paramsComprar['flujo.webid']     = _p28_flujo.webid;
        paramsComprar['flujo.ntramite']  = _p28_flujo.ntramite;
        paramsComprar['flujo.status']    = _p28_flujo.status;
        paramsComprar['flujo.cdunieco']  = _p28_flujo.cdunieco;
        paramsComprar['flujo.cdramo']    = _p28_flujo.cdramo;
        paramsComprar['flujo.estado']    = _p28_flujo.estado;
        paramsComprar['flujo.nmpoliza']  = _p28_flujo.nmpoliza;
        paramsComprar['flujo.nmsituac']  = _p28_flujo.nmsituac;
        paramsComprar['flujo.nmsuplem']  = _p28_flujo.nmsuplem;
    }
    
    Ext.Ajax.request(
    {
        url     : _p28_urlComprar
        ,params  : paramsComprar
        ,success : function(response,opts)
        {
            panelPri.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### Comprar:',json);
            if (json.exito)
            {
                if(Ext.isEmpty(_p28_flujo)
                    ||Ext.isEmpty(_p28_flujo.aux)
                    ||_p28_flujo.aux.indexOf('onComprar')==-1
                    ) //si no hay flujo, o no hay auxiliar en flujo, o el auxiliar no contiene la palabra onComprar
                {
                    centrarVentanaInterna(Ext.Msg.show(
                    {
                        title    : 'Tr&aacute;mite generado'
                        ,msg     : 'La cotizaci&oacute;n '+_fieldByName('nmpoliza').getValue()+' se guard&oacute; y no podr&aacute; ser modificada posteriormente'
                        ,buttons : Ext.Msg.OK
                        ,fn      : function()
                        {
                            var swExiper = (!Ext.isEmpty(_p28_recordClienteRecuperado) && Ext.isEmpty(_p28_recordClienteRecuperado.raw.CLAVECLI) && !Ext.isEmpty(_p28_recordClienteRecuperado.raw.CDIDEPER))? 'N' : 'S' ;
                            
                            var paramsDatCom =
                            {
                                'smap1.cdunieco'  : _p28_smap1.cdunieco
                                ,'smap1.cdramo'   : _p28_smap1.cdramo
                                ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
                                ,'smap1.estado'   : 'W'
                                ,'smap1.nmpoliza' : _fieldByName('nmpoliza').getValue()
                                ,'smap1.ntramite' : json.smap1.ntramite
                                ,'smap1.swexiper' : swExiper
                            };
                            
                            if(!Ext.isEmpty(_p28_flujo))
                            {
                                paramsDatCom['flujo.cdtipflu']  = _p28_flujo.cdtipflu;
                                paramsDatCom['flujo.cdflujomc'] = _p28_flujo.cdflujomc;
                                paramsDatCom['flujo.tipoent']   = _p28_flujo.tipoent;  //ACTUAL QUE SE RECUPERARA
                                paramsDatCom['flujo.claveent']  = _p28_flujo.claveent; //ACTUAL QUE SE RECUPERARA
                                paramsDatCom['flujo.webid']     = _p28_flujo.webid;    //ACTUAL QUE SE RECUPERARA
                                paramsDatCom['flujo.ntramite']  = _p28_flujo.ntramite;
                                paramsDatCom['flujo.status']    = _p28_flujo.status;
                                paramsDatCom['flujo.cdunieco']  = _p28_flujo.cdunieco;
                                paramsDatCom['flujo.cdramo']    = _p28_flujo.cdramo;
                                paramsDatCom['flujo.estado']    = _p28_flujo.estado;
                                paramsDatCom['flujo.nmpoliza']  = _p28_flujo.nmpoliza;
                                paramsDatCom['flujo.nmsituac']  = _p28_flujo.nmsituac;
                                paramsDatCom['flujo.nmsuplem']  = _p28_flujo.nmsuplem;
                                paramsDatCom['flujo.aux']       = 'RECUPERAR';
                            }
                            
                            Ext.create('Ext.form.Panel').submit(
                            {
                                url             : _p28_urlDatosComplementarios
                                ,standardSubmit : true
                                ,params         : paramsDatCom
                            });
                        }
                    }));
                }
                else //flujo tiene la palabra onComprar
                {
                    //si el flujo tiene este comodin ejecutaremos un turnado con el status indicado
                    var ck = 'Turnando tr\u00e1mite';
                    try
                    {
                        var status = _p28_flujo.aux.split('_')[1];
                        debug('status para turnar onComprar:',status,'.');
                        
                        _mask(ck);
                        Ext.Ajax.request(
                        {
                            url      : _GLOBAL_COMP_URL_TURNAR
                            ,params  :
                            {
                                'params.CDTIPFLU'   : _p28_flujo.cdtipflu
                                ,'params.CDFLUJOMC' : _p28_flujo.cdflujomc
                                ,'params.NTRAMITE'  : _p28_flujo.ntramite
                                ,'params.STATUSOLD' : _p28_flujo.status
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
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            panelPri.setLoading(false);
            errorComunicacion();
        }
    });
}

function _p28_imprimir()
{
    debug('>_p28_imprimir');
    var me = this;
    var urlRequestImpCotiza = _p28_urlImprimirCotiza
            + '?p_unieco='      + _p28_smap1.cdunieco
            + '&p_ramo='        + _p28_smap1.cdramo
            + '&p_subramo='     + _p28_smap1.cdtipsit
            + '&p_estado=W'
            + '&p_poliza='      + _fieldByName('nmpoliza').getValue()
            + '&p_suplem=0'
            + '&p_cdplan='      + _p28_selectedCdplan
            + '&p_plan='        + _p28_selectedCdplan
            + '&p_perpag='      + _p28_selectedCdperpag
            + '&p_ntramite='    + _p28_smap1.ntramite
            + '&p_cdusuari='    + _p28_smap1.cdusuari
            + '&destype=cache'
            + "&desformat=PDF"
            + "&userid="        + _p28_reportsServerUser
            + "&ACCESSIBLE=YES"
            + "&report="        + _p28_reporteCotizacion
            + "&paramform=no";
    debug('urlRequestImpCotiza:',urlRequestImpCotiza);
    var numRand = Math.floor((Math.random() * 100000) + 1);
    debug(numRand);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
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
                + _p28_urlViewDoc
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
    }).show());
    debug('<_p28_imprimir');
}

function _p28_enviar()
{
    debug('>_p28_enviar');
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Enviar cotizaci&oacute;n'
        ,width       : 550
        ,modal       : true
        ,height      : 150
        ,buttonAlign : 'center'
        ,bodyPadding : 5
        ,items       :
        [
            {
                xtype       : 'textfield'
                ,itemId     : '_p28_idInputCorreos'
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
                text     : 'Enviar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function()
                {
                    var me = this;
                    if (_fieldById('_p28_idInputCorreos').getValue().length > 0
                            &&_fieldById('_p28_idInputCorreos').getValue() != 'Correo(s) separados por ;')
                    {
                        debug('Se va a enviar cotizacion');
                        me.up().up().setLoading(true);
                        Ext.Ajax.request(
                        {
                            url : _p28_urlEnviarCorreo
                            ,params :
                            {
                                to          : _fieldById('_p28_idInputCorreos').getValue()
                                ,urlArchivo : _p28_urlImprimirCotiza
                                             + '?p_unieco='      + _p28_smap1.cdunieco
                                             + '&p_ramo='        + _p28_smap1.cdramo
                                             + '&p_subramo='     + _p28_smap1.cdtipsit
                                             + '&p_estado=W'
                                             + '&p_poliza='      + _fieldByName('nmpoliza').getValue()
                                             + '&p_suplem=0'
                                             + '&p_cdplan='      + _p28_selectedCdplan
                                             + '&p_plan='        + _p28_selectedCdplan
                                             + '&p_perpag='      + _p28_selectedCdperpag
                                             + '&p_ntramite='    + _p28_smap1.ntramite
                                             + '&p_cdusuari='    + _p28_smap1.cdusuari
                                             + '&destype=cache'
                                             + "&desformat=PDF"
                                             + "&userid="        + _p28_reportsServerUser
                                             + "&ACCESSIBLE=YES"
                                             + "&report="        + _p28_reporteCotizacion
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
                                        centrarVentanaInterna(Ext.Msg.show(
                                        {
                                            title : 'Correo enviado'
                                            ,msg : 'El correo ha sido enviado'
                                            ,buttons : Ext.Msg.OK
                                        }));
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
            ,{
                text     : 'Cancelar'
                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                ,handler : function()
                {
                    this.up().up().destroy();
                }
            }
        ]
    }).show());
    _fieldById('_p28_idInputCorreos').focus();
    debug('<_p28_enviar');
}

function _p28_detalles()
{
    debug('>_p28_detalles');
    Ext.Ajax.request(
    {
        url      : _p28_urlDetalleCotizacion
        ,params  :
        {
            'panel1.pv_cdunieco_i'  : _p28_smap1.cdunieco
            ,'panel1.pv_cdramo_i'   : _p28_smap1.cdramo
            ,'panel1.pv_estado_i'   : 'W'
            ,'panel1.pv_nmpoliza_i' : _fieldByName('nmpoliza').getValue()
            ,'panel1.pv_cdperpag_i' : _p28_selectedCdperpag
            ,'panel1.pv_cdplan_i'   : _p28_selectedCdplan
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### detalles:',json);
            if (json.success)
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
                centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    title       : 'Detalles de cotizaci&oacute;n'
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
                                ,startCollapsed : false
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
                }).show());
            }
            else
            {
                mensajeError('Error al obtener detalle');
            }
        }
        ,failure : errorComunicacion
    });
    debug('<_p28_detalles');
}

function _p28_guardarConfig()
{
    debug('>_p28_guardarConfig');
    var params =
    {
        'smap1.cdramo'    : _p28_smap1.cdramo
        ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
        ,'smap1.cdusuari' : _p28_smap1.cdusuari
    };
    var items  = Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p28_panel4Fieldset'));
    var valido = true;
    for(var i=0;i<items.length;i++)
    {
        valido = valido && items[i].isValid();
        params['smap1.valor'+(items[i].name.slice(-2))]=items[i].getValue();
    }
    debug('params:',params);
    
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
        var panelpri = _fieldById('_p28_panelpri');
        panelpri.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p28_urlGuardarConfig
            ,params  : params
            ,success : function(response)
            {
                panelpri.setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('### guardar config:',json);
                if(json.exito)
                {
                    mensajeCorrecto('Configuraci&oacute;n guardada',json.respuesta);
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_p28_guardarConfig');
}

function _p28_cesionClic()
{
    debug('>_p28_cesionClic');
    _fieldById('_p28_formDescuento').windowCesion.show();
    centrarVentanaInterna(_fieldById('_p28_formDescuento').windowCesion);
    debug('<_p28_cesionClic');
}

function _p28_cargarParametrizacionCoberturas(callback)
{
    debug('>_p28_cargarParametrizacionCoberturas callback:',!Ext.isEmpty(callback),'DUMMY');
    
    var _f1_negocio;
    var _f1_tipoServicio;
    var _f1_modelo;
    var _f1_tipoPersona;
    var _f1_submarca;
    var _f1_clavegs;
    
     if(!Ext.isEmpty(_fieldByLabel('NEGOCIO',null,true)))
    { _f1_negocio      = _fieldByLabel('NEGOCIO').getValue();}
    
    if(!Ext.isEmpty(_fieldLikeLabel('TIPO SERVICIO',null,true)))
    { _f1_tipoServicio = _fieldByLabel('TIPO SERVICIO').getValue();}
    else if(_p28_smap1.cdramo==Ramo.ServicioPublico){
        _f1_tipoServicio = '2';
    }
    
    if(!Ext.isEmpty(_fieldLikeLabel('MODELO',null,true)))
    { _f1_modelo       = _fieldByLabel('MODELO').getValue();}
    
    if(!Ext.isEmpty(_fieldLikeLabel('TIPO PERSONA',null,true)))
    { _f1_tipoPersona  = _fieldByLabel('TIPO PERSONA').getValue();}
    
    if(!Ext.isEmpty(_fieldLikeLabel('SUBMARCA',null,true)))
    { _f1_submarca     = _fieldByLabel('SUBMARCA').getValue();}
    
    if(!Ext.isEmpty(_fieldLikeLabel('AUTO',null,true)))
    { _f1_clavegs      = _fieldLikeLabel('AUTO').getValue();}
    else if(!Ext.isEmpty(_fieldLikeLabel('CLAVE GS',null,true)))
    { _f1_clavegs      = _fieldLikeLabel('CLAVE GS').getValue();}
    
    if(_p28_smap1.cdtipsit==TipoSituacion.ServicioPublicoAuto && _fieldByLabel('TIPO DE UNIDAD').getValue()==TipoUnidad.Fronterizo){
        _f1_clavegs='00000';
        _f1_submarca='00000';
        _f1_modelo=Ext.ComponentQuery.query('[fieldLabel*=MODELO][fieldLabel*=FRONTERIZO]')[0].getValue();
        
    }else if(_p28_smap1.cdtipsit==TipoSituacion.ServicioPublicoMicro){
        _f1_clavegs='00000';
        _f1_submarca='00000';
        _f1_modelo=Ext.ComponentQuery.query('[fieldLabel=MODELO]')[0].getValue();
    }
    
    var valido = !Ext.isEmpty(_f1_negocio)
                 &&!Ext.isEmpty(_f1_tipoServicio)
                 &&!Ext.isEmpty(_f1_modelo)
                 &&!Ext.isEmpty(_f1_tipoPersona)
                 &&!Ext.isEmpty(_f1_submarca)
                 &&!Ext.isEmpty(_f1_clavegs);
    
                
    if(valido)
    {
        var _f1_panelpri = _fieldById('_p28_panelpri');
        _f1_panelpri.setLoading(true);
        var params =
        {
            'smap1.cdtipsit'      : _p28_smap1.cdtipsit
            ,'smap1.cdsisrol'     : _p28_smap1.cdsisrol
            ,'smap1.negocio'      : _f1_negocio
            ,'smap1.tipoServicio' : _f1_tipoServicio
            ,'smap1.modelo'       : _f1_modelo
            ,'smap1.tipoPersona'  : _f1_tipoPersona
            ,'smap1.submarca'     : _f1_submarca
            ,'smap1.clavegs'      : _f1_clavegs
        };
        Ext.Ajax.request(
        {
            url      : _p28_urlCargarParamerizacionCoberturas
            ,params  : params
            ,success : function(response)
            {
                _f1_panelpri.setLoading(false);
                var _f1_json=Ext.decode(response.responseText);
                debug('### parametrizacion:',_f1_json);
                if(_f1_json.exito)
                {
                    for(var i=0;i<_f1_json.slist1.length;i++)
                    {
                        var item = _fieldByName('parametros.pv_otvalor'+(('00'+_f1_json.slist1[i].cdatribu).slice(-2)));
                        if(_f1_json.slist1[i].aplica+'x'=='1x')
                        {
                            console.log(item)
                            itt=item
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
                                            debug(me)
                                            var minimo = me.minValue;
                                            var maximo = me.maxValue;
                                            me.store.filterBy(function(record)
                                            {
                                                debug('filtrando record*=',record);
                                                var key=record.get('key')-0;
                                                debug('quitando key=',key,key>=minimo&&key<=maximo,'.');
                                                return key>=minimo&&key<=maximo;
                                            });
                                        }
                                    });
                                    
                                    item.validator=function(value)
                                    {
                                        var valido=true;
                                        if(value+'x'!='x')
                                        {
                                            var value2=this.getStore().findRecord('value',value)
                                            debug('### value:',value2)
                                            debug('### store:',this.getStore())
                                            if(value2==null){
                                                return 'Valor incorrecto';
                                            }
                                            value=this.getStore().findRecord('value',value).get('key')
                                            
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
                                }
                            }
                            itt=item
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
                    
                    if(_p28_smap1.cdtipsit+'x'=='ARx'||_p28_smap1.cdtipsit+'x'=='PPx')
                    {
                        _fieldLikeLabel('CANAD').heredar();
                    }
                }
                else
                {
                    mensajeError(_f1_json.respuesta);
                }
                
            }
            ,failure : function()
            {
                _f1_panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_p28_cargarParametrizacionCoberturas');
}

function _p28_cargarConfig()
{
    debug('>_p28_cargarConfig');
    Ext.Ajax.request(
    {
        url     : _p28_urlCargarConfig
        ,params :
        {
            'smap1.cdramo'    : _p28_smap1.cdramo
            ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
            ,'smap1.cdusuari' : _p28_smap1.cdusuari
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### config:',json);
            
            if(json.exito)
            {
                for(var prop in json.smap1)//json.smap1{parametros.pv_otvalor02:    "0", parametros.pv_otvalor03:"01"...}
                {
                    var cmp = _fieldByName(prop,null,true);
                    if(!Ext.isEmpty(cmp))
                    {
                        if(!Ext.isEmpty(cmp.heredar))
                        {
                            cmp.forceSelection=false;
                        }
                        cmp.setValue(json.smap1[prop]);
                        if(!Ext.isEmpty(cmp.heredar))
                        {
                            cmp.heredar(true,function(me)
                            {
                                me.forceSelection=true;
                                if(me.findRecord('key',me.getValue())==false)
                                {
                                    me.reset();
                                }
                            });
                        }
                    }
                }
                _p28_inicializarTatripol();
               
                _p28_cargarParamerizacionCoberturasRol();
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
    
    debug('<_p28_cargarConfig');
}

function _p28_recuperaObligatorioCamionRamo5()
{
    debug('>_p28_recuperaObligatorioCamionRamo5');
    _fieldLikeLabel('TRACTO').setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p28_urlCargarObligatorioCamionRamo5
        ,params :
        {
            'smap1.clave' : _fieldLikeLabel('CLAVE GS').getValue()
        }
        ,success : function(response)
        {
            _fieldLikeLabel('TRACTO').setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### obligatorio camion:',json);
            _fieldLikeLabel('TRACTO').allowBlank=json.smap1.tipo-0!=13;
            _fieldLikeLabel('TRACTO').isValid();
        }
        ,failure : function()
        {
            _fieldLikeLabel('TRACTO').setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p28_recuperaObligatorioCamionRamo5');
}

function _p28_cargarParamerizacionCoberturasRol()
{
    debug('>_p28_cargarParamerizacionCoberturasRol<');
    var params=
    {
        'smap1.cdtipsit'  : _p28_smap1.cdtipsit
        ,'smap1.cdsisrol' : _p28_smap1.cdsisrol
    };
    Ext.Ajax.request(
    {
        url      : _p28_urlCargarParamerizacionCoberturasRol
        ,params  : params
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### configuracion por rol:',json);
            if(json.exito)
            {
                for(var i=0;i<json.slist1.length;i++)
                {
                    var item = _fieldByName('parametros.pv_otvalor'+(('00'+json.slist1[i].cdatribu).slice(-2)));
                    if(json.slist1[i].aplica+'x'=='1x')
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
                            
                            if(_p28_smap1.cdramo+'x'=='8x')//Hoteles y Restaurantes
                            {
                                 if(_p28_smap1.cdtipsit =='8HO')//Hoteles
                                     {           
                                        _p28_camposEstacionamiento('N');
                                     }
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
                        item.setValue(json.slist1[i].valor);
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
                    for(var i=0;i<json.slist1.length;i++)
                    {
                        aux2 = aux2
                               +json.slist1[i].cdatribu+' - '
                               +'aplica ('+json.slist1[i].aplica
                               +') valor ('+json.slist1[i].valor
                               +')\n';
                    }
                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                    {
                        title   : '[DEBUG] PARAMETRIZACION DE COBERTURAS POR ROL'
                        ,items  :
                        [
                            {
                                xtype   : 'textarea'
                                ,width  : 400
                                ,height : 70
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
}

function _p28_inicializarTatripol(itemsTatripol)
{

    if(Ext.isEmpty(itemsTatripol))
    {
        itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p28_fieldsetTatripol'));
        debug('itemsTatripol:',itemsTatripol);
    }

    for(var i in itemsTatripol)
    {
        var tatriItem = itemsTatripol[i];
        
        if(_p28_smap1.cdramo+'x'=='5x' && tatriItem.fieldLabel=='TIPO CAMBIO')
        {
            if(Ext.isEmpty(_p28_precioDolarDia))
            {
                tatriItem.setLoading(true);
                Ext.Ajax.request(
                {
                    url      : _p28_urlCargarTipoCambioWS
                    ,success : function(response)
                    {
                        var me=_fieldByLabel('TIPO CAMBIO',_fieldById('_p28_fieldsetTatripol'));
                        me.setLoading(false);
                        var json=Ext.decode(response.responseText);
                        debug('### dolar:',json);
                        _p28_precioDolarDia=json.smap1.dolar;
                        me.setValue(_p28_precioDolarDia);
                    }
                    ,failure : function()
                    {
                        _fieldByLabel('TIPO CAMBIO',_fieldById('_p28_fieldsetTatripol')).setLoading(false);
                        errorComunicacion();
                    }
                });
            }
            else
            {
                tatriItem.setValue(_p28_precioDolarDia);
            }
            tatriItem.hide();
        }
        else if(_p28_smap1.cdramo+'x'=='5x' && tatriItem.fieldLabel=='MONEDA' && '|TL|TV|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')!=-1)
        {
            tatriItem.select('2');
        }
        else if(_p28_smap1.cdramo+'x'=='5x' && tatriItem.fieldLabel=='MONEDA' && '|TL|TV|'.lastIndexOf('|'+_p28_smap1.cdtipsit+'|')==-1)
        {
            tatriItem.select('1');
        }
    }
}

function _p28_atributoNacimientoContratante(combo)
{
    var val = 'S';
    
    if(combo != 'S')
    {
        val = combo.getValue();
    }
    
    debug('_p28_atributoNacimientoContratante val:',val,'.');
    
    if(val == 'S')
        {
            if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE',null,true)))
            {
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=false;
            }

        }
    else
        {
           if(!Ext.isEmpty(_fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE',null,true)))
           {
                _fieldLikeLabel('FECHA DE NACIMIENTO DEL CONTRATANTE').allowBlank=true;
           }

        }
}

function _p28_recuperarClienteTramite()
{
    /*
     * Cuando se tiene cdpercli se recupera para ramo 5 cuando exista el combo de cliente nuevo
     * inicio
     */
    if(!Ext.isEmpty(_p28_smap1.cdpercli))
    {
        var ckCdpercli = 'Recuperando cliente de tr\u00e1mite';
        try
        {
            var comboCliente = _fieldLikeLabel('CLIENTE NUEVO',null,true);
            debug('combo cliente nuevo:',comboCliente);
            if(!Ext.isEmpty(comboCliente))
            {
                debug('Entro a recuperar cliente con cdperson:',_p28_smap1.cdpercli);
                _mask(ckCdpercli);
                Ext.Ajax.request(
                {
                    url      : _p28_urlRecuperarCliente
                    ,params  :
                    {
                        'map1.pv_cdperson_i'  : _p28_smap1.cdpercli
                        ,'map1.soloBD'        : 'S'
                        ,'map1.pv_rfc_i'      : ''//rfc
                        ,'map1.cdtipsit'      : _p28_smap1.cdtipsit
                        ,'map1.pv_cdtipsit_i' : _p28_smap1.cdtipsit
                        ,'map1.pv_cdunieco_i' : _p28_smap1.cdunieco
                        ,'map1.pv_cdramo_i'   : _p28_smap1.cdramo
                        ,'map1.pv_estado_i'   : 'W'
                        ,'map1.pv_nmpoliza_i' : _fieldByName('nmpoliza').getValue()
                    }
                    ,success : function(response)
                    {
                        _unmask();
                        var ck = 'Decodificando respuesta al recuperar cliente de tr\u00e1mite';
                        try
                        {
                            var json = Ext.decode(response.responseText);
                            debug('### recuperacion cliente tramite:',json);
                            
                            comboCliente.semaforo = true;
                            comboCliente.setValue('N');
                            comboCliente.setReadOnly(true);
                            comboCliente.semaforo = false;
                            
                            _p28_recordClienteRecuperado = new _p28_modeloRecuperado(json.slist1[0]);
                            debug('_p28_recordClienteRecuperado:',_p28_recordClienteRecuperado);
                            
                            var nombreCliCmp    = _fieldLikeLabel('NOMBRE CLIENTE',null,true)
                                ,tipoPerCmp     = _fieldLikeLabel('TIPO PERSONA',null,true)
                                ,codPosCliCmp   = _fieldLikeLabel('CP CIRCULACI',null,true)
                                ,feNacimiCliCmp = _fieldLikeLabel('FECHA DE NACIMIENTO',null,true);
                            
                            if(!Ext.isEmpty(nombreCliCmp))
                            {
                                nombreCliCmp.setValue(_p28_recordClienteRecuperado.get('NOMBRECLI'));
                                nombreCliCmp.setReadOnly(true);
                            }
                            
                            if(!Ext.isEmpty(tipoPerCmp))
                            {
                                tipoPerCmp.setValue(_p28_recordClienteRecuperado.raw.TIPOPERSONA);
                                tipoPerCmp.setReadOnly(true);
                            }
                            
                            if(!Ext.isEmpty(codPosCliCmp))
                            {
                                codPosCliCmp.setValue(_p28_recordClienteRecuperado.raw.CODPOSTAL);
                                codPosCliCmp.setReadOnly(true);
                            }
                            
                            if(!Ext.isEmpty(feNacimiCliCmp))
                            {
                                feNacimiCliCmp.setValue(_p28_recordClienteRecuperado.raw.FENACIMICLI);
                                feNacimiCliCmp.setReadOnly(true);
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
                        errorComunicacion(null,'Error al recuperar cliente de tr\u00e1mite');
                    }
                });
            }
            
        }
        catch(e)
        {
            manejaException(e,ckCdpercli);
        }
    }
    /*
     * Cuando se tiene cdpercli se recupera para ramo 5 cuando exista el combo de cliente nuevo
     * fin
     */
}

/*
 * Este metodo busca en los otvalor del tramite el numero de la ultima cotizacion
 * realizada para recuperarla automaticamente en pantalla
 */
function _p28_recuperarCotizacionDeTramite()
{
    if(!Ext.isEmpty(_p28_flujo))
    {
        var mask, ck = 'Recuperando cotizaci\u00f3n de tr\u00e1mite';
        
        try
        {
            mask = _maskLocal(ck);
            Ext.Ajax.request(
            {
                url      : _p28_urlRecuperarOtvalorTramiteXDsatribu
                ,params  :
                {
                    'params.ntramite'  : _p28_flujo.ntramite
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
                            if(!Ext.isEmpty(json.params.otvalor))
                            {
                                _fieldByName('nmpoliza').setValue(json.params.otvalor);
                                _p28_cargar();
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
                    mask.close();
                    errorComunicacion(null,'Error al recuperar cotizaci\u00f3n de tr\u00e1mite');
                }
            });
        }
        catch(e)
        {
            manejaException(e,ck,mask);
        }
    }
}

function _p28_actualizarCotizacionTramite(callback)
{
    var ck = 'Registrando cotizaci\u00f3n de tr\u00e1mite';
    try
    {
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p28_urlActualizarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : _p28_flujo.ntramite
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
                                url      : _p28_urlDetalleTramite
                                ,params  :
                                {
                                    'smap1.ntramite'  : _p28_flujo.ntramite
                                    ,'smap1.status'   : _p28_flujo.status
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

function _p28_actualizarSwexiperTramite(callback)
{
    var ck = 'Registrando estado de cliente de tr\u00e1mite';
    try
    {
        var swExiper = (
            !Ext.isEmpty(_p28_recordClienteRecuperado)
            && Ext.isEmpty(_p28_recordClienteRecuperado.raw.CLAVECLI)
            && !Ext.isEmpty(_p28_recordClienteRecuperado.raw.CDIDEPER)
            ) ? 'N' : 'S' ;
    
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p28_urlActualizarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : _p28_flujo.ntramite
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

/*
 * Esta funcion se usa para recuperar una poliza de SIGS para renovacion en ICE
 * solo debe llamarse si: es por flujo, y si no hay una cotizacion anterior
 * si se recupera cotizacion por _p28_recuperarCotizacionDeTramite entonces
 * esta funcion no debe recuperar nada porque seria doble recuperacion, es decir
 * la condicion que tenga _p28_recuperarCotizacionDeTramite debe estar negada aqui
 */
function _p28_recuperarPolizaSIGS()
{
    if(!Ext.isEmpty(_p28_flujo))
    {
        debug('_p28_recuperarPolizaSIGS');
        
        var mask, ck = 'Recuperando cotizaci\u00f3n de tr\u00e1mite para revisar renovaci\u00f3n';
        
        try
        {
            mask = _maskLocal(ck);
            Ext.Ajax.request(
            {
                url      : _p28_urlRecuperarOtvalorTramiteXDsatribu
                ,params  :
                {
                    'params.ntramite'  : _p28_flujo.ntramite
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
                                    url      : _p28_urlRecuperarDatosTramiteValidacion
                                    ,params  : _flujoToParams(_p28_flujo)
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
                                                    
                                                    ck = 'Seteando valores para renovaci\u00f3n';
                                                    
                                                    _fieldByName('sucursal').setValue(jsonDatTram.datosTramite.TRAMITE.RENUNIEXT);
                                                    _fieldByName('ramo').setValue(jsonDatTram.datosTramite.TRAMITE.RENRAMO);
                                                    _fieldByName('poliza').setValue(jsonDatTram.datosTramite.TRAMITE.RENPOLIEX);
                                                    
                                                    _p28_cargarPoliza();
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

function mensajeCorrectoTarifas(titulo,mensaje,funcion)
{
    if(!Ext.isEmpty(funcion))
    {
        var tmpMensajeEmergente=Ext.Msg.show({
            title    : titulo
            ,icon: 'x-message-box-ok' 
            ,msg     : mensaje
            ,buttons : Ext.Msg.OK
            ,fn      : funcion 
        });
    }
    else
    {
        var tmpMensajeEmergente=Ext.Msg.show({
            title    : titulo
            ,icon: 'x-message-box-ok' 
            ,msg     : mensaje
            ,buttons : Ext.Msg.OK 
        });
    }
    centrarVentanaTarifas(tmpMensajeEmergente);
}


function centrarVentanaTarifas(ventana)
{
    try {
        ventana.setPosition(225,1200);
    } catch(e) {
        debug(e);
    }
    return ventana;
}


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
function _0_obtenerClaveGSPorAuto(callback)
{
	
    _fieldByName('parametros.pv_otvalor22').getStore().load(
    {
        params :
        {
            'params.substr' : _fieldByLabel('VERSION').getValue()
        }
        ,callback : function(records)
        {
            var dat=Ext.ComponentQuery.query('[fieldLabel="VERSION"],[fieldLabel="TIPO DE UNIDAD"],[fieldLabel="MARCA"],[fieldLabel="SUBMARCA"],[fieldLabel="MODELO"]')
            for(i in dat){
                debug("->",dat[i].getValue())
                if(Ext.isEmpty(dat[i].getValue())){
                    return;
                }
            }
            
            debug('callback records:',records);
            debug('### ',_fieldByLabel('TIPO DE UNIDAD'),_fieldByLabel('TIPO DE UNIDAD').getValue());
            
            var valor=_fieldByLabel('VERSION').getValue()
                +' - '+_fieldByLabel('TIPO DE UNIDAD').findRecord('key',_fieldByLabel('TIPO DE UNIDAD').getValue()).get('value')
                +' - '+_fieldByLabel('MARCA').findRecord('key',_fieldByLabel('MARCA').getValue()).get('value')
                +' - '+_fieldByLabel('SUBMARCA').findRecord('key',_fieldByLabel('SUBMARCA').getValue()).get('value')
                +' - '+_fieldByLabel('MODELO').findRecord('key',_fieldByLabel('MODELO').getValue()).get('value')
                +' - '+_fieldByLabel('VERSION').findRecord('key',_fieldByLabel('VERSION').getValue()).get('value');
            debug('valor para el auto->:',valor);
            _fieldByName('parametros.pv_otvalor22').setValue(
                _fieldByName('parametros.pv_otvalor22').findRecord('value',valor)
            );
           
            _0_cargarNumPasajerosAuto(callback);
            
        }
    });
    
}

function _0_cargarNumPasajerosAuto(callback)
{
    Ext.Ajax.request(
    {
        url      : _p28_urlCargarAutoPorClaveGS
        ,params  :
        {
            'smap1.cdramo'    : _p28_smap1.cdramo
            ,'smap1.clavegs'  : _fieldByName('parametros.pv_otvalor22').getValue()
            ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
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
            if(callback)
            	callback();
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
}


function _0_obtenerSumaAseguradaRamo6(mostrarError,respetarValue)
{
//     _0_panelPri.setLoading(true);
    var loading_0_obtenerSumaAseguradaRamo6 = _maskLocal();
    Ext.Ajax.request(
    {
        url      : _p28_urlCargarSumaAsegurada
        ,params  :
        {
            'smap1.modelo'    : _fieldByName('parametros.pv_otvalor04').getValue()
                                     .substr(_fieldByName('parametros.pv_otvalor04').getValue().length-4,4)
            ,'smap1.version'  : _fieldByName('parametros.pv_otvalor05').getValue()
            ,'smap1.cdsisrol' : _p28_smap1.cdsisrol
            ,'smap1.cdramo'   : _p28_smap1.cdramo
            ,'smap1.cdtipsit' : _p28_smap1.cdtipsit
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


function administradoraAgenteDXN(){
    try{
        var cdagente=_fieldByLabel('AGENTE').getValue();
        
        _fieldByLabel('ADMINISTRADORA').store.proxy.extraParams['params.cdagente']=cdagente;
        _fieldByLabel('ADMINISTRADORA').store.load(); 
    }catch(e){
        debugError(e)
    }
}

function tipoUnidadFronteriza(){
    //_fieldByName('parametros.pv_otvalor35').maxLength=17;
    
    
    
    
    _fieldByName('parametros.pv_otvalor35').on({
        blur:fronterizos
    });
    
    _fieldByLabel('TIPO DE UNIDAD').on({
        change:function(me,opc){
            // 13 = TIPO UNIDAD FRONTERIZO
            if(me.getValue()==13){
//                 _fieldById('_p28_fieldsetVehiculo').add({
//                     xtype       : 'hiddenfield'
//                     ,name        : 'aux.otvalor01'
//                 })

                //PERMITIMOS EDICION IGUAL QUE EN AF
			    Ext.ComponentQuery
			    .query('[name=parametros.pv_otvalor36],[name=parametros.pv_otvalor37],[name=parametros.pv_otvalor38],[name=parametros.pv_otvalor34]')
			    .forEach(function(it,idx){
			        it.setReadOnly(!RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol));
			    });
                
                _fieldByName('parametros.pv_otvalor22').allowBlank=true;
                _fieldByName('parametros.pv_otvalor02').allowBlank=true;
                _fieldByName('parametros.pv_otvalor03').allowBlank=true;
                _fieldByName('parametros.pv_otvalor04').allowBlank=true;
                _fieldByName('parametros.pv_otvalor05').allowBlank=true;
                
                _fieldByName('parametros.pv_otvalor22').hide();
                _fieldByName('parametros.pv_otvalor02').hide();
                _fieldByName('parametros.pv_otvalor03').hide();
                _fieldByName('parametros.pv_otvalor04').hide();
                _fieldByName('parametros.pv_otvalor05').hide();
                
                _fieldByName('parametros.pv_otvalor35').allowBlank=false;
                _fieldByName('parametros.pv_otvalor36').allowBlank=false;
                _fieldByName('parametros.pv_otvalor37').allowBlank=false;
                _fieldByName('parametros.pv_otvalor38').allowBlank=false;
                
                _fieldByName('parametros.pv_otvalor35').show();
                _fieldByName('parametros.pv_otvalor36').show();
                _fieldByName('parametros.pv_otvalor37').show();
                _fieldByName('parametros.pv_otvalor38').show();
                
            }else{
//                 if(Ext.ComponentQuery.query('[name="aux.otvalor01"]').length!=0)
//                    _fieldById('_p28_fieldsetVehiculo').remove(_fieldByName('aux.otvalor01'));
                _fieldByName('parametros.pv_otvalor22').allowBlank=false;
                _fieldByName('parametros.pv_otvalor02').allowBlank=false;
                _fieldByName('parametros.pv_otvalor03').allowBlank=false;
                _fieldByName('parametros.pv_otvalor04').allowBlank=false;
                _fieldByName('parametros.pv_otvalor05').allowBlank=false;
                
                _fieldByName('parametros.pv_otvalor22').show();
                _fieldByName('parametros.pv_otvalor02').show();
                _fieldByName('parametros.pv_otvalor03').show();
                _fieldByName('parametros.pv_otvalor04').show();
                _fieldByName('parametros.pv_otvalor05').show();
                
                _fieldByName('parametros.pv_otvalor35').allowBlank=true;
                _fieldByName('parametros.pv_otvalor36').allowBlank=true;
                _fieldByName('parametros.pv_otvalor37').allowBlank=true;
                _fieldByName('parametros.pv_otvalor38').allowBlank=true;
                
                _fieldByName('parametros.pv_otvalor35').hide();
                _fieldByName('parametros.pv_otvalor36').hide();
                _fieldByName('parametros.pv_otvalor37').hide();
                _fieldByName('parametros.pv_otvalor38').hide();
            }
            
            var arr=[_fieldByName('parametros.pv_otvalor22'),
            _fieldByName('parametros.pv_otvalor02'),
            _fieldByName('parametros.pv_otvalor03'),
            _fieldByName('parametros.pv_otvalor04'),
            _fieldByName('parametros.pv_otvalor05'),
            _fieldByName('parametros.pv_otvalor25'),
            _fieldByName('parametros.pv_otvalor35'),
            _fieldByName('parametros.pv_otvalor36'),
            _fieldByName('parametros.pv_otvalor37'),
            _fieldByName('parametros.pv_otvalor38')]
            
            arr.forEach(function(it,i){
                if(it.xtype=='combobox'){
                    it.clearValue();
                }else{
                    it.setValue(null);
                }
            });
            try{
            	_fieldByLabel('NEGOCIO',null,true).clearValue();
        	}catch(e){
        		debugError(e);
        	}
        }
    })
    
}

function fronterizos()
{
    var _0_formAgrupados=_fieldByName('datos_generales');
//     if(Ext.isEmpty(_0_formAgrupados.down('[name=parametros.pv_otvalor23').getValue()) || !_0_formAgrupados.down('[name=parametros.pv_otvalor23]').isValid()){
//         mensajeWarning('Debe de capturar primero el C&oacute;digo Postal');
//         return;
//     }
    
    var vim = this.value;
//     if( (this.minLength > 0 && vim.length < this.minLength) || (vim.length < this.minLength || vim.length > this.maxLength) )
//     {
//         if(this.minLength == this.maxLength) {
//             mensajeWarning('La longitud del n&uacute;mero de serie debe ser ' + this.minLength);
//         } else {
//             mensajeWarning('La longitud del n&uacute;mero de serie debe ser entre ' + this.minLength + ' y ' + this.maxLength);
//         }
//         return;
//     }
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
            ,'smap1.tipoveh'  : "1"
            ,'smap1.codpos'   : _0_formAgrupados.down('[name=parametros.pv_otvalor23]').getValue()?_0_formAgrupados.down('[name=parametros.pv_otvalor23]').getValue():"0"
        }
        ,success : function(response)
        {
            _0_formAgrupados.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('nada response:', json);
            if(json.success)
            {
                var precioDolar = _0_formAgrupados.down('[name=parametros.pv_otvalor34]').getValue()-0;
                debug('precioDolar:',precioDolar);
                _0_formAgrupados.down('[name=parametros.pv_otvalor36]').setValue(json.smap1.AUTO_MARCA);
                _0_formAgrupados.down('[name=parametros.pv_otvalor37]').setValue(json.smap1.AUTO_ANIO);
                _0_formAgrupados.down('[name=parametros.pv_otvalor38]').setValue(json.smap1.AUTO_DESCRIPCION);
                _0_formAgrupados.down('[name=parametros.pv_otvalor25]').setMinValue(((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1-(json.smap1.FACTOR_MIN-0)));
                _0_formAgrupados.down('[name=parametros.pv_otvalor25]').setMaxValue(((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1+(json.smap1.FACTOR_MAX-0)));
                _0_formAgrupados.down('[name=parametros.pv_otvalor25]').setValue((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2));
                //_0_formAgrupados.down('[name=parametros.pv_otvalor26]').setValue((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2));
                debug('set min value:',((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1-(json.smap1.FACTOR_MIN-0)));
                debug('set max value:',((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1+(json.smap1.FACTOR_MAX-0)));
                
                Ext.Ajax.request({
                    url     : _0_urlObtieneValNumeroSerie
                    ,params :
                    {
                        'smap1.numSerie'  :  _0_formAgrupados.down('[name=parametros.pv_otvalor35]').getValue()
                        ,'smap1.feini'   :  Ext.ComponentQuery.query('[name=feini]')[0].getValue()
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug(json);
                        if(json.exito!=true)
                        {
                            //if(_0_smap1.cdsisrol!='SUSCRIAUTO')
//                          if(rolesSuscriptores.lastIndexOf('|'+_0_smap1.cdsisrol+'|')==-1)        
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
                if(
                        RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol)
                        ) {
                    // Si no obtuvo datos el servicio "NADA", reseteamos valores:
                   Ext.ComponentQuery.query('[name=parametros.pv_otvalor36]').setValue();
                   Ext.ComponentQuery.query('[name=parametros.pv_otvalor37]').setValue();
                   Ext.ComponentQuery.query('[name=parametros.pv_otvalor38]').setValue();
                   Ext.ComponentQuery.query('[name=parametros.pv_otvalor25]').setValue();
                   Ext.ComponentQuery.query('[name=parametros.pv_otvalor25]').setMinValue();
                   Ext.ComponentQuery.query('[name=parametros.pv_otvalor25]').setMaxValue();
                    
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
}

function obtienefechafinplazo()
{
//  alert('obtienefechafinplazo');
    Ext.Ajax.request(
        {
            url     : _p28_urlCargarDetalleNegocioRamo5
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

function _p28_botonOnCotizarClic (me) {
    debug('_p28_botonOnCotizarClic args:', arguments);
    var ck = 'Enviando datos de cotizaci\u00f3n';
    try {
        Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES + 'VentanaTurnado');
        new VentanaTurnado({
            cdtipflu  : _p28_flujo.cdtipflu,
            cdflujomc : _p28_flujo.cdflujomc,
            tipoent   : _p28_flujo.tipoent,
            claveent  : 0,//_p28_flujo.claveent,
            webid     : 0,//_p28_flujo.webid,
            aux       : _p28_flujoAux.onCotizar,
            ntramite  : _p28_flujo.ntramite,
            status    : _p28_flujo.status,
            cdunieco  : _p28_flujo.cdunieco,
            cdramo    : _p28_flujo.cdramo,
            estado    : _p28_flujo.estado,
            nmpoliza  : _p28_flujo.nmpoliza,
            nmsituac  : _p28_flujo.nmsituac,
            nmsuplem  : _p28_flujo.nmsuplem,
            cdusuari  : _p28_smap1.cdusuari,
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
		debugError(e)
	}
	return listFP;
}

////// funciones //////
</script>
</head>
<body>
    <div id="_p28_divpri" style="height: 1700px;"></div>
</body>
</html>
